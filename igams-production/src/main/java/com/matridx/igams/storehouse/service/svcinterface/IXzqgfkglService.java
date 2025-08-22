package com.matridx.igams.storehouse.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.XzqgfkglDto;
import com.matridx.igams.storehouse.dao.entities.XzqgfkglModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IXzqgfkglService extends BaseBasicService<XzqgfkglDto, XzqgfkglModel>{

    /**
     * 获取最大单号
     * @param prefix
     * @return
     */
    String getDjhSerial(String prefix);

    /**
     * 新增时 处理月结数据
     * @param xzqgfkglDto
     * @return
     */
    boolean dealMonthData(XzqgfkglDto xzqgfkglDto) throws BusinessException;

    /**
     * 新增时 处理公对公数据
     * @param xzqgfkglDto
     * @return
     */
    boolean dealPtpData(XzqgfkglDto xzqgfkglDto);

    /**
     * 行政请购确认审核列表
     * @param xzqgfkglDto
     * @return
     */
    List<XzqgfkglDto> getPagedAuditList(XzqgfkglDto xzqgfkglDto);

    /**
     * 钉钉付款审批回调
     * @param obj
     * @param request
     * @param t_map
     * @return
     * @throws BusinessException
     */
    boolean callbackXzqgfkglAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;

    /**
     * 修改行政请购付款信息
     * @param xzqgfkglDto
     * @return
     */
    boolean updateAdministationPay(XzqgfkglDto xzqgfkglDto);

    /**
     * 删除付款信息
     * @param xzqgfkglDto
     * @return
     */
    boolean deleteFkxx(XzqgfkglDto xzqgfkglDto);
}
