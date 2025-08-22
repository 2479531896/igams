package com.matridx.crf.web.controller;

import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;


import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;

import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.crf.web.dao.entities.BeanLdzxxFroms;
import com.matridx.crf.web.dao.entities.HzxxDto;
import com.matridx.crf.web.dao.entities.NdzxxjlDto;
import com.matridx.crf.web.dao.entities.NdzdmxqDto;
import com.matridx.crf.web.dao.entities.NdzshDto;
import com.matridx.crf.web.dao.entities.NdzxcgDto;
import com.matridx.crf.web.dao.entities.NdzyzzbDto;
import com.matridx.crf.web.service.svcinterface.IHzxxService;
import com.matridx.crf.web.service.svcinterface.INdzxxjlService;
import com.matridx.crf.web.service.svcinterface.INdzdmxqService;
import com.matridx.crf.web.service.svcinterface.INdzshService;
import com.matridx.crf.web.service.svcinterface.INdzxcgService;
import com.matridx.crf.web.service.svcinterface.INdzyzzbService;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/casereport")
public class HzxxController extends BaseController {

	@Autowired
	IHzxxService hzxxService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	INdzxxjlService iNdzxxjlService;
	@Autowired
	private IFjcfbService fjcfbService;
	@Autowired
	private INdzdmxqService ndzdmxqService;
	@Autowired
	private INdzshService ndzshService;
	@Autowired
	private INdzyzzbService ndzyzzbService;
	@Autowired
	private INdzxcgService ndzxcgService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;

	/**
	 * 患者信息列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/hzxx/pageListHzxx")
	public ModelAndView pageListHzxx() {
		ModelAndView mav = new ModelAndView("casereport/hzxx_list");
		Map<String, List<JcsjDto>> jcsjlist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.PATIENTCATEGORY,
						BasicDataTypeEnum.TREATMENTDEPARTMENT, BasicDataTypeEnum.DISCHARGESTATUS });
		mav.addObject("patientcategoryList", jcsjlist.get(BasicDataTypeEnum.PATIENTCATEGORY.getCode()));// 病人类别
		mav.addObject("departmentList", jcsjlist.get(BasicDataTypeEnum.TREATMENTDEPARTMENT.getCode()));// 就诊科室
		mav.addObject("stateList", jcsjlist.get(BasicDataTypeEnum.DISCHARGESTATUS.getCode()));// 出院状态
		return mav;
	}

	/**
	 * 患者信息列表
	 * 
	 * @return
	 */

