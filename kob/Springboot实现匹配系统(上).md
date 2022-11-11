匹配系统的流程

要实现匹配系统起码要有两个客户端client1，client2,当客户端打开对战页面并开始匹配时，会给后端服务器server发送一个请求，而匹配是一个异步的过程，什么时候返回结果是不可预知的，所以我们要写一个专门的匹配系统，维护一堆用户的集合，当用户发起匹配请求时，请求会先传给后端服务器，然后再传给匹配系统处理，匹配系统会不断地在用户里去筛选，将rating较为相近的的用户匹配到一组。当成功匹配后，匹配系统就会返回结果给springboot的后端服务器，继而返回给客户端即前端。然后我们就能在前端看到匹配到的对手是谁啦。

举个例子，两个客户端请求两个链接，新建两个类：

```
public class WebSocketServer {
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        // 建立连接
        WebSocketServer client1 = new WebSocketServer();
        WebSocketServer client2 = new WebSocketServer();
    }

    @OnClose
    public void onClose() {
        // 关闭链接
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}


```

websocket协议
因为匹配是异步的过程，且需要前后端双向交互，而普通的http协议是单向的，一问一答式的，属于立即返回结果的类型，不能满足我们的异步需求，因此我们需要一个新的协议websocket：不仅客户端可以主动向服务器端发送请求，服务器端也可以主动向客户端发送请求，是双向双通的，且支持异步。简单来说就是客户端向后端发送请求，经过不确定的时间，会返回一次或多次结果给客户端。
基本原理： 每一个ws连接都会在后端维护起来，客户端连接服务器的时候会创建一个WebSocketServer类。每创建一个链接就是new一个WebSocketServer类的实例，所有与链接相关的信息，都会存在这个类里面。

前面逻辑的优化
由于每次刷新都会刷新不同的地图，为了公平起见，应该把地图的生成放在服务器端，然后返回结果给前端。
为了防止作弊，游戏的一系列操作、判断逻辑都应该放在服务器端，前端只是呈现动画。
我们可以从客户端获取输入，也可以通过微服务从代码端获得输入。
简单的流程如下： Game -> Create map -> 返回给客户端 -> 客户端等待匹配waiting (sleep) -> 匹配成功则进行一系列游戏逻辑



准备配置
1. 集成WebSocket
在pom.xml文件中添加依赖：

```
spring-boot-starter-websocket
```

fastjson
添加config/WebSocketConfig配置类

```
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {

        return new ServerEndpointExporter();
    }
}



```

添加consumer/WebSocketServer类

```
package com.popgame.backend.consumer;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        // 建立连接
    }

    @OnClose
    public void onClose() {
        // 关闭链接
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }
}


```

实现后端向前端发送信息，要手写个辅助函数sendMessage:
首先要存储所有链接，因为我们要根据用户Id找到所对应的链接是什么，才可以通过这个链接向前端发请求（全局变量要用static）
其次还要有链接与用户一一对应，每个链接都用一个session维护

需要注意的是：WebSocketServer并不是一个标准的Springboot的组件，不是一个单例模式(每一个类同一时间只能有一个实例，这里每建一个链接都会new一个类，所以不是单例模式)，向里面注入数据库并不像在Controller里一样直接@Autowired，要改成先定义一个static变量,再@Autowired加入到setUsersMapper函数上,如下：

```
package com.kob.backend.consumer;


import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @作者：xie
 * @时间：2022/11/11 14:07
 */
@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    private Session session = null;
    private User user;
//    静态全局变量-对所有的实例都可见 线程安全ConcurrentHashMap
    private static ConcurrentHashMap<Integer,WebSocketServer> users = new ConcurrentHashMap<>();

    private static UserMapper userMapper;

    @Autowired
    public void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper = userMapper;
    }


    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
            this.session = session;
            System.out.println("connected");
            Integer userId = Integer.parseInt(token);
            this.user = userMapper.selectById(userId);
            users.put(userId,this);
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected");
        if(this.user !=null){
            users.remove(this.user.getId());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message!");
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();

    }

    public void sendMessage(String message) {
//        异步锁
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}


```

放行websocket连接

配置config/SecurityConfig

```
@Override
public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/websocket/**");
}
```





前端
onMounted: 当组件被挂载的时候执行的函数
onUnmonted: 当组件被卸载的时候执行的函数
初步调试阶段，我们是将token传进user.id的

store/pk.js:

```

export default {
    state: {
        socket: null,
        status: "matching", //matching表示匹配界面，playing表示对战界面
        opponent_username: "",
        opponent_photo: "",
    },
    getters: {
    },
    //修改数据
    mutations: {
        updateSocket(state,socket) {
            state.socket = socket;
        },
        updateOpponent(state,opponent){
            state.opponent_username = opponent.username;
            state.opponent_photo = opponent.photo;
        },
        updateStatus(state,status){
            state.status = status;
        }
    },
    actions: {


    },
    modules: {
    }
}

```

