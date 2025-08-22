package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.JqxzDto;
import com.matridx.igams.hrm.dao.entities.JqxzModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IJqxzDao extends BaseBasicDao<JqxzDto, JqxzModel> {
    boolean insertList(List<JqxzDto> list);
}
