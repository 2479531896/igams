package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.GL_CashTableDto;
import com.matridx.igams.production.dao.entities.GL_CashTableModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GL_CashTableDao extends BaseBasicDao<GL_CashTableDto, GL_CashTableModel>{
    /**
     * 批量新增
     */
    Integer insertList(List<GL_CashTableDto> list);
    /**
     * 获取凭证编号最大值
     */
    String getMaxId(GL_CashTableDto gL_CashTableDto);
}
