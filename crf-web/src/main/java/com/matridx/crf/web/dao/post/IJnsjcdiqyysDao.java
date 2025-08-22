package com.matridx.crf.web.dao.post;

import com.matridx.crf.web.dao.entities.JnsjcdiqyysDto;
import com.matridx.crf.web.dao.entities.JnsjcdiqyysModel;
import com.matridx.igams.common.dao.BaseBasicDao;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IJnsjcdiqyysDao extends BaseBasicDao<JnsjcdiqyysDto, JnsjcdiqyysModel> {

    int insertList(List<JnsjcdiqyysDto> list);

    int delJncjcdiqyys(JnsjcdiqyysDto dto);

    List<JnsjcdiqyysDto>getDtoByList(JnsjcdiqyysDto dto);

}
