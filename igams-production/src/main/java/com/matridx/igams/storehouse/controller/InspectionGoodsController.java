package com.matridx.igams.storehouse.controller;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.SsdwTableEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.IUserService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.HtglDto;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.SbbfDto;
import com.matridx.igams.production.dao.entities.SbfyDto;
import com.matridx.igams.production.dao.entities.SbglwjDto;
import com.matridx.igams.production.dao.entities.SbjlDto;
import com.matridx.igams.production.dao.entities.SbpdmxDto;
import com.matridx.igams.production.dao.entities.SbtsjlDto;
import com.matridx.igams.production.dao.entities.SbyjllDto;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.dao.entities.SbyzDto;
import com.matridx.igams.production.service.svcinterface.IHtglService;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.igams.production.service.svcinterface.ISbbfService;
import com.matridx.igams.production.service.svcinterface.ISbfyService;
import com.matridx.igams.production.service.svcinterface.ISbglwjService;
import com.matridx.igams.production.service.svcinterface.ISbjlService;
import com.matridx.igams.production.service.svcinterface.ISbpdmxService;
import com.matridx.igams.production.service.svcinterface.ISbtsjlService;
import com.matridx.igams.production.service.svcinterface.ISbyjllService;
import com.matridx.igams.production.service.svcinterface.ISbyjqrService;
import com.matridx.igams.production.service.svcinterface.ISbysService;
import com.matridx.igams.production.service.svcinterface.ISbyzService;
import com.matridx.igams.storehouse.dao.entities.CkmxDto;
import com.matridx.igams.storehouse.dao.entities.CkxxDto;
import com.matridx.igams.storehouse.dao.entities.DbglDto;
import com.matridx.igams.storehouse.dao.entities.DhjyDto;
import com.matridx.igams.storehouse.dao.entities.DhxxDto;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.entities.JycglDto;
import com.matridx.igams.storehouse.dao.entities.RkglDto;
import com.matridx.igams.storehouse.dao.entities.SbtkDto;
import com.matridx.igams.storehouse.dao.entities.SbwxDto;
import com.matridx.igams.storehouse.service.svcinterface.ICkmxService;
import com.matridx.igams.storehouse.service.svcinterface.ICkxxService;
import com.matridx.igams.storehouse.service.svcinterface.IDbglService;
import com.matridx.igams.storehouse.service.svcinterface.IDhjyService;
import com.matridx.igams.storehouse.service.svcinterface.IDhxxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IJycglService;
import com.matridx.igams.storehouse.service.svcinterface.IRkglService;
import com.matridx.igams.storehouse.service.svcinterface.ISbtkService;
import com.matridx.igams.storehouse.service.svcinterface.ISbwxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/inspectionGoods")
public class InspectionGoodsController extends BaseBasicController{
	@Autowired
	private IDhjyService dhjyService;
	
	@Autowired
	private IHwxxService hwxxService;
	
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ICkxxService ckxxService;
	@Autowired
	ICkmxService ckmxService;
	@Autowired
	IDbglService dbglService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	ICommonService commonservice;
	@Autowired
	private IJycglService jycglService;
	@Autowired
	private IXxglService xxglService;
	@Autowired
	private IFjcfbService fjcfbService;
	@Autowired
	private IQgglService qgglService;
	@Autowired
	private IHtglService htglService;
	@Autowired
	private IDhxxService dhxxService;
	@Autowired
	private IRkglService rkglService;
	@Autowired
	private ISbysService sbysService;
	@Autowired
	private ISbyjllService sbyjllService;
	@Autowired
	private ISbyjqrService sbyjqrService;
	@Autowired
	private ISbtsjlService sbtsjlService;
	@Autowired
	private ISbjlService sbjlService;
	@Autowired
	private ISbyzService sbyzService;
	@Autowired
	private ISbglwjService sbglwjService;
	@Autowired
	private ISbbfService sbbfService;
	@Autowired
	ISbwxService sbwxService;
	@Autowired
	ISbtkService sbtkService;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Autowired
	IJgxxService jgxxService;
	@Autowired
	IUserService userService;
	@Autowired
	ISbpdmxService sbpdmxService;
	@Autowired
	ISbfyService sbfyService;

