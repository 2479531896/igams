package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.BfglDto;
import com.matridx.igams.wechat.dao.entities.BfglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IToolUtilDao extends BaseBasicDao<BfglDto, BfglModel>{

	/**
	 * 根据患者姓名查询报告
	 * @param params
	 * @return
	 */
	List<Map<String, String>> getReportByHzxm(Map<String, String> params);
	
}
