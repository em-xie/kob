acapp第三方授权原理图

![1_308359794d-acapp.png](https://cdn.acwing.com/media/article/image/2022/09/07/29231_eca4b02a2e-1_308359794d-acapp.png)

web第三方授权原理图

![1.png](https://cdn.acwing.com/media/article/image/2021/11/25/1_1ddf070e4d-weboauth2.png)

acapp第三方授权登录的实现
1.后端

目录结构

```
backend
    controller
        user
            account
                acwing
                    AcAppController.java
                    WebController.java
    service
        impl
            user
                account
                    acwing
                        utils
                            HttpClientUtil.java
                        AcAppServiceImpl.java
                        WebServiceImpl.java
        user
            account
                acwing
                    AcAppService.java
                    WebService.java


```

pom.xml

```
    <dependencies>
        ...
        <!-- https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient -->
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.5.13</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
            <version>2.7.3</version>
        </dependency>
    </dependencies>


```

接口
AcAppService.java

```
package com.kob.backend.service.user.account.acwing;

import com.alibaba.fastjson.JSONObject;

/**
 * @作者：xie
 * @时间：2022/11/18 13:49
 */
public interface AcAppService {

    JSONObject applyCode();

    JSONObject receiveCode(String code, String state);
}
```

WebService.java

```
package com.kob.backend.service.user.account.acwing;

import com.alibaba.fastjson.JSONObject;

/**
 * @作者：xie
 * @时间：2022/11/18 13:51
 */
public interface WebService {

    JSONObject applyCode();

    JSONObject receiveCode(String code, String state);
}
```

接口实现
AcAppServiceImpl.java

```
package com.kob.backend.service.Imp.user.account.acwing;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.user.account.acwing.AcAppService;
import org.springframework.stereotype.Service;

/**
 * @作者：xie
 * @时间：2022/11/18 13:52
 */
@Service
public class AcAppServiceImpl implements AcAppService {

    @Override
    public JSONObject applyCode() {
        return null;
    }

    @Override
    public JSONObject receiveCode(String code, String state) {
        return null;
    }
}
```

WebServiceImpl.java

```
package com.kob.backend.service.Imp.user.account.acwing;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.user.account.acwing.WebService;
import org.springframework.stereotype.Service;

/**
 * @作者：xie
 * @时间：2022/11/18 13:53
 */
@Service
public class WebServiceImpl implements WebService {

    @Override
    public JSONObject applyCode() {
        return null;
    }

    @Override
    public JSONObject receiveCode(String code, String state) {
        return null;
    }
}

```

控制器
AcAppController.java

```
package com.kob.backend.controller.user.account.acwing;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.user.account.acwing.AcAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/18 13:54
 */
@RestController
public class AcAppController {
    @Autowired
    private AcAppService acAppService;

    @GetMapping("/api/user/account/acwing/acapp/apply_code/")
    public JSONObject applyCode() {
        return acAppService.applyCode();
    }

    @GetMapping("/api/user/account/acwing/acapp/receive_code/")
    public JSONObject receiveCode(@RequestParam Map<String, String> data) {
        String code = data.get("code");
        String state = data.get("state");
        return acAppService.receiveCode(code, state);
    }
}


```

WebController

```
package com.kob.backend.controller.user.account.acwing;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.service.user.account.acwing.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/18 13:55
 */
@RestController
public class WebController {
    @Autowired
    private WebService webService;

    @GetMapping("/api/user/account/acwing/web/apply_code/")
    public JSONObject applyCode() {
        return webService.applyCode();
    }

    @GetMapping("/api/user/account/acwing/web/receive_code/")
    public JSONObject receiveCode(@RequestParam Map<String, String> data) {
        String code = data.get("code");
        String state = data.get("state");
        return webService.receiveCode(code, state);
    }
}

```

配置网关
SecurityConfig.java

```
package com.kob.backend.config;

...

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    ...

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(
                ...
                "/api/user/account/acwing/acapp/apply_code/",
                "/api/user/account/acwing/acapp/receive_code/",
                "/api/user/account/acwing/web/apply_code/",
                "/api/user/account/acwing/web/receive_code/"
            ).permitAll()
        ...
    }

    ...
}

```

实现在后端发送http请求的工具类
HttpClientUtil.java

```
package com.kob.backend.service.impl.user.account.acwing.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class HttpClientUtil {
    public static String get(String url, List<NameValuePair> params) {
        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        uriBuilder.setParameters(params);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            CloseableHttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}


```

修改数据库user表，添加openid列
点开idea右方数据库 -> 展开表 -> 右键user -> 修改表
本地数据库

![1.png](https://cdn.acwing.com/media/article/image/2022/09/08/29231_0aa74a1b2f-1.png)

云端数据库

修改pojo

```
User.java

backend/pojo/User.java

package com.kob.backend.pojo;

...

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    ...
    private String openid;
}
修改注册用户的接口实现
RegisterServiceImpl.java
backend/service/impl/user/account/RegisterServiceImpl.java

package com.kob.backend.service.impl.user.account;

...

@Service
public class RegisterServiceImpl implements RegisterService {
    ...
```




    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        ...
    
        User user = new User(null, username, encodedPassword, photo, 1500, null);
    
        ...
    }
}

实现AcAppServiceImpl
AcAppServiceImpl.java
package com.kob.backend.service.impl.user.account.acwing;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.user.account.acwing.utils.HttpClientUtil;
import com.kob.backend.service.user.account.acwing.AcAppService;
import com.kob.backend.utils.JwtUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Service
public class AcAppServiceImpl implements AcAppService {
    private static final String appid = "3222";
    private static final String appSecret = "83044f24d0f54c4ea041b684f902c1f9";
    private static final String redirectUri = "https://app3222.acapp.acwing.com.cn:20112/api/user/account/acwing/acapp/receive_code/";
    private static final String applyAccessTokenUrl = "https://www.acwing.com/third_party/api/oauth2/access_token/";
    private static final String getUserInfo = "https://www.acwing.com/third_party/api/meta/identity/getinfo/";
    private static final Random random = new Random();
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Override
    public JSONObject applyCode() {
        JSONObject resp = new JSONObject();
        resp.put("appid", appid);
        try {
            resp.put("redirect_uri", URLEncoder.encode(redirectUri, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        resp.put("scope", "userinfo");
        StringBuilder state = new StringBuilder();
        for(int i = 0; i < 10; i++) {
            state.append((char) (random.nextInt(10) + '0'));
        }
        resp.put("state", state.toString());
        resp.put("result", "success");
        redisTemplate.opsForValue().set(state.toString(), "true");
        redisTemplate.expire(state.toString(), Duration.ofMinutes(10));
    
        return resp;
    }
    
    @Override
    public JSONObject receiveCode(String code, String state) {
        JSONObject resp = new JSONObject();
    
        resp.put("result", "failed");
        // 如果没有code或state、或者state和我们的不相同 意味着这不是我们的请求
        if(code == null || state == null) return resp;
        if(Boolean.FALSE.equals(redisTemplate.hasKey(state))) return resp;
    
        List<NameValuePair> nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("appid", appid));
        nameValuePairs.add(new BasicNameValuePair("secret", appSecret));
        nameValuePairs.add(new BasicNameValuePair("code", code));
    
        // RestTemplate请求的是restful风格的接口，HttpClient可以请求http所有接口，因此HttpClient更加通用
        String getString = HttpClientUtil.get(applyAccessTokenUrl, nameValuePairs);
        if(getString == null) return resp;
        // 将请求到的信息转成json
        JSONObject getResp = JSONObject.parseObject(getString);
        String accessToken = getResp.getString("access_token");
        String openid = getResp.getString("openid");
    
        if(accessToken == null || openid == null) return resp;
    
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        // 若openid存在，直接登录
        List<User> users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty()) {
            User user = users.get(0);
            String jwt = JwtUtil.createJWT(user.getId().toString());
            resp.put("result", "success");
            resp.put("jwt_token", jwt);
            return resp;
        }
    
        nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("access_token", accessToken));
        nameValuePairs.add(new BasicNameValuePair("openid", openid));
        // 通过以上信息获取acwing的头像姓名
        getString = HttpClientUtil.get(getUserInfo, nameValuePairs);
        if(getString == null) return resp;
        getResp = JSONObject.parseObject(getString);
        String username = getResp.getString("username");
        String photo = getResp.getString("photo");
    
        // 创建用户
        User user = new User(
                null,
                username,
                null,
                photo,
                1500,
                openid
        );
        userMapper.insert(user);
        String jwt = JwtUtil.createJWT(user.getId().toString());
        resp.put("result", "success");
        resp.put("jwt_token", jwt);
        return resp;
    }
```
}
打包，将jar传到服务器，测试apply_code和receive_code
2.acapp

将AcWingOs存在user.js中
src/store/user.js

import $ from 'jquery'

export default {
  state: {
    AcWingOS: "AcWingOS",
    ...
  },
  getters: {
  },
  mutations: {
    ...
  },
  modules: {
  }
}
在进入acapp时请求apply_code接口
src/App.vue


<template>
    ...
</template>


<script>
...
import $ from 'jquery';


export default {
    name: "App",
    components: {
        ...
    },
    setup() {
        const store = useStore();
```




        $.ajax({
            url: "https://app3222.acapp.acwing.com.cn:20112/api/user/account/acwing/acapp/apply_code/",
            type: "GET",
            success: resp => {
                if(resp.result === "success") {
                    store.state.user.AcWingOS.api.oauth2.authorize(resp.appid, resp.redirect_uri, resp.scope, resp.state, resp => {
                        if(resp.result === "success") {
                            const jwt_token = resp.jwt_token;
                            store.commit("updateToken", jwt_token);
                            store.dispatch("getinfo", {
                                success() {
                                    store.commit("updatePullingInfo", false);
                                },
                                error() {
                                    store.commit("updatePullingInfo", false);
                                }
                            });
                        } else {
                            store.state.user.AcWingOS.api.window.close();
                        }
                    });
                } else {
                    store.state.user.AcWingOS.api.window.close();
                }
            }
        });
    }
```
}
</script>

<style scoped>
...
</style>

修改acapp目录下的upload.sh
#! /bin/bash

find dist/js/*.js | xargs sed -i 's/(function(){var e={/const myfunc = (function(myappid, AcWingOS){var e={/g'

find dist/js/*.js | xargs sed -i 's/AcWingOS:"AcWingOS"/AcWingOS:AcWingOS/g'

find dist/js/*.js | xargs sed -i 's/.mount("#app")}()})();/.mount(myappid)}()});/g'

echo "

export class Game {
    constructor(id, AcWingOS) {
        const myappid = '#' + id;
        myfunc(myappid, AcWingOS);
    }
}" >> dist/js/*.js

scp dist/js/*.js springboot:kob/acapp
scp dist/css/*.css springboot:kob/acapp
build项目，使用git bash运行upload.sh

解决用户重名问题
AcAppServiceImpl.java

package com.kob.backend.service.impl.user.account.acwing;

...

@Service
public class AcAppServiceImpl implements AcAppService {
```

​    ...

    @Override
    public JSONObject receiveCode(String code, String state) {
        ...
        String username = getResp.getString("username");
        String photo = getResp.getString("photo");
    
        if(username == null || photo == null) return resp;
    
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
    
        // 若用户名存在，则直接返回
        users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty()) {
            User user = users.get(0);
            String jwt = JwtUtil.createJWT(user.getId().toString());
            resp.put("result", "success");
            resp.put("jwt_token", jwt);
            return resp;
        }
    
        // 构造新的用户名
        for(int i = 0; i < 100; i++) {
            QueryWrapper<User> usernameQueryWrapper = new QueryWrapper<>();
            usernameQueryWrapper.eq("username", username);
            if(userMapper.selectList(usernameQueryWrapper).isEmpty()) break;
            username += (char)(random.nextInt(10) + '0');
            if(i == 99) return resp;
        }
    
        ...
    }
