package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.YsglDto;
import com.matridx.igams.production.dao.entities.YsglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IYsglDao extends BaseBasicDao<YsglDto, YsglModel>{

    /**
     * 获取部门
     */
    List<Map<String,String>> getDepartments();
    /**
     * 获取年度
     */
    List<Map<String,String>> getYears();
    /**
     * 保存验证
     */
    YsglDto saveVerification(YsglDto ysglDto);
    /*
     * 获取预算费用
     */
    double getBudgetExpenses(YsglDto ysglDto);
    /*
     * 获取部门预算费用
     */
    List<Map<String, Object>> getBudgetExpensesGroupByDepartment(YsglDto ysglDto);
    /*
     * 获取科目预算费用
     */
    List<Map<String, Object>> getBudgetExpensesGroupBySubject(YsglDto ysglDto);
    /*
     * 获取科目预算费用明细
     */
    List<Map<String, Object>> getSubjectBudgetExpenseDetail(YsglDto ysglDto);
    /*
     * 获取部门科目预算费用明细
     */
    List<Map<String, Object>> getDepartmentSubjectBudgetDetail(YsglDto ysglDto);
}
