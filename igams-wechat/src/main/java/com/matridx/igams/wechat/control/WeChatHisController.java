package com.matridx.igams.wechat.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.service.svcinterface.ISjdwxxService;
import com.matridx.igams.wechat.service.svcinterface.IWeChatHisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wechat")
public class WeChatHisController extends BaseController{
	@Autowired
	private ISjdwxxService sjdwxxservice;
	@Autowired
	private IXxglService xxglservice;
	@Autowired
	private IWeChatHisService  wechatHisService;
    
	/**
	* 跳转医院页面
	* 
	* @return
	*/
	@RequestMapping(value = "/his/pageListWechatHis")
	public ModelAndView WechatHis() {
        return new ModelAndView("wechat/menu/wechat_his");
	}
	/**
	* 推送医院
	* 
	* @return
	*/
	@RequestMapping(value = "/his/pushWechatHis")
	@ResponseBody
	public Map<String, Object> pushWechatHis(SjdwxxDto sjdwxxDto){
		Map<String, Object> map= new HashMap<>();
		try {
			boolean result=wechatHisService.creatHis(sjdwxxDto);
			map.put("status", result?"success":"fail");
			map.put("message", result?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		} catch (Exception e) {
		// TODO: handle exception
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	* 查询医院科室
	* @param sjdwxxDto
	* @return
	*/
	@RequestMapping(value = "/his/pageGetListWechatHis")
	@ResponseBody
	public Map<String, Object> pageListHis(SjdwxxDto sjdwxxDto) {
		Map<String, Object> map = new HashMap<>();
		List<SjdwxxDto> sjdwxxList = sjdwxxservice.getPagedDtoListSjdwxx(sjdwxxDto);
		map.put("total", sjdwxxDto.getTotalNumber());
		map.put("rows", sjdwxxList);
		return map;
	}

	/**
	 * 查看科室
	 * @param
	 * @return
	 */
	@RequestMapping(value="/his/viewHis")
	public ModelAndView viewHis(SjdwxxDto sjdwxxDto){
		ModelAndView mav = new ModelAndView("wechat/menu/wechat_viewHis");
		mav.addObject("sjdwxxDto", sjdwxxservice.getDtoById(sjdwxxDto.getDwid()));
		return mav;
	}

	/**
	* 跳转添加页面
	* 
	* @return
	*/

	@RequestMapping(value="/his/addWechatHis")
	public ModelAndView getWechatHis(SjdwxxDto sjdwxxDto){ 
		ModelAndView mav=new  ModelAndView("wechat/menu/wechat_hisAdd");
		mav.addObject("FormAction","/wechat/his/addSaveWechatHis");
		mav.addObject("sjdwxxDto", sjdwxxDto);
		return mav; 
	}
	
	/**
	* 单位添加操作
	* @param sjdwxxDto
	* @return
	*/
	@RequestMapping(value="/his/addSaveWechatHis")
	@ResponseBody
	public Map<String, Object>addWechatHis(SjdwxxDto sjdwxxDto){
		User user=getLoginInfo(); 
		sjdwxxDto.setLrry(user.getYhid());
		boolean isSuccess=sjdwxxservice.addWechatHis(sjdwxxDto);
		Map <String, Object> map= new HashMap<>();
	 	map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}
	 
	/**
	* 单个查询
	* @param sjdwxxDto
	* @return
	*/	 
	@RequestMapping("/his/modWechatHis")
	public  ModelAndView ModHis(SjdwxxDto sjdwxxDto) {
		ModelAndView mav= new ModelAndView("wechat/menu/wechat_hisAdd");
		SjdwxxDto sjdwxxDto_t=sjdwxxservice.getDtoById(sjdwxxDto.getDwid());
		mav.addObject("FormAction", "/wechat/his/modSaveWechatHis");
		mav.addObject("sjdwxxDto", sjdwxxDto_t);
	 	return mav;
	}
	 
	/**
	* 单位修改保存
	* @param sjdwxxDto
	* @return
	*/
	@RequestMapping(value = "/his/modSaveWechatHis")
	@ResponseBody
	public Map<String, Object> modSaveHis(SjdwxxDto sjdwxxDto){
		User user = getLoginInfo();
	 	sjdwxxDto.setXgry(user.getYhid());
		boolean isSuccess=sjdwxxservice.modSavewHis(sjdwxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}
	 
	/**  
	* 删除
	* @param sjdwxxDto
	* @return
	*/
	@RequestMapping(value = "/his/delWechatHis")
	@ResponseBody
	public Map<String, Object>delHis(SjdwxxDto sjdwxxDto){ 
		User user=getLoginInfo();
		sjdwxxDto.setScry(user.getYhid());
	 	Map<String, Object> map= new HashMap<>();
		boolean isSuccess=sjdwxxservice.delHisByDwid(sjdwxxDto);
	 	map.put("status", isSuccess?"success":"fail");
	 	map.put("message",isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr());
		return map; 
	}
	 	   
}
