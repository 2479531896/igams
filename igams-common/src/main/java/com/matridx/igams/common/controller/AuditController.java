package com.matridx.igams.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShlbDto;
import com.matridx.igams.common.dao.entities.SpgwDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShlbService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.ISpgwService;
import com.matridx.igams.common.service.svcinterface.IXtshService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/systemmain")
public class AuditController extends BaseController{
	
	@Autowired
	IXtshService xtshService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IShlbService shlbService;
	@Autowired
	ISpgwService spgwService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IShlcService shlcService;
	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	
	/**
	 * 审核列表
	 * @author asus
	 */
	@RequestMapping("/audit/pageListAudit")
	public ModelAndView pageListAudit(){
		ModelAndView mav = new ModelAndView("systemmain/audit/audit_list");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 审核列表明细
	 */
	@RequestMapping("/audit/pageGetListAudit")
	@ResponseBody
	public Map<String,Object> pageGetListAudit(XtshDto xtshDto){
		List<XtshDto> t_List = xtshService.getPagedDtoList(xtshDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", xtshDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 审核查看
	 */
	@RequestMapping(value="/audit/viewAudit")
	public ModelAndView viewAudit(XtshDto xtshDto){
		ModelAndView mav = new ModelAndView("systemmain/audit/audit_view");
		XtshDto t_xtshDto = xtshService.getDtoById(xtshDto.getShid());
		mav.addObject("xtshDto", t_xtshDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 审核新增
	 */
	@RequestMapping(value ="/audit/addAudit")
	public ModelAndView addAudit(XtshDto xtshDto){
		ModelAndView mav = new ModelAndView("systemmain/audit/audit_edit");
		xtshDto.setFormAction("add");
		List<ShlbDto> shlbDto = shlbService.getDtoList(null);
		mav.addObject("xtshDto", xtshDto);
		mav.addObject("shlbList", shlbDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 审核新增保存
	 */
	@RequestMapping(value="/audit/addSaveAudit")
	@ResponseBody
	public Map<String, Object> addSaveAudit(XtshDto xtshDto,HttpServletRequest request){
		//获取用户信息
		User user;
		user = getLoginInfo(request);
		xtshDto.setLrry(user.getYhid());
		xtshDto.setPrefix(prefixFlg);
		checkXSSValue(xtshDto);
		boolean isSuccess = xtshService.insert(xtshDto);
		if("1".equals(xtshDto.getSfgb()) && isSuccess) {
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.xtsh.update",JSONObject.toJSONString(xtshDto));
		}
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 审核修改
	 */
	@RequestMapping(value="/audit/modAudit")
	public ModelAndView modAudit(XtshDto xtshDto){
		ModelAndView mav = new ModelAndView("systemmain/audit/audit_edit");
		
		List<ShlbDto> shlbDto = shlbService.getDtoList(null);
		mav.addObject("shlbList", shlbDto);
		
		xtshDto = xtshService.getDtoById(xtshDto.getShid());
		if(xtshDto == null){
			xtshDto = new XtshDto(); 
		}
		xtshDto.setFormAction("mod");
		mav.addObject("xtshDto", xtshDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 审核修改保存
	 */
	@RequestMapping(value="/audit/modSaveAudit")
	@ResponseBody
	public Map<String, Object> modSaveAudit(XtshDto xtshDto,HttpServletRequest request){
		//获取用户信息
		User user;
		user = getLoginInfo(request);
		xtshDto.setXgry(user.getYhid());
		xtshDto.setPrefix(prefixFlg);
		checkXSSValue(xtshDto);
		boolean isSuccess = xtshService.update(xtshDto);
		if("1".equals(xtshDto.getSfgb()) && isSuccess) {
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.xtsh.update",JSONObject.toJSONString(xtshDto));
		}
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 删除审核信息
	 */
	@RequestMapping(value="/audit/delAudit")
	@ResponseBody
	public Map<String, Object> delAudit(XtshDto xtshDto,HttpServletRequest request){
		User user;
		user = getLoginInfo(request);
		xtshDto.setScry(user.getYhid());
		xtshDto.setScbj("1");
		xtshDto.setPrefix(prefixFlg);
		List<XtshDto> XtshDtos=xtshService.getXtshByIds(xtshDto);
		boolean isSuccess = xtshService.delete(xtshDto);
		List<String> ids= new ArrayList<>();
		if(XtshDtos!=null && !XtshDtos.isEmpty()) {
			for (XtshDto dto : XtshDtos) {
				if ("1".equals(dto.getSfgb())) {
					ids.add(dto.getShid());
				}
			}
		}
		if(!ids.isEmpty()) {
			xtshDto.setIds(ids);
			if(isSuccess) {
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.xtsh.del",JSONObject.toJSONString(xtshDto));
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
	private boolean checkXSSValue(XtshDto xtshDto){
		if(xtshDto == null)
			return true;
		
		xtshDto.setShmc(StringUtil.changeXSSInfo(xtshDto.getShmc()));
		xtshDto.setMs(StringUtil.changeXSSInfo(xtshDto.getMs()));
		xtshDto.setKzcs(StringUtil.changeXSSInfo(xtshDto.getKzcs()));
		return true;
	}
	
	/**
	 * 系统审核流程新增页面
	 */
	@RequestMapping(value ="/audit/pagedataAuditProcess")
	public ModelAndView addAuditProcess(SpgwDto spgwDto){
		ModelAndView mav = new ModelAndView("systemmain/audit/audit_configProcess");
		mav.addObject("spgwDto", spgwDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 获取审核流程可选岗位
	 */
	@RequestMapping(value ="/configProcess/pageGetListUnSelectProcess")
	@ResponseBody
	public Map<String,Object> listUnSelectProcess(SpgwDto spgwDto){
		//根据shid查询未选岗位
		List<SpgwDto> t_List = spgwService.getPagedOptionalList(spgwDto);
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", spgwDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}

	/**
	 * 获取审核流程已选岗位
	 */
	@RequestMapping(value ="/configProcess/pageGetListSelectedProcess")
	@ResponseBody
	public Map<String,Object> listSelectedProcess(SpgwDto spgwDto){
		//根据shid查询已选岗位
		List<SpgwDto> t_List = spgwService.getPagedSelectedList(spgwDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", spgwDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 更新审核流程
	 */
	@RequestMapping(value ="/configProcess/pagedataUpdateProcess")
	@ResponseBody
	public Map<String,Object> updateProcess(SpgwDto spgwDto,HttpServletRequest request){
		//获取用户信息
		User user;
		user = getLoginInfo(request);
		spgwDto.setLrry(user.getYhid());
		try{
			boolean result = spgwService.updateProcess(spgwDto);
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
	 * 流程设置页面
	 */
	@RequestMapping(value ="/audit/ProcessInstall")
	@ResponseBody
	public ModelAndView getProcessInstallPage(String lclb) {
		ModelAndView mav=new ModelAndView("systemmain/audit/audit_install");
		JcsjDto jcsjDto=new JcsjDto();
		if(lclb!=null && !lclb.isEmpty()) {
			jcsjDto=jcsjService.getDtoById(lclb);
		}else {
			jcsjDto.setCsid("");
			jcsjDto.setCsmc("");
		}
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.PROCESS_TYPE});
		mav.addObject("process_type", jclist.get(BasicDataTypeEnum.PROCESS_TYPE.getCode()));
		mav.addObject("JcsjDto", jcsjDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
}
