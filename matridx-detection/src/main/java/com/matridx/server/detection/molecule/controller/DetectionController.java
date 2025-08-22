package com.matridx.server.detection.molecule.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusinessTypeEnum;
import com.matridx.igams.common.enums.PayTypeEnum;
import com.matridx.igams.common.enums.ProgramCodeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.util.RSAUtil;
import com.matridx.server.detection.molecule.dao.entities.BkyyrqDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxxModel;
import com.matridx.server.detection.molecule.dao.entities.HzxxtDto;
import com.matridx.server.detection.molecule.dao.entities.WxbdglDto;
import com.matridx.server.detection.molecule.enums.DetMQTypeEnum;
import com.matridx.server.detection.molecule.enums.NUONUOEnum;
import com.matridx.server.detection.molecule.service.svcinterface.IBkyyrqService;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxmService;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxxService;
import com.matridx.server.detection.molecule.service.svcinterface.IHzxxtService;
import com.matridx.server.detection.molecule.service.svcinterface.IWxbdglService;
import com.matridx.server.wechat.dao.entities.PayinfoDto;
import com.matridx.server.wechat.dao.entities.WeChatUserModel;
import com.matridx.server.wechat.service.svcinterface.IBankService;
import com.matridx.server.wechat.service.svcinterface.IPayinfoService;
import com.matridx.server.wechat.service.svcinterface.IWeChatService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import net.sf.json.JSONArray;
import nuonuo.open.sdk.NNOpenSDK;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//分子检测
@Controller
@RequestMapping("/wechat")
public class DetectionController extends BaseController {
    @Autowired
    IHzxxtService hzxxService;
    @Autowired
    RSAUtil rsaUtil;
    @Autowired
    IBankService bankService;
    @Autowired
    private ICommonService commonService;
    @Autowired
    private IPayinfoService payinfoService;
    @Autowired
    IFzjcxxService fzjcxxService;
    @Autowired
    IXtszService xtszService;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    IJcsjService jcsjService;
    @Autowired(required = false)
    private AmqpTemplate amqpTempl;
    @Autowired
    private IWxbdglService wxbdglService;
    @Autowired
    IWeChatService weChatService;
    @Autowired
    IFzjcxmService fzjcxmService;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
	@Autowired
    RedisUtil redisUtil;
	@Autowired
    IBkyyrqService bkyyrqService;
    private Logger log = LoggerFactory.getLogger(DetectionController.class);

    /**
     * 切换绑定信息页面
     * @param wxid
     * @param request
     * @return
     */
    @RequestMapping(value="/changePhoneBinding")
    public ModelAndView changePhoneBinding(@RequestParam(name = "wxid", required = false) String wxid,@RequestParam(name = "sjh", required = false) String sjh, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("wechat/changePhoneBinding");
        mav.addObject("sjh",sjh);
        mav.addObject("wxid", wxid);
        mav.addObject("request",request);
        return mav;
    }

    /**
     * 切换绑定页面
     * @param wxid
     * @param request
     * @return
     */
    @RequestMapping(value="/phoneBinding")
    public ModelAndView phoneBinding(@RequestParam(name = "wxid", required = false) String wxid, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("wechat/phoneBinding");
        mav.addObject("wxid", wxid);
        mav.addObject("request",request);
        return mav;
    }

    /**
     * 新冠系统选择页面
     * @param wxid
     * @param code
     * @param request
     * @return
     */
    @RequestMapping(value="/detectionChoose")
    public ModelAndView detectionChoose(@RequestParam(name = "wxid", required = false) String wxid, @RequestParam(name = "code", required = false) String code,HttpServletRequest request){
        if (StringUtil.isNotBlank(code)){
            try {
                weChatService.getAccessToken(code);
            } catch (BusinessException e) {
                log.error(e.toString());
                e.printStackTrace();
            }
        }
        WxbdglDto sjhByWxid = wxbdglService.getDtoListByWxid(wxid);
        if (sjhByWxid != null) {
            //说明已有绑定手机号
            ModelAndView mav = new ModelAndView("wechat/detectionChoose_Index");
            DBEncrypt dbEncrypt = new DBEncrypt();
            String sjh = dbEncrypt.dCode(sjhByWxid.getSjh());
            sjh = (sjh.substring(0,3)+"*****"+sjh.substring(7,11));
            mav.addObject("wxid", wxid);
            mav.addObject("sjh", sjh);
            mav.addObject("request",request);
            return mav;
        }else {
            //说明未绑定任何手机号
            //显示绑定手机页面
            ModelAndView mav = new ModelAndView("wechat/phoneBinding");
            mav.addObject("wxid", wxid);
            mav.addObject("request",request);
            return mav;
        }
    }

    /**
     * 新冠支付 开票
     * @param wxid
     * @param fzjcid
     * @param sfje
     * @param request
     * @return
     */
    @RequestMapping(value="/invoiceInfo")
    public ModelAndView invoiceInfo(@RequestParam(name = "wxid", required = false) String wxid,@RequestParam(name = "fzjcid", required = false) String fzjcid,@RequestParam(name = "sfje", required = false) String sfje, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("wechat/invoiceInfo");
        mav.addObject("fzjcid", fzjcid);
        mav.addObject("wxid", wxid);
        mav.addObject("sfje",sfje);
        mav.addObject("request",request);
        return mav;
    }

    /**
     * 新冠支付 发票详情页面
     * @param wxid
     * @param fzjcid
     * @return
     */
    @RequestMapping(value="/invoiceImgInfo")
    public ModelAndView invoiceImgInfo(@RequestParam(name = "wxid", required = false) String wxid,@RequestParam(name = "fzjcid", required = false) String fzjcid){
        ModelAndView mav = new ModelAndView("wechat/invoiceImgInfo");
        if (StringUtil.isNotBlank(fzjcid)){
            FzjcxxDto fzjcxxDto = new FzjcxxDto();
            fzjcxxDto.setFzjcid(fzjcid);
            FzjcxxDto fzjcxxDto1 = fzjcxxService.getDto(fzjcxxDto);
            mav.addObject("fzjcxxDto", fzjcxxDto1);
        }
        mav.addObject("wxid", wxid);
        return mav;
    }

    /**
     * 杰毅生物公众号新冠系统选择页面
     * @param code
     * @param state
     * @param request
     * @return
     */
    @RequestMapping(value="/detectionChooseOnMatridx")
    public ModelAndView detectionChooseOnMatridx(@RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state, HttpServletRequest request){
        String wbcxdm = request.getParameter("wbcxdm");
        if (StringUtil.isBlank(wbcxdm)){
            wbcxdm = ProgramCodeEnum.MATRIDX.getCode();
        }
        String wxid = "";
        String organ = request.getParameter("organ");
        if (StringUtil.isNotBlank(organ)){
            wxid = organ;
        }else {
            WeChatUserModel userModel = weChatService.getReportListPageByUserAuth(code, state, wbcxdm);
            if (userModel != null){
                wxid = userModel.getOpenid();
            }
        }
        WxbdglDto sjhByWxid = wxbdglService.getDtoListByWxid(wxid);
        if (sjhByWxid != null) {
            //说明已有绑定手机号
            ModelAndView mav = new ModelAndView("wechat/detectionChoose_Index");
            DBEncrypt dbEncrypt = new DBEncrypt();
            String sjh = dbEncrypt.dCode(sjhByWxid.getSjh());
            sjh = (sjh.substring(0,3)+"*****"+sjh.substring(7,11));
            mav.addObject("wxid", wxid);
            mav.addObject("sjh", sjh);
            mav.addObject("request",request);
            return mav;
        }else {
            //说明未绑定任何手机号
            //显示绑定手机页面
            ModelAndView mav = new ModelAndView("wechat/phoneBinding");
            mav.addObject("wxid", wxid);
            mav.addObject("request",request);
            return mav;
        }
    }

    /**
     * 新冠病毒核酸检测预约
     * @param wxid
     * @param request
     * @return
     */
    @RequestMapping(value="/detectionAppointment")
    public ModelAndView detectionAppointment(@RequestParam(name = "wxid", required = false) String wxid, HttpServletRequest request){
        ModelAndView mav = new ModelAndView("wechat/detectionAppointment");
        DBEncrypt p = new DBEncrypt();
        JcsjDto jclx = new JcsjDto();
        List<JcsjDto> jclxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        for (JcsjDto lsjclx : jclxs) {
            if ("TYPE_COVID".equals(lsjclx.getCsdm())){
                jclx = lsjclx;
                break;
            }
        }
        List<JcsjDto> jcxmlist = new ArrayList<>();
        List<JcsjDto> jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
        for (JcsjDto jcxm : jcxms) {
            if (StringUtil.isNotBlank(jcxm.getFcsid())) {
                if (jcxm.getFcsid().equals(jclx.getCsid()) && "0".equals(jcxm.getScbj())) {
                    if ("1".equals(jcxm.getSfmr())) {
                        jcxm.setChecked("1");
                    }
                    jcxmlist.add(jcxm);
                }
            }
        }
        mav.addObject("jcxmlist",jcxmlist);
        HzxxtDto hzxxDto = new HzxxtDto();
        hzxxDto.setWxid(wxid);
        HzxxtDto hzxxtDto = hzxxService.getLastHzxxByWxid(hzxxDto);
        if (hzxxtDto!=null){
            hzxxDto.setXm(hzxxtDto.getXm());
            hzxxDto.setHzid(hzxxtDto.getHzid());
            hzxxDto.setZjh(p.dCode(hzxxtDto.getZjh()));
            hzxxDto.setSj(p.dCode(hzxxtDto.getSj()));
            hzxxDto.setZjlx(hzxxtDto.getZjlx());
            hzxxDto.setXxdz(hzxxtDto.getXxdz());
            hzxxDto.setSf(hzxxtDto.getSf());
            hzxxDto.setCs(hzxxtDto.getCs());
            hzxxDto.setCyd(hzxxtDto.getCyd());
        } else {
            List<JcsjDto> collect_samples = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
            for (JcsjDto collect_sample : collect_samples) {
                if ("1".equals(collect_sample.getSfmr())){
                    hzxxDto.setCyd(collect_sample.getCsid());
                }
            }
        }
        mav.addObject("wxid", wxid);
        mav.addObject("title", "新冠病毒核酸检测预约");
        mav.addObject("hzxxDto", hzxxDto);
        mav.addObject("request",request);
        mav.addObject("ywlx", BusinessTypeEnum.XG.getCode());
        XtszDto xtszDto = xtszService.selectById("xg_zfje");
        String fkje ="";
        if (null!=xtszDto){
            fkje  =  xtszDto.getSzz();//获取token 访问令牌
        }
        mav.addObject("fkje", fkje);
        //查询系统设置中预约时间段
        XtszDto appoinmentTimeStart = xtszService.selectById("appoinmentTimeStart");
        XtszDto appoinmentTimeEnd = xtszService.selectById("appoinmentTimeEnd");
        XtszDto appoinmentDateLength = xtszService.selectById("appoinmentDateLength");
        if (appoinmentDateLength!=null) {
            mav.addObject("appoinmentDateLength",appoinmentDateLength.getSzz());
        }else {
            mav.addObject("appoinmentDateLength","60");
        }
        mav.addObject("appoinmentTimeStart",appoinmentTimeStart.getSzz());
        mav.addObject("appoinmentTimeEnd",appoinmentTimeEnd.getSzz());
        return mav;
    }

