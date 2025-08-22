package com.matridx.igams.common.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdfbsglModel;

@Mapper
public interface IDdfbsglDao extends BaseBasicDao<DdfbsglDto, DdfbsglModel>{

	/**
	 * 获取回调失败的钉钉审批回调信息
	 */
	List<DdfbsglDto> getFailAuditExample(DdfbsglDto ddfbsglDto);

	List<DdfbsglDto> getNoEndList(DdfbsglDto ddfbsglDto);
	
	/**根据cropid获取wbcxid
	 */
	String getWbcxBycropid(String cropid);
}
