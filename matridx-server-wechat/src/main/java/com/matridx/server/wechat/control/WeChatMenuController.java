package com.matridx.server.wechat.control;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.matridx.igams.common.enums.CharacterEnum;
import com.matridx.igams.common.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.ProgramCodeEnum;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.server.wechat.dao.entities.BqglDto;
import com.matridx.server.wechat.dao.entities.WxcdDto;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.service.svcinterface.IBqglService;
import com.matridx.server.wechat.service.svcinterface.IWxcdService;
import com.matridx.server.wechat.service.svcinterface.IWbcxService;
import com.matridx.server.wechat.util.WeChatUtils;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Controller
@RequestMapping("/menu")
public class WeChatMenuController extends BaseController {
	@Autowired
	IWxcdService wxcdService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IBqglService bqglService;
	@Autowired
	IWbcxService wbcxService;
	@Autowired
	RedisUtil redisUtil;
	
	/**
	 * 更新微信菜单信息
	 * @return
	 */
	@RequestMapping("/menu/createMenu")
	@ResponseBody
	public Map<String, Object> createMenu(WxcdDto wxcdDto){
		Map<String,Object> map = new HashMap<>();
		try{
			boolean result = wxcdService.createMenu(wxcdDto);
			map.put("status", result?"success":"fail");
			map.put("message", result?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 删除微信菜单信息
	 * @return
	 */
	@RequestMapping("/menu/deleteMenu")
	@ResponseBody
	public Map<String, Object> deleteMenu(WxcdDto wxcdDto){
		Map<String,Object> map = new HashMap<>();
		try{
			boolean result = WeChatUtils.deleteMenu(restTemplate, wxcdDto.getWbcxdm(),redisUtil);
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
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setLx(CharacterEnum.WECHAT.getCode());
		List<WbcxDto> wxzghList=wbcxService.getDtoList(wbcxDto);
		mav.addObject("wxzghList", wxzghList);
		return mav;
	}
	
	/**
	 * 菜单页面程序选择
	 * @param bqglDto
	 * @return
	 */
	@RequestMapping(value="/menu/selectTag")
	@ResponseBody
	public Map<String,Object> selectTag(BqglDto bqglDto){
		Map<String,Object> map= new HashMap<>();
		//过微信公众号查询标签
		List<BqglDto> bqglList =  bqglService.selectTag(bqglDto);
		map.put("bqglList", bqglList);
		//查询外部程序代码
		WbcxDto wbcxDto = wbcxService.getDtoById(bqglDto.getWbcxid());
		map.put("wbcxDto", wbcxDto);
		return map;
	}
	
	/**
	 * 微信菜单新增保存
	 * @return
	 */
	@RequestMapping(value = "/menu/getGxlxList")
	@ResponseBody
	public Map<String, Object> getGxlxList(){
		List<BqglDto> bqglList =  bqglService.selectAll();
		Map<String,Object> map = new HashMap<>();
		map.put("bqglList", bqglList);
		return map;
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
	@RequestMapping(value ="/menu/addWechatMenu")
	public ModelAndView addWechatMenu(WxcdDto wxcdDto){
		ModelAndView mav = new ModelAndView("wechat/menu/wechat_menuAdd");
		mav.addObject("wxcdDto", wxcdDto);
		return mav;
	}
	
	/**
	 * 微信菜单新增保存
	 * @param wxcdDto
	 * @return
	 */
	@RequestMapping(value = "/menu/addSaveWechatMenu")
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
	 * 上传永久素材(图片)
	 * @return
	 */
	@RequestMapping(value ="/menu/uploadMaterial")
	public ModelAndView sendMaterial(){
		ModelAndView mav = new ModelAndView("wechat/menu/upload_material");
		mav.addObject("ywlx", BusTypeEnum.IMP_WECHAT_MATERIAL.getCode());
		return mav;
	}
	
	/**
	 * 上传永久素材(图片)
	 * @param xtszDto
	 * @return
	 */
	@RequestMapping(value = "/menu/uploadSaveMaterial")
	@ResponseBody
	public Map<String, Object> uploadSaveMaterial(HttpServletRequest request,XtszDto xtszDto){
		Map<String,Object> map = new HashMap<>();
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MATRIDX.getCode();
		}
		boolean isSuccess = wxcdService.uploadSaveMaterial(xtszDto, wbcxdm);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 群发消息
	 * @return
	 */
	@RequestMapping(value ="/menu/sendAllCustom")
	public ModelAndView sendAllCustom(){
        return new ModelAndView("wechat/menu/wechat_sendall");
	}
	
	/**
	 * 群发消息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/menu/sendSaveAllCustom")
	@ResponseBody
	public Map<String, Object> sendSaveAllCustom(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		String xxnr = request.getParameter("xxnr");
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MATRIDX.getCode();
		}
		xxnr = xxnr.replaceAll("＜","<").replaceAll("＞", ">");
		boolean isSuccess = false;
		if(StringUtil.isNotBlank(xxnr)){
			isSuccess  = WeChatUtils.sendAllText(restTemplate, xxnr, wbcxdm,redisUtil);
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 微信公众号列表
	 * @param wbcxDto
	 * @return
	 */
	@RequestMapping(value="/menu/pageListPublicName")
	public ModelAndView wechatPublicName(WbcxDto wbcxDto) {
		ModelAndView mav=new ModelAndView("wechat/menu/wechat_pubNameList");
		mav.addObject("wbcxDto", wbcxDto);
		return mav;
	}
	
	/**
	 * 列表数据
	 * @param wkgzhDto
	 * @return
	 */
	@RequestMapping(value="/menu/getPageDtoList")
	@ResponseBody
	public Map<String,Object> getPageDtoList(WbcxDto wkgzhDto){
		Map<String,Object> map= new HashMap<>();
		List<WbcxDto> list = wbcxService.getPagedDtoList(wkgzhDto);
		map.put("total", wkgzhDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}
	
	/**
	 * 新增微信公众号
	 * @param wbcxDto
	 * @return
	 */
	@RequestMapping(value="/menu/addWechatPubName")
	public ModelAndView addWechatPubName(WbcxDto wbcxDto) {
		ModelAndView mav=new ModelAndView("wechat/menu/wechat_pubNameAdd");
		wbcxDto.setFormAction("saveWechatPubName");
		List<String> list = new ArrayList<>();
		for (CharacterEnum value : CharacterEnum.values()) {
			list.add(value.getCode());
		}
		mav.addObject("list", list);
		mav.addObject("wbcxDto", wbcxDto);
		return mav;
	}
	
	/**
	 * 新增保存
	 * @param wbcxDto
	 * @return
	 */
	@RequestMapping(value="/menu/saveWechatPubName")
	@ResponseBody
	public Map<String,Object> saveWechatPubName(WbcxDto wbcxDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		wbcxDto.setLrry(user.getYhid());
		boolean isSuccess = wbcxService.addSave(wbcxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 修改微信公众号
	 * @param wbcxDto
	 * @return
	 */
	@RequestMapping(value="/menu/modWechatPubName")
	public ModelAndView modWechatPubName(WbcxDto wbcxDto) {
		ModelAndView mav=new ModelAndView("wechat/menu/wechat_pubNameAdd");
		wbcxDto.setFormAction("addWechatPubName");
		WbcxDto wbcxDto_t = wbcxService.getDto(wbcxDto);
		DBEncrypt crypt = new DBEncrypt();
		wbcxDto_t.setAppid(crypt.dCode(wbcxDto_t.getAppid()));
		wbcxDto_t.setSecret(crypt.dCode(wbcxDto_t.getSecret()));
		if(StringUtil.isNotBlank(wbcxDto_t.getToken())){
			wbcxDto_t.setToken(crypt.dCode(wbcxDto_t.getToken()));
		}
		wbcxDto_t.setFormAction("updateWechatPubName");
		List<String> list = new ArrayList<>();
		for (CharacterEnum value : CharacterEnum.values()) {
			list.add(value.getCode());
		}
		mav.addObject("list", list);
		mav.addObject("wbcxDto", wbcxDto_t);
		return mav;
	}
	
	/**
	 * 修改保存
	 * @param wbcxDto
	 * @return
	 */
	@RequestMapping(value="/menu/updateWechatPubName")
	@ResponseBody
	public Map<String,Object> updateWechatPubName(WbcxDto wbcxDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		wbcxDto.setXgry(user.getYhid());
		boolean isSuccess = wbcxService.modSave(wbcxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 查看微信公众号
	 * @param wbcxDto
	 * @return
	 */
	@RequestMapping(value="/menu/viewWechatPubName")
	public ModelAndView viewWechatPubName(WbcxDto wbcxDto) {
		ModelAndView mav=new ModelAndView("wechat/menu/wechat_pubNameView");
		WbcxDto wbcxDto_t=wbcxService.getDto(wbcxDto);
		DBEncrypt crypt = new DBEncrypt();
		wbcxDto_t.setAppid(crypt.dCode(wbcxDto_t.getAppid()));
		wbcxDto_t.setSecret(crypt.dCode(wbcxDto_t.getSecret()));
		if(StringUtil.isNotBlank(wbcxDto_t.getToken())){
			wbcxDto_t.setToken(crypt.dCode(wbcxDto_t.getToken()));
		}
		mav.addObject("wbcxDto", wbcxDto_t);
		return mav;
	}
	
	/**
	 * 删除微信公众号
	 * @param wbcxDto
	 * @return
	 */
	@RequestMapping(value="/menu/delWechatPubName")
	@ResponseBody
	public Map<String,Object> delWechatPubName(WbcxDto wbcxDto){
		Map<String,Object> map = new HashMap<>();
		User user=getLoginInfo();
		wbcxDto.setScry(user.getYhid());
		boolean isSuccess=wbcxService.delete(wbcxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 生成临时二维码
	 * @return
	 */
	@RequestMapping(value ="/menu/generateQRcodeView")
	public ModelAndView generateQRcodeView(WxcdDto wxcdDto){
		ModelAndView mav = new ModelAndView("wechat/menu/wechat_generateQRcode");
		mav.addObject("wxcdDto", wxcdDto);
		return mav;
	}
	
	/**
	 * 生成临时二维码
	 * @param wxcdDto
	 * @return
	 */
	@RequestMapping(value = "/menu/generateQRcode")
	@ResponseBody
	public Map<String, Object> generateQRcode(WxcdDto wxcdDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		String sceneStr = request.getParameter("sceneStr");
		if(StringUtil.isBlank(sceneStr)){
			map.put("status", "fail");
			map.put("message", "请输入二维码标识！");
		}
		String wbcxdm = wxcdDto.getWbcxdm();
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		String ticket = WeChatUtils.generateQRcodeTempStr(restTemplate, "2592000", sceneStr, wbcxdm,redisUtil);
		map.put("status", StringUtil.isNotBlank(ticket)?"success":"fail");
		map.put("message", StringUtil.isNotBlank(ticket)?"生成二维码成功！":"生成二维码失败！");
		map.put("ticket", "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket);
		return map;
	}
}
