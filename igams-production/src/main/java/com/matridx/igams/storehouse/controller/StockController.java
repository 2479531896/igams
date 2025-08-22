package com.matridx.igams.storehouse.controller;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.BmwlDto;
import com.matridx.igams.production.dao.entities.WlglDto;
import com.matridx.igams.production.service.svcinterface.IBmwlService;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.igams.storehouse.dao.entities.CkhwxxDto;
import com.matridx.igams.storehouse.dao.entities.CkxxDto;
import com.matridx.igams.storehouse.dao.entities.DbglDto;
import com.matridx.igams.storehouse.dao.entities.DbmxDto;
import com.matridx.igams.storehouse.dao.entities.DhthclDto;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.entities.JccglDto;
import com.matridx.igams.storehouse.dao.entities.LlcglDto;
import com.matridx.igams.storehouse.dao.entities.LlglDto;
import com.matridx.igams.storehouse.dao.entities.LlxxDto;
import com.matridx.igams.storehouse.dao.entities.XzdbcglDto;
import com.matridx.igams.storehouse.dao.entities.XzdbglDto;
import com.matridx.igams.storehouse.dao.entities.XzkcxxDto;
import com.matridx.igams.storehouse.dao.entities.XzllcglDto;
import com.matridx.igams.storehouse.dao.entities.XzllmxDto;
import com.matridx.igams.storehouse.dao.entities.XzrkmxDto;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.storehouse.service.svcinterface.ICkxxService;
import com.matridx.igams.storehouse.service.svcinterface.IDbglService;
import com.matridx.igams.storehouse.service.svcinterface.IDhthclService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IJccglService;
import com.matridx.igams.storehouse.service.svcinterface.ILlcglService;
import com.matridx.igams.storehouse.service.svcinterface.ILlglService;
import com.matridx.igams.storehouse.service.svcinterface.ILlxxService;
import com.matridx.igams.storehouse.service.svcinterface.IRkglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzdbcglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzkcxxService;
import com.matridx.igams.storehouse.service.svcinterface.IXzllcglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzllmxService;
import com.matridx.igams.storehouse.service.svcinterface.IXzrkmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/storehouse")
public class StockController extends BaseBasicController {

	@Autowired
	ICkhwxxService ckhwxxService;

	@Autowired
	IHwxxService hwxxService;

	@Autowired
	IRkglService rkglService;

	@Autowired
	ILlcglService llcglService;

	@Autowired
	IXxglService xxglService;

	@Autowired
	IXzllcglService xzllcglService;

	@Autowired
	ICommonService commonService;

	@Autowired
	IJccglService jccglService;

	@Autowired
	ILlglService llglService;

	@Autowired
	private ICkxxService ckxxService;

	@Autowired
	private IXzrkmxService xzrkmxService;

	@Autowired
	IWlglService wlglService;
	
	@Autowired
	IJcsjService jcsjService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	private IDhthclService dhthclService;

	@Override
	public String getPrefix() {
		return urlPrefix;
	}
	
	@Autowired
	IDbglService dbglService;

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private IXzkcxxService xzkcxxService;
	@Autowired IXzdbcglService xzdbcglService;
	@Autowired
	private IXzllmxService xzllmxService;
	@Autowired
	IBmwlService bmwlService;
	@Autowired
	ILlxxService llxxService;
	/**
	 * 跳转库存列表页面
	 * 
	 * @return
	 */

	@RequestMapping("/stock/pageListStock")
	public ModelAndView PageListStock(HttpServletRequest request,LlcglDto llcglDto) {
		ModelAndView mav = new ModelAndView("storehouse/stock/stock_list");
		Map<String,List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[] {
				BasicDataTypeEnum.MATERIELQUALITY_TYPE,	//类别
				BasicDataTypeEnum.MATERIEL_TYPE	//分组
				});
		mav.addObject("lblist",jclist.get(BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode()));
		mav.addObject("fzlist",jclist.get(BasicDataTypeEnum.MATERIEL_TYPE.getCode()));
		
		User user = getLoginInfo(request);
		llcglDto.setRyid(user.getYhid());
		JccglDto jccglDto = new JccglDto();
		jccglDto.setRyid(user.getYhid());
		List<LlcglDto> llcglDtos = llcglService.getDtoList(llcglDto);
		List<JccglDto> jccglDtos = jccglService.getDtoList(jccglDto);
		StringBuilder ids= new StringBuilder();
		if(!CollectionUtils.isEmpty(llcglDtos)) {
			for (LlcglDto llcglDto_t : llcglDtos) {
				ids.append(",").append(llcglDto_t.getCkhwid());
			}
		}
		StringBuilder jyids= new StringBuilder();
		if(!CollectionUtils.isEmpty(jccglDtos)) {
			for (JccglDto jccglDto1 : jccglDtos) {
				jyids.append(",").append(jccglDto1.getCkhwid());
			}
		}
		int llcsl=llcglDtos.size();
		String xzbj = request.getParameter("xzbj");
		CkxxDto ckxxDto = new CkxxDto();
		ckxxDto.setCklbdm("CK");
		if(StringUtil.isNotBlank(xzbj)&&"1".equals(xzbj)){
			ckxxDto.setXzbj(xzbj);
			ckxxDto.setJsid(user.getDqjs());
		}
		List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
		mav.addObject("ckxxDtos",ckxxDtos);

