package com.matridx.las.netty.service.impl;

import com.matridx.las.netty.dao.entities.AgvEpModel;
import com.matridx.las.netty.dao.entities.YsybxxDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.las.netty.dao.entities.YblcsjDto;
import com.matridx.las.netty.dao.entities.YblcsjModel;
import com.matridx.las.netty.dao.post.IYblcsjDao;
import com.matridx.las.netty.service.svcinterface.IYblcsjService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class YblcsjServiceImpl extends BaseBasicServiceImpl<YblcsjDto, YblcsjModel, IYblcsjDao> implements IYblcsjService{

	@Autowired
	private IYblcsjDao yblcsjDao;
	
	@Override
	public boolean updateYblcsjById(YblcsjDto dto) {
		// TODO Auto-generated method stub
		return yblcsjDao.updateYblcsjById(dto)>0;
	}


	@Override
	public boolean updateYblcsjByList(Map<String, Object> map,String index,String startOrEnd) {
		List<Map> mapList = new ArrayList<>();
		List<YblcsjDto> yblcsList=new ArrayList<>();
		List<AgvEpModel> agvEpModelList = (List<AgvEpModel>) map.get("list");
		for(AgvEpModel ma:agvEpModelList){
			YblcsjDto yblcsjDto = new YblcsjDto();
			yblcsjDto.setYsybid(ma.getYsybid());
			SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
			switch (startOrEnd){
				case "pzyks":
					if("1".equals(index)){
						yblcsjDto.setPzykssj(sdf.format(new Date()));
					}else if("2".equals(index)){
						yblcsjDto.setDecpykssj(sdf.format(new Date()));
					}
					break;
				case "pcrjs":
					if("1".equals(index)){
						yblcsjDto.setDycdljssj(sdf.format(new Date()));
					}else if("2".equals(index)){
						yblcsjDto.setDecdlyjssj(sdf.format(new Date()));
					}
					break;
			}



			yblcsList.add(yblcsjDto);
		}

		return yblcsjDao.updateYblcsjByList(yblcsList)>0;
	}

	@Override
	public YblcsjDto getByYsybId(YblcsjDto dto) {
		// TODO Auto-generated method stub
		return yblcsjDao.getByYsybId(dto);
	}

	@Override
	public boolean insertList(List<YblcsjDto> list) {

		return yblcsjDao.insertList(list)>0;
	}

	/**
	 * 获取原始样本list
	 * @param list
	 * @return
	 */
	@Override
	public List<YblcsjDto> getYsybxxDtoList(List<YblcsjDto> list) {
		List<YblcsjDto> list1 = dao.getYblcsjDtoList(list);

		return list1;
	}

}
