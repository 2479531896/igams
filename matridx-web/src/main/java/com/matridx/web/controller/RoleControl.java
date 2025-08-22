package com.matridx.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.web.dao.entities.XtjsDto;
import com.matridx.web.dao.entities.XtjsModel;
import com.matridx.web.dao.entities.XtyhDto;
import com.matridx.web.dao.entities.XtzyDto;
import com.matridx.web.dao.entities.YhjsDto;
import com.matridx.web.service.svcinterface.IXtjsService;
import com.matridx.web.service.svcinterface.IXtyhService;
import com.matridx.web.service.svcinterface.IXtzyService;
import com.matridx.web.service.svcinterface.IYhjsService;

@Controller
@RequestMapping("/systemrole")
public class RoleControl extends BaseController{
	
	@Autowired
	IXtyhService xtyhService;
	
	@Autowired
	IYhjsService yhjsService;
	
	@Autowired
	IXtjsService xtjsService;
	
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	IJcsjService jcsjService;
	
	@Autowired
	IXtzyService xtzyService;
	
	/**
	 * 权限列表页面
	 * @return
	 */
	@RequestMapping(value ="/role/pageListRole")
	public ModelAndView pageListRole(){

        return new ModelAndView("globalweb/systemrole/role_list");
	}

	@RequestMapping(value ="/role/page_ListRole_vue")
	public Map<String,Object> pageListRole_vue(HttpServletRequest request){
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try {
			User user = getLoginInfo();

			List<QxModel> qxDtos = user.getQxModels();

			List<QxModel> now_jsczDtos = new ArrayList<>();
			for(int i=0;i<qxDtos.size();i++){
				QxModel qxModel = qxDtos.get(i);
				if(qxModel.getZyid().equals(request.getParameter("zyid"))){
					now_jsczDtos.add(qxModel);
				}
			}
			resMap.put("czdmlist", now_jsczDtos);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}


		return resMap;
	}
	
	/**
	 * 用户列表
	 * @return
	 */
	@RequestMapping(value ="/role/listRole")
	@ResponseBody
	public Map<String,Object> listRole(XtjsDto xtjsDto){
		List<XtjsDto> t_List = xtjsService.getPagedDtoList(xtjsDto);
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", xtjsDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}

	@RequestMapping(value="/role/viewRole")
	public ModelAndView viewRole(XtjsDto xtjsDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/role_view");
		XtjsDto t_xtjsDto = xtjsService.getDtoById(xtjsDto.getJsid());
		mav.addObject("xtjsDto", t_xtjsDto);
		
		return mav;
	}

	@RequestMapping(value="/role/viewRole_vue")
	@ResponseBody
	public Map<String,Object> viewRole_vue(XtjsDto xtjsDto){
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try{
			XtjsDto t_xtjsDto = xtjsService.getDtoById(xtjsDto.getJsid());
			resMap.put("xtjsDto", t_xtjsDto);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}
		return resMap;
	}

	/**
	 * 添加角色
	 * @return
	 */
	@RequestMapping(value="/role/addRole")
	public ModelAndView addRole(){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/role_edit");
		
		XtjsDto t_xtjsDto = new XtjsDto();
		t_xtjsDto.setFormAction("add");
		mav.addObject("xtjsDto", t_xtjsDto);
		
		//获取父角色列表
		List<XtjsModel> xtjsModels = xtjsService.getModelListExceptSelf(null);
		mav.addObject("jslist", xtjsModels);
		//获取首页类型列表
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.HOME_PAGE_TYPE});
		mav.addObject("sylxlist", jclist.get(BasicDataTypeEnum.HOME_PAGE_TYPE.getCode()));
		
