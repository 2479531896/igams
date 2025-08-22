package com.matridx.igams.detection.molecule.dao.post;


import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.dao.entities.HzxxDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxModel;

import java.util.List;
import java.util.Map;

@Mapper
public interface IFzjcxxDao extends BaseBasicDao<FzjcxxDto, FzjcxxModel>{
    /**
     * 新冠列表数据
     */
     List<FzjcxxDto> getPagedDtoList(FzjcxxDto fzjcxxDto);
    /**
     * 获取标本编号
     */
     String getNbbhSerial(String str);

	/**
	 * 查询组装内部编码所需要的的数据
	 * @param fzjcxxDto
	 * @return
	 */
	List<Map<String,String>> getconfirmDmInfo(FzjcxxDto fzjcxxDto);

	/**
	 * 生成自定义流水号
	 * @param map
	 * @return
	 */
	String getCustomSerial(Map<String,Object> map);
    /**
     * 获取标本编号
     */
     String getSyhSerial(Map<String,String> map);
    /**
     * 获取jcxx
     */
     List<FzjcxxDto> getFzjcxxByybbh(FzjcxxDto fzjcxxDto);
    /**
     * 获取jcxx
     */
     List<FzjcxxDto> getInfoByNbbh(FzjcxxDto fzjcxxDto);
    /**
     * 根据nbbh修改
     */
     Integer updateInfoByNbbh(FzjcxxDto fzjcxxDto);
    /**
     * 获取jcxx
     */
     List<FzjcxxDto> getFzjcxxByNbbh(FzjcxxDto fzjcxxDto);

	/**
	 * 获取jcxx
	 */
	 List<FzjcxxDto> getFzjcxxBySyh(FzjcxxDto fzjcxxDto);
    /**
     * 获取jcxx
     */
     Integer saveFzjcxxInfo(FzjcxxDto fzjcxxDto);
    /**
     * 新冠列表查看
     */
     List<FzjcxxDto> viewCovidDetails(FzjcxxDto fzjcxxDto);
    /**
     * 获取新冠报告附件
     */
     List<FjcfbDto> getFjcfb(FzjcxxDto fzjcxxDto);

	/**
     * 新冠检测结果
     */
     List<FzjcxxDto> getCovidJcjg(FzjcxxDto fzjcxxDto);
    /**
     * 从数据库分页获取导出数据
     */
     List<FzjcxxDto> getListForSelectExp(FzjcxxDto fzjcxxDto);
    /**
     * 根据搜索条件获取导出条数
     */
     int getCountForSearchExp(FzjcxxDto fzjcxxDto);
    /**
     * 从数据库分页获取导出数据
     */
     List<FzjcxxDto> getListForSearchExp(FzjcxxDto fzjcxxDto);
    /**
     * 更新审核状态
     */
     int updateZt(FzjcxxDto fzjcxxDto);
    /**
     * 新冠审核列表审核中数据
     */
     List<FzjcxxDto> getPagedAuditIdList(FzjcxxDto fzjcxxDto);




    /**
     * 后台查找当天及之后的预约数据
     */
	 FzjcxxDto afterDayList(HzxxDto hzxxDto);

	/**
	 * 今天当天之前（含当天）的预约数据
	 */
	 List<FzjcxxDto> beforeDayList(HzxxDto sfzh);

	/**
	 * 根据身份证号和预约检测日期查找分子检测信息数据
	 */
	 List<FzjcxxDto> getxxBysfzhAndrq(FzjcxxDto fzjcxxDto);

	/**
	 * 查询退款订单信息
	 */
	 List<FzjcxxDto> getRefundOrder(FzjcxxDto fzjcxxDto);

	/**
	 * 生成标本编号
	 */
	 String generateFzjcYbbh(String prefix);

	/**
	 * 通过主键分子检测id查找分子检测信息
	 */
	 FzjcxxDto getFjzcByJcid(FzjcxxDto fzjcxxDto);

	/**
	 * 提交更新分子检测信息
	 */
	 boolean updateConfirm(FzjcxxDto fzjcxxDto);
	
    /*新增预约信息到分子检测信息表*/
    boolean insertDetectionAppointmentFzjcxx(FzjcxxDto fzjcxxDto);
    /*更新预约信息到分子检测信息表*/
    boolean updateDetectionAppointmentFzjcxx(FzjcxxDto fzjcxxDto);

