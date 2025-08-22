package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.dao.entities.XszbDto;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXszbService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.util.WechatCommonUtils;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.HbqxDto;
import com.matridx.igams.wechat.dao.entities.SjbgsmDto;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjnyxDto;
import com.matridx.igams.wechat.dao.entities.SjwzxxDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjybztDto;
import com.matridx.igams.wechat.dao.entities.SjzmjgDto;
import com.matridx.igams.wechat.service.svcinterface.IDdyhService;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjnyxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxResStatisticService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxStatisticService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxTwoService;
import com.matridx.igams.wechat.service.svcinterface.ISjzmjgService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ws")
public class WechatWsStatictisController extends BaseController{
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
//	@Value("${matridx.wechat.externalurl:}")
//	String externalurl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private ISjxxService sjxxService;
	@Autowired
	private IHbqxService hbqxService;
	@Autowired
	private IFjsqService fjsqService;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	private ISjnyxService sjnyxService;
	@Autowired
	private IFjcfbService fjcfbService;
	@Autowired
	private ISjbgsmService sjbgsmservice;
	@Autowired
	private ISjhbxxService sjhbxxService;	
	@Autowired
	private ISjxxStatisticService SjxxStatisticService;
	@Autowired
	private IDdyhService ddyhService;
	@Autowired
	private ISjxxStatisticService sjxxStatisticService;
	@Autowired
	private ISjzmjgService sjzmjgService;
	@Autowired
	private IXszbService xszbService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	WechatCommonUtils wechatCommonUtils;
	@Autowired
	ISjxxResStatisticService sjxxResStatisticService;
	@Autowired
	ISjxxTwoService sjxxTwoService;
	@Autowired
	IXxdyService xxdyService;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	private Logger logger = LoggerFactory.getLogger(WechatWsStatictisController.class);
	/**
	 * 接收微信用户统计
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/statictis/statictisByYhid")
	public ModelAndView statictisDayilyByYhid(HttpServletRequest req, HttpServletResponse resp) {
		ModelAndView mav=new ModelAndView("common/view/display_view");
		String jsrq = req.getParameter("jsrq");
		String jsrqstart = req.getParameter("jsrqstart");
		String jsrqend = req.getParameter("jsrqend");
		String yhid = req.getParameter("yhid");
		String sign = req.getParameter("sign");
		String flg = req.getParameter("flg");
//		String netflg = req.getParameter("netflg");
		String url=null;
		//if(StringUtil.isNotBlank(netflg) && netflg.equals("inter")){
			//内网
			if("dayily".equals(flg)) {
				url=applicationurl+"/ws/statictis/dailyByYhid?jsrq="+jsrq+"&yhid="+yhid+"&sign="+sign;
			}else if("weekly".equals(flg)) {
				url=applicationurl+"/ws/statictis/weeklyByYhidAndJsrq?jsrqstart="+jsrqstart+"&jsrqend="+jsrqend+"&yhid="+yhid+"&sign="+sign;
			}
			mav.addObject("view_url",url);
		/*}else{
			//外网
			if("dayily".equals(flg)) {
				url=externalurl+"/ws/statictis/dailyByYhid?jsrq="+jsrq+"&yhid="+yhid+"&sign="+sign;
			}else if("weekly".equals(flg)) {
				url=externalurl+"/ws/statictis/weeklyByYhid?jsrqstart="+jsrqstart+"&jsrqend="+jsrqend+"&yhid="+yhid+"&sign="+sign;
			}
			mav.addObject("view_url",url);
		}*/
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 微信用户访问日报统计
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statictis/dailyByYhid")
	public ModelAndView dailyByYhid(SjxxDto sjxxDto,HttpServletRequest req) {
		boolean result=commonService.checkSign(sjxxDto.getSign(), sjxxDto.getYhid()+sjxxDto.getJsrq(), req);
		if(result) {
			ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/statistic_daily");
			String sign=commonService.getSign(sjxxDto.getJsrq());
			sjxxDto.setSign(sign);
			String xsbj = req.getParameter("xsbj");
			mav.addObject("sjxxDto", sjxxDto);
			mav.addObject("xsbj", xsbj);
			//查询区域信息
			XszbDto xszbDto = new XszbDto();
			xszbDto.setJsmc("大区经理");
			List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
			mav.addObject("xszbDtos",xszbDtos);
			return mav;
		}else {
            return commonService.jumpError();
		}
	}
	/**
	 * 微信用户访问日报统计(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statictis/dailyByYhidAndJsrq")
	public ModelAndView dailyByYhidAndJsrq(SjxxDto sjxxDto,HttpServletRequest req) {
		boolean result=commonService.checkSign(sjxxDto.getSign(), sjxxDto.getYhid()+sjxxDto.getJsrq(), req);
		if(result) {
			ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/statistic_daily_jsrq");
			String sign=commonService.getSign(sjxxDto.getJsrq());
			sjxxDto.setSign(sign);
			String xsbj = req.getParameter("xsbj");
			mav.addObject("sjxxDto", sjxxDto);
			mav.addObject("xsbj", xsbj);
			//查询区域信息
			XszbDto xszbDto = new XszbDto();
			xszbDto.setJsmc("大区经理");
			List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
			mav.addObject("xszbDtos",xszbDtos);
			return mav;
		}else {
            return commonService.jumpError();
		}
	}
	
	/**
	 * 返回微信用户访问日报统计
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statictis/backDailyByYhid")
	public ModelAndView backDailyByYhid(SjxxDto sjxxDto,HttpServletRequest req) {
		ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/statistic_daily");
		String sign=commonService.getSign(sjxxDto.getJsrq());
		sjxxDto.setSign(sign);
		String xsbj = req.getParameter("xsbj");
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("xsbj", xsbj);
		//查询区域信息
		XszbDto xszbDto = new XszbDto();
		xszbDto.setJsmc("大区经理");
		List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
		mav.addObject("xszbDtos",xszbDtos);
		return mav;
	}
	/**
	 * 返回微信用户访问日报统计(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statictis/backDailyByYhidAndJsrq")
	public ModelAndView backDailyByYhidAndJsrq(SjxxDto sjxxDto,HttpServletRequest req) {
		ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/statistic_daily_jsrq");
		String sign=commonService.getSign(sjxxDto.getJsrq());
		sjxxDto.setSign(sign);
		String xsbj = req.getParameter("xsbj");
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("xsbj", xsbj);
		//查询区域信息
		XszbDto xszbDto = new XszbDto();
		xszbDto.setJsmc("大区经理");
		List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
		mav.addObject("xszbDtos",xszbDtos);
		return mav;
	}
	
	@RequestMapping("/ststictis/go_back_dailyByYhid")
	public ModelAndView go_back_dailyByYhid(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/statistic_daily");
		String sign=commonService.getSign(sjxxDto.getJsrq());
		sjxxDto.setSign(sign);
		//查询区域信息
		XszbDto xszbDto = new XszbDto();
		xszbDto.setJsmc("大区经理");
		List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
		mav.addObject("xszbDtos",xszbDtos);
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}
	@RequestMapping("/ststictis/go_back_dailyByYhid_jsrq")
	public ModelAndView go_back_dailyByYhid_jsrq(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/statistic_daily_jsrq");
		String sign=commonService.getSign(sjxxDto.getJsrq());
		sjxxDto.setSign(sign);
		//查询区域信息
		XszbDto xszbDto = new XszbDto();
		xszbDto.setJsmc("大区经理");
		List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
		mav.addObject("xszbDtos",xszbDtos);
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}
	
	/**
	 * 微信用户日报查询数据
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@RequestMapping("/statictis/getdailymodel")
	@ResponseBody
	public Map<String, Object> getdailymodel(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		boolean checkSign=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getJsrq(),request);
		if(checkSign) {
			if(StringUtil.isNotBlank(sjxxDto.getYhid())) {
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
					List<Map<String,String>>YbxxBySjh_daily_Map=SjxxStatisticService.getYbxxBySjhbDaily(sjxxDto);
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
					if(sjhbMap != null && sjhbMap.size()>39) {
						List<Map<String,String>> asc_YbxxBySjh_daily_Map= new ArrayList<>();
						asc_YbxxBySjh_daily_Map.addAll(sjhbMap.subList(0,39));
						map.put("sjhb", asc_YbxxBySjh_daily_Map);
						Collections.reverse(sjhbMap); // 倒序排列
					}else {
						map.put("sjhb", sjhbMap);
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
	            List<SjxxDto> notSylist=SjxxStatisticService.getNotSyListDaily(sjxxDto);
	            map.put("notSylist",notSylist);
	            //销售人员销售达成率日统计，默认当前季度
				XszbDto xszbDto = new XszbDto();
				Date searchDate = DateUtils.parseDate("yyyy-MM", sjxxDto.getJsrq());
				String start = DateUtils.format(searchDate);
				String end = "";
				xszbDto.setKszq(start);
				xszbDto.setJszq(end);
				xszbDto.setYhid(sjxxDto.getYhid());
				xszbDto.setZblxcsmc("Q");
				Map<String,Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
				if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
					for (String map1 : salesAttainmentRate.keySet()) {
						map.put("xszblist_"+map1,salesAttainmentRate.get(map1));
					}
				}
				map.put("searchData", sjxxDto);
					
			}
			return map;
		}
		return map;
	}

	/**
	 * 微信用户日报查询数据(接收日期)
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@RequestMapping("/statictis/getdailymodelByJsrq")
	@ResponseBody
	public Map<String, Object> getdailymodelByJsrq(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		boolean checkSign=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getJsrq(),request);
		if(checkSign) {
			if(StringUtil.isNotBlank(sjxxDto.getYhid())) {
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
					List<Map<String,String>>YbxxBySjh_daily_Map=SjxxStatisticService.getYbxxBySjhbDailyAndJsrq(sjxxDto);
					if(YbxxBySjh_daily_Map.size()>39) {
						List<Map<String,String>> asc_YbxxBySjh_daily_Map= new ArrayList<>();
						asc_YbxxBySjh_daily_Map.addAll(YbxxBySjh_daily_Map.subList(0,39));
						map.put("sjhb", asc_YbxxBySjh_daily_Map);
						Collections.reverse(YbxxBySjh_daily_Map); // 倒序排列
						List<Map<String,String>> desc_YbxxBySjh_daily_Map=YbxxBySjh_daily_Map.subList(0,39);
						map.put("sjhb_t", desc_YbxxBySjh_daily_Map);
					}else {
						map.put("sjhb", YbxxBySjh_daily_Map);
					}
				}else{
					//合作伙伴统计
					List<Map<String,String>> sjhbMap = sjxxService.getYbxxByTjmcAndJsrq(sjxxDto);
					if(sjhbMap != null && sjhbMap.size()>39) {
						List<Map<String,String>> asc_YbxxBySjh_daily_Map= new ArrayList<>();
						asc_YbxxBySjh_daily_Map.addAll(sjhbMap.subList(0,39));
						map.put("sjhb", asc_YbxxBySjh_daily_Map);
						Collections.reverse(sjhbMap); // 倒序排列
						List<Map<String,String>> desc_YbxxBySjh_daily_Map=sjhbMap.subList(0,39);
						map.put("sjhb_t", desc_YbxxBySjh_daily_Map);
					}else {
						map.put("sjhb", sjhbMap);
					}
				}
				//标本信息
				List<Map<String,String>> ybxx_daily_Map=SjxxStatisticService.getYbxxDailyByJsrq(sjxxDto);
				map.put("ybxx", ybxx_daily_Map);
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
				List<SjxxDto> sjxxList_daily=SjxxStatisticService.getSjxxListDailyByJsrq(sjxxDto);
				map.put("sjxxList",sjxxList_daily);
				//检测项目个数
	            List<Map<String,String>> jcxmDRList_daily=SjxxStatisticService.getJcxmSumDailyByJsrq(sjxxDto);
	            map.put("jcxmnum",jcxmDRList_daily);
	            //收费标本里边检测项目条数
	            List<Map<String,String>> jcxmTypeList_daily=SjxxStatisticService.getJcxmInSfsfDailyByJsrq(sjxxDto);
	            map.put("jcxmType",jcxmTypeList_daily);
	            List<SjxxDto> notSylist=SjxxStatisticService.getNotSyListDailyByJsrq(sjxxDto);
	            map.put("notSylist",notSylist);
	            //销售人员销售达成率日统计，默认当前季度
				XszbDto xszbDto = new XszbDto();
				Date searchDate = DateUtils.parseDate("yyyy-MM", sjxxDto.getJsrq());
				String start = DateUtils.format(searchDate);
				String end = "";
				xszbDto.setKszq(start);
				xszbDto.setJszq(end);
				xszbDto.setYhid(sjxxDto.getYhid());
				xszbDto.setZblxcsmc("Q");
				Map<String,Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRateByJsrq(xszbDto);
				if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
					for (String map1 : salesAttainmentRate.keySet()) {
						map.put("xszblist_"+map1,salesAttainmentRate.get(map1));
					}
				}
				map.put("searchData", sjxxDto);

			}
			return map;
		}
		return map;
	}

	/**
	 * 微信用户日报查询复检清单
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping(value ="/ststictis/fjsqViewDaily")
	public ModelAndView fjsqViewDaily(FjsqDto fjsqDto) {
		ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/recheck_view");
		mav.addObject("fjsqDto", fjsqDto);
		return mav;
	}
	/**
	 * 微信用户日报查询复检清单(接收日期)
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping(value ="/ststictis/fjsqViewDailyByJsrq")
	public ModelAndView fjsqViewDailyByJsrq(FjsqDto fjsqDto) {
		ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/recheck_view_jsrq");
		mav.addObject("fjsqDto", fjsqDto);
		return mav;
	}
	
	/**
	 * 微信用户日报查询复检列表
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/ststictis/recheckListDaily")
	@ResponseBody
	public Map<String, Object> recheckListDaily(FjsqDto fjsqDto){
		Map<String, Object> map= new HashMap<>();
		if("null".equals(fjsqDto.getLrsj())) {
			fjsqDto.setLrsj("");
		}
		if(fjsqDto.getYhid()!=null) {
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
		map.put("total", fjsqDto.getTotalNumber());
		return map;
	}
	
	/**
	 * 微信统计废弃标本点击页面
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping(value ="/ststictis/fqybViewDaily")
	public ModelAndView fqybViewDaily(SjybztDto sjybztDto) {
		ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/sampleState_view");
		if(sjybztDto.getZt().equals("0")) {
			sjybztDto.setZtflg("0");
		}else {
			sjybztDto.setZtflg("1");
		}
		mav.addObject("sjybztDto", sjybztDto);
		return mav;
	}
	/**
	 * 微信统计废弃标本点击页面(接收日期)
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping(value ="/ststictis/fqybViewDailyByJsrq")
	public ModelAndView fqybViewDailyByJsrq(SjybztDto sjybztDto) {
		ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/sampleState_view_jsrq");
		if(sjybztDto.getZt().equals("0")) {
			sjybztDto.setZtflg("0");
		}else {
			sjybztDto.setZtflg("1");
		}
		mav.addObject("sjybztDto", sjybztDto);
		return mav;
	}

	/**
	 * 微信统计废弃标本
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping("/ststictis/sampleStateListDaily")
	@ResponseBody
	public Map<String, Object> sampleStateListDaily(SjybztDto sjybztDto){
		Map<String, Object> map= new HashMap<>();
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
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping("/ststictis/getpercentageDaily")
	@ResponseBody
	public Map<String, String> getpercentageDaily(SjybztDto sjybztDto){
		Map<String,String> map= new HashMap<>();
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

	/**
	 * 微信统计废弃标本占比(接收日期)
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping("/ststictis/getpercentageDailyByJsrq")
	@ResponseBody
	public Map<String, String> getpercentageDailyByJsrq(SjybztDto sjybztDto){
		Map<String,String> map= new HashMap<>();
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
							Integer sjxxNum=sjxxService.getCountAllSjxxByJsrq(sjybztDto);
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


	/**
	 * 微信统计阴阳性列表
	 * @param sjwzxxDto
	 * @return
	 */
	@RequestMapping("/ststictis/positiveViewDaily")
	public ModelAndView positiveViewDaily(SjwzxxDto sjwzxxDto) {
		if(sjwzxxDto.getYhid()!=null) {
			List<HbqxDto> hbqxList=hbqxService.getHbxxByYhid(sjwzxxDto.getYhid());
			if(hbqxList!=null&& hbqxList.size()>0) {
				ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/repositive_view");
				List<String> strList= new ArrayList<>();
				for (int i = 0; i < hbqxList.size(); i++){
					strList.add(hbqxList.get(i).getHbmc());
				}
				sjwzxxDto.setSjhbs(strList);
				List<SjxxDto> sjxxList=sjxxService.getListPositive(sjwzxxDto);
				mav.addObject("sjxxList",sjxxList);
				mav.addObject("sjwzxxDto", sjwzxxDto);
			return mav;
			}else {
                return commonService.jumpError();
			}
		}else {
            return commonService.jumpError();
		}
	}

	/**
	 * 微信统计阴阳性列表(接收日期)
	 * @param sjwzxxDto
	 * @return
	 */
	@RequestMapping("/ststictis/positiveViewDailyByJsrq")
	public ModelAndView positiveViewDailyByJsrq(SjwzxxDto sjwzxxDto) {
		if(sjwzxxDto.getYhid()!=null) {
			List<HbqxDto> hbqxList=hbqxService.getHbxxByYhid(sjwzxxDto.getYhid());
			if(hbqxList!=null&& hbqxList.size()>0) {
				ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/repositive_view_jsrq");
				List<String> strList= new ArrayList<>();
				for (int i = 0; i < hbqxList.size(); i++){
					strList.add(hbqxList.get(i).getHbmc());
				}
				sjwzxxDto.setSjhbs(strList);
				List<SjxxDto> sjxxList=sjxxService.getListPositive(sjwzxxDto);
				mav.addObject("sjxxList",sjxxList);
				mav.addObject("sjwzxxDto", sjwzxxDto);
			return mav;
			}else {
                return commonService.jumpError();
			}
		}else {
            return commonService.jumpError();
		}
	}

