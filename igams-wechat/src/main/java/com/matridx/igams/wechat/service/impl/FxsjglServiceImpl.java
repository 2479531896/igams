package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponse;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.SjycDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.ISjycService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.wechat.dao.entities.FxsjglDto;
import com.matridx.igams.wechat.dao.entities.FxsjglModel;
import com.matridx.igams.wechat.dao.post.IFxsjglDao;
import com.matridx.igams.wechat.service.svcinterface.IFxsjglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FxsjglServiceImpl extends BaseBasicServiceImpl<FxsjglDto, FxsjglModel, IFxsjglDao> implements IFxsjglService, IAuditService {
    @Autowired
    IDdspglService ddspglService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    ICommonService commonService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    private IFjcfbService fjcfbService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IShxxService shxxService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    IDdfbsglService ddfbsglService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    ICommonService commonservice;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.wechat.registerurl:}")
    private String registerurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Value("${matridx.fileupload.releasePath:}")
    private String releasePath;
    @Autowired
    ISjycService sjycService;
    private Logger log = LoggerFactory.getLogger(FxsjglServiceImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean addInfo(FxsjglDto fxsjglDto) throws BusinessException {
        boolean success = dao.insert(fxsjglDto)!=0;
        if (!success)
            throw new BusinessException("msg","新增保存数据失败！");
        //文件复制到正式文件夹，插入信息至正式表
        if(fxsjglDto.getFjids()!=null && fxsjglDto.getFjids().size() > 0){
            for (int i = 0; i < fxsjglDto.getFjids().size(); i++) {
                success = fjcfbService.save2RealFile(fxsjglDto.getFjids().get(i),fxsjglDto.getFxsjid());
                if (!success)
                    throw new BusinessException("msg","附件保存失败失败！");
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean modInfo(FxsjglDto fxsjglDto) throws BusinessException {
        boolean success = dao.update(fxsjglDto)!=0;
        if (!success)
            throw new BusinessException("msg","修改数据失败！");
        //文件复制到正式文件夹，插入信息至正式表
        if(fxsjglDto.getFjids()!=null && fxsjglDto.getFjids().size() > 0){
            for (int i = 0; i < fxsjglDto.getFjids().size(); i++) {
                success = fjcfbService.save2RealFile(fxsjglDto.getFjids().get(i),fxsjglDto.getFxsjid());
                if (!success)
                    throw new BusinessException("msg","附件保存失败失败！");
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Boolean delInfo(FxsjglDto fxsjglDto) {
        return dao.delete(fxsjglDto)!=0;
    }

    @Override
    public List<FxsjglDto> getPagedAuditList(FxsjglDto fxsjglDto) {
        List<FxsjglDto> list = dao.getPagedAuditList(fxsjglDto);
        if (CollectionUtils.isEmpty(list))
            return list;
        List<FxsjglDto> sqList = dao.getAuditListRecheck(list);
        commonservice.setSqrxm(sqList);
        return sqList;
    }
    @Override
    public List<FxsjglDto> getDtoList(FxsjglDto fxsjglDto){
        List<FxsjglDto> dtoList = dao.getDtoList(fxsjglDto);
        for (FxsjglDto dto : dtoList) {
            FjcfbDto fjcfbDto = new FjcfbDto();
            fjcfbDto.setYwid(dto.getFxsjid());
            fjcfbDto.setYwlx(BusTypeEnum.IMP_RISK_BOARD.getCode());
            List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
            dto.setFjcfbDtos(fjcfbDtos);
        }
        return dtoList;
    }

    @Override
    public List<FxsjglDto> getPagedDtoList(FxsjglDto fxsjglDto) {
        List<FxsjglDto> list = dao.getPagedDtoList(fxsjglDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_RISK_BOARD.getCode(), "zt", "fxsjid",new String[] {
                    StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return list;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        FxsjglDto fxsjglDto = (FxsjglDto) baseModel;
        fxsjglDto.setXgry(operator.getYhid());
        boolean isSuccess = true;
        if (StringUtil.isNotBlank(fxsjglDto.getFxsjid())){
            isSuccess = dao.update(fxsjglDto)!=0;
            //文件复制到正式文件夹，插入信息至正式表
            if(fxsjglDto.getFjids()!=null && fxsjglDto.getFjids().size() > 0){
                for (int i = 0; i < fxsjglDto.getFjids().size(); i++) {
                    fjcfbService.save2RealFile(fxsjglDto.getFjids().get(i),fxsjglDto.getFxsjid());
                }
            }
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
            FxsjglDto fxsjglDto = new FxsjglDto();
            fxsjglDto.setFxsjid(shgcDto.getYwid());
            fxsjglDto.setXgry(operator.getYhid());
            FxsjglDto dtoById = dao.getDtoById(fxsjglDto.getFxsjid());
            if (StringUtil.isNotBlank(dtoById.getDdslid()) && !StatusEnum.CHECK_NO.getCode().equals(dtoById.getZt())){
                try {
                    GetProcessInstanceResponse processInstances = talkUtil.getProcessInstances(operator.getYhm(), dtoById.getDdslid());
                    for (GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResultFormComponentValues formComponentValue : processInstances.getBody().getResult().getFormComponentValues()) {
                        if ("标本处理方式".equals(formComponentValue.getName())){
                            List<JcsjDto> riskList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RISKY_ACTIONS.getCode());
                            for (JcsjDto jcsjDto : riskList) {
                                if (jcsjDto.getCsmc().equals(formComponentValue.getValue())){
                                    fxsjglDto.setBbcl(jcsjDto.getCsid());
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error(e.toString());
                }
            }
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                // 由于合同提交时直接发起了钉钉审批，所以审核不通过时直接采用钉钉的审批拒绝消息即可
                fxsjglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
//                //OA取消审批的同时组织钉钉审批
//                if(dtoById!=null && StringUtils.isNotBlank(dtoById.getDdslid()) && AuditTypeEnum.AUDIT_RISK_BOARD.getCode().equals(shgcDto.getShlb())) {
//                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhid(), dtoById.getDdslid(), "", operator.getDdid());
//                    //若撤回成功将实例ID至为空
//                    String success=String.valueOf(cancelResult.get("message"));
//                    @SuppressWarnings("unchecked")
//                    Map<String,Object> result_map=JSON.parseObject(success,Map.class);
//                    Boolean bo1=new Boolean((boolean) result_map.get("success"));
//                    if(Boolean.valueOf(bo1)==true)
//                        fxsjglDto.setDdslid("");
//                }

                String FXSJ_SH000003 = xxglService.getMsg("FXSJ_SH000003");
                String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
                if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0 ){
                    try{
                        int size = shgcDto.getSpgwcyDtos().size();
                        for(int i=0;i<size;i++){
                            if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00026,StringUtil.replaceMsg(FXSJ_SH000003,operator.getZsxm(),
                                        shgcDto.getShlbmc() ,dtoById.getNbbm(),dtoById.getFxlbmc(),dtoById.getHzxm(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                fxsjglDto.setZt(StatusEnum.CHECK_PASS.getCode());
                fxsjglDto.setQrsj("NOW");
                String FXSJ_SH000002 = xxglService.getMsg("FXSJ_SH000002");
                String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
                if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0){
                    try{
                        int size = shgcDto.getSpgwcyDtos().size();
                        for(int i=0;i<size;i++){
                            if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00006,StringUtil.replaceMsg(FXSJ_SH000002,operator.getZsxm(),
                                        shgcDto.getShlbmc() ,dtoById.getNbbm(),dtoById.getFxlbmc(),dtoById.getHzxm(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }else {
                if (null == dtoById || StringUtil.isBlank(dtoById.getDdid())){
                    throw new BusinessException("ICOM99019","未获取到送检伙伴用户钉钉！");
                }
                fxsjglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if("2".equals(shgcDto.getXlcxh())) {//提交的时候发起钉钉审批
                    try {
                        Map<String,Object> template=talkUtil.getTemplateCode(dtoById.getYhm(), "不合格标本退回审批");//获取审批模板ID
                        String templateCode=(String) template.get("message");
                        Map<String,String> map= new HashMap<>();
                        map.put("标本编号", dtoById.getYbbh());
                        map.put("患者姓名", dtoById.getHzxm());
                        map.put("标本类型", dtoById.getYblxmc());
                        map.put("特批事项", dtoById.getFxlbmc());
                        map.put("情况说明", StringUtil.isNotBlank(dtoById.getBz())?dtoById.getBz():"");
                        map.put("标本处理方式", dtoById.getBbclmc());
                        map.put("合作伙伴", dtoById.getDb());
                        map.put("其他内容",applicationurl+urlPrefix+"/ws/risk/getBoardUrl?fxsjid="+dtoById.getFxsjid());
                        Map<String,Object> t_map=talkUtil.createInstanceNew(dtoById.getYhm(), templateCode,  dtoById.getDdid(), operator.getDdid(), operator.getJgid(), map);

                        String status=(String) t_map.get("status");
                        if("success".equals(status)) {
                            String instanceId=(String) t_map.get("instanceId");
                            //保存至钉钉分布式管理表(主站)
                            RestTemplate t_restTemplate = new RestTemplate();
                            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                            paramMap.add("ddslid", String.valueOf(instanceId));
                            paramMap.add("fwqm", urlPrefix);
                            paramMap.add("cljg", "1");
                            paramMap.add("fwqmc", "杰毅医检");
                            paramMap.add("spywlx", shgcDto.getShlb());
                            paramMap.add("hddz",applicationurl);
                            paramMap.add("wbcxid", dtoById.getWbcxid());//存入外部程序id
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
                            fxsjglDto.setDdslid(String.valueOf(instanceId));

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
                }else if("1".equals(shgcDto.getXlcxh()) && shgcDto.getShxx()==null){
                    SjycDto sjycDto=new SjycDto();
                    sjycDto.setYwid(dtoById.getFxsjid());
                    List<SjycDto> dtoList = sjycService.getDtoList(sjycDto);
                    if(CollectionUtils.isEmpty(dtoList)){
                        List<JcsjDto> ycqflist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_DISTINGUISH.getCode());
                        if(ycqflist!=null&&ycqflist.size()>0){
                            for(JcsjDto dto:ycqflist){
                                if("NORMAL_EXCEPTION".equals(dto.getCsdm())){
                                    sjycDto.setYcqf(dto.getCsid());
                                    break;
                                }
                            }
                        }
                        sjycDto.setJcdw(dtoById.getJcdw());
                        List<JcsjDto> yclxlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_TYPE.getCode());
                        if(yclxlist!=null&&yclxlist.size()>0){
                            for(JcsjDto dto:yclxlist){
                                if("SAMPLE_RETURN".equals(dto.getCsdm())){
                                    sjycDto.setLx(dto.getCsid());
                                    break;
                                }
                            }
                        }
                        sjycDto.setYcxx(dtoById.getFxlbmc()+"    "+dtoById.getBz());
                        Object xtsz=redisUtil.hget("matridx_xtsz","exception_confirm_role");
                        if(xtsz!=null){
                            JSONObject jsonObject=JSONObject.parseObject(String.valueOf(xtsz));
                            sjycDto.setQrr(jsonObject.getString("szz"));
                            sjycDto.setQrlx("ROLE_TYPE");
                        }
                        sjycDto.setYcid(StringUtil.generateUUID());
                        sjycDto.setTwr(operator.getYhid());
                        sjycDto.setTwrmc(operator.getZsxm());
                        sjycDto.setLrry(operator.getYhid());
                        sjycDto.setYcbt(dtoById.getHzxm()+"-"+dtoById.getYblxmc()+"-"+dtoById.getDb());
                        FjcfbDto fjcfbDto = new FjcfbDto();
                        fjcfbDto.setYwid(fxsjglDto.getFxsjid());
                        fjcfbDto.setYwlx(BusTypeEnum.IMP_RISK_BOARD.getCode());
                        List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
                        List<FjcfbDto> list = new ArrayList<>();
                        if(!CollectionUtils.isEmpty(fjcfbDtos)){
                            int xh=1;
                            for(FjcfbDto dto:fjcfbDtos){
                                String wjlj = dto.getWjlj();
                                DBEncrypt dbEncrypt = new DBEncrypt();
                                String pathString = dbEncrypt.dCode(wjlj);
                                File file = new File(pathString);
                                // 文件不存在不做任何操作
                                if (file.exists()) {
                                    String copyReleasePath=releasePath + BusTypeEnum.IMP_EXCEPTION.getCode() + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
                                    String copyPath=copyReleasePath+"/"+file.getName().replaceAll("[\\\\/]", "");
                                    File copyFile=new File(copyPath);
                                    if (copyFile.exists()){
                                        copyFile.delete();
                                    }
                                    try {
                                        Files.copy(file.toPath(), copyFile.toPath());
                                        FjcfbDto fjcfbDto_t = new FjcfbDto();
                                        fjcfbDto_t.setFjid(StringUtil.generateUUID());
                                        fjcfbDto_t.setYwid(sjycDto.getYcid());
                                        fjcfbDto_t.setXh(String.valueOf(xh));
                                        xh++;
                                        fjcfbDto_t.setYwlx(BusTypeEnum.IMP_EXCEPTION.getCode());
                                        fjcfbDto_t.setWjm(dto.getWjm());
                                        fjcfbDto_t.setWjlj(dbEncrypt.eCode(copyPath));
                                        fjcfbDto_t.setFwjlj(dbEncrypt.eCode(copyReleasePath));
                                        fjcfbDto_t.setFwjm(dbEncrypt.eCode(dto.getWjm()));
                                        fjcfbDto_t.setZhbj("0");
                                        list.add(fjcfbDto_t);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                            if(!CollectionUtils.isEmpty(list)){
                                fjcfbService.batchInsertFjcfb(list);
                            }
                        }
                        sjycService.insert(sjycDto);
                        sjycDto.setYbbh(dtoById.getYbbh());
                        sjycDto.setQrrmc(operator.getZsxm());
                        if("ROLE_TYPE".equals(sjycDto.getQrlx()))
                            sjycService.fxsjaddException(sjycDto);
                    }
                } else{
                    String FXSJ_SH000001 = xxglService.getMsg("FXSJ_SH000001");
                    String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
                    if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0){
                        try{
                            int size = shgcDto.getSpgwcyDtos().size();
                            for(int i=0;i<size;i++){
                                if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
                                    talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00003,StringUtil.replaceMsg(FXSJ_SH000001,operator.getZsxm(),
                                            shgcDto.getShlbmc() ,dtoById.getNbbm(),dtoById.getFxlbmc(),dtoById.getHzxm(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                                }
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }else{
                        fxsjglDto.setDdslid("");
                    }
                }
            }
            //发送钉钉消息--取消审核人员
            if(shgcDto.getNo_spgwcyDtos() != null && shgcDto.getNo_spgwcyDtos().size() > 0){
                int size = shgcDto.getNo_spgwcyDtos().size();
                for(int i=0;i<size;i++){
                    if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                        talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",shgcDto.getShlbmc() ,dtoById.getNbbm(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                    }
                }
            }
            dao.update(fxsjglDto);
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
            FxsjglDto fxsjglDto = new FxsjglDto();
            String htid = shgcDto.getYwid();
            fxsjglDto.setXgry(operator.getYhid());
            fxsjglDto.setFxsjid(htid);
            if (auditParam.isCancelOpe()) {
                fxsjglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
            } else {
                fxsjglDto.setZt(StatusEnum.CHECK_NO.getCode());
                //OA取消审批的同时组织钉钉审批
                FxsjglDto dtoById = dao.getDtoById(fxsjglDto.getFxsjid());
                if(dtoById!=null && StringUtils.isNotBlank(dtoById.getDdslid()) && AuditTypeEnum.AUDIT_RISK_BOARD.getCode().equals(shgcDto.getShlb())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhid(), dtoById.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map= JSON.parseObject(success,Map.class);
                    Boolean bo1= (boolean) result_map.get("success");
                    if(bo1)
                        fxsjglDto.setDdslid("");
                }
            }
            dao.update(fxsjglDto);
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
        FxsjglDto fxsjglDto=new FxsjglDto();
        fxsjglDto.setIds(ids);
        List<FxsjglDto> dtoList = dao.getDtoList(fxsjglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(FxsjglDto dto:dtoList){
                list.add(dto.getFxsjid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean callbackRiskBoardAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException {
        FxsjglDto fxsjglDto=new FxsjglDto();
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
            if(org.apache.commons.lang3.StringUtils.isNotBlank(ddfbsglDto.getFwqm()) || !applicationurl.equals(registerurl)) {
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
            fxsjglDto.setDdslid(processInstanceId);
            //根据钉钉审批实例ID查询关联请购单
            fxsjglDto=dao.getDto(fxsjglDto);
            //若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if(fxsjglDto!=null) {
                //获取审批人信息
                User user=new User();
                user.setDdid(staffId);
                user.setWbcxid(wbcxidString);
                user = commonService.getByddwbcxid(user);
                //判断查出得信息是否为空
                if(user==null)
                    return false;
                //获取审批人角色信息
                //根据用户信息查找权限信息
                Map<String, String> param = new HashMap<>();
                param.put("yhid", user.getYhid());
                List<Map<String, String>> yhqxDtos = commonservice.getJsDtoList(param);
                // 获取当前审核过程
                ShgcDto t_shgcDto = shgcService.getDtoByYwid(fxsjglDto.getFxsjid());
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
                        shxxDto.setYwid(fxsjglDto.getFxsjid());
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
                                lastlcxh="1";
                                shxxDto.setThlcxh(lastlcxh);
                            }
                            //如果审批被转发
                            else if(("redirect").equals(result)) {
                                String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(finishTime) / 1000));
                                log.error("DingTalkMaterPurchaseAudit(钉钉物料请购审批转发提醒)------转发人员:"+user.getZsxm()+",人员ID:"+user.getYhid()+",标本编号:"+fxsjglDto.getYbbh()+",风险ID:"+fxsjglDto.getFxsjid()+",转发时间:"+date);
                                return true;
                            }
                            //调用审核方法
                            Map<String, List<String>> map= ToolUtil.reformRequest(request);
                            log.error("map:"+map);
                            List<String> list= new ArrayList<>();
                            list.add(fxsjglDto.getFxsjid());
                            map.put("fxsjid", list);
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
                            shxxDto.setYwglmc(fxsjglDto.getYbbh());
                            FxsjglDto fxsjglDto1 = new FxsjglDto();
                            fxsjglDto1.setFxsjid(fxsjglDto.getFxsjid());
                            dao.update(fxsjglDto1);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            //调用审核方法
                            Map<String, List<String>> map=ToolUtil.reformRequest(request);
                            List<String> list= new ArrayList<>();
                            list.add(fxsjglDto.getFxsjid());
                            map.put("fxsjid", list);
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
                        shxx.setYwid(fxsjglDto.getFxsjid());
                        shxx.setShlb(AuditTypeEnum.AUDIT_RISK_BOARD.getCode());
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
