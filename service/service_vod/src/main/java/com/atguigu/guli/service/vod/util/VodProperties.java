package com.atguigu.guli.service.vod.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/5/9 10:28
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "aliyun.vod")
public class VodProperties {
    private String keyid;
    private String keysecret;
    private String templateGroupId;
    private String workflowId;
}
