package com.matridx.igams.wechat.service.impl;


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
import com.matridx.igams.common.enums.CommonCreditEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.wechat.dao.entities.FksqDto;
import com.matridx.igams.wechat.dao.entities.FksqModel;
import com.matridx.igams.wechat.dao.entities.KpsqDto;
import com.matridx.igams.wechat.dao.entities.SwyszkDto;
import com.matridx.igams.wechat.dao.post.IFksqDao;
import com.matridx.igams.wechat.service.svcinterface.IFksqService;
import com.matridx.igams.wechat.service.svcinterface.ISwyszkService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import io.micrometer.core.instrument.util.StringUtils;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FksqServiceImpl extends BaseBasicServiceImpl<FksqDto, FksqModel, IFksqDao> implements IFksqService, IAuditService {

    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ICommonService commonService;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.wechat.registerurl:}")
    private String registerurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IDdfbsglService ddfbsglService;
    @Autowired
    IDdspglService ddspglService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IShxxService shxxService;
    @Autowired
    private IJcsjService jcsjService;
    @Autowired
    private ISwyszkService swyszkService;
    @Autowired
    IFjcfbService fjcfbService;
    private Logger log = LoggerFactory.getLogger(FksqServiceImpl.class);

    @Override
    public List<FksqDto> getPagedDtoList(FksqDto fksqDto) {
        List<FksqDto> list = dao.getPagedDtoList(fksqDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_BUSINESS_PAYMENT_APPLICATION.getCode(), "zt", "fksqid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 付款申请保存
     *
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean paymentSaveReceivableCredit(FksqDto fksqDto){
        fksqDto.setFksqid(StringUtil.generateUUID());
        if(fksqDto.getFjids()!=null && fksqDto.getFjids().size() > 0){
            for (int i = 0; i < fksqDto.getFjids().size(); i++) {
                boolean saveFile = fjcfbService.save2RealFile(fksqDto.getFjids().get(i),fksqDto.getFksqid());
                if(!saveFile)
                    return false;
            }
        }
        fksqDto.setYwlx(CommonCreditEnum.SW_YSZK_FK.getCode());
        fksqDto.setZt(StatusEnum.CHECK_NO.getCode());
        int insert = dao.insert(fksqDto);
        return insert != 0;
    }

    @Override
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
//        String token = talkUtil.getToken();
        for (int j=0;j<shgcList.size();j++) {
            FksqDto fksqDto=new FksqDto();
            fksqDto.setFksqid(shgcList.get(j).getYwid());
            fksqDto.setXgry(operator.getYhid());
            FksqDto fksqDto_t = getDto(fksqDto);
            List<SpgwcyDto> spgwcyDtos = shgcList.get(j).getSpgwcyDtos();
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcList.get(j).getAuditState())) {
                fksqDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (spgwcyDtos != null && spgwcyDtos.size() > 0) {
                    for (int i = 0; i < spgwcyDtos.size(); i++) {
                        if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcList.get(j).getShlb(),spgwcyDtos.get(i).getYhid(),spgwcyDtos.get(i).getYhm(),
                                    spgwcyDtos.get(i).getYhid(),
                                    xxglService.getMsg("ICOMM_SH00026"),xxglService.getMsg("ICOMM_SH00001",operator.getZsxm(),shgcList.get(j).getShlbmc() ,fksqDto_t.getFkfsmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                if(!StatusEnum.CHECK_UNPASS.getCode().equals(fksqDto_t.getZt())){
                    SwyszkDto swyszkDto=new SwyszkDto();
                    swyszkDto.setFkslbj("0");
                    swyszkDto.setYszkid(fksqDto_t.getYwid());
                    swyszkDto.setXgry(operator.getYhid());
                    swyszkService.updateAmount(swyszkDto);
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcList.get(j).getAuditState())) {
                fksqDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if (spgwcyDtos != null && spgwcyDtos.size() > 0) {
                    int size = spgwcyDtos.size();
                    for (int i = 0; i < size; i++) {
                        if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcList.get(j).getShlb(),spgwcyDtos.get(i).getYhid(),spgwcyDtos.get(i).getYhm(), spgwcyDtos.get(i).getYhid(),
                                    xxglService.getMsg("ICOMM_SH00006"),xxglService.getMsg("ICOMM_SH00016",
                                            operator.getZsxm(),shgcList.get(j).getShlbmc() ,fksqDto_t.getFkfsmc(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                List<FksqDto> dtoList = dao.getDtoList(fksqDto_t);
                SwyszkDto swyszkDto=swyszkService.getDtoById(fksqDto_t.getYwid());
                BigDecimal fkje=new BigDecimal("0");
                BigDecimal whkje=new BigDecimal("0");
                BigDecimal wfkje=new BigDecimal("0");
                BigDecimal fkzje=new BigDecimal("0");
                if(StringUtil.isNotBlank(swyszkDto.getWhkje())){
                    whkje=new BigDecimal(swyszkDto.getWhkje());
                }
                if(StringUtil.isNotBlank(swyszkDto.getWfkje())){
                    wfkje=new BigDecimal(swyszkDto.getWfkje());
                }
                if(dtoList!=null&&dtoList.size()>0){
                    for(FksqDto dto:dtoList){
                        if(StatusEnum.CHECK_PASS.getCode().equals(dto.getZt())&&StringUtil.isNotBlank(dto.getFkzje())){
                            fkzje=new BigDecimal(dto.getFkzje());
                            fkje=fkje.add(fkzje);
                        }
                    }
                }
                if(StringUtil.isNotBlank(fksqDto_t.getFkzje())){
                    fkzje=new BigDecimal(fksqDto_t.getFkzje());
                    fkje=fkje.add(fkzje);
                }
                wfkje=wfkje.subtract(fkzje);
                SwyszkDto swyszkDto_t=new SwyszkDto();
                swyszkDto_t.setYszkid(fksqDto_t.getYwid());
                swyszkDto_t.setXgry(operator.getYhid());
                swyszkDto_t.setFkje(String.valueOf(fkje));
                swyszkDto_t.setWfkje(String.valueOf(wfkje));
                if(whkje.compareTo(BigDecimal.ZERO)==0&&wfkje.compareTo(BigDecimal.ZERO)==0){
                    swyszkDto_t.setSfjq("1");
                }
                boolean update = swyszkService.update(swyszkDto_t);
                if(!update){
                    return false;
                }
                fksqDto.setFkrq(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
                if(!StatusEnum.CHECK_PASS.getCode().equals(fksqDto_t.getZt())){
                    SwyszkDto t_swyszkDto=new SwyszkDto();
                    t_swyszkDto.setYfkslbj("1");
                    t_swyszkDto.setYszkid(fksqDto_t.getYwid());
                    t_swyszkDto.setXgry(operator.getYhid());
                    swyszkService.updateAmount(t_swyszkDto);
                }
            }else {
                fksqDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if (shgcList.get(j).getXlcxh().equals("1")){
                    try{
                        Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "付款申请");//获取审批模板ID
                        String templateCode=(String) template.get("message");
                        //获取申请人信息(付款申请应该为采购部门)
                        User user=new User();
                        user.setYhid(operator.getYhid());
                        user=commonService.getUserInfoById(user);
                        if(user==null)
                            throw new BusinessException("ICOM99019","未获取到申请人信息！");
                        if(org.apache.commons.lang.StringUtils.isBlank(user.getDdid()))
                            throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
                        String userid=user.getDdid();
                        String dept=user.getJgid ();
                        Map<String,String> map= new HashMap<>();
                        Map<String,String> t_map= new HashMap<>();
                        List<Map<String,String>> mxlist= new ArrayList<>();
                        map.put("费用归属",fksqDto_t.getFygsmc());
                        map.put("用款部门2",fksqDto_t.getSqbmmc());
                        map.put("用款部门","["+fksqDto_t.getSqbm()+"]");
                        map.put("付款事由",fksqDto_t.getFksy());
                        map.put("付款总额",fksqDto_t.getFkzje());
                        t_map.put("付款金额（元）",fksqDto_t.getFkzje());
                        t_map.put("付款方式",fksqDto_t.getFkfsmc());
                        t_map.put("付款方",fksqDto_t.getFkfmc());
                        t_map.put("最晚支付日期",fksqDto_t.getZwzfrq());
                        t_map.put("支付对象",fksqDto_t.getZfdxmc());
                        t_map.put("支付方开户行",fksqDto_t.getZffkhh());
                        t_map.put("支付方银行账户",fksqDto_t.getZffyhzh());
                        log.error("钉钉商务付款申请参数获取--用款部门:"+fksqDto_t.getSqbmmc()+",付款总额:"+fksqDto_t.getFkzje()+",付款金额（元）:"+fksqDto_t.getFkzje()
                                +",付款方式:"+fksqDto_t.getFkfsmc()+",付款方:"+fksqDto_t.getFkfmc()+",最晚支付日期:"+fksqDto_t.getZwzfrq()+",支付对象:"+fksqDto_t.getZfdx());
                        t_map.put("说明","附件详情:"+applicationurl+urlPrefix+"/ws/application/getPaymentApplicationInfo?fksqid="+fksqDto_t.getFksqid());
                        mxlist.add(t_map);
                        Map<String,Object> result=talkUtil.createInstance(operator.getYhm(), templateCode, null, null, userid, dept, map,mxlist,null);
                        String str=(String) result.get("message");
                        String status=(String) result.get("status");
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
                                paramMap.add("fwqmc", "杰毅医检");
                                paramMap.add("hddz",applicationurl);
                                paramMap.add("spywlx",shgcList.get(j).getShlb());

                                //根据审批类型获取钉钉审批的业务类型，业务名称
                                JcsjDto jcsjDto_dd = new JcsjDto();
                                jcsjDto_dd.setCsdm(shgcList.get(j).getAuditType());
                                jcsjDto_dd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
                                jcsjDto_dd = jcsjService.getDtoByCsdmAndJclb(jcsjDto_dd);
                                if(StringUtil.isBlank(jcsjDto_dd.getCsmc()) || StringUtil.isBlank(jcsjDto_dd.getCskz1())) {
                                    throw new BusinessException("msg","请设置"+shgcList.get(j).getShlbmc()+"的钉钉审批回调类型基础数据！");
                                }
                                paramMap.add("ywmc", operator.getZsxm()+"提交的"+jcsjDto_dd.getCsmc());
                                paramMap.add("ywlx", jcsjDto_dd.getCskz1());
                                paramMap.add("wbcxid", operator.getWbcxid());
                                //分布式保留一份
                                boolean t_result = t_restTemplate.postForObject(applicationurl + "/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
                                if (!t_result)
                                    return false;
                                //主站保留一份
                                if (StringUtils.isNotBlank(registerurl)) {
                                    boolean result_t = t_restTemplate.postForObject(registerurl + "/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
                                    if (!result_t)
                                        return false;
                                }
                                //若钉钉审批提交成功，则关联审批实例ID
                                fksqDto.setDdslid(String.valueOf(result_map.get("process_instance_id")));
                                dao.update(fksqDto);
                            }else {
                                throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+result.get("message"));
                            }
                        }else {
                            throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+result.get("message"));
                        }
                    }catch(BusinessException e){
                        throw new BusinessException("msg",e.getMsg());
                    }catch (Exception e) {
                        // TODO: handle exception
                        throw new BusinessException("msg","异常!异常信息:"+e.toString());
                    }
                    SwyszkDto swyszkDto=new SwyszkDto();
                    swyszkDto.setFkslbj("1");
                    swyszkDto.setYszkid(fksqDto_t.getYwid());
                    swyszkDto.setXgry(operator.getYhid());
                    swyszkService.updateAmount(swyszkDto);
                }else{
                    // 发送钉钉消息
                    if (spgwcyDtos != null && spgwcyDtos.size() > 0) {
                        try {
                            int size = spgwcyDtos.size();
                            for (int i = 0; i < size; i++) {
                                if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                                    talkUtil.sendWorkDyxxMessage(shgcList.get(j).getShlb(),spgwcyDtos.get(i).getYhid(),shgcList.get(j).getSpgwcyDtos().get(i).getYhm(),
                                            shgcList.get(j).getSpgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00003"),StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SH00001"),
                                                    operator.getZsxm(), shgcList.get(j).getShlbmc(),fksqDto_t.getFkfsmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                //发送钉钉消息--取消审核人员
                if(shgcList.get(j).getNo_spgwcyDtos() != null && shgcList.get(j).getNo_spgwcyDtos().size() > 0){
                    int size = shgcList.get(j).getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcList.get(j).getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcList.get(j).getShlb(),spgwcyDtos.get(i).getYhid(),shgcList.get(j).getNo_spgwcyDtos().get(i).getYhm(),
                                    shgcList.get(j).getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcList.get(j).getShlbmc() ,fksqDto_t.getFkfsmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            dao.update(fksqDto);
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
                String fksqid = shgcDto.getYwid();
                FksqDto fksqDto=new FksqDto();
                fksqDto.setXgry(operator.getYhid());
                fksqDto.setFksqid(fksqid);
                fksqDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(fksqDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String fksqid = shgcDto.getYwid();
                FksqDto fksqDto=new FksqDto();
                fksqDto.setXgry(operator.getYhid());
                fksqDto.setFksqid(fksqid);
                fksqDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(fksqDto);
                //OA取消审批的同时组织钉钉审批
                FksqDto fksqDto_t=dao.getDtoById(fksqid);
                if(fksqDto_t!=null && StringUtils.isNotBlank(fksqDto_t.getDdslid())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), fksqDto_t.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map= JSON.parseObject(success,Map.class);
                    Boolean bo1= (boolean) result_map.get("success");
                    if(bo1)
                        dao.updateDdslidToNull(fksqDto_t);
                }
                SwyszkDto swyszkDto=new SwyszkDto();
                swyszkDto.setFkslbj("0");
                swyszkDto.setYszkid(fksqDto_t.getYwid());
                swyszkDto.setXgry(operator.getYhid());
                swyszkService.updateAmount(swyszkDto);
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
        List<String> ids = (List<String>)param.get("ywids");
        FksqDto fksqDto=new FksqDto();
        fksqDto.setIds(ids);
        List<FksqDto> dtoList = dao.getDtoList(fksqDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(FksqDto dto:dtoList){
                list.add(dto.getFksqid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean paymentApplicationCallback(JSONObject obj, HttpServletRequest request, Map<String,Object> t_map) throws BusinessException{
        String result=obj.getString("result");//正常结束时result为agree，拒绝时result为refuse，审批终止时没这个值，redirect转交
        String type=obj.getString("type");//审批正常结束（同意或拒绝）的type为finish，审批终止的type为terminate
        String processInstanceId=obj.getString("processInstanceId");//审批实例id
        String staffId=obj.getString("staffId");//审批人钉钉ID
        String remark=obj.getString("remark");//审核意见
        String content = obj.getString("content");//评论
        String finishTime=obj.getString("finishTime");//审批完成时间
        String title= obj.getString("title");
        String processCode=obj.getString("processCode");
        String ddspbcbj=request.getParameter("ddspbcbj");
        String wbcxid=obj.getString("wbcxid");
        log.error("回调参数获取---------result:"+result+",type:"+type+",processInstanceId:"+processInstanceId+",staffId:"+staffId+",remark:"+remark+",finishTime"+finishTime);
        //分布式服务器保存钉钉审批信息
        DdfbsglDto ddfbsglDto=new DdfbsglDto();
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
                if("1".equals(ddspbcbj)) {
                    ddspglDto=ddspglService.insertInfo(obj);
                }else {
                    //考虑190调190时，因为接收回调接口中已经保存一份消息数据，则这里直接取最新的那条，若没有则添加这条传递过来的消息
                    if(ddspgllist!=null && ddspgllist.size()>0) {
                        ddspglDto=ddspgllist.get(0);
                    }else{
                        ddspglDto=ddspglService.insertInfo(obj);
                    }
                }
            }

            //根据钉钉审批实例ID查询
            FksqDto fksqDto=dao.getDtoByDdslid(processInstanceId);
            //若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if(fksqDto!=null) {
                User t_user=new User();
                t_user.setDdid(staffId);
                t_user.setWbcxid(wbcxid);
                //获取审批人信息
                t_user=commonService.getByddwbcxid(t_user);
                if(t_user==null)
                    return false;
                User user=new User();
                user.setYhid(t_user.getYhid());
                user.setZsxm(t_user.getZsxm());
                user.setYhm(t_user.getYhm());
                user.setDdid(t_user.getDdid());
                //获取审批人角色信息
                List<FksqDto> dd_sprjs=dao.getSprjsBySprid(t_user.getYhid());
                // 获取当前审核过程
                ShgcDto t_shgcDto = shgcService.getDtoByYwid(fksqDto.getFksqid());
                if(t_shgcDto!=null) {
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
                    if(dd_sprjs!=null && dd_sprjs.size()>0) {
                        //审批正常结束（同意或拒绝）
                        ShxxDto shxxDto=new ShxxDto();
                        shxxDto.setLcxh(t_shgcDto.getXlcxh());
                        shxxDto.setShlb(t_shgcDto.getShlb());
                        shxxDto.setShyj(remark);
                        shxxDto.setYwid(fksqDto.getFksqid());
                        shxxDto.setGcid(t_shgcDto.getGcid());
                        if (StringUtil.isNotBlank(finishTime)){
                            shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                        }
                        String lastlcxh=null;//回退到指定流程

                        if(("finish").equals(type)) {
                            //如果审批通过,同意
                            if(("agree").equals(result)) {
                                log.error("同意------");
                                shxxDto.setSftg("1");
                                if(org.apache.commons.lang3.StringUtils.isBlank(t_shgcDto.getXlcxh()))
                                    throw new BusinessException("ICOM99019","现流程序号为空");
                                lastlcxh=String.valueOf(Integer.parseInt(t_shgcDto.getXlcxh())+1);
                            }
                            //如果审批未通过，拒绝
                            else if(("refuse").equals(result)) {
                                log.error("拒绝------");
                                shxxDto.setSftg("0");
                                shxxDto.setThlcxh(null);
                            }
                            //如果审批被转发
                            else if(("redirect").equals(result)) {
                                String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(finishTime) / 1000));
                                log.error("(钉钉审批转发提醒)------转发人员:"+t_user.getZsxm()+",人员ID:"+t_user.getYhid()+",转发时间:"+date);
                                return true;
                            }
                            //调用审核方法
                            Map<String, List<String>> map= ToolUtil.reformRequest(request);
                            log.error("map:"+map);
                            List<String> list= new ArrayList<>();
                            list.add(fksqDto.getFksqid());
                            map.put("fksqid", list);
                            //若一个用户多个角色导致审核异常时
                            for(int i=0;i<dd_sprjs.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(dd_sprjs.get(i).getSprjsid());
                                    user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
                                    shxxDto.setYwids(new ArrayList<>());
                                    if(("refuse").equals(result)){
                                        shgcService.terminateAudit(shxxDto, user,request,lastlcxh,obj);
                                    }else{
                                        shgcService.doManyBackAudit(shxxDto, user,request,lastlcxh,obj);
                                    }
                                    //更新审批管理信息
                                    ddspglDto.setCljg("1");
                                    ddspglService.updatecljg(ddspglDto);
                                    break;
                                } catch (Exception e) {
                                    log.error("钉钉审批回调-Exception:" + e.getMessage());
                                    t_map.put("ddspglid", ddspglDto.getDdspglid());
                                    t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());

                                    if(i==dd_sprjs.size()-1)
                                        throw new BusinessException("ICOM99019",e.getMessage());
                                }
                            }
                        }
                        //撤销审批
                        else if(("terminate").equals(type)) {
                            shxxDto.setThlcxh(null);
                            shxxDto.setYwglmc(fksqDto.getFksqid());
                            FksqDto fksqDto_t=new FksqDto();
                            fksqDto_t.setFksqid(fksqDto.getFksqid());
                            dao.updateDdslidToNull(fksqDto_t);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            //调用审核方法
                            Map<String, List<String>> map=ToolUtil.reformRequest(request);
                            List<String> list= new ArrayList<>();
                            list.add(fksqDto.getFksqid());
                            map.put("fksqid", list);
                            for(int i=0;i<dd_sprjs.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(dd_sprjs.get(i).getSprjsid());
                                    user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
                                    shxxDto.setYwids(new ArrayList<>());
                                    shgcService.terminateAudit(shxxDto, user,request,lastlcxh,obj);
//										shgcService.doAudit(shxxDto, user,request);
                                    //更新审批管理信息
                                    ddspglDto.setCljg("1");
                                    ddspglService.updatecljg(ddspglDto);
                                    break;
                                } catch (Exception e) {
                                    t_map.put("ddspglid", ddspglDto.getDdspglid());
                                    t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());

                                    if(i==dd_sprjs.size()-1)
                                        throw new BusinessException("ICOM99019",e.toString());
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
                        shxx.setYwid(fksqDto.getFksqid());
                        shxx.setShlb(AuditTypeEnum.AUDIT_BUSINESS_PAYMENT_APPLICATION.getCode());
                        List<ShxxDto> shxxlist=shxxService.getShxxOrderByPass(shxx);
                        if(shxxlist!=null && shxxlist.size()>0) {
                            shxx.setShid(shxxlist.get(0).getShid());
                            shxx.setShyj("评论:"+content.substring(0, Math.min(content.length(), 6000)));//这里写死，备注钉钉审批被提交人员撤销了
                            shxx.setLrry(user.getYhid());
                            shxxService.insert(shxx);
                        }
                    }
                }
            }
        }catch(BusinessException e) {
            log.error("BusinessException:" + e.getMessage());
            throw new BusinessException("ICOM99019",e.getMsg());
        }catch (Exception e) {
            log.error("Exception:" + e.getMessage());
            throw new BusinessException("ICOM99019",e.toString());
        }finally {
            if(ddfbsglDto!=null) {
                if(org.apache.commons.lang3.StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {
                    if("1".equals(ddspbcbj)) {
                        t_map.put("sfbcspgl", "1");//是否返回上一层新增钉钉审批管理信息
                    }
                }
            }
        }
        return true;
    }

    /**
     * 审核列表
     * @param fksqDto
     * @return
     */
    public List<FksqDto> pageGetListAuditPaymentApplication(FksqDto fksqDto){
        // 获取人员ID和履历号
        List<FksqDto> t_sbyzList= dao.getPagedAuditPaymentApplication(fksqDto);

        if (t_sbyzList == null || t_sbyzList.size() == 0)
            return t_sbyzList;

        List<FksqDto> sqList = dao.getAuditListPaymentApplication(t_sbyzList);

        commonService.setSqrxm(sqList);

        return sqList;
    }

    /**
     * 删除
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean deleteDto(FksqDto fksqDto){
        List<FksqDto> dtoList = dao.getDtoList(fksqDto);
        List<String> ywids=new ArrayList<>();
        if(dtoList!=null&&!dtoList.isEmpty()){
            for (FksqDto dto : dtoList) {
                //判断状态 是否需要撤回 OA取消审批的同时组织钉钉审批
                if (StatusEnum.CHECK_SUBMIT.getCode().equals(dto.getZt()) && StringUtil.isNotBlank(dto.getDdslid())){
                    ShgcDto dtoByYwid = shgcService.getDtoByYwid(dto.getFksqid());
                    talkUtil.cancelDingtalkAudit(dtoByYwid.getYhm(), dto.getDdslid(), "", dtoByYwid.getSqrddid());
                }
                if ((StatusEnum.CHECK_PASS.getCode().equals(dto.getZt())||StatusEnum.CHECK_SUBMIT.getCode().equals(dto.getZt()))
                        &&StringUtil.isNotBlank(dto.getYwid())
                        && CommonCreditEnum.SW_YSZK_FK.getCode().equals(dto.getYwlx())
                        &&!ywids.contains(dto.getYwid())){
                    ywids.add(dto.getYwid());
                }
            }
        }
        int delete = dao.delete(fksqDto);
        if(delete==0){
            return false;
        }
        if(!ywids.isEmpty()){
            List<String> yszkids=new ArrayList<>();
            List<SwyszkDto> updateData = dao.getUpdateData(ywids);
            if(updateData!=null&&!updateData.isEmpty()){
                for(String ywid:ywids){
                    boolean isFind=false;
                    for(SwyszkDto dto:updateData){
                       if(ywid.equals(dto.getYszkid())){
                           isFind=true;
                       }
                    }
                    if(!isFind){
                        yszkids.add(ywid);
                    }
                }
            }else{
               yszkids.addAll(ywids);
            }
            if(!yszkids.isEmpty()){
                SwyszkDto swyszkDto=new SwyszkDto();
                swyszkDto.setIds(yszkids);
                List<SwyszkDto> swyszkDtos = swyszkService.getDtoList(swyszkDto);
                if(swyszkDtos!=null&&!swyszkDtos.isEmpty()){
                    if(updateData==null||updateData.size()==0){
                        updateData=new ArrayList<>();
                    }
                   for(SwyszkDto dto:swyszkDtos){
                       BigDecimal countJsje = new BigDecimal(dto.getJsje());
                       BigDecimal countSfje = new BigDecimal(dto.getSfje());
                       BigDecimal countKdfy = new BigDecimal(dto.getKdfy());
                       SwyszkDto swyszkDto_t=new SwyszkDto();
                       if (countJsje.add(countKdfy).compareTo(countSfje) > 0){
                           swyszkDto_t.setWfkje("0");
                       }else{
                           swyszkDto_t.setWfkje(countSfje.subtract(countJsje).subtract(countKdfy).toString());
                       }
                       swyszkDto_t.setYszkid(dto.getYszkid());
                       swyszkDto_t.setFkje("0");
                       swyszkDto_t.setYfksl("0");
                       swyszkDto_t.setFksl("0");
                       updateData.add(swyszkDto_t);
                   }
                }
            }
            for(SwyszkDto dto:updateData){
                dto.setXgry(fksqDto.getScry());
                dto.setSfjq("0");
            }
            boolean updateList = swyszkService.updatePayList(updateData);
            if(!updateList){
                return false;
            }
        }
        return true;
    }
}
