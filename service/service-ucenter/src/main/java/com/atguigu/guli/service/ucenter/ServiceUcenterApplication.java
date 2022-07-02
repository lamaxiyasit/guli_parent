package com.atguigu.guli.service.ucenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/5/23 10:42
 * @Version 1.0
 */
@SpringBootApplication
@ComponentScan({"com.atguigu.guli"})
@EnableDiscoveryClient
public class ServiceUcenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceUcenterApplication.class,args);
    }
}
