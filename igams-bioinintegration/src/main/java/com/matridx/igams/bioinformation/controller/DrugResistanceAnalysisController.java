package com.matridx.igams.bioinformation.controller;

import com.matridx.igams.bioinformation.dao.entities.ZsxxDto;
import com.matridx.igams.bioinformation.service.svcinterface.IZsxxService;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/analysis")
public class DrugResistanceAnalysisController extends BaseController {

    @Autowired
    private IZsxxService zsxxService;
    @Autowired
    IXxglService xxglService;

    /**
     * 耐药分析注释列表
     */
    @RequestMapping("/analysis/pageListDrugResistanceAnalysis")
    public ModelAndView pageListDrugResistanceAnalysis() {
        return new ModelAndView("mngs/analysis/drugResistanceAnalysis_list");
    }

    /**
     * 耐药分析注释列表 数据查询
     */
    @RequestMapping("/analysis/pageGetListDrugResistanceAnalysis")
    @ResponseBody
    public Map<String, Object> pageGetListDrugResistanceAnalysis(ZsxxDto zsxxDto) {
        Map<String, Object> map = new HashMap<>();
        List<ZsxxDto> list = zsxxService.getPagedDtoList(zsxxDto);
        map.put("total",zsxxDto.getTotalNumber());
        map.put("rows",list);
        return map;
    }

    /**
     * 耐药分析注释列表  查看
     */
    @RequestMapping("/analysis/viewDrugResistanceAnalysis")
    public ModelAndView viewDrugResistanceAnalysis(ZsxxDto zsxxDto) {
        ModelAndView mav=new ModelAndView("mngs/analysis/drugResistanceAnalysis_view");
        ZsxxDto dto = zsxxService.getDto(zsxxDto);
        mav.addObject("zsxxDto",dto);
        return mav;
    }

    /**
     * 耐药分析注释列表  新增
     */
    @RequestMapping("/analysis/addDrugResistanceAnalysis")
    public ModelAndView addDrugResistanceAnalysis(ZsxxDto zsxxDto) {
        ModelAndView mav=new ModelAndView("mngs/analysis/drugResistanceAnalysis_edit");
        zsxxDto.setFormAction("addSaveDrugResistanceAnalysis");
        mav.addObject("zsxxDto",zsxxDto);
        return mav;
    }

    /**
     * 耐药分析注释列表 新增保存
     */
    @RequestMapping("/analysis/addSaveDrugResistanceAnalysis")
    @ResponseBody
    public Map<String, Object> addSaveDrugResistanceAnalysis(ZsxxDto zsxxDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        zsxxDto.setLrry(user.getYhid());
        boolean isSuccess = zsxxService.insertDto(zsxxDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 耐药分析注释列表  修改
     */
    @RequestMapping("/analysis/modDrugResistanceAnalysis")
    public ModelAndView modDrugResistanceAnalysis(ZsxxDto zsxxDto) {
        ModelAndView mav=new ModelAndView("mngs/analysis/drugResistanceAnalysis_edit");
        zsxxDto=zsxxService.getDto(zsxxDto);
        zsxxDto.setFormAction("modSaveDrugResistanceAnalysis");
        mav.addObject("zsxxDto",zsxxDto);
        return mav;
    }

    /**
     * 耐药分析注释列表 修改保存
     */
    @RequestMapping("/analysis/modSaveDrugResistanceAnalysis")
    @ResponseBody
    public Map<String, Object> modSaveDrugResistanceAnalysis(ZsxxDto zsxxDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        zsxxDto.setXgry(user.getYhid());
        boolean isSuccess = zsxxService.updateDto(zsxxDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 耐药分析注释列表 删除
     */
    @RequestMapping("/analysis/delDrugResistanceAnalysis")
    @ResponseBody
    public Map<String, Object> delDrugResistanceAnalysis(ZsxxDto zsxxDto) {
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = zsxxService.deleteDto(zsxxDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
}
