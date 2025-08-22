package com.matridx.igams.bioinformation.service.impl;


import com.matridx.igams.bioinformation.dao.entities.BioWzglDto;
import com.matridx.igams.bioinformation.dao.entities.BioWzglModel;
import com.matridx.igams.bioinformation.dao.post.IBioWzglDao;
import com.matridx.igams.bioinformation.service.svcinterface.IBioWzglService;
import com.matridx.igams.bioinformation.util.JDBCUtils;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class BioWzglServiceImpl extends BaseBasicServiceImpl<BioWzglDto, BioWzglModel, IBioWzglDao> implements IBioWzglService {

	/**
	 * 根据wzid/中文名称/英文名称 查找物种信息
	 */
	@Override
	public BioWzglDto getWzxxByMc(BioWzglDto wzglDto) {
		return dao.getWzxxByMc(wzglDto) ;
	}

	/**
	 * 获取物种List
	 */
	public List<BioWzglDto> getWzList(BioWzglDto wzglDto){
		return dao.getWzList(wzglDto);
	}

	/**
	 * 更新物种信息表
	 */
	public void updateWzgl(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd");

		// 获取一个月前的日期
		calendar.add(Calendar.MONTH, -1);
		String endDate = sdf.format(calendar.getTime());
		JDBCUtils jdbcUtils=new JDBCUtils();
		List<BioWzglDto> list=jdbcUtils.queryData(endDate);
		List<BioWzglDto> list1= new ArrayList<>();
		if( list != null && list.size()>0 ){
			if(list.size()>100){
				for(int a=1;a<=list.size();a++){
					list1.add(list.get(a-1));
					if(a!=0&&a%100==0){
						dao.updateList(list1);
						list1.clear();
					}
				}
				if(list1.size()>0){
					dao.updateList(list1);
				}
			}else{
				dao.updateList(list);
			}
		}

//		SPECIAL_GENUS = {1763: 'mycobacteria', 670516: 'mycobacteria', 2126281: 'mycobacteria', 1866885: 'mycobacteria',
//				2093: 'mycoplasma', 2129: 'mycoplasma', 810: 'chlamydia', 873565: 'rickettsia', 33993: 'rickettsia',
//				1237: 'rickettsia', 780: 'rickettsia', 69474: 'rickettsia', 171: 'spirochete', 138: 'spirochete',
//				157: 'spirochete'}
		//特殊物种需特殊处理
		List<BioWzglDto> upList=new ArrayList<>();
		BioWzglDto dto =new BioWzglDto();
		dto.setWzfl("Mycobacteria");
		dto.setFid("1763");
		upList.add(dto);
		dto =new BioWzglDto();
		dto.setWzfl("Mycobacteria");
		dto.setFid("670516");
		upList.add(dto);
		dto =new BioWzglDto();
		dto.setWzfl("Mycobacteria");
		dto.setFid("2126281");
		upList.add(dto);
		dto =new BioWzglDto();
		dto.setFid("1866885");
		dto.setWzfl("Mycobacteria");
		upList.add(dto);
		dto =new BioWzglDto();
		dto.setFid("2093");
		dto.setWzfl("Mycoplasma");
		upList.add(dto);
		dto =new BioWzglDto();
		dto.setFid("2129");
		dto.setWzfl("Mycoplasma");
		upList.add(dto);
		dto =new BioWzglDto();
		dto.setFid("810");
		dto.setWzfl("Chlamydia");
		upList.add(dto);
		dto =new BioWzglDto();
		dto.setFid("873565");
		dto.setWzfl("Rickettsia");
		upList.add(dto);
		dto =new BioWzglDto();
		dto.setFid("33993");
		dto.setWzfl("Rickettsia");
		upList.add(dto);
		dto =new BioWzglDto();
		dto.setFid("1237");
		dto.setWzfl("Rickettsia");
		upList.add(dto);
		dto =new BioWzglDto();
		dto.setFid("780");
		dto.setWzfl("Rickettsia");
		upList.add(dto);
		dto =new BioWzglDto();
		dto.setFid("69474");
		dto.setWzfl("Rickettsia");
		upList.add(dto);
		dto =new BioWzglDto();
		dto.setFid("171");
		dto.setWzfl("Spirochete");
		upList.add(dto);
		dto =new BioWzglDto();
		dto.setFid("138");
		dto.setWzfl("Spirochete");
		upList.add(dto);
		dto =new BioWzglDto();
		dto.setFid("157");
		dto.setWzfl("Spirochete");
		upList.add(dto);
		dao.updateWzflByFid(upList);
	}
}
