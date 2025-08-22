package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjgzbyDto;
import com.matridx.igams.wechat.dao.entities.SjgzbyModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.post.ISjgzbyDao;
import com.matridx.igams.wechat.service.svcinterface.ISjgzbyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SjgzbyServiceImpl extends BaseBasicServiceImpl<SjgzbyDto, SjgzbyModel, ISjgzbyDao> implements ISjgzbyService{
	/**
	 * 根据送检信息新增关注病原
	 * @param sjxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int insertBySjxx(SjxxDto sjxxDto) {
		//先删除再新增
		dao.deleteBySjxx(sjxxDto);
		// TODO Auto-generated method stub
		List<SjgzbyDto> sjgzbyDtos = sjxxDto.getSjgzbys();
		if(sjgzbyDtos != null && sjgzbyDtos.size() > 0){
            for (int i = 0; i < sjgzbyDtos.size(); i++) {
            	sjgzbyDtos.get(i).setSjid(sjxxDto.getSjid());
			}
            return dao.insertSjgzbyDtos(sjgzbyDtos);
		}
		return 0;
	}
	@Override
	public int insertSjgzby(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		dao.deleteBySjxx(sjxxDto);
		List<String> strGzby=sjxxDto.getBys();
		if (strGzby!=null &&strGzby.size()>0){
			List<SjgzbyDto> sjgzbyDtos = new ArrayList<>();
			for (int i = 0; i < strGzby.size(); i++){
				SjgzbyDto sjgzbyDto=new SjgzbyDto();
				sjgzbyDto.setSjid(sjxxDto.getSjid());
				sjgzbyDto.setBy(strGzby.get(i));
				sjgzbyDtos.add(sjgzbyDto);
			}
			dao.insertSjgzbyDtos(sjgzbyDtos);
		}
		
	return 0;
	}
	@Override
	public List<String> getGzby(String sjid)
	{
		// TODO Auto-generated method stub
		return dao.getGzby(sjid);
	}

}
