package com.matridx.igams.experiment.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.WkglDto;
import com.matridx.igams.experiment.dao.entities.WkglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IWkglDao extends BaseBasicDao<WkglDto, WkglModel>{

	/**
	 * 获取文库列表
	 */
	List<WkglDto> getPagedWkglDtoList(WkglDto wkglDto);
	
	/**
	 * 删除文库管理信息
	 */
	boolean deleteWkglxx(WkglDto wkglDto);
	
	/**
	 * 根据文库ID更新文库信息
	 */
	boolean updateWkxxByWkid(WkglDto wkglDto);
	
	/**
	 * 根据检测单位和创建日期查询文库信息
	 */
	List<WkglDto> getWkglByJcdwAndLrsj(WkglDto wkglDto);
	
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
	 * 文库管理信息删除标记置为2
	 */
    boolean delMergeWkgl(WkglDto wkglDto);

	/**
	 * 撤销合并的文库管理数据
	 */
    boolean cancelMergeWkgl(WkglDto wkgl);
	/**
	 * 获取实验管理数据
	 */
	List<Map<String,Object>> getDetectionInfo(Map<String, Object> params);

	/**
	 * 获取pooling相关信息
	 */
	Map<String,Object> getPoolingInfo(WkglDto wkglDto);
}
