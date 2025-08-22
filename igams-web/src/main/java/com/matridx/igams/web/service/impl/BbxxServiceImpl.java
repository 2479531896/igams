package com.matridx.igams.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.web.dao.entities.BbxxDto;
import com.matridx.igams.web.dao.entities.BbxxModel;
import com.matridx.igams.web.dao.post.IBbxxDao;
import com.matridx.igams.web.service.svcinterface.IBbxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class BbxxServiceImpl extends BaseBasicServiceImpl<BbxxDto, BbxxModel, IBbxxDao> implements IBbxxService {

	@Autowired
	RedisUtil redisUtil;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	/**
	 * 查询版本信息
	 * 
	 * @param
	 * @return
	 */
	@Override
	public List<BbxxDto> getBbxxDtoList(){
		List<Object> list = redisUtil.lGet("List_matridx_bbxx");
		List<BbxxDto> bbxxlist;
		List<BbxxDto> returnList = new ArrayList<>();
		if(CollectionUtils.isEmpty(list)){
			bbxxlist = dao.getBbxxDtoList();
			if (!CollectionUtils.isEmpty(bbxxlist)){
				for (BbxxDto bbxxDto : bbxxlist) {
					redisUtil.lSet("List_matridx_bbxx_new", JSONObject.toJSONString(bbxxDto),-1);
					if (StringUtil.isNotBlank(bbxxDto.getGxnr())){
						bbxxDto.setGxnr(bbxxDto.getGxnr().replaceAll("\\r\\n", "</br>")
								.replaceAll("\\n", "</br>")
								.replaceAll("\\r", "</br>"));
					}
					returnList.add(bbxxDto);
				}
			}
		}else {
			for (Object obj : list) {
				BbxxDto j = JSON.parseObject(obj.toString(), BbxxDto.class);
				String gxnr = j.getGxnr();
				if (gxnr !=null){
					gxnr = gxnr.replaceAll("\\r\\n", "</br>")
							.replaceAll("\\n", "</br>")
							.replaceAll("\\r", "</br>");
					j.setGxnr(gxnr);
				}else {
					j.setGxnr("");
				}
				returnList.add(j);
			}
		}
		redisUtil.rename("List_matridx_bbxx_new","List_matridx_bbxx");
		return returnList;
	}

    /**
	 * 获取所有版本信息
	 *
	 * @param
	 * @return
	 */
	@Override
	public List<BbxxDto> getAllBbxxDtoList(){
		return dao.getAllBbxxDtoList();
	}

    /**
	 * 获取版本信息列表
	 * 
	 * @param bbxxDto
	 * @return
	 */
	@Override
	public List<BbxxDto> getPageDtoListVersionInfo(BbxxDto bbxxDto){
		return dao.getPageDtoListVersionInfo(bbxxDto);
	}

    /**
	 * 数据用户查看
	 * @param bbid
	 * @return
	 */
	@Override
	public BbxxDto getDtoVersionInfoByBbid(String bbid) {
		return dao.getDtoVersionInfoByBbid(bbid);
	}

    /**
	 * 版本信息新增
	 * @param bbxxDto
	 * @return
	 */
	@Override
	public boolean insertDtoVersionInfo(BbxxDto bbxxDto) {
		boolean b = dao.insertDtoVersionInfo(bbxxDto);
		if (b){
			resetBbxxRedis();
		}
		return b;
	}

    /**
	 * 版本信息修改
	 * @param bbxxDto
	 * @return
	 */
	@Override
	public boolean updateDtoVersionInfo(BbxxDto bbxxDto) {
		boolean b = dao.updateDtoVersionInfo(bbxxDto);
		if (b) {
			resetBbxxRedis();
		}
		return b;
	}

    /**
	 * 版本信息删除（修改删除标记）
	 * @param bbxxDto
	 * @return
	 */
	@Override
	public boolean delDtoListVersionInfo(BbxxDto bbxxDto) {
		boolean b = dao.delDtoListVersionInfo(bbxxDto);
		if (b) {
			resetBbxxRedis();
		}
		return b;
	}

	public void resetBbxxRedis(){
		//清除版本信息的所有键
		Set<String> keys = redisTemplate.keys("List_matridx_bbxx");
		if (keys != null) {
			redisTemplate.delete(keys);
		}
		//重新加载版本信息到redis中
		getBbxxDtoList();
	}
}
