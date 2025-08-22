package com.matridx.igams.sample.controller;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.DepartmentDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.SsdwTableEnum;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.sample.dao.entities.YbsqDto;
import com.matridx.igams.sample.dao.entities.YbsqssdwDto;
import com.matridx.igams.sample.service.svcinterface.IYbsqService;
import com.matridx.igams.sample.service.svcinterface.IYbsqssdwService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value="/sample")
public class SampApplyController extends BaseController{
	
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IYbsqService ybsqService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IYbsqssdwService ybsqssdwService;
	@Autowired
	ICommonService commonService;
	
	/**
	 * 标本申请列表页面
	 */
	@RequestMapping(value ="/apply/pageListSampApply")
	public ModelAndView pageListSampApply(){
		ModelAndView mav = new ModelAndView("sample/apply/sampapply_list");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{
				BasicDataTypeEnum.SAMPLE_TYPE
				,BasicDataTypeEnum.SAMPLE_USE});
		mav.addObject("yblxlist", jclist.get(BasicDataTypeEnum.SAMPLE_TYPE.getCode()));
		mav.addObject("ytlist", jclist.get(BasicDataTypeEnum.SAMPLE_USE.getCode()));
		
		return mav;
	}
	
	/**
	 * 标本列表
	 */
	@RequestMapping(value ="/apply/pageGetListSampApply")
	@ResponseBody
	public Map<String,Object> pageGetListSampApply(YbsqDto ybsqDto){
		List<YbsqDto> t_List = ybsqService.getPagedDtoList(ybsqDto);
		
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", ybsqDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}

	/**
	 * 标本查看
	 */
	@RequestMapping(value="/apply/viewSampApply")
	public ModelAndView viewSamp(YbsqDto ybsqDto){
		ModelAndView mav = new ModelAndView("sample/apply/sampapply_view");
		YbsqDto t_ybsqDto = ybsqService.getDtoById(ybsqDto.getSqid());
		mav.addObject("ybsqDto", t_ybsqDto);
		
		return mav;
	}

	/**
	 * 标本申请页面
	 */
	@RequestMapping(value="/apply/applySample")
	public ModelAndView addSampApply(YbsqDto ybsqDto, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("sample/apply/sampapply_edit");
		
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SAMPLE_USE});
		mav.addObject("ytlist", jclist.get(BasicDataTypeEnum.SAMPLE_USE.getCode()));
		
		YbsqDto t_ybsqDto = ybsqService.getYbDto(ybsqDto);
		t_ybsqDto.setFormAction("add");
		YbsqssdwDto ybsqssdwDto = new YbsqssdwDto();
		//获取用户信息
		User user = getLoginInfo();
		Map<String, Object> map = commonService.getDepartmentByUser(null, user.getYhid(), request);
		@SuppressWarnings("unchecked")
		List<DepartmentDto> departmentDtos = (List<DepartmentDto>) map.get("departmentDtos");
		if(departmentDtos != null && !departmentDtos.isEmpty()){
			mav.addObject("departmentDto", departmentDtos.get(0));
			ybsqssdwDto.setJgid(departmentDtos.get(0).getJgid());
			ybsqssdwDto.setJgmc(departmentDtos.get(0).getJgmc());
		}
		mav.addObject("ybsqDto",t_ybsqDto);
		mav.addObject("ybsqssdwDto", ybsqssdwDto);
		return mav;
	}
	
	/**
	 * 标本申请保存
	 */
	@RequestMapping(value = "/apply/applySaveSample")
	@ResponseBody
	public Map<String, Object> addSaveSampApply(YbsqDto ybsqDto){
		//获取用户信息
		User user = getLoginInfo();
		ybsqDto.setLrry(user.getYhid());
		ybsqDto.setSqry(user.getYhid());
		boolean isSuccess = ybsqService.addSaveSampApply(ybsqDto);
		Map<String,Object> map = new HashMap<>();
		map.put("ywid", ybsqDto.getSqid());
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 标本申请修改页面
	 */
	@RequestMapping(value = "/apply/modSampApply")
	public ModelAndView modSampApply(YbsqDto ybsqDto, HttpServletRequest request){
		ModelAndView mav = new ModelAndView("sample/apply/sampapply_edit");
		YbsqDto t_ybsqDto = ybsqService.getDto(ybsqDto);
		t_ybsqDto.setFormAction("mod");
		mav.addObject("ybsqDto", t_ybsqDto);
		
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SAMPLE_USE});

		mav.addObject("ytlist", jclist.get(BasicDataTypeEnum.SAMPLE_USE.getCode()));
		
		YbsqssdwDto ybsqssdwDto = new YbsqssdwDto();
		ybsqssdwDto.setSqid(ybsqDto.getSqid());
		YbsqssdwDto t_ybsqssdwDto = ybsqssdwService.getDto(ybsqssdwDto);
		if(t_ybsqssdwDto != null){
			ybsqssdwDto = t_ybsqssdwDto;
		}
		if(StringUtil.isNotBlank(ybsqssdwDto.getJgid())){
			Map<String, Object> map = commonService.getDepartmentById(null, ybsqssdwDto.getJgid(), request);
			@SuppressWarnings("unchecked")
			List<DepartmentDto> departmentDtos = (List<DepartmentDto>) map.get("departmentDtos");
			if(departmentDtos != null && departmentDtos.size() == 1){
				ybsqssdwDto.setJgmc(departmentDtos.get(0).getJgmc());
			}
		}
		mav.addObject("ybsqssdwDto", ybsqssdwDto);
		return mav;
	}
	
	/**
	 * 标本申请修改保存
	 */
	@RequestMapping(value = "/apply/modSaveSampApply")
	@ResponseBody
	public Map<String, Object> modSaveSampApply(YbsqDto ybsqDto){
		//获取用户信息
		User user = getLoginInfo();
		ybsqDto.setXgry(user.getYhid());
		boolean isSuccess = ybsqService.modSaveSampApply(ybsqDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 标本删除
	 */
	@RequestMapping(value = "/apply/delSampApply")
	@ResponseBody
	public Map<String, Object> delSampApply(YbsqDto ybsqDto){
		//获取用户信息
		User user = getLoginInfo();
		ybsqDto.setScry(user.getYhid());
		boolean isSuccess = ybsqService.delete(ybsqDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 标本申请审核列表页面
	 */
	@RequestMapping(value ="/apply/pageListSampApplyAudit")
	public ModelAndView pageListSampApplyAudit(){
		ModelAndView mav = new ModelAndView("sample/apply/sampapply_listaudit");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{
				BasicDataTypeEnum.SAMPLE_TYPE
				,BasicDataTypeEnum.SAMPLE_USE});
		mav.addObject("yblxlist", jclist.get(BasicDataTypeEnum.SAMPLE_TYPE.getCode()));
		mav.addObject("ytlist", jclist.get(BasicDataTypeEnum.SAMPLE_USE.getCode()));
		
		return mav;
	}
	
	/**
	 * 标本申请审核列表
	 */
	@RequestMapping(value ="/apply/pageGetListSampApplyAudit")
	@ResponseBody
	public Map<String,Object> pageGetSampApplyAudit(YbsqDto ybsqDto){
		
		//附加委托参数
		DataPermission.addWtParam(ybsqDto);
		//附加审核状态过滤
		if(GlobalString.AUDIT_SHZT_YSH.equals(ybsqDto.getDqshzt())){
			DataPermission.add(ybsqDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "sq", "sqid", AuditTypeEnum.AUDIT_SAMPAPPLY);
		}else{
			DataPermission.add(ybsqDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "sq", "sqid", AuditTypeEnum.AUDIT_SAMPAPPLY);
		}
		DataPermission.addJsDdw(ybsqDto, "sq", SsdwTableEnum.YBSQGL);
		
		List<YbsqDto> t_List = ybsqService.getPagedAuditList(ybsqDto);
		
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", ybsqDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	
}
