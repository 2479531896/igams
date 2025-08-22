package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.production.dao.entities.*;
import com.matridx.igams.production.dao.post.ISbpdDao;
import com.matridx.igams.production.dao.post.ISbysDao;
import com.matridx.igams.production.service.svcinterface.ISbpdService;
import com.matridx.igams.production.service.svcinterface.ISbpdmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SbpdServiceImpl extends BaseBasicServiceImpl<SbpdDto, SbpdModel, ISbpdDao> implements ISbpdService, IAuditService {
    @Autowired
    ICommonService commonService;
    @Autowired
    private ISbysDao sbysDao;
    @Autowired
    private ISbpdmxService sbpdmxService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    private final Logger log = LoggerFactory.getLogger(SbpdServiceImpl.class);

    @Override
    public List<Map<String, String>> getDepartments() {
        return dao.getDepartments();
    }

    /**
     * 审核列表
     */
    @Override
    public List<SbpdDto> getPagedAuditEquipmentInventory(SbpdDto sbpdDto){
        // 获取人员ID和履历号
        List<SbpdDto> t_List= dao.getPagedAuditEquipmentInventory(sbpdDto);

        if(CollectionUtils.isEmpty(t_List))
            return t_List;

        List<SbpdDto> List = dao.getAuditListEquipmentInventory(t_List);

        commonService.setSqrxm(List);

        return List;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delEquipmentInventory(SbpdDto sbpdDto) {
        SbpdmxDto sbpdmxDto=new SbpdmxDto();
        sbpdmxDto.setIds(sbpdDto.getTgids());
        List<SbpdmxDto> dtoList = sbpdmxService.getDtoList(sbpdmxDto);
        if(dtoList!=null&&!dtoList.isEmpty()){
            List<String> ids =new ArrayList<>();
            for(SbpdmxDto dto:dtoList){
                ids.add(dto.getSbysid());
            }
            SbysDto sbysDto=new SbysDto();
            sbysDto.setIds(ids);
            sbysDto.setXgry(sbpdDto.getScry());
            boolean updated = sbysDao.updatePdztToNull(sbysDto);
            if(!updated){
                return false;
            }
        }
        int delete = dao.delete(sbpdDto);
        if(delete==0){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addEquipmentInventory(SbpdDto sbpdDto) {
        SbysDto sbysDto_sel = new SbysDto();
        sbysDto_sel.setIds(sbpdDto.getIds());
        List<SbysDto> listForInventory = sbysDao.getListForInventory(sbysDto_sel);
        if(listForInventory!=null&&!listForInventory.isEmpty()){
            List<SbpdDto> sbpdDtos=new ArrayList<>();
            List<SbysDto> sbysDtos=new ArrayList<>();
            List<SbpdmxDto> sbpdmxDtos=new ArrayList<>();
            for(SbysDto sbysDto:listForInventory){
                SbpdDto sbpdDto_add=new SbpdDto();
                sbpdDto_add.setSbpdid(StringUtil.generateUUID());
                sbpdDto_add.setFqry(sbpdDto.getLrry());
                sbpdDto_add.setFqsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
                sbpdDto_add.setPdry(sbysDto.getBmsbfzr());
                sbpdDto_add.setBm(sbysDto.getBmid());
                sbpdDto_add.setZt(StatusEnum.CHECK_NO.getCode());
                sbpdDto_add.setLrry(sbpdDto.getLrry());
                sbpdDtos.add(sbpdDto_add);
                for(String sbysid:sbysDto.getSbysid().split(",")){
                    SbysDto sbysDto_t=new SbysDto();
                    sbysDto_t.setSbysid(sbysid);
                    sbysDtos.add(sbysDto_t);
                    SbpdmxDto sbpdmxDto=new SbpdmxDto();
                    sbpdmxDto.setSbpdid(sbpdDto_add.getSbpdid());
                    sbpdmxDto.setPdmxid(StringUtil.generateUUID());
                    sbpdmxDto.setSbysid(sbysid);
                    sbpdmxDtos.add(sbpdmxDto);
                }
            }
            if(!sbpdDtos.isEmpty()){
                boolean inserted = dao.insertList(sbpdDtos);
                if(!inserted){
                    return false;
                }
            }
            if(!sbpdmxDtos.isEmpty()){
                boolean inserted = sbpdmxService.insertList(sbpdmxDtos);
                if(!inserted){
                    return false;
                }
            }
            if(!sbysDtos.isEmpty()){
                boolean updated = sbysDao.updateForInventory(sbysDtos);
                if(!updated){
                    return false;
                }
            }
            for(SbysDto sbysDto:listForInventory){
                if (StringUtil.isNotBlank(sbysDto.getDdid())) {
                    talkUtil.sendWorkMessage(sbysDto.getYhm(), sbysDto.getDdid(), "设备盘点通知", "请在设备盘点列表进行设备盘点!");
                }
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean modEquipmentInventory(SbpdDto sbpdDto) {
        int update = dao.update(sbpdDto);
        if(update==0){
            return false;
        }
        List<SbpdmxDto> list=(List<SbpdmxDto>) JSON.parseArray(sbpdDto.getPdmx_json(),SbpdmxDto.class);
        if(list!=null&&!list.isEmpty()){
            for (SbpdmxDto sbpdmxDto : list) {
                if (StringUtil.isBlank(sbpdmxDto.getPdzt())){
                    sbpdmxDto.setPdzt("00");
                }
            }
            boolean updateList = sbpdmxService.updateList(list);
            if(!updateList){
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        SbpdDto sbpdDto = (SbpdDto)baseModel;
        sbpdDto.setXgry(operator.getYhid());
        return modEquipmentInventory(sbpdDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
//        String token = talkUtil.getToken();
        for (ShgcDto shgcDto : shgcList) {
            SbpdDto sbpdDto = new SbpdDto();
            sbpdDto.setSbpdid(shgcDto.getYwid());
            sbpdDto.setXgry(operator.getYhid());
            SbpdDto sbpdDto_t = getDto(sbpdDto);
            shgcDto.setSqbm(sbpdDto_t.getBm());
            List<SpgwcyDto> spgwcyDtos = commonService.siftJgList(shgcDto.getSpgwcyDtos(), sbpdDto_t.getBm());
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                sbpdDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(),
                                    xxglService.getMsg("ICOMM_SH00026"), xxglService.getMsg("ICOMM_SBPD002", operator.getZsxm(), shgcDto.getShlbmc(), sbpdDto_t.getFqrymc(), sbpdDto_t.getFqsj(), sbpdDto_t.getPdrymc(), sbpdDto_t.getBmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                sbpdDto.setZt(StatusEnum.CHECK_PASS.getCode());
                SbpdmxDto sbpdmxDto=new SbpdmxDto();
                sbpdmxDto.setSbpdid(sbpdDto_t.getSbpdid());
                List<SbpdmxDto> dtoList = sbpdmxService.getDtoList(sbpdmxDto);
                if(dtoList!=null&&!dtoList.isEmpty()){
                    List<SbysDto> sbysDtos=new ArrayList<>();
                    for(SbpdmxDto dto:dtoList){
                        SbysDto sbysDto=new SbysDto();
                        sbysDto.setSbysid(dto.getSbysid());
                        sbysDto.setPdzt(dto.getPdzt());
                        sbysDtos.add(sbysDto);
                    }
                    boolean updated = sbysDao.updateForInventory(sbysDtos);
                    if(!updated){
                        return false;
                    }
                }
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(),
                                    xxglService.getMsg("ICOMM_SH00006"), xxglService.getMsg("ICOMM_SBPD002",
                                            operator.getZsxm(), shgcDto.getShlbmc(), sbpdDto_t.getFqrymc(), sbpdDto_t.getFqsj(), sbpdDto_t.getPdrymc(), sbpdDto_t.getBmmc(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else {
                sbpdDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00003"), xxglService.getMsg("ICOMM_SBPD001", operator.getZsxm(), shgcDto.getShlbmc(), sbpdDto_t.getFqrymc(), sbpdDto_t.getFqsj(), sbpdDto_t.getPdrymc(), sbpdDto_t.getBmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
                //发送钉钉消息--取消审核人员
                if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())){
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SBPD003",operator.getZsxm(),shgcDto.getShlbmc() , sbpdDto_t.getFqrymc(), sbpdDto_t.getFqsj(), sbpdDto_t.getPdrymc(), sbpdDto_t.getBmmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            dao.update(sbpdDto);
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        // TODO Auto-generated method stub
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String sbysid = shgcDto.getYwid();
                SbpdDto sbpdDto = new SbpdDto();
                sbpdDto.setSbpdid(sbysid);
                sbpdDto.setXgry(operator.getYhid());
                sbpdDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(sbpdDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                //String fpid = shgcDto.getYwid();
                String sbpdid = shgcDto.getYwid();
                SbpdDto sbpdDto = new SbpdDto();
                sbpdDto.setSbpdid(sbpdid);
                sbpdDto.setXgry(operator.getYhid());
                sbpdDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(sbpdDto);
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
        return null;
    }
}
