package com.matridx.igams.storehouse.service.impl;


import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.dao.post.ICpjgDao;
import com.matridx.igams.storehouse.service.svcinterface.ICpjgService;
import com.matridx.igams.storehouse.service.svcinterface.ICpjgmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

@Service
public class CpjgServiceImpl extends BaseBasicServiceImpl<CpjgDto, CpjgModel, ICpjgDao> implements ICpjgService {
    @Autowired
    private ICpjgmxService cpjgmxService;
    @Autowired
    IRdRecordService rdRecordService;

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean saveAddProductInfo(CpjgDto cpjgDto) throws BusinessException {
        cpjgDto.setMjshl("0");
        cpjgDto.setBbdh("10");
        Boolean success = dao.insert(cpjgDto) != 0;
        if (!success)
            throw new BusinessException("msg","保存主表信息失败！");
        List<CpjgmxDto> cpjgmxDtos= JSON.parseArray(cpjgDto.getCpjgmx_json(),CpjgmxDto.class);
        int zjhh = 10;
        for (CpjgmxDto cpjgmxDto : cpjgmxDtos) {
            cpjgmxDto.setCpjgmxid(StringUtil.generateUUID());
            cpjgmxDto.setCpjgid(cpjgDto.getCpjgid());
            cpjgmxDto.setLrry(cpjgDto.getLrry());
            cpjgmxDto.setZjhh(String.valueOf(zjhh));
            zjhh = zjhh +10;
            cpjgmxDto.setGxhh("0000");
            cpjgmxDto.setZjshl("0");
            if (StringUtil.isNotBlank(cpjgmxDto.getJbyl())&&StringUtil.isNotBlank(cpjgmxDto.getJcsl())){
                BigDecimal jbyl = new BigDecimal(cpjgmxDto.getJbyl());
                BigDecimal jcsl = new BigDecimal(cpjgmxDto.getJcsl());
                String sysy = jbyl.divide(jcsl,2, RoundingMode.HALF_UP).toString();
                cpjgmxDto.setSysl(sysy);
            }
        }
        success = cpjgmxService.batchInsertDtoList(cpjgmxDtos);
        if (!success){
            throw new BusinessException("msg","添加明细数据出错！");
        }
        CpjgDto dtoById = dao.getDtoById(cpjgDto.getCpjgid());
        Map<String, Object> map = rdRecordService.addU8DataCpjg(dtoById, cpjgmxDtos);
        CpjgDto cpjgDto1 = (CpjgDto) map.get("cpjgDto");
        success = dao.update(cpjgDto1)!=0;
        if (!success)
            throw new BusinessException("msg","更新U8关联字段失败！");
        @SuppressWarnings("unchecked")
        List<CpjgmxDto> cpjgmxDtoList = (List<CpjgmxDto>) map.get("cpjgmxDtoList");
        success = cpjgmxService.updateList(cpjgmxDtoList);
        if (!success)
            throw new BusinessException("msg","更新U8明细关联字段失败！");
        return true;
    }


    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean saveModProductInfo(CpjgDto cpjgDto) throws BusinessException {
        //先删除U8
        CpjgDto byId = dao.getDtoById(cpjgDto.getCpjgid());
        CpjgmxDto sc_cpjgmx = new CpjgmxDto();
        sc_cpjgmx.setCpjgid(cpjgDto.getCpjgid());
        List<CpjgmxDto> dtoList = cpjgmxService.getDtoList(sc_cpjgmx);
        rdRecordService.delU8DataCpjg(byId, dtoList);
        CpjgDto sc_cpjg = new CpjgDto();
        sc_cpjg.setCpjgid(cpjgDto.getCpjgid());
        sc_cpjg.setScbj("1");
        sc_cpjg.setScry(cpjgDto.getLrry());
        Boolean success =  dao.update(sc_cpjg)!=0;
        if (!success)
            throw new BusinessException("msg","更新主表信息失败！");

        sc_cpjgmx.setScbj("1");
        sc_cpjgmx.setScry(cpjgDto.getLrry());
        success = cpjgmxService.update(sc_cpjgmx);
        if (!success)
            throw new BusinessException("msg","更新明细信息失败！");

        cpjgDto.setCpjgid(StringUtil.generateUUID());
        cpjgDto.setMjshl("0");
        cpjgDto.setBbdh("10");
        success = dao.insert(cpjgDto) != 0;
        if (!success)
            throw new BusinessException("msg","保存主表信息失败！");
        List<CpjgmxDto> cpjgmxDtos= JSON.parseArray(cpjgDto.getCpjgmx_json(),CpjgmxDto.class);
        int zjhh = 10;
        for (CpjgmxDto cpjgmxDto : cpjgmxDtos) {
            cpjgmxDto.setCpjgmxid(StringUtil.generateUUID());
            cpjgmxDto.setCpjgid(cpjgDto.getCpjgid());
            cpjgmxDto.setLrry(cpjgDto.getLrry());
            cpjgmxDto.setZjhh(String.valueOf(zjhh));
            zjhh = zjhh +10;
            cpjgmxDto.setGxhh("0000");
            cpjgmxDto.setZjshl("0");
            if (StringUtil.isNotBlank(cpjgmxDto.getJbyl())&&StringUtil.isNotBlank(cpjgmxDto.getJcsl())){
                BigDecimal jbyl = new BigDecimal(cpjgmxDto.getJbyl());
                BigDecimal jcsl = new BigDecimal(cpjgmxDto.getJcsl());
                String sysy = jbyl.divide(jcsl,2,RoundingMode.HALF_UP).toString();
                cpjgmxDto.setSysl(sysy);
            }
        }
        success = cpjgmxService.batchInsertDtoList(cpjgmxDtos);
        if (!success){
            throw new BusinessException("msg","添加明细数据出错！");
        }
        CpjgDto dtoById = dao.getDtoById(cpjgDto.getCpjgid());
        Map<String, Object> map = rdRecordService.addU8DataCpjg(dtoById, cpjgmxDtos);
        CpjgDto cpjgDto1 = (CpjgDto) map.get("cpjgDto");
        success = dao.update(cpjgDto1)!=0;
        if (!success)
            throw new BusinessException("msg","更新U8关联字段失败！");
        @SuppressWarnings("unchecked")
        List<CpjgmxDto> cpjgmxDtoList = (List<CpjgmxDto>) map.get("cpjgmxDtoList");
        success = cpjgmxService.updateList(cpjgmxDtoList);
        if (!success)
            throw new BusinessException("msg","更新U8明细关联字段失败！");
        return true;
    }



    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean saveDelProductInfo(CpjgDto cpjgDto) throws BusinessException {
        //先删除U8
        for (String id : cpjgDto.getIds()) {
            CpjgDto byId = dao.getDtoById(id);
            CpjgmxDto sc_cpjgmx = new CpjgmxDto();
            sc_cpjgmx.setCpjgid(id);
            List<CpjgmxDto> dtoList = cpjgmxService.getDtoList(sc_cpjgmx);
            rdRecordService.delU8DataCpjg(byId, dtoList);
        }
        cpjgDto.setScbj("1");
        boolean success =  dao.update(cpjgDto)!=0;
        if (!success)
            throw new BusinessException("msg","删除主表信息失败！");
        CpjgmxDto sc_cpjgmx = new CpjgmxDto();
        sc_cpjgmx.setIds(cpjgDto.getIds());
        sc_cpjgmx.setScbj("1");
        sc_cpjgmx.setScry(cpjgDto.getScry());
        success = cpjgmxService.update(sc_cpjgmx);
        if (!success)
            throw new BusinessException("msg","更新明细信息失败！");
        return true;
    }
}
