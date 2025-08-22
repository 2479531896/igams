package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.QcjlDto;
import com.matridx.igams.production.dao.entities.QcjlModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IQcjlDao extends BaseBasicDao<QcjlDto, QcjlModel>{

    /**
     * 获取审核数据
     */
    List<QcjlDto> getPagedClearingRecords(QcjlDto qcjlDto);
    List<QcjlDto> getAuditListClearingRecords(List<QcjlDto> qcjlDtos);

    /**
     * 生成记录编号
     */
    String generateJlbh(String prefix);
}
