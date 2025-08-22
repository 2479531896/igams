package com.matridx.igams.common.controller;

import com.matridx.igams.common.dao.entities.DdspjlDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IDdspjlService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
@Controller
@RequestMapping("/record")
public class ApprovalRecordController extends BaseController {
    @Autowired
    private IJcsjService jcsjService;
    @Autowired
    private IDdspjlService ddspjlService;

    /**
     * @Description: 列表
     * @param ddspjlDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2025/7/7 15:27
     */
    @RequestMapping(value = "/record/pageListRecord")
    public ModelAndView pageListRecord(DdspjlDto ddspjlDto) {
        ModelAndView mav = new ModelAndView("common/record/record_list");
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
                new BasicDataTypeEnum[] { BasicDataTypeEnum.APPROVAL_SYNCHRONIZATION_PERMISSIONS});
        mav.addObject("splxlist", jclist.get(BasicDataTypeEnum.APPROVAL_SYNCHRONIZATION_PERMISSIONS.getCode()));
        return mav;
    }

    /**
     * @Description: 列表数据
     * @param ddspjlDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2025/7/7 15:28
     */
    @RequestMapping(value = "/record/pageGetListRecord")
    @ResponseBody
    public Map<String, Object> pageGetListRecord(DdspjlDto ddspjlDto) {
        Map<String, Object> map = new HashMap<>();
        List<DdspjlDto> list = ddspjlService.getPagedDtoList(ddspjlDto);
        map.put("total", ddspjlDto.getTotalNumber());
        map.put("rows", list);
        return map;
    }

    @RequestMapping(value = "/record/viewRecord")
    public ModelAndView viewRecord(DdspjlDto ddspjlDto) {
        ModelAndView mav = new ModelAndView("common/record/record_view");
        Map<String,Object> map = ddspjlService.queryView(ddspjlDto.getProcessinstanceid());
        mav.addObject("map",map);
        return mav;
    }
}
