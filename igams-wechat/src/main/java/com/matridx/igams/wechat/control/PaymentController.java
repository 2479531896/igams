package com.matridx.igams.wechat.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.wechat.dao.entities.PayinfoDto;
import com.matridx.igams.wechat.service.svcinterface.IPayinfoService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("payment")
public class PaymentController extends BaseController {

	@Autowired
	private IPayinfoService payinfoService;
	
	/**
	 * 新增一个界面
	 * @return
	 */
	@RequestMapping("/payment/pageListPayment")
	public ModelAndView pageListDto() {
        return new ModelAndView("wechat/payment/payment_list");
	}
	
	/**
	 * 显示所有数据
	 * @param payinfoDto
	 * @return
	 */
	@RequestMapping("/payment/pageGetListPayment")
	@ResponseBody
	public Map<String, Object> getPagedPayment(PayinfoDto payinfoDto){
		Map<String,Object> map = new HashMap<>();
		List<PayinfoDto> payinfoList = payinfoService.getPagedDtoList(payinfoDto);
		for(PayinfoDto pDto:payinfoList) {
			if(StringUtil.isNotBlank(pDto.getJyje())) {
				BigDecimal jyje_yuan = new BigDecimal(pDto.getJyje());
				BigDecimal num_100 = new BigDecimal(100.00);
				jyje_yuan = jyje_yuan.divide(num_100);
				pDto.setJyje_yuan(jyje_yuan);
			}
		}
		map.put("total",payinfoDto.getTotalNumber());
		map.put("rows",payinfoList);
		return map;
	}
	
	/**
	 * 查看消息详情
	 */
	@RequestMapping(value="/payment/viewPayment")
	@ResponseBody
	public ModelAndView viewMessageList(PayinfoDto payinfoDto){
		ModelAndView mav=new ModelAndView("wechat/payment/payment_view");
		payinfoDto = payinfoService.getDtoPayment(payinfoDto);
		if(StringUtil.isNotBlank(payinfoDto.getJyje())) {
			BigDecimal jyje_yuan = new BigDecimal(payinfoDto.getJyje());
			BigDecimal num_100 = new BigDecimal(100.00);
			jyje_yuan = jyje_yuan.divide(num_100);
			payinfoDto.setJyje_yuan(jyje_yuan);
		}
		mav.addObject("payinfoDto", payinfoDto);
		return mav;
	}

	/**
	 * 查询总金额
	 * @param
	 * @return
	 */
	@RequestMapping("/payment/pagedataPayYuan")
	@ResponseBody
	public Map<String, Object> selectPayYuan(PayinfoDto payinfoDto){
		Map<String,Object> map = new HashMap<>();
		PayinfoDto payinfoDto1 = payinfoService.selectPayYuan(payinfoDto);
		if (null != payinfoDto1 && StringUtil.isNotBlank(payinfoDto1.getJyzje())){
			BigDecimal jyje_yuan = new BigDecimal(payinfoDto1.getJyzje());
			BigDecimal num_100 = new BigDecimal(100.00);
			jyje_yuan = jyje_yuan.divide(num_100);
			map.put("jyzje",jyje_yuan);
			map.put("status","success");
		}
		return map;
	}

}
