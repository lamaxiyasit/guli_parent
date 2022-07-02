package com.atguigu.guli.service.sms.service.impl;

import com.atguigu.guli.service.sms.service.TengXunSmsService;
import com.atguigu.guli.service.sms.util.TengXunSmsProperties;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;

import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;


import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/5/22 15:16
 * @Version 1.0
 */
@Service
@Slf4j
public class TengXunSmsServiceImpl implements TengXunSmsService {

    @Autowired
    private TengXunSmsProperties tengXunSmsProperties;

    @Override
    public void send(String mobile,String code) {
        String jointMobile = mobile;
        String secretId = tengXunSmsProperties.getSecretId();
        String secretKey= tengXunSmsProperties.getSecretKey();
        //短信应用 ID
        String appid = tengXunSmsProperties.getAppid();
        //短信签名内容
        String sign = tengXunSmsProperties.getSign();
        //短信模板 ID
        String templateID = tengXunSmsProperties.getTemplateID();
        //+86为国家码，182********为手机号，最多不要超过200个手机号

        String[] phoneNumbers = { jointMobile};
        //模板参数: 若无模板参数，则设置为空
        String[] templateParams = { code };//对应模板中{1}
        try {
            // 实例化一个认证对象，入参需要传入腾讯云账户secretId，secretKey,此处还需注意密钥对的保密
            // 密钥可前往https://console.cloud.tencent.com/cam/capi网站进行获取
            Credential cred = new Credential(secretId, secretKey);
            // 实例化一个http选项，可选的，没有特殊需求可以跳过
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("sms.tencentcloudapi.com");
            // 实例化一个client选项，可选的，没有特殊需求可以跳过
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            // 实例化要请求产品的client对象,clientProfile是可选的
            SmsClient client = new SmsClient(cred, "ap-nanjing", clientProfile);
            // 实例化一个请求对象,每个接口都会对应一个request对象
            SendSmsRequest req = new SendSmsRequest();
            String[] phoneNumberSet1 = {jointMobile};
            req.setPhoneNumberSet(phoneNumberSet1);

            req.setSmsSdkAppId(tengXunSmsProperties.getSmsSdkAppId());
            req.setSignName("拉玛西亚公众号");
            req.setTemplateId(templateID);

            String[] templateParamSet1 = {code};
            req.setTemplateParamSet(templateParamSet1);

            // 返回的resp是一个SendSmsResponse的实例，与请求对象对应
            SendSmsResponse resp = client.SendSms(req);
            // 输出json格式的字符串回包
            System.out.println(SendSmsResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            log.error("腾讯云短信发送sdk调用失败："+e.getErrorCode()+","+e.getMessage());
//            throw new BusinessException(ResponseEnum.TENGXUN_SMS_ERROR,e);
        }
    }
}
