package com.matridx.igams.production.service.impl;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.DeviceStateEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.dao.entities.SbyzDto;
import com.matridx.igams.production.dao.entities.SbyzModel;
import com.matridx.igams.production.dao.post.ISbyzDao;
import com.matridx.igams.production.service.svcinterface.ISbjlService;
import com.matridx.igams.production.service.svcinterface.ISbysService;
import com.matridx.igams.production.service.svcinterface.ISbyzService;
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
public class SbyzServiceImpl extends BaseBasicServiceImpl<SbyzDto, SbyzModel, ISbyzDao> implements ISbyzService, IAuditService {
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
    ISbjlService sbjlService;
    @Autowired
    ICommonService commonService;
    private final Logger log = LoggerFactory.getLogger(SbyzServiceImpl.class);

    /**
     * 设备验证列表（查询审核状态）
     */
    @Override
    public List<SbyzDto> getPagedDtoList(SbyzDto sbyzDto) {
        List<SbyzDto> list = dao.getPagedDtoList(sbyzDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_DEVICE_TEST.getCode(), "zt", "sbyzid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    @Override
    public List<SbyzDto> getPagedAuditTesting(SbyzDto sbyzDto) {
        // 获取人员ID和履历号
        List<SbyzDto> t_sbyzList = dao.getPagedAuditTesting(sbyzDto);
        if(CollectionUtils.isEmpty(t_sbyzList))
            return t_sbyzList;
        List<SbyzDto> sqList = dao.getAuditListTesting(t_sbyzList);
        commonservice.setSqrxm(sqList);
        return sqList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean checkingSaveEquipmentAcceptance(SbysDto sbysDto,SbyzDto sbyzDto, User user) throws BusinessException, ParseException {
        if (!CollectionUtils.isEmpty(sbysDto.getIds())){
            List<SbyzDto> sbyzDtos=new ArrayList<>();
            List<SbysDto> sbysDtos=new ArrayList<>();
            for (int i=0;i<sbysDto.getIds().size();i++){
                SbyzDto sbyzDto_t=new SbyzDto();
                sbyzDto_t.setSbyzid(StringUtil.generateUUID());
                sbyzDto_t.setLrry(user.getYhid());
                sbyzDto_t.setSqr(user.getYhid());
                sbyzDto_t.setSqbm(user.getJgid());
                sbyzDto_t.setQwwcsj(sbysDto.getQwwcsj());
                sbyzDto_t.setZt("00");
                sbyzDto_t.setSbysid(sbysDto.getIds().get(i));
                SbysDto sbysDto_t=new SbysDto();
                sbysDto_t.setSbysid(sbysDto.getIds().get(i));
                sbysDto_t.setLsid(sbyzDto_t.getSbyzid());
                sbyzDtos.add(sbyzDto_t);
                sbysDtos.add(sbysDto_t);
            }
            if (!sbysService.updateList(sbysDtos)){
                throw new BusinessException("message","更新设备验收失败!");
            }else {
                if (!dao.insertList(sbyzDtos)){
                    throw new BusinessException("message","新增验证数据失败");
                }
            }
            for (SbyzDto dto : sbyzDtos) {
                ShgcDto shgcDto_t = new ShgcDto();
                shgcDto_t.setExtend_1("[\"" + dto.getSbyzid() + "\"]");
                shgcDto_t.setShlb(AuditTypeEnum.AUDIT_DEVICE_TEST.getCode());
                shgcDto_t.setLrry(user.getYhid());
                shgcService.checkAndCommit(shgcDto_t, user);
            }
        }else {
            if (StringUtil.isNotBlank(sbyzDto.getYzrq())){
                SbyzDto sbyzDto1=getDtoById(sbyzDto.getSbyzid());
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                Date date1=format.parse(sbyzDto.getYzrq());
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(date1);
                calendar.add(Calendar.MONTH,Integer.parseInt(sbyzDto1.getYzzq()));
                date1=calendar.getTime();
                String xcyzsj=format.format(date1);
                SbysDto sbysDto1=new SbysDto();
                sbysDto1.setSbysid(sbyzDto1.getSbysid());
                sbysDto1.setXcyzsj(xcyzsj);
                sbysService.update(sbysDto1);
            }
           if (!update(sbyzDto)){
               throw new BusinessException("message","更新设备验证失败!");
           }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delSaveDeviceTest(SbyzDto sbyzDto,String flag) {
        List<SbyzDto> sbyzDtos=dao.getDtoList(sbyzDto);
        List<SbysDto> sbysDtos=new ArrayList<>();
        //如果删除前该设备就是在验证 则修改该设备为原设备状态 否则不修改设备验收各状态
        for (SbyzDto sbyzDto1:sbyzDtos){
            if (DeviceStateEnum.VERIFICATION.getCode().equals(sbyzDto1.getSbzt())){
                SbysDto sbysDto = new SbysDto();
                sbysDto.setSbysid(sbyzDto1.getSbysid());
                sbysDto.setSbzt(sbyzDto1.getYsbzt());
                sbysDto.setShzt(StatusEnum.CHECK_NO.getCode());
                sbysDto.setXgry(sbyzDto.getScry());
                sbysDtos.add(sbysDto);
            }
        }
        if (!CollectionUtils.isEmpty(sbysDtos)){
            sbysService.updateListZt(sbysDtos);
        }
        delete(sbyzDto);
        if ("delete".equals(flag)){
            shgcService.deleteByYwids(sbyzDto.getIds());
        }
        return true;
    }

    @Override
    public List<SbyzDto> getDepartmentList(SbyzDto sbyzDto) {
        return dao.getDepartmentList(sbyzDto);
    }

    @Override
    public List<SbyzDto> getGlryList() {
        return dao.getGlryList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        SbyzDto sbyzDto=(SbyzDto)baseModel;
        sbyzDto.setXgrymc(operator.getYhid());
        SbysDto sbysDto=new SbysDto();
        boolean isSuccess;
        try {
            isSuccess = checkingSaveEquipmentAcceptance(sbysDto,sbyzDto,null);
        } catch (ParseException e) {
            throw new BusinessException("msg","更新失败!");
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
            SbyzDto sbyzDto = new SbyzDto();
            sbyzDto.setSbyzid(shgcDto.getYwid());
            sbyzDto.setXgry(operator.getYhid());
            SbyzDto sbyzDto1 = getDtoById(sbyzDto.getSbyzid());
            SbysDto sbysDto=sbysService.getSbysDtoById(sbyzDto1.getSbysid());
            shgcDto.setSqbm(sbyzDto1.getSqbm());
            List<SpgwcyDto> spgwcyDtos = commonService.siftJgList(shgcDto.getSpgwcyDtos(),sbyzDto1.getSqbm());
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                sbyzDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                sbysDto.setShzt(StatusEnum.CHECK_TEST_CALIBRATION.getCode());
                sbysDto.setSbzt(sbyzDto1.getYsbzt());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(),
                                    xxglService.getMsg("ICOMM_SH00026"), xxglService.getMsg("ICOMM_SBYZ00001", operator.getZsxm(), shgcDto.getShlbmc(), sbysDto.getSbbh(), sbysDto.getSbmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                sbyzDto.setZt(StatusEnum.CHECK_PASS.getCode());
                sbysDto.setShzt(StatusEnum.CHECK_PASS.getCode());
                sbysDto.setSbzt("01");
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(),
                                    xxglService.getMsg("ICOMM_SH00006"), xxglService.getMsg("ICOMM_SBYZ00002",
                                            operator.getZsxm(), shgcDto.getShlbmc(), sbysDto.getSbbh(), sbysDto.getSbmc(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else {
                sbysDto.setShzt(StatusEnum.CHECK_TEST_SUBMIT.getCode());
                sbyzDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if ("1".equals(shgcDto.getXlcxh())&& shgcDto.getShxx()==null) {
                    sbysDto.setSbzt(DeviceStateEnum.VERIFICATION.getCode());
                    sbysDto.setYsbzt(sbyzDto1.getSbzt());
                }
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/auditpage/testandverifygoods/testandverifygoods&sbyzid=" + sbyzDto1.getSbyzid()
                                        + "&sbbh=" + sbyzDto1.getSbbh() + "&sbmc=" + sbyzDto1.getSbmc() + "&sblxmc=" + sbyzDto1.getSblxmc() + "&ggxh=" + sbyzDto1.getGgxh()
                                        + "&gdzcbh=" + sbyzDto1.getGdzcbh() + "&sbccbh=" + sbyzDto1.getSbccbh() + "&shlb=" + shgcDto.getShlb()
                                        + "&qwwcsj=" + sbyzDto1.getQwwcsj() + "&yjwcsj=" + sbyzDto1.getYjwcsj() + "&yzrq=" + sbyzDto1.getYzrq() + "&urlPrefix=" + urlPrefix, StandardCharsets.UTF_8);
                                List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                                btnJsonList.setTitle("小程序");
                                btnJsonList.setActionUrl(internalbtn);
                                btnJsonLists.add(btnJsonList);
                                talkUtil.sendCardDyxxMessageThread(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(),
                                        xxglService.getMsg("ICOMM_SH00003"),
                                        StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SBYZ00003"),
                                                operator.getZsxm(), shgcDto.getShlbmc(), sbysDto.getSbbh(), sbysDto.getSbmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
                                        btnJsonLists, "1");
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
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,sbysDto.getSbmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            sbysDto.setXgry(operator.getYhid());
            sbysService.update(sbysDto);
            update(sbyzDto);
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                SbyzDto sbyzDto = new SbyzDto();
                sbyzDto.setSbyzid(shgcDto.getYwid());
                sbyzDto.setXgry(operator.getYhid());
                SbyzDto sbyzDto1=getDtoById(sbyzDto.getSbyzid());
                SbysDto sbysDto=new SbysDto();
                sbysDto.setSbysid(sbyzDto1.getSbysid());
                sbyzDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                sbysDto.setShzt(StatusEnum.CHECK_SUBMIT.getCode());
                sbysDto.setSbzt("07");
                dao.update(sbyzDto);
                sbysService.update(sbysDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                //String fpid = shgcDto.getYwid();
                SbyzDto sbyzDto = new SbyzDto();
                sbyzDto.setSbyzid(shgcDto.getYwid());
                sbyzDto.setXgry(operator.getYhid());
                SbyzDto sbyzDto1=getDtoById(sbyzDto.getSbyzid());
                SbysDto sbysDto=new SbysDto();
                sbysDto.setSbysid(sbyzDto1.getSbysid());
                sbyzDto.setZt(StatusEnum.CHECK_NO.getCode());
                sbysDto.setShzt(StatusEnum.CHECK_NO.getCode());
                sbysDto.setSbzt(sbyzDto1.getYsbzt());
                dao.update(sbyzDto);
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
        SbyzDto sbyzDto = new SbyzDto();
        sbyzDto.setIds(ids);
        List<SbyzDto> dtoList = dao.getDtoList(sbyzDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(SbyzDto dto:dtoList){
                list.add(dto.getSbyzid());
            }
        }
        map.put("list",list);
        return map;
    }
}
