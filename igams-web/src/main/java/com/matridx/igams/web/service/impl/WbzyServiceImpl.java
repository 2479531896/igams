package com.matridx.igams.web.service.impl;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.web.dao.entities.WbzyDto;
import com.matridx.igams.web.dao.entities.WbzyModel;
import com.matridx.igams.web.dao.post.IWbzyDao;
import com.matridx.igams.web.service.svcinterface.IWbzyService;
import com.matridx.springboot.util.base.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WbzyServiceImpl extends BaseBasicServiceImpl<WbzyDto, WbzyModel, IWbzyDao> implements IWbzyService{

	@Autowired
	RedisUtil redisUtil;
	
	/**
	 * 获取小程序菜单权限列表 
	 * @param jsid
	 * @return
	 */
	public List<WbzyDto> getMiniMenuTreeList(String jsid) {
		return dao.getMiniMenuTreeList(jsid);
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
	
	@Override
	public String installMiniTree(List<WbzyDto> wbzyList) {
		StringBuilder JSONDATA = new StringBuilder("[");
		if(wbzyList==null || wbzyList.size()==0) {
			JSONDATA.append("]");
			return JSONDATA.toString();
		}
		for(int i=0;i<=wbzyList.size();i++) {
			if(i == 0) {
				JSONDATA.append("{\"id\" : \"").append(wbzyList.get(i).getZyid()).append("\",\"text\" : \"").append(wbzyList.get(i).getZybt()).append("\"");
			}else if(i == wbzyList.size()){
				String depth = wbzyList.get(i-1).getDepth();
				if(StringUtil.isNotBlank(depth)) {
					int cnt = Integer.parseInt(depth) - 1;
                    JSONDATA.append("]}".repeat(Math.max(0, cnt)));
				}
			}else {
				if(Integer.parseInt(wbzyList.get(i).getDepth())>Integer.parseInt(wbzyList.get(i-1).getDepth())) {
					if(StringUtils.isNotBlank(wbzyList.get(i).getZylj()) && !wbzyList.get(i).getZylj().contains("/pages/index/nextindex")) {
						JSONDATA.append(",\"children\" : [{\"id\" : \"").append(wbzyList.get(i).getZyid()).append("!@").append(wbzyList.get(i).getBtsx()).append("\",\"text\" : \"").append(wbzyList.get(i).getZybt()).append("\",\"icon\" : \"glyphicon glyphicon-file\"").append(wetherChecked(wbzyList.get(i).getJsid())).append("}");
					}else {
						JSONDATA.append(",\"children\" : [{\"id\" : \"").append(wbzyList.get(i).getZyid()).append("\",\"text\" : \"").append(wbzyList.get(i).getZybt()).append("\"");
					}
				}else if(Integer.parseInt(wbzyList.get(i).getDepth())==Integer.parseInt(wbzyList.get(i-1).getDepth())) {
					if(StringUtils.isNotBlank(wbzyList.get(i).getZylj())) {
						if(i<wbzyList.size()-1) {
							if(Integer.parseInt(wbzyList.get(i+1).getDepth())>Integer.parseInt(wbzyList.get(i).getDepth())) {
								JSONDATA.append(",{\"id\" : \"").append(wbzyList.get(i).getZyid()).append("!@").append(wbzyList.get(i).getBtsx()).append("\",\"text\" : \"").append(wbzyList.get(i).getZybt()).append("\"").append(wetherChecked(wbzyList.get(i).getJsid()));
							}else {
								JSONDATA.append(",{\"id\" : \"").append(wbzyList.get(i).getZyid()).append("!@").append(wbzyList.get(i).getBtsx()).append("\",\"text\" : \"").append(wbzyList.get(i).getZybt()).append("\",\"icon\" : \"glyphicon glyphicon-file\"").append(wetherChecked(wbzyList.get(i).getJsid())).append("}");
							}
						}else {
							JSONDATA.append(",{\"id\" : \"").append(wbzyList.get(i).getZyid()).append("!@").append(wbzyList.get(i).getBtsx()).append("\",\"text\" : \"").append(wbzyList.get(i).getZybt()).append("\",\"icon\" : \"glyphicon glyphicon-file\"").append(wetherChecked(wbzyList.get(i).getJsid())).append("}");
						}
					}else {
						JSONDATA.append(",{\"id\" : \"").append(wbzyList.get(i).getZyid()).append("\",\"text\" : \"").append(wbzyList.get(i).getZybt()).append("\"");
					}
				}else {
					int ccc=Integer.parseInt(wbzyList.get(i-1).getDepth())-Integer.parseInt(wbzyList.get(i).getDepth());//层次差
					StringBuilder jw= new StringBuilder();
                    jw.append("]}".repeat(Math.max(0, ccc)));
					if(StringUtils.isNotBlank(wbzyList.get(i).getZylj())) {
						if(i<wbzyList.size()-1) {
							if(Integer.parseInt(wbzyList.get(i+1).getDepth())>Integer.parseInt(wbzyList.get(i).getDepth())) {
								JSONDATA.append(jw).append(",{\"id\" : \"").append(wbzyList.get(i).getZyid()).append("!@").append(wbzyList.get(i).getBtsx()).append("\",\"text\" : \"").append(wbzyList.get(i).getZybt()).append("\"").append(wetherChecked(wbzyList.get(i).getJsid()));
							}else {
								JSONDATA.append(jw).append(",{\"id\" : \"").append(wbzyList.get(i).getZyid()).append("!@").append(wbzyList.get(i).getBtsx()).append("\",\"text\" : \"").append(wbzyList.get(i).getZybt()).append("\",\"icon\" : \"glyphicon glyphicon-file\"").append(wetherChecked(wbzyList.get(i).getJsid())).append("}");
							}
						}else {
							JSONDATA.append(jw).append(",{\"id\" : \"").append(wbzyList.get(i).getZyid()).append("!@").append(wbzyList.get(i).getBtsx()).append("\",\"text\" : \"").append(wbzyList.get(i).getZybt()).append("\",\"icon\" : \"glyphicon glyphicon-file\"").append(wetherChecked(wbzyList.get(i).getJsid())).append("}");
						}
					}else {
						JSONDATA.append(jw).append(",{\"id\" : \"").append(wbzyList.get(i).getZyid()).append("\",\"text\" : \"").append(wbzyList.get(i).getZybt()).append("\"");
					}
				}
			}
		}
		JSONDATA.append("]");
		return JSONDATA.toString();
	}
	
	/**
	 * 保存菜单数组数据
	 * @throws BusinessException 
	 */
	@SuppressWarnings("unchecked")
	public boolean saveMiniMenuArray(WbzyDto wbzyDto) throws BusinessException {
		List<Object> gnzyidList = JsonUtil.jsonArrToList(wbzyDto.getMenuArray());
		String jsid = wbzyDto.getJsid();
		dao.deleteWbzyqx(jsid);
		String[] mapKeys = new String[]{"jsid","zyid",null};
	    Map<String, Object> param = new HashMap<>();
	    param.put("list", genarateMapList(gnzyidList,GlobalString.SEPARATOR_SIGN1, jsid, mapKeys));
	    if(param.get("list")!=null && !CollectionUtils.isEmpty((List<WbzyDto>) param.get("list"))) {
	    	int result = dao.batchSaveWbzyqx(param);
	    	if (result != ((List<WbzyDto>) param.get("list")).size()) {
		    	throw new BusinessException("I99004");
	    	}
	    }
	    
	    redisUtil.hdel("Users_DingdingMenu",jsid);
		return true;
	}
	
}
