package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjbgljxxDto;
import com.matridx.igams.wechat.dao.entities.SjbgljxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISjbgljxxDao extends BaseBasicDao<SjbgljxxDto, SjbgljxxModel>{
	/**
	 * 批量新增
	 */
    int insertList(List<SjbgljxxDto> sjbgljxxDtos);
}
