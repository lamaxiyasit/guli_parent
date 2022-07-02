package com.atguigu.guli.service.edu.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.guli.service.edu.entity.Subject;
import com.atguigu.guli.service.edu.entity.excel.ExcelSubjectData;
import com.atguigu.guli.service.edu.mapper.SubjectMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/4/20 15:44
 * @Version 1.0
 */
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
public class ExcelSubjectDataListener extends AnalysisEventListener<ExcelSubjectData> {

    private SubjectMapper subjectMapper;

    /**
     * 遍历每一行的记录
     * @param excelSubjectData
     * @param analysisContext
     */
    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        log.info("解析到一条记录：{}",excelSubjectData);
        //处理读取出来的数据
        String levelOneTitle = excelSubjectData.getLevelOneTitle();//一级标题
        String levelTwoTitle = excelSubjectData.getLevelTwoTitle();//二级标题
        log.info("levelOneTitle：{}",levelOneTitle);
        log.info("levelTwoTitle：{}",levelTwoTitle);

        //判断数据是否存在
        Subject subjectLevelOne = this.getByTitle(levelOneTitle);
        String parentId=null;
        if(subjectLevelOne == null){
            //组装一级类别
            Subject subject=new Subject();
            subject.setParentId("0");
            subject.setTitle(levelOneTitle);
            //存入数据库
            subjectMapper.insert(subject);
            parentId=subject.getId();
        }else{
            parentId=subjectLevelOne.getId();
        }

        //判断数据是否存在
        Subject subjectLevelTwo = this.getSubByTitle(levelTwoTitle, parentId);
        if(subjectLevelTwo==null){
            //组装二级类别
            Subject subject = new Subject();
            subject.setTitle(levelTwoTitle);
            subject.setParentId(parentId);
            subjectMapper.insert(subject);
        }
    }

    /**
     * 所有数据读取之后的收尾工作
     * @param analysisContext
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("全部数据解析完成");
    }

    /**
     * 根据一级分类的名称查询分类
     * @param title
     * @return
     */
    private Subject getByTitle(String title){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        queryWrapper.eq("parent_id","0");//一级分类
        return subjectMapper.selectOne(queryWrapper);
    }

    /**
     * 根据分类名称和父id查询查询数据是否存在
     * @param title
     * @param parent_id
     * @return
     */
    public Subject getSubByTitle(String title,String parent_id){
        QueryWrapper<Subject> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title",title);
        queryWrapper.eq("parent_id",parent_id);//一级分类
        return subjectMapper.selectOne(queryWrapper);
    }
}
