package com.matridx.igams.detection.molecule.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.dao.entities.KzbmxDto;
import com.matridx.igams.detection.molecule.dao.entities.KzbmxModel;

import java.util.List;
import java.util.Map;

public interface IKzbmxService extends BaseBasicService<KzbmxDto, KzbmxModel>{

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
    /**
     * 插入扩增板明细数据
     */
    boolean insertKzbmx(KzbmxDto kzbmxDto, FzjcxxDto fzjcxxDto, User user);
    /**
     * 修改扩增板明细数据
     */
    boolean updateKzbmx(KzbmxDto kzbmxDto, FzjcxxDto fzjcxxDto, User user);
}
