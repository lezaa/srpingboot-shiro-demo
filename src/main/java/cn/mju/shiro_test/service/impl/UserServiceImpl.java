package cn.mju.shiro_test.service.impl;

import cn.mju.shiro_test.Exception.SystemException;
import cn.mju.shiro_test.domain.Role;
import cn.mju.shiro_test.domain.User;
import cn.mju.shiro_test.mapper.PermMapper;
import cn.mju.shiro_test.mapper.RoleMapper;
import cn.mju.shiro_test.mapper.UserMapper;
import cn.mju.shiro_test.service.UserService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    /*@Value("${password.salt}")
    public static String salt;*/

    @Override
    public User findUserByUserName(String username) {
        User  user = userMapper.getUserByUsername(username);
        return user;
    }

    @Override
    public void addUser(User user) {
        User user1 = passwordToMD5(user);
        userMapper.addUser(user1);
    }


    public User passwordToMD5(User user) {
        String hashAlgorithmName = "MD5";
        Object crdentials = user.getPassword();
        ByteSource salt = ByteSource.Util.bytes(user.getUsername());
        int hashIterations = 1024;
        Object password = new SimpleHash(hashAlgorithmName, crdentials, salt, hashIterations);
        user.setPassword(password.toString());
        return user;
    }
}

