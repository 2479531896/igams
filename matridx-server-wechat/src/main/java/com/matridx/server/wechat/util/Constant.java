package com.matridx.server.wechat.util;

public class Constant {
	
	// 环境参数 0:测试环境 1:生产环境
	public static final String SETTING = "1";
	
	/* 微信通知模板 */
	
	// 支付成功通知模板
	public static final String TEMPLATE_PAY_SUCCESS = "FGozxZQILfRMn3JqmYuGAFlWsZ7A//AHjNPS5CQCbFjZdgK2ZZjjfxROn2/IMf0r";
	// 退款成功通知模板
	public static final String TEMPLATE_REFUND_SUCCESS = "wAZ0BDkT0morbN2NxpjtkCLCU7qmuKMrACVyV5Lr9LyiXgxiHO+MQH8s8Ifai2nD";
	
	/* 订单配置参数 */
	
    // 支付有效时间 默认900秒
    public static final String PAY_TIME = "900";
    // 交易场景 OFFLINE:线下;INSURANCE:保险;CHARITY:公益;
    public static final String TRADE_SCENE = "OFFLINE";
    // 交易币种 默认156,目前只支持人民币（156）
    public static final String CURRENCY_CODE = "156";
    // native支付有效时间  m:分钟  h:小时  d:天  1c:当天
    public static final String PAY_TIMEOUT = "15m";
    
    /* 测试环境接口地址 */
    
//    // 通知回调地址
//    public static final String NOTIFY_URL = "http://dev.matridx.com:8691/wechat/pay/payResultNotice";
//	// 收款码申请
//    public static final String QRCODE_APPLY_URL = "https://api.cmburl.cn:8065/polypay/v1.0/mchorders/qrcodeapply";
//	// 支付结果查询
//    public static final String ORDER_QUERY_URL = "https://api.cmburl.cn:8065/polypay/v1.0/mchorders/orderquery";
//	// 关闭订单
//    public static final String CLOSE_URL = "https://api.cmburl.cn:8065/polypay/v1.0/mchorders/close";
//	// 退款申请
//    public static final String REFUND_URL = "https://api.cmburl.cn:8065/polypay/v1.0/mchorders/refund";
//	// 退款结果查询
//    public static final String REFUND_QUERY_URL = "https://api.cmburl.cn:8065/polypay/v1.0/mchorders/refundquery";
//    // 支付宝native码
//    public static final String ZFB_QRCODE_URL = "https://api.cmburl.cn:8065/polypay/v1.0/mchorders/zfbqrcode";
//    // 微信统一下单
//    public static final String ONLINE_PAY_URL = "https://api.cmburl.cn:8065/polypay/v1.0/mchorders/onlinepay";
    
    /* 生产环境接口地址 */
    
    // 通知回调地址
    public static final String NOTIFY_URL = "http://60.191.45.243:8087/wechat/pay/payResultNotice";
	// 收款码申请
    public static final String QRCODE_APPLY_URL = "https://api.cmbchina.com/polypay/v1.0/mchorders/qrcodeapply";
	// 支付结果查询
    public static final String ORDER_QUERY_URL = "https://api.cmbchina.com/polypay/v1.0/mchorders/orderquery";
	// 关闭订单
    public static final String CLOSE_URL = "https://api.cmbchina.com/polypay/v1.0/mchorders/close";
	// 退款申请
    public static final String REFUND_URL = "https://api.cmbchina.com/polypay/v1.0/mchorders/refund";
	// 退款结果查询
    public static final String REFUND_QUERY_URL = "https://api.cmbchina.com/polypay/v1.0/mchorders/refundquery";
    // 支付宝native码
    public static final String ZFB_QRCODE_URL = "https://api.cmbchina.com/polypay/v1.0/mchorders/zfbqrcode";
    // 微信统一下单
    public static final String ONLINE_PAY_URL = "https://api.cmbchina.com/polypay/v1.0/mchorders/onlinepay";
    
}
