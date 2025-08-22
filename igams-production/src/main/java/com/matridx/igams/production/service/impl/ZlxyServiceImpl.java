package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
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
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.production.dao.entities.ZlxyDto;
import com.matridx.igams.production.dao.entities.ZlxyModel;
import com.matridx.igams.production.dao.entities.ZlxymxDto;
import com.matridx.igams.production.dao.post.IZlxyDao;
import com.matridx.igams.production.service.svcinterface.IZlxyService;
import com.matridx.igams.production.service.svcinterface.IZlxymxService;
import com.matridx.igams.production.util.XWPFContractUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
@Service
public class ZlxyServiceImpl extends BaseBasicServiceImpl<ZlxyDto, ZlxyModel, IZlxyDao> implements IZlxyService, IAuditService {
    @Autowired
    private IZlxymxService zlxymxService;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    DingTalkUtil talkUtil;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IShgcService shgcService;
    @Autowired
    ICommonService commonService;
    @Autowired(required=false)
    private final Logger log = LoggerFactory.getLogger(ZlxyServiceImpl.class);
    @Autowired
    IDdfbsglService ddfbsglService;
    @Autowired
    IDdspglService ddspglService;
    @Autowired
    ICommonService commonservice;
    @Autowired
    IShlcService shlcService;
    @Autowired
    IShxxService shxxService;
    @Autowired
    IDdxxglService ddxxglService;
    @Value("${matridx.systemflg.systemname:}")
    private String systemname;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Autowired
    IJcsjService jcsjService;
    @Value("${matridx.wechat.registerurl:}")
    private String registerurl;
    @Value("${matridx.systemflg.contractemail:}")
    private String contractemail;
    @Value("${matridx.fileupload.releasePath}")
    private String releaseFilePath;
    @Value("${matridx.fileupload.tempPath}")
    private String tempPath;
    @Value("${matridx.ftp.url:}")
    private String FTP_URL = null;

    @Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
    private String DOC_OK = null;

