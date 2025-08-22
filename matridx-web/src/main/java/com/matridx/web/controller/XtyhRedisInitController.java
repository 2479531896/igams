package com.matridx.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.web.dao.post.IYhjsDao;
import com.matridx.web.service.svcinterface.IXtyhService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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

	@Value("${matridx.systemflg.redisreset:}")
	private String redisreset;
	@Autowired
	IYhjsDao yhjsDao;
	@Autowired
	ICommonDao commonDao;
	private final Logger log = LoggerFactory.getLogger(XtyhRedisInitController.class);

	@RequestMapping("/redis")
	@ResponseBody
	public void xtyhRedisInit() {
		log.error("---------------系统用户存储redis执行开始---------------");
    	//清除系统用户的所有键
		redisUtil.del("Users");
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

        //重新加载系统用户到redis中
		List<User> user1List = xtyhService.getUsersList();// 所有的用户数据
		List<User> user3List = xtyhService.getWbaqUsersList();// 所有外部安全验证用户
		for (User user:user1List) {
			if(user!=null&& StringUtil.isNotBlank(user.getYhm())){			
				User redisUser = new User();
				redisUser.setYhm(user.getYhm());
				redisUser.setZsxm(user.getZsxm());
				redisUser.setSfsd(user.getSfsd());
				redisUser.setSdtime(user.getSdtime());
				redisUser.setJstime(user.getJstime());
				redisUser.setYhid(user.getYhid());
				redisUser.setDqjs(user.getDqjs());
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

		for (User user:user3List) {
			if(user!=null&& StringUtil.isNotBlank(user.getYhm())){
				User redisUser = new User();
				redisUser.setYhm(user.getYhm());
				redisUser.setZsxm(user.getZsxm());
				redisUser.setMm(user.getMm());
				redisUtil.hset("Users", user.getYhm(), JSON.toJSONString(redisUser),-1);
			}
		}
		log.error("---------------系统用户存储redis执行结束---------------");
	}

	@Override
	public void run(ApplicationArguments args) {
		if ("1".equals(redisreset) || "".equals(redisreset)) {
			xtyhRedisInit();
		}
	}
}
