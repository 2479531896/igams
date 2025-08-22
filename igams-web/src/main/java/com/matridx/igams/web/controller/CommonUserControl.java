package com.matridx.igams.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.DepartmentDto;
import com.matridx.igams.common.dao.entities.Role;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.web.dao.entities.XtjsDto;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.dao.entities.YhzDto;
import com.matridx.igams.web.dao.entities.YhzcyDto;
import com.matridx.igams.web.service.svcinterface.IXtjsService;
import com.matridx.igams.web.service.svcinterface.IXtyhService;
import com.matridx.igams.web.service.svcinterface.IYhzService;
import com.matridx.igams.web.service.svcinterface.IYhzcyService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/systemrole")
public class CommonUserControl extends BaseController{
	
	@Autowired
	ICommonService commonService;
	@Autowired
	ICommonDao commonDao;
	@Autowired
	IYhzService yhzService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IXtyhService xtyhService;
	@Autowired
	IYhzcyService yhzcyService;
	@Autowired
	IXtjsService xtjsService;
	
	private final Logger log = LoggerFactory.getLogger(CommonUserControl.class);
	
	/**
	 * 根据用户名模糊查询
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/common/getUserListByYhm")
	@ResponseBody
	public Map<String,Object> getUserListByYhm(HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		String yhm = request.getParameter("yhm");
		if(StringUtil.isBlank(yhm)){
			result.put("status", "fail");
			log.error("userNameList 未获取到用户名信息！");
			return result;
		}
		User user = new User();
		user.setYhm(yhm);
		List<User> users = commonDao.getListByYhm(user);
		result.put("users", users);
		return result;
	}
	
	/**
	 * 根据用户Ids查询用户信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/common/pagedataUserListByIds")
	@ResponseBody
	public Map<String,Object> getUserListByIds(HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		String str = request.getParameter("ids");
		if(StringUtil.isBlank(str)){
			result.put("status", "fail");
			log.error("userIdsList 未获取到用户Ids信息！");
			return result;
		}
		String[] split = str.split(",");
		List<String> ids = Arrays.asList(split);
		User user = new User();
		user.setIds(ids);
		List<User> users = commonDao.getListByIds(user);
		result.put("users", users);
		return result;
	}

	
	/**
	 * 获取用户列表
	 * @return
	 */
	@RequestMapping(value ="/common/getUserList")
	@ResponseBody
	public Map<String,Object> getUserList(){
		Map<String,Object> result = new HashMap<>();
		
		List<User> users = commonDao.getUserList();
		result.put("users", users);
		return result;
	}
	
	/**
	 * 获取机构列表
	 * @return
	 */
	@RequestMapping(value ="/common/getDepartmentList")
	@ResponseBody
	public Map<String,Object> getDepartmentList(){
		Map<String,Object> result = new HashMap<>();
		List<DepartmentDto> departmentDtos = commonDao.getJgxxList();
		result.put("departmentDtos", departmentDtos);
		return result;
	}
	
	/**
	 * 获取角色列表
	 * @return
	 */
	@RequestMapping(value ="/common/getRoleList")
	@ResponseBody
	public Map<String,Object> getRoleList(){
		Map<String,Object> result = new HashMap<>();
		List<Role> roles = commonDao.getRoleList();
		result.put("roles", roles);
		return result;
	}

