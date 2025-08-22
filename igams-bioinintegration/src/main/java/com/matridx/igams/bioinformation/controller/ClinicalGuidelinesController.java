package com.matridx.igams.bioinformation.controller;

import com.matridx.igams.bioinformation.dao.entities.BioLczzznglDto;
import com.matridx.igams.bioinformation.service.svcinterface.IBioLczzznglService;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
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
@RequestMapping("/guidelines")
public class ClinicalGuidelinesController extends BaseController {

    @Autowired
    private IBioLczzznglService bioLczzznglService;
    @Autowired
    IXxglService xxglService;
    @Autowired
    RedisUtil redisUtil;

    /**
     * 临床指南列表
     */
    @RequestMapping("/guidelines/pageListClinicalGuidelines")
    public ModelAndView pageListClinicalGuidelines() {
        return new ModelAndView("mngs/guidelines/clinicalGuidelines_list");
    }

    /**
     * 临床指南列表 数据查询
     */
    @RequestMapping("/guidelines/pageGetListClinicalGuidelines")
    @ResponseBody
    public Map<String, Object> pageGetListClinicalGuidelines(BioLczzznglDto bioLczzznglDto) {
        Map<String, Object> map = new HashMap<>();
        List<BioLczzznglDto> list = bioLczzznglService.getPagedDtoList(bioLczzznglDto);
        map.put("total",bioLczzznglDto.getTotalNumber());
        map.put("rows",list);
        return map;
    }

    /**
     * 临床指南列表  查看
     */
    @RequestMapping("/guidelines/viewClinicalGuidelines")
    public ModelAndView viewClinicalGuidelines(BioLczzznglDto bioLczzznglDto) {
        ModelAndView mav=new ModelAndView("mngs/guidelines/clinicalGuidelines_view");
        BioLczzznglDto dto = bioLczzznglService.getDto(bioLczzznglDto);
        mav.addObject("bioLczzznglDto",dto);
        return mav;
    }

    /**
     * 临床指南列表  新增
     */
    @RequestMapping("/guidelines/addClinicalGuidelines")
    public ModelAndView addClinicalGuidelines(BioLczzznglDto bioLczzznglDto) {
        ModelAndView mav=new ModelAndView("mngs/guidelines/clinicalGuidelines_edit");
        bioLczzznglDto.setFormAction("addSaveClinicalGuidelines");
        mav.addObject("bioLczzznglDto",bioLczzznglDto);
        mav.addObject("yblxList",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
        return mav;
    }

    /**
     * 临床指南列表 新增保存
     */
    @RequestMapping("/guidelines/addSaveClinicalGuidelines")
    @ResponseBody
    public Map<String, Object> addSaveClinicalGuidelines(BioLczzznglDto bioLczzznglDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        bioLczzznglDto.setLrry(user.getYhid());
        boolean isSuccess = bioLczzznglService.insertDto(bioLczzznglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 临床指南列表  修改
     */
    @RequestMapping("/guidelines/modClinicalGuidelines")
    public ModelAndView modClinicalGuidelines(BioLczzznglDto bioLczzznglDto) {
        ModelAndView mav=new ModelAndView("mngs/guidelines/clinicalGuidelines_edit");
        bioLczzznglDto=bioLczzznglService.getDto(bioLczzznglDto);
        bioLczzznglDto.setFormAction("modSaveClinicalGuidelines");
        mav.addObject("bioLczzznglDto",bioLczzznglDto);
        mav.addObject("yblxList",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode()));
        return mav;
    }

    /**
     * 临床指南列表 修改保存
     */
    @RequestMapping("/guidelines/modSaveClinicalGuidelines")
    @ResponseBody
    public Map<String, Object> modSaveClinicalGuidelines(BioLczzznglDto bioLczzznglDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        bioLczzznglDto.setXgry(user.getYhid());
        boolean isSuccess = bioLczzznglService.updateDto(bioLczzznglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 临床指南列表 删除
     */
    @RequestMapping("/guidelines/delClinicalGuidelines")
    @ResponseBody
    public Map<String, Object> delClinicalGuidelines(BioLczzznglDto bioLczzznglDto) {
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = bioLczzznglService.deleteDto(bioLczzznglDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
}
