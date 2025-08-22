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
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.production.dao.entities.CpxqjhDto;
import com.matridx.igams.production.dao.entities.CpxqjhModel;
import com.matridx.igams.production.dao.entities.XqjhmxDto;
import com.matridx.igams.production.dao.post.ICpxqjhDao;
import com.matridx.igams.production.service.svcinterface.ICpxqjhService;
import com.matridx.igams.production.service.svcinterface.ISczlmxService;
import com.matridx.igams.production.service.svcinterface.IXqjhmxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
public class CpxqjhServiceImpl extends BaseBasicServiceImpl<CpxqjhDto, CpxqjhModel, ICpxqjhDao> implements ICpxqjhService, IAuditService {

    @Autowired
    private IXqjhmxService xqjhmxService;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IShxxService shxxService;
    @Autowired
    IDdfbsglService ddfbsglService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IDdspglService ddspglService;
    @Autowired
    IJcsjService jcsjService;
    private final Logger log = LoggerFactory.getLogger(CpxqjhServiceImpl.class);
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired
    ICommonService commonservice;
    @Value("${matridx.systemflg.systemname:}")
    private String systemname;
    @Value("${matridx.wechat.registerurl:}")
    private String registerurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Autowired
    ICommonService commonService;
    @Autowired
    ISczlmxService sczlmxService;
    @Autowired
    IHwxxService hwxxService;
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean deleteByIds(CpxqjhDto cpxqjhDto) throws BusinessException{
        boolean success;
        XqjhmxDto xqjhmxDto = new XqjhmxDto();
        if (!CollectionUtils.isEmpty(cpxqjhDto.getIds())){
            xqjhmxDto.setIds(cpxqjhDto.getIds());
            xqjhmxDto.setScry(cpxqjhDto.getScry());
            xqjhmxDto.setScbj("1");
            success=  xqjhmxService.deleteByCpxqids(xqjhmxDto);
            if (!success)
                throw new BusinessException("msg","删除明细信息失败！");
            success=  dao.deleteByIds(cpxqjhDto) !=0;
            if (!success)
                throw new BusinessException("msg","删除主表信息失败！");
        }else{
            throw new BusinessException("msg","为获取到ids！");
        }
        return true;
    }

    @Override
    public List<CpxqjhDto> getHwxxByWlid(CpxqjhDto cpxqjhDto) {
        return dao.getHwxxByWlid(cpxqjhDto);
    }

