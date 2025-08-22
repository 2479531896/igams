package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjdlxxDto;
import com.matridx.igams.wechat.dao.entities.SjdlxxModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;

import java.util.List;

public interface ISjdlxxService extends BaseBasicService<SjdlxxDto, SjdlxxModel>{

    boolean insertDtoList(List<SjdlxxDto> sjdlxxDtos);

    List<SjdlxxDto> getDtoListByWkbh(SjdlxxDto sjdlxxDto);

    List<SjdlxxDto> getDtoBySjidAndJclx(SjxxDto sjxxDto);
}
