package com.matridx.server.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.SjycStatisticsWechatDto;
import com.matridx.server.wechat.dao.entities.SjycStatisticsWechatModel;
import com.matridx.server.wechat.dao.post.ISjycStatisticsWechatDao;
import com.matridx.server.wechat.service.svcinterface.ISjycStatisticsWechatService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SjycStatisticsWechatServiceImpl extends BaseBasicServiceImpl<SjycStatisticsWechatDto, SjycStatisticsWechatModel, ISjycStatisticsWechatDao> implements ISjycStatisticsWechatService {
    @Override
    public int insertList(List<SjycStatisticsWechatDto> list) {
        return dao.insertList(list);
    }

    @Override
    public int delByYcid(SjycStatisticsWechatDto sjycStatisticsDto) {
        return dao.delByYcid(sjycStatisticsDto);
    }

    @Override
    public List<SjycStatisticsWechatDto> getByYcid(SjycStatisticsWechatDto sjycStatisticsDto) {
        return dao.getByYcid(sjycStatisticsDto);
    }
}
