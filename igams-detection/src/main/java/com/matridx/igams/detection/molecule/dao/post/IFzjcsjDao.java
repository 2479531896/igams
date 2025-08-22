package com.matridx.igams.detection.molecule.dao.post;

import com.matridx.igams.detection.molecule.dao.entities.FzjcsjModel;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.detection.molecule.dao.entities.FzjcsjDto;

import java.util.List;

@Mapper
public interface IFzjcsjDao extends BaseBasicDao<FzjcsjDto, FzjcsjModel>{

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
