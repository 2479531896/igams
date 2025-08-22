package com.matridx.igams.hrm.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.SpgwDto;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.ISpgwService;
import com.matridx.igams.common.service.svcinterface.IXtshService;
import com.matridx.igams.hrm.dao.entities.CsszDto;
import com.matridx.igams.hrm.dao.entities.CsszModel;
import com.matridx.igams.hrm.dao.entities.QzszDto;
import com.matridx.igams.hrm.dao.post.ICsszDao;
import com.matridx.igams.hrm.service.svcinterface.ICsszService;
import com.matridx.igams.hrm.service.svcinterface.IQzszService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WYX
 * @version 1.0
 * @className CsszServiceImpl
 * @description TODO
 * @date 16:50 2023/1/9
 **/
@Service
public class CsszServiceImpl extends BaseBasicServiceImpl<CsszDto, CsszModel, ICsszDao> implements ICsszService {
    @Autowired
    IQzszService qzszService;
    @Autowired
    ISpgwService spgwService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IXtshService xtshService;
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveInitSetting(CsszDto csszDto) throws BusinessException {
        String csszid = StringUtil.generateUUID();
        csszDto.setCsszid(csszid);
        boolean isSuccess;
        isSuccess = insert(csszDto);
        if (!isSuccess){
            throw new BusinessException("msg","新增初始设置信息失败!");
        }
        //处理权重信息
        if (StringUtil.isNotBlank(csszDto.getQzxx_json())){
            List<QzszDto> qzszDtos = JSON.parseArray(csszDto.getQzxx_json(), QzszDto.class);
            if (!CollectionUtils.isEmpty(qzszDtos)){
                for (QzszDto qzszDto : qzszDtos) {
                    qzszDto.setQzszid(StringUtil.generateUUID());
                    qzszDto.setCsszid(csszid);
                }
                isSuccess = qzszService.insertQzszDtos(qzszDtos);
                if (!isSuccess){
                    throw new BusinessException("msg","新增权重设置信息失败!");
                }
            }
        }
        //处理绩效审核流程信息
        isSuccess = this.processJxShAndMbShAndAddGw(csszDto,csszDto.getLrrymc(),csszDto.getLrry());
        return isSuccess;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveInitSetting(CsszDto csszDto) throws BusinessException {
        boolean isSuccess;
        //删除权重信息
        QzszDto qzszDto_del = new QzszDto();
        qzszDto_del.setCsszid(csszDto.getCsszid());
        qzszService.delete(qzszDto_del);
        if (StringUtil.isNotBlank(csszDto.getQzxx_json())) {
            List<QzszDto> qzszDtos = JSON.parseArray(csszDto.getQzxx_json(), QzszDto.class);
            if (!CollectionUtils.isEmpty(qzszDtos)) {
                for (QzszDto dto : qzszDtos) {
                    dto.setQzszid(StringUtil.generateUUID());
                    dto.setCsszid(csszDto.getCsszid());
                }
                if (!CollectionUtils.isEmpty(qzszDtos)) {
                    isSuccess = qzszService.insertQzszDtos(qzszDtos);
                    if (!isSuccess) {
                        throw new BusinessException("msg", "新增权重设置信息失败!");
                    }
                }
            }
        }
        isSuccess = this.processJxShAndMbShAndAddGw(csszDto,csszDto.getXgrymc(),csszDto.getXgry());
        return isSuccess;
    }
    //处理新增岗位和模板审核流程信息
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean processJxShAndMbShAndAddGw(CsszDto csszDto, String rymc,String ryid) throws BusinessException {
        boolean isSuccess  = true;
        String nowDateStr = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss");
        //新增的审批岗位
        String addspgw_json = csszDto.getAddspgw_json();
        if (StringUtil.isNotBlank(addspgw_json)){
            List<SpgwDto> spgwDtos = JSON.parseArray(addspgw_json, SpgwDto.class);
            if (!CollectionUtils.isEmpty(spgwDtos)){
                isSuccess = spgwService.insertSpgwDtos(spgwDtos);
                if (!isSuccess){
                    throw new BusinessException("msg","新增审批岗位信息失败!");
                }
            }
        }
        //处理绩效流程信息
        //新增无流程系统审核标记
        boolean jxaddbj = false;
        ShlcDto shlcDto_jx = new ShlcDto();
        shlcDto_jx.setShid(csszDto.getJxshid());
        List<ShlcDto> jxShlcDtos = shlcService.getDtoListById(shlcDto_jx);
        String jxgw_json = csszDto.getJxgw_json();
        if (StringUtil.isNotBlank(jxgw_json)){
            List<SpgwDto> spgwDtos = JSON.parseArray(jxgw_json, SpgwDto.class);
            if (!CollectionUtils.isEmpty(spgwDtos)){
                StringBuilder jxshids = new StringBuilder();
                for (SpgwDto spgwDto : spgwDtos) {
                    if (StringUtil.isNotBlank(spgwDto.getGwid())){
                        jxshids.append(",").append(spgwDto.getGwid());
                    }
                }
                if (jxshids.length()>0){
                    jxshids = new StringBuilder(jxshids.substring(1));
                }
                StringBuilder yjxshids = new StringBuilder();
                if (!CollectionUtils.isEmpty(jxShlcDtos)){
                    for (ShlcDto jxShlcDto : jxShlcDtos) {
                        yjxshids.append(",").append(jxShlcDto.getGwid());
                    }
                    if (yjxshids.length()>0){
                        yjxshids = new StringBuilder(yjxshids.substring(1));
                    }
                }
                if (!jxshids.toString().equals(yjxshids.toString())){
                    XtshDto xtshDto = new XtshDto();
                    String shid = StringUtil.generateUUID();
                    csszDto.setJxshid(shid);
                    xtshDto.setShid(shid);
                    xtshDto.setShlb(AuditTypeEnum.AUDIT_PERFORMANCE.getCode());
                    xtshDto.setSfgb("0");
                    xtshDto.setShmc(csszDto.getKhlxmc());
                    xtshDto.setMs(rymc+ nowDateStr);
                    xtshDto.setMrsz("0");
                    xtshDto.setLrry(ryid);
                    isSuccess = xtshService.insertXtsh(xtshDto);
                    if (!isSuccess){
                        throw new BusinessException("msg","新增绩效系统审核信息失败!");
                    }
                    if (StringUtil.isNotBlank(jxshids.toString())){
                        List<SpgwDto> addShlcDtos = new ArrayList<>();
                        for (SpgwDto spgwDto_t : spgwDtos) {
                            if (StringUtil.isNotBlank(spgwDto_t.getGwid())){
                                SpgwDto spgwDto = new SpgwDto();
                                spgwDto.setGwid(spgwDto_t.getGwid());
                                spgwDto.setLcxh(spgwDto_t.getLcxh());
                                spgwDto.setShid(shid);
                                spgwDto.setLrry(ryid);
                                addShlcDtos.add(spgwDto);
                            }
                        }
                        if (!CollectionUtils.isEmpty(addShlcDtos)){
                            isSuccess = shlcService.insertBySpgwList(addShlcDtos);
                            if (!isSuccess){
                                throw new BusinessException("msg","新增绩效审核流程信息失败!");
                            }
                        }
                    }
                }
            }else {
                //如果之前不是空 现在是空 新增无流程系统审核
                if (!CollectionUtils.isEmpty(jxShlcDtos)){
                    jxaddbj = true;
                }
            }
        }else {
            if (!CollectionUtils.isEmpty(jxShlcDtos)){
                jxaddbj = true;
            }
        }
        if (jxaddbj){
            XtshDto xtshDto = new XtshDto();
            String shid = StringUtil.generateUUID();
            xtshDto.setShid(shid);
            csszDto.setJxshid(shid);
            xtshDto.setShlb(AuditTypeEnum.AUDIT_PERFORMANCE.getCode());
            xtshDto.setSfgb("0");
            xtshDto.setShmc(csszDto.getKhlxmc());
            xtshDto.setMs(rymc+ nowDateStr);
            xtshDto.setMrsz("0");
            xtshDto.setLrry(ryid);
            isSuccess = xtshService.insert(xtshDto);
            if (!isSuccess){
                throw new BusinessException("msg","新增绩效系统审核信息失败!");
            }
        }
        //处理模板流程信息
        //新增空模板系统审核标记
        boolean mbaddbj = false;
        ShlcDto shlcDto_mb = new ShlcDto();
        shlcDto_mb.setShid(csszDto.getMbshid());
        List<ShlcDto> mbShlcDtos = shlcService.getDtoListById(shlcDto_mb);
        String mbgw_json = csszDto.getMbgw_json();
        if (StringUtil.isNotBlank(mbgw_json)){
            List<SpgwDto> spgwDtos = JSON.parseArray(mbgw_json, SpgwDto.class);
            if (!CollectionUtils.isEmpty(spgwDtos)){
                StringBuilder mbshids = new StringBuilder();
                for (SpgwDto spgwDto : spgwDtos) {
                    if (StringUtil.isNotBlank(spgwDto.getGwid())){
                        mbshids.append(",").append(spgwDto.getGwid());
                    }
                }
                if (mbshids.length()>0){
                    mbshids = new StringBuilder(mbshids.substring(1));
                }
                StringBuilder ymbshids = new StringBuilder();
                if (!CollectionUtils.isEmpty(mbShlcDtos)){
                    for (ShlcDto mbShlcDto : mbShlcDtos) {
                        ymbshids.append(",").append(mbShlcDto.getGwid());
                    }
                    if (ymbshids.length()>0){
                        ymbshids = new StringBuilder(ymbshids.substring(1));
                    }
                }
                if (!mbshids.toString().equals(ymbshids.toString())){
                    XtshDto xtshDto = new XtshDto();
                    String shid = StringUtil.generateUUID();
                    xtshDto.setShid(shid);
                    csszDto.setMbshid(shid);
                    xtshDto.setShlb(AuditTypeEnum.AUDIT_PERFORMANCE_TEMPLATE.getCode());
                    xtshDto.setSfgb("0");
                    xtshDto.setShmc(csszDto.getKhlxmc());
                    xtshDto.setMs(rymc+ nowDateStr);
                    xtshDto.setMrsz("0");
                    xtshDto.setLrry(ryid);
                    isSuccess = xtshService.insertXtsh(xtshDto);
                    if (!isSuccess){
                        throw new BusinessException("msg","新增模板系统审核信息失败!");
                    }
                    if (StringUtil.isNotBlank(mbshids.toString())){
                        List<SpgwDto> addShlcDtos = new ArrayList<>();
                        for (SpgwDto spgwDto_t : spgwDtos) {
                            if (StringUtil.isNotBlank(spgwDto_t.getGwid())){
                                SpgwDto spgwDto = new SpgwDto();
                                spgwDto.setGwid(spgwDto_t.getGwid());
                                spgwDto.setLcxh(spgwDto_t.getLcxh());
                                spgwDto.setShid(shid);
                                spgwDto.setLrry(ryid);
                                addShlcDtos.add(spgwDto);
                            }
                        }
                        if (!CollectionUtils.isEmpty(addShlcDtos)){
                            isSuccess = shlcService.insertBySpgwList(addShlcDtos);
                            if (!isSuccess){
                                throw new BusinessException("msg","新增模板审核流程信息失败!");
                            }
                        }
                    }
                }
            }else {
                //如果之前不是空 现在是空 新增无流程系统审核
                if (!CollectionUtils.isEmpty(mbShlcDtos)){
                    mbaddbj = true;
                }
            }
        }else {
            if (!CollectionUtils.isEmpty(mbShlcDtos)){
                mbaddbj = true;
            }
        }
        if (mbaddbj){
            XtshDto xtshDto = new XtshDto();
            String shid = StringUtil.generateUUID();
            xtshDto.setShid(shid);
            csszDto.setMbshid(shid);
            xtshDto.setShlb(AuditTypeEnum.AUDIT_PERFORMANCE_TEMPLATE.getCode());
            xtshDto.setSfgb("0");
            xtshDto.setShmc(csszDto.getKhlxmc());
            xtshDto.setMs(rymc+ nowDateStr);
            xtshDto.setMrsz("0");
            xtshDto.setLrry(ryid);
            isSuccess = xtshService.insertXtsh(xtshDto);
            if (!isSuccess){
                throw new BusinessException("msg","新增模板系统审核信息失败!");
            }
        }
        update(csszDto);
        return isSuccess;
    }
    @Override
    public CsszDto getDtoByKhlx(String khlx) {
        return dao.getDtoByKhlx(khlx);
    }
}
