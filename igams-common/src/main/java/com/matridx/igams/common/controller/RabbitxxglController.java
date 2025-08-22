package com.matridx.igams.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.dao.entities.XxdlcwglDto;
import com.matridx.igams.common.service.svcinterface.IXxdlcwglService;


@RequestMapping("/rabbitxxgl")
@Controller
public class RabbitxxglController extends BaseController{
	@Autowired
	private IXxdlcwglService xxdlcwglService;
	
	/**
	 * 增加一个页面
	 */
     @RequestMapping("/rabbitxxgl/pageListRabbit")
     public ModelAndView PageRabbit() {
		 return new ModelAndView("common/rabbitxxgl/rabbitxxgl_list");
     }
     
 	/**
 	 * 页面显示所有消息队列错误的数据
 	 */
     @RequestMapping("/rabbitxxgl/pageGetListRabbit")
     @ResponseBody
     public Map<String,Object> getRabbitList(XxdlcwglDto xxdlcwglDto){
    	 Map<String,Object> map = new HashMap<>();
    	 List<XxdlcwglDto> xxdlList = xxdlcwglService.getPagedDtoList(xxdlcwglDto);
    	 map.put("rows",xxdlList);
    	 map.put("total",xxdlcwglDto.getTotalNumber());
    	 return map;
     }
     
 	/**
 	 * 增加一个查看的页面
 	 */
     @RequestMapping("/rabbitxxgl/viewRabbitxxgl")
     public ModelAndView viewRabbit(XxdlcwglDto xxdlcwglDto) {
    	 ModelAndView mav = new ModelAndView("common/rabbitxxgl/rabbitxxgl_view");
    	 xxdlcwglDto = xxdlcwglService.getDto(xxdlcwglDto);
    	 mav.addObject("xxdlcwglDto",xxdlcwglDto);
    	 return mav;
     }
     
}
