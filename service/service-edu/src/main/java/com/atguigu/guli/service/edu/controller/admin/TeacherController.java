package com.atguigu.guli.service.edu.controller.admin;


import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.vo.TeacherQueryVo;
import com.atguigu.guli.service.edu.feign.OssFileService;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author Helen
 * @since 2022-04-01
 */

@Api(description = "讲师管理")
@RestController
@RequestMapping("/admin/edu/teacher")
@CrossOrigin
@Slf4j
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private OssFileService ossFileService;

    @ApiOperation("所有讲师列表")
    @GetMapping("list")
    public R listAll(){
        List<Teacher> records=teacherService.list();
        return R.ok().data("items",records);
    }

    @ApiOperation(value="根据ID删除讲师",notes = "根据ID删除讲师")
    @DeleteMapping("remove/{id}")
    public R removeById(@ApiParam(value = "讲师ID",required = true) @PathVariable("id") String id) throws Exception {
        teacherService.removeAvatarById(id);
        boolean result = teacherService.removeById(id);
        if(result){
            return R.ok().message("删除成功");
        }
        else{
            return R.error().message("删除失败");
        }
    }

    @ApiOperation("分页讲师列表")
    @GetMapping("list/{page}/{limit}")
    public R listPage(@ApiParam(value = "当前页码",required = true) @PathVariable("page") Long page,
                      @ApiParam(value = "每页记录数",required = true) @PathVariable("limit") Long limit,
                      @ApiParam("讲师列表查询对象") TeacherQueryVo teacherQueryVo){
        IPage<Teacher> pageModel=teacherService.selectPage(page,limit,teacherQueryVo);
        List<Teacher> records=pageModel.getRecords();
        long total=pageModel.getTotal();
        return R.ok().data("total",total).data("rows",records);
    }

    @ApiOperation("新增讲师")
    @PostMapping("save")
    public R save(@ApiParam(value="讲师对象",required = true) @RequestBody Teacher teacher){
        boolean result=teacherService.save(teacher);
        if(result){
            return R.ok().message("保存成功");
        }
        else{
            return R.error().message("保存失败");
        }
    }

    @ApiOperation("更新讲师")
    @PutMapping("update")
    public R updateById(@ApiParam("讲师对象") @RequestBody Teacher teacher){
        boolean result=teacherService.updateById(teacher);
        if(result){
            return R.ok().message("更新成功");
        }
        else{
            return R.error().message("更新失败");
        }
    }

    @ApiOperation("根据讲师id获取信息")
    @GetMapping("get/{id}")
    public R getById(@ApiParam("讲师id") @PathVariable("id") String id){
        Teacher teacher = teacherService.getById(id);
        if(teacher!=null){
            return R.ok().data("item",teacher);
        }
        else{
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据id列表删除讲师")
    @DeleteMapping("batch-remove")
    public R removeRows(
            @ApiParam(value = "讲师id列表",required = true)
            @RequestBody List<String> idList){
        System.out.println("批量删除");
        boolean result=teacherService.removeByIds(idList);
        if(result){
            return R.ok().message("删除成功");
        }
        else{
            return R.error().message("数据不存在");
        }
    }

    @ApiOperation("根据左关键字查询讲师名列表")
    @GetMapping("list/name/{key}")
    public R selectNameListByKey(
            @ApiParam(value = "查询关键字",required = true)
            @PathVariable String key){
        List<Map<String, Object>> nameList = teacherService.selectNameListByKey(key);
        return R.ok().data("nameList", nameList);
    }

    @ApiOperation("测试服务调用")
    @GetMapping("test")
    public R test(){
        ossFileService.test();
        return R.ok();
    }

    @GetMapping("/message1")
    public String message1(){
        return "message1";
    }

    @GetMapping("/message2")
    public String message2(){
        return "message2";
    }
}

