package com.matridx.las.netty.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.las.netty.dao.entities.WkmxDto;
import com.matridx.las.netty.dao.entities.WkmxModel;

@Mapper
public interface IWkmxDao extends BaseBasicDao<WkmxDto, WkmxModel>{

	public int saveWkmx(List<WkmxDto> list);
	
	public WkmxDto getWkmxByYsybid(WkmxDto dto);
	
	public int updateByWkmxId(List<WkmxDto> list);

	public List<WkmxDto> getWkmxByWkid(WkmxDto dto);

	List<WkmxDto>getWkmxList(WkmxDto dto);
	
	public List<WkmxDto> generateReportList(String wkid);
	
	public int updateList(List<WkmxDto> wkmxDtos);
	
	public int updateWkmc(String wkid);

	/**
	 * 根据wkid和内部编号批量更改信息
	 * @param wkmxDtos
	 * @return
	 */
	public Integer updateListByWkidAndNbbh(List<WkmxDto> wkmxDtos);

	/**
	 * 根据wkid和内部编号获取信息
	 * @param dto
	 * @return
	 */
	public WkmxDto getDaoInfo(WkmxDto dto);
}
