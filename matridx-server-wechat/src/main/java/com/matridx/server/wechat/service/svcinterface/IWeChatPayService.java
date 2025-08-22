package com.matridx.server.wechat.service.svcinterface;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.WbcxDto;

public interface IWeChatPayService {
	/**
	 * 进行微信支付
	 * @param sjxxDto
	 * @param code
	 * @param wbcxDto
	 * @param request
	 * @return
	 */
    String createWechatPayOrder(SjxxDto sjxxDto, String code, WbcxDto wbcxDto, HttpServletRequest request);

	/**
	 * 微信异步结果通知
	 * @param request
	 * @param response
	 * @return
	 */
    void wxPayNotify(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 查询微信支付结果
	 * @param request
	 * @param response
	 * @return
	 */
    void selectPayNotify(SjxxDto sjxxDto, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 进行微信支付(H5)
	 * @param sjxxDto
	 * @param wbcxDto 
	 * @return
	 */
    String createWechatH5PayOrder(SjxxDto sjxxDto, WbcxDto wbcxDto, HttpServletRequest request);

}
