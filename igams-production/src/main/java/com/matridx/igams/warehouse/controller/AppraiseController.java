package com.matridx.igams.warehouse.controller;

import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.warehouse.dao.entities.GfpjbDto;
import com.matridx.igams.warehouse.dao.entities.GfpjmxDto;
import com.matridx.igams.warehouse.service.svcinterface.IGfpjbService;
import com.matridx.igams.warehouse.service.svcinterface.IGfpjmxService;
import com.matridx.igams.warehouse.service.svcinterface.IGfyzglService;
import com.matridx.igams.warehouse.service.svcinterface.IGfyzmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
@Controller
@RequestMapping("/appraise")
public class AppraiseController extends BaseBasicController {
    @Autowired
    IXxglService xxglService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IGfyzglService gfyzglService;
    @Autowired
    IGfyzmxService gfyzmxService;
    @Autowired
    IGfpjbService gfpjbService;
    @Autowired
    IGfpjmxService gfpjmxService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }

    /**
     * @Description: 供方评价列表
     * @param gfpjbDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/6/25 17:27
     */
    @RequestMapping(value = "/appraise/pageListAppraise")
    public ModelAndView pageListAppraise(GfpjbDto gfpjbDto) {
        ModelAndView mav=new ModelAndView("warehouse/appraise/appraiseList");
        gfpjbDto.setAuditType(AuditTypeEnum.AUDIT_SUPPLIER_EVALUATION.getCode());
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SUPPLIER_TYPE});
        mav.addObject("gfgllblist", jclist.get(BasicDataTypeEnum.SUPPLIER_TYPE.getCode()));//供应商管理类型
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("gfpjbDto", gfpjbDto);
        return mav;
    }

    /**
     * @Description: 供方评价列表数据
     * @param gfpjbDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/6/25 17:28
     */
    @RequestMapping(value = "/appraise/pageGetListAppraise")
    @ResponseBody
    public Map<String,Object> pageGetListAppraise(GfpjbDto gfpjbDto){
        List<GfpjbDto> gfpjbDtoList = gfpjbService.getPagedGfpjbDto(gfpjbDto);
        Map<String, Object> map=new HashMap<>();
        map.put("total",gfpjbDto.getTotalNumber());
        map.put("rows",gfpjbDtoList);
        return map;
    }

    /**
     * @Description: 查看
     * @param gfpjbDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/6/25 17:51
     */
    @RequestMapping(value = "/appraise/viewAppraise")
    public ModelAndView viewAppraise(GfpjbDto gfpjbDto) {
        ModelAndView mav = new ModelAndView("warehouse/appraise/appraise_view");
        GfpjbDto gfpjbDtoT = new GfpjbDto();
        if(StringUtil.isNotBlank(gfpjbDto.getGfyzid())){
            gfpjbDtoT = gfpjbService.getDtoByGfyzId(gfpjbDto.getGfyzid());
        }
        if(StringUtil.isNotBlank(gfpjbDto.getGfpjid())){
            gfpjbDtoT = gfpjbService.getDtoById(gfpjbDto.getGfpjid());
        }
        if(gfpjbDtoT==null){
            gfpjbDtoT = gfpjbDto;
        }
        mav.addObject("gfpjbDto", gfpjbDtoT);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * @Description: 获取查看明细数据
     * @param gfpjmxDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/6/26 9:41
     */
    @ResponseBody
    @RequestMapping(value = "/appraise/pagedataGetAppraise")
    public Map<String, Object> pagedataGetAppraise(GfpjmxDto gfpjmxDto) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(gfpjmxDto.getGfpjid())) {
            List<GfpjmxDto> gfpjmxDtoList = gfpjmxService.getDtoList(gfpjmxDto);
            map.put("rows", gfpjmxDtoList);
        } else {
            map.put("rows", new ArrayList<>());
        }
        return map;
    }

    /**
     * @Description: 供方评价表新增
     * @param gfpjbDto
     * @param request
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/6/26 10:13
     */
    @RequestMapping("/appraise/addAppraise")
    public ModelAndView addAppraise(GfpjbDto gfpjbDto, HttpServletRequest request) {
        ModelAndView mav=new ModelAndView("warehouse/appraise/appraise_edit");
        User user = getLoginInfo(request);
        gfpjbDto.setSqr(user.getYhid());
        gfpjbDto.setSqrmc(user.getZsxm());
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        Date date = new Date();
        gfpjbDto.setPjsj(sdf.format(date));
        //生成记录编号
        if(StringUtil.isNotBlank(user.getJgid())){
            gfpjbDto.setJlbh(gfpjbService.buildCode(user.getJgid()));
        }
        gfpjbDto.setFormAction("addSaveAppraise");
        gfpjbDto.setAuditType(AuditTypeEnum.AUDIT_SUPPLIER_EVALUATION.getCode());
        mav.addObject("gfpjbDto",gfpjbDto);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * @Description: 供方评价新增保存
     * @param gfpjbDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/6/26 10:14
     */
    @ResponseBody
    @RequestMapping(value = "/appraise/addSaveAppraise")
    public Map<String, Object> addSaveAppraise(GfpjbDto gfpjbDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        gfpjbDto.setLrry(user.getYhid());
        try {
            boolean isSuccess = gfpjbService.insertAppraise(gfpjbDto);
            map.put("ywid",gfpjbDto.getGfpjid());
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * @Description: 供方评价修改
     * @param gfpjbDto
     * @param request
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/6/26 14:04
     */
    @RequestMapping("/appraise/modAppraise")
    public ModelAndView modAppraise(GfpjbDto gfpjbDto, HttpServletRequest request) {
        ModelAndView mav=new ModelAndView("warehouse/appraise/appraise_edit");
        gfpjbDto = gfpjbService.getDtoById(gfpjbDto.getGfpjid());
        gfpjbDto.setFormAction("modSaveAppraise");
        gfpjbDto.setAuditType(AuditTypeEnum.AUDIT_SUPPLIER_EVALUATION.getCode());
        mav.addObject("gfpjbDto",gfpjbDto);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * @Description: 供方评价修改保存
     * @param gfpjbDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/6/26 14:05
     */
    @ResponseBody
    @RequestMapping(value = "/appraise/modSaveAppraise")
    public Map<String, Object> modSaveAppraise(GfpjbDto gfpjbDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        gfpjbDto.setXgry(user.getYhid());
        try {
            boolean isSuccess = gfpjbService.updateAppraise(gfpjbDto);
            map.put("ywid",gfpjbDto.getGfpjid());
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * @Description: 供方评价提交
     * @param gfpjbDto
     * @param request
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/6/26 14:04
     */
    @RequestMapping("/appraise/submitAppraise")
    public ModelAndView submitAppraise(GfpjbDto gfpjbDto, HttpServletRequest request) {
        ModelAndView mav=new ModelAndView("warehouse/appraise/appraise_edit");
        gfpjbDto = gfpjbService.getDtoById(gfpjbDto.getGfpjid());
        gfpjbDto.setFormAction("submitSaveAppraise");
        gfpjbDto.setAuditType(AuditTypeEnum.AUDIT_SUPPLIER_EVALUATION.getCode());
        mav.addObject("gfpjbDto",gfpjbDto);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * @Description: 供方评价提交保存
     * @param gfpjbDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/6/26 14:05
     */
    @ResponseBody
    @RequestMapping(value = "/appraise/submitSaveAppraise")
    public Map<String, Object> submitSaveAppraise(GfpjbDto gfpjbDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        gfpjbDto.setXgry(user.getYhid());
        try {
            boolean isSuccess = gfpjbService.updateAppraise(gfpjbDto);
            map.put("ywid",gfpjbDto.getGfpjid());
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * @Description: 删除
     * @param gfpjbDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/6/26 14:52
     */
    @ResponseBody
    @RequestMapping(value = "/appraise/delAppraise")
    public Map<String, Object> delAppraise(GfpjbDto gfpjbDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        gfpjbDto.setScry(user.getYhid());
        try {
            boolean isSuccess = gfpjbService.delAppraise(gfpjbDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * @Description: 供方评价打印
     * @param gfpjbDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/7/1 15:00
     */
    @RequestMapping("/appraise/printAppraise")
    public ModelAndView printAppraise(GfpjbDto gfpjbDto){
        ModelAndView mav;
        mav=new ModelAndView("warehouse/appraise/appraise_print");
        JcsjDto jcsj = new JcsjDto();
        jcsj.setCsdm("GFPJ");
        jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
        jcsj = jcsjService.getDto(jcsj);
        mav.addObject("dqmc",StringUtil.isNotBlank(jcsj.getCskz3())?jcsj.getCskz3():"");
        mav.addObject("wjbh", StringUtil.isNotBlank(jcsj.getCsmc())?jcsj.getCsmc():"");
        GfpjbDto gfpjbDtoT = gfpjbService.getDtoById(gfpjbDto.getGfpjid());
        SimpleDateFormat sdfDO = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfD = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            gfpjbDtoT.setPjsj(sdfD.format(sdfDO.parse(gfpjbDtoT.getPjsj())));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        GfpjmxDto gfpjmxDto = new GfpjmxDto();
        gfpjmxDto.setGfpjid(gfpjbDto.getGfpjid());
        List<GfpjmxDto> gfpjmxDtoList = gfpjmxService.getDtoList(gfpjmxDto);
        Map<String,String> shxxMap = gfpjmxService.queryShxxMap(gfpjbDto.getGfpjid());
        mav.addObject("cgbyj", shxxMap.get("cgbyj"));
        mav.addObject("scbyj", shxxMap.get("scbyj"));
        mav.addObject("yfbyj", shxxMap.get("yfbyj"));
        mav.addObject("sybmyj", shxxMap.get("sybmyj"));
        mav.addObject("zlbyj", shxxMap.get("zlbyj"));
        mav.addObject("glzdbyj", shxxMap.get("glzdbyj"));
        mav.addObject("sqryj", shxxMap.get("sqryj"));
        mav.addObject("gfpjbDto", gfpjbDtoT);
        mav.addObject("gfpjmxDtoList", gfpjmxDtoList);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
}
