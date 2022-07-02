package com.atguigu.guli.service.ucenter.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/6/2 10:48
 * @Version 1.0
 */
@Data
public class LoginVo implements Serializable {
    private static final long serialVersionUID=1L;
    private String mobile;
    private String password;
}
