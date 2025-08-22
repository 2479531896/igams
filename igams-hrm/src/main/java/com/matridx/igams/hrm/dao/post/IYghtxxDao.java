package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.YghtxxDto;
import com.matridx.igams.hrm.dao.entities.YghtxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IYghtxxDao extends BaseBasicDao<YghtxxDto, YghtxxModel> {

    boolean insertYghtxxDtos(List<YghtxxDto> yghtxxs);
}
