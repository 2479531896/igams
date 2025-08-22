package com.matridx.igams.wechat.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XszbDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXszbService;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.enums.CustomStaticEnum;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxStatisticService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wechat")
public class StatisticsController extends BaseController{
	@Autowired
	private ISjxxService sjxxService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private ISjhbxxService sjhbxxService;
	@Autowired
	private ISjxxStatisticService sjxxStatisService;
	@Autowired
	private IXszbService xszbService;
	@Autowired
	private IJcsjService jcsjService;
	/**
	 * 统计页面
	 * 
	 * @return
	 */
	@RequestMapping("/statistics/pagedataStatistics")
	public ModelAndView pageStatistics()
	{

		return sjxxService.getStatisPage();
	}
	
	/**
	 * 获取当天标本,报告,高关和低关的数量
	 * @return
	 */
	@RequestMapping("/statistics/pagedataListDtData")
	@ResponseBody
	public Map<String,Object> getdtYbsl(SjxxDto sjxxDto){
		return sjxxService.getYbslByday(sjxxDto);
	}

	@RequestMapping("/statistics/pagedataSjxxStatis")
	@ResponseBody
	public Map<String, Object> pageGetListSjxxStatis(SjxxDto sjxxDto){
		Map<String, Object> resultmap = new HashMap<>();
		
		SjxxDto t_sjxxDto = new SjxxDto();
		//标本类型
		setDateByYear(t_sjxxDto,0,false);
		sjxxDto.getZqs().put("yblx", t_sjxxDto.getJsrqYstart() + "~" + t_sjxxDto.getJsrqYend());
		List<Map<String, String>> statisbyyblxlist = sjxxService.getStatisByYblx(t_sjxxDto);
		resultmap.put("yblx", statisbyyblxlist);
		//高度关注
		sjxxDto.getZqs().put("ggzd", t_sjxxDto.getJsrqYstart() + "~" + t_sjxxDto.getJsrqYend());
		List<Map<String, String>> ggzdlist=sjxxService.getWzxxByGgzd(t_sjxxDto);
		resultmap.put("ggzd",ggzdlist);
		//合作伙伴
		sjxxDto.getZqs().put("db", t_sjxxDto.getJsrqYstart() + "~" + t_sjxxDto.getJsrqYend());
		List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(t_sjxxDto);
		resultmap.put("db",dblist);
		//每天送检数量默认是7天之内的
		
		if(sjxxDto.getTj()==null) {
            sjxxDto.setTj("day");
            setDateByDay(sjxxDto,-6);
            sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
		}
		List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
		List<Map<String,String>> yblxList = sjxxService.getStatisByDate(sjxxDto);
		List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
		if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
			for (int i = 0; i <sjbglist.size(); i++){
				yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
				yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
			}
			resultmap.put("jsrq",yblxList);
		}
		//高关，疑似，无
		setDateByYear(t_sjxxDto,0,false);
		sjxxDto.getZqs().put("possible", t_sjxxDto.getJsrqYstart() + "~" + t_sjxxDto.getJsrqYend());
		List<Map<String, String>> possiblelist=sjxxService.getYbnumByJglx(sjxxDto);
		resultmap.put("possible",possiblelist);
		
		Map<String,Object> map1=sjxxService.getBgWcd(sjxxDto);
		resultmap.put("bgwcd", map1);
		
		//送检情况
		List<Map<String, String>> ybqk=sjxxService.getSjxxBySy(sjxxDto);
		resultmap.put("ybqk", ybqk);
		
		//数据量情况
		List<Map<String,String>> sjlqk=sjxxService.getSjlStatisticsByJcdw(sjxxDto);
		resultmap.put("sjlqk",sjlqk);
		
