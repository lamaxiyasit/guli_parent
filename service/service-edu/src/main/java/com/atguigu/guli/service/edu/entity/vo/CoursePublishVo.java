package com.atguigu.guli.service.edu.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/4/28 16:12
 * @Version 1.0
 */
@Data
public class CoursePublishVo implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;
    private String title;
    private String cover;
    private Integer lessonNum;
    private String subjectParentTitle;
    private String subjectTitle;
    private String teacherName;
    private String price;
}
