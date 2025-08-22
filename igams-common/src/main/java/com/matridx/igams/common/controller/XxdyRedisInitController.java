package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * XxdyRedisInitController
 *
 */
@Component
@RequestMapping("/initXxdy")
public class XxdyRedisInitController implements ApplicationRunner {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IXxdyService xxdyService;

    private final Logger log = LoggerFactory.getLogger(XxdyRedisInitController.class);

    @RequestMapping("/redis")
    @ResponseBody
    public void xxdyRedisInit() {
        log.error("---------------信息对应-样本类型-存储redis执行开始---------------");
        XxdyDto dto = new XxdyDto();
        dto.setDylxcsdm("YBLX");
        List<XxdyDto> xxdyDtos = xxdyService.getDtoList(dto);
        if (!CollectionUtils.isEmpty(xxdyDtos)){
            for (XxdyDto xxdyDto : xxdyDtos) {
                redisUtil.hset("XXDY:"+xxdyDto.getDylxcsdm(),xxdyDto.getYxx(), JSON.toJSONString(xxdyDto), -1);
            }
        }
        log.error("---------------信息对应-样本类型-存储redis执行结束---------------");
    }
    @Override
    public void run(ApplicationArguments args) {
        xxdyRedisInit();
    }
}
