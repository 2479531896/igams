package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.DeviceStateEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.service.svcinterface.ISbysService;
import com.matridx.igams.storehouse.dao.entities.SbtkDto;
import com.matridx.igams.storehouse.dao.entities.SbtkModel;
import com.matridx.igams.storehouse.dao.post.ISbtkDao;
import com.matridx.igams.storehouse.service.svcinterface.ISbtkService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SbtkServiceImpl extends BaseBasicServiceImpl<SbtkDto, SbtkModel, ISbtkDao> implements ISbtkService, IAuditService {
    @Autowired
    IShgcService shgcService;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ICommonService commonService;
    @Autowired
    ISbysService sbysService;
    @Autowired
    IXtszService xtszService;
    private final Logger log = LoggerFactory.getLogger(SbtkServiceImpl.class);

    /**
     * 设备维修列表（查询审核状态）
     *
     * @param sbtkDto
     * @return
     */
    @Override
    public List<SbtkDto> getPagedDtoList(SbtkDto sbtkDto) {
        List<SbtkDto> list = dao.getPagedDtoList(sbtkDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_DEVICE_STOCKRETURN.getCode(), "zt", "sbtkid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
        SbtkDto sbtkDto = (SbtkDto)baseModel;
        sbtkDto.setXgry(operator.getYhid());
        return this.update(sbtkDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
        String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
        for (ShgcDto shgcDto : shgcList) {
            SbtkDto sbtkDto = new SbtkDto();
            sbtkDto.setSbtkid(shgcDto.getYwid());
            sbtkDto.setXgry(operator.getYhid());
            SbtkDto sbtkDto_t = getDtoById(sbtkDto.getSbtkid());
            shgcDto.setSqbm(sbtkDto_t.getSqbm());
            List<SpgwcyDto> spgwcyDtos = commonService.siftJgList(shgcDto.getSpgwcyDtos(),sbtkDto_t.getSqbm());
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                //修改审核状态为不通过 设备状态为使用中
                SbysDto sbysDto = new SbysDto();
                sbysDto.setSbysid(sbtkDto_t.getSbysid());
                sbysDto.setSbzt(sbtkDto_t.getYsbzt());
                sbysDto.setLsid(sbtkDto_t.getSbtkid());
                sbysDto.setShzt(StatusEnum.CHECK_UNPASS.getCode());
                sbysDto.setXgry(operator.getYhid());
                sbysService.update(sbysDto);
                sbtkDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00026,
                                    xxglService.getMsg("ICOMM_SBWX001", operator.getZsxm(), shgcDto.getShlbmc(), sbtkDto_t.getSqrmc(),
                                            sbtkDto_t.getSqbmmc(),
                                            sbtkDto_t.getSqrq(),
                                            sbtkDto_t.getSbmc()));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                //修改审核状态为审核通过 设备状态为在库
                SbysDto sbysDto = new SbysDto();
                sbysDto.setSbysid(sbtkDto_t.getSbysid());
                sbysDto.setSbzt(DeviceStateEnum.INSTOCK.getCode());
                sbysDto.setShzt(StatusEnum.CHECK_PASS.getCode());
                sbysDto.setXgry(operator.getYhid());
                XtszDto xtszDto = xtszService.getDtoById("default.device.location");
                if(xtszDto!=null && StringUtil.isNotBlank(xtszDto.getSzz())){
                    sbysDto.setSydd(xtszDto.getSzz());
                }
                XtszDto xtszDtoT = xtszService.getDtoById("default.device.department");
                if(xtszDtoT!=null && StringUtil.isNotBlank(xtszDtoT.getSzz())){
                    sbysDto.setSydd(xtszDtoT.getSzz());
                }
                sbysDto.setBmsbfzr("1");
                sbysService.update(sbysDto);

                sbtkDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00006,
                                    xxglService.getMsg("ICOMM_SBWX002", operator.getZsxm(), shgcDto.getShlbmc(), sbtkDto_t.getSqrmc(),
                                            sbtkDto_t.getSqbmmc(),
                                            sbtkDto_t.getSqrq(),
                                            sbtkDto_t.getSbmc()));
                        }
                    }
                }
            }else {
                sbtkDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if ("1".equals(shgcDto.getXlcxh())&& shgcDto.getShxx()==null){
                    //修改审核状态为审核中 设备状态为退库
                    SbysDto sbysDto = new SbysDto();
                    sbysDto.setSbysid(sbtkDto_t.getSbysid());
                    sbysDto.setSbzt(DeviceStateEnum.OUTSTOCK.getCode());
                    sbysDto.setYsbzt(sbtkDto_t.getSbzt());
                    sbysDto.setLsid(sbtkDto_t.getSbtkid());
                    sbysDto.setShzt(StatusEnum.CHECK_SUBMIT.getCode());
                    sbysDto.setXgry(operator.getYhid());
                    sbysService.update(sbysDto);
                }
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                        spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00003"),
                                        xxglService.getMsg("ICOMM_SBWX003"
                                                , operator.getZsxm(), shgcDto.getShlbmc(), sbtkDto_t.getSqrmc(),
                                                sbtkDto_t.getSqbmmc(),
                                                sbtkDto_t.getSqrq(),
                                                sbtkDto_t.getSbmc()));
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
                //发送钉钉消息--取消审核人员
                if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos()) ){
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
                                    shgcDto.getNo_spgwcyDtos().get(i).getYhid(),xxglService.getMsg("ICOMM_SH00004"), xxglService.getMsg("ICOMM_SH00005",shgcDto.getShlbmc() , StringUtil.isNotBlank(sbtkDto_t.getSbbh())?sbtkDto_t.getSbbh():"无", DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(sbtkDto);
        }
        return true;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        // TODO Auto-generated method stub
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String sbtkid = shgcDto.getYwid();
                SbtkDto sbtkDto = new SbtkDto();
                sbtkDto.setSbtkid(sbtkid);
                sbtkDto.setXgry(operator.getYhid());
                sbtkDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(sbtkDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String sbtkid = shgcDto.getYwid();
                SbtkDto sbtkDto = new SbtkDto();
                sbtkDto.setSbtkid(sbtkid);
                sbtkDto.setXgry(operator.getYhid());
                sbtkDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(sbtkDto);
                SbtkDto sbtkDto_t = dao.getDtoById(sbtkid);
                SbysDto sbysDto = new SbysDto();
                sbysDto.setSbysid(sbtkDto_t.getSbysid());
                sbysDto.setLsid(sbtkDto_t.getSbtkid());
                sbysDto.setSbzt(sbtkDto_t.getYsbzt());
                sbysDto.setShzt(StatusEnum.CHECK_NO.getCode());
                sbysDto.setXgry(operator.getYhid());
                sbysService.update(sbysDto);
            }
        }
        return true;
    }

    @Override
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
        return null;
    }

    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
        Map<String, Object> map =new HashMap<>();
        @SuppressWarnings("unchecked")
        List<String> ids = (List<String>)param.get("ywids");
        SbtkDto sbtkDto = new SbtkDto();
        sbtkDto.setIds(ids);
        List<SbtkDto> dtoList = dao.getDtoList(sbtkDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(SbtkDto dto:dtoList){
                list.add(dto.getSbtkid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Override
    public List<SbtkDto> getDepartmentList() {
        return dao.getDepartmentList();
    }

    @Override
    public List<SbtkDto> getGlryList() {
        return dao.getGlryList();
    }
    @Override
    public List<SbtkDto> getPagedAuditStockreturnDevice(SbtkDto sbtkDto) {
        // 获取人员ID和履历号
        List<SbtkDto> t_sbtkList = dao.getPagedAuditStockreturnDevice(sbtkDto);
        if (CollectionUtils.isEmpty(t_sbtkList))
            return t_sbtkList;
        List<SbtkDto> sqList = dao.getAuditListStockreturnDevice(t_sbtkList);
        commonService.setSqrxm(sqList);
        return sqList;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delStockreturnDevice(SbtkDto sbtkDto) throws BusinessException {
        List<SbtkDto> dtoList = dao.getDtoList(sbtkDto);
        List<SbysDto> sbysDtos=new ArrayList<>();
        //如果删除前该设备就是在退库 则修改该设备为原设备状态 否则不修改设备验收各状态
        for (SbtkDto sbtkDto1:dtoList){
            if (DeviceStateEnum.OUTSTOCK.getCode().equals(sbtkDto1.getSbzt())){
                SbysDto sbysDto = new SbysDto();
                sbysDto.setSbysid(sbtkDto1.getSbysid());
                sbysDto.setSbzt(sbtkDto1.getYsbzt());
                sbysDto.setShzt(StatusEnum.CHECK_NO.getCode());
                sbysDto.setXgry(sbtkDto.getScry());
                sbysDtos.add(sbysDto);
            }
        }
        if (!CollectionUtils.isEmpty(sbysDtos)){
            sbysService.updateListZt(sbysDtos);
        }
        boolean delete = delete(sbtkDto);
        if (!delete){
            throw new BusinessException("msg","删除设备维修记录失败！");
        }
        shgcService.deleteByYwids(sbtkDto.getIds());
        return true;
    }
}
