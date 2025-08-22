package com.matridx.igams.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.igams.common.dao.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DingNotificationTypeEnum;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/common")
public class DdxxglController extends BaseController
{
	@Autowired
	private IDdxxglService DdxxglService;
	@Autowired
	private IXxglService xxglservice;
	@Autowired
	private IJcsjService jcsjService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	/**
	 * 跳转
	 */
	@RequestMapping("/ddxxgl/pageList")
	public ModelAndView PageList()
	{
		ModelAndView mav = new ModelAndView("systemmain/ddxxgl/ddxx_list");
		List<Map<String, String>> xxlxlist = new ArrayList<>();
		List<Map<String, String>> tzlxlist = new ArrayList<>();
		// 查询消息类型
		Map<String, List<JcsjDto>> jcsjlist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DINGMESSAGETYPE});
		for (JcsjDto ddxx : jcsjlist.get("DINGMESSAGETYPE")) {
			Map<String, String> map = new HashMap<>();
			map.put("value", ddxx.getCsmc());
			map.put("code",ddxx.getCsdm());
			xxlxlist.add(map);
		}

		// 通过枚举类查询通知类型
		for (DingNotificationTypeEnum tzlxtype : DingNotificationTypeEnum.values())
		{
			Map<String, String> map = new HashMap<>();
			map.put("value", tzlxtype.getValue());
			map.put("code", tzlxtype.getCode());
			tzlxlist.add(map);
		}
		mav.addObject("xxlxlist", xxlxlist);
		mav.addObject("tzlxlist", tzlxlist);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/***
	 *跳转 实验室vue
	 */
	@RequestMapping("/ddxxgl/page_List_vue")
	@ResponseBody
	public Map<String,Object> PageList_vue(HttpServletRequest request){
		Map<String,Object> resMap=new HashMap<>();
		resMap.put("status", "success");
		try {
			List<Map<String, String>> xxlxlist = new ArrayList<>();
			List<Map<String, String>> tzlxlist = new ArrayList<>();
			// 查询消息类型
			Map<String, List<JcsjDto>> jcsjlist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DINGMESSAGETYPE});
			for (JcsjDto ddxx : jcsjlist.get("DINGMESSAGETYPE")) {
				Map<String, String> map = new HashMap<>();
				map.put("value", ddxx.getCsmc());
				map.put("code",ddxx.getCsdm());
				xxlxlist.add(map);
			}

			// 通过枚举类查询通知类型
			for (DingNotificationTypeEnum tzlxtype : DingNotificationTypeEnum.values())
			{
				Map<String, String> map = new HashMap<>();
				map.put("value", tzlxtype.getValue());
				map.put("code", tzlxtype.getCode());
				tzlxlist.add(map);
			}
			User user = getLoginInfo();

			List<QxModel> qxDtos = user.getQxModels();

			List<QxModel> now_jsczDtos = new ArrayList<>();
			for (QxModel qxModel : qxDtos) {
				if (qxModel.getZyid().equals(request.getParameter("zyid"))) {
					now_jsczDtos.add(qxModel);
				}
			}
			resMap.put("czdmlist", now_jsczDtos);
			resMap.put("xxlxlist", xxlxlist);
			resMap.put("tzlxlist", tzlxlist);
			resMap.put("urlPrefix", urlPrefix);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}
		return  resMap;
	}

	/**
	 * 钉钉消息管理列表
	 */
	@RequestMapping("/ddxxgl/pageGetList")
	@ResponseBody
	public Map<String, Object> getDdxxglList(DdxxglDto ddxxglDto)
	{
		//Map<String, List<JcsjDto>> jcsjlist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DINGMESSAGETYPE});
		//List<JcsjDto> ddxxList = jcsjlist.get("DINGMESSAGETYPE");
		List<DdxxglDto> ddxxglList = DdxxglService.getPagedDtoList(ddxxglDto);

		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb("DINGMESSAGETYPE");
		for (DdxxglDto dto : ddxxglList) {
			jcsjDto.setCsdm(dto.getDdxxlx());
			JcsjDto t_jcsjDto = jcsjService.getDtoByCsdmAndJclb(jcsjDto);
			if(t_jcsjDto!= null)
				dto.setDdxxlx(t_jcsjDto.getCsmc());
			dto.setTzlx(DingNotificationTypeEnum.getValueByCode(dto.getTzlx()));
		}
		Map<String, Object> map = new HashMap<>();
		map.put("total", ddxxglDto.getTotalNumber());
		map.put("rows", ddxxglList);
		return map;
	}

	/**
	 * 新增钉钉消息页面
	 */
	@RequestMapping("/ddxxgl/addDdxxgl")
	public ModelAndView addDdxxgl(DdxxglDto ddxxglDto)
	{
		ModelAndView mav = new ModelAndView("systemmain/ddxxgl/ddxx_addList");
		// 钉钉消息类型
		List<Map<String, String>> listType = new ArrayList<>();
		Map<String, List<JcsjDto>> jcsjlist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DINGMESSAGETYPE});
		for (JcsjDto ddxx : jcsjlist.get("DINGMESSAGETYPE")) {
			Map<String, String> map = new HashMap<>();
			map.put("value", ddxx.getCsmc());
			map.put("code",ddxx.getCsdm());
			listType.add(map);
		}
		// 通知类型
		List<Map<String, String>> tzlxList = new ArrayList<>();
		for (DingNotificationTypeEnum tzlxType : DingNotificationTypeEnum.values())
		{
			Map<String, String> map = new HashMap<>();
			map.put("code", tzlxType.getCode());
			map.put("value", tzlxType.getValue());
			tzlxList.add(map);
		}
		 //用户id
		List<DdxxglDto> xtyhlist= DdxxglService.getXtyh();
		mav.addObject("xtyhlist", xtyhlist); 
		ddxxglDto.setFormAction("addSaveDdxxgl");
		mav.addObject("tzlxList", tzlxList);
		mav.addObject("listType", listType);
		mav.addObject("ddxxglDto", ddxxglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 新增钉钉消息页面_vue
	 */
	@RequestMapping("/ddxxgl/addDdxxgl_vue")
	@ResponseBody
	public Map<String, Object> addDdxxgl_vue(DdxxglDto ddxxglDto)
	{
		Map<String, Object> resMap=new HashMap<>();
		resMap.put("status", "success");
		try {
			// 钉钉消息类型
			List<Map<String, String>> listType = new ArrayList<>();
			Map<String, List<JcsjDto>> jcsjlist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DINGMESSAGETYPE});
			for (JcsjDto ddxx : jcsjlist.get("DINGMESSAGETYPE")) {
				Map<String, String> map = new HashMap<>();
				map.put("value", ddxx.getCsmc());
				map.put("code",ddxx.getCsdm());
				listType.add(map);
			}
			// 通知类型
			List<Map<String, String>> tzlxList = new ArrayList<>();
			for (DingNotificationTypeEnum tzlxType : DingNotificationTypeEnum.values())
			{
				Map<String, String> map = new HashMap<>();
				map.put("code", tzlxType.getCode());
				map.put("value", tzlxType.getValue());
				tzlxList.add(map);
			}
			//用户id
			List<DdxxglDto> xtyhlist= DdxxglService.getXtyh();
			resMap.put("xtyhlist", xtyhlist);
			ddxxglDto.setFormAction("addSaveDdxxgl");
			resMap.put("tzlxList", tzlxList);
			resMap.put("listType", listType);
			resMap.put("ddxxglDto", ddxxglDto);
			resMap.put("urlPrefix", urlPrefix);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}

		return resMap;
	}
	/**
	 * 新增保存
	 */
	@RequestMapping("/ddxxgl/addSaveDdxxgl")
	@ResponseBody
	public Map<String, Object> addSaveDdxxgl(DdxxglDto ddxxglDto)
	{
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isBlank(ddxxglDto.getDdxxid()))
		{
			ddxxglDto.setDdxxid(StringUtil.generateUUID());
			boolean isSuccess = DdxxglService.insert(ddxxglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglservice.getModelById("ICOM00001").getXxnr() : xxglservice.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/*
	  通过钉钉通知类型查询通知人员角色

	  @param ddxxglDto
	 * @return
	 *//*
		 * @RequestMapping("/ddxxgl/sel_Tzlx")
		 * 
		 * @ResponseBody public Map<String, Object> sel_Tzlx(DdxxglDto
		 * ddxxglDto){ if(ddxxglDto.getTzlx()!=null && ddxxglDto.getTzlx()!="")
		 * { if(ddxxglDto.getTzlx().equals("")) {
		 * 
		 * } } return null; }
		 */
	/**
 * 通过id获取单个
 */
@RequestMapping("/ddxxgl/modDdxxgl")
public ModelAndView modDdxxgl(DdxxglDto ddxxglDto)
{
	ModelAndView mav = new ModelAndView("systemmain/ddxxgl/ddxx_addList");
	// 钉钉消息类型
	List<Map<String, String>> listType = new ArrayList<>();
	Map<String, List<JcsjDto>> jcsjlist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DINGMESSAGETYPE});
	for (JcsjDto ddxx : jcsjlist.get("DINGMESSAGETYPE")) {
		Map<String, String> map = new HashMap<>();
		map.put("value", ddxx.getCsmc());
		map.put("code",ddxx.getCsdm());
		listType.add(map);
	}
	// 通知类型
	List<Map<String, String>> tzlxList = new ArrayList<>();
	for (DingNotificationTypeEnum tzlxType : DingNotificationTypeEnum.values())
	{
		Map<String, String> map = new HashMap<>();
		map.put("code", tzlxType.getCode());
		map.put("value", tzlxType.getValue());
		tzlxList.add(map);
	}
	// 用户id
	List<DdxxglDto> xtyhlist = DdxxglService.getXtyh();
	DdxxglDto ddxxglDtos = DdxxglService.getDtoById(ddxxglDto.getDdxxid());
	ddxxglDtos.setFormAction("modSaveDdxxgl");
	mav.addObject("tzlxList", tzlxList);
	mav.addObject("listType", listType);
	mav.addObject("xtyhlist", xtyhlist);
	mav.addObject("ddxxglDto", ddxxglDtos);
	mav.addObject("urlPrefix", urlPrefix);
	return mav;
}

	/**
	 * 通过id获取单个_vue
	 */
	@RequestMapping("/ddxxgl/getUpdate_vue")
	@ResponseBody
	public Map<String,Object> getUpdate_vue(DdxxglDto ddxxglDto)
	{
		Map<String,Object> resMap=new HashMap<>();
		resMap.put("status", "success");
		try{
			// 钉钉消息类型
			List<Map<String, String>> listType = new ArrayList<>();
			Map<String, List<JcsjDto>> jcsjlist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DINGMESSAGETYPE});
			for (JcsjDto ddxx : jcsjlist.get("DINGMESSAGETYPE")) {
				Map<String, String> map = new HashMap<>();
				map.put("value", ddxx.getCsmc());
				map.put("code",ddxx.getCsdm());
				listType.add(map);
			}
			// 通知类型
			List<Map<String, String>> tzlxList = new ArrayList<>();
			for (DingNotificationTypeEnum tzlxType : DingNotificationTypeEnum.values())
			{
				Map<String, String> map = new HashMap<>();
				map.put("code", tzlxType.getCode());
				map.put("value", tzlxType.getValue());
				tzlxList.add(map);
			}
			// 用户id
			List<DdxxglDto> xtyhlist = DdxxglService.getXtyh();
			DdxxglDto ddxxglDtos = DdxxglService.getDtoById(ddxxglDto.getDdxxid());
			ddxxglDtos.setFormAction("modSaveDdxxgl");
			resMap.put("tzlxList", tzlxList);
			resMap.put("listType", listType);
			resMap.put("xtyhlist", xtyhlist);
			resMap.put("ddxxglDto", ddxxglDtos);
			resMap.put("urlPrefix", urlPrefix);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}

		return resMap;
	}
	/**
	 * 保存修改
	 */
	@RequestMapping("/ddxxgl/modSaveDdxxgl")
	@ResponseBody
	public Map<String, Object> modSaveDdxxgl(DdxxglDto ddxxglDto)
	{
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess = DdxxglService.update(ddxxglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglservice.getModelById("ICOM00001").getXxnr() : xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 删除消息
	 */
	@RequestMapping("/ddxxgl/delDdxxgl")
	@ResponseBody
	public Map<String, Object> delDdxxgl(DdxxglDto ddxxglDto)
	{
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess = DdxxglService.delete(ddxxglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglservice.getModelById("ICOM00003").getXxnr() : xxglservice.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 查看
	 */
	@RequestMapping("/ddxxgl/viewDdxxgl")
	public ModelAndView viewDdxxgl(DdxxglDto ddxxglDto)
	{
		ModelAndView mav = new ModelAndView("systemmain/ddxxgl/ddxx_viewList");
		DdxxglDto ddxxglDtos = DdxxglService.getDtoById(ddxxglDto.getDdxxid());

		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb("DINGMESSAGETYPE");
		jcsjDto.setCsdm(ddxxglDtos.getDdxxlx());

		ddxxglDtos.setDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsmc());
		ddxxglDtos.setTzlx(DingNotificationTypeEnum.getValueByCode(ddxxglDtos.getTzlx()));
		mav.addObject("ddxxglDto", ddxxglDtos);
		return mav;
	}

	/**
	 * 查看vue
	 */
	@RequestMapping("/ddxxgl/getView_vue")
	@ResponseBody
	public Map<String, Object>  getView_vue(DdxxglDto ddxxglDto)
	{
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try {
			DdxxglDto ddxxglDtos = DdxxglService.getDtoById(ddxxglDto.getDdxxid());

			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb("DINGMESSAGETYPE");
			jcsjDto.setCsdm(ddxxglDtos.getDdxxlx());

			ddxxglDtos.setDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsmc());
			ddxxglDtos.setTzlx(DingNotificationTypeEnum.getValueByCode(ddxxglDtos.getTzlx()));
			resMap.put("ddxxglDto", ddxxglDtos);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}

		return resMap;
	}
}
