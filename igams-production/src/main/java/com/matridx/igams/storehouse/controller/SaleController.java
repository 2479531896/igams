package com.matridx.igams.storehouse.controller;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/storehouse")
public class SaleController extends BaseBasicController {

	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Override
	public String getPrefix() {
		return urlPrefix;
	}
	@Autowired
	private IXsglService xsglService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private IXsmxService xsmxService;
	@Autowired
	private IXxglService xxglService;
	@Autowired
	IShgcService shgcService;
	@Autowired
	ICommonService commonService;
	@Autowired
	IJcjyxxService jcjyxxService;
	@Autowired
	IJcjyglService jcjyglService;
	@Autowired
	IDkjejlService dkjejlService;
	@Autowired
	IFhmxService fhmxService;

	private final Logger log = LoggerFactory.getLogger(SaleController.class);

	/**
	 * 跳转销售列表
	 *
	 * @return
	 */

	@RequestMapping("/sale/pageListSale")
	public ModelAndView pageListSale(HttpServletRequest request,XsglDto xsglDto) {
		ModelAndView mav = new ModelAndView("storehouse/sale/sale_list");
		xsglDto.setAuditType(AuditTypeEnum.AUDIT_SALEORDER.getCode());
		List<JcsjDto> khlblist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CUSTOMER_CATEGORY.getCode());
		mav.addObject("khlblist",khlblist);
		mav.addObject("xsglDto",xsglDto);
		mav.addObject("urlPrefix", urlPrefix);
		//字段限制标记 1 限制 0不限制
		mav.addObject("zdxz", request.getParameter("zdxz"));
		return mav;
	}

	/**
	 * 获取销售列表数据
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/pageGetListSale")
	@ResponseBody
	public Map<String, Object> getPageDtoSaleList(XsglDto xsglDto) {
		Map<String, Object> map = new HashMap<>();
		List<XsglDto> xsglDtos = xsglService.getPagedDtoList(xsglDto);
		try{
			shgcService.addShgcxxByYwid(xsglDtos, AuditTypeEnum.AUDIT_SALEORDER.getCode(), "zt", "xsid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		map.put("total", xsglDto.getTotalNumber());
		map.put("rows", xsglDtos);
		return map;
	}
	/**
	 * 获取销售列表数据钉钉
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/minidataGetListSale")
	@ResponseBody
	public Map<String, Object> minidataGetListSale(XsglDto xsglDto) {
		return getPageDtoSaleList(xsglDto);
	}

	/**
	 * 销售列表查看
	 * @return
	 */
	@RequestMapping("/sale/viewSale")
	public ModelAndView viewSale(XsglDto xsglDto,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/sale/sale_view");
		XsglDto dtoById = xsglService.getDtoById(xsglDto.getXsid());
		if (StringUtil.isNotBlank(dtoById.getJcjyid())){
			JcjyxxDto jcjyxxDto = new JcjyxxDto();
			jcjyxxDto.setJcjyid(dtoById.getJcjyid());
			List<JcjyxxDto> dtoListInfo = jcjyxxService.getDtoListInfo(jcjyxxDto);
			mav.addObject("jcjyxxDtos", dtoListInfo);
		}else {
			mav.addObject("jcjyxxDtos", null);
		}
		String zdxz = request.getParameter("zdxz");
		if ("0".equals(zdxz)){
			XsmxDto xsmxDto = new XsmxDto();
			xsmxDto.setXsid(xsglDto.getXsid());
			List<XsmxDto> xsmxDtos = xsmxService.getDkxxByXsid(xsmxDto);
			List<String> xsmxids = xsmxDtos.stream().map(XsmxDto::getXsmxid).distinct().collect(Collectors.toList());
			XsmxDto xsmxDto_fh = new XsmxDto();
			xsmxDto_fh.setIds(xsmxids);
			List<XsmxDto>  xsmxDtos_fh = xsmxService.getDkxxGroup(xsmxDto_fh);
			for (XsmxDto dto : xsmxDtos) {
				List<XsmxDto> list = new ArrayList<>();
				for (XsmxDto xsmxDto_z : xsmxDtos_fh) {
					if (dto.getXsmxid().equals(xsmxDto_z.getXsmxid())){
						list.add(xsmxDto_z);
					}
				}
				dto.setXsmxDtos(list);
			}
			mav.addObject("xsmxDtos", xsmxDtos);
			mav.addObject("xsmxDto", xsmxDto);
			//flag==view显示删除和确定按钮
			mav.addObject("flag", "view");
		}
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("xsglDto", dtoById);
		mav.addObject("zdxz", zdxz);
		return mav;
	}
	/**
	 * 销售列表查看
	 * @return
	 */
	@RequestMapping("/sale/pagedataViewSale")
	public ModelAndView pagedataViewSale(XsglDto xsglDto) {
		ModelAndView mav = new ModelAndView("storehouse/sale/sale_view");
		XsglDto dtoById = xsglService.getDtoById(xsglDto.getXsid());
		if (StringUtil.isNotBlank(dtoById.getJcjyid())){
			JcjyxxDto jcjyxxDto = new JcjyxxDto();
			jcjyxxDto.setJcjyid(dtoById.getJcjyid());
			List<JcjyxxDto> dtoListInfo = jcjyxxService.getDtoListInfo(jcjyxxDto);
			mav.addObject("jcjyxxDtos", dtoListInfo);
		}else {
			mav.addObject("jcjyxxDtos", null);
		}
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("xsglDto", dtoById);
		return mav;
	}
	/**
	 * 获取销售列表查看钉钉
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/minidataViewSale")
	@ResponseBody
	public Map<String, Object> minidataViewSale(XsglDto xsglDto) {
		Map<String, Object> map = new HashMap<>();
		XsglDto dtoById = xsglService.getDtoById(xsglDto.getXsid());
		XsmxDto xsmxDto=new XsmxDto();
		xsmxDto.setXsid(xsglDto.getXsid());
		List<XsmxDto> list = xsmxService.getDtoList(xsmxDto);
		map.put("xsglDto", dtoById);
		map.put("xsglDtos", list);
		return map;
	}

	/**
	 * 销售列表查看
	 * @return
	 */
	@RequestMapping("/sale/viewShipSale")
	public ModelAndView viewShipSale(XsglDto xsglDto) {
		ModelAndView mav = new ModelAndView("storehouse/sale/sale_view");
		XsglDto dtoById = xsglService.getDto(xsglDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("xsglDto", dtoById);
		return mav;
	}

	/**
	 * 销售列表查看
	 * @return
	 */
	@RequestMapping("/sale/pagedataViewShipSale")
	public ModelAndView pagedataViewShipSale(XsglDto xsglDto) {
		ModelAndView mav = new ModelAndView("storehouse/sale/sale_view");
		XsglDto dtoById = xsglService.getDto(xsglDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("xsglDto", dtoById);
		return mav;
	}
	/**
	 * 获取销售列表明细数据
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/pagedataGetViewSaleMx")
	@ResponseBody
	public Map<String, Object> getViewSaleMx(XsglDto xsglDto) {
		Map<String, Object> map = new HashMap<>();
		XsmxDto xsmxDto=new XsmxDto();
		xsmxDto.setXsid(xsglDto.getXsid());
		List<XsmxDto> list = xsmxService.getDtoList(xsmxDto);
		map.put("rows",list);
		return map;
	}

	/**
	 * 销售列表新增页面
	 */
	@RequestMapping("/sale/addSale")
	public ModelAndView addSale(XsglDto xsglDto,HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView("storehouse/sale/sale_edit");
		modelAndView.addObject("url", "/storehouse/sale/pagedataSaledetails");
		xsglDto.setFormAction("addSaveSale");
		User user = getLoginInfo(request);
		xsglDto.setYwy(user.getYhid());// 默认
		//获取默认部门
		user = commonService.getUserInfoById(user);
		if (user != null) {
			xsglDto.setYwymc(user.getZsxm());
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String now=sdf.format(date);
		xsglDto.setDdrq(now);
		String[] split = now.split("-");
		String year=split[0];
		String month=split[1];
		String day=split[2];
		String s="XS-"+year+month+day;
		String oAxsddh = xsglService.getOAxsddh(s);
		xsglDto.setOaxsdh(oAxsddh);
		xsglDto.setSuil("13");
		modelAndView.addObject("xsglDto", xsglDto);
		modelAndView.addObject("auditType", AuditTypeEnum.AUDIT_SALEORDER.getCode());
		modelAndView.addObject("urlPrefix", urlPrefix);
		List<JcsjDto> xslist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode());
		List<JcsjDto> ywlxlist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.TYPEOF_SERVICE.getCode());
		List<JcsjDto> sflist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PROVINCE.getCode());
		List<JcsjDto> ysfslist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DELIVERY_METHOD.getCode());
		modelAndView.addObject("xslist", xslist);
		modelAndView.addObject("ysfslist", ysfslist);
		modelAndView.addObject("ywlxlist", ywlxlist);
		modelAndView.addObject("sflist", sflist);
		modelAndView.addObject("cplxlist", JSON.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCT_CLASS.getCode())));
		return modelAndView;
	}

	/**
	 * 新增保存
	 *
	 * @param xsglDto
	 * @return
	 */
	@RequestMapping("/sale/addSaveSale")
	@ResponseBody
	public Map<String, Object> addSaveSale(XsglDto xsglDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		XsglDto dto = xsglService.getDto(xsglDto);
		if(dto!=null){
			map.put("status", "fail");
			map.put("message", "OA订单号重复！");
			return map;
		}
		User user = getLoginInfo(request);
		xsglDto.setLrry(user.getYhid());
		xsglDto.setZt(StatusEnum.CHECK_NO.getCode());
		xsglDto.setDjlx("0");
		boolean isSuccess = xsglService.insertDto(xsglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid",xsglDto.getXsid());
		map.put("auditType", AuditTypeEnum.AUDIT_SALEORDER.getCode());
		map.put("urlPrefix",urlPrefix);
		return map;
	}

	/**
	 * 修改
	 * @return
	 */
	@RequestMapping("/sale/modSale")
	public ModelAndView modSale(XsglDto xsglDto) {
		ModelAndView mav = new ModelAndView("storehouse/sale/sale_edit");
		mav.addObject("url", "/storehouse/sale/pagedataSaledetails");
		XsglDto dtoById = xsglService.getDtoById(xsglDto.getXsid());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("auditType", AuditTypeEnum.AUDIT_SALEORDER.getCode());
		dtoById.setFormAction("modSaveSale");
		List<JcsjDto> xslist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode());
		List<JcsjDto> ywlxlist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.TYPEOF_SERVICE.getCode());
		List<JcsjDto> sflist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PROVINCE.getCode());
		List<JcsjDto> ysfslist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DELIVERY_METHOD.getCode());
		mav.addObject("cplxlist", JSON.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCT_CLASS.getCode())));
		mav.addObject("xslist", xslist);
		mav.addObject("ywlxlist", ywlxlist);
		mav.addObject("sflist", sflist);
		mav.addObject("ysfslist", ysfslist);
		mav.addObject("xsglDto", dtoById);
		return mav;
	}

	/**
	 * 修改保存
	 *
	 * @param xsglDto
	 * @return
	 */
	@RequestMapping("/sale/modSaveSale")
	@ResponseBody
	public Map<String, Object> modSaveSale(XsglDto xsglDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		XsglDto dto = xsglService.verify(xsglDto);
		if(dto!=null){
			map.put("status", "fail");
			map.put("message", "OA订单号重复！");
			return map;
		}
		User user = getLoginInfo(request);
		xsglDto.setXgry(user.getYhid());
		xsglDto.setZt(StatusEnum.CHECK_NO.getCode());
		boolean isSuccess = xsglService.updateDto(xsglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid",xsglDto.getXsid());
		map.put("auditType", AuditTypeEnum.AUDIT_SALEORDER.getCode());
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 复制
	 * @return
	 */
	@RequestMapping("/sale/copySale")
	public ModelAndView copySale(XsglDto xsglDto) {
		ModelAndView mav = new ModelAndView("storehouse/sale/sale_edit");
		mav.addObject("url", "/storehouse/sale/pagedataSaledetails");
		XsglDto dtoById = xsglService.getDtoById(xsglDto.getXsid());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String now=sdf.format(date);
		xsglDto.setDdrq(now);
		String[] split = now.split("-");
		String year=split[0];
		String month=split[1];
		String day=split[2];
		String s="XS-"+year+month+day;
		String oAxsddh = xsglService.getOAxsddh(s);
		dtoById.setOaxsdh(oAxsddh);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("auditType", AuditTypeEnum.AUDIT_SALEORDER.getCode());
		dtoById.setFormAction("copySaveSale");
		List<JcsjDto> sflist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PROVINCE.getCode());
		List<JcsjDto> xslist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode());
		List<JcsjDto> ywlxlist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.TYPEOF_SERVICE.getCode());
		List<JcsjDto> ysfslist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DELIVERY_METHOD.getCode());
		mav.addObject("cplxlist", JSON.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCT_CLASS.getCode())));
		mav.addObject("xslist", xslist);
		mav.addObject("sflist", sflist);
		mav.addObject("ywlxlist", ywlxlist);
		mav.addObject("ysfslist", ysfslist);
		mav.addObject("xsglDto", dtoById);
		return mav;
	}
	/**
	 * 复制保存
	 *
	 * @param xsglDto
	 * @return
	 */
	@RequestMapping("/sale/copySaveSale")
	@ResponseBody
	public Map<String, Object> copySaveSale(XsglDto xsglDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		XsglDto dto = xsglService.verify(xsglDto);
		if(dto!=null){
			map.put("status", "fail");
			map.put("message", "OA订单号重复！");
			return map;
		}
		User user = getLoginInfo(request);
		xsglDto.setLrry(user.getYhid());
		xsglDto.setZt(StatusEnum.CHECK_NO.getCode());
		xsglDto.setDjlx("0");
		boolean isSuccess = xsglService.insertDto(xsglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid",xsglDto.getXsid());
		map.put("auditType", AuditTypeEnum.AUDIT_SALEORDER.getCode());
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 修改
	 * @return
	 */
	@RequestMapping("/sale/auditSale")
	public ModelAndView auditSale(XsglDto xsglDto) {
		ModelAndView mav = new ModelAndView("storehouse/sale/sale_edit");
		mav.addObject("url", "/storehouse/sale/pagedataSaledetails");
		XsglDto dtoById = xsglService.getDtoById(xsglDto.getXsid());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("auditType", AuditTypeEnum.AUDIT_SALEORDER.getCode());
		dtoById.setFormAction("auditSaveSale");
		List<JcsjDto> xslist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode());
		List<JcsjDto> ywlxlist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.TYPEOF_SERVICE.getCode());
		List<JcsjDto> sflist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PROVINCE.getCode());
		List<JcsjDto> ysfslist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DELIVERY_METHOD.getCode());
		mav.addObject("cplxlist", JSON.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCT_CLASS.getCode())));
		mav.addObject("xslist", xslist);
		mav.addObject("ywlxlist", ywlxlist);
		mav.addObject("sflist", sflist);
		mav.addObject("ysfslist", ysfslist);
		mav.addObject("xsglDto", dtoById);
		return mav;
	}

	/**
	 * 修改保存
	 *
	 * @param xsglDto
	 * @return
	 */
	@RequestMapping("/sale/auditSaveSale")
	@ResponseBody
	public Map<String, Object> auditSaveSale(XsglDto xsglDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		XsglDto dto = xsglService.verify(xsglDto);
		if(dto!=null){
			map.put("status", "fail");
			map.put("message", "OA订单号重复！");
			return map;
		}
		User user = getLoginInfo(request);
		xsglDto.setXgry(user.getYhid());
		xsglDto.setZt(StatusEnum.CHECK_NO.getCode());
		boolean isSuccess = xsglService.updateDto(xsglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid",xsglDto.getXsid());
		map.put("auditType", AuditTypeEnum.AUDIT_SALEORDER.getCode());
		map.put("urlPrefix",urlPrefix);
		return map;
	}

	/**
	 * 提交
	 * @return
	 */
	@RequestMapping("/sale/submitSale")
	public ModelAndView submitSale(XsglDto xsglDto) {
		ModelAndView mav = new ModelAndView("storehouse/sale/sale_edit");
		mav.addObject("url", "/storehouse/sale/pagedataSaledetails");
		XsglDto dtoById = xsglService.getDtoById(xsglDto.getXsid());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("auditType", AuditTypeEnum.AUDIT_SALEORDER.getCode());
		dtoById.setFormAction("submitSaveSale");
		List<JcsjDto> xslist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode());
		List<JcsjDto> ywlxlist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.TYPEOF_SERVICE.getCode());
		List<JcsjDto> sflist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PROVINCE.getCode());
		List<JcsjDto> ysfslist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DELIVERY_METHOD.getCode());
		mav.addObject("cplxlist", JSON.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCT_CLASS.getCode())));
		mav.addObject("xslist", xslist);
		mav.addObject("ywlxlist", ywlxlist);
		mav.addObject("sflist", sflist);
		mav.addObject("ysfslist", ysfslist);
		mav.addObject("xsglDto", dtoById);
		return mav;
	}

	/**
	 * 提交保存保存
	 *
	 * @param xsglDto
	 * @return
	 */
	@RequestMapping("/sale/submitSaveSale")
	@ResponseBody
	public Map<String, Object> submitSaveSale(XsglDto xsglDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		XsglDto dto = xsglService.verify(xsglDto);
		if(dto!=null){
			map.put("status", "fail");
			map.put("message", "OA订单号重复！");
			return map;
		}
		User user = getLoginInfo(request);
		xsglDto.setXgry(user.getYhid());
		xsglDto.setZt(StatusEnum.CHECK_NO.getCode());
		boolean isSuccess = xsglService.updateDto(xsglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid",xsglDto.getXsid());
		map.put("auditType", AuditTypeEnum.AUDIT_SALEORDER.getCode());
		map.put("urlPrefix",urlPrefix);
		return map;
	}


	/**
	 * 获取明细
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/pagedataSaledetails")
	@ResponseBody
	public Map<String, Object> getSaleDetails(XsglDto xsglDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(xsglDto.getJcjyid())){
			JcjyxxDto jcjyxxDto = new JcjyxxDto();
			jcjyxxDto.setJcjyid(xsglDto.getJcjyid());
			List<JcjyxxDto> dtoList = jcjyxxService.getDtoListInfo(jcjyxxDto);
			map.put("rows",dtoList);
		}else {
			List<XsmxDto> dtoList =new ArrayList<>();
			if(StringUtil.isNotBlank(xsglDto.getXsid())){
				XsmxDto xsmxDto=new XsmxDto();
				xsmxDto.setXsid(xsglDto.getXsid());
				dtoList = xsmxService.getListById(xsmxDto);
			}
			map.put("rows",dtoList);
		}
		return map;
	}

	/**
	 * 废弃
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/discardSale")
	@ResponseBody
	public Map<String, Object> discardSale(XsglDto xsglDto, HttpServletRequest request) {
		return delSale(xsglDto,request);
	}

	/**
	 * 	审核列表
	 * @return
	 */
	@RequestMapping("/sale/pageListAuditSale")
	public ModelAndView pageListAuditSale() {
		ModelAndView mav = new  ModelAndView("storehouse/sale/sale_auditList");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 	销售发票维护
	 * @return
	 */
	@RequestMapping("/sale/salereceiptPage")
	public ModelAndView salereceiptPage(XsglDto xsglDto) {
		ModelAndView mav = new  ModelAndView("storehouse/sale/salereceipt_page");
		XsglDto dtoById = xsglService.getDtoById(xsglDto.getXsid());
		dtoById.setFormAction("salereceiptSavePage");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("xsglDto", dtoById);
		return mav;
	}
	/**
	 * 销售发票维护保存
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/salereceiptSavePage")
	@ResponseBody
	public Map<String, Object> salereceiptSavePage(XsglDto xsglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		xsglDto.setXgry(user.getYhid());
		boolean isSuccess=xsglService.salereceiptSavePage(xsglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 	审核列表
	 * @param xsglDto
	 * @return
	 */
	@RequestMapping("/sale/pageGetListAuditSale")
	@ResponseBody
	public Map<String, Object> getListSaleAudit(XsglDto xsglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 附加委托参数
		DataPermission.addWtParam(xsglDto);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(xsglDto.getDqshzt())) {
			DataPermission.add(xsglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "xsgl", "xsid",
					AuditTypeEnum.AUDIT_SALEORDER);
		} else {
			DataPermission.add(xsglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "xsgl", "xsid",
					AuditTypeEnum.AUDIT_SALEORDER);
		}
		DataPermission.addCurrentUser(xsglDto, getLoginInfo(request));
		List<XsglDto> listMap = xsglService.getPagedAuditSale(xsglDto);
		map.put("total", xsglDto.getTotalNumber());
		map.put("rows", listMap);
		return map;
	}

	/**
	 * 刷新OA销售单号
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/pagedataRefreshDh")
	@ResponseBody
	public Map<String, Object> refreshDh() {
		Map<String, Object> map = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String now=sdf.format(date);
		String[] split = now.split("-");
		String year=split[0];
		String month=split[1];
		String day=split[2];
		String s="XS-"+year+month+day;
		map.put("oaxsdh",  xsglService.getOAxsddh(s));
		return map;
	}

	/**
	 * 删除
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/delSale")
	@ResponseBody
	public Map<String, Object> delSale(XsglDto xsglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		xsglDto.setScry(user.getYhid());
		List<String> ids = xsglDto.getIds();
		if(!CollectionUtils.isEmpty(ids)) {
			XsmxDto xsmxDto=new XsmxDto();
			xsmxDto.setIds(ids);
			List<XsmxDto> list=xsmxService.getDtoList(xsmxDto);
			if(!CollectionUtils.isEmpty(list)){
				for(XsmxDto dto:list){
					if(StringUtil.isNotBlank(dto.getFhmxid())){
						map.put("status", "fail");
						map.put("message", "OA销售单号为  "+dto.getOaxsdh()+"  的明细已发货！");
						return map;
					}
				}
			}
		}
		boolean isSuccess= false;
		try {
			isSuccess = xsglService.delSale(xsglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
		}
		return map;
	}
	/**
	 * 销售信息统计
	 *
	 * @return
	 */
	@RequestMapping(value = "/sale/minidataSaleStatis")
	@ResponseBody
	public List<XsglDto> minidataSaleStatis() {
		return xsglService.getXsxxWithKh();
	}
	/**
	 * 销售信息查看
	 *
	 * @return
	 */
	@RequestMapping(value = "/sale/minidataGetSale")
	@ResponseBody
	public List<XsglDto> minidataGetSale(XsglDto xsglDto) {
		return xsglService.getXsxxByKhid(xsglDto);
	}
	/**
	 * 销售订单导入页面
	 *
	 * @return
	 */
	@RequestMapping(value = "/sale/pageImportSale")
	public ModelAndView importOrder() {
		ModelAndView mav = new ModelAndView("storehouse/sale/saleOrder_import");
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_ORDER.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 借出借用单转销售订单
	 * @return
	 */
	@RequestMapping("/sale/ordertransformPage")
	public ModelAndView ordertransformPage(XsglDto xsglDto){
		ModelAndView modelAndView = new ModelAndView("storehouse/sale/sale_edit");
		modelAndView.addObject("url", "/storehouse/sale/pagedataSaledetails?jcjyid="+xsglDto.getJcjyid());
		xsglDto.setFormAction("ordertransformSavePage");
		JcjyglDto jcjyglDto = jcjyglService.getDtoById(xsglDto.getJcjyid());
		xsglDto.setHtdh(jcjyglDto.getHtbh());
		xsglDto.setHtid(jcjyglDto.getHtid());
		xsglDto.setFzdq(jcjyglDto.getFzdq());
		xsglDto.setYwymc(jcjyglDto.getYwymc());
		xsglDto.setYwy(jcjyglDto.getYwy());
		xsglDto.setXsbmmc(jcjyglDto.getBmmc());
		xsglDto.setXsbm(jcjyglDto.getBm());
		xsglDto.setKhjc(jcjyglDto.getDwid());
		xsglDto.setKhjcmc(jcjyglDto.getKhjc());
		xsglDto.setZdqy(jcjyglDto.getZdqy());
		xsglDto.setZdqymc(jcjyglDto.getZdqymc());
		xsglDto.setKhzd(jcjyglDto.getZd());
		xsglDto.setShlxfs(jcjyglDto.getShlxfs());
		xsglDto.setShdz(jcjyglDto.getShdz());
		xsglDto.setBz(jcjyglDto.getBz());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String now=sdf.format(date);
		xsglDto.setDdrq(now);
		String[] split = now.split("-");
		String year=split[0];
		String month=split[1];
		String day=split[2];
		String s="XS-"+year+month+day;
		String oAxsddh = xsglService.getOAxsddh(s);
		xsglDto.setOaxsdh(oAxsddh);
		xsglDto.setSuil("13");
		modelAndView.addObject("xsglDto", xsglDto);
		modelAndView.addObject("auditType", AuditTypeEnum.AUDIT_SALEORDER.getCode());
		modelAndView.addObject("urlPrefix", urlPrefix);
		List<JcsjDto> xslist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode());
		List<JcsjDto> ywlxlist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.TYPEOF_SERVICE.getCode());
		modelAndView.addObject("xslist", xslist);
		modelAndView.addObject("ywlxlist", ywlxlist);
		modelAndView.addObject("sflist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PROVINCE.getCode()));
		modelAndView.addObject("ysfslist", redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DELIVERY_METHOD.getCode()));
		modelAndView.addObject("cplxlist", JSON.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCT_CLASS.getCode())));
		return modelAndView;
	}
	/**
	 * 借出借用单转销售单保存
	 *
	 * @param xsglDto
	 * @return
	 */
	@RequestMapping("/sale/ordertransformSavePage")
	@ResponseBody
	public Map<String, Object> ordertransformSavePage(XsglDto xsglDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		XsglDto dto = xsglService.getDto(xsglDto);
		if(dto!=null){
			map.put("status", "fail");
			map.put("message", "OA订单号重复！");
			return map;
		}
		User user = getLoginInfo(request);
		xsglDto.setLrry(user.getYhid());
		xsglDto.setZt(StatusEnum.CHECK_NO.getCode());
		xsglDto.setDjlx("2");
		boolean isSuccess = xsglService.insertDto(xsglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid",xsglDto.getXsid());
		map.put("auditType", AuditTypeEnum.AUDIT_SALEORDER.getCode());
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 销售单换货
	 * @return
	 */
	@RequestMapping("/sale/xstransformSale")
	public ModelAndView xstransformSale(XsglDto xsglDto) {
		ModelAndView mav = new ModelAndView("storehouse/sale/sale_edit");
		mav.addObject("url", "/storehouse/sale/pagedataSaledetails");
		XsglDto dtoById = xsglService.getDtoById(xsglDto.getXsid());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String now=sdf.format(date);
		xsglDto.setDdrq(now);
		String[] split = now.split("-");
		String year=split[0];
		String month=split[1];
		String day=split[2];
		String s="XS-"+year+month+day;
		String oAxsddh = xsglService.getOAxsddh(s);
		dtoById.setOaxsdh(oAxsddh);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("auditType", AuditTypeEnum.AUDIT_SALEORDER.getCode());
		dtoById.setFormAction("xstransformSaveSale");
		List<JcsjDto> xslist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode());
		List<JcsjDto> ywlxlist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.TYPEOF_SERVICE.getCode());
		List<JcsjDto> sflist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PROVINCE.getCode());
		List<JcsjDto> ysfslist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.DELIVERY_METHOD.getCode());
		mav.addObject("cplxlist", JSON.toJSONString(redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCT_CLASS.getCode())));
		mav.addObject("xslist", xslist);
		mav.addObject("ywlxlist", ywlxlist);
		mav.addObject("ysfslist", ysfslist);
		mav.addObject("sflist", sflist);
		mav.addObject("xsglDto", dtoById);
		return mav;
	}

	/**
	 * 修改保存
	 *
	 * @param xsglDto
	 * @return
	 */
	@RequestMapping("/sale/xstransformSaveSale")
	@ResponseBody
	public Map<String, Object> xstransformSaveSale(XsglDto xsglDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		XsglDto dto = xsglService.getDto(xsglDto);
		if(dto!=null){
			map.put("status", "fail");
			map.put("message", "OA订单号重复！");
			return map;
		}
		User user = getLoginInfo(request);
		xsglDto.setLrry(user.getYhid());
		xsglDto.setZt(StatusEnum.CHECK_NO.getCode());
		xsglDto.setYxsid(xsglDto.getXsid());
		xsglDto.setJcjyid(null);
		xsglDto.setDjlx("1");
		boolean isSuccess = xsglService.insertDto(xsglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid",xsglDto.getXsid());
		map.put("auditType", AuditTypeEnum.AUDIT_SALEORDER.getCode());
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 	销售明细列表
	 * @return
	 */
	@RequestMapping("/sale/pageListSalesDetails")
	public ModelAndView pageListSalesDetails() {
		ModelAndView mav = new  ModelAndView("storehouse/sale/saleDetails_list");
		List<JcsjDto> xslxlist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.SELLING_TYPE.getCode());
		List<JcsjDto> khlblist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.CUSTOMER_CATEGORY.getCode());
		List<JcsjDto> cplxlist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCT_CLASS.getCode());
		mav.addObject("xslxlist",xslxlist);
		mav.addObject("khlblist",khlblist);
		mav.addObject("cplxlist",cplxlist);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 明细列表数据
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/pageGetListSalesDetails")
	@ResponseBody
	public Map<String, Object> pageGetListSalesDetails(XsmxDto xsmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<XsmxDto> xsmxDtoList=xsmxService.getPagedDtoListDetails(xsmxDto);
		map.put("total",xsmxDto.getTotalNumber());
		map.put("rows",xsmxDtoList);
		return map;
	}

	/**
	 * 	销售明细列表
	 * @return
	 */
	@RequestMapping("/sale/viewSaleDetails")
	public ModelAndView viewSaleDetails(XsmxDto xsmxDto) {
		ModelAndView mav = new  ModelAndView("storehouse/sale/saleDetails_view");
		xsmxDto=xsmxService.getDtoById(xsmxDto.getZjid());
		mav.addObject("xsmxDto",xsmxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 到款按钮
	 */
	@RequestMapping("/sale/paymentreceivedSale")
	public ModelAndView paymentreceivedSale(XsmxDto xsmxDto) {
		ModelAndView mav = new  ModelAndView("storehouse/sale/sale_paymentreceived");
		List<XsmxDto> xsmxDtos = xsmxService.getDkxxByXsid(xsmxDto);
		List<String> xsmxids = xsmxDtos.stream().map(XsmxDto::getXsmxid).distinct().collect(Collectors.toList());
		XsmxDto xsmxDto_fh = new XsmxDto();
		xsmxDto_fh.setIds(xsmxids);
		List<XsmxDto>  xsmxDtos_fh = xsmxService.getDkxxGroup(xsmxDto_fh);
		for (XsmxDto dto : xsmxDtos) {
			List<XsmxDto> list = new ArrayList<>();
			for (XsmxDto xsmxDto_z : xsmxDtos_fh) {
				if (dto.getXsmxid().equals(xsmxDto_z.getXsmxid())){
					list.add(xsmxDto_z);
				}
			}
			dto.setXsmxDtos(list);
		}
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("xsmxDtos", xsmxDtos);
		mav.addObject("xsmxDto", xsmxDto);
		//flag==dkwh显示删除和确定按钮
		mav.addObject("flag", "dkwh");
		return mav;
	}
	/**
	 * 到款按钮
	 */
	@RequestMapping("/sale/pagedataPaymentreceivedSale")
	public ModelAndView pagedataPaymentreceivedSale(XsglDto xsglDto) {
		ModelAndView mav = new  ModelAndView("storehouse/sale/sale_paymentreceived");
		xsglDto=xsglService.getDtoById(xsglDto.getXsid());
		mav.addObject("xsglDto",xsglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 明细列表数据
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/pagedataSalesDetails")
	@ResponseBody
	public Map<String, Object> pagdataSalesDetails(XsmxDto xsmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<XsmxDto> xsmxDtoList=xsmxService.getPagedDtoList(xsmxDto);
		map.put("total",xsmxDto.getTotalNumber());
		map.put("rows",xsmxDtoList);
		return map;
	}
	/**
	 * 到款记录
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/pagedataMoneyHos")
	public ModelAndView pagedataMoneyHos(DkjejlDto dkjejlDto,HttpServletRequest request) {
		ModelAndView mav = new  ModelAndView("storehouse/sale/sale_MoneyHos");
		dkjejlDto.setFormAction("/storehouse/sale/pagedataSaveMoneyHos");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("dkjejlDto", dkjejlDto);
		mav.addObject("flag", request.getParameter("flag"));
		return mav;
	}
	/**
	 * 到款记录数据
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/pagedataGetMoneyHos")
	@ResponseBody
	public Map<String, Object> pagedataGetMoneyHos(DkjejlDto dkjejlDto) {
		Map<String, Object> map = new HashMap<>();
		List<DkjejlDto> dkjejlDtos=dkjejlService.getDtoList(dkjejlDto);
		map.put("rows",dkjejlDtos);
		return map;
	}
	/**
	 * 到款记录保存
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/pagedataSaveMoneyHos")
	@ResponseBody
	public Map<String, Object> pagedataSaveMoneyHos(DkjejlDto dkjejlDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		boolean isSuccess= false;
		try {
			isSuccess = dkjejlService.ArriveMoneySaveMoneyHos(dkjejlDto,user);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 到款记录查看
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/pagedataViewMoneyHos")
	public ModelAndView pagedataViewMoneyHos(DkjejlDto dkjejlDto,HttpServletRequest request) {
		ModelAndView mav = new  ModelAndView("storehouse/sale/MoneyHos_view");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("dkjejlDto", dkjejlDto);
		return mav;
	}
	/**
	 * 负责人设置
	 */
	@RequestMapping("/sale/stewardsetSale")
	public ModelAndView stewardsetSale(XsglDto xsglDto) {
		ModelAndView mav = new  ModelAndView("storehouse/sale/sale_stewardset");
		xsglDto.setFormAction("stewardsetSaveSale");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 负责人设置保存
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/sale/stewardsetSaveSale")
	@ResponseBody
	public Map<String, Object> stewardsetSaveSale(XsglDto xsglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		xsglDto.setXgry(user.getYhid());
		boolean isSuccess=xsglService.stewardsetSaveSale(xsglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	@RequestMapping("/sale/pageGetListPaymentReceived")
	@ResponseBody
	public Map<String, Object> pageGetListPaymentReceived(DkjejlDto dkjejlDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		List<DkjejlDto> dkjejlDtos = dkjejlService.getPagedDtoList(dkjejlDto);
		map.put("total",dkjejlDto.getTotalNumber());
		map.put("rows",dkjejlDtos);
		super.setCzdmList(request,map);
		return map;
	}
	@RequestMapping("/sale/viewPaymentReceived")
	@ResponseBody
	public Map<String, Object> viewPaymentReceived(DkjejlDto dkjejlDto) {
		Map<String, Object> map = new HashMap<>();
		DkjejlDto dtoById = dkjejlService.getDtoById(dkjejlDto.getDkjlid());
		map.put("dkjejlDto",dtoById);
		return map;
	}
	@RequestMapping("/sale/pageGetListNotPaymentReceived")
	@ResponseBody
	public Map<String, Object> pageGetListNotPaymentReceived(DkjejlDto dkjejlDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		List<DkjejlDto> dkjejlDtos = dkjejlService.getPagedNotDtoList(dkjejlDto);
		map.put("total",dkjejlDto.getTotalNumber());
		map.put("rows",dkjejlDtos);
		super.setCzdmList(request,map);
		return map;
	}
	@RequestMapping("/sale/viewNotPaymentReceived")
	@ResponseBody
	public Map<String, Object> viewNotPaymentReceived(DkjejlDto dkjejlDto) {
		Map<String, Object> map = new HashMap<>();
		DkjejlDto dtoById = dkjejlService.getNotPaymentReceivedDto(dkjejlDto);
		map.put("dkjejlDto",dtoById);
		return map;
	}
}
