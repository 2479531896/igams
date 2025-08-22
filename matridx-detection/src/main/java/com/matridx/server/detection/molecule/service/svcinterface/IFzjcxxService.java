package com.matridx.server.detection.molecule.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxxModel;
import com.matridx.server.detection.molecule.dao.entities.HzxxtDto;

import java.util.List;

public interface IFzjcxxService extends BaseBasicService<FzjcxxDto, FzjcxxModel>{
    /**
     * 新冠列表查看
     * @param fzjcxxDto
     * @return
     */
    List<FzjcxxDto> viewCovidDetails(FzjcxxDto fzjcxxDto);
    /*新增预约信息到分子检测信息表*/
    boolean insertDetectionAppointmentFzjcxx(FzjcxxDto fzjcxxDto);
    /*更新预约信息到分子检测信息表*/
    boolean updateDetectionAppointmentFzjcxx(FzjcxxDto fzjcxxDto);
    /*取消已预约的患者信息*/
   boolean cancelDetectionAppointment(FzjcxxDto fzjcxxDto);
   /**
     * 报告日期查看
     * @param fzjcxxDto
     * @return
     */
   List<FzjcxxDto> bgrqDetails(FzjcxxDto fzjcxxDto);
    /**
     * 今天当天之前（含当天）的预约数据
     * @param
     * @return
     */
    List<FzjcxxDto> beforeDayList(FzjcxxDto fzjcxxDto);
    /**
     * 新冠检测结果
     * @param fzjcxxDto
     * @return
     */
    FzjcxxDto getSampleAcceptInfo(FzjcxxDto fzjcxxDto) throws BusinessException;
    /**
     * 获取内部编号
     * @param
     * @return
     */
    String getFzjcxxByybbh();
    /**
     * 获取主键id
     * @param
     * @return
     */
    String getFzjcid(FzjcxxDto fzjcxxDto);
    /**
     * 通过wxid查询患者信息
     * @param wxid
     * @return
     */
    List<FzjcxxDto> getDtoListByWxid(String wxid);
    /**
     * 报告日期查看
     * @param hzxxtDto
     * @return
     */
    List<FzjcxxDto> bgrqDetailsByZjh(HzxxtDto hzxxtDto);

    /**
     * 更新分子检测结果信息
     * @param fzjcxxDto
     * @return
     */
    boolean updateJcjg(FzjcxxDto fzjcxxDto);
    /**
     * 废弃按钮
     * @param fzjcxxDto
     * @return
     */
    boolean discardCovidDetails(FzjcxxDto fzjcxxDto);
    /**
     * 删除分子检测信息
     * @param fzjcxxDto
     * @return
     */
    boolean delCovidDetails(FzjcxxDto fzjcxxDto);
    /**
     * 修改预约日期
     * @param fzjcxxDto
     * @return
     */
    boolean updateAppDate(FzjcxxDto fzjcxxDto);
	 /**
     * 批量更新
     * @param list
     * @return
     */
     int updateList(List<FzjcxxDto> list);
    /**
     * 更新阿里订单信息
     * @param fzjcxxDto
     * @return
     */
    boolean updateAliOrder(FzjcxxDto fzjcxxDto);

    /**
     * 钉钉混检删除操作回滚数据
     * @param fzjcxxDto
     * @return
     */
    boolean callbackFzjcxx(FzjcxxDto fzjcxxDto);

    /**
     * 更新报告完成时间
     * @param fzjcxxDto
     * @return
     */
    boolean updateBgwcsj(FzjcxxDto fzjcxxDto);

}
