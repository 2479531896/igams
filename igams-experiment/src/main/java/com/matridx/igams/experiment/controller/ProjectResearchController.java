package com.matridx.igams.experiment.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.experiment.dao.entities.MbglDto;
import com.matridx.igams.experiment.dao.entities.XmcyDto;
import com.matridx.igams.experiment.dao.entities.XmglDto;
import com.matridx.igams.experiment.dao.entities.XmjdrwDto;
import com.matridx.igams.experiment.dao.entities.XmjdxxDto;
import com.matridx.igams.experiment.service.svcinterface.IMbglService;
import com.matridx.igams.experiment.service.svcinterface.IRwrqService;
import com.matridx.igams.experiment.service.svcinterface.IXmcyService;
import com.matridx.igams.experiment.service.svcinterface.IXmglService;
import com.matridx.igams.experiment.service.svcinterface.IXmjdrwService;
import com.matridx.igams.experiment.service.svcinterface.IXmjdxxService;

@Controller
@RequestMapping("/experiment")
public class ProjectResearchController extends BaseController{

	@Autowired
	IXmglService xmglService;
	@Autowired
	IXmjdxxService xmjdxxService;
	@Autowired
	IXmjdrwService xmjdrwService;
	@Autowired
	IMbglService mbglService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IXmcyService xmcyService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IRwrqService rwrqService;
	
	/**
	 * 项目研发列表页面
	 */
	@RequestMapping(value="/research/pageListProjectResearch")
	public ModelAndView pageListTaskResearch(){
		return new ModelAndView("experiment/research/project_researchlist");
	}
	
	/**
	 * 项目研发列表
	 */
	@RequestMapping(value="/research/pageGetListProjectResearch")
	@ResponseBody
	public Map<String,Object> getProjectResearchList(XmjdrwDto xmjdrwDto){
		// 获取用户信息
		User user = getLoginInfo();
		xmjdrwDto.setYhid(user.getYhid());
		List<XmjdrwDto> xmgllist=xmjdrwService.getPagedSearchTaskList(xmjdrwDto);//项目研发列表
		if(xmgllist.size()>0) {
		List<XmjdrwDto> xmlastjdlist=xmjdrwService.getXhIsLastJdXh(xmgllist);//阶段不为最终阶段的rwidlist
			for (XmjdrwDto dto : xmlastjdlist) {
				xmgllist.get(dto.getOrdernum()).setXygxmjdid(dto.getXmjdid());
				xmgllist.get(dto.getOrdernum()).setXygxmjdmc(dto.getJdmc());
			}
		}
		Map<String, Object> map= new HashMap<>();
		map.put("total",xmjdrwDto.getTotalNumber());
		map.put("rows",xmgllist);
		return map;
		}
	
