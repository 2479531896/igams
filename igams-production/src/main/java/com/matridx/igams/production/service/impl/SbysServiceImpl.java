package com.matridx.igams.production.service.impl;


import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
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
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.*;
import com.matridx.igams.production.dao.post.ISbysDao;
import com.matridx.igams.production.service.svcinterface.*;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import java.util.*;

@Service
public class SbysServiceImpl extends BaseBasicServiceImpl<SbysDto, SbysModel, ISbysDao> implements ISbysService, IAuditService {

    @Autowired
    ICommonService commonservice;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IHwxxService hwxxService;
    @Autowired
    ISbbhllService sbbhllService;
    @Autowired
    @Lazy
    ISbyjllService sbyjllService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IFjcfbService fjcfbService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    private final Logger log = LoggerFactory.getLogger(SbysServiceImpl.class);

    /**
     * 废弃按钮
     */
    public boolean discard(SbysDto sbysDto){
        return dao.discard(sbysDto);
    }

    /**
     * 新增
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertDto(SbysDto sbysDto){
        sbysDto.setSbysid(StringUtil.generateUUID());
        // HA年份（4位）日期（4位）流水号（2位）
        String prefix = "HA"+DateUtils.getCustomFomratCurrentDate("yyyy")+DateUtils.getCustomFomratCurrentDate("MMdd");
        String serial = dao.getSbysSerial(prefix);
        sbysDto.setJlbh(prefix+serial);
        boolean isSuccess = insert(sbysDto);
        //全部设备列表新增的没有货物id
        if (StringUtil.isNotBlank(sbysDto.getHwid())){
            HwxxDto hwxxDto = new HwxxDto();
            hwxxDto.setHwid(sbysDto.getHwid());
            hwxxDto.setSbysid(sbysDto.getSbysid());
            hwxxDto.setXgry(sbysDto.getLrry());
            hwxxService.updateForSbys(hwxxDto);
        }
        if(!CollectionUtils.isEmpty(sbysDto.getFjids())) {
            for (int i = 0; i < sbysDto.getFjids().size(); i++) {
                fjcfbService.save2RealFile(sbysDto.getFjids().get(i), sbysDto.getSbysid());
            }
        }
        return isSuccess;
    }

    /**
     * 审核列表
     */
    public List<SbysDto> getPagedAuditDevice(SbysDto sbysDto){
        // 获取人员ID和履历号
        List<SbysDto> t_sbyzList= dao.getPagedAuditDevice(sbysDto);

        if(CollectionUtils.isEmpty(t_sbyzList))
            return t_sbyzList;

        List<SbysDto> sqList = dao.getAuditListDevice(t_sbyzList);

        commonservice.setSqrxm(sqList);

        return sqList;
    }

    /**
     * 生成设备验收单号
     */
    public String getSbysdh(String str){
        String sbysdh="";
        String dh = dao.getSbysdh(str);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        String now=sdf.format(date);
        if(StringUtil.isNotBlank(dh)){
            sbysdh="YS-"+now+"-"+dh;
        }
        return sbysdh;

    }

    @Override
    public List<String> getDtoByGdzcbh(SbysDto sbysDto) {
        return dao.getDtoByGdzcbh(sbysDto);
    }

