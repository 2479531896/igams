package com.matridx.igams.detection.molecule.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.detection.molecule.dao.entities.FzzkjgDto;
import com.matridx.igams.detection.molecule.dao.entities.FzzkjgModel;
import com.matridx.igams.detection.molecule.dao.post.IFzzkjgDao;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzzkjgService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FzzkjgServiceImpl extends BaseBasicServiceImpl<FzzkjgDto, FzzkjgModel, IFzzkjgDao> implements IFzzkjgService {

    @Override
    public boolean insertList(List<FzzkjgDto> list) {
        return dao.insertList(list);
    }
}
