package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.ZjszDto;
import com.matridx.igams.storehouse.dao.entities.ZjszModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IZjszDao extends BaseBasicDao<ZjszDto, ZjszModel> {

    List<ZjszDto> getZjszDtos();

    boolean updateList(List<ZjszDto> zjszDtos);
}
