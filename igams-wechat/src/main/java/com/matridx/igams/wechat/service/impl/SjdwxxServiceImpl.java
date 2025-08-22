package com.matridx.igams.wechat.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjdwxxDto;
import com.matridx.igams.wechat.dao.entities.SjdwxxModel;
import com.matridx.igams.wechat.dao.post.ISjdwxxDao;
import com.matridx.igams.wechat.service.svcinterface.ISjdwxxService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class SjdwxxServiceImpl extends BaseBasicServiceImpl<SjdwxxDto, SjdwxxModel, ISjdwxxDao>implements ISjdwxxService {
	
	/**
	* 获取全部医院名称
	* @return
	*/
	@Override
	public List<SjdwxxDto> getPagedDtoListSjdwxx(SjdwxxDto sjdwxxDto) {
			// TODO Auto-generated method stub
		return dao.getPagedDtoListSjdwxx(sjdwxxDto);
	}
	/**
	 * 获取所有科室
	 * @return
	 */
	public List<SjdwxxDto> getAllSjdwxx(){
		return dao.getAllSjdwxx();
	}
	/**
	* 组装树形菜单
	* @return
	*/
	@Override
	public String installTree(List<SjdwxxDto> sjdwxxList, String JSONDATA) {
			// TODO Auto-generated method stub、
		JSONDATA += "[";
		int predepth = 0;
		int depth = 0;
		for (int i = 0; i <= sjdwxxList.size(); i++) {
			if (i == sjdwxxList.size()) {
				for (int j = 0; j < predepth; j++) {
					JSONDATA += "]}";
				}
			} else {
				depth = Integer.parseInt(sjdwxxList.get(i).getDepth());
				if (i == 0) {
					JSONDATA += "{\"id\" : \"" + sjdwxxList.get(i).getDwid() + "\",\"text\" : \""+ sjdwxxList.get(i).getDwmc() + "\",\"children\" : [";
				} else {
					if (depth < predepth) {
						for (int j = 0; j <= predepth - depth; j++) {
							JSONDATA += "]}";
						}
						JSONDATA += ",{\"id\" : \"" + sjdwxxList.get(i).getDwid() + "\",\"text\" : \""+ sjdwxxList.get(i).getDwmc() + "\",\"children\" : [";
					} else if (depth > predepth) {
						JSONDATA += "{\"id\" : \"" + sjdwxxList.get(i).getDwid() + "\",\"text\" : \""+ sjdwxxList.get(i).getDwmc() + "\",\"children\" : [";
					} else if (depth == predepth) {
						JSONDATA += "]}";
						JSONDATA += ",{\"id\" : \"" + sjdwxxList.get(i).getDwid() + "\",\"text\" : \""+ sjdwxxList.get(i).getDwmc() + "\",\"children\" : [";
					}
				}
				predepth = Integer.parseInt(sjdwxxList.get(i).getDepth());
			}
		}
		JSONDATA += "]";
		return JSONDATA;
	}
	
	/**
	* 添加菜单
	* @return
	*/
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addWechatHis(SjdwxxDto sjdwxxDto) {
		boolean result=insertDto(sjdwxxDto);
		return result;
	}
		 
	/**
	* 执行添加操作
	* @return
	*/
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(SjdwxxDto sjdwxxDto){
		sjdwxxDto.setDwid(StringUtil.generateUUID());
		int result = dao.insert(sjdwxxDto);
		if(result == 0)
			return false;
		return true;
	}
		 
	/**
	* 修改单位信息
	* @return
	*/
	@Override
	public boolean modSavewHis(SjdwxxDto sjdwxxDto) {
		// TODO Auto-generated method stub
		int result=dao.update(sjdwxxDto);
		if(result==0)
			return false;
		return true;
	}	
	
	/**
	* 删除单位信息
	*
	* @return
	*/
	@Override
	public boolean delHisByDwid(SjdwxxDto sjdwxxDto) {
		// TODO Auto-generated method stub
		return dao.delHisByDwid(sjdwxxDto);
	}

	/**
	 * 根据dwid获取送检单位信息
	 * @param dws
	 * @return
	 */
	@Override
	public List<SjdwxxDto> getListByDwid(List<String> dws) {
		// TODO Auto-generated method stub
		return dao.getListByDwid(dws);
	}
}
