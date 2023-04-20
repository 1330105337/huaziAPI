package com.project.example;

import com.project.example.service.UserInterfaceInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


import javax.annotation.Resource;

/**
 * @author:22603
 * @Date:2023/3/14 11:38
 */
@SpringBootTest
public class MyApplicationTest {

    public static void main(String[] args) {

    }

//    @Resource
//    private YuApiClient yuApiClient;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Test
    public void test(){

        boolean b = userInterfaceInfoService.invokeCount(1L, 1L);
        //assertTrue 如果为true，则运行success，反之Failure,断言（assertions）是测试方法中的核心部分，用来对测试需要满足的条件进行验证
        Assertions.assertTrue(b);

//        System.out.println(yuApiClient.getNameByGet("华子"));
//        User user = new User();
//        user.setUsername("huazi");
//        String usernameByPost = yuApiClient.getUsernameByPost(user);
//        System.out.println(usernameByPost);
//        Gson gson=new Gson();
//        String context=["name":"username","Type":"string"];

    }

}
