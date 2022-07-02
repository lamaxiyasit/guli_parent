package com.atguigu.guli.service.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description TODO
 * @Auther chengjiahui
 * @Date 2022/5/17 15:02
 * @Version 1.0
 */
@SpringBootApplication
@ComponentScan({"com.atguigu.guli"})
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceCmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmsApplication.class);
    }
}