	/**
	 * 查看送检信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/ststictis/sjxxViewDaily")
	public ModelAndView viewSjxx(SjxxDto sjxxDto) {
		ModelAndView mav = new ModelAndView("wechat/statistic_wxyh/sjxx_ListView");
		List<SjnyxDto> sjnyx = sjnyxService.getNyxBySjid(sjxxDto);
		SjxxDto sjxxDto2 = sjxxService.getDto(sjxxDto);
		if ("1".equals(sjxxDto2.getYyxxCskz1())) {
			sjxxDto2.setHospitalname(sjxxDto2.getHospitalname() + "-" + sjxxDto2.getSjdwmc());
		}
		List<SjwzxxDto> sjwzxx = sjxxService.selectWzxxBySjid(sjxxDto2);
		if (sjwzxx != null && sjwzxx.size() > 0) {
			String xpxx = sjwzxx.get(0).getXpxx();// 由于一个标本中的物种芯片信息相同，取其一
			mav.addObject("Xpxx", xpxx);
		}

//		if (("Z6").equals(sjxxDto2.getCskz1()) || ("Z12").equals(sjxxDto2.getCskz1()) || ("Z18").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())) {
		if (("Z").equals(sjxxDto2.getCskz1()) ||("Z6").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())) {
			List<SjzmjgDto> sjzmList;
			SjzmjgDto sjzmjgDto = new SjzmjgDto();
			sjzmjgDto.setSjid(sjxxDto.getSjid());
			sjzmList = sjzmjgService.getDtoList(sjzmjgDto);
			mav.addObject("sjzmList", sjzmList);
			mav.addObject("KZCS", sjxxDto2.getCskz1());
		}
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());

		fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionPdfYwlxs());

		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);

		fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionWordYwlxs());
		List<FjcfbDto> zhwj = fjcfbService.selectzhpdf(fjcfbDto);
		fjcfbDto.setYwlxs(null);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		SjbgsmDto sjbgsmdto = new SjbgsmDto();
		sjbgsmdto.setSjid(sjxxDto.getSjid());
		List<SjbgsmDto> sjbgsmxx = sjbgsmservice.selectSjbgBySjid(sjbgsmdto);

		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.DETECT_TYPE });
		List<JcsjDto> jcxmlist = jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> t_jcxmlist= new ArrayList<>();//用于结果页
		List<JcsjDto> c_jcxmlist= new ArrayList<>();//用于详细页

		if (jcxmlist != null && jcxmlist.size() > 0) {
			for (int i = 0; i < jcxmlist.size(); i++) {
				
				boolean wz_sftj=false;//判断对应该检测项目的物种信息是否存在，若存在一个则添加该项目
				if(sjwzxx!=null && sjwzxx.size()>0) {
					for(int j=0;j<sjwzxx.size();j++) {
						if(sjwzxx.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
							wz_sftj=true;
							break;
						}
					}
				}
				if(wz_sftj) 
					c_jcxmlist.add(jcxmlist.get(i));
				
				boolean sftj = false;// 判断对应该检测项目的报告说明和附件是否存在，若其中一个存在添加该项目
				if (sjbgsmxx != null && sjbgsmxx.size() > 0) {
					for (int j = 0; j < sjbgsmxx.size(); j++) {
						if (sjbgsmxx.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
							sftj = true;
							break;
						}
					}
				}
				if (t_fjcfbDtos != null && t_fjcfbDtos.size() > 0) {
					for (int j = 0; j < t_fjcfbDtos.size(); j++) {
						if (t_fjcfbDtos.get(j).getYwlx()
								.equals((jcxmlist.get(i).getCskz3() + jcxmlist.get(i).getCskz1()))) {
							sftj = true;
							break;
						}
					}
				}
				if (fjcfbDtos != null && fjcfbDtos.size() > 0) {
					for (int j = 0; j < fjcfbDtos.size(); j++) {
						if (fjcfbDtos.get(j).getYwlx()
								.equals((jcxmlist.get(i).getCskz3() + "_" + jcxmlist.get(i).getCskz1()))) {
							sftj = true;
							break;
						}
					}
				}
				if (sftj)
					t_jcxmlist.add(jcxmlist.get(i));
			}
		}
		// 查看当前复检申请信息
		FjsqDto fjsqDto = new FjsqDto();
		String[] zts = { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_PASS.getCode() };
		fjsqDto.setZts(zts);
		fjsqDto.setSjid(sjxxDto2.getSjid());
		List<FjsqDto> fjsqList = fjsqService.getListBySjid(fjsqDto);
		mav.addObject("fjsqList", fjsqList);
		mav.addObject("SjnyxDto", sjnyx);
		mav.addObject("zhwjpdf", zhwj);
		mav.addObject("sjbgsmList",sjbgsmxx);
		mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
		mav.addObject("sjxxDto", sjxxDto2);	
		mav.addObject("Sjwzxx", sjwzxx);
        mav.addObject("jcxmlist",t_jcxmlist);
        mav.addObject("wzjcxmlist", c_jcxmlist);
		return mav;
	}

	/**
	 * 查看送检信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/ststictis/sjxxViewDailyByJsrq")
	public ModelAndView viewSjxxByJsrq(SjxxDto sjxxDto) {
		ModelAndView mav = new ModelAndView("wechat/statistic_wxyh/sjxx_ListView_jsrq");
		List<SjnyxDto> sjnyx = sjnyxService.getNyxBySjid(sjxxDto);
		SjxxDto sjxxDto2 = sjxxService.getDto(sjxxDto);
		if ("1".equals(sjxxDto2.getYyxxCskz1())) {
			sjxxDto2.setHospitalname(sjxxDto2.getHospitalname() + "-" + sjxxDto2.getSjdwmc());
		}
		List<SjwzxxDto> sjwzxx = sjxxService.selectWzxxBySjid(sjxxDto2);
		if (sjwzxx != null && sjwzxx.size() > 0) {
			String xpxx = sjwzxx.get(0).getXpxx();// 由于一个标本中的物种芯片信息相同，取其一
			mav.addObject("Xpxx", xpxx);
		}

//		if (("Z6").equals(sjxxDto2.getCskz1()) || ("Z12").equals(sjxxDto2.getCskz1()) || ("Z18").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())) {
		if (("Z").equals(sjxxDto2.getCskz1()) ||("Z6").equals(sjxxDto2.getCskz1()) || ("F").equals(sjxxDto2.getCskz1())) {
			List<SjzmjgDto> sjzmList;
			SjzmjgDto sjzmjgDto = new SjzmjgDto();
			sjzmjgDto.setSjid(sjxxDto.getSjid());
			sjzmList = sjzmjgService.getDtoList(sjzmjgDto);
			mav.addObject("sjzmList", sjzmList);
			mav.addObject("KZCS", sjxxDto2.getCskz1());
		}
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(sjxxDto.getSjid());

		fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionPdfYwlxs());

		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);

		fjcfbDto.setYwlxs(wechatCommonUtils.getInspectionWordYwlxs());
		List<FjcfbDto> zhwj = fjcfbService.selectzhpdf(fjcfbDto);
		fjcfbDto.setYwlxs(null);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTION.getCode());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		SjbgsmDto sjbgsmdto = new SjbgsmDto();
		sjbgsmdto.setSjid(sjxxDto.getSjid());
		List<SjbgsmDto> sjbgsmxx = sjbgsmservice.selectSjbgBySjid(sjbgsmdto);

		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.DETECT_TYPE });
		List<JcsjDto> jcxmlist = jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> t_jcxmlist= new ArrayList<>();//用于结果页
		List<JcsjDto> c_jcxmlist= new ArrayList<>();//用于详细页

		if (jcxmlist != null && jcxmlist.size() > 0) {
			for (int i = 0; i < jcxmlist.size(); i++) {

				boolean wz_sftj=false;//判断对应该检测项目的物种信息是否存在，若存在一个则添加该项目
				if(sjwzxx!=null && sjwzxx.size()>0) {
					for(int j=0;j<sjwzxx.size();j++) {
						if(sjwzxx.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
							wz_sftj=true;
							break;
						}
					}
				}
				if(wz_sftj)
					c_jcxmlist.add(jcxmlist.get(i));

				boolean sftj = false;// 判断对应该检测项目的报告说明和附件是否存在，若其中一个存在添加该项目
				if (sjbgsmxx != null && sjbgsmxx.size() > 0) {
					for (int j = 0; j < sjbgsmxx.size(); j++) {
						if (sjbgsmxx.get(j).getJcxmid().equals(jcxmlist.get(i).getCsid())) {
							sftj = true;
							break;
						}
					}
				}
				if (t_fjcfbDtos != null && t_fjcfbDtos.size() > 0) {
					for (int j = 0; j < t_fjcfbDtos.size(); j++) {
						if (t_fjcfbDtos.get(j).getYwlx()
								.equals((jcxmlist.get(i).getCskz3() + jcxmlist.get(i).getCskz1()))) {
							sftj = true;
							break;
						}
					}
				}
				if (fjcfbDtos != null && fjcfbDtos.size() > 0) {
					for (int j = 0; j < fjcfbDtos.size(); j++) {
						if (fjcfbDtos.get(j).getYwlx()
								.equals((jcxmlist.get(i).getCskz3() + "_" + jcxmlist.get(i).getCskz1()))) {
							sftj = true;
							break;
						}
					}
				}
				if (sftj)
					t_jcxmlist.add(jcxmlist.get(i));
			}
		}
		// 查看当前复检申请信息
		FjsqDto fjsqDto = new FjsqDto();
		String[] zts = { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_PASS.getCode() };
		fjsqDto.setZts(zts);
		fjsqDto.setSjid(sjxxDto2.getSjid());
		List<FjsqDto> fjsqList = fjsqService.getListBySjid(fjsqDto);
		mav.addObject("fjsqList", fjsqList);
		mav.addObject("SjnyxDto", sjnyx);
		mav.addObject("zhwjpdf", zhwj);
		mav.addObject("sjbgsmList",sjbgsmxx);
		mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
		mav.addObject("sjxxDto", sjxxDto2);
		mav.addObject("Sjwzxx", sjwzxx);
        mav.addObject("jcxmlist",t_jcxmlist);
        mav.addObject("wzjcxmlist", c_jcxmlist);
		return mav;
	}

	
	/**
	 * 微信用户访问周报统计
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statictis/weeklyByYhid")
	public ModelAndView weeklyByYhid(SjxxDto sjxxDto,HttpServletRequest req) {
		boolean result=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getYhid()+sjxxDto.getJsrqstart()+sjxxDto.getJsrqend(), req);
		if(result) {
			ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/statistic_weekly");
			mav.addObject("t_sign", sjxxDto.getSign());
			String sign=commonService.getSign(sjxxDto.getJsrqstart()+sjxxDto.getJsrqend());
			sjxxDto.setSign(sign);
			//查询合作伙伴分类信息
			SjhbxxDto sjhbxxDto=new SjhbxxDto();
			sjhbxxDto.setXtyhid(sjxxDto.getYhid());
			List<SjhbxxDto> sjhbxxDtos = sjhbxxService.selectFlByWeekly(sjhbxxDto);
			String xsbj = req.getParameter("xsbj");
			mav.addObject("xsbj",xsbj);
			mav.addObject("sjhbxxDtos", sjhbxxDtos);
			mav.addObject("sjxxDto",sjxxDto);
			//查询区域信息
			XszbDto xszbDto = new XszbDto();
			xszbDto.setJsmc("大区经理");
			List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
			mav.addObject("xszbDtos",xszbDtos);
			return mav;
		}else {
            return commonService.jumpError();
		}
	}
	/**
	 * 微信用户访问周报统计(接收日期)(已转为钉钉访问)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statictis/weeklyByYhidAndJsrq")
	public ModelAndView weeklyByYhidAndJsrq(SjxxDto sjxxDto,HttpServletRequest req) {
		boolean result;
		if ("1".equals( req.getParameter("ldtzbj"))){
			result = commonService.checkSign(sjxxDto.getSign(),StringUtil.join(sjxxDto.getPtgss(),",")+sjxxDto.getJsrqstart()+sjxxDto.getJsrqend(), req);
		}else {
			result = commonService.checkSign(sjxxDto.getSign(),sjxxDto.getYhid()+sjxxDto.getJsrqstart()+sjxxDto.getJsrqend(), req);
		}
		if(result) {
			ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/statistic_weekly_jsrq");
			mav.addObject("t_sign", sjxxDto.getSign());
			String sign=commonService.getSign(sjxxDto.getJsrqstart()+sjxxDto.getJsrqend());
			sjxxDto.setSign(sign);
			//查询合作伙伴分类信息
//			SjhbxxDto sjhbxxDto=new SjhbxxDto();
//			sjhbxxDto.setXtyhid(sjxxDto.getYhid());
//			sjhbxxDto.setPtgss(sjxxDto.getPtgss());
//			List<SjhbxxDto> sjhbxxDtos = sjhbxxService.selectFlByWeekly(sjhbxxDto);
			XxdyDto xxdyDto=new XxdyDto();
			xxdyDto.setCskz1("JCSJ");
			xxdyDto.setCskz2(BasicDataTypeEnum.CLASSIFY.getCode());
			List<XxdyDto> xxdyDtos = xxdyService.getListGroupByYxx(xxdyDto);
			if(xxdyDtos!=null&&xxdyDtos.size()>0){
				List<JcsjDto> subclassificationlist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SUBCLASSIFICATION.getCode());
				for(XxdyDto dto:xxdyDtos){
					List<JcsjDto> jcsjDtos=new ArrayList<>();
					for(JcsjDto jcsjDto:subclassificationlist){
						if(dto.getDyxx().indexOf(jcsjDto.getFcsid())!=-1){
							jcsjDtos.add(jcsjDto);
						}
					}
					dto.setJcsjDtos(jcsjDtos);
				}
			}
			mav.addObject("xxdyDtos",xxdyDtos);
			String xsbj = req.getParameter("xsbj");
			mav.addObject("xsbj",xsbj);
//			mav.addObject("sjhbxxDtos", sjhbxxDtos);
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
			String ldtzbj = req.getParameter("ldtzbj");
			mav.addObject("ldtzbj",ldtzbj);
			mav.addObject("ptgss",StringUtil.join(sjxxDto.getPtgss(),","));
			return mav;
		}else {
            return commonService.jumpError();
		}
	}

	/**
	 * 微信用户访问周报统计
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statictis/backWeeklyByYhid")
	public ModelAndView backWeeklyByYhid(SjxxDto sjxxDto,HttpServletRequest req) {
		ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/statistic_weekly");
		mav.addObject("t_sign", sjxxDto.getSign());
		String sign=commonService.getSign(sjxxDto.getJsrqstart()+sjxxDto.getJsrqend());
		sjxxDto.setSign(sign);
		//查询合作伙伴分类信息
		SjhbxxDto sjhbxxDto=new SjhbxxDto();
		sjhbxxDto.setXtyhid(sjxxDto.getYhid());
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.selectFlByWeekly(sjhbxxDto);
		String xsbj = req.getParameter("xsbj");
		mav.addObject("xsbj",xsbj);
		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		mav.addObject("sjxxDto",sjxxDto);
		//查询区域信息
		XszbDto xszbDto = new XszbDto();
		xszbDto.setJsmc("大区经理");
		List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
		mav.addObject("xszbDtos",xszbDtos);
		return mav;
	}
	/**
	 * 微信用户访问周报统计(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statictis/backWeeklyByYhidAndJsrq")
	public ModelAndView backWeeklyByYhidAndJsrq(SjxxDto sjxxDto,HttpServletRequest req) {
		ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/statistic_weekly_jsrq");
		mav.addObject("t_sign", sjxxDto.getSign());
		String sign=commonService.getSign(sjxxDto.getJsrqstart()+sjxxDto.getJsrqend());
		sjxxDto.setSign(sign);
		//查询合作伙伴分类信息
		SjhbxxDto sjhbxxDto=new SjhbxxDto();
		sjhbxxDto.setXtyhid(sjxxDto.getYhid());
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.selectFlByWeekly(sjhbxxDto);
		String xsbj = req.getParameter("xsbj");
		mav.addObject("xsbj",xsbj);
		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		mav.addObject("sjxxDto",sjxxDto);
		//查询区域信息
		XszbDto xszbDto = new XszbDto();
		xszbDto.setJsmc("大区经理");
		List<XszbDto> xszbDtos = xszbService.getXszbQyxx(xszbDto);
		mav.addObject("xszbDtos",xszbDtos);
		return mav;
	}

	/**
	 * 微信用户访问周报统计
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statictis/WeeklyOtherStatictisPage")
	public ModelAndView WeeklyOtherStatictisPage(SjxxDto sjxxDto,HttpServletRequest req) {
		ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/statistic_weekly_other");
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
		if(StringUtil.isNotBlank(sjxxDto.getYhid())) {
			boolean sfdwxd=ddyhService.getDwxd(sjxxDto.getYhid());
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
		}
//		JcsjDto jcsjDto=new JcsjDto();
//		jcsjDto.setJclb(BasicDataTypeEnum.PROVINCE.getCode());
		//List<JcsjDto> sfDtos=jcsjService.getDtoList(jcsjDto);
		List<Map<String, String>> sf_list = sjxxStatisticService.getSfStatisByWeek(sjxxDto);
		mav.addObject("sfDtos", sf_list);
		mav.addObject("sjxxDto", sjxxDto);
		return mav;	
	}
	/**
	 * 微信用户访问周报统计(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statictis/WeeklyOtherStatictisPageByJsrq")
	public ModelAndView WeeklyOtherStatictisPageByJsrq(SjxxDto sjxxDto,HttpServletRequest req) {
		ModelAndView mav=new ModelAndView("wechat/statistic_wxyh/statistic_weekly_other_jsrq");
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
		if(StringUtil.isNotBlank(sjxxDto.getYhid())) {
			boolean sfdwxd=ddyhService.getDwxd(sjxxDto.getYhid());
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
		}
		List<JcsjDto> dyxxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.XXDY_TYPE.getCode());
		for (JcsjDto dyxx : dyxxs) {
			if ("XMFL".equals(dyxx.getCsdm())){
				sjxxDto.setYwlx(dyxx.getCsid());
			}
			if ("HBFL".equals(dyxx.getCsdm())){
				sjxxDto.setYwlx_q(dyxx.getCsid());
			}
		}
		if (ArrayUtils.isEmpty(sjxxDto.getYxxs())){
			XxdyDto xxdyDto = new XxdyDto();
			xxdyDto.setKzcs6("1");
			xxdyDto.setDylxcsdm("XMFL");
			List<XxdyDto> dtoList = xxdyService.getYxxMsg(xxdyDto);
			if (!CollectionUtils.isEmpty(dtoList)){
				List<String> yxxs = new ArrayList<>();
				for (XxdyDto dto : dtoList) {
					yxxs.add(dto.getYxx());
				}
				sjxxDto.setYxxs(yxxs.toArray(new String[0]));
			}
		}
//		JcsjDto jcsjDto=new JcsjDto();
//		jcsjDto.setJclb(BasicDataTypeEnum.PROVINCE.getCode());
		//List<JcsjDto> sfDtos=jcsjService.getDtoList(jcsjDto);
		List<Map<String, String>> sf_list = sjxxStatisticService.getSfStatisByWeekAndJsrq(sjxxDto);
		mav.addObject("sfDtos", sf_list);
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}

	/**
	 * 微信个人统计（省份统计）
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/weekWechatStatisProvincePage")
	public ModelAndView weekLeadStatisProvincePage(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistic_wxyh/statistics_weekly_sf");
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
		JcsjDto sfDto=jcsjService.getDtoById(sjxxDto.getSf());
		if(sfDto!=null)
			sjxxDto.setSfmc(sfDto.getCsmc());
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}
	/**
	 * 微信个人统计（省份统计）(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/weekWechatStatisProvincePageByJsrq")
	public ModelAndView weekLeadStatisProvincePageByJsrq(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistic_wxyh/statistics_weekly_sf_jsrq");
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
		JcsjDto sfDto=jcsjService.getDtoById(sjxxDto.getSf());
		if(sfDto!=null)
			sjxxDto.setSfmc(sfDto.getCsmc());
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}
	
	/**
	 * 小程序查询汇报领导的省份统计
	 * 
	 * @return
	 */
	@RequestMapping("/statistics/getWeekWechatProvinceStatis")
	@ResponseBody
	public Map<String, Object> getWeekWechatProvinceStatis(SjxxDto sjxxDto)
	{
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
		if (StringUtil.isBlank(sjxxDto.getJsrqstart()))
		{
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2)
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			} else
			{
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
        sjxxDto.setTj("day");
		List<String> rqs = sjxxService.getRqsByStartAndEnd(sjxxDto);

		sjxxDto.setRqs(rqs);
		//查询到当前用户下边的合作伙伴
		if(StringUtil.isNotBlank(sjxxDto.getYhid())) {
			boolean sfdwxd=ddyhService.getDwxd(sjxxDto.getYhid());
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
        
			//送检情况统计
			List<Map<String, String>> ybqk=SjxxStatisticService.getYbxxByWeekly(sjxxDto);
			map.put("sfybqk", ybqk);
			sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			//标本信息检测总条数
			List<Map<String,String>> jcxmnum=SjxxStatisticService.getYbxxDRByWeekly(sjxxDto);
			sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			map.put("sfcss", jcxmnum);
			//收费标本下边检测项目的总条数
			List<Map<String,String>> ybxxType=SjxxStatisticService.getYbxxTypeByWeekly(sjxxDto);
			sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			map.put("sfsfcss", ybxxType);
			
			map.put("searchData", sjxxDto);
		}
		return map;
	}
	/**
	 * 小程序查询汇报领导的省份统计(接收日期)(待完成，redis)
	 *
	 * @return
	 */
	@RequestMapping("/statistics/getWeekWechatProvinceStatisByJsrq")
	@ResponseBody
	public Map<String, Object> getWeekWechatProvinceStatisByJsrq(SjxxDto sjxxDto)
	{
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
		if (StringUtil.isBlank(sjxxDto.getJsrqstart()))
		{
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2)
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			} else
			{
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
        sjxxDto.setTj("day");
		List<String> rqs = sjxxService.getRqsByStartAndEnd(sjxxDto);

		sjxxDto.setRqs(rqs);
		//查询到当前用户下边的合作伙伴
		if(StringUtil.isNotBlank(sjxxDto.getYhid())) {
			boolean sfdwxd=ddyhService.getDwxd(sjxxDto.getYhid());
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

			//送检情况统计
			List<Map<String, String>> ybqk=SjxxStatisticService.getYbxxByWeeklyAndJsrq(sjxxDto);
			map.put("sfybqk", ybqk);
			sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			//标本信息检测总条数
			List<Map<String,String>> jcxmnum=SjxxStatisticService.getYbxxDRByWeeklyAndJsrq(sjxxDto);
			sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			map.put("sfcss", jcxmnum);
			//收费标本下边检测项目的总条数
			List<Map<String,String>> ybxxType=SjxxStatisticService.getYbxxTypeByWeeklyAndJsrq(sjxxDto);
			sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
			map.put("sfsfcss", ybxxType);

			map.put("searchData", sjxxDto);
		}
		return map;
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
		if ("NOW".equals(sjxxDto.getTjtj())){
			Date nowDate = new Date();
			calendar.setTime(nowDate);
			int yearDay = calendar.get(Calendar.DAY_OF_YEAR);
			if (yearDay<21){
				calendar.add(Calendar.YEAR,-1);
			}
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
		if ("NOW".equals(sjxxDto.getTjtj())){
			Date nowDate = new Date();
			calendar.setTime(nowDate);
			int monthDay=calendar.get(Calendar.DAY_OF_MONTH);
			if (monthDay<7){
				calendar.add(Calendar.MONTH,-1);
			}
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
	 * 微信用户查询周报数据
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@RequestMapping("/statictis/getweeklymodel")
	@ResponseBody
	public Map<String, Object> getweeklymodel(SjxxDto sjxxDto,HttpServletRequest request){
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
			if(StringUtil.isNotBlank(sjxxDto.getYhid())) {
				boolean sfdwxd=ddyhService.getDwxd(sjxxDto.getYhid());
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
				
				//送检情况统计
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> ybqk=SjxxStatisticService.getYbxxByWeekly(sjxxDto);
				map.put("ybqk", ybqk);

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
                sjxxDto.setJsrqstart(dateFormat.format(DateUtils.getDate(DateUtils.parseDate("yyyy-MM-dd", sjxxDto.getJsrqend()), - Integer.parseInt(zqString))));
				Map<String,List<Map<String, String>>> fllists=SjxxStatisticService.getSjhbFlByWeekly(sjxxDto);
				if(fllists!=null) {
					for(String map1 : fllists.keySet()){
						map.put("fltj_"+map1,fllists.get(map1));
					}
				}
				//销售达成率统计,按季度
				XszbDto xszbDto = new XszbDto();
				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                xszbDto.setKszq(currentYear);
				xszbDto.setJszq(currentYear);
				xszbDto.setYhid(sjxxDto.getYhid());
				xszbDto.setZblxcsmc("Q");
				Map<String,Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
				if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
					for (String map1 : salesAttainmentRate.keySet()) {
						map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
					}
				}
				map.put("searchData", sjxxDto);
			}
		} 
		return map;
	}
	/**
	 * 微信用户查询周报数据(接收日期)(待完成,redis)
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@RequestMapping("/statictis/getweeklymodelByJsrq")
	@ResponseBody
	public Map<String, Object> getweeklymodelByJsrq(SjxxDto sjxxDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		Calendar calendar_t = Calendar.getInstance();
		//时间，可以为具体的某一时间
		Date nowDate = new Date();
		calendar_t.setTime(nowDate);
		int monthDay = calendar_t.get(Calendar.DAY_OF_MONTH);
		boolean result;
		if ("local".equals(request.getParameter("flag"))){
			result= true;
		}else{
			result=commonService.checkSign(sjxxDto.getSign(),sjxxDto.getJsrqstart()+sjxxDto.getJsrqend(), request);
		}
		if(result) {
			if (StringUtil.isBlank(sjxxDto.getJsrqstart())){
				int dayOfWeek = DateUtils.getWeek(new Date());
				if (dayOfWeek <= 2){
					sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 8), "yyyy-MM-dd"));
					sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-2), "yyyy-MM-dd"));
					sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
					sjxxDto.setBgrqend(sjxxDto.getJsrqend());
				}else{
					sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
					sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 5 - dayOfWeek), "yyyy-MM-dd"));
					sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
					sjxxDto.setBgrqend(sjxxDto.getJsrqend());
				}
			}else if(StringUtil.isBlank(sjxxDto.getBgrqstart())){
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			}
			sjxxDto.setTjtj("NOW");
			List<JcsjDto> dyxxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.XXDY_TYPE.getCode());
			for (JcsjDto dyxx : dyxxs) {
				if ("XMFL".equals(dyxx.getCsdm())){
					//项目分类
					sjxxDto.setYwlx(dyxx.getCsid());
				}
				if ("HBFL".equals(dyxx.getCsdm())){
					//伙伴分类
					sjxxDto.setYwlx_q(dyxx.getCsid());
				}
			}
			sjxxDto.setTj("week");
			List<String> yxxList = new ArrayList<>();
			if (ArrayUtils.isEmpty(sjxxDto.getYxxs())){
				XxdyDto xxdyDto = new XxdyDto();
				xxdyDto.setKzcs6("1");
				xxdyDto.setDylxcsdm("XMFL");
				List<XxdyDto> dtoList = xxdyService.getYxxMsg(xxdyDto);
				if (!CollectionUtils.isEmpty(dtoList)){
					List<String> yxxs = new ArrayList<>();
					for (XxdyDto dto : dtoList) {
						yxxs.add(dto.getYxx());
					}
					yxxList = yxxs;
					sjxxDto.setYxxs(yxxs.toArray(new String[0]));
				}
			}
			//查询到当前用户下边的合作伙伴
			if(StringUtil.isNotBlank(sjxxDto.getYhid())|| ArrayUtils.isNotEmpty(sjxxDto.getPtgss())) {
				if (StringUtil.isNotBlank(sjxxDto.getYhid())){
					boolean sfdwxd=ddyhService.getDwxd(sjxxDto.getYhid());
					//如果没有单位限制用户id置为null
					if (!sfdwxd){
						sjxxDto.setYhid(null);
					}
				}
				// if(sfdwxd) {
				// 	List<HbqxDto> hbqxList=hbqxService.getHbxxByYhid(sjxxDto.getYhid());
				// 	if(hbqxList!=null&& hbqxList.size()>0) {
				// 		List<String> strList=new ArrayList<String>();
				// 		for (int i = 0; i < hbqxList.size(); i++){
				// 			strList.add(hbqxList.get(i).getHbmc());
				// 		}
				// 		sjxxDto.setDbs(strList);
				// 	}else {
				// 		List<String> strList=new ArrayList<String>();
				// 		strList.add("matridx-default");
				// 		sjxxDto.setDbs(strList);
				// 	}
				// }
				if (sjxxDto.getZq()==null) {
					sjxxDto.setZq("00");
				}
				if (redisUtil.getExpire("weekLeadStatisDefault")==-2){
					redisUtil.hset("weekLeadStatisDefault","ExpirationTime","1440min",86400);
				}
				String yhid = sjxxDto.getYhid();
				//送检情况统计
				String jsrqStart=sjxxDto.getJsrqstart();
				String jsrqEnd=sjxxDto.getJsrqend();
				sjxxDto.setTj("mon");
				setDateByMonth(sjxxDto,-6);
				sjxxDto.setRqs(sjxxService.getRqsByStartAndEnd(sjxxDto));
				sjxxDto.setJsrqend(jsrqEnd);
				sjxxDto.setJsrqstart(jsrqStart);
				String key;
//				sjxxDto.getZqs().put("ybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
//				List<Map<String, String>> ybqk = new ArrayList<>();
//				yhid + "_ybqk_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
//				Object lsybqk = redisUtil.hget("weekLeadStatis", key);
//				if (lsybqk!=null){
//					ybqk = (List<Map<String, String>>) JSONArray.parse((String) lsybqk);
//				}else {
//					ybqk = SjxxStatisticService.getYbxxByWeeklyAndJsrq(sjxxDto);
//					redisUtil.hset("weekLeadStatis",key, JSON.toJSONString(ybqk));
//				}
//				map.put("ybqk", ybqk);
//				sjxxDto.setJcxmdm("F");
//				sjxxDto.getZqs().put("rfs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
//				List<Map<String, String>> rfs = new ArrayList<>();
//				String rfskey = yhid + "_rfs_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
//				Object lsrfs = redisUtil.hget("weekLeadStatis", rfskey);
//				if (lsrfs!=null){
//					rfs = (List<Map<String, String>>) JSONArray.parse((String) lsrfs);
//				}else {
//					rfs = SjxxStatisticService.getYbxxByWeeklyAndJsrq(sjxxDto);
//					redisUtil.hset("weekLeadStatis",key, JSON.toJSONString(rfs));
//				}
//				map.put("rfs", rfs);
				//测试数信息
				List<Map<String, String>> jcxmDRList;
				key = yhid + "_jcxmnum_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",") +"_"+ StringUtil.join(sjxxDto.getYxxs(),",");
				Object lsjcxmDRList = redisUtil.hget("weekLeadStatisDefault", key);
				if (lsjcxmDRList!=null){
					jcxmDRList = (List<Map<String, String>>) JSONArray.parse((String) lsjcxmDRList);
				}else {
					jcxmDRList = SjxxStatisticService.getYbxxDRByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset("weekLeadStatisDefault",key, JSON.toJSONString(jcxmDRList));
				}
	            sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				sjxxDto.getZqs().put("jcxmnumyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
	            map.put("jcxmnum",jcxmDRList);
	           //收费标本测试数信息
				List<Map<String, String>> ybxxType;
				key = yhid + "_ybxxType_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",") +"_"+ StringUtil.join(sjxxDto.getYxxs(),",");
				Object lsybxxType = redisUtil.hget("weekLeadStatisDefault", key);
				if (lsybxxType!=null){
					ybxxType = (List<Map<String, String>>) JSONArray.parse((String) lsybxxType);
				}else {
					ybxxType = SjxxStatisticService.getYbxxTypeByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset("weekLeadStatisDefault",key, JSON.toJSONString(ybxxType));
				}
	    		sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				sjxxDto.getZqs().put("ybxxTypeyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
	    		map.put("ybxxType", ybxxType);
				//平台趋势
				List<Map<String, String>> qgqsnums;
				key = "jsrq_qgqsnums"+"_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Object jsrq_qgqsnums = redisUtil.hget("weekLeadStatisDefault", key);
				if (jsrq_qgqsnums !=null){
					qgqsnums = (List<Map<String,String>>) JSONArray.parse((String) jsrq_qgqsnums);
				}else {
					qgqsnums = sjxxTwoService.getAllCountryChangesForSale(sjxxDto);
					redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(qgqsnums));
				}
				map.put("qgqs", qgqsnums);
				sjxxDto.getZqs().put("qgqszqs", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				sjxxDto.getZqs().put("qgqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				//产品趋势
				List<Map<String, Object>> cpqstjnums;
				key = "jsrq_cpqstjnums"+"_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Object jsrq_cpqstjnums = redisUtil.hget("weekLeadStatisDefault", key);
				if (jsrq_cpqstjnums !=null){
					cpqstjnums = (List<Map<String,Object>>) JSONArray.parse((String) jsrq_cpqstjnums);
				}else {
					cpqstjnums = sjxxTwoService.getProductionChanges(sjxxDto);
					redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(cpqstjnums));
				}
				map.put("cpqstj", cpqstjnums);
				sjxxDto.getZqs().put("cpqstjzqs", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				sjxxDto.getZqs().put("cpqstjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
	    		//上上周的临床反馈结果
				/*List<Map<String, String>> lcfkList = new ArrayList<>();
				Object lslcfkList = redisUtil.hget("weekLeadStatis", yhid + "_lcfkList");
				if (lslcfkList!=null){
					lcfkList = (List<Map<String, String>>) JSONArray.parse((String) lslcfkList);
				}else {
					lcfkList = SjxxStatisticService.getLcfkOfBeforeWeekly(sjxxDto);
					redisUtil.hset("weekLeadStatis",yhid + "_lcfkList", JSON.toJSONString(lcfkList));
				}
				map.put("lcfkList", lcfkList);*/
	    		// //科室的统计图
	    		// List<Map<String,String>> ksList = new ArrayList<>();
				// key = yhid + "_ksList_" + sjxxDto.getZq();
				// Object lsksList = redisUtil.hget("weekLeadStatis", key);
				// if (lsksList!=null){
				// 	ksList = (List<Map<String, String>>) JSONArray.parse((String) lsksList);
				// }else {
				// 	ksList = SjxxStatisticService.getKsByWeeklyAndJsrq(sjxxDto);
				// 	redisUtil.hset("weekLeadStatis",key, JSON.toJSONString(ksList));
				// }
	    		// sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
	    		// map.put("ksList", ksList);
	    		// //增加合作医疗机构
				// List<Map<String,String>> sjdwOfsjysOfks = new ArrayList<>();
				// key = yhid + "_sjdwOfsjysOfks_" + sjxxDto.getZq();
				// Object lssjdwOfsjysOfks = redisUtil.hget("weekLeadStatis", key);
				// if (lssjdwOfsjysOfks!=null){
				// 	sjdwOfsjysOfks = (List<Map<String, String>>) JSONArray.parse((String) lssjdwOfsjysOfks);
				// }else {
				// 	sjdwOfsjysOfks = SjxxStatisticService.getCooperativeWeeklyByJSrq(sjxxDto);
				// 	redisUtil.hset("weekLeadStatis",key, JSON.toJSONString(sjdwOfsjysOfks));
				// }
	    		// map.put("sjdwOfsjysOfks", sjdwOfsjysOfks);
	    		// //统计合作伙伴
				// List<Map<String,String>> sjhbList = new ArrayList<>();
				// key = yhid + "_sjhbList_" + sjxxDto.getZq();
				// Object lssjhbList = redisUtil.hget("weekLeadStatis", key);
				// if (lssjhbList!=null){
				// 	sjhbList = (List<Map<String, String>>) JSONArray.parse((String) lssjhbList);
				// }else {
				// 	sjhbList = SjxxStatisticService.getSjhbByWeeklyAndJsrq(sjxxDto);
				// 	redisUtil.hset("weekLeadStatis",key, JSON.toJSONString(sjhbList));
				// }
	    		// sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
	    		// map.put("dbtj", sjhbList);
				//Top20第三方
				sjxxDto.setTj("week");
				List<Map<String, String>> topDsf20;
				key = yhid + "jsrq_topDsf20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Object jsrq_topDsf20 = redisUtil.hget("weekLeadStatisDefault", key);
				if (jsrq_topDsf20 !=null){
					topDsf20 = (List<Map<String,String>>) JSONArray.parse((String) jsrq_topDsf20);
				}else {
					topDsf20 = sjxxTwoService.getTopDsf20(sjxxDto);
					redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topDsf20));
				}
				map.put("topDsf20", topDsf20);
				sjxxDto.getZqs().put("topDsf20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				sjxxDto.getZqs().put("topDsf20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				//Top20直销
				List<Map<String, String>> topZx20;
				key = yhid + "jsrq_topZx20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Object jsrq_topZx20 = redisUtil.hget("weekLeadStatisDefault", key);
				if (jsrq_topZx20 !=null){
					topZx20 = (List<Map<String,String>>) JSONArray.parse((String) jsrq_topZx20);
				}else {
					topZx20 = sjxxTwoService.getTopZx20(sjxxDto);
					redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topZx20));
				}
				map.put("topZx20", topZx20);
				sjxxDto.getZqs().put("topZx20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				sjxxDto.getZqs().put("topZx20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				//Top20CSO
				List<Map<String, String>> topCSO20;
				key = yhid + "jsrq_topCSO20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Object jsrq_topCSO20 = redisUtil.hget("weekLeadStatisDefault", key);
				if (jsrq_topCSO20 !=null){
					topCSO20 = (List<Map<String,String>>) JSONArray.parse((String) jsrq_topCSO20);
				}else {
					topCSO20 = sjxxTwoService.getTopCSO20(sjxxDto);
					redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topCSO20));
				}
				map.put("topCSO20", topCSO20);
				sjxxDto.getZqs().put("topCSO20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				sjxxDto.getZqs().put("topCSO20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				//Top20RY
				List<Map<String, String>> topRY20;
				key = yhid +  "jsrq_topRY20"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Object jsrq_topRY20 = redisUtil.hget("weekLeadStatisDefault", key);
				if (jsrq_topRY20 !=null){
					topRY20 = (List<Map<String,String>>) JSONArray.parse((String) jsrq_topRY20);
				}else {
					topRY20 = sjxxTwoService.getTopRY20(sjxxDto);
					redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topRY20));
				}
				map.put("topRY20", topRY20);
				sjxxDto.getZqs().put("topRY20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				sjxxDto.getZqs().put("topRY20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				//平台销售达成率
				XszbDto xszbDto1 = new XszbDto();
				String currentYear1 = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				Calendar calendar = Calendar.getInstance();
				int currenMonth;
				if (monthDay>7){
					currenMonth = calendar.get(Calendar.MONTH)+1;
				}else {
					currenMonth = calendar.get(Calendar.MONTH);
				}
				String monthEnd = String.valueOf((currenMonth+2)/3*3);
				String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
				String start1 = currentYear1 + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
				String end1 = currentYear1 + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
				xszbDto1.setKszq(start1);
				xszbDto1.setJszq(end1);
				xszbDto1.setZblxcsmc("Q");
				yxxList.add("Resfirst");
				xszbDto1.setXms(yxxList);
				xszbDto1.setPtgss(sjxxDto.getPtgss());
				xszbDto1.setYwlx(sjxxDto.getYwlx());
				List<String> ptgss = new ArrayList<>();
				if(StringUtil.isNotBlank(sjxxDto.getYhid())){//当从钉钉消息进入
					ptgss=sjhbxxService.getPtgsByYhid(sjxxDto.getYhid());
					xszbDto1.setPtgss(ptgss.toArray(new String[ptgss.size()]));
				}
				List<Map<String, Object>> ptzbdclnums;
				key =yhid+ "jsrq_ptzbdcl"+"_"+xszbDto1.getKszq() + "~" + xszbDto1.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Object jsrq_ptzbdclnums = redisUtil.hget("weekLeadStatisDefault", key);
				if (jsrq_ptzbdclnums!=null){
					ptzbdclnums = (List<Map<String, Object>>) JSONArray.parse((String) jsrq_ptzbdclnums);
				}else {
					ptzbdclnums = sjxxStatisticService.getPtzbdcl(xszbDto1);
					redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(ptzbdclnums));
				}
				sjxxDto.getZqs().put("ptzbdcl", xszbDto1.getKszq() + "~" + xszbDto1.getJszq());
				sjxxDto.getZqs().put("ptzbdclsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				map.put("ptzbdcl", ptzbdclnums);
				if(StringUtil.isNotBlank(sjxxDto.getYhid()) && CollectionUtils.isEmpty(ptgss))//若销售页面传入yhid时，该人员没有关联伙伴则把数据清空
					map.put("ptzbdcl", "");
				//产品业务占比
				List<Map<String, Object>> cpywzbtjnums;
				xszbDto1.setYwlx(sjxxDto.getYwlx());
				xszbDto1.setYwlx_q(sjxxDto.getYwlx_q());
				xszbDto1.setXms(Arrays.asList(sjxxDto.getYxxs()));
				key = "jsrq_cpywzbtjnums"+"_"+xszbDto1.getKszq() + "~" + xszbDto1.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+StringUtil.join(xszbDto1.getPtgss(),",");
				Object jsrq_cpywzbtjnums = redisUtil.hget("weekLeadStatisDefault", key);
				if (jsrq_cpywzbtjnums !=null){
					cpywzbtjnums = (List<Map<String,Object>>) JSONArray.parse((String) jsrq_cpywzbtjnums);
				}else {
					cpywzbtjnums = sjxxTwoService.getProductionProportion(xszbDto1);
					redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(cpywzbtjnums));
				}
				map.put("cpywzbtj", cpywzbtjnums);
				if(StringUtil.isNotBlank(sjxxDto.getYhid()) && CollectionUtils.isEmpty(ptgss))
					map.put("cpywzbtj", "");
				sjxxDto.getZqs().put("cpywzbtjzqs", xszbDto1.getKszq() + "~" + xszbDto1.getJszq());
				sjxxDto.getZqs().put("cpywzbtjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				//特检销售各区域达成率，初始化按当前季度显示
				XszbDto tjXszb = new XszbDto();
				tjXszb.setZblxcsmc("Q");
				tjXszb.setKszq(xszbDto1.getKszq());
				tjXszb.setJszq(xszbDto1.getJszq());
				tjXszb.setXms(yxxList);
				tjXszb.setYwlx(sjxxDto.getYwlx());
//				Map<String,List<Map<String,String>>> tjxsmap = new HashMap<>();//查平台基础数据
				JcsjDto jcsj_zbfl = new JcsjDto();
				jcsj_zbfl.setJclb(BasicDataTypeEnum.SALE_CLASSIFY.getCode());
				jcsj_zbfl.setCsdm("TJXSTJ");
				jcsj_zbfl = jcsjService.getDto(jcsj_zbfl);
				JcsjDto jcsj_t = new JcsjDto();
				jcsj_t.setFcsid(jcsj_zbfl.getCsid());
				tjXszb.setZbfl(jcsj_zbfl.getCsid());
				List<JcsjDto> tjxs_zfls = jcsjService.getDtoList(jcsj_t);//取特检销售的子分类：区域
				map.put("jcsjXszfls", tjxs_zfls);
				if (tjxs_zfls != null){
					for (JcsjDto jcsj : tjxs_zfls){
						List<Map<String,String>> list;
						tjXszb.setQyid(jcsj.getCsid());
						tjXszb.setCskz3(jcsj.getCskz1());
						tjXszb.setCskz2(jcsj.getCskz2());
						tjXszb.setZbzfl(jcsj.getCsid());
						//由于外部统计数据，需要查找区域下的伙伴去限制外部统计数据范围
						List<String> hbids = xszbService.getDtosByZfl(tjXszb);
						tjXszb.setHbids(hbids);
						key = "jsrq_tjxsdcl"+"_"+jcsj.getCsid()+"_"+tjXszb.getKszq()+ "~" + tjXszb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",");
						Object jsrq_tjxsdcl = redisUtil.hget("weekLeadStatisDefault", key);
						if (jsrq_tjxsdcl !=null){
							list = (List<Map<String,String>>) JSONArray.parse((String) jsrq_tjxsdcl);
						}else {
							list = sjxxStatisticService.getTjsxdcl(tjXszb);
							redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(list));
						}
//						tjxsmap.put(jcsj.getCsid() , list);
						map.put("tjxsdcl_"+jcsj.getCsid(), list);
						sjxxDto.getZqs().put("tjxsdcl_"+jcsj.getCsid(), tjXszb.getKszq()+ "~" + tjXszb.getJszq());
						sjxxDto.getZqs().put("tjxsdclyxxs_"+jcsj.getCsid(), StringUtil.join(sjxxDto.getYxxs(),","));
					}
				}
				//业务准入发展
				sjxxDto.getZqs().put("ywzrfz", xszbDto1.getKszq() + "~" + xszbDto1.getJszq());
				sjxxDto.getZqs().put("ywzrfzsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				List<Map<String, Object>> ywzrfznums;
				key = "jsrq_ywzrfz"+"_"+xszbDto1.getKszq() + "~" + xszbDto1.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Object jsrq_ywzrfznums = redisUtil.hget("weekLeadStatisDefault", key);
				if (jsrq_ywzrfznums!=null){
					ywzrfznums = (List<Map<String, Object>>) JSONArray.parse((String) jsrq_ywzrfznums);
				}else {
					ywzrfznums = sjxxStatisticService.getYwfzbZbdcl(xszbDto1);
					redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(ywzrfznums));
				}
				map.put("ywzrfz", ywzrfznums);
				sjxxDto.getZqs().put("ywzrfz", xszbDto1.getKszq() + "~" + xszbDto1.getJszq());
				sjxxDto.getZqs().put("ywzrfzsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));

				//2.5、TOP10核心医院排行
				List<Map<String, Object>> topHxyxList;
				key = yhid + "jsrq_topHxyxList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Object jsrq_topHxyxList = redisUtil.hget("weekLeadStatisDefault", key);
				if (jsrq_topHxyxList !=null){
					topHxyxList = (List<Map<String,Object>>) JSONArray.parse((String) jsrq_topHxyxList);
				}else {
					topHxyxList = sjxxTwoService.getHxyyTopList(sjxxDto);
					redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(topHxyxList));
				}
				map.put("topHxyxList", topHxyxList);
				sjxxDto.getZqs().put("topHxyxList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				sjxxDto.getZqs().put("topHxyxListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				//2.4、收费科室分布（按照收费测试数统计）
				List<Map<String, Object>> ksSfcssList;
				key = yhid + "jsrq_ksSfcssList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Object jsrq_ksSfcssList = redisUtil.hget("weekLeadStatisDefault", key);
				if (jsrq_ksSfcssList !=null){
					ksSfcssList = (List<Map<String,Object>>) JSONArray.parse((String) jsrq_ksSfcssList);
				}else {
					ksSfcssList = sjxxTwoService.getChargesDivideByKs(sjxxDto);
					redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(ksSfcssList));
				}
				map.put("ksSfcssList", ksSfcssList);
				sjxxDto.getZqs().put("ksSfcssList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				sjxxDto.getZqs().put("ksSfcssListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				//2.5、收费标本类型分布（按照收费测试数统计）
				List<Map<String, Object>> yblxSfcssList;
				key = yhid + "jsrq_yblxSfcssList"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Object jsrq_yblxSfcssList = redisUtil.hget("weekLeadStatisDefault", key);
				if (jsrq_yblxSfcssList !=null){
					yblxSfcssList = (List<Map<String,Object>>) JSONArray.parse((String) jsrq_yblxSfcssList);
				}else {
					yblxSfcssList = sjxxTwoService.getChargesDivideByYblx(sjxxDto);
					redisUtil.hset("weekLeadStatisDefault",key,JSON.toJSONString(yblxSfcssList));
				}
				map.put("yblxSfcssList", yblxSfcssList);
				sjxxDto.getZqs().put("yblxSfcssList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				sjxxDto.getZqs().put("yblxSfcssListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
	    		//统计复检申请
				/*FjsqDto fjsqDto=new FjsqDto();
				// fjsqDto.setSjhbs(sjxxDto.getDbs());
				fjsqDto.setYhid(sjxxDto.getYhid());
				fjsqDto.setPtgss(sjxxDto.getPtgss());
				fjsqDto.setLrsjstart(sjxxDto.getJsrqstart());
				fjsqDto.setLrsjend(sjxxDto.getJsrqend());
				List<Map<String,String>> fjsqList = new ArrayList<>();
				key = yhid + "_fjsqList_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Object lsfjsqList = redisUtil.hget("weekLeadStatis", key);
				if (lsfjsqList!=null){
					fjsqList = (List<Map<String, String>>) JSONArray.parse((String) lsfjsqList);
				}else {
					fjsqList = SjxxStatisticService.getFjsqDaily(fjsqDto);
					redisUtil.hset("weekLeadStatis",key, JSON.toJSONString(fjsqList));
				}
				sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				map.put("fjsq", fjsqList);*/
				//统计废弃标本
				/*List<Map<String,String>> fqybMap = new ArrayList<>();
				Object lsfqybMap = redisUtil.hget("weekLeadStatis", yhid + "_fqybMap");
				if (lsfqybMap!=null){
					fqybMap = (List<Map<String, String>>) JSONArray.parse((String) lsfqybMap);
				}else {
					fqybMap = SjxxStatisticService.getFqybByYbztDaily(sjxxDto);
					redisUtil.hset("weekLeadStatis",yhid + "_fqybMap", JSON.toJSONString(fqybMap));
				}
				sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				map.put("fqyb", fqybMap);*/
				//标本日期统计
				/*List<Map<String,String>> rqList = new ArrayList<>();
				Object lsrqList = redisUtil.hget("weekLeadStatis", yhid + "_rqList");
				if (lsrqList!=null){
					rqList = (List<Map<String, String>>) JSONArray.parse((String) lsrqList);
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
				}else {
					rqList=SjxxStatisticService.getDataOfNumByJsrq(sjxxDto);
					redisUtil.hset("weekLeadStatis",yhid + "_rqList", JSON.toJSONString(fqybMap));
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
				}*/
				//微信按照周统计标本个数（周）
				/*List<Map<String,String>> ybsList = new ArrayList<>();
				Object lsybsList = redisUtil.hget("weekLeadStatis", yhid + "_weektj");
				if (lsybsList!=null){
					ybsList = (List<Map<String, String>>) JSONArray.parse((String) lsybsList);
				}else {
					ybsList=SjxxStatisticService.getYbxxnumByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset("weekLeadStatis",yhid + "_weektj", JSON.toJSONString(ybsList));
				}
				sjxxDto.getZqs().put("weektj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				map.put("weektj", ybsList);*/
				XxdyDto xxdyDto=new XxdyDto();
				xxdyDto.setCskz1("JCSJ");
				xxdyDto.setCskz2(BasicDataTypeEnum.CLASSIFY.getCode());
				List<XxdyDto> xxdyDtos = xxdyService.getListGroupByYxx(xxdyDto);
				//伙伴分类测试数占比
				sjxxDto.getZqs().put("hbflcsszbzqs", start1 + "~" + end1);
				sjxxDto.getZqs().put("hbflcsszbyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				List<Map<String, String>> hbflList=new ArrayList<>();
				if(xxdyDtos!=null&&xxdyDtos.size()>0){
					SjxxDto sjxxDto_t=new SjxxDto();
					sjxxDto_t.setJsrqstart(start1);
					sjxxDto_t.setJsrqend(end1);
					sjxxDto_t.setYwlx(sjxxDto.getYwlx());
					sjxxDto_t.setYwlx_q(sjxxDto.getYwlx_q());
					sjxxDto_t.setYxxs(sjxxDto.getYxxs());
					sjxxDto_t.setPtgss(sjxxDto.getPtgss());
					sjxxDto_t.setYhid(sjxxDto.getYhid());

					List<Map<String, String>> hbflcsszbMap;
					key = "jsrq_getHbflcsszb_"+start1 + "~" + end1+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
					Object hbflcssRedis = redisUtil.hget("weekLeadStatisDefault", key);
					if (hbflcssRedis!=null){
						hbflcsszbMap = (List<Map<String, String>>) JSONArray.parse((String) hbflcssRedis);
						hbflList = hbflcsszbMap;
					}else {
						hbflcsszbMap = sjxxStatisticService.getHbflcsszb(sjxxDto_t);
						for(XxdyDto xxdyDto_t:xxdyDtos){
							boolean isFind=false;
							for(Map<String, String> map_t:hbflcsszbMap){
								if(xxdyDto_t.getYxx().equals(map_t.get("yxx"))){
									hbflList.add(map_t);
									isFind=true;
									break;
								}
							}
							if(!isFind){
								Map<String, String> newMap=new HashMap<>();
								newMap.put("num","0");
								newMap.put("yxx",xxdyDto_t.getYxx());
								hbflList.add(newMap);
							}
						}
						redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(hbflList));
					}
				}
				map.put("hbflcsszb", hbflList);

				//送检区分测试数占比
				List<JcsjDto> sjqfs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
				sjxxDto.getZqs().put("sjqfcsszbzqs", start1 + "~" + end1);
				sjxxDto.getZqs().put("sjqfcsszbyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				List<Map<String, String>> sjqfList=new ArrayList<>();
				key = "jsrq_getSjqfcsszb_"+start1 + "~" + end1+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				Object sjqfcssRedis = redisUtil.hget("weekLeadStatisDefault", key);
				if (sjqfcssRedis!=null){
					sjqfList = (List<Map<String, String>>) JSONArray.parse((String) sjqfcssRedis);
				}else {
					JcsjDto jcsjDto1 = new JcsjDto();
					jcsjDto1.setCsid("第三方实验室");
					jcsjDto1.setCsmc("第三方实验室");
					sjqfs.add(jcsjDto1);
					JcsjDto jcsjDto2 = new JcsjDto();
					jcsjDto2.setCsid("直销");
					jcsjDto2.setCsmc("直销");
					sjqfs.add(jcsjDto2);
					JcsjDto jcsjDto3 = new JcsjDto();
					jcsjDto3.setCsid("CSO");
					jcsjDto3.setCsmc("CSO");
					sjqfs.add(jcsjDto3);
					if(sjqfs!=null&&sjqfs.size()>0){
						SjxxDto sjxxDto_t=new SjxxDto();
						sjxxDto_t.setJsrqstart(start1);
						sjxxDto_t.setJsrqend(end1);
						sjxxDto_t.setYwlx(sjxxDto.getYwlx());
						sjxxDto_t.setYxxs(sjxxDto.getYxxs());
						sjxxDto_t.setNewflg("zb");
						List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto_t);
						for(JcsjDto jcsjDto:sjqfs){
//							boolean isFind=false;
							for(Map<String, String> map_t:sjqfcsszb){
								if(jcsjDto.getCsid().equals(map_t.get("sjqf"))){
									map_t.put("sjqfmc",jcsjDto.getCsmc());
									sjqfList.add(map_t);
//									isFind=true;
									break;
								}
							}
							// if(!isFind){
							// 	Map<String, String> newMap=new HashMap<>();
							// 	newMap.put("num","0");
							// 	newMap.put("sjqfmc",jcsjDto.getCsmc());
							// 	sjqfList.add(newMap);
							// }
						}
					}
					redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(sjqfList));
				}
				map.put("sjqfcsszb", sjqfList);
				//销售性质测试数趋势
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("xsxzcssqszqs", sjxxDto.getRqs().get(0) + "~" + sjxxDto.getRqs().get(sjxxDto.getRqs().size()-1));
				sjxxDto.getZqs().put("xsxzcssqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				List<Map<String, String>> xsxzcssqsList=new ArrayList<>();
				key = "jsrq_getXsxzcssqszqs_"+sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				Object xsxzcssqszqsRedis = redisUtil.hget("weekLeadStatisDefault", key);
				if (xsxzcssqszqsRedis!=null){
					xsxzcssqsList = (List<Map<String, String>>) JSONArray.parse((String) xsxzcssqszqsRedis);
				}else {
					// JcsjDto jcsjDto1 = new JcsjDto();
					// jcsjDto1.setCsid("第三方实验室");
					// jcsjDto1.setCsmc("第三方实验室");
					// sjqfs.add(jcsjDto1);
					// JcsjDto jcsjDto2 = new JcsjDto();
					// jcsjDto2.setCsid("直销");
					// jcsjDto2.setCsmc("直销");
					// sjqfs.add(jcsjDto2);
					// JcsjDto jcsjDto3 = new JcsjDto();
					// jcsjDto3.setCsid("CSO");
					// jcsjDto3.setCsmc("CSO");
					// sjqfs.add(jcsjDto3);
					if(sjqfs!=null&&sjqfs.size()>0){
						sjxxDto.setNewflg("qs");
						List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
						List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto);
						for(int i=0;i<rqs.size();i++){
							for(JcsjDto jcsjDto:sjqfs){
								boolean isFind=false;
								for(Map<String, String> map_t:sjqfcsszb){
									if(rqs.get(i).equals(map_t.get("rq"))&&jcsjDto.getCsid().equals(map_t.get("sjqf"))){
										map_t.put("sjqfmc",jcsjDto.getCsmc());
										if(i==0){
											map_t.put("bl","0");
										}else{
											var num="0";
											for(Map<String, String> stringMap:xsxzcssqsList){
												if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
													num=stringMap.get("num");
												}
											}
											BigDecimal bigDecimal=new BigDecimal(num);
											BigDecimal bigDecimal1=new BigDecimal(map_t.get("num"));
											map_t.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
										}
										xsxzcssqsList.add(map_t);
										isFind=true;
										break;
									}
								}
								if(!isFind){
									Map<String, String> newMap=new HashMap<>();
									newMap.put("rq",rqs.get(i));
									newMap.put("num","0");
									newMap.put("sjqfmc",jcsjDto.getCsmc());
									newMap.put("sjqf",jcsjDto.getCsid());
									if(i==0){
										newMap.put("bl","0");
									}else{
										var num="0";
										for(Map<String, String> stringMap:xsxzcssqsList){
											if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
												num=stringMap.get("num");
											}
										}
										BigDecimal bigDecimal=new BigDecimal(num);
										BigDecimal bigDecimal1=new BigDecimal("0");
										newMap.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
									}
									xsxzcssqsList.add(newMap);
								}
							}
						}
					}
					redisUtil.hset("weekLeadStatisDefault", key,JSON.toJSONString(xsxzcssqsList));
				}
				map.put("xsxzcssqs", xsxzcssqsList);
				//按照合作伙伴类别
				 String zqString = "0";
				 if(StringUtil.isNotBlank(sjxxDto.getZq())) {
				 	zqString = sjxxDto.getZq().split(",")[0];
				 }
				 sjxxDto.setTj("day");
				 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				 sjxxDto.setJsrqstart(dateFormat.format(DateUtils.getDate(DateUtils.parseDate("yyyy-MM-dd",sjxxDto.getJsrqend()), - Integer.parseInt(zqString)+1)));
				 List<Map<String, String>> fllists=SjxxStatisticService.getHbflTestCountStatistics(sjxxDto);
				 if(fllists!=null) {
					 if(xxdyDtos!=null&&xxdyDtos.size()>0){
//						 Map<String, List<Map<String, String>>> flmap=null;
						 List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
						 for(XxdyDto xxdyDto_t:xxdyDtos){
							 List<Map<String, String>> newList=new ArrayList<>();
							 for(String time:rqs){
								 Map<String, String> newMap_one=new HashMap<>();
								 newMap_one.put("rq",time);
								 newMap_one.put("sfsf","1");
								 newMap_one.put("yxx",xxdyDto_t.getYxx());
								 newMap_one.put("num","0");
								 for(Map<String, String> map_t:fllists){
									 if(xxdyDto_t.getYxx().equals(map_t.get("yxx"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
										 newMap_one.put("num",map_t.get("num"));
										 break;
									 }
								 }
								 newList.add(newMap_one);
								 Map<String, String> newMap_two=new HashMap<>();
								 newMap_two.put("rq",time);
								 newMap_two.put("sfsf","0");
								 newMap_two.put("yxx",xxdyDto_t.getYxx());
								 newMap_two.put("num","0");
								 for(Map<String, String> map_t:fllists){
									 if(xxdyDto_t.getYxx().equals(map_t.get("yxx"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
										 newMap_two.put("num",map_t.get("num"));
										 break;
									 }
								 }
								 newList.add(newMap_two);
							 }
							 map.put("fltj_"+xxdyDto_t.getYxx(),newList);
							 sjxxDto.getZqs().put("fltj_"+xxdyDto_t.getYxx()+"yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
						 }
					 }
				 }
				//销售达成率统计,按季度
				// XszbDto xszbDto = new XszbDto();
				// String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				// String start = currentYear;
				// String end = currentYear;
				// xszbDto.setKszq(start);
				// xszbDto.setJszq(end);
				// xszbDto.setYhid(sjxxDto.getYhid());
				// xszbDto.setZblxcsmc("Q");
				// Map<String,Object> salesAttainmentRate = new HashMap<>();
				// key = yhid + "_salesAttainmentRate_" + sjxxDto.getZq();
				// Object lssalesAttainmentRate = redisUtil.hget("weekLeadStatis", key);
				// if (lssalesAttainmentRate!=null){
				// 	salesAttainmentRate = (Map<String,Object>) JSONArray.parse((String) lssalesAttainmentRate);
				// }else {
				// 	salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRateByJsrq(xszbDto);
				// 	redisUtil.hset("weekLeadStatis",key, JSON.toJSONString(salesAttainmentRate));
				// }
				// if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
				// 	for (String map1 : salesAttainmentRate.keySet()) {
				// 		map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
				// 	}
				// }
				map.put("searchData", sjxxDto);
			}else {
				logger.error("getweeklymodelByJsrq--不允许用户id和平台归属都为空！！");
				return null;
			}
		}
		//若过期时间为-1，重新设置过期时间
		if (redisUtil.getExpire("weekLeadStatisDefault")==-1){
			redisUtil.hset("weekLeadStatisDefault","ExpirationTime","1440min",86400);
		}
		return map;
	}

	/**
	 * 设置周的日期
	 * @param sjxxDto
	 * length 长度。要为负数，代表向前推移几天
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
					sjxxDto.setJsrqstart(daySdf.format(DateUtils.getDate(jsDate, -dayOfWeek-1 + 7*(length+1))));
					sjxxDto.setJsrqend(daySdf.format(DateUtils.getDate(jsDate, 5 - dayOfWeek)));
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

	/**
	 * 通过条件查询刷新页面
	 * 
	 * @return
	 */
	@RequestMapping("/statictis/getweeklymodel_condition")
	@ResponseBody
	public Map<String, Object> getSjxxStatisByTj(HttpServletRequest req,SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		String method = req.getParameter("method");
		//查询到当前用户下边的合作伙伴
		if(StringUtil.isNotBlank(sjxxDto.getYhid())) {
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
				}
			}
				//判断method不能为空，为空即返回空
			if(StringUtil.isBlank(method)) {
				return map;
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
				
				List<Map<String, String>> fllist;
				if(sfdwxd) {
					 fllist=SjxxStatisticService.getWeeklyByTjFl(sjxxDto);
				}else {
					fllist=SjxxStatisticService.getWeeklyByTjFlTm(sjxxDto);
				}				Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
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
				List<Map<String, String>> fllist;
				if(sfdwxd) {
					 fllist=SjxxStatisticService.getWeeklyByTjFl(sjxxDto);
				}else {
					fllist=SjxxStatisticService.getWeeklyByTjFlTm(sjxxDto);
				}				Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
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
				if(sjxxDto.getDbmcs()!=null&&sjxxDto.getDbmcs().size()>0) {
					sjxxDto.setDbs(sjxxDto.getDbmcs());
				}
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
				List<Map<String, String>> fllist;
				if(sfdwxd) {
					 fllist=SjxxStatisticService.getWeeklyByTjFl(sjxxDto);
				}else {
					fllist=SjxxStatisticService.getWeeklyByTjFlTm(sjxxDto);
				}				Map<String, List<Map<String, String>>> fllists = sjxxService.setReceiveId(fllist, "db");
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
				List<Map<String, String>> fllist;
				if(sfdwxd) {
					 fllist=SjxxStatisticService.getWeeklyByTjFl(sjxxDto);
				}else {
					fllist=SjxxStatisticService.getWeeklyByTjFlTm(sjxxDto);
				}
				map.put("hbztj",fllist);
			}else if("getAllhbByMon".equals(method)) {
				//合作伙伴总数按月
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
				sjxxDto.setTj("mon");
				List<Map<String, String>> fllist;
				if(sfdwxd) {
					 fllist=SjxxStatisticService.getWeeklyByTjFl(sjxxDto);
				}else {
					fllist=SjxxStatisticService.getWeeklyByTjFlTm(sjxxDto);
				}
				map.put("hbztj",fllist);
			}else if("getAllhbByWeek".equals(method)) {
				//合作伙伴总数按周
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
				sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				sjxxDto.setTj("day");
				List<Map<String, String>> fllist;
				if(sfdwxd) {
					 fllist=SjxxStatisticService.getWeeklyByTjFl(sjxxDto);
				}else {
					fllist=SjxxStatisticService.getWeeklyByTjFlTm(sjxxDto);
				}				map.put("hbztj",fllist);
			}else if("getAllhbByDay".equals(method)) {
				//合作伙伴总数按日
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				sjxxDto.setTj("day");
				List<Map<String, String>> fllist;
				if(sfdwxd) {
					 fllist=SjxxStatisticService.getWeeklyByTjFl(sjxxDto);
				}else {
					fllist=SjxxStatisticService.getWeeklyByTjFlTm(sjxxDto);
				}				map.put("hbztj",fllist);
			}else if("getSjxxYearBySyAndSf".equals(method)) {
				//省份测试数按年查询
				setDateByYear(sjxxDto,-6,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String,String>> sfybqk=SjxxStatisticService.getYbxxByWeekly(sjxxDto);
				map.put("sfybqk", sfybqk);
			}else if("getSjxxMonBySyAndSf".equals(method)) {
				//省份测试数按月查询
				setDateByMonth(sjxxDto,-6);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String, String>> sfybqk = SjxxStatisticService.getYbxxByWeekly(sjxxDto);
				map.put("sfybqk", sfybqk);
			}else if("getSjxxWeekBySyAndSf".equals(method)) {
				//省份测试数按周查询
				setDateByWeek(sjxxDto); 
				sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> sfybqk=SjxxStatisticService.getybxx_weekByWeekly(sjxxDto);
				map.put("sfybqk", sfybqk);
			}else if("getSjxxDayBySyAndSf".equals(method)) {
				//省份测试数按日查询
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> sfybqk=SjxxStatisticService.getYbxxByWeekly(sjxxDto);
				map.put("sfybqk", sfybqk);
			}else if("getSjxxDRByYearAndSf".equals(method)) {
				//省份测试数按年查询
				setDateByYear(sjxxDto,-6,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String,String>> sfcss=SjxxStatisticService.getYbxxDRByWeekly(sjxxDto);
				map.put("sfcss", sfcss);
			}else if("getSjxxDRByMonAndSf".equals(method)) {
				//省份测试数按月查询
				setDateByMonth(sjxxDto,-6);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String, String>> sfcss = SjxxStatisticService.getYbxxDRByWeekly(sjxxDto);
				map.put("sfcss", sfcss);
			}else if("getSjxxDRByWeekAndSf".equals(method)) {
				//检测单位测试数按周查询
				sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> sfybqk=SjxxStatisticService.getYbxxDR_weeek(sjxxDto);
				map.put("sfcss", sfybqk);
			}else if("getSjxxDRByDayAndSf".equals(method)) {
				//检测单位测试数按日查询
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> sfcss=SjxxStatisticService.getYbxxDRByWeekly(sjxxDto);
				map.put("sfcss", sfcss);
			}else if("getYbxxTypeByYearAndSf".equals(method)) {
				//省份收费标本下边检测项目的总条数 年
				setDateByYear(sjxxDto,-6,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String,String>> sfsfcss=SjxxStatisticService.getYbxxTypeByWeekly(sjxxDto);
				map.put("sfsfcss", sfsfcss);
			}else if("getYbxxTypeByMonAndSf".equals(method)) {
				//省份收费标本下边检测项目的总条数 月
				setDateByMonth(sjxxDto,-6);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String,String>> sfsfcss=SjxxStatisticService.getYbxxTypeByWeekly(sjxxDto);
				map.put("sfsfcss", sfsfcss);
			}else if("getYbxxTypeByWeekAndSf".equals(method)) {
				//省份收费标本下边检测项目的总条数 周
				sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String,String>> sfsfcss=SjxxStatisticService.getYbxxType_week(sjxxDto);
				map.put("sfsfcss", sfsfcss);
			}else if("getYbxxTypeByDayAndSf".equals(method)) {
				//省份收费标本下边检测项目的总条数 日
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String,String>> sfsfcss=SjxxStatisticService.getYbxxTypeByWeekly(sjxxDto);
				map.put("sfsfcss", sfsfcss);
			}else if ("getSalesAttainmentRateByYear".equals(method)) {
				//销售达成率按年
				String qyid = req.getParameter("qyid");
				XszbDto xszbDto = new XszbDto();
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.YEAR,-4);
				String start = DateUtils.format(calendar.getTime(), "yyyy");
				String end = DateUtils.format(new Date(), "yyyy");
				xszbDto.setKszq(start);
				xszbDto.setJszq(end);
				xszbDto.setYhid(sjxxDto.getYhid());
				xszbDto.setZblxcsmc("Y");
				xszbDto.setQyid(qyid);
				Map<String, Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
				if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
					for (String map1 : salesAttainmentRate.keySet()) {
						map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
					}
				}
				sjxxDto.getZqs().put("salesAttainmentRate", start + "~" + end);
				map.put("salesAttainmentRate", salesAttainmentRate);
			} else if ("getSalesAttainmentRateByQuarter".equals(method)) {
				//销售达成率按季度,全年季度
				String qyid = req.getParameter("qyid");
				XszbDto xszbDto = new XszbDto();
				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                xszbDto.setKszq(currentYear);
				xszbDto.setJszq(currentYear);
				xszbDto.setYhid(sjxxDto.getYhid());
				xszbDto.setZblxcsmc("Q");
				xszbDto.setQyid(qyid);
				Map<String, Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
				if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
					for (String map1 : salesAttainmentRate.keySet()) {
						map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
					}
				}
				sjxxDto.getZqs().put("salesAttainmentRate", currentYear + "~" + currentYear);
				map.put("salesAttainmentRate", salesAttainmentRate);
			} else if ("getSalesAttainmentRateByMon".equals(method)) {
				String qyid = req.getParameter("qyid");
				//销售达成率 默认6个月
				XszbDto xszbDto = new XszbDto();
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.MONTH,-4);
				String start = DateUtils.format(calendar.getTime(), "yyyy-MM");
				String end = DateUtils.format(new Date(), "yyyy-MM");
				xszbDto.setKszq(start);
				xszbDto.setJszq(end);
				xszbDto.setYhid(sjxxDto.getYhid());
				xszbDto.setZblxcsmc("M");
				xszbDto.setQyid(qyid);
				Map<String, Object> salesAttainmentRate = sjxxStatisticService.getSalesAttainmentRate(xszbDto);
				if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
					for (String map1 : salesAttainmentRate.keySet()) {
						map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
					}
				}
				sjxxDto.getZqs().put("salesAttainmentRate", start + "~" + end);
				map.put("salesAttainmentRate", salesAttainmentRate);
			}
			map.put("searchData", sjxxDto);
		}
		return map;
	}
	/**
	 * 通过条件查询刷新页面(接收日期)(待完成)
	 *
	 * @return
	 */
	@RequestMapping("/statictis/getweeklymodel_conditionByJsrq")
	@ResponseBody
	public Map<String, Object> getSjxxStatisByTjAndJsrq(HttpServletRequest req,SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		Calendar calendar_t = Calendar.getInstance();
		//时间，可以为具体的某一时间
		Date nowDate = new Date();
		calendar_t.setTime(nowDate);
		int monthDay = calendar_t.get(Calendar.DAY_OF_MONTH);
		String method = req.getParameter("method");
		sjxxDto.setTjtj("NOW");
		List<JcsjDto> dyxxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.XXDY_TYPE.getCode());
		for (JcsjDto dyxx : dyxxs) {
			if ("XMFL".equals(dyxx.getCsdm())){
				//项目分类
				sjxxDto.setYwlx(dyxx.getCsid());
			}
			if ("HBFL".equals(dyxx.getCsdm())){
				//伙伴分类
				sjxxDto.setYwlx_q(dyxx.getCsid());
			}
		}
		List<String> yxxList = new ArrayList<>();
		if (ArrayUtils.isEmpty(sjxxDto.getYxxs())){
			XxdyDto xxdyDto = new XxdyDto();
			xxdyDto.setKzcs6("1");
			xxdyDto.setDylxcsdm("XMFL");
			List<XxdyDto> dtoList = xxdyService.getYxxMsg(xxdyDto);
			if (!CollectionUtils.isEmpty(dtoList)){
				List<String> yxxs = new ArrayList<>();
				for (XxdyDto dto : dtoList) {
					yxxs.add(dto.getYxx());
				}
				yxxList = yxxs;
				yxxList.add("Resfirst");
				sjxxDto.setYxxs(yxxs.toArray(new String[0]));
			}
		}else{
			String[] yxxStrs = sjxxDto.getYxxs();
			yxxList.addAll(Arrays.asList(yxxStrs));
			yxxList.add("Resfirst");
		}
		//查询到当前用户下边的合作伙伴
		if(StringUtil.isNotBlank(sjxxDto.getYhid())||ArrayUtils.isNotEmpty(sjxxDto.getPtgss())) {
			boolean sfdwxd =ddyhService.getDwxd(sjxxDto.getYhid());
			if (!sfdwxd){
				sjxxDto.setYhid(null);
			}
			// if(sfdwxd) {
			// 	sjxxDto.setDwxzbj("1");
			// 	List<HbqxDto> hbqxList=hbqxService.getHbxxByYhid(sjxxDto.getYhid());
			// 	if(hbqxList!=null&& hbqxList.size()>0) {
			// 		List<String> strList=new ArrayList<String>();
			// 		for (int i = 0; i < hbqxList.size(); i++){
			// 			strList.add(hbqxList.get(i).getHbmc());
			// 		}
			// 		sjxxDto.setDbs(strList);
			// 	}
			// }
			if (sjxxDto.getZq()==null) {
				sjxxDto.setZq("00");
			}
			if (redisUtil.getExpire("weekLeadStatis")==-2){
				redisUtil.hset("weekLeadStatis","ExpirationTime","30min",1800);
			}
			String yhid = sjxxDto.getYhid();
				//判断method不能为空，为空即返回空
			if(StringUtil.isBlank(method)) {
				return map;
			}else if(method.startsWith("getRfsWeekly")) {
				sjxxDto.setJcxmdm("F");
				List<Map<String, String>> rfslist;
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				String key = null;
//				Object getRfsSjxx = null;
				if (method.contains("year")){
					//标本情况按年
					setDateByYear(sjxxDto,zq+1,false);
					sjxxDto.setTj("year");
					sjxxDto.getZqs().put("rfs", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
					key = yhid + "_getRfsWeekly_year_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				}else if (method.contains("mon")){
					setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
					sjxxDto.setTj("mon");
					sjxxDto.getZqs().put("rfs", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
					key = yhid + "_getRfsWeekly_mon_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				}else if (method.contains("week")){
					setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),zq+1);
					sjxxDto.setTj("week");
					sjxxDto.getZqs().put("rfs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					key = yhid + "_getRfsWeekly_week_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				}else if (method.contains("day")){
					setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
					sjxxDto.setTj("day");
					sjxxDto.getZqs().put("rfs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					key = yhid + "_getRfsWeekly_day_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				}
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					rfslist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					if (method.contains("week")){
						rfslist = SjxxStatisticService.getybxx_weekByWeeklyAndJsrq(sjxxDto);
					}else {
						rfslist = SjxxStatisticService.getYbxxByWeeklyAndJsrq(sjxxDto);
					}
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(rfslist));
				}
				map.put("rfs",rfslist);

			}
//			else if("getTjxsdclByYear".equals(method)) {
//				//统计销售达成率按年查询
//				XszbDto xszb_yzb = new XszbDto();
//				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
//					xszb_yzb.setKszq(sjxxDto.getLrsjStart());
//					xszb_yzb.setJszq(sjxxDto.getLrsjEnd());
//				} else {
//					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
//					String start1 = currentYear+"-01";
//					String end1 = currentYear+"-12";
//					xszb_yzb.setKszq(start1);
//					xszb_yzb.setJszq(end1);
//				}
//				xszb_yzb.setZblxcsmc("Y");
//				xszb_yzb.setXms(yxxList);
//				xszb_yzb.setQyid(sjxxDto.getZfl());
//				xszb_yzb.setYhid(sjxxDto.getYhid());
//				xszb_yzb.setPtgss(sjxxDto.getPtgss());
//				List<String> ptgss = new ArrayList<>();
//				if(StringUtil.isNotBlank(sjxxDto.getYhid())){//当从钉钉消息进入
//					ptgss=sjhbxxService.getPtgsByYhid(sjxxDto.getYhid());
//					xszb_yzb.setPtgss(ptgss.toArray(new String[ptgss.size()]));
//				}
//				sjxxDto.getZqs().put("tjxsdcl_"+sjxxDto.getZfl(), xszb_yzb.getKszq()+ "~" + xszb_yzb.getJszq());
//				sjxxDto.getZqs().put("tjxsdclyxxs_"+sjxxDto.getZfl(), StringUtil.join(sjxxDto.getYxxs(),","));
//				List<Map<String, String>> tjxsdclByYearlist = new ArrayList<>();
//				String key = "jsrq_tjxsdcl_"+sjxxDto.getZfl()+"_"+ xszb_yzb.getKszq()+ "~" + xszb_yzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
//				Object getXstjdclByYear = redisUtil.hget("weekLeadStatis", key);
//				if (getXstjdclByYear!=null){
//					tjxsdclByYearlist = (List<Map<String, String>>) JSONArray.parse((String) getXstjdclByYear);
//				}else {
//					tjxsdclByYearlist = sjxxStatisticService.getTjsxdcl(xszb_yzb);
//					redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(tjxsdclByYearlist));
//				}
//				map.put("tjxsdcl_"+sjxxDto.getZfl(), tjxsdclByYearlist);
//			}
			else if("getTjxsdclByQuarter".equals(method)) {
				//统计销售达成率按季度查询
				XszbDto xszb_qzb = new XszbDto();
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					xszb_qzb.setKszq(sjxxDto.getLrsjStart());
					xszb_qzb.setJszq(sjxxDto.getLrsjEnd());
				} else {
					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					Calendar calendar = Calendar.getInstance();
					calendar.get(Calendar.DAY_OF_MONTH);
					int currenMonth;
					if (monthDay>7){
						currenMonth = calendar.get(Calendar.MONTH)+1;
					}else {
						currenMonth = calendar.get(Calendar.MONTH);
					}
					String monthEnd = String.valueOf((currenMonth+2)/3*3);
					String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
					String start1 = currentYear + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
					String end1 = currentYear + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
					xszb_qzb.setKszq(start1);
					xszb_qzb.setJszq(end1);
				}
				xszb_qzb.setZblxcsmc("Q");
				xszb_qzb.setXms(yxxList);
				xszb_qzb.setQyid(sjxxDto.getZfl());
				xszb_qzb.setYhid(sjxxDto.getYhid());
				xszb_qzb.setPtgss(sjxxDto.getPtgss());
				xszb_qzb.setYwlx(sjxxDto.getYwlx());
				xszb_qzb.setZbzfl(sjxxDto.getZfl());
				xszb_qzb.setCskz3(redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.SALE_SUBCLASSIFY.getCode(), sjxxDto.getZfl()).getCskz1());
				xszb_qzb.setCskz2(redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.SALE_SUBCLASSIFY.getCode(), sjxxDto.getZfl()).getCskz2());
				//由于外部统计数据，需要查找区域下的伙伴去限制外部统计数据范围
				List<String> hbids = xszbService.getDtosByZfl(xszb_qzb);
				xszb_qzb.setHbids(hbids);
				List<String> ptgss;
				if(StringUtil.isNotBlank(sjxxDto.getYhid())){//当从钉钉消息进入
					ptgss=sjhbxxService.getPtgsByYhid(sjxxDto.getYhid());
					xszb_qzb.setPtgss(ptgss.toArray(new String[ptgss.size()]));
				}
				sjxxDto.getZqs().put("tjxsdcl_"+sjxxDto.getZfl(), xszb_qzb.getKszq()+ "~" + xszb_qzb.getJszq());
				sjxxDto.getZqs().put("tjxsdclyxxs_"+sjxxDto.getZfl(), StringUtil.join(sjxxDto.getYxxs(),","));
				List<Map<String, String>> tjxsdclByQuarterlist;
				String key = "jsrq_tjxsdcl_"+sjxxDto.getZfl()+"_"+ xszb_qzb.getKszq()+ "~" + xszb_qzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					tjxsdclByQuarterlist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					tjxsdclByQuarterlist = sjxxStatisticService.getTjsxdcl(xszb_qzb);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(tjxsdclByQuarterlist));
				}
				map.put("tjxsdcl_"+sjxxDto.getZfl(), tjxsdclByQuarterlist);
			}
