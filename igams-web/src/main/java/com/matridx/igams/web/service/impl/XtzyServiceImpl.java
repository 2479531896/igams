package com.matridx.igams.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Lists;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.web.dao.entities.XtzyDto;
import com.matridx.igams.web.dao.entities.XtzyModel;
import com.matridx.igams.web.dao.post.IXtzyDao;
import com.matridx.igams.web.service.svcinterface.IXtzyService;
import com.matridx.springboot.util.base.JsonUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class XtzyServiceImpl extends BaseBasicServiceImpl<XtzyDto, XtzyModel, IXtzyDao> implements IXtzyService{
	
	@Autowired
	RedisUtil redisUtil;
	
	/**
	 * 根据菜单ID获取子菜单信息
	 * @param xtzyDto
	 * @return
	 */
	public List<XtzyDto> getSubMenuByMenuId(XtzyDto xtzyDto){
		if(StringUtil.isBlank(xtzyDto.getZyid()))
			return null;
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
	 * @param xtzyDto
	 * @return
	 */
	public List<XtzyDto> getMenuTreeList(XtzyDto xtzyDto){
		return dao.getMenuTreeList(xtzyDto);
	}
	
	@Override
	public String installTree(List<XtzyDto> xtzyList, String JSONDATA) {
		JSONDATA += "[";
		if (!CollectionUtils.isEmpty(xtzyList)) {
            StringBuilder JSONDATABuilder = new StringBuilder(JSONDATA);
            for (int i = 0; i <= xtzyList.size(); i++) {
				if (i == 0) {
					JSONDATABuilder.append("{\"id\" : \"").append(xtzyList.get(i).getYjzyid()).append("\",\"text\" : \"").append(xtzyList.get(i).getYjzybt()).append("\",\"children\" : [{ \"id\" : \"").append(xtzyList.get(i).getEjzyid()).append("\",\"text\" : \"").append(xtzyList.get(i).getEjzybt()).append("\", \"children\" : [ {\"id\" : \"").append(xtzyList.get(i).getSjzyid()).append("\",\"text\" : \"").append(xtzyList.get(i).getSjzybt()).append("\", \"children\" : [ {\"id\" : \"").append(xtzyList.get(i).getSjzyid()).append("!@").append(xtzyList.get(i).getCzdm()).append("\",\"text\" : \"").append(xtzyList.get(i).getCzmc()).append("\",\"icon\" : \"glyphicon glyphicon-file\"").append(wetherChecked(xtzyList.get(i).getJsid())).append("}");
				} else if (i == xtzyList.size()) {
					JSONDATABuilder.append("]}]}]}");
				} else {
					if (!xtzyList.get(i).getYjzyid().equals(xtzyList.get(i - 1).getYjzyid())) {
						JSONDATABuilder.append("]}]}]}");
						JSONDATABuilder.append(",{\"id\" : \"").append(xtzyList.get(i).getYjzyid()).append("\",\"text\" : \"").append(xtzyList.get(i).getYjzybt()).append("\",\"children\" : [{ \"id\" : \"").append(xtzyList.get(i).getEjzyid()).append("\",\"text\" : \"").append(xtzyList.get(i).getEjzybt()).append("\", \"children\" : [ {\"id\" : \"").append(xtzyList.get(i).getSjzyid()).append("\",\"text\" : \"").append(xtzyList.get(i).getSjzybt()).append("\", \"children\" : [ {\"id\" : \"").append(xtzyList.get(i).getSjzyid()).append("!@").append(xtzyList.get(i).getCzdm()).append("\",\"text\" : \"").append(xtzyList.get(i).getCzmc()).append("\",\"icon\" : \"glyphicon glyphicon-file\"").append(wetherChecked(xtzyList.get(i).getJsid())).append("}");
					} else {
						if (!xtzyList.get(i).getEjzyid().equals(xtzyList.get(i - 1).getEjzyid())) {
							JSONDATABuilder.append("]}]}");
							JSONDATABuilder.append(",{\"id\" : \"").append(xtzyList.get(i).getEjzyid()).append("\",\"text\" : \"").append(xtzyList.get(i).getEjzybt()).append("\", \"children\" : [ {\"id\" : \"").append(xtzyList.get(i).getSjzyid()).append("\",\"text\" : \"").append(xtzyList.get(i).getSjzybt()).append("\", \"children\" : [ {\"id\" : \"").append(xtzyList.get(i).getSjzyid()).append("!@").append(xtzyList.get(i).getCzdm()).append("\",\"text\" : \"").append(xtzyList.get(i).getCzmc()).append("\",\"icon\" : \"glyphicon glyphicon-file\"").append(wetherChecked(xtzyList.get(i).getJsid())).append("}");
						} else {
							if (!xtzyList.get(i).getSjzyid().equals(xtzyList.get(i - 1).getSjzyid())) {
								JSONDATABuilder.append("]}");
								JSONDATABuilder.append(",{\"id\" : \"").append(xtzyList.get(i).getSjzyid()).append("\",\"text\" : \"").append(xtzyList.get(i).getSjzybt()).append("\", \"children\" : [ {\"id\" : \"").append(xtzyList.get(i).getSjzyid()).append("!@").append(xtzyList.get(i).getCzdm()).append("\",\"text\" : \"").append(xtzyList.get(i).getCzmc()).append("\",\"icon\" : \"glyphicon glyphicon-file\"").append(wetherChecked(xtzyList.get(i).getJsid())).append("}");
							} else {
								JSONDATABuilder.append(",{\"id\" : \"").append(xtzyList.get(i).getSjzyid()).append("!@").append(xtzyList.get(i).getCzdm()).append("\",\"text\" : \"").append(xtzyList.get(i).getCzmc()).append("\",\"icon\" : \"glyphicon glyphicon-file\"").append(wetherChecked(xtzyList.get(i).getJsid())).append("}");
							}
						}
					}
				}
			}
            JSONDATA = JSONDATABuilder.toString();
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
		if(StringUtil.isNotBlank(jsid)){
			str = ", \"state\" : { \"selected\" : true }";
		}
		return str;
	}
	
	/**
	 * 保存菜单数组数据
	 * @throws BusinessException 
	 */
	public boolean saveMenuArray(XtzyDto xtzyDto) throws BusinessException {
		List<Object> czdmidList = JsonUtil.jsonArrToList(xtzyDto.getMenuArray());
		//根据jsid和模块zyid获取当前模块权限
		List<XtzyDto> currentlist = dao.queryPermissions(xtzyDto);
		String jsid = xtzyDto.getJsid();
		if (!CollectionUtils.isEmpty(czdmidList)) {
			String[] mapKeys = new String[]{"jsid","zyid","czdm"};
			List<Map<String, String>> list = genarateMapList(czdmidList, GlobalString.SEPARATOR_SIGN1, jsid, mapKeys);
			//list转换为Object类型
			List<XtzyDto> xtzyList = JSON.parseObject(JSON.toJSONString(list), new TypeReference<>() {
            });
			
			//获取差集，删除
			if(!CollectionUtils.isEmpty(currentlist)) {
				List<XtzyDto> currentlist_del = currentlist.stream().filter(
		        		a -> !xtzyList.stream().map(
		                b -> b.getZyid() + "&" + b.getCzdm()).collect(Collectors.toList())
		                .contains(a.getZyid() + "&" + a.getCzdm())).collect(Collectors.toList());
		        if(!CollectionUtils.isEmpty(currentlist_del)) {
		        	dao.deleteByList(currentlist_del);
		        }				
			}
			
			//获取差集，新增
			List<XtzyDto> xtzyList_ins;
			if(!CollectionUtils.isEmpty(currentlist)) {
						xtzyList_ins = xtzyList.stream().filter(
		        		a -> !currentlist.stream().map(
		                b -> b.getZyid() + "&" + b.getCzdm()).collect(Collectors.toList())
		                .contains(a.getZyid() + "&" + a.getCzdm())).collect(Collectors.toList());
			}else {
				xtzyList_ins = xtzyList;
			}
			
			int result = 0;
			//将list分成100个一组进行新增操作
			List<List<XtzyDto>> partitionList = Lists.partition(xtzyList_ins, 100);
			for (List<XtzyDto> zyList : partitionList) {
				Map<String, Object> param = new HashMap<>();
				param.put("list", zyList);
				result += dao.batchSaveXtzyqx(param);
			}
//			param.put("list", genarateMapList(czdmidList, GlobalString.SEPARATOR_SIGN1, jsid, mapKeys));
//			int result = dao.batchSaveXtzyqx(param);
		    if (result != xtzyList_ins.size()) {
		    	throw new BusinessException("I99004");
		    }
		}else {
			//如果为空，直接全部删除
			if(!CollectionUtils.isEmpty(currentlist)) {
				dao.deleteByList(currentlist);
			}		
		}
		//因为菜单有修改，所以需要删除指定角色下的菜单权限和操作按钮权限
		redisUtil.hdel("Users_QxModel",jsid);
		redisUtil.hdel("Users_NobtnlistModels",jsid);
		return true;
	}
}
