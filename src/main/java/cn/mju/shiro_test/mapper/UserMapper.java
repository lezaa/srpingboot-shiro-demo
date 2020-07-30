package cn.mju.shiro_test.mapper;


import cn.mju.shiro_test.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    User getUserById(Integer userId);

    User getUserByUsername(String username);

    User getUserByPhone(Long phone);

    void addUser(User user);

    void updateUser(User user);

}
