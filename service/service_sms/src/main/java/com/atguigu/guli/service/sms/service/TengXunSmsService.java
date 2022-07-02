package com.atguigu.guli.service.sms.service;

import com.tencentcloudapi.common.exception.TencentCloudSDKException;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/5/22 15:16
 * @Version 1.0
 */
public interface TengXunSmsService {
    void send(String mobile, String code) throws TencentCloudSDKException;
}
