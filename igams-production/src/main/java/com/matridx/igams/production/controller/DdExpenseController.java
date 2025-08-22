package com.matridx.igams.production.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.production.dao.entities.DdbxglDto;
import com.matridx.igams.production.dao.entities.DdbxmxDto;
import com.matridx.igams.production.service.svcinterface.IDdbxglService;
import com.matridx.igams.production.service.svcinterface.IDdbxmxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
@Controller
@RequestMapping("/expense")
public class DdExpenseController extends BaseController{
    @Autowired
    private IDdbxglService ddbxglService;
    @Autowired
    private IDdbxmxService ddbxmxService;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;

    @Override
    public String getPrefix() {
        return urlPrefix;
    }

    /*
     * 增加一个个人审批列表页面
     */
    @RequestMapping("/expense/pageListDdExpense")
    public ModelAndView pageOneSpyqlist()
    {
        ModelAndView mav = new ModelAndView("common/expense/ddExpense_list");
        List<JcsjDto> jcdxlxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EXPENSE_TYPE.getCode());
        mav.addObject("bxlxlist",jcdxlxs);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /*
     * 数据库中取出个人审批列表数据
     */
    @RequestMapping("/expense/pageGetListDdExpense")
    @ResponseBody
    public Map<String,Object> getOneSpyqList(DdbxglDto ddbxglDto) {
        Map<String,Object> map = new HashMap<>();
        List<DdbxglDto> list=ddbxglService.getPagedDtoList(ddbxglDto);
        map.put("rows",list);
        map.put("total",ddbxglDto.getTotalNumber());
        return map;
    }

    /*
     * 查看个人审批列表的一条信息
     */
    @RequestMapping("/expense/viewDdExpense")
    public ModelAndView showOneSpyq(DdbxglDto ddbxglDto) {
        ModelAndView mav = new ModelAndView("common/expense/ddExpense_view");
        ddbxglDto=ddbxglService.getDto(ddbxglDto);
        mav.addObject("ddbxglDto",ddbxglDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
    /*
     * 获取明细
     */
    @RequestMapping("/expense/pagedataGetDdExpenseListInfo")
    @ResponseBody
    public Map<String,Object> pagedataGetDdExpenseListInfo(DdbxmxDto ddbxmxDto) {
        Map<String,Object> map = new HashMap<>();
        List<DdbxmxDto> list=ddbxmxService.getPagedDtoList(ddbxmxDto);
        map.put("rows",list);
        map.put("total",ddbxmxDto.getTotalNumber());
        return map;
    }
}
