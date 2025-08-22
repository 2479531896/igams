package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.util.WechatCommonUtils;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.HbqxDto;
import com.matridx.igams.wechat.dao.entities.SjbgsmDto;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjnyxDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxjgDto;
import com.matridx.igams.wechat.dao.entities.SjybztDto;
import com.matridx.igams.wechat.dao.entities.SjzmjgDto;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import com.matridx.igams.wechat.service.svcinterface.IDdyhService;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjnyxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxResStatisticService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxStatisticService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxjgService;
import com.matridx.igams.wechat.service.svcinterface.ISjzmjgService;
import com.matridx.igams.wechat.service.svcinterface.IWxyhService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ws")
public class MiniWsStatisticsController extends BaseController{

	@Autowired
	WechatCommonUtils wechatCommonUtils;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private IDdyhService ddyhService;
	@Autowired
	private IHbqxService hbqxService;
	@Autowired
	ISjxxService sjxxService;
	@Autowired
	private IFjcfbService fjcfbService;
	@Autowired
	private ISjbgsmService sjbgsmService;
	@Autowired
	ISjhbxxService sjhbxxService;
	@Autowired
	IFjsqService fjsqService;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	IWxyhService wxyhService;
	@Autowired
	private ISjnyxService sjnyxService;
	@Autowired
	private ISjxxStatisticService SjxxStatisticService;
	@Autowired
	private ISjzmjgService sjzmjgService;
	@Autowired
	private ISjxxjgService sjxxjgService;
	@Autowired
	ISjxxResStatisticService sjxxResStatisticService;
	
	/**
	 * 查询用户ID，若无返回录入人员列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/isLrryList")
	@ResponseBody
	public Map<String,Object> isLrryList(WxyhDto wxyhDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		List<String> lrrylist = new ArrayList<>();
		if(StringUtil.isNotBlank(wxyhDto.getWxid())){
			List<WxyhDto> wxyhlist = wxyhService.getListBySameId(wxyhDto);
			if(wxyhlist != null && wxyhlist.size() > 0){
				for (int i = 0; i < wxyhlist.size(); i++) {
					if(StringUtil.isNotBlank(wxyhlist.get(i).getXtyhid())){
						map.put("yhid", wxyhlist.get(i).getXtyhid());
					}
					lrrylist.add(wxyhlist.get(i).getWxid());
				}
			}else{
				lrrylist.add(wxyhDto.getWxid());
			}
		}
		map.put("lrrylist", lrrylist);
		return map;
	}
	
	
	/**
	 * 根据录入人员获取送检表的合作伙伴
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getDbsByLrrys")
	@ResponseBody
	public List<String> getDbsByLrrys(SjxxDto sjxxDto ,HttpServletRequest request){
        return sjxxService.getDbsByLrrys(sjxxDto);
	}
	
	/**
	 * 设置按年查询的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几年
	 * @param isBgFlag 是否设置报告日期标记
	 */
	private void setDateByYear(SjxxDto sjxxDto,int length,boolean isBgFlag) {
		setDateByYear(sjxxDto,sjxxDto.getJsrqstart(),length,isBgFlag);
	}
	
	/**
	 * 设置按年查询的日期
	 * @param sjxxDto
	 * @param date 标准日期
	 * @param length 长度。要为负数，代表向前推移几年
	 * @param isBgFlag 是否设置报告日期标记
	 */
	private void setDateByYear(SjxxDto sjxxDto,String date,int length,boolean isBgFlag) {
		SimpleDateFormat monthSdf=new SimpleDateFormat("yyyy");
		
		Calendar calendar=Calendar.getInstance();
		
		if(StringUtil.isNotBlank(date)) {
			Date jsDate = DateUtils.parseDate("yyyy-MM-dd",date);
			calendar.setTime(jsDate);
		}
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
		sjxxDto.setJsrqstart(null);
		sjxxDto.setJsrqend(null);
	}
	
	/**
	 * 设置按月查询的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几月
	 */
	private void setDateByMonth(SjxxDto sjxxDto,int length) {
		setDateByMonth(sjxxDto,sjxxDto.getJsrqstart(),length);
	}
	
	/**
	 * 设置按月查询的日期
	 * @param sjxxDto
	 * @param date 标准日期
	 * @param length 长度。要为负数，代表向前推移几月
	 */
	private void setDateByMonth(SjxxDto sjxxDto,String date,int length) {
		SimpleDateFormat monthSdf=new SimpleDateFormat("yyyy-MM");
		
		Calendar calendar=Calendar.getInstance();
		
		if(StringUtil.isNotBlank(date)) {
			Date jsDate = DateUtils.parseDate("yyyy-MM-dd",date);
			calendar.setTime(jsDate);
		}
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
		
		sjxxDto.setJsrqstart(null);
		sjxxDto.setJsrqend(null);
	}
	
	/**
	 * 设置周的日期
	 * @param sjxxDto
	 * 			length 长度。要为负数，代表向前推移几天
	 */
	private void setDateByWeek(SjxxDto sjxxDto) {
		setDateByWeek(sjxxDto,sjxxDto.getJsrqstart(),1);
	}
	
