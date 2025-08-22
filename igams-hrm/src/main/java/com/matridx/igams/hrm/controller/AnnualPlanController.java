package com.matridx.igams.hrm.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.hrm.dao.entities.NdpxDto;
import com.matridx.igams.hrm.dao.entities.PxglDto;
import com.matridx.igams.hrm.service.svcinterface.INdpxService;
import com.matridx.igams.hrm.service.svcinterface.IPxglService;
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
@RequestMapping("/annualPlan")
public class AnnualPlanController extends BaseController {

    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Override
    public String getPrefix() {
        return urlPrefix;
    }
    @Autowired
    private INdpxService ndpxService;
    @Autowired
    private IPxglService pxglService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IXxglService xxglService;

    /**
     * 年度培训列表
     */
    @RequestMapping(value="/annualPlan/pageListAnnualPlan")
    public ModelAndView pageListAnnualPlan() {
        ModelAndView mav=new ModelAndView("annualPlan/annualPlan/annualPlan_list");
        mav.addObject("urlPrefix",urlPrefix);
        mav.addObject("ndList",ndpxService.getFilterData());
        mav.addObject("pxfsList",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINING_METHODS.getCode()));
        return mav;
    }


    /**
     * 年度培训列表 数据
     */
    @RequestMapping(value="/annualPlan/pageGetListAnnualPlan")
    @ResponseBody
    public Map<String,Object> pageGetListAnnualPlan(NdpxDto ndpxDto){
        List<NdpxDto> list=ndpxService.getPagedDtoList(ndpxDto);
        Map<String,Object> result = new HashMap<>();
        result.put("total", ndpxDto.getTotalNumber());
        result.put("rows", list);
        return result;
    }

    /**
     * 年度培训列表 查看
     */
    @RequestMapping(value="/annualPlan/viewAnnualPlan")
    public ModelAndView viewAnnualPlan(NdpxDto ndpxDto) {
        ModelAndView mav=new ModelAndView("annualPlan/annualPlan/annualPlan_view");
        NdpxDto dto = ndpxService.getDto(ndpxDto);
        List<NdpxDto> taskInfo = ndpxService.getTaskInfo(ndpxDto);
        PxglDto pxglDto=new PxglDto();
        pxglDto.setNdpxid(ndpxDto.getNdpxid());
        List<PxglDto> dtoList = pxglService.getDtoList(pxglDto);
        mav.addObject("ndpxDto",dto);
        mav.addObject("pxglDtos",dtoList);
        mav.addObject("ndpxDtos",taskInfo);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    /**
     * 年度培训列表 新增
     */
    @RequestMapping(value="/annualPlan/addAnnualPlan")
    public ModelAndView addAnnualPlan(NdpxDto ndpxDto) {
        ModelAndView mav=new ModelAndView("annualPlan/annualPlan/annualPlan_edit");
        mav.addObject("urlPrefix",urlPrefix);
        mav.addObject("pxfsList",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINING_METHODS.getCode()));
        mav.addObject("formAction", "addSaveAnnualPlan");
        mav.addObject("ndpxDto", ndpxDto);
        return mav;
    }

    /**
     * 年度培训列表 新增保存
     */
    @RequestMapping("/annualPlan/addSaveAnnualPlan")
    @ResponseBody
    public Map<String, Object> addSaveAnnualPlan(NdpxDto ndpxDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        ndpxDto.setLrry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = ndpxService.addSaveAnnualPlan(ndpxDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 年度培训列表 修改
     */
    @RequestMapping(value="/annualPlan/modAnnualPlan")
    public ModelAndView modAnnualPlan(NdpxDto ndpxDto) {
        ModelAndView mav=new ModelAndView("annualPlan/annualPlan/annualPlan_edit");
        mav.addObject("urlPrefix",urlPrefix);
        mav.addObject("pxfsList",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINING_METHODS.getCode()));
        mav.addObject("formAction", "modSaveAnnualPlan");
        NdpxDto dto = ndpxService.getDto(ndpxDto);
        mav.addObject("ndpxDto", dto);
        return mav;
    }

    /**
     * 年度培训列表 修改保存
     */
    @RequestMapping("/annualPlan/modSaveAnnualPlan")
    @ResponseBody
    public Map<String, Object> modSaveAnnualPlan(NdpxDto ndpxDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        ndpxDto.setXgry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = ndpxService.modSaveAnnualPlan(ndpxDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 年度培训列表 删除
     */
    @RequestMapping("/annualPlan/delAnnualPlan")
    @ResponseBody
    public Map<String, Object> delAnnualPlan(NdpxDto ndpxDto, HttpServletRequest request){
        User user = getLoginInfo(request);
        ndpxDto.setScry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = ndpxService.delAnnualPlan(ndpxDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 年度培训 导入页面
     */
    @RequestMapping(value ="/annualPlan/pageImportAnnualPlan")
    public ModelAndView pageImportAnnualPlan(){
        ModelAndView mav = new ModelAndView("annualPlan/annualPlan/annualPlan_import");
        mav.addObject("ywlx", BusTypeEnum.IMP_ANNUAL_PLAN.getCode());
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 年度培训选择列表
     */
    @RequestMapping(value="/annualPlan/pagedataChooseAnnualPlan")
    public ModelAndView pagedataChooseAnnualPlan() {
        ModelAndView mav=new ModelAndView("annualPlan/annualPlan/annualPlan_chooseList");
        mav.addObject("urlPrefix",urlPrefix);
        mav.addObject("ndList",ndpxService.getFilterData());
        mav.addObject("pxfsList",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINING_METHODS.getCode()));
        return mav;
    }


    /**
     * 年度培训选择列表 数据
     */
    @RequestMapping(value="/annualPlan/pagedataGetChooseAnnualPlan")
    @ResponseBody
    public Map<String,Object> pagedataGetChooseAnnualPlan(NdpxDto ndpxDto){
        List<NdpxDto> list=ndpxService.getPagedDtoList(ndpxDto);
        Map<String,Object> result = new HashMap<>();
        result.put("total", ndpxDto.getTotalNumber());
        result.put("rows", list);
        return result;
    }
}
