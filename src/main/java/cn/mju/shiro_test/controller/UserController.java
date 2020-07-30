package cn.mju.shiro_test.controller;

import cn.mju.shiro_test.Exception.SystemException;
import cn.mju.shiro_test.base.ApiResponse;
import cn.mju.shiro_test.base.Status;
import cn.mju.shiro_test.domain.User;
import cn.mju.shiro_test.service.UserService;
import cn.mju.shiro_test.shiro.UserRealm;
import cn.mju.shiro_test.utils.JwtUtil;
import com.alibaba.fastjson.JSON;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

//@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    //登录
    @PostMapping("/login")
    @RequiresGuest
    public ApiResponse login(@RequestBody User user, boolean rememberMe) {

        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),user.getPassword(),rememberMe);
        User userByUserName = userService.findUserByUserName(user.getUsername());

        String jwtToken = JwtUtil.geneJsonWebToken(userByUserName);
        try {
            subject.login(token);
            boolean authenticated = subject.isAuthenticated();
            HashMap<String, Object> data = new HashMap<>();
            data.put("user",userByUserName);
            data.put("token",jwtToken);
            return ApiResponse.ofSuccess(data);
        } catch (UnknownAccountException e) {
            return ApiResponse.ofMessage(Status.ACCOUNT_ERROR.getCode(),Status.ACCOUNT_ERROR.getStandardMessage());
        } catch (IncorrectCredentialsException e) {
            return ApiResponse.ofMessage(Status.PASSWORD_ERROT.getCode(),Status.PASSWORD_ERROT.getStandardMessage());
        }
    }


    //登出
    @GetMapping("/logout")
    @RequiresAuthentication
    public ApiResponse logout(HttpServletRequest request) {

        RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm userRealm = (UserRealm) securityManager.getRealms().iterator().next();
        //调用userreaml里重写的方法来清空缓存
        userRealm.clearCache(SecurityUtils.getSubject().getPrincipals());
        SecurityUtils.getSubject().logout();
        return ApiResponse.ofSuccess();
    }


    //注册
    @PostMapping("/register")
    @RequiresGuest
    //@RequiresAuthentication
    public ApiResponse register(@RequestBody User user) {
        userService.addUser(user);
        return ApiResponse.ofMessage(Status.USER_REGISTER.getCode(),Status.USER_REGISTER.getStandardMessage());
    }

}
