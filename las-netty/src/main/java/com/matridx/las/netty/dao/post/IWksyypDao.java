package com.matridx.las.netty.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.las.netty.dao.entities.WksyypDto;
import com.matridx.las.netty.dao.entities.WksyypModel;

@Mapper
public interface IWksyypDao extends BaseBasicDao<WksyypDto, WksyypModel>{
	public int updateSyypList(List list);
	
	 /**
	  * 获取第一次传入pcr的数据
	  * @param map
	  * @return
	  */
	 public List getWzxxMapList(Map<String, String> map);
	 
	/**
	 * 生成文库文件
	 * @param wkid
	 * @return
	 */
	 List<WksyypDto> generateReportList(String wkid);
	 
	 /**
	 * 批量更新
	 * @param list
	 * @return
	 */
	 int updateList(List<WksyypDto> wksyypDto);
	 
	 /**
	 * 批量新增
	 * @param list
	 * @return
	 */
	 int insertList(List<WksyypDto> wksyypDtos);
	 
	 /**
	 * 批量更新
	 * @param list
	 * @return
	 */
	 int updateDtos(List<WksyypDto> wksyypDto);
}