    /*取消已预约的患者信息*/
    boolean cancelDetectionAppointment(FzjcxxDto fzjcxxDto);
	/**
	 * 查询生成报告数据
	*/
	 Map<String, Object> generateReport(FzjcxxDto fzjcxxDto);
	/**
     * 报告日期查看
     */
     List<FzjcxxDto> bgrqDetails(FzjcxxDto fzjcxxDto);
	/**
     * 点击实验按钮判断检查项目
     */
     List<FzjcxxDto> checkJcxm(FzjcxxDto fzjcxxDto);
	/**
	 * 查询模板路径
	*/
	 String getWjlj(String ywid);
	
	
	/**
	 * 查询模板路径
	 */
	 List<FzjcxxDto> getWjljByfzjcid(String fzjcid);
	/**
 	 * 检测项目明细
     */
	 List<Map<String,String>> generateReportList(FzjcxxDto fzjcxxDto);
	 
     /**
     * 更新实验状态
     */
     boolean updateSyzt(FzjcxxDto fzjcxxDto);
	
    /**
     * 通过主键ID更新分子检测信息表是否确认字段
     */
	 boolean UpdateSfqrById(FzjcxxDto fzjcxxDto);

    /**
     * 获取患者历史检测信息
     *
     */
     List<FzjcxxDto> getHistoryList(FzjcxxDto fzjcxxDto);
	
    /**
   	 * 查询科室信息
   	 */
   	 List<Map<String,String>> getSjdw();
	/**
	 * 根据ybbhs查询检测信息
	 */
	 List<FzjcxxDto> getFzjcxxByybbhs(FzjcxxDto fzjcxxDto);
	/**
	 * 获取个人word报告
	 */
	 List<FjcfbDto> getReport(FjcfbDto fjcfbDto);
	/**
	 * 根据标本申请审核的ID列表获取审核列表详细信息
	 */
	 List<FzjcxxDto> getAuditListByIds(List<FzjcxxDto> fzjcxxDtos);
	/**
	 * 修改分子检测信息
	 */
	 int saveEditCovid(FzjcxxDto fzjcxxDto);

	/**
	 * 更新报告日期
	 */
	 boolean updateBgrq(FzjcxxDto fzjcxxDto);

	/**
	 * 获取分子检测实验列表数据
	 */
    List<FzjcxxDto> getPagedDetectlab(FzjcxxDto fzjcxxDto);

	/**
	 * 更新分子检测结果信息
	 */
	 boolean updateJcjg(FzjcxxDto fzjcxxDto);
	
	/**
 	 * 生成报告
     */
	 List<Map<String,String>> greatReport(FzjcxxDto fzjcxxDto);
	 
	/**
 	 * 生成报告
     */
	 List<Map<String,String>> greatReportList(String fzxmid);
	/**
	 * 删除按钮
	 */
	 boolean delCovidDetails(FzjcxxDto fzjcxxDto);

	/**
	 * 废弃按钮
	 */
	 boolean discardCovidDetails(FzjcxxDto fzjcxxDto);



	/*通过审核过程中的ywid查找出分子检测信息数据list*/
    List<FzjcxxDto> getFzjcxxListByYwids(List<ShgcDto> shgcList);

	/*通过审核过程中的ywids查找出分子检测信息数据list*/
	List<FzjcxxDto> getListByIds(FzjcxxDto fzjcxxDto);

	/* 通过dto的ids字段查找分子检测信息数据 */
    List<FzjcxxDto> getFzjcxxListByIds(FzjcxxDto fzjcxxDto);

	/*根据shgclist中的ywid跟新数据的分子检测信息表的发送时间*/
	void updateFssjByYwids(List<ShgcDto> shgcList);

	/*更新分子检测信息表的发送时间和发送标记*/
	void updateFssjAndFsbj(List<FzjcxxDto> fzjcxxList);

	/**
	 * 更新分子检测信息数据集的批次任务号和单次任务号
	 */
    void updateTaskid(Map<String, Object> map);

	/**
	 * 更新分子检测信息表中对应单次任务号的检测结果字段
	 */
	void updateScjg(Map<String, Object> map);

