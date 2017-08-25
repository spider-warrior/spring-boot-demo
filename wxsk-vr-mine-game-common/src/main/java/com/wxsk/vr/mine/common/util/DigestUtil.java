package com.wxsk.vr.mine.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具
 */
public class DigestUtil {

    private static final Logger logger = LogManager.getLogger(DigestUtil.class);
    private static final char[] hexCode = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    public static MessageDigest digest;

    public static String md5(String source) {
        byte[] result = digest.digest(source.getBytes());
        StringBuilder r = new StringBuilder(result.length * 2);
        for (byte b : result) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }


    static {
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error("创建[MD5]加密工具失败", e);
        }
    }

    public static void main(String[] args) {
        System.out.println(md5("yangjian"));
    }

}
