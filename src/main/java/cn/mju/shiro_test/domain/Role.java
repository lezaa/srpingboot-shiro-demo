package cn.mju.shiro_test.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
public class Role implements Serializable {
    private Integer roleId;
    private String roleName;
    private String description;
    private Set<Perm> perms;


}