	/**
	 * 查找样本编号对应的分子检测ID
	 */
	List<Map<String, String>> getYbbhAndFzjcid(String[] ybbhs);

	//根据输入样本编号list去分子检测表中查找出存在的编号list
    List<String> getYbbhList(String[] ybbhs);

	List<String> getYbbhsBySyhExist(String[] ybbhs);

	/**
	 * 根据标本编号获取分子检测信息
	 */
	 List<FzjcxxDto> getFzjcxxByIds(FzjcxxDto fzjcxxDto);

	/**
	 * 生成标本子编号
	 */
    String generateFzjcBbzbh(String prefix);

	/**
	 * 通过标本标号更新分子检测确认信息
	 */
	boolean UpdateSfqrByYbbh(FzjcxxDto fzjcxxDto);
	/**
	 * 选择查询信息
	 */
	 List<FzjcxxDto> getCheckDtoList(FzjcxxDto fzjcxxDto);
    /**
	 * 更新发送时间和发送标记
	 */
    void updateFssjByIds(List<FzjcxxDto> fzjcxxlistx);
	
	/**
	 * 获取平台未付款记录
	 */
	 List<FzjcxxDto> getUnpaidByPt(FzjcxxDto fzjcxxDto);

	/**
	 * 批量更新
	 */
	 int updateList(List<FzjcxxDto> list);

	/**
	 * 根据选择查询报告信息(pdf结果)
	 */
	 List<FzjcxxDto> selectDownloadReportByIds(FzjcxxDto fzjcxxDto);

	/**
	 * 根据条件查询需要下载的报告信息
	 */
	 List<FzjcxxDto> selectDownloadReport(FzjcxxDto fzjcxxDto);

    List<FzjcxxDto> getExistBbzbh(FzjcxxDto fzjcxxDto);

	/**
	 * 修改预约日期
	 */
	 boolean updateAppDate(FzjcxxDto fzjcxxDto);
	/**
	 * 更新阿里订单信息
	 */
	 boolean updateAliOrder(FzjcxxDto fzjcxxDto);

	/**
	 * 查找数据库中scbj为0且标本子编号为某值的数据
	 */
	List<FzjcxxDto> getCountBbzbh(FzjcxxDto fzjcxxDto);

	/**
	 * 通过分子检测ID获取患者ID
	 */
    String getHzidByFzjcid(String fzjcid);

	FzjcxxDto getOderDtoByFzjcid(FzjcxxDto fzjcxxDto);

	/**
	 * 获取当前用户当前采样点的统计数据（管数）
	 */
	Map<String,Object> getCountData(FzjcxxDto fzjcxxDto);
	/**
	 * 获取当前用户当前采样点的统计数据（人数）
	 */
	List<FzjcxxDto> getPeopleCountData(FzjcxxDto fzjcxxDto);
    /**
	 * 更新分子检测表卫建上报的上报时间
	 */
	boolean updateSbsj(Map<String, Object> map);
	
	/**
	 * 查找单混检标本编号除外的数据是否存在相同实验号
	 */
    List<FzjcxxDto> getExistBySyhYbbh(FzjcxxDto fzjcxxDto);

	
	/**
	 * 钉钉小程序回滚分子检测信息
	 */
	 boolean callbackFzjcxx(FzjcxxDto fzjcxxDto);
	
	/**
	 * 得到删除标记为0的数据
	 */
	FzjcxxDto getFzjcxxDto(FzjcxxDto fzjcxxDto);

	/**
	 * 更新报告完成时间
	 */
	boolean updateBgwcsj(FzjcxxDto fzjcxxDto);

	/**
	 * 锁定新冠标本
	 */
	boolean lockSample(FzjcxxDto fzjcxxDto);

    boolean updateSfjfFzjcxx(FzjcxxDto fzjcxxDto);

	/**
	 * 钉钉获取数据
	 */
	List<FzjcxxDto> getFzjcxxList(FzjcxxDto fzjcxxDto);

	/**
	 * 保存履历信息
	 */
	int insertInfoResume(FzjcxxDto fzjcxxDto);

	/**
	 * 更新采集信息上传卫健成功的fzjcxxlist
	 */
    void updateCjxxsc(List<FzjcxxDto> list);

	/**
	 * 履历信息
	 */
	List<FzjcxxDto> getResumeList(FzjcxxDto fzjcxxDto);
}
