package com.atguigu.guli.service.edu.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/4/2 16:41
 * @Version 1.0
 */
@ApiModel("Teacher查询对象")
@Data
public class TeacherQueryVo {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "讲师姓名")
    private String name;
    @ApiModelProperty(value = "讲师级别")
    private Integer level;
    @ApiModelProperty(value = "开始时间")
    private String joinDateBegin;
    @ApiModelProperty(value = "结束时间")
    private String joinDateEnd;
}
