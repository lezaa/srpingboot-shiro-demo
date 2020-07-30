package cn.mju.shiro_test.service;


import cn.mju.shiro_test.domain.User;


public interface UserService {

    User findUserByUserName(String username);


    void addUser(User user);


}
