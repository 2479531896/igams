package com.matridx.las.netty.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.netty.dao.entities.AgvEpModel;
import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.las.netty.dao.entities.WkmxDto;
import com.matridx.las.netty.dao.entities.WkmxModel;

public interface IWkmxService extends BaseBasicService<WkmxDto, WkmxModel>{


	public void saveWkmx(List<AgvEpModel> listMap, SjlcDto sjlcDto, Map<String,String> ma);

	public List<WkmxDto> getWkmxByWkid(WkmxDto dto);

	public List<WkmxDto> generateReportList(String wkid);

	public boolean updateList(List<WkmxDto> wkmxDtos);

	public boolean updateWkmc(String wkid);

	public List<WkmxDto>getWkmxList(WkmxDto dto);

	/**
	 * 根据wkid和内部编号批量更改信息
	 * @param wkmxDtos
	 * @return
	 */
	public boolean updateListByWkidAndNbbh(List<WkmxDto> wkmxDtos);

	/**
	 * 根据wkid和内部编号获取信息
	 * @param dto
	 * @return
	 */
	public WkmxDto getDaoInfo(WkmxDto dto);
}
