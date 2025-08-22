package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.SczlglDto;
import com.matridx.igams.production.dao.entities.SczlglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISczlglDao extends BaseBasicDao<SczlglDto, SczlglModel> {

    /**
     * 获取指令单号
     */
    String getZldhSerial(String str);

    /**
     * 获取审核列表
     */
    List<SczlglDto> getPagedAuditDevice(SczlglDto sczlglDto);


    /**
     * 生产指令列表
     */
    List<SczlglDto> getPagedModelDtoList(SczlglDto sczlglDto);


    /**
     * 审核列表
     */
    List<SczlglDto> getAuditListDevice(List<SczlglDto> sczlglDto);
    /**
     * 通过物料id获取生产指令
     */
    SczlglDto getDtoByWlid(String wlid);
    /**
     * 对统计数据进行处理
     */
    List<Map<String, Object>> getHjyProgress(Map<String,Object> map);
    /**
     * 钉钉生产展板点击查看物料详情
     */
    List<Map<String, Object>> selectDingTalkXq(Map<String,Object> map);
    /**
     * 钉钉生产展板点击查看生产物料详情
     */
    List<Map<String, Object>> selectDingTalkSc(Map<String,Object> map);
    /**
     * 钉钉生产展板点击查看出库物料详情
     */
    List<Map<String, Object>> selectDingTalkCk(Map<String,Object> map);
    /**
     * 钉钉生产展板点击查看库存物料详情
     */
    List<Map<String, Object>> selectDingTalkKc(Map<String,Object> map);
    /**
     * 根据搜索条件获取导出条数
     */
    int getCountForSearchExp(SczlglDto sczlglDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<SczlglDto> getListForSearchExp(SczlglDto sczlglDto);

    /**
     * 从数据库分页获取导出数据
     */
    List<SczlglDto> getListForSelectExp(SczlglDto sczlglDto);
}
