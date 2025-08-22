package com.matridx.server.detection.molecule.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.detection.molecule.dao.entities.BkyyrqDto;
import com.matridx.server.detection.molecule.dao.entities.BkyyrqModel;

import java.util.List;

@Mapper
public interface IBkyyrqDao extends BaseBasicDao<BkyyrqDto, BkyyrqModel>{
    /**
     * 新增不可预约日期
     * @param bkyyrqDtos
     * @return
     */
    boolean insertBkyyrqDtoList(List<BkyyrqDto> bkyyrqDtos);

    /**
     * 修改不可预约日期信息
     * @param bkyyrqDto
     * @return
     */
    boolean updateBkyyrqDto(BkyyrqDto bkyyrqDto);
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