    /**
     * 新冠支付 保存诺诺支付
     * @param hzxxDto
     * @return
     */
    @RequestMapping(value="/nuonuoWxPay")
    @ResponseBody
    public Map<String, Object> nuonuoWxPay(HzxxtDto hzxxDto){
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(hzxxDto.getJclx())){
            List<JcsjDto> jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
            if (jcxms!=null&&jcxms.size()>0){
                for (JcsjDto jcsjDto:jcxms) {
                    if (hzxxDto.getJclx().equals(jcsjDto.getCsdm())){
                        hzxxDto.setJclx(jcsjDto.getCsid());
                    }
                }
            }
        }
        if (StringUtil.isNotBlank(hzxxDto.getWxid())) {
            NNOpenSDK sdk = NNOpenSDK.getIntance();
            String taxnum = NUONUOEnum.TAXNUM.getCode(); // 授权企业税号
            String appKey = NUONUOEnum.APPKEY.getCode();
            String appSecret = NUONUOEnum.APPSECRET.getCode();
            String method1 = NUONUOEnum.PAY_METHOD.getCode(); // API方法名
            String method =null;
            try {
                method = new String( method1.getBytes("utf-8") , "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            String accessToken = NNOpenSDK.getIntance().getMerchantToken(appKey,appSecret);//获取token 访问令牌
            XtszDto xtszDto = xtszService.selectById("access_token");
            String accessToken ="";
            if (null!=xtszDto){
                accessToken  =  xtszDto.getSzz();//获取token 访问令牌
            }
            String val = StringUtil.generateUUID();
//            if (StringUtil.isNotBlank(hzxxDto.getOrderno()) && !"null".equals(hzxxDto.getOrderno())) {
//                val = hzxxDto.getOrderno();
//            }else{
//                val = StringUtil.generateUUID();
//            }
            DBEncrypt p = new DBEncrypt();
            String address = p.dCode(applicationurl);
            String returnUrl = address+NUONUOEnum.RETURN_URL.getCode()+"?fzjcid="+hzxxDto.getFzjcid()+"&wxid="+hzxxDto.getWxid()+"&orderno="+val;
//            String returnUrl = "http://60.191.45.243:8087"+NUONUOEnum.RETURN_URL.getCode()+"?fzjcid="+hzxxDto.getFzjcid()+"&wxid="+hzxxDto.getWxid();
            String callbackUrl = address+NUONUOEnum.CALLBACK_URL.getCode()+"?fzjcid="+hzxxDto.getFzjcid();
//            String callbackUrl = "http://60.191.45.243:8087"+NUONUOEnum.CALLBACK_URL.getCode()+"?fzjcid="+hzxxDto.getFzjcid();

            FzjcxxDto fzjcxxDto = new FzjcxxDto();
            fzjcxxDto.setFzjcid(hzxxDto.getFzjcid());
            fzjcxxDto.setJclx(hzxxDto.getJclx());
            FzjcxxDto fzjcxxDto1 = fzjcxxService.getDto(fzjcxxDto);
            String content = "{" +
                    "\"taxNo\": \""+taxnum+"\"," +  //税号
                    "\"customerOrderNo\":  \""+val+"\"," + //订单号
                    "\"amount\": \""+hzxxDto.getAmount()+"\"," +  //支付金额
                    "\"subject\":\""+hzxxDto.getSubject()+"\"," +  //交易商品
                    "\"returnUrl\": \""+returnUrl+"\"," + //支付成功后跳回地址
                    "\"billingType\": \"0\"," + //支付即开票标志（0--仅支付 1支付+开票）
//                "\"sn\": \"1809CA884902\"," + //设备号
                    "\"sellerNote\": \""+(StringUtil.isNotBlank(fzjcxxDto1.getXm())?fzjcxxDto1.getXm():"杰毅生物")+"\"," + //商户备注
//                "\"deptId\": \"8F0095616A17484DAD2C17925C04B78E\"," + //部门id
                    "\"appKey\": \""+appKey+"\"," + //appKey
                    "\"timeExpire\": \"15\"," + //订单有效期（单位为分）范围1-120 默认为120
                    "\"invoiceExpire\": \"60\"," + //开票有效期（单位为天）范围 1-360，默认为60天
                    "\"payee\": \"杰毅生物\"," + //收款人
                    "\"autoType\": \"0\"," + //是否自动提交支付，0：否，1：是，为空则默认为“否”
                    "\"callbackMethod\": \"2\"," + //1-按开放平台配置地址回调 2-按本接口传参地址回调，未传默认为1
                   "\"callbackUrl\":  \""+callbackUrl+"\"," + //传参回调地址,但若回调方式配置为2，则该字段必传
                    "\"goodsListItem\": [ " +
                    "{\"amount\": \""+hzxxDto.getAmount()+"\"," + //含税单价，0.01-999999999.99
//                    "\"specification\": \"yll03-1391\"," +  ////规格型号，如只
                    "\"goodsName\": \""+hzxxDto.getSubject()+"\"," + //商品名称
                    "\"goodsNum\": \"1\" " + //商品数量，范围1-99999999
                    "}]" +
                    "}";
            String url = NUONUOEnum.URL.getCode();  // SDK请求地址
            String senid = UUID.randomUUID().toString().replace("-", ""); // 唯一标识，32位随机码，无需修改，保持默认即可
            String payUrl = sdk.sendPostSyncRequest(url, senid, appKey, appSecret, accessToken, taxnum, method, content);
            log.error("诺诺支付地址"+payUrl);
            fzjcxxDto.setOrderno(val);
            boolean success = fzjcxxService.update(fzjcxxDto);
            if (success){
                amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.FKBJ_MOD.getCode()+ JSONObject.toJSONString(fzjcxxDto));
            }
            map.put("status","success");
            map.put("payUrl",payUrl);

        }else{
            map.put("status","fail");
            map.put("message","未获取到您的微信\n请重新至公众号打开网页");
        }
        return map;
    }

