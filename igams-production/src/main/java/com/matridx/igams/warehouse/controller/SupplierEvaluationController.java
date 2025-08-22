package com.matridx.igams.warehouse.controller;

import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.warehouse.dao.entities.GfpjmxDto;
import com.matridx.igams.warehouse.dao.entities.GfyzglDto;
import com.matridx.igams.warehouse.dao.entities.GfyzmxDto;
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
@RequestMapping("/evaluation")
public class SupplierEvaluationController extends BaseBasicController {
    @Autowired
    IXxglService xxglService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IGfyzglService gfyzglService;
    @Autowired
    IGfyzmxService gfyzmxService;
    @Autowired
    IGfpjmxService gfpjmxService;
    @Autowired
    IShxxService shxxService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }

    /**
     * @Description: 供方验证列表
     * @param gfyzglDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/6/17 14:09
     */
    @RequestMapping(value = "/evaluation/pageListEvaluation")
    public ModelAndView pageListEvaluation(GfyzglDto gfyzglDto) {
        ModelAndView mav=new ModelAndView("warehouse/evaluation/evaluationList");
        gfyzglDto.setAuditType(AuditTypeEnum.AUDIT_VERIFICATION_AUDIT.getCode());
        gfyzglDto.setApplicationAudit(AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode());
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SUPPLIER_TYPE});
        mav.addObject("gfgllblist", jclist.get(BasicDataTypeEnum.SUPPLIER_TYPE.getCode()));//供应商管理类型
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("gfyzglDto", gfyzglDto);
        return mav;
    }

    /** 获取供方验证列表
     * @Description:
     * @param gfyzglDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/6/17 14:09
     */
    @RequestMapping(value = "/evaluation/pageGetListEvaluation")
    @ResponseBody
    public Map<String,Object> pageGetListEvaluation(GfyzglDto gfyzglDto){
        List<GfyzglDto> gfyzglDtoList = gfyzglService.getPagedEvaluation(gfyzglDto);
        Map<String, Object> map=new HashMap<>();
        map.put("total",gfyzglDto.getTotalNumber());
        map.put("rows",gfyzglDtoList);
        return map;
    }

    /**
     * @Description: 查看
     * @param gfyzglDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/6/17 14:09
     */
    @RequestMapping(value = "/evaluation/viewEvaluation")
    public ModelAndView viewEvaluation(GfyzglDto gfyzglDto) {
        ModelAndView mav = new ModelAndView("warehouse/evaluation/evaluation_view");
        GfyzglDto gfyzglDtoT = gfyzglService.getDtoById(gfyzglDto.getGfyzid());
        mav.addObject("gfyzglDto", gfyzglDtoT);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /**
     * @Description: 查看获取明细信息
     * @param gfyzmxDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/6/17 14:10
     */
    @ResponseBody
    @RequestMapping(value = "/evaluation/pagedataGetEvaluation")
    public Map<String, Object> pagedataGetEvaluation(GfyzmxDto gfyzmxDto) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(gfyzmxDto.getGfyzid())) {
            List<GfyzmxDto> gfyzmxDtoList = gfyzmxService.getListByGfyzid(gfyzmxDto);
            map.put("rows", gfyzmxDtoList);
        } else {
            map.put("rows", new ArrayList<>());
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/evaluation/pagedataQEvaluation")
    public Map<String, Object> pagedataQEvaluation(GfyzmxDto gfyzmxDto) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(gfyzmxDto.getGfyzid())) {
            List<GfyzmxDto> gfyzmxDtoList = gfyzmxService.getListByGfyzid(gfyzmxDto);
            map.put("rows", gfyzmxDtoList);
        } else {
            map.put("rows", new ArrayList<>());
        }
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/evaluation/pagedataGfyzmxList")
    public Map<String, Object> pagedataGfyzmxList(GfyzmxDto gfyzmxDto) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(gfyzmxDto.getGfyzid())) {
            List<GfyzmxDto> gfyzmxDtoList = gfyzmxService.getPagedDtoList(gfyzmxDto);
            map.put("total",gfyzmxDto.getTotalNumber());
            map.put("rows", gfyzmxDtoList);
        } else {
            map.put("rows", new ArrayList<>());
        }
        return map;
    }


    @ResponseBody
    @RequestMapping(value = "/evaluation/pagedataGetMxList")
    public Map<String, Object> pagedataGetMxList(GfyzmxDto gfyzmxDto) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(gfyzmxDto.getGfyzid())) {
            List<GfyzmxDto> gfyzmxDtoList = gfyzmxService.getListByGfyzid(gfyzmxDto);
            map.put("total",gfyzmxDto.getTotalNumber());
            map.put("gfyzmxDtoList", gfyzmxDtoList);
        } else {
            map.put("gfyzmxDtoList", new ArrayList<>());
        }
        if (StringUtil.isNotBlank(gfyzmxDto.getGfpjid())) {
            GfpjmxDto gfpjmxDto = new GfpjmxDto();
            gfpjmxDto.setGfpjid(gfyzmxDto.getGfpjid());
            List<GfpjmxDto> gfpjmxDtoList = gfpjmxService.getDtoList(gfpjmxDto);
            map.put("gfpjmxDtoList", gfpjmxDtoList);
        } else {
            map.put("gfpjmxDtoList", new ArrayList<>());
        }
        return map;
    }



    /**
     * @Description: 新增
     * @param gfyzglDto
     * @param request
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/6/17 14:10
     */
    @RequestMapping(value = "/evaluation/addEvaluation")
    public ModelAndView addArrivalGoods(GfyzglDto gfyzglDto,HttpServletRequest request) {
        ModelAndView mav=new ModelAndView("warehouse/evaluation/evaluation_edit");
        User user = getLoginInfo(request);
        gfyzglDto.setSqr(user.getYhid());
        gfyzglDto.setSqrmc(user.getZsxm());
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        Date date = new Date();
        gfyzglDto.setYzsqsj(sdf.format(date));
        //生成记录编号
        if(StringUtil.isNotBlank(user.getJgid())){
            gfyzglDto.setJlbh(gfyzglService.buildCode(user.getJgid()));
        }
        gfyzglDto.setFormAction("addSaveEvaluation");
        gfyzglDto.setAuditType(AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode());
        mav.addObject("gfyzglDto",gfyzglDto);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * @Description: 新增保存
     * @param gfyzglDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/6/18 15:56
     */
    @ResponseBody
    @RequestMapping(value = "/evaluation/addSaveEvaluation")
    public Map<String, Object> addSaveEvaluation(GfyzglDto gfyzglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        gfyzglDto.setLrry(user.getYhid());
        try {
            boolean isSuccess = gfyzglService.insertEvaluation(gfyzglDto);
            map.put("ywid",gfyzglDto.getGfyzid());
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * @Description: 修改
     * @param gfyzglDto
     * @param request
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/6/19 16:21
     */
    @RequestMapping(value = "/evaluation/modEvaluation")
    public ModelAndView modEvaluation(GfyzglDto gfyzglDto,HttpServletRequest request) {
        ModelAndView mav=new ModelAndView("warehouse/evaluation/evaluation_edit");
        gfyzglDto = gfyzglService.getDtoById(gfyzglDto.getGfyzid());
        gfyzglDto.setFormAction("modSaveEvaluation");
        gfyzglDto.setAuditType(AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode());
        mav.addObject("gfyzglDto",gfyzglDto);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }
    /**
     * @Description: 修改保存
     * @param gfyzglDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/6/19 16:33
     */
    @ResponseBody
    @RequestMapping(value = "/evaluation/modSaveEvaluation")
    public Map<String, Object> modSaveEvaluation(GfyzglDto gfyzglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        gfyzglDto.setXgry(user.getYhid());
        try {
            boolean isSuccess = gfyzglService.updateEvaluation(gfyzglDto);
            map.put("ywid",gfyzglDto.getGfyzid());
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * @Description: 提交
     * @param gfyzglDto
     * @param request
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/6/19 16:21
     */
    @RequestMapping(value = "/evaluation/submitEvaluation")
    public ModelAndView submitEvaluation(GfyzglDto gfyzglDto,HttpServletRequest request) {
        ModelAndView mav=new ModelAndView("warehouse/evaluation/evaluation_edit");
        gfyzglDto = gfyzglService.getDtoById(gfyzglDto.getGfyzid());
        gfyzglDto.setFormAction("submitSaveEvaluation");
        gfyzglDto.setAuditType(AuditTypeEnum.AUDIT_VERIFICATION_APPLICATION.getCode());
        mav.addObject("gfyzglDto",gfyzglDto);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }
    /**
     * @Description: 提交保存
     * @param gfyzglDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/6/19 16:33
     */
    @ResponseBody
    @RequestMapping(value = "/evaluation/submitSaveEvaluation")
    public Map<String, Object> submitSaveEvaluation(GfyzglDto gfyzglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        gfyzglDto.setXgry(user.getYhid());
        try {
            boolean isSuccess = gfyzglService.updateEvaluation(gfyzglDto);
            map.put("ywid",gfyzglDto.getGfyzid());
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
     * @param gfyzglDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/6/21 17:10
     */
    @ResponseBody
    @RequestMapping(value = "/evaluation/delEvaluation")
    public Map<String, Object> delEvaluation(GfyzglDto gfyzglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        gfyzglDto.setScry(user.getYhid());
        try {
            boolean isSuccess = gfyzglService.delEvaluation(gfyzglDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * @Description: 验证审核提交
     * @param gfyzglDto
     * @param request
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/6/21 11:34
     */
    @RequestMapping(value = "/evaluation/verificationEvaluation")
    public ModelAndView verificationEvaluation(GfyzglDto gfyzglDto,HttpServletRequest request) {
        ModelAndView mav=new ModelAndView("warehouse/evaluation/evaluation_edit");
        gfyzglDto = gfyzglService.getDtoById(gfyzglDto.getGfyzid());
        gfyzglDto.setFormAction("verificationSaveEvaluation");
        gfyzglDto.setAuditType(AuditTypeEnum.AUDIT_VERIFICATION_AUDIT.getCode());
        mav.addObject("gfyzglDto",gfyzglDto);
        mav.addObject("urlPrefix",urlPrefix);
        mav.addObject("xsbj","0");
        return mav;
    }

    /**
     * @Description: 验证审核保存
     * @param gfyzglDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/6/21 11:36
     */
    @ResponseBody
    @RequestMapping(value = "/evaluation/verificationSaveEvaluation")
    public Map<String, Object> verificationSaveEvaluation(GfyzglDto gfyzglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        gfyzglDto.setXgry(user.getYhid());
        try {
            boolean isSuccess = gfyzglService.updateEvaluation(gfyzglDto);
            map.put("ywid",gfyzglDto.getGfyzid());
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }

    /**
     * @Description: 选择列表
     * @param gfyzglDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/6/17 14:09
     */
    @RequestMapping(value = "/evaluation/pagedataEvaluation")
    public ModelAndView pagedataEvaluation(GfyzglDto gfyzglDto) {
        ModelAndView mav=new ModelAndView("warehouse/evaluation/evaluationChooseList");
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("gfyzglDto", gfyzglDto);
        return mav;
    }

    /**
     * @Description: 获取选择列表
     * @param gfyzglDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/6/17 14:09
     */
    @RequestMapping(value = "/evaluation/pagedataQueryEvaluation")
    @ResponseBody
    public Map<String,Object> pagedataQueryEvaluation(GfyzglDto gfyzglDto){
        List<GfyzglDto> gfyzglDtoList = gfyzglService.getPagedDtoList(gfyzglDto);
        Map<String, Object> map=new HashMap<>();
        map.put("total",gfyzglDto.getTotalNumber());
        map.put("rows",gfyzglDtoList);
        return map;
    }

    /**
     * @Description: 打印
     * @param gfyzglDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/6/28 15:04
     */
    @RequestMapping("/evaluation/printEvaluation")
    public ModelAndView printEvaluation(GfyzglDto gfyzglDto){
        ModelAndView mav;
        mav=new ModelAndView("warehouse/evaluation/evaluation_print");
        JcsjDto jcsj = new JcsjDto();
        jcsj.setCsdm("GFYZ");
        jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
        jcsj = jcsjService.getDto(jcsj);
        mav.addObject("dqmc",StringUtil.isNotBlank(jcsj.getCskz3())?jcsj.getCskz3():"");
        mav.addObject("wjbh", StringUtil.isNotBlank(jcsj.getCsmc())?jcsj.getCsmc():"");
        GfyzglDto gfyzglDtoT = gfyzglService.getDtoById(gfyzglDto.getGfyzid());
        SimpleDateFormat sdfDO = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfD = new SimpleDateFormat("yyyy年MM月dd日");
        try {
            gfyzglDtoT.setYzsqsj(sdfD.format(sdfDO.parse(gfyzglDtoT.getYzsqsj())));
            gfyzglDtoT.setYzshsj(sdfD.format(sdfDO.parse(gfyzglDtoT.getYzshsj())));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        GfyzmxDto gfyzmxDto = new GfyzmxDto();
        gfyzmxDto.setGfyzid(gfyzglDto.getGfyzid());
        List<GfyzmxDto> gfyzmxDtoList = gfyzmxService.getListByGfyzid(gfyzmxDto);
        Map<String,String> shxxMap = gfyzglService.queryShxxMap(gfyzglDtoT.getGfyzid());
        mav.addObject("sybmqryj", shxxMap.get("sybmqryj"));
        mav.addObject("scbqryj", shxxMap.get("scbqryj"));
        mav.addObject("yfbqryj", shxxMap.get("yfbqryj"));
        mav.addObject("zlbqryj", shxxMap.get("zlbqryj"));
        mav.addObject("glzdbqryj", shxxMap.get("glzdbqryj"));
        mav.addObject("scbyj", shxxMap.get("scbyj"));
        mav.addObject("yfbyj", shxxMap.get("yfbyj"));
        mav.addObject("sybmyj", shxxMap.get("sybmyj"));
        mav.addObject("zlbyj", shxxMap.get("zlbyj"));
        mav.addObject("glzdbyj", shxxMap.get("glzdbyj"));
        mav.addObject("gfyzglDto", gfyzglDtoT);
        mav.addObject("gfyzmxDtoList", gfyzmxDtoList);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
}

