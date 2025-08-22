package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjzmjgDto;
import com.matridx.igams.wechat.dao.entities.SjzmjgModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

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
