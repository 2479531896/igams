package com.matridx.igams.warehouse.controller;

import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.storehouse.dao.entities.DbglDto;
import com.matridx.igams.storehouse.dao.entities.DbmxDto;
import com.matridx.igams.storehouse.dao.entities.XzdbglDto;
import com.matridx.igams.storehouse.service.svcinterface.IDbglService;
import com.matridx.igams.storehouse.service.svcinterface.IDbmxService;
import com.matridx.igams.storehouse.service.svcinterface.IXzdbglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/allocate")
public class AllocateController extends BaseBasicController{
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    private IDbglService dbglService;
    @Autowired
    private IDbmxService dbmxService;
    @Autowired
    private IXzdbglService xzdbglService;
    @Autowired
    RedisUtil redisUtil;
    /**
     * 调拨列表
     * @param

     */
    @RequestMapping("/allocate/pageListAllocate")
    public ModelAndView getPutInStoragePageList() {
        ModelAndView mav = new  ModelAndView("warehouse/allocate/allocate_list");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 调拨列表跳转页面
     *

     */
    @RequestMapping("/allocate/pageGetListAllocate")
    @ResponseBody
    public Map<String, Object> getPageListPutInAllocate(DbglDto dbglDto){
        Map<String, Object> map = new HashMap<>();
        List<DbglDto> dbglDtos = dbglService.getPagedDtoList(dbglDto);
//        List<CkglDto> ckglDtos = ckglService.getPagedDtoList(ckglDto);
        map.put("rows",dbglDtos);
        map.put("total", dbglDto.getTotalNumber());
        return map;
    }
    /**
     * 调拨查看页面
     * @param

     */
    @RequestMapping("/allocate/viewAllocate")
    @ResponseBody
    public ModelAndView viewAllocate(DbglDto dbglDto){
        ModelAndView mav = new  ModelAndView("warehouse/allocate/allocate_view");
        DbglDto dbglDto1 = dbglService.getDtoByDbid(dbglDto);
        mav.addObject("dbglDto",dbglDto1);
        List<DbmxDto> dbmxDtos = dbmxService.getDtoListByDbid(dbglDto.getDbid());
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("dbmxDtos", dbmxDtos);
        return mav;
    }
    /**
     * 行政调拨列表
     * @param

     */
    @RequestMapping("/allocateAdministration/pageListAllocateAdministration")
    public ModelAndView pageListAllocateAdministration() {
        ModelAndView mav = new  ModelAndView("warehouse/allocateAdministration/allocateAdministration_list");
        mav.addObject("dckwlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.LOCATION_CATEGORY.getCode()));
        mav.addObject("drkwlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.LOCATION_CATEGORY.getCode()));
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

    /**
     * 行政调拨列表跳转页面
     *

     */
    @RequestMapping("/allocateAdministration/pageGetListAllocateAdministration")
    @ResponseBody
    public Map<String, Object> getPageListAllocateAdministration(XzdbglDto xzdbglDto){
        Map<String, Object> map = new HashMap<>();
        List<XzdbglDto> xzdbglDtos = xzdbglService.getPagedDtoList(xzdbglDto);
        map.put("rows",xzdbglDtos);
        map.put("total", xzdbglDto.getTotalNumber());
        return map;
    }
    /**
     * 行政调拨查看页面
     * @param

     */
    @RequestMapping("/allocateAdministration/viewAllocateAdministration")
    @ResponseBody
    public ModelAndView viewAllocateAdministration(XzdbglDto xzdbglDto){
        ModelAndView mav = new  ModelAndView("warehouse/allocateAdministration/allocateAdministration_view");
        xzdbglDto = xzdbglService.getDto(xzdbglDto);
        mav.addObject("xzdbglDto",xzdbglDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }
}
