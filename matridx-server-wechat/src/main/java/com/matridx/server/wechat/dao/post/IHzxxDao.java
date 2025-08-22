package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.HzxxDto;
import com.matridx.server.wechat.dao.entities.HzxxModel;

@Mapper
public interface IHzxxDao extends BaseBasicDao<HzxxDto, HzxxModel>{


	/**
	 * 患者信息统计
	 * @param hzxxDto
	 * @return
	 */
	List<HzxxDto> getPatientStatis(HzxxDto hzxxDto);

	/**
	 * 修改患者信息
	 * @param hzxxDto
	 * @return
	 */
	int updatePatient(HzxxDto hzxxDto);

}
