package com.matridx.igams.finance.dao.financesql;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author WYX
 * @version 1.0
 * {@code @className} IFinanceDao
 * {@code @description} TODO
 * {@code @date} 11:15 2023/8/15
 **/
@Mapper
public interface IFinanceDao {
    /**
     * {@code @description} 获取销售费用
     */
    List<Map<String, Object>> selectFinanceSaleCost(Map<String, Object> map);
    /**
     * {@code @description} 获取成本费用
     */
    List<Map<String, Object>> getCost(String sql);
    /**
     * {@code @description} 获取有费用的部门
     */
    List<String> getHaveCostDepartment(String haveDepSql);
    /**
     * {@code @description} 获取部门费用
     */
    List<Map<String, Object>> getDepartmentCost(String sql);

    List<String> getYearByCcode(Map<String, Object> map);

    List<Map<String, Object>> getDbCost(Map<String, Object> map);
    /*
         成本收入报表数据
     */
    List<Map<String, Object>> getRevenueCost(Map<String, Object> map);
}
