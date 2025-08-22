package com.matridx.igams.dmp.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.enums.DatabaseTypeEnum;
import com.matridx.igams.common.enums.RequestMethodEnum;
import com.matridx.igams.common.enums.TransferTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.dmp.dao.entities.RzqxDto;
import com.matridx.igams.dmp.dao.entities.ZyxxDto;
import com.matridx.igams.dmp.dao.entities.ZyxxModel;
import com.matridx.igams.dmp.dao.post.IZyxxDao;
import com.matridx.igams.dmp.service.svcinterface.ILjfxxService;
import com.matridx.igams.dmp.service.svcinterface.ISjkxxService;
import com.matridx.igams.dmp.service.svcinterface.IZyxxService;
import com.matridx.springboot.util.base.JsonUtil;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class ZyxxServiceImpl extends BaseBasicServiceImpl<ZyxxDto, ZyxxModel, IZyxxDao> implements IZyxxService{

	@Autowired
	ISjkxxService sjkxxService;
	@Autowired
	ILjfxxService ljfxxSerivce;
	
	/**
	 * 获取权限资源列表
	 * @param rzid
	 * @return
	 */
	@Override
	public List<ZyxxDto> getZyxxTreeList(String rzid) {
		// TODO Auto-generated method stub
		return dao.getZyxxTreeList(rzid);
	}

	/**
	 * 组装用户权限资源列表树
	 * @param zyxxList
	 * @param jSONDATA
	 * @return
	 */
	@Override
	public String installTree(List<ZyxxDto> zyxxList, String JSONDATA) {
		JSONDATA += "[";
		for(int i=0; i<=zyxxList.size(); i++){
			if(i == 0){
				JSONDATA += "{\"id\" : \""+zyxxList.get(i).getTgfid()+"\",\"text\" : \""+zyxxList.get(i).getTgfmc()+"\",\"children\" : [{ \"id\" : \""+zyxxList.get(i).getZyid()+"\",\"text\" : \""+zyxxList.get(i).getZymc()+"\",\"icon\" : \"glyphicon glyphicon-file\""+wetherChecked(zyxxList.get(i).getRzid())+"}";
			}else if(i == zyxxList.size()){
				JSONDATA += "]}";
			}else{
				if(!zyxxList.get(i).getTgfid().equals(zyxxList.get(i-1).getTgfid())){
					JSONDATA += "]}";
					JSONDATA += ",{\"id\" : \""+zyxxList.get(i).getTgfid()+"\",\"text\" : \""+zyxxList.get(i).getTgfmc()+"\", \"children\" : [ {\"id\" : \""+zyxxList.get(i).getZyid()+"\",\"text\" : \""+zyxxList.get(i).getZymc()+"\",\"icon\" : \"glyphicon glyphicon-file\""+wetherChecked(zyxxList.get(i).getRzid())+"}";
				}else{
					JSONDATA += ",{\"id\" : \""+zyxxList.get(i).getZyid()+"\",\"text\" : \""+zyxxList.get(i).getZymc()+"\",\"icon\" : \"glyphicon glyphicon-file\""+wetherChecked(zyxxList.get(i).getRzid())+"}";
				}
			}
		}
		JSONDATA += "]";
		return JSONDATA;
	}
	
	/**
	 * 根据是否存在rzid判断是否添加打钩样式
	 *@描述：
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param rzid
	 *@return
	 */
	public String wetherChecked(String rzid){
		String str = "";
		if(rzid != null && !"".equals(rzid)){
			str = ", \"state\" : { \"selected\" : true }";
		}
		return str;
	}

	/**
	 * 保存用户权限信息
	 * @param zyxxDto
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean saveCompetenceArray(ZyxxDto zyxxDto) throws BusinessException {
		// TODO Auto-generated method stub
		List<Object> zyidList = JsonUtil.jsonArrToList(zyxxDto.getCompetenceArray());
		String rzid = zyxxDto.getRzid();
		if (zyidList!=null && zyidList.size() != 0) {
			dao.updateRzqx(zyxxDto);
			List<RzqxDto> rzqxDtos = new ArrayList<RzqxDto>();
			for (int i = 0; i < zyidList.size(); i++) {
				RzqxDto rzqxDto = new RzqxDto();
				rzqxDto.setQxid(StringUtil.generateUUID());
				rzqxDto.setRzid(rzid);
				rzqxDto.setZyid((String) zyidList.get(i));
				rzqxDto.setLrry(zyxxDto.getXgry());
				rzqxDtos.add(rzqxDto);
			}
		    int result = dao.batchSaveRzqx(rzqxDtos);
		    if (result != zyidList.size()) {
		    	throw new BusinessException("I99004");
		    }
		}
		return true;
	}

	/** 
	 * 插入资源信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(ZyxxDto zyxxDto){
		zyxxDto.setZyid(StringUtil.generateUUID());
		int result = dao.insert(zyxxDto);
		if(result == 0)
			return false;
		return true;
	}
	
	/**
	 * 数据新增保存
	 * @param zyxxDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addSaveDataPlat(ZyxxDto zyxxDto) {
		// TODO Auto-generated method stub
		if("数据库".equals(zyxxDto.getDjfs())){
			if("0".equals(zyxxDto.getXzfs())){
				//新增资源信息，关联ID为数据库ID
				zyxxDto.setGlid(zyxxDto.getSjkid());
				boolean result = insertDto(zyxxDto);
				if(!result)
					return false;
			}else{
				//新增数据库信息，新增资源信息，关联ID为数据库ID
				boolean result = sjkxxService.insertSjkxxDtoByZyxxDto(zyxxDto);
				if(!result)
					return false;
				zyxxDto.setGlid(zyxxDto.getSjkid());
				result = insertDto(zyxxDto);
				if(!result)
					return false;
			}
		}else{
			//新增接口信息,新增资源信息,关联ID为连接ID,处理参数信息
			boolean result = ljfxxSerivce.insertLjfxxDtoByZyxxDto(zyxxDto);
			if(!result)
				return false;
			zyxxDto.setGlid(zyxxDto.getLjid());
			zyxxDto.setCs1(zyxxDto.getJkcs());
			result = insertDto(zyxxDto);
			if(!result)
				return false;
		}
		return true;
	}

	/**
	 * 获取数据库枚举类型
	 * @return
	 */
	@Override
	public List<String> getSjklx() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		for (DatabaseTypeEnum dir : DatabaseTypeEnum.values()) { 
			list.add(dir.getCode());
		}
		return list;
	}

	/**
	 * 获取调用方式枚举类型
	 * @return
	 */
	@Override
	public List<String> getDyfs() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		for (TransferTypeEnum dir : TransferTypeEnum.values()) { 
			list.add(dir.getCode());
		}
		return list;
	}

	/**
	 * 获取请求方式枚举类型
	 * @return
	 */
	@Override
	public List<String> getQqfs() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		for (RequestMethodEnum dir : RequestMethodEnum.values()) { 
			list.add(dir.getCode());
		}
		return list;
	}
	
	/**
	 * 根据用户代码和资源代码，获取用户授权信息
	 * @param zyxxDto
	 * @return
	 */
	public List<ZyxxDto> getAuthList(ZyxxDto zyxxDto){
		return dao.getAuthList(zyxxDto);
	}
}
