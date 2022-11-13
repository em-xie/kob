![0.png](https://cdn.acwing.com/media/article/image/2022/08/16/29231_fb0872801d-0.png)

红色为微服务，Match System是当前课的内容

微服务概念 及 本项目匹配思路

![1.png](https://cdn.acwing.com/media/article/image/2022/08/17/29231_21d4b0141d-1.png)

每个服务是一个独立的程序，每个服务之间可以相互通信。此项目中，BackEnd 和 MatchSystem都是一个微服务！
当BackEnd接收到玩家开始匹配的请求，BackEnd会想MatchSystem发送http请求，通知MatchSystem开始匹配，
MatchSystem会单独开一个线程开始匹配，匹配逻辑为：每秒都会扫描匹配池中的所有玩家，判断玩家能否相互匹
配，若能，则发送http请求给BackEnd。此项目使用的微服务技术是Spring Cloud。Spring Cloud技术涉及的
很多不只是服务间的相关通信，还包括网关、负载均衡等优化配置，由于项目并发量没那么大，因此未使用这些技
术。

Spring Cloud：每个服务都是一个Server，每个Server相互通信使用的是http协议。
Thrift：用Thrift生成的微服务，BackEnd与服务之间的通信为Socket协议，比http快一些。

Spring Cloud父级项目 和 Match System服务的实现
1.新建微服务父级项目

父级项目管理匹配系统和后端两个Spring Boot项目

文件 -> 新建 -> 项目

![2.png](https://cdn.acwing.com/media/article/image/2022/08/17/29231_37c15dfc1d-2.png)

![3.png](https://cdn.acwing.com/media/article/image/2022/08/17/29231_5e5511581d-3.png)

2.整理父级项目

删除backendcloud的src目录，因为父级项目没有逻辑，只负责管理两个微服务。

配置pom，记得点开IDEA右侧Maven重新加载所有Maven项目

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.kob.backendcloud</groupId>
    <artifactId>backendcloud</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <modules>
        <module>matchingsystem</module>
    </modules>
    <name>backendcloud</name>
    <description>backendcloud</description>

    <packaging>pom</packaging>

    <properties>
        <java.version>1.8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <spring-boot.version>2.3.7.RELEASE</spring-boot.version>
    </properties>

    <dependencies>

        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>2021.0.3</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>


    </dependencies>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>2.3.7.RELEASE</version>
                <configuration>
                    <mainClass>com.kob.backendcloud.backendcloud.BackendcloudApplication</mainClass>
                </configuration>
                <executions>
                    <execution>
                        <id>repackage</id>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>

```

新建模块
按照以下步骤新建模块之后若弹出将新建项目加到git中，勾选不再询问，点击取消

右键backendcloud->新建->模块

![4.png](https://cdn.acwing.com/media/article/image/2022/08/17/29231_7325d6681d-4.png)

子项目pom的管理

虽然pom稍些不同，但是其他代码全部一样

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>backendcloud</artifactId>
        <groupId>com.kob.backendcloud</groupId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
    </properties>

    <groupId>com.kob.matchingsystem</groupId>
    <artifactId>matchingsystem</artifactId>
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
    </dependencies>
</project>
```

配置端口
在matchingsystem -> src -> main -> resources右键新建文件， 文件名为：application.properties

3.匹配服务的搭建

文件结构

```
matchingsystem
    config
        SecurityConfig.java
    controller
        MatchingController.java
    service
        impl
            MatchingServiceImpl.java
        MatchingService.java
    MatchSystemApplication.java
```

接口

```
package com.kob.mathcingsystem.service;

/**
 * @作者：xie
 * @时间：2022/11/13 10:56
 */
public interface MatchingService {
    String addPlayer(Integer userId, Integer rating);
    String removePlayer(Integer userId);
}

```

接口实现

```
package com.kob.mathcingsystem.service.impl;

import com.kob.mathcingsystem.service.MatchingService;
import org.springframework.stereotype.Service;

/**
 * @作者：xie
 * @时间：2022/11/13 10:58
 */
@Service
public class MatchingServiceImpl implements MatchingService {

    @Override
    public String addPlayer(Integer userId, Integer rating) {
        System.out.println("add player:" +userId+ "" +rating);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove player:" + userId);
        return "remove player success";
    }
}

```

matchingsystem/controller/MatchingController.java

```
package com.kob.mathcingsystem.controller;

import com.kob.mathcingsystem.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @作者：xie
 * @时间：2022/11/13 11:02
 */
@RestController
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

   // 参数不能使用普通map，MultiValueMap和普通map的区别时，这个是一个键对应多个值
    @PostMapping("/player/add/")
    public String addPlayer(@RequestParam MultiValueMap<String,String> data){
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        Integer rating = Integer.parseInt(Objects.requireNonNull(data.getFirst("rating")));
        return matchingService.addPlayer(userId,rating);
    }


    @PostMapping("/player/remove/")
    public String removePlayer(@RequestParam MultiValueMap<String,String> data){
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        return matchingService.removePlayer(userId);
    }
}

```

```
package com.kob.mathcingsystem.controller;

import com.kob.mathcingsystem.service.MatchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @作者：xie
 * @时间：2022/11/13 11:02
 */
@RestController
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

   // 参数不能使用普通map，MultiValueMap和普通map的区别时，这个是一个键对应多个值
    @PostMapping("/player/add/")
    public String addPlayer(@RequestParam MultiValueMap<String,String> data){
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        Integer rating = Integer.parseInt(Objects.requireNonNull(data.getFirst("rating")));
        return matchingService.addPlayer(userId,rating);
    }


    @PostMapping("/player/remove/")
    public String removePlayer(@RequestParam MultiValueMap<String,String> data){
        Integer userId = Integer.parseInt(Objects.requireNonNull(data.getFirst("user_id")));
        return matchingService.removePlayer(userId);
    }
}
```

导包

```
       <!-- 此处修改 -->
        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-security -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
            <version>2.7.1</version>
        </dependency>
    </dependencies>


```

配置类

```
package com.kob.mathcingsystem.config;



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
                .antMatchers("/player/add/","/player/remove/").hasIpAddress("127.0.0.1")
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated();


    }

}


```

入口

```
package com.kob.mathcingsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @作者：xie
 * @时间：2022/11/13 10:56
 */
@SpringBootApplication
public class MatchingSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MatchingSystemApplication.class, args);
    }

}
```

测试
点击类左边的行号右边的启动图标运行项目，运行项目时，左下角会提示是否使用服务管理多个Spring Boot项目，点击使用。

BackEnd
将后端转化成一个微服务

右键backendcloud 新建 -> 模块

![5.png](https://cdn.acwing.com/media/article/image/2022/08/17/29231_8e47024e1d-5.png)

删掉backend的src目录

将之前项目的src目录复制到backend下，先在文件资源管理器赋值src目录，再右键idea的backend -> 粘贴 -> 确定

修改pom.xml

记得刷新Maven

```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <parent>
        <artifactId>backendcloud</artifactId>
        <groupId>com.kob.backendcloud</groupId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.kob.backend</groupId>
    <artifactId>backend</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
            <version>2.7.2</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>2.0.11</version>
        </dependency>

        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.11.2</version>
        </dependency>


        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.11.2</version>
            <scope>runtime</scope>
        </dependency>

        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.11.2</version>
            <scope>runtime</scope>
        </dependency>


        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
            <version>2.7.0</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
            <version>2.7.0</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>1.18.22</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.28</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.1</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-generator</artifactId>
            <version>3.5.1</version>
        </dependency>




        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>
</project>
```

Backend 和 Match System 的通信

1.文件结构

backend
    config
        RestTemplateConfig.java

2.代码

封装StartGame逻辑

```
package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    private User user;
    public final static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
//    private final static CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();
    private Session session = null;

    private static UserMapper userMapper;
    public static RecordMapper recordMapper;
    private static RestTemplate restTemplate;

    private  Game game =null;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }




    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);

        if (this.user != null) {
            users.put(userId, this);
            System.out.println("connected！");
        } else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected！");
        if (this.user != null) {
            users.remove(this.user.getId());
//            matchpool.remove(this.user);
        }
    }


    private void startGame(Integer aId,Integer bId){
       User a = userMapper.selectById(aId), b = userMapper.selectById(bId);
        Game game = new Game(13, 14, 20,a.getId(),b.getId());
        game.createMap();
//            另外一个线程
        game.start();


        users.get(a.getId()).game = game;
        users.get(b.getId()).game = game;

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
    }


    private void startMatching() {
        System.out.println("start matching!");

//        matchpool.add(this.user);
//
//        while (matchpool.size() >= 2) {
//            Iterator<User> iterator = matchpool.iterator();
//            User a = iterator.next(), b = iterator.next();
//
//            matchpool.remove(a);
//            matchpool.remove(b);
//
//
//        }
    }

    private void stopMatching() {
        System.out.println("stop matching!");
//        matchpool.remove(this.user);
    }

    private void move(int direction){
        if(game.getPlayerA().getId().equals(user.getId())){
            game.setNextStepA(direction);
        }else if(game.getPlayerB().getId().equals(user.getId())){
            game.setNextStepB(direction);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message！" + message);
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            startMatching();
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        }else  if("move".equals(event)){
            move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized(this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

```

构造Spring Cloud服务之间通信需要的RestTemplate
RestTemplate是Spring提供的用于访问Rest服务的客户端，RestTemplate提供了多种便捷访问远程Http服务的方法,能够大大提高客户端的编写效率。

```
package com.kob.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @作者：xie
 * @时间：2022/11/13 11:31
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
```

修改数据库，将排名移到玩家身上
因为打开了新创建的项目，因此重新连接数据库

打开kob数据库，展开表，右键user表，点击修改表，增加rating字段

![3.png](https://cdn.acwing.com/media/article/image/2022/08/17/29231_b77171eb1d-3.png)

加上BackEnd发送给matchingsystem服务的添加玩家和删除玩家的请求

```
package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {

    private User user;
    public final static ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
//    private final static CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();
    private Session session = null;

    private static UserMapper userMapper;
    public static RecordMapper recordMapper;
    private static RestTemplate restTemplate;
    private final static String addPlayerUrl = "http://127.0.0.1:8090/player/add/";
    private final static String removePlayerUrl = "http://127.0.0.1:8090/player/remove/";
    private  Game game =null;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }
    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }
    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        WebSocketServer.restTemplate = restTemplate;
    }




    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        Integer userId = JwtAuthentication.getUserId(token);
        this.user = userMapper.selectById(userId);

        if (this.user != null) {
            users.put(userId, this);
            System.out.println("connected！");
        } else {
            this.session.close();
        }
    }

    @OnClose
    public void onClose() {
        // 关闭链接
        System.out.println("disconnected！");
        if (this.user != null) {
            users.remove(this.user.getId());
//            matchpool.remove(this.user);
        }
    }


    private void startGame(Integer aId,Integer bId){
       User a = userMapper.selectById(aId), b = userMapper.selectById(bId);
        Game game = new Game(13, 14, 20,a.getId(),b.getId());
        game.createMap();
//            另外一个线程
        game.start();


        users.get(a.getId()).game = game;
        users.get(b.getId()).game = game;

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
    }


    private void startMatching() {
        System.out.println("start matching!");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        data.add("rating",this.user.getRating().toString());
        restTemplate.postForObject(addPlayerUrl,data,String.class);
//        matchpool.add(this.user);
//
//        while (matchpool.size() >= 2) {
//            Iterator<User> iterator = matchpool.iterator();
//            User a = iterator.next(), b = iterator.next();
//
//            matchpool.remove(a);
//            matchpool.remove(b);
//
//
//        }
    }

    private void stopMatching() {
        System.out.println("stop matching!");
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl,data,String.class);
//        matchpool.remove(this.user);
    }

    private void move(int direction){
        if(game.getPlayerA().getId().equals(user.getId())){
            game.setNextStepA(direction);
        }else if(game.getPlayerB().getId().equals(user.getId())){
            game.setNextStepB(direction);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 从Client接收消息
        System.out.println("receive message！" + message);
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if ("start-matching".equals(event)) {
            startMatching();
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        }else  if("move".equals(event)){
            move(data.getInteger("direction"));
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized(this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

```

测试
点击到backend/BackendApplication.java，运行项目，右下键会弹出Enable annotation processing，点击





Match System的实现
文件结构

引入lombok maven



```
matchingsystem
    config
        RestTemplateConfig.java
    service
        impl
            utils
                MatchingPool.java
                Player.java
```

引入lombok maven

Player.java
匹配服务也有玩家的概念







```
package com.kob.mathcingsystem.service.impl.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @作者：xie
 * @时间：2022/11/13 12:07
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer userId;
    private Integer rating;
    private Integer waitingTime;
}
```









MatchingServiceImpl.java
接收到backend的请求，添加玩家或删除玩家

```
@Service
public class MatchingServiceImpl implements MatchingService {

    //全局只有一个线程池 设置为静态
    public final static MatchingPool matchingPool = new MatchingPool();

    @Override
    public String addPlayer(Integer userId, Integer rating) {
        System.out.println("add player:" +userId+ " " +rating);
        matchingPool.addPlayer(userId, rating);
        return "add player success";
    }

    @Override
    public String removePlayer(Integer userId) {
        System.out.println("remove player:" + userId);
        matchingPool.removePlayer(userId);
        return "remove player success";
    }
}

```







发送给BackEnd消息辅助类：RestTemplate.java

```
package com.kob.mathcingsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @作者：xie
 * @时间：2022/11/13 11:31
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
```

MatchingPool.java
匹配池：匹配分数最接近的玩家，根据匹配时间增长，匹配范围逐渐增大。
操作：包括添加玩家，删除玩家，匹配玩家，发送给后端匹配成功的结果。
策略：为了防止匹配时间过长，优先将先匹配的玩家优先匹配，防止用户流失。



```
package com.kob.mathcingsystem.service.impl.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @作者：xie
 * @时间：2022/11/13 12:07
 */
@Component
public class MatchingPool extends Thread {
    private static List<Player> players = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    private static RestTemplate restTemplate;
    private static final String startGameUrl = "http://127.0.0.1:8089/pk/start/game/";

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate) {
        MatchingPool.restTemplate = restTemplate;
    }

    // 在多个线程(匹配线程遍历players时，主线程调用方法时)会操作players变量，因此加锁
    public void addPlayer(Integer userId, Integer rating){
        lock.lock();
        try {
            players.add(new Player(userId,rating,0));
        }finally {
            lock.unlock();
        }
    }

    public void removePlayer(Integer userId){
        lock.lock();
        try {
            List<Player> newPlayers = new ArrayList<>();
            for (Player player : players){
                if(!player.getUserId().equals(userId)){
                    newPlayers.add(player);
                }
            }
            players = newPlayers;
        }finally {
            lock.unlock();
        }
    }

    private void increaseWaitingTime(){ // 将所有当前玩家的等待时间 + 1
        for (Player player:players){
            player.setWaitingTime(player.getWaitingTime()+1);
        }
    }

    private boolean checkMatched(Player a, Player b){ // 判断两名玩家是否匹配
        int ratingDelta = Math.abs(a.getRating()  - b.getRating());
        int waitingTime = Math.min(a.getWaitingTime() , b.getWaitingTime());
        return ratingDelta <= waitingTime * 10;
    }

    private void sendResult(Player a, Player b) {   // 返回匹配结果
        System.out.println("send result: " + a + " " + b);
        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.add("a_id", a.getUserId().toString());
        data.add("b_id", b.getUserId().toString());
        restTemplate.postForObject(startGameUrl, data, String.class);

    }


    private void matchPlayer(){  // 尝试匹配所有玩家
        System.out.println("match players: " + players.toString());
        boolean[] used = new boolean[players.size()];
        for(int i = 0; i < players.size(); i++){
            if(used[i]) continue;
            for (int j = i + 1; j < players.size(); j++){
                if(used[j]) continue;
                Player a = players.get(i), b  = players.get(j);
                if(checkMatched(a,b)){
                    used[i] = used[j] = true;
                    sendResult(a, b);
                    break;
                }
            }
        }
        List<Player> newPlayers = new ArrayList<>();
        for(int i = 0; i < players.size(); i++) {
            if(!used[i]) {
                newPlayers.add(players.get(i));
            }
        }
        players = newPlayers;
    }


    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
                lock.lock();
                try {
                    increaseWaitingTime();
                    matchPlayer();
                }finally {
                    lock.unlock();
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

    }
}

```

MatchSystemApplication.java
启动匹配线程

```
matchingsystem/MatchSystemApplication.java

package com.kob.matchingsystem;

import com.kob.matchingsystem.service.impl.MatchingServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MatchSystemApplication {
    public static void main(String[] args) {
        MatchingServiceImpl.matchingPool.start();   // 启动匹配线程
        SpringApplication.run(MatchSystemApplication.class, args);
    }
}


```



Backend接收Match System的消息
删除backend/controller/pk下的BotInfoController.java和IndexController

文件结构

```
backend
    controller
        pk
            StartGameController.java
    service
        impl
            pk
                StartGameServiceImpl.java
        pk
            StartGameService.java
```

接口

```
package com.kob.backend.service.pk;

/**
 * @作者：xie
 * @时间：2022/11/13 14:14
 */
public interface StartGameService {
    String startGame(Integer aId, Integer bId);
}

```

接口实现

```
package com.kob.backend.service.Imp.pk;

import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

/**
 * @作者：xie
 * @时间：2022/11/13 14:16
 */
@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId, Integer bId) {
        System.out.println("start game: " + aId + " " + bId);
        WebSocketServer.startGame(aId,bId);
        return "start game success";
    }
}

```

控制器



```
package com.kob.backend.controller.pk;

import com.kob.backend.service.pk.StartGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * @作者：xie
 * @时间：2022/11/13 14:20
 */
@RestController
public class StartGameController {
    @Autowired
    private StartGameService startGameService;

    @PostMapping("/pk/start/game/")
    public String startGame(@RequestParam MultiValueMap<String, String> data) {
        Integer aId = Integer.parseInt(Objects.requireNonNull(data.getFirst("a_id")));
        Integer bId = Integer.parseInt(Objects.requireNonNull(data.getFirst("b_id")));
        return startGameService.startGame(aId, bId);
    }
}

```

权限控制
对Match System服务放行接口

```
 @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/user/account/token/", "/user/account/register/").permitAll()
                .antMatchers("/pk/start/game/").hasIpAddress("127.0.0.1")
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
```



增加程序鲁棒性：若玩家在匹配中突然停电，则会爆异常，需要特判

```
backend/consumer/WebSocketServer.java

package com.kob.backend.consumer;

...

@Component
// url链接：ws://127.0.0.1:3000/websocket/**
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    ...

    public static void startGame(Integer aId, Integer bId) {
        User a = userMapper.selectById(aId), b = userMapper.selectById(bId);
        Game game = new Game(13, 14, 20, a.getId(), b.getId());
        game.createMap();
        // 一局游戏一个线程，会执行game类的run方法
        game.start();

        if(users.get(a.getId()) != null)
            users.get(a.getId()).game = game;

        if(users.get(b.getId()) != null)
            users.get(b .getId()).game = game;

        JSONObject respGame = new JSONObject();
        // 玩家的id以及横纵信息
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("map", game.getG());

        // 发送给A的信息
        JSONObject respA = new JSONObject();
        respA.put("event", "start-matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);
        // 通过userId取出a的连接，给A发送respA
        if(users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString());

        // 发送给B的信息
        JSONObject respB = new JSONObject();
        respB.put("event", "start-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);
        // 通过userId取出b的连接，给B发送respB
        if(users.get(b.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toJSONString());
    }

    ...
}
backend/consumer/utils/Game.java

package com.kob.backend.consumer.utils;

...

public class Game extends Thread {
    ...

    private void senAllMessage(String message) {
        if(WebSocketServer.users.get(playerA.getId()) != null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if(WebSocketServer.users.get(playerB.getId()) != null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    ...
}


```































































