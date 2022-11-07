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



1. 实现密文存储：
在 config 下新建 SecurityConfig 。
实现config.SecurityConfig类，用来实现用户密码的加密存储。

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

