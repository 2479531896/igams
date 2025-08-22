package com.matridx.igams.storehouse.service.impl;

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
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.production.dao.entities.SbfyDto;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.service.svcinterface.ISbfyService;
import com.matridx.igams.production.service.svcinterface.ISbysService;
import com.matridx.igams.storehouse.dao.entities.SbwxDto;
import com.matridx.igams.storehouse.dao.entities.SbwxModel;
import com.matridx.igams.storehouse.dao.post.ISbwxDao;
import com.matridx.igams.storehouse.service.svcinterface.ISbwxService;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SbwxServiceImpl extends BaseBasicServiceImpl<SbwxDto, SbwxModel, ISbwxDao> implements ISbwxService, IAuditService {
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
    @Value("${matridx.systemflg.systemname:}")
    private String systemname;
    @Value("${matridx.wechat.registerurl:}")
    private String registerurl;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    private IJcsjService jcsjService;
    @Autowired
    IDdfbsglService ddfbsglService;
    @Autowired
    IDdspglService ddspglService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IShxxService shxxService;
    @Autowired
    IDdxxglService ddxxglService;
    @Autowired
    ISbysService sbysService;
    @Autowired
    ISbfyService sbfyService;
    private final Logger log = LoggerFactory.getLogger(SbwxServiceImpl.class);
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
        SbwxDto wxbyDto = (SbwxDto)baseModel;
        wxbyDto.setXgry(operator.getYhid());
        return this.update(wxbyDto);
    }
    /**
     * 设备维修列表（查询审核状态）
     *
     * @param sbwxDto
     * @return
     */
    @Override
    public List<SbwxDto> getPagedDtoList(SbwxDto sbwxDto) {
        List<SbwxDto> list = dao.getPagedDtoList(sbwxDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_DEVICE_UPKEEP.getCode(), "zt", "sbwxid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
        String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
        for (ShgcDto shgcDto : shgcList) {
            SbwxDto sbwxDto = new SbwxDto();
            sbwxDto.setSbwxid(shgcDto.getYwid());
            sbwxDto.setXgry(operator.getYhid());
            SbwxDto sbwxDto_t = getDtoById(sbwxDto.getSbwxid());
            shgcDto.setSqbm(sbwxDto_t.getSqbm());
            List<SpgwcyDto> spgwcyDtos = commonService.siftJgList(shgcDto.getSpgwcyDtos(),sbwxDto_t.getSqbm());
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                //修改审核状态为不通过 设备状态为维修
                SbysDto sbysDto = new SbysDto();
                sbysDto.setSbysid(sbwxDto_t.getSbysid());
                sbysDto.setSbzt(sbwxDto_t.getYsbzt());
                sbysDto.setLsid(sbwxDto_t.getSbwxid());
                sbysDto.setShzt(StatusEnum.CHECK_UNPASS.getCode());
                sbysDto.setXgry(operator.getYhid());
                sbysService.update(sbysDto);
                sbwxDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00026,
                                    xxglService.getMsg("ICOMM_SBWX001", operator.getZsxm(), shgcDto.getShlbmc(), sbwxDto_t.getSqrmc(),
                                            sbwxDto_t.getSqbmmc(),
                                            sbwxDto_t.getSqrq(),
                                            sbwxDto_t.getSbmc()));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                if(StringUtil.isNotBlank(sbwxDto_t.getFyje())){
                    if(new BigDecimal("0").compareTo(new BigDecimal(sbwxDto_t.getFyje()))<0){
                        SbfyDto sbfyDto = new SbfyDto();
                        sbfyDto.setSbfyid(StringUtil.generateUUID());
                        sbfyDto.setSbysid(sbwxDto_t.getSbysid());
                        sbfyDto.setSj(sbwxDto_t.getWxsj());
                        sbfyDto.setBm(sbwxDto_t.getSqbm());
                        sbfyDto.setJe(sbwxDto_t.getFyje());
                        sbfyDto.setNr(sbwxDto_t.getPzcs());
                        JcsjDto jcsjDto = new JcsjDto();
                        jcsjDto.setCsdm("WX");
                        JcsjDto jcsjDtoT = jcsjService.getDto(jcsjDto);
                        if(jcsjDtoT!=null){
                            sbfyDto.setFylx(jcsjDtoT.getCsid());
                        }
                        boolean isSuccess = sbfyService.insert(sbfyDto);
                        if(!isSuccess){
                            throw new BusinessException("msg","新增维修费用失败");
                        }
                    }
                }
                SbysDto sbysDto = new SbysDto();
                sbysDto.setSbysid(sbwxDto_t.getSbysid());
                //类别为保养，更新byrq xcbysj
                if ("1".equals(sbwxDto_t.getLb())){
                    SbysDto dtoById = sbysService.getDto(sbysDto);
                    String byzq = dtoById.getByzq();
                    if (StringUtil.isNotBlank(byzq)){
                        Calendar instance = Calendar.getInstance();
                        instance.setTime(DateUtils.parseDate("yyyy-MM-dd",sbwxDto_t.getWxsj()));
                        instance.add(Calendar.MONTH,Integer.parseInt(byzq));
                        sbysDto.setByrq(sbwxDto_t.getWxsj());
                        sbysDto.setXcbysj(DateUtils.getCustomFomratCurrentDate(instance.getTime(),"yyyy-MM-dd"));
                    }
                }
                //修改审核状态为审核通过 设备状态为使用中
                sbysDto.setSbysid(sbwxDto_t.getSbysid());
                sbysDto.setSbzt(DeviceStateEnum.USEING.getCode());
                sbysDto.setYsbzt(sbwxDto_t.getSbzt());
                sbysDto.setLsid(sbwxDto_t.getSbwxid());
                sbysDto.setShzt(StatusEnum.CHECK_PASS.getCode());
                sbysDto.setXgry(operator.getYhid());
                sbysService.update(sbysDto);
                sbwxDto.setXgry(operator.getYhid());
                sbwxDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00006,
                                    xxglService.getMsg("ICOMM_SBWX002", operator.getZsxm(), shgcDto.getShlbmc(), sbwxDto_t.getSqrmc(),
                                            sbwxDto_t.getSqbmmc(),
                                            sbwxDto_t.getSqrq(),
                                            sbwxDto_t.getSbmc()));
                        }
                    }
                }
            }else {
                sbwxDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if ("1".equals(shgcDto.getXlcxh())&& shgcDto.getShxx()==null){
                    try {
                        //修改审核状态为审核中 设备状态为维修
                        SbysDto sbysDto = new SbysDto();
                        sbysDto.setSbysid(sbwxDto_t.getSbysid());
                        sbysDto.setYsbzt(sbwxDto_t.getSbzt());
                        sbysDto.setSbzt(DeviceStateEnum.UPKEEP.getCode());
                        sbysDto.setLsid(sbwxDto_t.getSbwxid());
                        sbysDto.setShzt(StatusEnum.CHECK_SERVICE_SUBMIT.getCode());
                        sbysDto.setXgry(operator.getYhid());
                        sbysService.update(sbysDto);
                        Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "设施设备维修流程");//获取审批模板ID
                        String templateCode=(String) template.get("message");
                        //获取申请人信息
                        User user=new User();
                        user.setYhid(shgcDto.getSqr());
                        user=commonService.getUserInfoById(user);
                        if(user==null)
                            throw new BusinessException("ICOM99019","未获取到申请人信息！");
                        if(org.apache.commons.lang.StringUtils.isBlank(user.getDdid()))
                            throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
                        String userid=user.getDdid();
                        String dept=user.getJgid();
                        Map<String,String> map=new HashMap<>();
                        map.put("设备编号", StringUtil.isNotBlank(sbwxDto_t.getSbbh())?sbwxDto_t.getSbbh():"无");
                        map.put("设施设备类型", sbwxDto_t.getSblxmc());
                        map.put("设施/设备名称", sbwxDto_t.getSbmc());
                        map.put("规格/型号", StringUtil.isNotBlank(sbwxDto_t.getGgxh())?sbwxDto_t.getGgxh():"无");
                        map.put("设备序列号", StringUtil.isNotBlank(sbwxDto_t.getXlh())?sbwxDto_t.getXlh():"无");
                        map.put("位置", sbwxDto_t.getSydd());
                        map.put("公司类别", systemname);
                        map.put("情况说明", sbwxDto_t.getGzms());
                        Map<String,Object> t_map=talkUtil.createInstance(operator.getYhm(), templateCode, null, null, userid, dept, map,null,null);
                        String str=(String) t_map.get("message");
                        String status=(String) t_map.get("status");
                        if("success".equals(status)) {
                            @SuppressWarnings("unchecked")
                            Map<String,Object> result_map= JSON.parseObject(str,Map.class);
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
                                paramMap.add("wbcxid", operator.getWbcxid());
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
                                ddfbsglDto.setWbcxid(operator.getWbcxid());
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
                                sbwxDto.setDdslid(String.valueOf(result_map.get("process_instance_id")));
                            }else {
                                throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+t_map.get("message"));
                            }
                        }else {
                            throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+t_map.get("message"));
                        }

                    } catch (BusinessException e) {
                        // TODO: handle exception
                        throw new BusinessException("msg",e.getMsg());
                    }catch (Exception e) {
                        // TODO: handle exception
                        throw new BusinessException("msg","异常!异常信息:"+ e);
                    }
                    sbwxDto.setTjsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
                }else {
                    // 发送钉钉消息
                    if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                        try {
                            for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                                if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                    talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                            spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00003"),
                                            xxglService.getMsg("ICOMM_SBWX003"
                                                    , operator.getZsxm(), shgcDto.getShlbmc(), sbwxDto_t.getSqrmc(),
                                                    sbwxDto_t.getSqbmmc(),
                                                    sbwxDto_t.getSqrq(),
                                                    sbwxDto_t.getSbmc()));
                                }
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                }
                //发送钉钉消息--取消审核人员
                if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos()) ){
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
                                    shgcDto.getNo_spgwcyDtos().get(i).getYhid(),xxglService.getMsg("ICOMM_SH00004"), xxglService.getMsg("ICOMM_SH00005",shgcDto.getShlbmc() , StringUtil.isNotBlank(sbwxDto_t.getSbbh())?sbwxDto_t.getSbbh():"无", DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(sbwxDto);
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
                String sbwxid = shgcDto.getYwid();
                SbwxDto sbwxDto = new SbwxDto();
                sbwxDto.setSbwxid(sbwxid);
                sbwxDto.setXgry(operator.getYhid());
                sbwxDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(sbwxDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String sbwxid = shgcDto.getYwid();
                SbwxDto sbwxDto = new SbwxDto();
                sbwxDto.setSbwxid(sbwxid);
                sbwxDto.setXgry(operator.getYhid());
                sbwxDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(sbwxDto);
                //OA取消审批的同时组织钉钉审批
                SbwxDto sbwxDto_t = getDtoById(sbwxDto.getSbwxid());
                SbysDto sbysDto = new SbysDto();
                sbysDto.setSbysid(sbwxDto_t.getSbysid());
                sbysDto.setSbzt(sbwxDto_t.getYsbzt());
                sbysDto.setLsid(sbwxDto_t.getSbwxid());
                sbysDto.setShzt(StatusEnum.CHECK_NO.getCode());
                sbysDto.setXgry(operator.getYhid());
                sbysService.update(sbysDto);
                if(StringUtils.isNotBlank(sbwxDto_t.getDdslid())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), sbwxDto_t.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map=JSON.parseObject(success,Map.class);
                    boolean bo1= (boolean) result_map.get("success");
                    if(bo1){
                        dao.updateDdslidToNull(sbwxDto_t);
                    }else {
                        log.error("维修保养审批撤回审批异常："+JSON.toJSONString(cancelResult));
                    }
                }
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
        SbwxDto sbwxDto = new SbwxDto();
        sbwxDto.setIds(ids);
        List<SbwxDto> dtoList = dao.getDtoList(sbwxDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(SbwxDto dto:dtoList){
                list.add(dto.getSbwxid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean callbackUpkeepDeviceAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException {
        SbwxDto sbwxDto = new SbwxDto();
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
            sbwxDto.setDdslid(processInstanceId);
            // 根据钉钉审批实例ID查询关联请购单
            sbwxDto = dao.getDtoByDdslid(sbwxDto.getDdslid());
            // 若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if (sbwxDto != null) {
                //获取审批人信息
                User user=new User();
                user.setDdid(staffId);
                user.setWbcxid(wbcxidString);
                user = commonService.getByddwbcxid(user);
                if (user == null)
                    return false;
                log.error("user-yhid:" + user.getYhid() + "---user-zsxm:" + user.getZsxm() + "----user-yhm"
                        + user.getYhm() + "---user-ddid" + user.getDdid());
                // 获取审批人角色信息
                List<SbwxDto> dd_sprjs = dao.getSprjsBySprid(user.getYhid());
                // 获取当前审核过程
                ShgcDto t_shgcDto = shgcService.getDtoByYwid(sbwxDto.getSbwxid());
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
                        shxxDto.setYwid(sbwxDto.getSbwxid());
                        if (StringUtil.isNotBlank(finishTime)){
                            shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                        }
                        log.error("shxxDto-lcxh:" + t_shgcDto.getXlcxh() + " shxxDto-shlb:"
                                + t_shgcDto.getShlb() + " shxxDto-shyj:" + remark + " shxxDto-ywid:"
                                + sbwxDto.getSbwxid());
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
                                log.error("callbackUpkeepDeviceAduit(钉钉营销合同审批转发提醒)------转发人员:" + user.getZsxm()
                                        + ",人员ID:" + user.getYhm() + ",设备编号:" + sbwxDto.getSbbh() + ",单据ID:"
                                        + sbwxDto.getSbwxid() + ",转发时间:" + date);
                                return true;
                            }
                            // 调用审核方法
                            Map<String, List<String>> map = ToolUtil.reformRequest(request);
                            log.error("map:"+map);
                            List<String> list = new ArrayList<>();
                            list.add(sbwxDto.getSbwxid());
                            map.put("sbwxid", list);
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
                                    log.error("callbackUpkeepDeviceAduit-Exception:" + e.getMessage());
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
                            shxxDto.setYwglmc(sbwxDto.getSbbh());
                            SbwxDto t_sbwxDto = new SbwxDto();
                            t_sbwxDto.setSbwxid(sbwxDto.getSbwxid());
                            dao.updateDdslidToNull(t_sbwxDto);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            // 调用审核方法
                            Map<String, List<String>> map = ToolUtil.reformRequest(request);
                            List<String> list = new ArrayList<>();
                            list.add(t_sbwxDto.getSbwxid());
                            map.put("sbwxid", list);
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
                        shxx.setYwid(sbwxDto.getSbwxid());
                        shxx.setShlb(AuditTypeEnum.AUDIT_CONTRACT.getCode());
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
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delUpkeepDevice(SbwxDto sbwxDto) throws BusinessException {
        List<SbwxDto> dtoList = dao.getDtoList(sbwxDto);
        List<SbysDto> sbysDtos=new ArrayList<>();
        //如果删除前该设备就是在维修保养 则修改该设备为原设备状态 否则不修改设备验收各状态
        for (SbwxDto sbwxDto1:dtoList){
            if (DeviceStateEnum.UPKEEP.getCode().equals(sbwxDto1.getSbzt())){
                SbysDto sbysDto = new SbysDto();
                sbysDto.setSbysid(sbwxDto1.getSbysid());
                sbysDto.setSbzt(sbwxDto1.getYsbzt());
                sbysDto.setShzt(StatusEnum.CHECK_NO.getCode());
                sbysDto.setXgry(sbwxDto.getScry());
                sbysDtos.add(sbysDto);
            }
        }
        if (!CollectionUtils.isEmpty(sbysDtos)){
            sbysService.updateListZt(sbysDtos);
        }
        boolean delete = delete(sbwxDto);
        if (!delete){
            throw new BusinessException("msg","删除设备维修记录失败！");
        }
        shgcService.deleteByYwids(sbwxDto.getIds());
        return true;
    }

    @Override
    public List<SbwxDto> getPagedAuditUpkeepDevice(SbwxDto sbwxDto) {
        // 获取人员ID和履历号
        List<SbwxDto> t_sbwxList = dao.getPagedAuditUpkeepDevice(sbwxDto);
        if (CollectionUtils.isEmpty(t_sbwxList))
            return t_sbwxList;
        List<SbwxDto> sqList = dao.getAuditListUpkeepDevice(t_sbwxList);
        commonService.setSqrxm(sqList);
        return sqList;
    }

    @Override
    public List<SbwxDto> getDepartmentList() {
        return dao.getDepartmentList();
    }

    @Override
    public List<SbwxDto> getGlryList() {
        return dao.getGlryList();
    }

    @Override
    public String getSbwxSerial(String prefix) {
        return dao.getSbwxSerial(prefix);
    }
}
