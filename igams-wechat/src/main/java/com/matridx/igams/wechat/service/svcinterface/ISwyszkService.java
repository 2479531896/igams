package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.crm.dao.entities.JxhsmxDto;
import com.matridx.igams.wechat.dao.entities.SwyszkDto;
import com.matridx.igams.wechat.dao.entities.SwyszkModel;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ISwyszkService extends BaseBasicService<SwyszkDto, SwyszkModel> {

    /**
     * 上传保存附件相关信息
     *
     */

    String uploadSaveFile(SwyszkDto swyszkDto) throws BusinessException, IOException;

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
     * 删除
     * @param swyszkDto
     * @return
     */
    Boolean delInfo(SwyszkDto swyszkDto) throws BusinessException;
    /**
     * 修改数量字段
     */
    boolean updateAmount(SwyszkDto swyszkDto);
    /**
     * 收款维护
     */
    boolean collectionReceivableCredit(SwyszkDto swyszkDto);
    /**
     * 逾期通知
     * @param swyszkDto
     * @return
     */
    boolean latenoticeSaveReceivableCredit(SwyszkDto swyszkDto);
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
    boolean updateJxhsmxs(List<JxhsmxDto> list) throws BusinessException;
    /**
     * 批量修改
     */
    boolean updateJxhsmxsAddJxje(List<JxhsmxDto> list);
    /**
     * 批量修改
     */
    boolean updateJxhsmxsSfhs(List<JxhsmxDto> list);

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
