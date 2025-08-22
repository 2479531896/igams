package com.matridx.igams.experiment.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matridx.igams.experiment.service.svcinterface.ITqglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.PersonalSettingEnum;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.experiment.dao.entities.QclglDto;
import com.matridx.igams.experiment.dao.entities.QclmxDto;
import com.matridx.igams.experiment.service.svcinterface.IQclglService;
import com.matridx.igams.experiment.service.svcinterface.IQclmxService;

@Controller
@RequestMapping("/experiment")
public class CellCountingController extends BaseController {
	@Autowired
	private IQclglService qclglService;
	@Autowired
	private IXxglService xxglService;
	@Autowired
	private IQclmxService qclmxService;
	@Autowired
	private IGrszService grszService;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	private ITqglService tqglService;
	/**
	 * 跳转前处理列表页面
	 */
	@RequestMapping("/counting/pageListCounting")
	public ModelAndView pageListCounting() {
		return new ModelAndView("experiment/counting/counting_List");
	}
	
	/**
	 * 获取list数据
	 */
	@RequestMapping("/counting/pageGetListCounting")
	@ResponseBody
	public Map<String,Object> getPageList(QclglDto qclglDto){
		User user=getLoginInfo();
		List<QclglDto> qclList;
		List<Map<String,String>> jcdwList=tqglService.getJsjcdwByjsid(user.getDqjs());
		if(jcdwList!=null && jcdwList.size() > 0) {
			if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				List<String> strList= new ArrayList<>();
				for (Map<String, String> stringStringMap : jcdwList) {
					if (stringStringMap.get("jcdw") != null) {
						strList.add(stringStringMap.get("jcdw"));
					}
				}
				if(strList.size()>0) {
					qclglDto.setJcdwxz(strList);
					qclList=qclglService.getPagedDtoList(qclglDto);
				}else {
					qclList= new ArrayList<>();
				}
			}else {
				qclList=qclglService.getPagedDtoList(qclglDto);
			}
		}else {
			qclList=qclglService.getPagedDtoList(qclglDto);
		}
		Map<String,Object> map= new HashMap<>();
		map.put("total",qclglDto.getTotalNumber());
		map.put("rows", qclList);
		return map;
	}
	
	/**
	 * 查看页面
	 */
	@RequestMapping("/counting/viewCounting")
	public ModelAndView getViewCounting(QclglDto qclglDto) {
		ModelAndView mav=new ModelAndView("experiment/counting/counting_view");
		QclglDto qclglDto_t=qclglService.getDto(qclglDto);
		QclmxDto qclmxDto=new QclmxDto();
		qclmxDto.setQclid(qclglDto.getQclid());
		List<QclmxDto> qclmxDtos =qclmxService.getDtoList(qclmxDto);
		mav.addObject("qclglDto",qclglDto_t);
		mav.addObject("qclmxDtos",qclmxDtos);
		return mav;
	}
	
	/**
	 * 删除操作
	 */
	@RequestMapping("/counting/delCellcounting")
	@ResponseBody
	public Map<String,Object> delCellcounting(QclglDto qclglDto) {
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		qclglDto.setScry(user.getYhid());
		boolean result=qclglService.deleteQclgl(qclglDto);
		map.put("status", result ? "success" : "fail");
		map.put("message", result ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 细胞计数导入
	 */
	@RequestMapping("/counting/pageImportCounting")
	public ModelAndView importCounting() {
		ModelAndView mav=new ModelAndView("experiment/counting/counting_import");
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_CELLCOUNTING.getCode());
		//查询基础数据的检测单位
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECTION_UNIT});
		//查询个人设置的检测单位
		GrszDto grszDto=new GrszDto();
		User user=getLoginInfo();
		grszDto.setYhid(user.getYhid());
		grszDto.setSzlb(PersonalSettingEnum.TEST_PLACE.getCode());
		grszDto =grszService.selectGrszDtoByYhidAndSzlb(grszDto);
		if(grszDto==null) {
			grszDto=new GrszDto();
		}
		mav.addObject("detectionList", jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		mav.addObject("grszDto", grszDto);
		mav.addObject("fjcfbDto", fjcfbDto);
		return mav;
	}
}
