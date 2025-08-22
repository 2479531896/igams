package com.matridx.igams.common.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lop.open.api.sdk.DefaultDomainApiClient;
import com.lop.open.api.sdk.LopException;
import com.lop.open.api.sdk.domain.express.OrbLsCancelWaybillInterceptService.cancelOrder.CancelWaybillInterceptReq;
import com.lop.open.api.sdk.domain.express.OrbLsCancelWaybillInterceptService.cancelOrder.CancelWaybillInterceptRes;
import com.lop.open.api.sdk.domain.express.TraceQueryJsf.queryTrace.TraceQueryDTO;
import com.lop.open.api.sdk.domain.express.WaybillJosService.receiveOrderInfo.WaybillDTO;
import com.lop.open.api.sdk.plugin.LopPlugin;
import com.lop.open.api.sdk.plugin.factory.OAuth2PluginFactory;
import com.lop.open.api.sdk.request.express.OrbLsCancelWaybillInterceptServiceCancelOrderLopRequest;
import com.lop.open.api.sdk.request.express.TraceQueryJsfQueryTraceLopRequest;
import com.lop.open.api.sdk.request.express.WaybillJosServiceReceiveOrderInfoLopRequest;
import com.lop.open.api.sdk.response.express.OrbLsCancelWaybillInterceptServiceCancelOrderLopResponse;
import com.lop.open.api.sdk.response.express.TraceQueryJsfQueryTraceLopResponse;
import com.lop.open.api.sdk.response.express.WaybillJosServiceReceiveOrderInfoLopResponse;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 京东工具类
 */
@Component
public class JingDongUtil {
    @Autowired
    RedisUtil redisUtil;
    @Value("${matridx.jingdong.appSecret:}")
    private String appSecret;
    @Value("${matridx.jingdong.appKey:}")
    private String appKey;
    @Value("${matridx.jingdong.serverUrl:}")
    private String serverUrl;
    @Value("${matridx.jingdong.customerCode:}")
    private String customerCode;
    @Value("${matridx.jingdong.refreshToken:}")
    private String refreshToken;

    private final Logger log = LoggerFactory.getLogger(JingDongUtil.class);

