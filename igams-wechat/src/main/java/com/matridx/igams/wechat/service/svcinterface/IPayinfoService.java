package com.matridx.igams.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.PayinfoDto;
import com.matridx.igams.wechat.dao.entities.PayinfoModel;

public interface IPayinfoService extends BaseBasicService<PayinfoDto, PayinfoModel>{

	/**
	 * 新增送检信息
	 * @param payinfoDto
	 * @throws BusinessException 
	 */
	void addPayinfo(PayinfoDto payinfoDto) throws BusinessException;

	/**
	 * 同步送检信息
	 * @param payinfoDto
	 * @throws BusinessException 
	 */
	void editPayinfo(PayinfoDto payinfoDto) throws BusinessException;

	/**
	 * 根据业务信息查询支付信息
	 * @param ywid
	 * @param ywlx
	 * @return
	 */
	List<PayinfoDto> selectByYwxx(String ywid, String ywlx);

	/**
	 * 支付成功通知
	 * @param payinfoDto
	 */
	void paySuccessMessage(PayinfoDto payinfoDto);

	/**
	 * 支付列表中查询某条支付信息
	 * @param payinfoDto
	 * @return
	 */
	PayinfoDto getDtoPayment(PayinfoDto payinfoDto);

	/**
	 * 查询总金额
	 * @param
	 * @param
	 * @return
	 */
	PayinfoDto selectPayYuan(PayinfoDto payinfoDto);

}
