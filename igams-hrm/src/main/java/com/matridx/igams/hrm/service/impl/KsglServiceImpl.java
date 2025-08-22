package com.matridx.igams.hrm.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.matridx.igams.common.enums.RedisCommonKeyEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;

import com.matridx.igams.hrm.dao.entities.*;
import com.matridx.igams.hrm.dao.post.IKsglDao;
import com.matridx.igams.hrm.service.svcinterface.IKsglService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class KsglServiceImpl extends BaseBasicServiceImpl<KsglDto, KsglModel, IKsglDao> implements IKsglService {

    private final Logger log = LoggerFactory.getLogger(KsglServiceImpl.class);

    @Autowired
    RedisUtil redisUtil;

    /**
     * 通过tkid获取考试明细
     */
    public List<KsglDto> getListQuestions(KsglDto ksglDto){
        return dao.getListQuestions(ksglDto);
    }
    /**
     * 通过tkid删除题目
     */
    public void deleteByTkid(KsglDto ksglDto){
        dao.deleteByTkid(ksglDto);
    }
    /**
     * 获取题目数量
     */
    public int getCount(KsglDto ksglDto){
        return dao.getCount(ksglDto);
    }
    /**
     * 批量新增
     */
    public boolean insertList(List<KsglDto> ksglDtos){
        return dao.insertList(ksglDtos);
    }

    /**
     * 随机组题
     */
    public List<KsglDto> groupQuestions(int sl, Map<Object, Object> tmlist, List<KsglDto> list_t , GrksglDto grksglDto){
        if(sl>=tmlist.size()){
            for(Object obj : tmlist.values()) {
                KsglDto ksglDto = JSON.parseObject(obj.toString(),KsglDto.class);
                String mxhget = String.valueOf(redisUtil.get(RedisCommonKeyEnum.TRAIN_KSMX.getKey() + ksglDto.getKsid()));
                List<KsmxDto> xxlist = JSONArray.parseArray(mxhget, KsmxDto.class);
                ksglDto.setXxlist(xxlist);
                list_t.add(ksglDto);

                GrksmxDto grksmxDto=new GrksmxDto();
                grksmxDto.setGrksmxid(StringUtil.generateUUID());
                grksmxDto.setGrksid(grksglDto.getGrksid());
                grksmxDto.setKsid(ksglDto.getKsid());
                grksmxDto.setDa(ksglDto.getDa());
                grksmxDto.setTmlx(ksglDto.getTmlxmc());
                redisUtil.lSet(RedisCommonKeyEnum.TRAIN_GRKSMX.getKey()+grksglDto.getGzid(),JSON.toJSONString(grksmxDto),RedisCommonKeyEnum.TRAIN_GRKSMX.getExpire());
                log.error("---------------------插入Redis的Grksmx数据为："+JSON.toJSONString(grksmxDto));
            }
        }else{
            int count=0;
            List<String> nums=new ArrayList<>();
            while(count!=sl){
                Random r = new Random();
                int num = r.nextInt((tmlist.size()-1));
                if(!nums.contains(String.valueOf(num))){
                    Object hget = tmlist.get(String.valueOf(num));//redisUtil.hget(RedisCommonKeyEnum.TRAIN_KSGL.getKey() + tkid, String.valueOf(num));
                    KsglDto ksglDto = JSON.parseObject(String.valueOf(hget),KsglDto.class);
                    String mxhget = String.valueOf(redisUtil.get(RedisCommonKeyEnum.TRAIN_KSMX.getKey() + ksglDto.getKsid()));
                    List<KsmxDto> xxlist = JSONArray.parseArray(mxhget, KsmxDto.class);
                    ksglDto.setXxlist(xxlist);
                    list_t.add(ksglDto);

                    GrksmxDto grksmxDto=new GrksmxDto();
                    grksmxDto.setGrksmxid(StringUtil.generateUUID());
                    grksmxDto.setGrksid(grksglDto.getGrksid());
                    grksmxDto.setKsid(ksglDto.getKsid());
                    grksmxDto.setDa(ksglDto.getDa());
                    grksmxDto.setTmlx(ksglDto.getTmlxmc());
                    redisUtil.lSet(RedisCommonKeyEnum.TRAIN_GRKSMX.getKey()+grksglDto.getGzid(),JSON.toJSONString(grksmxDto),RedisCommonKeyEnum.TRAIN_GRKSMX.getExpire());
                    log.error("---------------------插入Redis的Grksmx数据为："+JSON.toJSONString(grksmxDto));

                    nums.add(String.valueOf(num));
                    count++;
                }
            }
        }
        return list_t;
    }

    @Override
    public List<KsglDto> getQuestions(KsglDto ksglDto) {
        return dao.getQuestions(ksglDto);
    }
}