	/**
	 * 检验列表跳转页面
	 * 
	 * @return
	 */
	@RequestMapping("/inspectionGoods/pageListInspectionGoods")
	public ModelAndView pageListInspectionGoods(DhjyDto dhjyDto){
		ModelAndView mav = new ModelAndView("storehouse/inspection/inspection_List");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.TEST_RESULT});
		mav.addObject("jyjglist", jclist.get(BasicDataTypeEnum.TEST_RESULT.getCode()));//检验结果
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("dhjyDto", dhjyDto);
		return mav;
	}

	/**
	 * 检验列表
	 *
	 */
	@RequestMapping("/inspectionGoods/pageGetListInspectionGoods")
	@ResponseBody
	public Map<String, Object> InspectionpageList(DhjyDto dhjyDto){
		Map<String, Object> map = new HashMap<>();
		List<DhjyDto> dhjyList = new ArrayList<>();
		if ("1".equals(dhjyDto.getLbbj())){
			dhjyList = dhjyService.getPagedWlDtoList(dhjyDto);
		}else{
			dhjyList = dhjyService.getPagedDtoList(dhjyDto);
		}
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.TEST_RESULT});
		map.put("jyjglist",jclist.get(BasicDataTypeEnum.TEST_RESULT.getCode()));
		map.put("total", dhjyDto.getTotalNumber());
		map.put("rows", dhjyList);
		return map;
	}
	
	/**
	 * 到货检验查看
	 * 
	 * @param dhjyDto
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/viewInspectionGoods")
	public ModelAndView viewInspection(DhjyDto dhjyDto) {
		ModelAndView mav = new ModelAndView("storehouse/inspection/inspection_view");
		DhjyDto t_dhjyDto = dhjyService.getDtoById(dhjyDto.getDhjyid());
		t_dhjyDto.setFlag(dhjyDto.getFlag());
		HwxxDto hwxxDto=new HwxxDto();
		hwxxDto.setDhjyid(dhjyDto.getDhjyid());
		List<HwxxDto> hwxxDtos = hwxxService.getDtoByDhjyidWithYck(hwxxDto);
		String ywids = "";
		if(!CollectionUtils.isEmpty(hwxxDtos)) {
			for (HwxxDto h : hwxxDtos) {
				ywids = ywids + "," + h.getHwid();
			}
		}
		List<FjcfbDto> fjcfbDtos = new ArrayList<>();
		if(StringUtil.isNotBlank(ywids)){
			ywids = ywids.substring(1);
			List<String> idList = Arrays.stream(ywids.split(",")).map(String::valueOf).collect(Collectors.toList());
			FjcfbDto fjcfbDto=new FjcfbDto();
			fjcfbDto.setYwids(idList);
			fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTIONGOODS_BACKDROP.getCode());
			fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto);
		}
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("dhjyDto", t_dhjyDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 到货检验详细查看
	 *
	 * @param dhjyDto
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/viewmoreInspectionGoods")
	public ModelAndView viewmoreInspectionGoods(DhjyDto dhjyDto) {
		return viewInspection(dhjyDto);
	}

	/**
	 * 检验列表钉钉
	 *
	 * @param dhjyDto
	 * @return
	 */
	@RequestMapping("/inspectionGoods/minidataGetInspectionGoods")
	@ResponseBody
	public Map<String, Object> getInspectionGoods(DhjyDto dhjyDto){
		Map<String, Object> map = new HashMap<>();
		DhjyDto t_dhjyDto = dhjyService.getDtoById(dhjyDto.getDhjyid());
		HwxxDto hwxxDto = new HwxxDto();
		List<String> dhjyids= new ArrayList<>();
		dhjyids.add(dhjyDto.getDhjyid());
		hwxxDto.setDhjyids(dhjyids);
		List<HwxxDto> hwxxList =  hwxxService.getListByDhjyid(hwxxDto);
		map.put("dhjyDto", t_dhjyDto);
		map.put("jymxList",hwxxList);
		return map;
	}
	/**
	 * 检验列表钉钉
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/inspectionGoods/minidataGetInspectionGoodsDetailList")
	@ResponseBody
	public Map<String, Object> minidataGetInspectionGoodsDetailList(HwxxDto hwxxDto){
		Map<String, Object> map = new HashMap<>();
		hwxxDto=hwxxService.getDtoByHwid(hwxxDto.getHwid());
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(hwxxDto.getHwid());
		List<FjcfbDto> list=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		map.put("hwxxDto",hwxxDto);
		map.put("fjcfbList",list);
		return map;
	}
	
	/**
	 * 试剂待检验列表跳转页面
	 * 
	 * @return
	 */
	@RequestMapping("/pendingInspection/pageListPendingone")
	public ModelAndView pageListPendingone(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("storehouse/inspection/pendingInspection_List");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.MATERIELQUALITY_TYPE,BasicDataTypeEnum.ARRIVAL_CATEGORY});
		mav.addObject("lblist", jclist.get(BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode()));
		mav.addObject("dhlxList", jclist.get(BasicDataTypeEnum.ARRIVAL_CATEGORY.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		//查询当前用户检验车的货物id
		String userId = getLoginInfo(request).getYhid();
		String cskz1 = request.getParameter("cskz1");
		JycglDto jyc = jycglService.getJychwidList(userId,cskz1);
		mav.addObject("inspectionType",cskz1);//1走仪器，2走试剂,4走退回或归还质检 5走耗材
		mav.addObject("jycCount",jyc.getHwCount());
		mav.addObject("jychwids",jyc.getHwids());
		return mav;
	}

	/**
	 * 仪器待检验列表跳转页面
	 * 
	 * @return
	 */
	@RequestMapping("/pendingInspection/pageListPendingtwo")
	public ModelAndView pageListPendingInspection(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("storehouse/inspection/pendingInspection_List");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.MATERIELQUALITY_TYPE,BasicDataTypeEnum.ARRIVAL_CATEGORY});
		mav.addObject("lblist", jclist.get(BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode()));
		mav.addObject("dhlxList", jclist.get(BasicDataTypeEnum.ARRIVAL_CATEGORY.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		//查询当前用户检验车的货物id
		String userId = getLoginInfo(request).getYhid();
		String cskz1 = request.getParameter("cskz1");
		JycglDto jyc = jycglService.getJychwidList(userId,cskz1);
		mav.addObject("inspectionType",cskz1);//1走仪器，2走试剂,4走退回或归还质检 5走耗材
		mav.addObject("jycCount",jyc.getHwCount());
		mav.addObject("jychwids",jyc.getHwids());
		return mav;
	}
	/**
	 * 退货或归还待检跳转页面
	 *
	 * @return
	 */
	@RequestMapping("/pendingInspection/pageListPendingfour")
	public ModelAndView pageListPendingfour(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("storehouse/inspection/pendingInspection_List");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.MATERIELQUALITY_TYPE,BasicDataTypeEnum.ARRIVAL_CATEGORY});
		mav.addObject("lblist", jclist.get(BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode()));
		mav.addObject("dhlxList", jclist.get(BasicDataTypeEnum.ARRIVAL_CATEGORY.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		//查询当前用户检验车的货物id
		String userId = getLoginInfo(request).getYhid();
		String cskz1 = request.getParameter("cskz1");
		JycglDto jyc = jycglService.getJychwidList(userId,cskz1);
		mav.addObject("inspectionType",cskz1);//1走仪器，2走试剂,4走退回或归还质检 5走耗材
		mav.addObject("jycCount",jyc.getHwCount());
		mav.addObject("jychwids",jyc.getHwids());
		return mav;
	}
	/**
	 * 耗材待检跳转页面
	 *
	 * @return
	 */
	@RequestMapping("/pendingInspection/pageListPendingfive")
	public ModelAndView pageListPendingfive(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("storehouse/inspection/pendingInspection_List");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.MATERIELQUALITY_TYPE,BasicDataTypeEnum.ARRIVAL_CATEGORY});
		mav.addObject("lblist", jclist.get(BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode()));
		mav.addObject("dhlxList", jclist.get(BasicDataTypeEnum.ARRIVAL_CATEGORY.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		//查询当前用户检验车的货物id
		String userId = getLoginInfo(request).getYhid();
		String cskz1 = request.getParameter("cskz1");
		JycglDto jyc = jycglService.getJychwidList(userId,cskz1);
		mav.addObject("inspectionType",cskz1);//1走仪器，2走试剂,4走退回或归还质检 5走耗材
		mav.addObject("jycCount",jyc.getHwCount());
		mav.addObject("jychwids",jyc.getHwids());
		return mav;
	}

	/**
	 * 试剂待检验列表
	 * 
	 * @return
	 */
	@RequestMapping("/pendingInspection/pageGetListPendingone")
	@ResponseBody
	public Map<String, Object> pageGetListPendingone(HwxxDto hwxxDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		List<HwxxDto> hwxxList = hwxxService.getPagedDtoDjyList(hwxxDto);
		map.put("total", hwxxDto.getTotalNumber());
		map.put("rows", hwxxList);
		String userId = getLoginInfo(request).getYhid();
		JycglDto jyc = jycglService.getJychwidList(userId,hwxxDto.getLbcskz1());
		map.put("jycCount",jyc.getHwCount());
		map.put("jychwids",jyc.getHwids());
		return map;
	}
	
	/**
	 * 仪器待检验列表
	 * 
	 * @return
	 */
	@RequestMapping("/pendingInspection/pageGetListPendingtwo")
	@ResponseBody
	public Map<String, Object> pageGetListPendingtwo(HwxxDto hwxxDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		List<HwxxDto> hwxxList = hwxxService.getPagedDtoDjyList(hwxxDto);
		map.put("total", hwxxDto.getTotalNumber());
		map.put("rows", hwxxList);
		String userId = getLoginInfo(request).getYhid();
		JycglDto jyc = jycglService.getJychwidList(userId,hwxxDto.getLbcskz1());
		map.put("jycCount",jyc.getHwCount());
		map.put("jychwids",jyc.getHwids());
		return map;
	}
	/**
	 * 退货或归还待检验列表
	 *
	 * @param hwxxDto
	 * @return
	 */
	@RequestMapping("/pendingInspection/pageGetListPendingfour")
	@ResponseBody
	public Map<String, Object> pageGetListPendingfour(HwxxDto hwxxDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		List<HwxxDto> hwxxList = hwxxService.getPagedDtoDjyList(hwxxDto);
		map.put("total", hwxxDto.getTotalNumber());
		map.put("rows", hwxxList);
		String userId = getLoginInfo(request).getYhid();
		JycglDto jyc = jycglService.getJychwidList(userId,hwxxDto.getLbcskz1());
		map.put("jycCount",jyc.getHwCount());
		map.put("jychwids",jyc.getHwids());
		return map;
	}
	/**
	 * 耗材待检验列表
	 *
	 * @param hwxxDto
	 * @return
	 */
	@RequestMapping("/pendingInspection/pageGetListPendingfive")
	@ResponseBody
	public Map<String, Object> pageGetListPendingfive(HwxxDto hwxxDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		List<HwxxDto> hwxxList = hwxxService.getPagedDtoDjyList(hwxxDto);
		map.put("total", hwxxDto.getTotalNumber());
		map.put("rows", hwxxList);
		String userId = getLoginInfo(request).getYhid();
		JycglDto jyc = jycglService.getJychwidList(userId,hwxxDto.getLbcskz1());
		map.put("jycCount",jyc.getHwCount());
		map.put("jychwids",jyc.getHwids());
		return map;
	}

	/**
	 * 待检验查看
	 * 
	 * @return
	 */
	@RequestMapping(value = "/pendingInspection/viewPendingInspection")
	public ModelAndView viewPendingInspection(HwxxDto hwxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/inspection/pendingInspection_view");
		HwxxDto t_hwxxDto = hwxxService.getDtoById(hwxxDto.getHwid());
		BigDecimal dhsl = new BigDecimal(t_hwxxDto.getDhsl()==null?"0":t_hwxxDto.getDhsl());
		BigDecimal cythsl = new BigDecimal(t_hwxxDto.getCythsl()==null?"0":t_hwxxDto.getCythsl());
		String reduce = dhsl.subtract(cythsl).toString();
		t_hwxxDto.setReduce(reduce);
		QgglDto qgglDto_t = new QgglDto();
		if(StringUtil.isNotEmpty(t_hwxxDto.getQgid())) {
			QgglDto qgglDto = new QgglDto();
			qgglDto.setQgid(t_hwxxDto.getQgid());
			qgglDto_t = qgglService.getDto(qgglDto);
		}
		HtglDto htglDto = new HtglDto();
		if(StringUtil.isNotEmpty(t_hwxxDto.getHtid())) {
			htglDto = htglService.getDtoById(t_hwxxDto.getHtid());
		}
		DhxxDto dhxxDto = new DhxxDto();
		if(StringUtil.isNotEmpty(t_hwxxDto.getDhid())) {
			dhxxDto = dhxxService.getDtoById(t_hwxxDto.getDhid());
		}
		DhjyDto dhjyDto = new DhjyDto();
		if(StringUtil.isNotEmpty(t_hwxxDto.getDhjyid())) {
			dhjyDto = dhjyService.getDtoById(t_hwxxDto.getDhjyid());
		}else{
			if("3".equals(t_hwxxDto.getLbcskz1())){
				SbysDto dtoById = sbysService.getDtoById(t_hwxxDto.getHwid());
				if(dtoById!=null){
					dhjyDto.setJydh(dtoById.getSbysdh());
					dhjyDto.setJyrq(dtoById.getYsrq());
					dhjyDto.setLrsj(dtoById.getLrry());
					dhjyDto.setDhjyid(t_hwxxDto.getHwid());
				}
			}
		}
		RkglDto rkglDto = new RkglDto();
		if(StringUtil.isNotEmpty(t_hwxxDto.getRkid())) {
			rkglDto = rkglService.getDtoById(t_hwxxDto.getRkid());
		}
		mav.addObject("htglDto",htglDto);
		mav.addObject("hwxxDto", t_hwxxDto);
		mav.addObject("qgglDto",qgglDto_t);
		mav.addObject("dhxxDto",dhxxDto);
		mav.addObject("dhjyDto",dhjyDto);
		mav.addObject("rkglDto",rkglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 待处理列表跳转页面
	 *
	 * @return
	 */
	@RequestMapping("/pending/pageListPending")
	public ModelAndView pageListPending(){
		ModelAndView mav = new ModelAndView("storehouse/inspection/pending_List");
		List<JcsjDto> rklblist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.INBOUND_TYPE.getCode());
		List<JcsjDto> lblist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode());
		List<JcsjDto> dhlxlist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.ARRIVAL_CATEGORY.getCode());
		mav.addObject("lblist", lblist);
		mav.addObject("dhlxlist", dhlxlist);
		mav.addObject("rklblist", rklblist);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 待处理列表
	 *
	 * @param hwxxDto
	 * @return
	 */
	@RequestMapping("/pending/pageGetListPending")
	@ResponseBody
	public Map<String, Object> pendingList(HwxxDto hwxxDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		List<HwxxDto> hwxxList = hwxxService.getPagedDtoDclList(hwxxDto);
		map.put("total", hwxxDto.getTotalNumber());
		map.put("rows", hwxxList);
		return map;
	}

	/**
	 * 待处理查看
	 *
	 * @param hwxxDto
	 * @return
	 */
	@RequestMapping(value = "/pending/viewPendingDetail")
	public ModelAndView viewPendingDetail(HwxxDto hwxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/inspection/pending_view");
		HwxxDto hwxxDto_t = hwxxService.viewPendingDetail(hwxxDto);
		mav.addObject("hwxxDto", hwxxDto_t);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 处理功能跳转
	 */
	@RequestMapping("/pending/disposalPendingDetail")
	public ModelAndView disposalPendingDetail(HttpServletRequest request, DbglDto dbglDto) {
		ModelAndView mav = new ModelAndView("storehouse/inspection/pending_allocation");
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
		//生成处理单号
		String dbdh = dbglService.generateDbdh();
		dbglDto.setDbdh(dbdh);
		dbglDto.setFormAction("/inspectionGoods/pending/disposalSavePendingDetail");
		mav.addObject("dbglDto", dbglDto);
		mav.addObject("ckxxList",ckxxList);
		mav.addObject("hwids",dbglDto.getHwxxids());
		mav.addObject("url", "/inspectionGoods/inspection/pagedataGethwxxinfo");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 采购红字
	 */
	@RequestMapping("/pending/purchasePendingDetail")
	public ModelAndView purchasePendingDetail(HttpServletRequest request, DhxxDto dhxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/inspection/pending_purchase");
		User user = getLoginInfo(request);
		user = commonservice.getUserInfoById(user);
		dhxxDto.setSqbm(user.getJgid());
		dhxxDto.setSqbmmc(user.getJgmc());
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
		Date date = new Date();
		dhxxDto.setDhrq(sdf.format(date));
		String dhdh=dhxxService.generateDhdh();
		dhxxDto.setDhdh(dhdh);
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
		dhxxDto.setFormAction("/inspectionGoods/pending/purchaseSavePendingDetail");
		List<JcsjDto> rklblist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.INBOUND_TYPE.getCode());
		List<JcsjDto> cglblist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PURCHASE_TYPE.getCode());
		if (!CollectionUtils.isEmpty(rklblist)){
			for (JcsjDto dto : rklblist) {
				if (dto.getCsdm().equals("cghz")){
					dhxxDto.setRklb(dto.getCsid());
				}
			}
		}
		if (!CollectionUtils.isEmpty(cglblist)){
			for (JcsjDto dto : cglblist) {
				if (dto.getFcsid().equals(dhxxDto.getRklb())){
					dhxxDto.setCglx(dto.getCsid());
				}
			}
		}
		mav.addObject("hwxxDto", dhxxDto);
		mav.addObject("ckxxList",ckxxList);
		mav.addObject("rklblist", rklblist);// 入库类别
		mav.addObject("cglblist", cglblist);
		mav.addObject("url", "/inspectionGoods/inspection/pagedataGethwxxinfo");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 采购红字修改
	 */
	@RequestMapping("/pending/pagedataPurchasePendingMod")
	public ModelAndView purchasePendingMod(HttpServletRequest request, DhxxDto dhxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/inspection/pending_purchase");
		DhxxDto dtoById = dhxxService.getDtoById(dhxxDto.getDhid());
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setDhid(dhxxDto.getDhid());
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
		dtoById.setFormAction("/inspectionGoods/pending/pagedataPurchaseModSave");
		List<JcsjDto> rklblist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.INBOUND_TYPE.getCode());
		List<JcsjDto> cglblist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PURCHASE_TYPE.getCode());
		mav.addObject("hwxxDto", dtoById);
		mav.addObject("ckxxList",ckxxList);
		mav.addObject("rklblist", rklblist);// 入库类别
		mav.addObject("cglblist", cglblist);
		mav.addObject("url", "/inspectionGoods/inspection/pagedataGetCghcHwxxInfo");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 红字修改list
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/inspection/pagedataGetCghcHwxxInfo")
	@ResponseBody
	public Map<String, Object> getCghcHwxxInfo(HwxxDto hwxxDto) {
		Map<String, Object> map = new HashMap<>();
		List<HwxxDto> hwxxDtos = hwxxService.getListByChDhid(hwxxDto);
		map.put("rows", hwxxDtos);
		return map;
	}


	/**
	 * U8查询是否存在出库单
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/inspection/pagedataGetU8ckdh")
	@ResponseBody
	public Map<String, Object> getU8ckdh(CkmxDto ckmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<String> strings = ckmxService.getU8ckdh(ckmxDto);
		if(!CollectionUtils.isEmpty(strings)) {
			map.put("status", "fail");
			StringBuilder str = new StringBuilder();
			for (String string : strings) {
				str.append(",").append(string);
			}
			map.put("message", "请先删除U8出库单"+str);
		}else{
			map.put("status", "success");
		}
		return map;
	}

	/**
	 * 采购红冲新增
	 * @param
	 * @return
	 */
	@RequestMapping("/pending/purchaseSavePendingDetail")
	@ResponseBody
	public Map<String,Object> purchaseAdd(DhxxDto dhxxDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		//判断调拨单是否存在
		User user = getLoginInfo(request);
		dhxxDto.setLrry(user.getYhid());
		//校验到货单号是否重复
		boolean isSuccess=dhxxService.isDhdhRepeat(dhxxDto.getDhdh(),dhxxDto.getDhid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "到货单号不允许重复！");
			return map;
		}
		try {
			dhxxDto.setDhid(StringUtil.generateUUID());
			isSuccess=dhxxService.purchaseAdd(dhxxDto);
			map.put("ywid", dhxxDto.getDhid());
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 采购红冲修改
	 * @param
	 * @return
	 */
	@RequestMapping("/pending/pagedataPurchaseModSave")
	@ResponseBody
	public Map<String,Object> purchaseMod(DhxxDto dhxxDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		//判断调拨单是否存在
		User user = getLoginInfo(request);
		dhxxDto.setXgry(user.getYhid());
		//校验到货单号是否重复
		boolean isSuccess=dhxxService.isDhdhRepeat(dhxxDto.getDhdh(),dhxxDto.getDhid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "到货单号不允许重复！");
			return map;
		}
		try {
			isSuccess=dhxxService.purchaseMod(dhxxDto);
			map.put("ywid", dhxxDto.getDhid());
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
			map.put("auditType",AuditTypeEnum.AUDIT_GOODS_PENDING.getCode());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}


	/**
	 * 采购红字删除
	 *
	 * @param dhxxDto
	 * @return
	 */
	@RequestMapping(value = "/pending/delPurchase")
	@ResponseBody
	public Map<String, Object> purchaseDel(DhxxDto dhxxDto,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		dhxxDto.setScry(user.getYhid());
		try {
			boolean isSuccess = dhxxService.purchaseDel(dhxxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
		}
		return map;
	}
	/**
	 * 处理保存
	 * @param
	 * @return
	 */
	@RequestMapping("/pending/disposalSavePendingDetail")
	@ResponseBody
	public Map<String,Object> disposalPendingSave(DbglDto dbglDto, HttpServletRequest request){
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
			isSuccess = dbglService.disposalPendingSave(dbglDto);
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
	 * 获取rows
	 *
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/inspection/pagedataGethwxxinfo")
	@ResponseBody
	public Map<String, Object> getHwxxInfo(HwxxDto hwxxDto) {
		Map<String, Object> map = new HashMap<>();
		List<HwxxDto> hwxxDtos = hwxxService.getDtoList(hwxxDto);
		map.put("rows", hwxxDtos);
		return map;
	}
	/**
	 * 添加货物到检验车
	 * @param
	 * @return
	 */
	@RequestMapping("/pendingInspection/pagedataAddCheckMater")
	@ResponseBody
	public Map<String,Object> addToShoppingCar(JycglDto jycglDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		jycglDto.setRyid(getLoginInfo(request).getYhid());
		boolean isSuccess=jycglService.insert(jycglDto);
		//获取检验车已加入的货物id
		String cskz1 = request.getParameter("cskz1");
		JycglDto jyc = jycglService.getJychwidList(getLoginInfo(request).getYhid(),cskz1);
		map.put("jychwids",jyc.getHwids());
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 从检验车删除该货物
	 * @return
	 */
	@RequestMapping("/pendingInspection/pagedataDelCheckMater")
	@ResponseBody
	public Map<String,Object> delToShoppingCar(JycglDto jycglDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		jycglDto.setRyid(getLoginInfo(request).getYhid());
		boolean isSuccess=jycglService.delete(jycglDto);
		String cskz1 = request.getParameter("cskz1");
		//获取检验车已加入的货物ids
		JycglDto jyc = jycglService.getJychwidList(getLoginInfo(request).getYhid(),cskz1);
		map.put("jychwids",jyc.getHwids());
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 检验新增页面
	 * @author lifan
	 * @param dhjyDto 
	 * @return
	 */
	@RequestMapping(value = "/pendingInspection/inspectioncarPending")
	public ModelAndView inspectionCar(DhjyDto dhjyDto,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/inspection/inspection_add");
		//查询检验车是否有保存的记录
		String lbcskz1 = dhjyDto.getLbcskz1();
		DhjyDto dhjyRecord = dhjyService.getSaveRecord(getLoginInfo(request).getYhid(),lbcskz1);
		if (dhjyRecord != null) {
			dhjyDto = dhjyRecord;
		}
		dhjyDto.setLbcskz1(lbcskz1);
		dhjyDto.setFormAction("inspectioncarSavePending");
		mav.addObject("dhjyDto", dhjyDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("currentUser",getLoginInfo(request).getYhid());
		mav.addObject("currentUserName",getLoginInfo(request).getZsxm());
		
		if("1".equals(lbcskz1)){//仪器审核
			mav.addObject("auditType",AuditTypeEnum.AUDIT_GOODS_INSTRUMENT.getCode());
		}else if ("2".equals(lbcskz1)){//试剂审核
			mav.addObject("auditType",AuditTypeEnum.AUDIT_GOODS_REAGENT.getCode());
		}else if ("4".equals(lbcskz1)){//退货归还审核
			mav.addObject("auditType",AuditTypeEnum.AUDIT_GOODS_THORGH.getCode());
		}else if ("5".equals(lbcskz1)){//耗材审核
			mav.addObject("auditType",AuditTypeEnum.AUDIT_GOODS_CONSUMABLE.getCode());
		}
		mav.addObject("auditType_ll",AuditTypeEnum.AUDIT_GOODS_APPLY.getCode());
		mav.addObject("ywlx",BusTypeEnum.IMP_UPLOADINSPECTION.getCode());
		return mav;
	}
	/**
	 * 刷新检验单号
	 * @author lifan
	 * @return
	 */
	@RequestMapping("/pendingInspection/pagedataRefreshJydh")
	@ResponseBody
	public Map<String,Object> refreshJydh(){
		Map<String,Object> map=new HashMap<>();
		String jydh = dhjyService.getInspectionDh();
		map.put("jydh",jydh);
		return map;
	}
	/**
	 * 从检验车获取货物相关数据
	 * @author lifan
	 * @return
	 */
	@RequestMapping("/pendingInspection/pagedataGetGoodsFromJyc")
	@ResponseBody
	public Map<String,Object> getGoodsFromJyc(HttpServletRequest request){
		String lbcskz1 = request.getParameter("lbcskz1");
		Map<String,Object> map=new HashMap<>();
		String userId = getLoginInfo(request).getYhid();
		JycglDto jycgl = new JycglDto();
		jycgl.setRyid(userId);
		jycgl.setLbcskz1(lbcskz1);
		List<JycglDto> jycHwList = jycglService.getGoods(jycgl);
		map.put("rows",jycHwList);
		return map;
	}
	/**
	 * 从到货检验获取货物相关数据
	 * @author lifan
	 * @return
	 */
	@RequestMapping("/pendingInspection/pagedataGetGoodsFromDhjyid")
	@ResponseBody
	public Map<String,Object> getGoodsFromDhjyid(DhjyDto dhjyDto){
		Map<String,Object> map=new HashMap<>();
		HwxxDto hwxxDto = new HwxxDto();
		List<String> dhjyids= new ArrayList<>();
		dhjyids.add(dhjyDto.getDhjyid());
		hwxxDto.setDhjyids(dhjyids);
		List<HwxxDto> hwxxList =  hwxxService.getListByDhjyid(hwxxDto);
		map.put("rows",hwxxList);
		return map;
	}
	
	
	/**
	 * 检验修改保存
	 * @return
	 */
	@RequestMapping("/inspectionGoods/modSaveInspection")
	@ResponseBody
	public Map<String,Object> modSaveInspection(DhjyDto dhjyDto,HttpServletRequest request){
		String hwxxJson = request.getParameter("hwxxlist");
		String delids = request.getParameter("delids");
		Map<String,Object> map = new HashMap<>();
		Map<String, Object> result;
		try {
			result = dhjyService.addInspection(dhjyDto,hwxxJson,delids,getLoginInfo(request).getYhid());
			boolean isSuccess = (boolean) result.get("success");
			map.put("status",isSuccess?"success":"fail");
			//获取检验车已加入的货物ids
			JycglDto jyc = jycglService.getJychwidList(getLoginInfo(request).getYhid(),dhjyDto.getLbcskz1());
			map.put("jychwids",jyc.getHwids());
			map.put("dyjyid", result.get("dhjyid"));
			map.put("message",result.get("message"));
			map.put("ywid",result.get("ywid"));
			map.put("llbj",result.get("llbj"));
			map.put("auditType",AuditTypeEnum.AUDIT_GOODS_APPLY.getCode());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	
	/**
	 * 检验提交修改保存
	 * @return
	 */
	@RequestMapping("/inspectionGoods/submitSaveInspection")
	@ResponseBody
	public Map<String,Object> submitSaveInspection(DhjyDto dhjyDto,HttpServletRequest request){
		String hwxxJson = request.getParameter("hwxxlist");
		String delids = request.getParameter("delids");
		Map<String,Object> map = new HashMap<>();
		Map<String, Object> result;
		try {
			result = dhjyService.addInspection(dhjyDto,hwxxJson,delids,getLoginInfo(request).getYhid());
			boolean isSuccess = (boolean) result.get("success");
			map.put("status",isSuccess?"success":"fail");
			//获取检验车已加入的货物ids
			JycglDto jyc = jycglService.getJychwidList(getLoginInfo(request).getYhid(),dhjyDto.getLbcskz1());
			map.put("jychwids",jyc.getHwids());
			map.put("dyjyid", result.get("dhjyid"));
			map.put("message",result.get("message"));
			map.put("ywid",result.get("ywid"));
			map.put("llbj",result.get("llbj"));
			map.put("auditType",AuditTypeEnum.AUDIT_GOODS_APPLY.getCode());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 新增到货检验
	 * @return
	 */
	@RequestMapping("/pendingInspection/inspectioncarSavePending")
	@ResponseBody
	public Map<String,Object> addInspection(DhjyDto dhjyDto,HttpServletRequest request){
		String hwxxJson = request.getParameter("hwxxlist");
		String delids = request.getParameter("delids");
		Map<String,Object> map = new HashMap<>();
		Map<String, Object> result;
		try {
			result = dhjyService.addInspection(dhjyDto,hwxxJson,delids,getLoginInfo(request).getYhid());
			boolean isSuccess = (boolean) result.get("success");
			map.put("status",isSuccess?"success":"fail");
			//获取检验车已加入的货物ids
			JycglDto jyc = jycglService.getJychwidList(getLoginInfo(request).getYhid(),dhjyDto.getLbcskz1());
			map.put("jychwids",jyc.getHwids());
			map.put("dyjyid", result.get("dhjyid"));
			map.put("message",result.get("message"));
			map.put("ywid",result.get("ywid"));
			map.put("llbj",result.get("llbj"));
			map.put("auditType",AuditTypeEnum.AUDIT_GOODS_APPLY.getCode());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 删除到货检验数据
	 * @param dhjydto
	 * @return
	 */
	@RequestMapping("/inspectionGoods/delInspectionGoods")
	@ResponseBody
	public Map<String,Object> delInspectionGoods(DhjyDto dhjydto){
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> result = dhjyService.delInspectionGoods(dhjydto);
		boolean isSuccess = (boolean) result.get("success");
		map.put("status",isSuccess?"success":"fail");
		map.put("message",result.get("message"));
		return map;
	}
	/**
	 * 废弃
	 * @param dhjydto
	 * @return
	 */
	@RequestMapping("/inspectionGoods/abandonedInspection")
	@ResponseBody
	public Map<String,Object> abandonedInspection(DhjyDto dhjydto){
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> result = dhjyService.abandonedInspectionGoods(dhjydto);
		boolean isSuccess = (boolean) result.get("success");
		map.put("status",isSuccess?"success":"fail");
		map.put("message",result.get("message"));
		return map;
	}
	/**
	 * 合格证打印
	 */
	@RequestMapping("/inspectionGoods/certificateprintInspectionGoods")
	public ModelAndView certificateprintInspectionGoods(HwxxDto hwxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/inspection/inspection_certificateprint");
		List<HwxxDto> hwlist=hwxxService.getDtoListWithCertificate(hwxxDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("hwlist", hwlist);
		return mav;
	}

	/**
	 * @Description: 背景信息上传
	 * @param hwxxDto
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2025/5/13 17:09
	 */
	@RequestMapping("/inspectionGoods/backdropInspectionGoods")
	public ModelAndView backdropInspectionGoods(HwxxDto hwxxDto) {
		ModelAndView mav =new ModelAndView("storehouse/inspection/inspection_backdrop");
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(hwxxDto.getHwid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTIONGOODS_BACKDROP.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("ywlx", BusTypeEnum.IMP_INSPECTIONGOODS_BACKDROP.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("hwxxDto",hwxxDto);
		return mav;
	}

	@RequestMapping("/inspectionGoods/backdropSaveInspectionGoods")
	@ResponseBody
	public Map<String,Object> backdropSaveInspectionGoods(HwxxDto hwxxDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		hwxxDto.setXgry(user.getYhid());
		boolean isSuccess = dhjyService.backdropSaveInspectionGoods(hwxxDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 报告上传
	 */
	@RequestMapping("/inspectionGoods/uploadInspectionGoods")
	public ModelAndView uploadInspectionGoods(HwxxDto hwxxDto) {
		ModelAndView mav =new ModelAndView("storehouse/inspection/inspection_upload");
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(hwxxDto.getHwid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTIONGOODS_REPORT.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTIONGOODS_RECORD.getCode());
		List<FjcfbDto> fjcfbDtos_t = fjcfbService.getDtoList(fjcfbDto);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTIONGOODS_DATA.getCode());
		List<FjcfbDto> fjcfbDtos_data = fjcfbService.getDtoList(fjcfbDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("fjcfbDtos_t",fjcfbDtos_t);
		mav.addObject("fjcfbDtos_data",fjcfbDtos_data);
		mav.addObject("ywlx1", BusTypeEnum.IMP_INSPECTIONGOODS_REPORT.getCode());
		mav.addObject("ywlx2", BusTypeEnum.IMP_INSPECTIONGOODS_RECORD.getCode());
		mav.addObject("ywlx3", BusTypeEnum.IMP_INSPECTIONGOODS_DATA.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("hwxxDto",hwxxDto);
		return mav;
	}
	/**
	 * 修改
	 * @return
	 */
	@RequestMapping("/inspectionGoods/modInspection")
	public ModelAndView modInspection(DhjyDto dhjyDto,HttpServletRequest request){
		String auditType = dhjyDto.getAuditType();
		ModelAndView mav = new ModelAndView("storehouse/inspection/inspection_mod");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.TEST_RESULT});
		mav.addObject("jyjglist", jclist.get(BasicDataTypeEnum.TEST_RESULT.getCode()));// 检验结果
		dhjyDto = dhjyService.getDtoById(dhjyDto.getDhjyid());
		dhjyDto.setFormAction("modSaveInspection");
		mav.addObject("dhjyDto", dhjyDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("currentUser",getLoginInfo(request).getYhid());
		mav.addObject("currentUserName",getLoginInfo(request).getZsxm());
		mav.addObject("auditType",auditType);
		mav.addObject("ywlx",BusTypeEnum.IMP_UPLOADINSPECTION.getCode());
		mav.addObject("auditType_ll",AuditTypeEnum.AUDIT_GOODS_APPLY.getCode());
		return mav;
	}
	
	/**
	 * 提交
	 * @return
	 */
	@RequestMapping("/inspectionGoods/submitInspection")
	public ModelAndView submitInspection(DhjyDto dhjyDto,HttpServletRequest request){
		String auditType = dhjyDto.getAuditType();
		ModelAndView mav = new ModelAndView("storehouse/inspection/inspection_mod");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.TEST_RESULT});
		mav.addObject("jyjglist", jclist.get(BasicDataTypeEnum.TEST_RESULT.getCode()));// 检验结果
		dhjyDto = dhjyService.getDtoById(dhjyDto.getDhjyid());
		dhjyDto.setFormAction("submitSaveInspection");
		mav.addObject("dhjyDto", dhjyDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("currentUser",getLoginInfo(request).getYhid());
		mav.addObject("currentUserName",getLoginInfo(request).getZsxm());
		mav.addObject("auditType",auditType);
		mav.addObject("ywlx",BusTypeEnum.IMP_UPLOADINSPECTION.getCode());
		mav.addObject("auditType_ll",AuditTypeEnum.AUDIT_GOODS_APPLY.getCode());
		return mav;
	}
	/**
	 * 更新留样库存留样小结
	 */
	@RequestMapping("/inspectionGoods/uploadSaveInspectionGoods")
	@ResponseBody
	public Map<String,Object> uploadSaveInspectionGoods(HwxxDto hwxxDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		hwxxDto.setXgry(user.getYhid());
		boolean isSuccess = hwxxService.uploadSaveInspectionGoods(hwxxDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 高级修改
	 * @return
	 */
	@RequestMapping("/inspectionGoods/advancedmodInspection")
	public ModelAndView advancedmodInspection(DhjyDto dhjyDto,HttpServletRequest request){
		String auditType = dhjyDto.getAuditType();
		ModelAndView mav = new ModelAndView("storehouse/inspection/inspection_mod");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.TEST_RESULT});
		mav.addObject("jyjglist", jclist.get(BasicDataTypeEnum.TEST_RESULT.getCode()));// 检验结果
		dhjyDto = dhjyService.getDtoById(dhjyDto.getDhjyid());
		dhjyDto.setFormAction("advancedmodSaveInspection");
		mav.addObject("dhjyDto", dhjyDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("currentUser",getLoginInfo(request).getYhid());
		mav.addObject("currentUserName",getLoginInfo(request).getZsxm());
		mav.addObject("auditType",auditType);
		mav.addObject("ywlx",BusTypeEnum.IMP_UPLOADINSPECTION.getCode());
		mav.addObject("auditType_ll",AuditTypeEnum.AUDIT_GOODS_APPLY.getCode());
		return mav;
	}

	/**
	 * 高级修改保存
	 * @param dhjyDto
	 * @param request
	 * @return
	 */
	@RequestMapping("/inspectionGoods/advancedmodSaveInspection")
	@ResponseBody
	public Map<String,Object> advancedModSaveInspection(DhjyDto dhjyDto,HttpServletRequest request) {
		String hwxxJson = request.getParameter("hwxxlist");
		String delids = request.getParameter("delids");
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> result;
		try {
			result = dhjyService.advancedModSaveInspection(dhjyDto, hwxxJson, delids, getLoginInfo(request).getYhid());
			boolean isSuccess = (boolean) result.get("success");
			map.put("status", isSuccess ? "success" : "fail");
			//获取检验车已加入的货物ids
			JycglDto jyc = jycglService.getJychwidList(getLoginInfo(request).getYhid(), dhjyDto.getLbcskz1());
			map.put("jychwids", jyc.getHwids());
			map.put("dyjyid", result.get("dhjyid"));
			map.put("message", result.get("message"));
			map.put("ywid", result.get("ywid"));
			map.put("llbj", result.get("llbj"));
			map.put("auditType", AuditTypeEnum.AUDIT_GOODS_APPLY.getCode());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg()) ? e.getMsg() : xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 检验审核页面
	 * @return
	 */
	@RequestMapping("/inspectionGoods/pageListInspectionAudit")
	public ModelAndView pageListInspectionAudit(DhjyDto dhjyDto,HttpServletRequest request){
		ModelAndView mav=new ModelAndView("storehouse/inspection/inspection_auditList");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 检验审核列表
	 * @param dhjyDto
	 * @param request
	 * @return
	 */
	@RequestMapping("inspectionGoods/pageGetListInspectionAudit")
	@ResponseBody
	public Map<String,Object> getInspectionAuditList(DhjyDto dhjyDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		// 附加审核状态过滤
		List<AuditTypeEnum> auditTypes = new ArrayList<>();
		auditTypes.add(AuditTypeEnum.AUDIT_GOODS_REAGENT);
		auditTypes.add(AuditTypeEnum.AUDIT_GOODS_INSTRUMENT);
		auditTypes.add(AuditTypeEnum.AUDIT_GOODS_THORGH);
		auditTypes.add(AuditTypeEnum.AUDIT_GOODS_CONSUMABLE);
		if (GlobalString.AUDIT_SHZT_YSH.equals(dhjyDto.getDqshzt())){
			DataPermission._add(dhjyDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "dhjy", "dhjyid",auditTypes,null);
		} else{
			DataPermission._add(dhjyDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "dhjy", "dhjyid",auditTypes,null);
		}
		DataPermission.addCurrentUser(dhjyDto, getLoginInfo(request));
		List<DhjyDto> listMap = dhjyService.getPagedAuditDhjy(dhjyDto);

		map.put("total", dhjyDto.getTotalNumber());
		map.put("rows", listMap);
		return map;
	}
	/**
	 * 检验审核历史记录
	 * @param dhjyDto
	 * @param request
	 * @return
	 */
	@RequestMapping("inspectionGoods/getInspectionAuditHistoryList")
	@ResponseBody
	public Map<String, Object> getInspectionAuditHistoryList(DhjyDto dhjyDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 附加审核状态过滤
		List<AuditTypeEnum> auditTypes = new ArrayList<>();
		auditTypes.add(AuditTypeEnum.AUDIT_GOODS_REAGENT);
		auditTypes.add(AuditTypeEnum.AUDIT_GOODS_INSTRUMENT);
		auditTypes.add(AuditTypeEnum.AUDIT_GOODS_THORGH);
		auditTypes.add(AuditTypeEnum.AUDIT_GOODS_CONSUMABLE);
		if (GlobalString.AUDIT_SHZT_YSH.equals(dhjyDto.getDqshzt())) {
			DataPermission._add(dhjyDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "dhjy", "dhjyid", auditTypes,
					null);
		} else {
			DataPermission._add(dhjyDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "dhjy", "dhjyid", auditTypes,
					null);
		}
		DataPermission.addCurrentUser(dhjyDto, getLoginInfo(request));
		List<DhjyDto> listMap = dhjyService.getPagedAuditDhjy(dhjyDto);

		map.put("total", dhjyDto.getTotalNumber());
		map.put("rows", listMap);
		return map;
	}
	/**
	 * 检验审核
	 * @param dhjyDto
	 * @param request
	 * @return
	 */
	@RequestMapping("inspectionGoods/auditInspection")
	public ModelAndView inspectionAudit(DhjyDto dhjyDto,HttpServletRequest request){
		ModelAndView mav = new ModelAndView("storehouse/inspection/inspection_mod");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.TEST_RESULT});
		mav.addObject("jyjglist", jclist.get(BasicDataTypeEnum.TEST_RESULT.getCode()));
		DhjyDto t_dhjyDto = dhjyService.getDtoById(dhjyDto.getDhjyid());
		t_dhjyDto.setFormAction("auditSaveInspection");
		mav.addObject("dhjyDto", t_dhjyDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 检验审核保存
	 * @return
	 */
	@RequestMapping("/inspectionGoods/auditSaveInspection")
	@ResponseBody
	public Map<String,Object> auditSaveInspection(DhjyDto dhjyDto,HttpServletRequest request){
		String hwxxJson = request.getParameter("hwxxlist");
		String delids = request.getParameter("delids");
		Map<String,Object> map = new HashMap<>();
		Map<String, Object> result;
		try {
			result = dhjyService.addInspection(dhjyDto,hwxxJson,delids,getLoginInfo(request).getYhid());
			boolean isSuccess = (boolean) result.get("success");
			map.put("status",isSuccess?"success":"fail");
			//获取检验车已加入的货物ids
			JycglDto jyc = jycglService.getJychwidList(getLoginInfo(request).getYhid(),dhjyDto.getLbcskz1());
			map.put("jychwids",jyc.getHwids());
			map.put("dyjyid", result.get("dhjyid"));
			map.put("message",result.get("message"));
			map.put("ywid",result.get("ywid"));
			map.put("llbj",result.get("llbj"));
			map.put("auditType",AuditTypeEnum.AUDIT_GOODS_APPLY.getCode());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	
	/**
	 * 到货检验审核详细信息
	 * @param dhjyDto
	 * @param request
	 * @return
	 */
	@RequestMapping("inspectionGoods/viewInspectionAudit")
	public ModelAndView viewInspectionAudit(DhjyDto dhjyDto,HttpServletRequest request){
		ModelAndView mav = new ModelAndView("storehouse/inspection/inspection_view");
		DhjyDto t_dhjyDto = dhjyService.getDtoById(dhjyDto.getDhjyid());
		t_dhjyDto.setAuditType(request.getParameter("auditType"));
		mav.addObject("dhjyDto", t_dhjyDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 附件列表上传页面
	 * @param fjids 附件ids
	 * @param ywid  业务类型
	 * @param zywid 子业务类型
	 * @param ckbj  查看标记
	 * @param request
	 * @return
	 */
	@RequestMapping("/pendingInspection/pagedataGetUploadFilePage")
	public ModelAndView getUploadFilePage(String fjids,String ywid,String zywid,String ckbj,HttpServletRequest request) {
		String requestName = request.getParameter("requestName");
		ModelAndView mav = new ModelAndView("storehouse/inspection/inspection_uploadFile");
		// 查询临时文件并显示
		List<String> fjidList = new ArrayList<>();
		if (fjids != null) {
			String[] fjidArray = fjids.split(",");
			fjidList.addAll(Arrays.asList(fjidArray));
		}
		List<FjcfbDto> redisList = fjcfbService.getRedisList(fjidList);
		mav.addObject("redisList", redisList);
		if (!"copy".equals(requestName)) {
			// 查询正式文件并显示
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(ywid);
			fjcfbDto.setZywid(zywid);
			List<FjcfbDto> fjcfbDtos = fjcfbService.getListByZywid(fjcfbDto);
			mav.addObject("fjcfbDtos", fjcfbDtos);
			mav.addObject("ywlx", BusTypeEnum.IMP_PURCHASE.getCode());
			mav.addObject("fjids", fjids);
			mav.addObject("ywid", ywid);
			mav.addObject("zywid", zywid);
			mav.addObject("ckbj", ckbj);
		}
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTIONGOODS_REPORT.getCode());
		fjcfbDto.setYwid(zywid);
		List<FjcfbDto> fjcfbDtos_t = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		if ("viewmore".equals(request.getParameter("flag"))){
			//查询附件 报告和说明
			fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTIONGOODS_RECORD.getCode());
			List<FjcfbDto> fjcfbDtos_s = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			fjcfbDto.setYwlx(BusTypeEnum.IMP_INSPECTIONGOODS_DATA.getCode());
			List<FjcfbDto> fjcfbDtos_d = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			mav.addObject("fjcfbDtos_s", fjcfbDtos_s);
			mav.addObject("fjcfbDtos_d", fjcfbDtos_d);
		}
		mav.addObject("fjcfbDtos_t", fjcfbDtos_t);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 检验列表钉钉
	 *
	 * @param dhjyDto
	 * @return
	 */
	@RequestMapping("/inspectionGoods/minidataInspectionGoodspageList")
	@ResponseBody
	public Map<String, Object> minidataInspectionGoodspageList(DhjyDto dhjyDto){
		Map<String, Object> map = new HashMap<>();
		List<DhjyDto> dhjyList = dhjyService.getPagedDtoList(dhjyDto);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.TEST_RESULT});
		map.put("jyjglist",jclist.get(BasicDataTypeEnum.TEST_RESULT.getCode()));
		map.put("total", dhjyDto.getTotalNumber());
		map.put("rows", dhjyList);
		return map;
	}
	/**
	 * 钉钉小程序审核到货检验查看
	 *
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/minidataDingtalkviewInspectionGoods")
	@ResponseBody
	public Map<String,Object> minidataDingtalkviewInspectionGoods(DhjyDto dhjyDto) {
		Map<String,Object> map=new HashMap<>();
		DhjyDto t_dhjyDto = dhjyService.getDtoById(dhjyDto.getDhjyid());
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setDhjyid(dhjyDto.getDhjyid());
		List<HwxxDto> hwxxDtos = hwxxService.getDtoList(hwxxDto);
		map.put("dhjyDto", t_dhjyDto);
		map.put("hwxxDtos", hwxxDtos);
		return map;
	}
	
	/**
	 * 打开打印请检单页面
	 * @return
	 */
	@RequestMapping("/inspectionGoods/printCheckReport")
	public ModelAndView printCheckReport(HwxxDto hwxxDto) {
		ModelAndView mav=new ModelAndView("storehouse/dhxx/arrival_goods_print");
		List<HwxxDto> qjhwlist=hwxxService.getHwxxByZtAndDhids(hwxxDto);
		mav.addObject("qjhwlist", qjhwlist);
		mav.addObject("urlPrefix", urlPrefix);
		//根据当前账套获取打印标题
			JcsjDto jcsj = new JcsjDto();
			jcsj.setCsdm(urlPrefix);
			jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
			jcsj = jcsjService.getDto(jcsj);
			mav.addObject("dqmc",jcsj.getCsmc());
		
		return mav;
	}
	/**
	 * 检验查看 共通页面
	 * 
	 * @param dhjyDto
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/viewCommonInspectionGoods")
	public ModelAndView viewCommonInspectionGoods(DhjyDto dhjyDto) {
		ModelAndView mav = new ModelAndView("storehouse/inspection/inspectionGoods_viewCommon");
		// 获取到货信息
		DhjyDto t_dhjyDto = dhjyService.getDtoById(dhjyDto.getDhjyid());
		mav.addObject("dhjyDto",t_dhjyDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 检验查看 共通页面
	 *
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/pagedataGetGoodsView")
	public ModelAndView getGoodsView(HwxxDto hwxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/inspection/inspection_viewGoods");
		// 获取到货信息
		HwxxDto hwxxDto_t = hwxxService.getDtoById(hwxxDto.getHwid());
		mav.addObject("hwxxDto",hwxxDto_t);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取货物明明细
	 */
	@RequestMapping(value = "/inspectionGoods/minidataGetHwxxById")
	@ResponseBody
	public Map<String,Object> minidataGetHwxxById(HwxxDto hwxxDto) {
		Map<String,Object> map=new HashMap<>();
		hwxxDto=hwxxService.getDtoByHwid(hwxxDto.getHwid());
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(hwxxDto.getHwid());
		fjcfbDto=fjcfbService.getDto(fjcfbDto);
		map.put("hwxxDto", hwxxDto);
		map.put("fjcfbDto",fjcfbDto);
		return map;
	}


	/**
	 * * 成品不合格处理
	 */
	@RequestMapping("/pending/substandardDisposal")
	public ModelAndView substandardDisposal(HttpServletRequest request, RkglDto rkglDto) {
		ModelAndView mav = new ModelAndView("storehouse/putInStorage/putInStorage_edit");
		User user = getLoginInfo(request);
		user = commonservice.getUserInfoById(user);
		rkglDto.setBm(user.getJgid());
		rkglDto.setSqbmmc(user.getJgmc());
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
		Date date = new Date();
		rkglDto.setRkrq(sdf.format(date));
		String rkdh = rkglService.generatePutInStorageCode(rkglDto);
		rkglDto.setRkdh(rkdh);
		//获取仓库信息
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setCsdm("CK");
		jcsjDto.setJclb(BasicDataTypeEnum.WAREHOUSE_TYPE.getCode());
		jcsjDto = jcsjService.getDto(jcsjDto);
		CkxxDto ckxxDto = new CkxxDto();
		List<CkxxDto> ckxxList;
		if (jcsjDto != null) {
			ckxxDto.setCklb(jcsjDto.getCsid());
			ckxxList = ckxxService.getDtoList(ckxxDto);
			rkglDto.setAuditType(AuditTypeEnum.AUDIT_GOODS_STORAGE.getCode());
			rkglDto.setFormAction("addSavePutInStorage");
			rkglDto.setBs("1");
			List<JcsjDto> rklblist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INBOUND_TYPE.getCode());
//		List<JcsjDto> cglblist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PURCHASE_TYPE.getCode());
			if (!CollectionUtils.isEmpty(rklblist)) {
				for (JcsjDto dto : rklblist) {
					if (dto.getCsdm().equals("bhg")) {
						rkglDto.setRklb(dto.getCsid());
					}
				}
			}
//		if (null != cglblist && cglblist.size()>0){
//			for (JcsjDto dto : cglblist) {
//				if (dto.getCsdm().equals("01")){
//					dhxxDto.setCglx(dto.getCsid());
//				}
//			}
//		}
			mav.addObject("rkglDto", rkglDto);
			mav.addObject("cklist", ckxxList);
			mav.addObject("rklblist", rklblist);// 入库类别
			mav.addObject("xsbj", "0");
//		mav.addObject("cglblist", cglblist);
			mav.addObject("rklbbj", "0");
			mav.addObject("rkcbj", "0");
			mav.addObject("url", "/inspectionGoods/inspection/pagedataGethwxxinfo");
			mav.addObject("urlPrefix", urlPrefix);
		}
		return mav;
	}

	/**
	 * 检验统计
	 * @return
	 */
	@RequestMapping("/inspectionGoods/pageStatisticsInspectionGoods")
	public ModelAndView inspectionGoodsStats(){
		ModelAndView mav = new ModelAndView("storehouse/inspection/inspection_stats");
		List<DhjyDto> yearGroup = dhjyService.getYearGroup();
		mav.addObject("yearlist", yearGroup);
		mav.addObject("urlPrefix", urlPrefix);
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.DAY_OF_MONTH, instance.getActualMinimum(Calendar.DAY_OF_MONTH));
		mav.addObject("rqstart", DateUtils.getCustomFomratCurrentDate(instance.getTime(),"yyyy-MM-dd"));
		instance.set(Calendar.DAY_OF_MONTH, instance.getActualMaximum(Calendar.DAY_OF_MONTH));
		mav.addObject("rqend", DateUtils.getCustomFomratCurrentDate(instance.getTime(),"yyyy-MM-dd"));
		return mav;
	}

	/**
	 * 检验统计接口
	 * @return
	 */
	@RequestMapping("/inspectionGoods/pagedataStatisticsInspectionInfer")
	@ResponseBody
	public Map<String,Object> pagedataStatisticsInspectionInfer(){
		Map<String,Object> map=new HashMap<>();
		List<DhjyDto> yearGroup = dhjyService.getYearGroup();
		map.put("yearlist", yearGroup);
		map.put("urlPrefix", urlPrefix);
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.DAY_OF_MONTH, instance.getActualMinimum(Calendar.DAY_OF_MONTH));
		map.put("rqstart", DateUtils.getCustomFomratCurrentDate(instance.getTime(),"yyyy-MM-dd"));
		instance.set(Calendar.DAY_OF_MONTH, instance.getActualMaximum(Calendar.DAY_OF_MONTH));
		map.put("rqend", DateUtils.getCustomFomratCurrentDate(instance.getTime(),"yyyy-MM-dd"));
		return map;
	}

	/**
	 * 检验审核统计
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/pagedataGetAuditStatistics")
	@ResponseBody
	public Map<String,Object> getAuditStatistics(HttpServletRequest request) {
		String year = request.getParameter("year");
		String id = request.getParameter("id");
		String rqstart = request.getParameter("rqstart");
		String rqend = request.getParameter("rqend");
		Map<String,Object> map=new HashMap<>();
		List<DhjyDto> list = new ArrayList<>();
		List<HwxxDto> purchaseStatistics =new ArrayList<>();
		List<Map<String,Object>> inspectionResult =new ArrayList<>();
		List<Map<String,Object>> inspectionCategoryProportion = new ArrayList<>();
		if(StringUtil.isNotBlank(id)){
			if("zt".equals(id)){
				list = dhjyService.getAuditStatistics(year);
			}else if("qy".equals(id)){
				purchaseStatistics = hwxxService.getPurchaseStatistics(year);
			}else if("jyjg".equals(id)){
				inspectionResult = dhjyService.getInspectionResultStatistics(year);
			}else if("wllbzb".equals(id)){
				inspectionCategoryProportion = dhjyService.getInspectionCategoryProportion(rqstart, rqend);
			}
		}else{
			list = dhjyService.getAuditStatistics(year);
			purchaseStatistics = hwxxService.getPurchaseStatistics(year);
			inspectionResult = dhjyService.getInspectionResultStatistics(year);
			inspectionCategoryProportion = dhjyService.getInspectionCategoryProportion(rqstart, rqend);
		}
		map.put("zt", list);
		map.put("qy", purchaseStatistics);
		map.put("jyjg", inspectionResult);
		map.put("wllbzb", inspectionCategoryProportion);
		return map;
	}
	/**
	 * 新固定资产列表
	 * @return
	 */
	@RequestMapping("/inspectionGoods/pageListEquipmentAcceptance")
	public ModelAndView pageListEquipmentAcceptance(){
		ModelAndView mav = new ModelAndView("storehouse/inspection/equipmentAcceptance_list");
		List<SbysDto> xybmList=sbysService.getDepartmentList();
		List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("xybmList",xybmList);
		mav.addObject("sblxList",sblxList);
		return mav;
	}
	/**
	 * 全部设备列表数据
	 *
	 * @param sbysDto
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/pageGetListEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> pageGetListEquipmentAcceptance(SbysDto sbysDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbysDto.setXsybm(user.getJgid());
		sbysDto.setXsyr(user.getYhid());
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setJclb(BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
		if (StringUtil.isNotBlank(sbysDto.getSblxdm())){
			jcsjDto.setCsdm(sbysDto.getSblxdm());
			jcsjDto=jcsjService.getDtoByCsdmAndJclb(jcsjDto);
			sbysDto.setSblx(jcsjDto.getCsid());
		}
		super.setCzdmList(request,map);
		List<SbysDto> xybmList=sbysService.getDepartmentList();
		List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
		List<JcsjDto> yyList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DEVICE_TURN_REASON.getCode());
		map.put("xybmList",xybmList);
		map.put("sblxList",sblxList);
		map.put("yyList",yyList);
		if ("1".equals(sbysDto.getSfbmxz())){
			DataPermission.addCurrentUser(sbysDto, getLoginInfo(request));
			DataPermission.addJsDdw(sbysDto, "sbys", SsdwTableEnum.SBYS);
		}
		if(sbysDto.getPdzts()!=null&&sbysDto.getPdzts().length>0){
			for(String pdzt:sbysDto.getPdzts()){
				if("0".equals(pdzt)){
					sbysDto.setPdzt("0");
				}
			}
		}
		List<SbysDto> sbysDtoList=sbysService.getPagedEquipmentList(sbysDto);
		map.put("total",sbysDto.getTotalNumber());
		map.put("rows",sbysDtoList);
		map.put("yhid",user.getYhid());
		map.put("select","EQUIPMENTACCEPTANCE_SELECT");
		map.put("search","EQUIPMENTACCEPTANCE_SEARCH");
		return map;
	}
	/**
	 * 全部设备列表数据
	 *
	 * @param sbysDto
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/pagedataPurchaseEquipment")
	@ResponseBody
	public Map<String,Object> pagedataPurchaseEquipment(SbysDto sbysDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		List<SbysDto> sbysDtoList=sbysService.getSbysDtoList(sbysDto);
		map.put("rows",sbysDtoList);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 设备验收列表开箱验收打印
	 */
	@RequestMapping(value ="/inspectionGoods/unpackingacceptanceprintDevice")
	@ResponseBody
	public Map<String,Object> unpackingacceptanceprintDevice(SbysDto sbysDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		List<SbysDto> sbysDtos=sbysService.getSbysDtoList(sbysDto);
		//根据基础数据获取打印信息
		JcsjDto jcsj = new JcsjDto();
		jcsj.setCsdm("sb_kxys");
		jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
		jcsj = jcsjService.getDto(jcsj);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(sbysDto.getSbysid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_DEVICE_CHECK.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
		StringBuilder fjnames= new StringBuilder();
		if (!CollectionUtils.isEmpty(fjcfbDtos)){
			for (FjcfbDto fjcfbDto1:fjcfbDtos){
				fjnames.append(",").append(fjcfbDto1.getFileName());
			}
		}
		if (fjnames.length()>0){
			fjnames = new StringBuilder(fjnames.substring(1));
		}
		map.put("wjbh",StringUtil.isNotBlank(jcsj.getCsmc())?jcsj.getCsmc():"");
		map.put("sbysDtos",sbysDtos);
		map.put("fjnames", fjnames.toString());
		map.put("applicationurl",applicationurl);
		return map;
	}
	/**
	 * 设备验收列表验收报告打印
	 */
	@RequestMapping(value ="/inspectionGoods/acceptancereportprintDevice")
	@ResponseBody
	public Map<String,Object> acceptancereportprintDevice(SbysDto sbysDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		List<SbysDto> sbysDtos=sbysService.getSbysDtoList(sbysDto);
		List<JcsjDto> qwfsList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DECONTAMINATION_METHOD.getCode());
		//根据基础数据获取打印信息
		JcsjDto jcsj = new JcsjDto();
		jcsj.setCsdm("sb_ysbg");
		jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
		jcsj = jcsjService.getDto(jcsj);
		for (JcsjDto jcsjDto:qwfsList){
			if ("其他".equals(jcsjDto.getCsmc())){
				qwfsList.remove(jcsjDto);
				break;
			}
		}
		map.put("wjbh",StringUtil.isNotBlank(jcsj.getCsmc())?jcsj.getCsmc():"");
		map.put("sxrq",StringUtil.isNotBlank(jcsj.getCskz1())?jcsj.getCskz1():"");
		map.put("bbh",StringUtil.isNotBlank(jcsj.getCskz1())?jcsj.getCskz1():"");
		map.put("sbysDtos",sbysDtos);
		map.put("qwfsList",qwfsList);
		map.put("applicationurl",applicationurl);
		return map;
	}
	/**
	 * 设备列表安装调试
	 */
	@RequestMapping(value ="/inspectionGoods/shakedownprintDevice")
	@ResponseBody
	public Map<String,Object> shakedownprintprintDevice(SbyjllDto sbyjllDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		List<SbyjllDto> sbyjllDtos=sbyjllService.getDtoList(sbyjllDto);
		List<String> llids=new ArrayList<>();
		List<SbtsjlDto> sbtsjlDtos;
		if(!CollectionUtils.isEmpty(sbyjllDtos)) {
			for(SbyjllDto sbyjllDto1:sbyjllDtos){
				llids.add(sbyjllDto1.getLlid());
			}
			SbtsjlDto sbtsjlDto=new SbtsjlDto();
			sbtsjlDto.setLlids(llids);
			sbtsjlDtos=sbtsjlService.getDtoList(sbtsjlDto);
			map.put("sbtsjlDtos",sbtsjlDtos);
		}else {
			map.put("status","fail");
			map.put("message","该设备没有调试记录!");
		}
		return map;
	}
	/**
	 * 设备安装调试打印
	 */
	@RequestMapping(value ="/inspectionGoods/pagedataPrintDevice")
	@ResponseBody
	public Map<String,Object> pagedataPrintDevice(SbtsjlDto sbtsjlDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		List<SbtsjlDto> sbtsjlDtos=sbtsjlService.getDtoList(sbtsjlDto);
		//根据基础数据获取打印信息
		JcsjDto jcsj = new JcsjDto();
		jcsj.setCsdm("sb_sbts");
		jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
		jcsj = jcsjService.getDto(jcsj);
		map.put("sbtsjlDtos",sbtsjlDtos);
		map.put("wjbh",StringUtil.isNotBlank(jcsj.getCsmc())?jcsj.getCsmc():"");
		map.put("applicationurl",applicationurl);
		return map;
	}
	/**
	 * 设备移交列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/pageGetListEquipmentTurnOver")
	@ResponseBody
	public Map<String,Object> pageGetListEquipmentTurnOver(SbyjllDto sbyjllDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		super.setCzdmList(request,map);
		sbyjllDto.setFlag("0");
		List<SbyjllDto> ysybmList=sbyjllService.selectOldOrNewDepartments(sbyjllDto);
		sbyjllDto.setFlag("1");
		List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
		List<SbyjllDto> xsybmList=sbyjllService.selectOldOrNewDepartments(sbyjllDto);
		if ("1".equals(sbyjllDto.getSfbmxz())){
			DataPermission.addCurrentUser(sbyjllDto,user);
			DataPermission.addJsDdw(sbyjllDto, "sbyjll", SsdwTableEnum.SBYJll);
		}
		List<SbyjllDto> sbysDtoList=sbyjllService.getPagedDtoList(sbyjllDto);
		List<JcsjDto> yyList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DEVICE_TURN_REASON.getCode());
		map.put("yyList",yyList);
		map.put("ysybmList",ysybmList);
		map.put("xsybmList",xsybmList);
		map.put("sblxList",sblxList);
		map.put("total",sbyjllDto.getTotalNumber());
		map.put("rows",sbysDtoList);
		map.put("auditType", AuditTypeEnum.AUDIT_TURN_METERING.getCode());
		return map;
	}
	/**
	 * 设备移交列表查看
	 *
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/viewEquipmentTurnOver")
	@ResponseBody
	public Map<String,Object> viewEquipmentTurnOver(SbyjllDto sbyjllDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		sbyjllDto=sbyjllService.getDtoById(sbyjllDto.getLlid());
		map.put("sbyjllDto",sbyjllDto);
		return map;
	}
	/**
	 * 设备移交列表提交按钮
	 *
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/submitEquipmentTurnOver")
	@ResponseBody
	public Map<String,Object> submitEquipmentTurnOver(SbyjllDto sbyjllDto) {
		Map<String,Object> map=new HashMap<>();
		sbyjllDto=sbyjllService.getDtoById(sbyjllDto.getLlid());
		List<JcsjDto> yyList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DEVICE_TURN_REASON.getCode());
		map.put("yyList",yyList);
		map.put("sbyjllDto",sbyjllDto);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 设备移交列表提交保存按钮
	 *
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/submitSaveEquipmentTurnOver")
	@ResponseBody
	public Map<String,Object> submitSaveEquipmentTurnOver(SbyjllDto sbyjllDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		SbysDto sbysDto=new SbysDto();
		sbysDto.setXgry(user.getYhid());
		boolean isSuccess;
		try {
			isSuccess=sbyjllService.turnSaveEquipmentAcceptance(sbysDto,sbyjllDto);
			map.put("auditType", AuditTypeEnum.AUDIT_TURN_METERING.getCode());
			map.put("ywid",sbyjllDto.getLlid());
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}catch (BusinessException e){
			map.put("status", "fail");
			map.put("message", e.getMsg()!=null?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 设备移交列表删除按钮
	 */
	@RequestMapping(value = "/inspectionGoods/delMetering")
	@ResponseBody
	public Map<String, Object> delMetering(SbjlDto sbjlDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		sbjlDto.setScry(user.getYhid());
		boolean isSuccess=sbjlService.delSaveDeviceMetering(sbjlDto,"delete");
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	/**
	 * 设备移交审核列表
	 *
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/pageGetListAuditedTurnOver")
	@ResponseBody
	public Map<String,Object> pageGetListAuditedTurnOver(SbyjllDto sbyjllDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		super.setCzdmList(request,map);
		sbyjllDto.setLrry(user.getYhid());
		sbyjllDto.setFlag("0");
		List<SbyjllDto> ysybmList=sbyjllService.selectOldOrNewDepartments(sbyjllDto);
		sbyjllDto.setFlag("1");
		List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
		List<SbyjllDto> xsybmList=sbyjllService.selectOldOrNewDepartments(sbyjllDto);
		// 附加委托参数
		DataPermission.addWtParam(sbyjllDto);
		super.setCzdmList(request,map);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(sbyjllDto.getDqshzt())) {
			DataPermission.add(sbyjllDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "sbyjll", "llid",
					AuditTypeEnum.AUDIT_TURN_METERING);
		} else {
			DataPermission.add(sbyjllDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "sbyjll", "llid",
					AuditTypeEnum.AUDIT_TURN_METERING);
		}
		DataPermission.addCurrentUser(sbyjllDto, getLoginInfo(request));
		DataPermission.addSpDdw(sbyjllDto, "sbyjll", SsdwTableEnum.SBYJll);
		List<SbyjllDto> sbysDtoList=sbyjllService.getPagedAuditDeviceTurnOver(sbyjllDto);
		map.put("ysybmList",ysybmList);
		map.put("xsybmList",xsybmList);
		map.put("sblxList",sblxList);
		map.put("total",sbyjllDto.getTotalNumber());
		map.put("rows",sbysDtoList);
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_METERING.getCode());
		return map;
	}
	/**
	 * 设备移交审核按钮
	 */
	@RequestMapping(value = "/inspectionGoods/auditEquipmentTurnOver")
	@ResponseBody
	public Map<String,Object> auditEquipmentTurnOver(SbyjllDto sbyjllDto) {
		Map<String,Object> map=new HashMap<>();
		sbyjllDto=sbyjllService.getDtoById(sbyjllDto.getLlid());
		List<JcsjDto> yyList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DEVICE_TURN_REASON.getCode());
		map.put("yyList",yyList);
		map.put("sbyjllDto",sbyjllDto);
		map.put("urlPrefix",urlPrefix);
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_METERING.getCode());
		return map;
	}
	/**
	 * 固定资产列表钉钉移交确认页面
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/minidataTurnEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> minidataTurnEquipmentAcceptance(SbyjllDto sbyjllDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		super.setCzdmList(request,map);
		sbyjllDto.setLrry(user.getYhid());
		sbyjllDto.setFlag("0");
		List<SbyjllDto> ysybmList=sbyjllService.selectOldOrNewDepartments(sbyjllDto);
		sbyjllDto.setFlag("1");
		List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
		List<SbyjllDto> xsybmList=sbyjllService.selectOldOrNewDepartments(sbyjllDto);
		List<SbyjllDto> sbysDtoList=sbyjllService.getPagedDtoList(sbyjllDto);
		List<JcsjDto> yyList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DEVICE_TURN_REASON.getCode());
		map.put("yyList",yyList);
		map.put("ysybmList",ysybmList);
		map.put("xsybmList",xsybmList);
		map.put("sblxList",sblxList);
		map.put("total",sbyjllDto.getTotalNumber());
		map.put("rows",sbysDtoList);
		map.put("auditType", AuditTypeEnum.AUDIT_TURN_METERING.getCode());
		return map;
	}
	/**
	 * 固定资产新增页面
	 * @return
	 */
	@RequestMapping("/inspectionGoods/addEquipmentAcceptance")
	public ModelAndView addEquipmentAcceptance(SbysDto sbysDto){
		ModelAndView mav = new ModelAndView("storehouse/inspection/equipmentAcceptance_add");
		sbysDto.setFormAction("/inspectionGoods/inspectionGoods/addSaveEquipmentAcceptance");
		List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
		mav.addObject("sblxList",sblxList);
		mav.addObject("sbysDto",sbysDto);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	/**
	 * 新增固定资产保存
	 *
	 * @param sbysDto
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/addSaveEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> addSaveEquipmentAcceptance(SbysDto sbysDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbysDto.setSbysid(StringUtil.generateUUID());
		sbysDto.setLrry(user.getYhid());
		sbysDto.setSbzt("0");
		sbysDto.setZt("80");
		boolean isSuccess=sbysService.insert(sbysDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 固定资产修改页面
	 * @return
	 */
	@RequestMapping("/inspectionGoods/modEquipmentAcceptance")
	public ModelAndView modEquipmentAcceptance(SbysDto sbysDto){
		ModelAndView mav = new ModelAndView("storehouse/inspection/equipmentAcceptance_add");
		sbysDto=sbysService.getSbysDtoById(sbysDto.getSbysid());
		sbysDto.setFormAction("/inspectionGoods/inspectionGoods/modSaveEquipmentAcceptance");
		List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
		mav.addObject("sblxList",sblxList);
		mav.addObject("sbysDto",sbysDto);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	/**
	 * 修改固定资产保存
	 *
	 * @param sbysDto
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/modSaveEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> modSaveEquipmentAcceptance(SbysDto sbysDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		try {
			User user=getLoginInfo(request);
			sbysDto.setXgry(user.getYhid());
			boolean isSuccess=sbysService.updateAndSave(sbysDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}catch (BusinessException e){
			map.put("status","fail");
			map.put("message",e.getMsg());
		}

		return map;
	}
	/**
	 * 全部设备列表查看页面
	 * @return
	 */
	@RequestMapping("/inspectionGoods/pagedataEquipmentAcceptanceOA")
	public ModelAndView pagedataEquipmentAcceptanceOA(SbysDto sbysDto){
		ModelAndView mav = new ModelAndView("storehouse/inspection/equipmentAcceptance_view");
		sbysDto=sbysService.getSbysDtoById(sbysDto.getSbysid());
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_DEVICE_CHECK.getCode());
		fjcfbDto.setYwid(sbysDto.getSbysid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("sbysDto",sbysDto);
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	/**
	 * OA全部设备列表查看数据
	 *
	 * @param sbysDto
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/pagedataEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> pagedataEquipmentAcceptance(SbysDto sbysDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		sbysDto=sbysService.getSbysDtoById(sbysDto.getSbysid());
		SbjlDto sbjlDto=new SbjlDto();
		sbjlDto.setSbysid(sbysDto.getSbysid());
		List<SbjlDto> sbjlDtos=sbjlService.getDtoList(sbjlDto);
		SbyzDto sbyzDto=new SbyzDto();
		sbyzDto.setSbysid(sbysDto.getSbysid());
		List<SbyzDto> sbyzDtos=sbyzService.getDtoList(sbyzDto);
		SbyjllDto sbyjllDto=new SbyjllDto();
		sbyjllDto.setSbysid(sbysDto.getSbysid());
		List<SbyjllDto> sbyjllDtos=sbyjllService.getSyjllDtos(sbyjllDto);
		List<String> llids=new ArrayList<>();
		List<SbtsjlDto> sbtsjlDtos=new ArrayList<>();
		if (!CollectionUtils.isEmpty(sbyjllDtos)){
			for (SbyjllDto sbyjllDto1:sbyjllDtos){
				llids.add(sbyjllDto1.getLlid());
			}
			SbtsjlDto sbtsjlDto=new SbtsjlDto();
			sbtsjlDto.setLlids(llids);
			sbtsjlDtos=sbtsjlService.getDtoList(sbtsjlDto);
		}
		SbwxDto sbwxDto=new SbwxDto();
		sbwxDto.setSbysid(sbysDto.getSbysid());
		List<SbwxDto> sbwxDtos=sbwxService.getDtoList(sbwxDto);
		if ("sbyj".equals(request.getParameter("flag"))){
			map.put("rows",sbyjllDtos);
			map.put("total",sbyjllDtos.size());
		}else if ("sbtsjl".equals(request.getParameter("flag"))){
			map.put("rows",sbtsjlDtos);
			map.put("total",sbtsjlDtos.size());
		}else if ("sbyz".equals(request.getParameter("flag"))){
			map.put("rows",sbyzDtos);
			map.put("total",sbyzDtos.size());
		}else if ("sbjl".equals(request.getParameter("flag"))){
			map.put("rows",sbjlDtos);
			map.put("total",sbjlDtos.size());
		}else if ("sbwx".equals(request.getParameter("flag"))){
			map.put("rows",sbwxDtos);
			map.put("total",sbwxDtos.size());
		}
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 全部设备列表查看数据
	 *
	 * @param sbysDto
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/viewEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> viewEquipmentAcceptance(SbysDto sbysDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		sbysDto=sbysService.getSbysDtoById(sbysDto.getSbysid());
		SbjlDto sbjlDto=new SbjlDto();
		sbjlDto.setSbysid(sbysDto.getSbysid());
		List<SbjlDto> sbjlDtos=sbjlService.getDtoList(sbjlDto);
		SbyzDto sbyzDto=new SbyzDto();
		sbyzDto.setSbysid(sbysDto.getSbysid());
		List<SbyzDto> sbyzDtos=sbyzService.getDtoList(sbyzDto);
		SbyjllDto sbyjllDto=new SbyjllDto();
		sbyjllDto.setSbysid(sbysDto.getSbysid());
		List<SbyjllDto> sbyjllDtos=sbyjllService.getSyjllDtos(sbyjllDto);
		List<String> llids=new ArrayList<>();
		List<SbtsjlDto> sbtsjlDtos=new ArrayList<>();
		if (!CollectionUtils.isEmpty(sbyjllDtos)){
			for (SbyjllDto sbyjllDto1:sbyjllDtos){
				llids.add(sbyjllDto1.getLlid());
			}
			SbtsjlDto sbtsjlDto=new SbtsjlDto();
			sbtsjlDto.setLlids(llids);
			sbtsjlDtos=sbtsjlService.getDtoList(sbtsjlDto);
		}
		map.put("sbtsjlDtos",sbtsjlDtos);
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_DEVICE_CHECK.getCode());
		fjcfbDto.setYwid(sbysDto.getSbysid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		SbwxDto sbwxDto=new SbwxDto();
		sbwxDto.setSbysid(sbysDto.getSbysid());
		List<SbwxDto> sbwxDtos=sbwxService.getDtoList(sbwxDto);
		SbpdmxDto sbpdmxDto=new SbpdmxDto();
		sbpdmxDto.setSbysid(sbysDto.getSbysid());
		List<SbpdmxDto> sbpdmxDtos = sbpdmxService.getDtoList(sbpdmxDto);
		SbfyDto sbfyDto = new SbfyDto();
		sbfyDto.setSbysid(sbysDto.getSbysid());
		Map<String,Object> sbfyMap = sbfyService.querySbfyList(sbfyDto);
		map.put("sbfyMap",sbfyMap);
		map.put("sbpdmxDtos",sbpdmxDtos);
		map.put("sbjlDtos",sbjlDtos);
		map.put("sbyzDtos",sbyzDtos);
		map.put("sbysDto",sbysDto);
		map.put("sbyjllDtos",sbyjllDtos);
		map.put("sbwxDtos",sbwxDtos);
		map.put("fjcfbDtos",fjcfbDtos);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 关联文件
	 */
	@RequestMapping(value = "/inspectionGoods/pagedataDeviceFiles")
	@ResponseBody
	public Map<String,Object> pagedataDeviceFiles(SbglwjDto sbglwjDto){
		Map<String,Object> map=new HashMap<>();
		List<SbglwjDto> sbglwjDtos=sbglwjService.getDtoList(sbglwjDto);
		map.put("rows",sbglwjDtos);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 关联文件
	 */
	@RequestMapping(value = "/inspectionGoods/pagedataDeviceFilesOA")
	public ModelAndView pagedataDeviceFilesOA(SbglwjDto sbglwjDto){
		ModelAndView mav = new ModelAndView("storehouse/inspection/equipmentAcceptance_FileCon");
		mav.addObject("sbglwjDto",sbglwjDto);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	/**
	 * 调试记录(OA)
	 */
	@RequestMapping(value = "/inspectionGoods/pagedataDeviceShakedownOA")
	public ModelAndView pagedataDeviceShakedownOA(SbtsjlDto sbtsjlDto){
		ModelAndView mav = new ModelAndView("storehouse/inspection/equipmentAcceptance_turnoverAffirm");
		mav.addObject("sbtsjlDto",sbtsjlDto);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	/**
	 * 获取调试记录
	 */
	@RequestMapping(value = "/inspectionGoods/pagedataDeviceShakedown")
	@ResponseBody
	public Map<String,Object> pagedataDeviceShakedown(SbtsjlDto sbtsjlDto){
		Map<String,Object> map=new HashMap<>();
		List<SbtsjlDto> sbtsjlDtos=sbtsjlService.getDtoList(sbtsjlDto);
		map.put("rows",sbtsjlDtos);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 设备删除
	 */
	@RequestMapping(value = "/inspectionGoods/delEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> delEquipmentAcceptance(SbysDto sbysDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbysDto.setScry(user.getYhid());
		boolean isSuccess=sbysService.delete(sbysDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 设备移交
	 * @return
	 */
	@RequestMapping(value ="/inspectionGoods/turnoverEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> turnoverEquipmentAcceptance(SbyjllDto sbyjllDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbyjllDto.setYjsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
		UserDto userDto_sel = new UserDto();
		userDto_sel.setYhid(user.getYhid());
		UserDto userDto = userService.getZszgByYh(userDto_sel);
		sbyjllDto.setJsrzg(userDto.getYhid());
		sbyjllDto.setJsrzgmc(userDto.getZsxm());
		map.put("sbyjllDto",sbyjllDto);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 设备移交保存
	 *
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/turnoverSaveEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> turnoverSaveEquipmentAcceptance(SbyjllDto sbyjllDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		SbysDto sbysDto=new SbysDto();
		sbysDto.setXgry(user.getYhid());
		boolean isSuccess;
		try {
			isSuccess=sbyjllService.turnSaveEquipmentAcceptance(sbysDto,sbyjllDto);
			map.put("auditType", AuditTypeEnum.AUDIT_TURN_METERING.getCode());
			map.put("ywid",sbyjllDto.getLlid());
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}catch (BusinessException e){
			map.put("status", "fail");
			map.put("message", e.getMsg()!=null?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	//查看设备移交确认
//	@RequestMapping(value = "/inspectionGoods/pagedataQrlist")
//	@ResponseBody
//	public ModelAndView pagedataQrlist(SbyjqrDto sbyjqrDto){
//		ModelAndView mav = new ModelAndView("storehouse/inspection/equipmentAcceptance_turnoverAffirm");
//		mav.addObject("urlPrefix",urlPrefix);
//		mav.addObject("sbyjqrDto",sbyjqrDto);
//		return mav;
//	}
	//设备移交确认历史
//	@RequestMapping(value = "/inspectionGoods/pagedataSbyjqrList")
//	@ResponseBody
//	public Map<String,Object> pagedataSbyjqrList(SbyjqrDto sbyjqrDto) {
//		Map<String,Object> map=new HashMap<>();
//		List<SbyjqrDto> sbyjqrDtos=sbyjqrService.getDtoList(sbyjqrDto);
//		map.put("rows",sbyjqrDtos);
//		return map;
//	}
	/**
	 * 设备报废
	 * @param sbysDto
	 */
	@RequestMapping(value = "/inspectionGoods/scrapEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> scrapEquipmentAcceptance(SbysDto sbysDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		user = commonservice.getUserInfoById(user);
		sbysDto.setSqr(user.getYhid());
		sbysDto.setSqrmc(user.getZsxm());
		sbysDto.setSqbm(user.getJgid());
		sbysDto.setSqbmmc(user.getJgmc());
		Date date = new Date();
		SimpleDateFormat sqsj = new SimpleDateFormat("yyyy-MM-dd");
		sbysDto.setSqsj(sqsj.format(date));
		map.put("sbysDto",sbysDto);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 设备报废保存
	 */
	@RequestMapping(value = "/inspectionGoods/scrapSaveEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> scrapSaveEquipmentAcceptance(SbbfDto sbbfDto, HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbbfDto.setLrry(user.getYhid());
		SbysDto sbysDto=new SbysDto();
		sbysDto.setSbysid(sbbfDto.getSbysid());
		try {
			boolean isSuccess=sbbfService.scrapSaveEquipmentAcceptance(sbysDto,sbbfDto);
			map.put("auditType", AuditTypeEnum.AUDIT_DEVICE_SCRAP.getCode());
			map.put("ywid",sbbfDto.getSbbfid());
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}catch (BusinessException e){
			map.put("status", "fail");
			map.put("message", e.getMsg()!=null?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 设备报废列表
	 */
	@RequestMapping(value = "/inspectionGoods/pageGetListDeviceScarp")
	@ResponseBody
	public Map<String,Object> pageGetListDeviceScarp(SbbfDto sbbfDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		super.setCzdmList(request,map);
		User user=getLoginInfo(request);
		sbbfDto.setSqbm(user.getJgid());
		sbbfDto.setSqr(user.getYhid());
		List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
		List<SbbfDto> glrylist=sbbfService.getLeadList();
		List<SbbfDto> sybmlist=sbbfService.getDepartmentList();
		map.put("sblxList",sblxList);
		map.put("glrylist",glrylist);
		map.put("sybmlist",sybmlist);
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_SCRAP.getCode());
		if ("1".equals(sbbfDto.getSfbmxz())){
			DataPermission.addCurrentUser(sbbfDto,user);
			DataPermission.addJsDdw(sbbfDto, "sbbf", SsdwTableEnum.SBBF);
		}
		List<SbbfDto> sbbfDtos=sbbfService.getPagedDtoList(sbbfDto);
		map.put("total",sbbfDto.getTotalNumber());
		map.put("rows",sbbfDtos);
		return map;
	}
	/**
	 * 设备报废查看
	 */
	@RequestMapping(value = "/inspectionGoods/viewDeviceScarp")
	@ResponseBody
	public Map<String,Object> viewDeviceScarp(SbbfDto sbbfDto) {
		Map<String,Object> map=new HashMap<>();
		sbbfDto=sbbfService.getDtoById(sbbfDto.getSbbfid());
		map.put("sbbfDto",sbbfDto);
		return map;
	}
	/**
	 *设备报废列表提交
	 */
	@RequestMapping(value = "/inspectionGoods/submitDeviceScarp")
	@ResponseBody
	public Map<String,Object> submitDeviceScarp(SbbfDto sbbfDto) {
		Map<String,Object> map=new HashMap<>();
		sbbfDto=sbbfService.getDtoById(sbbfDto.getSbbfid());
		map.put("sbbfDto",sbbfDto);
		return map;
	}
	/**
	 *设备报废列表提交保存
	 */
	@RequestMapping(value = "/inspectionGoods/submitSaveDeviceScarp")
	@ResponseBody
	public Map<String,Object> submitSaveDeviceScarp(SbbfDto sbbfDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbbfDto.setXgry(user.getYhid());
		SbysDto sbysDto=new SbysDto();
		try {
			boolean isSuccess=sbbfService.scrapSaveEquipmentAcceptance(sbysDto,sbbfDto);
			map.put("ywid",sbbfDto.getSbbfid());
			map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_SCRAP.getCode());
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}catch (BusinessException e){
			map.put("status", "fail");
			map.put("message", e.getMsg()!=null?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 设备报废列表删除
	 */
	@RequestMapping(value = "/inspectionGoods/delDeviceScarp")
	@ResponseBody
	public Map<String,Object> delDeviceScarp(SbbfDto sbbfDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbbfDto.setScry(user.getYhid());
		boolean isSuccess=sbbfService.delDeviceScarp(sbbfDto,"delete");
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	/**
	 * 设备报废列表废弃
	 */
	@RequestMapping(value = "/inspectionGoods/discardDeviceScarp")
	@ResponseBody
	public Map<String,Object> discardDeviceScarp(SbbfDto sbbfDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbbfDto.setScry(user.getYhid());
		boolean isSuccess=sbbfService.delDeviceScarp(sbbfDto,"discard");
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?"废弃成功":"废弃失败");
		return map;
	}
	/**
	 * 设备报废审核列表
	 */
	@RequestMapping(value = "/inspectionGoods/pageGetListAuditedScrap")
	@ResponseBody
	public Map<String,Object> pageGetListAuditedScrap(SbbfDto sbbfDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		super.setCzdmList(request,map);
		// 附加委托参数
		DataPermission.addWtParam(sbbfDto);
		super.setCzdmList(request,map);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(sbbfDto.getDqshzt())) {
			DataPermission.add(sbbfDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "sbbf", "sbbfid",
					AuditTypeEnum.AUDIT_DEVICE_SCRAP);
		} else {
			DataPermission.add(sbbfDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "sbbf", "sbbfid",
					AuditTypeEnum.AUDIT_DEVICE_SCRAP);
		}
		DataPermission.addCurrentUser(sbbfDto, getLoginInfo(request));
		DataPermission.addSpDdw(sbbfDto, "sbbf", SsdwTableEnum.SBBF);
		List<SbbfDto> listMap=sbbfService.getPagedAuditDeviceScrap(sbbfDto);
		map.put("total", sbbfDto.getTotalNumber());
		map.put("rows", listMap);
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_SCRAP.getCode());
		return map;
	}
	/**
	 * 设备报废审核列表审核按钮
	 */
	@RequestMapping(value = "/inspectionGoods/auditDeviceScarp")
	@ResponseBody
	public Map<String,Object> auditDeviceScarp(SbbfDto sbbfDto) {
		Map<String,Object> map=new HashMap<>();
		sbbfDto=sbbfService.getDtoById(sbbfDto.getSbbfid());
		map.put("sbbfDto",sbbfDto);
		map.put("urlPrefix",urlPrefix);
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_SCRAP.getCode());
		return map;
	}
	/**
	 * 设备售出
	 * @param sbysDto
	 */
	@RequestMapping(value = "/inspectionGoods/soldEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> soldEquipmentAcceptance(SbysDto sbysDto){
		Map<String,Object> map=new HashMap<>();
		sbysDto.setSbzt("03");
		boolean isSuccess= sbysService.update(sbysDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message", isSuccess?"售出成功":"售出失败");
		return map;
	}
	/**
	 * 设备闲置
	 * @param sbysDto
	 */
	@RequestMapping(value = "/inspectionGoods/inactiveEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> inactiveEquipmentAcceptance(SbysDto sbysDto){
		Map<String,Object> map=new HashMap<>();
		sbysDto.setSbzt("00");
		boolean isSuccess= sbysService.update(sbysDto);
		map.put("status",isSuccess?"success":"fail");
		map.put("message", isSuccess?"闲置成功":"闲置失败");
		return map;
	}
	/**
	 * 设备计量列表
	 * @param sbjlDto
	 */
	@RequestMapping(value = "/inspectionGoods/pageGetListMetering")
	@ResponseBody
	public Map<String,Object> pageGetListMetering(SbjlDto sbjlDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		super.setCzdmList(request,map);
		User user=getLoginInfo(request);
		if ("1".equals(sbjlDto.getSfbmxz())){
			DataPermission.addCurrentUser(sbjlDto,user);
			DataPermission.addJsDdw(sbjlDto, "sbjl", SsdwTableEnum.SBJL);
		}
		List<SbjlDto> rows=sbjlService.getPagedDtoList(sbjlDto);
		List<SbjlDto> xybmList=sbjlService.getDepartmentList();
		List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
		List<SbjlDto> glryList = sbjlService.getGlryList();
		map.put("sybmList",xybmList);
		map.put("sblxList",sblxList);
		map.put("glryList",glryList);
		map.put("rows",rows);
		map.put("total",sbjlDto.getTotalNumber());
		map.put("urlPrefix",urlPrefix);
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_METERING.getCode());
		return map;
	}
	/**
	 * 设备计量列表查看
	 * @param sbjlDto
	 */
	@RequestMapping(value = "/inspectionGoods/viewMetering")
	@ResponseBody
	public Map<String,Object> viewMetering(SbjlDto sbjlDto){
		Map<String,Object> map=new HashMap<>();
		sbjlDto=sbjlService.getDtoById(sbjlDto.getSbjlid());
		SbglwjDto sbglwjDto=new SbglwjDto();
		sbglwjDto.setLb("0");
		sbglwjDto.setSbglid(sbjlDto.getSbjlid());
		List<SbglwjDto> sbglwjDtos=sbglwjService.getDtoList(sbglwjDto);
		map.put("sbjlDto",sbjlDto);
		map.put("sbglwjDtos",sbglwjDtos);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 设备计量列表提交按钮
	 *
	 * @param sbjlDto
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/submitMetering")
	@ResponseBody
	public Map<String,Object> submitMetering(SbjlDto sbjlDto) {
		Map<String,Object> map=new HashMap<>();
		map.put("sbjlDto",sbjlDto);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 设备计量列表提交保存
	 *
	 * @param sbjlDto
	 * @return
	 */
	@RequestMapping(value = "/inspectionGoods/submitSaveMetering")
	@ResponseBody
	public Map<String,Object> submitSaveMetering(SbjlDto sbjlDto,HttpServletRequest request) {
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbjlDto.setXgry(user.getYhid());
		sbjlDto.setSqr(user.getYhid());
		sbjlDto.setSqrmc(user.getZsxm());
		SbysDto sbysDto=new SbysDto();
		try {
			boolean isSuccess=sbjlService.meteringSaveEquipmentAcceptance(sbysDto,sbjlDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_METERING.getCode());
			map.put("ywid",sbjlDto.getSbjlid());
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			map.put("urlPrefix",urlPrefix);
		}catch (Exception e){
			map.put("status", "fail");
			map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_METERING.getCode());
			map.put("message", e.getMessage());
			map.put("urlPrefix",urlPrefix);
		}

		return map;
	}
	/**
	 * 设备计量列表废弃按钮
	 */
	@RequestMapping(value = "/inspectionGoods/discardMetering")
	@ResponseBody
	public Map<String, Object> discardMetering(SbjlDto sbjlDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		sbjlDto.setScry(user.getYhid());
		boolean isSuccess = sbjlService.delSaveDeviceMetering(sbjlDto,"discard");
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	/**
	 * 设备移交列表删除按钮
	 */
	@RequestMapping(value = "/inspectionGoods/delEquipmentTurnOver")
	@ResponseBody
	public Map<String, Object> delEquipmentTurnOver(SbyjllDto sbyjllDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		sbyjllDto.setScry(user.getYhid());
		boolean isSuccess = sbyjllService.delTurnOverSave(sbyjllDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	/**
	 * 设备移交列表废弃按钮
	 */
	@RequestMapping(value = "/inspectionGoods/discardEquipmentTurnOver")
	@ResponseBody
	public Map<String, Object> discardEquipmentTurnOver(SbyjllDto sbyjllDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		sbyjllDto.setScry(user.getYhid());
		boolean isSuccess = sbyjllService.delTurnOverSave(sbyjllDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	/**
	 * 	设备计量审核列表
	 * @param sbjlDto
	 * @return
	 */
	@RequestMapping("/inspectionGoods/pageGetListAuditedMetering")
	@ResponseBody
	public Map<String, Object> pageGetListAuditedMetering(SbjlDto sbjlDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 附加委托参数
		DataPermission.addWtParam(sbjlDto);
		super.setCzdmList(request,map);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(sbjlDto.getDqshzt())) {
			DataPermission.add(sbjlDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "sbjl", "sbjlid",
					AuditTypeEnum.AUDIT_DEVICE_METERING);
		} else {
			DataPermission.add(sbjlDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "sbjl", "sbjlid",
					AuditTypeEnum.AUDIT_DEVICE_METERING);
		}
		DataPermission.addCurrentUser(sbjlDto, getLoginInfo(request));
		DataPermission.addSpDdw(sbjlDto, "sbjl", SsdwTableEnum.SBJL);
		List<SbjlDto> listMap = sbjlService.getPagedAuditMetering(sbjlDto);
		map.put("total", sbjlDto.getTotalNumber());
		map.put("rows", listMap);
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_METERING.getCode());
		return map;
	}
	/**
	 * 设备计量审核按钮
	 */
	@RequestMapping(value = "/inspectionGoods/auditMetering")
	@ResponseBody
	public Map<String,Object> auditMetering(SbjlDto sbjlDto) {
		Map<String,Object> map=new HashMap<>();
		sbjlDto=sbjlService.getDtoById(sbjlDto.getSbjlid());
		map.put("sbjlDto",sbjlDto);
		map.put("urlPrefix",urlPrefix);
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_METERING.getCode());
		return map;
	}
	/**
	 * 设备验证列表
	 * @param sbyzDto
	 */
	@RequestMapping(value = "/inspectionGoods/pageGetListTesting")
	@ResponseBody
	public Map<String,Object> pageGetListTesting(SbyzDto sbyzDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		super.setCzdmList(request,map);
		User user=getLoginInfo(request);
		if ("1".equals(sbyzDto.getSfbmxz())){
			DataPermission.addCurrentUser(sbyzDto,user);
			DataPermission.addJsDdw(sbyzDto, "sbyz", SsdwTableEnum.SBYZ);
		}
		List<SbyzDto> rows=sbyzService.getPagedDtoList(sbyzDto);
		List<SbyzDto> xybmList=sbyzService.getDepartmentList(sbyzDto);
		List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
		List<SbyzDto> glryList = sbyzService.getGlryList();
		map.put("sybmList",xybmList);
		map.put("sblxList",sblxList);
		map.put("glryList",glryList);
		map.put("rows",rows);
		map.put("total",sbyzDto.getTotalNumber());
		map.put("urlPrefix",urlPrefix);
		map.put("auditType", AuditTypeEnum.AUDIT_DEVICE_TEST.getCode());
		return map;
	}
	/**
	 * 设备验证列表查看
	 * @param sbyzDto
	 */
	@RequestMapping(value = "/inspectionGoods/viewTesting")
	@ResponseBody
	public Map<String,Object> viewTesting(SbyzDto sbyzDto){
		Map<String,Object> map=new HashMap<>();
		sbyzDto=sbyzService.getDtoById(sbyzDto.getSbyzid());
		SbglwjDto sbglwjDto=new SbglwjDto();
		sbglwjDto.setLb("1");
		sbglwjDto.setSbglid(sbyzDto.getSbyzid());
		List<SbglwjDto> sbglwjDtos=sbglwjService.getDtoList(sbglwjDto);
		map.put("sbglwjDtos",sbglwjDtos);
		map.put("sbyzDto",sbyzDto);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 设备验证
	 * @return
	 */
	@RequestMapping(value ="/inspectionGoods/checkingEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> checkingEquipmentAcceptance(SbysDto sbysDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		map.put("sbysDto",sbysDto);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 设备验证保存
	 * @return
	 */
	@RequestMapping(value ="/inspectionGoods/checkingSaveEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> checkingSaveEquipmentAcceptance(SbysDto sbysDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		SbyzDto sbyzDto=new SbyzDto();
		try {
			boolean isSuccess=sbyzService.checkingSaveEquipmentAcceptance(sbysDto,sbyzDto,user);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}catch (Exception e){
			map.put("status", "fail");
			map.put("message", e.getMessage()!=null?e.getMessage():xxglService.getModelById("ICOM00002").getXxnr());
		}
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 设备验证提交
	 * @return
	 */
	@RequestMapping(value ="/inspectionGoods/submitTesting")
	@ResponseBody
	public Map<String,Object> submitTesting(SbyzDto sbyzDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		sbyzDto=sbyzService.getDtoById(sbyzDto.getSbyzid());
		map.put("sbyzDto",sbyzDto);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 设备验证提交保存
	 * @return
	 */
	@RequestMapping(value ="/inspectionGoods/submitSaveTesting")
	@ResponseBody
	public Map<String,Object> submitSaveTesting(SbyzDto sbyzDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		SbysDto sbysDto=new SbysDto();
		User user=getLoginInfo(request);
		try {
			boolean isSuccess=sbyzService.checkingSaveEquipmentAcceptance(sbysDto,sbyzDto,user);
			map.put("auditType", AuditTypeEnum.AUDIT_DEVICE_TEST.getCode());
			map.put("ywid",sbyzDto.getSbyzid());
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}catch (Exception e){
			map.put("status", "fail");
			map.put("message", e.getMessage()!=null?e.getMessage():xxglService.getModelById("ICOM00002").getXxnr());
		}
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 	设备验证审核列表
	 * @return
	 */
	@RequestMapping("/inspectionGoods/pageGetListAuditedTesting")
	@ResponseBody
	public Map<String, Object> pageGetListAuditedTesting(SbyzDto sbyzDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 附加委托参数
		DataPermission.addWtParam(sbyzDto);
		super.setCzdmList(request,map);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(sbyzDto.getDqshzt())) {
			DataPermission.add(sbyzDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "sbyz", "sbyzid",
					AuditTypeEnum.AUDIT_DEVICE_TEST);
		} else {
			DataPermission.add(sbyzDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "sbyz", "sbyzid",
					AuditTypeEnum.AUDIT_DEVICE_TEST);
		}
		DataPermission.addCurrentUser(sbyzDto, getLoginInfo(request));
		DataPermission.addSpDdw(sbyzDto, "sbyz", SsdwTableEnum.SBYZ);
		List<SbyzDto> listMap = sbyzService.getPagedAuditTesting(sbyzDto);
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_TEST.getCode());
		map.put("total", sbyzDto.getTotalNumber());
		map.put("rows", listMap);
		return map;
	}
	/**
	 * 设备验证审核按钮
	 */
	@RequestMapping(value = "/inspectionGoods/auditDeviceTest")
	@ResponseBody
	public Map<String,Object> auditDeviceTest(SbyzDto sbyzDto) {
		Map<String,Object> map=new HashMap<>();
		sbyzDto=sbyzService.getDtoById(sbyzDto.getSbyzid());
		map.put("sbyzDto",sbyzDto);
		map.put("urlPrefix",urlPrefix);
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_TEST.getCode());
		return map;
	}
	/**
	 * 设备验证列表删除
	 */
	@RequestMapping(value = "/inspectionGoods/delTesting")
	@ResponseBody
	public Map<String, Object> delTesting(SbyzDto sbyzDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		sbyzDto.setScry(user.getYhid());
		boolean isSuccess=sbyzService.delSaveDeviceTest(sbyzDto,"delete");
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	/**
	 * 设备验证列表废弃
	 */
	@RequestMapping(value = "/inspectionGoods/discardDeviceTesting")
	@ResponseBody
	public Map<String, Object> discardDeviceTesting(SbyzDto sbyzDto,HttpServletRequest request){
		//获取用户信息
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		sbyzDto.setScry(user.getYhid());
		boolean isSuccess= sbyzService.delSaveDeviceTest(sbyzDto,"discard");
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?"废弃成功":"废弃失败");
		return map;
	}
	/**
	 * 计量功能
	 */
	@RequestMapping(value ="/inspectionGoods/meteringEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> meteringEquipmentAcceptance(SbysDto sbysDto){
		Map<String,Object> map=new HashMap<>();
		map.put("sbysDto",sbysDto);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 计量保存
	 */
	@RequestMapping(value ="/inspectionGoods/meteringSaveEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> meteringSaveEquipmentAcceptance(SbysDto sbysDto,HttpServletRequest request)  {
		Map<String,Object> map=new HashMap<>();
		SbjlDto sbjlDto=new SbjlDto();
		User user=getLoginInfo(request);
		sbjlDto.setLrry(user.getYhid());
		sbjlDto.setSqr(user.getYhid());
		sbjlDto.setSqbm(user.getJgid());
		sbjlDto.setSqrmc(user.getZsxm());
		sbjlDto.setQwwcsj(sbysDto.getQwwcsj());
		try {
			boolean isSuccess=sbjlService.meteringSaveEquipmentAcceptance(sbysDto,sbjlDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_METERING.getCode());
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			map.put("urlPrefix",urlPrefix);
		}catch (Exception e){
			map.put("status", "fail");
			map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_METERING.getCode());
			map.put("message", e.getMessage());
			map.put("urlPrefix",urlPrefix);
		}

		return map;
	}
	/**
	 * 设备调整功能
	 */
	@RequestMapping(value ="/inspectionGoods/shakedownEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> shakedownEquipmentAcceptance(SbysDto sbysDto, SbtsjlDto sbtsjlDto){
		Map<String,Object> map=new HashMap<>();
		SbyjllDto sbyjllDto=new SbyjllDto();
		sbyjllDto.setSbysid(sbysDto.getSbysid());
		List<SbyjllDto> list=sbyjllService.getDtoList(sbyjllDto);
		if (CollectionUtils.isEmpty(list)){
			map.put("status","fail");
			map.put("msg","设备未转移，无需调试");
			return map;
		}
		map.put("status","success");
		map.put("sbtsjlDto",sbtsjlDto);
		map.put("sbyjDtolist",list);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 设备调整保存功能
	 */
	@RequestMapping(value ="/inspectionGoods/shakedownSaveEquipmentAcceptance")
	@ResponseBody
	public Map<String,Object> shakedownSaveEquipmentAcceptance(SbtsjlDto sbtsjlDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbtsjlDto.setTsid(StringUtil.generateUUID());
		sbtsjlDto.setLrry(user.getYhid());
		boolean isSuccess= sbtsjlService.insert(sbtsjlDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 维修/保养设备
	 */
	@RequestMapping(value ="/inspectionGoods/upkeepDevice")
	@ResponseBody
	public Map<String,Object> upkeepDevice(SbwxDto sbwxDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user = getLoginInfo(request);
		user = commonservice.getUserInfoById(user);
		sbwxDto.setSqr(user.getYhid());
		sbwxDto.setSqrmc(user.getZsxm());
		sbwxDto.setSqbm(user.getJgid());
		sbwxDto.setSqbmmc(user.getJgmc());
		sbwxDto.setSqrq(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
		map.put("sbwxDto",sbwxDto);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 维修/保养设备保存
	 */
	@RequestMapping(value ="/inspectionGoods/upkeepSaveDevice")
	@ResponseBody
	public Map<String,Object> upkeepSaveDevice(SbwxDto sbwxDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbwxDto.setSbwxid(StringUtil.generateUUID());
		sbwxDto.setLrry(user.getYhid());
		sbwxDto.setZt(StatusEnum.CHECK_NO.getCode());
		// HA-年份（4位）-日期（4位）-流水号（2位）
		String prefix = "HA-"+DateUtils.getCustomFomratCurrentDate("yyyy")+"-"+DateUtils.getCustomFomratCurrentDate("MMdd")+"-";
		String serial = sbwxService.getSbwxSerial(prefix);
		sbwxDto.setJlbh(prefix+serial);
		boolean isSuccess= sbwxService.insert(sbwxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid",sbwxDto.getSbwxid());
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_UPKEEP.getCode());
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 维修/保养列表
	 * @param sbwxDto
	 */
	@RequestMapping(value = "/inspectionGoods/pageGetListUpkeepDevice")
	@ResponseBody
	public Map<String,Object> pageGetListUpkeepDevice(SbwxDto sbwxDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user = getLoginInfo(request);
		if ("1".equals(sbwxDto.getSfbmxz())){
			DataPermission.addCurrentUser(sbwxDto,user);
			DataPermission.addJsDdw(sbwxDto, "sbwx", SsdwTableEnum.SBWX);
		}
		List<SbwxDto> rows=sbwxService.getPagedDtoList(sbwxDto);
		map.put("rows",rows);
		map.put("total",sbwxDto.getTotalNumber());
		map.put("urlPrefix",urlPrefix);
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_UPKEEP.getCode());
		List<SbwxDto> xsrbmList = sbwxService.getDepartmentList();
		List<SbwxDto> glryList = sbwxService.getGlryList();
		map.put("xsrbmList",xsrbmList);
		map.put("glryList",glryList);
		map.put("sblxlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode()));
		map.put("qwfslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DECONTAMINATION_METHOD.getCode()));
		super.setCzdmList(request,map);
		return map;
	}
	/**
	 * 维修/保养设备 查看
	 */
	@RequestMapping(value ="/inspectionGoods/viewUpkeepDevice")
	@ResponseBody
	public Map<String,Object> viewUpkeepDevice(SbwxDto sbwxDto){
		Map<String,Object> map=new HashMap<>();
		sbwxDto = sbwxService.getDtoById(sbwxDto.getSbwxid());
		map.put("sbwxDto",sbwxDto);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 维修/保养设备 修改
	 */
	@RequestMapping(value ="/inspectionGoods/modUpkeepDevice")
	@ResponseBody
	public Map<String,Object> modUpkeepDevice(SbwxDto sbwxDto){
		Map<String,Object> map=new HashMap<>();
		sbwxDto = sbwxService.getDtoById(sbwxDto.getSbwxid());
		map.put("sbwxDto",sbwxDto);
		map.put("qwfslist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DECONTAMINATION_METHOD.getCode()));
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 维修/保养设备 修改保存
	 */
	@RequestMapping(value ="/inspectionGoods/modSaveUpkeepDevice")
	@ResponseBody
	public Map<String,Object> modSaveUpkeepDevice(SbwxDto sbwxDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbwxDto.setXgry(user.getYhid());
		boolean isSuccess= sbwxService.update(sbwxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 维修/保养设备 提交
	 */
	@RequestMapping(value ="/inspectionGoods/submitUpkeepDevice")
	@ResponseBody
	public Map<String,Object> submitUpkeepDevice(SbwxDto sbwxDto){
		return this.modUpkeepDevice(sbwxDto);
	}
	/**
	 * 维修/保养设备 修改保存
	 */
	@RequestMapping(value ="/inspectionGoods/submitSaveUpkeepDevice")
	@ResponseBody
	public Map<String,Object> submitSaveUpkeepDevice(SbwxDto sbwxDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbwxDto.setXgry(user.getYhid());
		boolean isSuccess= sbwxService.update(sbwxDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid",sbwxDto.getSbwxid());
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_UPKEEP.getCode());
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 维修/保养设备 删除
	 */
	@RequestMapping(value ="/inspectionGoods/delUpkeepDevice")
	@ResponseBody
	public Map<String,Object> delUpkeepDevice(SbwxDto sbwxDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbwxDto.setScry(user.getYhid());
		boolean isSuccess;
		try {
			isSuccess = sbwxService.delUpkeepDevice(sbwxDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
			map.put("urlPrefix",urlPrefix);
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
			map.put("urlPrefix",urlPrefix);
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
			map.put("urlPrefix",urlPrefix);
		}
		return map;
	}
	/**
	 * 维修/保养设备 废弃
	 */
	@RequestMapping(value ="/inspectionGoods/discardUpkeepDevice")
	@ResponseBody
	public Map<String,Object> discardUpkeepDevice(SbwxDto sbwxDto, HttpServletRequest request){
		return this.delUpkeepDevice(sbwxDto,request);
	}
	/**
	 * 	维修/保养审核列表
	 * @param sbwxDto
	 * @return
	 */
	@RequestMapping("/inspectionGoods/pageGetListAudiedUpkeepDevice")
	@ResponseBody
	public Map<String, Object> pageGetListAudiedUpkeepDevice(SbwxDto sbwxDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 附加委托参数
		DataPermission.addWtParam(sbwxDto);
		super.setCzdmList(request,map);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(sbwxDto.getDqshzt())) {
			DataPermission.add(sbwxDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "sbwx", "sbwxid",
					AuditTypeEnum.AUDIT_DEVICE_UPKEEP);
		} else {
			DataPermission.add(sbwxDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "sbwx", "sbwxid",
					AuditTypeEnum.AUDIT_DEVICE_UPKEEP);
		}
		DataPermission.addCurrentUser(sbwxDto, getLoginInfo(request));
		DataPermission.addSpDdw(sbwxDto, "sbwx", SsdwTableEnum.SBWX);
		List<SbwxDto> sbwxDtos = sbwxService.getPagedAuditUpkeepDevice(sbwxDto);
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_UPKEEP.getCode());
		map.put("total", sbwxDto.getTotalNumber());
		map.put("rows", sbwxDtos);
		return map;
	}

	/**
	 * 维修保养打印
	 * @param sbwxDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/inspectionGoods/maintenanceprintDevice")
	@ResponseBody
	public Map<String,Object> maintenanceprintDevice(SbwxDto sbwxDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		List<SbwxDto> sbwxDtos=sbwxService.getDtoList(sbwxDto);
		List<JcsjDto> qwfsList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DECONTAMINATION_METHOD.getCode());
		for (JcsjDto jcsjDto:qwfsList){
			if ("未擦拭".equals(jcsjDto.getCsmc())){
				qwfsList.remove(jcsjDto);
				break;
			}
		}
		//根据基础数据获取打印信息
		JcsjDto jcsj = new JcsjDto();
		jcsj.setCsdm("sb_wxdj");
		jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
		jcsj = jcsjService.getDto(jcsj);
		map.put("wjbh",StringUtil.isNotBlank(jcsj.getCsmc())?jcsj.getCsmc():"");
		map.put("sxrq",StringUtil.isNotBlank(jcsj.getCskz1())?jcsj.getCskz1():"");
		map.put("bbh",StringUtil.isNotBlank(jcsj.getCskz1())?jcsj.getCskz1():"");
		map.put("sbwxDtos",sbwxDtos);
		map.put("qwfsList",qwfsList);
		map.put("applicationurl",applicationurl);
		return map;
	}
	/**
	 * 故障报修打印
	 * @param sbwxDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="/inspectionGoods/repairreportprintDevice")
	@ResponseBody
	public Map<String,Object> repairreportprintDevice(SbwxDto sbwxDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		List<SbwxDto> sbwxDtos=sbwxService.getDtoList(sbwxDto);
		List<JcsjDto> qwfsList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DECONTAMINATION_METHOD.getCode());
		for (JcsjDto jcsjDto:qwfsList){
			if ("未擦拭".equals(jcsjDto.getCsmc())){
				qwfsList.remove(jcsjDto);
				break;
			}
		}
		//根据基础数据获取打印信息
		JcsjDto jcsj = new JcsjDto();
		jcsj.setCsdm("sb_gzbx");
		jcsj.setJclb(BasicDataTypeEnum.DOCUMENTNUM.getCode());
		jcsj = jcsjService.getDto(jcsj);
		map.put("wjbh",StringUtil.isNotBlank(jcsj.getCsmc())?jcsj.getCsmc():"");
		map.put("sbwxDtos",sbwxDtos);
		map.put("qwfsList",qwfsList);
		map.put("applicationurl",applicationurl);
		return map;
	}

		/**
	 * 维修/保养设备 审核
	 */
	@RequestMapping(value ="/inspectionGoods/auditUpkeepDevice")
	@ResponseBody
	public Map<String,Object> auditUpkeepDevice(SbwxDto sbwxDto){
		return this.modUpkeepDevice(sbwxDto);
	}
    /**
     * 退库申请 设备
     */
    @RequestMapping(value ="/inspectionGoods/stockreturnDevice")
    @ResponseBody
    public Map<String,Object> stockreturnDevice(SbtkDto sbtkDto, HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        User user = getLoginInfo(request);
        user = commonservice.getUserInfoById(user);
        sbtkDto.setSqr(user.getYhid());
        sbtkDto.setSqrmc(user.getZsxm());
        sbtkDto.setSqbm(user.getJgid());
        sbtkDto.setSqbmmc(user.getJgmc());
        sbtkDto.setSqrq(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
        map.put("sbtkDto",sbtkDto);
        map.put("urlPrefix",urlPrefix);
        return map;
    }
    /**
     * 退库申请设备保存
     */
    @RequestMapping(value ="/inspectionGoods/stockreturnSaveDevice")
    @ResponseBody
    public Map<String,Object> stockreturnSaveDevice(SbtkDto sbtkDto, HttpServletRequest request){
        Map<String,Object> map=new HashMap<>();
        User user=getLoginInfo(request);
		sbtkDto.setSbtkid(StringUtil.generateUUID());
		sbtkDto.setLrry(user.getYhid());
		sbtkDto.setZt(StatusEnum.CHECK_NO.getCode());
        boolean isSuccess= sbtkService.insert(sbtkDto);
        map.put("status", isSuccess?"success":"fail");
        map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        map.put("ywid",sbtkDto.getSbtkid());
        map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_STOCKRETURN.getCode());
        map.put("urlPrefix",urlPrefix);
        return map;
    }
	/**
	 * 退库申请设备 查看
	 */
	@RequestMapping(value ="/inspectionGoods/viewStockreturnDevice")
	@ResponseBody
	public Map<String,Object> viewStockreturnDevice(SbtkDto sbtkDto){
		Map<String,Object> map=new HashMap<>();
		sbtkDto = sbtkService.getDtoById(sbtkDto.getSbtkid());
		map.put("sbtkDto",sbtkDto);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 *	退库申请列表
	 * @param sbtkDto
	 */
	@RequestMapping(value = "/inspectionGoods/pageGetListStockreturnDevice")
	@ResponseBody
	public Map<String,Object> pageGetListStockreturnDevice(SbtkDto sbtkDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user = getLoginInfo(request);
		if ("1".equals(sbtkDto.getSfbmxz())){
			DataPermission.addCurrentUser(sbtkDto,user);
			DataPermission.addJsDdw(sbtkDto, "sbtk", SsdwTableEnum.SBTK);
		}
		List<SbtkDto> rows=sbtkService.getPagedDtoList(sbtkDto);
		map.put("rows",rows);
		map.put("total",sbtkDto.getTotalNumber());
		map.put("urlPrefix",urlPrefix);
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_STOCKRETURN.getCode());
		List<SbtkDto> xsrbmList = sbtkService.getDepartmentList();
		List<SbtkDto> glryList = sbtkService.getGlryList();
		map.put("xsrbmList",xsrbmList);
		map.put("glryList",glryList);
		map.put("sblxlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode()));
		super.setCzdmList(request,map);
		return map;
	}
	/**
	 * 退库申请设备 修改
	 */
	@RequestMapping(value ="/inspectionGoods/modStockreturnDevice")
	@ResponseBody
	public Map<String,Object> modStockreturnDevice(SbtkDto sbtkDto){
		Map<String,Object> map=new HashMap<>();
		sbtkDto = sbtkService.getDtoById(sbtkDto.getSbtkid());
		map.put("sbtkDto",sbtkDto);
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 退库申请设备 修改保存
	 */
	@RequestMapping(value ="/inspectionGoods/modSaveStockreturnDevice")
	@ResponseBody
	public Map<String,Object> modSaveStockreturnDevice(SbtkDto sbtkDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbtkDto.setXgry(user.getYhid());
		boolean isSuccess= sbtkService.update(sbtkDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 退库申请设备 提交
	 */
	@RequestMapping(value ="/inspectionGoods/submitStockreturnDevice")
	@ResponseBody
	public Map<String,Object> submitStockreturnDevice(SbtkDto sbtkDto){
		return this.modStockreturnDevice(sbtkDto);
	}
	/**
	 * 退库申请设备 提交保存
	 */
	@RequestMapping(value ="/inspectionGoods/submitSaveStockreturnDevice")
	@ResponseBody
	public Map<String,Object> submitSaveStockreturnDevice(SbtkDto sbtkDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbtkDto.setXgry(user.getYhid());
		boolean isSuccess= sbtkService.update(sbtkDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("ywid",sbtkDto.getSbtkid());
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_STOCKRETURN.getCode());
		map.put("urlPrefix",urlPrefix);
		return map;
	}
	/**
	 * 	退库申请审核列表
	 * @param sbtkDto
	 * @return
	 */
	@RequestMapping("/inspectionGoods/pageGetListAudiedStockreturnDevice")
	@ResponseBody
	public Map<String, Object> pageGetListAudiedStockreturnDevice(SbtkDto sbtkDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 附加委托参数
		DataPermission.addWtParam(sbtkDto);
		super.setCzdmList(request,map);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(sbtkDto.getDqshzt())) {
			DataPermission.add(sbtkDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "sbtk", "sbtkid",
					AuditTypeEnum.AUDIT_DEVICE_STOCKRETURN);
		} else {
			DataPermission.add(sbtkDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "sbtk", "sbtkid",
					AuditTypeEnum.AUDIT_DEVICE_STOCKRETURN);
		}
		DataPermission.addCurrentUser(sbtkDto, getLoginInfo(request));
		DataPermission.addSpDdw(sbtkDto, "sbtk", SsdwTableEnum.SBTK);
		List<SbtkDto> sbtkDtos = sbtkService.getPagedAuditStockreturnDevice(sbtkDto);
		map.put("auditType",AuditTypeEnum.AUDIT_DEVICE_STOCKRETURN.getCode());
		map.put("total", sbtkDto.getTotalNumber());
		map.put("rows", sbtkDtos);
		return map;
	}
	/**
	 * 退库申请设备 审核
	 */
	@RequestMapping(value ="/inspectionGoods/auditStockreturnDevice")
	@ResponseBody
	public Map<String,Object> auditStockreturnDevice(SbtkDto sbtkDto){
		return this.modStockreturnDevice(sbtkDto);
	}
	/**
	 * 退库申请设备 删除
	 */
	@RequestMapping(value ="/inspectionGoods/delStockreturnDevice")
	@ResponseBody
	public Map<String,Object> delStockreturnDevice(SbtkDto sbtkDto, HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		sbtkDto.setScry(user.getYhid());
		boolean isSuccess;
		try {
			isSuccess = sbtkService.delStockreturnDevice(sbtkDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr() : xxglService.getModelById("ICOM00004").getXxnr());
			map.put("urlPrefix",urlPrefix);
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
			map.put("urlPrefix",urlPrefix);
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
			map.put("urlPrefix",urlPrefix);
		}
		return map;
	}
	/**
	 * 退库申请设备 废弃
	 */
	@RequestMapping(value ="/inspectionGoods/discardStockreturnDevice")
	@ResponseBody
	public Map<String,Object> discardStockreturnDevice(SbtkDto sbtkDto, HttpServletRequest request){
		return this.delStockreturnDevice(sbtkDto,request);
	}
	/**
	 * 关联设备
	 * @param sbysDto
	 * @return
	 * @author yao
	 */
	@RequestMapping("/inspectionGoods/pagedataChooseDevice")
	public ModelAndView pagedataChooseDevice(SbysDto sbysDto,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_glsb");
		mav.addObject("cskz2", request.getParameter("cskz2"));
		mav.addObject("url", "/inspectionGoods/inspectionGoods/pagedataGetChooseDevice");
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	/**
	 * 选择关联信息
	 * @param sbysDto
	 */
	@RequestMapping(value = "/inspectionGoods/pagedataGetChooseDevice")
	@ResponseBody
	public Map<String,Object> pagedataGetChooseDevice(SbysDto sbysDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		String cskz2 = request.getParameter("cskz2");
		List<SbysDto> rows;
		if ("0".equals(cskz2)){
			rows = sbysService.getPagedDeviceInfoForJl(sbysDto);
		}else if ("1".equals(cskz2)){
			rows = sbysService.getPagedDeviceInfoYZ(sbysDto);
		}else {
			rows = sbysService.getPagedEquipmentList(sbysDto);
		}
		map.put("rows",rows);
		map.put("total", sbysDto.getTotalNumber());
		return map;
	}
	/**
	 * 设备新增
	 * @return
	 */
	@RequestMapping("/equipment/addEquipment")
	@ResponseBody
	public Map<String,Object> addEquipment(SbysDto sbysDto,HttpServletRequest request) {
		Map<String,Object> map = new HashMap<>();
		List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
		List<JcsjDto> qwfsList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DECONTAMINATION_METHOD.getCode());
		// 设置默认创建日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		sbysDto.setYsrq(sdf.format(date));
		User user = getLoginInfo(request);
		sbysDto.setGlry(user.getYhid());
		sbysDto.setGlrymc(user.getZsxm());
		sbysDto.setYsr(user.getYhid());
		sbysDto.setYsrmc(user.getZsxm());
		map.put("sbysDto", sbysDto);
		map.put("sblxList", sblxList);
		map.put("qwfsList", qwfsList);
		return map;
	}
	/**
	 * 设备新增保存
	 *
	 * @param sbysDto
	 * @return
	 */
	@RequestMapping("/equipment/addSaveEquipment")
	@ResponseBody
	public Map<String, Object> addSaveDeviceCheck(SbysDto sbysDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		List<String> gdzcbhs = sbysService.getDtoByGdzcbh(sbysDto);
		if (!CollectionUtils.isEmpty(gdzcbhs)  && !"/".equals(sbysDto.getGdzcbh())){
			map.put("status", "fail");
			map.put("message", "固定资产编号或设备编号重复！");
			return map;
		}
		User user = getLoginInfo(request);
		sbysDto.setLrry(user.getYhid());
		sbysDto.setZt(StatusEnum.CHECK_PASS.getCode());
		sbysDto.setTzzt("1");
		boolean isSuccess = sbysService.insertDto(sbysDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 设备修改
	 * @return
	 */
	@RequestMapping("/equipment/modEquipment")
	@ResponseBody
	public Map<String, Object> modEquipment(SbysDto sbysDto) {
		Map<String, Object> map = new HashMap<>();
		List<JcsjDto> sblxList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.EQUIPMENT_TYPE.getCode());
		List<JcsjDto> qwfsList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DECONTAMINATION_METHOD.getCode());
		sbysDto = sbysService.getDto(sbysDto);
		map.put("sbysDto", sbysDto);
		map.put("sblxList", sblxList);
		map.put("qwfsList", qwfsList);
		return map;
	}
	/**
	 * 设备修改保存
	 *
	 * @param sbysDto
	 * @return
	 */
	@RequestMapping("/equipment/modSaveEquipment")
	@ResponseBody
	public Map<String, Object> modSaveEquipment(SbysDto sbysDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		sbysDto.setXgry(user.getYhid());
		List<String> gdzcbhs = sbysService.getDtoByGdzcbh(sbysDto);
		if (!CollectionUtils.isEmpty(gdzcbhs)  && !"/".equals(sbysDto.getGdzcbh())){
			map.put("status", "fail");
			map.put("message", "固定资产编号或设备编号重复！");
			return map;
		}
		boolean isSuccess = sbysService.updatePageEvent(sbysDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
