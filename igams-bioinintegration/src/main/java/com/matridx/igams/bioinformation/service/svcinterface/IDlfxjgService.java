package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.bioinformation.dao.entities.DlfxjgDto;
import com.matridx.igams.bioinformation.dao.entities.DlfxjgModel;

import java.util.List;
import java.util.Map;

public interface IDlfxjgService extends BaseBasicService<DlfxjgDto, DlfxjgModel>{

    /**
     * 批量插入毒力分析结果数据
     */
    boolean insertList(List<DlfxjgDto> dlfxjglist);

    /**
     * 查询数据
     */
    List<DlfxjgDto> getDtoList(DlfxjgDto dlfxjgDto);

    /**
     * 查询数据 wkcxid 和 bbh
     */
    List<DlfxjgDto> getDtoListByWkcxId(DlfxjgDto dlfxjgDto);
    /**
     * 毒力结果导出
     */
    List<DlfxjgDto> getListForExp(Map<String, Object> params);
}