		resultmap.put("searchData", sjxxDto);
		return resultmap;
	}
	
	/**
	 * 通过条件查询刷新页面
	 * 
	 * @return
	 */
	@RequestMapping("/statistics/pagedataSjxxStatisByTj")
	@ResponseBody
	public Map<String, Object> pagedataSjxxStatisByTj(HttpServletRequest req,SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		String method = req.getParameter("method");
		//判断method不能为空，为空即返回空
		if(StringUtil.isBlank(method)) {
			return map;
		}else if("getYblxByYear".equals(method)) {
			//标本按年查询
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("yblx", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
        	List<Map<String, String>> statisbyyblxlist = sjxxService.getStatisByYblx(sjxxDto);
			 map.put("yblx", statisbyyblxlist);
        }else if("getYblxByMon".equals(method)) {
        	//标本按月查询
        	setDateByMonth(sjxxDto,0);
        	sjxxDto.getZqs().put("yblx", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
        	List<Map<String, String>> statisbyyblxlist = sjxxService.getStatisByYblx(sjxxDto);
        	map.put("yblx", statisbyyblxlist);
        }else if("getYblxByWeek".equals(method)) {
        	//标本按周查询
        	setDateByWeek(sjxxDto);
        	sjxxDto.getZqs().put("yblx", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
        	List<Map<String, String>> ybsList=sjxxService.getStatisByYblx(sjxxDto);
			 map.put("yblx", ybsList);
        }else if("getYblxByDay".equals(method)) {
        	//标本按日查询
        	setDateByDay(sjxxDto,0);
        	sjxxDto.getZqs().put("yblx", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
        	List<Map<String, String>> ybsList=sjxxService.getStatisByYblx(sjxxDto);
			 map.put("yblx", ybsList);
        }else if("getGgzdByYear".equals(method)) {
        	//高关注度按年
        	setDateByYear(sjxxDto,0,false);
        	sjxxDto.getZqs().put("ggzd", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
        	List<Map<String, String>> ggzdlist=sjxxService.getWzxxByGgzd(sjxxDto);
    		map.put("ggzd",ggzdlist);
        }else if("getGgzdByMon".equals(method)) {
        	//高关注度按月
        	setDateByMonth(sjxxDto,0);
        	sjxxDto.getZqs().put("ggzd", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
        	List<Map<String, String>> ggzdlist=sjxxService.getWzxxByGgzd(sjxxDto);
    		map.put("ggzd",ggzdlist);
        }else if("getGgzdByWeek".equals(method)){
        	//高关注度按周
        	setDateByWeek(sjxxDto);
        	sjxxDto.getZqs().put("ggzd", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
        	List<Map<String, String>> ggzdlist=sjxxService.getWzxxByGgzd(sjxxDto);
    		map.put("ggzd",ggzdlist);
        }else if("getDbByYear".equals(method)) {
        	//合作伙伴按年
        	setDateByYear(sjxxDto,0,false);
        	sjxxDto.getZqs().put("db", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
        	List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(sjxxDto);
			map.put("db",dblist);
        }else if("getDbByMon".equals(method)) {
        	//合作伙伴按月
        	setDateByMonth(sjxxDto,0);
        	sjxxDto.getZqs().put("db", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
        	List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(sjxxDto);
    		map.put("db",dblist);
        }else if("getDbByWeek".equals(method)) {
        	//合作伙伴按周
        	setDateByWeek(sjxxDto);
        	sjxxDto.getZqs().put("db", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
        	List<Map<String, String>> dblist=sjxxService.getStatisWeekBysjhb(sjxxDto);
			map.put("db",dblist);
        }else if("getDbByDay".equals(method)) {
        	//合作伙伴按日
        	setDateByDay(sjxxDto,0);
        	sjxxDto.getZqs().put("db", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
        	List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(sjxxDto);
			map.put("db",dblist);
        }else if("getJsrqByYear".equals(method)) {
        	//阳性率-送检数量-报告数量 按年
        	sjxxDto.setTj("year");
        	setDateByYear(sjxxDto,-5,true);
            sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqYstart()+"~"+sjxxDto.getJsrqYend());
            List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
    		List<Map<String,String>> yblxList = sjxxService.getStatisByDate(sjxxDto);
    		List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
    		if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
    			for (int i = 0; i <sjbglist.size(); i++){
    				yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
    				yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
    			}
    			map.put("jsrq",yblxList);
    		}
        }else if("getJsrqByMon".equals(method)) {
        	//阳性率-送检数量-报告数量 按月
        	sjxxDto.setTj("mon");
        	setDateByMonth(sjxxDto,-6);
            sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqMstart()+"~"+sjxxDto.getJsrqMend());
            List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
    		List<Map<String,String>> yblxList = sjxxService.getStatisByDate(sjxxDto);
    		List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
    		if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
    			for (int i = 0; i <sjbglist.size(); i++){
    				yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
    				yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
    			}
    			map.put("jsrq",yblxList);
    		}
        }else if("getJsrqByWeek".equals(method)) {
        	//阳性率-送检数量-报告数量 按周（七天）
        	sjxxDto.setTj("day");
        	setDateByDay(sjxxDto,-6);
            sjxxDto.getZqs().put("jsrq", sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
            List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
    		List<Map<String,String>> yblxList = sjxxService.getStatisByDate(sjxxDto);
    		List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
    		if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
    			for (int i = 0; i <sjbglist.size(); i++){
    				yblxList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
    				yblxList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
    			}
    			map.put("jsrq",yblxList);
    		}
		}else if("getPossibleByYear".equals(method)) {
	    //高关低关 按年
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("possible", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> possiblelist=sjxxService.getYbnumByJglx(sjxxDto);
			map.put("possible",possiblelist);
        }else if("getPossibleByMon".equals(method)) {
        	setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("possible", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> possiblelist=sjxxService.getYbnumByJglx(sjxxDto);
			map.put("possible",possiblelist);
        }else if("getPossibleByWeek".equals(method)) {
        	setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("possible", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> possiblelist=sjxxService.getYbnumByJglx(sjxxDto);
			map.put("possible",possiblelist);
        }else if("getSjxxYearBySy".equals(method)) {
        	//标本情况按年
        	setDateByYear(sjxxDto,-6,false);
        	sjxxDto.setTj("year");
        	List<Map<String, String>> ybqklist=sjxxService.getSjxxBySy(sjxxDto);
    		map.put("ybqk",ybqklist);
        }else if("getSjxxMonBySy".equals(method)) {
        	//标本情况按月
        	setDateByMonth(sjxxDto,-6);
        	sjxxDto.setTj("mon");
        	List<Map<String, String>> ybqklist=sjxxService.getSjxxBySy(sjxxDto);
    		map.put("ybqk",ybqklist);
        }else if("getSjxxWeekBySy".equals(method)){
        	//标本情况按周
        	setDateByWeek(sjxxDto);
        	List<Map<String, String>> ybqklist=sjxxService.getSjxxWeekBySy(sjxxDto);
    		map.put("ybqk",ybqklist);
        }else if("getSjxxDayBySy".equals(method)){
        	//标本情况按日
        	setDateByDay(sjxxDto,-6);
        	sjxxDto.setTj("day");
        	List<Map<String, String>> ybqklist=sjxxService.getSjxxBySy(sjxxDto);
    		map.put("ybqk",ybqklist);
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
	 * 长度。要为负数，代表向前推移几天
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
	@RequestMapping(value = "/statistics/pagedataListYblx")
	@ResponseBody
	public Map<String,Object> pagedataListYblx(SjxxDto sjxxdto){
		Map<String, Object> map = new HashMap<>();
		String date=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -6); //得到最近7天
		Date beforesevendate = calendar.getTime();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String startdate=df.format(beforesevendate);//获取到7天前的日期
		sjxxdto.setJsrqstart(startdate);
		sjxxdto.setJsrqend(date);
		List<Map<String,Object>> yblxlist=sjxxService.getSjxlxx(sjxxdto);
		map.put("yblxlist", yblxlist);
		return map;
	}
	
	/**
	 * 数据可视化页面
	 * @return
	 */
	@RequestMapping(value = "/statistics/pageListData")
	public ModelAndView DataVisualization() {
        return new ModelAndView("wechat/statistics/dataVisualization");
	}
	
	/**
	 * 数据可视化刷新
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pagedataListSjxx")
	@ResponseBody
	public Map<String,Object> pagedataListSjxx(SjxxDto sjxxDto){
		Map<String, Object> map= new HashMap<>();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -1); //得到前一天
		Date date = calendar.getTime();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		sjxxDto.setLrsjStart(sdf.format(date)+" "+"23:59:59");
		map.put("data", sjxxService.getListSjxx(sjxxDto));
		return map;
	}
	
	/**
	 * 数据可视化刷新
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pagedataSjxxBylrsj")
	@ResponseBody
	public Map<String,Object> pageGetListSjxxBylrsj(SjxxDto sjxxDto){
		Map<String, Object> map= new HashMap<>();
		List<SjxxDto> sjxxList=null;
		if(sjxxDto.getLrsjStart()!=null && sjxxDto.getLrsjEnd()!=null) {
			sjxxList=sjxxService.getListSjxxBylrsj(sjxxDto);
		}
		map.put("data", sjxxList);
		return map;
	}
	
	/**
	 * 自定义统计页面
	 * @return
	 */
	@RequestMapping("/statistics/pageStatisticsCustom")
	public ModelAndView pageStatisticsCustom() {
		
		ModelAndView mav=new ModelAndView("wechat/statistics/statiscs_dynamic");
		
		mav.addObject("listMap", CustomStaticEnum.getAllListMap());
		mav.addObject("tjtjMap", CustomStaticEnum.getWhereListMap());
		return mav;
	}
	
	/**
	 * 本地服务 日报页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/statistics/pageListLocal_daliy")
	public  ModelAndView pageListLocal_daliy(SjxxDto sjxxDto) {
		if(StringUtil.isNotBlank(sjxxDto.getJsrq())) {
			//直接访问日报
			ModelAndView mav=new ModelAndView("wechat/statistics/statistics_day_lead");
			String sign=commonService.getSign(sjxxDto.getJsrq());
            sjxxDto.setSign(sign);
			String load_flg="1";
			//查询区域信息
			XszbDto xszbDto = new XszbDto();
			xszbDto.setJsmc("大区经理");
			try {
				if(StringUtil.isBlank(sjxxDto.getJsrq())) {
					SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd");
					String jsrq=sdf.format(new Date());
					sjxxDto.setJsrq(jsrq);
				}
				SjxxDto sxxDto = sjxxService.getSxx(sjxxDto);
				SjxxDto fclDto = sjxxService.getFcl(sjxxDto);
				mav.addObject("sxxDto",sxxDto);
				mav.addObject("fclDto",fclDto);
			}catch (Exception e){
				List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
				mav.addObject("xszbDtos", xszbDtos);
				mav.addObject("sjxxDto", sjxxDto);
				mav.addObject("load_flg", load_flg);
				return mav;
			}
			List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
			mav.addObject("xszbDtos", xszbDtos);
			mav.addObject("sjxxDto", sjxxDto);
			mav.addObject("load_flg", load_flg);
			return mav;
		}else {//先访问单独页面之后再去访问日报
			ModelAndView mav=new ModelAndView("wechat/statistics/local_weekly");
			SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd");
			String jsrq=sdf.format(new Date());
			mav.addObject("jsrq", jsrq);
			return mav;
		}
	}
	/**
	 * 本地服务 日报页面(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/statistics/pageListLocal_daliy_jsrq")
	public  ModelAndView pageListLocal_daliy_jsrq(SjxxDto sjxxDto) {
		if(StringUtil.isNotBlank(sjxxDto.getJsrq())) {
			//直接访问日报
			ModelAndView mav=new ModelAndView("wechat/statistics/statistics_day_lead_jsrq");
			String sign=commonService.getSign(sjxxDto.getJsrq());
            sjxxDto.setSign(sign);
			String load_flg="1";
			//查询区域信息
			XszbDto xszbDto = new XszbDto();
			xszbDto.setJsmc("大区经理");
			try {
				SjxxDto sxxDto = sjxxService.getSxx(sjxxDto);
				SjxxDto fclDto = sjxxService.getFcl(sjxxDto);
				mav.addObject("sxxDto",sxxDto);
				mav.addObject("fclDto",fclDto);
			}catch (Exception e){
				List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
				mav.addObject("xszbDtos", xszbDtos);
				mav.addObject("sjxxDto", sjxxDto);
				mav.addObject("load_flg", load_flg);
				return mav;
			}
			List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
			mav.addObject("xszbDtos", xszbDtos);
			mav.addObject("sjxxDto", sjxxDto);
			mav.addObject("load_flg", load_flg);
			return mav;
		}else {//先访问单独页面之后再去访问日报
			ModelAndView mav=new ModelAndView("wechat/statistics/local_weekly_jsrq");
			SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd");
			String jsrq=sdf.format(new Date());
			mav.addObject("jsrq", jsrq);
			return mav;
		}
	}
	
	/**
	 * 本地服务 周报页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/statistics/pageListLocal_weekly")
	public  ModelAndView pageListLocal_weekly(SjxxDto sjxxDto) {
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_weekly_lead");
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
			}
		}
		//查询合作伙伴分类信息
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.selectFl();
		//查询区域信息
		XszbDto xszbDto = new XszbDto();
		xszbDto.setJsmc("大区经理");
		List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
		mav.addObject("xszbDtos",xszbDtos);
		String load_flg="1";
		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		mav.addObject("load_flg",load_flg);
		return mav;
	}
	/**
	 * 本地服务 周报页面(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/statistics/pageListLocal_weekly_jsrq")
	public  ModelAndView pageListLocal_weekly_jsrq(SjxxDto sjxxDto) {
		ModelAndView mav = new ModelAndView("wechat/statistics/statistics_weekly_lead_jsrq");
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 8), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-2), "yyyy-MM-dd"));
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 5 - dayOfWeek), "yyyy-MM-dd"));
			}
		}
		//查平台基础数据
		JcsjDto jcsj_zbfl = new JcsjDto();
		jcsj_zbfl.setJclb(BasicDataTypeEnum.SALE_CLASSIFY.getCode());
		jcsj_zbfl.setCsdm("TJXSTJ");
		jcsj_zbfl = jcsjService.getDto(jcsj_zbfl);
		JcsjDto jcsj_t = new JcsjDto();
		jcsj_t.setFcsid(jcsj_zbfl.getCsid());
		List<JcsjDto> tjxs_zfls = jcsjService.getDtoList(jcsj_t);//取特检销售的子分类：区域
		mav.addObject("jcsjXszfls", tjxs_zfls);
		// //查询合作伙伴分类信息
//		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getSjhbFlAndZfl();
		//查询区域信息
		XszbDto xszbDto = new XszbDto();
		xszbDto.setJsmc("大区经理");
		List<XszbDto> dqs = xszbService.getXszbQyxx(xszbDto);
		mav.addObject("xszbDtos",dqs);
		String load_flg="1";
//		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		mav.addObject("load_flg",load_flg);
		return mav;
	}
	/**
	 * 周报统计(营销)(销售)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pageListLocal_weekly_jsrq_sale")
	public ModelAndView pageListLocal_weekly_jsrq_sale(SjxxDto sjxxDto,HttpServletRequest req) {
		User loginInfo = getLoginInfo();
		sjxxDto.setYhid(loginInfo.getYhid());
		ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/statistic_weekly_jsrq");
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 8), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-2), "yyyy-MM-dd"));
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 5 - dayOfWeek), "yyyy-MM-dd"));
			}
		}
		//查询合作伙伴分类信息
		SjhbxxDto sjhbxxDto=new SjhbxxDto();
		sjhbxxDto.setXtyhid(sjxxDto.getYhid());
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.selectFlByWeekly(sjhbxxDto);
		mav.addObject("xsbj","0");
		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		mav.addObject("sjxxDto",sjxxDto);
		//查平台基础数据
		JcsjDto jcsj_zbfl = new JcsjDto();
		jcsj_zbfl.setJclb(BasicDataTypeEnum.SALE_CLASSIFY.getCode());
		jcsj_zbfl.setCsdm("TJXSTJ");
		jcsj_zbfl = jcsjService.getDto(jcsj_zbfl);
		JcsjDto jcsj_t = new JcsjDto();
		jcsj_t.setFcsid(jcsj_zbfl.getCsid());
		List<JcsjDto> tjxs_zfls = jcsjService.getDtoList(jcsj_t);//取特检销售的子分类：区域
		mav.addObject("jcsjXszfls", tjxs_zfls);
		//查询区域信息
		XszbDto xszbDto = new XszbDto();
		xszbDto.setJsmc("大区经理");
		List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
		mav.addObject("xszbDtos",xszbDtos);
		//是否领导周报页面跳转
		mav.addObject("ldtzbj","0");
		mav.addObject("ptgss","");
		mav.addObject("flag","local");
		return mav;
	}

	
	/**
	 * 日报统计，跳转日报页面
	 * @return
	 */
	@RequestMapping("/statistics/getDaily")
	public ModelAndView getDaily(SjxxDto sjxxDto,HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("wechat/statistics/statistics_day_lead");
		sjxxDto.setSign(commonService.getSign(sjxxDto.getJsrq()));
		String load_flg="1";
		//查询区域信息
		XszbDto xszbDto = new XszbDto();
		xszbDto.setJsmc("大区经理");
		List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
		mav.addObject("xszbDtos", xszbDtos);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg", load_flg);
		return mav;
	}
	
	/**
	 * 运营统计
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pageStatisticsOperational")
	public ModelAndView pageStatisticsOperational(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/operational/operational");
		if(StringUtil.isBlank(sjxxDto.getTjzq())) {
			sjxxDto.setTjzq("week");
		}
		mav.addObject("sjxxDto",sjxxDto);
		return mav;
	}
	
	/**
	 * 运营统计数据
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pagedataOperational")
	@ResponseBody
	public Map<String,Object> pagedataOperational(SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		String tjzq=sjxxDto.getTjzq();
		if(StringUtil.isNotBlank(tjzq)) {
			if("year".equals(tjzq)) {
				setDateByYear(sjxxDto,-6,false);
				sjxxDto.setTjzq("year");
				sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				map.put("searchData", sjxxDto);
			}else if("mon".equals(tjzq)) {
				setDateByMonth(sjxxDto,-6);
				sjxxDto.setTjzq("mon");
				sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				map.put("searchData", sjxxDto);
			}else if("week".equals(tjzq)) {
				setDateByWeek(sjxxDto);
				sjxxDto.setTjzq("week");
				sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				map.put("searchData", sjxxDto);
			}else if("day".equals(tjzq)) {
				setDateByDay(sjxxDto,0);
				sjxxDto.setTjzq("day");
				sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				map.put("searchData", sjxxDto);
			}
			
			List<Map<String,Object>> jcdw=sjxxStatisService.getSjxxByJcdw(sjxxDto);
			List<Map<String,Object>> sys=sjxxStatisService.getWsjByJcdw(sjxxDto);
			map.put("sys", sys);
			map.put("jcdw", jcdw);
		}
		return map;
	}
	
	/**
	 * 运营统计第一页点击周期查询
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/statistics/pagedataOperational_firstPage_click")
	public Map<String,Object> pagedataOperational_firstPage_click(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		String method = request.getParameter("method");
		if(StringUtil.isBlank(method)) {
			return map;
		}else if("jcdw_year".equals(method)) {
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.setTjzq("year");
			sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String,Object>> jcdw=sjxxStatisService.getSjxxByJcdw(sjxxDto);
			map.put("jcdw", jcdw);
		}else if("jcdw_mon".equals(method)) {
			setDateByMonth(sjxxDto,-6);
			sjxxDto.setTjzq("mon");
			sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String,Object>> jcdw=sjxxStatisService.getSjxxByJcdw(sjxxDto);
			map.put("jcdw", jcdw);
		}else if("jcdw_week".equals(method)) {
			setDateByWeek(sjxxDto);
			sjxxDto.setTjzq("week");
			sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> jcdw=sjxxStatisService.getSjxxByJcdw(sjxxDto);
			map.put("jcdw", jcdw);
		}else if("jcdw_day".equals(method)) {
			setDateByDay(sjxxDto,0);
			sjxxDto.setTjzq("day");
			sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> jcdw=sjxxStatisService.getSjxxByJcdw(sjxxDto);
			map.put("jcdw", jcdw);
		}else if("sys_year".equals(method)) {
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.setTjzq("year");
			sjxxDto.getZqs().put("sys", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String,Object>> sys=sjxxStatisService.getWsjByJcdw(sjxxDto);
			map.put("sys", sys);
		}else if("sys_mon".equals(method)) {
			setDateByMonth(sjxxDto,-6);
			sjxxDto.setTjzq("mon");
			sjxxDto.getZqs().put("sys", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String,Object>> sys=sjxxStatisService.getWsjByJcdw(sjxxDto);
			map.put("sys", sys);
		}else if("sys_week".equals(method)) {
			setDateByWeek(sjxxDto);
			sjxxDto.setTjzq("week");
			sjxxDto.getZqs().put("sys", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> sys=sjxxStatisService.getWsjByJcdw(sjxxDto);
			map.put("sys", sys);
		}else if("sys_day".equals(method)) {
			setDateByDay(sjxxDto,0);
			sjxxDto.setTjzq("day");
			sjxxDto.getZqs().put("sys", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> sys=sjxxStatisService.getWsjByJcdw(sjxxDto);
			map.put("sys", sys);
		}
		map.put("searchData",sjxxDto);
		return map;
	}

	/**
	 * 根据检测单位查询统计页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pageGetListOperationalStatistics_one_jcdw")
	public ModelAndView pageGetListOperationalStatistics_one_jcdw(SjxxDto sjxxDto) {
		ModelAndView mav= new ModelAndView("wechat/operational/operational_one_jcdw");
		mav.addObject("sjxxDto",sjxxDto);
		return mav;
	}
	
	/**
	 * 根据检测单位查询统计页面2
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pageGetListOperationalStatistics_two_jcdw")
	public ModelAndView pageGetListOperationalStatistics_two_jcdw(SjxxDto sjxxDto) {
		ModelAndView mav= new ModelAndView("wechat/operational/operational_two_jcdw");
		mav.addObject("sjxxDto",sjxxDto);
		return mav;
	}

	/**
	 * 根据检测单位查询统计数据
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pagedataOperational_one_jcdw")
	@ResponseBody
	public Map<String,Object> pagedataOperational_one_jcdw(SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		String tjzq=sjxxDto.getTjzq();
		if(StringUtil.isNotBlank(tjzq)) {
			//查询检测单位
			SjxxDto sjxxDto_jcdw=new SjxxDto();
			sjxxDto_jcdw.setJcdw(sjxxDto.getJcdw());
			if("year".equals(tjzq)) {
				setDateByYear(sjxxDto_jcdw,-6,false);
				sjxxDto_jcdw.setTjzq("year");
				sjxxDto_jcdw.setTj("year");
				sjxxDto.getZqs().put("jcdw", sjxxDto_jcdw.getJsrqYstart() + "~" + sjxxDto_jcdw.getJsrqYend());
			}else if("mon".equals(tjzq)) {
				setDateByMonth(sjxxDto_jcdw,-6);
				sjxxDto_jcdw.setTjzq("mon");
				sjxxDto_jcdw.setTj("mon");
				sjxxDto.getZqs().put("jcdw", sjxxDto_jcdw.getJsrqMstart() + "~" + sjxxDto_jcdw.getJsrqMend());
			}else if("week".equals(tjzq)) {
				setDateByWeek(sjxxDto_jcdw);
				sjxxDto_jcdw.setTjzq("week");
				sjxxDto_jcdw.setTj("week");
				sjxxDto.getZqs().put("jcdw", sjxxDto_jcdw.getJsrqstart() + "~" + sjxxDto_jcdw.getJsrqend());
			}else if("day".equals(tjzq)) {
				setDateByDay(sjxxDto_jcdw,-6);
				sjxxDto_jcdw.setTjzq("day");
				sjxxDto_jcdw.setTj("day");
				sjxxDto.getZqs().put("jcdw", sjxxDto_jcdw.getJsrqstart() + "~" + sjxxDto_jcdw.getJsrqend());
			}
			List<Map<String,Object>> jcdw=sjxxStatisService.getSjxxInJcdw(sjxxDto_jcdw);
			map.put("jcdw", jcdw);
			
			//查询省份
			SjxxDto sjxxDto_sf=new SjxxDto();
			sjxxDto_sf.setJcdw(sjxxDto.getJcdw());
			if("year".equals(tjzq)) {
				setDateByYear(sjxxDto_sf,-6,false);
				sjxxDto_sf.setTjzq("year");
				sjxxDto_sf.getZqs().put("sf", sjxxDto_sf.getJsrqYstart() + "~" + sjxxDto_sf.getJsrqYend());
			}else if("mon".equals(tjzq)) {
				setDateByMonth(sjxxDto_sf,-6);
				sjxxDto_sf.setTjzq("mon");
				sjxxDto_sf.getZqs().put("sf", sjxxDto_sf.getJsrqMstart() + "~" + sjxxDto_sf.getJsrqMend());
			}else if("week".equals(tjzq)) {
				setDateByWeek(sjxxDto_sf);
				sjxxDto_sf.setTjzq("week");
				sjxxDto.getZqs().put("sf", sjxxDto_sf.getJsrqstart() + "~" + sjxxDto_sf.getJsrqend());
			}else if("day".equals(tjzq)) {
				setDateByDay(sjxxDto_sf,0);
				sjxxDto_sf.setTjzq("day");
				sjxxDto.getZqs().put("sf", sjxxDto_sf.getJsrqstart() + "~" + sjxxDto_sf.getJsrqend());
			}
			List<Map<String,Object>> sf=sjxxStatisService.getSjxxBySfPie(sjxxDto_sf);
			map.put("sf", sf);
			
			map.put("searchData", sjxxDto);
			map.put("jcdw_tjzq", sjxxDto_jcdw.getTjzq());
			map.put("sf_tjzq", sjxxDto_sf.getTjzq());
		}
		return map;
	}
	
	/**
	 * 根据检测单位查询统计数据
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pagedataOperational_two_jcdw")
	@ResponseBody
	public Map<String,Object> pagedataOperational_two_jcdw(SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		String tjzq=sjxxDto.getTjzq();
		if(StringUtil.isNotBlank(tjzq)) {
			//查询检测单位
			SjxxDto sjxxDto_jcdw=new SjxxDto();
			sjxxDto_jcdw.setJcdw(sjxxDto.getJcdw());
			if("year".equals(tjzq)) {
				setDateByYear(sjxxDto_jcdw,-6,false);
				sjxxDto_jcdw.setTjzq("year");
				sjxxDto_jcdw.setTj("year");
				sjxxDto.getZqs().put("jcdw", sjxxDto_jcdw.getJsrqYstart() + "~" + sjxxDto_jcdw.getJsrqYend());
			}else if("mon".equals(tjzq)) {
				setDateByMonth(sjxxDto_jcdw,-6);
				sjxxDto_jcdw.setTjzq("mon");
				sjxxDto_jcdw.setTj("mon");
				sjxxDto.getZqs().put("jcdw", sjxxDto_jcdw.getJsrqMstart() + "~" + sjxxDto_jcdw.getJsrqMend());
			}else if("week".equals(tjzq)) {
				setDateByWeek(sjxxDto_jcdw);
				sjxxDto_jcdw.setTjzq("week");
				sjxxDto_jcdw.setTj("week");
				sjxxDto.getZqs().put("jcdw", sjxxDto_jcdw.getJsrqstart() + "~" + sjxxDto_jcdw.getJsrqend());
			}else if("day".equals(tjzq)) {
				setDateByDay(sjxxDto_jcdw,-6);
				sjxxDto_jcdw.setTjzq("day");
				sjxxDto_jcdw.setTj("day");
				sjxxDto.getZqs().put("jcdw", sjxxDto_jcdw.getJsrqstart() + "~" + sjxxDto_jcdw.getJsrqend());
			}
			List<Map<String,Object>> jcdw=sjxxStatisService.getSjxxInJcdwByWsj(sjxxDto_jcdw);
			map.put("jcdw", jcdw);
			sjxxDto_jcdw.setJsrq(null);
			List<SjxxDto> list=sjxxStatisService.getSjxxInJcdwByWsjList(sjxxDto_jcdw);
			map.put("sjxxDtoList",list);			
			map.put("searchData", sjxxDto);
			map.put("jcdw_tjzq", sjxxDto_jcdw.getTjzq());
			
		}
		return map;
	}
	
	
	/**
	 * 点击柱状图操作饼状图
	 * @param request
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pagedataClickCategoryEchartsLookWsj")
	@ResponseBody
	public Map<String,Object> pagedataClickCategoryEchartsLookWsj(HttpServletRequest request,SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		String suffix=request.getParameter("suffix");
		if(StringUtil.isBlank(suffix)){
			return map;
		}else if("Y".equals(suffix)) {
			sjxxDto.setJsrqYstart(sjxxDto.getJsrq());
			sjxxDto.setJsrqYend(sjxxDto.getJsrq());
			sjxxDto.setJsrq(null);
		}else if("M".equals(suffix)) {
			sjxxDto.setJsrqMstart(sjxxDto.getJsrq());
			sjxxDto.setJsrqMend(sjxxDto.getJsrq());
			sjxxDto.setJsrq(null);
		}else if("W".equals(suffix)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(sdf.parse(sjxxDto.getJsrq()));
				calendar.add(Calendar.DATE, -6);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sjxxDto.setJsrqstart(sdf.format(calendar.getTime()));
			sjxxDto.setJsrqend(sjxxDto.getJsrq());
			sjxxDto.setJsrq(null);
		}else if("D".equals(suffix)) {
			sjxxDto.setJsrqstart(sjxxDto.getJsrq());
			sjxxDto.setJsrqend(sjxxDto.getJsrq());
			sjxxDto.setJsrq(null);
		}
		List<SjxxDto> list=sjxxStatisService.getSjxxInJcdwByWsjList(sjxxDto);
		map.put("sjxxDtoList",list);
		return map;
	}
	
	/**
	 * 统计点击切换周期
	 * @param request
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pagedataOperational_Statis_click")
	@ResponseBody
	public Map<String,Object> pagedataOperational_Statis_click(HttpServletRequest request,SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		String method = request.getParameter("method");
		if(StringUtil.isBlank(method)) {
			return map;
		}else if("jcdw_year".equals(method)) {
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.setTjzq("year");
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String,Object>> jcdw=sjxxStatisService.getSjxxInJcdw(sjxxDto);
			map.put("jcdw", jcdw);
			sjxxDto.setJsrqYstart(sjxxDto.getJsrq());
			sjxxDto.setJsrqYend(sjxxDto.getJsrq());
			sjxxDto.setJsrq(null);
		}else if("jcdw_mon".equals(method)) {
			setDateByMonth(sjxxDto,-6);
			sjxxDto.setTjzq("mon");
			sjxxDto.setTj("mon");
			sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String,Object>> jcdw=sjxxStatisService.getSjxxInJcdw(sjxxDto);
			map.put("jcdw", jcdw);
		}else if("jcdw_week".equals(method)) {
			setDateByWeek(sjxxDto);
			sjxxDto.setTjzq("week");
			sjxxDto.setTj("week");
			sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> jcdw=sjxxStatisService.getSjxxInJcdw(sjxxDto);
			map.put("jcdw", jcdw);
		}else if("jcdw_day".equals(method)) {
			setDateByDay(sjxxDto,-6);
			sjxxDto.setTjzq("day");
			sjxxDto.setTj("day");
			sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> jcdw=sjxxStatisService.getSjxxInJcdw(sjxxDto);
			map.put("jcdw", jcdw);
		}else if("sf_year".equals(method)) {
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.setTjzq("year");
			sjxxDto.getZqs().put("sf", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String,Object>> sf=sjxxStatisService.getSjxxBySfPie(sjxxDto);
			map.put("sf", sf);
		}else if("sf_mon".equals(method)) {
			setDateByMonth(sjxxDto,-6);
			sjxxDto.setTjzq("mon");
			sjxxDto.getZqs().put("sf", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String,Object>> sf=sjxxStatisService.getSjxxBySfPie(sjxxDto);
			map.put("sf", sf);
		}else if("sf_week".equals(method)) {
			setDateByWeek(sjxxDto);
			sjxxDto.setTjzq("week");
			sjxxDto.getZqs().put("sf", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> sf=sjxxStatisService.getSjxxBySfPie(sjxxDto);
			map.put("sf", sf);
		}else if("sf_day".equals(method)) {
			setDateByDay(sjxxDto,0);
			sjxxDto.setTjzq("day");
			sjxxDto.getZqs().put("sf", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> sf=sjxxStatisService.getSjxxBySfPie(sjxxDto);
			map.put("sf", sf);
		}else if("hbmc_year".equals(method)) {
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.setTjzq("year");
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("hbmc", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String,Object>> hbmc=sjxxStatisService.getSjxxInJcdw(sjxxDto);
			map.put("hbmc", hbmc);
		}else if("hbmc_mon".equals(method)) {
			setDateByMonth(sjxxDto,-6);
			sjxxDto.setTjzq("mon");
			sjxxDto.setTj("mon");
			sjxxDto.getZqs().put("hbmc", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String,Object>> hbmc=sjxxStatisService.getSjxxInJcdw(sjxxDto);
			map.put("hbmc", hbmc);
		}else if("hbmc_week".equals(method)) {
			setDateByWeek(sjxxDto);
			sjxxDto.setTjzq("week");
			sjxxDto.setTj("week");
			sjxxDto.getZqs().put("hbmc", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> hbmc=sjxxStatisService.getSjxxInJcdw(sjxxDto);
			map.put("hbmc", hbmc);
		}else if("hbmc_day".equals(method)) {
			setDateByDay(sjxxDto,-6);
			sjxxDto.setTjzq("day");
			sjxxDto.setTj("day");
			sjxxDto.getZqs().put("hbmc", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> hbmc=sjxxStatisService.getSjxxInJcdw(sjxxDto);
			map.put("hbmc", hbmc);
		}
		map.put("searchData",sjxxDto);
		return map;
	}
	
	/**
	 * 统计点击切换周期
	 * @param request
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pagedataOperational_StatisWsj_click")
	@ResponseBody
	public Map<String,Object> pagedataOperational_StatisWsj_click(HttpServletRequest request,SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		String method = request.getParameter("method");
		if(StringUtil.isBlank(method)) {
			return map;
		}else if("jcdw_year".equals(method)) {
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.setTjzq("year");
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String,Object>> jcdw=sjxxStatisService.getSjxxInJcdwByWsj(sjxxDto);
			map.put("jcdw", jcdw);
		}else if("jcdw_mon".equals(method)) {
			setDateByMonth(sjxxDto,-6);
			sjxxDto.setTjzq("mon");
			sjxxDto.setTj("mon");
			sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String,Object>> jcdw=sjxxStatisService.getSjxxInJcdwByWsj(sjxxDto);
			map.put("jcdw", jcdw);
		}else if("jcdw_week".equals(method)) {
			setDateByWeek(sjxxDto);
			sjxxDto.setTjzq("week");
			sjxxDto.setTj("week");
			sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> jcdw=sjxxStatisService.getSjxxInJcdwByWsj(sjxxDto);
			map.put("jcdw", jcdw);
		}else if("jcdw_day".equals(method)) {
			setDateByDay(sjxxDto,-6);
			sjxxDto.setTjzq("day");
			sjxxDto.setTj("day");
			sjxxDto.getZqs().put("jcdw", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> jcdw=sjxxStatisService.getSjxxInJcdwByWsj(sjxxDto);
			map.put("jcdw", jcdw);
		}
		sjxxDto.setJsrq(null);
		List<SjxxDto> list=sjxxStatisService.getSjxxInJcdwByWsjList(sjxxDto);
		map.put("sjxxDtoList",list);
		map.put("searchData",sjxxDto);
		return map;
	}
	
	/**
	 * 点击柱状图操作饼状图
	 * @param request
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pagedataClickCategoryEcharts")
	@ResponseBody
	public Map<String,Object> pagedataClickCategoryEcharts(HttpServletRequest request,SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		String suffix=request.getParameter("suffix");
		String index= request.getParameter("index");
		if(StringUtil.isBlank(suffix)){
			return map;
		}else if("Y".equals(suffix)) {
			sjxxDto.setJsrqYstart(sjxxDto.getJsrq());
			sjxxDto.setJsrqYend(sjxxDto.getJsrq());
			sjxxDto.setJsrq(null);
			sjxxDto.getZqs().put(index, sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
		}else if("M".equals(suffix)) {
			sjxxDto.setJsrqMstart(sjxxDto.getJsrq());
			sjxxDto.setJsrqMend(sjxxDto.getJsrq());
			sjxxDto.setJsrq(null);
			sjxxDto.getZqs().put(index, sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
		}else if("W".equals(suffix)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(sdf.parse(sjxxDto.getJsrq()));
				calendar.add(Calendar.DATE, -6);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sjxxDto.setJsrqstart(sdf.format(calendar.getTime()));
			sjxxDto.setJsrqend(sjxxDto.getJsrq());
			sjxxDto.setJsrq(null);
			sjxxDto.getZqs().put(index, sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		}else if("D".equals(suffix)) {
			sjxxDto.setJsrqstart(sjxxDto.getJsrq());
			sjxxDto.setJsrqend(sjxxDto.getJsrq());
			sjxxDto.setJsrq(null);
			sjxxDto.getZqs().put(index, sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		}
		List<Map<String,Object>> list=sjxxStatisService.getSjxxBySfPie(sjxxDto);
		map.put(index,list);
		map.put("searchData",sjxxDto);
		return map;
	}
	
	/**		
	 * 
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pageListOperational_Statis_sjhb")
	public ModelAndView pageListOperational_Statis_sjhb(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/operational/operational_one_sjhb");
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}
	
	/**
	 * 按照省份数据查询
	 * @param request
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pagedataOperational_Statis_sjhb_echarts")
	@ResponseBody
	public Map<String,Object> pagedataOperational_Statis_sjhb_echarts(HttpServletRequest request,SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		String tjzq=sjxxDto.getTjzq();
		SjxxDto sjxxDto_t=new SjxxDto();
		sjxxDto_t.setJcdw(sjxxDto.getJcdw());
		sjxxDto_t.setSf(sjxxDto.getSf());
		
		if("year".equals(tjzq)) {
			setDateByYear(sjxxDto_t,-6,false);
			sjxxDto_t.setTjzq("year");
			sjxxDto_t.setTj("year");
			sjxxDto.getZqs().put("sjsf", sjxxDto_t.getJsrqYstart() + "~" + sjxxDto_t.getJsrqYend());
			List<Map<String,Object>> sjsf=sjxxStatisService.getSjxxInJcdw(sjxxDto_t);
			map.put("sjsf", sjsf);
		}else if("mon".equals(tjzq)) {
			setDateByMonth(sjxxDto_t,-6);
			sjxxDto_t.setTjzq("mon");
			sjxxDto_t.setTj("mon");
			sjxxDto.getZqs().put("sjsf", sjxxDto_t.getJsrqMstart() + "~" + sjxxDto_t.getJsrqMend());
			List<Map<String,Object>> sjsf=sjxxStatisService.getSjxxInJcdw(sjxxDto_t);
			map.put("sjsf", sjsf);
		}else if("week".equals(tjzq)) {
			setDateByWeek(sjxxDto_t);
			sjxxDto_t.setTjzq("week");
			sjxxDto_t.setTj("week");
			sjxxDto.getZqs().put("sjsf", sjxxDto_t.getJsrqstart() + "~" + sjxxDto_t.getJsrqend());
			List<Map<String,Object>> sjsf=sjxxStatisService.getSjxxInJcdw(sjxxDto_t);
			map.put("sjsf", sjsf);
		}else if("day".equals(tjzq)) {
			setDateByDay(sjxxDto_t,-6);
			sjxxDto_t.setTjzq("day");
			sjxxDto_t.setTj("day");
			sjxxDto.getZqs().put("sjsf", sjxxDto_t.getJsrqstart() + "~" + sjxxDto_t.getJsrqend());
			List<Map<String,Object>> sjsf=sjxxStatisService.getSjxxInJcdw(sjxxDto_t);
			map.put("sjsf", sjsf);
		}
		
		if("year".equals(tjzq)) {
			setDateByYear(sjxxDto_t,-6,false);
			sjxxDto_t.setTjzq("year");
			sjxxDto_t.setTj("year");
			sjxxDto.getZqs().put("sjhb", sjxxDto_t.getJsrqYstart() + "~" + sjxxDto_t.getJsrqYend());
			List<Map<String,Object>> sjhb=sjxxStatisService.getSjxxBySfPie(sjxxDto_t);
			map.put("sjhb", sjhb);
		}else if("mon".equals(tjzq)) {
			setDateByMonth(sjxxDto_t,-6);
			sjxxDto_t.setTjzq("mon");
			sjxxDto_t.setTj("mon");
			sjxxDto.getZqs().put("sjhb", sjxxDto_t.getJsrqMstart() + "~" + sjxxDto_t.getJsrqMend());
			List<Map<String,Object>> sjhb=sjxxStatisService.getSjxxBySfPie(sjxxDto_t);
			map.put("sjhb", sjhb);
		}else if("week".equals(tjzq)) {
			setDateByWeek(sjxxDto_t);
			sjxxDto_t.setTjzq("week");
			sjxxDto.getZqs().put("sjhb", sjxxDto_t.getJsrqstart() + "~" + sjxxDto_t.getJsrqend());
			List<Map<String,Object>> sjhb=sjxxStatisService.getSjxxBySfPie(sjxxDto_t);
			map.put("sjhb", sjhb);
		}else if("day".equals(tjzq)) {
			setDateByDay(sjxxDto_t,0);
			sjxxDto_t.setTjzq("day");
			sjxxDto.getZqs().put("sjhb", sjxxDto_t.getJsrqstart() + "~" + sjxxDto_t.getJsrqend());
			List<Map<String,Object>> sjhb=sjxxStatisService.getSjxxBySfPie(sjxxDto_t);
			map.put("sjhb", sjhb);
		}
		map.put("sjhb_tjzq", sjxxDto_t.getTjzq());
		map.put("sjsf_tjzq", sjxxDto_t.getTjzq());
		map.put("searchData", sjxxDto);
		return map;
	}
	
	/**
	 * 点击切换统计周期
	 * @param request
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/pagedataChangeTjzq_Statis_sjhb_echarts")
	@ResponseBody
	public Map<String,Object> pagedataChangeTjzq_Statis_sjhb_echarts(HttpServletRequest request,SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		String method = request.getParameter("method");
		if(StringUtil.isBlank(method)) {
			return map;
		}else if("sjsf_year".equals(method)) {
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.setTjzq("year");
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("sjsf", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String,Object>> sjsf=sjxxStatisService.getSjxxInJcdw(sjxxDto);
			map.put("sjsf", sjsf);
		}else if("sjsf_mon".equals(method)) {
			setDateByMonth(sjxxDto,-6);
			sjxxDto.setTjzq("mon");
			sjxxDto.setTj("mon");
			sjxxDto.getZqs().put("sjsf", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String,Object>> sjsf=sjxxStatisService.getSjxxInJcdw(sjxxDto);
			map.put("sjsf", sjsf);
		}else if("sjsf_week".equals(method)) {
			setDateByWeek(sjxxDto);
			sjxxDto.setTjzq("week");
			sjxxDto.setTj("week");
			sjxxDto.getZqs().put("sjsf", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> sjsf=sjxxStatisService.getSjxxInJcdw(sjxxDto);
			map.put("sjsf", sjsf);
		}else if("sjsf_day".equals(method)) {
			setDateByDay(sjxxDto,-6);
			sjxxDto.setTjzq("day");
			sjxxDto.setTj("day");
			sjxxDto.getZqs().put("sjsf", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> sjsf=sjxxStatisService.getSjxxInJcdw(sjxxDto);
			map.put("sjsf", sjsf);
		}else if("sjhb_year".equals(method)) {
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.setTjzq("year");
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("sjhb", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String,Object>> sjhb=sjxxStatisService.getSjxxBySfPie(sjxxDto);
			map.put("sjhb", sjhb);
		}else if("sjhb_mon".equals(method)) {
			setDateByMonth(sjxxDto,-6);
			sjxxDto.setTjzq("mon");
			sjxxDto.setTj("mon");
			sjxxDto.getZqs().put("sjhb", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String,Object>> sjhb=sjxxStatisService.getSjxxBySfPie(sjxxDto);
			map.put("sjhb", sjhb);
		}else if("sjhb_week".equals(method)) {
			setDateByWeek(sjxxDto);
			sjxxDto.setTjzq("week");
			sjxxDto.getZqs().put("sjhb", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> sjhb=sjxxStatisService.getSjxxBySfPie(sjxxDto);
			map.put("sjhb", sjhb);
		}else if("sjhb_day".equals(method)) {
			setDateByDay(sjxxDto,0);
			sjxxDto.setTjzq("day");
			sjxxDto.getZqs().put("sjhb", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,Object>> sjhb=sjxxStatisService.getSjxxBySfPie(sjxxDto	);
			map.put("sjhb", sjhb);
		}
		map.put("searchData",sjxxDto);
		return map;
	}
	
	/**
	 * 统计送检合作伙伴页面
	 * @param request
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/statistics/pageStatisticsOperational_Statis_hbmc")
	public ModelAndView pageStatisticsOperational_Statis_hbmc(HttpServletRequest request,SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/operational/operational_one_hbmc");
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}
	
	/**
	 * 统计送检合作伙伴数据
	 * @param request
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value="/statistics/pagedataOperational_Statis_hbmc_echarts")
	@ResponseBody
	public Map<String,Object> pagedataOperational_Statis_hbmc_echarts(HttpServletRequest request,SjxxDto sjxxDto){
		Map<String,Object> map= new HashMap<>();
		String tjzq=sjxxDto.getTjzq();
		SjxxDto sjxxDto_t=new SjxxDto();
		sjxxDto_t.setJcdw(sjxxDto.getJcdw());
		sjxxDto_t.setSf(sjxxDto.getSf());
		sjxxDto_t.setDb(sjxxDto.getDb());
		
		if("year".equals(tjzq)) {
			setDateByYear(sjxxDto_t,-6,false);
			sjxxDto_t.setTjzq("year");
			sjxxDto_t.setTj("year");
			sjxxDto.getZqs().put("hbmc", sjxxDto_t.getJsrqYstart() + "~" + sjxxDto_t.getJsrqYend());
			List<Map<String,Object>> hbmc=sjxxStatisService.getSjxxInJcdw(sjxxDto_t);
			map.put("hbmc", hbmc);
		}else if("mon".equals(tjzq)) {
			setDateByMonth(sjxxDto_t,-6);
			sjxxDto_t.setTjzq("mon");
			sjxxDto_t.setTj("mon");
			sjxxDto.getZqs().put("hbmc", sjxxDto_t.getJsrqMstart() + "~" + sjxxDto_t.getJsrqMend());
			List<Map<String,Object>> hbmc=sjxxStatisService.getSjxxInJcdw(sjxxDto_t);
			map.put("hbmc", hbmc);
		}else if("week".equals(tjzq)) {
			setDateByWeek(sjxxDto_t);
			sjxxDto_t.setTjzq("week");
			sjxxDto_t.setTj("week");
			sjxxDto.getZqs().put("hbmc", sjxxDto_t.getJsrqstart() + "~" + sjxxDto_t.getJsrqend());
			List<Map<String,Object>> hbmc=sjxxStatisService.getSjxxInJcdw(sjxxDto_t);
			map.put("hbmc", hbmc);
		}else if("day".equals(tjzq)) {
			setDateByDay(sjxxDto_t,-6);
			sjxxDto_t.setTjzq("day");
			sjxxDto_t.setTj("day");
			sjxxDto.getZqs().put("hbmc", sjxxDto_t.getJsrqstart() + "~" + sjxxDto_t.getJsrqend());
			List<Map<String,Object>> hbmc=sjxxStatisService.getSjxxInJcdw(sjxxDto_t);
			map.put("hbmc", hbmc);
		}
		map.put("hbmc_tjzq", sjxxDto_t.getTjzq());
		map.put("searchData", sjxxDto);
		return map;
	}

	/**
	 * 实验室统计页面
	 * @return
	 */
	@RequestMapping("/statistics/pageStatisticsLaboratoryStatistics")
	public ModelAndView pageStatisticsLaboratoryStatistics() {
		ModelAndView mav=new ModelAndView("wechat/statistics/statistics_laboratory");
		//获取用户信息
		User user=getLoginInfo();
		String jsid = user.getDqjs();
		//获取当前用户当前角色的检测单位和检测单位名称
		List<Map<String, String>> lsjcdwList = sjxxService.getJsjcdwByjsid(jsid);
		List<Map<String, Object>> jcdwList = new ArrayList<>();
		if(lsjcdwList !=null && lsjcdwList.size()>0){
			for (Map<String, String> map : lsjcdwList) {
				Map<String,Object> jcdwMap= new HashMap<>();
				if (StringUtil.isNotBlank(map.get("jcdw"))){
					jcdwMap.put("jcdw",map.get("jcdw"));
					JcsjDto jcdwmcDto = jcsjService.getDtoById(map.get("jcdw"));
					if (jcdwmcDto!=null) {
						jcdwMap.put("jcdwmc",jcsjService.getDtoById(map.get("jcdw")).getCsmc());
						jcdwList.add(jcdwMap);
					}
				}
			}
		}
		//获取检测单位详情
		//获取复测文库列表
		List<SjxxDto> fcwkList = sjxxService.getYbbhAndYyToday();


		mav.addObject("jcdwList",jcdwList);
		mav.addObject("fcwkList",fcwkList);
		return mav;
	}

	/**
	 * 实验室统计详情页面
	 * @return
	 */
	@RequestMapping("/statistics/pageStatisticsLaboratoryStatisticsDetail")
	public ModelAndView pageStatisticsLaboratoryStatisticsDetail(String jcdw) {

        return new ModelAndView("wechat/statistics/statistics_laboratory_detail");
	}
}