	@RequestMapping(value = "/hzxx/listHzxx")
	@ResponseBody
	public Map<String, Object> listHzxx(HzxxDto hzxxDto) {
		// 添加角色判断
		hzxxDto.setDqjs(getLoginInfo().getDqjs());
		List<HzxxDto> t_List = (List<HzxxDto>) hzxxService.getPagedDtoList(hzxxDto);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", hzxxDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}

	@RequestMapping(value = "/hzxx/addHzxx")
	public ModelAndView addHzxx(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("casereport/hzxx_edit");
		if(StringUtil.isNotBlank(request.getParameter("isVal"))) {
			mav = new ModelAndView("casereport/hzxx_add");
		}
		HzxxDto t_hzxxDto = new HzxxDto();
		t_hzxxDto.setBgsj(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		List<FjcfbDto> fjcfbList=new ArrayList<>();
		mav.addObject("fjcfbList",fjcfbList);
		t_hzxxDto.setDqjs(getLoginInfo().getDqjs());
		t_hzxxDto.setFormAction("add");
		t_hzxxDto.setYwlx(BusTypeEnum.IMP_REPORT_HJYZ.getCode());
		mav.addObject("hzxxDto", t_hzxxDto);
		NdzxxjlDto ndzxxjlDtoOne = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoOne);
		NdzxxjlDto ndzxxjlDtoThree = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoThree);
		NdzxxjlDto ndzxxjlDtoFive = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoFive);
		NdzxxjlDto ndzxxjlDtoSeven = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoSeven);
		mav.addObject("ndzxxjlDtoOne", ndzxxjlDtoOne);
		mav.addObject("ndzxxjlDtoThree", ndzxxjlDtoThree);
		mav.addObject("ndzxxjlDtoFive", ndzxxjlDtoFive);
		mav.addObject("ndzxxjlDtoSeven", ndzxxjlDtoSeven);
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DISCHARGESTATUS, BasicDataTypeEnum.TREATMENTDEPARTMENT,
						BasicDataTypeEnum.PATIENTCATEGORY, BasicDataTypeEnum.COMPLICATION,
						BasicDataTypeEnum.ANTIBACTERIALHISTORY, BasicDataTypeEnum.SUBGROUP });
		mav.addObject("stateList", jclist.get(BasicDataTypeEnum.DISCHARGESTATUS.getCode()));
		mav.addObject("departmentList", jclist.get(BasicDataTypeEnum.TREATMENTDEPARTMENT.getCode()));
		mav.addObject("patientcategoryList", jclist.get(BasicDataTypeEnum.PATIENTCATEGORY.getCode()));
		mav.addObject("complicationList", jclist.get(BasicDataTypeEnum.COMPLICATION.getCode()));
		mav.addObject("hospitalList", hzxxService.getHospitailList(t_hzxxDto));
		mav.addObject("historyList", jclist.get(BasicDataTypeEnum.ANTIBACTERIALHISTORY.getCode()));
		mav.addObject("subgroupList", jclist.get(BasicDataTypeEnum.SUBGROUP.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	@RequestMapping(value = "/hzxx/addSaveHzxx")
	@ResponseBody
	public Map<String, Object> addSaveUser(HzxxDto hzxxDto, BeanLdzxxFroms beanLdzxxFroms, HttpServletRequest request){
		Map<String, Object> map = new HashMap<String, Object>();
		hzxxDto.setTjr(getLoginInfo().getYhid());
		boolean isSuccess = hzxxService.insertDto(beanLdzxxFroms, hzxxDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
				: xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	@RequestMapping(value = "/hzxx/modHzxx")
	public ModelAndView editHzxx(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("casereport/hzxx_edit");
		String hzid = request.getParameter("hzid").toString();
		HzxxDto hzxxDto = hzxxService.getDtoById(hzid);
		List<FjcfbDto> fjcfbList = hzxxService.getFjcfb(hzxxDto);
		if(fjcfbList.size()==0){
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			hzxxDto.setBgsj(simpleDateFormat.format(date));
		}
		mav.addObject("fjcfbList",fjcfbList);
		hzxxDto.setDqjs(getLoginInfo().getDqjs());
		List<NdzxxjlDto> list = iNdzxxjlService.getNdzByHzid(hzid);
		hzxxDto.setFormAction("mod");
		hzxxDto.setYwlx(BusTypeEnum.IMP_REPORT_HJYZ.getCode());
		NdzxxjlDto ndzxxjlDtoOne = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoOne);
		NdzxxjlDto ndzxxjlDtoThree = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoThree);
		NdzxxjlDto ndzxxjlDtoFive = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoFive);
		NdzxxjlDto ndzxxjlDtoSeven = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoSeven);
		FjcfbDto fjcfbDtoOne=null;
		FjcfbDto fjcfbDtoThree=null;
		FjcfbDto fjcfbDtoFive=null;
		FjcfbDto fjcfbDtoSeven=null;
		if (list != null && list.size() > 0) {
			for (NdzxxjlDto ldzxxjlDto : list) {
				if (ldzxxjlDto != null) {
					setNdzshow(ldzxxjlDto.getNdzjlid(),ldzxxjlDto);
					if (ldzxxjlDto.getJldjt().equals("1")) {
						ndzxxjlDtoOne = ldzxxjlDto;
						fjcfbDtoOne = hzxxService.getFjcfbByjlid(ndzxxjlDtoOne);
					} else if (ldzxxjlDto.getJldjt().equals("3")) {
						ndzxxjlDtoThree = ldzxxjlDto;
						fjcfbDtoThree = hzxxService.getFjcfbByjlid(ndzxxjlDtoThree);
					} else if (ldzxxjlDto.getJldjt().equals("5")) {
						ndzxxjlDtoFive = ldzxxjlDto;
						fjcfbDtoFive = hzxxService.getFjcfbByjlid(ndzxxjlDtoFive);
					} else if (ldzxxjlDto.getJldjt().equals("7")) {
						ndzxxjlDtoSeven = ldzxxjlDto;
						fjcfbDtoSeven = hzxxService.getFjcfbByjlid(ndzxxjlDtoSeven);
					}
				}
			}
		}
		mav.addObject("fjcfbDtoOne", fjcfbDtoOne);
		mav.addObject("fjcfbDtoThree", fjcfbDtoThree);
		mav.addObject("fjcfbDtoFive", fjcfbDtoFive);
		mav.addObject("fjcfbDtoSeven", fjcfbDtoSeven);
		mav.addObject("ndzxxjlDtoOne", ndzxxjlDtoOne);
		mav.addObject("ndzxxjlDtoThree", ndzxxjlDtoThree);
		mav.addObject("ndzxxjlDtoFive", ndzxxjlDtoFive);
		mav.addObject("ndzxxjlDtoSeven", ndzxxjlDtoSeven);
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DISCHARGESTATUS, BasicDataTypeEnum.TREATMENTDEPARTMENT,
						BasicDataTypeEnum.PATIENTCATEGORY, BasicDataTypeEnum.COMPLICATION,
						BasicDataTypeEnum.ANTIBACTERIALHISTORY, BasicDataTypeEnum.SUBGROUP });
		mav.addObject("stateList", jclist.get(BasicDataTypeEnum.DISCHARGESTATUS.getCode()));
		mav.addObject("departmentList", jclist.get(BasicDataTypeEnum.TREATMENTDEPARTMENT.getCode()));
		mav.addObject("patientcategoryList", jclist.get(BasicDataTypeEnum.PATIENTCATEGORY.getCode()));
		mav.addObject("complicationList", jclist.get(BasicDataTypeEnum.COMPLICATION.getCode()));
		mav.addObject("hospitalList", hzxxService.getHospitailList(hzxxDto));
		mav.addObject("historyList", jclist.get(BasicDataTypeEnum.ANTIBACTERIALHISTORY.getCode()));
		mav.addObject("subgroupList", jclist.get(BasicDataTypeEnum.SUBGROUP.getCode()));
		mav.addObject("hzxxDto", hzxxDto);

		if (hzxxDto.getJwhbz() != null && hzxxDto.getJwhbz().length() > 0) {
			List<String> strlist = Arrays.asList(hzxxDto.getJwhbz().split(","));
			mav.addObject("strlist", strlist);

		}
		if (hzxxDto.getGrbw() != null && hzxxDto.getGrbw().length() > 0) {
			List<String> grbwlist = Arrays.asList(hzxxDto.getGrbw().split(","));
			mav.addObject("grbwlist", grbwlist);

		}
		if (hzxxDto.getYz() != null && hzxxDto.getYz().length() > 0) {
			List<String> yzlist = Arrays.asList(hzxxDto.getYz().split(","));
			mav.addObject("yzlist", yzlist);

		}
		return mav;
	}

	@RequestMapping(value = "/hzxx/modSaveHzxx")
	@ResponseBody
	public Map<String, Object> modSaveHzxx(HzxxDto hzxxDto, BeanLdzxxFroms beanLdzxxFroms, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		hzxxDto.setXgr(getLoginInfo().getYhid());
		boolean isSuccess = hzxxService.updateDto(beanLdzxxFroms, hzxxDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
				: xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	@RequestMapping(value = "/hzxx/viewHzxx")
	public ModelAndView viewHzxx(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("casereport/hzxx_view");
		String hzid = request.getParameter("hzid").toString();
		HzxxDto hzxxDto = hzxxService.getDtoById(hzid);
		List<FjcfbDto> fjcfbList = hzxxService.getFjcfb(hzxxDto);
		mav.addObject("fjcfbList",fjcfbList);
		List<NdzxxjlDto> list = iNdzxxjlService.getNdzByHzid(hzid);
		NdzxxjlDto ndzxxjlDtoOne = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoOne);
		NdzxxjlDto ndzxxjlDtoThree = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoThree);
		NdzxxjlDto ndzxxjlDtoFive = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoFive);
		NdzxxjlDto ndzxxjlDtoSeven = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoSeven);
		FjcfbDto fjcfbDtoOne=null;
		FjcfbDto fjcfbDtoThree=null;
		FjcfbDto fjcfbDtoFive=null;
		FjcfbDto fjcfbDtoSeven=null;
		if (list != null && list.size() > 0) {
			for (NdzxxjlDto ldzxxjlDto : list) {
				if (ldzxxjlDto != null) {
					setNdzshow(ldzxxjlDto.getNdzjlid(),ldzxxjlDto);
					if (ldzxxjlDto.getJldjt().equals("1")) {
						ndzxxjlDtoOne = ldzxxjlDto;
						fjcfbDtoOne = hzxxService.getFjcfbByjlid(ndzxxjlDtoOne);
					} else if (ldzxxjlDto.getJldjt().equals("3")) {
						ndzxxjlDtoThree = ldzxxjlDto;
						fjcfbDtoThree = hzxxService.getFjcfbByjlid(ndzxxjlDtoThree);
					} else if (ldzxxjlDto.getJldjt().equals("5")) {
						ndzxxjlDtoFive = ldzxxjlDto;
						fjcfbDtoFive = hzxxService.getFjcfbByjlid(ndzxxjlDtoFive);
					} else if (ldzxxjlDto.getJldjt().equals("7")) {
						ndzxxjlDtoSeven = ldzxxjlDto;
						fjcfbDtoSeven = hzxxService.getFjcfbByjlid(ndzxxjlDtoSeven);
					}
				}
			}
		}
		mav.addObject("fjcfbDtoOne", fjcfbDtoOne);
		mav.addObject("fjcfbDtoThree", fjcfbDtoThree);
		mav.addObject("fjcfbDtoFive", fjcfbDtoFive);
		mav.addObject("fjcfbDtoSeven", fjcfbDtoSeven);
		mav.addObject("ndzxxjlDtoOne", ndzxxjlDtoOne);
		mav.addObject("ndzxxjlDtoThree", ndzxxjlDtoThree);
		mav.addObject("ndzxxjlDtoFive", ndzxxjlDtoFive);
		mav.addObject("ndzxxjlDtoSeven", ndzxxjlDtoSeven);
		if (hzxxDto.getJwhbz() != null) {
			JcsjDto jwhbsjsDto = new JcsjDto();
			jwhbsjsDto.setJclb(BasicDataTypeEnum.COMPLICATION.getCode());
			jwhbsjsDto.setIds(hzxxDto.getJwhbz());
			List<JcsjDto> jwhbzjcsjDtos = jcsjService.selectDetectionUnit(jwhbsjsDto);
			mav.addObject("jwhbzjcsjDtos", jwhbzjcsjDtos);

		}
		if (hzxxDto.getGrbw() != null) {
			JcsjDto grbwjsDto = new JcsjDto();
			grbwjsDto.setJclb(BasicDataTypeEnum.ANTIBACTERIALHISTORY.getCode());
			grbwjsDto.setIds(hzxxDto.getGrbw());
			List<JcsjDto> grbwjcsjDtos = jcsjService.selectDetectionUnit(grbwjsDto);
			mav.addObject("grbwjcsjDtos", grbwjcsjDtos);
		}
		if (hzxxDto.getYz() != null) {
			JcsjDto yzsDto = new JcsjDto();
			yzsDto.setJclb(BasicDataTypeEnum.SUBGROUP.getCode());
			yzsDto.setIds(hzxxDto.getYz());
			List<JcsjDto> yzjcsjDtos = jcsjService.selectDetectionUnit(yzsDto);
			mav.addObject("yzjcsjDtos", yzjcsjDtos);
		}
		// 获取名称
		hzxxDto.setJzks(getCsmc(hzxxDto.getJzks()));
		hzxxDto.setBrlb(getCsmc(hzxxDto.getBrlb()));
		hzxxDto.setSsyy(getCsmc(hzxxDto.getSsyy()));
		hzxxDto.setCicuzt(getCsmc(hzxxDto.getCicuzt()));
		hzxxDto.setCyzt(getCsmc(hzxxDto.getCyzt()));
		if (hzxxDto.getXb() != null) {
			if (hzxxDto.getXb().equals("0")) {
				hzxxDto.setXb("未知");
			} else if (hzxxDto.getXb().equals("1")) {
				hzxxDto.setXb("男");
			} else if (hzxxDto.getXb().equals("2")) {
				hzxxDto.setXb("女");

			}
		}
		if (hzxxDto.getKjywbls() != null) {
			if (hzxxDto.getKjywbls().equals("0")) {
				hzxxDto.setKjywbls("前30天");
			} else if (hzxxDto.getKjywbls().equals("1")) {
				hzxxDto.setKjywbls("前60天");
			} else if (hzxxDto.getKjywbls().equals("2")) {
				hzxxDto.setKjywbls("前90天");

			}
		}
		mav.addObject("hzxxDto", hzxxDto);
		return mav;
	}

	@RequestMapping(value = "/hzxx/delHzxx")
	@ResponseBody
	public Map<String, Object> delHzxx(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String hzid = request.getParameter("ids");
		boolean isSuccess = hzxxService.delHzxx(hzid, getLoginInfo().getYhid());
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
				: xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	@RequestMapping(value = "/hzxx/reportHzxx")
	public ModelAndView reportHzxx(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("casereport/hzxx_report");
		String hzid = request.getParameter("hzid").toString();
		HzxxDto hzxxDto = hzxxService.getDtoById(hzid);
		List<FjcfbDto> fjcfbList = hzxxService.getFjcfb(hzxxDto);
		if(fjcfbList.size()==0){
			SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
			Date date=new Date();
			hzxxDto.setBgsj(simpleDateFormat.format(date));
		}
		mav.addObject("fjcfbList",fjcfbList);
		hzxxDto.setDqjs(getLoginInfo().getDqjs());
		List<NdzxxjlDto> list = iNdzxxjlService.getNdzByHzid(hzid);
		hzxxDto.setFormAction("mod");
		NdzxxjlDto ndzxxjlDtoOne = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoOne);
		NdzxxjlDto ndzxxjlDtoThree = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoThree);
		NdzxxjlDto ndzxxjlDtoFive = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoFive);
		NdzxxjlDto ndzxxjlDtoSeven = new NdzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoSeven);
		FjcfbDto fjcfbDtoOne=null;
		FjcfbDto fjcfbDtoThree=null;
		FjcfbDto fjcfbDtoFive=null;
		FjcfbDto fjcfbDtoSeven=null;
		if (list != null && list.size() > 0) {
			for (NdzxxjlDto ldzxxjlDto : list) {
				if (ldzxxjlDto != null) {
					setNdzshow(ldzxxjlDto.getNdzjlid(),ldzxxjlDto);
					if (ldzxxjlDto.getJldjt().equals("1")) {
						ndzxxjlDtoOne = ldzxxjlDto;
						fjcfbDtoOne = hzxxService.getFjcfbByjlid(ndzxxjlDtoOne);
					} else if (ldzxxjlDto.getJldjt().equals("3")) {
						ndzxxjlDtoThree = ldzxxjlDto;
						fjcfbDtoThree = hzxxService.getFjcfbByjlid(ndzxxjlDtoThree);
					} else if (ldzxxjlDto.getJldjt().equals("5")) {
						ndzxxjlDtoFive = ldzxxjlDto;
						fjcfbDtoFive = hzxxService.getFjcfbByjlid(ndzxxjlDtoFive);
					} else if (ldzxxjlDto.getJldjt().equals("7")) {
						ndzxxjlDtoSeven = ldzxxjlDto;
						fjcfbDtoSeven = hzxxService.getFjcfbByjlid(ndzxxjlDtoSeven);
					}
				}
			}
		}
		mav.addObject("fjcfbDtoOne", fjcfbDtoOne);
		mav.addObject("fjcfbDtoThree", fjcfbDtoThree);
		mav.addObject("fjcfbDtoFive", fjcfbDtoFive);
		mav.addObject("fjcfbDtoSeven", fjcfbDtoSeven);

		mav.addObject("ndzxxjlDtoOne", ndzxxjlDtoOne);
		mav.addObject("ndzxxjlDtoThree", ndzxxjlDtoThree);
		mav.addObject("ndzxxjlDtoFive", ndzxxjlDtoFive);
		mav.addObject("ndzxxjlDtoSeven", ndzxxjlDtoSeven);
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DISCHARGESTATUS, BasicDataTypeEnum.TREATMENTDEPARTMENT,
						BasicDataTypeEnum.PATIENTCATEGORY, BasicDataTypeEnum.COMPLICATION,
						BasicDataTypeEnum.ANTIBACTERIALHISTORY, BasicDataTypeEnum.SUBGROUP });
		mav.addObject("stateList", jclist.get(BasicDataTypeEnum.DISCHARGESTATUS.getCode()));
		mav.addObject("departmentList", jclist.get(BasicDataTypeEnum.TREATMENTDEPARTMENT.getCode()));
		mav.addObject("patientcategoryList", jclist.get(BasicDataTypeEnum.PATIENTCATEGORY.getCode()));
		mav.addObject("complicationList", jclist.get(BasicDataTypeEnum.COMPLICATION.getCode()));
		mav.addObject("hospitalList", hzxxService.getHospitailList(hzxxDto));
		mav.addObject("historyList", jclist.get(BasicDataTypeEnum.ANTIBACTERIALHISTORY.getCode()));
		mav.addObject("subgroupList", jclist.get(BasicDataTypeEnum.SUBGROUP.getCode()));
		mav.addObject("hzxxDto", hzxxDto);
		if (hzxxDto.getJwhbz() != null && hzxxDto.getJwhbz().length() > 0) {
			List<String> strlist = Arrays.asList(hzxxDto.getJwhbz().split(","));
			mav.addObject("strlist", strlist);
		}
		if (hzxxDto.getYz() != null && hzxxDto.getYz().length() > 0) {
			List<String> yzlist = Arrays.asList(hzxxDto.getYz().split(","));
			mav.addObject("yzlist", yzlist);

		}
		if (hzxxDto.getGrbw() != null && hzxxDto.getGrbw().length() > 0) {
			List<String> grbwlist = Arrays.asList(hzxxDto.getGrbw().split(","));
			mav.addObject("grbwlist", grbwlist);

		}
		// 判断那个日期没有记录
		//加上判断判断界面是否显示此页
		JcsjDto jsYz = jcsjService.getDtoById(hzxxDto.getYz());
		
		if (StringUtil.isBlank(ndzxxjlDtoOne.getJlrq())) {
			mav.addObject("djtView", "1");
			return mav;
		}
		if (StringUtil.isBlank(ndzxxjlDtoThree.getJlrq())&&
				(jsYz.getCskz1().equals("ADULT")||jsYz.getCskz1().equals("IMMUNOSUPPRESSION"))) {
			mav.addObject("djtView", "3");
			return mav;
		}
		if (StringUtil.isBlank(ndzxxjlDtoFive.getJlrq())&&
				(jsYz.getCskz1().equals("ADULT")||jsYz.getCskz1().equals("IMMUNOSUPPRESSION"))) {
			mav.addObject("djtView", "5");
			return mav;
		}
		if (StringUtil.isBlank(ndzxxjlDtoSeven.getJlrq())) {
			mav.addObject("djtView", "7");
			return mav;
		}
		
		mav.addObject("djtView", "1");
		return mav;
	}

	@RequestMapping(value = "/hzxx/exportHzxx")
	public ModelAndView exportHzxx(HzxxDto hzxxDto) {
		ModelAndView mav = new ModelAndView("casereport/hzxx_export");
		FjcfbDto fjDto = new FjcfbDto();
		fjDto.setYwid(hzxxDto.getHzid());
		fjDto.setYwlx(BusTypeEnum.IMP_PURCHASE_GENERATE.getCode());
		List<FjcfbDto> fjlist = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjDto);
		mav.addObject("fjlist", fjlist);
		mav.addObject("hzxxDto", hzxxDto);
		return mav;
	}

	/**
	 * 替换模板
	 * 
	 * @param htglDto
	 * @return
	 */
	@RequestMapping("/hzxx/replaceHzxx")
	@ResponseBody
	public List<Map<String, Object>> replaceHzxx(HzxxDto hzxxDto) {
		List<Map<String, Object>> map = hzxxService.getParamForHzxx(hzxxDto);
		return map;
	}

	// 获取名称
	private String getCsmc(String csid) {
		JcsjDto jsDto = new JcsjDto();
		jsDto.setCsid(csid);
		if (csid == null || csid.equals(""))
			return "";
		JcsjDto jzksJsDto = jcsjService.getDto(jsDto);
		if (jzksJsDto != null && !jzksJsDto.equals("")) {
			return jzksJsDto.getCsmc();
		}
		return "";
	}

	// 存放页面配置字段到对象中
	private void setNdzshow(String ndzid, NdzxxjlDto ndzxxjlDto) {
		// 查询配置的显示字段
		List<NdzdmxqDto> listd = ndzdmxqService.getSjz(ndzid);
		List<NdzshDto> lists = ndzshService.getSjz(ndzid);
		List<NdzxcgDto> listx = ndzxcgService.getSjz(ndzid);
		List<NdzyzzbDto> listy = ndzyzzbService.getSjz(ndzid);
		ndzxxjlDto.setDmxqs(listd);
		ndzxxjlDto.setShs(lists);
		ndzxxjlDto.setXcgs(listx);
		ndzxxjlDto.setYzzbs(listy);
	}

	@InitBinder("ndzxxjlDtoSeven")
	public void initSeven(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("ndzxxjlDtoSeven.");
	}

	@InitBinder("ndzxxjlDtoFive")
	public void initFive(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("ndzxxjlDtoFive.");
	}

	@InitBinder("ndzxxjlDtoThree")
	public void initThree(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("ndzxxjlDtoThree.");
	}

	@InitBinder("ndzxxjlDtoOne")
	public void initOne(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("ndzxxjlDtoOne.");
	}
	/**
	 * 上传页面
	 * @param
	 * @return
	 */
	@RequestMapping(value ="/hzxx/uploadHzxx")
	public ModelAndView uploadHzxx(HzxxDto hzxxDto){
		ModelAndView mav = new ModelAndView("casereport/hzxx_upload");
		List<FjcfbDto> fjcfbList = hzxxService.getFjcfb(hzxxDto);
		String bgsj = hzxxService.getDtoById(hzxxDto.getHzid()).getBgsj();
		if(StringUtil.isBlank(bgsj)) {
			hzxxDto.setBgsj(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		}else {
			hzxxDto.setBgsj(bgsj );
		}
		hzxxDto.setYwlx(BusTypeEnum.IMP_REPORT_HJYZ.getCode());
		mav.addObject("fjcfbList",fjcfbList);
		mav.addObject("hzxxDto", hzxxDto);
		return mav;
	}
	/**
	 * 报告上传保存
	 * @param ndzxxDto
	 * @return
	 */
	@RequestMapping(value = "/hzxx/uploadHzxxSave")
	@ResponseBody
	public Map<String, Object> uploadHzxxSave(HzxxDto hzxxDto){
		Map<String,Object> map = new HashMap<String,Object>();
		try {
			//获取用户信息
			User user = getLoginInfo();
			hzxxDto.setLrry(user.getYhid());
			hzxxDto.setXgry(user.getYhid());			
			hzxxDto.setXgr(user.getYhid());
			List<FjcfbDto> fjcfbList = hzxxService.getFjcfb(hzxxDto);
			boolean isSuccess = hzxxService.uploadHzxxSave(hzxxDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("status", "fail");
			map.put("message", e.toString());
		}
		return map;
	}

}
