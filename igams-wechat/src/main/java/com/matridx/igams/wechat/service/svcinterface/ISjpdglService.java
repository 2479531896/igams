package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjpdglDto;
import com.matridx.igams.wechat.dao.entities.SjpdglModel;

import java.util.List;
import java.util.Map;

public interface ISjpdglService extends BaseBasicService<SjpdglDto, SjpdglModel>{
    /**
     * 自动生成派单号
     * @return
     */
    String generatePdh();

    /**
     * 新增
     * @param sjpdglDto
     * @return
     */
    boolean insertDto(SjpdglDto sjpdglDto);

	boolean deletesjpd(SjpdglDto sjpdglDto);
    /**
     * 获取我的物流信息
     */
    List<SjpdglDto> getPagedBylrry(SjpdglDto sjpdglDto);

    /**
     * 取消接单
     */
    boolean updatesjpd(SjpdglDto sjpdglDto);
    /**
     * 处理派单页面过来的暂存保存或派单处理
     * @param sjpdglDto
     * @return
     */
    String pdMsgSave(SjpdglDto sjpdglDto);

    Map<String, Object> pdMsgGet(SjpdglDto sjpdglDto);
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
    boolean notificateTzry(SjpdglDto sjpdglDto);
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
     * 取消订单
     * @param sjpdglDto
     * @return
     */
    boolean cancelOrder(SjpdglDto sjpdglDto);
    /**
     * 获取已完成的订单(不做权限设置)
     * @param sjpdglDto
     * @return
     */
    List<SjpdglDto> getPagedOrdersByZt(SjpdglDto sjpdglDto, User user);
    /**
     * 个人物流清单和全部个人清单
     */
    List<SjpdglDto> getDtoListSignalAndAll(SjpdglDto sjpdglDto, User user);
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

    int updateSjpdxx(SjpdglDto high);	

    /**
     * 根据搜索条件获取导出条数
     * @param sjpdglDto
     * @return
     */
    int getCountForSearchExp(SjpdglDto sjpdglDto, Map<String, Object> params);
	
	  /**
     * 获取预计今天到达的样本数
     * @param sjpdglDto
     * @return
     */
    Map<String,Object> getTodayArriveYbs(SjpdglDto sjpdglDto,User user);

    SjpdglDto getSfpd(SjpdglDto sjpdglDto);
    /**
     * @description 通过关联单号
     * @param sjpdglDto
     * @return 
     */
    boolean updateByGldh(SjpdglDto sjpdglDto);
    /**
     * @description 获取字符运输时长
     * @param sjpdglDto
     * @return
     */
    void getYssc(SjpdglDto sjpdglDto);
}
