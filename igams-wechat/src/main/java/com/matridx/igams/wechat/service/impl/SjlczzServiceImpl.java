package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjlczzDto;
import com.matridx.igams.wechat.dao.entities.SjlczzModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.post.ISjlczzDao;
import com.matridx.igams.wechat.service.svcinterface.ISjlczzService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SjlczzServiceImpl extends BaseBasicServiceImpl<SjlczzDto, SjlczzModel, ISjlczzDao> implements ISjlczzService{

	/**
	 * 根据送检信息新增临床症状
	 * @param sjxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertBySjxx(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		dao.deleteBySjxx(sjxxDto);
		List<SjlczzDto> sjlczzDtos = sjxxDto.getSjlczzs();
		if(sjlczzDtos != null && sjlczzDtos.size() > 0){
			for (int i = 0; i < sjlczzDtos.size(); i++) {
				sjlczzDtos.get(i).setSjid(sjxxDto.getSjid());
				sjlczzDtos.get(i).setXh(String.valueOf(i+1));
			}
			int result = dao.insertSjlczzDtos(sjlczzDtos);
            return result != 0;
		}
		return true;
	}
	
	/**
	 * 根据送检信息新增临床症状
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean insertSjlczz(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		dao.deleteBySjxx(sjxxDto);
		List<String> strSjlczz =sjxxDto.getLczzs();
		if(strSjlczz!=null && strSjlczz.size()>0) {
			List<SjlczzDto> sjlczzDtos= new ArrayList<>();
			for (int i = 0; i < strSjlczz.size(); i++){
				SjlczzDto sjlczzDto=new SjlczzDto();
				sjlczzDto.setSjid(sjxxDto.getSjid());
				sjlczzDto.setXh(String.valueOf(i+1));
				sjlczzDto.setZz(strSjlczz.get(i));
				sjlczzDtos.add(sjlczzDto);
			}
			int result=dao.insertSjlczzDtos(sjlczzDtos);
            return result != 0;
		}
		
		return true;
	}

	@Override
	public List<String> getLczz(String sjid){
		// TODO Auto-generated method stub
		return dao.getLczz(sjid);
	}

}
