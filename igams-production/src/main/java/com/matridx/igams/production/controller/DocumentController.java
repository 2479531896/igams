package com.matridx.igams.production.controller;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.DepartmentDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.SsdwTableEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.FfjlDto;
import com.matridx.igams.production.dao.entities.GlwjxxDto;
import com.matridx.igams.production.dao.entities.WjglDto;
import com.matridx.igams.production.dao.entities.WjssdwDto;
import com.matridx.igams.production.service.svcinterface.IFfjlService;
import com.matridx.igams.production.service.svcinterface.IGlwjxxService;
import com.matridx.igams.production.service.svcinterface.IWjglService;
import com.matridx.igams.production.service.svcinterface.IWjqxService;
import com.matridx.igams.production.service.svcinterface.IWjssdwService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/production")
public class DocumentController extends BaseBasicController{

	@Autowired
	IWjglService wjglService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IWjqxService wjqxService;
	@Autowired
	IWjssdwService wjssdwService;
	@Autowired
	ICommonService commonService;
	@Autowired
	IXtszService xtszService;
	@Autowired
	ICommonDao commonDao;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IFfjlService ffjlService;
	
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	
	@Autowired
	RestTemplate restTemplate;

	@Autowired
	IGlwjxxService glwjxxService;

	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	
	/**
	 * 文件列表页面
	 */
	@RequestMapping(value ="/document/pageListDocument")
	public ModelAndView pageListDcoument(WjglDto wjglDto,HttpServletRequest request){
		ModelAndView mav = new ModelAndView("production/document/document_list");
		List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DOCUMENT_TYPE.getCode());
		List<JcsjDto> wjfllist=new ArrayList<>();
		if(wjglDto.getWjfls()!=null&&wjglDto.getWjfls().length>0){
			String wjflString = "";
			for(JcsjDto jcsjDto:list){
				for(String wjfl:wjglDto.getWjfls()){
					if(wjfl.equals(jcsjDto.getCskz1())){
						wjfllist.add(jcsjDto);
						wjflString = wjflString + "," + jcsjDto.getCsid();
					}
				}
			}
			if(StringUtil.isNotBlank(wjflString)){
				wjflString = wjflString.substring(1);
				wjglDto.setWjfls(wjflString.split(","));
				wjglDto.setWjfls_str(wjflString);
			}else{
				wjfllist=list;
			}
		}else{
			wjfllist=list;
		}
		mav.addObject("wjfllist", wjfllist);
		User user=getLoginInfo(request);
		mav.addObject("jsid",user.getDqjs());
		mav.addObject("yhid",user.getYhid());
		wjglDto.setFwbj("authority");
		//获取审核类型
		wjglDto.setAuditType(AuditTypeEnum.QUALITY_FILE_ADD.getCode());
		wjglDto.setAuditModType(AuditTypeEnum.QUALITY_FILE_MOD.getCode());
		mav.addObject("wjglDto",wjglDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("formAction","/production/document/pageGetListDocument");
		return mav;
	}

