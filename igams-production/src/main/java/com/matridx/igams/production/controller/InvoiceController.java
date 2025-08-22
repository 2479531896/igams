package com.matridx.igams.production.controller;


import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.FpglDto;
import com.matridx.igams.production.dao.entities.FpmxDto;

import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.service.svcinterface.IFpglService;
import com.matridx.igams.production.service.svcinterface.IFpmxService;
import com.matridx.igams.production.service.svcinterface.IHtmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.igams.common.util.DingTalkUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/invoice")
public class InvoiceController extends BaseBasicController {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IFpglService fpglService;
    @Autowired
    IShgcService shgcService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    ICommonService commonService;
    @Autowired
    IHtmxService htmxService;
    @Autowired
    IFpmxService fpmxService;
    @Autowired
    IXxglService xxglService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
    private final Logger log = LoggerFactory.getLogger(InvoiceController.class);

    @Override
	public String getPrefix(){
		return urlPrefix;
	}
	
    @Autowired
	DingTalkUtil talkUtil;

    /**
     * 发票列表
     */
    @RequestMapping("/invoice/pageListInvoice")
    public ModelAndView getInvoiceDtoList(FpglDto fpglDto) {
        ModelAndView mav = new ModelAndView("invoice/invoice/invoice_list");
        fpglDto.setAuditType(AuditTypeEnum.AUDIT_INVOICE.getCode());
        mav.addObject("typelist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INVOICE_TYPE.getCode()));
        mav.addObject("classlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INVOICE_CLASS.getCode()));
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("fpglDto", fpglDto);
        return mav;
    }

    /**
     * 发票列表
     */
    @RequestMapping("/invoice/pageGetListInvoice")
    @ResponseBody
    public Map<String, Object> pageGetListInvoice(FpglDto fpglDto) {
        Map<String, Object> map = new HashMap<>();
        List<FpglDto> list = fpglService.getPagedDtoList(fpglDto);
        try{
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_INVOICE.getCode(), "zt", "fpid", new String[]{
                    StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
        } catch (BusinessException e){
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        map.put("total", fpglDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }
    /**
     * 钉钉发票列表
     */
    @RequestMapping("/invoice/minidataGetPageListInvoice")
    @ResponseBody
    public Map<String, Object> minidataGetPageListInvoice(FpglDto fpglDto) {
        Map<String, Object> map = new HashMap<>();
        List<FpglDto> list = fpglService.getPagedDtoList(fpglDto);
        try{
            shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_INVOICE.getCode(), "zt", "fpid", new String[]{
                    StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
        } catch (BusinessException e){
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        map.put("fpzllist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INVOICE_TYPE.getCode()));
        map.put("total", fpglDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }

    /**
     * 发票列表查看
     */
    @RequestMapping("/invoice/viewInvoice")
    public ModelAndView viewInvoice(FpglDto fpglDto) {
        ModelAndView mav = new ModelAndView("invoice/invoice/invoice_view");
        FpglDto dtoById = fpglService.getDtoById(fpglDto.getFpid());
        FpmxDto fpmxDto=new FpmxDto();
        fpmxDto.setFpid(fpglDto.getFpid());
        List<FpmxDto> list = fpmxService.getListForHw(fpmxDto);
        List<FpmxDto> mxlist=new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            FpmxDto fpmxDto_t=list.get(0);
            List<FpmxDto> u8ids=new ArrayList<>();
            for(int i=0;i<list.size();i++){
                if(fpmxDto_t.getFpmxid().equals(list.get(i).getFpmxid())){
                    FpmxDto fpmxDto1=new FpmxDto();
                    fpmxDto1.setRkid(list.get(i).getRkid());
                    fpmxDto1.setDhid(list.get(i).getDhid());
                    fpmxDto1.setU8rkdh(list.get(i).getU8rkdh());
                    fpmxDto1.setLbcskz1(list.get(i).getLbcskz1());
                    u8ids.add(fpmxDto1);
                }else{
                    fpmxDto_t.setFpmxDtos(u8ids);
                    mxlist.add(fpmxDto_t);
                    fpmxDto_t=list.get(i);
                    u8ids=new ArrayList<>();
                    FpmxDto fpmxDto1=new FpmxDto();
                    fpmxDto1.setRkid(list.get(i).getRkid());
                    fpmxDto1.setDhid(list.get(i).getDhid());
                    fpmxDto1.setU8rkdh(list.get(i).getU8rkdh());
                    fpmxDto1.setLbcskz1(list.get(i).getLbcskz1());
                    u8ids.add(fpmxDto1);
                }
                if(i==list.size()-1){
                    fpmxDto_t.setFpmxDtos(u8ids);
                    mxlist.add(fpmxDto_t);
                }
            }
        }
        List<FpmxDto> rklist = fpmxService.getListForRk(fpmxDto);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("fpglDto", dtoById);
        mav.addObject("mxlist", mxlist);
        mav.addObject("rklist", rklist);
        return mav;
    }
    /**
     * 发票列表查看钉钉
     */
    @RequestMapping("/invoice/minidataGetViewInvoice")
    @ResponseBody
    public Map<String, Object> getViewInvoice(FpglDto fpglDto) {
        Map<String, Object> map = new HashMap<>();
        FpglDto dtoById = fpglService.getDtoById(fpglDto.getFpid());
        FpmxDto fpmxDto=new FpmxDto();
        fpmxDto.setFpid(fpglDto.getFpid());
        List<FpmxDto> list = fpmxService.getListForHw(fpmxDto);
        List<FpmxDto> mxlist=new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            FpmxDto fpmxDto_t=list.get(0);
            List<FpmxDto> u8ids=new ArrayList<>();
            for(int i=0;i<list.size();i++){
                if(fpmxDto_t.getFpmxid().equals(list.get(i).getFpmxid())){
                    FpmxDto fpmxDto1=new FpmxDto();
                    fpmxDto1.setRkid(list.get(i).getRkid());
                    fpmxDto1.setDhid(list.get(i).getDhid());
                    fpmxDto1.setU8rkdh(list.get(i).getU8rkdh());
                    fpmxDto1.setLbcskz1(list.get(i).getLbcskz1());
                    u8ids.add(fpmxDto1);
                }else{
                    fpmxDto_t.setFpmxDtos(u8ids);
                    mxlist.add(fpmxDto_t);
                    fpmxDto_t=list.get(i);
                    u8ids=new ArrayList<>();
                    FpmxDto fpmxDto1=new FpmxDto();
                    fpmxDto1.setRkid(list.get(i).getRkid());
                    fpmxDto1.setDhid(list.get(i).getDhid());
                    fpmxDto1.setU8rkdh(list.get(i).getU8rkdh());
                    fpmxDto1.setLbcskz1(list.get(i).getLbcskz1());
                    u8ids.add(fpmxDto1);
                }
                if(i==list.size()-1){
                    fpmxDto_t.setFpmxDtos(u8ids);
                    mxlist.add(fpmxDto_t);
                }
            }
        }
        List<FpmxDto> rklist = fpmxService.getListForRk(fpmxDto);
        map.put("fpglDto", dtoById);
        map.put("mxlist", mxlist);
        map.put("rklist", rklist);
        return map;
    }
    /**
     * 合同列表发票维护页面
     */
    @RequestMapping(value = "/invoice/receiptInvoice")
    public ModelAndView receiptInvoice(FpglDto fpglDto, HttpServletRequest request) {
        fpglDto.setLjbj("receiptSaveInvoice");
        return this.addInvoice(fpglDto,request);
    }
    /**
     * 发票维护页面
     */
    @RequestMapping(value = "/invoice/addInvoice")
    public ModelAndView addInvoice(FpglDto fpglDto, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("invoice/invoice/invoice_edit");
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
                new BasicDataTypeEnum[] { BasicDataTypeEnum.INVOICE_TYPE,BasicDataTypeEnum.CURRENCY,BasicDataTypeEnum.INVOICE_CLASS});
        mav.addObject("invoicelist", jclist.get(BasicDataTypeEnum.INVOICE_TYPE.getCode()));// 发票类型
        mav.addObject("currencylist", jclist.get(BasicDataTypeEnum.CURRENCY.getCode()));// 币种
        mav.addObject("classlist", jclist.get(BasicDataTypeEnum.INVOICE_CLASS.getCode()));// 发票种类

        // 设置默认创建日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        fpglDto.setKprq(sdf.format(date));
        if ("receiptSaveInvoice".equals(fpglDto.getLjbj())){
            fpglDto.setFormAction("receiptSaveInvoice");
        }else {
            fpglDto.setFormAction("addSaveInvoice");
        }
        User user = getLoginInfo(request);
        fpglDto.setYwy(user.getYhid());// 默认
        //获取默认部门
        user = commonService.getUserInfoById(user);
        if (user != null) {
            fpglDto.setYwymc(user.getZsxm());
            fpglDto.setBmmc(user.getJgmc());
            fpglDto.setBm(user.getJgid());
        }
        mav.addObject("fpglDto", fpglDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 获取明细信息
     */
    @ResponseBody
    @RequestMapping(value = "/invoice/pagedataGetInvoiceDetailList")
    public Map<String, Object> pagedataGetInvoiceDetailList(HtmxDto htmxDto) {
        Map<String, Object> map = new HashMap<>();
        List<String> ids = htmxDto.getIds();
        List<HtmxDto> htmxDtos=new ArrayList<>();
        if(!CollectionUtils.isEmpty(ids)) {
            htmxDto.setHtids(htmxDto.getIds());
            htmxDto.setIds(new ArrayList<>());
            List<HtmxDto> list = htmxService.getListForInvoice(htmxDto);
            List<HtmxDto> mxlist=new ArrayList<>();
            if(!CollectionUtils.isEmpty(list)){
                HtmxDto htmxDto_t=list.get(0);
                List<FpmxDto> u8ids=new ArrayList<>();
                for(int i=0;i<list.size();i++){
                    if(htmxDto_t.getHtmxid().equals(list.get(i).getHtmxid())){
                        FpmxDto fpmxDto1=new FpmxDto();
                        fpmxDto1.setRkid(list.get(i).getRkid());
                        fpmxDto1.setDhid(list.get(i).getDhid());
                        fpmxDto1.setU8rkdh(list.get(i).getU8rkdh());
                        fpmxDto1.setLbcskz1(list.get(i).getLbcskz1());
                        fpmxDto1.setSbysdh(list.get(i).getSbysdh());
                        fpmxDto1.setSbysid(list.get(i).getSbysid());
                        u8ids.add(fpmxDto1);
                    }else{
                        htmxDto_t.setFpmxDtos(u8ids);
                        mxlist.add(htmxDto_t);
                        htmxDto_t=list.get(i);
                        u8ids=new ArrayList<>();
                        FpmxDto fpmxDto1=new FpmxDto();
                        fpmxDto1.setRkid(list.get(i).getRkid());
                        fpmxDto1.setDhid(list.get(i).getDhid());
                        fpmxDto1.setU8rkdh(list.get(i).getU8rkdh());
                        fpmxDto1.setLbcskz1(list.get(i).getLbcskz1());
                        fpmxDto1.setSbysdh(list.get(i).getSbysdh());
                        fpmxDto1.setSbysid(list.get(i).getSbysid());
                        u8ids.add(fpmxDto1);
                    }
                    if(i==list.size()-1){
                        htmxDto_t.setFpmxDtos(u8ids);
                        mxlist.add(htmxDto_t);
                    }
                }
            }
            if(!CollectionUtils.isEmpty(mxlist)){
                for (HtmxDto value : mxlist) {
                    List<FpmxDto> fpmxDtos = value.getFpmxDtos();
                    if (!CollectionUtils.isEmpty(fpmxDtos)) {
                        boolean flag = false;
                        for (FpmxDto dto : fpmxDtos) {
                            if (StringUtil.isNotBlank(dto.getU8rkdh())) {
                                flag = true;
                            }
                        }
                        if (!flag) {
                            value.setWwhsl(value.getSl());
                        }
                    } else {
                        value.setWwhsl(value.getSl());
                    }
                    BigDecimal sl = new BigDecimal(value.getSl());
                    BigDecimal hsdj = new BigDecimal(value.getHsdj());
                    value.setSl(String.valueOf(sl));
                    value.setMxsl(String.valueOf(sl));
                    value.setHjje(String.valueOf(sl.multiply(hsdj)));
                    htmxDtos.add(value);
                }
            }
            map.put("rows",htmxDtos);
        }else{
            if(StringUtil.isNotBlank(htmxDto.getFpid())){
                FpmxDto fpmxDto=new FpmxDto();
                fpmxDto.setFpid(htmxDto.getFpid());
                List<FpmxDto> list = fpmxService.getInvoiceList(fpmxDto);
                List<FpmxDto> mxlist=new ArrayList<>();
                if(!CollectionUtils.isEmpty(list)){
                    FpmxDto fpmxDto_t=list.get(0);
                    List<FpmxDto> u8ids=new ArrayList<>();
                    for(int i=0;i<list.size();i++){
                        if(fpmxDto_t.getHtmxid().equals(list.get(i).getHtmxid())){
                            FpmxDto fpmxDto1=new FpmxDto();
                            fpmxDto1.setRkid(list.get(i).getRkid());
                            fpmxDto1.setDhid(list.get(i).getDhid());
                            fpmxDto1.setU8rkdh(list.get(i).getU8rkdh());
                            fpmxDto1.setLbcskz1(list.get(i).getLbcskz1());
                            fpmxDto1.setSbysdh(list.get(i).getSbysdh());
                            fpmxDto1.setSbysid(list.get(i).getSbysid());
                            u8ids.add(fpmxDto1);
                        }else{
                            fpmxDto_t.setFpmxDtos(u8ids);
                            mxlist.add(fpmxDto_t);
                            fpmxDto_t=list.get(i);
                            u8ids=new ArrayList<>();
                            FpmxDto fpmxDto1=new FpmxDto();
                            fpmxDto1.setRkid(list.get(i).getRkid());
                            fpmxDto1.setDhid(list.get(i).getDhid());
                            fpmxDto1.setU8rkdh(list.get(i).getU8rkdh());
                            fpmxDto1.setLbcskz1(list.get(i).getLbcskz1());
                            fpmxDto1.setSbysdh(list.get(i).getSbysdh());
                            fpmxDto1.setSbysid(list.get(i).getSbysid());
                            u8ids.add(fpmxDto1);
                        }
                        if(i==list.size()-1){
                            fpmxDto_t.setFpmxDtos(u8ids);
                            mxlist.add(fpmxDto_t);
                        }
                    }
                }
                if(!CollectionUtils.isEmpty(mxlist)){
                    for (FpmxDto value : mxlist) {
                        List<FpmxDto> fpmxDtos = value.getFpmxDtos();
                        if (!CollectionUtils.isEmpty(fpmxDtos)) {
                            boolean flag = false;
                            for (FpmxDto dto : fpmxDtos) {
                                if (StringUtil.isNotBlank(dto.getU8rkdh())) {
                                    flag = true;
                                }
                            }
                            if (!flag) {
                                value.setWwhsl(value.getMxsl());
                            }
                        } else {
                            value.setWwhsl(value.getMxsl());
                        }
                    }
                }
                map.put("rows", mxlist);
            }else{
                map.put("rows", null);
            }
        }
        return map;
    }
    /**
     * 合同列表发票维护保存
     */
    @RequestMapping("/invoice/receiptSaveInvoice")
    @ResponseBody
    public Map<String, Object> receiptSaveInvoice(FpglDto fpglDto,HttpServletRequest request){
        return this.addSaveInvoice(fpglDto,request);
    }
    /**
     * 发票维护保存
     */
    @RequestMapping("/invoice/addSaveInvoice")
    @ResponseBody
    public Map<String, Object> addSaveInvoice(FpglDto fpglDto,HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        List<FpglDto> verify = fpglService.verifyDmAndFph(fpglDto);
        if(!CollectionUtils.isEmpty(verify)) {
            map.put("status", "fail");
            map.put("message", "发票号重复！");
            return map;
        }
        fpglDto.setFpid(StringUtil.generateUUID());
        User user = getLoginInfo(request);
        fpglDto.setLrry(user.getYhid());
        fpglDto.setZt(StatusEnum.CHECK_NO.getCode());
        boolean isSuccess = fpglService.insertDto(fpglDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        map.put("ywid",fpglDto.getFpid());
        map.put("auditType", AuditTypeEnum.AUDIT_INVOICE.getCode());
        map.put("urlPrefix",urlPrefix);
        return map;
    }

    /**
     * 	发票审核
     */
    @RequestMapping("/invoice/pageListInvoiceAudit")
    public ModelAndView pageListInvoiceAudit() {
        ModelAndView mav = new  ModelAndView("invoice/invoice/invoice_auditList");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 	发票审核列表
     */
    @RequestMapping("/invoice/pageGetListInvoiceAudit")
    @ResponseBody
    public Map<String, Object> pageGetListInvoiceAudit(FpglDto fpglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        // 附加委托参数
        DataPermission.addWtParam(fpglDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(fpglDto.getDqshzt())) {
            DataPermission.add(fpglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "fpgl", "fpid",
                    AuditTypeEnum.AUDIT_INVOICE);
        } else {
            DataPermission.add(fpglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "fpgl", "fpid",
                    AuditTypeEnum.AUDIT_INVOICE);
        }
        DataPermission.addCurrentUser(fpglDto, getLoginInfo(request));
        List<FpglDto> listMap = fpglService.getPagedAuditInvoice(fpglDto);
        map.put("total", fpglDto.getTotalNumber());
        map.put("rows", listMap);
        return map;
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/invoice/modInvoice")
    public ModelAndView modInvoice(FpglDto fpglDto) {
        ModelAndView mav = new ModelAndView("invoice/invoice/invoice_edit");
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
                new BasicDataTypeEnum[] { BasicDataTypeEnum.INVOICE_TYPE,BasicDataTypeEnum.CURRENCY,BasicDataTypeEnum.INVOICE_CLASS});
        mav.addObject("invoicelist", jclist.get(BasicDataTypeEnum.INVOICE_TYPE.getCode()));// 发票类型
        mav.addObject("currencylist", jclist.get(BasicDataTypeEnum.CURRENCY.getCode()));// 币种
        mav.addObject("classlist", jclist.get(BasicDataTypeEnum.INVOICE_CLASS.getCode()));// 发票种类
        FpglDto dto = fpglService.getDto(fpglDto);
        if ("submitSaveInvoice".equals(fpglDto.getLjbj())){
            dto.setFormAction("submitSaveInvoice");
        }else if ("auditSaveInvoice".equals(fpglDto.getLjbj())){
            dto.setFormAction("auditSaveInvoice");
        }else {
            dto.setFormAction("modSaveInvoice");
        }
        dto.setFormAction("modSaveInvoice");
        dto.setAuditType(AuditTypeEnum.AUDIT_INVOICE.getCode());
        mav.addObject("fpglDto", dto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 提交
     */
    @RequestMapping(value = "/invoice/submitInvoice")
    public ModelAndView submitInvoice(FpglDto fpglDto) {
        fpglDto.setLjbj("submitSaveInvoice");
        return this.modInvoice(fpglDto);
    }
    /**
     * 提交保存
     */
    @RequestMapping("/invoice/submitSaveInvoice")
    @ResponseBody
    public Map<String, Object> submitSaveInvoice(FpglDto fpglDto,HttpServletRequest request){
        return this.modSaveInvoice(fpglDto,request);
    }
    /**
     * 审核
     */
    @RequestMapping(value = "/invoice/auditInvoice")
    public ModelAndView auditInvoice(FpglDto fpglDto) {
        fpglDto.setLjbj("auditSaveInvoice");
        return this.modInvoice(fpglDto);
    }
    /**
     * 审核保存
     */
    @RequestMapping("/invoice/auditSaveInvoice")
    @ResponseBody
    public Map<String, Object> auditSaveInvoice(FpglDto fpglDto,HttpServletRequest request){
        return this.modSaveInvoice(fpglDto,request);
    }
    /**
     * 修改保存
     */
    @RequestMapping("/invoice/modSaveInvoice")
    @ResponseBody
    public Map<String, Object> modSaveInvoice(FpglDto fpglDto,HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        List<FpglDto> verify = fpglService.verifyDmAndFph(fpglDto);
        if(!CollectionUtils.isEmpty(verify)) {
            map.put("status", "fail");
            map.put("message", "发票代码或发票号重复！");
            return map;
        }
        User user = getLoginInfo(request);
        fpglDto.setXgry(user.getYhid());
        boolean isSuccess = fpglService.updateDto(fpglDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        map.put("ywid",fpglDto.getFpid());
        map.put("auditType", AuditTypeEnum.AUDIT_INVOICE.getCode());
        map.put("urlPrefix",urlPrefix);
        return map;
    }

    /**
     * 删除
     */
    @RequestMapping("/invoice/delInvoice")
    @ResponseBody
    public Map<String, Object> delInvoice(FpglDto fpglDto,HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        boolean isSuccess = fpglService.deleteDto(fpglDto, user);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 审核页面
     */
    @RequestMapping(value = "/invoice/pagedataAuditInvoice")
    public ModelAndView pagedataAuditInvoice(FpglDto fpglDto) {
        ModelAndView mav = new ModelAndView("invoice/invoice/invoice_audit");
        FpglDto dto = fpglService.getDto(fpglDto);
        dto.setAuditType(AuditTypeEnum.AUDIT_INVOICE.getCode());
        mav.addObject("fpglDto", dto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }


    /**
     * 发票列表查看
     */
    @RequestMapping("/invoice/pagedataGetListByHtmxid")
    public ModelAndView pagedataGetListByHtmxid(FpmxDto fpmxDto) {
        ModelAndView mav = new ModelAndView("invoice/invoice/invoice_viewList");
        List<FpmxDto> listByHtmxid = fpmxService.getListByHtmxid(fpmxDto);
        mav.addObject("list",listByHtmxid);
        return mav;
    }

}
