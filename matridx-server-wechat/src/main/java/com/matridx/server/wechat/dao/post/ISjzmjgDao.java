package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjzmjgDto;
import com.matridx.server.wechat.dao.entities.SjzmjgModel;

@Mapper
public interface ISjzmjgDao extends BaseBasicDao<SjzmjgDto, SjzmjgModel>{

	/**
	 * list新增送检自免结果
	 * @param list
	 * @return
	 */
    boolean insertSjzmjg(List<SjzmjgDto> list);
	
	/**
	 * 根据送检ID删除送检自免结果
	 * @param sjzmjgDto
	 * @return
	 */
    boolean deleteSjzmjg(SjzmjgDto sjzmjgDto);
}
