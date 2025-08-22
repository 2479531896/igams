package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.crm.dao.entities.JxhsmxDto;
import com.matridx.igams.wechat.dao.entities.SwyszkDto;
import com.matridx.igams.wechat.dao.entities.SwyszkModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISwyszkDao extends BaseBasicDao<SwyszkDto, SwyszkModel>{

    /**
     * 批量插入
     */
    Boolean insertList(List<SwyszkDto> list);
    /**
     * 获取未结清数据
     * @return
     */
    List<SwyszkDto> getOutstandingData();
    /**
     * 修改是否逾期字段
     */
    Boolean updateSfyq(SwyszkDto swyszkDto);
    /**
     * 修改是否逾期字段
     */
    Boolean updateSfjs(SwyszkDto swyszkDto);
    /**
     * 应收款选中导出
     * @param swyszkDto
     * @return
     */
    List<SwyszkDto> getYskListForSelectExp(SwyszkDto swyszkDto);

    /**
     * 根据搜索条件获取导出条数
     * @param swyszkDto
     * @return
     */
    int getYskCountForSearchExp(SwyszkDto swyszkDto);

    /**
     * 应收款搜索导出
     * @param swyszkDto
     * @return
     */
    List<SwyszkDto> getYskListForSearchExp(SwyszkDto swyszkDto);
    /**
     * 修改数量字段
     */
    boolean updateAmount(SwyszkDto swyszkDto);
    /**
     * 修改逾期通知时间字段
     */
    Boolean updateYqtzsj(SwyszkDto swyszkDto);
    /**
     * 批量修改
     */
    boolean updateInvoiceList(List<SwyszkDto> list);
    /**
     * 批量修改
     */
    boolean updatePayList(List<SwyszkDto> list);
    /**
     * 批量修改
     */
    int updateJxhsmxsAddJxje(List<JxhsmxDto> list);
    /**
     * 批量修改
     */
    int updateJxhsmxsSfhs(List<JxhsmxDto> list);

    List<SwyszkDto> getPullList(SwyszkDto swyszkDto);


    /**
     * 获取待核算绩效清单
     * @param swyszkDto
     * @return
     */
    List<SwyszkDto> getAccountingList(SwyszkDto swyszkDto);

    /**
     * 获取考核指标和绩效
     * @param map
     * @return
     */
    Map<String,String> getCheckTarget(Map<String,String> map);

    /**
     * 销售收款统计(个人)
     * @param map
     * @return
     */
    List<Map<String,String>> getSaleReceiptsStatistics(Map<String,String> map);

}
