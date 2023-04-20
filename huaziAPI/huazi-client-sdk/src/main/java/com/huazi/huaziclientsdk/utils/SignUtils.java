package com.huazi.huaziclientsdk.utils;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;

/**
 * 签名加密算法
 * @author huazi
 */
public class SignUtils {

        public static String genSign(String body, String secretKey){
        Digester md5 = new Digester(DigestAlgorithm.SHA256);
        String context=body+"."+secretKey;
        String digestHex = md5.digestHex(context);
        return digestHex;
    }
}
