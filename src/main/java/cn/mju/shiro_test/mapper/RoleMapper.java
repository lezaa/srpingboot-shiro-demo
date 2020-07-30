package cn.mju.shiro_test.mapper;

import cn.mju.shiro_test.domain.Role;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleMapper {


    Role getRolesByRoleId(Integer roleId);


}
