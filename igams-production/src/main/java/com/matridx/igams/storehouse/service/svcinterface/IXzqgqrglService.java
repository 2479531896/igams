package com.matridx.igams.storehouse.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.XzqgqrcglDto;
import com.matridx.igams.storehouse.dao.entities.XzqgqrglDto;
import com.matridx.igams.storehouse.dao.entities.XzqgqrglModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IXzqgqrglService extends BaseBasicService<XzqgqrglDto, XzqgqrglModel>{

    /**
     * 处理月结数据
     * @param xzqgqrglDto
     * @return
     */
    boolean dealMonthBalanceData(XzqgqrglDto xzqgqrglDto) throws BusinessException;

    /**
     * 处理公对公数据
     * @param xzqgqrglDto
     * @return
     */
    boolean dealPtpBalanceData(XzqgqrglDto xzqgqrglDto);

    /**
     * 自动生成单号
     * @param xzqgqrcglDto
     * @return
     */
    String generateConfirmDjh(XzqgqrcglDto xzqgqrcglDto);

     /**
     * 删除行政请购确认单
     * @param xzqgqrglDto
     * @return
     */
     boolean delConfirmDtos(XzqgqrglDto xzqgqrglDto);

    /**
     * 修改行政请购确认单
     * @param xzqgqrglDto
     * @return
     */
    boolean modSavePurchaseConfirm(XzqgqrglDto xzqgqrglDto);
	/**
     * 行政请购确认审核列表
     * @param xzqgqrglDto
     * @return
     */
    List<XzqgqrglDto> getPagedAuditList(XzqgqrglDto xzqgqrglDto);

    /**
     * 行政请购确认钉钉审批回调
     * @param json_obj
     * @param request
     * @param t_map
     * @return
     */
    boolean callbackXzqgqrAduit(JSONObject json_obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;

    /**
     * 根据qrids获取行政请购确认信息
     * @param xzqgqrglDto
     * @return
     */
    List<XzqgqrglDto> getDtoListByIds(XzqgqrglDto xzqgqrglDto);

    /**
     * 完成确认行政请购确认列表数据
     * @param xzqgqrglDto
     * @return
     */
    boolean finishDto(XzqgqrglDto xzqgqrglDto, User user);
}
