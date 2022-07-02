package com.atguigu.guli.service.edu.entity.vo;

import com.atguigu.guli.service.edu.entity.Video;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/4/29 16:37
 * @Version 1.0
 */
@Data
public class ChapterVo implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;
    private String title;
    private Integer sort;
    private List<VideoVo> children=new ArrayList<>();
}
