package com.matridx.igams.crm.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.crm.dao.entities.JxhsmxDto;
import com.matridx.igams.crm.dao.entities.JxhsmxModel;

import java.util.List;

@Mapper
public interface IJxhsmxDao extends BaseBasicDao<JxhsmxDto, JxhsmxModel>{
    /**
     * 批量新增绩效核算明细
     * @param jxhsmxDtos
     * @return
     */
    int insertDtos(List<JxhsmxDto> jxhsmxDtos);
    /**
     * 批量修改绩效核算明细
     * @param jxhsmxDtos
     * @return
     */
    int updateDtos(List<JxhsmxDto> jxhsmxDtos);

    /**
     * 批量删除绩效核算明细
     * @param jxhsmxDtos
     * @return
     */
    int deleteDtos(List<JxhsmxDto> jxhsmxDtos);
}
