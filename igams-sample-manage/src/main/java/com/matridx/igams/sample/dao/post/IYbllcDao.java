package com.matridx.igams.sample.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.sample.dao.entities.YbllcDto;
import com.matridx.igams.sample.dao.entities.YbllcModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IYbllcDao extends BaseBasicDao<YbllcDto, YbllcModel> {
    //通过人员id获取领料明细
    List<YbllcDto> getYbLlcDtoList(YbllcDto ybllcDto);
}