    /**
     * 新冠支付 保存诺诺支付
     * @param fzjcxxDto
     * @return
     */
    @RequestMapping(value="/refundQuery")
    @ResponseBody
    public Map<String, Object> refundQuery(FzjcxxDto fzjcxxDto){
        Map<String, Object> map = new HashMap<>();
        FzjcxxDto fzjcxxDto1 = fzjcxxService.getDto(fzjcxxDto);
        if (StringUtil.isNotBlank(fzjcxxDto1.getCjsj())){
            map.put("status","fail");
            map.put("message","该条数据已采集\n不允许取消");
            return map;
        }
        if (StringUtil.isNotBlank(fzjcxxDto.getOrderno())) {
            NNOpenSDK sdk = NNOpenSDK.getIntance();
            String taxnum = NUONUOEnum.TAXNUM.getCode(); // 授权企业税号
            String appKey = NUONUOEnum.APPKEY.getCode();
            String appSecret = NUONUOEnum.APPSECRET.getCode();
            String method1 = NUONUOEnum.REFUND_QUERY.getCode(); // API方法名
            String method =null;
            try {
                method = new String( method1.getBytes("utf-8") , "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            XtszDto xtszDto = xtszService.selectById("access_token");
            String accessToken ="";
            if (null!=xtszDto){
                accessToken  =  xtszDto.getSzz();//获取token 访问令牌
            }

            String content = "{" +
                    "\"taxNo\": \""+taxnum+"\"," +  //税号
                    "\"customerOrderNo\":  \""+fzjcxxDto.getOrderno()+"\"," + //订单号
                    "\"partRefundAmount\": \""+fzjcxxDto.getSfje()+"\"," +  //支付金额
                    "\"userName\":\""+"新冠病毒检测"+"\"," +  //交易商品
                    "\"refundReason\": \""+"杭州杰毅用户退款"+"\"," + //退款说明
                    "\"appKey\": \""+appKey+"\"" + //appKey
                    "}";
            String url = NUONUOEnum.URL.getCode();  // SDK请求地址
            String senid = UUID.randomUUID().toString().replace("-", ""); // 唯一标识，32位随机码，无需修改，保持默认即可
            String result  = sdk.sendPostSyncRequest(url, senid, appKey, appSecret, accessToken, taxnum, method, content);
            log.error("诺诺退款信息"+result);
            map.put("status","success");
            map.put("result",result );

        }else{
            map.put("status","fail");
            map.put("message","未获取到您的微信\n请重新至公众号打开网页");
        }
        return map;
    }

    /**
     * 新冠病毒核酸检测预约 小程序接口
     * @param wxid
     * @param request
     * @return
     */
    @RequestMapping(value="/miniDetectionAppointment")
    @ResponseBody
    public Map<String,Object> miniDetectionAppointment(@RequestParam(name = "wxid", required = false) String wxid, HttpServletRequest request){
        Map<String,Object> map = new HashMap<>();
        //证件类型
        String zjlxjson = JSONArray.fromObject(redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.IDCARD_CATEGORY.getCode())).toString();
        map.put("zjlxlist",zjlxjson.replaceAll("csmc","label").replaceAll("csid","value"));
        //分子检测项目（删除标记为0且父参数id等于检测类型为TYPE_COVID的参数id）
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        JcsjDto jclx = new JcsjDto();
        for (JcsjDto lsjclx : jclxList) {
            if ("TYPE_COVID".equals(lsjclx.getCsdm())){
                jclx = lsjclx;
            }
        }
        List<JcsjDto> lsjcxmlist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
        List<JcsjDto> jcxmlist = new ArrayList<>();
        for (JcsjDto jcxm : lsjcxmlist) {
            if ("0".equals(jcxm.getScbj()) && jcxm.getFcsid().equals(jclx.getCsid())){
                if ("1".equals(jcxm.getSfmr())){
                    jcxm.setChecked("1");
                }
                jcxmlist.add(jcxm);
            }
        }
        map.put("jcxmlist",jcxmlist);
        //根据微信id查询最近一条预约信息，并返回（若）
        HzxxtDto hzxxDto = new HzxxtDto();
        hzxxDto.setWxid(wxid);
        HzxxtDto hzxxtDto = hzxxService.getLastHzxxByWxid(hzxxDto);
        DBEncrypt p = new DBEncrypt();
        if (hzxxtDto!=null){
            hzxxDto.setXm(hzxxtDto.getXm());
            hzxxDto.setHzid(hzxxtDto.getHzid());
            hzxxDto.setZjh(p.dCode(hzxxtDto.getZjh()));
            hzxxDto.setSj(p.dCode(hzxxtDto.getSj()));
            hzxxDto.setZjlx(hzxxtDto.getZjlx());
            hzxxDto.setXxdz(hzxxtDto.getXxdz());
            hzxxDto.setSf(hzxxtDto.getSf());
            hzxxDto.setCs(hzxxtDto.getCs());
            hzxxDto.setCyd(hzxxtDto.getCyd());
        }
        map.put("wxid", wxid);
        map.put("title", "新冠病毒核酸检测预约");
        map.put("hzxxDto", hzxxDto);
        map.put("request",request);
        return map;
    }

    /**
     * 新冠病毒核酸检测预约信息保存
     * @param hzxxDto
     * @return
     */
    @RequestMapping(value="/detectionAppointmentSave")
    @ResponseBody
    public Map<String, Object> detectionAppointmentSave(HzxxtDto hzxxDto){
        if (StringUtil.isNotBlank(hzxxDto.getWxid())) {
            if (StringUtil.isNotBlank(hzxxDto.getJclx())){
                List<JcsjDto> jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
                if (jcxms!=null&&jcxms.size()>0){
                    for (JcsjDto jcsjDto:jcxms) {
                        if (hzxxDto.getJclx().equals(jcsjDto.getCsdm())){
                            hzxxDto.setJclx(jcsjDto.getCsid());
                        }
                    }
                }
            }
            return hzxxService.detectionAppointmentAdd(hzxxDto);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("status","fail");
        map.put("message","未获取到您的微信\n请重新至公众号打开网页");
        return map;
    }

    /**
     * 新冠支付 创建、修改开票
     * @param fzjcxxDto
     * @return
     */
    @RequestMapping(value="/creatInvoice")
    @ResponseBody
    public Map<String, Object> creatInvoice(FzjcxxDto fzjcxxDto){
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(fzjcxxDto.getFzjcid())) {
            FzjcxxDto fzjcxxDto1 = fzjcxxService.getDto(fzjcxxDto);
            if (null != fzjcxxDto1 && StringUtil.isNotBlank(fzjcxxDto1.getFphm()) && "1".equals(fzjcxxDto1.getKpbj())){
                map.put("invoiceUrl",fzjcxxDto1.getFphm());
                map.put("status","fphm");
            }else{
                NNOpenSDK sdk = NNOpenSDK.getIntance();
                String taxnum = NUONUOEnum.INVOICE_TAXNUM.getCode(); // 授权企业税号
                String appKey = NUONUOEnum.INVOICE_APPKEY.getCode();
                String appSecret = NUONUOEnum.INVOICE_APPSECRET.getCode();
                String method1 = NUONUOEnum.INVOICE_METHOD.getCode(); // API方法名
                String method =null;
                try {
                    method = new String( method1.getBytes("utf-8") , "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                XtszDto xtszDto = xtszService.selectById("invoice_access_token");
                String accessToken ="";
                if (null!=xtszDto){
                    accessToken  =  xtszDto.getSzz();//获取token 访问令牌
                }
//            String accessToken = NNOpenSDK.getIntance().getMerchantToken(appKey,appSecret);//获取token 访问令牌
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String content = "{" +
                        "\"order\":{" +
                        "\"buyerName\":\""+(StringUtil.isNotBlank(fzjcxxDto.getBuyerName())?fzjcxxDto.getBuyerName():"")+"\"," +//Y购方名称：企业/个人
                        "\"buyerTaxNum\":\""+(StringUtil.isNotBlank(fzjcxxDto.getBuyerTaxNum())?fzjcxxDto.getBuyerTaxNum():"")+"\"," +//购方税号（企业必填）
                        "\"buyerTel\":\""+(StringUtil.isNotBlank(fzjcxxDto.getBuyerTel())?fzjcxxDto.getBuyerTel():"")+"\"," +//N固定购方电话
                        "\"buyerAddress\":\""+(StringUtil.isNotBlank(fzjcxxDto.getBuyerAddress())?fzjcxxDto.getBuyerAddress():"")+"\"," +//N购方地址
                        "\"buyerPhone\":\""+(StringUtil.isNotBlank(fzjcxxDto.getBuyerPhone())?fzjcxxDto.getBuyerPhone():"")+"\"," + //Y购方电话
                        "\"email\":\""+(StringUtil.isNotBlank(fzjcxxDto.getEmail())?fzjcxxDto.getEmail():"")+"\"," +//Y购方邮箱
                    "\"buyerAccount\":\""+(StringUtil.isNotBlank(fzjcxxDto.getBuyerAccounts())?fzjcxxDto.getBuyerAccounts():"")+(StringUtil.isNotBlank(fzjcxxDto.getBuyerAccount())?fzjcxxDto.getBuyerAccount():"")+"\"," + //N购方银行账号和地址：中国工商银行 111111111111
                        "\"salerTaxNum\":\""+taxnum+"\"," + //Y销方税号：339901999999142
                        "\"salerTel\":\"0571-86038615\"," +//Y销方电话
                        "\"salerAddress\":\"浙江省杭州市余杭区良渚街道金昌路2073号3号楼103室\"," + //Y销方地址
                    "\"salerAccount\":\"招商银行杭州分行营业部571914232010401\"," + //N销方销方银行账号和开户行地址
                        "\"orderNo\":\""+fzjcxxDto1.getOrderno()+"\"," + //Y订单号（每个企业唯一）
                        "\"invoiceDate\":\""+ simpleDateFormat.format(date)+"\"," + //Y订单时间
                        "\"clerk\":\"卢慧莲\"," + //Y开票员
                        "\"checker\":\"邵益旺\"," + //N复核人
                        "\"payee\":\"董文丹\"," + //N收款人
                        "\"invoiceType\":\"1\"," + //Y开票类型：1:蓝票;2:红票
                        "\"invoiceLine\":\""+fzjcxxDto.getFplx()+"\"," + //N开票种类：p,普通发票(电票)(默认);c,普通发票(纸票);s,专用发票;e,收购发票(电票);f,收购发票(纸质);r,普通发票(卷式);b,增值税电子专用发票;j,机动车销售统一发票
                        "\"hiddenBmbbbh\":\"0\"," + //N是否隐藏编码表版本号 0-否 1-是（默认0，在企业资质中也配置为是隐藏的时候，并且此字段传1的时候代开发票 税率显示***）
//                    "\"callBackUrl\":\"N开票完成回传发票信息地址\"," + //N开票完成回传发票信息地址
                        "\"proxyInvoiceFlag\":\"0\"," + //N代开标志：0非代开;1代开。代开蓝票时备注要求填写文案：代开企业税号:***,代开企业名称:***；代开红票时备注要求填写文案：对应正数发票代码:***号码:***代开企业税号:***代开企业名称:***
                        "\"invoiceDetail\":[" + //(Y发票明细，支持填写商品明细最大2000行（包含折扣行、被折扣行）)
                        "{" +
                        "\"goodsName\":\"检测服务费\"," + //Y商品名称（如invoiceLineProperty =1，则此商品行为折扣行，折扣行不允许多行折扣，折扣行必须紧邻被折扣行，商品名称必须与被折扣行一致）
                        "\"goodsCode\":\"3070202\"," + //N商品编码（商品税收分类编码开发者自行填写）
//                    "\"selfCode\":\"N自行编码\"," + //N自行编码
                        "\"withTaxFlag\":\"1\"," + //Y单价含税标志：0:不含税,1:含税
                        "\"price\":\""+fzjcxxDto.getSfje()+"\"," + //N单价（精确到小数点后8位），当单价(price)为空时，数量(num)也必须为空；(price)为空时，含税金额(taxIncludedAmount)、不含税金额(taxExcludedAmount)、税额(tax)都不能为空
                        "\"num\":\"1\"," + //N数量（精确到小数点后8位，开具红票时数量为负数）
//                    "\"unit\":\"次\"," + //N单位
//                    "\"specType\":\"N规格型号\"," + //N规格型号
//                    "\"tax\":\"N税额\"," + //N税额
//                    "\"taxExcludedAmount\":\"N不含税金额。红票为负。不含税金额、税额、含税金额任何一个不传时，会根据传入的单价，数量进行计算，可能和实际数值存在误差，建议都传入\"," + //N不含税金额。红票为负。不含税金额、税额、含税金额任何一个不传时，会根据传入的单价，数量进行计算，可能和实际数值存在误差，建议都传入
                        "\"taxRate\":\"0.06\"," + //Y税率，注：纸票清单红票存在为null的情况
//                    "\"taxIncludedAmount\":\"\"," + //N含税金额，[不含税金额] + [税额] = [含税金额]，红票为负。不含税金额、税额、含税金额任何一个不传时，会根据传入的单价，数量进行计算，可能和实际数值存在误差，建议都传入
                        "\"invoiceLineProperty\":\"0\"," + //N发票行性质：0,正常行;1,折扣行;2,被折扣行
                        "\"favouredPolicyFlag\":\"0\"," + //N优惠政策标识：0,不使用;1,使用
//                    "\"favouredPolicyName\":\"免税\"," + //N增值税特殊管理（优惠政策名称）,当favouredPolicyFlag为1时，此项必填
//                    "\"deduction\":\"\"," + //N扣除额，差额征收时填写，目前只支持填写一项。 注意：当传0、空或字段不传时，都表示非差额征税；传0.00才表示差额征税：0.00
                        "\"zeroRateFlag\":\"\"" + //零税率标识：空,非零税率;1,免税;2,不征税;3,普通零税率；1、当税率为：0%，且增值税特殊管理：为“免税”， 零税率标识：需传“1” 2、当税率为：0%，且增值税特殊管理：为不征税零税率标识：需传“2” 3、当税率为：0%，且增值税特殊管理：为空 零税率标识：需传“3”
                        "}" +
                        "]" +
                        "}" +
                        "}";
                String url = NUONUOEnum.URL.getCode();  // SDK请求地址
                String senid = UUID.randomUUID().toString().replace("-", ""); // 唯一标识，32位随机码，无需修改，保持默认即可
                String invoiceUrl = sdk.sendPostSyncRequest(url, senid, appKey, appSecret, accessToken, taxnum, method, content);
                log.error("诺诺发票信息"+invoiceUrl);
                map.put("invoiceUrl",invoiceUrl);
                map.put("status","success");
            }
        }else{
            map.put("status","fail");
            map.put("message","未获取到您的微信\n请重新至公众号打开网页");
        }
        return map;
    }

    /**
     * 新冠支付 选择开票
     * @param fzjcxxDto
     * @return
     */
    @RequestMapping(value="/selectInvoice")
    @ResponseBody
    public Map<String, Object> selectInvoice(FzjcxxDto fzjcxxDto){
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(fzjcxxDto.getFzjcid())){
            FzjcxxDto fzjcxxDto1 = fzjcxxService.getDto(fzjcxxDto);
            if (StringUtil.isBlank(fzjcxxDto1.getImgurl())){
                map.put("status","success");
                NNOpenSDK sdk = NNOpenSDK.getIntance();
                String taxnum = NUONUOEnum.INVOICE_TAXNUM.getCode(); // 授权企业税号
                String appKey = NUONUOEnum.INVOICE_APPKEY.getCode();
                String appSecret = NUONUOEnum.INVOICE_APPSECRET.getCode();
                String method1 = NUONUOEnum.INVOICE_INFO_METHOD.getCode(); // API方法名
                String method =null;
                try {
                    method = new String( method1.getBytes("utf-8") , "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                XtszDto xtszDto = xtszService.selectById("invoice_access_token");
                String accessToken ="";
                if (null!=xtszDto){
                    accessToken  =  xtszDto.getSzz();//获取token 访问令牌
                }
//        String accessToken = NNOpenSDK.getIntance().getMerchantToken(appKey,appSecret);//获取token 访问令牌
//            String val = StringUtil.generateUUID();
//            DBEncrypt p = new DBEncrypt();
//            String address = p.dCode(applicationurl);
//            String returnUrl = address+NUONUOEnum.RETURN_URL.getCode()+"?fzjcid="+map.get("fzjcid")+"&wxid="+hzxxDto.getWxid();
//            String callbackUrl = address+NUONUOEnum.CALLBACK_URL.getCode()+"?fzjcid="+map.get("fzjcid");
                String content = "{\"serialNos\": [\""+fzjcxxDto1.getFphm()+"\"]}";
                String url = NUONUOEnum.URL.getCode();  // SDK请求地址
                String senid = UUID.randomUUID().toString().replace("-", ""); // 唯一标识，32位随机码，无需修改，保持默认即可
                String invoiceUrl = sdk.sendPostSyncRequest(url, senid, appKey, appSecret, accessToken, taxnum, method, content);
                log.error("诺诺发票查询信息"+invoiceUrl);
                map.put("invoiceUrl",invoiceUrl);
            }else{
                map.put("status","imgurl");
                map.put("imgurl",fzjcxxDto1.getImgurl());
            }
        }else{
            map.put("status","fail");
            map.put("message","发生错误请稍后再试！");
        }
        return map;
    }

    /**
     * 新冠支付 发票信息保存
     * @param fzjcxxDto
     * @return
     */
    @RequestMapping(value="/updateInvoiceInfo")
    @ResponseBody
    public Map<String, Object> updateInvoiceInfo(FzjcxxDto fzjcxxDto){
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(fzjcxxDto.getFzjcid())){
            boolean success = fzjcxxService.update(fzjcxxDto);
            if (success){
                amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.FKBJ_MOD.getCode()+ JSONObject.toJSONString(fzjcxxDto));
            }
            map.put("status","success");
        }else{
            map.put("status","fail");
            map.put("message","发生错误请稍后再试！");
        }
        return map;
    }

    /**
     * 新冠支付 发票不足通知
     * @return
     */
    @RequestMapping(value="/invoiceFailNotice")
    @ResponseBody
    public Map<String, Object> invoiceFailNotice(){
        Map<String, Object> map = new HashMap<>();
        amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.KFP_FAIL.getCode()+"发票开具失败");
        map.put("status","success");
        return map;
    }

    /**
     * 新冠支付 支付信息保存
     * @param fzjcxxModel
     * @param data
     * @return
     */
    @RequestMapping(value="/payCallbackUrl")
    @ResponseBody
    public String payCallbackUrl(FzjcxxModel fzjcxxModel,String data){
        log.error("callbackurl:" + data);
        String result = "fail";
//        try {
//            Map<String, Object> key = rsaUtil.genKeyPair();
//            String publicKey = rsaUtil.getPublicKey(key);
//            if (StringUtil.isNotBlank(data)){
//                String json = rsaUtil.decryptByPublicKey(data,publicKey);
                JSONObject rejsonObject = JSONObject.parseObject(data);
                if (StringUtil.isNotBlank(fzjcxxModel.getFzjcid())){
                    fzjcxxModel.setFkbj("1");
                    fzjcxxModel.setSfje(rejsonObject.get("amount").toString());
                    fzjcxxModel.setPtorderno(rejsonObject.get("orderNo").toString());
                    //支付方式
                    fzjcxxModel.setZffs("诺诺"+rejsonObject.get("payType"));
                    boolean success = fzjcxxService.update(fzjcxxModel);
                    if (success){
                        result = "success";
                        amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.FKBJ_MOD.getCode()+ JSONObject.toJSONString(fzjcxxModel));
                    }
                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return result;
    }

    /**
     * 新冠支付 诺诺支付
     * @param fzjcid
     * @param wxid
     * @return
     */
    @RequestMapping(value="/nuonuoPay")
    public  ModelAndView nuonuoPay(@RequestParam(name = "fzjcid", required = false) String fzjcid,@RequestParam(name = "wxid", required = false) String wxid,@RequestParam(name = "orderno", required = false) String orderno){
//        if (StringUtil.isNotBlank(fzjcid)){
//            FzjcxxModel fzjcxxModel = new FzjcxxModel();
//            fzjcxxModel.setFzjcid(fzjcid);
//            fzjcxxModel.setFkbj("1");
//            Boolean success = fzjcxxService.update(fzjcxxModel);
//            if (success){
//                amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.FKBJ_MOD.getCode()+ JSONObject.toJSONString(fzjcxxModel));
//            }
//        }
        ModelAndView mav = new ModelAndView("wechat/detectionViewAndMod");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = sdf.format(new Date());
        mav.addObject("nowDate",nowDate);
        mav.addObject("wxid", wxid);
        mav.addObject("fzjcid", fzjcid);
        mav.addObject("orderno", orderno);
        XtszDto xtszDto = xtszService.selectById("xg_zfje");
        String fkje ="";
        if (null!=xtszDto){
            fkje  =  xtszDto.getSzz();//获取token 访问令牌
        }
        mav.addObject("fkje", fkje);
        return mav;
    }

    /**
     * 新冠支付 查询诺诺订单信息
     * @param fzjcxxDto
     * @return
     */
    @RequestMapping(value="/nuonuoOrderInfo")
    @ResponseBody
    public Map<String, Object> nuonuoOrderInfo(FzjcxxDto fzjcxxDto){
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isBlank(fzjcxxDto.getOrderno())) {
            map.put("status", "fail");
            map.put("message", "未获取到支付信息！");
            return map;
        }
        NNOpenSDK sdk = NNOpenSDK.getIntance();
        String taxnum = NUONUOEnum.TAXNUM.getCode(); // 授权企业税号
        String appKey = NUONUOEnum.APPKEY.getCode();
        String appSecret = NUONUOEnum.APPSECRET.getCode();
        String method1 = NUONUOEnum.ORDER_METHOD.getCode(); // API方法名
        String method =null;
        try {
            method = new String( method1.getBytes("utf-8") , "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        XtszDto xtszDto = xtszService.selectById("access_token");
        String accessToken ="";
        if (null!=xtszDto){
            accessToken  =  xtszDto.getSzz();//获取token 访问令牌
        }
        String content = "{" +
                "\"customerOrderNo\": \""+fzjcxxDto.getOrderno()+"\","+
                "\"taxNo\": \""+taxnum+"\"" +  //税号
                "}";
        String url = NUONUOEnum.URL.getCode();  // SDK请求地址
        String senid = UUID.randomUUID().toString().replace("-", ""); // 唯一标识，32位随机码，无需修改，保持默认即可
        String result = sdk.sendPostSyncRequest(url, senid, appKey, appSecret, accessToken, taxnum, method, content);
        map.put("status","success");
        map.put("result",result);
        log.error("诺诺订单信息查询："+result);
        return map;
    }

    /**
     * 新冠支付 查询订单信息
     * @param fzjcxxDto
     * @return
     */
    @RequestMapping(value="/getInvoiceLinks")
    @ResponseBody
    public Map<String, Object> getInvoiceLinks(FzjcxxDto fzjcxxDto){
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isBlank(fzjcxxDto.getOrderno())) {
            map.put("status", "fail");
            map.put("message", "未获取到支付信息！");
            return map;
        }
        NNOpenSDK sdk = NNOpenSDK.getIntance();
        String taxnum = NUONUOEnum.TAXNUM.getCode(); // 授权企业税号
        String appKey = NUONUOEnum.APPKEY.getCode();
        String appSecret = NUONUOEnum.APPSECRET.getCode();
        String method1 = NUONUOEnum.INVOICE_LINK.getCode(); // API方法名
        String method =null;
        try {
            method = new String( method1.getBytes("utf-8") , "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        XtszDto xtszDto = xtszService.selectById("access_token");
        String accessToken ="";
        if (null!=xtszDto){
            accessToken  =  xtszDto.getSzz();//获取token 访问令牌
        }
        String content = "{" +
                "\"customerOrderNo\": \""+fzjcxxDto.getOrderno()+"\","+
                "\"taxNo\": \""+taxnum+"\"" +  //税号
                "}";
        String url = NUONUOEnum.URL.getCode();  // SDK请求地址
        String senid = UUID.randomUUID().toString().replace("-", ""); // 唯一标识，32位随机码，无需修改，保持默认即可
        String result = sdk.sendPostSyncRequest(url, senid, appKey, appSecret, accessToken, taxnum, method, content);
        log.error("诺诺WECHAT开票："+result);
        map.put("status","success");
        map.put("result",result);
        return map;
    }

    /**
     * 查看修改已预约的检测 页面
     * @param wxid
     * @param sj
     * @return
     */
    @RequestMapping(value="/detectionViewAndMod")
    public ModelAndView detectionViewAndMod(@RequestParam(name = "wxid", required = false) String wxid,@RequestParam(name = "sj", required = false) String sj){
        ModelAndView mav = new ModelAndView("wechat/detectionViewAndMod");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = sdf.format(new Date());
        mav.addObject("nowDate",nowDate);
        mav.addObject("wxid", wxid);
        mav.addObject("fzjcid", "");
        mav.addObject("orderno", "");
        mav.addObject("ywlx", BusinessTypeEnum.XG.getCode());
        XtszDto xtszDto = xtszService.selectById("xg_zfje");
        String fkje ="";
        if (null!=xtszDto){
            fkje  =  xtszDto.getSzz();//获取token 访问令牌
        }
        mav.addObject("fkje", fkje);
        return mav;
    }

    /**
     * 查看修改已预约的检测 数据
     * @param lshzxxDto
     * @return
     */
    @RequestMapping(value="/getDetectionViewAndModList")
    @ResponseBody
    public Map<String, Object> getDetectionViewAndModList(HzxxtDto lshzxxDto){
        Map<String, Object> map= new HashMap<>();
        if (StringUtil.isNotBlank(lshzxxDto.getJclx())){
            List<JcsjDto> jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
            if (jcxms!=null&&jcxms.size()>0){
                for (JcsjDto jcsjDto:jcxms) {
                    if (lshzxxDto.getJclx().equals(jcsjDto.getCsdm())){
                        lshzxxDto.setJclx(jcsjDto.getCsid());
                    }
                }
            }
        }
        if (StringUtil.isNotBlank(lshzxxDto.getWxid())) {
            WxbdglDto wxbdglDto = wxbdglService.getDtoListByWxid(lshzxxDto.getWxid());//查询微信绑定的手机号
            if (wxbdglDto!=null) {
                lshzxxDto.setSj(wxbdglDto.getSjh());//将手机号存入lshzxxDto
            }
            List<HzxxtDto> hzxxList = hzxxService.getHzxxListByWxid(lshzxxDto);//查询患者信息手机为绑定的手机以及分子检测信息为该微信的数据
            for (HzxxtDto hzxxDto : hzxxList) {
                String jcxmmc = hzxxDto.getJcxmmc();
                String jcxmid = hzxxDto.getJcxmid();
                if(StringUtil.isNotBlank(jcxmmc)) {
                    hzxxDto.setJcxmmcs(jcxmmc.trim().split(","));
                }else {
                    List<JcsjDto> jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
                    String jcxmmcs = "";
                    if(StringUtil.isNotBlank(jcxmid)) {
                        String[] split = jcxmid.trim().split(",");
                        for (String xmid : split) {
                            for (JcsjDto jcxm : jcxms) {
                                if (xmid.equals(jcxm.getCsid())){
                                    jcxmmcs = jcxmmcs + "," + jcxm.getCsmc();
                                    break;
                                }
                            }
                        }
                        hzxxDto.setJcxmmc(jcxmmcs.substring(1));
                        hzxxDto.setJcxmids(jcxmid.trim().split(","));
                    }
                }
                String yyjcrq = hzxxDto.getYyjcrq();
                if (StringUtil.isNotBlank(yyjcrq)){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Calendar cal = Calendar.getInstance();
                    try {
                        simpleDateFormat.parse(yyjcrq);
                        cal.setTime(simpleDateFormat.parse(yyjcrq));
                        cal.add(Calendar.HOUR, 1);
                        yyjcrq = yyjcrq.substring(0,yyjcrq.length()-3) + "~" + simpleDateFormat.format(cal.getTime()).split(" ")[1];
                    } catch (ParseException e) {
                        log.error("预约检测日期转换失败！");
                    }
                hzxxDto.setYyjcrq(yyjcrq);
                }
                String zjh = hzxxDto.getZjh();
                if (StringUtil.isNotBlank(zjh)){
                    DBEncrypt dbEncrypt = new DBEncrypt();
                    zjh = dbEncrypt.dCode(zjh);
                    hzxxDto.setZjh(zjh);
                }
            }
            map.put("hzxxList",hzxxList);
        }
        map.put("status","fail");
        map.put("message","未获取到您的微信\n请重新至公众号打开网页");

        return map;
    }

    /**
     * 修改单个已预约的检测页面
     * @param wxid
     * @param hzxxDto
     * @return
     */
    @RequestMapping(value="/detectionMod")
    public ModelAndView detectionMod(@RequestParam(name = "wxid", required = false) String wxid,HzxxtDto hzxxDto){
        ModelAndView mav = new ModelAndView("wechat/detectionAppointment");
        DBEncrypt p = new DBEncrypt();
        if (StringUtil.isNotBlank(hzxxDto.getJclx())){
            List<JcsjDto> jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
            if (jcxms!=null&&jcxms.size()>0){
                for (JcsjDto jcsjDto:jcxms) {
                    if (hzxxDto.getJclx().equals(jcsjDto.getCsdm())){
                        hzxxDto.setJclx(jcsjDto.getCsid());
                    }
                }
            }
        }
        HzxxtDto hzxxDto_t = hzxxService.getHzxxDtoByHzid(hzxxDto);
        hzxxDto_t.setZjh(p.dCode(hzxxDto_t.getZjh()));
        hzxxDto_t.setSj(p.dCode(hzxxDto_t.getSj()));
        String yyjcrq = hzxxDto_t.getYyjcrq();
        if (StringUtil.isNotBlank(yyjcrq)){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar cal = Calendar.getInstance();
            try {
                yyjcrq = simpleDateFormat.format(simpleDateFormat.parse(yyjcrq));
                cal.setTime(simpleDateFormat.parse(yyjcrq));
                cal.add(Calendar.HOUR, 1);
                yyjcrq = yyjcrq + "~" + simpleDateFormat.format(cal.getTime()).split(" ")[1];
            } catch (ParseException e) {
                log.error("预约检测日期转换失败！");
            }
            hzxxDto_t.setYyjcrq(yyjcrq);
        }
        String jcxmid = hzxxDto_t.getJcxmid();
        JcsjDto jclx = new JcsjDto();
        List<JcsjDto> jclxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        for (JcsjDto lsjclx : jclxs) {
            if ("TYPE_COVID".equals(lsjclx.getCsdm())){
                jclx = lsjclx;
                break;
            }
        }
        List<JcsjDto> jcxmlist = new ArrayList<>();
        List<JcsjDto> jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
        for (JcsjDto jcxm : jcxms) {
            if (StringUtil.isNotBlank(jcxm.getFcsid())) {
                if (jcxm.getFcsid().equals(jclx.getCsid()) && "0".equals(jcxm.getScbj())) {
                    if ("1".equals(jcxm.getSfmr())) {
                        jcxm.setChecked("1");
                    }
                    jcxmlist.add(jcxm);
                }
            }
        }

        if(StringUtil.isNotBlank(jcxmid)) {
            hzxxDto_t.setJcxmids(jcxmid.trim().split(","));
            for (int i = 0;i<hzxxDto_t.getJcxmids().length;i++){
                if (jcxmlist!=null){
                    for (int j = 0; j < jcxmlist.size(); j++) {
                        if (jcxmlist.get(j).getCsid().equals(hzxxDto_t.getJcxmids()[i])){
                            jcxmlist.get(j).setChecked("1");
                        }
                    }
                }
            }
        }
        mav.addObject("jcxmlist",jcxmlist);
        mav.addObject("updateFlag","updateFlag");
        mav.addObject("hzxxDto",hzxxDto_t);
        mav.addObject("title", "修改预约");
        mav.addObject("wxid", wxid);
        //查询系统设置中预约时间段
        XtszDto appoinmentTimeStart = xtszService.selectById("appoinmentTimeStart");
        XtszDto appoinmentTimeEnd = xtszService.selectById("appoinmentTimeEnd");
        XtszDto appoinmentDateLength = xtszService.selectById("appoinmentDateLength");
        if (appoinmentDateLength!=null) {
            mav.addObject("appoinmentDateLength",appoinmentDateLength.getSzz());
        }else {
            mav.addObject("appoinmentDateLength","60");
        }
        mav.addObject("appoinmentTimeStart",appoinmentTimeStart.getSzz());
        mav.addObject("appoinmentTimeEnd",appoinmentTimeEnd.getSzz());
        return mav;
    }

    /**
     * 修改单个已预约的检测页面 小程序接口
     * @param wxid
     * @param hzxxDto
     * @return
     */
    @RequestMapping(value="/miniDetectionMod")
    @ResponseBody
    public Map<String, Object> miniDetectionMod(@RequestParam(name = "wxid", required = false) String wxid,HzxxtDto hzxxDto){
        Map<String, Object> map = new HashMap<>();
        DBEncrypt p = new DBEncrypt();
        HzxxtDto hzxxDto_t = hzxxService.getHzxxDtoByHzid(hzxxDto);
        hzxxDto_t.setZjh(p.dCode(hzxxDto_t.getZjh()));
        hzxxDto_t.setSj(p.dCode(hzxxDto_t.getSj()));
        //证件类型
        String zjlxjson = JSONArray.fromObject(redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.IDCARD_CATEGORY.getCode())).toString();
        //分子检测项目（删除标记为0且父参数id等于检测类型为TYPE_COVID的参数id）
        String jcxmid = hzxxDto_t.getJcxmid();
        if(StringUtil.isNotBlank(jcxmid)) {
            hzxxDto_t.setJcxmids(jcxmid.trim().split(","));
        }
        List<JcsjDto> jclxList = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        JcsjDto jclx = new JcsjDto();
        for (JcsjDto lsjclx : jclxList) {
            if ("TYPE_COVID".equals(lsjclx.getCsdm())){
                jclx = lsjclx;
            }
        }
        List<JcsjDto> lsjcxmlist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
        List<JcsjDto> jcxmlist = new ArrayList<>();
        for (JcsjDto jcxm : lsjcxmlist) {
            if ("0".equals(jcxm.getScbj()) && jcxm.getFcsid().equals(jclx.getCsid())){
                for (int i = 0; i < hzxxDto_t.getJcxmids().length; i++) {
                    if (jcxm.getCsid().equals(hzxxDto_t.getJcxmids()[i])){
                        jcxmlist.get(i).setChecked("1");
                    }
                }
                jcxmlist.add(jcxm);
            }
        }
        map.put("jcxmlist",jcxmlist);
        map.put("updateFlag","updateFlag");
        map.put("zjlxlist",zjlxjson.replaceAll("csmc","label").replaceAll("csid","value"));
        map.put("hzxxDto",hzxxDto_t);
        map.put("title", "修改预约");
        map.put("wxid", wxid);
        return map;
    }

    /**
     * 修改保存已预约的检测
     * @param hzxxDto
     * @return
     */
    @RequestMapping(value="/detectionAppointmentEdit")
    @ResponseBody
    public Map<String, Object> detectionAppointmentEdit(HzxxtDto hzxxDto){
        if (StringUtil.isNotBlank(hzxxDto.getWxid())) {
            if (StringUtil.isNotBlank(hzxxDto.getJclx())){
                List<JcsjDto> jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
                if (jcxms!=null&&jcxms.size()>0){
                    for (JcsjDto jcsjDto:jcxms) {
                        if (hzxxDto.getJclx().equals(jcsjDto.getCsdm())){
                            hzxxDto.setJclx(jcsjDto.getCsid());
                        }
                    }
                }
            }
            return hzxxService.detectionAppointmentMod(hzxxDto);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("status","fail");
        map.put("message","未获取到您的微信\n请重新至公众号打开网页");
        return map;

    }

    /**
     * 新冠病毒核酸检测报告查询
     * @param wxid
     * @param zjh
     * @param sj
     * @param request
     * @return
     */
    @RequestMapping(value="/detectionReport")
    public ModelAndView detectionReport(@RequestParam(name = "wxid", required = false) String wxid,@RequestParam(name = "zjh", required = false) String zjh,@RequestParam(name = "sj", required = false) String sj, HttpServletRequest request){
        ModelAndView mav;
        if(zjh!=null){
            mav=new ModelAndView("wechat/informed/detectionReportChoose");
            DBEncrypt dbEncrypt=new DBEncrypt();
            zjh=dbEncrypt.eCode(zjh);
            sj=dbEncrypt.eCode(sj);
            mav.addObject("flag",0);
            mav.addObject("zjh", zjh);
            mav.addObject("sj", sj);
            mav.addObject("request",request);
            return mav;
        }
        mav=new ModelAndView("wechat/informed/detectionReportChoose");
        mav.addObject("flag",1);
        mav.addObject("wxid", wxid);
        mav.addObject("request",request);
        return mav;
    }

    /**
     * 新冠病毒核酸检测报告查询页面 小程序接口
     * @param wxid
     * @param zjh
     * @param sj
     * @param request
     * @return
     */
    @RequestMapping(value="/miniDetectionReport")
    @ResponseBody
    public Map<String, Object> miniDetectionReport(@RequestParam(name = "wxid", required = false) String wxid,@RequestParam(name = "zjh", required = false) String zjh,@RequestParam(name = "sj", required = false) String sj, HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        if(zjh!=null){
            DBEncrypt dbEncrypt=new DBEncrypt();
            zjh=dbEncrypt.eCode(zjh);
            sj=dbEncrypt.eCode(sj);
            map.put("flag",0);
            map.put("zjh", zjh);
            map.put("sj", sj);
            map.put("request",request);
            return map;
        }
        map.put("flag",1);
        map.put("wxid", wxid);
        map.put("request",request);
        return map;
    }

    @RequestMapping(value="/getDetectionReport")
    @ResponseBody
    public Map<String, Object> getDetectionReport(FzjcxxDto fzjcxxDto){
        Map<String, Object> map= new HashMap<>();
        //设置检测类型为新冠
        List<JcsjDto> jclxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        String jclx = "";
        for (JcsjDto jcsj : jclxs) {
            if ("TYPE_COVID".equals(jcsj.getCsdm())){
                jclx = jcsj.getCsid();
            }
        }
        fzjcxxDto.setJclx(jclx);
        if(fzjcxxDto.getZjh()!=null&&fzjcxxDto.getZjh()!=""){
            HzxxtDto hzxxtDto=new HzxxtDto();
            DBEncrypt dbEncrypt = new DBEncrypt();
            hzxxtDto.setZjh(dbEncrypt.eCode(fzjcxxDto.getZjh()));
            hzxxtDto.setSj(dbEncrypt.eCode(fzjcxxDto.getSj()));
            hzxxtDto.setPageSize(fzjcxxDto.getPageSize());
            hzxxtDto.setPageNumber(fzjcxxDto.getPageNumber());
            hzxxtDto.setPageStart((fzjcxxDto.getPageNumber() -1)*fzjcxxDto.getPageSize());
            hzxxtDto.setJclx(jclx);
            List<FzjcxxDto> fzjcxxDtos = fzjcxxService.bgrqDetailsByZjh(hzxxtDto);
            map.put("fzjcxxDtos",fzjcxxDtos);
        }else{
            WxbdglDto wxbdglDto = wxbdglService.getDtoListByWxid(fzjcxxDto.getWxid());//查询微信绑定的手机号
            fzjcxxDto.setSj(wxbdglDto.getSjh());//将手机号存入fzjcxxDto
            List<FzjcxxDto> fzjcxxDtos = fzjcxxService.bgrqDetails(fzjcxxDto);
            for(FzjcxxDto dto:fzjcxxDtos){
                if(StringUtil.isNotBlank(dto.getFjid())){
                    DBEncrypt dbEncrypt=new DBEncrypt();
                    String sign = dbEncrypt.eCode(dto.getFjid());
                    dto.setSign(sign);
                }
            }
            map.put("fzjcxxDtos",fzjcxxDtos);
        }

        return map;
    }

    /**
     * 新冠病毒核酸检测更多报告查询
     * @param request
     * @return
     */
    @RequestMapping(value="/moreReport")
    public ModelAndView moreReport(HttpServletRequest request){
        ModelAndView mav=new ModelAndView("wechat/informed/detectionReportByID");
        mav.addObject("request",request);
        return mav;
    }

    /**
     * 新冠病毒核酸检测更多报告查询 小程序接口
     * @return
     */
    @RequestMapping(value="/miniMoreReport")
    @ResponseBody
    public Map<String, Object> miniMoreReport(){
        Map<String, Object> map= new HashMap<>();
        List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.IDCARD_CATEGORY.getCode());
        map.put("dtos",jcsjDtos);
        return map;
    }

    /**
     * 新冠病毒核酸检测报告显示
     * @param fzjcxxDto
     * @return
     */
    @RequestMapping(value="/viewReport")
    public ModelAndView viewReport(FzjcxxDto fzjcxxDto){
        ModelAndView mav= new ModelAndView("wechat/informed/detectionReport");
        mav.addObject("fzjcxxDto",fzjcxxDto);
        return mav;
    }

    /**
     * 新冠病毒核酸检测报告显示 小程序接口
     * @param fzjcxxDto
     * @return
     */
    @RequestMapping(value="/miniViewReport")
    @ResponseBody
    public Map<String, Object> miniViewReport(FzjcxxDto fzjcxxDto){
        Map<String, Object> map= new HashMap<>();
        map.put("fzjcxxDto",fzjcxxDto);
        return map;
    }

    /**
     * 获取报告详情
     * @param
     * @return
     */
    @RequestMapping("/getReportDetails")
    public @ResponseBody Map<String, Object> getReportDetails(FzjcxxDto fzjcxxDto){
        Map<String,Object> map= new HashMap<>();
        List<FzjcxxDto> fzjcxxDtos = fzjcxxService.viewCovidDetails(fzjcxxDto);
        List<FjcfbDto> fjcfbDtoList = fjcfbService.selectFjcfbDtoByYwid(fzjcxxDto.getFzjcid());
        FjcfbDto fjcfbDto =new FjcfbDto();
        if(fjcfbDtoList!=null&&fjcfbDtoList.size()>0){
            fjcfbDto = fjcfbDtoList.get(0);
            DBEncrypt dbEncrypt=new DBEncrypt();
            String sign = dbEncrypt.eCode(fjcfbDto.getFjid());
            fjcfbDto.setSign(sign);
        }
        map.put("fzjcxxDtos",fzjcxxDtos);
        map.put("fzjcxxDto",fzjcxxDtos.get(0));
        map.put("fjcfbDto",fjcfbDto);
        return map;
    }

    /**
     * 新冠报告发送短信验证码
     * @param hzxxDto
     * @return
     */
    @RequestMapping("/sendCode")
    public @ResponseBody Map<String, Object> sendCode(HzxxtDto hzxxDto){
        Map<String,Object> map= new HashMap<>();
        DBEncrypt dbEncrypt=new DBEncrypt();
        hzxxDto.setSj(dbEncrypt.eCode(hzxxDto.getSj()));
        hzxxDto.setZjh(dbEncrypt.eCode(hzxxDto.getZjh()));
        List<HzxxtDto> dtos = hzxxService.getDtoListBySj(hzxxDto.getSj());
        if(dtos!=null&&dtos.size()>0){
            boolean isSuccess = hzxxService.sendCode(hzxxDto);
            map.put("status", isSuccess?"success":"fail");
            map.put("message", isSuccess?"发送成功":"发送失败");
            return map;
        }else{
            map.put("status","fail");
            map.put("message", "此手机号并未登记预约");
            return map;
        }
    }

    /**
     * 新冠报告验证短信验证码
     * @param hzxxDto
     * @return
     */
    @RequestMapping("/checkCode")
    public @ResponseBody Map<String, Object> checkCode(HzxxtDto hzxxDto){
        Map<String,Object> map= new HashMap<>();
        DBEncrypt dbEncrypt=new DBEncrypt();
        hzxxDto.setSj(dbEncrypt.eCode(hzxxDto.getSj()));
        hzxxDto.setZjh(dbEncrypt.eCode(hzxxDto.getZjh()));
        HzxxtDto hzxxDto_t = hzxxService.getCode(hzxxDto);
        Date date = new Date();
        long now = date.getTime();
        Date fssj = hzxxDto_t.getFssj();
        long time = fssj.getTime();
        if(now-time>300000){
            map.put("status","fail");
            map.put("message", "验证码已过期，请重新获取");
            return map;
        }else{
            if(hzxxDto_t.getYzm().equals(hzxxDto.getYzm())){
                map.put("status","success");
                map.put("message", "验证成功");
            }else {
                map.put("status","fail");
                map.put("message", "验证码错误");
            }
            return map;
        }
    }

    /**
     * 取消已预约的检测
     * @param hzxxDto
     * @return
     */
    @RequestMapping(value="/cancleAppointmentEdit")
    @ResponseBody
    public Map<String, Object> cancleAppointmentEdit(HzxxtDto hzxxDto){
        Map<String, Object> map = new HashMap<>();
        log.error("退款信息："+hzxxDto.getMes());
        if (StringUtil.isNotBlank(hzxxDto.getJclx())){
            List<JcsjDto> jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
            if (jcxms!=null&&jcxms.size()>0){
                for (JcsjDto jcsjDto:jcxms) {
                    if (hzxxDto.getJclx().equals(jcsjDto.getCsdm())){
                        hzxxDto.setJclx(jcsjDto.getCsid());
                    }
                }
            }
        }
        if (StringUtil.isNotBlank(hzxxDto.getWxid())) {
            PayinfoDto payinfoDto = new PayinfoDto();
            payinfoDto.setYwid(hzxxDto.getFzjcid());
            payinfoDto.setYwlx(BusinessTypeEnum.XG.getCode());
            payinfoDto.setJclx(hzxxDto.getJclx());
            List<PayinfoDto> list = payinfoService.selectPayOrders(payinfoDto);
            if (null != list && list.size()>0){
                for (int i = 0; i < list.size(); i++) {
                    try {
                        bankService.closeOrder(restTemplate, list.get(i));
                    } catch (BusinessException e) {
                        map.put("status","fail");
                        map.put("message",e.toString());
                        e.printStackTrace();
                    }
                }
            }
            return hzxxService.cancleAppointment(hzxxDto);
        }
        map.put("status","fail");
        map.put("message","未获取到您的微信\n请重新至公众号打开网页");
        return map;
    }

    /**
     * 新冠支付 生成退款订单
     * @param payinfoDto
     * @return
     */
    @RequestMapping(value="/payRefundApply")
    @ResponseBody
    public Map<String, Object> payRefundApply(PayinfoDto payinfoDto){
        Map<String,Object> map;
        payinfoDto.setLrry(payinfoDto.getWxid());
        payinfoDto.setYwlx(BusinessTypeEnum.XG.getCode());
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        String sign = commonService.getSign(payinfoDto.getZfid());
        payinfoDto.setSign(sign);
        paramMap.add("payinfoDto", payinfoDto);
        RestTemplate t_restTemplate = new RestTemplate();
        DBEncrypt p = new DBEncrypt();
        String address = p.dCode(applicationurl);
        String str = t_restTemplate.postForObject(address + "/wechat/pay/localRefundApply", paramMap, String.class);
        map = JSON.parseObject(str, HashMap.class);
        return map;
    }

    /**
     * 新冠支付 退款查询
     * @param payinfoDto
     * @return
     */
    @RequestMapping(value="/confirmPay")
    @ResponseBody
    public Map<String, Object> confirmPay(PayinfoDto payinfoDto){
        Map<String, Object> map = new HashMap<>();
        if (StringUtil.isNotBlank(payinfoDto.getJclx())){
            List<JcsjDto> jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
            if (jcxms!=null&&jcxms.size()>0){
                for (JcsjDto jcsjDto:jcxms) {
                    if (payinfoDto.getJclx().equals(jcsjDto.getCsdm())){
                        payinfoDto.setJclx(jcsjDto.getCsid());
                    }
                }
            }
        }
        FzjcxxDto fzjcxxDto = new FzjcxxDto();
        fzjcxxDto.setFzjcid(payinfoDto.getYwid());
        fzjcxxDto.setJclx(payinfoDto.getJclx());
        FzjcxxDto fzjcxxDto1 = fzjcxxService.getDto(fzjcxxDto);
        if (StringUtil.isNotBlank(fzjcxxDto1.getCjsj())){
            map.put("status","ycj");
            map.put("message","该条数据已采集\n不允许取消");
            return map;
        }
        payinfoDto.setDylx(PayTypeEnum.PAY.getCode());
        List<PayinfoDto> payinfoDtos = payinfoService.selectPayOrdersSuccess(payinfoDto);
        List<PayinfoDto> payinfoList = payinfoService.getIncompOrders(payinfoDto);
        if(payinfoDtos != null && payinfoDtos.size() > 0) {
            for(PayinfoDto payinfoDto1:payinfoList){
                PayinfoDto payinfoDto2 = new PayinfoDto();
                payinfoDto2.setJg("2");
                payinfoDto2.setZfid(payinfoDto1.getZfid());
                payinfoService.callbackInfo(payinfoDto2);
            }
            map.put("status","success");
            map.put("payinfoDto",payinfoDtos.get(0));
            return map;
        }else {
            if(payinfoList != null && payinfoList.size() > 0) {
                map.put("status","fail");
                map.put("message","正在确认支付状态");
                return map;
            }else{
                map.put("status","cancel");
                map.put("message","无需退款直接取消");
            }
        }
        return map;
    }

    /**
     * 根据省份去查城市
     * @param jcsjDto
     * @return
     */
    @RequestMapping(value="/getCtiysByProvince")
    @ResponseBody
    public List<JcsjDto> getCtiysByProvince(JcsjDto jcsjDto){
        jcsjDto.setJclb(BasicDataTypeEnum.CITY.getCode());
        return jcsjService.getJcsjDtoList(jcsjDto);
    }

    /**
     * 查询省份
     * @return
     */
    @RequestMapping(value="/getProvince")
    @ResponseBody
    public List<JcsjDto> getProvince(){
        return redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.PROVINCE.getCode());
    }

    /**
     * 查询采样点
     * @return
     */
    @RequestMapping(value="/getCollectSamples")
    @ResponseBody
    public List<JcsjDto> getCollectSamples(){
        return redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
    }

    /**
     * 查询证件类型
     * @return
     */
    @RequestMapping(value="/getIdcardCategory")
    @ResponseBody
    public List<JcsjDto> getIdcardCategory(){
        return redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.IDCARD_CATEGORY.getCode());
    }

