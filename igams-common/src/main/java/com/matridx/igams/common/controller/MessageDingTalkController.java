package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XxglDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
@RequestMapping("/message")
public class MessageDingTalkController extends BaseController{
	
	@Autowired
	private IXxglService xxglService;
	@Autowired
	private IJcsjService jcsjService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	@Autowired
	private IGrszService grszService;
	@Autowired
	private RedisUtil redisUtil;
	/**
	 * 增加一个消息管理的页面模块
	 */
	@RequestMapping("/message/pageListMessage")
	public ModelAndView pageMessage() {
		ModelAndView mav= new ModelAndView("common/message/message_list");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.MESSAGE_TYPE});
		mav.addObject("xxlxlist",jclist.get(BasicDataTypeEnum.MESSAGE_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 显示信息管理列表
	 */
	@RequestMapping("/message/pageGetListMessage")
	@ResponseBody
	public Map<String, Object> getPagedDtoList(XxglDto xxglDto){
		Map<String,Object> map= new HashMap<>();
		List<XxglDto> xxgllist = xxglService.getPagedDtoList(xxglDto);
		ArrayList<String> xxnrList = new ArrayList<>();
		for(XxglDto xxgldto : xxgllist) {
			String xxnrContent = xxgldto.getXxnr().replaceAll("<", "＜").
					replaceAll(">", "＞").replaceAll("\"","＂").replaceAll("#","＃");
			xxnrList.add(xxnrContent);
			xxgldto.setXxnr(xxnrContent);
		}
		map.put("total",xxglDto.getTotalNumber());
		map.put("rows",xxgllist);
		return map;
	}
	
	/**
	 * 增加点击新增的页面
	 */
	@RequestMapping("/message/addMessage")
	@ResponseBody
	public ModelAndView addMessageList(XxglDto xxglDto){
		ModelAndView mav=new ModelAndView("common/message/message_add");
		xxglDto.setFormAction("addSaveMessage");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.MESSAGE_TYPE});
		mav.addObject("xxglDto", xxglDto);
		mav.addObject("messageTypelist",jclist.get(BasicDataTypeEnum.MESSAGE_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	    * 增加信息到数据库
	 */
    @RequestMapping(value="/message/addSaveMessage")
	@ResponseBody
    public Map<String, Object> addSaveMessage(XxglDto xxglDto){
    	Map<String, Object> map= new HashMap<>();
    	boolean isSuccess;
    	String xxnrContent = xxglDto.getXxnr().replaceAll("＜", "<").replaceAll("＞", ">").replaceAll("＂","\"").replaceAll("＃","#");
    	xxglDto.setXxnr(xxnrContent);
    	int num = xxglService.getCount(xxglDto);
    	if(num>0) {
    		map.put("status", "fail");
    		map.put("message", "消息已经存在，请核实后添加！");
    	}else if(num==0) {
    		isSuccess=xxglService.insertDto(xxglDto);
			if(StringUtil.isNotBlank(xxglDto.getCskz1()) && "DYXX".equals(xxglDto.getCskz1())){
				redisUtil.hset("Grsz:MR",xxglDto.getXxid(),"1",-1);
			}
        	map.put("status",isSuccess?"success":"fail");
    		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
    	}
		return map;
    }
	
	/**
	 * 修改时新增一个页面
	 */
	@RequestMapping(value="/message/modMessage")
	@ResponseBody
	public ModelAndView modMessageList(XxglDto xxglDto){
		ModelAndView mav=new ModelAndView("common/message/message_mod");
		xxglDto = xxglService.getDto(xxglDto);
		xxglDto.setFormAction("modSaveMessage");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.MESSAGE_TYPE});
		mav.addObject("xxglDto", xxglDto);
		mav.addObject("messageTypelist",jclist.get(BasicDataTypeEnum.MESSAGE_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 修改消息到数据库
	 */
	@RequestMapping(value="/message/modSaveMessage")
	@ResponseBody
	public Map<String,Object> modSaveMessage(XxglDto xxglDto){
		Map<String,Object> map = new HashMap<>();
		String xxnrContent = xxglDto.getXxnr().replaceAll("＜", "<").replaceAll("＞", ">").replaceAll("＂","\"").replaceAll("＃","#");
		xxglDto.setXxnr(xxnrContent);
        boolean isSuccess;
		isSuccess=xxglService.updateDto(xxglDto);
    	map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 删除消息
	 */
	@RequestMapping(value="/message/delMessage")
	@ResponseBody
	public Map<String,Object> deleteMessage(XxglDto xxglDto){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = xxglService.delete(xxglDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 查看消息详情
	 */
	@RequestMapping(value="/message/viewMessage")
	@ResponseBody
	public ModelAndView viewMessageList(XxglDto xxglDto){
		ModelAndView mav=new ModelAndView("common/message/message_view");
		xxglDto = xxglService.getDto(xxglDto);
		mav.addObject("xxglDto", xxglDto);
		return mav;
	}

	/**
	 * @Description: 订阅消息默认设置
	 * @param
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2024/11/19 15:36
	 */
	@RequestMapping("/message/setdefaultMessage")
	public ModelAndView setdefaultMessage() {
		ModelAndView mav= new ModelAndView("common/message/message_setDefault");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.MESSAGE_TYPE});
		List<JcsjDto> jcsjDtos = jclist.get(BasicDataTypeEnum.MESSAGE_TYPE.getCode());
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setCsmc("审核订阅消息");
		jcsjDtos.add(jcsjDto);
		mav.addObject("messageTypelist",jcsjDtos);
		Map<String,Object> map = new HashMap<>();
		map = grszService.queryMrsz();
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("map", map);
		String dyxxJson = JSONObject.toJSONString(map);
		mav.addObject("dyxxJson", dyxxJson);
		return mav;
	}

	/**
	 * @Description: 默认订阅消息保存
	 * @param grszDto
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/21 10:02
	 */
	@RequestMapping("/message/setdefaultSaveMessage")
	@ResponseBody
	public Map<String,Object> setdefaultSaveMessage(GrszDto grszDto, HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		grszDto.setLrry(user.getYhid());
		try {
			boolean isSuccess = grszService.setDefaultSave(grszDto);
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return  map;
	}
}
