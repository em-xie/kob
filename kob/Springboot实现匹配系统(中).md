1、同步两个玩家的位置思路解析
除了地图同步以外、我们还需要同步两个玩家的位置
同步玩家的位置我们可以标记一下、至于谁在A谁在B我们需要在云端确定
确定完之后我们会把每一个玩家的位置传给前端，我们可以傻瓜式的确定a在左下角b在
右上角、我们在存地图的时候需要存一下玩家的id和位置
在game这个类里我们需要加一个player类来维护玩家的位置信息
一般开发思路需要用什么定义什么、先定义需要用到的各种函数
有参构造函数无参构造函数、存一下每个玩家每一次的指令是什么

![12ced28ab24e5852bce7b738e0439b6.png](https://cdn.acwing.com/media/article/image/2022/08/12/167993_4b3502e91a-12ced28ab24e5852bce7b738e0439b6.png)

2、实现了三个棋盘的同步原理
现在有三个棋盘、还有一个在云端
有两个浏览器就是有两个client、状态同步的机制
client向云端发送消息表示这个蛇动了一下、当服务器接收到两个蛇的移动之后
服务器就会把两个蛇移动的信息分别返回给Client1client2
同步给两名玩家、这样我们就实现了三个棋盘的同步



3、初始化一下我们的playerAplayerB
首先我们构造map的时候传入两名玩家的userid、初始化一下我们的playerAplayerB
为了需要访问到我们的player、我们需要写两个函数
后端就可以把两个玩家的信息传过去、前端做出相应修改

联机对战：同步玩家的操作

```
package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @作者：xie
 * @时间：2022/11/12 9:21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer sx;
    private Integer sy;
     // 蛇每个球的方向
    private List<Integer> steps;
}

```

游戏类
一场游戏包含两名玩家

```
    private final Player playerA,playerB;

    public Game(Integer rows, Integer cols, Integer inner_walls_count,Integer idA,Integer idB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
        playerA = new Player(idA,rows-2,1,new ArrayList<>());
        playerB = new Player(idB,1,cols-2,new ArrayList<>());
    }

    public Player getPlayerA(){
        return playerA;
    }

    public Player getPlayerB(){
        return playerB;
    }
```

```
           JSONObject respGame = new JSONObject();
            respGame.put("a_id",game.getPlayerA().getId());
            respGame.put("a_sx",game.getPlayerA().getSx());
            respGame.put("a_sy",game.getPlayerA().getSy());
            respGame.put("b_id",game.getPlayerB().getId());
            respGame.put("b_sx",game.getPlayerB().getSx());
            respGame.put("b_sy",game.getPlayerB().getSy());
            respGame.put("gamemap",game.getG());

            JSONObject respA = new JSONObject();
            respA.put("event", "start-matching");
            respA.put("opponent_username", b.getUsername());
            respA.put("opponent_photo", b.getPhoto());
            respA.put("game", respGame);
            users.get(a.getId()).sendMessage(respA.toJSONString());
            System.out.println(respA.toJSONString());
            JSONObject respB = new JSONObject();
            respB.put("event", "start-matching");
            respB.put("opponent_username", a.getUsername());
            respB.put("opponent_photo", a.getPhoto());
            respB.put("game",respGame);
            users.get(b.getId()).sendMessage(respB.toJSONString());
```

2.前端：接收两名玩家位置以及地图信息

pk类的存储

```
src/store/pk.js

export default {
  state: {
    ...

    gamemap: null,
    a_id: 0,
    a_sx: 0,
    a_sy: 0,
    b_id: 0,
    b_sx: 0,
    b_sy: 0,
  },

  ...

  mutations: {
    ...

    updateGame(state, game) {
        state.gamemap = game.map;
        state.a_id = game.a_id;
        state.a_sx = game.a_sx;
        state.a_sy = game.a_sy;
        state.b_id = game.b_id;
        state.b_sx = game.b_sx;
        state.b_sy = game.b_sy;
    }
  },

  ...
}

```



pk界面
修改pk界面的接收地图相关参数

```
src/views/pk/PkIndexView.vue

<template>
    ...
</template>

<script>
...

export default {
    ...

    setup() {
        ...

        onMounted(() => {
            ...

            // 回调函数：接收到后端信息调用
            socket.onmessage = msg => {
                // 返回的信息格式由后端框架定义，django与spring定义的不一样
                const data = JSON.parse(msg.data);
                if(data.event === "start-matching") {
                    ...

                    setTimeout(() => {
                        store.commit("updateStatus", "playing");
                    }, 200);

                    store.commit("updateGame", data.game);
                }
            }

            ...
        });

        ...
    }
}
</script>

<style scoped>

</style>

、
```



4、什么是线程为什么要用多线程？
Game不能作为单线程来处理、线程：一个人干就是单线程，两个人干就是多线程
涉及到两个线程之间的通信以及加锁的问题
我们需要先把game变成一个支持多线程的类
就变成多线程了、我们需要实现thread类的一个入口函数
alt+insert就可以实现、重载run函数
start函数就是thread函数的一个api、可以另起一个线程来执行这个函数
为了方便我们需要先把我们的game存放到这个类里面
我们的线程就要一步一步等待下一步操作的操作
这里设计到两个线程同时读写一个变量、这样就会有读写冲突、涉及到顺序问题

![083211f255c94ff8a9d454c75db79d1.png](https://cdn.acwing.com/media/article/image/2022/08/12/167993_738a87f91a-083211f255c94ff8a9d454c75db79d1.png)

alt+insert

3.后端：一局游戏的逻辑

Webscoket操作类
将Game作为一局游戏的一个线程，使Game线程启动

```
backend/consumer/WebSocketServer.java

package com.kob.backend.consumer;

...

@Component
// url链接：ws://127.0.0.1:3000/websocket/**
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    ...

    // 将private改成public，因为Game类需要使用
    public static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();

    ...

    private Game game = null;

    ...

    private void startMatching() {
        System.out.println("start matching!");
        matchPool.add(this.user);

        while(matchPool.size() >= 2) {
            ...

            game.createMap();
            // 一局游戏一个线程，会执行game类的run方法
            game.start();

            users.get(a.getId()).game = game;
            users.get(b .getId()).game = game;

            ...
        }
    }

    ...

}


```

游戏类
一局游戏的操作，首先执行run方法。只有读写、写写有冲突，此处关于nextStep，我们会接收前端的nextStep输入 或 bots代码的输入，而且会频繁的读，因此需要加锁。

Game.java

```
backend/consumer/utils/Game.java

package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
    ...

    private Integer nextStepA = null;   // A的操作
    private Integer nextStepB = null;   // B的操作
    private ReentrantLock lock = new ReentrantLock();
    private String status = "playing";  // playing -> finished
    private String loser = "";  // all: 平局，A：A输，B：B输

    ...

    // 在主线程会读两个玩家的操作，并且玩家随时可能输入操作，存在读写冲突
    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    public int[][] getG() {
        ...
    }

    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        ...
    }

    // 画地图
    private boolean draw() {
        ...
    }

    public void createMap() {
        ...
    }

    // 接收玩家的下一步操作
    private boolean nextStep() {
        // 每秒五步操作，因此第一步操作是在200ms后判断是否接收到输入。并给地图初始化时间
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        /*
         * 个人理解：此循环循环了5000ms，也就是5s，前端是一秒移动5步，
         * 后端接收玩家键盘输入是5s内玩家的一个输入，若在一方先输入，
         * 一方还未输入，输入的一方多此操作，以最后一次为准。
         */
        // 因为会读玩家的nextStep操作，因此加锁
        for(int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if(nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return false;
    }

    private void judge() {      // 判断两名玩家操作是否合法

    }

    private void senAllMessage(String message) {
        WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    private void sendMove() {   // 向两名玩家传递移动信息
        // 因为需要读玩家的下一步操作，所以需要加锁
        lock.lock();
        try {
            JSONObject resp = new JSONObject();
            resp.put("event", "move");
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            senAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }
    }

    private void sendResult() {     // 向两名玩家发送游戏结果
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("loser", loser);
        senAllMessage(resp.toJSONString());
    }

    @Override
    public void run() {
        // 一局游戏，地图大小总共13 * 14 = 182 ≈ 200，蛇每三步长一个格子，两条蛇总长度若200，每三步长一格，最多600步 < 1000
        for(int i = 0; i < 1000; i++) {
            // 是否获取到两条蛇的下一步操作
            if(nextStep()) {
                judge();
                if(status.equals("playing")) {
                    sendMove();
                } else {
                    sendResult();
                    break;
                }
            } else {
                status = "finished";
                // 因为读了nextStep操作，因此也要加锁
                lock.lock();
                // try finally是为了出异常也会抛锁
                try {
                    if(nextStepA == null && nextStepB == null) {
                        loser = "all";
                    } else if(nextStepA == null) {
                        loser = "A";
                    } else {
                        loser = "B";
                    }
                } finally {
                    lock.unlock();
                }
                // 游戏结束
                sendResult();
                break;
            }
        }
    }
}


```

4.前端: 发送移动指令给后端

GameMap.js

```
src/assets/scripts/GameMap.js

import { AcGameObject } from "./AcGameObject";
import { Wall } from "./Wall";
import { Snake } from './Snake';

export class GameMap extends AcGameObject {
    ...

    add_listening_events() {
        this.ctx.canvas.focus();
        this.ctx.canvas.addEventListener("keydown", e => {
            let d = -1;
            if(e.key === 'w') d = 0;
            else if(e.key === 'd') d = 1;
            else if(e.key === 's') d = 2;
            else if(e.key === 'a') d = 3;
            // else if(e.key === 'ArrowUp') snake1.set_direction(0);
            // else if(e.key === 'ArrowRight') snake1.set_direction(1);
            // else if(e.key === 'ArrowDown') snake1.set_direction(2);
            // else if(e.key === 'ArrowLeft') snake1.set_direction(3);

            // 若移动了，发送给后端
            if(d >= 0) {
                this.store.state.pk.socket.send(JSON.stringify({
                    event: "move",
                    direction: d,
                }));
            }
        });
    }

    ...
}


```

5.后端处理接收移动的事件

WebSocket操作类

```
backend/consumer/WebSocketServer.java

package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
// url链接：ws://127.0.0.1:3000/websocket/**
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    ...

    private void move(int direction) {
        if(game.getPlayerA().getId().equals(user.getId())) {
            game.setNextStepA(direction);
        } else if(game.getPlayerB().getId().equals(user.getId())) {
            game.setNextStepB(direction);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message: " + message);
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)) {
            startMatching();
        } else if("stop-matching".equals(event)) {
            stopMatching();
        } else if("move".equals(event)) {
            move(data.getInteger("direction"));
        }
    }

    ...
}



```

6.前端接收后端发送过来的移动事件

```
设立GameMap的存储
src/store/pk.js

export default {
  state: {
    ...

    gameObject: null,
  },
  getters: {
  },
  mutations: {
    ...

    updateGameObject(state, gameobject) {
        state.gameObject = gameobject;
    }
  },
  actions: {

  },
  modules: {
  }
}
```

GameMap存储

```
src/components/GameMap.vue

<template>
    ...
</template>

<script>
...

export default {
    setup() {
        ...

        onMounted(() => {
            store.commit(
                "updateGameObject",
                new GameMap(canvas.value.getContext('2d'), parent.value, store)
            );
        });

        ...
    }
}
</script>

<style scoped>

...

</style>


```

Pk页面：接收后端的移动事件

```
src/views/pk/PkIndexView.vue

<template>
    ...
</template>

<script>
...

export default {
    components: {
        PlayGround,
        MatchGround,
    },
    setup() {
        ...

        onMounted(() => {
            ...

            // 回调函数：接收到后端信息调用
            socket.onmessage = msg => {
                // 返回的信息格式由后端框架定义，django与spring定义的不一样
                const data = JSON.parse(msg.data);
                if(data.event === "start-matching") {
                    ...
                } else if(data.event === "move") {
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;
                    snake0.set_direction(data.a_direction);
                    snake1.set_direction(data.b_direction);
                } else if(data.event === "result") {
                    console.log(data);
                }
            }

            ...
        });
        onUnmounted(() => {
            ...
        });
    }
}
</script>

<style scoped>

</style>


```

7.前端：根据后端返回结果将死了的蛇变白

Snake.js
删除前端判断蛇的操作是否有效

```
src/assets/scripts/Snake.js

import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";

export class Snake extends AcGameObject {
    ...

    // 将蛇状态变为走下一步
    next_step() {
        ...

        // 让蛇在下一回合长一个格子
        const k = this.cells.length;
        for(let i = k; i > 0; i--) {
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }

        // 删除前端判断蛇的操作是否有效
    }

    ...
}


```

PkIndexView.vue
根据后端结果将失败的一方颜色变白

```
src/views/pk/PkIndexView.vue

<template>
    ...
</template>

<script>
...

export default {
    ...

    setup() {
        ...

        onMounted(() => {
            ...

            // 回调函数：接收到后端信息调用
            socket.onmessage = msg => {
                // 返回的信息格式由后端框架定义，django与spring定义的不一样
                const data = JSON.parse(msg.data);
                if(data.event === "start-matching") {
                    ...
                } else if(data.event === "move") {
                    ...
                } else if(data.event === "result") {
                    console.log(data);
                    const game = store.state.pk.gameObject;
                    const [snake0, snake1] = game.snakes;

                    if(data.loser === "all" || data.loser === "A") {
                        snake0.status = "die";
                    }
                    if(data.loser === "all" || data.loser === "B") {
                        snake1.status = "die";
                    }
                }
            }

            ...
        });
        onUnmounted(() => {
            ...
        });
    }
}
</script>

<style scoped>

</style>


```

8.后端：将前端的裁判程序移到后端

注意，此地方有两处代码有迷惑性，分别是：
1. 在Player.java的getCells函数里为什么删除的是第0个元素
2. 在Game.java的check_valid函数里为什么不判断最后一个元素是否重合
以上两个答案在下方代码的关键处有解释

Cell.java

```
backend/consumer/utils/Cell.java

package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cell {
    public int x;
    public int y;
}
```

Player.java

```
backend/consumer/utils/Player.java

package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    ...

    // 检查当前会和，蛇的长度是否会增加
    private boolean check_tail_increasing(int step) {
        if(step <= 10) return true;
        return step % 3 == 1;
    }

    public List<Cell> getCells() {
        List<Cell> res = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        res.add(new Cell(x, y));
        int step = 0;
        for(int d : steps) {
            x += dx[d];
            y += dy[d];
            /**
             * 每一步移动都会把蛇头移动到下一个格子(注：蛇头有两个cell，详看前端Snake.js的next_step()与update_move()逻辑)，
             * 若当前长度增加，蛇头正好移到新的一个格子，剩下的蛇身长度不变，因此长度 + 1；若长度不增加，则删除蛇尾
             */
            res.add(new Cell(x, y));
            if(!check_tail_increasing(++step)) {
                /**
                 * 关键：
                 * 为什么此处删除0呢，首先存储蛇身、且判定是否增加、且画蛇的逻辑此时还是在前端，我们只是将
                 * 判断蛇是否撞到 墙和蛇身 移到后端。并且我们在后端保存的是是蛇头的x、y坐标和蛇身相对
                 * 于上一步操作的方向，但是在我们做了第一个操作后蛇尾才是蛇头，意思就是res逆序才是蛇
                 * 头到蛇尾的位置！
                 */
                res.remove(0);
            }
        }
        return res;
    }
}

```

Game.java

```
backend/consumer/utils/Game.java

package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
    ...

    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();
        Cell cell = cellsA.get(n - 1);
        // 如果是墙，则非法
        if(g[cell.x][cell.y] == 1) return false;

        // 遍历A除最后一个Cell
        /**
         * 关键：
         * 首先我在Player中已经解释getCells的函数返回的res是蛇尾到蛇头的位置。
         * 因此以下两个for循环分别判断的是蛇头是否和两条蛇的蛇身重合！
         * 那么为什么不用判断两个蛇头是否重合呢？可能是地图大小为13 * 14，
         * 两个蛇头的位置初始为(1, 1)和(11, 12)，两个蛇头的位置横纵之和分别为偶数
         * 和奇数，因此两个蛇头永远不会走到同一个格子！
         */
        for(int i = 0; i < n - 1; i++) {
            // 和蛇身是否重合
            if(cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y) {
                return false;
            }
        }

        // 遍历B除最后一个Cell
        for(int i = 0; i < n - 1; i++) {
            // 和B蛇身是否重合
            if(cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y) {
                return false;
            }
        }
        return true;
    }

    private void judge() {      // 判断两名玩家操作是否合法
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = check_valid(cellsA, cellsB);
        boolean valibB = check_valid(cellsB, cellsA);

        if(!validA || !valibB) {
            status = "finished";
            if(!validA && !valibB) {
                loser = "all";
            } else if(!validA) {
                loser = "A";
            } else {
                loser = "B";
            }
        }
    }

    private void senAllMessage(String message) {
        ...
    }

    private void sendMove() {   // 向两名玩家传递移动信息
        ...
    }

    private void sendResult() {     // 向两名玩家发送游戏结果
        ...
    }

    @Override
    public void run() {
        ...
    }
}

```

9.结果板的实现

pk信息存储类

```
src/store/pk.js

export default {
  state: {
    ...
    loser: "none",  // all、A、B
  },
  getters: {
  },
  mutations: {
    ...
    updateLoser(state, loser) {
        state.loser = loser;
    }
  },
  actions: {

  },
  modules: {
  }
}
```

ResultBoard.vue
src/components/ResultBoard.vue

```
<template>
    <div class="result-board">
        <div class="result-board-text" v-if="$store.state.pk.loser === 'all'">
            Draw
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.loser === 'A' && $store.state.pk.a_id === parseInt($store.state.user.id)">
            Lose
        </div>
        <div class="result-board-text" v-else-if="$store.state.pk.loser === 'B' && $store.state.pk.b_id === parseInt($store.state.user.id)">
            Lose
        </div>
        <div class="result-board-text" v-else>
            Win
        </div>
        <div class="result-board-btn">
            <button @click="restart" type="button" class="btn btn-warning btn-lg">
                再来!
            </button>
        </div>
    </div>
</template>

<script>
import { useStore } from 'vuex';

export default {
    setup() {
        const store = useStore();

        const restart = () => {
            store.commit("updateStatus", "matching");
            store.commit("updateLoser", 'none');
            store.commit("updateOpponent", {
                username: "我的对手",
                photo: "https://cdn.acwing.com/media/article/image/2022/08/09/1_1db2488f17-anonymous.png",
            });
        }

        return {
            restart,
        }
    }
}

</script>

<style scoped>
div.result-board {
    height: 30vh;
    width: 30vw;
    background-color: rgba(50, 50, 50, 0.5);
    position: absolute;
    top: 20vh;
    left: 35vw;
}

div.result-board-text {
    text-align: center;
    color: white;
    font-size: 50px;
    font-weight: 600;
    font-style: italic;
    padding-top: 5vh;
}

div.result-board-btn {
    padding-top: 7vh;
    text-align: center;
}
</style>

```

Pk页面

```
src/views/pk/PkIndexView.vue

<template>
    ...

    <ResultBoard v-if="$store.state.pk.loser != 'none'" />
</template>

<script>

...

import ResultBoard from '../../components/ResultBoard.vue';

...

export default {
    components: {
        ...

        ResultBoard,
    },
    setup() {

        ...

        onMounted(() => {

            ...

            // 回调函数：接收到后端信息调用
            socket.onmessage = msg => {
                // 返回的信息格式由后端框架定义，django与spring定义的不一样
                const data = JSON.parse(msg.data);
                if(data.event === "start-matching") {
                    ...
                } else if(data.event === "move") {
                    ...
                } else if(data.event === "result") {
                    ...

                    store.commit("updateLoser", data.loser);
                }
            }

            ...
        });
        ...
    }
}
</script>

<style scoped>

</style>


```

对局回放
通过保存对局每个状态的信息实现对局的回放功能

文件结构
backend
    pojo
        Record.java
    mapper
        RecordMapper.java

![2.png](https://cdn.acwing.com/media/article/image/2022/08/12/29231_407121d51a-2.png)

Record.java

```
backend/pojo/Record.java

package com.kob.backend.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer aId;
    private Integer aSx;
    private Integer aSy;
    private Integer bId;
    private Integer bSx;
    private Integer bSy;
    private String aSteps;
    private String bSteps;
    private String map;
    private String loser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createtime;
}


```

RecordMapper.java

```
backend.mapper.RecordMapper.java

package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.Record;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecordMapper extends BaseMapper<Record> {
}
```

WebSocketServer.java
注入RecordMapper，为了保存对局信息

```
backend/consumer/WebSocketServer.java

package com.kob.backend.consumer;

...

import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;

...

@Component
// url链接：ws://127.0.0.1:3000/websocket/**
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    ...

    private static UserMapper userMapper;
    public static RecordMapper recordMapper;

    ...

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    ...

}

```

Player.java
将玩家的蛇的方向偏移量转化成String

```
backend/consumer/utils/Player.java

package com.kob.backend.consumer.utils;

...

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    ...

    public String getStepsString() {
        StringBuilder res = new StringBuilder();
        for(int d : steps) {
            res.append(d);
        }
        return res.toString();
    }
}

```

Game.java
将对局信息保存至数据库

```
backend/consumer/utils/Game.java

package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Record;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {

    ...

    private String getMapString() {
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < cols; j++) {
                res.append(g[i][j]);
            }
        }
        return res.toString();
    }

    private void saveToDataBase() {
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()
        );
        WebSocketServer.recordMapper.insert(record);
    }

    private void sendResult() {     // 向两名玩家发送游戏结果
        ...

        saveToDataBase();
        senAllMessage(resp.toJSONString());
    }

    ...
}


```

