package com.matridx.igams.detection.molecule.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.detection.molecule.dao.entities.FzbbztDto;
import com.matridx.igams.detection.molecule.dao.entities.FzbbztModel;

import java.util.List;

@Mapper
public interface IFzbbztDao extends BaseBasicDao<FzbbztDto, FzbbztModel>{

    /**
     * 新增普检标本状态
     */
    boolean insertFzbbzt(List<FzbbztDto> fzbbztDtoList);

    /**
     * 根据分子检测id删除分子标本状态数据
     */
    boolean deleteByFzjcid(String fzjcid);

    /**
     * 通过分子检测id查找普检标本状态
     */
    List<FzbbztDto> getListByFzjcid(String fzjcid);

    /**
     * 通过分子检测id查找普检标本状态csid的list
     */
    List<String> getZtListByFzjcid(String fzjcid);
}
