package com.matridx.server.wechat.control;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.server.wechat.dao.entities.FjsqDto;
import com.matridx.server.wechat.dao.entities.SjbgsmDto;
import com.matridx.server.wechat.dao.entities.SjhbxxDto;
import com.matridx.server.wechat.dao.entities.SjnyxDto;
import com.matridx.server.wechat.dao.entities.SjwzxxDto;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.SjxxjgDto;
import com.matridx.server.wechat.dao.entities.SjybztDto;
import com.matridx.server.wechat.dao.entities.SjzmjgDto;
import com.matridx.server.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.server.wechat.service.svcinterface.ISjnyxService;
import com.matridx.server.wechat.service.svcinterface.ISjxxService;
import com.matridx.server.wechat.service.svcinterface.ISjxxjgService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Controller
@RequestMapping("/wechat")
public class WeChatStatictisController extends BaseController {
	
	@Value("${matridx.wechat.companyurl:}")
	private String companyurl;
	@Autowired
	ICommonService commonService;
	@Autowired
	ISjnyxService sjnyxService;
	@Autowired
	ISjxxService sjxxService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	ISjbgsmService sjbgsmService;
	@Autowired
	ISjxxjgService sjxxjgService;

	
	/**
	 * 图片预览
	 * @param fjcfbDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/statistics/pdfPreview")
	public ModelAndView pdfPreview(FjcfbDto fjcfbDto, HttpServletRequest request) {
		try {
			boolean checkSign = commonService.checkSign(fjcfbDto.getSign(), fjcfbDto.getFjid(), request);
			if(!checkSign){
                return commonService.jumpError();
			}
			String filepath = "/wechat/file/getFileInfo?fjid=" + fjcfbDto.getFjid();
			ModelAndView mv = new ModelAndView("wechat/statistics/viewer");
			mv.addObject("file",filepath);
			
			return mv;
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据传入日期获取当前周
	 * @param jsrq
	 * @return
	 */
	public Map<String, String> getWeekTime(String jsrq){
		Map<String, String> map = new HashMap<>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		int dayOfWeek;
		String jsrqstart = null;
		String jsrqend = null;
		try {
			if(StringUtils.isNotBlank(jsrq)) {
				dayOfWeek = DateUtils.getWeek(sf.parse(jsrq));
			}else {
				jsrq=sf.format(new Date());
				dayOfWeek = DateUtils.getWeek(sf.parse(jsrq));
			}
			if (dayOfWeek <= 2) {
				jsrqstart = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), -dayOfWeek - 7), "yyyy-MM-dd");
				jsrqend = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), -dayOfWeek - 1), "yyyy-MM-dd");
			} else {
				jsrqstart = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), -dayOfWeek), "yyyy-MM-dd");
				jsrqend = DateUtils.format(DateUtils.getDate(sf.parse(jsrq), 6 - dayOfWeek), "yyyy-MM-dd");
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		map.put("jsrqstart", jsrqstart);
		map.put("jsrqend", jsrqend);
		return map;
	}
	
	/*------------------------------------------  个人日报   -------------------------------------------------*/
	
	/**
	 * 日报统计，跳转日报页面
	 * @return
	 */
	@RequestMapping("/statistics/getWeChatDaily")
	public ModelAndView getWeChatDaily(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/statistics/statistic_daily");
		//直接访问日报
		String sign=commonService.getSign(sjxxDto.getJsrq());
		sjxxDto.setSign(sign);
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}
	
	/**
	 * 日报统计，由日报页面发起的获取相应信息
	 * @return
	 */
	@RequestMapping("/statistics/getDailyModel")
	@ResponseBody
	public Map<String, Object> getDailyModel(SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/getDailyModel", paramMap, Map.class);
		if(resultMap != null) {
            map = resultMap;
        }
		return map;
	}
	
	/**
	 * 返回日报主页
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/go_back_dailyByYhid")
	public ModelAndView go_back_dailyByYhid(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/statistics/statistic_daily");
		String sign=commonService.getSign(sjxxDto.getJsrq());
		sjxxDto.setSign(sign);
		mav.addObject("sjxxDto", sjxxDto);
		return mav;
	}
	
	/**
	 * 微信用户日报查询复检清单
	 * @return
	 */
	@RequestMapping(value ="/statistics/fjsqViewDaily")
	public ModelAndView fjsqViewDaily(FjsqDto fjsqDto) {
		ModelAndView mav=new ModelAndView("wechat/statistics/recheck_view");
		mav.addObject("fjsqDto", fjsqDto);
		return mav;
	}
	
	/**
	 * 微信用户日报查询复检列表
	 * @param fjsqDto
	 * @return
	 */
	@RequestMapping("/statistics/recheckListDaily")
	@ResponseBody
	public Map<String, Object> recheckListDaily(FjsqDto fjsqDto){
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("fjsqDto", JSONObject.toJSONString(fjsqDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/recheckListDaily", paramMap, Map.class);
		return map;
	}
	
	/**
	 * 微信统计阴阳性列表
	 * @param sjwzxxDto
	 * @return
	 */
	@RequestMapping("/statistics/positiveViewDaily")
	public ModelAndView positiveViewDaily(SjwzxxDto sjwzxxDto) {
		ModelAndView mav=new ModelAndView("wechat/statistics/repositive_view");
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjwzxxDto", JSONObject.toJSONString(sjwzxxDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/positiveViewDaily", paramMap, Map.class);
		if("success".equals((String)map.get("status"))){
			@SuppressWarnings("unchecked")
			List<SjxxDto> sjxxList = (List<SjxxDto>) map.get("sjxxList");
			mav.addObject("sjxxList", sjxxList);
			String str = (String) map.get("sjwzxxDto");
			JSONObject parseObject = JSONObject.parseObject(str);
			SjwzxxDto t_sjwzxxDto = JSONObject.toJavaObject(parseObject, SjwzxxDto.class);
			mav.addObject("sjwzxxDto", t_sjwzxxDto);
		}else{
			mav = commonService.jumpError();
		}
		return mav;
	}
	
	/**
	* 查看送检信息
	* @param sjxxDto
	* @return
	*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/statistics/sjxxViewDaily")
	public ModelAndView viewSjxx(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/statistics/sjxx_ListView");
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		DBEncrypt crypt = new DBEncrypt();
		Map<String, Object> map = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/sjxxViewDaily", paramMap, Map.class);
		List<FjcfbDto> fjcfbDtos = (List<FjcfbDto>) map.get("fjcfbDtos");
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("SjnyxDto", (List<SjnyxDto>) map.get("SjnyxDtos"));
		List<FjcfbDto> zhwj_fjcfbDtos = (List<FjcfbDto>) map.get("zhwj_fjcfbDtos");
		mav.addObject("zhwjpdf", zhwj_fjcfbDtos);
		mav.addObject("sjbgsmList",(List<SjbgsmDto>) map.get("sjbgsmList"));
		List<FjcfbDto> t_fjcfbDtos = (List<FjcfbDto>) map.get("t_fjcfbDtos");
		mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
		String str = (String) map.get("t_sjxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		mav.addObject("sjxxDto", JSONObject.toJavaObject(parseObject, SjxxDto.class));	
		mav.addObject("Sjwzxx", (List<SjwzxxDto>) map.get("sjwzxxDtos"));
        mav.addObject("Xpxx", (String)map.get("Xpxx"));
        mav.addObject("sjzmList", (List<SjzmjgDto>)map.get("sjzmList"));
        mav.addObject("KZCS", (String)map.get("KZCS"));
        mav.addObject("jcxmlist", (List<JcsjDto>)map.get("jcxmlist"));
        mav.addObject("SjxxjgList", (List<SjxxjgDto>)map.get("SjxxjgList"));
        mav.addObject("wzjcxmlist", (List<JcsjDto>)map.get("wzjcxmlist"));
		return mav;
	}
	
	/**
	 * 微信统计废弃标本点击页面
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping(value ="/statistics/fqybViewDaily")
	public ModelAndView fqybViewDaily(SjybztDto sjybztDto) {
		ModelAndView mav=new ModelAndView("wechat/statistics/sampleState_view");
		if("0".equals(sjybztDto.getZt())) {
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
	@RequestMapping("/statistics/sampleStateListDaily")
	@ResponseBody
	public Map<String, Object> sampleStateListDaily(SjybztDto sjybztDto){
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjybztDto", JSONObject.toJSONString(sjybztDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/sampleStateListDaily", paramMap, Map.class);
		return map;
	}
	
	/**
	 * 微信统计废弃标本占比
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping("/statistics/getpercentageDaily")
	@ResponseBody
	public Map<String, String> getpercentageDaily(SjybztDto sjybztDto){
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjybztDto", JSONObject.toJSONString(sjybztDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, String> map = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/getpercentageDaily", paramMap, Map.class);
		return map;
	}
	
	/*------------------------------------------  个人周报   -------------------------------------------------*/
	
	/**
	 * 微信用户访问周报统计
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/getWeChatWeekly")
	public ModelAndView weeklyByYhid(SjxxDto sjxxDto) {
		ModelAndView mav=new ModelAndView("wechat/statistics/statistic_weekly");
		if(StringUtil.isBlank(sjxxDto.getJsrqstart())){
			Map<String, String> weekTimeMap = getWeekTime(sjxxDto.getJsrq());
			String jsrqstart = weekTimeMap.get("jsrqstart");
			String jsrqend = weekTimeMap.get("jsrqend");
			sjxxDto.setJsrqstart(jsrqstart);
			sjxxDto.setJsrqend(jsrqend);
		}
		String sign=commonService.getSign(sjxxDto.getJsrqstart()+sjxxDto.getJsrqend());
		sjxxDto.setSign(sign);
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/getWeChatWeekly", paramMap, Map.class);
		@SuppressWarnings("unchecked")
		List<SjhbxxDto> sjhbxxDtos = (List<SjhbxxDto>) map.get("sjhbxxDtos");
		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		mav.addObject("sjxxDto",sjxxDto);
		return mav;
	}

	/**
	 * 微信用户查询周报数据
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/getWeeklyModel")
	@ResponseBody
	public Map<String, Object> getWeeklyModel(SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/getWeeklyModel", paramMap, Map.class);
		if(resultMap != null) {
            map = resultMap;
        }
		return map;
	}	
	
	/**
	 * 通过条件查询刷新页面
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@RequestMapping("/statistics/getWeeklyModel_Condition")
	@ResponseBody
	public Map<String, Object> getWeeklyModel_Condition(SjxxDto sjxxDto, HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		String method = request.getParameter("method");
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		paramMap.set("method", method);
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/getWeeklyModel_Condition", paramMap, Map.class);
		if(resultMap != null) {
            map = resultMap;
        }
		return map;
	}
	
	/**
	 * 通过用户id，查询伙伴权限
	 * 
	 * @return
	 */
	@RequestMapping("/statistics/getWeeklyPartner_Hbqx")
	@ResponseBody
	public Map<String, Object> getWeeklyPartner_Hbqx(SjxxDto sjxxDto, SjhbxxDto sjhbxxDto){
		Map<String, Object> map = new HashMap<>();
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		paramMap.set("sjhbxxDto", JSONObject.toJSONString(sjhbxxDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/getWeeklyPartner_Hbqx", paramMap, Map.class);
		if(resultMap != null) {
            map = resultMap;
        }
		return map;
	}
	
	/**
	 * 统计合作伙伴页面
	 * @param sjxxDto
	 * @param sjhbxxDto
	 * @return
	 */
	@RequestMapping("/statistics/weeklyPartnerPage")
	public ModelAndView weeklyPartnerPage(SjxxDto sjxxDto, SjhbxxDto sjhbxxDto){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statistics/statistic_weekly_partner");
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		paramMap.set("sjhbxxDto", JSONObject.toJSONString(sjhbxxDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/weeklyPartnerPage", paramMap, Map.class);
		@SuppressWarnings("unchecked")
		List<SjhbxxDto> sjhbxxDtos = (List<SjhbxxDto>) resultMap.get("sjhbxxDtos");
		String str = (String) resultMap.get("sjxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjxxDto t_sjxxDto = JSONObject.toJavaObject(parseObject, SjxxDto.class);
		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		mav.addObject("sjxxDto", t_sjxxDto);
		return mav;
	}
	
	/**
	 * 查询汇报领导的伙伴统计
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/getPartnerStatisWeekly")
	@ResponseBody
	public Map<String, Object> getPartnerStatisWeekly(SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/getPartnerStatisWeekly", paramMap, Map.class);
		if(resultMap != null) {
            map = resultMap;
        }
		return map;
	}
	
	
	/*------------------------------------------  全部日报   -------------------------------------------------*/
	
	/**
	 * 日报统计，跳转日报页面
	 * @return
	 */
	@RequestMapping("/statistics/getAllDaily")
	public ModelAndView getDaily(SjxxDto sjxxDto) {
		//直接访问日报
		ModelAndView mav=new ModelAndView("wechat/statis/statistics_day_lead");
		String sign=commonService.getSign(sjxxDto.getJsrq());
		sjxxDto.setSign(sign);
		String load_flg="1";
		mav.addObject("sjxxDto", sjxxDto);
		mav.addObject("load_flg", load_flg);
		return mav;
	}
	
	/**
	 * 日报统计，由日报页面发起的获取相应信息
	 * @return
	 */
	@RequestMapping("/statistics/getSfsfybxx")
	@ResponseBody
	public Map<String, Object> getSfsfybxx(SjxxDto sjxxDto){
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		//判断用户权限
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/getSfsfybxx", paramMap, Map.class);
		if(map == null) {
            map = new HashMap<>();
        }
		return map;
	}
	
	/**
	 * 复检统计点击查看列表
	 * @return
	 */
	@RequestMapping(value ="/statistics/FjsqListView")
	public ModelAndView FjsqListView(FjsqDto fjsqDto,String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statis/recheckList");
		mav.addObject("fjsqDto", fjsqDto);
		mav.addObject("load_flg", load_flg);
		return mav;
	}
	
	/**
	 * 日报统计，由日报页面发起的获取相应信息
	 * @return
	 */
	@RequestMapping("/recheck/RecheckStatisPage")
	@ResponseBody
	public Map<String, Object> RecheckStatisPage(FjsqDto fjsqDto){
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("fjsqDto", JSONObject.toJSONString(fjsqDto));
		//判断用户权限
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/RecheckStatisPage", paramMap, Map.class);
		if(map == null) {
            map = new HashMap<>();
        }
		return map;
	}
	
	/**
	 * 废弃标本统计点击查看页面
	 * @return
	 */
	@RequestMapping(value ="/statistics/fqybListView")
	public ModelAndView FqybListView(SjybztDto sjybztDto,String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statis/sampleState");
		if("0".equals(sjybztDto.getZt())) {
			sjybztDto.setZtflg("0");
		}else {
			sjybztDto.setZtflg("1");
		}
		mav.addObject("sjybztDto", sjybztDto);
		mav.addObject("load_flg", load_flg);
		return mav;
	}
	
	/**
	 * 废弃标本统计列表
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping("/statistics/sampleStatePage")
	@ResponseBody
	public Map<String, Object> sampleStatePage(SjybztDto sjybztDto){
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjybztDto", JSONObject.toJSONString(sjybztDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/sampleStatePage", paramMap, Map.class);
		return map;
	}
	
	/**
	 * 查询不同状态的标本的占比
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping("/statistics/getPercentage")
	@ResponseBody
	public Map<String, String> getPercentage(SjybztDto sjybztDto){
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjybztDto", JSONObject.toJSONString(sjybztDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, String> map = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/getPercentage", paramMap, Map.class);
		return map;
	}
	
	/**
	 * 送检报告阳性列表
	 * @param sjwzxxDto
	 * @return
	 */
	@RequestMapping("/statistics/positive_PageList")
	public ModelAndView positive_PageList(SjwzxxDto sjwzxxDto,String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statis/rpositive");
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjwzxxDto", JSONObject.toJSONString(sjwzxxDto));
		DBEncrypt crypt = new DBEncrypt();
		//判断用户权限
		@SuppressWarnings("unchecked")
		Map<String, Object> map = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/getListPositive", paramMap, Map.class);
		@SuppressWarnings("unchecked")
		List<SjxxDto> sjxxList = (List<SjxxDto>) map.get("sjxxList");
		mav.addObject("sjxxList",sjxxList);
		mav.addObject("sjwzxxDto", sjwzxxDto);
		mav.addObject("load_flg", load_flg);
		return mav;
	}
	
	/**
	* 查看送检信息
	* @param sjxxDto
	* @return
	*/
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/statistics/sjxxViewAllDaily")
	public ModelAndView sjxxViewAllDaily(SjxxDto sjxxDto, String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statis/sjxx_ListView");
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		DBEncrypt crypt = new DBEncrypt();
		Map<String, Object> map = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/sjxxViewDaily", paramMap, Map.class);
		List<FjcfbDto> fjcfbDtos = (List<FjcfbDto>) map.get("fjcfbDtos");
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("SjnyxDto", (List<SjnyxDto>) map.get("SjnyxDtos"));
		List<FjcfbDto> zhwj_fjcfbDtos = (List<FjcfbDto>) map.get("zhwj_fjcfbDtos");
		mav.addObject("zhwjpdf", zhwj_fjcfbDtos);
		mav.addObject("sjbgsmList",(List<SjbgsmDto>) map.get("sjbgsmList"));
		List<FjcfbDto> t_fjcfbDtos = (List<FjcfbDto>) map.get("t_fjcfbDtos");
		mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
		String str = (String) map.get("t_sjxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		mav.addObject("sjxxDto", JSONObject.toJavaObject(parseObject, SjxxDto.class));	
		mav.addObject("Sjwzxx", (List<SjwzxxDto>) map.get("sjwzxxDtos"));
        mav.addObject("Xpxx", (String)map.get("Xpxx"));
        mav.addObject("sjzmList", (List<SjzmjgDto>)map.get("sjzmList"));
        mav.addObject("KZCS", (String)map.get("KZCS"));
        mav.addObject("jcxmlist", (List<JcsjDto>)map.get("jcxmlist"));
        mav.addObject("SjxxjgList", (List<SjxxjgDto>)map.get("SjxxjgList"));
        mav.addObject("wzjcxmlist", (List<JcsjDto>)map.get("wzjcxmlist"));
		return mav;
	}
	
	/*------------------------------------------  全部周报   -------------------------------------------------*/
	
	/**
	 * 周报统计页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/getAllWeekly")
	public ModelAndView getAllWeekly(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statis/statistics_weekly_lead");
		if(StringUtil.isBlank(sjxxDto.getJsrqstart())){
			Map<String, String> weekTimeMap = getWeekTime(sjxxDto.getJsrq());
			String jsrqstart = weekTimeMap.get("jsrqstart");
			String jsrqend = weekTimeMap.get("jsrqend");
			sjxxDto.setJsrqstart(jsrqstart);
			sjxxDto.setJsrqend(jsrqend);
		}
		String sign=commonService.getSign(sjxxDto.getJsrqstart()+sjxxDto.getJsrqend());
		sjxxDto.setSign(sign);
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/weekLeadStatisPage", paramMap, Map.class);
		@SuppressWarnings("unchecked")
		List<SjhbxxDto> sjhbxxDtos = (List<SjhbxxDto>) map.get("sjhbxxDtos");
		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		String load_flg="0";
		mav.addObject("load_flg",load_flg);
		return mav;
	}
	
	/**
	 * 周报统计页面
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/getAllWeeklyByJsrq")
	public ModelAndView getAllWeeklyByJsrq(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statis/statistics_weekly_lead_jsrq");
		if(StringUtil.isBlank(sjxxDto.getJsrqstart())){
			Map<String, String> weekTimeMap = getWeekTime(sjxxDto.getJsrq());
			String jsrqstart = weekTimeMap.get("jsrqstart");
			String jsrqend = weekTimeMap.get("jsrqend");
			sjxxDto.setJsrqstart(jsrqstart);
			sjxxDto.setJsrqend(jsrqend);
		}
		String sign=commonService.getSign(sjxxDto.getJsrqstart()+sjxxDto.getJsrqend());
		sjxxDto.setSign(sign);
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> map = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/weekLeadStatisPageByJsrq", paramMap, Map.class);
		@SuppressWarnings("unchecked")
		List<SjhbxxDto> sjhbxxDtos = (List<SjhbxxDto>) map.get("sjhbxxDtos");
		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		String load_flg="0";
		mav.addObject("load_flg",load_flg);
		return mav;
	}
	
	/**
	 * 微信用户查询周报数据
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/getWeekLeadStatis")
	@ResponseBody
	public Map<String, Object> getWeekLeadStatis(SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/getWeekLeadStatis", paramMap, Map.class);
		if(resultMap != null) {
            map = resultMap;
        }
		return map;
	}
	
	/**
	 * 通过条件查询刷新页面
	 * @param sjxxDto
	 * @param request
	 * @return
	 */
	@RequestMapping("/statistics/getSjxxStatisByTj")
	@ResponseBody
	public Map<String, Object> getSjxxStatisByTj(SjxxDto sjxxDto, HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		paramMap.set("method", request.getParameter("method"));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/getSjxxStatisByTj", paramMap, Map.class);
		if(resultMap != null) {
            map = resultMap;
        }
		return map;
	}
	
	/**
	  * 获取标本路线信息
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping(value = "/statistics/getYblx")
	@ResponseBody
	public Map<String,Object> getYblx(SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/getYblx", paramMap, Map.class);
		if(resultMap != null) {
            map = resultMap;
        }
		return map;
	}
	
	/**
	 * 统计伙伴信息页面
	 * @param sjxxDto
	 * @param sjhbxxDto
	 * @param load_flg
	 * @return
	 */
	@RequestMapping("/statistics/weekLeadStatisPartnerPage")
	public ModelAndView weekLeadStatisPartnerPage(SjxxDto sjxxDto, SjhbxxDto sjhbxxDto,String load_flg){
		// TODO Auto-generated method stub
		ModelAndView mav = new ModelAndView("wechat/statis/statistics_weekly_lead_partner");
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		paramMap.set("sjhbxxDto", JSONObject.toJSONString(sjhbxxDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/weekLeadStatisPartnerPage", paramMap, Map.class);
		@SuppressWarnings("unchecked")
		List<SjhbxxDto> sjhbxxDtos = (List<SjhbxxDto>) resultMap.get("sjhbxxDtos");
		String str = (String) resultMap.get("sjxxDto");
		JSONObject parseObject = JSONObject.parseObject(str);
		SjxxDto t_sjxxDto = JSONObject.toJavaObject(parseObject, SjxxDto.class);
		mav.addObject("sjhbxxDtos", sjhbxxDtos);
		mav.addObject("sjxxDto", t_sjxxDto);
		mav.addObject("load_flg",load_flg);
		return mav;
	}
	
	/**
	 * 查询汇报领导的伙伴统计
	 * 
	 * @return
	 */
	@RequestMapping("/statistics/getWeekLeadPartnerStatis")
	@ResponseBody
	public Map<String, Object> getWeekLeadPartnerStatis(SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/getWeekLeadPartnerStatis", paramMap, Map.class);
		if(resultMap != null) {
            map = resultMap;
        }
		return map;
	}
	
	/**
	 * 查询伙伴信息
	 * @param sjxxDto
	 * @param sjhbxxDto
	 * @return
	 */
	@RequestMapping("/statistics/getWeekLeadStatisPartner")
	@ResponseBody
	public Map<String, Object> getWeekLeadStatisPartner(SjxxDto sjxxDto, SjhbxxDto sjhbxxDto){
		Map<String, Object> map = new HashMap<>();
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.set("sjxxDto", JSONObject.toJSONString(sjxxDto));
		paramMap.set("sjhbxxDto", JSONObject.toJSONString(sjhbxxDto));
		DBEncrypt crypt = new DBEncrypt();
		@SuppressWarnings("unchecked")
		Map<String, Object> resultMap = restTemplate.postForObject(crypt.dCode(companyurl)+"/ws/miniprogram/getWeekLeadStatisPartner", paramMap, Map.class);
		if(resultMap != null) {
            map = resultMap;
        }
		return map;
	}
	
	/**
	 * 查询汇报领导的统计
	 * @param request
	 * @param sjxxDto
	 * @return
	 */
	@RequestMapping("/statistics/getWeekLeadStatisByrq")
	@ResponseBody
	public Map<String, Object> getWeekLeadStatisByrq(HttpServletRequest request,SjxxDto sjxxDto){
		Map<String, Object> map = new HashMap<>();
		String method = request.getParameter("method");
		map.put("searchData", sjxxDto);
		if(StringUtil.isBlank(method)){
			return map;
		}
		//elseif内代码被注释，故注释该块代码，2023/10/26
//		else if("getHbdwByrq".equals(method)) {
//			//map = sjxxService.getSjdwBydb(sjxxDto);
//		}
		return map;
	}
	
	/**
	 * 周报复检申请查看
	 * @param fjsqDto
	 * @param load_flg
	 * @return
	 */
	@RequestMapping("/statistics/week_PageList_Fjsq")
	public ModelAndView week_PageList_Fjsq(FjsqDto fjsqDto,String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statis/recheckList");
		mav.addObject("fjsqDto", fjsqDto);
		mav.addObject("load_flg", load_flg);
		return mav;
	}
	
	/**
	 * 周报废弃标本查看
	 * @param sjybztDto
	 * @return
	 */
	@RequestMapping("/statistics/week_PageList_Fqyb")
	public ModelAndView week_PageList_Fqyb(SjybztDto sjybztDto,String load_flg) {
		ModelAndView mav=new ModelAndView("wechat/statis/sampleState");
		if("0".equals(sjybztDto.getZt())) {
			sjybztDto.setZtflg("0");
		}else {
			sjybztDto.setZtflg("1");
		}
		mav.addObject("sjybztDto", sjybztDto);
		mav.addObject("load_flg", load_flg);
		return mav;
	}
	
}
