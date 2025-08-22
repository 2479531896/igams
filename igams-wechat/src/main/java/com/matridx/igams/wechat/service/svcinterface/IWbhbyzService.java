package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.dao.entities.WbaqyzDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.WbhbyzDto;
import com.matridx.igams.wechat.dao.entities.WbhbyzModel;

import java.util.List;

public interface IWbhbyzService extends BaseBasicService<WbhbyzDto, WbhbyzModel>{

	/**
	 * 根据代码查询合作伙伴
	 * @param code
	 * @return
	 */
	List<WbhbyzDto> getSjhbByCode(String code);
	/**
	 * 查询合作伙伴
	 * @return
	 */
	List<WbhbyzDto> getSjhbAll();

	/**
	 * 查询伙伴信息
	 */
	List<WbhbyzDto> getListByCode(WbhbyzDto wbhbyzDto);

	/**
	 * 修改操作
	 * @param
	 * @return
	 */
    Boolean modInfo(WbaqyzDto wbaqyzDto);

	/**
	 * 删除操作
	 * @param
	 * @return
	 */
    Boolean delInfo(WbaqyzDto wbaqyzDto) throws BusinessException;

	/**
	 * 关联伙伴功能保存
	 * @param
	 * @return
	 */
    Boolean addSavewbaqyzpartner(SjhbxxDto sjhbxxDto) throws BusinessException;

}
