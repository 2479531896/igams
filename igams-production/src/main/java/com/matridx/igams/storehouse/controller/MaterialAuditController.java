package com.matridx.igams.storehouse.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.SsdwTableEnum;
import com.matridx.igams.storehouse.dao.entities.LlglDto;
import com.matridx.igams.storehouse.service.svcinterface.ILlglService;

@Controller
@RequestMapping("/storehouse")
public class MaterialAuditController extends BaseController{
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	private ILlglService llglService;
	
	@Override
	public String getPrefix() {
		return urlPrefix;
	}
	
	/**
	 * 领料申请审核列表（get）
	 * @return
	 */
	@RequestMapping(value ="/materialAudit/pageListMaterialAudit")
	public ModelAndView PageListReceiveMateriel() {
		ModelAndView mav = new ModelAndView("storehouse/materialAudit/materialAudit_listaudit");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 获取领料申请审核列表
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping("/materialAudit/pageGetListMaterialAudit")
	@ResponseBody
	public Map<String, Object> getPagedListMaterialAudit(LlglDto llglDto,HttpServletRequest request) {
//		=================================
		//附加委托参数
		DataPermission.addWtParam(llglDto);
		//附加审核状态过滤
		List<AuditTypeEnum> auditTypes = new ArrayList<>();
		auditTypes.add(AuditTypeEnum.AUDIT_GOODS_APPLY);
		auditTypes.add(AuditTypeEnum.AUDIT_GOODS_APPLY_DING);
		auditTypes.add(AuditTypeEnum.AUDIT_GOODS_DEVICE);
		if(GlobalString.AUDIT_SHZT_YSH.equals(llglDto.getDqshzt())){
			DataPermission.add(llglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "llgl", "llid",auditTypes);
		}else{
			DataPermission.add(llglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "llgl", "llid",auditTypes);
		}
		DataPermission.addCurrentUser(llglDto, getLoginInfo(request));
		DataPermission.addSpDdw(llglDto, "llgl", SsdwTableEnum.LLGL);
		List<LlglDto> llglDtos = llglService.getPagedAuditLlgl(llglDto);
//		=================================
		Map<String, Object> map = new HashMap<>();
		map.put("total", llglDto.getTotalNumber());
		map.put("rows", llglDtos);
		return map;
	}
	
	
	/**
	 * 查看领料申请审核信息
 	*/
	@RequestMapping(value = "/materialAudit/viewMaterialAudit") 
	@ResponseBody
	public ModelAndView getDtoMaterialAuditByllid(LlglDto llglDto) { 
		ModelAndView mav = new ModelAndView("storehouse/materialAudit/materialAudit_view"); 
		llglDto = llglService.getDtoById(llglDto.getLlid()); 
		mav.addObject("llglDto", llglDto);
		mav.addObject("urlPrefix", urlPrefix); 
		return mav; 
	}		

}
