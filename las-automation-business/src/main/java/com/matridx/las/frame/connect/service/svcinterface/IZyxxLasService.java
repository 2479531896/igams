package com.matridx.las.frame.connect.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.frame.connect.dao.entities.ZyxxLasDto;
import com.matridx.las.frame.connect.dao.entities.ZyxxLasModel;

import java.util.List;

public interface IZyxxLasService extends BaseBasicService<ZyxxLasDto, ZyxxLasModel>{

    List<ZyxxLasDto> getZyxxInit();
}
