package com.example.huaziinterface;

import com.huazi.huaziclientsdk.client.YuApiClient;

import com.huazi.huaziclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class HuaziInterfaceApplicationTests {
    @Resource
    private YuApiClient yuApiClient;

    @Test
    void contextLoads() {
        System.out.println(yuApiClient.getNameByGet("华子"));
        User user = new User();
        user.setUsername("huazi");
        String usernameByPost = yuApiClient.getUsernameByPost(user);
        System.out.println(usernameByPost);
    }

}
