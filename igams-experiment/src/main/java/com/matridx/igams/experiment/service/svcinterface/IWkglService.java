package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.WkglDto;
import com.matridx.igams.experiment.dao.entities.WkglModel;
import com.matridx.igams.experiment.dao.entities.WkmxDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface IWkglService extends BaseBasicService<WkglDto, WkglModel>{

	/**
	 * 获取文库列表
	 */
	List<WkglDto> getPagedWkglDtoList(WkglDto wkglDto);
	
	/**
	 * 删除文库信息
	 */
	boolean deleteWkxxlist(WkglDto wkglDto);
	
	/**
	 * 解析pooling数据
	 */
	Map<String,Object>  analysisPooling(String filePath);
		
	/**
	 * 查询当天已建立的文库数量
	 */
	WkglDto getCountByDay(WkglDto wkglDto);
	
	/**
	 * 根据文库名称查询文库信息
	 */
	WkglDto getDtoByWkmc(WkglDto wkglDto);
	
	/**
	 * 查询pooling表模板
	 */
	Map <String,Object> getPoolingTemplatePath(WkglDto wkglDto);

	/**
	 * 查询文库t\q数量
	 */
	Map <String,Object> getPoolingLibraryCount(String wkid);
	/**
	 * 根据文库ID更新文库信息
	 */
	boolean updatesjph(WkglDto wkglDto);
	/**
	 * 更新测序仪
	 */
	boolean updateYqlx(WkglDto wkglDto);

	/**
	 * 更新文库上机上传时间
	 */
	boolean updateSjscsj(WkglDto wkglDto);

	/**
	 * 根据文库ID更新文库信息
	 */
	boolean updateWkxxByWkid(WkglDto wkglDto);
	/**
	 * 删除合并的文库信息
	 */
	boolean deleteMergeWkxx(WkglDto wkglDto);

	/**
	 * 撤销文库合并数据
	 */
	boolean cancelMergeWkxx(WkglDto wkglDto);

	/**
	 * 取消合并方法（删除合并的数据，恢复合并前的数据）
	 */
    boolean cancelMerge(WkglDto wkglDto);

	/**
	 * 文库合并保存方法
	 */
    boolean mergeSaveWk(WkmxDto wkmxDto);

	/**
	 * 外部同步文库信息
	 */
	Map<String,Object> syncLibraryInfo(HttpServletRequest request);
	/**
	 * 获取实验管理数据
	 */
	List<Map<String,Object>> getDetectionInfo(Map<String, Object> params);
	/**
	 * 获取pooling相关信息
	 */
	Map<String,Object> getPoolingInfo(WkglDto wkglDto);

	Map<String,Object> dealPooping(WkglDto wkglDto,HttpServletRequest request);

	/**
	 * pooling系数批量调整 设置 处理
	 * @param wkglDto
	 * @return
	 */
	//List<Map<String,Object>> generatePoolingCoefficient(WkglDto wkglDto);
	/**
	 * pooling系数批量调整 设置 处理
	 * @param wkglDto
	 * @return
	 */
	Map<String,Object> generatePoolingSetting(WkglDto wkglDto);
}
