package com.matridx.igams.storehouse.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.matridx.igams.production.dao.entities.RdRecordDto;
import com.matridx.igams.production.dao.matridxsql.RdRecordDao;
import com.matridx.igams.storehouse.dao.entities.CkglDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.CkmxDto;
import com.matridx.igams.storehouse.dao.entities.CkmxModel;
import com.matridx.igams.storehouse.dao.entities.FhmxDto;
import com.matridx.igams.storehouse.dao.post.ICkmxDao;
import com.matridx.igams.storehouse.service.svcinterface.ICkmxService;
import org.springframework.util.CollectionUtils;

@Service
public class CkmxServiceImpl extends BaseBasicServiceImpl<CkmxDto, CkmxModel, ICkmxDao> implements ICkmxService{
	@Autowired
	RdRecordDao rdRecordDao;
	/**
	 * 添加出库明细信息
	 *
	 */
	@Override
	public boolean insertckmxs(List<CkmxDto> ckmxDtos) {
		int result = dao.insertckmxs(ckmxDtos);
		return result>0;
	}

	@Override
	public List<String> getU8ckdh(CkmxDto ckmxDto) {
		List<String> strings = new ArrayList<>();
		List<CkmxDto> ckmxDtos = dao.getDtoListByHwid(ckmxDto);
		if (!CollectionUtils.isEmpty(ckmxDtos)){
			List<String> ckdhs = new ArrayList<>();
			for (CkmxDto dto : ckmxDtos) {
				ckdhs.add(dto.getU8ckdh());
			}
            RdRecordDto recordDto = new RdRecordDto();
            recordDto.setIds(ckdhs);
            List<RdRecordDto> u8Ckdh = rdRecordDao.getU8Ckdh(recordDto);
            if (!CollectionUtils.isEmpty(u8Ckdh)){
                for (RdRecordDto rdRecordDto : u8Ckdh) {
                    strings.add(rdRecordDto.getcCode());
                }
            }
        }
		return strings;
	}

	/**
	 * 更新出库明细信息
	 */
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateCkmxList(List<CkmxDto> ckmxDtos) {
		int result = dao.updateCkmxList(ckmxDtos);
		return result>0;
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public Boolean deleteByCkmxids(CkmxDto ckmxDto) {
		return dao.deleteByCkmxids(ckmxDto)!= 0;
	}

	/**
	 * 获取明细数据
	 */
	@Override
	public List<CkmxDto> getDtoMxList(CkmxDto ckmxDto){
		return dao.getDtoMxList(ckmxDto);
	}

	/**
	 * 获取明细数据
	 */
	@Override
	public List<CkmxDto> getHcDtoList(CkmxDto ckmxDto){
		return dao.getHcDtoList(ckmxDto);
	}


	/**
	 * 更新出库明细信息
	 */
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateList(List<FhmxDto> fhmxDtos) {
		int result = dao.updateList(fhmxDtos);
		return result>0;
	}

	/**
	 * 分组查询
	 */
	@Override
	public List<CkmxDto> getDtoGroupBy(String ckid) {
		// TODO Auto-generated method stub
		return dao.getDtoGroupBy(ckid);
	}

	/**
	 * 批量删除
	 */
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteByCkid(CkmxDto ckmxDto) {
		return dao.deleteByCkid(ckmxDto)>0;
	}

	/**
	 * 批量更新
	 */
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateCkmxDtos(List<CkmxDto> ckmxDtos) {
		// TODO Auto-generated method stub
		return dao.updateCkmxDtos(ckmxDtos)>0;
	}
	/**
	 * 获取明细数据
	 */
	public List<CkmxDto> getPrintDtoList(CkmxDto ckmxDto){
		return dao.getPrintDtoList(ckmxDto);
	}

	/**
	 * 获取委外明细数据
	 */
	public List<CkmxDto> getWWDtoList(CkmxDto ckmxDto){
		return dao.getWWDtoList(ckmxDto);
	}

	@Override
	public List<CkmxDto> selectCkmxListByList(List<CkglDto> list) {
		return dao.selectCkmxListByList(list);
	}

	/**
	 * 获取出库明细列表用于异常
	 */
	public List<CkmxDto> getPagedForException(CkmxDto ckmxDto){
		return dao.getPagedForException(ckmxDto);
	}
}
