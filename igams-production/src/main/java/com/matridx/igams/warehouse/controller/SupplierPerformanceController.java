package com.matridx.igams.warehouse.controller;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.warehouse.dao.entities.GfjxglDto;
import com.matridx.igams.warehouse.dao.entities.GfjxmxDto;
import com.matridx.igams.warehouse.service.svcinterface.IGfjxglService;
import com.matridx.igams.warehouse.service.svcinterface.IGfjxmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
@Controller
@RequestMapping("/performance")
public class SupplierPerformanceController extends BaseBasicController {
    @Autowired
    IXxglService xxglService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IGfjxglService gfjxglService;
    @Autowired
    IGfjxmxService gfjxmxService;
    @Autowired
    IShxxService shxxService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix(){
        return urlPrefix;
    }

    /**
     * @Description: 供方绩效列表
     * @param gfjxglDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/7/2 17:05
     */
    @RequestMapping(value = "/performance/pageListPerformance")
    public ModelAndView pageListPerformance(GfjxglDto gfjxglDto) {
        ModelAndView mav=new ModelAndView("warehouse/performance/performanceList");
        gfjxglDto.setAuditType(AuditTypeEnum.AUDIT_SUPPLIER_PERFORMANCE.getCode());
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SUPPLIER_TYPE,BasicDataTypeEnum.EVALUATION_TYPE,BasicDataTypeEnum.EVALUATION_GRADE});
        mav.addObject("gfgllblist", jclist.get(BasicDataTypeEnum.SUPPLIER_TYPE.getCode()));//供应商管理类型
        mav.addObject("khxzlist", jclist.get(BasicDataTypeEnum.EVALUATION_TYPE.getCode()));//考核类型
        mav.addObject("pjlist", jclist.get(BasicDataTypeEnum.EVALUATION_GRADE.getCode()));//评估等级
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("gfjxglDto", gfjxglDto);
        return mav;
    }

    /**
     * @Description: 列表数据
     * @param gfjxglDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/7/2 17:46
     */
    @RequestMapping(value = "/performance/pageGetListPerformance")
    @ResponseBody
    public Map<String,Object> pageGetListPerformance(GfjxglDto gfjxglDto){
        List<GfjxglDto> gfjxglDtoList = gfjxglService.getPagedPerformance(gfjxglDto);
        Map<String, Object> map=new HashMap<>();
        map.put("total",gfjxglDto.getTotalNumber());
        map.put("rows",gfjxglDtoList);
        return map;
    }

    /**
     * @Description: 查看
     * @param gfjxglDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/7/2 17:47
     */
    @RequestMapping(value = "/performance/viewPerformance")
    public ModelAndView viewPerformance(GfjxglDto gfjxglDto) {
        ModelAndView mav = new ModelAndView("warehouse/performance/performance_view");
        GfjxglDto gfjxglDtoT = gfjxglService.getDtoById(gfjxglDto.getGfjxid());
        mav.addObject("gfjxglDto", gfjxglDtoT);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * @Description: 获取明细数据
     * @param gfjxmxDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/7/2 17:50
     */
    @ResponseBody
    @RequestMapping(value = "/performance/pagedataGetPerformance")
    public Map<String, Object> pagedataGetPerformance(GfjxmxDto gfjxmxDto) {
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(gfjxmxDto.getGfjxid())) {
            List<GfjxmxDto> gfjxmxDtoList = gfjxmxService.getDtoList(gfjxmxDto);
            map.put("rows", gfjxmxDtoList);
        } else {
            List<GfjxmxDto> gfjxmxDtoList = gfjxmxService.queryDtoList();
            map.put("rows", gfjxmxDtoList);
        }
        return map;
    }

    /**
     * @Description: 新增
     * @param gfjxglDto
     * @param request
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/7/3 16:40
     */
    @RequestMapping(value = "/performance/addPerformance")
    public ModelAndView addPerformance(GfjxglDto gfjxglDto, HttpServletRequest request) {
        ModelAndView mav=new ModelAndView("warehouse/performance/performance_edit");
        User user = getLoginInfo(request);
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        Date date = new Date();
        //生成记录编号
        if(StringUtil.isNotBlank(user.getJgid())){
            gfjxglDto.setJlbh(gfjxglService.buildCode(user.getJgid()));
        }
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.EVALUATION_TYPE,BasicDataTypeEnum.EVALUATION_GRADE,BasicDataTypeEnum.EVALUATION_CONTENT});
        mav.addObject("ksxzlist", jclist.get(BasicDataTypeEnum.EVALUATION_TYPE.getCode()));
        mav.addObject("pjJson", JSON.toJSONString(jclist.get(BasicDataTypeEnum.EVALUATION_GRADE.getCode())));
        List<JcsjDto> jcsjDtoList = jclist.get(BasicDataTypeEnum.EVALUATION_CONTENT.getCode());
        BigDecimal mf = new BigDecimal("0");
        if(jcsjDtoList!=null && !jcsjDtoList.isEmpty()){
            for (JcsjDto jcsjDto:jcsjDtoList){
                if(StringUtil.isNotBlank(jcsjDto.getCskz2())){
                    mf = mf.add(new BigDecimal(jcsjDto.getCskz2()));
                }
            }
        }
        gfjxglDto.setMf(mf.toString());
        gfjxglDto.setFormAction("addSavePerformance");
        gfjxglDto.setAuditType(AuditTypeEnum.AUDIT_SUPPLIER_PERFORMANCE.getCode());
        mav.addObject("gfjxglDto",gfjxglDto);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * @Description: 保存
     * @param gfjxglDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/7/3 17:23
     */
    @ResponseBody
    @RequestMapping(value = "/performance/addSavePerformance")
    public Map<String, Object> addSavePerformance(GfjxglDto gfjxglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        gfjxglDto.setLrry(user.getYhid());
        try {
            boolean isSuccess = gfjxglService.insertPerpromance(gfjxglDto);
            map.put("ywid",gfjxglDto.getGfjxid());
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
     * @param gfjxglDto
     * @param request
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/7/4 13:57
     */
    @RequestMapping(value = "/performance/modPerformance")
    public ModelAndView modPerformance(GfjxglDto gfjxglDto, HttpServletRequest request) {
        ModelAndView mav=new ModelAndView("warehouse/performance/performance_edit");
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.EVALUATION_TYPE,BasicDataTypeEnum.EVALUATION_GRADE});
        mav.addObject("ksxzlist", jclist.get(BasicDataTypeEnum.EVALUATION_TYPE.getCode()));
        mav.addObject("pjJson", JSON.toJSONString(jclist.get(BasicDataTypeEnum.EVALUATION_GRADE.getCode())));
        gfjxglDto = gfjxglService.getDtoById(gfjxglDto.getGfjxid());
        gfjxglDto.setFormAction("modSavePerformance");
        gfjxglDto.setAuditType(AuditTypeEnum.AUDIT_SUPPLIER_PERFORMANCE.getCode());
        mav.addObject("gfjxglDto",gfjxglDto);
        mav.addObject("urlPrefix",urlPrefix);
        mav.addObject("xgqGysid",gfjxglDto.getGysid());
        return mav;
    }

    /**
     * @Description: 修改保存
     * @param gfjxglDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/7/4 13:58
     */
    @ResponseBody
    @RequestMapping(value = "/performance/modSavePerformance")
    public Map<String, Object> modSavePerformance(GfjxglDto gfjxglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        gfjxglDto.setXgry(user.getYhid());
        try {
            boolean isSuccess = gfjxglService.updatePerpromance(gfjxglDto);
            map.put("ywid",gfjxglDto.getGfjxid());
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
     * @param gfjxglDto
     * @param request
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2024/7/4 15:26
     */
    @RequestMapping(value = "/performance/submitPerformance")
    public ModelAndView submitPerformance(GfjxglDto gfjxglDto, HttpServletRequest request) {
        ModelAndView mav=new ModelAndView("warehouse/performance/performance_edit");
        Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.EVALUATION_TYPE,BasicDataTypeEnum.EVALUATION_GRADE,BasicDataTypeEnum.EVALUATION_CONTENT});
        mav.addObject("ksxzlist", jclist.get(BasicDataTypeEnum.EVALUATION_TYPE.getCode()));
        mav.addObject("pjJson", JSON.toJSONString(jclist.get(BasicDataTypeEnum.EVALUATION_GRADE.getCode())));
        gfjxglDto = gfjxglService.getDtoById(gfjxglDto.getGfjxid());
        gfjxglDto.setFormAction("submitSavePerformance");
        gfjxglDto.setAuditType(AuditTypeEnum.AUDIT_SUPPLIER_PERFORMANCE.getCode());
        mav.addObject("gfjxglDto",gfjxglDto);
        mav.addObject("urlPrefix",urlPrefix);
        mav.addObject("xgqGysid",gfjxglDto.getGysid());
        return mav;
    }

    /**
     * @Description: 提交保存
     * @param gfjxglDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/7/4 15:26
     */
    @ResponseBody
    @RequestMapping(value = "/performance/submitSavePerformance")
    public Map<String, Object> submitSavePerformance(GfjxglDto gfjxglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        gfjxglDto.setXgry(user.getYhid());
        try {
            boolean isSuccess = gfjxglService.updatePerpromance(gfjxglDto);
            map.put("ywid",gfjxglDto.getGfjxid());
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
     * @param gfjxglDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2024/7/4 15:25
     */
    @ResponseBody
    @RequestMapping(value = "/performance/delPerformance")
    public Map<String, Object> delPerformance(GfjxglDto gfjxglDto, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        gfjxglDto.setScry(user.getYhid());
        try {
            boolean isSuccess = gfjxglService.delPerformance(gfjxglDto);
            map.put("status", isSuccess ? "success" : "fail");
            map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        } catch (BusinessException e) {
            map.put("status", "fail");
            map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
        }
        return map;
    }


    @RequestMapping("/performance/printPerformance")
    public ModelAndView printPerformance(GfjxglDto gfjxglDto){
        ModelAndView mav;
        mav=new ModelAndView("warehouse/performance/performance_print");
        JcsjDto jcsj = new JcsjDto();
        jcsj.setCsdm("GFJX");
        jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
        jcsj = jcsjService.getDto(jcsj);
        mav.addObject("dqmc",StringUtil.isNotBlank(jcsj.getCskz3())?jcsj.getCskz3():"");
        mav.addObject("wjbh", StringUtil.isNotBlank(jcsj.getCsmc())?jcsj.getCsmc():"");
        GfjxglDto gfjxglDtoT = gfjxglService.getDtoById(gfjxglDto.getGfjxid());
        if(StringUtil.isBlank(gfjxglDtoT.getLxr())){
            gfjxglDtoT.setLxr("/");
        }
        if(StringUtil.isBlank(gfjxglDtoT.getCz())){
            gfjxglDtoT.setCz("/");
        }
        if(StringUtil.isBlank(gfjxglDtoT.getDh())){
            gfjxglDtoT.setDh("/");
        }
        GfjxmxDto gfjxmxDto = new GfjxmxDto();
        gfjxmxDto.setGfjxid(gfjxglDto.getGfjxid());
        List<GfjxmxDto> gfjxmxDtoList = gfjxmxService.getDtoList(gfjxmxDto);
        Map<String,String> shxxMap = gfjxglService.queryShxxMap(gfjxglDto.getGfjxid());
        String gfjxmxJson = JSON.toJSONString(gfjxmxDtoList);
        mav.addObject("gfjxmxJson", gfjxmxJson);
        mav.addObject("scbyj", shxxMap.get("scbyj"));
        mav.addObject("zlbyj", shxxMap.get("zlbyj"));
        mav.addObject("sybmyj", shxxMap.get("sybmyj"));
        mav.addObject("cgbyj", shxxMap.get("cgbyj"));
        mav.addObject("glzdbyj", shxxMap.get("glzdbyj"));
        mav.addObject("gfjxglDto", gfjxglDtoT);
        mav.addObject("gfjxmxDtoList", gfjxmxDtoList);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
}
