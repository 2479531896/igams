package com.matridx.igams.wechat.control;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JkdymxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IJkdymxService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.BqglDto;
import com.matridx.igams.wechat.dao.entities.SysxxDto;
import com.matridx.igams.wechat.dao.entities.WxcdDto;
import com.matridx.igams.wechat.service.svcinterface.IBqglService;
import com.matridx.igams.wechat.service.svcinterface.ISysxxService;
import com.matridx.igams.wechat.service.svcinterface.IWeChatService;
import com.matridx.igams.wechat.service.svcinterface.IWxcdService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wechat")
public class WeChatControl extends BaseController {
	
	@Autowired
	IWxcdService wxcdService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IBqglService bqglService;
	@Autowired
	IXtszService xtszService;
	@Autowired
	ISysxxService sysxxService;
	@Autowired
	IJkdymxService jkdymxService;
	@Autowired
	IXxglService xxglservice;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	private IWeChatService webChatService;
	
	/**
	 * 更新微信菜单信息
	 * @return
	 */
	@RequestMapping("/createMenu")
	@ResponseBody
	public Map<String, Object> createMenu(WxcdDto wxcdDto){
		Map<String,Object> map = new HashMap<>();
		try{
			boolean result = webChatService.createMenu(wxcdDto);
			map.put("status", result?"success":"fail");
			map.put("message", result?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 微信菜单页面
	 * @return
	 */
	@RequestMapping(value ="/menu/pageListWechatMenu")
	public ModelAndView pageListWechatMenu(){
		ModelAndView mav = new ModelAndView("wechat/menu/wechat_menu");
		List<BqglDto> bqglList =  bqglService.selectAll();
		mav.addObject("bqglList", bqglList);
		return mav;
	}
	
	/**
	 * 查询菜单列表
	 * @param wxcdDto
	 * @param rsp
	 * @return
	 */
	@RequestMapping(value = "/menu/wechatMenuTree",produces = "application/json;charset=UTF-8")
	@ResponseBody
	public String wechatMenuTree(WxcdDto wxcdDto,HttpServletResponse rsp){
		String JSONDATA = "";
		List<WxcdDto> wxcdList = wxcdService.getWxcdTreeList(wxcdDto);
		JSONDATA = wxcdService.installTree(wxcdList,JSONDATA);
		rsp.addHeader("Access-Control-Allow-Origin", "*");
		rsp.setHeader("Content-Type", "application/json;charset=UTF-8");
		return JSONDATA;
	}
	
	/**
	 * 微信菜单修改页面
	 * @return
	 */
	@RequestMapping(value ="/menu/modWechatMenu")
	public ModelAndView modWechatMenu(WxcdDto wxcdDto){
		ModelAndView mav = new ModelAndView("wechat/menu/wechat_menuMod");
		WxcdDto t_wxcdDto = wxcdService.getDtoById(wxcdDto.getCdid());
		mav.addObject("wxcdDto", t_wxcdDto);
		return mav;
	}
	
	/**
	 * 微信菜单修改保存
	 * @param wxcdDto
	 * @return
	 */
	@RequestMapping(value = "/menu/modSaveWechatMenu")
	@ResponseBody
	public Map<String, Object> modSaveWechatMenu(WxcdDto wxcdDto){
		User user = getLoginInfo();
		wxcdDto.setXgry(user.getYhid());
		boolean isSuccess = wxcdService.modSaveWechatMenu(wxcdDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 微信菜单新增页面
	 * @return
	 */
	@RequestMapping(value ="/menu/pagedataAddWechatMenu")
	public ModelAndView pagedataAddWechatMenu(WxcdDto wxcdDto){
		ModelAndView mav = new ModelAndView("wechat/menu/wechat_menuAdd");
		mav.addObject("wxcdDto", wxcdDto);
		return mav;
	}
	
	/**
	 * 微信菜单新增保存
	 * @param wxcdDto
	 * @return
	 */
	@RequestMapping(value = "/menu/pagedataAddSaveWechatMenu")
	@ResponseBody
	public Map<String, Object> addSaveWechatMenu(WxcdDto wxcdDto){
		User user = getLoginInfo();
		wxcdDto.setLrry(user.getYhid());
		boolean isSuccess = wxcdService.addSaveWechatMenu(wxcdDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 微信菜单删除
	 * @param wxcdDto
	 * @return
	 */
	@RequestMapping(value = "/menu/delWechatMenu")
	@ResponseBody
	public Map<String, Object> delWechatMenu(WxcdDto wxcdDto){
		//获取用户信息
		User user = getLoginInfo();
		wxcdDto.setScry(user.getYhid());
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = wxcdService.deleteByCdid(wxcdDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 获取钉钉小程序版本号
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/dingtalk/getVersion")
	@ResponseBody
	public Map<String, Object> getVersion(HttpServletRequest request){
	    Map<String,Object> map= new HashMap<>();
	    XtszDto xtszDto = new XtszDto();
	    xtszDto.setSzlb(GlobalString.MATRIDX_DINGTALK_VERSION);
	    xtszDto = xtszService.getDto(xtszDto);
	    map.put("version", xtszDto == null?null:xtszDto.getSzz());
	    return map;
	}

	/**
	 * 版本信息列表页面
	 *
	 * @return
	 */
	@RequestMapping("/sys/pageListSysxx")
	public ModelAndView pageListSysxx() {
		ModelAndView mav = new ModelAndView("wechat/sysxx/sysxx_List");
		return mav;
	}

	/**
	 * 获取列表数据
	 */
	@RequestMapping("/sys/pagedataSysxx")
	@ResponseBody
	public Map<String, Object> listSysxx(SysxxDto sysxxDto) {
		Map<String, Object> map = new HashMap<>();
		List<SysxxDto> sysxxDtoList = sysxxService.getPageSysxxDtoList(sysxxDto);
		map.put("total", sysxxDto.getTotalNumber());
		map.put("rows", sysxxDtoList);
		return map;
	}

	/**
	 * 查看信息对应详情
	 */
	@RequestMapping("/sys/viewSysxx")
	public ModelAndView viewSysxx(SysxxDto sysxxDto) {
		ModelAndView mav = new ModelAndView("wechat/sysxx/sysxx_view");
		SysxxDto sysxxDto_t = sysxxService.getDtoById(sysxxDto.getSysid());
		mav.addObject("sysxxDto", sysxxDto_t);
		return mav;
	}

	/**
	 * 增加数据
	 */
	@RequestMapping("/sys/addSysxx")
	public ModelAndView addSysxx(SysxxDto sysxxDto) {
		ModelAndView mav = new ModelAndView("wechat/sysxx/sysxx_add");
		sysxxDto.setFormAction("addSaveSysxx");
		mav.addObject("decetionlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("sysxxDto", sysxxDto);
		return mav;
	}

	/**
	 * 增加信息到数据库
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sys/pagedataSaveSysxx")
	@ResponseBody
	public Map<String, Object> pagedataSaveSysxx(SysxxDto sysxxDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		boolean isSuccess = false;
		if (StringUtil.isNotBlank(sysxxDto.getSysid())){
			sysxxDto.setXgry(user.getYhid());
			isSuccess = sysxxService.updateDto(sysxxDto);
		}else {
			sysxxDto.setSysid(StringUtil.generateUUID());
			sysxxDto.setLrry(user.getYhid());
			isSuccess = sysxxService.insertDto(sysxxDto);
		}
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglservice.getModelById("ICOM00001").getXxnr() : xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 修改时新增一个页面
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sys/modSysxx")
	public ModelAndView modSysxx(SysxxDto sysxxDto) {
		ModelAndView mav = new ModelAndView("wechat/sysxx/sysxx_add");
		SysxxDto sysxxDto_t = sysxxService.getDtoById(sysxxDto.getSysid());
		sysxxDto_t.setFormAction("modSaveSysxx");
		mav.addObject("decetionlist", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("sysxxDto", sysxxDto_t);
		return mav;
	}

	/**
	 * 删除信息
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/sys/delSysxx")
	@ResponseBody
	public Map<String, Object> delSysxx(SysxxDto sysxxDto) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess = sysxxService.delete(sysxxDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglservice.getModelById("ICOM00003").getXxnr() : xxglservice.getModelById("ICOM00004").getXxnr());
		return map;
	}

//	/jkdymx/pagedataJkdymx
	/**
	 * 接口调用信息列表页面
	 *
	 * @return
	 */
	@RequestMapping("/jkdymx/pageListJkdymx")
	public ModelAndView pageListJkdymx() {
		ModelAndView mav = new ModelAndView("wechat/jkdymx/jkdymx_List");
		mav.addObject("lxqfs",jkdymxService.getSearchItems("lxqf"));
		mav.addObject("dyfl",jkdymxService.getSearchItems("dyfl"));
		return mav;
	}

	/**
	 * 获取列表数据
	 */
	@RequestMapping("/jkdymx/pagedataJkdymx")
	@ResponseBody
	public Map<String, Object> listJkdymx(JkdymxDto jkdymxDto) {
		Map<String, Object> map = new HashMap<>();
		List<JkdymxDto> jkdymxDtoList = jkdymxService.getPageJkdymxDtoList(jkdymxDto);
		map.put("total", jkdymxDto.getTotalNumber());
		map.put("rows", jkdymxDtoList);
		return map;
	}

	/**
	 * 查看信息对应详情
	 */
	@RequestMapping("/jkdymx/viewJkdymx")
	public ModelAndView viewJkdymx(JkdymxDto jkdymxDto) {
		ModelAndView mav = new ModelAndView("wechat/jkdymx/jkdymx_view");
		JkdymxDto jkdymxDto_t = jkdymxService.getDtoById(jkdymxDto.getDyid());
		mav.addObject("jkdymxDto", jkdymxDto_t);
		return mav;
	}
}
