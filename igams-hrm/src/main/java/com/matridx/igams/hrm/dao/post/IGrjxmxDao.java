package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.GrjxmxDto;
import com.matridx.igams.hrm.dao.entities.GrjxmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IGrjxmxDao extends BaseBasicDao<GrjxmxDto, GrjxmxModel> {
    /**
     * @description 批量插入个人绩效明细信息
     */
    boolean insertGrjxmxDtos(List<GrjxmxDto> grjxmxDtos);

    boolean updateGrjxmxDtosWithNull(List<GrjxmxDto> grjxmxDtos);

    boolean updateGrjxmxDtosWithGwid(List<GrjxmxDto> grjxmxDtos);

    String getSumScore(GrjxmxDto grjxmxDto);

    List<GrjxmxDto> getDtoListWithScore(GrjxmxDto grjxmxDto);

    List<GrjxmxDto> getDtoListWithNull(GrjxmxDto grjxmxDto);

    boolean discard(GrjxmxDto grjxmxDto);

    List<GrjxmxDto> getDtoListByGwid(GrjxmxDto grjxmxDto);
}
