package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.ShysDto;
import com.matridx.igams.production.dao.entities.ShysModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Mapper
public interface IShysDao extends BaseBasicDao<ShysDto, ShysModel> {
    int insertDtoList(List<ShysDto> list);
    int updateDispose(ShysDto shysDto);
}
