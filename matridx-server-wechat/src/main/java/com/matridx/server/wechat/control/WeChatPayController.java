package com.matridx.server.wechat.control;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.enums.ProgramCodeEnum;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.service.svcinterface.IWbcxService;
import com.matridx.server.wechat.service.svcinterface.IWeChatPayService;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/wechat")
public class WeChatPayController extends BaseController {

	@Autowired
	IWeChatPayService weChatPayService;
	@Autowired
	IWbcxService wbcxService;
	
	final Logger log = LoggerFactory.getLogger(WeChatPayController.class);
	
	/**
	 * 跳转微信支付
	 * @param sjxxDto
	 * @param code
	 * @return
	 */
	@RequestMapping(value="/pay/wxPay")
	@ResponseBody
	public String wxPay(SjxxDto sjxxDto, String code, HttpServletRequest request) {
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		try {
			//根据外部程序代码查询外部程序表信息
			WbcxDto wbcxDto = new WbcxDto();
			wbcxDto.setWbcxdm(wbcxdm);
			wbcxDto = wbcxService.getDto(wbcxDto);
			if(wbcxDto == null){
				log.error("未找到外部编码为 "+wbcxdm+" 的外部程序信息！");
				JSONObject result = new JSONObject();
				result.put("msg", "未找到外部编码为 "+wbcxdm+" 的外部程序信息！");
				return result.toString();
			}
			return weChatPayService.createWechatPayOrder(sjxxDto, code, wbcxDto, request);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 跳转微信支付(H5)
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/wxH5Pay")
	@ResponseBody
	public String wxH5Pay(SjxxDto sjxxDto, HttpServletRequest request) {
		String wbcxdm = request.getParameter("wbcxdm");
		if(StringUtil.isBlank(wbcxdm)){
			wbcxdm = ProgramCodeEnum.MDINSPECT.getCode();
		}
		try {
			//根据外部程序代码查询外部程序表信息
			WbcxDto wbcxDto = new WbcxDto();
			wbcxDto.setWbcxdm(wbcxdm);
			wbcxDto = wbcxService.getDto(wbcxDto);
			if(wbcxDto == null){
				log.error("未找到外部编码为 "+wbcxdm+" 的外部程序信息！");
				JSONObject result = new JSONObject();
				result.put("msg", "未找到外部编码为 "+wbcxdm+" 的外部程序信息！");
				return result.toString();
			}
			log.info("调用H5支付------controller");
			String formString = weChatPayService.createWechatH5PayOrder(sjxxDto, wbcxDto, request);
			log.info("调用H5支付返回值------controller："+formString);
			return formString;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 微信异步结果通知
	 * @param request
	 * @param response
	 */
	@RequestMapping("/pay/wxPayNotify")
	@ResponseBody
	public void wxPayNotify(HttpServletRequest request, HttpServletResponse response){
		weChatPayService.wxPayNotify(request, response);
	}
	
	/**
	 * 跳转微信支付成功页面
	 * @return
	 */
	@RequestMapping(value="/pay/wxPayComplete")
	public ModelAndView wxPayComplete() {
        return new ModelAndView("wechat/pay/pay_complete");
	}
	
	/**
	 * 跳转微信支付失败页面
	 * @return
	 */
	@RequestMapping(value="/pay/wxPayFaild")
	public ModelAndView wxPayFaild() {
        return new ModelAndView("wechat/pay/pay_faild");
	}
	
	/**
	 * 跳转默认授权返回页面
	 * @return
	 */
	@RequestMapping(value="/pay/wxCodeView")
	public ModelAndView wxCodeView(String code, String state) {
		ModelAndView mv = new ModelAndView("wechat/pay/pay_code");
		log.info("wxCodeView ------ controller code:"+code+" state:"+state);
		String[] split = state.split(",");
		if(split.length == 2){
			String ybbh = split[0];
			mv.addObject("ybbh", ybbh);
			String fkje = split[1];
			mv.addObject("fkje", fkje);
		}
		mv.addObject("code", code);
		mv.addObject("state", state);
		
		return mv;
	}
}
