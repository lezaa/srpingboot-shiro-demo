package cn.mju.shiro_test.Exception;


import cn.mju.shiro_test.base.ApiResponse;
import cn.mju.shiro_test.base.Status;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 路径未授权异常
 */
@ControllerAdvice
//用了注解控制权限的方式后访问未授权页面则会触发异常，用过滤链的方式则会走定义好的noauth
public class ShiroException {
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public ApiResponse defaultAuthorizedExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        return ApiResponse.ofMessage(Status.NOT_AUTH.getCode(),Status.NOT_AUTH.getStandardMessage());
    }


    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseBody
    public ApiResponse defaultAuthenticatedExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {

        return ApiResponse.ofMessage(Status.NOT_LOGIN.getCode(),Status.NOT_LOGIN.getStandardMessage());
    }

}
