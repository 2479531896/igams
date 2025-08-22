package com.matridx.igams.detection.molecule.util;

import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

/**
 * 阿里健康解密工具类
 * @author AliHealth
 * {@code @date2022年1月13日}
 */
@Component
public final class AliHealthUtil {
    /*key⻓度 */ private static final int KEY_LENGTH = 16;
    /*编码⽅式 */ private static final String CHARSET_NAME = "UTF-8";
    /*算法/加密模式/填充⽅式 */ private static final String AES_PKCS5P = "AES/ECB/PKCS5Padding";

    /*** 解密 ** @param str 需要解密的字符串 * @param merchantAesKey 服务商私有秘钥，由健康同学根据isv的appkey⽣成 * @return 解密后的字符串 */
    public static String decrypt(String str, String merchantAesKey) {
        if (StringUtils.isEmpty(merchantAesKey)) {
            throw new RuntimeException("merchantAesKey不能为空");
        }
        try {
            if (str == null) {
                return null;
            }// 判断Key是否为16位
            if (merchantAesKey.length() != KEY_LENGTH) {
                return null;
            }
            byte[] raw = merchantAesKey.getBytes(CHARSET_NAME);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(AES_PKCS5P);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec); // 先⽤base64解密
            byte[] encrypted = Base64Utils.decodeFromString(str);
            try {
                byte[] original = cipher.doFinal(encrypted);
                return new String(original, CHARSET_NAME);
            } catch (Exception e) {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }
}
