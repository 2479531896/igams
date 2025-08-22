package com.matridx.igams.common.controller;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;

@Controller
@RequestMapping("/ddfbsgl")
public class DdfbsglController extends BaseController {
	@Autowired
	private IDdspglService ddspglService;
	
	@Autowired
	private IDdfbsglService ddfbsglService;
	
	@Autowired
	private IXxglService xxglService;
	
	@Autowired
	private IJcsjService jcsjService;
	
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	
	/**
	 * 增加一个钉钉消息审批的页面
	 */
	@RequestMapping("/ddfbsgl/pageListDdfbsgl")
	public ModelAndView pageDingspgl() {
		ModelAndView mav = new ModelAndView("common/ddfbsgl/ddfbsgl_list");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DINGTALK_AUDTI_CALLBACK_TYPE});
		mav.addObject("ddsplxlist", jclist.get(BasicDataTypeEnum.DINGTALK_AUDTI_CALLBACK_TYPE.getCode()));//钉钉审批回调类型
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 显示钉钉审批的展示列表
	 */
	@RequestMapping("/ddfbsgl/pageGetListDdfbsgl")
	@ResponseBody
	public Map<String, Object> getPageDingspglList(DdfbsglDto ddfbsglDto){
		Map<String,Object> map = new HashMap<>();
		List<DdfbsglDto> ddfbsgllist = ddfbsglService.getPagedDtoList(ddfbsglDto);
		map.put("rows",ddfbsgllist);
		map.put("total",ddfbsglDto.getTotalNumber());
		return map;
	}
	
	/**
	 *  查看消息的页面
	 */
	@RequestMapping("/ddfbsgl/viewDdfbs")
	@ResponseBody
	public ModelAndView showDingspgl(DdfbsglDto ddfbsglDto) {
		ModelAndView mav = new ModelAndView("common/ddfbsgl/ddfbsgl_view");
		ddfbsglDto = ddfbsglService.getDtoById(ddfbsglDto.getProcessinstanceid());
		DdspglDto ddspglDto=new DdspglDto();
		ddspglDto.setProcessinstanceid(ddfbsglDto.getProcessinstanceid());
		List<DdspglDto> ddspgllist=ddspglService.getDtoList(ddspglDto);
		mav.addObject("ddfbsglDto",ddfbsglDto);
		mav.addObject("ddspgllist", ddspgllist);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 执行钉钉审批回调
	 */
	@RequestMapping("/ddfbsgl/excuteAudit")
	@ResponseBody
	public Map<String,Object> excuteAudit(DdfbsglDto ddfbsglDto){
		Map<String,Object> map= new HashMap<>();
		boolean isSuccess;
		try {
			isSuccess = ddfbsglService.excuteAudit(ddfbsglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM99029").getXxnr() : xxglService.getModelById("ICOM99030").getXxnr());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			map.put("status", "fail");
			map.put("message", e.getMsg());
		}
		return map;
	}
}
