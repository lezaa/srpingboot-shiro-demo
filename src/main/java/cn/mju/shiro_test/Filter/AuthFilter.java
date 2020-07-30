package cn.mju.shiro_test.Filter;

import cn.mju.shiro_test.Exception.SystemException;
import cn.mju.shiro_test.base.Status;
import cn.mju.shiro_test.mapper.UserMapper;
import cn.mju.shiro_test.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Slf4j
public class AuthFilter extends FormAuthenticationFilter {
    @Autowired
    UserMapper userMapper;
    /**
     * 判断token是否为空、过期
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String token = getRequestToken((HttpServletRequest) request);
        if (token == null||StringUtils.isBlank(token)){
            return false;
        }
        Claims claims = JwtUtil.checkJWT(token);
        if (JwtUtil.isTokenExpired(claims.getExpiration())) {
            throw new SystemException(Status.Token_Expried.getCode(),Status.Token_Expried.getStandardMessage());
        }
        String username = (String)claims.get("username");
        if (userMapper.getUserByUsername(username) == null){
            throw new SystemException(Status.THIS_USERINFO_NOT_EXITS.getCode(),Status.THIS_USERINFO_NOT_EXITS.getStandardMessage());
        }
        return true;
    }

    /**
     * 上面的方法如果返回false,则接下来会执行这个方法,如果返回为true,则不会执行这个方法
     * 判断是否为登录url,进一步判断请求是不是post
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        //是否是登录请求
        if (isLoginRequest(request, response)) {
            //是否是post请求
            if (isLoginSubmission(request, response)) {
                return true;
            }
        }
        throw new SystemException(Status.Token_Blank.getCode(),Status.Token_Blank.getStandardMessage());
    }

    /**
     * 获取请求中的token,首先从请求头中获取,如果没有,则尝试从请求参数中获取
     *
     * @param request
     * @return
     */
    private String getRequestToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (StringUtils.isBlank(token)) {
            token = request.getParameter("token");
        }
        return token;
    }






    }
