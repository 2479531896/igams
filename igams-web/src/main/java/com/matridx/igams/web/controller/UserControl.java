package com.matridx.igams.web.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.SpgwDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.ZjglDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.PersonalSettingEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.ISpgwService;
import com.matridx.igams.common.service.svcinterface.ISpgwcyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.service.svcinterface.IZjglService;
import com.matridx.igams.web.dao.entities.WbcxDto;
import com.matridx.igams.web.dao.entities.XtjsDto;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.dao.entities.YhjgqxDto;
import com.matridx.igams.web.dao.entities.YhjsDto;
import com.matridx.igams.web.dao.entities.YhqdxxDto;
import com.matridx.igams.web.dao.entities.YhssjgDto;
import com.matridx.igams.web.service.svcinterface.IWbcxService;
import com.matridx.igams.web.service.svcinterface.IXtjsService;
import com.matridx.igams.web.service.svcinterface.IXtyhService;
import com.matridx.igams.web.service.svcinterface.IYhjgqxService;
import com.matridx.igams.web.service.svcinterface.IYhjsService;
import com.matridx.igams.web.service.svcinterface.IYhqdxxService;
import com.matridx.igams.web.service.svcinterface.IYhssjgService;
import com.matridx.igams.wechat.dao.entities.HbqxDto;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.IWxyhService;
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
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	IYhjgqxService yhjgqxService;
	
	@Autowired
	ISpgwService spgwService;
	
	@Autowired
	ISpgwcyService spgwcyService;
	
	@Autowired
	IYhssjgService yhssjgService;
	
	@Autowired
	IYhqdxxService yhqdxxService;
	
	@Autowired
	IWxyhService wxyhService;
	@Autowired
	IWbcxService wbcxService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IHbqxService hbqxService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IZjglService zjglService;

	private final static Logger log = LoggerFactory.getLogger(UserControl.class);
	/**
	 * 更新考勤页面
	 * @return
	 */
	@RequestMapping(value ="/user/pageListUser")
	public ModelAndView pageListUser(){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/user_list");
		List<WbcxDto> wbcxDtos=wbcxService.getSsgsList();
		mav.addObject("ssgslist",wbcxDtos);
		return mav;
	}

	/**
	 * 外部用户新增界面
	 * @return
	 */
	@RequestMapping(value ="/user/addExternalUser")
	public ModelAndView addExternalUser(){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/externaluser_edit");
		List<XtjsDto> xtjsDtos = xtjsService.getAllJsAndChecked(null);
		mav.addObject("xtjsDtos", xtjsDtos);
		List<JgxxDto> jgxxList=jgxxService.getJgxxList();
		mav.addObject("jgxxList", jgxxList);
		XtyhDto t_xtyhDto = new XtyhDto();
		t_xtyhDto.setFormAction("add");
		mav.addObject("xtyhDto", t_xtyhDto);
		List<JcsjDto> xtlist = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SYSTEM_CODE.getCode());//系统
		List<JcsjDto> decetionlist = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode());//系统
		mav.addObject("xtlist", xtlist);
		mav.addObject("decetionlist", decetionlist);
		mav.addObject("yhid",StringUtil.generateUUID());//先赋予yhid
		return mav;
	}

	/**
	 * 保存外部用户信息
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping(value = "/user/addSaveExternalUser")
	@ResponseBody
	public Map<String, Object> addSaveExternalUser(XtyhDto xtyhDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		xtyhDto.setLrry(user.getYhid());
		//如果没有设置则为不锁定
		if(StringUtil.isBlank(xtyhDto.getSfsd())) {
			xtyhDto.setSfsd("0");
		}
		Map<String,String> result=new HashMap<>();
		if("1".equals(xtyhDto.getSignflg())){//如果为第二步确定操作，直接请求生信端
			result=xtyhService.postToSxSaveUser(xtyhDto,request);
			map.put("status", result.get("status"));
			map.put("message", result.get("message"));
		}else{
			XtyhDto xtyhDto_t = xtyhService.getDtoByName(xtyhDto.getYhm());
			if(xtyhDto_t==null) {
				result= xtyhService.insertExternalUser(xtyhDto,request);
				map.put("status", result.get("status"));
				map.put("message", result.get("message"));
			}else {
				map.put("status", "fail");
				map.put("message",xxglService.getModelById("ICOMM_MM00008").getXxnr());
			}
		}
		return map;
	}

	// /**
	//  * 反馈更新考勤的结果
	//  * @return
	//  */
	// @RequestMapping(value ="/user/updatecheckSavePersonal")
	// @ResponseBody
	// public Map<String,Object> updatecheckSavePersonal(XtyhDto xtyhDto){
	// 	XtyhDto t_xtyhDto = xtyhService.getDtoById(xtyhDto.getYhid());
	// 	if(t_xtyhDto!=null){
	// 		xtyhDto.setDdid(t_xtyhDto.getDdid());
	// 		xtyhDto.setYhm(t_xtyhDto.getYhm());
	// 		xtyhDto.setWbcxid(t_xtyhDto.getWbcxid());
	// 	}
	// 	boolean isSuccess = yhkqxxService.updateCheckSave(xtyhDto);
	// 	//Json格式的要求{total:22,rows:{}}
	// 	//构造成Json的格式传递
	// 	Map<String,Object> map = new HashMap<String,Object>();
	// 	map.put("status", isSuccess?"success":"fail");
	// 	map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
	// 	return map;
	// }



	/**
	 * 用户列表页面
	 * @return
	 */
	@RequestMapping(value ="/user/updateCheck")
	public ModelAndView updateCheck(){
        return new ModelAndView("globalweb/systemrole/updatecheck_edit");
	}
	
	/**
	 * 用户列表
	 * @return
	 */
	@RequestMapping(value ="/user/pageGetListUser")
	@ResponseBody
	public Map<String,Object> pageGetListUser(XtyhDto xtyhDto){
		List<XtyhDto> t_List = xtyhService.getPagedDtoList(xtyhDto);
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", xtyhDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}

	/**
	 * 用户列表(不分页 无需验证)
	 * @return
	 */
	@RequestMapping(value ="/user/pagedataUserList")
	@ResponseBody
	public Map<String,Object> pagedataUserList(XtyhDto xtyhDto){
		List<XtyhDto> t_List = xtyhService.getNoPagedDtoList(xtyhDto);
		Map<String,Object> result = new HashMap<>();
		result.put("rows", t_List);
		return result;
	}

	@RequestMapping(value="/user/viewUser")
	public ModelAndView viewUser(XtyhDto xtyhDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/user_view");
		XtyhDto t_xtyhDto = xtyhService.getDtoById(xtyhDto.getYhid());
		mav.addObject("xtyhDto", t_xtyhDto);
		
		YhjsDto t_YhjsDto = new YhjsDto();
		t_YhjsDto.setYhid(t_xtyhDto.getYhid());
		List<YhjsDto> yhjsDtos = yhjsService.getAllByYhid(t_YhjsDto);
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SIGNATURE.getCode());
		fjcfbDto.setYwid(t_xtyhDto.getYhm());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
		mav.addObject("yhjsDtos", yhjsDtos);
		
		return mav;
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
	
	@RequestMapping(value = "/user/addSaveUser")
	@ResponseBody
	public Map<String, Object> addSaveUser(XtyhDto xtyhDto){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		xtyhDto.setLrry(user.getYhid());
		//如果没有设置则为不锁定
		if(StringUtil.isBlank(xtyhDto.getSfsd())) {
            xtyhDto.setSfsd("0");
        }
		XtyhDto xtyhDto_t = xtyhService.getDtoByName(xtyhDto.getYhm());
		if(xtyhDto_t==null) {
			boolean isSuccess = xtyhService.insertDto(xtyhDto);			
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			map.put("yhid", xtyhDto.getYhid());
		}else {
			map.put("status", "fail");
			map.put("message",xxglService.getModelById("ICOMM_MM00008").getXxnr());
		}		
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
		mav.addObject("zjs", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.JOB_RANK.getCode()));//职级
		List<XtjsDto> xtjsDtos = xtjsService.getAllJsAndChecked(t_xtyhDto.getYhid());
		mav.addObject("xtjsDtos", xtjsDtos);
		return mav;
	}
	
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
	
	@RequestMapping(value = "/user/delUser")
	@ResponseBody
	public Map<String, Object> delUser(XtyhDto xtyhDto) {
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo();
		xtyhDto.setScry(user.getYhid());
		boolean isSuccess;
		try {
			isSuccess = xtyhService.deleteXtyh(xtyhDto);
			xtyhService.deleteInCompleteTaskByIds(xtyhDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		} catch (BusinessException e) {
			map.put("message", e.getMsg());
			return map;
		}
		map.put("status", isSuccess?"success":"fail");
		return map;
	}
	
	@RequestMapping(value = "/user/pagedataModPass")
	public ModelAndView modPass(XtyhDto xtyhDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/pass_edit");
		mav.addObject("xtyhDto",xtyhDto);
		return mav;
	}
	
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
	@RequestMapping(value = "/user/initdd")
	@ResponseBody
	public Map<String, Object> initdd(XtyhDto xtyhDto){
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
		GrszDto grszDto=new GrszDto();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlb(PersonalSettingEnum.SETTING_NEXT_CONFIRMER.getCode());
		GrszDto grszDto_t=grszService.selectGrszDtoByYhidAndSzlb(grszDto);
		if(grszDto_t==null) {
			grszDto_t=new GrszDto();
		}
		mav.addObject("xtyhDto", xtyhDto);
		mav.addObject("grszDto", grszDto_t);
		return mav;
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
	 * 跳转微信用户列表
	 * @return
	 */
	@RequestMapping(value = "/user/pageListWechatUser")
	public ModelAndView WxyhhUser(WxyhDto wxyhdto) {
		ModelAndView mav=new ModelAndView("globalweb/systemrole/relwechatuser_list");
		mav.addObject("wxyhdto", wxyhdto);
		return mav;
	}
	/**
	 * 微信用户列表
	 * @param wxyhdto
	 * @return
	 */
	@RequestMapping(value="/user/pageGetListWechatUser")
	@ResponseBody
	public Map<String,Object> getUserList(WxyhDto wxyhdto){
		List<WxyhDto> wxyhlist=xtyhService.getPagedDtoListWxyh(wxyhdto);
		Map<String, Object> map= new HashMap<>();
		map.put("total", wxyhdto.getTotalNumber());
		map.put("rows", wxyhlist);
		return map;
	}
	
	/**
	 * 更新微信用户和系统用户信息
	 * @param wxyhdto
	 * @return
	 */
	@RequestMapping(value="/user/pagedataUpdate")
	@ResponseBody
	public Map<String, Object> upateYhxx(WxyhDto wxyhdto){
		Map<String, Object> map= new HashMap<>();
		boolean isSuccess=wxyhService.upateYhxx(wxyhdto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 根据token获取用户信息
	 * @return
	 */
	@RequestMapping(value ="/common/getUserByToken")
	@ResponseBody
	public User getUserByToken(HttpServletRequest request){
		String token = request.getParameter("access_token");
        return xtyhService.getUserByToken(token);
	}
	
	/**
	 * 查询出所有的审批岗位
	 * @param spgwDto
	 * @return
	 */
	@RequestMapping(value="/user/pagedataDtoList")
	@ResponseBody
	public Map<String,Object> getDtoList(SpgwDto spgwDto){
		Map<String,Object> map= new HashMap<>();
		List<SpgwDto> spgwDtos=spgwService.getDtoList(spgwDto);
		map.put("spgwDtos",spgwDtos);
		return map;
	} 
	
	/**
	 * 通过jsid和yhid查询出当前js的审批岗位
	 * @param spgwcyDto
	 * @return
	 */
	@RequestMapping(value="/user/pagedataSpgwcyList")
	@ResponseBody
	public Map<String,Object> getSpgwcyList(SpgwcyDto spgwcyDto){
		Map<String,Object> map= new HashMap<>();
		List<SpgwcyDto> spgwcyDtos=spgwcyService.getDtoList(spgwcyDto);
		map.put("spgwcyDtos",spgwcyDtos);
		return map;
	} 
	
	/**
	 * 通过jsid查询当前角色的单位限制
	 * @param xtjsDto
	 * @return
	 */
	@RequestMapping(value="/user/pagedataDwxzbj")
	@ResponseBody
	public Map<String,Object> getdwxzbj(XtjsDto xtjsDto){
		Map<String,Object> map= new HashMap<>();
		XtjsDto xtjsDto_t=xtjsService.getDtoById(xtjsDto.getJsid());
		map.put("xtjsDto",xtjsDto_t);
		return map;
	} 
	
	/**
	 * 通过jsid查询当前角色的单位限制
	 * @param yhjgqxDto
	 * @return
	 */
	@RequestMapping(value="/user/pagedataListByjsid")
	@ResponseBody
	public Map<String,Object> getListByjsid(YhjgqxDto yhjgqxDto){
		Map<String,Object> map= new HashMap<>();
		List<YhjgqxDto> yhjgqxDtos=yhjgqxService.getListByjsid(yhjgqxDto);
		map.put("yhjgqxDtos",yhjgqxDtos);
		return map;
	}
	
	/**
	 * 更新下载签到信息页面
	 * @return
	 */
	@RequestMapping(value="/user/checkinrecordPage")
	public ModelAndView checkinrecordPage(){
        return new ModelAndView("globalweb/systemrole/user_checkinRecord");
	}
	
	/**
	 * 更新用户列表的微信ID到微信用户列表
	 * @param yhqdxxDto
	 * @return
	 */
	@RequestMapping(value="/user/checkinrecordSavePage")
	@ResponseBody
	public Map<String, Object> saveCheckinRecord(YhqdxxDto yhqdxxDto){
		Map<String, Object> map= new HashMap<>();
		User user = getLoginInfo();
		yhqdxxDto.setLrry(user.getYhid());
		yhqdxxDto.setJgid("132428666");//销售总部
		String path = yhqdxxService.saveCheckinRecord(yhqdxxDto,user);
		map.put("path", path);
		map.put("status",StringUtil.isNotBlank(path)?"success":"fail");
		map.put("message",StringUtil.isNotBlank(path)?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	
	/**
	 * 下载记录
	 * @param yhqdxxDto
	 * @return
	 */
	@RequestMapping(value="/user/downloadCheckinRecord")
	@ResponseBody
	public String downloadCheckinRecord(YhqdxxDto yhqdxxDto, HttpServletResponse response){
		if(StringUtil.isNotBlank(yhqdxxDto.getPath())){
			File file =  new File(yhqdxxDto.getPath());
	        byte[] buffer = new byte[1024];
	        BufferedInputStream bis = null;
	        InputStream iStream = null;
	        OutputStream os = null; //输出流
	        try {
	        	response.setHeader("content-type", "application/octet-stream");
				response.setContentLength((int) file.length());
		        //指明为下载
		        response.setContentType("application/x-download");
		        response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode("签到导出.xls", StandardCharsets.UTF_8));
	        	iStream = new FileInputStream(file);
	            os = response.getOutputStream();
	            bis = new BufferedInputStream(iStream);
	            int i = bis.read(buffer);
	            while(i != -1){
	                os.write(buffer);
	                os.flush();
	                i = bis.read(buffer);
	            }
	            
	        } catch (Exception e) {
				log.error(e.toString());
    		} finally {
    			closeStream(new Closeable[] { bis, iStream, os });
    		}
		}
		return null;
	}
	
	/**
	 * 关闭流
	 * 
	 * @param streams
	 */
	private static void closeStream(Closeable[] streams) {
		if (streams == null || streams.length < 1) {
            return;
        }
        for (Closeable closeable : streams) {
            try {
                if (null != closeable) {
                    closeable.close();
				}
            } catch (IOException e) {
                log.error(e.toString());
            }
        }
	}
	
	/**
	 * 上传签名页面
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping(value ="/user/uploadsignUser")
	public ModelAndView uploadsignUser(XtyhDto xtyhDto){
		ModelAndView mav = new ModelAndView("globalweb/systemrole/user_uploadsign");
		//获取文件类型
		xtyhDto.setYwlx(BusTypeEnum.IMP_SIGNATURE.getCode());
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(xtyhDto.getYhm());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SIGNATURE.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("xtyhDto", xtyhDto);
		return mav;
	}
	
	/**
	 * 上传签名保存
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping(value = "/user/uploadsignSaveUser")
	@ResponseBody
	public Map<String, Object> uploadsignSaveUser(XtyhDto xtyhDto){
		Map<String,Object> map = new HashMap<>();
		//获取用户信息
		User user = getLoginInfo();
		xtyhDto.setLrry(user.getYhid());
		boolean isSuccess = xtyhService.uploadSaveSignature(xtyhDto);
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

	@RequestMapping(value = "/user/pagedataModUpdateData")
	@ResponseBody
	public Map<String, Object> modUpdateData(XtyhDto xtyhDto){
		boolean isSuccess = xtyhService.updateJg(xtyhDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 获取外部用户的信息
	 * @param xtyhDto
	 * @return
	 */
	@RequestMapping(value = "/user/pagedataExternalUser")
	@ResponseBody
	public Map<String,Object> pagedataExternalUser(XtyhDto xtyhDto){
		Map<String,Object> map=new HashMap<>();
		xtyhDto = xtyhService.getDtoById(xtyhDto.getYhid());
		map.put("xtyhDto",xtyhDto);
		List<XtjsDto> xtjsDtos = xtjsService.getAllJsAndChecked(xtyhDto.getYhid());
		map.put("xtjsDtos", xtjsDtos);
		List<HbqxDto> hbqxDtos=hbqxService.getHbxxByYhid(xtyhDto.getYhid());
		map.put("hbqxDtos",hbqxDtos);
		return map;
	}

	/**
	 * @Description: 个人消息订阅
	 * @param xtyhDto
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2024/11/21 15:50
	 */
	@RequestMapping("/user/pagedataGetMessage")
	public ModelAndView pagedataGetMessage(XtyhDto xtyhDto) {
		ModelAndView mav=new ModelAndView("globalweb/message/subscribeGrMessage");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.MESSAGE_TYPE});
		List<JcsjDto> jcsjDtos = jclist.get(BasicDataTypeEnum.MESSAGE_TYPE.getCode());
		List<JcsjDto> yhJcsjDtos = new ArrayList<>();
		if(!CollectionUtils.isEmpty(jcsjDtos)){
			for (JcsjDto jcsjDto:jcsjDtos){
				if("HB".equals(jcsjDto.getCskz2())){
					mav.addObject("hbDto",jcsjDto);
				}else{
					yhJcsjDtos.add(jcsjDto);
				}
			}
		}
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setCsmc("审核订阅消息");
		yhJcsjDtos.add(jcsjDto);
		mav.addObject("yhJcsjDtos",yhJcsjDtos);
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(xtyhDto.getYhid());
		Map<String,Object> map = grszService.queryMessage(grszDto);
		mav.addObject("hbList",map.get("sjhbxxDtos"));
		mav.addObject("dyxxMap",map.get("dyxxMap"));
		mav.addObject("hbJson",map.get("hbList"));
		mav.addObject("yhJson",map.get("yhList"));
		mav.addObject("zsxm",map.get("zsxm"));
		mav.addObject("yhid",xtyhDto.getYhid());
		mav.addObject("ids",map.get("hbid"));
		mav.addObject("formAction","/systemrole/user/pagedataSaveMessage");
		return mav;
	}

	/**
	 * @Description: 获取伙伴消息
	 * @param xtyhDto
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/25 13:56
	 */
	@RequestMapping("/user/pagedataGetHbMessage")
	@ResponseBody
	public List<GrszDto> pagedataGetHbMessage(XtyhDto xtyhDto) {
		List<GrszDto> grszDtoList = new ArrayList<>();
		if(StringUtil.isNotBlank(xtyhDto.getYhid())){
			grszDtoList = grszService.queryByYhid(xtyhDto.getYhid());
		}
		return grszDtoList;
	}

	/**
	 * @Description: 个人消息订阅保存
	 * @param grszDto
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/25 15:39
	 */
	@RequestMapping("/user/pagedataSaveMessage")
	@ResponseBody
	public Map<String,Object> pagedataSaveMessage(GrszDto grszDto) {
		Map<String,Object> map = new HashMap<>();
		try {
			boolean isSuccess = grszService.saveAndSendRabbit(grszDto);
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return  map;
	}

	/**
	 * @Description: 个人消息订阅
	 * @param xtyhDto
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2024/11/21 15:50
	 */
	@RequestMapping("/user/setmessageUser")
	public ModelAndView setmessageUser(XtyhDto xtyhDto) {
		ModelAndView mav=new ModelAndView("globalweb/message/subscribeMessage");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.MESSAGE_TYPE});
		List<JcsjDto> jcsjDtos = jclist.get(BasicDataTypeEnum.MESSAGE_TYPE.getCode());
		List<JcsjDto> yhJcsjDtos = new ArrayList<>();
		if(!CollectionUtils.isEmpty(jcsjDtos)){
			for (JcsjDto jcsjDto:jcsjDtos){
				if("HB".equals(jcsjDto.getCskz2())){
					mav.addObject("hbDto",jcsjDto);
				}else{
					yhJcsjDtos.add(jcsjDto);
				}
			}
		}
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setCsmc("审核订阅消息");
		yhJcsjDtos.add(jcsjDto);
		mav.addObject("yhJcsjDtos",yhJcsjDtos);
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(xtyhDto.getYhid());
		Map<String,Object> map = grszService.queryMessage(grszDto);
		mav.addObject("hbList",map.get("sjhbxxDtos"));
		mav.addObject("dyxxMap",map.get("dyxxMap"));
		mav.addObject("hbJson",map.get("hbList"));
		mav.addObject("yhJson",map.get("yhList"));
		mav.addObject("zsxm",map.get("zsxm"));
		mav.addObject("yhid",xtyhDto.getYhid());
		mav.addObject("ids",map.get("hbid"));
		mav.addObject("formAction","/systemrole/user/setmessageSaveUser");
		return mav;
	}

	/**
	 * @Description: 个人消息订阅保存
	 * @param grszDto
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/25 15:39
	 */
	@RequestMapping("/user/setmessageSaveUser")
	@ResponseBody
	public Map<String,Object> setmessageSaveUser(GrszDto grszDto) {
		Map<String,Object> map = new HashMap<>();
		try {
			boolean isSuccess = grszService.saveAndSendRabbit(grszDto);
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return  map;
	}
	/**
	 * @Description: 上传工具
	 * @param
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2025/4/22 14:29
	 */
	@RequestMapping("/user/pagedataUploadTool")
	public ModelAndView pagedataUploadTool() {
		ModelAndView mav = new ModelAndView("globalweb/tool/uploadTool");
		ZjglDto zjglDto = new ZjglDto();
		zjglDto.setIds("tool,external");
		List<ZjglDto> zjglDtoList = zjglService.getDtoList(zjglDto);
		mav.addObject("ywlx", BusTypeEnum.IMP_UPLOADTOOL.getCode());
		mav.addObject("zjglDtoList", zjglDtoList);
		return mav;
	}
	/**
	 * @Description: 保存工具
	 * @param zjglDto
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2025/4/29 16:15
	 */
	@RequestMapping("/user/pagedataSaveUploadTool")
	@ResponseBody
	public Map<String,Object> pagedataSaveUploadTool(ZjglDto zjglDto, HttpServletRequest request) {
		User user = getLoginInfo(request);
		zjglDto.setLrry(user.getYhid());
		return zjglService.insertTool(zjglDto);
	}

	/**
	 * @Description: 删除工具
	 * @param zjglDto
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2025/4/29 16:16
	 */
	@RequestMapping("/user/pagedataDelTool")
	@ResponseBody
	public Map<String,Object> pagedataDelTool(ZjglDto zjglDto, HttpServletRequest request) {
		User user = getLoginInfo(request);
		zjglDto.setScry(user.getYhid());
		return zjglService.delTool(zjglDto);
	}
}
