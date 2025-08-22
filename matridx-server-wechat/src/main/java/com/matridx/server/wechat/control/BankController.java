package com.matridx.server.wechat.control;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.matridx.igams.common.enums.ProgramCodeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.matridx.igams.common.util.HttpUtil;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.BusinessTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.server.wechat.dao.entities.PayinfoDto;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.enums.MQWechatTypeEnum;
import com.matridx.server.wechat.service.svcinterface.IBankService;
import com.matridx.server.wechat.service.svcinterface.IPayinfoService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/wechat")
public class BankController extends BaseController {
	
	@Autowired
	IBankService bankService;
	@Autowired
	ISjxxService sjxxService;
	@Autowired
	IXtszService xtszService;
	@Autowired
	ICommonService commonService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IPayinfoService	payinfoService;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	
	private Logger log = LoggerFactory.getLogger(BankController.class);
	
	/**
	 * 快捷支付页面（小程序、公众号均未使用）
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/pay/fastPayView")
	public ModelAndView fastPayView(SjxxDto sjxxDto) {
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_fastPay");
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("ywlx", BusinessTypeEnum.SJ.getCode());
		return mav;
	}
	
	/**
	 * 修改金额页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/payInspectEdit")
	public ModelAndView payInspectEdit(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("wechat/informed/wechat_payEdit");
		// scan：扫码支付，transfer：转账支付
		String flag = request.getParameter("flag");
		String sjid = request.getParameter("sjid");
		SjxxDto sjxxDto = new SjxxDto();
		if(StringUtil.isNotBlank(sjid)) {
			sjxxDto = sjxxService.getDtoById(sjid);
		}
		String wxid = request.getParameter("wxid");
		sjxxDto.setWxid(wxid);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("flag", flag);
		mav.addObject("ywlx", BusinessTypeEnum.SJ.getCode());
		return mav;
	}
	
	/**
	 * 获取送检信息
	 * @param sjxxDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pay/getInspectionInfo")
	public Map<String, Object> getInspectionInfo(SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		SjxxDto t_sjxxDto = sjxxService.getDto(sjxxDto, 2);
		map.put("status", t_sjxxDto!=null?"success":"fail");
		map.put("sjxxDto", t_sjxxDto);
		return map;
	}
	
	/**
	 * 支付宝引导页面
	 * @param payinfoDto
	 * @return
	 */
	@RequestMapping(value="/pay/alipayGuide")
	public ModelAndView alipayGuide(PayinfoDto payinfoDto) {
		ModelAndView mav = new ModelAndView("wechat/pay/pay_alipayGuide");
		mav.addObject("payinfoDto", payinfoDto);
		return mav;
	}
	
