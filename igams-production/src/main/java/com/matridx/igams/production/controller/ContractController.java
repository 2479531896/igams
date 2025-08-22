
package com.matridx.igams.production.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.DdAuditTypeEnum;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXtshService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.FpmxDto;
import com.matridx.igams.production.dao.entities.HtfkmxDto;
import com.matridx.igams.production.dao.entities.HtfkqkDto;
import com.matridx.igams.production.dao.entities.HtfktxDto;
import com.matridx.igams.production.dao.entities.HtfpqkDto;
import com.matridx.igams.production.dao.entities.HtglDto;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.ShysDto;
import com.matridx.igams.production.service.svcinterface.IFpmxService;
import com.matridx.igams.production.service.svcinterface.IHtfkmxService;
import com.matridx.igams.production.service.svcinterface.IHtfkqkService;
import com.matridx.igams.production.service.svcinterface.IHtfktxService;
import com.matridx.igams.production.service.svcinterface.IHtfpqkService;
import com.matridx.igams.production.service.svcinterface.IHtglService;
import com.matridx.igams.production.service.svcinterface.IHtmxService;
import com.matridx.igams.production.service.svcinterface.IShysService;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/contract")
public class ContractController extends BaseController {

	@Autowired
	private IHtglService htglService;
	@Autowired
	IHtmxService htmxService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IHtfkqkService htfkqkService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IHtfpqkService htfpqkService;
	@Autowired
	IHwxxService hwxxService;
	@Autowired
	IHtfkmxService htfkmxService;
	@Autowired
	IHtfktxService htfktxService;
	@Autowired
	IFpmxService fpmxService;
	@Autowired
	private RedisUtil redisUtil;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	private IDdxxglService ddxxglService;
	@Autowired
	private ICommonService commonService;

	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	@Value("${matridx.rabbit.flg:}")
	private String rabbitFlg;

