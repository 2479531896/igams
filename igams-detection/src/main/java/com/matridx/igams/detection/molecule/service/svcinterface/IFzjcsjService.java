package com.matridx.igams.detection.molecule.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.detection.molecule.dao.entities.FzjcsjDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcsjModel;

import java.util.List;

public interface IFzjcsjService extends BaseBasicService<FzjcsjDto, FzjcsjModel>{

     Boolean insertDtoList(List<FzjcsjDto> list);

    /**
     * 根据分子检测ID和分子项目ID删除检测数据
     */
     boolean deleteByFzjcidsAndFzxmid(FzjcsjDto fzjcsjDto);

    /**
     * 根据分子检测ID和分子项目ID查询检测数据
     */
     List<FzjcsjDto> getDtoByFzjcidAndFzxmid(FzjcsjDto fzjcsjDto);
}
