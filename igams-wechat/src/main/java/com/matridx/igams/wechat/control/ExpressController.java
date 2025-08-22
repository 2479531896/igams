package com.matridx.igams.wechat.control;


import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.wechat.dao.entities.ExpressDto;
import com.matridx.igams.wechat.service.svcinterface.IExpressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/express")
public class ExpressController extends BaseController{
    @Autowired
    IExpressService expressService;

    /**
     * 快递页面
     * @param expressDto
     * @return
     */
    @RequestMapping(value ="/express/pageListExpress")
    public ModelAndView viewExpress(ExpressDto expressDto) {
        ModelAndView mav=new ModelAndView("wechat/express/express_list");
        mav.addObject("expressDto", expressDto);
        return mav;
    }

    /**
     * 条件查找列表信息
     * @param expressDto
     * @return
     */
    @RequestMapping("/express/pageGetListExpress")
    @ResponseBody
    public Map<String, Object> expressPage(ExpressDto expressDto){
        Map<String, Object> map= new HashMap<>();
        List<ExpressDto> expressList=expressService.getPagedDtoList(expressDto);
        map.put("rows", expressList);
        map.put("total", expressDto.getTotalNumber());
        return map;
    }

    /**
     * 查看物流信息页面
     * @param expressDto
     * @return
     */
    @RequestMapping("/express/pagedataExpress")
    public ModelAndView viewLogistics(ExpressDto expressDto) {
        ModelAndView mav = new ModelAndView("wechat/express/express_view");
        List<ExpressDto> kdxxList = expressService.getKdxx(expressDto);
        mav.addObject("kdxxList", kdxxList);
        return mav;
    }

    /**
     * 查看物流信息页面
     * @param expressDto
     * @return
     */
    @RequestMapping("/express/minidataViewExpressDingtalk")
    @ResponseBody
    public Map<String,Object> minidataViewExpressDingtalk(ExpressDto expressDto) {
        Map<String,Object> map = new HashMap<>();
        List<ExpressDto> kdxxList = expressService.getKdxx(expressDto);
        map.put("kdxxList", kdxxList);
        return map;
    }
}
