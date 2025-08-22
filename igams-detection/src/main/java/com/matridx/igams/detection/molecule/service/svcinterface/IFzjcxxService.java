package com.matridx.igams.detection.molecule.service.svcinterface;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxModel;
import com.matridx.igams.detection.molecule.dao.entities.HzxxDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IFzjcxxService extends BaseBasicService<FzjcxxDto, FzjcxxModel>{
    /**
     * 删除按钮
     */
     boolean delCovidDetails(FzjcxxDto fzjcxxDto);
    /**
     * 废弃按钮
     */
     boolean discardCovidDetails(FzjcxxDto fzjcxxDto);
    /**
     * 新冠列表数据
     */
     List<FzjcxxDto> getPagedDtoList(FzjcxxDto fzjcxxDto);
    /**
     * 新冠列表查看
     */
     List<FzjcxxDto> viewCovidDetails(FzjcxxDto fzjcxxDto);
    /**
     * 新冠检测结果
     */
     FzjcxxDto getSampleAcceptInfo(FzjcxxDto fzjcxxDto) throws BusinessException;

    /**
     * 新冠检测结果
     */
     List<FzjcxxDto> getSampleAcceptInfoList(FzjcxxDto fzjcxxDto) throws BusinessException;
    /**
     * 新冠检测结果
     */
     FzjcxxDto getInfoByNbbh(FzjcxxDto fzjcxxDto) throws BusinessException;
    /**
     * 根据nbbh修改
     */
     Boolean updateInfoByNbbh(FzjcxxDto fzjcxxDto);
    /**
     * 获取内部编号
     */
     String getFzjcxxByybbh();

    String generateYbbh(FzjcxxDto fzjcxxDto);

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
     boolean saveFzjcxxInfo(FzjcxxDto fzjcxxDto) throws BusinessException;

    /**
     * 获取实验号
     */
     String getSyhSerial(String jclx) throws BusinessException;
    /**
     * 获取新冠报告附件
     */
     List<FjcfbDto> getFjcfb(FzjcxxDto fzjcxxDto);
    /**
     * 从数据库分页获取导出数据
     */
     List<FzjcxxDto> getListForSelectExp(Map<String, Object> params);
    /**
     * 根据搜索条件获取导出条数
     */
     int getCountForSearchExp(FzjcxxDto fzjcxxDto,Map<String,Object> params);
    /**
     * 根据搜索条件获取导出信息
     */
     List<FzjcxxDto> getListForSearchExp(Map<String, Object> params);
    
   /**
     * 获取预约的数据
     */
	 Map<String, Object> getAppointList(HzxxDto hzxxDto);
	
	/**
	 * 检查预约数据，重复判断的处理
     */
	 Map<String, String> checkAppointData(FzjcxxDto fzjcxxDto, HzxxDto hzxxDto) throws Exception;

	
	/**
 	 * 得到过期的预约数据
	 */
	 List<FzjcxxDto> overdueAppoint(HzxxDto hzxxDto);
	
	/**
	 * 生成报告结果
	 */
	 Map<String, Object> generateReport(FzjcxxDto fzjcxxDto)throws BusinessException;

    /**
     * 重新生成报告结果
     */
     Map<String, Object> generateReportSec(FzjcxxDto fzjcxxDto)throws BusinessException;
	
	 /**
     * 处理新冠检测数据，并分析生成新冠报告
     */
     boolean dealCovidReport(FzjcxxDto fzjcxxDto) throws BusinessException;

    /*新增预约信息到分子检测信息表*/
    boolean insertDetectionAppointmentFzjcxx(FzjcxxDto fzjcxxDto);
    /*更新预约信息到分子检测信息表*/
    boolean updateDetectionAppointmentFzjcxx(FzjcxxDto fzjcxxDto);

    /*取消已预约的患者信息*/
    boolean cancelDetectionAppointment(FzjcxxDto fzjcxxDto);
    /**
     * 报告日期查看
     */
     List<FzjcxxDto> bgrqDetails(FzjcxxDto fzjcxxDto);
    /**
     * 点击实验按钮判断检查项目
     */
     List<FzjcxxDto> checkJcxm(FzjcxxDto fzjcxxDto);
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
     */
     List<FzjcxxDto> getHistoryList(FzjcxxDto fzjcxxDto);
    /**
   	 * 查询科室信息
   	 */
   	 List<Map<String,String>> getSjdw();
    /**
     * 获取个人word报告
     */
     List<FjcfbDto> getReport(FjcfbDto fjcfbDto);

    /**
     * 修改分子检测信息
     */
     boolean saveEditCovid(FzjcxxDto fzjcxxDto);

     boolean modSaveDetectionResult(FzjcxxDto fzjcxxDto);
     List<FzjcxxDto> getPagedAuditList(FzjcxxDto fzjcxxDto);

    /**
     * 获取检测实验数据
     */
    List<FzjcxxDto> getPagedDetectlab(FzjcxxDto fzjcxxDto);
    
    /**
     * 生成检验结果报告
     */
     Map<String, Object> testResultReport(FzjcxxDto fzjcxxDto)throws BusinessException;

    /**
     * 通过审核过程中的ywid查找出分子检测信息数据list
     */
    List<FzjcxxDto> getFzjcxxListByYwids(List<ShgcDto> shgcList);

    /**
     * 批量上传新冠核酸检测数据至卫建委
     */
    boolean dataReportToCity(FzjcxxDto fzjcxxDto, User user);

     boolean reportgenerate(FzjcxxDto fzjcxxDto);

    /**
     * 更新分子检测信息的数据
     */
    void updateScjg(Map<String, Object> map);

    /**
     * 查找样本编号对应的分子检测ID
     */
    List<Map<String, String>> getYbbhAndFzjcid(String[] ybbhs);

    /**
     * 根据输入样本编号list去分子检测表中查找出存在的编号list
     */
    List<String> getYbbhList(String[] ybbhs);

    /**
     * 根据输入样本编号list去分子检测表中查找出存在Syh的编号list
     */
    List<String> getYbbhsBySyhExist(String[] ybbhs);

    /**
     * 根据标本编号获取分子检测信息
     */
     List<FzjcxxDto> getFzjcxxByIds(FzjcxxDto fzjcxxDto);
    

    /**
     * 给相应人员发送文件消息(微信、短信)
     */
    boolean sendDetectionMessage(Map<String,Object> msgMap) throws BusinessException;
	
	/**
     * 通过标本标号更新分子检测确认信息
     */
    boolean UpdateSfqrByYbbh(FzjcxxDto fzjcxxDto);
    /**
     * 选择查询信息
     */
     List<FzjcxxDto> getCheckDtoList(FzjcxxDto fzjcxxDto);
    /**
     * 根据ybbhs查询检测信息
     */
     List<FzjcxxDto> getFzjcxxByybbhs(FzjcxxDto fzjcxxDto);
	
    /**
     * 增加分子物检信息
     */
    String insertObjDetection(FzjcxxDto fzjcxxDto);
	
	/**
     * 阿里健康、橄榄枝 对外预约保存
     */
     String saveDetAppointmentInfo(String ipAddress,String platform, String appointmentInfo);


    /**
     * 阿里健康、橄榄枝 对外取消预约
     */
     String cancelDetAppointmentInfo(String ipAddress,String platform,String appointmentInfo);


    /**
     * 服务商确认履约完成
     */
     boolean confirmPerformance(FzjcxxDto fzjcxxDto) throws Exception;
     boolean confirmPerformanceToGanlanzhi(FzjcxxDto fzjcxxDto) throws Exception;

    /*通过审核过程中的ywids查找出分子检测信息数据list*/
    List<FzjcxxDto> getListByIds(FzjcxxDto fzjcxxDto);
       /**
     * 获取ip地址
     */
     String getIpAddress(HttpServletRequest request);
/**
     * 获取平台未付款记录
     */
     List<FzjcxxDto> getUnpaidByPt(FzjcxxDto fzjcxxDto);

    /**
     * 批量更新
     */
     int updateList(List<FzjcxxDto> list);
    /**
     * 根据条件下载报告压缩包
     */
     Map<String,Object> reportZipDownload(FzjcxxDto fzjcxxDto, HttpServletRequest request);

    /**
     * 查找数据库除自身以外是否还存在某标本子编号数据
     */
    List<FzjcxxDto> getExistBbzbh(FzjcxxDto fzjcxxDto);
	
	/**
     * 阿里回传报告
     */
     boolean dataReportToAli(FzjcxxDto fzjcxxDto);
    /**
     * 修改预约日期
     */
     boolean updateAppDate(FzjcxxDto fzjcxxDto);
    /**
     * 更新阿里订单信息
     */
     boolean updateAliOrder(FzjcxxDto fzjcxxDto);

    /**
     * 小程序扫预约码更新分子检测信息
     */
     boolean modSaveCovid(FzjcxxDto fzjcxxDto) throws Exception;

    /**
     * 小程序扫码更新物检分子检测信息
     */
     boolean materialDetConfirm(FzjcxxDto fzjcxxDto) throws Exception;

    /**
     * 通过分子检测id获取患者ID
     */
    String getHzidByFzjcid(String fzjcid);

    /**
     * 通过分子检测ID查找某条预约数据
     */
    FzjcxxDto getOderDtoByFzjcid(FzjcxxDto fzjcxxDto);

    /**
     * 获取当前用户当前采样点的统计数据（管数）
     */
    Map<String,Object> getCountData(FzjcxxDto fzjcxxDto);
    /**
     * 获取当前用户当前采样点的统计数据(人数)
     */
    List<FzjcxxDto> getPeopleCountData(FzjcxxDto fzjcxxDto);
    /**
     * 查找单混检标本编号除外的数据是否存在相同实验号
     */
    List<FzjcxxDto> getExistBySyhYbbh(FzjcxxDto fzjcxxDto);

	
	/**
     * 钉钉小程序混检页面删除操作
     */
    boolean callbackCovid(FzjcxxDto fzjcxxDto);

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
     boolean positiveLock(FzjcxxDto fzjcxxDto);

    /**
     * 修改分子检测信息是否交付状态
     */
    boolean updateSfjfFzjcxx(FzjcxxDto fzjcxxDto);

    /**
     * 钉钉获取数据
     */
    List<FzjcxxDto> getFzjcxxList(FzjcxxDto fzjcxxDto);

    /**
     * 保存履历信息
     */
    Boolean insertInfoResume(FzjcxxDto fzjcxxDto);
    /**
     * 采集信息上报卫健
     */
    boolean cjsjReport(FzjcxxDto fzjcxxDto);

    /**
     * 履历信息
     */
    List<FzjcxxDto> getResumeList(FzjcxxDto fzjcxxDto);
}
