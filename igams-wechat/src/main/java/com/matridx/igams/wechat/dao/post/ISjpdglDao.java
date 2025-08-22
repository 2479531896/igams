package com.matridx.igams.wechat.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjpdglDto;
import com.matridx.igams.wechat.dao.entities.SjpdglModel;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISjpdglDao extends BaseBasicDao<SjpdglDto, SjpdglModel>{
    boolean deletesjpd(SjpdglDto sjpdglDto);




    /**
     * 获取流水号
     * @param pdh
     * @return
     */
    String getPdhSerial(String pdh);
    /**
     * 获取我的物流信息
     */
    List<SjpdglDto> getPagedBylrry(SjpdglDto sjpdglDto);
    /**
     * 查询历史订单
     */
    List<SjpdglDto> getPagedHistoryOrder(SjpdglDto sjpdglDto);

    /**
     * 确认标本编号是否重复
     * @param sjpdglDto
     * @return
     */
    List<SjpdglDto> isBbbhRepeat(SjpdglDto sjpdglDto);
    /**
     * 接单列表
     */
    List<SjpdglDto> getPagedOrderList(SjpdglDto sjpdglDto);

    /**
     * 根据录入人员查找最新的派单管理数据
     * @param sjpdglDto
     * @return
     */
    SjpdglDto getLatestSjpd(SjpdglDto sjpdglDto);
    /**
     * 获取已完成的订单(不做权限设置)
     * @param sjpdglDto
     * @return
     */
    List<SjpdglDto> getPagedOrdersByZt(SjpdglDto sjpdglDto);
    /**
     * 通过角色id查找限制单位
     */
    List<String> getJcdwListByJsid(SjpdglDto sjpdglDto);
    /**
     * 通过用户id查找所有的角色id
     */
    List<String> getAllXzJsids(SjpdglDto sjpdglDto);
    /**
     * 获取单位显示标记
     */
    SjpdglDto getDqjsdwxdbjByJsid(SjpdglDto sjpdglDto);
   
	int updateSjpdxx(SjpdglDto sjpdglDto);
    /**
     * 根据搜索条件获取导出条数
     * @param sjpdglDto
     * @return
     */
    int getCountForSearchExp(SjpdglDto sjpdglDto);

    /**
     * 从数据库分页获取导出数据
     * @param sjpdglDto
     * @return
     */
    List<SjpdglDto> getListForSearchExp(SjpdglDto sjpdglDto);

    /**
     * 从数据库分页获取导出数据
     * @param sjpdglDto
     * @return
     */
    List<SjpdglDto> getListForSelectExp(SjpdglDto sjpdglDto);
	
	    /**
     * 获取预计今天到达的样本数
     * @param sjpdglDto
     * @return
     */
    Map<String,Object> getTodayArriveYbs(SjpdglDto sjpdglDto);

    /**
     * 查找未接单的送检派单管理
     * @return
     */
    List<SjpdglDto> getNotJdList(SjpdglDto sjpdglDto);
	
	SjpdglDto getSfpd(SjpdglDto sjpdglDto);

    /**
     * @description 通过关联单号
     * @param sjpdglDto
     * @return
     */
    boolean updateByGldh(SjpdglDto sjpdglDto);
}
