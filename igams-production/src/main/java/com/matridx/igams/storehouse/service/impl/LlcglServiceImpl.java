package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.dao.post.IQgglDao;
import com.matridx.igams.storehouse.dao.entities.LlcglDto;
import com.matridx.igams.storehouse.dao.entities.LlcglModel;
import com.matridx.igams.storehouse.dao.post.ILlcglDao;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.ILlcglService;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LlcglServiceImpl extends BaseBasicServiceImpl<LlcglDto, LlcglModel, ILlcglDao> implements ILlcglService{

	@Autowired
	IQgglDao qgglDao;

	@Autowired
	IHwxxService hwxxService;

	@Autowired
	ICkhwxxService ckhwxxService;
	
	/**
	 * 领料车列表
	 * @param llcglDto
	 * @return
	 */
	public List<LlcglDto> getLlcDtoList(LlcglDto llcglDto){
		
		//获取领料车数据
		 List<LlcglDto> llcglDtos = dao.getLlcDtoList(llcglDto);
		 //获取项目大类项目编码
		 StringBuilder ids = new StringBuilder();
		 if(!CollectionUtils.isEmpty(llcglDtos)) {
			for (LlcglDto llcglDto_t : llcglDtos) {
				ids.append(",").append(llcglDto_t.getWlid());
			}
		 }
		 if(StringUtil.isNotBlank(ids.toString())) {
			 ids = new StringBuilder(ids.substring(1));
			 QgglDto qgglDto = new QgglDto();
			 qgglDto.setIds(ids.toString());
			 List<QgglDto> qgglDtos = qgglDao.queryByWlid(qgglDto);
			 for (LlcglDto llcglDto_r : llcglDtos) {
				for (QgglDto qgglDto_r : qgglDtos) {
					if(llcglDto_r.getWlid().equals(qgglDto_r.getWlid())) {
						llcglDto_r.setXmdl(qgglDto_r.getXmdl());
						llcglDto_r.setXmbm(qgglDto_r.getXmbm());
						break;
					}
				}
			 }
		 }
		 return llcglDtos;
	}

	@Override
	public boolean insertLlxxList(List<QgmxDto> list) {
		return dao.insertLlxxList(list);
	}

	@Override
	public boolean deleteLlxxlist(List<QgmxDto> list) {
		return dao.deleteLlxxlist(list);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insetLlcglDtos(LlcglDto llcglDto) {
		List<LlcglDto> llcglDtoList = dao.getDtoList(llcglDto);
		List<String> ids = llcglDto.getIds();
		String newIds = "";
		if(llcglDtoList!=null && !llcglDtoList.isEmpty()){
			for (String id:ids){
				boolean flg = true;
				for (LlcglDto llcglDtoT:llcglDtoList){
					if(id.equals(llcglDtoT.getCkhwid())){
						flg = false;
						break;
					}
				}
				if(flg){
					newIds = newIds + "," + id;
				}
			}
		}
		if(StringUtil.isNotBlank(newIds)){
			llcglDto.setIds(newIds);
		}
		boolean result = dao.insetLlcglDtos(llcglDto);
		return result;
	}

	/**
	 * @Description: 批量删除
	 * @param llcglDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2024/7/8 17:15
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean delLlcglDtos(LlcglDto llcglDto) {
		return dao.delete(llcglDto)>0;
	}
}
