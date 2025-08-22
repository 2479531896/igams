package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IYxsjxxDao extends BaseBasicDao<SjxxDto, SjxxModel>{
    /**
     * 优化送检列表查询(数量)
     * @param params
     * @return
     */
    int getCountOptimize(Map<String,Object> params);

    /**
     * 优化送检列表查询(内部拆分)
     * @param params
     * @return
     */
    List<Map<String,Object>> getDtoListOptimize(Map<String, Object> params);

    /**
     * 选中导出
     * @param params
     * @return
     */
    List<Map<String, Object>> getListForSelectExp(Map<String, Object> params);

    /**
     * 搜索导出
     * @param map
     * @return
     */
    List<Map<String, Object>> getListForSearchExp(Map<String, Object> map);

    /**
     * 搜索导出条数
     * @param map
     * @return
     */
    int getCountForSearchExp(Map<String, Object> map);

    /**
     * 对账单数据
     * @param params
     * @return
     */
    List<Map<String,Object>> getDtoAccountList(Map<String, Object> params);
}
