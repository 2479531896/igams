package com.matridx.igams.hrm.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.enums.RedisCommonKeyEnum;
import com.matridx.igams.common.redis.RedisLock;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.JpjgDto;
import com.matridx.igams.hrm.dao.entities.JpjgModel;
import com.matridx.igams.hrm.dao.post.IJpjgDao;
import com.matridx.igams.hrm.service.svcinterface.IJpjgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JpjgServiceImpl extends BaseBasicServiceImpl<JpjgDto, JpjgModel, IJpjgDao> implements IJpjgService {
    @Autowired
    RedisUtil redisUtil;
    @Override
    @RedisLock
    public List<JpjgDto> getLotteryRecords(JpjgDto jpjgDto) {
        Set<Object> lotteryRecordsObj = redisUtil.zRange(RedisCommonKeyEnum.AWARD_RECORE_LIST.getKey()+jpjgDto.getJpglid(),jpjgDto.getPageStart(),jpjgDto.getPageSize()+jpjgDto.getPageStart()-1);
        List<JpjgDto> lotteryRecords = new ArrayList<>();
        if (!CollectionUtils.isEmpty(lotteryRecordsObj)){
            lotteryRecords = lotteryRecordsObj.stream().map(e -> JSON.parseObject(String.valueOf(e), JpjgDto.class)).collect(Collectors.toList());
        }else {
            Set<Object> objects = redisUtil.zRange(RedisCommonKeyEnum.AWARD_RECORE_LIST.getKey() + jpjgDto.getJpglid(), 0, 0);
            if (CollectionUtils.isEmpty(objects)){
                List<JpjgDto> allLotteryRecords = dao.getLotteryRecords(jpjgDto);
                if (!CollectionUtils.isEmpty(allLotteryRecords)){
                    allLotteryRecords.forEach(e->redisUtil.zIncrementScore(RedisCommonKeyEnum.AWARD_RECORE_LIST.getKey()+jpjgDto.getJpglid(),JSON.toJSONString(e),e.getHjsjDate().getTime() ,allLotteryRecords.get(0).getRqc()*24*60*60));
                    lotteryRecordsObj = redisUtil.zRange(RedisCommonKeyEnum.AWARD_RECORE_LIST.getKey()+jpjgDto.getJpglid(),jpjgDto.getPageStart(),jpjgDto.getPageSize()-1);
                    lotteryRecords = lotteryRecordsObj.stream().map(e -> JSON.parseObject(String.valueOf(e), JpjgDto.class)).collect(Collectors.toList());
                }
            }
        }
        return lotteryRecords;
    }

    @Override
    public JpjgDto getPersonalLotteryRecord(JpjgDto jpjgDto) {
        Object personalLotteryRecordObj = redisUtil.hget(RedisCommonKeyEnum.AWARD_RECORES.getKey() + jpjgDto.getJpglid(), jpjgDto.getYhid());
        JpjgDto jpjgDto_res;
        if (personalLotteryRecordObj!=null){
            jpjgDto_res = JSON.parseObject(String.valueOf(personalLotteryRecordObj),JpjgDto.class);
        }else {
            jpjgDto_res = dao.getPersonalLotteryRecord(jpjgDto);
            if (jpjgDto_res!=null){
                redisUtil.hset(RedisCommonKeyEnum.AWARD_RECORES.getKey()+jpjgDto.getJpglid(),jpjgDto.getYhid(),JSON.toJSONString(jpjgDto_res), jpjgDto_res.getRqc()*24*60*60);
            }
        }
        return jpjgDto_res;
    }
}
