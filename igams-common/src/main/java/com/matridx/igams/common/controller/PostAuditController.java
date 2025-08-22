package com.matridx.igams.common.controller;

import java.util.*;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.SpgwDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.service.svcinterface.ISpgwService;
import com.matridx.igams.common.service.svcinterface.ISpgwcyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/systemmain")
public class PostAuditController extends BaseController{
	
	@Autowired
	ISpgwService spgwService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	ISpgwcyService spgwcyService;
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;
    @Value("${matridx.rabbit.flg:}")
    private String prefixFlg;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	/**
	 * 岗位列表
	 */
	@RequestMapping("/audit/pageListAuditPost")
	public ModelAndView pageListAuditPost(){
		ModelAndView mav = new ModelAndView("systemmain/audit/post_list");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 岗位列表明细
	 */
	@RequestMapping("/audit/pageGetListAuditPost")
	@ResponseBody
	public Map<String,Object> pageGetListAuditPost(SpgwDto spgwDto){
		List<SpgwDto> t_List = spgwService.getPagedDtoList(spgwDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", spgwDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 岗位查看
	 */
	@RequestMapping(value="/audit/viewAuditPost")
	public ModelAndView viewAuditPost(SpgwDto spgwDto){
		ModelAndView mav = new ModelAndView("systemmain/audit/post_view");
		SpgwDto t_spgwDto = spgwService.getDtoById(spgwDto.getGwid());
		mav.addObject("spgwDto", t_spgwDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 审核岗位新增
	 */
	@RequestMapping(value ="/audit/addAuditPost")
	public ModelAndView addAuditPost(SpgwDto spgwDto){
		ModelAndView mav = new ModelAndView("systemmain/audit/post_edit");
		spgwDto.setFormAction("add");
		mav.addObject("spgwDto", spgwDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 审核岗位新增保存
	 */
	@RequestMapping(value="/audit/addSaveAuditPost")
	@ResponseBody
	public Map<String, Object> addSaveAuditPost(SpgwDto spgwDto){
		if(spgwDto.getDwxz()==null || Objects.equals(spgwDto.getDwxz(), "")) {
			spgwDto.setDwxz("0");
		}
		checkXSSValue(spgwDto);
		boolean isSuccess = spgwService.insert(spgwDto);
		if("1".equals(spgwDto.getSfgb()) && isSuccess) {
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.spgw.update",JSONObject.toJSONString(spgwDto));
		}
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 审核岗位修改
	 */
	@RequestMapping(value="/audit/modAuditPost")
	public ModelAndView modAuditPost(SpgwDto spgwDto){
		ModelAndView mav = new ModelAndView("systemmain/audit/post_edit");
		
		spgwDto = spgwService.getDtoById(spgwDto.getGwid());
		if(spgwDto == null){
			spgwDto = new SpgwDto(); 
		}
		spgwDto.setFormAction("mod");
		mav.addObject("spgwDto", spgwDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 审核岗位修改保存
	 */
	@RequestMapping(value="/audit/modSaveAuditPost")
	@ResponseBody
	public Map<String, Object> modSaveAuditPost(SpgwDto spgwDto){
		spgwDto.setPrefix(prefixFlg);
		if(spgwDto.getDwxz()==null || Objects.equals(spgwDto.getDwxz(), "")) {
			spgwDto.setDwxz("0");
		}
		checkXSSValue(spgwDto);
		boolean isSuccess = spgwService.update(spgwDto);
		if("1".equals(spgwDto.getSfgb()) && isSuccess) {
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.spgw.update",JSONObject.toJSONString(spgwDto));
		}
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 删除审核岗位信息
	 */
	@RequestMapping(value="/audit/delAuditPost")
	@ResponseBody
	public Map<String, Object> delAuditPost(SpgwDto spgwDto){
		spgwDto.setPrefix(prefixFlg);
		List<SpgwDto> SpgwDtos=spgwService.getSpgwByIds(spgwDto);
		List<String> ids=new ArrayList<>();
		boolean isSuccess = spgwService.delete(spgwDto);
		if(SpgwDtos!=null && !SpgwDtos.isEmpty()) {
			for (SpgwDto dto : SpgwDtos) {
				if ("1".equals(dto.getSfgb())) {
					ids.add(dto.getGwid());
				}
			}
		}
		if(!ids.isEmpty()) {
			spgwDto.setIds(ids);
			if(isSuccess) {
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.spgw.del",JSONObject.toJSONString(spgwDto));
			}
		}
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 检查输入项里的非法字符
	 */
	private boolean checkXSSValue(SpgwDto spgwDto){
		if(spgwDto == null)
			return true;
		
		spgwDto.setGwmc(StringUtil.changeXSSInfo(spgwDto.getGwmc()));
		spgwDto.setYwjs(StringUtil.changeXSSInfo(spgwDto.getYwjs()));
		return true;
	}
	
	/**
	 * 审核岗位成员新增页面
	 */
	@RequestMapping(value ="/audit/addAuditPostMember")
	public ModelAndView addAuditPostMember(SpgwDto spgwDto){
		ModelAndView mav = new ModelAndView("systemmain/audit/auditPost_configMember");
		mav.addObject("spgwDto", spgwDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 岗位可选成员
	 */
	@RequestMapping(value ="/configMember/pagedataListUnSelectMember")
	@ResponseBody
	public Map<String,Object> listUnSelectMember(SpgwcyDto spgwcyDto){
		List<SpgwcyDto> t_List = spgwcyService.getPagedOptionalList(spgwcyDto);
		//Json格式的要求{total:22,rows:{}}getPagedOptionalList(spgwcyDto);
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", spgwcyDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 岗位已选成员
	 */
	@RequestMapping(value ="/configMember/pagedataSelectedMember")
	@ResponseBody
	public Map<String,Object> listSelectedMember(SpgwcyDto spgwcyDto){
		List<SpgwcyDto> t_List = spgwcyService.getPagedSelectedList(spgwcyDto);
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", spgwcyDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 岗位添加成员
	 */
	@RequestMapping(value ="/configMember/pagedataToSelected")
	@ResponseBody
	public Map<String,Object> toSelected(SpgwcyDto spgwcyDto){
		try{
			boolean result = spgwcyService.toSelected(spgwcyDto);
			Map<String,Object> map = new HashMap<>();
			map.put("status", result?"success":"fail");
			map.put("message", result?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		}catch(Exception e){
			Map<String,Object> map = new HashMap<>();
			map.put("status", "fail");
			map.put("message", e.getMessage());
			return map;
		}
	}
	
	/**
	 * 岗位去除成员
	 */
	@RequestMapping(value ="/configMember/pagedataToOptional")
	@ResponseBody
	public Map<String,Object> toOptional(SpgwcyDto spgwcyDto){
		try{
			boolean result = spgwcyService.toOptional(spgwcyDto);
			Map<String,Object> map = new HashMap<>();
			map.put("status", result?"success":"fail");
			map.put("message", result?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		}catch(Exception e){
			Map<String,Object> map = new HashMap<>();
			map.put("status", "fail");
			map.put("message", e.getMessage());
			return map;
		}
	}
	/**
	 * 审核岗位成员查看
	 */
	@RequestMapping(value="/configMember/pagedataViewMember")
	public ModelAndView pagedataViewMember(SpgwcyDto spgwcyDto){
		ModelAndView mav = new ModelAndView("systemmain/audit/auditPost_viewMember");
		List<SpgwcyDto> spgwcyDtos = spgwcyService.getDtoListWithDW(spgwcyDto);
		mav.addObject("spgwcyDtos", spgwcyDtos);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
}
