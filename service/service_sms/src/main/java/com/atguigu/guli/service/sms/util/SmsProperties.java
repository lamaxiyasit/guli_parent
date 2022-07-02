package com.atguigu.guli.service.sms.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/5/22 19:32
 * @Version 1.0
 */
@Data
@Component
//注意prefix要写到最后一个 "." 符号之前
@ConfigurationProperties(prefix="aliyun.sms")
public class SmsProperties {
    private String regionId;
    private String keyId;
    private String keySecret;
    private String templateCode;
    private String signName;
}
