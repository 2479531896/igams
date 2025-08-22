package com.matridx.igams.warehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.DdAuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.warehouse.dao.entities.GfyzglDto;
import com.matridx.igams.warehouse.dao.entities.GfyzglModel;
import com.matridx.igams.warehouse.dao.entities.GfyzmxDto;
import com.matridx.igams.warehouse.dao.post.IGfyzglDao;
import com.matridx.igams.warehouse.service.svcinterface.IGfyzglService;
import com.matridx.igams.warehouse.service.svcinterface.IGfyzmxService;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author jld
 */
@Service
public class GfyzglServiceImpl extends BaseBasicServiceImpl<GfyzglDto, GfyzglModel, IGfyzglDao> implements IGfyzglService, IAuditService {
    @Autowired
    IShgcService shgcService;
    @Autowired
    IJgxxService jgxxService;
    @Autowired
    IGfyzmxService gfyzmxService;
    @Autowired
    IDdxxglService ddxxglService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IDdfbsglService ddfbsglService;
    @Autowired
    IDdspglService ddspglService;
    @Autowired
    ICommonService commonService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IShxxService shxxService;
    @Autowired
    private IJcsjService jcsjService;
    @Value("${matridx.wechat.registerurl:}")
    private String registerurl;
    @Value("${matridx.systemflg.systemname:}")
    private String systemname;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;

    private final Logger log = LoggerFactory.getLogger(GfyzglServiceImpl.class);

