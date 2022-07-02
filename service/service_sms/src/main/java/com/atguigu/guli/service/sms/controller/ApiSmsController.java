package com.atguigu.guli.service.sms.controller;

import com.aliyuncs.exceptions.ClientException;
import com.atguigu.guli.common.base.result.R;
import com.atguigu.guli.common.base.result.ResultCodeEnum;
import com.atguigu.guli.common.base.util.FormUtils;
import com.atguigu.guli.common.base.util.RandomUtils;
import com.atguigu.guli.service.base.exception.GuliException;
import com.atguigu.guli.service.sms.service.SmsService;
import com.atguigu.guli.service.sms.service.TengXunSmsService;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/5/22 15:14
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/sms")
@Api(description = "短信管理")
@CrossOrigin //跨域
@Slf4j
public class ApiSmsController {
    @Autowired
    private TengXunSmsService tengXunSmsService;

    @Autowired
    private SmsService smsService;


    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("获取验证码")
    @GetMapping("send/{mobile}")
    public R getCode(@PathVariable String mobile) throws ClientException, TencentCloudSDKException {

        //校验手机号是否合法
        if(StringUtils.isEmpty(mobile) || !FormUtils.isMobile(mobile)) {
            log.error("请输入正确的手机号码 ");
            throw new GuliException(ResultCodeEnum.LOGIN_PHONE_ERROR);
        }
        //生成验证码
        String checkCode = RandomUtils.getFourBitRandom();
        //发送验证码
        tengXunSmsService.send(mobile, checkCode);
        //将验证码存入redis缓存
        redisTemplate.opsForValue().set(mobile, checkCode, 10, TimeUnit.MINUTES);
        return R.ok().message("短信发送成功");
    }
}
