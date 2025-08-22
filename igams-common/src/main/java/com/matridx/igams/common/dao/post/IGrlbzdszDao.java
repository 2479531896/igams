package com.matridx.igams.common.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.GrlbzdszDto;
import com.matridx.igams.common.dao.entities.GrlbzdszModel;
import com.matridx.igams.common.dao.entities.LbzdszDto;

@Mapper
public interface IGrlbzdszDao extends BaseBasicDao<GrlbzdszDto, GrlbzdszModel>{
	/**
	 * 根据用户信息获取用户已选择的字段信息
	 * grlbzdszDto
	 * 
	 */
	 List<LbzdszDto> getChoseList(GrlbzdszDto grlbzdszDto);
	
	/**
	 * 保存个人选择字段信息
	 * grlbzdszDto
	 * 
	 */
	 boolean SaveChoseList(GrlbzdszDto grlbzdszDto);

	 boolean SaveChoseListVue(GrlbzdszDto grlbzdszDto);
}
