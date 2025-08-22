package com.matridx.server.wechat.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.matridx.igams.common.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.SjdwxxDto;
import com.matridx.server.wechat.dao.entities.SjdwxxModel;
import com.matridx.server.wechat.dao.post.ISjdwlsxxDao;
import com.matridx.server.wechat.dao.post.ISjdwxxDao;
import com.matridx.server.wechat.service.svcinterface.ISjdwxxService;
import com.matridx.server.wechat.util.WeChatUtils;

@Service
public class SjdwxxServiceImpl extends BaseBasicServiceImpl<SjdwxxDto, SjdwxxModel, ISjdwxxDao> implements ISjdwxxService{
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	private ISjdwlsxxDao lsdwdao;
	@Autowired(required = false)
	private Logger log = LoggerFactory.getLogger(WeChatServiceImpl.class);
	@Autowired
	private ISjdwxxDao sjdwxxdao;
	@Autowired
	RedisUtil redisUtil;
		
	@Override
	public List<SjdwxxDto> getSjdwxx(){
		// TODO Auto-generated method stub
		return sjdwxxdao.getSjdwxx();
	}
	/**
	 * 获取所有科室
	 * @return
	 */
	@Override
	public List<SjdwxxDto> getAllSjdwxx(){
		return dao.getAllSjdwxx();
	}

	/**
	* 获取本地推送医院信息
	* 
	* @param request
	* @return
	*/
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean createHis(HttpServletRequest request){
		// 获取传过来的信息 并转换为JSON
		String sign = request.getParameter("sign");
		log.info("sign:" + sign);
		boolean checkSign = WeChatUtils.checkSign(sign,redisUtil);
		if (!checkSign){
			return false;
		}
		String jsonObj2 = request.getParameter("sjdwTreeList");
		JSONArray jsonObj = JSONObject.parseArray(jsonObj2);
		/* 查询送检单位信息 */
		List<SjdwxxDto> lsdwList = getAllSjdwxx();
		/* 如果送检单位为空 ，直接将接受过来的信息添加到送检单位信息里边 */
		if (lsdwList.size() > 0){
			/* 如果不为空，先将信息添加到送检单位历史信息表 */
			boolean result = lsdwdao.insertLsxx(lsdwList);
			if (result){
				boolean result1 = dao.delSjdwxx();
				if (result1){
					for (int i = 0; i < jsonObj.size(); i++){
						SjdwxxDto sjdwxxDto = JSONObject.toJavaObject((JSONObject) jsonObj.get(i), SjdwxxDto.class);
						boolean success = dao.insertSjdwxx(sjdwxxDto);
						if (!success) {
                            return false;
                        }
					}
				}
			}
		} else{
			for (int i = 0; i < jsonObj.size(); i++){
				SjdwxxDto sjdwxxDto = JSONObject.toJavaObject((JSONObject) jsonObj.get(i), SjdwxxDto.class);
				dao.insertSjdwxx(sjdwxxDto);
			}
		}
		return true;
	}
	
	/**
	* 查询送检单位列表
	* @return
	*/
	@Override
	public List<SjdwxxDto> selectSjdwList() {
				// TODO Auto-generated method stub
		return dao.selectSjdwList();
	}

}
