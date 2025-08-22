package com.matridx.server.wechat.service.svcinterface;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.matridx.server.wechat.dao.entities.SjxxDto;

public interface IAliService {
	/**
	 * 进行阿里支付
	 * 
	 * @param sjxxDto
	 */
    String createAliPayOrder(SjxxDto sjxxDto);
	
	/**
	 * 支付宝支付失败更新
	 * 
	 * @param request
	 */
    String AliPayFaild(HttpServletRequest request);
	
	/**
	 * 支付宝支付通知
	 * 
	 * @param request
	 */
    String aliPayNotify(HttpServletRequest request);
	
	/**
	 * 支付宝支付成功
	 * 
	 * @param request
	 */
    String aliPayComplete(HttpServletRequest request, ModelAndView mv);

	/**
	 * 生成订单二维码(路径)
	 */
    String createOrderQRCode();
}
