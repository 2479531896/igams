package com.matridx.crf.web.dao.post;

import com.matridx.crf.web.dao.entities.JnsjhzxxDto;
import com.matridx.crf.web.dao.entities.JnsjhzxxModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.JcsjDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface IJnsjhzxxDao extends BaseBasicDao<JnsjhzxxDto, JnsjhzxxModel> {

    JnsjhzxxDto getDtoById(JnsjhzxxDto dto);

    int updateDto(JnsjhzxxDto dto);

    boolean deletelistByhzids(Map<String, Object> map);

    List<JcsjDto> getHospitailList(JnsjhzxxDto jnsjhzxxDto);
}
