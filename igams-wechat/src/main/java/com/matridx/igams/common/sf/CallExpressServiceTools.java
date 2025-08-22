package com.matridx.igams.common.sf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CallExpressServiceTools {
    private static final Logger logger = LoggerFactory.getLogger(CallExpressServiceTools.class);
    private static CallExpressServiceTools tools = new CallExpressServiceTools();

    private CallExpressServiceTools() {
    }

    public static CallExpressServiceTools getInstance() {
        synchronized(CallExpressServiceTools.class) {
            if (tools == null) {
                tools = new CallExpressServiceTools();
            }
        }

        return tools;
    }

    public static String callSfExpressServiceByCSIM(String reqURL, String reqXML, String clientCode, String checkword) {
        String result;
        String verifyCode = VerifyCodeUtil.md5EncryptAndBase64(reqXML + checkword);
        result = querySFAPIservice(reqURL, reqXML, verifyCode);
        return result;
    }

    public static String querySFAPIservice(String url, String xml, String verifyCode) {
        HttpClientUtil httpclient = new HttpClientUtil();
        if (url == null) {
            url = "http://bsp-oisp.sf-express.com/bsp-oisp/sfexpressService";
        }

        String result;

        try {
            result = httpclient.postSFAPI(url, xml, verifyCode);
            return result;
        } catch (Exception var6) {
            logger.warn(" " + var6);
            return null;
        }
    }

    public static String packageMsgData(EspServiceCode espServiceCode) {
        String jsonString = "";

        try {
            InputStream is = new FileInputStream(espServiceCode.getPath());
            byte[] bs = new byte[is.available()];
            is.read(bs);
            jsonString = new String(bs);
        } catch (Exception var4) {
            logger.error(var4.toString());
        }

        return jsonString;
    }

    public static String getMsgDigest(String msgData, String timeStamp, String md5Key) throws UnsupportedEncodingException {
        return VerifyCodeUtil.md5EncryptAndBase64(URLEncoder.encode(msgData + timeStamp + md5Key, StandardCharsets.UTF_8));
    }
}
