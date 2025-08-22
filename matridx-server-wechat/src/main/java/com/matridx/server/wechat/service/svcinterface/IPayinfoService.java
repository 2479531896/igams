package com.matridx.server.wechat.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.PayinfoDto;
import com.matridx.server.wechat.dao.entities.PayinfoModel;

public interface IPayinfoService extends BaseBasicService<PayinfoDto, PayinfoModel>{

	/**
	 * 新增支付信息(同步)
	 * @param payinfoDto
	 * @return
	 */
    boolean insertPayinfoDto(PayinfoDto payinfoDto);

	/**
	 * 回调信息修改
	 * @param payinfoDto
	 */
    boolean callbackInfo(PayinfoDto payinfoDto);

	/**
	 * 查询未完成订单信息
	 * @param payinfoDto 
	 * @return
	 */
    List<PayinfoDto> getIncompOrders(PayinfoDto payinfoDto);

	/**
	 * 查询支付成功订单
	 * @param payinfoDto
	 * @return
	 */
	List<PayinfoDto> selectPayOrdersSuccess(PayinfoDto payinfoDto);

	/**
	 * 修改支付信息(同步)
	 * @param t_payinfoDto
	 * @return
	 */
    boolean updatePayinfoDto(PayinfoDto t_payinfoDto);

	/**
	 * 根据业务ID查询订单信息
	 * @param payinfoDto
	 * @return
	 */
    List<PayinfoDto> selectPayOrders(PayinfoDto payinfoDto);

	/**
	 * 查询退款未完成订单信息
	 * @param payinfoDto
	 * @return
	 */
    List<PayinfoDto> getRefundOrders(PayinfoDto payinfoDto);

	/**
	 * 查询当前实付金额
	 * @param payinfoDto
	 * @return
	 */
    String selectPaidAmount(PayinfoDto payinfoDto);
}
