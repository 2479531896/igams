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
import com.matridx.igams.common.enums.BasicDataTypeEnum;
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
import com.matridx.igams.warehouse.dao.entities.GfjxglDto;
import com.matridx.igams.warehouse.dao.entities.GfjxglModel;
import com.matridx.igams.warehouse.dao.entities.GfjxmxDto;
import com.matridx.igams.warehouse.dao.entities.GysxxDto;
import com.matridx.igams.warehouse.dao.post.IGfjxglDao;
import com.matridx.igams.warehouse.service.svcinterface.IGfjxglService;
import com.matridx.igams.warehouse.service.svcinterface.IGfjxmxService;
import com.matridx.igams.warehouse.service.svcinterface.IGysxxService;
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
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
@Service
public class GfjxglServiceImpl extends BaseBasicServiceImpl<GfjxglDto, GfjxglModel, IGfjxglDao> implements IGfjxglService, IAuditService {
    @Autowired
    IShgcService shgcService;
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
    IJgxxService jgxxService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IShxxService shxxService;
    @Autowired
    IGfjxmxService gfjxmxService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IGysxxService gysxxService;
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
     * @Description: 获取供方绩效列表数据
     * @param gfjxglDto
     * @return java.util.List<com.matridx.igams.warehouse.dao.entities.GfjxglDto>
     * @Author: 郭祥杰
     * @Date: 2024/7/2 17:10
     */
    @Override
    public List<GfjxglDto> getPagedPerformance(GfjxglDto gfjxglDto) {
        List<GfjxglDto> list = dao.getPagedDtoList(gfjxglDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_SUPPLIER_PERFORMANCE.getCode(), "zt", "gfjxid",
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
     * @Date: 2024/7/4 15:55
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
     * @Description: 新增保存
     * @param gfjxglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/7/3 17:25
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertPerpromance(GfjxglDto gfjxglDto) throws BusinessException {
        gfjxglDto.setGfjxid(StringUtil.generateUUID());
        gfjxglDto.setZt(StatusEnum.CHECK_NO.getCode());
        List<GfjxmxDto> gfjxmxDtoList = JSON.parseArray(gfjxglDto.getGfjxmxJson(),GfjxmxDto.class);
        BigDecimal mf = new BigDecimal("0");
        BigDecimal df = new BigDecimal("0");
        if(gfjxmxDtoList!=null && !gfjxmxDtoList.isEmpty()){
            for (GfjxmxDto gfjxmxDto:gfjxmxDtoList){
                gfjxmxDto.setGfjxid(gfjxglDto.getGfjxid());
                gfjxmxDto.setLrry(gfjxglDto.getLrry());
                gfjxmxDto.setJxmxid(StringUtil.generateUUID());
                if(StringUtil.isBlank(gfjxmxDto.getDfbj())){
                    mf = mf.add(new BigDecimal(StringUtil.isNotBlank(gfjxmxDto.getBizfs())?gfjxmxDto.getBizfs():"0"));
                    df = df.add(new BigDecimal(StringUtil.isNotBlank(gfjxmxDto.getDf())?gfjxmxDto.getDf():"0"));
                }
            }
        }else {
            throw new BusinessException("msg","供方绩效项目不允许为空!");
        }
        gfjxglDto.setDf(String.valueOf(df));
        gfjxglDto.setMf(String.valueOf(mf));
        BigDecimal dflDec = df.divide(mf, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        gfjxglDto.setDfl(dflDec.toString());
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.EVALUATION_GRADE});
        List<JcsjDto> jcsjDtoList = jclist.get(BasicDataTypeEnum.EVALUATION_GRADE.getCode());
        String pj = "";
        for(JcsjDto jcsjDto:jcsjDtoList){
            BigDecimal minString = new BigDecimal("0");
            BigDecimal maxString = new BigDecimal("0");
            if(StringUtil.isNotBlank(jcsjDto.getCskz1())){
                minString = new BigDecimal(jcsjDto.getCskz1());
            }
            if(StringUtil.isNotBlank(jcsjDto.getCskz2())){
                maxString = new BigDecimal(jcsjDto.getCskz2());
            }
            int minResult = dflDec.compareTo(minString);
            int maxResult = dflDec.compareTo(maxString);
            if(minResult>=0 && maxResult<=0){
                pj=jcsjDto.getCsid();
                break;
            }
        }
        gfjxglDto.setPj(pj);
        boolean result = dao.insert(gfjxglDto)!=0;
        if(!result){
            throw new BusinessException("msg","新增供方绩效失败!");
        }
        GysxxDto gysxxDto = new GysxxDto();
        gysxxDto.setXgry(gfjxglDto.getLrry());
        gysxxDto.setIds(gfjxglDto.getGysid());
        gysxxDto.setJxflg(gfjxglDto.getKhjssj());
        result = gysxxService.update(gysxxDto);
        if(!result){
            throw new BusinessException("msg","修改供方绩效标记失败!");
        }
        result = gfjxmxService.insertGfjxmxDtos(gfjxmxDtoList);
        if(!result){
            throw new BusinessException("msg","新增供方绩效明细失败!");
        }
        return true;
    }

    /**
     * @Description: 修改保存
     * @param gfjxglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/7/4 14:00
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePerpromance(GfjxglDto gfjxglDto) throws BusinessException {
        List<GfjxmxDto> gfjxmxDtoList = JSON.parseArray(gfjxglDto.getGfjxmxJson(),GfjxmxDto.class);
        BigDecimal mf = new BigDecimal("0");
        BigDecimal df = new BigDecimal("0");
        if(gfjxmxDtoList!=null && !gfjxmxDtoList.isEmpty()){
            for (GfjxmxDto gfjxmxDto:gfjxmxDtoList){
                gfjxmxDto.setXgry(gfjxglDto.getXgry());
                if(StringUtil.isBlank(gfjxmxDto.getDfbj())){
                    mf = mf.add(new BigDecimal(StringUtil.isNotBlank(gfjxmxDto.getBizfs())?gfjxmxDto.getBizfs():"0"));
                    df = df.add(new BigDecimal(StringUtil.isNotBlank(gfjxmxDto.getDf())?gfjxmxDto.getDf():"0"));
                }
            }
        }else {
            throw new BusinessException("msg","供方绩效明细不允许为空!");
        }
        gfjxglDto.setDf(String.valueOf(df));
        gfjxglDto.setMf(String.valueOf(mf));
        BigDecimal dflDec = df.divide(mf, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
        gfjxglDto.setDfl(dflDec.toString());
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.EVALUATION_GRADE});
        List<JcsjDto> jcsjDtoList = jclist.get(BasicDataTypeEnum.EVALUATION_GRADE.getCode());
        String pj = "";
        for(JcsjDto jcsjDto:jcsjDtoList){
            BigDecimal minString = new BigDecimal("0");
            BigDecimal maxString = new BigDecimal("0");
            if(StringUtil.isNotBlank(jcsjDto.getCskz1())){
                minString = new BigDecimal(jcsjDto.getCskz1());
            }
            if(StringUtil.isNotBlank(jcsjDto.getCskz2())){
                maxString = new BigDecimal(jcsjDto.getCskz2());
            }
            int minResult = dflDec.compareTo(minString);
            int maxResult = dflDec.compareTo(maxString);
            if(minResult>=0 && maxResult<=0){
                pj=jcsjDto.getCsid();
                break;
            }
        }
        gfjxglDto.setPj(pj);
        GysxxDto gysxxDto = new GysxxDto();
        gysxxDto.setXgry(gfjxglDto.getLrry());
        gysxxDto.setIds(gfjxglDto.getGysid());
        gysxxDto.setJxflg(gfjxglDto.getKhjssj());
        boolean result = gysxxService.update(gysxxDto);
        if(!result){
            throw new BusinessException("msg","修改供方绩效标记失败!");
        }
        result = dao.update(gfjxglDto)!=0;
        if(!result){
            throw new BusinessException("msg","修改供方绩效失败!");
        }
        result = gfjxmxService.updateGfjxmxDtos(gfjxmxDtoList);
        if(!result){
            throw new BusinessException("msg","修改供方绩效明细失败!");
        }
        return true;
    }

    /**
     * @Description: 删除
     * @param gfjxglDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/7/4 14:15
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delPerformance(GfjxglDto gfjxglDto) throws BusinessException {
        boolean result = dao.delete(gfjxglDto)>0;
        if(!result){
            throw new BusinessException("msg","删除供方绩效失败!");
        }
        GfjxmxDto gfjxmxDto = new GfjxmxDto();
        gfjxmxDto.setScry(gfjxglDto.getScry());
        gfjxmxDto.setIds(gfjxglDto.getIds());
        result = gfjxmxService.delete(gfjxmxDto);
        if(!result){
            throw new BusinessException("msg","删除供方绩效明细失败!");
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        GfjxglDto gfjxglDto = (GfjxglDto) baseModel;
        gfjxglDto.setXgry(operator.getYhid());
        if(StringUtil.isNotBlank(gfjxglDto.getGfjxmxJson())){
            boolean result = updatePerpromance(gfjxglDto);
            if(!result){
                throw new BusinessException("msg","供方绩效修改失败!");
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
            GfjxglDto gfjxglDto = new GfjxglDto();
            gfjxglDto.setGfjxid(shgcDto.getYwid());
            gfjxglDto.setXgry(operator.getYhid());
            GfjxglDto gfjxglDtoT = getDtoById(shgcDto.getYwid());
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                gfjxglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
            }else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())){
                gfjxglDto.setZt(StatusEnum.CHECK_PASS.getCode());
            }else{
                gfjxglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if ("1".equals(shgcDto.getXlcxh())&& shgcDto.getShxx()==null){
                    try {
                        // 根据申请部门查询审核流程
                        List<DdxxglDto> ddxxglDtos = ddxxglService.getProcessByJgid(gfjxglDtoT.getJgid(), DdAuditTypeEnum.SP_GFJX.getCode());
                        if(CollectionUtils.isEmpty(ddxxglDtos)){
                            throw new BusinessException("msg","未找到所属部门审核流程！");
                        }
                        if(ddxxglDtos.size() > 1){
                            throw new BusinessException("msg","找到多条所属部门审核流程！");
                        }
                        String approvers = ddxxglDtos.get(0).getSpr();
                        if("1".equals(gfjxglDtoT.getKhxzCskz1())){
                            approvers = approvers.substring(0, approvers.indexOf("/"));
                        }else{
                            approvers = approvers.replace("/", ",");
                            if(StringUtil.isNotBlank(gfjxglDtoT.getShrddid())){
                                approvers = approvers.replace("|", ","+gfjxglDtoT.getShrddid()+",");
                            }else {
                                approvers = approvers.replace("|", ",");
                            }
                        }
                        String csr = StringUtil.isNotBlank(ddxxglDtos.get(0).getCsr())?ddxxglDtos.get(0).getCsr():"";
                        Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "供方绩效审核");//获取审批模板ID
                        String templateCode=(String) template.get("message");
                        Map<String,String> map=new HashMap<>();
                        //所属公司，申请人，供方名称，考核性质，销售额，考核周期，供方联系人，供方电话，所供产品/服务，主要客户，评估项目及内容。
                        map.put("所属公司",systemname);
                        map.put("申请人",gfjxglDtoT.getLrrymc());
                        map.put("供方名称",gfjxglDtoT.getGfmc());
                        map.put("考核性质",gfjxglDtoT.getKhxzmc());
                        map.put("销售额",StringUtil.isNotBlank(gfjxglDtoT.getXse())?gfjxglDtoT.getXse():"");
                        map.put("考核周期",gfjxglDtoT.getKhkssj()+"~"+gfjxglDtoT.getKhjssj());
                        map.put("供方联系人",gfjxglDtoT.getLxr());
                        map.put("供方电话",gfjxglDtoT.getDh());
                        map.put("所供产品/服务",gfjxglDtoT.getSgcp());
                        map.put("主要客户",gfjxglDtoT.getZykh());
                        map.put("结论",gfjxglDtoT.getJl());
                        map.put("评估项目及内容",applicationurl+urlPrefix+"/ws/performance/performanceView?gfjxid="+gfjxglDtoT.getGfjxid());
                        Map<String,Object> tMap=talkUtil.createInstance(operator.getYhm(), templateCode, approvers, csr, gfjxglDtoT.getDdid(), gfjxglDtoT.getJgid(), map,null,null);
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
                                gfjxglDto.setDdslid(String.valueOf(resultMap.get("process_instance_id")));
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
            update(gfjxglDto);
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
                GfjxglDto gfjxglDto = new GfjxglDto();
                gfjxglDto.setGfjxid(shgcDto.getYwid());
                gfjxglDto.setXgry(operator.getYhid());
                gfjxglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                update(gfjxglDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                //OA取消审批的同时组织钉钉审批
                GfjxglDto gfjxglDtoT = getDtoById(shgcDto.getYwid());

                if(gfjxglDtoT!=null && StringUtils.isNotBlank(gfjxglDtoT.getDdslid())) {
                    Map<String,Object> cancelResult=
                            talkUtil.cancelDingtalkAudit(operator.getYhm(), gfjxglDtoT.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    Map<String,Object> resultMap=JSON.parseObject(success,Map.class);
                    boolean result= (boolean) resultMap.get("success");
                    GfjxglDto gfjxglDto = new GfjxglDto();
                    gfjxglDto.setGfjxid(shgcDto.getYwid());
                    gfjxglDto.setXgry(operator.getYhid());
                    gfjxglDto.setZt(StatusEnum.CHECK_NO.getCode());
                    gfjxglDto.setUpdateFlg("0");
                    update(gfjxglDto);
                    if(!result){
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
        GfjxglDto gfjxglDto = new GfjxglDto();
        gfjxglDto.setIds(ids);
        List<GfjxglDto> gfjxglDtoList = dao.getDtoList(gfjxglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(gfjxglDtoList)){
            for(GfjxglDto dto:gfjxglDtoList){
                list.add(dto.getGfjxid());
            }
        }
        map.put("list",list);
        return map;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean callbackPerformanceAduit(JSONObject object, HttpServletRequest request, Map<String, Object> map) throws BusinessException {
        GfjxglDto gfjxglDto = new GfjxglDto();
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
            gfjxglDto.setDdslid(processInstanceId);
            gfjxglDto = dao.getDto(gfjxglDto);
            // 若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if (gfjxglDto != null) {
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
                List<GfjxglDto> gfjxglDtoList = dao.getSprjsBySprid(user.getYhid());
                // 获取当前审核过程
                ShgcDto tShgcDto = shgcService.getDtoByYwid(gfjxglDto.getGfjxid());
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
                    if (!CollectionUtils.isEmpty(gfjxglDtoList)) {
                        // 审批正常结束（同意或拒绝）
                        ShxxDto shxxDto = new ShxxDto();
                        shxxDto.setGcid(tShgcDto.getGcid());
                        shxxDto.setLcxh(tShgcDto.getXlcxh());
                        shxxDto.setShlb(tShgcDto.getShlb());
                        shxxDto.setShyj(remark);
                        shxxDto.setAuditType(tShgcDto.getShlb());
                        shxxDto.setYwid(gfjxglDto.getGfjxid());
                        if (StringUtil.isNotBlank(finishTime)){
                            shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                        }
                        log.error("shxxDto-lcxh:" + tShgcDto.getXlcxh() + " shxxDto-shlb:"
                                + tShgcDto.getShlb() + " shxxDto-shyj:" + remark + " shxxDto-ywid:"
                                + gfjxglDto.getGfjxid());
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
                                        + gfjxglDto.getGfjxid() + ",转发时间:" + date);
                                return true;
                            }
                            // 调用审核方法
                            Map<String, List<String>> mapT = ToolUtil.reformRequest(request);
                            log.error("map:"+mapT);
                            List<String> list = new ArrayList<>();
                            list.add(gfjxglDto.getGfjxid());
                            mapT.put("gfjxid", list);
                            for(int i=0;i<gfjxglDtoList.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(gfjxglDtoList.get(i).getSprjsid());
                                    user.setDqjsmc(gfjxglDtoList.get(i).getSprjsmc());
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
                                    if(i==gfjxglDtoList.size()-1){
                                        throw new BusinessException("msg",e.getMessage());
                                    }
                                }
                            }
                        }
                        // 撤销审批
                        else if (("terminate").equals(type)) {
                            shxxDto.setThlcxh(null);
                            shxxDto.setYwglmc(gfjxglDto.getGfmc());
                            GfjxglDto gfjxglDtoT = new GfjxglDto();
                            gfjxglDtoT.setGfjxid(gfjxglDto.getGfjxid());
                            gfjxglDtoT.setUpdateFlg("0");
                            gfjxglDtoT.setZt(StatusEnum.CHECK_NO.getCode());
                            update(gfjxglDtoT);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            // 调用审核方法
                            Map<String, List<String>> mapY = ToolUtil.reformRequest(request);
                            List<String> list = new ArrayList<>();
                            list.add(gfjxglDto.getGfjxid());
                            mapY.put("gfjxid", list);
                            for(int i=0;i<gfjxglDtoList.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(gfjxglDtoList.get(i).getSprjsid());
                                    user.setDqjsmc(gfjxglDtoList.get(i).getSprjsmc());
                                    shxxDto.setYwids(new ArrayList<>());
                                    shgcService.terminateAudit(shxxDto, user,request,lastlcxh,object);
                                    //更新审批管理信息
                                    ddspglDto.setCljg("1");
                                    ddspglService.updatecljg(ddspglDto);
                                    break;
                                } catch (Exception e) {
                                    map.put("ddspglid", ddspglDto.getDdspglid());
                                    map.put("processInstanceId", ddspglDto.getProcessinstanceid());
                                    if(i==gfjxglDtoList.size()-1){
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
                        shxx.setYwid(gfjxglDto.getGfjxid());
                        shxx.setShlb(AuditTypeEnum.AUDIT_SUPPLIER_PERFORMANCE.getCode());
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
                            shxxDtoT.setYwid(gfjxglDto.getGfjxid());
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
     * @Description: 查询审核信息
     * @param gfjxid
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @Author: 郭祥杰
     * @Date: 2024/7/5 15:35
     */
    @Override
    public Map<String, String> queryShxxMap(String gfjxid) {
        Map<String, String> map = new HashMap<>();
        ShxxDto shxxDto = new ShxxDto();
        shxxDto.setYwid(gfjxid);
        shxxDto.setShlb(AuditTypeEnum.AUDIT_SUPPLIER_PERFORMANCE.getCode());
        List<ShxxDto> shxxDtos = shxxService.getShxxLsit(shxxDto);
        map.put("cgbyj",(shxxDtos!=null && !shxxDtos.isEmpty())?
                StringUtil.isNotBlank(shxxDtos.get(0).getShyj())?shxxDtos.get(0).getShyj()
                        +"\t(姓名："+shxxDtos.get(0).getShrmc()+"\t日期:"+shxxDtos.get(0).getShsj()+")"
                        :"同意。\t(姓名："+shxxDtos.get(0).getShrmc()+"\t日期:"+shxxDtos.get(0).getShsj()+")":"/");
        map.put("zlbyj",(shxxDtos!=null && !shxxDtos.isEmpty() && shxxDtos.size()>=2)?
                StringUtil.isNotBlank(shxxDtos.get(1).getShyj())?shxxDtos.get(1).getShyj()
                        +"\t(姓名："+shxxDtos.get(1).getShrmc()+"\t日期:"+shxxDtos.get(1).getShsj()+")"
                        :"同意。\t(姓名："+shxxDtos.get(1).getShrmc()+"\t日期:"+shxxDtos.get(1).getShsj()+")":"/");
        map.put("scbyj",(shxxDtos!=null && !shxxDtos.isEmpty() && shxxDtos.size()>=3)?
                StringUtil.isNotBlank(shxxDtos.get(2).getShyj())?shxxDtos.get(2).getShyj()
                        +"\t(姓名："+shxxDtos.get(2).getShrmc()+"\t日期:"+shxxDtos.get(2).getShsj()+")"
                        :"同意。\t(姓名："+shxxDtos.get(2).getShrmc()+"\t日期:"+shxxDtos.get(2).getShsj()+")":"/");
        int t = (shxxDtos!=null && !shxxDtos.isEmpty())?shxxDtos.size():0;
        map.put("glzdbyj",(shxxDtos!=null && !shxxDtos.isEmpty() && shxxDtos.size()>=4)?
                StringUtil.isNotBlank(shxxDtos.get(t-1).getShyj())?shxxDtos.get(t-1).getShyj()
                        +"\t(姓名："+shxxDtos.get(t-1).getShrmc()+"\t日期:"+shxxDtos.get(t-1).getShsj()+")"
                        :"同意。\t(姓名："+shxxDtos.get(t-1).getShrmc()+"\t日期:"+shxxDtos.get(t-1).getShsj()+")":"/");

        StringBuilder sybmyj = new StringBuilder();
        if(shxxDtos!=null && shxxDtos.size()>4){
            for (int k = 3;k<shxxDtos.size()-2;k++){
                sybmyj.append(StringUtil.isNotBlank(shxxDtos.get(k).getShyj())?
                        shxxDtos.get(k).getShyj()+"\t(姓名："+shxxDtos.get(k).getShrmc()+"\t日期:"+shxxDtos.get(k).getShsj()+")\n"
                        :"同意。\t(姓名："+shxxDtos.get(k).getShrmc()+"\t日期:"+shxxDtos.get(k).getShsj()+")\n");
            }
        }
        map.put("sybmyj",StringUtil.isNotBlank(sybmyj)?sybmyj.toString():"/");
        return map;
    }

}
