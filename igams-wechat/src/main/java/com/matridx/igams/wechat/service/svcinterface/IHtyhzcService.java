package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.HtyhzcDto;
import com.matridx.igams.wechat.dao.entities.HtyhzcModel;

import java.util.List;

public interface IHtyhzcService extends BaseBasicService<HtyhzcDto, HtyhzcModel>{

    public boolean insertList(List<HtyhzcDto> htyhzcDtos);

    public boolean updateList(List<HtyhzcDto> htyhzcDtos);

    public List<HtyhzcDto> getYhzcInfo(HtyhzcDto htyhzcDto);
}
