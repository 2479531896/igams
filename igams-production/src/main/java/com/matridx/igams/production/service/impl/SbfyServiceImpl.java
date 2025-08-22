package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.production.dao.entities.SbfyDto;
import com.matridx.igams.production.dao.entities.SbfyModel;
import com.matridx.igams.production.dao.post.ISbfyDao;
import com.matridx.igams.production.service.svcinterface.ISbfyService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : 郭祥杰
 * @date :
 */
@Service
public class SbfyServiceImpl extends BaseBasicServiceImpl<SbfyDto, SbfyModel, ISbfyDao> implements ISbfyService {
    @Autowired
    IJcsjService jcsjService;

    /**
     * @Description: 设备费用查询
     * @param sbfyDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/11/5 17:22
     */
    @Override
    public Map<String,Object> querySbfyList(SbfyDto sbfyDto) {
        Map<String,Object> map = new HashMap<>();
        Map<String,Object> sbfyDtoMap= new HashMap<>();
        JcsjDto jcsjDto = new JcsjDto();
        jcsjDto.setJclb(BasicDataTypeEnum.DEVICE_TYPE.getCode());
        List<JcsjDto> jcsjDtos = jcsjService.getJcsjListByIdsAndJclb(jcsjDto);
        BigDecimal countFy = new BigDecimal("0");
        if(jcsjDtos!=null && !jcsjDtos.isEmpty()){
            List<SbfyDto> sbfyDtoList = dao.getDtoList(sbfyDto);
            if(sbfyDtoList!=null && !sbfyDtoList.isEmpty()){
                for (JcsjDto jcsjDtoT:jcsjDtos){
                    List<SbfyDto> sbfyDtosT = new ArrayList<>();
                    BigDecimal zfy = new BigDecimal("0");
                    for(SbfyDto sbfyDtoT:sbfyDtoList){
                        if(sbfyDtoT.getFylx().equals(jcsjDtoT.getCsid())){
                            sbfyDtosT.add(sbfyDtoT);
                            zfy = zfy.add(StringUtil.isNotBlank(sbfyDtoT.getJe())?new BigDecimal(sbfyDtoT.getJe()):new BigDecimal("0"));
                        }
                    }
                    countFy = countFy.add(zfy);
                    List<SbfyDto> orderList = sbfyDtosT.stream().sorted(Comparator.comparing(SbfyDto::getSj).reversed()).collect(Collectors.toList());
                    sbfyDtoMap.put(jcsjDtoT.getCsmc(),orderList);
                    sbfyDtoMap.put(jcsjDtoT.getCsid(),zfy.toString());
                }
            }
        }
        map.put("jcsjList",jcsjDtos);
        map.put("sbfyDtoMap",sbfyDtoMap);
        map.put("zfy",countFy.toString());
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertSbfyDto(SbfyDto sbfyDto) {
        if (StringUtil.isBlank(sbfyDto.getSbfyid())){
            sbfyDto.setSbfyid(StringUtil.generateUUID());
        }
        return dao.insert(sbfyDto)>0;
    }
}
