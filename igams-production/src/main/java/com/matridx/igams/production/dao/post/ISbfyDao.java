package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.SbfyDto;
import com.matridx.igams.production.dao.entities.SbfyModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : 郭祥杰
 * @date :
 */
@Mapper
public interface ISbfyDao  extends BaseBasicDao<SbfyDto, SbfyModel> {
}
