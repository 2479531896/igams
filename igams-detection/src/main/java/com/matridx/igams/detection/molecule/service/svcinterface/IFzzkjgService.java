package com.matridx.igams.detection.molecule.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.detection.molecule.dao.entities.FzzkjgDto;
import com.matridx.igams.detection.molecule.dao.entities.FzzkjgModel;

import java.util.List;

public interface IFzzkjgService extends BaseBasicService<FzzkjgDto, FzzkjgModel>{

    /**
     * 批量插入
     */
    boolean insertList(List<FzzkjgDto> list);
}
