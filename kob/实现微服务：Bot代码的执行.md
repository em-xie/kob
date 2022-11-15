![1.png](https://cdn.acwing.com/media/article/image/2022/08/18/29231_aa5209521e-1.png)

代码执行，此项目只支持java代码的执行，用的是joor java 8实现
可扩展为docker实现，设置内存上限，时间，用命令可执行所有语言代码，并具备一定安全性，因为docker与运行环境隔绝

让BotRunning System获得到前端选择的Bot
1.新建Bot执行微服务项目

右键backendcloud->新建->模块

![1.png](https://cdn.acwing.com/media/article/image/2022/08/19/29231_751e90771f-1.png)

```
 <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
            <version>2.7.1</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.24</version>
            <scope>provided</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/org.jooq/joor-java-8 -->
        <dependency>
            <groupId>org.jooq</groupId>
            <artifactId>joor-java-8</artifactId>
            <version>0.9.14</version>
        </dependency>
    </dependencies>

```

3.BotRunningSystem接收前端选择的botId

文件结构

```
botrunningsystem
    config
        RestTemplateConfig.java
        SecurityConfig.java
    controller
        BotRunningController.java
    service
        impl
            BotRunningServiceImpl.java
        BotRunningService.java
    BotRunningSystemApplication.java
```

将BotRunningSystem/Main.java 更名为 BotRunningSystemApplication.java

接口

```
BotRunningService.java

package com.kob.botrunningsystem.service;

public interface BotRunningService {

    String addBot(Integer userId, String botCode, String input);
}
```

接口实现

```
@Service
public class BotRunningServiceImpl implements BotRunningService {

    @Override
    public String addBot(Integer userId, String  botCode, String input) {
        System.out.println("add bot: " + userId + " " + botCode + " " + input);
        return "add bot success";
    }
}
```

控制器
BotRunningController.java

```
@RestController
public class BotRunningController {
    @Autowired
    private BotRunningService botRunningService;

    @PostMapping("/bot/add/")
    public String addBot(@RequestParam MultiValueMap<String, String> data) {
        Integer userID = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        String botCode = data.getFirst("bot_code");
        String input = data.getFirst("input");
        return botRunningService.addBot(userID, botCode, input);
    }
}

```

权限控制(网关)

```
package com.kob.botrunningsystem.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


/**
 * @作者：xie
 * @时间：2022/11/7 13:21
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/bot/add/").hasIpAddress("127.0.0.1")
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated();


    }

}


```

4.前端选择Bot

匹配界面
添加选择操作方式

```
<template>
    <div class="matchground">
        <div class="row">
            <div class="col-4">
                ...
            </div>
            <div class="col-4">
                <div class="user-select-bot">
                    <select class="form-select" aria-label="Default select example" v-model="select_bot">
                        <option value="-1" selected>亲自出马</option>
                        <option v-for="bot in bots" :key="bot.id" :value="bot.id">{{ bot.title }}</option>
                    </select>
                </div>
            </div>
            <div class="col-4">
                ...
            </div>
            <div class="col-12" style="text-align: center; padding-top: 15vh;">
                ...
            </div>
        </div>
    </div>
</template>

<script>
import { ref } from 'vue';
import { useStore } from 'vuex';
import $ from 'jquery';

export default {
    setup() {
        ...

        let bots = ref([]);
        let select_bot = ref("-1");

        const click_match_btn = () => {
            if(match_btn_info.value === "开始匹配") {
                match_btn_info.value = "取消";
                store.state.pk.socket.send(JSON.stringify({
                    event: "start-matching",
                    bot_id: select_bot.value,
                }));
            }

            ...
        }; 

        const refresh_bots = () => {
            $.ajax({
                url: "http://127.0.0.1:3000/user/bot/getlist/",
                type: "get",
                headers: {
                    'Authorization': "Bearer " + store.state.user.token,
                },
                success(resp) {
                    bots.value = resp;
                }
            })
        };
        refresh_bots();
        return {
            ...

            bots,
            select_bot,
        }
    }
}
</script>

<style scoped>
...

div.user-select-bot {
    padding-top: 20vh;
}
div.user-select-bot > select {
    width: 60%;
    margin: 0 auto;
}
</style>


```

5.后端接收bot

BackEnd接收Bot
WebSocketServer接收到匹配请求，将bot传给匹配服务

```
WebSocketServer.java

package com.kob.backend.consumer;

...

@Component
// url链接：ws://127.0.0.1:3000/websocket/**
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    ...

    private void startMatching(Integer botId) {
        System.out.println("start matching!");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        data.add("bot_id", botId.toString());
        restTemplate.postForObject(addPlayerUrl, data, String.class);
    }

    ...

    @OnMessage
    public void onMessage(String message, Session session) {
        ...

        if("start-matching".equals(event)) {
            startMatching(data.getInteger("bot_id"));
        }

        ...
    }

    ...
}


```

Matching System接收Bot
Matching System接收到backend传的botId，将bot传给BotRunningSystem服务

控制器

```
MatchingController.java

package com.kob.matchingsystem.controller;

...

import java.util.Objects;

@RestController
public class MatchingController {
    @Autowired
    private MatchingService matchingService;

    // 参数不能使用普通map，MultiValueMap和普通map的区别时，这个是一个键对应多个值
    @PostMapping("/player/add/")
    public String addPlayer(@RequestParam MultiValueMap<String, String> data) {
        ...

        Integer botId = Integer.parseInt(Objects.requireNonNull(data.getFirst("bot_id")));
        return matchingService.addPlayer(userId, rating, botId);
    }

    ...
}

```

接口

```
MatchingServiceImpl.java

package com.kob.matchingsystem.service.impl;

...

@Service
public class MatchingServiceImpl implements MatchingService {
    public static final MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating, Integer botId) {
        System.out.println("Add Player: " + userId + " " + rating + " " + botId);
        matchingPool.addPlayer(userId, rating, botId);
        return "add player success";
    }

    ...
}

```

匹配池

```
MatchingPool.java

package com.kob.matchingsystem.service.impl.utils;

...

// 匹配池是多线程的
@Component
public class MatchingPool extends Thread {
    ...

    public void addPlayer(Integer userId, Integer rating, Integer botId) {
        // 在多个线程(匹配线程遍历players时，主线程调用方法时)会操作players变量，因此加锁
        lock.lock();
        try {
            players.add(new Player(userId, rating, botId, 0));
        } finally {
            lock.unlock();
        }
    }

    ...
}

```

匹配池的Player

```
Player.java

package com.kob.matchingsystem.service.impl.utils;

...

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer userId;
    private Integer rating;
    private Integer botId;
    private Integer waitingTime;
}
```

匹配池返回结果加上botId

```
package com.kob.matchingsystem.service.impl.utils;

...

// 匹配池是多线程的
@Component
public class MatchingPool extends Thread {
    ...

    private void sendResult(Player a, Player b) {   // 返回匹配结果
        System.out.println("send result: " + a + " " + b);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", a.getUserId().toString());
        data.add("a_bot_id", a.getBotId().toString());
        data.add("b_id", b.getUserId().toString());
        data.add("b_bot_id", b.getBotId().toString());
        restTemplate.postForObject(startGameUrl, data, String.class);
    }

    ...
}

```

BackEnd接收匹配成功返回的botId
StartGameController.java

```
StartGameController.java

package com.kob.backend.controller.pk;

...

import java.util.Objects;

@RestController
public class StartGameController {
    ...

    @PostMapping("/pk/start/game/")
    public String startGame(@RequestParam MultiValueMap<String, String> data) {
        Integer aId = Integer.parseInt(Objects.requireNonNull(data.getFirst("a_id")));
        Integer aBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("a_bot_id")));
        Integer bId = Integer.parseInt(Objects.requireNonNull(data.getFirst("b_id")));
        Integer bBotId = Integer.parseInt(Objects.requireNonNull(data.getFirst("b_bot_id")));
        return startGameService.startGame(aId, aBotId, bId, bBotId);
    }
}


```

```
StartGameService.java

StartGameService.java

package com.kob.backend.service.pk;

public interface StartGameService {

    String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId);

}
StartGameServiceImpl.java

StartGameServiceImpl.java

package com.kob.backend.service.impl.pk;

...

@Service
public class StartGameServiceImpl implements StartGameService {
```



    @Override
    public String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        System.out.println("start gameL: " + aId + " " + bId);
        WebSocketServer.startGame(aId, aBotId, bId, bBotId);
        return "start game success";
    }
```
}
WebSocketServer.java

WebSocketServer.java

package com.kob.backend.consumer;

...

import com.kob.backend.pojo.Bot;
import com.kob.backend.mapper.BotMapper;

@Component
// url链接：ws://127.0.0.1:3000/websocket/**
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    ...
```



    public static BotMapper botMapper;
    
    ...
    
    @Autowired
    public void setBotMapper(BotMapper botMapper) {
        WebSocketServer.botMapper = botMapper;
    }
    
    ...
    
    public static void startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId) {
        User a = userMapper.selectById(aId), b = userMapper.selectById(bId);
        Bot botA = botMapper.selectById(aBotId), botB = botMapper.selectById(bBotId);
    
        Game game = new Game(
                13,
                14,
                20,
                a.getId(),
                botA,
                b.getId(),
                botB);
    
        ...
    }
    
    ...

