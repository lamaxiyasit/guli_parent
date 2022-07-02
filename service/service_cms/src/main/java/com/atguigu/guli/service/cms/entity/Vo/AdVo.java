package com.atguigu.guli.service.cms.entity.Vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/5/17 15:33
 * @Version 1.0
 */
@Data
public class AdVo implements Serializable {

    private static final long serialVersionUID=1L;
    private String id;
    private String title;
    private Integer sort;
    private String type;
}
