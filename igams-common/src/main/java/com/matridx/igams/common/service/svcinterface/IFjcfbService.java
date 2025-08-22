package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IFjcfbService extends BaseBasicService<FjcfbDto, FjcfbModel>{

	/**
	 * 保存文件到临时目录中
	 */
	 boolean save2TempFile(MultipartFile[] files,FjcfbDto fjcfbDto,User user);
	
	/**
	 * 获取文件上传列表
	 */
	 List<Map<String, String>> getImpList(FjcfbDto fjcfbDto, HttpServletRequest request,User user);
	
	/**
	 * 根据附件ID获取导入信息
	 */
	 Map<String, Object> checkImpFileProcess(String fjid);
	
	/**
	 * 保存导入信息到数据库
	 */
	 boolean save2Db(FjcfbDto fjcfbDto,User user);
	
	/**
	 * 根据附件ID检查插入数据库进度
	 */
	 Map<String, Object> checkImpRecordProcess(String fjid);
	
	/**
	 * 根据文件ID从redis中获取信息后，把文件从临时文件夹中移动到正式文件夹中
	 */
	 boolean save2RealFile(String wjid, String ywid);
	/**
	 * 根据文件ID从redis中获取信息后，把文件从临时文件夹中移动到正式文件夹中,并创建新的fjid
	 */
	 boolean saveRealFileNewId(String wjid, String ywid);

	/**
	 * 批量上传附件
	 */
	 boolean batchRealFile(String wjid, String ywid,String fjid);
	
	/**
	 * 根据文件ID从redis中获取信息后，把文件从临时文件夹中移动到正式文件夹中
	 */
	 boolean save3RealFile(String wjid, String ywid,String zywid);
	
	/**
	  * 返回需要插入的附件
	 */
	 List<FjcfbModel> update(String busId, String relatedBusId, List<String> busTypes, List<FjcfbModel> fjs);
	
	/**
	 * 获取不重复下一个序号
	 */
	 String getDRNextSeqNum(FjcfbModel fjcfbDto);
	
	/**
	 * 附件重新排序
	 */
	 boolean updateSeqNum(String ywid, List<String> ywlxs);
	
	/**
	 * 转换失败更新相应的转换次数
	 */
	 int updateFjljFail(FjcfbModel fjcfbModel);
	
	 int getFileTransLimit();
	
	 String getFtpPath();
	
	 String getFtpUrl();
	
	 String getFtpUser();
	
	 String getFtpPD();
	
	 String getFtpPort();
	 String getFtpWordPath();
	
	 String getFtpPdfPath();

	/**
	 * 通过业务ID查询附件信息
	 */
	 List<FjcfbDto> selectFjcfbDtoByYwid(String ywid);
	
	/**
	 * 通过附件IDs查询删除标记为0的附件信息
	 */
	List<FjcfbDto> selectFjcfbDtoByFjids(FjcfbDto fjcfbDto);

	/**
	 * 通过业务ID删除附件信息
	 */
	 boolean deleteByYwid(FjcfbModel fjcfbModel);

	/**
	 * 更新转换标记
	 */
	 boolean updateZhbj(FjcfbModel fjcfbModel);

	/**
	 * 通过业务ID和业务类型查询附件信息
	 */
	 List<FjcfbDto> selectFjcfbDtoByYwidAndYwlx(FjcfbDto fjcfbDto);

	/**
	 * 附件排序
	 */
	 boolean fileSort(FjcfbDto fjcfbDto);

	/**
	 * 删除文件
	 */
	 boolean delFile(FjcfbDto fjcfbDto);
	
	/**
	 * 转换后的文件新增一个zhwjxx
	 */
	 boolean updateZhwjxx(FjcfbModel fjcfbModel);
	
	/**
	 * 查询没有转换过的文件
	 */
	 List<FjcfbDto> selectWord();

	/**
	 * 根据业务ID和业务类型删除附件信息
	 */
	 boolean deleteByYwidAndYwlx(FjcfbModel fjcfbModel);

	/**
	 * 根据业务IDs和业务类型删除附件信息
	 */
	 int deleteByYwidsAndYwlx(FjcfbDto fjcfbDto);
	
	/**
	 * 查询是否有转换为pdf的文件
	 */
	 List<FjcfbDto> selectzhpdf(FjcfbDto fjcfbDto);
	
	/**
	 * 根据业务ID和序号查询
	 */
	 List<FjcfbDto> getListByYwidAndXh(List<FjcfbDto> fjcfbDtos);

	/**
	 * 替换文件
	 */
	 boolean replaceFile(FjcfbModel fjcfbModel);

	/**
	 * 接收word文件转换成pdf
	 */
	 String receiveWord(MultipartFile file);
	
	/**
	 * 根据fjids删除附件
	 */
	 boolean delByFjids(FjcfbDto fjcfbDto);

	/**
	 * 保存送检报告
	 */
	 boolean save2Inspection(FjcfbDto fjcfbDto, User user);

	/**
	 * 从缓存中获取附件信息
	 */
	 List<FjcfbDto> getRedisList(List<String> fjids);

	/**
	 * 根据业务ID和子业务ID查询附件信息
	 */
	 List<FjcfbDto> getListByZywid(FjcfbDto fjcfbDto);
	
	/**
	 * 上传Word文件
	 */
	 boolean sendWordFile(String fileName);
	
	/**
	 * 批量保存附件到正式文件夹
	 */
	 boolean saveFormalFile(List<FjcfbDto> fjcfbDtos,String ywlx,String ywid,String zywid);

	/**
	 * 根据业务IDs和业务类型
	 */
	 List<FjcfbDto> selectFjcfbDtoByYwidsAndYwlx(FjcfbDto fjcfbDto);
	/**
	 * 保存带序号的附件
	 */
	 boolean saveXhFile(String wjid,String ywid,String zywid,String xh);

	/**
	 * 根据业务IDs和业务类型，并根据业务类型排序
	 */
    List<FjcfbDto> selectFjcfbDtoByYwidAndYwlxOrderByYwlx(FjcfbDto fjcfbDto);

	/**
	 * 批量更新附件存放表
	 */
	boolean updateByYwidAndWjmhz(List<FjcfbDto> fjcfbDtos);
	
    /**
     * 删除附件，近删除附件存放表，不删除服务器文件
     */
    boolean delFileOnlyFjcfb(FjcfbDto fjcfbDto);

	/**
	 * 重写getDto方法，防止无条件SQL执行
	 */
	FjcfbDto getDto(FjcfbDto fjcfbDto);

	/**
	 * 批量新增附件存放表信息
	 */
	boolean batchInsertFjcfb(List<FjcfbDto> fjcfbModels);

	/**
	 * 通过业务ID查询删除标记为0的附件信息
	 */
	List<FjcfbDto> getFjcfbDtoByYwid(String yzid);

	/**
	 * 查询删除标记不等于1的附件信息
	 */
	FjcfbDto getDtoWithScbjNotOne(FjcfbDto fjcfbDto);

	/**
	 * 根据ywids获取附件list
	 */
	List<FjcfbDto> selectFjcfbDtoByIds(FjcfbDto fjcfbDto);
	
	/**
	 * 根据fjid 更新删除标记
	 */
	boolean deleteByFjid(FjcfbDto fjcfbDto);

	/**
	 * 根据业务ID和业务类型删修改删除标记
	 * @param fjcfbDto
	 * @return
	 */
	boolean updateScbjYwidAndYwlxd(FjcfbDto fjcfbDto);
	/**
	 * 根据文件ID从redis中获取信息后，把文件从临时文件夹中移动到正式文件夹中 并且保存录入人员
	 */
	boolean saveRealFileForPeo(String wjid, String ywid, String lrry);
	/*
		获取附件存在情况
	 */
	List<FjcfbDto> existFileInfo(FjcfbDto fjcfbDto);
}
