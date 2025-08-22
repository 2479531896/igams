package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.XszbDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXszbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/saletarget")
public class SaletargetController extends BaseController{
	@Autowired
	IXszbService xszbService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	RedisUtil redisUtil;
	
	/**
	 * 销售目标导入页面
	 */
	@RequestMapping(value ="/saletarget/pageImportSale")
	public ModelAndView importSamp(){
		ModelAndView mav = new ModelAndView("systemmain/saletarget/sale_import");
		
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SALE.getCode());
		XszbDto xszbDto = new XszbDto();
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("xszbDto", xszbDto);
		
		return mav;
	}
	
	/**
	 * 增加一个销售列表的页面模块
	 */
	@RequestMapping("/saletarget/pageListSale")
	public ModelAndView pageMessage() {
		ModelAndView mav = new ModelAndView("systemmain/saletarget/saletarget_list");
		List<JcsjDto> zblxlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SALE_TYPE.getCode());
		List<JcsjDto> zbfllist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SALE_CLASSIFY.getCode());
		List<JcsjDto> zbzfllist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SALE_SUBCLASSIFY.getCode());
		mav.addObject("zblxlist",zblxlist);
		mav.addObject("zbfllist",zbfllist);
		mav.addObject("zbzfllist",zbzfllist);
		return mav;
	}
	
	/**
	 * 显示销售列表列表
	 */
	@RequestMapping("/saletarget/pageGetListSale")
	@ResponseBody
	public Map<String, Object> getShtxList(XszbDto xszbDto){
		Map<String,Object> map=new HashMap<>();
		List<XszbDto> xszblist = xszbService.getPagedDtoList(xszbDto);
		map.put("total",xszbDto.getTotalNumber());
		map.put("rows",xszblist);
		return map;
	}
	
	/**
	 * 查看某条销售数据详情
	 */
	@RequestMapping("/saletarget/viewSale")
	@ResponseBody
	public ModelAndView viewShtxList(XszbDto xszbDto){
		ModelAndView mav=new ModelAndView("systemmain/saletarget/saletarget_view");
		xszbDto = xszbService.getDto(xszbDto);
		mav.addObject("xszbDto", xszbDto);
		return mav;
	}

	@RequestMapping("/saletarget/delSale")
	@ResponseBody
	public Map<String,Object> delSale(XszbDto xszbDto){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = xszbService.delete(xszbDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	@RequestMapping("/saletarget/addSale")
	@ResponseBody
	public ModelAndView addSale(XszbDto xszbDto){
		ModelAndView mav=new ModelAndView("systemmain/saletarget/sale_edit");
		xszbDto.setFormAction("addSaveSale");
		mav.addObject("xszbDto", xszbDto);
		mav.addObject("zblxList",redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.SALE_TYPE.getCode()));
		mav.addObject("zbflList",redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.SALE_CLASSIFY.getCode()));
		mav.addObject("zbzflList",redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.SALE_SUBCLASSIFY.getCode()));
		mav.addObject("zbzflJson", JSON.toJSONString(redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.SALE_SUBCLASSIFY.getCode())));
		return mav;
	}

	@RequestMapping(value="/saletarget/addSaveSale")
	@ResponseBody
	public Map<String, Object> addSaveSale(XszbDto xszbDto){
		Map<String, Object> map;
		xszbDto.setLrry(getLoginInfo().getYhid());
		if(StringUtil.isBlank(xszbDto.getZblx()))
			xszbDto.setZblx(xszbDto.getZblxid());
		map = xszbService.editSaveSale(xszbDto);
		return map;
	}

	/**
	 * 修改时新增一个页面
	 */
	@RequestMapping(value="/saletarget/modSale")
	@ResponseBody
	public ModelAndView modSale(XszbDto xszbDto){
		ModelAndView mav=new ModelAndView("systemmain/saletarget/sale_edit");
		xszbDto = xszbService.getDto(xszbDto);
		xszbDto.setFormAction("modSaveSale");
		mav.addObject("xszbDto", xszbDto);
		mav.addObject("zblxList",redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.SALE_TYPE.getCode()));
		mav.addObject("zbflList",redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.SALE_CLASSIFY.getCode()));
		mav.addObject("zbzflList",redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.SALE_SUBCLASSIFY.getCode()));
		mav.addObject("zbzflJson", JSON.toJSONString(redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.SALE_SUBCLASSIFY.getCode())));
		return mav;
	}

	/**
	 * 修改消息到数据库
	 */
	@RequestMapping(value="/saletarget/modSaveSale")
	@ResponseBody
	public Map<String,Object> modSaveSale(XszbDto xszbDto){
		Map<String,Object> map;
		xszbDto.setXgry(getLoginInfo().getYhid());
		if(StringUtil.isBlank(xszbDto.getZblx()))
			xszbDto.setZblx(xszbDto.getZblxid());
		map = xszbService.editSaveSale(xszbDto);
		return map;
	}
	
}
