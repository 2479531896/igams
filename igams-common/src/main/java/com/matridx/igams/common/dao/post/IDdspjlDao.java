package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.DdspjlDto;
import com.matridx.igams.common.dao.entities.DdspjlModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Mapper
public interface IDdspjlDao extends BaseBasicDao<DdspjlDto, DdspjlModel> {
    List<String> filtrateDdslid(DdspjlDto ddspjlDto);
    boolean insertList(List<DdspjlDto> list);
}
