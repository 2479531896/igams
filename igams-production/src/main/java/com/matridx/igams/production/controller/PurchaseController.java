package com.matridx.igams.production.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.DepartmentDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.post.IShxxDao;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.DdAuditTypeEnum;
import com.matridx.igams.common.enums.SsdwTableEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.HtglDto;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.dao.entities.QgqxglDto;
import com.matridx.igams.production.dao.entities.QgqxmxDto;
import com.matridx.igams.production.service.svcinterface.IHtmxService;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.production.service.svcinterface.IQgqxglService;
import com.matridx.igams.production.service.svcinterface.IQgqxmxService;
import com.matridx.igams.storehouse.dao.entities.CkhwxxDto;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.entities.LlcglDto;
import com.matridx.igams.storehouse.dao.entities.XzrkglDto;
import com.matridx.igams.storehouse.dao.entities.XzrkmxDto;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.ILlcglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzrkglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzrkmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/purchase")
public class PurchaseController extends BaseController {

	@Autowired
	private IQgglService qgglService;

	@Autowired
	private IJcsjService jcsjService;

	@Autowired
	private IXxglService xxglService;

	@Autowired
	private IXzrkglService xzrkglService;

	@Autowired
	private IXzrkmxService xzrkmxService;


	@Autowired
	private IFjcfbService fjcfbService;

	@Autowired
	private IQgmxService qgmxService;

	@Autowired
	private IHtmxService htmxService;

	@Autowired
	private IDdxxglService ddxxglService;

	@Autowired
	private IQgqxglService qgqxglService;

	@Autowired
	private IQgqxmxService qgqxmxService;

	@Autowired
	private ICommonService commonService;


	@Autowired
	private IHwxxService hwxxService;

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private ICkhwxxService ckhwxxService;
	@Autowired
	private ILlcglService llcglService;
	@Autowired
	private IShxxDao shxxDao;
	@Autowired
	private IXtszService xtszService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;

	private final Logger log = LoggerFactory.getLogger(PurchaseController.class);

	@Override
	public String getPrefix() {
		return urlPrefix;
	}

	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;

	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	// 是否发送rabbit标记 1：发送
	@Value("${matridx.rabbit.systemconfigflg:}")
	private String systemconfigflg;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;


