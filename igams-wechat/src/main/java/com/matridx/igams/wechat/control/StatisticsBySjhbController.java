package com.matridx.igams.wechat.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.service.svcinterface.ITjbysjhbService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wechat")
public class StatisticsBySjhbController extends BaseController{
	@Autowired
	private ITjbysjhbService tjbysjhbService;
	/**
	 * 统计页面
	 * 
	 * @return
	 */
	@RequestMapping("/statisticsBySjhb/pageStatistics")
	public ModelAndView pageStatistics()
	{

		return tjbysjhbService.getStatisPage();
	}
	
	/**
	 * 获取当天标本,报告,高关和低关的数量
	 * @return
	 */
	@RequestMapping("/statisticsBySjhb/pagedataDtData")
	@ResponseBody
	public Map<String,Object> getdtYbsl(SjxxDto sjxxDto){
		User user =getLoginInfo();
		sjxxDto.setYhid(user.getYhid());
		return tjbysjhbService.getYbslBydayAndDb(sjxxDto);
	}

	@RequestMapping("/statisticsBySjhb/pagedataSjxxStatis")
	@ResponseBody
	public Map<String, Object> pagedataSjxxStatis(SjxxDto sjxxDto){
		Map<String, Object> resultmap = new HashMap<>();
		User user =getLoginInfo();
		sjxxDto.setYhid(user.getYhid());
		SjxxDto t_sjxxDto = new SjxxDto();
		t_sjxxDto.setYhid(user.getYhid());
		//标本类型
		setDateByYear(t_sjxxDto,0,false);
		sjxxDto.getZqs().put("yblx", t_sjxxDto.getJsrqYstart() + "~" + t_sjxxDto.getJsrqYend());
		List<Map<String, String>> statisbyyblxlist = tjbysjhbService.getStatisByYblxAndDb(t_sjxxDto);
		resultmap.put("yblxhb", statisbyyblxlist);
		//高度关注
		sjxxDto.getZqs().put("ggzd", t_sjxxDto.getJsrqYstart() + "~" + t_sjxxDto.getJsrqYend());
		List<Map<String, String>> ggzdlist=tjbysjhbService.getWzxxByGgzdAndDb(t_sjxxDto);
		resultmap.put("ggzdhb",ggzdlist);
		//合作伙伴
		sjxxDto.getZqs().put("db", t_sjxxDto.getJsrqYstart() + "~" + t_sjxxDto.getJsrqYend());
		List<Map<String, String>> dblist=tjbysjhbService.getStatisBysjhbAndDb(t_sjxxDto);
		resultmap.put("dbhb",dblist);
		//每天送检数量默认是7天之内的
		
		if(sjxxDto.getTj()==null) {
            sjxxDto.setTj("day");
            setDateByDay(sjxxDto,-6);
            sjxxDto.getZqs().put("jsrqhb", sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
		}
		List<Map<String,String>> sjbglist=tjbysjhbService.getStatisSjbgByBgrqAndDb(sjxxDto);
		List<Map<String,String>> yblxList = tjbysjhbService.getStatisByDateAndDb(sjxxDto);
		List<Map<String,String>> yxllist=tjbysjhbService.getStatisYxlBybgrqAndDb(sjxxDto);
		if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
			for (int i = 0; i <sjbglist.size(); i++){
				yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
				yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
			}
			resultmap.put("jsrqhb",yblxList);
		}
		//高关，疑似，无
		setDateByYear(t_sjxxDto,0,false);
		sjxxDto.getZqs().put("possible", t_sjxxDto.getJsrqYstart() + "~" + t_sjxxDto.getJsrqYend());
		List<Map<String, String>> possiblelist=tjbysjhbService.getYbnumByJglxAndDb(sjxxDto);
		resultmap.put("possiblehb",possiblelist);
		
		Map<String,Object> map1=tjbysjhbService.getBgWcd(sjxxDto);
		resultmap.put("bgwcdhb", map1);
		
		//送检情况
		List<Map<String, String>> ybqk=tjbysjhbService.getSjxxBySyAndDb(sjxxDto);
		resultmap.put("ybqkhb", ybqk);
		
		resultmap.put("searchData", sjxxDto);
		return resultmap;
	}
	
