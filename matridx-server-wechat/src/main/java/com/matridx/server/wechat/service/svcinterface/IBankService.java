package com.matridx.server.wechat.service.svcinterface;

import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.server.wechat.dao.entities.PayinfoDto;

public interface IBankService {
	
	/**
	 * 生成订单银标码
	 * @param restTemplate
	 * @param payinfoDto
	 * @return
	 * @throws BusinessException 
	 */
    String createOrderQRCode(RestTemplate restTemplate, PayinfoDto payinfoDto) throws BusinessException;

	/**
	 * 根据链接生成二维码临时文件
	 * @param qrCode
	 * @return
	 * @throws BusinessException 
	 * @throws Exception 
	 */
    String generateQRCode(String qrCode) throws BusinessException;

	/**
	 * 二维码临时文件
	 * @param qrCode
	 * @return
	 * @throws BusinessException
	 * @throws Exception
	 */
    String getQRCode(String qrCode) throws BusinessException;

	/**
	 * 微信统一下单
	 * @param restTemplate
	 * @param payinfoDto
	 * @param ipAddress 
	 * @return
	 * @throws BusinessException 
	 */
    Map<String, Object> wechatPayOrder(RestTemplate restTemplate, PayinfoDto payinfoDto, String ipAddress) throws BusinessException;
	
	/**
	 * 支付宝服务窗支付(暂不使用)
	 * @param restTemplate
	 * @param payinfoDto
	 * @return
	 * @throws BusinessException 
	 */
    Map<String, Object> alipayService(RestTemplate restTemplate, PayinfoDto payinfoDto) throws BusinessException;
	
	/**
	 * 支付宝native码
	 * @param restTemplate
	 * @param payinfoDto
	 * @return
	 * @throws BusinessException 
	 */
    Map<String, Object> alipayNative(RestTemplate restTemplate, PayinfoDto payinfoDto) throws BusinessException;
	
	/**
	 * 支付结果查询
	 * @param restTemplate
	 * @param payinfoDto 
	 * @return
	 * @throws BusinessException 
	 */
    Map<String, Object> payResultInquire(RestTemplate restTemplate, PayinfoDto payinfoDto) throws BusinessException;

	/**
	 * 支付/退款结果通知
	 * @param restTemplate
	 * @param requestBodyString
	 * @return
	 * @throws Exception 
	 */
    Map<String, String> payResultNotice(RestTemplate restTemplate, String requestBodyString);

	/**
	 * 关闭订单
	 * @param restTemplate
	 * @param payinfoDto 
	 * @return
	 * @throws BusinessException 
	 */
    Map<String, Object> closeOrder(RestTemplate restTemplate, PayinfoDto payinfoDto) throws BusinessException;

	/**
	 * 退款申请
	 * @param restTemplate
	 * @param payinfoDto
	 * @return
	 * @throws BusinessException 
	 */
    Map<String, Object> refundApply(RestTemplate restTemplate, PayinfoDto payinfoDto) throws BusinessException;

	/**
	 * 退款结果查询
	 * @param restTemplate
	 * @param payinfoDto
	 * @return
	 * @throws BusinessException 
	 */
    Map<String, Object> refundResultInquire(RestTemplate restTemplate, PayinfoDto payinfoDto) throws BusinessException;


	/**
	 * 根据ywid关闭订单
	 * @param payinfoDto
	 * @return
	 */
    Map<String,Object> closeOrdersByYwid(PayinfoDto payinfoDto);

}