	/**
	 * 根据用户ID获取机构信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/common/getDepartmentByUser")
	@ResponseBody
	public Map<String,Object> getDepartmentByUser(HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		String yhid = request.getParameter("yhid");
		if(StringUtil.isBlank(yhid)){
			result.put("status", "fail");
			log.error("userIdsList 未获取到用户Id信息！");
			return result;
		}
		User user = new User();
		user.setYhid(yhid);
		User t_user = commonDao.getUserInfoById(user);
		result.put("user", t_user);
		return result;
	}
	
	/**
	 * 根据ID获取机构信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/common/getDepartmentById")
	@ResponseBody
	public Map<String,Object> getDepartmentById(HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		String jgid = request.getParameter("jgid");
		DepartmentDto departmentDto = new DepartmentDto();
		departmentDto.setJgid(jgid);
		List<DepartmentDto> departmentDtos = commonDao.getJgxxById(departmentDto);
		result.put("departmentDtos", departmentDtos);
		return result;
	}
	
	/**
	 * 根据机构Ids获取机构列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/common/getDepartmentListByIds")
	@ResponseBody
	public Map<String,Object> getDepartmentListByIds(HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		String str = request.getParameter("ids");
		String[] split = str.split(",");
		List<String> ids = Arrays.asList(split);
		DepartmentDto departmentDto = new DepartmentDto();
		departmentDto.setIds(ids);
		List<DepartmentDto> departmentDtos = commonDao.getJgListByIds(departmentDto);
		result.put("departmentDtos", departmentDtos);
		return result;
	}
	
	/**
	 * 根据departmentDtos获取信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/common/getDepartmentListByDtos")
	@ResponseBody
	public Map<String,Object> getDepartmentListByDtos(HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		String s_jsonObject = request.getParameter("departmentDtos");
		JSONArray jsonObject = JSONObject.parseArray(s_jsonObject);
		@SuppressWarnings("unchecked")
		List<DepartmentDto> departmentDtos = JSONObject.toJavaObject(jsonObject, List.class);
		List<Map<String, String>> listMap = commonDao.getDepartmentListByDtos(departmentDtos);
		result.put("listMap", listMap);
		return result;
	}
	
	/**
	 * 根据users获取信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/common/getUserListByDtos")
	@ResponseBody
	public Map<String,Object> getUserListByDtos(HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		String s_jsonObject = request.getParameter("users");
		JSONArray jsonObject = JSONObject.parseArray(s_jsonObject);
		@SuppressWarnings("unchecked")
		List<User> users = JSONObject.toJavaObject(jsonObject, List.class);
		List<Map<String, String>> listMap = commonDao.getUserListByDtos(users);
		result.put("listMap", listMap);
		return result;
	}
	
	/**
	 * 根据ID获取机构信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/common/setSqrxm")
	@ResponseBody
	public Map<String,Object> setSqrxm(HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		String s_jsonObject = request.getParameter("list");
		JSONArray jsonObject = JSONObject.parseArray(s_jsonObject);
		List<?> list = JSONObject.toJavaObject(jsonObject, List.class);
		commonService.setSqrxmByJson(list);
		result.put("list", list);
		return result;
	}

	
	/**
	 * 查询机构列表
	 * @param request 
	 * @return
	 */
	@RequestMapping(value ="/common/getPagedDepartmentList")
	@ResponseBody
	public Map<String,Object> getPagedDepartmentList(HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		String s_jsonObject = request.getParameter("departmentDto");
		JSONArray jsonObject = JSONObject.parseArray(s_jsonObject);
		DepartmentDto departmentDto = JSONObject.toJavaObject(jsonObject, DepartmentDto.class);
		List<DepartmentDto> departmentDtos = commonDao.getPagedDepartmentList(departmentDto);
		result.put("departmentDtos", departmentDtos);
		return result;
	}
	
