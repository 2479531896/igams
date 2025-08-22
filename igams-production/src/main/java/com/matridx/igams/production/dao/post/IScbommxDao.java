package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.BommxDto;
import com.matridx.igams.production.dao.entities.BommxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IScbommxDao extends BaseBasicDao<BommxDto, BommxModel> {

    /**
     * @description 新增BOM明细
     */
    boolean insertBommxDtos(List<BommxDto> bommxDtos);
    /**
     * @description 修改BOM明细
     */
    boolean updateBommxDtos(List<BommxDto> bommxDtos);
    /**
     * @description 删除BOM明细
     */
    boolean deleteByBomIds(BommxDto bommxDto);
    /*
        获取生产领料bom明细
    */
    List<BommxDto> getProduceReceiveBom(BommxDto bommxDto);
}
