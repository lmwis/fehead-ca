package com.fehead.open.ca;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@SpringBootApplication()
@EnableEurekaClient // 注册中心
@EnableReactiveMongoRepositories // 启用mongodb支持
public class FeheadCAApplication {

    private static final Logger logger = LoggerFactory.getLogger(FeheadCAApplication.class);

    public static void main(String[] args) {

        // 注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {

                // 从注册中心实例注册列表删除
                logger.info("This should completed shut down of DiscoveryClient");
            }
        });

        SpringApplication.run(FeheadCAApplication.class,args);
    }
}
