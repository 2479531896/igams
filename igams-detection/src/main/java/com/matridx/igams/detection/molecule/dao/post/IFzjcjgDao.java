package com.matridx.igams.detection.molecule.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.detection.molecule.dao.entities.FzjcjgDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcjgModel;

import java.util.List;
import java.util.Map;

@Mapper
public interface IFzjcjgDao extends BaseBasicDao<FzjcjgDto, FzjcjgModel>{

    /**
     * 添加分子检测结果信息
     */
    boolean addDtoList(List<FzjcjgDto> list);

    /**
     * 根据分子检测IDs删除结果信息
     */
    boolean delDtoListByIds(FzjcjgDto fzjcjgDto);

    /**
     * 根据分子检测ID查询结果信息
     */
    List<FzjcjgDto> getDtosList(List<String> list);


    /**
     * 根据分子检测ID和分子项目ID查询结果信息
     */
    List<FzjcjgDto> getListByXmidAndJcid(FzjcjgDto fzjcjgDto);

    /**
     * 根据分子检测id查询结果信息
     */
    List<Map<String, Object>> getFzjcjgListById(String fzjcid);
}
