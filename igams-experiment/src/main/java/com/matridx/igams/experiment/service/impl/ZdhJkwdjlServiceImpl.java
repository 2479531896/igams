package com.matridx.igams.experiment.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.ZdhJkcsDto;
import com.matridx.igams.experiment.dao.entities.ZdhJkwdjlDto;
import com.matridx.igams.experiment.dao.entities.ZdhJkwdjlModel;
import com.matridx.igams.experiment.dao.post.IZdhJkwdjlDao;
import com.matridx.igams.experiment.service.svcinterface.IZdhJkcsService;
import com.matridx.igams.experiment.service.svcinterface.IZdhJkwdjlService;
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
public class ZdhJkwdjlServiceImpl extends BaseBasicServiceImpl<ZdhJkwdjlDto, ZdhJkwdjlModel, IZdhJkwdjlDao> implements IZdhJkwdjlService {
    private final Logger log = LoggerFactory.getLogger(ZdhJkwdjlServiceImpl.class);
    @Autowired
    IZdhJkcsService zdhJkcsService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean temperatureReporting(String jsonString) throws BusinessException {
        List<ZdhJkwdjlDto> temperatureList =  JSON.parseArray(jsonString, ZdhJkwdjlDto.class);
        if(temperatureList==null || temperatureList.isEmpty()){
            log.error("temperatureList为空!");
            return false;
        }
        List<ZdhJkcsDto> jkcsDtoList = new ArrayList<>();
        for (ZdhJkwdjlDto zdhjkwdjlDto:temperatureList) {
            zdhjkwdjlDto.setWdjlid(StringUtil.generateUUID());
            ZdhJkcsDto zdhJkcsDto = new ZdhJkcsDto();
            zdhJkcsDto.setJkcsid(StringUtil.generateUUID());
            zdhJkcsDto.setBbbm(zdhjkwdjlDto.getBbbm());
            zdhJkcsDto.setYbjbh(zdhjkwdjlDto.getYbjbh());
            zdhJkcsDto.setCs(jsonString);
            jkcsDtoList.add(zdhJkcsDto);
        }
        boolean result = zdhJkcsService.insertJkcsDtos(jkcsDtoList);
        if(!result){
            log.error("接口参数新增失败!");
            throw new BusinessException("msg","接口参数新增失败!");
        }
        result = dao.insertJkwdjlList(temperatureList);
        if(!result){
            log.error("建库温度记录新增失败!");
            throw new BusinessException("msg","建库温度记录新增失败!");
        }
        return true;
    }
}
