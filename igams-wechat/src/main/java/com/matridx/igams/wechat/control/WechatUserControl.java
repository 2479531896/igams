package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import com.matridx.igams.wechat.service.svcinterface.IWxyhService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/wechat")
public class WechatUserControl extends BaseController{

	@Autowired
	IWxyhService wxyhservice;
	@Autowired
	IXxglService xxglservice;
	@Autowired
	RedisUtil redisUtil;
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
	 * 跳转用户列表
	 * @return
	 */
	@RequestMapping(value = "/user/pageListUser")
	public ModelAndView YhUser(WxyhDto wxyhdto) {
		ModelAndView mav=new ModelAndView("wechat/menu/reluser_list");
		mav.addObject("wxyhdto", wxyhdto);
		return mav;
	}
	/**
	 * 系统用户列表
	 * @param wxyhdto
	 * @return
	 */
	@RequestMapping(value="/user/pageGetListUser")
	@ResponseBody
	public Map<String,Object> getUserList(WxyhDto wxyhdto){
		List<WxyhDto> yhlist=wxyhservice.getPagedDtoListXtyh(wxyhdto);
		Map<String, Object> map= new HashMap<>();
		map.put("total", wxyhdto.getTotalNumber());
		map.put("rows", yhlist);
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
		boolean isSuccess = wxyhservice.upateYhxx(wxyhdto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 微信用户列表
	 * @param wxyhdto
	 * @return
	 */
	@RequestMapping(value = "/user/pagedataListWechatUserPage")
	@ResponseBody
	public Map<String,Object> WxyhList(WxyhDto wxyhdto){
		List<WxyhDto> wxyhlist=wxyhservice.getPagedDtoList(wxyhdto);
		Map<String, Object> map= new HashMap<>();
		map.put("total", wxyhdto.getTotalNumber());
		map.put("rows", wxyhlist);
		return map;
	}
	/**
	 * 新增用户信息
	 * @return
	 */
	@RequestMapping(value = "/user/addUser")
	public ModelAndView WechatUserAdd(WxyhDto wxyhDto) {
		ModelAndView mav= new ModelAndView("wechat/menu/wechat_userAdd");
		WxyhDto wxyhdto = new WxyhDto();
		wxyhdto.setFormAction("addSaveUser");
		List<WxyhDto> wxyhbq=wxyhservice.selectbqmbybqlbid(wxyhdto);
		mav.addObject("Wxyhbqlist", wxyhbq);
		mav.addObject("WxyhDto", wxyhdto);
		List<Map<String, Object>> wbcxDtos = new ArrayList<>();
		Map<Object, Object> wbcxInfo = redisUtil.hmget("WbcxInfo");
		if (wbcxInfo!=null && !wbcxInfo.isEmpty()) {
			for (Map.Entry<Object, Object> next : wbcxInfo.entrySet()) {
				Object value = next.getValue();
				Object key = next.getKey();
				if (null != value && null != key) {
					Map<String, Object> jsonObject = JSONObject.parseObject(String.valueOf(value),Map.class);
					wbcxDtos.add(jsonObject);
				}
			}
		}
		mav.addObject("wbcxDtos", wbcxDtos);
		return mav;
	}
	/**
	 * 查看微信用户信息
	 * @param wxyhdto
	 * @return
	 */
	@RequestMapping(value = "/user/viewUser")
	public ModelAndView WechatUserView(WxyhDto wxyhdto) {
		ModelAndView mav = new ModelAndView("wechat/menu/wechat_userView");
		WxyhDto wxyhxx = wxyhservice.WxyhDto(wxyhdto);
		mav.addObject("WxyhDto", wxyhxx);
		List<Map<String, Object>> wbcxDtos = new ArrayList<>();
		Map<Object, Object> wbcxInfo = redisUtil.hmget("WbcxInfo");
		if (wbcxInfo!=null && !wbcxInfo.isEmpty()) {
			for (Map.Entry<Object, Object> next : wbcxInfo.entrySet()) {
				Object value = next.getValue();
				Object key = next.getKey();
				if (null != value && null != key) {
					Map<String, Object> jsonObject = JSONObject.parseObject(String.valueOf(value),Map.class);
					wbcxDtos.add(jsonObject);
				}
			}
		}
		mav.addObject("wbcxDtos", wbcxDtos);
		return mav;
	}
	/**
	 * 添加微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	
	@RequestMapping(value = "/user/addSaveUser")
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
		WxyhDto wxyhxx=wxyhservice.WxyhDto(wxyhDto);
		List<WxyhDto> wxyhbq=wxyhservice.selectbqmbybqlbid(wxyhDto);
		wxyhxx.setFormAction("modSaveUser");
		mav.addObject("WxyhDto", wxyhxx);
		mav.addObject("Wxyhbqlist", wxyhbq);
		List<Map<String, Object>> wbcxDtos = new ArrayList<>();
		Map<Object, Object> wbcxInfo = redisUtil.hmget("WbcxInfo");
		if (wbcxInfo!=null && !wbcxInfo.isEmpty()) {
			for (Map.Entry<Object, Object> next : wbcxInfo.entrySet()) {
				Object value = next.getValue();
				Object key = next.getKey();
				if (null != value && null != key) {
					Map<String, Object> jsonObject = JSONObject.parseObject(String.valueOf(value),Map.class);
					wbcxDtos.add(jsonObject);
				}
			}
		}
		mav.addObject("wbcxDtos", wbcxDtos);
		return mav;
	}
	/**
	 * 修改微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value = "/user/modSaveUser")
	@ResponseBody
	public Map<String,Object> WxyhUpdate(WxyhDto wxyhDto){
		User user = getLoginInfo();
		wxyhDto.setXgry(user.getYhid());
		wxyhDto.setBqm((wxyhservice.WxyhDto(wxyhDto)).getBqidlb());
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
	 * 获取阿里服务器微信用户信息
	 * @return
	 */
	@RequestMapping("/user/getUserList")
	@ResponseBody
	public Map<String, Object> getUserList(HttpServletRequest request, WxyhDto wxyhDto){
		//获取用户信息
		User user = getLoginInfo();
		wxyhDto.setLrry(user.getYhid());
		boolean isSuccess = wxyhservice.getUserList(wxyhDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglservice.getModelById("ICOM00001").getXxnr()):xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 选择微信用户表页面
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping("/user/pagedataListUser")
	public ModelAndView pagelistUser(WxyhDto wxyhDto) {
        return new ModelAndView("wechat/menu/list_wechatUser");
	}
}
