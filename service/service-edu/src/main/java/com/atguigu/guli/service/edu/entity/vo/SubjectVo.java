package com.atguigu.guli.service.edu.entity.vo;


import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/4/20 20:17
 * @Version 1.0
 */
@Data
public class SubjectVo implements Serializable {

    private static final long serialVersionUId=1L;

    private String id;
    private String title;
    private Integer sort;
    private List<SubjectVo> children=new ArrayList<>();
}