将pk引入store中

store/index.js

```
import { createStore } from 'vuex'
import ModuleUser from './user'
import ModulePk from './pk'
export default createStore({
  state: {
  },
  getters: {
  },
  mutations: {
  },
  actions: {
  },
  modules: {
    user: ModuleUser,
    pk: ModulePk
  }
})

```

前端与后端建立连接
views/pk/PKIndex.vue

```
<template>
    <PlayGround/>
  </template>
  
  
  <script>
  import PlayGround from '../../components/PlayGround.vue'
  import { onMounted,onUnmounted } from 'vue';
  import { useStore } from 'vuex';
  export default {
      components: {
          PlayGround
      },
      setup(){
        const store = useStore();
        //字符串中有${}表达式操作的话要用``，不能用引号
        const socketUrl = `ws://127.0.0.1:8089/websocket/${store.state.user.id}/`;

        let socket = null;
        onMounted(() => { //当当前页面打开时调用
            socket = new WebSocket(socketUrl); //js自带的WebSocket()
            socket.onopen = () => { //连接成功时调用的函数
                console.log("connected!");
                store.commit("updateSocket",socket);
            }

            socket.onmessage = msg => { //前端接收到信息时调用的函数
                const data = JSON.parse(msg.data); //不同的框架数据定义的格式不一样
                console.log(data);
            }

            socket.onclose = () => { //关闭时调用的函数
                console.log("disconnected!");
            }
        });

        onUnmounted(() => { //当当前页面关闭时调用
            socket.close(); //卸载的时候断开连接
        });
      }
  }
  </script>
  
  <style scoped> 
  </style>
```

至此，前端与后端就可以通过ws互相连接了。

将token改成jwt验证
若使用userId建立ws连接，用户可伪装成任意用户，因此这是不安全的

const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;

添加ws的jwt验证，根据token判断用户是否存在
consumer/utils/JwtAuthenciation.java

```
package com.kob.backend.consumer.utils;

import com.kob.backend.utils.JwtUtil;
import io.jsonwebtoken.Claims;

/**
 * @作者：xie
 * @时间：2022/11/11 15:02
 */
public class JwtAuthentication {
//    静态函数，在外面能用
    public static Integer getUserId(String token) {
        int userId = -1; //-1表示不存在
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userId = Integer.parseInt(claims.getSubject());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userId;
    }
}


```

修改后端
consumer/WebSocketServer.java

如果可以正常解析出jwt token的话表示登录成功，否则登录不成功，直接close

```
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
            this.session = session;

            System.out.println("connected");
            Integer userId = JwtAuthentication.getUserId(token);
            this.user = userMapper.selectById(userId);
            if(this.user != null){
                users.put(userId,this);

            }else {
                this.session.close();
            }
        System.out.println(users);
    }

```

实现前端逻辑

对战界面和匹配界面的切换

views/pk/PKindex.vue

```
<template>

    <PlayGround v-if="$store.state.pk.status === 'playing'" />
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
</template>
```

```

<template>
    <div class="matchGround">
        <div class="row">
            <div class="col-6">
                <div class="user-photo">
                    <img :src="$store.state.user.photo" alt="">
                </div>
                <div class="user-name">
                    {{ $store.state.user.username }}
                </div>
            </div>

            <div class="col-6">

                <div class="user-photo">
                    <img :src="$store.state.pk.opponent_photo" alt="">
                </div>
                <div class="user-name">
                    {{ $store.state.pk.opponent_username }}
                </div>
            </div>
            <div class="col-12" style="text-align:center ;padding-top: 15vh;">
                <button @click="click_match_btn" type="button" class="btn btn-warning btn-lg">{{ match_btn_info }}</button>
            </div>
        </div>
    </div>
</template>



<script>

import {ref} from 'vue'
export default {
    setup() {
        let match_btn_info = ref("开始匹配");
        const click_match_btn = () => {
            if(match_btn_info.value==="开始匹配"){
                match_btn_info.value="取消";
            }else{
                match_btn_info.value = "开始匹配";
            }
        }

        return {
            match_btn_info,
            click_match_btn
        }
    }

}
</script>

<style scoped>
div.matchGround {
    /* 浏览器的宽高 */
    width: 60vw;
    height: 70vh;
    background-color: rgba(50,50,50,50);
    /* 居中 */
    margin: 40px auto;
}
div.user-photo{
    text-align:  center;
    padding-top: 10vh;
}
div.user-photo >img {
    border-radius: 50%;
    width: 20vh;
}

