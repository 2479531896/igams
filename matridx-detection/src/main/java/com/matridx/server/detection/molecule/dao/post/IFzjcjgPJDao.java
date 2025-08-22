package com.matridx.server.detection.molecule.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.detection.molecule.dao.entities.FzjcjgDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcjgModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IFzjcjgPJDao extends BaseBasicDao<FzjcjgDto, FzjcjgModel>{

    /**
     * 添加分子检测结果信息
     */
    boolean addDtoList(List<FzjcjgDto> list);

    /**
     * 根据分子项目id删除结果信息
     */
    boolean delDtoListByFzxmid(FzjcjgDto fzjcjgDto);
    /**
     * 批量更新
     */
    int updateList(List<FzjcjgDto> list);
    /**
     * 根据分子检测id查询结果信息
     */
    List<Map<String, Object>> getFzjcjgListById(String fzjcid);
    /**
     * 批量新增
     */
    int insertList(List<FzjcjgDto> list);
}