		return mav;
	}

	/**
	 * 添加角色 vue
	 * @return
	 */
	@RequestMapping(value="/role/addRole_vue")
	@ResponseBody
	public Map<String,Object> addRole_vue(){
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try{
			XtjsDto t_xtjsDto = new XtjsDto();
			t_xtjsDto.setFormAction("add");
			resMap.put("xtjsDto", t_xtjsDto);
			//获取父角色列表
			List<XtjsModel> xtjsModels = xtjsService.getModelListExceptSelf(null);
			resMap.put("jslist", xtjsModels);
			//获取首页类型列表
			Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.HOME_PAGE_TYPE});
			resMap.put("sylxlist", jclist.get(BasicDataTypeEnum.HOME_PAGE_TYPE.getCode()));
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}
		return resMap;
	}

	/**
	 * 添加角色
	 * @param xtjsDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/role/addSaveRole")
	@ResponseBody
	public Map<String, Object> addSaveRole(XtjsDto xtjsDto,HttpServletRequest request){
		User user = getLoginInfo();
		xtjsDto.setLrry(user.getYhid());
		if (StringUtil.isBlank(xtjsDto.getDwxdbj())){
			xtjsDto.setDwxdbj("0");
		}
		boolean isSuccess = xtjsService.insert(xtjsDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	@RequestMapping(value = "/role/modRole")
	public ModelAndView modRole(XtjsDto xtjsDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/role_edit");
		XtjsDto t_xtjsDto = xtjsService.getDto(xtjsDto);
		t_xtjsDto.setFormAction("mod");
		mav.addObject("xtjsDto", t_xtjsDto);
		//获取父角色列表
		List<XtjsModel> xtjsModels = xtjsService.getModelListExceptSelf(xtjsDto);
		mav.addObject("jslist", xtjsModels);

		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.HOME_PAGE_TYPE});
		mav.addObject("sylxlist", jclist.get(BasicDataTypeEnum.HOME_PAGE_TYPE.getCode()));

		return mav;
	}

	@RequestMapping(value = "/role/modRole_vue")
	@ResponseBody
	public Map<String,Object> modRole_vue(XtjsDto xtjsDto){
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try{
			XtjsDto t_xtjsDto = xtjsService.getDto(xtjsDto);
			t_xtjsDto.setFormAction("mod");
			resMap.put("xtjsDto", t_xtjsDto);
			//获取父角色列表
			List<XtjsModel> xtjsModels = xtjsService.getModelListExceptSelf(xtjsDto);
			resMap.put("jslist", xtjsModels);

			Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.HOME_PAGE_TYPE});
			resMap.put("sylxlist", jclist.get(BasicDataTypeEnum.HOME_PAGE_TYPE.getCode()));
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}
		return resMap;
	}
	@RequestMapping(value = "/role/modSaveRole")
	@ResponseBody
	public Map<String, Object> modSaveRole(XtjsDto xtjsDto){
		User user = getLoginInfo();
		xtjsDto.setXgry(user.getYhid());
		
		if(StringUtil.isBlank(xtjsDto.getDwxdbj())) {
			xtjsDto.setDwxdbj("0");
		}
		boolean isSuccess = xtjsService.update(xtjsDto);
		
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	@RequestMapping(value = "/role/delRole")
	@ResponseBody
	public Map<String, Object> delRole(XtjsDto xtjsDto){
		User user = getLoginInfo();
		xtjsDto.setScry(user.getYhid());
		boolean isSuccess = xtjsService.delete(xtjsDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	@RequestMapping(value = "/role/setMenu")
	public ModelAndView setMenu(XtjsDto xtjsDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/role_configmenu");
		mav.addObject("xtjsDto", xtjsDto);
		return mav;
	}

	/**
	 * 设置菜单界面 vue
	 * @param xtjsDto
	 * @return
	 */
	@RequestMapping(value = "/role/setMenu_vue")
	@ResponseBody
	public Map<String,Object> setMenu_vue(XtjsDto xtjsDto){
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try{
			resMap.put("xtjsDto", xtjsDto);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}
		return resMap;
	}

	/**
	 * 菜单树
	 * @param xtjsDto
	 * @param rsp
	 * @return
	 */
	@RequestMapping(value = "/role/menuTree",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String menuTree(XtjsDto xtjsDto,HttpServletResponse rsp){
		String JSONDATA = "";
		List<XtzyDto> xtzyList = xtzyService.getMenuTreeList(xtjsDto.getJsid());
		JSONDATA = xtzyService.installTree(xtzyList,JSONDATA);
		rsp.addHeader("Access-Control-Allow-Origin", "*");
		rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
		return JSONDATA;
	}

	/**
	 * 保存菜单
	 * @param xtzyDto
	 * @param rsp
	 * @return
	 */
	@RequestMapping(value = "/role/menuSaveTree",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> menuSaveTree(XtzyDto xtzyDto,HttpServletResponse rsp){
		try{
			boolean result = xtzyService.saveMenuArray(xtzyDto);
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
	
	@RequestMapping(value = "/role/configuser")
	public ModelAndView configuser(XtjsDto xtjsDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/role_configuser");
		mav.addObject("xtjsDto", xtjsDto);
		return mav;
	}

	/**
	 * 设置用户界面 vue
	 * @param xtjsDto
	 * @return
	 */
	@RequestMapping(value = "/role/configuser_vue")
	@ResponseBody
	public Map<String,Object> configuser_vue(XtjsDto xtjsDto){
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try{
			resMap.put("xtjsDto", xtjsDto);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}
		return resMap;
	}


	
	/**
	 * 可选用户
	 * @return
	 */
	@RequestMapping(value ="/configuser/listUnSelectUser")
	@ResponseBody
	public Map<String,Object> listUnSelectUser(XtyhDto xtyhDto){
		List<XtyhDto> t_List = yhjsService.getPagedOptionalList(xtyhDto);
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", xtyhDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 已选用户
	 * @return
	 */
	@RequestMapping(value ="/configuser/listSelectedUser")
	@ResponseBody
	public Map<String,Object> listSelectedUser(XtyhDto xtyhDto){
		List<XtyhDto> t_List = yhjsService.getPagedSelectedList(xtyhDto);
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", xtyhDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 添加用户
	 * @return
	 */
	@RequestMapping(value ="/configuser/toSelected")
	@ResponseBody
	public Map<String,Object> toSelected(YhjsDto yhjsDto){
		try{
			boolean result = yhjsService.toSelected(yhjsDto);
			Map<String,Object> map = new HashMap<>();
			map.put("status", result?"success":"fail");
			map.put("message", result?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		}catch(Exception e){
			Map<String,Object> map = new HashMap<>();
			map.put("status", "fail");
			map.put("message", e.getMessage());
			return map;
		}
	}
	
	/**
	 * 去除用户
	 * @return
	 */
	@RequestMapping(value ="/configuser/toOptional")
	@ResponseBody
	public Map<String,Object> toOptional(YhjsDto yhjsDto){
		try{
			boolean result = yhjsService.toOptional(yhjsDto);
			Map<String,Object> map = new HashMap<>();
			map.put("status", result?"success":"fail");
			map.put("message", result?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		}catch(Exception e){
			Map<String,Object> map = new HashMap<>();
			map.put("status", "fail");
			map.put("message", e.getMessage());
			return map;
		}
	}
}
