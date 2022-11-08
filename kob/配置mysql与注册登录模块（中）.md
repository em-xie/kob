1. 整体框架

   ![4.2 实现注册与登录模块](https://img-blog.csdnimg.cn/3da321496911427fb32903b0b1bfdf28.png#pic_center)


2. 实现JwtToken验证

2.1 添加依赖
在 pom.xml 中添加下列依赖：

jjwt-api
jjwt-impl
jjwt-jackson

添加之后点击重新加载。

```
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


```

2.2 编写、修改相关类

1. 实现 JwtUtil 类
在 backend 目录下创建软件包 utils 并创建 JwtUtil 类。
JwtUtil 类为jwt 工具类，用来创建、解析 jwt token。

```
package com.kob.backend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * @作者：xie
 * @时间：2022/11/7 13:48
 */

@Component
public class JwtUtil {

    public static final long JWT_TTL = 60 * 60 * 1000L * 24 * 14;  // 有效期14天
    public static final String JWT_KEY = "SDFGjhdsfalshdfHFdsjkdsfds121232131afasdfac";

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if (ttlMillis == null) {
            ttlMillis = JwtUtil.JWT_TTL;
        }

        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)
                .setSubject(subject)
                .setIssuer("sg")
                .setIssuedAt(now)
                .signWith(signatureAlgorithm, secretKey)
                .setExpiration(expDate);
    }

    public static SecretKey generalKey() {
        byte[] encodeKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        return new SecretKeySpec(encodeKey, 0, encodeKey.length, "HmacSHA256");
    }

    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }
}

```

2. 实现 JwtAuthenticationTokenFilter 类
  在 backend 的 config 目录下创建 cinfig 软件包，并创建 JwtAuthenticationTokenFilter 类。
  实现 JwtAuthenticationTokenFilter 类，用来验证 jwt token ，如果验证成功，则将 User 信息注入上下文中。

  ```
  package com.kob.backend.config.Filter;
  
  import com.kob.backend.mapper.UserMapper;
  import com.kob.backend.pojo.User;
  import com.kob.backend.service.Imp.utils.UserDetailsImpl;
  import com.kob.backend.utils.JwtUtil;
  import com.sun.istack.internal.NotNull;
  import io.jsonwebtoken.Claims;
  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
  import org.springframework.security.core.context.SecurityContextHolder;
  import org.springframework.stereotype.Component;
  import org.springframework.util.StringUtils;
  import org.springframework.web.filter.OncePerRequestFilter;
  
  import javax.servlet.FilterChain;
  import javax.servlet.ServletException;
  import javax.servlet.http.HttpServletRequest;
  import javax.servlet.http.HttpServletResponse;
  import java.io.IOException;
  
  /**
   * @作者：xie
   * @时间：2022/11/7 14:05
   */
  @Component
  public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
      @Autowired
      private UserMapper userMapper;
  
      @Override
      protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
          String token = request.getHeader("Authorization");
  
          if (!StringUtils.hasText(token) || !token.startsWith("Bearer ")) {
              filterChain.doFilter(request, response);
              return;
          }
  
          token = token.substring(7);
  
          String userid;
          try {
              Claims claims = JwtUtil.parseJWT(token);
              userid = claims.getSubject();
          } catch (Exception e) {
              throw new RuntimeException(e);
          }
  
          User user = userMapper.selectById(Integer.parseInt(userid));
  
          if (user == null) {
              throw new RuntimeException("用户名未登录");
          }
  
          UserDetailsImpl loginUser = new UserDetailsImpl(user);
          UsernamePasswordAuthenticationToken authenticationToken =
                  new UsernamePasswordAuthenticationToken(loginUser, null, null);
  
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  
          filterChain.doFilter(request, response);
      }
  }
  
  
  ```

  3. 配置config.SecurityConfig类
    放行登录、注册等接口。

    ```
    package com.kob.backend.config;
    
    import com.kob.backend.config.Filter.JwtAuthenticationTokenFilter;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    
    /**
     * @作者：xie
     * @时间：2022/11/7 13:21
     */
    @Configuration
    @EnableWebSecurity
    public class SecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    
        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/user/account/token/", "/user/account/register/").permitAll()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .anyRequest().authenticated();
    
            http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }
    
    
    ```

    

3. 实现后端API
3.1 修改数据库
将数据库中的 id 域变为自增。
在 pojo.User 类中添加注解：@TableId(type = IdType.AUTO)
右击 user ，点击 修改表 ，双击 id ，选择 自动增加。同时添加一列 photo，类型设置为 varchar(1000) ，用来存储照片，默认值可以设置为自己的头像。

3.2 实现接口API
实现API需要三个相关类或接口：

在 service 下创建一个接口。
在 service 下创建一个类实现这个接口。
在 controller 下创建一个类进行操作。

1. 实现 LoginService
验证用户名密码，验证成功后返回 jwt token（令牌）

创建接口：在 service下 创建 user 创建 account 新建一个接口 LoginService

```
package com.kob.backend.service.user.account;

        import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/7 14:15
 */
public interface LoginService {

    public  Map<String,String> getToken(String username, String password);
}

```

创建实现类：在 service下 impl 下创建 user 创建 account 新建一个实现类LoginServiceImpl

```
package com.kob.backend.service.Imp.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.Imp.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/7 14:23
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Override
    public Map<String, String> getToken(String username, String password) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username,password);

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();

        User user = loginUser.getUser();

        String jwt = JwtUtil.createJWT(user.getId().toString());

        HashMap<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("token", jwt);
        return map;
    }
}

```

创建controller：在 controller 创建 user 创建 account 新建一个LoginController

```
package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/7 14:35
 */

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;


    @PostMapping("/user/account/token")
    public Map<String,String> getToken(@RequestParam Map<String,String> map){
        String username = map.get("username");
        String password = map.get("password");
        return loginService.getToken(username,password);
    }

}



```

2. 配置InforService类
根据令牌返回用户信息。

创建接口：在 service下 创建 user 创建 account 新建一个接口 InfoService

```
package com.kob.backend.service.user.account;

import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/7 14:15
 */
public interface InfoService {

   public Map<String,String> getInfo();
}

```

创建实现类：在 service下 impl 下创建 user 创建 account 新建一个实现类InfoServiceImpl

```
package com.kob.backend.service.Imp.user.account;

import com.kob.backend.pojo.User;
import com.kob.backend.service.Imp.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.InfoService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/7 14:56
 */

@Service
public class InfoServiceImpl implements InfoService {


    @Override
    public Map<String, String> getInfo() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        User user = loginUser.getUser();
        HashMap<String, String> map = new HashMap<>();
        map.put("error_message", "success");
        map.put("id", user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());
        return map;
    }
}

```

创建controller：在 controller 创建 user 创建 account 新建一个InfoController

```
package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/7 15:01
 */

@RestController
public class InfoController {

    @Autowired
    private InfoService infoService;

    @GetMapping("/user/account/info/")
    public Map<String, String> getInfo() {
        return infoService.getInfo();
    }
}

```

3. 配置RegisterService类
注册账号

创建接口：在 service下 创建 user 创建 account 新建一个接口 RegisterService

```
package com.kob.backend.service.Imp.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/7 15:08
 */
@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {

        HashMap<String, String> map = new HashMap<>();
        if(username == null){
            map.put("error_message","用户名为空");
            return map;
        }

        if(password == null || confirmedPassword == null) {
            map.put("error_message","密码不能为空");
            return map;
        }
        //删除制表符，空格
        username = username.trim();
        if(username.length() == 0) {
            map.put("error_message","用户名不能为空");
            return map;
        }

        if(password.length() == 0 || confirmedPassword.length()==0) {
            map.put("error_message","密码不能为空");
            return map;
        }


        if(username.length() > 100 ){
            map.put("error_message","用户名长度不能大于100");
            return map;
        }

        if(password.length() > 100 || confirmedPassword.length()>100){
            map.put("error_message","密码长度不能大于100");
            return map;
        }

        if(!password.equals(confirmedPassword)) {
            map.put("error_message","两次输入的密码不一致");
            return map;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username" , username);
        List<User> users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty()){
            map.put("error_message","用户名已存在");
            return map;
        }

        String encodePassword = passwordEncoder.encode(password);
        String photo = "https://cdn.acwing.com/media/user/profile/photo/245919_lg_d984357855.jpg";
        User user = new User(null,username,encodePassword,photo);
        userMapper.insert(user);
        map.put("error_message","success");
        return map;
    }
}

```

创建controller：在 controller 创建 user 创建 account 新建一个RegisterController

```
package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/7 15:26
 */
@RestController
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @PostMapping("/user/account/register/")
    public Map<String,String> register(@RequestParam Map<String,String> map){
        String username = map.get("username");
        String password = map.get("password");
        String confirmedPassword = map.get("confirmedPassword");
        return registerService.register(username, password, confirmedPassword);
    }
}

```

3.2 调试接口API
验证用户登陆：
在 APP.vue 中编写：

注意：这里的Authorization: “Bearer “有空格，且token为自己的浏览器的token，需要更改token。

```
<template>
  <NavBar></NavBar>
  <router-view></router-view>
</template>
<script>
import NavBar from './components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap"
import $ from 'jquery'
export default {
  components: {
    NavBar
  },
  setup(){
    $.ajax({
      url: "http://127.0.0.1:8089/user/account/token/",
      type: "post",
      data: {
        username: 'xie',
        password: '123456',
      },
      success(resp) {
        console.log(resp);
      },
      error(resp) {
        console.log(resp);
      }
    });
    $.ajax({
      url: "http://127.0.0.1:8089/user/account/info/",
      type: "get",
      headers: {
        //授权 
        Authorization: "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI4MDY2ZjBhZWZlMmE0OGNhOGExYTEwOGVhOTdkMTJkYyIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY2NzgwMzg3NywiZXhwIjoxNjY5MDEzNDc3fQ.EtSg9dHEZKUcAd-p8-A92s24aVPQSXLZbrsIVzRkysI"
      },
      success(resp) {
        console.log(resp);
      },
      error(resp) {
        console.log(resp);
      }
    });
    $.ajax({
      url: "http://127.0.0.1:8089/user/account/register/",
      type: "post",
      data: {
        username: 'xie1',
        password: '123',
        confirmedPassword: '123',
      },
      success(resp) {
        console.log(resp);
      },
      error(resp) {
        console.log(resp);
      }
    });
  }
}
</script>

<style>
body {
  background-image: url("@/assets/images/background.png");
  background-size: cover;
}
</style>

```

4. 实现前端的登陆、注册界面
1. 实现两个页面–登陆、注册的前端样式：
从 bootstrap 上去寻找合适的样式：

举个例子：

样式：

```
<div class="container">
  <div class="row row-cols-2">
    <div class="col">Column</div>
    <div class="col">Column</div>
  </div>
</div>
```

![在这里插入图片描述](https://img-blog.csdnimg.cn/9154bc93f2ac4906a06f59c2052b6e59.png#pic_center)

表单：

```
<div class="mb-3">
  <label for="exampleFormControlInput1" class="form-label">Email address</label>
  <input type="email" class="form-control" id="exampleFormControlInput1" placeholder="name@example.com">
</div>
<div class="mb-3">
  <label for="exampleFormControlTextarea1" class="form-label">Example textarea</label>
  <textarea class="form-control" id="exampleFormControlTextarea1" rows="3"></textarea>
</div>
```

<div class="mb-3">
  <label for="exampleFormControlInput1" class="form-label">Email address</label>
  <input type="email" class="form-control" id="exampleFormControlInput1" placeholder="name@example.com">
</div>
<div class="mb-3">
  <label for="exampleFormControlTextarea1" class="form-label">Example textarea</label>
  <textarea class="form-control" id="exampleFormControlTextarea1" rows="3"></textarea>
</div>


提交按钮

<button type="button" class="btn btn-primary">Primary</button>

创建页面：
在 views 目录下创建 user ，新建两个文件 UserAccountLoginView.vue 和 UserAccountRegisterView.vue 。

实现 UserAccountRegisterView.vue 页面

```
<template>
    <ContentField>
        注册
    </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField.vue'

export default {
    components: {
        ContentField
    }
}
</script>

<style scoped>
</style>
```

实现 UserAccountLoginView.vue 页面

基本的 login 页面

```
<template>
    <ContentField>
        <div class="row justify-content-md-center">
            <div class="col-3">
                <form>
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>
                    <div class="error-message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-primary">提交</button>
                </form>
            </div>
        </div>
    </ContentField>
</template>

<script>
import ContentField from '../../../components/ContentField.vue'

export default {
    components: {
        ContentField
    }
}
</script>

<style scoped>
button {
    width: 100%;
}
div.error-message {
    color: red;
}
</style>


```

2. 在router目录下的index.js中加入路径

修改 index.js

```
import UserAccountLoginView from '../views/user/account/UserAccountLoginView'
import UserAccountRegisterView from '../views/user/account/UserAccountRegisterView'

const routes = [
  {
    path: "/user/account/login",
    name: "user_account_login",
    component: UserAccountLoginView,
  },
  {
    path: "/user/account/register",
    name: "user_account_register",
    component: UserAccountRegisterView,
  }

]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

```

3. 在store下的user.js存储用户信息
在 store 下创建 user.js

```
import $ from 'jquery'

export default {
    state: {
        id: "",
        username: "",
        password: "",
        photo: "",
        token: "",
        is_login: false,
    },
    getters: {
    },
    mutations: {
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateToken(state, token) {
            state.token = token;
        },
    },
    actions: {

    },
    modules: {
    }
}

```

4 把user加入到全局module
在 store 下的 index.js

```
import { createStore } from 'vuex'
import ModuleUser from './user'

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
  }
})
```

5. 添加辅助函数login
在 store 下的 user.js 修改。

```
import $ from 'jquery'

export default {
    state: {
        id: "",
        username: "",
        password: "",
        photo: "",
        token: "",
        is_login: false,
    },
    getters: {
    },
    mutations: {
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateToken(state, token) {
            state.token = token;
        },
    },
    actions: {
        login(context, data) {
            $.ajax({
                url: "http://127.0.0.1:8080/user/account/token/",
                type: "post",
                data: {
                  username: data.username,
                  password: data.password,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        context.commit("updateToken", resp.token);
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                },
                error(resp) {
                  data.error(resp); 
                }
              });
        },
    },
    modules: {
    }
}


```

6. 在 UserAccountLoginView.vue 中实现

```
<script>
import ContentField from '../../../components/ContentField.vue'
import { useStore } from 'vuex'
import { ref } from 'vue'

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let error_message = ref('');
        //触发函数
        const login = () => {
            error_message.value = "";
            store.dispatch("login", {
                username: username.value,
                password: password.value,
                success(resp) {
                    console.log(resp);
                },
                error(resp) {
                    console.log(resp);
                }

                /*
                success() {
                    //登陆成功后获取用户信息
                    store.dispatch("getinfo", {
                        success() {
                            router.push({ name: 'home' });
                            console.log(store.state.user);
                        }
                    })
                },
                error() {
                    error_message.value = "用户名或密码错误";
                }
                */
            })
        }

        return {
            username,
            password,
            error_message,
            login,
        }
    }
}
</script>


```

7. 把内容和变量绑定起来

```
<form @submit.prevent="login">
    <div class="mb-3">
        <label for="username" class="form-label">用户名</label>
        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
    </div>
    <div class="mb-3">
        <label for="password" class="form-label">密码</label>
        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
    </div>
    <div class="error-message">{{ error_message }}</div>
    <button type="submit" class="btn btn-primary">提交</button>
</form>


```

8. 在登陆界面测试
输入用户名和密码，获取 token。

9. 用户名和密码输入正确，点击提交跳到主页面
在 UserAccountLoginView.vue 中实现

```
<script>
import ContentField from '../../../components/ContentField.vue'
import { useStore } from 'vuex'
import { ref } from 'vue'
import router from '../../../router/index'

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let error_message = ref('');
        //触发函数
        const login = () => {
            error_message.value = "";
            store.dispatch("login", {
                username: username.value,
                password: password.value,
                success() {
                    //成功后跳转到主页面
                    router.push({name: 'home'});
                },
                error() {
                    error_message.value = "用户名或密码错误";
                }

                /*
                success() {
                    //登陆成功后获取用户信息
                    store.dispatch("getinfo", {
                        success() {
                            router.push({ name: 'home' });
                            console.log(store.state.user);
                        }
                    })
                },
                error() {
                    error_message.value = "用户名或密码错误";
                }
                */
            })
        }

        return {
            username,
            password,
            error_message,
            login,
        }
    }
}
</script>



```

10. 登陆成功后获取用户的信息
在 user.js 中添加辅助函数

```
import $ from 'jquery'

export default {
    state: {
        id: "",
        username: "",
        password: "",
        photo: "",
        token: "",
        is_login: false,
    },
    getters: {
    },
    mutations: {
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateToken(state, token) {
            state.token = token;
        },
    },
    actions: {
        login(context, data) {
            $.ajax({
                url: "http://127.0.0.1:8080/user/account/token/",
                type: "post",
                data: {
                  username: data.username,
                  password: data.password,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        context.commit("updateToken", resp.token);
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                },
                error(resp) {
                  data.error(resp); 
                }
              });
        },
        getinfo(context, data) {
            $.ajax({
                url: "http://127.0.0.1:8080/user/account/info/",
                type: "get",
                headers: {
                  Authorization: "Bearer " + context.state.token,
                },
                //成功就更新用户。
                success(resp) {
                    if (resp.error_message === "success") {
                        context.commit("updateUser", {
                            ...resp,
                            is_login: true,
                        });
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
              });
        },
        logout(context) {
            context.commit("logout");
        }
    },
    modules: {
    }
}



```

修改 UserAccountLoginView.vue

```
<script>
import ContentField from '../../../components/ContentField.vue'
import { useStore } from 'vuex'
import { ref } from 'vue'
import router from '../../../router/index'

export default {
    components: {
        ContentField
    },
    setup() {
        const store = useStore();
        let username = ref('');
        let password = ref('');
        let error_message = ref('');
        //触发函数
        const login = () => {
            error_message.value = "";
            store.dispatch("login", {
                username: username.value,
                password: password.value,
                success() {
                    //登陆成功后获取用户信息
                    store.dispatch("getinfo", {
                        success(resp) {
                            router.push({ name: 'home' });
                            console.log(store.state.user);
                        }
                    })
                },
                error() {
                    error_message.value = "用户名或密码错误";
                }

            })
        }

        return {
            username,
            password,
            error_message,
            login,
        }
    }
}
</script>


```

11. 把结果返回到右上角个人信息 – 修改导航栏
修改 NavBar.vue

```
<template>
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
  <div class="container">
    <!--router-link实现点击、图标不刷新  -->
    <router-link class="navbar-brand" :to="{name: 'home'}">King Of Bots</router-link>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText" aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarText">
      <ul class="navbar-nav me-auto mb-2 mb-lg-0">
        <li class="nav-item">
          <router-link :class="route_name == 'pk_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'pk_index'}">对战</router-link>
        </li>
        <li class="nav-item">
          <router-link :class="route_name == 'record_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'record_index'}">对局列表</router-link>
        </li>
        <li class="nav-item">
          <router-link :class="route_name == 'ranklist_index' ? 'nav-link active' : 'nav-link'" :to="{name: 'ranklist_index'}">排行榜</router-link>
        </li>
      </ul>
      <ul class="navbar-nav" v-if="$store.state.user.is_login">
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            {{ $store.state.user.username }}
          </a>
          <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
            <li>

                <router-link class="dropdown-item" :to="{name: 'user_bot_index'}">我的Bot</router-link>
            </li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item" href="#" @click="logout">退出</a></li>
          </ul>
        </li>
      </ul>
      <ul class="navbar-nav" v-else>
        <li class="nav-item">
          <router-link class="nav-link" :to="{name: 'user_account_login' }" role="button">
            登录
          </router-link>
        </li>
        <li class="nav-item">
          <router-link class="nav-link" :to="{name: 'user_account_register'}" role="button">
            注册
          </router-link>
        </li>
      </ul>


      <ul class="navbar-nav ">
        <div class="space">
        </div>
      </ul>
    </div>
  </div>
</nav>

</template>

<script>
import { useRoute } from 'vue-router'
import { computed } from 'vue'
import { useStore } from 'vuex';

export default {
    setup() {
        const store = useStore();
        const route = useRoute();
        let route_name = computed(() => route.name)

        const logout = () => {
          store.dispatch("logout");
        }

        return {
            route_name,
            logout
        }
    }
}
</script>



<style scoped>
/* .navbar-nav{
  margin-left:40px ;
} */
.space{
  margin-right: 50px;
}
</style>
```

12. 退出登陆
user.js 添加辅助函数

```
mutations: {
    logout(state) {
        state.id = "";
        state.username = "";
        state.photo = "";
        state.token = "";
        state.is_login = false;
    }
},
actions: {
    logout(context) {
        context.commit("logout");
    }
},
```

