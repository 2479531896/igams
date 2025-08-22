package com.matridx.crf.web.dao.post;

import com.matridx.crf.web.dao.entities.JnsjcdizlDto;
import com.matridx.crf.web.dao.entities.JnsjcdizlModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IJnsjcdizlDao extends BaseBasicDao<JnsjcdizlDto, JnsjcdizlModel> {

    int insertList(List<JnsjcdizlDto> list);

    List<JnsjcdizlDto> getListByBgId(JnsjcdizlDto dto);

    int delJncjcdizl(JnsjcdizlDto dto);

}
