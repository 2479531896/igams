package com.matridx.igams.common.controller;

import com.matridx.igams.common.dao.entities.AuditResult;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.dao.entities.XxglModel;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.ISpgwcyService;
import com.matridx.igams.common.service.svcinterface.IXtshService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/systemcheck")
public class CheckControl extends BaseController{
	
	private final Logger logger = LoggerFactory.getLogger(CheckControl.class);

//	public static final String CHECK = "check";
	public static final String MSG = "message";	//返回信息
	public static final String RESULT = "result";	//执行结果
	
	@Autowired
	private IShgcService shgcService;
	@Autowired
	private IXxglService xxglService;
	@Autowired
	private IShlcService shlcService;
	@Autowired
	private IShxxService shxxService;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private ISpgwcyService spgwcyService;
	@Autowired
	private IXtshService xtshService;

	
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	
	/**
	 * 提交审核
	 */
	@RequestMapping(value ="/auditProcess/commCommitAudit")
	@ResponseBody
	public Map<String,Object> commCommitAudit(ShgcDto shgcDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		Map<String, Object> result = new HashMap<>();
		try{
			result = shgcService.checkAndCommit(shgcDto,user);
		} catch (BusinessException e) {
			logger.error(e.toString());
			result.put("status", "fail");
			result.put("message", xxglService.getModelById("ICOM99016").getXxnr() + (StringUtil.isBlank(e.getMsg())?"":e.getMsg()));
		} catch (Exception e) {
			if (shgcDto.getYwids() != null) {
				logger.error("Ywids: " + shgcDto.getYwids().toString());
			}
			logger.error("commCommitAudit Exception:" + e.toString());
			result.put("status", "fail");
			result.put("message",  xxglService.getModelById("ICOM99016").getXxnr());
		}
		
		return result;
	}
	