```
}
Player.java

Player.java

package com.kob.backend.consumer.utils;

...

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private Integer botId;  // -1表示亲自出马，否则表示用AI打
    private String botCode;

    ...

}
```

```
WebSocketServer.java

将RestTemplate变成public，若是代码输入则屏蔽人的输入

WebSocketServer.java

package com.kob.backend.consumer;

...

@Component
// url链接：ws://127.0.0.1:3000/websocket/**
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    ...
```



    public static RestTemplate restTemplate;
    
    private void move(int direction) {
        if(game.getPlayerA().getId().equals(user.getId())) {
            if(game.getPlayerA().getBotId().equals(-1))     // 亲自出马则接收输入
                game.setNextStepA(direction);
        } else if(game.getPlayerB().getId().equals(user.getId())) {
            if(game.getPlayerB().getBotId().equals(-1))
                game.setNextStepB(direction);
        }
    }
    
    ...
}
Game.java

Game.java

package com.kob.backend.consumer.utils;

...

import com.kob.backend.pojo.Bot;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class Game extends Thread {
    ...

    private static final String addBotUrl = "http://127.0.0.1:3002/bot/add/";


    public Game(
            Integer rows,
            Integer cols,
            Integer inner_walls_count,
            Integer idA,
            Bot botA,
            Integer idB,
            Bot botB
    ) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];
    
        Integer botIdA = -1, botIdB = -1;
        String botCodeA = "", botCodeB = "";
    
        if(botA != null) {
            botIdA = botA.getId();
            botCodeA = botA.getContent();
        }
        if(botB != null) {
            botIdB= botB.getId();
            botCodeB = botB.getContent();
        }
    
        playerA = new Player(idA, botIdA, botCodeA, rows - 2, 1, new ArrayList<>());
        playerB = new Player(idB, botIdB, botCodeB, 1, cols - 2, new ArrayList<>());
    }


    private String getInput(Player player) {    // 将当前局面信息编码成字符串
        // 地图#my.sx#my.sy#my操作#you.sx#you.sy#you操作
        Player me, you;
        if(playerA.getId().equals(player.getId())) {
            me = playerA;
            you = playerB;
        } else {
            me = playerB;
            you = playerA;
        }
    
        return getMapString() + "#" +
                me.getSx() + "#" +
                me.getSy() + "#(" +
                me.getStepsString() + ")#" +    // 加()是为了预防操作序列为空
                you.getSx() + "#" +
                you.getSy() + "#(" +
                you.getStepsString() + ")";
    }
    
    private void sendBotCode(Player player) {
        if(player.getBotId().equals(-1)) return;
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", player.getId().toString());
        data.add("bot_code", player.getBotCode());
        data.add("input", getInput(player));
        WebSocketServer.restTemplate.postForObject(addBotUrl, data, String.class);
    }
    
    // 接收玩家的下一步操作
    private boolean nextStep() {
        // 每秒五步操作，因此第一步操作是在200ms后判断是否接收到输入。
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    
        sendBotCode(playerA);
        sendBotCode(playerB);
    
        ...
    }
    
    ...

