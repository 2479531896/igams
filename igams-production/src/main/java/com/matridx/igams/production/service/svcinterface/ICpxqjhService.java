package com.matridx.igams.production.service.svcinterface;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.CpxqjhDto;
import com.matridx.igams.production.dao.entities.CpxqjhModel;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICpxqjhService extends BaseBasicService<CpxqjhDto, CpxqjhModel>{

    /**
     * 获取需求单号
     */
    String getXqdh();

    /**
     * 成品列表
     */
    List<CpxqjhDto> getPagedDtoList(CpxqjhDto cpxqjhDto);

    /**
     * 成品需求新增
     */
    boolean progressAddSave(CpxqjhDto cpxqjhDto) throws BusinessException;

    /**
     * 成品需求修改
     */
    boolean progressModSave(CpxqjhDto cpxqjhDto) throws BusinessException;

    /**
     * 审核列表
     */
    List<CpxqjhDto> getPagedAuditDevice(CpxqjhDto cpxqjhDto);
    /**
     * 钉钉审批回调
     */
    boolean callbackLlglAduit(JSONObject obj, HttpServletRequest request, Map<String,Object> t_map) throws BusinessException;

    /**
     * 删除
     */
    boolean deleteByIds(CpxqjhDto cpxqjhDto) throws BusinessException;
    /**
     * 获取货物信息
     */
    List<CpxqjhDto> getHwxxByWlid(CpxqjhDto cpxqjhDto);
    /**
     * 需求计划列表
     */
    List<CpxqjhDto> getPagedXqjhDtoList(CpxqjhDto cpxqjhDto);
    /**
     * 处理需求计划入库状态
     */
    void dealXqrkzt(Set<String> xqjhids);
    /**
     * 入库状态维护保存
     */
    boolean rkmaintenanceSaveProgress(CpxqjhDto cpxqjhDto);
    /**
     * @description 修改生产评审和附件
     */
    boolean updateScpsAndFj(CpxqjhDto cpxqjhDto);
}
