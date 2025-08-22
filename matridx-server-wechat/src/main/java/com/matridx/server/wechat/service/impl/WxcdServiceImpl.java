package com.matridx.server.wechat.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matridx.igams.common.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.server.wechat.dao.entities.WeChatButtonModel;
import com.matridx.server.wechat.dao.entities.WeChatCommonButtonModel;
import com.matridx.server.wechat.dao.entities.WeChatComplexButtonModel;
import com.matridx.server.wechat.dao.entities.WeChatMaterialModel;
import com.matridx.server.wechat.dao.entities.WeChatMenuModel;
import com.matridx.server.wechat.dao.entities.WeChatUserMenuMatchrule;
import com.matridx.server.wechat.dao.entities.WxcdDto;
import com.matridx.server.wechat.dao.entities.WxcdModel;
import com.matridx.server.wechat.dao.post.IWxcdDao;
import com.matridx.server.wechat.service.svcinterface.IWxcdService;
import com.matridx.server.wechat.util.WeChatUtils;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class WxcdServiceImpl extends BaseBasicServiceImpl<WxcdDto, WxcdModel, IWxcdDao> implements IWxcdService{

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	IXtszService xtszService;
	@Autowired
	RedisUtil redisUtil;
	
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
		int depth = 0;
		for(int i=0; i<=wxcdList.size(); i++){
			if(i == wxcdList.size()){
				for (int j = 0; j < predepth; j++) {
					JSONDATA += "]}";
				}
			}else{
				depth = Integer.parseInt(wxcdList.get(i).getDepth());
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
					}else if(depth == predepth){
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

	/**
	 * 推送微信菜单至微信
	 * @param wxcdDto
	 * @return
	 */
	@Override
	public boolean createMenu(WxcdDto wxcdDto) {
		// TODO Auto-generated method stub
		List<WxcdDto> wxcdTreeList = getWxcdTreeList(wxcdDto);
		if(StringUtil.isBlank(wxcdDto.getGxlx())){
			//默认
            return createDefaultMenu(wxcdTreeList, wxcdDto);
		}else{
			//个性
            return createMatchruleMenu(wxcdTreeList, wxcdDto);
		}
    }

	/**
	 * 推送微信默认菜单
	 * @param wxcdTreeList
	 * @param t_wxcdDto
	 * @return
	 */
	private boolean createDefaultMenu(List<WxcdDto> wxcdTreeList, WxcdDto t_wxcdDto) {
		// TODO Auto-generated method stub
		int predepth = 0;
		Map<Integer,List<Object>> menuMap = new HashMap<>();
		for(int i = wxcdTreeList.size()-1;i>=0;i--){
			WxcdDto wxcdDto = wxcdTreeList.get(i);
			int depth = Integer.parseInt(wxcdDto.getDepth());
			if(depth >= predepth){
				//做成最后一层形式
				WeChatCommonButtonModel sub = new WeChatCommonButtonModel();
				if("view".equals(wxcdDto.getCdlx())){
					sub.setType(wxcdDto.getCdlx());
					sub.setName(wxcdDto.getCdm());
					sub.setUrl(WeChatUtils.changeURLCode(wxcdDto.getLjdz(),redisUtil));
				}else if("miniprogram".equals(wxcdDto.getCdlx())){
					sub.setType(wxcdDto.getCdlx());
					sub.setName(wxcdDto.getCdm());
					sub.setUrl(WeChatUtils.changeURLCode(wxcdDto.getLjdz(),redisUtil));
					sub.setAppid(wxcdDto.getAppid());
					sub.setPagepath(wxcdDto.getYmlj());
				}else{
					sub.setType(wxcdDto.getCdlx());
					sub.setName(wxcdDto.getCdm());
					sub.setKey(wxcdDto.getCdj());
				}
				//判断是否有同级list
				if(menuMap.get(depth) != null){
					List<Object> list = menuMap.get(depth);
					list.add(0,sub);
					menuMap.put(depth,list);
				}else{
					List<Object> list = new ArrayList<>();
					list.add(sub);
					menuMap.put(depth,list);
				}
			}else if(depth < predepth){
				//做成带子菜单形式
				WeChatComplexButtonModel complex = new WeChatComplexButtonModel();
				complex.setName(wxcdDto.getCdm());
				List<Object> list = new ArrayList<>();
				list.add(complex);
				WeChatButtonModel[] arr = new WeChatButtonModel[menuMap.get(depth+1).size()];
				arr =menuMap.get(depth+1).toArray(new WeChatButtonModel[menuMap.get(depth+1).size()]);
				complex.setSub_button(arr);
				//判断是否有同级list
				if(menuMap.get(depth) != null){
					list.addAll(menuMap.get(depth));
					menuMap.put(depth,list);
				}else{
					menuMap.put(depth,list);
				}
				//清空depth+1级的数据
				menuMap.remove(depth+1);
			}
			predepth = depth;
		}
		int size = menuMap.get(1).size();
		WeChatButtonModel[] menu_buttons = new WeChatButtonModel[size];
		for (int i = 0; i < menuMap.get(1).size(); i++) {
			menu_buttons[i] = (WeChatButtonModel) menuMap.get(1).get(i);
		}
		WeChatMenuModel menu = new WeChatMenuModel();
		menu.setButton(menu_buttons);
        return WeChatUtils.createMenu(restTemplate, menu, t_wxcdDto.getWbcxdm(),redisUtil);
    }

	/**
	 * 推送微信个性菜单
	 * @param wxcdTreeList
	 * @param t_wxcdDto
	 * @return
	 */
	private boolean createMatchruleMenu(List<WxcdDto> wxcdTreeList, WxcdDto t_wxcdDto) {
		// TODO Auto-generated method stub
		int predepth = 0;
		Map<Integer,List<Object>> menuMap = new HashMap<>();
		for(int i = wxcdTreeList.size()-1;i>=0;i--){
			WxcdDto wxcdDto = wxcdTreeList.get(i);
			int depth = Integer.parseInt(wxcdDto.getDepth());
			if(depth >= predepth){
				//做成最后一层形式
				WeChatCommonButtonModel sub = new WeChatCommonButtonModel();
				if("view".equals(wxcdDto.getCdlx())){
					sub.setType(wxcdDto.getCdlx());
					sub.setName(wxcdDto.getCdm());
					sub.setUrl(WeChatUtils.changeURLCode(wxcdDto.getLjdz(),redisUtil));
				}else if("miniprogram".equals(wxcdDto.getCdlx())){
					sub.setType(wxcdDto.getCdlx());
					sub.setName(wxcdDto.getCdm());
					sub.setUrl(WeChatUtils.changeURLCode(wxcdDto.getLjdz(),redisUtil));
					sub.setAppid(wxcdDto.getAppid());
					sub.setPagepath(wxcdDto.getYmlj());
				}else{
					sub.setType(wxcdDto.getCdlx());
					sub.setName(wxcdDto.getCdm());
					sub.setKey(wxcdDto.getCdj());
				}
				//判断是否有同级list
				if(menuMap.get(depth) != null){
					List<Object> list = menuMap.get(depth);
					list.add(0,sub);
					menuMap.put(depth,list);
				}else{
					List<Object> list = new ArrayList<>();
					list.add(sub);
					menuMap.put(depth,list);
				}
			}else if(depth < predepth){
				//做成带子菜单形式
				WeChatComplexButtonModel complex = new WeChatComplexButtonModel();
				complex.setName(wxcdDto.getCdm());
				List<Object> list = new ArrayList<>();
				list.add(complex);
				WeChatButtonModel[] arr = new WeChatButtonModel[menuMap.get(depth+1).size()];
				arr =menuMap.get(depth+1).toArray(new WeChatButtonModel[menuMap.get(depth+1).size()]);
				complex.setSub_button(arr);
				//判断是否有同级list
				if(menuMap.get(depth) != null){
					list.addAll(menuMap.get(depth));
					menuMap.put(depth,list);
				}else{
					menuMap.put(depth,list);
				}
				//清空depth+1级的数据
				menuMap.remove(depth+1);
			}
			predepth = depth;
		}
		int size = menuMap.get(1).size();
		WeChatButtonModel[] menu_buttons = new WeChatButtonModel[size];
		for (int i = 0; i < menuMap.get(1).size(); i++) {
			menu_buttons[i] = (WeChatButtonModel) menuMap.get(1).get(i);
		}
		
		WeChatMenuModel menu = new WeChatMenuModel();
		menu.setButton(menu_buttons);
		WeChatUserMenuMatchrule menu_matchrule = new WeChatUserMenuMatchrule();
		menu_matchrule.setTag_id(Integer.valueOf(t_wxcdDto.getGxlx()));
		menu.setMatchrule(menu_matchrule);
        return WeChatUtils.createMatchruleMenu(restTemplate, menu, t_wxcdDto.getWbcxdm(),redisUtil);
    }

	/**
	 * 上传永久素材(图片)
	 * @param xtszDto
	 * @param wbcxdm
	 * @return
	 */
	@Override
	public boolean uploadSaveMaterial(XtszDto xtszDto, String wbcxdm) {
		// TODO Auto-generated method stub
		xtszDto.setMessage(xtszDto.getMessage().replaceAll("＜","<").replaceAll("＞", ">"));
		xtszDto.getLsbcdz();
		XtszDto s_xtszDto = new XtszDto();
		s_xtszDto.setSzlb(GlobalString.WECAHT_SEND_MESSAGE);
		s_xtszDto.setSzz(xtszDto.getBt());
		s_xtszDto.setBz(xtszDto.getMs());
		s_xtszDto.setKzcs1(xtszDto.getKzcs1());
		boolean result = xtszService.insertOrUpdateXtsz(s_xtszDto);
		if(!result){
			return result;
		}
		XtszDto r_xtszDto = new XtszDto();
		r_xtszDto.setSzlb(GlobalString.WECAHT_RESULT_MESSAGE);
		r_xtszDto.setSzz(xtszDto.getMessage());
		result = xtszService.insertOrUpdateXtsz(r_xtszDto);
		if(!result){
			return result;
		}
		WeChatMaterialModel uploadImageMaterial = WeChatUtils.uploadImageMaterial(xtszDto.getLsbcdz(), "image", wbcxdm,redisUtil);
		XtszDto m_xtszDto = new XtszDto();
		m_xtszDto.setSzlb(GlobalString.WECAHT_UPLOAD_MATERIAL);
		m_xtszDto.setSzz(uploadImageMaterial.getMedia_id());
		m_xtszDto.setKzcs1(uploadImageMaterial.getUrl());
		//客服消息，关注回复
		XtszDto t_xtszDto = xtszService.getDto(m_xtszDto);
		if(t_xtszDto != null){
			XtszDto d_xtszDto = new XtszDto();
			d_xtszDto.setSzlb(GlobalString.WECAHT_UPLOAD_MATERIAL_DEL);
			d_xtszDto = xtszService.getDto(d_xtszDto);
			XtszDto n_xtszDto = new XtszDto();
			n_xtszDto.setSzlb(GlobalString.WECAHT_UPLOAD_MATERIAL_DEL);
			if(d_xtszDto != null){
				n_xtszDto.setSzz(n_xtszDto.getSzz()+","+t_xtszDto.getSzz());
				n_xtszDto.setKzcs1(n_xtszDto.getKzcs1()+","+t_xtszDto.getKzcs1());
			}else{
				n_xtszDto.setSzz(t_xtszDto.getSzz());
				n_xtszDto.setKzcs1(t_xtszDto.getKzcs1());
			}
			result = xtszService.insertOrUpdateXtsz(n_xtszDto);
			if(!result){
				return result;
			}
		}
		return xtszService.insertOrUpdateXtsz(m_xtszDto);
	}
}
