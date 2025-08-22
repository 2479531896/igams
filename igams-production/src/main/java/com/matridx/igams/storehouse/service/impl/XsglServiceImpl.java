package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.BaseModel;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.production.dao.entities.WlglDto;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.dao.post.IXsglDao;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IDkjejlService;
import com.matridx.igams.storehouse.service.svcinterface.IFhmxService;
import com.matridx.igams.storehouse.service.svcinterface.IJcjyglService;
import com.matridx.igams.storehouse.service.svcinterface.ILlglService;
import com.matridx.igams.storehouse.service.svcinterface.IXsglService;
import com.matridx.igams.storehouse.service.svcinterface.IXsmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;

import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XsglServiceImpl extends BaseBasicServiceImpl<XsglDto, XsglModel, IXsglDao> implements IXsglService, IAuditService, IFileImport {

    @Autowired
    ICommonService commonservice;
    @Autowired
    IXsmxService xsmxService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    ICkhwxxService ckhwxxService;
    @Autowired
    IWlglService wlglService;
    @Autowired
    IRdRecordService rdRecordService;
    @Autowired(required=false)
    private final Logger log = LoggerFactory.getLogger(XsglServiceImpl.class);
    @Autowired
    ICommonService commonService;
    @Value("${matridx.wechat.registerurl:}")
    private String registerurl;
    @Value("${matridx.systemflg.systemname:}")
    private String systemname;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Autowired
    IJcsjService jcsjService;
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
    IDdxxglService ddxxglService;
	@Lazy
	@Autowired
    IDkjejlService dkjejlService;
	@Autowired
    ILlglService llglService;
	@Autowired
    IJcjyglService jcjyglService;
    @Autowired
    IFhmxService fhmxService;
    @Value("${matridx.rabbit.systemreceiveflg:}")
    private String systemreceiveflg;
    @Value("${sqlserver.matridxds.flag:}")
    private String matridxdsflag;
    /**
     * 生成oa销售订单号
     * @return
     */
    public String getOAxsddh(String str){
        String oaxsdh="";
        String oAxsddh = dao.getOAxsddh(str);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String now=sdf.format(date);
        String[] strs = now.split("-");
        String year=strs[0];
        String month=strs[1];
        String day=strs[2];
        if(StringUtil.isNotBlank(oAxsddh)){
            oaxsdh="XS-"+year+month+day+"-"+oAxsddh;
        }
        return oaxsdh;
    }

    /**
     * 废弃按钮
     * @param xsglDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean discard(XsglDto xsglDto,List<XsmxDto> list){
        boolean isSuccess;
        // if(!CollectionUtils.isEmpty(list)){
        //     List<CkhwxxDto> cklist=new ArrayList<>();
        //     for(XsmxDto dto:list){
        //         CkhwxxDto ckhwxxDto=new CkhwxxDto();
        //         ckhwxxDto.setYds(dto.getSl());
        //         ckhwxxDto.setYdsbj("0");
        //         ckhwxxDto.setCkhwid(dto.getCkhwid());
        //         cklist.add(ckhwxxDto);
        //     }
        //     int i = ckhwxxService.updateYdsList(cklist);
        //     if(i<=0){
        //         return false;
        //     }
        // }
        XsmxDto xsmxDto=new XsmxDto();
        xsmxDto.setIds(xsglDto.getIds());
        xsmxDto.setXgry(xsglDto.getScry());
        xsmxService.discard(xsmxDto);
        isSuccess= dao.discard(xsglDto);
        return isSuccess;
    }

    /**
     * 审核列表
     * @param xsglDto
     * @return
     */
    public List<XsglDto> getPagedAuditSale(XsglDto xsglDto){
        // 获取人员ID和履历号
        List<XsglDto> t_sbyzList= dao.getPagedAuditSale(xsglDto);

        if(CollectionUtils.isEmpty(t_sbyzList))
            return t_sbyzList;

        List<XsglDto> sqList = dao.getAuditListSale(t_sbyzList);

        commonservice.setSqrxm(sqList);

        return sqList;
    }

    /**
     * 验证
     * @param xsglDto
     * @return
     */
    public XsglDto verify(XsglDto xsglDto){
        return dao.verify(xsglDto);
    }

    /**
     * 新增
     * @param xsglDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertDto(XsglDto xsglDto){
        xsglDto.setXsid(StringUtil.generateUUID());
        xsglDto.setHtdh(xsglDto.getHtid());
        BigDecimal zje=new BigDecimal(xsglDto.getXszje());
        BigDecimal dkje=new BigDecimal(xsglDto.getDkje());
        xsglDto.setYsk(String.valueOf(zje.subtract(dkje)));
        boolean isSuccess = insert(xsglDto);
        List<XsmxDto> dtos = JSON.parseArray(xsglDto.getXsmx_json(), XsmxDto.class);
        if(!CollectionUtils.isEmpty(dtos)) {
            for(XsmxDto dto:dtos){
                dto.setXsmxid(StringUtil.generateUUID());
                dto.setXsid(xsglDto.getXsid());
                dto.setLrry(xsglDto.getLrry());
                dto.setBj(dto.getHsdj());
                dto.setSuil(xsglDto.getSuil());
                dto.setJyxxid(null);
                dto.setSczt(null);
            }
            isSuccess=xsmxService.insertList(dtos);
        }
        return isSuccess;
    }

    /**
     * 修改
     * @param xsglDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateDto(XsglDto xsglDto){
        BigDecimal zje=new BigDecimal(xsglDto.getXszje());
        BigDecimal dkje=new BigDecimal(xsglDto.getDkje());
        xsglDto.setYsk(String.valueOf(zje.subtract(dkje)));
        xsglDto.setHtdh(xsglDto.getHtid());
        boolean isSuccess = update(xsglDto);
        XsmxDto xsmxDto=new XsmxDto();
        xsmxDto.setXsid(xsglDto.getXsid());
        xsmxDto.setScry(xsglDto.getXgry());
        // List<XsmxDto> listById = xsmxService.getListById(xsmxDto);
        //先将原本明细的预定数还原，防止修改删除明细的时候预定数不变
        // if(listById!=null&&listById.size()>0){
        //     List<CkhwxxDto> list=new ArrayList<>();
        //     for(XsmxDto dto:listById){
        //         CkhwxxDto ckhwxxDto=new CkhwxxDto();
        //         ckhwxxDto.setYds(dto.getYds());
        //         ckhwxxDto.setYdsbj("2");
        //         ckhwxxDto.setCkhwid(dto.getCkhwid());
        //         list.add(ckhwxxDto);
        //     }
        //     int i = ckhwxxService.updateYdsList(list);
        //     if(i<=0){
        //         return false;
        //     }
        // }
        
        List<XsmxDto> dtos = JSON.parseArray(xsglDto.getXsmx_json(), XsmxDto.class);
        if(!CollectionUtils.isEmpty(dtos)) {
            xsmxService.delete(xsmxDto);
            // List<CkhwxxDto> list=new ArrayList<>();
            for(XsmxDto dto:dtos){
                dto.setXsmxid(StringUtil.generateUUID());
                dto.setXsid(xsglDto.getXsid());
                dto.setLrry(xsglDto.getXgry());
                dto.setBj(dto.getHsdj());
                dto.setSuil(xsglDto.getSuil());
                // CkhwxxDto ckhwxxDto=new CkhwxxDto();
                // BigDecimal yds=new BigDecimal(dto.getYds());
                // BigDecimal sl=new BigDecimal(dto.getSl());
                // ckhwxxDto.setYds(String.valueOf(yds.add(sl)));
                // ckhwxxDto.setYdsbj("2");
                // ckhwxxDto.setCkhwid(dto.getCkhwid());
                // list.add(ckhwxxDto);
            }
            // int i = ckhwxxService.updateYdsList(list);
            // if(i<=0){
            //     return false;
            // }
            isSuccess=xsmxService.insertList(dtos);
        }
        return isSuccess;
    }

    /**
     * 删除
     * @param xsglDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delSale(XsglDto xsglDto) throws BusinessException{
        boolean isSuccess = false;
        //获取该销售单的总到款金额
        DkjejlDto dkjejlDto_del = new DkjejlDto();
        XsmxDto xsmxDto_all = new XsmxDto();
        xsmxDto_all.setIds(xsglDto.getIds());
        List<XsmxDto> xsmxDtos = xsmxService.getXsmxByXs(xsmxDto_all);
        List<String> ywids = xsmxDtos.stream().map(XsmxDto::getXsmxid).collect(Collectors.toList());
        dkjejlDto_del.setScry(xsglDto.getScry());
        dkjejlDto_del.setYwids(ywids);
        dkjejlService.deleteByYwids(dkjejlDto_del);
        XsmxDto xsmxDto=new XsmxDto();
        xsmxDto.setIds(xsglDto.getIds());
        xsmxDto.setScry(xsglDto.getScry());
        isSuccess = xsmxService.delete(xsmxDto);
        if (!isSuccess){
            throw new BusinessException("msg","删除销售明细失败");
        }
        isSuccess= delete(xsglDto);
        if (!isSuccess){
            throw new BusinessException("msg","删除销售单失败");
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
        XsglDto xsglDto = (XsglDto) baseModel;
        boolean flag = true;
        if (StringUtil.isNotBlank(xsglDto.getOaxsdh())){
            XsglDto dto = verify(xsglDto);
            if(dto!=null){
                return false;
            }
            xsglDto.setXgry(operator.getYhid());
            flag = updateDto(xsglDto);
        }
        return flag;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        if (shgcList == null || shgcList.isEmpty()) {
            return true;
        }
        for (ShgcDto shgcDto : shgcList) {
            XsglDto xsglDto = new XsglDto();
            xsglDto.setXsid(shgcDto.getYwid());
            xsglDto.setXgry(operator.getYhid());
            xsglDto.setShry(operator.getYhid());
            XsglDto xsglDto_t = getDto(xsglDto);
            List<SpgwcyDto> spgwcyDtos = shgcDto.getSpgwcyDtos();
            // 审核退回
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                xsglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                // List<XsmxDto> list = xsmxService.getDtoList(xsmxDto);
                // if(!CollectionUtils.isEmpty(list)){
                //     List<CkhwxxDto> cklist=new ArrayList<>();
                //     for(XsmxDto dto:list){
                //         CkhwxxDto ckhwxxDto=new CkhwxxDto();
                //         ckhwxxDto.setYds(dto.getSl());
                //         ckhwxxDto.setYdsbj("0");
                //         ckhwxxDto.setCkhwid(dto.getCkhwid());
                //         cklist.add(ckhwxxDto);
                //     }
                //     int i = ckhwxxService.updateYdsList(cklist);
                //     if(i<=0){
                //         return false;
                //     }
                // }

                // 发送钉钉消息
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(),
                                    xxglService.getMsg("ICOMM_SH00026"), xxglService.getMsg("ICOMM_SH00106", operator.getZsxm(), shgcDto.getShlbmc(), xsglDto_t.getOaxsdh(), xsglDto_t.getXsbmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                // 审核通过
            } else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
                xsglDto.setZt(StatusEnum.CHECK_PASS.getCode());
                xsglDto_t.setXgry(xsglDto.getXgry());
                XsmxDto xsmxDto_t=new XsmxDto();
                xsmxDto_t.setXsid(xsglDto.getXsid());
                List<XsmxDto> dtoList = xsmxService.getDtoList(xsmxDto_t);
                if(!"1".equals(systemreceiveflg) && StringUtil.isNotBlank(matridxdsflag)){
                    if (!rdRecordService.determine_Entry(xsglDto_t.getDdrq())){
                        throw new BusinessException("ICOM99019","该月份已结账，不允许再录入U8数据，请修改单据日期!");
                    }
                    Map<String, Object> map = rdRecordService.addU8SaleData(xsglDto_t,dtoList);
                    XsglDto xsglDto_x = (XsglDto) map.get("xsglDto");
                    @SuppressWarnings("unchecked")
                    List<XsmxDto> xsmxlist = (List<XsmxDto>) map.get("xsmxlist");
                    int num = xsmxService.updateList(xsmxlist);
                    if (num <= 0) {
                        throw new BusinessException("msg", "更新明细关联ID失败!");
                    }
                    xsglDto.setGlid(xsglDto_x.getGlid());
                    xsglDto.setU8xsdh(xsglDto_x.getU8xsdh());
                }

                XsmxDto xsmxDto=new XsmxDto();
                xsmxDto.setXsid(xsglDto.getXsid());
                xsmxDto.setSczt("1");
                xsmxService.updateDto(xsmxDto);
                List<DkjejlDto> dkjejlListAdd=new ArrayList<>();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date=new Date();
                String lrsj = simpleDateFormat.format(date);
                for (XsmxDto xsmxDto1:dtoList){
                    //到款金额为空不存
                    if (StringUtil.isNotBlank(xsmxDto1.getDkje())){
                        DkjejlDto dkjejlDto=new DkjejlDto();
                        dkjejlDto.setDkjlid(StringUtil.generateUUID());
                        dkjejlDto.setYwid(xsmxDto1.getXsmxid());
                        dkjejlDto.setDky(xsmxDto1.getDky());
                        dkjejlDto.setDkje(xsmxDto1.getDkje());
                        dkjejlDto.setLrsj(lrsj);
                        dkjejlDto.setYwlx("0");
                        dkjejlDto.setLb("0");
                        dkjejlListAdd.add(dkjejlDto);
                    }
                }
                if (!CollectionUtils.isEmpty(dkjejlListAdd)){
                    dkjejlService.insertList(dkjejlListAdd);
                }
                if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                    for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                        if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(),
                                    xxglService.getMsg("ICOMM_SH00006"), xxglService.getMsg("ICOMM_SH00107",
                                            operator.getZsxm(), shgcDto.getShlbmc(), xsglDto_t.getOaxsdh(),
                                            xsglDto_t.getXsbmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
                List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.AUDIT_PASS_XSTZ.getCode());
                String ywlx = "xs";
                String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
                String ICOMM_SH000101 = xxglService.getMsg("ICOMM_SH000101");
                if (!CollectionUtils.isEmpty(ddxxglDtolist)) {
                    for (DdxxglDto ddxxglDto : ddxxglDtolist) {
                        if (StringUtil.isNotBlank(ddxxglDto.getYhm())) {
                            // 内网访问
                            String internalbtn = applicationurl + urlPrefix
                                    + "/ws/storehouse/receiveMateriel/viewYwxxWithWlxx?ywid=" + xsglDto.getXsid() + "&ywlx=" + ywlx;
                            List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                            OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                            btnJsonList.setTitle("详细");
                            btnJsonList.setActionUrl(internalbtn);
                            btnJsonLists.add(btnJsonList);
                            talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),ddxxglDto.getYhid(),ddxxglDto.getYhm(), ddxxglDto.getDdid(),ICOMM_SH00006,StringUtil.replaceMsg(ICOMM_SH000101,xsglDto_t.getOaxsdh(),xsglDto_t.getXsbmmc(),xsglDto_t.getYwymc(),
                                            xsglDto_t.getKhjcmc(),xsglDto_t.getShdz(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
                                    btnJsonLists,"1");
                        }
                    }
                }
            }else {
                xsglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                if (shgcDto.getXlcxh().equals("1") && AuditTypeEnum.AUDIT_SALEORDER.getCode().equals(shgcDto.getShlb())){
                    try {
                        Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "销售订单");//获取审批模板ID
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
                        map.put("所属公司",systemname);
                        map.put("订单编号", xsglDto_t.getOaxsdh()!=null?xsglDto_t.getOaxsdh():"");
                        List<String> ddids=new ArrayList<>();
                        ddids.add(xsglDto_t.getYwyddid());
                        map.put("所属业务员",JSON.toJSONString(ddids));
                        map.put("购货单位", xsglDto_t.getKhjcmc()!=null?xsglDto_t.getKhjcmc():"");
                        map.put("其他到货要求", xsglDto_t.getYsyq()!=null?xsglDto_t.getYsyq():"");
                        map.put("附件", applicationurl + urlPrefix+"/ws/production/dingTalkViewSale?xsid="+xsglDto_t.getXsid());
                        map.put("备注", xsglDto_t.getBz()!=null?xsglDto_t.getBz():"");
                        Map<String,Object> t_map=talkUtil.createInstance(operator.getYhm(), templateCode,null, null, userid, dept, map,null,null);
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
                                xsglDto.setDdslid(String.valueOf(result_map.get("process_instance_id")));
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
                    if (!CollectionUtils.isEmpty(spgwcyDtos)) {
                        try {
                            for (SpgwcyDto spgwcyDto : spgwcyDtos) {
                                if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
                                    //内网访问
                                    @SuppressWarnings("deprecation")
                                    String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/auditpage/ARAuditor/ARAuditor&type=1&ywzd=xsid&ywid=" + xsglDto_t.getXsid() + "&auditType=" + shgcDto.getAuditType() + "&urlPrefix=" + urlPrefix,StandardCharsets.UTF_8);
                                    List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                                    OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                                    btnJsonList.setTitle("小程序");
                                    btnJsonList.setActionUrl(internalbtn);
                                    btnJsonLists.add(btnJsonList);
                                    talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00003"),
                                            xxglService.getMsg("ICOMM_SH00105", operator.getZsxm(), shgcDto.getShlbmc(), xsglDto_t.getOaxsdh(), xsglDto_t.getXsbmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
                                }
                            }
                        } catch (Exception e) {
                            throw new BusinessException("msg",e.getMessage());
                        }
                    }
                    //发送钉钉消息--取消审核人员
                    if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())){
                        int size = shgcDto.getNo_spgwcyDtos().size();
                        for(int i=0;i<size;i++){
                            if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,xsglDto_t.getOaxsdh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                            }
                        }
                    }
                }
            }
            update(xsglDto);
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
        // TODO Auto-generated method stub
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String xsid = shgcDto.getYwid();
                XsglDto xsglDto = new XsglDto();
                xsglDto.setXsid(xsid);
                xsglDto.setXgry(operator.getYhid());
                xsglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(xsglDto);
            }
        }else {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String xsid = shgcDto.getYwid();
                XsglDto xsglDto = new XsglDto();
                xsglDto.setXsid(xsid);
                xsglDto.setXgry(operator.getYhid());
                xsglDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(xsglDto);
                //OA取消审批的同时组织钉钉审批
                XsglDto xsglDto_t = getDtoById(xsglDto.getXsid());
                if(xsglDto_t!=null && StringUtils.isNotBlank(xsglDto_t.getDdslid())) {
                    Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), xsglDto_t.getDdslid(), "", operator.getDdid());
                    //若撤回成功将实例ID至为空
                    String success=String.valueOf(cancelResult.get("message"));
                    @SuppressWarnings("unchecked")
                    Map<String,Object> result_map=JSON.parseObject(success,Map.class);
                    boolean bo1= (boolean) result_map.get("success");
                    if(bo1)
                        dao.updateDdslidToNull(xsglDto_t);
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
        XsglDto xsglDto = new XsglDto();
        xsglDto.setIds(ids);
        List<XsglDto> dtoList = dao.getDtoList(xsglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(XsglDto dto:dtoList){
                list.add(dto.getXsid());
            }
        }
        map.put("list",list);
        return map;
    }

    /**
     * 列表选中导出
     * @param params
     * @return
     */
    public List<XsglDto> getListForSelectExp(Map<String, Object> params){
        XsglDto xsglDto = (XsglDto) params.get("entryData");
        queryJoinFlagExport(params,xsglDto);
        return dao.getListForSelectExp(xsglDto);
    }

    /**
     * 根据搜索条件获取导出条数
     * @return
     */
    public int getCountForSearchExp(XsglDto xsglDto,Map<String,Object> params){
        return dao.getCountForSearchExp(xsglDto);
    }

    /**
     * 根据搜索条件分页获取导出信息
     * @return
     */
    public List<XsglDto> getListForSearchExp(Map<String,Object> params){
        XsglDto xsglDto = (XsglDto)params.get("entryData");
        queryJoinFlagExport(params,xsglDto);
        return dao.getListForSearchExp(xsglDto);
    }
    /**
     * 列表选中导出
     * @param params
     * @return
     */
    public List<XsglDto> getMAINTABLEListForSelectExp(Map<String, Object> params){
        XsglDto xsglDto = (XsglDto) params.get("entryData");
        xsglDto.setExportFlag("Y");
        queryJoinFlagExport(params,xsglDto);
        return dao.getListForSelectExp(xsglDto);
    }

    /**
     * 根据搜索条件获取导出条数
     * @return
     */
    public int getMAINTABLECountForSearchExp(XsglDto xsglDto,Map<String,Object> params){
        xsglDto.setExportFlag("Y");
        return dao.getCountForSearchExp(xsglDto);
    }

    /**
     * 根据搜索条件分页获取导出信息
     * @return
     */
    public List<XsglDto> getMAINTABLEListForSearchExp(Map<String,Object> params){
        XsglDto xsglDto = (XsglDto)params.get("entryData");
        xsglDto.setExportFlag("Y");
        queryJoinFlagExport(params,xsglDto);
        return dao.getListForSearchExp(xsglDto);
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, XsglDto xsglDto){
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for (DcszDto dcszDto : choseList){
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;
            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd()))
            {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        xsglDto.setSqlParam(sqlParam.toString());
    }

    @Override
    public boolean existCheck(String fieldName, String value) {
        return false;
    }

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages) throws BusinessException {
            XsmxDto xsmxDto = (XsmxDto) baseModel;
            if (StringUtil.isNotBlank(xsmxDto.getWlbm())){
                if (StringUtil.isNotBlank(xsmxService.escapeWlbm(xsmxDto))){
                    xsmxDto.setWlid(xsmxService.escapeWlbm(xsmxDto));
                }
                else
                    throw new BusinessException("msg", "此物料编码不存在");
            }
            if (StringUtil.isNotBlank(xsmxDto.getCplx())){
                if (StringUtil.isNotBlank(xsmxService.escapecplx(xsmxDto))){
                    xsmxDto.setCplx(xsmxService.escapecplx(xsmxDto));
                }
                else
                    throw new BusinessException("msg", "此产品类型不存在");
            }
            if (StringUtil.isNotBlank(xsmxDto.getXsid())) {
                XsglDto xsglDto=new XsglDto();
                xsglDto.setXsid(xsmxDto.getXsid());
                xsglDto.setLrry(user.getYhid());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String now = sdf.format(date);
                String[] split = now.split("-");
                String year = split[0];
                String month = split[1];
                String day = split[2];
                String s = "XS-" + year + month + day;
                xsglDto.setOaxsdh(getOAxsddh(s));
                xsmxDto.setXsmxid(StringUtil.generateUUID());
                xsmxDto.setLrry(user.getYhid());
                xsglDto.setZt("00");
                BigDecimal hsdj=new BigDecimal(xsmxDto.getHsdj()) ;
                BigDecimal sl=new BigDecimal(xsmxDto.getSl());
                BigDecimal jsze=(hsdj.multiply(sl));
                BigDecimal xszje=(hsdj.multiply(sl));
                jsze=jsze.setScale(2, RoundingMode.HALF_UP);
                xszje=xszje.setScale(5,RoundingMode.HALF_UP);
                xsmxDto.setJsze(jsze.toString());
                xsglDto.setXszje(xszje.toString());
                xsglDto.setYwy(user.getYhid());
                xsglDto.setXsbm(user.getJgid());
                xsglDto.setDdrq(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                if (getDtoById(xsglDto.getXsid())==null){
                    if (!insert(xsglDto))
                        throw new BusinessException("msg", "导入失败");
                }
                else {
                    XsglDto xsglDto1=getDtoById(xsglDto.getXsid());
                    BigDecimal xszje1=new BigDecimal(xsglDto.getXszje());
                    BigDecimal xszje2=new BigDecimal(xsglDto1.getXszje());
                    XsglDto xsglDto2=new XsglDto();
                    BigDecimal xszje3=xszje1.add(xszje2);
                    xszje3=xszje3.setScale(5,RoundingMode.HALF_UP);
                    xsglDto2.setXszje(xszje3.toString());
                    xsglDto2.setXsid(xsglDto.getXsid());
                    xsglDto2.setXgry(user.getYhid());
                    if (!update(xsglDto2))
                        throw new BusinessException("msg", "导入失败");
                }
            }
            if (!xsmxService.insert(xsmxDto))
                throw new BusinessException("msg", "导入失败");
        return true;
    }

    @Override
    public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
        // TODO Auto-generated method stub
        if(tranTrack.equalsIgnoreCase("XSDD001")){
            WlglDto wlglDto=new WlglDto();
            wlglDto.setWlbm(value);
            WlglDto t_wlglDto=wlglService.getDto(wlglDto);
            if(t_wlglDto==null) {
                errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                        .append("列，找不到对应的物料，单元格值为：").append(value).append("；<br>");
            }else {
                if(!t_wlglDto.getZt().equals(StatusEnum.CHECK_PASS.getCode())) {
                    errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
                            .append("列，物料未通过审核，不允许销售，单元格值为：").append(value).append("；<br>");
                }
                return t_wlglDto.getWlbm();
            }
        }
        return null;
    }

    @Override
    public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
        return false;
    }

    @Override
    public boolean checkDefined(List<Map<String, String>> defined) {
        return true;
    }
	
	 @Override
    public List<XsglDto> getXsxxWithKh() {
        return dao.getXsxxWithKh();
    }

    @Override
    public List<XsglDto> getXsxxByKhid(XsglDto xsglDto) {
        return dao.getXsxxByKhid(xsglDto);
    }

    @Override
    public boolean salereceiptSavePage(XsglDto xsglDto) {
       return dao.update(xsglDto) > 0;
    }
    @Override
    public List<XsglDto> getDtoListByJyId(String jcjyid) {
        return dao.getDtoListByJyId(jcjyid);
    }
    @Override
    public boolean updateWlxx(XsglDto xsglDto) {
        return dao.updateWlxx(xsglDto);
    }

    /**
     * 钉钉审批回调
     * @return
     */
    public boolean callbackXsglAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException{
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
            XsglDto xsglDto =dao.getDtoByDdslid(processInstanceId);
            //若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
            if(xsglDto!=null) {
                //获取审批人信息
                User user=new User();
                user.setDdid(staffId);
                user.setWbcxid(wbcxidString);
                user = commonService.getByddwbcxid(user);
                if(user==null)
                    return false;
                //获取审批人角色信息
                List<XsglDto> dd_sprjs=dao.getSprjsBySprid(user.getYhid());
                // 获取当前审核过程
                ShgcDto t_shgcDto = shgcService.getDtoByYwid(xsglDto.getXsid());
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
                        shxxDto.setYwid(xsglDto.getXsid());
                        shxxDto.setYwglmc(xsglDto.getOaxsdh());
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
                                log.error("DingTalkMaterPurchaseAudit(钉钉物料领料审批转发提醒)------转发人员:"+user.getZsxm()+",人员ID:"+user.getYhid()+",单据号:"+xsglDto.getOaxsdh()+",单据ID:"+xsglDto.getXsid()+",转发时间:"+date);
                                return true;
                            }
                            //调用审核方法
                            Map<String, List<String>> map= ToolUtil.reformRequest(request);
                            log.error("map:"+map);
                            List<String> list=new ArrayList<>();
                            list.add(xsglDto.getXsid());
                            map.put("xsid", list);
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
                            shxxDto.setYwglmc(xsglDto.getOaxsdh());
                            XsglDto xsglDto_t=new XsglDto();
                            xsglDto_t.setXsid(xsglDto.getXsid());
                            dao.updateDdslidToNull(xsglDto_t);
                            log.error("撤销------");
                            shxxDto.setSftg("0");
                            //调用审核方法
                            Map<String, List<String>> map=ToolUtil.reformRequest(request);
                            List<String> list=new ArrayList<>();
                            list.add(xsglDto.getXsid());
                            map.put("xsid", list);
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
                        shxx.setYwid(xsglDto.getXsid());
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

    @Override
    @Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean stewardsetSaveSale(XsglDto xsglDto) {
        dao.updateFzrByYfzr(xsglDto);
        JcjyglDto jcjyglDto=new JcjyglDto();
        jcjyglDto.setYfzr(xsglDto.getYfzr());
        jcjyglDto.setFzr(xsglDto.getFzr());
        jcjyglService.updateFzrByYfzr(jcjyglDto);
        LlglDto llglDto=new LlglDto();
        llglDto.setYfzr(xsglDto.getYfzr());
        llglDto.setFzr(xsglDto.getFzr());
        llglService.updateFzrByYfzr(llglDto);
        return true;
    }

    @Override
    public List<XsglDto> getPagedSaleData(XsglDto xsglDto) {
        return dao.getPagedSaleData(xsglDto);
    }

    @Override
    public void updateFhzt(List<FhglDto> fhglDtos) {
        dao.updateFhzt(fhglDtos);
    }

    @Override
    public boolean updateYsk(XsglDto xsglDto) {
        return dao.updateYsk(xsglDto);
    }

    @Override
    public void updateYsks(List<XsglDto> xsglDtos) {
        dao.updateYsks(xsglDtos);
    }
}
