package com.matridx.igams.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.HabitsTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.IJsxtqxService;
import com.matridx.igams.common.service.svcinterface.ILbzdszService;
import com.matridx.igams.common.service.svcinterface.ISpgwcyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.storehouse.dao.entities.CkhwxxDto;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.web.dao.entities.JsdwqxDto;
import com.matridx.igams.web.dao.entities.JsgnqxDto;
import com.matridx.igams.web.dao.entities.JsjcdwDto;
import com.matridx.igams.web.dao.entities.JszyczbDto;
import com.matridx.igams.web.dao.entities.WbzyDto;
import com.matridx.igams.web.dao.entities.XtjsDto;
import com.matridx.igams.web.dao.entities.XtjsModel;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.dao.entities.XtzyDto;
import com.matridx.igams.web.dao.entities.YhjgqxDto;
import com.matridx.igams.web.dao.entities.YhjsDto;
import com.matridx.igams.web.service.svcinterface.IJsdwqxService;
import com.matridx.igams.web.service.svcinterface.IJsgnqxService;
import com.matridx.igams.web.service.svcinterface.IJsjcdwService;
import com.matridx.igams.web.service.svcinterface.IJszyczbService;
import com.matridx.igams.web.service.svcinterface.IWbzyService;
import com.matridx.igams.web.service.svcinterface.IXtjsService;
import com.matridx.igams.web.service.svcinterface.IXtyhService;
import com.matridx.igams.web.service.svcinterface.IXtzyService;
import com.matridx.igams.web.service.svcinterface.IYhjgqxService;
import com.matridx.igams.web.service.svcinterface.IYhjsService;
import com.matridx.igams.wechat.dao.entities.PptglDto;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
@RequestMapping("/systemrole")
public class RoleControl extends BaseController{

	@Autowired
	IYhjsService yhjsService;
	
	@Autowired
	IXtyhService xtyhService;
	
	@Autowired
	IXtjsService xtjsService;
	
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	IJcsjService jcsjService;
	
	@Autowired
	IXtzyService xtzyService;
	
	@Autowired
	IWbzyService wbzyService;
	
	@Autowired
	IJgxxService jgxxService;
	
	@Autowired
	IJsdwqxService jsdwqxService;
	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	
	@Autowired
	IJsgnqxService jsgnqxService;

	@Autowired
	ICkhwxxService ckhwxxService;
	
	@Autowired
	private ILbzdszService lbzdszService;
	
	@Autowired
	IJsjcdwService jsjcdwService;
	
	@Autowired
	IJsxtqxService jsxtqxService;

	@Autowired
	IYhjgqxService yhjgqxService;
	
	@Autowired
	ISpgwcyService spgwcyService;
	@Autowired
	IJszyczbService jszyczbService;
	@Autowired
	RedisUtil redisUtil;

	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;

	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	/**
	 * 权限列表页面
	 * @return
	 */
	@RequestMapping(value ="/role/pageListRole")
	public ModelAndView pageListRole(){

        return new ModelAndView("globalweb/systemrole/role_list");
	}

