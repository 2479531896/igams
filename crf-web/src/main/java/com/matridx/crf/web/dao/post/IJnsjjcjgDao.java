package com.matridx.crf.web.dao.post;

import com.matridx.crf.web.dao.entities.JnsjjcjgModel;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.crf.web.dao.entities.JnsjjcjgDto;

import java.util.List;

@Mapper
public interface IJnsjjcjgDao extends BaseBasicDao<JnsjjcjgDto, JnsjjcjgModel>{

    int insertList(List<JnsjjcjgDto> list);

    List<JnsjjcjgDto> getDtoList(JnsjjcjgDto dto);

    int delJnsjjcjg (JnsjjcjgDto dto);

}
