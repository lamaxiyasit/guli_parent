package com.atguigu.guli.service.edu.controller;

import com.atguigu.guli.common.base.result.R;
import org.springframework.web.bind.annotation.*;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/4/14 09:18
 * @Version 1.0
 */
@CrossOrigin
@RestController
@RequestMapping("/user")
public class LoginController {

    /**
     * 登录
     * @return
     */
    @PostMapping("/login")
    public R login(){
        return R.ok().data("token","admin");
    }

    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/info")
    public R info(){
        return R.ok()
                .data("name","admin")
                .data("roles","[admin]")
                .data("avatar","https://s3.ifanr.com/wp-content/uploads/2020/05/16-2.jpg");
    }

    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    public R logout(){
        return R.ok();
    }
}
