package com.matridx.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.web.dao.entities.YhssjgDto;
import com.matridx.web.service.svcinterface.IYhssjgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.web.dao.entities.XtjsDto;
import com.matridx.web.dao.entities.XtyhDto;
import com.matridx.web.dao.entities.YhjsDto;
import com.matridx.web.service.svcinterface.IXtjsService;
import com.matridx.web.service.svcinterface.IXtyhService;
import com.matridx.web.service.svcinterface.IYhjsService;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/systemrole")
public class UserControl extends BaseController{
	
	@Autowired
	IXtyhService xtyhService;
	
	@Autowired
	IYhjsService yhjsService;
	
	@Autowired
	IXtjsService xtjsService;
	
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	IGrszService grszService;
	@Autowired
	IJgxxService jgxxService;
	@Autowired
	IYhssjgService yhssjgService;
	
	/**
	 * 用户列表页面
	 * @return
	 */
	@RequestMapping(value ="/user/pageListUser")
	public ModelAndView pageListUser(){


        return new ModelAndView("globalweb/systemrole/user_list");
	}

	/**
	 * 用户列表页面_vue
	 * @return
	 */
	@RequestMapping(value ="/user/page_ListUser_vue")
	@ResponseBody
	public Map<String,Object> pageListUser_vue(HttpServletRequest request){
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
	@RequestMapping(value ="/user/listUser")
	@ResponseBody
	public Map<String,Object> listUser(XtyhDto xtyhDto){
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try {
			List<XtyhDto> t_List = xtyhService.getPagedDtoList(xtyhDto);
			//Json格式的要求{total:22,rows:{}}
			//构造成Json的格式传递
			if (StringUtil.isNotBlank(xtyhDto.getCzbs()) && "docker".equals(xtyhDto.getCzbs())){
				YhjsDto yhjsDto = new YhjsDto();
				List<YhjsDto> dtoList = yhjsService.getDtoList(yhjsDto);
				for (XtyhDto xtyhDto1 : t_List) {
					for (YhjsDto dto : dtoList) {
						if (dto.getYhid().equals(xtyhDto1.getYhid())){
							xtyhDto1.setDqjsmc(dto.getJsmc());
							xtyhDto1.setDqjs(dto.getJsid());
						}
					}
				}
			}
			resMap.put("total", xtyhDto.getTotalNumber());
			resMap.put("rows", t_List);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}

		return resMap;
	}


	@RequestMapping(value="/user/viewUser")
	public ModelAndView viewUser(XtyhDto xtyhDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/user_view");
		XtyhDto t_xtyhDto = xtyhService.getDtoById(xtyhDto.getYhid());
		mav.addObject("xtyhDto", t_xtyhDto);

		YhjsDto t_YhjsDto = new YhjsDto();
		t_YhjsDto.setYhid(t_xtyhDto.getYhid());
		List<YhjsDto> yhjsDtos = yhjsService.getDtoList(t_YhjsDto);
		mav.addObject("yhjsDtos", yhjsDtos);

		return mav;
	}

	/**
	 * 查看用户 vue
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping(value="/user/viewUser_vue")
	@ResponseBody
	public Map<String,Object> viewUser_vue(XtyhDto xtyhDto){
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try{
			XtyhDto t_xtyhDto = xtyhService.getDtoById(xtyhDto.getYhid());
			resMap.put("xtyhDto", t_xtyhDto);

			YhjsDto t_YhjsDto = new YhjsDto();
			t_YhjsDto.setYhid(t_xtyhDto.getYhid());
			List<YhjsDto> yhjsDtos = yhjsService.getDtoList(t_YhjsDto);
			resMap.put("yhjsDtos", yhjsDtos);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}
		return resMap;
	}
	
	@RequestMapping(value="/user/addUser")
	public ModelAndView addUser(){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/user_edit");
		List<XtjsDto> xtjsDtos = xtjsService.getAllJsAndChecked(null);
		mav.addObject("xtjsDtos", xtjsDtos);
		List<JgxxDto> jgxxList=jgxxService.getJgxxList();
		mav.addObject("jgxxList", jgxxList);
		XtyhDto t_xtyhDto = new XtyhDto();
		t_xtyhDto.setFormAction("add");
		mav.addObject("xtyhDto", t_xtyhDto);
		
		return mav;
	}

	/**
	 * 添加用户页面
	 * @return
	 */
	@RequestMapping(value="/user/addUser_vue")
	@ResponseBody
	public Map<String,Object> addUser_vue(){
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try{
			List<XtjsDto> xtjsDtos = xtjsService.getAllJsAndChecked(null);
			resMap.put("xtjsDtos", xtjsDtos);
			List<JgxxDto> jgxxList=jgxxService.getJgxxList();
			resMap.put("jgxxList", jgxxList);
			XtyhDto t_xtyhDto = new XtyhDto();
			t_xtyhDto.setFormAction("add");
			resMap.put("xtyhDto", t_xtyhDto);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}
		return resMap;
	}


	/**
	 * 角色信息
	 * @return
	 */
	@RequestMapping(value ="/role/listRoleInfo")
	@ResponseBody
	public Map<String,Object> listRoleInfo(){
		List<XtjsDto> xtjsDtos = xtjsService.getAllJsAndChecked(null);
		Map<String,Object> result = new HashMap<>();
		result.put("xtjsDtos", xtjsDtos);
		return result;
	}

	/**
	 * 获取登录用户信息
	 * @return
	 */
	@RequestMapping(value ="/user/loginInfo")
	@ResponseBody
	public Map<String,Object> loginInfo( ){
		User user = getLoginInfo();
		Map<String,Object> result = new HashMap<>();
		result.put("yhid", user.getYhid());
		result.put("status","success");
		return result;
	}



	/**
	 * 用户信息
	 * @return
	 */
	@RequestMapping(value ="/user/userInfo")
	@ResponseBody
	public Map<String,Object> userInfo(XtyhDto xtyhDto){
		XtyhDto t_xtyhDto = xtyhService.getDto(xtyhDto);
		Map<String,Object> result = new HashMap<>();
		List<XtjsDto> xtjsDtos = xtjsService.getAllJsAndChecked(t_xtyhDto.getYhid());
		YhjsDto yhjsDto = new YhjsDto();
		yhjsDto.setYhid(t_xtyhDto.getYhid());
		List<YhjsDto> dtoList = yhjsService.getDtoList(yhjsDto);
		t_xtyhDto.setDqjs(dtoList.get(0).getJsid());
		result.put("user", t_xtyhDto);
		result.put("xtjsDtos", xtjsDtos);
		return result;
	}
	
	@RequestMapping(value = "/user/addSaveUser")
	@ResponseBody
	public Map<String, Object> addSaveUser(XtyhDto xtyhDto){
		User user = getLoginInfo();
		xtyhDto.setLrry(user.getYhid());
		Map<String,Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(xtyhDto.getYhm())){
			XtyhDto dtoByName = xtyhService.getDtoByName(xtyhDto.getYhm());
			if (dtoByName != null ){
				map.put("status", "fail");
				map.put("message", "用户名已存在！");
				return map;
			}
		}
		//如果没有设置则为不锁定
		if(StringUtil.isBlank(xtyhDto.getSfsd())) {
            xtyhDto.setSfsd("0");
        }
		boolean isSuccess = xtyhService.insertDto(xtyhDto);

		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	@RequestMapping(value = "/user/modUser")
	public ModelAndView modUser(XtyhDto xtyhDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/user_edit");
		XtyhDto t_xtyhDto = xtyhService.getDto(xtyhDto);
		t_xtyhDto.setFormAction("mod");
		//查询到当前用户id的机构
		YhssjgDto yhssjgDto=yhssjgService.getDtoById(xtyhDto.getYhid());
		if(yhssjgDto!=null) {
			t_xtyhDto.setJgid(yhssjgDto.getJgid()); }
		List<JgxxDto> jgxxList=jgxxService.getJgxxList();
		mav.addObject("jgxxList", jgxxList);
		mav.addObject("xtyhDto", t_xtyhDto);

		List<XtjsDto> xtjsDtos = xtjsService.getAllJsAndChecked(t_xtyhDto.getYhid());
		mav.addObject("xtjsDtos", xtjsDtos);
		return mav;
	}

	/**
	 * 修改用户页面 vue
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping(value = "/user/modUser_vue")
	@ResponseBody
	public Map<String,Object> modUser_vue(XtyhDto xtyhDto){
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try{
			XtyhDto t_xtyhDto = xtyhService.getDto(xtyhDto);
			t_xtyhDto.setFormAction("mod");
			resMap.put("xtyhDto", t_xtyhDto);

			List<XtjsDto> xtjsDtos = xtjsService.getAllJsAndChecked(t_xtyhDto.getYhid());
			resMap.put("xtjsDtos", xtjsDtos);
			List<JgxxDto> jgxxList=jgxxService.getJgxxList();
			resMap.put("jgxxList", jgxxList);
		}catch (Exception e) {
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}

		return resMap;
	}

	/**
	 * 修改用户信息
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping(value = "/user/modSaveUser")
	@ResponseBody
	public Map<String, Object> modSaveUser(XtyhDto xtyhDto){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		xtyhDto.setXgry(user.getYhid());
		//如果没有设置则为不锁定
		if(StringUtil.isBlank(xtyhDto.getSfsd())) {
            xtyhDto.setSfsd("0");
        }
		XtyhDto xtyhDto_t = xtyhService.getDtoByYhm(xtyhDto);
		if(xtyhDto_t==null) {
			boolean isSuccess = xtyhService.updateYhxx(xtyhDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}else {
			map.put("status", "fail");
			map.put("message",xxglService.getModelById("ICOMM_MM00008").getXxnr());
		}
		return map;
	}

	/**
	 * 删除用户
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping(value = "/user/delUser")
	@ResponseBody
	public Map<String, Object> delUser(XtyhDto xtyhDto){
		User user = getLoginInfo();
		xtyhDto.setScry(user.getYhid());
		boolean isSuccess = xtyhService.delete(xtyhDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	@RequestMapping(value = "/user/pagedataModPass")
	public ModelAndView modPass(XtyhDto xtyhDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/pass_edit");
		mav.addObject("xtyhDto",xtyhDto);
		return mav;
	}


	/**
	 * 修改密码
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping(value = "/user/pagedataModSavePass")
	@ResponseBody
	public Map<String, Object> modSavePass(XtyhDto xtyhDto){
		boolean isSuccess;
		try{
			User user = getLoginInfo();
			xtyhDto.setXgry(user.getYhid());
			isSuccess = xtyhService.updatePass(xtyhDto);
		}catch(BusinessException e){
			Map<String,Object> map = new HashMap<>();
			map.put("status", "fail");
			map.put("message", xxglService.getModelById(e.getMsgId()).getXxnr());
			return map;
		}
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 初始化密码
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping(value = "/user/initpassWord")
	@ResponseBody
	public Map<String, Object> initpassWord(XtyhDto xtyhDto){
		User user = getLoginInfo();
		xtyhDto.setXgry(user.getYhid());
		boolean isSuccess = xtyhService.initPass(xtyhDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 更新钉钉信息
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping(value = "/user/initddUser")
	@ResponseBody
	public Map<String, Object> initddUser(XtyhDto xtyhDto){
		User user = getLoginInfo();
		xtyhDto.setXgry(user.getYhid());
		boolean isSuccess = xtyhService.initDingDing(xtyhDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 更新任务确认人员页面
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping(value = "/user/pagedataModSettingConfirmer")
	public ModelAndView modSettingConfirmer(XtyhDto xtyhDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/settingConfirmer_edit");
		User user = getLoginInfo();
		xtyhDto.setYhid(user.getYhid());
		mav.addObject("xtyhDto", xtyhDto);
		return mav;
	}

	/**
	 * 更新任务确认人员页面 vue
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping(value = "/user/modSettingConfirmer_vue")
	@ResponseBody
	public Map<String,Object> modSettingConfirmer_vue(XtyhDto xtyhDto){
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try{
			User user = getLoginInfo();
			xtyhDto.setYhid(user.getYhid());
			resMap.put("xtyhDto", xtyhDto);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}

		return resMap;
	}
	
	/**
	 * 更新任务确认人员保存
	 * @param grszDto
	 * @return
	 */
	@RequestMapping(value = "/user/pagedataModSaveSettingConfirmer")
	@ResponseBody
	public Map<String, Object> modSaveSettingConfirmer(GrszDto grszDto){
		boolean isSuccess = grszService.modSaveSettingConfirmer(grszDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 查看个人信息页面
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping("/user/pagedataModPersonalData")
	public ModelAndView getPersonalData(XtyhDto xtyhDto) {
		ModelAndView mav=new ModelAndView("globalweb/systemrole/user_information");
		XtyhDto xtyhDto_t=xtyhService.getPersonalData(xtyhDto);
		List<XtyhDto> jgmclist = xtyhService.getJgmc();
		mav.addObject("xtyhDto", xtyhDto_t);
		mav.addObject("jgmclist",jgmclist);
		return mav;
	}

	/**
	 * 查看个人信息页面 vue
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping("/user/modPersonalData_vue")
	@ResponseBody
	public Map<String,Object> modPersonalData_vue(XtyhDto xtyhDto) {
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try {
			XtyhDto xtyhDto_t=xtyhService.getPersonalData(xtyhDto);
			List<XtyhDto> jgmclist = xtyhService.getJgmc();
			resMap.put("xtyhDto", xtyhDto_t);
			resMap.put("jgmclist",jgmclist);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}

		return resMap;
	}

	@RequestMapping(value = "/user/pagedataModUpdateData")
	@ResponseBody
	public Map<String, Object> modUpdateData(XtyhDto xtyhDto){
		boolean isSuccess = xtyhService.updateJg(xtyhDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
