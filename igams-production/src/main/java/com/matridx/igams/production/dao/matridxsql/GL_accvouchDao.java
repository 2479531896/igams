package com.matridx.igams.production.dao.matridxsql;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.GL_accvouchDto;
import com.matridx.igams.production.dao.entities.GL_accvouchModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface GL_accvouchDao extends BaseBasicDao<GL_accvouchDto, GL_accvouchModel>{
    /**
     * 批量新增
     */
    void insertList(List<GL_accvouchDto> list);
    /*
     * 获取总计实际支出
     */
    double getActualExpenses(String sql);
    /*
     * 获取各部门的实际支出
     */
    List<Map<String, Object>> getDepartmentActualExpenses(String sql);
    /*
     * 获取科目实际支出
     */
    List<Map<String, Object>> getSubjectActualExpenses(String sql);
    /*
     * 获取科目实际支出明细
     */
    List<Map<String, Object>> getSubjectActualExpensesDetail(String sql);
    /*
     * 获取部门科目实际支出明细
     */
    List<Map<String, Object>> getDepartmentSubjectExpenseDetail(String sql);
}
