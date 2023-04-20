package com.example.huaziinterface.cotroller;

import com.huazi.huaziclientsdk.model.User;
import com.huazi.huaziclientsdk.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping("/")
    public String getNameByGet(String name){
        return "GET 名字是"+name;
    }

    @PostMapping("/")
    public String getNameByPost(@RequestParam String name){
        return "POST 名字是"+name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request){
//        String accessKey = request.getHeader("accessKey");
//        String nonce = request.getHeader("nonce");
//        String body = request.getHeader("body");
//        String timestamp = request.getHeader("timestamp");
//        String sign = request.getHeader("sign");
//        if (!accessKey.equals("huazi")){
//        throw new RuntimeException("无权限");
//        }
//        if (Long.parseLong(nonce)>10000){
//            throw new RuntimeException("无权限");
//        }
//        //实际情况拿到密钥
//        String genSign = SignUtils.genSign(body, "abcdefg");
//        if (!sign.equals(genSign)){
//            throw new RuntimeException("签名不一致");
//        }
        return "POST 用户名是"+user.getUsername();
    }
}
