package com.atguigu.guli.service.edu.service.impl;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.entity.*;
import com.atguigu.guli.service.edu.entity.form.CourseInfoForm;
import com.atguigu.guli.service.edu.entity.vo.*;
import com.atguigu.guli.service.edu.feign.OssFileService;
import com.atguigu.guli.service.edu.mapper.*;
import com.atguigu.guli.service.edu.service.CourseService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author Helen
 * @since 2022-04-01
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    @Autowired
    private CourseDescriptionMapper courseDescriptionMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CourseCollectMapper courseCollectMapper;

    @Autowired
    private OssFileService ossFileService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveCourseInfo(CourseInfoForm courseInfoForm) {

        //保存Course
        Course course = new Course();

        //拷贝同名属性
        BeanUtils.copyProperties(courseInfoForm,course);
        course.setStatus(Course.COURSE_DRAFT);
        baseMapper.insert(course);

        //保存CourseDescription
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(course.getId());
        courseDescriptionMapper.insert(courseDescription);
        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseInfoById(String id) {
        //根据id获取Course
        Course course=baseMapper.selectById(id);
        //根据id获取CourseDescription
        CourseDescription courseDescription=courseDescriptionMapper.selectById(id);
        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course,courseInfoForm);
        courseInfoForm.setDescription(courseDescription.getDescription());
        //组装成CourseInfoForm
        return courseInfoForm;
    }

    @Override
    public void updateCourseInfoById(CourseInfoForm courseInfoForm) {

        //更新Course
        Course course = new Course();

        //拷贝同名属性
        BeanUtils.copyProperties(courseInfoForm,course);
        baseMapper.updateById(course);

        //更新CourseDescription
        CourseDescription courseDescription = new CourseDescription();
        courseDescription.setDescription(courseInfoForm.getDescription());
        courseDescription.setId(courseInfoForm.getId());
        courseDescriptionMapper.updateById(courseDescription);
    }

    @Override
    public IPage<CourseVo> selectPage(Long page, Long limit, CourseQueryVo courseQueryVo) {
        //组装查询条件
        QueryWrapper<CourseVo> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("c.gmt_create");
        String title=courseQueryVo.getTitle();
        String teacherId=courseQueryVo.getTeacherId();
        String subjectParentId=courseQueryVo.getSubjectParentId();
        String subjectId=courseQueryVo.getSubjectId();

        if(!StringUtils.isEmpty(title)){
            queryWrapper.like("c.title",title);
        }
        if(!StringUtils.isEmpty(teacherId)){
            queryWrapper.eq("c.teacher_id",teacherId);
        }
        if(!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("c.subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("c.subject_id",subjectId);
        }

        //组装分页
        Page<CourseVo> pageParam = new Page<>(page,limit);

        //执行查询
        //只需要在mapper层传入封装好的分页组件即可，sql分页条件组装的过程由mp自动完成
        List<CourseVo> records = baseMapper.selectPageByCourseQueryVo(pageParam,queryWrapper);
        //将records设置到pageParam中
        return pageParam.setRecords(records);
    }

    @Override
    public boolean removeCoverById(String id) {
        Course course=baseMapper.selectById(id);
        if(course!=null){
            String cover=course.getCover();
            if(!StringUtils.isEmpty(cover)){
                R r=ossFileService.removeFile(cover);
                return r.getSuccess();
            }
        }
        return false;
    }

    /**
     * 数据库中外键约束的设置：
     *    互联网分布式项目中不允许使用外键与级联更新，一切设计级联的操作不要依赖数据库层，要在业务层解决
     *
     * 如果业务层解决级联删除功能
     *    那么先删除子表数据，再删除附表数据
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeCourseById(String id) {

        //课时信息：video
        QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", id);
        videoMapper.delete(videoQueryWrapper);

        //章节信息：chapter
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", id);
        chapterMapper.delete(chapterQueryWrapper);

        //评论信息：comment
        QueryWrapper<Comment> commentQueryWrapper = new QueryWrapper<>();
        commentQueryWrapper.eq("course_id", id);
        commentMapper.delete(commentQueryWrapper);

        //收藏信息：course_collect
        QueryWrapper<CourseCollect> courseCollectQueryWrapper=new QueryWrapper<>();
        courseCollectQueryWrapper.eq("course_id",id);
        courseCollectMapper.delete(courseCollectQueryWrapper);

        //课程详情：course_description
        courseDescriptionMapper.deleteById(id);

        //课程信息：course
        return this.removeById(id);
    }

    @Override
    public CoursePublishVo getCoursePublishById(String id) {
        return baseMapper.selectCoursePublishVoById(id);
    }

    @Override
    public boolean publishCourseById(String id) {
        Course course=new Course();
        course.setId(id);
        course.setStatus(Course.COURSE_NORMAL);
        return this.updateById(course);
    }

    @Override
    public List<Course> webSelectList(WebCourseQueryVo webCourseQueryVo) {
        QueryWrapper<Course> courseQueryWrapper = new QueryWrapper<Course>();
        //查看已经发布的课程
        courseQueryWrapper.eq("status",Course.COURSE_NORMAL);
        if(!StringUtils.isEmpty(webCourseQueryVo.getSubjectParentId())){
            courseQueryWrapper.eq("subject_parent_id",webCourseQueryVo.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(webCourseQueryVo.getSubjectId())){
            courseQueryWrapper.eq("subject_id",webCourseQueryVo.getSubjectId());
        }
        if(!StringUtils.isEmpty(webCourseQueryVo.getBuyCountSort())){
            courseQueryWrapper.orderByDesc("buy_count");
        }
        if(!StringUtils.isEmpty(webCourseQueryVo.getGmtCreateSort())){
            courseQueryWrapper.orderByDesc("gmt_create");
        }
        if(!StringUtils.isEmpty(webCourseQueryVo.getPriceSort())){
            if(webCourseQueryVo.getType()==null||webCourseQueryVo.getType()==1){
                courseQueryWrapper.orderByAsc("price");
            }else{
                courseQueryWrapper.orderByDesc("price");
            }
        }
        return baseMapper.selectList(courseQueryWrapper);
    }

    /**
     * 获取课程信息并更新浏览量
     * @param courseId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public WebCourseVo selectWebCourseVoById(String courseId) {
        //更新课程浏览数
        Course course=baseMapper.selectById(courseId);
        course.setViewCount(course.getViewCount()+1);
        baseMapper.updateById(course);
        return baseMapper.selectWebCourseVoById(courseId);
    }

    @Cacheable(value = "index",key = "'selectHotCourse'")
    @Override
    public List<Course> selectHotCourse() {
        QueryWrapper<Course> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("view_count");
        queryWrapper.last("limit 8");
        return baseMapper.selectList(queryWrapper);
    }

}
