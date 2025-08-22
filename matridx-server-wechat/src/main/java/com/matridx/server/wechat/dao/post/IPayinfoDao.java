package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.PayinfoDto;
import com.matridx.server.wechat.dao.entities.PayinfoModel;

@Mapper
public interface IPayinfoDao extends BaseBasicDao<PayinfoDto, PayinfoModel>{

	/**
	 * 查询未完成订单信息
	 * @param payinfoDto 
	 * @return
	 */
	List<PayinfoDto> getIncompOrders(PayinfoDto payinfoDto);

	/**
	 * 根据业务ID查询订单信息
	 * @param payinfoDto
	 * @return
	 */
	List<PayinfoDto> selectPayOrders(PayinfoDto payinfoDto);

	/**
	 * 查询支付成功订单
	 * @param payinfoDto
	 * @return
	 */
	List<PayinfoDto> selectPayOrdersSuccess(PayinfoDto payinfoDto);

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
