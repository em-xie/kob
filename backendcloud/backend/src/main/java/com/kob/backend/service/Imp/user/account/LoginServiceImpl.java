package com.kob.backend.service.Imp.user.account;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kob.backend.domain.User;

import com.kob.backend.service.user.account.ISysUserService;
import com.kob.backend.service.user.account.LoginService;

import com.kob.common.exception.user.UserException;
import com.kob.common.utils.ServletUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;


/**
 * @作者：xie
 * @时间：2022/11/7 14:23
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class LoginServiceImpl implements LoginService {


    private final ISysUserService userService;
    //private final LogininforService asyncService;
    public static final String LOGIN_USER_KEY = "loginUser";

    //    @Autowired
//    private AuthenticationManager authenticationManager;
    @Override
    public String getToken(String username, String password) {

//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(username,password);
//
//        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
//
//        UserDetailsImpl loginUser = (UserDetailsImpl) authenticate.getPrincipal();
//
//        User user = loginUser.getUser();
//
//        String jwt = JwtUtil.createJWT(user.getId().toString());
//
//        HashMap<String, String> map = new HashMap<>();
//        map.put("token", jwt);
        HttpServletRequest request = ServletUtils.getRequest();
        User user = loadUserByUsername(username);
        if (BCrypt.checkpw(password, user.getPassword())) {
            // 此处可根据登录用户的数据不同 自行创建 loginUser
            User loginUser = buildLoginUser(user);
            // 生成token
            StpUtil.login(loginUser.getId());
            SaHolder.getStorage().set(LOGIN_USER_KEY,loginUser);
            StpUtil.getTokenSession().set(LOGIN_USER_KEY, loginUser);
            //asyncService.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"), request);
            return StpUtil.getTokenValue();
        }else{
                String error_msg = "密码错误";
             return error_msg;
        }
    }



    /**
     * 构建登录用户
     */
    private User buildLoginUser(User user) {

        user.setId(user.getId());
        user.setUsername(user.getUsername());
        return user;
    }

    private User loadUserByUsername(String username) {
        User user = userService.selectUserByUserName(username);
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UserException("user.not.exists", username);
        }
        return user;
    }


}
