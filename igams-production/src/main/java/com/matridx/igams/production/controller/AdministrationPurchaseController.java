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
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.storehouse.dao.entities.XzqgfkglDto;
import com.matridx.igams.storehouse.dao.entities.XzqgfkmxDto;
import com.matridx.igams.storehouse.dao.entities.XzqgqrcglDto;
import com.matridx.igams.storehouse.dao.entities.XzqgqrglDto;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgfkglService;
import com.matridx.igams.storehouse.dao.entities.XzqgqrmxDto;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgfkmxService;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgqrcglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgqrglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgqrmxService;

import com.matridx.springboot.util.base.StringUtil;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/administration")
public class AdministrationPurchaseController extends BaseController {

    @Autowired
    private IXxglService xxglService;

    @Autowired
    private IXzqgqrcglService xzqgqrcglService;

    @Autowired
    private IQgmxService qgmxService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IJcsjService jcsjService;

    @Autowired
    private IXzqgqrglService xzqgqrglService;
	
	@Autowired
    private IXzqgqrmxService xzqgqrmxService;

    @Autowired
    private IXzqgfkglService xzqgfkglService;

    @Autowired
    private IXzqgfkmxService xzqgfkmxService;


    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;

    private final Logger log = LoggerFactory.getLogger(AdministrationPurchaseController.class);

    @Override
    public String getPrefix() {
        return urlPrefix;
    }
    
	@Autowired
	private ICommonService commonService;

