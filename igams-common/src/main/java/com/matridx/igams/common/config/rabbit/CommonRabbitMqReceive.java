package com.matridx.igams.common.config.rabbit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.scheduled.CronTaskRegistrar;
import com.matridx.igams.common.service.svcinterface.ICommonRabbitService;
import com.matridx.igams.common.service.svcinterface.IDsrwszService;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IOutWardService;
import com.matridx.igams.common.service.svcinterface.IXxdlcwglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class CommonRabbitMqReceive {

	@Autowired
	ICommonRabbitService rabbitService;

	//@Autowired(required=false)
	//private AmqpTemplate amqpTempl;

	@Autowired
	IXxdlcwglService xxdlcwglService;
	@Autowired
	RedisUtil redisUtil;
	@Value("${matridx.rabbit.flg:}")
	private String rabbitFlg;
	@Value("${matridx.rabbit.receiveflg:1}")
	private String receiveflg;
	@Value("${matridx.systemflg.remindtype:}")
	private String remindtype;
	@Autowired
	private CronTaskRegistrar cronTaskRegistrar;
	@Autowired
	private IGrszService grszService;
	@Autowired
	IDsrwszService dsrwservice;
	@Value("${matridx.wechat.registerurl:}")
	private String registerurl;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.wechat.serverapplicationurl:}")
	private String serverapplicationurl;
	@Value("${matridx.wechat.tokenUrl:}")
	private String tokenUrl;
	@Value("${matridx.wechat.username:}")
	private String username;
	@Value("${matridx.wechat.password:}")
	private String password;
	@Autowired
	DingTalkUtil dingTalkUtil;
	private final Logger log = LoggerFactory.getLogger(CommonRabbitMqReceive.class);
	@Autowired(required=false)
	AmqpTemplate amqpTempl;

	@RabbitListener(queues = ("matridx.inspect.report.send"), containerFactory="defaultFactory")
	public void sendReport(String str) {
		String token = "";
		JSONObject jsonObject = JSONObject.parseObject(str);
		FjcfbDto fjcfbDto = JSONObject.parseObject(jsonObject.get("fjcfbDto").toString(),FjcfbDto.class);

		try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
			//获取服务器token
			HttpPost getTokenPost = new HttpPost(serverapplicationurl+tokenUrl);
			// 创建MultipartEntityBuilder来构建请求体
			MultipartEntityBuilder getTokenBuilder = MultipartEntityBuilder.create();
			getTokenBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			getTokenBuilder.addTextBody("username", username, ContentType.APPLICATION_JSON);
			getTokenBuilder.addTextBody("password", password, ContentType.APPLICATION_JSON);
			// 构建请求体
			org.apache.http.HttpEntity getTokenMultipart = getTokenBuilder.build();

			// 设置请求体到HttpPost对象
			getTokenPost.setEntity(getTokenMultipart);

			// 执行请求并获取响应
			CloseableHttpResponse getTokenresponse = httpClient.execute(getTokenPost);
			try {
				org.apache.http.HttpEntity getTokenResponseEntity = getTokenresponse.getEntity();
				if (getTokenResponseEntity != null) {
					JSONObject map  = JSONObject.parseObject(EntityUtils.toString(getTokenResponseEntity));
					token = map.get("access_token").toString();
				}else{
					log.error("获取token失败！");
				}
			} finally {
				getTokenresponse.close();
			}




			// 创建HttpPost对象
			HttpPost httpPost = new HttpPost(String.valueOf(jsonObject.get("url")));

			// 创建MultipartEntityBuilder来构建请求体
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			// 添加参数
			builder.addTextBody("impflg", fjcfbDto.getImpflg(), ContentType.APPLICATION_JSON);
			builder.addTextBody("lsbcdz", fjcfbDto.getLsbcdz(), ContentType.APPLICATION_JSON);
			builder.addTextBody("checkflg", fjcfbDto.getCheckFlg(), ContentType.APPLICATION_JSON);
			builder.addTextBody("fjid", fjcfbDto.getFjid(), ContentType.APPLICATION_JSON);
			builder.addTextBody("ywlx", fjcfbDto.getYwlx(), ContentType.APPLICATION_JSON);
			builder.addTextBody("wjm", fjcfbDto.getWjm(), ContentType.APPLICATION_JSON);
			builder.addTextBody("access_token", token, ContentType.APPLICATION_JSON);
			builder.addTextBody("lrry", fjcfbDto.getLrry(), ContentType.APPLICATION_JSON);
			// 添加文件附件
			File file = new File(fjcfbDto.getLsbcdz());
			builder.addBinaryBody("_file", file);

			// 构建请求体
			org.apache.http.HttpEntity multipart = builder.build();

			// 设置请求体到HttpPost对象
			httpPost.setEntity(multipart);

			// 执行请求并获取响应
			CloseableHttpResponse response = httpClient.execute(httpPost);
			try {
				org.apache.http.HttpEntity responseEntity = response.getEntity();
				if (responseEntity != null) {
					log.error(EntityUtils.toString(responseEntity));
				}
			} finally {
				response.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 系统用户新增，修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.xtyh.update${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertOrUpdateUser(String str) {
        try {
        	if("1".equals(receiveflg)) {
				User user = JSONObject.parseObject(str, User.class);
				if(!rabbitFlg.equals(user.getPrefix())){
					rabbitService.insertOrUpdateUser(user);
					XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
					xxdlcwglDto.setCwid(StringUtil.generateUUID());
					xxdlcwglDto.setCwlx("sys.igams.xtyh.update");
					if(str.length() > 4000){
						str = str.substring(0, 4000);
					}
					xxdlcwglDto.setYsnr(str);
					xxdlcwglService.insert(xxdlcwglDto);
				}
        	}
		} catch (Exception e) {
			log.error("Receive xtyh-update:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.xtyh.update");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}

	/**
	 * 钉钉发送失败时，比如短时间内发送次数超过限制，可以先发送到临时rabbit消息这边，延迟几分钟后再次进行发送。
	 * 在发送的时候还是发送到原来的消息队列中。当前只针对钉钉消息。
	 * @param message
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}wechar.dd.mesage.queue${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void ddMessage(String message){
		log.error("钉钉消息报错后的延时重发队列:"+message);
		amqpTempl.convertAndSend("send.message.dispose.exchange","Send.message.dispose", message);
	}
	
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}wechar.report.outside.send.queue${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void receiveMessage(String message){
		log.error("钉钉回调异常延迟队列------接收:receiveMessage--message"+message);
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) JSON.parse(message);
		String method= map.get("method");
		String parameters = map.get("parameters");
		if (StringUtil.isNotBlank(method) && StringUtil.isNotBlank(parameters)){
			try{
				IOutWardService outWardService = (IOutWardService)ServiceFactory.getService(method);
				if (outWardService != null) {
					outWardService.getInfoParameters(parameters);
				}
			}catch (Exception e){
				log.error("wechar.report.outside.send.queue:"+e.getMessage());
			}
		}
		String ddhd= map.get("ddhd");
		if(StringUtil.isNotBlank(ddhd) ){
			RestTemplate t_restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<JSONObject> request = new HttpEntity<JSONObject>(JSON.parseObject(JSON.toJSONString(map.get("json"))), headers);
			//boolean t_result = t_restTemplate.postForObject(registerurl + "/ws/callback", paramMap, boolean.class);
			t_restTemplate.postForObject(applicationurl + "/ws/callback?wbcxid="+map.get("wbcxid")+"&signature="+map.get("signature")+"&timestamp="+map.get("timestamp")+"&hdcs="+map.get("hdcs")+"&nonce="+map.get("nonce"), request, String.class);

		}
	}
	/*
		发送群消息
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}send.group.messages.queue${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void receiveToSendGroupMessages(String message){
		log.error("receiveToSendGroupMessages--message"+message);
		Map<String, String> map = JSONObject.parseObject(message, new TypeReference<>() {
		});
		//发送钉钉群消息
		String sendGroupMessage = map.get("sendGroupMessage");
		if(StringUtil.isNotBlank(sendGroupMessage) ){
			String yhid = map.get("yhid");
			String tzqcskz1 = map.get("tzqcskz1");
			String tznr = map.get("tznr");
			if (StringUtil.isNotBlank(yhid)&&StringUtil.isNotBlank(tzqcskz1)&&StringUtil.isNotBlank(tznr)){
				dingTalkUtil.sendGroupMessage(yhid, tzqcskz1, tznr);
			}
		}
	}

//	@RabbitListener(queues = ("xx_delay_queue"))
//	public void receiveDelayMessage(String message){
//		amqpTempl.convertAndSend("wechar.report.outside.send.exchange", "wechar.report.outside.send.key", message);
//	}
	
	/**
	 * 系统用户删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.xtyh.del${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delUser(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	User user = JSONObject.parseObject(str, User.class);
	        	if(!rabbitFlg.equals(user.getPrefix())){
	        		rabbitService.deleteUser(user);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.xtyh.del");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive xtyh-del:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.xtyh.del");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 系统角色新增、修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.xtjs.update${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertOrUpdateRole(String str) {
        try {
        	if("1".equals(receiveflg)) {
				Role role = JSONObject.parseObject(str, Role.class);
				if(!rabbitFlg.equals(role.getPrefix())){
					rabbitService.insertOrUpdateRole(role);
					XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
					xxdlcwglDto.setCwid(StringUtil.generateUUID());
					xxdlcwglDto.setCwlx("sys.igams.xtjs.update");
					if(str.length() > 4000){
						str = str.substring(0, 4000);
					}
					xxdlcwglDto.setYsnr(str);
					xxdlcwglService.insert(xxdlcwglDto);
				}
        	}
		} catch (Exception e) {
			log.error("Receive web-xtjs-update:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.xtjs.update");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 系统角色删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.xtjs.del${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delRole(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	Role role = JSONObject.parseObject(str, Role.class);
	        	if(!rabbitFlg.equals(role.getPrefix())){
	        		rabbitService.deleteRole(role);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.xtjs.del");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
	        }
		} catch (Exception e) {
			log.error("Receive xtjs-del:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.xtjs.del");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 用户机构权限新增
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.yhjgqx.insert${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertYhjgqx(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	List<User> users = JSONObject.parseArray(str, User.class);
	        	if(!rabbitFlg.equals(users.get(0).getPrefix())){
	        		rabbitService.insertYhjgqxs(users);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.yhjgqx.insert");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive yhjgqx-insert:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.yhjgqx.insert");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 用户机构权限删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.yhjgqx.del${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delYhjgqx(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	User user = JSONObject.parseObject(str, User.class);
	        	if(!rabbitFlg.equals(user.getPrefix())){
	        		rabbitService.deleteYhjgqx(user);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.yhjgqx.del");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive yhjgqx-del:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.yhjgqx.del");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 用户所属机构新增
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.yhssjg.insert${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertYhssjg(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	User user = JSONObject.parseObject(str, User.class);
	        	if(!rabbitFlg.equals(user.getPrefix())){
	        		rabbitService.deleteYhssjgByYhid(user);
	        		rabbitService.insertYhssjg(user);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.yhssjg.insert");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive yhssjg-insert:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.yhssjg.insert");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 用户所属机构删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.yhssjg.del${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delYhssjg(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	User user = JSONObject.parseObject(str, User.class);
	        	if(!rabbitFlg.equals(user.getPrefix())){
	        		rabbitService.deleteYhssjg(user);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.yhssjg.del");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive yhssjg-del:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.yhssjg.del");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 用户角色新增
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.yhjs.insert${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertYhjs(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	User user = JSONObject.parseObject(str, User.class);
	        	if(!rabbitFlg.equals(user.getPrefix())){
	        		rabbitService.insertYhjs(user);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.yhjs.insert");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive yhjs-insert:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.yhjs.insert");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 用户角色删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.yhjs.del${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delYhjs(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	User user = JSONObject.parseObject(str, User.class);
	        	if(!rabbitFlg.equals(user.getPrefix())){
	        		rabbitService.deleteYhjs(user);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.yhjs.del");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive yhjs-del:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.yhjs.del");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 审批岗位成员新增
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.spgwcy.insert${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertSpgwcy(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	List<SpgwcyDto> spgwcyList = JSONObject.parseArray(str, SpgwcyDto.class);
	        	if(!rabbitFlg.equals(spgwcyList.get(0).getPrefix())){
	        		rabbitService.insertSpgwcys(spgwcyList);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.spgwcy.insert");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive spgwcy-insert:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.spgwcy.insert");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 审批岗位成员删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.spgwcy.del${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delSpgwcy(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	User user = JSONObject.parseObject(str, User.class);
	        	if(!rabbitFlg.equals(user.getPrefix())){
	        		rabbitService.deleteSpgwcy(user);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.spgwcy.del");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive spgwcy-del:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.spgwcy.del");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 审批岗位成员批量删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.spgwcy.batchdel${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void batchDelSpgwcy(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	List<SpgwcyDto> spgwcyList = JSONObject.parseArray(str, SpgwcyDto.class);
	        	if(!rabbitFlg.equals(spgwcyList.get(0).getPrefix())){
	        		rabbitService.batchDelSpgwcy(spgwcyList);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.spgwcy.batchdel");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive spgwcy-batchdel:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.spgwcy.batchdel");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 机构信息新增
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.jgxx.update${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertOrUpdateJgxx(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	DepartmentDto departmentDto = JSONObject.parseObject(str, DepartmentDto.class);
	        	if(!rabbitFlg.equals(departmentDto.getPrefix())){
					departmentDto.setFbsbj(urlPrefix);
	        		rabbitService.insertOrUpdateJgxx(departmentDto);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.jgxx.update");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive jgxx-update:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.jgxx.update");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}

	/**
	 * 个人设置消息订阅设置
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.grsz.save${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void modAndSaveMessage(String str) {
		try {
			if("1".equals(receiveflg)) {
				GrszDto grszDto = JSONObject.parseObject(str, GrszDto.class);
				grszService.modSaveMessage(grszDto);
			}
		} catch (Exception e) {
			log.error("Receive jgxx-update:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.grsz.save");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	/**
	 * 机构信息修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.jgxx.updateList${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void modJgxxList(String str) {
        try {
        	if("1".equals(receiveflg)) {
				List<DepartmentDto> departmentDtos = JSONObject.parseArray(str, DepartmentDto.class);
				if(!rabbitFlg.equals(departmentDtos.get(0).getPrefix())){
	        		rabbitService.modJgxxList(departmentDtos);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.jgxx.updateList");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive jgxx-updateList:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.jgxx.updateList");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	/**
	 * 机构信息删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.jgxx.dels${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delJgxxList(String str) {
        try {
        	if("1".equals(receiveflg)) {
				DepartmentDto departmentDto = JSONObject.parseObject(str, DepartmentDto.class);
				if(!rabbitFlg.equals(departmentDto.getPrefix())){
	        		rabbitService.deleteByWbcxid(departmentDto);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.jgxx.dels");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive jgxx-dels:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.jgxx.dels");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 机构信息删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.jgxx.del${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delJgxx(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	DepartmentDto departmentDto = JSONObject.parseObject(str, DepartmentDto.class);
	        	if(!rabbitFlg.equals(departmentDto.getPrefix())){
	        		rabbitService.deleteJgxx(departmentDto);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.jgxx.del");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive jgxx-del:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.jgxx.del");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	/**
	 * 系统审核新增，修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.xtsh.update${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertOrUpdateXtsh(String str) {
        try {
        	if("1".equals(receiveflg)) {
				XtshDto xtshDto = JSONObject.parseObject(str, XtshDto.class);
				if(!rabbitFlg.equals(xtshDto.getPrefix())){
					rabbitService.insertOrUpdateXtsh(xtshDto);
					XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
					xxdlcwglDto.setCwid(StringUtil.generateUUID());
					xxdlcwglDto.setCwlx("sys.igams.xtsh.update");
					if(str.length() > 4000){
						str = str.substring(0, 4000);
					}
					xxdlcwglDto.setYsnr(str);
					xxdlcwglService.insert(xxdlcwglDto);
				}
        	}
		} catch (Exception e) {
			log.error("Receive xtsh-update:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.xtsh.update");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 系统审核删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.xtsh.del${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delXtsh(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	XtshDto xtshDto = JSONObject.parseObject(str, XtshDto.class);
	        	if(!rabbitFlg.equals(xtshDto.getPrefix())){
	        		rabbitService.deleteXtsh(xtshDto);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.xtsh.del");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive xtsh-del:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.xtsh.del");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
		/**
	 * 角色单位权限新增
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.jsdwqx.insert${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertJsdwqx(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	Role role = JSONObject.parseObject(str, Role.class);
	        	if(!rabbitFlg.equals(role.getPrefix())){
	        		rabbitService.insertJsdwqx(role);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.jsdwqx.insert");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive jsdwqx-insert:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.jsdwqx.insert");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 角色单位权限删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.jsdwqx.del${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delJsdwqx(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	Role role = JSONObject.parseObject(str, Role.class);
	        	if(!rabbitFlg.equals(role.getPrefix())){
	        		rabbitService.deleteJsdwqx(role);
	    			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	    			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	    			xxdlcwglDto.setCwlx("sys.igams.jsdwqx.del");
	    			if(str.length() > 4000){
	    				str = str.substring(0, 4000);
	    			}
	    			xxdlcwglDto.setYsnr(str);
	    			xxdlcwglService.insert(xxdlcwglDto);
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive jsdwqx-del:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.jsdwqx.del");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}

	/**
	 * 审批岗位新增，修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.spgw.update${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertOrUpdateSpgw(String str) {
        try {
        	if("1".equals(receiveflg)) {
				SpgwDto spgwDto = JSONObject.parseObject(str, SpgwDto.class);
				if(!rabbitFlg.equals(spgwDto.getPrefix())){
					rabbitService.insertOrUpdateSpgw(spgwDto);
					XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
					xxdlcwglDto.setCwid(StringUtil.generateUUID());
					xxdlcwglDto.setCwlx("sys.igams.spgw.update");
					if(str.length() > 4000){
						str = str.substring(0, 4000);
					}
					xxdlcwglDto.setYsnr(str);
					xxdlcwglService.insert(xxdlcwglDto);
				}
        	}
		} catch (Exception e) {
			log.error("Receive spgw-update:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.spgw.update");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 审批岗位删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.spgw.del${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delSpgw(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	SpgwDto spgwDto = JSONObject.parseObject(str, SpgwDto.class);
	        	if(!rabbitFlg.equals(spgwDto.getPrefix())){
	        		if(spgwDto.getIds()!=null&&!spgwDto.getIds().isEmpty()) {
	        			rabbitService.deleteSpgw(spgwDto);
	        			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
	        			xxdlcwglDto.setCwid(StringUtil.generateUUID());
	        			xxdlcwglDto.setCwlx("sys.igams.spgw.del");
	        			if(str.length() > 4000){
	        				str = str.substring(0, 4000);
	        			}
	        			xxdlcwglDto.setYsnr(str);
	        			xxdlcwglService.insert(xxdlcwglDto);
	        		}
	        	}
        	}
		} catch (Exception e) {
			log.error("Receive spgw-del:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.spgw.del");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 审批流程新增
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.shlc.insert${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void addShlc(String str) {
        try {
        	if("1".equals(receiveflg)) {
				List<SpgwDto> spgwlist = JSONObject.parseArray(str, SpgwDto.class);
				if(!rabbitFlg.equals(spgwlist.get(0).getPrefix())){
					rabbitService.insertBySpgwList(spgwlist);
					XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
					xxdlcwglDto.setCwid(StringUtil.generateUUID());
					xxdlcwglDto.setCwlx("sys.igams.shlc.insert");
					if(str.length() > 4000){
						str = str.substring(0, 4000);
					}
					xxdlcwglDto.setYsnr(str);
					xxdlcwglService.insert(xxdlcwglDto);
				}
        	}
		} catch (Exception e) {
			log.error("Receive shlc-insert:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.shlc.insert");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 审核流程更新
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.shlc.update${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updateShlc(String str) {
        try {
        	if("1".equals(receiveflg)) {
				SpgwDto spgwDto = JSONObject.parseObject(str, SpgwDto.class);
				if(!rabbitFlg.equals(spgwDto.getPrefix())){
					rabbitService.updateTysjByShid(spgwDto);
					XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
					xxdlcwglDto.setCwid(StringUtil.generateUUID());
					xxdlcwglDto.setCwlx("sys.igams.shlc.update");
					if(str.length() > 4000){
						str = str.substring(0, 4000);
					}
					xxdlcwglDto.setYsnr(str);
					xxdlcwglService.insert(xxdlcwglDto);
				}
        	}
		} catch (Exception e) {
			log.error("Receive shlc-update:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.shlc.update");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 审核类别新增
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.shlb.insert${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void addShlb(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	ShlbDto shlbDto = JSONObject.parseObject(str, ShlbDto.class);
				if(!rabbitFlg.equals(shlbDto.getPrefix())){
					rabbitService.insertShlb(shlbDto);
					XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
					xxdlcwglDto.setCwid(StringUtil.generateUUID());
					xxdlcwglDto.setCwlx("sys.igams.shlb.insert");
					if(str.length() > 4000){
						str = str.substring(0, 4000);
					}
					xxdlcwglDto.setYsnr(str);
					xxdlcwglService.insert(xxdlcwglDto);
				}
        	}
		} catch (Exception e) {
			log.error("Receive shlb-insert:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.shlb.insert");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 审核类别更新
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.shlb.update${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void updateShlb(String str) {
        try {
        	if("1".equals(receiveflg)) {
				ShlbDto shlbDto = JSONObject.parseObject(str, ShlbDto.class);
				if(!rabbitFlg.equals(shlbDto.getPrefix())){
					rabbitService.updateShlb(shlbDto);
					XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
					xxdlcwglDto.setCwid(StringUtil.generateUUID());
					xxdlcwglDto.setCwlx("sys.igams.shlb.update");
					if(str.length() > 4000){
						str = str.substring(0, 4000);
					}
					xxdlcwglDto.setYsnr(str);
					xxdlcwglService.insert(xxdlcwglDto);
				}
        	}
		} catch (Exception e) {
			log.error("Receive shlb-update:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.shlb.update");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 审核类别删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.shlb.del${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delShlb(String str) {
        try {
        	if("1".equals(receiveflg)) {
				ShlbDto shlbDto = JSONObject.parseObject(str, ShlbDto.class);
				if(!rabbitFlg.equals(shlbDto.getPrefix())){
					if(shlbDto.getIds()!=null&&!shlbDto.getIds().isEmpty()) {
						rabbitService.deleteShlb(shlbDto);
						XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
						xxdlcwglDto.setCwid(StringUtil.generateUUID());
						xxdlcwglDto.setCwlx("sys.igams.shlb.del");
						if(str.length() > 4000){
							str = str.substring(0, 4000);
						}
						xxdlcwglDto.setYsnr(str);
						xxdlcwglService.insert(xxdlcwglDto);
					}
				}
        	}
		} catch (Exception e) {
			log.error("Receive shlb-del:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.shlb.del");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 基础数据新增或修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.jcsj.update${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void addOrModJcsj(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	JcsjDto jcsjDto = JSONObject.parseObject(str, JcsjDto.class);
				if(!rabbitFlg.equals(jcsjDto.getPrefix())){
					rabbitService.insertOrUpdateJcsj(jcsjDto);
					XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
					xxdlcwglDto.setCwid(StringUtil.generateUUID());
					xxdlcwglDto.setCwlx("sys.igams.jcsj.update");
					if(str.length() > 4000){
						str = str.substring(0, 4000);
					}
					xxdlcwglDto.setYsnr(str);
					xxdlcwglService.insert(xxdlcwglDto);
				}
        	}
		} catch (Exception e) {
			log.error("Receive jcsj-update:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.jcsj.update");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 基础数据新增或修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.jcsj.del${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void delJcsj(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	JcsjDto jcsjDto = JSONObject.parseObject(str, JcsjDto.class);
				if(!rabbitFlg.equals(jcsjDto.getPrefix())){
					rabbitService.deleteJcsj(jcsjDto);
					XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
					xxdlcwglDto.setCwid(StringUtil.generateUUID());
					xxdlcwglDto.setCwlx("sys.igams.jcsj.del");
					if(str.length() > 4000){
						str = str.substring(0, 4000);
					}
					xxdlcwglDto.setYsnr(str);
					xxdlcwglService.insert(xxdlcwglDto);
				}
        	}
		} catch (Exception e) {
			log.error("Receive jcsj-del:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.jcsj.del");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	/**
	 * 用户签名新增或修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.xtyh.sign${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void userSign(String str) {
        try {
        	if("1".equals(receiveflg)) {
	        	FjcfbDto fjcfbDto = JSONObject.parseObject(str, FjcfbDto.class);
				if(!rabbitFlg.equals(fjcfbDto.getPrefix())){
					rabbitService.userSign(fjcfbDto);
					XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
					xxdlcwglDto.setCwid(StringUtil.generateUUID());
					xxdlcwglDto.setCwlx("sys.igams.xtyh.sign");
					if(str.length() > 4000){
						str = str.substring(0, 4000);
					}
					xxdlcwglDto.setYsnr(str);
					xxdlcwglService.insert(xxdlcwglDto);
				}
        	}
		} catch (Exception e) {
			log.error("Receive xtyh-sign:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.xtyh.sign");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	
	/*----------------------------------------    整合rabbit    ----------------------------------------*/
	
	//用户信息同步整合
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.user.operate${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void userOperate(String str) {
        try {
        	if("1".equals(receiveflg) && StringUtil.isNotBlank(str) && str.length() > 8) {
        		String lx = str.substring(0,8);
        		String msg = str.substring(8);
        		if(RabbitEnum.YHJS_ADD.getCode().equals(lx)) {
        			log.error("Receive YHJS_ADD");
        			User user = JSONObject.parseObject(msg, User.class);
        			if(!rabbitFlg.equals(user.getPrefix()))
        				rabbitService.insertYhjs(user);
            	}else if(RabbitEnum.YHJS_DEL.getCode().equals(lx)) {
            		log.error("Receive YHJS_DEL");
            		User user = JSONObject.parseObject(msg, User.class);
            		if(!rabbitFlg.equals(user.getPrefix()))
            			rabbitService.deleteYhjs(user);
            	}else if(RabbitEnum.SSJG_ADD.getCode().equals(lx)) {
            		log.error("Receive SSJG_ADD");
            		User user = JSONObject.parseObject(msg, User.class);
            		if(!rabbitFlg.equals(user.getPrefix())) {
            			rabbitService.deleteYhssjgByYhid(user);
                		rabbitService.insertYhssjg(user);
            		}
            	}else if(RabbitEnum.SSJG_MOD.getCode().equals(lx)) {
            		log.error("Receive SSJG_MOD");
            		User user = JSONObject.parseObject(msg, User.class);
            		if(!rabbitFlg.equals(user.getPrefix())) {
            			rabbitService.updateYhssjg(user);
            		}
            	}else if(RabbitEnum.GWCY_DEL.getCode().equals(lx)) {
            		log.error("Receive GWCY_DEL");
            		List<SpgwcyDto> spgwcyList = JSONObject.parseArray(msg, SpgwcyDto.class);
            		if(!rabbitFlg.equals(spgwcyList.get(0).getPrefix()))
            			rabbitService.batchDelSpgwcy(spgwcyList);
            	}else if(RabbitEnum.GWCY_ADD.getCode().equals(lx)) {
            		log.error("Receive GWCY_ADD");
            		List<SpgwcyDto> spgwcyList = JSONObject.parseArray(msg, SpgwcyDto.class);
            		if(!rabbitFlg.equals(spgwcyList.get(0).getPrefix()))
            			rabbitService.insertSpgwcys(spgwcyList);
            	}else if(RabbitEnum.JGQX_ADD.getCode().equals(lx)) {
            		log.error("Receive JGQX_ADD");
            		List<User> users = JSONObject.parseArray(msg, User.class);
            		if(!rabbitFlg.equals(users.get(0).getPrefix()))
            			rabbitService.insertYhjgqxs(users);
            	}else if(RabbitEnum.XTYH_MOD.getCode().equals(lx)) {
            		log.error("Receive XTYH_MOD");
            		User user = JSONObject.parseObject(msg, User.class);
            		if(!rabbitFlg.equals(user.getPrefix()))
            			rabbitService.insertOrUpdateUser(user);
            	}else if(RabbitEnum.CKQX_MOD.getCode().equals(lx)) {
					log.error("Receive CKQX_MOD");
					Role role = JSONObject.parseObject(msg, Role.class);
					if(!rabbitFlg.equals(role.getPrefix()))
						rabbitService.updateCkqxByjsid(role);
				}else if(RabbitEnum.XTYH_MODLIST.getCode().equals(lx)) {
					log.error("Receive XTYH_MODLIST");
					List<User> users= JSONObject.parseArray(msg, User.class);
					if(!rabbitFlg.equals(users.get(0).getPrefix())){
						rabbitService.updateUsers(users);
					}
				}else if(RabbitEnum.XTYH_ADDLIST.getCode().equals(lx)) {
					log.error("Receive XTYH_ADDLIST");
					List<User> users= JSONObject.parseArray(msg, User.class);
					if(!rabbitFlg.equals(users.get(0).getPrefix())){
						rabbitService.insertUsers(users);
					}
				}else if(RabbitEnum.HMC_ADD.getCode().equals(lx)) {
					log.error("Receive HMC_ADD");
					List<Map<String, String>> maps = JSONObject.parseObject(msg, new TypeReference<>() {
					});
					if(!rabbitFlg.equals(String.valueOf(maps.get(0).get("prefix")))){
						rabbitService.insertYghmcDtos(maps);
					}
				}else if(RabbitEnum.HMC_MOD.getCode().equals(lx)) {
					log.error("Receive HMC_MOD");
					List<Map<String, String>> maps = JSONObject.parseObject(msg, new TypeReference<>() {
					});
					if(!rabbitFlg.equals(String.valueOf(maps.get(0).get("prefix")))){
						rabbitService.updateYghmcDtos(maps);
					}
				}else if(RabbitEnum.HMC_PRO.getCode().equals(lx)) {
					log.error("Receive HMC_PRO");
					Map<String,Object> map = JSONObject.parseObject(msg, new TypeReference<>() {
					});
					if(!rabbitFlg.equals(String.valueOf(map.get("prefix")))){
						rabbitService.updateDqtxByIds(map);
					}
				}else if(RabbitEnum.YHT_ADD.getCode().equals(lx)) {
					log.error("Receive YHT_ADD");
					List<Map<String, String>> maps = JSONObject.parseObject(msg, new TypeReference<>() {
					});
					if(!rabbitFlg.equals(String.valueOf(maps.get(0).get("prefix")))){
						rabbitService.insertYghtxxDtos(maps);
					}
				}else if(RabbitEnum.YLZ_ADD.getCode().equals(lx)) {
					log.error("Receive YLZ_ADD");
					List<Map<String, String>> maps = JSONObject.parseObject(msg, new TypeReference<>() {
					});
					if(!rabbitFlg.equals(String.valueOf(maps.get(0).get("prefix")))){
						rabbitService.insertYglzxxDtos(maps);
					}
				}else if(RabbitEnum.YHT_DEL.getCode().equals(lx)) {
					log.error("Receive YHT_DEL");
					Map<String,Object> map = JSON.parseObject(msg, new TypeReference<>() {
					});
					if(!rabbitFlg.equals(String.valueOf(map.get("prefix")))){
						rabbitService.deleteYghtxx(map);
					}
				}else if(RabbitEnum.YLZ_DEL.getCode().equals(lx)) {
					log.error("Receive YLZ_DEL");
					Map<String,Object> map = JSON.parseObject(msg, new TypeReference<>() {
					});
					if(!rabbitFlg.equals(String.valueOf(map.get("prefix")))){
						rabbitService.deleteYglzxx(map);
					}
				}else if(RabbitEnum.YHQT_ADD.getCode().equals(lx)) {
					log.error("Receive YHQT_ADD");
					User user = JSONObject.parseObject(msg, User.class);
					if(!rabbitFlg.equals(user.getPrefix())) {
						rabbitService.insertYhqtxx(user);
					}
				}
        		XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
				xxdlcwglDto.setCwid(StringUtil.generateUUID());
				xxdlcwglDto.setCwlx("sys.igams.user.operate");
				if(str.length() > 4000){
					str = str.substring(0, 4000);
				}
				xxdlcwglDto.setYsnr(str);
				xxdlcwglService.insert(xxdlcwglDto);
        	}
		} catch (Exception e) {
			log.error("Receive user_operate:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.user.operate");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.task.taskHandle${matridx.rabbit.flg:}${matridx.systemflg.remindflg:1}"),containerFactory="defaultFactory")
	public void taskHandle(String str){
		try {
			Map<String,Object>map=JSONObject.parseObject(str);
			if(map.get("type")!=null&&map.get("dsrwid")!=null){
				DsrwszDto t_dsrwszDto=new DsrwszDto();
				t_dsrwszDto.setRwid(map.get("dsrwid").toString());
				DsrwszDto dsrwszDto=dsrwservice.getDto(t_dsrwszDto);
				if (StringUtil.isNotBlank(remindtype) && !Arrays.asList(remindtype.split(",")).contains(dsrwszDto.getRemindtype())){
					cronTaskRegistrar.removeTriggerTask(t_dsrwszDto.getRwid());
					return;
				}
				switch (map.get("type").toString()){
					case "add":
							cronTaskRegistrar.addTriggerTask(dsrwszDto);
						break;
					case "update":
						cronTaskRegistrar.removeTriggerTask(t_dsrwszDto.getRwid());
						cronTaskRegistrar.addTriggerTask(dsrwszDto);
						break;
					case "del":
						cronTaskRegistrar.removeTriggerTask(t_dsrwszDto.getRwid());
						break;
				}
			}
		} catch (Exception e) {
			log.error("taskHandle error:" + e.toString());
		}

	}
}
