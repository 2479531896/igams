package com.matridx.igams.wechat.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.NyysxxDto;
import com.matridx.igams.wechat.dao.entities.NyysxxModel;
import com.matridx.igams.wechat.dao.post.INyysxxDao;
import com.matridx.igams.wechat.service.svcinterface.INyysxxService;

import java.util.List;

@Service
public class NyysxxServiceImpl extends BaseBasicServiceImpl<NyysxxDto, NyysxxModel, INyysxxDao> implements INyysxxService{

    public boolean insertDtoList(List<NyysxxDto> nyysxxDtos){
        return dao.insertDtoList(nyysxxDtos);
    }

    public List<NyysxxDto> getNyysxxList(NyysxxDto nyysxxDto){
        return dao.getNyysxxList(nyysxxDto);
    }
}
