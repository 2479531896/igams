package com.matridx.igams.bioinformation.dao.post;

import com.matridx.igams.bioinformation.dao.entities.ActionrecordDto;
import com.matridx.igams.bioinformation.dao.entities.ActionrecordModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IActionrecordDao extends BaseBasicDao<ActionrecordDto, ActionrecordModel> {

}
