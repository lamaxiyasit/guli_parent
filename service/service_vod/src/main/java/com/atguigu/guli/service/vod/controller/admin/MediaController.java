package com.atguigu.guli.service.vod.controller.admin;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.ExceptionUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.vod.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/5/9 13:55
 * @Version 1.0
 */
@Api(description = "阿里云视频点播")
@CrossOrigin
@RestController
@RequestMapping("/admin/vod/media")
@Slf4j
public class MediaController {

    @Autowired
    private VideoService videoService;

    @PostMapping("upload")
    public R upload(
            @ApiParam(value = "文件",required = true)
            @RequestParam("file") MultipartFile file){
        try{
            InputStream inputStream= file.getInputStream();;
            String originalFileName= file.getOriginalFilename();
            String videoId = videoService.uploadVideo(inputStream,originalFileName);
            return R.ok().message("视频上传成功").data("videoId",videoId);
        }catch (IOException e){
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_TOMCAT_ERROR);
        }
    }

    @DeleteMapping("remove/{vodId}")
    public R removeVideo(
            @ApiParam(value="阿里云视频id",required = true)
            @PathVariable String vodId){
        try{
            videoService.removeVideo(vodId);
            return R.ok().message("视频删除成功");
        }catch (Exception e){
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }

    @DeleteMapping("remove")
    public R removeVideoByIdList(
            @ApiParam(value="阿里云视频列表",required = true)
            @RequestBody List<String> videoIdList){
        try{
            videoService.removeVideoByIdList(videoIdList);
            return R.ok().message("视频删除成功");
        }catch (Exception e){
            log.error(ExceptionUtils.getMessage(e));
            throw new GuliException(ResultCodeEnum.VIDEO_DELETE_ALIYUN_ERROR);
        }
    }
}
