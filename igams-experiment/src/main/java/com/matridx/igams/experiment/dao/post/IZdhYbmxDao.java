package com.matridx.igams.experiment.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.ZdhYbmxDto;
import com.matridx.igams.experiment.dao.entities.ZdhYbmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Mapper
public interface IZdhYbmxDao extends BaseBasicDao<ZdhYbmxDto, ZdhYbmxModel> {

    List<ZdhYbmxDto> getDtosByYbid(ZdhYbmxDto ybmxDto);
}
