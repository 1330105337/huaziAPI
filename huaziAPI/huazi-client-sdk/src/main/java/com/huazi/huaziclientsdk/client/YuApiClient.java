package com.huazi.huaziclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.huazi.huaziclientsdk.model.User;

import java.util.HashMap;
import java.util.Map;

import static com.huazi.huaziclientsdk.utils.SignUtils.genSign;

/**
 * 调用第三方接口的客户端
 * @author huazi
 */
public class YuApiClient {

    private String accessKey;
    private String secretKey;

    public YuApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }


    public String getNameByGet(String name){
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result= HttpUtil.get("http://localhost:8123/api/name/", paramMap);
        System.out.println(result);
        return result;
    }


    public String getNameByPost( String name){
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result= HttpUtil.post("http://localhost:8123/api/name/", paramMap);
        System.out.println(result);
        return result;
    }
    private Map<String,String> getHanderMap(String body){
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("accessKey",accessKey);
       // hashMap.put("secretKey",secretKey);
        hashMap.put("nonce", RandomUtil.randomNumbers(4));
        hashMap.put("timestamp",String.valueOf(System.currentTimeMillis() / 1000));
        hashMap.put("body",body);
        hashMap.put("sign",genSign(body,secretKey));

        return hashMap;
    }


    public String getUsernameByPost( User user){
        String json= JSONUtil.toJsonStr(user);
        HttpResponse execute = HttpRequest.post("http://localhost:8123/api/name/user")
                .addHeaders(getHanderMap(json))
                .body(json)
                .execute();
        System.out.println(execute.getStatus());
        String result = execute.body();
        System.out.println(result);
        return result;
    }
}
