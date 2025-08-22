package com.matridx.igams.common.controller;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.PcrsyglDto;
import com.matridx.igams.common.dao.entities.PcrsyjgDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IPcrsyglService;
import com.matridx.igams.common.service.svcinterface.IPcrsyjgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/pcrinfo")
public class PcrController extends BaseController {
    @Autowired
    private IPcrsyjgService pcrsyjgService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    private IPcrsyglService pcrsyglService;
    @Autowired
    private RedisUtil redisUtil;


    //PCR列表页面
    @RequestMapping("/pcrinfo/pageListPcrinfo")
    public ModelAndView pageListPcrinfo() {
        ModelAndView mav = new ModelAndView("common/list/pcrinfo_list");
        List<JcsjDto> jcdwlist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode());  //检测单位
        mav.addObject("jcdwlist",jcdwlist);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }

    //PCR列表
    @RequestMapping("/pcrinfo/pageGetListPcrinfo")
    @ResponseBody
    public Map<String, Object> getPcrinfoData(PcrsyglDto pcrsyglDto, HttpServletRequest request){
        Map<String,Object> map= new HashMap<>();
        List<PcrsyglDto> pcrlist = new ArrayList<>();
        User user = getLoginInfo(request);
        List<Map<String,String>> jcdwList = pcrsyjgService.getJsjcdwByjsid(user.getDqjs());
        if(jcdwList!=null && !jcdwList.isEmpty()) {
	        if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
	            List<String> strList=new ArrayList<>();
                for (Map<String, String> stringStringMap : jcdwList) {
                    if (stringStringMap.get("jcdw") != null) {
                        strList.add(stringStringMap.get("jcdw"));
                    }
                }
	            if(!strList.isEmpty()) {
	                pcrsyglDto.setJcdwxz(strList);
	                pcrlist = pcrsyglService.getPagedDtoList(pcrsyglDto);
	            }
	        }else {
	            pcrlist = pcrsyglService.getPagedDtoList(pcrsyglDto);
	        }
        }else {
            pcrlist = pcrsyglService.getPagedDtoList(pcrsyglDto);
        }
        map.put("total",pcrsyglDto.getTotalNumber());
        map.put("rows",pcrlist);
        return map;
    }

    //查看PCR列表数据
    @RequestMapping("/pcrinfo/viewPcrinfo")
    public ModelAndView viewPcrinfo(PcrsyjgDto pcrsyjgDto) {
        ModelAndView mav = new ModelAndView("common/view/pcrinfo_view");
        List<PcrsyjgDto> pcrsyjgDtos= pcrsyjgService.getDtoList(pcrsyjgDto);
        mav.addObject("pcrsyjgDtos",pcrsyjgDtos);
        mav.addObject("urlPrefix",urlPrefix);
        return mav;
    }
}
