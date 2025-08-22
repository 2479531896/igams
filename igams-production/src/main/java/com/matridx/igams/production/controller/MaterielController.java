package com.matridx.igams.production.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.production.dao.entities.*;
import com.matridx.igams.production.dao.matridxsql.IMatridxInventoryDao;
import com.matridx.igams.production.service.svcinterface.IBmwlService;
import com.matridx.igams.storehouse.dao.entities.CkhwxxDto;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.production.service.svcinterface.IQgcglService;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.igams.production.service.svcinterface.IWlgllsbService;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/production")
public class MaterielController extends BaseBasicController{
	public static final String MSG = "message";	//返回信息
	public static final String RESULT = "result";	//执行结果
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	IJcsjService jcsjService;
	
	@Autowired
	IWlglService wlglService;
	
	@Autowired
	IWlgllsbService wlgllsbService;
	
	@Autowired
	IQgcglService qgcglService;
	
	@Autowired
	IQgglService qgglService;
	
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IMatridxInventoryDao matridxInventoryDao;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	ICkhwxxService ckhwxxService;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}

	@Autowired
	private IXtszService xtszService;
	
	
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	
	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
    //是否发送rabbit标记     1：发送
	@Value("${matridx.rabbit.systemconfigflg:}")
	private String systemconfigflg;
	
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IHwxxService hwxxService;
	@Autowired
	IBmwlService bmwlService;
	/**
	 * 物料列表页面
	 */
	@RequestMapping(value ="/materiel/pageListMater")
	public ModelAndView pageListMater(HttpServletRequest request){
		ModelAndView mav = new ModelAndView("production/materiel/mater_list");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.MATERIEL_TYPE,BasicDataTypeEnum.MATERIELQUALITY_TYPE,BasicDataTypeEnum.MATERIEL_SUBTYPE_COLLECTIVELY,BasicDataTypeEnum.MATERIEL_SAFE_TYPE});
		mav.addObject("wllblist", jclist.get(BasicDataTypeEnum.MATERIEL_TYPE.getCode()));
		mav.addObject("wlzlbtclist", jclist.get(BasicDataTypeEnum.MATERIEL_SUBTYPE_COLLECTIVELY.getCode()));
		mav.addObject("lblist", jclist.get(BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode()));
		mav.addObject("wlaqlblist", jclist.get(BasicDataTypeEnum.MATERIEL_SAFE_TYPE.getCode()));
		WlglDto t_wlglDto = new WlglDto();
		t_wlglDto.setAuditType(AuditTypeEnum.AUDIT_METRIEL.getCode());
		t_wlglDto.setAuditModType(AuditTypeEnum.AUDIT_METRIELMOD.getCode());
		//获取采购车已加入的物料数量
		User user = getLoginInfo(request); 
		QgcglDto qgcglDto=new QgcglDto();
		qgcglDto.setRyid(user.getYhid());
        List<QgcglDto> qglist=qgcglService.getQgcList(qgcglDto);

		if(!CollectionUtils.isEmpty(qglist)) {
			t_wlglDto.setYcgzlsl(String.valueOf(qglist.size()));
			StringBuilder ids= new StringBuilder();
			for (QgcglDto dto : qglist) {
				ids.append(",").append(dto.getWlid());
			}
			ids = new StringBuilder(ids.substring(1));
			t_wlglDto.setQg_ids(ids.toString());
		}
		mav.addObject("wlglDto",t_wlglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 物料列表
	 */
	@RequestMapping(value ="/materiel/pageGetListMater")
	@ResponseBody
	public Map<String,Object> pageGetListMater(WlglDto wlglDto){
		List<WlglDto> t_List = wlglService.getPagedDtoList(wlglDto);
		
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", wlglDto.getTotalNumber());
		result.put("rows", t_List);
		result.put("wllblist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.MATERIEL_TYPE.getCode()));
		result.put("wlzlbtclist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.MATERIEL_SUBTYPE_COLLECTIVELY.getCode()));
		result.put("lblist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode()));
		result.put("wlaqlblist", redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.MATERIEL_SAFE_TYPE.getCode()));
		return result;
	}
	
	//物料统计数据
	/*@RequestMapping(value ="/materiel/ansyStatisMater")
	@ResponseBody
	public Map<String,Object> ansyStatisMater(WlglDto wlglDto){
		
		//构造成Json的格式传递
		Map<String,Object> result = wlglService.getStatisMater(wlglDto);
		
		return result;
	}*/

	//物料查看
	@RequestMapping(value="/materiel/viewMater")
	public ModelAndView viewMater(WlglDto wlglDto){
		ModelAndView mav = new ModelAndView("production/materiel/mater_view");
		WlglDto t_ybglDto = wlglService.getDtoById(wlglDto.getWlid());
		String hq=wlglService.selectBiggestHq(wlglDto.getWlid());
		if (StringUtil.isNotBlank(hq)){
			t_ybglDto.setHq(hq);
		}
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[] {BasicDataTypeEnum.PRODUCTION_TYPE});
		
		if(t_ybglDto!=null){
			// 将sscplb由字符串转换为数组存入sscplbs中
			String sscplb = t_ybglDto.getSscplb();
			if(StringUtil.isNotBlank(sscplb)) {
				t_ybglDto.setSscplbs(sscplb.trim().split(","));
				// 设置一个选择标记，若sscplbs中有与jcsj中的MATERIELQUALITY_TYPE中的一样的，则设置标记为1，即选中
				for(int i=0;i<t_ybglDto.getSscplbs().length;i++) {
					if(jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode())!=null) {
						for(int j=0;j<jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).size();j++) {
							if(jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).get(j).getCsid().equals(t_ybglDto.getSscplbs()[i]))
								jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).get(j).setChecked("1");
						}
					}
				}
			}		
			mav.addObject("sscplblist",jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()));
			mav.addObject("wlglDto", t_ybglDto);
		}else {
			mav.addObject("wlglDto", new WlglDto());
		}
		//从物料临时表查询审核历史
		WlgllsbDto wlgllsbDto = new WlgllsbDto();
		wlgllsbDto.setWlid(wlglDto.getWlid());
		List<WlgllsbDto> wllsbDtos = wlgllsbService.getDtoListByWlid(wlgllsbDto);
		mav.addObject("wllsbDtos", wllsbDtos);		
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 物料查看
	 */
	@RequestMapping(value="/materiel/pagedataViewMater")
	public ModelAndView pagedataViewMater(WlglDto wlglDto){
		ModelAndView mav = new ModelAndView("production/materiel/mater_view");
		WlglDto t_ybglDto = wlglService.getDtoById(wlglDto.getWlid());
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[] {BasicDataTypeEnum.PRODUCTION_TYPE});
		
		if(t_ybglDto!=null){
			// 将sscplb由字符串转换为数组存入sscplbs中
			String sscplb = t_ybglDto.getSscplb();
			if(StringUtil.isNotBlank(sscplb)) {
				t_ybglDto.setSscplbs(sscplb.trim().split(","));
				// 设置一个选择标记，若sscplbs中有与jcsj中的MATERIELQUALITY_TYPE中的一样的，则设置标记为1，即选中
				for(int i=0;i<t_ybglDto.getSscplbs().length;i++) {
					if(jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode())!=null) {
						for(int j=0;j<jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).size();j++) {
							if(jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).get(j).getCsid().equals(t_ybglDto.getSscplbs()[i]))
								jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).get(j).setChecked("1");
						}
					}
				}
			}
			mav.addObject("sscplblist",jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()));
			mav.addObject("wlglDto", t_ybglDto);
		}else {
			mav.addObject("wlglDto", new WlglDto());
		}
		//从物料临时表查询审核历史
		WlgllsbDto wlgllsbDto = new WlgllsbDto();
		wlgllsbDto.setWlid(wlglDto.getWlid());
		List<WlgllsbDto> wllsbDtos = wlgllsbService.getDtoListByWlid(wlgllsbDto);
		mav.addObject("wllsbDtos", wllsbDtos);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 物料库存信息查看
	 */
	@RequestMapping(value="/materiel/pagedataViewMaterKcInfo")
	public ModelAndView pagedataViewMaterKcInfo(HwxxDto hwxxDto,HttpServletRequest request){
		ModelAndView mav = new ModelAndView("production/materiel/mater_viewkcInfo");
		List<HwxxDto> hwxxDtos = hwxxService.getWlkcInfoGroupBy(hwxxDto);
		Map<String,Object> map=new HashMap<>();
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
		map.put("wlid",hwxxDto.getWlid());
		mav.addObject("wlid", hwxxDto.getWlid());
		List<HwxxDto> llhwxxDtos=hwxxService.selectFiveMonthsLlsl(map);
		WlglDto wlglDto=wlglService.getDtoById(hwxxDto.getWlid());
		for (String yf : yfs) {
			boolean flag = false;
			for (HwxxDto hwxxDto1 : llhwxxDtos) {
				if (yf.equals(hwxxDto1.getYf())) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				String lcsl = "0.00" + wlglDto.getJldw();
				HwxxDto hwxxDto_t = new HwxxDto();
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
		List<HwxxDto> ztslHwxxDtos=hwxxService.selectZtsl(hwxxDto);
		mav.addObject("hwxxDtos", hwxxDtos);
		mav.addObject("llhwxxDtos", llhwxxDtos);
		mav.addObject("ztslHwxxDtos", ztslHwxxDtos);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("sign", request.getParameter("sign"));
		return mav;
	}
	/**
	 * 货期数据
	 */
	@RequestMapping(value = "/materiel/pagedataGetHq")
	@ResponseBody
	public Map<String, Object> pagedataGetHq(HwxxDto hwxxDto){
		Map<String,Object> map=new HashMap<>();
		List<HwxxDto> hqlist=hwxxService.getLastestHq(hwxxDto);
		map.put("hqxxType",hqlist);
		return map;
	}
	/**
	 * 钉钉物料查看
	 */
	@RequestMapping(value = "/materiel/minidataSelectSameMater")
	@ResponseBody
	public Map<String, Object> minidataSelectSameMater(WlglDto wlglDto){
		Map<String,Object> map=new HashMap<>();
		WlglDto t_ybglDto = wlglService.getDtoById(wlglDto.getWlid());
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[] {BasicDataTypeEnum.PRODUCTION_TYPE});
		
		if(t_ybglDto!=null){
			StringBuilder sscplbmc= new StringBuilder();
			// 将sscplb由字符串转换为数组存入sscplbs中
			String sscplb = t_ybglDto.getSscplb();
			if(StringUtil.isNotBlank(sscplb)) {
				t_ybglDto.setSscplbs(sscplb.trim().split(","));

				// 设置一个选择标记，若sscplbs中有与jcsj中的MATERIELQUALITY_TYPE中的一样的，则设置标记为1，即选中
				for(int i=0;i<t_ybglDto.getSscplbs().length;i++) {
					if(jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode())!=null) {
						for(int j=0;j<jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).size();j++) {
							if(jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).get(j).getCsid().equals(t_ybglDto.getSscplbs()[i])){
								jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).get(j).setChecked("1");
							sscplbmc.append(",").append(jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).get(j).getCsmc());
							}
						}
					}
				}
			}
			if (sscplbmc.length()>0){
				sscplbmc = new StringBuilder(sscplbmc.substring(1));
				t_ybglDto.setSscplbmc(sscplbmc.toString());
			}
			map.put("sscplblist",jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()));
			map.put("wlglDto", t_ybglDto);
		}else {
			map.put("wlglDto", new WlglDto());
		}
		//从物料临时表查询审核历史
		WlgllsbDto wlgllsbDto = new WlgllsbDto();
		wlgllsbDto.setWlid(wlglDto.getWlid());
		List<WlgllsbDto> wllsbDtos = wlgllsbService.getDtoListByWlid(wlgllsbDto);
		map.put("wllsbDtos", wllsbDtos);
		return map;
	}
	
	/**
	 * 查看修改内容
	 */
	@RequestMapping(value="/materiel/pagedataViewModMaterCompare")
	public ModelAndView viewModMaterCompare(WlgllsbDto wlgllsbDto){
		ModelAndView mav = new ModelAndView("production/materiel/mater_viewCompare");
		List<WlgllsbDto> wllsbDtos = wlgllsbService.getDtoListByWlid(wlgllsbDto);
		
		//根据编号查询两条对比数据
		WlgllsbDto n_wlgllsbDto = new WlgllsbDto();
		int index = Integer.parseInt(wlgllsbDto.getExtend_1());
		WlgllsbDto t__wlgllsbDto = wllsbDtos.get(index);
		//判断数据是否为最后一条
		if(index < wllsbDtos.size()-1){
			String nextlsid = wllsbDtos.get(index+1).getLsid();
			n_wlgllsbDto = wlgllsbService.getDtoById(nextlsid);
		}
		mav.addObject("wlgllsbDto", t__wlgllsbDto);
		mav.addObject("n_wlgllsbDto", n_wlgllsbDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 钉钉查看修改内容
	 */
	@RequestMapping(value = "/materiel/minidataViewModMaterCompare")
	@ResponseBody
	public Map<String, Object> minidataViewModMaterCompare(WlgllsbDto wlgllsbDto){
		Map<String,Object> map=new HashMap<>();
		List<WlgllsbDto> wllsbDtos = wlgllsbService.getDtoListByWlid(wlgllsbDto);
		//根据编号查询两条对比数据
		int index=0;
		if (StringUtil.isNotBlank(wlgllsbDto.getExtend_1())) {
		 index = Integer.parseInt(wlgllsbDto.getExtend_1());
		}
		WlgllsbDto t__wlgllsbDto = wllsbDtos.get(index);
		map.put("wlgllsbDto", t__wlgllsbDto);
		return map;
	}
	/**
	 * 物料新增
	 */
	@RequestMapping(value ="/materiel/addMater")
	public ModelAndView addMater(WlglDto wlglDto){
		ModelAndView mav = new ModelAndView("production/materiel/mater_edit");
		wlglDto.setFormAction("add");
		mav.addObject("wlglDto", wlglDto);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.MATERIEL_TYPE,BasicDataTypeEnum.MATERIEL_SUBTYPE,BasicDataTypeEnum.MATERIEL_SUBTYPE_COLLECTIVELY,BasicDataTypeEnum.MATERIEL_SAFE_TYPE});
		mav.addObject("wlzlblist", jclist.get(BasicDataTypeEnum.MATERIEL_SUBTYPE.getCode()));
		mav.addObject("wllblist", jclist.get(BasicDataTypeEnum.MATERIEL_TYPE.getCode()));
		mav.addObject("wlaqlblist", jclist.get(BasicDataTypeEnum.MATERIEL_SAFE_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("wlzlbtclist", jclist.get(BasicDataTypeEnum.MATERIEL_SUBTYPE_COLLECTIVELY.getCode()));
		return mav;
	}
	
	/**
	 * 物料模糊查询
	 */
	@RequestMapping(value = "/materiel/pagedataSelectSameMater")
	@ResponseBody
	public Map<String, Object> pagedataSelectSameMater(WlglDto wlglDto){
		//获取用户信息
		List<WlglDto> wlglDtos = wlglService.selectSameMater(wlglDto);
		int count = wlglService.getCountForSearchExp(wlglDto,null);
		Map<String,Object> map = new HashMap<>();
		map.put("prewlglDtos", wlglDtos);
		map.put("count", count);
		return map;
	}
	
	/**
	 * 物料新增保存
	 */
	@RequestMapping(value = "/materiel/addSaveMater")
	@ResponseBody
	public Map<String, Object> addSaveMater(WlglDto wlglDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		wlglDto.setLrry(user.getYhid());
		if(wlglDto.getBzqflg()==null) {
			wlglDto.setBzqflg("0");
		}
		boolean isSuccess = wlglService.insertDto(wlglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("ywid", wlglDto.getWlid());
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 物料修改
	 */
	@RequestMapping(value = "/materiel/modMater")
	public ModelAndView modMater(WlglDto wlglDto){
		ModelAndView mav = new ModelAndView("production/materiel/mater_edit");
		WlglDto t_ybglDto = wlglService.getDto(wlglDto);
		if ("submit".equals(wlglDto.getLjbj())){
			t_ybglDto.setFormAction("submit");
		}else if ("audit".equals(wlglDto.getLjbj())){
			t_ybglDto.setFormAction("audit");
		}else {
			t_ybglDto.setFormAction("mod");
		}
		mav.addObject("wlglDto", t_ybglDto);
		
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.MATERIEL_TYPE,BasicDataTypeEnum.MATERIEL_SUBTYPE_COLLECTIVELY,BasicDataTypeEnum.MATERIEL_SAFE_TYPE});
		
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(t_ybglDto.getWllb());
		List<JcsjDto> zlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		mav.addObject("wlaqlblist", jclist.get(BasicDataTypeEnum.MATERIEL_SAFE_TYPE.getCode()));
		mav.addObject("wllblist", jclist.get(BasicDataTypeEnum.MATERIEL_TYPE.getCode()));
		mav.addObject("wlzlblist", zlbList);
		mav.addObject("wlzlbtclist", jclist.get(BasicDataTypeEnum.MATERIEL_SUBTYPE_COLLECTIVELY.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 物料提交
	 */
	@RequestMapping(value = "/materiel/submitMater")
	public ModelAndView submitMater(WlglDto wlglDto){
		wlglDto.setLjbj("submit");
		return this.modMater(wlglDto);
	}
	/**
	 * 物料提交保存
	 */
	@RequestMapping(value = "/materiel/submitSaveMater")
	@ResponseBody
	public Map<String, Object> submitSaveMater(WlglDto wlglDto, HttpServletRequest request) {
		return this.modSaveMater(wlglDto,request);
	}
	/**
	 * 物料审核
	 */
	@RequestMapping(value = "/materiel/auditMater")
	public ModelAndView auditMater(WlglDto wlglDto){
		wlglDto.setLjbj("audit");
		return this.modMater(wlglDto);
	}
	/**
	 * 物料审核保存
	 */
	@RequestMapping(value = "/materiel/auditSaveMater")
	@ResponseBody
	public Map<String, Object> auditSaveMater(WlglDto wlglDto, HttpServletRequest request) {
		return this.modSaveMater(wlglDto,request);
	}
	/**
	 * 物料修改保存
	 */
	@RequestMapping(value = "/materiel/modSaveMater")
	@ResponseBody
	public Map<String, Object> modSaveMater(WlglDto wlglDto, HttpServletRequest request) {
		//获取用户信息
		Map<String, Object> data = new HashMap<>();
		Boolean result = false;
		try{
			//获取用户信息
			User user = getLoginInfo(request);
			wlglDto.setXgry(user.getYhid());
			wlglDto.setXgryzsxm(user.getZsxm());
			if (wlglDto.getBzqflg()==null){
				wlglDto.setBzqflg("0");
			}
			int isSuccess = wlglService.updateWlgl(wlglDto);
			if (isSuccess > 0 && "mod".equals(wlglDto.getFormAction())){
				wlglDto.setSyncFlag("updateWlgl");
				RabbitUtils.sendSyncWlRabbitMsg(JSON.toJSONString(wlglDto),"sys.igams.wlgl.updateWlgl(deleteWlgl,updateKcwh)");
			}
			Map<String,Object> map = new HashMap<>();
			map.put("status", isSuccess==1?"fail":"success"); 
			map.put("message",isSuccess==1?xxglService.getModelById("ICOM00002").getXxnr():isSuccess==2?xxglService.getModelById("ICOM00001").getXxnr():"添加成功，但U8系统不存在此物料");
			map.put("ywid",wlglDto.getWlid());
			return map;
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			String mess = e.getMsg();
			XxglModel xxglModel = xxglService.getModelById(e.getMsgId());
			String idMsg = xxglModel==null?"":xxglModel.getXxnr();
			data.put("status", "fail");
			data.put(MSG, idMsg + (StringUtil.isNotBlank(idMsg) && StringUtil.isNotBlank(mess) ? "<br/>" : "")
					+ (StringUtil.isNotBlank(mess) ? mess : ""));

		}
		data.put(RESULT, result);
		return data;
	
	}
	
	/**
	 * 物料删除
	 */
	@RequestMapping(value = "/materiel/delMater")
	@ResponseBody
	public Map<String, Object> delMater(WlglDto wlglDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		wlglDto.setScry(user.getYhid());
		boolean isSuccess = wlglService.deleteWlgl(wlglDto);
		if (isSuccess){
			wlglDto.setSyncFlag("deleteWlgl");
			RabbitUtils.sendSyncWlRabbitMsg(JSON.toJSONString(wlglDto),"sys.igams.wlgl.updateWlgl(deleteWlgl,updateKcwh)");
		}
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 物料废弃
	 */
	@RequestMapping(value = "/materiel/discardMater")
	@ResponseBody
	public Map<String, Object> discardMater(WlglDto wlglDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		wlglDto.setXgry(user.getYhid());
		boolean isSuccess = wlglService.discard(wlglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 物料申请审核列表页面
	 */
	@RequestMapping(value ="/materiel/pageListMaterAudit")
	public ModelAndView pageListMaterAudit(){
		ModelAndView mav = new ModelAndView("production/materiel/mater_listaudit");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{
				BasicDataTypeEnum.MATERIEL_TYPE});
		mav.addObject("wllblist", jclist.get(BasicDataTypeEnum.MATERIEL_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 物料申请审核列表
	 */
	@RequestMapping(value ="/materiel/pageGetListMaterAudit")
	@ResponseBody
	public Map<String,Object> pageGetListMaterAudit(WlglDto wlglDto,HttpServletRequest request){
		
		//附加委托参数
		DataPermission.addWtParam(wlglDto);
		//附加数据权限控制
		//DataPermission.addDyx(wlglDto, "wj", SsdwTableEnum.WJGL);
		//附加审核状态过滤
		if(GlobalString.AUDIT_SHZT_YSH.equals(wlglDto.getDqshzt())){
			DataPermission.add(wlglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "wl", "wlid", AuditTypeEnum.AUDIT_METRIEL);
		}else{
			DataPermission.add(wlglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "wl", "wlid", AuditTypeEnum.AUDIT_METRIEL);
		}
		DataPermission.addCurrentUser(wlglDto, getLoginInfo(request));
		
		List<WlglDto> t_List = wlglService.getPagedAuditList(wlglDto);
		
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", wlglDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 物料导入页面
	 */
	@RequestMapping(value ="/materiel/pageImportProduction")
	public ModelAndView pageImportProduction(){
		ModelAndView mav = new ModelAndView("production/materiel/mater_import");
		
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_MATER.getCode());
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 物料临时修改提交页面
	 */
	@RequestMapping(value = "/materiel/modsubmitMaterTemp")
	public ModelAndView modMaterTemp(WlgllsbDto wlgllsbDto){
		ModelAndView mav = new ModelAndView("production/materiel/materTemp_edit");
		mav.addObject("urlPrefix", urlPrefix);
		//获得物料管理表Dto
		WlgllsbDto t_wlgllsbDto = new WlgllsbDto();
		if(StringUtil.isNotBlank(wlgllsbDto.getWlid())){
			t_wlgllsbDto = wlglService.selectWllsbDtoByWlid(wlgllsbDto.getWlid());
		}
		if(StringUtil.isBlank(wlgllsbDto.getLsid())){
			//判断正式表数据状态
			if(StatusEnum.CHECK_SUBMIT.getCode().equals(t_wlgllsbDto.getZt()) || StatusEnum.CHECK_MALMOD.getCode().equals(t_wlgllsbDto.getZt())){
				mav.addObject("status", "fail");
				mav.addObject("message", xxglService.getModelById("WCOM_WAL00001").getXxnr());
				mav.addObject("wlglDto", t_wlgllsbDto);
				return mav;
			}else if(StatusEnum.CHECK_NO.getCode().equals(t_wlgllsbDto.getZt())){
				mav.addObject("status", "fail");
				mav.addObject("message", xxglService.getModelById("WCOM_WAL00002").getXxnr());
				mav.addObject("wlglDto", t_wlgllsbDto);
				return mav;
			}else if(StatusEnum.CHECK_UNPASS.getCode().equals(t_wlgllsbDto.getZt())){
				mav.addObject("status", "fail");
				mav.addObject("message", xxglService.getModelById("WCOM_WAL00003").getXxnr());
				mav.addObject("wlglDto", t_wlgllsbDto);
				return mav;
			}
		}
		//查询临时表中相同wlid数据信息
		List<WlgllsbDto> wlgllsbDtoList = wlgllsbService.getDtoListByWlid(wlgllsbDto);
		if(!CollectionUtils.isEmpty(wlgllsbDtoList)){
			if(!StatusEnum.CHECK_PASS.getCode().equals(wlgllsbDtoList.get(0).getZt())){
				//获取时间最近的一条数据
				t_wlgllsbDto = wlgllsbDtoList.get(0);
			}
		}
		if ("audit".equals(wlgllsbDto.getLjbj())){
			t_wlgllsbDto.setFormAction("audit");
		}else {
			t_wlgllsbDto.setFormAction("modsubmit");
		}
		mav.addObject("wlgllsbDto", t_wlgllsbDto);
		//此处查询物料表的数据，用于和传入修改数据进行对比
		WlglDto wlglDto = wlglService.getDtoById(t_wlgllsbDto.getWlid());
		mav.addObject("wlglDto", wlglDto);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.MATERIEL_TYPE, BasicDataTypeEnum.MATERIEL_SUBTYPE_COLLECTIVELY,BasicDataTypeEnum.MATERIEL_SAFE_TYPE});
		JcsjDto tJcsjDto = new JcsjDto();
		tJcsjDto.setFcsid(t_wlgllsbDto.getWllb());
		List<JcsjDto> zlbList = jcsjService.getJcsjDtoList(tJcsjDto);
		CkhwxxDto ckhwxxDto = new CkhwxxDto();
		ckhwxxDto.setWlid(wlgllsbDto.getWlid());
		CkhwxxDto lastCkInfo = ckhwxxService.getLastCkInfo(ckhwxxDto);
		String remindMsg;
		if (lastCkInfo != null){
			remindMsg = "提示：库存量："+lastCkInfo.getKcl() +"      上次出库时间："+(StringUtil.isNotBlank(lastCkInfo.getCkrq())?lastCkInfo.getCkrq():"无出库信息");
		}else {
			remindMsg = "提示：库存量：无入库信息      上次出库时间：无出库信息";
		}
		mav.addObject("remindMsg", remindMsg);
		mav.addObject("wllblist", jclist.get(BasicDataTypeEnum.MATERIEL_TYPE.getCode()));
		mav.addObject("wlzlblist", zlbList);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("wlaqlblist", jclist.get(BasicDataTypeEnum.MATERIEL_SAFE_TYPE.getCode()));
		mav.addObject("wlzlbtclist", jclist.get(BasicDataTypeEnum.MATERIEL_SUBTYPE_COLLECTIVELY.getCode()));
		mav.addObject("modbj","1");
		return mav;
	}
	/**
	 * 物料临时修改审核页面
	 */
	@RequestMapping(value = "/materiel/auditMaterTemp")
	public ModelAndView auditMaterTemp(WlgllsbDto wlgllsbDto){
		wlgllsbDto.setLjbj("audit");
		return this.modMaterTemp(wlgllsbDto);
	}
	/**
	 * 物料临时修改提交保存
	 */
	@RequestMapping(value = "/materiel/modsubmitSaveMaterTemp")
	@ResponseBody
	public Map<String, Object> modsubmitSaveMaterTemp(WlgllsbDto wlgllsbDto,HttpServletRequest request){
		//获取用户信息
		User user = getLoginInfo(request);
		wlgllsbDto.setLrry(user.getYhid());
		if (wlgllsbDto.getBzqflg()==null){
			wlgllsbDto.setBzqflg("0");
		}
		boolean isSuccess = true;
		WlgllsbDto t_wlgllsbDto = wlgllsbService.modSubmitSaveMaterTemp(wlgllsbDto);
		Map<String,Object> map = new HashMap<>();
		if(t_wlgllsbDto == null){
			isSuccess = false;
		}else{
			map.put("ywid", t_wlgllsbDto.getLsid());
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 物料临时修改审核保存
	 */
	@RequestMapping(value = "/materiel/auditSaveMaterTemp")
	@ResponseBody
	public Map<String, Object> auditSaveMaterTemp(WlgllsbDto wlgllsbDto,HttpServletRequest request){
		return this.modsubmitSaveMaterTemp(wlgllsbDto,request);
	}
	/**
	 * 物料修改申请审核列表页面
	 */
	@RequestMapping(value ="/materiel/pageListModMaterAudit")
	public ModelAndView pageListModMaterAudit(){
		ModelAndView mav = new ModelAndView("production/materiel/modmater_listaudit");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{
				BasicDataTypeEnum.MATERIEL_TYPE});
		mav.addObject("wllblist", jclist.get(BasicDataTypeEnum.MATERIEL_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 物料修改申请审核列表
	 */
	@RequestMapping(value ="/materiel/pageGetListModMaterAudit")
	@ResponseBody
	public Map<String,Object> pageGetListModMaterAudit(WlgllsbDto wlgllsbDto,HttpServletRequest request){
		
		//附加委托参数
		DataPermission.addWtParam(wlgllsbDto);
		//附加审核状态过滤
		if(GlobalString.AUDIT_SHZT_YSH.equals(wlgllsbDto.getDqshzt())){
			DataPermission.add(wlgllsbDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "wllsb", "lsid", AuditTypeEnum.AUDIT_METRIELMOD);
		}else{
			DataPermission.add(wlgllsbDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "wllsb", "lsid", AuditTypeEnum.AUDIT_METRIELMOD);
		}
		DataPermission.addCurrentUser(wlgllsbDto, getLoginInfo(request));
		List<WlgllsbDto> t_List = wlgllsbService.getPagedModAuditList(wlgllsbDto);
		
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", wlgllsbDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	/**
	 * 物料临时修改查看
	 */
	@RequestMapping(value="/materiel/viewModTempMater")
	public ModelAndView viewModTempMater(WlgllsbDto wlgllsbDto){
		ModelAndView mav = new ModelAndView("production/materiel/modmater_view");
		WlgllsbDto t_ybglDto = wlgllsbService.getDtoById(wlgllsbDto.getLsid());
		mav.addObject("wlgllsbDto", t_ybglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	@RequestMapping("/getWlglforHwxx")
	@ResponseBody
	public Map<String, Object> getWlglforHwxx(WlglDto wlglDto){
		Map<String, Object> map=new HashMap<>();
		WlglDto wlglDtos=wlglService.getDto(wlglDto);
		if(wlglDtos!=null) {
			map.put("wlglDto", wlglDtos);
			map.put("state","success");
		}else {
			map.put("state","falie");
		}
			return map;
		}
	
	/**
	 * 跳转库存维护界面
	 */
	@RequestMapping(value ="/materiel/stockupholdMater")
	public ModelAndView stockupholdMater(WlglDto wlglDto) {
		ModelAndView mav=new ModelAndView("production/materiel/mater_stockuphold");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[] {BasicDataTypeEnum.PRODUCTION_TYPE,BasicDataTypeEnum.MATERIELQUALITY_TYPE});
		mav.addObject("lblist",jclist.get(BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode()));
		WlglDto t_ybglDto = wlglService.getDto(wlglDto);
		t_ybglDto.setFormAction("stockuphold");
		// 将sscplb由字符串转换为数组存入sscplbs中
		String sscplb = t_ybglDto.getSscplb();
		if(StringUtil.isNotBlank(sscplb)) {
			t_ybglDto.setSscplbs(sscplb.trim().split(","));
			// 设置一个选择标记，若sscplbs中有与jcsj中的MATERIELQUALITY_TYPE中的一样的，则设置标记为1，即选中
			for(int i=0;i<t_ybglDto.getSscplbs().length;i++) {
				if(jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode())!=null) {
					for(int j=0;j<jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).size();j++) {
						if(jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).get(j).getCsid().equals(t_ybglDto.getSscplbs()[i]))
							jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).get(j).setChecked("1");
					}
				}
			}
		}		

		mav.addObject("sscplblist",jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()));
		mav.addObject("wlglDto", t_ybglDto);
//		JcsjDto JcsjDto = new JcsjDto();
//		JcsjDto.setJclb(BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode());
//		List<JcsjDto> lbList = jcsjService.getJcsjDtoList(JcsjDto);
//		mav.addObject("lblist", lbList);
		mav.addObject("urlPrefix", urlPrefix);

		return mav;
	}
	
	/**
	 * 库存维护保存
	 */
	@RequestMapping(value ="/materiel/stockupholdSaveMater")
	@ResponseBody
	public Map<String, Object> stockupholdSaveMater(WlglDto wlglDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		wlglDto.setXgry(user.getYhid());
		wlglDto.setXgryzsxm(user.getZsxm());
		boolean isSuccess=wlglService.updateSafeStock(wlglDto);
		if (isSuccess){
			wlglDto.setSyncFlag("updateKcwh");
			RabbitUtils.sendSyncWlRabbitMsg(JSON.toJSONString(wlglDto),"sys.igams.wlgl.updateWlgl(deleteWlgl,updateKcwh)");
		}
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 跳转部门设置界面
	 */
	@RequestMapping(value ="/materiel/depsettingMater")
	public ModelAndView depsettingMater(WlglDto wlglDto,HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("production/materiel/mater_depsetting");
		WlglDto t_ybglDto = wlglService.getDto(wlglDto);
		BmwlDto bmwlDto = new BmwlDto();
		bmwlDto.setWlid(wlglDto.getWlid());
		bmwlDto.setBm(getLoginInfo(request).getJgid());
		BmwlDto dto = bmwlService.getDto(bmwlDto);
		t_ybglDto.setFormAction("depsetting");
		mav.addObject("wlglDto", t_ybglDto);
		mav.addObject("sfsz", dto==null?"0":"1");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 同步物料
	 * @param wlglDto
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/data/syncWlgl")
	@ResponseBody
	public Map<String, Object> syncWlgl(WlglDto wlglDto,HttpServletRequest request){
		WlglDto t_wlglDto = wlglService.getDto(wlglDto);
		Map<String,Object> map = new HashMap<>();
		try{
			RabbitUtils.sendSyncWlRabbitMsg(JSON.toJSONString(t_wlglDto),"sys.igams.wlgl.insertWlgl");
			map.put("status", "success");
			map.put("message", "同步成功");
		}catch (Exception e){
			map.put("status", "fail");
			map.put("message", e.getMessage());
		}
		return map;
	}
	/**
	 * 部门设置保存
	 */
	@RequestMapping(value ="/materiel/depsettingSaveMater")
	@ResponseBody
	public Map<String, Object> depsettingSaveMater(WlglDto wlglDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user = getLoginInfo(request);
		BmwlDto bmwlDto = new BmwlDto();
		bmwlDto.setBm(user.getJgid());
		bmwlDto.setWlid(wlglDto.getWlid());
		wlglDto.setXgry(user.getYhid());
		wlglDto.setXgryzsxm(user.getZsxm());
		boolean Success;
		try {
			Success=wlglService.depsettingSaveMater(wlglDto,bmwlDto);
			map.put("status", Success ? "success" : "fail");
			map.put("message", Success ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 货期修订页面
	 */
	@RequestMapping(value="/materiel/revisehqRevise")
	public ModelAndView revisehqRevise(WlglDto wlglDto){
		ModelAndView mav=new ModelAndView("production/materiel/revise_hq");
		mav.addObject("urlPrefix", urlPrefix);
		WlglDto wlglDtos=wlglService.getDtoById(wlglDto.getWlid());
		mav.addObject("wlglDto", wlglDtos!=null?wlglDtos:wlglDto);
		return mav;
	}
	
	/**
	 * 保存修订货期
	 */
	@RequestMapping(value="/materiel/revisehqSaveRevise")
	@ResponseBody
	public Map<String, Object> revisehqSaveRevise(WlglDto wlglDto,HttpServletRequest request){
		Map<String, Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		wlglDto.setXgry(user.getYhid());
		boolean iSsuccess=wlglService.update(wlglDto);
		map.put("status", iSsuccess?"success":"fail");
		map.put("message", iSsuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 添加物料至采购车
	 */
	@RequestMapping("/materiel/pagedataAddToShoppingCar")
	@ResponseBody
	public Map<String,Object> pagedataAddToShoppingCar(QgcglDto qgcglDto,HttpServletRequest request){
		User user = getLoginInfo(request); 
		qgcglDto.setRyid(user.getYhid());
		Map<String,Object> map=new HashMap<>();
		boolean isSuccess=qgcglService.insert(qgcglDto);
		//获取采购车已加入的物料数量
		List<QgcglDto> qglist=qgcglService.getQgcList(qgcglDto);
		StringBuilder ids= new StringBuilder();
		if(!CollectionUtils.isEmpty(qglist)) {
			for (QgcglDto dto : qglist) {
				ids.append(",").append(dto.getWlid());
			}
			ids = new StringBuilder(ids.substring(1));
		}
		map.put("qg_ids", ids.toString());
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 从采购车删除该物料
	 */
	@RequestMapping("/materiel/pagedataDelToShoppingCar")
	@ResponseBody
	public Map<String,Object> pagedataDelToShoppingCar(QgcglDto qgcglDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user = getLoginInfo(request); 
		qgcglDto.setRyid(user.getYhid());
		boolean isSuccess=qgcglService.delete(qgcglDto);
		//获取采购车已加入的物料数量
		List<QgcglDto> qglist=qgcglService.getQgcList(qgcglDto);
		StringBuilder ids= new StringBuilder();
		if(!CollectionUtils.isEmpty(qglist)) {
			for (QgcglDto dto : qglist) {
				ids.append(",").append(dto.getWlid());
			}
			ids = new StringBuilder(ids.substring(1));
		}
		map.put("qg_ids", ids.toString());
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 物料采购页面
	 */
	@RequestMapping("/materiel/shoppingMater")
	public ModelAndView getShoppingMaterPage(QgglDto qgglDto,HttpServletRequest request) {
		User user = getLoginInfo(request);
		qgglDto.setSqr(user.getYhid());//默认申请人
		QgglDto qgglDto_t=qgglService.getMrsqrxxByYhid(qgglDto);//获取默认申请人信息
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setCsdm("MATERIAL");
		jcsjDto.setJclb(BasicDataTypeEnum.PURCHASE_SUB_TYPE.getCode());
		jcsjDto=jcsjService.getDto(jcsjDto);
		qgglDto_t.setQglb(jcsjDto.getCsid());
		qgglDto_t.setQglbdm(jcsjDto.getCsdm());
		qgglDto_t.setQglbmc(jcsjDto.getCsmc());
		if(StringUtil.isBlank(qgglDto_t.getSqrq())) {
			SimpleDateFormat sdf=new SimpleDateFormat("yyy-MM-dd");
			Date date=new Date();
			qgglDto_t.setSqrq(sdf.format(date));
		}
		qgglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		ModelAndView mav=new ModelAndView("production/materiel/mater_shopping");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_ITEMENCODING,BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING});
		mav.addObject("xmbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_ITEMENCODING.getCode()));
		mav.addObject("xmdlbmlist", jclist.get(BasicDataTypeEnum.PURCHASE_CLASS_ITEMENCODING.getCode()));
		qgglDto_t.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		JcsjDto qglb = jcsjService.getDtoById(qgglDto_t.getQglb());
		qgglDto_t.setCskz1(qglb.getCskz1());
		// 自动生成单据号和记录编号
		if(StringUtil.isNotBlank(qgglDto_t.getJgdh())) {
			String djh = qgglService.generateDjh(qgglDto_t);
			qgglDto_t.setDjh(djh);
			qgglDto_t.setJlbh(djh);
		}
		mav.addObject("qgglDto", qgglDto_t);
		mav.addObject("formAction", "/production/purchase/pagedataAddSavePurchase");
		mav.addObject("url", "/production/materiel/pagedataShoppinglist");
		mav.addObject("flag", "qgc");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 采购车页面已采购物料列表
	 */
	@RequestMapping("/materiel/pagedataShoppinglist")
	@ResponseBody
	public Map<String,Object> getshoppinglist(QgcglDto qgcglDto,HttpServletRequest request){
		User user = getLoginInfo(request); 
		qgcglDto.setRyid(user.getYhid());
		Map<String,Object> map=new HashMap<>();
		List<QgcglDto> list=new ArrayList<>();
		if("MATERIAL".equals(qgcglDto.getQglbdm()) || "DEVICE".equals(qgcglDto.getQglbdm())|| "OUTSOURCE".equals(qgcglDto.getQglbdm())) {
			list=qgcglService.getQgcDtoList(qgcglDto);
		}
		map.put("total", qgcglDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}
	
	/**
	 * 
	 * 购物车界面添加新物料
	 */
	@RequestMapping("/materiel/pagedataAddMaterOnShopping")
	@ResponseBody
	public Map<String,Object> addMaterOnShopping(QgcglDto qgcglDto){
		WlglDto wlglDto=new WlglDto();
		wlglDto.setWlid(qgcglDto.getWlid());
		WlglDto wlglDto_t=wlglService.getDto(wlglDto);
		wlglDto_t.setWlzt(wlglDto_t.getZt());
		wlglDto_t.setZt(StatusEnum.CHECK_NO.getCode());
		wlglDto_t.setQxqg("0");
		wlglDto_t.setBz(null);
		Map<String,Object> map=new HashMap<>();
		//若为停用状态的物料则返回空
		if("2".equals(wlglDto_t.getScbj())){
			wlglDto_t=new WlglDto();
		}
		map.put("qgcglDto", wlglDto_t);
		return map;
	}
	
	/**
	 * 
	 * 购物车界面通过粘贴批量添加新物料
	 */
	@RequestMapping("/materiel/pagedataAddMatersOnShopping")
	@ResponseBody
	public Map<String,Object> addMatersOnShopping(QgcglDto qgcglDto){
		WlglDto wlglDto=new WlglDto();
		wlglDto.setIds(qgcglDto.getWlbms());
		List<WlglDto> wlglDtos_t=wlglService.getDtosByIds(wlglDto);
		if(!CollectionUtils.isEmpty(wlglDtos_t)) {
			for (WlglDto dto : wlglDtos_t) {
				dto.setWlzt(dto.getZt());
				dto.setZt(StatusEnum.CHECK_NO.getCode());
				dto.setQxqg("0");
				dto.setBz(null);
			}
		}
		Map<String,Object> map=new HashMap<>();
		map.put("qgcglDtos", wlglDtos_t);
		return map;
	}
	/**
	 * 选择请购类型页面
	 */
	@RequestMapping("/purchase/chancePurchaseTypePage")
	public ModelAndView chancePurchaseTypePage(QgglDto qgglDto) {
		ModelAndView mav = new ModelAndView("purchase/purchase/purchase_chance");
		Map<String, List<JcsjDto>> jclist = jcsjService
				.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.PURCHASE_SUB_TYPE});
		List<JcsjDto> jcsjList = jclist.get(BasicDataTypeEnum.PURCHASE_SUB_TYPE.getCode());
		List<JcsjDto> jcsjs = new ArrayList<>();
		if ("1".equals(qgglDto.getWlflag())) {
			for (JcsjDto jcsjDto : jcsjList) {
				if ("MATERIAL".equals(jcsjDto.getCsdm()) || "DEVICE".equals(jcsjDto.getCsdm())||"OUTSOURCE".equals(jcsjDto.getCsdm()) ) {
					jcsjs.add(jcsjDto);
				}
			}
		}
		mav.addObject("qglbList", "1".equals(qgglDto.getWlflag()) ? jcsjs : jcsjList);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("qglx", StringUtil.isNotBlank(qgglDto.getQglx()) ? qgglDto.getQglx() : "");
		mav.addObject("wlflag", qgglDto.getWlflag());
		mav.addObject("ljbj", qgglDto.getLjbj());
		return mav;
	}
	/**
	 * 选择物料采购车跳转请购选择
	 */
	@RequestMapping("/purchase/shoppingMater")
	public ModelAndView shoppingMater(QgglDto qgglDto) {
		qgglDto.setLjbj("shoppingSaveMater");
		return this.chancePurchaseTypePage(qgglDto);
	}
	/**
	 * 选择请购新增跳转请购选择
	 */
	@RequestMapping("/purchase/addPurchase")
	public ModelAndView addPurchase(QgglDto qgglDto) {
		return this.chancePurchaseTypePage(qgglDto);
	}
	/**
	 * 选择OA请购新增跳转请购选择
	 */
	@RequestMapping("/purchase/systemPurchase")
	public ModelAndView systemPurchase(QgglDto qgglDto) {
		qgglDto.setLjbj("systemSavePurchase");
		return this.chancePurchaseTypePage(qgglDto);
	}
	/**
	 * 保存OA采购单
	 */
	@RequestMapping("/purchase/systemSavePurchase")
	@ResponseBody
	public Map<String,Object> systemSavePurchase(QgglDto qgglDto,HttpServletRequest request) throws BusinessException{
		return this.addSaveProdution(qgglDto,request);
	}

	/**
	 * 保存采购单
	 */
	@RequestMapping("/purchase/pagedataAddSavePurchase")
	@ResponseBody
	public Map<String,Object> addSaveProdution(QgglDto qgglDto,HttpServletRequest request) throws BusinessException{
		User user = getLoginInfo(request); 
		qgglDto.setLrry(user.getYhid());
		qgglDto.setRkzt("01");
		Map<String,Object> map=new HashMap<>();
		QgglDto djhsfcf=qgglService.getDtoByDjh(qgglDto);
		if(djhsfcf!=null) {
			map.put("status", "fail");
			map.put("message", "采购单号不允许重复!");
			return map;
		}
		String qgmxs = JSON.toJSON(qgglDto.getQgmx_json()).toString();
		List<QgmxDto> qgmxlist= JSON.parseArray(qgmxs, QgmxDto.class);
		if (CollectionUtils.isEmpty(qgmxlist)){
			map.put("status", "fail");
			map.put("message", "请购明细不允许为空!");
			return map;
		}
		//处理压缩文件，分配图纸到对应的请购明细
		qgglDto.setQgmxlist(qgmxlist);
		List<String> errorwlbm=new ArrayList<>();
		StringBuilder t_wlfile=new StringBuilder();
		for (QgmxDto qgmxDto : qgmxlist) {
			qgmxDto.setQgmxid(StringUtil.generateUUID());
			if (!CollectionUtils.isEmpty(qgmxDto.getFjids())) {
				if (StringUtils.isBlank(qgmxDto.getWlzlbtc()) && "1".equals(qgmxDto.getCskz2())) {
					errorwlbm.add(qgmxDto.getWlbm());
				}
			}
			if ("1".equals(qgmxDto.getCskz1()) && CollectionUtils.isEmpty(qgmxDto.getFjids())
			) {
				t_wlfile.append(qgmxDto.getWlbm());
			}
		}
		if(t_wlfile.length()>0) {
			map.put("status", "fail");
			map.put("message", "物料编码为"+ t_wlfile +"的物料需上传图纸!");
			return map;
		}
		if(!CollectionUtils.isEmpty(errorwlbm) && !"1".equals(qgglDto.getBcbj())) {
			StringBuilder str= new StringBuilder();
			for (String s : errorwlbm) {
				str.append(",").append(s);
			}
			str = new StringBuilder(str.substring(1));
			map.put("status", "fail");
			map.put("message", "物料编码为"+ str +"的物料子类别统称不能为空，请先完善信息，可先保存请购单信息!");
			return map;
		}
		String str = JSON.toJSONString(qgmxlist);
		qgglDto.setQgmx_json(str);
		boolean isSuccess=qgglService.addSaveProdution(qgglDto);
		if("1".equals(systemconfigflg) && isSuccess) {
			qgglDto.setPrefixFlg(prefixFlg);
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qggl.insert",JSONObject.toJSONString(qgglDto));
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		map.put("auditType",AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		map.put("ywid", qgglDto.getQgid());
		return map;
	}
	/**
	 * 采购车保存
	 */
	@RequestMapping("/purchase/shoppingSaveMater")
	@ResponseBody
	public Map<String,Object> shoppingSaveMater(QgglDto qgglDto,HttpServletRequest request) throws BusinessException{
		return this.addSaveProdution(qgglDto,request);
	}
	/**
	 * 根据输入信息查询物料
	 */
	@RequestMapping("/purchase/pagedataSelectWl")
	@ResponseBody
	public Map<String,Object> pagedataSelectWl(WlglDto wlglDto){
		List<WlglDto> f_wlglDtos = wlglService.selectWl(wlglDto);
		Map<String,Object> map = new HashMap<>();
		map.put("f_wlglDtos", f_wlglDtos);
		return map;
	}
	
	/**
	 * 请购明细附件上传页面
	 */
	@RequestMapping("/purchase/pagedataGetUploadFile")
	public ModelAndView getUploadFilePage(QgglDto qgglDto,HttpServletRequest request) {
		String requestName=request.getParameter("requestName");
		ModelAndView mav=new ModelAndView("production/materiel/mater_uploadShoppingFile");
		// 查询临时文件并显示
		List<String> fjids = qgglDto.getFjids();
		List<FjcfbDto> redisList = fjcfbService.getRedisList(fjids);
		mav.addObject("redisList", redisList);
		if(!"copy".equals(requestName)) {
			// 查询正式文件并显示
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(qgglDto.getQgid());
			fjcfbDto.setZywid(qgglDto.getQgmxid());
			List<FjcfbDto> fjcfbDtos = fjcfbService.getListByZywid(fjcfbDto);
			mav.addObject("fjcfbDtos", fjcfbDtos);
			qgglDto.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
			mav.addObject("qgglDto", qgglDto);
		}
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 请购明细图纸批量上传页面
	 */
	@RequestMapping("/purchase/pagedataGetBatchUploadFilePage")
	public ModelAndView getBatchUploadFilePage(QgglDto qgglDto) {
		ModelAndView mav=new ModelAndView("production/materiel/mater_batchUploadFile");
		// 查询临时文件并显示
		List<String> fjids = qgglDto.getYsfjids();
		List<FjcfbDto> redisList = fjcfbService.getRedisList(fjids);
		mav.addObject("redisList", redisList);
		// 查询正式文件并显示
//		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(qgglDto.getQgid());
//		mav.addObject("fjcfbDtos", fjcfbDtos);
		qgglDto.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
		mav.addObject("qgglDto", qgglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 单条物料点击查看接口
	 */
	@RequestMapping("/materiel/queryByWlid")
	@ResponseBody
	public Map<String,Object> queryByWlid(WlglDto wlglDto){
		Map<String,Object> map=new HashMap<>();
		WlglDto wlglDto_t=wlglService.queryByWlid(wlglDto);
		map.put("wlglDto",wlglDto_t);
		return map;
	}
	/**
	 * 钉钉物料查看
	 */
	@RequestMapping(value="/materiel/minidataViewMater")
	@ResponseBody
	public Map<String, Object> minidataViewMater(WlglDto wlglDto){
		Map<String, Object> map = new HashMap<>();
		WlglDto t_ybglDto = wlglService.getDtoById(wlglDto.getWlid());
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[] {BasicDataTypeEnum.PRODUCTION_TYPE});

		if(t_ybglDto!=null){
			// 将sscplb由字符串转换为数组存入sscplbs中
			String sscplb = t_ybglDto.getSscplb();
			if(StringUtil.isNotBlank(sscplb)) {
				t_ybglDto.setSscplbs(sscplb.trim().split(","));
				// 设置一个选择标记，若sscplbs中有与jcsj中的MATERIELQUALITY_TYPE中的一样的，则设置标记为1，即选中
				for(int i=0;i<t_ybglDto.getSscplbs().length;i++) {
					if(jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode())!=null) {
						for(int j=0;j<jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).size();j++) {
							if(jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).get(j).getCsid().equals(t_ybglDto.getSscplbs()[i]))
								jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()).get(j).setChecked("1");
						}
					}
				}
			}
			map.put("sscplblist",jclist.get(BasicDataTypeEnum.PRODUCTION_TYPE.getCode()));
			map.put("wlglDto", t_ybglDto);
		}else {
			map.put("wlglDto", new WlglDto());
		}
		//从物料临时表查询审核历史
		WlgllsbDto wlgllsbDto = new WlgllsbDto();
		wlgllsbDto.setWlid(wlglDto.getWlid());
		List<WlgllsbDto> wllsbDtos = wlgllsbService.getDtoListByWlid(wlgllsbDto);
		map.put("wllsbDtos", wllsbDtos);
		map.put("urlPrefix", urlPrefix);
		return map;
	}
}


