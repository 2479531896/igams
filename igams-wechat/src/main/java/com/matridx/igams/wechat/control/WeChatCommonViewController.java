package com.matridx.igams.wechat.control;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;

@Controller
@RequestMapping("/common")
public class WeChatCommonViewController  extends BaseController{
	
	@Autowired
	ISjxxService sjxxService;
	
	@Autowired
	IFjcfbService fjcfbService;
	
	/**
	 * 钉钉显示送检信息时，点击后的查看页面
	 * @param request
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/view/localInspectionView")
	public ModelAndView inspectionView(HttpServletRequest request, SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/sjxx_ListView");
		SjxxDto sjxxDto2=sjxxService.getDto(sjxxDto);
		FjcfbDto fjcfbDto = new FjcfbDto(); 
		fjcfbDto.setYwid(sjxxDto.getSjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
		mav.addObject("sjxxDto", sjxxDto2);	
		return mav;
	}
}
