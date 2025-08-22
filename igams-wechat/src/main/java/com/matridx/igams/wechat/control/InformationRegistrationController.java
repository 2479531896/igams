package com.matridx.igams.wechat.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.XxdjDto;
import com.matridx.igams.wechat.dao.entities.XxpzDto;
import com.matridx.igams.wechat.service.svcinterface.IXxdjService;
import com.matridx.igams.wechat.service.svcinterface.IXxpzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wechat")
public class InformationRegistrationController extends BaseController{
	
	@Autowired
	IXxdjService xxdjService;
	@Autowired
	IXxpzService xxpzService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IJcsjService jcsjService;
	
	/**
	 * 跳转信息登记列表页面
	 * @return
	 */
	@RequestMapping(value="/information/pageListRegistration")
	public ModelAndView getInformationPage() {
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.INFORMATION_TYPE});		
		ModelAndView mav=new ModelAndView("wechat/information/informationregistration_list");
		mav.addObject("xxlxlist", jclist.get(BasicDataTypeEnum.INFORMATION_TYPE.getCode()));
		return mav;
	}
	
	/**
	 * 获取信息配置
	 * @param xxpzDto
	 * @return
	 */
	@RequestMapping(value="/information/pagedataXxpz")
	@ResponseBody
	public Map<String,Object> getXxpz(XxpzDto xxpzDto){
		List<XxpzDto> xxpzlist=xxpzService.getXxpzList(xxpzDto);//获取信息配置
		Map<String, Object> map= new HashMap<>();
		map.put("pzs",xxpzlist);
		return map;
	}
	/**
	 * 信息登记列表
	 * @param xxdjDto
	 * @return
	 */
	@RequestMapping(value="/information/pageGetListRegistration")
	@ResponseBody
	public Map<String,Object> getInformationDjPageList(XxdjDto xxdjDto){
		List<XxdjDto> wglist;
		if(xxdjDto.getXxlx()==null) {//若没有选择信息类型，列表数据为空
			wglist=null;
		}else{
			wglist=xxdjService.getPagedDtoList(xxdjDto);
		}
		Map<String, Object> map= new HashMap<>();
		map.put("total",xxdjDto.getTotalNumber());
		map.put("rows",wglist);
		return map;
	}
	
	/**
	 * 查看信息登记
	 * @param xxdjDto
	 * @return
	 */
	@RequestMapping(value="/information/viewinformation")
	public ModelAndView viewInformationDj(XxdjDto xxdjDto) {
		ModelAndView mav=new ModelAndView("wechat/information/informationregistration_view");
		XxpzDto xxpzDto=new XxpzDto();
		xxpzDto.setXxlx(xxdjDto.getXxlx());
		List<XxpzDto> xxpzlist=xxpzService.getXxpzList(xxpzDto);
		XxdjDto xxdj=xxdjService.getDtoById(xxdjDto.getXxid());
		mav.addObject("XxdjDto", xxdj);
		mav.addObject("Xxpzlist", xxpzlist);
		return mav;
	}
	
	/**
	 * 删除信息登记
	 * @param xxdjDto
	 * @return
	 */
	@RequestMapping(value="/information/delinformation")
	@ResponseBody
	public Map<String,Object> delInformationDj(XxdjDto xxdjDto){
		User user=getLoginInfo();
		xxdjDto.setScry(user.getYhid());
		boolean isSuccess=xxdjService.delete(xxdjDto);
		Map<String, Object> map = new HashMap<>();
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
}
