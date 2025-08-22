package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.BomglDto;
import com.matridx.igams.production.dao.entities.BomglModel;

public interface IScbomService extends BaseBasicService<BomglDto, BomglModel> {

    /**
     * @description 新增保存BOM信息
     */
    boolean addSaveProduceBom(BomglDto bomglDto) throws BusinessException;
    /**
     * @description 修改保存BOM信息
     */
    boolean modSaveProduceBom(BomglDto bomglDto) throws BusinessException;
    /**
     * @description 删除BOM信息
     */
    boolean delProduceBom(BomglDto bomglDto) throws BusinessException;
    /**
     * 根据物料id查询
     */
    BomglDto selectDtoByMjwlid(String mjwlid);
}
