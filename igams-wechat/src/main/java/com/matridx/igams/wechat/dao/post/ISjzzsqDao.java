package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjzzsqDto;
import com.matridx.igams.wechat.dao.entities.SjzzsqModel;

import java.util.List;

@Mapper
public interface ISjzzsqDao extends BaseBasicDao<SjzzsqDto, SjzzsqModel>{

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
    boolean addSavePaperReportsApply(SjzzsqDto sjzzsqDto);

    /**
     * 获取审核列表
     * @param sjzzsqDto
     * @return
     */
    List<SjzzsqDto> getPagedAuditPaperReports(SjzzsqDto sjzzsqDto);
    /**
     * 审核列表
     * @param list
     * @return
     */
    List<SjzzsqDto> getAuditListPaperReports(List<SjzzsqDto> list);
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
     * 批量新增
     * @param sjzzsqDtos
     * @return
     */
    public boolean insertList(List<SjzzsqDto> sjzzsqDtos);
    /**
     * 获取批量审核全部数据
     * @param shgcList
     * @return
     */
    String getStringByYwids(List<ShgcDto> shgcList);
  /**
     * 获取电话全部数据
     * @param shgcList
     * @return
     */
    String getDhByYwids(List<ShgcDto> shgcList);

    /**
     * 通过快递单号mailno获取商家下单号号sjmailno
     * @param sjzzsqDto
     * @return
     */
    String getSjmailnoByMailno(SjzzsqDto sjzzsqDto);
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
     * 生成包号
     * @param prefix
     * @return
     */
    String getBhSerial(String prefix);

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

    int updateZtByIds(SjzzsqDto sjzzsqDto);

    List<String> getDtoListByMailno(String mailno);
	/**
     * 支付列表选中导出
     * @param sjzzsqDto
     * @return
     */    /**
     * 搜索条件分页获取导出信息
     * @param sjzzsqDto
     * @return
     */
    List<SjzzsqDto> getListForSearchExp(SjzzsqDto sjzzsqDto);

    /**
     * 根据搜索条件获取导出条数
     * @param sjzzsqDto
     * @return
     */
    int getCountForSearchExp(SjzzsqDto sjzzsqDto);

    /**
     * 列表选中导出
     * @param sjzzsqDto
     * @return
     */
    List<SjzzsqDto> getListForSelectExp(SjzzsqDto sjzzsqDto);

    /**
     * 获取当前纸质申请数据的审核信息
     * @param sjzzsqDto
     * @return
     */
    ShxxDto getAuditDto(SjzzsqDto sjzzsqDto);

    /**
     * 根据包号获取送检纸质申请数据
     * @param sjzzsqDto
     * @return
     */
    List<SjzzsqDto> getDtoListByBh(SjzzsqDto sjzzsqDto);
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
