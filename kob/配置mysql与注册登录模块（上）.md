数据库名 ：kob

4. 使用工具来操作数据库。
我自己也不喜欢写sql语句(懒 )，所以平常使用 Navicat for MySQL绿色版 or Navicat for MySQL 来操作数据库，连接上直接就是按钮操作，挺方便的！刚开始接触sql的同学可以多写写hh，至于教程的话网上一大堆，百度一下花个几分钟学一下就行。
界面大概如下：

2.3 IDEA连接MySQL

1. 连接，点击右边的数据库 -> + -> 数据源-> MySQL，输入账号、密码、数据库名称，这里是kob，点击测试连接，成功后点击应用就可以了。

```
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

```

2. 在application.properties中添加数据库配置：

//输入你自己的用户和密码

```
spring.datasource.username=root
spring.datasource.password=123456
spring.datasource.url=jdbc:mysql://localhost:3306/kob?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

点击运行 出现报错 可能是因为路径问题。
点击运行，输入网址 http://127.0.0.1:8080/pk/index/显示界面就成功了



3.2 实现简单的CRUD ：
1. SpringBoot中的常用模块
pojo层：将数据库中的表对应成Java中的Class
mapper层（也叫Dao层）：将pojo层的class中的操作，映射成sql语句
service层：写具体的业务逻辑，组合使用mapper中的操作
controller层：负责请求转发，接受页面过来的参数，传给Service处理，接到返回值，再传给页面

注解：

使用注解可以帮助我们不在需要配置繁杂的xml文件，以前最基本的web项目是需要写xml配置的，需要标注你的哪个页面和哪个 servle 是对应的，注解可以帮助我们减少这方面的麻烦。

@Controller：用于定义控制器类，在spring项目中由控制器负责将用户发来的URL请求转发到对应的服务接口（service层），一般这个注解在类中，通常方法需要配合注解@RequestMapping。

@RequestMapping：提供路由信息，负责URL到Controller中的具体函数的映射。

@Autowired：自动导入依赖的bean

@Service：一般用于修饰service层的组件

@Bean：用@Bean标注方法等价于XML中配置的bean。

@AutoWired：自动导入依赖的bean。byType方式。把配置好的Bean拿来用，完成属性、方法的组装，它可以对类成员变量、方法及构造函数进行标注，完成自动装配的工作。当加上（required=false）时，就算找不到bean也不报错。

可以不用花时间去了解类似注解的实现原理，用的时候背下来就行，这里只是简单记录一下，如果想深入了解，可以百度。



2.1 在 backend 下创建 pojo 包 创建一个类 User，将数据库中的表 User转化为 Java 中的 User.class



```
package com.kob.backend.pojo;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @作者：xie
 * @时间：2022/11/7 12:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
}
```

2.2 在backend创建mapper 包，创建一个 Java 类的接口 UserMapper

```
package com.kob.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kob.backend.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @作者：xie
 * @时间：2022/11/7 12:40
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
```

2.3 在backend 的 controller 下创建 user 包然后创建 UserController.

```
package com.kob.backend.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @作者：xie
 * @时间：2022/11/7 12:41
 */

@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    /**
     * 查询所有用户
     */
    @GetMapping("/user/all/")
    public List<User> getAll() {
        return userMapper.selectList(null);
    }

    /**
     * 查询单个用户
     */
    @GetMapping("/user/{userId}/")
    public User getUser(@PathVariable int userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",userId);

        return userMapper.selectOne(queryWrapper);

        // 范围遍历
        // public List<User> getUser(int userId)
        // queryWrapper.ge("id", 2).le("id", 3);
        // return userMapper.selectList(queryWrapper);
    }

    /**
     * 添加某个用户 直接输入 id name password
     * @param userId
     * @param username
     * @param password
     * @return Add User Sucessfully
     */
    @GetMapping("/user/add/{userId}/{username}/{password}/")
    public String addUser (@PathVariable int userId,
                           @PathVariable String username,
                           @PathVariable String password) {

        User user = new User(userId, username, password);
        userMapper.insert(user);
        return "Add User Sucessfully";
    }

    /**
     * 删除某个用户，直接输入 id
     * @param userId
     * @return Delete User Successfully
     */
    @GetMapping("/user/delete/{userId}/")
    public String deleteUser(@PathVariable int userId) {
        userMapper.deleteById(userId);
        return "Delete User Successfully";
    }
}



```

测试

[127.0.0.1:8089/user/all/](http://127.0.0.1:8089/user/all/)



4. 配置Spring Security
是用户认证操作 – 一种授权机制，目的是安全。

4.1 添加依赖：

1. 添加依赖，添加之后刷新。

```
spring-boot-starter-security

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
    <version>2.7.0</version>