//			else if("getTjxsdclByMon".equals(method)) {
//				//统计销售达成率按月查询
//				XszbDto xszb_mzb = new XszbDto();
//				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
//					xszb_mzb.setKszq(sjxxDto.getLrsjStart());
//					xszb_mzb.setJszq(sjxxDto.getLrsjEnd());
//				}else {
//					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
//					Calendar calendar = Calendar.getInstance();
//					String currentMonth = String.valueOf(calendar.get(Calendar.MONTH));
//					String start1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth);
//					String end1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth);
//					xszb_mzb.setKszq(start1);
//					xszb_mzb.setJszq(end1);
//				}
//				xszb_mzb.setZblxcsmc("M");
//				xszb_mzb.setXms(yxxList);
//				xszb_mzb.setQyid(sjxxDto.getZfl());
//				xszb_mzb.setYhid(sjxxDto.getYhid());
//				xszb_mzb.setPtgss(sjxxDto.getPtgss());
//				List<String> ptgss = new ArrayList<>();
//				if(StringUtil.isNotBlank(sjxxDto.getYhid())){//当从钉钉消息进入
//					ptgss=sjhbxxService.getPtgsByYhid(sjxxDto.getYhid());
//					xszb_mzb.setPtgss(ptgss.toArray(new String[ptgss.size()]));
//				}
//				sjxxDto.getZqs().put("tjxsdcl_"+sjxxDto.getZfl(), xszb_mzb.getKszq()+ "~" + xszb_mzb.getJszq());
//				sjxxDto.getZqs().put("tjxsdclyxxs_"+sjxxDto.getZfl(), StringUtil.join(sjxxDto.getYxxs(),","));
//				List<Map<String, String>> tjxsdclByMonlist = new ArrayList<>();
//				String key = "jsrq_tjxsdcl_"+sjxxDto.getZfl()+"_"+xszb_mzb.getKszq()+ "~" + xszb_mzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
//				Object getXstjdclByMon = redisUtil.hget("weekLeadStatis", key);
//				if (getXstjdclByMon!=null){
//					tjxsdclByMonlist = (List<Map<String, String>>) JSONArray.parse((String) getXstjdclByMon);
//				}else {
//					tjxsdclByMonlist = sjxxStatisticService.getTjsxdcl(xszb_mzb);
//					redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(tjxsdclByMonlist));
//				}
//				map.put("tjxsdcl_"+sjxxDto.getZfl(), tjxsdclByMonlist);
//			}
			//平台指标达成率
			else if("getPtzbdclByYear".equals(method)) {
				//平台指标达成率按年查询
				XszbDto xszb_yzb = new XszbDto();
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					xszb_yzb.setKszq(sjxxDto.getLrsjStart());
					xszb_yzb.setJszq(sjxxDto.getLrsjEnd());
				}else {
					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					String start1 = currentYear+"-01";
					String end1 = currentYear+"-12";
					xszb_yzb.setKszq(start1);
					xszb_yzb.setJszq(end1);
				}
				xszb_yzb.setZblxcsmc("Y");
				xszb_yzb.setXms(yxxList);
				xszb_yzb.setYhid(sjxxDto.getYhid());
				xszb_yzb.setPtgss(sjxxDto.getPtgss());
				xszb_yzb.setYwlx(sjxxDto.getYwlx());
				List<String> ptgss;
				if(StringUtil.isNotBlank(sjxxDto.getYhid())){//当从钉钉消息进入
					ptgss=sjhbxxService.getPtgsByYhid(sjxxDto.getYhid());
					xszb_yzb.setPtgss(ptgss.toArray(new String[ptgss.size()]));
				}
				sjxxDto.getZqs().put("ptzbdcl", xszb_yzb.getKszq()+ "~" + xszb_yzb.getJszq());
				sjxxDto.getZqs().put("ptzbdclsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				List<Map<String, Object>> ptzbdclByYearlist;
				String key = "jsrq_getPtzbdclByYear_"+ xszb_yzb.getKszq()+ "~" + xszb_yzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					ptzbdclByYearlist = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					ptzbdclByYearlist = sjxxStatisticService.getPtzbdcl(xszb_yzb);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ptzbdclByYearlist));
				}
				map.put("ptzbdcl", ptzbdclByYearlist);
			}
			else if("getPtzbdclByQuarter".equals(method)) {
				//平台指标达成率按季度查询
				XszbDto xszb_qzb = new XszbDto();
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					xszb_qzb.setKszq(sjxxDto.getLrsjStart());
					xszb_qzb.setJszq(sjxxDto.getLrsjEnd());
				} else {
					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					Calendar calendar = Calendar.getInstance();
					calendar.get(Calendar.DAY_OF_MONTH);
					int currenMonth;
					if (monthDay>7){
						currenMonth = calendar.get(Calendar.MONTH)+1;
					}else {
						currenMonth = calendar.get(Calendar.MONTH);
					}
					String monthEnd = String.valueOf((currenMonth+2)/3*3);
					String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
					String start1 = currentYear + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
					String end1 = currentYear + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
					xszb_qzb.setKszq(start1);
					xszb_qzb.setJszq(end1);
				}
				xszb_qzb.setZblxcsmc("Q");
				xszb_qzb.setXms(yxxList);
				xszb_qzb.setYhid(sjxxDto.getYhid());
				xszb_qzb.setPtgss(sjxxDto.getPtgss());
				xszb_qzb.setYwlx(sjxxDto.getYwlx());
				List<String> ptgss;
				if(StringUtil.isNotBlank(sjxxDto.getYhid())){//当从钉钉消息进入
					ptgss=sjhbxxService.getPtgsByYhid(sjxxDto.getYhid());
					xszb_qzb.setPtgss(ptgss.toArray(new String[ptgss.size()]));
				}
				sjxxDto.getZqs().put("ptzbdcl", xszb_qzb.getKszq()+ "~" + xszb_qzb.getJszq());
				sjxxDto.getZqs().put("ptzbdclsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				List<Map<String, Object>> ptzbdclByQuarterlist;
				String key = "jsrq_getPtzbdclByQuarter_"+xszb_qzb.getKszq()+ "~" + xszb_qzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					ptzbdclByQuarterlist = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					ptzbdclByQuarterlist = sjxxStatisticService.getPtzbdcl(xszb_qzb);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ptzbdclByQuarterlist));
				}
				map.put("ptzbdcl", ptzbdclByQuarterlist);
			}
			//产品业务占比
			else if(method.startsWith("getCpywzb")) {
				List<Map<String, Object>> cpywzbList;
				String key = null;
//				Object cpywzbListxx = null;
				XszbDto xszbDto = new XszbDto();
				xszbDto.setYwlx_q(sjxxDto.getYwlx_q());
				xszbDto.setYwlx(sjxxDto.getYwlx());
				xszbDto.setYhid(sjxxDto.getYhid());
				xszbDto.setPtgss(sjxxDto.getPtgss());
				if(StringUtil.isNotBlank(sjxxDto.getYhid())){//当从钉钉消息进入
				List<String> ptgss=sjhbxxService.getPtgsByYhid(sjxxDto.getYhid());
					xszbDto.setPtgss(ptgss.toArray(new String[ptgss.size()]));
				}
				if (method.contains("Year")){
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						xszbDto.setKszq(sjxxDto.getLrsjStart().substring(0,4));
						xszbDto.setJszq(sjxxDto.getLrsjEnd().substring(0,4));
					} else {
						xszbDto.setKszq(sjxxDto.getJsrqstart().substring(0,4));
						xszbDto.setJszq(sjxxDto.getJsrqend().substring(0,4));
					}
					xszbDto.setZblxcsmc("Y");
					xszbDto.setXms(Arrays.asList(sjxxDto.getYxxs()));
					sjxxDto.getZqs().put("cpywzbtjzqs",xszbDto.getKszq()+ "~" + xszbDto.getJszq());
					sjxxDto.getZqs().put("cpywzbtjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getCpywzbByYearlist_"+xszbDto.getKszq()+ "~" + xszbDto.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				}else if (method.contains("Mon")){
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						xszbDto.setKszq(sjxxDto.getLrsjStart());
						xszbDto.setJszq(sjxxDto.getLrsjEnd());
					} else {
						String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
						Calendar calendar = Calendar.getInstance();
						String currentMonth;
						if(calendar.get(Calendar.DAY_OF_MONTH)>10){
							currentMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
						}else {
							currentMonth = String.valueOf(calendar.get(Calendar.MONTH));
						}
						String start1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth) ;
						String end1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth) ;
						xszbDto.setKszq(start1);
						xszbDto.setJszq(end1);
					}
					xszbDto.setZblxcsmc("M");
					xszbDto.setXms(Arrays.asList(sjxxDto.getYxxs()));
					sjxxDto.getZqs().put("cpywzbtjzqs", xszbDto.getKszq()+ "~" + xszbDto.getJszq());
					sjxxDto.getZqs().put("cpywzbtjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getCpywzbByMonth_"+xszbDto.getKszq()+ "~" + xszbDto.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				}else if (method.contains("Quarter")){
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						xszbDto.setKszq(sjxxDto.getLrsjStart());
						xszbDto.setJszq(sjxxDto.getLrsjEnd());
					} else {
						String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
						Calendar calendar = Calendar.getInstance();
						int currenMonth;
						if (monthDay>7){
							currenMonth = calendar.get(Calendar.MONTH)+1;
						}else {
							currenMonth = calendar.get(Calendar.MONTH);
						}
						String monthEnd = String.valueOf((currenMonth+2)/3*3);
						String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
						String start1 = currentYear + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
						String end1 = currentYear + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
						xszbDto.setKszq(start1);
						xszbDto.setJszq(end1);
					}
					xszbDto.setZblxcsmc("Q");
					xszbDto.setXms(Arrays.asList(sjxxDto.getYxxs()));

					sjxxDto.getZqs().put("cpywzbtjzqs", xszbDto.getKszq()+ "~" + xszbDto.getJszq());
					sjxxDto.getZqs().put("cpywzbtjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getCpywzbByQuarter_"+xszbDto.getKszq()+ "~" + xszbDto.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				}
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					cpywzbList = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					cpywzbList = sjxxTwoService.getProductionProportion(xszbDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(cpywzbList));
				}
				map.put("cpywzbtj",cpywzbList);
			}
			else if("getPtzbdclByMon".equals(method)) {
				//平台指标达成率按月查询
				XszbDto xszb_mzb = new XszbDto();
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					xszb_mzb.setKszq(sjxxDto.getLrsjStart());
					xszb_mzb.setJszq(sjxxDto.getLrsjEnd());
				} else {
					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					Calendar calendar = Calendar.getInstance();
					String currentMonth;
					if(calendar.get(Calendar.DAY_OF_MONTH)>10){
						currentMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
					}else {
						currentMonth = String.valueOf(calendar.get(Calendar.MONTH));
					}
					String start1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth);
					String end1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth) ;
					xszb_mzb.setKszq(start1);
					xszb_mzb.setJszq(end1);
				}
				xszb_mzb.setZblxcsmc("M");
				xszb_mzb.setXms(yxxList);
				xszb_mzb.setYhid(sjxxDto.getYhid());
				xszb_mzb.setPtgss(sjxxDto.getPtgss());
				xszb_mzb.setYwlx(sjxxDto.getYwlx());
				List<String> ptgss;
				if(StringUtil.isNotBlank(sjxxDto.getYhid())){//当从钉钉消息进入
					ptgss=sjhbxxService.getPtgsByYhid(sjxxDto.getYhid());
					xszb_mzb.setPtgss(ptgss.toArray(new String[ptgss.size()]));
				}
				sjxxDto.getZqs().put("ptzbdcl", xszb_mzb.getKszq() + "~" + xszb_mzb.getJszq());
				sjxxDto.getZqs().put("ptzbdclsyxxs", StringUtil.join(sjxxDto.getYxxs(), ","));
				List<Map<String, Object>> ptzbdclByMonlist;
				String key = "jsrq_getPtzbdclByMon_" + xszb_mzb.getKszq() + "~" + xszb_mzb.getJszq() + "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					ptzbdclByMonlist = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					ptzbdclByMonlist = sjxxStatisticService.getPtzbdcl(xszb_mzb);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ptzbdclByMonlist));
				}
				map.put("ptzbdcl", ptzbdclByMonlist);
			}
