package com.matridx.igams.storehouse.controller;

import com.alibaba.fastjson.JSONObject;
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
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.storehouse.dao.entities.CkxxDto;
import com.matridx.igams.storehouse.dao.entities.ThglDto;
import com.matridx.igams.storehouse.dao.entities.ThmxDto;
import com.matridx.igams.storehouse.service.svcinterface.ICkxxService;
import com.matridx.igams.storehouse.service.svcinterface.IFhglService;
import com.matridx.igams.storehouse.service.svcinterface.IThglService;
import com.matridx.igams.storehouse.service.svcinterface.IThmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *  退货管理
 *
 */
@Controller
@RequestMapping("/storehouse")
public class ReturnManagementController extends BaseBasicController {

    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;

    @Override
    public String getPrefix(){
        return urlPrefix;
    }

    @Autowired
    IXxglService xxglService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    IThglService thglService;
    @Autowired
    IFhglService fhglService;
    @Autowired
    ICkxxService ckxxService;
    @Autowired
    ICommonService commonService;
    @Autowired
    IThmxService thmxService;

    /**
     * 退货列表页面
     *
     * @return
     */
    @RequestMapping(value = "/returnManagement/pageListReturnManagement")
    public ModelAndView pageListReturnManagement(ThglDto thglDto) {
        ModelAndView mav = new ModelAndView("storehouse/thgl/return_management_list");
        mav.addObject("thglDto", thglDto);
        mav.addObject("auditType", AuditTypeEnum.RETURN_GOODS.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 退货列表
     *
     * @return
     */
    @RequestMapping(value = "/returnManagement/pageGetListReturnManagement")
    @ResponseBody
    public Map<String, Object> pageGetListReturnManagement(ThglDto thglDto) {
        List<ThglDto> thglDtos = thglService.getPagedDtoList(thglDto);
        Map<String, Object> map = new HashMap<>();
        map.put("total", thglDto.getTotalNumber());
        map.put("rows", thglDtos);
        return map;
    }
    /**
     * 查看退货详细信息
     * @param thglDto
     * @return
     */
    @RequestMapping(value = "/returnManagement/viewReturnManagement")
    public ModelAndView viewReturnManagement(ThglDto thglDto) {
        ModelAndView mav=new ModelAndView("storehouse/thgl/return_management_view");
        ThglDto dtoById = thglService.getDtoById(thglDto.getThid());
        ThmxDto thmxDto = new ThmxDto();
        thmxDto.setThid(thglDto.getThid());
        List<ThmxDto> dtoList = thmxService.getDtoList(thmxDto);
        mav.addObject("thglDto", dtoById);
        mav.addObject("thmxDtos", dtoList);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 退货新增页面
     *
     * @param thglDto
     * @return
     */
    @RequestMapping(value = "/returnManagement/addReturnManagement")
    public ModelAndView addReturnManagement(ThglDto thglDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        ModelAndView mav = new ModelAndView("storehouse/thgl/return_management_edit");
        thglDto.setFormAction("addSaveReturnManagement");
        // 设置默认创建日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        thglDto.setDjrq(sdf.format(date));
        String thdh = thglService.generateThdh();
        thglDto.setThdh(thdh);
        user = commonService.getUserInfoById(user);
        thglDto.setXsbm(user.getJgid());
        thglDto.setXsbmdm(user.getJgdm());
        thglDto.setXsbmmc(user.getJgmc());
        thglDto.setYwymc(user.getZsxm());
        thglDto.setYwy(user.getYhid());
        //获取仓库
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
        CkxxDto ckxxDto = new CkxxDto();
        CkxxDto kwxxDto = new CkxxDto();
        for (JcsjDto jcsjDto:jcsjDtos) {
            if ("CK".equals(jcsjDto.getCsdm())){
                ckxxDto.setCklb(jcsjDto.getCsid());
            }else if("KW".equals(jcsjDto.getCsdm())){
                kwxxDto.setCklb(jcsjDto.getCsid());
            }
        }
        List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
        List<CkxxDto> kwxxDtos = ckxxService.getDtoList(kwxxDto);
        mav.addObject("cklist", JSONObject.toJSONString(ckxxDtos));
        mav.addObject("kwlist",JSONObject.toJSONString(kwxxDtos));
        mav.addObject("thglDto", thglDto);
        mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SELLING_TYPE.getCode()));// 销售类型
        mav.addObject("ysfslist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DELIVERY_METHOD.getCode()));// 运送方式
        mav.addObject("bizlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.CURRENCY.getCode()));// 币种
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 新增保存退货信息
     * @param thglDto
     * @param request
     * @return
     */
    @RequestMapping("/returnManagement/addSaveReturnManagement")
    @ResponseBody
    public Map<String,Object> addSaveReturnManagement(ThglDto thglDto, HttpServletRequest request){
        User user=getLoginInfo(request);
        thglDto.setLrry(user.getYhid());
        thglDto.setZt(StatusEnum.CHECK_NO.getCode());
        Map<String,Object> map=new HashMap<>();
        //校验到货单号是否重复
        boolean isSuccess=thglService.getDtoByThdh(thglDto);
        if(!isSuccess) {
            map.put("status", "fail");
            map.put("message", "退货单号不允许重复！");
            return map;
        }
        boolean Success;
        //保存到货信息
        try {
            Success=thglService.addSaveReturnManagements(thglDto);
            map.put("status", Success ? "success" : "fail");
            map.put("message", Success ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("ywid",thglDto.getThid());
            map.put("auditType", AuditTypeEnum.RETURN_GOODS.getCode());
            map.put("urlPrefix",urlPrefix);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }
    /**
     * 退货修改页面
     *
     * @param thglDto
     * @return
     */
    @RequestMapping(value = "/returnManagement/modReturnManagement")
    public ModelAndView modReturnManagement(ThglDto thglDto) {
        ModelAndView mav = new ModelAndView("storehouse/thgl/return_management_edit");
        ThglDto dtoById = thglService.getDtoById(thglDto.getThid());
        if ("submitSaveReturnManagement".equals(thglDto.getLjbj())){
            dtoById.setFormAction("submitSaveReturnManagement");
        }else if ("auditSaveReturnManagement".equals(thglDto.getLjbj())){
            dtoById.setFormAction("auditSaveReturnManagement");
        }else {
            dtoById.setFormAction("modSaveReturnManagement");
        }
        dtoById.setXgqthdh(dtoById.getThdh());
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
        CkxxDto ckxxDto = new CkxxDto();
        CkxxDto kwxxDto = new CkxxDto();
        for (JcsjDto jcsjDto:jcsjDtos) {
            if ("CK".equals(jcsjDto.getCsdm())){
                ckxxDto.setCklb(jcsjDto.getCsid());
            }else if("KW".equals(jcsjDto.getCsdm())){
                kwxxDto.setCklb(jcsjDto.getCsid());
            }
        }
        List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
        List<CkxxDto> kwxxDtos = ckxxService.getDtoList(kwxxDto);
        mav.addObject("cklist", JSONObject.toJSONString(ckxxDtos));
        mav.addObject("kwlist",JSONObject.toJSONString(kwxxDtos));
        mav.addObject("thglDto", dtoById);
        mav.addObject("xslxlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SELLING_TYPE.getCode()));// 销售类型
        mav.addObject("ysfslist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DELIVERY_METHOD.getCode()));// 运送方式
        mav.addObject("bizlist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.CURRENCY.getCode()));// 币种
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("flag", "mod");
        return mav;
    }
    /**
     *  修改保存退货信息
     * @param thglDto
     * @param request
     * @return
     */
    @RequestMapping("/returnManagement/modSaveReturnManagement")
    @ResponseBody
    public Map<String,Object> modSaveReturnManagement(ThglDto thglDto, HttpServletRequest request){
        User user=getLoginInfo(request);
        thglDto.setXgry(user.getYhid());
        Map<String,Object> map=new HashMap<>();
        //校验到货单号是否重复
        boolean isSuccess=thglService.getDtoByThdh(thglDto);
        if(!isSuccess&&!thglDto.getXgqthdh().equals(thglDto.getThdh())) {
            map.put("status", "fail");
            map.put("message", "退货单号不允许重复！");
            return map;
        }
        boolean Success;
        //保存到货信息
        try {
            Success=thglService.modSaveReturnManagements(thglDto);
            map.put("status", Success ? "success" : "fail");
            map.put("message", Success ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
            map.put("ywid",thglDto.getThid());
            map.put("auditType", AuditTypeEnum.RETURN_GOODS.getCode());
            map.put("urlPrefix",urlPrefix);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }
    /**
     * 退货提交页面
     *
     * @param thglDto
     * @return
     */
    @RequestMapping(value = "/returnManagement/submitReturnManagement")
    public ModelAndView submitReturnManagement(ThglDto thglDto) {
        thglDto.setLjbj("submitSaveReturnManagement");
        return this.modReturnManagement(thglDto);
    }
    /**
     *  提交保存退货信息
     * @param thglDto
     * @param request
     * @return
     */
    @RequestMapping("/returnManagement/submitSaveReturnManagement")
    @ResponseBody
    public Map<String,Object> submitSaveReturnManagement(ThglDto thglDto, HttpServletRequest request){
        return this.modSaveReturnManagement(thglDto,request);
    }
    /**
     * 退货审核页面
     *
     * @param thglDto
     * @return
     */
    @RequestMapping(value = "/returnManagement/auditReturnManagement")
    public ModelAndView auditReturnManagement(ThglDto thglDto) {
        thglDto.setLjbj("auditSaveReturnManagement");
        return this.modReturnManagement(thglDto);
    }
    /**
     *  审核保存退货信息
     * @param thglDto
     * @param request
     * @return
     */
    @RequestMapping("/returnManagement/auditSaveReturnManagement")
    @ResponseBody
    public Map<String,Object> auditSaveReturnManagement(ThglDto thglDto, HttpServletRequest request){
        return this.modSaveReturnManagement(thglDto,request);
    }
    /**
     *  删除退货信息
     * @param thglDto
     * @param request
     * @return
     */
    @RequestMapping("/returnManagement/delReturnManagement")
    @ResponseBody
    public Map<String,Object> delReturnManagement(ThglDto thglDto, HttpServletRequest request){
        User user=getLoginInfo(request);
        thglDto.setScry(user.getYhid());
        Map<String,Object> map=new HashMap<>();
        boolean Success;
        //保存到货信息
        try {
            Success=thglService.delReturnManagement(thglDto);
            map.put("status", Success ? "success" : "fail");
            map.put("message", Success ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
            map.put("urlPrefix",urlPrefix);
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
            map.put("urlPrefix",urlPrefix);
        }
        return map;
    }
    /**
     *  废弃退货信息
     * @param thglDto
     * @param request
     * @return
     */
    @RequestMapping("/returnManagement/discardReturnManagement")
    @ResponseBody
    public Map<String,Object> discardReturnManagement(ThglDto thglDto, HttpServletRequest request){
        return this.delReturnManagement(thglDto,request);
    }

    /*
    * 查询退货明细
    * */
    @RequestMapping("/returnManagement/pagedataGetReturnManagementDetailList")
    @ResponseBody
    public Map<String,Object> pagedataGetReturnManagementDetailList(ThmxDto thmxDto) {
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isNotBlank(thmxDto.getThid())) {
            List<ThmxDto> dtoList = thmxService.getDtoList(thmxDto);
            map.put("rows", dtoList);
        }else {
            map.put("rows", null);
        }
        return map;
    }
    /**
     * 跳转退货审核列表
     * @return
     */
    @RequestMapping(value = "/returnManagement/pageListReturnManagementsh")
    public ModelAndView pageListReturnManagementsh() {
        ModelAndView mav=new ModelAndView("storehouse/thgl/return_management_auditlist");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * 获取退货审核列表
     * @return
     */
    @RequestMapping(value = "/returnManagement/pageGetListReturnManagementsh")
    @ResponseBody
    public Map<String,Object> pageGetListReturnManagementsh(ThglDto thglDto, HttpServletRequest request){
        // 附加委托参数
        DataPermission.addWtParam(thglDto);
        // 附加审核状态过滤
        if (GlobalString.AUDIT_SHZT_YSH.equals(thglDto.getDqshzt())) {
            DataPermission.add(thglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "thgl", "thid",
                    AuditTypeEnum.RETURN_GOODS);
        } else {
            DataPermission.add(thglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "thgl", "thid",
                    AuditTypeEnum.RETURN_GOODS);
        }
        DataPermission.addCurrentUser(thglDto, getLoginInfo(request));
        List<ThglDto> thglDtos = thglService.getPagedAuditReturnManagement(thglDto);
        Map<String, Object> map=new HashMap<>();
        map.put("total", thglDto.getTotalNumber());
        map.put("rows", thglDtos);
        return map;
    }
}
