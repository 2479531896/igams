package com.matridx.igams.production.service.impl;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DeviceStateEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.production.dao.entities.SbfyDto;
import com.matridx.igams.production.dao.entities.SbjlDto;
import com.matridx.igams.production.dao.entities.SbjlModel;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.dao.post.ISbjlDao;
import com.matridx.igams.production.service.svcinterface.ISbfyService;
import com.matridx.igams.production.service.svcinterface.ISbjlService;
import com.matridx.igams.production.service.svcinterface.ISbysService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author:JYK
 */
@Service
public class SbjlServiceImpl extends BaseBasicServiceImpl<SbjlDto, SbjlModel, ISbjlDao> implements ISbjlService, IAuditService {
    @Autowired
    IShgcService shgcService;
    @Autowired
    ICommonService commonservice;
    @Autowired
    ISbysService sbysService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    ICommonService commonService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    ISbfyService sbfyService;

    private final Logger log = LoggerFactory.getLogger(SbjlServiceImpl.class);

    @Override
    public List<SbjlDto> getDepartmentList() {
        return dao.getDepartmentList();
    }

    @Override
    public List<SbjlDto> getGlryList() {
        return dao.getGlryList();
    }

    @Override
    public List<SbjlDto> getPagedAuditMetering(SbjlDto sbjlDto) {
        // 获取人员ID和履历号
        List<SbjlDto> t_sbyzList = dao.getPagedAuditMetering(sbjlDto);
        if(CollectionUtils.isEmpty(t_sbyzList))
            return t_sbyzList;
        List<SbjlDto> sqList = dao.getAuditListMetering(t_sbyzList);
        commonservice.setSqrxm(sqList);
        return sqList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean meteringSaveEquipmentAcceptance(SbysDto sbysDto, SbjlDto sbjlDto) throws BusinessException, ParseException {
        if (!CollectionUtils.isEmpty(sbysDto.getIds())){
            List<SbysDto> sbysDtos=new ArrayList<>();
            List<SbjlDto> sbjlDtos=new ArrayList<>();
            for (int i=0;i<sbysDto.getIds().size();i++){
                SbysDto sbysDto1=new SbysDto();
                SbjlDto sbjlDto1=new SbjlDto();
                sbysDto1.setSbysid(sbysDto.getIds().get(i));
                sbjlDto1.setSbjlid(StringUtil.generateUUID());
                sbysDto1.setLsid(sbjlDto1.getSbjlid());
                sbjlDto1.setSbysid(sbysDto.getIds().get(i));
                sbjlDto1.setSqr(sbjlDto.getSqr());
                sbjlDto1.setLrry(sbjlDto.getLrry());
                sbjlDto1.setSqbm(sbjlDto.getSqbm());
                sbjlDto1.setQwwcsj(sbjlDto.getQwwcsj());
                sbjlDto1.setZt("00");
                sbysDtos.add(sbysDto1);
                sbjlDtos.add(sbjlDto1);
            }
            boolean isSuccess=sbysService.updateList(sbysDtos);
            if (isSuccess){
                isSuccess=dao.insertList(sbjlDtos);
                if (!isSuccess){
                    throw new BusinessException("message","新增设备计量失败!");
                }
            }else {
                throw new BusinessException("message","更新设备验收失败!");
            }
            for (SbjlDto dto : sbjlDtos) {
                ShgcDto shgcDto_t = new ShgcDto();
                shgcDto_t.setExtend_1("[\"" + dto.getSbjlid() + "\"]");
                shgcDto_t.setShlb(AuditTypeEnum.AUDIT_DEVICE_METERING.getCode());
                User user = new User();
                user.setYhid(sbjlDto.getSqr());
                user.setZsxm(sbjlDto.getSqrmc());
                shgcDto_t.setLrry(sbjlDto.getSqr());
                shgcService.checkAndCommit(shgcDto_t, user);
            }
        }
        else{
            if (StringUtil.isNotBlank(sbjlDto.getJlrq())){
                SbjlDto sbjlDto1=getDtoById(sbjlDto.getSbjlid());
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                Date date1=format.parse(sbjlDto.getJlrq());
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(date1);
                calendar.add(Calendar.MONTH,Integer.parseInt(sbjlDto1.getJlzq()));
                date1=calendar.getTime();
                String xcjlsj=format.format(date1);
                SbysDto sbysDto1=new SbysDto();
                sbysDto1.setSbysid(sbjlDto1.getSbysid());
                sbysDto1.setXcjlsj(xcjlsj);
                sbysService.update(sbysDto1);
            }
            if (!update(sbjlDto)){
                throw new BusinessException("message","更新设备计量失败!");
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delSaveDeviceMetering(SbjlDto sbjlDto,String flag) {
        List<SbysDto> sbysDtos = new ArrayList<>();
        List<SbjlDto> sbjlDtos=dao.getDtoList(sbjlDto);
        //如果删除前该设备就是在计量 则修改该设备为原设备状态 否则不修改设备验收各状态
        for (SbjlDto sbjlDto1:sbjlDtos){
            if (DeviceStateEnum.METERING.getCode().equals(sbjlDto1.getSbzt())){
                SbysDto sbysDto = new SbysDto();
                sbysDto.setSbysid(sbjlDto1.getSbysid());
                sbysDto.setSbzt(sbjlDto1.getYsbzt());
                sbysDto.setShzt(StatusEnum.CHECK_NO.getCode());
                sbysDto.setXgry(sbjlDto.getScry());
                sbysDtos.add(sbysDto);
            }
        }
        if (!CollectionUtils.isEmpty(sbysDtos)){
            sbysService.updateListZt(sbysDtos);
        }
        boolean isSuccess =delete(sbjlDto);
        if (isSuccess&&"delete".equals(flag)){
            shgcService.deleteByYwids(sbjlDto.getIds());
        }
        return isSuccess;
    }

    /**
     * 计量列表（查询审核状态）
     */
    @Override
    public List<SbjlDto> getPagedDtoList(SbjlDto sbjlDto) {
        List<SbjlDto> list = dao.getPagedDtoList(sbjlDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_DEVICE_METERING.getCode(), "zt", "sbjlid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        SbjlDto sbjlDto = (SbjlDto)baseModel;
        sbjlDto.setXgry(operator.getYhid());
        boolean isSuccess;
        SbysDto sbysDto=new SbysDto();
        try {
            isSuccess = meteringSaveEquipmentAcceptance(sbysDto,sbjlDto);
            if(StringUtil.isNotBlank(sbjlDto.getJlrq())){
                JcsjDto jcsjDto = new JcsjDto();
                jcsjDto.setJclb(BasicDataTypeEnum.DEVICE_TYPE.getCode());
                jcsjDto.setCsdm("JL");
                JcsjDto jcsjDto1 = jcsjService.getDtoByCsdmAndJclb(jcsjDto);
                if(isSuccess && jcsjDto1!=null){
                    SbjlDto sbjlDtoT=getDtoById(sbjlDto.getSbjlid());
                    SbfyDto sbfyDto = new SbfyDto();
                    sbfyDto.setJe(sbjlDto.getJe());
                    sbfyDto.setBm(sbjlDtoT.getSqbm());
                    sbfyDto.setNr(sbjlDto.getNr());
                    sbfyDto.setFylx(jcsjDto1.getCsid());
                    sbfyDto.setSbysid(sbjlDtoT.getSbysid());
                    sbfyDto.setSbfyid(StringUtil.generateUUID());
                    sbfyDto.setSj(sbjlDto.getJlrq());
                    sbfyDto.setLrry(operator.getYhid());
                    sbfyService.insert(sbfyDto);
                }
            }
        } catch (ParseException e) {
            throw new BusinessException("msg","修改失败!");
        }
        return isSuccess;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {

        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        for (ShgcDto shgcDto : shgcList) {
            SbjlDto sbjlDto = new SbjlDto();
            sbjlDto.setSbjlid(shgcDto.getYwid());
            sbjlDto.setXgry(operator.getYhid());
            SbjlDto sbjlDto1 = getDtoById(sbjlDto.getSbjlid());
            SbysDto sbysDto=sbysService.getSbysDtoById(sbjlDto1.getSbysid());
            shgcDto.setSqbm(sbjlDto1.getSqbm());
            List<SpgwcyDto> spgwcyDtos = commonService.siftJgList(shgcDto.getSpgwcyDtos(),sbjlDto1.getSqbm());
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                sbjlDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                sbysDto.setShzt(StatusEnum.CHECK_METERING_UNPASS.getCode());
                sbysDto.setSbzt(sbjlDto1.getYsbzt());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(),
                                    xxglService.getMsg("ICOMM_SH00026"), xxglService.getMsg("ICOMM_SBJL00001", operator.getZsxm(), shgcDto.getShlbmc(), sbysDto.getSbbh(), sbysDto.getSbmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                sbjlDto.setZt(StatusEnum.CHECK_PASS.getCode());
                sbysDto.setShzt(StatusEnum.CHECK_PASS.getCode());
                sbysDto.setSbzt("01");
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    int size = spgwcyDtos.size();
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(),
                                    xxglService.getMsg("ICOMM_SH00006"), xxglService.getMsg("ICOMM_SBJL00002",
                                            operator.getZsxm(), shgcDto.getShlbmc(), sbysDto.getSbbh(), sbysDto.getSbmc(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else {
                sbysDto.setShzt(StatusEnum.CHECK_METERING_SUBMIT.getCode());
                sbjlDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if ("1".equals(shgcDto.getXlcxh())&& shgcDto.getShxx()==null) {
                    sbysDto.setSbzt(DeviceStateEnum.METERING.getCode());
                    sbysDto.setYsbzt(sbjlDto1.getSbzt());
                }
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos) ) {
                    try {
                        int size = spgwcyDtos.size();
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/auditpage/meteringgoods/meteringgoods&sbjlid=" + sbjlDto1.getSbjlid()
                                        + "&sbbh=" + sbjlDto1.getSbbh() + "&sbmc=" + sbjlDto1.getSbmc() + "&sblxmc=" + sbjlDto1.getSblxmc() + "&ggxh=" + sbjlDto1.getGgxh()
                                        + "&gdzcbh=" + sbjlDto1.getGdzcbh() + "&sbccbh=" + sbjlDto1.getSbccbh() + "&qwwcsj=" + sbjlDto1.getQwwcsj() + "&yjwcsj=" + sbjlDto1.getYjwcsj()
                                        + "&jlrq=" + sbjlDto1.getJlrq() + "&shlb=" + shgcDto.getShlb() + "&urlPrefix=" + urlPrefix, StandardCharsets.UTF_8);
                                List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                                btnJsonList.setTitle("小程序");
                                btnJsonList.setActionUrl(internalbtn);
                                btnJsonLists.add(btnJsonList);
                                talkUtil.sendCardDyxxMessageThread(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(),
                                        xxglService.getMsg("ICOMM_SH00003"),
                                        StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SBJL00003"),
                                                operator.getZsxm(), shgcDto.getShlbmc(), sbysDto.getSbbh(), sbysDto.getSbmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
                                        btnJsonLists, "1");
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
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,sbysDto.getSbmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(sbjlDto);
            sbysDto.setXgry(operator.getYhid());
            sbysService.update(sbysDto);
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
                SbjlDto sbjlDto = new SbjlDto();
                sbjlDto.setSbjlid(shgcDto.getYwid());
                sbjlDto.setXgry(operator.getYhid());
                SbjlDto sbjlDto1=getDtoById(sbjlDto.getSbjlid());
                SbysDto sbysDto=new SbysDto();
                sbysDto.setSbysid(sbjlDto1.getSbysid());
                sbjlDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                sbysDto.setShzt(StatusEnum.CHECK_SUBMIT.getCode());
                sbysDto.setSbzt("06");
                dao.update(sbjlDto);
                sbysService.update(sbysDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                SbjlDto sbjlDto = new SbjlDto();
                sbjlDto.setSbjlid(shgcDto.getYwid());
                sbjlDto.setXgry(operator.getYhid());
                SbjlDto sbjlDto1=getDtoById(sbjlDto.getSbjlid());
                SbysDto sbysDto=new SbysDto();
                sbysDto.setSbysid(sbjlDto1.getSbysid());
                sbjlDto.setZt(StatusEnum.CHECK_NO.getCode());
                sbysDto.setShzt(StatusEnum.CHECK_NO.getCode());
                sbysDto.setSbzt(sbjlDto1.getYsbzt());
                dao.update(sbjlDto);
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
        SbjlDto sbjlDto = new SbjlDto();
        sbjlDto.setIds(ids);
        List<SbjlDto> dtoList = dao.getDtoList(sbjlDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(SbjlDto dto:dtoList){
                list.add(dto.getSbjlid());
            }
        }
        map.put("list",list);
        return map;
    }
}
