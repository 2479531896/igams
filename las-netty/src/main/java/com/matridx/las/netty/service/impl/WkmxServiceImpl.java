package com.matridx.las.netty.service.impl;

import java.text.SimpleDateFormat;
import java.util.*;

import com.matridx.las.netty.dao.entities.*;
import com.matridx.las.netty.dao.post.IWkglDao;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.util.RedisSetAndGetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.las.netty.dao.post.IWkmxDao;
import com.matridx.las.netty.service.svcinterface.IWkmxService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class WkmxServiceImpl extends BaseBasicServiceImpl<WkmxDto, WkmxModel, IWkmxDao> implements IWkmxService{

	@Autowired
	private IWkmxDao wkmxDao;
	@Autowired
	private IWkglDao wkglDao;



	@Override
	/***
	 * 保存文库明细----auto第一次开始
	 */
	@Transactional
	public void saveWkmx(List<AgvEpModel> listMap, SjlcDto sjlcDto, Map<String,String> ma) {
		// TODO Auto-generated method stub
		String wkid = StringUtil.generateUUID();
		Map<String,Object> libMap=new HashMap<>();
		libMap.put("wkid",wkid);
		libMap.put("list",listMap);

		InstrumentStateGlobal.setLibaryInfMap(ma.get(sjlcDto.getZyyqlx()),libMap);
		SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
		String dateStr = sdf.format(new Date());
		List<WkmxDto> list =new ArrayList<>();
		if(listMap!=null){
			for(int i=0;i<listMap.size();i++) {
				WkmxDto dto =new WkmxDto();
				//添加样本编号，方便后面通过文库id和样本编号找到文库明细
				AgvEpModel agvEpModel = listMap.get(i);

				dto.setWkmxid(StringUtil.generateUUID());
				dto.setWkid(wkid);
				dto.setLrsj(dateStr);
				dto.setScbj("0");
				dto.setWkxzb(agvEpModel.getXzb());//文库x坐标
				dto.setWkyzb(agvEpModel.getYzb());//文库y坐标
				dto.setHsxzb(agvEpModel.getXzb());//核酸x坐标
				dto.setHsyzb(agvEpModel.getYzb());//核酸y坐标
				//int[] xy=getXY(i+1);
//				dto.setWkxzb(xy[0]+"");//文库x坐标
//				dto.setWkyzb(xy[1]+"");//文库y坐标
//				dto.setHsxzb(xy[0]+"");//核酸x坐标
//				dto.setHsyzb(xy[1]+"");//核酸y坐标

				//todo
				dto.setYsybid(agvEpModel.getYsybid());
				dto.setNbbh(agvEpModel.getNbbh());
				/*dto.setYsybid(map.get("ysybid").toString());//原始样本id
				if(map!=null&&map.get((i+1)+"")!=null){
					dto.setNbbh(map.get((i+1)+"").toString());
				}*/
				list.add(dto);
			}
			WkglDto wkglDto =new WkglDto();
			wkglDto.setLrsj(dateStr);
			wkglDto.setWkid(wkid);
			wkglDao.insertWkgl(wkglDto);
			wkmxDao.saveWkmx(list);
			InstrumentStateGlobal.setAutoIdWkid(wkid,ma.get(sjlcDto.getZyyqlx()));
			InstrumentStateGlobal.setWkidByAutoId(ma.get(sjlcDto.getZyyqlx()),wkid);

		}

	}

	@Override
	public List<WkmxDto> getWkmxByWkid(WkmxDto dto) {
		return wkmxDao.getWkmxByWkid(dto);
	}

	//根据数量生产auto需要的位置
	private int[] getXY(int i) {
		int[] xy = new int[2];
		xy[0] = (int) Math.ceil(Double.valueOf(i) / 8);
		xy[1] = (i % 8) == 0 ? 8 : (i % 8);
		return xy;
	}

	/**
	 * 生成polling文件
	 * @param wkid
	 * @return
	 */
	@Override
	public List<WkmxDto> generateReportList(String wkid) {
		// TODO Auto-generated method stub
		return dao.generateReportList(wkid);
	}

	/**
	 * 修改文库体积液
	 * @param wkmxDtos
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateList(List<WkmxDto> wkmxDtos) {
		// TODO Auto-generated method stub
		return dao.updateList(wkmxDtos)>0;
	}

	/**
	 * 修改文库名
	 * @param wkid
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public boolean updateWkmc(String wkid) {
		// TODO Auto-generated method stub
		return dao.updateWkmc(wkid)>0;
	}
	@Override
	public List<WkmxDto>getWkmxList(WkmxDto dto){
		return dao.getWkmxList(dto);
	}

	/**
	 * 根据wkid和内部编号批量更改信息
	 * @param wkmxDtos
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@Override
	public boolean updateListByWkidAndNbbh(List<WkmxDto> wkmxDtos){
		return dao.updateListByWkidAndNbbh(wkmxDtos)!=0;
	}

	@Override
	public WkmxDto getDaoInfo(WkmxDto dto) {
		return dao.getDaoInfo(dto);
	}

}
