package com.matridx.server.wechat.service.impl;

import java.util.List;

import com.matridx.igams.common.util.RabbitUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.wechat.dao.entities.PayinfoDto;
import com.matridx.server.wechat.dao.entities.PayinfoModel;
import com.matridx.server.wechat.dao.post.IPayinfoDao;
import com.matridx.server.wechat.enums.MQWechatTypeEnum;
import com.matridx.server.wechat.service.svcinterface.IPayinfoService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class PayinfoServiceImpl extends BaseBasicServiceImpl<PayinfoDto, PayinfoModel, IPayinfoDao> implements IPayinfoService{

	@Autowired(required=false)
    private AmqpTemplate amqpTempl; 
	
	/**
	 * 新增支付信息(同步)
	 * @param payinfoDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertPayinfoDto(PayinfoDto payinfoDto) {
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(payinfoDto.getZfid())){
			payinfoDto.setZfid(StringUtil.generateUUID());
		}
		//先发送消息至本地
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.PYIF_ADD.getCode() + JSONObject.toJSONString(payinfoDto));
		int insert = dao.insert(payinfoDto);
        return insert != 0;
    }
	
	/**
	 * 修改支付信息(同步)
	 * @param payinfoDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updatePayinfoDto(PayinfoDto payinfoDto) {
		// TODO Auto-generated method stub
		//先发送消息至本地
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQWechatTypeEnum.OPERATE_INSPECTION.getCode(), RabbitEnum.PYIF_ADD.getCode() + JSONObject.toJSONString(payinfoDto));
		return update(payinfoDto);
	}

	/**
	 * 回调信息修改
	 * @param payinfoDto
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean callbackInfo(PayinfoDto payinfoDto) {
		// TODO Auto-generated method stub
		PayinfoDto t_payinfoDto = dao.getDto(payinfoDto);
		// 判断支付记录
		if(t_payinfoDto != null) {
			payinfoDto.setZfid(t_payinfoDto.getZfid());
			return updatePayinfoDto(payinfoDto);
		}else {
			return insertPayinfoDto(payinfoDto);
		}
	}

	/**
	 * 查询未完成订单信息
	 * @param payinfoDto 
	 * @return
	 */
	@Override
	public List<PayinfoDto> getIncompOrders(PayinfoDto payinfoDto) {
		// TODO Auto-generated method stub
		return dao.getIncompOrders(payinfoDto);
	}

	@Override
	public List<PayinfoDto> selectPayOrdersSuccess(PayinfoDto payinfoDto) {
		return dao.selectPayOrdersSuccess(payinfoDto);
	}

	/**
	 * 根据业务ID查询订单信息
	 * @param payinfoDto
	 * @return
	 */
	@Override
	public List<PayinfoDto> selectPayOrders(PayinfoDto payinfoDto) {
		// TODO Auto-generated method stub
		return dao.selectPayOrders(payinfoDto);
	}

	/**
	 * 查询退款未完成订单信息
	 * @param payinfoDto
	 * @return
	 */
	@Override
	public List<PayinfoDto> getRefundOrders(PayinfoDto payinfoDto) {
		// TODO Auto-generated method stub
		return dao.getRefundOrders(payinfoDto);
	}

	/**
	 * 查询当前实付金额
	 * @param payinfoDto
	 * @return
	 */
	@Override
	public String selectPaidAmount(PayinfoDto payinfoDto) {
		// TODO Auto-generated method stub
		return dao.selectPaidAmount(payinfoDto);
	}
}
