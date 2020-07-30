package cn.mju.shiro_test;

import cn.mju.shiro_test.domain.Role;
import cn.mju.shiro_test.domain.User;
import cn.mju.shiro_test.mapper.RoleMapper;
import cn.mju.shiro_test.mapper.UserMapper;
import cn.mju.shiro_test.service.UserService;
import cn.mju.shiro_test.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class ShiroTestApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleMapper roleMapper;



    @Test
    public void test2(){
        StringBuffer stringBuffer = new StringBuffer(202005);
        StringBuffer reportStart = stringBuffer.insert(4, "-");
        System.out.println(reportStart+"..........");

    }


    @Test
    public void getFilm(){
        Claims claims = JwtUtil.checkJWT("eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MTEsInVzZXJuYW1lIjoi6Zu35YabIiwiZW1haWwiOiI4NzcyODI4NDBAcXEuY29tIiwiaWF0IjoxNTk1OTA4MjQ5LCJleHAiOjE1OTY1MTMwNDl9.n-yG0GAedvCs3pJIpUVq7S44qMPaYvQzr7XqkbFKt8U");
        System.out.println("......."+claims);
    }


}
