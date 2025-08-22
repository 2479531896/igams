package com.matridx.igams.wechat.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.KpsqDto;
import com.matridx.igams.wechat.dao.entities.KpsqModel;
import com.matridx.igams.wechat.dao.entities.SwyszkDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public interface IKpsqService extends BaseBasicService<KpsqDto, KpsqModel> {
    /**
     * 插入
     */
    Boolean insertInfo(KpsqDto kpsqDto) ;

    /**
     * 提交
     */
    Boolean submitInfo(KpsqDto kpsqDto) ;


    /**
     * 删除开票申请
     */
    Boolean delInfo(KpsqDto kpsqDto) ;

    /**
     * 更新发票号码
     */
    Boolean updateFphm(KpsqDto kpsqDto) throws BusinessException;

    /**
     * 审核列表
     * @param
     * @return
     */
    List<KpsqDto> getPagedAuditList(KpsqDto kpsqDto);

    /**
     * 申请回调
     * @param obj
     * @param request
     * @param t_map
     * @return
     * @throws BusinessException
     */
    boolean requestsCallback(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;
    /**
     *
     * @param kpsqDto
     * @return
     */
    SwyszkDto getAllInfo(KpsqDto kpsqDto);

}
