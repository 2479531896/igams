package com.matridx.server.wechat.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.server.wechat.dao.entities.BqglDto;
import com.matridx.server.wechat.dao.entities.DxtzDto;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WxyhDto;
import com.matridx.server.wechat.service.svcinterface.IBqglService;
import com.matridx.server.wechat.service.svcinterface.IWbcxService;
import com.matridx.server.wechat.service.svcinterface.IWxyhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/user")
public class WechatUserControl extends BaseController{

	@Autowired
	IWxyhService wxyhservice;
	@Autowired
	IXxglService xxglservice;
	@Autowired
	IBqglService bqglService;
	@Autowired
	IWbcxService wbcxService;
	
	/**
	 * 跳转微信用户界面
	 * @return
	 */
	@RequestMapping(value = "/user/pageListWechatUser")
	public ModelAndView WechatUser() {
		ModelAndView mav=new ModelAndView("wechat/menu/wechat_userlist");
		List<WxyhDto> wxyhidlist=wxyhservice.getPagedDtoList();
		mav.addObject("wxyhlist",wxyhidlist);
		return mav;
	}
	
	/**
	 * 微信用户列表
	 * @param wxyhdto
	 * @return
	 */
	@RequestMapping(value = "/user/wechatUserPage")
	@ResponseBody
	public Map<String,Object> WxyhList(WxyhDto wxyhdto){
		List<WxyhDto> wxyhlist=wxyhservice.getPagedDtoList(wxyhdto);
		Map<String, Object> map= new HashMap<>();
		map.put("total", wxyhdto.getTotalNumber());
		map.put("rows", wxyhlist);
		return map;
	}
	
	/**
	 * 查看微信用户信息
	 * @param wxyhdto
	 * @return
	 */
	@RequestMapping(value = "/user/viewUser")
	public ModelAndView WechatUserView(WxyhDto wxyhdto) {
		ModelAndView mav = new ModelAndView("wechat/menu/wechat_userView");
		WxyhDto wxyhxx = wxyhservice.getDto(wxyhdto);
		mav.addObject("WxyhDto", wxyhxx);
		return mav;
	}
	
	/**
	 * 新增用户信息
	 * @return
	 */
	@RequestMapping(value = "/user/addUser")
	public ModelAndView WechatUserAdd() {
		ModelAndView mav= new ModelAndView("wechat/menu/wechat_userAdd");
		WxyhDto wxyhdto = new WxyhDto();
		wxyhdto.setFormAction("insert");
		mav.addObject("WxyhDto", wxyhdto);
		//查询关注平台
		List<WbcxDto> wbcxDtos = wbcxService.getDtoList(null);
		mav.addObject("wbcxDtos", wbcxDtos);
		return mav;
	}

	/**
	 * 添加微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	
	@RequestMapping(value = "/user/insert")
	@ResponseBody
	public Map<String, Object> insertWxyhxx(WxyhDto wxyhDto){
		User user=getLoginInfo();
		wxyhDto.setLrry(user.getYhid());
		boolean isSuccess=wxyhservice.insertDto(wxyhDto);
		Map<String, Object> map= new HashMap<>();
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 修改页面:先通过id将微信用户信息查询出来
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value = "/user/modUser")
	public ModelAndView WechatUserMol(WxyhDto wxyhDto) {
		ModelAndView mav=new ModelAndView("wechat/menu/wechat_userAdd");
		WxyhDto wxyhxx = wxyhservice.getDto(wxyhDto);
		wxyhxx.setFormAction("UpdateWxyh");
		mav.addObject("WxyhDto", wxyhxx);
		//查询关注平台
		List<WbcxDto> wbcxDtos = wbcxService.getDtoList(null);
		mav.addObject("wbcxDtos", wbcxDtos);
		//查询标签
		BqglDto bqglDto = new BqglDto();
		bqglDto.setWbcxid(wxyhxx.getGzpt());
		List<BqglDto> bqglDtos = bqglService.selectTag(bqglDto);
		mav.addObject("bqglDtos", bqglDtos);
		return mav;
	}
	
	/**
	 * 修改微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value = "/user/UpdateWxyh")
	@ResponseBody
	public Map<String,Object> WxyhUpdate(WxyhDto wxyhDto){
		User user = getLoginInfo();
		wxyhDto.setXgry(user.getYhid());
		boolean isSuccess=wxyhservice.updateWxyh(wxyhDto);
		Map<String, Object> map= new HashMap<>();
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 根据用户id删除微信用户信息
	 * @param wxyhdto
	 * @return
	 */
	@RequestMapping(value = "/user/delUser")
	@ResponseBody
	public Map<String, Object> delWxyh(WxyhDto wxyhdto){
		User user=getLoginInfo(); 
		wxyhdto.setScry(user.getYhid()); 
		boolean isSuccess= wxyhservice.deleteWxyhbyyhid(wxyhdto); 
		Map<String, Object> map= new HashMap<>();
		map.put("status",isSuccess?"success":"fail"); 
		map.put("message",isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr()); 
		return map;
	}
	