// 业务准入发展达成率
//			else if("getYwzrfzByYear".equals(method)) {
//				//平台指标达成率按年查询
//				XszbDto xszb_yzb = new XszbDto();
//				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
//					xszb_yzb.setKszq(sjxxDto.getLrsjStart());
//					xszb_yzb.setJszq(sjxxDto.getLrsjEnd());
//				} else {
//					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
//					String start1 = currentYear+"-01";
//					String end1 = currentYear+"-12";
//					xszb_yzb.setKszq(start1);
//					xszb_yzb.setJszq(end1);
//				}
//				xszb_yzb.setZblxcsmc("Y");
//				xszb_yzb.setXms(Arrays.asList(sjxxDto.getYxxs()));
//
//				sjxxDto.getZqs().put("ywzrfz", xszb_yzb.getKszq()+ "~" + xszb_yzb.getJszq());
//				sjxxDto.getZqs().put("ywzrfzsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
//				List<Map<String, Object>> ywzrfzByYearlist = new ArrayList<>();
//				String key = "jsrq_getYwzrfzByYear_"+ xszb_yzb.getKszq()+ "~" + xszb_yzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
//				Object getYwzrfzByYear = redisUtil.hget("weekLeadStatis", key);
//				if (getYwzrfzByYear!=null){
//					ywzrfzByYearlist = (List<Map<String, Object>>) JSONArray.parse((String) getYwzrfzByYear);
//				}else {
//					ywzrfzByYearlist = sjxxStatisticService.getYwfzbZbdcl(xszb_yzb);
//					redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ywzrfzByYearlist));
//				}
//				map.put("ywzrfz", ywzrfzByYearlist);
//			}
			else if("getYwzrfzByQuarter".equals(method)) {
				//业务准入发展达成率按季度查询
				XszbDto xszb_qzb = new XszbDto();
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					xszb_qzb.setKszq(sjxxDto.getLrsjStart());
					xszb_qzb.setJszq(sjxxDto.getLrsjEnd());
				} else {
					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					Calendar calendar = Calendar.getInstance();
					int currenMonth;
					if (monthDay>7){
						currenMonth = calendar.get(Calendar.MONTH)+1;
					}else {
						currenMonth = calendar.get(Calendar.MONTH);
					}
					String monthEnd = String.valueOf((currenMonth+2)/3*3);
					String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
					String start1 = currentYear + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
					String end1 = currentYear + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
					xszb_qzb.setKszq(start1);
					xszb_qzb.setJszq(end1);
				}
				xszb_qzb.setZblxcsmc("Q");
				xszb_qzb.setXms(yxxList);
				xszb_qzb.setYwlx(sjxxDto.getYwlx());

				sjxxDto.getZqs().put("ywzrfz", xszb_qzb.getKszq()+ "~" + xszb_qzb.getJszq());
				sjxxDto.getZqs().put("ywzrfzsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				List<Map<String, Object>> ywzrfzByQuarterlist;
				String key = "jsrq_getYwzrfzByQuarter_"+xszb_qzb.getKszq()+ "~" + xszb_qzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					ywzrfzByQuarterlist = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					ywzrfzByQuarterlist = sjxxStatisticService.getYwfzbZbdcl(xszb_qzb);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ywzrfzByQuarterlist));
				}
				map.put("ywzrfz", ywzrfzByQuarterlist);
			}
