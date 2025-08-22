package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.WzysxxDto;
import com.matridx.igams.wechat.dao.entities.WzysxxModel;

import java.util.List;

public interface IWzysxxService extends BaseBasicService<WzysxxDto, WzysxxModel>{

    boolean insertDtoList(List<WzysxxDto> wzysxxDtos);

    List<WzysxxDto> getLlh(WzysxxDto wzysxxDto);

    List<WzysxxDto> getDtoList(WzysxxDto wzysxxDto);
}
