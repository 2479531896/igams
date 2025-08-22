package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
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
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.production.dao.entities.SbbfDto;
import com.matridx.igams.production.dao.entities.SbbfModel;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.dao.post.ISbbfDao;
import com.matridx.igams.production.service.svcinterface.ISbbfService;
import com.matridx.igams.production.service.svcinterface.ISbysService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
@Service
public class SbbfServiceImpl extends BaseBasicServiceImpl<SbbfDto, SbbfModel, ISbbfDao> implements ISbbfService, IAuditService {
    @Autowired
    ISbysService sbysService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ICommonService commonservice;
    @Autowired
    DingTalkUtil talkUtil;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Value("${matridx.systemflg.systemname:}")
    private String systemname;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IShxxService shxxService;
    @Autowired
    IDdfbsglService ddfbsglService;
    @Autowired
    IDdspglService ddspglService;
    @Value("${matridx.wechat.registerurl:}")
    private String registerurl;
    private final Logger log = LoggerFactory.getLogger(SbbfServiceImpl.class);
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean scrapSaveEquipmentAcceptance(SbysDto sbysDto,SbbfDto sbbfDto) throws BusinessException {
        boolean isSuccess;
        if (StringUtil.isBlank(sbbfDto.getSbbfid())){
            sbbfDto.setSbbfid(StringUtil.generateUUID());
            sbbfDto.setZt("00");
            sbysDto.setLsid(sbbfDto.getSbbfid());
            sbbfDto.setSbysid(sbysDto.getSbysid());
            if (insert(sbbfDto)){
                if (sbysService.update(sbysDto)){
                    isSuccess=true;
                }else {
                    throw new BusinessException("msg","修改设备验收失败!");
                }
            }else {
                throw new BusinessException("msg","新增报废设备失败!");
            }
        }
        else {
            if (update(sbbfDto)){
                isSuccess=true;
            }else {
                throw new BusinessException("msg","修改设备报废失败!");
            }
        }
        return isSuccess;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
        SbbfDto sbbfDto=(SbbfDto)baseModel;
        return update(sbbfDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
        String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
        String ICOMM_SBBF00004 = xxglService.getMsg("ICOMM_SBBF00004");
        for (ShgcDto shgcDto : shgcList) {
            SbbfDto sbbfDto=new SbbfDto();
            sbbfDto.setSbbfid(shgcDto.getYwid());
            SbbfDto sbbfDto1=getDtoById(sbbfDto.getSbbfid());
            SbysDto sbysDto=sbysService.getSbysDtoById(sbbfDto1.getSbysid());
            shgcDto.setSqbm(sbbfDto1.getSqbm());
            List<SpgwcyDto> spgwcyDtos = commonservice.siftJgList(shgcDto.getSpgwcyDtos(), sbbfDto1.getSqbm());
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {

                // 由于合同提交时直接发起了钉钉审批，所以审核不通过时直接采用钉钉的审批拒绝消息即可
                sbbfDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                sbysDto.setShzt(StatusEnum.CHECK_DISCARD_CALIBRATION.getCode());
                sbysDto.setSbzt(sbbfDto1.getYsbzt());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(), spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00026,
                                    xxglService.getMsg("ICOMM_SBBF00005", operator.getZsxm(), shgcDto.getShlbmc(),
                                            sbbfDto1.getSqrmc(), sbbfDto1.getBz(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                sbbfDto.setZt(StatusEnum.CHECK_PASS.getCode());
                sbysDto.setShzt(StatusEnum.CHECK_PASS.getCode());
                sbysDto.setSbzt("02");
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00006, StringUtil.replaceMsg(ICOMM_SBBF00004,
                                    operator.getZsxm(), shgcDto.getShlbmc(), sbbfDto1.getSqrmc(), sbbfDto1.getBz(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else {
                sbbfDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                sbysDto.setShzt(StatusEnum.CHECK_DISCARD_SUBMIT.getCode());
                if ("1".equals(shgcDto.getXlcxh())&& shgcDto.getShxx()==null){
                    sbysDto.setSbzt(DeviceStateEnum.SCRAP.getCode());
                    sbysDto.setYsbzt(sbbfDto1.getSbzt());
                }
                try {
                    Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "设备报废流程");//获取审批模板ID
                    String templateCode=(String) template.get("message");
                    User user=new User();
                    user.setYhid(shgcDto.getSqr());
                    user=commonservice.getUserInfoById(user);
                    if(user==null)
                        throw new BusinessException("ICOM99019","未获取到申请人信息！");
                    if(org.apache.commons.lang.StringUtils.isBlank(user.getDdid()))
                        throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
                    String userId=user.getDdid();
                    String dept=user.getJgid();
                    Map<String,String> map=new HashMap<>();
                    map.put("设备编号",sbbfDto1.getSbbh());
                    map.put("设备名称",sbbfDto1.getSbmc());
                    map.put("规格/型号",sbbfDto1.getGgxh());
                    map.put("设备序列号",StringUtil.isNotBlank(sbbfDto1.getXlh())?sbbfDto1.getXlh():"/");
                    map.put("是否为固定资产",StringUtil.isNotBlank(sbbfDto1.getGdzcbh())?"是":"否");
                    map.put("固定资产编号",StringUtil.isNotBlank(sbbfDto1.getGdzcbh())?sbbfDto1.getGdzcbh():"/");
//                    List<String> bms = new ArrayList<>();
//                    bms.add(sbbfDto1.getSqbm());
                    map.put("部门", sbbfDto1.getSqbmmc());
                    map.put("放置位置",StringUtil.isNotBlank(sbbfDto1.getFzwz())?sbbfDto1.getFzwz():"/");
                    map.put("报废原因",sbbfDto1.getBz());
                    Map<String,Object> t_map=talkUtil.createInstance(operator.getYhm(), templateCode, null, null, userId, dept, map,null,null);
                    String str=(String) t_map.get("message");
                    String status=(String) t_map.get("status");
                    if ("success".equals(status)){
                        @SuppressWarnings("unchecked")
                        Map<String,Object> result_map=JSON.parseObject(str,Map.class);
                        if(("0").equals(String.valueOf(result_map.get("errcode")))) {
                            //保存至钉钉分布式管理表(主站)
                            RestTemplate t_restTemplate = new RestTemplate();
                            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                            paramMap.add("ddslid", String.valueOf(result_map.get("process_instance_id")));
                            paramMap.add("fwqm", urlPrefix);
                            paramMap.add("cljg", "1");
                            paramMap.add("fwqmc", systemname);
                            paramMap.add("hddz",applicationurl);
                            paramMap.add("spywlx",shgcDto.getShlb());
                            paramMap.add("wbcxid",operator.getWbcxid());
                            //根据审批类型获取钉钉审批的业务类型，业务名称
                            JcsjDto jcsjDto_dd = new JcsjDto();
                            jcsjDto_dd.setCsdm(shgcDto.getAuditType());
                            jcsjDto_dd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
                            jcsjDto_dd = jcsjService.getDtoByCsdmAndJclb(jcsjDto_dd);
                            if(StringUtil.isBlank(jcsjDto_dd.getCsmc()) || StringUtil.isBlank(jcsjDto_dd.getCskz1())) {
                                throw new BusinessException("msg","请设置"+shgcDto.getShlbmc()+"的钉钉审批回调类型基础数据！");
                            }
                            paramMap.add("ywmc", operator.getZsxm()+"提交的"+jcsjDto_dd.getCsmc());
                            paramMap.add("ywlx", jcsjDto_dd.getCskz1());
                            //分布式保留
                            DdfbsglDto ddfbsglDto = new DdfbsglDto();
                            ddfbsglDto.setProcessinstanceid(String.valueOf(result_map.get("process_instance_id")));
                            ddfbsglDto.setFwqm(urlPrefix);
                            ddfbsglDto.setCljg("1");
                            ddfbsglDto.setFwqmc(systemname);
                            ddfbsglDto.setSpywlx(shgcDto.getShlb());
                            ddfbsglDto.setHddz(applicationurl);
                            ddfbsglDto.setYwlx(jcsjDto_dd.getCskz1());
                            ddfbsglDto.setYwmc(operator.getZsxm()+"提交的"+jcsjDto_dd.getCsmc());
                            ddfbsglDto.setWbcxid(operator.getWbcxid()); //存入外部程序id
                            boolean result_t = ddfbsglService.saveDistributedMsg(ddfbsglDto);
                            if(!result_t)
                                return false;
                            //主站保留一份
                            if(StringUtils.isNotBlank(registerurl)){
                                boolean result = t_restTemplate.postForObject(registerurl + "/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
                                if(!result)
                                    return false;
                            }
                            //若钉钉审批提交成功，则关联审批实例ID
                            sbbfDto.setDdslid(String.valueOf(result_map.get("process_instance_id")));
                        }else {
                            throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+t_map.get("message"));
                        }
                    }else {
                        throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+t_map.get("message"));
                    }
                }catch (BusinessException e) {
                    // TODO: handle exception
                    throw new BusinessException("msg",e.getMsg());
                }catch (Exception e) {
                    // TODO: handle exception
                    throw new BusinessException("msg","异常!异常信息:"+ e);
                }
                //发送钉钉消息--取消审核人员
                if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos()) ){
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(),
                                    xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,  sbbfDto1.getSbmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            sbysDto.setXgry(operator.getYhid());
            sbysService.update(sbysDto);
            update(sbbfDto);
        }

        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        if (auditParam.isCancelOpe()) {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                SbbfDto sbbfDto = new SbbfDto();
                sbbfDto.setSbbfid(shgcDto.getYwid());
                sbbfDto=getDtoById(sbbfDto.getSbbfid());
                sbbfDto.setXgry(operator.getYhid());
                sbbfDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                SbysDto sbysDto=new SbysDto();
                sbysDto.setShzt(StatusEnum.CHECK_SUBMIT.getCode());
                SbbfDto sbbfDto1=dao.getDtoById(sbbfDto.getSbbfid());
                sbysDto.setSbysid(sbbfDto1.getSbysid());
                sbysDto.setSbzt("09");
                sbysService.update(sbysDto);
                update(sbbfDto);
            }
        } else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                SbbfDto sbbfDto = new SbbfDto();
                sbbfDto.setSbbfid(shgcDto.getYwid());
                sbbfDto.setXgry(operator.getYhid());
                sbbfDto.setZt(StatusEnum.CHECK_NO.getCode());
                SbysDto sbysDto=new SbysDto();
                sbysDto.setShzt(StatusEnum.CHECK_NO.getCode());
                SbbfDto sbbfDto1=dao.getDtoById(sbbfDto.getSbbfid());
                sbysDto.setSbysid(sbbfDto1.getSbysid());
                sbysDto.setSbzt(sbbfDto1.getYsbzt());
                sbysDto.setShzt(StatusEnum.CHECK_NO.getCode());
                sbysService.update(sbysDto);
                update(sbbfDto);
                //OA取消审批的同时组织钉钉审批
                SbbfDto sbbfDto_t = getDtoById(sbbfDto.getSbbfid());
                if(sbbfDto_t!=null && org.apache.commons.lang3.StringUtils.isNotBlank(sbbfDto_t.getDdslid())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhid(), sbbfDto_t.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map=JSON.parseObject(success,Map.class);
                    boolean bo1= (boolean) result_map.get("success");
                    if(bo1){
                        dao.updateDdslidToNull(sbbfDto_t);
                    }else {
                        log.error("设备报废撤回审批异常："+JSON.toJSONString(cancelResult));
                    }

                }
            }

        }
        return true;
    }
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean callbackDeviceScarpAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException {
        SbbfDto sbbfDto=new SbbfDto();
        String result = obj.getString("result");// 正常结束时result为agree，拒绝时result为refuse，审批终止时没这个值，redirect转交
        String type = obj.getString("type");// 审批正常结束（同意或拒绝）的type为finish，审批终止的type为terminate
        String processInstanceId = obj.getString("processInstanceId");// 审批实例id
        String staffId = obj.getString("staffId");// 审批人钉钉ID
        String remark = obj.getString("remark");// 审核意见
        String content = obj.getString("content");//评论
        String title= obj.getString("title");
        String processCode=obj.getString("processCode");
        String finishTime = obj.getString("finishTime");// 审批完成时间
        String wbcxidString  = obj.getString("wbcxid"); //获取外部程序id
        log.error("回调参数获取---------result:"+result+",type:"+type+",processInstanceId:"+processInstanceId+",staffId:"+staffId+",remark:"+remark+",finishTime"+finishTime+",外部程序id"+wbcxidString);
        DdfbsglDto ddfbsglDto = new DdfbsglDto();
        ddfbsglDto.setProcessinstanceid(processInstanceId);
        ddfbsglDto=ddfbsglService.getDtoById(processInstanceId);
        DdspglDto ddspglDto=new DdspglDto();
        DdspglDto t_ddspglDto=new DdspglDto();
        t_ddspglDto.setProcessinstanceid(processInstanceId);
        t_ddspglDto.setType("finish");
        t_ddspglDto.setEventtype(DingTalkUtil.BPMS_TASK_CHANGE);
        List<DdspglDto> ddspgllist=ddspglService.getDtoList(t_ddspglDto);
        try {
            if(ddfbsglDto==null)
                throw new BusinessException("message","未获取到相应的钉钉分布式信息！");
            if(org.apache.commons.lang.StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {
                if(!CollectionUtils.isEmpty(ddspgllist)) {
                    ddspglDto=ddspgllist.get(0);
                }
            }
            sbbfDto.setDdslid(processInstanceId);
            // 根据钉钉审批实例ID查询关联请购单
            sbbfDto = dao.getDtoByDdslid(sbbfDto.getDdslid());
            // 若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if (sbbfDto != null) {
                //获取审批人信息
                User user=new User();
                user.setDdid(staffId);
                user.setWbcxid(wbcxidString);
                user = commonservice.getByddwbcxid(user);
                if (user == null)
                    return false;
                log.error("user-yhid:" + user.getYhid() + "---user-zsxm:" + user.getZsxm() + "----user-yhm"
                        + user.getYhm() + "---user-ddid" + user.getDdid());
                // 获取审批人角色信息
                List<SbbfDto> dd_sprjs = dao.getSprjsBySprid(user.getYhid());
                // 获取当前审核过程
                ShgcDto t_shgcDto = shgcService.getDtoByYwid(sbbfDto.getSbbfid());
                if (t_shgcDto != null) {
                    // 获取的审核流程列表

                    ShlcDto shlcParam = new ShlcDto();
                    shlcParam.setShid(t_shgcDto.getShid());
                    shlcParam.setGcid(t_shgcDto.getGcid());// 处理旧流程判断用
                    @SuppressWarnings("unused")
                    List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);

                    if (("start").equals(type)) {
                        //更新分布式管理表信息
                        DdfbsglDto t_ddfbsglDto=new DdfbsglDto();
                        t_ddfbsglDto.setProcessinstanceid(processInstanceId);
                        t_ddfbsglDto.setYwlx(processCode);
                        t_ddfbsglDto.setYwmc(title);
                        ddfbsglService.update(t_ddfbsglDto);
                    }
                    if (!CollectionUtils.isEmpty(dd_sprjs)) {
                        // 审批正常结束（同意或拒绝）
                        ShxxDto shxxDto = new ShxxDto();
                        shxxDto.setGcid(t_shgcDto.getGcid());
                        shxxDto.setLcxh(t_shgcDto.getXlcxh());
                        shxxDto.setShlb(t_shgcDto.getShlb());
                        shxxDto.setShyj(remark);
                        shxxDto.setAuditType(t_shgcDto.getShlb());
                        shxxDto.setYwid(sbbfDto.getSbbfid());
                        if (StringUtil.isNotBlank(finishTime)){
                            shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                        }
                        log.error("shxxDto-lcxh:" + t_shgcDto.getXlcxh() + " shxxDto-shlb:"
                                + t_shgcDto.getShlb() + " shxxDto-shyj:" + remark + " shxxDto-ywid:"
                                + sbbfDto.getSbbfid());
                        String lastlcxh = null;// 回退到指定流程

                        if (("finish").equals(type)) {
                            // 如果审批通过,同意
                            if (("agree").equals(result)) {
                                log.error("同意------");
                                shxxDto.setSftg("1");
                                if (org.apache.commons.lang.StringUtils.isBlank(t_shgcDto.getXlcxh()))
                                    throw new BusinessException("msg", "回调失败，现流程序号为空！");
                                lastlcxh = String.valueOf(Integer.parseInt(t_shgcDto.getXlcxh()) + 1);
                            }
                            // 如果审批未通过，拒绝
                            else if (("refuse").equals(result)) {
                                log.error("拒绝------");
                                shxxDto.setSftg("0");
                                lastlcxh = "1";
                                shxxDto.setThlcxh("-1");
                            }
                            // 如果审批被转发
                            else if (("redirect").equals(result)) {
                                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                                        .format(new Date(Long.parseLong(finishTime) / 1000));
                                log.error("DingTalkMaterPurchaseAudit(设备报废审批转发提醒)------转发人员:" + user.getZsxm()
                                        + ",人员ID:" + user.getYhm() + ",设备编号:" + sbbfDto.getSbbh() + ",单据ID:"
                                        + sbbfDto.getSbbfid() + ",转发时间:" + date);
                                return true;
                            }
                            // 调用审核方法
                            Map<String, List<String>> map = ToolUtil.reformRequest(request);
                            log.error("map:"+map);
                            List<String> list = new ArrayList<>();
                            list.add(sbbfDto.getSbbfid());
                            map.put("sbbfid", list);
                            for(int i=0;i<dd_sprjs.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(dd_sprjs.get(i).getSprjsid());
                                    user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
                                    shxxDto.setYwids(new ArrayList<>());
                                    shgcService.doManyBackAudit(shxxDto, user,request,lastlcxh,obj);
                                    //更新审批管理信息
                                    ddspglDto.setCljg("1");
                                    ddspglService.updatecljg(ddspglDto);
                                    break;
                                } catch (Exception e) {
                                    log.error("callbackHtglAduit-Exception:" + e.getMessage());
                                    t_map.put("ddspglid", ddspglDto.getDdspglid());
                                    t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());
                                    if(i==dd_sprjs.size()-1)
                                        throw new BusinessException("msg",e.getMessage());
                                }
                            }
                        }
                        // 撤销审批
                        else if (("terminate").equals(type)) {
                            shxxDto.setThlcxh(null);
                            shxxDto.setYwglmc(sbbfDto.getSbbh());
                            SbbfDto t_sbbfDto = new SbbfDto();
                            t_sbbfDto.setSbbfid(sbbfDto.getSbbfid());
                            dao.updateDdslidToNull(t_sbbfDto);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            // 调用审核方法
                            Map<String, List<String>> map = ToolUtil.reformRequest(request);
                            List<String> list = new ArrayList<>();
                            list.add(sbbfDto.getSbbfid());
                            map.put("sbbfid", list);
                            for(int i=0;i<dd_sprjs.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(dd_sprjs.get(i).getSprjsid());
                                    user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
                                    shxxDto.setYwids(new ArrayList<>());
                                    shgcService.terminateAudit(shxxDto, user,request,lastlcxh,obj);
                                    //更新审批管理信息
                                    ddspglDto.setCljg("1");
                                    ddspglService.updatecljg(ddspglDto);
                                    break;
                                } catch (Exception e) {
                                    t_map.put("ddspglid", ddspglDto.getDdspglid());
                                    t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());
                                    if(i==dd_sprjs.size()-1)
                                        throw new BusinessException("msg",e.toString());
                                }
                            }
                        }else if(("comment").equals(type)) {
                            //评论时保存评论信息，添加至审核信息表
                            ShgcDto shgc = shgcService.getDtoByYwid(shxxDto.getYwid());//获得已有审核过程
                            ShxxDto shxx = new ShxxDto();
                            String shxxid =StringUtil.generateUUID();
                            shxx.setShxxid(shxxid);
                            shxx.setSqr(shgc.getSqr());
                            shxx.setLcxh(null);
                            shxx.setShid(shgc.getShid());
                            shxx.setSftg("1");
                            shxx.setGcid(shgc.getGcid());
                            shxx.setYwid(shxxDto.getYwid());
                            shxx.setShyj("评论:"+content.substring(0, Math.min(content.length(), 6000)));//这里写死，备注钉钉审批被提交人员撤销了
                            shxx.setLrry(user.getYhid());
                            shxxService.insert(shxx);
                        }
                    }
                }else {
                    if(("comment").equals(type)) {
                        //评论时保存评论信息，添加至审核信息表
                        ShxxDto shxx = new ShxxDto();
                        String shxxid =StringUtil.generateUUID();
                        shxx.setShxxid(shxxid);
                        shxx.setSftg("1");
                        shxx.setYwid(sbbfDto.getSbbfid());
                        shxx.setShlb(AuditTypeEnum.AUDIT_DEVICE_SCRAP.getCode());
                        List<ShxxDto> shxxlist=shxxService.getShxxOrderByPass(shxx);
                        if(!CollectionUtils.isEmpty(shxxlist)) {
                            shxx.setShid(shxxlist.get(0).getShid());
                            shxx.setShyj("评论:"+content.substring(0, Math.min(content.length(), 6000)));//这里写死，备注钉钉审批被提交人员撤销了
                            shxx.setLrry(user.getYhid());
                            shxxService.insert(shxx);
                        }
                    }
                }
            }
        }catch(BusinessException e) {
            log.error(e.getMsg());
            throw new BusinessException("msg",e.getMsg());
        }catch (Exception e) {
            log.error(e.getMessage());
            throw new BusinessException("msg",e.toString());
        }

        return true;
    }

    @Override
    public List<SbbfDto> getDepartmentList() {
        return dao.getDepartmentList();
    }

    @Override
    public List<SbbfDto> getLeadList() {
        return dao.getLeadList();
    }

    @Override
    public boolean delDeviceScarp(SbbfDto sbbfDto,String flag) {
        List<SbbfDto> list=getDtoList(sbbfDto);
        List<SbysDto> sbysDtos=new ArrayList<>();
        delete(sbbfDto);
        //如果删除前该设备就是在报废 则修改该设备为原设备状态 否则不修改设备验收各状态
        for (SbbfDto sbbfDto1:list){
            if (DeviceStateEnum.SCRAP.getCode().equals(sbbfDto1.getSbzt())){
                SbysDto sbysDto = new SbysDto();
                sbysDto.setSbysid(sbbfDto1.getSbysid());
                sbysDto.setSbzt(sbbfDto1.getYsbzt());
                sbysDto.setShzt(StatusEnum.CHECK_NO.getCode());
                sbysDto.setXgry(sbbfDto.getScry());
                sbysDtos.add(sbysDto);
            }
        }
        if (!CollectionUtils.isEmpty(sbysDtos)){
            sbysService.updateListZt(sbysDtos);
        }
        if ("delete".equals(flag)){
            shgcService.deleteByYwids(sbbfDto.getIds());
        }
        return true;
    }

    @Override
    public List<SbbfDto> getPagedAuditDeviceScrap(SbbfDto sbbfDto) {
        // 获取人员ID和履历号
        List<SbbfDto> t_sbyzList= dao.getPagedAuditDeviceScrap(sbbfDto);

        if(CollectionUtils.isEmpty(t_sbyzList))
            return t_sbyzList;

        List<SbbfDto> sqList = dao.getAuditDeviceScrap(t_sbyzList);

        commonservice.setSqrxm(sqList);

        return sqList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
        return null;
    }

    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
        Map<String, Object> map =new HashMap<>();
        @SuppressWarnings("unchecked")
        List<String> ids = (List<String>)param.get("ywids");
        SbbfDto sbbfDto = new SbbfDto();
        sbbfDto.setIds(ids);
        List<SbbfDto> dtoList = dao.getDtoList(sbbfDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(SbbfDto dto:dtoList){
                list.add(dto.getSbbfid());
            }
        }
        map.put("list",list);
        return map;
    }

    /**
     * 设备报废列表（查询审核状态）
     */
    @Override
    public List<SbbfDto> getPagedDtoList(SbbfDto sbbfDto) {
        List<SbbfDto> list = dao.getPagedDtoList(sbbfDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_DEVICE_SCRAP.getCode(), "zt", "sbbfid",
                    new String[]{StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode()});
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }
    }
