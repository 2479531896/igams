package com.matridx.igams.experiment.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.SysjglDto;
import com.matridx.igams.experiment.dao.entities.SysjglModel;
import com.matridx.igams.experiment.dao.post.ISysjglDao;
import com.matridx.igams.experiment.service.svcinterface.ISysjglService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SysjglServiceImpl extends BaseBasicServiceImpl<SysjglDto, SysjglModel, ISysjglDao> implements ISysjglService{


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertSysjglList(String jsonStr,String type) {
        if(StringUtil.isNotBlank(jsonStr)){
            List<SysjglDto> sysjglDtos = JSON.parseArray(jsonStr,SysjglDto.class);
            if(!CollectionUtils.isEmpty(sysjglDtos)){
                for (SysjglDto sysjglDto : sysjglDtos) {
                    sysjglDto.setSysjid(StringUtil.generateUUID());
                    sysjglDto.setType(type);
                }
                return insertSysjglDtos(sysjglDtos);
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertSysjglDtos(List<SysjglDto> sysjglDtos) {
        return dao.insertSysjglDtos(sysjglDtos);
    }

    @Override
    public Map<String, Object> getSysjxxxMap(SysjglDto sysjglDto) {
        Map<String, Object> map = new HashMap<>();
        List<SysjglDto> sysjglDtos = dao.getSysjxxxMap(sysjglDto);
        List<Map<String,Object>> listGroupMap = new ArrayList<>();
        sysjglDtos.stream()
                .collect(Collectors.groupingBy(dto ->
                        dto.getSjrq() + "_" + dto.getNbbh() + "_" + dto.getType()
                ))
                .forEach((key, value) -> {
                    Map<String, Object> groupMap = new HashMap<>();
                    groupMap.put("sjrq", value.get(0).getSjrq());
                    groupMap.put("nbbh", value.get(0).getNbbh());
                    groupMap.put("type", value.get(0).getType());
                    groupMap.put("sjList", value);
                    listGroupMap.add(groupMap);
                });
        Map<String, Object> groupByRqMap = new HashMap<>();
        sysjglDtos.stream()
                .collect(Collectors.groupingBy(SysjglModel::getSjrq))
                .forEach((key, value) -> {
                    groupByRqMap.put(value.get(0).getSjrq(), value);
                });
        List<Map<String,Object>> listMap = new ArrayList<>();
        groupByRqMap.forEach((key,value) -> {
            Map<String, Object> mapRq = new HashMap<>();
            Map<String,Object> mapGroupRq = new HashMap<>();
            mapRq.put("sjrq",key);
            List<Map<String,Object>> listGroupRq = new ArrayList<>();
            for (Map<String, Object> mapT : listGroupMap) {
                if(mapT.get("sjrq").equals(key)){
                    listGroupRq.add(mapT);
                }
            }
            listGroupRq.sort((map1, map2) -> {
                int typeCompare = ((String) map1.get("type")).compareTo((String) map2.get("type"));
                if (typeCompare != 0) {
                    return typeCompare;
                }
                return ((String) map1.get("nbbh")).compareTo((String) map2.get("nbbh"));
            });
            mapRq.put("bhList",listGroupRq);
            listMap.add(mapRq);
        });
        map.put("sysj",listMap);
        return map;
    }

    /**
     * @Description: 根据ids删除
     * @param sysjglDto
     * @return boolean
     */
    public boolean deleteByIds(SysjglDto sysjglDto){
        return dao.deleteByIds(sysjglDto);
    }
}