	/**
	 * 显示审核页面
	 */
	@RequestMapping(value ="/auditProcess/audit")
	public ModelAndView audit(ShgcDto shgcDto){
		ModelAndView mav = new ModelAndView("globalweb/systemcheck/audit_process");
		// 获取当前审核过程
		ShgcDto t_shgcDto = shgcService.getDtoByYwid(shgcDto.getYwid());
		mav.addObject("urlPrefix", urlPrefix);
		if(t_shgcDto == null)
			return mav;
		//审核业务页面
		t_shgcDto.setBusiness_url(shgcDto.getBusiness_url());
		t_shgcDto.setYwzd(shgcDto.getYwzd());
		// 获取的审核流程列表
		ShlcDto shlcParam = new ShlcDto();
		shlcParam.setShid(t_shgcDto.getShid());
		shlcParam.setGcid(t_shgcDto.getGcid());// 处理旧流程判断用
		List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
		// 退回列表
		List<ShlcDto> backShlcList = new ArrayList<>();
		ShlcDto startLc = new ShlcDto();
		startLc.setGwmc("用户");
		startLc.setLcxh("-1");
		backShlcList.add(startLc);
		for (ShlcDto shlc : shlcList) {
			// 当流程序号小于现流程序号则放入退回列表
			if (t_shgcDto.getXlcxh() != null
					&& Integer.parseInt(shlc.getLcxh()) < Integer.parseInt(t_shgcDto.getXlcxh())) {
				shlc.setAudited(true);
				backShlcList.add(shlc);
			} else if (shlc.getLcxh().equals(t_shgcDto.getXlcxh())) {// 相等则是当前流程
				// 当前流程做标记
				shlc.setCurrent(true);// 当前流程
				shlc.setAudited(true); //已审核流程
				// 最后一步标志
				if (t_shgcDto.getXlcxh() != null
						&& shlcList.get(shlcList.size() - 1).getLcxh().equals(t_shgcDto.getXlcxh())) {
					t_shgcDto.setLastStep(true);
				}
				//判断如果流程类别不为空，则代表需要显示特殊页面
				if(StringUtil.isNotBlank(shlc.getLclbcskz1())) {
					JcsjDto jcsjDto = jcsjService.getDtoById(shlc.getLclb());
					//审核业务页面
					if (jcsjDto!=null){
						t_shgcDto.setBusiness_url(urlPrefix+jcsjDto.getCskz1());
					}
				}
				break;// 跳出for循环
			}
		}
		ShxxDto shxxParam = new ShxxDto();
		shxxParam.setShlb(shgcDto.getShlb());
		shxxParam.setYwid(shgcDto.getYwid());
		List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
		mav.addObject("shgcDto", t_shgcDto);
		mav.addObject("backShlcList", backShlcList);
		mav.addObject("shlcList", shlcList);
		mav.addObject("shxxList", shxxList);
		return mav;
	}
	/**
	 * 系统审核选择页面
	 */
	@RequestMapping(value ="/auditProcess/chooseFlow")
	public ModelAndView chooseFlow(ShgcDto shgcDto){
		ModelAndView mav = new ModelAndView("globalweb/systemcheck/audit_chooseFlow");
		XtshDto xtshDto=new XtshDto();
		xtshDto.setShlb(shgcDto.getShlb());
		List<XtshDto> xtshDtoList=xtshService.getDtoList(xtshDto);
		mav.addObject("xtshDtoList",xtshDtoList);
		mav.addObject("shgcDto",shgcDto);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	/**
	 * 获取审核页面数据
	 */
	@PostMapping(value ="/auditProcess/minidataAudit")
	@ResponseBody
	public Map<String,Object> minidataAudit(ShgcDto shgcDto) {
		// 获取当前审核过程
		Map<String,Object> result = new HashMap<>();
		ShgcDto t_shgcDto = shgcService.getDtoByYwid(shgcDto.getYwid());
		result.put("urlPrefix", urlPrefix);
		if(t_shgcDto == null){
			return result;
		}
		//审核业务页面
		t_shgcDto.setBusiness_url(shgcDto.getBusiness_url());
		t_shgcDto.setYwzd(shgcDto.getYwzd());
		// 获取的审核流程列表
		ShlcDto shlcParam = new ShlcDto();
		shlcParam.setShid(t_shgcDto.getShid());
		shlcParam.setGcid(t_shgcDto.getGcid());// 处理旧流程判断用
		List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
		// 退回列表
		List<ShlcDto> backShlcList = new ArrayList<>();
		ShlcDto startLc = new ShlcDto();
		startLc.setGwmc("用户");
		startLc.setLcxh("-1");
		backShlcList.add(startLc);
		for (ShlcDto shlc : shlcList) {
			// 当流程序号小于现流程序号则放入退回列表
			if (t_shgcDto.getXlcxh() != null
					&& Integer.parseInt(shlc.getLcxh()) < Integer.parseInt(t_shgcDto.getXlcxh())) {
				shlc.setAudited(true);
				backShlcList.add(shlc);
			} else if (shlc.getLcxh().equals(t_shgcDto.getXlcxh())) {// 相等则是当前流程
				// 当前流程做标记
				shlc.setCurrent(true);// 当前流程
				shlc.setAudited(true); //已审核流程
				// 最后一步标志
				if (t_shgcDto.getXlcxh() != null
						&& shlcList.get(shlcList.size() - 1).getLcxh().equals(t_shgcDto.getXlcxh())) {
					t_shgcDto.setLastStep(true);
				}
				//判断如果流程类别不为空，则代表需要显示特殊页面
				if(StringUtil.isNotBlank(shlc.getLclb())) {
					JcsjDto jcsjDto = jcsjService.getDtoById(shlc.getLclb());
					//审核业务页面
					if (jcsjDto!=null){
						t_shgcDto.setBusiness_url(urlPrefix+jcsjDto.getCskz1());
						t_shgcDto.setBusiness_url2(urlPrefix+jcsjDto.getCskz2());
					}
				}
				break;// 跳出for循环
			}
		}
		ShxxDto shxxParam = new ShxxDto();
		shxxParam.setShlb(shgcDto.getShlb());
		shxxParam.setYwid(shgcDto.getYwid());
		List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
		result.put("shgcDto", t_shgcDto);
		result.put("backShlcList", backShlcList);
		result.put("shlcList", shlcList);
		result.put("shxxList", shxxList);
		return result;
	}
	/**
	 * 审核保存
	 */
	@RequestMapping(value ="/auditProcess/judgment")
	@ResponseBody
	public Map<String, Object> judgment(ShxxDto shxxDto) {
		Map<String, Object> data = new HashMap<>();
		List<ShgcDto> dtoByYwids = shgcService.getDtoByYwids(shxxDto.getYwids());
		if (null !=dtoByYwids && dtoByYwids.size()==1){
			data.put("status","true");
		}else{
			data.put("status","fail");
			data.put("message","所选数据存在流程不一样的数据！");
		}
		return data;
	}
	/**
	 * 显示批量审核页面
	 */
	@RequestMapping(value ="/auditProcess/batchAuditInfo")
	public ModelAndView batchAuditInfo(ShgcDto shgcDto){
		ModelAndView mav = new ModelAndView("globalweb/systemcheck/audit_process");
		// 获取当前审核过程
		ShgcDto t_shgcDto = shgcService.getDtoByYwid(shgcDto.getYwids().get(0));
		mav.addObject("urlPrefix", urlPrefix);
		if(t_shgcDto == null)
			return mav;
		//审核业务页面
		t_shgcDto.setBusiness_url(shgcDto.getBusiness_url());
		t_shgcDto.setYwzd(shgcDto.getYwzd());
		// 获取的审核流程列表
		ShlcDto shlcParam = new ShlcDto();
		shlcParam.setShid(t_shgcDto.getShid());
		shlcParam.setGcid(t_shgcDto.getGcid());// 处理旧流程判断用
		List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
		// 退回列表
		List<ShlcDto> backShlcList = new ArrayList<>();
		ShlcDto startLc = new ShlcDto();
		startLc.setGwmc("用户");
		startLc.setLcxh("-1");
		backShlcList.add(startLc);
		for (ShlcDto shlc : shlcList) {
			//判断如果流程类别不为空，则代表需要显示特殊页面，不能进行批量审核
			if(StringUtil.isNotBlank(shlc.getLclb()) && shlc.getLcxh().equals(t_shgcDto.getXlcxh())) {
				mav.addObject("isBatch", "false");
				break;
			}
			// 当流程序号小于现流程序号则放入退回列表
			if (t_shgcDto.getXlcxh() != null
					&& Integer.parseInt(shlc.getLcxh()) < Integer.parseInt(t_shgcDto.getXlcxh())) {
				shlc.setAudited(true);
				backShlcList.add(shlc);
			} else if (shlc.getLcxh().equals(t_shgcDto.getXlcxh())) {// 相等则是当前流程
				// 当前流程做标记
				shlc.setCurrent(true);// 当前流程
				shlc.setAudited(true); //已审核流程
				// 最后一步标志
				if (t_shgcDto.getXlcxh() != null && shlcList.get(shlcList.size() - 1).getLcxh().equals(t_shgcDto.getXlcxh())) {
					t_shgcDto.setLastStep(true);
					break;// 跳出for循环
				}
			}
		}
		ShxxDto shxxParam = new ShxxDto();
		shxxParam.setShlb(shgcDto.getShlb());
		shxxParam.setYwid(shgcDto.getYwid());
		List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
		t_shgcDto.setYwid(null);
		mav.addObject("shgcDto", t_shgcDto);
		mav.addObject("backShlcList", backShlcList);
		mav.addObject("shlcList", shlcList);
		mav.addObject("shxxList", shxxList);
		return mav;
	}
	
	/**
	 * 审核保存
	 */
	@RequestMapping(value ="/auditProcess/auditSave")
	@ResponseBody
	public Map<String, Object> auditSave(ShxxDto shxxDto,HttpServletRequest request) {
		Map<String, Object> data = new HashMap<>();
		Boolean result = false;
		try {
			// 传入委托相关参数
			DataPermission.addWtParam(shxxDto);
			
			AuditResult auditResult = shgcService.doAudit(shxxDto, getLoginInfo(request),request);
			//完成审核逻辑后的回调处理，主要是为了跟审核的事务分开，确保数据都已经反馈到数据库中，则其他线程可以获取到数据
			// 比如发送rabbitmq 处理报告生成的时候，报告日期已经放入到数据库中
			shgcService.doBatchAuditEnd(auditResult, getLoginInfo(request),shxxDto, true);
			result = auditResult.getReturnResult();
			String msg = auditResult.getReturnMsg(xxglService.getModelById("ICOM99018").getXxnr(), xxglService.getModelById("ICOM99019").getXxnr());
			data.put("status", "success");
			data.put(MSG, msg);
			if(auditResult.getBackMap()!=null)
				data.put("backInfo", auditResult.getBackMap());
		} catch (BusinessException e) {
			logger.error(e.toString());
			String mess = e.getMsg();
			XxglModel xxglModel = xxglService.getModelById(e.getMsgId());
			String idMsg = xxglModel==null?"":xxglModel.getXxnr();
			data.put("status", "fail");
			data.put(MSG, idMsg + (StringUtil.isNotBlank(idMsg) && StringUtil.isNotBlank(mess) ? "<br/>" : "")
					+ (StringUtil.isNotBlank(mess) ? mess : ""));

		} catch (Exception e) {
			logger.error(e.toString());
			data.put("status", "fail");
			data.put(MSG, xxglService.getModelById("ICOM99019").getXxnr());
		}
		data.put(RESULT, result);
		return data;
	}
	
	/**
	 * 查看审核记录信息
	 */
	@RequestMapping(value ="/auditProcess/auditView")
	public ModelAndView auditView(ShgcDto shgcDto) {
		ModelAndView mav = new ModelAndView("globalweb/systemcheck/audit_processview");
		//User user = getLoginInfo(request);
		try {
			ShxxDto shxxParam = new ShxxDto();
			ShgcDto d_shgcDto;
			// 未传递gcid，则根据xmid和shlb获取shgc
			if (StringUtil.isBlank(shgcDto.getGcid())) {
				// 获取当前审核过程
				d_shgcDto = shgcService.getDto(shgcDto);
			} else {// 传递了gcid，则根据gcid获取shgc
					// 获取当前审核过程
				d_shgcDto = shgcService.getDtoById(shgcDto.getGcid());
			}
			if (d_shgcDto == null) {// 数据不在审核中，表示通过
				shgcDto.setXlcxh("");// 审核通过时
			} else {
				shgcDto = d_shgcDto;
			}
			shxxParam.setShlb(shgcDto.getShlb());
			shxxParam.setShlbs(shgcDto.getShlbs());
			shxxParam.setYwid(shgcDto.getYwid());
			//shxxParam.setCommit(true);// 查询提交的数据
			shxxParam.setGcid(shgcDto.getGcid());// 用过程id进行查询

			shxxParam.setSftg(null);
			shxxParam.setCommit(false); 
			List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
			if (null == shxxList) {
				shxxList = new ArrayList<>();
			}

			// 获取的审核流程列表
			ShlcDto shlcParam = new ShlcDto();
			// gcid或shid参数不全的情况（已审核完，延期审核查看会传递gcid，所以gcid和shid都需要判断），根据审核信息获取流程
			if ((StringUtil.isBlank(shgcDto.getGcid()) || StringUtil.isBlank(shgcDto.getShid()))
					&&!CollectionUtils.isEmpty(shxxList)) {
				shlcParam.setShid(shxxList.get(0).getShid());
				shlcParam.setSqsj(shxxList.get(0).getSqsj());
			} else {
				shlcParam.setShid(shgcDto.getShid());
				shlcParam.setGcid(shgcDto.getGcid());// 处理旧流程判断用
			}
			if (StringUtil.isNotBlank(shlcParam.getShid())) {
				List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
				if (StringUtil.isNotBlank(shlcParam.getGcid())) {
					if(StringUtil.isNotBlank(shgcDto.getXlcxh())){
						for (ShlcDto shlc : shlcList) {
//							if (StringUtil.isNotBlank(shlc.getWbgw()) && "TS".equals(shlc.getWbgw())){
//								ShlbDto shlbDto = new ShlbDto();
//								shlbDto.setShlb(shgcDto.getShlb());
//								ShlbDto shlbxxByShlb = shlbService.getShlbxxByShlb(shlbDto);
//								BaseBasicServiceImpl service = (BaseBasicServiceImpl)ServiceFactory.getService(shlbxxByShlb.getHdl());
//								BaseBasicModel dtoById = (BaseBasicModel)service.getDtoById(shgcDto.getYwid());
//								String str = null;
//								if (StringUtil.isNotBlank(dtoById.getDdspid())) {
//									str = dtoById.getDdspid();
//								}else if (StringUtil.isNotBlank(dtoById.getDdslid())){
//									str = dtoById.getDdslid();
//								}
//								if (StringUtil.isNotBlank(str)){
//									String tsSpr = dingTalkUtil.getTsSpr(str);
//									if (StringUtil.isNotBlank(tsSpr)&& !"null".equals(tsSpr)){
//										JSONArray jsonArray = JSONArray.parseArray(dingTalkUtil.getTsSpr(str));
//										String info = "";
//										for (Object o : jsonArray) {
//											User yhid = commonService.getYhid(String.valueOf(o));
//											info += "," + yhid.getZsxm();
//										}
//										shlc.setInfo(info.substring(1));
//									}
//								}
//							}
							if (shlc.getLcxh().equals(shgcDto.getXlcxh())) {// 相等则是当前流程
								//在当前流程处理去各个实现类中获取机构ID
								SpgwcyDto spgwcyDto = new SpgwcyDto();
								spgwcyDto.setGwid(shlc.getGwid());
								StringBuilder spgwcymcs = new StringBuilder();
								List<SpgwcyDto> spgwcyDtos = spgwcyService.getDtoList(spgwcyDto);//获取所有的审批岗位成员（此时未单位限制，取到的是所有的）

								Map<String, Object> map = shgcService.dealSpgwcy(shgcDto);//去各个实现类中获取机构ID
								if (StringUtil.isNotBlank(shgcDto.getSqbm()) || (map != null && StringUtil.isNotBlank((String)map.get("jgid")))){
									//有些审核中是进行单位限制的，有些不进行，有机构ID则单位限制过滤，无机构ID则单位权限不过滤
									spgwcyDtos = commonService.siftJgList(spgwcyDtos,StringUtil.isNotBlank(shgcDto.getSqbm())?shgcDto.getSqbm():(String) map.get("jgid"));//过滤单位限制的审批岗位成员
								}
								if (spgwcyDtos!=null&&!spgwcyDtos.isEmpty()){
									for (SpgwcyDto dto : spgwcyDtos) {
										if (spgwcymcs.indexOf(dto.getZsxm())==-1){
											spgwcymcs.append(", ").append(dto.getZsxm());
										}
									}
								}

								// 当前流程做标记
								shlc.setCurrent(true);// 当前流程
								shlc.setAudited(true); //已审核流程
								if(spgwcymcs.length() > 0)
									shlc.setSpgwcymcs(spgwcymcs.substring(1));
								break;// 跳出for循环
							}
							shlc.setAudited(true); //已审核流程
						}
					}
				}else{
					for (ShlcDto shlc : shlcList) {
						shlc.setAudited(true); //已审核流程
					}
				}
				mav.addObject("shlcList", shlcList);
			}
			mav.addObject("shxxList", shxxList);
			mav.addObject("shgcDto", shgcDto);
			mav.addObject("urlPrefix", urlPrefix);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return mav;
	}
	/**
	 * vue查看审核记录信息
	 */
	@RequestMapping(value ="/auditProcess/minidataAuditView")
	@ResponseBody
	public Map<String, Object> minidataAuditView(ShgcDto shgcDto) {
		Map<String, Object> data = new HashMap<>();
		//User user = getLoginInfo(request);
		try {
			ShxxDto shxxParam = new ShxxDto();
			ShgcDto d_shgcDto;
			// 未传递gcid，则根据xmid和shlb获取shgc
			if (StringUtil.isBlank(shgcDto.getGcid())) {
				// 获取当前审核过程
				d_shgcDto = shgcService.getDtoByYwid(shgcDto.getYwid());
			} else {// 传递了gcid，则根据gcid获取shgc
				// 获取当前审核过程
				d_shgcDto = shgcService.getDtoById(shgcDto.getGcid());
			}
			if (d_shgcDto == null) {// 数据不在审核中，表示通过
				shgcDto.setXlcxh("");// 审核通过时
			} else {
				shgcDto = d_shgcDto;
			}
			shxxParam.setShlb(shgcDto.getShlb());
			shxxParam.setShlbs(shgcDto.getShlbs());
			shxxParam.setYwid(shgcDto.getYwid());
			//shxxParam.setCommit(true);// 查询提交的数据
			shxxParam.setGcid(shgcDto.getGcid());// 用过程id进行查询

			shxxParam.setSftg(null);
			shxxParam.setCommit(false);
			List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
			if (null == shxxList) {
				shxxList = new ArrayList<>();
			}

			// 获取的审核流程列表
			ShlcDto shlcParam = new ShlcDto();
			// gcid或shid参数不全的情况（已审核完，延期审核查看会传递gcid，所以gcid和shid都需要判断），根据审核信息获取流程
			if (StringUtil.isBlank(shgcDto.getGcid()) || StringUtil.isBlank(shgcDto.getShid())) {
				shlcParam.setShid(shxxList.get(0).getShid());
				shlcParam.setSqsj(shxxList.get(0).getSqsj());
			} else {
				shlcParam.setShid(shgcDto.getShid());
				shlcParam.setGcid(shgcDto.getGcid());// 处理旧流程判断用
			}
			if (StringUtil.isNotBlank(shlcParam.getShid())) {
				List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
				if (StringUtil.isNotBlank(shlcParam.getGcid())) {
					if(StringUtil.isNotBlank(shgcDto.getXlcxh())){
						for (ShlcDto shlc : shlcList) {
							if (shlc.getLcxh().equals(shgcDto.getXlcxh())) {// 相等则是当前流程
								//在当前流程处理去各个实现类中获取机构ID
								SpgwcyDto spgwcyDto = new SpgwcyDto();
								spgwcyDto.setGwid(shlc.getGwid());
								StringBuilder spgwcymcs = new StringBuilder();
								List<SpgwcyDto> spgwcyDtos = spgwcyService.getDtoList(spgwcyDto);//获取所有的审批岗位成员（此时未单位限制，取到的是所有的）

								Map<String, Object> map = shgcService.dealSpgwcy(shgcDto);//去各个实现类中获取机构ID
								if (StringUtil.isNotBlank(shgcDto.getSqbm()) || (map != null && StringUtil.isNotBlank((String)map.get("jgid")))){
									//有些审核中是进行单位限制的，有些不进行，有机构ID则单位限制过滤，无机构ID则单位权限不过滤
									spgwcyDtos = commonService.siftJgList(spgwcyDtos,StringUtil.isNotBlank(shgcDto.getSqbm())?shgcDto.getSqbm():(String) map.get("jgid"));//过滤单位限制的审批岗位成员
								}
								if (spgwcyDtos!=null&&!spgwcyDtos.isEmpty()){
									for (SpgwcyDto dto : spgwcyDtos) {
										if (spgwcymcs.indexOf(dto.getZsxm())==-1){
											spgwcymcs.append(", ").append(dto.getZsxm());
										}
									}
								}

								// 当前流程做标记
								shlc.setCurrent(true);// 当前流程
								shlc.setAudited(true); //已审核流程
								if(spgwcymcs.length() > 0)
									shlc.setSpgwcymcs(spgwcymcs.substring(1));
								break;// 跳出for循环
							}
							shlc.setAudited(true); //已审核流程
						}
					}
				}else{
					for (ShlcDto shlc : shlcList) {
						shlc.setAudited(true); //已审核流程
					}
				}
				data.put("shlcList", shlcList);
			}
			data.put("shxxList", shxxList);
			data.put("shgcDto", shgcDto);
			data.put("urlPrefix", urlPrefix);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return data;
	}
	/**
	 * 取消审核操作
	 */
	@RequestMapping(value ="/auditProcess/cancelAudit")
	@ResponseBody
	public Map<String, Object> cancelAudit(ShxxDto shxxDto, HttpServletRequest request){
		Map<String, Object> data = new HashMap<>();
		boolean result;
		try {
			User user = getLoginInfo(request);
			//传入委托相关参数
			DataPermission.addWtParam(shxxDto);
			
			result = shgcService.doCancleAudit(shxxDto, user);
			
			String msg = xxglService.getModelById("ICOM99020").getXxnr();
			data.put("status", result?"success":"fail");
			data.put(MSG, msg);
			
		}catch (BusinessException e) {
			String mess = e.getMsg();
			XxglModel xxglModel = xxglService.getModelById(e.getMsgId());
			String idMsg = xxglModel==null?"":xxglModel.getXxnr();
			data.put("status", "fail");
			data.put(MSG, idMsg + (StringUtil.isNotBlank(idMsg) && StringUtil.isNotBlank(mess) ? "<br/>" : "")
					+ (StringUtil.isNotBlank(mess) ? mess : ""));

		} catch (Exception e) {
			logger.error(e.getMessage());
			data.put("status", "fail");
			data.put(MSG, xxglService.getModelById("ICOM99021").getXxnr());
		}
		return data;
	}
	
	/**
	 * 显示批量审核页面
	 */
	@RequestMapping(value ="/auditProcess/batchaudit")
	public ModelAndView batchAudit(ShgcDto shgcDto){
		ModelAndView mav = new ModelAndView("globalweb/systemcheck/audit_batch");
		mav.addObject("isBatch", "true");
		List<String> t_ywids = shgcDto.getYwids();
		String ywids = t_ywids.toString().substring(1,t_ywids.toString().length()-1);
		// 获取当前审核过程
		ShgcDto t_shgcDto = shgcService.getDtoByYwid(t_ywids.get(0));
		if(t_shgcDto == null)
			return mav;
		//审核业务页面
		t_shgcDto.setBusiness_url(shgcDto.getBusiness_url());
		t_shgcDto.setYwzd(shgcDto.getYwzd());
		// 获取的审核流程列表
		ShlcDto shlcParam = new ShlcDto();
		shlcParam.setShid(t_shgcDto.getShid());
		shlcParam.setGcid(t_shgcDto.getGcid());// 处理旧流程判断用
		List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
		// 退回列表
		List<ShlcDto> backShlcList = new ArrayList<>();
		ShlcDto startLc = new ShlcDto();
		startLc.setGwmc("用户");
		startLc.setLcxh("-1");
		backShlcList.add(startLc);
		for (ShlcDto shlc : shlcList) {
			//判断如果流程类别不为空，则代表需要显示特殊页面，不能进行批量审核
			if(StringUtil.isNotBlank(shlc.getLclb()) && shlc.getLcxh().equals(t_shgcDto.getXlcxh())) {
				mav.addObject("isBatch", "false");
				break;
			}
			// 当流程序号小于现流程序号则放入退回列表
			if (t_shgcDto.getXlcxh() != null
					&& Integer.parseInt(shlc.getLcxh()) < Integer.parseInt(t_shgcDto.getXlcxh())) {
				shlc.setAudited(true);
				backShlcList.add(shlc);
			} else if (shlc.getLcxh().equals(t_shgcDto.getXlcxh())) {// 相等则是当前流程
				// 当前流程做标记
				shlc.setCurrent(true);// 当前流程
				shlc.setAudited(true); //已审核流程
				// 最后一步标志
				if (t_shgcDto.getXlcxh() != null && shlcList.get(shlcList.size() - 1).getLcxh().equals(t_shgcDto.getXlcxh())) {
					t_shgcDto.setLastStep(true);
					break;// 跳出for循环
				}
			}
		}
		
		mav.addObject("ywids", ywids);
		mav.addObject("shgcDto", t_shgcDto);
		mav.addObject("backShlcList", backShlcList);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 显示批量审核页面
	 */
	@RequestMapping(value ="/auditProcess/batchauditVue")
	@ResponseBody
	public Map<String, Object> batchauditVue(ShgcDto shgcDto){
		Map<String, Object> map = new HashMap<>();
		map.put("isBatch", "true");
		List<String> t_ywids = shgcDto.getYwids();
		String ywids = t_ywids.toString().substring(1,t_ywids.toString().length()-1);
		// 获取当前审核过程
		ShgcDto t_shgcDto = shgcService.getDtoByYwid(t_ywids.get(0));
		if(t_shgcDto == null)
			return map;
		//审核业务页面
		t_shgcDto.setBusiness_url(shgcDto.getBusiness_url());
		t_shgcDto.setYwzd(shgcDto.getYwzd());
		// 获取的审核流程列表
		ShlcDto shlcParam = new ShlcDto();
		shlcParam.setShid(t_shgcDto.getShid());
		shlcParam.setGcid(t_shgcDto.getGcid());// 处理旧流程判断用
		List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
		// 退回列表
		List<ShlcDto> backShlcList = new ArrayList<>();
		ShlcDto startLc = new ShlcDto();
		startLc.setGwmc("用户");
		startLc.setLcxh("-1");
		backShlcList.add(startLc);
		for (ShlcDto shlc : shlcList) {
			//判断如果流程类别不为空，则代表需要显示特殊页面，不能进行批量审核
			if(StringUtil.isNotBlank(shlc.getLclb()) && shlc.getLcxh().equals(t_shgcDto.getXlcxh())) {
				map.put("isBatch", "false");
				break;
			}
			// 当流程序号小于现流程序号则放入退回列表
			if (t_shgcDto.getXlcxh() != null
					&& Integer.parseInt(shlc.getLcxh()) < Integer.parseInt(t_shgcDto.getXlcxh())) {
				shlc.setAudited(true);
				backShlcList.add(shlc);
			} else if (shlc.getLcxh().equals(t_shgcDto.getXlcxh())) {// 相等则是当前流程
				// 当前流程做标记
				shlc.setCurrent(true);// 当前流程
				shlc.setAudited(true); //已审核流程
				// 最后一步标志
				if (t_shgcDto.getXlcxh() != null && shlcList.get(shlcList.size() - 1).getLcxh().equals(t_shgcDto.getXlcxh())) {
					t_shgcDto.setLastStep(true);
					break;// 跳出for循环
				}
			}
		}

		map.put("ywids", ywids);
		map.put("shgcDto", t_shgcDto);
		map.put("backShlcList", backShlcList);
		map.put("urlPrefix", urlPrefix);
		return map;
	}
	/**
	 * 批量审核保存
	 */
	@RequestMapping(value ="/auditProcess/batchauditSaveVue")
	@ResponseBody
	public Map<String, Object> batchauditSaveVue(ShxxDto shxxDto,HttpServletRequest request) {
		return batchAuditSave(shxxDto,request);
	}

	/**
	 * 批量审核保存
	 */
	@RequestMapping(value ="/auditProcess/batchauditSave")
	@ResponseBody
	public Map<String, Object> batchAuditSave(ShxxDto shxxDto,HttpServletRequest request) {
		Map<String, Object> data = new HashMap<>();
		boolean result = false;
		try {
			// 传入委托相关参数
			DataPermission.addWtParam(shxxDto);

			shgcService.doBatchAudit(shxxDto, getLoginInfo(request),request);
			/*
			result = auditResult.getReturnResult();
			String msg = auditResult.getReturnMsg(xxglService.getModelById("ICOM99018").getXxnr(), xxglService.getModelById("ICOM99019").getXxnr());
			data.put("status", "success");
			data.put(MSG, msg);
			*/
			result = true;
		} catch (BusinessException e) {
			String mess = e.getMsg();
			XxglModel xxglModel = xxglService.getModelById(e.getMsgId());
			String idMsg = xxglModel==null?"":xxglModel.getXxnr();
			data.put("status", "fail");
			data.put(MSG, idMsg + (StringUtil.isNotBlank(idMsg) && StringUtil.isNotBlank(mess) ? "<br/>" : "")
					+ (StringUtil.isNotBlank(mess) ? mess : ""));

		} catch (Exception e) {
			logger.error(e.getMessage());
			data.put("status", "fail");
			data.put(MSG, xxglService.getModelById("ICOM99019").getXxnr());
		}
		data.put(RESULT, result);
		return data;
	}
	
	/**
	 * 检查审批处理进度
	 */
	@RequestMapping(value ="/auditProcess/commCheckAudit")
	@ResponseBody
	public Map<String,Object> commCheckAudit(ShxxDto shxxDto) {
		
		Map<String,Object> map = new HashMap<>();
		
		AuditResult result = shgcService.checkAuditThreadStatus(shxxDto);
		
		if(result.isFinished()){
			String msg = result.getMsg();
			
			map.put("result", true);
			map.put("status", result.getType());
			
			if(result.getType() == AuditResult.SUCCESS) {
				map.put("msg", xxglService.getModelById("ICOM99018").getXxnr() + msg);
				map.put("backInfo", result.getBackMap());
			}else {
				map.put("msg", xxglService.getModelById("ICOM99019").getXxnr() + msg);
			}
		}else{
			map.put("result", false);
			map.put("currentCount", result.getCnt());
		}
		return map;
	}
	
	/**
	 * 取消正在审批的处理(主要用于批量审核进行时的取消处理)
	 */
	@RequestMapping(value ="/auditProcess/cancelAuditProcess")
	@ResponseBody
	public Map<String,Object> cancelAuditProcess(ShxxDto shxxDto){
		boolean result = shgcService.cancelAuditProcess(shxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", result);
		return map;
	}
	
	/**
	 * 撤回审核要求
	 * 提交后如果还没有被审核过，则允许撤回
	 */
	@RequestMapping(value ="/auditProcess/recallAudit")
	@ResponseBody
	public Map<String,Object> recallAudit(ShgcDto shgcDto,HttpServletRequest request) {
		Map<String, Object> data = new HashMap<>();
		try {
			// 如果没有审核类型，则为异常
			if (StringUtil.isBlank(shgcDto.getShlb())) {
				throw new BusinessException("ICOM99022", null);
			}

			// 根据流程数，判断是否需要提交
			User user = getLoginInfo(request);
			String result = shgcService.updateAuditRecall(shgcDto, user);
			if (StringUtil.isBlank(result)) {
				data.put("msg", xxglService.getModelById("ICOM99023").getXxnr());
			} else {
				data.put("msg", xxglService.getModelById("ICOM99023").getXxnr() + "<br/>" + result);
			}
		} catch (BusinessException e) {
			data.put("msg", xxglService.getModelById("ICOM99022").getXxnr()  + "<br/>" + e.getMessage());
		} catch (Exception e) {
			logger.error(e.toString());
			data.put("msg", xxglService.getModelById("ICOM99022").getXxnr()  + "<br/>" + e.getMessage());
		}
		return data;
	}
	
	/**
	 * 钉钉调用查询审核过程信息
	 */
	@RequestMapping("/dingTalkaudit/auditProcess")
	@ResponseBody
	public Map<String,Object> auditProcess(ShgcDto shgcDto){
		Map<String,Object> map= new HashMap<>();
		ShgcDto t_shgcDto = shgcService.getDtoByYwid(shgcDto.getYwid());
		if(t_shgcDto == null) {
			t_shgcDto=new ShgcDto();
		}
		t_shgcDto.setYwzd(shgcDto.getYwzd());
		// 获取的审核流程列表
		ShlcDto shlcParam = new ShlcDto();
		shlcParam.setShid(t_shgcDto.getShid());
		shlcParam.setGcid(t_shgcDto.getGcid());// 处理旧流程判断用
		List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
		// 退回列表
		List<ShlcDto> backShlcList = new ArrayList<>();
		ShlcDto startLc = new ShlcDto();
		startLc.setGwmc("用户");
		startLc.setLcxh("-1");
		backShlcList.add(startLc);
		for (ShlcDto shlc : shlcList) {
			// 当流程序号小于现流程序号则放入退回列表
			if (t_shgcDto.getXlcxh() != null
					&& Integer.parseInt(shlc.getLcxh()) < Integer.parseInt(t_shgcDto.getXlcxh())) {
				shlc.setAudited(true);
				backShlcList.add(shlc);
			} else if (shlc.getLcxh().equals(t_shgcDto.getXlcxh())) {// 相等则是当前流程
				// 当前流程做标记
				shlc.setCurrent(true);// 当前流程
				shlc.setAudited(true); //已审核流程
				// 最后一步标志
				if (t_shgcDto.getXlcxh() != null
						&& shlcList.get(shlcList.size() - 1).getLcxh().equals(t_shgcDto.getXlcxh())) {
					t_shgcDto.setLastStep(true);
				}
				//判断如果流程类别不为空，则代表需要显示特殊页面
				if(StringUtil.isNotBlank(shlc.getLclb())) {
					JcsjDto jcsjDto = jcsjService.getDtoById(shlc.getLclb());
					//审核业务页面
					t_shgcDto.setBusiness_url(jcsjDto.getCskz2());
				}
				break;// 跳出for循环
			}
		}
		ShxxDto shxxParam = new ShxxDto();
		shxxParam.setShlb(shgcDto.getShlb());
		shxxParam.setYwid(shgcDto.getYwid());
		List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
		SpgwcyDto spgwcyDto = new SpgwcyDto();
		spgwcyDto.setYhid(shgcDto.getYhid());
		spgwcyDto.setJsid(shgcDto.getJsid());
		ShlcDto shlcDto = shlcList.get(Integer.parseInt(t_shgcDto.getXlcxh())-1);
		spgwcyDto.setGwid(shlcDto.getGwid());
		List<SpgwcyDto> dtoList = spgwcyService.getDtoList(spgwcyDto);
		if (dtoList!=null&&!dtoList.isEmpty()){
			map.put("isAudit",true);
		}else {
			map.put("isAudit",false);
		}
		map.put("shgcDto", t_shgcDto);
		map.put("backShlcList", backShlcList);
		map.put("shlcList", shlcList);
		map.put("shxxList", shxxList);
		return map;
	}
	
	/**
	 * 
	 * 钉钉调用查询审核历史信息
	 */
	@RequestMapping(value ="/dingTalkaudit/dingAuditView")
	@ResponseBody
	public Map<String,Object> dingAuditView(ShgcDto shgcDto) {
		Map<String,Object> map= new HashMap<>();
		try {
			ShxxDto shxxParam = new ShxxDto();
			ShgcDto d_shgcDto;
			// 未传递gcid，则根据xmid和shlb获取shgc
			if (StringUtil.isBlank(shgcDto.getGcid())) {
				// 获取当前审核过程
				d_shgcDto = shgcService.getDtoByYwid(shgcDto.getYwid());
			} else {// 传递了gcid，则根据gcid获取shgc
					// 获取当前审核过程
				d_shgcDto = shgcService.getDtoById(shgcDto.getGcid());
			}
			if (d_shgcDto == null) {// 数据不在审核中，表示通过
				shgcDto.setXlcxh("");// 审核通过时
			} else {
				shgcDto = d_shgcDto;
			}
			shxxParam.setShlb(shgcDto.getShlb());
			shxxParam.setShlbs(shgcDto.getShlbs());
			shxxParam.setYwid(shgcDto.getYwid());
			//shxxParam.setCommit(true);// 查询提交的数据
			shxxParam.setGcid(shgcDto.getGcid());// 用过程id进行查询

			shxxParam.setSftg(null);
			shxxParam.setCommit(false); 
			List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
			if (null == shxxList) {
				shxxList = new ArrayList<>();
			}

			// 获取的审核流程列表
			ShlcDto shlcParam = new ShlcDto();
			// gcid或shid参数不全的情况（已审核完，延期审核查看会传递gcid，所以gcid和shid都需要判断），根据审核信息获取流程
			if (!shxxList.isEmpty()) {
				shlcParam.setShid(shxxList.get(0).getShid());
				shlcParam.setSqsj(shxxList.get(0).getSqsj());
			} else {
				shlcParam.setShid(shgcDto.getShid());
				shlcParam.setGcid(shgcDto.getGcid());// 处理旧流程判断用
			}
			if (StringUtil.isNotBlank(shlcParam.getShid())) {
				List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
				if (StringUtil.isNotBlank(shlcParam.getGcid())) {
					if(StringUtil.isNotBlank(shgcDto.getXlcxh())){
						for (ShlcDto shlc : shlcList) {
							if (shlc.getLcxh().equals(shgcDto.getXlcxh())) {// 相等则是当前流程
								// 当前流程做标记
								shlc.setCurrent(true);// 当前流程
								shlc.setAudited(true); //已审核流程
								break;// 跳出for循环
							}
							shlc.setAudited(true); //已审核流程
						}
					}
				}else{
					for (ShlcDto shlc : shlcList) {
						shlc.setAudited(true); //已审核流程
					}
				}
				map.put("shlcList", shlcList);
			}
			map.put("shxxList", shxxList);
			map.put("shgcDto", shgcDto);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return map;
	}
	
	
	/**
	 * 显示审核页面
	 */
	@RequestMapping(value ="/auditProcess/modauditView")
	public ModelAndView modauditView(ShgcDto shgcDto,ShxxDto shxxDto) {
		ModelAndView mav = new ModelAndView("globalweb/systemcheck/audit_modprocess");
		try {
			ShxxDto shxxParam = new ShxxDto();
			ShgcDto d_shgcDto;
			// 未传递gcid，则根据xmid和shlb获取shgc
			if (StringUtil.isBlank(shgcDto.getGcid())) {
				// 获取当前审核过程
				d_shgcDto = shgcService.getDtoByYwid(shgcDto.getYwid());
			} else {// 传递了gcid，则根据gcid获取shgc
					// 获取当前审核过程
				d_shgcDto = shgcService.getDtoById(shgcDto.getGcid());
			}
			if (d_shgcDto == null) {// 数据不在审核中，表示通过
				shgcDto.setXlcxh("");// 审核通过时
			} else {
				shgcDto = d_shgcDto;
			}
			shxxParam.setShlb(shgcDto.getShlb());
			shxxParam.setShlbs(shgcDto.getShlbs());
			shxxParam.setYwid(shgcDto.getYwid());
			//shxxParam.setCommit(true);// 查询提交的数据
			shxxParam.setGcid(shgcDto.getGcid());// 用过程id进行查询
			shxxParam.setSftg(null);
			shxxParam.setCommit(false); 
			List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
			if (null == shxxList) {
				shxxList = new ArrayList<>();
			}

			// 获取的审核流程列表
			ShlcDto shlcParam = new ShlcDto();
			// gcid或shid参数不全的情况（已审核完，延期审核查看会传递gcid，所以gcid和shid都需要判断），根据审核信息获取流程
			if (!shxxList.isEmpty()) {
				shlcParam.setShid(shxxList.get(0).getShid());
				shlcParam.setSqsj(shxxList.get(0).getSqsj());
			} else {
				shlcParam.setShid(shgcDto.getShid());
				shlcParam.setGcid(shgcDto.getGcid());// 处理旧流程判断用
			}
			if (StringUtil.isNotBlank(shlcParam.getShid())) {
				List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
				if (StringUtil.isNotBlank(shlcParam.getGcid())) {
					if(StringUtil.isNotBlank(shgcDto.getXlcxh())){
						for (ShlcDto shlc : shlcList) {
							if (shlc.getLcxh().equals(shgcDto.getXlcxh())) {// 相等则是当前流程
								// 当前流程做标记
								shlc.setCurrent(true);// 当前流程
								shlc.setAudited(true); //已审核流程
								break;// 跳出for循环
							}
							shlc.setAudited(true); //已审核流程
						}
					}
				}else{
					for (ShlcDto shlc : shlcList) {
						shlc.setAudited(true); //已审核流程
					}
				}
				mav.addObject("shlcList", shlcList);
			}
			mav.addObject("shxxList", shxxList);
			mav.addObject("shgcDto", shgcDto);
			mav.addObject("ywid", shxxDto.getYwid());
			mav.addObject("shlb", shxxDto.getShlb());
			mav.addObject("urlPrefix", urlPrefix);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return mav;
	}
	
	/**
	 * 修改审核页面
	 */
	@RequestMapping(value ="/auditProcess/auditList")
	@ResponseBody
	public Map<String,Object> auditList(ShxxDto shxxDto) {
		Map<String,Object> map= new HashMap<>();
		List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxDto);
		if (null == shxxList) {
			shxxList = new ArrayList<>();
		}
		map.put("total", shxxDto.getTotalNumber());
		map.put("rows", shxxList);
		return map;
	}
	/**
	 * 修改审核页面
	 */
	@RequestMapping(value ="/auditProcess/modaudit")
	@ResponseBody
	public Map<String,Object> modaudit(ShxxDto shxxDto) {
		Map<String,Object> map= new HashMap<>();
		ShxxDto shxxDto_t = shxxService.getDtoById(shxxDto.getShxxid());
		map.put("shrmc",shxxDto_t.getShrmc());
		map.put("shxxid",shxxDto_t.getShxxid());
		map.put("gwmc",shxxDto_t.getGwmc());
		map.put("shsj",shxxDto_t.getShsj());
		map.put("sftg",shxxDto_t.getSftg());
		map.put("shyj",shxxDto_t.getShyj());
		return map;
	}
	
	/**
	 * 修改审核保存
	 */
	@RequestMapping(value ="/auditProcess/savemodaudit")
	@ResponseBody
	public Map<String,Object> savemodaudit(ShxxDto shxxDto,HttpServletRequest request) {
		Map<String,Object> map= new HashMap<>();
		User user = getLoginInfo(request);
		shxxDto.setXgry(user.getYhid());
		User user_t= commonService.getUserId(shxxDto.getLrry());
		if(user_t==null) {
			map.put("status","fail");
			map.put("message","未找到该审核人信息");
		}else {
			shxxDto.setLrry(user_t.getYhid());
			boolean isSuccess = shxxService.update(shxxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
		}		
		return map;
	}
	
}
