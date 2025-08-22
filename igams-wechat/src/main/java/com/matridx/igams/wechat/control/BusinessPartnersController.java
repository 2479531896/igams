package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.wechat.dao.entities.*;
import com.matridx.igams.wechat.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/business")
public class BusinessPartnersController extends BaseController {

	@Autowired
	private ISwkhglService swkhglService;
	@Autowired
	private ISwhtglService swhtglService;
	@Autowired
	private ISwhtmxService swhtmxService;
	@Autowired
	private IWbtjglService wbtjglService;
	@Autowired
	private IFjcfbService fjcfbService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private IXxglService xxglService;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private ISwyszkService swyszkService;
	@Autowired
	private ISjkzxxService sjkzxxService;
	@Autowired
	private IYszkmxService yszkmxService;
	@Autowired
	private IFksqService fksqService;
	@Autowired
	IJgxxService jgxxService;
	@Autowired
	IShxxService shxxService;
	@Autowired
	private IKpsqService kpsqService;
	@Autowired
	private ISwskglService swskglService;
	@Autowired
	private IHtyhzcService htyhzcService;


	/**
	 * 选择微信用户表页面
	 * @param
	 * @return
	 */
	@RequestMapping("/customer/pagedataListModel")
	public ModelAndView pagelistUser(SwkhglDto swkhglDto) {
        return new ModelAndView("wechat/sjxx/customer_list");
	}

