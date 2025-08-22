package com.matridx.las.netty.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.las.netty.dao.entities.DlysymxDto;
import com.matridx.las.netty.dao.entities.DlysymxModel;

@Mapper
public interface IDlysymxDao extends BaseBasicDao<DlysymxDto, DlysymxModel>{
	/**
	 * 保存定量仪明细
	 * @param list
	 * @return
	 */
	public int saveDlysymx(List<DlysymxDto> list);

}
