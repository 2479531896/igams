package com.matridx.igams.experiment.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.ZdhJkcsDto;
import com.matridx.igams.experiment.dao.entities.ZdhYqycxxDto;
import com.matridx.igams.experiment.dao.entities.ZdhYqycxxModel;
import com.matridx.igams.experiment.dao.post.IZdhYqycxxDao;
import com.matridx.igams.experiment.service.svcinterface.IZdhJkcsService;
import com.matridx.igams.experiment.service.svcinterface.IZdhYqycxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Service
public class ZdhYqycxxServiceImpl extends BaseBasicServiceImpl<ZdhYqycxxDto, ZdhYqycxxModel, IZdhYqycxxDao> implements IZdhYqycxxService {
    private final Logger log = LoggerFactory.getLogger(ZdhYqycxxServiceImpl.class);
    @Autowired
    IZdhJkcsService zdhJkcsService;
    /**
     * @Description: 仪器异常上报
     * @param jsonString
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/19 11:34
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean exceptionReporting(String jsonString) throws BusinessException {
        List<ZdhYqycxxDto> exceptionList =  JSON.parseArray(jsonString, ZdhYqycxxDto.class);
        if(exceptionList==null || exceptionList.isEmpty()){
            log.error("exceptionList为空!");
            return false;
        }
        List<ZdhJkcsDto> zdhJkcsDtoList = new ArrayList<>();
        for (ZdhYqycxxDto zdhYqycxxDto:exceptionList){
            zdhYqycxxDto.setYqycid(StringUtil.generateUUID());
            ZdhJkcsDto zdhJkcsDto = new ZdhJkcsDto();
            zdhJkcsDto.setJkcsid(StringUtil.generateUUID());
            zdhJkcsDto.setYqbm(zdhYqycxxDto.getYqbm());
            zdhJkcsDto.setCs(jsonString);
            zdhJkcsDtoList.add(zdhJkcsDto);
        }
        boolean result = zdhJkcsService.insertJkcsDtos(zdhJkcsDtoList);
        if(!result){
            log.error("新增接口参数失败!");
            throw new BusinessException("msg","新增接口参数失败!");
        }
        result = dao.insertYqycDtoLsit(exceptionList);
        if(!result){
            log.error("新增仪器异常失败!");
            throw new BusinessException("msg","新增仪器异常失败!");
        }
        return true;
    }
}
