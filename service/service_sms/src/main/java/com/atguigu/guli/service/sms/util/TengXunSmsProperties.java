package com.atguigu.guli.service.sms.util;

import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/5/22 15:11
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "tengxun.sms")
public class TengXunSmsProperties {
    private String secretId;
    private String secretKey;
    private String appid;
    private String sign;
    private String templateID;
    private String smsSdkAppId;

}