BotRunning System的实现
思路：

评测器是一个经典的生产者消费者模型，此服务生产者会将任务放进一个队列中，
消费者是单独的一个线程，当有任务就会从队头立即执行；并且关键问题是评测
器的执行代码不能单纯的用sleep 1s去判断是否有任务，这样很影响评测体验，
因此需要用到Condition Variable，当有任务执行，无任务等待。
文件结构

```
matchingsystem
    service
        impl
            utils
                Bot.java
                BotPool.java
                Consumer.java
    utils
        Bot.java
        BotInterface.java
```

Bot的实现
Bot.java

```
package com.kob.botrunningsystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bot {
    Integer userId;
    String botCode;
    String input;
}
BotPoll的实现
```

```
BotPoll的实现
虽然队列没用消息队列，但是因为我们写了条件变量与锁的操作，所以等价于消息队列

BotPool.java

package com.kob.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread {
    // 以下的锁和条件变量加不加static都可以，因为BotPool只有一个
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Queue<Bot> bots = new LinkedList<>();
```



    public void addBot(Integer userId, String botCode, String input) {
        lock.lock();
        try {
            bots.add(new Bot(userId, botCode, input));
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
    
    private void consume(Bot bot) {
    
    }
    
    @Override
    public void run() {
        while(true) {
            lock.lock();
            if(bots.isEmpty()) {
                try {
                    // 若执行了await会自动释放锁
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    lock.unlock();
                    break;
                }
            } else {
                Bot bot = bots.remove();
                lock.unlock();
                // 耗时操作，因此要在释放锁之后执行
                consume(bot);
            }
        }
    }
```
}
BotRunningServiceImpl.java
加任务

BotRunningServiceImpl.java

package com.kob.botrunningsystem.service.impl;

...

import com.kob.botrunningsystem.service.impl.utils.BotPool;

@Service
public class BotRunningServiceImpl implements BotRunningService {
    public static final BotPool botPool = new BotPool();
```



    @Override
    public String addBot(Integer userId, String botCode, String input) {
        System.out.println("add bot: " + userId + " " + botCode + " " + input);
        botPool.addBot(userId, botCode, input);
        return "add bot success";
    }
```
}

BotPool线程的启动
BotRunningSystemApplication.java

package com.kob.botrunningsystem;

...

@SpringBootApplication
public class BotRunningSystemApplication {
    public static void main(String[] args) {
        BotRunningServiceImpl.botPool.start();
        SpringApplication.run(BotRunningSystemApplication.class, args);
    }
}
BotInterface.java
用户写Bot实现的接口

BotInterface.java

package com.kob.botrunningsystem.utils;

public interface BotInterface {
    Integer nextMove(String input);
}

Bot.java
Bot.java

package com.kob.botrunningsystem.utils;

public class Bot implements BotInterface {
```



    @Override
    public Integer nextMove(String input) {
        return 0;   // 向上走
    }
```
}
Consumer的实现
docker与沙箱分别有什么区别

Consumer.java

package com.kob.botrunningsystem.service.impl.utils;

import com.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;

import java.util.UUID;

public class Consumer extends Thread {
    private Bot bot;
```



    public void startTimeout(long timeout, Bot  bot) {
        this.bot = bot;
        this.start();
    
        // 在 程序运行结束后 或 程序在指定timeout时间后还未执行完毕 直接中断代码执行
        try {
            this.join(timeout);
            this.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            this.interrupt();
        }
    }
    
    private String addUid(String code, String uid) {    // 在code中的Bot类名后添加uid
        int k = code.indexOf(" implements BotInterface");
        return code.substring(0, k) + uid + code.substring(k);
    }
    
    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0, 8);
        BotInterface botInterface = Reflect.compile(
                "com.kob.botrunningsystem.utils.Bot" + uid,
                addUid(bot.getBotCode(), uid)
        ).create().get();
    
        Integer direction = botInterface.nextMove(bot.getInput());
        System.out.println("move-direction: " + bot.getUserId() + " " + direction);
    }
