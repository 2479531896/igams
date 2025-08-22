package com.matridx.igams.bioinformation.service.svcinterface;

import com.matridx.igams.bioinformation.dao.entities.NyfxjgDto;
import com.matridx.igams.bioinformation.dao.entities.NyfxjgModel;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;

public interface INyfxjgService extends BaseBasicService<NyfxjgDto, NyfxjgModel>{

    /**
     * 批量插入耐药分析结果
     */
    boolean insertList(List<NyfxjgDto> nyfxjgDtoList);

    /**
     * 查询数据
     */
    List<NyfxjgDto> getDtos(NyfxjgDto nyfxjgDto);
    /**
     *根据文库测序ID和版本号查询
     */
    List<NyfxjgDto> getListByWkcxid(NyfxjgDto nyfxjgDto);

    /**
     * 根据耐药基因查找数据
     */
    List<NyfxjgDto> getListByNyjy(NyfxjgDto nyfxjgDto);

    /**
     * 根据ids查询list
     */
    List<NyfxjgDto> getDtoListById(String id);
    /**
     * 根据ids查找数据
     */
    List<NyfxjgDto> getDtoListByIds(NyfxjgDto nyfxjgDto);
    /**
     * 耐药结果导出
     */
    List<NyfxjgDto> getListForExp(Map<String, Object> params);
    /**
     * 获取导出数据
     */
    List<NyfxjgDto> getExportList(NyfxjgDto nyfxjgDto);
}
