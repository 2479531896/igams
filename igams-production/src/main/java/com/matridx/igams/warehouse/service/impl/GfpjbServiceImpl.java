package com.matridx.igams.warehouse.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.DdAuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.warehouse.dao.entities.GfpjbDto;
import com.matridx.igams.warehouse.dao.entities.GfpjbModel;
import com.matridx.igams.warehouse.dao.entities.GfpjmxDto;
import com.matridx.igams.warehouse.dao.entities.GfyzglDto;
import com.matridx.igams.warehouse.dao.post.IGfpjbDao;
import com.matridx.igams.warehouse.service.svcinterface.IGfpjbService;
import com.matridx.igams.warehouse.service.svcinterface.IGfpjmxService;
import com.matridx.igams.warehouse.service.svcinterface.IGfyzglService;
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
import java.util.stream.Collectors;

/**
 * @author jld
 */
@Service
public class GfpjbServiceImpl extends BaseBasicServiceImpl<GfpjbDto, GfpjbModel, IGfpjbDao> implements IGfpjbService, IAuditService {
    @Autowired
    IShgcService shgcService;
    @Autowired
    IJgxxService jgxxService;
    @Autowired
    IGfpjmxService gfpjmxService;
    @Autowired
    IGfyzglService gfyzglService;
    @Autowired
    IShxxService shxxService;
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
    IJcsjService jcsjService;
    @Value("${matridx.wechat.registerurl:}")
    private String registerurl;
    @Value("${matridx.systemflg.systemname:}")
    private String systemname;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;

    private final Logger log = LoggerFactory.getLogger(GfpjbServiceImpl.class);

