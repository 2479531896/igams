package com.matridx.igams.experiment.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.ZdhYbmxDto;
import com.matridx.igams.experiment.dao.entities.ZdhYbmxModel;
import com.matridx.igams.experiment.dao.post.IZdhYbmxDao;
import com.matridx.igams.experiment.service.svcinterface.IZdhYbmxService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Service
public class ZdhYbmxServiceImpl extends BaseBasicServiceImpl<ZdhYbmxDto, ZdhYbmxModel, IZdhYbmxDao> implements IZdhYbmxService {
    public List<ZdhYbmxDto> getDtosByYbid(ZdhYbmxDto zdhYbmxDto){
        return dao.getDtosByYbid(zdhYbmxDto);
    }
}
