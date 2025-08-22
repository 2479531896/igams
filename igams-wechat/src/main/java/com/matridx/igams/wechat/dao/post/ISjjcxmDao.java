package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjjcxmModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ISjjcxmDao extends BaseBasicDao<SjjcxmDto, SjjcxmModel>{

	/**
	 * 根据送检信息新增检测项目
	 * @param sjjcxmDtos
	 * @return
	 */
	int insertSjjcxmDtos(List<SjjcxmDto> sjjcxmDtos);

	/**
	 * 根据项目管理id修改
	 * @param list
	 * @return
	 */
	int updateListNew(List<SjjcxmDto> list);
	/**
	 * 更改应收账款信息
	 * @param dto
	 * @return
	 */
	int updateYszkInfoToNull(SjjcxmDto dto);

	/**
	 * 置导出标记为null
	 * @param dto
	 * @return
	 */
	int updateDcEmpty(SjjcxmDto dto);

	/**
	 * 批量更新对账数据
	 * @param sjjcxmDtos
	 * @return
	 */
	int updateListDzInfo(List<SjjcxmDto> sjjcxmDtos);



	/**
	 * 批量修改
	 * @param sjjcxmDtos
	 * @return
	 */
	int updateList(List<SjjcxmDto> sjjcxmDtos);

	/**
	 * 获取检测项目所以数据
	 * @param sjjcxmDto
	 * @return
	 */
    List<SjjcxmDto> getAllDtoList(SjjcxmDto sjjcxmDto);

	/**
	 * 修改同步信息
	 * @param list
	 * @return
	 */
	int updateSyncInfo(List<SjjcxmDto> list);
	/**
	 * 根据送检信息更新检测项目
	 * @param sjjcxmDtos
	 * @return
	 */
	int updateSjjcxmDtos(List<SjjcxmDto> sjjcxmDtos);
	/**
	 * 查询送检检测项目
	 * @param sjid
	 * @return
	 */
    List<String> getSjjcxm(String sjid);
	
	/**
	 * 删除检测项目
	 * @param sjxxDto
	 * @return
	 */
    int deleteSjjcxm(SjxxDto sjxxDto);
	
	/**
	 * 查询送检检测项目List
	 * @param sjid
	 * @return
	 */
	List<SjjcxmDto> getDtoList(String sjid);
	
	/**
	 * 获取检测项目信息
	 * @param sjxxDto
	 * @return
	 */
	List<SjjcxmDto> getSjjcxmDtos(SjxxDto sjxxDto);
	
	/**
	 * 根据sjid查询检测项目信息
	 * @param sjids
	 * @return
	 */
	List<SjjcxmDto> getListBySjid(List<String> sjids);

	/**
	 * 根据sjid清空检测子项目
	 * @param sjid
	 * @return
	 */
	boolean emptySubDetect(String sjid);


	/**
	 * 还原数据
	 * @param sjjcxmDtos
	 * @return
	 */
	boolean revertData(List<SjjcxmDto> sjjcxmDtos);

	/**
	 * 更新导出标记
	 * @param sjjcxmDto
	 * @return
	 */
	boolean updateDcbj(SjjcxmDto sjjcxmDto);
}
