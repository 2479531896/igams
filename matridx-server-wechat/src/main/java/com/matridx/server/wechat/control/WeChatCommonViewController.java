package com.matridx.server.wechat.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.SjyzDto;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.server.wechat.service.svcinterface.ISjyzService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/common")
public class WeChatCommonViewController  extends BaseController{
	
	@Autowired
	ISjxxService sjxxService;
	
	@Autowired
	IFjcfbService fjcfbService;
	
	@Autowired
	ISjyzService sjyzService;
	
	@Autowired
	ICommonService commonService;
	
	//日志输出
	private Logger log = LoggerFactory.getLogger(WeChatCommonViewController.class);
	
	@RequestMapping(value="/view/inspectionView")
	public ModelAndView inspectionView(SjxxDto sjxxDto) {
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_reportView");
		SjxxDto sjxxDto2 = sjxxService.getDto(sjxxDto);
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid()); 
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		if(sjxxDto2 == null) {
			log.error("inspectionView:未找到记录。");
			sjxxDto2 = new SjxxDto();
		}
		mav.addObject("sjxxDto", sjxxDto2);
		return mav;
	}
	
	/**
	 * 送检验证查看页面
	 * @param sjyzDto
	 * @return
	 */
	@RequestMapping("/view/verificationView")
	public ModelAndView verificationView(SjyzDto sjyzDto) {
		ModelAndView mav;
		try {
			//查询送检验证信息
			SjyzDto sjyzDto_t = sjyzService.getDto(sjyzDto);
			mav = new ModelAndView("wechat/verification/verification_view");
			//查询送检信息
			SjxxDto sjxxDto = new SjxxDto();
			sjxxDto.setSjid(sjyzDto_t.getSjid());
			SjxxDto sjxxDto_t = sjxxService.getDto(sjxxDto);
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(sjyzDto.getYzid());
			mav.addObject("sjxxDto", sjxxDto_t);
			mav.addObject("flag", StringUtil.isNotBlank(sjyzDto.getFlag())?sjyzDto.getFlag():"0");
			mav.addObject("sjyzDto", sjyzDto_t);
			mav.addObject("fjcfbDtos", fjcfbDtos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			mav = commonService.jumpError();
		}
		return mav;
	}
}
