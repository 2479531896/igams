package com.matridx.igams.dmp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.dmp.dao.entities.JkrzxxDto;
import com.matridx.igams.dmp.dao.entities.ZyxxDto;
import com.matridx.igams.dmp.service.svcinterface.IJkrzxxService;
import com.matridx.igams.dmp.service.svcinterface.IZyxxService;

@Controller
@RequestMapping("/dmp")
public class DataUserController extends BaseController{

	@Autowired
	IJkrzxxService jkrzxxService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IZyxxService zyxxService;
	
	/**
	 * 数据用户列表页面
	 * @return
	 */
	@RequestMapping(value ="/data/pageListDataUser")
	public ModelAndView pageListDataUser(){
        return new ModelAndView("dmp/user/datauser_list");
	}
	
	/**
	 * 数据用户列表
	 * @return
	 */
	@RequestMapping(value ="/data/listDataUser")
	@ResponseBody
	public Map<String,Object> listDataUser(JkrzxxDto jkrzxxDto){
		
		List<JkrzxxDto> t_List = jkrzxxService.getPagedDtoList(jkrzxxDto);
		
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", jkrzxxDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 数据用户查看
	 * @param jkrzxxDto
	 * @return
	 */
	@RequestMapping(value="/data/viewDataUser")
	public ModelAndView viewDataUser(JkrzxxDto jkrzxxDto){
		ModelAndView mav = new ModelAndView("dmp/user/datauser_view");
		jkrzxxDto = jkrzxxService.getDtoById(jkrzxxDto.getRzid());
		mav.addObject("jkrzxxDto", jkrzxxDto);
		
		return mav;
	}
	
	/**
	 * 数据用户新增
	 * @param xtshDto
	 * @return
	 */
	@RequestMapping(value ="/data/addDataUser")
	public ModelAndView addDataUser(JkrzxxDto jkrzxxDto){
		ModelAndView mav = new ModelAndView("dmp/user/datauser_edit");
		jkrzxxDto.setFormAction("add");
		mav.addObject("jkrzxxDto", jkrzxxDto);
		return mav;
	}
	
	/**
	 * 数据用户新增保存
	 * @param xtshDto
	 * @return
	 */
	@RequestMapping(value="/data/addSaveDataUser")
	@ResponseBody
	public Map<String, Object> addSaveDataUser(JkrzxxDto jkrzxxDto){
		//获取用户信息
		User user = getLoginInfo();
		jkrzxxDto.setLrry(user.getYhid());
		
		boolean isSuccess = jkrzxxService.insertDto(jkrzxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 数据用户修改
	 * @param jkrzxxDto
	 * @return
	 */
	@RequestMapping(value="/data/modDataUser")
	public ModelAndView modDataUser(JkrzxxDto jkrzxxDto){
		ModelAndView mav = new ModelAndView("dmp/user/datauser_edit");
		
		jkrzxxDto = jkrzxxService.getDtoById(jkrzxxDto.getRzid());
		jkrzxxDto.setFormAction("mod");
		mav.addObject("jkrzxxDto", jkrzxxDto);
		
		return mav;
	}
	
	/**
	 * 数据用户修改保存
	 * @param jkrzxxDto
	 * @return
	 */
	@RequestMapping(value="/data/modSaveDataUser")
	@ResponseBody
	public Map<String, Object> modSaveDataUser(JkrzxxDto jkrzxxDto){
		//获取用户信息
		User user = getLoginInfo();
		jkrzxxDto.setXgry(user.getYhid());
		
		boolean isSuccess = jkrzxxService.update(jkrzxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 删除数据用户信息
	 * @param xtshDto
	 * @return
	 */
	@RequestMapping(value="/data/delDataUser")
	@ResponseBody
	public Map<String, Object> delDataUser(JkrzxxDto jkrzxxDto){
		User user = getLoginInfo();
		jkrzxxDto.setScry(user.getYhid());
		jkrzxxDto.setScbj("1");
		boolean isSuccess = jkrzxxService.delete(jkrzxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 修改数据用户权限页面
	 * @param jkrzxxDto
	 * @return
	 */
	@RequestMapping(value = "/user/addDataUserCompetence")
	public ModelAndView setMenu(JkrzxxDto jkrzxxDto){
		ModelAndView mav = new ModelAndView("dmp/user/datauser_competence");
		mav.addObject("jkrzxxDto", jkrzxxDto);
		return mav;
	}
	
	/**
	 * 查询权限资源列表
	 * @param jkrzxxDto
	 * @param rsp
	 * @return
	 */
	@RequestMapping(value = "/user/dataUserCompetenceTree",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String dataUserCompetenceTree(JkrzxxDto jkrzxxDto,HttpServletResponse rsp){
		String JSONDATA = "";
		List<ZyxxDto> ZyxxList = zyxxService.getZyxxTreeList(jkrzxxDto.getRzid());
		JSONDATA = zyxxService.installTree(ZyxxList,JSONDATA);
		rsp.addHeader("Access-Control-Allow-Origin", "*");
		rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
		return JSONDATA;
	}
	
	/**
	 * 保存用户权限信息
	 * @param zyxxDto
	 * @param rsp
	 * @return
	 */
	@RequestMapping(value = "/user/dataUserCompetenceSaveTree",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> dataUserCompetenceSaveTree(ZyxxDto zyxxDto,HttpServletResponse rsp){
		try{
			User user = getLoginInfo();
			zyxxDto.setXgry(user.getYhid());
			boolean result = zyxxService.saveCompetenceArray(zyxxDto);
			Map<String,Object> map = new HashMap<>();
			map.put("status", result?"success":"fail");
			map.put("message", result?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		}catch(BusinessException e){
			Map<String,Object> map = new HashMap<>();
			map.put("status", "fail");
			map.put("message", e.getMsg());
			return map;
		}
	}
}
