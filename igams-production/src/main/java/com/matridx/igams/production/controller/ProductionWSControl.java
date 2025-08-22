package com.matridx.igams.production.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.CheckControl;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.DepartmentDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.production.dao.entities.CpxqjhDto;
import com.matridx.igams.production.dao.entities.GlwjxxDto;
import com.matridx.igams.production.dao.entities.HtfkmxDto;
import com.matridx.igams.production.dao.entities.HtfkqkDto;
import com.matridx.igams.production.dao.entities.HtglDto;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.dao.entities.QgqxglDto;
import com.matridx.igams.production.dao.entities.QgqxmxDto;
import com.matridx.igams.production.dao.entities.WjglDto;
import com.matridx.igams.production.dao.entities.WjssdwDto;
import com.matridx.igams.production.dao.entities.WlglDto;
import com.matridx.igams.production.dao.entities.WlgllsbDto;
import com.matridx.igams.production.dao.entities.XqjhmxDto;
import com.matridx.igams.production.dao.entities.YxhtDto;
import com.matridx.igams.production.dao.entities.ZlxyDto;
import com.matridx.igams.production.dao.entities.ZlxymxDto;
import com.matridx.igams.production.service.svcinterface.ICpxqjhService;
import com.matridx.igams.production.service.svcinterface.IDdbxglService;
import com.matridx.igams.production.service.svcinterface.IGlwjxxService;
import com.matridx.igams.production.service.svcinterface.IHtfkmxService;
import com.matridx.igams.production.service.svcinterface.IHtfkqkService;
import com.matridx.igams.production.service.svcinterface.IHtglService;
import com.matridx.igams.production.service.svcinterface.IHtmxService;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.production.service.svcinterface.IQgqxglService;
import com.matridx.igams.production.service.svcinterface.IQgqxmxService;
import com.matridx.igams.production.service.svcinterface.ISbbfService;
import com.matridx.igams.production.service.svcinterface.ISbyjllService;
import com.matridx.igams.production.service.svcinterface.IWjglService;
import com.matridx.igams.production.service.svcinterface.IWjqxService;
import com.matridx.igams.production.service.svcinterface.IWjssdwService;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.igams.production.service.svcinterface.IWlgllsbService;
import com.matridx.igams.production.service.svcinterface.IXqjhmxService;
import com.matridx.igams.production.service.svcinterface.IYsglService;
import com.matridx.igams.production.service.svcinterface.IYxhtService;
import com.matridx.igams.production.service.svcinterface.IZlxyService;
import com.matridx.igams.production.service.svcinterface.IZlxymxService;
import com.matridx.igams.storehouse.dao.entities.CkmxDto;
import com.matridx.igams.storehouse.dao.entities.FhglDto;
import com.matridx.igams.storehouse.dao.entities.FhmxDto;
import com.matridx.igams.storehouse.dao.entities.HwllxxDto;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.entities.JcjyglDto;
import com.matridx.igams.storehouse.dao.entities.JcjymxDto;
import com.matridx.igams.storehouse.dao.entities.JcjyxxDto;
import com.matridx.igams.storehouse.dao.entities.LlglDto;
import com.matridx.igams.storehouse.dao.entities.WlxxDto;
import com.matridx.igams.storehouse.dao.entities.XsglDto;
import com.matridx.igams.storehouse.dao.entities.XsmxDto;
import com.matridx.igams.storehouse.dao.entities.XzllmxDto;
import com.matridx.igams.storehouse.dao.entities.XzqgqrglDto;
import com.matridx.igams.storehouse.dao.entities.XzqgqrmxDto;
import com.matridx.igams.storehouse.service.svcinterface.ICkmxService;
import com.matridx.igams.storehouse.service.svcinterface.IFhglService;
import com.matridx.igams.storehouse.service.svcinterface.IFhmxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwllmxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwllxxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IJcjyglService;
import com.matridx.igams.storehouse.service.svcinterface.IJcjymxService;
import com.matridx.igams.storehouse.service.svcinterface.IJcjyxxService;
import com.matridx.igams.storehouse.service.svcinterface.ILlglService;
import com.matridx.igams.storehouse.service.svcinterface.ISbwxService;
import com.matridx.igams.storehouse.service.svcinterface.IWlxxService;
import com.matridx.igams.storehouse.service.svcinterface.IXsglService;
import com.matridx.igams.storehouse.service.svcinterface.IXsmxService;
import com.matridx.igams.storehouse.service.svcinterface.IXzllglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzllmxService;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgfkglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgqrglService;
import com.matridx.igams.storehouse.service.svcinterface.IXzqgqrmxService;
import com.matridx.igams.warehouse.dao.entities.GfjxglDto;
import com.matridx.igams.warehouse.dao.entities.GfjxmxDto;
import com.matridx.igams.warehouse.dao.entities.GfpjmxDto;
import com.matridx.igams.warehouse.dao.entities.GfyzmxDto;
import com.matridx.igams.warehouse.dao.entities.GysxxDto;
import com.matridx.igams.warehouse.service.svcinterface.IGfjxglService;
import com.matridx.igams.warehouse.service.svcinterface.IGfjxmxService;
import com.matridx.igams.warehouse.service.svcinterface.IGfpjbService;
import com.matridx.igams.warehouse.service.svcinterface.IGfpjmxService;
import com.matridx.igams.warehouse.service.svcinterface.IGfyzglService;
import com.matridx.igams.warehouse.service.svcinterface.IGfyzmxService;
import com.matridx.igams.warehouse.service.svcinterface.IGysxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Controller
@RequestMapping(value ="/ws")
public class ProductionWSControl {
	@Autowired
	IWlglService wlglService;
	@Autowired
	IWlgllsbService wlgllsbService;
	@Autowired
	ICpxqjhService cpxqjhService;
	@Autowired
	ILlglService llglService;
	@Autowired
	private IHwllxxService hwllxxService;
	@Autowired
	IHwllmxService hwllmxService;
	@Autowired
	private IXqjhmxService xqjhmxService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IWjglService wjglService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IWjqxService wjqxService;
	@Autowired
	IWjssdwService wjssdwService;
	@Autowired
	ICommonService commonService;
	@Autowired
	IQgglService qgglService;
	@Autowired
	IQgmxService qgmxService;
	@Autowired
	IGysxxService gysxxService;
	@Autowired
	IXzqgqrglService xzqgqrglService;
	@Autowired
	IXzqgfkglService xzqgfkglService;
	@Autowired
	IXzqgqrmxService xzqgqrmxService;
	@Autowired
	ICommonDao commonDao;
	@Autowired
	IFhglService fhglService;
	@Autowired
	IFhmxService fhmxService;
	
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IHtglService htglService;
	@Autowired
	IHtmxService htmxService;
	@Autowired
	IDdfbsglService ddfbsglService;
	@Autowired
	IQgqxglService qgqxglService;
	@Autowired
	IQgqxmxService qgqxmxService;
	@Autowired
	IDdspglService ddspglService;
	@Autowired
	IShlcService shlcService;
	@Autowired
	IShxxService shxxService;
	@Autowired
	IShgcService shgcService;
	@Autowired
	IHtfkqkService htfkqkService;
	@Autowired
	IHwxxService hwxxService;
	@Autowired
	IHtfkmxService htfkmxService;
	@Autowired
	IXzllmxService xzllmxService;
	@Autowired
	IXzllglService xzllglService;
	@Autowired
	IDdbxglService ddbxglService;
	@Autowired
	IJcjyglService jcjyglServicel;
	@Autowired
	IJcjyxxService jcjyxxService;
	@Autowired
	IJcjymxService jcjymxService;
	@Autowired
	IXsglService xsglService;
	@Autowired
	IXsmxService xsmxService;
	@Autowired
	IWlxxService wlxxService;
	@Autowired
	IZlxyService zlxyService;
	@Autowired
	IZlxymxService zlxymxService;
	@Autowired
	IYxhtService yxhtService;
	@Autowired
	ISbyjllService sbyjllService;
	@Autowired
	ISbbfService sbbfService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	ISbwxService wxbyService;
	@Autowired
	IGlwjxxService glwjxxService;
	@Autowired
	ICkmxService ckmxService;
	@Autowired
	IGfyzglService gfyzglService;
	@Autowired
	IGfyzmxService gfyzmxService;
	@Autowired
	IGfpjbService gfpjbService;
	@Autowired
	IGfpjmxService gfpjmxService;
	@Autowired
	IGfjxglService gfjxglService;
	@Autowired
	IGfjxmxService gfjxmxService;
	@Autowired
	IYsglService ysglService;
	private final Logger logger = LoggerFactory.getLogger(CheckControl.class);
	private final Logger log = LoggerFactory.getLogger(ProductionWSControl.class);
	/**
	 * 物料修改审核
	 */
	@RequestMapping(value = "/production/materiel/modMater")
	public ModelAndView modMater(WlglDto wlglDto){
		ModelAndView mav = new ModelAndView("production/materiel/mater_edit");
		WlglDto t_ybglDto = wlglService.getDto(wlglDto);
		t_ybglDto.setFormAction("mod");
		mav.addObject("wlglDto", t_ybglDto);
		
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.MATERIEL_TYPE,BasicDataTypeEnum.MATERIEL_SAFE_TYPE});
		mav.addObject("wlaqlblist", jclist.get(BasicDataTypeEnum.MATERIEL_SAFE_TYPE.getCode()));
		
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(t_ybglDto.getWllb());
		List<JcsjDto> zlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		
		mav.addObject("wllblist", jclist.get(BasicDataTypeEnum.MATERIEL_TYPE.getCode()));
		mav.addObject("wlzlblist", zlbList);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 物料查看
	 */
	@RequestMapping(value = "/production/materiel/viewMateriel")
	public ModelAndView viewMateriel(WlglDto wlglDto){
		ModelAndView mav = new ModelAndView("production/materiel/mater_wsview");
		WlglDto t_ybglDto = wlglService.getDtoById(wlglDto.getWlid());
		mav.addObject("wlglDto", t_ybglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 物料临时修改提交页面
	 */
	@RequestMapping(value = "/production/materiel/modMaterTemp")
	public ModelAndView modMaterTemp(WlgllsbDto wlgllsbDto){
		ModelAndView mav = new ModelAndView("production/materiel/materTemp_edit");
		//获得物料管理表Dto
		WlgllsbDto tt_wlgllsbDto = new WlgllsbDto();
		tt_wlgllsbDto.setLsid(wlgllsbDto.getWlid());
		WlgllsbDto t_wlgllsbDto = wlgllsbService.getDto(tt_wlgllsbDto);
		t_wlgllsbDto.setFormAction("mod");

		mav.addObject("wlgllsbDto", t_wlgllsbDto);

		//此处查询物料表的数据，用于和传入修改数据进行对比
		WlglDto wlglDto = wlglService.getDtoById(t_wlgllsbDto.getWlid());
		mav.addObject("wlglDto", wlglDto);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.MATERIEL_TYPE, BasicDataTypeEnum.MATERIEL_SUBTYPE_COLLECTIVELY,BasicDataTypeEnum.MATERIEL_SAFE_TYPE});
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(t_wlgllsbDto.getWllb());
		List<JcsjDto> zlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		mav.addObject("wllblist", jclist.get(BasicDataTypeEnum.MATERIEL_TYPE.getCode()));
		mav.addObject("wlzlblist", zlbList);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("wlzlbtclist", jclist.get(BasicDataTypeEnum.MATERIEL_SUBTYPE_COLLECTIVELY.getCode()));
		mav.addObject("wlaqlblist", jclist.get(BasicDataTypeEnum.MATERIEL_SAFE_TYPE.getCode()));
		mav.addObject("modbj","1");
		return mav;
	}

	/**
	 * 文件修改页面
	 */
	@RequestMapping(value = "/production/document/modDocument")
	public ModelAndView modDocument(WjglDto wjglDto){
		ModelAndView mav = new ModelAndView("production/document/document_editws");
		mav.addObject("urlPrefix", urlPrefix);
		WjglDto t_wjglDto = wjglService.modDocument(wjglDto);
		if(t_wjglDto == null){
			mav.addObject("status", "fail");
			mav.addObject("message", xxglService.getModelById("WCOM_DOC00001").getXxnr());
			mav.addObject("wjglDto", wjglDto);
			return mav;
		}
		t_wjglDto.setFwbj("/ws");
		mav.addObject("wjglDto", t_wjglDto);
		//根据文件id查询所属单位信息
		WjssdwDto wjssdwDto=new WjssdwDto();
		wjssdwDto.setWjid(wjglDto.getWjid());
		WjssdwDto wjssdwxx=wjssdwService.getWjssdwByWjid(wjssdwDto);
		mav.addObject("departmentDto", wjssdwxx==null?wjssdwDto:wjssdwxx);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DOCUMENT_TYPE});
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(t_wjglDto.getWjfl());
		List<JcsjDto> wjlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		mav.addObject("wjfllist", jclist.get(BasicDataTypeEnum.DOCUMENT_TYPE.getCode()));
		mav.addObject("wjlblist", wjlbList);

