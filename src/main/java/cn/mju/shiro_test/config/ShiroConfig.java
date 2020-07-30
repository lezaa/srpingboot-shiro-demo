package cn.mju.shiro_test.config;

import cn.mju.shiro_test.Filter.AuthFilter;
import cn.mju.shiro_test.shiro.UserRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;


@Configuration
public class ShiroConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /**
     * 创建ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager
                                                                ,@Qualifier("myFilter") FormAuthenticationFilter myFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        /**
         * Shiro内置过滤器：
         *     anon：无需认证（登录）可以访问
         *     authc：必须认证才可以访问
         *     user:如果使用remenberMe的功能可以直接访问
         *     perms:该资源必须得到资源权限才可以访问
         *     roles:所拥有角色，用法和perms一样
         *
         */
        //默认登录页面，拦截到资源会自动跳转到登录页面
        shiroFilterFactoryBean.setLoginUrl("/user/login");
        shiroFilterFactoryBean.setSuccessUrl("/main");
        //设置未授权页面
        //shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");

        //拦截器
        HashMap<String, Filter> filter = new HashMap<>();
        filter.put("myFilter",myFilter);

        shiroFilterFactoryBean.setFilters(filter);
        //设置过滤页面与相应的权限
        //路径过滤链
        LinkedHashMap<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/**", "myFilter");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);



        return shiroFilterFactoryBean;
    }




    /**
     * 创建Realm：shiro连接数据的桥梁
     */
    @Bean("userRealm")
    public UserRealm getRealm(@Qualifier("matcher") HashedCredentialsMatcher hashedCredentialsMatcher) {
        UserRealm userRealm = new UserRealm();
        //关闭认证缓存
        userRealm.setAuthenticationCachingEnabled(false);
        //将规则匹配器注入到realm里
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return userRealm;
    }



    /**
     * 密码校验规则匹配器
     *
     * @return
     */
    @Bean("matcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {

        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        //指定加密方式为MD5
        credentialsMatcher.setHashAlgorithmName("MD5");
        //指定加密次数
        credentialsMatcher.setHashIterations(1024);
        //是否存储为16进制
        credentialsMatcher.setStoredCredentialsHexEncoded(true);

        return credentialsMatcher;
    }

    /**
     * 自定义会话管理（redis）
     */
    @Bean
    public DefaultWebSessionManager sessionManager(){
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setSessionDAO(redisSessionDAO());
        defaultWebSessionManager.setGlobalSessionTimeout(3600 * 1000);//过期时间，默认半小时
        defaultWebSessionManager.setSessionIdUrlRewritingEnabled(false);
        return defaultWebSessionManager;


    }

    // RedisSessionDao 插件
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    // RedisManager
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        //redisManager().setTimeout(timeout);
        if (!password.equals("") && password != null){
            redisManager.setPassword(password);
        }

        return redisManager;

    }



    /**
     * 自定义缓存（redis）
     */
    public RedisCacheManager cacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        //redisCacheManager.setPrincipalIdFieldName("id");
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * 记住我cookie
     */
    private SimpleCookie rememberMeCookie() {
        // 设置 cookie 名称，对应 login.html 页面的 <input type="checkbox" name="rememberMe"/>
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        // 设置 cookie 的过期时间，单位为秒 一个月
        cookie.setMaxAge(3600 * 30);
        return cookie;
    }

    private CookieRememberMeManager rememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // rememberMe cookie 加密的密钥
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        return cookieRememberMeManager;
    }


    /**
     * 注解aop控制权限（不用过滤链的方式），需要导入aop依赖
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(@Qualifier("securityManager") DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


    /**
     * 创建DefaultWebSecurityManager安全管理器
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm,
                                                                  @Qualifier("sessionManager") DefaultWebSessionManager sessionManager
    ) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        securityManager.setSessionManager(sessionManager);//自定义会话管理
        securityManager.setCacheManager(cacheManager());//自定义缓存管理
        securityManager.setRememberMeManager(rememberMeManager());//记住我cookie

        return securityManager;
    }

    @Bean
    public FilterRegistrationBean<Filter> registration(@Qualifier("myFilter") AuthFilter authFilter) {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<Filter>(authFilter);
        registration.setEnabled(false);
        return registration;
    }


    @Bean("myFilter")
    public AuthFilter getFormAuthenticationFilter(){
        return new AuthFilter();

        }


}
