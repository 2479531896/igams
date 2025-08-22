package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.WxcdDto;
import com.matridx.igams.wechat.dao.entities.WxcdModel;
import com.matridx.igams.wechat.dao.post.IWxcdDao;
import com.matridx.igams.wechat.service.svcinterface.IWxcdService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WxcdServiceImpl extends BaseBasicServiceImpl<WxcdDto, WxcdModel, IWxcdDao> implements IWxcdService{

	/**
	 * 查询微信菜单信息
	 * @return
	 */
	@Override
	public List<WxcdDto> getWxcdTreeList(WxcdDto wxcdDto) {
		// TODO Auto-generated method stub
		return dao.getWxcdTreeList(wxcdDto);
	}

	/**
	 * 组装微信菜单列表树
	 * @param wxcdList
	 * @param JSONDATA
	 * @return
	 */
	@Override
	public String installTree(List<WxcdDto> wxcdList, String JSONDATA) {
		// TODO Auto-generated method stub
		JSONDATA += "[";
		int predepth = 0;
		for(int i=0; i<=wxcdList.size(); i++){
			if(i == wxcdList.size()){
				for (int j = 0; j < predepth; j++) {
					JSONDATA += "]}";
				}
			}else{
				int depth = Integer.parseInt(wxcdList.get(i).getDepth());
				if(i == 0){
					JSONDATA += "{\"id\" : \""+wxcdList.get(i).getCdid()+"\",\"text\" : \""+wxcdList.get(i).getCdm()+"\",\"children\" : [";
				}else {
					if(depth < predepth){
						for (int j = 0; j <= predepth - depth; j++) {
							JSONDATA += "]}";
						}
						JSONDATA += ",{\"id\" : \""+wxcdList.get(i).getCdid()+"\",\"text\" : \""+wxcdList.get(i).getCdm()+"\",\"children\" : [";
					}else if(depth > predepth){
						JSONDATA += "{\"id\" : \""+wxcdList.get(i).getCdid()+"\",\"text\" : \""+wxcdList.get(i).getCdm()+"\",\"children\" : [";
					}else {
						JSONDATA += "]}";
						JSONDATA += ",{\"id\" : \""+wxcdList.get(i).getCdid()+"\",\"text\" : \""+wxcdList.get(i).getCdm()+"\",\"children\" : [";
					}
				}
				predepth = Integer.parseInt(wxcdList.get(i).getDepth());
			}
		}
		JSONDATA += "]";
		return JSONDATA;
	}

	/**
	 * 修改菜单信息
	 * @param wxcdDto
	 * @return
	 */
	@Override
	public boolean modSaveWechatMenu(WxcdDto wxcdDto) {
		// TODO Auto-generated method stub
		int result = dao.update(wxcdDto);
        return result != 0;
    }
	
	/**
	 * 新增菜单信息
	 * @param wxcdDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addSaveWechatMenu(WxcdDto wxcdDto) {
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(wxcdDto.getFcdid())){
			//新增同级
			if(StringUtil.isBlank(wxcdDto.getCdid())){
                return insertDto(wxcdDto);
			}else{
				WxcdDto t_wxcdDto = dao.getDtoById(wxcdDto.getCdid());
				wxcdDto.setFcdid(t_wxcdDto.getFcdid());
                return insertDto(wxcdDto);
			}
		}else{
			//新增子菜单
            return insertDto(wxcdDto);
		}
    }

	/** 
	 * 插入菜单信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(WxcdDto wxcdDto){
		wxcdDto.setCdid(StringUtil.generateUUID());
		int result = dao.insert(wxcdDto);
        return result != 0;
    }

	/**
	 * 删除菜单及子菜单
	 * @param wxcdDto
	 * @return
	 */
	@Override
	public boolean deleteByCdid(WxcdDto wxcdDto) {
		// TODO Auto-generated method stub
		return dao.deleteByCdid(wxcdDto);
	}
}
