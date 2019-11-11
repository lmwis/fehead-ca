package com.fehead.open.ca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Description:
 * @Author lmwis
 * @Date 2019-11-11 18:48
 * @Version 1.0
 */
@SpringBootApplication
@EnableEurekaClient
public class FeheadCAApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeheadCAApplication.class,args);
    }
}
