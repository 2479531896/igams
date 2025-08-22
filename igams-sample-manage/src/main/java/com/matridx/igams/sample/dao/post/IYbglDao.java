package com.matridx.igams.sample.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.sample.dao.entities.YbglDto;
import com.matridx.igams.sample.dao.entities.YbglModel;

@Mapper
public interface IYbglDao extends BaseBasicDao<YbglDto, YbglModel>{
	
	/**
	 * 根据申请ID更新标本表里的预定数
	 */
    int updateYdsBySq(YbglDto ybglDto);
	
	/**
	 * 获取标本类型的统计信息
	 */
    List<Map<String, String>> getYblxCount();
	
	/**
	 * 获取冰箱的空闲统计信息
	 */
    List<Map<String, String>> getBxkxCount();
	
	/**
	 * 获取标本使用情况信息
	 */
    List<Map<String, String>> getYbsyqkCount(YbglDto ybglDto);

	/**
	 * 新增导入标本信息
	 */
    int insertImpYbglDto(YbglDto ybglDto);

	/**
	 * 新增导入标本来源信息
	 */
    void insertYblyByYbglDto(YbglDto ybglDto);

}
