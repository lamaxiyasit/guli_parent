package com.atguigu.guli.service.edu.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/4/20 15:40
 * @Version 1.0
 */
@Data
public class ExcelSubjectData {

    @ExcelProperty(value="一级分类")
    private String levelOneTitle;

    @ExcelProperty(value="二级分类")
    private String levelTwoTitle;

}
