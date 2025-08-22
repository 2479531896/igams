package com.matridx.igams.detection.molecule.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.detection.molecule.dao.entities.FzbbztDto;
import com.matridx.igams.detection.molecule.dao.entities.FzbbztModel;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;

public interface IFzbbztService extends BaseBasicService<FzbbztDto, FzbbztModel>{
    //普检标本状态
    boolean insertFzbbzt(FzjcxxDto fzjcxxDto);
}
