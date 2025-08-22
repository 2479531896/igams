package com.matridx.igams.common.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;

/**
 * 扩展支持DTO操作的Service基类
 * @author goofus
 *
 * @param <D> DTO
 * @param <T> MODEL
 * @param <E> DAO
 */
public class BaseBasicServiceImpl<D, T, E extends BaseBasicDao<D,T>> 
		extends BaseServiceImpl<T, E> 
		implements BaseBasicService<D,T> {

	public D getDtoById(String id) {
		return dao.getDtoById(id);
	}

	public D getDto(D t) {
		return dao.getDto(t);
	}

	public List<D> getDtoList(D t) {
		return dao.getDtoList(t);
	}

	public List<D> getPagedDtoList(D t) {
		return dao.getPagedDtoList(t);
	}
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(D t) {
		int result = dao.insertDto(t);
		return result > 0;
	}
	
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateDto(D t) {
		int result = dao.updateDto(t);
		return result > 0;
	}

	/**
	 * 审核完成后处理
	 * @param dto	审核信息，主要是业务ID列表，ywids
	 * @param operator  操作人
	 * @param param 审核传递的额外参数信息
	 * @return
	 * @throws BusinessException
	 */
	public boolean updateAuditEnd(ShxxDto dto, User operator, Map<String, String> param) throws BusinessException
	{
		return true;
	}

}