```
}

删除重名用户和对局记录
sudo mysql -uroot -p123456
use kob;
delete from user where username = '重复用户名';
select * from user;
delete from record;

web的第三方授权登陆的实现
1.后端

接口实现
WebServiceImpl.java

package com.kob.backend.service.impl.user.account.acwing;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.user.account.acwing.utils.HttpClientUtil;
import com.kob.backend.service.user.account.acwing.WebService;
import com.kob.backend.utils.JwtUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

@Service
public class WebServiceImpl implements WebService {
    private static final String appid = "3222";
    private static final String appSecret = "83044f24d0f54c4ea041b684f902c1f9";
    // 由于后端是restful风格接口，因此不请求后端接口，请求前端接口
    // (nginx配置会按照api路径转发到相应地方，/api开头的路由到后端，/websocket开头会路由到websocket，其他路径路由到前端)
    private static final String redirectUri = "https://app3222.acapp.acwing.com.cn:20112/user/account/acwing/web/receive_code/";
    private static final String applyAccessTokenUrl = "https://www.acwing.com/third_party/api/oauth2/access_token/";
    private static final String getUserInfo = "https://www.acwing.com/third_party/api/meta/identity/getinfo/";
    private static final Random random = new Random();
    @Autowired
    private UserMapper userMapper;
```



    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Override
    public JSONObject applyCode() {
        JSONObject resp = new JSONObject();
        String encodeUrl = "";
        try {
            encodeUrl = URLEncoder.encode(redirectUri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            resp.put("result", "failed");
            return resp;
        }
        StringBuilder state = new StringBuilder();
        for(int i = 0; i < 10; i++) {
            state.append((char) (random.nextInt(10) + '0'));
        }
        resp.put("result", "success");
        redisTemplate.opsForValue().set(state.toString(), "true");
        redisTemplate.expire(state.toString(), Duration.ofMinutes(10));
    
        String applyCodeUrl = "https://www.acwing.com/third_party/api/oauth2/web/authorize/?appid=" + appid
                + "&redirect_uri=" + redirectUri
                + "&scope=userinfo"
                + "&state=" + state;
        resp.put("apply_code_url", applyCodeUrl);
        return resp;
    }
    
    @Override
    public JSONObject receiveCode(String code, String state) {
        JSONObject resp = new JSONObject();
    
        resp.put("result", "failed");
        // 如果没有code或state、或者state和我们的不相同 意味着这不是我们的请求
        if(code == null || state == null) return resp;
        if(Boolean.FALSE.equals(redisTemplate.hasKey(state))) return resp;
    
        List<NameValuePair> nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("appid", appid));
        nameValuePairs.add(new BasicNameValuePair("secret", appSecret));
        nameValuePairs.add(new BasicNameValuePair("code", code));
    
        // RestTemplate请求的是restful风格的接口，HttpClient可以请求http所有接口，因此HttpClient更加通用
        String getString = HttpClientUtil.get(applyAccessTokenUrl, nameValuePairs);
        if(getString == null) return resp;
        // 将请求到的信息转成json
        JSONObject getResp = JSONObject.parseObject(getString);
        String accessToken = getResp.getString("access_token");
        String openid = getResp.getString("openid");
    
        if(accessToken == null || openid == null) return resp;
    
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("openid", openid);
        // 若openid存在，直接登录
        List<User> users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty()) {
            User user = users.get(0);
            String jwt = JwtUtil.createJWT(user.getId().toString());
            resp.put("result", "success");
            resp.put("jwt_token", jwt);
            return resp;
        }
    
        nameValuePairs = new LinkedList<>();
        nameValuePairs.add(new BasicNameValuePair("access_token", accessToken));
        nameValuePairs.add(new BasicNameValuePair("openid", openid));
        // 通过以上信息获取acwing的头像姓名
        getString = HttpClientUtil.get(getUserInfo, nameValuePairs);
        if(getString == null) return resp;
        getResp = JSONObject.parseObject(getString);
        String username = getResp.getString("username");
        String photo = getResp.getString("photo");
    
        if(username == null || photo == null) return resp;
    
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
    
        // 若用户名存在，则直接返回
        users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty()) {
            User user = users.get(0);
            String jwt = JwtUtil.createJWT(user.getId().toString());
            resp.put("result", "success");
            resp.put("jwt_token", jwt);
            return resp;
        }
    
        // 构造新的用户名
        for(int i = 0; i < 100; i++) {
            QueryWrapper<User> usernameQueryWrapper = new QueryWrapper<>();
            usernameQueryWrapper.eq("username", username);
            if(userMapper.selectList(usernameQueryWrapper).isEmpty()) break;
            username += (char)(random.nextInt(10) + '0');
            if(i == 99) return resp;
        }
    
        // 创建用户
        User user = new User(
                null,
                username,
                null,
                photo,
                1500,
                openid
        );
        userMapper.insert(user);
        String jwt = JwtUtil.createJWT(user.getId().toString());
        resp.put("result", "success");
        resp.put("jwt_token", jwt);
        return resp;
    }
