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

import com.matridx.igams.common.controller.BaseController;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.service.svcinterface.IAliService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.springboot.util.base.StringUtil;


@Controller
@RequestMapping("/wechat")
public class AliControl extends BaseController {
	@Autowired
	ISjxxService sjxxService;
	
	@Autowired
	private IAliService aliService;
	
    private Logger log = LoggerFactory.getLogger(AliControl.class);
	

	/**
	 * 跳转阿里支付页面
	 * @param sjxxDto
	 * @param response
	 */
	@RequestMapping(value="/pay/aliPay")
	public void aliPay(SjxxDto sjxxDto,HttpServletResponse response) {
		try {
			
			String formString = aliService.createAliPayOrder(sjxxDto);
			
			if(StringUtil.isNotBlank(formString)) {
				response.setContentType("text/html;charset=" + "utf8");
				
				response.getWriter().write(formString);//直接将完整的表单html输出到页面
				response.getWriter().flush();
				response.getWriter().close();
			}else {
				log.error("支付宝调用失败:" + formString);
				response.setContentType("text/html;charset=" + "utf8");
				
				response.getWriter().write("支付宝调用失败");
				response.getWriter().flush();
				response.getWriter().close();
			}
			//return mv;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 微信跳转阿里支付页面，需先到一个提示页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/pay/aliPaySkip")
	public ModelAndView aliPaySkip(SjxxDto sjxxDto) {
		ModelAndView mv = new ModelAndView("wechat/pay/pay");
		mv.addObject("ybbh",sjxxDto.getYbbh());
		return mv;
	}
	
	/**
	 * 阿里支付成功页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/aliPayComplete")
	public ModelAndView aliPayComplete(HttpServletRequest request) {
		try {
			ModelAndView mv = new ModelAndView("wechat/pay/pay_complete");
			aliService.aliPayComplete(request,mv);
			return mv;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 阿里支付通知页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/aliPayNotify")
	@ResponseBody
	public String aliPayNotify(HttpServletRequest request) {
		try {
			return aliService.aliPayNotify(request);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return "fail";
	}
	
	/**
	 * 阿里支付失败页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/pay/aliPayFaild")
	public ModelAndView aliPayFaild(HttpServletRequest request) {
		try {
			ModelAndView mv = new ModelAndView("wechat/pay/pay_faild");
			aliService.AliPayFaild(request);
			System.out.println("aliPayFaild");
			return mv;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 生成订单二维码(链接)
	 */
	@ResponseBody
	@RequestMapping(value="/pay/aliScanCodePay")
	public String aliScanCodePay() {
		// 生成订单二维码(链接)
		return aliService.createOrderQRCode();
	}
	
}
