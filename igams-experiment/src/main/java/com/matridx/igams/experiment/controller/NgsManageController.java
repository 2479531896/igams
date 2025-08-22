package com.matridx.igams.experiment.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.experiment.dao.entities.NgsglDto;
import com.matridx.igams.experiment.service.svcinterface.INgsglService;

@Controller
@RequestMapping("/experiment")
public class NgsManageController extends BaseController {
	@Autowired
	INgsglService iNgsglService;
	
	@Autowired
	IJcsjService jcsjService;

	/**
	 * NGS管理列表
	 */
	@RequestMapping("/ngs/pageListNgs")
	public ModelAndView pageListNgs() {
		ModelAndView mav = new ModelAndView("experiment/ngs/ngs_list");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECTION_UNIT});
		 mav.addObject("jcdwmclist",jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		 mav.addObject("sfcglist", iNgsglService.queryBySfcg());
		 return mav;
	}

	/**
	 * 获取NGS列表数据
	 */
	@RequestMapping("/ngs/pageGetListNgs")
	@ResponseBody
	public Map<String, Object> getPageListNgs(NgsglDto ngsglDto) {
		Map<String, Object> map = new HashMap<>();
		List<NgsglDto> ngsgldtos = iNgsglService.getPageNgsglDto(ngsglDto);
		map.put("total", ngsglDto.getTotalNumber());
		map.put("rows", ngsgldtos);
		return map;
	}

	/**
	 * 根据ngsid获取ngs详细信息
	 */
	  @RequestMapping("/ngs/viewCkxx")
	  public ModelAndView viewCkxx(NgsglDto ngsglDto){
		  ModelAndView mav = new ModelAndView("experiment/ngs/ngs_view");
			NgsglDto ngs = iNgsglService.getNgsByngsid(ngsglDto.getNgsid());
			String rzStr = ngs.getRz();//解決日志显示换行的问题
			String rzStrM = rzStr.replaceAll("\\r\\n", "</br>")
					.replaceAll("\\n", "</br>")
					.replaceAll("\\r", "</br>");
			ngs.setRz(rzStrM);
			mav.addObject("ngsglDto", ngs);		
			return mav;
	  }
}
