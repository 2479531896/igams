package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.SczlglDto;
import com.matridx.igams.production.dao.entities.SczlglModel;
import com.matridx.igams.production.dao.entities.XqjhmxDto;

import java.util.List;
import java.util.Map;

public interface ISczlglService extends BaseBasicService<SczlglDto, SczlglModel>{

    /**
     * 获取指令单号
     */
    String getZldh();

    /**
     * 生产指令新增
     */
    List<String>  produceAddSave(SczlglDto sczlglDto, XqjhmxDto xqjhmxDto) throws BusinessException;

    /**
     * 生产指令修改
     */
    List<String>  produceModSave(SczlglDto sczlglDto) throws BusinessException;


    /**
     * 生产指令提交附件保存
     */
    void produceModAudit(SczlglDto sczlglDto) throws BusinessException;

    /**
     * 生产指令删除
     */
    Boolean  produceDel(SczlglDto sczlglDto) throws BusinessException;

    /**
     * 生产指令列表
     */
    List<SczlglDto> getPagedDtoList(SczlglDto sczlglDto);


    /**
     * 生产指令列表
     */
    List<SczlglDto> getPagedModelDtoList(SczlglDto sczlglDto);


    /**
     * 审核列表
     */
    List<SczlglDto> getPagedAuditDevice(SczlglDto sczlglDto);
    /**
     * 对统计数据进行处理
     */
    Map<String, Object> getHjyProgress(Map<String, Object> map);
    /**
     * 钉钉生产展板点击查看需求物料详情
     */
    List<Map<String, Object>> selectDingTalkXq(Map<String, Object> map);
    /**
     * 钉钉生产展板点击查看生产物料详情
     */
    List<Map<String, Object>> selectDingTalkSc(Map<String, Object> map);
    /**
     * 钉钉生产展板点击查看出库物料详情
     */
    List<Map<String, Object>> selectDingTalkCk(Map<String, Object> map);
    /**
     * 钉钉生产展板点击查看库存物料详情
     */
    List<Map<String, Object>> selectDingTalkKc(Map<String, Object> map);
}