	/**
	 * 根据角色ID获取用户信息(文件)
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/common/selectAddXtyhByJsid")
	@ResponseBody
	public Map<String,Object> selectAddXtyhByJsid(HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		String s_jsonObject = request.getParameter("jsMap");
		JSONObject jsonObject = JSONObject.parseObject(s_jsonObject);
		@SuppressWarnings("unchecked")
		Map<String,Object> jsMap = JSONObject.toJavaObject(jsonObject, Map.class);
		List<User> users = commonDao.selectAddXtyhByJsid(jsMap);
		result.put("users", users);
		return result;
	}

	//================================增加用户组列表=====================================
	@RequestMapping(value = "/user/pageListUserGroup")
	public ModelAndView pageListUserGroup() {
        return new ModelAndView("systemmain/userGroup/userGroup_list");
	}

	@RequestMapping("/user/pageGetListUserGroup")
	@ResponseBody
	public Map<String, Object> pageDataUserGroup(YhzDto yhzDto){
		Map<String,Object> map= new HashMap<>();
		User user = getLoginInfo();
		yhzDto.setYhid(user.getYhid());
		//查找当前用户私有的用户组和改用户创建的用户组
		//TODO 这里还应该增加一个创建者的组要显示出来
		List<String> priYhzidlist = yhzService.getPrivateYhzList(yhzDto);
		yhzDto.setIds(priYhzidlist);
		List<YhzDto> yhzList = yhzService.getPagedDtoList(yhzDto);

		map.put("total",yhzDto.getTotalNumber());
		map.put("rows",yhzList);
		return map;
	}

	@RequestMapping(value = "/user/addUserGroup")
	public ModelAndView addUserGroup(YhzDto yhzDto) {
		ModelAndView mav = new ModelAndView("systemmain/userGroup/userGroup_add");
		yhzDto.setFormAction("insertUserGroup");
		mav.addObject("yhzDto", yhzDto);
		return mav;
	}

	@RequestMapping("/user/insertUserGroup")
	@ResponseBody
	public Map<String, Object> addUserGroupData(YhzDto yhzDto){
		Map<String,Object> map= new HashMap<>();
		User user = getLoginInfo();
		yhzDto.setLrry(user.getYhid());
		yhzDto.setYhzid(StringUtil.generateUUID());
		boolean isSuccess=yhzService.insert(yhzDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 修改时新增一个页面
	 * @param
	 * @return
	 */
	@RequestMapping("/user/modUserGroup")
	@ResponseBody
	public ModelAndView modUserGroup(YhzDto yhzDto){
		ModelAndView mav=new ModelAndView("systemmain/userGroup/userGroup_add");
		yhzDto = yhzService.getDto(yhzDto);
		yhzDto.setFormAction("modSaveUserGroup");
		mav.addObject("yhzDto", yhzDto);
		return mav;
	}

	/**
	 * 修改消息到数据库
	 */
	@RequestMapping("/user/modSaveUserGroup")
	@ResponseBody
	public Map<String,Object> modSaveUserGroup(YhzDto yhzDto){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		yhzDto.setXgry(user.getYhid());
		if(user.getYhid().equals(yhzDto.getLrry())){
			//只有创建者才可以更改信息（创建者即为组的录入人员）
			boolean isSuccess=yhzService.updateDto(yhzDto);
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}else {
			//修改用户不是创建者时
			map.put("status","fail");
			map.put("message","非创建者不可修改");
		}
		return map;
	}

