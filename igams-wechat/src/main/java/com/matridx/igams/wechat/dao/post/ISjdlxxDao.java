package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.wechat.dao.entities.SjxxDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjdlxxDto;
import com.matridx.igams.wechat.dao.entities.SjdlxxModel;

import java.util.List;

@Mapper
public interface ISjdlxxDao extends BaseBasicDao<SjdlxxDto, SjdlxxModel>{

    boolean insertDtoList(List<SjdlxxDto> sjdlxxDtos);

    List<SjdlxxDto> getDtoListByWkbh(SjdlxxDto sjdlxxDto);

    List<SjdlxxDto> getDtoBySjidAndJclx(SjxxDto sjxxDto);
}
