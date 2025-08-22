package com.matridx.server.detection.molecule.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxmModel;

import java.util.List;

public interface IFzjcxmPJService extends BaseBasicService<FzjcxmDto, FzjcxmModel>{
    /**
     * 根据分子检测信息的主键id删除关联的分子检测项目
     * @param fzjcxmDto
     */
    boolean delFzjcxmByFzjcid(FzjcxmDto fzjcxmDto);

    /**
     * 成组插入分子检测项目
     */
    boolean insertList(List<FzjcxmDto> fzjcxmDtos);

    /**
     * 根据分子检测id查询有关的检测项目信息
     */
    List<FzjcxmDto> getDtoListByFzjcid(String fzjcid);
    /*
    通过分子项目id删除分子检测项目
 	*/
    void delFzjcxmByIds(FzjcxmDto fzjcxmDto);
    /*
        修改状态
     */
    void updateZt(FzjcxmDto fzjcxmDto);
    /*
        通过分子检测id删除分子检测项目
    */
    void delFzjcxmByFzjc(FzjcxmDto fzjcxmDto);
}
