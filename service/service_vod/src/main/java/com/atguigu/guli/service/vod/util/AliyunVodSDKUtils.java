package com.atguigu.guli.service.vod.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/5/9 16:48
 * @Version 1.0
 */
public class AliyunVodSDKUtils {
    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret) {
        String regionId="cn-shanghai";
        DefaultProfile profile=DefaultProfile.getProfile(regionId,accessKeyId,accessKeySecret);
        DefaultAcsClient client=new DefaultAcsClient(profile);
        return client;
    }
}
