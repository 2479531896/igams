package com.matridx.igams.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.WbaqyzDto;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IWbaqyzService;
import com.matridx.igams.web.dao.entities.WbcxDto;
import com.matridx.igams.web.dao.entities.YhjsDto;
import com.matridx.igams.web.dao.post.IWbcxDao;
import com.matridx.igams.web.dao.post.IYhjsDao;
import com.matridx.igams.web.service.svcinterface.IBbxxService;
import com.matridx.igams.web.service.svcinterface.IXtyhService;
import com.matridx.igams.wechat.dao.entities.WbhbyzDto;
import com.matridx.igams.wechat.service.svcinterface.IWbhbyzService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequestMapping("/initXtyh")
public class XtyhRedisInitController implements ApplicationRunner{
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private IXtyhService xtyhService;
	@Autowired
	private IBbxxService bbxxService;

	@Value("${matridx.systemflg.redisreset:}")
	private String redisreset;
	@Autowired
	IYhjsDao yhjsDao;
	@Autowired
	IWbcxDao wbcxDao;
	@Autowired
	ICommonDao commonDao;
	@Autowired
	private IWbhbyzService wbhbyzService;
	@Autowired
	private IWbaqyzService wbaqyzService;

	private final Logger log = LoggerFactory.getLogger(XtyhRedisInitController.class);

	@RequestMapping("/redis")
	@ResponseBody
	public void xtyhRedisInit() {
		log.error("---------------系统用户存储redis执行开始---------------");
    	//清除系统用户的所有键
		redisUtil.del("Users");
		redisUtil.del("accessToken");
		redisUtil.del("WbcxInfo");
		redisUtil.del("Users_Xtqx");
		redisUtil.del("Users_QxModel");
		redisUtil.del("Users_NobtnlistModels");
		redisUtil.del("Users_DingdingMenu");
		redisUtil.del("ClientDtos");
		redisUtil.del("weekLeadStatis");
		redisUtil.delLike("Users_WbQxModel:");

		List<QxModel> qxModelList_common=commonDao.getWbQxModelListByCommon();
		List<QxModel> qxModelList_jsid=commonDao.getWbQxModelListByJsid();

		Map<String, List<QxModel>> map = qxModelList_jsid.stream().collect(Collectors.groupingBy(QxModel::getJsid));
		if(map!=null&&map.size()>0){
			Set<String> stringSet=map.keySet();
			for(String key:stringSet){
				List<QxModel> redisList=map.get(key);
				redisList.addAll(qxModelList_common);
				redisUtil.set("Users_WbQxModel:" + key, JSONObject.toJSONString(redisList), -1);
			}
		}


		List<WbcxDto> dtoList = wbcxDao.getDtoList(new WbcxDto());
		for (WbcxDto wbcxDto : dtoList) {
			redisUtil.hset("WbcxInfo",wbcxDto.getWbcxid(), JSONObject.toJSONString(wbcxDto),-1);
		}

		//查询所有用户的权限信息
		YhjsDto searchDto = new YhjsDto();
		List<YhjsDto> yhqxDtos = yhjsDao.getDtoList(searchDto);
        //重新加载系统用户到redis中
		List<User> user1List = xtyhService.getUsersList();// 所有的用户数据
		List<User> user2List = xtyhService.getUsersByDdORWechatList();// 所有钉钉或者微信用户
		List<User> user3List = xtyhService.getWbaqUsersList();// 所有外部安全验证用户
		List<String> xtqxList = new ArrayList<>();//系统权限
		List<String> zyqxList = new ArrayList<>();//资源权限
		for (User user:user1List) {
			if(user!=null&& StringUtil.isNotBlank(user.getYhm())){			
				xtyhService.getRunAuthorByUser(user,xtqxList,zyqxList,yhqxDtos);
				User redisUser = new User();
				redisUser.setYhm(user.getYhm());
				redisUser.setZsxm(user.getZsxm());
				redisUser.setSfsd(user.getSfsd());
				redisUser.setSdtime(user.getSdtime());
				redisUser.setJstime(user.getJstime());
				redisUser.setYhid(user.getYhid());
				redisUser.setDqjs(user.getDqjs());
				redisUser.setDqjsmc(user.getDqjsmc());
				redisUser.setJsmcs(user.getJsmcs());
				redisUser.setDqjsdwxdbj(user.getDqjsdwxdbj());
				redisUser.setJgid(user.getJgid());
				redisUser.setDdid(user.getDdid());
				redisUser.setWechatid(user.getWechatid());
				redisUser.setMm(user.getMm());
				redisUser.setMmxgsj(user.getMmxgsj());
				redisUser.setGwmc(user.getGwmc());
				redisUser.setDdtxlj(user.getDdtxlj());
				redisUser.setEmail(user.getEmail());
				redisUser.setUnionid(user.getUnionid());
				redisUser.setWbcxid(user.getWbcxid());
				redisUtil.hset("Users", user.getYhm(), JSON.toJSONString(redisUser),-1);
			}
		}
		redisUtil.del("Users_WeChatMenu");
		for (User user:user2List) {
			if(user!=null && StringUtil.isNotBlank(user.getClient_id())){
				
				//存入外部用户标记,区分网页端登陆和钉钉或者微信登陆
				user.setLoginFlg("1");
				//获取角色信息
				xtyhService.getRunJsInfoByUser(user,yhqxDtos);
				//判断当前角色是否为null
				if(StringUtil.isNotBlank(user.getDqjs())) {
					//判断当前角色是否在角色资源权限为空的list中，如果在，不去查询
					if(!zyqxList.contains(user.getDqjs())) {
						xtyhService.getAuthor(user,zyqxList);
					}		
					//外部用户存放系统资源
					xtyhService.getWbzyqx(user, "3");
				}
				
				User redisUser = new User();
				redisUser.setYhm(user.getYhm());
				redisUser.setZsxm(user.getZsxm());
				redisUser.setSfsd(user.getSfsd());
				redisUser.setSdtime(user.getSdtime());
				redisUser.setJstime(user.getJstime());
				//redisUser.setJsids(user.getJsids());
				redisUser.setJsmcs(user.getJsmcs());
				redisUser.setYhid(user.getYhid());
				redisUser.setDqjs(user.getDqjs());
				redisUser.setDqjsdm(user.getDqjsdm());
				redisUser.setDqjsmc(user.getDqjsmc());
				redisUser.setDqjsdwxdbj(user.getDqjsdwxdbj());
				redisUser.setDdid(user.getDdid());
				redisUser.setJgid(user.getJgid());
				redisUser.setMm(user.getMm());
				redisUser.setMmxgsj(user.getMmxgsj());
				redisUser.setGwmc(user.getGwmc());
				redisUser.setDdtxlj(user.getDdtxlj());
				redisUser.setEmail(user.getEmail());
				redisUser.setUnionid(user.getUnionid());
				//因为是用微信ID或 钉钉ID登录的，所以需要设置标记
				redisUser.setLoginFlg(user.getLoginFlg());
				redisUser.setWbcxid(user.getWbcxid());
				redisUtil.hset("Users", user.getClient_id(), JSON.toJSONString(redisUser),-1);
			}
		}

		for (User user:user3List) {
			if(user!=null&& StringUtil.isNotBlank(user.getYhm())){
				User redisUser = new User();
				redisUser.setClient_id(user.getClient_id());
				redisUser.setYhm(user.getYhm());
				redisUser.setZsxm(user.getZsxm());
				redisUser.setMm(user.getMm());
				redisUtil.hset("Users", user.getYhm(), JSON.toJSONString(redisUser),-1);
			}
		}
		log.error("---------------系统用户存储redis执行结束---------------");
	}

