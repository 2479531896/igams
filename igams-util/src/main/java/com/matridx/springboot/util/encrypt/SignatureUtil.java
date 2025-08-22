package com.matridx.springboot.util.encrypt;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class SignatureUtil {

    public SignatureUtil() {
    }

    public static String rsaSign(String content, String privateKey, String charset) {
        try {
            PrivateKey priKey = getPrivateKeyFromPKCS8("RSA", new ByteArrayInputStream(privateKey.getBytes()));
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initSign(priKey);
            if (null==charset|| charset.isEmpty()) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            byte[] signed = signature.sign();
            return new String(Base64.encodeBase64(signed));
        } catch (Exception var6) {
            throw new RuntimeException("RSAcontent = " + content + "; charset = " + charset, var6);
        }
    }

    public static PrivateKey getPrivateKeyFromPKCS8(String algorithm, InputStream ins) throws Exception {
        if (ins != null && null!=algorithm&& !algorithm.isEmpty()) {
            KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
            byte[] encodedKey= Base64.decodeBase64( copyToString(ins, StandardCharsets.UTF_8));
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encodedKey));
        } else {
            return null;
        }
    }

    public static String copyToString(InputStream in, Charset charset) throws IOException {
        if (in == null) {
            return "";
        } else {
            StringBuilder out = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(in, charset);
            char[] buffer = new char[4096];

            int bytesRead;
            while((bytesRead = reader.read(buffer)) != -1) {
                out.append(buffer, 0, bytesRead);
            }

            return out.toString();
        }
    }

    /**
     * 签名字段排序
     */
    public static String getSignContent(Map<String, String> sortedParams) {
        StringBuilder content = new StringBuilder();
        List<String> keys = new ArrayList<>(sortedParams.keySet());
        Collections.sort(keys);
        int index = 0;
        for (String key : keys) {
            String value = sortedParams.get(key);
            if (null != key && null != value) {
                content.append(index == 0 ? "" : "&").append(key).append("=").append(value);
                index++;
            }
        }
        return content.toString();
    }

    public static boolean rsaCheck(String content, String sign, String publicKey, String charset) {
        try {
            PublicKey pubKey = getPublicKeyFromX509("RSA", new ByteArrayInputStream(publicKey.getBytes()));
            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initVerify(pubKey);
            if (null==charset|| charset.isEmpty()) {
                signature.update(content.getBytes());
            } else {
                signature.update(content.getBytes(charset));
            }

            return signature.verify(Base64.decodeBase64(sign.getBytes()));
        } catch (Exception var6) {
            throw new RuntimeException("RSAcontent = " + content + ",sign=" + sign + ",charset = " + charset, var6);
        }
    }

    public static PublicKey getPublicKeyFromX509(String algorithm, InputStream ins) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        byte[] encodedKey =  Base64.decodeBase64(copyToString(ins, StandardCharsets.UTF_8));
        return keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
    }

    public static String decode(String str) {
        String result = null;
        if (str != null) {
            result = URLDecoder.decode(str, StandardCharsets.UTF_8);
        }
        return result;
    }

}
