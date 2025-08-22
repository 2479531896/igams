package com.matridx.igams.bioinformation.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.bioinformation.dao.entities.WkdlfxDto;
import com.matridx.igams.bioinformation.dao.entities.WkdlfxModel;
import com.matridx.igams.bioinformation.dao.post.IWkdlfxDao;
import com.matridx.igams.bioinformation.service.svcinterface.IWkdlfxService;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class WkdlfxServiceImpl extends BaseBasicServiceImpl<WkdlfxDto, WkdlfxModel, IWkdlfxDao> implements IWkdlfxService {


	/**
	 * 批量新增
	 */
	public boolean insertList(List<WkdlfxDto> list){
		return dao.insertList(list);
	}

	public List<WkdlfxDto> getDtoList(WkdlfxDto wkdlfxDto){
		List<WkdlfxDto> dtoList = dao.getDtoList(wkdlfxDto);
		if (null != dtoList && dtoList.size() > 0){
			for (WkdlfxDto dto : dtoList) {
				if (StringUtil.isNotBlank(dto.getNr())){
					JSONObject jsonObject = JSONObject.parseObject(dto.getNr());
					dto.setVirulencefactor(jsonObject.get("dlyz").toString());
					dto.setCategory(jsonObject.get("dlxglb").toString());
					dto.setGene(jsonObject.get("dljy").toString());
					dto.setReads(jsonObject.get("dbreads").toString());
					dto.setSpecies(jsonObject.get("vfdb").toString());
				}
			}
		}
		return dtoList;
	}
}
