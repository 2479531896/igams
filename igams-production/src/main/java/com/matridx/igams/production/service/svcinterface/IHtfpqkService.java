package com.matridx.igams.production.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.HtfpqkDto;
import com.matridx.igams.production.dao.entities.HtfpqkModel;

public interface IHtfpqkService extends BaseBasicService<HtfpqkDto, HtfpqkModel>{

	/**
	 * 新增保存发票情况
	 */
	boolean insertFpqk(HtfpqkDto htfpqkDto) throws BusinessException;
	
	/**
	 * 查看合同发票情况列表
	 */
	List<HtfpqkDto> getFpqkList(HtfpqkDto htfpqkDto);
	
	/**
	 * 合同发票情况修改
	 */
	boolean updateFpqk(HtfpqkDto htfpqkDto) throws BusinessException;
	
	/**
	 * 删除合同发票情况
	 */
	boolean deleteFpqk(HtfpqkDto htfpqkDto);

	/**
	 * 发送钉钉发票信息
	 */
	boolean sendInvoiceMessage(List<DdxxglDto> ddxxglDtos, HtfpqkDto htfpqkDto, String xxbt, String xxnr);

	/**
	 * 根据合同ID,发票号码，发票种类查找
	 */
	List<HtfpqkDto> getFpqk(HtfpqkDto htfpqkDto);
}
