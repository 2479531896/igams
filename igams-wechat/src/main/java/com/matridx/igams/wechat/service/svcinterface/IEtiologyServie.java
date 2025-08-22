package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.EtiologyDto;
import com.matridx.igams.wechat.dao.entities.EtiologyModel;

import java.util.List;

public interface IEtiologyServie extends BaseBasicService<EtiologyDto, EtiologyModel> {

    List<EtiologyDto> getYjByCxlx(EtiologyDto etiologyDto);
}