	/**
	 * 文件统计
	 */
	@RequestMapping("/document/pageStatistics")
	public ModelAndView pageListStats(){
		ModelAndView mav = new ModelAndView("production/document/document_stats");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR,calendar.get(Calendar.DAY_OF_YEAR)-7);
		Date date = new Date();
		Date endTime = calendar.getTime();
		List<WjssdwDto> listGroup = wjssdwService.getListGroup();
		mav.addObject("endTime", simpleDateFormat.format(date));
		mav.addObject("startTime", simpleDateFormat.format(endTime));
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("jglist", listGroup);
		List<WjglDto> yearGroup = wjglService.getYearGroup();
		mav.addObject("yearlist", yearGroup);
		return mav;
	}

	/**
	 * 文件统计详情
	 */
	@RequestMapping("/document/pageGetStatistics")
	@ResponseBody
	public Map<String,Object> pageGetStatisticsInfo(WjglDto wjglDto){
		Map<String,Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(wjglDto.getCs())){
			if (wjglDto.getCs().contains("xgsj")){
				List<WjglDto> wjglDtoList = wjglService.getUpdateStatsInfo(wjglDto);
				map.put("xdwj",wjglDtoList);
			}else{
				List<WjglDto> wjglDtos = wjglService.getInsertStatsInfo(wjglDto);
				map.put("xzwj",wjglDtos);
			}
		}else if(StringUtil.isNotBlank(wjglDto.getYear())||StringUtil.isNotBlank(wjglDto.getJgid())){
			List<WjglDto> yflist = wjglService.getDocumentMonthStats(wjglDto);
			map.put("wjyf",yflist);
		}else{
			wjglDto.setCs(wjglDto.getLrsjDay());
			List<WjglDto> wjglDtos = wjglService.getInsertStatsInfo(wjglDto);
			wjglDto.setCs(wjglDto.getXgsjDay());
			List<WjglDto> wjglDtoList = wjglService.getUpdateStatsInfo(wjglDto);
			map.put("xzwj",wjglDtos);
			map.put("xdwj",wjglDtoList);
			List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DOCUMENT_TYPE.getCode());
			List<WjglDto> wjlist=new ArrayList<>();
			if(!CollectionUtils.isEmpty(list)){
				List<WjglDto> fileStats = wjglService.getDocumentClassStats();
				if(!CollectionUtils.isEmpty(fileStats)){
					for(JcsjDto jcsjDto:list){
						String sl="0";
						for(WjglDto dto:fileStats){
							if(jcsjDto.getCsid().equals(dto.getWjfl())){
								sl=dto.getWjsl();
								break;
							}
						}
						WjglDto wjglDto_t=new WjglDto();
						wjglDto_t.setWjflmc(jcsjDto.getCsmc());
						wjglDto_t.setWjsl(sl);
						wjlist.add(wjglDto_t);
					}
				}else{
					for(JcsjDto jcsjDto:list){
						WjglDto wjglDto_t=new WjglDto();
						wjglDto_t.setWjflmc(jcsjDto.getCsmc());
						wjglDto_t.setWjsl("0");
						wjlist.add(wjglDto_t);
					}
				}
			}
			map.put("wjfl",wjlist);
			if(StringUtil.isNotBlank(wjglDto.getLrsj())){
				wjglDto.setYear(wjglDto.getLrsj());
			}
			List<WjglDto> yflist = wjglService.getDocumentMonthStats(wjglDto);
			map.put("wjyf",yflist);
		}
		return map;
	}
	
	/**
	 * 获取文件分类
	 */
	@RequestMapping(value ="/document/getListInit")
	@ResponseBody
	public Map<String,Object> getListInit(HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DOCUMENT_TYPE});
		result.put("wjfllist", jclist.get(BasicDataTypeEnum.DOCUMENT_TYPE.getCode()));
		
		super.setCzdmList(request,result);
		
		return result;
	}
	
	
	/**
	 * 文件列表
	 */
	@RequestMapping(value ="/document/pageGetListDocument")
	@ResponseBody
	public Map<String,Object> pageGetListDocument(WjglDto wjglDto,HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		if(wjglDto.getFwbj().equals("authority")){
			User user = getLoginInfo(request);
			wjglDto.setJsid(user.getDqjs());
			wjglDto.setYhid(user.getYhid());
			//获取用户信息
			List<WjglDto> t_List = wjglService.getPagedDtoList(wjglDto);
			//Json格式的要求{total:22,rows:{}}
			//构造成Json的格式传递
			result.put("total", wjglDto.getTotalNumber());
			result.put("rows", t_List);
		}else if(wjglDto.getFwbj().equals("all")){
			//获取用户信息
			List<WjglDto> t_List = wjglService.getPagedDtoList(wjglDto);
			//Json格式的要求{total:22,rows:{}}
			//构造成Json的格式传递
			result.put("total", wjglDto.getTotalNumber());
			result.put("rows", t_List);
		}
		return result;
	}

	/**
	 * 全部文件列表
	 */
	@RequestMapping(value ="/document/pageGetListAllDocument")
	@ResponseBody
	public Map<String,Object> pageGetListAllDocument(WjglDto wjglDto,HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
		if(wjglDto.getFwbj().equals("authority")){
			User user = getLoginInfo(request);
			wjglDto.setJsid(user.getDqjs());
			wjglDto.setYhid(user.getYhid());
			//获取用户信息
			List<WjglDto> t_List = wjglService.getPagedDtoList(wjglDto);
			//Json格式的要求{total:22,rows:{}}
			//构造成Json的格式传递
			result.put("total", wjglDto.getTotalNumber());
			result.put("rows", t_List);
		}else if(wjglDto.getFwbj().equals("all")){
			//获取用户信息
			List<WjglDto> t_List = wjglService.getPagedDtoList(wjglDto);
			//Json格式的要求{total:22,rows:{}}
			//构造成Json的格式传递
			result.put("total", wjglDto.getTotalNumber());
			result.put("rows", t_List);
		}
		return result;
	}
	/**
	 * 全部文件列表页面（无权限）
	 */
	@RequestMapping(value ="/document/pageListAllDocument")
	public ModelAndView pageListAllDcoument(WjglDto wjglDto){
		ModelAndView mav = new ModelAndView("production/document/document_list");
		List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DOCUMENT_TYPE.getCode());
		List<JcsjDto> wjfllist=new ArrayList<>();
		if(wjglDto.getWjfls()!=null&&wjglDto.getWjfls().length>0){
			String wjflString = "";
			for(JcsjDto jcsjDto:list){
				for(String wjfl:wjglDto.getWjfls()){
					if(wjfl.equals(jcsjDto.getCskz1())){
						wjfllist.add(jcsjDto);
						wjflString = wjflString + "," + jcsjDto.getCsid();
					}
				}
			}
			if(StringUtil.isNotBlank(wjflString)){
				wjflString = wjflString.substring(1);
				wjglDto.setWjfls(wjflString.split(","));
				wjglDto.setWjfls_str(wjflString);
			}else{
				wjfllist=list;
			}
		}else{
			wjfllist=list;
		}
		mav.addObject("wjfllist", wjfllist);
		wjglDto.setFwbj("all");
		//获取审核类型
		wjglDto.setAuditType(AuditTypeEnum.QUALITY_FILE_ADD.getCode());
		wjglDto.setAuditModType(AuditTypeEnum.QUALITY_FILE_MOD.getCode());
		mav.addObject("wjglDto",wjglDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("formAction","/production/document/pageGetListAllDocument");
		return mav;
	}

	/**
	 * 文件查看
	 */
	@RequestMapping(value="/document/pagedataViewDocument")
	public ModelAndView pagedataViewDocument(WjglDto wjglDto, HttpServletRequest request){
		return this.viewDocument(wjglDto,request);
	}
	/**
	 * 文件查看
	 */
	@RequestMapping(value="/document/viewDocument")
	public ModelAndView viewDocument(WjglDto wjglDto, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("production/document/document_view");
		wjglDto.setFormAction("view");
		mav.addObject("wjglaction",wjglDto);
		//查询附件
		List<FjcfbDto> fjcfbDtos = wjglService.selectFjcfbDtoByWjid(wjglDto.getWjid());
		mav.addObject("fjcfbDtos",fjcfbDtos);
		WjglDto t_wjglDto = wjglService.getDtoById(wjglDto.getWjid(),request);
		//获取权限角色
		t_wjglDto = wjqxService.getPermissionStr(t_wjglDto, request);
		
		//扩展字段显示名称设置
		if(t_wjglDto!=null) {
			JcsjDto fl_jcsjDto=jcsjService.getDtoById(t_wjglDto.getWjfl());
			JcsjDto lb_jcsjDto=jcsjService.getDtoById(t_wjglDto.getWjlb());
			if(fl_jcsjDto!=null) {
				String cskz1=fl_jcsjDto.getCskz1();
				if(StringUtils.isNotBlank(cskz1)) {
					String szlb="document.type."+cskz1;
					List<XtszDto> xtszlist=xtszService.getObscureDto(szlb);
					if(!CollectionUtils.isEmpty(xtszlist)){
						for (XtszDto xtszDto : xtszlist) {
							if ("cskz1".equals(xtszDto.getSzz())) {
								t_wjglDto.setCskz1mc(xtszDto.getSzmc());
							} else if ("cskz2".equals(xtszDto.getSzz())) {
								t_wjglDto.setCskz2mc(xtszDto.getSzmc());
							} else if ("cskz3".equals(xtszDto.getSzz())) {
								t_wjglDto.setCskz3mc(xtszDto.getSzmc());
							}
						}
					}
				}
			}
			if(lb_jcsjDto!=null) {
				String cskz1=lb_jcsjDto.getCskz1();
				if(StringUtils.isNotBlank(cskz1)) {
					String szlb="document.subtype."+cskz1;
					List<XtszDto> xtszlist=xtszService.getObscureDto(szlb);
					if(!CollectionUtils.isEmpty(xtszlist)){
						for (XtszDto xtszDto : xtszlist) {
							if ("cskz1".equals(xtszDto.getSzz())) {
								if (StringUtils.isBlank(t_wjglDto.getCskz1mc()))
									t_wjglDto.setCskz1mc(xtszDto.getSzmc());
							} else if ("cskz2".equals(xtszDto.getSzz())) {
								if (StringUtils.isBlank(t_wjglDto.getCskz2mc()))
									t_wjglDto.setCskz2mc(xtszDto.getSzmc());
							} else if ("cskz3".equals(xtszDto.getSzz())) {
								if (StringUtils.isBlank(t_wjglDto.getCskz3mc()))
									t_wjglDto.setCskz3mc(xtszDto.getSzmc());
							}
						}
					}
				}
			}
		}
		mav.addObject("wjglDto", t_wjglDto);
		//查询履历
		List<WjglDto> wjglDtos = wjglService.getDtoListByGwjid(t_wjglDto);
		mav.addObject("wjglDtos", wjglDtos);
		//查询当前用户是否有下载权限
		User user = getLoginInfo(request);
		wjglDto.setJsid(user.getDqjs());
		boolean result = wjqxService.isDownload(wjglDto);
		mav.addObject("isDownload", result);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	//查找钉钉文件查看基本信息
	@RequestMapping(value="/document/viewDocumentdd")
	@ResponseBody
	public Map<String, Object> viewDocumentdd(WjglDto wjglDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		//查询附件
		List<FjcfbDto> fjcfbDtos = wjglService.selectFjcfbDtoByWjid(wjglDto.getWjid());
		map.put("fjcfbDtos",fjcfbDtos);
		WjglDto t_wjglDto = wjglService.getDtoById(wjglDto.getWjid(),request);
		//获取权限角色
		t_wjglDto = wjqxService.getPermissionStr(t_wjglDto, request);

		//扩展字段显示名称设置
		if(t_wjglDto!=null) {
			JcsjDto fl_jcsjDto=jcsjService.getDtoById(t_wjglDto.getWjfl());
			JcsjDto lb_jcsjDto=jcsjService.getDtoById(t_wjglDto.getWjlb());
			if(fl_jcsjDto!=null) {
				String cskz1=fl_jcsjDto.getCskz1();
				if(StringUtils.isNotBlank(cskz1)) {
					String szlb="document.type."+cskz1;
					List<XtszDto> xtszlist=xtszService.getObscureDto(szlb);
					if(!CollectionUtils.isEmpty(xtszlist)){
						for (XtszDto xtszDto : xtszlist) {
							if ("cskz1".equals(xtszDto.getSzz())) {
								t_wjglDto.setCskz1mc(xtszDto.getSzmc());
							} else if ("cskz2".equals(xtszDto.getSzz())) {
								t_wjglDto.setCskz2mc(xtszDto.getSzmc());
							} else if ("cskz3".equals(xtszDto.getSzz())) {
								t_wjglDto.setCskz3mc(xtszDto.getSzmc());
							}
						}
					}
				}
			}
			if(lb_jcsjDto!=null) {
				String cskz1=lb_jcsjDto.getCskz1();
				if(StringUtils.isNotBlank(cskz1)) {
					String szlb="document.subtype."+cskz1;
					List<XtszDto> xtszlist=xtszService.getObscureDto(szlb);
					if(!CollectionUtils.isEmpty(xtszlist)){
						for (XtszDto xtszDto : xtszlist) {
							if ("cskz1".equals(xtszDto.getSzz())) {
								if (StringUtils.isBlank(t_wjglDto.getCskz1mc()))
									t_wjglDto.setCskz1mc(xtszDto.getSzmc());
							} else if ("cskz2".equals(xtszDto.getSzz())) {
								if (StringUtils.isBlank(t_wjglDto.getCskz2mc()))
									t_wjglDto.setCskz2mc(xtszDto.getSzmc());
							} else if ("cskz3".equals(xtszDto.getSzz())) {
								if (StringUtils.isBlank(t_wjglDto.getCskz3mc()))
									t_wjglDto.setCskz3mc(xtszDto.getSzmc());
							}
						}
					}
				}
			}
		}
		map.put("wjglDto", t_wjglDto);
		//查询当前用户是否有下载权限
		User user = getLoginInfo(request);
		wjglDto.setJsid(user.getDqjs());
		boolean result = wjqxService.isDownload(wjglDto);
		map.put("isDownload", result);
		map.put("urlPrefix", urlPrefix);
		return map;
	}
	
	/**
	 * 文件详细查看
	 */
	@RequestMapping(value="/document/viewmoreDocument")
	public ModelAndView viewmoreDocument(WjglDto wjglDto, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("production/document/document_view");
		wjglDto.setFormAction("viewmore");
		mav.addObject("wjglaction",wjglDto);
		//查询附件
		List<FjcfbDto> fjcfbDtos = wjglService.selectFjcfbDtoByWjid(wjglDto.getWjid());
		mav.addObject("fjcfbDtos",fjcfbDtos);
		WjglDto t_wjglDto = wjglService.getDtoById(wjglDto.getWjid(),request);
		//获取权限角色
		t_wjglDto = wjqxService.getPermissionStr(t_wjglDto, request);
		
		//扩展字段显示名称设置
		if(t_wjglDto!=null) {
			JcsjDto fl_jcsjDto=jcsjService.getDtoById(t_wjglDto.getWjfl());
			JcsjDto lb_jcsjDto=jcsjService.getDtoById(t_wjglDto.getWjlb());
			if(fl_jcsjDto!=null) {
				String cskz1=fl_jcsjDto.getCskz1();
				String szlb="document.type."+cskz1;
				List<XtszDto> xtszlist=xtszService.getObscureDto(szlb);
				if(!CollectionUtils.isEmpty(xtszlist)){
					for (XtszDto xtszDto : xtszlist) {
						if ("cskz1".equals(xtszDto.getSzz())) {
							t_wjglDto.setCskz1mc(xtszDto.getSzmc());
						} else if ("cskz2".equals(xtszDto.getSzz())) {
							t_wjglDto.setCskz2mc(xtszDto.getSzmc());
						} else if ("cskz3".equals(xtszDto.getSzz())) {
							t_wjglDto.setCskz3mc(xtszDto.getSzmc());
						}
					}
				}
			}
			if(lb_jcsjDto!=null) {
				String cskz1=lb_jcsjDto.getCskz1();
				String szlb="document.subtype."+cskz1;
				List<XtszDto> xtszlist=xtszService.getObscureDto(szlb);
				if(!CollectionUtils.isEmpty(xtszlist)){
					for (XtszDto xtszDto : xtszlist) {
						if ("cskz1".equals(xtszDto.getSzz())) {
							if (StringUtils.isBlank(t_wjglDto.getCskz1mc()))
								t_wjglDto.setCskz1mc(xtszDto.getSzmc());
						} else if ("cskz2".equals(xtszDto.getSzz())) {
							if (StringUtils.isBlank(t_wjglDto.getCskz2mc()))
								t_wjglDto.setCskz2mc(xtszDto.getSzmc());
						} else if ("cskz3".equals(xtszDto.getSzz())) {
							if (StringUtils.isBlank(t_wjglDto.getCskz3mc()))
								t_wjglDto.setCskz3mc(xtszDto.getSzmc());
						}
					}
				}
			}
		}
		mav.addObject("wjglDto", t_wjglDto);
		//查询履历
		List<WjglDto> wjglDtos = wjglService.getDtoListByGwjid(t_wjglDto);
		mav.addObject("wjglDtos", wjglDtos);
		//查询当前用户是否有下载权限
		User user = getLoginInfo(request);
		wjglDto.setJsid(user.getDqjs());
		boolean result = wjqxService.isDownload(wjglDto);
		mav.addObject("isDownload", result);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 查看任务进度
	 */
	@RequestMapping(value="/document/pagedataViewRwjd")
	public ModelAndView viewRwjd(WjglDto wjglDto){
		ModelAndView mav = new ModelAndView("production/document/document_viewRwjd");
		//根据文件ID查询工作管理信息
		List<GzglDto> gzglDtos = wjglService.selectGzglByWjid(wjglDto);
		mav.addObject("gzglDtos",gzglDtos);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	
	/**
	 * 文件新增页面
	 */
	@RequestMapping(value ="/document/addDocument")
	public ModelAndView addDocument(WjglDto wjglDto,HttpServletRequest request){
		ModelAndView mav = new ModelAndView("production/document/document_edit");
		wjglDto.setFormAction("add");
		//获取用户信息
		User user = getLoginInfo(request);
		Map<String, Object> map = commonService.getDepartmentByUser(null, user.getYhid(), request);
		@SuppressWarnings("unchecked")
		List<DepartmentDto> departmentDtos = (List<DepartmentDto>) map.get("departmentDtos");
		if(!CollectionUtils.isEmpty(departmentDtos)){
			mav.addObject("departmentDto", departmentDtos.get(0));
		}else{
			mav.addObject("departmentDto", new DepartmentDto());
		}
		List<User> xtyhlist=commonDao.getAllUserList();
		mav.addObject("xtyhlist", xtyhlist);
		//获取文件类型
		wjglDto.setBzry(user.getYhid());
		wjglDto.setYwlx(BusTypeEnum.IMP_DOCUMENT.getCode());
		mav.addObject("wjglDto", wjglDto);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DOCUMENT_TYPE});
		mav.addObject("wjfllist", jclist.get(BasicDataTypeEnum.DOCUMENT_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 文件新增保存
	 */
	@RequestMapping(value = "/document/addSaveDocument")
	@ResponseBody
	public Map<String, Object> addSaveDocument(WjglDto wjglDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		//获取用户信息
		User user = getLoginInfo(request);
		wjglDto.setLrry(user.getYhid());
		boolean isSuccess;
		try {
			isSuccess = wjglService.addSaveDocument(wjglDto);
			map.put("ywid", wjglDto.getWjid());
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("message", e.getMsg());
			return map;
		}
		map.put("status", isSuccess?"success":"fail");
		return map;
	}
	
	/**
	 * QA文件新增页面
	 */
	@RequestMapping(value ="/document/qaaddDocument")
	public ModelAndView qaaddDocument(WjglDto wjglDto, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("production/document/document_qaedit");
		wjglDto.setFormAction("qaadd");
		//获取文件类型
		wjglDto.setYwlx(BusTypeEnum.IMP_DOCUMENT.getCode());
		mav.addObject("wjglDto", wjglDto);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DOCUMENT_TYPE});
		mav.addObject("wjfllist", jclist.get(BasicDataTypeEnum.DOCUMENT_TYPE.getCode()));
		//获取权限列表
		Map<String,List<WjglDto>> permissionMap = wjqxService.getPermission(wjglDto,request);
		//查询查看权限角色
		List<WjglDto> viewList = permissionMap.get("viewList");
		mav.addObject("viewList", viewList);
		//查询学习权限角色
		List<WjglDto> studyList = permissionMap.get("studyList");
		mav.addObject("studyList", studyList);
		//查询没有学习、查看权限角色
		List<WjglDto> t_wjglList = permissionMap.get("t_wjglList");
		mav.addObject("t_wjgllist", t_wjglList);
		//查询下载权限角色
		List<WjglDto> downloadList = permissionMap.get("downloadList");
		mav.addObject("downloadList", downloadList);
		//查询没有下载权限角色
		List<WjglDto> undownloadList = permissionMap.get("undownloadList");
		mav.addObject("undownloadList", undownloadList);
		//获取用户信息
        User user = getLoginInfo(request);
        Map<String, Object> map = commonService.getDepartmentByUser(null, user.getYhid(), request);
        @SuppressWarnings("unchecked")
        List<DepartmentDto> departmentDtos = (List<DepartmentDto>) map.get("departmentDtos");
        if(!CollectionUtils.isEmpty(departmentDtos)){
            mav.addObject("departmentDto", departmentDtos.get(0));
        }else{
            mav.addObject("departmentDto", new DepartmentDto());
        }
        mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 文件qa新增保存
	 */
	@RequestMapping(value = "/document/qaaddSaveDocument")
	@ResponseBody
	public Map<String, Object> qaaddSaveDocument(WjglDto wjglDto, HttpServletRequest request){
		return this.addSaveDocument(wjglDto,request);
	}
	/**
	 * 文件修改页面
	 */
	@RequestMapping(value = "/document/modDocument")
	public ModelAndView modDocument(WjglDto wjglDto, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("production/document/document_edit");
		WjglDto t_wjglDto = wjglService.modDocument(wjglDto);
		if(t_wjglDto == null){
			mav.addObject("status", "fail");
			mav.addObject("message", xxglService.getModelById("WCOM_DOC00001").getXxnr());
			mav.addObject("wjglDto", wjglDto);
			return mav;
		}
		mav.addObject("wjglDto", t_wjglDto);
		//扩展字段显示名称设置
		JcsjDto fl_jcsjDto=jcsjService.getDtoById(t_wjglDto.getWjfl());
		JcsjDto lb_jcsjDto=jcsjService.getDtoById(t_wjglDto.getWjlb());
		if(fl_jcsjDto!=null) {
			String cskz1=fl_jcsjDto.getCskz1();
			String szlb="document.type."+cskz1;
			List<XtszDto> xtszlist=xtszService.getObscureDto(szlb);
			if(!CollectionUtils.isEmpty(xtszlist)){
				for (XtszDto xtszDto : xtszlist) {
					if ("cskz1".equals(xtszDto.getSzz())) {
						t_wjglDto.setCskz1mc(xtszDto.getSzmc());
					} else if ("cskz2".equals(xtszDto.getSzz())) {
						t_wjglDto.setCskz2mc(xtszDto.getSzmc());
					} else if ("cskz3".equals(xtszDto.getSzz())) {
						t_wjglDto.setCskz3mc(xtszDto.getSzmc());
					}
				}
			}
		}
		if(lb_jcsjDto!=null) {
			String cskz1=lb_jcsjDto.getCskz1();
			String szlb="document.subtype."+cskz1;
			List<XtszDto> xtszlist=xtszService.getObscureDto(szlb);
			if(!CollectionUtils.isEmpty(xtszlist)){
				for (XtszDto xtszDto : xtszlist) {
					if ("cskz1".equals(xtszDto.getSzz())) {
						if (StringUtils.isBlank(t_wjglDto.getCskz1mc()))
							t_wjglDto.setCskz1mc(xtszDto.getSzmc());
					} else if ("cskz2".equals(xtszDto.getSzz())) {
						if (StringUtils.isBlank(t_wjglDto.getCskz2mc()))
							t_wjglDto.setCskz2mc(xtszDto.getSzmc());
					} else if ("cskz3".equals(xtszDto.getSzz())) {
						if (StringUtils.isBlank(t_wjglDto.getCskz3mc()))
							t_wjglDto.setCskz3mc(xtszDto.getSzmc());
					}
				}
			}
		}
		//根据文件id查询所属单位信息
		WjssdwDto wjssdwDto=new WjssdwDto();
		wjssdwDto.setWjid(wjglDto.getWjid());
		WjssdwDto wjssdwxx=wjssdwService.getWjssdwByWjid(wjssdwDto);
		mav.addObject("departmentDto", wjssdwxx==null?wjssdwDto:wjssdwxx);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DOCUMENT_TYPE});
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(t_wjglDto.getWjfl());
		List<JcsjDto> wjlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		mav.addObject("wjfllist", jclist.get(BasicDataTypeEnum.DOCUMENT_TYPE.getCode()));
		mav.addObject("wjlblist", wjlbList);
		//获取权限列表
		Map<String,List<WjglDto>> permissionMap = wjqxService.getPermission(wjglDto,request);
		//查询查看权限角色
		List<WjglDto> viewList = permissionMap.get("viewList");
		mav.addObject("viewList", viewList);
		//查询学习权限角色
		List<WjglDto> studyList = permissionMap.get("studyList");
		mav.addObject("studyList", studyList);
		//查询没有学习、查看权限角色
		List<WjglDto> t_wjglList = permissionMap.get("t_wjglList");
		mav.addObject("t_wjgllist", t_wjglList);
		//查询下载权限角色
		List<WjglDto> downloadList = permissionMap.get("downloadList");
		mav.addObject("downloadList", downloadList);
		//查询没有下载权限角色
		List<WjglDto> undownloadList = permissionMap.get("undownloadList");
		mav.addObject("undownloadList", undownloadList);
		//根据文件ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = wjglService.selectFjcfbDtoByWjid(wjglDto.getWjid());
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("urlPrefix", urlPrefix);
		List<User> xtyhlist=commonDao.getAllUserList();
		mav.addObject("xtyhlist", xtyhlist);
		return mav;
	}
	/**
	 * 文件提交页面
	 */
	@RequestMapping(value = "/document/submitDocument")
	public ModelAndView submitDocument(WjglDto wjglDto, HttpServletRequest request){
		wjglDto.setLjbj("submit");
		return this.modDocument(wjglDto,request);
	}
	/**
	 * 文件新增或修订审核页面
	 */
	@RequestMapping(value = "/document/auditDocument")
	public ModelAndView auditDocument(WjglDto wjglDto, HttpServletRequest request){
		wjglDto.setLjbj("audit");
		return this.modDocument(wjglDto,request);
	}
	/**
	 * 文件高级修改页面
	 */
	@RequestMapping(value = "/document/advancedmodDocument")
	public ModelAndView advancedmodDocument(WjglDto wjglDto, HttpServletRequest request){
		wjglDto.setLjbj("advancedmod");
		return this.modDocument(wjglDto,request);
	}
	/**
	 * 文件修改页面(审核时间)
	 */
	@RequestMapping(value = "/document/commonModDocumentAudit")
	public ModelAndView modDocumentAudit(WjglDto wjglDto, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("production/document/document_editAudit");
		WjglDto t_wjglDto = wjglService.modDocument(wjglDto);
		if(t_wjglDto == null){
			mav.addObject("status", "fail");
			mav.addObject("message", xxglService.getModelById("WCOM_DOC00001").getXxnr());
			mav.addObject("wjglDto", wjglDto);
			return mav;
		}
		mav.addObject("wjglDto", t_wjglDto);
		//根据文件id查询所属单位信息
		WjssdwDto wjssdwDto=new WjssdwDto();
		wjssdwDto.setWjid(wjglDto.getWjid());
		WjssdwDto wjssdwxx=wjssdwService.getWjssdwByWjid(wjssdwDto);
		mav.addObject("departmentDto", wjssdwxx==null?wjssdwDto:wjssdwxx);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DOCUMENT_TYPE});
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(t_wjglDto.getWjfl());
		List<JcsjDto> wjlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		mav.addObject("wjfllist", jclist.get(BasicDataTypeEnum.DOCUMENT_TYPE.getCode()));
		mav.addObject("wjlblist", wjlbList);
		//获取权限列表
		Map<String,List<WjglDto>> permissionMap = wjqxService.getPermission(wjglDto,request);
		//查询查看权限角色
		List<WjglDto> viewList = permissionMap.get("viewList");
		mav.addObject("viewList", viewList);
		//查询学习权限角色
		List<WjglDto> studyList = permissionMap.get("studyList");
		mav.addObject("studyList", studyList);
		//查询没有学习、查看权限角色
		List<WjglDto> t_wjglList = permissionMap.get("t_wjglList");
		mav.addObject("t_wjgllist", t_wjglList);
		//查询下载权限角色
		List<WjglDto> downloadList = permissionMap.get("downloadList");
		mav.addObject("downloadList", downloadList);
		//查询没有下载权限角色
		List<WjglDto> undownloadList = permissionMap.get("undownloadList");
		mav.addObject("undownloadList", undownloadList);
		//根据文件ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = wjglService.selectFjcfbDtoByWjid(wjglDto.getWjid());
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 文件修改保存
	 */
	@RequestMapping(value = "/document/modSaveDocument")
	@ResponseBody
	public Map<String, Object> modSaveDocument(WjglDto wjglDto, HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		wjglDto.setXgry(user.getYhid());
		wjglDto.setZsxm(user.getZsxm());
		boolean isSuccess = wjglService.modSaveDocument(wjglDto, request);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid", wjglDto.getWjid());
		return map;
	}
	/**
	 * 文件提交保存
	 */
	@RequestMapping(value = "/document/submitSaveDocument")
	@ResponseBody
	public Map<String, Object> submitSaveDocument(WjglDto wjglDto, HttpServletRequest request){
		return this.modSaveDocument(wjglDto,request);
	}
	/**
	 * 文件高级修改保存
	 */
	@RequestMapping(value = "/document/advancedmodSaveDocument")
	@ResponseBody
	public Map<String, Object> advancedmodSaveDocument(WjglDto wjglDto, HttpServletRequest request){
		return this.modSaveDocument(wjglDto,request);
	}
	/**
	 * 文件新增或修订审核保存
	 */
	@RequestMapping(value = "/document/auditSaveDocument")
	@ResponseBody
	public Map<String, Object> auditSaveDocument(WjglDto wjglDto, HttpServletRequest request){
		return this.modSaveDocument(wjglDto,request);
	}
	/**
	 * 文件删除
	 */
	@RequestMapping(value = "/document/delDocument")
	@ResponseBody
	public Map<String, Object> delDocument(WjglDto wjglDto, HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		wjglDto.setScry(user.getYhid());
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess = wjglService.deleteDto(wjglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	/**
	 * 文件废弃
	 */
	@RequestMapping(value = "/document/discardDocument")
	@ResponseBody
	public Map<String, Object> discardDocument(WjglDto wjglDto, HttpServletRequest request){
		return this.delDocument(wjglDto,request);
	}
	/**
	 * 文件更新时间页面
	 */
	@RequestMapping(value="document/timeUpdateDocument")
	public ModelAndView timeUpdateDocument(WjglDto wjglDto, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("production/document/document_modtime");
		WjglDto t_wjglDto = wjglService.getDtoById(wjglDto.getWjid(),request);
		t_wjglDto.setPresfth(wjglDto.getPresfth());
		t_wjglDto.setPrezhgz(wjglDto.getPrezhgz());
		mav.addObject("wjglDto", t_wjglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 文件更新时间保存
	 */
	@RequestMapping(value = "/document/timeSaveUpdateDocument")
	@ResponseBody
	public Map<String, Object> modSaveDocumentTime(WjglDto wjglDto, HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		wjglDto.setXgry(user.getYhid());
		boolean isSuccess = wjglService.updateTimeByWjid(wjglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid", wjglDto.getWjid());
		map.put("wjglDto", wjglDto);
		return map;
	}
	
	/**
	 * 文件修改提交页面
	 */
	@RequestMapping(value = "/document/reviseDocumentView")
	public ModelAndView reviseDocumentView(WjglDto wjglDto, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("production/document/document_modSubmit");
		mav.addObject("urlPrefix", urlPrefix);
        /*//根据文件id查询所属单位信息
        WjssdwDto wjssdwDto=new WjssdwDto();
        wjssdwDto.setWjid(wjglDto.getWjid());
        WjssdwDto wjssdwxx=wjssdwService.getWjssdwByWjid(wjssdwDto);
        mav.addObject("departmentDto", wjssdwxx);*/
        //获取用户信息
  		User user = getLoginInfo(request);
  		Map<String, Object> map = commonService.getDepartmentByUser(null, user.getYhid(), request);
  		@SuppressWarnings("unchecked")
  		List<DepartmentDto> departmentDtos = (List<DepartmentDto>) map.get("departmentDtos");
  		if(!CollectionUtils.isEmpty(departmentDtos)){
  			mav.addObject("departmentDto", departmentDtos.get(0));
  		}else{
  			mav.addObject("departmentDto", new DepartmentDto());
  		}
		//获得文件管理表Dto
		WjglDto t_wjglDto = wjglService.getDtoById(wjglDto.getWjid(),request);
		//获取文件类型
		t_wjglDto.setYwlx(BusTypeEnum.IMP_DOCUMENT.getCode());
		mav.addObject("wjglDto", t_wjglDto);
		//判断关联文件是否为空
		if(!StringUtil.isBlank(t_wjglDto.getGlwjid())){
			mav.addObject("status", "fail");
			mav.addObject("message", xxglService.getModelById("WCOM_DOC00001").getXxnr());
			return mav;
		}
		//判断文件状态
		if(StatusEnum.CHECK_SUBMIT.getCode().equals(t_wjglDto.getZt())){
			mav.addObject("status", "fail");
			mav.addObject("message", xxglService.getModelById("WCOM_WAL00001").getXxnr());
			return mav;
		}else if(StatusEnum.CHECK_NO.getCode().equals(t_wjglDto.getZt())){
			mav.addObject("status", "fail");
			mav.addObject("message", xxglService.getModelById("WCOM_WAL00002").getXxnr());
			return mav;
		}else if(StatusEnum.CHECK_UNPASS.getCode().equals(t_wjglDto.getZt())){
			mav.addObject("status", "fail");
			mav.addObject("message", xxglService.getModelById("WCOM_WAL00003").getXxnr());
			return mav;
		}
		
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DOCUMENT_TYPE});
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(t_wjglDto.getWjfl());
		List<JcsjDto> wjlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		mav.addObject("wjfllist", jclist.get(BasicDataTypeEnum.DOCUMENT_TYPE.getCode()));
		mav.addObject("wjlblist", wjlbList);
		//获取权限列表
		Map<String,List<WjglDto>> permissionMap = wjqxService.getPermission(wjglDto,request);
		//查询查看权限角色
		List<WjglDto> viewList = permissionMap.get("viewList");
		mav.addObject("viewList", viewList);
		//查询学习权限角色
		List<WjglDto> studyList = permissionMap.get("studyList");
		mav.addObject("studyList", studyList);
		//查询没有学习、查看权限角色
		List<WjglDto> t_wjglList = permissionMap.get("t_wjglList");
		mav.addObject("t_wjgllist", t_wjglList);
		//查询下载权限角色
		List<WjglDto> downloadList = permissionMap.get("downloadList");
		mav.addObject("downloadList", downloadList);
		//查询没有下载权限角色
		List<WjglDto> undownloadList = permissionMap.get("undownloadList");
		mav.addObject("undownloadList", undownloadList);
		//根据文件ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = wjglService.selectFjcfbDtoByWjid(wjglDto.getWjid());
		mav.addObject("fjcfbDtos",fjcfbDtos);
		return mav;
	}
	
	/**
	 * 文件修改提交保存
	 */
	@RequestMapping(value = "/document/reviseSaveDocumentView")
	@ResponseBody
	public Map<String, Object> modSaveDocumentView(WjglDto wjglDto, HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		wjglDto.setLrry(user.getYhid());
		wjglDto.setBzry(user.getYhid());
		boolean isSuccess = true;
		WjglDto t_wjgllsbDto = wjglService.modSaveDocumentView(wjglDto);
		Map<String,Object> map = new HashMap<>();
		if(t_wjgllsbDto == null){
			isSuccess = false;
		}else{
			map.put("ywid", t_wjgllsbDto.getWjid());
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 文件申请审核列表页面
	 */
	@RequestMapping(value ="/document/pageListDocumentAudit")
	public ModelAndView pageListDocumentAudit(){
		ModelAndView mav = new ModelAndView("production/document/document_listaudit");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{
				BasicDataTypeEnum.DOCUMENT_TYPE});
		mav.addObject("wjfllist", jclist.get(BasicDataTypeEnum.DOCUMENT_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 文件申请审核列表
	 */
	@RequestMapping(value ="/document/pageGetListDocumentAudit")
	@ResponseBody
	public Map<String,Object> pageGetListDocumentAudit(WjglDto wjglDto,HttpServletRequest request){
		
		//附加委托参数
		DataPermission.addWtParam(wjglDto);
		//附加审核状态过滤
		if(GlobalString.AUDIT_SHZT_YSH.equals(wjglDto.getDqshzt())){
			DataPermission.add(wjglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "wj", "wjid", AuditTypeEnum.QUALITY_FILE_ADD);
		}else{
			DataPermission.add(wjglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "wj", "wjid", AuditTypeEnum.QUALITY_FILE_ADD);
		}
		DataPermission.addCurrentUser(wjglDto, getLoginInfo(request));
		DataPermission.addSpDdw(wjglDto, "wj", SsdwTableEnum.WJGL);
		
		List<WjglDto> t_List = wjglService.getPagedAuditList(wjglDto);
		
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", wjglDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 文件修改审核页面
	 */
	@RequestMapping(value = "/document/pageListModDocumentAudit")
	public ModelAndView pageListModDocumentAudit(){
		ModelAndView mav = new ModelAndView("production/document/document_listmodaudit");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{
				BasicDataTypeEnum.DOCUMENT_TYPE});
		mav.addObject("wjfllist", jclist.get(BasicDataTypeEnum.DOCUMENT_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 文件修改审核列表
	 */
	@RequestMapping(value = "/document/pageGetListModDocumentAudit")
	@ResponseBody
	public Map<String, Object> pageGetListModDocumentAudit(WjglDto wjglDto,HttpServletRequest request){
		//附加委托参数
		DataPermission.addWtParam(wjglDto);
		//附加审核状态过滤
		if(GlobalString.AUDIT_SHZT_YSH.equals(wjglDto.getDqshzt())){
			DataPermission.add(wjglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "wj", "wjid", AuditTypeEnum.QUALITY_FILE_MOD);
		}else{
			DataPermission.add(wjglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "wj", "wjid", AuditTypeEnum.QUALITY_FILE_MOD);
		}
		DataPermission.addSpDdw(wjglDto, "wj", SsdwTableEnum.WJGL);
		DataPermission.addCurrentUser(wjglDto, getLoginInfo(request));
		List<WjglDto> t_List = wjglService.getPagedAuditList(wjglDto);
		
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", wjglDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}

	/**
	 * 文件权限批量修改页面
	 */
	@RequestMapping(value="/document/batchpermitDocument")
	public ModelAndView batchpermitDocument(WjglDto wjglDto, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("production/document/document_batchPermit");
		mav.addObject("wjglDto", wjglDto);
		//获取权限列表
		Map<String,List<WjglDto>> permissionMap = wjqxService.getPermission(wjglDto,request);
		//查询查看权限角色
		List<WjglDto> viewList = permissionMap.get("viewList");
		mav.addObject("viewList", viewList);
		//查询学习权限角色
		List<WjglDto> studyList = permissionMap.get("studyList");
		mav.addObject("studyList", studyList);
		//查询没有学习、查看权限角色
		List<WjglDto> t_wjglList = permissionMap.get("t_wjglList");
		mav.addObject("t_wjgllist", t_wjglList);
		//查询下载权限角色
		List<WjglDto> downloadList = permissionMap.get("downloadList");
		mav.addObject("downloadList", downloadList);
		//查询没有下载权限角色
		List<WjglDto> undownloadList = permissionMap.get("undownloadList");
		mav.addObject("undownloadList", undownloadList);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 文件权限批量修改保存
	 */
	@RequestMapping(value = "/document/batchpermitSaveDocument")
	@ResponseBody
	public Map<String, Object> batchpermitSaveDocument(WjglDto wjglDto, HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		wjglDto.setXgry(user.getYhid());
		wjglDto.setZsxm(user.getZsxm());
		boolean isSuccess = wjglService.batchPermitSaveDocument(wjglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid", wjglDto.getWjid());
		return map;
	}
	
	/**
	 * 转换文件格式
	 */
	@RequestMapping(value="/document/transformDocument")
	public ModelAndView transformDocument(WjglDto wjglDto, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("production/document/document_transform");
		wjglDto.setFormAction("view");
		mav.addObject("wjglaction",wjglDto);
		//查询附件
		List<FjcfbDto> fjcfbDtos = wjglService.selectFjcfbDtoByWjid(wjglDto.getWjid());
		mav.addObject("fjcfbDtos",fjcfbDtos);
		WjglDto t_wjglDto = wjglService.getDtoById(wjglDto.getWjid());
		t_wjglDto.setStudyjsmc(wjqxService.SelectStudyJs(wjglDto));
		t_wjglDto.setViewjsmc(wjqxService.SelectViewJs(wjglDto));
		t_wjglDto.setDownloadjsmc(wjqxService.SelectDownloadJs(wjglDto));
		mav.addObject("wjglDto", t_wjglDto);
		//查询当前用户是否有下载权限
		User user = getLoginInfo(request);
		wjglDto.setJsid(user.getDqjs());
		boolean result = wjqxService.isDownload(wjglDto);
		mav.addObject("isDownload", result);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 转换文件格式
	 */
	@RequestMapping("/document/transformSaveDocument")
	@ResponseBody
	public Map<String,Object> transferSaveDocument(WjglDto wjglDto){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess =wjglService.transferDocument(wjglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM99015").getXxnr():xxglService.getModelById("ICOM99016").getXxnr());
		return map;
	}
	
	/**
	 * 原文件下载
	 */
	@RequestMapping(value="/document/predownloadDocument")
	public ModelAndView predownloadDocument(WjglDto wjglDto){
		ModelAndView mav = new ModelAndView("production/document/document_predownload");
		wjglDto.setFormAction("view");
		mav.addObject("wjglaction",wjglDto);
		//查询附件
		List<FjcfbDto> fjcfbDtos = wjglService.selectFjcfbDtoByWjid(wjglDto.getWjid());
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("urlPrefix", urlPrefix);
		//查询当前用户是否有下载权限
		/*User user = getLoginInfo(productionPrefix, request);
		wjglDto.setJsid(user.getDqjs());
		boolean result = wjqxService.isDownload(wjglDto);
		mav.addObject("isDownload", result);*/
		return mav;
	}
	
	/**
	 * 文件导入页面
	 */
	@RequestMapping(value ="/document/pageImportDocument")
	public ModelAndView pageImportDocument(){
		ModelAndView mav = new ModelAndView("production/document/document_import");
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_DOCUMENT_BATCH.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 跳转文件列表
	 */
	@RequestMapping(value = "/document/reldocumentPage")
	public ModelAndView reldocumentPage(WjglDto wjglDto) {
		ModelAndView mav=new ModelAndView("production/document/document_related");
		wjglDto = wjglService.getDto(wjglDto);
		mav.addObject("wjglDto", wjglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 文件列表
	 */
	@RequestMapping(value="/document/pagedataGetListDocumentRE")
	@ResponseBody
	public Map<String,Object> getPagedListDocument(WjglDto wjglDto){
		Map<String,Object> map = new HashMap<>();
		List<WjglDto> t_List = wjglService.getPagedListDocument(wjglDto);
		//Json格式的要求{total:22,rows:{}} 构造成Json的格式传递
		map.put("total", wjglDto.getTotalNumber());
		map.put("rows", t_List);
		return map;
	}
	
	/**
	 * 修改关联关系
	 */
	@RequestMapping("/document/reldocumentSavePage")
	@ResponseBody
	public Map<String,Object> updateRelated(WjglDto wjglDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		//获取用户信息
		User user = getLoginInfo(request);
		wjglDto.setXgry(user.getYhid());
		boolean isSuccess =wjglService.updateRelated(wjglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM99015").getXxnr():xxglService.getModelById("ICOM99016").getXxnr());
		return map;
	}
	
	/**
	 * 角色文件权限设置
	 */
	@RequestMapping("/document/roleAuthoritySetting")
	public ModelAndView roleAuthoritySetting(WjglDto wjglDto) {
		ModelAndView mav=new ModelAndView("production/document/document_roleAuthority");
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, Object> xtjsparamMap = new LinkedMultiValueMap<>();
		xtjsparamMap.add("access_token", wjglDto.getAccess_token());
		String xtjsurl=applicationurl+"/systemrole/role/getDtoListxtjs";
		@SuppressWarnings("unchecked")
		List<Object> xtjslist=restTemplate.postForObject(xtjsurl, xtjsparamMap, List.class);
		mav.addObject("xtjslist", xtjslist);
		return mav;
	}
	
	/**
	 * 获取扩展字段
	 */
	@RequestMapping("/document/pagedataGetExtendField")
	@ResponseBody
	public Map<String,Object> getExtendField(WjglDto wjglDto){
		Map<String,Object> map=new HashMap<>();
		//文件分类
		if(StringUtils.isNotBlank(wjglDto.getWjfl())) {
			JcsjDto wjfl_jcsjDto=jcsjService.getDtoById(wjglDto.getWjfl());
			if(wjfl_jcsjDto!=null) {
				String cskz1=wjfl_jcsjDto.getCskz1();
				//组装
				if(StringUtils.isNotBlank(cskz1)) {
					String szlb="document.type."+cskz1;
					List<XtszDto> xtszlist=xtszService.getObscureDto(szlb);
					map.put("wjflkzlist", xtszlist);
				}
			}
		}
		
		//文件类别
		if(StringUtils.isNotBlank(wjglDto.getWjlb())) {
			JcsjDto wjlb_jcsjDto=jcsjService.getDtoById(wjglDto.getWjlb());
			if(wjlb_jcsjDto!=null) {
				String cskz1=wjlb_jcsjDto.getCskz1();
				//组装
				if(StringUtils.isNoneBlank(cskz1)) {
					String szlb="document.subtype."+cskz1;
					List<XtszDto> xtszlist=xtszService.getObscureDto(szlb);
					map.put("wjlbkzlist", xtszlist);
				}
			}
		}
		return map;
	}
	/**
	 * 打印发放页面
	 */
	@RequestMapping(value ="/document/printgrantDocument")
	public ModelAndView printgrantDocument(FfjlDto ffjlDto){
		ModelAndView mav = new ModelAndView("production/document/document_printgrant");
		mav.addObject("ffjlDto", ffjlDto);
		mav.addObject("nowdate", DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 *  打印发放保存
	 */
	@RequestMapping("/document/printgrantSaveDocument")
	@ResponseBody
	public Map<String,Object> printgrantSaveDocument(FfjlDto ffjlDto){
		Map<String,Object> map=new HashMap<>();
		boolean isSuccess;
		//保存到货信息
		try {
			isSuccess=ffjlService.printgrantSaveDocument(ffjlDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 获取发放记录信息
	 */
	@RequestMapping("/document/pagedataGetPrintgrantDocumentList")
	@ResponseBody
	public Map<String,Object> pagedataGetPrintgrantDocumentList(FfjlDto ffjlDto){
		Map<String,Object> map=new HashMap<>();
		List<FfjlDto> dtoList = ffjlService.getDtoList(ffjlDto);
		map.put("rows",dtoList);
		return map;
	}
	/**
	 * 选择文件列表页面
	 */
	@RequestMapping("/document/pagedataListDocument")
	public ModelAndView pagedataListDocument(WjglDto wjglDto) {
		ModelAndView mav = new ModelAndView("production/document/document_choose");
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("wjglDto",wjglDto);
		return mav;
	}
	/**
	 * 选择文件列表数据
	 */
	@RequestMapping("/document/pagedataGetListDocument")
	@ResponseBody
	public Map<String, Object> pagedataGetListDocument(WjglDto wjglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		wjglDto.setJsid(user.getDqjs());
		wjglDto.setYhid(user.getYhid());
		//获取用户信息
		List<WjglDto> t_List = wjglService.getPagedDtoList(wjglDto);
		//构造成Json的格式传递
		map.put("total", wjglDto.getTotalNumber());
		map.put("rows", t_List);
		return map;
	}
	/**
	 * 获取文件详细信息
	 */
	@RequestMapping("/document/pagedataGetDocumentDetails")
	@ResponseBody
	public Map<String, Object> pagedataGetDocumentDetails(WjglDto wjglDto) {
		Map<String, Object> map = new HashMap<>();
		List<WjglDto> wjglDtos = wjglService.getDtoByIds(wjglDto);
		map.put("wjglDtos", wjglDtos);
		return map;
	}
	/**
	 * 获取关联文件
	 */
	@RequestMapping("/document/pagedataGetGlWj")
	@ResponseBody
	public Map<String, Object> pagedataGetGlWj(GlwjxxDto glwjxxDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(glwjxxDto.getWjid())){
			List<GlwjxxDto> rows = glwjxxService.getDtoList(glwjxxDto);
			map.put("rows", rows);
		}else {
			map.put("rows", new ArrayList<>());
		}
		return map;
	}

	/**
	 * @Description: 批量导入文件
	 * @param wjglDto
	 * @param request
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2024/12/5 14:51
	 */
	@RequestMapping(value ="/document/bulkimportsDocument")
	public ModelAndView bulkimportsDocument(WjglDto wjglDto,HttpServletRequest request){
		ModelAndView mav = new ModelAndView("production/document/document_bulkimports");
		//获取用户信息
		User user = getLoginInfo(request);
		Map<String, Object> map = commonService.getDepartmentByUser(null, user.getYhid(), request);
		@SuppressWarnings("unchecked")
		List<DepartmentDto> departmentDtos = (List<DepartmentDto>) map.get("departmentDtos");
		if(!CollectionUtils.isEmpty(departmentDtos)){
			mav.addObject("departmentDto", departmentDtos.get(0));
		}else{
			mav.addObject("departmentDto", new DepartmentDto());
		}
		List<User> xtyhlist=commonDao.getAllUserList();
		mav.addObject("xtyhlist", xtyhlist);
		//获取文件类型
		wjglDto.setBzry(user.getYhid());
		wjglDto.setYwlx(BusTypeEnum.IMP_IMPORTDOCUMENT.getCode());
		mav.addObject("wjglDto", wjglDto);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DOCUMENT_TYPE});
		mav.addObject("wjfllist", jclist.get(BasicDataTypeEnum.DOCUMENT_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	@RequestMapping(value = "/document/bulkimportsSaveDocument")
	@ResponseBody
	public Map<String, Object> bulkimportsSaveDocument(WjglDto wjglDto, HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		wjglDto.setLrry(user.getYhid());
		Map<String,Object> map = new HashMap<>();
		try {
			Map<String,Object> resultMap = wjglService.bulkimportsSaveDocument(wjglDto);
			if(!CollectionUtils.isEmpty(resultMap)){
				map.put("urlPrefix",urlPrefix);
				map.put("ywids",resultMap.get("ywids"));
				map.put("auditType",AuditTypeEnum.QUALITY_FILE_ADD.getCode());
				map.put("status", "success");
				map.put("message", xxglService.getModelById("ICOM00001").getXxnr());
			}else {
				map.put("status", "fail");
				map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * @Description: 资料预览
	 * @param wjglDto
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2025/3/11 16:07
	 */
	@RequestMapping("/document/pagedataGetUploadFile")
	public ModelAndView getUploadFilePage(WjglDto wjglDto) {
		ModelAndView mav=new ModelAndView("production/document/document_viewFile");
		List<FjcfbDto> fjcfbDtos = wjglService.selectFjcfbDtoByWjid(wjglDto.getWjid());
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
}
