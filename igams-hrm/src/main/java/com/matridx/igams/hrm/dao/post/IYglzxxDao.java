package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;

import com.matridx.igams.hrm.dao.entities.YglzxxDto;
import com.matridx.igams.hrm.dao.entities.YglzxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IYglzxxDao extends BaseBasicDao<YglzxxDto, YglzxxModel> {

    boolean insertYglzxxDtos(List<YglzxxDto> yglzxxs);
}
