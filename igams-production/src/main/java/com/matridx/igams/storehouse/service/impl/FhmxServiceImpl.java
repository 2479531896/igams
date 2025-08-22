package com.matridx.igams.storehouse.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.FhmxDto;
import com.matridx.igams.storehouse.dao.entities.FhmxModel;
import com.matridx.igams.storehouse.dao.post.IFhmxDao;
import com.matridx.igams.storehouse.service.svcinterface.IFhmxService;

import java.util.List;

@Service
public class FhmxServiceImpl extends BaseBasicServiceImpl<FhmxDto, FhmxModel, IFhmxDao> implements IFhmxService{

    /**
     * 获取明细数据
     */
    public List<FhmxDto> getDtoMxList(String fhid){
        return dao.getDtoMxList(fhid);
    }

    /**
	 * 获取明细列表 
	 * @param fhmxDto
	 * @return
	 */
	@Override
	public List<FhmxDto> getFhmxList(FhmxDto fhmxDto) {
		return dao.getFhmxList(fhmxDto);
	}

    /**
 	 * 批量新增明细
 	 * @return
 	 */
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertList(List<FhmxDto> fhmxList) {
		int result = dao.insertList(fhmxList);
		return result>0;
	}

    /**
 	  *  查找明细信息
 	 * @param fhmxDto
 	 * @return
 	 */
	@Override
	public List<FhmxDto> getDtoAllByFhid(FhmxDto fhmxDto) {
		return dao.getDtoAllByFhid(fhmxDto);
	}

   /**
 	  * 批量更新发货明细
 	 * @return
 	 */
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateList(List<FhmxDto> fhmxList) {
		int result = dao.updateList(fhmxList);
		return result>0;
	}

    /**
  	  * 分组查询
  	 * @return
  	 */
	@Override
	public List<FhmxDto> getDtoGroupBy(String fhid) {
		// TODO Auto-generated method stub
		return dao.getDtoGroupBy(fhid);
	}

	@Override
	public boolean updateThsls(List<FhmxDto> fhmxDtos) {
		return dao.updateThsls(fhmxDtos);
	}

	@Override
	public boolean updateFhsls(List<FhmxDto> fhmxDtos) {
		return dao.updateFhsls(fhmxDtos);
	}
	/**
	 * 获取明细列表 用于异常
	 * @param fhmxDto
	 * @return
	 */
	public List<FhmxDto> getPagedForException(FhmxDto fhmxDto){
		return dao.getPagedForException(fhmxDto);
	}

	@Override
	public List<FhmxDto> getFhByXsmx(FhmxDto fhmxDto) {
		return dao.getFhByXsmx(fhmxDto);
	}

}
