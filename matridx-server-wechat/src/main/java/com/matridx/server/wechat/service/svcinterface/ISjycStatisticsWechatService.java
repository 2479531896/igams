package com.matridx.server.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.SjycStatisticsWechatDto;
import com.matridx.server.wechat.dao.entities.SjycStatisticsWechatModel;

import java.util.List;

public interface ISjycStatisticsWechatService extends BaseBasicService<SjycStatisticsWechatDto, SjycStatisticsWechatModel> {

    int insertList(List<SjycStatisticsWechatDto> list);


    int delByYcid(SjycStatisticsWechatDto sjycStatisticsDto);

    List<SjycStatisticsWechatDto>getByYcid(SjycStatisticsWechatDto sjycStatisticsDto);
}
