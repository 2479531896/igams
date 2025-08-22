package com.matridx.igams.finance.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;

import java.util.List;
import java.util.Map;

/**
 * {@code @author:JYK}
 */
public interface IU8FinanceService {
    /**
     * 报表导出
     */
    Map<String,Object> ExportFinanceOut(Map<String,Object> map) throws BusinessException;
    /**
     * 获取两个月份之间的所有月份(含跨年)
     */
    Map<String,List<String>> getMonthAndYearBetween(String minDate, String maxDate);

    /**
     * 根据ccode获取年份
     * @param map
     * @return
     * @throws BusinessException
     */
    List<String> getYearByCcode(Map<String,Object> map)throws BusinessException;

}