	/**
	 * 设置周的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几天
	 * @param date 标准日期
	 * @param length 几周
	 */
	private void setDateByWeek(SjxxDto sjxxDto,String date,int length) {
		SimpleDateFormat daySdf=new SimpleDateFormat("yyyy-MM-dd");
		Date jsDate = DateUtils.parseDate("yyyy-MM-dd",date);
		if(StringUtil.isBlank(date)) {
			int dayOfWeek = DateUtils.getWeek(jsDate);
			//往后的周
			if(length >= 0) {
				if (dayOfWeek <= 2)
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek - 7)));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek-1 + 7*(length-1))));
				} else
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek )));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, 6 - dayOfWeek + 7*(length-1))));
				}
			}else {
				if (dayOfWeek <= 2)
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek - 7*length)));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek-1)));
				} else
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek - 7*(length-1))));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, 6 - dayOfWeek)));
				}
			}
		}else if( length > 1 || length < -1) {
			int dayOfWeek = DateUtils.getWeek(jsDate);
			//往后的周
			if(length >= 0) {
				if (dayOfWeek <= 2)
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek - 7)));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek-1 + 7*(length-1))));
				} else
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek )));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, 6 - dayOfWeek + 7*(length-1))));
				}
			}else {
				if (dayOfWeek <= 2)
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek + 7*length)));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek-1)));
				} else
				{
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek + 7*(length+1))));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, 6 - dayOfWeek)));
				}
			}
		}
		sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
		sjxxDto.setBgrqend(sjxxDto.getJsrqend());
	}
	
	/**
	 * 设置按天查询的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几天
	 */
	private void setDateByDay(SjxxDto sjxxDto,int length) {
		setDateByDay(sjxxDto,sjxxDto.getJsrqstart(),length);
	}
	
	/**
	 * 设置按天查询的日期
	 * @param sjxxDto
	 * @param length 长度。要为负数，代表向前推移几天
	 */
	private void setDateByDay(SjxxDto sjxxDto,String date,int length) {
		SimpleDateFormat daySdf=new SimpleDateFormat("yyyy-MM-dd");
		
		if(StringUtil.isNotBlank(date)) {
			if(length >=0 ) {
				sjxxDto.setJsrqstart(date);
				sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(DateUtils.parseDate("yyyy-MM-dd",date), length)));
			}else {
				sjxxDto.setJsrqend(date);
				sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(DateUtils.parseDate("yyyy-MM-dd",date), length)));
			}
		}else if(length ==0){
			sjxxDto.setJsrqstart(date);
			sjxxDto.setJsrqend(sjxxDto.getJsrqstart());
		}
		sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
		sjxxDto.setBgrqend(sjxxDto.getJsrqend());
	}
	
	/*------------------------------------------  个人日报   -------------------------------------------------*/
	
	/**
	 * 微信用户日报查询数据
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/getDailyModel")
	@ResponseBody
	public Map<String, Object> getDailyModel(HttpServletRequest request){
		String str = request.getParameter("sjxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjxxDto sjxxDto = JSONObject.toJavaObject(parseObject, SjxxDto.class);
		Map<String, Object> map = new HashMap<>();
		boolean checkSign=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getJsrq(),request);
		if(checkSign) {
			if(StringUtil.isNotBlank(sjxxDto.getYhid()) || (sjxxDto.getLrrys() != null && sjxxDto.getLrrys().size() > 0)) {
				if(StringUtil.isNotBlank(sjxxDto.getYhid())){
					boolean sfdwxd =ddyhService.getDwxd(sjxxDto.getYhid());
					if(sfdwxd) {
                        List<HbqxDto> hbqxList=hbqxService.getHbxxByYhid(sjxxDto.getYhid());
						List<HbqxDto> hbmcqxList=hbqxService.getHbxxGroupByYhid(sjxxDto.getYhid());
						if(hbqxList!=null&& hbqxList.size()>0) {
							List<String> strList= new ArrayList<>();
							for (int i = 0; i < hbqxList.size(); i++){
								strList.add(hbqxList.get(i).getHbmc());
							}
							sjxxDto.setDbs(strList);
						}else {
							List<String> strList= new ArrayList<>();
							strList.add("matridx-default");
							sjxxDto.setDbs(strList);
						}
						if(hbmcqxList!=null&& hbmcqxList.size()>0) {
							List<String> strList= new ArrayList<>();
							for (int i = 0; i < hbmcqxList.size(); i++){
								strList.add(hbmcqxList.get(i).getHbmc());
							}
							sjxxDto.setDbmcs(strList);
						}else {
							List<String> strList= new ArrayList<>();
							strList.add("matridx-default");
							sjxxDto.setDbmcs(strList);
						}
						//合作伙伴统计
						List<Map<String,String>>YbxxBySjh_daily_Map = SjxxStatisticService.getYbxxBySjhbDaily(sjxxDto);
						if(YbxxBySjh_daily_Map.size()>39) {
							List<Map<String,String>> asc_YbxxBySjh_daily_Map= new ArrayList<>();
							asc_YbxxBySjh_daily_Map.addAll(YbxxBySjh_daily_Map.subList(0,39));
							map.put("sjhb", asc_YbxxBySjh_daily_Map);
							Collections.reverse(YbxxBySjh_daily_Map); // 倒序排列
						}else {
							map.put("sjhb", YbxxBySjh_daily_Map);
						}
					}else{
						//合作伙伴统计
						List<Map<String,String>> sjhbMap = sjxxService.getYbxxByTjmc(sjxxDto);
						if(sjhbMap.size()>39) {
							List<Map<String,String>> asc_YbxxBySjh_daily_Map= new ArrayList<>();
							asc_YbxxBySjh_daily_Map.addAll(sjhbMap.subList(0,39));
							map.put("sjhb", asc_YbxxBySjh_daily_Map);
							Collections.reverse(sjhbMap); // 倒序排列
						}else {
							map.put("sjhb", sjhbMap);
						}	
					}
				}else if(sjxxDto.getLrrys() != null && sjxxDto.getLrrys().size() > 0){
					List<String> dbs = getDbsByLrrys(sjxxDto, request);
					if(dbs != null && dbs.size() > 0){
						sjxxDto.setDbs(dbs);
					}else {
						List<String> strList= new ArrayList<>();
						strList.add("matridx-default");
						sjxxDto.setDbs(strList);
					}
				}
				//标本信息
				List<Map<String,String>> ybxx_daily_Map=SjxxStatisticService.getYbxxDaily(sjxxDto);
				map.put("ybxx", ybxx_daily_Map);
				//ResFirst™标本信息
				List<Map<String,String>> rybxxMap=sjxxResStatisticService.getRYbxxByDay(sjxxDto);
				Map<String, Object> rfssyMap = new HashMap<>();
				SjxxDto sjxxDto_t = new SjxxDto();
				sjxxDto_t.setJsrq(sjxxDto.getJsrq());
				sjxxDto_t.setCskz3("IMP_REPORT_RFS_TEMEPLATE");
				Map<String,Integer> rfssjfj = sjxxResStatisticService.getSjFjNum(sjxxDto_t);
				rfssyMap.put("rybxx", rybxxMap);
				rfssyMap.put("rfssjfj",rfssjfj);
				map.put("rybxx", rfssyMap);
				//ResFirst™各周期标本数
				Map<String, String> rfszqbbs = sjxxResStatisticService.getLifeCount(sjxxDto);
				map.put("rfszqbbs",rfszqbbs);
				//当日ResFirst™各周期标本用时
				SjxxDto sjxxDto_r = new SjxxDto();
				sjxxDto_r.setJsrq(sjxxDto.getJsrq());
				sjxxDto_r.setLrsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
				List<Map<String, String>> rfsbbys = sjxxResStatisticService.getLifeTimeCount(sjxxDto_r);
				map.put("rfsbbys",rfsbbys);
				//统计复检申请
				FjsqDto fjsqDto=new FjsqDto();
				fjsqDto.setSjhbs(sjxxDto.getDbs());
				fjsqDto.setLrsj(sjxxDto.getJsrq());
				List<Map<String,String>> fjsq_daily_Map=SjxxStatisticService.getFjsqDaily(fjsqDto);
				map.put("fjsq", fjsq_daily_Map);
				//统计废弃标本
				List<Map<String,String>> fqybMap_daily_Map=SjxxStatisticService.getFqybByYbztDaily(sjxxDto);
				map.put("fqyb", fqybMap_daily_Map);
				//统计报高阳性率
				List<Map<String, String>> possiblelist_daily=SjxxStatisticService.getJyjgDaily(sjxxDto);
				map.put("possiblelist",possiblelist_daily);
				//送检清单
				List<SjxxDto> sjxxList_daily=SjxxStatisticService.getSjxxListDaily(sjxxDto);
				map.put("sjxxList",sjxxList_daily);
				//检测项目个数
	            List<Map<String,String>> jcxmDRList_daily=SjxxStatisticService.getJcxmSumDaily(sjxxDto);
	            map.put("jcxmnum",jcxmDRList_daily);
	            //收费标本里边检测项目条数
	            List<Map<String,String>> jcxmTypeList_daily=SjxxStatisticService.getJcxmInSfsfDaily(sjxxDto);
	            map.put("jcxmType",jcxmTypeList_daily);
				map.put("searchData", sjxxDto);
					
			}
			return map;
		}
		return map;
	}
	
	/**
	 * 微信用户日报查询复检列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/recheckListDaily")
	@ResponseBody
	public Map<String, Object> recheckListDaily(HttpServletRequest request){
		Map<String, Object> map= new HashMap<>();
		String str = request.getParameter("fjsqDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		FjsqDto fjsqDto = JSONObject.toJavaObject(parseObject, FjsqDto.class);
		if("null".equals(fjsqDto.getLrsj())) {
			fjsqDto.setLrsj("");
		}
		if(fjsqDto.getYhid()!=null) {
			//根据用户
			List<HbqxDto> hbqxList=hbqxService.getHbxxByYhid(fjsqDto.getYhid());
			if(hbqxList!=null&& hbqxList.size()>0) {
				List<String> strList= new ArrayList<>();
				for (int i = 0; i < hbqxList.size(); i++){
					strList.add(hbqxList.get(i).getHbmc());
				}
				fjsqDto.setSjhbs(strList);
				List<FjsqDto> fjsqList=fjsqService.getPagedStatisRecheck(fjsqDto);
				map.put("rows", fjsqList);
			}
		}
//		else{
//			//根据录入人员
//
//		}
		map.put("total", fjsqDto.getTotalNumber());
		return map;
	}
	
	/**
	 * 微信统计阴阳性列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/positiveViewDaily")
	@ResponseBody
	public Map<String, Object> positiveViewDaily(HttpServletRequest request) {
		Map<String, Object> map= new HashMap<>();
		String str = request.getParameter("sjwzxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjwzxxDto sjwzxxDto = JSONObject.toJavaObject(parseObject, SjwzxxDto.class);
		if(sjwzxxDto.getYhid()!=null) {
			List<HbqxDto> hbqxList=hbqxService.getHbxxByYhid(sjwzxxDto.getYhid());
			if(hbqxList!=null&& hbqxList.size()>0) {
				List<String> strList= new ArrayList<>();
				for (int i = 0; i < hbqxList.size(); i++){
					strList.add(hbqxList.get(i).getHbmc());
				}
				sjwzxxDto.setSjhbs(strList);
				List<SjxxDto> sjxxList=sjxxService.getListPositive(sjwzxxDto);
				map.put("status","success");
				map.put("sjxxList",sjxxList);
				map.put("sjwzxxDto", JSONObject.toJSONString(sjwzxxDto));
				return map;
			}
		}
		map.put("status","fail");
		return map;
	}
	
	/**
	* 查看送检信息
	* @param request
	* @return
	*/
	@RequestMapping(value="/miniprogram/sjxxViewDaily")
	@ResponseBody
	public Map<String, Object> viewSjxx(HttpServletRequest request) {
		Map<String, Object> map= new HashMap<>();
		String str = request.getParameter("sjxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjxxDto sjxxDto = JSONObject.toJavaObject(parseObject, SjxxDto.class);
		
		List<SjnyxDto> SjnyxDtos = sjnyxService.getNyxBySjid(sjxxDto);
		SjxxDto t_sjxxDto = sjxxService.getDto(sjxxDto);
		if("1".equals(t_sjxxDto.getYyxxCskz1())) {
			t_sjxxDto.setHospitalname(t_sjxxDto.getHospitalname()+"-"+t_sjxxDto.getSjdwmc());
		}
		t_sjxxDto.setYhid(sjxxDto.getYhid());
		t_sjxxDto.setLrrys(sjxxDto.getLrrys());
		List<SjwzxxDto> sjwzxxDtos =sjxxService.selectWzxxBySjid(t_sjxxDto);
		if(sjwzxxDtos != null && sjwzxxDtos.size() > 0) {
			String xpxx = sjwzxxDtos.get(0).getXpxx();//由于一个标本中的物种芯片信息相同，取其一
			map.put("Xpxx", xpxx);
		}
//		if(("Z6").equals(t_sjxxDto.getCskz1()) || ("Z12").equals(t_sjxxDto.getCskz1()) || ("Z18").equals(t_sjxxDto.getCskz1()) || ("F").equals(t_sjxxDto.getCskz1())) {
		if(("Z").equals(t_sjxxDto.getCskz1()) ||("Z6").equals(t_sjxxDto.getCskz1()) || ("F").equals(t_sjxxDto.getCskz1())) {
        	SjzmjgDto sjzmjgDto=new SjzmjgDto();
        	sjzmjgDto.setSjid(sjxxDto.getSjid());
			List<SjzmjgDto> sjzmList = sjzmjgService.getDtoList(sjzmjgDto);
        	map.put("sjzmList", sjzmList);
        	map.put("KZCS",t_sjxxDto.getCskz1());
        }
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());
		fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionPdfYwlxs());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		for (int i = 0; i < fjcfbDtos.size(); i++) {
			fjcfbDtos.get(i).setSign(commonService.getSign(fjcfbDtos.get(i).getFjid()));
		}
		map.put("fjcfbDtos",fjcfbDtos);

		fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionWordYwlxs());
		List<FjcfbDto> zhwj_fjcfbDtos = fjcfbService.selectzhpdf(fjcfbDto);
		for (int i = 0; i < zhwj_fjcfbDtos.size(); i++) {
			zhwj_fjcfbDtos.get(i).setSign(commonService.getSign(zhwj_fjcfbDtos.get(i).getFjid()));
		}
		fjcfbDto.setYwlxs(null);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		for (int i = 0; i < t_fjcfbDtos.size(); i++) {
			t_fjcfbDtos.get(i).setSign(commonService.getSign(t_fjcfbDtos.get(i).getFjid()));
		}
		SjbgsmDto sjbgsmdto=new SjbgsmDto();
		sjbgsmdto.setSjid(sjxxDto.getSjid());
		List<SjbgsmDto> sjbgsmList = sjbgsmService.selectSjbgBySjid(sjbgsmdto);
		
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE});
		List<JcsjDto> jcxmlist=jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> t_jcxmlist= new ArrayList<>();//用于结果页
		List<JcsjDto> c_jcxmlist= new ArrayList<>();//用于详细页
		
		if(jcxmlist!=null && jcxmlist.size()>0) {
			for(int i=0;i<jcxmlist.size();i++) {
				boolean wz_sftj=false;//判断对应该检测项目的物种信息是否存在，若存在一个则添加该项目
				if(sjwzxxDtos!=null && sjwzxxDtos.size()>0) {
					for(int j=0;j<sjwzxxDtos.size();j++) {
						if(sjwzxxDtos.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
							wz_sftj=true;
							break;
						}
					}
				}
				if(wz_sftj) 
					c_jcxmlist.add(jcxmlist.get(i));
				
				boolean sftj=false;//判断对应该检测项目的报告说明和附件是否存在，若其中一个存在添加该项目
				if(sjbgsmList!=null && sjbgsmList.size()>0) {
					for(int j=0;j<sjbgsmList.size();j++) {
						if(sjbgsmList.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
							sftj=true;
							break;
						}
					}
				}
				if(t_fjcfbDtos!=null && t_fjcfbDtos.size()>0) {
					for(int j=0;j<t_fjcfbDtos.size();j++) {
						if(t_fjcfbDtos.get(j).getYwlx().equals((jcxmlist.get(i).getCskz3()+jcxmlist.get(i).getCskz1()))) {
							sftj=true;
							break;
						}
					}
				}
				if(fjcfbDtos!=null && fjcfbDtos.size()>0) {
					for(int j=0;j<fjcfbDtos.size();j++) {
						if(fjcfbDtos.get(j).getYwlx().equals((jcxmlist.get(i).getCskz3()+"_"+jcxmlist.get(i).getCskz1()))) {
							sftj=true;
							break;
						}
					}
				}
				if(sftj)
					t_jcxmlist.add(jcxmlist.get(i));
			}
		}
			
		SjxxjgDto sjxxjgDto=new SjxxjgDto();
		sjxxjgDto.setSjid(sjxxDto.getSjid());
		List<SjxxjgDto> getJclxCount = sjxxjgService.getJclxCount(sjxxjgDto);
		map.put("SjnyxDtos", SjnyxDtos);
		map.put("zhwj_fjcfbDtos", zhwj_fjcfbDtos);
		map.put("sjbgsmList",sjbgsmList);
		map.put("t_fjcfbDtos",t_fjcfbDtos);
		map.put("t_sjxxDto", JSONObject.toJSONString(t_sjxxDto));	
		map.put("sjwzxxDtos", sjwzxxDtos);
		map.put("jcxmlist",t_jcxmlist);
		map.put("SjxxjgList", getJclxCount);
		map.put("wzjcxmlist", c_jcxmlist);
		return map;
	}
	
	/**
	 * 微信统计废弃标本
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/sampleStateListDaily")
	@ResponseBody
	public Map<String, Object> sampleStateListDaily(HttpServletRequest request){
		Map<String, Object> map= new HashMap<>();
		String str = request.getParameter("sjybztDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjybztDto sjybztDto = JSONObject.toJavaObject(parseObject, SjybztDto.class);
		if("null".equals(sjybztDto.getJsrq())) {
			sjybztDto.setJsrq("");
		}
		if(sjybztDto.getYhid()!=null) {
			List<HbqxDto> hbqxList=hbqxService.getHbxxByYhid(sjybztDto.getYhid());
			if(hbqxList!=null&& hbqxList.size()>0) {
				List<String> strList= new ArrayList<>();
				for (int i = 0; i < hbqxList.size(); i++){
					strList.add(hbqxList.get(i).getHbmc());
				}
				sjybztDto.setSjhbs(strList);
				List<SjxxDto> sjxxList=sjxxService.getPageFqybList(sjybztDto);
				map.put("rows", sjxxList);
			}
		}
		map.put("total", sjybztDto.getTotalNumber());
		return map;
	}
	
	/**
	 * 微信统计废弃标本占比
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/getpercentageDaily")
	@ResponseBody
	public Map<String, String> getpercentageDaily(HttpServletRequest request){
		Map<String,String> map= new HashMap<>();
		String str = request.getParameter("sjybztDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjybztDto sjybztDto = JSONObject.toJavaObject(parseObject, SjybztDto.class);
		if(sjybztDto.getYhid()!=null) {
			List<HbqxDto> hbqxList=hbqxService.getHbxxByYhid(sjybztDto.getYhid());
			if(hbqxList!=null&& hbqxList.size()>0) {
				List<String> strList= new ArrayList<>();
				for (int i = 0; i < hbqxList.size(); i++){
					strList.add(hbqxList.get(i).getHbmc());
				}
				sjybztDto.setSjhbs(strList);
				if(sjybztDto.getZt().equals("0")) {//状态为0的是没有选择标本状态的
					sjybztDto.setZtflg("1");
					sjybztDto.setZt("");
					Integer ybztNum=sjxxService.getCountByzt(sjybztDto);
					Integer sjxxNum=sjxxService.getAllFqyb(sjybztDto);
					map.put("sjxxNum", sjxxNum+"");
					map.put("ybztNum", ybztNum+"");
				}else {
					Integer ybztNum=sjxxService.getCountByzt(sjybztDto);
					map.put("ybztNum", ybztNum+"");
					JcsjDto jcsjDto=jcsjService.getDtoById(sjybztDto.getZt());//查询标本状态的扩展参数
					if(jcsjDto!=null) {
						if("1".equals(jcsjDto.getCskz1())) { //扩展参数1的为溶血的标本，统计基数为所有的nbbm以B开头的标本
							Integer sjxxNum=sjxxService.getCountAllSjxx(sjybztDto);
							map.put("sjxxNum", sjxxNum+"");
						}else {//其他的标本统计基数为所有的废弃的标本
							Integer sjxxNum=sjxxService.getAllFqyb(sjybztDto);
							map.put("sjxxNum", sjxxNum+"");
						}
					}
				}
				
			}
		}
		return map;
	}
	
	
	/*------------------------------------------  个人周报   -------------------------------------------------*/
	
	/**
	 * 微信用户访问周报统计
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/getWeChatWeekly")
	@ResponseBody
	public Map<String, Object> getWeChatWeekly(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		String str = request.getParameter("sjxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjxxDto sjxxDto = JSONObject.toJavaObject(parseObject, SjxxDto.class);
		//查询合作伙伴分类信息
		SjhbxxDto sjhbxxDto=new SjhbxxDto();
		sjhbxxDto.setXtyhid(sjxxDto.getYhid());
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.selectFlByWeekly(sjhbxxDto);
		map.put("sjhbxxDtos", sjhbxxDtos);
		return map;
	}
	
	/**
	 * 微信用户查询周报数据
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/getWeeklyModel")
	@ResponseBody
	public Map<String, Object> getWeeklyModel(HttpServletRequest request){
		String str = request.getParameter("sjxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjxxDto sjxxDto = JSONObject.toJavaObject(parseObject, SjxxDto.class);
		Map<String, Object> map = new HashMap<>();
		boolean result=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getJsrqstart()+sjxxDto.getJsrqend(), request);
		if(result) {
			if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
				int dayOfWeek = DateUtils.getWeek(new Date());
				if (dayOfWeek <= 2){
					sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
					sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
					sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
					sjxxDto.setBgrqend(sjxxDto.getJsrqend());
				}else{
					sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
					sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
					sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
					sjxxDto.setBgrqend(sjxxDto.getJsrqend());
				}
			}else if(StringUtil.isBlank(sjxxDto.getBgrqstart())){
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			}
			//查询到当前用户下边的合作伙伴
			if(StringUtil.isNotBlank(sjxxDto.getYhid()) || (sjxxDto.getLrrys() != null && sjxxDto.getLrrys().size() > 0)) {
				if(StringUtil.isNotBlank(sjxxDto.getYhid())){
					boolean sfdwxd =ddyhService.getDwxd(sjxxDto.getYhid());
					if(sfdwxd) {
						List<HbqxDto> hbqxList=hbqxService.getHbxxByYhid(sjxxDto.getYhid());
						if(hbqxList!=null&& hbqxList.size()>0) {
							List<String> strList= new ArrayList<>();
							for (int i = 0; i < hbqxList.size(); i++){
								strList.add(hbqxList.get(i).getHbmc());
							}
							sjxxDto.setDbs(strList);
						}else {
							List<String> strList= new ArrayList<>();
							strList.add("matridx-default");
							sjxxDto.setDbs(strList);
						}
					}
				}else if(sjxxDto.getLrrys() != null && sjxxDto.getLrrys().size() > 0){
					List<String> dbs = getDbsByLrrys(sjxxDto, request);
					if(dbs != null && dbs.size() > 0){
						sjxxDto.setDbs(dbs);
					}else {
						List<String> strList= new ArrayList<>();
						strList.add("matridx-default");
						sjxxDto.setDbs(strList);
					}
				}
				
				//送检情况统计
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> ybqk=SjxxStatisticService.getYbxxByWeekly(sjxxDto);
				map.put("ybqk", ybqk);
				//标本数信息
				sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> yblxList = SjxxStatisticService.getSfsfByWeekly(sjxxDto);
				map.put("ybtj", yblxList);
				//测试数信息
	            List<Map<String,String>> jcxmDRList=SjxxStatisticService.getYbxxDRByWeekly(sjxxDto);
	            sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
	            map.put("jcxmnum",jcxmDRList);
	           //收费标本测试数信息
	    		List<Map<String,String>> ybxxType=SjxxStatisticService.getYbxxTypeByWeekly(sjxxDto);
	    		sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
	    		map.put("ybxxType", ybxxType);
	    		//上上周的临床反馈结果
	    		List<Map<String,String>> lcfkList=SjxxStatisticService.getLcfkOfBeforeWeekly(sjxxDto);
	    		map.put("lcfkList", lcfkList);
	    		//标本类型前三的阳性率
	    		List<Map<String,String>> jyjgOfYblx=SjxxStatisticService.getJyjgWeekly(sjxxDto);
	    		sjxxDto.getZqs().put("jyjgOfYblx", sjxxDto.getBgrqstart() + "~" + sjxxDto.getBgrqend());
	    		map.put("jyjgOfYblx", jyjgOfYblx);
	    		//科室的统计图
	    		List<Map<String,String>> ksList=SjxxStatisticService.getKsByWeekly(sjxxDto);
	    		sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
	    		map.put("ksList", ksList);
	    		//增加合作医疗机构
	    		List<Map<String,String> >sjdwOfsjysOfks=SjxxStatisticService.getCooperativeWeekly(sjxxDto);
	    		map.put("sjdwOfsjysOfks", sjdwOfsjysOfks);
	    		//统计合作伙伴
	    		List<Map<String, String>> sjhbList=SjxxStatisticService.getSjhbByWeekly(sjxxDto);
	    		sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
	    		map.put("dbtj", sjhbList);
	    		//统计复检申请
				FjsqDto fjsqDto=new FjsqDto();
				fjsqDto.setSjhbs(sjxxDto.getDbs());
				fjsqDto.setLrsjstart(sjxxDto.getJsrqstart());
				fjsqDto.setLrsjend(sjxxDto.getJsrqend());
				List<Map<String,String>> fjsqList=SjxxStatisticService.getFjsqDaily(fjsqDto);
				sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				map.put("fjsq", fjsqList);
				//统计废弃标本
				List<Map<String,String>> fqybMap=SjxxStatisticService.getFqybByYbztDaily(sjxxDto);
				sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				map.put("fqyb", fqybMap);
				//标本日期统计
				List<Map<String, String>> rqList=SjxxStatisticService.getDataOfNum(sjxxDto);
				List<Map<String,String>> sjbglist=SjxxStatisticService.getSjbgByBgrq(sjxxDto);
				List<Map<String,String>> yxllist=SjxxStatisticService.getJyjgBybgrq(sjxxDto);
				sjxxDto.getZqs().put("rqtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
					for (int i = 0; i <sjbglist.size(); i++){
						rqList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
						rqList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
					}
					map.put("rqtj",rqList);
				}
				//报告结果
				List<Map<String, String>> ybbumList=SjxxStatisticService.getJyjgLxByWeekly(sjxxDto);
				sjxxDto.getZqs().put("bgjgtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				map.put("bgjgtj", ybbumList);
				//微信按照周统计标本个数（周）
				List<Map<String, String>> ybsList=SjxxStatisticService.getYbxxnumByWeekly(sjxxDto);
				sjxxDto.getZqs().put("weektj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				map.put("weektj", ybsList);
				//按照合作伙伴类别
				String zqString = "0";
				if(StringUtil.isNotBlank(sjxxDto.getZq())) {
					zqString = sjxxDto.getZq().split(",")[0];
				}
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				sjxxDto.setJsrqstart(dateFormat.format(DateUtils.getDate(DateUtils.parseDate("yyyy-MM-dd",sjxxDto.getJsrqend()), - Integer.parseInt(zqString))));
				Map<String,List<Map<String, String>>> fllists=SjxxStatisticService.getSjhbFlByWeekly(sjxxDto);
				if(fllists!=null) {
					for(String map1 : fllists.keySet()){
						map.put("fltj_"+map1,fllists.get(map1));
					}
				}
				map.put("searchData", sjxxDto);
			}
		} 
		return map;
	}
	
	/**
	 * 通过条件查询刷新页面
	 * 
	 * @return
	 */
	@RequestMapping("/miniprogram/getWeeklyModel_Condition")
	@ResponseBody
	public Map<String, Object> getWeeklyModel_Condition(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String str = request.getParameter("sjxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjxxDto sjxxDto = JSONObject.toJavaObject(parseObject, SjxxDto.class);
		String method = request.getParameter("method");
		//查询到当前用户下边的合作伙伴
		if(StringUtil.isNotBlank(sjxxDto.getYhid()) || (sjxxDto.getLrrys() != null && sjxxDto.getLrrys().size() > 0)) {
			if(StringUtil.isNotBlank(sjxxDto.getYhid())){
				boolean sfdwxd =ddyhService.getDwxd(sjxxDto.getYhid());
				if(sfdwxd) {
					sjxxDto.setDwxzbj("1");
					List<HbqxDto> hbqxList=hbqxService.getHbxxByYhid(sjxxDto.getYhid());
					if(hbqxList!=null&& hbqxList.size()>0) {
						List<String> strList= new ArrayList<>();
						for (int i = 0; i < hbqxList.size(); i++){
							strList.add(hbqxList.get(i).getHbmc());
						}
						sjxxDto.setDbs(strList);
					}else {
						List<String> strList= new ArrayList<>();
						strList.add("matridx-default");
						sjxxDto.setDbs(strList);
					}
				}
			}else if(sjxxDto.getLrrys() != null && sjxxDto.getLrrys().size() > 0){
				List<String> dbs = getDbsByLrrys(sjxxDto, request);
				if(dbs != null && dbs.size() > 0){
					sjxxDto.setDbs(dbs);
				}else {
					List<String> strList= new ArrayList<>();
					strList.add("matridx-default");
					sjxxDto.setDbs(strList);
				}
			}
			//判断method不能为空，为空即返回空
			if(StringUtil.isBlank(method)) {
				return map;
			}else if("getYbxxWeekly_year".equals(method)) {
				//送检情况（年）
				setDateByYear(sjxxDto,-6,false);
				sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String, String>> ybqklist=SjxxStatisticService.getYbxxByWeekly(sjxxDto);
				map.put("ybqk",ybqklist);
			}else if("getYbxxWeekly_mon".equals(method)) {
				//送检情况（月）
				setDateByMonth(sjxxDto,-6);
				sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String, String>> ybqklist=SjxxStatisticService.getYbxxByWeekly(sjxxDto);
				map.put("ybqk",ybqklist);
			}else if("getYbxxWeekly_week".equals(method)){
				 //标本情况按周
				setDateByWeek(sjxxDto); 
				sjxxDto.getZqs().put("ybqk",sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> ybqklist=SjxxStatisticService.getybxx_weekByWeekly(sjxxDto);
				map.put("ybqk",ybqklist);
			}else if("getYbxxWeekly_day".equals(method)){
				//送检情况（天）
				sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> ybqklist=SjxxStatisticService.getYbxxByWeekly(sjxxDto);
				map.put("ybqk",ybqklist);
			}else if("getSfsf_year".equals(method)) {
				//标本类型（年）
				setDateByYear(sjxxDto,0,false);
				sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String, String>> yblxList = SjxxStatisticService.getSfsfByWeekly(sjxxDto);
				 map.put("ybtj", yblxList);
			}else if("getSfsf_mon".equals(method)) {
				//标本类型（月）
				setDateByMonth(sjxxDto,0);
				sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String, String>> yblxList = SjxxStatisticService.getSfsfByWeekly(sjxxDto);
				map.put("ybtj", yblxList);
			}else if("getSfsf_week".equals(method)) {
				//标本类型（周）
				setDateByWeek(sjxxDto);
				sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> yblxList=SjxxStatisticService.getSfsfByWeekly(sjxxDto);
				 map.put("ybtj", yblxList);
			}else if("getSfsf_day".equals(method)) {
				//标本类型（日）
				setDateByDay(sjxxDto,0);
				sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> yblxList=SjxxStatisticService.getSfsfByWeekly(sjxxDto);
				map.put("ybtj", yblxList);
			}else if("getYbxxDR_year".equals(method)) {
				//检测总次数按年查询
				setDateByYear(sjxxDto,-6,false);
				sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String,String>> jcxmnum=SjxxStatisticService.getYbxxDRByWeekly(sjxxDto);
				map.put("jcxmnum", jcxmnum);
			}else if("getYbxxDR_mon".equals(method)) {
				//检测总次数按月查询
				setDateByMonth(sjxxDto,-6);
				sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String, String>> jcxmnum = SjxxStatisticService.getYbxxDRByWeekly(sjxxDto);
				map.put("jcxmnum", jcxmnum);
			}else if("getYbxxDR_week".equals(method)) {
				//检测总次数按周查询
				sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> jcxmnum=SjxxStatisticService.getYbxxDR_weeek(sjxxDto);
				map.put("jcxmnum", jcxmnum);
			}else if("getYbxxDR_day".equals(method)) {
				//检测总次数按日查询
				sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> jcxmnum=SjxxStatisticService.getYbxxDRByWeekly(sjxxDto);
				map.put("jcxmnum", jcxmnum);
			}else if("getYbxxType_year".equals(method)) {
				//收费测试数信息
				setDateByYear(sjxxDto,-6,false);
				sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String,String>> ybxxType=SjxxStatisticService.getYbxxTypeByWeekly(sjxxDto);
				map.put("ybxxType", ybxxType);
			}else if("getYbxxType_mon".equals(method)) {
				//收费测试数信息
				setDateByMonth(sjxxDto,-6);
				sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String, String>> ybxxType = SjxxStatisticService.getYbxxTypeByWeekly(sjxxDto);
				map.put("ybxxType", ybxxType);
			}else if("getYbxxType_week".equals(method)) {
				//收费测试数信息
				sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> ybxxType=SjxxStatisticService.getYbxxType_week(sjxxDto);
				map.put("ybxxType", ybxxType);
			}else if("getYbxxType_day".equals(method)) {
				//收费测试数信息
				sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> ybxxType=SjxxStatisticService.getYbxxTypeByWeekly(sjxxDto);
				map.put("ybxxType", ybxxType);
			}else if("getJyjgWeekly_year".equals(method)) {
				//标本阳性率（年）
				setDateByYear(sjxxDto,0,true);
				sjxxDto.getZqs().put("jyjgOfYblx", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String, String>> jyjgOfYblx = SjxxStatisticService.getJyjgWeekly(sjxxDto);
				 map.put("jyjgOfYblx", jyjgOfYblx);
			}else if("getJyjgWeekly_mon".equals(method)) {
				//标本阳性率(月)
				setDateByMonth(sjxxDto,0);
				sjxxDto.getZqs().put("jyjgOfYblx", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String, String>> jyjgOfYblx = SjxxStatisticService.getJyjgWeekly(sjxxDto);
				map.put("jyjgOfYblx", jyjgOfYblx);
			}else if("getJyjgWeekly_week".equals(method)) {
				//标本阳性率(周)
				setDateByWeek(sjxxDto);
				sjxxDto.getZqs().put("jyjgOfYblx", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> jyjgOfYblx=SjxxStatisticService.getJyjgWeekly(sjxxDto);
				 map.put("jyjgOfYblx", jyjgOfYblx);
			}else if("getJyjgWeekly_day".equals(method)) {
				//标本阳性率(日)
				setDateByDay(sjxxDto,0);
				sjxxDto.getZqs().put("jyjgOfYblx", sjxxDto.getJsrqstart());
				List<Map<String, String>> jyjgOfYblx=SjxxStatisticService.getJyjgWeekly(sjxxDto);
				map.put("jyjgOfYblx", jyjgOfYblx);
			}else if("getKsByWeekly_year".equals(method)) {
				//送检科室(年)
				setDateByYear(sjxxDto,0,false);
				sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String, String>> ksList = SjxxStatisticService.getKsByWeekly(sjxxDto);
				 map.put("ksList", ksList);
			}else if("getKsByWeekly_mon".equals(method)) {
				//送检科室(月)
				setDateByMonth(sjxxDto,0);
				sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String, String>> ksList = SjxxStatisticService.getKsByWeekly(sjxxDto);
				map.put("ksList", ksList);
			}else if("getKsByWeekly_week".equals(method)) {
				//送检科室(周)
				setDateByWeek(sjxxDto);
				sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> ksList=SjxxStatisticService.getKsByWeekly(sjxxDto);
				 map.put("ksList", ksList);
			}else if("getKsByWeekly_day".equals(method)) {
				//送检科室(日)
				setDateByDay(sjxxDto,0);
				sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqstart());
				List<Map<String, String>> ksList=SjxxStatisticService.getKsByWeekly(sjxxDto);
				map.put("ksList", ksList);
			}else if("getSjhb_year".equals(method)) {
				//送检排名按年
				setDateByYear(sjxxDto,0,false);
				sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
				List<Map<String, String>> dblist=SjxxStatisticService.getSjhbByWeekly(sjxxDto);
				map.put("dbtj",dblist);
			}else if("getSjhb_mon".equals(method)) {
				//送检排名按月
				setDateByMonth(sjxxDto,0);
				sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
				List<Map<String, String>> dblist=SjxxStatisticService.getSjhbByWeekly(sjxxDto);
				map.put("dbtj",dblist);
			}else if("getSjhb_week".equals(method)) {
				//送检排名按周
				setDateByWeek(sjxxDto);
				sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				List<Map<String, String>> dblist=SjxxStatisticService.getSjhbByWeekly(sjxxDto);
				map.put("dbtj",dblist);
			}else if("getSjhb_day".equals(method)) {
				//送检排名按日
				setDateByDay(sjxxDto,0);
				sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				List<Map<String, String>> dblist=SjxxStatisticService.getSjhbByWeekly(sjxxDto);
				map.put("dbtj",dblist);
			}else if("getFjsqDaily_year".equals(method)) {
				//复检按年查询
				setDateByYear(sjxxDto,0,false);
				sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				FjsqDto fjsqDto=new FjsqDto();
				fjsqDto.setLrsjYstart(sjxxDto.getJsrqYstart());
				fjsqDto.setLrsjYend(sjxxDto.getJsrqYend());
				List<Map<String,String>> fjsqMap=SjxxStatisticService.getFjsqDaily(fjsqDto);
				map.put("fjsq", fjsqMap);
			}else if("getFjsqDaily_mon".equals(method)) {
				//复检按月查询
				setDateByMonth(sjxxDto,0);
				sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				FjsqDto fjsqDto=new FjsqDto();
				fjsqDto.setLrsjMstart(sjxxDto.getJsrqMstart());
				fjsqDto.setLrsjMend(sjxxDto.getJsrqMend());
				List<Map<String,String>> fjsqMap=SjxxStatisticService.getFjsqDaily(fjsqDto);
				map.put("fjsq", fjsqMap);
			}else if("getFjsqDaily_week".equals(method)) {
				//复检按周查询
				setDateByWeek(sjxxDto);
				sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				FjsqDto fjsqDto=new FjsqDto();
				fjsqDto.setLrsjstart(sjxxDto.getJsrqstart());
				fjsqDto.setLrsjend(sjxxDto.getJsrqend());
				List<Map<String,String>> fjsqMap=SjxxStatisticService.getFjsqDaily(fjsqDto);
				map.put("fjsq", fjsqMap);
			}else if("getFjsqDaily_day".equals(method)) {
				//复检按日查询
				setDateByDay(sjxxDto,0);
				sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				FjsqDto fjsqDto=new FjsqDto();
				fjsqDto.setLrsjstart(sjxxDto.getJsrqstart());
				fjsqDto.setLrsjend(sjxxDto.getJsrqend());
				List<Map<String,String>> fjsqMap=SjxxStatisticService.getFjsqDaily(fjsqDto);
				map.put("fjsq", fjsqMap);
			}else if("getFqybByYbztDaily_year".equals(method)) {
				//废弃标本按年查询
				setDateByYear(sjxxDto,0,false);
				sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String,String>>fqybMap=SjxxStatisticService.getFqybByYbztDaily(sjxxDto); 
				map.put("fqyb",fqybMap);
			}else if("getFqybByYbztDaily_mon".equals(method)) {
				//废弃标本按月查询
				setDateByMonth(sjxxDto,0);
				sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String,String>>fqybMap=SjxxStatisticService.getFqybByYbztDaily(sjxxDto); 
				map.put("fqyb",fqybMap);
			}else if("getFqybByYbztDaily_week".equals(method)) {
				//废弃标本按周查询
				setDateByWeek(sjxxDto);
				sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String,String>>fqybMap=SjxxStatisticService.getFqybByYbztDaily(sjxxDto); 
				map.put("fqyb",fqybMap);
			}else if("getFqybByYbztDaily_day".equals(method)) {
				//废弃标本按日查询
				setDateByDay(sjxxDto,0);
				sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String,String>>fqybMap=SjxxStatisticService.getFqybByYbztDaily(sjxxDto); 
				map.put("fqyb",fqybMap);
			}else if("getFlByYear".equals(method)){
				//分类情况按年
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
				sjxxDto.setTj("year");
				Map<String,List<Map<String, String>>> fllists=SjxxStatisticService.getSjhbFlByWeekly(sjxxDto);
				if(fllists!=null) {
					for(String map1 : fllists.keySet()){
						map.put("fltj_"+map1,fllists.get(map1));
					}
				}
			}else if("getFlByMon".equals(method)){
				//分类情况按月
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("mon");
				Map<String,List<Map<String, String>>> fllists=SjxxStatisticService.getSjhbFlByWeekly(sjxxDto);
				if(fllists!=null) {
					for(String map1 : fllists.keySet()){
						map.put("fltj_"+map1,fllists.get(map1));
					}
				}
			}else if("getFlByWeek".equals(method)){
				//分类情况按周
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
				Map<String,List<Map<String, String>>> fllists=SjxxStatisticService.getSjhbFl_week(sjxxDto);
				if(fllists!=null) {
					for(String map1 : fllists.keySet()){
						map.put("fltj_"+map1,fllists.get(map1));
					}
				}
			}else if("getFlByDay".equals(method)){
				//分类情况按日
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("day");
				Map<String,List<Map<String, String>>> fllists=SjxxStatisticService.getSjhbFlByWeekly(sjxxDto);
				if(fllists!=null) {
					for(String map1 : fllists.keySet()){
						map.put("fltj_"+map1,fllists.get(map1));
					}
				}
			}else if("getHbByYear".equals(method)){
				//伙伴情况按年
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
				sjxxDto.setTj("year");
				
				List<Map<String, String>> fllist=SjxxStatisticService.getWeeklyByTjFl(sjxxDto);
				Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
				if(fllists!=null) {
					for(String map1 : fllists.keySet()){
						map.put("hbtj_"+map1,fllists.get(map1));
					}
				}
			}else if("getHbByMon".equals(method)){
				//伙伴情况按月
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("mon");
				List<Map<String, String>> fllist=SjxxStatisticService.getWeeklyByTjFl(sjxxDto);
				Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
				if(fllists!=null) {
					for(String map1 : fllists.keySet()){
						map.put("hbtj_"+map1,fllists.get(map1));
					}
				}
			}else if("getHbByWeek".equals(method)){
				//伙伴情况按周
//					int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
				sjxxDto.setTj("week");
				List<Map<String, String>> fllist=SjxxStatisticService.getWeeklyByTjFl_week(sjxxDto);
				Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
				if(fllists!=null) {
					for(String map1 : fllists.keySet()){
						map.put("hbtj_"+map1,fllists.get(map1));
					}
				}
			}else if("getHbByDay".equals(method)){
				//伙伴情况按日
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("day");
				List<Map<String, String>> fllist=SjxxStatisticService.getWeeklyByTjFl(sjxxDto);
				Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
				if(fllists!=null) {
					for(String map1 : fllists.keySet()){
						map.put("hbtj_"+map1,fllists.get(map1));
					}
				}
			}else if("getAllhbByYear".equals(method)) {
				//合作伙伴总数按年
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
				List<Map<String, String>> fllist=SjxxStatisticService.getWeeklyByTjFl(sjxxDto);
				map.put("hbztj",fllist);
			}else if("getAllhbByMon".equals(method)) {
				//合作伙伴总数按月
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
				sjxxDto.setTj("mon");
				List<Map<String, String>> fllist=SjxxStatisticService.getWeeklyByTjFl(sjxxDto);
				map.put("hbztj",fllist);
			}else if("getAllhbByWeek".equals(method)) {
				//合作伙伴总数按周
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
				sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				sjxxDto.setTj("day");
				List<Map<String, String>> fllist=SjxxStatisticService.getWeeklyByTjFl(sjxxDto);
				map.put("hbztj",fllist);
			}else if("getAllhbByDay".equals(method)) {
				//合作伙伴总数按日
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				sjxxDto.setTj("day");
				List<Map<String, String>> fllist=SjxxStatisticService.getWeeklyByTjFl(sjxxDto);
				map.put("hbztj",fllist);
			}
			map.put("searchData", sjxxDto);
		}
		return map;
	}
	
	/**
	 * 统计合作伙伴页面
	 * 
	 * @return
	 */
	@RequestMapping("/miniprogram/weeklyPartnerPage")
	@ResponseBody
	public Map<String, Object> weeklyPartnerPage(HttpServletRequest request){
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		String str = request.getParameter("sjxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjxxDto sjxxDto = JSONObject.toJavaObject(parseObject, SjxxDto.class);
		String strhb = request.getParameter("sjhbxxDto");
		JSONObject hbObject = JSONObject.parseObject(strhb);
		SjhbxxDto sjhbxxDto = JSONObject.toJavaObject(hbObject, SjhbxxDto.class);
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
		sjxxDto.setHbfl(sjhbxxDto.getFl());
		sjxxDto.setHbzfl(sjhbxxDto.getZfl());
		if(StringUtil.isBlank(sjhbxxDto.getXtyhid()) && StringUtil.isNotBlank(sjhbxxDto.getYhid())) {
			sjhbxxDto.setXtyhid(sjhbxxDto.getYhid());
		}
		//查询合作伙伴信息
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getSjhbDtoListByHbqx(sjhbxxDto);
		map.put("sjhbxxDtos", sjhbxxDtos);
		map.put("sjxxDto", JSONObject.toJSONString(sjxxDto));
		return map;
	}
	
	/**
	 * 查询汇报领导的伙伴统计
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/getPartnerStatisWeekly")
	@ResponseBody
	public Map<String, Object> getPartnerStatisWeekly(HttpServletRequest request){
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
		Map<String, Object> map = new HashMap<>();
		String str = request.getParameter("sjxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjxxDto sjxxDto = JSONObject.toJavaObject(parseObject, SjxxDto.class);
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			}
		}else if(StringUtil.isBlank(sjxxDto.getBgrqstart())){
			sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
			sjxxDto.setBgrqend(sjxxDto.getJsrqend());
		}
		if(StringUtil.isNotBlank(sjxxDto.getYhid()) || (sjxxDto.getLrrys() != null && sjxxDto.getLrrys().size() > 0)) {
			if(StringUtil.isNotBlank(sjxxDto.getYhid())){
				boolean sfdwxd =ddyhService.getDwxd(sjxxDto.getYhid());
				if(sfdwxd) {
					List<HbqxDto> hbqxList=hbqxService.getHbxxByYhid(sjxxDto.getYhid());
					List<HbqxDto> hbmcqxList=hbqxService.getHbxxGroupByYhid(sjxxDto.getYhid());
					if(hbqxList!=null&& hbqxList.size()>0) {
						List<String> strList= new ArrayList<>();
						for (int i = 0; i < hbqxList.size(); i++){
							strList.add(hbqxList.get(i).getHbmc());
						}
						sjxxDto.setDbs(strList);
					}else {
						List<String> strList= new ArrayList<>();
						strList.add("matridx-default");
						sjxxDto.setDbs(strList);
					}
					if(hbmcqxList!=null&& hbmcqxList.size()>0) {
						List<String> strList= new ArrayList<>();
						for (int i = 0; i < hbmcqxList.size(); i++){
							strList.add(hbmcqxList.get(i).getHbmc());
						}
						sjxxDto.setDbmcs(strList);
					}else {
						List<String> strList= new ArrayList<>();
						strList.add("matridx-default");
						sjxxDto.setDbmcs(strList);
					}
				}
			}else if(sjxxDto.getLrrys() != null && sjxxDto.getLrrys().size() > 0){
				List<String> dbs = getDbsByLrrys(sjxxDto, request);
				if(dbs != null && dbs.size() > 0){
					sjxxDto.setDbs(dbs);
				}else {
					List<String> strList= new ArrayList<>();
					strList.add("matridx-default");
					sjxxDto.setDbs(strList);
				}
				
			}
			//按伙伴查询
			String zqString = "0";
			if(StringUtil.isNotBlank(sjxxDto.getZq())) {
				zqString = sjxxDto.getZq().split(",")[0];
			}
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			sjxxDto.setTj("day");
			sjxxDto.setJsrqstart(dateFormat.format(DateUtils.getDate(DateUtils.parseDate("yyyy-MM-dd",sjxxDto.getJsrqend()), - Integer.parseInt(zqString))));
			List<Map<String, String>> fllist=SjxxStatisticService.getWeeklyByTjFl(sjxxDto);
			map.put("hbztj", fllist);
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
			
			for(String map1 : fllists.keySet()){
				map.put("hbtj_"+map1,fllists.get(map1));
			}
			map.put("searchData", sjxxDto);
			}
		return map;
	}
	
	/**
	 * 通过用户id，查询伙伴权限
	 * 
	 * @return
	 */
	@RequestMapping("/miniprogram/getWeeklyPartner_Hbqx")
	@ResponseBody
	public Map<String, Object> getWeeklyPartner_Hbqx(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String str = request.getParameter("sjxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjxxDto sjxxDto = JSONObject.toJavaObject(parseObject, SjxxDto.class);
		String strsjhb = request.getParameter("sjhbxxDto");
		JSONObject parsesjhb = JSONObject.parseObject(strsjhb);
		SjhbxxDto sjhbxxDto = JSONObject.toJavaObject(parsesjhb, SjhbxxDto.class);	
		List<HbqxDto> sjhbxxDtos = hbqxService.getHbxxByYhid(sjhbxxDto.getXtyhid());
		map.put("sjhbxxDtos", sjhbxxDtos);
		sjxxDto.setHbfl(sjhbxxDto.getFl());
		sjxxDto.setHbzfl(sjhbxxDto.getZfl());
		map.put("sjxxDto", sjxxDto);
		return map;
	}
	
	/*------------------------------------------  全部日报   -------------------------------------------------*/
	
	/**
	 * 报告阳性率列表
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/miniprogram/getListPositive")
	@ResponseBody
	public Map<String, Object> getListPositive(HttpServletRequest request){
		String str = request.getParameter("sjwzxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjwzxxDto sjwzxxDto = JSONObject.toJavaObject(parseObject, SjwzxxDto.class);
		Map<String, Object> map = new HashMap<>();
		if(sjwzxxDto.getJyjg()==null) {
			if("阳性".equals(sjwzxxDto.getLxmc())) {
				sjwzxxDto.setJyjg("1");
			}else if("阴性".equals(sjwzxxDto.getLxmc())) {
				sjwzxxDto.setJyjg("0");
			}else if("疑似".equals(sjwzxxDto.getLxmc())) {
				sjwzxxDto.setJyjg("2");
			}
		}
		List<SjxxDto> sjxxList = sjxxService.getListPositive(sjwzxxDto);
		map.put("sjxxList", sjxxList);
		return map;
	}
	
	/**
	 * 日报统计
	 * @return
	 */
	@RequestMapping("/miniprogram/getSfsfybxx")
	@ResponseBody
	public Map<String, Object> getSfsfybxx(HttpServletRequest request){
		String str = request.getParameter("sjxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjxxDto sjxxDto = JSONObject.toJavaObject(parseObject, SjxxDto.class);
		Map<String, Object> map = new HashMap<>();
		boolean checkSign=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getJsrq(),request);
		sjxxDto.setBgrq(sjxxDto.getJsrq());
		if(checkSign) {
			//标本信息
			List<Map<String,String>> ybxxMap=sjxxService.getYbxxByDay(sjxxDto);
			map.put("ybxx", ybxxMap);
			//合作伙伴统计
			List<Map<String,String>> sjhbMap=sjxxService.getYbxxByTjmc(sjxxDto);
			if(sjhbMap.size()>39) {
				List<Map<String,String>> asc_YbxxBySjh_daily_Map= new ArrayList<>();
				asc_YbxxBySjh_daily_Map.addAll(sjhbMap.subList(0, 39));
				map.put("sjhb", asc_YbxxBySjh_daily_Map);
				Collections.reverse(sjhbMap); // 倒序排列 
				List<Map<String,String>> desc_YbxxBySjh_daily_Map=sjhbMap.subList(0,39);
				map.put("sjhb_t", desc_YbxxBySjh_daily_Map);
			}else {
				map.put("sjhb", sjhbMap);
			}	
			//统计复检申请
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setLrsj(sjxxDto.getJsrq());
			List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			map.put("fjsq", fjsqMap);
			//统计废弃标本
			List<Map<String,String>> fqybMap=sjxxService.getFqybByYbzt(sjxxDto);
			map.put("fqyb", fqybMap);
			//统计报高阳性率
			List<Map<String, String>> possiblelist=sjxxService.getYbnumByJglx(sjxxDto);
			map.put("possiblelist",possiblelist);
			//统计物种信息
			//List<Map<String, String>> species=sjxxService.getWzxxBySjid(sjxxDto);
			/*
			 * Map<String,String> testmap=new HashMap<String, String>();
			 * testmap.put("num","5"); testmap.put("wzzwm", "wzzwm");
			 * List<Map<String, String>> species=new
			 * ArrayList<Map<String,String>>(); for (int i = 0; i < 1000; i++){
			 * species.add(testmap); }
			 */
			//map.put("species",species);
			//送检清单
			List<SjxxDto> sjxxList=sjxxService.getListBydaily(sjxxDto);
			map.put("sjxxList",sjxxList);
			//检测项目个数
            List<Map<String,String>> jcxmDRList=sjxxService.getYbxxDRByDay(sjxxDto);
            map.put("jcxmnum",jcxmDRList);
            //收费标本里边检测项目条数
            List<Map<String,String>> jcxmTypeList=sjxxService.getYbxxTypeByDay(sjxxDto);
            map.put("jcxmType",jcxmTypeList);
         
			map.put("searchData", sjxxDto);
			return map;
		}
		return map;
	}
	
	/**
	 * 统计时点击查看列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/RecheckStatisPage")
	@ResponseBody
	public Map<String, Object> RecheckStatisPage(HttpServletRequest request){
		String str = request.getParameter("fjsqDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		FjsqDto fjsqDto = JSONObject.toJavaObject(parseObject, FjsqDto.class);
		Map<String, Object> map= new HashMap<>();
		if("null".equals(fjsqDto.getLrsj())) {
			fjsqDto.setLrsj("");
		}
		List<FjsqDto> fjsqList=fjsqService.getPagedStatisRecheck(fjsqDto);
		map.put("total", fjsqDto.getTotalNumber());
		map.put("rows", fjsqList);
		return map;
	}
	
	/**
	 * 废弃标本统计列表
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/sampleStatePage")
	@ResponseBody
	public Map<String, Object> sampleStatePage(HttpServletRequest request){
		Map<String, Object> map= new HashMap<>();
		String str = request.getParameter("sjybztDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjybztDto sjybztDto = JSONObject.toJavaObject(parseObject, SjybztDto.class);
		if("null".equals(sjybztDto.getJsrq())) {
			sjybztDto.setJsrq("");
		}
		List<SjxxDto> sjxxList=sjxxService.getPageFqybList(sjybztDto);
		map.put("total", sjybztDto.getTotalNumber());
		map.put("rows", sjxxList);
		return map;
	}
	
	/**
	 * 查询不同状态的标本的占比
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/getPercentage")
	@ResponseBody
	public Map<String, String> getPercentage(HttpServletRequest request){
		Map<String,String> map= new HashMap<>();
		String str = request.getParameter("sjybztDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjybztDto sjybztDto = JSONObject.toJavaObject(parseObject, SjybztDto.class);
		if(sjybztDto.getZt().equals("0")) {//状态为0的是没有选择标本状态的
			sjybztDto.setZtflg("1");
			sjybztDto.setZt("");
			Integer ybztNum=sjxxService.getCountByzt(sjybztDto);
			Integer sjxxNum=sjxxService.getAllFqyb(sjybztDto);
			map.put("sjxxNum", sjxxNum+"");
			map.put("ybztNum", ybztNum+"");
		}else {
			Integer ybztNum=sjxxService.getCountByzt(sjybztDto);
			map.put("ybztNum", ybztNum+"");
			JcsjDto jcsjDto=jcsjService.getDtoById(sjybztDto.getZt());//查询标本状态的扩展参数
			if(jcsjDto!=null) {
				if("1".equals(jcsjDto.getCskz1())) { //扩展参数1的为溶血的标本，统计基数为所有的nbbm以B开头的标本
					Integer sjxxNum=sjxxService.getCountAllSjxx(sjybztDto);
					map.put("sjxxNum", sjxxNum+"");
				}else {//其他的标本统计基数为所有的废弃的标本
					Integer sjxxNum=sjxxService.getAllFqyb(sjybztDto);
					map.put("sjxxNum", sjxxNum+"");
				}
			}
		}
		return map;
	}
	
	/*------------------------------------------  全部周报   -------------------------------------------------*/
	
	/**
	 * 周报统计
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/weekLeadStatisPage")
	@ResponseBody
	public Map<String, Object> weekLeadStatisPage(HttpServletRequest request){
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		//查询合作伙伴分类信息
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.selectFl();
		map.put("sjhbxxDtos", sjhbxxDtos);
		return map;
	}
	
	/**
	 * 周报统计
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/weekLeadStatisPageByJsrq")
	@ResponseBody
	public Map<String, Object> weekLeadStatisPageByJsrq(HttpServletRequest request){
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		//查询合作伙伴分类信息
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.selectFl();
		map.put("sjhbxxDtos", sjhbxxDtos);
		return map;
	}
	
	/**
	 * 查询汇报领导的统计
	 * 
	 * @return
	 */
	@RequestMapping("/miniprogram/getWeekLeadStatis")
	@ResponseBody
	public Map<String, Object> getWeekLeadStatis(HttpServletRequest request){
		String strsj = request.getParameter("sjxxDto");
		JSONObject sjObject = JSONObject.parseObject(strsj);
		SjxxDto sjxxDto = JSONObject.toJavaObject(sjObject, SjxxDto.class);
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			}
		}else if(StringUtil.isBlank(sjxxDto.getBgrqstart())){
			sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
			sjxxDto.setBgrqend(sjxxDto.getJsrqend());
		}
		//标本类型统计
		Map<String, Object> map = new HashMap<>();
		sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		List<Map<String, String>> yblxList = sjxxService.getStatisByYblx(sjxxDto);
		map.put("ybtj", yblxList);
		//合作伙伴统计
		sjxxDto.setTj("sjdw");
		sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
		List<Map<String, String>> sjhbList=sjxxService.getStatisWeekBysjhb(sjxxDto);
		map.put("dbtj", sjhbList);
		//标本日期统计
		sjxxDto.setTj("day");
		List<Map<String, String>> rqList=sjxxService.getStatisByDate(sjxxDto);
		List<Map<String,String>> sjbglist=sjxxService.getStatisSjbgByBgrq(sjxxDto);
		List<Map<String,String>> yxllist=sjxxService.getStatisYxlBybgrq(sjxxDto);
		if(sjbglist!=null && sjbglist.size()>0 &&yblxList!=null&&yblxList.size()>0&&yxllist!=null&&yxllist.size()>0) {
			for (int i = 0; i <sjbglist.size(); i++){
				rqList.get(i).put("sjbg", String.valueOf(sjbglist.get(i).get("num")));
				rqList.get(i).put("yxl", String.valueOf(yxllist.get(i).get("num")));
			}
			map.put("rqtj",rqList);
		}
		//报告结果统计
		List<Map<String, String>> ybbumList=sjxxService.getYbnumByJglx(sjxxDto);
		map.put("bgjgtj", ybbumList);
		//标本周统计
		List<Map<String, String>> ybsList=sjxxService.getStatisWeekYbsByJsrq(sjxxDto);
		map.put("weektj", ybsList);
		
		//送检情况统计
		List<Map<String, String>> ybqk=sjxxService.getSjxxBySy(sjxxDto);
		map.put("ybqk", ybqk);
		sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		//统计复检申请
		FjsqDto fjsqDto=new FjsqDto();
		fjsqDto.setLrsjstart(sjxxDto.getJsrqstart());
		fjsqDto.setLrsjend(sjxxDto.getJsrqend());
		sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
		List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
		map.put("fjsq", fjsqMap);
		//统计废弃标本
		sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
		List<Map<String,String>> fqybMap=sjxxService.getFqybByYbzt(sjxxDto);
		map.put("fqyb", fqybMap);
		//标本信息检测总条数
		List<Map<String,String>> jcxmnum=sjxxService.getSjxxDRBySy(sjxxDto);
		sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		map.put("jcxmnum", jcxmnum);
		//增加合作医院数，科室数，医生数的统计表
		List<Map<String,String> >sjdwOfsjysOfks=sjxxService.getSjdwSjysKs(sjxxDto);
		sjxxDto.getZqs().put("sjdwOfsjysOfks", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		map.put("sjdwOfsjysOfks", sjdwOfsjysOfks);
		//增加科室的圆饼统计图
		List<Map<String,String>> ksList=sjxxService.getKsByweek(sjxxDto);
		sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		map.put("ksList", ksList);
		//收费标本下边检测项目的总条数
		List<Map<String,String>> ybxxType=sjxxService.getYbxxTypeBYWeek(sjxxDto);
		sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		map.put("ybxxType", ybxxType);
		//统计上上周的临床反馈结果
		List<Map<String,String>> lcfkList=sjxxService.getLcfkByBefore(sjxxDto);
		sjxxDto.getZqs().put("lcfkList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		map.put("lcfkList", lcfkList);
		//周报增加标本类型前三的阳性率
		List<Map<String,String>> jyjgOfYblx=sjxxService.getJyjgByYblx(sjxxDto);
		sjxxDto.getZqs().put("jyjgOfYblx", sjxxDto.getBgrqstart() + "~" + sjxxDto.getBgrqend());
		map.put("jyjgOfYblx", jyjgOfYblx);
		
		//按分类查询
		//标本按日查询
		String zqString = "0";
		if(StringUtil.isNotBlank(sjxxDto.getZq())) {
			zqString = sjxxDto.getZq().split(",")[0];
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		sjxxDto.setJsrqstart(dateFormat.format(DateUtils.getDate(DateUtils.parseDate("yyyy-MM-dd",sjxxDto.getJsrqend()), - Integer.parseInt(zqString))));
		Map<String,List<Map<String, String>>> fllists=sjxxService.getStatisByFl(sjxxDto);
		
		for(String map1 : fllists.keySet()){
			map.put("fltj_"+map1,fllists.get(map1));
		}
		map.put("searchData", sjxxDto);
		return map;
	}
	
	/**
	 * 通过条件查询刷新页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/getSjxxStatisByTj")
	@ResponseBody
	public Map<String, Object> getSjxxStatisByTj(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String strsj = request.getParameter("sjxxDto");
		JSONObject sjObject = JSONObject.parseObject(strsj);
		SjxxDto sjxxDto = JSONObject.toJavaObject(sjObject, SjxxDto.class);
		String method = request.getParameter("method");
		//判断method不能为空，为空即返回空
		if(StringUtil.isBlank(method)) {
			return map;
		}else if("getYblxByYear".equals(method)) {
			//标本按年查询
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> statisbyyblxlist = sjxxService.getStatisByYblx(sjxxDto);
			 map.put("ybtj", statisbyyblxlist);
		}else if("getYblxByMon".equals(method)) {
			//标本按月查询
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> statisbyyblxlist = sjxxService.getStatisByYblx(sjxxDto);
			map.put("ybtj", statisbyyblxlist);
		}else if("getYblxByWeek".equals(method)) {
			//标本按周查询
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybsList=sjxxService.getStatisByYblx(sjxxDto);
			 map.put("ybtj", ybsList);
		}else if("getYblxByDay".equals(method)) {
			//标本按日查询
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("ybtj", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybsList=sjxxService.getStatisByYblx(sjxxDto);
			 map.put("ybtj", ybsList);
		}else if("getSjxxDRByYear".equals(method)) {
			//检测总次数按年查询
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String,String>> jcxmnum=sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcxmnum", jcxmnum);
		}else if("getSjxxDRByMon".equals(method)) {
			//检测总次数按月查询
			setDateByMonth(sjxxDto,-6);
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> jcxmnum = sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcxmnum", jcxmnum);
		}else if("getSjxxDRByWeek".equals(method)) {
			//检测总次数按周查询
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jcxmnum=sjxxService.getSjxxDRByWeek(sjxxDto);
			map.put("jcxmnum", jcxmnum);
		}else if("getSjxxDRByDay".equals(method)) {
			//检测总次数按日查询
			sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jcxmnum=sjxxService.getSjxxDRBySy(sjxxDto);
			map.put("jcxmnum", jcxmnum);
		}else if("getKsByYear".equals(method)) {
			//科室的圆饼统计图(年)
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> ksList = sjxxService.getKsByweek(sjxxDto);
			 map.put("ksList", ksList);
		}else if("getKsByMon".equals(method)) {
			//科室的圆饼统计图(月)
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> ksList = sjxxService.getKsByweek(sjxxDto);
			map.put("ksList", ksList);
		}else if("getKsByWeek".equals(method)) {
			//科室的圆饼统计图(周)
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ksList=sjxxService.getKsByweek(sjxxDto);
			 map.put("ksList", ksList);
		}else if("getKsByDay".equals(method)) {
			//科室的圆饼统计图(日)
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqstart());
			List<Map<String, String>> ksList=sjxxService.getKsByweek(sjxxDto);
			map.put("ksList", ksList);
		}
		else if("getjyjgOfYblxByYear".equals(method)) {
			//标本阳性率（年）
			setDateByYear(sjxxDto,0,true);
			sjxxDto.getZqs().put("jyjgOfYblx", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> jyjgOfYblx = sjxxService.getJyjgByYblx(sjxxDto);
			 map.put("jyjgOfYblx", jyjgOfYblx);
		}else if("getjyjgOfYblxByMon".equals(method)) {
			//标本阳性率(月)
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("jyjgOfYblx", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> jyjgOfYblx = sjxxService.getJyjgByYblx(sjxxDto);
			map.put("jyjgOfYblx", jyjgOfYblx);
		}else if("getjyjgOfYblxByWeek".equals(method)) {
			//标本阳性率(周)
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("jyjgOfYblx", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> jyjgOfYblx=sjxxService.getJyjgByYblx(sjxxDto);
			 map.put("jyjgOfYblx", jyjgOfYblx);
		}else if("getjyjgOfYblxByDay".equals(method)) {
			//标本阳性率(日)
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("jyjgOfYblx", sjxxDto.getJsrqstart());
			List<Map<String, String>> jyjgOfYblx=sjxxService.getJyjgByYblx(sjxxDto);
			map.put("jyjgOfYblx", jyjgOfYblx);
		}
		else if("getYbxxTypeByYear".equals(method)) {
			//收费标本检测项目次数（年）
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String,String>> ybxxType=sjxxService.getYbxxTypeBYWeek(sjxxDto);
			map.put("ybxxType", ybxxType);
		}else if("getYbxxTypeByMon".equals(method)) {
			//检测总次数按月查询
			setDateByMonth(sjxxDto,-6);
			sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> ybxxType = sjxxService.getYbxxTypeBYWeek(sjxxDto);
			map.put("ybxxType", ybxxType);
		}else if("getYbxxTypeByWeek".equals(method)) {
			sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybxxType=sjxxService.getWeekYbxxType(sjxxDto);
			map.put("ybxxType", ybxxType);
		}else if("getYbxxTypeByDay".equals(method)) {
			//检测总次数按日查询
			sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybxxType=sjxxService.getYbxxTypeBYWeek(sjxxDto);
			map.put("ybxxType", ybxxType);
		}
		else if("getDbByYear".equals(method)) {
			//合作伙伴按年
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
			List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(sjxxDto);
			map.put("dbtj",dblist);
		}else if("getDbByMon".equals(method)) {
			//合作伙伴按月
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
			List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(sjxxDto);
			map.put("dbtj",dblist);
		}else if("getDbByWeek".equals(method)) {
			//合作伙伴按周
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			List<Map<String, String>> dblist=sjxxService.getStatisWeekBysjhb(sjxxDto);
			map.put("dbtj",dblist);
		}else if("getDbByDay".equals(method)) {
			//合作伙伴按日
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			List<Map<String, String>> dblist=sjxxService.getStatisBysjhb(sjxxDto);
			map.put("dbtj",dblist);
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
		}else if("getSjxxYearBySy".equals(method)) {
			//标本情况按年
			setDateByYear(sjxxDto,-6,false);
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String, String>> ybqklist=sjxxService.getSjxxBySy(sjxxDto);
			map.put("ybqk",ybqklist);
		}else if("getSjxxMonBySy".equals(method)) {
			//标本情况按月
			setDateByMonth(sjxxDto,-6);
			sjxxDto.setTj("mon");
			sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String, String>> ybqklist=sjxxService.getSjxxBySy(sjxxDto);
			map.put("ybqk",ybqklist);
		}else if("getSjxxWeekBySy".equals(method)){
			//标本情况按周
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybqklist=sjxxService.getSjxxWeekBySy(sjxxDto);
			map.put("ybqk",ybqklist);
		}else if("getSjxxDayBySy".equals(method)){
			//标本情况按日
			//setDateByDay(sjxxDto,-6);
			sjxxDto.setTj("day");
			sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String, String>> ybqklist=sjxxService.getSjxxBySy(sjxxDto);
			map.put("ybqk",ybqklist);
		}else if("getFlByYear".equals(method)){
			//分类情况按年
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
			sjxxDto.setTj("year");
			
			Map<String,List<Map<String, String>>> fllists=sjxxService.getStatisByFl(sjxxDto);
			
			for(String map1 : fllists.keySet()){
				map.put("fltj_"+map1,fllists.get(map1));
			}
		}else if("getFlByMon".equals(method)){
			//分类情况按月
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("mon");
			Map<String,List<Map<String, String>>> fllists=sjxxService.getStatisByFl(sjxxDto);
			
			for(String map1 : fllists.keySet()){
				map.put("fltj_"+map1,fllists.get(map1));
			}
		}else if("getFlByWeek".equals(method)){
			//分类情况按周
//			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
			sjxxDto.setTj("week");
			Map<String,List<Map<String, String>>> fllists=sjxxService.getStatisByWeekFl(sjxxDto);
			
			for(String map1 : fllists.keySet()){
				map.put("fltj_"+map1,fllists.get(map1));
			}
		}else if("getFlByDay".equals(method)){
			//分类情况按日
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("day");
			Map<String,List<Map<String, String>>> fllists=sjxxService.getStatisByFl(sjxxDto);
			
			for(String map1 : fllists.keySet()){
				map.put("fltj_"+map1,fllists.get(map1));
			}
		}else if("getHbByYear".equals(method)){
			//伙伴情况按年
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
			sjxxDto.setTj("year");
			
			List<Map<String, String>> fllist=sjxxService.getStatisByTjHbfl(sjxxDto);
			Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
			for(String map1 : fllists.keySet()){
				map.put("hbtj_"+map1,fllists.get(map1));
			}
		}else if("getHbByMon".equals(method)){
			//伙伴情况按月
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("mon");
			List<Map<String, String>> fllist=sjxxService.getStatisByTjHbfl(sjxxDto);
			Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
			
			for(String map1 : fllists.keySet()){
				map.put("hbtj_"+map1,fllists.get(map1));
			}
		}else if("getHbByWeek".equals(method)){
			//伙伴情况按周
//			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
			sjxxDto.setTj("week");
			List<Map<String, String>> fllist=sjxxService.getStatisByWeekTjHbfl(sjxxDto);
			Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
			for(String map1 : fllists.keySet()){
				map.put("hbtj_"+map1,fllists.get(map1));
			}
		}else if("getHbByDay".equals(method)){
			//伙伴情况按日
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.setTj("day");
			List<Map<String, String>> fllist=sjxxService.getStatisByTjHbfl(sjxxDto);
			Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
			for(String map1 : fllists.keySet()){
				map.put("hbtj_"+map1,fllists.get(map1));
			}
		}else if("getAllhbByYear".equals(method)) {
			//合作伙伴总数按年
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
			List<Map<String, String>> fllist=sjxxService.getStatisByTj(sjxxDto);
			map.put("hbztj",fllist);
		}else if("getAllhbByMon".equals(method)) {
			//合作伙伴总数按月
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
			sjxxDto.setTj("mon");
			List<Map<String, String>> fllist=sjxxService.getStatisByTj(sjxxDto);
			map.put("hbztj",fllist);
		}else if("getAllhbByWeek".equals(method)) {
			//合作伙伴总数按周
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			sjxxDto.setTj("week");
			List<Map<String, String>> fllist=sjxxService.getStatisByTj(sjxxDto);
			map.put("hbztj",fllist);
		}else if("getAllhbByDay".equals(method)) {
			//合作伙伴总数按日
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			sjxxDto.setTj("day");
			List<Map<String, String>> fllist=sjxxService.getStatisByTj(sjxxDto);
			map.put("hbztj",fllist);
		}else if("getFjsqByYear".equals(method)) {
			//复检按年查询
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setLrsjYstart(sjxxDto.getJsrqYstart());
			fjsqDto.setLrsjYend(sjxxDto.getJsrqYend());
			List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			map.put("fjsq", fjsqMap);
		}else if("getFjsqByMon".equals(method)) {
			//复检按月查询
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setLrsjMstart(sjxxDto.getJsrqMstart());
			fjsqDto.setLrsjMend(sjxxDto.getJsrqMend());
			List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			map.put("fjsq", fjsqMap);
		}else if("getFjsqByWeek".equals(method)) {
			//复检按周查询
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setLrsjstart(sjxxDto.getJsrqstart());
			fjsqDto.setLrsjend(sjxxDto.getJsrqend());
			List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			map.put("fjsq", fjsqMap);
		}else if("getFjsqByDay".equals(method)) {
			//复检按日查询
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setLrsjstart(sjxxDto.getJsrqstart());
			fjsqDto.setLrsjend(sjxxDto.getJsrqend());
			List<Map<String,String>> fjsqMap=sjxxService.getFjsqByDay(fjsqDto);
			map.put("fjsq", fjsqMap);
		}else if("getFqybByYear".equals(method)) {
			//废弃标本按年查询
			setDateByYear(sjxxDto,0,false);
			sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
			List<Map<String,String>>fqybMap=sjxxService.getFqybByYbzt(sjxxDto); 
			map.put("fqyb",fqybMap);
		}else if("getFqybByMon".equals(method)) {
			//废弃标本按月查询
			setDateByMonth(sjxxDto,0);
			sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
			List<Map<String,String>>fqybMap=sjxxService.getFqybByYbzt(sjxxDto); 
			map.put("fqyb",fqybMap);
		}else if("getFqybByWeek".equals(method)) {
			//废弃标本按周查询
			setDateByWeek(sjxxDto);
			sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,String>>fqybMap=sjxxService.getFqybByYbzt(sjxxDto); 
			map.put("fqyb",fqybMap);
		}else if("getFqybByDay".equals(method)) {
			//废弃标本按日查询
			setDateByDay(sjxxDto,0);
			sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			List<Map<String,String>>fqybMap=sjxxService.getFqybByYbzt(sjxxDto); 
			map.put("fqyb",fqybMap);
		}
		map.put("searchData", sjxxDto);
		return map;
	}
	
	/**
	  * 获取标本路线信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/miniprogram/getYblx")
	@ResponseBody
	public Map<String,Object> getYblx(HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String str = request.getParameter("sjxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjxxDto sjxxDto = JSONObject.toJavaObject(parseObject, SjxxDto.class);
		if(StringUtil.isNotBlank(sjxxDto.getJsrqstart())) {
			List<Map<String,Object>> yblxlist=sjxxService.getSjxlxx(sjxxDto);
			map.put("yblxlist", yblxlist);
		}else {
			map.put("yblxlist", new ArrayList<Map<String,Object>>());
		}
		return map;
	}
	
	/**
	 * 统计伙伴信息页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/weekLeadStatisPartnerPage")
	@ResponseBody
	public Map<String, Object> weekLeadStatisPartnerPage(HttpServletRequest request){
		// TODO Auto-generated method stub
		String str = request.getParameter("sjhbxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjhbxxDto sjhbxxDto = JSONObject.toJavaObject(parseObject, SjhbxxDto.class);
		String strsj = request.getParameter("sjxxDto");
		JSONObject sjObject = JSONObject.parseObject(strsj);
		SjxxDto sjxxDto = JSONObject.toJavaObject(sjObject, SjxxDto.class);
		Map<String, Object> map = new HashMap<>();
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
		sjxxDto.setHbfl(sjhbxxDto.getFl());
		sjxxDto.setHbzfl(sjhbxxDto.getZfl());
		//查询合作伙伴信息
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getTjDtoList(sjhbxxDto);
		map.put("sjhbxxDtos", sjhbxxDtos);
		map.put("sjxxDto",  JSONObject.toJSONString(sjxxDto));
		return map;
	}
	
	/**
	 * 查询伙伴信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/miniprogram/getWeekLeadStatisPartner")
	@ResponseBody
	public Map<String, Object> getWeekLeadStatisPartner(HttpServletRequest request){
		String str = request.getParameter("sjhbxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjhbxxDto sjhbxxDto = JSONObject.toJavaObject(parseObject, SjhbxxDto.class);
		String strsj = request.getParameter("sjxxDto");
		JSONObject sjObject = JSONObject.parseObject(strsj);
		SjxxDto sjxxDto = JSONObject.toJavaObject(sjObject, SjxxDto.class);
		Map<String, Object> map = new HashMap<>();
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getDtoList(sjhbxxDto);
		map.put("sjhbxxDtos", sjhbxxDtos);
		sjxxDto.setHbfl(sjhbxxDto.getFl());
		sjxxDto.setHbzfl(sjhbxxDto.getZfl());
		map.put("sjxxDto", sjxxDto);
		return map;
	}
	
	/**
	 * 查询汇报领导的伙伴统计
	 * 
	 * @return
	 */
	@RequestMapping("/miniprogram/getWeekLeadPartnerStatis")
	@ResponseBody
	public Map<String, Object> getWeekLeadPartnerStatis(HttpServletRequest request){
		String strsj = request.getParameter("sjxxDto");
		JSONObject sjObject = JSONObject.parseObject(strsj);
		SjxxDto sjxxDto = JSONObject.toJavaObject(sjObject, SjxxDto.class);
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
		if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2){
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			} else{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			}
		}else if(StringUtil.isBlank(sjxxDto.getBgrqstart())){
			sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
			sjxxDto.setBgrqend(sjxxDto.getJsrqend());
		}
		Map<String, Object> map = new HashMap<>();
		
		//按伙伴查询
		String zqString = "0";
		if(StringUtil.isNotBlank(sjxxDto.getZq())) {
			zqString = sjxxDto.getZq().split(",")[0];
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		sjxxDto.setTj("day");
		sjxxDto.setJsrqstart(dateFormat.format(DateUtils.getDate(DateUtils.parseDate("yyyy-MM-dd",sjxxDto.getJsrqend()), - Integer.parseInt(zqString))));
		List<Map<String, String>> fllist=sjxxService.getStatisByTjHbfl(sjxxDto);
		map.put("hbztj", fllist);
		sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
		Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
		
		for(String map1 : fllists.keySet()){
			map.put("hbtj_"+map1,fllists.get(map1));
		}
		map.put("searchData", sjxxDto);
		return map;
	}
	
}
