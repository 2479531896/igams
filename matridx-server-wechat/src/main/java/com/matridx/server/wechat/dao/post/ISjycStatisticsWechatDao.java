package com.matridx.server.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjycStatisticsWechatDto;
import com.matridx.server.wechat.dao.entities.SjycStatisticsWechatModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISjycStatisticsWechatDao extends BaseBasicDao<SjycStatisticsWechatDto, SjycStatisticsWechatModel> {
    int insertList(List<SjycStatisticsWechatDto> list);


    int delByYcid(SjycStatisticsWechatDto sjycStatisticsDto);

    List<SjycStatisticsWechatDto>getByYcid(SjycStatisticsWechatDto sjycStatisticsDto);
}
