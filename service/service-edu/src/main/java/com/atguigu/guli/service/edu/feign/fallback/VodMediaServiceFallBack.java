package com.atguigu.guli.service.edu.feign.fallback;

import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.service.edu.feign.VodMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/5/9 20:20
 * @Version 1.0
 */
@Service
@Slf4j
public class VodMediaServiceFallBack implements VodMediaService {
    @Override
    public R removeVideo(String vodId) {
        log.error("熔断保护");
        return R.error();
    }

    @Override
    public R removeVideoByIdList(List<String> videoIdList) {
        log.error("熔断保护");
        return R.error();
    }
}