    /**
     * 绑定手机发送短信验证码
     * @param sjh
     * @return
     */
    @RequestMapping("/sendMessageCode")
    @ResponseBody
    public Map<String, Object> sendMessageCode(String wxid, String sjh){
        if (wxid!=null && wxid != ""){
            return hzxxService.sendMessageCode(sjh);
        }else {
            Map<String, Object> map = new HashMap<>();
            map.put("status","fail");
            map.put("message","发送失败！");
            return map;
        }
    }

    /**
     * 绑定手机验证短信验证码
     * @param wxid
     * @param sjh
     * @param yzm
     * @return
     */
    @RequestMapping("/checkMessageCode")
    @ResponseBody
    public Map<String, Object> checkMessageCode(String wxid,String sjh,String yzm){
        Map<String, Object> map;
        map = hzxxService.checkMessageCode(sjh, yzm,"300000");
        if ("fail".equals(map.get("status"))){
            return map;
        }
        map = wxbdglService.phoneBinding(wxid, sjh);
        return map;
    }

    /**
     * 查询已被禁用的日期
     * @param bkyyrqDto
     * @return
     */
    @RequestMapping("/detection/getUnAppDate")
    @ResponseBody
    public Map<String,Object> getUnAppDate(BkyyrqDto bkyyrqDto) {
        Map<String, Object> map=new HashMap<>();
        XtszDto appoinmentDateLength = xtszService.selectById("appoinmentDateLength");
        int length = 60;
        if (appoinmentDateLength!=null) {
            length = Integer.parseInt(appoinmentDateLength.getSzz());
        }//可预约日期长度
        Date startDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String start = simpleDateFormat.format(startDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DATE,length);
        String end = simpleDateFormat.format(cal.getTime());
        bkyyrqDto.setBkyyrqstart(start);
        bkyyrqDto.setBkyyrqend(end);
        List<BkyyrqDto> bkyyrqDtos = bkyyrqService.getUnAppDate(bkyyrqDto);
        map.put("bkyyrqList",bkyyrqDtos);
        //查询系统设置中预约时间段
        XtszDto appoinmentTimeStart = xtszService.selectById("appoinmentTimeStart");
        XtszDto appoinmentTimeEnd = xtszService.selectById("appoinmentTimeEnd");
        map.put("appoinmentTimeStart",appoinmentTimeStart.getSzz());
        map.put("appoinmentTimeEnd",appoinmentTimeEnd.getSzz());
        return map;
    }

