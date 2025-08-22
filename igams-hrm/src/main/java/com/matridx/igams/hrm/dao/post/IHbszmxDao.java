package com.matridx.igams.hrm.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.HbszmxDto;
import com.matridx.igams.hrm.dao.entities.HbszmxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IHbszmxDao extends BaseBasicDao<HbszmxDto, HbszmxModel> {

    /**
     * @description 批量插入红包设置明细信息
     */
    boolean insertHbszmxDtos(List<HbszmxDto> hbszmxDtos);
    /**
     * @description 批量修改红包设置明细信息
     */
    boolean updateHbszmxDtos(List<HbszmxDto> hbszmxDtos);
    /**
     * @description 批量删除红包设置明细信息
     */
    boolean deleteByHbmxids(HbszmxDto hbszmxDto);
    /**
     * @description 批量修改红包设置剩余数量信息
     */
    void updateHbszmxSysl(List<HbszmxDto> hbszmxDtos);
}