```
重新打包backend，传到后端
2.web前端

增加AcWing一键登录按钮
src/views/user/account/UserAccountLoginView.vue

<template>
    <ContentField v-if="!$store.state.user.pulling_info">
        <div class="row justify-content-md-center">
            <div class="col-3" >
                ...
                <div style="text-align: center; margin-top: 20px; cursor: pointer;" @click="acwing_login">
                    <img width="30" src="https://cdn.acwing.com/media/article/image/2022/09/06/1_32f001fd2d-acwing_logo.png" alt="">
                    <br>
                    AcWing一键登录
                </div>
            </div>
        </div>
    </ContentField>
</template>


<script>
...


export default {
    ...
    setup() {
        ...
```




        const acwing_login = () => {
            $.ajax({
                url: "https://app3222.acapp.acwing.com.cn:20112/api/user/account/acwing/web/apply_code/",
                type: "GET",
                success: resp => {
                    if(resp.result === "success") {
                        window.location.replace(resp.apply_code_url)
                    }
                }
            });
        }
    
        ...
    
        return {
            ...
            acwing_login,
        }
    }
```
}
</script>

<style scoped>
...
</style>

增加receiveCode前端回调函数
src/views/user/account/UserAccountAcWingWebReceiveCodeView.vue

<template>
    <div>


    </div>

</template>
```



