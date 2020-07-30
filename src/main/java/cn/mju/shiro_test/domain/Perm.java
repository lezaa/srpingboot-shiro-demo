package cn.mju.shiro_test.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Perm implements Serializable {
    private Integer permId;
    private String permName;
    private String description;
}
