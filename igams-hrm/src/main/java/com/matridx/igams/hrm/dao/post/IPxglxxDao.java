package com.matridx.igams.hrm.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.PxglxxDto;
import com.matridx.igams.hrm.dao.entities.PxglxxModel;

import java.util.List;

@Mapper
public interface IPxglxxDao extends BaseBasicDao<PxglxxDto, PxglxxModel>{
    /**
     * @description 批量插入培训关联信息
     */
    boolean insertPxglxxDtos(List<PxglxxDto> pxglxxDtos);
    /**
     * @description 文件关联培训信息
     */
    List<PxglxxDto> getTrainByWj(PxglxxDto pxglxxDto);

}
