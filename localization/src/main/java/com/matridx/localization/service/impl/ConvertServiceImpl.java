package com.matridx.localization.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.localization.service.svcinterface.IConvertService;
import com.matridx.localization.util.ConvertJsonUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.Encrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * ConvertServiceImpl
 *
 * @author hmz
 * @version 1.0
 * @date 2024/7/18 上午9:08
 */
@Service
public class ConvertServiceImpl implements IConvertService {

    private Logger log = LoggerFactory.getLogger(ConvertServiceImpl.class);
    @Autowired
    IXxdyService xxdyService;
    @Autowired
    RestTemplate restTemplate;
    //主服务器地址
    @Value("${matridx.wechat.serverapplicationurl:}")
    private String serverapplicationurl;
    /**
     * 将传递进来的数据转成JSONList,并发送给主服务器
     * @param json
     * @param db
     * @param dwmc
     * @return
     */
    @Override
    public Map<String,Object> convertJsonAndSendToServer(String flag, String db, String dwmc, String json, String filePath, String lineSplit) {
        log.error("接收到的参数：flg:" + flag + " db:" + db + " dwmc:" + dwmc + " json:" + json + " filePath:" + filePath + " lineSplit:" + lineSplit);
        Map<String,Object> map = new HashMap<>();
        XxdyDto xxdyDto = new XxdyDto();
        xxdyDto.setDylx("BDHDY");
        List<String> yxxs = new ArrayList<>();
        yxxs.add(db);
        xxdyDto.setYxxs(yxxs);
        List<JcsjDto> dyjcsjList = xxdyService.getJcsjListByXxdy(xxdyDto);
        JcsjDto setting = null;
        if (!CollectionUtils.isEmpty(dyjcsjList)){
            setting = dyjcsjList.get(0);
            if (dyjcsjList.size() > 1 && StringUtil.isNotBlank(dwmc)){
                if (StringUtil.isNotBlank(dwmc)){
                    setting = dyjcsjList.stream().filter(jcsj -> StringUtil.isNotBlank(jcsj.getCskz2()) && Arrays.stream(jcsj.getCskz2().split(",")).anyMatch(dwmcs -> dwmcs.equals(dwmc))).findFirst().get();
                } else {
                    setting = dyjcsjList.stream().filter(jcsj -> StringUtil.isBlank(jcsj.getCskz2())).findFirst().get();
                }
            }
        }
        if (setting != null){
            List<Map<String, Object>> list = new ArrayList<>();
            String result = null;
            ConvertJsonUtil convertJsonUtil = new ConvertJsonUtil();
            Map<String,Object> settings = JSONObject.parseObject(setting.getCskz1().replaceAll("＃", "#").replaceAll("＆", "&").replaceAll("＞", ">").replaceAll("＜", "<"), Map.class);

            if ("lineFile".equals(flag) && StringUtil.isNotBlank(filePath) && StringUtil.isNotBlank(lineSplit)){
                result = ConvertJsonUtil.readLineFileToString(filePath,lineSplit);
            } else if ("jsonFile".equals(flag) && StringUtil.isNotBlank(filePath)) {
                result = ConvertJsonUtil.readJsonFileToString(filePath);
            } else if ("jsonLineFile".equals(flag) && StringUtil.isNotBlank(filePath)) {
                result = ConvertJsonUtil.readJsonLineFileToString(filePath);
            } else if ("json".equals(flag) && StringUtil.isNotBlank(json)){
                result = json;
            } else {
                map.put("message","保存失败!参数缺失!");
                map.put("status","fail");
                return map;
            }
            list = convertJsonUtil.convertObject(result, settings);
            //log.error("转换后的List长度：" + list.size() + " 第一条数据：" + ((list.size()>0)?JSONObject.toJSONString(list.get(0)):""));
            log.error("转换后的List长度：" + list.size() + " 最后一条数据：" + ((list.size()>0)?JSONObject.toJSONString(list.get(list.size()-1)):""));
            //在服务端删除一些不需要的标本信息
            for(int i = list.size()-1; i >=0; i--){
                //String t_wbbm = (String)list.get(i).get("wbbm");
                String t_hzxm = (String)list.get(i).get("hzxm");

                Pattern pattern = Pattern.compile("^[0-9]+$");
                if (pattern.matcher(t_hzxm).matches()){
                    //log.error("删除标本信息：" + JSONObject.toJSONString(list.get(i)) + " 索引：" + i);
                    list.remove(i);
                }
            }
            String[] split = setting.getCskz3().split(",");
            String jsonString = JSON.toJSONString(list);
            String text = jsonString + split[0] + split[1];
            Encrypt crypts = new Encrypt();
            String t_sign = crypts.encrypt(text, "SHA1");
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            log.error("kzcs3：" + setting.getCskz3() + " orage：" + split[0] + " 服务器地址：" + serverapplicationurl+ "/ws/inspect/receiveInspectInfo");
            log.error("jsonString：" + jsonString + "  sign:", t_sign);
            paramMap.add("organ", split[0]);
            paramMap.add("sign", t_sign);
            paramMap.add("jsonStr", jsonString);
            RestTemplate t_restTemplate = new RestTemplate();
            map = t_restTemplate.postForObject(serverapplicationurl + "/ws/inspect/receiveInspectInfo", paramMap, Map.class);
            log.error("返回结果：" + JSON.toJSONString(map));
            return map;
        } else {
            map.put("message","保存失败!");
            map.put("status","fail");
            return map;
        }
    }
}
