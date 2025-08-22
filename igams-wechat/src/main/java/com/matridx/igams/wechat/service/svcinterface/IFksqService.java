package com.matridx.igams.wechat.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.FksqDto;
import com.matridx.igams.wechat.dao.entities.FksqModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public interface IFksqService extends BaseBasicService<FksqDto, FksqModel> {

    /**
     * 付款申请保存
     *
     */
    Boolean paymentSaveReceivableCredit(FksqDto fksqDto);

    /**
     * 商务付款申请回调
     * @param obj
     * @param request
     * @param t_map
     * @return
     * @throws BusinessException
     */
    boolean paymentApplicationCallback(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;

    /**
     * 审核列表
     * @param fksqDto
     * @return
     */
    List<FksqDto> pageGetListAuditPaymentApplication(FksqDto fksqDto);

    /**
     * 删除
     */
    boolean deleteDto(FksqDto fksqDto);

}
