package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.GoodsBackTypeEnums;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.dao.matridxsql.TransVouchsDao;
import com.matridx.igams.production.dao.post.IHtmxDao;
import com.matridx.igams.production.dao.post.IQgglDao;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.storehouse.dao.entities.CkglDto;
import com.matridx.igams.storehouse.dao.entities.CkmxDto;
import com.matridx.igams.storehouse.dao.entities.DhjyDto;
import com.matridx.igams.storehouse.dao.entities.DhjyModel;
import com.matridx.igams.storehouse.dao.entities.HwllmxDto;
import com.matridx.igams.storehouse.dao.entities.HwllxxDto;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.entities.LlglDto;
import com.matridx.igams.storehouse.dao.entities.LykcxxDto;
import com.matridx.igams.storehouse.dao.entities.LyrkxxDto;
import com.matridx.igams.storehouse.dao.post.IDhjyDao;
import com.matridx.igams.storehouse.dao.post.IHwllxxDao;
import com.matridx.igams.storehouse.service.svcinterface.ICkglService;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.storehouse.service.svcinterface.ICkmxService;
import com.matridx.igams.storehouse.service.svcinterface.ICkxxService;
import com.matridx.igams.storehouse.service.svcinterface.IDhjyService;
import com.matridx.igams.storehouse.service.svcinterface.IHwllmxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IJycglService;
import com.matridx.igams.storehouse.service.svcinterface.ILlglService;
import com.matridx.igams.storehouse.service.svcinterface.ILykcxxService;
import com.matridx.igams.storehouse.service.svcinterface.ILyrkxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DhjyServiceImpl extends BaseBasicServiceImpl<DhjyDto, DhjyModel, IDhjyDao> implements IDhjyService,IAuditService{
	@Autowired
	IShgcService shgcService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	ICommonService commonservice;
	@Autowired
	ICkhwxxService ckhwxxService;
	@Autowired
	private IFjcfbService fjcfbService;
	@Autowired
	private IHwxxService hwxxService;
	@Autowired
	TransVouchsDao transVouchsDao;
	@Autowired
	private IJycglService jycService;
	@Autowired
	private IDdxxglService ddxxglService;
	@Autowired
	private DingTalkUtil talkUtil;
	@Autowired
	private IQgmxService qgmxService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	ICommonService commonService;
	@Autowired
	ILlglService llglService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IHwllmxService hwllmxService;
	@Autowired
	IHwllxxDao hwllxxDao;

	@Autowired
	ICkxxService ckxxService;

	@Autowired
	IRdRecordService rdRecordService;

	@Autowired
	IQgglDao qgglDao;

	@Autowired
	ICkglService ckglService;

	@Autowired
	ICkmxService ckmxService;

	@Autowired
	IHtmxDao htmxDao;
	@Autowired
	ILykcxxService lykcxxService;
	@Autowired
	ILyrkxxService lyrkxxService;
	private final Logger log = LoggerFactory.getLogger(DhjyServiceImpl.class);

	/**
	 * 到货检验列表（查询审核状态）
	 */
	@Override
	public List<DhjyDto> getPagedDtoList(DhjyDto dhjyDto) {
		List<DhjyDto> list = dao.getPagedDtoList(dhjyDto);
		try {
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_REAGENT.getCode(), "zt", "dhjyid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_INSTRUMENT.getCode(), "zt", "dhjyid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_THORGH.getCode(), "zt", "dhjyid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_CONSUMABLE.getCode(), "zt", "dhjyid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return list;
	}

	/**
	 * 导出
	 *
	 */
	@Override
	public int getCountForSearchExp(DhjyDto dhjyDto, Map<String, Object> params) {
		return dao.getCountForSearchExp(dhjyDto);
	}

	/**
	 * 检验物料导出
	 *
	 */
	@Override
	public int getCountForGoodsSearchExp(DhjyDto dhjyDto, Map<String, Object> params) {
		return dao.getCountForGoodsSearchExp(dhjyDto);
	}

	/**
	 * 根据搜索条件获取导出信息
	 */
	public List<DhjyDto> getListForSearchExp(Map<String, Object> params) {
		DhjyDto dhjyDto = (DhjyDto) params.get("entryData");
		queryJoinFlagExport(params, dhjyDto);
		return dao.getListForSearchExp(dhjyDto);
	}

	/**
	 * 根据搜索条件获取导出检验物料信息
	 */
	public List<DhjyDto> getListForGoodsSearchExp(Map<String, Object> params) {
		DhjyDto dhjyDto = (DhjyDto) params.get("entryData");
		queryJoinFlagExport(params, dhjyDto);
		return dao.getListForGoodsSearchExp(dhjyDto);
	}
	/**
	 * 根据选择信息获取导出信息
	 */
	public List<DhjyDto> getListForSelectExp(Map<String, Object> params) {
		DhjyDto dhjyDto = (DhjyDto) params.get("entryData");
		queryJoinFlagExport(params, dhjyDto);
		return dao.getListForSelectExp(dhjyDto);
	}

	/**
	 * 根据选择信息获取导出检验物料信息
	 */
	public List<DhjyDto> getListForGoodsSelectExp(Map<String, Object> params) {
		DhjyDto dhjyDto = (DhjyDto) params.get("entryData");
		queryJoinFlagExport(params, dhjyDto);
		return dao.getListForGoodsSelectExp(dhjyDto);
	}

	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String, Object> params, DhjyDto dhjyDto) {
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuilder sqlParam = new StringBuilder();
		for (DcszDto dcszDto : choseList) {
			if (dcszDto == null || dcszDto.getDczd() == null)
				continue;

			sqlParam.append(",");
			if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		String sqlcs = sqlParam.toString();
		dhjyDto.setSqlParam(sqlcs);
	}
	/**
	 * 获取检验列表的流水号
	 */
	@Override
	public String getInspectionDh() {
		String jydh = dao.getInspectionDh();
		if(StringUtils.isNotBlank(jydh)) {
			DateTimeFormatter matter=DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDateTime now = LocalDateTime.now();
			return "JY-"+ matter.format(now)+"-"+jydh;
		}
		return null;
	}
	/**
	 * 待检验信息保存,提交
	 */

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Map<String, Object> addInspection(DhjyDto dhjydto, String hwxxJson,String delids,String xgry)  throws BusinessException{
		Map<String,Object> result = new HashMap<>();
		//是否留样出库修改时
		if (StringUtil.isNotBlank(dhjydto.getDhjyid())){
			HwxxDto hwxxDto_t = new HwxxDto();
			hwxxDto_t.setDhjyid(dhjydto.getDhjyid());
			List<HwxxDto> hwxxList = hwxxService.getDtoByDhjyidWithYck(hwxxDto_t);
			List<String> ids = new ArrayList<>();
			if (!CollectionUtils.isEmpty(hwxxList)) {
				List<String> allids = new ArrayList<>();
				for (HwxxDto hwxxDto : hwxxList) {
					if (StringUtil.isNotBlank(hwxxDto.getLlid())){
						if (!ids.contains(hwxxDto.getLlid())) {
							if ("80".equals(hwxxDto.getCkzt())){
								ids.add(hwxxDto.getLlid());
							}
						}
						if (!allids.contains(hwxxDto.getLlid())){
							allids.add(hwxxDto.getLlid());
						}
					}
				}
				if (!CollectionUtils.isEmpty(allids)){
					LlglDto llglDto_t = new LlglDto();
					llglDto_t.setIds(allids);
					List<LlglDto> list = llglService.getLllxByIds(llglDto_t);
					for (LlglDto llglDto : list) {
						llglDto.setXgry(xgry);
						if ("0".equals(dhjydto.getSflyck())){
							if ("4".equals(llglDto.getLllx())){
								llglDto.setLllx("1");
							}else if ("2".equals(llglDto.getLllx())){
								llglDto.setLllx("0");
							}
						}else if ("1".equals(dhjydto.getSflyck())){
							if ("0".equals(llglDto.getLllx())){
								llglDto.setLllx("0");
							}else if ("1".equals(llglDto.getLllx())){
								llglDto.setLllx("1");
							}
						}
						boolean isSuccess = llglService.updateLllx(llglDto);
						if (!isSuccess){
							throw new BusinessException("msg","修改领料类型失败！");
						}
					}
				}
			}
		}
		String dhlxdm="";
		result.put("success", true);
		result.put("message","保存成功");
		//判断是否生成了新的领料单
		boolean llbj=true;
		String jyid = dhjydto.getDhjyid();
		String dhjyid = StringUtil.isEmpty(dhjydto.getDhjyid())?StringUtil.generateUUID():dhjydto.getDhjyid();
		//1.提交时 校验检验单号 再新增数据
		DhjyDto dto = new DhjyDto();
		dto.setJydh(dhjydto.getJydh());
		DhjyDto selDto = dao.getDto(dto);
		if (selDto != null && !selDto.getDhjyid().equals(dhjyid)) {
			result.put("success", false);
			result.put("message", "检验单号已存在,请刷新单号");
			return result;
		}
		// 2.校验是否存在货物已被检验
		List<HwxxDto> hwxxlist = JSON.parseArray(hwxxJson,HwxxDto.class);
		if(!CollectionUtils.isEmpty(hwxxlist)) {
			dhlxdm=hwxxlist.get(0).getDhlxdm();
		}
		//存修改时取样量有变化的货物信息
		List<HwxxDto> hwxx_modqyl = new ArrayList<>();
		//判断检验id是否为空
		if(StringUtil.isNotBlank(jyid)) {
			//判断取样量是否增加
			HwxxDto hwxxDto_t = new HwxxDto();
			hwxxDto_t.setDhjyid(dhjydto.getDhjyid());
			List<HwxxDto> hwxx_t = hwxxService.getDtoList(hwxxDto_t);
			for (int i = hwxxlist.size()-1; i >= 0; i--) {
				for (HwxxDto hwxxDto : hwxx_t) {
					//获取取样量变化情况
					if (hwxxlist.get(i).getHwid().equals(hwxxDto.getHwid())) {
						double qyl_edit = Double.parseDouble(hwxxlist.get(i).getQyl()) - Double.parseDouble(hwxxDto.getQyl());
						//如果变大，再次生成一个新的领料单
						if (qyl_edit > 0.00) {
							hwxxlist.get(i).setAddqyl(Double.toString(qyl_edit));
							hwxx_modqyl.add(hwxxlist.get(i));
							llbj = false;
						}
						if (qyl_edit < 0.00) {
							throw new BusinessException("msg", "取样量不能减少！");
						}
						break;
					}
				}
			}
		}
		List<String> ids = new ArrayList<>();
		if(!CollectionUtils.isEmpty(hwxxlist)) {
			for (HwxxDto value : hwxxlist) {
				//判断借用量是否为null或0
				double jysl = Double.parseDouble(StringUtil.isNotBlank(value.getJysl()) ? value.getJysl() : "0");
				if (jysl > 0.00) {
					//不为0，借用标记为1,带借用
					value.setJybj("1");
				} else {
					//借用标记为0，无需借用
					value.setJybj("0");
				}
				value.setXgry(xgry);
				value.setDhjyid(dhjyid);
				ids.add(value.getHwid());
			}
			HwxxDto hwxxDto = new HwxxDto();
			hwxxDto.setIds(ids);
			List<HwxxDto> list = hwxxService.getHwxxByids(hwxxDto);
			for(HwxxDto hwxx :list) {
				if(StringUtil.isNotEmpty(hwxx.getDhjyid())) {
					DhjyDto dhjydto2 =  dao.getDtoById(hwxx.getDhjyid());
					if(!dhjydto.getJydh().equals(dhjydto2.getJydh())) {
						result.put("success", false);
						result.put("message",hwxx.getWlbm()+"已被检验,请确认。");
						return result;
					}
				}
			}
		}
		DateTimeFormatter matter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		dhjydto.setLrry(xgry);
		dhjydto.setLrsj(matter.format(now));
		dhjydto.setScbj("0");
		if ("1".equals(dhjydto.getBcbj())) {
			dhjydto.setZt("00");
		}
		int insertOrUpdateResult;
		if (StringUtil.isBlank(dhjydto.getSffqlld())){
			dhjydto.setSffqlld("0");
		}
		if(StringUtil.isEmpty(dhjydto.getDhjyid())) {
			dhjydto.setDhjyid(dhjyid);
			insertOrUpdateResult = dao.insert(dhjydto);
		}else {
			insertOrUpdateResult = dao.update(dhjydto);
		}
		if(insertOrUpdateResult==0) {
			result.put("success", false);
			result.put("message","保存失败");
			return result;
		}
		// 3.保存附件
		if(!CollectionUtils.isEmpty(hwxxlist)) {
			for (HwxxDto hwxxDto : hwxxlist) {
				if(!CollectionUtils.isEmpty(hwxxDto.getFjids())) {
					for (int j = 0; j < hwxxDto.getFjids().size(); j++) {
						if (StringUtil.isNotBlank(hwxxDto.getFjids().get(j))) {
							fjcfbService.save3RealFile(hwxxDto.getFjids().get(j), dhjyid, hwxxDto.getHwid());
						}
					}
				}
			}
		}
		//4.更新货物信息
		boolean updateFlag= hwxxService.updateHwxxDtos(hwxxlist);
		if(!updateFlag) {
			result.put("success", false);
			result.put("message","修改货物信息失败");
			return result;
		}
		//5.如果存在移除的数据，清空货物信息表里的到货检验ID,
		List<String> delIdList = JSON.parseArray(delids,String.class);
		if(!CollectionUtils.isEmpty(delIdList)) {
			HwxxDto hwxxDto = new HwxxDto();
			hwxxDto.setIds(delIdList);
			hwxxService.updateDhjyidEmpty(hwxxDto);
		}
		//6.保存时从检验车中删除信息
		ids.addAll(delIdList);
		jycService.deleteHw(xgry,ids);
		result.put("dhjyid", dhjyid);
		//自动生成领料申请单
		boolean isSuccess = (boolean) result.get("success");
		if(isSuccess) {
			if(StringUtil.isBlank(jyid) || !CollectionUtils.isEmpty(hwxx_modqyl)) {
				User user = new User();
				user.setYhid(xgry);
				user = commonService.getUserInfoById(user);
				LlglDto llglDto = new LlglDto();
				llglDto.setLrry(xgry);
				llglDto.setLlid(StringUtil.generateUUID());
				if (user != null) {
					llglDto.setSqry(xgry);
					llglDto.setSqbm(user.getJgid());
					SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
					Date date = new Date();
					llglDto.setSqrq(sdf.format(date));//申请时间
					//生产记录编号
					String jlbh = llglService.generateJlbh();
					llglDto.setJlbh(jlbh);
				}
				String lldh = llglService.generateDjh(llglDto); //生成领料单
				llglDto.setLldh(lldh);
				llglDto.setQybj("1");
				//生成出库类型
				Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(
						new BasicDataTypeEnum[] { BasicDataTypeEnum.DELIVERY_TYPE});
				List<JcsjDto> jcsjDtos = jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode());
				for (JcsjDto jcsjDto : jcsjDtos) {
					if("b".equals(jcsjDto.getCsdm())) {
						llglDto.setCklb(jcsjDto.getCsid());
					}
				}
				if("OA".equals(dhlxdm)){
					llglDto.setLllx("1");
				}else{
					llglDto.setLllx("0");
				}
				//生成仓库
				HwxxDto hwxxDto_ck =new HwxxDto();
				List<String> dhjyids = new ArrayList<>();
				dhjyids.add(dhjydto.getDhjyid());
				hwxxDto_ck.setDhjyids(dhjyids);
				List<HwxxDto> hwxxDtos_ck = hwxxService.getDtoByDhjyid(hwxxDto_ck);
				llglDto.setCkid(hwxxDtos_ck.get(0).getCkid());
				//遍历集合
				List<HwxxDto> hwxxlist_f;
				//判断是新增还是修改
				if(StringUtil.isBlank(jyid)) {
					hwxxlist_f = hwxxlist;
				}else {
					hwxxlist_f = hwxx_modqyl;
				}
				List<HwxxDto> hwxxDtos=new ArrayList<>();
				if(!CollectionUtils.isEmpty(hwxxlist_f)) {
					for (HwxxDto hwxxDto_t:hwxxlist_f){
						HwxxDto hwxxDto=new HwxxDto();
						hwxxDto.setQyl(hwxxDto_t.getQyl());
						hwxxDto.setAddqyl(hwxxDto_t.getAddqyl());
						hwxxDto.setHwid(hwxxDto_t.getHwid());
						hwxxDto.setZjthsl(hwxxDto_t.getZjthsl());
						hwxxDto.setBgdh(hwxxDto_t.getBgdh());
						hwxxDto.setDhjyid(hwxxDto_t.getDhjyid());
						hwxxDto.setJysl(hwxxDto_t.getJysl());
						hwxxDto.setJybj(hwxxDto_t.getJybj());
						hwxxDto.setWlid(hwxxDto_t.getWlid());
						hwxxDto.setXgry(hwxxDto_t.getXgry());
						if (StringUtil.isBlank(hwxxDto_t.getQyrq())){
							hwxxDto.setQyrq(hwxxDto_t.getQyrq());
						}
						if (StringUtil.isBlank(hwxxDto_t.getJysl())){
							hwxxDto.setJysl(hwxxDto_t.getJysl());
						}
						hwxxDtos.add(hwxxDto);
					}
				}
				List<HwxxDto> list = new ArrayList<>();
				if(!CollectionUtils.isEmpty(hwxxlist_f)) {
					for (HwxxDto hwxxDto : hwxxlist_f) {
						if (CollectionUtils.isEmpty(list)) {
							list.add(hwxxDto);
						}else {
							boolean flag = true;
							for (HwxxDto hwxxDto1 : list) {
								if (hwxxDto1.getWlid().equals(hwxxDto.getWlid())) {
									flag = false;
									if (StringUtil.isBlank(jyid)) {
										BigDecimal qyl = new BigDecimal(hwxxDto1.getQyl());
										BigDecimal qyl_t = new BigDecimal(hwxxDto.getQyl());
										hwxxDto1.setQyl(qyl.add(qyl_t).toString());
									} else {
										BigDecimal qyl = new BigDecimal(hwxxDto1.getAddqyl());
										BigDecimal qyl_t = new BigDecimal(hwxxDto.getAddqyl());
										hwxxDto1.setQyl(qyl.add(qyl_t).toString());
										hwxxDto1.setAddqyl(qyl.add(qyl_t).toString());
									}
								}
							}
							if (flag){
								list.add(hwxxDto);
							}
						}
					}
				}
				//生成货物领料信息
				List<HwllxxDto> hwllxxDtolist = new ArrayList<>();
				List<HwllmxDto> hwllmxDtolist = new ArrayList<>();

				for (HwxxDto hwxxDto : list) {
					if(StringUtil.isBlank(hwxxDto.getQyl())) {
						throw new BusinessException("msg","取样量不能为空！");
					}
					if(0<Double.parseDouble(hwxxDto.getQyl())) {
						HwllxxDto hwllxxDto = new HwllxxDto();
						hwllxxDto.setHwllid(StringUtil.generateUUID());
						hwllxxDto.setLrry(llglDto.getLrry());
						hwllxxDto.setLlry(llglDto.getSqry());
						hwllxxDto.setLlid(llglDto.getLlid());
						hwllxxDto.setZt(StatusEnum.CHECK_NO.getCode());
						hwllxxDto.setWlid(hwxxDto.getWlid());
						QgglDto qgglDto_q = new QgglDto();
						qgglDto_q.setWlid(hwxxDto.getWlid());
						List<QgglDto> qgglDtos = qgglDao.queryByWlid(qgglDto_q);
						if(!CollectionUtils.isEmpty(qgglDtos)) {
							for (QgglDto qgglDto : qgglDtos) {
								if(hwxxDto.getWlid().equals(qgglDto.getWlid())) {
									hwllxxDto.setXmdl(qgglDto.getXmdl());
									hwllxxDto.setXmbm(qgglDto.getXmbm());
									break;
								}
							}
						}
						//新增set取样量，修改set增加的取样量
						if(StringUtil.isBlank(jyid)) {
							hwllxxDto.setQlsl(hwxxDto.getQyl());
							hwllxxDto.setYxsl(hwxxDto.getQyl());
						}else {
							hwllxxDto.setQlsl(hwxxDto.getQyl());
							hwllxxDto.setYxsl(hwxxDto.getQyl());
						}
						hwllxxDtolist.add(hwllxxDto);
					}
				}
				for (HwxxDto hwxxDto : hwxxDtos) {
					for (HwllxxDto hwllxxDto:hwllxxDtolist){
						if (hwxxDto.getWlid().equals(hwllxxDto.getWlid())){
							hwxxDto.setHwllid(hwllxxDto.getHwllid());
						}
					}
				}
				for (HwxxDto hwxxDto : hwxxDtos) {
					if(StringUtil.isBlank(hwxxDto.getQyl())) {
						throw new BusinessException("msg","取样量不能为空！");
					}
					if(0<Double.parseDouble(hwxxDto.getQyl())) {
						HwllmxDto hwllmxDto = new HwllmxDto();
						//新增set取样量，修改set增加的取样量
						if(StringUtil.isBlank(jyid)) {
							hwllmxDto.setQlsl(hwxxDto.getQyl());
							hwllmxDto.setYxsl(hwxxDto.getQyl());
						}else {
							hwllmxDto.setQlsl(hwxxDto.getAddqyl());
							hwllmxDto.setYxsl(hwxxDto.getAddqyl());
						}
						hwllmxDto.setHwllid(hwxxDto.getHwllid());
						hwllmxDto.setLlid(llglDto.getLlid());
						hwllmxDto.setHwid(hwxxDto.getHwid());
						hwllmxDtolist.add(hwllmxDto);
					}
				}
				if(!CollectionUtils.isEmpty(hwllmxDtolist)) {
					llglDto.setBz(dhjydto.getBz());
					boolean result_ll = llglService.insertDto(llglDto);
					if(!result_ll) {
						throw new BusinessException("msg","自动生成领料单失败(领料)！");
					}
					boolean result_hwll = hwllxxDao.insertHwllxx(hwllxxDtolist);
					if(!result_hwll) {
						throw new BusinessException("msg","自动生成领料单失败(领料货物)！");
					}
					boolean result_llmx = hwllmxService.insertHwllmxs(hwllmxDtolist);
					if(!result_llmx) {
						throw new BusinessException("msg","自动生成领料单失败(领料明细)！");
					}
					result.put("ywid", llglDto.getLlid());
				}
				if(!llbj) {
					result.put("llbj","1");
				}
			}
		}

		//判断是否删除领料单
		if(StringUtil.isNotBlank(dhjydto.getSffqlld())) {
			if("1".equals(dhjydto.getSffqlld())) {
				//获取相关的领料单
				HwxxDto hw = new HwxxDto();
				hw.setDhjyid(dhjydto.getDhjyid());
				List<HwxxDto> hwList = hwxxService.queryByDhjyid(hw);
				StringBuilder llids = new StringBuilder();
				//StringBuilder glids = new StringBuilder();
				StringBuilder ckids = new StringBuilder();
				for (HwxxDto hw_t : hwList) {
					if(StringUtil.isNotBlank(hw_t.getLlid())) {
						llids.append(",").append(hw_t.getLlid());
						//glids.append(",").append(hw_t.getLlckglid());
						ckids.append(",").append(hw_t.getLlckid());
					}
				}
				//判断是否存在领料单，因为有可能已经被删掉
				if(StringUtil.isNotBlank(llids.toString())) {
					llids = new StringBuilder(llids.substring(1));
					LlglDto llglDto = new LlglDto();
					llglDto.setIds(llids.toString());
					llglDto.setScry(dhjydto.getXgry());
					llglDto.setScbj("2");
					//废弃领料单
					boolean result_t = llglService.delete(llglDto);
					if(!result_t) {
						throw new BusinessException("msg","废弃领料单失败！");
					}

					//废弃明细单
					HwllxxDto hwllxxDto = new HwllxxDto();
					hwllxxDto.setIds(llids.toString());
					hwllxxDto.setScbj("2");
					hwllxxDto.setScry(dhjydto.getXgry());
					result_t = hwllxxDao.deleteByllid(hwllxxDto) > 0;
					if(!result_t) {
						throw new BusinessException("msg","废弃领料信息失败！");
					}

					//废弃明细单
					HwllmxDto hwllmxDto = new HwllmxDto();
					hwllmxDto.setIds(llids.toString());
					hwllmxDto.setScbj("2");
					hwllmxDto.setScry(dhjydto.getXgry());
					result_t = hwllmxService.deleteByllid(hwllmxDto);
					if(!result_t) {
						throw new BusinessException("msg","废弃领料明细失败！");
					}

					//废弃出库单
					CkglDto ckglDto = new CkglDto();
					ckglDto.setIds(llids.toString());
					ckglDto.setScry(dhjydto.getXgry());
					ckglDto.setScbj("2");
					result_t = ckglService.deleteByLlid(ckglDto);
					if(!result_t) {
						throw new BusinessException("msg","废弃出库单失败！");
					}

					//废弃出库明细单
					CkmxDto ckmxDto = new CkmxDto();
					ckids = new StringBuilder(ckids.substring(1));
					ckmxDto.setIds(ckids.toString());
					ckmxDto.setScry(dhjydto.getXgry());
					ckmxDto.setScbj("2");
					result_t = ckmxService.deleteByCkid(ckmxDto);
					if(!result_t) {
						throw new BusinessException("msg","废弃出库明细单失败！");
					}
					//判断到货类型是否是OA
					// if(!"1".equals(hwList.get(0).getLllx())) {
					// glids = new StringBuilder(glids.substring(1));
					//RdRecordDto rdRecordDto = new RdRecordDto();
//						rdRecordDto.setIds(glids);
//						Map<String,Object> map = rdRecordService.redInkOffsetOfDeliveryOrder(rdRecordDto);
//						List<CkglDto> ckglDtos=(List<CkglDto>) map.get("ckglDtos");
//						List<CkmxDto> ckmxDtos=(List<CkmxDto>) map.get("ckmxDtos");
//						boolean result_ck = ckglService.updateCkglDtos(ckglDtos);
//						if(!result_ck) {
//							throw new BusinessException("msg","出库红冲单信息更新失败！");
//						}
//						result_ck = ckmxService.updateCkmxDtos(ckmxDtos);
//						if(!result_ck) {
//							throw new BusinessException("msg","出库红冲单明细信息更新失败！");
//						}
// 					}
				}
			}
		}
		return result;
	}

	/**
	 * 高级修改保存
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Map<String, Object> advancedModSaveInspection(DhjyDto dhjydto, String hwxxJson,String delids,String xgry)  throws BusinessException{
		DhjyDto dhjyDto_h = getDtoById(dhjydto.getDhjyid());
		if(!"80".equals(dhjyDto_h.getZt())) {
			return addInspection(dhjydto,hwxxJson,delids,xgry);
		}
		List<HwxxDto> hwxxlist = JSON.parseArray(hwxxJson,HwxxDto.class);
		HwxxDto hwxxDto = new HwxxDto();
		List<String> dhjyids= new ArrayList<>();
		dhjyids.add(dhjydto.getDhjyid());
		hwxxDto.setDhjyids(dhjyids);
		List<HwxxDto> hwxxDtos =  hwxxService.getInfoByDhjyid(hwxxDto);

		//判断取样量是否发生变化
		//判断取样量是否发生变化
		double sumSaveQYL = 0;
		double sumSaveZjthsl = 0;
		for (HwxxDto hwxxDto1:hwxxlist){
			sumSaveQYL += Double.parseDouble(hwxxDto1.getQyl());
			sumSaveZjthsl += Double.parseDouble(hwxxDto1.getZjthsl());
		}
		double sumQYL = 0;
		double sumZjthsl = 0;
		for (HwxxDto hwxxDto2 : hwxxDtos){
			for (HwxxDto hwxxDto1 : hwxxlist){
				if (hwxxDto1.getHwid().equals(hwxxDto2.getHwid())){
					hwxxDto2.setQyl(hwxxDto1.getQyl());
					hwxxDto2.setQyrq(hwxxDto1.getQyrq());
					hwxxDto2.setZjthsl(hwxxDto1.getZjthsl());
					hwxxDto2.setBgdh(hwxxDto1.getBgdh());
					hwxxService.updateByHwId(hwxxDto2);
					//保存附件信息
					if(!CollectionUtils.isEmpty(hwxxDto1.getFjids())) {
						for (int j = 0; j < hwxxDto1.getFjids().size(); j++) {
							if(StringUtil.isNotBlank(hwxxDto1.getFjids().get(j))) {
								fjcfbService.save3RealFile(hwxxDto1.getFjids().get(j),dhjydto.getDhjyid(), hwxxDto1.getHwid());
							}
						}
					}
				}
			}
		}
		for (HwxxDto hwxxDto2:hwxxDtos){
			sumQYL += Double.parseDouble(hwxxDto2.getQyl());
			sumZjthsl += Double.parseDouble(hwxxDto2.getZjthsl());
		}
		//判断取样量
		if (sumSaveQYL != sumQYL) {
			rdRecordService.pdQyl(hwxxlist,hwxxDtos,sumSaveQYL,sumQYL);
		}
		//判断退回数量
		if (sumSaveZjthsl != sumZjthsl) {
			rdRecordService.ggthsl(hwxxlist,hwxxDtos,sumSaveQYL,sumQYL);
		}
		dao.update(dhjydto);
		Map<String,Object> result = new HashMap<>();
		result.put("success", true);
		result.put("message","保存成功");
		return result;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam)
			throws BusinessException {
		DhjyDto dhjyDto = (DhjyDto) baseModel;
		String hwxxJson = dhjyDto.getHwxxlist();
		String delids = dhjyDto.getDelids();
		if(StringUtil.isBlank(dhjyDto.getDdbj())) {
			addInspection(dhjyDto,hwxxJson,delids,operator.getYhid());//保存修改的数据
		}
		return true;
	}
	/**
	 *
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		if (shgcList == null || shgcList.isEmpty()) {
			return true;
		}
		for (ShgcDto shgcDto : shgcList) {
			String dhjyid = shgcDto.getYwid();
			DhjyDto dhjyDto_t = dao.getDtoById(dhjyid);
			String jydh = dhjyDto_t.getJydh();
			DhjyDto dhjyDto = new DhjyDto();
			dhjyDto.setXgry(operator.getYhid());
			dhjyDto.setDhjyid(dhjyid);
			//判断领料申请是否通过，未通过不允许提交
			HwxxDto hwxxDto_Sel = new HwxxDto();
			hwxxDto_Sel.setDhjyid(dhjyid);
			List<HwxxDto> hwxxList_t = hwxxService.getDtoByDhjyid(hwxxDto_Sel);
			if(!CollectionUtils.isEmpty(hwxxList_t)) {
				throw new BusinessException("msg", "取样领料单" + hwxxList_t.get(0).getLldh() + "未出库！");
			}
			// 审核退回
			if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
//				String token = talkUtil.getToken();
				dhjyDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				//发送钉钉消息
				if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())) {
					try {
						int size = shgcDto.getSpgwcyDtos().size();
						for (int i = 0; i < size; i++) {
							if (StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())) {
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
										shgcDto.getSpgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00026"), xxglService.getMsg("ICOMM_SH00099", operator.getZsxm(), shgcDto.getShlbmc(), dhjyDto_t.getJydh(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
				//审核通过
			} else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
				//更新到货审核表的状态
				dhjyDto.setXgry(operator.getYhid());
				dhjyDto.setDhjyid(dhjyid);
				dhjyDto.setZt(StatusEnum.CHECK_PASS.getCode());
				dhjyDto.setZsxm(operator.getZsxm());
				//更新货物信息表的状态
				HwxxDto hwxxDto = new HwxxDto();
				List<String> dhjyids = new ArrayList<>();
				dhjyids.add(shgcDto.getYwid());
				hwxxDto.setDhjyids(dhjyids);
				List<HwxxDto> hwxxList = hwxxService.getListByDhjyid(hwxxDto);
				List<String> qgmxids = new ArrayList<>();
				// List<String> htmxids = new ArrayList<>();
				BigDecimal slAll = new BigDecimal("0");
				dhjyDto.setShsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
				// 检验工时计算规则：
				// 先判断检验单录入时间和检验单审核通过时间是不是同一天，如果是，工时=审核通过时间-检验单录入时间，单位小时。
				// 如果不是同一天，计算录入时间到下班时间多少小时，审核通过时间距离上班时间多少小时，录入时间距离审核通过时间有几天，
				// 工时= （录入时间距离审核通过时间的天数-1）*8+录入时间到下班时间的小时+审核通过时间距离上班时间的小时
				Date lrsjDate = DateUtils.parseDate(dhjyDto_t.getLrsj());
				String lrsj = DateUtils.getCustomFomratCurrentDate(lrsjDate, "yyyy-MM-dd");
				String nowStr = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd");
				BigDecimal lrsjBig = new BigDecimal(lrsjDate.getTime());
				BigDecimal rqBig = new BigDecimal(1000 * 60 * 60);
				long nowTime = new Date().getTime();
				String jygs;
				if (nowStr.equals(lrsj)) {
					jygs = String.valueOf(new BigDecimal(nowTime).subtract(lrsjBig).divide(rqBig, 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
				} else {
					LocalDate today = LocalDate.now();
					DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate lrsjL = LocalDate.parse(lrsj, df);
					long rqc = ChronoUnit.DAYS.between(lrsjL, today);
					BigDecimal xb_lr = new BigDecimal(DateUtils.parseDate(lrsj + " 17:30:00").getTime()).subtract(lrsjBig).divide(rqBig, 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
					BigDecimal sb_sh = new BigDecimal(nowTime).subtract(new BigDecimal(DateUtils.parseDate(nowStr + " 08:30:00").getTime())).divide(rqBig, 2, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
					jygs = String.valueOf(new BigDecimal((rqc - 1) * 8).add(xb_lr).add(sb_sh));
				}
				dhjyDto.setJygs(jygs);
				List<LyrkxxDto> lyrkxxDtos = new ArrayList<>();
				List<LykcxxDto> lykcxxDtos = new ArrayList<>();
				for (HwxxDto dto : hwxxList) {
					dto.setZt("03");
					BigDecimal dhsl = new BigDecimal(dto.getDhsl() == null ? "0" : dto.getDhsl());
					BigDecimal cythsl = new BigDecimal(dto.getCythsl() == null ? "0" : dto.getCythsl());
					BigDecimal zjthsl = new BigDecimal(dto.getZjthsl() == null ? "0" : dto.getZjthsl());
					BigDecimal qyl = new BigDecimal(dto.getQyl() == null ? "0" : dto.getQyl());
					//判断是否删除领料单
					if (StringUtil.isBlank(dto.getSffqlld())) {
						throw new BusinessException("msg", "请选择是否废弃领料单");
					}
					BigDecimal sl;
					if ("1".equals(dto.getSffqlld())) {
						//废弃领料单,sl=到货数量-初验退回数量-质检退回数量
						sl = dhsl.subtract(cythsl).subtract(zjthsl);
					} else {
						//不废弃领料单，sl=到货数量-初验退回数量-质检退回数量-取样量
						sl = dhsl.subtract(cythsl).subtract(zjthsl).subtract(qyl);
					}
					dto.setSl(sl.toString());
					slAll = slAll.add(sl);
					if (StringUtil.isNotEmpty(dto.getQgmxid())) {
						if (!qgmxids.contains(dto.getQgmxid())) {
							qgmxids.add(dto.getQgmxid());
						}
					}
					// if(StringUtil.isNotEmpty(hwxxList.get(i).getHtmxid())) {
					// 	if(!qgmxids.contains(hwxxList.get(i).getHtmxid())) {
					// 		htmxids.add(hwxxList.get(i).getHtmxid());
					// 	}
					// }
					if(StringUtil.isNotBlank(dto.getLysl())){
						BigDecimal lysl = new BigDecimal(dto.getLysl());
						if(lysl.compareTo(new BigDecimal("0"))!=0){
							LyrkxxDto lyrkxxDto = new LyrkxxDto();
							lyrkxxDto.setLyrkid(StringUtil.generateUUID());
							lyrkxxDto.setHwid(dto.getHwid());
							lyrkxxDto.setSl(dto.getLysl());
							lyrkxxDto.setLrry(dhjyDto_t.getJyfzr());
							lyrkxxDtos.add(lyrkxxDto);
						}
					}
				}
				if(!lyrkxxDtos.isEmpty()){
					boolean isSuccessy = lyrkxxService.insertList(lyrkxxDtos);
					if (!isSuccessy) {
						throw new BusinessException("msg", "留样入库信息新增失败！");
					}
				}
				HwxxDto hwxxDtoD = new HwxxDto();
				hwxxDtoD.setDhjyid(dhjyid);
				List<HwxxDto> hwxxDtos_d = hwxxService.orderByQueryByDhjyid(hwxxDtoD);
				if(hwxxDtos_d!=null && !hwxxDtos_d.isEmpty()){
					List<LykcxxDto> addList = new ArrayList<>();
					List<LykcxxDto> updateList = new ArrayList<>();
					for(HwxxDto hwxxDto1:hwxxDtos_d){
						if(StringUtil.isNotBlank(hwxxDto1.getZlysl())){
							BigDecimal zlysl = new BigDecimal(hwxxDto1.getZlysl());
							if(zlysl.compareTo(new BigDecimal("0"))!=0) {
								if (StringUtil.isNotBlank(hwxxDto1.getLykcid())) {
									LykcxxDto lykcxxDto = new LykcxxDto();
									lykcxxDto.setLykcid(hwxxDto1.getLykcid());
									lykcxxDto.setKcl(hwxxDto1.getZlysl());
									lykcxxDto.setKclbj("1");
									lykcxxDto.setXgry(dhjyDto_t.getJyfzr());
									updateList.add(lykcxxDto);
								} else {
									LykcxxDto lykcxxDto = new LykcxxDto();
									lykcxxDto.setLykcid(StringUtil.generateUUID());
									lykcxxDto.setWlid(hwxxDto1.getWlid());
									lykcxxDto.setScph(hwxxDto1.getScph());
									lykcxxDto.setScrq(hwxxDto1.getScrq());
									lykcxxDto.setYxq(hwxxDto1.getYxq());
									lykcxxDto.setKcl(hwxxDto1.getZlysl());
									lykcxxDto.setLrry(dhjyDto_t.getJyfzr());
									addList.add(lykcxxDto);
								}
							}
						}
					}
					if (!updateList.isEmpty()) {
						boolean isSuccess = lykcxxService.updateKclListByBj(updateList);
						if (!isSuccess) {
							throw new BusinessException("msg", "留样库存信息修改失败！");
						}
					}
					if (!addList.isEmpty()) {
						boolean isSuccess = lykcxxService.insertList(addList);
						if (!isSuccess) {
							throw new BusinessException("msg", "留样库存信息新增失败！");
						}
					}
				}
				//生成调拨单
//				if(!result) {
//					//更新关联id
//					Map<String, Object> map = rdRecordService.addU8DBData(dhjyDto_h);
//					DhjyDto dhjyDto_d = (DhjyDto) map.get("dhjyDto_d");
//					boolean result_jy = update(dhjyDto_d);
//					if(!result_jy) {
//						throw new BusinessException("msg","更新关联id失败！");
//					}
//
//					@SuppressWarnings("unchecked")
//					List<HwxxDto> hwxx_glid = (List<HwxxDto>) map.get("hwxx_glid");
//					//更新货物信息关联U8调拨信息
//					boolean result_glid = hwxxService.updateHwxxDtos(hwxx_glid);
//					if(!result_glid)
//						throw new BusinessException("msg","更新货物信息关联调拨id失败！");
//
//				}

				hwxxService.updateHwxxDtos(hwxxList);
				//更新请购明细里的到货数量 总和(到货数量 - 货物信息的初验退回数量 - 货物信息的质检退回数量）
				if(!CollectionUtils.isEmpty(qgmxids)) {
					QgmxDto qgmxDto = new QgmxDto();
					qgmxDto.setIds(qgmxids);
					qgmxService.updateQgmxDhsl(qgmxDto);
				}
//				//更新合同明细里的到货数量 总和(到货数量 - 货物信息的初验退回数量 - 货物信息的质检退回数量）
//				if(htmxids.size()>0) {
//					HtmxDto htmxDto = new HtmxDto();
//					htmxDto.setIds(htmxids);
//					htmxService.updateHtmxDhsl(htmxDto);
//				}
				// 判断退回数量，如果不为0，更新退货处理表。到货明细id检索到货退回表，没有的话新增一条数据状态为10
				//有的话更新数量
				hwxxService.createArrivalGoodsBack(hwxxList, GoodsBackTypeEnums.QUALITYCHECK_BACK.getCode(), operator.getYhid());
				// 发送钉钉与微信消息

				JcsjDto jcsjDto = new JcsjDto();
				jcsjDto.setJclb("DINGMESSAGETYPE");
				jcsjDto.setCsdm("GOODS_IN_STORAGE_CHECK");
				List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
				// Iterator<DdxxglDto> ddxxIterator = ddxxglDtolist.iterator();
//				String token = talkUtil.getToken();
// 				List<Map<String, String>> receivers = new ArrayList<Map<String, String>>();
// 				while (ddxxIterator.hasNext()) {
// 					Map<String, String> userMap = new HashMap<>();
// 					DdxxglDto ddxx = ddxxIterator.next();
// 					if (StringUtils.isNotBlank(ddxx.getDdid())) {
// 						userMap.put("ddid", ddxx.getDdid());
// 					}
// 					if (StringUtils.isNotBlank(ddxx.getWechatid())) {
// 						userMap.put("wxid", ddxx.getWechatid());
// 					}
// 					receivers.add(userMap);
// 				}
				String ICOMM_RK00001 = xxglService.getMsg("ICOMM_RK00001");
				String ICOMM_SH00053 = xxglService.getMsg("ICOMM_SH00053");
				if(!CollectionUtils.isEmpty(ddxxglDtolist)) {
					for (int i = 0; i < ddxxglDtolist.size(); i++) {
						for (DdxxglDto ddxxglDto : ddxxglDtolist) {
							if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
								//获取下一步审核人用户名
								//外网访问
//							String sign = URLEncoder.encode(commonservice.getSign(dhxxDto.getDhid()),"UTF-8");
								// 内网访问
								@SuppressWarnings("deprecation")
								String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/viewpage/inspectiongoods/inspectionGoodsView&type=1&dhjyid=" + dhjyDto_t.getDhjyid() + "&urlPrefix=" + urlPrefix,StandardCharsets.UTF_8);
								// 外网访问
								//String external = dingtalkurl + "page=/pages/index/viewpage/inspectiongoods/inspectionGoodsView?type=1&dhjyid="+dhjyDto.getDhjyid()+"&urlPrefix="+urlPrefix;
								List<BtnJsonList> btnJsonLists = new ArrayList<>();
								BtnJsonList btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("小程序");
								btnJsonList.setActionUrl(internalbtn);
								btnJsonLists.add(btnJsonList);
							/*btnJsonList = new BtnJsonList();
							btnJsonList.setTitle("外网访问");
							btnJsonList.setActionUrl(external);
							btnJsonLists.add(btnJsonList);*/
								talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),ddxxglDto.getYhid(),ddxxglDto.getYhm(), ddxxglDto.getDdid(), ICOMM_RK00001,
										StringUtil.replaceMsg(ICOMM_SH00053, operator.getZsxm(), dhjyDto_t.getJydh(),
												slAll.toString(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
										btnJsonLists, "1");
							}
						}
					}
					if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())) {
						int size = shgcDto.getSpgwcyDtos().size();
						for (int i = 0; i < size; i++) {
							if (StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())) {
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
										shgcDto.getSpgwcyDtos().get(i).getYhid(),
										xxglService.getMsg("ICOMM_SH00006"), xxglService.getMsg("ICOMM_SH00052",
												operator.getZsxm(), shgcDto.getShlbmc(),
												jydh, shgcDto.getShxx_shyj(),
												DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}
					//审核中
				}
			}else {
				dhjyDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
				String ICOMM_SH00017 = xxglService.getMsg("ICOMM_SH00017");
				//发送钉钉消息
				if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())){
					try {
						int size = shgcDto.getSpgwcyDtos().size();
						for (int i = 0; i < size; i++) {
							if (StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())) {
								//获取下一步审核人用户名
								//小程序访问
								//内网访问
								@SuppressWarnings("deprecation")
								String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/auditpage/inspectiongoods/inspectionGoodsView&type=1&ywzd=dhjyid&ywid=" + dhjyDto.getDhjyid() + "&auditType=" + shgcDto.getAuditType() + "&urlPrefix=" + urlPrefix,StandardCharsets.UTF_8);
								//外网访问
								List<BtnJsonList> btnJsonLists = new ArrayList<>();
								BtnJsonList btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("小程序");
								btnJsonList.setActionUrl(internalbtn);
								btnJsonLists.add(btnJsonList);
								talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00003, StringUtil.replaceMsg(ICOMM_SH00017,
										operator.getZsxm(), shgcDto.getShlbmc(), jydh, DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
							}
						}
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
				//发送钉钉消息--取消审核人员
				if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())) {
					try {
						int size = shgcDto.getNo_spgwcyDtos().size();
						for (int i = 0; i < size; i++) {
							if (StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())) {
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"), xxglService.getMsg("ICOMM_SH00005", operator.getZsxm(), shgcDto.getShlbmc(), jydh, DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
			}
			dao.update(dhjyDto);
		}
		return true;
	}
	/**
	 * 审核回调
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		if (shgcList == null || shgcList.isEmpty()) {
			return true;
		}
		if (auditParam.isCancelOpe()) {
			// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String dhjyid = shgcDto.getYwid();
				DhjyDto dhjyDto = new DhjyDto();
				dhjyDto.setXgry(operator.getYhid());
				dhjyDto.setDhjyid(dhjyid);
				dhjyDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				dao.update(dhjyDto);
			}
		} else {
			// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String dhjyid = shgcDto.getYwid();
				DhjyDto dhjyDto = new DhjyDto();
				dhjyDto.setXgry(operator.getYhid());
				dhjyDto.setDhjyid(dhjyid);
				dhjyDto.setZt(StatusEnum.CHECK_NO.getCode());
				dao.update(dhjyDto);
			}
		}
		return true;
	}
	/**
	 * 查询检验车是否有保存的记录
	 */
	@Override
	public DhjyDto getSaveRecord(String yhid, String lbcskz1) {

		DhjyDto dhjyDto = new DhjyDto();
		String dhjyid = dao.getSaveRecord(yhid,lbcskz1);
		if(StringUtil.isNotEmpty(dhjyid)) {
			dhjyDto = dao.getDtoById(dhjyid);
		}
		return dhjyDto;
	}
	/**
	 * 删除到货检验信息(批量)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, Object> delInspectionGoods(DhjyDto dhjydto) {
		Map<String,Object> result = new HashMap<>();
		HwxxDto dto = new HwxxDto();
		dto.setDhjyids(dhjydto.getIds());
		List<DhjyDto> dhjyDtos = dao.getCgHcxxByDhjyids(dto);
		for (DhjyDto dhjyDto : dhjyDtos) {
			if (StringUtil.isNotBlank(dhjyDto.getChhwid())){
				result.put("success", false);
				result.put("message",dhjyDto.getJydh()+"已生成采购红冲单"+dhjyDto.getDhdh()+"不允许删除!");
				return result;
			}
			if (StringUtil.isNotBlank(dhjyDto.getDrhwid())||StringUtil.isNotBlank(dhjyDto.getDchwid())){
				result.put("success", false);
				result.put("message","物料编码+"+dhjyDto.getWlbm()+"不合格数量已处理，不允许删除!");
				return result;
			}
		}

		//1.判断货物入库id是否存在，存在不允许删除
		List<HwxxDto> hwxxList = hwxxService.getDtoByDhjyid(dto);
		List<String> shzids = new ArrayList<>();//审核中的数据
		for(HwxxDto hwxx:hwxxList) {
			if(StringUtil.isNotBlank(hwxx.getLldh())) {
				result.put("success", false);
				result.put("message",hwxx.getLldh()+"货物已发起领料申请，不允许删除。");
				return result;
			}
			if(StringUtil.isNotEmpty(hwxx.getRkid())) {
				result.put("success", false);
				result.put("message",hwxx.getWlmc()+"货物已入库，不允许删除。");
				return result;
			}
			if(StatusEnum.CHECK_SUBMIT.getCode().equals(hwxx.getDhjyzt())) {
				shzids.add(hwxx.getDhjyid());
			}
		}
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setDhjyids(dhjydto.getIds());
		//修改htmx的dhsl,dhsl=dhsl+zjthsl(不合格数量)；
		List<HtmxDto> htmxDtos = new ArrayList<>();
		List<HwxxDto> hwxxDtos =  hwxxService.getInfoByDhjyid(hwxxDto);
		for (HwxxDto hwxxDto_t : hwxxDtos) {
			if (StringUtil.isNotBlank(hwxxDto_t.getZjthsl())&&StringUtil.isNotBlank(hwxxDto_t.getHtmxid())){
				HtmxDto htmxDto = new HtmxDto();
				htmxDto.setHtmxid(hwxxDto_t.getHtmxid());
				htmxDto.setDhslbj("1");
				htmxDto.setBydhsl(hwxxDto_t.getZjthsl());
				htmxDtos.add(htmxDto);
			}
		}
		if(!CollectionUtils.isEmpty(htmxDtos)) {
			int updateHtmxDtos = htmxDao.updateHtmxDtos(htmxDtos);
			if (updateHtmxDtos==0){
				result.put("success", false);
				result.put("message","删除失败");
				return result;
			}
		}
		result.put("success", false);
		result.put("message","删除失败");
		//修改货物的状态，改为02待检验
		dto.setZt("02");
		hwxxService.updateHwxxByDhjyids(dto);
		//到货检验中的数据标记为已删除
		int flag = dao.updatedelflag(dhjydto);
		if(flag==0) {
			return result;
		}
		//清除货物信息表里的到货检验id
		flag = hwxxService.updateDhjyidEmptyByDhjyids(dto);
		if(flag==0) {
			result.put("success", false);
			result.put("message","删除失败");
			return result;
		}
		//如是审核中的数据,删除对于的审核流程
		if(!CollectionUtils.isEmpty(shzids)){
			shgcService.deleteByYwids(shzids);
		}
		result.put("success", true);
		result.put("message","删除成功");
		return result;
	}
	/**
	 * 废弃
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, Object> abandonedInspectionGoods(DhjyDto dhjydto) {
		Map<String,Object> result = new HashMap<>();
		HwxxDto dto = new HwxxDto();
		dto.setDhjyids(dhjydto.getIds());
		//1.判断货物入库id是否存在，存在不允许删除
		List<HwxxDto> hwxxList = hwxxService.getListByDhjyid(dto);
		List<String> shzids = new ArrayList<>();//审核中的数据
		for(HwxxDto hwxx:hwxxList) {
			if(StringUtil.isNotEmpty(hwxx.getRkid())) {
				result.put("success", false);
				result.put("message",hwxx.getWlmc()+"货物已入库，不允许废弃。");
				return result;
			}
			if(StatusEnum.CHECK_UNPASS.getCode().equals(hwxx.getDhjyzt())) {
				shzids.add(hwxx.getDhjyid());
			}
		}
		result.put("success", false);
		result.put("message","废弃失败");
		//修改货物的状态，改为02待检验
		dto.setZt("02");
		hwxxService.updateHwxxByDhjyids(dto);
		//到货检验中的数据标记为已删除
		int flag = dao.updatedelflag(dhjydto);
		if(flag==0) {
			return result;
		}
		//清除货物信息表里的到货检验id
		flag = hwxxService.updateDhjyidEmptyByDhjyids(dto);
		if(flag==0) {
			result.put("success", false);
			result.put("message","废弃失败");
			return result;
		}
		//如是审核中的数据,删除对于的审核流程
		if(!CollectionUtils.isEmpty(shzids)) {
			shgcService.deleteByYwids(shzids);
		}
		result.put("success", true);
		result.put("message","废弃成功");
		return result;
	}

	@Override
	public List<DhjyDto> getPagedAuditDhjy(DhjyDto dhjyDto) {
		List<DhjyDto> dhjyList=dao.getPagedAuditDhjy(dhjyDto);
		if(CollectionUtils.isEmpty(dhjyList))
			return dhjyList;
		List<DhjyDto> sqList = dao.getAuditListDhjy(dhjyList);
		commonservice.setSqrxm(sqList);

		return sqList;
	}

	/**
	 * 根据到货检验ids获取到货检验信息（共通页面）
	 * @param dhjyDto
	 * @return
	 */
	public List<DhjyDto> getCommonDtoListByDhjyids(DhjyDto dhjyDto){
		return dao.getCommonDtoListByDhjyids(dhjyDto);
	}

	/**
	 * 	自动生成领料单
	 * @param dhjydto 到货检验主数据
	 * @param hwxxJson 操作的货物信息
	 * @return
	 */
	@Override
	public Map<String, Object> addLlxx(DhjyDto dhjydto, String hwxxJson) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) throws BusinessException {
		return null;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) throws BusinessException {
		Map<String, Object> map =new HashMap<>();
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>)param.get("ywids");
		DhjyDto dhjyDto = new DhjyDto();
		dhjyDto.setIds(ids);
		List<DhjyDto> dtoList = dao.getDtoList(dhjyDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(DhjyDto dto:dtoList){
				list.add(dto.getDhjyid());
			}
		}
		map.put("list",list);
		return map;
	}

	/**
	 * 检验列表审核状态统计
	 * @param year
	 * @return
	 */
	public List<DhjyDto> getAuditStatistics(String year){
		return dao.getAuditStatistics(year);
	}
	/**
	 * 获取统计年份
	 */
	public List<DhjyDto> getYearGroup(){
		return dao.getYearGroup();
	}

	@Override
	public List<DhjyDto> getPagedWlDtoList(DhjyDto dhjyDto) {
		List<DhjyDto> list = dao.getPagedWlDtoList(dhjyDto);
		try {
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_REAGENT.getCode(), "zt", "dhjyid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_INSTRUMENT.getCode(), "zt", "dhjyid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_THORGH.getCode(), "zt", "dhjyid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_CONSUMABLE.getCode(), "zt", "dhjyid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getInspectionResultStatistics(String year) {
		return dao.getInspectionResultStatistics(year);
	}

	@Override
	public List<Map<String,Object>> getInspectionCategoryProportion(String rqstart, String rqend) {
		return dao.getInspectionCategoryProportion(rqstart,rqend);
	}

	/**
	 * @Description: 背景信息上传
	 * @param hwxxDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2025/5/13 17:15
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean backdropSaveInspectionGoods(HwxxDto hwxxDto) {
		if(!CollectionUtils.isEmpty(hwxxDto.getFjids())) {
			for (int i = 0; i < hwxxDto.getFjids().size(); i++) {
				fjcfbService.save2RealFile(hwxxDto.getFjids().get(i), hwxxDto.getHwid());
			}
		}
		return true;
	}
}
