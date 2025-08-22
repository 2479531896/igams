package com.matridx.las.netty.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.las.netty.dao.entities.WksyDto;
import com.matridx.las.netty.dao.entities.WksyModel;

@Mapper
public interface IWksyDao extends BaseBasicDao<WksyDto, WksyModel>{
	/**
	 * 定量仪结果保存
	 * @param dlysyDto
	 * @return
	 */
	public int saveDlysy(WksyDto dlysyDto);
	public int updateDlysyDto(WksyDto dlysyDto);
	public WksyDto getWksyDtoBywkid(WksyDto wksyDto) ;
	 
}
