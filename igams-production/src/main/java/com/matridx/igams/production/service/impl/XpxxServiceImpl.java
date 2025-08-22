package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.XpxxDto;
import com.matridx.igams.production.dao.entities.XpxxModel;
import com.matridx.igams.production.dao.post.IXpxxDao;
import com.matridx.igams.production.service.svcinterface.IXpxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class XpxxServiceImpl extends BaseBasicServiceImpl<XpxxDto, XpxxModel, IXpxxDao> implements IXpxxService{

    //日志
    private Logger log = LoggerFactory.getLogger(XpxxServiceImpl.class);

    @Autowired
    RedisUtil redisUtil;    /**
     * 新增芯片信息
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveChipinfo(XpxxDto xpxxDto) {
        xpxxDto.setXpid(StringUtil.generateUUID());
        int insert = dao.insert(xpxxDto);
        return insert != 0;
    }

    /**
     * 修改芯片信息
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveChipinfo(XpxxDto xpxxDto){
        int update = dao.update(xpxxDto);
        return update != 0;
    }

    /**

     * 定时任务获取blast token
     */
    public String getSxBCLToken() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders requestHeaders = new HttpHeaders();
            Map<String, Object> requestEntrustModel = new HashMap<>();
            requestEntrustModel.put("username", "medlab");
            requestEntrustModel.put("password", "Matridx123!");
            requestEntrustModel.put("party", "IT");
            HttpEntity requestEntity = new HttpEntity(requestEntrustModel, requestHeaders);
            Map<String, Object> result = restTemplate.postForObject("http://bcl.matridx.top/BCL/api/token/", requestEntity, Map.class);
            String token = result.get("token").toString();
            redisUtil.set("BLC_TOKEN", token, 60 * 60 * 2);
            log.error("getSxBCLToken 正常:{}",token);
            return token;
        } catch (Exception e) {
            log.error("getSxBCLToken 异常:{}",e.getMessage());
        }
        return null;
    }
    @Override
    public void getChipQcScoreAndSave(Map<String,Object> map){
        // 1、 获取所有sfgx不为1的近三天的数据
        XpxxDto xpxxDto = new XpxxDto();
        String timeOffset =  "-3 days";
        if (map.get("timeOffset") != null){
            timeOffset = map.get("timeOffset").toString();
        }
        xpxxDto.setTimeOffset(timeOffset);
        List<XpxxDto> xpxxList = dao.getUnUpdateChips(xpxxDto);
        if (!CollectionUtils.isEmpty(xpxxList)){
            String url = "http://bcl.matridx.top/BCL/api/chip_qc_score/?seq_name=<seq_name>";
            // 2、获取测序管理系统token

            Object blc_token = redisUtil.get("BLC_TOKEN");
            if (blc_token == null){
                blc_token = getSxBCLToken();
            }
            RestTemplate restTemplate=new RestTemplate();
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            requestHeaders.set("Authorization", "Token " + blc_token);
            HttpEntity httpEntity = new HttpEntity(requestHeaders);
            List<XpxxDto> updateList = new ArrayList<>();
            for (XpxxDto xpxx : xpxxList) {
                //获取生信部芯片列表
                try {
                    // 延迟100毫秒
                    Thread.sleep(500);
                    String xmUrl = url.replace("<seq_name>",xpxx.getXpm());
                    // 发送get请求
                    ResponseEntity<String> response = restTemplate.exchange(xmUrl, HttpMethod.GET, httpEntity, String.class);
                    log.error("getChipQcScoreAndSave获取成功:{},明细:{}",xpxx.getXpm(), JSON.toJSONString(response));
                    if (200 == response.getStatusCodeValue()){
                        XpxxDto updateDto = new XpxxDto();
                        updateDto.setSfgx("1");
                        updateDto.setQcjson(response.getBody());
                        updateDto.setXpid(xpxx.getXpid());
                        updateList.add(updateDto);
                    }
                } catch (Exception e) {
                    log.error("getChipQcScoreAndSave获取失败:{},明细:{},token值：{}",xpxx.getXpm(),e.getMessage(),blc_token);
                }
            }
            if (!CollectionUtils.isEmpty(updateList)){
                dao.updateChipQcScore(updateList);
            }
        }
    }
}
