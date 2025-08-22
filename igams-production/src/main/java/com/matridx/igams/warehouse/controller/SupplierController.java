package com.matridx.igams.warehouse.controller;

import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.SupplierDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.ISupplierService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.warehouse.dao.entities.GysxxDto;
import com.matridx.igams.warehouse.service.svcinterface.IGysxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/warehouse")
public class SupplierController extends BaseBasicController{
	
	@Autowired
	IGysxxService gysxxService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	ISupplierService supplierService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}

	/**
	 * 选择服务器页面
	 
	 */
	@RequestMapping("/supplier/copySupplier")
	public ModelAndView chanceSupplierTypePage(GysxxDto gysxxDto) {
		ModelAndView mav = new ModelAndView("warehouse/supplier/supplier_chance");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.JAVA_SERVER});
		mav.addObject("serverList", jclist.get(BasicDataTypeEnum.JAVA_SERVER.getCode()));
		mav.addObject("gysxxDto",gysxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}


	/**
	 * 跳转供应商列表界面
	 
	 */
	@RequestMapping(value = "/supplier/pageListSupplier")
	public ModelAndView getSupplierPageList() {
		ModelAndView mav=new ModelAndView("warehouse/supplier/supplierlist");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SUPPLIER_TYPE});
		mav.addObject("gfgllblist", jclist.get(BasicDataTypeEnum.SUPPLIER_TYPE.getCode()));//供应商管理类型
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 获取供应商列表
	 
	 */
	@RequestMapping(value = "/supplier/pageGetListSupplier")
	@ResponseBody
	public Map<String,Object> getSupplierList(GysxxDto gysxxDto){
		List<GysxxDto> gysxxlist=gysxxService.getPagedSupplierList(gysxxDto);
		Map<String, Object> map=new HashMap<>();
		map.put("total",gysxxDto.getTotalNumber());
		map.put("rows",gysxxlist);
		return map;
	}
	/**
	 * 钉钉获取供应商列表
	 */
	@RequestMapping(value = "/supplier/minidataGetListSupplier")
	@ResponseBody
	public Map<String,Object> minidataGetListSupplier(GysxxDto gysxxDto){
		return getSupplierList(gysxxDto);
	}
	
	/**
	 * 跳转至供应商新增界面
	 
	 */
	@RequestMapping(value = "/supplier/addSupplier")
	@ResponseBody
	public ModelAndView toAddSupplierPage(GysxxDto gysxxDto) {
		ModelAndView mav=new ModelAndView("warehouse/supplier/supplieradd");
		gysxxDto.setFormAction("addSaveSupplier");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.SUPPLIER_TYPE,BasicDataTypeEnum.PROVINCE});
		mav.addObject("supplierlist", jclist.get(BasicDataTypeEnum.SUPPLIER_TYPE.getCode()));//供应商管理类型
		mav.addObject("sflist", jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));
		mav.addObject("GysxxDto", gysxxDto);
		mav.addObject("fjcfbDto",new FjcfbDto());
		mav.addObject("ywlx", BusTypeEnum.IMP_SUBSUPPLIER.getCode());
		mav.addObject("mbywlx", BusTypeEnum.IMP_FORMWORKSUBSUPPLIER.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 提交保存供应商信息
	 * @param gysxxDto
	 
	 */
	@RequestMapping(value = "/supplier/addSaveSupplier")
	@ResponseBody
	public Map<String,Object> AddsaveSupplierxx(GysxxDto gysxxDto, HttpServletRequest request){
		User user = getLoginInfo(request);
		gysxxDto.setLrry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess = true;
		if(!"/".equals(gysxxDto.getGfbh())){
			isSuccess = gysxxService.queryByGfbh(gysxxDto);
		}
		if(isSuccess){
			isSuccess = gysxxService.getDtoByGysmcAndJc(gysxxDto);
			if(isSuccess) {
				isSuccess=gysxxService.insertDto(gysxxDto);
				map.put("status", isSuccess ? "success" : "fail");
				map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
			}else {
				map.put("status","fail");
				map.put("message",xxglService.getModelById("ICOM99050").getXxnr());
			}
		}else{
			map.put("status","fail");
			map.put("message","供方编号已存在，请修改供方编号！");
		}
		return map;
	}
	
	/**
	 * 查看供应商信息
	 * @param gysxxDto
	 
	 */
	@RequestMapping(value = "/supplier/viewSupplier")
	public ModelAndView viewSupplierxx(GysxxDto gysxxDto) {
		ModelAndView mav=new ModelAndView("warehouse/supplier/supplierview");
		GysxxDto gysxx=gysxxService.selectGysxxByGysid(gysxxDto);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(gysxxDto.getGysid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBSUPPLIER.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		if (!CollectionUtils.isEmpty(fjcfbDtos)){
			Date now = DateUtils.parseDate("yyyy.MM.dd", DateUtils.getCustomFomratCurrentDate("yyyy.MM.dd"));
			for (FjcfbDto dto : fjcfbDtos) {
				String wjm = dto.getWjm();
				wjm = wjm.replaceAll("）",")");
				if (wjm.lastIndexOf(")")!=-1&&wjm.lastIndexOf("-")!=-1&&wjm.lastIndexOf(")")>wjm.lastIndexOf("-")){
					String jssj = wjm.substring(wjm.lastIndexOf("-") + 1, wjm.lastIndexOf(")"));
					Date date = DateUtils.parseDate("yyyy.MM.dd",jssj);
					if (date!=null){
						if (date.getTime()>=now.getTime()){
							dto.setSfgq("未过期");
						}else {
							dto.setSfgq("已过期");
						}
					}
				}
			}
		}
		FjcfbDto fjcfbDto_t=new FjcfbDto();
		fjcfbDto_t.setYwid(gysxxDto.getGysid());
		fjcfbDto_t.setYwlx(BusTypeEnum.IMP_FORMWORKSUBSUPPLIER.getCode());
		List<FjcfbDto> fjcfbDtoList=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto_t);
		mav.addObject("t_fjcfbDtos",fjcfbDtos);
		mav.addObject("fjcfbDtoList",fjcfbDtoList);
		mav.addObject("GysxxDto", gysxx);
		mav.addObject("nowStr", DateUtils.getCustomFomratCurrentDate(new Date(),"yyyy.MM.dd"));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 查看供应商信息
	 * @param gysxxDto
	 
	 */
	@RequestMapping(value = "/supplier/pagedataViewSupplier")
	public ModelAndView pagedataViewSupplier(GysxxDto gysxxDto) {
		return viewSupplierxx(gysxxDto);
	}
	/**
	 * 获取信息钉钉
	 * @param gysxxDto
	 
	 */
	@RequestMapping(value = "/supplier/minidataViewSupplierxx")
	@ResponseBody
	public Map<String,Object> minidataViewSupplierxx(GysxxDto gysxxDto){
		Map<String, Object> map = new HashMap<>();
		GysxxDto gysxx=gysxxService.selectGysxxByGysid(gysxxDto);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(gysxxDto.getGysid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBSUPPLIER.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		map.put("gysxx",gysxx);
		map.put("fjcfbDtos",fjcfbDtos);
		return map;
	}
	
	/**
	 * 跳转至修改界面
	 * @param gysxxDto
	 
	 */
	@RequestMapping(value = "/supplier/modSupplier")
	public ModelAndView modSupplierxx(GysxxDto gysxxDto) {
		ModelAndView mav=new ModelAndView("warehouse/supplier/supplieradd");
		GysxxDto gysxx=gysxxService.selectGysxxByGysid(gysxxDto);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.SUPPLIER_TYPE,BasicDataTypeEnum.PROVINCE});
		mav.addObject("supplierlist", jclist.get(BasicDataTypeEnum.SUPPLIER_TYPE.getCode()));//供应商管理类型
		mav.addObject("sflist", jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));
		gysxx.setFormAction("modSaveSupplier");
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(gysxxDto.getGysid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBSUPPLIER.getCode());
		List<FjcfbDto> dto = fjcfbService.getDtoList(fjcfbDto);
		FjcfbDto fjcfbDto_t=new FjcfbDto();
		fjcfbDto_t.setYwid(gysxxDto.getGysid());
		fjcfbDto_t.setYwlx(BusTypeEnum.IMP_FORMWORKSUBSUPPLIER.getCode());
		List<FjcfbDto> fjcfblist = fjcfbService.getDtoList(fjcfbDto_t);
		mav.addObject("fjcfbDtos",dto);
		mav.addObject("fjcfblist",fjcfblist);
		mav.addObject("ywlx", BusTypeEnum.IMP_SUBSUPPLIER.getCode());
		mav.addObject("mbywlx", BusTypeEnum.IMP_FORMWORKSUBSUPPLIER.getCode());
		mav.addObject("GysxxDto", gysxx);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 修改保存供应商信息
	 * @param gysxxDto
	 
	 */
	@RequestMapping(value ="/supplier/modSaveSupplier")
	@ResponseBody
	public Map<String,Object> modSaveSupplierxx(GysxxDto gysxxDto, HttpServletRequest request){
		User user = getLoginInfo(request);
		gysxxDto.setXgry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess = true;
		if(!"/".equals(gysxxDto.getGfbh())){
			isSuccess = gysxxService.queryByGfbh(gysxxDto);
		}
		if(isSuccess){
			isSuccess = gysxxService.getDtoByGysmcAndJc(gysxxDto);
			if(isSuccess) {
				isSuccess=gysxxService.updateDto(gysxxDto);
				map.put("status", isSuccess ? "success" : "fail");
				map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());

			}else {
				map.put("status","fail");
				map.put("message",xxglService.getModelById("ICOM99050").getXxnr());
			}
		}else {
			map.put("status","fail");
			map.put("message","供方编号已存在，请修改供方编号！");
		}
		return map;
	}
	
	/**
	 * 删除供应商信息
	 * @param gysxxDto
	 
	 */
	@RequestMapping(value ="/supplier/delSupplier")
	@ResponseBody
	public Map<String,Object> deleteGysxx(GysxxDto gysxxDto, HttpServletRequest request){
		User user = getLoginInfo(request);
		gysxxDto.setScry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess=gysxxService.deleteByGysids(gysxxDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	/**
	 * 停用供应商信息
	 * @param gysxxDto
	 
	 */
	@RequestMapping(value ="/supplier/disableSupplier")
	@ResponseBody
	public ModelAndView disableSupplier(GysxxDto gysxxDto, HttpServletRequest request){
		ModelAndView mav=new ModelAndView("warehouse/supplier/supplierdisable");
		User user = getLoginInfo(request);
		GysxxDto dtoById = gysxxService.getDtoById(gysxxDto.getGysid());
		gysxxDto.setBz(dtoById.getBz());
		gysxxDto.setTyry(user.getYhid());
		gysxxDto.setTyrymc(user.getZsxm());
		gysxxDto.setTysj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("gysxxDto", gysxxDto);
		return mav;
	}
	/**
	 * 停用保存供应商信息
	 * @param gysxxDto
	 
	 */
	@RequestMapping(value ="/supplier/disableSaveSupplier")
	@ResponseBody
	public Map<String,Object> disableSaveSupplier(GysxxDto gysxxDto){
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess;
		try {
			isSuccess = gysxxService.disableSupplier(gysxxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr(): xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
		}
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 启用供应商信息
	 * @param gysxxDto
	 */
	@RequestMapping(value ="/supplier/enableSupplier")
	@ResponseBody
	public Map<String,Object> enableSupplier(GysxxDto gysxxDto){
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess;
		try {
			isSuccess = gysxxService.enableSupplier(gysxxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr(): xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
		}
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 模糊查询供应商
	 * @param gysxxDto
	 
	 */
	@RequestMapping("/supplier/selectBygysmc")
	@ResponseBody
	public Map<String, Object> selectBygysmc(GysxxDto gysxxDto){
		Map<String, Object> map=new HashMap<>();
		List<GysxxDto> gysList=gysxxService.selectBygysmc(gysxxDto);
		map.put("gysList",gysList);
		map.put("gysxxDto",gysxxDto);
		return map;
	}
	
	/**
	 * 跳转至复制界面
	 * @param gysxxDto
	 
	 */
	@RequestMapping(value = "/supplier/pagedataCopysupplier")
	public ModelAndView copysupplier(GysxxDto gysxxDto) {
		ModelAndView mav=new ModelAndView("warehouse/supplier/supplieradd");
		GysxxDto gysxx=gysxxService.selectGysxxByGysid(gysxxDto);
		JcsjDto jcsjDto = gysxxService.getServer(gysxxDto.getServer());
		gysxx.setServer(jcsjDto.getCsdm()+jcsjDto.getCskz1());
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.SUPPLIER_TYPE,BasicDataTypeEnum.PROVINCE});
		mav.addObject("supplierlist", jclist.get(BasicDataTypeEnum.SUPPLIER_TYPE.getCode()));//供应商管理类型
		mav.addObject("sflist", jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));
		gysxx.setFormAction("copySaveSupplier");
		mav.addObject("GysxxDto", gysxx);
		mav.addObject("JcsjDto", jcsjDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 发送供应商信息
	 * @param gysxxDto
	 
	 */
	@RequestMapping(value = "/supplier/copySaveSupplier")
	@ResponseBody
	public Map<String,Object> copysendsupplier(GysxxDto gysxxDto,JcsjDto jcsjDto,HttpServletRequest request){
		RestTemplate restTemplate = new RestTemplate();
		User user = getLoginInfo(request);
		MultiValueMap<String, Object> xtjsparamMap = new LinkedMultiValueMap<>();
		xtjsparamMap.add("yhid", user.getYhid());
		xtjsparamMap.add("gysmc", gysxxDto.getGysmc());
		xtjsparamMap.add("gysdm", gysxxDto.getGysdm());
		xtjsparamMap.add("gysjc", gysxxDto.getGysjc());
		xtjsparamMap.add("dq", gysxxDto.getDq());
		xtjsparamMap.add("fzrq", gysxxDto.getFzrq());
		xtjsparamMap.add("lxr", gysxxDto.getLxr());
		xtjsparamMap.add("dh", gysxxDto.getDh());
		xtjsparamMap.add("sj", gysxxDto.getSj());
		xtjsparamMap.add("qq", gysxxDto.getQq());
		xtjsparamMap.add("wx", gysxxDto.getWx());
		xtjsparamMap.add("cz", gysxxDto.getCz());
		xtjsparamMap.add("yx", gysxxDto.getYx());
		xtjsparamMap.add("khh", gysxxDto.getKhh());
		xtjsparamMap.add("zh", gysxxDto.getZh());
		xtjsparamMap.add("sl", gysxxDto.getSl());
		xtjsparamMap.add("gfgllbmc", gysxxDto.getGfgllbmc());
		xtjsparamMap.add("gfgllb", gysxxDto.getGfgllb());
		xtjsparamMap.add("sfmc", gysxxDto.getSfmc());
		xtjsparamMap.add("sf", gysxxDto.getSf());
		xtjsparamMap.add("bz", gysxxDto.getBz());
		String xtjsurl=jcsjDto.getCskz1()+"/ws/supplier/savesupplier";
		@SuppressWarnings("unchecked")
		Map<String,Object> gysmap=  restTemplate.postForObject(xtjsurl, xtjsparamMap, Map.class);
		return gysmap;
	}
	/**
	 * 供应商列表
	 * @param supplierDto
	 
	 */
	@RequestMapping("/supplier/pagedataSupplier")
	public ModelAndView pageListSupplier(SupplierDto supplierDto) {
		ModelAndView mav = new ModelAndView("warehouse/supplier/list_supplier");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("supplierDto", supplierDto);
		return mav;
	}
	/**
	 * 列表数据
	 * @param supplierDto
	 
	 */
	@RequestMapping("/supplier/pagedataListSupplier")
	@ResponseBody
	public Map<String,Object> pagedataListSupplier(SupplierDto supplierDto){
		Map<String,Object> map=new HashMap<>();
		List<SupplierDto> list=supplierService.getPagedDtoList(supplierDto);
		map.put("total", supplierDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}
	/**
	 * 货期设置
	 * @param gysxxDto
	 
	 */
	@RequestMapping("/supplier/deliverysetSupplier")
	public ModelAndView deliverysetSupplier(GysxxDto gysxxDto) {
		ModelAndView mav = new ModelAndView("warehouse/supplier/supplier_deliveryset");
		gysxxDto.setFormAction("deliverysetSaveSupplier");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("gysxxDto", gysxxDto);
		return mav;
	}
	/**
	 * 删除供应商信息
	 * @param gysxxDto
	 
	 */
	@RequestMapping(value ="/supplier/deliverysetSaveSupplier")
	@ResponseBody
	public Map<String,Object> deliverysetSaveSupplier(GysxxDto gysxxDto, HttpServletRequest request){
		User user = getLoginInfo(request);
		gysxxDto.setXgry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess=gysxxService.update(gysxxDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 供应商附件下载
	 *
	 * @param gysxxDto
	 
	 */
	@RequestMapping("/supplier/downloadSupplier")
	@ResponseBody
	public Map<String, Object> downloadSupplier(GysxxDto gysxxDto) {
		return gysxxService.downloadSupplier(gysxxDto);
	}
}
