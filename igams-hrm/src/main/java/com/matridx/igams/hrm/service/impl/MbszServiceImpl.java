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
import com.matridx.igams.hrm.dao.entities.MbszDto;
import com.matridx.igams.hrm.dao.entities.MbszModel;
import com.matridx.igams.hrm.dao.entities.QzszDto;
import com.matridx.igams.hrm.dao.post.IMbszDao;
import com.matridx.igams.hrm.service.svcinterface.IMbszService;
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
 * @author:JYK
 */
@Service
public class MbszServiceImpl extends BaseBasicServiceImpl<MbszDto, MbszModel, IMbszDao> implements IMbszService {
    @Autowired
    ISpgwService spgwService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IXtshService xtshService;
    @Autowired
    IQzszService qzszService;
    @Override
    public boolean updateMbszDtos(List<MbszDto> mbszDtos) {
        return dao.updateMbszDtos(mbszDtos);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean advancedTemplateSetting(MbszDto mbszDto) throws BusinessException {
        boolean isSuccess;
        MbszDto mbszDto_del = new MbszDto();
        mbszDto_del.setScbj("2");
        mbszDto_del.setMbszid(mbszDto.getMbszid());
        mbszDto_del.setXgry(mbszDto.getLrry());
        isSuccess = update(mbszDto_del);
        if (!isSuccess){
            throw new BusinessException("msg","修改模板设置信息失败!");
        }
        String mbszid = StringUtil.generateUUID();
        mbszDto.setMbszid(mbszid);
        mbszDto.setScbj("0");
        isSuccess = insert(mbszDto);
        if (!isSuccess){
            throw new BusinessException("msg","新增模板设置信息失败!");
        }
        //处理权重信息
        if (StringUtil.isNotBlank(mbszDto.getQzxx_json())){
            List<QzszDto> qzszDtos = JSON.parseArray(mbszDto.getQzxx_json(), QzszDto.class);
            if (!CollectionUtils.isEmpty(qzszDtos)){
                for (QzszDto qzszDto : qzszDtos) {
                    qzszDto.setQzszid(StringUtil.generateUUID());
                    qzszDto.setMbszid(mbszid);
                    qzszDto.setJxmbid(mbszDto.getMbid());
                }
                isSuccess = qzszService.insertQzszDtos(qzszDtos);
                if (!isSuccess){
                    throw new BusinessException("msg","新增权重设置信息失败!");
                }
            }
        }
        isSuccess = this.processJxShAndAddGw(mbszDto, mbszDto.getLrrymc(), mbszDto.getLrry());
        return isSuccess;
    }

    @Override
    public boolean deleteBymbid(MbszDto mbszDto) {
        return dao.deleteBymbid(mbszDto);
    }

    //处理新增岗位和绩效审核流程信息
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean processJxShAndAddGw(MbszDto mbszDto, String rymc, String ryid) throws BusinessException {
        boolean isSuccess  = true;
        String nowDateStr = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss");
        //新增的审批岗位
        String addspgw_json = mbszDto.getAddspgw_json();
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
        shlcDto_jx.setShid(mbszDto.getJxshid());
        List<ShlcDto> jxShlcDtos = shlcService.getDtoListById(shlcDto_jx);
        String jxgw_json = mbszDto.getJxgw_json();
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
                    mbszDto.setJxshid(shid);
                    xtshDto.setShid(shid);
                    xtshDto.setShlb(AuditTypeEnum.AUDIT_PERFORMANCE.getCode());
                    xtshDto.setSfgb("0");
                    xtshDto.setShmc(mbszDto.getKhlxmc());
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
            mbszDto.setJxshid(shid);
            xtshDto.setShlb(AuditTypeEnum.AUDIT_PERFORMANCE.getCode());
            xtshDto.setSfgb("0");
            xtshDto.setShmc(mbszDto.getKhlxmc());
            xtshDto.setMs(rymc+ nowDateStr);
            xtshDto.setMrsz("0");
            xtshDto.setLrry(ryid);
            isSuccess = xtshService.insert(xtshDto);
            if (!isSuccess){
                throw new BusinessException("msg","新增绩效系统审核信息失败!");
            }
        }
        update(mbszDto);
        return isSuccess;
    }

    @Override
    public boolean updateSxrqAndYxq(MbszDto mbszDto) {
        return dao.updateSxrqAndYxq(mbszDto);
    }
}
