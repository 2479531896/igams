package com.matridx.igams.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.YyxxDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum_pub;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.service.svcinterface.IYyxxService;

@Controller
@RequestMapping("/hospital")
public class HospitalController extends BaseController {
	@Autowired
	private IYyxxService yyxxService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	private IXxglService xxglservice;
	
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;

	/**
	 * 拜访对象列表
	 */
	@RequestMapping("/yyxx/pageListYyxx")
	public ModelAndView pageListBfdxglDto() {
		ModelAndView mav = new ModelAndView("hospital/hospital/hospital_list");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.VISITUNIT_TYPE, BasicDataTypeEnum.VISITUNIT_GRADE });
		mav.addObject("dwlblist", jclist.get(BasicDataTypeEnum.VISITUNIT_TYPE.getCode()));
		mav.addObject("dwdjlist", jclist.get(BasicDataTypeEnum.VISITUNIT_GRADE.getCode()));
		return mav;
	}

	/**
	 * 获取医院列表数据
	 */
	@RequestMapping("/yyxx/pageGetListYyxx")
	@ResponseBody
	public Map<String, Object> getPageListBfdxg(YyxxDto yyxxDto) {
		Map<String, Object> map = new HashMap<>();
		List<YyxxDto> yyxxDtos = yyxxService.getPageYyxxDtoList(yyxxDto);
		map.put("total", yyxxDto.getTotalNumber());
		map.put("rows", yyxxDtos);
		return map;
	}

	/**
	 * 获取医院列表数据(无访问权限)
	 */
	@RequestMapping("/yyxx/pagedataGetYyxxList")
	@ResponseBody
	public Map<String, Object> pagedataGetYyxxList(YyxxDto yyxxDto) {
		return this.getPageListBfdxg(yyxxDto);
	}

	/**
	 * 根据dwid获取详细信息
	 */
	@RequestMapping("/yyxx/viewYyxx")
	public ModelAndView viewHospital(YyxxDto yyxxDto) {
		ModelAndView mav = new ModelAndView("hospital/hospital/hospital_view");
		YyxxDto yyxxDtos = yyxxService.getDtoById(yyxxDto.getDwid());
		mav.addObject("yyxxDto", yyxxDtos);
		return mav;
	}

	/**
	 * 医院管理新增页面
	 */
	@RequestMapping("/yyxx/addYyxx")
	public ModelAndView addYyxx(YyxxDto yyxxDto) {
		ModelAndView mav = new ModelAndView("hospital/hospital/hospital_add");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[] {
				BasicDataTypeEnum.VISITUNIT_TYPE, BasicDataTypeEnum.VISITUNIT_GRADE, BasicDataTypeEnum.PROVINCE});
		mav.addObject("dwlblist", jclist.get(BasicDataTypeEnum.VISITUNIT_TYPE.getCode()));
		mav.addObject("dwdjlist", jclist.get(BasicDataTypeEnum.VISITUNIT_GRADE.getCode()));
		mav.addObject("sflist", jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));
		yyxxDto.setFormAction("addSaveYyxx");
		mav.addObject("yyxxDto", yyxxDto);
		return mav;
	}

	/**
	 * 医院新增保存
	 */
	@RequestMapping("/yyxx/addSaveYyxx")
	@ResponseBody
	public Map<String, Object> addSaveYyxx(YyxxDto yyxxDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		yyxxDto.setLrry(user.getYhid());
		List<YyxxDto> yyxxs = yyxxService.queryByDwmc(yyxxDto);
		if(yyxxs==null || yyxxs.isEmpty()) {
			boolean iSsuccess = yyxxService.insertDto(yyxxDto);
			amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum_pub.MOD_HOSPITAL.getCode(), JSONObject.toJSONString(yyxxDto));
			map.put("status", iSsuccess ? "success" : "fail");
			map.put("message", iSsuccess ? xxglservice.getModelById("ICOM00001").getXxnr()
					: xxglservice.getModelById("ICOM00002").getXxnr());
		}else {
			map.put("status", "fail");
			map.put("message", xxglservice.getModelById("ICOM99028").getXxnr());
		}
		return map;
	}

	/**
	 * 医院管理修改页面
	 */

	@RequestMapping("/yyxx/modYyxx")
	public ModelAndView modYyxx(YyxxDto yyxxDto) {
		ModelAndView mav = new ModelAndView("hospital/hospital/hospital_add");
		YyxxDto yyxxDto_t = yyxxService.getDtoById(yyxxDto.getDwid());
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[] {
				BasicDataTypeEnum.VISITUNIT_TYPE, BasicDataTypeEnum.VISITUNIT_GRADE, BasicDataTypeEnum.PROVINCE});
		mav.addObject("dwlblist", jclist.get(BasicDataTypeEnum.VISITUNIT_TYPE.getCode()));
		mav.addObject("dwdjlist", jclist.get(BasicDataTypeEnum.VISITUNIT_GRADE.getCode()));
		mav.addObject("sflist", jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(yyxxDto_t.getSf());
		List<JcsjDto> cslist = jcsjService.getJcsjDtoList(tJcsjDto);
		mav.addObject("cslist", cslist);
		yyxxDto_t.setFormAction("modSaveYyxx");
		mav.addObject("yyxxDto", yyxxDto_t);
		return mav;
	}

	/**
	 * 修改医院管理保存
	 */
	@RequestMapping("/yyxx/modSaveYyxx")
	@ResponseBody
	public Map<String, Object> modSaveYyxx(YyxxDto yyxxDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		yyxxDto.setXgry(user.getYhid());
		List<YyxxDto> yyxxs = yyxxService.queryByDwmc(yyxxDto);
		if (yyxxs==null || yyxxs.isEmpty()) {
			boolean iSsuccess = yyxxService.update(yyxxDto);
			amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum_pub.MOD_HOSPITAL.getCode(),
					JSONObject.toJSONString(yyxxDto));
			map.put("status", iSsuccess ? "success" : "fail");
			map.put("message", iSsuccess ? xxglservice.getModelById("ICOM00001").getXxnr()
					: xxglservice.getModelById("ICOM00002").getXxnr());
		}else {
			map.put("status", "fail");
			map.put("message", xxglservice.getModelById("ICOM99028").getXxnr());
		}
		return map;
	}
	
	   
    /**
     * 根据省份去查城市
     */
    @RequestMapping(value="/pagedataJscjcity")
	@ResponseBody
    public List<JcsjDto> jcsjcity(JcsjDto jcsjDto){
		 jcsjDto.setJclb(BasicDataTypeEnum.CITY.getCode());
		return jcsjService.getJcsjDtoList(jcsjDto);
    }
    
    
	/**
	 * 删除医院信息
	 */
	@RequestMapping(value="/yyxx/delYyxx")
	@ResponseBody
	public Map<String,Object> delyyxx(YyxxDto yyxxDto){
		User user = getLoginInfo();
		yyxxDto.setScry(user.getYhid());
		boolean isSuccess=yyxxService.delete(yyxxDto);
		amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum_pub.DEL_HOSPITAL.getCode(), JSONObject.toJSONString(yyxxDto));
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr());
		return map;
	}
    

}