	/**
	 * 请购管理列表
	 */
	@RequestMapping("/purchase/pageListPurchase")
	public ModelAndView pageListPurchase(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_List");
		qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.PAYMENT_TYPE,
						BasicDataTypeEnum.PAYER, BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING, BasicDataTypeEnum.PURCHASE_SUB_TYPE});
		mav.addObject("zffsList", jclist.get(BasicDataTypeEnum.PAYMENT_TYPE.getCode()));
		mav.addObject("fkfList", jclist.get(BasicDataTypeEnum.PAYER.getCode()));
		mav.addObject("xmdlList", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("qglbList", jclist.get(BasicDataTypeEnum.PURCHASE_SUB_TYPE.getCode()));
		return mav;
	}

	/**
	 * 获取请购列表数据
	 */
	@RequestMapping("/purchase/pageGetListPurchase")
	@ResponseBody
	public Map<String, Object> pageGetListPurchase(QgglDto qgglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		DataPermission.addCurrentUser(qgglDto, getLoginInfo(request));
		DataPermission.addJsDdw(qgglDto, "qggl", SsdwTableEnum.QGGL);
		List<QgglDto> list = qgglService.getPagedList(qgglDto);
		map.put("total", qgglDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 请购管理列表(无权限限制)
	 */
	@RequestMapping("/purchase/pageListAllPurchase")
	public ModelAndView pageListAllPurchase(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_AllList");
		qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.PAYMENT_TYPE,
						BasicDataTypeEnum.PAYER, BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING, BasicDataTypeEnum.PURCHASE_SUB_TYPE});
		mav.addObject("zffsList", jclist.get(BasicDataTypeEnum.PAYMENT_TYPE.getCode()));
		mav.addObject("fkfList", jclist.get(BasicDataTypeEnum.PAYER.getCode()));
		mav.addObject("xmdlList", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("qglbList", jclist.get(BasicDataTypeEnum.PURCHASE_SUB_TYPE.getCode()));
		Map<String,Object> map = qgglService.queryOverTime("");
		String overString = "0个";
		String overdueString = "0个";
		String postStart = "";
		String postEnd = "";
		String overTime = "";
		String overdueTime = "";
		if(!CollectionUtils.isEmpty(map)){
			List<QgglDto> overList = (List<QgglDto>) map.get("overList");
			List<QgglDto> overdueList = (List<QgglDto>) map.get("overdueList");
			if(!CollectionUtils.isEmpty(overList)){
				overString = overList.size()+"个";
			}
			if(!CollectionUtils.isEmpty(overdueList)){
				overdueString = overdueList.size()+"个";
			}
			if(map.get("postStart")!=null){
				postStart = map.get("postStart").toString();
			}
			if(map.get("postEnd")!=null){
				postEnd = map.get("postEnd").toString();
			}
			if(map.get("overTime")!=null){
				overTime = map.get("overTime").toString();
			}
			if(map.get("overdueTime")!=null){
				overdueTime = map.get("overdueTime").toString();
			}
		}
		mav.addObject("overString",overString);
		mav.addObject("overdueString",overdueString);
		mav.addObject("postStart",postStart);
		mav.addObject("postEnd",postEnd);
		mav.addObject("overTime",overTime);
		mav.addObject("overdueTime",overdueTime);
		return mav;
	}

	/**
	 * 获取请购列表数据
	 */
	@RequestMapping("/purchase/pageGetListAllPurchase")
	@ResponseBody
	public Map<String, Object> pageGetListAllPurchase(QgglDto qgglDto) {
		Map<String, Object> map = new HashMap<>();
		if ("1".equals(qgglDto.getWcbj())) {
			qgglDto.setQglbdm("MATERIAL");
		} else if ("2".equals(qgglDto.getWcbj())) {
			qgglDto.setQglbdm("ADMINISTRATION");
			qgglDto.setWcbj(null);
		}
		List<QgglDto> lists = qgglService.getPagedList(qgglDto);
		map.put("total", qgglDto.getTotalNumber());
		map.put("rows", lists);
		return map;
	}
	/**
	 * 获取请购明细数据
	 */
	@RequestMapping("/purchase/pagedataChooseListPurchaseMx")
	@ResponseBody
	public Map<String, Object> pageGetListChooseListPurchase(QgmxDto qgmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<QgmxDto> lists = qgmxService.getPagedDetails(qgmxDto);
		map.put("total", qgmxDto.getTotalNumber());
		map.put("rows", lists);
		return map;
	}

	/**
	 * 新增请购信息
	 */
	@RequestMapping("/purchase/addPurchase")
	public ModelAndView addPurchase(QgglDto qgglDto, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("production/materiel/mater_shopping");
		JcsjDto qglb = new JcsjDto();
		if (StringUtils.isNotBlank(qgglDto.getQglb())) {
			qglb = jcsjService.getDtoById(qgglDto.getQglb());
			qgglDto.setQglbdm(qglb.getCsdm());
			qgglDto.setQglbmc(qglb.getCsmc());
			//判断是否是OA采购，是，提交至OA采购审核流程
			if ("1".equals(qgglDto.getQglx())) {
				qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONSTWO.getCode());
			} else {
				if ("ADMINISTRATION".equals(qglb.getCsdm())) {
					qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_ADMINISTRATION.getCode());
				} else if ("DEVICE".equals(qglb.getCsdm())) {
					qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_DEVICE.getCode());
				} else if ("SERVICE".equals(qglb.getCsdm())) {
					qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_SERVICE.getCode());
				} else {
					qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
				}
			}
		} else {
			qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		}
		User user = getLoginInfo(request);
		qgglDto.setSqr(user.getYhid());// 默认申请人
		QgglDto mrqgglDto = qgglService.getMrsqrxxByYhid(qgglDto);// 获取默认申请人信息
		if (mrqgglDto != null) {
			mrqgglDto.setCskz1(qglb.getCskz1());
			qgglDto.setSqrmc(mrqgglDto.getSqrmc());
			qgglDto.setSqr(mrqgglDto.getSqr());
			qgglDto.setSqbm(mrqgglDto.getSqbm());
			qgglDto.setSqbmmc(mrqgglDto.getSqbmmc());
			qgglDto.setSqbmdm(mrqgglDto.getSqbmdm());
			// 自动生成单据号和记录编号
			if (StringUtil.isNotBlank(mrqgglDto.getJgdh())) {
				String djh = qgglService.generateDjh(mrqgglDto);
				qgglDto.setDjh(djh);
				qgglDto.setJlbh(djh);
			}
		}
		qgglDto.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		if (StringUtil.isBlank(qgglDto.getSqrq())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
			Date date = new Date();
			qgglDto.setSqrq(sdf.format(date));
		}
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{
				BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING, BasicDataTypeEnum.PURCHASE_ITEMENCODING, BasicDataTypeEnum.RESEARCH_ITEM});
		mav.addObject("xmbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode()));
		mav.addObject("xmdlbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		mav.addObject("ssyfxmlist", jclist.get(BasicDataTypeEnum.RESEARCH_ITEM.getCode()));
		mav.addObject("qgglDto", qgglDto);
		if ("shoppingSaveMater".equals(qgglDto.getLjbj())){
			mav.addObject("formAction", "/production/purchase/shoppingSaveMater");
		}else if ("systemSavePurchase".equals(qgglDto.getLjbj())){
			mav.addObject("formAction", "/production/purchase/systemSavePurchase");
		}else {
			mav.addObject("formAction", "/production/purchase/pagedataAddSavePurchase");
		}
		mav.addObject("url", "/production/materiel/pagedataShoppinglist");
		mav.addObject("flag", "qgc");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	@RequestMapping("/purchase/pagedataAddPurchase")
	public ModelAndView pagedataAddPurchase(QgglDto qgglDto, HttpServletRequest request) {
		return this.addPurchase(qgglDto,request);
	}
	/**
	 * 提交请购信息
	 */
	@RequestMapping("/purchase/submitPurchase")
	public ModelAndView modPurchase(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("production/materiel/mater_shopping");
		QgglDto qgglDto_t = qgglService.getDtoById(qgglDto.getQgid());
		if (StringUtils.isNotBlank(qgglDto_t.getQglbdm())) {
			if ("1".equals(qgglDto_t.getQglx())) {
				qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONSTWO.getCode());
			} else {
				if ("ADMINISTRATION".equals(qgglDto_t.getQglbdm())) {
					qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_ADMINISTRATION.getCode());
				} else if ("DEVICE".equals(qgglDto_t.getQglbdm())) {
					qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_DEVICE.getCode());
				} else if ("SERVICE".equals(qgglDto_t.getQglbdm())) {
					qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_SERVICE.getCode());
				} else {
					qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
				}
			}
		} else {
			qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		}
		qgglDto_t.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		// 查询附件信息 -采购单
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(qgglDto_t.getYwlx());
		fjcfbDto.setYwid(qgglDto.getQgid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		// 查询附件信息 - 明细
		mav.addObject("qgglDto", qgglDto_t);
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING, BasicDataTypeEnum.RESEARCH_ITEM});
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(qgglDto_t.getXmdl());
		List<JcsjDto> zlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		mav.addObject("xmbmlist", zlbList);
		mav.addObject("ssyfxmlist", jclist.get(BasicDataTypeEnum.RESEARCH_ITEM.getCode()));
		mav.addObject("xmdlbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		mav.addObject("formAction", "/purchase/purchase/pagedataSavePurchase");
		mav.addObject("url", "/purchase/purchase/pagedataListQgmx?qgid=" + qgglDto.getQgid());
		mav.addObject("flag", "qgmx");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 请购审核信息
	 */
	@RequestMapping("/purchase/auditPurchase")
	public ModelAndView auditPurchase(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("production/materiel/mater_shopping");
		QgglDto qgglDto_t = qgglService.getDtoById(qgglDto.getQgid());
		if (StringUtils.isNotBlank(qgglDto_t.getQglbdm())) {
			if ("1".equals(qgglDto_t.getQglx())) {
				qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONSTWO.getCode());
			} else {
				if ("ADMINISTRATION".equals(qgglDto_t.getQglbdm())) {
					qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_ADMINISTRATION.getCode());
				} else if ("DEVICE".equals(qgglDto_t.getQglbdm())) {
					qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_DEVICE.getCode());
				} else if ("SERVICE".equals(qgglDto_t.getQglbdm())) {
					qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_SERVICE.getCode());
				} else {
					qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
				}
			}
		} else {
			qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		}
		qgglDto_t.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		// 查询附件信息 -采购单
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(qgglDto_t.getYwlx());
		fjcfbDto.setYwid(qgglDto.getQgid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		// 查询附件信息 - 明细
		mav.addObject("qgglDto", qgglDto_t);
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING, BasicDataTypeEnum.RESEARCH_ITEM});
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(qgglDto_t.getXmdl());
		List<JcsjDto> zlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		mav.addObject("xmbmlist", zlbList);
		mav.addObject("ssyfxmlist", jclist.get(BasicDataTypeEnum.RESEARCH_ITEM.getCode()));
		mav.addObject("xmdlbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		mav.addObject("formAction", "/purchase/purchase/pagedataSavePurchase");
		mav.addObject("url", "/purchase/purchase/pagedataListQgmx?qgid=" + qgglDto.getQgid());
		mav.addObject("flag", "qgmx");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 高级修改请购信息
	 */
	@RequestMapping("/purchase/advancedmodPurchase")
	public ModelAndView advancedmodPurchase(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("production/materiel/mater_shopping");
		QgglDto qgglDto_t = qgglService.getDtoById(qgglDto.getQgid());
		qgglDto_t.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		mav.addObject("qgglDto", qgglDto_t);
		// 查询附件信息 -采购单
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(qgglDto_t.getYwlx());
		fjcfbDto.setYwid(qgglDto.getQgid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		// 查询基础数据信息
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(qgglDto_t.getXmdl());
		List<JcsjDto> zlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		mav.addObject("xmbmlist", zlbList);
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,
						BasicDataTypeEnum.PAYMENT_TYPE, BasicDataTypeEnum.PAYER, BasicDataTypeEnum.RESEARCH_ITEM});
		mav.addObject("xmdlbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		mav.addObject("ssyfxmlist", jclist.get(BasicDataTypeEnum.RESEARCH_ITEM.getCode()));
		mav.addObject("zffslist", jclist.get(BasicDataTypeEnum.PAYMENT_TYPE.getCode()));
		mav.addObject("fkflist", jclist.get(BasicDataTypeEnum.PAYER.getCode()));
		mav.addObject("flag", "gjxg");
		mav.addObject("CCflag", "0");
		mav.addObject("url", "/purchase/purchase/pagedataListQgmx?qgid=" + qgglDto.getQgid());
		mav.addObject("formAction", "/purchase/purchase/advancedmodSavePurchase");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 高级修改请购信息保存
	 */
	@RequestMapping("/purchase/advancedmodSavePurchase")
	@ResponseBody
	public Map<String, Object> advancedModSavePurchase(QgglDto qgglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		qgglDto.setXgry(user.getYhid());
		qgglDto.setFlg("0");
		int count = qgglService.getCount(qgglDto);
		if (count > 0) {
			map.put("status", "fail");
			map.put("message", "采购单号不允许重复!");
		} else {
			List<QgmxDto> qgmxlist = JSON.parseArray(qgglDto.getQgmx_json(), QgmxDto.class);
			qgglDto.setQgmxlist(qgmxlist);
			if (!CollectionUtils.isEmpty(qgmxlist)) {
				List<String> errorwlbm = new ArrayList<>();
				for (QgmxDto qgmxDto : qgmxlist) {
					if (!CollectionUtils.isEmpty(qgmxDto.getFjids())) {
						if (StringUtils.isBlank(qgmxDto.getWlzlbtc()) && "1".equals(qgmxDto.getCskz2())) {
							errorwlbm.add(qgmxDto.getWlbm());
						}
					}
					if ("1".equals(qgmxDto.getCskz1())
							&& (StringUtils.isBlank(qgmxDto.getFjbj()) || !"0".equals(qgmxDto.getFjbj()))
					) {
						map.put("status", "fail");
						map.put("message", "物料编码为" + qgmxDto.getWlbm() + "的物料需上传图纸!");
						return map;
					}
				}
				if (!CollectionUtils.isEmpty(errorwlbm) && !"1".equals(qgglDto.getBcbj())) {
					StringBuilder str = new StringBuilder();
					for (String s : errorwlbm) {
						str.append(",").append(s);
					}
					str = new StringBuilder(str.substring(1));
					map.put("status", "fail");
					map.put("message", "物料编码为" + str + "的物料子类别统称不能为空，请先完善信息!");
				} else {
					boolean isSuccess;
					try {
						isSuccess = qgglService.advancedModPurchase(qgglDto, qgmxlist);
						if ("1".equals(systemconfigflg) && isSuccess) {
							qgglDto.setPrefixFlg(prefixFlg);
							qgglDto.setQgmxlist(qgmxlist);
							amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qggl.advancedUpdate",
									JSONObject.toJSONString(qgglDto));
						}
						map.put("status", isSuccess ? "success" : "fail");
						map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
								: xxglService.getModelById("ICOM00002").getXxnr());
					} catch (BusinessException e) {
						map.put("status", "fail");
						map.put("message", e.getMsg());
						return map;
					} catch (Exception e) {
						log.error(e.toString());
						map.put("status", "fail");
						map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
						return map;
					}
				}
			} else {
				map.put("status", "fail");
				map.put("message", "请购明细不允许为空!");
			}
		}

		return map;
	}

	/**
	 * 采购明细列表
	 */
	@RequestMapping("/purchase/pageListPurchaseInfo")
	public ModelAndView pageListPurchaseInfo() {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchaseInfo_List");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 采购明细数据
	 */
	@RequestMapping("/purchase/pagedataPurchaseInfo")
	@ResponseBody
	public Map<String, Object> getListPurchaseInfo(QgmxDto qgmxDto,QgglDto qgglDto) {
		Map<String, Object> map = new HashMap<>();
		QgglDto qgglDto_t = qgglService.getDto(qgglDto);
		// 查询附件信息 -采购单
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(qgglDto_t.getYwlx());
		fjcfbDto.setYwid(qgglDto.getQgid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		// 查看合同明细
		HtmxDto htmxDto = new HtmxDto();
		htmxDto.setQgid(qgglDto.getQgid());
		List<HtmxDto> htmxDtos = htmxService.getDtoList(htmxDto);
		// 请购取消明细
		QgqxmxDto qgqxmxDto = new QgqxmxDto();
		qgqxmxDto.setQgid(qgglDto.getQgid());
		List<QgqxmxDto> qgqxmxDtos = qgqxmxService.getDtoList(qgqxmxDto);
		map.put("htmxDtos", htmxDtos);
		map.put("fjcfbDtos", fjcfbDtos);
		map.put("qgglDto", qgglDto_t);
		map.put("urlPrefix", urlPrefix);
		map.put("qgqxmxDtos", qgqxmxDtos);
		List<QgmxDto> list = qgmxService.getPagedDtoList(qgmxDto);
		map.put("rows", list);
		map.put("total", qgmxDto.getTotalNumber());
		return map;
	}

	/**
	 * 采购明细数据(不分页)
	 */
	@RequestMapping("/purchase/pagedataListQgmx")
	@ResponseBody
	public Map<String, Object> getQgmxList(QgmxDto qgmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<QgmxDto> list = qgmxService.getQgmxList(qgmxDto);
		map.put("rows", list);
		return map;
	}

	/**
	 * 取消请购页面明细数据
	 */
	@RequestMapping("/purchase/pagedataQxqgmxList")
	@ResponseBody
	public Map<String, Object> getQxqgmxList(QgmxDto qgmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<QgmxDto> list = qgmxService.getQxqgmxList(qgmxDto);
		map.put("rows", list);
		return map;
	}

	/**
	 * 修改请购单
	 */
	@RequestMapping("/purchase/pagedataSavePurchase")
	@ResponseBody
	public Map<String, Object> updatePurchase(QgglDto qgglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		qgglDto.setXgry(user.getYhid());
		qgglDto.setFlg("0");
		try {
			int count = qgglService.getCount(qgglDto);
			if (count > 0) {
				map.put("status", "fail");
				map.put("message", "采购单号不允许重复!");
				return map;
			}
			List<QgmxDto> qgmxlist = JSON.parseArray(qgglDto.getQgmx_json(), QgmxDto.class);
			if (CollectionUtils.isEmpty(qgmxlist)){
				map.put("status", "fail");
				map.put("message", "请购明细不允许为空!");
				return map;
			}
			qgglDto.setQgmxlist(qgmxlist);
			List<String> errorwlbm = new ArrayList<>();
			List<String> errorwlbmTz = new ArrayList<>();
			List<String> qgmxids = new ArrayList<>();
			for (QgmxDto qgmxDto : qgmxlist) {
				qgmxDto.setLrry(user.getYhid());
				qgmxDto.setQgid(qgglDto.getQgid());
				qgmxDto.setSysl(qgmxDto.getSl());
				qgmxDto.setYssl(qgmxDto.getSl());
				if (!CollectionUtils.isEmpty(qgmxDto.getFjids())) {
					if (StringUtils.isBlank(qgmxDto.getWlzlbtc()) && "1".equals(qgmxDto.getCskz2())) {
						errorwlbm.add(qgmxDto.getWlbm());
					}
				}
				//修改时fjids默认是没有的 所以通过fjbj去校验图纸
				if ("1".equals(qgmxDto.getCskz1()) && (StringUtils.isBlank(qgmxDto.getFjbj()) || !"0".equals(qgmxDto.getFjbj()))) {
					errorwlbmTz.add(qgmxDto.getWlbm());
				}
				if ("1".equals(qgmxDto.getCskz1())&&StringUtil.isNotBlank(qgmxDto.getQgmxid())){
					qgmxids.add(qgmxDto.getQgmxid());
				}
			}
			if (!CollectionUtils.isEmpty(qgmxids)){
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwids(qgmxids);
				fjcfbDto.setYwid(qgglDto.getQgid());
				List<FjcfbDto> fjcfbDtos = fjcfbService.existFileInfo(fjcfbDto);
				for (FjcfbDto dto : fjcfbDtos) {
					//数据库不存在附件
					if ("0".equals(dto.getSfcz())){
						for (QgmxDto qgmxDto : qgmxlist) {
							//当前上传的也不存在附件
							if (dto.getZywid().equals(qgmxDto.getQgmxid())&&"1".equals(qgmxDto.getCskz1())&&CollectionUtils.isEmpty(qgmxDto.getFjids())&&!errorwlbmTz.contains(qgmxDto.getWlbm())){
								errorwlbmTz.add(qgmxDto.getWlbm());
								break;
							}
						}
					}
				}
			}
			if (!CollectionUtils.isEmpty(errorwlbmTz)&& !"1".equals(qgglDto.getBcbj())){
				map.put("status", "fail");
				map.put("message", "物料编码为"+ StringUtil.join(errorwlbmTz,",") +"的物料需上传图纸!");
				return map;
			}
			if (!CollectionUtils.isEmpty(errorwlbm) && !"1".equals(qgglDto.getBcbj())) {
				map.put("status", "fail");
				map.put("message", "物料编码为" + StringUtil.join(errorwlbm,",") + "的物料子类别统称不能为空，请先完善信息!");
				return map;
			}
			boolean isSuccess = qgglService.updateProdution(qgglDto, qgmxlist);
			if ("1".equals(systemconfigflg) && isSuccess) {
				qgglDto.setPrefixFlg(prefixFlg);
				qgglDto.setQgmxlist(qgmxlist);
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qggl.update",
						JSONObject.toJSONString(qgglDto));
			}
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
		} catch (Exception e) {
			log.error(e.getMessage());
			// TODO: handle exception
			map.put("status", "fail");
			map.put("message", e.getMessage());
			return map;
		}
		return map;
	}

	/**
	 * 查看请购信息
	 */
	@RequestMapping("/purchase/viewPurchase")
	public ModelAndView viewPurchase(HttpServletRequest request,QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_View");
		QgglDto qgglDto_t = qgglService.getDto(qgglDto);
		// 查询附件信息 -采购单
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(qgglDto_t.getYwlx());
		fjcfbDto.setYwid(qgglDto.getQgid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		// 查看合同明细
		HtmxDto htmxDto = new HtmxDto();
		htmxDto.setQgid(qgglDto.getQgid());
		List<HtmxDto> htmxDtos = htmxService.getDtoList(htmxDto);
		// 请购取消明细
		QgqxmxDto qgqxmxDto = new QgqxmxDto();
		qgqxmxDto.setQgid(qgglDto.getQgid());
		List<QgqxmxDto> qgqxmxDtos = qgqxmxService.getDtoList(qgqxmxDto);
		User user = getLoginInfo(request);
		LlcglDto llcglDto = new LlcglDto();
		llcglDto.setRyid(user.getYhid());
		List<LlcglDto> llcglDtos = llcglService.getDtoList(llcglDto);
		StringBuilder ids= new StringBuilder();
		if(!CollectionUtils.isEmpty(llcglDtos)) {
			for (LlcglDto llcglDto_t : llcglDtos) {
				ids.append(",").append(llcglDto_t.getCkhwid());
			}
		}
		mav.addObject("idswl", ids.toString());
		mav.addObject("htmxDtos", htmxDtos);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		mav.addObject("qgglDto", qgglDto_t);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("qgqxmxDtos", qgqxmxDtos);
		return mav;
	}



//	/**
//	 * 行政请购入库按钮
//	 *
//	 * @param qgglDto
//	 * @return
//	 */
//	@RequestMapping("/purchase/instorePurchase")
//	public ModelAndView instorePurchase(QgglDto qgglDto, HttpServletRequest request) {
//		ModelAndView mav = new ModelAndView("production/materiel/mater_instore");
//		XzrkglDto xzrkglDto = new XzrkglDto();
//		User user = getLoginInfo(request);
//		xzrkglDto.setRkry(user.getYhid());
//		xzrkglDto.setRkrymc(user.getZsxm());
//		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//		Date date = new Date();
//		xzrkglDto.setRkrq(simpleDateFormat.format(date));
//		xzrkglDto.setRkdh(xzrkglService.generateDjh(xzrkglDto));
//		String ids ="";
//		if(qgglDto.getIds().size()>0) {
//			for (String str : qgglDto.getIds()) {
//				if (StringUtil.isNotBlank(ids)){
//					ids += ","+str;
//				}else{
//					ids= str;
//				}
//			}
//		}
//		List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
//		CkxxDto ckxxDto = new CkxxDto();
//		CkxxDto kwxxDto = new CkxxDto();
//		for (JcsjDto jcsjDto:jcsjDtos) {
//			if ("CK".equals(jcsjDto.getCsdm())){
//				ckxxDto.setCklb(jcsjDto.getCsid());
//			}else if("KW".equals(jcsjDto.getCsdm())){
//				kwxxDto.setCklb(jcsjDto.getCsid());
//			}
//		}
//		List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
//		List<CkxxDto> kwxxDtos = ckxxService.getDtoList(kwxxDto);
//		mav.addObject("formAction", "/purchase/purchase/saveInstorePurchase");
//		mav.addObject("url", "/purchase/purchase/getRKmxList");
//		mav.addObject("cklist",JSONObject.toJSONString(ckxxDtos));
//		mav.addObject("kwlist",JSONObject.toJSONString(kwxxDtos));
//		mav.addObject("urlPrefix", urlPrefix);
//		mav.addObject("ids",ids);
//		mav.addObject("xzrkglDto", xzrkglDto);
//		mav.addObject("auditType", AuditTypeEnum.AUDIT_INSTORE.getCode());
//		return mav;
//	}

	/**
	 * 刷新入库单号
	 */
	@RequestMapping("/purchase/sxrkdh")
	@ResponseBody
	public Map<String, Object> sxrkdh(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		XzrkglDto xzrkglDto = new XzrkglDto();
		User user = getLoginInfo(request);
		xzrkglDto.setRkry(user.getYhid());
		xzrkglDto.setRkrymc(user.getZsxm());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		xzrkglDto.setRkrq(simpleDateFormat.format(date));
		map.put("rkdh", xzrkglService.generateDjh(xzrkglDto));
		return map;
	}

	/**
	 * 采购明细数据(不分页)
	 */
	@RequestMapping("/purchase/getRKmxList")
	@ResponseBody
	public Map<String, Object> getRKmxList(QgmxDto qgmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<QgmxDto> list = qgmxService.getDtoList(qgmxDto);
		if (!CollectionUtils.isEmpty(list)) {
			for (int i = list.size() - 1; i >= 0; i--) {
				XzrkmxDto xzrkmxDto = xzrkmxService.getrksl(list.get(i).getQgmxid());
				if (null != xzrkmxDto && StringUtil.isNotBlank(xzrkmxDto.getRksl())) {
					if (Double.parseDouble(xzrkmxDto.getRksl()) >= Double.parseDouble(list.get(i).getSl())) {
						list.remove(i);
					} else {
						list.get(i).setSl(String.valueOf(Double.parseDouble(list.get(i).getSl()) - Double.parseDouble(xzrkmxDto.getRksl())));
					}
				}
			}
		}

		map.put("rows", list);
		return map;
	}

//	/**
//	 * 行政入库保存
//	 *
//	 * @param
//	 * @return
//	 */
//	@RequestMapping("/purchase/saveInstorePurchase")
//	@ResponseBody
//	public Map<String, Object> saveInstorePurchase(XzrkglDto xzrkglDto, HttpServletRequest request) {
//		Map<String, Object> map = new HashMap<>();
//		User user=getLoginInfo(request);
//		xzrkglDto.setLrry(user.getYhid());
//		boolean isSuccess = true;
//		//判断领料单号是否重复
//		isSuccess = xzrkglService.getDtoByRkdh(xzrkglDto) == null;
//		if(!isSuccess) {
//			map.put("status", "fail");
//			map.put("message", "入库单号不允许重复！");
//			return map;
//		}
//		//新增保存
//		try {
//			if(StringUtils.isBlank(xzrkglDto.getXzrkid())) {
//				xzrkglDto.setXzrkid(StringUtil.generateUUID());
//			}
//			isSuccess = xzrkglService.addSaveInStore(xzrkglDto);
//			map.put("status", isSuccess ? "success" : "fail");
//			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
//					: xxglService.getModelById("ICOM00002").getXxnr());
//			map.put("auditType",AuditTypeEnum.AUDIT_GOODS_APPLY.getCode());
//			map.put("ywid", xzrkglDto.getXzrkid());
//		} catch (BusinessException e) {
//			map.put("status", "fail");
//			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
//		}
//		return map;
//	}


	/**
	 * 废弃请购信息
	 */
	@RequestMapping("/purchase/discardPurchase")
	@ResponseBody
	public Map<String, Object> discardPurchase(QgglDto qgglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			// 获取用户信息
			User user = getLoginInfo(request);
			qgglDto.setScry(user.getYhid());
			boolean isSuccess = qgglService.deleteQggl(qgglDto);
			if ("1".equals(systemconfigflg) && isSuccess) {
				qgglDto.setPrefixFlg(prefixFlg);
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qggl.del",
						JSONObject.toJSONString(qgglDto));
			}
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr()
					: xxglService.getModelById("ICOM00004").getXxnr());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			map.put("status", "fail");
			map.put("message",
					StringUtil.isNotBlank(e.getMsg()) ? e.getMsg() : xxglService.getModelById("ICOM00004").getXxnr());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
		}
		return map;
	}
	/**
	 * 删除请购信息
	 */
	@RequestMapping("/purchase/delPurchase")
	@ResponseBody
	public Map<String, Object> delPurchase(QgglDto qgglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			// 获取用户信息
			User user = getLoginInfo(request);
			qgglDto.setScry(user.getYhid());
			boolean isSuccess = qgglService.deleteQggl(qgglDto);
			if ("1".equals(systemconfigflg) && isSuccess) {
				qgglDto.setPrefixFlg(prefixFlg);
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qggl.del",
						JSONObject.toJSONString(qgglDto));
			}
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr()
					: xxglService.getModelById("ICOM00004").getXxnr());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			map.put("status", "fail");
			map.put("message",
					StringUtil.isNotBlank(e.getMsg()) ? e.getMsg() : xxglService.getModelById("ICOM00004").getXxnr());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
		}
		return map;
	}



	/**
	 * 复制请购信息
	 */
	@RequestMapping("/purchase/copyPurchase")
	public ModelAndView copyPurchase(QgglDto qgglDto, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("production/materiel/mater_shopping");
		QgglDto qgglDto_t = qgglService.getDtoById(qgglDto.getQgid());
		qgglDto_t.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		if (StringUtils.isNotBlank(qgglDto_t.getQglbdm())) {
			if ("ADMINISTRATION".equals(qgglDto_t.getQglbdm())) {
				qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_ADMINISTRATION.getCode());
			} else if ("DEVICE".equals(qgglDto_t.getQglbdm())) {
				qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_DEVICE.getCode());
			} else if ("SERVICE".equals(qgglDto_t.getQglbdm())) {
				qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_SERVICE.getCode());
			} else {
				qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
			}
		} else {
			qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		}

		// 查询附件信息 -采购单
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(qgglDto_t.getYwlx());
		fjcfbDto.setYwid(qgglDto.getQgid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		User user = getLoginInfo(request);
		qgglDto.setSqr(user.getYhid());// 默认申请人
		QgglDto mrqgglDto = qgglService.getMrsqrxxByYhid(qgglDto);// 获取默认申请人信息
		if (mrqgglDto != null) {
			mrqgglDto.setCskz1(qgglDto_t.getCskz1());
			// 自动生成单据号和记录编号
			if (StringUtil.isNotBlank(mrqgglDto.getJgdh())) {
				String djh = qgglService.generateDjh(mrqgglDto);
				qgglDto_t.setDjh(djh);
				qgglDto_t.setJlbh(djh);
			}
		}
		// 复制时单据号不显示申请日期改为默认今天
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String sqrq = sdf.format(new Date());
		qgglDto_t.setSqrq(sqrq);
		qgglDto_t.setZt(StatusEnum.CHECK_NO.getCode());
		qgglDto_t.setQgid(null);
		mav.addObject("qgglDto", qgglDto_t);
		// 查询附件信息 - 明细
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING, BasicDataTypeEnum.RESEARCH_ITEM});
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(qgglDto_t.getXmdl());
		List<JcsjDto> zlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		mav.addObject("xmbmlist", zlbList);
		mav.addObject("xmdlbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		mav.addObject("ssyfxmlist", jclist.get(BasicDataTypeEnum.RESEARCH_ITEM.getCode()));
		mav.addObject("formAction", "/purchase/purchase/copySavePurchase");
		mav.addObject("url", "/purchase/purchase/pagedataListQgmx?qgid=" + qgglDto.getQgid());
		mav.addObject("flag", "qgmx");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("requestName", "copy");
		return mav;
	}

	/**
	 * 保存采购单
	 */
	@RequestMapping("/purchase/copySavePurchase")
	@ResponseBody
	public Map<String, Object> addSaveProdution(QgglDto qgglDto, HttpServletRequest request) throws BusinessException {
		User user = getLoginInfo(request);
		qgglDto.setLrry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		qgglDto.setFlg("1");
		int count = qgglService.getCount(qgglDto);
		if (count > 0) {
			map.put("status", "fail");
			map.put("message", "采购单号不允许重复!");
		} else {
			List<QgmxDto> qgmxlist = JSON.parseArray(qgglDto.getQgmx_json(), QgmxDto.class);
			qgglDto.setQgmxlist(qgmxlist);
			if (!CollectionUtils.isEmpty(qgmxlist)) {
				List<String> errorwlbm = new ArrayList<>();
				for (QgmxDto qgmxDto : qgmxlist) {
					if (!CollectionUtils.isEmpty(qgmxDto.getFjids())) {
						if (StringUtils.isBlank(qgmxDto.getWlzlbtc()) && "1".equals(qgmxDto.getCskz2())) {
							errorwlbm.add(qgmxDto.getWlbm());
						}
					}
					if ("1".equals(qgmxDto.getCskz1())
							&& CollectionUtils.isEmpty(qgmxDto.getFjids())) {
						map.put("status", "fail");
						map.put("message", "物料编码为" + qgmxDto.getWlbm() + "的物料需上传图纸!");
						return map;
					}
				}
				if (!CollectionUtils.isEmpty(errorwlbm) && !"1".equals(qgglDto.getBcbj())) {
					StringBuilder str = new StringBuilder();
					for (String s : errorwlbm) {
						str.append(",").append(s);
					}
					str = new StringBuilder(str.substring(1));
					map.put("status", "fail");
					map.put("message", "物料编码为" + str + "的物料子类别统称不能为空，请先完善信息，可先保存请购单信息!");
				} else {
					qgglDto.setQgid("");
					boolean isSuccess = qgglService.addSaveProdution(qgglDto);
					if ("1".equals(systemconfigflg) && isSuccess) {
						qgglDto.setPrefixFlg(prefixFlg);
						amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qggl.insert",
								JSONObject.toJSONString(qgglDto));
					}
					map.put("status", isSuccess ? "success" : "fail");
					map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
							: xxglService.getModelById("ICOM00002").getXxnr());
					map.put("auditType", AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
					map.put("ywid", qgglDto.getQgid());
				}
			} else {
				map.put("status", "fail");
				map.put("message", "请购明细不允许为空!");
			}
		}
		return map;
	}

	/**
	 * 请购审核列表
	 */
	@RequestMapping("/purchase/pageListAuditPurchase")
	public ModelAndView pageListAuditPurchase() {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_auditList");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 请购审核列表(物料，行政，设备，服务)
	 */
	@RequestMapping("/purchase/pageGetListAuditPurchase")
	@ResponseBody
	public Map<String, Object> pageGetListAuditPurchase(QgglDto qgglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 附加委托参数
		DataPermission.addWtParam(qgglDto);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(qgglDto.getDqshzt())) {
			List<AuditTypeEnum> auditTypes = new ArrayList<>();
			auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS);
			auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS_DEVICE);
			auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS_SERVICE);
			auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS_ADMINISTRATION);
			auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONSTWO);
			DataPermission._add(qgglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "qggl", "qgid",
					auditTypes, null);
		} else {
			List<AuditTypeEnum> auditTypes = new ArrayList<>();
			auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS);
			auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS_DEVICE);
			auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS_SERVICE);
			auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS_ADMINISTRATION);
			auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONSTWO);
			DataPermission._add(qgglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "qggl", "qgid",
					auditTypes, null);
		}
		DataPermission.addCurrentUser(qgglDto, getLoginInfo(request));
		DataPermission.addSpDdw(qgglDto, "qggl", SsdwTableEnum.QGGL);
		List<QgglDto> listMap = qgglService.getPagedAuditQggl(qgglDto);

		map.put("total", qgglDto.getTotalNumber());
		map.put("rows", listMap);
		return map;
	}

	/**
	 * 请购审核修改页面
	 */
	@RequestMapping("/purchase/commonModAuditPurchase")
	public ModelAndView modAuditPurchase(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("production/materiel/mater_shopping");
		QgglDto qgglDto_t = qgglService.getDtoById(qgglDto.getQgid());
		qgglDto_t.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		mav.addObject("qgglDto", qgglDto_t);
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,
						BasicDataTypeEnum.PAYMENT_TYPE, BasicDataTypeEnum.PAYER, BasicDataTypeEnum.RESEARCH_ITEM,
						BasicDataTypeEnum.RANGE_OF_PRICE});
		// 查询附件信息 -采购单
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(qgglDto_t.getYwlx());
		fjcfbDto.setYwid(qgglDto.getQgid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(qgglDto_t.getXmdl());
		List<JcsjDto> zlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		//获取部门钉钉审核流程
		List<DdxxglDto> jgsplist;
		if ("ADMINISTRATION".equals(qgglDto_t.getQglbdm())) {
			jgsplist = ddxxglService.getDingtalkAuditDep(DdAuditTypeEnum.SP_QG_XZ.getCode());
		} else {
			jgsplist = ddxxglService.getDingtalkAuditDep(DdAuditTypeEnum.SP_QG.getCode());
		}
		mav.addObject("jgsplist", jgsplist);
		mav.addObject("xmbmlist", zlbList);
		mav.addObject("xmdlbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		mav.addObject("ssyfxmlist", jclist.get(BasicDataTypeEnum.RESEARCH_ITEM.getCode()));
		mav.addObject("zffslist", jclist.get(BasicDataTypeEnum.PAYMENT_TYPE.getCode()));
		mav.addObject("fkflist", jclist.get(BasicDataTypeEnum.PAYER.getCode()));
		mav.addObject("jgfwlist", jclist.get(BasicDataTypeEnum.RANGE_OF_PRICE.getCode()));
		mav.addObject("flag", "qxgjxg");
		mav.addObject("url", "/purchase/purchase/pagedataListQgmx?qgid=" + qgglDto.getQgid());
		mav.addObject("formAction", "/purchase/purchase/pagedataSavePurchase");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 查询抄送人员
	 */
	@RequestMapping("/purchase/pagedataSelectCsrs")
	@ResponseBody
	public Map<String, Object> selectCsrs(QgglDto qgglDto) {
		Map<String, Object> map = new HashMap<>();
		if ("ADMINISTRATION".equals(qgglDto.getQglbdm())) {
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb("DINGMESSAGETYPE");
			jcsjDto.setCsdm("PURCHASE_AUDIT_XZ_CC");
			if (jcsjService.getDtoByCsdmAndJclb(jcsjDto) != null) {
				List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
				map.put("ddxxglDtos", ddxxglDtos);
			}
		} else {
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb("DINGMESSAGETYPE");
			jcsjDto.setCsdm("PURCHASE_AUDIT_CC");
			List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
			map.put("ddxxglDtos", ddxxglDtos);
		}
		return map;
	}

	/**
	 * 请购审核查看页面

	 */
	@RequestMapping("/purchase/commonViewAuditPurchase")
	public ModelAndView viewAuditPurchase(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_auditView");
		QgglDto qgglDto_t = qgglService.getDtoById(qgglDto.getQgid());
		QgmxDto qgmxDto = new QgmxDto();
		qgmxDto.setQgid(qgglDto.getQgid());
		List<QgmxDto> qgmxlist = qgmxService.getQgmxList(qgmxDto);
		BigDecimal zjg = new BigDecimal("0");
		DecimalFormat df4 = new DecimalFormat("#,##0.00");
		if (!CollectionUtils.isEmpty(qgmxlist)) {
			for (QgmxDto dto : qgmxlist) {
				String jg = dto.getJg();
				String sl = dto.getSl();
				if (StringUtils.isNotBlank(jg) && StringUtils.isNotBlank(sl)
						&& StatusEnum.CHECK_PASS.getCode().equals(dto.getZt())) {
					zjg = zjg.add(new BigDecimal(jg).multiply(new BigDecimal(sl)));
				}
			}
		}
		zjg = zjg.setScale(2, RoundingMode.UP);
		String t_zjg = df4.format(zjg);// 千位符分隔
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{
				BasicDataTypeEnum.PURCHASE_ITEMENCODING, BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,
				BasicDataTypeEnum.PAYMENT_TYPE, BasicDataTypeEnum.PAYER, BasicDataTypeEnum.RESEARCH_ITEM});
		mav.addObject("xmbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode()));
		mav.addObject("xmdlbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		mav.addObject("ssyfxmlist", jclist.get(BasicDataTypeEnum.RESEARCH_ITEM.getCode()));
		mav.addObject("zffslist", jclist.get(BasicDataTypeEnum.PAYMENT_TYPE.getCode()));
		mav.addObject("fkflist", jclist.get(BasicDataTypeEnum.PAYER.getCode()));
		mav.addObject("qgglDto", qgglDto_t);
		mav.addObject("formAction", "updatePurchase");
		mav.addObject("zjg", t_zjg);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("shlc", "view");
		return mav;
	}

	/**
	 * 下载请购附件界面
	 */
	@RequestMapping("/purchase/downloadPage")
	public ModelAndView downloadPage(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_download");
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.MATERIEL_SUBTYPE_COLLECTIVELY});
		mav.addObject("wlzlbtclist", jclist.get(BasicDataTypeEnum.MATERIEL_SUBTYPE_COLLECTIVELY.getCode()));
		qgglDto.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 请购明细附件下载
	 */
	@RequestMapping("/purchase/pagedataDownloadQgmxFile")
	@ResponseBody
	public Map<String, Object> downloadQgmxFile(QgmxDto qgmxDto) {
		return qgmxService.downloadQgmxFile(qgmxDto);
	}

	/**
	 * 获取请购信息
	 */
	@RequestMapping("/purchase/pagedataGetPurchaseList")
	@ResponseBody
	public Map<String, Object> getPurchaseList(QgglDto qgglDto) {
		Map<String, Object> map = new HashMap<>();
		qgglDto.setScbj("0");
		List<QgglDto> qgglDtos = qgglService.getDtoList(qgglDto);
		map.put("qgglDtos", qgglDtos);
		return map;
	}

	/**
	 * 跳转请购选择列表

	 */
	@RequestMapping(value = "/purchase/pagedataChooseListPurchase")
	public ModelAndView chooseListPurchase(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_chooseList");
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.PAYMENT_TYPE,
						BasicDataTypeEnum.PAYER, BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING, BasicDataTypeEnum.PURCHASE_SUB_TYPE});
		mav.addObject("zffsList", jclist.get(BasicDataTypeEnum.PAYMENT_TYPE.getCode()));
		mav.addObject("fkfList", jclist.get(BasicDataTypeEnum.PAYER.getCode()));
		mav.addObject("xmdlList", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		mav.addObject("qglbList", jclist.get(BasicDataTypeEnum.PURCHASE_SUB_TYPE.getCode()));
		if (StringUtil.isNotBlank(qgglDto.getLllb())) {
			mav.addObject("lllb", qgglDto.getLllb());
		} else {
			mav.addObject("lllb", "0");
		}
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 跳转请购选择列表
	 */
	@RequestMapping(value = "/purchase/pagedataChooseQgd")
	public ModelAndView pagedataChooseQgd(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_chooseQgd");
		List<JcsjDto> qglbList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.PURCHASE_SUB_TYPE.getCode());
		for (int i=0;i<qglbList.size();i++){
			if ("SERVICE".equals(qglbList.get(i).getCsdm())){
				qglbList.remove(qglbList.get(i));
				break;
			}
		}
		mav.addObject("qglbList", qglbList);
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 跳转请购明细列表
	 */
	@RequestMapping(value = "/purchase/pagedataChooseListPurchaseInfo")
	public ModelAndView chooseListPurchaseInfo(QgmxDto qgmxDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_chooseInfoList");
		QgglDto qgglDto = qgglService.getDtoById(qgmxDto.getQgid());
		// 根据请购ID查询请购明细(80状态)
		qgmxDto.setZt("80");
		List<QgmxDto> qgmxDtos = qgmxService.getQgmxList(qgmxDto);
		// 过滤请购明细信息
		if ("1".equals(qgmxDto.getLllb())) {
			for (int i = qgmxDtos.size() - 1; i >= 0; i--) {
				if (StringUtil.isNotBlank(qgmxDtos.get(i).getKlsl())) {
					double jssl = Double.parseDouble(qgmxDtos.get(i).getKlsl());
					if (jssl <= 0) {
						qgmxDtos.remove(i);
					}
				} else {
					qgmxDtos.remove(i);
				}
			}
		} else if ("2".equals(qgmxDto.getLllb())) {
			System.out.println();
		} else {
			for (int i = qgmxDtos.size() - 1; i >= 0; i--) {
				double jssl = Double.parseDouble(qgmxDtos.get(i).getSysl());
				if (StringUtil.isNotBlank(qgmxDtos.get(i).getYdsl())) {
					jssl = jssl - Double.parseDouble(qgmxDtos.get(i).getYdsl());
				}
				if (jssl <= 0) {
					qgmxDtos.remove(i);
				}
			}
		}
		if (StringUtil.isNotBlank(qgmxDto.getLllb())) {
			mav.addObject("lllb", qgmxDto.getLllb());
		} else {
			mav.addObject("lllb", "0");
		}
		mav.addObject("qgmxDtos", qgmxDtos);
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取请购明细信息
	 */
	@RequestMapping("/purchase/pagedataGetPurchaseInfo")
	@ResponseBody
	public Map<String, Object> getPurchaseInfo(QgmxDto qgmxDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		String dqjs = "1";
		if (StringUtil.isNotBlank(qgmxDto.getLllb())&&"1".equals(qgmxDto.getLllb())) {
			dqjs = getLoginInfo(request).getDqjs();
		}
		List<HtmxDto> htmxDtos = qgmxService.getListByQgmxids(qgmxDto, dqjs);
		if (!CollectionUtils.isEmpty(htmxDtos)) {
			List<String> wlids = htmxDtos.stream().map(HtmxDto::getWlid).collect(Collectors.toList());
			HtmxDto htmxDto_w = new HtmxDto();
			htmxDto_w.setWlids(wlids);
			List<String> swlids = htmxService.getSfccdg(htmxDto_w);
			for (HtmxDto htmxDto : htmxDtos) {
				if (swlids.contains(htmxDto.getWlid())) {
					htmxDto.setCcdg("0");
				} else {
					htmxDto.setCcdg("1");
				}
			}
		}
		if ("1".equals(qgmxDto.getKjhtbj())){
			HtmxDto htmxDto = new HtmxDto();
			htmxDto.setGys(qgmxDto.getGys());
			htmxDto.setSfgq("0");
			htmxDto.setHtlx("3");
			htmxDto.setSfty("0");
			htmxDto.setZt(StatusEnum.CHECK_PASS.getCode());
			List<HtmxDto> kjhtmxDtos = htmxService.getFrameworkContractDetail(htmxDto);
			if (!CollectionUtils.isEmpty(kjhtmxDtos)){
				Calendar instance = Calendar.getInstance();
				for (HtmxDto dto : htmxDtos) {
					for (HtmxDto kjhtmxDto : kjhtmxDtos) {
						if (StringUtil.isNotBlank(dto.getWlid())&&dto.getWlid().equals(kjhtmxDto.getWlid())){
							setKjhtmx(dto, kjhtmxDto,instance);
							break;
						}else if (StringUtil.isNotBlank(dto.getHwmc())&&dto.getHwmc().equals(kjhtmxDto.getHwmc())){
							setKjhtmx(dto, kjhtmxDto,instance);
							break;
						}
					}
				}
			}
		}
		map.put("htmxDtos", htmxDtos);
		return map;
	}

	private void setKjhtmx(HtmxDto dto, HtmxDto kjhtmxDto,Calendar instance) {
		dto.setKjhtmxid(kjhtmxDto.getHtmxid());
		dto.setHsdj(kjhtmxDto.getHsdj());
		dto.setCcdg(kjhtmxDto.getCcdg());
		dto.setWlfl(kjhtmxDto.getWlfl());
		dto.setCpzch(kjhtmxDto.getCpzch());
		dto.setBxsj(kjhtmxDto.getBxsj());
		dto.setSfgdzc(kjhtmxDto.getSfgdzc());
		dto.setBz(kjhtmxDto.getBz());
		String sysl = dto.getSysl();
		String ydsl = dto.getYdsl();
		String presl = dto.getPresl();
		BigDecimal syslB = new BigDecimal(StringUtil.isNotBlank(sysl) ? sysl : "0");
		BigDecimal ydslB = new BigDecimal(StringUtil.isNotBlank(ydsl) ? ydsl : "0");
		BigDecimal preslB = new BigDecimal(StringUtil.isNotBlank(presl) ? presl : "0");
		BigDecimal sl = syslB.subtract(ydslB).add(preslB);
		BigDecimal hsdj = new BigDecimal(StringUtil.isNotBlank(kjhtmxDto.getHsdj()) ? kjhtmxDto.getHsdj() : "0");
		BigDecimal hjje = sl.multiply(hsdj).setScale(2, RoundingMode.HALF_UP);
		dto.setHjje(String.valueOf(hjje));
		instance.setTime(new Date());
		instance.add(Calendar.DATE,StringUtil.isNotBlank(kjhtmxDto.getDhzq())?Integer.parseInt(kjhtmxDto.getDhzq()):0);
		dto.setJhdhrq(DateUtils.getCustomFomratCurrentDate(instance.getTime(),"yyyy-MM-dd"));
	}

	/**
	 * 请购物料列表
	 */
	@RequestMapping("/purchase/pageListMatterPurchase")
	public ModelAndView pageListMatterPurchase() {
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_SUB_TYPE});
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_matter_List");
		mav.addObject("qglbList", jclist.get(BasicDataTypeEnum.PURCHASE_SUB_TYPE.getCode()));
		mav.addObject("wlfllist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MATERIAL_CLASSIFICATION.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 请购物料列表数据
	 */
	@RequestMapping("/purchase/pageGetListMatterPurchase")
	@ResponseBody
	public Map<String, Object> pageGetListMatterPurchase(QgmxDto qgmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<QgmxDto> list = qgmxService.getPagedQgmxDtos(qgmxDto);
		map.put("total", qgmxDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 查看请购物料信息
	 */
	@RequestMapping("/purchase/viewMatterPurchase")
	public ModelAndView viewMatterPurchase(QgmxDto qgmxDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_matter_View");
		HtmxDto htmxDto=htmxService.getDtoById(qgmxDto.getHtmxid());
		mav.addObject("htmxDto", htmxDto!=null? htmxDto: new HtmxDto());
		qgmxDto = qgmxService.getDtoById(qgmxDto.getQgmxid());
		mav.addObject("qgmxDto", qgmxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 查看more请购物料信息
	 */
	@RequestMapping("/purchase/viewmoreMatterPurchase")
	public ModelAndView viewmoreMatterPurchase(QgmxDto qgmxDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_matter_Viewmore");
		List<QgmxDto> qgmxDtos = qgmxService.getDtoByQgidAndQgmxid(qgmxDto);
		mav.addObject("qgmxDtos", qgmxDtos);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}


	/**
	 * 取消请购页面
	 */
	@RequestMapping("/purchase/cancelrequisitionButton")
	public ModelAndView cancelRequisition(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_cancel");
		QgglDto qgglDto_t = qgglService.getDtoById(qgglDto.getQgid());
		qgglDto_t.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_CANCEL.getCode());
		mav.addObject("qgglDto", qgglDto_t);
		// 查询附件信息 -采购单
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(qgglDto_t.getYwlx());
		fjcfbDto.setYwid(qgglDto.getQgid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		// 查询基础数据信息
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(qgglDto_t.getXmdl());
		List<JcsjDto> zlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		mav.addObject("xmbmlist", zlbList);
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,
						BasicDataTypeEnum.PAYMENT_TYPE, BasicDataTypeEnum.PAYER});
		mav.addObject("xmdlbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		mav.addObject("zffslist", jclist.get(BasicDataTypeEnum.PAYMENT_TYPE.getCode()));
		mav.addObject("fkflist", jclist.get(BasicDataTypeEnum.PAYER.getCode()));
		mav.addObject("url", "/purchase/purchase/pagedataQxqgmxList?qgid=" + qgglDto.getQgid());
		mav.addObject("formAction", "/purchase/purchase/cancelrequisitionSaveButton");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 取消请购保存
	 */
	@RequestMapping("/purchase/cancelrequisitionSaveButton")
	@ResponseBody
	public Map<String, Object> cancelrequisitionSaveButton(QgqxglDto qgqxglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		qgqxglDto.setLrry(user.getYhid());
		boolean isSuccess = qgqxglService.addSavePurchaseCancel(qgqxglDto);
		if ("1".equals(systemconfigflg) && isSuccess) {
			qgqxglDto.setPrefixFlg(prefixFlg);
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qgqxgl.insertQxqg",
					JSONObject.toJSONString(qgqxglDto));
		}
		map.put("ywid", qgqxglDto.getQgqxid());
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
				: xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 价格统计
	 */
	@RequestMapping("/statistic/pagedataStatisticPrice")
	@ResponseBody
	public Map<String, Object> statisticPrice(QgmxDto qgmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> resultList = qgmxService.statisticPrice(qgmxDto);
		map.put("resultList", resultList);
		return map;
	}

	/**
	 * 请购周期统计
	 */
	@RequestMapping("/statistic/statisticQgmxCycle")
	@ResponseBody
	public Map<String, Object> statisticQgmxCycle(QgmxDto qgmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> resultList = qgmxService.statisticQgmxCycle(qgmxDto);
		map.put("resultList", resultList);
		return map;
	}

	/**
	 * 请购导入页面
	 */
	@RequestMapping(value = "/purchase/pageImportPurchase")
	public ModelAndView pageImportPurchase() {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_import");
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_SUB_TYPE});
		mav.addObject("qglbList", jclist.get(BasicDataTypeEnum.PURCHASE_SUB_TYPE.getCode()));
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 行政请购导入页面
	 */
	@RequestMapping(value = "/purchase/pageImportPurchaseAdministration")
	public ModelAndView pageImportPurchaseAdministration() {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchaseAdministration_import");
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_SUB_TYPE});
		mav.addObject("qglbList", jclist.get(BasicDataTypeEnum.PURCHASE_SUB_TYPE.getCode()));
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 过程维护
	 */
	@RequestMapping(value = "/purchase/maintenanceMatterPurchase")
	public ModelAndView maintenanceMatterPurchase(QgmxDto qgmxDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_maintenance");
		// 拆分ids获取qgid和qgmxid
		String ids = qgmxDto.getIds().toString();
		String strids = ids.substring(ids.indexOf("[") + 1);
		String newIds = strids.substring(0, strids.indexOf("]"));
		List<QgmxDto> qgmxDtos_t = new ArrayList<>();
		for (String str : newIds.split(",")) {
			String qgmxid = str.substring(0, str.indexOf("."));
			String qgid = str.substring(qgmxid.length() + 1);
			String newQgmxid = qgmxid.trim();// 去掉首尾空格
			qgmxDto.setQgmxid(newQgmxid);
			qgmxDto.setQgid(qgid);
			// 获取明细集合
			List<QgmxDto> qgmxDtos = qgmxService.getDtoByQgidAndQgmxid(qgmxDto);
			// 集合合并
			qgmxDtos_t.addAll(qgmxDtos);
		}
		qgmxDto.setFormAction("SaveMaintenance");
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.SD_TYPE});
		mav.addObject("kdlist", jclist.get(BasicDataTypeEnum.SD_TYPE.getCode()));// 快递类型
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("qgmxDtos", qgmxDtos_t);
		mav.addObject("qgmxDto", qgmxDto);
		return mav;
	}

	/**
	 * 过程维护保存
	 */
	@ResponseBody
	@RequestMapping(value = "/purchase/maintenanceSave")
	public Map<String, Object> SaveMaintenance(HtmxDto htmxDto, HttpServletRequest request) {
		User user = getLoginInfo(request);
		htmxDto.setXgry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess = htmxService.modSaveMaintenance(htmxDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
				: xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 获取部门审批流程
	 */
	@RequestMapping("/purchase/pagedataGetDingtalkFlow")
	@ResponseBody
	public Map<String, Object> getDingtalkFlow(User user) {
		Map<String, Object> map = new HashMap<>();
		List<User> yhlist = new ArrayList<>();
		if (!CollectionUtils.isEmpty(user.getIds())) {
			yhlist = commonService.getYhxxsByDdids(user);
		}
		map.put("yhlist", yhlist);
		return map;
	}

	/**
	 * 生成申购单
	 */
	@RequestMapping("/purchase/generatepurchaseButton")
	public ModelAndView generatepurchaseButton(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_generate");
		FjcfbDto fjDto = new FjcfbDto();
		fjDto.setYwid(qgglDto.getQgid());
		fjDto.setYwlx(BusTypeEnum.IMP_PURCHASE_GENERATE.getCode());
		List<FjcfbDto> fjlist = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjDto);
		mav.addObject("fjlist", fjlist);
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 替换申购单模板
	 */
	@RequestMapping("/purchase/pagedataReplacePurchase")
	@ResponseBody
	public List<Map<String, Object>> replacePurchase(QgglDto qgglDto) {
		return qgglService.getParamForPurchase(qgglDto);
	}

	/**
	 * 查询PDF版合同
	 */
	@RequestMapping("/purchase/pagedataGetPurchasePDF")
	@ResponseBody
	public Map<String, Object> getPurchasePDF(FjcfbDto fjcfbDto) {
		Map<String, Object> map = new HashMap<>();
		List<FjcfbDto> fjcfbDto_t = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		map.put("fjcfbDto", fjcfbDto_t);
		return map;
	}

	/**
	 * 获取明细基础数据
	 */
	@RequestMapping("/purchase/pagedataGetMoreJcsj")
	@ResponseBody
	public Map<String, Object> getMoreJcsj() {
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.QUALITY_REQUIRE, BasicDataTypeEnum.RECONCILIATION_TYPE});
		Map<String, Object> map = new HashMap<>();
		map.put("zlyqlist", jclist.get(BasicDataTypeEnum.QUALITY_REQUIRE.getCode()));
		map.put("dzfslist", jclist.get(BasicDataTypeEnum.RECONCILIATION_TYPE.getCode()));
		return map;
	}

	/**
	 * 处理上传的压缩包，将压缩包中的附件按物料规格分配到对应明细
	 */
	@RequestMapping("/purchase/pagedataDealQgmxBatchFile")
	@ResponseBody
	public Map<String, Object> dealQgmxBatchFile(QgglDto qgglDto) {
		return qgglService.dealBatchFileSec(qgglDto);
	}

	/**
	 * 请购明细查看 共通页面
	 */
	@RequestMapping(value = "/purchase/viewCommonPurchase")
	public ModelAndView viewCommonPurchase(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_viewCommon");
		// 获取请购信息
		QgglDto t_qgglDto = qgglService.getDtoById(qgglDto.getQgid());
		mav.addObject("qgglDto", t_qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 请购明细查看 共通页面
	 */
	@RequestMapping(value = "/purchase/viewCommonAllPurchase")
	public ModelAndView viewCommonAllPurchase(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_viewCommon");
		// 获取请购信息
		QgglDto t_qgglDto = qgglService.getDtoById(qgglDto.getQgid());
		mav.addObject("qgglDto", t_qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 请购物料列表生成合同功能(合同新增)
	 */
	@RequestMapping("/purchase/generateContract")
	public ModelAndView generateContract(QgglDto qgglDto) {
		HtglDto htglDto = new HtglDto();
		ModelAndView mav = new ModelAndView("contract/contract/contract_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[]{BasicDataTypeEnum.PAYMENT_METHOD, BasicDataTypeEnum.INVOICE_METHOD,
						BasicDataTypeEnum.CONTRACT_TYPE, BasicDataTypeEnum.CURRENCY, BasicDataTypeEnum.CONTRACT_PAYEY});
		mav.addObject("paymentlist", jclist.get(BasicDataTypeEnum.PAYMENT_METHOD.getCode()));// 付款方式
		mav.addObject("invoicelist", jclist.get(BasicDataTypeEnum.INVOICE_METHOD.getCode()));// 发票方式
		mav.addObject("contractlist", jclist.get(BasicDataTypeEnum.CONTRACT_TYPE.getCode()));// 合同类型
		mav.addObject("currencylist", jclist.get(BasicDataTypeEnum.CURRENCY.getCode()));// 币种
		mav.addObject("contractpaysylist", jclist.get(BasicDataTypeEnum.CONTRACT_PAYEY.getCode()));//付款方
		htglDto.setFormAction("addSaveContract");
		// 设置默认创建日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		htglDto.setCjrq(sdf.format(date));
		htglDto.setIds(qgglDto.getIds());//存放选择要采购的请购明细信息
		mav.addObject("url", "/contract/contract/pagedataGetContractDetailList");
		mav.addObject("htglDto", htglDto);
		mav.addObject("flag", "add");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}


	/**
	 * 行政请购列表
	 */
	@RequestMapping("/purchase/pageListPurchaseAdministration")
	public ModelAndView pageListPurchaseAdministration(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchaseAdministration_List");
		qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.PAYMENT_TYPE,
						BasicDataTypeEnum.PAYER, BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING, BasicDataTypeEnum.PURCHASE_SUB_TYPE});
		List<JcsjDto> qglbs = jclist.get(BasicDataTypeEnum.PURCHASE_SUB_TYPE.getCode());
		for (JcsjDto qglbDto : qglbs) {
			if ("ADMINISTRATION".equals(qglbDto.getCsdm())) {
				String qglb = qglbDto.getCsid();
				mav.addObject("qglb", qglb);
			}
		}
		mav.addObject("qbqgflg", qgglDto.getQbqgflg());
		mav.addObject("zffsList", jclist.get(BasicDataTypeEnum.PAYMENT_TYPE.getCode()));
		mav.addObject("fkfList", jclist.get(BasicDataTypeEnum.PAYER.getCode()));
		mav.addObject("xmdlList", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("qglbList", jclist.get(BasicDataTypeEnum.PURCHASE_SUB_TYPE.getCode()));
		return mav;
	}

	/**
	 * 行政请购列表数据
	 */
	@RequestMapping("/purchase/pageGetListPurchaseAdministration")
	@ResponseBody
	public Map<String, Object> getPageListPurchaseAdministration(QgglDto qgglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		//如果全部请购标记为1，则说明是全部请购列表，不执行以下操作
		if (!"1".equals(qgglDto.getQbqgflg())) {
			DataPermission.addCurrentUser(qgglDto, getLoginInfo(request));
			DataPermission.addJsDdw(qgglDto, "qggl", SsdwTableEnum.QGGL);
		}
		List<QgglDto> list = qgglService.getPagedListAdministration(qgglDto);
		map.put("total", qgglDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 行政请购 报销
	 */
	@RequestMapping("/purchase/baoxiaoPurchase")
	@ResponseBody
	public Map<String, Object> purchaseBaoxiao(QgglDto qgglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			// 获取用户信息
			User user = getLoginInfo(request);
			qgglDto.setXgry(user.getYhid());
			boolean isSuccess = qgglService.baoxiaoQggl(qgglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? "报销设置成功！" : "报销设置失败！");
		} // TODO Auto-generated catch block
        catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
			map.put("status", "fail");
			map.put("message", "报销设置失败！");
		}
		return map;
	}

	/**
	 * 新增请购信息(钉钉端)
	 */
	@RequestMapping("/purchase/minidataAddPurchaseDingDing")
	@ResponseBody
	public Map<String, Object> addPurchaseDingDing(QgglDto qgglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_SUB_TYPE, BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING, BasicDataTypeEnum.PURCHASE_ITEMENCODING, BasicDataTypeEnum.RESEARCH_ITEM});
		List<JcsjDto> qglbs = jclist.get(BasicDataTypeEnum.PURCHASE_SUB_TYPE.getCode());
		for (JcsjDto qglbDto : qglbs) {
			if (qgglDto.getQglbdm().equals(qglbDto.getCsdm())) {
				String qglbid = qglbDto.getCsid();
				qgglDto.setQglb(qglbid);
			}
		}
		qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		User user = getLoginInfo(request);
		qgglDto.setSqr(user.getYhid());// 默认申请人
		QgglDto mrqgglDto = qgglService.getMrsqrxxByYhid(qgglDto);// 获取默认申请人信息
		JcsjDto qglb = jcsjService.getDtoById(qgglDto.getQglb());
		if (mrqgglDto != null) {
			mrqgglDto.setCskz1(qglb.getCskz1());
			qgglDto.setSqrmc(mrqgglDto.getSqrmc());
			qgglDto.setSqr(mrqgglDto.getSqr());
			qgglDto.setSqbm(mrqgglDto.getSqbm());
			qgglDto.setSqbmmc(mrqgglDto.getSqbmmc());
			qgglDto.setSqbmdm(mrqgglDto.getSqbmdm());
			// 自动生成单据号和记录编号
			if (StringUtil.isNotBlank(mrqgglDto.getJgdh())) {
				String djh = qgglService.generateDjh(mrqgglDto);
				qgglDto.setDjh(djh);
				qgglDto.setJlbh(djh);
			}
		}
		qgglDto.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		if (StringUtil.isBlank(qgglDto.getSqrq())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
			Date date = new Date();
			qgglDto.setSqrq(sdf.format(date));
		}
		map.put("xmbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode()));
		map.put("xmdlbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		map.put("ssyfxmlist", jclist.get(BasicDataTypeEnum.RESEARCH_ITEM.getCode()));
		map.put("qgglDto", qgglDto);
		map.put("formAction", "/production/purchase/pagedataAddSavePurchase");
		map.put("url", "/production/materiel/pagedataShoppinglist");
		map.put("flag", "qgc");
		map.put("urlPrefix", urlPrefix);
		return map;
	}

	/**
	 * 行政请购列表数据(钉钉端)
	 */
	@RequestMapping("/purchase/minidataGetPageListPurchaseAdministrationDingDing")
	@ResponseBody
	public Map<String, Object> getPageListPurchaseAdministrationDingDing(QgglDto qgglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.PAYMENT_TYPE,
						BasicDataTypeEnum.PAYER, BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING, BasicDataTypeEnum.PURCHASE_SUB_TYPE});
		List<JcsjDto> qglbs = jclist.get(BasicDataTypeEnum.PURCHASE_SUB_TYPE.getCode());
		for (JcsjDto qglbDto : qglbs) {
			if ("ADMINISTRATION".equals(qglbDto.getCsdm())) {
				String qglb = qglbDto.getCsid();
				map.put("qglb", qglb);
			}
		}
		//如果全部请购标记为1，则说明是全部行政请购列表，不执行以下操作
		if (!"1".equals(qgglDto.getQbqgflg())) {
			DataPermission.addCurrentUser(qgglDto, getLoginInfo(request));
			DataPermission.addJsDdw(qgglDto, "qggl", SsdwTableEnum.QGGL);
		}
		List<QgglDto> list = qgglService.getPagedListAdministration(qgglDto);

		map.put("qbqgflg", qgglDto.getQbqgflg());
		map.put("zffsList", jclist.get(BasicDataTypeEnum.PAYMENT_TYPE.getCode()));
		map.put("fkfList", jclist.get(BasicDataTypeEnum.PAYER.getCode()));
		map.put("xmdlList", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		map.put("qgglDto", qgglDto);
		map.put("urlPrefix", urlPrefix);
		map.put("qglbList", jclist.get(BasicDataTypeEnum.PURCHASE_SUB_TYPE.getCode()));
		map.put("total", qgglDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 查看请购信息（钉钉端）
	 */
	@RequestMapping("/purchase/minidataViewPurchaseDingDing")
	@ResponseBody
	public Map<String, Object> viewPurchaseDingDing(QgglDto qgglDto) {
		Map<String, Object> map = new HashMap<>();
		QgglDto qgglDto_t = qgglService.getDto(qgglDto);
		// 查看合同明细
		HtmxDto htmxDto = new HtmxDto();
		htmxDto.setQgid(qgglDto.getQgid());
		List<HtmxDto> htmxDtos = htmxService.getDtoList(htmxDto);
		// 请购取消明细
		QgqxmxDto qgqxmxDto = new QgqxmxDto();
		qgqxmxDto.setQgid(qgglDto.getQgid());
		List<QgqxmxDto> qgqxmxDtos = qgqxmxService.getDtoList(qgqxmxDto);
		QgmxDto qgmxDto = new QgmxDto();
		qgmxDto.setQgid(qgglDto.getQgid());
		List<QgmxDto> qgmxDtos = qgmxService.getQgmxDtos(qgmxDto);
		map.put("qgmxDtos", qgmxDtos);
		map.put("htmxDtos", htmxDtos);
		map.put("qgglDto", qgglDto_t);
		map.put("urlPrefix", urlPrefix);
		map.put("qgqxmxDtos", qgqxmxDtos);
		return map;
	}

	/**
	 * 修改请购信息（钉钉端）
	 */
	@RequestMapping("/purchase/minidataModPurchaseDingDing")
	@ResponseBody
	public Map<String, Object> modPurchaseDingDing(QgglDto qgglDto) {
		Map<String, Object> map = new HashMap<>();
		QgglDto qgglDto_t = qgglService.getDtoById(qgglDto.getQgid());
		if (StringUtils.isNotBlank(qgglDto_t.getQglbdm())) {
			if ("ADMINISTRATION".equals(qgglDto_t.getQglbdm())) {
				qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_ADMINISTRATION.getCode());
			} else if ("DEVICE".equals(qgglDto_t.getQglbdm())) {
				qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_DEVICE.getCode());
			} else if ("SERVICE".equals(qgglDto_t.getQglbdm())) {
				qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_SERVICE.getCode());
			} else {
				qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
			}
		} else {
			qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		}
		map.put("qgglDto", qgglDto_t);
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PAYMENT_TYPE, BasicDataTypeEnum.PAYER, BasicDataTypeEnum.RESEARCH_ITEM});

		map.put("zffslist", jclist.get(BasicDataTypeEnum.PAYMENT_TYPE.getCode()));
		map.put("fkflist", jclist.get(BasicDataTypeEnum.PAYER.getCode()));
		map.put("ssyfxmlist", jclist.get(BasicDataTypeEnum.RESEARCH_ITEM.getCode()));
		map.put("url", "/purchase/purchase/pagedataListQgmx?qgid=" + qgglDto.getQgid());
		map.put("formAction", "/purchase/purchase/pagedataSavePurchase");
		map.put("flag", "qgmx");
		map.put("urlPrefix", urlPrefix);
		return map;
	}

	/**
	 * 修改请购信息保存（钉钉端）
	 */
	@RequestMapping("/purchase/minidataModSavePurchaseDingDing")
	@ResponseBody
	public Map<String, Object> modSavePurchaseDingDing(QgglDto qgglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		qgglDto.setXgry(user.getYhid());
		qgglDto.setFlg("0");
		int count = qgglService.getCount(qgglDto);
		if (count > 0) {
			map.put("status", "fail");
			map.put("message", "采购单号不允许重复!");
		} else {
			List<QgmxDto> qgmxlist = JSON.parseArray(qgglDto.getQgmx_json(), QgmxDto.class);
			qgglDto.setQgmxlist(qgmxlist);
			if (!CollectionUtils.isEmpty(qgmxlist)) {
				List<String> errorwlbm = new ArrayList<>();
				for (QgmxDto qgmxDto : qgmxlist) {
					if (!CollectionUtils.isEmpty(qgmxDto.getFjids())) {
						if (StringUtils.isBlank(qgmxDto.getWlzlbtc()) && "1".equals(qgmxDto.getCskz2())) {
							errorwlbm.add(qgmxDto.getWlbm());
						}
					}
					if ("1".equals(qgmxDto.getCskz1())
							&& (StringUtils.isBlank(qgmxDto.getFjbj()) || !"0".equals(qgmxDto.getFjbj()))
					) {
						map.put("status", "fail");
						map.put("message", "物料编码为" + qgmxDto.getWlbm() + "的物料需上传图纸!");
						return map;
					}
				}
				if (!CollectionUtils.isEmpty(errorwlbm) && !"1".equals(qgglDto.getBcbj())) {
					StringBuilder str = new StringBuilder();
					for (String s : errorwlbm) {
						str.append(",").append(s);
					}
					str = new StringBuilder(str.substring(1));
					map.put("status", "fail");
					map.put("message", "物料编码为" + str + "的物料子类别统称不能为空，请先完善信息!");
				} else {
					boolean isSuccess;
					try {
						isSuccess = qgglService.advancedModPurchase(qgglDto, qgmxlist);
						if ("1".equals(systemconfigflg) && isSuccess) {
							qgglDto.setPrefixFlg(prefixFlg);
							qgglDto.setQgmxlist(qgmxlist);
							amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qggl.advancedUpdate",
									JSONObject.toJSONString(qgglDto));
						}
						map.put("status", isSuccess ? "success" : "fail");
						map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
								: xxglService.getModelById("ICOM00002").getXxnr());
					} catch (BusinessException e) {
						map.put("status", "fail");
						map.put("message", e.getMsg());
						return map;
					} catch (Exception e) {
						log.error(e.toString());
						map.put("status", "fail");
						map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
						return map;
					}
				}
			} else {
				map.put("status", "fail");
				map.put("message", "请购明细不允许为空!");
			}
		}

		return map;
	}

	/**
	 * 删除请购信息（钉钉端）
	 */
	@RequestMapping("/purchase/minidataDeletePurchaseDingDing")
	@ResponseBody
	public Map<String, Object> deletePurchaseDingDing(QgglDto qgglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		try {
			// 获取用户信息
			User user = getLoginInfo(request);
			qgglDto.setScry(user.getYhid());
			boolean isSuccess = qgglService.deleteQggl(qgglDto);
			if ("1".equals(systemconfigflg) && isSuccess) {
				qgglDto.setPrefixFlg(prefixFlg);
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qggl.del",
						JSONObject.toJSONString(qgglDto));
			}
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr()
					: xxglService.getModelById("ICOM00004").getXxnr());
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			map.put("status", "fail");
			map.put("message",
					StringUtil.isNotBlank(e.getMsg()) ? e.getMsg() : xxglService.getModelById("ICOM00004").getXxnr());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
		}
		return map;
	}

	/**
	 * 请购审核列表(物料，行政，设备，服务) (钉钉端)
	 */
	@RequestMapping("/purchase/minidataGetListPurchaseDingDing_audit")
	@ResponseBody
	public Map<String, Object> getListPurchaseDingDing_audit(QgglDto qgglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 附加委托参数
		DataPermission.addWtParam(qgglDto);
		// 附加审核状态过滤
		if ("ADMINISTRATION".equals(qgglDto.getQglbdm())) {
			if (GlobalString.AUDIT_SHZT_YSH.equals(qgglDto.getDqshzt())) {
				List<AuditTypeEnum> auditTypes = new ArrayList<>();
				auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS_ADMINISTRATION);
				DataPermission._add(qgglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "qggl", "qgid",
						auditTypes, null);
			} else {
				List<AuditTypeEnum> auditTypes = new ArrayList<>();
				auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS_ADMINISTRATION);
				DataPermission._add(qgglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "qggl", "qgid",
						auditTypes, null);
			}
		} else {
			if (GlobalString.AUDIT_SHZT_YSH.equals(qgglDto.getDqshzt())) {
				List<AuditTypeEnum> auditTypes = new ArrayList<>();
				auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS);
				auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS_DEVICE);
				auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS_SERVICE);
				DataPermission._add(qgglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "qggl", "qgid",
						auditTypes, null);
			} else {
				List<AuditTypeEnum> auditTypes = new ArrayList<>();
				auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS);
				auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS_DEVICE);
				auditTypes.add(AuditTypeEnum.AUDIT_REQUISITIONS_SERVICE);
				DataPermission._add(qgglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "qggl", "qgid",
						auditTypes, null);
			}
		}

		DataPermission.addCurrentUser(qgglDto, getLoginInfo(request));
		DataPermission.addSpDdw(qgglDto, "qggl", SsdwTableEnum.QGGL);
		List<QgglDto> listMap = qgglService.getPagedAuditQggl(qgglDto);
		map.put("total", qgglDto.getTotalNumber());
		map.put("rows", listMap);
		map.put("urlPrefix", urlPrefix);
		return map;
	}

	/**
	 * 入库完成页面
	 */
	@RequestMapping("/purchase/warehouseComplete")
	public ModelAndView warehouseComplete(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/warehouse_complete");
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlprefix", urlPrefix);
		return mav;
	}

	/**
	 * 入库完成
	 */
	@RequestMapping("/purchase/saveWarehouseComplete")
	@ResponseBody
	public Map<String, Object> saveWarehouseComplete(QgglDto qgglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		qgglDto.setXgry(user.getYhid());
		boolean isSuccess = qgglService.updateWcbjs(qgglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 重新生成请购单号
	 */
	@RequestMapping("/purchase/pagedataGenerateDjh")
	@ResponseBody
	public Map<String, Object> pagedataGenerateDjh(QgglDto qgglDto) {
		Map<String, Object> map = new HashMap<>();
		//获取机构信息
		DepartmentDto departmentDto = commonService.getJgxxInfo(qgglDto.getSqbm());
		// 自动生成单据号和记录编号
		if (departmentDto != null) {
			if (StringUtils.isNotBlank(departmentDto.getKzcs1())) {
				qgglDto.setJgdh(departmentDto.getKzcs1());
				String djh = qgglService.generateDjh(qgglDto);
				map.put("djh", djh);
				map.put("jlbh", djh);
			}
		}
		return map;
	}

	/**
	 * 查看请购明细信息
	 */
	@RequestMapping("/production/viewPurchaseDetail")
	public ModelAndView viewPurchaseDetail(QgmxDto qgmxDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_viewDetail");
		qgmxDto=qgmxService.getDtoById(qgmxDto.getQgmxid());
		mav.addObject("qgmxDto", qgmxDto);
		mav.addObject("urlprefix",urlPrefix);
		return mav;
	}


	/**
	 * 选择行政入库类型页面
	 */
	@RequestMapping("/purchase/instoreChooseType")
	public ModelAndView chooseInstoreType(XzrkglDto xzrkglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_chooseInstore");
		mav.addObject("rklist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ADMINISTRATION_INBOUND_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		List<String> ids = xzrkglDto.getIds();
		StringBuilder str= new StringBuilder();
		if(!CollectionUtils.isEmpty(ids)){
			for(String s:ids){
				str.append(",").append(s);
			}
			str = new StringBuilder(str.substring(1));
		}
		mav.addObject("ids", str.toString());
		return mav;
	}

	/**
	 * 入库页面
	 */
	@RequestMapping(value = "/purchase/pagedataInstorePurchase")
	public ModelAndView instorePurchase(XzrkglDto xzrkglDto, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_instore");
		mav.addObject("kwlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.LOCATION_CATEGORY.getCode()));
		xzrkglDto.setFormAction("pagedataInstoreSavePurchase");
		// 设置默认创建日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		xzrkglDto.setRkrq(sdf.format(date));
		User user = getLoginInfo(request);
		xzrkglDto.setRkry(user.getYhid());// 默认
		xzrkglDto.setRkrymc(user.getZsxm());
		xzrkglDto.setRkdh(xzrkglService.generateDjh(xzrkglDto));
		mav.addObject("xzrkglDto", xzrkglDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("auditType", AuditTypeEnum.AUDIT_INSTORE.getCode());
		return mav;
	}


	/**
	 * 行政入库保存
	 */
	@RequestMapping("/purchase/pagedataInstoreSavePurchase")
	@ResponseBody
	public Map<String, Object> instoreSavePurchase(XzrkglDto xzrkglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		xzrkglDto.setLrry(user.getYhid());
		boolean isSuccess;
		//判断领料单号是否重复
		isSuccess = xzrkglService.getDtoByRkdh(xzrkglDto) == null;
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "入库单号不允许重复！");
			return map;
		}
		//新增保存
		try {
			if(StringUtils.isBlank(xzrkglDto.getXzrkid())) {
				xzrkglDto.setXzrkid(StringUtil.generateUUID());
			}
			isSuccess = xzrkglService.addSaveInStore(xzrkglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
			map.put("ywid", xzrkglDto.getXzrkid());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 其他入库页面
	 */
	@RequestMapping(value = "/purchase/pagedataInstoreOtherPurchase")
	public ModelAndView instoreOtherPurchase(XzrkglDto xzrkglDto, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_instoreOther");
		mav.addObject("kwlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.LOCATION_CATEGORY.getCode()));
		xzrkglDto.setFormAction("pagedataInstoreOtherSavePurchase");
		// 设置默认创建日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		xzrkglDto.setRkrq(sdf.format(date));
		User user = getLoginInfo(request);
		xzrkglDto.setRkry(user.getYhid());// 默认
		xzrkglDto.setRkrymc(user.getZsxm());
		xzrkglDto.setRkdh(xzrkglService.generateDjh(xzrkglDto));
		mav.addObject("xzrkglDto", xzrkglDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("auditType", AuditTypeEnum.AUDIT_INSTORE.getCode());
		return mav;
	}

	/**
	 * 其他入库列表
	 */
	@RequestMapping("/purchase/pagedataListInstoreOtherList")
	@ResponseBody
	public Map<String, Object> getInstoreOtherList() {
		Map<String, Object> map = new HashMap<>();
		List<XzrkmxDto> list=new ArrayList<>();
		map.put("rows",list);
		map.put("total",list.size());
		return map;
	}

	/**
	 * 其他入库保存
	 */
	@RequestMapping("/purchase/pagedataInstoreOtherSavePurchase")
	@ResponseBody
	public Map<String, Object> instoreOtherSavePurchase(XzrkglDto xzrkglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		xzrkglDto.setLrry(user.getYhid());
		boolean isSuccess;
		//判断领料单号是否重复
		isSuccess = xzrkglService.getDtoByRkdh(xzrkglDto) == null;
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "入库单号不允许重复！");
			return map;
		}
		//新增保存
		try {
			if(StringUtils.isBlank(xzrkglDto.getXzrkid())) {
				xzrkglDto.setXzrkid(StringUtil.generateUUID());
			}
			isSuccess = xzrkglService.addSaveInStoreOther(xzrkglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
			map.put("ywid", xzrkglDto.getXzrkid());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 跳转请购选择列表
	 */
	@RequestMapping(value = "/purchase/pagedataChooseAdminPurchaseList")
	public ModelAndView chooseAdminPurchaseList(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_chooseForInstore");
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取明细列表数据
	 */
	@RequestMapping("/purchase/pagedataAdminPurchaseDetailList")
	@ResponseBody
	public Map<String, Object> getAdminPurchaseDetailList(QgmxDto qgmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<QgmxDto> list=qgmxService.getQgmxList(qgmxDto);
		map.put("total", qgmxDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}
	/**
	 * 获取请购列表数据
	 */
	@RequestMapping("/purchase/pagedataAdminPurchase")
	@ResponseBody
	public Map<String, Object> pageGetListAdminPurchase(QgglDto qgglDto) {
		Map<String, Object> map = new HashMap<>();
		qgglDto.setZt("80");
		List<QgglDto> list = qgglService.getPagedListAdministration(qgglDto);
		map.put("total", qgglDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 获取请购明细信息
	 */
	@RequestMapping("/purchase/pagedataGetAdminPurchaseInfo")
	@ResponseBody
	public Map<String, Object> getAdminPurchaseInfo(QgmxDto qgmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<QgmxDto> list = qgmxService.getListByQgid(qgmxDto.getQgid());
		map.put("qgmxDtos", list);
		return map;
	}


	/**
	 * 跳转请购明细列表
	 */
	@RequestMapping(value = "/purchase/pagedataChooseListAdminPurchaseInfo")
	public ModelAndView chooseListAdminPurchaseInfo(QgmxDto qgmxDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_chooseForInstoreInfo");
		QgglDto qgglDto = qgglService.getDtoById(qgmxDto.getQgid());
		List<QgmxDto> qgmxDtos = qgmxService.getQgmxList(qgmxDto);
		mav.addObject("qgmxDtos", qgmxDtos);
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 重新生成请购单号
	 */
	@RequestMapping("/purchase/pagedataQueryWlxxByWlid")
	@ResponseBody
	public Map<String, Object> queryWlxxByWlid(CkhwxxDto ckhwxxDto) {
		CkhwxxDto ckhwxxDto_t = ckhwxxService.queryWlxxByWlid(ckhwxxDto);
		Map<String, Object> map = new HashMap<>();
		map.put("ckhwxxDto", ckhwxxDto_t);
		return map;

	}
	/**
	 * 单条物料点击查看
	 */
	@RequestMapping("/purchase/queryByQgid")
	@ResponseBody
	public Map<String, Object> queryByQgid(QgglDto qgglDto,QgmxDto qgmxDto) {
		Map<String, Object> map = new HashMap<>();
		QgglDto qgglDto_t = qgglService.getDto(qgglDto);
		// 查询附件信息 -采购单
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(qgglDto.getYwlx());
		fjcfbDto.setYwid(qgglDto.getQgid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		// 查看合同明细
		HtmxDto htmxDto = new HtmxDto();
		HwxxDto hwxxDto=new HwxxDto();
		htmxDto.setQgid(qgglDto.getQgid());
		List<HtmxDto> htmxDtos = htmxService.getDtoList(htmxDto);
		List<QgmxDto> qgmxDtos = qgmxService.getQgmxDtos(qgmxDto);
		List<List<HwxxDto>> hwxxDtos=new ArrayList<>();
		if (!CollectionUtils.isEmpty(qgmxDtos)){
			for (QgmxDto dto : qgmxDtos) {
				hwxxDto.setQgmxid(dto.getQgmxid());
				List<HwxxDto> hwxxDtoList = hwxxService.getDtolistByQgmxid(hwxxDto);
				hwxxDtos.add(hwxxDtoList);
			}

		}
		map.put("qgglDto",qgglDto_t);
		map.put("fjcfbDtos",fjcfbDtos);
		map.put("htmxDtos",htmxDtos);
		map.put("qgmxDtos",qgmxDtos);
		map.put("hwxxDtos",hwxxDtos);
		return map;
	}
	/**
	 * 跳转行政入库列表界面
	 */
	@RequestMapping(value = "/purchase/pageListAdministrationStorage")
	public ModelAndView getAdministrationStoragePageList() {
		ModelAndView mav=new ModelAndView("purchase/purchase/administrationStorage_list");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.LOCATION_CATEGORY, BasicDataTypeEnum.LOCATION_CATEGORY ,BasicDataTypeEnum.LOCATION_CATEGORY,BasicDataTypeEnum.ADMINISTRATION_INBOUND_TYPE});
		mav.addObject("kwlblist", jclist.get(BasicDataTypeEnum.LOCATION_CATEGORY.getCode()));
		mav.addObject("rklblist", jclist.get(BasicDataTypeEnum.ADMINISTRATION_INBOUND_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 行政入库列表
	 */
	@RequestMapping(value = "/purchase/pageGetListAdministrationStorage")
	@ResponseBody
	public Map<String, Object> listAdministrationStorage(XzrkglDto xzrkglDto) {
		List<XzrkglDto> t_List = xzrkglService.getPagedDtoList(xzrkglDto);
		Map<String, Object> map = new HashMap<>();
		map.put("total", xzrkglDto.getTotalNumber());
		map.put("rows", t_List);
		return map;
	}

	/**
	 * 查看行政入库信息
	 */
	@RequestMapping(value = "/purchase/viewAdministrationStorage")
	public ModelAndView viewAdministrationStorage(XzrkglDto xzrkglDto) {
		ModelAndView mav=new ModelAndView("purchase/purchase/administrationStorage_view");
		XzrkglDto dtoById = xzrkglService.getDtoByXzrkid(xzrkglDto.getXzrkid());
		List<XzrkmxDto> xzrkmxDtos = xzrkmxService.getDtoListByXzrkid(xzrkglDto.getXzrkid());
		mav.addObject("xzrkglDto", dtoById);
		mav.addObject("xzrkmxDtos", xzrkmxDtos);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 钉钉查看行政入库信息
	 */
	@RequestMapping(value = "/purchase/minidataViewAdministrationStorage")
	@ResponseBody
	public Map<String,Object> minidataViewAdministrationStorage(XzrkglDto xzrkglDto) {
		Map<String, Object> map = new HashMap<>();
		XzrkglDto dtoById = xzrkglService.getDtoByXzrkid(xzrkglDto.getXzrkid());
		List<XzrkmxDto> xzrkmxDtos = xzrkmxService.getDtoListByXzrkid(xzrkglDto.getXzrkid());
		map.put("xzrkglDto", dtoById);
		map.put("xzrkmxDtos", xzrkmxDtos);
		map.put("urlPrefix", urlPrefix);
		return map;
	}
	/**
	 * 修改行政入库信息
	 */
	@RequestMapping(value = "/purchase/modAdministrationStorage")
	public ModelAndView modAdministrationStorage(XzrkglDto xzrkglDto) {
		xzrkglDto = xzrkglService.getDtoByXzrkid(xzrkglDto.getXzrkid());
		ModelAndView mav = null;
		if ("QG".equals(xzrkglDto.getRklbdm())){
			mav=new ModelAndView("purchase/purchase/administrationStorage_QGmod");
			xzrkglDto.setFormAction("modSaveAdministrationStorage");
		}else if ("QT".equals(xzrkglDto.getRklbdm())){
			mav=new ModelAndView("purchase/purchase/administrationStorage_QTmod");
			xzrkglDto.setFormAction("modSaveAdministrationStorage");
		}
		if (mav != null) {
			mav.addObject("kwlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.LOCATION_CATEGORY.getCode()));
			mav.addObject("xzrkglDto", xzrkglDto);
			mav.addObject("urlPrefix", urlPrefix);
		}
		return mav;
	}
	/**
	 * 获取明细数据
	 */
	@RequestMapping("/purchase/pagedataAdminInStoreDetailList")
	@ResponseBody
	public Map<String, Object> pagedataAdminInStoreDetailList(XzrkmxDto xzrkmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<XzrkmxDto> xzrkmxDtos=xzrkmxService.getDtoListByDto(xzrkmxDto);
		map.put("total", xzrkmxDto.getTotalNumber());
		map.put("rows",xzrkmxDtos);
		return map;
	}
	/**
	 * 行政修改保存
	 */
	@RequestMapping("/purchase/modSaveAdministrationStorage")
	@ResponseBody
	public Map<String, Object> modSaveAdministrationStorage(XzrkglDto xzrkglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		xzrkglDto.setLrry(user.getYhid());
		boolean isSuccess;
		//修改保存
		try {
			xzrkglDto.setXgry(user.getYhid());
			if ("QG".equals(xzrkglDto.getRklbdm())){
				isSuccess = xzrkglService.modSaveAdministrationStorage(xzrkglDto);
			}else {
				isSuccess=xzrkglService.modSaveAdministrationOtherStorage(xzrkglDto);
			}
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
			map.put("ywid", xzrkglDto.getXzrkid());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 删除行政入库信息
	 */
	@RequestMapping(value ="/purchase/delAdministrationStorage")
	@ResponseBody
	public Map<String,Object> delAdministrationStorage(XzrkglDto xzrkglDto, HttpServletRequest request){
		User user = getLoginInfo(request);
		xzrkglDto.setScry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess;
		try {
			isSuccess = xzrkglService.delAdministrationStorage(xzrkglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? (xxglService.getModelById("ICOM00003").getXxnr()) : xxglService.getModelById("ICOM00004").getXxnr());
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
	 * 请购单统计
	 */
	@RequestMapping(value = "/purchase/getCountStatistics")
	@ResponseBody
	public Map<String, Object> getCountStatistics(String year) {
		List<QgglDto> list = qgglService.getCountStatistics(year);
		Map<String, Object> map = new HashMap<>();
		map.put("rows", list);
		return map;
	}
	/**
	 * 钉钉请购单统计
	 */
	@RequestMapping(value = "/purchase/minidataGetCountStatistics")
	@ResponseBody
	public Map<String, Object> minidataGetCountStatistics(String year) {
		List<QgglDto> list = qgglService.getCountStatistics(year);
		Map<String, Object> map = new HashMap<>();
		map.put("rows", list);
		return map;
	}
	/**
	 * 今日单数
	 */
	@RequestMapping(value = "/purchase/minidataGetTodeyCount")
	@ResponseBody
	public Map<String, Object> minidataGetTodeyCount(QgglDto qgglDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isBlank(qgglDto.getLrsj())){
			qgglDto.setLrsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
		}
		List<QgglDto> list = qgglService.getTodeyCount(qgglDto);
		for (QgglDto qggl : list) {
			map.put(qggl.getLx(),qggl.getSl());
		}
		return map;
	}
	/**
	 * 今日情况对应信息
	 */
	@RequestMapping(value = "/purchase/minidataGetTodayDetail")
	@ResponseBody
	public List<QgglDto> minidataGetTodayDetail(HttpServletRequest request) {
		String lx = request.getParameter("lx");
		String lrsj = request.getParameter("lrsj");
		List<QgglDto> todayList = null;
		if (StringUtil.isNotBlank(lx)){
			QgglDto qgglDto = new QgglDto();
			qgglDto.setLx(lx);
			if (StringUtil.isBlank(lrsj)){
				qgglDto.setLrsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
			}else {
				qgglDto.setLrsj(lrsj);
			}
			todayList = qgglService.getTodayList(qgglDto);
		}
		return todayList;
	}

	/**
	 * 打印
	 */
	@RequestMapping("/purchase/printPurchase")
	public ModelAndView printPurchase(QgglDto qgglDto) {
		ModelAndView mav=null;
		if("/labigams".equals(urlPrefix)){//labigams
			//获取打印信息
			JcsjDto jcsj = new JcsjDto();
			jcsj.setCsdm(qgglDto.getQglbdm());
			jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
			jcsj = jcsjService.getDto(jcsj);
			if("MATERIAL".equals(qgglDto.getQglbdm())){
				mav=new ModelAndView("purchase/purchase/purchaseMaterial_print");
			}else if("SERVICE".equals(qgglDto.getQglbdm())){
				mav=new ModelAndView("purchase/purchase/purchaseService_print");
			}else if("DEVICE".equals(qgglDto.getQglbdm())){
				mav=new ModelAndView("purchase/purchase/purchaseDevice_print");
			}
			if (mav==null){
				return mav;
			}
			mav.addObject("wjbh", jcsj);
			// QgglDto dto = qgglService.getDto(qgglDto);
			List<QgglDto> dtoListByIds = qgglService.getDtoListByIds(qgglDto);
			for (QgglDto dtoListById : dtoListByIds) {
				List<QgmxDto> qgmxList = qgmxService.getListByQgid(dtoListById.getQgid());
				dtoListById.setQgmxDtos(qgmxList);
				//获取审核信息
				ShxxDto t_shxxDto = new ShxxDto();
				t_shxxDto.setSftg("1");
				t_shxxDto.setYwid(dtoListById.getQgid());
				List<ShxxDto> shxxList = shxxDao.getShxxOrderByShsj(t_shxxDto);
				Map<String, Object> map = new HashMap<>();
				if(!CollectionUtils.isEmpty(shxxList)){
					for(int i=0;i<4;i++){
						map.put("shry_"+i, shxxList.get(i).getShrmc());
						map.put("shrq_"+i, shxxList.get(i).getShsj().substring(0,11));
					}
				}
				dtoListById.setMap(map);
			}
			mav.addObject("qgglDtos",dtoListByIds);
			// mav.addObject("qgmxList",qgmxList);
			// mav.addObject("qgglDto",dto);
			// mav.addObject("qgmxList",qgmxList);
			mav.addObject("urlPrefix", urlPrefix);
		}
		return mav;
	}
	/**
	 * 产品新增
	 */
	@RequestMapping(value = "/purchase/productionaddPurchase")
	public ModelAndView productionaddPurchase(QgglDto qgglDto,HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("purchase/purchase/purchase_productionadd");
		List<JcsjDto> xmdlbmlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode());
		List<JcsjDto> xmbmlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode());
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setJclb("PURCHASE_SUB_TYPE");
		jcsjDto.setCsdm("MATERIAL");
		JcsjDto qglb=jcsjService.getDtoByCsdmAndJclb(jcsjDto);
		qgglDto.setQglb(qglb.getCsid());
		if (StringUtils.isNotBlank(qgglDto.getQglb())) {
			qglb = jcsjService.getDtoById(qgglDto.getQglb());
			qgglDto.setQglbdm(qglb.getCsdm());
			qgglDto.setQglbmc(qglb.getCsmc());
			//判断是否是OA采购，是，提交至OA采购审核流程
			if ("1".equals(qgglDto.getQglx())) {
				qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONSTWO.getCode());
			} else {
				if ("ADMINISTRATION".equals(qglb.getCsdm())) {
					qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_ADMINISTRATION.getCode());
				} else if ("DEVICE".equals(qglb.getCsdm())) {
					qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_DEVICE.getCode());
				} else if ("SERVICE".equals(qglb.getCsdm())) {
					qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_SERVICE.getCode());
				} else {
					qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
				}
			}
		} else {
			qgglDto.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		}
		User user = getLoginInfo(request);
		qgglDto.setSqr(user.getYhid());// 默认申请人
		QgglDto mrqgglDto = qgglService.getMrsqrxxByYhid(qgglDto);// 获取默认申请人信息
		if (mrqgglDto != null) {
			mrqgglDto.setCskz1(qglb.getCskz1());
			qgglDto.setSqrmc(mrqgglDto.getSqrmc());
			qgglDto.setSqr(mrqgglDto.getSqr());
			qgglDto.setSqbm(mrqgglDto.getSqbm());
			qgglDto.setSqbmmc(mrqgglDto.getSqbmmc());
			qgglDto.setSqbmdm(mrqgglDto.getSqbmdm());
			// 自动生成单据号和记录编号
			if (StringUtil.isNotBlank(mrqgglDto.getJgdh())) {
				String djh = qgglService.generateDjh(mrqgglDto);
				qgglDto.setDjh(djh);
				qgglDto.setJlbh(djh);
			}
		}
		qgglDto.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		if (StringUtil.isBlank(qgglDto.getSqrq())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
			Date date = new Date();
			qgglDto.setSqrq(sdf.format(date));
		}
		qgglDto.setFormAction("/production/purchase/pagedataAddSavePurchase");
		mav.addObject("qgglDto",qgglDto);
		mav.addObject("mrqgglDto",mrqgglDto);
		mav.addObject("xmdlbmlist",xmdlbmlist);
		mav.addObject("xmbmlist",xmbmlist);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * @Description: 查询库存信息
	 * @param qgmxDto
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/7/8 17:03
	 */
	@RequestMapping("/purchase/pagedataQueryKcxx")
	@ResponseBody
	public Map<String, Object> pagedataQueryKcxx(QgmxDto qgmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<QgmxDto> qgmxDtoList = qgmxService.queryByKcxx(qgmxDto);
		map.put("urlPrefix", urlPrefix);
		map.put("rows", qgmxDtoList);
		return map;
	}

	@RequestMapping("/purchase/pagedataAddPickingCar")
	@ResponseBody
	public Map<String, Object> pagedataAddPickingCar(HttpServletRequest request,LlcglDto llcglDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		llcglDto.setRyid(user.getYhid());
		boolean isSuccess = llcglService.insetLlcglDtos(llcglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		List<LlcglDto> llcglDtos = llcglService.getDtoList(llcglDto);
		StringBuilder ids= new StringBuilder();
		if(!CollectionUtils.isEmpty(llcglDtos)) {
			for (LlcglDto llcglDto_t : llcglDtos) {
				ids.append(",").append(llcglDto_t.getCkhwid());
			}
		}
		map.put("idswl",ids.toString());
		return map;
	}

	@RequestMapping("/purchase/pagedataDelPickingCar")
	@ResponseBody
	public Map<String, Object> pagedataDelPickingCar(HttpServletRequest request,LlcglDto llcglDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		llcglDto.setRyid(user.getYhid());
		boolean isSuccess = llcglService.delLlcglDtos(llcglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		List<LlcglDto> llcglDtos = llcglService.getDtoList(llcglDto);
		StringBuilder ids= new StringBuilder();
		if(!CollectionUtils.isEmpty(llcglDtos)) {
			for (LlcglDto llcglDto_t : llcglDtos) {
				ids.append(",").append(llcglDto_t.getCkhwid());
			}
		}
		map.put("idswl",ids.toString());
		return map;
	}

	@RequestMapping("/purchase/pagedataQueryOverTime")
	@ResponseBody
	public Map<String, Object> pagedataQueryOverTime(HttpServletRequest request) {
		Map<String,Object> resultMap= new HashMap<>();
		Map<String,Object> map = qgglService.queryOverTime("");
		String overString = "0个";
		String overdueString = "0个";
		if(!CollectionUtils.isEmpty(map)){
			List<QgglDto> overList = (List<QgglDto>) map.get("overList");
			List<QgglDto> overdueList = (List<QgglDto>) map.get("overdueList");
			if(!CollectionUtils.isEmpty(overList)){
				overString = overList.size()+"个";
			}
			if(!CollectionUtils.isEmpty(overdueList)){
				overdueString = overdueList.size()+"个";
			}
		}
		resultMap.put("overString",overString);
		resultMap.put("overdueString",overdueString);
		return resultMap;
	}
    @RequestMapping(value = "/purchase/pagedataQueryOverTable")
    public ModelAndView pagedataQueryOverTable(QgglDto qgglDto) {
        ModelAndView mav=new ModelAndView("purchase/purchase/overTimeView");
        mav.addObject("qgglDto", qgglDto);
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
    }

	@RequestMapping("/purchase/pagedataGetOverTable")
	@ResponseBody
	public Map<String, Object> pagedataGetOverTable(QgglDto qgglDto) {
		Map<String, Object> resultMap = new HashMap<>();
		List<QgglDto> qgglDtos = qgglService.queryOverTimeTable(qgglDto);
		resultMap.put("rows", qgglDtos);
		return resultMap;
	}

	@RequestMapping(value = "/purchase/pageListOverTime")
	public ModelAndView pageListOverTime() {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_overTimeList");
		XtszDto xtszDto = xtszService.selectById("approval.qg");
		List<QgglDto> list = new ArrayList<>();
		String title = "请设置规则!";
		if (xtszDto != null) {
			if (StringUtil.isNotBlank(xtszDto.getSzz())) {
				Map<String, Object> mapT = JSON.parseObject(xtszDto.getSzz(), new TypeReference<Map<String, Object>>() {
				});
				if (mapT.get("rule") != null && mapT.get("unit") != null) {
					if("hour".equals(mapT.get("unit").toString()) && "0".equals(mapT.get("rule").toString())){
						title = "审核用时(单位/小时)(不去除节假日)";
					}
					if("hour".equals(mapT.get("unit").toString()) && "1".equals(mapT.get("rule").toString())){
						title = "审核用时(单位/小时)(去除节假日)";
					}
					if("day".equals(mapT.get("unit").toString()) && "0".equals(mapT.get("rule").toString())){
						title = "审核用时(单位/天)(不去除节假日)";
					}
					if("day".equals(mapT.get("unit").toString()) && "1".equals(mapT.get("rule").toString())){
						title = "审核用时(单位/天)(去除节假日)";
					}
					mav.addObject("unit", mapT.get("unit").toString());
				}
			}
		}
		LocalDate currentDate = LocalDate.now();
		mav.addObject("titleString", title);
		mav.addObject("sqrqend", currentDate.toString());
		mav.addObject("year",currentDate.getYear());
		LocalDate startOfYear = currentDate.with(TemporalAdjusters.firstDayOfYear());
		mav.addObject("sqrqstart", startOfYear.toString());
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	@RequestMapping(value = "/purchase/pageGetListOverTime")
	@ResponseBody
	public Map<String, Object> pageGetListOverTime(QgglDto qgglDto) {
		List<QgglDto> t_List = qgglService.getQgOverTimeTable(qgglDto);
		Map<String, Object> map = new HashMap<>();
		map.put("total", qgglDto.getTotalNumber());
		map.put("rows", t_List);
		return map;
	}

	@RequestMapping(value = "/purchase/pagedataDispose")
	public ModelAndView pagedataDispose(QgglDto qgglDto) {
		ModelAndView mav=new ModelAndView("purchase/purchase/disposeView");
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	@RequestMapping(value ="/purchase/pagedataSaveDispose")
	@ResponseBody
	public Map<String,Object> pagedataSaveDispose(QgglDto qgglDto, HttpServletRequest request){
		User user = getLoginInfo(request);
		qgglDto.setXgry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess;
		isSuccess = qgglService.updateDispose(qgglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	@RequestMapping(value = "/purchase/pagedataGetOverTimeStatistics")
	@ResponseBody
	public Map<String,Object> pagedataGetOverTimeStatistics(HttpServletRequest request) {
		String rqstart = request.getParameter("rqstart");
		String rqend = request.getParameter("rqend");
		Map<String,Object> map=new HashMap<>();
		map = qgglService.getOverTimeTable(rqstart, rqend);
		return map;
	}

}