	@RequestMapping("/redisBbxx")
	@ResponseBody
	public void bbxxRedisInit() {
		log.error("---------------版本信息存储redis执行开始---------------");
		//清除版本信息的所有键
		//redisUtil.del("List_matridx_bbxx");
		//重新加载版本信息到redis中(取最近3条)(getBbxxDtoList方法中有redis存放的方法)
		bbxxService.getBbxxDtoList();
		log.error("---------------版本信息存储redis执行结束---------------");
	}
	@RequestMapping("/redisWbhbyz")
	@ResponseBody
	public void wbhbyzRedisInit() {
		log.error("---------------外部伙伴验证存储redis执行开始---------------");
		redisUtil.del("Wbhbyz:");
		List<WbhbyzDto> sjhbAll = wbhbyzService.getSjhbAll();
		// 将sjhbAll,已code为key,分组存入redis
		Map<String, List<WbhbyzDto>> map = sjhbAll.stream().collect(Collectors.groupingBy(WbhbyzDto::getCode));
		if(map!=null&&map.size()>0){
			Set<String> stringSet=map.keySet();
			for(String key:stringSet){
				List<WbhbyzDto> redisList=map.get(key);
				redisUtil.set("Wbhbyz:" + key, JSONObject.toJSONString(redisList), 21600);
			}
		}

		log.error("---------------外部伙伴验证存储redis执行结束---------------");
	}

	@RequestMapping("/redisWbaqyz")
	@ResponseBody
	public void wbaqyzRedisInit() {
		log.error("---------------外部安全验证存储redis执行开始---------------");
		redisUtil.del("Wbaqyz:");
		WbaqyzDto wbaqyzDto = new WbaqyzDto();
		List<WbaqyzDto> aqyzAll = wbaqyzService.getDtoList(wbaqyzDto);
		// 将sjhbAll,已code为key,分组存入redis
		if(aqyzAll!=null&&aqyzAll.size()>0){
			for(WbaqyzDto t_aqyzDto:aqyzAll){
				redisUtil.set("Wbaqyz:" + t_aqyzDto.getCode(), JSONObject.toJSONString(t_aqyzDto), -1);
			}
		}

		log.error("---------------外部安全验证存储redis执行结束---------------");
	}

	@Override
	public void run(ApplicationArguments args) {
		if ("1".equals(redisreset) || "".equals(redisreset)) {
			xtyhRedisInit();
			bbxxRedisInit();
			wbaqyzRedisInit();
			wbhbyzRedisInit();
		}
	}
}
