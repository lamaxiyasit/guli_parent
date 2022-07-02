package com.atguigu.guli.service.edu.service.impl;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.atguigu.guli.service.edu.entity.Chapter;
import com.atguigu.guli.service.edu.entity.Video;
import com.atguigu.guli.service.edu.entity.vo.ChapterVo;
import com.atguigu.guli.service.edu.entity.vo.VideoVo;
import com.atguigu.guli.service.edu.mapper.ChapterMapper;
import com.atguigu.guli.service.edu.mapper.VideoMapper;
import com.atguigu.guli.service.edu.service.ChapterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

    @Autowired
    private VideoMapper videoMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean removeChapterById(String id) {
        //课时信息：video
        QueryWrapper<Video> videoQueryWrapper=new QueryWrapper<>();
        videoQueryWrapper.eq("chapter_id",id);
        videoMapper.delete(videoQueryWrapper);
        //章节信息：chapter
        return this.removeById(id);
    }

    @Override
    public List<ChapterVo> nestedList(String courseId) {
        //组装章节列表：List<ChapterVo>

        //方案1：效率低 1+n sql
        //通过course_id获取章节列表信息：List<Chapter>
        //遍历List<Chapter>{
        //    通过chapter_id查询List<Video>
        //}

        //方案2：效率高 1+1 sql
        //通过course_id获取章节信息列表：List<Chapter> sql
        //通过course_id查询List<Video> sql

        //获取章节信息
        QueryWrapper<Chapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id",courseId);
        chapterQueryWrapper.orderByAsc("sort","id");
        List<Chapter> chapterList = baseMapper.selectList(chapterQueryWrapper);
        //获取课时信息
        QueryWrapper<Video> videoQueryWrapper=new QueryWrapper<>();
        videoQueryWrapper.eq("course_id",courseId);
        videoQueryWrapper.orderByAsc("sort","id");
        List<Video> videoList=videoMapper.selectList(videoQueryWrapper);
        //组装章节列表 List<ChapterVo>
        List<ChapterVo> chapterVoList=new ArrayList<>();
        for(int i=0;i<chapterList.size();i++){

            Chapter chapter=chapterList.get(i);
            ChapterVo chapterVo=new ChapterVo();
            BeanUtils.copyProperties(chapter,chapterVo);

            List<VideoVo> videoVoList=new ArrayList<>();
            for(int j=0;j<videoList.size();j++){
                Video video=videoList.get(j);
                if(chapter.getId().equals(video.getChapterId())){
                    VideoVo videoVo=new VideoVo();
                    BeanUtils.copyProperties(video,videoVo);
                    videoVoList.add(videoVo);
                }
            }
            chapterVo.setChildren(videoVoList);
            chapterVoList.add(chapterVo);
        }
        return chapterVoList;
    }
}
