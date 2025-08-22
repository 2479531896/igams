package com.matridx.igams.bioinformation.controller;

import com.matridx.igams.bioinformation.dao.entities.BioDlyzzsDto;
import com.matridx.igams.bioinformation.service.svcinterface.IBioDlyzzsService;
import com.matridx.igams.common.controller.BaseController;
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
@RequestMapping("/virulence")
public class VirulenceFactorNotesController extends BaseController {

    @Autowired
    private IBioDlyzzsService bioDlyzzsService;
    @Autowired
    IXxglService xxglService;

    /**
     * 毒力因子注释列表
     */
    @RequestMapping("/virulence/pageListVirulenceFactorNotes")
    public ModelAndView pageListVirulenceFactorNotes() {
        return new ModelAndView("mngs/virulence/virulenceFactorNotes_list");
    }

    /**
     * 毒力因子注释列表 数据查询
     */
    @RequestMapping("/virulence/pageGetListVirulenceFactorNotes")
    @ResponseBody
    public Map<String, Object> pageGetListVirulenceFactorNotes(BioDlyzzsDto bioDlyzzsDto) {
        Map<String, Object> map = new HashMap<>();
        List<BioDlyzzsDto> list = bioDlyzzsService.getPagedDtoList(bioDlyzzsDto);
        map.put("total",bioDlyzzsDto.getTotalNumber());
        map.put("rows",list);
        return map;
    }

    /**
     * 毒力因子注释列表  查看
     */
    @RequestMapping("/virulence/viewVirulenceFactorNotes")
    public ModelAndView viewSpecies(BioDlyzzsDto bioDlyzzsDto) {
        ModelAndView mav=new ModelAndView("mngs/virulence/virulenceFactorNotes_view");
        BioDlyzzsDto dto = bioDlyzzsService.getDto(bioDlyzzsDto);
        mav.addObject("bioDlyzzsDto",dto);
        return mav;
    }

    /**
     * 毒力因子注释列表  新增
     */
    @RequestMapping("/virulence/addVirulenceFactorNotes")
    public ModelAndView addVirulenceFactorNotes(BioDlyzzsDto bioDlyzzsDto) {
        ModelAndView mav=new ModelAndView("mngs/virulence/virulenceFactorNotes_edit");
        bioDlyzzsDto.setFormAction("addSaveVirulenceFactorNotes");
        mav.addObject("bioDlyzzsDto",bioDlyzzsDto);
        return mav;
    }

    /**
     * 毒力因子注释列表 新增保存
     */
    @RequestMapping("/virulence/addSaveVirulenceFactorNotes")
    @ResponseBody
    public Map<String, Object> addSaveVirulenceFactorNotes(BioDlyzzsDto bioDlyzzsDto) {
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = bioDlyzzsService.insertDto(bioDlyzzsDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 毒力因子注释列表  修改
     */
    @RequestMapping("/virulence/modVirulenceFactorNotes")
    public ModelAndView modVirulenceFactorNotes(BioDlyzzsDto bioDlyzzsDto) {
        ModelAndView mav=new ModelAndView("mngs/virulence/virulenceFactorNotes_edit");
        bioDlyzzsDto=bioDlyzzsService.getDto(bioDlyzzsDto);
        bioDlyzzsDto.setFormAction("modSaveVirulenceFactorNotes");
        mav.addObject("bioDlyzzsDto",bioDlyzzsDto);
        return mav;
    }

    /**
     * 毒力因子注释列表 修改保存
     */
    @RequestMapping("/virulence/modSaveVirulenceFactorNotes")
    @ResponseBody
    public Map<String, Object> modSaveVirulenceFactorNotes(BioDlyzzsDto bioDlyzzsDto) {
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = bioDlyzzsService.updateDto(bioDlyzzsDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }

    /**
     * 毒力因子注释列表 删除
     */
    @RequestMapping("/virulence/delVirulenceFactorNotes")
    @ResponseBody
    public Map<String, Object> delVirulenceFactorNotes(BioDlyzzsDto bioDlyzzsDto) {
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = bioDlyzzsService.deleteDto(bioDlyzzsDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
}
