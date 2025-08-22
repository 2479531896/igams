package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjzzsqDto;
import com.matridx.igams.wechat.dao.entities.SjzzsqModel;

import java.util.List;
import java.util.Map;

public interface ISjzzsqService extends BaseBasicService<SjzzsqDto, SjzzsqModel>{

    /**
     * 获取纸质报告申请列表
     * @param sjzzsqDto
     * @return
     */
    List<SjzzsqDto> getPagedDtoListPaperReportsApply(SjzzsqDto sjzzsqDto);

    /**
     * 新增保存纸质报告信息
     * @param sjzzsqDto
     * @return
     */
    boolean addSavePaperReportsApply(SjzzsqDto sjzzsqDto, User user,List<String> zzsqids);
    /**
     * 修改保存纸质报告信息
     * @param sjzzsqDto
     * @return
     */
    boolean modSavePaperReportsApply(SjzzsqDto sjzzsqDto);
    /**
     * 审核列表
     * @param sjzzsqDto
     * @return
     */
    List<SjzzsqDto> getPagedAuditPaperReports(SjzzsqDto sjzzsqDto);
    /**
     * 获取sjid字符串
     * @param sjzzsqDto
     * @return
     */
    String getSjidsByIds(SjzzsqDto sjzzsqDto);
    /**
     * 获取状态为审核中的sjid字符串
     * @param sjzzsqDto
     * @return
     */
    String getSjidsByZt(SjzzsqDto sjzzsqDto);

    /**
     * 锁定
     * @param sjzzsqDto
     * @return
     */
    boolean lock(SjzzsqDto sjzzsqDto);
    /**
     * 获取批量审核全部数据
     * @param shgcList
     * @return
     */
    String getStringByYwids(List<ShgcDto> shgcList);

    /**
     * 取消快递订单
     * @return
     */
    Map<String, Object> cancelOrder(SjzzsqDto sjzzsqDto,User user);

    /**
     *重下单
     */
    Map<String, Object> createOrder(SjzzsqDto sjzzsqDto, User user);
	
	/**
     * 获取电话全部数据
     * @param shgcList
     * @return
     */
    String getDhByYwids(List<ShgcDto> shgcList);
    /**
     * 取消锁定
     * @param sjzzsqDto
     * @return
     */
    boolean cancelLock(SjzzsqDto sjzzsqDto);
    /**
     * 获取所选数据包号
     * @param sjzzsqDto
     * @return
     */
    String getBhsByIds(SjzzsqDto sjzzsqDto);

    /**
     * 自动生成包号
     * @return
     */
    String generateBh(User user);
    /**
     * 打包
     * @param sjzzsqDto
     * @return
     */
    boolean pack(SjzzsqDto sjzzsqDto);
    /**
     * 打包
     * @param sjzzsqDto
     * @return
     */
    boolean cancelPack(SjzzsqDto sjzzsqDto);

    boolean updateZtByIds(SjzzsqDto sjzzsqDto);

    /**
     * 获取送检报告列表
     * @param sjzzsqDto
     * @return
     */
    List<SjzzsqDto> getPageReportList(SjzzsqDto sjzzsqDto);

    /**
     * 通过sjid获取纸质申请数据
     * @param sjzzsqDto
     * @return
     */
    List<SjzzsqDto> getSjzzsqBySjid(SjzzsqDto sjzzsqDto);
}