    @Override
    public List<SbysDto> getPagedEquipmentList(SbysDto sbysDto) {
        List<SbysDto> list = dao.getPagedEquipmentList(sbysDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_DEVICE_METERING.getCode(), "zt", "lsid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    @Override
    public List<SbysDto> getDepartmentList() {
        return dao.getDepartmentList();
    }

    @Override
    public SbysDto getSbysDtoById(String id) {
        return dao.getSbysDtoById(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAndSave(SbysDto sbysDto) throws BusinessException {
        if (!(sbysDto.getYgdzcbh().equals(sbysDto.getGdzcbh())&&sbysDto.getYsbbh().equals(sbysDto.getSbysdh()))){
            SbbhllDto sbbhllDto=new SbbhllDto();
            sbbhllDto.setLlid(StringUtil.generateUUID());
            sbbhllDto.setSbysid(sbysDto.getSbysid());
            sbbhllDto.setYsbbh(sbysDto.getYsbbh());
            sbbhllDto.setYgdzcbh(sbysDto.getYgdzcbh());
            if (!sbbhllService.insert(sbbhllDto)){
                throw new BusinessException("msg","新增设备编号履历失败!");
            }
        }else {
            sbysDto.setYgdzcbh(null);
            sbysDto.setYsbbh(null);
     }
        return update(sbysDto);

    }

    @Override
    public List<SbysDto> getDtoEquipmentList(SbysDto sbysDto) {
        return dao.getDtoEquipmentList(sbysDto);
    }

    @Override
    public boolean updateList(List<SbysDto> list) {
        return dao.updateList(list);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean scrapOrSoldSaveEquipmentAcceptance(SbysDto sbysDto) throws BusinessException {
        if (!CollectionUtils.isEmpty(sbysDto.getSbzts_t())){
            List<String> sbysDtoList=new ArrayList<>();
            for (int i=0;i<sbysDto.getSbzts_t().size();i++){
                if ("1".equals(sbysDto.getSbzts_t().get(i))){
                    sbysDtoList.add(sbysDto.getIds().get(i));
                }
            }
            if (!CollectionUtils.isEmpty(sbysDtoList)){
                List<String> ids=sbyjllService.selectNeedScrapList(sbysDtoList);
                SbyjllDto sbyjllDto=new SbyjllDto();
                sbyjllDto.setIds(ids);
                sbyjllDto.setSignal(sbysDto.getLx());
                    if (!CollectionUtils.isEmpty(ids)){
                    if (!sbyjllService.scrapOrSold(sbyjllDto)){
                        throw new BusinessException("msg","更新设备移交履历失败!");
                    }
                }
            }
            if (!dao.scrapOrSold(sbysDto)){
                throw new BusinessException("msg","更新设备验收失败!");
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePageEvent(SbysDto sbysDto) {
        boolean isSuccess = true;
        if(StringUtil.isNotBlank(sbysDto.getSbysid())){
            isSuccess = dao.updatePageEvent(sbysDto);
        }else{
            sbysDto.setSbysid(StringUtil.generateUUID());
            HwxxDto hwxxDto = new HwxxDto();
            hwxxDto.setHwid(sbysDto.getHwid());
            hwxxDto.setSbysid(sbysDto.getSbysid());
            isSuccess = hwxxService.update(hwxxDto);
            isSuccess = dao.insert(sbysDto)>0;
        }

        if (isSuccess){
            if(!CollectionUtils.isEmpty(sbysDto.getFjids())) {
                for (int i = 0; i < sbysDto.getFjids().size(); i++) {
                    fjcfbService.save2RealFile(sbysDto.getFjids().get(i), sbysDto.getSbysid());
                }
            }
        }
        return isSuccess;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean inactiveSaveEquipmentAcceptance(SbysDto sbysDto) throws BusinessException {
        sbysDto.setLx("inactive");
        if (!dao.scrapOrSold(sbysDto)){
            throw new BusinessException("msg","更新设备状态失败!");
        }
        List<SbyjllDto> sbyjllDtos=new ArrayList<>();
        for (int i=0;i<sbysDto.getIds().size();i++){
            SbyjllDto sbyjllDto=new SbyjllDto();
            sbyjllDto.setLlid(StringUtil.generateUUID());
            sbyjllDto.setSbysid(sbysDto.getIds().get(i));
            sbyjllDto.setZt("3");
            sbyjllDto.setQrzt("1");
            sbyjllDtos.add(sbyjllDto);
        }
        if (!CollectionUtils.isEmpty(sbyjllDtos)){
           if (!sbyjllService.insertList(sbyjllDtos)){
               throw new BusinessException("msg","新增设备移交履历失败!");
           }
        }
        return true;
    }

    @Override
    public List<SbysDto> getPagedMeteringList(SbysDto sbysDto) {
        return null;
    }

    @Override
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
        SbysDto sbysDto = (SbysDto)baseModel;
        return updatePageEvent(sbysDto);
    }

    @Override
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
//        String token = talkUtil.getToken();
        for (ShgcDto shgcDto : shgcList) {
            SbysDto sbysDto = new SbysDto();
            sbysDto.setSbysid(shgcDto.getYwid());
            sbysDto.setXgry(operator.getYhid());
            SbysDto sbysDto_t = getDto(sbysDto);
            List<SpgwcyDto> spgwcyDtos = shgcDto.getSpgwcyDtos();
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                sbysDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(),
                                    xxglService.getMsg("ICOMM_SH00026"), xxglService.getMsg("ICOMM_SH00001", operator.getZsxm(), shgcDto.getShlbmc(), sbysDto_t.getWlbm(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                sbysDto.setZt(StatusEnum.CHECK_PASS.getCode());
                HwxxDto hwxxDto=new HwxxDto();
                hwxxDto.setHwid(sbysDto_t.getHwid());
                hwxxDto.setZt("03");
                HwxxDto hwxxDto_t = hwxxService.getDtoById(sbysDto_t.getHwid());
                sbysDto.setSbmc(hwxxDto_t.getWlmc());
                sbysDto.setGgxh(hwxxDto_t.getGg());
                sbysDto.setGys(hwxxDto_t.getGys());
                sbysDto.setSccj(hwxxDto_t.getScs());
                sbysDto.setSbzt("0");
                if("0".equals(sbysDto_t.getYsjg())){
                    hwxxDto.setZjthsl(sbysDto_t.getYssl());
                }else{
                    Double reduce=Double.parseDouble(hwxxDto_t.getReduce());
                    Double zjthsl=Double.parseDouble(hwxxDto_t.getZjthsl());
                    hwxxDto.setSl(String.valueOf(reduce-zjthsl));
                }
                boolean b = hwxxService.update(hwxxDto);
                if(!b){
                    return false;
                }
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(),
                                    xxglService.getMsg("ICOMM_SH00006"), xxglService.getMsg("ICOMM_SH00016",
                                            operator.getZsxm(), shgcDto.getShlbmc(), sbysDto_t.getWlbm(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else {
                sbysDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(), spgwcyDto.getYhm(), spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00003"), xxglService.getMsg("ICOMM_SH00001", operator.getZsxm(), shgcDto.getShlbmc(), sbysDto_t.getWlbm(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
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
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(), shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,sbysDto_t.getWlbm(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(sbysDto);
        }
        return true;
    }

    @Override
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        // TODO Auto-generated method stub
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String sbysid = shgcDto.getYwid();
                SbysDto sbysDto = new SbysDto();
                sbysDto.setSbysid(sbysid);
                sbysDto.setXgry(operator.getYhid());
                sbysDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(sbysDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                //String fpid = shgcDto.getYwid();
                String sbysid = shgcDto.getYwid();
                SbysDto sbysDto = new SbysDto();
                sbysDto.setSbysid(sbysid);
                sbysDto.setXgry(operator.getYhid());
                sbysDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(sbysDto);
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
        SbysDto sbysDto = new SbysDto();
        sbysDto.setIds(ids);
        List<SbysDto> dtoList = dao.getDtoList(sbysDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(SbysDto dto:dtoList){
                list.add(dto.getSbysid());
            }
        }
        map.put("list",list);
        return map;
    }

    /**
     * 导出
     */
    public int getCountForSearchExp(SbysDto sbysDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(sbysDto);
    }

    /**
     * 根据搜索条件获取导出信息
     */
    public List<SbysDto> getListForSearchExp(Map<String, Object> params) {
        SbysDto sbysDto = (SbysDto) params.get("entryData");
        queryJoinFlagExport(params, sbysDto);
        return dao.getListForSearchExp(sbysDto);
    }

    /**
     * 根据选择信息获取导出信息

     */
    public List<SbysDto> getListForSelectExp(Map<String, Object> params) {
        SbysDto sbysDto = (SbysDto) params.get("entryData");
        queryJoinFlagExport(params, sbysDto);
        return dao.getListForSelectExp(sbysDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, SbysDto sbysDto) {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for (DcszDto dcszDto : choseList) {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs = sqlParam.toString();
        sbysDto.setSqlParam(sqlcs);
    }
    /**
     * 设备列表（查询审核状态）
     */
    @Override
    public List<SbysDto> getPagedDtoList(SbysDto sbysDto) {
        List<SbysDto> list = dao.getPagedDtoList(sbysDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_DEVICE_All.getCode(), "zt", "sbysid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    @Override
    public void updateTzs(List<SbysDto> sbysDtos) {
        dao.updateTzs(sbysDtos);
    }

    @Override
    public void updateTzsWithNull(List<SbysDto> sbysDtos) {
        dao.updateTzsWithNull(sbysDtos);
    }

    @Override
    public void updateSbzts(SbysDto sbysDto) {
        dao.updateSbzts(sbysDto);
    }

    @Override
    public List<SbysDto> getSbysDtoList(SbysDto sbysDto) {
        return dao.getSbysDtoList(sbysDto);
    }
	@Override
    public List<SbysDto> getPagedDeviceInfoForJl(SbysDto sbysDto) {
        return dao.getPagedDeviceInfoForJl(sbysDto);
    }

    @Override
    public List<SbysDto> getPagedDeviceInfoYZ(SbysDto sbysDto) {
        return dao.getPagedDeviceInfoYZ(sbysDto);
    }
	@Override
    public void updateForWlid(SbysDto sbysDto) {
        dao.updateForWlid(sbysDto);
    }
    /**
     * 设备计量验证保养定时任务
     */
    public void deviceScheduledTasks() {
        String internalbtn;
        List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
        OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
        btnJsonList.setTitle("小程序");
        SbysDto sbysDto =new SbysDto();
        sbysDto.setDsrwFlag("sbjl");
        List<SbysDto> sbjlGlryDtos=dao.selectAdministrators(sbysDto);
        List<SbysDto> sbjlFzrDtos=dao.selectLeaders(sbysDto);
        sbysDto.setDsrwFlag("sbyz");
        List<SbysDto> sbyzFzrDtos=dao.selectLeaders(sbysDto);
        List<SbysDto> sbyzGlryDtos=dao.selectAdministrators(sbysDto);
        sbysDto.setDsrwFlag("sbwx");
        List<SbysDto> sbwxGlryDtos=dao.selectAdministrators(sbysDto);
        List<SbysDto> sbwxFzrDtos=dao.selectLeaders(sbysDto);
        if(!CollectionUtils.isEmpty(sbjlGlryDtos)) {
            for (SbysDto sbjlGlryDto : sbjlGlryDtos) {
                internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/device/mydevice/mydevice&dsrwFlag=" + "sbjl" + "&urlPrefix=" + urlPrefix + "&dsrwGlry=" + sbjlGlryDto.getGlry(), StandardCharsets.UTF_8);
                btnJsonList.setActionUrl(internalbtn);
                btnJsonLists.add(btnJsonList);
                talkUtil.sendCardMessageThread(sbjlGlryDto.getYhm(), sbjlGlryDto.getGlry(),
                        xxglService.getMsg("ICOMM_SBJL00007"),
                        StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SBJL00008"), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
                        btnJsonLists, "1");
            }
        }
        if(!CollectionUtils.isEmpty(sbjlFzrDtos)) {
            for (SbysDto sbjlFzrDto : sbjlFzrDtos) {
                internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/device/mydevice/mydevice&dsrwFlag=" + "sbjl" + "&urlPrefix=" + urlPrefix + "&dsrwFzr=" + sbjlFzrDto.getBmsbfzr(), StandardCharsets.UTF_8);
                btnJsonList.setActionUrl(internalbtn);
                btnJsonLists.add(btnJsonList);
                talkUtil.sendCardMessageThread(sbjlFzrDto.getYhm(), sbjlFzrDto.getBmsbfzr(),
                        xxglService.getMsg("ICOMM_SBJL00007"),
                        StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SBJL00008"), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
                        btnJsonLists, "1");
            }
        }
        if(!CollectionUtils.isEmpty(sbyzFzrDtos)) {
            for (SbysDto sbyzFzrDto : sbyzFzrDtos) {
                internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/device/mydevice/mydevice&dsrwFlag=" + "sbyz" + "&urlPrefix=" + urlPrefix + "&dsrwFzr=" + sbyzFzrDto.getBmsbfzr(), StandardCharsets.UTF_8);
                btnJsonList.setActionUrl(internalbtn);
                btnJsonLists.add(btnJsonList);
                talkUtil.sendCardMessageThread(sbyzFzrDto.getYhm(), sbyzFzrDto.getBmsbfzr(),
                        xxglService.getMsg("ICOMM_SBYZ00004"),
                        StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SBYZ00005"), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
                        btnJsonLists, "1");
            }
        }
        if(!CollectionUtils.isEmpty(sbyzGlryDtos)) {
            for (SbysDto sbyzGlryDto : sbyzGlryDtos) {
                internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/device/mydevice/mydevice&dsrwFlag=" + "sbyz" + "&urlPrefix=" + urlPrefix + "&dsrwGlry=" + sbyzGlryDto.getGlry(), StandardCharsets.UTF_8);
                btnJsonList.setActionUrl(internalbtn);
                btnJsonLists.add(btnJsonList);
                talkUtil.sendCardMessageThread(sbyzGlryDto.getYhm(), sbyzGlryDto.getBmsbfzr(),
                        xxglService.getMsg("ICOMM_SBYZ00004"),
                        StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SBYZ00005"), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
                        btnJsonLists, "1");
            }
        }
        if(!CollectionUtils.isEmpty(sbwxGlryDtos)) {
            for (SbysDto sbwxGlryDto : sbwxGlryDtos) {
                internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/device/mydevice/mydevice&dsrwFlag=" + "sbwx" + "&urlPrefix=" + urlPrefix + "&dsrwGlry=" + sbwxGlryDto.getGlry(), StandardCharsets.UTF_8);
                btnJsonList.setActionUrl(internalbtn);
                btnJsonLists.add(btnJsonList);
                talkUtil.sendCardMessageThread(sbwxGlryDto.getYhm(), sbwxGlryDto.getBmsbfzr(),
                        xxglService.getMsg("ICOMM_SBWX00004"),
                        StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SBWX00005"), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
                        btnJsonLists, "1");
            }
        }
        if(!CollectionUtils.isEmpty(sbwxFzrDtos)) {
            for (SbysDto sbwxFzrDto : sbwxFzrDtos) {
                internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/device/mydevice/mydevice&dsrwFlag=" + "sbwx" + "&urlPrefix=" + urlPrefix + "&dsrwFzr=" + sbwxFzrDto.getBmsbfzr(), StandardCharsets.UTF_8);
                btnJsonList.setActionUrl(internalbtn);
                btnJsonLists.add(btnJsonList);
                talkUtil.sendCardMessageThread(sbwxFzrDto.getYhm(), sbwxFzrDto.getBmsbfzr(),
                        xxglService.getMsg("ICOMM_SBWX00004"),
                        StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SBWX00005"), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
                        btnJsonLists, "1");
            }
        }
    }

    @Override
    public void updateListZt(List<SbysDto> sbysDtos) {
        dao.updateListZt(sbysDtos);
    }
}
