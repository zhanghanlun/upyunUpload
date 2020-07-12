package com.zhanghanlun.upyun.upyun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;

public class UpYunUtils {
    public static final String VERSION = "upyun-java-sdk/4.2.0";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public UpYunUtils() {
    }

    public static String getPolicy(Map<String, Object> paramMap) {
        JSONObject obj = new JSONObject(paramMap);
        return Base64Coder.encodeString(obj.toString());
    }

    public static String getSignature(String policy, String secretKey) {
        return md5(policy + "&" + secretKey);
    }

    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException var7) {
            throw new RuntimeException("UTF-8 is unsupported", var7);
        } catch (NoSuchAlgorithmException var8) {
            throw new RuntimeException("MessageDigest不支持MD5Util", var8);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        byte[] var3 = hash;
        int var4 = hash.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            if ((b & 255) < 16) {
                hex.append("0");
            }

            hex.append(Integer.toHexString(b & 255));
        }

        return hex.toString();
    }

    public static String md5(File file, int blockSize) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            FileInputStream in = new FileInputStream(file);
            byte[] buffer = new byte[blockSize];

            int length;
            while((length = in.read(buffer)) > 0) {
                messageDigest.update(buffer, 0, length);
            }

            byte[] hash = messageDigest.digest();
            StringBuilder hex = new StringBuilder(hash.length * 2);
            byte[] var8 = hash;
            int var9 = hash.length;

            for(int var10 = 0; var10 < var9; ++var10) {
                byte b = var8[var10];
                if ((b & 255) < 16) {
                    hex.append("0");
                }

                hex.append(Integer.toHexString(b & 255));
            }

            return hex.toString();
        } catch (FileNotFoundException var12) {
            throw new RuntimeException("file not found", var12);
        } catch (IOException var13) {
            throw new RuntimeException("file get md5 failed", var13);
        } catch (NoSuchAlgorithmException var14) {
            throw new RuntimeException("MessageDigest不支持MD5Util", var14);
        }
    }

    public static String md5(byte[] bytes) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(bytes);
        } catch (NoSuchAlgorithmException var7) {
            throw new RuntimeException("MessageDigest不支持MD5Util", var7);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        byte[] var3 = hash;
        int var4 = hash.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            if ((b & 255) < 16) {
                hex.append("0");
            }

            hex.append(Integer.toHexString(b & 255));
        }

        return hex.toString();
    }

    public static String sign(String method, String date, String path, String userName, String password, String md5) throws UpException {
        StringBuilder sb = new StringBuilder();
        String sp = "&";
        sb.append(method);
        sb.append(sp);
        sb.append(path);
        sb.append(sp);
        sb.append(date);
        if (md5 != null && md5.length() > 0) {
            sb.append(sp);
            sb.append(md5);
        }

        String raw = sb.toString().trim();
        Object var9 = null;

        byte[] hmac;
        try {
            hmac = calculateRFC2104HMACRaw(password, raw);
        } catch (Exception var11) {
            throw new UpException("calculate SHA1 wrong.");
        }

        return hmac != null ? "UPYUN " + userName + ":" + Base64Coder.encodeLines(hmac).trim() : null;
    }

    public static byte[] calculateRFC2104HMACRaw(String key, String data) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        byte[] keyBytes = key.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        return mac.doFinal(data.getBytes());
    }
}
