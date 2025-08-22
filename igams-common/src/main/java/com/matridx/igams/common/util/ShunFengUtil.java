package com.matridx.igams.common.util;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.redis.RedisUtil;
import com.sf.csim.express.service.CallExpressServiceTools;
import com.sf.csim.express.service.HttpClientUtil;
import com.sf.csim.express.service.IServiceCodeStandard;
import com.sf.csim.express.service.code.ExpressServiceCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 顺丰工具类
 */
@Component
public class ShunFengUtil {
    @Autowired
    RedisUtil redisUtil;
    @Value("${matridx.sf.client_code:}")
    private String clientCode;
    @Value("${matridx.sf.check_word:}")
    private String checkWord;
    @Value("${matridx.sf.call_url_box:}")
    private String callUrlBox;
    @Value("${matridx.sf.call_url_prod:}")
    private String callUrlProd;


    /**
     * 顺丰下订单接口
     */
    public String SfCreateOrder(String sjmailno,String sjr,String dz,String dh) throws UnsupportedEncodingException {
        /**ExpressServiceCodeEnum     对应速运类-快递APIs
         POSTServiceCodeEnum        对应速运类-驿站APIs
         YJTServiceCodeEnum         对应解决方案-医寄通APIs
         EPSServiceCodeEnum         对应解决方案-快递管家APIs
         详情见code目录下枚举类，客户可自行修改引用的该类
         **/
        IServiceCodeStandard standardService = ExpressServiceCodeEnum.EXP_RECE_CREATE_ORDER; //下订单
        //	IServiceCodeStandard standardService = ExpressServiceCodeEnum.EXP_RECE_SEARCH_ORDER_RESP; //查订单
//          IServiceCodeStandard standardService = ExpressServiceCodeEnum.EXP_RECE_UPDATE_ORDER;//订单取消
        // 	IServiceCodeStandard standardService = ExpressServiceCodeEnum.EXP_RECE_FILTER_ORDER_BSP;//订单筛选
        //  IServiceCodeStandard standardService = ExpressServiceCodeEnum.EXP_RECE_SEARCH_ROUTES;//查路由
        //	IServiceCodeStandard standardService = ExpressServiceCodeEnum.EXP_RECE_GET_SUB_MAILNO;//子单号2
        //	IServiceCodeStandard standardService = ExpressServiceCodeEnum.EXP_RECE_QUERY_SFWAYBILL;//查运费
        //	IServiceCodeStandard standardService = ExpressServiceCodeEnum.EXP_RECE_REGISTER_ROUTE;//注册路由
        //	IServiceCodeStandard standardService =

        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_sf_jjr")));
        String jjr=jsonObject.getString("szz");//顺丰-寄件人
        jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_sf_jjrdz")));
        String jjrdz=jsonObject.getString("szz");//顺丰-寄件人地址
        jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_sf_bgs")));
        String bgs=jsonObject.getString("szz");//顺丰-包裹数
        jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_sf_bgzl")));
        String bgzl=jsonObject.getString("szz");//顺丰-包裹重量
        jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_sf_tj")));
        String tj=jsonObject.getString("szz");//顺丰-体积
        jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_sf_jjrdh")));
        String jjrdh=jsonObject.getString("szz");//顺丰-寄件人电话
        jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_sf_gsmc")));
        String gsmc=jsonObject.getString("szz");//顺丰-公司名称
        jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_sf_hwmc")));
        String hwmc=jsonObject.getString("szz");//顺丰-货物名称
        jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_sf_dzbm")));
        String dzbm=jsonObject.getString("szz");//顺丰-地址编码
        CallExpressServiceTools tools=CallExpressServiceTools.getInstance();

        // set common header
        Map<String, String> params = new HashMap<>();

        String timeStamp = String.valueOf(System.currentTimeMillis());
        String msgData;
        msgData="{\n" +
                "    \"language\":\"zh_CN\",\n" +
                "    \"orderId\":\""+sjmailno+"\",\n" +
                "    \"cargoDetails\":[\n" +
                "        {\n" +
                "            \"name\":\""+hwmc+"\"\n" +
                "        }],\n" +
                "    \"contactInfoList\": [\n" +
                "        {\n" +
                "            \"address\":\""+jjrdz+"\",\n" +
                "            \"company\":\""+gsmc+"\",\n" +
                "            \"contact\":\""+jjr+"\",\n" +
                "            \"contactType\":1,\n" +
                "            \"country\":\""+dzbm+"\",\n" +
                "            \"mobile\":\""+jjrdh+"\",\n" +
                "            \"tel\":\""+jjrdh+"\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"address\":\""+dz+"\",\n" +
                "            \"company\":\"顺丰速运\",\n" +
                "            \"contact\":\""+sjr+"\",\n" +
                "            \"contactType\":2,\n" +
                "            \"country\":\""+dzbm+"\",\n" +
                "            \"mobile\":\""+dh+"\",\n" +
                "            \"tel\":\""+dh+"\"\n" +
                "        }],\n" +
                "    \"parcelQty\":"+bgs+",\n" +
                "    \"volume\":"+tj+",\n" +
                "    \"totalWeight\":"+bgzl+",\n" +
                "    \"isReturnRoutelabel\":0\n" +
                "}\n";
        params.put("partnerID", clientCode);  // 顾客编码
        params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
        params.put("serviceCode",standardService.getCode());// 接口服务码
        params.put("timestamp", timeStamp);
        params.put("msgData", msgData);
        params.put("msgDigest", tools.getMsgDigest(msgData,timeStamp,checkWord));

        String result = HttpClientUtil.post(callUrlProd, params);
        return result;
    }

    /**
     * 顺丰快递取消订单
     */
    public String cancelOrder(String sjkdid) throws UnsupportedEncodingException {
        IServiceCodeStandard standardService = ExpressServiceCodeEnum.EXP_RECE_UPDATE_ORDER;//订单取消
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "inspect_papersend_sf_bgzl")));
        String bgzl=jsonObject.getString("szz");//顺丰-包裹重量
        String msgData ="{\n" +
                "     \"dealType\": 2,\n" +
                "    \"language\": \"zh-CN\",\n" +
                "    \"orderId\": \""+sjkdid+"\",\n" +
                "    \"totalWeight\": "+bgzl+",\n" +
                "    \"waybillNoInfoList\": []\n" +
                "}";
        CallExpressServiceTools client=CallExpressServiceTools.getInstance();
        String timeStamp = String.valueOf(System.currentTimeMillis());
        Map<String, String> params = new HashMap<>();
        params.put("partnerID", clientCode);  // 顾客编码 ，对应丰桥上获取的clientCode
        params.put("requestID", UUID.randomUUID().toString().replace("-", ""));
        params.put("serviceCode",standardService.getCode());// 接口服务码
        params.put("timestamp", timeStamp);
        params.put("msgData", msgData);
        params.put("msgDigest", client.getMsgDigest(msgData,timeStamp,checkWord));//数据签名
        String result = HttpClientUtil.post(callUrlProd, params);
        return result;
    }

}