    @Autowired(required = false)
    private AmqpTemplate amqpTempl;
    /**
     * 新增保存
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveAgreement(ZlxyDto zlxyDto){
        ZlxyDto zlxyDto_t=new ZlxyDto();
        zlxyDto_t.setXgry(zlxyDto.getLrry());
        zlxyDto_t.setGysid(zlxyDto.getGysid());
        zlxyDto_t.setHtxj("0");
        dao.modHtxjByGysid(zlxyDto_t);
        zlxyDto.setZt(StatusEnum.CHECK_NO.getCode());
        zlxyDto.setHtxj("1");
        zlxyDto.setZlxyid(StringUtil.generateUUID());
        int insert = dao.insert(zlxyDto);
        if(insert==0){
            return false;
        }
        if(!CollectionUtils.isEmpty(zlxyDto.getFjids())) {
            for (int i = 0; i < zlxyDto.getFjids().size(); i++) {
                boolean saveFile = fjcfbService.save2RealFile(zlxyDto.getFjids().get(i),zlxyDto.getZlxyid());
                if(!saveFile)
                    return false;
            }
        }
        List<ZlxymxDto> list = JSON.parseArray(zlxyDto.getZlxymx_json(), ZlxymxDto.class);
        if(!CollectionUtils.isEmpty(list)) {
            for(ZlxymxDto zlxymxDto:list){
                zlxymxDto.setZlxyid(zlxyDto.getZlxyid());
                zlxymxDto.setZlxymxid(StringUtil.generateUUID());
                zlxymxDto.setLrry(zlxyDto.getLrry());
                if (StringUtil.isNotBlank(zlxymxDto.getWlid())){
                    zlxymxDto.setSjxmh(null);
                }
                else if (StringUtil.isBlank(zlxymxDto.getFwmc())&&StringUtil.isNotBlank(zlxymxDto.getWlmc())){
                    zlxymxDto.setFwmc(zlxymxDto.getWlmc());
                }
            }
            return zlxymxService.insertList(list);
        }
        return true;
    }

    /**
     * 修改保存
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveAgreement(ZlxyDto zlxyDto){
            if (!zlxyDto.getGysid().equals(zlxyDto.getYgysid())){
                ZlxyDto zlxyDto_t=dao.selectLastDto(zlxyDto.getGysid());
                if (null!=zlxyDto_t){
                    if (zlxyDto_t.getKssj().compareTo(zlxyDto.getKssj())<0){
                        zlxyDto.setHtxj("1");
                        zlxyDto_t.setHtxj("0");
                    }else if (zlxyDto_t.getKssj().compareTo(zlxyDto.getKssj())==0&&zlxyDto_t.getCjsj().compareTo(zlxyDto.getCjsj())<0){
                        zlxyDto.setHtxj("1");
                        zlxyDto_t.setHtxj("0");
                    }
                    else {
                        zlxyDto.setHtxj("0");
                        zlxyDto_t.setHtxj("1");
                    }
                    dao.update(zlxyDto_t);
                }else {
                    zlxyDto.setHtxj("1");
                }

            }
        int update = dao.update(zlxyDto);
        if(update==0){
            return false;
        }
            if (!zlxyDto.getGysid().equals(zlxyDto.getYgysid())){
                ZlxyDto zlxyDto1=dao.selectLastDto(zlxyDto.getYgysid());
                if (null!=zlxyDto1){
                    zlxyDto1.setHtxj("1");
                    dao.update(zlxyDto1);
                }
            }

        if(!CollectionUtils.isEmpty(zlxyDto.getFjids())) {
            for (int i = 0; i < zlxyDto.getFjids().size(); i++) {
                boolean saveFile = fjcfbService.save2RealFile(zlxyDto.getFjids().get(i),zlxyDto.getZlxyid());
                if(!saveFile)
                    return false;
            }
        }
        //先删除后新增
        ZlxymxDto zlxymxDto=new ZlxymxDto();
        zlxymxDto.setZlxyid(zlxyDto.getZlxyid());
        zlxymxService.deleteInfo(zlxymxDto);
        List<ZlxymxDto> list = JSON.parseArray(zlxyDto.getZlxymx_json(), ZlxymxDto.class);
        if(!CollectionUtils.isEmpty(list)) {
            for(ZlxymxDto zlxymxDto_t:list){
                zlxymxDto_t.setZlxyid(zlxyDto.getZlxyid());
                zlxymxDto_t.setZlxymxid(StringUtil.generateUUID());
                zlxymxDto_t.setLrry(zlxyDto.getXgry());
                if (StringUtil.isNotBlank(zlxymxDto_t.getWlid())){
                    zlxymxDto_t.setSjxmh(null);
                }else if (StringUtil.isBlank(zlxymxDto_t.getFwmc())&&StringUtil.isNotBlank(zlxymxDto_t.getWlmc())){
                    zlxymxDto_t.setFwmc(zlxymxDto_t.getWlmc());
                }
            }
            return zlxymxService.insertList(list);
        }
        return true;
    }

    /**
     * 删除
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delAgreement(ZlxyDto zlxyDto){
        List<String> gysids=dao.selectGysids(zlxyDto);
        int delete = dao.delete(zlxyDto);
        if(delete==0){
            return false;
        }
        List<ZlxyDto> zlxyDtos=dao.selectZlxyDtos(gysids);
        dao.updateList(zlxyDtos);
        ZlxymxDto zlxymxDto=new ZlxymxDto();
        zlxymxDto.setIds(zlxyDto.getIds());
        zlxymxDto.setScry(zlxyDto.getScry());
        boolean deleteInfo = zlxymxService.delete(zlxymxDto);
        if(!deleteInfo){
            return false;
        }
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwids(zlxyDto.getIds());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_QUALITY_AGREEMENT_FILE.getCode());
        fjcfbService.deleteByYwidsAndYwlx(fjcfbDto);
        return true;
    }

    @Override
    public List<ZlxyDto> getPagedAuditAgreement(ZlxyDto zlxyDto) {
        List<ZlxyDto> t_List= dao.getPagedAuditAgreement(zlxyDto);

        if (CollectionUtils.isEmpty(t_List))
            return t_List;

        List<ZlxyDto> sqList = dao.getAuditListAgreemen(t_List);

        commonService.setSqrxm(sqList);

        return sqList;
    }

    @Override
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        ZlxyDto zlxyDto=(ZlxyDto) baseModel;
        zlxyDto.setXgry(operator.getYhid());
        if (StringUtil.isNotBlank(zlxyDto.getYgysid())){
            return modSaveAgreement(zlxyDto);
        }
        return true;
    }

    @Override
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        String ICOMM_ZLXY00001 = xxglService.getMsg("ICOMM_ZLXY00001");
        String ICOMM_ZLXY00002 = xxglService.getMsg("ICOMM_ZLXY00002");
        String ICOMM_ZLXY00005 = xxglService.getMsg("ICOMM_ZLXY00005");
        String ICOMM_ZLXY00006 = xxglService.getMsg("ICOMM_ZLXY00006");
        for (ShgcDto shgcDto : shgcList) {
            ZlxyDto zlxyDto=new ZlxyDto();
            zlxyDto.setZlxyid(shgcDto.getYwid());
            zlxyDto.setXgry(shgcDto.getYhid());
            ZlxyDto zlxyDto_t=getDtoById(zlxyDto.getZlxyid());
            if (StringUtil.isBlank(zlxyDto_t.getBz())){
                zlxyDto_t.setBz("");
            }
            List<SpgwcyDto> spgwcyDtos=shgcDto.getSpgwcyDtos();
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                String ICOMM_ZLXY00003 = xxglService.getMsg("ICOMM_ZLXY00003");
                String ICOMM_ZLXY00004 = xxglService.getMsg("ICOMM_ZLXY00004");
                zlxyDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // 发送钉钉消息
                if(!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_ZLXY00003,
                                    StringUtil.replaceMsg(ICOMM_ZLXY00004, zlxyDto_t.getGysmc(), zlxyDto_t.getFzrmc(), zlxyDto_t.getZlxybh(), zlxyDto_t.getBz(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            }else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                zlxyDto.setZt(StatusEnum.CHECK_PASS.getCode());
                if(!CollectionUtils.isEmpty(spgwcyDtos)) {
                    int size = spgwcyDtos.size();
                    for (int i = 0; i < size; i++) {
                        if (StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),spgwcyDtos.get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_ZLXY00005,
                                    StringUtil. replaceMsg(ICOMM_ZLXY00006, zlxyDto_t.getGysmc(),zlxyDto_t.getFzrmc(),zlxyDto_t.getZlxybh(),zlxyDto_t.getBz(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }else {
                zlxyDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                ZlxyDto t_zlxyDto=getDtoById(shgcDto.getYwid());
                if("1".equals(shgcDto.getXlcxh()) && shgcDto.getShxx()==null) {
                    try {
                        // 根据申请部门查询审核流程
                        List<DdxxglDto> ddxxglDtos = ddxxglService.getProcessByJgid(t_zlxyDto.getSqbm(), DdAuditTypeEnum.SP_HT.getCode());
                        if(CollectionUtils.isEmpty(ddxxglDtos))
                            throw new BusinessException("msg","未找到所属部门审核流程！");
                        if(ddxxglDtos.size() > 1)
                            throw new BusinessException("msg","找到多条所属部门审核流程！");
                        String spr = ddxxglDtos.get(0).getSpr();
                        String csr = ddxxglDtos.get(0).getCsr();
                        //如果修改了钉钉审批流程则采用修改后的钉钉审批流程
                        if(StringUtil.isNotBlank(t_zlxyDto.getDdspid())) {
                            DdxxglDto ddxxglDto=ddxxglService.getProcessBySpid(t_zlxyDto.getDdspid());
                            if(ddxxglDto!=null) {
                                if(StringUtils.isNotBlank(ddxxglDto.getSpr())) {
                                    spr=ddxxglDto.getSpr();
                                }
                                if(StringUtils.isNotBlank(ddxxglDto.getCsr())) {
                                    csr=ddxxglDto.getCsr();
                                }
                            }
                        }
                        if(!StringUtils.isNotBlank(spr)) {
                            spr="";
                        }
                        if(!StringUtils.isNotBlank(csr)) {
                            csr="";
                        }
                        if(StringUtil.isNotBlank(t_zlxyDto.getCsrs())) {
                            String[] split = t_zlxyDto.getCsrs().split(",|，");
                            // 根据抄送人查询钉钉ID
                            List<String> yhids = Arrays.asList(split);
                            List<User> yhxxs = commonservice.getDdidByYhids(yhids);
                            List<String> ddids=new ArrayList<>();
                            if(!CollectionUtils.isEmpty(yhxxs)) {
                                for(User user : yhxxs) {
                                    if(StringUtils.isBlank(user.getDdid()))
                                        throw new BusinessException("ICOM99019","该用户("+user.getZsxm()+")未获取到钉钉ID！");
                                    ddids.add(user.getDdid());
                                }
                            }
                            if(!CollectionUtils.isEmpty(ddids)) {
                                ddids.removeIf(StringUtil::isBlank);
                                csr += "," + String.join(",", ddids);
                                if(",".equals(csr.substring(0,1))) {
                                    csr=csr.substring(1);
                                }
                            }
                        }
                        //提交审核至钉钉审批
                        String cc_list = csr; // 抄送人信息
                        String approvers = spr; // 审核人信息
                        Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "质量协议审批");//获取审批模板ID
                        String templateCode=(String) template.get("message");
                        //获取申请人信息(合同申请应该为采购部门)
                        User user=new User();
                        user.setYhid(shgcDto.getSqr());
                        user=commonservice.getUserInfoById(user);
                        if(user==null)
                            throw new BusinessException("ICOM99019","未获取到申请人信息！");
                        if(StringUtils.isBlank(user.getDdid()))
                            throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
                        String userid=user.getDdid();
                        String dept=user.getJgid ();
                        //审批人与申请人做比较，若流程中前面与申请人相同则省略相同的流程
//                        String[] t_sprs=spr.split(",");
//                        List<String> sprs_list=Arrays.asList(t_sprs);
//                        String t_str="";
//                        if(sprs_list!=null && sprs_list.size()>0) {
//                            boolean sflx=true;
//                            for(int i=0;i<sprs_list.size();i++) {
//                                if(StringUtils.isNotBlank(userid)) {
//                                    if(sflx) {
//                                        if(!userid.equals(sprs_list.get(i))) {
//                                            t_str=t_str+","+sprs_list.get(i);
//                                            sflx=false;
//                                        }
//                                    }else {
//                                        t_str=t_str+","+sprs_list.get(i);
//                                    }
//                                }
//                            }
//                            if(t_str.length()>0)
//                                approvers=t_str.substring(1);
//                        }
                        Map<String,String> map=new HashMap<>();
                        ZlxymxDto zlxymxDto=new ZlxymxDto();
                        zlxymxDto.setZlxymxid(t_zlxyDto.getZlxyid());
                        List<Map<String,String>> mxlist=new ArrayList<>();
                        map.put("所属公司", systemname);
                        map.put("供应商", t_zlxyDto.getGysmc());
                        map.put("供方编号", t_zlxyDto.getGfbh());
                        map.put("负责人",t_zlxyDto.getFzrmc());
                        map.put("质量协议编号",t_zlxyDto.getZlxybh());
                        map.put("供方管理类别",t_zlxyDto.getGfgllbmc());
                        map.put("协议开始时间", t_zlxyDto.getKssj());
                        map.put("协议结束时间", t_zlxyDto.getDqsj());
                        map.put("详细","详细信息："+applicationurl+urlPrefix+"/ws/production/getQualityUrl?zlxyid="+t_zlxyDto.getZlxyid());
                        Map<String,Object> t_map=talkUtil.createInstance(operator.getYhm(), templateCode, approvers, cc_list, userid, dept, map,mxlist,null);
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
                                ZlxyDto zlxy_dd=new ZlxyDto();
                                zlxy_dd.setDdslid(String.valueOf(result_map.get("process_instance_id")));
                                zlxy_dd.setXgry(operator.getYhid());
                                zlxy_dd.setZlxyid(t_zlxyDto.getZlxyid());
                                dao.update(zlxy_dd);
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

                }
                // 发送钉钉消息
                if(!CollectionUtils.isEmpty(spgwcyDtos)) {
                    try {
                        for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                            if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_ZLXY00001, StringUtil.replaceMsg(ICOMM_ZLXY00002,
                                        zlxyDto_t.getGysmc(), zlxyDto_t.getFzrmc(), zlxyDto_t.getZlxybh(), zlxyDto_t.getBz(),
                                        DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
                //发送钉钉消息--取消审核人员
                if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())) {
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005", operator.getZsxm(),shgcDto.getShlbmc(),
                                    DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
            }
            update(zlxyDto);
        }
        return true;
    }

    /**
     * 质量协议审批回调
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean callbackQualityAduit(JSONObject obj, HttpServletRequest request, Map<String,Object> t_map) throws BusinessException {
        ZlxyDto zlxyDto = new ZlxyDto();
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
            if(StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {
                if(!CollectionUtils.isEmpty(ddspgllist)) {
                    ddspglDto=ddspgllist.get(0);
                }
            }
            zlxyDto.setDdslid(processInstanceId);
            // 根据钉钉审批实例ID查询关联请购单
            zlxyDto = dao.getDtoByDdslid(zlxyDto);
            // 若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if (zlxyDto != null) {
                //获取审批人信息
                User user=new User();
                user.setDdid(staffId);
                user.setWbcxid(wbcxidString);
                user = commonservice.getByddwbcxid(user);
                if (user == null)
                    return false;
                log.error("user-yhid:" + user.getYhid() + "---user-zsxm:" + user.getZsxm() + "----user-yhm"
                        + user.getYhm() + "---user-ddid" + user.getDdid());
                // 获取审批人角色信息
                List<ZlxyDto> dd_sprjs = dao.getSprjsBySprid(user.getYhid());
                // 获取当前审核过程
                ShgcDto t_shgcDto = shgcService.getDtoByYwid(zlxyDto.getZlxyid());
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
                    if(!CollectionUtils.isEmpty(dd_sprjs)) {
                        // 审批正常结束（同意或拒绝）
                        ShxxDto shxxDto = new ShxxDto();
                        shxxDto.setGcid(t_shgcDto.getGcid());
                        shxxDto.setLcxh(t_shgcDto.getXlcxh());
                        shxxDto.setShlb(t_shgcDto.getShlb());
                        shxxDto.setShyj(remark);
                        shxxDto.setAuditType(t_shgcDto.getShlb());
                        shxxDto.setYwid(zlxyDto.getZlxyid());
                        if (StringUtil.isNotBlank(finishTime)){
                            shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
                        }
                        log.error("shxxDto-lcxh:" + t_shgcDto.getXlcxh() + " shxxDto-shlb:"
                                + t_shgcDto.getShlb() + " shxxDto-shyj:" + remark + " shxxDto-ywid:"
                                + zlxyDto.getZlxyid());
                        String lastlcxh = null;// 回退到指定流程

                        if (("finish").equals(type)) {
                            // 如果审批通过,同意
                            if (("agree").equals(result)) {
                                log.error("同意------");
                                shxxDto.setSftg("1");
                                if (StringUtils.isBlank(t_shgcDto.getXlcxh()))
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
                                log.error("DingTalkMaterPurchaseAudit(钉钉合同审批转发提醒)------转发人员:" + user.getZsxm()
                                        + ",人员ID:" + user.getYhm() + ",质量协议编号:" + zlxyDto.getZlxybh() + ",单据ID:"
                                        + zlxyDto.getZlxyid() + ",转发时间:" + date);
                                return true;
                            }
                            // 调用审核方法
                            Map<String, List<String>> map = ToolUtil.reformRequest(request);
                            log.error("map:"+map);
                            List<String> list = new ArrayList<>();
                            list.add(zlxyDto.getZlxyid());
                            map.put("zlxyid", list);
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
                            shxxDto.setYwglmc(zlxyDto.getZlxybh());
                            ZlxyDto t_zlxyDto = new ZlxyDto();
                            t_zlxyDto.setZlxyid(zlxyDto.getZlxyid());
                            dao.updateDdslidToNull(t_zlxyDto);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            // 调用审核方法
                            Map<String, List<String>> map = ToolUtil.reformRequest(request);
                            List<String> list = new ArrayList<>();
                            list.add(zlxyDto.getZlxyid());
                            map.put("zlxyid", list);
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
                        shxx.setYwid(zlxyDto.getZlxyid());
                        shxx.setShlb(AuditTypeEnum.AUDIT_QUALITYAGREEMENT.getCode());
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
    public Map<String, Object> getParamForContract(ZlxyDto zlxyDto) {
        XWPFContractUtil xwpfContractUtil = new XWPFContractUtil();
        Map<String, Object> map = dao.getContractMap(zlxyDto);
        String kssj = map.get("kssj") != null ? map.get("kssj").toString() : null;
        if (StringUtil.isNotBlank(kssj)) {
            int lastIndex = kssj.lastIndexOf("-");
            if (lastIndex > -1) {
                if (StringUtil.isNotBlank(kssj)) {
                    String year = kssj.substring(0, 4);
                    if (StringUtil.isNotBlank(year)) {
                        map.put("yearstart", year);
                    }
                    String mon = kssj.substring(5, 7);
                    if (StringUtil.isNotBlank(mon)) {
                        map.put("monstart", mon);
                    }
                    String day = kssj.substring(8, 10);
                    if (StringUtil.isNotBlank(day)) {
                        map.put("daystart", day);
                    }
                }
            }
        }
        String dqsj = map.get("dqsj") != null ? map.get("dqsj").toString() : null;
        if (StringUtil.isNotBlank(dqsj)) {
            int lastIndex = dqsj.lastIndexOf("-");
            if (lastIndex > -1) {
                if (StringUtil.isNotBlank(dqsj)) {
                    String year = dqsj.substring(0, 4);
                    if (StringUtil.isNotBlank(year)) {
                        map.put("yearend", year);
                    }
                    String mon = dqsj.substring(5, 7);
                    if (StringUtil.isNotBlank(mon)) {
                        map.put("monend", mon);
                    }
                    String day = dqsj.substring(8, 10);
                    if (StringUtil.isNotBlank(day)) {
                        map.put("dayend", day);
                    }
                }
            }
        }
        List<Map<String, String>> listMap = dao.getContractListMap(zlxyDto);
        map.put("buyerEmail", contractemail);
        map.put("htmx", listMap);
        map.put("releaseFilePath", releaseFilePath); // 正式文件路径
        map.put("tempPath", tempPath); // 临时文件路径
        return xwpfContractUtil.replaceQualityContract(map, fjcfbService, FTP_URL, DOC_OK, amqpTempl);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean formalSaveAgreementContract(ZlxyDto zlxyDto) {
            // 文件复制到正式文件夹，插入信息至正式表
            if(!CollectionUtils.isEmpty(zlxyDto.getFjids())) {
                for (int i = 0; i < zlxyDto.getFjids().size(); i++) {
                    fjcfbService.save2RealFile(zlxyDto.getFjids().get(i), zlxyDto.getZlxyid());
                }
            }
            update(zlxyDto);
        return true;
    }


    @Override
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                ZlxyDto zlxyDto = new ZlxyDto();
                zlxyDto.setXgry(operator.getYhid());
                zlxyDto.setZlxyid(shgcDto.getYwid());
                zlxyDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(zlxyDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                ZlxyDto zlxyDto = new ZlxyDto();
                zlxyDto.setXgry(operator.getYhid());
                zlxyDto.setZlxyid(shgcDto.getYwid());
                zlxyDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(zlxyDto);
                //OA取消审批的同时组织钉钉审批
                ZlxyDto zlxyDto_t = getDtoById(zlxyDto.getZlxyid());
                if(zlxyDto_t!=null && org.apache.commons.lang3.StringUtils.isNotBlank(zlxyDto_t.getDdslid())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), zlxyDto_t.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map=JSON.parseObject(success,Map.class);
                    boolean bo1= (boolean) result_map.get("success");
                    if(bo1){
                        dao.updateDdslidToNull(zlxyDto_t);
                    }else {
                        log.error("质量协议撤回审批异常："+JSON.toJSONString(cancelResult));
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
        ZlxyDto zlxyDto = new ZlxyDto();
        zlxyDto.setIds(ids);
        List<ZlxyDto> dtoList = dao.getDtoList(zlxyDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(ZlxyDto dto:dtoList){
                list.add(dto.getZlxyid());
            }
        }
        map.put("list",list);
        return map;
    }

    @Override
    public List<ZlxyDto> getPagedDtoList(ZlxyDto zlxyDto) {
        List<ZlxyDto> list = dao.getPagedDtoList(zlxyDto);
        try {
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_QUALITYAGREEMENT.getCode(), "zt", "zlxyid",
                    new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        return list;
    }

    /**
     * 导出
     */
    public int getCountForSearchExp(ZlxyDto zlxyDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(zlxyDto);
    }

