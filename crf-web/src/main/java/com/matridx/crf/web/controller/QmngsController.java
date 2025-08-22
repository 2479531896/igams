package com.matridx.crf.web.controller;

import com.matridx.crf.web.dao.entities.*;
import com.matridx.crf.web.service.svcinterface.*;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/qmngs")
public class QmngsController extends BaseController {

	@Autowired
	IHzxxService hzxxService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IQmngsndzxxjlService iNdzxxjlService;
	@Autowired
	private IQmngsdmxqService ndzdmxqService;
	@Autowired
	private IQmngsshService ndzshService;
	@Autowired
	private IQmngsyzzbService ndzyzzbService;
	@Autowired
	private IQmngsxcgService ndzxcgService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	private IQmngshzxxService qmngshzxxService;
	@Autowired
	IQmngsndzxxjlService qmngsndzxxjlService;
	@Autowired
	private IQmngskgryService qmngskgryService;
	/**
	 * qmngs患者信息列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "/hzxx/pageListQmngsHzxx")
	public ModelAndView pageListQmngsHzxx() {
		ModelAndView mav = new ModelAndView("qmngs/qmngshzxx_list");
		Map<String, List<JcsjDto>> jcsjlist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.QMNGSINFECTIONDIAGNOSIS,BasicDataTypeEnum.QMNGSDISCHARGESTATUS,BasicDataTypeEnum.QMNGSDISCHARGESTATUS});
		mav.addObject("infectiondiagnosisList", jcsjlist.get(BasicDataTypeEnum.QMNGSINFECTIONDIAGNOSIS.getCode()));//感染相关诊断
		mav.addObject("stateList", jcsjlist.get(BasicDataTypeEnum.QMNGSDISCHARGESTATUS.getCode()));// 出院状态

		return mav;
	}

	/**
	 * qmngs患者信息列表
	 * 
	 * @return
	 */

