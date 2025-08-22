package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.LlxxDto;
import com.matridx.igams.storehouse.dao.entities.LlxxModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : 郭祥杰
 * @date :
 */
@Mapper
public interface ILlxxDao  extends BaseBasicDao<LlxxDto, LlxxModel> {
}
