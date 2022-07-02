package com.atguigu.guli.service.edu.entity.vo;

import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/4/29 15:52
 * @Version 1.0
 */
@Data
public class VideoVo implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;
    private String title;
    private Boolean free;
    private Integer sort;

    private String videoSourceId;
}
