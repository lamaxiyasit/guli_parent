package com.atguigu.guli.service.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.vod.service.VideoService;
import com.atguigu.guli.service.vod.util.AliyunVodSDKUtils;
import com.atguigu.guli.service.vod.util.VodProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.List;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/5/9 10:56
 * @Version 1.0
 */
@Service
@Slf4j
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VodProperties vodProperties;

    @Override
    public String uploadVideo(InputStream file, String originalFileName) {
        String title=originalFileName.substring(0,originalFileName.lastIndexOf("."));
        UploadStreamRequest request=new UploadStreamRequest(
                vodProperties.getKeyid(),
                vodProperties.getKeysecret(),
                title,originalFileName,file);
        /*模板组ID*/
//        request.setTemplateGroupId(vodProperties.getTemplateGroupId());
        /*工作流ID*/
//        request.setWorkflowId(vodProperties.getWorkflowId());
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        String videoId=response.getVideoId();
        if(StringUtils.isEmpty(videoId)){
            log.error("阿里云上传失败："+response.getCode()+"-"+response.getMessage());
            throw new GuliException(ResultCodeEnum.VIDEO_UPLOAD_ALIYUN_ERROR);
        }
        return videoId;
    }

    @Override
    public void removeVideo(String videoId) throws ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());
        DeleteVideoRequest request = new DeleteVideoRequest();
        request.setVideoIds(videoId);
        client.getAcsResponse(request);
    }

    @Override
    public void removeVideoByIdList(List<String> videoIdList) throws ClientException {
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());
        DeleteVideoRequest request = new DeleteVideoRequest();

        int size=videoIdList.size();//id列表长度
        StringBuffer idListStr=new StringBuffer();//组装好的字符串
        for(int i=0;i<size;i++){
            idListStr.append(videoIdList.get(i));
            if(i==size-1||i%20==19){ //假设size<=20
                //删除
                log.info("idListStr = " + idListStr.toString());
                request.setVideoIds(idListStr.toString());
                client.getAcsResponse(request);
                idListStr=new StringBuffer();
            }else if(i%20<19){
                idListStr.append(",");
            }
        }
    }

    @Override
    public String getPlayAuth(String videoSourceId) throws ClientException {
        //初始化client对象
        DefaultAcsClient client = AliyunVodSDKUtils.initVodClient(vodProperties.getKeyid(), vodProperties.getKeysecret());
        //创建请求对象
        GetVideoPlayAuthRequest request=new GetVideoPlayAuthRequest();
        request.setVideoId(videoSourceId);
        //获取响应
        GetVideoPlayAuthResponse response=client.getAcsResponse(request);
        return response.getPlayAuth();
    }

    /**
     * 删除视频
     * @param client 发送请求客户端
     * @return DeleteVideoResponse 删除视频响应数据
     * @throws Exception
     */
//    public static DeleteVideoResponse deleteVideo(DefaultAcsClient client) throws Exception {
//        DeleteVideoRequest request = new DeleteVideoRequest();
//        //支持传入多个视频ID，多个用逗号分隔
//        request.setVideoIds("VideoId1,VideoId2");
//        return client.getAcsResponse(request);
//    }
}
