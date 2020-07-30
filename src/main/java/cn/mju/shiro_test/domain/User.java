package cn.mju.shiro_test.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
public class User implements Serializable {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private Long phone;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",
            pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",
            pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private Set<Role> roles;




}
