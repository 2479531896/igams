package com.matridx.igams.production.controller;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.*;
import com.matridx.igams.production.service.svcinterface.IFpmxService;
import com.matridx.igams.production.service.svcinterface.IHtfkmxService;
import com.matridx.igams.production.service.svcinterface.IHtfkqkService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/contract")
public class ContractPayMentController extends BaseController {

    @Autowired
    IHtfkqkService htfkqkService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IHtfkmxService htfkmxService;
    @Autowired
    IFpmxService fpmxService;

    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }
    @Autowired
    IXxglService xxglService;

    /**
     * 跳转合同付款审核列表页面
     */
    @RequestMapping("/payment/pageListAuditPayMent")
    public ModelAndView pageListAuditPayMent(){
        ModelAndView mav = new ModelAndView("contract/payment/contractPayment_auditList");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 获取合同付款审核列表数据
     */
    @RequestMapping("/payment/pageGetListAuditPayMent")
    @ResponseBody
    public Map<String,Object> pageGetListAuditPayMent(HtfkqkDto htfkqkDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        // 附加委托参数
        DataPermission.addWtParam(htfkqkDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(htfkqkDto.getDqshzt())) {
            DataPermission.add(htfkqkDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "htfkqk", "htfkid",
                    AuditTypeEnum.AUDIT_CONTRACT_PAYMENT);
        } else {
            DataPermission.add(htfkqkDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "htfkqk", "htfkid",
                    AuditTypeEnum.AUDIT_CONTRACT_PAYMENT);
        }
        DataPermission.addCurrentUser(htfkqkDto, getLoginInfo(request));
        List<HtfkqkDto> listMap = htfkqkService.getPagedAuditHtfkqk(htfkqkDto);

        map.put("total", htfkqkDto.getTotalNumber());
        map.put("rows", listMap);
        return map;
    }

    /**
     * 跳转合同付款列表
     * /Contract/payInfo/pageListPayInfo
     */
    @RequestMapping("/payInfo/pageListPayInfo")
    public ModelAndView pageListPayInfo(){
        ModelAndView mav = new ModelAndView("contract/payment/contractPayList");
        mav.addObject("urlPrefix", urlPrefix);
        HtfkqkDto htfkqkDto=new HtfkqkDto();
        htfkqkDto.setAuditType(AuditTypeEnum.AUDIT_CONTRACT_PAYMENT.getCode());
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
                new BasicDataTypeEnum[] { BasicDataTypeEnum.PAY_BELONG});
        mav.addObject("paybelonglist", jclist.get(BasicDataTypeEnum.PAY_BELONG.getCode()));// 费用归属
        mav.addObject("htfkqkDto",htfkqkDto);
        return mav;
    }

    /**
     * 获取合同付款列表数据
     */
    @RequestMapping("/payInfo/pagedataGetPayInfoList")
    @ResponseBody
    public Map<String,Object> pagedataGetPayInfoList(HtfkqkDto htfkqkDto){
        Map<String,Object> map = new HashMap<>();
        List<HtfkqkDto> htfkqkDtoList = htfkqkService.getPagedDtoList(htfkqkDto);
        map.put("total", htfkqkDto.getTotalNumber());
        map.put("rows", htfkqkDtoList);
        return map;
    }

    /**
     * 获取合同付款列表数据
     */
    @RequestMapping("/payInfo/minidataGetPayInfoList")
    @ResponseBody
    public Map<String,Object> minidataGetPayInfoList(HtfkqkDto htfkqkDto){
        return pagedataGetPayInfoList(htfkqkDto);
    }
    /**
     * 合同付款查看按钮
     * /Contract/payInfo/pageListPayInfo
     */
    @RequestMapping("/payInfo/viewPayInfo")
    public ModelAndView payInfoView(String htfkid){
        ModelAndView mav = new ModelAndView("contract/payment/contract_pay_view");
        HtfkqkDto htfkqkDto = htfkqkService.getDtoById(htfkid);
        HtfkmxDto htfkmxDto=new HtfkmxDto();
        htfkmxDto.setHtfkid(htfkid);
        List<HtfkmxDto> dtoList = htfkmxService.getDtoList(htfkmxDto);
//        HtglDto htglDto = htglService.getDtoById(htfkqkDto.getHtid());
//        htfkqkDto.setHtnbbh(htglDto.getHtnbbh());
//        htfkqkDto.setCjrq(htglDto.getCjrq());
        FpmxDto fpmxDto=new FpmxDto();
        fpmxDto.setHtfkid(htfkid);
        List<FpmxDto> list = fpmxService.getListByHtfkid(fpmxDto);
        mav.addObject("mxlist", list);
        mav.addObject("htfkqkDto", htfkqkDto);
        mav.addObject("htfkmxList", dtoList);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 获取合同付款列表查看数据
     */
    @RequestMapping("/payInfo/minidataGetViewPayInfoList")
    @ResponseBody
    public Map<String,Object> minidataGetViewPayInfoList(String htfkid){
        Map<String,Object> map = new HashMap<>();
        HtfkqkDto htfkqkDto = htfkqkService.getDtoById(htfkid);
        HtfkmxDto htfkmxDto=new HtfkmxDto();
        htfkmxDto.setHtfkid(htfkid);
        List<HtfkmxDto> dtoList = htfkmxService.getDtoList(htfkmxDto);
        map.put("htfkqkDto", htfkqkDto);
        map.put("htfkqkmxList", dtoList);
        return map;
    }
    /**
     * 删除付款信息
     */
    @ResponseBody
    @RequestMapping(value = "/payInfo/delPayInfoView")
    public Map<String,Object> delContractView(HtfkqkDto htfkqkDto,HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        try {
            //获取用户信息
            User user = getLoginInfo(request);
            htfkqkDto.setScry(user.getYhid());
            boolean delete = htfkqkService.delHtfkqk(htfkqkDto);
            map.put("status", delete?"success":"fail");
            map.put("message", delete?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        }catch (BusinessException e) {
    			map.put("status", "fail");
    			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
    		}
        return map;
    }

    /**
     * 修改合同付款页面
     * /Contract/payInfo/pageListPayInfo
     */
    @RequestMapping("/payInfo/modPayInfo")
    public ModelAndView modPayInfo(HtfkqkDto htfkqkDto,HttpServletRequest request){
        ModelAndView mav = new ModelAndView("contract/payment/contract_pay_update");
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
                new BasicDataTypeEnum[] { BasicDataTypeEnum.PAYAPPLY_PAYWAY, BasicDataTypeEnum.PAYAPPLY_PAYER,BasicDataTypeEnum.PAY_BELONG});
        mav.addObject("paywaylist", jclist.get(BasicDataTypeEnum.PAYAPPLY_PAYWAY.getCode()));// 付款申请付款方式
        mav.addObject("payerlist", jclist.get(BasicDataTypeEnum.PAYAPPLY_PAYER.getCode()));// 付款申请付款方
        mav.addObject("paybelonglist", jclist.get(BasicDataTypeEnum.PAY_BELONG.getCode()));// 费用归属
        HtfkqkDto t_htfkqkDto = htfkqkService.getDtoById(htfkqkDto.getHtfkid());
//        t_htfkqkDto.setOpen_flg(htfkqkDto.getOpen_flg());
        User user = getLoginInfo(request);
        t_htfkqkDto.setFkbfb(t_htfkqkDto.getFkbfb()+"%");
        t_htfkqkDto.setFkjexgq(t_htfkqkDto.getFkzje());
        t_htfkqkDto.setAuditType(AuditTypeEnum.AUDIT_CONTRACT_PAYMENT.getCode());
        mav.addObject("czr", user.getZsxm());
        if ("submitSavePayInfo".equals(htfkqkDto.getLjbj())){
            mav.addObject("formAction", "/contract/payInfo/submitSavePayInfo");
        }else if ("auditSavePayInfo".equals(htfkqkDto.getLjbj())){
            mav.addObject("formAction", "/contract/payInfo/auditSavePayInfo");
        }else {
            mav.addObject("formAction", "/contract/payInfo/modSavePayInfo");
        }
        mav.addObject("htfkqkDto", t_htfkqkDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 提交合同付款页面
     * /Contract/payInfo/pageListPayInfo
     */
    @RequestMapping("/payInfo/submitPayInfo")
    public ModelAndView submitPayInfo(HtfkqkDto htfkqkDto,HttpServletRequest request){
        htfkqkDto.setLjbj("submitSavePayInfo");
        return this.modPayInfo(htfkqkDto,request);
    }
    /**
     * 合同付款提交保存
     */
    @ResponseBody
    @RequestMapping(value = "/payInfo/submitSavePayInfo")
    public Map<String, Object> submitSavePayInfo(HtfkqkDto htfkqkDto, HttpServletRequest request) {
        return this.modSavePayContract(htfkqkDto,request);
    }
    /**
     * 审核合同付款页面
     * /Contract/payInfo/pageListPayInfo
     */
    @RequestMapping("/payInfo/auditPayInfo")
    public ModelAndView auditPayInfo(HtfkqkDto htfkqkDto,HttpServletRequest request){
        htfkqkDto.setLjbj("auditSavePayInfo");
        return this.modPayInfo(htfkqkDto,request);
    }
    /**
     * 合同付款审核保存
     */
    @ResponseBody
    @RequestMapping(value = "/payInfo/auditSavePayInfo")
    public Map<String, Object> auditSavePayInfo(HtfkqkDto htfkqkDto, HttpServletRequest request) {
        return this.modSavePayContract(htfkqkDto,request);
    }
    /**
     * 合同付款修改保存
     */
    @ResponseBody
    @RequestMapping(value = "/payInfo/modSavePayInfo")
    public Map<String, Object> modSavePayContract(HtfkqkDto htfkqkDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        htfkqkDto.setXgry(user.getYhid());
        Map<String,Object> map=htfkqkService.checkMoney(htfkqkDto);
        if(map.get("status")=="fail")
            return map;
        boolean isSuccess = htfkqkService.updateFkqk(htfkqkDto);
        if(isSuccess) {
            map.put("status","success");
            map.put("message",xxglService.getModelById("ICOM00001").getXxnr());
        }else {
            map.put("status","fail");
            map.put("message",xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * 获取合同付款明细信息
     */
    @ResponseBody
    @RequestMapping(value = "/payment/pagedataGetContractPayDetails")
    public Map<String, Object> getContractPayDetails(HtfkmxDto htfkmxDto) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(htfkmxDto.getHtfkid())) {
            List<HtfkmxDto> dtoList = htfkmxService.getDtoList(htfkmxDto);
            map.put("rows", dtoList);
        } else {
            map.put("rows", null);
        }
        return map;
    }

    /**
     * 获取合同付款明细信息
     */
    @ResponseBody
    @RequestMapping(value = "/payInfo/pagedataGetContractPayList")
    public Map<String, Object> pagedataGetContractPayList(HtfkmxDto hfktmxDto) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(hfktmxDto.getHtid())) {
            List<HtfkmxDto> htfkmxDtos = htfkmxService.getListByHtid(hfktmxDto.getHtid());
            map.put("rows", htfkmxDtos);
        } else {
            map.put("rows", null);
        }
        return map;
    }


    /**
     * 跳转合同付款明细列表
     * /Contract/payInfo/pageListPayInfoDetail
     */
    @RequestMapping("/payInfo/pageListPayInfoDetail")
    public ModelAndView pageListPayInfoDetail(){
        ModelAndView mav = new ModelAndView("contract/payment/contractPayDetailList");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 获取合同付款明细列表数据
     */
    @RequestMapping("/payInfo/pageGetListPayInfoDetail")
    @ResponseBody
    public Map<String,Object> pageGetListPayInfoDetail(HtfkmxDto htfkmxDto){
        Map<String,Object> map = new HashMap<>();
        List<HtfkmxDto> htfkmxDtoList = htfkmxService.getPagedDtoList(htfkmxDto);
        map.put("total", htfkmxDto.getTotalNumber());
        map.put("rows", htfkmxDtoList);
        return map;
    }

    /**
     * 合同付款查看按钮
     */
    @RequestMapping("/payInfo/viewPayInfoDetail")
    public ModelAndView viewPayInfoDetail(String htfkmxid){
        ModelAndView mav = new ModelAndView("contract/payment/contract_payDetail_view");
        HtfkmxDto htfkmxDto = htfkmxService.getDtoById(htfkmxid);
        mav.addObject("htfkmxDto", htfkmxDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
}
