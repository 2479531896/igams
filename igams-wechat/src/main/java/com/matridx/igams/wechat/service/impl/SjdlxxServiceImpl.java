package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.wechat.dao.entities.SjxxDto;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjdlxxDto;
import com.matridx.igams.wechat.dao.entities.SjdlxxModel;
import com.matridx.igams.wechat.dao.post.ISjdlxxDao;
import com.matridx.igams.wechat.service.svcinterface.ISjdlxxService;

import java.util.List;

@Service
public class SjdlxxServiceImpl extends BaseBasicServiceImpl<SjdlxxDto, SjdlxxModel, ISjdlxxDao> implements ISjdlxxService{

    public boolean insertDtoList(List<SjdlxxDto> sjdlxxDtos){
        return dao.insertDtoList(sjdlxxDtos);
    }

    public List<SjdlxxDto> getDtoListByWkbh(SjdlxxDto sjdlxxDto){
        return dao.getDtoListByWkbh(sjdlxxDto);
    }
	
    public  List<SjdlxxDto> getDtoBySjidAndJclx(SjxxDto sjxxDto){
        return dao.getDtoBySjidAndJclx(sjxxDto);
    }
}