    /**
     * @Description: 供方验证列表
     * @param gfyzglDto
     * @return java.util.List<com.matridx.igams.warehouse.dao.entities.GfyzglDto>
     * @Author: 郭祥杰
     * @Date: 2024/6/17 9:30
     */
    @Override
    public List<GfyzglDto> getPagedEvaluation(GfyzglDto gfyzglDto) {
        List<GfyzglDto> list = dao.getPagedDtoList(gfyzglDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode(), "sqzt", "gfyzid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_VERIFICATION_AUDIT.getCode(), "yzzt", "gfyzid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    /**
     * @Description: 生成记录编号
     * @param jgid
     * @return java.lang.String
     * @Author: 郭祥杰
     * @Date: 2024/6/17 14:24
     */
    @Override
    public String buildCode(String jgid) {
        JgxxDto jgxxDto = jgxxService.getDtoById(jgid);
        String jlbh="";
        if(StringUtil.isNotBlank(jgxxDto.getKzcs1())){
            jlbh=jgxxDto.getKzcs1() + "-" + DateUtils.getCustomFomratCurrentDate("yyyy-MMdd") + "-";
            jlbh = jlbh + dao.getJlbhSerial(jlbh);
        }
        return jlbh;
    }

    /**
     * @Description: 新增供方验证
     * @param gfyzglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/18 15:59
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertEvaluation(GfyzglDto gfyzglDto)  throws BusinessException{
        gfyzglDto.setGfyzid(StringUtil.generateUUID());
        List<GfyzmxDto> gfyzmxDtoList = JSON.parseArray(gfyzglDto.getGfyzmxJson(), GfyzmxDto.class);
        if(gfyzmxDtoList!=null && !gfyzmxDtoList.isEmpty()){
            for (GfyzmxDto gfyzmxDto:gfyzmxDtoList){
                gfyzmxDto.setYzmxid(StringUtil.generateUUID());
                gfyzmxDto.setGfyzid(gfyzglDto.getGfyzid());
                gfyzmxDto.setLrry(gfyzglDto.getLrry());
            }
            gfyzglDto.setSqzt(StatusEnum.CHECK_NO.getCode());
            gfyzglDto.setYzzt(StatusEnum.CHECK_NO.getCode());
            boolean result = dao.insert(gfyzglDto)!= 0;
            if(!result){
                throw new BusinessException("msg","新增供方验证失败!");
            }
            result = gfyzmxService.insertGfyzmxDtoList(gfyzmxDtoList);
            if(!result){
                throw new BusinessException("msg","新增供方验证明细失败!");
            }
        }else {
            throw new BusinessException("msg","供方验证明细不允许为空!");
        }
        return true;
    }

    /**
     * @Description: 修改
     * @param gfyzglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/19 16:35
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateEvaluation(GfyzglDto gfyzglDto) throws BusinessException {
        boolean result = false;
        List<GfyzmxDto> gfyzmxDtoList = JSON.parseArray(gfyzglDto.getGfyzmxJson(), GfyzmxDto.class);
        if(gfyzmxDtoList!=null && !gfyzmxDtoList.isEmpty()){
            List<GfyzmxDto> delGfyzmxDtos = new ArrayList<>();
            List<GfyzmxDto> modGfyzmxDtos = new ArrayList<>();
            List<GfyzmxDto> addGfyzmxDtos = new ArrayList<>();
            GfyzmxDto gfyzmxDto = new GfyzmxDto();
            gfyzmxDto.setGfyzid(gfyzglDto.getGfyzid());
            List<GfyzmxDto> gfyzmxDtos = gfyzmxService.getListByGfyzid(gfyzmxDto);
            if(gfyzmxDtos!=null && !gfyzmxDtos.isEmpty()){
                //获取gfyzmxDtos存在，gfyzmxDtoList不存在的数据删除
                delGfyzmxDtos = gfyzmxDtos.stream()
                        .filter(item -> !gfyzmxDtoList.stream()
                                .map(GfyzmxDto::getYzmxid)
                                .collect(Collectors.toList())
                                .contains(item.getYzmxid()))
                        .collect(Collectors.toList());
                //获取gfyzmxDtoList存在，gfyzmxDtos不存在的数据新增
                addGfyzmxDtos = gfyzmxDtoList.stream()
                        .filter(item -> !gfyzmxDtos.stream()
                                .map(GfyzmxDto::getYzmxid)
                                .collect(Collectors.toList())
                                .contains(item.getYzmxid()))
                        .collect(Collectors.toList());
                //获取gfyzmxDtoList存在，gfyzmxDtos存在的数据修改
                modGfyzmxDtos = gfyzmxDtoList.stream()
                        .filter(item -> gfyzmxDtos.stream()
                                .map(GfyzmxDto::getYzmxid)
                                .collect(Collectors.toList())
                                .contains(item.getYzmxid()))
                        .collect(Collectors.toList());
            }else{
                addGfyzmxDtos = gfyzmxDtoList;
            }
            if(!addGfyzmxDtos.isEmpty()){
                for (GfyzmxDto gfyzmx:addGfyzmxDtos){
                    gfyzmx.setGfyzid(gfyzglDto.getGfyzid());
                    gfyzmx.setYzmxid(StringUtil.generateUUID());
                    gfyzmx.setLrry(gfyzglDto.getXgry());
                }
                result = gfyzmxService.insertGfyzmxDtoList(addGfyzmxDtos);
                if(!result){
                    throw new BusinessException("msg","供方验证明细新增失败!");
                }
            }
            if(!delGfyzmxDtos.isEmpty()){
                for (GfyzmxDto gfyzmx : delGfyzmxDtos){
                    gfyzmx.setScsj("1");
                    gfyzmx.setScry(gfyzglDto.getXgry());
                    gfyzmx.setScbj("1");
                }
                modGfyzmxDtos.addAll(delGfyzmxDtos);
            }
            if(!modGfyzmxDtos.isEmpty()){
                for (GfyzmxDto gfyzmx : modGfyzmxDtos){
                    gfyzmx.setXgry(gfyzglDto.getXgry());
                }
                result = gfyzmxService.updateGfyzmxDtos(modGfyzmxDtos);
                if(!result){
                    throw new BusinessException("msg","供方验证明细修改失败!");
                }
            }
            result = dao.update(gfyzglDto)>0;
            if(!result){
                throw new BusinessException("msg","供方验证修改失败!");
            }
        }else {
            throw new BusinessException("msg","供方验证明细不允许为空!");
        }
        return true;
    }

    /**
     * @Description: 批量删除
     * @param gfyzglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/20 16:37
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delEvaluation(GfyzglDto gfyzglDto) throws BusinessException {
        boolean result = dao.delete(gfyzglDto)>0;
        if(!result){
            throw new BusinessException("msg","删除供方验证失败!");
        }
        GfyzmxDto gfyzmxDto = new GfyzmxDto();
        gfyzmxDto.setIds(gfyzglDto.getIds());
        gfyzmxDto.setScry(gfyzglDto.getScry());
        result = gfyzmxService.delete(gfyzmxDto);
        if(!result){
            throw new BusinessException("msg","删除供方验证明细失败!");
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        GfyzglDto gfyzglDto = (GfyzglDto) baseModel;
        gfyzglDto.setXgry(operator.getYhid());
        if(StringUtil.isNotBlank(gfyzglDto.getGfyzmxJson())){
            boolean result = updateEvaluation(gfyzglDto);
            if(!result){
                throw new BusinessException("msg","供方验证审核修改失败!");
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }

        for (ShgcDto shgcDto : shgcList) {
            GfyzglDto gfyzglDto = new GfyzglDto();
            gfyzglDto.setGfyzid(shgcDto.getYwid());
            gfyzglDto.setXgry(operator.getYhid());
            GfyzglDto gfyzglDtoT = getDtoById(shgcDto.getYwid());
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                if(AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode().equals(shgcDto.getAuditType())){
                    gfyzglDto.setSqzt(StatusEnum.CHECK_UNPASS.getCode());
                }
                if(AuditTypeEnum.AUDIT_VERIFICATION_AUDIT.getCode().equals(shgcDto.getAuditType())){
                    gfyzglDto.setYzzt(StatusEnum.CHECK_UNPASS.getCode());
                }
            }else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())){
                if(AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode().equals(shgcDto.getAuditType())){
                    gfyzglDto.setSqzt(StatusEnum.CHECK_PASS.getCode());
                }
                if(AuditTypeEnum.AUDIT_VERIFICATION_AUDIT.getCode().equals(shgcDto.getAuditType())){
                    gfyzglDto.setYzzt(StatusEnum.CHECK_PASS.getCode());
                }
            }else{
                if(AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode().equals(shgcDto.getAuditType())){
                    gfyzglDto.setSqzt(StatusEnum.CHECK_SUBMIT.getCode());
                }
                if(AuditTypeEnum.AUDIT_VERIFICATION_AUDIT.getCode().equals(shgcDto.getAuditType())){
                    gfyzglDto.setYzzt(StatusEnum.CHECK_SUBMIT.getCode());
                }
                if ("1".equals(shgcDto.getXlcxh())&& shgcDto.getShxx()==null){
                    try{
                        // 根据申请部门查询审核流程
                        List<DdxxglDto> ddxxglDtos = ddxxglService.getProcessByJgid(gfyzglDtoT.getJgid(), AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode().equals(shgcDto.getAuditType())?DdAuditTypeEnum.SP_GFYZSQ.getCode():DdAuditTypeEnum.SP_GFYZJG.getCode());
                        if(CollectionUtils.isEmpty(ddxxglDtos)){
                            throw new BusinessException("msg","未找到所属部门审核流程！");
                        }
                        if(ddxxglDtos.size() > 1){
                            throw new BusinessException("msg","找到多条所属部门审核流程！");
                        }
                        String approvers = ddxxglDtos.get(0).getSpr();
                        String csr = StringUtil.isNotBlank(ddxxglDtos.get(0).getCsr())?ddxxglDtos.get(0).getCsr():"";
                        Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode().equals(shgcDto.getAuditType())?"验证审核申请":"验证审核结果");//获取审批模板ID
                        String templateCode=(String) template.get("message");
                        Map<String,String> map=new HashMap<>();
                        map.put("所属公司",systemname);
                        map.put("供方名称",gfyzglDtoT.getGfmc());
                        map.put("供方编号",gfyzglDtoT.getGfbh());
                        map.put("联系人",gfyzglDtoT.getLxr());
                        map.put("电话",gfyzglDtoT.getDh());
                        map.put("传真",StringUtil.isNotBlank(gfyzglDtoT.getCz())?gfyzglDtoT.getCz():"");
                        map.put("地址",gfyzglDtoT.getDz());
                        map.put("申请人",gfyzglDtoT.getSqrmc());
                        if(AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode().equals(shgcDto.getAuditType())){
                            if(StringUtil.isNotBlank(gfyzglDtoT.getShrddid())){
                                approvers = gfyzglDtoT.getShrddid()+","+approvers;
                            }
                            map.put("申请时间",gfyzglDtoT.getYzsqsj());
                            map.put("初步评价",gfyzglDtoT.getCbpj());
                            map.put("供应物资信息",applicationurl+urlPrefix+"/ws/evaluation/evaluationView?gfyzid="+gfyzglDtoT.getGfyzid());
                        }else{
                            map.put("结论",gfyzglDtoT.getJl());
                            if(StringUtil.isNotBlank(gfyzglDtoT.getShrddid())){
                                approvers = approvers.replace("|", ","+gfyzglDtoT.getShrddid()+",");
                            }else{
                                approvers = approvers.replace("|", ",");
                            }
                            List<String> glspds = new ArrayList<>();//关联审批单
                            glspds.add(gfyzglDtoT.getSqddslid());
                            map.put("验证审核时间",gfyzglDtoT.getYzshsj());
                            map.put("验证审核评价",gfyzglDtoT.getYzshpj());
                            map.put("关联审批单", JSON.toJSONString(glspds));
                            map.put("供应物资信息",applicationurl+urlPrefix+"/ws/evaluation/evaluationView?xsbj=0&gfyzid="+gfyzglDtoT.getGfyzid());
                        }
                        Map<String,Object> tMap=talkUtil.createInstance(operator.getYhm(), templateCode, approvers, csr, gfyzglDtoT.getDdid(), gfyzglDtoT.getJgid(), map,null,null);
                        String str=(String) tMap.get("message");
                        String status=(String) tMap.get("status");
                        if("success".equals(status)) {
                            @SuppressWarnings("unchecked")
                            Map<String,Object> resultMap= JSON.parseObject(str,Map.class);
                            if(("0").equals(String.valueOf(resultMap.get("errcode")))) {
                                //保存至钉钉分布式管理表(主站)
                                RestTemplate restTemplate = new RestTemplate();
                                MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                                paramMap.add("ddslid", String.valueOf(resultMap.get("process_instance_id")));
                                paramMap.add("fwqm", urlPrefix);
                                paramMap.add("cljg", "1");
                                paramMap.add("fwqmc", systemname);
                                paramMap.add("hddz",applicationurl);
                                paramMap.add("spywlx",shgcDto.getShlb());
                                //根据审批类型获取钉钉审批的业务类型，业务名称
                                JcsjDto jcsjDtoDd = new JcsjDto();
                                jcsjDtoDd.setCsdm(shgcDto.getAuditType());
                                jcsjDtoDd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
                                jcsjDtoDd = jcsjService.getDtoByCsdmAndJclb(jcsjDtoDd);
                                if(StringUtil.isBlank(jcsjDtoDd.getCsmc()) || StringUtil.isBlank(jcsjDtoDd.getCskz1())) {
                                    throw new BusinessException("msg","请设置"+shgcDto.getShlbmc()+"的钉钉审批回调类型基础数据！");
                                }
                                paramMap.add("ywmc", operator.getZsxm()+"提交的"+jcsjDtoDd.getCsmc());
                                paramMap.add("ywlx", jcsjDtoDd.getCskz1());
                                paramMap.add("wbcxid", operator.getWbcxid());
                                //分布式保留
                                DdfbsglDto ddfbsglDto = new DdfbsglDto();
                                ddfbsglDto.setProcessinstanceid(String.valueOf(resultMap.get("process_instance_id")));
                                ddfbsglDto.setFwqm(urlPrefix);
                                ddfbsglDto.setCljg("1");
                                ddfbsglDto.setFwqmc(systemname);
                                ddfbsglDto.setSpywlx(shgcDto.getShlb());
                                ddfbsglDto.setHddz(applicationurl);
                                ddfbsglDto.setYwlx(jcsjDtoDd.getCskz1());
                                ddfbsglDto.setYwmc(operator.getZsxm()+"提交的"+jcsjDtoDd.getCsmc());
                                ddfbsglDto.setWbcxid(operator.getWbcxid());
                                boolean result = ddfbsglService.saveDistributedMsg(ddfbsglDto);
                                if(!result){
                                    return false;
                                }
                                //主站保留一份
                                if(StringUtils.isNotBlank(registerurl)){
                                    result = Boolean.TRUE.equals(restTemplate.postForObject(registerurl + "/ws/purchase/saveDistributedMsg", paramMap, boolean.class));
                                    if(!result){
                                        return false;
                                    }
                                }
                                //若钉钉审批提交成功，则关联审批实例ID
                                if(AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode().equals(shgcDto.getAuditType())){
                                    gfyzglDto.setSqddslid(String.valueOf(resultMap.get("process_instance_id")));
                                }else{
                                    gfyzglDto.setYzddslid(String.valueOf(resultMap.get("process_instance_id")));
                                }
                            }else {
                                throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+tMap.get("message"));
                            }
                        }else {
                            throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+tMap.get("message"));
                        }
                    }catch (BusinessException e) {
                        // TODO: handle exception
                        throw new BusinessException("msg",e.getMsg());
                    }catch (Exception e) {
                        // TODO: handle exception
                        throw new BusinessException("msg","异常!异常信息:"+ e);
                    }
                }
            }
            update(gfyzglDto);
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                GfyzglDto gfyzglDto = new GfyzglDto();
                gfyzglDto.setGfyzid(shgcDto.getYwid());
                gfyzglDto.setXgry(operator.getYhid());
                if(AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode().equals(shgcDto.getShlb())){
                    gfyzglDto.setSqzt(StatusEnum.CHECK_SUBMIT.getCode());
                }else{
                    gfyzglDto.setYzzt(StatusEnum.CHECK_SUBMIT.getCode());
                }
                update(gfyzglDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                //OA取消审批的同时组织钉钉审批
                GfyzglDto gfyzglDtoT = getDtoById(shgcDto.getYwid());

                if(gfyzglDtoT!=null && (StringUtils.isNotBlank(gfyzglDtoT.getSqddslid()) || StringUtils.isNotBlank(gfyzglDtoT.getYzddslid()))) {
                    Map<String,Object> cancelResult=
                            talkUtil.cancelDingtalkAudit(operator.getYhm(), AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode().equals(shgcDto.getShlb())?gfyzglDtoT.getSqddslid():gfyzglDtoT.getYzddslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    Map<String,Object> resultMap=JSON.parseObject(success,Map.class);
                    boolean result= (boolean) resultMap.get("success");
                    if(result){
                        GfyzglDto gfyzglDto = new GfyzglDto();
                        gfyzglDto.setGfyzid(shgcDto.getYwid());
                        gfyzglDto.setXgry(operator.getYhid());
                        if(AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode().equals(shgcDto.getShlb())){
                            gfyzglDto.setSqzt(StatusEnum.CHECK_NO.getCode());
                            gfyzglDto.setUpdateFlg("0");
                        }else{
                            gfyzglDto.setYzzt(StatusEnum.CHECK_NO.getCode());
                            gfyzglDto.setUpdateFlg("1");
                        }
                        update(gfyzglDto);
                    }else {
                        log.error("钉钉撤回异常："+JSON.toJSONString(cancelResult));
                    }

                }
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
        @SuppressWarnings("unchecked")
        List<String> ids = (List<String>)param.get("ywids");
        GfyzglDto gfyzglDto = new GfyzglDto();
        gfyzglDto.setIds(ids);
        List<GfyzglDto> gfyzglDtoList = dao.getDtoList(gfyzglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(gfyzglDtoList)){
            for(GfyzglDto dto:gfyzglDtoList){
                list.add(dto.getGfyzid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean callbackEvaluationAduit(JSONObject object, HttpServletRequest request, Map<String, Object> map) throws BusinessException {
        GfyzglDto gfyzglDto = new GfyzglDto();
        String result = object.getString("result");// 正常结束时result为agree，拒绝时result为refuse，审批终止时没这个值，redirect转交
        String type = object.getString("type");// 审批正常结束（同意或拒绝）的type为finish，审批终止的type为terminate
        String processInstanceId = object.getString("processInstanceId");// 审批实例id
        String staffId = object.getString("staffId");// 审批人钉钉ID
        String remark = object.getString("remark");// 审核意见
        String content = object.getString("content");//评论
        String title= object.getString("title");
        String processCode=object.getString("processCode");
        String finishTime = object.getString("finishTime");// 审批完成时间
        String wbcxidString  = object.getString("wbcxid"); //获取外部程序id
        log.error("回调参数获取---------result:"+result+",type:"+type+",processInstanceId:"+processInstanceId+",staffId:"+staffId+",remark:"+remark+",finishTime"+finishTime+",外部程序id"+wbcxidString);
        DdfbsglDto ddfbsglDto = new DdfbsglDto();
        ddfbsglDto.setProcessinstanceid(processInstanceId);
        ddfbsglDto=ddfbsglService.getDtoById(processInstanceId);
        DdspglDto ddspglDto=new DdspglDto();
        DdspglDto tDdspglDto=new DdspglDto();
        tDdspglDto.setProcessinstanceid(processInstanceId);
        tDdspglDto.setType("finish");
        tDdspglDto.setEventtype(DingTalkUtil.BPMS_TASK_CHANGE);
        List<DdspglDto> ddspgllist=ddspglService.getDtoList(tDdspglDto);
        try {
            if(ddfbsglDto==null){
                throw new BusinessException("message","未获取到相应的钉钉分布式信息！");
            }
            if(org.apache.commons.lang.StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {
                if(!CollectionUtils.isEmpty(ddspgllist)) {
                    ddspglDto=ddspgllist.get(0);
                }
            }
            if(AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode().equals(ddfbsglDto.getSpywlx())){
                gfyzglDto.setSqddslid(processInstanceId);
            }else{
                gfyzglDto.setYzddslid(processInstanceId);
            }

            gfyzglDto = dao.getDto(gfyzglDto);
            // 若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if (gfyzglDto != null) {
                //获取审批人信息
                User user=new User();
                user.setDdid(staffId);
                user.setWbcxid(wbcxidString);
                user = commonService.getByddwbcxid(user);
                if (user == null){
                    return false;
                }
                log.error("user-yhid:" + user.getYhid() + "---user-zsxm:" + user.getZsxm() + "----user-yhm"
                        + user.getYhm() + "---user-ddid" + user.getDdid());
                // 获取审批人角色信息
                List<GfyzglDto> gfyzglDtoList = dao.getSprjsBySprid(user.getYhid());
                // 获取当前审核过程
                ShgcDto tShgcDto = shgcService.getDtoByYwid(gfyzglDto.getGfyzid());
                if (tShgcDto != null) {
                    // 获取的审核流程列表
                    ShlcDto shlcParam = new ShlcDto();
                    shlcParam.setShid(tShgcDto.getShid());
                    shlcParam.setGcid(tShgcDto.getGcid());// 处理旧流程判断用
                    @SuppressWarnings("unused")
                    List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);

                    if (("start").equals(type)) {
                        //更新分布式管理表信息
                        DdfbsglDto tDdfbsglDto=new DdfbsglDto();
                        tDdfbsglDto.setProcessinstanceid(processInstanceId);
                        tDdfbsglDto.setYwlx(processCode);
                        tDdfbsglDto.setYwmc(title);
                        ddfbsglService.update(tDdfbsglDto);
                    }
                    if (!CollectionUtils.isEmpty(gfyzglDtoList)) {
                        // 审批正常结束（同意或拒绝）
                        ShxxDto shxxDto = new ShxxDto();
                        shxxDto.setGcid(tShgcDto.getGcid());
                        shxxDto.setLcxh(tShgcDto.getXlcxh());
                        shxxDto.setShlb(tShgcDto.getShlb());
                        shxxDto.setShyj(remark);
                        shxxDto.setAuditType(tShgcDto.getShlb());
                        shxxDto.setYwid(gfyzglDto.getGfyzid());
                        if (StringUtil.isNotBlank(finishTime)){
                            shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                        }
                        log.error("shxxDto-lcxh:" + tShgcDto.getXlcxh() + " shxxDto-shlb:"
                                + tShgcDto.getShlb() + " shxxDto-shyj:" + remark + " shxxDto-ywid:"
                                + gfyzglDto.getGfyzid());
                        String lastlcxh = null;// 回退到指定流程

                        if (("finish").equals(type)) {
                            // 如果审批通过,同意
                            if (("agree").equals(result)) {
                                log.error("同意------");
                                shxxDto.setSftg("1");
                                if (org.apache.commons.lang.StringUtils.isBlank(tShgcDto.getXlcxh())){
                                    throw new BusinessException("msg", "回调失败，现流程序号为空！");
                                }
                                lastlcxh = String.valueOf(Integer.parseInt(tShgcDto.getXlcxh()) + 1);
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
                                log.error("DingTalkMaterPurchaseAudit(钉钉供方验证审批转发提醒)------转发人员:" + user.getZsxm()
                                        + ",人员ID:" + user.getYhm() + ",供方验证id:"
                                        + gfyzglDto.getGfyzid() + ",转发时间:" + date);
                                return true;
                            }
                            // 调用审核方法
                            Map<String, List<String>> mapT = ToolUtil.reformRequest(request);
                            log.error("map:"+mapT);
                            List<String> list = new ArrayList<>();
                            list.add(gfyzglDto.getGfyzid());
                            mapT.put("htid", list);
                            for(int i=0;i<gfyzglDtoList.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(gfyzglDtoList.get(i).getSprjsid());
                                    user.setDqjsmc(gfyzglDtoList.get(i).getSprjsmc());
                                    shxxDto.setYwids(new ArrayList<>());
                                    shgcService.doManyBackAudit(shxxDto, user,request,lastlcxh,object);
                                    //更新审批管理信息
                                    ddspglDto.setCljg("1");
                                    ddspglService.updatecljg(ddspglDto);
                                    break;
                                } catch (Exception e) {
                                    log.error("callbackHtglAduit-Exception:" + e.getMessage());
                                    map.put("ddspglid", ddspglDto.getDdspglid());
                                    map.put("processInstanceId", ddspglDto.getProcessinstanceid());
                                    if(i==gfyzglDtoList.size()-1){
                                        throw new BusinessException("msg",e.getMessage());
                                    }
                                }
                            }
                        }
                        // 撤销审批
                        else if (("terminate").equals(type)) {
                            shxxDto.setThlcxh(null);
                            shxxDto.setYwglmc(gfyzglDto.getGfmc());
                            GfyzglDto gfyzglDtoT = new GfyzglDto();
                            gfyzglDtoT.setGfyzid(gfyzglDto.getGfyzid());
                            if(AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode().equals(ddfbsglDto.getSpywlx())){
                                gfyzglDtoT.setUpdateFlg("0");
                                gfyzglDtoT.setSqzt(StatusEnum.CHECK_NO.getCode());
                            }else{
                                gfyzglDtoT.setUpdateFlg("1");
                                gfyzglDtoT.setYzzt(StatusEnum.CHECK_NO.getCode());
                            }
                            update(gfyzglDtoT);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            // 调用审核方法
                            Map<String, List<String>> mapY = ToolUtil.reformRequest(request);
                            List<String> list = new ArrayList<>();
                            list.add(gfyzglDto.getGfyzid());
                            mapY.put("gfyzid", list);
                            for(int i=0;i<gfyzglDtoList.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(gfyzglDtoList.get(i).getSprjsid());
                                    user.setDqjsmc(gfyzglDtoList.get(i).getSprjsmc());
                                    shxxDto.setYwids(new ArrayList<>());
                                    shgcService.terminateAudit(shxxDto, user,request,lastlcxh,object);
                                    //更新审批管理信息
                                    ddspglDto.setCljg("1");
                                    ddspglService.updatecljg(ddspglDto);
                                    break;
                                } catch (Exception e) {
                                    map.put("ddspglid", ddspglDto.getDdspglid());
                                    map.put("processInstanceId", ddspglDto.getProcessinstanceid());
                                    if(i==gfyzglDtoList.size()-1){
                                        throw new BusinessException("msg",e.toString());
                                    }
                                }
                            }
                        }else if(("comment").equals(type)) {
                            //评论时保存评论信息，添加至审核信息表
                            ShgcDto shgc = shgcService.getDtoByYwid(shxxDto.getYwid());//获得已有审核过程
                            ShxxDto shxx = new ShxxDto();
                            String shxxid =StringUtil.generateUUID();
                            if (StringUtil.isNotBlank(finishTime)){
                                shxx.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                            }
                            shxx.setPlbj("1");
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
                        if (StringUtil.isNotBlank(finishTime)){
                            shxx.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                        }
                        shxx.setShxxid(shxxid);
                        shxx.setSftg("1");
                        shxx.setYwid(gfyzglDto.getGfyzid());
                        shxx.setShlb(ddfbsglDto.getSpywlx());
                        shxx.setPlbj("1");
                        List<ShxxDto> shxxlist=shxxService.getShxxOrderByPass(shxx);
                        if(!CollectionUtils.isEmpty(shxxlist)) {
                            shxx.setShid(shxxlist.get(0).getShid());
                            shxx.setShyj("评论:"+content.substring(0, Math.min(content.length(), 6000)));//这里写死，备注钉钉审批被提交人员撤销了
                            shxx.setLrry(user.getYhid());
                            shxxService.insert(shxx);
                        }
                    }else{
                        //根据审核时间和人员判断是否存在审核信息，存在不做处理，不存在新增
                        if (StringUtil.isNotBlank(finishTime)){
                            ShxxDto shxxDtoT = new ShxxDto();
                            shxxDtoT.setYwid(gfyzglDto.getGfyzid());
                            shxxDtoT = shxxService.getShxxByYwid(shxxDtoT);
                            if(shxxDtoT!=null){
                                shxxDtoT.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                                shxxDtoT.setLrry(user.getYhid());
                                ShxxDto shxxDtoR = shxxService.getShxxByYwid(shxxDtoT);
                                if(shxxDtoR==null){
                                    shxxDtoT.setShxxid(StringUtil.generateUUID());
                                    shxxDtoT.setXh(String.valueOf(Integer.parseInt(shxxDtoT.getXh())+1));
                                    shxxDtoT.setSftg("1");
                                    shxxDtoT.setShyj(remark);
                                    shxxService.insert(shxxDtoT);
                                }
                            }
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

    /**
     * @Description: 更新是否完成
     * @param gfyzglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/26 15:44
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateList(GfyzglDto gfyzglDto) {
        return dao.updateList(gfyzglDto);
    }

    /**
     * @Description: 查询审核信息数据
     * @param gfyzid
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @Author: 郭祥杰
     * @Date: 2024/6/28 17:51
     */
    @Override
    public Map<String, String> queryShxxMap(String gfyzid) {
        Map<String, String> map = new HashMap<>();
        ShxxDto shxxDto = new ShxxDto();
        shxxDto.setYwid(gfyzid);
        shxxDto.setShlb(AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode());
        List<ShxxDto> shxxDtoList = shxxService.getShxxLsit(shxxDto);
        int i = (shxxDtoList!=null && !shxxDtoList.isEmpty())?shxxDtoList.size():0;
        map.put("scbqryj",(shxxDtoList!=null && !shxxDtoList.isEmpty() && i>=4)?
                StringUtil.isNotBlank(shxxDtoList.get(i-4).getShyj())?shxxDtoList.get(i-4).getShyj()
                        +"\t(姓名："+shxxDtoList.get(i-4).getShrmc()+"\t日期:"+shxxDtoList.get(i-4).getShsj()+")"
                        :"同意。\t(姓名："+shxxDtoList.get(i-4).getShrmc()+"\t日期:"+shxxDtoList.get(i-4).getShsj()+")":"/");
        map.put("yfbqryj",(shxxDtoList!=null && !shxxDtoList.isEmpty() && i>=3)?
                StringUtil.isNotBlank(shxxDtoList.get(i-3).getShyj())?shxxDtoList.get(i-3).getShyj()
                        +"\t(姓名："+shxxDtoList.get(i-3).getShrmc()+"\t日期:"+shxxDtoList.get(i-3).getShsj()+")"
                        :"同意。\t(姓名："+shxxDtoList.get(i-3).getShrmc()+"\t日期:"+shxxDtoList.get(i-3).getShsj()+")":"/");
        map.put("zlbqryj",(shxxDtoList!=null && !shxxDtoList.isEmpty() && i>=2)?
                StringUtil.isNotBlank(shxxDtoList.get(i-2).getShyj())?shxxDtoList.get(i-2).getShyj()
                        +"\t(姓名："+shxxDtoList.get(i-2).getShrmc()+"\t日期:"+shxxDtoList.get(i-2).getShsj()+")"
                        :"同意。\t(姓名："+shxxDtoList.get(i-2).getShrmc()+"\t日期:"+shxxDtoList.get(i-2).getShsj()+")":"/");
        map.put("glzdbqryj",(shxxDtoList!=null && !shxxDtoList.isEmpty())?
                StringUtil.isNotBlank(shxxDtoList.get(i-1).getShyj())?shxxDtoList.get(i-1).getShyj()
                        +"\t(姓名："+shxxDtoList.get(i-1).getShrmc()+"\t日期:"+shxxDtoList.get(i-1).getShsj()+")"
                        :"同意。\t(姓名："+shxxDtoList.get(i-1).getShrmc()+"\t日期:"+shxxDtoList.get(i-1).getShsj()+")":"/");
        StringBuilder sybmqryj = new StringBuilder();
        if(shxxDtoList!=null && shxxDtoList.size()>4){
            for (int j = 0;j<shxxDtoList.size()-4;j++){
                sybmqryj.append(StringUtil.isNotBlank(shxxDtoList.get(j).getShyj())?
                        shxxDtoList.get(j).getShyj()+"\t(姓名："+shxxDtoList.get(j).getShrmc()+"\t日期:"+shxxDtoList.get(j).getShsj()+")\n"
                        :"同意。\t(姓名："+shxxDtoList.get(j).getShrmc()+"\t日期:"+shxxDtoList.get(j).getShsj()+")\n");
            }
        }
        shxxDto.setShlb(AuditTypeEnum.AUDIT_VERIFICATION_AUDIT.getCode());
        List<ShxxDto> shxxDtos = shxxService.getShxxLsit(shxxDto);
        map.put("sybmqryj",StringUtil.isNotBlank(sybmqryj)?sybmqryj.toString():"/");
        map.put("scbyj",(shxxDtos!=null && !shxxDtos.isEmpty())?
                StringUtil.isNotBlank(shxxDtos.get(0).getShyj())?shxxDtos.get(0).getShyj()
                        +"\t(姓名："+shxxDtos.get(0).getShrmc()+"\t日期:"+shxxDtos.get(0).getShsj()+")"
                        :"合格。\t(姓名："+shxxDtos.get(0).getShrmc()+"\t日期:"+shxxDtos.get(0).getShsj()+")":"/");
        map.put("yfbyj",(shxxDtos!=null && !shxxDtos.isEmpty() && shxxDtos.size()>=2)?
                StringUtil.isNotBlank(shxxDtos.get(1).getShyj())?shxxDtos.get(1).getShyj()
                        +"\t(姓名："+shxxDtos.get(1).getShrmc()+"\t日期:"+shxxDtos.get(1).getShsj()+")"
                        :"合格。\t(姓名："+shxxDtos.get(1).getShrmc()+"\t日期:"+shxxDtos.get(1).getShsj()+")":"/");
        int t = (shxxDtos!=null && !shxxDtos.isEmpty())?shxxDtos.size():0;
        map.put("zlbyj",(shxxDtos!=null && !shxxDtos.isEmpty() && shxxDtos.size()>=4)?
                StringUtil.isNotBlank(shxxDtos.get(t-2).getShyj())?shxxDtos.get(t-2).getShyj()
                        +"\t(姓名："+shxxDtos.get(t-2).getShrmc()+"\t日期:"+shxxDtos.get(t-2).getShsj()+")"
                        :"合格。\t(姓名："+shxxDtos.get(t-2).getShrmc()+"\t日期:"+shxxDtos.get(t-2).getShsj()+")":"/");
        map.put("glzdbyj",(shxxDtos!=null && !shxxDtos.isEmpty() && shxxDtos.size()>=4)?
                StringUtil.isNotBlank(shxxDtos.get(t-1).getShyj())?shxxDtos.get(t-1).getShyj()
                        +"\t(姓名："+shxxDtos.get(t-1).getShrmc()+"\t日期:"+shxxDtos.get(t-1).getShsj()+")"
                        :"合格。\t(姓名："+shxxDtos.get(t-1).getShrmc()+"\t日期:"+shxxDtos.get(t-1).getShsj()+")":"/");
        StringBuilder sybmyj = new StringBuilder();
        if(shxxDtos!=null && shxxDtos.size()>4){
            for (int k = 2;k<shxxDtos.size()-2;k++){
                sybmyj.append(StringUtil.isNotBlank(shxxDtos.get(k).getShyj())?
                        shxxDtos.get(k).getShyj()+"\t(姓名："+shxxDtos.get(k).getShrmc()+"\t日期:"+shxxDtos.get(k).getShsj()+")\n"
                        :"合格。\t(姓名："+shxxDtos.get(k).getShrmc()+"\t日期:"+shxxDtos.get(k).getShsj()+")\n");
            }
        }
        map.put("sybmyj",StringUtil.isNotBlank(sybmyj)?sybmyj.toString():"/");
        return map;
    }
}
