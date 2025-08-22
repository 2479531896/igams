package com.matridx.igams.research.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.research.dao.entities.YfglDto;
import com.matridx.igams.research.service.svcinterface.IYfglService;

@Controller
@RequestMapping("/research")
public class ResearchController extends BaseController{

	@Autowired
	IYfglService yfglService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IXxglService xxglService;
	
	/**
	 * 研发列表页面
	 */
	@RequestMapping(value ="/research/pageListResearch")
	public ModelAndView pageListResearch(){
		ModelAndView mav = new ModelAndView("research/research/research_list");
		//获取研发类型
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.RESEARCH_TYPE});
		mav.addObject("yflxlist", jclist.get(BasicDataTypeEnum.RESEARCH_TYPE.getCode()));
		//获取当前进度（阶段）
//		mav.addObject("dqjdlist",yfglDto);
		return mav;
	}
	
	/**
	 * 研发列表
	 */
	@RequestMapping(value ="/research/listResearch")
	@ResponseBody
	public Map<String,Object> listResearch(YfglDto yfglDto){
		
		List<YfglDto> t_List = yfglService.getPagedDtoList(yfglDto);
		
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", yfglDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 研发查看
	 */
	@RequestMapping(value="/research/viewResearch")
	public ModelAndView viewDocument(YfglDto yfglDto){
		ModelAndView mav = new ModelAndView("research/research/research_view");
		YfglDto t_yfglDto = yfglService.getDtoById(yfglDto.getYfid());
		mav.addObject("yfglDto", t_yfglDto);
		return mav;
	}
	
	/**
	 * 研发新增页面
	 */
	@RequestMapping(value ="/research/addResearch")
	public ModelAndView addDocument(YfglDto yfglDto){
		ModelAndView mav = new ModelAndView("research/research/research_edit");
		yfglDto.setFormAction("add");
		mav.addObject("wjglDto", yfglDto);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.RESEARCH_TYPE});
		mav.addObject("yflxlist", jclist.get(BasicDataTypeEnum.RESEARCH_TYPE.getCode()));
		//文件导入
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_RESEARCH.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		return mav;
	}
	
	/**
	 * 研发新增保存
	 */
	@RequestMapping(value = "/research/addSaveResearch")
	@ResponseBody
	public Map<String, Object> addSaveResearch(YfglDto yfglDto){
		//获取用户信息
		User user = getLoginInfo();
		yfglDto.setLrry(user.getYhid());
		boolean isSuccess = yfglService.addSaveResearch(yfglDto);
		
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 文件修改页面
	 */
	@RequestMapping(value = "/research/modResearch")
	public ModelAndView modResearch(YfglDto yfglDto){
		ModelAndView mav = new ModelAndView("research/research/research_edit");
		YfglDto t_yfglDto = yfglService.getDto(yfglDto);
		t_yfglDto.setFormAction("mod");
		mav.addObject("yfglDto", t_yfglDto);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.RESEARCH_TYPE});
		mav.addObject("yflxlist", jclist.get(BasicDataTypeEnum.RESEARCH_TYPE.getCode()));
		
		//文件导入
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_DOCUMENT.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		//根据研发ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = yfglService.selectFjcfbDtoByYfid(yfglDto.getYfid());
		mav.addObject("fjcfbDtos",fjcfbDtos);
		return mav;
	}
}
