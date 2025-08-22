package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjlcznDto;
import com.matridx.igams.wechat.dao.entities.SjlcznModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISjlcznDao extends BaseBasicDao<SjlcznDto, SjlcznModel>{

	/**
	 * 查询临床诊疗指南
	 * @param sjid
	 * @return
	 */
    List<SjlcznDto> getGuideForWord(String sjid);
}
