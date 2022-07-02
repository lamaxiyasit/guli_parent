package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.Course;
import com.atguigu.guli.service.edu.entity.Teacher;
import com.atguigu.guli.service.edu.entity.vo.TeacherQueryVo;
import com.atguigu.guli.service.edu.feign.OssFileService;
import com.atguigu.guli.service.edu.mapper.CourseMapper;
import com.atguigu.guli.service.edu.mapper.TeacherMapper;
import com.atguigu.guli.service.edu.service.TeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sun.util.resources.cldr.pt.TimeZoneNames_pt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2022-04-01
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements TeacherService {

    @Autowired
    private OssFileService ossFileService;

    @Autowired
    private CourseMapper courseMapper;

    @Override
    public IPage<Teacher> selectPage(Long page, Long limit, TeacherQueryVo teacherQueryVo) {

        Page<Teacher> pageParam=new Page<>(page,limit);

        QueryWrapper<Teacher> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");

        if(teacherQueryVo==null){
            return baseMapper.selectPage(pageParam,queryWrapper);
        }

        String name=teacherQueryVo.getName();
        Integer level=teacherQueryVo.getLevel();
        String joinDateBegin = teacherQueryVo.getJoinDateBegin();
        String joinDateEnd = teacherQueryVo.getJoinDateEnd();

        if(!StringUtils.isEmpty(name)){
            queryWrapper.likeRight("name",name);
        }

        if(level!=null){
            queryWrapper.eq("level",level);
        }

        if(!StringUtils.isEmpty(joinDateBegin)){
            queryWrapper.ge("join_date",joinDateBegin);
        }

        if(!StringUtils.isEmpty(joinDateEnd)){
            queryWrapper.le("join_date",joinDateEnd);
        }

        //只能单表查询
        return baseMapper.selectPage(pageParam,queryWrapper);
    }

    @Override
    public List<Map<String, Object>> selectNameListByKey(String key) {
        QueryWrapper<Teacher> queryWrapper=new QueryWrapper<>();
        queryWrapper.select("name");
        queryWrapper.likeRight("name",key);
        List<Map<String,Object>> list=baseMapper.selectMaps(queryWrapper);
        return list;
    }

    @Override
    public boolean removeAvatarById(String id) throws Exception {
        Teacher teacher=baseMapper.selectById(id);
        if(teacher!=null){
            String avatar=teacher.getAvatar();
            if(!StringUtils.isEmpty(avatar)){
                //删除图片
                R r=ossFileService.removeFile(avatar);
                if(!r.getSuccess()){
                    throw new Exception("删除异常");
                }
            }
        }
        return false;
    }

    @Override
    public Map<String, Object> selectTeacherInfoById(String id) {
        Teacher teacher=baseMapper.selectById(id);
        List<Course> courseList=courseMapper.selectList(new QueryWrapper<Course>().eq("teacher_id",id));
        Map<String,Object> map=new HashMap<>();
        map.put("teacher",teacher);
        map.put("courseList",courseList);
        return map;
    }

    @Cacheable(value = "index",key = "'selectHotTeacher'")
    @Override
    public List<Teacher> selectHotTeacher() {
        QueryWrapper<Teacher> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        queryWrapper.last("limit 4");
        return baseMapper.selectList(queryWrapper);
    }
}
