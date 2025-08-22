package com.matridx.web.service.impl;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.springboot.util.base.JsonUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.web.dao.entities.XtzyDto;
import com.matridx.web.dao.entities.XtzyModel;
import com.matridx.web.dao.post.IXtzyDao;
import com.matridx.web.service.svcinterface.IXtzyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class XtzyServiceImpl extends BaseBasicServiceImpl<XtzyDto, XtzyModel, IXtzyDao> implements IXtzyService{
	/**
	 * 根据菜单ID获取子菜单信息
	 * @param xtzyDto
	 * @return
	 */
	public List<XtzyDto> getSubMenuByMenuId(XtzyDto xtzyDto){
		if(StringUtil.isBlank(xtzyDto.getZyid())) {
            return null;
        }
		List<XtzyDto> zyList = dao.getDtoListByMenuId(xtzyDto);
		
		for(int i= zyList.size() -1;i>=0;i--){
			XtzyDto t_xtzyDto = zyList.get(i);
			if("3".equals(t_xtzyDto.getDepth()) && StringUtil.isBlank(t_xtzyDto.getCzdm())){
				zyList.remove(i);
			}else if("2".equals(t_xtzyDto.getDepth())){
				//如果最后一个是第二层的，则直接删除
				if(i == zyList.size() -1)
					zyList.remove(i);
				//如果前一个资源为 第二层，则直接删除
				else if("2".equals(zyList.get(i + 1).getDepth()))
					zyList.remove(i);
			}
		}
		
		return zyList;
	}
	
	/**
	 * 获取菜单权限列表
	 * @param jsid
	 * @return
	 */
	public List<XtzyDto> getMenuTreeList(String jsid){
		return dao.getMenuTreeList(jsid);
	}
	
	@Override
	public String installTree(List<XtzyDto> xtzyList, String JSONDATA) {
		JSONDATA += "[";
		for(int i=0; i<=xtzyList.size(); i++){
			if(i == 0){
				JSONDATA += "{\"id\" : \""+xtzyList.get(i).getYjzyid()+"\",\"text\" : \""+xtzyList.get(i).getYjzybt()+"\",\"children\" : [{ \"id\" : \""+xtzyList.get(i).getEjzyid()+"\",\"text\" : \""+xtzyList.get(i).getEjzybt()+"\", \"children\" : [ {\"id\" : \""+xtzyList.get(i).getSjzyid()+"\",\"text\" : \""+xtzyList.get(i).getSjzybt()+"\", \"children\" : [ {\"id\" : \""+xtzyList.get(i).getSjzyid()+"!@"+xtzyList.get(i).getCzdm()+"\",\"text\" : \""+xtzyList.get(i).getCzmc()+"\",\"icon\" : \"glyphicon glyphicon-file\""+wetherChecked(xtzyList.get(i).getJsid())+"}";
			}else if(i == xtzyList.size()){
				JSONDATA += "]}]}]}";
			}else{
				if(!xtzyList.get(i).getYjzyid().equals(xtzyList.get(i-1).getYjzyid()) ){
					JSONDATA += "]}]}]}";
					JSONDATA += ",{\"id\" : \""+xtzyList.get(i).getYjzyid()+"\",\"text\" : \""+xtzyList.get(i).getYjzybt()+"\",\"children\" : [{ \"id\" : \""+xtzyList.get(i).getEjzyid()+"\",\"text\" : \""+xtzyList.get(i).getEjzybt()+"\", \"children\" : [ {\"id\" : \""+xtzyList.get(i).getSjzyid()+"\",\"text\" : \""+xtzyList.get(i).getSjzybt()+"\", \"children\" : [ {\"id\" : \""+xtzyList.get(i).getSjzyid()+"!@"+xtzyList.get(i).getCzdm()+"\",\"text\" : \""+xtzyList.get(i).getCzmc()+"\",\"icon\" : \"glyphicon glyphicon-file\""+wetherChecked(xtzyList.get(i).getJsid())+"}";
				}else{
					if(!xtzyList.get(i).getEjzyid().equals(xtzyList.get(i-1).getEjzyid())){
						JSONDATA += "]}]}";
						JSONDATA += ",{\"id\" : \""+xtzyList.get(i).getEjzyid()+"\",\"text\" : \""+xtzyList.get(i).getEjzybt()+"\", \"children\" : [ {\"id\" : \""+xtzyList.get(i).getSjzyid()+"\",\"text\" : \""+xtzyList.get(i).getSjzybt()+"\", \"children\" : [ {\"id\" : \""+xtzyList.get(i).getSjzyid()+"!@"+xtzyList.get(i).getCzdm()+"\",\"text\" : \""+xtzyList.get(i).getCzmc()+"\",\"icon\" : \"glyphicon glyphicon-file\""+wetherChecked(xtzyList.get(i).getJsid())+"}";
					}else{
						if(!xtzyList.get(i).getSjzyid().equals(xtzyList.get(i-1).getSjzyid())){
							JSONDATA += "]}";
							JSONDATA += ",{\"id\" : \""+xtzyList.get(i).getSjzyid()+"\",\"text\" : \""+xtzyList.get(i).getSjzybt()+"\", \"children\" : [ {\"id\" : \""+xtzyList.get(i).getSjzyid()+"!@"+xtzyList.get(i).getCzdm()+"\",\"text\" : \""+xtzyList.get(i).getCzmc()+"\",\"icon\" : \"glyphicon glyphicon-file\""+wetherChecked(xtzyList.get(i).getJsid())+"}";
						}else{
							JSONDATA += ",{\"id\" : \""+xtzyList.get(i).getSjzyid()+"!@"+xtzyList.get(i).getCzdm()+"\",\"text\" : \""+xtzyList.get(i).getCzmc()+"\",\"icon\" : \"glyphicon glyphicon-file\""+wetherChecked(xtzyList.get(i).getJsid())+"}";
						}
					}
				}
			}
		}
		JSONDATA += "]";
		return JSONDATA;
	}
	
	/**
	 * 根据是否存在jsid判断是否添加打钩样式
	 *@描述：
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param jsid
	 *@return
	 */
	public String wetherChecked(String jsid){
		String str = "";
		if(jsid != null && !"".equals(jsid)){
			str = ", \"state\" : { \"selected\" : true }";
		}
		return str;
	}
	
	/**
	 * 保存菜单数组数据
	 * @throws BusinessException 
	 */
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean saveMenuArray(XtzyDto xtzyDto) throws BusinessException {
		List<Object> czdmidList = JsonUtil.jsonArrToList(xtzyDto.getMenuArray());
		String jsid = xtzyDto.getJsid();
		if (czdmidList!=null && czdmidList.size() != 0) {
			dao.deleteXtzyqx(jsid);
			String[] mapKeys = new String[]{"jsid","zyid","czdm"};
			List<Map<String, String>> maps = genarateMapList(czdmidList, GlobalString.SEPARATOR_SIGN1, jsid, mapKeys);
			int result_qxs =0;
			if(maps.size()>100) {
				int length = (int)Math.ceil((double)maps.size() / 100);//向上取整
				for (int i = 0; i < length; i++) {
					List<Map<String, String>> list = new ArrayList<Map<String, String>>();
					int t_length = 0;
					if (i != length - 1) {
						t_length = (i + 1) * 100;
					} else {
						t_length = maps.size();
					}
					for (int j = i * 100; j < t_length; j++) {
						list.add(maps.get(j));
					}
					result_qxs = dao.insertXtzyqxList(list);
					if (result_qxs < 1)
						throw new BusinessException("I99004");
				}
			}else{
				result_qxs = dao.insertXtzyqxList(maps);
				if (result_qxs < 1)
					throw new BusinessException("I99004");
			}
		}
		return true;
	}
}
