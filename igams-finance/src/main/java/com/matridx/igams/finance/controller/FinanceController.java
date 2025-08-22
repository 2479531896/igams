package com.matridx.igams.finance.controller;

import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.AccountEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.finance.service.svcinterface.IU8FinanceService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@code @author:JYK}
 */
@Controller
@RequestMapping("/finance")
public class FinanceController extends BaseBasicController {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IU8FinanceService u8FinanceService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    IXxglService xxglService;
    @Autowired

    /**
     * 财务列表页面
     */
    @RequestMapping(value ="/finance/pageListFinance")
    public ModelAndView pageListFinance(){
        return new ModelAndView("finance/finance/finance_list");
    }
    /**
     * 财务列表页面
     */
    @RequestMapping(value ="/finance/pagedataFinanceExport")
    public ModelAndView pagedataFinanceExport(){
        ModelAndView mav = new ModelAndView("finance/finance/finance_export");
        List<JcsjDto> xsztlist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_U8.getCode());
        List<JcsjDto> xsbblist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.REPORT_FORMS.getCode());
        List<JcsjDto> after = xsbblist.stream().filter(e->"1".equals(e.getCskz1())).collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(JcsjDto::getCsmc))), ArrayList::new));
        mav.addObject("xsztlist",xsztlist);
        mav.addObject("xsbblist",after);
        return mav;
    }
    @RequestMapping(value ="/finance/pagedataFinanceMedlabExport")
    public ModelAndView pagedataFinanceMedlabExport() throws Exception {
        ModelAndView mav = new ModelAndView("finance/finance/finance_medlab_export");
        List<JcsjDto> xsbblist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.REPORT_FORMS.getCode());
        List<JcsjDto> xsztlist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_U8.getCode());

        for(JcsjDto jcsjDto:xsbblist){
            if("SRDB_YJ".equals(jcsjDto.getCsdm())){
                JcsjDto jcsjDto1=new JcsjDto();
                JcsjDto zt=xsztlist.stream().filter(x -> x.getCsid().equals(jcsjDto.getFcsid())).findFirst().orElse(null);
                Map<String,Object>map=new HashMap<>();
                if(zt !=null){
                    map.put("databaseName",zt.getCskz1());
                    mav.addObject("databaseName",zt.getCskz1());
                    mav.addObject("ztcsdm",zt.getCsdm());
                }
                jcsjDto1.setJclb(BasicDataTypeEnum.ACCOUNT_SUBJECT.getCode());
                jcsjDto1.setFcsdm("SRDB_YJ");
                String[] fcsids=new String[]{jcsjDto.getCsid()};
                jcsjDto1.setFcsids(fcsids);
                map.put("jclb",BasicDataTypeEnum.ACCOUNT_SUBJECT.getCode());
                map.put("fcsdm","SRDB_YJ");
                List<JcsjDto> jcsjDtos=jcsjService.getJcsjDtoList(jcsjDto1);
                String ccodes=jcsjDtos.stream().map(JcsjDto::getCsdm).collect(Collectors.joining(","));
                mav.addObject("ccodes",ccodes);
                map.put("ccodes",jcsjDtos);
                List<String> years=u8FinanceService.getYearByCcode(map);
                mav.addObject("year",years);

            }
        }
        return mav;
    }

    @RequestMapping("/finance/pagedataExportMedlabFinance")
    @ResponseBody
    public Map<String,Object> pagedataExportMedlabFinance(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("ztcsdm",request.getParameter("ztcsdm"));
        map.put("bbcsdm","SRDB_YJ");
        map.put("year",request.getParameter("dbyear"));
        map.put("databaseName",request.getParameter("databaseName"));
        map.put("ccodes",Arrays.asList(request.getParameter("ccodes").split(",")));
        try {
        Map<String, Object> map1 = u8FinanceService.ExportFinanceOut(map);
        map.put("wjid",map1.get("wjid"));
        map.put("totalCount",map1.get("totalCount"));
        }catch (Exception e) {
            map.put("message", e.getStackTrace()[0].toString()+"导出失败！");
            map.put("status","error");
            return map;
        }
        map.put("status","success");
        map.put("message","导出成功");
        return map;
    }
    /**
     * 报表导出
     */
    @RequestMapping("/finance/pagedataExportFinance")
    @ResponseBody
    public Map<String,Object> pagedataExportFinance(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("ztcsdm",request.getParameter("ztcsdm"));
        map.put("bbcsdm",request.getParameter("bbcsdm"));
        map.put("rq",request.getParameter("rq"));
        try {
            Map<String,Object> map1= u8FinanceService.ExportFinanceOut(map);
            map.put("wjid",map1.get("wjid"));
            map.put("totalCount",map1.get("totalCount"));
        }catch (BusinessException e) {
            map.put("msg", AccountEnum.getValueByCode(request.getParameter("ztcsdm"))+getBbcsmc(request.getParameter("bbcsdm"))+(StringUtil.isNotBlank(e.getMsg())?e.getMsg():"导出失败！"));
        }catch (Exception e) {
            map.put("msg", AccountEnum.getValueByCode(request.getParameter("ztcsdm"))+getBbcsmc(request.getParameter("bbcsdm"))+(StringUtil.isNotBlank(String.valueOf(e.getStackTrace()[0]))?"导出失败！"+e.getStackTrace()[0].toString():"导出失败！"));
        }
        return map;
    }
    /**
     * 成本收入表选择条件
     */
    @RequestMapping(value ="/finance/pagedataRevenueCostExport")
    public ModelAndView pagedataRevenueCostExport(){
        ModelAndView mav = new ModelAndView("finance/finance/finance_revenueCostexport");
        List<JcsjDto> xsztlist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_U8.getCode());
        mav.addObject("xsztlist",xsztlist);
        List<String> years= new ArrayList<>();
        Calendar instance = Calendar.getInstance();
        for (int i = 0; i < 5; i++) {
            years.add(String.valueOf(instance.get(Calendar.YEAR)));
            instance.add(Calendar.YEAR,-1);
        }
        mav.addObject("years",years);
        return mav;
    }
    /**
     * 成本收入表导出
     */
    @RequestMapping("/finance/pagedataExportRevenueCostExport")
    @ResponseBody
    public Map<String,Object> pagedataExportRevenueCostExport(HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        map.put("ztcsdm",request.getParameter("ztcsdm"));
        map.put("year",request.getParameter("year"));
        map.put("bbcsdm","CBSR");
        try {
            Map<String,Object> map1= u8FinanceService.ExportFinanceOut(map);
            map.put("wjid",map1.get("wjid"));
            map.put("totalCount",map1.get("totalCount"));
        }catch (BusinessException e) {
            map.put("msg", AccountEnum.getValueByCode(request.getParameter("ztcsdm"))+(StringUtil.isNotBlank(e.getMsg())?e.getMsg():"导出失败！"));
        }catch (Exception e) {
            map.put("msg", AccountEnum.getValueByCode(request.getParameter("ztcsdm"))+(StringUtil.isNotBlank(String.valueOf(e.getStackTrace()[0]))?"导出失败！"+e.getStackTrace()[0].toString():"导出失败！"));
        }
        return map;
    }
    /**
     * 获取两个日期之间相差
     */
    @RequestMapping("/finance/pagedataGetAllDates")
    @ResponseBody
    public  Map<String,List<String>> pagedataGetAllDates(String startDate, String endDate){
        return u8FinanceService.getMonthAndYearBetween(startDate,endDate);
    }

    private String getBbcsmc(String bbcsdm) {
        if (bbcsdm.contains("XSFY")){
            return "销售费用";
        }else if (bbcsdm.contains("CBFY")){
            return "成本费用";
        }else if (bbcsdm.contains("BMFY")){
            return "部门费用";
        }else {
            return "";
        }
    }
}
