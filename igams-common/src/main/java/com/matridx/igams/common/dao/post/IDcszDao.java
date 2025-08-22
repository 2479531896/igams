package com.matridx.igams.common.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.CydcszModel;
import com.matridx.igams.common.dao.entities.CydcxxModel;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DcszModel;

@Mapper
public interface IDcszDao extends BaseBasicDao<DcszDto, DcszModel>{

	/**
	 * 已选字段
	 */
	 List<DcszDto> getChoseList(DcszDto dcszDto);
	
	/**
	 * 未选字段
	 */
	 List<DcszDto> getCateWaitList(DcszDto dcszDto);

	/**
	 * 删除已选字段配置
	 */
	 int delete(DcszDto dcszDto);
	
	/**
	 * 获取常用导出信息
	 */
	 List<CydcxxModel> getCydcxxs(CydcszModel cydcszModel);
	
	/**
	 * 获取常用导出设置
	 */
	 List<CydcszModel> getCydcszs(DcszModel dcszModel);
	
	/**
	 * 保存用户所选择的导出设置
	 */
	 int batchInsertMap(Map<String,Object> map);
	
	/**
	 * 根据客户选择的字段，从数据库重新获取全部信息
	 */
	 List<DcszDto> getSelectedList(Map<String, Object> param);
	
	/**
	 * 删除已有数据
	 */
	 void deleteLs(DcszModel dcszModel);
	/**
	 * 根据ywid获取全部信息
	 */
	 List<DcszDto> getListById(String ywid);
}
