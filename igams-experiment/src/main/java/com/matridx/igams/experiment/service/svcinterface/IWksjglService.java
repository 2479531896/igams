package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.CxyxxDto;
import com.matridx.igams.experiment.dao.entities.WksjglDto;
import com.matridx.igams.experiment.dao.entities.WksjglModel;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface IWksjglService extends BaseBasicService<WksjglDto, WksjglModel>{

	/**
	 * 根据文库ID获取文库上机信息
	 */
	WksjglDto getDtoByWkid(WksjglDto wksjglDto);

	/**
	 * 保存文库上机信息
	 */
	boolean addSaveWksj(WksjglDto wksjglDto);

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

	Map<String, Object> downloadChipExport(WksjglDto wksjglDto, HttpServletResponse response);

	/**-
	 * 切换测序仪时候获取wksjmx
	 * @param cxyxxDto
	 * @param nddw
	 * @param sfjyh
	 * @param wkglService
	 * @return
	 */
	Map<String, Object>getWksjmxInfo(CxyxxDto cxyxxDto,String nddw,String sfjyh,IWkglService wkglService);
}
