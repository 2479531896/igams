package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.NyysxxDto;
import com.matridx.igams.wechat.dao.entities.NyysxxModel;

import java.util.List;

public interface INyysxxService extends BaseBasicService<NyysxxDto, NyysxxModel>{

    boolean insertDtoList(List<NyysxxDto> nyysxxDto);

    List<NyysxxDto> getNyysxxList(NyysxxDto nyysxxDto);
}
