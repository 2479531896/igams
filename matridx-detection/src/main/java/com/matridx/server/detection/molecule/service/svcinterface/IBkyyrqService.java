package com.matridx.server.detection.molecule.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.detection.molecule.dao.entities.BkyyrqDto;
import com.matridx.server.detection.molecule.dao.entities.BkyyrqModel;

import java.util.List;
import java.util.Map;

public interface IBkyyrqService extends BaseBasicService<BkyyrqDto, BkyyrqModel>{
    /**
     * 新增不可预约日期信息
     * @param bkyyrqDtos
     * @return
     */
    Map<String,Object> insertBkyyrqDtoList(List<BkyyrqDto> bkyyrqDtos);
    /**
     * 修改不可预约日期信息
     * @param bkyyrqDto
     * @return
     */
    Map<String,Object> updateBkyyrqDto(BkyyrqDto bkyyrqDto);
    /**
     * 根据id查询不可预约日期信息
     * @param bkyyrqDto
     * @return
     */
    BkyyrqDto getBkyyrqDto(BkyyrqDto bkyyrqDto);
    /**
     * 删除不可预约日期信息
     * @param bkyyrqDto
     * @return
     */
    boolean delUnAppDateDetails(BkyyrqDto bkyyrqDto);
    /**
     * 根据日期范围查询不可预约日期信息
     * @param bkyyrqDto
     * @return
     */
    List<BkyyrqDto> getUnAppDate(BkyyrqDto bkyyrqDto);
}
