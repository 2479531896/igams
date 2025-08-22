package com.matridx.igams.web.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.web.dao.entities.BbxxDto;
import com.matridx.igams.web.service.svcinterface.IBbxxService;
import com.matridx.springboot.util.base.StringUtil;
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
public class BbxxController extends BaseController{
	@Autowired
	private IBbxxService bbxxService;

	@Value("${matridx.prefix.urlprefix:}") 
	private String urlPrefix;
 
	@Autowired
	private IXxglService xxglService;
	
	@Override 
	public String getPrefix(){ return urlPrefix; }


	/**
	 * 版本信息列表页面
	 * 
	 * @return
	 */
	@RequestMapping("/versionInfo/pageListVersionInfo")
	public ModelAndView PageListVersionInfo(){
		ModelAndView mav = new ModelAndView("systemmain/versionInfo/versionInfo_list");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 获取版本信息列表
	 * 
	 * @param
	 * @return
	 */

	@RequestMapping("/versionInfo/pageGetListVersionInfo")
	@ResponseBody
	public Map<String, Object> getPageDtoListVersionInfo(BbxxDto bbxxDto) {
		Map<String, Object> map = new HashMap<>();
		List<BbxxDto> bbxxDtos = bbxxService.getPageDtoListVersionInfo(bbxxDto);
		map.put("total", bbxxDto.getTotalNumber());
		map.put("rows", bbxxDtos);
		return map;
	}
	
	/**
	 * 查看版本详情
	 */
	@RequestMapping("/versionInfo/viewVersionInfo")
	@ResponseBody
	public ModelAndView viewVersionInfo(BbxxDto bbxxDto){
		ModelAndView mav=new ModelAndView("systemmain/versionInfo/versionInfo_view");
		bbxxDto = bbxxService.getDtoVersionInfoByBbid(bbxxDto.getBbid());
		//更新内容 回车换行
		String gxnr = bbxxDto.getGxnr();
		gxnr = gxnr.replaceAll("\\r\\n", "</br>")
				.replaceAll("\\n", "</br>")
				.replaceAll("\\r", "</br>");
		bbxxDto.setGxnr(gxnr);
		mav.addObject("bbxxDto", bbxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	
	/**
	 * 删除版本信息
	 */
	@RequestMapping("/versionInfo/delVersionInfo")
	@ResponseBody
	public Map<String,Object> delVersionInfoList(BbxxDto bbxxDto) {
		// 获取用户信息
		User user = getLoginInfo();
		bbxxDto.setScry(user.getYhid());
		bbxxDto.setScbj("1");
		boolean isSuccess = bbxxService.delDtoListVersionInfo(bbxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 版本信息新增
	 */
	@RequestMapping(value ="/versionInfo/addVersionInfo")
	public ModelAndView addVersionInfo(BbxxDto bbxxDto){
		ModelAndView mav = new ModelAndView("systemmain/versionInfo/versionInfo_add");
		User user = getLoginInfo();
		bbxxDto.setCzry(user.getZsxm());
		bbxxDto.setFormAction("add");
		mav.addObject("bbxxDto", bbxxDto);
		mav.addObject("urlPrefix", urlPrefix);

		return mav;
	}
	
	/**
	 * 版本信息新增保存
	 */
	@RequestMapping(value="/versionInfo/addSaveVersionInfo")
	@ResponseBody
	public Map<String, Object> addSaveVersionInfo(BbxxDto bbxxDto){
		bbxxDto.setBbid(StringUtil.generateUUID());
		//获取用户信息
		User user = getLoginInfo();
		bbxxDto.setLrry(user.getYhid());//录入人员
		boolean isSuccess = bbxxService.insertDtoVersionInfo(bbxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 版本信息修改
	 */
	@RequestMapping(value="/versionInfo/modVersionInfo")
	public ModelAndView modVersionInfo(BbxxDto bbxxDto){
		ModelAndView mav = new ModelAndView("systemmain/versionInfo/versionInfo_add");
		bbxxDto = bbxxService.getDtoVersionInfoByBbid(bbxxDto.getBbid());
		bbxxDto.setFormAction("mod");
		mav.addObject("bbxxDto", bbxxDto);
		mav.addObject("urlPrefix", urlPrefix);

		return mav;
	}
	
	/**
	 * 版本信息修改保存
	 */
	@RequestMapping(value="/versionInfo/modSaveVersionInfo")
	@ResponseBody
	public Map<String, Object> modSaveVersionInfo(BbxxDto bbxxDto){
		//获取用户信息
		User user = getLoginInfo();
		bbxxDto.setXgry(user.getYhid());
		boolean isSuccess = bbxxService.updateDtoVersionInfo(bbxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