<script>
import router from '@/router/index';
import { useRoute } from 'vue-router';
import { useStore } from 'vuex';
import $ from 'jquery';

export default {
    setup() {
        const myRoute = useRoute();
        const store = useStore();

        $.ajax({
            url: "https://app3222.acapp.acwing.com.cn:20112/api/user/account/acwing/web/receive_code/", 
            type: "GET",
            data: {
                code: myRoute.query.code,
                state: myRoute.query.state
            },
            success: resp => {
                if(resp.result === "success") {
                    localStorage.setItem("jwt_token", resp.jwt_token);
                    store.commit("updateToken", resp.jwt_token);
                    router.push({ name: "home" });
                    store.commit("updatePullingInfo", false);
                } else {
                    router.push({ name: "user_account_login" });
                }
            }
        });
    }
```
}
</script>

<style scoped>


</style>
配置router
src/router/index.js

...
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView'
import UserAccountAcWingWebReceiveCodeView from '../views/user/account/UserAccountAcWingWebReceiveCodeView'

const routes = [
    ...
    {
        path: "/user/account/register/",
        name: "user_account_register",
        component: UserAccountRegisterView,
        meta: {
            requestAuth: false,
        }
    },
    {
        path: "/user/account/acwing/web/receive_code/",
        name: "user_account_acwing_web_receive_code",
        component: UserAccountAcWingWebReceiveCodeView,
        meta: {
            requestAuth: false,
        }
    },
    ...
]

...
```


