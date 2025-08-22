package com.matridx.igams.wechat.dao.post;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjyzmxDto;
import com.matridx.igams.wechat.dao.entities.SjyzmxModel;

@Mapper
public interface ISjyzmxDao extends BaseBasicDao<SjyzmxDto, SjyzmxModel>{
	
	/**
	 * 根据验证id查询最大序号
	 * @param yzid
	 * @return
	 */
	int getMaxXh(String yzid);
	
	SjyzmxDto getSjyzmxFristBynbbh(Map<String,String> map);
	
	boolean updateBynbbm(SjyzmxDto sjyzmxDto);

}