	@Autowired(required=false)
	private AmqpTemplate amqpTempl;
	//是否发送rabbit标记     1：发送
	@Value("${matridx.rabbit.systemconfigflg:}")
	private String systemconfigflg;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	@Autowired
	private IXtszService xtszService;
	@Autowired
	private IShysService shysService;
	@Autowired
	private IXtshService xtshService;
	/**
	 * 合同列表页面
	 */
	@RequestMapping(value = "/contract/pageListContract")
	public ModelAndView pageListContract(HtglDto htglDto) {
		ModelAndView mav = new ModelAndView("contract/contract/contract_list");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.PAYMENT_METHOD, BasicDataTypeEnum.INVOICE_METHOD ,BasicDataTypeEnum.CONTRACT_RISK,BasicDataTypeEnum.CONTRACT_TYPE});
		mav.addObject("fkfslist", jclist.get(BasicDataTypeEnum.PAYMENT_METHOD.getCode()));
		mav.addObject("fpfslist", jclist.get(BasicDataTypeEnum.INVOICE_METHOD.getCode()));
		mav.addObject("risklist",jclist.get(BasicDataTypeEnum.CONTRACT_RISK.getCode()));
		mav.addObject("contractlist", jclist.get(BasicDataTypeEnum.CONTRACT_TYPE.getCode()));// 合同类型
		htglDto.setAuditType(AuditTypeEnum.AUDIT_CONTRACT.getCode());
		mav.addObject("htglDto", htglDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("htlx", htglDto.getHtlx());
		Map<String,Object> map = htglService.queryOverTime("");
		String overString = "0个";
		String overdueString = "0个";
		String postEnd = "";
		String overTime = "";
		String overdueTime = "";
		if(!CollectionUtils.isEmpty(map)){
			List<HtglDto> overList = (List<HtglDto>) map.get("overList");
			List<HtglDto> overdueList = (List<HtglDto>) map.get("overdueList");
			if(!CollectionUtils.isEmpty(overList)){
				overString = overList.size()+"个";
			}
			if(!CollectionUtils.isEmpty(overdueList)){
				overdueString = overdueList.size()+"个";
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
		mav.addObject("postEnd",postEnd);
		mav.addObject("overTime",overTime);
		mav.addObject("overdueTime",overdueTime);
		return mav;
	}

	/**
	 * 合同列表
	 */
	@RequestMapping(value = "/contract/pageGetListContract")
	@ResponseBody
	public Map<String, Object> pageGetListContract(HtglDto htglDto) {
		if (StringUtil.isBlank(htglDto.getHtlx())){
			//不查询框架合同
			htglDto.setNhtlx("3");
		}
		List<HtglDto> t_List = htglService.getPagedDtoList(htglDto);
		List<JcsjDto> fkfslist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PAYMENT_METHOD.getCode());
		List<JcsjDto> fpfslist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.INVOICE_METHOD.getCode());
		List<JcsjDto> cglxlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PURCHASE_TYPE.getCode());
		Map<String, Object> map = new HashMap<>();
		map.put("fkfslist",fkfslist);
		map.put("fpfslist",fpfslist);
		map.put("cglxlist",cglxlist);
		map.put("total", htglDto.getTotalNumber());
		map.put("rows", t_List);
		return map;
	}
	/**
	 * 合同列表钉钉
	 */
	@RequestMapping(value = "/contract/minidataListContract")
	@ResponseBody
	public Map<String, Object> minidataListContract(HtglDto htglDto) {
		return pageGetListContract(htglDto);
	}

	/**
	 * 合同查看
	 */
	@RequestMapping(value = "/contract/viewContract")
	public ModelAndView viewContract(HtglDto htglDto) {
		ModelAndView mav = new ModelAndView("contract/contract/contract_view");
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_UPLOADCONTRACT.getCode());
		fjcfbDto.setYwid(htglDto.getHtid());
		List<FjcfbDto> h_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("h_fjcfbDtos",h_fjcfbDtos);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBCONTRACT.getCode());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
		HtglDto t_htglDto = htglService.getDtoById(htglDto.getHtid());
		FpmxDto fpmxDto=new FpmxDto();
		fpmxDto.setHtid(htglDto.getHtid());
		List<FpmxDto> dtoList = fpmxService.getListForHw(fpmxDto);
		List<FpmxDto> mxlist=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			FpmxDto fpmxDto_t=dtoList.get(0);
			List<FpmxDto> u8ids=new ArrayList<>();
			for(int i=0;i<dtoList.size();i++){
				if(fpmxDto_t.getFpmxid().equals(dtoList.get(i).getFpmxid())){
					FpmxDto fpmxDto1=new FpmxDto();
					fpmxDto1.setRkid(dtoList.get(i).getRkid());
					fpmxDto1.setDhid(dtoList.get(i).getDhid());
					fpmxDto1.setU8rkdh(dtoList.get(i).getU8rkdh());
					fpmxDto1.setLbcskz1(dtoList.get(i).getLbcskz1());
					u8ids.add(fpmxDto1);
                }else{
					fpmxDto_t.setFpmxDtos(u8ids);
					mxlist.add(fpmxDto_t);
					fpmxDto_t=dtoList.get(i);
					u8ids=new ArrayList<>();
					FpmxDto fpmxDto1=new FpmxDto();
					fpmxDto1.setRkid(dtoList.get(i).getRkid());
					fpmxDto1.setDhid(dtoList.get(i).getDhid());
					fpmxDto1.setU8rkdh(dtoList.get(i).getU8rkdh());
					fpmxDto1.setLbcskz1(dtoList.get(i).getLbcskz1());
					u8ids.add(fpmxDto1);
                }
                if(i==dtoList.size()-1){
                    fpmxDto_t.setFpmxDtos(u8ids);
                    mxlist.add(fpmxDto_t);
                }
            }
		}
		mxlist= mxlist.stream().sorted(Comparator.comparing(FpmxDto::getFph))
				.collect(Collectors.toList());
		BigDecimal bigDecimal=new BigDecimal(0);
		if(!CollectionUtils.isEmpty(mxlist)){
			for(FpmxDto dto:mxlist){
				bigDecimal=bigDecimal.add(new BigDecimal(dto.getHjje()));
			}
		}
		List<FpmxDto> rklist = fpmxService.getRkListByFp(fpmxDto);
		mav.addObject("zje",bigDecimal);
		mav.addObject("rklist", rklist);
		mav.addObject("mxlist", mxlist);
		mav.addObject("htglDto", t_htglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 合同查看
	 */
	@RequestMapping(value = "/contract/pagedataViewContract")
	public ModelAndView pagedataViewContract(HtglDto htglDto) {
		ModelAndView mav = new ModelAndView("contract/contract/contract_view");
		//查询附件
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_UPLOADCONTRACT.getCode());
		fjcfbDto.setYwid(htglDto.getHtid());
		List<FjcfbDto> h_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("h_fjcfbDtos",h_fjcfbDtos);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBCONTRACT.getCode());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("t_fjcfbDtos",t_fjcfbDtos);
		HtglDto t_htglDto = htglService.getDtoById(htglDto.getHtid());
		FpmxDto fpmxDto=new FpmxDto();
		fpmxDto.setHtid(htglDto.getHtid());
		List<FpmxDto> dtoList = fpmxService.getListForHw(fpmxDto);
		List<FpmxDto> mxlist=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			FpmxDto fpmxDto_t=dtoList.get(0);
			List<FpmxDto> u8ids=new ArrayList<>();
			for(int i=0;i<dtoList.size();i++){
				if(fpmxDto_t.getFpmxid().equals(dtoList.get(i).getFpmxid())){
					FpmxDto fpmxDto1=new FpmxDto();
					fpmxDto1.setRkid(dtoList.get(i).getRkid());
					fpmxDto1.setDhid(dtoList.get(i).getDhid());
					fpmxDto1.setU8rkdh(dtoList.get(i).getU8rkdh());
					fpmxDto1.setLbcskz1(dtoList.get(i).getLbcskz1());
					u8ids.add(fpmxDto1);
                }else{
					fpmxDto_t.setFpmxDtos(u8ids);
					mxlist.add(fpmxDto_t);
					fpmxDto_t=dtoList.get(i);
					u8ids=new ArrayList<>();
					FpmxDto fpmxDto1=new FpmxDto();
					fpmxDto1.setRkid(dtoList.get(i).getRkid());
					fpmxDto1.setDhid(dtoList.get(i).getDhid());
					fpmxDto1.setU8rkdh(dtoList.get(i).getU8rkdh());
					fpmxDto1.setLbcskz1(dtoList.get(i).getLbcskz1());
					u8ids.add(fpmxDto1);
                }
                if(i==dtoList.size()-1){
                    fpmxDto_t.setFpmxDtos(u8ids);
                    mxlist.add(fpmxDto_t);
                }
            }
		}
		mxlist= mxlist.stream().sorted(Comparator.comparing(FpmxDto::getFph))
				.collect(Collectors.toList());
		BigDecimal bigDecimal=new BigDecimal(0);
		if(!CollectionUtils.isEmpty(mxlist)){
			for(FpmxDto dto:mxlist){
				bigDecimal=bigDecimal.add(new BigDecimal(dto.getHjje()));
			}
		}
		List<FpmxDto> rklist = fpmxService.getRkListByFp(fpmxDto);
		mav.addObject("zje",bigDecimal);
		mav.addObject("rklist", rklist);
		mav.addObject("mxlist", mxlist);
		mav.addObject("htglDto", t_htglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 合同查看发票和入库信息
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/pagedataViewFpOrRk")
	public Map<String, Object> pagedataViewFpAndRk(HtglDto htglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
			FpmxDto fpmxDto = new FpmxDto();
			fpmxDto.setHtid(htglDto.getHtid());
			List<FpmxDto> dtoList = fpmxService.getListForHw(fpmxDto);
			List<FpmxDto> mxlist = new ArrayList<>();
			if(!CollectionUtils.isEmpty(dtoList)){
				FpmxDto fpmxDto_t = dtoList.get(0);
				List<FpmxDto> u8ids = new ArrayList<>();
				for (int i = 0; i < dtoList.size(); i++) {
					if (fpmxDto_t.getFpmxid().equals(dtoList.get(i).getFpmxid())) {
						FpmxDto fpmxDto1 = new FpmxDto();
						fpmxDto1.setRkid(dtoList.get(i).getRkid());
						fpmxDto1.setDhid(dtoList.get(i).getDhid());
						fpmxDto1.setU8rkdh(dtoList.get(i).getU8rkdh());
						fpmxDto1.setLbcskz1(dtoList.get(i).getLbcskz1());
						u8ids.add(fpmxDto1);
                    } else {
						fpmxDto_t.setFpmxDtos(u8ids);
						mxlist.add(fpmxDto_t);
						fpmxDto_t = dtoList.get(i);
						u8ids = new ArrayList<>();
						FpmxDto fpmxDto1 = new FpmxDto();
						fpmxDto1.setRkid(dtoList.get(i).getRkid());
						fpmxDto1.setDhid(dtoList.get(i).getDhid());
						fpmxDto1.setU8rkdh(dtoList.get(i).getU8rkdh());
						fpmxDto1.setLbcskz1(dtoList.get(i).getLbcskz1());
						u8ids.add(fpmxDto1);
                    }
                    if (i == dtoList.size() - 1) {
                        fpmxDto_t.setFpmxDtos(u8ids);
                        mxlist.add(fpmxDto_t);
                    }
                }
			}
			mxlist = mxlist.stream().sorted(Comparator.comparing(FpmxDto::getFph))
					.collect(Collectors.toList());
			BigDecimal bigDecimal = new BigDecimal(0);
			if (!CollectionUtils.isEmpty(mxlist)) {
				for (FpmxDto dto : mxlist) {
					bigDecimal = bigDecimal.add(new BigDecimal(dto.getHjje()));
				}
			}
		if ("fp".equals(request.getParameter("flag"))){
			map.put("rows",mxlist);
			return map;
		}else {
			List<FpmxDto> rklist = fpmxService.getRkListByFp(fpmxDto);
			map.put("rows",rklist);
			return map;
		}
	}
	/**
	 * 请购物料列表查看发票和入库信息
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/pagedataViewFpOrRkByHtmxid")
	public Map<String, Object> pagedataViewFpOrRkByHtmxid(HtmxDto htmxDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isBlank(htmxDto.getHtmxid())){
			map.put("rows",null);
			return map;
		}
		FpmxDto fpmxDto = new FpmxDto();
		fpmxDto.setHtmxid(htmxDto.getHtmxid());
		List<FpmxDto> dtoList = fpmxService.getListForHw(fpmxDto);
		List<FpmxDto> mxlist = new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			FpmxDto fpmxDto_t = dtoList.get(0);
			List<FpmxDto> u8ids = new ArrayList<>();
			for (int i = 0; i < dtoList.size(); i++) {
				if (fpmxDto_t.getFpmxid().equals(dtoList.get(i).getFpmxid())) {
					FpmxDto fpmxDto1 = new FpmxDto();
					fpmxDto1.setRkid(dtoList.get(i).getRkid());
					fpmxDto1.setDhid(dtoList.get(i).getDhid());
					fpmxDto1.setU8rkdh(dtoList.get(i).getU8rkdh());
					fpmxDto1.setLbcskz1(dtoList.get(i).getLbcskz1());
					u8ids.add(fpmxDto1);
                } else {
					fpmxDto_t.setFpmxDtos(u8ids);
					mxlist.add(fpmxDto_t);
					fpmxDto_t = dtoList.get(i);
					u8ids = new ArrayList<>();
					FpmxDto fpmxDto1 = new FpmxDto();
					fpmxDto1.setRkid(dtoList.get(i).getRkid());
					fpmxDto1.setDhid(dtoList.get(i).getDhid());
					fpmxDto1.setU8rkdh(dtoList.get(i).getU8rkdh());
					fpmxDto1.setLbcskz1(dtoList.get(i).getLbcskz1());
					u8ids.add(fpmxDto1);
                }
                if (i == dtoList.size() - 1) {
                    fpmxDto_t.setFpmxDtos(u8ids);
                    mxlist.add(fpmxDto_t);
                }
            }
		}
		mxlist = mxlist.stream().sorted(Comparator.comparing(FpmxDto::getFph))
				.collect(Collectors.toList());
		BigDecimal bigDecimal = new BigDecimal(0);
		if (!CollectionUtils.isEmpty(mxlist)) {
			for (FpmxDto dto : mxlist) {
				bigDecimal = bigDecimal.add(new BigDecimal(dto.getHjje()));
			}
		}
		if ("fp".equals(request.getParameter("flag"))){
			map.put("rows",mxlist);
			return map;
		}else {
			List<FpmxDto> rklist = fpmxService.getRkListByFp(fpmxDto);
			map.put("rows",rklist);
			return map;
		}
	}
	/**
	 * 钉钉合同单号点击查看
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/minidataViewContract")
	public Map<String, Object> minidataViewContract(HtglDto htglDto, HtmxDto htmxDto, HtfkmxDto htfkmxDto) {
		Map<String, Object> map = new HashMap<>();
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_UPLOADCONTRACT.getCode());
		fjcfbDto.setYwid(htglDto.getHtid());
		List<FjcfbDto> h_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		map.put("h_fjcfbDtos",h_fjcfbDtos);
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBCONTRACT.getCode());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		map.put("t_fjcfbDtos",t_fjcfbDtos);
		HtglDto t_htglDto = htglService.getDtoById(htglDto.getHtid());
		FpmxDto fpmxDto=new FpmxDto();
		fpmxDto.setHtid(htglDto.getHtid());
		List<FpmxDto> dtoList = fpmxService.getListForHw(fpmxDto);
		List<FpmxDto> mxlist=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			FpmxDto fpmxDto_t=dtoList.get(0);
			List<FpmxDto> u8ids=new ArrayList<>();
			for(int i=0;i<dtoList.size();i++){
				if(fpmxDto_t.getFpmxid().equals(dtoList.get(i).getFpmxid())){
					FpmxDto fpmxDto1=new FpmxDto();
					fpmxDto1.setRkid(dtoList.get(i).getRkid());
					fpmxDto1.setDhid(dtoList.get(i).getDhid());
					fpmxDto1.setU8rkdh(dtoList.get(i).getU8rkdh());
					fpmxDto1.setLbcskz1(dtoList.get(i).getLbcskz1());
					u8ids.add(fpmxDto1);
                }else{
					fpmxDto_t.setFpmxDtos(u8ids);
					mxlist.add(fpmxDto_t);
					fpmxDto_t=dtoList.get(i);
					u8ids=new ArrayList<>();
					FpmxDto fpmxDto1=new FpmxDto();
					fpmxDto1.setRkid(dtoList.get(i).getRkid());
					fpmxDto1.setDhid(dtoList.get(i).getDhid());
					fpmxDto1.setU8rkdh(dtoList.get(i).getU8rkdh());
					fpmxDto1.setLbcskz1(dtoList.get(i).getLbcskz1());
					u8ids.add(fpmxDto1);
                }
                if(i==dtoList.size()-1){
                    fpmxDto_t.setFpmxDtos(u8ids);
                    mxlist.add(fpmxDto_t);
                }
            }
		}
		mxlist= mxlist.stream().sorted(Comparator.comparing(FpmxDto::getFph))
				.collect(Collectors.toList());
		BigDecimal bigDecimal=new BigDecimal(0);
		if(!CollectionUtils.isEmpty(mxlist)){
			for(FpmxDto dto:mxlist){
				bigDecimal=bigDecimal.add(new BigDecimal(dto.getHjje()));
			}
		}
		List<FpmxDto> rklist = fpmxService.getRkListByFp(fpmxDto);
		if (StringUtil.isNotBlank(htmxDto.getHtid())) {
			List<HtmxDto> htmxDtos = htmxService.getListByHtid(htmxDto.getHtid());
			map.put("qgmxDtos", htmxDtos);
		} else {
			map.put("qgmxDtos", null);
		}
		if (StringUtil.isNotBlank(htfkmxDto.getHtid())) {
			List<HtfkmxDto> htfkmxDtos = htfkmxService.getListByHtid(htfkmxDto.getHtid());
			map.put("htfkmxDtos", htfkmxDtos);
		} else {
			map.put("htfkmxDtos", null);
		}
		map.put("zje",bigDecimal);
		map.put("rklist", rklist);
		map.put("pfmxlist", mxlist);
		map.put("htglDto", t_htglDto);
		map.put("urlPrefix", urlPrefix);
		return map;
	}

	/**
	 * 合同新增页面
	 */
	@RequestMapping(value = "/contract/addContractView")
	public ModelAndView addContractView(HtglDto htglDto, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("contract/contract/contract_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.PAYMENT_METHOD, BasicDataTypeEnum.INVOICE_METHOD,
						BasicDataTypeEnum.CONTRACT_TYPE, BasicDataTypeEnum.CURRENCY,BasicDataTypeEnum.CONTRACT_PAYEY,
						BasicDataTypeEnum.CONTRACT_RISK});
		mav.addObject("paymentlist", jclist.get(BasicDataTypeEnum.PAYMENT_METHOD.getCode()));// 付款方式
		mav.addObject("invoicelist", jclist.get(BasicDataTypeEnum.INVOICE_METHOD.getCode()));// 发票方式
		mav.addObject("contractlist", jclist.get(BasicDataTypeEnum.CONTRACT_TYPE.getCode()));// 合同类型
		mav.addObject("currencylist", jclist.get(BasicDataTypeEnum.CURRENCY.getCode()));// 币种
		mav.addObject("contractpaysylist", jclist.get(BasicDataTypeEnum.CONTRACT_PAYEY.getCode()));//付款方
		mav.addObject("risklist",jclist.get(BasicDataTypeEnum.CONTRACT_RISK.getCode()));//合同风险程度
		mav.addObject("wlfls",JSONObject.toJSONString(redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MATERIAL_CLASSIFICATION.getCode())));//物料分类
		// 设置默认创建日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		htglDto.setCjrq(sdf.format(date));
		if ("systemSaveContractView".equals(htglDto.getLjbj())){
			htglDto.setFormAction("systemSaveContractView");
		}else if ("wwaddSaveContractView".equals(htglDto.getLjbj())){
			htglDto.setFormAction("wwaddSaveContractView");
		}else if ("frameworkSaveContractView".equals(htglDto.getLjbj())){
			htglDto.setFormAction("frameworkSaveContractView");
		}else {
			htglDto.setFormAction("addSaveContractView");
		}
		String htlx=request.getParameter("htlx");
		User user = getLoginInfo(request);
		htglDto.setFzr(user.getYhid());
		htglDto.setFzrmc(user.getZsxm());
		mav.addObject("url", "/contract/contract/pagedataGetContractDetailList");
		mav.addObject("htglDto", htglDto);
		mav.addObject("flag", "add");
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("ywlx", BusTypeEnum.IMP_SUBCONTRACT.getCode());
		FjcfbDto fjcfbDto = new FjcfbDto();
		mav.addObject("fjcfbDto", fjcfbDto);
		mav.addObject("htlx",htlx);
		mav.addObject("yfJson","");
		return mav;
	}
	/**
	 * OA合同新增页面
	 */
	@RequestMapping(value = "/contract/systemContractView")
	public ModelAndView systemContractView(HtglDto htglDto, HttpServletRequest request) {
		htglDto.setLjbj("systemSaveContractView");
		return this.addContractView(htglDto,request);
	}
	/**
	 * OA合同新增保存
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/systemSaveContractView")
	public Map<String, Object> systemSaveContract(HtglDto htglDto, HttpServletRequest request) {
		return this.addSaveContract(htglDto,request);
	}
	/**
	 * 委外合同新增页面
	 */
	@RequestMapping(value = "/contract/wwaddContractView")
	public ModelAndView wwaddContractView(HtglDto htglDto, HttpServletRequest request) {
		htglDto.setLjbj("wwaddSaveContractView");
		return this.addContractView(htglDto,request);
	}
	/**
	 * 委外合同新增保存
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/wwaddSaveContractView")
	public Map<String, Object> wwaddSaveContract(HtglDto htglDto, HttpServletRequest request) {
		return this.addSaveContract(htglDto,request);
	}
	/**
	 * 框架合同新增页面
	 */
	@RequestMapping(value = "/contract/frameworkContractView")
	public ModelAndView frameworkContractView(HtglDto htglDto, HttpServletRequest request) {
		htglDto.setLjbj("frameworkSaveContractView");
		return this.addContractView(htglDto,request);
	}
	/**
	 * 补充合同页面
	 */
	@RequestMapping(value = "/contract/supplementContractView")
	public ModelAndView supplementContractView(HtglDto htglDto) {
		htglDto.setLjbj("supplementContractView");
		return this.modContractView(htglDto);
	}

	/**
	 * 框架合同新增保存
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/frameworkSaveContractView")
	public Map<String, Object> frameworkSaveContractView(HtglDto htglDto, HttpServletRequest request) {
		htglDto.setYwlx(htglDto.getHtywlx());
		User user = getLoginInfo(request);
		htglDto.setLrry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		// 判断合同内部编号是否重复
		boolean isSuccess = htglService.isHtnbbhRepeat(htglDto.getHtnbbh(), htglDto.getHtid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "内部编号重复，请重新输入！");
			return map;
		}
		try {
			isSuccess = htglService.addSaveFrameworkContract(htglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 合同新增保存
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/addSaveContractView")
	public Map<String, Object> addSaveContract(HtglDto htglDto, HttpServletRequest request) {
		htglDto.setYwlx(htglDto.getHtywlx());
		User user = getLoginInfo(request);
		htglDto.setLrry(user.getYhid());
		htglDto.setQgid(htglDto.getDjhs());
		Map<String, Object> map = new HashMap<>();
		// 判断合同内部编号是否重复
		boolean isSuccess = htglService.isHtnbbhRepeat(htglDto.getHtnbbh(), htglDto.getHtid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "内部编号重复，请重新输入！");
			return map;
		}
		if ("1".equals(htglDto.getSfkj())&&StringUtil.isBlank(htglDto.getKjhtid())){
			map.put("status", "fail");
			map.put("message", "该供应商没有签订框架合同!不允许保存！");
			return map;
		}
		// 合同数量校验
		Map<String, Object> checkMap = htglService.checkQuantity(htglDto);
		if(checkMap != null) {
			map.put("status", "fail");
			map.put("message", checkMap.get("message"));
			map.put("htmxList_wlgl", checkMap.get("htmxList_wlgl"));
			map.put("htmxList_qgmx", checkMap.get("htmxList_qgmx"));
			return map;
		}
		try {
			isSuccess = htglService.addSaveContract(htglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "failtwo");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		if("1".equals(systemconfigflg) && isSuccess) {
			htglDto.setPrefixFlg(rabbitFlg);
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htgl.insert",JSONObject.toJSONString(htglDto));
		}
		return map;
	}


	/**
	 * 获取合同请购明细信息
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/pagedataGetContractDetailList")
	public Map<String, Object> getContractDetailList(HtmxDto htmxDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(htmxDto.getHtid())) {
			List<HtmxDto> htmxDtos = htmxService.getListByHtid(htmxDto.getHtid());
			map.put("rows", htmxDtos);
		} else {
			map.put("rows", new ArrayList<>());
		}
		return map;
	}
	/**
	 * 获取原合同明细信息
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/pagedataGetOriginalContractDetails")
	public Map<String, Object> pagedataGetOriginalContractDetails(HtmxDto htmxDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(htmxDto.getHtid())||StringUtil.isNotBlank(htmxDto.getHtmxid())) {
			List<HtmxDto> htmxDtos = htmxService.getOriginalContractDetails(htmxDto);
			map.put("rows", htmxDtos);
		}else {
			map.put("rows", new ArrayList<>());
		}
		return map;
	}
	/**
	 * 合同修改页面
	 */
	@RequestMapping(value = "/contract/modContractView")
	public ModelAndView modContractView(HtglDto htglDto) {
		ModelAndView mav = new ModelAndView("contract/contract/contract_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.PAYMENT_METHOD, BasicDataTypeEnum.INVOICE_METHOD,
						BasicDataTypeEnum.CONTRACT_TYPE, BasicDataTypeEnum.CURRENCY, BasicDataTypeEnum.CONTRACT_PAYEY,BasicDataTypeEnum.CONTRACT_RISK});
		mav.addObject("paymentlist", jclist.get(BasicDataTypeEnum.PAYMENT_METHOD.getCode()));// 付款方式
		mav.addObject("invoicelist", jclist.get(BasicDataTypeEnum.INVOICE_METHOD.getCode()));// 发票方式
		mav.addObject("contractlist", jclist.get(BasicDataTypeEnum.CONTRACT_TYPE.getCode()));// 合同类型
		mav.addObject("currencylist", jclist.get(BasicDataTypeEnum.CURRENCY.getCode()));// 币种
		mav.addObject("contractpaysylist", jclist.get(BasicDataTypeEnum.CONTRACT_PAYEY.getCode()));//付款方
		mav.addObject("risklist",jclist.get(BasicDataTypeEnum.CONTRACT_RISK.getCode()));//合同风险程度
		mav.addObject("wlfls",JSONObject.toJSONString(redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MATERIAL_CLASSIFICATION.getCode())));//物料分类
		// 查询合同信息
		HtglDto t_htglDto = htglService.getDto(htglDto);
		t_htglDto.setAuditType(AuditTypeEnum.AUDIT_CONTRACT.getCode());
		HtfktxDto htfktxDto=new HtfktxDto();
		htfktxDto.setHtid(htglDto.getHtid());
		List<HtfktxDto> listByHtid = htfktxService.getListByHtid(htfktxDto);
		mav.addObject("yfJson",JSON.toJSONString(listByHtid));
		if ("auditSaveContractView".equals(htglDto.getLjbj())){
			t_htglDto.setFormAction("auditSaveContractView");
		}else if ("supplementContractView".equals(htglDto.getLjbj())){
			t_htglDto.setBchtid(t_htglDto.getHtid());
			t_htglDto.setHtid(null);
			t_htglDto.setYyhtnbbh(t_htglDto.getHtnbbh());
			t_htglDto.setHtnbbh(null);
			t_htglDto.setHtwbbh(null);
			t_htglDto.setQgid(null);
			t_htglDto.setFormAction("supplementSaveContractView");
			Map<String, Object> contractCode = getContractCode(t_htglDto);
			if ("success".equals(String.valueOf(contractCode.get("status")))){
				t_htglDto.setHtnbbh(String.valueOf(contractCode.get("message")));
				t_htglDto.setHtwbbh(String.valueOf(contractCode.get("message")));
			}
			if ("3".equals(t_htglDto.getHtlx())){
				t_htglDto.setKjlx("1");
			}
		}else {
			t_htglDto.setFormAction("modSaveContractView");
		}
		boolean yhtbchtFlag = StringUtil.isNotBlank(t_htglDto.getBchtid())&&!"1".equals(t_htglDto.getKjlx());
		if (yhtbchtFlag){
			FjcfbDto fjcfbDto_yht=new FjcfbDto();
			fjcfbDto_yht.setYwid(t_htglDto.getBchtid());
			fjcfbDto_yht.setYwlx(BusTypeEnum.IMP_SUBCONTRACT.getCode());
			List<FjcfbDto> fjcfbDtos_yht = fjcfbService.getDtoList(fjcfbDto_yht);
			mav.addObject("fjcfbDtos_yht", fjcfbDtos_yht);
		}
		if (StringUtil.isNotBlank(t_htglDto.getHtid())){
			FjcfbDto fjcfbDto=new FjcfbDto();
			fjcfbDto.setYwid(htglDto.getHtid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBCONTRACT.getCode());
			List<FjcfbDto> dto = fjcfbService.getDtoList(fjcfbDto);
			mav.addObject("fjcfbDtos",dto);
		}
		//是否为普通合同的补充合同
		mav.addObject("yhtbchtFlag", yhtbchtFlag?"1":"0");
		mav.addObject("url", "/contract/contract/pagedataGetContractDetailList");
		mav.addObject("htglDto", t_htglDto);
		mav.addObject("ywlx", BusTypeEnum.IMP_SUBCONTRACT.getCode());
		mav.addObject("flag", "mod");
		mav.addObject("htlx", t_htglDto.getHtlx());
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 合同审核页面
	 */
	@RequestMapping(value = "/contract/auditContractView")
	public ModelAndView auditContractView(HtglDto htglDto) {
		htglDto.setLjbj("auditSaveContractView");
		return this.modContractView(htglDto);
	}
	/**
	 * 合同审核保存
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/auditSaveContractView")
	public Map<String, Object> auditSaveContract(HtglDto htglDto, HttpServletRequest request) {
		return this.modSaveContract(htglDto,request);
	}
	/**
	 * 补充合同保存
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/supplementSaveContractView")
	public Map<String, Object> supplementSaveContractView(HtglDto htglDto, HttpServletRequest request) {
		if ("3".equals(htglDto.getHtlx())){
			return this.frameworkSaveContractView(htglDto,request);
		}else {
			return this.addSaveContract(htglDto,request);
		}
	}
	/**
	 * 合同修改保存
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/modSaveContractView")
	public Map<String, Object> modSaveContract(HtglDto htglDto, HttpServletRequest request) {
		htglDto.setYwlx(htglDto.getHtywlx());
		User user = getLoginInfo(request);
		htglDto.setXgry(user.getYhid());
		htglDto.setQgid(htglDto.getDjhs());
		Map<String, Object> map = new HashMap<>();
		// 判断合同内部编号是否重复
		boolean isSuccess = htglService.isHtnbbhRepeat(htglDto.getHtnbbh(), htglDto.getHtid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "内部编号重复，请重新输入！");
			return map;
		}
		if ("3".equals(htglDto.getHtlx())){
			return this.modFrameworkSaveContract(htglDto);
		}
		if ("1".equals(htglDto.getSfkj())&&StringUtil.isBlank(htglDto.getKjhtid())){
			map.put("status", "fail");
			map.put("message", "该供应商没有签订框架合同!不允许保存！");
			return map;
		}
		//补充合同不校验数量
		if (StringUtil.isBlank(htglDto.getBchtid())){
			// 合同数量校验
			Map<String, Object> checkMap = htglService.checkQuantity(htglDto);
			if(checkMap != null) {
				map.put("status", "fail");
				map.put("message", checkMap.get("message"));
				map.put("htmxList_wlgl", checkMap.get("htmxList_wlgl"));
				map.put("htmxList_qgmx", checkMap.get("htmxList_qgmx"));
				return map;
			}
		}
		try {
			isSuccess = htglService.modSaveContract(htglDto);
			if("1".equals(systemconfigflg) && isSuccess) {
				htglDto.setPrefixFlg(rabbitFlg);
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htgl.update",JSONObject.toJSONString(htglDto));
			}
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
			map.put("ywid", htglDto.getHtid());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	public Map<String, Object> modFrameworkSaveContract(HtglDto htglDto) {
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess;
		try {
			isSuccess = htglService.modFrameworkSaveContract(htglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}
	/**
	 * 合同高级修改页面
	 */
	@RequestMapping(value = "/contract/advancedmodContractView")
	public ModelAndView advancedModContractView(HtglDto htglDto) {
		ModelAndView mav = new ModelAndView("contract/contract/contract_edit");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.PAYMENT_METHOD, BasicDataTypeEnum.INVOICE_METHOD,
						BasicDataTypeEnum.CONTRACT_TYPE, BasicDataTypeEnum.CURRENCY, BasicDataTypeEnum.CONTRACT_PAYEY});
		mav.addObject("paymentlist", jclist.get(BasicDataTypeEnum.PAYMENT_METHOD.getCode()));// 付款方式
		mav.addObject("invoicelist", jclist.get(BasicDataTypeEnum.INVOICE_METHOD.getCode()));// 发票方式
		mav.addObject("contractlist", jclist.get(BasicDataTypeEnum.CONTRACT_TYPE.getCode()));// 合同类型
		mav.addObject("currencylist", jclist.get(BasicDataTypeEnum.CURRENCY.getCode()));// 币种
		mav.addObject("contractpaysylist", jclist.get(BasicDataTypeEnum.CONTRACT_PAYEY.getCode()));//付款方
		mav.addObject("wlfls",JSONObject.toJSONString(redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MATERIAL_CLASSIFICATION.getCode())));//物料分类
		// 查询合同信息
		HtglDto t_htglDto = htglService.getDto(htglDto);
		t_htglDto.setAuditType(AuditTypeEnum.AUDIT_CONTRACT.getCode());
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBCONTRACT.getCode());
		fjcfbDto.setYwid(htglDto.getHtid());
		List<FjcfbDto> dto = fjcfbService.getDtoList(fjcfbDto);
		mav.addObject("fjcfbDtos",dto);
		HtfktxDto htfktxDto=new HtfktxDto();
		htfktxDto.setHtid(htglDto.getHtid());
		List<HtfktxDto> listByHtid = htfktxService.getListByHtid(htfktxDto);
		mav.addObject("yfJson",JSON.toJSONString(listByHtid));

		t_htglDto.setFormAction("advancedmodSaveContractView");

		mav.addObject("url", "/contract/contract/pagedataGetContractDetailList");
		mav.addObject("htglDto", t_htglDto);
		mav.addObject("ywlx", BusTypeEnum.IMP_SUBCONTRACT.getCode());
		mav.addObject("flag", "advancedmod");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 合同高级修改保存
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/advancedmodSaveContractView")
	public Map<String, Object> advancedmodSaveContract(HtglDto htglDto, HttpServletRequest request) {
		htglDto.setYwlx(htglDto.getHtywlx());
		User user = getLoginInfo(request);
		htglDto.setXgry(user.getYhid());
		htglDto.setQgid(htglDto.getDjhs());
		Map<String, Object> map = new HashMap<>();
		// 判断合同内部编号是否重复
		boolean isSuccess = htglService.isHtnbbhRepeat(htglDto.getHtnbbh(), htglDto.getHtid());
		if(!isSuccess) {
			map.put("status", "fail");
			map.put("message", "内部编号重复，请重新输入！");
			return map;
		}
		// 合同数量校验
		Map<String, Object> checkMap = htglService.checkQuantity(htglDto);
		if(checkMap != null) {
			map.put("status", "fail");
			map.put("message", checkMap.get("message"));
			map.put("htmxList_wlgl", checkMap.get("htmxList_wlgl"));
			map.put("htmxList_qgmx", checkMap.get("htmxList_qgmx"));
			return map;
		}
		try {
			isSuccess = htglService.advancedModSaveContract(htglDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
			map.put("ywid", htglDto.getHtid());
			if("1".equals(systemconfigflg) && isSuccess) {
				htglDto.setPrefixFlg(rabbitFlg);
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htgl.advancedupdate",JSONObject.toJSONString(htglDto));
			}
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 合同内部编码刷新
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/pagedataGetContractCode")
	public Map<String, Object> getContractCode(HtglDto htglDto) {
		return htglService.generateContractCode(htglDto);
	}

	/**
	 * 合同周期统计
	 */
	@RequestMapping("/statistic/pagedataStatisticCycle")
	@ResponseBody
	public Map<String, Object> statisticCycle(HtmxDto htmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> resultList = htmxService.statisticCycle(htmxDto);
		map.put("resultList", resultList);
		return map;
	}

	/**
	 * 含税单价统计
	 */
	@RequestMapping("/statistic/pagedataStatisticTaxPrice")
	@ResponseBody
	public Map<String, Object> pagedataStatisticTaxPrice(HtmxDto htmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> resultList = htmxService.statisticTaxPrice(htmxDto);
		map.put("resultList", resultList);
		return map;
	}

	/**
	 * 合同审核列表
	 */
	@RequestMapping("/contract/pageListAuditContract")
	public ModelAndView pageListAuditContract() {
		ModelAndView mav = new ModelAndView("contract/contract/contract_auditList");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 合同审核列表
	 */
	@RequestMapping("/contract/pageGetListAuditContract")
	@ResponseBody
	public Map<String, Object> pageGetListAuditContract(HtglDto htglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		// 附加委托参数
		DataPermission.addWtParam(htglDto);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(htglDto.getDqshzt())) {
			List<AuditTypeEnum> auditTypes = new ArrayList<>();
			auditTypes.add(AuditTypeEnum.AUDIT_CONTRACT);
			auditTypes.add(AuditTypeEnum.AUDIT_CONTRACT_OA);
			DataPermission._add(htglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "htgl", "htid",
					auditTypes,null);
		} else {
			List<AuditTypeEnum> auditTypes = new ArrayList<>();
			auditTypes.add(AuditTypeEnum.AUDIT_CONTRACT);
			auditTypes.add(AuditTypeEnum.AUDIT_CONTRACT_OA);
			DataPermission._add(htglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "htgl", "htid",
					auditTypes,null);
		}
		DataPermission.addCurrentUser(htglDto, getLoginInfo(request));
		List<HtglDto> listMap = htglService.getPagedAuditHtgl(htglDto);

		map.put("total", htglDto.getTotalNumber());
		map.put("rows", listMap);
		return map;
	}

	/**
	 * 生成合同
	 */
	@RequestMapping("/contract/contractGenerate")
	public ModelAndView generateContract(HtglDto htglDto) {
		ModelAndView mav=new ModelAndView("contract/contract/contract_generate");
		mav.addObject("htglDto",htglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 替换合同模板
	 */
	@RequestMapping("/contract/pagedataReplaceContract")
	@ResponseBody
	public Map<String,Object> replaceContract(HtglDto htglDto){
		return htglService.getParamForContract(htglDto);
	}

	/**
	 * 物料采购列表页面
	 */
	@RequestMapping("/contract/pageListMatterPurchase")
	public ModelAndView pageListPurchaseMatter() {
		ModelAndView mav=new ModelAndView("contract/contract/matterPurchase_list");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 物料采购列表
	 */
	@RequestMapping("/contract/pageGetListMatterPurchase")
	@ResponseBody
	public Map<String,Object> pageGetListMatterPurchase(HtmxDto htmxDto){
		List<HtmxDto> t_List = htmxService.getPagedDtoList(htmxDto);
		Map<String, Object> map = new HashMap<>();
		map.put("total", htmxDto.getTotalNumber());
		map.put("rows", t_List);
		return map;
	}
	/**
	 * 物料采购列表
	 */
	@RequestMapping("/contract/minidataListMatterPurchase")
	@ResponseBody
	public Map<String,Object> minidataListMatterPurchase(HtmxDto htmxDto){
		return pageGetListMatterPurchase(htmxDto);
	}

	/**
	 * 获取物料采购明细信息
	 */
	@RequestMapping("/contract/viewMatterPurchase")
	public ModelAndView viewMatterPurchase(HtmxDto htmxDto) {
		ModelAndView mav=new ModelAndView("contract/contract/matterPurchase_view");
		htmxDto=htmxService.getDtoById(htmxDto.getHtmxid());
		if(htmxDto!=null) {
			HtglDto htglDto=new HtglDto();
			htglDto.setHtid(htmxDto.getHtid());
			htglDto=htglService.getDto(htglDto);
			mav.addObject("htglDto", htglDto);
		}
		mav.addObject("htmxDto", htmxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 获取物料采购明细信息接口
	 */
	@RequestMapping("/contract/minidataGetMatterPurchase")
	@ResponseBody
	public Map<String,Object> minidataGetMatterPurchase(HtmxDto htmxDto){
		Map<String, Object> map = new HashMap<>();
		htmxDto=htmxService.getDtoById(htmxDto.getHtmxid());
		map.put("htmxDto", htmxDto);
		return map;
	}

	/**
	 *	 合同付款维护页面
	 */
	@RequestMapping("/contract/paymentContract")
	public ModelAndView paymentContract(HtglDto htglDto, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("contract/contract/contract_pay");
		HtglDto t_htglDto = htglService.getDtoById(htglDto.getHtid());
		HtmxDto htmxDto=new HtmxDto();
		htmxDto.setHtid(htglDto.getHtid());
		List<HtmxDto> htmxlist=htmxService.getDtoList(htmxDto);
		// 获取默认负责人
		HtfkqkDto htfkqkDto = new HtfkqkDto();
		User user = getLoginInfo(request);
		user = commonService.getUserInfoById(user);
		htfkqkDto.setSqrmc(user.getZsxm());
		htfkqkDto.setSqr(user.getYhid());
		htfkqkDto.setSqbmmc(user.getJgmc());
		htfkqkDto.setSqbm(user.getJgid());
		htfkqkDto.setZfdx(t_htglDto.getGys());
		htfkqkDto.setZfdxmc(t_htglDto.getGysmc());
		htfkqkDto.setZffkhh(t_htglDto.getZffkhh());
		htfkqkDto.setZffyhzh(t_htglDto.getZffyhzh());
		if(!CollectionUtils.isEmpty(htmxlist)){
			//获取默认用款部门
			htfkqkDto.setYkbm(htmxlist.get(0).getSqbm());
			htfkqkDto.setYkbmmc(htmxlist.get(0).getSqbmmc());
		}
		htfkqkDto.setAuditType(AuditTypeEnum.AUDIT_CONTRACT_PAYMENT.getCode());
		List<JcsjDto> fkfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYWAY.getCode());
		List<JcsjDto> fkfs = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYER.getCode());
		List<JcsjDto> fygss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAY_BELONG.getCode());  //付款申请  费用归属
		List<JcsjDto> djcdfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECEIPTS_TRANSMIT_TYPE.getCode());  //付款申请  费用归属
		mav.addObject("djcdfss",djcdfss);
		mav.addObject("fkfss",fkfss);
		mav.addObject("fkfs",fkfs);
		mav.addObject("fygss",fygss);
		mav.addObject("czr", user.getZsxm());
		mav.addObject("formAction", "paymentSaveContract");
		if (!CollectionUtils.isEmpty(djcdfss)){
			for (JcsjDto jcsjDto : djcdfss) {
				if ("1".equals(jcsjDto.getSfmr())){
					htfkqkDto.setDjcdfs(jcsjDto.getCsid());
				}
			}
		}
		//自动生成付款单号
		String fkdh=htfkqkService.generatePayDjh(htfkqkDto);
		htfkqkDto.setFkdh(fkdh);
		// 获取百分比
		if (t_htglDto.getZje() != null) {
			BigDecimal zje = new BigDecimal(t_htglDto.getZje());
			if (zje.setScale(2, RoundingMode.UP).compareTo(new BigDecimal("0"))==0) {
				t_htglDto.setFkbfb("100%");
				t_htglDto.setZje("0");
			} else {
				if (t_htglDto.getYfje() != null) {
					BigDecimal yfje = new BigDecimal(t_htglDto.getYfje());
					if(!(yfje.setScale(2, RoundingMode.UP).compareTo(new BigDecimal("0"))==0)) {
						// 设置精确到小数点后2位
						BigDecimal result = yfje.divide(zje, 4,RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
						if(result.compareTo(new BigDecimal("100")) > -1) {
							t_htglDto.setFkbfb("100%");
						}else {
							t_htglDto.setFkbfb(result + "%");
						}
					}else {
						t_htglDto.setFkbfb("0%");
					}
				}
			}
		}
		mav.addObject("htglDto", t_htglDto);
		mav.addObject("htfkqkDto", htfkqkDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}


	/**
	 *	 合同发票维护页面
	 */
	@RequestMapping("/contract/receiptContract")
	public ModelAndView receiptContract(HtglDto htglDto,HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("contract/contract/contract_receipt");
		HtglDto t_htglDto = htglService.getDtoById(htglDto.getHtid());
		String gyssl = t_htglDto.getSl();
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[] { BasicDataTypeEnum.INVOICE_CLASS});
		mav.addObject("fpzllist", jclist.get(BasicDataTypeEnum.INVOICE_CLASS.getCode()));
		// 获取默认负责人
		HtfpqkDto htfpqkDto = new HtfpqkDto();
		htfpqkDto.setHtid(t_htglDto.getHtid());
		List<HtfpqkDto> htfpqkDtos = htfpqkService.getFpqkList(htfpqkDto);
		BigDecimal zjg=new BigDecimal("0");
		BigDecimal htzjg=new BigDecimal(t_htglDto.getZje());
		DecimalFormat df4 = new DecimalFormat("#,##0.000");
		if(!CollectionUtils.isEmpty(htfpqkDtos)) {
			for (HtfpqkDto dto : htfpqkDtos) {
				zjg = zjg.add(new BigDecimal(dto.getFpje()));
			}
		}
		zjg = zjg.setScale(2, RoundingMode.UP);
		htzjg = htzjg.setScale(2, RoundingMode.UP);
		String str_fpzje=df4.format(zjg);
		String str_htzje=df4.format(htzjg);
		htfpqkDto.setFpzje(str_fpzje);
		htfpqkDto.setYwlx(BusTypeEnum.IMP_FP_ANNEX.getCode());
		t_htglDto.setZje(str_htzje);
		mav.addObject("gyssl", gyssl);
		mav.addObject("htfplist", htfpqkDtos);
		User user = getLoginInfo(request);
		mav.addObject("czr", user.getZsxm());
		mav.addObject("formAction", "receiptSaveContract");
		mav.addObject("htglDto", t_htglDto);
		mav.addObject("htfpqkDto", htfpqkDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}


	/**
	 * 合同发票新增保存
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/receiptSaveContract")
	public Map<String, Object> receiptSaveContract(HtfpqkDto htfpqkDto, HttpServletRequest request) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		htfpqkDto.setLrry(user.getYhid());
		if (CollectionUtils.isEmpty(htfpqkService.getFpqk(htfpqkDto))){
			boolean isSuccess = htfpqkService.insertFpqk(htfpqkDto);
			if (isSuccess) {
				String ICOMM_FP00001 = xxglService.getMsg("ICOMM_FP00001");
				String ICOMM_FP00002 = xxglService.getMsg("ICOMM_FP00002");
				List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(DingMessageType.INVOICE_MAINTENANCE.getCode());
				boolean b = htfpqkService.sendInvoiceMessage(ddxxglDtos,htfpqkDto,ICOMM_FP00001,ICOMM_FP00002);//发送发票信息
				if (b){
					HtglDto t_htglDto = htglService.getDtoById(htfpqkDto.getHtid());
					map.put("htid", t_htglDto.getHtid());
					map.put("status", "success");
					map.put("message", xxglService.getModelById("ICOM00001").getXxnr());
				}else{
					map.put("status", "fail");
					map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
				}

			} else {
				map.put("status", "fail");
				map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
			}
		}else {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOMM_FP_0001").getXxnr());
		}
		return map;
	}

	/**
	 * 	合同付款明细列表
	 */
	@RequestMapping("/contract/pagedataListPayment")
	@ResponseBody
	public Map<String, Object> pageGetListPayment(HtfkmxDto htfkmxDto) {
		List<HtfkmxDto> htfkmxDtos = htfkmxService.getDtoList(htfkmxDto);
		Map<String, Object> map = new HashMap<>();
		map.put("total", htfkmxDto.getTotalNumber());
		map.put("rows", htfkmxDtos);
		return map;
	}


	/**
	 * 合同付款新增保存
	 */
	@RequestMapping(value = "/contract/paymentSaveContract")
	@ResponseBody
	public Map<String, Object> paymentSaveContract(HtfkqkDto htfkqkDto, HttpServletRequest request) {
		User user = getLoginInfo(request);
		htfkqkDto.setLrry(user.getYhid());
		Map<String,Object> map=htfkqkService.checkMoney(htfkqkDto);
		if(map.get("status")=="fail")
			return map;
		boolean isSuccess = htfkqkService.insertFkqk(htfkqkDto);
		map.put("ywid",htfkqkDto.getHtfkid());
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 合同付款提醒界面
	 */
	@RequestMapping(value = "/contract/paymentremindPage")
	public ModelAndView paymentremindPage(HtglDto htglDto) {
		ModelAndView mav = new ModelAndView("contract/contract/contract_payremind");
		HtglDto htglDtoT = htglService.queryBchtglDto(htglDto);
		htglDtoT.setHtid(htglDto.getHtid());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("htglDto", htglDtoT);
		return mav;
	}

	/**
	 * 合同付款提醒列表
	 */
	@RequestMapping("/contract/pagedataListPaymentRemind")
	@ResponseBody
	public Map<String,Object> listPaymentRemind(HtfktxDto htfktxDto){
		Map<String,Object> map=new HashMap<>();
		List<HtfktxDto> list=htfktxService.getListByHtid(htfktxDto);
		map.put("rows", list);
		return map;
	}

	/**
	 * 合同付款提醒保存
	 */
	@RequestMapping("/contract/paymentremindSavePage")
	@ResponseBody
	public Map<String,Object> saveContractPayRemind(HtfktxDto htfktxDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo(request);
		htfktxDto.setLrry(user.getYhid());
		List<HtfktxDto> htfkDtos = JSON.parseArray(htfktxDto.getFktxJson(), HtfktxDto.class);
		BigDecimal zje=new BigDecimal("0");
		BigDecimal htzje=new BigDecimal(htfktxDto.getHtzje());
		if(!CollectionUtils.isEmpty(htfkDtos)){
			for(HtfktxDto dto:htfkDtos){
				BigDecimal yfje=new BigDecimal(dto.getYfje());
				zje=zje.add(yfje);
			}
		}
		if(zje.compareTo(htzje) > 0){
			map.put("status","fail");
			map.put("message","预付金额总和不得大于总金额！");
		}else{
			boolean isSuccess=htfktxService.saveContractPayRemind(htfktxDto,htfkDtos);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr() : xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 合同付款修改页面
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/modViewPayContract")
	public Map<String, Object> modViewPayContract(HtfkqkDto htfkqkDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		HtfkqkDto htfkqkDto_t = htfkqkService.getDtoById(htfkqkDto.getHtfkid());
		User user = getLoginInfo(request);
		map.put("czr", user.getZsxm());
		map.put("htzje", htfkqkDto_t.getHtzje());
		map.put("formAction", "modSavePayContract");
//		map.put("htid", htfkqkDto_t.getHtid());
//		map.put("fkje", htfkqkDto_t.getFkje());
		map.put("fkrq", htfkqkDto_t.getFkrq());
		map.put("fkbfb", htfkqkDto_t.getFkbfb() +"%");
		map.put("htfkid", htfkqkDto_t.getHtfkid());
		return map;
	}

	/**
	 * 合同发票修改页面
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/modViewReceiptContract")
	public Map<String, Object> modViewReceiptContract(HtfpqkDto htfpqkDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		HtfpqkDto htfpqkDto_t = htfpqkService.getDtoById(htfpqkDto.getHtfpid());
		// 查询附件信息 -采购单
		FjcfbDto fjcfbDto = new FjcfbDto();
		htfpqkDto_t.setYwlx(BusTypeEnum.IMP_FP_ANNEX.getCode());
		fjcfbDto.setYwlx(htfpqkDto.getYwlx());
		fjcfbDto.setYwid(htfpqkDto.getHtfpid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		map.put("fjcfbDtos", fjcfbDtos);
		User user=getLoginInfo(request);
		map.put("czr", user.getZsxm());
		map.put("formAction", "modSaveReceiptContract");
		map.put("htfpqkDto", htfpqkDto_t);
		return map;
	}


	/**
	 * 合同发票修改保存
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/modSaveReceiptContract")
	public Map<String, Object> modSaveReceiptContract(HtfpqkDto htfpqkDto, HttpServletRequest request) throws BusinessException{
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		htfpqkDto.setXgry(user.getYhid());
		if (htfpqkService.getFpqk(htfpqkDto).size() <= 1 ){
			boolean isSuccess = htfpqkService.updateFpqk(htfpqkDto);
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		}else{
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOMM_FP_0001").getXxnr());
		}
		return map;

	}

	/**
	 * 合同发票删除
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/delSaveReceiptContract")
	public Map<String, Object> delSaveReceiptContract(HtfpqkDto htfpqkDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		htfpqkDto.setScry(user.getYhid());
		boolean isSuccess = htfpqkService.deleteFpqk(htfpqkDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 合同提交页面
	 */
	@RequestMapping(value = "/contract/submitContractView")
	public ModelAndView submitContractView(HtglDto htglDto) {
		ModelAndView mav = new ModelAndView("contract/contract/contract_submit");
		// 查询合同信息
		HtglDto t_htglDto = htglService.getDto(htglDto);
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBCONTRACT.getCode());
		fjcfbDto.setYwid(htglDto.getHtid());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos",t_fjcfbDtos);
		t_htglDto.setHtywlx(t_htglDto.getYwlx());
		t_htglDto.setFormAction("submitSaveContractView");
		//获取部门钉钉审核流程
		List<DdxxglDto> jgsplist=ddxxglService.getDingtalkAuditDep(DdAuditTypeEnum.SP_HT.getCode());
		mav.addObject("jgsplist", jgsplist);
		mav.addObject("url", "/contract/contract/pagedataGetContractDetailList");
		mav.addObject("ywlx", BusTypeEnum.IMP_SUBCONTRACT.getCode());
		t_htglDto.setAuditType("1".equals(t_htglDto.getHtlx())?AuditTypeEnum.AUDIT_CONTRACT_OA.getCode():AuditTypeEnum.AUDIT_CONTRACT.getCode());
		mav.addObject("htglDto", t_htglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 查询抄送人员
	 */
	@RequestMapping("/contract/pagedataSelectCsrs")
	@ResponseBody
	public Map<String,Object> selectCsrs() {
		Map<String,Object> map = new HashMap<>();
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb("DINGMESSAGETYPE");
		jcsjDto.setCsdm("CONTRACT_AUDIT_CC");
		List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
		map.put("ddxxglDtos", ddxxglDtos);
		return map;
	}

	/**
	 * 合同提交保存
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/submitSaveContractView")
	public Map<String, Object> submitSaveContract(HtglDto htglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		//校验是否上传附件
		boolean result_t = htglService.isUploadAttachments(htglDto);
		if(!result_t) {
			map.put("status", "fail");
			map.put("message","请上传附件！");
			return map;
		}
		User user = getLoginInfo(request);
		htglDto.setXgry(user.getYhid());
		htglDto.setQgid(htglDto.getDjhs());
		htglDto.setYwlx(htglDto.getHtywlx());
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		htglDto.setTjrq(dateString);
		boolean isSuccess;
		try {
			if ("3".equals(htglDto.getHtlx())){
				isSuccess = htglService.updateContract(htglDto)>0;
				// 文件复制到正式文件夹，插入信息至正式表
				if (!CollectionUtils.isEmpty(htglDto.getFjids())) {
					for (int i = 0; i < htglDto.getFjids().size(); i++) {
						fjcfbService.save2RealFile(htglDto.getFjids().get(i), htglDto.getHtid());
					}
				}
			}else {
				//校验信息是否已经完善
				boolean result=htglService.checkHtMsg(htglDto);
				if(!result){
					map.put("status", "fail");
					map.put("message","请先完善合同信息，价格，数量，期望到货日期不能为空！");
					return map;
				}
				isSuccess = htglService.modSaveContract(htglDto);
				if("1".equals(systemconfigflg) && isSuccess) {
					htglDto.setPrefixFlg(rabbitFlg);
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htgl.update",JSONObject.toJSONString(htglDto));
				}
			}
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
					: xxglService.getModelById("ICOM00002").getXxnr());
			map.put("ywid", htglDto.getHtid());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return map;
	}

	/**
	 * 查询PDF版合同
	 */
	@RequestMapping("/contract/pagedataGetConttractPDF")
	@ResponseBody
	public Map<String,Object> pagedataGetConttractPDF(FjcfbDto fjcfbDto){
		Map<String,Object> map=new HashMap<>();
		FjcfbDto fjcfbDto_t=fjcfbService.getDto(fjcfbDto);
		map.put("fjcfbDto", fjcfbDto_t);
		return map;
	}

	/**
	 * 删除合同
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/delContractView")
	public Map<String,Object> delContractView(HtglDto htglDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		try {
			//获取用户信息
			User user = getLoginInfo(request);
			htglDto.setScry(user.getYhid());
			htglDto.setFqbj("1");
			boolean isSuccess = htglService.deleteHtgl(htglDto);
			if("1".equals(systemconfigflg) && isSuccess) {
				htglDto.setPrefixFlg(rabbitFlg);
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htgl.del",JSONObject.toJSONString(htglDto));
			}
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
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
	 * 	废弃合同
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/discardContractView")
	public Map<String,Object> discardContractView(HtglDto htglDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		try {
			//获取用户信息
			User user = getLoginInfo(request);
			htglDto.setScry(user.getYhid());
			htglDto.setFqbj("2");
			boolean isSuccess = htglService.deleteHtgl(htglDto);
			if("1".equals(systemconfigflg) && isSuccess) {
				htglDto.setPrefixFlg(rabbitFlg);
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htgl.del",JSONObject.toJSONString(htglDto));
			}
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
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
	 * 	合同外发盖章
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/outgrowthContractView")
	public Map<String,Object> outgrowthContractView(HtglDto htglDto){
		Map<String,Object> map=new HashMap<>();
		boolean isSuccess = htglService.OutgrowthContract(htglDto);
		if("1".equals(systemconfigflg) && isSuccess) {
			htglDto.setPrefixFlg(rabbitFlg);
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htgl.updateOut",JSONObject.toJSONString(htglDto));
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 正式合同上传页面
	 */
	@RequestMapping(value = "/contract/formalContractView")
	public ModelAndView formalContractView(HtglDto htglDto) {
		ModelAndView mav = new ModelAndView("contract/contract/contract_upload");
		// 查询合同信息
		HtglDto t_htglDto = htglService.getDto(htglDto);
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_UPLOADCONTRACT.getCode());
		fjcfbDto.setYwid(htglDto.getHtid());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos",t_fjcfbDtos);
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		t_htglDto.setDdrq(dateString);
		t_htglDto.setFormAction("formalSaveContractView");
		mav.addObject("url", "/contract/contract/pagedataGetContractDetailList");
		mav.addObject("fjywlx", BusTypeEnum.IMP_UPLOADCONTRACT.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("htglDto",t_htglDto);
		return mav;
	}

	/**
	 *正式合同提交保存
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/formalSaveContractView")
	public Map<String, Object> formalSaveContractView(HtglDto htglDto, HttpServletRequest request) {
		User user = getLoginInfo(request);
		htglDto.setXgry(user.getYhid());
		htglDto.setSzbj("1");
		Map<String, Object> map = new HashMap<>();
		if (CollectionUtils.isEmpty(htglDto.getFjids())) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		}
		boolean isSuccess = htglService.saveSzht(htglDto);
		if("1".equals(systemconfigflg) && isSuccess) {
			htglDto.setPrefixFlg(rabbitFlg);
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htgl.update",JSONObject.toJSONString(htglDto));
		}
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
				: xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 *过程维护
	 */
	@RequestMapping(value = "/contract/maintenanceMatterContract")
	public ModelAndView maintenanceMatterContract(HtmxDto htmxDto) {
		ModelAndView mav = new ModelAndView("contract/contract/matterPuchase_maintenance");
		htmxDto.setFormAction("maintenanceSaveContract");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[] { BasicDataTypeEnum.SD_TYPE});
		mav.addObject("maintenancelist", jclist.get(BasicDataTypeEnum.SD_TYPE.getCode()));// 快递类型
		mav.addObject("htmxDto",htmxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 *过程维护保存
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/maintenanceSaveContract")
	public Map<String, Object> maintenanceSaveContract(HtmxDto htmxDto, HttpServletRequest request) {
		User user = getLoginInfo(request);
		htmxDto.setXgry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess = htmxService.modSaveMaintenance(htmxDto);
		if("1".equals(systemconfigflg) && isSuccess) {
			htmxDto.setPrefixFlg(rabbitFlg);
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htmx.update",JSONObject.toJSONString(htmxDto));
		}
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr()
				: xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 到货新增选择合同页面
	 */
	@RequestMapping("/contract/pagedataChooseListContract")
	public ModelAndView chooseHtxx(HtglDto htglDto) {
		ModelAndView mav=new ModelAndView("contract/contract/contract_chooseList");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(
				new BasicDataTypeEnum[] { BasicDataTypeEnum.PAYMENT_METHOD, BasicDataTypeEnum.INVOICE_METHOD });
		mav.addObject("fkfslist", jclist.get(BasicDataTypeEnum.PAYMENT_METHOD.getCode()));
		mav.addObject("fpfslist", jclist.get(BasicDataTypeEnum.INVOICE_METHOD.getCode()));
		HtglDto t_htglDto = new HtglDto();
		if (htglDto!=null){
			t_htglDto.setHtlx(htglDto.getHtlx());
			t_htglDto.setCghzbj(htglDto.getCghzbj());
		}
		t_htglDto.setAuditType(AuditTypeEnum.AUDIT_CONTRACT.getCode());
		mav.addObject("htglDto", t_htglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取合同列表数据
	 */
	@RequestMapping(value = "/contract/pagedataAllContract")
	@ResponseBody
	public Map<String,Object> pagedataAllContract(HtglDto htglDto) {
		Map<String,Object> map=new HashMap<>();
		htglDto.setZt("80");
		List<HtglDto> list=htglService.getPagedDtoKdhList(htglDto);
		map.put("total", htglDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 获取合同列表数据
	 */
	@RequestMapping(value = "/contract/pagedataGetPageListAllContract")
	@ResponseBody
	public Map<String,Object> chooseListPurchaseInfo(HtglDto htglDto) {
		Map<String,Object> map=new HashMap<>();
		htglDto.setZt("80");
		List<HtglDto> list=htglService.getPagedDtoKdhList(htglDto);
		map.put("total", htglDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 跳转合同明细列表
	 */
	@RequestMapping(value = "/contract/pagedataChooseListContractInfo")
	public ModelAndView chooseListPurchaseInfo(HtmxDto htmxDto) {
		ModelAndView mav=new ModelAndView("contract/contract/contract_chooseInfoList");
		HtglDto htglDto = htglService.getDtoById(htmxDto.getHtid());
		// 根据合同ID查询合同明细(80状态)
		htmxDto.setZt("80");
		List<HtmxDto> htmxDtos = htmxService.getHtmxList(htmxDto);
		List<HtmxDto> t_htmxDtos = new ArrayList<>();
		// 过滤合同明细信息
		for(HtmxDto t_htmxDto : htmxDtos) {
			HwxxDto hwxxDto_t = new HwxxDto();
			hwxxDto_t.setHtmxid(t_htmxDto.getHtmxid());
			hwxxDto_t.setCghzbj(htmxDto.getCghzbj());
			List<HwxxDto> hwxxlist=hwxxService.getDtoByHtmxid(hwxxDto_t);
			double sjdhsl=0.00;//实际到货数量
			for(HwxxDto hwxxDto : hwxxlist) {
				if(!"1".equals(htmxDto.getCghzbj()))
					sjdhsl=sjdhsl+Double.parseDouble(hwxxDto.getSjsl());
			}
			if(sjdhsl<Double.parseDouble(t_htmxDto.getSl()))
				t_htmxDtos.add(t_htmxDto);
		}
		mav.addObject("htmxDtos", t_htmxDtos);
		mav.addObject("htglDto", htglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 获取合同明细信息
	 */
	@RequestMapping("/contract/pagedataGetcontractinfo")
	@ResponseBody
	public Map<String,Object> pagedataGetcontractinfo(HtmxDto htmxDto){
		Map<String,Object> map = new HashMap<>();
		List<HwxxDto> hwxxDtos = hwxxService.getListByHtmxids(htmxDto);
		List<HwxxDto> t_htmxDtos = new ArrayList<>();
		// 过滤合同明细信息
		for(HwxxDto t_htmxDto : hwxxDtos) {
			t_htmxDto.setCghzbj(htmxDto.getCghzbj());
			List<HwxxDto> hwxxlist=hwxxService.getDtoByHtmxid(t_htmxDto);
			double sjdhsl=0.00;//实际到货数量
			for(HwxxDto hwxxDto : hwxxlist) {
				// if(!"cghz".equals(hwxxDto.getRklbdm()))
				sjdhsl=sjdhsl+Double.parseDouble(hwxxDto.getSjsl());
			}
			t_htmxDto.setYdhsl(Double.toString(sjdhsl));
			if(sjdhsl<Double.parseDouble(t_htmxDto.getHtsl()))
				t_htmxDtos.add(t_htmxDto);
		}
		map.put("hwxxDtos", t_htmxDtos);
		return map;
	}
	/**
	 * 获取合同明细信息
	 */
	@RequestMapping("/contract/pagedataGetHzInfo")
	@ResponseBody
	public Map<String,Object> pagedataGetHzInfo(HwxxDto hwxxDto){
		Map<String,Object> map = new HashMap<>();
		List<HwxxDto> hwxxDtos = hwxxService.getHwxxByHtmxidsWithHz(hwxxDto);
		map.put("hwxxDtos", hwxxDtos);
		return map;
	}

	/**
	 * 获取合同明细信息
	 */
	@RequestMapping("/contract/pagedataGetContractInfo")
	@ResponseBody
	public Map<String,Object> getPurchaseInfo(HtmxDto htmxDto){
		Map<String,Object> map = new HashMap<>();
		List<HwxxDto> hwxxDtos = hwxxService.getListByHtmxids(htmxDto);
		List<HwxxDto> t_htmxDtos = new ArrayList<>();
		// 过滤合同明细信息
		for(HwxxDto t_htmxDto : hwxxDtos) {
			t_htmxDto.setCghzbj(htmxDto.getCghzbj());
			List<HwxxDto> hwxxlist=hwxxService.getDtoByHtmxid(t_htmxDto);
			double sjdhsl=0.00;//实际到货数量
			for(HwxxDto hwxxDto : hwxxlist) {
				sjdhsl=sjdhsl+Double.parseDouble(hwxxDto.getSjsl());
			}
			if(sjdhsl<Double.parseDouble(t_htmxDto.getHtsl()))
				t_htmxDtos.add(t_htmxDto);
		}
		map.put("hwxxDtos", t_htmxDtos);
		return map;
	}

	/**
	 * 获取合同信息
	 */
	@RequestMapping("/contract/pagedataGetContractList")
	@ResponseBody
	public Map<String,Object> getContractList(HtglDto htglDto){
		Map<String,Object> map = new HashMap<>();
		List<HtglDto> htglDtos = htglService.getDtoList(htglDto);
		map.put("htglDtos", htglDtos);
		return map;
	}

	/**
	 * 合同明细查看 共通页面
	 */
	@RequestMapping(value = "/contract/viewCommonContract")
	public ModelAndView viewCommonContract(HtglDto htglDto) {
		ModelAndView mav = new ModelAndView("contract/contract/contract_viewCommon");
		// 获取合同信息
		HtglDto t_htglDto = htglService.getDtoById(htglDto.getHtid());
		mav.addObject("htglDto", t_htglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 *
	 */
	@RequestMapping("/contract/pagedataGetHtmxList")
	@ResponseBody
	public Map<String,Object> pagedataGetHtmxList(HtmxDto htmxDto){
		Map<String,Object> map=new HashMap<>();
		List<HtmxDto> list=htmxService.getListByHtid(htmxDto.getHtid());
		map.put("total", htmxDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 合同付款列表页面
	 */
	@RequestMapping("/pendingPayment/pageListPendingPayment")
	public ModelAndView pageListPendingPayment(){
		ModelAndView mav = new ModelAndView("contract/payment/pendingPayment_list");
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}

	/**
	 * 获取待付款列表
	 */
	@RequestMapping("/pendingPayment/pageGetListPendingPayment")
	@ResponseBody
	public Map<String,Object> pageGetListPendingPayment(HtfktxDto htfktxDto){
		Map<String,Object> map=new HashMap<>();
		List<HtfktxDto> list=htfktxService.getPagedDtoList(htfktxDto);
		map.put("total", htfktxDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 待付款界面
	 */
	@RequestMapping("/pendingPayment/payPendingPayment")
	@ResponseBody
	public ModelAndView payPendingPayment(HtfkqkDto htfkqkDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		user = commonService.getUserInfoById(user);
		//获取供应商信息
		HtfktxDto htfktxDto=new HtfktxDto();
		htfktxDto.setIds(htfkqkDto.getIds());
		List<HtfktxDto> htfktxDtos=htfktxService.getDtoList(htfktxDto);
		if (!CollectionUtils.isEmpty(htfktxDtos)) {
			htfktxDto=htfktxDtos.get(0);
			htfkqkDto.setZfdx(htfktxDto.getGysid());
			htfkqkDto.setZfdxmc(htfktxDto.getGysmc());
			htfkqkDto.setZffkhh(htfktxDto.getGyskhh());
			htfkqkDto.setZffyhzh(htfktxDto.getGysyyzh());
		}
		htfkqkDto.setSqr(user.getYhid());
		htfkqkDto.setSqrmc(user.getZsxm());
		htfkqkDto.setSqbm(user.getJgid());
		htfkqkDto.setSqbmmc(user.getJgmc());
		ModelAndView mav=new ModelAndView("contract/payment/pendingPayment_pay");
		List<JcsjDto> fkfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYWAY.getCode());
		List<JcsjDto> fkfs = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAYAPPLY_PAYER.getCode());
		List<JcsjDto> fygss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.PAY_BELONG.getCode());  //付款申请  费用归属
		List<JcsjDto> djcdfss = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.RECEIPTS_TRANSMIT_TYPE.getCode());  //付款申请  单据传达方式
		if (!CollectionUtils.isEmpty(djcdfss)){
			for (JcsjDto jcsjDto : djcdfss) {
				if ("1".equals(jcsjDto.getSfmr())){
					htfkqkDto.setDjcdfs(jcsjDto.getCsid());
				}
			}
		}
		//自动生成付款单号
		String fkdh=htfkqkService.generatePayDjh(htfkqkDto);
		htfkqkDto.setFkdh(fkdh);
		StringBuilder fktxids= new StringBuilder();
		if(!CollectionUtils.isEmpty(htfkqkDto.getIds())){
			for(String fktxid : htfkqkDto.getIds()){
				fktxids.append(",").append(fktxid);
			}
			fktxids = new StringBuilder(fktxids.substring(1));
		}

		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("htfkqkDto",htfkqkDto);
		mav.addObject("djcdfss",djcdfss);
		mav.addObject("auditType",AuditTypeEnum.AUDIT_CONTRACT_PAYMENT.getCode());
		mav.addObject("fkfss",fkfss);
		mav.addObject("fkfs",fkfs);
		mav.addObject("fygss",fygss);
		mav.addObject("fktxids", fktxids.toString());
		mav.addObject("htids",htfkqkDto.getHtids());
		mav.addObject("url","/contract/pendingPayment/paySavePendingPayment");
		return mav;
	}

	/**
	 * 获取付款界面明细数据
	 */
	@RequestMapping("/pendingPayment/pagedataGetNeedPayList")
	@ResponseBody
	public Map<String,Object> pagedataGetNeedPayList(HtfktxDto htfktxDto){
		Map<String,Object> map=new HashMap<>();
		String htids = htfktxDto.getHtids();
		String[] split = htids.split(",");
		String sqbm="";
		String sqbmmc="";
		if(!ArrayUtils.isEmpty(split)){
			String s = split[0];
			HtmxDto htmxDto=new HtmxDto();
			htmxDto.setHtid(s);
			List<HtmxDto> purchaseDetails = htmxService.getPurchaseDetails(htmxDto);
			if(!CollectionUtils.isEmpty(purchaseDetails)){
				for (HtmxDto htmx : purchaseDetails) {
					if(StringUtil.isNotBlank(htmx.getSqbmmc())) {
						sqbm=htmx.getSqbm();
						sqbmmc=htmx.getSqbmmc();
						break;
					}
				}
			}
		}
		List<HtfktxDto> list=htfktxService.getDtoList(htfktxDto);
		if(!CollectionUtils.isEmpty(list)){
			list.get(0).setYkbm(sqbm);
			list.get(0).setYkbmmc(sqbmmc);
		}
		map.put("total", htfktxDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 付款申请保存
	 */
	@RequestMapping("/pendingPayment/paySavePendingPayment")
	@ResponseBody
	public Map<String,Object> paySavePendingPayment(HtfkqkDto htfkqkDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user = getLoginInfo(request);
		htfkqkDto.setLrry(user.getYhid());
		//校验付款金额是否超出合同未付款金额
		Map<String,Object> checkresult=htfkqkService.checkMoney(htfkqkDto);
		if(checkresult.get("status")=="fail")
			return checkresult;
		boolean isSuccess=htfkqkService.insertFkqk(htfkqkDto);
		map.put("ywid",htfkqkDto.getHtfkid());
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr(): xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 删除合同付款提醒
	 */
	@RequestMapping("/pendingPayment/delPendingPayment")
	@ResponseBody
	public Map<String,Object> delPendingPayment(HtfktxDto htfktxDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		User user = getLoginInfo(request);
		htfktxDto.setScry(user.getYhid());
		boolean isSuccess=htfktxService.delPendingPayment(htfktxDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr(): xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 跳转合同选择列表
	 */
	@RequestMapping(value = "/contract/pagedataChooseContractList")
	public ModelAndView pagedataChooseContractList(HtglDto htglDto) {
		ModelAndView mav = new ModelAndView("contract/contract/contract_chooseForInvoice");
		mav.addObject("htglDto", htglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 合同选择列表
	 */
	@RequestMapping(value = "/contract/pagedataGetPageListContract")
	@ResponseBody
	public Map<String, Object> pagedataGetPageListContract(HtglDto htglDto) {
		List<HtglDto> t_List = htglService.getPageListContract(htglDto);
		Map<String, Object> map = new HashMap<>();
		map.put("total", htglDto.getTotalNumber());
		map.put("rows", t_List);
		return map;
	}

	/**
	 * 	获取合同单详情
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/pagedataGetContractChooseInfo")
	public Map<String, Object> getContractChooseInfo(HtmxDto htmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<HtmxDto> htmxDtos = new ArrayList<>();
		List<HtmxDto> list = htmxService.getListForInvoice(htmxDto);
		List<HtmxDto> mxlist=new ArrayList<>();
		if(!CollectionUtils.isEmpty(list)){
			HtmxDto htmxDto_t=list.get(0);
			List<FpmxDto> u8ids=new ArrayList<>();
			for(int i=0;i<list.size();i++){
				if(htmxDto_t.getHtmxid().equals(list.get(i).getHtmxid())){
					FpmxDto fpmxDto1=new FpmxDto();
					fpmxDto1.setRkid(list.get(i).getRkid());
					fpmxDto1.setDhid(list.get(i).getDhid());
					fpmxDto1.setU8rkdh(list.get(i).getU8rkdh());
					fpmxDto1.setLbcskz1(list.get(i).getLbcskz1());
					fpmxDto1.setSbysid(list.get(i).getSbysid());
					fpmxDto1.setSbysdh(list.get(i).getSbysdh());
					u8ids.add(fpmxDto1);
                }else{
					htmxDto_t.setFpmxDtos(u8ids);
					mxlist.add(htmxDto_t);
					htmxDto_t=list.get(i);
					u8ids=new ArrayList<>();
					FpmxDto fpmxDto1=new FpmxDto();
					fpmxDto1.setRkid(list.get(i).getRkid());
					fpmxDto1.setDhid(list.get(i).getDhid());
					fpmxDto1.setU8rkdh(list.get(i).getU8rkdh());
					fpmxDto1.setLbcskz1(list.get(i).getLbcskz1());
					fpmxDto1.setSbysid(list.get(i).getSbysid());
					fpmxDto1.setSbysdh(list.get(i).getSbysdh());
					u8ids.add(fpmxDto1);
                }
                if(i==list.size()-1){
                    htmxDto_t.setFpmxDtos(u8ids);
                    mxlist.add(htmxDto_t);
                }
            }
		}
		if(!CollectionUtils.isEmpty(mxlist)){
			for (HtmxDto value : mxlist) {
				List<FpmxDto> fpmxDtos = value.getFpmxDtos();
				if (!CollectionUtils.isEmpty(fpmxDtos)) {
					boolean flag = false;
					for (FpmxDto dto : fpmxDtos) {
						if (StringUtil.isNotBlank(dto.getU8rkdh())) {
							flag = true;
						}
					}
					if (!flag) {
						value.setWwhsl(value.getSl());
					}
				} else {
					value.setWwhsl(value.getSl());
				}
				BigDecimal sl = new BigDecimal(value.getSl());
				BigDecimal hsdj = new BigDecimal(value.getHsdj());
				value.setSl(String.valueOf(sl));
				value.setMxsl(String.valueOf(sl));
				value.setHjje(String.valueOf(sl.multiply(hsdj)));
				htmxDtos.add(value);
			}
		}
		map.put("htmxDtos", htmxDtos);
		return map;
	}

	/**
	 * 跳转合同详情列表
	 */
	@RequestMapping(value = "/contract/pagedataChooseContractInfoList")
	public ModelAndView pagedataChooseContractInfoList(HtmxDto htmxDto) {
		ModelAndView mav = new ModelAndView("contract/contract/contract_chooseForInvoiceInfo");
		List<HtmxDto> htmxDtos = new ArrayList<>();
		List<HtmxDto> list = htmxService.getListForInvoice(htmxDto);
		List<HtmxDto> mxlist=new ArrayList<>();
		if(!CollectionUtils.isEmpty(list)){
			HtmxDto htmxDto_t=list.get(0);
			List<FpmxDto> u8ids=new ArrayList<>();
			for(int i=0;i<list.size();i++){
				if(htmxDto_t.getHtmxid().equals(list.get(i).getHtmxid())){
					FpmxDto fpmxDto1=new FpmxDto();
					fpmxDto1.setRkid(list.get(i).getRkid());
					fpmxDto1.setDhid(list.get(i).getDhid());
					fpmxDto1.setU8rkdh(list.get(i).getU8rkdh());
					fpmxDto1.setLbcskz1(list.get(i).getLbcskz1());
					u8ids.add(fpmxDto1);
                }else{
					htmxDto_t.setFpmxDtos(u8ids);
					mxlist.add(htmxDto_t);
					htmxDto_t=list.get(i);
					u8ids=new ArrayList<>();
					FpmxDto fpmxDto1=new FpmxDto();
					fpmxDto1.setRkid(list.get(i).getRkid());
					fpmxDto1.setDhid(list.get(i).getDhid());
					fpmxDto1.setU8rkdh(list.get(i).getU8rkdh());
					fpmxDto1.setLbcskz1(list.get(i).getLbcskz1());
					u8ids.add(fpmxDto1);
                }
                if(i==list.size()-1){
                    htmxDto_t.setFpmxDtos(u8ids);
                    mxlist.add(htmxDto_t);
                }
            }
		}
		if(!CollectionUtils.isEmpty(mxlist)){
			for (HtmxDto value : mxlist) {
				List<FpmxDto> fpmxDtos = value.getFpmxDtos();
				if (!CollectionUtils.isEmpty(fpmxDtos)) {
					boolean flag = false;
					for (FpmxDto dto : fpmxDtos) {
						if (StringUtil.isNotBlank(dto.getU8rkdh())) {
							flag = true;
						}
					}
					if (!flag) {
						value.setWwhsl(value.getSl());
					}
				} else {
					value.setWwhsl(value.getSl());
				}
				Double sl = Double.parseDouble(value.getSl());
				Double hsdj = Double.parseDouble(value.getHsdj());
				value.setSl(String.valueOf(sl));
				value.setMxsl(String.valueOf(sl));
				value.setHjje(String.valueOf((sl) * hsdj));
				htmxDtos.add(value);
			}
		}
		mav.addObject("htmxDtos", htmxDtos);
		mav.addObject("htmxDto", htmxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 钉钉单条合同查看
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/minidataGetSignalContract")
	public Map<String, Object> minidataGetSignalContract(HtglDto htglDto) {
		Map<String, Object> map=new HashMap<>();
		htglDto=htglService.getDtoById(htglDto.getHtid());
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(htglDto.getHtid());
		List<FjcfbDto> fjcfbDtos=fjcfbService.getFjcfbDtoByYwid(fjcfbDto.getYwid());
		if (StringUtil.isNotBlank(htglDto.getHtid())) {
			List<HtmxDto> htmxDtos = htmxService.getListByHtidDingTalk(htglDto.getHtid());
			map.put("rows", htmxDtos);
		} else {
			map.put("rows", null);
		}
		map.put("fjcfbDtos",fjcfbDtos);
		map.put("htglDto",htglDto);
		return map;
	}

	/**
	 * 合同单统计
	 */
	@RequestMapping(value = "/contract/getCountStatistics")
	@ResponseBody
	public Map<String, Object> getCountStatistics(String year) {
		List<HtglDto> list = htglService.getCountStatistics(year);
		Map<String, Object> map = new HashMap<>();
		map.put("rows", list);
		return map;
	}
	/**
	 * 钉钉合同单统计
	 */
	@RequestMapping(value = "/contract/minidataGetCountStatistics")
	@ResponseBody
	public Map<String, Object> minidataGetCountStatistics(String year) {
		List<HtglDto> list = htglService.getCountStatistics(year);
		Map<String, Object> map = new HashMap<>();
		map.put("rows", list);
		return map;
	}
	/**
	 * 请购合同统计
	 */
	@RequestMapping("/contract/pageStatisticsPurchaseContract")
	public ModelAndView pageStatisticsPurchaseContract(HtglDto htglDto){
		ModelAndView mav = new ModelAndView("contract/contract/purchaseContract_stats");
		setDateByWeek(htglDto);
		mav.addObject("urlPrefix", urlPrefix);
		mav.addObject("htglDto", htglDto);
		mav.addObject("gysids",htglService.getSupplierID());
		return mav;
	}

	/**
     * 请购合同统计数据
	 */
	@RequestMapping("/contract/pagedataPurchaseContractStatsInfo")
	@ResponseBody
	public Map<String, Object> purchaseContractStatsInfo(HtglDto htglDto){
		Map<String,Object> map = new HashMap<>();
		setDateByWeek(htglDto);
		//请购审核用时
		Map<String,Object> purchaseAudit = htglService.getPurchaseAuditTimeByRq(htglDto);
		setGwAndPjys(purchaseAudit);
		map.put("purchaseAudit",purchaseAudit);
		//请购审核到合同提交时间
		Map<String,Object> purchaseAuditToContractSubmit = htglService.getPurchaseAuditToContractSubmit(htglDto);
		map.put("purchaseAuditToContractSubmit",purchaseAuditToContractSubmit);
		//合同订单个数
		List<Map<String,Object>> countOrders = htglService.getCountOrdersByRq(htglDto);
		map.put("countOrders",countOrders);
		//合同审核用时
		Map<String,Object> contractAudit = htglService.getContractAuditTimeByRq(htglDto);
		setGwAndPjysC(contractAudit);
		map.put("contractAudit",contractAudit);
		//合同付款审核用时
		Map<String,Object> contractPay = htglService.getContractPayAuditTimeByRq(htglDto);
		setGwAndPjysCF(contractPay);
		map.put("contractPay",contractPay);
		//按部门统计合同金额
		List<Map<String,Object>> contractAmount = htglService.getContractAmountByRq(htglDto);
		map.put("contractAmount",contractAmount);
		//按合同物料统计到货及时率
		List<Map<String,Object>> matterArrive=htglService.getMatterArrivePer(htglDto);
		map.put("matterArrive",matterArrive);
		//按合同类型统计到货及时率
		List<Map<String,Object>> ContractClassArrive=htglService.getContractClassArrivePer(htglDto);
		map.put("contractClassArrive",ContractClassArrive);
		//按负责人统计到货及时率
		List<Map<String,Object>> chargePerArrivePer=htglService.getChargePerArrivePer(htglDto);
		map.put("chargePerArrivePer",chargePerArrivePer);
		//按供应商统计到货及时率
		List<Map<String,Object>> supplierArrivePer=htglService.getSupplierArrivePer(htglDto);
		map.put("supplierArrivePer",supplierArrivePer);
		map.put("searchData", htglDto);
		return map;
	}
	/**
	 * 通过条件查询刷新页面
	 */
	@RequestMapping("/contract/pagedataGetPurchaseContractStatsByTj")
	@ResponseBody
	public Map<String, Object> pagedataGetPurchaseContractStatsByTj(HttpServletRequest req,HtglDto htglDto) {
		Map<String, Object> map = new HashMap<>();
		String method = req.getParameter("method");
		//判断method不能为空，为空即返回空
		if(StringUtil.isBlank(method)) {
			return map;
		}else if("queryQ".equals(method)) {
			//请购审核用时
			Map<String,Object> purchaseAudit = htglService.getPurchaseAuditTimeByRq(htglDto);
			setGwAndPjys(purchaseAudit);
			map.put("purchaseAudit", purchaseAudit);
		}else if("queryQc".equals(method)) {
			//请购审核到合同提交时间
			Map<String,Object> purchaseAuditToContractSubmit = htglService.getPurchaseAuditToContractSubmit(htglDto);
			map.put("purchaseAuditToContractSubmit",purchaseAuditToContractSubmit);
		}else if("queryCs".equals(method)) {
			//合同审核用时
			Map<String,Object> contractAudit = htglService.getContractAuditTimeByRq(htglDto);
			setGwAndPjysC(contractAudit);
			map.put("contractAudit", contractAudit);
		} else if("queryCf".equals(method)) {
			//合同付款审核用时
			Map<String,Object> contractPay = htglService.getContractPayAuditTimeByRq(htglDto);
			setGwAndPjysCF(contractPay);
			map.put("contractPay",contractPay);
		}else if("getCountOrdersByYear".equals(method)) {
			//订单个数按年查询
			setDateByYear(htglDto);
			List<Map<String,Object>> countOrders = htglService.getCountOrdersByRq(htglDto);
			map.put("countOrders", countOrders);
			htglDto.setTjsjstart(htglDto.getTjsjYstart());
			htglDto.setTjsjend(htglDto.getTjsjYend());
		}else if("getCountOrdersByMon".equals(method)) {
			//订单个数按月查询
			setDateByMonth(htglDto);
			List<Map<String,Object>> countOrders = htglService.getCountOrdersByRq(htglDto);
			map.put("countOrders", countOrders);
			htglDto.setTjsjstart(htglDto.getTjsjMstart());
			htglDto.setTjsjend(htglDto.getTjsjMend());
		}else if("getCountOrdersByWeek".equals(method)) {
			//订单个数按周查询
			setDateByWeek(htglDto);
			List<Map<String,Object>> countOrders = htglService.getCountOrdersByRq(htglDto);
			map.put("countOrders", countOrders);
		}else if("getCountOrdersByDay".equals(method)) {
			//订单个数按天查询
			setDateByDay(htglDto);
			List<Map<String,Object>> countOrders = htglService.getCountOrdersByRq(htglDto);
			map.put("countOrders", countOrders);
		}else if("getContractAmountByYear".equals(method)) {
			//按部门统计合同金额按年查询
			setDateByYear(htglDto);
			List<Map<String,Object>> contractAmount = htglService.getContractAmountByRq(htglDto);
			map.put("contractAmount",contractAmount);
			htglDto.setTjsjstart(htglDto.getTjsjYstart());
			htglDto.setTjsjend(htglDto.getTjsjYend());
		}else if("getContractAmountByMon".equals(method)) {
			//按部门统计合同金额按月查询
			setDateByMonth(htglDto);
			List<Map<String,Object>> contractAmount = htglService.getContractAmountByRq(htglDto);
			map.put("contractAmount",contractAmount);
			htglDto.setTjsjstart(htglDto.getTjsjMstart());
			htglDto.setTjsjend(htglDto.getTjsjMend());
		}else if("getContractAmountByWeek".equals(method)) {
			//按部门统计合同金额按周查询
			setDateByWeek(htglDto);
			List<Map<String,Object>> contractAmount = htglService.getContractAmountByRq(htglDto);
			map.put("contractAmount",contractAmount);
		}else if("getContractAmountByDay".equals(method)) {
			//按部门统计合同金额按天查询
			setDateByDay(htglDto);
			List<Map<String,Object>> contractAmount = htglService.getContractAmountByRq(htglDto);
			map.put("contractAmount",contractAmount);
		}else if("getMatterArrivePer".equals(method)) {
			//按合同物料统计到货及时率
			List<Map<String,Object>> matterArrive=htglService.getMatterArrivePer(htglDto);
			map.put("matterArrive",matterArrive);
		}else if("getContractClassArrivePer".equals(method)) {
			//按合同分了统计到货及时率
			List<Map<String,Object>> matterArrive=htglService.getContractClassArrivePer(htglDto);
			map.put("contractClassArrive",matterArrive);
		}else if("getChargePerArrivePer".equals(method)) {
			//按负责人统计到货及时率
			List<Map<String,Object>> chargePerArrivePer=htglService.getChargePerArrivePer(htglDto);
			map.put("chargePerArrivePer",chargePerArrivePer);
		}else if("getSupplierArrivePer".equals(method)) {
			//按供应商统计到货及时率
			List<Map<String,Object>> supplierArrivePer=htglService.getSupplierArrivePer(htglDto);
			map.put("supplierArrivePer",supplierArrivePer);
		}
		map.put("searchData", htglDto);
		return map;
	}

	/**
	 * 设置按年查询的日期
	 * length 长度。要为负数，代表向前推移几年
	 */
	private void setDateByYear(HtglDto htglDto) {
		SimpleDateFormat monthSdf=new SimpleDateFormat("yyyy");
		Calendar calendar=Calendar.getInstance();
		htglDto.setTjsjYstart(monthSdf.format(calendar.getTime()));
		calendar.add(Calendar.YEAR, 0);
		htglDto.setTjsjYend(monthSdf.format(calendar.getTime()));
	}

	/**
	 * 设置按月查询的日期
	 * length 长度。要为负数，代表向前推移几月
	 */
	private void setDateByMonth(HtglDto htglDto) {
		SimpleDateFormat monthSdf=new SimpleDateFormat("yyyy-MM");
		Calendar calendar=Calendar.getInstance();
		htglDto.setTjsjMstart(monthSdf.format(calendar.getTime()));
		calendar.add(Calendar.MONTH, 0);
		htglDto.setTjsjMend(monthSdf.format(calendar.getTime()));
	}

	/**
	 * 设置周的日期
	 */
	private void setDateByWeek(HtglDto htglDto) {
		SimpleDateFormat daySdf=new SimpleDateFormat("yyyy-MM-dd");
		int dayOfWeek = DateUtils.getWeek(new Date());
		if (dayOfWeek <= 2)
		{
			htglDto.setTjsjstart(daySdf.format(DateUtils.getDate(new Date(), -dayOfWeek - 7)));
			htglDto.setTjsjend(daySdf.format(DateUtils.getDate(new Date(), -dayOfWeek-1)));
		} else
		{
			htglDto.setTjsjstart(daySdf.format(DateUtils.getDate(new Date(), -dayOfWeek)));
			htglDto.setTjsjend(daySdf.format(DateUtils.getDate(new Date(), 6 - dayOfWeek)));
		}
	}

	/**
	 * 设置按天查询的日期
	 * length 长度。要为负数，代表向前推移几天
	 */
	private void setDateByDay(HtglDto htglDto) {
		SimpleDateFormat daySdf=new SimpleDateFormat("yyyy-MM-dd");
		htglDto.setTjsjstart(daySdf.format(DateUtils.getDate(new Date(), 0)));
		htglDto.setTjsjend(daySdf.format(new Date()));
	}
	/**
	 * 设置岗位用时和平均用时(请购审核)
	 */
	private void setGwAndPjys(Map<String,Object> map) {
		BigDecimal bmjl = new BigDecimal(map.get("bmjl").toString());
		BigDecimal cggl = new BigDecimal(map.get("cggl").toString());
		BigDecimal cgjl = new BigDecimal(map.get("cgjl").toString());
		BigDecimal gsld = new BigDecimal(map.get("gsld").toString());
		BigDecimal pjys = bmjl.add(cggl).add(cgjl).add(gsld).divide(new BigDecimal(4),2,RoundingMode.HALF_UP);
		map.put("pjys",pjys.toString());
		map.put("bmjl",bmjl.setScale(2,RoundingMode.HALF_UP).toString());
		map.put("cggl",cggl.setScale(2,RoundingMode.HALF_UP).toString());
		map.put("cgjl",cgjl.setScale(2,RoundingMode.HALF_UP).toString());
		map.put("gsld",gsld.setScale(2,RoundingMode.HALF_UP).toString());
	}
	/**
	 * 设置岗位用时和平均用时(合同审核)
	 */
	private void setGwAndPjysC(Map<String,Object> map) {
		BigDecimal cgjl = new BigDecimal(map.get("cgjl").toString());
		BigDecimal fwsp = new BigDecimal(map.get("fwsp").toString());
		BigDecimal gsld = new BigDecimal(map.get("gsld").toString());
		BigDecimal kj = new BigDecimal(map.get("kj").toString());
		BigDecimal pjys = cgjl.add(fwsp).add(gsld).add(kj).divide(new BigDecimal(4),2,RoundingMode.HALF_UP);
		map.put("pjys",pjys.toString());
		map.put("cgjl",cgjl.setScale(2,RoundingMode.HALF_UP).toString());
		map.put("fwsp",fwsp.setScale(2,RoundingMode.HALF_UP).toString());
		map.put("gsld",gsld.setScale(2,RoundingMode.HALF_UP).toString());
		map.put("kj",kj.setScale(2,RoundingMode.HALF_UP).toString());
	}
	/**
	 * 设置岗位用时和平均用时(合同付款)
	 */
	private void setGwAndPjysCF(Map<String,Object> mao) {
		BigDecimal cgjl = new BigDecimal(mao.get("cgjl").toString());
		BigDecimal cwqy = new BigDecimal(mao.get("cwqy").toString());
		BigDecimal cwqe = new BigDecimal(mao.get("cwqe").toString());
		BigDecimal gsld = new BigDecimal(mao.get("gsld").toString());
		BigDecimal cn = new BigDecimal(mao.get("cn").toString());
		BigDecimal pjys = cgjl.add(cwqy).add(cwqe).add(gsld).add(cn).divide(new BigDecimal(5),2, RoundingMode.HALF_UP);
		mao.put("pjys",pjys.toString());
		mao.put("cgjl",cgjl.setScale(2,RoundingMode.HALF_UP).toString());
		mao.put("cwqy",cwqy.setScale(2,RoundingMode.HALF_UP).toString());
		mao.put("cwqe",cwqe.setScale(2,RoundingMode.HALF_UP).toString());
		mao.put("gsld",gsld.setScale(2,RoundingMode.HALF_UP).toString());
		mao.put("cn",cn.setScale(2,RoundingMode.HALF_UP).toString());
	}
	/**
	 * 合同行关闭页面
	 */
	@RequestMapping(value = "/contract/opencloseContract")
	public ModelAndView opencloseContract(HtglDto htglDto) {
		ModelAndView mav = new ModelAndView("contract/contract/contractopenclose_list");
		// 查看合同明细
		HtmxDto htmxDto = new HtmxDto();
		htmxDto.setHtid(htglDto.getHtid());
		List<HtmxDto> htmxDtos = htmxService.getListByHtid(htmxDto.getHtid());
		mav.addObject("htmxDtos", htmxDtos);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 是否到货审核通过
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/pagedataSfDh")
	public Map<String,Object> pagedataSfDh(HtmxDto htmxDto){
		Map<String,Object> map=new HashMap<>();
		List<HtmxDto> htmxDtos = htmxService.getDhxx(htmxDto);
		boolean isSuccess = true;
		for (HtmxDto dto : htmxDtos) {
			if (StringUtil.isNotBlank(dto.getHwid())){
				isSuccess  = false;
			}
		}
		map.put("status",isSuccess ? "success":"fail");
		return map;
	}
	/**
	 * 开启合同行
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/pagedataOpenContract")
	public Map<String,Object> pagedataOpenContract(HtmxDto htmxDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		boolean isSuccess;
		try {
			//获取用户信息
			User user = getLoginInfo(request);
			htmxDto.setDkry(user.getYhid());
			htmxDto.setDkrymc(user.getZsxm());
			isSuccess = htmxService.openContract(htmxDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
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
	 * 关闭合同行
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/pagedataCloseContract")
	public Map<String,Object> pagedataCloseContract(HtmxDto htmxDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		boolean isSuccess;
		try {
			//获取用户信息
			User user = getLoginInfo(request);
			htmxDto.setGbry(user.getYhid());
			htmxDto.setGbrymc(user.getZsxm());
			isSuccess = htmxService.closeContract(htmxDto);
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
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
	 * 根据供应商查询合同
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/pagedataContractByGys")
	public Map<String,Object> pagedataContractByGys(HtglDto htglDto){
		Map<String,Object> map=new HashMap<>();
		List<HtglDto> htglDtos=htglService.getDtoListByGys(htglDto);
		map.put("rows",htglDtos);
		return map;
	}
	/**
	 * 根据供应商获取历史合同
	 */
	@RequestMapping("/contract/pagedataGetHisContract")
	@ResponseBody
	public Map<String, Object> pagedataGetHisContract(HtmxDto htmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<HtmxDto> list = new ArrayList<>();
		if(StringUtils.isNotBlank(htmxDto.getGysid())){
			list = htmxService.getPagedHisContract(htmxDto);
		}
		map.put("total", htmxDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}
	/**
	 * 获取合同附件
	 */
	@RequestMapping("/contract/pagedataGetDocuments")
	@ResponseBody
	public Map<String, Object> pagedataGetDocuments(HtglDto htglDto) {
		Map<String, Object> map = new HashMap<>();
		List<HtglDto> dtoList = htglService.getDtoList(htglDto);
		List<FjcfbDto> fjcfbDtos =new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			List<String> ids =new ArrayList<>();
			for(HtglDto dto:dtoList){
				if(StringUtil.isNotBlank(htglDto.getHtid())){
					if(!htglDto.getHtid().equals(dto.getHtid())){
						ids.add(dto.getHtid());
					}
				}else{
					ids.add(dto.getHtid());
				}

			}
			if(!CollectionUtils.isEmpty(ids)) {
				FjcfbDto fjcfbDto=new FjcfbDto();
				fjcfbDto.setYwids(ids);
				fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBCONTRACT.getCode());
				fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto);
			}

		}
		map.put("fjcfbDtos",fjcfbDtos);
		return map;
	}
	/**
	 * 框架合同明细数据
	 */
	@RequestMapping("/contract/pagedataFrameworkContract")
	@ResponseBody
	public Map<String, Object> pagedataFrameworkContract(HtmxDto htmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<HtmxDto> htmxDtos = new ArrayList<>();
		if (StringUtil.isNotBlank(htmxDto.getHtid())) {
			htmxDtos = htmxService.getListByHtid(htmxDto.getHtid());
			map.put("rows", htmxDtos);
		} else {
			map.put("rows", htmxDtos);
		}
		return map;
	}
	/**
	 * 操作明细
	 */
	@RequestMapping("/contract/pagedataUpdateScbj")
	@ResponseBody
	public Map<String, Object> pagedataUpdateScbj(HtmxDto htmxDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		htmxDto.setXgry(user.getYhid());
		boolean isSuccess = htmxService.updateScbj(htmxDto);
		map.put("status", isSuccess ? "success" : "fail");
		return map;
	}
	/**
	 * 操作明细
	 */
	@RequestMapping("/contract/pagedataGetHisMx")
	@ResponseBody
	public Map<String, Object> pagedataGetHisMx(HtmxDto htmxDto) {
		Map<String, Object> map = new HashMap<>();
		htmxDto =  htmxService.getHisContract(htmxDto);
		map.put("htmxDto", htmxDto);
		return map;
	}
	/**
	 * 通过供应商获取合同
	 */
	@RequestMapping("/contract/pagedataContractByGysWithWgq")
	@ResponseBody
	public Map<String, Object> getContractByGysWithWgq(HtglDto htglDto) {
		Map<String, Object> map = new HashMap<>();
		htglDto =  htglService.getContractByGysWithWgq(htglDto);
		map.put("htglDto", htglDto);
		return map;
	}
	/**
	 * 获取合同请购明细信息
	 */
	@ResponseBody
	@RequestMapping(value = "/contract/pagedataFrameworkContractDetail")
	public Map<String, Object> pagedataFrameworkContractDetail(HtmxDto htmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<HtmxDto> htmxDtos = new ArrayList<>();
		if (StringUtil.isNotBlank(htmxDto.getHtid())) {
			htmxDtos = htmxService.getListByHtid(htmxDto.getHtid());
			if (!"3".equals(htmxDto.getHtlx())){
				List<String> ids = htmxDtos.stream().filter(e -> StringUtil.isNotBlank(e.getKjhtmxid())).distinct().map(HtmxDto::getKjhtmxid).collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(ids)){
					HtmxDto htmxDto_kj = new HtmxDto();
					htmxDto_kj.setIds(ids);
					htmxDtos = htmxService.getFrameworkContractDetail(htmxDto_kj);
				}else {
					htmxDtos = new ArrayList<>();
				}
			}
		}
		map.put("rows", htmxDtos);
		return map;
	}

	/**
	 * @Description: 验证付款
	 * @param htglDto
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/8/27 15:10
	 */
	@RequestMapping(value = "/contract/pagedataQueryBcht")
	@ResponseBody
	public Map<String, Object> pagedataQueryBcht(HtglDto htglDto) {
		Map<String, Object> map = new HashMap<>();
		List<HtglDto> htglDtoList = htglService.queryByHtid(htglDto);
		String fkbj = "1";
		if(htglDtoList!=null && !htglDtoList.isEmpty()){
			fkbj = "0";
		}
		map.put("fkbj",fkbj);
		return map;
	}
	/**
	 * @Description: 获取补充合同明细信息
	 * @param htmxDto
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/8/28 15:14
	 */
	@RequestMapping("/contract/pagedataQueryBchtmx")
	@ResponseBody
	public Map<String,Object> pagedataQueryBchtmx(HtmxDto htmxDto){
		Map<String,Object> map=new HashMap<>();
		List<HtmxDto> htmxDtoList = htmxService.queryBchtmxDto(htmxDto);
		map.put("rows", htmxDtoList);
		return map;
	}

	@RequestMapping("/contract/pagedataQueryOverTime")
	@ResponseBody
	public Map<String, Object> pagedataQueryOverTime(HttpServletRequest request) {
		Map<String,Object> resultMap= new HashMap<>();
		Map<String,Object> map = htglService.queryOverTime("");
		String overString = "0个";
		String overdueString = "0个";
		if(!CollectionUtils.isEmpty(map)){
			List<HtglDto> overList = (List<HtglDto>) map.get("overList");
			List<HtglDto> overdueList = (List<HtglDto>) map.get("overdueList");
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
	@RequestMapping(value = "/contract/pagedataQueryOverTable")
	public ModelAndView pagedataQueryOverTable(HtglDto htglDto) {
		ModelAndView mav=new ModelAndView("contract/contract/overTimeView");
		mav.addObject("htglDto", htglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	@RequestMapping("/contract/pagedataGetOverTable")
	@ResponseBody
	public Map<String, Object> pagedataGetOverTable(HtglDto htglDto) {
		Map<String, Object> resultMap = new HashMap<>();
		List<HtglDto> htglDtos = htglService.queryOverTimeTable(htglDto);
		resultMap.put("rows", htglDtos);
		return resultMap;
	}

	@RequestMapping(value = "/contract/pagedataQueryOverTableMx")
	public ModelAndView pagedataQueryOverTableMx(HtglDto htglDto) {
		ModelAndView mav=new ModelAndView("contract/contract/overTimeMxView");
		mav.addObject("htglDto", htglDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	@RequestMapping("/contract/pagedataGetOverTableMx")
	@ResponseBody
	public Map<String, Object> pagedataGetOverTableMx(HtmxDto htmxDto) {
		Map<String, Object> resultMap = new HashMap<>();
		List<HtmxDto> htmxDtos = new ArrayList<>();
		if(StringUtil.isNotBlank(htmxDto.getHtid()) && StringUtil.isNotBlank(htmxDto.getQgid())){
			htmxDtos = htmxService.getByQgidAndHtid(htmxDto);
		}else if (StringUtil.isNotBlank(htmxDto.getQgid())){
			htmxDtos = htmxService.getByQgid(htmxDto);
		}
		resultMap.put("rows", htmxDtos);
		return resultMap;
	}

	@RequestMapping(value = "/contract/pageListOverTime")
	public ModelAndView pageListOverTime() {
		ModelAndView mav = new ModelAndView("contract/contract/contract_overTimeList");
		XtszDto xtszDto = xtszService.selectById("approval.ht");
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
		mav.addObject("shsjend", currentDate.toString());
		mav.addObject("year",currentDate.getYear());
		LocalDate startOfYear = currentDate.with(TemporalAdjusters.firstDayOfYear());
		mav.addObject("shsjstart", startOfYear.toString());
		mav.addObject("titleString", title);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	@RequestMapping(value = "/contract/pageGetListOverTime")
	@ResponseBody
	public Map<String, Object> pageGetListOverTime(HtglDto htglDto) {
		List<HtglDto> t_List = htglService.getHtOverTimeTable(htglDto);
		Map<String, Object> map = new HashMap<>();
		map.put("total", htglDto.getTotalNumber());
		map.put("rows", t_List);
		return map;
	}

	@RequestMapping(value = "/contract/pagedataDispose")
	public ModelAndView pagedataDispose(ShysDto shysDto) {
		ModelAndView mav=new ModelAndView("contract/contract/disposeView");
		mav.addObject("shysDto", shysDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	@RequestMapping(value ="/contract/pagedataSaveDispose")
	@ResponseBody
	public Map<String,Object> pagedataSaveDispose(ShysDto shysDto, HttpServletRequest request){
		User user = getLoginInfo(request);
		shysDto.setXgry(user.getYhid());
		Map<String, Object> map = new HashMap<>();
		boolean isSuccess;
		isSuccess = shysService.updateDispose(shysDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	@RequestMapping(value = "/contract/pagedataGetOverTimeStatistics")
	@ResponseBody
	public Map<String,Object> pagedataGetOverTimeStatistics(HttpServletRequest request) {
		String rqstart = request.getParameter("rqstart");
		String rqend = request.getParameter("rqend");
		Map<String,Object> map=new HashMap<>();
		map = htglService.getOverTimeTableMap(rqstart, rqend);
		return map;
	}
	@RequestMapping(value = "/contract/pagedataOverTimeView")
	public ModelAndView pagedataOverTimeView(HtmxDto htmxDto) {
		ModelAndView mav=new ModelAndView("contract/contract/contract_overTime");
		mav.addObject("htmxDto", htmxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	@RequestMapping("/contract/pagedataGetOverTimeView")
	@ResponseBody
	public Map<String, Object> pagedataGetOverTimeView(HtmxDto htmxDto) {
		Map<String, Object> resultMap = new HashMap<>();
		List<HtmxDto> htmxDtos = htmxService.getByQgidAndHtid(htmxDto);
		resultMap.put("rows", htmxDtos);
		return resultMap;
	}

	@RequestMapping(value = "/contract/pageListGrOverTime")
	public ModelAndView pageListGrOverTime() {
		ModelAndView mav=new ModelAndView("contract/contract/contract_overTime_gr");
		LocalDate currentDate = LocalDate.now();
		mav.addObject("startTime", currentDate.getYear());
		mav.addObject("urlPrefix", urlPrefix);
		List<String> ids = new ArrayList<>();
		ids.add(AuditTypeEnum.AUDIT_CONTRACT.getCode());
		ids.add(AuditTypeEnum.AUDIT_CONTRACT_OA.getCode());
		ids.add(AuditTypeEnum.AUDIT_REQUISITIONSTWO.getCode());
		ids.add(AuditTypeEnum.AUDIT_REQUISITIONS_SERVICE.getCode());
		ids.add(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
		ids.add(AuditTypeEnum.AUDIT_REQUISITIONS_DEVICE.getCode());
		XtshDto xtshDto = new XtshDto();
		xtshDto.setIds(ids);
		List<XtshDto> xtshDtoList = xtshService.getDtoList(xtshDto);
		mav.addObject("xtshDtoList", xtshDtoList);
		return mav;
	}
	@RequestMapping(value = "/contract/pagedataGetGrOverTime")
	@ResponseBody
	public Map<String,Object> pagedataGetGrOverTime(HtglDto htglDto) {
		return htglService.getGrOverTimeTableMap(htglDto);
	}
}