```
}
BotPool.java
调用Consumer

BotPool.java

package com.kob.botrunningsystem.service.impl.utils;

...

public class BotPool extends Thread {
```

​    ...

    private void consume(Bot bot) {
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000, bot);
    }
    
    ...
```
}
测试
前端Bug的修改

在游戏结束后，点到其他页面再点回pk页面，结果没有消失

PkIndexView.vue

<template>
    ...
</template>


<script>
...

export default {
    ...
```



    setup() {
        const store = useStore();
        const socketUrl = `ws://127.0.0.1:3000/websocket/${store.state.user.token}/`;
    
        store.commit("updateLoser", 'none');
    
        ...
    }
```
}
</script>

<style scoped>


</style>
将Bot执行结果传给前端
1.BackEnd接收Bot代码的结果

文件结构
backend
    controller
        pk
            ReceiveBotMoveController.java
    service
        impl
            pk
                ReceiveBotMoveServiceImpl.java
        pk
            ReceiveBotMoveService.java
接口
ReceiveBotMoveService.java

package com.kob.backend.service.pk;

public interface ReceiveBotMoveService {
```



    String receiveBotMove(Integer userId, Integer direction);
```
}
WebSocketServer操作类
将game改为public

WebSocketServer.java

package com.kob.backend.consumer;

...

@Component
// url链接：ws://127.0.0.1:3000/websocket/**
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
```

​    ...

    public Game game = null;
    
    ...
```
}
接口实现
ReceiveBotMoveServiceImpl.java

package com.kob.backend.service.impl.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.stereotype.Service;

@Service
public class ReceiveBotMoveServiceImpl implements ReceiveBotMoveService {
```



    @Override
    public String receiveBotMove(Integer userId, Integer direction) {
        System.out.println("receive bot move: " + userId + " " + direction + " ");
        if(WebSocketServer.users.get(userId) != null) {
            Game game = WebSocketServer.users.get(userId).game;
            if(game != null) {
                if(game.getPlayerA().getId().equals(userId)) {
                    game.setNextStepA(direction);
                } else if(game.getPlayerB().getId().equals(userId)) {
                    game.setNextStepB(direction);
                }
            }
        }
        return "receive bot move success";
    }
}