    /**
     * 根据搜索条件获取导出信息
     */
    public List<ZlxyDto> getListForSearchExp(Map<String, Object> params) {
        ZlxyDto zlxyDto = (ZlxyDto) params.get("entryData");
        queryJoinFlagExport(params, zlxyDto);
        return dao.getListForSearchExp(zlxyDto);
    }

    /**
     * 根据选择信息获取导出信息
     */
    public List<ZlxyDto> getListForSelectExp(Map<String, Object> params) {
        ZlxyDto zlxyDto = (ZlxyDto) params.get("entryData");
        queryJoinFlagExport(params, zlxyDto);
        return dao.getListForSelectExp(zlxyDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, ZlxyDto zlxyDto) {
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
        zlxyDto.setSqlParam(sqlcs);
    }

    @Override
    public String getAgreementContractSerial(String prefix,String num) {
        return dao.getAgreementContractSerial(prefix,num);
    }

    @Override
    public boolean isZlxybhRepeat(String zlxybh, String zlxyid) {
        // TODO Auto-generated method stub
        if (StringUtil.isNotBlank(zlxybh)) {
            ZlxyDto zlxyDto = new ZlxyDto();
            zlxyDto.setZlxybh(zlxybh);
            zlxyDto.setZlxyid(zlxyid);
            List<ZlxyDto> zlxyDtos = dao.getListByZlxybh(zlxyDto);
            return CollectionUtils.isEmpty(zlxyDtos);
        }
        return true;
    }
}
