package com.matridx.igams.bioinformation.controller;

import com.matridx.igams.bioinformation.dao.entities.BioWzglDto;
import com.matridx.igams.bioinformation.service.svcinterface.IBioWzglService;
import com.matridx.igams.common.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/species")
public class SpeciesController extends BaseController {

    @Autowired
    private IBioWzglService bioWzglService;

    /**
     * 物种管理列表
     */
    @RequestMapping("/species/pageListSpecies")
    public ModelAndView pageListSpecies() {
        return new ModelAndView("mngs/species/species_list");
    }

    /**
     * 物种管理列表 数据查询
     */
    @RequestMapping("/species/pageGetListSpecies")
    @ResponseBody
    public Map<String, Object> pageGetListSpecies(BioWzglDto bioWzglDto) {
        Map<String, Object> map = new HashMap<>();
        List<BioWzglDto> list = bioWzglService.getPagedDtoList(bioWzglDto);
        map.put("total",bioWzglDto.getTotalNumber());
        map.put("rows",list);
        return map;
    }

    /**
     * 物种管理列表  查看
     */
    @RequestMapping("/species/viewSpecies")
    public ModelAndView viewSpecies(BioWzglDto bioWzglDto) {
        ModelAndView mav=new ModelAndView("mngs/species/species_view");
        BioWzglDto dto = bioWzglService.getDto(bioWzglDto);
        mav.addObject("bioWzglDto",dto);
        return mav;
    }

}
