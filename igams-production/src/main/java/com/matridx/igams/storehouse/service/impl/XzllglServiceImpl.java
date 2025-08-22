package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.service.svcinterface.ISbysService;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.post.IXzllglDao;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class XzllglServiceImpl extends BaseBasicServiceImpl<XzllglDto, XzllglModel, IXzllglDao> implements IXzllglService, IAuditService {
    @Autowired
    IShgcService shgcService;
    @Autowired
    IXzrkmxService xzrkmxService;
    @Autowired
    IXzllmxService xzllmxService;
    @Autowired
    IXzllcglService xzllcglService;
    @Autowired
    DingTalkUtil talkUtil;

    @Autowired
    IXxglService xxglService;
    @Autowired
    IShxxService shxxService;

    @Autowired
    ICommonService commonService;
    private final Logger log = LoggerFactory.getLogger(XzllglServiceImpl.class);
    @Value("${matridx.systemflg.systemname:}")
    private String systemname;
    @Value("${matridx.wechat.registerurl:}")
    private String registerurl;
    @Autowired
    IXzkcxxService xzkcxxService;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IDdfbsglService ddfbsglService;
    @Autowired
    IDdspglService ddspglService;
    @Autowired
    IShlcService shlcService;
    @Autowired
    private IDdxxglService ddxxglService;
    @Autowired
    private IJcsjService jcsjService;
    @Autowired
    private ISbysService sbysService;
    @Override
    public List<XzllglDto> getPagedAuditXzllgl(XzllglDto xzllglDto) {

        // 获取人员ID和履历号
        List<XzllglDto> t_sbyzList = dao.getPagedAuditXzllgl(xzllglDto);
        if(CollectionUtils.isEmpty(t_sbyzList))
            return t_sbyzList;

        List<XzllglDto> sqList = dao.getAuditXzllglList(t_sbyzList);

        commonService.setSqrxm(sqList);
        return sqList;
    }

    /**
     * 获取领料列表
     * @param
     * @return
     */
    public List<XzllglDto> getPagedDtoListReceiveAdministrationMateriel(XzllglDto xzllglDto) {
        List<XzllglDto> list = dao.getPagedDtoListReceiveAdministrationMateriel(xzllglDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_ADMINISTRATION.getCode(), "zt", "xzllid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_ADMINISTRATION_DEVICE.getCode(), "zt", "xzllid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    @Override
    public String generateDjh() {
        // TODO Auto-generated method stub
        // 生成规则: LL-20201022-01 LL-年份日期-流水号 。
        String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
        String prefix = "LL" + "-" + date + "-";
        // 查询流水号
        String serial = dao.getDjhSerial(prefix);
        return prefix + serial;
    }

    @Override
    public XzllglDto getDtoByLldh(XzllglDto xzllglDto) {
        return dao.getDtoByLldh(xzllglDto);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveXzPicking(XzllglDto xzllglDto) throws BusinessException {

        xzllglDto.setZt(StatusEnum.CHECK_NO.getCode());
        boolean result=dao.insert(xzllglDto) != 0;
        if(!result) {
            throw new BusinessException("msg","领料信息保存失败!");
        }
        List<XzrkmxDto> xzrkmxDtos = new ArrayList<>();
        //保存货物领料信息
        List<XzllmxDto> xzllmxDtos= JSON.parseArray(xzllglDto.getLlmx_json(),XzllmxDto.class);
        for (XzllmxDto xzllmxDto:xzllmxDtos) {
            xzllmxDto.setXzllid(xzllglDto.getXzllid());
            xzllmxDto.setXzllmxid(StringUtil.generateUUID());
            xzllmxDto.setLrry(xzllglDto.getLrry());
            XzllcglDto xzllcglDto = new XzllcglDto();
            xzllcglDto.setRkmxid(xzllmxDto.getXzrkmxid());
            xzllcglDto.setRyid(xzllglDto.getLrry());
            result = xzllcglService.delete(xzllcglDto);
            if(!result) {
                throw new BusinessException("msg","领料车数据更新失败!");
            }
            XzrkmxDto xzrkmxDto = new XzrkmxDto();
            xzrkmxDto.setXzrkmxid(xzllmxDto.getXzrkmxid());
            XzrkmxDto xzrkmxDto1 = xzrkmxService.getDto(xzrkmxDto);
            xzrkmxDto.setXgry(xzllmxDto.getLrry());
            xzrkmxDto.setKcl(String.valueOf(Double.parseDouble(xzrkmxDto1.getKcl()) - Double.parseDouble(xzllmxDto.getQlsl())));
            xzrkmxDtos.add(xzrkmxDto);
        }
        if (!CollectionUtils.isEmpty(xzrkmxDtos)){
            for (XzrkmxDto xzrkmxDto:xzrkmxDtos) {
                result = xzrkmxService.updateAdministrationStockByXzrkmxid(xzrkmxDto);
                if(!result) {
                    throw new BusinessException("msg","库存数据更新失败!");
                }
            }
        }
        result = xzllmxService.insertList(xzllmxDtos);
        if(!result) {
            throw new BusinessException("msg","领料明细信息保存失败!");
        }
        return true;
    }

    /**
     * 根据行政领料id获取行政领料信息
     * @param xzllglDto
     * @return
     */
    public XzllglDto getDtoReceiveAdministrationMaterielByXzllid(XzllglDto xzllglDto){
        return dao.getDtoReceiveAdministrationMaterielByXzllid(xzllglDto);
    }

    /**
     * 删除行政领料信息
     * @param xzllglDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delReceiveAdministrationMateriel(XzllglDto xzllglDto) {
        List<XzllglDto> xzllglDtos = dao.getReceiveAdministrationMaterielListByXzllids(xzllglDto);
        List<String> sbysids=new ArrayList<>();
        if(!CollectionUtils.isEmpty(xzllglDtos)) {
            List<XzkcxxDto> list=new ArrayList<>();
            for (XzllglDto dto : xzllglDtos) {
                XzkcxxDto xzkcxxDto = new XzkcxxDto();
                xzkcxxDto.setXzkcid(dto.getXzkcid());
                if (StringUtil.isNotBlank(dto.getSbysid())){
                    sbysids.add(dto.getSbysid());
                }
                if(StringUtil.isNotBlank(dto.getFlry())){
                    xzkcxxDto.setKcl(dto.getQlsl());
                    xzkcxxDto.setUpdateFlag("add");
                    xzkcxxDto.setXgry(xzllglDto.getScry());
                    list.add(xzkcxxDto);
                }else{
                    xzkcxxDto.setYds(dto.getQlsl());
                    xzkcxxDto.setYdsbj("0");
                    xzkcxxDto.setXgry(xzllglDto.getScry());
                    list.add(xzkcxxDto);
                }
            }
           xzkcxxService.updateXzllDtos(list);
        }
        if(!CollectionUtils.isEmpty(sbysids)) {
            SbysDto sbysDto=new SbysDto();
            sbysDto.setScry(xzllglDto.getScry());
            sbysDto.setIds(sbysids);
            sbysService.delete(sbysDto);
        }
        XzllmxDto xzllmxDto=new XzllmxDto();
        xzllmxDto.setIds(xzllglDto.getIds());
        xzllmxDto.setScry(xzllglDto.getScry());
        xzllmxService.delete(xzllmxDto);
        dao.delReceiveAdministrationMateriel(xzllglDto);
        shgcService.deleteByYwids(xzllglDto.getIds());//删除审核过程,否则审批延期会有脏数据(虽然审核中不允许删除)
        return true;

    }

    /**
     * 根据行政领料ids获取审核通过行政领料详细信息
     * @param
     * @return
     */
    public List<XzllglDto> getReceiveAdministrationMaterielListByXzllids(XzllglDto xzllglDto) {
        return dao.getReceiveAdministrationMaterielListByXzllids(xzllglDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        // TODO Auto-generated method stub
        XzllglDto xzllglDto = (XzllglDto)baseModel;
        xzllglDto.setXgry(operator.getYhid());
       if(StringUtil.isNotBlank(xzllglDto.getLldh())){
           XzllglDto dtoByLldh = dao.getDtoByLldh(xzllglDto);
           if (dtoByLldh!=null){
               throw new BusinessException("msg","领料单号不允许重复!");
           }
       }
        return modReceiveAdminMateriel(xzllglDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
        String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
        String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
        String ICOMM_SH00078 = xxglService.getMsg("ICOMM_SH00078");
        String ICOMM_SH00079 = xxglService.getMsg("ICOMM_SH00079");
        String ICOMM_SH00080 = xxglService.getMsg("ICOMM_SH00080");
        for (ShgcDto shgcDto : shgcList) {
            XzllglDto xzllglDto = new XzllglDto();
            xzllglDto.setXzllid(shgcDto.getYwid());
            xzllglDto.setXgry(operator.getYhid());
            XzllglDto xzllglDto_t = getDtoById(xzllglDto.getXzllid());
            List<SpgwcyDto> spgwcyDtos = shgcDto.getSpgwcyDtos();
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                xzllglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                    spgwcyDto.getYhid(), ICOMM_SH00026,
                                    StringUtil.replaceMsg(ICOMM_SH00080, operator.getZsxm(), shgcDto.getShlbmc(), xzllglDto_t.getLldh(), xzllglDto_t.getSqrmc(), xzllglDto_t.getSqbmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                xzllglDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    int size = spgwcyDtos.size();
                    for (int i = 0; i < size; i++) {
                        if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
                            		shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00006,
                                    StringUtil. replaceMsg(ICOMM_SH00079, operator.getZsxm(),shgcDto.getShlbmc() ,xzllglDto_t.getLldh(),xzllglDto_t.getSqrmc(),xzllglDto_t.getSqbmmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else {
                xzllglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if (shgcDto.getXlcxh().equals("1") && AuditTypeEnum.AUDIT_ADMINISTRATION.getCode().equals(shgcDto.getShlb())){
                    try {
                        Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "行政领料审批");//获取审批模板ID
                        String templateCode=(String) template.get("message");
                        //获取申请人信息(合同申请应该为采购部门)
                        User user=new User();
                        user.setYhid(shgcDto.getSqr());
                        user=commonService.getUserInfoById(user);
                        if(user==null)
                            throw new BusinessException("ICOM99019","未获取到申请人信息！");
                        if(org.apache.commons.lang.StringUtils.isBlank(user.getDdid()))
                            throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
                        String userid=user.getDdid();
                        String dept=user.getJgid ();
                        Map<String,String> map=new HashMap<>();
                        map.put("所属公司", systemname);
                        map.put("领料单号", xzllglDto_t.getLldh());
                        List<String> bms = new ArrayList<>();
                        bms.add(xzllglDto_t.getSqbm());
                        map.put("申请部门", JSON.toJSONString(bms));
                        map.put("申请人", xzllglDto_t.getSqrmc());
                        map.put("领料明细", applicationurl + urlPrefix+"/ws/production/requisitionAdminInfo?xzllid="+xzllglDto_t.getXzllid());
                        map.put("备注", xzllglDto_t.getBz()!=null?xzllglDto_t.getBz():"");

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
                                xzllglDto.setDdslid(String.valueOf(result_map.get("process_instance_id")));
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

//						//如果提交审批时，申请人属于下一个岗位时，则直接将xlcxh设置为2，跳过当前审批流程
//						if(spgwcyDtos!=null && spgwcyDtos.size()>0 && "1".equals(shgcDto.getXlcxh())) {
//							for(int i=0;i<spgwcyDtos.size();i++) {
//								if(spgwcyDtos.get(i).getYhid().equals(shgcDto.getSqr())) {
//									shgcDto.setXlcxh("2");
//								}
//							}
//						}

                }else{
                    // 发送钉钉消息
                    if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                        try {
                            for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                                if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                    talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
                                            spgwcyDto.getYhid(), ICOMM_SH00003, StringUtil.replaceMsg(ICOMM_SH00078,
                                                    operator.getZsxm(), shgcDto.getShlbmc(), xzllglDto_t.getLldh(), xzllglDto_t.getSqrmc(), xzllglDto_t.getSqbmmc(),
                                                    DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
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
                            		shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,xzllglDto_t.getLldh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(xzllglDto);
        }
        return true;
    }

    @Override
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        // TODO Auto-generated method stub
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String xzllid = shgcDto.getYwid();
                XzllglDto xzllglDto = new XzllglDto();
                xzllglDto.setXzllid(xzllid);
                xzllglDto.setXgry(operator.getYhid());
                xzllglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(xzllglDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String xzllid = shgcDto.getYwid();
                XzllglDto xzllglDto = new XzllglDto();
                xzllglDto.setXzllid(xzllid);
                xzllglDto.setXgry(operator.getYhid());
                xzllglDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(xzllglDto);
                //OA取消审批的同时组织钉钉审批
                XzllglDto xzllglDto_t = getDtoById(xzllglDto.getXzllid());
                if(xzllglDto_t!=null && StringUtils.isNotBlank(xzllglDto_t.getDdslid())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), xzllglDto_t.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map=JSON.parseObject(success,Map.class);
                    boolean bo1= (boolean) result_map.get("success");
                    if(bo1)
                        dao.updateDdslidToNull(xzllglDto_t);
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
        XzllglDto xzllglDto = new XzllglDto();
        xzllglDto.setIds(ids);
        List<XzllglDto> dtoList = dao.getDtoList(xzllglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(XzllglDto dto:dtoList){
                list.add(dto.getXzllid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Override
    public boolean callbackXzllglAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException {
        String result=obj.getString("result");//正常结束时result为agree，拒绝时result为refuse，审批终止时没这个值，redirect转交
        String type=obj.getString("type");//审批正常结束（同意或拒绝）的type为finish，审批终止的type为terminate
        String processInstanceId=obj.getString("processInstanceId");//审批实例id
        String staffId=obj.getString("staffId");//审批人钉钉ID
        String remark=obj.getString("remark");//审核意见
        String content = obj.getString("content");//评论
        String finishTime=obj.getString("finishTime");//审批完成时间
        String title= obj.getString("title");
        String processCode=obj.getString("processCode");
        String wbcxidString  = obj.getString("wbcxid"); //获取外部程序id
        log.error("回调参数获取---------result:"+result+",type:"+type+",processInstanceId:"+processInstanceId+",staffId:"+staffId+",remark:"+remark+",finishTime"+finishTime+",外部程序id"+wbcxidString);
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
            XzllglDto xzllglDto =dao.getDtoByDdslid(processInstanceId);
            //若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if(xzllglDto!=null) {
                //获取审批人信息
                User user=new User();
                user.setDdid(staffId);
                user.setWbcxid(wbcxidString);
                user = commonService.getByddwbcxid(user);
                if(user==null)
                    return false;
                //获取审批人角色信息
                List<XzllglDto> dd_sprjs=dao.getSprjsBySprid(user.getYhid());
                // 获取当前审核过程
                ShgcDto t_shgcDto = shgcService.getDtoByYwid(xzllglDto.getXzllid());
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
                        shxxDto.setYwid(xzllglDto.getXzllid());
                        shxxDto.setYwglmc(xzllglDto.getLldh());
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
                                log.error("DingTalkMaterPurchaseAudit(钉钉物料领料审批转发提醒)------转发人员:"+user.getZsxm()+",人员ID:"+user.getYhid()+",单据号:"+xzllglDto.getLldh()+",单据ID:"+xzllglDto.getXzllid()+",转发时间:"+date);
                                return true;
                            }
                            //调用审核方法
                            Map<String, List<String>> map= ToolUtil.reformRequest(request);
                            log.error("map:"+map);
                            List<String> list=new ArrayList<>();
                            list.add(xzllglDto.getXzllid());
                            map.put("xzllid", list);
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
                                    log.error("callbackXzllglAduit-Exception:" + e.getMessage());
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
                            shxxDto.setYwglmc(xzllglDto.getLldh());
                            XzllglDto xzllglDto1=new XzllglDto();
                            xzllglDto1.setXzllid(xzllglDto.getXzllid());
                            dao.updateDdslidToNull(xzllglDto1);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            //调用审核方法
                            Map<String, List<String>> map=ToolUtil.reformRequest(request);
                            List<String> list=new ArrayList<>();
                            list.add(xzllglDto.getXzllid());
                            map.put("xzllid", list);
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
                        shxx.setYwid(xzllglDto.getXzllid());
                        shxx.setShlb(AuditTypeEnum.AUDIT_ADMINISTRATION.getCode());
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

    /**
     * 修改保存行政领料信息
     * @param xzllglDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modReceiveAdminMateriel(XzllglDto xzllglDto) throws BusinessException{
        //修改行政领料管理信息
        boolean result=dao.modReceiveAdminMateriel(xzllglDto);
        if(!result) {
           throw new BusinessException("msg","领料信息保存失败！");
        }
        if(StringUtil.isNotBlank(xzllglDto.getLlmx_json())){
            List<XzllmxDto> dtos = xzllmxService.getDtoXzllmxListByXzllid(xzllglDto.getXzllid());
            List<XzkcxxDto> list=new ArrayList<>();
            if(!CollectionUtils.isEmpty(dtos)) {
                for (XzllmxDto xzllmxDto : dtos) {
                    XzkcxxDto xzkcxxDto=new XzkcxxDto();
                    xzkcxxDto.setXzkcid(xzllmxDto.getXzkcid());
                    xzkcxxDto.setYds(xzllmxDto.getYds());
                    list.add(xzkcxxDto);
                }
            }
            //删除货物领料明细信息
            XzllmxDto xzllmxDto_t = new XzllmxDto();
            xzllmxDto_t.setXzllid(xzllglDto.getXzllid());
            xzllmxDto_t.setScry(xzllglDto.getXgry());
            result = xzllmxService.delXzllmxByXzllid(xzllmxDto_t);
            if (!result) {
                throw new BusinessException("msg", "领料明细信息数据更新失败！");
            }
            //保存货物领料信息
            List<XzllmxDto> xzllmxDtos = JSON.parseArray(xzllglDto.getLlmx_json(), XzllmxDto.class);
            if(!CollectionUtils.isEmpty(xzllmxDtos)) {
                for (XzllmxDto xzllmxDto : xzllmxDtos) {
                    xzllmxDto.setLrry(xzllglDto.getXgry());
                    xzllmxDto.setXzllmxid(StringUtil.generateUUID());
                    xzllmxDto.setXzllid(xzllglDto.getXzllid());
                    XzkcxxDto xzkcxxDto=new XzkcxxDto();
                    xzkcxxDto.setXzkcid(xzllmxDto.getXzkcid());
                    BigDecimal yds=new BigDecimal(xzllmxDto.getYds());
                    BigDecimal qlsl=new BigDecimal(xzllmxDto.getQlsl());
                    xzkcxxDto.setYds(String.valueOf(yds.add(qlsl)));
                    list.add(xzkcxxDto);
                }
                result = xzllmxService.insertList(xzllmxDtos);
                if (!result) {
                    throw new BusinessException("msg", "领料明细信息保存失败！");
                }
            }
            if(!CollectionUtils.isEmpty(list)){
                result =xzkcxxService.updateList(list);
                if (!result) {
                    throw new BusinessException("msg", "更新行政库存失败！");
                }
            }
        }
        return true;
    }


    /**
     * 新增保存行政领料信息
     * @param xzllglDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addReceiveAdminMateriel(XzllglDto xzllglDto) throws BusinessException {
        boolean result;
        xzllglDto.setZt(StatusEnum.CHECK_NO.getCode());
        xzllglDto.setXzllid(StringUtil.generateUUID());
        if ("1".equals(xzllglDto.getLllx())){
            xzllglDto.setLllx("1");
        }else {
            xzllglDto.setLllx("0");
        }
        //修改行政领料管理信息
        int isSuccess = dao.insert(xzllglDto);
        if (isSuccess == 0) {
            throw new BusinessException("msg", "领料信息保存失败！");
        }
        //保存货物领料信息
        List<XzllmxDto> xzllmxDtos = JSON.parseArray(xzllglDto.getLlmx_json(), XzllmxDto.class);
        if(!CollectionUtils.isEmpty(xzllmxDtos)) {
            List<XzkcxxDto> list=new ArrayList<>();
            List<String> ids=new ArrayList<>();
            for (XzllmxDto xzllmxDto : xzllmxDtos) {
                xzllmxDto.setLrry(xzllglDto.getLrry());
                xzllmxDto.setXzllmxid(StringUtil.generateUUID());
                xzllmxDto.setXzllid(xzllglDto.getXzllid());
                XzkcxxDto xzkcxxDto=new XzkcxxDto();
                xzkcxxDto.setXzkcid(xzllmxDto.getXzkcid());
                BigDecimal yds=new BigDecimal(xzllmxDto.getYds());
                BigDecimal qlsl=new BigDecimal(xzllmxDto.getQlsl());
                xzkcxxDto.setYds(String.valueOf(yds.add(qlsl)));
                list.add(xzkcxxDto);
                ids.add(xzllmxDto.getXzkcid());
            }
            result =xzkcxxService.updateList(list);
            if (!result) {
                throw new BusinessException("msg", "更新行政库存失败！");
            }
            result = xzllmxService.insertList(xzllmxDtos);
            if (!result) {
                throw new BusinessException("msg", "领料明细信息保存失败！");
            }
            XzllcglDto xzllcglDto=new XzllcglDto();
            xzllcglDto.setRyid(xzllglDto.getLrry());
            if ("1".equals(xzllglDto.getDdbcbj())){
                xzllcglDto.setIds(ids);
            }
            xzllcglService.delete(xzllcglDto);

        }
        return true;
    }


    /**
     * 出库保存行政领料信息
     * @param xzllglDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean releaseSaveReceiveAdminMateriel(XzllglDto xzllglDto) throws BusinessException{
        //修改行政领料管理信息
        boolean result=dao.modReceiveAdminMateriel(xzllglDto);
        if(!result) {
            throw new BusinessException("msg","领料信息保存失败！");
        }
        if ("1".equals(xzllglDto.getLllx())){
            SbysDto sbysDto=new SbysDto();
            sbysDto.setXzllid(xzllglDto.getXzllid());
            sbysDto.setTzzt("1");
            sbysService.update(sbysDto);
        }
//        List<XzllmxDto> dtos = xzllmxService.getDtoXzllmxListByXzllid(xzllglDto.getXzllid());
        List<XzkcxxDto> list=new ArrayList<>();
//        if(!CollectionUtils.isEmpty(dtos)) {
//            for (XzllmxDto xzllmxDto : dtos) {
//                XzkcxxDto xzkcxxDto=new XzkcxxDto();
//                xzkcxxDto.setXzkcid(xzllmxDto.getXzkcid());
//                xzkcxxDto.setYds(xzllmxDto.getYds());
//                list.add(xzkcxxDto);
//            }
//        }
        //保存货物领料信息
        List<XzllmxDto> xzllmxDtos = JSON.parseArray(xzllglDto.getLlmx_json(), XzllmxDto.class);
        if(!CollectionUtils.isEmpty(xzllmxDtos)) {
            for (XzllmxDto xzllmxDto : xzllmxDtos) {
                xzllmxDto.setXgry(xzllglDto.getXgry());
                XzkcxxDto xzkcxxDto=new XzkcxxDto();
                xzkcxxDto.setXzkcid(xzllmxDto.getXzkcid());
                xzkcxxDto.setKcl(xzllmxDto.getCksl());
                xzkcxxDto.setYds(xzllmxDto.getYds());
                xzkcxxDto.setUpdateFlag("subtract");
                list.add(xzkcxxDto);
            }
            result = xzllmxService.updateList(xzllmxDtos);
            if (!result) {
                throw new BusinessException("msg", "领料明细信息更新失败！");
            }
        }
        if(!CollectionUtils.isEmpty(list)){
            result =xzkcxxService.updateList(list);
            if (!result) {
                throw new BusinessException("msg", "更新行政库存失败！");
            }
        }
        List<XzkcxxDto> xzkcxxDtos=xzkcxxService.getXzkcxxs(list);
        for (XzkcxxDto xzkcxxDto:xzkcxxDtos ) {
            if (xzkcxxDto.getAqkc() != null) {
                if (Double.parseDouble(xzkcxxDto.getKcl()) < Double.parseDouble(xzkcxxDto.getAqkc())) {
                    // 发送钉钉消息
                    JcsjDto jcsjDto = new JcsjDto();
                    jcsjDto.setJclb("DINGMESSAGETYPE");
                    jcsjDto.setCsdm("SAFETY_STOCK");
                    List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
                    String title = xxglService.getMsg("ICOMM_SH00013");
                    String message = xxglService.getMsg("ICOMM_KC00001", xzkcxxDto.getHwmc(), xzkcxxDto.getKcl(),xzkcxxDto.getKw(), xzkcxxDto.getAqkc());
                    if(!CollectionUtils.isEmpty(ddxxglDtos)) {
                        for (DdxxglDto ddxxglDto : ddxxglDtos) {
                            if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
                                talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), title, message);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * 导出
     *
     * @param xzllglDto
     * @return
     */
    public int getCountForSearchExp(XzllglDto xzllglDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(xzllglDto);
    }

    /**
     * 根据搜索条件获取导出信息
     *
     * @return
     */
    public List<XzllglDto> getListForSearchExp(Map<String, Object> params) {
        XzllglDto xzllglDto = (XzllglDto) params.get("entryData");
        queryJoinFlagExport(params, xzllglDto);
        return dao.getListForSearchExp(xzllglDto);
    }

    /**
     * 根据选择信息获取导出信息
     *
     * @return
     */
    public List<XzllglDto> getListForSelectExp(Map<String, Object> params) {
        XzllglDto xzllglDto = (XzllglDto) params.get("entryData");
        queryJoinFlagExport(params, xzllglDto);
        return dao.getListForSelectExp(xzllglDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, XzllglDto xzllglDto) {
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
        xzllglDto.setSqlParam(sqlcs);
    }

}