    /**
     * 新冠病毒核酸检测报告显示
     * @param request
     * @return
     */
    @RequestMapping(value="/qrcode")
    public ModelAndView qrcodeInfo(HttpServletRequest request){
        ModelAndView mav= new ModelAndView("wechat/detQrcodePage");
        String fzjcid = request.getParameter("id");
        if (StringUtil.isNotBlank(fzjcid) && 32==fzjcid.length()){
            mav.addObject("id",fzjcid);
            FzjcxxDto fzjcxxDto = new FzjcxxDto();
            fzjcxxDto.setFzjcid(fzjcid);
            List<FzjcxxDto> fzjcxxDtos = fzjcxxService.viewCovidDetails(fzjcxxDto);
            if (fzjcxxDtos != null && fzjcxxDtos.size()==1){
                FzjcxxDto fzjcxxDto_t = fzjcxxDtos.get(0);
                if (!"0".equals(fzjcxxDto_t.getScbj())){
                    mav.addObject("pageFlag","erro");
                    mav.addObject("erroText","该预约已取消");
                }else {
                    String yyjcrq;
                    if (StringUtil.isNotBlank(fzjcxxDto_t.getYyjcrq()) && fzjcxxDto_t.getYyjcrq().length()>10){
                        yyjcrq = fzjcxxDto_t.getYyjcrq().substring(0,10);
                    }else {
                        Date date = new Date();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        yyjcrq = simpleDateFormat.format(date);
                    }
                    fzjcxxDto_t.setYyjcrq(yyjcrq);
                    List<FjcfbDto> fjcfbDtoList = fjcfbService.selectFjcfbDtoByYwid(fzjcxxDto.getFzjcid());
                    if (fjcfbDtoList != null && fjcfbDtoList.size()>0){
                        mav.addObject("fjid",fjcfbDtoList.get(0).getFjid());
                    }
                    mav.addObject("fzjcxx",fzjcxxDto_t);
                }
            }else {
                mav.addObject("pageFlag","erro");
                mav.addObject("erroText","您打开的地址有误！");
            }
        }else {
            mav.addObject("pageFlag","erro");
            mav.addObject("erroText","您打开的地址有误！");
        }
        return mav;
    }