    /**
     * 京东快递接单接口（不带单号）
     */
    public String receiveOrderInfo(String sjmailno,String sjr,String dz,String dh,String kdlx) {
        //设置接口域名(有的开放业务同时支持生产和沙箱环境，有的仅支持生产，具体以开放业务中的【API文档-请求地址】为准)，生产域名：https://api.jdl.com 沙箱环境域名：https://uat-api.jdl.com
        //DefaultDomainApiClient对象全局只需要创建一次
        DefaultDomainApiClient client = new DefaultDomainApiClient(serverUrl);

        //入参对象
        WaybillJosServiceReceiveOrderInfoLopRequest request = new WaybillJosServiceReceiveOrderInfoLopRequest();


        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_jd_jjr")));
        String jjr=jsonObject.getString("szz");//京东-寄件人
        jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_jd_jjrdz")));
        String jjrdz=jsonObject.getString("szz");//京东-寄件人地址
        jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_jd_bgs")));
        String bgs=jsonObject.getString("szz");//京东-包裹数
        jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_jd_bgzl")));
        String bgzl=jsonObject.getString("szz");//京东-包裹重量
        jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_jd_tj")));
        String tj=jsonObject.getString("szz");//京东-体积
        //1-特惠送;2-特快送;4-城际闪送;7-微小件;8: 生鲜专送;16-生鲜特快;17-生鲜特惠;20:函速达;21-特惠包裹;22-医药冷链;24-特惠小件;26-冷链专送。默认特惠送，其他类型请找销售开通。
        if(StringUtil.isBlank(kdlx)){
            jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_jd_kdlx")));
            kdlx=jsonObject.getString("szz");//京东-快递类型
        }
        jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_jd_jjrdh")));
        String jjrdh=jsonObject.getString("szz");//京东-寄件人电话

        //设置入参
        WaybillDTO waybillDTO = new  WaybillDTO();
        waybillDTO.setSalePlat("0030001");//销售平台编码，用于区分订单来源，京东商城的订单使用：0010001；非京东平台及其他渠道的订单使用：0030001； 京东平台POP商家自主售后：0090001，使用此编码则会生成JDY开头的运单
        waybillDTO.setCustomerCode(customerCode);//商家编码/青龙业主号/配送编码/月结编码。与京东物流签约后生成
        waybillDTO.setOrderId(sjmailno);//商家订单号，请保证商家编码下唯一，字段长度：1-50。若使用相同的订单号下单则会提示：重复运单
        waybillDTO.setSenderName(jjr);//寄件人姓名
        waybillDTO.setSenderAddress(jjrdz);//寄件地址
        waybillDTO.setSenderMobile(jjrdh);//寄件人手机
        waybillDTO.setReceiveName(sjr);//收件人名称
        waybillDTO.setReceiveAddress(dz);//收件人地址
        waybillDTO.setReceiveMobile(dh);//收件人电话
        waybillDTO.setPackageCount(Integer.parseInt(bgs));//包裹数
        waybillDTO.setWeight(Double.parseDouble(bgzl));//重量
        waybillDTO.setVloumn(Double.parseDouble(tj));//体积
        waybillDTO.setPromiseTimeType(Integer.parseInt(kdlx));//快递类型
        request.setWaybillDTO(waybillDTO);

        //设置插件，必须的操作，不同类型的应用入参不同，请看入参注释
        LopPlugin lopPlugin = null;
        try {
            lopPlugin = OAuth2PluginFactory.produceLopPlugin(
                    client.getServerUrl(), //合作伙伴应用无需传入
                    appKey,
                    appSecret,
                    refreshToken //合作伙伴应用无需传入
            );
        } catch (LopException e) {
            e.printStackTrace();
        }
        request.addLopPlugin(lopPlugin);

        //发送HTTP请求
        WaybillJosServiceReceiveOrderInfoLopResponse response = null;
        try {
            response = client.execute(request);
        } catch (LopException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return JSON.toJSONString(response);
    }

    /**
     * 京东快递取消订单
     */
    public String cancelOrder(String cancelYhZsxm, String mailno){
        OrbLsCancelWaybillInterceptServiceCancelOrderLopResponse response = null;
        try{
            //设置接口域名(有的开放业务同时支持生产和沙箱环境，有的仅支持生产，具体以开放业务中的【API文档-请求地址】为准)，生产域名：https://api.jdl.com 沙箱环境域名：https://uat-api.jdl.com
            DefaultDomainApiClient client = new DefaultDomainApiClient(serverUrl);
            //OrbLsCancelWaybillInterceptService/cancelOrder
            OrbLsCancelWaybillInterceptServiceCancelOrderLopRequest request = new OrbLsCancelWaybillInterceptServiceCancelOrderLopRequest();
            CancelWaybillInterceptReq cancelWaybillInterceptReq = new CancelWaybillInterceptReq();
            cancelWaybillInterceptReq.setCancelOperator(cancelYhZsxm);//取消订单操作人
            cancelWaybillInterceptReq.setCancelReasonCode(1);//取消原因编码：1用户发起取消； 2超时未支付
            cancelWaybillInterceptReq.setCancelTime(new Date());//发起取消时间
            cancelWaybillInterceptReq.setDeliveryId(mailno);//运单号
            cancelWaybillInterceptReq.setInterceptReason("用户发起取消");//取消原因描述
            cancelWaybillInterceptReq.setVendorCode(customerCode);//商家编码
            request.setCancelRequest(cancelWaybillInterceptReq);
            LopPlugin lopPlugin = OAuth2PluginFactory.produceLopPlugin(
                    client.getServerUrl(), //合作伙伴应用无需传入
                    appKey,//appkey
                    appSecret,//appsecret
                    refreshToken //refrshtoken  合作伙伴应用无需传入
            );
            request.addLopPlugin(lopPlugin);
            //发送HTTP请求（请记得更换为自己要使用的接口入参对象、出参对象）
            response = client.execute(request);
            CancelWaybillInterceptRes result = response.getResult();
            System.out.println("xx");
        }catch (Exception e){
            e.printStackTrace();
        }
        return JSON.toJSONString(response);
    }

    /**
     * 查询物流轨迹（商家用）
     */
    public String queryTrace  (String mailno) {
        //设置接口域名(有的开放业务同时支持生产和沙箱环境，有的仅支持生产，具体以开放业务中的【API文档-请求地址】为准)，生产域名：https://api.jdl.com 沙箱环境域名：https://uat-api.jdl.com
        //DefaultDomainApiClient对象全局只需要创建一次
        DefaultDomainApiClient client = new DefaultDomainApiClient(serverUrl);

        //入参对象
        TraceQueryJsfQueryTraceLopRequest request = new TraceQueryJsfQueryTraceLopRequest();

        //设置入参
        TraceQueryDTO traceQueryDTO=new TraceQueryDTO();
        traceQueryDTO.setCustomerCode(customerCode);
        traceQueryDTO.setWaybillCode(mailno);
        traceQueryDTO.setJosPin("");
        request.setQueryDTO(traceQueryDTO);

        //设置插件，必须的操作，不同类型的应用入参不同，请看入参注释
        LopPlugin lopPlugin = null;
        try {
            lopPlugin = OAuth2PluginFactory.produceLopPlugin(
                    client.getServerUrl(), //合作伙伴应用无需传入
                    appKey,
                    appSecret,
                    refreshToken //合作伙伴应用无需传入
            );
        } catch (LopException e) {
            e.printStackTrace();
        }
        request.addLopPlugin(lopPlugin);

        //发送HTTP请求
        TraceQueryJsfQueryTraceLopResponse response = null;
        try {
            response = client.execute(request);
        } catch (LopException e) {
            e.printStackTrace();
        }

        return JSON.toJSONString(response);
    }
}
