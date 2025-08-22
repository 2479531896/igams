package com.matridx.igams.wechat.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.CommonCreditEnum;
import com.matridx.igams.common.enums.DdAuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.wechat.dao.entities.KpsqDto;
import com.matridx.igams.wechat.dao.entities.KpsqModel;
import com.matridx.igams.wechat.dao.entities.SwyszkDto;
import com.matridx.igams.wechat.dao.post.IKpsqDao;
import com.matridx.igams.wechat.service.svcinterface.IKpsqService;
import com.matridx.igams.wechat.service.svcinterface.ISwyszkService;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KpsqServiceImpl extends BaseBasicServiceImpl<KpsqDto, KpsqModel, IKpsqDao> implements IKpsqService, IAuditService {
    @Autowired
    ICommonService commonservice;
    @Autowired
    DingTalkUtil talkUtil;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.wechat.registerurl:}")
    private String registerurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IDdfbsglService ddfbsglService;
    @Autowired
    ICommonService commonService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IDdspglService ddspglService;
    @Autowired
    IShxxService shxxService;
    @Autowired
    ISwyszkService swyszkService;
    @Autowired
    private IFjcfbService fjcfbService;
    @Autowired
    IDdxxglService ddxxglService;

    private Logger log = LoggerFactory.getLogger(KpsqServiceImpl.class);

    @Autowired
    IShgcService shgcService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean insertInfo(KpsqDto kpsqDto) {
        boolean success = dao.insert(kpsqDto)!=0;
        if (!success)
            return false;
        if(!CollectionUtils.isEmpty(kpsqDto.getFjids())){
            for (int i = 0; i < kpsqDto.getFjids().size(); i++) {
                success = fjcfbService.save2RealFile(kpsqDto.getFjids().get(i),kpsqDto.getFpsqid());
                if (!success)
                    return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean submitInfo(KpsqDto kpsqDto) {
        boolean success = dao.update(kpsqDto)!=0;
        if (!success)
            return false;
        if(!CollectionUtils.isEmpty(kpsqDto.getFjids())){
            for (int i = 0; i < kpsqDto.getFjids().size(); i++) {
                success = fjcfbService.save2RealFile(kpsqDto.getFjids().get(i),kpsqDto.getFpsqid());
                if (!success)
                    return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean delInfo(KpsqDto kpsqDto) {
        List<KpsqDto> dtoList = dao.getDtoList(kpsqDto);
        List<String> ywids=new ArrayList<>();
        if(dtoList!=null&&!dtoList.isEmpty()){
            for (KpsqDto dto : dtoList) {
                //判断状态 是否需要撤回 OA取消审批的同时组织钉钉审批
                if (StatusEnum.CHECK_SUBMIT.getCode().equals(dto.getZt()) && StringUtil.isNotBlank(dto.getDdslid())){
                    ShgcDto dtoByYwid = shgcService.getDtoByYwid(dto.getFpsqid());
                    talkUtil.cancelDingtalkAudit(dtoByYwid.getYhm(), dto.getDdslid(), "", dtoByYwid.getSqrddid());
                }
                if ((StatusEnum.CHECK_PASS.getCode().equals(dto.getZt())||StatusEnum.CHECK_SUBMIT.getCode().equals(dto.getZt()))
                        &&StringUtil.isNotBlank(dto.getYwid())
                        && CommonCreditEnum.SW_YSZK_KP.getCode().equals(dto.getYwlx())
                        &&!ywids.contains(dto.getYwid())){
                    ywids.add(dto.getYwid());
                }
            }
        }
        int delete = dao.delete(kpsqDto);
        if(delete==0){
            return false;
        }
        if(!ywids.isEmpty()){
            List<SwyszkDto> updateData = dao.getUpdateData(ywids);
            if(updateData!=null&&!updateData.isEmpty()){
                for(SwyszkDto dto:updateData){
                    dto.setXgry(kpsqDto.getScry());
                    for(KpsqDto kpsqDto_t:dtoList){
                        if(StringUtil.isNotBlank(kpsqDto_t.getYwid())&&kpsqDto_t.getYwid().equals(dto.getYszkid())){
                            if(StatusEnum.CHECK_PASS.getCode().equals(kpsqDto_t.getZt())){
                                dto.setYkpsl(String.valueOf(Integer.parseInt(dto.getYkpsl())-1));
                                dto.setKpsl(String.valueOf(Integer.parseInt(dto.getKpsl())-1));
                            }else if(StatusEnum.CHECK_SUBMIT.getCode().equals(kpsqDto_t.getZt())){
                                dto.setKpsl(String.valueOf(Integer.parseInt(dto.getKpsl())-1));
                            }
                        }
                    }
                }
            }else{
                updateData=new ArrayList<>();
                for(String ywid:ywids){
                    SwyszkDto swyszkDto=new SwyszkDto();
                    swyszkDto.setYszkid(ywid);
                    swyszkDto.setXgry(kpsqDto.getScry());
                    swyszkDto.setKpje("0");
                    swyszkDto.setYkpsl("0");
                    swyszkDto.setKpsl("0");
                    updateData.add(swyszkDto);
                }
            }
            boolean updateList = swyszkService.updateInvoiceList(updateData);
            if(!updateList){
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean updateFphm(KpsqDto kpsqDto) throws BusinessException {
        boolean success = dao.update(kpsqDto)!=0;
        if (!success)
            throw new BusinessException("msg","更新开票申请发票号码信息失败!");
        kpsqDto.setFpsqid(null);
        SwyszkDto swyszkDto = dao.getAllInfo(kpsqDto);
        if (null != swyszkDto && StringUtil.isNotBlank(swyszkDto.getKphm()) ){
            swyszkDto.setXgry(kpsqDto.getXgry());
            swyszkDto.setYszkid(kpsqDto.getYwid());
            success = swyszkService.update(swyszkDto);
            if (!success)
                throw new BusinessException("msg","更新应收账款发票号码信息失败!");
        }
        return true;
    }

    @Override
    public SwyszkDto getAllInfo(KpsqDto kpsqDto){
        return dao.getAllInfo(kpsqDto);
    }

    @Override
    public List<KpsqDto> getPagedDtoList(KpsqDto kpsqDto) {
        List<KpsqDto> list = dao.getPagedDtoList(kpsqDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_BUSINESS_INVOICE_APPLICATION.getCode(), "zt", "fpsqid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }
    @Override
    public List<KpsqDto> getPagedAuditList(KpsqDto kpsqDto) {
        List<KpsqDto> list = dao.getPagedAuditList(kpsqDto);
        if (CollectionUtils.isEmpty(list))
            return list;
        List<KpsqDto> sqList = dao.getAuditListRecheck(list);
        commonservice.setSqrxm(sqList);
        return sqList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        KpsqDto kpsqDto = (KpsqDto)baseModel;
        kpsqDto.setXgry(operator.getYhid());
        boolean isSuccess = true;
        if (StringUtil.isNotBlank(kpsqDto.getFpsqid())){
            isSuccess = submitInfo(kpsqDto);
        }
        return isSuccess;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        for (ShgcDto shgcDto : shgcList) {
            KpsqDto kpsqDto = new KpsqDto();
            kpsqDto.setFpsqid(shgcDto.getYwid());
            kpsqDto.setXgry(operator.getYhid());
            KpsqDto dtoById = dao.getDtoById(kpsqDto.getFpsqid());
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                // 由于合同提交时直接发起了钉钉审批，所以审核不通过时直接采用钉钉的审批拒绝消息即可
                kpsqDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                //OA取消审批的同时组织钉钉审批
                if(dtoById!=null && StringUtils.isNotBlank(dtoById.getDdslid()) && AuditTypeEnum.AUDIT_BUSINESS_INVOICE_APPLICATION.getCode().equals(shgcDto.getShlb())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), dtoById.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map=JSON.parseObject(success,Map.class);
                    boolean bo1= (boolean) result_map.get("success");
                    if(bo1)
                        kpsqDto.setDdslid("");
                }
                if(!StatusEnum.CHECK_UNPASS.getCode().equals(dtoById.getZt())){
                    SwyszkDto swyszkDto=new SwyszkDto();
                    swyszkDto.setFpslbj("0");
                    swyszkDto.setYszkid(dtoById.getYwid());
                    swyszkDto.setXgry(operator.getYhid());
                    swyszkService.updateAmount(swyszkDto);
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                kpsqDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if (StringUtil.isNotBlank(dtoById.getYwid()) && CommonCreditEnum.SW_YSZK_KP.getCode().equals(dtoById.getYwlx())){
                    SwyszkDto swyszkDto = dao.getAllInfo(dtoById);
                    if (null != swyszkDto){
                        swyszkDto.setXgry(operator.getYhid());
                        swyszkDto.setYszkid(dtoById.getYwid());
                        swyszkService.update(swyszkDto);
                    }
                }
                if(!StatusEnum.CHECK_PASS.getCode().equals(dtoById.getZt())){
                    SwyszkDto swyszkDto=new SwyszkDto();
                    swyszkDto.setYfpslbj("1");
                    swyszkDto.setYszkid(dtoById.getYwid());
                    swyszkDto.setXgry(operator.getYhid());
                    swyszkService.updateAmount(swyszkDto);
                }
            }else {
                kpsqDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if("1".equals(shgcDto.getXlcxh()) && shgcDto.getShxx()==null) {//提交的时候发起钉钉审批
                    try {
                        Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "开票申请流程");//获取审批模板ID
                        String templateCode=(String) template.get("message");
                        //获取申请人信息(合同申请应该为采购部门)
                        User user=new User();
                        user.setYhid(shgcDto.getSqr());
                        user=commonservice.getUserInfoById(user);
                        if(user==null)
                            throw new BusinessException("ICOM99019","未获取到申请人信息！");
                        if(org.apache.commons.lang.StringUtils.isBlank(user.getDdid()))
                            throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
                        String userid=user.getDdid();
                        String dept=user.getJgid ();
                        Map<String,String> map= new HashMap<>();
                        map.put("部门", "["+dtoById.getSqbm()+"]");
                        map.put("发票性质", dtoById.getFpxzmc());
                        map.put("开票对象", dtoById.getKpdxmc());
                        map.put("客户名称", dtoById.getKhmc());
                        map.put("税号", StringUtil.isNotBlank(dtoById.getSh())?dtoById.getSh():"");
                        map.put("开户银行与账号", StringUtil.isNotBlank(dtoById.getKhyhjzh())?dtoById.getKhyhjzh():"");
                        map.put("地址电话",StringUtil.isNotBlank(dtoById.getDzdh())?dtoById.getDzdh():"");
                        map.put("电子邮箱地址",StringUtil.isNotBlank(dtoById.getYx())?dtoById.getYx():"");
                        map.put("开具日期", dtoById.getKjrq());
                        map.put("开具金额（元）", dtoById.getKjje());
                        map.put("开票类型", dtoById.getKplxmc());
                        map.put("开票主体", dtoById.getKpztmc());
                        map.put("开票内容", dtoById.getKpnr());
                        map.put("备注", StringUtil.isNotBlank(dtoById.getBz())?dtoById.getBz():"");
                        map.put("其他内容",applicationurl+urlPrefix+"/ws/invoicing/getRequestsUrl?ywid="+dtoById.getFpsqid());
                        List<DdxxglDto> dtoList = ddxxglService.getDingtalkAuditDep(DdAuditTypeEnum.SW_KP.getCode());
                        if (CollectionUtils.isEmpty(dtoList))
                            throw new BusinessException("msg","未配置钉钉审批流程!");
                        String ddids = "";
                        if (StringUtil.isNotBlank(dtoById.getSpr())){
                            User user1 = new User();
                            user1.setIds(Arrays.asList(dtoById.getSpr().split(",")));
                            List<User> list = commonService.getListByIds(user1);
                            for (User user2 : list) {
                                if (StringUtil.isNotBlank(user2.getDdid())){
                                    ddids+=","+user2.getDdid();
                                }
                            }
                        }


                        Map<String,Object> t_map=talkUtil.createInstance(operator.getYhm(), templateCode, StringUtil.isNotBlank(ddids)? ddids.substring(1)+","+dtoList.get(0).getSpr():dtoList.get(0).getSpr(), dtoList.get(0).getCsr(), userid, dept, map,null,null);
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
                                paramMap.add("fwqmc", "杰毅医检");
                                paramMap.add("spywlx", shgcDto.getShlb());
                                paramMap.add("hddz",applicationurl);
                                paramMap.add("wbcxid", operator.getWbcxid());//存入外部程序id

                                //                                //分布式保留一份
                                boolean t_result = t_restTemplate.postForObject( applicationurl+"/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
                                if(!t_result)
                                    return false;
                                //主站保留一份
                                if(org.apache.commons.lang.StringUtils.isNotBlank(registerurl)){
                                    boolean result = t_restTemplate.postForObject(registerurl + "/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
                                    if(!result)
                                        return false;
                                }
                                kpsqDto.setDdslid(String.valueOf(result_map.get("process_instance_id")));
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
                        throw new BusinessException("msg","异常!异常信息:"+e.toString());
                    }
                    SwyszkDto swyszkDto=new SwyszkDto();
                    swyszkDto.setFpslbj("1");
                    swyszkDto.setYszkid(dtoById.getYwid());
                    swyszkDto.setXgry(operator.getYhid());
                    swyszkService.updateAmount(swyszkDto);
                }
            }
            dao.update(kpsqDto);
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        for (ShgcDto shgcDto : shgcList) {
            KpsqDto kpsqDto = new KpsqDto();
            String fpsqid = shgcDto.getYwid();
            kpsqDto.setXgry(operator.getYhid());
            kpsqDto.setFpsqid(fpsqid);
            KpsqDto dtoById = dao.getDtoById(kpsqDto.getFpsqid());
            if (auditParam.isCancelOpe()) {
                kpsqDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
            } else {
                kpsqDto.setZt(StatusEnum.CHECK_NO.getCode());
                //OA取消审批的同时组织钉钉审批
                if(dtoById!=null && StringUtils.isNotBlank(dtoById.getDdslid()) && AuditTypeEnum.AUDIT_BUSINESS_INVOICE_APPLICATION.getCode().equals(shgcDto.getShlb())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), dtoById.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map=JSON.parseObject(success,Map.class);
                    boolean bo1= (boolean) result_map.get("success");
                    if(bo1)
                        kpsqDto.setDdslid("");
                }
                SwyszkDto swyszkDto=new SwyszkDto();
                swyszkDto.setFpslbj("0");
                swyszkDto.setYszkid(dtoById.getYwid());
                swyszkDto.setXgry(operator.getYhid());
                swyszkService.updateAmount(swyszkDto);
            }
            dao.update(kpsqDto);
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) throws BusinessException {
        return null;
    }

    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) throws BusinessException {
        Map<String, Object> map =new HashMap<>();
        List<String> ids = (List<String>)param.get("ywids");
        KpsqDto kpsqDto = new KpsqDto();
        kpsqDto.setIds(ids);
        List<KpsqDto> dtoList = dao.getDtoList(kpsqDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(KpsqDto dto:dtoList){
                list.add(dto.getFpsqid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean requestsCallback(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException {
        KpsqDto kpsqDto=new KpsqDto();
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
        String wbcxidString  = obj.getString("wbcxid"); //获取外部程序id

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
//			int a=1/0;
            if(ddfbsglDto==null)
                throw new BusinessException("message","未获取到相应的钉钉分布式信息！");
            if(org.apache.commons.lang3.StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {
                if("1".equals(ddspbcbj)) {
                    ddspglDto=ddspglService.insertInfo(obj);
                }else {
                    if(ddspgllist!=null && ddspgllist.size()>0) {
                        ddspglDto=ddspgllist.get(0);
                    }else{
                        ddspglDto=ddspglService.insertInfo(obj);
                    }
                }
            }
            kpsqDto.setDdslid(processInstanceId);
            //根据钉钉审批实例ID查询关联请购单
            kpsqDto=dao.getDto(kpsqDto);
            //若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if(kpsqDto!=null) {
                //获取审批人信息
                User user=new User();
                user.setDdid(staffId);
                user.setWbcxid(wbcxidString);
                user = commonservice.getByddwbcxid(user);
                //判断查出得信息是否为空
                if(user==null)
                    return false;
                //获取审批人角色信息
                Map<String, String> param = new HashMap<>();
                param.put("yhid", user.getYhid());
                List<Map<String, String>> yhqxDtos = commonservice.getJsDtoList(param);

                // 获取当前审核过程
                ShgcDto t_shgcDto = shgcService.getDtoByYwid(kpsqDto.getFpsqid());
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
                    if(yhqxDtos!=null && yhqxDtos.size()>0) {
                        //审批正常结束（同意或拒绝）
                        ShxxDto shxxDto=new ShxxDto();
                        shxxDto.setLcxh(t_shgcDto.getXlcxh());
                        shxxDto.setShlb(t_shgcDto.getShlb());
                        shxxDto.setShyj(remark);
                        shxxDto.setYwid(kpsqDto.getFpsqid());
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
                                log.error("DingTalkMaterPurchaseAudit(钉钉物料请购审批转发提醒)------转发人员:"+user.getZsxm()+",人员ID:"+user.getYhid()+",发票号码:"+kpsqDto.getFphm()+",开票申请ID:"+kpsqDto.getFpsqid()+",转发时间:"+date);
                                return true;
                            }
                            //调用审核方法
                            Map<String, List<String>> map= ToolUtil.reformRequest(request);
                            log.error("map:"+map);
                            List<String> list= new ArrayList<>();
                            list.add(kpsqDto.getFpsqid());
                            map.put("fpsqid", list);
                            //若一个用户多个角色导致审核异常时
                            for(int i=0;i<yhqxDtos.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(yhqxDtos.get(i).get("jsid"));
                                    user.setDqjsmc(yhqxDtos.get(i).get("jsmc"));
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
                                    log.error("callbackAduit-Exception:" + e.getMessage());
                                    t_map.put("ddspglid", ddspglDto.getDdspglid());
                                    t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());

                                    if(i==yhqxDtos.size()-1)
                                        throw new BusinessException("ICOM99019",e.getMessage());
                                }
                            }
                        }
                        //撤销审批
                        else if(("terminate").equals(type)) {
                            shxxDto.setThlcxh(null);
                            shxxDto.setYwglmc(kpsqDto.getFphm());
                            KpsqDto kpsqDto1 = new KpsqDto();
                            kpsqDto1.setFpsqid(kpsqDto.getFpsqid());
                            kpsqDto1.setDdslid("");
                            dao.update(kpsqDto1);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            //调用审核方法
                            Map<String, List<String>> map=ToolUtil.reformRequest(request);
                            List<String> list= new ArrayList<>();
                            list.add(kpsqDto.getFpsqid());
                            map.put("fpsqid", list);
                            for(int i=0;i<yhqxDtos.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(yhqxDtos.get(i).get("jsid"));
                                    user.setDqjsmc(yhqxDtos.get(i).get("jsmc"));
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

                                    if(i==yhqxDtos.size()-1)
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
                        shxx.setYwid(kpsqDto.getFpsqid());
                        shxx.setShlb(AuditTypeEnum.AUDIT_BUSINESS_CONTRACT.getCode());
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
}
