package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.LoginService;
import com.kob.common.constant.Constants;
import com.kob.common.core.domain.R;
import com.kob.common.core.domain.model.LoginBody;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @作者：xie
 * @时间：2022/11/7 14:35
 */
@Validated
@RequiredArgsConstructor
@RestController
public class LoginController {


    private final LoginService loginService;


    @PostMapping("/user/account/token")
    public R<Map<String, Object>> getToken(@Validated @RequestBody LoginBody loginBody){
        Map<String, Object> ajax = new HashMap<>();
        // 生成令牌
        String token = loginService.getToken(loginBody.getUsername(), loginBody.getPassword());
        ajax.put(Constants.TOKEN, token);
        return R.ok(ajax);
    }

}


