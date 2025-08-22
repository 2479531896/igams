package com.matridx.igams.production.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.HtfpqkDto;
import com.matridx.igams.production.dao.entities.HtfpqkModel;

@Mapper
public interface IHtfpqkDao extends BaseBasicDao<HtfpqkDto, HtfpqkModel>{

	/**
	 * 查看合同发票情况列表
	 */
	List<HtfpqkDto> getFpqkList(HtfpqkDto htfpqkDto);
	
	/**
	 * 合同发票情况修改
	 */
	boolean updateFpqk(HtfpqkDto htfpqkDto);
	
	/**
	 * 删除合同发票情况
	 */
	boolean deleteFpqk(HtfpqkDto htfpqkDto);

	/**
	 * 根据合同ID,发票号码，发票种类查找
	 */
	List<HtfpqkDto> getFpqk(HtfpqkDto htfpqkDto);
}
