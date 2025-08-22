package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.XqjhmxDto;
import com.matridx.igams.production.dao.entities.XqjhmxModel;
import java.util.List;

public interface IXqjhmxService extends BaseBasicService<XqjhmxDto, XqjhmxModel>{
    /**
     * 批量新增需求明细信息
     */
    Boolean insertList(List<XqjhmxDto> xqjhmxDtoList);

    /**
     * 批量修改需求明细信息
     */
    Boolean updateList(List<XqjhmxDto> xqjhmxDtoList);

    /**
     * 批量修改需求明细信息
     */
    Boolean updateDtoList(List<XqjhmxDto> xqjhmxDtoList);


    /**
     * 删除
     */
    boolean deleteByCpxqids(XqjhmxDto xqjhmxDto);

    /**
     * 修改生产状态
     */
    boolean modSaveChoose(XqjhmxDto xqjhmxDto) throws BusinessException;
    /**
     * 需求物料列表sql
     */
    List<XqjhmxDto>getPagedACMaterials(XqjhmxDto xqjhmxDto);
    XqjhmxDto getACMaterialDto(XqjhmxDto xqjhmxDto);

    /**
     * 根据ID查询
     */
    List<XqjhmxDto> getDtoListById(XqjhmxDto xqjhmxDto);
}