</dependency>
```

刷新之后显示登陆：



默认的叫 Username 是 user ，密码自动生成。

4.2 与数据库对接 ：
在backend 的 service 创建 impl 包，新建 UserDetailsServiceImpl 类。
实现service.impl.UserDetailsServiceImpl类，继承自UserDetailsService接口，用来接入数据库信息。

![image-20221107133022356](C:\Users\A\AppData\Roaming\Typora\typora-user-images\image-20221107133022356.png)



```
package com.kob.backend.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.Imp.utils.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @作者：xie
 * @时间：2022/11/7 13:10
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

//3.1 UserDetailsService
UserDetailsService接口。该接口只提供了一个方法：

//UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

//该方法很容易理解：通过用户名来加载用户 。这个方法主要用于从系统数据中查询并加载具体的用户到Spring Security中。
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return new UserDetailsImpl(user);
    }
}

```

```
package com.kob.backend.service.Imp.utils;

import com.kob.backend.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @作者：xie
 * @时间：2022/11/7 13:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


```

### 3.2 UserDetails

从上面`UserDetailsService` 可以知道最终交给Spring Security的是`UserDetails` 。该接口是提供用户信息的核心接口。该接口实现仅仅存储用户的信息。后续会将该接口提供的用户信息封装到认证对象`Authentication`中去。`UserDetails` 默认提供了：

- 用户的权限集， 默认需要添加`ROLE_` 前缀
- 用户的加密后的密码， 不加密会使用`{noop}`前缀
- 应用内唯一的用户名
- 账户是否过期
- 账户是否锁定
- 凭证是否过期
- 用户是否可用

如果以上的信息满足不了你使用，你可以自行实现扩展以存储更多的用户信息。比如用户的邮箱、手机号等等。通常我们使用其实现类：

```
org.springframework.security.core.userdetails.User
```

该类内置一个建造器`UserBuilder` 会很方便地帮助我们构建`UserDetails` 对象

```
org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
```

源码如下：

```
@Configuration
@ConditionalOnClass(AuthenticationManager.class)
@ConditionalOnBean(ObjectPostProcessor.class)
@ConditionalOnMissingBean({ AuthenticationManager.class, AuthenticationProvider.class, UserDetailsService.class })
public class UserDetailsServiceAutoConfiguration {

    private static final String NOOP_PASSWORD_PREFIX = "{noop}";

    private static final Pattern PASSWORD_ALGORITHM_PATTERN = Pattern.compile("^\\{.+}.*$");

    private static final Log logger = LogFactory.getLog(UserDetailsServiceAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean(
            type = "org.springframework.security.oauth2.client.registration.ClientRegistrationRepository")
    @Lazy
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(SecurityProperties properties,
            ObjectProvider<PasswordEncoder> passwordEncoder){
        SecurityProperties.User user = properties.getUser();
        List<String> roles = user.getRoles();
        return new InMemoryUserDetailsManager(
                User.withUsername(user.getName()).password(getOrDeducePassword(user, passwordEncoder.getIfAvailable()))
                        .roles(StringUtils.toStringArray(roles)).build());
    }

    private String getOrDeducePassword(SecurityProperties.User user, PasswordEncoder encoder) {
        String password = user.getPassword();
        if (user.isPasswordGenerated()) {
            logger.info(String.format("%n%nUsing generated security password: %s%n", user.getPassword()));
        }
        if (encoder != null || PASSWORD_ALGORITHM_PATTERN.matcher(password).matches()) {
            return password;
        }
        return NOOP_PASSWORD_PREFIX + password;
    }

}
```

我们来简单解读一下该类，从`@Conditional`系列注解我们知道该类在类路径下存在`AuthenticationManager`、在Spring [容器](https://cloud.tencent.com/product/tke?from=10680)中存在Bean `ObjectPostProcessor`并且不存在Bean `AuthenticationManager`, `AuthenticationProvider`, `UserDetailsService`的情况下生效。千万不要纠结这些类干嘛用的! 该类只初始化了一个`UserDetailsManager` 类型的Bean。`UserDetailsManager` 类型负责对安全用户实体抽象`UserDetails`的增删查改操作。同时还继承了`UserDetailsService`接口。

明白了上面这些让我们把目光再回到`UserDetailsServiceAutoConfiguration` 上来。该类初始化了一个名为`InMemoryUserDetailsManager` 的内存用户管理器。该管理器通过配置注入了一个默认的`UserDetails`存在内存中，就是我们上面用的那个`user` ，每次启动`user`都是动态生成的。那么问题来了如果我们定义自己的`UserDetailsManager` Bean是不是就可以实现我们需要的用户管理逻辑呢？





1. 实现密文存储：
在 config 下新建 SecurityConfig 。
实现config.SecurityConfig类，用来实现用户密码的加密存储。

### 3.3 UserDetailsServiceAutoConfiguration

`UserDetailsServiceAutoConfiguration` 全限定名为:

```
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```



2. 测试
在 Test 下生成需要转换的密文，同时修改数据库下的密码为密文。

```
package com.kob.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class BackendApplicationTests {

    @Test
    void contextLoads() {
        PasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("123456"));
    }

}

