package com.huazi.huazigateway;

import com.project.example.provider.DemoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@EnableDubbo
@Service
public class HuaziGatewayApplication {
    @DubboReference
    private DemoService demoService;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(HuaziGatewayApplication.class, args);
        HuaziGatewayApplication application = applicationContext.getBean(HuaziGatewayApplication.class);
        String result = application.doSayHello("world");
        System.out.println("result+"+result);


    }
    public String doSayHello(String name) {
        return demoService.sayHello(name);
    }

}
