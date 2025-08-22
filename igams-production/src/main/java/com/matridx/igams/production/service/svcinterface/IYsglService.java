package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.YsglDto;
import com.matridx.igams.production.dao.entities.YsglModel;

import java.util.List;
import java.util.Map;

public interface IYsglService extends BaseBasicService<YsglDto, YsglModel>{


    /**
     * 获取部门
     */
    List<Map<String,String>> getDepartments();
    /**
     * 获取年度
     */
    List<Map<String,String>> getYears();
    /**
     * 新增保存
     */
    boolean addSaveFinancialBudget(YsglDto ysglDto);
    /**
     * 修改保存
     */
    boolean modSaveFinancialBudget(YsglDto ysglDto);
    /**
     * 删除
     */
    boolean delFinancialBudget(YsglDto ysglDto);
    /**
     * 保存验证
     */
    YsglDto saveVerification(YsglDto ysglDto);
    /*
     * 获取预算与实际支出
     */
    Map<String, Object> getBudgetAndActualExpenses(YsglDto ysglDto) throws BusinessException;
    /*
     * 获取部门实际费用占比
     */
    List<Map<String, Object>> getDepartmentActualExpenses(YsglDto ysglDto) throws BusinessException;
    /*
     * 初始化统计必须数据
     */
    void InitData(boolean needSubject,boolean needDepartment,User user,YsglDto ysglDto) throws BusinessException;
    /*
     * 删除初始化的统计必须数据
     */
    void delInitData(long threadId);
    /*
     * 获取科目实际费用占比
     */
    List<Map<String, Object>> getSubjectActualExpenses(YsglDto ysglDto) throws BusinessException;
    /*
     * 获取部门预算进度
     */
    List<Map<String, Object>> getDepartmentBudgetProgress(YsglDto ysglDto) throws BusinessException;
    /*
     * 获取科目预算进度
     */
    List<Map<String, Object>> getSubjectBudgetProgress(YsglDto ysglDto) throws BusinessException;
    /*
     * 获取科目预算与实际支出明细
     */
    List<Map<String, Object>> getSubjectExpenseDetail(YsglDto ysglDto) throws BusinessException;

    Map<String,Object> getDetail(String jgid);
}