	@RequestMapping(value = "/hzxx/listQmngsHzxx")
	@ResponseBody
	public Map<String, Object> listQmngsHzxx(QmngshzxxDto qmngshzxxDto) {
		// 添加角色判断
		List<QmngshzxxDto> t_List = new ArrayList<>();
		List<Map<String,String>> yyxzlist = qmngshzxxService.getHospitailList(getLoginInfo().getDqjs());
		List<String> strList=new ArrayList<String>();
		for (int i = 0; i < yyxzlist.size(); i++){
			if(yyxzlist.get(i).get("csid")!=null) {
				strList.add(yyxzlist.get(i).get("csid"));
			}
		}
		if(strList!=null && strList.size()>0) {
			qmngshzxxDto.setYyxz(strList);
			t_List = (List<QmngshzxxDto>) qmngshzxxService.getPagedDtoList(qmngshzxxDto);
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("total", qmngshzxxDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}

	@RequestMapping(value = "/hzxx/addQmngsHzxx")
	public ModelAndView addHzxx(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("qmngs/qmngshzxx_edit");
		QmngshzxxDto qmngshzxxDto = new QmngshzxxDto();
		qmngshzxxDto.setFormAction("addQmngs");
		mav.addObject("hzxxDto", qmngshzxxDto);
		QmngsndzxxjlDto ndzxxjlDtoOne = new QmngsndzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoOne);
		QmngsndzxxjlDto ndzxxjlDtoThree = new QmngsndzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoThree);
		QmngsndzxxjlDto ndzxxjlDtoFive = new QmngsndzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoFive);
		QmngsndzxxjlDto ndzxxjlDtoSeven = new QmngsndzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoSeven);
		QmngsndzxxjlDto ndzxxjlDtoTwentyEight = new QmngsndzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoTwentyEight);
		mav.addObject("ndzxxjlDtoOne", ndzxxjlDtoOne);
		mav.addObject("ndzxxjlDtoThree", ndzxxjlDtoThree);
		mav.addObject("ndzxxjlDtoFive", ndzxxjlDtoFive);
		mav.addObject("ndzxxjlDtoSeven", ndzxxjlDtoSeven);
		mav.addObject("ndzxxjlDtoTwentyEight", ndzxxjlDtoTwentyEight);
		List<QmngskgryDto> qmngskgryDtoList = new ArrayList<>();
		for(int i=0;i<10;i++){
			qmngskgryDtoList.add(new QmngskgryDto());
		}
		mav.addObject("qmngskgryDtos", qmngskgryDtoList);
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DISCHARGESTATUS, BasicDataTypeEnum.TREATMENTDEPARTMENT,
						BasicDataTypeEnum.PATIENTCATEGORY, BasicDataTypeEnum.QMNGSCOMPLICATION,
						BasicDataTypeEnum.ANTIBACTERIALHISTORY, BasicDataTypeEnum.SUBGROUP,BasicDataTypeEnum.QMNGSINFECTIONDIAGNOSIS ,BasicDataTypeEnum.QMNGS72EFFICACYJUDGMENT});
		mav.addObject("stateList", jclist.get(BasicDataTypeEnum.DISCHARGESTATUS.getCode()));
		mav.addObject("grList", jclist.get(BasicDataTypeEnum.QMNGSINFECTIONDIAGNOSIS.getCode()));
		mav.addObject("sgrList", jclist.get(BasicDataTypeEnum.QMNGS72EFFICACYJUDGMENT.getCode()));
		mav.addObject("departmentList", jclist.get(BasicDataTypeEnum.TREATMENTDEPARTMENT.getCode()));
		mav.addObject("patientcategoryList", jclist.get(BasicDataTypeEnum.PATIENTCATEGORY.getCode()));
		mav.addObject("complicationList", jclist.get(BasicDataTypeEnum.QMNGSCOMPLICATION.getCode()));
		mav.addObject("hospitalList", qmngshzxxService.getHospitailList(getLoginInfo().getDqjs()));
		mav.addObject("historyList", jclist.get(BasicDataTypeEnum.QMNGSANTIBACTERIALHISTORY.getCode()));
		mav.addObject("subgroupList", jclist.get(BasicDataTypeEnum.SUBGROUP.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("kgrysize", qmngskgryDtoList.size());

		return mav;
	}

	@RequestMapping(value = "/hzxx/addQmngsSaveHzxx")
	@ResponseBody
	public Map<String, Object> addSaveUser(QmngshzxxDto hzxxDto,BeanQmngsndzxxFroms beanQmngsndzxxFroms){
		Map<String, Object> map = new HashMap<String, Object>();
		hzxxDto.setLrry(getLoginInfo().getYhid());
		boolean isSuccess =  qmngshzxxService.saveQmngsNdz(hzxxDto,beanQmngsndzxxFroms);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
				: xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	@RequestMapping(value = "/hzxx/modQmngsHzxx")
	public ModelAndView editHzxx(QmngshzxxDto qmngshzxxDto,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("qmngs/qmngshzxx_edit");
		qmngshzxxDto = qmngshzxxService.getDto(qmngshzxxDto);
		if(qmngshzxxDto==null){
			qmngshzxxDto = new QmngshzxxDto();
		}
		qmngshzxxDto.setFormAction("modQmngs");
		QmngsndzxxjlDto ndzxxjlDtoOne = new QmngsndzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoOne);
		QmngsndzxxjlDto ndzxxjlDtoThree = new QmngsndzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoThree);
 		QmngsndzxxjlDto ndzxxjlDtoFive = new QmngsndzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoFive);
		QmngsndzxxjlDto ndzxxjlDtoSeven = new QmngsndzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoSeven);
		QmngsndzxxjlDto ndzxxjlDtoTwentyEight = new QmngsndzxxjlDto();
		setNdzshow("-1",ndzxxjlDtoTwentyEight);
		QmngsndzxxjlDto qmngsndzxxjlDto = new QmngsndzxxjlDto();
		qmngsndzxxjlDto.setQmngshzid(qmngshzxxDto.getQmngshzid());
		List<QmngsndzxxjlDto> list = qmngsndzxxjlService.getDtoList(qmngsndzxxjlDto);
		if (list != null && list.size() > 0) {
			for (QmngsndzxxjlDto ldzxxjlDto : list) {
				if (ldzxxjlDto != null) {
					setNdzshow(ldzxxjlDto.getQmngsndzjlid(),ldzxxjlDto);
					if (ldzxxjlDto.getJldjt().equals("1")) {
						ndzxxjlDtoOne = ldzxxjlDto;
					} else if (ldzxxjlDto.getJldjt().equals("3")) {
						ndzxxjlDtoThree = ldzxxjlDto;
					} else if (ldzxxjlDto.getJldjt().equals("5")) {
						ndzxxjlDtoFive = ldzxxjlDto;
					} else if (ldzxxjlDto.getJldjt().equals("7")) {
						ndzxxjlDtoSeven = ldzxxjlDto;
					}
					else if (ldzxxjlDto.getJldjt().equals("28")) {
						ndzxxjlDtoTwentyEight = ldzxxjlDto;
					}
				}
			}
		}
		QmngskgryDto qmngskgryDto = new QmngskgryDto();
		qmngskgryDto.setQmngshzid(qmngshzxxDto.getQmngshzid());
		List <QmngskgryDto> list1 = qmngskgryService.getDtoList(qmngskgryDto);
		for(int i=0;i<10-list1.size();i++){
			list1.add(new QmngskgryDto());
		}
		mav.addObject("ndzxxjlDtoOne", ndzxxjlDtoOne);
		mav.addObject("ndzxxjlDtoThree", ndzxxjlDtoThree);
		mav.addObject("ndzxxjlDtoFive", ndzxxjlDtoFive);
		mav.addObject("ndzxxjlDtoSeven", ndzxxjlDtoSeven);
		mav.addObject("ndzxxjlDtoTwentyEight", ndzxxjlDtoTwentyEight);
		mav.addObject("qmngskgryDtos", list1);
		mav.addObject("hzxxDto", qmngshzxxDto);
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DISCHARGESTATUS, BasicDataTypeEnum.TREATMENTDEPARTMENT,
						BasicDataTypeEnum.PATIENTCATEGORY, BasicDataTypeEnum.QMNGSCOMPLICATION,
						BasicDataTypeEnum.ANTIBACTERIALHISTORY, BasicDataTypeEnum.SUBGROUP ,BasicDataTypeEnum.QMNGSINFECTIONDIAGNOSIS ,BasicDataTypeEnum.QMNGS72EFFICACYJUDGMENT});
		mav.addObject("stateList", jclist.get(BasicDataTypeEnum.DISCHARGESTATUS.getCode()));
		mav.addObject("departmentList", jclist.get(BasicDataTypeEnum.TREATMENTDEPARTMENT.getCode()));
		mav.addObject("patientcategoryList", jclist.get(BasicDataTypeEnum.PATIENTCATEGORY.getCode()));
		mav.addObject("complicationList", jclist.get(BasicDataTypeEnum.QMNGSCOMPLICATION.getCode()));
		mav.addObject("hospitalList", qmngshzxxService.getHospitailList(getLoginInfo().getDqjs()));
		mav.addObject("historyList", jclist.get(BasicDataTypeEnum.QMNGSANTIBACTERIALHISTORY.getCode()));
		mav.addObject("subgroupList", jclist.get(BasicDataTypeEnum.SUBGROUP.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("grList", jclist.get(BasicDataTypeEnum.QMNGSINFECTIONDIAGNOSIS.getCode()));
		mav.addObject("sgrList", jclist.get(BasicDataTypeEnum.QMNGS72EFFICACYJUDGMENT.getCode()));
		if (qmngshzxxDto.getJwhbz() != null && qmngshzxxDto.getJwhbz().length() > 0) {
			List<String> strlist = Arrays.asList(qmngshzxxDto.getJwhbz().split(","));
			mav.addObject("strlist", strlist);

		}
		if (qmngshzxxDto.getGrbw() != null && qmngshzxxDto.getGrbw().length() > 0) {
			List<String> grbwlist = Arrays.asList(qmngshzxxDto.getGrbw().split(","));
			mav.addObject("grbwlist", grbwlist);

		}
		mav.addObject("kgrysize", list1.size());

		return mav;
	}

	@RequestMapping(value = "/hzxx/modQmngsSaveHzxx")
	@ResponseBody
	public Map<String, Object> modSaveHzxx(QmngshzxxDto qmngshzxxDto, BeanQmngsndzxxFroms beanQmngsndzxxFroms, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		qmngshzxxDto.setXgry(getLoginInfo().getYhid());
		boolean isSuccess = qmngshzxxService.updateQmngshzxx(qmngshzxxDto,beanQmngsndzxxFroms);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
				: xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * qmngs患者信息查看
	 * 
	 * @return
	 */
	@RequestMapping(value = "/hzxx/viewQmngsHzxx")
	public ModelAndView viewHzxx(HttpServletRequest request,QmngshzxxDto qmngshzxxDto) {
		ModelAndView mav = new ModelAndView("qmngs/qmngshzxx_view");
		//患者信息
		QmngshzxxDto hzxxDto = qmngshzxxService.queryById(qmngshzxxDto.getQmngshzid());
		//患者记录
		Map<String,Object> jlMap = qmngsndzxxjlService.getHzjl(qmngshzxxDto.getQmngshzid());
		//抗感染药
		QmngskgryDto qmngskgryDto = new QmngskgryDto();
		qmngskgryDto.setQmngshzid(qmngshzxxDto.getQmngshzid());
		List<QmngskgryDto> qmngskgrylist = qmngskgryService.getDtoList(qmngskgryDto);
		
		mav.addObject("ndzxxjlDtoOne", (QmngsndzxxjlDto)jlMap.get("qmngsndzxxjlsa"));
		mav.addObject("ndzxxjlDtoThree", (QmngsndzxxjlDto)jlMap.get("qmngsndzxxjlsb"));
		mav.addObject("ndzxxjlDtoFive", (QmngsndzxxjlDto)jlMap.get("qmngsndzxxjlsc"));
		mav.addObject("ndzxxjlDtoSeven", (QmngsndzxxjlDto)jlMap.get("qmngsndzxxjlsd"));
		mav.addObject("ndzxxjlDtoEsb", (QmngsndzxxjlDto)jlMap.get("qmngsndzxxjlse"));
		mav.addObject("qmngskgrylist", qmngskgrylist);	
		mav.addObject("hzxxDto", hzxxDto);
		return mav;
	}

	@RequestMapping(value = "/hzxx/delQmngsHzxx")
	@ResponseBody
	public Map<String, Object> delHzxx(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		String qmngshzid = request.getParameter("ids");
		boolean isSuccess = qmngshzxxService.delQmngsHzxx(qmngshzid, getLoginInfo().getYhid());
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
				: xxglService.getModelById("ICOM00002").getXxnr());
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
	private void setNdzshow(String ndzid, QmngsndzxxjlDto ndzxxjlDto) {
		// 查询配置的显示字段
		List<QmngsdmxqDto> listd = ndzdmxqService.getSjz(ndzid);
		List<QmngsshDto> lists = ndzshService.getSjz(ndzid);
		List<QmngsxcgDto> listx = ndzxcgService.getSjz(ndzid);
		List<QmngsyzzbDto> listy = ndzyzzbService.getSjz(ndzid);
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

	@InitBinder("ndzxxjlDtoTwentyEight")
	public void initTwentyEight(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("ndzxxjlDtoTwentyEight.");
	}


}
