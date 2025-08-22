package com.matridx.igams.experiment.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.experiment.dao.entities.SysjglDto;
import com.matridx.igams.experiment.service.svcinterface.ISysjglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author : 郭祥杰
 * @date :
 */
@Controller
@RequestMapping("/reagent")
public class ReagentController extends BaseController {
    @Autowired
    private ISysjglService sysjglService;
    @Autowired
    private IJcsjService jcsjService;
    @Autowired
    private IXxglService xxglService;
    /**
     * @Description: 试剂管理列表
     * @param
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2025/5/8 13:43
     */
    @RequestMapping(value = "/reagent/pageListReagent")
    public ModelAndView pageListReagent() {
        ModelAndView mav = new ModelAndView("reagent/reagentList");
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
                new BasicDataTypeEnum[] { BasicDataTypeEnum.DETECTION_UNIT});
        mav.addObject("jcdwList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));
        return mav;
    }

    /**
     * @Description: 获取试剂管理列表数据
     * @param sysjglDto
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2025/5/9 10:05
     */
    @RequestMapping(value = "/reagent/pageGetListReagent")
    @ResponseBody
    public Map<String, Object> pageGetListReagent(SysjglDto sysjglDto) {
        Map<String,Object> map = new HashMap<>();
        List<SysjglDto> sysjglDtos = sysjglService.getPagedDtoList(sysjglDto);
        map.put("total", sysjglDto.getTotalNumber());
        map.put("rows", sysjglDtos);
        return map;
    }
    /**
     * @Description: 查看
     * @param sysjglDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2025/5/9 14:58
     */
    @RequestMapping(value = "/reagent/viewReagent")
    public ModelAndView viewReagent(SysjglDto sysjglDto) {
        ModelAndView mav = new ModelAndView("reagent/reagentView");
        SysjglDto sysjglDtoT = sysjglService.getDtoById(sysjglDto.getSysjid());
        mav.addObject("sysjglDto", sysjglDtoT);
        return mav;
    }
    /**
     * @Description: 修改
     * @param sysjglDto
     * @return org.springframework.web.servlet.ModelAndView
     * @Author: 郭祥杰
     * @Date: 2025/5/9 14:59
     */
    @RequestMapping(value = "/reagent/modReagent")
    public ModelAndView modReagent(SysjglDto sysjglDto) {
        ModelAndView mav = new ModelAndView("reagent/reagentEdit");
        SysjglDto sysjglDtoT = sysjglService.getDtoById(sysjglDto.getSysjid());
        Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
                new BasicDataTypeEnum[] { BasicDataTypeEnum.DETECTION_UNIT});
        mav.addObject("jcdwList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));
        mav.addObject("sysjglDto", sysjglDtoT);
        return mav;
    }
    /**
     * @Description: 修改保存
     * @param sysjglDto
     * @param request
     * @return java.util.Map<java.lang.String,java.lang.Object>
     * @Author: 郭祥杰
     * @Date: 2025/5/9 15:26
     */
    @ResponseBody
    @RequestMapping(value = "/reagent/modSaveReagent")
    public Map<String, Object> modSaveReagent(SysjglDto sysjglDto, HttpServletRequest request) {
        User user = getLoginInfo(request);
        sysjglDto.setXgry(user.getYhid());
        Map<String, Object> map = new HashMap<>();
        boolean isSuccess = sysjglService.update(sysjglDto);
        map.put("status", isSuccess ? "success" : "fail");
        map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
                : xxglService.getModelById("ICOM00002").getXxnr());
        return map;
	}

    @RequestMapping(value="/reagent/pagedataSjxx")
    @ResponseBody
    public Map<String,Object> pagedataSjxx(SysjglDto sysjglDto){
        return sysjglService.getSysjxxxMap(sysjglDto);
    }

}
