package com.matridx.igams.experiment.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.experiment.dao.entities.ZdhJkwdjlDto;
import com.matridx.igams.experiment.dao.entities.ZdhYbmxDto;
import com.matridx.igams.experiment.dao.entities.ZdhYbxxDto;
import com.matridx.igams.experiment.service.impl.ZdhYbxxServiceImpl;
import com.matridx.igams.experiment.service.svcinterface.IZdhJkwdjlService;
import com.matridx.igams.experiment.service.svcinterface.IZdhYbmxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/exteranl")
public class ExternalSampleMsgController extends BaseController {
    @Autowired
    ZdhYbxxServiceImpl ybxxService;
    @Autowired
    IZdhYbmxService ybmxService;
    @Autowired
    IZdhJkwdjlService zdhJkwdjlService;
    /**
     * 实验室外部标本信息
     * @param ybxxDto
     * @return
     */
    @RequestMapping(value="/sample/pageGetListSample")
    @ResponseBody
    public Map<String,Object> getSamplelistvuePage(HttpServletRequest request, ZdhYbxxDto ybxxDto){
        Map<String,Object> map=new HashMap<>();
        ybxxDto.setSyzt("1");

        if(!CollectionUtils.isEmpty(ybxxDto.getLcs())){
            List<String> lcmcs =new ArrayList<>();
            List<String> lcxhs =new ArrayList<>();
            for(String lc:ybxxDto.getLcs()){
                String[] lcarr=lc.split("_");
                lcmcs.add(lcarr[0]);
                lcxhs.add(lcarr[1]);
            }
            ybxxDto.setLcmcs(lcmcs);
            ybxxDto.setLcxhs(lcxhs);
        }
        if(!CollectionUtils.isEmpty(ybxxDto.getZlcs())){
            List<String> zlcmcs =new ArrayList<>();
            List<String> zlcxhs =new ArrayList<>();
            for(String zlc:ybxxDto.getZlcs()){
                String[] zlcarr=zlc.split("_");
                zlcmcs.add(zlcarr[0]);
                zlcxhs.add(zlcarr[1]);
            }
            ybxxDto.setZlcmcs(zlcmcs);
            ybxxDto.setZlcxhs(zlcxhs);
        }
        List<ZdhYbxxDto> list=ybxxService.getDtoList(ybxxDto);
        super.setCzdmList(request,map);
        super.setTyszList(request,map);
        map.put("total", ybxxDto.getTotalNumber());
        map.put("rows",list);
        return map;
    }

    @RequestMapping(value="/sample/viewSimpleObject")
    @ResponseBody
    public Map<String,Object> viewSimpleObject( ZdhYbxxDto ybxxDto){
        Map<String,Object> map=new HashMap<>();
        ZdhYbxxDto dto = ybxxService.getDto(ybxxDto);
        ZdhJkwdjlDto jkwdjlDto = new ZdhJkwdjlDto();
        jkwdjlDto.setYbxxid(ybxxDto.getYbxxid());
        List<ZdhJkwdjlDto> dtoList = zdhJkwdjlService.getDtoList(jkwdjlDto);
        List<String> temperatureList = dtoList.stream().map(item -> item.getWd()).collect(Collectors.toList());
        List<String> dateList = dtoList.stream().map(item -> item.getSj()).collect(Collectors.toList());
        map.put("title",dto.getBbbm()+"建库温度");
        map.put("dateList",dateList);
        map.put("temperatureList",temperatureList);
        ZdhYbmxDto ybmxDto=new ZdhYbmxDto();
        ybmxDto.setYbxxid(ybxxDto.getYbxxid());
        List<ZdhYbmxDto> ybmxDtos=ybmxService.getDtosByYbid(ybmxDto);
        map.put("YbxxDto",dto);
        map.put("ybmxDtos",ybmxDtos);
        return map;
    }
}
