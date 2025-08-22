package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.QcxmDto;
import com.matridx.igams.production.dao.entities.QcxmModel;

import java.util.List;

public interface IQcxmService extends BaseBasicService<QcxmDto, QcxmModel>{

    /**
     * 批量新增
     */
    boolean insertList(List<QcxmDto> list);
    /**
     * 删除废弃数据
     */
    boolean delAbandonedData(QcxmDto qcxmDto);

}
