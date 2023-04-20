package com.project.example;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;


/**
 * @author:22603
 * @Date:2023/3/14 11:38
 */
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@MapperScan("com.project.example.mapper")
@EnableDubbo
public class MyApplication {


    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

}
