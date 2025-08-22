package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.FfjlDto;
import com.matridx.igams.production.dao.entities.FfjlModel;

import java.util.List;

public interface IFfjlService extends BaseBasicService<FfjlDto, FfjlModel>{
    /**
     * 发放保存
     */
    boolean printgrantSaveDocument(FfjlDto ffjlDto) throws BusinessException;
    /**
     * @description 新增发放记录
     */
    void insertFfjlDtos(List<FfjlDto> ffjlDtos);
}
