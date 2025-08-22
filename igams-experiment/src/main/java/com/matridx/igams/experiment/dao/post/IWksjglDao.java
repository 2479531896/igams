package com.matridx.igams.experiment.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.WksjglDto;
import com.matridx.igams.experiment.dao.entities.WksjglModel;

import java.util.List;
import java.util.Map;

@Mapper
public interface IWksjglDao extends BaseBasicDao<WksjglDto, WksjglModel>{

	/**
	 * 根据文库ID获取文库上机信息
	 */
	WksjglDto getDtoByWkid(WksjglDto wksjglDto);

	/**
	 * 获取文库上机信息
	 */
	List<WksjglDto> getWksjxxByWkid(String wkid);

	/**
	 * 删除文库上机管理表数据（删除标记置为2）
	 */
    boolean deleteDto(WksjglDto wksjglDto);

	/**
	 * 批量删除
	 */
    boolean deleteDtoList(List<WksjglDto> delsjglList);

	/**
	 * 撤销删除文库上机管理（删除标记置为0）
	 */
    boolean cancelWksjglDeleteByWkid(List<WksjglDto> wksjglDtos);

	/**
	 * 更新测序仪
	 */
    boolean updateCxy(WksjglDto wksjglDto);

	boolean delList(List<WksjglDto> wksjglDtos);

    List<Map<String, Object>> getChipByWkids(WksjglDto wksjglDto);
}
