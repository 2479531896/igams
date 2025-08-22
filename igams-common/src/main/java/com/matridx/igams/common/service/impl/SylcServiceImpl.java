package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.SylcDto;
import com.matridx.igams.common.dao.entities.SylcModel;
import com.matridx.igams.common.dao.entities.SylcmxDto;
import com.matridx.igams.common.dao.post.ISylcDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ISylcService;
import com.matridx.igams.common.service.svcinterface.ISylcmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class SylcServiceImpl extends BaseBasicServiceImpl<SylcDto, SylcModel, ISylcDao> implements ISylcService {

    @Autowired
    private ISylcmxService sylcmxService;

    @Override
    public List<Map<String,Object>> getMenuList(Map<String, Object> map) {
        return dao.getMenuList(map);
    }

    @Override
    public List<Map<String, Object>> getButtonList(Map<String, Object> map) {
        return dao.getButtonList(map);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveHomepageProcess(SylcDto sylcDto) {
        sylcDto.setLcid(StringUtil.generateUUID());
        int inserted1 = dao.insert(sylcDto);
        if(inserted1==0){
            return false;
        }
        List<SylcmxDto> sylcmxDtos = JSON.parseArray(sylcDto.getLcmx_json(), SylcmxDto.class);
        if(sylcmxDtos!=null&&!sylcmxDtos.isEmpty()){
            for(SylcmxDto dto:sylcmxDtos){
                dto.setLcmxid(StringUtil.generateUUID());
                dto.setLcid(sylcDto.getLcid());
                dto.setLrry(sylcDto.getLrry());
            }
        }
        boolean inserted = sylcmxService.insertList(sylcmxDtos);
        if(!inserted){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveHomepageProcess(SylcDto sylcDto) {
        int updated = dao.update(sylcDto);
        if(updated==0){
            return false;
        }
        SylcmxDto sylcmxDto=new SylcmxDto();
        sylcmxDto.setIds(sylcDto.getLcid());
        sylcmxDto.setScry(sylcDto.getXgry());
        sylcmxService.delObsoleteData(sylcmxDto);
        sylcmxService.delete(sylcmxDto);
        List<SylcmxDto> sylcmxDtos = JSON.parseArray(sylcDto.getLcmx_json(), SylcmxDto.class);
        if(sylcmxDtos!=null&&!sylcmxDtos.isEmpty()){
            for(SylcmxDto dto:sylcmxDtos){
                dto.setLcmxid(StringUtil.generateUUID());
                dto.setLcid(sylcDto.getLcid());
                dto.setLrry(sylcDto.getXgry());
            }
        }
        boolean inserted = sylcmxService.insertList(sylcmxDtos);
        if(!inserted){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delHomepageProcess(SylcDto sylcDto) {
        int deleted = dao.delete(sylcDto);
        if(deleted==0){
            return false;
        }
        SylcmxDto sylcmxDto=new SylcmxDto();
        sylcmxDto.setIds(sylcDto.getIds());
        sylcmxDto.setScry(sylcDto.getScry());
        boolean delete = sylcmxService.delete(sylcmxDto);
        if(!delete){
            return false;
        }
        return true;
    }

    @Override
    public List<SylcDto> getAllData() {
        return dao.getAllData();
    }
}