//			else if("getYwzrfzByMon".equals(method)) {
//				//业务准入发展达成率按月查询
//				XszbDto xszb_mzb = new XszbDto();
//				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
//					xszb_mzb.setKszq(sjxxDto.getLrsjStart());
//					xszb_mzb.setJszq(sjxxDto.getLrsjEnd());
//				}else {
//					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
//					Calendar calendar = Calendar.getInstance();
//					String currentMonth = String.valueOf(calendar.get(Calendar.MONTH));
//					String start1 = currentYear + "-" + currentMonth;
//					String end1 = currentYear + "-" + currentMonth;
//					xszb_mzb.setKszq(start1);
//					xszb_mzb.setJszq(end1);
//				}
//				xszb_mzb.setZblxcsmc("M");
//				xszb_mzb.setXms(yxxList);
//
//				sjxxDto.getZqs().put("ywzrfz", xszb_mzb.getKszq()+ "~" + xszb_mzb.getJszq());
//				sjxxDto.getZqs().put("ywzrfzsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
//				List<Map<String, Object>>ywzrfzByMonlist = new ArrayList<>();
//				String key = "jsrq_getYwzrfzByMon_"+xszb_mzb.getKszq()+ "~" + xszb_mzb.getJszq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
//				Object getYwzrfzByMon = redisUtil.hget("weekLeadStatis", key);
//				if (getYwzrfzByMon!=null){
//					ywzrfzByMonlist = (List<Map<String, Object>>) JSONArray.parse((String) getYwzrfzByMon);
//				}else {
//					ywzrfzByMonlist = sjxxStatisticService.getPtzbdcl(xszb_mzb);
//					redisUtil.hset("weekLeadStatis", key,JSON.toJSONString(ywzrfzByMonlist));
//				}
//				map.put("ywzrfz", ywzrfzByMonlist);
//			}
			else if("getYbxxDR_year".equals(method)) {
				//检测总次数按年查询
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String, String>> jcxmnum;
				String key = yhid + "_getYbxxDR_year_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",") +"_"+ StringUtil.join(sjxxDto.getYxxs(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					jcxmnum = SjxxStatisticService.getYbxxDRByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(jcxmnum));
				}
				sjxxDto.getZqs().put("jcxmnumyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				map.put("jcxmnum", jcxmnum);
			}else if("getYbxxDR_mon".equals(method)) {
				//检测总次数按月查询
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String, String>> jcxmnum;
				String key = yhid + "_getYbxxDR_mon_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",") +"_"+ StringUtil.join(sjxxDto.getYxxs(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					jcxmnum = SjxxStatisticService.getYbxxDRByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(jcxmnum));
				}
				sjxxDto.getZqs().put("jcxmnumyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				map.put("jcxmnum", jcxmnum);
			}else if("getYbxxDR_week".equals(method)) {
				//检测总次数按周查询
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("week");
				sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> jcxmnum;
				String key = yhid + "_getYbxxDR_week_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",") +"_"+ StringUtil.join(sjxxDto.getYxxs(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					jcxmnum = SjxxStatisticService.getYbxxDR_weeekByJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(jcxmnum));
				}
				sjxxDto.getZqs().put("jcxmnumyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				map.put("jcxmnum", jcxmnum);
			}else if("getYbxxDR_day".equals(method)) {
				//检测总次数按日查询
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("jcxmnum", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> jcxmnum;
				String key = yhid + "_getYbxxDR_day_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",") +"_"+ StringUtil.join(sjxxDto.getYxxs(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					jcxmnum = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					jcxmnum = SjxxStatisticService.getYbxxDRByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(jcxmnum));
				}
				sjxxDto.getZqs().put("jcxmnumyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				map.put("jcxmnum", jcxmnum);
			}else if("getYbxxType_year".equals(method)) {
				//收费测试数信息
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String, String>> ybxxType;
				String key = yhid + "_getYbxxType_year_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",") +"_"+ StringUtil.join(sjxxDto.getYxxs(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					ybxxType = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					ybxxType = SjxxStatisticService.getYbxxTypeByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ybxxType));
				}
				sjxxDto.getZqs().put("ybxxTypeyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				map.put("ybxxType", ybxxType);
			}else if("getYbxxType_mon".equals(method)) {
				//收费测试数信息
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String, String>> ybxxType;
				String key = yhid + "_getYbxxType_mon_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",") +"_"+ StringUtil.join(sjxxDto.getYxxs(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					ybxxType = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					ybxxType = SjxxStatisticService.getYbxxTypeByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ybxxType));
				}
				sjxxDto.getZqs().put("ybxxTypeyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				map.put("ybxxType", ybxxType);
			}else if("getYbxxType_week".equals(method)) {
				//收费测试数信息
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("week");
				sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> ybxxType;
				String key = yhid + "_getYbxxType_week_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",") +"_"+ StringUtil.join(sjxxDto.getYxxs(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					ybxxType = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					ybxxType = SjxxStatisticService.getYbxxType_weekByJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ybxxType));
				}
				sjxxDto.getZqs().put("ybxxTypeyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				map.put("ybxxType", ybxxType);
			}else if("getYbxxType_day".equals(method)) {
				//收费测试数信息
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("ybxxType", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> ybxxType;
				String key = yhid + "_getYbxxType_day_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",") +"_"+ StringUtil.join(sjxxDto.getYxxs(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					ybxxType = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					ybxxType = SjxxStatisticService.getYbxxTypeByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ybxxType));
				}
				sjxxDto.getZqs().put("ybxxTypeyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				map.put("ybxxType", ybxxType);
			}else if("getKsByWeekly_year".equals(method)) {
				//送检科室(年)
				setDateByYear(sjxxDto,0,false);
				sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String, String>> ksList;
				String key = yhid + "_getKsByWeekly_year_" + sjxxDto.getZq();
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					ksList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					ksList = SjxxStatisticService.getKsByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ksList));
				}
				map.put("ksList", ksList);
			}else if("getKsByWeekly_mon".equals(method)) {
				//送检科室(月)
				setDateByMonth(sjxxDto,0);
				sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String, String>> ksList;
				String key = yhid + "_getKsByWeekly_mon_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					ksList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					ksList = SjxxStatisticService.getKsByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ksList));
				}
				map.put("ksList", ksList);
			}else if("getKsByWeekly_week".equals(method)) {
				//送检科室(周)
				setDateByWeek(sjxxDto);
				sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> ksList;
				String key = yhid + "_getKsByWeekly_week_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					ksList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					ksList = SjxxStatisticService.getKsByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ksList));
				}
				 map.put("ksList", ksList);
			}else if("getKsByWeekly_day".equals(method)) {
				//送检科室(日)
				setDateByDay(sjxxDto,0);
				sjxxDto.getZqs().put("ksList", sjxxDto.getJsrqstart());
				List<Map<String, String>> ksList;
				String key = yhid + "_getKsByWeekly_day_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					ksList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					ksList = SjxxStatisticService.getKsByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(ksList));
				}
				map.put("ksList", ksList);
			}else if("getSjhb_year".equals(method)) {
				//送检排名按年
				setDateByYear(sjxxDto,0,false);
				sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
				List<Map<String, String>> dblist;
				String key = yhid + "_getSjhb_year_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					dblist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					dblist = SjxxStatisticService.getSjhbByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(dblist));
				}
				map.put("dbtj",dblist);
			}else if("getSjhb_mon".equals(method)) {
				//送检排名按月
				setDateByMonth(sjxxDto,0);
				sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
				List<Map<String, String>> dblist;
				String key = yhid + "_getSjhb_mon_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					dblist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					dblist = SjxxStatisticService.getSjhbByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(dblist));
				}
				map.put("dbtj",dblist);
			}else if("getSjhb_week".equals(method)) {
				//送检排名按周
				setDateByWeek(sjxxDto);
				sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				List<Map<String, String>> dblist;
				String key = yhid + "_getSjhb_week_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					dblist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					dblist = SjxxStatisticService.getSjhbByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(dblist));
				}
				map.put("dbtj",dblist);
			}else if("getSjhb_day".equals(method)) {
				//送检排名按日
				setDateByDay(sjxxDto,0);
				sjxxDto.getZqs().put("dbtj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				List<Map<String, String>> dblist;
				String key = yhid + "_getSjhb_day_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					dblist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					dblist = SjxxStatisticService.getSjhbByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(dblist));
				}
				map.put("dbtj",dblist);
			}else if("getFjsqDaily_year".equals(method)) {
				//复检按年查询
				setDateByYear(sjxxDto,0,false);
				sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				FjsqDto fjsqDto=new FjsqDto();
				fjsqDto.setYhid(sjxxDto.getYhid());
				fjsqDto.setPtgss(sjxxDto.getPtgss());
				fjsqDto.setLrsjYstart(sjxxDto.getJsrqYstart());
				fjsqDto.setLrsjYend(sjxxDto.getJsrqYend());
				List<Map<String, String>> fjsqMap;
				String key = yhid + "_getFjsqDaily_year_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					fjsqMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					fjsqMap = SjxxStatisticService.getFjsqDaily(fjsqDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fjsqMap));
				}
				map.put("fjsq", fjsqMap);
			}else if("getFjsqDaily_mon".equals(method)) {
				//复检按月查询
				setDateByMonth(sjxxDto,0);
				sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				FjsqDto fjsqDto=new FjsqDto();
				fjsqDto.setYhid(sjxxDto.getYhid());
				fjsqDto.setPtgss(sjxxDto.getPtgss());
				fjsqDto.setLrsjMstart(sjxxDto.getJsrqMstart());
				fjsqDto.setLrsjMend(sjxxDto.getJsrqMend());
				List<Map<String, String>> fjsqMap;
				String key = yhid + "_getFjsqDaily_mon_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					fjsqMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					fjsqMap = SjxxStatisticService.getFjsqDaily(fjsqDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fjsqMap));
				}
				map.put("fjsq", fjsqMap);
			}else if("getFjsqDaily_week".equals(method)) {
				//复检按周查询
				setDateByWeek(sjxxDto);
				sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				FjsqDto fjsqDto=new FjsqDto();
				fjsqDto.setYhid(sjxxDto.getYhid());
				fjsqDto.setPtgss(sjxxDto.getPtgss());
				fjsqDto.setLrsjstart(sjxxDto.getJsrqstart());
				fjsqDto.setLrsjend(sjxxDto.getJsrqend());
				List<Map<String, String>> fjsqMap;
				String key = yhid + "_getFjsqDaily_week_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					fjsqMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					fjsqMap = SjxxStatisticService.getFjsqDaily(fjsqDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fjsqMap));
				}
				map.put("fjsq", fjsqMap);
			}else if("getFjsqDaily_day".equals(method)) {
				//复检按日查询
				setDateByDay(sjxxDto,0);
				sjxxDto.getZqs().put("fjsq", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				FjsqDto fjsqDto=new FjsqDto();
				fjsqDto.setYhid(sjxxDto.getYhid());
				fjsqDto.setPtgss(sjxxDto.getPtgss());
				fjsqDto.setLrsjstart(sjxxDto.getJsrqstart());
				fjsqDto.setLrsjend(sjxxDto.getJsrqend());
				List<Map<String, String>> fjsqMap;
				String key = yhid + "_getFjsqDaily_day_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					fjsqMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					fjsqMap = SjxxStatisticService.getFjsqDaily(fjsqDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fjsqMap));
				}
				map.put("fjsq", fjsqMap);
			}else if("getFqybByYbztDaily_year".equals(method)) {
				//废弃标本按年查询
				setDateByYear(sjxxDto,0,false);
				sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String, String>> fqybMap;
				String key = yhid + "_getFqybByYbztDaily_year_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					fqybMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					fqybMap = SjxxStatisticService.getFqybByYbztDaily(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fqybMap));
				}
				map.put("fqyb",fqybMap);
			}else if("getFqybByYbztDaily_mon".equals(method)) {
				//废弃标本按月查询
				setDateByMonth(sjxxDto,0);
				sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String, String>> fqybMap;
				String key = yhid + "_getFqybByYbztDaily_mon_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					fqybMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					fqybMap = SjxxStatisticService.getFqybByYbztDaily(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fqybMap));
				}
				map.put("fqyb",fqybMap);
			}else if("getFqybByYbztDaily_week".equals(method)) {
				//废弃标本按周查询
				setDateByWeek(sjxxDto);
				sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> fqybMap;
				String key = yhid + "_getFqybByYbztDaily_week_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					fqybMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					fqybMap = SjxxStatisticService.getFqybByYbztDaily(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fqybMap));
				}
				map.put("fqyb",fqybMap);
			}else if("getFqybByYbztDaily_day".equals(method)) {
				//废弃标本按日查询
				setDateByDay(sjxxDto,0);
				sjxxDto.getZqs().put("fqyb", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> fqybMap;
				String key = yhid + "_getFqybByYbztDaily_day_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					fqybMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					fqybMap = SjxxStatisticService.getFqybByYbztDaily(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(fqybMap));
				}
				map.put("fqyb",fqybMap);
			}else if("getFlByYear".equals(method)){
				//分类情况按年
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("fltj_"+sjxxDto.getHbfl()+"yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
				List<Map<String, String>> fllists=SjxxStatisticService.getHbflTestCountStatistics(sjxxDto);
				if(fllists!=null) {
					XxdyDto xxdyDto=new XxdyDto();
					xxdyDto.setCskz1("JCSJ");
					xxdyDto.setCskz2(BasicDataTypeEnum.CLASSIFY.getCode());
					xxdyDto.setYxx(sjxxDto.getHbfl());
					List<XxdyDto> xxdyDtos = xxdyService.getListGroupByYxx(xxdyDto);
					if(xxdyDtos!=null&&xxdyDtos.size()>0){
						for(XxdyDto xxdyDto_t:xxdyDtos){
							List<Map<String, String>> newList=new ArrayList<>();
							for(String time:rqs){
								Map<String, String> newMap_one=new HashMap<>();
								newMap_one.put("rq",time);
								newMap_one.put("sfsf","1");
								newMap_one.put("yxx",xxdyDto_t.getYxx());
								newMap_one.put("num","0");
								for(Map<String, String> map_t:fllists){
									if(xxdyDto_t.getYxx().equals(map_t.get("yxx"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
										newMap_one.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_one);
								Map<String, String> newMap_two=new HashMap<>();
								newMap_two.put("rq",time);
								newMap_two.put("sfsf","0");
								newMap_two.put("yxx",xxdyDto_t.getYxx());
								newMap_two.put("num","0");
								for(Map<String, String> map_t:fllists){
									if(xxdyDto_t.getYxx().equals(map_t.get("yxx"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
										newMap_two.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_two);
							}
							map.put("fltj_"+xxdyDto_t.getYxx(),newList);
						}
					}
				}
			}else if("getFlByMon".equals(method)){
				//分类情况按月
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("fltj_"+sjxxDto.getHbfl()+"yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
				List<Map<String, String>> fllists=SjxxStatisticService.getHbflTestCountStatistics(sjxxDto);
				if(fllists!=null) {
					XxdyDto xxdyDto=new XxdyDto();
					xxdyDto.setCskz1("JCSJ");
					xxdyDto.setCskz2(BasicDataTypeEnum.CLASSIFY.getCode());
					xxdyDto.setYxx(sjxxDto.getHbfl());
					List<XxdyDto> xxdyDtos = xxdyService.getListGroupByYxx(xxdyDto);
					if(xxdyDtos!=null&&xxdyDtos.size()>0){
						for(XxdyDto xxdyDto_t:xxdyDtos){
							List<Map<String, String>> newList=new ArrayList<>();
							for(String time:rqs){
								Map<String, String> newMap_one=new HashMap<>();
								newMap_one.put("rq",time);
								newMap_one.put("sfsf","1");
								newMap_one.put("yxx",xxdyDto_t.getYxx());
								newMap_one.put("num","0");
								for(Map<String, String> map_t:fllists){
									if(xxdyDto_t.getYxx().equals(map_t.get("yxx"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
										newMap_one.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_one);
								Map<String, String> newMap_two=new HashMap<>();
								newMap_two.put("rq",time);
								newMap_two.put("sfsf","0");
								newMap_two.put("yxx",xxdyDto_t.getYxx());
								newMap_two.put("num","0");
								for(Map<String, String> map_t:fllists){
									if(xxdyDto_t.getYxx().equals(map_t.get("yxx"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
										newMap_two.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_two);
							}
							map.put("fltj_"+xxdyDto_t.getYxx(),newList);
						}
					}
				}
			}else if("getFlByWeek".equals(method)){
				//分类情况按周
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
				sjxxDto.setTj("week");
				sjxxDto.getZqs().put("fltj_"+sjxxDto.getHbfl()+"yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				List<String> rqs = new ArrayList<>();
				// 设置日期
				try
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));

					int zq = Integer.parseInt(sjxxDto.getZq());
					for (int i = 0; i < zq; i++)
					{
						calendar.add(Calendar.DATE, +7);
						rqs.add(sdf.format(calendar.getTime()));
					}
				} catch (Exception ex){
					logger.error(ex.getMessage());
				}
				List<Map<String, String>> fllists=SjxxStatisticService.getHbflTestCountStatistics(sjxxDto);
				if(fllists!=null) {
					XxdyDto xxdyDto=new XxdyDto();
					xxdyDto.setCskz1("JCSJ");
					xxdyDto.setCskz2(BasicDataTypeEnum.CLASSIFY.getCode());
					xxdyDto.setYxx(sjxxDto.getHbfl());
					List<XxdyDto> xxdyDtos = xxdyService.getListGroupByYxx(xxdyDto);
					if(xxdyDtos!=null&&xxdyDtos.size()>0){
						for(XxdyDto xxdyDto_t:xxdyDtos){
							List<Map<String, String>> newList=new ArrayList<>();
							for(String time:rqs){
								Map<String, String> newMap_one=new HashMap<>();
								newMap_one.put("rq",time);
								newMap_one.put("sfsf","1");
								newMap_one.put("yxx",xxdyDto_t.getYxx());
								newMap_one.put("num","0");
								for(Map<String, String> map_t:fllists){
									if(xxdyDto_t.getYxx().equals(map_t.get("yxx"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
										newMap_one.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_one);
								Map<String, String> newMap_two=new HashMap<>();
								newMap_two.put("rq",time);
								newMap_two.put("sfsf","0");
								newMap_two.put("yxx",xxdyDto_t.getYxx());
								newMap_two.put("num","0");
								for(Map<String, String> map_t:fllists){
									if(xxdyDto_t.getYxx().equals(map_t.get("yxx"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
										newMap_two.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_two);
							}
							map.put("fltj_"+xxdyDto_t.getYxx(),newList);
						}
					}
				}
			}else if("getFlByDay".equals(method)){
				//分类情况按日
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("fltj_"+sjxxDto.getHbfl()+"yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
				List<Map<String, String>> fllists=SjxxStatisticService.getHbflTestCountStatistics(sjxxDto);
				if(fllists!=null) {
					XxdyDto xxdyDto=new XxdyDto();
					xxdyDto.setCskz1("JCSJ");
					xxdyDto.setCskz2(BasicDataTypeEnum.CLASSIFY.getCode());
					xxdyDto.setYxx(sjxxDto.getHbfl());
					List<XxdyDto> xxdyDtos = xxdyService.getListGroupByYxx(xxdyDto);
					if(xxdyDtos!=null&&xxdyDtos.size()>0){
						for(XxdyDto xxdyDto_t:xxdyDtos){
							List<Map<String, String>> newList=new ArrayList<>();
							for(String time:rqs){
								Map<String, String> newMap_one=new HashMap<>();
								newMap_one.put("rq",time);
								newMap_one.put("sfsf","1");
								newMap_one.put("yxx",xxdyDto_t.getYxx());
								newMap_one.put("num","0");
								for(Map<String, String> map_t:fllists){
									if(xxdyDto_t.getYxx().equals(map_t.get("yxx"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
										newMap_one.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_one);
								Map<String, String> newMap_two=new HashMap<>();
								newMap_two.put("rq",time);
								newMap_two.put("sfsf","0");
								newMap_two.put("yxx",xxdyDto_t.getYxx());
								newMap_two.put("num","0");
								for(Map<String, String> map_t:fllists){
									if(xxdyDto_t.getYxx().equals(map_t.get("yxx"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
										newMap_two.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_two);
							}
							map.put("fltj_"+xxdyDto_t.getYxx(),newList);
						}
					}
				}
			}else if("getHbByYear".equals(method)){
				//伙伴情况按年
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
				sjxxDto.setTj("year");
				List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
				List<Map<String, String>> statistics=SjxxStatisticService.getStatisticsByTjFlAndJsrq(sjxxDto);
				List<String> strmcList=Arrays.asList(sjxxDto.getDbhbs().split(","));
				if(statistics!=null) {
					if(strmcList!=null&&strmcList.size()>0){
						for(String db:strmcList){
							List<Map<String, String>> newList=new ArrayList<>();
							for(String time:rqs){
								Map<String, String> newMap_one=new HashMap<>();
								newMap_one.put("rq",time);
								newMap_one.put("sfsf","1");
								newMap_one.put("db",db);
								newMap_one.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
										newMap_one.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_one);
								Map<String, String> newMap_two=new HashMap<>();
								newMap_two.put("rq",time);
								newMap_two.put("sfsf","0");
								newMap_two.put("db",db);
								newMap_two.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
										newMap_two.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_two);
							}
							map.put("hbtj_"+db,newList);
							sjxxDto.getZqs().put("hbtj_"+db+"yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
						}
					}
				}
			}else if("getHbByMon".equals(method)){
				//伙伴情况按月
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("mon");
				List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
				List<Map<String, String>> statistics=SjxxStatisticService.getStatisticsByTjFlAndJsrq(sjxxDto);
				List<String> strmcList=Arrays.asList(sjxxDto.getDbhbs().split(","));
				if(statistics!=null) {
					if(strmcList!=null&&strmcList.size()>0){
						for(String db:strmcList){
							List<Map<String, String>> newList=new ArrayList<>();
							for(String time:rqs){
								Map<String, String> newMap_one=new HashMap<>();
								newMap_one.put("rq",time);
								newMap_one.put("sfsf","1");
								newMap_one.put("db",db);
								newMap_one.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
										newMap_one.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_one);
								Map<String, String> newMap_two=new HashMap<>();
								newMap_two.put("rq",time);
								newMap_two.put("sfsf","0");
								newMap_two.put("db",db);
								newMap_two.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
										newMap_two.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_two);
							}
							map.put("hbtj_"+db,newList);
							sjxxDto.getZqs().put("hbtj_"+db+"yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
						}
					}
				}
			}else if("getHbByWeek".equals(method)){
				//伙伴情况按周
//					int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
				sjxxDto.setTj("week");
//				String key = yhid +"_getHbByWeek_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				if(sjxxDto.getDbmcs()!=null&&sjxxDto.getDbmcs().size()>0) {
					sjxxDto.setDbs(sjxxDto.getDbmcs());
				}
				List<String> rqs = new ArrayList<>();
				// 设置日期
				try
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));

					int zq = Integer.parseInt(sjxxDto.getZq());
					for (int i = 0; i < zq; i++)
					{
						calendar.add(Calendar.DATE, +7);
						rqs.add(sdf.format(calendar.getTime()));
					}
				} catch (Exception ex){
					logger.error(ex.getMessage());
				}
				List<Map<String, String>> statistics=SjxxStatisticService.getStatisticsByTjFlAndJsrq(sjxxDto);
				List<String> strmcList=Arrays.asList(sjxxDto.getDbhbs().split(","));
				if(statistics!=null) {
					if(strmcList!=null&&strmcList.size()>0){
						for(String db:strmcList){
							List<Map<String, String>> newList=new ArrayList<>();
							for(String time:rqs){
								Map<String, String> newMap_one=new HashMap<>();
								newMap_one.put("rq",time);
								newMap_one.put("sfsf","1");
								newMap_one.put("db",db);
								newMap_one.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
										newMap_one.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_one);
								Map<String, String> newMap_two=new HashMap<>();
								newMap_two.put("rq",time);
								newMap_two.put("sfsf","0");
								newMap_two.put("db",db);
								newMap_two.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
										newMap_two.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_two);
							}
							map.put("hbtj_"+db,newList);
							sjxxDto.getZqs().put("hbtj_"+db+"yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
						}
					}
				}
			}else if("getHbByDay".equals(method)){
				//伙伴情况按日
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("day");
				List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
				List<Map<String, String>> statistics=SjxxStatisticService.getStatisticsByTjFlAndJsrq(sjxxDto);
				List<String> strmcList=Arrays.asList(sjxxDto.getDbhbs().split(","));
				if(statistics!=null) {
					if(strmcList!=null&&strmcList.size()>0){
						for(String db:strmcList){
							List<Map<String, String>> newList=new ArrayList<>();
							for(String time:rqs){
								Map<String, String> newMap_one=new HashMap<>();
								newMap_one.put("rq",time);
								newMap_one.put("sfsf","1");
								newMap_one.put("db",db);
								newMap_one.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
										newMap_one.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_one);
								Map<String, String> newMap_two=new HashMap<>();
								newMap_two.put("rq",time);
								newMap_two.put("sfsf","0");
								newMap_two.put("db",db);
								newMap_two.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
										newMap_two.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_two);
							}
							map.put("hbtj_"+db,newList);
							sjxxDto.getZqs().put("hbtj_"+db+"yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
						}
					}
				}
			}else if("getAllhbByYear".equals(method)) {
				//合作伙伴总数按年
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
				List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
				List<Map<String, String>> statistics=SjxxStatisticService.getStatisticsByTjFlAndJsrq(sjxxDto);
				List<String> strmcList=Arrays.asList(sjxxDto.getDbhbs().split(","));
				List<Map<String, String>> hbztj=new ArrayList<>();
				if(statistics!=null) {
					if(strmcList!=null&&strmcList.size()>0){
						for(String db:strmcList){
							List<Map<String, String>> newList=new ArrayList<>();
							for(String time:rqs){
								Map<String, String> newMap_one=new HashMap<>();
								newMap_one.put("rq",time);
								newMap_one.put("sfsf","1");
								newMap_one.put("db",db);
								newMap_one.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
										newMap_one.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_one);
								Map<String, String> newMap_two=new HashMap<>();
								newMap_two.put("rq",time);
								newMap_two.put("sfsf","0");
								newMap_two.put("db",db);
								newMap_two.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
										newMap_two.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_two);
							}
							map.put("hbtj_"+db,newList);
							hbztj.addAll(newList);
						}
					}
				}
				map.put("hbztj",hbztj);
				sjxxDto.getZqs().put("hbztjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
			}else if("getAllhbByMon".equals(method)) {
				//合作伙伴总数按月
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
				sjxxDto.setTj("mon");
				List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
				List<Map<String, String>> statistics=SjxxStatisticService.getStatisticsByTjFlAndJsrq(sjxxDto);
				List<String> strmcList=Arrays.asList(sjxxDto.getDbhbs().split(","));
				List<Map<String, String>> hbztj=new ArrayList<>();
				if(statistics!=null) {
					if(strmcList!=null&&strmcList.size()>0){
						for(String db:strmcList){
							List<Map<String, String>> newList=new ArrayList<>();
							for(String time:rqs){
								Map<String, String> newMap_one=new HashMap<>();
								newMap_one.put("rq",time);
								newMap_one.put("sfsf","1");
								newMap_one.put("db",db);
								newMap_one.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
										newMap_one.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_one);
								Map<String, String> newMap_two=new HashMap<>();
								newMap_two.put("rq",time);
								newMap_two.put("sfsf","0");
								newMap_two.put("db",db);
								newMap_two.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
										newMap_two.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_two);
							}
							map.put("hbtj_"+db,newList);
							hbztj.addAll(newList);
						}
					}
				}
				map.put("hbztj",hbztj);
				sjxxDto.getZqs().put("hbztjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
			}else if("getAllhbByWeek".equals(method)) {
				//合作伙伴总数按周
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
				sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				sjxxDto.setTj("week");
				List<String> rqs = new ArrayList<>();
				// 设置日期
				try
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));

					int zq = Integer.parseInt(sjxxDto.getZq());
					for (int i = 0; i < zq; i++)
					{
						calendar.add(Calendar.DATE, +7);
						rqs.add(sdf.format(calendar.getTime()));
					}
				} catch (Exception ex){
					logger.error(ex.getMessage());
				}
				List<Map<String, String>> statistics=SjxxStatisticService.getStatisticsByTjFlAndJsrq(sjxxDto);
				List<String> strmcList=Arrays.asList(sjxxDto.getDbhbs().split(","));
				List<Map<String, String>> hbztj=new ArrayList<>();
				if(statistics!=null) {
					if(strmcList!=null&&strmcList.size()>0){
						for(String db:strmcList){
							List<Map<String, String>> newList=new ArrayList<>();
							for(String time:rqs){
								Map<String, String> newMap_one=new HashMap<>();
								newMap_one.put("rq",time);
								newMap_one.put("sfsf","1");
								newMap_one.put("db",db);
								newMap_one.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
										newMap_one.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_one);
								Map<String, String> newMap_two=new HashMap<>();
								newMap_two.put("rq",time);
								newMap_two.put("sfsf","0");
								newMap_two.put("db",db);
								newMap_two.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
										newMap_two.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_two);
							}
							map.put("hbtj_"+db,newList);
							hbztj.addAll(newList);
						}
					}
				}
				map.put("hbztj",hbztj);
				sjxxDto.getZqs().put("hbztjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
			}else if("getAllhbByDay".equals(method)) {
				//合作伙伴总数按日
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
				sjxxDto.setTj("day");
				List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
				List<Map<String, String>> statistics=SjxxStatisticService.getStatisticsByTjFlAndJsrq(sjxxDto);
				List<String> strmcList=Arrays.asList(sjxxDto.getDbhbs().split(","));
				List<Map<String, String>> hbztj=new ArrayList<>();
				if(statistics!=null) {
					if(strmcList!=null&&strmcList.size()>0){
						for(String db:strmcList){
							List<Map<String, String>> newList=new ArrayList<>();
							for(String time:rqs){
								Map<String, String> newMap_one=new HashMap<>();
								newMap_one.put("rq",time);
								newMap_one.put("sfsf","1");
								newMap_one.put("db",db);
								newMap_one.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
										newMap_one.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_one);
								Map<String, String> newMap_two=new HashMap<>();
								newMap_two.put("rq",time);
								newMap_two.put("sfsf","0");
								newMap_two.put("db",db);
								newMap_two.put("num","0");
								for(Map<String, String> map_t:statistics){
									if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
										newMap_two.put("num",map_t.get("num"));
										break;
									}
								}
								newList.add(newMap_two);
							}
							map.put("hbtj_"+db,newList);
							hbztj.addAll(newList);
						}
					}
				}
				map.put("hbztj",hbztj);
				sjxxDto.getZqs().put("hbztjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
			}else if("getSjxxYearBySyAndSf".equals(method)) {
				//省份测试数按年查询
				setDateByYear(sjxxDto,-6,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String, String>> sfybqk;
				String key = yhid +"_getSjxxYearBySyAndSf_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					sfybqk = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					sfybqk = SjxxStatisticService.getYbxxByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfybqk));
				}
				map.put("sfybqk", sfybqk);
			}else if("getSjxxMonBySyAndSf".equals(method)) {
				//省份测试数按月查询
				setDateByMonth(sjxxDto,-6);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String, String>> sfybqk;
				String key = yhid +"_getSjxxMonBySyAndSf_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					sfybqk = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					sfybqk = SjxxStatisticService.getYbxxByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfybqk));
				}
				map.put("sfybqk", sfybqk);
			}else if("getSjxxWeekBySyAndSf".equals(method)) {
				//省份测试数按周查询
				setDateByWeek(sjxxDto);
				sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> sfybqk;
				String key = yhid +"_getSjxxWeekBySyAndSf_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					sfybqk = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					sfybqk = SjxxStatisticService.getybxx_weekByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfybqk));
				}
				map.put("sfybqk", sfybqk);
			}else if("getSjxxDayBySyAndSf".equals(method)) {
				//省份测试数按日查询
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("sfybqk", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> sfybqk;
				String key = yhid +"_getSjxxDayBySyAndSf_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					sfybqk = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					sfybqk = SjxxStatisticService.getYbxxByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfybqk));
				}
				map.put("sfybqk", sfybqk);
			}else if("getSjxxDRByYearAndSf".equals(method)) {
				//省份测试数按年查询
				setDateByYear(sjxxDto,-6,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String, String>> sfcss;
				String key = yhid +"_getSjxxDRByYearAndSf_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					sfcss = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					sfcss = SjxxStatisticService.getYbxxDRByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfcss));
				}
				map.put("sfcss", sfcss);
			}else if("getSjxxDRByMonAndSf".equals(method)) {
				//省份测试数按月查询
				setDateByMonth(sjxxDto,-6);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String, String>> sfcss;
				String key = yhid +"_getSjxxDRByMonAndSf_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					sfcss = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					sfcss = SjxxStatisticService.getYbxxDRByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfcss));
				}
				map.put("sfcss", sfcss);
			}else if("getSjxxDRByWeekAndSf".equals(method)) {
				//检测单位测试数按周查询
				sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> sfybqk;
				String key = yhid +"_getSjxxDRByWeekAndSf_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					sfybqk = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					sfybqk = SjxxStatisticService.getYbxxDR_weeekByJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfybqk));
				}
				map.put("sfcss", sfybqk);
			}else if("getSjxxDRByDayAndSf".equals(method)) {
				//检测单位测试数按日查询
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("sfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> sfcss;
				String key = yhid +"_getSjxxDRByDayAndSf_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					sfcss = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					sfcss = SjxxStatisticService.getYbxxDRByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfcss));
				}
				map.put("sfcss", sfcss);
			}else if("getYbxxTypeByYearAndSf".equals(method)) {
				//省份收费标本下边检测项目的总条数 年
				setDateByYear(sjxxDto,-6,false);
				sjxxDto.setTj("year");
				sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				List<Map<String, String>> sfsfcss;
				String key = yhid +"_getYbxxTypeByYearAndSf_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					sfsfcss = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					sfsfcss = SjxxStatisticService.getYbxxTypeByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfsfcss));
				}
				map.put("sfsfcss", sfsfcss);
			}else if("getYbxxTypeByMonAndSf".equals(method)) {
				//省份收费标本下边检测项目的总条数 月
				setDateByMonth(sjxxDto,-6);
				sjxxDto.setTj("mon");
				sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				List<Map<String, String>> sfsfcss;
				String key = yhid +"_getYbxxTypeByMonAndSf_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					sfsfcss = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					sfsfcss = SjxxStatisticService.getYbxxTypeByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfsfcss));
				}
				map.put("sfsfcss", sfsfcss);
			}else if("getYbxxTypeByWeekAndSf".equals(method)) {
				//省份收费标本下边检测项目的总条数 周
				sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> sfsfcss;
				String key = yhid +"_getYbxxTypeByWeekAndSf_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					sfsfcss = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					sfsfcss = SjxxStatisticService.getYbxxType_weekByJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfsfcss));
				}
				map.put("sfsfcss", sfsfcss);
			}else if("getYbxxTypeByDayAndSf".equals(method)) {
				//省份收费标本下边检测项目的总条数 日
				sjxxDto.setTj("day");
				sjxxDto.getZqs().put("sfsfcss", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				List<Map<String, String>> sfsfcss;
				String key = yhid +"_getYbxxTypeByDayAndSf_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					sfsfcss = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					sfsfcss = SjxxStatisticService.getYbxxTypeByWeeklyAndJsrq(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sfsfcss));
				}
				map.put("sfsfcss", sfsfcss);
			}else if ("getSalesAttainmentRateByYear".equals(method)) {
				//销售达成率按年
				String qyid = req.getParameter("qyid");
				XszbDto xszbDto = new XszbDto();
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.YEAR,-4);
				String start = DateUtils.format(calendar.getTime(), "yyyy");
				String end = DateUtils.format(new Date(), "yyyy");
				xszbDto.setKszq(start);
				xszbDto.setJszq(end);
				xszbDto.setYhid(sjxxDto.getYhid());
				xszbDto.setZblxcsmc("Y");
				xszbDto.setQyid(qyid);
				Map<String, Object> salesAttainmentRate;
				String key = yhid +"_getSalesAttainmentRateByYear_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					salesAttainmentRate = (Map<String, Object>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					salesAttainmentRate = SjxxStatisticService.getSalesAttainmentRateByJsrq(xszbDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(salesAttainmentRate));
				}
				if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
					for (String map1 : salesAttainmentRate.keySet()) {
						map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
					}
				}
				sjxxDto.getZqs().put("salesAttainmentRate", start + "~" + end);
				map.put("salesAttainmentRate", salesAttainmentRate);
			} else if ("getSalesAttainmentRateByQuarter".equals(method)) {
				//销售达成率按季度,全年季度
				String qyid = req.getParameter("qyid");
				XszbDto xszbDto = new XszbDto();
				String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                xszbDto.setKszq(currentYear);
				xszbDto.setJszq(currentYear);
				xszbDto.setYhid(sjxxDto.getYhid());
				xszbDto.setZblxcsmc("Q");
				xszbDto.setQyid(qyid);
				Map<String, Object> salesAttainmentRate;
				String key = yhid +"_getSalesAttainmentRateByQuarter_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					salesAttainmentRate = (Map<String, Object>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					salesAttainmentRate = SjxxStatisticService.getSalesAttainmentRateByJsrq(xszbDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(salesAttainmentRate));
				}
				if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
					for (String map1 : salesAttainmentRate.keySet()) {
						map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
					}
				}
				sjxxDto.getZqs().put("salesAttainmentRate", currentYear + "~" + currentYear);
				map.put("salesAttainmentRate", salesAttainmentRate);
			} else if ("getSalesAttainmentRateByMon".equals(method)) {
				String qyid = req.getParameter("qyid");
				//销售达成率 默认6个月
				XszbDto xszbDto = new XszbDto();
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.MONTH,-4);
				String start = DateUtils.format(calendar.getTime(), "yyyy-MM");
				String end = DateUtils.format(new Date(), "yyyy-MM");
				xszbDto.setKszq(start);
				xszbDto.setJszq(end);
				xszbDto.setYhid(sjxxDto.getYhid());
				xszbDto.setZblxcsmc("M");
				xszbDto.setQyid(qyid);
				Map<String, Object> salesAttainmentRate;
				String key = yhid +"_getSalesAttainmentRateByMon_" + sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getPtgss(),",");
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					salesAttainmentRate = (Map<String, Object>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					salesAttainmentRate = SjxxStatisticService.getSalesAttainmentRateByJsrq(xszbDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(salesAttainmentRate));
				}
				if(salesAttainmentRate!=null && salesAttainmentRate.size()>0){
					for (String map1 : salesAttainmentRate.keySet()) {
						map.put("salesAttainmentRate_"+map1,salesAttainmentRate.get(map1));
					}
				}
				sjxxDto.getZqs().put("salesAttainmentRate", start + "~" + end);
				map.put("salesAttainmentRate", salesAttainmentRate);
			}else if(method.startsWith("getTopDsf20")) {
				List<Map<String, String>> topDsf20List;
				String key = null;
//				Object topDsf20Listxx = null;
				if (method.contains("Year")){
					sjxxDto.setTj("year");
					setDateByYear(sjxxDto,0,false);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqYstart(null);
						sjxxDto.setJsrqYend(null);
						sjxxDto.getZqs().put("topDsf20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topDsf20", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
					}
					sjxxDto.getZqs().put("topDsf20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopDsf20ByYear"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}else if (method.contains("Mon")){
					sjxxDto.setTj("mon");
					setDateByMonth(sjxxDto,0);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqMstart(null);
						sjxxDto.setJsrqMend(null);
						sjxxDto.getZqs().put("topDsf20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topDsf20", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
					}
					sjxxDto.getZqs().put("topDsf20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopDsf20ByMon"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}else if (method.contains("Week")){
					sjxxDto.setTj("week");
					setDateByWeek(sjxxDto);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqstart(null);
						sjxxDto.setJsrqend(null);
						sjxxDto.getZqs().put("topDsf20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topDsf20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					}
					sjxxDto.getZqs().put("topDsf20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopDsf20ByWeek"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}else if (method.contains("Day")){
					sjxxDto.setTj("day");
					setDateByDay(sjxxDto,0);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqstart(null);
						sjxxDto.setJsrqend(null);
						sjxxDto.getZqs().put("topDsf20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topDsf20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					}
					sjxxDto.getZqs().put("topDsf20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopDsf20ByDay"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					topDsf20List = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					topDsf20List = sjxxTwoService.getTopDsf20(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(topDsf20List));
				}
				map.put("topDsf20",topDsf20List);
			}else if(method.startsWith("getTopZx20")) {
				List<Map<String, String>> topZx20List;
				String key = null;
//				Object topZx20Listxx = null;
				if (method.contains("Year")){
					sjxxDto.setTj("year");
					setDateByYear(sjxxDto,0,false);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqYstart(null);
						sjxxDto.setJsrqYend(null);
						sjxxDto.getZqs().put("topZx20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topZx20", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
					}
					sjxxDto.getZqs().put("topZx20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopZx20ByYear"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}else if (method.contains("Mon")){
					sjxxDto.setTj("mon");
					setDateByMonth(sjxxDto,0);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqMstart(null);
						sjxxDto.setJsrqMend(null);
						sjxxDto.getZqs().put("topZx20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topZx20", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
					}
					sjxxDto.getZqs().put("topZx20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopZx20ByMon"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}else if (method.contains("Week")){
					sjxxDto.setTj("week");
					setDateByWeek(sjxxDto);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqstart(null);
						sjxxDto.setJsrqend(null);
						sjxxDto.getZqs().put("topZx20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topZx20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					}
					sjxxDto.getZqs().put("topZx20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopZx20ByWeek"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}else if (method.contains("Day")){
					sjxxDto.setTj("day");
					setDateByDay(sjxxDto,0);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqstart(null);
						sjxxDto.setJsrqend(null);
						sjxxDto.getZqs().put("topZx20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topZx20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					}
					sjxxDto.getZqs().put("topZx20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopZx20ByDay"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					topZx20List = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					topZx20List = sjxxTwoService.getTopZx20(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(topZx20List));
				}
				map.put("topZx20",topZx20List);
			}else if(method.startsWith("getTopCSO20")) {
				List<Map<String, String>> topCSO20List;
				String key = null;
//				Object topCSO20Listxx = null;
				if (method.contains("Year")) {
					sjxxDto.setTj("year");
					setDateByYear(sjxxDto, 0, false);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqYstart(null);
						sjxxDto.setJsrqYend(null);
						sjxxDto.getZqs().put("topCSO20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topCSO20", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
					}
					sjxxDto.getZqs().put("topCSO20yxxs", StringUtil.join(sjxxDto.getYxxs(), ","));
					key = "jsrq_getTopCSO20ByYear" + "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				} else if (method.contains("Mon")) {
					sjxxDto.setTj("mon");
					setDateByMonth(sjxxDto, 0);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqMstart(null);
						sjxxDto.setJsrqMend(null);
						sjxxDto.getZqs().put("topCSO20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topCSO20", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
					}
					sjxxDto.getZqs().put("topCSO20yxxs", StringUtil.join(sjxxDto.getYxxs(), ","));
					key = "jsrq_getTopCSO20ByMon" + "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				} else if (method.contains("Week")) {
					sjxxDto.setTj("week");
					setDateByWeek(sjxxDto);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqstart(null);
						sjxxDto.setJsrqend(null);
						sjxxDto.getZqs().put("topCSO20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topCSO20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					}
					sjxxDto.getZqs().put("topCSO20yxxs", StringUtil.join(sjxxDto.getYxxs(), ","));
					key = "jsrq_getTopCSO20ByWeek" + "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				} else if (method.contains("Day")) {
					sjxxDto.setTj("day");
					setDateByDay(sjxxDto, 0);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqstart(null);
						sjxxDto.setJsrqend(null);
						sjxxDto.getZqs().put("topCSO20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topCSO20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					}
					sjxxDto.getZqs().put("topCSO20yxxs", StringUtil.join(sjxxDto.getYxxs(), ","));
					key = "jsrq_getTopCSO20ByDay" + "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					topCSO20List = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					topCSO20List = sjxxTwoService.getTopCSO20(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(topCSO20List));
				}
				map.put("topCSO20", topCSO20List);
			}else if(method.startsWith("getTopRY20")) {
				List<Map<String, String>> topRY20List;
				String key = null;
//				Object topRY20Listxx = null;
				if (method.contains("Year")){
					sjxxDto.setTj("year");
					setDateByYear(sjxxDto,0,false);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqYstart(null);
						sjxxDto.setJsrqYend(null);
						sjxxDto.getZqs().put("topRY20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topRY20", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
					}
					sjxxDto.getZqs().put("topRY20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopRY20ByYear"+ "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}else if (method.contains("Mon")){
					sjxxDto.setTj("mon");
					setDateByMonth(sjxxDto,0);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqMstart(null);
						sjxxDto.setJsrqMend(null);
						sjxxDto.getZqs().put("topRY20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topRY20", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
					}
					sjxxDto.getZqs().put("topRY20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopRY20ByMon"+ "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}else if (method.contains("Week")){
					sjxxDto.setTj("week");
					setDateByWeek(sjxxDto);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqstart(null);
						sjxxDto.setJsrqend(null);
						sjxxDto.getZqs().put("topRY20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topRY20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					}
					sjxxDto.getZqs().put("topRY20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopRY20ByWeek"+ "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}else if (method.contains("Day")){
					sjxxDto.setTj("day");
					setDateByDay(sjxxDto,0);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqstart(null);
						sjxxDto.setJsrqend(null);
						sjxxDto.getZqs().put("topRY20", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topRY20", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					}
					sjxxDto.getZqs().put("topRY20yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopRY20ByDay"+ "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					topRY20List = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					topRY20List = sjxxTwoService.getTopRY20(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(topRY20List));
				}
				map.put("topRY20",topRY20List);
			}else if(method.startsWith("getTopHxyxList")) {
				List<Map<String, Object>> topHxyxList;
				String key = null;
//				Object topHxyxListxx = null;
				if (method.contains("Year")){
					sjxxDto.setTj("year");
					setDateByYear(sjxxDto,0,false);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqYstart(null);
						sjxxDto.setJsrqYend(null);
						sjxxDto.getZqs().put("topHxyxList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topHxyxList", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
					}
					sjxxDto.getZqs().put("topHxyxListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopHxyxListByYear"+ "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}else if (method.contains("Mon")){
					sjxxDto.setTj("mon");
					setDateByMonth(sjxxDto,0);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqMstart(null);
						sjxxDto.setJsrqMend(null);
						sjxxDto.getZqs().put("topHxyxList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topHxyxList", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
					}
					sjxxDto.getZqs().put("topHxyxListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopHxyxListByMon"+ "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}else if (method.contains("Week")){
					sjxxDto.setTj("week");
					setDateByWeek(sjxxDto);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqstart(null);
						sjxxDto.setJsrqend(null);
						sjxxDto.getZqs().put("topHxyxList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topHxyxList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					}
					sjxxDto.getZqs().put("topHxyxListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopHxyxListByWeek"+ "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}else if (method.contains("Day")){
					sjxxDto.setTj("day");
					setDateByDay(sjxxDto,0);
					if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
						sjxxDto.setJsrqstart(null);
						sjxxDto.setJsrqend(null);
						sjxxDto.getZqs().put("topHxyxList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
					}else {
						sjxxDto.getZqs().put("topHxyxList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					}
					sjxxDto.getZqs().put("topHxyxListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getTopHxyxListByDay"+ "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid()+"_"+sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd();
				}
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					topHxyxList = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					topHxyxList = sjxxTwoService.getHxyyTopList(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(topHxyxList));
				}
				map.put("topHxyxList",topHxyxList);
			}else if(method.startsWith("getQgqs")) {
				List<Map<String, String>> qgqslist;
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				String key = null;
//				Object getQgqs = null;
				if (method.contains("Year")){
					setDateByYear(sjxxDto,zq+1,false);
					sjxxDto.setTj("year");
					sjxxDto.getZqs().put("qgqszqs", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
					sjxxDto.getZqs().put("qgqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getQgqsSjxxYear_"+sjxxDto.getZq()+ "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				}else if (method.contains("Mon")){
					setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
					sjxxDto.setTj("mon");
					sjxxDto.getZqs().put("qgqszqs", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
					sjxxDto.getZqs().put("qgqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getQgqsSjxxMon_"+sjxxDto.getZq()+ "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				}else if (method.contains("Week")){
					setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),zq+1);
					sjxxDto.setTj("week");
					sjxxDto.getZqs().put("qgqszqs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					sjxxDto.getZqs().put("qgqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getQgqsSjxxWeek_"+sjxxDto.getZq()+ "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				}else if (method.contains("Day")){
					setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
					sjxxDto.setTj("day");
					sjxxDto.getZqs().put("qgqszqs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					sjxxDto.getZqs().put("qgqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getQgqsSjxxDay_"+sjxxDto.getZq()+ "_" + StringUtil.join(sjxxDto.getYxxs(), ",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				}
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					qgqslist = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					qgqslist = sjxxTwoService.getAllCountryChangesForSale(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(qgqslist));
				}
				map.put("qgqs", qgqslist);
			}else if(method.startsWith("getCpqstj")) {
				List<Map<String, Object>> cpzqtjlist;
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				String key = null;
//				Object getCpqstjs = null;
				if (method.contains("Year")){
					setDateByYear(sjxxDto,zq+1,false);
					sjxxDto.setTj("year");
					sjxxDto.getZqs().put("cpqstjzqs", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
					sjxxDto.getZqs().put("cpqstjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getCpqstjSjxxYear_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				}else if (method.contains("Mon")){
					setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
					sjxxDto.setTj("mon");
					sjxxDto.getZqs().put("cpqstjzqs", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
					sjxxDto.getZqs().put("cpqstjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getCpqstjSjxxMon_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				}else if (method.contains("Week")){
					setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),zq+1);
					sjxxDto.setTj("week");
					sjxxDto.getZqs().put("cpqstjzqs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					sjxxDto.getZqs().put("cpqstjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getCpqstjSjxxWeek_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				}else if (method.contains("Day")){
					setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
					sjxxDto.setTj("day");
					sjxxDto.getZqs().put("cpqstjzqs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
					sjxxDto.getZqs().put("cpqstjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
					key = "jsrq_getCpqstjSjxxDay_"+sjxxDto.getZq()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				}
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					cpzqtjlist = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					cpzqtjlist = sjxxTwoService.getProductionChanges(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(cpzqtjlist));
				}
				map.put("cpqstj", cpzqtjlist);
			}else if(method.startsWith("getChargesDivideByKs")) {
				List<Map<String, Object>> chargesDivideByKs;
//				Object chargesDivideByKsxx = null;
				sjxxDto.setTj("day");
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqstart(null);
					sjxxDto.setJsrqend(null);
					sjxxDto.getZqs().put("ksSfcssList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("ksSfcssList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				}
				sjxxDto.getZqs().put("ksSfcssListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				String key = "jsrq_getChargesDivideByKs"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getJsrqstart()+"_"+sjxxDto.getJsrqend()+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd()+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();

				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					chargesDivideByKs = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					chargesDivideByKs = sjxxTwoService.getChargesDivideByKs(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(chargesDivideByKs));
				}
				map.put("ksSfcssList",chargesDivideByKs);
			}else if(method.startsWith("getChargesDivideByYblx")) {
				List<Map<String, Object>> chargesDivideByYblx = new ArrayList<>();
//				Object chargesDivideByYblxxx = null;
				sjxxDto.setTj("day");
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					sjxxDto.setJsrqstart(null);
					sjxxDto.setJsrqend(null);
					sjxxDto.getZqs().put("yblxSfcssList", sjxxDto.getLrsjStart() + "~" + sjxxDto.getLrsjEnd());
				}else {
					sjxxDto.getZqs().put("yblxSfcssList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				}
				sjxxDto.getZqs().put("yblxSfcssListyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				String key = "jsrq_getChargesDivideByYblx"+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+sjxxDto.getJsrqstart()+"_"+sjxxDto.getJsrqend()+"_"+sjxxDto.getLrsjStart()+"_"+sjxxDto.getLrsjEnd()+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();

				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					chargesDivideByYblx = (List<Map<String, Object>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					chargesDivideByYblx = sjxxTwoService.getChargesDivideByYblx(sjxxDto);
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(chargesDivideByYblx));
				}
				map.put("yblxSfcssList",chargesDivideByYblx);
			}else if(method.contains("getHbflcsszb")) {
				String start1, end1;
				if (StringUtil.isNotBlank(sjxxDto.getLrsjStart())&&StringUtil.isNotBlank(sjxxDto.getLrsjEnd())){
					start1 = sjxxDto.getLrsjStart();
					end1 = sjxxDto.getLrsjEnd();
				}else if (method.contains("Year")){
					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					start1 = currentYear+"-01";
					end1 = currentYear+"-12";
				}else if(method.contains("Quarter")){
					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					Calendar calendar = Calendar.getInstance();
					int currenMonth;
					if (monthDay>7){
						currenMonth = calendar.get(Calendar.MONTH)+1;
					}else {
						currenMonth = calendar.get(Calendar.MONTH);
					}
					String monthEnd = String.valueOf((currenMonth+2)/3*3);
					String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
					start1 = currentYear + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
					end1 = currentYear + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
				}else if(method.contains("Mon")){
					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					Calendar calendar = Calendar.getInstance();
					String currentMonth;
					if(calendar.get(Calendar.DAY_OF_MONTH)>10){
						currentMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
					}else {
						currentMonth = String.valueOf(calendar.get(Calendar.MONTH));
					}
					start1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth);
					end1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth) ;
				}else{
					start1 = sjxxDto.getLrsjStart();
					end1 = sjxxDto.getLrsjEnd();
				}
				//伙伴分类测试数占比
				XxdyDto xxdyDto=new XxdyDto();
				xxdyDto.setCskz1("JCSJ");
				xxdyDto.setCskz2(BasicDataTypeEnum.CLASSIFY.getCode());
				List<XxdyDto> xxdyDtos = xxdyService.getListGroupByYxx(xxdyDto);
				sjxxDto.getZqs().put("hbflcsszbzqs", start1 + "~" + end1);
				sjxxDto.getZqs().put("hbflcsszbyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				List<Map<String, String>> newList=new ArrayList<>();
				if(xxdyDtos!=null&&xxdyDtos.size()>0){
					SjxxDto sjxxDto_t=new SjxxDto();
					sjxxDto_t.setJsrqstart(start1);
					sjxxDto_t.setJsrqend(end1);
					sjxxDto_t.setYwlx(sjxxDto.getYwlx());
					sjxxDto_t.setYwlx_q(sjxxDto.getYwlx_q());
					sjxxDto_t.setYxxs(sjxxDto.getYxxs());
					sjxxDto_t.setPtgss(sjxxDto.getPtgss());
					sjxxDto_t.setYhid(sjxxDto.getYhid());
					List<Map<String, String>> hbflcsszbMap;
					String key = "jsrq_"+method+"_"+start1 + "~" + end1+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
					Map<String, Object> objMap = redisUtil.hgetStatistics(key);
					if ( "weekLeadStatis".equals(objMap.get("key")) ){
						hbflcsszbMap = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
						newList = hbflcsszbMap;
					}else {
						hbflcsszbMap = sjxxStatisticService.getHbflcsszb(sjxxDto_t);
						for(XxdyDto xxdyDto_t:xxdyDtos){
							boolean isFind=false;
							for(Map<String, String> map_t:hbflcsszbMap){
								if(xxdyDto_t.getYxx().equals(map_t.get("yxx"))){
									newList.add(map_t);
									isFind=true;
									break;
								}
							}
							if(!isFind){
								Map<String, String> newMap=new HashMap<>();
								newMap.put("num","0");
								newMap.put("yxx",xxdyDto_t.getYxx());
								newList.add(newMap);
							}
						}
						redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(newList));
					}
				}
				map.put("hbflcsszb", newList);
			}
			else if(method.contains("getSjqfcsszb")){
				String start1, end1;
				if (method.contains("Year")){
					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					start1 = currentYear+"-01";
					end1 = currentYear+"-12";
				}else if(method.contains("Quarter")){
					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					Calendar calendar = Calendar.getInstance();
					int currenMonth;
					if (monthDay>7){
						currenMonth = calendar.get(Calendar.MONTH)+1;
					}else {
						currenMonth = calendar.get(Calendar.MONTH);
					}
					String monthEnd = String.valueOf((currenMonth+2)/3*3);
					String monthStart = String.valueOf(((currenMonth-1)/3*3)+1);
					start1 = currentYear + "-" + ( monthStart.length()<2? ("0"+monthStart):monthStart);
					end1 = currentYear + "-" + (monthEnd.length()<2? ("0"+monthEnd):monthEnd);
				}else if(method.contains("Mon")){
					String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
					Calendar calendar = Calendar.getInstance();
					String currentMonth;
					if(calendar.get(Calendar.DAY_OF_MONTH)>10){
						currentMonth = String.valueOf(calendar.get(Calendar.MONTH)+1);
					}else {
						currentMonth = String.valueOf(calendar.get(Calendar.MONTH));
					}
					start1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth);
					end1 = currentYear + "-" + ( currentMonth.length()<2? ("0"+currentMonth):currentMonth) ;
				}else{
					start1 = sjxxDto.getLrsjStart();
					end1 = sjxxDto.getLrsjEnd();
				}
				List<JcsjDto> sjqfs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
				sjxxDto.getZqs().put("sjqfcsszbzqs", start1 + "~" + end1);
				sjxxDto.getZqs().put("sjqfcsszbyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				List<Map<String, String>> sjqfList=new ArrayList<>();
				String key = "jsrq_getSjqfcsszb_"+start1 + "~" + end1+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					sjqfList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					JcsjDto jcsjDto1 = new JcsjDto();
					jcsjDto1.setCsid("第三方实验室");
					jcsjDto1.setCsmc("第三方实验室");
					sjqfs.add(jcsjDto1);
					JcsjDto jcsjDto2 = new JcsjDto();
					jcsjDto2.setCsid("直销");
					jcsjDto2.setCsmc("直销");
					sjqfs.add(jcsjDto2);
					JcsjDto jcsjDto3 = new JcsjDto();
					jcsjDto3.setCsid("CSO");
					jcsjDto3.setCsmc("CSO");
					sjqfs.add(jcsjDto3);
					if(sjqfs!=null&&sjqfs.size()>0){
						SjxxDto sjxxDto_t=new SjxxDto();
						sjxxDto_t.setJsrqstart(start1);
						sjxxDto_t.setJsrqend(end1);
						sjxxDto_t.setYwlx(sjxxDto.getYwlx());
						sjxxDto_t.setYxxs(sjxxDto.getYxxs());
						sjxxDto_t.setPtgss(sjxxDto.getPtgss());
						sjxxDto_t.setYhid(sjxxDto.getYhid());
						sjxxDto_t.setNewflg("zb");
						List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto_t);
						for(JcsjDto jcsjDto:sjqfs){
//							boolean isFind=false;
							for(Map<String, String> map_t:sjqfcsszb){
								if(jcsjDto.getCsid().equals(map_t.get("sjqf"))){
									map_t.put("sjqfmc",jcsjDto.getCsmc());
									sjqfList.add(map_t);
//									isFind=true;
									break;
								}
							}
							// if(!isFind){
							// 	Map<String, String> newMap=new HashMap<>();
							// 	newMap.put("num","0");
							// 	newMap.put("sjqfmc",jcsjDto.getCsmc());
							// 	sjqfList.add(newMap);
							// }
						}
					}
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(sjqfList));
				}
				map.put("sjqfcsszb", sjqfList);
			}
			else if("getXsxzcssqsDay".equals(method)){
				//销售性质测试数趋势
				List<Map<String, String>> xsxzcssqsList=new ArrayList<>();
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByDay(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("day");
				List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
				List<JcsjDto> sjqfs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
				sjxxDto.getZqs().put("xsxzcssqszqs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				sjxxDto.getZqs().put("xsxzcssqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				String key = "jsrq_getXsxzcssqszqs_"+sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					xsxzcssqsList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					JcsjDto jcsjDto1 = new JcsjDto();
					jcsjDto1.setCsid("第三方实验室");
					jcsjDto1.setCsmc("第三方实验室");
					sjqfs.add(jcsjDto1);
					JcsjDto jcsjDto2 = new JcsjDto();
					jcsjDto2.setCsid("直销");
					jcsjDto2.setCsmc("直销");
					sjqfs.add(jcsjDto2);
					JcsjDto jcsjDto3 = new JcsjDto();
					jcsjDto3.setCsid("CSO");
					jcsjDto3.setCsmc("CSO");
					sjqfs.add(jcsjDto3);
					if(sjqfs!=null&&sjqfs.size()>0){
						sjxxDto.setNewflg("qs");
						List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto);
						for(int i=0;i<rqs.size();i++){
							for(JcsjDto jcsjDto:sjqfs){
								boolean isFind=false;
								for(Map<String, String> map_t:sjqfcsszb){
									if(rqs.get(i).equals(map_t.get("rq"))&&jcsjDto.getCsid().equals(map_t.get("sjqf"))){
										map_t.put("sjqfmc",jcsjDto.getCsmc());
										if(i==0){
											map_t.put("bl","0");
										}else{
											var num="0";
											for(Map<String, String> stringMap:xsxzcssqsList){
												if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
													num=stringMap.get("num");
												}
											}
											BigDecimal bigDecimal=new BigDecimal(num);
											BigDecimal bigDecimal1=new BigDecimal(map_t.get("num"));
											map_t.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
										}
										xsxzcssqsList.add(map_t);
										isFind=true;
										break;
									}
								}
								if(!isFind){
									Map<String, String> newMap=new HashMap<>();
									newMap.put("rq",rqs.get(i));
									newMap.put("num","0");
									newMap.put("sjqfmc",jcsjDto.getCsmc());
									newMap.put("sjqf",jcsjDto.getCsid());
									if(i==0){
										newMap.put("bl","0");
									}else{
										var num="0";
										for(Map<String, String> stringMap:xsxzcssqsList){
											if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
												num=stringMap.get("num");
											}
										}
										BigDecimal bigDecimal=new BigDecimal(num);
										BigDecimal bigDecimal1=new BigDecimal("0");
										newMap.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
									}
									xsxzcssqsList.add(newMap);
								}
							}
						}
					}
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(xsxzcssqsList));
				}
				map.put("xsxzcssqs", xsxzcssqsList);
			}
			else if("getXsxzcssqsWeek".equals(method)){
				//销售性质测试数趋势
				List<Map<String, String>> xsxzcssqsList=new ArrayList<>();
				//分类情况按周
				setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
				sjxxDto.setTj("week");
				List<String> rqs = new ArrayList<>();
				// 设置日期
				try
				{
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
					rqs.add(sdf.format(calendar.getTime()));
					int zq = Integer.parseInt(sjxxDto.getZq());
					for (int i = 0; i < zq-1; i++)
					{
						calendar.add(Calendar.DATE, -7);
						rqs.add(sdf.format(calendar.getTime()));
					}
					Collections.reverse(rqs);
				} catch (Exception ex){
					logger.error(ex.getMessage());
				}
				List<JcsjDto> sjqfs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
				sjxxDto.getZqs().put("xsxzcssqszqs", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
				sjxxDto.getZqs().put("xsxzcssqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				String key = "jsrq_getXsxzcssqszqs_"+sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					xsxzcssqsList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					JcsjDto jcsjDto1 = new JcsjDto();
					jcsjDto1.setCsid("第三方实验室");
					jcsjDto1.setCsmc("第三方实验室");
					sjqfs.add(jcsjDto1);
					JcsjDto jcsjDto2 = new JcsjDto();
					jcsjDto2.setCsid("直销");
					jcsjDto2.setCsmc("直销");
					sjqfs.add(jcsjDto2);
					JcsjDto jcsjDto3 = new JcsjDto();
					jcsjDto3.setCsid("CSO");
					jcsjDto3.setCsmc("CSO");
					sjqfs.add(jcsjDto3);
					if(sjqfs!=null&&sjqfs.size()>0){
						sjxxDto.setNewflg("qs");
						List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto);
						for(int i=0;i<rqs.size();i++){
							for(JcsjDto jcsjDto:sjqfs){
								boolean isFind=false;
								for(Map<String, String> map_t:sjqfcsszb){
									if(rqs.get(i).equals(map_t.get("rq"))&&jcsjDto.getCsid().equals(map_t.get("sjqf"))){
										map_t.put("sjqfmc",jcsjDto.getCsmc());
										if(i==0){
											map_t.put("bl","0");
										}else{
											var num="0";
											for(Map<String, String> stringMap:xsxzcssqsList){
												if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
													num=stringMap.get("num");
												}
											}
											BigDecimal bigDecimal=new BigDecimal(num);
											BigDecimal bigDecimal1=new BigDecimal(map_t.get("num"));
											map_t.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
										}
										xsxzcssqsList.add(map_t);
										isFind=true;
										break;
									}
								}
								if(!isFind){
									Map<String, String> newMap=new HashMap<>();
									newMap.put("rq",rqs.get(i));
									newMap.put("num","0");
									newMap.put("sjqfmc",jcsjDto.getCsmc());
									newMap.put("sjqf",jcsjDto.getCsid());
									if(i==0){
										newMap.put("bl","0");
									}else{
										var num="0";
										for(Map<String, String> stringMap:xsxzcssqsList){
											if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
												num=stringMap.get("num");
											}
										}
										BigDecimal bigDecimal=new BigDecimal(num);
										BigDecimal bigDecimal1=new BigDecimal("0");
										newMap.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
									}
									xsxzcssqsList.add(newMap);
								}
							}
						}
					}
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(xsxzcssqsList));
				}
				map.put("xsxzcssqs", xsxzcssqsList);
			}
			else if("getXsxzcssqsMon".equals(method)){
				//销售性质测试数趋势
				List<Map<String, String>> xsxzcssqsList=new ArrayList<>();
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
				sjxxDto.setTj("mon");
				List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
				List<JcsjDto> sjqfs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
				sjxxDto.getZqs().put("xsxzcssqszqs", sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend());
				sjxxDto.getZqs().put("xsxzcssqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				String key = "jsrq_getXsxzcssqszqs_"+sjxxDto.getJsrqMstart() + "~" + sjxxDto.getJsrqMend()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					xsxzcssqsList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					JcsjDto jcsjDto1 = new JcsjDto();
					jcsjDto1.setCsid("第三方实验室");
					jcsjDto1.setCsmc("第三方实验室");
					sjqfs.add(jcsjDto1);
					JcsjDto jcsjDto2 = new JcsjDto();
					jcsjDto2.setCsid("直销");
					jcsjDto2.setCsmc("直销");
					sjqfs.add(jcsjDto2);
					JcsjDto jcsjDto3 = new JcsjDto();
					jcsjDto3.setCsid("CSO");
					jcsjDto3.setCsmc("CSO");
					sjqfs.add(jcsjDto3);
					if(sjqfs!=null&&sjqfs.size()>0){
						sjxxDto.setNewflg("qs");
						List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto);
						for(int i=0;i<rqs.size();i++){
							for(JcsjDto jcsjDto:sjqfs){
								boolean isFind=false;
								for(Map<String, String> map_t:sjqfcsszb){
									if(rqs.get(i).equals(map_t.get("rq"))&&jcsjDto.getCsid().equals(map_t.get("sjqf"))){
										map_t.put("sjqfmc",jcsjDto.getCsmc());
										if(i==0){
											map_t.put("bl","0");
										}else{
											var num="0";
											for(Map<String, String> stringMap:xsxzcssqsList){
												if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
													num=stringMap.get("num");
												}
											}
											BigDecimal bigDecimal=new BigDecimal(num);
											BigDecimal bigDecimal1=new BigDecimal(map_t.get("num"));
											map_t.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
										}
										xsxzcssqsList.add(map_t);
										isFind=true;
										break;
									}
								}
								if(!isFind){
									Map<String, String> newMap=new HashMap<>();
									newMap.put("rq",rqs.get(i));
									newMap.put("num","0");
									newMap.put("sjqfmc",jcsjDto.getCsmc());
									newMap.put("sjqf",jcsjDto.getCsid());
									if(i==0){
										newMap.put("bl","0");
									}else{
										var num="0";
										for(Map<String, String> stringMap:xsxzcssqsList){
											if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
												num=stringMap.get("num");
											}
										}
										BigDecimal bigDecimal=new BigDecimal(num);
										BigDecimal bigDecimal1=new BigDecimal("0");
										newMap.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
									}
									xsxzcssqsList.add(newMap);
								}
							}
						}
					}
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(xsxzcssqsList));
				}
				map.put("xsxzcssqs", xsxzcssqsList);
			}
			else if("getXsxzcssqsYear".equals(method)){
				//销售性质测试数趋势
				List<Map<String, String>> xsxzcssqsList=new ArrayList<>();
				int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
				setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
				sjxxDto.setTj("year");
				List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
				List<JcsjDto> sjqfs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
				sjxxDto.getZqs().put("xsxzcssqszqs", sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend());
				sjxxDto.getZqs().put("xsxzcssqsyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
				String key = "jsrq_getXsxzcssqszqs_"+sjxxDto.getJsrqYstart() + "~" + sjxxDto.getJsrqYend()+"_"+StringUtil.join(sjxxDto.getYxxs(),",")+"_"+StringUtil.join(sjxxDto.getPtgss(),",")+"_"+sjxxDto.getYhid();
				Map<String, Object> objMap = redisUtil.hgetStatistics(key);
				if ( "weekLeadStatis".equals(objMap.get("key")) ){
					xsxzcssqsList = (List<Map<String, String>>) JSONArray.parse((String) objMap.get("obj"));
				}else {
					JcsjDto jcsjDto1 = new JcsjDto();
					jcsjDto1.setCsid("第三方实验室");
					jcsjDto1.setCsmc("第三方实验室");
					sjqfs.add(jcsjDto1);
					JcsjDto jcsjDto2 = new JcsjDto();
					jcsjDto2.setCsid("直销");
					jcsjDto2.setCsmc("直销");
					sjqfs.add(jcsjDto2);
					JcsjDto jcsjDto3 = new JcsjDto();
					jcsjDto3.setCsid("CSO");
					jcsjDto3.setCsmc("CSO");
					sjqfs.add(jcsjDto3);
					if(sjqfs!=null&&sjqfs.size()>0){
						sjxxDto.setNewflg("qs");
						List<Map<String, String>> sjqfcsszb = sjxxStatisticService.getSjqfcsszb(sjxxDto);
						for(int i=0;i<rqs.size();i++){
							for(JcsjDto jcsjDto:sjqfs){
								boolean isFind=false;
								for(Map<String, String> map_t:sjqfcsszb){
									if(rqs.get(i).equals(map_t.get("rq"))&&jcsjDto.getCsid().equals(map_t.get("sjqf"))){
										map_t.put("sjqfmc",jcsjDto.getCsmc());
										if(i==0){
											map_t.put("bl","0");
										}else{
											var num="0";
											for(Map<String, String> stringMap:xsxzcssqsList){
												if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
													num=stringMap.get("num");
												}
											}
											BigDecimal bigDecimal=new BigDecimal(num);
											BigDecimal bigDecimal1=new BigDecimal(map_t.get("num"));
											map_t.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
										}
										xsxzcssqsList.add(map_t);
										isFind=true;
										break;
									}
								}
								if(!isFind){
									Map<String, String> newMap=new HashMap<>();
									newMap.put("rq",rqs.get(i));
									newMap.put("num","0");
									newMap.put("sjqfmc",jcsjDto.getCsmc());
									newMap.put("sjqf",jcsjDto.getCsid());
									if(i==0){
										newMap.put("bl","0");
									}else{
										var num="0";
										for(Map<String, String> stringMap:xsxzcssqsList){
											if(jcsjDto.getCsid().equals(stringMap.get("sjqf"))){
												num=stringMap.get("num");
											}
										}
										BigDecimal bigDecimal=new BigDecimal(num);
										BigDecimal bigDecimal1=new BigDecimal("0");
										newMap.put("bl",String.valueOf((bigDecimal1.subtract(bigDecimal)).multiply(new BigDecimal("100")).divide(bigDecimal.compareTo(BigDecimal.ZERO)!=0?bigDecimal:new BigDecimal("1"),BigDecimal.ROUND_HALF_UP)));
									}
									xsxzcssqsList.add(newMap);
								}
							}
						}
					}
					redisUtil.hset(StringUtil.isNotBlank((String)objMap.get("key"))?(String)objMap.get("key"):"weekLeadStatis", key,JSON.toJSONString(xsxzcssqsList));
				}
				map.put("xsxzcssqs", xsxzcssqsList);
			}
			map.put("searchData", sjxxDto);
		}else{
			logger.error("getSjxxStatisByTjAndJsrq--不允许用户id和平台归属都为空！！");
			return map;
		}
		//若过期时间为-1，重新设置过期时间
		if (redisUtil.getExpire("weekLeadStatis")==-1){
			redisUtil.hset("weekLeadStatis","ExpirationTime","30min",1800);
		}
		return map;
	}

	/**
	 * 通过用户id，查询伙伴权限
	 * 
	 * @return
	 */
	@RequestMapping("/ststictis/getWeeklyPartner_hbqx")
	@ResponseBody
	public Map<String, Object> getWeeklyPartner_hbqx(HttpServletRequest req,SjxxDto sjxxDto, SjhbxxDto sjhbxxDto){
		Map<String, Object> map = new HashMap<>();
		List<HbqxDto> sjhbxxDtos = hbqxService.getHbxxByYhid(sjhbxxDto.getXtyhid());
		map.put("sjhbxxDtos", sjhbxxDtos);
		sjxxDto.setHbfl(sjhbxxDto.getFl());
		sjxxDto.setHbzfl(sjhbxxDto.getZfl());
		map.put("sjxxDto", sjxxDto);
		return map;
	}

	/**
	 * 统计合作伙伴页面
	 * 
	 * @return
	 */
	@RequestMapping("/ststictis/WeeklyPartnerPage")
	public ModelAndView WeeklyPartnerPage(SjxxDto sjxxDto, SjhbxxDto sjhbxxDto,HttpServletRequest request){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistic_wxyh/statistic_weekly_partner");
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
		sjxxDto.setYhid(sjhbxxDto.getYhid());

		//查询合作伙伴信息
		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getSjhbDtoListByHbqx(sjhbxxDto);
		List<Map<String, Object>> sjhbxxDtodb = sjhbxxService.getnotNullDb(sjxxDto);
		
		String dbs = "";
		if (sjhbxxDtos != null && sjhbxxDtodb.size() > 0) {
			for (Map<String, Object> m : sjhbxxDtodb) {
				dbs = dbs + m.get("hbmc") + ",";
			}
			if (StringUtil.isNotBlank(dbs))
				dbs = dbs.substring(0, dbs.length() - 1);
		}
		mav.addObject("dbhbs", dbs);

		mav.addObject("sjhbxxDtos", sjhbxxDtodb);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("bj", request.getParameter("bj"));
		return mav;
	}
	/**
	 * 统计合作伙伴页面(接收日期)
	 *
	 * @return
	 */
	@RequestMapping("/ststictis/WeeklyPartnerPageByJsrq")
	public ModelAndView WeeklyPartnerPageByJsrq(SjxxDto sjxxDto, SjhbxxDto sjhbxxDto,HttpServletRequest request){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistic_wxyh/statistic_weekly_partner_jsrq");
		String method = request.getParameter("method");
		mav.addObject("method",method);
		if (redisUtil.getExpire("weekLeadStatis")==-2){
			redisUtil.hset("weekLeadStatis","ExpirationTime","30min",1800);
		}
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
		String jsrqstart = sjxxDto.getJsrqstart();
		String jsrqend = sjxxDto.getJsrqend();
		sjxxDto.setZq("7");
		sjxxDto.setHbfl(sjhbxxDto.getFl());
		sjxxDto.setHbzfl(sjhbxxDto.getZfl());
		if(StringUtil.isBlank(sjhbxxDto.getXtyhid()) && StringUtil.isNotBlank(sjhbxxDto.getYhid())) {
			sjhbxxDto.setXtyhid(sjhbxxDto.getYhid());
		}
		sjxxDto.setYhid(sjhbxxDto.getYhid());

		String zqString = "0";
		if(StringUtil.isNotBlank(sjxxDto.getZq())) {
			zqString = sjxxDto.getZq().split(",")[0];
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        sjxxDto.setJsrqstart(dateFormat.format(DateUtils.getDate(DateUtils.parseDate("yyyy-MM-dd", sjxxDto.getJsrqend()), - Integer.parseInt(zqString))));
		//查询合作伙伴信息
		List<SjhbxxDto> sjhbxxDtos;
		if(StringUtil.isNotBlank(sjhbxxDto.getXtyhid())){
			sjhbxxDtos = sjhbxxService.getSjhbDtoListByHbqx(sjhbxxDto);
		}else{
			sjhbxxDtos = sjhbxxService.getListFromXxdy(sjhbxxDto);
		}
		List<Map<String, Object>> sjhbxxDtodb;
		if(StringUtil.isNotBlank(sjhbxxDto.getXtyhid())){
			sjhbxxDtodb=sjhbxxService.getnotNullDbByJsrq(sjxxDto);
		}else{
			sjhbxxDtodb=sjhbxxService.getNotNullDbFromXxdy(sjxxDto);
		}
		String dbs = "";
		if (sjhbxxDtos != null && sjhbxxDtodb.size() > 0) {
			for (Map<String, Object> m : sjhbxxDtodb) {
				dbs = dbs + m.get("hbmc") + ",";
			}
			if (StringUtil.isNotBlank(dbs))
				dbs = dbs.substring(0, dbs.length() - 1);
		}
		mav.addObject("dbhbs", dbs);

		mav.addObject("sjhbxxDtos", sjhbxxDtodb);
		sjxxDto.setJsrqstart(jsrqstart);
		sjxxDto.setJsrqend(jsrqend);
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("bj", request.getParameter("bj"));
		mav.addObject("ptgss",StringUtil.join(sjxxDto.getPtgss(),","));
		mav.addObject("t_sign",request.getParameter("sign"));
		return mav;
	}
	
	/**
	 * 查询汇报领导的伙伴统计
	 * 
	 * @return
	 */
	@RequestMapping("/ststictis/getPartnerStatisWeekly")
	@ResponseBody
	public Map<String, Object> getPartnerStatisWeekly(SjxxDto sjxxDto)
	{
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
		Map<String, Object> map = new HashMap<>();
		if(StringUtil.isNotBlank(sjxxDto.getYhid())) {
            List<HbqxDto> hbqxList=hbqxService.getHbxxByYhid(sjxxDto.getYhid());
			if(hbqxList!=null&& hbqxList.size()>0) {
				
				  List<String> strList= new ArrayList<>(); for (int i = 0; i <
				  hbqxList.size(); i++){ strList.add(hbqxList.get(i).getHbmc()); }
				  sjxxDto.setDbs(strList);
				 
				String[] hbmcqxList = sjxxDto.getDbhbs().split(",");
				List<String> strmcList= new ArrayList<>();
				strmcList.addAll(Arrays.asList(hbmcqxList));
				sjxxDto.setDbmcs(strmcList);
				//sjxxDto.setDbs(strmcList);

				if (StringUtil.isBlank(sjxxDto.getJsrqstart()))
				{
					int dayOfWeek = DateUtils.getWeek(new Date());
					if (dayOfWeek <= 2)
					{
						sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
						sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
						sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
						sjxxDto.setBgrqend(sjxxDto.getJsrqend());
					} else
					{
						sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
						sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
						sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
						sjxxDto.setBgrqend(sjxxDto.getJsrqend());
					}
				}else if(StringUtil.isBlank(sjxxDto.getBgrqstart())){
					sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
					sjxxDto.setBgrqend(sjxxDto.getJsrqend());
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
		}
		return map;
	}
	/**
	 * 查询汇报领导的伙伴统计(接收日期)
	 *
	 * @return
	 */
	@RequestMapping("/ststictis/getPartnerStatisWeeklyByJsrq")
	@ResponseBody
	public Map<String, Object> getPartnerStatisWeeklyByJsrq(SjxxDto sjxxDto,String method)
	{
		// 如果未设定接收起始日期则自动根据规则设定，星期二之前设定为上一周，星期三以后设定为本周
		Map<String, Object> map = new HashMap<>();
		List<JcsjDto> dyxxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.XXDY_TYPE.getCode());
		for (JcsjDto dyxx : dyxxs) {
			if ("XMFL".equals(dyxx.getCsdm())){
				//项目分类
				sjxxDto.setYwlx(dyxx.getCsid());
			}
		}
		if (ArrayUtils.isEmpty(sjxxDto.getYxxs())){
			XxdyDto xxdyDto = new XxdyDto();
			xxdyDto.setKzcs6("1");
			xxdyDto.setDylxcsdm("XMFL");
			List<XxdyDto> dtoList = xxdyService.getYxxMsg(xxdyDto);
			if (!CollectionUtils.isEmpty(dtoList)){
				List<String> yxxs = new ArrayList<>();
				for (XxdyDto dto : dtoList) {
					yxxs.add(dto.getYxx());
				}
				sjxxDto.setYxxs(yxxs.toArray(new String[0]));
			}
		}
		if(StringUtil.isNotBlank(sjxxDto.getYhid())) {
            List<HbqxDto> hbqxList=hbqxService.getHbxxByYhid(sjxxDto.getYhid());
			if(hbqxList!=null&& hbqxList.size()>0) {
				  List<String> strList= new ArrayList<>();
				  for (int i = 0; i <hbqxList.size(); i++){ strList.add(hbqxList.get(i).getHbmc()); }
				  sjxxDto.setDbs(strList);
			}
		}else{
			String[] hbmcqxList = sjxxDto.getDbhbs().split(",");
			List<String> strmcList= new ArrayList<>();
			strmcList.addAll(Arrays.asList(hbmcqxList));
			sjxxDto.setDbs(strmcList);
		}
		String[] hbmcqxList = sjxxDto.getDbhbs().split(",");
		List<String> strmcList= new ArrayList<>();
		strmcList.addAll(Arrays.asList(hbmcqxList));
		sjxxDto.setDbmcs(strmcList);
		//sjxxDto.setDbs(strmcList);

		if (StringUtil.isBlank(sjxxDto.getJsrqstart()))
		{
			int dayOfWeek = DateUtils.getWeek(new Date());
			if (dayOfWeek <= 2)
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek - 7), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek-1), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			} else
			{
				sjxxDto.setJsrqstart(DateUtils.format(DateUtils.getDate(new Date(), -dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setJsrqend(DateUtils.format(DateUtils.getDate(new Date(), 6 - dayOfWeek), "yyyy-MM-dd"));
				sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
				sjxxDto.setBgrqend(sjxxDto.getJsrqend());
			}
		}else if(StringUtil.isBlank(sjxxDto.getBgrqstart())){
			sjxxDto.setBgrqstart(sjxxDto.getJsrqstart());
			sjxxDto.setBgrqend(sjxxDto.getJsrqend());
		}
		if (StringUtil.isBlank(sjxxDto.getZq())) {
			sjxxDto.setZq("7");
		}
		//按伙伴查询
		String zqString = "0";
		if(StringUtil.isNotBlank(sjxxDto.getZq())) {
			zqString = sjxxDto.getZq().split(",")[0];
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		if ("getFlByWeek".equals(method)) {
			//周 getFlByWeek
//					setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
//					sjxxDto.getZqs().put("hbztj",sjxxDto.getJsrqstart()+"~"+sjxxDto.getJsrqend());
//					sjxxDto.setTj("day");
//					fllist = SjxxStatisticService.getWeeklyByTjFlAndJsrq(sjxxDto);
			setDateByWeek(sjxxDto,sjxxDto.getJsrqend(),-Integer.parseInt(sjxxDto.getZq()));
			sjxxDto.setTj("week");
			if(sjxxDto.getDbmcs()!=null&&sjxxDto.getDbmcs().size()>0) {
				sjxxDto.setDbs(sjxxDto.getDbmcs());
			}
			List<String> rqs = new ArrayList<>();
			// 设置日期
			try
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));

				int zq = Integer.parseInt(sjxxDto.getZq());
				for (int i = 0; i < zq; i++)
				{
					calendar.add(Calendar.DATE, -7);
					rqs.add(sdf.format(calendar.getTime()));
				}
			} catch (Exception ex){
				logger.error(ex.getMessage());
			}
			List<Map<String, String>> statistics=SjxxStatisticService.getStatisticsByTjFlAndJsrq(sjxxDto);
			List<Map<String, String>> hbztj=new ArrayList<>();
			if(statistics!=null) {
				if(strmcList!=null&&strmcList.size()>0){
					for(String db:strmcList){
						List<Map<String, String>> newList=new ArrayList<>();
						for(String time:rqs){
							Map<String, String> newMap_one=new HashMap<>();
							newMap_one.put("rq",time);
							newMap_one.put("sfsf","1");
							newMap_one.put("db",db);
							newMap_one.put("num","0");
							for(Map<String, String> map_t:statistics){
								if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
									newMap_one.put("num",map_t.get("num"));
									break;
								}
							}
							newList.add(newMap_one);
							Map<String, String> newMap_two=new HashMap<>();
							newMap_two.put("rq",time);
							newMap_two.put("sfsf","0");
							newMap_two.put("db",db);
							newMap_two.put("num","0");
							for(Map<String, String> map_t:statistics){
								if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
									newMap_two.put("num",map_t.get("num"));
									break;
								}
							}
							newList.add(newMap_two);
						}
						map.put("hbtj_"+db,newList);
						sjxxDto.getZqs().put("hbtj_"+db+"yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
						hbztj.addAll(newList);
					}
				}
			}
			map.put("hbztj",hbztj);
			sjxxDto.getZqs().put("hbztjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		}else if ("getFlByMon".equals(method)){
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByMonth(sjxxDto,sjxxDto.getJsrqend(),zq+1);
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqMstart()+"~"+ sjxxDto.getJsrqMend());
			sjxxDto.setTj("mon");
			List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
			List<Map<String, String>> statistics=SjxxStatisticService.getStatisticsByTjFlAndJsrq(sjxxDto);
			List<Map<String, String>> hbztj=new ArrayList<>();
			if(statistics!=null) {
				if(strmcList!=null&&strmcList.size()>0){
					for(String db:strmcList){
						List<Map<String, String>> newList=new ArrayList<>();
						for(String time:rqs){
							Map<String, String> newMap_one=new HashMap<>();
							newMap_one.put("rq",time);
							newMap_one.put("sfsf","1");
							newMap_one.put("db",db);
							newMap_one.put("num","0");
							for(Map<String, String> map_t:statistics){
								if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
									newMap_one.put("num",map_t.get("num"));
									break;
								}
							}
							newList.add(newMap_one);
							Map<String, String> newMap_two=new HashMap<>();
							newMap_two.put("rq",time);
							newMap_two.put("sfsf","0");
							newMap_two.put("db",db);
							newMap_two.put("num","0");
							for(Map<String, String> map_t:statistics){
								if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
									newMap_two.put("num",map_t.get("num"));
									break;
								}
							}
							newList.add(newMap_two);
						}
						map.put("hbtj_"+db,newList);
						sjxxDto.getZqs().put("hbtj_"+db+"yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
						hbztj.addAll(newList);
					}
				}
			}
			map.put("hbztj",hbztj);
			sjxxDto.getZqs().put("hbztjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		}else if ("getFlByYear".equals(method)) {
			int zq = (Integer.parseInt(sjxxDto.getZq()) > 0) ? -Integer.parseInt(sjxxDto.getZq()) : Integer.parseInt(sjxxDto.getZq());
			setDateByYear(sjxxDto,sjxxDto.getJsrqend(),zq+1,false);
			sjxxDto.setTj("year");
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqYstart()+"~"+ sjxxDto.getJsrqYend());
			List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
			List<Map<String, String>> statistics=SjxxStatisticService.getStatisticsByTjFlAndJsrq(sjxxDto);
			List<Map<String, String>> hbztj=new ArrayList<>();
			if(statistics!=null) {
				if(strmcList!=null&&strmcList.size()>0){
					for(String db:strmcList){
						List<Map<String, String>> newList=new ArrayList<>();
						for(String time:rqs){
							Map<String, String> newMap_one=new HashMap<>();
							newMap_one.put("rq",time);
							newMap_one.put("sfsf","1");
							newMap_one.put("db",db);
							newMap_one.put("num","0");
							for(Map<String, String> map_t:statistics){
								if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
									newMap_one.put("num",map_t.get("num"));
									break;
								}
							}
							newList.add(newMap_one);
							Map<String, String> newMap_two=new HashMap<>();
							newMap_two.put("rq",time);
							newMap_two.put("sfsf","0");
							newMap_two.put("db",db);
							newMap_two.put("num","0");
							for(Map<String, String> map_t:statistics){
								if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
									newMap_two.put("num",map_t.get("num"));
									break;
								}
							}
							newList.add(newMap_two);
						}
						map.put("hbtj_"+db,newList);
						sjxxDto.getZqs().put("hbtj_"+db+"yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
						hbztj.addAll(newList);
					}
				}
			}
			map.put("hbztj",hbztj);
			sjxxDto.getZqs().put("hbztjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		} else {
			sjxxDto.setJsrqstart(dateFormat.format(DateUtils.getDate(DateUtils.parseDate("yyyy-MM-dd",sjxxDto.getJsrqend()), - Integer.parseInt(zqString)+1)));
			sjxxDto.getZqs().put("hbztj", sjxxDto.getJsrqstart()+"~"+ sjxxDto.getJsrqend());
			sjxxDto.setTj("day");
			List<String> rqs = SjxxStatisticService.getRqsByStartAndEnd(sjxxDto);
			List<Map<String, String>> statistics=SjxxStatisticService.getStatisticsByTjFlAndJsrq(sjxxDto);
			List<Map<String, String>> hbztj=new ArrayList<>();
			if(statistics!=null) {
				if(strmcList!=null&&strmcList.size()>0){
					for(String db:strmcList){
						List<Map<String, String>> newList=new ArrayList<>();
						for(String time:rqs){
							Map<String, String> newMap_one=new HashMap<>();
							newMap_one.put("rq",time);
							newMap_one.put("sfsf","1");
							newMap_one.put("db",db);
							newMap_one.put("num","0");
							for(Map<String, String> map_t:statistics){
								if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"1".equals(map_t.get("sfsf"))){
									newMap_one.put("num",map_t.get("num"));
									break;
								}
							}
							newList.add(newMap_one);
							Map<String, String> newMap_two=new HashMap<>();
							newMap_two.put("rq",time);
							newMap_two.put("sfsf","0");
							newMap_two.put("db",db);
							newMap_two.put("num","0");
							for(Map<String, String> map_t:statistics){
								if(db.equals(map_t.get("db"))&&time.equals(map_t.get("rq"))&&"0".equals(map_t.get("sfsf"))){
									newMap_two.put("num",map_t.get("num"));
									break;
								}
							}
							newList.add(newMap_two);
						}
						map.put("hbtj_"+db,newList);
						sjxxDto.getZqs().put("hbtj_"+db+"yxxs", StringUtil.join(sjxxDto.getYxxs(),","));
						hbztj.addAll(newList);
					}
				}
			}
			map.put("hbztj",hbztj);
			sjxxDto.getZqs().put("hbztjyxxs", StringUtil.join(sjxxDto.getYxxs(),","));
		}
		map.put("searchData", sjxxDto);
		return map;
	}

	/**
	 * 跳转质控统计页面
	 * @return
	 */
	@RequestMapping("/statistics/qualityStstictis")
	public ModelAndView qualityStstictis(SjxxDto sjxxDto){
		ModelAndView mav = new ModelAndView("wechat/statistics/quality_statistics");
		//获取区域信息
		UserDto userDto = new UserDto();
		userDto.setJsmc("大区经理");
		List<UserDto> users = sjxxService.queryYhByJsmc(userDto);
		for (UserDto userDto_t : users) {
			if (userDto_t.getGwmc() != null && userDto_t.getGwmc().contains("（") && userDto_t.getGwmc().contains("）")){
				String gwmc = userDto_t.getGwmc().substring(userDto_t.getGwmc().indexOf("（")+1,userDto_t.getGwmc().indexOf("）"));
				userDto_t.setGwmc(gwmc);
			}else {
				userDto_t.setGwmc("");
			}
		}
		//存入一个"全国"
		userDto.setGwmc("全国");
		userDto.setYhm("ALL");
		userDto.setYhid("ALL");
		users.add(userDto);
		//对区域信息进行遍历排序
		List<UserDto> userDtos=new ArrayList<>();
		String sortName="全国,华东一区,华东二区,华北一区,华北二区,华南,西南,西北,第三方";
		String[] sort = sortName.split(",");
		for(String s:sort){
			for (UserDto userDto_t : users) {
				if(StringUtil.isNotBlank(userDto_t.getGwmc())){
					if(userDto_t.getGwmc().indexOf(s)!=-1){
						userDtos.add(userDto_t);
						users.remove(userDto_t);
						break;
					}
				}else{
					if(userDto_t.getZsxm().indexOf(s)!=-1){
						userDtos.add(userDto_t);
						users.remove(userDto_t);
						break;
					}
				}
			}
		}
		userDtos.addAll(users);
		//获取省份信息
		List<JcsjDto> sflist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PROVINCE.getCode());
		//获取项目信息
		//Q项目，T项目，ResFirst项目，Onco项目（去除去人源项目）
		List<String> yxxList = new ArrayList<>();
		XxdyDto xxdyDto = new XxdyDto();
		xxdyDto.setKzcs6("1");
		xxdyDto.setDylxcsdm("XMFL");
		List<XxdyDto> dtoList = xxdyService.getYxxMsg(xxdyDto);//
		if (!CollectionUtils.isEmpty(dtoList)){
			for (XxdyDto dto : dtoList) {
				yxxList.add(dto.getYxx());
			}
		}
		yxxList.add("ONCO-SEQ");
		yxxList.add("Resfirst");
		XxdyDto xxdyDto_t = new XxdyDto();
		xxdyDto_t.setYxxs(yxxList);
		xxdyDto_t.setDylx("XMFL");
		List<JcsjDto> xmlist = xxdyService.getJcsjListByXxdy(xxdyDto_t);
		for (int i = xmlist.size() - 1; i >= 0; i--) {
			if (xmlist.get(i) == null || StringUtil.isBlank(xmlist.get(i).getCsmc()) || xmlist.get(i).getCsmc().contains("去人源")) {
				xmlist.remove(i);
			}
		}
		//送检区分
		List<JcsjDto> sjqflist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
		//伙伴分类
		List<JcsjDto> hbfllist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.CLASSIFY.getCode());
		//获取过去10年
		List<String> yearList = new ArrayList<>();
//		SimpleDateFormat format =new SimpleDateFormat("yyyy");
		Calendar date = Calendar.getInstance();
		String year=String.valueOf(date.get(Calendar.YEAR));
		for(int i = 2019; i<=Integer.parseInt(year); i++){
			yearList.add(String.valueOf(i));
		}
		//伙伴分类
		List<JcsjDto> hbfls = new ArrayList<>();
		List<String> list = new ArrayList<>();
		for (JcsjDto jcsjDto : hbfllist) {
			if ("001".equals(jcsjDto.getCsdm())||"002".equals(jcsjDto.getCsdm())||"003".equals(jcsjDto.getCsdm())||"004".equals(jcsjDto.getCsdm())){
				if (!list.contains(jcsjDto.getCskz4())){
					list.add(jcsjDto.getCskz4());
					hbfls.add(jcsjDto);
				} else {
					for (JcsjDto hbfl : hbfls) {
						if (jcsjDto.getCskz4().equals(hbfl.getCskz4())){
							hbfl.setCsid(hbfl.getCsid()+','+jcsjDto.getCsid());
						}
					}
				}
			}
		}
		mav.addObject("hbfls",hbfls);//渠道收费免费统计伙伴
		mav.addObject("yearList",yearList);//年份
		mav.addObject("sjqflist",sjqflist);//送检区分
		mav.addObject("hbfllist",hbfllist);//伙伴分类
		mav.addObject("provinceList", sflist);//省份
		mav.addObject("provinceListStr", JSON.toJSONString(sflist));//省份
		mav.addObject("detectList",xmlist);//检测项目
		mav.addObject("sjxxDto", sjxxDto);//注入高级筛选的一些list
		mav.addObject("userDtos", userDtos);
		mav.addObject("detectListStr",JSON.toJSONString(xmlist));//检测项目
		mav.addObject("usersStr", JSON.toJSONString(userDtos));
		mav.addObject("flag", null);//注入高级筛选的一些list
		return mav;
	}
	/**
	 * 获取质控统计参数
	 * @return
	 */
	@RequestMapping("/statistics/qualityStstictisParam")
	@ResponseBody
	public Map<String,Object> qualityStstictisParam(){
		Map<String,Object> map = new HashMap<>();
		//获取区域信息
		UserDto userDto = new UserDto();
		userDto.setJsmc("大区经理");
		List<UserDto> users = sjxxService.queryYhByJsmc(userDto);
		for (UserDto userDto_t : users) {
			if (userDto_t.getGwmc() != null && userDto_t.getGwmc().contains("（") && userDto_t.getGwmc().contains("）")){
				String gwmc = userDto_t.getGwmc().substring(userDto_t.getGwmc().indexOf("（")+1,userDto_t.getGwmc().indexOf("）"));
				userDto_t.setGwmc(gwmc);
			}else {
				userDto_t.setGwmc("");
			}
		}
		//存入一个"全国"
		userDto.setGwmc("全国");
		userDto.setYhm("ALL");
		userDto.setYhid("ALL");
		users.add(userDto);
		//对区域信息进行遍历排序
		List<UserDto> userDtos=new ArrayList<>();
		String sortName="全国,华东一区,华东二区,华北一区,华北二区,华南,西南,西北,第三方";
		String[] sort = sortName.split(",");
		for(String s:sort){
			for (UserDto userDto_t : users) {
				if(userDto_t.getGwmc().indexOf(s)!=-1){
					userDtos.add(userDto_t);
					users.remove(userDto_t);
					break;
				}
			}
		}
		userDtos.addAll(users);
		//获取省份信息
		List<JcsjDto> sflist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PROVINCE.getCode());
		//获取项目信息
		List<JcsjDto> xmlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> xmlist_t=new ArrayList<>();
		for (JcsjDto jcsjDto:xmlist){
			if ("001".equals(jcsjDto.getCsdm())||"002".equals(jcsjDto.getCsdm())||"003".equals(jcsjDto.getCsdm())||"005".equals(jcsjDto.getCsdm())||"006".equals(jcsjDto.getCsdm())||"007".equals(jcsjDto.getCsdm())||"030".equals(jcsjDto.getCsdm())||"031".equals(jcsjDto.getCsdm())||"032".equals(jcsjDto.getCsdm())
					||"033".equals(jcsjDto.getCsdm())||"045".equals(jcsjDto.getCsdm())||"046".equals(jcsjDto.getCsdm())
					||"047".equals(jcsjDto.getCsdm())||"IMP_REPORT_C_TEMEPLATE".equals(jcsjDto.getCskz3())
					||"051".equals(jcsjDto.getCsdm())||"052".equals(jcsjDto.getCsdm())
					||"053".equals(jcsjDto.getCsdm())||"054".equals(jcsjDto.getCsdm())
			)
			{
				xmlist_t.add(jcsjDto);
			}
		}
		//送检区分
		List<JcsjDto> sjqflist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
		//伙伴分类
		List<JcsjDto> hbfllist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.CLASSIFY.getCode());
		//获取过去10年
		List<String> yearList = new ArrayList<>();
		SimpleDateFormat format =new SimpleDateFormat("yyyy");
		for(int i=0;i>-10;i--){
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.YEAR,i);
			Date y=calendar.getTime();
			yearList.add(format.format(y));
		}
		//伙伴分类
		List<JcsjDto> hbfls = new ArrayList<>();
		List<String> list = new ArrayList<>();
		for (JcsjDto jcsjDto : hbfllist) {
			if ("001".equals(jcsjDto.getCsdm())||"002".equals(jcsjDto.getCsdm())||"003".equals(jcsjDto.getCsdm())||"004".equals(jcsjDto.getCsdm())){
				if (!list.contains(jcsjDto.getCskz4())){
					list.add(jcsjDto.getCskz4());
					hbfls.add(jcsjDto);
				} else {
					for (JcsjDto hbfl : hbfls) {
						if (jcsjDto.getCskz4().equals(hbfl.getCskz4())){
							hbfl.setCsid(hbfl.getCsid()+','+jcsjDto.getCsid());
						}
					}
				}
			}
		}
		map.put("hbfls",hbfls);//渠道收费免费统计伙伴
		map.put("yearList",yearList);//年份
		map.put("sjqflist",sjqflist);//送检区分
		map.put("hbfllist",hbfllist);//伙伴分类
		map.put("provinceList", sflist);//省份
		map.put("detectList",xmlist_t);//检测项目
		map.put("userDtos", userDtos);
		map.put("flag", null);//注入高级筛选的一些list
		return map;
	}

	/**
	 * 获取质控营销统计数据
	 * @param map
	 * @return
	 */
	@RequestMapping("/statistics/getMarketStstictisData")
	@ResponseBody
	public Map<String,Object> getMarketStstictisData(Map<String,Object> map,HttpServletRequest req){
		//service获取营销统计数据
		List<String> yf= StringUtil.isNotBlank(req.getParameter("yf"))?Arrays.asList(req.getParameter("yf").split(",")):new ArrayList<>();
		List<String> jcxm=StringUtil.isNotBlank(req.getParameter("jcxm"))?Arrays.asList(req.getParameter("jcxm").split(",")):new ArrayList<>();
		List<String> nf=StringUtil.isNotBlank(req.getParameter("nf"))?Arrays.asList(req.getParameter("nf").split(",")):new ArrayList<>();
		List<String> hbflid =StringUtil.isNotBlank(req.getParameter("hbflid"))?Arrays.asList(req.getParameter("hbflid").split(",")):new ArrayList<>();
		List<String> sf=StringUtil.isNotBlank(req.getParameter("sf"))?Arrays.asList(req.getParameter("sf").split(",")):new ArrayList<>();
		map.put("flag",req.getParameter("flag")==null?"":req.getParameter("flag"));
		map.put("moneyflag",req.getParameter("moneyflag")==null?"":req.getParameter("moneyflag"));
		map.put("sjqf",req.getParameter("sjqf"));
		map.put("hbflid",hbflid);
		map.put("yf",yf);
		map.put("jcxm",jcxm);
		map.put("nf",nf);
		map.put("sf",sf);
		String qy = req.getParameter("qy");
		if (qy.indexOf("ALL")==-1){
			List<String> qys= StringUtil.isNotBlank(qy)?Arrays.asList(qy.split(",")):new ArrayList<>();
			map.put("qys",qys);
		}
		map.put("type",req.getParameter("zqlx"));
		return sjxxStatisticService.getMarketStstictisData(map);
	}
	@RequestMapping("/statistics/getMarketStstictisDataLately")
	@ResponseBody
	public Map<String,Object> getMarketStstictisDataLately(Map<String,Object> map,HttpServletRequest req){
		//service获取营销统计数据
		List<String> yf= StringUtil.isNotBlank(req.getParameter("yf"))?Arrays.asList(req.getParameter("yf").split(",")):new ArrayList<>();
		List<String> jcxm=StringUtil.isNotBlank(req.getParameter("jcxm"))?Arrays.asList(req.getParameter("jcxm").split(",")):new ArrayList<>();
		List<String> nf=StringUtil.isNotBlank(req.getParameter("nf"))?Arrays.asList(req.getParameter("nf").split(",")):new ArrayList<>();
		map.put("moneyflag",req.getParameter("moneyflag")==null?"":req.getParameter("moneyflag"));
		map.put("yf",yf);
		map.put("jcxm",jcxm);
		map.put("nf",nf);
		String qy = req.getParameter("qy");
		if (qy.indexOf("ALL")==-1){
			List<String> qys= StringUtil.isNotBlank(qy)?Arrays.asList(qy.split(",")):new ArrayList<>();
			map.put("qys",qys);
		}
		map.put("type",req.getParameter("zqlx"));
		return sjxxStatisticService.getMarketStstictisDataLately(map);
	}
	@RequestMapping("/statistics/getMarketStatisticsDataWithSj")
	@ResponseBody
	public Map<String,Object> getMarketStatisticsDataWithSj(Map<String,Object> map,HttpServletRequest req){
		//根据flag查询对应数据
		map.put("flag",req.getParameter("flag")==null?"":req.getParameter("flag"));
		return sjxxStatisticService.getMarketStatisticsDataWithSj(map);
	}
}
