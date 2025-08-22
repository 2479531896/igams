package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.QcxmDto;
import com.matridx.igams.production.dao.entities.QcxmModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IQcxmDao extends BaseBasicDao<QcxmDto, QcxmModel>{

    /**
     * 批量新增
     */
    boolean insertList(List<QcxmDto> list);
    /**
     * 删除废弃数据
     */
    boolean delAbandonedData(QcxmDto qcxmDto);

}
