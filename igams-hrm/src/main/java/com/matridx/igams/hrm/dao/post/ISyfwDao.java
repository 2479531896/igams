package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.SyfwDto;
import com.matridx.igams.hrm.dao.entities.SyfwModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface ISyfwDao extends BaseBasicDao<SyfwDto, SyfwModel> {
    /**
     * @description 批量插入适用范围信息
     */
    boolean insertSyfwDtos(List<SyfwDto> syfwDtos);
    /**
     * @description 批量修改适用范围信息
     */
    boolean updateSyfwDtos(List<SyfwDto> syfwDtos);
    /**
     * @description 获取适用范围信息
     */
    List<SyfwDto> getDtoListByJxmbid(SyfwDto syfwDto);
    /**
     * @description 通过绩效模板id获取适用范围
     */
    List<SyfwDto> getPagedDtoListByJxmbid(SyfwDto syfwDto);
    /**
     * 根据绩效模板ids删除
     */
    void deleteByJxmbids(SyfwDto syfwDto);
    /**
     * @description 根据yhid删除适用范围
     */
    boolean deleteByYhids(SyfwDto syfwDto);
}
