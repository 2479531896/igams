package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.DsrwqxDto;
import com.matridx.igams.common.dao.entities.DsrwszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.service.svcinterface.IDsrwqxService;
import com.matridx.igams.common.service.svcinterface.IDsrwszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/systemmain")
public class TimingTaskController extends BaseController{

	@Autowired
	IDsrwszService dsrwservice;
	@Autowired
	IDsrwqxService dsrwqxService;
	@Autowired
	IXxglService xxglService;

	
	@Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;

	@Value("${matridx.systemflg.remindtype:}")
	private String remindtype;

	@Value("${matridx.rabbit.flg:}")
	private String rabbitFlg;

	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;

	@Override
	public String getPrefix() {
		return urlPrefix;
	}

	@Autowired(required=false)
	private AmqpTemplate amqpTempl;
	
	/**
	 * 定时任务页面
	 */
	@RequestMapping(value = "/timing/pageListTimingTask")
	public ModelAndView getTimingTaskPage() {
		ModelAndView mav = new ModelAndView("systemmain/timing/timingtask_list");
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	
	/**
	 * 定时任务列表
	 */
	@RequestMapping(value = "/timing/pageGetListTimingTask")
	@ResponseBody
	public Map<String,Object> getTimingTaskList(DsrwszDto dsrwszDto){
		List<DsrwszDto> dsrwszlist=dsrwservice.getPagedDtoList(dsrwszDto);
		Map<String, Object> map=new HashMap<>();
		map.put("total", dsrwszDto.getTotalNumber());
		map.put("rows", dsrwszlist);
		return map;
	}

	/**
	 * 查看定时任务信息
	 */
	@RequestMapping(value = "/timing/viewTimingTask")
	public ModelAndView getDsrwxx(DsrwszDto dsrwszDto) {
		ModelAndView mav = new ModelAndView("systemmain/timing/timingtask_view");
		DsrwszDto dsrwxx = dsrwservice.selectDsrwxxByRwid(dsrwszDto);
		mav.addObject("DsrwszDto", dsrwxx);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
		
	}
	
	/**
	 * 新增定时任务页面
	 */
	@RequestMapping(value="/timing/addTimingTask")
	public ModelAndView toAddPage(DsrwszDto dsrwszDto) {
		ModelAndView mav = new ModelAndView("systemmain/timing/timingtask_add");
		dsrwszDto.setFormAction("addSaveTimingTask");
		mav.addObject("Dsrwxx", dsrwszDto);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	/**
	 * 提交保存
	 */
	@RequestMapping(value ="/timing/addSaveTimingTask")
	@ResponseBody
	public Map<String,Object> addDsrwxx(DsrwszDto dsrwszDto,HttpServletRequest request) {
		User user=getLoginInfo(request);
		dsrwszDto.setLrry(user.getYhid());
		boolean isSuccess=dsrwservice.insertDto(dsrwszDto);

		if(isSuccess){
			Map<String,Object>newMap=new HashMap<>();
			newMap.put("type","add");
			newMap.put("dsrwid",dsrwszDto.getRwid());
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg+"sys.igams.task.taskHandle"+rabbitFlg+"1", JSONObject.toJSONString(newMap));

		}

		Map<String, Object> map=new HashMap<>();
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 编辑定时任务页面
	 */
	@RequestMapping(value="/timing/modTimingTask")
	public ModelAndView toModPage(DsrwszDto dsrwszDto) {
		ModelAndView mav = new ModelAndView("systemmain/timing/timingtask_add");
		DsrwszDto dsrwxx=dsrwservice.selectDsrwxxByRwid(dsrwszDto);
		dsrwxx.setFormAction("modSaveTimingTask");
		mav.addObject("Dsrwxx", dsrwxx);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	
	/**
	 * 编辑提交保存
	 */
	@RequestMapping(value="/timing/modSaveTimingTask")
	@ResponseBody
	public Map<String,Object> modSaveTimingTask(DsrwszDto dsrwszDto,HttpServletRequest request) {
		User user=getLoginInfo(request);
		dsrwszDto.setXgry(user.getYhid());
		boolean isSuccess=dsrwservice.updateDsxx(dsrwszDto);
		Map<String, Object> map=new HashMap<>();
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
//		ScheduledFuture future=futerMap.get(dsrwszDto.getRwid());
//		if(future!=null){
//			future.cancel(true);
//			futerMap.remove(dsrwszDto.getRwid());
//			 future =trPoolTaskScheduler.schedule(new ScheduleRunnable(dsrwszDto),new MatridxTrigger(dsrwservice,dsrwszDto) );
//			futerMap.put(dsrwszDto.getRwid(),future);
//		}

		if(isSuccess){
			Map<String,Object>newMap=new HashMap<>();
			newMap.put("type","update");
			newMap.put("dsrwid",dsrwszDto.getRwid());
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg+"sys.igams.task.taskHandle"+rabbitFlg+"1", JSONObject.toJSONString(newMap));

		}

		return map;
	}



	/**
	 * 删除定时任务信息
	 */
	@RequestMapping(value="/timing/delTimingTask")
	@ResponseBody
	public Map<String,Object> deleteDsrwxx(DsrwszDto dsrwszDto,HttpServletRequest request) {
		User user=getLoginInfo(request);
		dsrwszDto.setScry(user.getYhid());
		boolean isSuccess=dsrwservice.deleteByRwid(dsrwszDto);
		Map<String, Object> map=new HashMap<>();
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		if(isSuccess){
			if(dsrwszDto.getIds()!= null && !dsrwszDto.getIds().isEmpty()){
				for(int i=0;i<dsrwszDto.getIds().size();i++){
					//ScheduledFuture future=futerMap.get(dsrwszDto.getIds().get(i));
//				if(future!=null){
////					future.cancel(true);
////					futerMap.remove(dsrwszDto.getRwid());
//					cronTaskRegistrar.removeTriggerTask(dsrwszDto.getRwid());
//				}
					Map<String,Object>newMap=new HashMap<>();
					newMap.put("type","del");
					newMap.put("dsrwid",dsrwszDto.getIds().get(i));
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg+"sys.igams.task.taskHandle"+rabbitFlg+"1", JSONObject.toJSONString(newMap));
				}
			}
		}


		return map;
	}

	/**
	 * 执行定时任务
	 */
	@RequestMapping(value="/timing/executeTimingTask")
	@ResponseBody
	public Map<String,Object> executeTimingTask(DsrwszDto dsrwszDto) {
		Map<String, Object> messagemap=new HashMap<>();
		DsrwszDto dsrwxx = dsrwservice.selectDsrwxxByRwid(dsrwszDto);
		List<String> split = Arrays.asList(remindtype.split(","));
		if (null != dsrwxx && ( (StringUtil.isNotBlank(remindtype) && !split.contains(dsrwxx.getRemindtype())))){
			messagemap.put("status","fail");
			messagemap.put("message","分布式限制不允许执行！"); 
			return messagemap;
		}
		//执行类
		String classService = dsrwxx.getZxl();
		//执行方法
		String classMethod = dsrwxx.getZxff();
		//处理定时任务中的参数cs,注意格式写不对的，以及为null的，增加异常捕获
		//参数的书写为name=18,height=165
		Map<String, String> map = new HashMap<>();
		try {
			if( !StringUtil.isBlank(dsrwxx.getCs()) ) {
				String[] csstr = dsrwxx.getCs().split(",");
				for (String s : csstr) {
					String[] str = s.split("=");
					map.put(str[0], str[1]);
				}
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {

			Object serviceInstance = ServiceFactory.getService(classService);//获取方法所在class

			Method getCountMethod;
			if(StringUtil.isBlank(dsrwxx.getCs())){
				//获取方法（定时任务无参数）
				getCountMethod = serviceInstance.getClass().getMethod(classMethod);//获取计数方法
				//执行方法
				getCountMethod.invoke(serviceInstance);
			}else {
				//获取方法（定时任务有参数）
				getCountMethod = serviceInstance.getClass().getMethod(classMethod,Map.class);//获取计数方法
				//执行方法
				getCountMethod.invoke(serviceInstance,map);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			messagemap.put("status","fail");
			messagemap.put("message","运行失败"+e.getMessage());
			return messagemap;
		}
		messagemap.put("status","success");
		messagemap.put("message","运行成功");
		return messagemap;
	}

	/**
	 * 个人定时任务页面
	 */
	@RequestMapping(value = "/timing/pageListTimingTaskPersonal")
	public ModelAndView getTimingTaskPersonalPage(DsrwszDto dsrwszDto) {
		ModelAndView mav = new ModelAndView("systemmain/timing/timingtaskcube_list");
		Map<String,Object> result = new HashMap<>();
		User user = getLoginInfo();
		dsrwszDto.setYhid(user.getYhid());
		dsrwszDto.setJsid(user.getDqjs());
		List<DsrwszDto> dsrwszlist = dsrwservice.getDsrwListByLimt(dsrwszDto);
		mav.addObject("rows", dsrwszlist);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}

	@RequestMapping(value = "/timing/distinguishTimingTask")
	public ModelAndView configuser(DsrwqxDto dsrwqxDto){
		ModelAndView mav = new ModelAndView("systemmain/timing/timingtask_configuser");
		mav.addObject("dsrwqxDto", dsrwqxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 可选用户
	 */
	@RequestMapping(value ="/timing/pagedataListSelectUser")
	@ResponseBody
	public Map<String,Object> listUnSelectUser(UserDto userDto){
		List<UserDto> t_List;
		if("USER".equals(userDto.getLx())){
			t_List = dsrwqxService.getPagedOptionalList(userDto);
		}else{
			t_List = dsrwqxService.getPagedOptionalRoleList(userDto);
		}
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", userDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}


	/**
	 * 已选用户
	 */
	@RequestMapping(value ="/timing/pagedataSelectedUser")
	@ResponseBody
	public Map<String,Object> listSelectedUser(UserDto userDto){
		List<UserDto> t_List = dsrwqxService.getPagedSelectedList(userDto);
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", userDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}



	/**
	 * 添加用户
	 */
	@RequestMapping(value ="/timing/pagedataToSelected")
	@ResponseBody
	public Map<String,Object> toSelected(DsrwqxDto dsrwqxDto){
		try{
			User user = getLoginInfo();
			dsrwqxDto.setLrry(user.getYhid());
			Map<String,Object> map = new HashMap<>();
			boolean result = dsrwqxService.toSelected(dsrwqxDto);
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
	 */
	@RequestMapping(value ="/timing/pagedataToOptional")
	@ResponseBody
	public Map<String,Object> toOptional(DsrwqxDto dsrwqxDto){
		try{
			boolean result = dsrwqxService.toOptional(dsrwqxDto);
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

}