		List<BmwlDto> bms = bmwlService.getAllBm();
		mav.addObject("bms", bms);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("llcsl", Integer.toString(llcsl));
		mav.addObject("idswl", ids.toString());
		mav.addObject("idsJywl", jyids.toString());
		mav.addObject("jccsl",jccglDtos.size());
		mav.addObject("xzbj",StringUtil.isNotBlank(xzbj)?xzbj:"");
		mav.addObject("jsid",user.getDqjs());
		return mav;
	}

	@RequestMapping("/stock/pageListSecureStock")
	public ModelAndView PageListSecureStock(HttpServletRequest request,LlcglDto llcglDto) {
		ModelAndView mav = new ModelAndView("storehouse/stock/stock_secure_list");
		Map<String,List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[] {
				BasicDataTypeEnum.MATERIELQUALITY_TYPE,	//类别
				BasicDataTypeEnum.MATERIEL_TYPE	//分组
		});
		mav.addObject("lblist",jclist.get(BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode()));
		mav.addObject("fzlist",jclist.get(BasicDataTypeEnum.MATERIEL_TYPE.getCode()));

		User user = getLoginInfo(request);
		llcglDto.setRyid(user.getYhid());
		JccglDto jccglDto = new JccglDto();
		jccglDto.setRyid(user.getYhid());
		List<LlcglDto> llcglDtos = llcglService.getDtoList(llcglDto);
		List<JccglDto> jccglDtos = jccglService.getDtoList(jccglDto);
		StringBuilder ids= new StringBuilder();
		if(!CollectionUtils.isEmpty(llcglDtos)) {
			for (LlcglDto llcglDto_t : llcglDtos) {
				ids.append(",").append(llcglDto_t.getCkhwid());
			}
		}
		StringBuilder jyids= new StringBuilder();
		if(!CollectionUtils.isEmpty(jccglDtos)) {
			for (JccglDto jccglDto1 : jccglDtos) {
				jyids.append(",").append(jccglDto1.getCkhwid());
			}
		}
		int llcsl=llcglDtos.size();

		List<BmwlDto> bms = bmwlService.getAllBm();
		mav.addObject("bms", bms);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("llcsl", Integer.toString(llcsl));
		mav.addObject("idswl", ids.toString());
		mav.addObject("idsJywl", jyids.toString());
		mav.addObject("jccsl",jccglDtos.size());
		return mav;
	}
	/**
	 * 获取库存列表
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping("/stock/pageGetListStock")
	@ResponseBody
	public Map<String, Object> getPageDtoStockList(CkhwxxDto ckhwxxDto, HttpServletRequest request) {
		User user = getLoginInfo(request);
		Map<String, Object> map = new HashMap<>();
		List<CkhwxxDto> stocklist = ckhwxxService.getPagedDtoStockList(ckhwxxDto,user.getDqjs());
		map.put("total", ckhwxxDto.getTotalNumber());
		map.put("rows", stocklist);
		return map;
	}

	/**
	 * 获取库存列表
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/stock/pageGetListSecureStock")
	@ResponseBody
	public Map<String, Object> getPageDtoSecureStockList(CkhwxxDto ckhwxxDto, HttpServletRequest request) {
		User user = getLoginInfo(request);
		Map<String, Object> map = new HashMap<>();
		List<CkhwxxDto> stocklist = ckhwxxService.getPagedDtoSecureStockList(ckhwxxDto,user.getDqjs());
		map.put("total", ckhwxxDto.getTotalNumber());
		map.put("rows", stocklist);
		return map;
	}
	/**
	 * 获取库存列表钉钉
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/stock/minidataGetPagedDtoStockListDingTalk")
	@ResponseBody
	public Map<String, Object> minidataGetPagedDtoStockListDingTalk(CkhwxxDto ckhwxxDto, HttpServletRequest request) {
		User user = getLoginInfo(request);
		Map<String, Object> map = new HashMap<>();
		List<CkhwxxDto> stocklist = ckhwxxService.getPagedDtoStockListDingTalk(ckhwxxDto,user.getDqjs());
		List<JcsjDto> wllblist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.MATERIEL_TYPE);
		List<JcsjDto> wlzlblist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.MATERIEL_SUBTYPE);
		List<JcsjDto> wlzllblist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.MATERIELQUALITY_TYPE);
		List<JcsjDto> wlaqlblist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.MATERIEL_SAFE_TYPE);
		map.put("wllblist",wllblist);
		map.put("wlzlblist",wlzlblist);
		map.put("wlzllblist",wlzllblist);
		map.put("wlaqlblist",wlaqlblist);
		map.put("total", ckhwxxDto.getTotalNumber());
		map.put("rows", stocklist);
		return map;
	}
	/**
	 * 库存查看 查看仓库货物信息
	 * 
	 * @param ckhwxxDto
	 * @return
	 */
	@RequestMapping(value = "/stock/viewStock")
	@ResponseBody
	public ModelAndView getJbxxByCkhwid(CkhwxxDto ckhwxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/stock/stock_view");
		// 查看基本信息
		CkhwxxDto jbxxDto = ckhwxxService.getJbxxByCkhwid(ckhwxxDto);
		mav.addObject("jbxxDto", jbxxDto);
		// 查看进货信息
		CkhwxxDto jhxxDto = new CkhwxxDto();
		jhxxDto.setCkhwid(ckhwxxDto.getCkhwid());
//		List<CkhwxxDto> jhxxDtos = ckhwxxService.getJhxxByCkhwid(ckhwxxDto.getCkhwid());
//		List<CkhwxxDto> a=new ArrayList<>();
		List<CkhwxxDto> jhxxDtos = ckhwxxService.getJhxxList(ckhwxxDto);
		List<CkhwxxDto> ckhwxxDtos=ckhwxxService.getHwxxtbyCkhwid(ckhwxxDto.getCkhwid());
		mav.addObject("jhxxDtos", jhxxDtos);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("hwxxDtos",ckhwxxDtos);
		return mav;
	}

	@RequestMapping(value = "/stock/viewSecureStock")
	@ResponseBody
	public ModelAndView getSecureJbxxByCkhwid(CkhwxxDto ckhwxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/stock/stock_secure_view");
		// 查看基本信息
		List<CkhwxxDto> jbxxDto = ckhwxxService.getJbxxListByCkhwid(ckhwxxDto);
		mav.addObject("jbxxDtos", jbxxDto);
		// 查看进货信息
		CkhwxxDto jhxxDto = new CkhwxxDto();
		jhxxDto.setWlid(ckhwxxDto.getWlid());
//		List<CkhwxxDto> jhxxDtos = ckhwxxService.getJhxxByCkhwid(ckhwxxDto.getCkhwid());
//		List<CkhwxxDto> a=new ArrayList<>();
		List<CkhwxxDto> jhxxDtos = ckhwxxService.getJhxxListByWlid(ckhwxxDto);
		List<CkhwxxDto> ckhwxxDtos=ckhwxxService.getHwxxtbyWlid(ckhwxxDto.getWlid());
		mav.addObject("jhxxDtos", jhxxDtos);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("hwxxDtos",ckhwxxDtos);
		return mav;
	}

	/**
	 * 领料车页面
	 * 
	 * @param llglDto
	 * @param request
	 * @return
	 */
	@RequestMapping("/stock/pickingCarstock")
	public ModelAndView pickingCarstock(LlglDto llglDto, HttpServletRequest request) {
		User user = getLoginInfo(request);
		llglDto.setSqry(user.getYhid());// 默认申请人
		//获取默认部门
		user = commonService.getUserInfoById(user);
		if (user != null) {
			llglDto.setSqrmc(user.getZsxm());
			llglDto.setSqbmmc(user.getJgmc());
			llglDto.setSqbm(user.getJgid());
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
			Date date = new Date();
			llglDto.setSqrq(sdf.format(date));//申请时间
		}
		llglDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_APPLY.getCode());
		ModelAndView mav = new ModelAndView("storehouse/stock/pickingCar");
		String lldh = llglService.generateDjh(llglDto); //生成领料单
		llglDto.setLldh(lldh);
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DELIVERY_TYPE,BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,BasicDataTypeEnum.PURCHASE_ITEMENCODING});
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		mav.addObject("xmdllist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode())));// 项目大类
		mav.addObject("xmbmlist", JSONObject.toJSONString(jclist.get(BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode())));// 项目编码
        llglDto.setYwlx(BusTypeEnum.IMP_REQUISITION.getCode());
		mav.addObject("llglDto", llglDto);
		mav.addObject("formAction", "/storehouse/stock/addSavePicking");
		mav.addObject("url", "/storehouse/stock/pagedataPickingList");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取已加入领料车的数据
	 * 
	 * @param llcglDto
	 * @param request
	 * @return
	 */
	@RequestMapping("/stock/pagedataPickingList")
	@ResponseBody
	public Map<String, Object> pickingList(LlcglDto llcglDto, HttpServletRequest request) {
		User user = getLoginInfo(request);
		llcglDto.setRyid(user.getYhid());
		List<LlcglDto> llcgllist = llcglService.getLlcDtoList(llcglDto);
		Map<String, Object> map = new HashMap<>();
		map.put("total", llcglDto.getTotalNumber());
		map.put("rows", llcgllist);
		return map;
	}

	/**
	 * 将物料添加至领料车
	 * 
	 * @param
	 * @param request
	 * @return
	 */
	@RequestMapping("/stock/pagedataAddToPickingCar")
	@ResponseBody
	public Map<String, Object> addToPickingCar(LlcglDto llcglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		llcglDto.setRyid(user.getYhid());
		LlcglDto llcglDtoT = llcglService.getDto(llcglDto);
		boolean isSuccess = true;
		if(llcglDtoT==null){
			isSuccess = llcglService.insert(llcglDto);
		}
		List<LlcglDto> llcglDtos = llcglService.getDtoList(llcglDto);
		StringBuilder ids= new StringBuilder();
		if(!CollectionUtils.isEmpty(llcglDtos)) {
			for (LlcglDto llcglDto_t : llcglDtos) {
				ids.append(",").append(llcglDto_t.getCkhwid());
			}
			ids = new StringBuilder(ids.substring(1));
		}		
		map.put("idswl", ids.toString());
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 从领料车删除物料
	 * 
	 * @param
	 * @param request
	 * @return
	 */
	@RequestMapping("/stock/pagedataDelFromPickingCar")
	@ResponseBody
	public Map<String, Object> delFromPickingCar(LlcglDto llcglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		llcglDto.setRyid(user.getYhid());
		boolean isSuccess = llcglService.delete(llcglDto);
		// 获取领料车已加入的物料数量
		List<LlcglDto> llcgllist = llcglService.getLlcDtoList(llcglDto);
		String ids = "";
		if(!CollectionUtils.isEmpty(llcgllist)) {
			for (LlcglDto t_llcglDto : llcgllist) {
				ids = "," + t_llcglDto.getCkhwid();
			}
			ids = ids.substring(1);
		}
		map.put("idswl", ids);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr()
				: xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	@RequestMapping("/stock/addSavePicking")
	@ResponseBody
	public Map<String, Object> addSavePicking(LlglDto llglDto, HttpServletRequest request) throws BusinessException {
		User user = getLoginInfo(request);
		llglDto.setLrry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		// 校验领料单号是否重复
		boolean result = llglService.getDtoByLldh(llglDto);
		if (!result) {
			map.put("status", "fail");
			map.put("message", "领料单号不允许重复！");
			return map;
		}

		boolean isSuccess = llglService.addSavePicking(llglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
				: xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid", llglDto.getLlid());
		return map;
	}

	/**
	 * 判断领料车中是否有此物料
	 *
	 * @param
	 * @param request
	 * @return
	 */
	@RequestMapping("/stock/pagedataCheckToPickingCar")
	@ResponseBody
	public Map<String, Object> pagedataCheckToPickingCar(LlcglDto llcglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		llcglDto.setRyid(user.getYhid());
		LlcglDto dto = llcglService.getDto(llcglDto);
		map.put("llcglDto", dto);
		return map;
	}

	/**
	 * 货物退回处理列表
	 */
	@RequestMapping("/goodback/pageListGoodBack")
	public ModelAndView pageListGoodBack() {
		ModelAndView mav = new ModelAndView("storehouse/goodback/goodback_list");
		mav.addObject("auditType",AuditTypeEnum.AUDIT_GOODS_BACK.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 获取货物退回列表
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping("/goodback/goodbackpageList")
	@ResponseBody
	public Map<String, Object> goodbackpageList(DhthclDto dhthclDto) {
		Map<String, Object> map = new HashMap<>();
		List<DhthclDto> stocklist = dhthclService.getPagedDtoStockList(dhthclDto);
		map.put("total", dhthclDto.getTotalNumber());
		map.put("rows", stocklist);
		return map;
	}
	
	/**
	 * 货物退回处置页面
	 */
	@RequestMapping("/goodback/disposeGoodBack")
	public ModelAndView disposeGoodBack(DhthclDto dhthclDto) {
		ModelAndView mav = new ModelAndView("storehouse/goodback/goodback_dispose");
		mav.addObject(dhthclDto);
		mav.addObject("auditType",AuditTypeEnum.AUDIT_GOODS_BACK.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 领料调拨跳转
	 */
	@RequestMapping("/stock/allocationStock")
	public ModelAndView allocationStock(HttpServletRequest request,DbglDto dbglDto) {
		ModelAndView mav = new ModelAndView("storehouse/stock/stock_allocation");
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
		Date date = new Date();
		dbglDto.setDbrq(sdf.format(date));//申请时间
		User user = getLoginInfo(request);
		dbglDto.setJsr(user.getYhid());
		dbglDto.setJsrmc(user.getZsxm());
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.DELIVERY_TYPE,//出库
						BasicDataTypeEnum.INBOUND_TYPE}//入库r
						);
		mav.addObject("cklblist", jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode()));// 出库类别
		mav.addObject("rklblist", jclist.get(BasicDataTypeEnum.INBOUND_TYPE.getCode()));// 入库类别
		//获取仓库信息
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setCsdm("CK");
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto = jcsjService.getDto(jcsjDto);
		CkxxDto ckxxDto = new CkxxDto();
		List<CkxxDto> ckxxList = new ArrayList<>();
		if(jcsjDto!=null) {
			ckxxDto.setCklb(jcsjDto.getCsid());
			ckxxList = ckxxService.getDtoList(ckxxDto);
		}
		//生成调拨单号
		String dbdh = dbglService.generateDbdh();
		dbglDto.setDbdh(dbdh);
		dbglDto.setFormAction("/storehouse/stock/allocationSaveStock");
		mav.addObject("dbglDto", dbglDto);
		mav.addObject("ckxxList",ckxxList);
		mav.addObject("url", "/storehouse/stock/pagedataGetrows");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取rows
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/stock/pagedataGetrows")
	@ResponseBody
	public Map<String, Object> getrows() {
		List<DbmxDto> dbmxlist = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("rows", dbmxlist);
		return map;
	}

	/**
	 * 查询要添加的物料信息
	 * @param ckhwxxDto
	 * @return
	 */
	@RequestMapping("/stock/pagedataQueryWlxxByWlid")
	@ResponseBody
	public Map<String,Object> queryWlxxByWlid(CkhwxxDto ckhwxxDto){
		CkhwxxDto ckhwxxDto_t = ckhwxxService.queryWlxxByWlid(ckhwxxDto);
		List<CkhwxxDto> ckhwxxDtoList = ckhwxxService.getWlxxByWlid(ckhwxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("ckhwxxDto", ckhwxxDto_t);
		map.put("ckhwxxDtoList", ckhwxxDtoList);
		return map;
	}
	/**
	 * 查询要添加的物料信息
	 * @param ckhwxxDto
	 * @return
	 */
	@RequestMapping("/stock/pagedataQueryCkhwxxByWlid")
	@ResponseBody
	public Map<String,Object> pagedataQueryCkhwxxByWlid(CkhwxxDto ckhwxxDto){
		CkhwxxDto ckhwxxDto_t = ckhwxxService.queryCkhwxxGroupByWlid(ckhwxxDto);
		List<CkhwxxDto> ckhwxxDtoList = ckhwxxService.getWlxxByWlid(ckhwxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("ckhwxxDto", ckhwxxDto_t);
		map.put("ckhwxxDtoList", ckhwxxDtoList);
		return map;
	}
	/**
	 * 查询生产批号
	 * @param
	 * @return
	 */
	@RequestMapping("/stock/scphList")
	@ResponseBody
	public Map<String,Object> scphList(HwxxDto hwxxDto){
		List<HwxxDto> hwxxDtoList = hwxxService.selectHWxx(hwxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("hwxxDtoList", hwxxDtoList);
		return map;
	}
	/**
	 * 查询货位信息
	 * @param
	 * @return
	 */
	@RequestMapping("/stock/pagedataCkxxList")
	@ResponseBody
	public Map<String,Object> ckxxList(CkxxDto ckxxDto){
		List<CkxxDto> ckxxDtoLists = ckxxService.getKwDtoListByFckid(ckxxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("ckxxDtoList", ckxxDtoLists);
		return map;
	}
	
	/**
	 * 调拨保存
	 * @param
	 * @return
	 */
	@RequestMapping("/stock/allocationSaveStock")
	@ResponseBody
	public Map<String,Object> allocationsave(DbglDto dbglDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		//判断调拨单是否存在
		User user = getLoginInfo(request);
		dbglDto.setLrry(user.getYhid());
		// 判断入库单号是否重复
		boolean isSuccess = dbglService.isDbdhRepeat(dbglDto.getDbdh(),dbglDto.getDbid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "调拨单号不允许重复！");
			return map;
		}
		
		try {
			isSuccess = dbglService.allocationsave(dbglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
			map.put("auditType",AuditTypeEnum.AUDIT_GOODS_STORAGE.getCode());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	
	/**
	 * 调拨明细选择界面
	 */
	@RequestMapping("/stock/pagedataChooseDbmx")
	public ModelAndView chooseDbmx(HwxxDto hwxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/stock/stock_chooseDbmx");
		CkxxDto ckxxDto = new CkxxDto();
		ckxxDto.setFckid(hwxxDto.getZrck());
		List<CkxxDto> kwlist=ckxxService.getKwDtoListByFckid(ckxxDto);
		mav.addObject("kwlist", JSONObject.toJSONString(kwlist));
		mav.addObject("hwxxDto", hwxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 获取货物明细数据
	 * @param
	 * @return
	 */
	@RequestMapping("/stock/pagedataGetHwxxList")
	@ResponseBody
	public Map<String,Object> getHwxxList(HwxxDto hwxxDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		List<HwxxDto> hwxxList = hwxxService.queryHWxx(hwxxDto,user.getDqjs());
		Map<String,Object> map = new HashMap<>();
		map.put("rows", hwxxList);
		return map;
	}

	/**
	 * 跳转库存列表页面
	 *
	 * @return
	 */

	@RequestMapping("/stock/pageListAllStock")
	public ModelAndView pageListAllStock() {
		ModelAndView mav = new ModelAndView("storehouse/stock/allstock_list");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取库存列表
	 *
	 * @param hwxxDto
	 * @return
	 */
	@RequestMapping("/stock/pageGetListAllStock")
	@ResponseBody
	public Map<String, Object> getPageDtoAllStockList(HwxxDto hwxxDto) {
		Map<String, Object> map = new HashMap<>();
		List<HwxxDto> pagedDtoAllStockList = hwxxService.getPagedDtoAllStockList(hwxxDto);
		map.put("total", hwxxDto.getTotalNumber());
		map.put("rows", pagedDtoAllStockList);
		return map;
	}

	/**
	 * 跳转全部库存列表查看页面
	 * @return
	 */
	@RequestMapping("/stock/viewAllStock")
	public ModelAndView viewAllStock(HwxxDto hwxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/stock/allstock_view");
		HwxxDto hwxxDto_t = hwxxService.viewStockDto(hwxxDto);
		mav.addObject("hwxx", hwxxDto_t);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 查询要添加的物料信息
	 * @param
	 * @return
	 */
	@RequestMapping("/stock/queryXzkcInfo")
	@ResponseBody
	public Map<String,Object> queryXzkcInfo(XzrkmxDto xzrkmxDto){
		XzrkmxDto xzrkmxDto1 = xzrkmxService.getDto(xzrkmxDto);
		Map<String,Object> map = new HashMap<>();
		map.put("xzrkmxDto", xzrkmxDto1);
		return map;
	}

	/**
	 * 将物料添加至领料车
	 *
	 * @param
	 * @param request
	 * @return
	 */
	@RequestMapping("/stock/pagedataAddToXzPickingCar")
	@ResponseBody
	public Map<String, Object> addToXzPickingCar(XzllcglDto xzllcglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		xzllcglDto.setRyid(user.getYhid());
		boolean isSuccess = xzllcglService.insert(xzllcglDto);
		List<XzllcglDto> xzllcglDtos = xzllcglService.getDtoList(xzllcglDto);
		StringBuilder ids= new StringBuilder();
		if(!CollectionUtils.isEmpty(xzllcglDtos)) {
			for (XzllcglDto xzllcglDto_t : xzllcglDtos) {
				ids.append(",").append(xzllcglDto_t.getXzkcid());
			}
			if (ids.length()>0){
				ids = new StringBuilder(ids.substring(1));
			}
		}
		map.put("ids", ids.toString());
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 从领料车删除物料
	 *
	 * @param
	 * @param request
	 * @return
	 */
	@RequestMapping("/stock/pagedataDelFromXzPickingCar")
	@ResponseBody
	public Map<String, Object> delFromXzPickingCar(XzllcglDto xzllcglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		xzllcglDto.setRyid(user.getYhid());
		boolean isSuccess = xzllcglService.delete(xzllcglDto);
		// 获取领料车已加入的物料数量
		List<XzllcglDto> xzllcglDtos = xzllcglService.getDtoList(xzllcglDto);
		StringBuilder ids = new StringBuilder();
		if(!CollectionUtils.isEmpty(xzllcglDtos)) {
			for (XzllcglDto t_xzllcglDto : xzllcglDtos) {
				ids.append(",").append(t_xzllcglDto.getXzkcid());
			}
			ids = new StringBuilder(ids.substring(1));
		}
		XzkcxxDto xzkcxxDto=new XzkcxxDto();
		xzkcxxDto.setXzkcid(xzllcglDto.getXzkcid());
		XzkcxxDto dto = xzkcxxService.getDto(xzkcxxDto);
		if(dto!=null){
			map.put("klsl", dto.getKlsl());
		}
		map.put("ids", ids.toString());
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr()
				: xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 将物料添加至调拨车
	 *
	 * @param
	 * @param request
	 * @return
	 */
	@RequestMapping("/stock/pagedataAddToXzAllocationCar")
	@ResponseBody
	public Map<String, Object> addToXzAllocationCar(XzdbcglDto xzdbcglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		xzdbcglDto.setRyid(user.getYhid());
		boolean isSuccess = xzdbcglService.insert(xzdbcglDto);
		List<XzdbcglDto> xzdbcglDtos = xzdbcglService.getDtoList(xzdbcglDto);
		StringBuilder idss= new StringBuilder();
		if(!CollectionUtils.isEmpty(xzdbcglDtos)) {
			for (XzdbcglDto xzdbcglDto_t : xzdbcglDtos) {
				idss.append(",").append(xzdbcglDto_t.getXzkcid());
			}
			idss = new StringBuilder(idss.substring(1));
		}
		map.put("idss", idss.toString());
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 从调拨车删除物料
	 *
	 * @param
	 * @param request
	 * @return
	 */
	@RequestMapping("/stock/pagedataDelFromXzAllocationCar")
	@ResponseBody
	public Map<String, Object> delFromXzAllocationCar(XzdbcglDto xzdbcglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		xzdbcglDto.setRyid(user.getYhid());
		boolean isSuccess = xzdbcglService.deleteById(xzdbcglDto.getXzkcid());
		// 获取调拨车已加入的物料数量
		List<XzdbcglDto> xzdbcglDtos = xzdbcglService.getDtoList(xzdbcglDto);
		String idss = "";
		if(!CollectionUtils.isEmpty(xzdbcglDtos)) {
			for (XzdbcglDto xzdbcglDto_t : xzdbcglDtos) {
				idss = "," + xzdbcglDto_t.getXzkcid();
			}
			idss = idss.substring(1);
		}
		XzkcxxDto xzkcxxDto=new XzkcxxDto();
		xzkcxxDto.setXzkcid(xzdbcglDto.getXzkcid());
		XzkcxxDto dto = xzkcxxService.getDto(xzkcxxDto);
		if(dto!=null){
			map.put("kcl", dto.getKcl());
			map.put("yds",dto.getYds());
			map.put("klsl",dto.getKlsl());
		}
		map.put("idss", idss);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr()
				: xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	/**
	 * 跳转 行政库存列表 页面
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/stock/pageListAdministrationStockt")
	public ModelAndView pageListAdministrationStockt(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/stock/administrationStock_list");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.LOCATION_CATEGORY});
		mav.addObject("kwlblist", jclist.get(BasicDataTypeEnum.LOCATION_CATEGORY.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		XzllcglDto xzllcglDto=new XzllcglDto();
		User user = getLoginInfo(request);
		xzllcglDto.setRyid(user.getYhid());
		XzdbcglDto xzdbcglDto=new XzdbcglDto();
		List<XzllcglDto> xzllcglDtos = xzllcglService.getDtoList(xzllcglDto);
		List<XzdbcglDto> xzdbcglDtos=xzdbcglService.getDtoList(xzdbcglDto);
		StringBuilder ids= new StringBuilder();
		if(!CollectionUtils.isEmpty(xzllcglDtos)) {
			for (XzllcglDto xzllcglDto_t : xzllcglDtos) {
				ids.append(",").append(xzllcglDto_t.getXzkcid());
			}
			ids = new StringBuilder(ids.substring(1));
		}
		StringBuilder idss= new StringBuilder();
		if(!CollectionUtils.isEmpty(xzdbcglDtos)) {
			for (XzdbcglDto xzdbcglDto_t : xzdbcglDtos) {
				idss.append(",").append(xzdbcglDto_t.getXzkcid());
			}
			idss = new StringBuilder(idss.substring(1));
		}
		int llcsl=xzllcglDtos.size();
		int dbcsl=xzdbcglDtos.size();
		mav.addObject("llcsl", Integer.toString(llcsl));
		mav.addObject("dbcsl", Integer.toString(dbcsl));
		mav.addObject("ids", ids.toString());
		mav.addObject("idss", idss.toString());
		return mav;
	}

	/**
	 * 获取 行政库存 数据
	 * @return
	 */
	@RequestMapping("/stock/pageGetListAdministrationStockt")
	@ResponseBody
	public Map<String, Object> getPageDtoAdministrationStockList(XzkcxxDto xzkcxxDto) {
		Map<String, Object> map = new HashMap<>();
		List<XzkcxxDto> xzkcxxDtoList = xzkcxxService.getPagedDtoList(xzkcxxDto);
		map.put("total", xzkcxxDto.getTotalNumber());
		map.put("rows", xzkcxxDtoList);
		return map;
	}
	/**
	 * 获取 钉钉行政库存 数据
	 * @param xzkcxxDto
	 * @return
	 */
	@RequestMapping("/stock/minidataGetListAdministrationStockt")
	@ResponseBody
	public Map<String, Object> minidataGetListAdministrationStockt(XzkcxxDto xzkcxxDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		XzllcglDto xzllcglDto=new XzllcglDto();
		User user = getLoginInfo(request);
		xzllcglDto.setRyid(user.getYhid());
		List<XzllcglDto> xzllcglDtos = xzllcglService.getDtoList(xzllcglDto);
		StringBuilder ids= new StringBuilder();
		if(!CollectionUtils.isEmpty(xzllcglDtos)) {
			for (XzllcglDto xzllcglDto_t : xzllcglDtos) {
				ids.append(",").append(xzllcglDto_t.getXzkcid());
			}
			ids = new StringBuilder(ids.substring(1));
		}
		List<XzkcxxDto> xzkcxxDtoList = xzkcxxService.getPagedDtoList(xzkcxxDto);
		int llcsl=xzllcglDtos.size();
		map.put("llcsl", Integer.toString(llcsl));
		map.put("total", xzkcxxDto.getTotalNumber());
		map.put("rows", xzkcxxDtoList);
		map.put("ids", ids.toString());
		map.put("urlPrefix", urlPrefix);
		return map;
	}

	/**
	 * 库存查看 查看仓库货物信息
	 * @return
	 */
	@RequestMapping(value = "/stock/viewAdministrationStock")
	@ResponseBody
	public ModelAndView viewAdministrationStock(XzkcxxDto xzkcxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/stock/administrationStock_view");
		xzkcxxDto=xzkcxxService.getDto(xzkcxxDto);
		mav.addObject("xzkcxxDto",xzkcxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 行政库存查看领料信息
	 * @return
	 */
	@RequestMapping(value="/stock/pagedataAdministrationStockLlmx")
	@ResponseBody
	public Map<String,Object> pagedataAdministrationStockLlmx(XzllmxDto xzllmxDto){
		Map<String,Object> map = new HashMap<>();
		List<XzllmxDto> xzllmxDtoList=xzllmxService.getDtoXzllmxListByXzkcid(xzllmxDto.getXzkcid());
		map.put("rows",xzllmxDtoList);
		return map;
	}
	/**
	 * 行政库存查看入库信息
	 * @param xzrkmxDto
	 * @return
	 */
	@RequestMapping(value="/stock/pagedataAdministrationStorage")
	@ResponseBody
	public Map<String,Object> pagedataAdministrationStorage(XzrkmxDto xzrkmxDto){
		Map<String,Object> map = new HashMap<>();
		List<XzrkmxDto> xzrkmxDtos=xzrkmxService.getDtoList(xzrkmxDto);
		map.put("rows",xzrkmxDtos);
		return map;
	}

	/**
	 * 跳转 行政库存修改 页面
	 * @param xzrkmxDto
	 * @return
	 */
	@RequestMapping(value="/stock/modAdministrationStock")
	public ModelAndView modAdministrationStock(XzrkmxDto xzrkmxDto){
		ModelAndView mav = new ModelAndView("storehouse/stock/administrationStock_edit");
		XzrkmxDto xzrkmxDto_t = xzrkmxService.getJbxxByXzrkmxid(xzrkmxDto);
		//获取仓库列表
		JcsjDto ckDto = new JcsjDto();
		JcsjDto kwDto = new JcsjDto();
		List<JcsjDto> cklist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		for (JcsjDto ck : cklist) {
			if ("CK".equals(ck.getCsdm())){
				ckDto = ck;
			}
			if ("KW".equals(ck.getCsdm())) {
				kwDto = ck;
			}
		}
		CkxxDto ckxxDto = new CkxxDto();
		CkxxDto kwxxDto = new CkxxDto();
		//获取仓库列表
		ckxxDto.setCklb(ckDto.getCsid());
		List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
		//获取库位列表
		kwxxDto.setCklb(kwDto.getCsid());
		List<CkxxDto> kwxxDtos = ckxxService.getDtoList(kwxxDto);
		mav.addObject("cklist",ckxxDtos);
		mav.addObject("kwlist",kwxxDtos);
		mav.addObject("kwlb",kwDto.getCsid());
		mav.addObject("xzrkmxDto", xzrkmxDto_t);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}


	/**
	 * 行政库存修改 保存
	 * @param xzrkmxDto
	 * @return
	 */
	@RequestMapping(value="/stock/modSaveAdministrationStock")
	@ResponseBody
	public Map<String,Object> modSaveAdministrationStock(XzrkmxDto xzrkmxDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		//获取用户信息，设置修改人员
		User user = getLoginInfo(request);
		xzrkmxDto.setXgry(user.getYhid());
		boolean isSuccess = xzrkmxService.updateAdministrationStockByXzrkmxid(xzrkmxDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):(xxglService.getModelById("ICOM00002").getXxnr()));
		return map;
	}
	/**
	 * 行政库存 删除
	 * @param xzrkmxDto
	 * @return
	 */
	@RequestMapping(value="/stock/delAdministrationStock")
	@ResponseBody
	public Map<String,Object> delAdministrationStock(XzrkmxDto xzrkmxDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		//获取用户信息，设置修改人员
		User user = getLoginInfo(request);
		xzrkmxDto.setScry(user.getYhid());
		boolean isSuccess = xzrkmxService.delAdministrationStockByXzrkmxid(xzrkmxDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?(xxglService.getModelById("ICOM00003").getXxnr()):(xxglService.getModelById("ICOM00004").getXxnr()));
		return map;
	}
	/**
	 * 点击调拨跳转模块框
	 * @return
	 */
	@RequestMapping(value = "/stock/pagedataAdministrationStockAllocation")
	@ResponseBody
	public ModelAndView administrationStockAllocation(XzkcxxDto xzkcxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/stock/administrationStock_allocation");
		xzkcxxDto=xzkcxxService.getDto(xzkcxxDto);
		mav.addObject("xzkcxxDto",xzkcxxDto);
		xzkcxxDto.setFormAction("pagedataAllocationAdministrationStock");
		List<JcsjDto> kwlist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.LOCATION_CATEGORY);
		mav.addObject("kwlist",kwlist);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 增加信息到数据库
	 * @param
	 * @return
	 */
	@RequestMapping("/stock/pagedataAllocationAdministrationStock")
	@ResponseBody
	public Map<String, Object> allocationAdministrationStock(XzdbglDto xzdbglDto,XzkcxxDto xzkcxxDto,HttpServletRequest request){
		Map<String, Object> map=new HashMap<>();
		User user = getLoginInfo(request);
		if (xzdbglDto.getDckw().equals(xzdbglDto.getDrkw())) {
			map.put("status", "fail");
			map.put("message", "调入库位与原库位相同");
			return map;
		}

		if (Double.parseDouble(xzkcxxDto.getKlsl())<Double.parseDouble(xzkcxxDto.getDbsl())){
			map.put("status", "fail");
			map.put("message", "调拨数量不允许大于库存量");
			return map;

		}
		try {
			boolean iSsuccess=xzkcxxService.Dbcz(xzdbglDto,xzkcxxDto,user);
			map.put("status", iSsuccess ? "success" : "fail");
			map.put("message", iSsuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
		}catch (BusinessException e){
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}

			return map;
	}
	/**
	 * 点击调拨车跳转模块框
	 * @return
	 */
	@RequestMapping(value = "/stock/allocationCarStock")
	@ResponseBody
	public ModelAndView StockAllocationCar(XzkcxxDto xzkcxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/stock/allocationCar");
		xzkcxxDto.setFormAction("pagedataAllocationCarSaveStock");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/stock/pagedataAllocationCar")
	@ResponseBody
	public Map<String, Object> allocationCar(XzdbcglDto xzdbcglDto){
		Map<String, Object> map=new HashMap<>();
		List<XzkcxxDto> xzkcxxDtos=xzkcxxService.getDtoByXzkcids(xzdbcglDto);
		List<JcsjDto> kwlist = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.LOCATION_CATEGORY);
		map.put("kwlist",kwlist);
		map.put("rows", xzkcxxDtos);
		return map;
	}
	/**
	 *将调拨车里的数据加到库存表
	 * @param
	 * @return
	 */
	@RequestMapping("/stock/pagedataAllocationCarSaveStock")
	@ResponseBody
	public Map<String, Object> addToXzkcxx(XzdbglDto xzdbglDto,HttpServletRequest request){
		Map<String, Object> map=new HashMap<>();
		User user = getLoginInfo(request);
		try {
			boolean iSsuccess=xzkcxxService.czDbc(xzdbglDto,user);
			map.put("status", iSsuccess ? "success" : "fail");
			map.put("message", iSsuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
		}catch (BusinessException e){
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 点击库存维护弹出模态框
	 * @return
	 */
	@RequestMapping(value = "/stock/stockupholdButton")
	@ResponseBody
	public ModelAndView stockUphold(XzkcxxDto xzkcxxDto) {
		xzkcxxDto=xzkcxxService.getDto(xzkcxxDto);
		ModelAndView mav = new ModelAndView("storehouse/stock/stock_uphold");
		xzkcxxDto.setFormAction("stockupholdSaveButton");
		mav.addObject("xzkcxxDto",xzkcxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 *将安全库存插入到数据库中
	 * @param
	 * @return
	 */
	@RequestMapping("/stock/stockupholdSaveButton")
	@ResponseBody
	public Map<String, Object> insertAykc(XzkcxxDto xzkcxxDto){
		Map<String, Object> map=new HashMap<>();
		boolean iSsuccess=xzkcxxService.updateAqkc(xzkcxxDto);
		map.put("status", iSsuccess ? "success" : "fail");
		map.put("message", iSsuccess ? xxglService.getModelById("ICOM00001").getXxnr()
				: xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 库存信息统计
	 *
	 * @return
	 */
	@RequestMapping(value = "/stock/minidataSockStatis")
	@ResponseBody
	public Map<String,Object> minidatasSockStatis() {
		Map<String, Object> map=new HashMap<>();
		List<HwxxDto> hwxxDtos = hwxxService.getWlCountGroupByCk();
		List<JcsjDto> wllbs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MATERIEL_TYPE.getCode());
		List<JcsjDto> wlzlbs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MATERIEL_SUBTYPE.getCode());
		if (!CollectionUtils.isEmpty(hwxxDtos)){
			int sum = 0;
			for (HwxxDto dto : hwxxDtos) {
				sum+= Integer.parseInt(dto.getTs());
			}
			for (HwxxDto dto : hwxxDtos) {
				if (sum==0){
					dto.setBfb(0);
					dto.setCount(0);
				}else {
					String ts = dto.getTs();
					BigDecimal tsBig = new BigDecimal(ts);
					BigDecimal sumBig = new BigDecimal(sum);
					String bfb = tsBig.divide(sumBig, 4, RoundingMode.HALF_UP).setScale(4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).toString();
					dto.setBfb(Double.parseDouble(bfb));
					dto.setCount(1);
				}
			}
		}
		map.put("hwxxDtos",hwxxDtos);
		map.put("wllbs",wllbs);
		map.put("wlzlbs",wlzlbs);
		return map;
	}
	/**
	 * 库存物料信息统计
	 *
	 * @return
	 */
	@RequestMapping(value = "/stock/minidataSockWlStatis")
	@ResponseBody
	public List<HwxxDto> minidataSockWlStatis(HwxxDto hwxxDto) {
		return hwxxService.getWlSockByCkid(hwxxDto);
	}
	/**
	 * 库存物料信息查看
	 *
	 * @return
	 */
	@RequestMapping(value = "/stock/minidataSockWlView")
	@ResponseBody
	public Map<String, Object> minidataSockWlView(HwxxDto hwxxDto) {
		Map<String, Object> map = new HashMap<>();
		WlglDto wlglDto = wlglService.getDtoById(hwxxDto.getWlid());
		List<HwxxDto> hwxxDtos = hwxxService.getWlSockxxByCkidAndWlid(hwxxDto);
		map.put("wlglDto", wlglDto);
		map.put("hwxxDtos", hwxxDtos);
		return map;
	}
	/**
	 * 调拨列表删除
	 *
	 * @return
	 */
	@RequestMapping("/stock/delAllocationStock")
	@ResponseBody
	public Map<String, Object> delAllocationStock(DbglDto dbglDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		try {
			dbglDto.setScry(user.getYhid());
			boolean isSuccess = dbglService.DeleteAllocations(dbglDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		}
		catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
		}
		return map;
	}

	/**
	 * @Description: 实验室库存列表（直接领取无需流程）
	 * @param request
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2025/8/18 15:35
	 */
	@RequestMapping("/storehouse/pageListStorehouse")
	public ModelAndView pageListStorehouse(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/stock/storehouse_list");
		User user = getLoginInfo(request);
		CkxxDto ckxxDto = new CkxxDto();
		ckxxDto.setCklbdm("CK");
		ckxxDto.setXzbj("1");
		ckxxDto.setJsid(user.getDqjs());
		List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
		mav.addObject("ckxxDtos",ckxxDtos);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("jsid",user.getDqjs());
		return mav;
	}

	/**
	 * @Description: 获取库存信息
	 * @param hwxxDto
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2025/8/18 15:57
	 */
	@RequestMapping("/storehouse/pageGetListStorehouse")
	@ResponseBody
	public Map<String, Object> pageGetListStorehouse(HwxxDto hwxxDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		List<HwxxDto> stocklist = hwxxService.getPagedDtoByJsid(hwxxDto);
		map.put("total", hwxxDto.getTotalNumber());
		map.put("rows", stocklist);
		return map;
	}

	/**
	 * @Description: 领料
	 * @param llxxDto
	 * @param request
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2025/8/19 15:00
	 */
	@RequestMapping("/storehouse/pagedataLl")
	public ModelAndView pagedataLl(LlxxDto llxxDto, HttpServletRequest request) {
		ModelAndView mav =new ModelAndView("storehouse/stock/storehouse_ll");
		User loginInfo = getLoginInfo(request);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		llxxDto.setZsxm(loginInfo.getZsxm());
		llxxDto.setQlry(loginInfo.getYhid());
		llxxDto.setLlrq(sdf.format(date));
		mav.addObject("llxxDto", llxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * @Description: 领料保存
	 * @param llxxDto
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2025/8/19 16:09
	 */
	@RequestMapping(value = "/storehouse/pagedataSaveLl")
	@ResponseBody
	public Map<String,Object> pagedataSaveLl(LlxxDto llxxDto, HttpServletRequest request) {
		User user = getLoginInfo(request);
		llxxDto.setLrry(user.getYhid());
		Map<String,Object> map=new HashMap<>();
		boolean isSuccess;
		try {
			isSuccess = llxxService.saveLlxxDto(llxxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * @Description: 领料列表
	 * @param request
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2025/8/19 16:10
	 */
	@RequestMapping("/storehouse/pageListLl")
	public ModelAndView pageListLl(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/stock/ll_list");
		User user = getLoginInfo(request);
		CkxxDto ckxxDto = new CkxxDto();
		ckxxDto.setCklbdm("CK");
		ckxxDto.setXzbj("1");
		ckxxDto.setJsid(user.getDqjs());
		List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
		mav.addObject("ckxxDtos",ckxxDtos);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("jsid",user.getDqjs());
		return mav;
	}

	/**
	 * @Description: 获取领料列表数据
	 * @param llxxDto
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2025/8/19 16:12
	 */
	@RequestMapping("/storehouse/pageGetListLl")
	@ResponseBody
	public Map<String, Object> pageGetListLl(LlxxDto llxxDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		List<LlxxDto> llxxDtos = llxxService.getPagedDtoByJsid(llxxDto);
		map.put("total", llxxDto.getTotalNumber());
		map.put("rows", llxxDtos);
		return map;
	}

	/**
	 * @Description: 领料删除
	 * @param llxxDto
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2025/8/19 16:43
	 */
	@RequestMapping(value="/storehouse/delStorehouse")
	@ResponseBody
	public Map<String,Object> delStorehouse(LlxxDto llxxDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		llxxDto.setScry(user.getYhid());
		boolean isSuccess = false;
		try {
			isSuccess = llxxService.deleteLlxxDto(llxxDto);
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?(xxglService.getModelById("ICOM00003").getXxnr()):(xxglService.getModelById("ICOM00004").getXxnr()));
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
		}
		return map;
	}

	/**
	 * @Description: 领料列表查看
	 * @param llxxDto
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2025/8/20 13:44
	 */
	@RequestMapping(value = "/storehouseLl/viewStorehouseLl")
	@ResponseBody
	public ModelAndView viewStorehouseLl(LlxxDto llxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/stock/ll_view");
		llxxDto = llxxService.getDtoById(llxxDto.getLlid());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("llxxDto",llxxDto);
		return mav;
	}

	/**
	 * @Description: 库存列表查看
	 * @param hwxxDto
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2025/8/20 13:49
	 */
	@RequestMapping(value = "/storehouse/viewStorehouse")
	@ResponseBody
	public ModelAndView viewStorehouse(HwxxDto hwxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/stock/storehouse_view");
		hwxxDto = hwxxService.getDtoById(hwxxDto.getHwid());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("hwxxDto",hwxxDto);
		return mav;
	}
}