    @Override
    public List<CpxqjhDto> getPagedXqjhDtoList(CpxqjhDto cpxqjhDto) {
        List<CpxqjhDto> pagedDtoList =dao.getPagedXqjhDtoList(cpxqjhDto);
        try {
            shgcService.addShgcxxByYwid(pagedDtoList, AuditTypeEnum.AUDIT_FG_PLAN.getCode(), "zt", "cpxqid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
            return pagedDtoList;
    }

    @Override
    public String getXqdh() {
        String yearLast = new SimpleDateFormat("yyyyMMdd", Locale.CHINESE).format(new Date().getTime());
        String prefix = "XQ-" + yearLast+"-";
        // 查询流水号
        String serial = dao.getXqdhSerial(prefix);
        return prefix + serial;
    }
    @Override
    public List<CpxqjhDto> getPagedDtoList(CpxqjhDto cpxqjhDto){
        List<CpxqjhDto> pagedDtoList = dao.getPagedDtoList(cpxqjhDto);
        try {
            shgcService.addShgcxxByYwid(pagedDtoList, AuditTypeEnum.AUDIT_FG_PLAN.getCode(), "zt", "cpxqid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return pagedDtoList;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean progressAddSave(CpxqjhDto cpxqjhDto) throws BusinessException {
        cpxqjhDto.setZt(StatusEnum.CHECK_NO.getCode());
        Boolean success = dao.insert(cpxqjhDto)!=0;
        if (!success){
            throw new BusinessException("msg","新增成品需求计划失败！");
        }
        //文件复制到正式文件夹，插入信息至正式表
        if(!CollectionUtils.isEmpty(cpxqjhDto.getFjids())){
            for (int i = 0; i < cpxqjhDto.getFjids().size(); i++) {
                boolean saveFile = fjcfbService.save2RealFile(cpxqjhDto.getFjids().get(i),cpxqjhDto.getCpxqid());
                if(!saveFile)
                    throw new BusinessException("msg","成品需求附件保存失败!");
            }
        }
        List<XqjhmxDto> xqjhmxDtos = JSON.parseArray(cpxqjhDto.getXqjhmx_json(),XqjhmxDto.class);
        if(!CollectionUtils.isEmpty(xqjhmxDtos)){
            for (XqjhmxDto xqjhmxDto : xqjhmxDtos) {
                xqjhmxDto.setLrry(cpxqjhDto.getLrry());
                xqjhmxDto.setXqjhid(cpxqjhDto.getCpxqid());
                xqjhmxDto.setXqjhmxid(StringUtil.generateUUID());
                xqjhmxDto.setSfsc(null);
            }
            success = xqjhmxService.insertList(xqjhmxDtos);
            if (!success){
                throw new BusinessException("msg","新增需求计划明细信息失败！");
            }
        }else{
            throw new BusinessException("msg","明细计划信息为空！");
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean progressModSave(CpxqjhDto cpxqjhDto) throws BusinessException {
        Boolean success = dao.update(cpxqjhDto)!=0;
        if (!success){
            throw new BusinessException("msg","修改成品需求计划失败！");
        }
        //文件复制到正式文件夹，插入信息至正式表
        if(!CollectionUtils.isEmpty(cpxqjhDto.getFjids())){
            for (int i = 0; i < cpxqjhDto.getFjids().size(); i++) {
                boolean saveFile = fjcfbService.save2RealFile(cpxqjhDto.getFjids().get(i),cpxqjhDto.getCpxqid());
                if(!saveFile)
                    throw new BusinessException("msg","成品需求附件保存失败!");
            }
        }
        List<XqjhmxDto> xqjhmxDtos = JSON.parseArray(cpxqjhDto.getXqjhmx_json(),XqjhmxDto.class);
        if(!CollectionUtils.isEmpty(xqjhmxDtos)){
            XqjhmxDto dto = new XqjhmxDto();
            dto.setXqjhid(cpxqjhDto.getCpxqid());
            dto.setScry(cpxqjhDto.getXgry());
            dto.setScbj("1");
            xqjhmxService.delete(dto);
            for (XqjhmxDto xqjhmxDto : xqjhmxDtos) {
                xqjhmxDto.setLrry(cpxqjhDto.getXgry());
                xqjhmxDto.setXqjhid(cpxqjhDto.getCpxqid());
                xqjhmxDto.setXqjhmxid(StringUtil.generateUUID());
            }
            success = xqjhmxService.insertList(xqjhmxDtos);
            if (!success){
                throw new BusinessException("msg","新增需求计划明细信息失败！");
            }

        }
        return true;
    }

    @Override
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        CpxqjhDto cpxqjhDto = (CpxqjhDto)baseModel;
        cpxqjhDto.setXgry(operator.getYhid());
        boolean isSuccess = true;
        if (StringUtil.isBlank(cpxqjhDto.getDdbj())){
            isSuccess = progressModSave(cpxqjhDto);
        }
        return isSuccess;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
        String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
        String ICOMM_SH00089 = xxglService.getMsg("ICOMM_SH00089");
        for (ShgcDto shgcDto : shgcList) {
            CpxqjhDto cpxqjhDto = new CpxqjhDto();
            cpxqjhDto.setCpxqid(shgcDto.getYwid());
            cpxqjhDto.setXgry(operator.getYhid());
            CpxqjhDto cpxqjhDto1 = getDtoById(cpxqjhDto.getCpxqid());
            shgcDto.setSqbm(cpxqjhDto1.getSqbm());
            List<SpgwcyDto> spgwcyDtos = commonservice.siftJgList(shgcDto.getSpgwcyDtos(), cpxqjhDto1.getSqbm());
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {

                // 由于合同提交时直接发起了钉钉审批，所以审核不通过时直接采用钉钉的审批拒绝消息即可
                cpxqjhDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00026,
                                    xxglService.getMsg("ICOMM_SH00090", operator.getZsxm(), shgcDto.getShlbmc(),
                                            cpxqjhDto1.getXqdh(), cpxqjhDto1.getSqrmc(), cpxqjhDto1.getSqbmmc(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                //OA取消审批的同时组织钉钉审批
                if(StringUtils.isNotBlank(cpxqjhDto1.getDdslid()) && AuditTypeEnum.AUDIT_FG_PLAN.getCode().equals(shgcDto.getShlb())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), cpxqjhDto1.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map=JSON.parseObject(success,Map.class);
                    boolean bo1= (boolean) result_map.get("success");
                    if(bo1)
                        dao.updateDdslidToNull(cpxqjhDto1);
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                cpxqjhDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(), spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00006, StringUtil.replaceMsg(ICOMM_SH00089,
                                    operator.getZsxm(), shgcDto.getShlbmc(), cpxqjhDto1.getXqdh(), cpxqjhDto1.getSqrmc(), cpxqjhDto1.getSqbmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                Date date=new Date();
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                cpxqjhDto.setShsj(dateFormat.format(date));
                XqjhmxDto xqjhmxDto=new XqjhmxDto();
                xqjhmxDto.setXqjhid(cpxqjhDto.getCpxqid());
                xqjhmxDto.setSfsc("1");
                xqjhmxService.updateDto(xqjhmxDto);
            }else {
                cpxqjhDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                try {
                    Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "生产需求审批");//获取审批模板ID
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
                    map.put("需求单号", cpxqjhDto1.getXqdh());
//                    map.put("申请人", cpxqjhDto1.getSqrmc());
                    map.put("申请部门", cpxqjhDto1.getSqbmmc());
                    map.put("预计用途", cpxqjhDto1.getYjyt());
                    map.put("需求日期", cpxqjhDto1.getXqrq());
                    map.put("明细", applicationurl + urlPrefix+"/ws/production/progressInfo?cpxqid="+cpxqjhDto1.getCpxqid());

                    Map<String,Object> t_map=talkUtil.createInstance(operator.getYhm(), templateCode, null, null, userid, dept, map,null,null);
                    String str=(String) t_map.get("message");
                    String status=(String) t_map.get("status");
                    if("success".equals(status)) {
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
                            cpxqjhDto.setDdslid(String.valueOf(result_map.get("process_instance_id")));
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

                //发送钉钉消息--取消审核人员
                if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())){
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,cpxqjhDto1.getXqdh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
//			}
            update(cpxqjhDto);
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        if (auditParam.isCancelOpe()) {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String cpxqid = shgcDto.getYwid();
                CpxqjhDto cpxqjhDto = new CpxqjhDto();
                cpxqjhDto.setXgry(operator.getYhid());
                cpxqjhDto.setCpxqid(cpxqid);
                cpxqjhDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                progressModSave(cpxqjhDto);
            }
        } else {
            // 审核回调方法
            for (ShgcDto shgcDto : shgcList) {
                String cpxqid = shgcDto.getYwid();
                CpxqjhDto cpxqjhDto = new CpxqjhDto();
                cpxqjhDto.setXgry(operator.getYhid());
                cpxqjhDto.setCpxqid(cpxqid);
                cpxqjhDto.setZt(StatusEnum.CHECK_NO.getCode());
                boolean success = dao.update(cpxqjhDto) != 0;
                if (!success)
                    throw new BusinessException("msg","更新主表信息失败！");
                //OA取消审批的同时组织钉钉审批
                CpxqjhDto dtoById=dao.getDtoById(cpxqid);
                if(dtoById!=null && StringUtils.isNotBlank(dtoById.getDdslid()) && AuditTypeEnum.AUDIT_FG_PLAN.getCode().equals(shgcDto.getShlb())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), dtoById.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String result=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map=JSON.parseObject(result,Map.class);
					boolean bo1= (boolean) result_map.get("success");
					if(bo1)
                        dao.updateDdslidToNull(dtoById);
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
        CpxqjhDto cpxqjhDto = new CpxqjhDto();
        cpxqjhDto.setIds(ids);
        List<CpxqjhDto> dtoList = dao.getDtoList(cpxqjhDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(CpxqjhDto dto:dtoList){
                list.add(dto.getCpxqid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Override
    public List<CpxqjhDto> getPagedAuditDevice(CpxqjhDto cpxqjhDto) {
        // 获取人员ID和履历号
        List<CpxqjhDto> cpxqjhDtos= dao.getPagedAuditDevice(cpxqjhDto);
        if (CollectionUtils.isEmpty(cpxqjhDtos))
            return cpxqjhDtos;
        List<CpxqjhDto> cpxqjhDtoList = dao.getAuditListDevice(cpxqjhDtos);
        commonservice.setSqrxm(cpxqjhDtoList);
        return cpxqjhDtoList;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean callbackLlglAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException {
        String result=obj.getString("result");//正常结束时result为agree，拒绝时result为refuse，审批终止时没这个值，redirect转交
        String type=obj.getString("type");//审批正常结束（同意或拒绝）的type为finish，审批终止的type为terminate
        String processInstanceId=obj.getString("processInstanceId");//审批实例id
        String staffId=obj.getString("staffId");//审批人钉钉ID
        String remark=obj.getString("remark");//审核意见
        String content = obj.getString("content");//评论
        String finishTime=obj.getString("finishTime");//审批完成时间
        String title= obj.getString("title");
        String processCode=obj.getString("processCode");
        String wbcxidString=obj.getString("wbcxid");
        log.error("回调参数获取---------result:"+result+",type:"+type+",processInstanceId:"+processInstanceId+",staffId:"+staffId+",remark:"+remark+",finishTime"+finishTime+"wbcxidString="+wbcxidString);
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
            if(StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {                
                if(!CollectionUtils.isEmpty(ddspgllist)) {
                    ddspglDto=ddspgllist.get(0);
                }
            }
            //根据钉钉审批实例ID查询关联领料单
            CpxqjhDto cpxqjhDto =dao.getDtoByDdslid(processInstanceId);
            //若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if(cpxqjhDto!=null) {
                //获取审批人信息
                User user=new User();
                user.setDdid(staffId);
                user.setWbcxid(wbcxidString);
                user = commonService.getByddwbcxid(user);
                if(user==null)
                    return false;
                //获取审批人角色信息
                List<CpxqjhDto> dd_sprjs=dao.getSprjsBySprid(user.getYhid());
                // 获取当前审核过程
                ShgcDto t_shgcDto = shgcService.getDtoByYwid(cpxqjhDto.getCpxqid());
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
                    if(!CollectionUtils.isEmpty(dd_sprjs)) {
                        //审批正常结束（同意或拒绝）
                        ShxxDto shxxDto=new ShxxDto();
                        shxxDto.setGcid(t_shgcDto.getGcid());
                        shxxDto.setLcxh(t_shgcDto.getXlcxh());
                        shxxDto.setShlb(t_shgcDto.getShlb());
                        shxxDto.setShyj(remark);
                        shxxDto.setYwid(cpxqjhDto.getCpxqid());
                        shxxDto.setYwglmc(cpxqjhDto.getXqdh());
                        if (StringUtil.isNotBlank(finishTime)){
                            shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                        }
                        String lastlcxh=null;//回退到指定流程

                        if(("finish").equals(type)) {
                            //如果审批通过,同意
                            if(("agree").equals(result)) {
                                log.error("同意------");
                                shxxDto.setSftg("1");
                                if(StringUtils.isBlank(t_shgcDto.getXlcxh()))
                                    throw new BusinessException("ICOM99019","现流程序号为空");
                                lastlcxh=String.valueOf(Integer.parseInt(t_shgcDto.getXlcxh())+1);
                            }
                            //如果审批未通过，拒绝
                            else if(("refuse").equals(result)) {
                                log.error("拒绝------");
                                shxxDto.setSftg("0");
                                shxxDto.setLcxh(null);
                                lastlcxh="1";
                                shxxDto.setThlcxh(null);
                            }
                            //如果审批被转发
                            else if(("redirect").equals(result)) {
                                String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(finishTime) / 1000));
                                log.error("DingTalkMaterPurchaseAudit(钉钉物料领料审批转发提醒)------转发人员:"+user.getZsxm()+",人员ID:"+user.getYhid()+",单据号:"+cpxqjhDto.getXqdh()+",单据ID:"+cpxqjhDto.getCpxqid()+",转发时间:"+date);
                                return true;
                            }
                            //调用审核方法
                            Map<String, List<String>> map= ToolUtil.reformRequest(request);
                            log.error("map:"+map);
                            List<String> list=new ArrayList<>();
                            list.add(cpxqjhDto.getCpxqid());
                            map.put("cpxqid", list);
                            //若一个用户多个角色导致审核异常时
                            for(int i=0;i<dd_sprjs.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(dd_sprjs.get(i).getSprjsid());
                                    user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
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
                                    log.error("callbackQgglAduit-Exception:" + e.getMessage());
                                    t_map.put("ddspglid", ddspglDto.getDdspglid());
                                    t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());

                                    if(i==dd_sprjs.size()-1)
                                        throw new BusinessException("ICOM99019",e.getMessage());
                                }
                            }
                        }
                        //撤销审批
                        else if(("terminate").equals(type)) {
                            //退回到采购部
                            shxxDto.setThlcxh(null);
                            shxxDto.setYwglmc(cpxqjhDto.getCpxqid());
                            CpxqjhDto cpxqjhDto1=new CpxqjhDto();
                            cpxqjhDto1.setCpxqid(cpxqjhDto.getCpxqid());
                            dao.updateDdslidToNull(cpxqjhDto1);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            //调用审核方法
                            Map<String, List<String>> map=ToolUtil.reformRequest(request);
                            List<String> list=new ArrayList<>();
                            list.add(cpxqjhDto.getCpxqid());
                            map.put("cpxqid", list);
                            for(int i=0;i<dd_sprjs.size();i++) {
                                try {
                                    //取下一个角色
                                    user.setDqjs(dd_sprjs.get(i).getSprjsid());
                                    user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
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
                        shxx.setYwid(cpxqjhDto.getCpxqid());
                        shxx.setShlb(AuditTypeEnum.AUDIT_FG_PLAN.getCode());
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
            log.error("BusinessException:" + e.getMessage());
            throw new BusinessException("ICOM99019",e.getMsg());
        }catch (Exception e) {
            log.error("Exception:" + e.getMessage());
            throw new BusinessException("ICOM99019",e.toString());
        }
        return true;
    }

    @Override
    public void dealXqrkzt(Set<String> xqjhids) {
        if (!CollectionUtils.isEmpty(xqjhids)) {
            for (String xqjhid : xqjhids) {
                CpxqjhDto cpxqjhDto_mod = new CpxqjhDto();
                cpxqjhDto_mod.setCpxqid(xqjhid);
                //获取需求数量
                CpxqjhDto cpxqjhDto_xq = dao.getXqslByCpxqid(xqjhid);
                //获取入库数量
                CpxqjhDto cpxqjhDto_rk = dao.getRkslByCpxqid(xqjhid);
                int flag = new BigDecimal(cpxqjhDto_xq.getXqsl()).compareTo(new BigDecimal(cpxqjhDto_rk.getRksl()));
                if (flag > 0) {
                    //需求数量大于入库数量 部分入库
                    cpxqjhDto_mod.setRkzt("02");
                } else {
                    //需求数量等于入库数量 全部入库
                    cpxqjhDto_mod.setRkzt("03");
                }
                //没有入库数量 未入库
                if (Double.parseDouble(cpxqjhDto_rk.getRksl()) == 0) {
                    cpxqjhDto_mod.setRkzt("01");
                }
                dao.update(cpxqjhDto_mod);
            }
        }
    }

    @Override
    public boolean rkmaintenanceSaveProgress(CpxqjhDto cpxqjhDto) {
        return dao.updateRkzt(cpxqjhDto);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateScpsAndFj(CpxqjhDto cpxqjhDto) {
        if (!CollectionUtils.isEmpty(cpxqjhDto.getFjids())) {
            for (int i = 0; i < cpxqjhDto.getFjids().size(); i++) {
                fjcfbService.save2RealFile(cpxqjhDto.getFjids().get(i), cpxqjhDto.getCpxqid());
            }
        }
        return update(cpxqjhDto);
    }
}