```

### 3.4 自定义UserDetailsManager

我们来自定义一个`UserDetailsManager` 来看看能不能达到自定义用户管理的效果。首先我们针对`UserDetailsManager` 的所有方法进行一个代理的实现，我们依然将用户存在内存中，区别就是这是我们自定义的：

```
package cn.felord.spring.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * 代理 {@link org.springframework.security.provisioning.UserDetailsManager} 所有功能
 *
 * @author Felordcn
 */
public class UserDetailsRepository {

    private Map<String, UserDetails> users = new HashMap<>();


    public void createUser(UserDetails user) {
        users.putIfAbsent(user.getUsername(), user);
    }


    public void updateUser(UserDetails user) {
        users.put(user.getUsername(), user);
    }


    public void deleteUser(String username) {
        users.remove(username);
    }


    public void changePassword(String oldPassword, String newPassword) {
        Authentication currentUser = SecurityContextHolder.getContext()
                .getAuthentication();

        if (currentUser == null) {
            // This would indicate bad coding somewhere
            throw new AccessDeniedException(
                    "Can't change password as no Authentication object found in context "
                            + "for current user.");
        }

        String username = currentUser.getName();

        UserDetails user = users.get(username);


        if (user == null) {
            throw new IllegalStateException("Current user doesn't exist in database.");
        }

        // todo copy InMemoryUserDetailsManager  自行实现具体的更新密码逻辑
    }


    public boolean userExists(String username) {

        return users.containsKey(username);
    }


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return users.get(username);
    }
}
```

该类负责具体对`UserDetails` 的增删改查操作。我们将其注入Spring 容器：

```
    @Bean
    public UserDetailsRepository userDetailsRepository() {
        UserDetailsRepository userDetailsRepository = new UserDetailsRepository();

        // 为了让我们的登录能够运行 这里我们初始化一个用户Felordcn 密码采用明文 当你在密码12345上使用了前缀{noop} 意味着你的密码不使用加密，authorities 一定不能为空 这代表用户的角色权限集合
        UserDetails felordcn = User.withUsername("Felordcn").password("{noop}12345").authorities(AuthorityUtils.NO_AUTHORITIES).build();
        userDetailsRepository.createUser(felordcn);
        return userDetailsRepository;
    }
```

为了方便测试 我们也内置一个名称为`Felordcn` 密码为`12345`的`UserDetails`用户，密码采用明文 当你在密码`12345`上使用了前缀`{noop}` 意味着你的密码不使用加密，这里我们并没有指定密码加密方式你可以使用`PasswordEncoder` 来指定一种加密方式。通常推荐使用`Bcrypt`作为加密方式。默认Spring Security使用的也是此方式。authorities 一定不能为`null` 这代表用户的角色权限集合。接下来我们实现一个`UserDetailsManager` 并注入Spring 容器：

```
    @Bean
    public UserDetailsManager userDetailsManager(UserDetailsRepository userDetailsRepository) {
        return new UserDetailsManager() {
            @Override
            public void createUser(UserDetails user) {
                userDetailsRepository.createUser(user);
            }

            @Override
            public void updateUser(UserDetails user) {
                userDetailsRepository.updateUser(user);
            }

            @Override
            public void deleteUser(String username) {
                userDetailsRepository.deleteUser(username);
            }

            @Override
            public void changePassword(String oldPassword, String newPassword) {
                userDetailsRepository.changePassword(oldPassword, newPassword);
            }

            @Override
            public boolean userExists(String username) {
                return userDetailsRepository.userExists(username);
            }

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return userDetailsRepository.loadUserByUsername(username);
            }
        };
    }
```

这样实际执行委托给了`UserDetailsRepository` 来做。我们重复 `章节3.` 的动作进入登陆页面分别输入`Felordcn`和`12345` 成功进入。





4.4 使用密文添加用户 ：
修改 controller 下的 user 的 UserController的注册，密码直接存储加密之后的密码。

```
@GetMapping("/user/add/{userId}/{username}/{password}/")
    public String addUser(
            @PathVariable int userId,
            @PathVariable String username,
            @PathVariable String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(userId, username, encodedPassword);
        userMapper.insert(user);
        return "Add User Successfully";
    }


```

