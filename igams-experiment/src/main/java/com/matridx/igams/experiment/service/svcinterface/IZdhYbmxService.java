package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.ZdhYbmxDto;
import com.matridx.igams.experiment.dao.entities.ZdhYbmxModel;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
public interface IZdhYbmxService extends BaseBasicService<ZdhYbmxDto, ZdhYbmxModel> {
    List<ZdhYbmxDto> getDtosByYbid(ZdhYbmxDto zdhYbmxDto);
}