div.user-name{
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    color:white;
    padding-top:2vh;
}
</style>
```

```
<template>
    <PlayGround v-if="$store.state.pk.status === 'playing'"/>
    <MatchGround v-if="$store.state.pk.status === 'matching'" />
  </template>
  
  
  <script>
  import PlayGround from '../../components/PlayGround.vue'
  import MatchGround from '@/components/MatchGround.vue'
  import { onMounted,onUnmounted } from 'vue';
  import { useStore } from 'vuex';
  export default {
      components: {
          PlayGround,
          MatchGround
      },
      setup(){
        
        const store = useStore();
        //字符串中有${}表达式操作的话要用``，不能用引号
        const socketUrl = `ws://127.0.0.1:8089/websocket/${store.state.user.token}/`;

        let socket = null;
        onMounted(() => { 
            //调用updateOpponent函数 pk.js
            store.commit("updateOpponent",{
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png"
            })
```

前端向后端发送请求：store.state.pk.socket.send(字符串);
以开始匹配为例：

```
store.state.pk.socket.send(JSON.stringify({
                    event: "start matching",
                }));
```

后端可以在onMessage那里接收到前端的请求，并且解析传送过来的数据

```
consumer/WebSocketServer.java
```

```
    private void startMatching(){
        System.out.println("start matching!");
        matchPoll.add(this.user);
        while (matchPoll.size()>=2){
            //Iterator 迭代器枚举
            Iterator<User> it = matchPoll.iterator();
            User a = it.next(),b = it.next();
            matchPoll.remove(a);
            matchPoll.remove(b);

            JSONObject respA = new JSONObject();
            respA.put("event", "start-matching");
            respA.put("opponent_username", b.getUsername());
            respA.put("opponent_photo", b.getPhoto());
            //用users获取a的链接
            users.get(a.getId()).sendMessage(respA.toJSONString()); //用这个链接将信息传回给前端

            JSONObject respB = new JSONObject();
            respB.put("event", "start-matching");
            respB.put("opponent_username", a.getUsername());
            respB.put("opponent_photo", a.getPhoto());
            //用users获取b的链接
            users.get(b.getId()).sendMessage(respB.toJSONString()); //用这个链接将信息传回给前端


        }
    }
    private void stopMatching(){
        System.out.println("stop matching!");
        matchPoll.remove(this.user);
    }

    @OnMessage
    public void onMessage(String message, Session session) { //当作路由
        // 从Client接收消息
        System.out.println("receive message!");

        JSONObject data =JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)){
            startMatching();
        }else if("stop-matching".equals(event)){
            stopMatching();
        }
    }

```

用线程安全的set定义匹配池：final private static CopyOnWriteArraySet<Users> matchPoll = new CopyOnWriteArraySet<>();
开始匹配时，将用户放进拼配池里，取消匹配时将用户移除匹配池
匹配过程在目前调试阶段可以简单地两两匹配





后端返回信息给前端后，在前端接受并处理信息
views/PKindex.vue

```
socket.onmessage = msg => { //前端接收到信息时调用的函数
                const data = JSON.parse(msg.data); //不同的框架数据定义的格式不一样
                if (data.event === "start-matching") { //这个这个start-matching是respA或respB返回的
                    //匹配成功,更新对手信息
                    store.commit("updateOpponent",{
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });
                }
            }
```

这样，我们就能初步实现匹配系统了。
匹配成功2s后跳到pk页面,只需要updateStatus即可
注意的是卸载组件的时候要记得把状态改为matching

```
            socket.onmessage = msg => { //前端接收到信息时调用的函数
                const data = JSON.parse(msg.data); //不同的框架数据定义的格式不一样
                if (data.event === "start-matching") { //这个这个start-matching是respA或respB返回的
                    //匹配成功,更新对手信息
                    store.commit("updateOpponent",{
                        username: data.opponent_username,
                        photo: data.opponent_photo,
                    });
                    setTimeout(()=>{
                        store.commit("updateStatus", "playing");
                    },2000); //延时函数，单位是毫秒
                   
                }
            }

            socket.onclose = () => { //关闭时调用的函数
                console.log("disconnected!");
            }
        });
```



解决同步问题
前文也提到过，生成地图，游戏逻辑等与游戏相关的操作都应该放在服务端，不然的话客户每次刷新得到的地图都不一样，游戏的公平性也不能得到保证。因此，我们要将之前在前端写的游戏逻辑全部转移到后端（云端），前端只负责动画的演示即可。

后端实现
首先要在后端创建一个Game类实现游戏流程，其实就是把之前在前端写的js全部翻译成Java就好了

```
consumer/utils/Game.java
```

直接按照之前的gamemap.js搬过去就好了,代码太长这里就不贴了
然后我们在WebSocketServer.java里面调用一下
现阶段直接在局部调用作为调试

```
private void startMatching() {
       ...
        while (matchPoll.size() >= 2) {

           ...
            respA.put("game_map", game.getMark());
           ...
            respB.put("game_map", game.getMark());

        }

    }
```

