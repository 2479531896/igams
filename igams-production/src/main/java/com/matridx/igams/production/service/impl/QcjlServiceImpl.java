package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.production.dao.entities.QcjlDto;
import com.matridx.igams.production.dao.entities.QcjlModel;
import com.matridx.igams.production.dao.entities.QcxmDto;
import com.matridx.igams.production.dao.post.IQcjlDao;
import com.matridx.igams.production.service.svcinterface.IQcjlService;
import com.matridx.igams.production.service.svcinterface.IQcxmService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QcjlServiceImpl extends BaseBasicServiceImpl<QcjlDto, QcjlModel, IQcjlDao> implements IQcjlService, IAuditService {

    @Autowired
    IShgcService shgcService;
    @Autowired
    ICommonService commonService;
    @Autowired
    private IQcxmService qcxmService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;

    private final Logger log = LoggerFactory.getLogger(QcjlServiceImpl.class);
    @Override
    public List<QcjlDto> getPagedDtoList(QcjlDto qcjlDto) {
        List<QcjlDto> list = dao.getPagedDtoList(qcjlDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_CLEARING_RECORDS.getCode(), "zt", "qcjlid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    @Override
    public List<QcjlDto> getPagedAuditClearingRecords(QcjlDto qcjlDto) {
        List<QcjlDto> t_List= dao.getPagedClearingRecords(qcjlDto);

        if (CollectionUtils.isEmpty(t_List))
            return t_List;

        List<QcjlDto> sqList = dao.getAuditListClearingRecords(t_List);

        commonService.setSqrxm(sqList);

        return sqList;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delClearingRecords(QcjlDto qcjlDto) {
        int deleted = dao.delete(qcjlDto);
        if(deleted==0){
            return false;
        }
        QcxmDto qcxmDto=new QcxmDto();
        qcxmDto.setScry(qcjlDto.getScry());
        qcxmDto.setIds(qcjlDto.getIds());
        boolean deleted1 = qcxmService.delete(qcxmDto);
        if(!deleted1){
            return false;
        }
        return true;
    }

    @Override
    public String generateJlbh() {
        String date = DateUtils.getCustomFomratCurrentDate("yyyy-MMdd");
        String prefix = "P-" + date + "-";
        //查询流水号
        String serial = dao.generateJlbh(prefix);
        return prefix+serial;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean clearSaveClearingRecords(QcjlDto qcjlDto) {
        //qcjlDto.setJlbh(generateJlbh());
        qcjlDto.setQcjlid(StringUtil.generateUUID());
        qcjlDto.setZt(StatusEnum.CHECK_NO.getCode());
        int inserted = dao.insert(qcjlDto);
        if(inserted==0){
            return false;
        }
        List<QcxmDto> list=(List<QcxmDto>)JSON.parseArray(qcjlDto.getQcxm_json(),QcxmDto.class);
        if(list!=null&&!list.isEmpty()){
            for(QcxmDto dto:list){
                dto.setQcxmid(StringUtil.generateUUID());
                dto.setQcjlid(qcjlDto.getQcjlid());
                dto.setLrry(qcjlDto.getLrry());
            }
            boolean inserted1 = qcxmService.insertList(list);
            if(!inserted1){
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveClearingRecords(QcjlDto qcjlDto) {
        int updated = dao.update(qcjlDto);
        if(updated==0){
            return false;
        }
        QcxmDto qcxmDto=new QcxmDto();
        qcxmDto.setIds(qcjlDto.getQcjlid());
        qcxmService.delAbandonedData(qcxmDto);
        qcxmDto.setScry(qcjlDto.getXgry());
        qcxmService.delete(qcxmDto);
        List<QcxmDto> list=(List<QcxmDto>)JSON.parseArray(qcjlDto.getQcxm_json(),QcxmDto.class);
        if(list!=null&&!list.isEmpty()){
            for(QcxmDto dto:list){
                dto.setQcxmid(StringUtil.generateUUID());
                dto.setQcjlid(qcjlDto.getQcjlid());
                dto.setLrry(qcjlDto.getLrry());
            }
            boolean inserted1 = qcxmService.insertList(list);
            if(!inserted1){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        QcjlDto qcjlDto= (QcjlDto) baseModel;
        qcjlDto.setXgry(operator.getYhid());
        qcjlDto.setJcr(operator.getYhid());
        return modSaveClearingRecords(qcjlDto);
    }

    @Override
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        for (ShgcDto shgcDto : shgcList) {
            QcjlDto qcjlDto=new QcjlDto();
            qcjlDto.setQcjlid(shgcDto.getYwid());
            qcjlDto.setXgry(shgcDto.getYhid());
            QcjlDto qcjlDto_t=dao.getDtoById(shgcDto.getYwid());
            List<SpgwcyDto> spgwcyDtos=shgcDto.getSpgwcyDtos();
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                qcjlDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if(!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00026"),
                                    StringUtil.replaceMsg(xxglService.getMsg("ICOMM_QC00001"), operator.getZsxm(), shgcDto.getShlbmc(),
                                            qcjlDto_t.getFjmc(), qcjlDto_t.getSsgx(), qcjlDto_t.getWlbm(), qcjlDto_t.getJlbh(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            }else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                qcjlDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if(!CollectionUtils.isEmpty(spgwcyDtos)) {
                    int size = spgwcyDtos.size();
                    for (int i = 0; i < size; i++) {
                        if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),spgwcyDtos.get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00006"),
                                    StringUtil. replaceMsg(xxglService.getMsg("ICOMM_QC00002"), operator.getZsxm(), shgcDto.getShlbmc(),
                                            qcjlDto_t.getFjmc(), qcjlDto_t.getSsgx(), qcjlDto_t.getWlbm(), qcjlDto_t.getJlbh(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else {
                qcjlDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                // 发送钉钉消息
                if(!CollectionUtils.isEmpty(spgwcyDtos)) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00003"),
                                        StringUtil.replaceMsg(xxglService.getMsg("ICOMM_QC00003"), operator.getZsxm(), shgcDto.getShlbmc(),
                                                qcjlDto_t.getFjmc(), qcjlDto_t.getSsgx(), qcjlDto_t.getWlbm(), qcjlDto_t.getJlbh(),
                                                DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
                //发送钉钉消息--取消审核人员
                if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())) {
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
                                    shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005", operator.getZsxm(),shgcDto.getShlbmc(),
                                    DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            dao.update(qcjlDto);
        }
        return true;
    }

    @Override
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                QcjlDto qcjlDto = new QcjlDto();
                qcjlDto.setXgry(operator.getYhid());
                qcjlDto.setQcjlid(shgcDto.getYwid());
                qcjlDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(qcjlDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                QcjlDto qcjlDto = new QcjlDto();
                qcjlDto.setXgry(operator.getYhid());
                qcjlDto.setQcjlid(shgcDto.getYwid());
                qcjlDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(qcjlDto);
            }
        }
        return true;
    }

    @Override
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) throws BusinessException {
        return null;
    }

    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) throws BusinessException {
        Map<String, Object> map =new HashMap<>();
        return map;
    }
}
