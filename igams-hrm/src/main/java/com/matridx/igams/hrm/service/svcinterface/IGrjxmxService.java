package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.GrjxmxDto;
import com.matridx.igams.hrm.dao.entities.GrjxmxModel;

import java.util.List;

/**
 * @author:JYK
 */
public interface IGrjxmxService extends BaseBasicService<GrjxmxDto, GrjxmxModel> {
    /**
     * @description 批量插入个人绩效明细信息
     */
    boolean insertGrjxmxDtos(List<GrjxmxDto> grjxmxDtos);
    /**
     * @description 获取树结构个人绩效明细数据
     */
    List<GrjxmxDto> getNewDtoList(List<GrjxmxDto> grjxmxDtos);
    /**
     * @description 通过个人绩效id修改岗位id为空的明细
     */
    boolean updateGrjxmxDtosWithNull(List<GrjxmxDto> grjxmxDtos);
    /**
     * @description 通过个人绩效id和岗位id修改明细
     */
    boolean updateGrjxmxDtosWithGwid(List<GrjxmxDto> grjxmxDtos);
    /**
     * @description 获取考核总分
     */
    String getSumScore(GrjxmxDto grjxmxDto);

    List<GrjxmxDto> getDtoListWithScore(GrjxmxDto grjxmxDto);

    List<GrjxmxDto> getDtoListWithNull(GrjxmxDto grjxmxDto);

    boolean discard(GrjxmxDto grjxmxDto);

    List<GrjxmxDto> getDtoListByGwid(GrjxmxDto grjxmxDto);
}
