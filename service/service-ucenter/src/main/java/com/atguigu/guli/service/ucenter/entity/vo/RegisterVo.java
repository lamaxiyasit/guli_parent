package com.atguigu.guli.service.ucenter.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/5/23 10:57
 * @Version 1.0
 */
@Data
public class RegisterVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nickname;
    private String mobile;
    private String password;
    private String code;
}
