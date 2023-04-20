package com.huazi.huazigateway;

import com.example.huazicommon.model.entity.InterfaceInfo;
import com.example.huazicommon.service.InnerInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HuaziGatewayApplicationTests {
   @DubboReference
   private InnerInterfaceInfoService innerInterfaceInfoService;
    @Test
    void contextLoads() {
        String path="http://localhost:8123/api/name/user";
        String method="POST";
        InterfaceInfo interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        System.out.println(interfaceInfo);
    }

}
