package com.matridx.server.detection.molecule.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.detection.molecule.dao.entities.FzjcjgDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcjgModel;

import java.util.List;

public interface IFzjcjgService extends BaseBasicService<FzjcjgDto, FzjcjgModel>{

    /**
     * 添加分子检测结果信息
     * @param list
     * @return
     */
    boolean addDtoList(List<FzjcjgDto> list);

    /**
     * 根据分子检测IDs删除结果信息
     * @param fzjcjgDto
     * @return
     */
    boolean delDtoListByIds(FzjcjgDto fzjcjgDto);

    /**
     * 根据分子检测ID查询结果信息
     * @param list
     * @return
     */
    List<FzjcjgDto> getDtosList(List<String> list);
}
