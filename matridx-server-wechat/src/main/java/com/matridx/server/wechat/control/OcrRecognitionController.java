package com.matridx.server.wechat.control;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.util.OcrRecognitionUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hmz
 * @date2022/03/18 version V1.0
 **/
@Controller
@RequestMapping("/wechat")
public class OcrRecognitionController {

    @Autowired
    RedisUtil redisUtil;
    @Value("${matridx.aliOcr.ocrIdCardAppCode:}")
    private String ocrIdCardAppCode;
    @Value("${matridx.baidu.appkey:}")
    private String ocrAkeyBaidu;
    @Value("${matridx.baidu.secret:}")
    private String ocrSkeyBaidu;

    //private Logger log = LoggerFactory.getLogger(OcrRecognitionController.class);

    /**
     * 识别身份证接口
     * @return
     */
    @RequestMapping(value="/ocrRecognitionIdCard")
    @ResponseBody
    public Map<String, Object> ocrRecognitionIdCard(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        OcrRecognitionUtil ocrRecognitionUtil = new OcrRecognitionUtil();
        String imgFile = request.getParameter("imgFile");
        String dyjk = "baidu";
        Object xtsz_o = redisUtil.hget("matridx_xtsz", "IdCardOcr");
        if (xtsz_o!=null){
            String xtsz_s = xtsz_o.toString();
            if (StringUtil.isNotBlank(xtsz_s)){
                XtszDto xtszDto = JSON.parseObject(xtsz_s, XtszDto.class);
                dyjk = xtszDto.getSzz();
            }
        }
        if ("ali".equals(dyjk)){
            map = ocrRecognitionUtil.OcrRecognitionByAli(ocrIdCardAppCode,imgFile);
        }else {
            DBEncrypt dbEncrypt = new DBEncrypt();
            Map<String, String>  authmap = ocrRecognitionUtil.getAuth(dbEncrypt.dCode(ocrAkeyBaidu), dbEncrypt.dCode(ocrSkeyBaidu));
            if ("success".equals(authmap.get("status"))){
                String auth = authmap.get("access_token");
                //String expires = authmap.get("expires_in");
                map = ocrRecognitionUtil.OcrRecognitionByBaidu(auth,imgFile);
            }else {
                map.put("message",authmap.get("message"));
                map.put("status","fail");
            }
        }
        return map;
    }
}
