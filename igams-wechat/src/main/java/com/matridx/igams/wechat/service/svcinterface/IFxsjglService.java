package com.matridx.igams.wechat.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.FxsjglDto;
import com.matridx.igams.wechat.dao.entities.FxsjglModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IFxsjglService extends BaseBasicService<FxsjglDto, FxsjglModel>{

    /**
     * 新增数据
     * @param fxsjglDto
     * @return
     */
    Boolean addInfo(FxsjglDto fxsjglDto) throws BusinessException;

    /**
     * 修改数据
     * @param fxsjglDto
     * @return
     */
    Boolean modInfo(FxsjglDto fxsjglDto) throws BusinessException;

    /**
     * 修改数据
     * @param fxsjglDto
     * @return
     */
    Boolean delInfo(FxsjglDto fxsjglDto);
    /**
     * 审核列表
     * @param
     * @return
     */
    List<FxsjglDto> getPagedAuditList(FxsjglDto fxsjglDto);

    /**
     * 根据ywid获取数据
     * @param fxsjglDto
     * @return
     */
    List<FxsjglDto> getDtoList(FxsjglDto fxsjglDto);

    /**
     * 分页查询
     * @param
     * @return
     */
    List<FxsjglDto> getPagedDtoList(FxsjglDto fxsjglDto);
    /**
     * 申请回调
     * @param obj
     * @param request
     * @param t_map
     * @return
     * @throws BusinessException
     */
    boolean callbackRiskBoardAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException;


}