	/**
	 * 批量添加用户标签页面
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value = "user/setTagUser")
	public ModelAndView setTagUser(WxyhDto wxyhDto){
		ModelAndView mav = new ModelAndView("wechat/menu/wechat_userTag");
		wxyhDto.setFormAction("set");
		mav.addObject("wxyhDto", wxyhDto);
		BqglDto bqglDto = new BqglDto();
		bqglDto.setWbcxid(wxyhDto.getGzpt());
		List<BqglDto> bqglDtos = bqglService.selectTag(bqglDto);
		mav.addObject("bqglDtos", bqglDtos);
		return mav;
	}
	
	/**
	 * 批量添加用户标签保存
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value="/user/setSaveTagUser")
	@ResponseBody
	public Map<String, Object> setSaveTagUser(WxyhDto wxyhDto){
		//获取用户信息
		User user = getLoginInfo();
		wxyhDto.setXgry(user.getYhid());
		boolean isSuccess = wxyhservice.setSaveTagUser(wxyhDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglservice.getModelById("ICOM00001").getXxnr()):xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 批量取消用户标签页面
	 * @param wxyhDto
	 * @return
	 */
	
	@RequestMapping(value = "user/cancleTagUser")
	public ModelAndView cancleTagUser(WxyhDto wxyhDto){
		ModelAndView mav = new ModelAndView("wechat/menu/wechat_userTag");
		wxyhDto.setFormAction("cancle");
		mav.addObject("wxyhDto", wxyhDto);
		BqglDto bqglDto = new BqglDto();
		bqglDto.setWbcxid(wxyhDto.getGzpt());
		List<BqglDto> bqglDtos = bqglService.selectTag(bqglDto);
		mav.addObject("bqglDtos", bqglDtos);
		return mav;
	}
	
	/**
	 * 批量取消用户标签保存
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value="/user/cancleSaveTagUser")
	@ResponseBody
	public Map<String, Object> cancleSaveTagUser(WxyhDto wxyhDto){
		//获取用户信息
		User user = getLoginInfo();
		wxyhDto.setXgry(user.getYhid());
		boolean isSuccess = wxyhservice.cancleSaveTagUser(wxyhDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglservice.getModelById("ICOM00001").getXxnr()):xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 获取用户信息页面
	 * @return
	 */
	@RequestMapping(value = "/getUserList")
	public ModelAndView getUserView(){
        return new ModelAndView("wechat/menu/wechat_userGet");
	}
	
	/**
	 * 跳转消息通知界面
	 * @return
	 */
	@RequestMapping(value="/user/noticeUser")
	public ModelAndView getnoticeUserPage(DxtzDto dxtzDto) {
		ModelAndView mav=new ModelAndView("wechat/menu/wechat_usernotice");
		dxtzDto.setYwlx(BusTypeEnum.IMP_SMS.getCode());
		mav.addObject("dxtzDto", dxtzDto);
		return mav;
	}
	
	/**
	 * 更新用户信息
	 * @return
	 */
	@RequestMapping("/user/getUserList")
	@ResponseBody
	public Map<String, Object> getUserList(WbcxDto wbcxDto){
		//获取用户信息
		User user = getLoginInfo();
		wbcxDto.setLrry(user.getYhid());
		boolean isSuccess = wxyhservice.updateUserList(wbcxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglservice.getModelById("ICOM00001").getXxnr()):xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
