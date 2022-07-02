package com.atguigu.guli.service.edu.feign.fallback;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.feign.OssFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/4/18 20:41
 * @Version 1.0
 */
@Service
@Slf4j
public class OssFileServiceFallBack implements OssFileService {

    @Override
    public R test() {
        log.info("读取超时");
        return R.error();
    }

    @Override
    public R removeFile(String url) {
        log.info("熔断保护");
        return R.error();
    }
}
