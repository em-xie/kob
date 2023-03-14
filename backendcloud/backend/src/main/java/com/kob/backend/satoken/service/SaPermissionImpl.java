package com.kob.backend.satoken.service;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import com.kob.backend.domain.User;
import com.kob.common.core.domain.model.LoginUser;
import com.kob.common.enums.UserType;
import com.kob.common.helper.LoginHelper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * sa-token 权限管理实现类
 *
 * @author Lion Li
 */
@Component
public class SaPermissionImpl implements StpInterface {
    public static final String LOGIN_USER_KEY = "loginUser";
    /**
     * 获取菜单权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginUser loginUser = LoginHelper.getLoginUser();
        UserType userType = UserType.getUserType(loginUser.getUserType());
        if (userType == UserType.SYS_USER) {
            return new ArrayList<>(loginUser.getMenuPermission());
        } else if (userType == UserType.APP_USER) {
            // 其他端 自行根据业务编写
        }
        return new ArrayList<>();
    }

    /**
     * 获取角色权限列表
     */
//    @Override
//    public List<String> getRoleList(Object loginId, String loginType) {
//        LoginUser loginUser = LoginHelper.getLoginUser();

//        UserType userType = UserType.getUserType(loginUser.getUserType());
//        if (userType == UserType.SYS_USER) {
//            return new ArrayList<>(loginUser.getRolePermission());
//        } else if (userType == UserType.APP_USER) {
//            // 其他端 自行根据业务编写
//        }
//        return new ArrayList<>();
//    }

    /**
     * 获取角色权限列表
     * @return
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {

        User loginUser1 = (User) SaHolder.getStorage().get(LOGIN_USER_KEY);
        if(loginUser1 != null){
            if ("xie1" == loginUser1.getUsername()) {
                ArrayList<String> list = new ArrayList<>();
                list.add("xie1");
                return  list;
            }
        }
        User user = (User) StpUtil.getTokenSession().get(LOGIN_USER_KEY);
        SaHolder.getStorage().set(LOGIN_USER_KEY, user);
        if ("xie1".equals(user.getUsername()) ) {
            ArrayList<String> list = new ArrayList<>();
            list.add("admin");
            return  list;
        }
        return new ArrayList<>();
    }
}