    /** 列表
     * @Description:
     * @param gfpjbDto
     * @return java.util.List<com.matridx.igams.warehouse.dao.entities.GfpjbDto>
     * @Author: 郭祥杰
     * @Date: 2024/6/25 17:40
     */
    @Override
    public List<GfpjbDto> getPagedGfpjbDto(GfpjbDto gfpjbDto) {
        List<GfpjbDto> list = dao.getPagedDtoList(gfpjbDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_SUPPLIER_EVALUATION.getCode(), "zt", "gfpjid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    /**
     * @Description: 生产记录编号
     * @param jgid
     * @return java.lang.String
     * @Author: 郭祥杰
     * @Date: 2024/6/25 17:41
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
     * @Description: 供方评价新增
     * @param gfpjbDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/26 10:16
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertAppraise(GfpjbDto gfpjbDto) throws BusinessException {
        gfpjbDto.setGfpjid(StringUtil.generateUUID());
        List<GfpjmxDto> gfpjmxDtoList = JSON.parseArray(gfpjbDto.getGfpjmxJson(),GfpjmxDto.class);
        if(gfpjmxDtoList!=null && !gfpjmxDtoList.isEmpty()){
            for (GfpjmxDto gfpjmxDto : gfpjmxDtoList){
                gfpjmxDto.setPjmxid(StringUtil.generateUUID());
                gfpjmxDto.setGfpjid(gfpjbDto.getGfpjid());
                gfpjmxDto.setLrry(gfpjbDto.getLrry());
            }
            gfpjbDto.setZt(StatusEnum.CHECK_NO.getCode());
            boolean result = dao.insert(gfpjbDto)!=0;
            if(!result){
                throw new BusinessException("msg","新增供方评价失败!");
            }
            result = gfpjmxService.insetGfpjmxDtoList(gfpjmxDtoList);
            if(!result){
                throw new BusinessException("msg","新增供方评价明细失败!");
            }
        }else{
            throw new BusinessException("msg","供方评价明细不允许为空!");
        }
        return true;
    }

    /**
     * @Description: 供方评价修改保存
     * @param gfpjbDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/26 14:06
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAppraise(GfpjbDto gfpjbDto) throws BusinessException {
        boolean result = false;
        List<GfpjmxDto> gfpjmxDtoList = JSON.parseArray(gfpjbDto.getGfpjmxJson(),GfpjmxDto.class);
        if(gfpjmxDtoList!=null && !gfpjmxDtoList.isEmpty()){
            List<GfpjmxDto> delgfpjmxDtoList = new ArrayList<>();
            List<GfpjmxDto> modGfpjmxDtoList = new ArrayList<>();
            List<GfpjmxDto> addGfpjmxDtoList = new ArrayList<>();
            GfpjmxDto gfpjmxDto = new GfpjmxDto();
            gfpjmxDto.setGfpjid(gfpjbDto.getGfpjid());
            List<GfpjmxDto> gfpjmxDtos = gfpjmxService.getDtoList(gfpjmxDto);
            if(gfpjmxDtos!=null && !gfpjmxDtos.isEmpty()){
                delgfpjmxDtoList = gfpjmxDtos.stream()
                        .filter(item -> !gfpjmxDtoList.stream()
                                .map(GfpjmxDto::getPjmxid)
                                .collect(Collectors.toList())
                                .contains(item.getPjmxid()))
                        .collect(Collectors.toList());
                addGfpjmxDtoList = gfpjmxDtoList.stream()
                        .filter(item -> !gfpjmxDtos.stream()
                                .map(GfpjmxDto::getPjmxid)
                                .collect(Collectors.toList())
                                .contains(item.getPjmxid()))
                        .collect(Collectors.toList());
                modGfpjmxDtoList = gfpjmxDtoList.stream()
                        .filter(item -> gfpjmxDtos.stream()
                                .map(GfpjmxDto::getPjmxid)
                                .collect(Collectors.toList())
                                .contains(item.getPjmxid()))
                        .collect(Collectors.toList());
            }else {
                addGfpjmxDtoList = gfpjmxDtoList;
            }
            if(!addGfpjmxDtoList.isEmpty()){
                for (GfpjmxDto gfpjmx:addGfpjmxDtoList){
                    gfpjmx.setGfpjid(gfpjbDto.getGfpjid());
                    gfpjmx.setPjmxid(StringUtil.generateUUID());
                    gfpjmx.setLrry(gfpjbDto.getXgry());
                }
                result = gfpjmxService.insetGfpjmxDtoList(addGfpjmxDtoList);
                if(!result){
                    throw new BusinessException("msg","供方评价明细新增失败!");
                }
            }
            if(!delgfpjmxDtoList.isEmpty()){
                for (GfpjmxDto gfpjmx:delgfpjmxDtoList){
                    gfpjmx.setScsj("1");
                    gfpjmx.setScry(gfpjbDto.getXgry());
                    gfpjmx.setScbj("1");
                }
                modGfpjmxDtoList.addAll(delgfpjmxDtoList);
            }
            if(!modGfpjmxDtoList.isEmpty()){
                for (GfpjmxDto gfpjmx:modGfpjmxDtoList){
                    gfpjmx.setXgry(gfpjbDto.getXgry());
                }
                result = gfpjmxService.updateGfyzmxDtoList(modGfpjmxDtoList);
                if(!result){
                    throw new BusinessException("msg","供方评价明细修改失败!");
                }
            }
            result = dao.update(gfpjbDto)>0;
            if(!result){
                throw new BusinessException("msg","供方评价修改失败!");
            }
        }else {
            throw new BusinessException("msg","供方评价明细不允许为空!");
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delAppraise(GfpjbDto gfpjbDto) throws BusinessException {
        boolean result = dao.delete(gfpjbDto)>0;
        if(!result){
            throw new BusinessException("msg","删除供方评价失败!");
        }
        GfyzglDto gfyzglDto = new GfyzglDto();
        gfyzglDto.setIds(gfpjbDto.getGfyzids());
        result = gfyzglService.updateList(gfyzglDto);
        if(!result){
            throw new BusinessException("msg","更新供方验证完成标记失败!");
        }
        GfpjmxDto gfpjmxDto = new GfpjmxDto();
        gfpjmxDto.setIds(gfpjbDto.getIds());
        gfpjmxDto.setScry(gfpjbDto.getScry());
        result = gfpjmxService.delete(gfpjmxDto);
        if(!result){
            throw new BusinessException("msg","删除供方评价明细失败!");
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        GfpjbDto gfpjbDto = (GfpjbDto) baseModel;
        gfpjbDto.setXgry(operator.getYhid());
        if(StringUtil.isNotBlank(gfpjbDto.getGfpjmxJson())){
            boolean result = updateAppraise(gfpjbDto);
            if(!result){
                throw new BusinessException("msg","供方评价审核修改失败!");
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
            GfpjbDto gfpjbDto = new GfpjbDto();
            gfpjbDto.setGfpjid(shgcDto.getYwid());
            gfpjbDto.setXgry(operator.getYhid());
            GfpjbDto gfpjbDtoT = getDtoById(shgcDto.getYwid());
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                gfpjbDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
            }else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())){
                gfpjbDto.setZt(StatusEnum.CHECK_PASS.getCode());
                //更新是否完成
                GfyzglDto gfyzglDto = new GfyzglDto();
                gfyzglDto.setGfyzid(gfpjbDtoT.getGfyzid());
                gfyzglDto.setSfwc("1");
                boolean result = gfyzglService.update(gfyzglDto);
                if(!result){
                    throw new BusinessException("msg","更新完成标记失败！");
                }
            }else{
                gfpjbDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if ("1".equals(shgcDto.getXlcxh())&& shgcDto.getShxx()==null){
                    try{
                        // 根据申请部门查询审核流程
                        List<DdxxglDto> ddxxglDtos = ddxxglService.getProcessByJgid(gfpjbDtoT.getJgid(),  DdAuditTypeEnum.SP_GFPJ.getCode());
                        if(CollectionUtils.isEmpty(ddxxglDtos)){
                            throw new BusinessException("msg","未找到所属部门审核流程！");
                        }
                        if(ddxxglDtos.size() > 1){
                            throw new BusinessException("msg","找到多条所属部门审核流程！");
                        }
                        String approvers = ddxxglDtos.get(0).getSpr();
                        if(StringUtil.isNotBlank(gfpjbDtoT.getShrddid())){
                            approvers = approvers.replace("|", ","+gfpjbDtoT.getShrddid()+",");
                        }else{
                            approvers = approvers.replace("|", ",");
                        }
                        String csr = StringUtil.isNotBlank(ddxxglDtos.get(0).getCsr())?ddxxglDtos.get(0).getCsr():"";
                        Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "供方评价");//获取审批模板ID
                        String templateCode=(String) template.get("message");
                        Map<String,String> map=new HashMap<>();
                        map.put("所属公司",systemname);
                        map.put("供方名称",gfpjbDtoT.getGfmc());
                        map.put("供方编号",gfpjbDtoT.getGfbh());
                        map.put("联系人",gfpjbDtoT.getLxr());
                        map.put("电话",gfpjbDtoT.getDh());
                        map.put("传真",StringUtil.isNotBlank(gfpjbDtoT.getCz())?gfpjbDtoT.getCz():"");
                        map.put("供方地址",gfpjbDtoT.getDz());
                        map.put("申请人",gfpjbDtoT.getSqrmc());
                        map.put("申请时间",gfpjbDtoT.getPjsj());
                        map.put("资质文件情况",gfpjbDtoT.getZzwjqk());
                        map.put("质量情况",gfpjbDtoT.getZlqk());
                        map.put("价格情况",gfpjbDtoT.getJgqk());
                        map.put("供货周期情况",gfpjbDtoT.getGhzqqk());
                        map.put("现场审核情况",gfpjbDtoT.getXcshqk());
                        map.put("结论",gfpjbDtoT.getJl());
                        List<String> glspds = new ArrayList<>();//关联审批单
                        glspds.add(gfpjbDtoT.getYzddslid());
                        glspds.add(gfpjbDtoT.getSqddslid());
                        map.put("关联审批单", JSON.toJSONString(glspds));
                        map.put("供方物资信息",applicationurl+urlPrefix+"/ws/appraise/appraiseView?gfpjid="+gfpjbDtoT.getGfpjid());

                        Map<String,Object> tMap=talkUtil.createInstance(operator.getYhm(), templateCode, approvers, csr, gfpjbDtoT.getDdid(), gfpjbDtoT.getJgid(), map,null,null);
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
                                gfpjbDto.setDdslid(String.valueOf(resultMap.get("process_instance_id")));
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
            update(gfpjbDto);
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
                GfpjbDto gfpjbDto = new GfpjbDto();
                gfpjbDto.setGfpjid(shgcDto.getYwid());
                gfpjbDto.setXgry(operator.getYhid());
                gfpjbDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                update(gfpjbDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                //OA取消审批的同时组织钉钉审批
                GfpjbDto gfpjbDto = getDtoById(shgcDto.getYwid());

                if(gfpjbDto!=null && StringUtils.isNotBlank(gfpjbDto.getDdslid())) {
                    Map<String,Object> cancelResult=
                            talkUtil.cancelDingtalkAudit(operator.getYhm(), gfpjbDto.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    Map<String,Object> resultMap=JSON.parseObject(success,Map.class);
                    boolean result= (boolean) resultMap.get("success");
                    if(result){
                        GfpjbDto gfpjbDtoT = new GfpjbDto();
                        gfpjbDtoT.setGfpjid(shgcDto.getYwid());
                        gfpjbDtoT.setXgry(operator.getYhid());
                        gfpjbDtoT.setZt(StatusEnum.CHECK_NO.getCode());
                        gfpjbDtoT.setUpdateFlg("0");
                        update(gfpjbDtoT);
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
        GfpjbDto gfpjbDto = new GfpjbDto();
        gfpjbDto.setIds(ids);
        List<GfpjbDto> gfpjbDtoList = dao.getDtoList(gfpjbDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(gfpjbDtoList)){
            for(GfpjbDto dto:gfpjbDtoList){
                list.add(dto.getGfpjid());
            }
        }
        map.put("list",list);
        return map;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean callbackAppraiseAduit(JSONObject object, HttpServletRequest request, Map<String, Object> map) throws BusinessException {
        GfpjbDto gfpjbDto = new GfpjbDto();
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
            gfpjbDto.setDdslid(processInstanceId);
            gfpjbDto = dao.getDto(gfpjbDto);
            // 若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if (gfpjbDto != null) {
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
                List<GfpjbDto> gfpjbDtoList = dao.getSprjsBySprid(user.getYhid());
                // 获取当前审核过程
                ShgcDto tShgcDto = shgcService.getDtoByYwid(gfpjbDto.getGfpjid());
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
                    if (!CollectionUtils.isEmpty(gfpjbDtoList)) {
                        // 审批正常结束（同意或拒绝）
                        ShxxDto shxxDto = new ShxxDto();
                        shxxDto.setGcid(tShgcDto.getGcid());
                        shxxDto.setLcxh(tShgcDto.getXlcxh());
                        shxxDto.setShlb(tShgcDto.getShlb());
                        shxxDto.setShyj(remark);
                        shxxDto.setAuditType(tShgcDto.getShlb());
                        shxxDto.setYwid(gfpjbDto.getGfpjid());
                        if (StringUtil.isNotBlank(finishTime)){
                            shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                        }
                        log.error("shxxDto-lcxh:" + tShgcDto.getXlcxh() + " shxxDto-shlb:"
                                + tShgcDto.getShlb() + " shxxDto-shyj:" + remark + " shxxDto-ywid:"
                                + gfpjbDto.getGfpjid());
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
                                        + gfpjbDto.getGfpjid() + ",转发时间:" + date);
                                return true;
                            }
                            // 调用审核方法
                            Map<String, List<String>> mapT = ToolUtil.reformRequest(request);
                            log.error("map:"+mapT);
                            List<String> list = new ArrayList<>();
                            list.add(gfpjbDto.getGfpjid());
                            mapT.put("htid", list);
                            for(int i=0;i<gfpjbDtoList.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(gfpjbDtoList.get(i).getSprjsid());
                                    user.setDqjsmc(gfpjbDtoList.get(i).getSprjsmc());
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
                                    if(i==gfpjbDtoList.size()-1){
                                        throw new BusinessException("msg",e.getMessage());
                                    }
                                }
                            }
                        }
                        // 撤销审批
                        else if (("terminate").equals(type)) {
                            shxxDto.setThlcxh(null);
                            shxxDto.setYwglmc(gfpjbDto.getGfmc());
                            GfpjbDto gfpjbDtoT = new GfpjbDto();
                            gfpjbDtoT.setGfpjid(gfpjbDto.getGfpjid());
                            gfpjbDtoT.setUpdateFlg("0");
                            gfpjbDtoT.setZt(StatusEnum.CHECK_NO.getCode());
                            update(gfpjbDtoT);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            // 调用审核方法
                            Map<String, List<String>> mapY = ToolUtil.reformRequest(request);
                            List<String> list = new ArrayList<>();
                            list.add(gfpjbDto.getGfpjid());
                            mapY.put("gfyzid", list);
                            for(int i=0;i<gfpjbDtoList.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(gfpjbDtoList.get(i).getSprjsid());
                                    user.setDqjsmc(gfpjbDtoList.get(i).getSprjsmc());
                                    shxxDto.setYwids(new ArrayList<>());
                                    shgcService.terminateAudit(shxxDto, user,request,lastlcxh,object);
                                    //更新审批管理信息
                                    ddspglDto.setCljg("1");
                                    ddspglService.updatecljg(ddspglDto);
                                    break;
                                } catch (Exception e) {
                                    map.put("ddspglid", ddspglDto.getDdspglid());
                                    map.put("processInstanceId", ddspglDto.getProcessinstanceid());
                                    if(i==gfpjbDtoList.size()-1){
                                        throw new BusinessException("msg",e.toString());
                                    }
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
                        shxx.setYwid(gfpjbDto.getGfpjid());
                        shxx.setShlb(AuditTypeEnum.AUDIT_SUPPLIER_EVALUATION.getCode());
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
                            shxxDtoT.setYwid(gfpjbDto.getGfpjid());
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

    @Override
    public GfpjbDto getDtoByGfyzId(String gfyzid) {
        return dao.getDtoByGfyzId(gfyzid);
    }
}
