package com.matridx.igams.detection.molecule.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.detection.molecule.dao.entities.BkyyrqDto;
import com.matridx.igams.detection.molecule.dao.entities.BkyyrqModel;

import java.util.List;

@Mapper
public interface IBkyyrqDao extends BaseBasicDao<BkyyrqDto, BkyyrqModel>{
    /**
     * 新增不可预约日期
     */
    boolean insertBkyyrqDtoList(List<BkyyrqDto> bkyyrqDtos);

    /**
     * 修改不可预约日期信息
     */
    boolean updateBkyyrqDto(BkyyrqDto bkyyrqDto);
    /**
     * 根据id查询不可预约日期信息
     */
    BkyyrqDto getBkyyrqDto(BkyyrqDto bkyyrqDto);

    /**
     * 删除不可预约日期信息
     */
    boolean delUnAppDateDetails(BkyyrqDto bkyyrqDto);
    /**
     * 根据日期范围查询不可预约日期信息
     */
    List<BkyyrqDto> getUnAppDate(BkyyrqDto bkyyrqDto);
}