    /**
     * 微信扫二维码跳转公众号页面获取wxid
     * @param code
     * @param state
     * @param request
     * @return
     */
    @RequestMapping("/toCovidAddPage")
    public ModelAndView toCovidAddPage(@RequestParam(name = "code", required = false) String code,@RequestParam(name = "state", required = false) String state, HttpServletRequest request){
        ModelAndView mav=new ModelAndView("wechat/detectionAppointment");
        String wbcxdm = request.getParameter("wbcxdm");
        if(StringUtil.isBlank(wbcxdm)){
            wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
        }
        FzjcxxDto fzjcxxDto=new FzjcxxDto();
        String organ = request.getParameter("organ");
        log.error("organ--"+organ);
        if(StringUtil.isNotBlank(organ)){
            fzjcxxDto.setWxid(organ);
        }else{
            WeChatUserModel userModel = weChatService.getReportListPageByUserAuth(code,state, wbcxdm);
            if(userModel != null) {
                fzjcxxDto.setWxid(userModel.getOpenid());
                log.error("wxid--"+userModel.getOpenid());
            }
        }
        mav.addObject("wxid",fzjcxxDto.getWxid());
        log.error("新冠预约界面获取wxid----"+fzjcxxDto.getWxid());
        DBEncrypt p = new DBEncrypt();
        JcsjDto jclx = new JcsjDto();
        List<JcsjDto> jclxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
        for (JcsjDto lsjclx : jclxs) {
            if ("TYPE_COVID".equals(lsjclx.getCsdm())){
                jclx = lsjclx;
                break;
            }
        }
        List<JcsjDto> jcxmlist = new ArrayList<>();
        List<JcsjDto> jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_ITEM.getCode());
        for (JcsjDto jcxm : jcxms) {
            if (jcxm.getFcsid().equals(jclx.getCsid()) && "0".equals(jcxm.getScbj())){
                if ("1".equals(jcxm.getSfmr())){
                    jcxm.setChecked("1");
                }
                jcxmlist.add(jcxm);
            }
        }
        mav.addObject("jcxmlist",jcxmlist);
        HzxxtDto hzxxDto = new HzxxtDto();
        hzxxDto.setWxid(fzjcxxDto.getWxid());
        HzxxtDto hzxxtDto = hzxxService.getLastHzxxByWxid(hzxxDto);
        if (hzxxtDto!=null){
            hzxxDto.setXm(hzxxtDto.getXm());
            hzxxDto.setHzid(hzxxtDto.getHzid());
            hzxxDto.setZjh(p.dCode(hzxxtDto.getZjh()));
            hzxxDto.setSj(p.dCode(hzxxtDto.getSj()));
            hzxxDto.setZjlx(hzxxtDto.getZjlx());
            hzxxDto.setXxdz(hzxxtDto.getXxdz());
            hzxxDto.setSf(hzxxtDto.getSf());
            hzxxDto.setCs(hzxxtDto.getCs());
            hzxxDto.setCyd(hzxxtDto.getCyd());
        } else {
            List<JcsjDto> collect_samples = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
            for (JcsjDto collect_sample : collect_samples) {
                if ("1".equals(collect_sample.getSfmr())){
                    hzxxDto.setCyd(collect_sample.getCsid());
                }
            }
        }
        mav.addObject("title", "新冠病毒核酸检测预约");
        mav.addObject("hzxxDto", hzxxDto);
        mav.addObject("request",request);
        mav.addObject("ywlx", BusinessTypeEnum.XG.getCode());
        XtszDto xtszDto = xtszService.selectById("xg_zfje");
        String fkje ="";
        if (null!=xtszDto){
            fkje  =  xtszDto.getSzz();//获取token 访问令牌
        }
        mav.addObject("fkje", fkje);
        //查询系统设置中预约时间段
        XtszDto appoinmentTimeStart = xtszService.selectById("appoinmentTimeStart");
        XtszDto appoinmentTimeEnd = xtszService.selectById("appoinmentTimeEnd");
        XtszDto appoinmentDateLength = xtszService.selectById("appoinmentDateLength");
        if (appoinmentDateLength!=null) {
            mav.addObject("appoinmentDateLength",appoinmentDateLength.getSzz());
        }else {
            mav.addObject("appoinmentDateLength","60");
        }
        mav.addObject("appoinmentTimeStart",appoinmentTimeStart.getSzz());
        mav.addObject("appoinmentTimeEnd",appoinmentTimeEnd.getSzz());
        return mav;
    }
}
