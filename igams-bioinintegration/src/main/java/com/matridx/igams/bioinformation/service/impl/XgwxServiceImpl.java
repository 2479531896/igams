package com.matridx.igams.bioinformation.service.impl;

import com.matridx.igams.bioinformation.dao.entities.XgwxDto;
import com.matridx.igams.bioinformation.dao.entities.XgwxModel;
import com.matridx.igams.bioinformation.dao.post.IXgwxDao;
import com.matridx.igams.bioinformation.service.svcinterface.IXgwxService;
import com.matridx.igams.bioinformation.util.JDBCUtils;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class XgwxServiceImpl extends BaseBasicServiceImpl<XgwxDto, XgwxModel, IXgwxDao> implements IXgwxService {
	@Autowired
	RedisUtil redisUtil;

	/**
	 * 根据taxids查询list
	 */
	public List<XgwxDto> getDtoListByIds(XgwxDto xgwxDto){
		return dao.getDtoListByIds(xgwxDto);
	}

	/**
	 * 查询list
	 */
	public List<XgwxDto> getXgwxList(XgwxDto xgwxDto){
		return dao.getXgwxList(xgwxDto);
	}

	/**
	 * 更新相关文献表
	 */
	public void updateXgwx() {
		JDBCUtils jdbcUtils = new JDBCUtils();
		List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
		List<XgwxDto> list = jdbcUtils.queryXgwxData(jcsjDtos);
		List<XgwxDto> newList= new ArrayList<>();
		if( list != null && list.size()>0 ){
			if(list.size()>100){
				for(int a=1;a<=list.size();a++){
					newList.add(list.get(a-1));
					if(a!=0&&a%100==0){
						dao.updateList(newList);
						dao.updateCkwxgl(newList);
						newList.clear();
					}
				}
				if(newList.size()>0){
					dao.updateList(newList);
					dao.updateCkwxgl(newList);
				}
			}else{
				dao.updateList(list);
				dao.updateCkwxgl(newList);
			}
		}
	}

}
