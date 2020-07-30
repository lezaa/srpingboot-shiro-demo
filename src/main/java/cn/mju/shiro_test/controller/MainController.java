package cn.mju.shiro_test.controller;

import cn.mju.shiro_test.base.ApiResponse;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/main")
    @RequiresRoles("user")
    @RequiresPermissions("update:user")
    public ApiResponse blogMain(){
        return ApiResponse.ofSuccess("这是主页啊啊啊啊啊啊啊啊啊啊");
    }
}