	/**
	 * 用户列表
	 * @return
	 */
	@RequestMapping(value ="/role/pageGetListRole")
	@ResponseBody
	public Map<String,Object> pageGetListRole(XtjsDto xtjsDto){
		List<XtjsDto> t_List = xtjsService.getPagedDtoList(xtjsDto);
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", xtjsDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}

	/**
	 * 角色列表公用
	 * @return
	 */
	@RequestMapping(value ="/common/commonGetListRole")
	@ResponseBody
	public Map<String,Object> commonGetListRole(XtjsDto xtjsDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		xtjsDto.setYhid(user.getYhid());
		xtjsDto.setQf(HabitsTypeEnum.ROLE_HABITS.getCode());
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
		YhjsDto t_YhjsDto = new YhjsDto();
		t_YhjsDto.setJsid(xtjsDto.getJsid());
		List<YhjsDto> yhjsDtos = xtjsService.getAllJgxxByxtjs(t_YhjsDto);
		mav.addObject("yhjsDtos", yhjsDtos); 
		mav.addObject("xtjsDto", t_xtjsDto);
		return mav;
	}

	/**
	 * 新增仓库分类设置
	 * @param
	 * @return
	 */
	@RequestMapping("/role/storehousesetCkfl")
	public ModelAndView addStorehouseSet(XtjsDto xtjsDto) {
		ModelAndView mav=new ModelAndView("globalweb/systemrole/role_storehouseSet");
		//获取仓库信息
		List<JcsjDto> jcsjDtos = jcsjService.getDtoListBylb(BasicDataTypeEnum.CK_PERMISSIONS_TYPE.getCode());
//		CkxxDto ckxxDto = new CkxxDto();
		List<String> strlist= new ArrayList<>();
		CkhwxxDto ckhwxxDto = ckhwxxService.getJsInfo(xtjsDto.getJsid());
		if (null != ckhwxxDto){
			if (StringUtil.isNotBlank(ckhwxxDto.getCkqx())){
				String[] strs = ckhwxxDto.getCkqx().split(",");
				if(strs!=null && strs.length >0 ) {
                    Collections.addAll(strlist, strs);
				}
			}
		}
		mav.addObject("strlist",strlist);
		mav.addObject("jcsjDtos",jcsjDtos);
		mav.addObject("xtjsDto",xtjsDto);
		return mav;
	}

	/**
	 * 保存仓库分类设置
	 * @param
	 * @return
	 */
	@RequestMapping("/role/storehousesetSaveCkfl")
	@ResponseBody
	public Map<String,Object> saveStorehouseSet(XtjsDto xtjsDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess = xtjsService.updateCkqxByjsid(xtjsDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	@RequestMapping(value="/role/addRole")
	public ModelAndView addRole(XtjsDto xtjsDto){
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
		//获取角色机构权限
		Map<String,List<JsdwqxDto>> permissionMap = jsdwqxService.getPermission(xtjsDto);
		//选中机构
		List<JsdwqxDto> selectList = permissionMap.get("selectList");
		mav.addObject("selectList", selectList);
		//未选中机构
		List<JsdwqxDto> unselectList = permissionMap.get("unselectList");
		mav.addObject("unselectList", unselectList);
		return mav;
	}
	
	@RequestMapping(value = "/role/addSaveRole")
	@ResponseBody
	public Map<String, Object> addSaveRole(XtjsDto xtjsDto,HttpServletRequest request){
		User user = getLoginInfo();
		xtjsDto.setLrry(user.getYhid());
		if (StringUtil.isBlank(xtjsDto.getDwxdbj())){
			xtjsDto.setDwxdbj("0");
		}
		boolean isSuccess = xtjsService.insert(xtjsDto);
		jsdwqxService.insertByJsid(xtjsDto);
		JszyczbDto jszyczbDto=new JszyczbDto();
		jszyczbDto.setJsid(xtjsDto.getJsid());
		jszyczbDto.setCzdm("menupower");
		Object xtsz=redisUtil.hget("matridx_xtsz","default.menu.power");
		if(xtsz!=null) {
			XtszDto xtszDto = JSON.parseObject(String.valueOf(xtsz), XtszDto.class);
			if(StringUtil.isNotBlank(xtszDto.getSzz())){
				jszyczbDto.setZyid(xtszDto.getSzz());
			}
		}else{
			jszyczbDto.setZyid("000101");
		}
		jszyczbService.insert(jszyczbDto);
		amqpTempl.convertAndSend("sys.igams",preRabbitFlg + "sys.igams.xtjs.update",JSONObject.toJSONString(xtjsDto));
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
		//获取角色机构权限
		Map<String,List<JsdwqxDto>> permissionMap = jsdwqxService.getPermission(xtjsDto);
		//选中机构
		List<JsdwqxDto> selectList = permissionMap.get("selectList");
		mav.addObject("selectList", selectList);
		//未选中机构
		List<JsdwqxDto> unselectList = permissionMap.get("unselectList");
		mav.addObject("unselectList", unselectList);
		PptglDto pptglDto=new PptglDto();
		return mav;
	}
	
	@RequestMapping(value = "/role/modSaveRole")
	@ResponseBody
	public Map<String, Object> modSaveRole(XtjsDto xtjsDto){
		User user = getLoginInfo();
		xtjsDto.setXgry(user.getYhid());
		if (StringUtil.isBlank(xtjsDto.getDwxdbj())){
			xtjsDto.setDwxdbj("0");
		}
		boolean isSuccess = xtjsService.updateJsxx(xtjsDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	@RequestMapping(value = "/role/copyRole")
	public ModelAndView copyRole(XtjsDto xtjsDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/role_edit");
		XtjsDto t_xtjsDto = xtjsService.getDto(xtjsDto);
		t_xtjsDto.setFormAction("copy");
		mav.addObject("xtjsDto", t_xtjsDto);
		//获取父角色列表
		List<XtjsModel> xtjsModels = xtjsService.getModelListExceptSelf(xtjsDto);
		mav.addObject("jslist", xtjsModels);

		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.HOME_PAGE_TYPE});
		mav.addObject("sylxlist", jclist.get(BasicDataTypeEnum.HOME_PAGE_TYPE.getCode()));
		//获取角色机构权限
		Map<String,List<JsdwqxDto>> permissionMap = jsdwqxService.getPermission(xtjsDto);
		//选中机构
		List<JsdwqxDto> selectList = permissionMap.get("selectList");
		mav.addObject("selectList", selectList);
		//未选中机构
		List<JsdwqxDto> unselectList = permissionMap.get("unselectList");
		mav.addObject("unselectList", unselectList);
		return mav;
	}

	@RequestMapping(value = "/role/copySaveRole")
	@ResponseBody
	public Map<String, Object> copySaveRole(XtjsDto xtjsDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		xtjsDto.setLrry(user.getYhid());
		if (StringUtil.isBlank(xtjsDto.getDwxdbj())){
			xtjsDto.setDwxdbj("0");
		}
		XtjsDto dto = xtjsService.getDto(xtjsDto);
		if(dto!=null){
			map.put("status", "fail");
			map.put("message", "角色名称重复！");
			return map;
		}
		boolean isSuccess = xtjsService.copySaveRole(xtjsDto);
		amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.xtjs.update",JSONObject.toJSONString(xtjsDto));
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
		if(isSuccess){
			xtjsDto.setPrefix(prefixFlg);
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.xtjs.del",JSONObject.toJSONString(xtjsDto));
			
			SpgwcyDto spgwcyDto = new SpgwcyDto();
			spgwcyDto.setJsidlist(xtjsDto.getIds());
			spgwcyService.delete(spgwcyDto);
		}
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		for (String id : xtjsDto.getIds()) {
			redisUtil.hdel("Users_QxModel",id);//删除redis中角色信息
		}
		return map;
	}
	
	@RequestMapping(value = "/role/pagedataSetMenu")
	public ModelAndView pagedataSetMenu(XtjsDto xtjsDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/role_configmenu");
		//获取账套基础数据
		List<JcsjDto> ztlxlist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.ACCOUNT_TYPE.getCode());
		mav.addObject("ztlxlist", ztlxlist);
		mav.addObject("xtjsDto", xtjsDto);
		return mav;
	}
	
	@RequestMapping(value = "/role/pagedataSetMiniMenu")
	public ModelAndView setMiniMenu(XtjsDto xtjsDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/role_configminimenu");
		mav.addObject("xtjsDto", xtjsDto);
		return mav;
	}
	
	@RequestMapping(value = "/role/pagedataMenuTree",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String menuTree(XtzyDto xtzyDto, HttpServletResponse rsp){
		String JSONDATA = "";
		List<XtzyDto> xtzyList = xtzyService.getMenuTreeList(xtzyDto);
		JSONDATA = xtzyService.installTree(xtzyList,JSONDATA);
		rsp.addHeader("Access-Control-Allow-Origin", "*");
		rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
		return JSONDATA;
	}

	@RequestMapping(value = "/role/pagedataMenuTree_vue",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String menuTree_vue(HttpServletResponse rsp){
		String JSONDATA = "";
		//User user=getLoginInfo();
		//List<XtzyDto> xtzyList = xtzyService.getMenuTreeList(user.getJsid());
		rsp.addHeader("Access-Control-Allow-Origin", "*");
		rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
		return JSONDATA;
	}
	@RequestMapping(value = "/role/pagedataMinimenuTree",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String minimenuTree(XtjsDto xtjsDto,HttpServletResponse rsp){
		List<WbzyDto> wbzyList = wbzyService.getMiniMenuTreeList(xtjsDto.getJsid());
		String JSONDATA = wbzyService.installMiniTree(wbzyList);
		rsp.addHeader("Access-Control-Allow-Origin", "*");
		rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
		return JSONDATA;
	}
	
	//角色列表的设置菜单
	@RequestMapping(value = "/role/pagedataMenuSaveTree",produces = "application/json;charset=UTF-8")
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
	
	//钉钉菜单设置
	@RequestMapping(value = "/role/pagedataMinimenuSaveTree",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public Map<String, Object> minimenuSaveTree(WbzyDto wbzyDto,HttpServletResponse rsp){
		try{
			boolean result = wbzyService.saveMiniMenuArray(wbzyDto);
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
	
	@RequestMapping(value = "/role/pagedataConfiguser")
	public ModelAndView configuser(XtjsDto xtjsDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/role_configuser");
		mav.addObject("xtjsDto", xtjsDto);
		return mav;
	}
	
	/**
	 * 可选用户
	 * @return
	 */
	@RequestMapping(value ="/configuser/pagedataListUnSelectUser")
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
	@RequestMapping(value ="/configuser/pagedataListSelectedUser")
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
	@RequestMapping(value ="/configuser/pagedataToSelected")
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
	@RequestMapping(value ="/configuser/pagedataToOptional")
	@ResponseBody
	public Map<String,Object> toOptional(YhjsDto yhjsDto){
		try{
			boolean result = yhjsService.toOptional(yhjsDto);
			
			if(yhjsDto.getIds()!=null && yhjsDto.getIds().size() > 0) {
				List<String> ids = yhjsDto.getIds();
				for(String id:ids) {
					resetRedisInfo(yhjsDto,id,false);
				}
			}else if(StringUtil.isNotBlank(yhjsDto.getYhid())){
				resetRedisInfo(yhjsDto,yhjsDto.getYhid(),false);
			}
			
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
	 * 根据用户ID重置Redis里的用户角色信息
	 * @param yhjsDto
	 * @param id
	 */
	private void resetRedisInfo(YhjsDto yhjsDto,String id,boolean addflg) {
		XtyhDto yhDto = xtyhService.getDtoById(id);
		
		if(yhDto == null)
			return;
		
		//更新redis里用户信息
        User user = redisUtil.hugetDto("Users", yhDto.getYhm());
        if (user != null) {
        	if(addflg) {
        		//Redis的jsids里不保留任何信息 
        		/*List<String> jsids = user.getJsids();
        		if(jsids==null)
        			jsids = new ArrayList<>();
        		jsids.add(yhjsDto.getJsid());*/
        	}else {
	        	/*List<String> jsids = user.getJsids();
	        	if(jsids != null) {
	            	for(int i=0;i<jsids.size();i++) {
	            		String jsid = jsids.get(i);
	            		if(jsid.equals(yhjsDto.getJsid())) {
	            			jsids.remove(i);
	            			break;
	            		}
	            	}
	                user.setJsids(jsids);
	        	}*/
                if(yhjsDto.getJsid().equals(user.getDqjs())) {
                	user.setDqjs(null);
                }
        	}
            redisUtil.hset("Users", yhDto.getYhm(), JSON.toJSONString(user), -1);
        }
	}
	
	/**
	 * 查询角色列表
	 * @param xtjsDto
	 * @param rsp
	 * @return
	 */
	@RequestMapping(value = "/menu/commonRolesMenuTree",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String rolesMenuTree(HttpServletResponse rsp,XtjsDto xtjsDto){
		String JSONDATA = "";
		List<XtjsDto> xtjsList = xtjsService.getDtoList(xtjsDto);
		JSONDATA = xtjsService.installTree(xtjsList,JSONDATA);
		rsp.addHeader("Access-Control-Allow-Origin", "*");
		rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
		return JSONDATA;
	}
	
	/**
	 * 单位设置页面
	 * @param xtjsDto
	 * @return
	 */
	@RequestMapping(value = "/role/pagedataSetUnit")
	public ModelAndView setUnit(XtjsDto xtjsDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/role_configunit");
		mav.addObject("XtjsDto", xtjsDto);
		return mav;
	}
	
	/**
	 * 获取机构列表
	 * @return
	 */
	@RequestMapping(value="/configunit/pagedataUnSelectJgxx")
	@ResponseBody
	public Map<String,Object> getJgxx(JgxxDto jgxxDto){
		List<JgxxDto> otherjgxx=jgxxService.getPagedOtherJgxxList(jgxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("total", jgxxDto.getTotalNumber());
		map.put("rows", otherjgxx);
		return map;
	}
	
	/**
	 * 获取已选机构列表
	 * @param
	 * @return
	 */
	@RequestMapping(value="/configunit/pagedataSelectJgxx")
	@ResponseBody
	public Map<String,Object> getYxJgxx(JsdwqxDto jsdwqxDto){
		List<JsdwqxDto> otherjgxx=jsdwqxService.getPagedYxJgxxList(jsdwqxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("total", jsdwqxDto.getTotalNumber());
		map.put("rows", otherjgxx);
		return map;
	}
	
	/**
	 * 添加机构
	 * @return
	 */
	@RequestMapping(value ="/configunit/pagedataToSelectedJg")
	@ResponseBody
	public Map<String,Object> toSelectedJg(JsdwqxDto jsdwqxDto){
		try{
			boolean result = jsdwqxService.toSelectedJg(jsdwqxDto);
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
	 * 去除机构
	 * @param jsdwqxDto
	 * @return
	 */
	@RequestMapping(value="/common/toOptionalJg")
	@ResponseBody
	public Map<String,Object> toOptionalJg(JsdwqxDto jsdwqxDto){
		try{
			boolean result = jsdwqxService.toOptionalJg(jsdwqxDto);
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
	 * 通过角色id查询下属人员
	 * @param yhjsDto
	 * @return
	 */
	@RequestMapping("/configunit/pagedataYhidByJsid")
	@ResponseBody
	public Map<String, Object>  getYhidByJsid(YhjsDto yhjsDto){
		Map<String, Object> map= new HashMap<>();
		List<YhjsDto> yhjsList=yhjsService.selectYhidByJsid(yhjsDto);
		if(!CollectionUtils.isEmpty(yhjsList)) {
			for (YhjsDto dto : yhjsList) {
				if (StringUtil.isBlank(dto.getYhid())) {
                    continue;
                }
				YhjgqxDto yhjgqxDto = new YhjgqxDto();
				yhjgqxDto.setYhid(dto.getYhid());
				yhjgqxDto.setJsid(yhjsDto.getJsid());
				List<YhjgqxDto> yhjgqxDtos=yhjgqxService.getListByjsid(yhjgqxDto);
				dto.setDtos(yhjgqxDtos);
			}
			map.put("yhjsList",yhjsList);
		}
		return map;
	}
	
	/**
	 * 角色字段设置
	 * @param xtjsDto
	 * @return
	 */
	@RequestMapping("/configunit/settingField")
	public ModelAndView settingField(XtjsDto xtjsDto) {
		ModelAndView mav=new ModelAndView("globalweb/systemrole/role_setting");
		List<Map<String,Object>> listMap= new ArrayList<>();
		List<JsgnqxDto> list = jsgnqxService.getListById(xtjsDto.getIds().get(0));
		List<String> strlist=lbzdszService.getYwmc();
		if(!CollectionUtils.isEmpty(strlist)) {
            for (String s : strlist) {
                Map<String, Object> map = new HashMap<>();
                List<LbzdszDto> lbzdszList = lbzdszService.getDczd(s);
                if (!CollectionUtils.isEmpty(lbzdszList)) {
                    map.put("ywid", s);
                    map.put("ywmc", lbzdszList.get(0).getYwmc());
                    map.put("zdmsz", lbzdszList);
                    List<String> zds = new ArrayList<>();
                    for (JsgnqxDto jsgnqxDto : list) {
                        if (jsgnqxDto.getYwid().equals(s)) {
                            zds.add(jsgnqxDto.getXszd());
                        }
                    }
                    map.put("zds", zds);
                    listMap.add(map);
                }
            }
		}
		mav.addObject("listMap",listMap);
		mav.addObject("xtjsDto",xtjsDto);
		return mav;
	}
	
	/**
	 * 修改jsgnqx
	 * @param xtjsDto
	 * @return
	 */
	@RequestMapping("/configunit/settingSaveField")
	@ResponseBody
	public Map<String,Object> settingSaveField(XtjsDto xtjsDto){
		Map<String, Object> map= new HashMap<>();
		boolean isSuccess =jsgnqxService.insertJsgnqx(xtjsDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 新增检测单位设置功能
	 * @param jsjcdwDto
	 * @return
	 */
	@RequestMapping("/configunit/detectionunitJsjcdw")
	public ModelAndView detectionunitJsjcdw(JsjcdwDto jsjcdwDto) {
		ModelAndView mav=new ModelAndView("globalweb/systemrole/role_detectionunit");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECTION_UNIT});
		List<JsjcdwDto> jcdwList=jsjcdwService.getDtoList(jsjcdwDto);
		List<String> strlist= new ArrayList<>();
		if(!CollectionUtils.isEmpty(jcdwList)) {
			for (JsjcdwDto jsjcdwDto_t:jcdwList){
				strlist.add(jsjcdwDto_t.getJcdw());
			}
		}
		mav.addObject("detectionUnitList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("strlist",strlist);
		mav.addObject("jsjcdwDto",jsjcdwDto);
		return mav;
	}
	
	/**
	 * 保存角色检测单位
	 * @param xtjsDto
	 * @return
	 */
	@RequestMapping("/configunit/detectionunitSaveJsjcdw")
	@ResponseBody
	public Map<String,Object> saveJsjcdw(XtjsDto xtjsDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=jsjcdwService.insertJsjcdw(xtjsDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 设置系统页面
	 * @param jsxtqxDto
	 * @return
	 */
	@RequestMapping("/role/systemSet")
	public  ModelAndView systemSet(JsxtqxDto jsxtqxDto){
		XtjsDto xtjsDto=new XtjsDto();
		xtjsDto.setJsid(jsxtqxDto.getJsid());
		xtjsDto=xtjsService.getDto(xtjsDto);
		jsxtqxDto.setJsdm(xtjsDto.getJsdm());
		jsxtqxDto.setJsmc(xtjsDto.getJsmc());
		ModelAndView mav=new ModelAndView("globalweb/systemrole/role_system");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.SYSTEM_CODE});
		List<String> strlist= new ArrayList<>();
		List<JsxtqxDto> jsxtqxDtos = jsxtqxService.getDtoList(jsxtqxDto);
		if(!CollectionUtils.isEmpty(jsxtqxDtos)) {
			for (JsxtqxDto jsxtqxDto_t:jsxtqxDtos){
				strlist.add(jsxtqxDto_t.getXtid());
			}
		}	
		mav.addObject("xtlist",jclist.get(BasicDataTypeEnum.SYSTEM_CODE.getCode()));
		mav.addObject("strlist",strlist);
		mav.addObject("jsxtqxDto",jsxtqxDto);
		return mav;
	}
	/**
	 * 保存角色系统设置
	 * @param jsxtqxDto
	 * @return
	 */
	@RequestMapping("/role/systemSaveSet")
	@ResponseBody
	public Map<String,Object> systemSaveSet(JsxtqxDto jsxtqxDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess=jsxtqxService.insertJsxtqxDto(jsxtqxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 角色选择界面
	 * @param xtjsDto
	 * @return
	 */
	@RequestMapping("/role/pagedataSelectListRole")
	@ResponseBody
	public ModelAndView pagedataSelectListRole(XtjsDto xtjsDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/select_roleList");
		mav.addObject("xtjsDto", xtjsDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 获取该账套下的模块
	 * @param xtzyDto
	 * @return
	 */
	@RequestMapping("/role/pagedataQueryMk")
	@ResponseBody
	public List<XtzyDto> pagedataQueryMk(XtzyDto xtzyDto){

		return xtzyService.getDtoList(xtzyDto);
	}
}
