package com.matridx.igams.production.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.production.dao.entities.YxhtDto;
import com.matridx.igams.production.dao.entities.YxhtModel;
import com.matridx.igams.production.dao.post.IYxhtDao;
import com.matridx.igams.production.service.svcinterface.IWjglService;
import com.matridx.igams.production.service.svcinterface.IYxhtService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
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

@Service
public class YxhtServiceImpl extends BaseBasicServiceImpl<YxhtDto, YxhtModel, IYxhtDao> implements IYxhtService, IAuditService {
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
    IWjglService wjglService;
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;
    /**
     * 文档转换完成OK
     */
    @Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
    private final String DOC_OK = null;
    private final Logger log = LoggerFactory.getLogger(YxhtServiceImpl.class);
    @Override
    public boolean isHtbhRepeat(String htbh, String htid) {
        // TODO Auto-generated method stub
        if (StringUtil.isNotBlank(htbh)) {
            YxhtDto yxhtDto = new YxhtDto();
            yxhtDto.setHtbh(htbh);
            yxhtDto.setHtid(htid);
            List<YxhtDto> htglDtos = dao.getListByHtbh(yxhtDto);
            return CollectionUtils.isEmpty(htglDtos);
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addSaveMarketingContract(YxhtDto yxhtDto) throws BusinessException {
        yxhtDto.setHtid(StringUtil.generateUUID());
        insert(yxhtDto);
        if(!CollectionUtils.isEmpty(yxhtDto.getFjids())) {
            for (int i = 0; i < yxhtDto.getFjids().size(); i++) {
                boolean save2RealFile = fjcfbService.save2RealFile(yxhtDto.getFjids().get(i), yxhtDto.getHtid());
                if (!save2RealFile){
                    throw new BusinessException("msg", "保存附件失败!");
                }
                FjcfbDto fjcfbDto=new FjcfbDto();
                fjcfbDto.setFjid(yxhtDto.getFjids().get(i));
                FjcfbDto t_fjcfbDto=fjcfbService.getDto(fjcfbDto);
                int begin=t_fjcfbDto.getWjm().lastIndexOf(".");
                String wjmkzm=t_fjcfbDto.getWjm().substring(begin);
                DBEncrypt p = new DBEncrypt();
                if((wjmkzm.equalsIgnoreCase(".doc"))||(wjmkzm.equalsIgnoreCase(".docx"))||(wjmkzm.equalsIgnoreCase(".xls"))||(wjmkzm.equalsIgnoreCase(".xlsx"))) {
                    String wjljjm=p.dCode(t_fjcfbDto.getWjlj());
                    //连接服务器
                    boolean sendFlg=wjglService.sendWordFile(wjljjm);
                    if(sendFlg) {
                        Map<String,String> t_map=new HashMap<>();
                        String fwjm=p.dCode(t_fjcfbDto.getFwjm());
                        t_map.put("wordName", fwjm);
                        t_map.put("fwjlj",t_fjcfbDto.getFwjlj());
                        t_map.put("fjid",t_fjcfbDto.getFjid());
                        t_map.put("ywlx",t_fjcfbDto.getYwlx());
                        t_map.put("MQDocOkType",DOC_OK);
                        //发送Rabbit消息转换pdf
                        amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(t_map));
                    }
                }
            }
        }
        return true;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean modSaveMarketingContract(YxhtDto yxhtDto) throws BusinessException {
        dao.updatePageEvent(yxhtDto);
        if(!CollectionUtils.isEmpty(yxhtDto.getFjids())) {
            for (int i = 0; i < yxhtDto.getFjids().size(); i++) {
                boolean save2RealFile = fjcfbService.save2RealFile(yxhtDto.getFjids().get(i), yxhtDto.getHtid());
                if (!save2RealFile){
                    throw new BusinessException("msg", "保存附件失败!");
                }
                FjcfbDto fjcfbDto=new FjcfbDto();
                fjcfbDto.setFjid(yxhtDto.getFjids().get(i));
                FjcfbDto t_fjcfbDto=fjcfbService.getDto(fjcfbDto);
                int begin=t_fjcfbDto.getWjm().lastIndexOf(".");
                String wjmkzm=t_fjcfbDto.getWjm().substring(begin);
                DBEncrypt p = new DBEncrypt();
                if((wjmkzm.equalsIgnoreCase(".doc"))||(wjmkzm.equalsIgnoreCase(".docx"))||(wjmkzm.equalsIgnoreCase(".xls"))||(wjmkzm.equalsIgnoreCase(".xlsx"))) {
                    String wjljjm=p.dCode(t_fjcfbDto.getWjlj());
                    //连接服务器
                    boolean sendFlg=wjglService.sendWordFile(wjljjm);
                    if(sendFlg) {
                        Map<String,String> t_map=new HashMap<>();
                        String fwjm=p.dCode(t_fjcfbDto.getFwjm());
                        t_map.put("wordName", fwjm);
                        t_map.put("fwjlj",t_fjcfbDto.getFwjlj());
                        t_map.put("fjid",t_fjcfbDto.getFjid());
                        t_map.put("ywlx",t_fjcfbDto.getYwlx());
                        t_map.put("MQDocOkType",DOC_OK);
                        //发送Rabbit消息转换pdf
                        amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(t_map));
                    }
                }

            }
        }
        return true;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        YxhtDto yxhtDto = (YxhtDto)baseModel;
        boolean isSuccess = this.isHtbhRepeat(yxhtDto.getHtbh(), yxhtDto.getHtid());
        if(!isSuccess) {
            throw new BusinessException("msg","合同编号不允许重复!");
        }
        yxhtDto.setXgry(operator.getYhid());
        return this.update(yxhtDto);
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
            YxhtDto yxhtDto = new YxhtDto();
            yxhtDto.setHtid(shgcDto.getYwid());
            yxhtDto.setXgry(operator.getYhid());
            YxhtDto yxhtDto_t = getDtoById(yxhtDto.getHtid());
            List<SpgwcyDto> spgwcyDtos = shgcDto.getSpgwcyDtos();
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                yxhtDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00026,
                                    xxglService.getMsg("ICOMM_SH00125", operator.getZsxm(), yxhtDto_t.getHtbh(),
                                            yxhtDto_t.getHtrq(),
                                            yxhtDto_t.getSsywymc(),
                                            yxhtDto_t.getKhmc(),
                                            yxhtDto_t.getZd()));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                yxhtDto.setXgry(operator.getYhid());
                yxhtDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00006,
                                    xxglService.getMsg("ICOMM_SH00126", operator.getZsxm(), yxhtDto_t.getHtbh(),
                                            yxhtDto_t.getHtrq(),
                                            yxhtDto_t.getSsywymc(),
                                            yxhtDto_t.getKhmc(),
                                            yxhtDto_t.getZd()));
                        }
                    }
                }
            }else {
                yxhtDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if ("1".equals(shgcDto.getXlcxh())&& shgcDto.getShxx()==null){
                    try {
                        StringBuilder zsld = new StringBuilder();
                        if (StringUtil.isNotBlank(yxhtDto_t.getZsld())){
                            List<YxhtDto> zsldInfo = dao.getInfoByZsld(yxhtDto_t);
                            for (YxhtDto dto : zsldInfo) {
                                zsld.append(",").append(dto.getDdid());
                            }
                            zsld = new StringBuilder(zsld.substring(1));
                        }else {
                            throw new BusinessException("ICOM99019","未选择直属领导！");
                        }
                        // 根据申请部门查询审核流程
                        List<DdxxglDto> ddxxglDtos = ddxxglService.getProcessByJgid(yxhtDto_t.getJgid(), DdAuditTypeEnum.SP_YXHT.getCode());
                        if(CollectionUtils.isEmpty(ddxxglDtos))
                            throw new BusinessException("msg","未找到所属部门审核流程！");
                        if(ddxxglDtos.size() > 1)
                            throw new BusinessException("msg","找到多条所属部门审核流程！");
                        String spr = ddxxglDtos.get(0).getSpr();
                        String csr = ddxxglDtos.get(0).getCsr();
                        if(!StringUtils.isNotBlank(csr)) {
                            csr="";
                        }else if(!"WT".equals(yxhtDto_t.getHtlxdm())){
                            csr=StringUtil.substringBeforeLast(StringUtil.substringBeforeLast(csr,","),",");
                        }
                        String approvers = yxhtDto_t.getYwyddid()+","+zsld+","+spr;
                        if("A".equals(yxhtDto_t.getYxhtlxdm())){
                            String part1 = approvers.substring(0, approvers.indexOf('|'));
                            String part2 = approvers.substring(approvers.lastIndexOf('|')+1);
                            approvers = part1 + "," + part2;
                        }else {
                            approvers = approvers.replace("|", ",");
                        }
                        String[] string = StringUtil.split(approvers,",");
                        List<String> sprList = new ArrayList<>(Arrays.asList(string));
                        Iterator<String> iterator = sprList.iterator();
                        while (iterator.hasNext()) {
                            String item = iterator.next();
                            if (sprList.indexOf(item) != sprList.lastIndexOf(item)) {
                                iterator.remove();
                            }
                        }
                        approvers = sprList.stream().collect(Collectors.joining(","));
                        Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "营销合同");//获取审批模板ID
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
                        map.put("所属公司", systemname);
                        map.put("合同主体", yxhtDto_t.getHtzt());
                        map.put("合同类型", yxhtDto_t.getYxhtlxmc());
                        map.put("销售类别", yxhtDto_t.getXslbmc());
                        map.put("用章类型", yxhtDto_t.getYzlbmc());
                        map.put("合同名称", yxhtDto_t.getHtmc());
                        map.put("合同编号", yxhtDto_t.getHtbh());
                        map.put("客户名称", yxhtDto_t.getKhmc());
                        map.put("合同金额（元）", yxhtDto_t.getHtje());
                        map.put("合同份数", "1");
                        map.put("合同邮寄信息", yxhtDto_t.getHtyjxx());
                        map.put("备注", yxhtDto_t.getBz()!=null?yxhtDto_t.getBz():"");
                        map.put("其他内容","附件信息："+applicationurl+urlPrefix+"/ws/marketingContract/getMarketingContractUrl?htid="+yxhtDto_t.getHtid());
                        Map<String,Object> t_map=talkUtil.createInstance(operator.getYhm(), templateCode, approvers, csr, userid, dept, map,null,null);
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
                                yxhtDto.setDdslid(String.valueOf(result_map.get("process_instance_id")));
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
                }else {
                    // 发送钉钉消息
                    if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                        try {
                            for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                                if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                    talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                            spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00003"),
                                            xxglService.getMsg("ICOMM_SH00124"
                                                    , operator.getZsxm(), yxhtDto_t.getHtbh(),
                                                    yxhtDto_t.getHtrq(),
                                                    yxhtDto_t.getSsywymc(),
                                                    yxhtDto_t.getKhmc(),
                                                    yxhtDto_t.getZd()));
                                }
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                }
                //发送钉钉消息--取消审核人员
                if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())){
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
                                    shgcDto.getNo_spgwcyDtos().get(i).getYhid(),xxglService.getMsg("ICOMM_SH00004"), xxglService.getMsg("ICOMM_SH00005",shgcDto.getShlbmc() ,yxhtDto_t.getHtbh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(yxhtDto);
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
                String htid = shgcDto.getYwid();
                YxhtDto yxhtDto = new YxhtDto();
                yxhtDto.setHtid(htid);
                yxhtDto.setXgry(operator.getYhid());
                yxhtDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(yxhtDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String htid = shgcDto.getYwid();
                YxhtDto yxhtDto = new YxhtDto();
                yxhtDto.setHtid(htid);
                yxhtDto.setXgry(operator.getYhid());
                yxhtDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(yxhtDto);
                //OA取消审批的同时组织钉钉审批
                YxhtDto yxhtDto_t = getDtoById(yxhtDto.getHtid());
                if(yxhtDto_t!=null && StringUtils.isNotBlank(yxhtDto_t.getDdslid())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), yxhtDto_t.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map=JSON.parseObject(success,Map.class);
                    boolean bo1= (boolean) result_map.get("success");
                    if(bo1){
                        dao.updateDdslidToNull(yxhtDto_t);
                    }else {
                        log.error("营销合同撤回审批异常："+JSON.toJSONString(cancelResult));
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
        YxhtDto yxhtDto = new YxhtDto();
        yxhtDto.setIds(ids);
        List<YxhtDto> dtoList = dao.getDtoList(yxhtDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(YxhtDto dto:dtoList){
                list.add(dto.getHtid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean callbackMarketingContractAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException {
        YxhtDto yxhtDto = new YxhtDto();
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
            yxhtDto.setDdslid(processInstanceId);
            // 根据钉钉审批实例ID查询关联请购单
            yxhtDto = dao.getDtoByDdslid(yxhtDto.getDdslid());
            // 若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if (yxhtDto != null) {
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
                List<YxhtDto> dd_sprjs = dao.getSprjsBySprid(user.getYhid());
                // 获取当前审核过程
                ShgcDto t_shgcDto = shgcService.getDtoByYwid(yxhtDto.getHtid());
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
                        shxxDto.setYwid(yxhtDto.getHtid());
                        if (StringUtil.isNotBlank(finishTime)){
                            shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                        }
                        log.error("shxxDto-lcxh:" + t_shgcDto.getXlcxh() + " shxxDto-shlb:"
                                + t_shgcDto.getShlb() + " shxxDto-shyj:" + remark + " shxxDto-ywid:"
                                + yxhtDto.getHtid());
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
                                log.error("DingTalkMaterPurchaseAudit(钉钉营销合同审批转发提醒)------转发人员:" + user.getZsxm()
                                        + ",人员ID:" + user.getYhm() + ",合同编号:" + yxhtDto.getHtbh() + ",单据ID:"
                                        + yxhtDto.getHtid() + ",转发时间:" + date);
                                return true;
                            }
                            // 调用审核方法
                            Map<String, List<String>> map = ToolUtil.reformRequest(request);
                            log.error("map:"+map);
                            List<String> list = new ArrayList<>();
                            list.add(yxhtDto.getHtid());
                            map.put("htid", list);
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
                            shxxDto.setYwglmc(yxhtDto.getHtbh());
                            YxhtDto t_yxhtDto = new YxhtDto();
                            t_yxhtDto.setHtid(yxhtDto.getHtid());
                            dao.updateDdslidToNull(t_yxhtDto);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            // 调用审核方法
                            Map<String, List<String>> map = ToolUtil.reformRequest(request);
                            List<String> list = new ArrayList<>();
                            list.add(yxhtDto.getHtid());
                            map.put("htid", list);
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
                        shxx.setYwid(yxhtDto.getHtid());
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
    public List<YxhtDto> getPagedAuditMarketingContract(YxhtDto yxhtDto) {
        // 获取人员ID和履历号
        List<YxhtDto> t_sbyzList = dao.getPagedAuditMarketingContract(yxhtDto);
        if(CollectionUtils.isEmpty(t_sbyzList))
            return t_sbyzList;

        List<YxhtDto> sqList = dao.getAuditListMarketingContract(t_sbyzList);

        commonService.setSqrxm(sqList);

        return sqList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delMarketingContract(YxhtDto yxhtDto) throws BusinessException{
        List<YxhtDto> yxhtDtos = dao.getDhByHtIds(yxhtDto);
        StringBuilder errmsg = new StringBuilder();
        for (YxhtDto dto : yxhtDtos) {
            boolean flag = false;
            if (StringUtil.isNotBlank(dto.getOaxsdh())){
                errmsg.append(",销售单号：").append(dto.getOaxsdh());
                flag = true;
            }
            if (StringUtil.isNotBlank(dto.getLldh())){
                errmsg.append(",领料单号").append(dto.getLldh());
                flag = true;
            }
            if (StringUtil.isNotBlank(dto.getJydh())){
                errmsg.append(",借出借用单号").append(dto.getJydh());
                flag = true;
            }
            if (flag){
                errmsg.append("引用了合同编号: ").append(dto.getHtbh());
            }
        }
        if (StringUtil.isNotBlank(errmsg.toString())){
            throw new BusinessException("msg",errmsg.substring(1)+"不允许删除！");
        }else {
            if (StringUtil.isBlank(yxhtDto.getScbj())) {
                yxhtDto.setScbj("1");
            }
            yxhtDto.setHtqdzt("2");
            delete(yxhtDto);
        }
        return true;
    }

    @Override
    public List<YxhtDto> selectHtqxList() {
        return dao.selectHtqxList();
    }
    /**
     * 营销合同列表（查询审核状态）
     */
    @Override
    public List<YxhtDto> getPagedDtoList(YxhtDto yxhtDto) {
        List<YxhtDto> list = dao.getPagedDtoList(yxhtDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_MARKETING_CONTRACT.getCode(), "zt", "htid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean formalSaveMarketingContract(YxhtDto yxhtDto) throws BusinessException{
        dao.update(yxhtDto);
        if(!CollectionUtils.isEmpty(yxhtDto.getFjids())) {
            for (int i = 0; i < yxhtDto.getFjids().size(); i++) {
                boolean save2RealFile = fjcfbService.save2RealFile(yxhtDto.getFjids().get(i), yxhtDto.getHtid());
                if (!save2RealFile){
                    throw new BusinessException("msg", "保存附件失败!");
                }
            }
        }
        return true;
    }

    @Override
    public List<YxhtDto> getInfoByZsld(YxhtDto yxhtDto) {
        return dao.getInfoByZsld(yxhtDto);
    }
    /**
     * 导出
     */
    @Override
    public int getCountForSearchExp(YxhtDto yxhtDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(yxhtDto);
    }

    /**
     * 根据搜索条件获取导出信息
     */
    public List<YxhtDto> getListForSearchExp(Map<String, Object> params) {
        YxhtDto yxhtDto = (YxhtDto) params.get("entryData");
        queryJoinFlagExport(params, yxhtDto);
        return dao.getListForSearchExp(yxhtDto);
    }
    /**
     * 根据选择信息获取导出信息
     */
    public List<YxhtDto> getListForSelectExp(Map<String, Object> params) {
        YxhtDto yxhtDto = (YxhtDto) params.get("entryData");
        queryJoinFlagExport(params, yxhtDto);
        return dao.getListForSelectExp(yxhtDto);
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, YxhtDto yxhtDto) {
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
        yxhtDto.setSqlParam(sqlcs);
    }

    @Override
    public String getMarketingContractSerial(String prefix) {
        return dao.getMarketingContractSerial(prefix);
    }
}
