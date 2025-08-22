package com.matridx.igams.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author hmz
 * @date2022/03/18 version V1.0
 **/
public class OcrRecognitionUtil {

    private final Logger log = LoggerFactory.getLogger(OcrRecognitionUtil.class);

    /**
     * 对图像进行base64编码
     * @param path
     * @return
     */
    public static String img_base64(String path) {
        /**
         *  对path进行判断，如果是本地文件就二进制读取并base64编码，如果是url,则返回
         */
        String imgBase64="";
        if (path.startsWith("http")){
            imgBase64 = path;
        }else {
            try {
                File file = new File(path);
                byte[] content = new byte[(int) file.length()];
                FileInputStream finputstream = new FileInputStream(file);
                finputstream.read(content);
                finputstream.close();
                imgBase64 = new String(Base64.encodeBase64(content));
            } catch (IOException e) {
                e.printStackTrace();
                return imgBase64;
            }
        }

        return imgBase64;
    }


    /**
     * 调用阿里识别身份证方法
     * @param appcode appcode
     * @param imgFile 本地图片路径或者图片的url
     * @return
     */
    public Map<String,Object> OcrRecognitionByAli(String appcode,String imgFile){
        Map<String,Object> map = new HashMap<>();
        String host = "http://dm-51.data.aliyun.com";
        String path = "/rest/160601/ocr/ocr_idcard.json";
        String method = "POST";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        // 对图像进行base64编码
        String imgBase64 = img_base64(imgFile);
        //configure配置
        JSONObject configObj = new JSONObject();
        configObj.put("side", "face");
        String config_str = configObj.toString();
        // 拼装请求body的json字符串
        JSONObject requestObj = new JSONObject();
        requestObj.put("image", imgBase64);
        if(!configObj.isEmpty()) {
            requestObj.put("configure", config_str);
        }
        String bodys = requestObj.toString();
        try {
            HttpResponse response = AliHttpUtils.doPost(host, path, method, headers, querys, bodys);
            int stat = response.getStatusLine().getStatusCode();
            if(stat != 200){
                log.error("code" + stat);
                map.put("status","fail");
                map.put("message","识别失败！");
                return map;
            }
            String res = EntityUtils.toString(response.getEntity());
            JSONObject res_obj = JSON.parseObject(res);
            log.error("阿里身份识别信息："+res_obj.toJSONString());
            map.put("status","success");
            map.put("message","识别成功！");
            Map<String,String> bodyMap = new HashMap<>();
            bodyMap.put("name",res_obj.getString("name"));
            bodyMap.put("sex",res_obj.getString("sex"));
            bodyMap.put("nationality",res_obj.getString("nationality"));
            bodyMap.put("address",res_obj.getString("address"));
            bodyMap.put("birth",res_obj.getString("birth"));
            bodyMap.put("num",res_obj.getString("num"));
            map.put("body",bodyMap);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("status","fail");
        map.put("message","网络异常！");
        return map;
    }


    /**
     * 调用百度识别身份证方法
     * @param token token
     * @param imgFile 本地图片路径或者图片的url
     * @return
     */
    public Map<String,Object> OcrRecognitionByBaidu(String token,String imgFile){
        Map<String,Object> map = new HashMap<>();
        // iocr识别apiUrl
        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";
        try {
            // 本地文件路径
            String filePath = imgFile;
            String imgStr = img_base64(imgFile);
            String imgParam = URLEncoder.encode(imgStr, StandardCharsets.UTF_8);

            String param = "id_card_side=" + "front" + "&image=" + imgParam;

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String accessToken = token;

            String result = HttpUtil.post(url, accessToken, param);
            log.error("百度身份识别信息："+result);
            JSONObject jsonObject = JSONObject.parseObject(result);
            //处理返回信息
            if ("normal".equals(jsonObject.getString("image_status"))){
                JSONObject words_result = jsonObject.getJSONObject("words_result");
                Map<String,String> bodyMap = new HashMap<>();
                bodyMap.put("name",JSON.parseObject(words_result.getString("姓名")).getString("words"));
                bodyMap.put("sex",JSON.parseObject(words_result.getString("性别")).getString("words"));
                bodyMap.put("nationality",JSON.parseObject(words_result.getString("民族")).getString("words"));
                bodyMap.put("address",JSON.parseObject(words_result.getString("住址")).getString("words"));
                bodyMap.put("birth",JSON.parseObject(words_result.getString("出生")).getString("words"));
                bodyMap.put("num",JSON.parseObject(words_result.getString("公民身份号码")).getString("words"));
                map.put("status","success");
                map.put("message","识别成功！");
                map.put("body",bodyMap);
            }else if("reversed_side".equals(jsonObject.getString("image_status"))){
                map.put("status","fail");
                map.put("message","身份证正反面颠倒！");
            }else if("non_idcard".equals(jsonObject.getString("image_status"))){
                map.put("status","fail");
                map.put("message","上传的图片中不包含身份证！");
            }else if("blurred".equals(jsonObject.getString("image_status"))){
                map.put("status","fail");
                map.put("message","身份证模糊！");
            }else if("other_type_card".equals(jsonObject.getString("image_status"))){
                map.put("status","fail");
                map.put("message","其他类型证照！");
            }else if("over_exposure".equals(jsonObject.getString("image_status"))){
                map.put("status","fail");
                map.put("message","身份证关键字段反光或过曝！");
            }else if("over_dark".equals(jsonObject.getString("image_status"))){
                map.put("status","fail");
                map.put("message","身份证欠曝（亮度过低）！");
            }else if("unknown".equals(jsonObject.getString("image_status"))){
                map.put("status","fail");
                map.put("message","未知状态！");
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("status","fail");
        map.put("message","网络异常！");
        return map;
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Secret Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public Map<String,String> getAuth(String ak, String sk) {
        Map<String,String> resultmap = new HashMap<>();
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();

            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            /**
             * 返回结果示例
             */
            JSONObject jsonObject = JSONObject.parseObject(result.toString());
            String access_token = jsonObject.getString("access_token");
            String expires_in = jsonObject.getString("expires_in");
            resultmap.put("status","success");
            resultmap.put("access_token",access_token);
            resultmap.put("expires_in",expires_in);
            return resultmap;
        } catch (Exception e) {
            resultmap.put("message",e.toString());
            log.error(e.toString());
            e.printStackTrace(System.err);
        }
        resultmap.put("status","fail");
        return resultmap;
    }
}
