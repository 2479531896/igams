package com.matridx.igams.wechat.service.impl;


import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IStatisService;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxModel;
import com.matridx.igams.wechat.dao.post.ITjbysjhbDao;
import com.matridx.igams.wechat.service.svcinterface.ITjbysjhbService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TjbysjhbServiceImpl extends BaseBasicServiceImpl<SjxxDto, SjxxModel, ITjbysjhbDao> implements ITjbysjhbService,IStatisService,IFileImport{
	
	private Logger log = LoggerFactory.getLogger(TjbysjhbServiceImpl.class);

	/**
	 * 实现Statis接口
	 */
	@Override
	public ModelAndView getStatisPage(){
		// TODO Auto-generated method stub
		//地图显示最近一周的时间
		String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -6); //得到前六天
		Date weekdate = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String weektoday=df.format(weekdate);
		String week=weektoday+"~"+date;
		ModelAndView mav = new ModelAndView("wechat/statistics_sjhb/statistics_sjhb");
		mav.addObject("weektime", week);
		return mav;
	}
	
	/**
	 * 按照送检单位统计个数。点击后按照送检医生排名
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisBysjhbAndDb(SjxxDto sjxxDto){
        return dao.getStatisBysjhbAndDb(sjxxDto);
	}
	
	/**
	 * 按照周送检单位统计个数。点击后按照送检医生排名
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisWeekBysjhbAndDb(SjxxDto sjxxDto){
        return dao.getStatisWeekBysjhbAndDb(sjxxDto);
	}

	/**
	 * 根据日期查询每天标本数量
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisByDateAndDb(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		return dao.getStatisYblxByJsrqAndDb(sjxxDto);
	}
	
	/**
	 * 按照日期统计送检报告个数，可按照日，月，年，显示指定前几个,需传递起始时间和时间列表
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisSjbgByBgrqAndDb(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		List<Map<String, String>> listMap= new ArrayList<>();
		try {
			List<String> rqs= new ArrayList<>();
			if("day".equals(sjxxDto.getTj())) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getBgrqstart()));
				Calendar endcalendar=Calendar.getInstance();
				if(StringUtil.isNotBlank(sjxxDto.getBgrqend())) {
					endcalendar.setTime(sdf.parse(sjxxDto.getBgrqend()));
				}else {
					endcalendar.setTime(new Date());
				}
				while(endcalendar.compareTo(calendar) >= 0) {
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.DATE, 1);
				}
			}else if("mon".equals(sjxxDto.getTj())) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getBgrqMstart()));

				Calendar endcalendar=Calendar.getInstance();
				if(StringUtil.isNotBlank(sjxxDto.getBgrqMend())) {
					endcalendar.setTime(sdf.parse(sjxxDto.getBgrqMend()));
				}else {
					endcalendar.setTime(new Date());
				}
				
				while(endcalendar.compareTo(calendar) >= 0) {
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.MONTH, 1);
				}
			}else if("year".equals(sjxxDto.getTj())) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getBgrqYstart()));

				Calendar endcalendar=Calendar.getInstance();
				if(StringUtil.isNotBlank(sjxxDto.getBgrqYend())) {
					endcalendar.setTime(sdf.parse(sjxxDto.getBgrqYend()));
				}else {
					endcalendar.setTime(new Date());
				}
				
				while(endcalendar.compareTo(calendar) >= 0) {
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.YEAR, 1);
				}
			}
			
			sjxxDto.setRqs(rqs);
			listMap = dao.getStatisSjbgByBgrqAndDb(sjxxDto);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
		return listMap;
	}
	/**
	 * 根据标本类型统计标本信息(按照日期)
	 * @return
	 */
	public List<Map<String, String>> getStatisByYblxAndDb(SjxxDto sjxxDto){
        return dao.getStatisByYblxAndDb(sjxxDto);
	}

	/**
	 * 高关注度
	 * @return
	 */
	@Override
	public List<Map<String, String>> getWzxxByGgzdAndDb(SjxxDto sjxxDto){
		return dao.getWzxxByGgzdAndDb(sjxxDto);
	}
	
	/**
	 * 统计结果类型为高关，疑似以及无的标本数量
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String, String>> getYbnumByJglxAndDb(SjxxDto sjxxDto){
		return dao.getYbnumByJglxAndDb(sjxxDto);
	}

	/**
	 * 查询当天录入数量,标本数量,报告数量,高关,低关数量
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public Map<String,Object> getYbslBydayAndDb(SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		//标本数量
		sjxxDto.setJsrq(date);
		Map<String,Object> dtybsl=dao.getYbslBydayAndDb(sjxxDto);
		map.put("dtybsl", dtybsl);
		//报告数量
		SjxxDto sjxxDtobg=new SjxxDto();
		sjxxDtobg.setBgrq(date);
		sjxxDtobg.setYhid(sjxxDto.getYhid());
		Map<String,Object> dtbgsl=dao.getYbslBydayAndDb(sjxxDtobg);
		map.put("dtbgsl", dtbgsl);
		//高关数和低关数
		List<Map<String, String>> dtggdgsl=dao.getYbnumByJglxAndDb(sjxxDtobg);
		map.put("dtggdgsl",dtggdgsl);
		//今日录入数
		sjxxDto.setLrsj(date);
		Map<String,Object> dtlrsl=dao.getLrYbslBydayAndDb(sjxxDto);
		map.put("dtlrybsl",dtlrsl);
		//今日实验数
		Map<String,String> symap= new HashMap<>();
		symap.put("num",dao.getJcbjBySyrqAndDb(sjxxDto)+"");
		map.put("symap",symap);
		return map;
	}
	/**
	 * 统计每天标本的阳性率
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getStatisYxlBybgrqAndDb(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		return dao.getStatisYxlBybgrqAndDb(sjxxDto);
	}

	/**
	  * 查询送检报告完成度
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public Map<String,Object> getBgWcd(SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		//查询前一天进行了实验的标本数量
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); //得到前一天
		Date yestdate = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String yestoday=df.format(yestdate);
		SjxxDto sjxxwcd=new SjxxDto();
		sjxxwcd.setSyrq(yestoday);
		sjxxwcd.setDsyrq(yestoday);
		sjxxwcd.setYhid(sjxxDto.getYhid());
		Map<String,Object> sjxxwad=dao.getybwcd(sjxxwcd);
		map.put("sjxxqytybs", sjxxwad);
		SjxxDto t_sjxxDto = new SjxxDto();
		//前一天进行实验的标本数量
		t_sjxxDto.setBgrq(date);
		t_sjxxDto.setSyrq(yestoday);
		t_sjxxDto.setYhid(sjxxDto.getYhid());
		Map<String,Object> ztsdybsl=dao.getYbslBydayAndDb(t_sjxxDto);
		map.put("sjxxwad", ztsdybsl);
		return map;
	}
	
	/**
	 * 获取最近一周标本送检路线
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String,Object>> getSjxlxxAndDb(SjxxDto sjxxDto){
		return dao.getSjxlxxAndDb(sjxxDto);
	}
	
	/**
	 * 根据收费情况进行统计收样(按照不收费的标本变化，收费标本的变化)
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String, String>> getSjxxBySyAndDb(SjxxDto sjxxDto){
		
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		List<Map<String, String>> resultMaps = dao.getSjxxBySyAndDb(sjxxDto);
		
		String s_preRq = "";
		long sum =0,presum=0;
		for(int i=0;i<resultMaps.size();i++) {
			//日期有变化
			if(!s_preRq.equals(resultMaps.get(i).get("rq"))) {
				if(presum == 0 && i!=0) {
					resultMaps.get(i-1).put("bl", "0");
				}else if(presum != 0) {
					BigDecimal bg_sum = new BigDecimal(presum);
					resultMaps.get(i-1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum,0, RoundingMode.HALF_UP).toString());
				}
				
				presum = sum;
				
				s_preRq= resultMaps.get(i).get("rq");
				
				if("1".equals(resultMaps.get(i).get("sfjs"))) {
					sum = Integer.parseInt(resultMaps.get(i).get("num"));
				}else {
					sum =0;
				}
			}else if("1".equals(resultMaps.get(i).get("sfjs"))) {
				sum += Long.parseLong(resultMaps.get(i).get("num"));
			}
			
		}
		if(presum == 0 && resultMaps.size() > 0) {
			resultMaps.get(resultMaps.size()-1).put("bl", "0");
		}else if(presum != 0) {
			BigDecimal bg_sum = new BigDecimal(presum);
			resultMaps.get(resultMaps.size()-1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum,0, RoundingMode.HALF_UP).toString());
		}
		
		return resultMaps;
	}
	
	/**
	 * 根据收费情况以周为单位进行统计收样(按照不收费的标本变化，收费标本的变化)
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String, String>> getSjxxWeekBySyAndDb(SjxxDto sjxxDto){
		List<Map<String, String>> listMap=null;
		try {
			List<String> rqs= new ArrayList<>();
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
			for(int i=0;i<5;i++) {
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			List<Map<String, String>> resultMaps = dao.getSjxxWeekBySyAndDb(sjxxDto);
			
			String s_preRq = "";
			int sum =0,presum=0;
			for(int i=0;i<resultMaps.size();i++) {
				//日期有变化
				if(!s_preRq.equals(resultMaps.get(i).get("rq"))) {
					if(presum == 0 && i!=0) {
						resultMaps.get(i-1).put("bl", "0");
					}else if(presum != 0) {
						BigDecimal bg_sum = new BigDecimal(presum);
						resultMaps.get(i-1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum,0, RoundingMode.HALF_UP).toString());
					}
					
					presum = sum;
					s_preRq= resultMaps.get(i).get("rq");
					
					if("1".equals(resultMaps.get(i).get("sfjs"))) {
						sum = Integer.parseInt(resultMaps.get(i).get("num"));
					}else {
						sum =0;
					}
				}else if("1".equals(resultMaps.get(i).get("sfjs"))) {
					sum += Integer.parseInt(resultMaps.get(i).get("num"));
				}
				
			}
			if(presum == 0 && resultMaps.size() > 0) {
				resultMaps.get(resultMaps.size()-1).put("bl", "0");
			}else if(presum != 0) {
				BigDecimal bg_sum = new BigDecimal(presum);
				resultMaps.get(resultMaps.size()-1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum,0, RoundingMode.HALF_UP).toString());
			}
			
			return resultMaps;
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
		return listMap;
	}

	/**
	 * 根据开始日期和结束日期，自动计算每一天的日期，形成一个list
	 * @param sjxxDto
	 * @return
	 */
	private List<String> getRqsByStartAndEnd(SjxxDto sjxxDto){
		List<String> rqs= new ArrayList<>();
		try {
			if("day".equals(sjxxDto.getTj())) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
				Calendar endcalendar=Calendar.getInstance();
				if(StringUtil.isNotBlank(sjxxDto.getJsrqend())) {
					endcalendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
				}else {
					endcalendar.setTime(new Date());
				}
				while(endcalendar.compareTo(calendar) >= 0) {
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.DATE, 1);
				}
			}else if("mon".equals(sjxxDto.getTj())) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqMstart()));
	
				Calendar endcalendar=Calendar.getInstance();
				if(StringUtil.isNotBlank(sjxxDto.getJsrqMend())) {
					endcalendar.setTime(sdf.parse(sjxxDto.getJsrqMend()));
				}else {
					endcalendar.setTime(new Date());
				}
				
				while(endcalendar.compareTo(calendar) >= 0) {
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.MONTH, 1);
				}
			}else if("year".equals(sjxxDto.getTj())) {
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy");
				Calendar calendar=Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqYstart()));
	
				Calendar endcalendar=Calendar.getInstance();
				if(StringUtil.isNotBlank(sjxxDto.getJsrqYend())) {
					endcalendar.setTime(sdf.parse(sjxxDto.getJsrqYend()));
				}else {
					endcalendar.setTime(new Date());
				}
				
				while(endcalendar.compareTo(calendar) >= 0) {
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.YEAR, 1);
				}
			}
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
		return rqs;
	}


	@Override
	public boolean existCheck(String fieldName, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 检查标题定义，主要防止模板信息过旧
	 * @param defined
	 * @return
	 */
	public boolean checkDefined(List<Map<String,String>> defined) {
		return true;
	}

}
