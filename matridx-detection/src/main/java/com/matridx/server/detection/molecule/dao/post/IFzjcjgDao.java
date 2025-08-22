package com.matridx.server.detection.molecule.dao.post;

import com.matridx.server.detection.molecule.dao.entities.FzjcjgModel;
import com.matridx.server.detection.molecule.dao.entities.FzjcjgDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;

import java.util.List;

@Mapper
public interface IFzjcjgDao extends BaseBasicDao<FzjcjgDto, FzjcjgModel>{

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
