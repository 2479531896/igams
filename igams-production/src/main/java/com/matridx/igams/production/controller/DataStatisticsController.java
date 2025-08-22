package com.matridx.igams.production.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.matridx.igams.production.dao.entities.HtglDto;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.service.svcinterface.IHtglService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.springboot.util.date.DateUtils;

@Controller
@RequestMapping("/dataStatistics")
public class DataStatisticsController extends BaseController{

	@Autowired
	private IQgglService qgglService;
	@Autowired
	private IHtglService htglService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	private final Logger log = LoggerFactory.getLogger(DataStatisticsController.class);
	@RequestMapping(value = "/dataStatistics/pageListDataStatistics")
	public ModelAndView pageListDataStatistics() {
		ModelAndView mav = new ModelAndView("dataStatistics/dataStatistics/dataStatistics");
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String nowDate=sdf.format(new Date());
		mav.addObject("nowDate",nowDate);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
/*	@RequestMapping(value = "/dataStatistics/getJrDataStatistics")
	@ResponseBody
	public Map<String,Object> getJrDataStatistics() {
		Map<String, Object> resultmap = new HashMap<>();
		
		List<Map<String, Object>> requisitionMapByMon = qgglService.getRequisitionMapByMon();//获取请购单数量(月)
		List<Map<String, Object>> requisitionMapByWeek = qgglService.getRequisitionMapByWeek();//获取请购单数量(周)
		List<Map<String, Object>> purchaseMaterialMapByMon = qgglService.getPurchaseMaterialMapByMon();//获取请购物料数量(月)
		List<Map<String, Object>> purchaseMaterialMapByWeek = qgglService.getPurchaseMaterialMapByWeek();//获取请购物料数量(周)
		List<Map<String, Object>> contractMapByMon = htglService.getContractMapByMon();//获取请购物料数量(月)
		resultmap.put("byqgdsl",requisitionMapByMon.get(0).get("count"));
		resultmap.put("bzqgdsl",requisitionMapByWeek.get(0).get("num"));
		resultmap.put("byqgwlsl",purchaseMaterialMapByMon.get(0).get("count"));
		resultmap.put("bzqgwlsl",purchaseMaterialMapByWeek.get(0).get("num"));
		resultmap.put("byhtsl",contractMapByMon.get(0).get("count"));
		
		return resultmap;
	}*/
	
	@RequestMapping(value = "/dataStatistics/getDataStatistics")
	@ResponseBody
	public Map<String,Object> getDataStatistics(){
		Map<String, Object> map = new HashMap<>();
		//设置月查询的日期起始
		QgglDto qgglDto_m = new QgglDto();
		HtglDto htglDto_m = new HtglDto();
		QgglDto qgglDto_w = new QgglDto();
		SimpleDateFormat sdf_m = new SimpleDateFormat("yyyy-MM");
		Calendar calendar_m = Calendar.getInstance();
		calendar_m.add(Calendar.MONTH,1);
		String sqrqend = sdf_m.format(calendar_m.getTime());
		try {
			calendar_m.setTime(sdf_m.parse(sqrqend));
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		calendar_m.add(Calendar.MONTH,-11);
		String sqrqstart = sdf_m.format(calendar_m.getTime());
		qgglDto_m.setSqrqstart(sqrqstart);
		qgglDto_m.setSqrqend(sqrqend);
		htglDto_m.setShsjstart(sqrqstart);
		htglDto_m.setShsjend(sqrqend);
		//设置周查询的日期起始
		int dayOfWeek = DateUtils.getWeek(new Date());
		SimpleDateFormat sdf_w = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar_w = Calendar.getInstance();
		String rq;
		if (dayOfWeek == 0)
		{ rq = (DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd")); }
		else
		{ rq = (DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd")); }
		List<String> zrqs = new ArrayList<>();
		zrqs.add(sdf_w.format(new Date()));
		try {
			calendar_w.setTime(sdf_w.parse(rq));
		} catch (ParseException e) {log.error(e.getMessage());}
		for (int i = 0; i < 11; i++)
		{
			zrqs.add(sdf_w.format(calendar_w.getTime()));
			calendar_w.add(Calendar.DATE, -7);
		}
		String[] rqs = new String[zrqs.size()];
		zrqs.toArray(rqs);
		qgglDto_w.setRqs(rqs);
		qgglDto_w.setSqrq(rqs[0]);

		List<Map<String, Object>> requisitionMapByMon = qgglService.getRequisitionMapByMon(qgglDto_m);//获取请购单数量(月)
		List<Map<String, Object>> requisitionMapByWeek = qgglService.getRequisitionMapByWeek(qgglDto_w);//获取请购单数量(周)
		List<Map<String, Object>> purchaseMaterialMapByMon = qgglService.getPurchaseMaterialMapByMon(qgglDto_m);//获取请购物料数量(月)
		List<Map<String, Object>> purchaseMaterialMapByWeek = qgglService.getPurchaseMaterialMapByWeek(qgglDto_w);//获取请购物料数量(周)
		List<Map<String, Object>> contractMapByMon = htglService.getContractMapByMon(htglDto_m);//获取请购物料数量(月)

		map.put("requisitionMapByMon",requisitionMapByMon);
		map.put("requisitionMapByWeek",requisitionMapByWeek);
		map.put("purchaseMaterialMapByMon",purchaseMaterialMapByMon);
		map.put("purchaseMaterialMapByWeek",purchaseMaterialMapByWeek);
		map.put("contractMapByMon",contractMapByMon);
		
		return map;
	}
}
