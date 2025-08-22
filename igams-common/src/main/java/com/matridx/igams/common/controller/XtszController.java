package com.matridx.igams.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.redis.RedisUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;

@Controller
@RequestMapping("/systemmain")
public class XtszController extends BaseController{
	
	@Autowired
	private IXtszService xtszService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	private IXxglService xxglService;
	@Autowired
	private RedisUtil redisUtil;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	@Autowired
	private RabbitTemplate rabbitTemplate;
	@Value("${spring.rabbitmq.password:}")
	private String password;
	@Value("${spring.rabbitmq.username:}")
	private String username;
	@Autowired
	private RabbitAdmin rabbitAdmin;
	
	/**
	 * 跳转
	 */
	@RequestMapping("/xtsz/pageList")
	public ModelAndView PageList()
	{
		ModelAndView mav = new ModelAndView("systemmain/xtsz/xtsz_list");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 系统设置列表
	 */
	@RequestMapping("/xtsz/pagedataListXtsz")
	@ResponseBody
	public Map<String, Object> getPagedDtoList(XtszDto xtszDto){
		Map<String,Object> map=new HashMap<>();
		List<XtszDto> xtszList = xtszService.getPagedDtoList(xtszDto);
		map.put("total",xtszDto.getTotalNumber());
		map.put("count",rabbitAdmin.getQueueProperties("retry.fail.queue").get("QUEUE_MESSAGE_COUNT"));
		map.put("rows",xtszList);
		return map;
	}
	
	/**
	 * 新增系统设置的页面
	 */
	@RequestMapping("/xtsz/addXtsz")
	public ModelAndView addXtsz(XtszDto xtszDto){
		ModelAndView mav=new ModelAndView("systemmain/xtsz/xtsz_add");
		xtszDto.setFormAction("addSaveXtsz");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("xtszDto", xtszDto);
		return mav;
	}


	/**
	 * 获取异常rabbit数据
	 */ 
	@RequestMapping("/xtsz/rabbitXtsz")
	public ModelAndView rabbitXtsz(){
		ModelAndView mav=new ModelAndView("systemmain/xtsz/xtsz_rabbit");
		Message message = rabbitTemplate.receive("retry.fail.queue" );
		if(null != message){
			mav.addObject("message",message.getMessageProperties().getHeaders().get("x-exception-message"));
			mav.addObject("exchange",message.getMessageProperties().getHeaders().get("x-original-exchange"));
			mav.addObject("routingKey",message.getMessageProperties().getHeaders().get("x-original-routingKey"));
			mav.addObject("body",new String(message.getBody()));
		}

//		try {
//			ConnectionFactory connectionFactory = rabbitTemplate.getConnectionFactory();
//			QueryMsgFromMq client = new QueryMsgFromMq("http://"+username+":"+password+"@"+connectionFactory.getHost()+":15672/api/");
//			String vhost = "/";
//			String queuename = "retry.fail.queue";
//			List<Object> list = client.queryMessageFromMq(vhost, queuename, "100", connectionFactory.getHost());
//			List<Map<String,Object>> mapList = new ArrayList<>();
//			for (Object entry : list) {
//				if (entry instanceof Map) {
//					Map<String,Object> map = new HashMap<>();
//					Map<String,Object> properties = (Map)((Map) entry).get("properties");
//					Map<String,Object> headers = (Map)properties.get("headers");
//					map.put("payload",((Map) entry).get("payload"));
//					map.put("message",headers.get("x-exception-message"));
//					map.put("exchange",headers.get("x-original-exchange"));
//					map.put("routingKey",headers.get("x-original-routingKey"));
//					map.put("priority",properties.get("priority"));
//					map.put("delivery",properties.get("delivery_mode"));
//					mapList.add(map);
//				}
//			}
//			mav.addObject("list", mapList);
//		} catch (MalformedURLException | URISyntaxException e) {
//			e.printStackTrace();
//		}


//		        <dependency>
//            <groupId>com.rabbitmq</groupId>
//            <artifactId>http-client</artifactId>
//            <version>3.1.1.RELEASE</version>
//        </dependency>
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * rabbit消息
	 */
	@RequestMapping(value="/xtsz/pagedataRabbitInfo")
	@ResponseBody
	public Map<String, Object> pagedataRabbitInfo(){
		Map<String, Object> map=new HashMap<>();
		Message message = rabbitTemplate.receive("retry.fail.queue" );
		if(null != message){
			map.put("message",message.getMessageProperties().getHeaders().get("x-exception-message"));
			map.put("exchange",message.getMessageProperties().getHeaders().get("x-original-exchange"));
			map.put("routingKey",message.getMessageProperties().getHeaders().get("x-original-routingKey"));
			map.put("body",new String(message.getBody()));
			map.put("status","success");
		}
		map.put("info","数据已全部处理完成！");
		return map;
	}

	/**
	 * rabbit消息发送
	 */
	@RequestMapping(value="/xtsz/rabbitSaveXtsz")
	@ResponseBody
	public Map<String, Object> rabbitSaveXtsz(String exchange,String routingKey,String body){
		Map<String, Object> map=new HashMap<>();
		rabbitTemplate.convertAndSend(exchange, routingKey,body);
		map.put("status","success");
		map.put("message","发送成功！");
		return map;
	}
	
	/**
	    * 增加系统设置信息到数据库
	 */
	@RequestMapping(value="/xtsz/addSaveXtsz")
	@ResponseBody
	public Map<String, Object> addSaveXtsz(XtszDto xtszDto){
		Map<String, Object> map=new HashMap<>();
		boolean isSuccess;
		int num = xtszService.getCount(xtszDto);
		if(num>0) {
			map.put("status", "fail");
			map.put("message", "设置类别已经存在，请核实后添加！");
		}else if(num==0) {
			isSuccess=xtszService.insert(xtszDto);
			//redis同步存入一条系统设置数据
			redisUtil.hset("matridx_xtsz", xtszDto.getSzlb(), JSONObject.toJSONString(xtszDto),-1);

			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	
	/**
	 * 系统设置信息修改页面
	 */
	@RequestMapping(value="/xtsz/modXtsz")
	public ModelAndView modXtsz(XtszDto xtszDto){
		ModelAndView mav=new ModelAndView("systemmain/xtsz/xtsz_mod");
		xtszDto = xtszService.getDto(xtszDto);
		xtszDto.setFormAction("modSaveXtsz");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("xtszDto", xtszDto);
		return mav;
	}
	/**
	 * 修改系统设置信息
	 */
	@RequestMapping(value="/xtsz/modSaveXtsz")
	@ResponseBody
	public Map<String,Object> modSaveXtsz(XtszDto xtszDto){
		Map<String,Object> map = new HashMap<>();
		//查询设置类别是否存在
		if(!xtszDto.getOldszlb().equals(xtszDto.getSzlb())) {
			XtszDto dto = xtszService.getDto(xtszDto);
			if(dto!=null) {
				map.put("status", "fail");
				map.put("message", "设置类别已存在");
				return map;
			}
		}
		boolean isSuccess = xtszService.update(xtszDto);
		//redis同步修改一条数据
		boolean exist = redisUtil.hHasKey("matridx_xtsz", xtszDto.getOldszlb());
		if(exist) {
			redisUtil.hdel("matridx_xtsz", xtszDto.getOldszlb());
			redisUtil.hset("matridx_xtsz", xtszDto.getSzlb(), JSONObject.toJSONString(xtszDto),-1);
		}else {
			redisUtil.hset("matridx_xtsz", xtszDto.getSzlb(), JSONObject.toJSONString(xtszDto),-1);
		}

		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 查看系统设置信息
	 */
	@RequestMapping(value="/xtsz/viewXtsz")
	public ModelAndView ViewStsz(XtszDto xtszDto){
		ModelAndView mav=new ModelAndView("systemmain/xtsz/xtsz_view");
		xtszDto = xtszService.getDto(xtszDto);
		mav.addObject("xtszDto", xtszDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 删除系统设置信息
	 */
	@RequestMapping(value="/xtsz/delXtsz")
	@ResponseBody
	public Map<String,Object> deleteXtsz(XtszDto xtszDto){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = xtszService.delete(xtszDto);
		if (xtszDto.getIds()!=null && !xtszDto.getIds().isEmpty()){
			for(int i=0 ; i<xtszDto.getIds().size() ; i++) {
				boolean exist = redisUtil.hHasKey("matridx_xtsz", xtszDto.getIds().get(i));
				if(exist) {
					redisUtil.hdel("matridx_xtsz", xtszDto.getIds().get(i));
				}
			}
		}
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
}
