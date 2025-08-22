package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.XxdjDto;
import com.matridx.igams.wechat.dao.entities.XxdjModel;
import com.matridx.igams.wechat.dao.post.IXxdjDao;
import com.matridx.igams.wechat.service.svcinterface.IXxdjService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class XxdjServiceImpl extends BaseBasicServiceImpl<XxdjDto, XxdjModel, IXxdjDao> implements IXxdjService{

	/**
	 * 阿里服务器同步登记信息到本地服务器
	 * @param xxdjDto
	 * @return
	 */
	@Override
	public boolean checkXxdjDto(XxdjDto xxdjDto){
		// TODO Auto-generated method stub
		boolean result=false;
		if(StringUtil.isNotBlank(xxdjDto.getXxid())) {
			XxdjDto t_xxdjDto = getDto(xxdjDto);
			if(t_xxdjDto == null) {//执行新增操作
				result = insert(xxdjDto);
			}else {//执行修改操作
				result = update(xxdjDto);
			}
		}
		return result;
	}

	/**
	 * 根据搜索条件获取导出条数
	 * @param xxdjDto
	 * @param params
	 * @return
	 */
	public int getCountForSearchExp(XxdjDto xxdjDto,Map<String,Object> params){
		return dao.getCountForSearchExp(xxdjDto);
	}
	/**
	 * 根据搜索条件分页获取导出信息
	 * @param params
	 * @return
	 */
	public List<XxdjDto> getListForSearchExp(Map<String,Object> params){
		XxdjDto xxdjDto = (XxdjDto)params.get("entryData");
		queryJoinFlagExport(params,xxdjDto);
        return dao.getListForSearchExp(xxdjDto);
	}
	
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String,Object> params,XxdjDto xxdjDto){
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuffer sqlParam = new StringBuffer();
		for(DcszDto dcszDto:choseList){
			if(dcszDto == null || dcszDto.getDczd() == null)
				continue;
			sqlParam.append(",");
			if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		xxdjDto.setSqlParam(sqlParam.toString());
	}
	
}
