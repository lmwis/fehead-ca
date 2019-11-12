package com.fehead.open.ca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

/**
 * @Description: 启动类
 * @Author lmwis
 * @Date 2019-11-11 18:48
 * @Version 1.0
 */
@SpringBootApplication
@EnableEurekaClient // 注册中心
@EnableReactiveMongoRepositories // 启用mongodb支持
public class FeheadCAApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeheadCAApplication.class,args);
    }
}
