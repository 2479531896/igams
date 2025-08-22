package com.matridx.igams.storehouse.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.storehouse.dao.entities.KhglDto;
import com.matridx.igams.storehouse.service.svcinterface.IKhglService;
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
@RequestMapping("/systemmain")
public class KhglController extends BaseController {
	
	@Autowired
	private IKhglService khglService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	
	/**
	 * 跳转
	 * 
	 * @return
	 */
	@RequestMapping("/client/pagedataListClient")
	public ModelAndView PageList(KhglDto khglDto){
		ModelAndView mav = new ModelAndView("systemmain/client/client_list");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("khglDto", khglDto);
		return mav;
	}

	/**
	 * 系统设置列表
	 * @param 
	 * @return
	 */
	@RequestMapping("/client/pagedataGetPagedDtoList")
	@ResponseBody
	public Map<String, Object> getPagedDtoList(KhglDto khglDto){
		Map<String,Object> map=new HashMap<>();
		List<KhglDto> khglDtos = khglService.getPagedDtoList(khglDto);
		map.put("total",khglDto.getTotalNumber());
		map.put("rows",khglDtos);
		return map;
	}
	/**
	 * 钉钉获取
	 * @param
	 * @return
	 */
	@RequestMapping("/client/minidataGetPagedDtoList")
	@ResponseBody
	public Map<String, Object> minidataGetPagedDtoList(KhglDto khglDto){
		return this.getPagedDtoList(khglDto);
	}
}