	/**
	 * 删除数据
	 */
	@RequestMapping("/user/delUserGroup")
	@ResponseBody
	public Map<String,Object> delUserGroup(YhzDto yhzDto){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		yhzDto.setScry(user.getYhid());
		yhzcyService.deleteByIds(yhzDto.getIds());//删除用户组成员
		boolean isSuccess = yhzService.delete(yhzDto);//删除用户组
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 查看审核提醒详情
	 */
	@RequestMapping("/user/viewUserGroup")
	@ResponseBody
	public ModelAndView viewUserGroup(YhzDto yhzDto){
		ModelAndView mav=new ModelAndView("systemmain/userGroup/userGroup_view");
		List<YhzDto> yhcylist = yhzService.getYhzxx(yhzDto);
		StringBuilder yhxms = new StringBuilder();
		if(!CollectionUtils.isEmpty(yhcylist)){
			for (YhzDto yhzdto : yhcylist) {
				if (StringUtil.isBlank(yhzdto.getXm())){
					yhzdto.setXm("");
				}
				yhxms.append(",").append(yhzdto.getXm());
			}
			yhxms = new StringBuilder(yhxms.substring(1));
		}
		yhzDto = yhzService.getDto(yhzDto);
		yhzDto.setYhxms(yhxms.toString());
		mav.addObject("yhzDto", yhzDto);
		return mav;
	}
//=============================用户组成员================================
	@RequestMapping(value = "/user/pagedataConfiguser")
	public ModelAndView configuser(YhzcyDto yhzcyDto){
		ModelAndView mav = new ModelAndView("systemmain/userGroup/userGroup_configuser");
		mav.addObject("yhzcyDto", yhzcyDto);
		return mav;
	}

	/**
	 * 用户组中增加成员
	 */
	@RequestMapping("/user/addUserToGroup")
	@ResponseBody
	public Map<String,Object> addUserToGroup(YhzcyDto yhzcyDto){
		Map<String,Object> map = new HashMap<>();
		map.put("yhzcyDto", yhzcyDto);
		return map;
	}

	/**
	 * 用户组可选用户
	 * @return
	 */
	@RequestMapping(value ="/user/pageGetListUnSelectUser")
	@ResponseBody
	public Map<String,Object> listUnSelectUser(XtyhDto xtyhDto){
		List<XtyhDto> t_List = xtyhService.getPagedYhzOptionalList(xtyhDto);
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", xtyhDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}

	/**
	 * 用户组已选用户
	 * @return
	 */
	@RequestMapping(value ="/user/pageGetListSelectedUser")
	@ResponseBody
	public Map<String,Object> listSelectedUser(XtyhDto xtyhDto){
		List<XtyhDto> t_List = xtyhService.getPagedYhzSelectedList(xtyhDto);
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
	@RequestMapping(value ="/user/toYhzcySelected")
	@ResponseBody
	public Map<String,Object> toYhzcySelected(YhzcyDto yhzcyDto){
		try{
			boolean result = yhzcyService.toYhzcySelected(yhzcyDto);
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
	@RequestMapping(value ="/user/toYhzcyOptional")
	@ResponseBody
	public Map<String,Object> toYhzcyOptional(YhzcyDto yhzcyDto){
		try{
			boolean result = yhzcyService.toYhzcyOptional(yhzcyDto);
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

	@RequestMapping(value = "/user/pagedataSetCreater")
	public ModelAndView setCreater(YhzDto yhzDto){
		ModelAndView mav = new ModelAndView("systemmain/userGroup/userGroup_setCreater");
		List<User> xtyhlist=commonDao.getUserList();
		yhzDto = yhzService.getDto(yhzDto);
		mav.addObject("xtyhlist", xtyhlist);
		mav.addObject("yhzDto", yhzDto);
		return mav;
	}

	@RequestMapping(value ="/user/pagedataModCreater")
	@ResponseBody
	public Map<String,Object> modCreater(YhzDto yhzDto){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess=yhzService.updateDto(yhzDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 打开添加组用户页面之前进行判读选中用户组的lrry和当前用户是否一致，只有创建者才能添加用户组成员
	 */
	@RequestMapping(value ="/user/pagedataCheckLrry")
	@ResponseBody
	public Map<String,Object> checkLrry(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		String lrry = request.getParameter("lrry");
		//String yhzid = request.getParameter("yhzid");
		User user = getLoginInfo();
		if (user.getYhid().equals(lrry)){
			map.put("status",true);
			map.put("message","");
		}else{
			map.put("status",false);
			map.put("message","非创建者不可修改");
		}
		return map;
	}
	/**
	 * 查询系统角色
	 *
	 * @return
	 */
	@RequestMapping("/user/pagedataGetXtjs")
	@ResponseBody
	public Map<String,Object> pagedataGetXtjs(XtjsDto xtjsDto) {
		Map<String,Object> map = new HashMap<>();
		List<XtjsDto> xtjslist = xtjsService.getDtoList(xtjsDto);
		map.put("xtjslist",xtjslist);
		return map;
	}
}
