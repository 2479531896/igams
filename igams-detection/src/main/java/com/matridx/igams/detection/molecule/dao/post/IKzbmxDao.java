package com.matridx.igams.detection.molecule.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.detection.molecule.dao.entities.KzbmxDto;
import com.matridx.igams.detection.molecule.dao.entities.KzbmxModel;

import java.util.List;
import java.util.Map;

@Mapper
public interface IKzbmxDao extends BaseBasicDao<KzbmxDto, KzbmxModel>{

    /**
     * 批量插入扩增板明细数据
     */
    boolean batchInsertDtoList(List<KzbmxDto> kzbmxlist);

    /**
     * 通过扩增板ID获取扩增明细数据
     */
    List<KzbmxDto> getKzbmxListByKzbid(String kzbid);

    /**
     * 删除扩增板ID对应的扩增板明细数据
     */
    void deleteKzbmxByKzbid(Map<String, Object> map);

    /**
     * 通过样本编号获取扩增明细数据
     */
    KzbmxDto getDtoByYbbh(KzbmxDto kzbmxDto);
}
