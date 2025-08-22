package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.*;
import com.matridx.igams.wechat.dao.post.IHbsfbzDao;
import com.matridx.igams.wechat.service.svcinterface.IHbsfbzService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HbsfbzServiceImpl extends BaseBasicServiceImpl<HbsfbzDto, HbsfbzModel, IHbsfbzDao> implements IHbsfbzService{
	
	/**
	 * 添加收费标准
	 * @param hbsfbzDto
	 * @return
	 */
	@Override
	public boolean insertsfbz(List<HbsfbzDto> hbsfbzDto){
		// TODO Auto-generated method stub
		return dao.insertsfbz(hbsfbzDto);
	}
	
	/**
	 * 查看收费标准
	 * @param hbid
	 * @return
	 */
	@Override
	public List<HbsfbzDto> selectByid(String hbid){
		// TODO Auto-generated method stub
		return dao.selectByid(hbid);
	}

	/**
	 * 从基础数据中查询检测项目
	 * @return
	 */
	@Override
	public List<HbsfbzDto> jcsjjcxm(){
		// TODO Auto-generated method stub
		return dao.jcsjjcxm();
	}

	/**
	 * 查看
	 * @param hbid
	 * @return
	 */
	@Override
	public List<HbsfbzDto> viewByid(String hbid){
		// TODO Auto-generated method stub
		return dao.viewByid(hbid);
	}

	/**
	 * 获取默认收费标准
	 * @param hbsfbzDto
	 * @return
	 */
	@Override
	public HbsfbzDto getDefaultDto(HbsfbzDto hbsfbzDto) {
		// TODO Auto-generated method stub
		return dao.getDefaultDto(hbsfbzDto);
	}

	/**
	 * 通过送检合作伙伴和项目查询收费
	 * @param hbsfbzDto
	 * @return
	 */
	@Override
	public HbsfbzDto getHbsfbzByHbmxAndXm(HbsfbzDto hbsfbzDto) {
		// TODO Auto-generated method stub
		return dao.getHbsfbzByHbmxAndXm(hbsfbzDto);
	}

	/**
	 * 根据送检信息获取收费
	 * @param sjxxlist
	 * @return
	 */
	@Override
	public List<HbsfbzDto> getListBySjxxDtos(List<SjxxDto> sjxxlist) {
		// TODO Auto-generated method stub
		return dao.getListBySjxxDtos(sjxxlist);
	}

	@Override
	public Boolean updateSfbzByHt(SwhtmxDto swhtmxDto) {
		return dao.updateSfbzByHt(swhtmxDto);
	}

	/**
	 * 根据伙伴ID查找收费标准信息
	 * @param sjhbxxDto
	 * @return
	 */
	@Override
	public List<HbsfbzDto> getDtosByHbid(SjhbxxDto sjhbxxDto) {
		return dao.getDtosByHbid(sjhbxxDto);
	}

	@Override
	public boolean batchModSfbz(List<HbsfbzDto> modList) {
		return dao.batchModSfbz(modList);
	}
	/**
	 * 收费标准列表
	 * @param sjhbxxDto
	 * @return
	 */
	@Override
	public List<SjhbxxDto> getPagedSfbzDtoList(SjhbxxDto sjhbxxDto) {
		return dao.getPagedSfbzDtoList(sjhbxxDto);
	}

	/**
	 * 查找商务合同明细有效且状态为10or30的数据，子项目不为null
	 * @param modList
	 * @return
	 */
	@Override
	public List<HbsfbzDto> getYxAndZt(List<HbsfbzDto> modList) {
		return dao.getYxAndZt(modList);
	}

	/**
	 * 根据传入的list获取数据库中没有的收费标准
	 * @param modList
	 * @return
	 */
	@Override
	public List<HbsfbzDto> getAddFromTmp(List<HbsfbzDto> modList) {
		return dao.getAddFromTmp(modList);
	}

	/**
	 * 查找商务合同明细有效且状态为10or30的数据，子项目为null
	 * @param zxmisnotnullList
	 * @return
	 */
	@Override
	public List<HbsfbzDto> getYxAndZtZxmIsNull(List<HbsfbzDto> zxmisnotnullList) {
		return dao.getYxAndZtZxmIsNull(zxmisnotnullList);
	}


}
