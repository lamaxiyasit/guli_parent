package com.atguigu.guli.service.edu.controller.api;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.vo.SubjectVo;
import com.atguigu.guli.service.edu.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/5/14 20:23
 * @Version 1.0
 */
@CrossOrigin
@Api(description = "课程")
@RestController
@RequestMapping("/api/edu/subject")
@Slf4j
public class ApiSubjectController {

    @Autowired
    private SubjectService subjectService;

    @ApiOperation("嵌套数据列表")
    @GetMapping("nested-list")
    public R nestedList(){
        long start=System.currentTimeMillis();
        log.info("执行开始");
        List<SubjectVo> subjectVoList=subjectService.nestedList();
        long end=System.currentTimeMillis();
        log.info("执行结束，执行时间："+(end-start)/1000+"s");
        return R.ok().data("items",subjectVoList);
    }
}
