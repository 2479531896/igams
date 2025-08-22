package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.ShfkdjDto;
import com.matridx.igams.production.dao.entities.ShfkdjModel;

import java.util.List;
import java.util.Map;

/**
 * @author:JYK
 */
public interface IShfkdjService extends BaseBasicService<ShfkdjDto, ShfkdjModel> {
    /**
     * 钉钉售后反馈登记列表
     */
    List<ShfkdjDto> getPagedListSaleFeedbackDingTalk(ShfkdjDto shfkdjDto);
    /**
     * 处理记录
     */
    List<Map<String,Object>> getShfkCljl(ShfkdjDto shfkdjDto);
    /**
     * 修改进度
     */
    boolean updateJd(ShfkdjDto shfkdjDto);
    /**
     * 新增保存
     */
    boolean addSaveSaleFeedback(ShfkdjDto shfkdjDto);
    /**
     * 修改保存
     */
    boolean modSaveSaleFeedback(ShfkdjDto shfkdjDto);
    /**
     * 派单需要给通知人员发送钉钉消息
     */
    boolean sendSaleFeedbackMsg(ShfkdjDto shfkdjDto) throws BusinessException;
    /**
     * 取消派单
     */
    boolean cancelSendOrders(ShfkdjDto shfkdjDto) throws BusinessException;
    /**
     * 派单保存
     */
    boolean sendOrdersSave(ShfkdjDto shfkdjDto) throws BusinessException;
}

