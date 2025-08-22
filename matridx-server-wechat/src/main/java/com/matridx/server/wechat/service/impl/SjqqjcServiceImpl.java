package com.matridx.server.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.SjqqjcDto;
import com.matridx.server.wechat.dao.entities.SjqqjcModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.post.ISjqqjcDao;
import com.matridx.server.wechat.service.svcinterface.ISjqqjcService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
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
		// TODO Auto-generated method stub
		List<SjqqjcDto> sjqqjcDtos = sjxxDto.getSjqqjcs();
		if(sjqqjcDtos != null && sjqqjcDtos.size() > 0){
			dao.deleteBySjxx(sjxxDto);
            for (int i = sjqqjcDtos.size()-1; i >=0; i--) {
            	if(StringUtil.isBlank(sjqqjcDtos.get(i).getJcz()))
            	{
            		sjqqjcDtos.remove(i);
            		continue;
            	}
            	sjqqjcDtos.get(i).setSjid(sjxxDto.getSjid());
			}
			if (!CollectionUtils.isEmpty(sjqqjcDtos)){
				int result = dao.insertSjqqjcDtos(sjqqjcDtos);
				return result != 0;
			}
		}
		return true;
	}
	
	/**
	 * 根据页面的输入列表登录数据库
	 * @param sjgzbyDtos
	 * @return
	 */
	/*
	 * public int updateBySjxx(SjxxDto sjxxDto) { //先删除再新增
	 * dao.deleteBySjxx(sjxxDto); List<SjqqjcDto> sjqqjcDtos = sjxxDto.getSjqqjcs();
	 * if(sjqqjcDtos != null && sjqqjcDtos.size() > 0){ for (int i = 0; i <
	 * sjqqjcDtos.size(); i++) { sjqqjcDtos.get(i).setSjid(sjxxDto.getSjid()); } int
	 * result = dao.insertSjqqjcDtos(sjqqjcDtos); return result; } return 0; }
	 */
	
	/**
	 * 获取前期检测的清单
	 * @param sjqqjcDto
	 * @return
	 */
	public List<SjqqjcDto> getDtoList(SjqqjcDto sjqqjcDto){
		return dao.getDtoList(sjqqjcDto);
	}
	
	/**
	 * 获取前期检测的清单
	 * @param sjqqjcDto
	 * @return
	 */
	public List<SjqqjcDto> getDtoListByJcsj(SjqqjcDto sjqqjcDto){
		return dao.getDtoListByJcsj(sjqqjcDto);
	}

	/**
	 * 根据送检信息新增前期检测
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean insertBySjxxDto(SjxxDto sjxxDto) {
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
				if(result == 0)
					return false;
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