	/**
	 * 查看项目研发详细信息
	 */
	@RequestMapping(value="/research/viewProjectResearch")
	@ResponseBody
	public ModelAndView ProjectResearchView(XmjdrwDto xmjdrwDto) {
		ModelAndView mav = new ModelAndView("experiment/research/project_researchview");
		// 获取用户信息
		User user = getLoginInfo();
		xmjdrwDto.setYhid(user.getYhid());
		XmjdrwDto xmyfrwxx=xmjdrwService.getSearchTaskRwDto(xmjdrwDto);
		XmglDto xmcyDto=new XmglDto();
		xmcyDto.setXmid(xmjdrwDto.getXmid());
		List<XmcyDto> xmcylist=xmcyService.selectSelMember(xmcyDto);
		List<XmjdxxDto> xmjdxxlist= xmjdrwService.getzrwByjd(xmjdrwDto);
		//根据任务ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(xmjdrwDto.getRwid());
		if(fjcfbDtos != null && fjcfbDtos.size() > 0){
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
				fjcfbDto.setWjmhz(wjmhz);
			}
		}
		mav.addObject("fjcfbDtos", fjcfbDtos);
		mav.addObject("Xmcylist", xmcylist);
		mav.addObject("Xmjdrwxx", xmyfrwxx);
		mav.addObject("Xmjdxxlist",xmjdxxlist);
		return mav;
	}
	
	/**
	 * 跳转新增界面
	 */
	@RequestMapping(value="/research/addProjectResearch")
	@ResponseBody
	public ModelAndView toAddResearchPage(XmjdrwDto xmjdrwDto) {
		ModelAndView mav=new ModelAndView("experiment/research/project_researchadd");
		//获取模板列表
		List<MbglDto> mbgldtos = mbglService.getAllMb(new MbglDto());
		User user=getLoginInfo();
		//添加默认项目成员
		XmcyDto xmcyDto=new XmcyDto();
		xmcyDto.setYhid(user.getYhid());
		xmcyDto.setZsxm(user.getZsxm());
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.TASK_LABEL,BasicDataTypeEnum.TASK_RANK});
		xmjdrwDto.setFormAction("addSaveProjectResearch");
		xmjdrwDto.setFzr(user.getYhid());
		xmjdrwDto.setFzrmc(user.getZsxm());
		mav.addObject("xmjdrwDto", xmjdrwDto);
		mav.addObject("xmcyDto", xmcyDto);
		mav.addObject("task_label", jclist.get(BasicDataTypeEnum.TASK_LABEL.getCode()));
		mav.addObject("task_rank", jclist.get(BasicDataTypeEnum.TASK_RANK.getCode()));
		mav.addObject("xmglDtos", mbgldtos);
		return mav;
	}
	
	/**
	 * 保存提交项目研发任务
	 */
	@RequestMapping(value ="/research/addSaveProjectResearch")
	@ResponseBody
	public Map<String,Object> AddSaveResearchProject(XmjdrwDto xmjdrwDto){
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		xmjdrwDto.setLrry(user.getYhid());
		XmglDto xmglDto=new XmglDto();
		xmglDto.setXmmc(xmjdrwDto.getXmmc());
		boolean xmcexit=xmglService.selectXmByXmmc(xmglDto);
		if(!xmcexit) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOMM_XM00001").getXxnr());
			return map;
		}
		boolean isSuccess=xmjdrwService.saveSearchProject(xmjdrwDto);
		if(("1").equals(xmjdrwDto.getMbzrwfzrxx())) {
			map.put("status", "caution");
			map.put("message",xxglService.getModelById("ICOMM_XMYF00001").getXxnr());
			return map;
		}
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 根据输入内容查询系统用户
	 */
	@RequestMapping(value="/research/pagedataSelectXmcy")
	@ResponseBody
	public Map<String, Object> getXmcy(XmglDto xmglDto){
		Map<String,Object> map = new HashMap<>();
		List<XmglDto> cyxx=xmglService.selectXtyh(xmglDto);
		map.put("xmglDtos",cyxx);
		map.put("xmglDto",xmglDto);
		return map;
	}
	
	/**
	 * 获取已选项目成员
	 */
	@RequestMapping(value = "/research/getMemberList")
	@ResponseBody
	public Map<String,Object> getMemberList(XmglDto xmglDto){
		List<XmcyDto> selmemebers = xmcyService.selectSelMember(xmglDto);
		Map<String,Object> result = new HashMap<>();
		result.put("rows", selmemebers);
		return result;
	}
	
	/**
	 * 删除项目研发任务
	 */
	@RequestMapping(value ="/research/delProjectResearch")
	@ResponseBody
	public Map<String,Object> deleteXmxx(XmjdrwDto xmjdrwDto){
		User user = getLoginInfo();
		xmjdrwDto.setScry(user.getYhid());
		boolean isSuccess=xmjdrwService.deleteSearchProjecttoHsz(xmjdrwDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
		return map; 
	}
	
	/**
	 * 跳转修改项目研发页面
	 */
	@RequestMapping(value ="/research/modProjectResearch")
	public ModelAndView modXmyfxx(XmjdrwDto xmjdrwDto){
		ModelAndView mav=new ModelAndView("experiment/research/project_researchadd");
		User user = getLoginInfo();
		xmjdrwDto.setYhid(user.getYhid());
		XmjdrwDto xmyfrwxx=xmjdrwService.getSearchTaskRwDto(xmjdrwDto);
		//获取子任务信息
		XmjdrwDto xmjdzrw=new XmjdrwDto();
		xmjdzrw.setFrwid(xmjdrwDto.getRwid());
		List<XmjdxxDto> zrwjdlist=xmjdxxService.getJdsByFrwid(xmjdzrw);
		List<XmjdrwDto> zrwxxlist=xmjdrwService.getDtoListById(xmjdrwDto.getRwid());
		XmglDto xmglDto=new XmglDto();
		xmglDto.setXmid(xmyfrwxx.getXmid());
		List<XmcyDto> xmcyDtos = xmcyService.selectSelMember(xmglDto);
		List<Map<String,Object> > listmap= new ArrayList<>();
		for (XmcyDto xmcyDto : xmcyDtos) {
			Map<String, Object> map = new HashMap<>();
			map.put("value", xmcyDto.getYhid());
			map.put("text", xmcyDto.getZsxm());
			listmap.add(map);
		}
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.TASK_LABEL,BasicDataTypeEnum.TASK_RANK});
		xmyfrwxx.setFormAction("modSaveProjectResearch");
		mav.addObject("zrwxxlist", zrwxxlist);
		mav.addObject("zrwjdlist", zrwjdlist);
		mav.addObject("xmjdrwDto", xmyfrwxx);
		mav.addObject("xmcyDto", JSON.toJSON(listmap)); 
		mav.addObject("task_label", jclist.get(BasicDataTypeEnum.TASK_LABEL.getCode()));
		mav.addObject("task_rank", jclist.get(BasicDataTypeEnum.TASK_RANK.getCode()));
		return mav; 
	}
	
	
	//编辑保存项目研发信息
	@RequestMapping(value ="/research/modSaveProjectResearch")
	@ResponseBody
	public Map<String,Object> updateXmxx(XmjdrwDto xmjdrwDto){
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		xmjdrwDto.setXgry(user.getYhid());
		xmjdrwDto.setYhid(user.getYhid());
		boolean isSuccess=xmjdrwService.modXmyfrw(xmjdrwDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map; 
	}
	
	//转项目任务阶段页面
	@RequestMapping(value= "/research/pagedataModJdxx")
	public ModelAndView getXmjdzhPage(XmjdrwDto xmjdrwDto) {
		ModelAndView mav=new ModelAndView("experiment/research/project_changeXmjdPage");
		XmjdrwDto xmjdrwxx=xmjdrwService.getSearchTaskRwDto(xmjdrwDto);
		XmjdxxDto xmjdxxDto=new XmjdxxDto();
		xmjdxxDto.setXmid(xmjdrwxx.getXmid());
		List<XmjdxxDto> xmjdxxlist=xmjdxxService.getDtoList(xmjdxxDto);
		mav.addObject("xmjdrwDto", xmjdrwxx);
		mav.addObject("xmjdxxlist", xmjdxxlist);
		return mav;
	}
	
	//转阶段
	@RequestMapping (value="/research/pagedataTochangejd")
	@ResponseBody
	public Map<String,Object> toChangeJdxx(XmjdrwDto xmjdrwDto) {
		// 获取用户信息
		User user = getLoginInfo();
		xmjdrwDto.setXgry(user.getYhid());
		boolean isSuccess = xmjdrwService.ResearchtaskSort(xmjdrwDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map; 
	}
}