/*		//获取权限列表
	    Map<String,List<WjglDto>> permissionMap = wjqxService.getPermission(wjglDto,request);
	    //查询查看权限角色
	    List<WjglDto> viewList = permissionMap.get("viewList");
	    mav.addObject("viewList", viewList);
	    //查询学习权限角色
	    List<WjglDto> studyList = permissionMap.get("studyList");
	    mav.addObject("studyList", studyList);
	    //查询没有学习、查看权限角色
	    List<WjglDto> t_wjglList = permissionMap.get("t_wjglList");
	    mav.addObject("t_wjgllist", t_wjglList);
	    //查询下载权限角色
	    List<WjglDto> downloadList = permissionMap.get("downloadList");
	    mav.addObject("downloadList", downloadList);
	    //查询没有下载权限角色
	    List<WjglDto> undownloadList = permissionMap.get("undownloadList");
	    mav.addObject("undownloadList", undownloadList);*/
	    
		//根据文件ID查询附件表信息
		List<FjcfbDto> fjcfbDtos = wjglService.selectFjcfbDtoByWjid(wjglDto.getWjid());
		mav.addObject("fjcfbDtos",fjcfbDtos);
		mav.addObject("urlPrefix", urlPrefix);
		if(!CollectionUtils.isEmpty(fjcfbDtos)) {
			String wjm = fjcfbDtos.get(0).getWjm();
			String fjid = fjcfbDtos.get(0).getFjid();
			mav.addObject("audit_wjm", wjm);
			try {
				String sign = URLEncoder.encode(commonService.getSign(wjm), StandardCharsets.UTF_8);
				mav.addObject("audit_sign", sign);
				sign = URLEncoder.encode(commonService.getSign(fjid), StandardCharsets.UTF_8);
				mav.addObject("audit_wssign", sign);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
				mav.addObject("audit_sign", "");
			}
		}else {
			mav.addObject("audit_wjm", "");
			mav.addObject("audit_sign", "");
		}
		List<User> xtyhlist=commonDao.getUserList();
		mav.addObject("xtyhlist", xtyhlist);
		return mav;
	}

	/**
	 * 选择机构页面
	 */
	@RequestMapping("/production/department/Departmentlist")
	public ModelAndView DepartmentList(DepartmentDto departmentDto){
		ModelAndView mav = new ModelAndView("common/department/list_department");
		mav.addObject("departmentDto", departmentDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 选择机构列表
	 */
	@RequestMapping("/production/department/pagedataListDepartment")
	@ResponseBody
	public Map<String, Object> listDepartment(DepartmentDto departmentDto, HttpServletRequest request){
		List<DepartmentDto> t_List = commonService.getPagedDepartmentList(departmentDto.getPrefix(), departmentDto, request);
		Map<String,Object> result = new HashMap<>();
		result.put("total", departmentDto.getTotalNumber());
		result.put("rows", t_List);
		result.put("urlPrefix",urlPrefix);
		return result;
	}
	
	
	/**
	 * 查看请购明细
	 */
	@RequestMapping("/production/purchase/viewAuditPurchase")
	public ModelAndView viewAuditPurchase(QgglDto qgglDto) {
		ModelAndView mav=new ModelAndView("purchase/purchase/purchase_auditView");
		QgglDto qgglDto_t=qgglService.getDto(qgglDto);
		QgmxDto qgmxDto=new QgmxDto();
		qgmxDto.setQgid(qgglDto.getQgid());
		List<QgmxDto> qgmxlist=qgmxService.getQgmxList(qgmxDto);
		BigDecimal zjg=new BigDecimal("0");
		DecimalFormat df4 = new DecimalFormat("#,##0.00");
		if(!CollectionUtils.isEmpty(qgmxlist)) {
			for (QgmxDto dto : qgmxlist) {
				String jg = dto.getJg();
				String sl = dto.getSl();
				if (StringUtil.isNotBlank(jg) && StringUtil.isNotBlank(sl)) {
					zjg = zjg.add(new BigDecimal(jg).multiply(new BigDecimal(sl)));
				}
			}
		}
		zjg=zjg.setScale(2, RoundingMode.UP);
		String t_zjg=df4.format(zjg);//千位符分隔
		mav.addObject("qgglDto", qgglDto_t);
		qgglDto_t.setFwbj("/ws");
		mav.addObject("zjg", t_zjg);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 *8085端获取请购信息
	 */
	@RequestMapping("/production/purchase/getPurchaseForWechat")
	@ResponseBody
	public Map<String,Object> getQgglForWechat(QgglDto qgglDto) {
		QgglDto qgglDto_t=qgglService.getDto(qgglDto);
		if(qgglDto_t==null) {
			qgglDto_t=new QgglDto();
		}
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		fjcfbDto.setYwid(qgglDto_t.getQgid());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		QgmxDto qgmxDto=new QgmxDto();
		qgmxDto.setQgid(qgglDto.getQgid());
		qgmxDto.setZt(qgglDto.getZt());
		List<QgmxDto> qgmxlist=qgmxService.getQgmxList(qgmxDto);
		Map<String,Object> map=new HashMap<>();
		map.put("qgglDto", qgglDto_t);
		map.put("qgmxlist", qgmxlist);
		map.put("t_fjcfbDtos", t_fjcfbDtos);
//===========================
		// 获取当前审核过程
		try {
			ShgcDto shgcDto = new ShgcDto();
			shgcDto.setShlb("AUDIT_REQUISITIONS");
			shgcDto.setYwid(qgglDto.getQgid());
			
			ShxxDto shxxParam = new ShxxDto();
			ShgcDto d_shgcDto;
			// 未传递gcid，则根据ywid和shlb获取shgc
			if (StringUtil.isBlank(shgcDto.getGcid())) {
				d_shgcDto = shgcService.getDtoByYwid(shgcDto.getYwid());
			} else {// 传递了gcid，则根据gcid获取shgc
				d_shgcDto = shgcService.getDtoById(shgcDto.getGcid());
			}
			if (d_shgcDto == null) {// 数据不在审核中，表示通过
				shgcDto.setXlcxh("");// 审核通过时
			} else {
				shgcDto = d_shgcDto;
			}
			shxxParam.setShlb(shgcDto.getShlb());
			shxxParam.setShlbs(shgcDto.getShlbs());
			shxxParam.setYwid(shgcDto.getYwid());
			shxxParam.setGcid(shgcDto.getGcid());// 用过程id进行查询

			shxxParam.setSftg(null);
			shxxParam.setCommit(false); 
			List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
			if (null == shxxList) {
				shxxList = new ArrayList<>();
			}

			// 获取的审核流程列表
			ShlcDto shlcParam = new ShlcDto();
			// gcid或shid参数不全的情况（已审核完，延期审核查看会传递gcid，所以gcid和shid都需要判断），根据审核信息获取流程
			if ((StringUtil.isBlank(shgcDto.getGcid()) || StringUtil.isBlank(shgcDto.getShid()))
					&& !CollectionUtils.isEmpty(shxxList)) {
				shlcParam.setShid(shxxList.get(0).getShid());
				shlcParam.setSqsj(shxxList.get(0).getSqsj());
			} else {
				shlcParam.setShid(shgcDto.getShid());
				shlcParam.setGcid(shgcDto.getGcid());// 处理旧流程判断用
			}
			if (StringUtil.isNotBlank(shlcParam.getShid())) {
				List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
				if (StringUtil.isNotBlank(shlcParam.getGcid())) {
					if(StringUtil.isNotBlank(shgcDto.getXlcxh())){
						for (ShlcDto shlc : shlcList) {
							if (shlc.getLcxh().equals(shgcDto.getXlcxh())) {// 相等则是当前流程
								// 当前流程做标记
								shlc.setCurrent(true);// 当前流程
								shlc.setAudited(true); //已审核流程
								break;// 跳出for循环
							}
							shlc.setAudited(true); //已审核流程
						}
					}
				}else{
					for (ShlcDto shlc : shlcList) {
						shlc.setAudited(true); //已审核流程
					}
				}
				map.put("shlcList", shlcList);
			}
			map.put("shgcDto",shgcDto);
			map.put("shxxList", shxxList);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			return map;
	}

	
	/**
	 * 8085端获取合同信息
	 */
	@RequestMapping("/production/contract/getContractForwechat")
	@ResponseBody
	public Map<String,Object> getQgglForWechat(HtglDto htglDto){
		Map<String,Object> map=new HashMap<>();
		HtglDto t_htglDto = htglService.getDtoById(htglDto.getHtid());
		List<HtmxDto> htmxDtos = htmxService.getListByHtid(htglDto.getHtid());
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBCONTRACT.getCode());
		fjcfbDto.setYwid(htglDto.getHtid());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		map.put("t_fjcfbDtos",t_fjcfbDtos);
		map.put("htglDto", t_htglDto);
		map.put("htmxlist", htmxDtos);
		return map;
	}
	
	/**
	 *8085端获取请购明细信息
	 */
	@RequestMapping("/production/getPurchaseInfoForWechat")
	@ResponseBody
	public Map<String,Object> getQgmxForWechat(QgmxDto qgmxDto) {
		Map<String,Object> map=new HashMap<>();
		List<QgmxDto> list=qgmxService.getQgmxList(qgmxDto);
		map.put("rows", list);
		map.put("total", qgmxDto.getTotalNumber());
		return map;
	}
	
	/**
	 * 8085端查看请购明细信息
	 */
	@RequestMapping("/production/viewMorePurchase")
	@ResponseBody
	public Map<String,Object> viewMorePurchase(QgmxDto qgmxDto){
		Map<String,Object> map=new HashMap<>();
		qgmxDto=qgmxService.getDtoById(qgmxDto.getQgmxid());
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		fjcfbDto.setYwid(qgmxDto.getQgid());
		fjcfbDto.setZywid(qgmxDto.getQgmxid());
		List<FjcfbDto> mx_fjcfbDtos = fjcfbService.getListByZywid(fjcfbDto);
		map.put("QgmxDto", qgmxDto);
		map.put("mx_fjcfbDtos", mx_fjcfbDtos);
		if (!"SERVICE".equals(qgmxDto.getQglbdm())){
			Map<String, Object> map_wl = this.getHqMateriel(qgmxDto);
			map.putAll(map_wl);
		}
		return map;
	}
	/**
	 * 货期数据
	 */
	@RequestMapping(value = "/production/pagedataGetHq")
	@ResponseBody
	public Map<String, Object> pagedataGetHq(HwxxDto hwxxDto){
		Map<String,Object> map=new HashMap<>();
		List<HwxxDto> hqlist=hwxxService.getLastestHq(hwxxDto);
		map.put("hqxxType",hqlist);
		return map;
	}
	/**
	 * 查看物料货期相关信息
	 */
	@RequestMapping(value="/materiel/viewHqMater")
	public ModelAndView viewHqMater(WlglDto wlglDto){
		ModelAndView mav = new ModelAndView("production/materiel/mater_hqview");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("wlglDto", wlglDto);
		return mav;
	}

	/**
	 * 获取物料货期相关信息
	 */
	@RequestMapping("/production/getHqMateriel")
	@ResponseBody
	public Map<String,Object> getHqMateriel(QgmxDto qgmxDto){
		Map<String,Object> map=new HashMap<>();
		HwxxDto hwxxDto_kc = new HwxxDto();
		hwxxDto_kc.setWlid(qgmxDto.getWlid());
		List<HwxxDto> hwxxDtos = hwxxService.getWlkcInfoGroupBy(hwxxDto_kc);
		List<JcsjDto> list=redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DELIVERY_TYPE.getCode());
		for (JcsjDto jcsjDto:list){
			if ("b".equals(jcsjDto.getCsdm())){
				map.put("cklb",jcsjDto.getCsid());
			}
		}

		String[] yfs =new String[5];
		int j=0;
		for (int i=-4;i<=0;i++){
			Date date = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MONTH, i);
			date = calendar.getTime();
			String defaultStartDate = new SimpleDateFormat("yyyy-MM").format(date);
			yfs[j]=defaultStartDate;
			j++;
		}
		map.put("yfs",yfs);
		map.put("wlid",qgmxDto.getWlid());
		List<HwxxDto> llhwxxDtos=hwxxService.selectFiveMonthsLlsl(map);
		WlglDto wlglDto=wlglService.getDtoById(qgmxDto.getWlid());
		for (String yf : yfs) {
			boolean flag = false;
			for (HwxxDto hwxxDto1 : llhwxxDtos) {
				if (yf.equals(hwxxDto1.getYf())) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				HwxxDto hwxxDto_t = new HwxxDto();
				String lcsl = "0.00" + wlglDto.getJldw();
				hwxxDto_t.setYf(yf);
				hwxxDto_t.setLcsl(lcsl);
				llhwxxDtos.add(hwxxDto_t);
			}
		}
		for (int i=0;i<llhwxxDtos.size();i++){
			for (int x=i+1;x<llhwxxDtos.size();x++){
				if (llhwxxDtos.get(i).getYf().compareTo(llhwxxDtos.get(x).getYf())>0){
					HwxxDto hwxxDto1=llhwxxDtos.get(i);
					llhwxxDtos.set(i, llhwxxDtos.get(x));
					llhwxxDtos.set(x,hwxxDto1);
				}
			}
		}
		HwxxDto hwxxDto=new HwxxDto();
		hwxxDto.setWlid(qgmxDto.getWlid());
		List<HwxxDto> ztslHwxxDtos=hwxxService.selectZtsl(hwxxDto);
		map.put("hwxxDtos", hwxxDtos);
		map.put("llhwxxDtos", llhwxxDtos);
		map.put("ztslHwxxDtos", ztslHwxxDtos);
		return map;
	}
	/**
	 * 查看请购确认明细信息
	 */
	@RequestMapping("/production/viewMorePurchaseConfirm")
	@ResponseBody
	public Map<String,Object> viewMorePurchaseConfirm(XzqgqrmxDto xzqgqrmxDto){
		Map<String,Object> map=new HashMap<>();
		xzqgqrmxDto=xzqgqrmxService.getDtoById(xzqgqrmxDto.getQrmxid());
		map.put("xzqgqrmxDto", xzqgqrmxDto);
		return map;
	}

	/**
	 * 查看合同付款明细信息
	 */
	@RequestMapping("/production/viewMoreContractConfirm")
	@ResponseBody
	public Map<String,Object> viewMoreContractConfirm(HtfkmxDto htfkmxDto){
		Map<String,Object> map=new HashMap<>();
		htfkmxDto=htfkmxService.getDtoById(htfkmxDto.getHtfkmxid());
		map.put("htfkmxDto", htfkmxDto);
		return map;
	}
	
	/**
	 * 采购明细数据(不分页)
	 */
	@RequestMapping("/production/purchase/pagedataQgmxList")
	@ResponseBody
	public Map<String,Object> getQgmxList(QgmxDto qgmxDto){
		Map<String,Object> map=new HashMap<>();
		List<QgmxDto> list=qgmxService.getQgmxList(qgmxDto);
		map.put("rows", list);
		map.put("total", qgmxDto.getTotalNumber());
		return map;
	}
	
	/**
	 * 采购明细数据(分页)
	 */
	@RequestMapping("/production/purchase/getListPurchaseInfo")
	@ResponseBody
	public Map<String,Object> getListPurchaseInfo(QgmxDto qgmxDto){
		Map<String,Object> map=new HashMap<>();
		List<QgmxDto> list=qgmxService.getPagedDtoList(qgmxDto);
		map.put("rows", list);
		map.put("total", qgmxDto.getTotalNumber());
		return map;
	}
	
    /**
     * 钉钉请购审核回调(分布式处理)
     */
    @RequestMapping("/production/purchase/aduitCallback")
    @ResponseBody
    public Map<String,Object> aduitCallback(HttpServletRequest request){
    	String obj=request.getParameter("obj");
    	JSONObject json_obj=JSON.parseObject(obj);
    	Map<String,Object> map=new HashMap<>();
    	Map<String,Object> t_map=new HashMap<>();//用于接收返回值
    	try {
    		String ddspbcbj=request.getParameter("ddspbcbj");
	    	if("1".equals(ddspbcbj)) {
	    		  if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
                          || ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
	    			  json_obj.put("cljg", "0");
	    		  }
	    		  ddspglService.insertInfo(json_obj);
	    	}
			boolean result = qgglService.callbackQgglAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
            if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
                //更新钉钉审批管理表数据
                //更新审批管理信息,失败
                DdspglDto ddspglDto=new DdspglDto();
                ddspglDto.setCljg("0");
                ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
                ddspglService.updatecljg(ddspglDto);
            }

			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
    	return map;
    }

	/**
	 * 钉钉请购审核回调(分布式处理)
	 */
	@RequestMapping("/production/purchase/receiveAduitCallback")
	@ResponseBody
	public Map<String,Object> receiveAduitCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> t_map=new HashMap<>();//用于接收返回值
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
	    	if("1".equals(ddspbcbj)) {
	    		  if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
                          || ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
	    			  json_obj.put("cljg", "0");
	    		  }
	    		  ddspglService.insertInfo(json_obj);
	    	}
			boolean result = llglService.callbackLlglAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
                //更新钉钉审批管理表数据
                //更新审批管理信息,失败
                DdspglDto ddspglDto=new DdspglDto();
                ddspglDto.setCljg("0");
                ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
                ddspglService.updatecljg(ddspglDto);
            }
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}

	/**
	 * 钉钉请购审核回调(分布式处理)
	 */
	@RequestMapping("/production/storehouse/receiveAdminAduitCallback")
	@ResponseBody
	public Map<String,Object> receiveAdminAduitCallback(HttpServletRequest request){
		log.error("1.进入钉钉审批回调方法" );
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> t_map=new HashMap<>();//用于接收返回值
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
	    	if("1".equals(ddspbcbj)) {
	    		log.error("2.1.equals(ddspbcbj)" );
	    		  if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
                          || ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
	    			  json_obj.put("cljg", "0");
	    			  log.error("3.处理结果为0" );
	    		  }
	    		  ddspglService.insertInfo(json_obj);
	    		  log.error("4.新增完成" );
	    	}
			boolean result = xzllglService.callbackXzllglAduit(json_obj, request,t_map);
			log.error("5.回调完成" );
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			log.error("6.回调异常" );
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
                //更新钉钉审批管理表数据
                //更新审批管理信息,失败
                DdspglDto ddspglDto=new DdspglDto();
                ddspglDto.setCljg("0");
                ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
                ddspglService.updatecljg(ddspglDto);
            }
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}
	/**
	 * 钉钉营销合同审核回调(分布式处理)
	 */
	@RequestMapping("/production/marketingContractAduitCallback")
	@ResponseBody
	public Map<String,Object> marketingContractAduitCallback(HttpServletRequest request){
		log.error("1.进入钉钉审批回调方法" );
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> t_map=new HashMap<>();//用于接收返回值
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
			if("1".equals(ddspbcbj)) {
				log.error("2.1.equals(ddspbcbj)" );
				if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
						|| ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
					json_obj.put("cljg", "0");
					log.error("3.处理结果为0" );
				}
				ddspglService.insertInfo(json_obj);
				log.error("4.新增完成" );
			}
			boolean result = yxhtService.callbackMarketingContractAduit(json_obj, request,t_map);
			log.error("5.回调完成" );
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			log.error("6.回调异常" );
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
				//更新钉钉审批管理表数据
				//更新审批管理信息,失败
				DdspglDto ddspglDto=new DdspglDto();
				ddspglDto.setCljg("0");
				ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
				ddspglService.updatecljg(ddspglDto);
			}
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}
	/**
	 * 钉钉维修保养设备审核回调(分布式处理)
	 */
	@RequestMapping("/production/upkeepDeviceAduitCallback")
	@ResponseBody
	public Map<String,Object> upkeepDeviceAduitCallback(HttpServletRequest request){
		log.error("1.进入钉钉审批回调方法" );
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> t_map=new HashMap<>();//用于接收返回值
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
			if("1".equals(ddspbcbj)) {
				log.error("2.1.equals(ddspbcbj)" );
				if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
						|| ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
					json_obj.put("cljg", "0");
					log.error("3.处理结果为0" );
				}
				ddspglService.insertInfo(json_obj);
				log.error("4.新增完成" );
			}
			boolean result = wxbyService.callbackUpkeepDeviceAduit(json_obj, request,t_map);
			log.error("5.回调完成" );
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			log.error("6.回调异常" );
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
				//更新钉钉审批管理表数据
				//更新审批管理信息,失败
				DdspglDto ddspglDto=new DdspglDto();
				ddspglDto.setCljg("0");
				ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
				ddspglService.updatecljg(ddspglDto);
			}
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}
	/**
	 * 钉钉需求计划回调(分布式处理)
	 */
	@RequestMapping("/production/purchase/progressAduitCallback")
	@ResponseBody
	public Map<String,Object> progressAduitCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> t_map=new HashMap<>();//用于接收返回值
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
	    	if("1".equals(ddspbcbj)) {
	    		  if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
                          || ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
	    			  json_obj.put("cljg", "0");
	    		  }
	    		  ddspglService.insertInfo(json_obj);
	    	}
			boolean result = cpxqjhService.callbackLlglAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
                //更新钉钉审批管理表数据
                //更新审批管理信息,失败
                DdspglDto ddspglDto=new DdspglDto();
                ddspglDto.setCljg("0");
                ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
                ddspglService.updatecljg(ddspglDto);
            }
            
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}
	/**
	 * 钉钉设备移交计划回调(分布式处理)
	 */
	@RequestMapping("/production/deviceTurnOverAduitCallback")
	@ResponseBody
	public Map<String,Object> deviceTurnOverAduitCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> t_map=new HashMap<>();//用于接收返回值
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
			if("1".equals(ddspbcbj)) {
				if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
						|| ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
					json_obj.put("cljg", "0");
				}
				ddspglService.insertInfo(json_obj);
			}
			boolean result = sbyjllService.callbackDeviceTurnOverAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
				//更新钉钉审批管理表数据
				//更新审批管理信息,失败
				DdspglDto ddspglDto=new DdspglDto();
				ddspglDto.setCljg("0");
				ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
				ddspglService.updatecljg(ddspglDto);
			}

			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}
	/**
	 * 钉钉设备报废回调(分布式处理)
	 */
	@RequestMapping("/production/deviceScrapAduitCallback")
	@ResponseBody
	public Map<String,Object> deviceScrapAduitCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> t_map=new HashMap<>();//用于接收返回值
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
			if("1".equals(ddspbcbj)) {
				if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
						|| ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
					json_obj.put("cljg", "0");
				}
				ddspglService.insertInfo(json_obj);
			}
			boolean result = sbbfService.callbackDeviceScarpAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
				//更新钉钉审批管理表数据
				//更新审批管理信息,失败
				DdspglDto ddspglDto=new DdspglDto();
				ddspglDto.setCljg("0");
				ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
				ddspglService.updatecljg(ddspglDto);
			}

			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}
    /**
     * 钉钉合同审核回调(分布式处理)
     */
    @RequestMapping("/production/contract/aduitCallback")
    @ResponseBody
    public Map<String,Object> contractAduitCallback(HttpServletRequest request) {
    	String obj=request.getParameter("obj");
    	JSONObject json_obj=JSON.parseObject(obj);
    	boolean result;
    	Map<String,Object> map=new HashMap<>();
    	Map<String,Object> t_map=new HashMap<>();//用于接收返回值
    	
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
	    	if("1".equals(ddspbcbj)) {
	    		  if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
                          || ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
	    			  json_obj.put("cljg", "0");
	    		  }
	    		  ddspglService.insertInfo(json_obj);
	    	}
			result = htglService.callbackHtglAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
                //更新钉钉审批管理表数据
                //更新审批管理信息,失败
                DdspglDto ddspglDto=new DdspglDto();
                ddspglDto.setCljg("0");
                ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
                ddspglService.updatecljg(ddspglDto);
            }
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
    	return map;
    }

	/**
	 * 钉钉合同付款申请审核回调(分布式处理)
	 */
	@RequestMapping("/production/payment/aduitCallback")
	@ResponseBody
	public Map<String,Object> paymentAduitCallback(HttpServletRequest request) {
		String obj=request.getParameter("obj");
		logger.error("paymentAduitCallback--obj="+obj);
		JSONObject json_obj=JSON.parseObject(obj);
		boolean result;
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> t_map=new HashMap<>();//用于接收返回值
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
	    	if("1".equals(ddspbcbj)) {
	    		  if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
                          || ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
	    			  json_obj.put("cljg", "0");
	    		  }
	    		  ddspglService.insertInfo(json_obj);
	    	}
			result = htfkqkService.callbackHtfkqkAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
                //更新钉钉审批管理表数据
                //更新审批管理信息,失败
                DdspglDto ddspglDto=new DdspglDto();
                ddspglDto.setCljg("0");
                ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
                ddspglService.updatecljg(ddspglDto);
            }
            
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}

	/**
	 * 钉钉行政需求价格确认审核回调(分布式处理)
	 */
	@RequestMapping("/production/purchaseConfirm/aduitCallback")
	@ResponseBody
	public Map<String,Object> purchaseConfirmAduitCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> t_map=new HashMap<>();//用于接收返回值
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
	    	if("1".equals(ddspbcbj)) {
	    		  if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
                          || ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
	    			  json_obj.put("cljg", "0");
	    		  }
	    		  ddspglService.insertInfo(json_obj);
	    	}
			boolean result = xzqgqrglService.callbackXzqgqrAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
                //更新钉钉审批管理表数据
                //更新审批管理信息,失败
                DdspglDto ddspglDto=new DdspglDto();
                ddspglDto.setCljg("0");
                ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
                ddspglService.updatecljg(ddspglDto);
            }
          
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}

	/**
	 * 钉钉行政付款审核回调(分布式处理)
	 */
	@RequestMapping("/production/adminPurchasePay/aduitCallback")
	@ResponseBody
	public Map<String,Object> aduitAdminPayCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> t_map=new HashMap<>();//用于接收返回值
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
	    	if("1".equals(ddspbcbj)) {
	    		  if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
                          || ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
	    			  json_obj.put("cljg", "0");
	    		  }
	    		  ddspglService.insertInfo(json_obj);
	    	}
			boolean result = xzqgfkglService.callbackXzqgfkglAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
                //更新钉钉审批管理表数据
                //更新审批管理信息,失败
                DdspglDto ddspglDto=new DdspglDto();
                ddspglDto.setCljg("0");
                ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
                ddspglService.updatecljg(ddspglDto);
            }
           
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}
    
    /**
     * 更新钉钉分布式管理信息并添加钉钉审批管理信息(分布式处理)
     */
    @RequestMapping("/production/updateSaveDdfbsgl")
    @ResponseBody
    public boolean updateSaveDdfbsgl(HttpServletRequest request) {
    	String obj=request.getParameter("obj");
    	String jszt=request.getParameter("jszt");
    	JSONObject json_obj=JSONObject.parseObject(obj);
    	DdfbsglDto ddfbsglDto=new DdfbsglDto();
    	ddfbsglDto.setYwmc(json_obj.getString("title"));
    	ddfbsglDto.setYwlx(json_obj.getString("processCode"));
    	if(StringUtils.isNotBlank(jszt))
    		ddfbsglDto.setJszt(jszt);
    	ddfbsglDto.setProcessinstanceid(json_obj.getString("processInstanceId"));
    	boolean result=ddfbsglService.update(ddfbsglDto);
    	if(!result)
    		return false;
    	ddspglService.insertInfo(json_obj);
    	return true;
    }
    
    /**
     * 修改页面
     */
    @RequestMapping("/production/purchase/modPurchase")
    public ModelAndView modPurchase(QgglDto qgglDto) {
 		ModelAndView mav = new ModelAndView("production/materiel/mater_shopping");
		QgglDto qgglDto_t = qgglService.getDtoById(qgglDto.getQgid());
		qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		qgglDto_t.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		qgglDto_t.setFwbj("/ws");
		// 查询附件信息 -采购单
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(qgglDto_t.getYwlx());
		fjcfbDto.setYwid(qgglDto.getQgid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		mav.addObject("qgglDto", qgglDto_t);
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING});
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(qgglDto_t.getXmdl());
		List<JcsjDto> zlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		mav.addObject("xmbmlist",zlbList);
		mav.addObject("xmdlbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		mav.addObject("formAction", "/purchase/purchase/pagedataSavePurchase");
		mav.addObject("url", "/production/purchase/pagedataQgmxList?qgid="+qgglDto.getQgid());
		mav.addObject("flag", "qgmx");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
    }
    /**
     * 审核修改页面
     */
    @RequestMapping("/production/purchase/auditPurchase")
    public ModelAndView auditPurchase(QgglDto qgglDto) {
 		ModelAndView mav = new ModelAndView("production/materiel/mater_shopping");
		QgglDto qgglDto_t = qgglService.getDtoById(qgglDto.getQgid());
		qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		qgglDto_t.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		qgglDto_t.setFwbj("/ws");
		// 查询附件信息 -采购单
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(qgglDto_t.getYwlx());
		fjcfbDto.setYwid(qgglDto.getQgid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		mav.addObject("qgglDto", qgglDto_t);
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING});
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(qgglDto_t.getXmdl());
		List<JcsjDto> zlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		mav.addObject("xmbmlist",zlbList);
		mav.addObject("xmdlbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		mav.addObject("formAction", "/purchase/purchase/pagedataSavePurchase");
		mav.addObject("url", "/production/purchase/pagedataQgmxList?qgid="+qgglDto.getQgid());
		mav.addObject("flag", "qgmx");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
    }
    
	/**
	 * 请购明细附件上传页面
	 */
	@RequestMapping("/production/purchase/getUploadFilePage")
	public ModelAndView getUploadFilePage(QgglDto qgglDto) {
		ModelAndView mav=new ModelAndView("production/materiel/mater_uploadShoppingFile");
		// 查询临时文件并显示
		List<String> fjids = qgglDto.getFjids();
		List<FjcfbDto> redisList = fjcfbService.getRedisList(fjids);
		mav.addObject("redisList", redisList);
		// 查询正式文件并显示
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(qgglDto.getQgid());
		fjcfbDto.setZywid(qgglDto.getQgmxid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.getListByZywid(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		qgglDto.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 请购审核修改页面
	 */
	@RequestMapping("/purchase/purchase/modAuditPurchase")
    public ModelAndView modAuditPurchase(QgglDto qgglDto) {
		ModelAndView mav=new ModelAndView("production/materiel/mater_shopping"); 
		QgglDto qgglDto_t=qgglService.getDtoById(qgglDto.getQgid());
		qgglDto_t.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		qgglDto_t.setFwbj("/ws");
		mav.addObject("qgglDto", qgglDto_t);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING,BasicDataTypeEnum.PAYMENT_TYPE,BasicDataTypeEnum.PAYER});
		// 查询附件信息 -采购单
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(qgglDto_t.getYwlx());
		fjcfbDto.setYwid(qgglDto.getQgid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(qgglDto_t.getXmdl());
		List<JcsjDto> zlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		mav.addObject("xmbmlist",zlbList);
		mav.addObject("xmdlbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		mav.addObject("zffslist", jclist.get(BasicDataTypeEnum.PAYMENT_TYPE.getCode()));
		mav.addObject("fkflist", jclist.get(BasicDataTypeEnum.PAYER.getCode()));
		mav.addObject("flag", "qxgjxg");
		mav.addObject("url", "/production/purchase/pagedataQgmxList?qgid="+qgglDto.getQgid());
		mav.addObject("formAction", "/purchase/purchase/pagedataSavePurchase");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
    }
	
	/**
	 * 合同修改页面
	 */
	@RequestMapping("/contract/contract/modContractView")
	public ModelAndView modContractView(HtglDto htglDto) {
		ModelAndView mav = new ModelAndView("contract/contract/contract_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.PAYMENT_METHOD, BasicDataTypeEnum.INVOICE_METHOD,
						BasicDataTypeEnum.CONTRACT_TYPE, BasicDataTypeEnum.CURRENCY });
		mav.addObject("paymentlist", jclist.get(BasicDataTypeEnum.PAYMENT_METHOD.getCode()));// 付款方式
		mav.addObject("invoicelist", jclist.get(BasicDataTypeEnum.INVOICE_METHOD.getCode()));// 发票方式
		mav.addObject("contractlist", jclist.get(BasicDataTypeEnum.CONTRACT_TYPE.getCode()));// 合同类型
		mav.addObject("currencylist", jclist.get(BasicDataTypeEnum.CURRENCY.getCode()));// 币种
		// 查询合同信息
		HtglDto t_htglDto = htglService.getDto(htglDto);
		t_htglDto.setFwbj("/ws");
		t_htglDto.setAuditType(AuditTypeEnum.AUDIT_CONTRACT.getCode());
		t_htglDto.setFormAction("modSaveContract");
		mav.addObject("htglDto", t_htglDto);
		mav.addObject("url", "/contract/contract/pagedataContractDetailList");
		return mav;
	}
	
	/**
	 * 获取合同请购明细信息
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/contract/pagedataContractDetailList")
	public Map<String, Object> getContractDetailList(HtmxDto htmxDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(htmxDto.getHtid())) {
			List<HtmxDto> htmxDtos = htmxService.getListByHtid(htmxDto.getHtid());
			map.put("rows", htmxDtos);
		} else {
			map.put("rows", null);
		}
		return map;
	}
	
	/**
	 * 合同审核通过查看页面
	 */
	@RequestMapping("/purchase/purchase/purchaseWsView")
	public ModelAndView purchaseWsView(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_WsView");
		HtglDto htglDto_t = htglService.getDtoById(qgglDto.getHtid());
		QgglDto qgglDto_t = qgglService.getDtoById(qgglDto.getQgid());
		List<QgglDto> qgglDtos_t = qgglService.getQgList(qgglDto);
		mav.addObject("djh_t", qgglDto_t.getDjh());
		mav.addObject("htnbbh_t", htglDto_t.getHtnbbh());
		mav.addObject("qgglDtos_t", qgglDtos_t);
		return mav;
	}
	
    /**
     * 取消请购修改页面
     */
    @RequestMapping("/purchaseCancel/purchaseCancel/modPurchaseCancel")
    public ModelAndView modPurchaseCancel(QgqxglDto qgqxglDto) {
		ModelAndView mav =new ModelAndView("purchase/purchase/purchase_cancel");
		QgqxglDto qgqxglDto_t = qgqxglService.getDtoById(qgqxglDto.getQgqxid());
		qgqxglDto_t.setFwbj("/ws");
		qgqxglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_CANCEL.getCode());
        mav.addObject("qgglDto", qgqxglDto_t);
        mav.addObject("urlPrefix", urlPrefix);
        mav.addObject("url", "/purchaseCancel/purchaseCancel/pagedataQgqxmxList?qgqxid="+qgqxglDto_t.getQgqxid());
        mav.addObject("formAction", "/purchaseCancel/purchaseCancel/pagedataSavePurchaseCancel");
        return mav;
    }

	/**
	 * 请购取消明细列表(不分页)
	 */
	@RequestMapping("/purchaseCancel/purchaseCancel/pagedataQgqxmxList")
	@ResponseBody
	public Map<String,Object> getQgqxmxList(QgqxglDto qgqxglDto){
		Map<String,Object> map=new HashMap<>();
		QgqxmxDto qgqxmxDto=new QgqxmxDto();
		qgqxmxDto.setQgqxid(qgqxglDto.getQgqxid());
		List<QgqxmxDto> qgqxmxlist=qgqxmxService.getQgqxmxCancelList(qgqxmxDto);
		map.put("rows", qgqxmxlist);
		return map;
	}
    
	   /**
     * 将钉钉审批信息保存至钉钉分布式管理表
     */
    @RequestMapping("/purchase/saveDistributedMsg")
    @ResponseBody
    public boolean saveDistributedMsg(HttpServletRequest request) {
    	//提交审核至钉钉审批
        String ddslid=request.getParameter("ddslid");
        String fwqm=request.getParameter("fwqm");
        String cljg=request.getParameter("cljg");
        String fwqmc=request.getParameter("fwqmc");
        String spywlx=request.getParameter("spywlx");
        String hddz=request.getParameter("hddz");
        String ywlx = request.getParameter("ywlx");
        String ywmc = request.getParameter("ywmc");
        String wbcxString = request.getParameter("wbcxid");
        if(StringUtils.isBlank(hddz))
			hddz="http://"+request.getRemoteAddr();
        DdfbsglDto ddfbsglDto=ddfbsglService.getDtoById(ddslid);
		if(ddfbsglDto==null) {
			DdfbsglDto t_ddfbsglDto=new DdfbsglDto();
			t_ddfbsglDto.setProcessinstanceid(ddslid);
			t_ddfbsglDto.setFwqm(fwqm);
			t_ddfbsglDto.setCljg(cljg);
			t_ddfbsglDto.setFwqmc(fwqmc);
			t_ddfbsglDto.setSpywlx(spywlx);
			t_ddfbsglDto.setHddz(hddz);
			t_ddfbsglDto.setYwlx(ywlx);
			t_ddfbsglDto.setYwmc(ywmc);
			t_ddfbsglDto.setWbcxid(wbcxString);
			return ddfbsglService.insert(t_ddfbsglDto);
		}else {
			ddfbsglDto.setFwqm(fwqm);
			ddfbsglDto.setWbcxid(wbcxString);
			return ddfbsglService.update(ddfbsglDto);
		}
    }

    /**
     * 保存钉钉审批管理信息
     */
    @RequestMapping("/production/saveDdspgl")
    @ResponseBody
    public boolean saveDdspgl(HttpServletRequest request) {
    	String text=request.getParameter("obj");
    	JSONObject obj = JSON.parseObject(text);
    	DdspglDto ddspglDto=ddspglService.insertInfo(obj);
		return ddspglDto != null;
	}
    
	/**
	 * 8086端查看请购信息
	 */
	@RequestMapping("/production/purchase/getPurchaseMessage")
	@ResponseBody
	public ModelAndView getRequestUrl(HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("purchase/purchase/purchase_phone_auditView");
		String qgid=request.getParameter("qgid");
		QgglDto qgglDto = new QgglDto();
		qgglDto.setQgid(qgid);
		qgglDto=qgglService.getDto(qgglDto);
		if(qgglDto==null) {
			qgglDto=new QgglDto();
		}
	    //查询附件
	    FjcfbDto fjcfbDto = new FjcfbDto();
	    fjcfbDto.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
	    fjcfbDto.setYwid(qgglDto.getQgid());
	    List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
	    QgmxDto qgmxDto=new QgmxDto();
	    qgmxDto.setQgid(qgglDto.getQgid());
	    qgmxDto.setZt(StatusEnum.CHECK_PASS.getCode());
	    List<QgmxDto> qgmxlist=qgmxService.getQgmxList(qgmxDto);
		//zt字段必须是80，为了过滤掉取消请购和拒绝审批的物料，同时状态不为80的不计入金额计算
//		Map<String,Object> map=restTemplate.getForObject(url+urlPrefix+"/ws/production/purchase/getPurchaseForWechat?qgid="+qgid+"&zt=80",Map.class, "");
//		QgglDto qgglDto = JSON.parseObject(JSON.toJSONString(map.get("qgglDto")), QgglDto.class);
		List<QgmxDto> t_list=new ArrayList<>();
		
		BigDecimal zjg=new BigDecimal("0");
//		List<Object> list = JSON.parseObject(JSON.toJSONString(map.get("qgmxlist")), List.class);
		DecimalFormat df4 = new DecimalFormat("#,##0.00");
		if(!CollectionUtils.isEmpty(qgmxlist)) {
			for (QgmxDto dto : qgmxlist) {
				String jg = dto.getJg();
				String sl = dto.getSl();
				if (StringUtils.isNotBlank(jg) && StringUtils.isNotBlank(sl)) {
					zjg = zjg.add(new BigDecimal(jg).multiply(new BigDecimal(sl)));
					dto.setJg(df4.format(new BigDecimal(jg)));
				}
				t_list.add(dto);
			}
		}
		zjg=zjg.setScale(2, RoundingMode.UP);
		String t_zjg=df4.format(zjg);//千位符分隔
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("qgmxlist", t_list);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("zjg", t_zjg);

		if(!CollectionUtils.isEmpty(t_fjcfbDtos)) {
			List<FjcfbDto> fjcfbDtos = new ArrayList<>(t_fjcfbDtos);
			String wjm=fjcfbDtos.get(0).getWjm();
			String fjid=fjcfbDtos.get(0).getFjid();
			mav.addObject("wjm", wjm);
			String sign = URLEncoder.encode(commonService.getSign(wjm), StandardCharsets.UTF_8);
			mav.addObject("sign", sign);
			sign = URLEncoder.encode(commonService.getSign(fjid), StandardCharsets.UTF_8);
			mav.addObject("wssign", sign);
			mav.addObject("t_fjcfbDtos", fjcfbDtos);
		}

		//下面部分为审核历史页面需要的数据
		
		ShgcDto shgcDto = new ShgcDto();
		shgcDto.setShlb("AUDIT_REQUISITIONS");
		shgcDto.setYwid(qgglDto.getQgid());
      
		ShxxDto shxxParam = new ShxxDto();
		ShgcDto d_shgcDto;
		// 未传递gcid，则根据ywid和shlb获取shgc
		if (StringUtil.isBlank(shgcDto.getGcid())) {
			d_shgcDto = shgcService.getDtoByYwid(shgcDto.getYwid());
		} else {// 传递了gcid，则根据gcid获取shgc
			d_shgcDto = shgcService.getDtoById(shgcDto.getGcid());
		}
		if (d_shgcDto == null) {// 数据不在审核中，表示通过
			shgcDto.setXlcxh("");// 审核通过时
		} else {
			shgcDto = d_shgcDto;
		}
		shxxParam.setShlb(shgcDto.getShlb());
		shxxParam.setShlbs(shgcDto.getShlbs());
		shxxParam.setYwid(shgcDto.getYwid());
		shxxParam.setGcid(shgcDto.getGcid());// 用过程id进行查询

		shxxParam.setSftg(null);
		shxxParam.setCommit(false); 
		List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
		if (null == shxxList) {
			shxxList = new ArrayList<>();
		}

		// 获取的审核流程列表
		ShlcDto shlcParam = new ShlcDto();
		// gcid或shid参数不全的情况（已审核完，延期审核查看会传递gcid，所以gcid和shid都需要判断），根据审核信息获取流程
		if ((StringUtil.isBlank(shgcDto.getGcid()) || StringUtil.isBlank(shgcDto.getShid()))
				&& !CollectionUtils.isEmpty(shxxList)) {
			shlcParam.setShid(shxxList.get(0).getShid());
			shlcParam.setSqsj(shxxList.get(0).getSqsj());
		} else {
			shlcParam.setShid(shgcDto.getShid());
			shlcParam.setGcid(shgcDto.getGcid());// 处理旧流程判断用
		}
		if (StringUtil.isNotBlank(shlcParam.getShid())) {
			List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
			if (StringUtil.isNotBlank(shlcParam.getGcid())) {
				if(StringUtil.isNotBlank(shgcDto.getXlcxh())){
					for (ShlcDto shlc : shlcList) {
						if (shlc.getLcxh().equals(shgcDto.getXlcxh())) {// 相等则是当前流程
							// 当前流程做标记
							shlc.setCurrent(true);// 当前流程
							shlc.setAudited(true); //已审核流程
							break;// 跳出for循环
						}
						shlc.setAudited(true); //已审核流程
					}	
				}
			}else{
				for (ShlcDto shlc : shlcList) {
					shlc.setAudited(true); //已审核流程
				}
			}
			mav.addObject("shlcList", shlcList);
		}
		mav.addObject("shgcDto", shgcDto);
        mav.addObject("shxxList", shxxList);		
		return mav;
	}

	/**
	 * 8086端查看领料出库信息
	 */
	@RequestMapping("/production/requisitionInfo")
	@ResponseBody
	public ModelAndView requisitionInfo(HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("purchase/purchase/receive_auditView");
		String llid=request.getParameter("llid");
		LlglDto llglDto = llglService.getDtoById(llid);
		if(llglDto==null) {
			llglDto=new LlglDto();
		}
		HwllxxDto hwllxxDto = new HwllxxDto();
		hwllxxDto.setLlid(llid);
		List<HwllxxDto> hwllxxDtos = hwllxxService.getDtoList(hwllxxDto);
		mav.addObject("llglDto", llglDto);
		mav.addObject("hwllxxDtos", hwllxxDtos);
		mav.addObject("urlPrefix", urlPrefix);

		//下面部分为审核历史页面需要的数据

		ShgcDto shgcDto = new ShgcDto();
		shgcDto.setShlb(AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode());
		shgcDto.setYwid(llid);

		ShxxDto shxxParam = new ShxxDto();
		ShgcDto d_shgcDto;
		// 未传递gcid，则根据ywid和shlb获取shgc
		if (StringUtil.isBlank(shgcDto.getGcid())) {
			d_shgcDto = shgcService.getDtoByYwid(shgcDto.getYwid());
		} else {// 传递了gcid，则根据gcid获取shgc
			d_shgcDto = shgcService.getDtoById(shgcDto.getGcid());
		}
		if (d_shgcDto == null) {// 数据不在审核中，表示通过
			shgcDto.setXlcxh("");// 审核通过时
		} else {
			shgcDto = d_shgcDto;
		}
		shxxParam.setShlb(shgcDto.getShlb());
		shxxParam.setShlbs(shgcDto.getShlbs());
		shxxParam.setYwid(shgcDto.getYwid());
		shxxParam.setGcid(shgcDto.getGcid());// 用过程id进行查询

		shxxParam.setSftg(null);
		shxxParam.setCommit(false);
		List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
		if (null == shxxList) {
			shxxList = new ArrayList<>();
		}

		// 获取的审核流程列表
		ShlcDto shlcParam = new ShlcDto();
		// gcid或shid参数不全的情况（已审核完，延期审核查看会传递gcid，所以gcid和shid都需要判断），根据审核信息获取流程
		if ((StringUtil.isBlank(shgcDto.getGcid()) || StringUtil.isBlank(shgcDto.getShid()))
				&& !CollectionUtils.isEmpty(shxxList)) {
			shlcParam.setShid(shxxList.get(0).getShid());
			shlcParam.setSqsj(shxxList.get(0).getSqsj());
		} else {
			shlcParam.setShid(shgcDto.getShid());
			shlcParam.setGcid(shgcDto.getGcid());// 处理旧流程判断用
		}
		if (StringUtil.isNotBlank(shlcParam.getShid())) {
			List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
			if (StringUtil.isNotBlank(shlcParam.getGcid())) {
				if(StringUtil.isNotBlank(shgcDto.getXlcxh())){
					for (ShlcDto shlc : shlcList) {
						if (shlc.getLcxh().equals(shgcDto.getXlcxh())) {// 相等则是当前流程
							// 当前流程做标记
							shlc.setCurrent(true);// 当前流程
							shlc.setAudited(true); //已审核流程
							break;// 跳出for循环
						}
						shlc.setAudited(true); //已审核流程
					}
				}
			}else{
				for (ShlcDto shlc : shlcList) {
					shlc.setAudited(true); //已审核流程
				}
			}
			mav.addObject("shlcList", shlcList);
		}
		mav.addObject("shgcDto", shgcDto);
		mav.addObject("shxxList", shxxList);
		return mav;
	}


	/**
	 * 8086端查看行政领料信息
	 */
	@RequestMapping("/production/requisitionAdminInfo")
	public ModelAndView requisitionAdminInfo(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveAdmin_auditView");
		String xzllid=request.getParameter("xzllid");
		List<XzllmxDto> list = xzllmxService.getDtoXzllmxListByXzllid(xzllid);
		mav.addObject("list", list);
		return mav;
	}


	/**
	 * 8086端查看领料出库信息
	 */
	@RequestMapping("/production/progressInfo")
	@ResponseBody
	public ModelAndView progressInfo(HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("production/progress/progress_auditView");
		String cpxqid=request.getParameter("cpxqid");
		CpxqjhDto cpxqjhDto = cpxqjhService.getDtoById(cpxqid);
		if(cpxqjhDto!=null) {
			XqjhmxDto xqjhmxDto = new XqjhmxDto();
			xqjhmxDto.setXqjhid(cpxqjhDto.getCpxqid());
			List<XqjhmxDto> dtoList = xqjhmxService.getDtoList(xqjhmxDto);
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwlx(BusTypeEnum.IMP_DEMAND.getCode());
			fjcfbDto.setYwid(cpxqjhDto.getCpxqid());
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			mav.addObject("fjcfbDtos",fjcfbDtos);
			mav.addObject("cpxqjhDto",cpxqjhDto);
			mav.addObject("mxlist",dtoList);

			//下面部分为审核历史页面需要的数据

			ShgcDto shgcDto = new ShgcDto();
			shgcDto.setShlb(AuditTypeEnum.AUDIT_FG_PLAN.getCode());
			shgcDto.setYwid(cpxqid);

			ShxxDto shxxParam = new ShxxDto();
			ShgcDto d_shgcDto;
			// 未传递gcid，则根据ywid和shlb获取shgc
			if (StringUtil.isBlank(shgcDto.getGcid())) {
				d_shgcDto = shgcService.getDtoByYwid(shgcDto.getYwid());
			} else {// 传递了gcid，则根据gcid获取shgc
				d_shgcDto = shgcService.getDtoById(shgcDto.getGcid());
			}
			if (d_shgcDto == null) {// 数据不在审核中，表示通过
				shgcDto.setXlcxh("");// 审核通过时
			} else {
				shgcDto = d_shgcDto;
			}
			shxxParam.setShlb(shgcDto.getShlb());
			shxxParam.setShlbs(shgcDto.getShlbs());
			shxxParam.setYwid(shgcDto.getYwid());
			shxxParam.setGcid(shgcDto.getGcid());// 用过程id进行查询

			shxxParam.setSftg(null);
			shxxParam.setCommit(false);
			List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
			if (null == shxxList) {
				shxxList = new ArrayList<>();
			}

			// 获取的审核流程列表
			ShlcDto shlcParam = new ShlcDto();
			// gcid或shid参数不全的情况（已审核完，延期审核查看会传递gcid，所以gcid和shid都需要判断），根据审核信息获取流程
			if ((StringUtil.isBlank(shgcDto.getGcid()) || StringUtil.isBlank(shgcDto.getShid()))
					&& !CollectionUtils.isEmpty(shxxList)) {
				shlcParam.setShid(shxxList.get(0).getShid());
				shlcParam.setSqsj(shxxList.get(0).getSqsj());
			} else {
				shlcParam.setShid(shgcDto.getShid());
				shlcParam.setGcid(shgcDto.getGcid());// 处理旧流程判断用
			}
			if (StringUtil.isNotBlank(shlcParam.getShid())) {
				List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
				if (StringUtil.isNotBlank(shlcParam.getGcid())) {
					if(StringUtil.isNotBlank(shgcDto.getXlcxh())){
						for (ShlcDto shlc : shlcList) {
							if (shlc.getLcxh().equals(shgcDto.getXlcxh())) {// 相等则是当前流程
								// 当前流程做标记
								shlc.setCurrent(true);// 当前流程
								shlc.setAudited(true); //已审核流程
								break;// 跳出for循环
							}
							shlc.setAudited(true); //已审核流程
						}
					}
				}else{
					for (ShlcDto shlc : shlcList) {
						shlc.setAudited(true); //已审核流程
					}
				}
				mav.addObject("shlcList", shlcList);
			}
			mav.addObject("shgcDto", shgcDto);
			mav.addObject("shxxList", shxxList);
		}else{
			mav.addObject("cpxqjhDto",new CpxqjhDto());
		}
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 生产明细查看
	 */
	@RequestMapping("/production/viewMoreProgressDetails")
	@ResponseBody
	public Map<String,Object> viewMoreProgressDetails(XqjhmxDto xqjhmxDto){
		Map<String,Object> map = new HashMap<>();
		xqjhmxDto=xqjhmxService.getDtoById(xqjhmxDto.getXqjhmxid());
		map.put("xqjhmxDto",xqjhmxDto);
		return map;
	}
	/**
	 * 查看合同信息
	 */
	@RequestMapping("/contract/getContractUrl")
	public ModelAndView getContractUrl(HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("contract/contract/contract_phone_dingtalkView");
		String htid=request.getParameter("htid");
		HtglDto t_htglDto = htglService.getDtoById(htid);
		List<HtmxDto> htmxDtos = htmxService.getListByHtid(htid);
		//合同附件显示类别
		String fjmcxslb = "1";
		//是否为普通合同的补充合同
		mav.addObject("yhtbchtFlag",StringUtil.isNotBlank(t_htglDto.getBchtid()) && !"1".equals(t_htglDto.getKjlx()) ?"1":"0");
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBCONTRACT.getCode());
		fjcfbDto.setYwid(htid);
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		List<FjcfbDto> bc_fjcfbDtos = new ArrayList<>();
		List<FjcfbDto> kj_fjcfbDtos = new ArrayList<>();
		List<FjcfbDto> bckj_fjcfbDtos = new ArrayList<>();
		//补充合同
		if (StringUtil.isNotBlank(t_htglDto.getBchtid())){
			fjcfbDto.setYwid(t_htglDto.getBchtid());
			bc_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			mav.addObject("bc_fjcfbDtos", bc_fjcfbDtos);
			fjmcxslb = fjmcxslb+"2";
		}
		if (StringUtil.isNotBlank(t_htglDto.getKjhtid())){
			fjcfbDto.setYwid(t_htglDto.getKjhtid());
			kj_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			mav.addObject("kj_fjcfbDtos", kj_fjcfbDtos);
			HtglDto htglDto = new HtglDto();
			htglDto.setBchtid(t_htglDto.getKjhtid());
			List<HtglDto> dtoList = htglService.getDtoList(htglDto);
			if (!CollectionUtils.isEmpty(dtoList)){
				List<String> ids = dtoList.stream().map(HtglDto::getHtid).distinct().collect(Collectors.toList());
				FjcfbDto fjcfbDto_kjbc = new FjcfbDto();
				fjcfbDto_kjbc.setYwlx(BusTypeEnum.IMP_SUBCONTRACT.getCode());
				fjcfbDto_kjbc.setYwids(ids);
				bckj_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto_kjbc);
				mav.addObject("bckj_fjcfbDtos", bckj_fjcfbDtos);
			}
			fjmcxslb = fjmcxslb+"3";
		}
		if("3".equals(t_htglDto.getHtlx())){
			fjmcxslb = fjmcxslb+"4";
		}
		FjcfbDto fjcfbDto_t = new FjcfbDto();
		fjcfbDto_t.setYwlx(BusTypeEnum.IMP_SUBSUPPLIER.getCode());
		fjcfbDto_t.setYwid(t_htglDto.getGys());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto_t);
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
//		String url=request.getParameter("url");
		mav.addObject("fjcfbDtos", fjcfbDtos);
		mav.addObject("nowStr", DateUtils.getCustomFomratCurrentDate(new Date(),"yyyy.MM.dd"));

		List<FjcfbDto> htFjcfbDtos = new ArrayList<>();
		List<FjcfbDto> bchtjcfbDtos = new ArrayList<>();
		List<FjcfbDto> kjhtFjcfbDtos = new ArrayList<>();
		List<FjcfbDto> kjbchtFjcfbDtos = new ArrayList<>();

		//合同名称显示
		String htName="";
		String bchtName="";

		//如果合同类型为3，补充合同id不为空,为补充框架合同
		if("3".equals(t_htglDto.getHtlx()) && StringUtil.isNotBlank(t_htglDto.getBchtid())){
			kjbchtFjcfbDtos = t_fjcfbDtos;
			kjhtFjcfbDtos = bc_fjcfbDtos;
		}
		//如果合同类型为3，补充合同id为空,为框架合同
		if("3".equals(t_htglDto.getHtlx()) && StringUtil.isBlank(t_htglDto.getBchtid())){
			kjhtFjcfbDtos = t_fjcfbDtos;
		}
		//如果合同类型不为3，补充合同id不为空,框架合同不为空，为下属合同补充协议
		if(!"3".equals(t_htglDto.getHtlx()) &&  StringUtil.isNotBlank(t_htglDto.getBchtid()) && StringUtil.isNotBlank(t_htglDto.getKjhtid())){
			htFjcfbDtos = bc_fjcfbDtos;
			bchtjcfbDtos = t_fjcfbDtos;
			kjhtFjcfbDtos = kj_fjcfbDtos;
			kjbchtFjcfbDtos = bckj_fjcfbDtos;
			htName="下属合同";
			bchtName="下属合同补充协议";
		}
		//如果合同类型不为3，补充合同id不为空,框架合同为空，为原合同补充协议
		if(!"3".equals(t_htglDto.getHtlx()) &&  StringUtil.isNotBlank(t_htglDto.getBchtid()) && StringUtil.isBlank(t_htglDto.getKjhtid())){
			bchtjcfbDtos = t_fjcfbDtos;
			htFjcfbDtos = bc_fjcfbDtos;
			htName="原合同";
			bchtName="原合同补充协议";
		}
		//如果合同类型不为3，补充合同id为空,框架合同不为空，为下属合同
		if(!"3".equals(t_htglDto.getHtlx()) &&  StringUtil.isBlank(t_htglDto.getBchtid()) && StringUtil.isNotBlank(t_htglDto.getKjhtid())){
			htFjcfbDtos = t_fjcfbDtos;
			kjhtFjcfbDtos = kj_fjcfbDtos;
			kjbchtFjcfbDtos = bckj_fjcfbDtos;
			htName="下属合同";
		}
		//如果合同类型不为3，补充合同id为空,框架合同为空，为合同
		if(!"3".equals(t_htglDto.getHtlx()) &&  StringUtil.isBlank(t_htglDto.getBchtid()) && StringUtil.isBlank(t_htglDto.getKjhtid())){
			htFjcfbDtos = t_fjcfbDtos;
			htName="合同";
		}
		mav.addObject("htName", htName);
		mav.addObject("bchtName", bchtName);

		mav.addObject("htFjcfbDtos", htFjcfbDtos);
		mav.addObject("bchtjcfbDtos", bchtjcfbDtos);
		mav.addObject("kjhtFjcfbDtos", kjhtFjcfbDtos);
		mav.addObject("kjbchtFjcfbDtos", kjbchtFjcfbDtos);

		if(!CollectionUtils.isEmpty(t_fjcfbDtos)) {
			String wjm=t_fjcfbDtos.get(0).getWjm();
			String fjid=t_fjcfbDtos.get(0).getFjid();
			mav.addObject("wjm", wjm);
			mav.addObject("fjid", fjid);
			mav.addObject("wjs",t_fjcfbDtos.size());
			String sign = URLEncoder.encode(commonService.getSign(wjm), StandardCharsets.UTF_8);
			mav.addObject("sign", sign);
			sign = URLEncoder.encode(commonService.getSign(fjid), StandardCharsets.UTF_8);
			mav.addObject("wssign", sign);
			mav.addObject("t_fjcfbDtos", t_fjcfbDtos);
			mav.addObject("urlPrefix", urlPrefix);
			mav.addObject("htglDto", t_htglDto);
			mav.addObject("htmxlist", htmxDtos);
			mav.addObject("fjmcxslb", fjmcxslb);
			return mav;
		}
		return null;
	}


	/**
	 * 文件下载 外部接口
	 */
	@RequestMapping(value="/production/downloadFile")
	@ResponseBody
	public String downloadFile(FjcfbDto fjcfbDto, HttpServletResponse response, HttpServletRequest request){
		String filePath;
		String wjm;
		if (StringUtil.isNotBlank(fjcfbDto.getTemporary()) && !"null".equals(fjcfbDto.getTemporary())){
			filePath = fjcfbDto.getWjlj();
			wjm = fjcfbDto.getWjm();
		}else {
			FjcfbDto t_fjcfbDto = fjcfbService.getDtoWithScbjNotOne(fjcfbDto);
			if(t_fjcfbDto!=null) {
				String wjlj = t_fjcfbDto.getWjlj();
				wjm = t_fjcfbDto.getWjm();
				DBEncrypt crypt = new DBEncrypt();
				filePath = crypt.dCode(wjlj);
			}else{
				Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+fjcfbDto.getFjid());
				if(mFile!=null&&mFile.size()>0){
					filePath = (String)mFile.get("wjlj");
					wjm = (String)mFile.get("wjm");
				}else{
					log.error("对不起，系统未找到相应文件!");
					return null;
				}
			}

		}
		File downloadFile = new File(filePath);
		response.setContentLength((int) downloadFile.length());
		String agent = request.getHeader("user-agent");
		//log.error("文件下载 agent=" + agent);
		//指明为下载
		response.setHeader("content-type", "application/octet-stream");
		/*if(wjm != null){
			wjm = URLEncoder.encode(wjm, "utf-8");
		}*/
		if(wjm != null){
			if (agent.contains("iPhone") || agent.contains("Trident")){
				//iphone手机 微信内置浏览器 下载
				if (agent.contains("MicroMessenger")|| agent.contains("micromessenger")){
//						byte[] bytes = agent.contains("MSIE") ? wjm.getBytes() : wjm.getBytes("UTF-8");
//						wjm = new String(bytes, "ISO-8859-1");
					wjm = URLEncoder.encode(wjm, StandardCharsets.UTF_8);
					response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", wjm));// 文件名外的双引号处理firefox的空格截断问题
				}else {
					//iphone手机 非微信内置浏览器 下载 或 ie浏览器 下载
					wjm = URLEncoder.encode(wjm, StandardCharsets.UTF_8);
					response.setHeader("Content-Disposition","attachment;filename*=UTF-8''" + wjm);
				}
			}else {
				wjm = URLEncoder.encode(wjm, StandardCharsets.UTF_8);
				response.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", wjm));// 文件名外的双引号处理firefox的空格截断问题
			}
		}
		if (wjm != null) {
			log.error("文件下载 准备完成 wjm=" + URLDecoder.decode(wjm, StandardCharsets.UTF_8));
		}

		byte[] buffer = new byte[1024];
		BufferedInputStream bis = null;
		InputStream iStream;
		OutputStream os = null; //输出流
		try {
			iStream = new FileInputStream(filePath);
			os = response.getOutputStream();
			bis = new BufferedInputStream(iStream);
			int i = bis.read(buffer);
			while(i != -1){
				os.write(buffer);
				os.flush();
				i = bis.read(buffer);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("文件下载 写文件异常：" + e);
		}
		try {
			if(bis!=null)
				bis.close();
			if(os!=null) {
				os.flush();
				os.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("文件下载 关闭文件流异常：" + e);
			return null;
		}
		return null;
	}

	/**
	 * 含税单价统计
	 */
	@RequestMapping("/statistic/pagedataStatisticTaxPrice")
	@ResponseBody
	public Map<String, Object> pagedataStatisticTaxPrice(HtmxDto htmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> hsdjxx = htmxService.statisticTaxPrice(htmxDto);
		map.put("hsdjxx", hsdjxx);
		return map;
	}
	/**
	 * 获取物料到货情况
	 */
	@RequestMapping("/production/getContractArrivalInfo")
	@ResponseBody
	public Map<String, Object> getContractArrivalInfo(HtmxDto htmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<HtmxDto> htmxDtos = htmxService.getPagedtContractArrivalInfo(htmxDto);
		map.put("rows", htmxDtos);
		map.put("total", htmxDto.getTotalNumber());
		return map;
	}
	/**
	 * 查看营销合同信息
	 */
	@RequestMapping("/marketingContract/getMarketingContractUrl")
	@ResponseBody
	public ModelAndView getMarketingContractUrl(HttpServletRequest request) {
		String htid=request.getParameter("htid");
		ModelAndView mav = new ModelAndView("marketingContract/marketingContract/marketingContract_viewfj");
		YxhtDto yxhtDto = yxhtService.getDtoById(htid);
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_MARKETING_CONTRACT.getCode());
		fjcfbDto.setYwid(htid);
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		if (StringUtil.isNotBlank(yxhtDto.getKhid())){
			fjcfbDto.setYwid(yxhtDto.getKhid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_CUSTOM.getCode());
			List<FjcfbDto> fjcfbDtos=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			mav.addObject("fjcfbDtos",fjcfbDtos);
		}
		mav.addObject("yxhtDto",yxhtDto);
		mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 查看质量协议信息
	 */
	@RequestMapping("/production/getQualityUrl")
	@ResponseBody
	public ModelAndView getQualityUrl(HttpServletRequest request) {

		String zlxyid=request.getParameter("zlxyid");
		ZlxyDto zlxyDto = zlxyService.getDtoById(zlxyid);
		ZlxymxDto zlxymxDto=new ZlxymxDto();
		zlxymxDto.setZlxyid(zlxyid);
		List<ZlxymxDto> zlxymxDtos = zlxymxService.getDtoList(zlxymxDto);
		List<JcsjDto> sjxmhlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCTION_TYPE.getCode());//涉及项目号
		for (ZlxymxDto zlxymxDto1:zlxymxDtos){
			StringBuilder sjxmhmc= new StringBuilder();
			if (StringUtil.isNotBlank(zlxymxDto1.getSjxmh())){
				zlxymxDto1.setSjxmhs(zlxymxDto1.getSjxmh().trim().split(","));
				for(int i=0;i<zlxymxDto1.getSjxmhs().length;i++) {
					if(!CollectionUtils.isEmpty(sjxmhlist)) {
						for (JcsjDto jcsjDto : sjxmhlist) {
							if (jcsjDto.getCsid().equals(zlxymxDto1.getSjxmhs()[i]))
								sjxmhmc.append(",").append(jcsjDto.getCsmc());
						}
					}
				}
			}
			if (sjxmhmc.length()>0){
				sjxmhmc = new StringBuilder(sjxmhmc.substring(1));
			}
			zlxymxDto1.setSjxmhmc(sjxmhmc.toString());
		}
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_QUALITY_AGREEMENT_FILE.getCode());
		fjcfbDto.setYwid(zlxyid);
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);//质量协议合同附件
		ModelAndView mav=new ModelAndView("production/quality/quality_phone_auditView");
		FjcfbDto fjcfbDto_t=new FjcfbDto();
		fjcfbDto_t.setYwid(zlxyDto.getGysid());
		fjcfbDto_t.setYwlx(BusTypeEnum.IMP_SUBSUPPLIER.getCode());
		List<FjcfbDto> fjcfbDtos_t = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto_t);
		mav.addObject("t_fjcfbDtos", fjcfbDtos);
		mav.addObject("fjcfbDtos", fjcfbDtos_t);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("zlxyDto", zlxyDto);
		mav.addObject("zlxymxDtos", zlxymxDtos);
		return mav;
	}

	/**
	 * 查看合同付款信息
	 */
	@RequestMapping("/payment/getPaymentUrl")
	@ResponseBody
	public ModelAndView getPaymentUrl(HttpServletRequest request){
		ModelAndView mav=new ModelAndView("contract/payment/contract_pay_phoneview");
		//付款信息
	 	String htfkid=request.getParameter("htfkid");
		HtfkqkDto htfkqkDto=htfkqkService.getDtoById(htfkid);
		if(htfkqkDto==null)
			htfkqkDto=new HtfkqkDto();
		mav.addObject("htfkqkDto",htfkqkDto);
		HtfkmxDto htfkmxDto=new HtfkmxDto();
		htfkmxDto.setHtfkid(htfkid);
		List<HtfkmxDto> htfkmxDtos=htfkmxService.getDtoList(htfkmxDto);
		if(!CollectionUtils.isEmpty(htfkmxDtos)){
			List<String> ids=new ArrayList<>();
			for (HtfkmxDto dto : htfkmxDtos) {
				ids.add(dto.getHtid());
			}
			FjcfbDto fjcfbDto=new FjcfbDto();
			fjcfbDto.setYwids(ids);
			fjcfbDto.setYwlx(BusTypeEnum.IMP_UPLOADCONTRACT.getCode());
			List<FjcfbDto> fjcfbDtos=fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto);
			mav.addObject("t_fjcfbDtos",fjcfbDtos);
		}
		
		mav.addObject("htfkmxDtos",htfkmxDtos);
		mav.addObject("urlPrefix",urlPrefix);
	 	return mav;
	 }

	/**
	 * 复制保存供应商信息
	 */
	@RequestMapping(value = "/supplier/savesupplier")
	@ResponseBody
	public Map<String,Object> copysavesupplier(HttpServletRequest request){
		GysxxDto gysxxDto=new GysxxDto();
		gysxxDto.setGysmc(request.getParameter("gysmc"));
		gysxxDto.setGysdm(request.getParameter("gysdm"));
		gysxxDto.setGysjc(request.getParameter("gysjc"));
		gysxxDto.setDq(request.getParameter("dq"));
		gysxxDto.setFzrq(request.getParameter("fzrq"));
		gysxxDto.setLxr(request.getParameter("lxr"));
		gysxxDto.setDh(request.getParameter("dh"));
		gysxxDto.setSj(request.getParameter("sj"));
		gysxxDto.setQq(request.getParameter("qq"));
		gysxxDto.setWx(request.getParameter("wx"));
		gysxxDto.setCz(request.getParameter("cz"));
		gysxxDto.setYx(request.getParameter("yx"));
		gysxxDto.setKhh(request.getParameter("khh"));
		gysxxDto.setZh(request.getParameter("zh"));
		gysxxDto.setSl(request.getParameter("sl"));
		gysxxDto.setGfgllbmc(request.getParameter("gfgllbmc"));
		gysxxDto.setGfgllb(request.getParameter("gfgllb"));
		gysxxDto.setSfmc(request.getParameter("sfmc"));
		gysxxDto.setSf(request.getParameter("sf"));
		gysxxDto.setBz(request.getParameter("bz"));
		gysxxDto.setLrry(request.getParameter("yhid"));
		Map<String, Object> map = new HashMap<>();
		GysxxDto gysxxDto_mc = gysxxService.selectByGysmc(gysxxDto);
		boolean isSuccess;
		if(gysxxDto_mc==null){
			isSuccess=gysxxService.insertDto(gysxxDto);
		}else{
			isSuccess=false;
		}
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 *行政请购确认外部页面查看
	 */
	@RequestMapping("/production/administration/getPurchaseConfirmMessage")
	public ModelAndView getPurchaseConfirmMessage(HttpServletRequest request){
		ModelAndView mav=new ModelAndView("purchase/administration/confirmPurchase_phoneView");
		String qrid=request.getParameter("qrid");
		XzqgqrglDto xzqgqrglDto=xzqgqrglService.getDtoById(qrid);
		XzqgqrmxDto xzqgqrmxDto=new XzqgqrmxDto();
		xzqgqrmxDto.setQrid(qrid);
		List<XzqgqrmxDto> list=xzqgqrmxService.getDtoList(xzqgqrmxDto);
		mav.addObject("xzqgqrglDto",xzqgqrglDto);
		mav.addObject("qrmxlist",list);
		mav.addObject("urlPrefix", urlPrefix);

		//下面部分为审核历史页面需要的数据

		ShgcDto shgcDto = new ShgcDto();
		shgcDto.setShlb("AUDIT_REQUISITIONS");
		shgcDto.setYwid(qrid);

		ShxxDto shxxParam = new ShxxDto();
		ShgcDto d_shgcDto;
		// 未传递gcid，则根据ywid和shlb获取shgc
		if (StringUtil.isBlank(shgcDto.getGcid())) {
			d_shgcDto = shgcService.getDtoByYwid(shgcDto.getYwid());
		} else {// 传递了gcid，则根据gcid获取shgc
			d_shgcDto = shgcService.getDtoById(shgcDto.getGcid());
		}
		if (d_shgcDto == null) {// 数据不在审核中，表示通过
			shgcDto.setXlcxh("");// 审核通过时
		} else {
			shgcDto = d_shgcDto;
		}
		shxxParam.setShlb(shgcDto.getShlb());
		shxxParam.setShlbs(shgcDto.getShlbs());
		shxxParam.setYwid(shgcDto.getYwid());
		shxxParam.setGcid(shgcDto.getGcid());// 用过程id进行查询

		shxxParam.setSftg(null);
		shxxParam.setCommit(false);
		List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
		if (null == shxxList) {
			shxxList = new ArrayList<>();
		}

		// 获取的审核流程列表
		ShlcDto shlcParam = new ShlcDto();
		// gcid或shid参数不全的情况（已审核完，延期审核查看会传递gcid，所以gcid和shid都需要判断），根据审核信息获取流程
		if ((StringUtil.isBlank(shgcDto.getGcid()) || StringUtil.isBlank(shgcDto.getShid()))
				&& !CollectionUtils.isEmpty(shxxList)) {
			shlcParam.setShid(shxxList.get(0).getShid());
			shlcParam.setSqsj(shxxList.get(0).getSqsj());
		} else {
			shlcParam.setShid(shgcDto.getShid());
			shlcParam.setGcid(shgcDto.getGcid());// 处理旧流程判断用
		}
		if (StringUtil.isNotBlank(shlcParam.getShid())) {
			List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
			if (StringUtil.isNotBlank(shlcParam.getGcid())) {
				if(StringUtil.isNotBlank(shgcDto.getXlcxh())){
					for (ShlcDto shlc : shlcList) {
						if (shlc.getLcxh().equals(shgcDto.getXlcxh())) {// 相等则是当前流程
							// 当前流程做标记
							shlc.setCurrent(true);// 当前流程
							shlc.setAudited(true); //已审核流程
							break;// 跳出for循环
						}
						shlc.setAudited(true); //已审核流程
					}
				}
			}else{
				for (ShlcDto shlc : shlcList) {
					shlc.setAudited(true); //已审核流程
				}
			}
			mav.addObject("shlcList", shlcList);
		}
		mav.addObject("shgcDto", shgcDto);
		mav.addObject("shxxList", shxxList);
		return mav;
	}

	/**
	 * 到货审核通过查看页面
	 */
	@RequestMapping("/arrivalGoods/arrivalGoods/arrivalGoodsWsView")
	public ModelAndView arrivalGoodsWsView(HwxxDto hwxxDto) {
		ModelAndView mav = new ModelAndView("storehouse/dhxx/arrival_goods_WsView");
		List<HwxxDto> list = hwxxService.getDtoListByDhid(hwxxDto);
		mav.addObject("list", list);
		return mav;
	}
	/**
	 * 设备领料查看页面
	 */
	@RequestMapping("/storehouse/receiveMateriel/viewReceiveMateriel")
	public ModelAndView modRequisition(LlglDto llglDto) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/receiveMateriel_WsView");
		// 查看 领料信息
		LlglDto llxxDto = llglService.getDtoReceiveMaterielByLlid(llglDto.getLlid());
		mav.addObject("llxxDto", llxxDto);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	
	/**
	 * 发货物流信息查看
	 */
	@RequestMapping("/storehouse/ship/viewShip")
	public ModelAndView modRequisition(FhglDto fhglDto, FhmxDto fhmxDto) {
		ModelAndView mav = new ModelAndView("warehouse/ship/ship_WsView");
		// 查看 领料信息
		fhglDto = fhglService.getDtoByid(fhglDto);
		fhmxDto.setFhmxid(fhglDto.getFhid());
		List<FhmxDto> fhmxDtos=fhmxService.getDtoMxList(fhmxDto.getFhid());
		mav.addObject("fhglDto", fhglDto);
		mav.addObject("fhmxDtos",fhmxDtos);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
	/**
	 * 钉钉报销回调(分布式处理)
	 
	 */
	@RequestMapping("/production/reimburseCallback")
	@ResponseBody
	public Map<String,Object> reimburseCallback(HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		String data=request.getParameter("data");
		String processInstanceId = request.getParameter("processInstanceId");
		String processCode = request.getParameter("processCode");
		boolean isSuccess = ddbxglService.reimburseCallback(data,processInstanceId,processCode);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 8085端查看货物领料信息
	 */
	@RequestMapping("/production/viewMoreHwllxx")
	@ResponseBody
	public Map<String,Object> viewMoreHwllxx(HwllxxDto hwllxxDto){
		Map<String,Object> map=new HashMap<>();
		hwllxxDto=hwllxxService.getDtoById(hwllxxDto.getHwllid());
		map.put("hwllxxDto", hwllxxDto);
		return map;
	}
	/**
	 * 查看物流信息
	 */
	@RequestMapping("/storehouse/receiveMateriel/viewYwxxWithWlxx")
	public ModelAndView viewYwxxWithWlxx(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/receiveMateriel/view_YwxxWithWlxx");
		String ywid = request.getParameter("ywid");
		String ywlx = request.getParameter("ywlx");
		if ("ll".equals(ywlx)){
			HwllxxDto hwllxxDto = new HwllxxDto();
			hwllxxDto.setLlid(ywid);
			LlglDto dtoById = llglService.getDtoById(ywid);
			List<HwllxxDto> dtos = hwllxxService.getDtoList(hwllxxDto);
			mav.addObject("dto",dtoById);
			mav.addObject("dtos",dtos);
			mav.addObject("djlx","领料单");
		}else if ("fh".equals(ywlx)){
			FhglDto fhglDto = new FhglDto();
			fhglDto.setFhid(ywid);
			FhmxDto fhmxDto = new FhmxDto();
			fhmxDto.setFhid(ywid);
			FhglDto dtoByid = fhglService.getDtoByid(fhglDto);
			List<FhmxDto> dtoAllByFhid = fhmxService.getDtoAllByFhid(fhmxDto);
			mav.addObject("dto",dtoByid);
			mav.addObject("dtos",dtoAllByFhid);
			mav.addObject("djlx","发货单");
		}else if ("jcjy".equals(ywlx)){
			JcjyxxDto jcjyxxDto = new JcjyxxDto();
			jcjyxxDto.setJcjyid(ywid);
			JcjyglDto dtoById = jcjyglServicel.getDtoById(ywid);
			List<JcjyxxDto> dtoListInfo = jcjyxxService.getDtoListInfo(jcjyxxDto);
			mav.addObject("dto",dtoById);
			mav.addObject("dtos",dtoListInfo);
			mav.addObject("djlx","借出借用单");
		}else if ("xs".equals(ywlx)){
			XsmxDto xsmxDto = new XsmxDto();
			xsmxDto.setXsid(ywid);
			XsglDto dtoById = xsglService.getDtoById(ywid);
			List<XsmxDto> List = xsmxService.getDtoList(xsmxDto);
			mav.addObject("dto",dtoById);
			mav.addObject("dtos",List);
			mav.addObject("djlx","销售单");
		}
		List<WlxxDto> dtoList = wlxxService.getDtoListById(ywid);
		FjcfbDto fjcfbDto=new FjcfbDto();
		for (WlxxDto wlxxDto:dtoList){
			fjcfbDto.setZywid(wlxxDto.getWlxxid());
			fjcfbDto.setYwid(wlxxDto.getYwid());
			List<FjcfbDto> fjcfbDtos = fjcfbService.getListByZywid(fjcfbDto);
			wlxxDto.setFjcfbDtos(fjcfbDtos);
		}
		mav.addObject("dtoList",dtoList);
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("ywlx",ywlx);
		mav.addObject("ywid",ywid);
		return mav;
	}

	/**
	 * 钉钉销售订单审核回调(分布式处理)
	 */
	@RequestMapping("/production/saleOrderCallback")
	@ResponseBody
	public Map<String,Object> saleOrderCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> t_map=new HashMap<>();//用于接收返回值
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
			if("1".equals(ddspbcbj)) {
				if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
						|| ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
					json_obj.put("cljg", "0");
				}
				ddspglService.insertInfo(json_obj);
			}
			boolean result = xsglService.callbackXsglAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
				//更新钉钉审批管理表数据
				//更新审批管理信息,失败
				DdspglDto ddspglDto=new DdspglDto();
				ddspglDto.setCljg("0");
				ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
				ddspglService.updatecljg(ddspglDto);
			}
			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}


	/**
	 * 查看销售订单信息
	 */
	@RequestMapping("/production/dingTalkViewSale")
	@ResponseBody
	public ModelAndView dingTalkViewSale(HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("storehouse/sale/sale_auditView");
		String xsid=request.getParameter("xsid");
		XsglDto xsglDto = xsglService.getDtoById(xsid);
		if(xsglDto==null) {
			xsglDto=new XsglDto();
		}
		if (StringUtil.isNotBlank(xsglDto.getKhjc())){
			FjcfbDto fjcfbDto=new FjcfbDto();
			fjcfbDto.setYwid(xsglDto.getKhjc());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_CUSTOM.getCode());
			List<FjcfbDto> fjcfbDtos=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			mav.addObject("t_fjcfbDtos",fjcfbDtos);
		}
		XsmxDto xsmxDto = new XsmxDto();
		xsmxDto.setXsid(xsid);
		List<XsmxDto> xsmxDtos = xsmxService.getDtoList(xsmxDto);
		mav.addObject("xsglDto", xsglDto);
		mav.addObject("xsmxDtos", xsmxDtos);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 查看销售明细信息
	 */
	@RequestMapping("/production/viewMoreXsmx")
	@ResponseBody
	public Map<String,Object> viewMoreXsmx(XsmxDto xsmxDto){
		Map<String,Object> map=new HashMap<>();
		xsmxDto=xsmxService.getDto(xsmxDto);
		map.put("xsmxDto", xsmxDto);
		return map;
	}
	/**
	 * 质量协议审核回调(分布式处理)
	 
	 */
	@RequestMapping("/production/aduitQualityCallback")
	@ResponseBody
	public Map<String,Object> aduitQualityCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> t_map=new HashMap<>();//用于接收返回值
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
			if("1".equals(ddspbcbj)) {
				if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
						|| ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
					json_obj.put("cljg", "0");
				}
				ddspglService.insertInfo(json_obj);
			}
			boolean result = zlxyService.callbackQualityAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
				//更新钉钉审批管理表数据
				//更新审批管理信息,失败
				DdspglDto ddspglDto=new DdspglDto();
				ddspglDto.setCljg("0");
				ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
				ddspglService.updatecljg(ddspglDto);
			}

			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}
	/**
	 * 	质量协议明细数据
	 */
	@RequestMapping("/production/pagedataAgreementDetatils")
	@ResponseBody
	public Map<String, Object> pagedataAgreementDetatils(ZlxymxDto zlxymxDto) {
		Map<String, Object> map = new HashMap<>();
		zlxymxDto=zlxymxService.getDtoById(zlxymxDto.getZlxymxid());
		List<JcsjDto> sjxmhlist=redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.PRODUCTION_TYPE.getCode());//涉及项目号
			StringBuilder sjxmhmc= new StringBuilder();
			if (StringUtil.isNotBlank(zlxymxDto.getSjxmh())){
				zlxymxDto.setSjxmhs(zlxymxDto.getSjxmh().trim().split(","));
				for(int i=0;i<zlxymxDto.getSjxmhs().length;i++) {
					if(!CollectionUtils.isEmpty(sjxmhlist)) {
						for (JcsjDto jcsjDto : sjxmhlist) {
							if (jcsjDto.getCsid().equals(zlxymxDto.getSjxmhs()[i]))
								sjxmhmc.append(",").append(jcsjDto.getCsmc());
						}
					}
				}
			}
			if (sjxmhmc.length()>0){
				sjxmhmc = new StringBuilder(sjxmhmc.substring(1));
			}
		zlxymxDto.setSjxmhmc(sjxmhmc.toString());
		map.put("zlxymxDto", zlxymxDto);
		return map;
	}
	/**
	 * 获取关联文件
	 */
	@RequestMapping("/production/document/pagedataGetGlWj")
	@ResponseBody
	public Map<String, Object> pagedataGetGlWj(GlwjxxDto glwjxxDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(glwjxxDto.getWjid())){
			List<GlwjxxDto> rows = glwjxxService.getDtoList(glwjxxDto);
			map.put("rows", rows);
		}else {
			map.put("rows", new ArrayList<>());
		}
		return map;
	}
	/** 获取文件权限
	 */
	@RequestMapping("/production/document/pagedataGetWjqx")
	@ResponseBody
	public Map<String, Object> pagedataGetWjqx(WjglDto wjglDto) {
		Map<String, Object> map = new HashMap<>();
		List<WjglDto> rows = new ArrayList<>();
		if ("VIEW".equals(wjglDto.getQxlx())){
			rows = wjqxService.getViewJs(wjglDto);
		}else if ("STUDY".equals(wjglDto.getQxlx())){
			rows = wjqxService.getStudyJs(wjglDto);
		}else if ("DOWNLOAD".equals(wjglDto.getQxlx())){
			rows = wjqxService.getDownloadJs(wjglDto);
		}
		map.put("rows", rows);
		return map;
	}
	/**
	 * 选择角色列表页面
	 */
	@RequestMapping("/production/role/pagedataListRole")
	public ModelAndView pagedataListRole(WjglDto wjglDto) {
		ModelAndView mav = new ModelAndView("production/document/role_choose");
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("njsids",StringUtil.join(wjglDto.getNjsids(), ","));
		mav.addObject("qxlx",wjglDto.getQxlx());
		return mav;
	}
	/** 获取角色
	 */
	@RequestMapping("/production/role/pagedataGetUnSelectJs")
	@ResponseBody
	public Map<String, Object> pagedataGetUnSelectJs(WjglDto wjglDto) {
		Map<String, Object> map = new HashMap<>();
		List<WjglDto> rows = wjqxService.getPagedRoles(wjglDto);
		map.put("rows", rows);
		map.put("total", wjglDto.getTotalNumber());
		return map;
	}
	/**
	 * 附件列表上传页面
	 */
	@RequestMapping("/production/viewFj")
	public ModelAndView viewFj(String ywid,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("storehouse/inspection/inspection_uploadFile");
		// 查询正式文件并显示
		List<FjcfbDto> fjcfbDtos = fjcfbService.getFjcfbDtoByYwid(ywid);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		mav.addObject("ywlx", request.getParameter("ywlx"));
		mav.addObject("fjids", "");
		mav.addObject("ywid", ywid);
		mav.addObject("zywid", "");
		mav.addObject("ckbj", "1");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 明细列表数据
	 */
	@RequestMapping("/sale/pagedataListSalesDetails")
	@ResponseBody
	public Map<String, Object> pagedataListSalesDetails(FhmxDto fhmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<FhmxDto> xsmxDtoList=fhmxService.getPagedForException(fhmxDto);
		map.put("total",fhmxDto.getTotalNumber());
		map.put("rows",xsmxDtoList);
		return map;
	}

	/**
	 * 借出借用明细列表
	 */
	@RequestMapping(value = "/borrowing/pagedataListBorrowingDetails")
	@ResponseBody
	public Map<String, Object> pagedataListBorrowingDetails(JcjymxDto jcjymxDto) {
		List<JcjymxDto> jcjymxDtos = jcjymxService.getPagedDtoList(jcjymxDto);
		Map<String, Object> map = new HashMap<>();
		map.put("total", jcjymxDto.getTotalNumber());
		map.put("rows", jcjymxDtos);
		return map;
	}

	/**
	 * 获取物流信息
	 */
	@RequestMapping("/requisition/pagedataListPickingDetails")
	@ResponseBody
	public Map<String, Object> pagedataListPickingDetails(CkmxDto ckmxDto){
		Map<String, Object> map = new HashMap<>();
		List<CkmxDto> t_List = ckmxService.getPagedForException(ckmxDto);
		map.put("total", ckmxDto.getTotalNumber());
		map.put("rows", t_List);
		return map;
	}
	/**
	 * 获取物流信息
	 */
	@RequestMapping("/requisition/pagedataGetWlxxByYwid")
	@ResponseBody
	public Map<String, Object> pagedataGetWlxxByYwid(WlxxDto wlxxDto){
		Map<String, Object> map = new HashMap<>();
		List<WlxxDto> dtoList = wlxxService.getDtoListById(wlxxDto.getYwid());
		map.put("rows",dtoList);
		map.put("total",wlxxDto.getTotalNumber());
		return map;
	}

	/**
	 * @description 获取销售订单信息
	 */
	@RequestMapping("/sale/getSaleInfo")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSaleInfo(XsglDto xsglDto) {
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("pageNumber", xsglDto.getPageNumber());
		paramMap.add("pageSize", xsglDto.getPageSize());
		paramMap.add("sortOrder", xsglDto.getSortOrder());
		paramMap.add("sortLastOrder", xsglDto.getSortLastOrder());
		paramMap.add("sortName", xsglDto.getSortName());
		paramMap.add("sortLastName", xsglDto.getSortLastName());
		paramMap.add("ddrqstart", xsglDto.getDdrqstart());
		paramMap.add("ddrqend", xsglDto.getDdrqend());
		paramMap.add("khjc", xsglDto.getKhjc());
		Object xtsz=redisUtil.hget("matridx_xtsz","business.sale.address");
		String address="";
		if(xtsz!=null){
			JSONObject job=JSONObject.parseObject(String.valueOf(xtsz));
			address=job.getString("szz");
		}
		String url=address+"/ws/sale/pageGetSaleInfo";
		return restTemplate.postForObject(url, paramMap, Map.class);
	}


	/**
	 * @description 获取销售订单明细信息
	 */
	@RequestMapping("/sale/viewSaleDetails")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Map<String, Object> viewSaleDetails(XsglDto xsglDto) {
		RestTemplate restTemplate = new RestTemplate();
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("xsid", xsglDto.getXsid());
		Object xtsz=redisUtil.hget("matridx_xtsz","business.sale.address");
		String address="";
		if(xtsz!=null){
			JSONObject job=JSONObject.parseObject(String.valueOf(xtsz));
			address=job.getString("szz");
		}
		String url=address+"/ws/sale/pageGetSaleInfo";
		return restTemplate.postForObject(url, paramMap, Map.class);
	}

	/**
	 * @description 提供一个获取销售订单的外部接口
	 */
	@RequestMapping("/sale/pageGetSaleInfo")
	@ResponseBody
	public Map<String, Object> getPageDtoSaleList(XsglDto xsglDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(xsglDto.getXsid())){
			XsglDto dtoById = xsglService.getDtoById(xsglDto.getXsid());
			XsmxDto xsmxDto=new XsmxDto();
			xsmxDto.setXsid(xsglDto.getXsid());
			List<XsmxDto> xsmxDtos = xsmxService.getDtoList(xsmxDto);
			map.put("xsglDto", dtoById);
			map.put("xsmxDtos", xsmxDtos);
		}else {
			List<XsglDto> xsglDtos = xsglService.getPagedSaleData(xsglDto);
			map.put("total", xsglDto.getTotalNumber());
			map.put("rows", xsglDtos);
		}
		return map;
	}

	@RequestMapping("/production/aduitEvaluationCallback")
	@ResponseBody
	public Map<String,Object> aduitEvaluationCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> t_map=new HashMap<>();//用于接收返回值
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
			if("1".equals(ddspbcbj)) {
				if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
						|| ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
					json_obj.put("cljg", "0");
				}
				ddspglService.insertInfo(json_obj);
			}
			boolean result = gfyzglService.callbackEvaluationAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
				//更新钉钉审批管理表数据
				//更新审批管理信息,失败
				DdspglDto ddspglDto=new DdspglDto();
				ddspglDto.setCljg("0");
				ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
				ddspglService.updatecljg(ddspglDto);
			}

			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}

	/**
	 * @Description: 查看供方验证信息
	 * @param request
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2024/6/25 15:12
	 */
	@RequestMapping("/evaluation/evaluationView")
	@ResponseBody
	public ModelAndView evaluationView(HttpServletRequest request) {
		String gfyzid=request.getParameter("gfyzid");
		ModelAndView mav = new ModelAndView("warehouse/evaluation/evaluationAuditView");
		GfyzmxDto gfyzmxDto = new GfyzmxDto();
		gfyzmxDto.setGfyzid(gfyzid);
		List<GfyzmxDto> gfyzmxDtoList = gfyzmxService.getListByGfyzid(gfyzmxDto);
		mav.addObject("gfyzmxDtoList",gfyzmxDtoList);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * @Description: 返回供方验证明细数据
	 * @param gfyzmxDto
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/6/25 15:45
	 */
	@RequestMapping("/evaluation/viewMoreEvaluation")
	@ResponseBody
	public Map<String,Object> viewMorePurchase(GfyzmxDto gfyzmxDto){
		Map<String,Object> map=new HashMap<>();
		gfyzmxDto=gfyzmxService.getDtoById(gfyzmxDto.getYzmxid());
		map.put("gfyzmxDto", gfyzmxDto);
		return map;
	}

	@RequestMapping("/production/aduitAppraiseCallback")
	@ResponseBody
	public Map<String,Object> aduitAppraiseCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> t_map=new HashMap<>();//用于接收返回值
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
			if("1".equals(ddspbcbj)) {
				if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
						|| ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
					json_obj.put("cljg", "0");
				}
				ddspglService.insertInfo(json_obj);
			}
			boolean result = gfpjbService.callbackAppraiseAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
				//更新钉钉审批管理表数据
				//更新审批管理信息,失败
				DdspglDto ddspglDto=new DdspglDto();
				ddspglDto.setCljg("0");
				ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
				ddspglService.updatecljg(ddspglDto);
			}

			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}

	/**
	 * @Description: 查看供方评价信息
	 * @param request
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2024/6/25 15:12
	 */
	@RequestMapping("/appraise/appraiseView")
	@ResponseBody
	public ModelAndView appraiseView(HttpServletRequest request) {
		String gfpjid=request.getParameter("gfpjid");
		ModelAndView mav = new ModelAndView("warehouse/appraise/appraiseAuditView");
		GfpjmxDto gfpjmxDto = new GfpjmxDto();
		gfpjmxDto.setGfpjid(gfpjid);
		List<GfpjmxDto> gfpjmxDtoList = gfpjmxService.getDtoList(gfpjmxDto);
		mav.addObject("gfpjmxDtoList",gfpjmxDtoList);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * @Description: 返回供方验证明细数据
	 * @param gfpjmxDto
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/6/25 15:45
	 */
	@RequestMapping("/appraise/viewMoreAppraise")
	@ResponseBody
	public Map<String,Object> viewMoreAppraise(GfpjmxDto gfpjmxDto){
		Map<String,Object> map=new HashMap<>();
		gfpjmxDto=gfpjmxService.getDtoById(gfpjmxDto.getPjmxid());
		map.put("gfpjmxDto", gfpjmxDto);
		return map;
	}

	/**
	 * @Description: 供方绩效查看
	 * @param gfjxglDto
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2024/7/4 16:57
	 */
	@RequestMapping(value = "/performance/performanceView")
	public ModelAndView performanceView(GfjxglDto gfjxglDto) {
		ModelAndView mav = new ModelAndView("warehouse/performance/performanceAuditView");
		GfjxglDto gfjxglDtoT = gfjxglService.getDtoById(gfjxglDto.getGfjxid());
		GfjxmxDto gfjxmxDto = new GfjxmxDto();
		gfjxmxDto.setGfjxid(gfjxglDto.getGfjxid());
		List<GfjxmxDto> gfjxmxDtoList = gfjxmxService.getDtoList(gfjxmxDto);
		String gfjxmxJson = JSON.toJSONString(gfjxmxDtoList);
		mav.addObject("gfjxmxDtoList", gfjxmxDtoList);
		mav.addObject("gfjxglDto", gfjxglDtoT);
		mav.addObject("gfjxmxJson", gfjxmxJson);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * @Description: 供方绩效审核回调
	 * @param request
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/7/4 17:05
	 */
	@RequestMapping("/production/aduitPerformanceCallback")
	@ResponseBody
	public Map<String,Object> aduitPerformanceCallback(HttpServletRequest request){
		String obj=request.getParameter("obj");
		JSONObject json_obj=JSON.parseObject(obj);
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> t_map=new HashMap<>();//用于接收返回值
		try {
			String ddspbcbj=request.getParameter("ddspbcbj");
			if("1".equals(ddspbcbj)) {
				if(("finish".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_TASK_CHANGE.equals(json_obj.getString("EventType")))
						|| ("terminate".equals(json_obj.getString("type")) && DingTalkUtil.BPMS_INSTANCE_CHANGE.equals(json_obj.getString("EventType")))) {
					json_obj.put("cljg", "0");
				}
				ddspglService.insertInfo(json_obj);
			}
			boolean result = gfjxglService.callbackPerformanceAduit(json_obj, request,t_map);
			if(result) {
				map.put("status", "success");
				map.put("message", "回调成功!");
			}else {
				map.put("status", "fail");
				map.put("message", "回调失败!");
			}
		} catch (BusinessException e) {
			if(StringUtil.isNotBlank((String) t_map.get("ddspglid"))) {
				//更新钉钉审批管理表数据
				//更新审批管理信息,失败
				DdspglDto ddspglDto=new DdspglDto();
				ddspglDto.setCljg("0");
				ddspglDto.setDdspglid((String) t_map.get("ddspglid"));
				ddspglService.updatecljg(ddspglDto);
			}

			map.put("status", "exception");
			map.put("message", e.getMessage());
			// TODO Auto-generated catch block
		}
		return map;
	}


	/**
	 *
	 */
	@RequestMapping("/financialStatistics/pagedataFinancialStatisticsOnPhone")
	public ModelAndView pagedataFinancialStatisticsOnPhone(String yhm ,String ddid, String jgid, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("financialStatistics/financialStatisticsPage");
		mav.addObject("urlPrefix", urlPrefix);
		Map<String, Object> detail = ysglService.getDetail(jgid);
		mav.addAllObjects(detail);
		return mav;
	}

	@RequestMapping("/querystock/viewQueryStock")
	public ModelAndView viewQueryStock() {
		Object object = redisUtil.get("QUERY_STOCK_MESSAGE");
		List<Map<String, String>> hwxxDtos = new ArrayList<>();
		if(object!=null){
			hwxxDtos = (List<Map<String, String>>) JSON.parse(object.toString());
		}
		Object objectT = redisUtil.get("QUERY_OA_STOCK_MESSAGE");
		List<Map<String, String>> hwxxDtoList = new ArrayList<>();
		if(objectT!=null){
			hwxxDtoList = (List<Map<String, String>>)JSON.parse(objectT.toString());
		}
		ModelAndView mav = new ModelAndView("storehouse/stock/viewQueryStock");
		mav.addObject("hwxxDtos", hwxxDtos);
		mav.addObject("hwxxDtoList", hwxxDtoList);
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}
}