	/**
	 * 获取商务客户管理list
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/customer/pageGetListInfo")
	@ResponseBody
	public Map<String, Object> pageGetListInfo(SwkhglDto swkhglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		super.setCzdmList(request,map);
		List<SwkhglDto> list = swkhglService.getPagedDtoList(swkhglDto);
		List<JcsjDto> businessList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CUSTOMER_CATEGORY.getCode());
		List<JcsjDto> reconciliationList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RECONCILIATION_TYPE.getCode());
		List<JcsjDto> customerBusinessList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CUSTOMER_BUSINESS_TYPE.getCode());
		List<JcsjDto> invoiceNatureList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INVOICE_CLASS.getCode());
		map.put("businessList", businessList);
		map.put("customerBusinessList", customerBusinessList);
		map.put("invoiceNatureList", invoiceNatureList);
		map.put("reconciliationList", reconciliationList);
		map.put("total", swkhglDto.getTotalNumber());
		map.put("ywlx", BusTypeEnum.IMP_BUSINESS_PARTNERS.getCode());
		map.put("select", ExportTypeEnum.BUSINESS_SELECT.getCode());
		map.put("search", ExportTypeEnum.BUSINESS_SEARCH.getCode());
		map.put("rows", list);
		return map;
	}

	/**
	 * 获取商务客户管理list
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/customer/pagedataListInfo")
	@ResponseBody
	public Map<String, Object> pagedataListInfo(SwkhglDto swkhglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		List<SwkhglDto> list = swkhglService.getPagedDtoList(swkhglDto);
		map.put("rows", list);
		map.put("total", swkhglDto.getTotalNumber());
		return map;
	}

	/**
	 * 获取修改信息
	 * @param
	 * @return
	 */
	@RequestMapping("/customer/modCustomer")
	@ResponseBody
	public Map<String, Object> modCustomer(SwkhglDto swkhglDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(swkhglDto.getKhid())){
			SwkhglDto dtoById = swkhglService.getDtoById(swkhglDto.getKhid());
			map.put("dto", dtoById);
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(swkhglDto.getKhid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_BUSINESS_PARTNERS.getCode());
			List<FjcfbDto> list = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			map.put("list", list);
		}
		return map;
	}

	/**
	 * 获取查看信息
	 * @param
	 * @return
	 */
	@RequestMapping("/customer/viewCustomer")
	@ResponseBody
	public Map<String, Object> viewCustomer(SwkhglDto swkhglDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(swkhglDto.getKhid())){
			map = modCustomer(swkhglDto);
			SwhtglDto swhtglDto = new SwhtglDto();
			swhtglDto.setQddx(swkhglDto.getKhid());
			List<SwhtglDto> htList  = swhtglService.getAllInfo(swhtglDto);
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(SwhtglDto.class, "htid","htfl","htbh","htmc","htfx","qddx","yzlx","xslb","htfs","je","htzfl","htksrq","htjsrq","htyjxx","gzzt","htzt","zt","bz","htflmc","htfxmc","xslbmc","yzlxmc","khmc","htzflmc","htmxList","fjcfbDtos","qdzt","qdztmc","fzr","sflx","sflxmc","lxr","lxdh","sqqy","zdkh");
			SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
			listFilters[0] = filter;
			map.put("htList", JSONObject.toJSONString(htList,listFilters));
		}
		return map;
	}


	/**
	 * 新增保存
	 * @param
	 * @return
	 */
	@RequestMapping("/customer/addSaveCustomer")
	@ResponseBody
	public Map<String, Object> addSaveCustomer(SwkhglDto swkhglDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		if (StringUtil.isBlank(swkhglDto.getGsmc())){
			map.put("status", "fail");
			map.put("message", "公司名称不能为空！");
			return map;
		}
		swkhglDto.setCzbs("1");
		List<SwkhglDto> dtoList = swkhglService.getRepeatDtoList(swkhglDto);
		if (CollectionUtils.isNotEmpty(dtoList)){
			map.put("status", "fail");
			map.put("message", "公司名称已经存在！");
			return map;
		}
		swkhglDto.setKhid(StringUtil.generateUUID());
		swkhglDto.setLrry(user.getYhid());
		boolean success = swkhglService.insert(swkhglDto);
		//文件复制到正式文件夹，插入信息至正式表
		if(swkhglDto.getFjids()!=null && swkhglDto.getFjids().size() > 0){
			for (int i = 0; i < swkhglDto.getFjids().size(); i++) {
				success = fjcfbService.save2RealFile(swkhglDto.getFjids().get(i),swkhglDto.getKhid());
			}
		}
		map.put("status", success ? "success" : "fail");
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 修改保存
	 * @param
	 * @return
	 */
	@RequestMapping("/customer/modSaveCustomer")
	@ResponseBody
	public Map<String, Object> modSaveCustomer(SwkhglDto swkhglDto) {
		Map<String, Object> map = new HashMap<>();
		boolean success = false;
		if (StringUtil.isNotBlank(swkhglDto.getKhid())){
			if (StringUtil.isBlank(swkhglDto.getGsmc())){
				map.put("status", "fail");
				map.put("message", "公司名称不能为空！");
				return map;
			}
			swkhglDto.setCzbs("2");
			List<SwkhglDto> dtoList = swkhglService.getRepeatDtoList(swkhglDto);
			if (CollectionUtils.isNotEmpty(dtoList)){
				map.put("status", "fail");
				map.put("message", "公司名称已经存在！");
				return map;
			}
			User user = getLoginInfo();
			swkhglDto.setXgry(user.getYhid());
			success = swkhglService.update(swkhglDto);
			//文件复制到正式文件夹，插入信息至正式表
			if(swkhglDto.getFjids()!=null && swkhglDto.getFjids().size() > 0){
				for (int i = 0; i < swkhglDto.getFjids().size(); i++) {
					success = fjcfbService.save2RealFile(swkhglDto.getFjids().get(i),swkhglDto.getKhid());
				}
			}
		}
		map.put("status", success ? "success" : "fail");
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 删除操作
	 * @param
	 * @return
	 */
	@RequestMapping("/customer/delCustomer")
	@ResponseBody
	public Map<String, Object> delCustomer(SwkhglDto swkhglDto) {
		Map<String, Object> map = new HashMap<>();
		boolean success = false;
		if (CollectionUtils.isNotEmpty(swkhglDto.getIds())){
			User user = getLoginInfo();
			swkhglDto.setScry(user.getYhid());
			success = swkhglService.delete(swkhglDto);
		}
		map.put("status", success ? "success" : "fail");
		map.put("message", success ? xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 获取商务合同管理list
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/pageGetListContract")
	@ResponseBody
	public Map<String, Object> pageGetListContract(SwhtglDto swhtglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		super.setCzdmList(request,map);
		List<SwhtglDto> list = swhtglService.getPagedDtoList(swhtglDto);
		map.put("total", swhtglDto.getTotalNumber());
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(SwhtglDto.class, "htid","htfl","htbh","htmc","htfx","qddx","yzlx","xslb","htfs","je","htzfl","htksrq","htjsrq","htyjxx","gzzt","htzt","zt","bz","htflmc","htfxmc","xslbmc","yzlxmc","khmc","htzflmc","shxx_dqgwmc","fzr","qdzt","qdztmc","_key","sflx","sflxmc","qdzt","qdztmc","_key","lxr","lxdh","sqqy","sfcl","zdkh");
		SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
		listFilters[0] = filter;
		map.put("rows", JSONObject.toJSONString(list,listFilters));
		List<JcsjDto> riskList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CONTRACT_RISK.getCode());
		List<JcsjDto> classifySubList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CONTRACT_SUB_CLASSIFY.getCode());
		List<JcsjDto> classifyList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CONTRACT_CLASSIFY.getCode());
		List<JcsjDto> chaptersList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CONTRACT_CHAPTERS.getCode());
		List<JcsjDto> saleList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SALE_CATEGORY.getCode());
		List<JcsjDto> detectList = redisUtil.lgetDto("All_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> subList = redisUtil.lgetDto("All_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
		List<JcsjDto> businessList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CUSTOMER_CATEGORY.getCode());
		List<JcsjDto> subjectList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.AFFILIATED_COMPANY.getCode());
		List<JcsjDto> yhflList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DISCOUNT_CLASSIFY.getCode());
		List<JcsjDto> yhzflList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DISCOUNT_SUBCLASSIFY.getCode());
		List<JcsjDto> yhxsList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DISCOUNT_FORM.getCode());
		map.put("businessList", businessList);//商务类型
		map.put("subjectList", subjectList);//签订主体
		map.put("detectList", detectList);//检测项目
		map.put("subList", subList);//检测子项目
		map.put("riskList", riskList);//合同风险
		map.put("classifyList", classifyList);//合同分类
		map.put("chaptersList", chaptersList);//合同用章
		map.put("saleList", saleList);//销售类别
		map.put("classifySubList", classifySubList);//合同子分类
		map.put("yhflList", yhflList);//优惠分类
		map.put("yhzflList", yhzflList);//优惠子分类
		map.put("yhxsList", yhxsList);//优惠形式
		map.put("ywlx", BusTypeEnum.IMP_BUSINESS_CONTRACT.getCode());
		map.put("chapter_ywlx", BusTypeEnum.IMP_CHAPTER_CONTRACT.getCode());
		map.put("shlb", AuditTypeEnum.AUDIT_BUSINESS_CONTRACT.getCode());
		map.put("select", ExportTypeEnum.CONTRACT_SELECT.getCode());
		map.put("search", ExportTypeEnum.CONTRACT_SEARCH.getCode());
		return map;
	}


	/**
	 * 获取修改信息
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/modContract")
	@ResponseBody
	public Map<String, Object> modContract(SwhtglDto swhtglDto) {
        return viewContract(swhtglDto);
	}

	/**
	 * 获取修改信息
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/viewContract")
	@ResponseBody
	public Map<String, Object> viewContract(SwhtglDto swhtglDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(swhtglDto.getHtid()) || StringUtil.isNotBlank(swhtglDto.getHtbh())){
			SwhtglDto dtoById = swhtglService.getDto(swhtglDto);
			if (null == dtoById)
				return map;
			if (StringUtil.isNotBlank(dtoById.getSpr())){
				List<String> strings = new ArrayList<>();
				strings.addAll(Arrays.asList(dtoById.getSpr().split(",")));
				if (CollectionUtils.isNotEmpty(strings)){
					User user = new User();
					user.setIds(strings);
					List<User> list = commonService.getListByIds(user);
					List<User> newList=new ArrayList<>();
					for(String id:strings){
						for(User _user:list){
							if(id.equals(_user.getYhid())){
								newList.add(_user);
							}
						}
					}
					map.put("userList", newList);
				}
			}
			SwhtmxDto swhtmxDto = new SwhtmxDto();
			swhtmxDto.setHtid(dtoById.getHtid());
			List<SwhtmxDto> dtoList = swhtmxService.getDtoList(swhtmxDto);
			map.put("dto", dtoById);
			map.put("dtoList", dtoList);
			HtyhzcDto htyhzcDto=new HtyhzcDto();
			htyhzcDto.setHtid(dtoById.getHtid());
			List<HtyhzcDto> yhzcdtoList = htyhzcService.getDtoList(htyhzcDto);
			map.put("yhzcList", yhzcdtoList);
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(dtoById.getHtid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_BUSINESS_CONTRACT.getCode());
			List<FjcfbDto> list = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			FjcfbDto fjcfbDto1 = new FjcfbDto();
			fjcfbDto1.setYwid(dtoById.getHtid());
			fjcfbDto1.setYwlx(BusTypeEnum.IMP_CHAPTER_CONTRACT.getCode());
			List<FjcfbDto> szlist = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto1);
			map.put("list", list);
			map.put("szlist", szlist);
			SwhtglDto swhtglDto_t = new SwhtglDto();
			swhtglDto_t.setHtid(dtoById.getHtid());
			List<SwhtglDto> swhtglDtos = swhtglService.getKhhtglInfo(swhtglDto_t);
			map.put("swhtglDtos", swhtglDtos);
		}
		return map;
	}

	/**
	 * 项目修改
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/modprojectSaveContract")
	@ResponseBody
	public Map<String, Object> modprojectSaveContract(SwhtmxDto swhtmxDto) {
		Map<String, Object> map = new HashMap<>();
		Boolean success = false;
		if (StringUtil.isNotBlank(swhtmxDto.getJson()) && !CollectionUtils.isEmpty(swhtmxDto.getIds())){
			List<SwhtmxDto> list=(List<SwhtmxDto>) JSON.parseArray(swhtmxDto.getJson(),SwhtmxDto.class);
			var user = getLoginInfo();
			for (var dto : list) {
				if (StringUtil.isBlank(dto.getJcxmid())){
					map.put("status", "fail");
					map.put("message", "数据存在错误！");
					return map;
				}
				dto.setXgry(user.getYhid());
			}
			swhtmxDto.setDtos(list);
			success = swhtmxService.modProjectInfo(swhtmxDto);
		}
		map.put("status", success ? "success" : "fail");
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 获取合同信息
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/pagedataContractInfo")
	@ResponseBody
	public Map<String, Object> pagedataContractInfo(SwhtglDto swhtglDto) {
		return viewContract(swhtglDto);
	}

	/**
	 * 根据类型获取合同编码
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/pagedataContractNumber")
	@ResponseBody
	public Map<String, Object> pagedataContractNumber(SwhtglDto swhtglDto) {
		Map<String, Object> map = new HashMap<>();
		String contractNumber = "";
		if (StringUtil.isNotBlank(swhtglDto.getHtzflcskz1())){
			contractNumber = swhtglService.getContractNumber(swhtglDto.getHtzflcskz1());
		}
		map.put("contractNumber", contractNumber);
		return map;
	}

	/**
	 * 新增保存商务合同信息
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/addSaveContract")
	@ResponseBody
	public Map<String, Object> addSaveContract(SwhtglDto swhtglDto) {
		Map<String, Object> map = new HashMap<>();
		boolean success;
		try {
			if (StringUtil.isBlank(swhtglDto.getHtbh())){
				map.put("status", "fail");
				map.put("message", "合同编号不能为空！");
				return map;
			}
			swhtglDto.setCzbs("1");
			List<SwhtglDto> dtoList = swhtglService.getRepeatDtoList(swhtglDto);
			if (CollectionUtils.isNotEmpty(dtoList)){
				map.put("status", "fail");
				map.put("message", "合同编号已经存在！");
				return map;
			}
			User user = getLoginInfo();
			swhtglDto.setLrry(user.getYhid());
			swhtglDto.setHtid(StringUtil.generateUUID());
			swhtglDto.setZt(StatusEnum.CHECK_NO.getCode());
			swhtglDto.setHtzt("00");
			swhtglDto.setGzzt("0");
			swhtglDto.setSflx("0");
			List<JcsjDto> classifyList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CONTRACT_CLASSIFY.getCode());
			for (JcsjDto dto : classifyList) {
				if (dto.getCsid().equals(swhtglDto.getHtfl()) && "PERSONAL_FILING".equals(dto.getCsdm()))
					swhtglDto.setHtzt("10");
			}
			success = swhtglService.insertInfo(swhtglDto);
		} catch (BusinessException e) {
			map.put("status","fail");
			map.put("message", e.getMsg());
			return map;
		}
		map.put("status", success ? "success" : "fail");
		map.put("ywid", swhtglDto.getHtid());
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 修改保存商务合同信息
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/modSaveContract")
	@ResponseBody
	public Map<String, Object> modSaveContract(SwhtglDto swhtglDto) {
		Map<String, Object> map = new HashMap<>();
		boolean success = false;
		User user = getLoginInfo();
		swhtglDto.setXgry(user.getYhid());
		if (StringUtil.isNotBlank(swhtglDto.getHtid())){
			try {
				if (StringUtil.isBlank(swhtglDto.getHtbh())){
					map.put("status", "fail");
					map.put("message", "合同编号不能为空！");
					return map;
				}
				swhtglDto.setCzbs("2");
				List<SwhtglDto> dtoList = swhtglService.getRepeatDtoList(swhtglDto);
				if (CollectionUtils.isNotEmpty(dtoList)){
					map.put("status", "fail");
					map.put("message", "合同编号已经存在！");
					return map;
				}
				success = swhtglService.updateInfo(swhtglDto);
			} catch (BusinessException e) {
				map.put("status","fail");
				map.put("message", e.getMsg());
				return map;
			}
		}
		map.put("status", success ? "success" : "fail");
		map.put("ywid", swhtglDto.getHtid());
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}


	/**
	 * 删除商务合同信息
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/delContract")
	@ResponseBody
	public Map<String, Object> delContract(SwhtglDto swhtglDto) {
		Map<String, Object> map = new HashMap<>();
		boolean success = false;
		User user = getLoginInfo();
		swhtglDto.setScry(user.getYhid());
		if (CollectionUtils.isNotEmpty(swhtglDto.getIds())){
			try {
				success = swhtglService.delInfo(swhtglDto);
			} catch (BusinessException e) {
				map.put("status","fail");
				map.put("message", e.getMsg());
				return map;
			}
		}
		map.put("status", success ? "success" : "fail");
		map.put("message", success ? xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 审批通过商务合同信息
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/approvedContract")
	@ResponseBody
	public Map<String, Object> approvedContract(SwhtglDto swhtglDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		swhtglDto.setXgry(user.getYhid());
		swhtglDto.setZt(StatusEnum.CHECK_PASS.getCode());
		boolean success = swhtglService.updateZt(swhtglDto);
		map.put("status", success ? "success" : "fail");
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 更新收费标准按钮
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/modsfbzContract")
	@ResponseBody
	public Map<String, Object> modsfbzContract(SwhtglDto swhtglDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		swhtglDto.setXgry(user.getYhid());
		swhtglDto.setZt(StatusEnum.CHECK_PASS.getCode());
		boolean success = swhtglService.updateSfbz(swhtglDto);
		map.put("status", success ? "success" : "fail");
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 获取商务合同审核管理list
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/pageGetListContractAudit")
	@ResponseBody
	public Map<String, Object> pageGetListContractAudit(SwhtglDto swhtglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		super.setCzdmList(request,map);
		//附加委托参数
		DataPermission.addWtParam(swhtglDto);
		//附加审核状态过滤
		List<AuditTypeEnum> auditTypes = new ArrayList<>();
		auditTypes.add(AuditTypeEnum.AUDIT_BUSINESS_CONTRACT);
		if(GlobalString.AUDIT_SHZT_YSH.equals(swhtglDto.getDqshzt())){
			DataPermission.add(swhtglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "swhtgl", "htid",auditTypes);
		}else{
			DataPermission.add(swhtglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "swhtgl", "htid",auditTypes);
		}
		List<SwhtglDto> list = swhtglService.getPagedAuditList(swhtglDto);
		map.put("total", swhtglDto.getTotalNumber());
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(SwhtglDto.class, "fzr","htid","htfl","qdzt","qdztmc","htbh","htmc","htfx","qddx","yzlx","xslb","htfs","je","htzfl","htksrq","htjsrq","htyjxx","gzzt","htzt","zt","bz","htflmc","htfxmc","xslbmc","yzlxmc","khmc","htzflmc","shxx_dqgwmc","sflx","sflxmc","fzr","qdzt","qdztmc","lxr","lxdh","sqqy","zdkh");
		SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
		listFilters[0] = filter;
		map.put("rows", JSONObject.toJSONString(list,listFilters));

		List<JcsjDto> riskList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CONTRACT_RISK.getCode());
		List<JcsjDto> classifySubList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CONTRACT_SUB_CLASSIFY.getCode());
		List<JcsjDto> classifyList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CONTRACT_CLASSIFY.getCode());
		List<JcsjDto> chaptersList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CONTRACT_CHAPTERS.getCode());
		List<JcsjDto> saleList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SALE_CATEGORY.getCode());
		List<JcsjDto> detectList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> subList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
		List<JcsjDto> businessList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CUSTOMER_CATEGORY.getCode());
		List<JcsjDto> subjectList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.AFFILIATED_COMPANY.getCode());
		map.put("subjectList", subjectList);//签订主体
		map.put("businessList", businessList);//商务类型
		map.put("detectList", detectList);
		map.put("subList", subList);
		map.put("riskList", riskList);
		map.put("classifyList", classifyList);
		map.put("chaptersList", chaptersList);
		map.put("saleList", saleList);
		map.put("classifySubList", classifySubList);
		map.put("ywlx", BusTypeEnum.IMP_BUSINESS_CONTRACT.getCode());
		map.put("shlb", AuditTypeEnum.AUDIT_BUSINESS_CONTRACT.getCode());
		return map;
	}

	/**
	 * 双章合同保存
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/formalSaveContract")
	@ResponseBody
	public Map<String, Object> formalSaveContract(SwhtglDto swhtglDto) {
		Map<String, Object> map = new HashMap<>();
		boolean success = false;
		User user = getLoginInfo();
		swhtglDto.setXgry(user.getYhid());
		if (CollectionUtils.isNotEmpty(swhtglDto.getChapterFjids()) && StringUtil.isNotBlank(swhtglDto.getHtid())){
			try {
				success = swhtglService.formalSaveContract(swhtglDto);
			} catch (BusinessException e) {
				map.put("status","fail");
				map.put("message", e.getMsg());
				return map;
			}
		}
		map.put("status", success ? "success" : "fail");
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 终止合同
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/discontinuecontractSaveContract")
	@ResponseBody
	public Map<String, Object> discontinuecontractSaveContract(SwhtglDto swhtglDto) {
		Map<String, Object> map = new HashMap<>();
		boolean success = false;
		User user = getLoginInfo();
		swhtglDto.setXgry(user.getYhid());
		if (StringUtil.isNotBlank(swhtglDto.getHtid())){
			try {
				success = swhtglService.discontinuecontractSave(swhtglDto);
			} catch (BusinessException e) {
				map.put("status","fail");
				map.put("message", e.getMsg());
				return map;
			}
		}
		map.put("status", success ? "success" : "fail");
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}


	/**
	 * 补充合同
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/replenishcontractContract")
	@ResponseBody
	public Map<String, Object> replenishcontractContract() {
		Map<String, Object> map = new HashMap<>();
		List<JcsjDto> riskList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CONTRACT_RISK.getCode());
		List<JcsjDto> classifySubList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CONTRACT_SUB_CLASSIFY.getCode());
		List<JcsjDto> classifyList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CONTRACT_CLASSIFY.getCode());
		List<JcsjDto> chaptersList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CONTRACT_CHAPTERS.getCode());
		List<JcsjDto> saleList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.SALE_CATEGORY.getCode());
		List<JcsjDto> detectList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> subList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
		List<JcsjDto> businessList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CUSTOMER_CATEGORY.getCode());
		List<JcsjDto> subjectList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.AFFILIATED_COMPANY.getCode());
		map.put("subjectList", subjectList);//签订主体
		map.put("businessList", businessList);//商务类型
		map.put("detectList", detectList);//检测项目
		map.put("subList", subList);//检测子项目
		map.put("riskList", riskList);//合同风险
		map.put("classifyList", classifyList);//合同分类
		map.put("chaptersList", chaptersList);//合同用章
		map.put("saleList", saleList);//销售类别
		map.put("classifySubList", classifySubList);//合同类型
		map.put("chapter_ywlx", BusTypeEnum.IMP_CHAPTER_CONTRACT.getCode());
		return map;
	}

	/**
	 * 补充合同
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/replenishcontractSaveContract")
	@ResponseBody
	public Map<String, Object> replenishcontractSaveContract(SwhtglDto swhtglDto) {
		Map<String, Object> map = new HashMap<>();
		boolean success;
		try {
			if (StringUtil.isNotBlank(swhtglDto.getHtid())){
					success = swhtglService.replenishcontractSaveContract(swhtglDto);
			}else{
				List<JcsjDto> jcsjDtoList=redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.CONTRACT_CLASSIFY.getCode());
				boolean flag=false;
				for(JcsjDto jcsjDto:jcsjDtoList){
					if(swhtglDto.getHtfl().equals(jcsjDto.getCsid())&&jcsjDto.getCsdm().equals("BUSINESS_CONTRACT")){
						flag=true;
						break;
					}
				}
				if (CollectionUtils.isEmpty(swhtglDto.getChapterFjids())&&flag){
					map.put("status","fail");
					map.put("message", "双章附件不存在！");
					return map;
				}
				User user = getLoginInfo();
				swhtglDto.setLrry(user.getYhid());
				swhtglDto.setHtid(StringUtil.generateUUID());
				swhtglDto.setZt(StatusEnum.CHECK_PASS.getCode());
				if (StringUtil.isNotBlank(swhtglDto.getHtjsrq())){
					String htzt = swhtglService.getHtzt(swhtglDto);
					if("30".equals(htzt)){
						swhtglDto.setSfcl("2");
					}
					swhtglDto.setSflx("0");//暂时默认都设置为0，只通过定时任务来进行履行
					swhtglDto.setHtzt(htzt);
				}else{
					map.put("status","fail");
					map.put("message", "合同结束日期不存在！");
					return map;
				}
				swhtglDto.setGzzt("1");
				success = swhtglService.insertInfo(swhtglDto);
			}
		} catch (BusinessException e) {
			map.put("status","fail");
			map.put("message", e.getMsg());
			return map;
		}
		map.put("status", success ? "success" : "fail");
		map.put("ywid", swhtglDto.getHtid());
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 获取商务应收账款表上传按钮
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/credit/uploadSaveCredit")
	@ResponseBody
	public Map<String, Object> uploadSaveCredit(SwyszkDto swyszkDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		if (CollectionUtils.isNotEmpty(swyszkDto.getFjids())){
			try {
				User user = getLoginInfo(request);
				swyszkDto.setLrry(user.getYhid());
				String message = swyszkService.uploadSaveFile(swyszkDto);
				if (!"success".equals(message) && !"fail".equals(message)){
					map.put("status", "confirm");
					map.put("message", message);
					return map;
				}
				if ("fail".equals(message)){
					map.put("status", "fail");
					map.put("message", "未获取到附件！");
					return map;
				}
			} catch (BusinessException e) {
				map.put("status", "fail");
				map.put("message", e.getMsg());
				return map;
			} catch (IOException e) {
				map.put("status", "fail");
				map.put("message", e.toString());
				return map;
			}
			map.put("status", "success");
			map.put("message", "保存成功！");
		}else {
			map.put("status", "fail");
			map.put("message", "未获取到附件！");
		}
		return map;
	}

	/**
	 * 开票申请保存
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/credit/billingSaveCredit")
	@ResponseBody
	public Map<String, Object> billingSaveCredit(KpsqDto kpsqDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		Boolean success = false;
		if (StringUtil.isNotBlank(kpsqDto.getYwid())){
			User user = getLoginInfo(request);
			kpsqDto.setLrry(user.getYhid());
			kpsqDto.setWbcxid(user.getWbcxid());
			kpsqDto.setFpsqid(StringUtil.generateUUID());
			kpsqDto.setYwlx(CommonCreditEnum.SW_YSZK_KP.getCode());
			kpsqDto.setZt(StatusEnum.CHECK_NO.getCode());
			success = kpsqService.insertInfo(kpsqDto);
		}
		map.put("status", success ? "success" : "fail");
		map.put("ywid", kpsqDto.getFpsqid());
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 开票申请保存（无需提交）
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/credit/pagedataSaveBilling")
	@ResponseBody
	public Map<String, Object> pagedataSaveBilling(KpsqDto kpsqDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		boolean success = false;
		if (StringUtil.isNotBlank(kpsqDto.getYwid())){
			User user = getLoginInfo(request);
			kpsqDto.setLrry(user.getYhid());
			kpsqDto.setWbcxid(user.getWbcxid());
			kpsqDto.setFpsqid(StringUtil.generateUUID());
			kpsqDto.setYwlx(CommonCreditEnum.SW_YSZK_KP.getCode());
			kpsqDto.setZt(StatusEnum.CHECK_PASS.getCode());
			kpsqService.insertInfo(kpsqDto);
			SwyszkDto swyszkDto = kpsqService.getAllInfo(kpsqDto);
			if (null != swyszkDto){
				swyszkDto.setXgry(user.getYhid());
				swyszkDto.setYszkid(kpsqDto.getYwid());
				swyszkService.update(swyszkDto);
			}
			SwyszkDto swyszkDto_t=new SwyszkDto();
			swyszkDto_t.setYfpslbj("1");
			swyszkDto_t.setYszkid(kpsqDto.getYwid());
			swyszkDto_t.setXgry(user.getYhid());
			success = swyszkService.updateAmount(swyszkDto_t);
		}
		map.put("status", success ? "success" : "fail");
		map.put("ywid", kpsqDto.getFpsqid());
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 获取商务应收账款表查询条件
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/credit/pageListReceivableCredit")
	@ResponseBody
	public Map<String, Object> pageListReceivableCredit() {
		Map<String, Object> map = new HashMap<>();
		List<JcsjDto> khxzlist = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CLASSIFY.getCode());
		List<JcsjDto> classfyList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CLASSIFY.getCode());
		map.put("khxzlist", khxzlist);
		map.put("classfyList", classfyList);
		return map;
	}

	/**
	 * 获取商务应收账款表
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/credit/pageGetListReceivableCredit")
	@ResponseBody
	public Map<String, Object> pageGetListReceivableCredit(SwyszkDto swyszkDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		super.setCzdmList(request,map);
		Object xtsz=redisUtil.hget("matridx_xtsz","business.receipts.judge.time");
		JSONObject job=JSONObject.parseObject(String.valueOf(xtsz));
		swyszkDto.setSzz(String.valueOf(job.getString("szz")));
		List<SwyszkDto> list = swyszkService.getPagedDtoList(swyszkDto);

		List<JcsjDto> invoiceObjectList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INVOICE_OBJECT.getCode());
		List<JcsjDto> invoiceTypeList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INVOICE_TYPE.getCode());
		List<JcsjDto> invoiceClassList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INVOICE_CLASS.getCode());
		List<JcsjDto> invoiceBodyList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INVOICE_BODY.getCode());
		List<JcsjDto> khxzlist = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CLASSIFY.getCode());
		List<JcsjDto> classfyList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.CLASSIFY.getCode());
		map.put("khxzlist", khxzlist);
		map.put("classfyList", classfyList);
		map.put("invoiceObjectList", invoiceObjectList);
		map.put("invoiceTypeList", invoiceTypeList);
		map.put("invoiceClassList", invoiceClassList);
		map.put("invoiceBodyList", invoiceBodyList);
		map.put("ywlx", BusTypeEnum.IMP_UPLOAD_CREDIT.getCode());
		map.put("kpywlx", BusTypeEnum.IMP_INVOICE_CREDIT.getCode());
		map.put("auditType", AuditTypeEnum.AUDIT_BUSINESS_INVOICE_APPLICATION.getCode());
		map.put("shlb", AuditTypeEnum.AUDIT_BUSINESS_INVOICE_APPLICATION.getCode());
		map.put("total", swyszkDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 查看商务应收账款表
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/credit/viewReceivableCredit")
	@ResponseBody
	public Map<String, Object> viewReceivableCredit(SwyszkDto swyszkDto) {
		Map<String, Object> map = new HashMap<>();
		SwyszkDto swyszkDto_t= swyszkService.getDto(swyszkDto);
		Map<String,Object> params = new HashMap<>();
		params.put("yszkid",swyszkDto.getYszkid());
		List<Map<String, Object>> dtoListOptimize = yszkmxService.getDtoListOptimize(params);
		if(dtoListOptimize==null||dtoListOptimize.size()==0){
			dtoListOptimize = sjkzxxService.getDtoListOptimize(params);
		}
		map.put("swyszkDto", swyszkDto_t);
		map.put("mxlist", dtoListOptimize);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(swyszkDto.getYszkid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_UPLOAD_CREDIT.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
		map.put("fjcfbDtos", fjcfbDtos);
		FksqDto fksqDto=new FksqDto();
		fksqDto.setYwid(swyszkDto.getYszkid());
		fksqDto.setYwlx(CommonCreditEnum.SW_YSZK_FK.getCode());
		List<FksqDto> fksqDtos = fksqService.getDtoList(fksqDto);
		if(fksqDtos!=null&&fksqDtos.size()>0){
			List<String> ywids =new ArrayList<>();
			for(FksqDto dto:fksqDtos){
				ywids.add(dto.getFksqid());
			}
			if(ywids!=null&&ywids.size()>0){
				ShxxDto shxxDto=new ShxxDto();
				shxxDto.setYwids(ywids);
				List<ShxxDto> shxxDtos = shxxService.getShxxGroupByLcxh(shxxDto);
				if(shxxDtos!=null&&shxxDtos.size()>0){
					for(FksqDto dto:fksqDtos){
						List<ShxxDto> list=new ArrayList<>();
						for(ShxxDto shxxDto_t:shxxDtos){
							if(shxxDto_t.getYwid().equals(dto.getFksqid())){
								list.add(shxxDto_t);
							}
						}
						dto.setShxxDtos(list);
					}
				}
			}
		}
		map.put("fksqDtos", fksqDtos);
		KpsqDto kpsqDto=new KpsqDto();
		kpsqDto.setYwid(swyszkDto.getYszkid());
		kpsqDto.setYwlx(CommonCreditEnum.SW_YSZK_KP.getCode());
		List<KpsqDto> kpsqDtos = kpsqService.getDtoList(kpsqDto);
		if(kpsqDtos!=null&&kpsqDtos.size()>0){
			List<String> ywids =new ArrayList<>();
			for(KpsqDto dto:kpsqDtos){
				ywids.add(dto.getFpsqid());
			}
			if(ywids!=null&&ywids.size()>0){
				ShxxDto shxxDto=new ShxxDto();
				shxxDto.setYwids(ywids);
				List<ShxxDto> shxxDtos = shxxService.getShxxGroupByLcxh(shxxDto);
				if(shxxDtos!=null&&shxxDtos.size()>0){
					for(KpsqDto dto:kpsqDtos){
						List<ShxxDto> list=new ArrayList<>();
						for(ShxxDto shxxDto_t:shxxDtos){
							if(shxxDto_t.getYwid().equals(dto.getFpsqid())){
								list.add(shxxDto_t);
							}
						}
						dto.setShxxDtos(list);
					}
				}
			}
		}
		map.put("kpsqDtos", kpsqDtos);
		SwskglDto swskglDto=new SwskglDto();
		swskglDto.setYszkid(swyszkDto.getYszkid());
		List<SwskglDto> dtoList = swskglService.getDtoList(swskglDto);
		map.put("swskglDtos", dtoList);
		return map;
	}

	/**
	 * 更新开票号码
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/credit/pagedataRenewInvoice")
	@ResponseBody
	public Map<String, Object> pagedataRenewInvoice(KpsqDto kpsqDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(kpsqDto.getFpsqid()) && StringUtil.isNotBlank(kpsqDto.getFphm()) && StringUtil.isNotBlank(kpsqDto.getYwid())){
			try {
				User user = getLoginInfo(request);
				kpsqDto.setXgry(user.getYhid());
				kpsqService.updateFphm(kpsqDto);
			} catch (BusinessException e) {
				map.put("status", "fail");
				map.put("message", e.getMsg());
				return map;
			}
			map.put("status", "success");
			map.put("message", "保存成功！");
		}else {
			map.put("status", "fail");
			map.put("message", "发票信息缺失！");
		}
		return map;
	}


	/**
	 * 收款维护
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/credit/collectionReceivableCredit")
	@ResponseBody
	public Map<String, Object> collectionReceivableCredit(SwyszkDto swyszkDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		swyszkDto.setXgry(user.getYhid());
		boolean isSuccess = swyszkService.collectionReceivableCredit(swyszkDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 付款维护
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/credit/paymentReceivableCredit")
	@ResponseBody
	public Map<String, Object> paymentReceivableCredit() {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		map.put("fygslist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAY_BELONG.getCode()));// 费用归属
		map.put("fkflist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYER.getCode()));//付款方
		map.put("fkfslist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYWAY.getCode()));//付款方式
		JgxxDto jgxx = new JgxxDto();
		jgxx.setJgid(user.getJgid());
		jgxx = jgxxService.selectJgxxByJgid(jgxx);
		String jgmc="";
		if (jgxx!=null){
			jgmc=jgxx.getJgmc();
		}
		map.put("sqbmmc",jgmc);
		map.put("sqbm",user.getJgid());
		return map;
	}

	/**
	 * 应收账款删除
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/credit/delCredit")
	@ResponseBody
	public Map<String, Object> delCredit(SwyszkDto swyszkDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		swyszkDto.setScry(user.getYhid());
		boolean success = false;
		if (CollectionUtils.isNotEmpty(swyszkDto.getIds())){
			try {
				success = swyszkService.delInfo(swyszkDto);
			} catch (BusinessException e) {
				map.put("status","fail");
				map.put("message", e.getMsg());
				return map;
			}
		}
		map.put("status", success ? "success" : "fail");
		map.put("message", success ? xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}


	/**
	 * 付款维护保存
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/credit/paymentSaveReceivableCredit")
	@ResponseBody
	public Map<String, Object> paymentSaveReceivableCredit(FksqDto fksqDto) {
		Map<String, Object> map = new HashMap<>();
		if(StringUtil.isBlank(fksqDto.getBcbj())){
			FksqDto fksqDto_t=new FksqDto();
			fksqDto_t.setYwid(fksqDto.getYwid());
			List<FksqDto> dtoList = fksqService.getDtoList(fksqDto_t);
			if(dtoList!=null&&!dtoList.isEmpty()){
				int num=0;
				for(FksqDto dto:dtoList){
					if(StatusEnum.CHECK_SUBMIT.getCode().equals(dto.getZt())){
						map.put("status", "fail");
						map.put("message", "当前应收款有正在审核中的付款流程,不允许发起申请!");
						return map;
					}else if(StatusEnum.CHECK_PASS.getCode().equals(dto.getZt())){
						num++;
					}
				}
				if(num>0){
					map.put("status", "caution");
					map.put("message", "当前应收款当前已成功发起"+ num +"次付款申请,是否继续申请付款?");
					return map;
				}
			}
		}
		User user = getLoginInfo();
		fksqDto.setLrry(user.getYhid());
		fksqDto.setSqr(user.getYhid());
		fksqDto.setSqbm(user.getJgid());
		fksqDto.setWbcxid(user.getWbcxid());
		Boolean isSuccess = fksqService.paymentSaveReceivableCredit(fksqDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid",fksqDto.getFksqid());
		map.put("auditType", AuditTypeEnum.AUDIT_BUSINESS_PAYMENT_APPLICATION.getCode());
		return map;
	}

	/**
	 * 结清
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/credit/settleReceivableCredit")
	@ResponseBody
	public Map<String, Object> settleReceivableCredit(SwyszkDto swyszkDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		swyszkDto.setXgry(user.getYhid());
		swyszkDto.setSfjq("1");
		boolean isSuccess = swyszkService.updateDto(swyszkDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

    /**
     * 结算
     * @param swyszkDto
     * @return
     */
    @RequestMapping("/credit/settlementReceivableCredit")
    @ResponseBody
    public Map<String, Object> settlementReceivableCredit(SwyszkDto swyszkDto) {
        Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo();
        swyszkDto.setXgry(user.getYhid());
        swyszkDto.setSfjs("1");
        Boolean isSuccess = swyszkService.updateSfjs(swyszkDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
        return map;
    }
	/**
	 * 扩展修改修改销售负责人
	 * @param swyszkDto
	 * @return
	 */
	@RequestMapping("/credit/pagedataUpdateXsfzr")
	@ResponseBody
	public Map<String, Object> pagedataUpdateXsfzr(SwyszkDto swyszkDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		swyszkDto.setXgry(user.getYhid());
		Boolean isSuccess =  swyszkService.updateAmount(swyszkDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}


	/**
	 * 扩展修改
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/credit/extendReceivableCredit")
	@ResponseBody
	public Map<String, Object> extendReceivableCredit(SwyszkDto swyszkDto) {
		Map<String, Object> map = new HashMap<>();
		SwyszkDto swyszkDto_t= swyszkService.getDto(swyszkDto);
		map.put("swyszkDto", swyszkDto_t);
		FksqDto fksqDto=new FksqDto();
		fksqDto.setYwid(swyszkDto.getYszkid());
		fksqDto.setYwlx(CommonCreditEnum.SW_YSZK_FK.getCode());
		List<FksqDto> fksqDtos = fksqService.getDtoList(fksqDto);
		map.put("fksqDtos", fksqDtos);
		map.put("fk_AuditType", AuditTypeEnum.AUDIT_BUSINESS_PAYMENT_APPLICATION.getCode());
		KpsqDto kpsqDto=new KpsqDto();
		kpsqDto.setYwid(swyszkDto.getYszkid());
		kpsqDto.setYwlx(CommonCreditEnum.SW_YSZK_KP.getCode());
		List<KpsqDto> kpsqDtos = kpsqService.getDtoList(kpsqDto);
		map.put("kpsqDtos", kpsqDtos);
		map.put("kp_AuditType", AuditTypeEnum.AUDIT_BUSINESS_INVOICE_APPLICATION.getCode());
		return map;
	}

	/**
	 * 	审核列表
	 * @param fksqDto
	 * @return
	 */
	@RequestMapping("/application/pageGetListAuditPaymentApplication")
	@ResponseBody
	public Map<String, Object> pageGetListAuditPaymentApplication(FksqDto fksqDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		super.setCzdmList(request,map);
		// 附加委托参数
		DataPermission.addWtParam(fksqDto);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(fksqDto.getDqshzt())) {
			DataPermission.add(fksqDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "fksq", "fksqid",
					AuditTypeEnum.AUDIT_BUSINESS_PAYMENT_APPLICATION);
		} else {
			DataPermission.add(fksqDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "fksq", "fksqid",
					AuditTypeEnum.AUDIT_BUSINESS_PAYMENT_APPLICATION);
		}
		DataPermission.addCurrentUser(fksqDto, getLoginInfo(request));
		List<FksqDto> listMap = fksqService.pageGetListAuditPaymentApplication(fksqDto);
		map.put("total", fksqDto.getTotalNumber());
		map.put("rows", listMap);
		map.put("auditType", AuditTypeEnum.AUDIT_BUSINESS_PAYMENT_APPLICATION.getCode());
		return map;
	}

	/**
	 * 获取机构信息列表
	 * @param jgxxDto
	 * @return
	 */
	@RequestMapping(value="/application/pagedataGetDepartments")
	@ResponseBody
	public Map<String,Object> pagedataGetDepartments(JgxxDto jgxxDto){
		List<JgxxDto> jglist=jgxxService.getDtoList(jgxxDto);
		Map<String, Object> map= new HashMap<>();
		map.put("rows",jglist);
		return map;
	}

	/**
	 * 查看
	 * @param fksqDto
	 * @return
	 */
	@RequestMapping(value="/application/viewPaymentApplication")
	@ResponseBody
	public Map<String,Object> viewPaymentApplication(FksqDto fksqDto){
		Map<String, Object> map= new HashMap<>();
		FksqDto dto = fksqService.getDto(fksqDto);
		map.put("fksqDto",dto);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(fksqDto.getFksqid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_BUSINESS_PAYMENT_APPLICATION_FILE.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		map.put("fjcfbDtos",fjcfbDtos);
		return map;
	}

	/**
	 * 审核
	 * @param fksqDto
	 * @return
	 */
	@RequestMapping(value="/application/auditPaymentApplication")
	@ResponseBody
	public Map<String,Object> auditPaymentApplication(FksqDto fksqDto){
		return this.viewPaymentApplication(fksqDto);
	}

	/**
	 * 获取商务付款申请列表
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/application/pageListPaymentApplication")
	@ResponseBody
	public Map<String, Object> pageListPaymentApplication() {
		Map<String, Object> map = new HashMap<>();
		map.put("fygslist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAY_BELONG.getCode()));// 费用归属
		map.put("fkflist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYER.getCode()));//付款方
		map.put("fkfslist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYWAY.getCode()));//付款方式
		map.put("auditType", AuditTypeEnum.AUDIT_BUSINESS_PAYMENT_APPLICATION.getCode());
		return map;
	}

	/**
	 * 获取商务付款申请列表
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/application/pageGetListPaymentApplication")
	@ResponseBody
	public Map<String, Object> pageGetListPaymentApplication(FksqDto fksqDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		super.setCzdmList(request,map);
		List<FksqDto> list = fksqService.getPagedDtoList(fksqDto);
		map.put("total", fksqDto.getTotalNumber());
		map.put("rows", list);
		map.put("shlb", AuditTypeEnum.AUDIT_BUSINESS_PAYMENT_APPLICATION.getCode());
		return map;
	}

	/**
	 * 删除
	 * @param fksqDto
	 * @return
	 */
	@RequestMapping(value="/application/delPaymentApplication")
	@ResponseBody
	public Map<String,Object> delPaymentApplication(FksqDto fksqDto,HttpServletRequest request){
		Map<String, Object> map= new HashMap<>();
		User user = getLoginInfo(request);
		fksqDto.setScry(user.getYhid());
		boolean isSuccess = fksqService.deleteDto(fksqDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 获取外部统计列表
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/exterior/pageGetListStatistics")
	@ResponseBody
	public Map<String, Object> pageGetListStatistics(WbtjglDto wbtjglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		super.setCzdmList(request,map);
		List<WbtjglDto> list = wbtjglService.getPagedDtoList(wbtjglDto);
		List<JcsjDto> detectList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> subList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
		List<JcsjDto> detectionUnitList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECTION_UNIT.getCode());
		List<JcsjDto> inspectionList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
		map.put("detectList", detectList);
		map.put("inspectionList", inspectionList);
		map.put("subList", subList);
		map.put("detectionUnitList", detectionUnitList);
		map.put("total", wbtjglDto.getTotalNumber());
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(WbtjglDto.class, "wbtjid","wbtjmc","hbid","sjdw","jcxm","jcdw","sjdwmc","sfsf","css","jsrq","lrry","lrsj","xgry","xgsj","scry","scsj","scbj","jczxm","jcxmmc","jczxmmc","jcdwmc","hbmc","dwmc","sjqf","sjqfmc");
		SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
		listFilters[0] = filter;
		map.put("rows", JSONObject.toJSONString(list,listFilters));
		return map;
	}


	/**
	 * 获取外部统计列表
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/exterior/viewStatistics")
	@ResponseBody
	public Map<String, Object> viewStatistics(WbtjglDto wbtjglDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isBlank(wbtjglDto.getWbtjid())){
			map.put("status", "fail");
			map.put("message", "未获取到外部统计ID！");
			return map;
		}
		WbtjglDto dtoById = wbtjglService.getDtoById(wbtjglDto.getWbtjid());
		map.put("status", "success");
		map.put("dto", dtoById);
		return map;
	}

	/**
	 * 新增外部统计
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/exterior/addSaveStatistics")
	@ResponseBody
	public Map<String, Object> addSaveStatistics(WbtjglDto wbtjglDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		wbtjglDto.setLrry(user.getYhid());
		wbtjglDto.setWbtjid(StringUtil.generateUUID());
		boolean success = wbtjglService.insert(wbtjglDto);
		map.put("status", success ? "success" : "fail");
		map.put("ywid", wbtjglDto.getWbtjid());
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 修改获取外部统计列表
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/exterior/modStatistics")
	@ResponseBody
	public Map<String, Object> modStatistics(WbtjglDto wbtjglDto) {
		return viewStatistics(wbtjglDto);
	}


	/**
	 * 修改外部统计
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/exterior/modSaveStatistics")
	@ResponseBody
	public Map<String, Object> modSaveStatistics(WbtjglDto wbtjglDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		wbtjglDto.setXgry(user.getYhid());
		boolean success = wbtjglService.update(wbtjglDto);
		map.put("status", success ? "success" : "fail");
		map.put("ywid", wbtjglDto.getWbtjid());
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 删除外部统计
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/exterior/delStatistics")
	@ResponseBody
	public Map<String, Object> delStatistics(WbtjglDto wbtjglDto) {
		Map<String, Object> map = new HashMap<>();
		if (CollectionUtils.isEmpty(wbtjglDto.getIds())){
			map.put("status", "fail");
			map.put("message", "未获取到删除数据！");
			return map;
		}
		User user = getLoginInfo();
		wbtjglDto.setScry(user.getYhid());
		boolean success = wbtjglService.delete(wbtjglDto);
		map.put("status", success ? "success" : "fail");
		map.put("message", success ? xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 处理商务合同信息
	 * @param
	 * @return
	 */
	@RequestMapping("/contract/processContract")
	@ResponseBody
	public Map<String, Object> processContract(SwhtglDto swhtglDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		swhtglDto.setXgry(user.getYhid());
		boolean success = swhtglService.processContract(swhtglDto);
		map.put("status", success ? "success" : "fail");
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 对账设置
	 * @param
	 * @return
	 */
	@RequestMapping("/customer/reconciliationsettingCustomer")
	@ResponseBody
	public Map<String, Object> reconciliationsettingCustomer(SwkhglDto swkhglDto) {
		Map<String, Object> map = new HashMap<>();
		SwkhglDto dto = swkhglService.getDto(swkhglDto);
		map.put("swkhglDto",dto);
		map.put("dzzdlist",redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.RECONCILIATION_FIELD)));
		return map;
	}

	/**
	 * 对账设置
	 * @param
	 * @return
	 */
	@RequestMapping("/customer/reconciliationsettingSaveCustomer")
	@ResponseBody
	public Map<String, Object> reconciliationsettingSaveCustomer(SwkhglDto swkhglDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		swkhglDto.setXgry(user.getYhid());
		boolean isSuccess = swkhglService.updateDzzd(swkhglDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 逾期通知
	 * @param
	 * @return
	 */
	@RequestMapping("/credit/latenoticeSaveReceivableCredit")
	@ResponseBody
	public Map<String, Object> latenoticeSaveReceivableCredit(SwyszkDto swyszkDto) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = getLoginInfo();
		swyszkDto.setXgry(user.getYhid());
		boolean isSuccess = swyszkService.latenoticeSaveReceivableCredit(swyszkDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