	/**
	 * 通过条件查询刷新页面
	 * 
	 * @return
	 */
	@RequestMapping("/statisticsBySjhb/pagedataSjxxStatisByTj")
	@ResponseBody
	public Map<String, Object> getSjxxStatisByTj(HttpServletRequest req,SjxxDto sjxxDto){
		User user =getLoginInfo();
		sjxxDto.setYhid(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		String method = req.getParameter("method");
		//判断method不能为空，为空即返回空
		if(StringUtil.isBlank(method)) {
			return map;
		}else if("getYblxByYear".equals(method)) {
			//标本按年查询
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("yblx", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
        	List<Map<String, String>> statisbyyblxlist = tjbysjhbService.getStatisByYblxAndDb(sjxxDto);
			 map.put("yblxhb", statisbyyblxlist);
        }else if("getYblxByMon".equals(method)) {
        	//标本按月查询
        	setDateByMonth(sjxxDto,0);
        	sjxxDto.getZqs().put("yblxhb", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
        	List<Map<String, String>> statisbyyblxlist = tjbysjhbService.getStatisByYblxAndDb(sjxxDto);
        	map.put("yblxhb", statisbyyblxlist);
        }else if("getYblxByWeek".equals(method)) {
        	//标本按周查询
        	setDateByWeek(sjxxDto);
        	sjxxDto.getZqs().put("yblx", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
        	List<Map<String, String>> ybsList=tjbysjhbService.getStatisByYblxAndDb(sjxxDto);
			 map.put("yblxhb", ybsList);
        }else if("getYblxByDay".equals(method)) {
        	//标本按日查询
        	setDateByDay(sjxxDto,0);
        	sjxxDto.getZqs().put("yblx", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
        	List<Map<String, String>> ybsList=tjbysjhbService.getStatisByYblxAndDb(sjxxDto);
			 map.put("yblxhb", ybsList);
        }else if("getGgzdByYear".equals(method)) {
        	//高关注度按年
        	setDateByYear(sjxxDto,0,false);
        	sjxxDto.getZqs().put("ggzd", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
        	List<Map<String, String>> ggzdlist=tjbysjhbService.getWzxxByGgzdAndDb(sjxxDto);
    		map.put("ggzdhb",ggzdlist);
        }else if("getGgzdByMon".equals(method)) {
        	//高关注度按月
        	setDateByMonth(sjxxDto,0);
        	sjxxDto.getZqs().put("ggzd", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
        	List<Map<String, String>> ggzdlist=tjbysjhbService.getWzxxByGgzdAndDb(sjxxDto);
    		map.put("ggzdhb",ggzdlist);
        }else if("getGgzdByWeek".equals(method)){
        	//高关注度按周
        	setDateByWeek(sjxxDto);
        	sjxxDto.getZqs().put("ggzd", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
        	List<Map<String, String>> ggzdlist=tjbysjhbService.getWzxxByGgzdAndDb(sjxxDto);
    		map.put("ggzdhb",ggzdlist);
        }else if("getDbByYear".equals(method)) {
        	//合作伙伴按年
        	setDateByYear(sjxxDto,0,false);
        	sjxxDto.getZqs().put("db", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
        	List<Map<String, String>> dblist=tjbysjhbService.getStatisBysjhbAndDb(sjxxDto);
			map.put("dbhb",dblist);
        }else if("getDbByMon".equals(method)) {
        	//合作伙伴按月
        	setDateByMonth(sjxxDto,0);
        	sjxxDto.getZqs().put("db", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
        	List<Map<String, String>> dblist=tjbysjhbService.getStatisBysjhbAndDb(sjxxDto);
    		map.put("dbhb",dblist);
        }else if("getDbByWeek".equals(method)) {
        	//合作伙伴按周
        	setDateByWeek(sjxxDto);
        	sjxxDto.getZqs().put("db", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
        	List<Map<String, String>> dblist=tjbysjhbService.getStatisWeekBysjhbAndDb(sjxxDto);
			map.put("dbhb",dblist);
        }else if("getDbByDay".equals(method)) {
        	//合作伙伴按日
        	setDateByDay(sjxxDto,0);
        	sjxxDto.getZqs().put("db", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
        	List<Map<String, String>> dblist=tjbysjhbService.getStatisBysjhbAndDb(sjxxDto);
			map.put("dbhb",dblist);
        }else if("getJsrqByYear".equals(method)) {
        	//阳性率-送检数量-报告数量 按年
        	sjxxDto.setTj("year");
        	setDateByYear(sjxxDto,-5,true);
            sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqYstart()+"~"+sjxxDto.getJsrqYend());
            List<Map<String,String>> sjbglist=tjbysjhbService.getStatisSjbgByBgrqAndDb(sjxxDto);
    		List<Map<String,String>> yblxList = tjbysjhbService.getStatisByDateAndDb(sjxxDto);
    		List<Map<String,String>> yxllist=tjbysjhbService.getStatisYxlBybgrqAndDb(sjxxDto);
    		if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
    			for (int i = 0; i <sjbglist.size(); i++){
    				yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
    				yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
    			}
    			map.put("jsrqhb",yblxList);
    		}
        }else if("getJsrqByMon".equals(method)) {
        	//阳性率-送检数量-报告数量 按月
        	sjxxDto.setTj("mon");
        	setDateByMonth(sjxxDto,-6);
            sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqMstart()+"~"+sjxxDto.getJsrqMend());
            List<Map<String,String>> sjbglist=tjbysjhbService.getStatisSjbgByBgrqAndDb(sjxxDto);
    		List<Map<String,String>> yblxList = tjbysjhbService.getStatisByDateAndDb(sjxxDto);
    		List<Map<String,String>> yxllist=tjbysjhbService.getStatisYxlBybgrqAndDb(sjxxDto);
    		if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
    			for (int i = 0; i <sjbglist.size(); i++){
    				yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
    				yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
    			}
    			map.put("jsrqhb",yblxList);
    		}
        }else if("getJsrqByWeek".equals(method)) {
        	//阳性率-送检数量-报告数量 按周（七天）
        	sjxxDto.setTj("day");
        	setDateByDay(sjxxDto,-6);
            sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
            List<Map<String,String>> sjbglist=tjbysjhbService.getStatisSjbgByBgrqAndDb(sjxxDto);
    		List<Map<String,String>> yblxList = tjbysjhbService.getStatisByDateAndDb(sjxxDto);
    		List<Map<String,String>> yxllist=tjbysjhbService.getStatisYxlBybgrqAndDb(sjxxDto);
    		if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
    			for (int i = 0; i <sjbglist.size(); i++){
    				yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
    				yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
    			}
    			map.put("jsrqhb",yblxList);
    		}
		}else if("getPossibleByYear".equals(method)) {
	    //高关低关 按年
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("possible", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> possiblelist=tjbysjhbService.getYbnumByJglxAndDb(sjxxDto);
			map.put("possiblehb",possiblelist);
        }else if("getPossibleByMon".equals(method)) {
        	setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("possible", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> possiblelist=tjbysjhbService.getYbnumByJglxAndDb(sjxxDto);
			map.put("possiblehb",possiblelist);
        }else if("getPossibleByWeek".equals(method)) {
        	setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("possible", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> possiblelist=tjbysjhbService.getYbnumByJglxAndDb(sjxxDto);
			map.put("possiblehb",possiblelist);
        }else if("getSjxxYearBySy".equals(method)) {
        	//标本情况按年
        	setDateByYear(sjxxDto,-6,false);
        	sjxxDto.setTj("year");
        	List<Map<String, String>> ybqklist=tjbysjhbService.getSjxxBySyAndDb(sjxxDto);
    		map.put("ybqkhb",ybqklist);
        }else if("getSjxxMonBySy".equals(method)) {
        	//标本情况按月
        	setDateByMonth(sjxxDto,-6);
        	sjxxDto.setTj("mon");
        	List<Map<String, String>> ybqklist=tjbysjhbService.getSjxxBySyAndDb(sjxxDto);
    		map.put("ybqkhb",ybqklist);
        }else if("getSjxxWeekBySy".equals(method)){
        	//标本情况按周
        	setDateByWeek(sjxxDto);
        	List<Map<String, String>> ybqklist=tjbysjhbService.getSjxxWeekBySyAndDb(sjxxDto);
    		map.put("ybqkhb",ybqklist);
        }else if("getSjxxDayBySy".equals(method)){
        	//标本情况按日
        	setDateByDay(sjxxDto,-6);
        	sjxxDto.setTj("day");
        	List<Map<String, String>> ybqklist=tjbysjhbService.getSjxxBySyAndDb(sjxxDto);
    		map.put("ybqkhb",ybqklist);
        }
		map.put("searchData", sjxxDto);
		return map;
	}
	
	/**
	 * 设置按月查询的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几年
	 */
	private void setDateByYear(SjxxDto sjxxDto,int length,boolean isBgFlag) {
		SimpleDateFormat monthSdf=new SimpleDateFormat("yyyy");
		
		Calendar calendar=Calendar.getInstance();
		
		if(length >= 0) {
			sjxxDto.setJsrqYstart(monthSdf.format(calendar.getTime()));
	        calendar.add(Calendar.YEAR, length);
	        sjxxDto.setJsrqYend(monthSdf.format(calendar.getTime()));
		}else {
	        sjxxDto.setJsrqYend(monthSdf.format(calendar.getTime()));
	        calendar.add(Calendar.YEAR, length);
			sjxxDto.setJsrqYstart(monthSdf.format(calendar.getTime()));
		}
        
        if(isBgFlag) {
			sjxxDto.setBgrqYstart(sjxxDto.getJsrqYstart());
			sjxxDto.setBgrqYend(sjxxDto.getJsrqYend());
        }
	}
	
	/**
	 * 设置按月查询的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几月
	 */
	private void setDateByMonth(SjxxDto sjxxDto,int length) {
		SimpleDateFormat monthSdf=new SimpleDateFormat("yyyy-MM");
		
		Calendar calendar=Calendar.getInstance();
		
		if(length>=0) {
			sjxxDto.setJsrqMstart(monthSdf.format(calendar.getTime()));
	        calendar.add(Calendar.MONTH,length);
	        sjxxDto.setJsrqMend(monthSdf.format(calendar.getTime()));
		}else {
	        sjxxDto.setJsrqMend(monthSdf.format(calendar.getTime()));
	        calendar.add(Calendar.MONTH,length);
			sjxxDto.setJsrqMstart(monthSdf.format(calendar.getTime()));
		}
        
		sjxxDto.setBgrqMstart(sjxxDto.getJsrqMstart());
		sjxxDto.setBgrqMend(sjxxDto.getJsrqMend());
	}
	
	/**
	 * 设置周的日期
	 * @param sjxxDto
	 * 			length 长度。要为负数，代表向前推移几天
	 */
	private void setDateByWeek(SjxxDto sjxxDto) {
		SimpleDateFormat daySdf=new SimpleDateFormat("yyyy-MM-dd");
		int dayOfWeek = DateUtils.getWeek(new Date());
		if (dayOfWeek <= 2)
		{
			sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(new Date(), -dayOfWeek - 7)));
			sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(new Date(), -dayOfWeek-1)));
			sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
			sjxxDto.setBgrqend(sjxxDto.getJsrqend());
		} else
		{
			sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(new Date(), -dayOfWeek)));
			sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(new Date(), 6 - dayOfWeek)));
			sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
			sjxxDto.setBgrqend(sjxxDto.getJsrqend());
		}
	}
	
	/**
	 * 设置按天查询的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几天
	 */
	private void setDateByDay(SjxxDto sjxxDto,int length) {
		SimpleDateFormat daySdf=new SimpleDateFormat("yyyy-MM-dd");
		
		if(length >=0 ) {
			sjxxDto.setJsrqstart(daySdf.format(new Date()));
			sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(new Date(), length)));
		}else {
			sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(new Date(), length)));
			sjxxDto.setJsrqend(daySdf.format(new Date()));
		}
		sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
		sjxxDto.setBgrqend(sjxxDto.getJsrqend());

	}
	
	/**
	  * 获取标本路线信息
	 * @param sjxxdto
	 * @return
	 */
	@RequestMapping(value = "/statisticsBySjhb/pagedataGetListYblx")
	@ResponseBody
	public Map<String,Object> pageGetListYblx(SjxxDto sjxxdto){
		User user =getLoginInfo();
		sjxxdto.setYhid(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -6); //得到最近7天
		Date beforesevendate = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String startdate=df.format(beforesevendate);//获取到7天前的日期
		sjxxdto.setJsrqstart(startdate);
		sjxxdto.setJsrqend(date);
		List<Map<String,Object>> yblxlist=tjbysjhbService.getSjxlxxAndDb(sjxxdto);
		map.put("yblxlist", yblxlist);
		return map;
	}
	
}
