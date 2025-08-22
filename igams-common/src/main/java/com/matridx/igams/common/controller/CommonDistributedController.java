package com.matridx.igams.common.controller;

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

import com.matridx.igams.common.dao.entities.DepartmentDto;
import com.matridx.igams.common.dao.entities.SupplierDto;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.ISupplierService;

/**
 * 分布式
 * @author linwu
 *
 */
@Controller
@RequestMapping("/systemmain")
public class CommonDistributedController  extends BaseController {
	
	@Autowired
	ICommonService commonService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	
	@Autowired
	ISupplierService supplierService;
	
	/**
	 * 选择机构页面
	 */
	@RequestMapping("/department/pagedataDepartment")
	public ModelAndView pageListDepartment(DepartmentDto departmentDto){
		ModelAndView mav = new ModelAndView("common/department/list_department");
		mav.addObject("departmentDto", departmentDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 选择机构列表
	 */
	@RequestMapping("/department/pagedataListDepartment")
	@ResponseBody
	public Map<String, Object> listDepartment(DepartmentDto departmentDto, HttpServletRequest request){
		List<DepartmentDto> t_List = commonService.getPagedDepartmentList(departmentDto.getPrefix(), departmentDto, request);
		Map<String,Object> result = new HashMap<>();
		result.put("total", departmentDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 供应商列表
	 */
	@RequestMapping("/supplier/pageListSupplier")
	public ModelAndView pageListSupplier(SupplierDto supplierDto) {
		ModelAndView mav = new ModelAndView("common/spplier/list_supplier");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("supplierDto", supplierDto);
		return mav;
	}
	
	/**
	 * 列表数据
	 */
	@RequestMapping("/supplier/listSupplier")
	@ResponseBody
	public Map<String,Object> listSupplier(SupplierDto supplierDto){
		Map<String,Object> map= new HashMap<>();
		List<SupplierDto> list=supplierService.getPagedDtoList(supplierDto);
		map.put("total", supplierDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}
}
