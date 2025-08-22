package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.QcjlDto;
import com.matridx.igams.production.dao.entities.QcjlModel;

import java.util.List;

public interface IQcjlService extends BaseBasicService<QcjlDto, QcjlModel>{

    /**
     * 获取审核列表
     */
    List<QcjlDto> getPagedAuditClearingRecords(QcjlDto qcjlDto);

    /**
     * 删除
     */
    boolean delClearingRecords(QcjlDto qcjlDto);

    /**
     * 生成记录编号
     */
    String generateJlbh();
    /**
     * 清场保存
     */
    boolean clearSaveClearingRecords(QcjlDto qcjlDto);
    /**
     * 修改保存
     */
    boolean modSaveClearingRecords(QcjlDto qcjlDto);

}
