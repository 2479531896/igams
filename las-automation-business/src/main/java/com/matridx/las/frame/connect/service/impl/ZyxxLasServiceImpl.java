package com.matridx.las.frame.connect.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.las.frame.connect.dao.entities.ZyxxLasDto;
import com.matridx.las.frame.connect.dao.entities.ZyxxLasModel;
import com.matridx.las.frame.connect.dao.post.IZyxxLasDao;
import com.matridx.las.frame.connect.service.svcinterface.IZyxxLasService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZyxxLasServiceImpl extends BaseBasicServiceImpl<ZyxxLasDto, ZyxxLasModel, IZyxxLasDao> implements IZyxxLasService {
    @Override
    public List<ZyxxLasDto> getZyxxInit() {
        return dao.getZyxxInit();
    }
}
