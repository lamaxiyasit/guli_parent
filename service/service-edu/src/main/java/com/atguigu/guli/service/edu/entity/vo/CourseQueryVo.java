package com.atguigu.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/4/25 09:17
 * @Version 1.0
 */
@Data
public class CourseQueryVo implements Serializable {

    private static final long serialVersionUID=1L;

    private String title;
    private String teacherId;
    private String subjectParentId;
    private String subjectId;
}
