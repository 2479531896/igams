package com.matridx.server.detection.molecule.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxmModel;

public interface IFzjcxmService extends BaseBasicService<FzjcxmDto, FzjcxmModel>{
    /**
     * 增加分子检测项目
     * @param fzjcxmDto
     * @return
     */
    boolean insertFzjcxmDto(FzjcxmDto fzjcxmDto);
    /**
     * 根据分子检测信息的主键id删除关联的分子检测项目
     * @param fzjcxmDto
     */
    boolean delFzjcxmByFzjcid(FzjcxmDto fzjcxmDto);

}