    /**
     * 行政请购未确认列表
     */
    @RequestMapping("/purchase/pageListUnconfirmPurchaseAdministration")
    public ModelAndView pageListUnconfirmPurchaseAdministration(QgmxDto qgmxDto, HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("purchase/purchase/unconfirmPurchaseAdministration_List");
        User user = getLoginInfo(request);
        XzqgqrcglDto xzqgqrcglDto = new XzqgqrcglDto();
        xzqgqrcglDto.setRyid(user.getYhid());
        List<XzqgqrcglDto> xzqgqrcglDtos = xzqgqrcglService.getDtoListByRyid(xzqgqrcglDto);
        StringBuilder ids = new StringBuilder();
        if(!CollectionUtils.isEmpty(xzqgqrcglDtos)) {
        	for (XzqgqrcglDto xzqgqrcglDto_t : xzqgqrcglDtos) {
        		ids.append(",").append(xzqgqrcglDto_t.getQgmxid());
			}
        }
        mav.addObject("qrcsl", xzqgqrcglDtos.size());
        mav.addObject("idqgmxids", ids.toString());
        mav.addObject("qgmxDto", qgmxDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 行政请购未确认列表数据
     */
    @RequestMapping("/purchase/pageGetListUnconfirmPurchaseAdministration")
    @ResponseBody
    public Map<String, Object> getPageListUnconfirmPurchaseAdministration(QgmxDto qgmxDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        qgmxDto.setRyid(user.getYhid());
        List<QgmxDto> list = qgmxService.getPageListUnconfirmPurchaseAdministration(qgmxDto);
        map.put("total", qgmxDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }


    /**
     * 行政请购未确认查看
     */
    @RequestMapping("/purchase/viewUnconfirmPurchaseAdministration")
    public ModelAndView viewUnconfirmPurchaseAdministration(QgmxDto qgmxDto) {
        ModelAndView mav = new ModelAndView("purchase/purchase/unconfirmPurchaseAdministration_View");
        QgmxDto qgmxDto_t = qgmxService.getWqrQgmxByQgmxid(qgmxDto);
        mav.addObject("qgmxDto", qgmxDto_t);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 行政请购确认页面
     */
    @RequestMapping("/purchase/confirmCar")
    public ModelAndView confirmCar(XzqgqrcglDto xzqgqrcglDto,HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("purchase/purchase/confirmCar_edit");
        User user = getLoginInfo(request);
//        user = commonService.getUserInfoById(user);
        xzqgqrcglDto.setRyid(user.getYhid());
        xzqgqrcglDto.setSqr(user.getYhid());
        xzqgqrcglDto.setSqrmc(user.getZsxm());
        xzqgqrcglDto.setSqbm(user.getJgid());
        xzqgqrcglDto.setSqbmmc(user.getJgmc());
        xzqgqrcglDto.setJgdm(user.getJgdm());
        //获取确认车数据
        List<XzqgqrcglDto> xzqgqrcglDtos = xzqgqrcglService.getDtoListByRyid(xzqgqrcglDto);
        //获取对账方式基础数据
        List<JcsjDto> dzfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECONCILIATION_TYPE.getCode());
        List<JcsjDto> fkfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYWAY.getCode());
        List<JcsjDto> fkfs = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYER.getCode());
        List<JcsjDto> fygss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAY_BELONG.getCode());  //付款申请  费用归属
        List<JcsjDto> fylblist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.EXPENSE_CATEGORY.getCode());
        if(!CollectionUtils.isEmpty(dzfss)){
            for(JcsjDto jcsjDto : dzfss){
                if("1".equals(jcsjDto.getSfmr()))
                    xzqgqrcglDto.setDzfsdm(jcsjDto.getCsdm());
            }
        }
        String djh= xzqgqrglService.generateConfirmDjh(xzqgqrcglDto);
        xzqgqrcglDto.setDjh(djh);
        mav.addObject("xzqgqrcglDtos", xzqgqrcglDtos);
        mav.addObject("dzfss", dzfss);
        mav.addObject("fkfss",fkfss);
        mav.addObject("fkfs",fkfs);
        mav.addObject("fygss",fygss);
        mav.addObject("fylblist",fylblist);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("xzqgqrcglDto",xzqgqrcglDto);
        mav.addObject("auditType",AuditTypeEnum.AUDIT_ADMINISTRATIONCONFIRMPURCHASE.getCode());
        mav.addObject("auditTypePTP",AuditTypeEnum.AUDIT_ADMINPURCHASEPAY_PTP.getCode());
        return mav;
    }


    /**
     * 	获取确认车数据
     */
    @ResponseBody
    @RequestMapping(value = "/purchase/pagedataConfirmCarList")
    public Map<String, Object> getConfirmCarList(XzqgqrcglDto xzqgqrcglDto,HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        xzqgqrcglDto.setRyid(user.getYhid());
        List<XzqgqrcglDto> xzqgqrcglDtos = xzqgqrcglService.getDtoListByRyid(xzqgqrcglDto);
        if(!CollectionUtils.isEmpty(xzqgqrcglDtos)) {
            map.put("rows", xzqgqrcglDtos);
        }else{
            map.put("rows", null);
        }
        return map;
    }

    /**
     * 加入确认车
     */
    @ResponseBody
    @RequestMapping(value = "/purchase/pagedataSaveConfirmCar")
    public Map<String, Object> saveConfirmCar(XzqgqrcglDto xzqgqrcglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        xzqgqrcglDto.setRyid(user.getYhid());
        boolean isSuccess = xzqgqrcglService.saveConfirmCar(xzqgqrcglDto);
        xzqgqrcglDto.setQgmxid(null);
        List<XzqgqrcglDto> xzqgqrcglDtos = xzqgqrcglService.getDtoListByRyid(xzqgqrcglDto);
        StringBuilder ids = new StringBuilder();
        if(!CollectionUtils.isEmpty(xzqgqrcglDtos)) {
            for (XzqgqrcglDto xzqgqrcglDto_t : xzqgqrcglDtos) {
                ids.append(",").append(xzqgqrcglDto_t.getQgmxid());
            }
            ids = new StringBuilder(ids.substring(1));
        }
        map.put("idqgmxids", ids.toString());
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 移除确认车
     */
    @ResponseBody
    @RequestMapping(value = "/purchase/pagedataDelConfirmCar")
    public Map<String, Object> pagedataDelConfirmCar(XzqgqrcglDto xzqgqrcglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        xzqgqrcglDto.setRyid(user.getYhid());
        boolean isSuccess = xzqgqrcglService.delConfirmCar(xzqgqrcglDto);
        xzqgqrcglDto.setQgmxid(null);
        List<XzqgqrcglDto> xzqgqrcglDtos = xzqgqrcglService.getDtoListByRyid(xzqgqrcglDto);
        StringBuilder ids= new StringBuilder();
        if(!CollectionUtils.isEmpty(xzqgqrcglDtos)) {
            for (XzqgqrcglDto xzqgqrcglDto_t : xzqgqrcglDtos) {
                ids.append(",").append(xzqgqrcglDto_t.getQgmxid());
            }
        }
        map.put("idqgmxids", ids.toString());
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 确认页面点击选择按钮跳转行政请购未确认列表
     */
    @RequestMapping("/purchase/pagedataGetUnConfirmMsgPage")
    @ResponseBody
    public ModelAndView getUnConfirmMsgPage(HttpServletRequest request){
        ModelAndView mav=new ModelAndView("purchase/purchase/unconfirmPurchaseinfo_xz_list");
        mav.addObject("urlPrefix",urlPrefix);
        mav.addObject("flag",request.getParameter("flag"));
        return mav;
    }

    /**
     * 确认页面保存
     */
    @RequestMapping("/purchase/pagedataConfirmCarSave")
    @ResponseBody
    public Map<String,Object> confirmCarSave(XzqgqrglDto xzqgqrglDto, HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        try {
            User user = getLoginInfo(request);
            xzqgqrglDto.setLrry(user.getYhid());
            //月结处理
            boolean isSuccess;
            JcsjDto jcsjDto=jcsjService.getDtoById(xzqgqrglDto.getDzfs());
            if("MB".equals(jcsjDto.getCsdm())){
                isSuccess=xzqgqrglService.dealMonthBalanceData(xzqgqrglDto);
            }else{
                List<JcsjDto> djcdfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECEIPTS_TRANSMIT_TYPE.getCode());  //单据传达方式
                if(!CollectionUtils.isEmpty(djcdfss)){
                    for(JcsjDto jcsjDto_t : djcdfss){
                        if("1".equals(jcsjDto_t.getSfmr()))
                            xzqgqrglDto.setDjcdfs(jcsjDto_t.getCsid());
                    }
                }
                //公对公处理
                isSuccess=xzqgqrglService.dealPtpBalanceData(xzqgqrglDto);
            }
            //删除行政确认车数据
            XzqgqrcglDto xzqgqrcgl = new XzqgqrcglDto();
            xzqgqrcgl.setRyid(user.getYhid());
            xzqgqrcglService.delConfirmCar(xzqgqrcgl);
            map.put("ywid",xzqgqrglDto.getYwid());
            map.put("status", isSuccess?"success":"fail");
            map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        }
        catch (BusinessException e){
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * 重新生成单据号
     */
    @RequestMapping("/purchase/pagedataRegenerateDjh")
    @ResponseBody
    public Map<String,Object> pagedataRegenerateDjh(XzqgqrcglDto xzqgqrcglDto){
        Map<String,Object> map=new HashMap<>();
        String djh= xzqgqrglService.generateConfirmDjh(xzqgqrcglDto);
        map.put("djh",djh);
        return map;
    }

    /**
     * 行政请购确认列表
     */
    @RequestMapping("/purchase/pageListConfirmPurchaseAdministration")
    public ModelAndView confirmListPage(){
        ModelAndView mav=new ModelAndView("purchase/administration/confirmPurchase_list");
        List<JcsjDto> dzfslist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECONCILIATION_TYPE.getCode());  //对账方式
        mav.addObject("dzfslist",dzfslist);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * 获取行政请购确认数据
     */
    @RequestMapping("/purchase/pageGetListConfirmPurchaseAdministration")
    @ResponseBody
    public Map<String,Object> getPaagedListConfirm(XzqgqrglDto xzqgqrglDto){
        Map<String,Object> map=new HashMap<>();
        List<XzqgqrglDto> list=xzqgqrglService.getPagedDtoList(xzqgqrglDto);
        map.put("total", xzqgqrglDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }
    /**
     * 获取行政请购确认数据钉钉
     */
    @RequestMapping("/purchase/minidataGetPagedListConfirm")
    @ResponseBody
    public Map<String,Object> minidataGetPagedListConfirm(XzqgqrglDto xzqgqrglDto){
        return getPaagedListConfirm(xzqgqrglDto);
    }

    /**
     * 获取行政请购确认明细数据
     */
    @RequestMapping("/purchase/viewConfirmPurchaseAdministration")
    public ModelAndView viewPurchaseConfirm(XzqgqrglDto xzqgqrglDto){
        ModelAndView mav = new ModelAndView("purchase/administration/confirmPurchase_view");
        xzqgqrglDto=xzqgqrglService.getDto(xzqgqrglDto);
        mav.addObject("xzqgqrglDto",xzqgqrglDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 完成行政请购确认明细数据
     */
    @RequestMapping("/purchase/finishConfirmPurchaseAdministration")
    @ResponseBody
    public Map<String,Object> finishPurchaseConfirm(XzqgqrglDto xzqgqrglDto,HttpServletRequest request){
        User user=getLoginInfo(request);
        Map<String,Object> map = new HashMap<>();
        boolean isSuccess = xzqgqrglService.finishDto(xzqgqrglDto,user);
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 获取确认明细信息
     */
    @RequestMapping("/purchase/pagedataListQrmxList")
    @ResponseBody
    public Map<String,Object> getQrmxList(XzqgqrmxDto xzqgqrmxDto){
        Map<String,Object> map=new HashMap<>();
        List<XzqgqrmxDto> list=xzqgqrmxService.getPagedDtoList(xzqgqrmxDto);
        map.put("total", xzqgqrmxDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }
    /**
     * 获取确认信息和明细信息钉钉
     */
    @RequestMapping("/purchase/minidataGetQrmxListDingTalk")
    @ResponseBody
    public Map<String,Object> getQrmxListDingTalk(XzqgqrglDto xzqgqrglDto,XzqgqrmxDto xzqgqrmxDto){
        Map<String,Object> map=new HashMap<>();
        xzqgqrglDto=xzqgqrglService.getDto(xzqgqrglDto);
        List<XzqgqrmxDto> list=xzqgqrmxService.getDtoList(xzqgqrmxDto);
        map.put("xzqgqrmxDtos", list);
        map.put("xzqgqrglDto",xzqgqrglDto);
        return map;
    }

    /**
     * 获取确认明细信息不分页
     */
    @RequestMapping("/purchase/pagedataListQrmxListInfo")
    @ResponseBody
    public Map<String,Object> getQrmxListInfo(XzqgqrmxDto xzqgqrmxDto){
        Map<String,Object> map=new HashMap<>();
        List<XzqgqrmxDto> list=xzqgqrmxService.getDtoList(xzqgqrmxDto);
        map.put("total", xzqgqrmxDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }
	
	   /**
     * 行政请购付款列表
     */
    @RequestMapping("/administrationPay/pageListAdministrationPay")
    public ModelAndView pageListAdministrationPay(){
        ModelAndView mav=new ModelAndView("purchase/administration/adminPurchasePay_list");
        List<JcsjDto> fylblist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.EXPENSE_CATEGORY.getCode());
        List<JcsjDto> fkfslist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYWAY.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("auditType", AuditTypeEnum.AUDIT_ADMINPURCHASEPAY.getCode());
        mav.addObject("auditTypePTP", AuditTypeEnum.AUDIT_ADMINPURCHASEPAY_PTP.getCode());
        mav.addObject("fylblist", fylblist);
        mav.addObject("fkfslist", fkfslist);
        return mav;
    }

    /**
     * 获取行政请购付款列表数据
     */
    @RequestMapping("/administrationPay/pageGetListAdministrationPay")
    @ResponseBody
    public Map<String,Object> getPageListAdministrationPay(XzqgfkglDto xzqgfkglDto){
        Map<String,Object> map=new HashMap<>();
        List<XzqgfkglDto> list = xzqgfkglService.getPagedDtoList(xzqgfkglDto);
        map.put("total", xzqgfkglDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }
    /**
     * 获取行政请购付款列表数据钉钉
     */
    @RequestMapping("/administrationPay/minidataGetPageListAdministrationPay")
    @ResponseBody
    public Map<String,Object> minidataGetPageListAdministrationPay(XzqgfkglDto xzqgfkglDto){
        return getPageListAdministrationPay(xzqgfkglDto);
    }

    /**
     * 查看行政请购付款信息
     */
    @RequestMapping("/administrationPay/viewAdministrationPay")
    public ModelAndView viewAdministrationPay(XzqgfkglDto xzqgfkglDto){
        ModelAndView mav=new ModelAndView("purchase/administration/adminPurchasePay_view");
        xzqgfkglDto = xzqgfkglService.getDto(xzqgfkglDto);
        List<XzqgfkmxDto> fkmxlist = xzqgfkmxService.getListByFkid(xzqgfkglDto.getFkid());
        mav.addObject("xzqgfkglDto", xzqgfkglDto);
        mav.addObject("fkmxlist", fkmxlist);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 点击查看钉钉
     */
    @RequestMapping("/administrationPay/pagedataAdministrationPay")
    @ResponseBody
    public Map<String,Object> pagedataAdministrationPay(XzqgfkglDto xzqgfkglDto){
        Map<String,Object> map=new HashMap<>();
        List<XzqgfkmxDto> fkmxlist = xzqgfkmxService.getListByFkid(xzqgfkglDto.getFkid());
        map.put("rows",fkmxlist);
        return map;
    }
    /**
     * 点击查看钉钉
     */
    @RequestMapping("/administrationPay/minidataGetViewAdministrationPay")
    @ResponseBody
    public Map<String,Object> minidataGetViewAdministrationPay(XzqgfkglDto xzqgfkglDto){
        Map<String,Object> map=new HashMap<>();
        xzqgfkglDto = xzqgfkglService.getDto(xzqgfkglDto);
        List<XzqgfkmxDto> fkmxlist = xzqgfkmxService.getListByFkid(xzqgfkglDto.getFkid());
        map.put("fkmxlist",fkmxlist);
        map.put("xzqgfkglDto", xzqgfkglDto);
        return map;
    }
    /**
     * 点击查看钉钉
     */
    @RequestMapping("/administrationPay/getAdministrationPayMx")
    @ResponseBody
    public Map<String,Object> getAdministrationPayMx(XzqgfkmxDto xzqgfkmxDto){
        Map<String,Object> map=new HashMap<>();
        xzqgfkmxDto = xzqgfkmxService.getDtoById(xzqgfkmxDto.getFkmxid());
        map.put("xzqgfkmxDto", xzqgfkmxDto);
        return map;
    }
    /**
     * 删除行政请购付款管理数据
     */
    @RequestMapping("/administrationPay/delAdministrationPay")
    @ResponseBody
    public Map<String,Object> delAdministrationPay(XzqgfkglDto xzqgfkglDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        xzqgfkglDto.setScry(user.getYhid());
        boolean isSuccess = xzqgfkglService.deleteFkxx(xzqgfkglDto);
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }
 /**
     * 行政请购确认修改页面
     */
    @RequestMapping("/purchase/modConfirmPurchaseAdministration")
    public ModelAndView modConfirmPurchaseAdministration(XzqgqrglDto xzqgqrglDto){
        ModelAndView mav=new ModelAndView("purchase/administration/confirmPurchase_edit");
        List<JcsjDto> dzfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECONCILIATION_TYPE.getCode());
        xzqgqrglDto=xzqgqrglService.getDto(xzqgqrglDto);
        mav.addObject("dzfss",dzfss);
        mav.addObject("auditType", AuditTypeEnum.AUDIT_ADMINISTRATIONCONFIRMPURCHASE.getCode());
        mav.addObject("xzqgqrglDto",xzqgqrglDto);
        mav.addObject("url","/administration/purchase/pagedataSavePurchaseConfirm");
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }
 /**
     * 行政请购确认修改页面
     */
    @RequestMapping("/purchase/auditConfirmPurchaseAdministration")
    public ModelAndView auditConfirmPurchaseAdministration(XzqgqrglDto xzqgqrglDto){
        ModelAndView mav=new ModelAndView("purchase/administration/confirmPurchase_edit");
        List<JcsjDto> dzfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECONCILIATION_TYPE.getCode());
        xzqgqrglDto=xzqgqrglService.getDto(xzqgqrglDto);
        mav.addObject("dzfss",dzfss);
        mav.addObject("auditType", AuditTypeEnum.AUDIT_ADMINISTRATIONCONFIRMPURCHASE.getCode());
        mav.addObject("xzqgqrglDto",xzqgqrglDto);
        mav.addObject("url","/administration/purchase/pagedataSavePurchaseConfirm");
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }
 /**
     * 行政请购确认修改页面
     */
    @RequestMapping("/purchase/submitConfirmPurchaseAdministration")
    public ModelAndView submitConfirmPurchaseAdministration(XzqgqrglDto xzqgqrglDto){
        ModelAndView mav=new ModelAndView("purchase/administration/confirmPurchase_edit");
        List<JcsjDto> dzfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECONCILIATION_TYPE.getCode());
        xzqgqrglDto=xzqgqrglService.getDto(xzqgqrglDto);
        mav.addObject("dzfss",dzfss);
        mav.addObject("auditType", AuditTypeEnum.AUDIT_ADMINISTRATIONCONFIRMPURCHASE.getCode());
        mav.addObject("xzqgqrglDto",xzqgqrglDto);
        mav.addObject("url","/administration/purchase/pagedataSavePurchaseConfirm");
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * 修改行政请购确认信息
     */
    @RequestMapping("/purchase/pagedataSavePurchaseConfirm")
    @ResponseBody
    public Map<String,Object> modSavePurchaseConfirm(XzqgqrglDto xzqgqrglDto,HttpServletRequest request){
        User user=getLoginInfo(request);
        xzqgqrglDto.setLrry(user.getYhid());
        xzqgqrglDto.setXgry(user.getYhid());
        xzqgqrglDto.setScry(user.getYhid());
        Map<String,Object> map=new HashMap<>();
        boolean isSuccess=xzqgqrglService.modSavePurchaseConfirm(xzqgqrglDto);
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 删除行政请购确认信息
     */
    @RequestMapping("/purchase/delConfirmPurchaseAdministration")
    @ResponseBody
    public Map<String,Object> delConfirmPurchaseAdministration(XzqgqrglDto xzqgqrglDto,HttpServletRequest request){
        User user=getLoginInfo(request);
        xzqgqrglDto.setScry(user.getYhid());
        boolean isSuccess=xzqgqrglService.delConfirmDtos(xzqgqrglDto);
        Map<String,Object> map=new HashMap<>();
        map.put("status",isSuccess?"success":"fail");
        map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 新增行政请购付款管理数据
     */
    @RequestMapping("/administrationPay/addAdministrationPay")
    public ModelAndView addAdministrationPay(XzqgfkglDto xzqgfkglDto, HttpServletRequest request){
        User user=getLoginInfo(request);
        user = commonService.getUserInfoById(user);
        xzqgfkglDto.setSqrmc(user.getZsxm());
        xzqgfkglDto.setSqr(user.getYhid());
        xzqgfkglDto.setSqbmmc(user.getJgmc());
        xzqgfkglDto.setSqbm(user.getJgid());
        ModelAndView mav=new ModelAndView("purchase/administration/adminPurchasePay_edit");
        //获取对账方式基础数据
        List<JcsjDto> dzfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECONCILIATION_TYPE.getCode());  //对账类型
        List<JcsjDto> fkfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYWAY.getCode());  //付款申请  付款方式
        List<JcsjDto> fkfs = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYER.getCode());  //付款申请  付款方
        List<JcsjDto> fygss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAY_BELONG.getCode());  //付款申请  费用归属
        List<JcsjDto> djcdfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECEIPTS_TRANSMIT_TYPE.getCode());  //付款申请  单据传达方式
        List<JcsjDto> fylblist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.EXPENSE_CATEGORY.getCode());  //费用类别
        if(!CollectionUtils.isEmpty(dzfss)){
            for(JcsjDto jcsjDto : dzfss){
                if("1".equals(jcsjDto.getSfmr()))
                    xzqgfkglDto.setDzfsdm(jcsjDto.getCsdm());
            }
        }
        if (!CollectionUtils.isEmpty(djcdfss)){
            for (JcsjDto jcsjDto : djcdfss) {
                if ("1".equals(jcsjDto.getSfmr())){
                    xzqgfkglDto.setDjcdfs(jcsjDto.getCsid());
                }
            }
        }
        XzqgqrcglDto xzqgqrcglDto = new XzqgqrcglDto();
        String djh= xzqgqrglService.generateConfirmDjh(xzqgqrcglDto);
        xzqgfkglDto.setFkdh(djh);
        mav.addObject("djcdfss", djcdfss);
        mav.addObject("dzfss", dzfss);
        mav.addObject("fkfss",fkfss);
        mav.addObject("fkfs",fkfs);
        mav.addObject("fygss",fygss);
        mav.addObject("fylblist",fylblist);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("xzqgfkglDto",xzqgfkglDto);
        mav.addObject("auditType",AuditTypeEnum.AUDIT_ADMINPURCHASEPAY.getCode());
        mav.addObject("auditTypePTP",AuditTypeEnum.AUDIT_ADMINPURCHASEPAY_PTP.getCode());
        mav.addObject("url","/administration/administrationPay/addSaveAdministrationPay");
        return mav;
    }

    /**
     * 新增行政请购付款管理数据
     */
    @RequestMapping("/administrationPay/addSaveAdministrationPay")
    @ResponseBody
    public Map<String,Object> insertAdministrationPay(XzqgfkglDto xzqgfkglDto, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        try {
            User user = getLoginInfo(request);
            xzqgfkglDto.setLrry(user.getYhid());
            boolean isSuccess;
            JcsjDto jcsjDto=jcsjService.getDtoById(xzqgfkglDto.getDzfs());//对账方式
            if("MB".equals(jcsjDto.getCsdm())){
                isSuccess=xzqgfkglService.dealMonthData(xzqgfkglDto);
            }else{
                log.error("insertAdministrationPay 公对公处理");
                //公对公处理
                isSuccess=xzqgfkglService.dealPtpData(xzqgfkglDto);
            }
            map.put("ywid",xzqgfkglDto.getFkid());
            map.put("status", isSuccess?"success":"fail");
            map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        }

        catch (BusinessException e){
            map.put("status","fail");
            map.put("message",StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }


    /**
     * 行政付款修改页面
     */
    @RequestMapping("/administrationPay/modAdministrationPay")
    public ModelAndView modAdministrationPay(XzqgfkglDto xzqgfkglDto){
        ModelAndView mav=new ModelAndView("purchase/administration/adminPurchasePay_edit");

        xzqgfkglDto = xzqgfkglService.getDtoById(xzqgfkglDto.getFkid());
        List<XzqgfkmxDto> fkmxlist = xzqgfkmxService.getListByFkid(xzqgfkglDto.getFkid());
        List<JcsjDto> dzfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECONCILIATION_TYPE.getCode());  //对账类型
        List<JcsjDto> fkfs = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYER.getCode());  //付款申请  付款方
        List<JcsjDto> fkfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYWAY.getCode());  //付款申请  付款方式
        List<JcsjDto> fygss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAY_BELONG.getCode());  //付款申请  费用归属
        List<JcsjDto> djcdfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECEIPTS_TRANSMIT_TYPE.getCode());  //付款申请  单据传达方式
        List<JcsjDto> fylblist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.EXPENSE_CATEGORY.getCode());  //费用类别
        mav.addObject("auditType",AuditTypeEnum.AUDIT_ADMINPURCHASEPAY.getCode());
        mav.addObject("auditTypePTP",AuditTypeEnum.AUDIT_ADMINPURCHASEPAY_PTP.getCode());
        mav.addObject("dzfss",dzfss);
        mav.addObject("dzfsdm", xzqgfkglDto.getDzfsdm());
        mav.addObject("djcdfss",djcdfss);
        mav.addObject("fylblist",fylblist);
        mav.addObject("fkfs",fkfs);
        mav.addObject("fkfss",fkfss);
        mav.addObject("fygss",fygss);
        mav.addObject("fkmxlist", fkmxlist);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("xzqgfkglDto",xzqgfkglDto);
        mav.addObject("url","/administration/administrationPay/modSaveAdministrationPay");
        return mav;
    }

    /**
     * 行政付款提交页面
     */
    @RequestMapping("/administrationPay/submitAdministrationPay")
    public ModelAndView submitAdministrationPay(XzqgfkglDto xzqgfkglDto){
        ModelAndView mav=new ModelAndView("purchase/administration/adminPurchasePay_edit");

        xzqgfkglDto = xzqgfkglService.getDtoById(xzqgfkglDto.getFkid());
        List<XzqgfkmxDto> fkmxlist = xzqgfkmxService.getListByFkid(xzqgfkglDto.getFkid());
        List<JcsjDto> dzfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECONCILIATION_TYPE.getCode());  //对账类型
        List<JcsjDto> fkfs = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYER.getCode());  //付款申请  付款方
        List<JcsjDto> fkfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYWAY.getCode());  //付款申请  付款方式
        List<JcsjDto> fygss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAY_BELONG.getCode());  //付款申请  费用归属
        List<JcsjDto> djcdfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECEIPTS_TRANSMIT_TYPE.getCode());  //付款申请  单据传达方式
        List<JcsjDto> fylblist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.EXPENSE_CATEGORY.getCode());  //费用类别
        mav.addObject("auditType",AuditTypeEnum.AUDIT_ADMINPURCHASEPAY.getCode());
        mav.addObject("auditTypePTP",AuditTypeEnum.AUDIT_ADMINPURCHASEPAY_PTP.getCode());
        mav.addObject("dzfss",dzfss);
        mav.addObject("dzfsdm", xzqgfkglDto.getDzfsdm());
        mav.addObject("djcdfss",djcdfss);
        mav.addObject("fylblist",fylblist);
        mav.addObject("fkfs",fkfs);
        mav.addObject("fkfss",fkfss);
        mav.addObject("fygss",fygss);
        mav.addObject("fkmxlist", fkmxlist);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("xzqgfkglDto",xzqgfkglDto);
        mav.addObject("url","/administration/administrationPay/modSaveAdministrationPay");
        return mav;
    }

    /**
     * 行政付款提交页面
     */
    @RequestMapping("/administrationPay/auditAdministrationPay")
    public ModelAndView auditAdministrationPay(XzqgfkglDto xzqgfkglDto){
        ModelAndView mav=new ModelAndView("purchase/administration/adminPurchasePay_edit");

        xzqgfkglDto = xzqgfkglService.getDtoById(xzqgfkglDto.getFkid());
        List<XzqgfkmxDto> fkmxlist = xzqgfkmxService.getListByFkid(xzqgfkglDto.getFkid());
        List<JcsjDto> dzfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECONCILIATION_TYPE.getCode());  //对账类型
        List<JcsjDto> fkfs = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYER.getCode());  //付款申请  付款方
        List<JcsjDto> fkfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYWAY.getCode());  //付款申请  付款方式
        List<JcsjDto> fygss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAY_BELONG.getCode());  //付款申请  费用归属
        List<JcsjDto> djcdfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECEIPTS_TRANSMIT_TYPE.getCode());  //付款申请  单据传达方式
        List<JcsjDto> fylblist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.EXPENSE_CATEGORY.getCode());  //费用类别
        mav.addObject("auditType",AuditTypeEnum.AUDIT_ADMINPURCHASEPAY.getCode());
        mav.addObject("auditTypePTP",AuditTypeEnum.AUDIT_ADMINPURCHASEPAY_PTP.getCode());
        mav.addObject("dzfss",dzfss);
        mav.addObject("dzfsdm", xzqgfkglDto.getDzfsdm());
        mav.addObject("djcdfss",djcdfss);
        mav.addObject("fylblist",fylblist);
        mav.addObject("fkfs",fkfs);
        mav.addObject("fkfss",fkfss);
        mav.addObject("fygss",fygss);
        mav.addObject("fkmxlist", fkmxlist);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("xzqgfkglDto",xzqgfkglDto);
        mav.addObject("url","/administration/administrationPay/modSaveAdministrationPay");
        return mav;
    }

    /**
     * 获取行政请购付款明细列表
     */
    @ResponseBody
    @RequestMapping("/administrationPay/pagedataFkmxlist")
    public Map<String, Object> getFkmxlist(XzqgfkglDto xzqgfkglDto) {
        Map<String, Object> map = new HashMap<>();
        List<XzqgfkmxDto> fkmxlist = xzqgfkmxService.getListByFkid(xzqgfkglDto.getFkid());
        if (!CollectionUtils.isEmpty(fkmxlist)){
            map.put("rows", fkmxlist);
        }else{
            map.put("rows", null);
        }
        return map;
    }

    /**
     * 付款修改保存
     */
    @ResponseBody
    @RequestMapping("/administrationPay/modSaveAdministrationPay")
    public Map<String, Object> updateAdministrationPay(XzqgfkglDto xzqgfkglDto,HttpServletRequest request) {
        Map<String,Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        xzqgfkglDto.setXgry(user.getYhid());
        boolean isSuccess=xzqgfkglService.updateAdministationPay(xzqgfkglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

	/**
     * 行政请购确认审核列表
     */
    @RequestMapping("/purchase/pageListAuditConfirmPurchaseAdministration")
    public ModelAndView pageListAuditConfirmPurchaseAdministration() {
        ModelAndView mav = new ModelAndView("purchase/administration/confirmPurchase_auditList");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 行政请购确认审核列表待审核
     */
    @RequestMapping("/purchase/pageGetListAuditConfirmPurchaseAdministration")
    @ResponseBody
    public Map<String, Object> getListConfirmPurchase(XzqgqrglDto xzqgqrglDto,HttpServletRequest request){
        //附加委托参数
        DataPermission.addWtParam(xzqgqrglDto);
        //附加审核状态过滤
        if(GlobalString.AUDIT_SHZT_YSH.equals(xzqgqrglDto.getDqshzt())){
            DataPermission.add(xzqgqrglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "qrgl", "qrid", AuditTypeEnum.AUDIT_ADMINISTRATIONCONFIRMPURCHASE);
        }else{
            DataPermission.add(xzqgqrglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "qrgl", "qrid", AuditTypeEnum.AUDIT_ADMINISTRATIONCONFIRMPURCHASE);
        }
        DataPermission.addCurrentUser(xzqgqrglDto, getLoginInfo(request));
        List<XzqgqrglDto> t_List = xzqgqrglService.getPagedAuditList(xzqgqrglDto);

        //Json格式的要求{total:22,rows:{}}
        //构造成Json的格式传递
        Map<String,Object> result = new HashMap<>();
        result.put("total", xzqgqrglDto.getTotalNumber());
        result.put("rows", t_List);
        return result;
    }

    /**
     * 行政请购付款审核列表
     */
    @RequestMapping("/administrationPay/pageListAuditAdministrationPay")
    public ModelAndView pageListAuditAdministrationPay() {
        ModelAndView mav = new ModelAndView("purchase/administration/adminPurchasePay_auditList");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * 行政请购付款审核列表待审核
     */
    @RequestMapping("/administrationPay/pageGetListAuditAdministrationPay")
    @ResponseBody
    public Map<String, Object> getListAdministrationPay(XzqgfkglDto xzqgfkglDto,HttpServletRequest request){
        //附加委托参数
        DataPermission.addWtParam(xzqgfkglDto);
        //附加审核状态过滤
        if(GlobalString.AUDIT_SHZT_YSH.equals(xzqgfkglDto.getDqshzt())){
            List<AuditTypeEnum> auditTypes = new ArrayList<>();
            auditTypes.add(AuditTypeEnum.AUDIT_ADMINPURCHASEPAY);
            auditTypes.add(AuditTypeEnum.AUDIT_ADMINPURCHASEPAY_PTP);
            DataPermission._add(xzqgfkglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "fkgl", "fkid", auditTypes,null);
        }else{
            List<AuditTypeEnum> auditTypes = new ArrayList<>();
            auditTypes.add(AuditTypeEnum.AUDIT_ADMINPURCHASEPAY);
            auditTypes.add(AuditTypeEnum.AUDIT_ADMINPURCHASEPAY_PTP);
            DataPermission._add(xzqgfkglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "fkgl", "fkid", auditTypes,null);
        }
        DataPermission.addCurrentUser(xzqgfkglDto, getLoginInfo(request));
        List<XzqgfkglDto> t_List = xzqgfkglService.getPagedAuditList(xzqgfkglDto);

        //Json格式的要求{total:22,rows:{}}
        //构造成Json的格式传递
        Map<String,Object> result = new HashMap<>();
        result.put("total", xzqgfkglDto.getTotalNumber());
        result.put("rows", t_List);
        return result;
    }

    /**
     * 跳转行政请购确认选择列表
     */
    @RequestMapping(value = "/administration/pagedataChooseListConfirm")
    public ModelAndView chooseListConfirm(XzqgqrglDto xzqgqrglDto) {
        ModelAndView mav = new ModelAndView("purchase/administration/confirmPurchase_chooseList");
        mav.addObject("xzqgqrglDto", xzqgqrglDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 获取行政确认明细列表页面
     */
    @RequestMapping(value="/administration/getXzqrmxListPage")
    public ModelAndView getXzqrmxListPage(XzqgqrmxDto xzqgqrmxDto){
        ModelAndView mav=new ModelAndView("purchase/administration/confirmPurchase_chooseInfoList");
        List<XzqgqrmxDto> list=xzqgqrmxService.getNeedFkData(xzqgqrmxDto);
        XzqgqrglDto xzqgqrglDto=xzqgqrglService.getDtoById(xzqgqrmxDto.getQrid());
        mav.addObject("xzqgqrmxDtos",list);
        mav.addObject("xzqgqrglDto",xzqgqrglDto);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }
}