	/**
	 * 生成订单和银标码
	 * @param payinfoDto
	 */
	@ResponseBody
	@RequestMapping(value="/pay/createOrderQRCode")
	public Map<String, Object> createOrderQRCode(PayinfoDto payinfoDto) {
		Map<String, Object> map = new HashMap<>();
		// 生成银标码
		try {
			if(StringUtil.isNotBlank(payinfoDto.getWxid())) {
				payinfoDto.setLrry(payinfoDto.getWxid());
			}
			if(StringUtil.isNotBlank(payinfoDto.getDdid())) {
				payinfoDto.setLrry(payinfoDto.getDdid());
			}
			if(StringUtil.isBlank(payinfoDto.getWbcxdm())) {
				payinfoDto.setWbcxdm(ProgramCodeEnum.MDINSPECT.getCode());
			}
			String qrCode = bankService.createOrderQRCode(restTemplate, payinfoDto);
			if(StringUtil.isNotBlank(qrCode)) {
				// 查询系统设置
				XtszDto xtszDto = xtszService.selectById(GlobalString.PAY_RESULT_FLAG);
				if(xtszDto != null && "0".equals(xtszDto.getSzz())) {
					// 修改标记为1
					xtszDto.setSzz("1");
					xtszDto.setOldszlb(GlobalString.PAY_RESULT_FLAG);
					xtszService.update(xtszDto);
					log.error("createOrderQRCode 发送支付轮询rabbit！");
					// 发送rabbit开启结果轮询
					amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.SELECT_PAY_RESULT.getCode(), "1");
				}
				// 根据链接生成二维码
				String filePath = bankService.generateQRCode(qrCode);
				map.put("filePath", filePath);
				map.put("qrCode", qrCode);
				map.put("status", "success");
			}else {
				map.put("status", "fail");
				map.put("message", "未获取到qrCode信息！");
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
		}
		return map;
	}
	/**
	 * 生成二维码
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getQRCode")
	public String getQRCode(HttpServletRequest request ){
		String qrCode =  request.getParameter("qrCode");
		String filePath= "";
		try {
			filePath = bankService.getQRCode(qrCode);

		} catch (BusinessException e) {
			log.error(e.getMsgId());
		}
		return "/wechat/pay/getPicturePreview?path="+filePath;
	}
	
	/**
	 * 微信统一下单
	 * @param request
	 * @param payinfoDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pay/wechatPayOrder")
	public Map<String, Object> wechatPayOrder(HttpServletRequest request, PayinfoDto payinfoDto){
		Map<String, Object> map = new HashMap<>();
		String ipAddress = HttpUtil.getIpAddress(request);
		log.info("wechatPayOrder ipAddress:" + ipAddress);
		try {
			map = bankService.wechatPayOrder(restTemplate, payinfoDto, ipAddress);
			if("success".equals(map.get("status"))) {
				// 查询系统设置
				XtszDto xtszDto = xtszService.selectById(GlobalString.PAY_RESULT_FLAG);
				if(xtszDto != null && "0".equals(xtszDto.getSzz())) {
					// 修改标记为1
					xtszDto.setSzz("1");
					xtszDto.setOldszlb(GlobalString.PAY_RESULT_FLAG);
					xtszService.update(xtszDto);
					log.error("alipayNative 发送支付轮询rabbit！");
					// 发送rabbit开启结果轮询
					amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.SELECT_PAY_RESULT.getCode(), "1");
				}
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
		}
		return map;
	}
	
	/**
	 * 支付宝服务窗支付(暂不使用)
	 * @param payinfoDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pay/alipayService")
	public Map<String, Object> alipayService(PayinfoDto payinfoDto){
		Map<String, Object> map = new HashMap<>();
		try {
			map = bankService.alipayService(restTemplate, payinfoDto);
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
		}
		return map;
	}
	
	/**
	 * 支付宝native码
	 * @param payinfoDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pay/alipayNative")
	public Map<String, Object> alipayNative(PayinfoDto payinfoDto){
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(payinfoDto.getYwid()) && StringUtil.isNotBlank(payinfoDto.getYwlx())){
			if ("XG".equals(payinfoDto.getYwlx())){
				if (null != redisUtil.get("PAY_XG_"+payinfoDto.getYwid())){
					PayinfoDto payinfoDto1 = new PayinfoDto();
					payinfoDto1.setZfid(String.valueOf(redisUtil.get("PAY_XG_"+payinfoDto.getYwid())));
				 	payinfoDto1 = payinfoService.getDto(payinfoDto1);
					if (null != payinfoDto1 && StringUtil.isNotBlank(payinfoDto1.getJg()) && "1".equals(payinfoDto1.getJg())){
						map.put("status", "fail");
						map.put("message", "该订单已支付，请勿重复支付");
						return map;
					}else{
						try {
							bankService.payResultInquire(restTemplate, payinfoDto1);
							payinfoDto1 = payinfoService.getDto(payinfoDto1);
							if (null != payinfoDto1 && StringUtil.isNotBlank(payinfoDto1.getJg()) && "1".equals(payinfoDto1.getJg())) {
								map.put("status", "fail");
								map.put("message", "该订单已支付，请勿重复支付");
								return map;
							}
						} catch (BusinessException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		try {
			if(StringUtil.isNotBlank(payinfoDto.getWxid())) {
				payinfoDto.setLrry(payinfoDto.getWxid());
			}
			if(StringUtil.isNotBlank(payinfoDto.getDdid())) {
				payinfoDto.setLrry(payinfoDto.getDdid());
			}
			if(StringUtil.isBlank(payinfoDto.getWbcxdm())) {
				payinfoDto.setWbcxdm(ProgramCodeEnum.MDINSPECT.getCode());
			}
			map = bankService.alipayNative(restTemplate, payinfoDto);
			if("success".equals(map.get("status"))) {
				// 查询系统设置
				XtszDto xtszDto = xtszService.selectById(GlobalString.PAY_RESULT_FLAG);
				if(xtszDto != null && "0".equals(xtszDto.getSzz())) {
					// 修改标记为1
					xtszDto.setSzz("1");
					xtszDto.setOldszlb(GlobalString.PAY_RESULT_FLAG);
					xtszService.update(xtszDto);
					log.error("alipayNative 发送支付轮询rabbit！");
					// 发送rabbit开启结果轮询
					amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.SELECT_PAY_RESULT.getCode(), "1");
				}
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
		}
		return map;
	}
	
	/**
	 * 支付结果查询
	 * @param payinfoDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pay/payResultInquire")
	public Map<String, Object> payResultInquire(PayinfoDto payinfoDto) {
		// 只能查询已做交易的订单
		Map<String, Object> map = new HashMap<>();
		try {
			map = bankService.payResultInquire(restTemplate, payinfoDto);
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
		}
		return map;
	}
	
	/**
	 * 支付/退款结果通知
	 * @param requestBodyString
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pay/payResultNotice", method = RequestMethod.POST)
	public Map<String, String> payResultNotice(@RequestBody String requestBodyString) {
		// 只有成功支付的交易才会有支付结果通知
        return bankService.payResultNotice(restTemplate, requestBodyString);
	}
	
	/**
	 * 关闭订单
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pay/closeOrder")
	public Map<String, Object> closeOrder(PayinfoDto payinfoDto) {
		// 支付成功的交易不允许关单
		Map<String, Object> map = new HashMap<>();
		try {
			map = bankService.closeOrder(restTemplate, payinfoDto);
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("msg", e.getMsg());
		}
		return map;
	}
	
	/**
	 * 退款申请
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pay/refundApply")
	public Map<String, Object> refundApply(HttpServletRequest request, PayinfoDto payinfoDto) {
		Map<String, Object> map = new HashMap<>();
		try {
			boolean checkSign = commonService.checkSign(payinfoDto.getSign(), payinfoDto.getZfid(), request);
			if(checkSign) {
				PayinfoDto t_payinfoDto = payinfoService.getDto(payinfoDto);
				t_payinfoDto.setTkje(payinfoDto.getTkje());
				t_payinfoDto.setYwlx(payinfoDto.getYwlx());
				if(StringUtil.isBlank(t_payinfoDto.getYbbh())) {
					t_payinfoDto.setYbbh(payinfoDto.getYbbh());
				}
				map = bankService.refundApply(restTemplate, t_payinfoDto);
			}else {
				map.put("status", "fail");
				map.put("message", "签名校验失败！");
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
		}
		return map;
	}
	
	/**
	 * 管理端调用退款申请
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pay/localRefundApply")
	public Map<String, Object> localRefundApply(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			String s_jsonObject = request.getParameter("payinfoDto");
			log.error("退款申请接收信息：" + s_jsonObject);
			JSONObject jsonObject = (JSONObject) JSONObject.parse(s_jsonObject);
			PayinfoDto payinfoDto = JSONObject.toJavaObject((JSONObject) jsonObject, PayinfoDto.class);
			if(payinfoDto != null && StringUtil.isNotBlank(payinfoDto.getZfid())) {
				boolean checkSign = commonService.checkSign(payinfoDto.getSign(), payinfoDto.getZfid(), request);
				if(checkSign) {
					PayinfoDto t_payinfoDto = payinfoService.getDto(payinfoDto);
					t_payinfoDto.setTkje(payinfoDto.getTkje());
					t_payinfoDto.setYwlx(payinfoDto.getYwlx());
					if(StringUtil.isBlank(t_payinfoDto.getYbbh())) {
						t_payinfoDto.setYbbh(payinfoDto.getYbbh());
					}
					map = bankService.refundApply(restTemplate, t_payinfoDto);
				}else {
					map.put("status", "fail");
					map.put("message", "签名校验失败！");
					log.error("退款申请：签名校验失败！");
				}
			}else {
				map.put("status", "fail");
				map.put("message", "未接收到支付订单信息！");
				log.error("退款申请：未接收到支付订单信息！");
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
			log.error("退款申请：" +e.getMsg());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", "管理端调用退款申请异常！");
			log.error("退款申请：管理端调用退款申请异常！");
		}
		return map;
	}
	
	/**
	 * 退款结果查询
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pay/refundResultInquire")
	public Map<String, Object> refundResultInquire(PayinfoDto payinfoDto) {
		Map<String, Object> map = new HashMap<>();
		try {
			map = bankService.refundResultInquire(restTemplate, payinfoDto);
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("msg", e.getMsg());
		}
		return map;
	}
	
	/**
	 * 二维码预览
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/picturePreview")
	public ModelAndView picturePreview(HttpServletRequest request) {
		ModelAndView mav= new ModelAndView("wechat/pay/qrcode_view");
		String path = request.getParameter("path");
		String filepath = "/wechat/pay/getPicturePreview?path=" + path;
		mav.addObject("file",filepath);
		String fkje = request.getParameter("fkje");
		mav.addObject("fkje", fkje);
		String ybbh = request.getParameter("ybbh");
		mav.addObject("ybbh", ybbh);
		String ywid = request.getParameter("ywid");
		mav.addObject("ywid", ywid);
		String ywlx = request.getParameter("ywlx");
		mav.addObject("ywlx", ywlx);
		//String hzxm = request.getParameter("hzxm");
		SjxxDto sjxx = sjxxService.getDtoById(ywid);
		mav.addObject("hzxm", sjxx.getHzxm());
		return mav;
	}
	
	/**
	 * 二维码图片预览
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="/pay/getPicturePreview")
	public void getPicturePreview(HttpServletRequest request, HttpServletResponse response){
		String filePath = request.getParameter("path");
		try {
			filePath = URLDecoder.decode(filePath, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(StringUtil.isBlank(filePath)) {
            return;
        }
        
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null; //输出流
        try {
    		InputStream iStream = new FileInputStream(filePath);
            os = response.getOutputStream();
            bis = new BufferedInputStream(iStream);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                os.flush();
                i = bis.read(buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            bis.close();
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * 开启轮询(测试使用)
	 */
	@RequestMapping(value="/pay/openQuery")
	@ResponseBody
	public void openQuery() {
		amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.SELECT_PAY_RESULT.getCode(), "1");
	}

	/**
	 * 关闭订单
	 * @param payinfoDto
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/pay/closeOrders")
	public Map<String, Object> closeOrdersByYwid(PayinfoDto payinfoDto) {
		// 只能查询已做交易的订单
		return bankService.closeOrdersByYwid(payinfoDto);
	}
}
