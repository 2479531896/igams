package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.SylcDto;
import com.matridx.igams.common.dao.entities.SylcmxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DetectionTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ISylcService;
import com.matridx.igams.common.service.svcinterface.ISylcmxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/process")
public class HomepageProcessController extends BaseController{

    @Autowired
    ISylcService sylcService;
    @Autowired
    ISylcmxService sylcmxService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IXxglService xxglService;


    /**
     * 本地化首页
     */
    @RequestMapping("/process/pagedataHomepage")
    @ResponseBody
    public Map<String, Object> pagedataHomepage() {
        Map<String, Object> map= new HashMap<>();
        List<SylcDto> list=sylcService.getAllData();
        List<SylcDto> sylcDtos=new ArrayList<>();
        if(list!=null&&!list.isEmpty()){
            Map<String, List<SylcDto>> listMap = list.stream().collect(Collectors.groupingBy(SylcDto::getLcid));
            if (!listMap.isEmpty()){
                Iterator<Map.Entry<String, List<SylcDto>>> entries = listMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String,  List<SylcDto>> entry = entries.next();
                    SylcDto sylcDto_t=entry.getValue().get(0);
                    sylcDto_t.setLcmx_json(JSON.toJSONString(entry.getValue()));
                    sylcDtos.add(sylcDto_t);
                }
            }
        }
        map.put("rows", sylcDtos);
        return map;
    }

    /**
     * 首页流程--列表
     */
    @RequestMapping(value ="/process/pageListHomepageProcess")
    public ModelAndView pageListHomepageProcess(SylcDto sylcDto) {
        ModelAndView mav=new ModelAndView("common/homepage/homepageProcess_List");
        mav.addObject("sylcDto", sylcDto);
        return mav;
    }

    /**
     * 首页流程--列表数据
     */
    @RequestMapping("/process/pageGetListHomepageProcess")
    @ResponseBody
    public Map<String, Object> pageGetListHomepageProcess(SylcDto sylcDto){
        Map<String, Object> map= new HashMap<>();
        List<SylcDto> list=sylcService.getPagedDtoList(sylcDto);
        map.put("rows", list);
        map.put("total", sylcDto.getTotalNumber());
        return map;
    }

    /**
     * 首页流程--查看
     */
    @RequestMapping(value ="/process/viewHomepageProcess")
    public ModelAndView viewHomepageProcess(SylcDto sylcDto) {
        ModelAndView mav=new ModelAndView("common/homepage/homepageProcess_view");
        SylcDto dto = sylcService.getDto(sylcDto);
        mav.addObject("sylcDto", dto);
        SylcmxDto sylcmxDto=new SylcmxDto();
        sylcmxDto.setLcid(sylcDto.getLcid());
        List<SylcmxDto> sylcmxDtos=sylcmxService.getDtoList(sylcmxDto);
        mav.addObject("sylcmxDtos", sylcmxDtos);
        return mav;
    }

    /**
     * 首页流程--新增
     */
    @RequestMapping(value ="/process/addHomepageProcess")
    public ModelAndView addHomepageProcess(SylcDto sylcDto) {
        ModelAndView mav=new ModelAndView("common/homepage/homepageProcess_edit");
        sylcDto.setFormAction("addSaveHomepageProcess");
        mav.addObject("sylcDto", sylcDto);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("cdcc","1");
        List<Map<String,Object>> menuList = sylcService.getMenuList(paramMap);
        mav.addObject("mklist", JSON.toJSONString(menuList));
        paramMap.put("cdcc","3");
        List<Map<String,Object>> subMenuList = sylcService.getMenuList(paramMap);
        mav.addObject("menulist", JSON.toJSONString(subMenuList));
        List<Map<String,Object>> buttonList = sylcService.getButtonList(paramMap);
        mav.addObject("buttonlist", JSON.toJSONString(buttonList));
        mav.addObject("classlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.HOMEPAGE_PROCESS_TYPE.getCode()));
        return mav;
    }

    /**
     * 首页流程--新增保存
     */
    @RequestMapping("/process/addSaveHomepageProcess")
    @ResponseBody
    public Map<String, Object> addSaveHomepageProcess(SylcDto sylcDto){
        Map<String, Object> map= new HashMap<>();
        User user = getLoginInfo();
        sylcDto.setLrry(user.getYhid());
        boolean isSuccess=sylcService.addSaveHomepageProcess(sylcDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 首页流程--修改
     */
    @RequestMapping(value ="/process/modHomepageProcess")
    public ModelAndView modHomepageProcess(SylcDto sylcDto) {
        ModelAndView mav=new ModelAndView("common/homepage/homepageProcess_edit");
        SylcDto dto = sylcService.getDto(sylcDto);
        dto.setFormAction("modSaveHomepageProcess");
        mav.addObject("sylcDto", dto);
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("cdcc","1");
        List<Map<String,Object>> menuList = sylcService.getMenuList(paramMap);
        mav.addObject("mklist", JSON.toJSONString(menuList));
        paramMap.put("cdcc","3");
        List<Map<String,Object>> subMenuList = sylcService.getMenuList(paramMap);
        mav.addObject("menulist", JSON.toJSONString(subMenuList));
        List<Map<String,Object>> buttonList = sylcService.getButtonList(paramMap);
        mav.addObject("buttonlist", JSON.toJSONString(buttonList));
        mav.addObject("classlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.HOMEPAGE_PROCESS_TYPE.getCode()));
        return mav;
    }

    /**
     * 首页流程--修改保存
     */
    @RequestMapping("/process/modSaveHomepageProcess")
    @ResponseBody
    public Map<String, Object> modSaveHomepageProcess(SylcDto sylcDto){
        Map<String, Object> map= new HashMap<>();
        User user = getLoginInfo();
        sylcDto.setXgry(user.getYhid());
        boolean isSuccess=sylcService.modSaveHomepageProcess(sylcDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 首页流程--删除
     */
    @RequestMapping("/process/delHomepageProcess")
    @ResponseBody
    public Map<String, Object> delHomepageProcess(SylcDto sylcDto){
        Map<String, Object> map= new HashMap<>();
        User user = getLoginInfo();
        sylcDto.setScry(user.getYhid());
        boolean isSuccess=sylcService.delHomepageProcess(sylcDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
        return map;
    }

    /**
     * 首页流程--明细列表数据
     */
    @RequestMapping("/process/pagedataGetProcessDetailList")
    @ResponseBody
    public Map<String, Object> pagedataGetProcessDetailList(SylcmxDto sylcmxDto){
        Map<String, Object> map= new HashMap<>();
        if(StringUtil.isNotBlank(sylcmxDto.getLcid())){
            List<SylcmxDto> list=sylcmxService.getDtoList(sylcmxDto);
            map.put("rows", list);
        }else{
            map.put("rows", new ArrayList<>());
        }
        return map;
    }
}