```
控制器
ReceiveBotMoveController.java

package com.kob.backend.controller.pk;

import com.kob.backend.service.pk.ReceiveBotMoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class ReceiveBotMoveController {
    @Autowired
    private ReceiveBotMoveService receiveBotMoveService;
```



    @PostMapping("/pk/receive/bot/move/")
    public String receiveBotMove(@RequestParam MultiValueMap<String, String> data) {
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        Integer direction = Integer.parseInt(Objects.requireNonNull(data.getFirst("direction")));
        return receiveBotMoveService.receiveBotMove(userId, direction);
    }
}
权限控制(网关)
放行接口

```
SecurityConfig.java

package com.kob.backend.config;

...

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
```

​    ...

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 放行这两个接口
                .antMatchers("/user/account/token/", "/user/account/register/", "/getKaptcha").permitAll()
                .antMatchers("/pk/start/game/", "/pk/receive/bot/move/").hasIpAddress("127.0.0.1")
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated();
    
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
    
    ...
}
2.BotRunningSystem返回Bot执行结果

```
Consumer.java
Consumer.java

package com.kob.botrunningsystem.service.impl.utils;

import com.kob.botrunningsystem.utils.BotInterface;
import org.joor.Reflect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Component
public class Consumer extends Thread {
    private Bot bot;
    private static RestTemplate restTemplate;
    private static final String receiveBotMoveUrl = "http://127.0.0.1:3000/pk/receive/bot/move/";
```



    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        Consumer.restTemplate = restTemplate;
    }
    
    ...
    
    @Override
    public void run() {
        UUID uuid = UUID.randomUUID();
        String uid = uuid.toString().substring(0, 8);
        BotInterface botInterface = Reflect.compile(
                "com.kob.botrunningsystem.utils.Bot" + uid,
                addUid(bot.getBotCode(), uid)
        ).create().get();
    
        Integer direction = botInterface.nextMove(bot.getInput());
        System.out.println("move-direction: " + bot.getUserId() + " " + direction);
    
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id", bot.getUserId().toString());
        data.add("direction", direction.toString());
    
        restTemplate.postForObject(receiveBotMoveUrl, data, String.class);
    }
}
测试
y总bot代码：

```
Bot.java

package com.kob.botrunningsystem.utils;

import java.util.ArrayList;
import java.util.List;

public class Bot implements BotInterface {
```



    static class Cell {
        public int x, y;
        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    // 检查当前回合，蛇的长度是否会增加
    private boolean check_tail_increasing(int step) {
        if(step <= 10) return true;
        return step % 3 == 1;
    }
    
    public List<Cell> getCells(int sx, int sy, String steps) {
        steps = steps.substring(1, steps.length() - 1);
        List<Cell> res = new ArrayList<>();
    
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int x = sx, y = sy;
        int step = 0;
        res.add(new Cell(x, y));
        for(int i = 0; i < steps.length(); i++) {
            int d = steps.charAt(i) - '0';
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));
            if(!check_tail_increasing(++step)) {
                res.remove(0);
            }
        }
        return res;
    }
    
    @Override
    public Integer nextMove(String input) {
        // 地图#my.sx#my.sy#(my操作)#you.sx#you.sy#(you操作)
        String[] strs = input.split("#");
        int[][] g = new int[13][14];
        for(int i = 0, k = 0; i < 13; i++) {
            for(int j = 0; j < 14; j++, k++) {
                if(strs[0].charAt(k) == '1') {
                    g[i][j] = 1;
                }
            }
        }
    
        int aSx = Integer.parseInt(strs[1]), aSy = Integer.parseInt(strs[2]);
        int bSx = Integer.parseInt(strs[4]), bSy = Integer.parseInt(strs[5]);
    
        List<Cell> aCells = getCells(aSx, aSy, strs[3]);
        List<Cell> bCells = getCells(bSx, bSy, strs[6]);
    
        for(Cell c : aCells) g[c.x][c.y] = 1;
        for(Cell c : bCells) g[c.x][c.y] = 1;
    
        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        for(int i = 0; i < 4; i++) {
            int x = aCells.get(aCells.size() - 1).x + dx[i];
            int y = aCells.get(aCells.size() - 1).y + dy[i];
            if(x >= 0 && x < 13 && y >= 0 && y < 14 && g[x][y] == 0) {
                return i;
            }
        }
        return 0;
    }
}

