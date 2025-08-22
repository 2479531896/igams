package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjqqjcDto;
import com.matridx.igams.wechat.dao.entities.SjqqjcModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.post.ISjqqjcDao;
import com.matridx.igams.wechat.service.svcinterface.ISjqqjcService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SjqqjcServiceImpl extends BaseBasicServiceImpl<SjqqjcDto, SjqqjcModel, ISjqqjcDao> implements ISjqqjcService{

	/**
	 * 根据送检信息新增检测项目
	 * @param sjxxDto
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertBySjxx(SjxxDto sjxxDto) {
		dao.deleteBySjxx(sjxxDto);
		// TODO Auto-generated method stub
		List<SjqqjcDto> sjqqjcDtos = sjxxDto.getSjqqjcs();
		if(sjqqjcDtos != null && sjqqjcDtos.size() > 0){
            for (int i = sjqqjcDtos.size()-1; i >=0; i--) {
            	if(StringUtil.isBlank(sjqqjcDtos.get(i).getJcz()))
            	{
            		sjqqjcDtos.remove(i);
            		continue;
            	}
            	sjqqjcDtos.get(i).setSjid(sjxxDto.getSjid());
			}
            if(sjqqjcDtos.size() > 0) {
				int result = dao.insertSjqqjcDtos(sjqqjcDtos);
                return result != 0;
            }else {
				return true;
			}
		}
		return true;
	}
	
	/**
	 * 根据送检id查看送检前期检测信息
	 * @param sjid
	 * @return
	 */
	@Override
	public List<SjqqjcDto> getJcz(String sjid){
		// TODO Auto-generated method stub
		return dao.getJcz(sjid);
	}
}
