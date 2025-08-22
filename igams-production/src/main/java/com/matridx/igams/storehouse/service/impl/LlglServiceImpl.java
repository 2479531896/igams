package com.matridx.igams.storehouse.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.DeviceStateEnum;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.production.dao.entities.IA_SummaryDto;
import com.matridx.igams.production.dao.entities.SbyjllDto;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.dao.matridxsql.IA_SummaryDao;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.production.service.svcinterface.ISbyjllService;
import com.matridx.igams.production.service.svcinterface.ISbysService;
import com.matridx.igams.storehouse.dao.entities.CkglDto;
import com.matridx.igams.storehouse.dao.entities.CkhwxxDto;
import com.matridx.igams.storehouse.dao.entities.CkmxDto;
import com.matridx.igams.storehouse.dao.entities.HwllmxDto;
import com.matridx.igams.storehouse.dao.entities.HwllxxDto;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.entities.LlcglDto;
import com.matridx.igams.storehouse.dao.entities.LlglDto;
import com.matridx.igams.storehouse.dao.entities.LlglModel;
import com.matridx.igams.storehouse.dao.entities.LykcxxDto;
import com.matridx.igams.storehouse.dao.entities.LyrkxxDto;
import com.matridx.igams.storehouse.dao.post.IHwllxxDao;
import com.matridx.igams.storehouse.dao.post.ILlglDao;
import com.matridx.igams.storehouse.service.svcinterface.ICkglService;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.storehouse.service.svcinterface.ICkmxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwllmxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.ILlcglService;
import com.matridx.igams.storehouse.service.svcinterface.ILlglService;
import com.matridx.igams.storehouse.service.svcinterface.ILykcxxService;
import com.matridx.igams.storehouse.service.svcinterface.ILyrkxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LlglServiceImpl extends BaseBasicServiceImpl<LlglDto, LlglModel, ILlglDao> implements ILlglService,IAuditService{

	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IShxxService shxxService;
	@Autowired
	IDdfbsglService ddfbsglService;
	@Autowired
	IDdspglService ddspglService;
	@Autowired
	IHwllxxDao hwllxxDao;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.systemflg.systemname:}")
	private String systemname;
	@Value("${matridx.wechat.registerurl:}")
	private String registerurl;

	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;

	@Autowired
	ICommonService commonService;
	
	@Autowired
	ILlcglService llcglService;
	
	@Autowired
	ICkhwxxService ckhwxxService;
	
	@Autowired
	DingTalkUtil talkUtil;
	
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	ICommonService commonservice;
	
	@Autowired
	IDdxxglService ddxxglService;
	
	@Autowired
	IShlcService shlcService;
	
	@Autowired
	IHwxxService hwxxService;
	
	@Autowired
	IHwllmxService hwllmxService;
	
	@Autowired
	IShgcService shgcService;
	
	@Autowired
	IRdRecordService rdRecordService;
	
	@Autowired
	ICkglService ckglService;
	
	@Autowired
	ICkmxService ckmxService;
	
	@Autowired
	IJcsjService jcsjService;

	@Autowired
	ILyrkxxService lyrkxxService;
	@Autowired
	ILykcxxService lykcxxService;
	@Autowired
	ISbysService sbysService;
	@Autowired
	ISbyjllService sbyjllService;
	@Value("${matridx.rabbit.systemreceiveflg:}")
	private String systemreceiveflg;

	@Value("${sqlserver.matridxds.flag:}")
	private String matridxdsflag;
	@Autowired
	IA_SummaryDao iaSummaryDao;
	private final Logger log = LoggerFactory.getLogger(LlglServiceImpl.class);
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(LlglDto llglDto) {
		if(StringUtils.isBlank(llglDto.getLlid())) {
			llglDto.setLlid(StringUtil.generateUUID());
		}
		if(StringUtils.isBlank(llglDto.getQybj())) {
			llglDto.setQybj("0");
		}
		if(StringUtil.isBlank(llglDto.getLllx())) {
			llglDto.setLllx("0");
		}
		if (StringUtil.isNotBlank(llglDto.getSczlid())){
			llglDto.setLllx("5");
		}
		llglDto.setZt(StatusEnum.CHECK_NO.getCode());
		llglDto.setCkzt(StatusEnum.CHECK_NO.getCode());
		int result=dao.insert(llglDto);
		return result > 0;
	}
	
	/**
	 * 领料列表
	 * @param llglDto
	 * @return
	 */
	@Override
	public List<LlglDto> getPagedDtoReceiveMaterielList(LlglDto llglDto){
		List<LlglDto> list = dao.getPagedDtoReceiveMaterielList(llglDto);
		try {
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_APPLY.getCode(), "zt", "llid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode(), "zt", "llid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_DEVICE.getCode(), "zt", "llid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return list;
	}

	@Override
	public List<LlglDto> getPagedReceiveMateriel(LlglDto llglDto) {
		List<LlglDto> list = dao.getPagedDtoReceiveMaterielList(llglDto);
		try {
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_APPLY.getCode(), "zt", "llid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode(), "zt", "llid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_DEVICE.getCode(), "zt", "llid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return list;
	}

	/**
	 * 查看领料信息 基本信息
	 * @param llid
	 * @return
	 */
	@Override
	public LlglDto getDtoReceiveMaterielByLlid(String llid) {
		return dao.getDtoReceiveMaterielByLlid(llid);
	}
	@Override
	public List<LlglDto> getDtoReceiveMateriel(LlglDto llglDto){
		return dao.getDtoReceiveMateriel(llglDto);
	}
	@Override
	public List<LlglDto> getDtoReceiveMaterielWithPrint(LlglDto llglDto){
		return dao.getDtoReceiveMaterielWithPrint(llglDto);
	}
	/**
	 * 自动生成领料单号
	 * @param llglDto
	 * @return
	 */
	public String generateDjh(LlglDto llglDto) {
		// TODO Auto-generated method stub
		// 生成规则: LL-20201022-01 LL-年份日期-流水号 。
		String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		String prefix = "LL" + "-" + date + "-";
		// 查询流水号
		String serial = dao.getDjhSerial(prefix);
		return prefix + serial;
	}
	
	/**
	 * 自动生成出库单号
	 * @param llglDto
	 * @return
	 */
	public String generateCkdh(LlglDto llglDto) {
		// TODO Auto-generated method stub
		// 生成规则: LL-20201022-01 LL-年份日期-流水号 。
		String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		String prefix = "CK" + "-" + date + "-";
		// 查询流水号
		String serial = dao.getCkdhSerial(prefix);
		return prefix + serial;
	}
	
	/**
	 * 校验领料单号是否重复
	 * @param llglDto
	 * @return
	 */
	public boolean getDtoByLldh(LlglDto llglDto) {
		LlglDto llglDto_t = dao.getDtoByLldh(llglDto);
		return llglDto_t==null;
	}
	
	/**
	 * 保存领料信息
	 * @param llglDto
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addSavePicking(LlglDto llglDto) throws BusinessException {
		llglDto.setHtdh(llglDto.getHtid());
		boolean result = insertDto(llglDto);
		if(!result)
			return false;
		
		//文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(llglDto.getFjids())) {
			for (int i = 0; i < llglDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(llglDto.getFjids().get(i),llglDto.getLlid());
				if(!saveFile)
					throw new BusinessException("msg","领料附件保存失败!");
			}
		}
		//保存货物领料信息
		List<HwllxxDto> hwllxxlist= JSON.parseArray(llglDto.getLlmx_json(),HwllxxDto.class);
		if(!CollectionUtils.isEmpty(hwllxxlist)) {
			//存ids用于清空领料车
			StringBuilder ids = new StringBuilder();
			//存要更新的仓库货物信息集合
			List<CkhwxxDto> ckhwxxDtos = new ArrayList<>();
			//存储新增的货物领料明细信息
			List<HwllmxDto> hwllmxs = new ArrayList<>();
			//存修改的货物信息
			List<HwxxDto> hwxxDtos = new ArrayList<>();
			for(HwllxxDto hwllxxDto : hwllxxlist) {
				CkhwxxDto ckhwxxDto = new CkhwxxDto();
				ckhwxxDto.setCkhwid(hwllxxDto.getCkhwid());
				ckhwxxDto.setWlid(hwllxxDto.getWlid()); //存入物料id
				ckhwxxDto.setYds(hwllxxDto.getQlsl());	//存入预定数
				ckhwxxDto.setYdsbj("1");//预定数标记 1：加  0：减
				ckhwxxDtos.add(ckhwxxDto);			
				hwllxxDto.setLrry(llglDto.getLrry());
				hwllxxDto.setLlry(llglDto.getSqry());
				hwllxxDto.setLlid(llglDto.getLlid());
				hwllxxDto.setZt(StatusEnum.CHECK_NO.getCode());
				ids.append(",").append(hwllxxDto.getCkhwid());
				hwllxxDto.setHwllid(StringUtil.generateUUID());
				//判断货物领料明细是否为空
				List<HwllmxDto> hwllmxlist= JSON.parseArray(hwllxxDto.getHwllmx_json(),HwllmxDto.class);
				//如果存在领料明细，新增领料明细
				if(!CollectionUtils.isEmpty(hwllmxlist)) {
					for (int i = hwllmxlist.size()-1; i >= 0; i--) {
						if(hwllmxlist.get(i).getWlid().equals(hwllxxDto.getWlid())) {
							Double integerSl
									= Double.valueOf(StringUtil.isNotBlank(hwllmxlist.get(i).getQlsl())?hwllmxlist.get(i).getQlsl():"0");
							if(0.00==integerSl) {
								hwllmxlist.remove(i);
							}else {
								hwllmxlist.get(i).setHwllid(hwllxxDto.getHwllid());
								hwllmxlist.get(i).setLlid(llglDto.getLlid());
								hwllmxlist.get(i).setYxsl(hwllmxlist.get(i).getQlsl());
								//更新货物信息预定数
								HwxxDto hwxxDto = new HwxxDto();
								hwxxDto.setHwid(hwllmxlist.get(i).getHwid());
								double yds_hw = Double.parseDouble(StringUtil.isNotBlank(hwllmxlist.get(i).getQlsl())?hwllmxlist.get(i).getQlsl():"0");
								hwxxDto.setYdsbj("1");
								hwxxDto.setYds(Double.toString(yds_hw));
								hwxxDto.setXgry(llglDto.getLrry());
								hwxxDtos.add(hwxxDto);
							}						
						}
					}
					hwllmxs.addAll(hwllmxlist);
				}
			}
			boolean addSaveLlmx = hwllxxDao.insertHwllxx(hwllxxlist);
			if(!addSaveLlmx)
				throw new BusinessException("msg","新增货物领料信息失败！");
			//如果存在领料明细，新增领料明细
			if(!CollectionUtils.isEmpty(hwllmxs)) {
				boolean result_mx = hwllmxService.insertHwllmxs(hwllmxs);
				if(!result_mx) {
					throw new BusinessException("msg","更新领料明细失败！");
				}
				//更新货物信息预定数
				boolean result_hw = hwxxService.updateHwxxDtos(hwxxDtos);
				if(!result_hw) {
					throw new BusinessException("msg","更新货物信息预定数失败！");
				}				
			}
			//更新仓库货物信息表预定数
			boolean result_h = ckhwxxService.updateByCkhwid(ckhwxxDtos);
			if(!result_h)
				throw new BusinessException("msg","更新仓库信息预定数失败！");
			//清空领料车
			LlcglDto llcglDto = new LlcglDto();
			llcglDto.setRyid(llglDto.getLrry());
			ids = new StringBuilder(ids.substring(1));
			llcglDto.setIds(ids.toString());
			llcglService.delete(llcglDto);
		}
		return true;
	}
	
	/**
	 * 修改领料信息
	 * @param llglDto
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean modSavePicking(LlglDto llglDto) throws BusinessException {
		llglDto.setHtdh(llglDto.getHtid());
		boolean result = update(llglDto);
		if(!result)
			return false;
		
		//文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(llglDto.getFjids())) {
			for (int i = 0; i < llglDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(llglDto.getFjids().get(i),llglDto.getLlid());
				if(!saveFile)
					throw new BusinessException("msg","领料附件保存失败!");
			}
		}
		//判断是否为取样请领,如果是，不做修改
		if(!("1".equals(llglDto.getQybj()))) {
			//修改货物领料信息
			List<HwllxxDto> hwllxxlist= JSON.parseArray(llglDto.getLlmx_json(),HwllxxDto.class);
			if(!CollectionUtils.isEmpty(hwllxxlist)) {
				List<String> stringList = hwllxxlist.stream().map(HwllxxDto::getCkhwid)
						.collect(Collectors.toList());
				long count = stringList.stream().distinct().count();
				if (stringList.size() != count){
					return false;
				}
				//存要更新的货物信息
				List<HwxxDto> hwxxDtos = new ArrayList<>();
				//存储新增的货物领料明细信息
				List<HwllmxDto> hwllmxs_add = new ArrayList<>();
				//存储修改的货物领料明细信息
				List<HwllmxDto> hwllmxs_mod = new ArrayList<>();
				StringBuilder ids;
				StringBuilder hwmx_ids= new StringBuilder();
				HwllxxDto hwllxxDto = new HwllxxDto();
				hwllxxDto.setLlid(llglDto.getLlid());
				//获取修改前的货物领料信息
				List<HwllxxDto> hwllxxlist_fort = hwllxxDao.getDtoList(hwllxxDto);
				//存要更新的仓库货物信息
				List<CkhwxxDto> ckhwxxDtos = new ArrayList<>();
				//筛选出删除的货物领料信息
				for (int i = hwllxxlist_fort.size() - 1; i >= 0; i--) {
					boolean isDel = true;
					for (HwllxxDto dto : hwllxxlist) {
						if (StringUtil.isNotBlank(hwllxxlist_fort.get(i).getHwllid()) && StringUtil.isNotBlank(dto.getHwllid())) {
							if (hwllxxlist_fort.get(i).getHwllid().equals(dto.getHwllid())) {
								if (!"2".equals(llglDto.getTjbj())) {
									//hwllid相等做更新,存要更新的仓库货物信息
									CkhwxxDto ckhwxxDto = new CkhwxxDto();
									ckhwxxDto.setYdsbj("1");
									//预定数  修改后请领数量-修改前请领数量
									double yds;
									if ("1".equals(llglDto.getTjbj())) {
										yds = Double.parseDouble(dto.getQlsl());
									} else {
										yds = Double.parseDouble(dto.getQlsl()) - Double.parseDouble(hwllxxlist_fort.get(i).getQlsl());
									}
									ckhwxxDto.setYds(Double.toString(yds));
									ckhwxxDto.setWlid(hwllxxlist_fort.get(i).getWlid());
									ckhwxxDto.setCkhwid(hwllxxlist_fort.get(i).getCkhwid());
									ckhwxxDtos.add(ckhwxxDto);
								}
								isDel = false;
								break;
							}
						}
					}
					if (!isDel) {
						hwllxxlist_fort.remove(i);
					}
				}
				
				//处理删除的货物领料表
				ids = new StringBuilder();
				for (HwllxxDto hwllxxDto_del : hwllxxlist_fort) {
					ids.append(",").append(hwllxxDto_del.getHwllid());
					if(!("2".equals(llglDto.getTjbj()))) {
						CkhwxxDto ckhwxxDto = new CkhwxxDto();
						ckhwxxDto.setYds(hwllxxDto_del.getQlsl());
						ckhwxxDto.setCkhwid(hwllxxDto_del.getCkhwid());
						ckhwxxDto.setYdsbj("0"); //预定数标记 1：加 0：减
						ckhwxxDto.setWlid(hwllxxDto_del.getWlid());
						ckhwxxDtos.add(ckhwxxDto);	
					}									
				}
				
				//存新增的货物领料
				List<HwllxxDto> hwllxxDtos_add = new ArrayList<>();
				//存修改的货物领料
				List<HwllxxDto> hwllxxDtos_mod = new ArrayList<>();
				//存修改时新增物料的wlid,用于删除领料车里的数据
				StringBuilder ids_wl = new StringBuilder();
				//判断新增的货物里的hwllid,如果存在，修改，如果不存在，新增
				for (HwllxxDto hwllxxDto_m : hwllxxlist) {
					HwllxxDto hwllxxDto_z = new HwllxxDto();
					if(StringUtil.isNotBlank(hwllxxDto_m.getHwllid())){	
						hwllxxDto_z.setHwllid(hwllxxDto_m.getHwllid());
						hwllxxDto_z.setCkhwid(hwllxxDto_m.getCkhwid());
						hwllxxDto_z.setXmdl(hwllxxDto_m.getXmdl());
						hwllxxDto_z.setXmbm(hwllxxDto_m.getXmbm());
						hwllxxDto_z.setQlsl(hwllxxDto_m.getQlsl());
						hwllxxDto_z.setBz(hwllxxDto_m.getBz());
						hwllxxDto_z.setCplx(hwllxxDto_m.getCplx());
						hwllxxDto_z.setLlry(llglDto.getSqrq());
						hwllxxDto_z.setXgry(llglDto.getXgry());
						hwllxxDtos_mod.add(hwllxxDto_z);
						//判断货物领料明细是否为空
						List<HwllmxDto> hwllmxlist= JSON.parseArray(hwllxxDto_m.getHwllmx_json(),HwllmxDto.class);
						//如果存在领料明细，新增领料明细
						if(!CollectionUtils.isEmpty(hwllmxlist)) {
							for (int i = hwllmxlist.size()-1; i >= 0; i--) {
								if(hwllmxlist.get(i).getWlid().equals(hwllxxDto_m.getWlid())) {
									Double integerSl
											= Double.valueOf(StringUtil.isNotBlank(hwllmxlist.get(i).getQlsl())?hwllmxlist.get(i).getQlsl():"0");
									if(0.00==integerSl) {
										hwllmxlist.remove(i);
									}else {
										if(StringUtil.isNotBlank(hwllmxlist.get(i).getLlmxid())) {
											HwllmxDto hwllmx_t = new HwllmxDto();
											hwllmx_t.setHwllid(hwllxxDto_m.getHwllid());
											hwllmx_t.setLlid(llglDto.getLlid());
											hwllmx_t.setYxsl(hwllmxlist.get(i).getQlsl());
											hwllmx_t.setQlsl(hwllmxlist.get(i).getQlsl());
											hwllmx_t.setWlid(hwllmxlist.get(i).getWlid());
											hwllmx_t.setHwid(hwllmxlist.get(i).getHwid());
											hwllmxs_mod.add(hwllmx_t);
										}else {
											HwllmxDto hwllmx_t = new HwllmxDto();
											hwllmx_t.setHwllid(hwllxxDto_m.getHwllid());
											hwllmx_t.setLlid(llglDto.getLlid());
											hwllmx_t.setYxsl(hwllmxlist.get(i).getQlsl());
											hwllmx_t.setQlsl(hwllmxlist.get(i).getQlsl());
											hwllmx_t.setWlid(hwllmxlist.get(i).getWlid());
											hwllmx_t.setHwid(hwllmxlist.get(i).getHwid());
											hwllmxs_add.add(hwllmx_t);
										}
										//更新货物信息预定数
										if(!"2".equals(llglDto.getTjbj())) {
											HwxxDto hwxxDto = new HwxxDto();
											hwxxDto.setHwid(hwllmxlist.get(i).getHwid());
											double yds_hw;
											if("1".equals(llglDto.getTjbj())) {
												yds_hw = Double.parseDouble(StringUtil.isNotBlank(hwllmxlist.get(i).getQlsl())?hwllmxlist.get(i).getQlsl():"0");
											}else {
												yds_hw = Double.parseDouble(StringUtil.isNotBlank(hwllmxlist.get(i).getQlsl())?hwllmxlist.get(i).getQlsl():"0") - Double.parseDouble(StringUtil.isNotBlank(hwllmxlist.get(i).getQqls())?hwllmxlist.get(i).getQqls():"0");
											}		
											hwxxDto.setYdsbj("1");
											hwxxDto.setYds(Double.toString(yds_hw));
											hwxxDto.setXgry(llglDto.getLrry());
											hwxxDtos.add(hwxxDto);
										}										
									}						
								}
							}
						}	
					}else {
						ids_wl.append(",").append(hwllxxDto_m.getWlid());
						if(!"2".equals(llglDto.getTjbj())) {
							CkhwxxDto ckhwxxDto = new CkhwxxDto();
							ckhwxxDto.setWlid(hwllxxDto_m.getWlid());					
							//新增，预定数=预定数+请领数量
							ckhwxxDto.setYdsbj("1");
							ckhwxxDto.setYds(hwllxxDto_m.getQlsl());
							ckhwxxDto.setCkhwid(hwllxxDto_m.getCkhwid());
							ckhwxxDto.setCkqxlx(hwllxxDto_m.getCkqxlx());
							ckhwxxDtos.add(ckhwxxDto);
						}						
						hwllxxDto_z.setHwllid(StringUtil.generateUUID());
						hwllxxDto_z.setXmdl(hwllxxDto_m.getXmdl());
						hwllxxDto_z.setXmbm(hwllxxDto_m.getXmbm());
						hwllxxDto_z.setCkhwid(hwllxxDto_m.getCkhwid());
						hwllxxDto_z.setQlsl(hwllxxDto_m.getQlsl());
						hwllxxDto_z.setWlid(hwllxxDto_m.getWlid());
						hwllxxDto_z.setLlid(llglDto.getLlid());
						hwllxxDto_z.setLlry(llglDto.getSqrq());
						hwllxxDto_z.setLrry(llglDto.getXgry());
						hwllxxDto_z.setCplx(hwllxxDto_m.getCplx());
						hwllxxDto_z.setZt(StatusEnum.CHECK_NO.getCode());
						hwllxxDtos_add.add(hwllxxDto_z);
						//判断货物领料明细是否为空
						List<HwllmxDto> hwllmxlist= JSON.parseArray(hwllxxDto_m.getHwllmx_json(),HwllmxDto.class);
						//如果存在领料明细，新增领料明细
						if(!CollectionUtils.isEmpty(hwllmxlist)) {
							for (int i = hwllmxlist.size()-1; i >= 0; i--) {
								if(hwllmxlist.get(i).getWlid().equals(hwllxxDto_m.getWlid())) {
									Double integerSl
											= Double.valueOf(StringUtil.isNotBlank(hwllmxlist.get(i).getQlsl())?hwllmxlist.get(i).getQlsl():"0");
									if(0.00==integerSl) {
										hwllmxlist.remove(i);
									}else {
										hwllmxlist.get(i).setHwllid(hwllxxDto_z.getHwllid());
										hwllmxlist.get(i).setLlid(llglDto.getLlid());
										hwllmxlist.get(i).setYxsl(hwllmxlist.get(i).getQlsl());
										//更新货物信息预定数
										if(!"2".equals(llglDto.getTjbj())) {
											HwxxDto hwxxDto = new HwxxDto();
											hwxxDto.setHwid(hwllmxlist.get(i).getHwid());
											double yds_hw = Double.parseDouble(StringUtil.isNotBlank(hwllmxlist.get(i).getQlsl())?hwllmxlist.get(i).getQlsl():"0");
											hwxxDto.setYdsbj("1");
											hwxxDto.setYds(Double.toString(yds_hw));
											hwxxDto.setXgry(llglDto.getLrry());
											hwxxDtos.add(hwxxDto);
										}										
									}						
								}
							}
							hwllmxs_add.addAll(hwllmxlist);
						}
					}
				}
				
				//更新仓库货物信息预定数
				if(!CollectionUtils.isEmpty(ckhwxxDtos)) {
					boolean result_h = ckhwxxService.updateByCkhwid(ckhwxxDtos);
					if(!result_h)
						throw new BusinessException("msg","更新仓库信息预定数失败！");	
				}
					
				//判断是否删除货物
				if(StringUtil.isNotBlank(ids.toString())) {
					//删除删除的货物领料信息
					ids = new StringBuilder(ids.substring(1));
					HwllxxDto hwllxxDto_d = new HwllxxDto();
					hwllxxDto_d.setIds(ids.toString());
					hwllxxDto_d.setScry(llglDto.getXgry());
					boolean result_d = hwllxxDao.delete(hwllxxDto_d) > 0;
					if(!result_d)
						throw new BusinessException("msg","删除货物领料信息失败！");
					//删除货物领料明细
					HwllmxDto hwllmx = new HwllmxDto();
					hwllmx.setIds(ids.toString());
					List<HwllmxDto> hwllmxDtos = hwllmxService.queryByHwllid(hwllmx);
					if(!CollectionUtils.isEmpty(hwllmxDtos)) {						
						//删除货物领料明细,修改货物信息预定数
						for (HwllmxDto hwllmxDto : hwllmxDtos) {
							hwmx_ids.append(",").append(hwllmxDto.getLlmxid());
							if(!"2".equals(llglDto.getTjbj())) { //判断提交标记是否存在，不存在说明是审核修改，所以可以修改hwxx预定数，如果不存在，说明是重新提交，由于未提交状态未操作预定数，所以不需要修改hwxx预定数。
								HwxxDto hwxx = new HwxxDto();
								hwxx.setYdsbj("0");
								hwxx.setYds(hwllmxDto.getYxsl());
								hwxx.setXgry(llglDto.getXgry());
								hwxx.setHwid(hwllmxDto.getHwid());
								hwxxDtos.add(hwxx);
							}						
						}
					}
				}
				if(!CollectionUtils.isEmpty(hwllxxDtos_mod)) {
					//更新之前的货物领料信息
					boolean result_hl = hwllxxDao.updateHwllxxDtos(hwllxxDtos_mod) > 0;
					if(!result_hl)
						throw new BusinessException("msg","更新货物领料信息失败！");
				}
				if(!CollectionUtils.isEmpty(hwllxxDtos_add)) {
					//新增新添加的货物领料信息
					boolean addSaveLlmx = hwllxxDao.insertHwllxx(hwllxxDtos_add);
					if(!addSaveLlmx)
						throw new BusinessException("msg","新增货物领料信息失败！");
				}
				if(!CollectionUtils.isEmpty(hwxxDtos)) {
					//更新货物信息预定数
					boolean result_hw = hwxxService.updateHwxxDtos(hwxxDtos);
					if(!result_hw) {
						throw new BusinessException("msg","更新货物信息预定数失败！");
					}
				}
				if(StringUtil.isNotBlank(hwmx_ids.toString())) {
					HwllmxDto hwllmxDto = new HwllmxDto();
					hwmx_ids = new StringBuilder(hwmx_ids.substring(1));
					hwllmxDto.setIds(hwmx_ids.toString());
					boolean result_mx = hwllmxService.deleteByHwllid(hwllmxDto);
					if(!result_mx) {
						throw new BusinessException("msg","删除货物领料明细失败！");
					}
				}
				if(!CollectionUtils.isEmpty(hwllmxs_add)) {
					boolean result_mx_add = hwllmxService.insertHwllmxs(hwllmxs_add);
					if(!result_mx_add) {
						throw new BusinessException("msg","新增领料明细失败！");
					}
				}
				if(!CollectionUtils.isEmpty(hwllmxs_mod)) {
					boolean result_mx_mod = hwllmxService.updateHwllmxs(hwllmxs_mod);
					if(!result_mx_mod) {
						throw new BusinessException("msg","更新领料明细失败！");
					}
				}
				//清空领料车数据
				if(StringUtil.isNotBlank(ids_wl.toString())) {
					ids_wl = new StringBuilder(ids_wl.substring(1));
					LlcglDto llcglDto = new LlcglDto();
					llcglDto.setRyid(llglDto.getXgry());
					llcglDto.setIds(ids_wl.toString());
					llcglService.delete(llcglDto);
				}
			}
		}else {
			List<HwllxxDto> hwllxxlist= JSON.parseArray(llglDto.getLlmx_json(),HwllxxDto.class);
			if(!CollectionUtils.isEmpty(hwllxxlist)) {
				for (HwllxxDto hwllxxDto : hwllxxlist) {
					hwllxxDto.setXgry(llglDto.getXgry());
				}
				boolean result_hwll = hwllxxDao.updateHwllxxDtos(hwllxxlist) > 0;
				if(!result_hwll) {
					throw new BusinessException("msg","更新货物领料信息失败！");
				}
			}
		}
		return true;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean AdvancedmodSavePicking(LlglDto llglDto) throws BusinessException {
		
		LlglDto llglDto_h = dao.getDtoByllId(llglDto.getLlid());
		if(!"80".equals(llglDto_h.getZt())) {
			return modSavePicking(llglDto);
		}else{
			rdRecordService.updateLlglUpdate(llglDto,llglDto_h);
		}
		update(llglDto);
		//文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(llglDto.getFjids())) {
			for (int i = 0; i < llglDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(llglDto.getFjids().get(i),llglDto.getLlid());
				if(!saveFile)
					throw new BusinessException("msg","领料附件保存失败!");
			}
		}
		List<HwllxxDto> hwllxxlist = JSON.parseArray(llglDto.getLlmx_json(),HwllxxDto.class);//updateHwllxxDtos
		for (HwllxxDto hwllxxDto:hwllxxlist) {
			hwllxxDto.setXgry(llglDto.getXgry());
		}
		hwllxxDao.updateHwllxxDtos(hwllxxlist);
		return true;
	}
//	/**
//	 * 获取领料申请审核列表数据
//	 * @param llglDto
//	 * @return
//	 */
//	@Override
//	public List<LlglDto> getPagedDtoMaterialAudit(LlglDto llglDto) {
//		// TODO Auto-generated method stub
//		return dao.getPagedDtoMaterialAudit(llglDto);
//	}

	/**
	 * 获取领料申请
	 */
	@Override
	public List<LlglDto> getPagedAuditLlgl(LlglDto llglDto) {
		// 获取人员ID和履历号
		List<LlglDto> t_sbyzList = dao.getPagedAuditLlgl(llglDto);
		if(CollectionUtils.isEmpty(t_sbyzList))
			return t_sbyzList;

		List<LlglDto> sqList = dao.getAuditListLlgl(t_sbyzList);

		commonservice.setSqrxm(sqList);

		return sqList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam)
			throws BusinessException {
		LlglDto llglDto = (LlglDto) baseModel;
		llglDto.setXgry(operator.getYhid());
		boolean isSuccess = true;
		if (StringUtil.isBlank(llglDto.getDdbj())){
			if("1".equals(llglDto.getXsbj())) {
				isSuccess = modSavePicking(llglDto);
			}else {
				isSuccess = seniormodSavePicking(llglDto);
			}
		}
		return isSuccess;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		if (shgcList == null || shgcList.isEmpty()) {
			return true;
		}
		String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
		String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
		String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
		String ICOMM_SH00065 = xxglService.getMsg("ICOMM_SH00065");
		for (ShgcDto shgcDto : shgcList) {
			LlglDto llglDto = new LlglDto();
			llglDto.setLlid(shgcDto.getYwid());
			llglDto.setXgry(operator.getYhid());
			LlglDto llglDto_t = getDtoById(llglDto.getLlid());
			shgcDto.setSqbm(llglDto_t.getSqbm());
			List<SpgwcyDto> spgwcyDtos = commonservice.siftJgList(shgcDto.getSpgwcyDtos(), llglDto_t.getSqbm());
			// 审核退回
			if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
				//查出领料数量
				List<HwllxxDto> hwllxxList = hwllxxDao.getDtoHwllxxListByLlid(shgcDto.getYwid());
				List<HwllmxDto> hwllmxList = hwllmxService.getDtoHwllmxListByLlid(shgcDto.getYwid());
				//存更新的库存数据
				List<CkhwxxDto> ckhwxxList = new ArrayList<>();
				List<HwxxDto> hwxxList = new ArrayList<>();
				//组装更新的数据
				for (HwllxxDto hwllxxDto : hwllxxList) {
					CkhwxxDto ckhwxxDto = new CkhwxxDto();
					ckhwxxDto.setCkhwid(hwllxxDto.getCkhwid());
					ckhwxxDto.setYdsbj("0");
					ckhwxxDto.setYds(hwllxxDto.getQlsl());
					ckhwxxList.add(ckhwxxDto);
				}
				//更新仓库货物信息预定数
				boolean result = ckhwxxService.updateByCkhwid(ckhwxxList);
				if(!result)
					throw new BusinessException("msg","更新库存预定数失败！");

				if(!CollectionUtils.isEmpty(hwllmxList)) {
					for (HwllmxDto hwllmxDto : hwllmxList) {
						HwxxDto hwxxDto = new HwxxDto();
						hwxxDto.setHwid(hwllmxDto.getHwid());
						hwxxDto.setYdsbj("0");
						hwxxDto.setYds(hwllmxDto.getYxsl());
						hwxxList.add(hwxxDto);
					}
					//更新货物信息预定数
					result = hwxxService.updateHwxxDtos(hwxxList);
					if(!result)
						throw new BusinessException("msg","更新货物预定数失败！");
				}
				HwllmxDto hwllmxDto = new HwllmxDto();
				hwllmxDto.setIds(shgcDto.getYwid());
				hwllmxDto.setScbj("1");
				hwllmxService.deleteByllid(hwllmxDto);
				// 由于合同提交时直接发起了钉钉审批，所以审核不通过时直接采用钉钉的审批拒绝消息即可
				llglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				// 发送钉钉消息
				if (!CollectionUtils.isEmpty(spgwcyDtos)) {
					for (SpgwcyDto spgwcyDto : spgwcyDtos) {
						if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
							talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00026,
									xxglService.getMsg("ICOMM_SH00098", operator.getZsxm(), systemname,shgcDto.getShlbmc(),
											llglDto_t.getLldh(), llglDto_t.getCklbmc(), llglDto_t.getSqbmmc(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
				//OA取消审批的同时组织钉钉审批
				if(StringUtils.isNotBlank(llglDto_t.getDdspid())) {
					Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), llglDto_t.getDdspid(), "", operator.getDdid());
					//若撤回成功将实例ID至为空
					String success=String.valueOf(cancelResult.get("message"));
					@SuppressWarnings("unchecked")
					Map<String,Object> result_map=JSON.parseObject(success,Map.class);
					boolean bo1= (boolean) result_map.get("success");
					if(bo1)
						dao.updateDdslidToNull(llglDto_t);
				}
				// 审核通过
			} else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
				llglDto.setZt(StatusEnum.CHECK_PASS.getCode());
				llglDto_t.setZsxm(operator.getZsxm());
				if(!"1".equals(llglDto_t.getLllx())&&!"4".equals(llglDto_t.getLllx())&&!"1".equals(systemreceiveflg) && StringUtil.isNotBlank(matridxdsflag)){
					if (!rdRecordService.determine_Entry(llglDto_t.getSqrq())){
						throw new BusinessException("ICOM99019","该月份已结账，不允许再录入U8数据，请修改单据日期!");
					}
					Map<String, Object> map = rdRecordService.addU8LlData(llglDto_t);
					@SuppressWarnings("unchecked")
					List<HwllmxDto> hwllmxDtos = (List<HwllmxDto>) map.get("hwllmxDtos");
					LlglDto llgl_l = (LlglDto) map.get("llglDto");
					llglDto.setGlid(llgl_l.getGlid());
					//更新关联id
					hwllmxService.updateMxList(hwllmxDtos);
//					if(!result_mx)
//						throw new BusinessException("msg","更新货物领料明细失败！");
				}
				HwllmxDto hwllmxDto = new HwllmxDto();
				hwllmxDto.setLlid(llglDto_t.getLlid());
				List<HwllmxDto> hwllmxDtos = hwllmxService.getDtoByLlid(hwllmxDto);
				List<SbysDto> sbysDtos = new ArrayList<>();
				List<String> sbysids = new ArrayList<>();
				boolean flag = false;
				for (HwllmxDto dto : hwllmxDtos) {
					if ("3".equals(dto.getLbcskz1())){
						flag = true;
						if(StringUtil.isNotBlank(dto.getSbysid())){
							sbysids.add(dto.getSbysid());
							SbysDto sbysDto = new SbysDto();
							sbysDto.setSbysid(dto.getSbysid());
							sbysDto.setTzzt("1");
							sbysDto.setSbzt(DeviceStateEnum.USEING.getCode());
							sbysDto.setXsybm(llglDto_t.getSqbm());
							sbysDto.setBmsbfzr(llglDto_t.getBmsbfzr());
							sbysDto.setXgry(operator.getYhid());
							sbysDto.setYsbzt(dto.getSbzt());
							sbysDtos.add(sbysDto);
						}
					}
				}
				if (!CollectionUtils.isEmpty(sbysDtos)){
					sbysService.updateTzs(sbysDtos);
				}
				if (!CollectionUtils.isEmpty(sbysids)){
					List<SbyjllDto> sbyjllDtos = new ArrayList<>();
					for (String sbysid : sbysids) {
						SbyjllDto sbyjllDto = new SbyjllDto();
						sbyjllDto.setLlid(StringUtil.generateUUID());
						sbyjllDto.setSbysid(sbysid);
						sbyjllDto.setBmsbfzr(llglDto_t.getBmsbfzr());
						sbyjllDto.setSybm(llglDto_t.getSqbm());
						sbyjllDto.setSydd(llglDto_t.getSydd());
						sbyjllDto.setYjsj(llglDto_t.getSqrq());
						sbyjllDto.setSyry(llglDto_t.getSqry());
						sbyjllDto.setBz(llglDto_t.getBz());
						sbyjllDto.setZt("80");
						sbyjllDto.setLrry(operator.getYhid());
						sbyjllDtos.add(sbyjllDto);
					}
					sbyjllService.insertSbyjllDtos(sbyjllDtos);
				}
				if (flag||"1".equals(llglDto_t.getSfzy())){
					List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.DEVICE_LL.getCode());
						try{
							if (!CollectionUtils.isEmpty(ddxxglDtolist)) {
								String ICOMM_SH00101 = xxglService.getMsg("ICOMM_SH00101");
								String ICOMM_SH00102 = xxglService.getMsg("ICOMM_SH00102");
								for (DdxxglDto ddxxglDto : ddxxglDtolist) {
									if (StringUtil.isNotBlank(ddxxglDto.getYhm())){
										String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/getwllist/getwlchecklist/getwlchecklist&llid="+llglDto_t.getLlid()+"&urlPrefix="+urlPrefix+"&wbfw=1", StandardCharsets.UTF_8);
										List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
										OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
										btnJsonList.setTitle("小程序");
										btnJsonList.setActionUrl(internalbtn);
										btnJsonLists.add(btnJsonList);
										talkUtil.sendCardDyxxMessageThread(shgcDto.getShlb(),ddxxglDto.getYhid(),ddxxglDto.getYhm(),
												ddxxglDto.getDdid(),
												ICOMM_SH00101,
												StringUtil.replaceMsg(ICOMM_SH00102,
														llglDto_t.getLldh(),systemname,llglDto_t.getSqrmc(),
														llglDto_t.getSqbmmc(), llglDto_t.getSqrq()),
												btnJsonLists, "1");
									}
								}
							}
						}catch(Exception e){
							log.error(e.getMessage());
						}
					}
				if (!CollectionUtils.isEmpty(spgwcyDtos)) {
					for (SpgwcyDto spgwcyDto : spgwcyDtos) {
						if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
							//获取下一步审核人用户名
							//小程序访问
							@SuppressWarnings("deprecation")
							String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/viewpage/receivegoods/receivegoodsView&type=1&ywzd=llid&ywid=" + llglDto_t.getLlid() + "&auditType=" + shgcDto.getAuditType() + "&urlPrefix=" + urlPrefix,StandardCharsets.UTF_8);
							//外网访问
							//String external=dingtalkurl+"page=/pages/index/auditpage/inspectiongoods/inspectionGoodsView?type=1&ywzd=dhjyid&ywid="+dhjyDto.getDhjyid()+"&auditType="+shgcDto.getAuditType()+"&urlPrefix="+urlPrefix;
							List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
							OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
							btnJsonList.setTitle("小程序");
							btnJsonList.setActionUrl(internalbtn);
							btnJsonLists.add(btnJsonList);
							talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00006, StringUtil.replaceMsg(ICOMM_SH00065,
									operator.getZsxm(), systemname,shgcDto.getShlbmc(), llglDto_t.getLldh(), llglDto_t.getCklbmc(), llglDto_t.getSqbmmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");

						}
					}
				}
				
			}else {
				llglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				if (!"1".equals(systemreceiveflg) && StringUtil.isNotBlank(matridxdsflag) && shgcDto.getXlcxh().equals("1") && AuditTypeEnum.AUDIT_GOODS_APPLY_DING.getCode().equals(shgcDto.getAuditType())){
					try {
						Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "领料申请");//获取审批模板ID
						String templateCode=(String) template.get("message");
						//获取申请人信息(合同申请应该为采购部门)
						User user=new User();
						user.setYhid(shgcDto.getSqr());
						user=commonservice.getUserInfoById(user);
						if(user==null)
							throw new BusinessException("ICOM99019","未获取到申请人信息！");
						if(org.apache.commons.lang.StringUtils.isBlank(user.getDdid()))
							throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
						String lllx = "其他（试剂或耗材）";
						List<LlglDto> llglDtos = dao.queryByLlid(llglDto);
						for (LlglDto llglDtoT : llglDtos){
							if(StringUtil.isNotBlank(llglDtoT.getLbcskz1()) && "3".equals(llglDtoT.getLbcskz1())){
								lllx = "设备类";
								break;
							}
						}
						String userid=user.getDdid();
						String dept=user.getJgid();
						Map<String,String> map=new HashMap<>();
						map.put("领料类型", lllx);
						map.put("领料单号", llglDto_t.getLldh());
						map.put("申请人", llglDto_t.getSqrmc());
						List<String> bms = new ArrayList<>();
						bms.add(llglDto_t.getSqbm());
						map.put("申请部门", JSON.toJSONString(bms));
						map.put("申请日期", llglDto_t.getSqrq());
						map.put("出库类别", llglDto_t.getCklbmc());
						map.put("记录编号", llglDto_t.getJlbh());
						map.put("领料明细", applicationurl + urlPrefix+"/ws/production/requisitionInfo?llid="+llglDto_t.getLlid());

						Map<String,Object> t_map=talkUtil.createInstance(operator.getYhm(), templateCode, null, null, userid, dept, map,null,null);
						String str=(String) t_map.get("message");
						String status=(String) t_map.get("status");
						if("success".equals(status)) {
							@SuppressWarnings("unchecked")
							Map<String,Object> result_map=JSON.parseObject(str,Map.class);
							if(("0").equals(String.valueOf(result_map.get("errcode")))) {
								//保存至钉钉分布式管理表(主站)
								RestTemplate t_restTemplate = new RestTemplate();
								MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
								paramMap.add("ddslid", String.valueOf(result_map.get("process_instance_id")));
								paramMap.add("fwqm", urlPrefix);
								paramMap.add("cljg", "1");
								paramMap.add("fwqmc", systemname);
								paramMap.add("hddz",applicationurl);
								paramMap.add("spywlx",shgcDto.getShlb());
								
								//根据审批类型获取钉钉审批的业务类型，业务名称
								JcsjDto jcsjDto_dd = new JcsjDto();
								jcsjDto_dd.setCsdm(shgcDto.getAuditType());
								jcsjDto_dd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
								jcsjDto_dd = jcsjService.getDtoByCsdmAndJclb(jcsjDto_dd);
								if(StringUtil.isBlank(jcsjDto_dd.getCsmc()) || StringUtil.isBlank(jcsjDto_dd.getCskz1())) {
									throw new BusinessException("msg","请设置"+shgcDto.getShlbmc()+"的钉钉审批回调类型基础数据！");
								}
								
								paramMap.add("ywmc", operator.getZsxm()+"提交的"+jcsjDto_dd.getCsmc());
								paramMap.add("ywlx", jcsjDto_dd.getCskz1());
								paramMap.add("wbcxid", operator.getWbcxid());
								//分布式保留
								DdfbsglDto ddfbsglDto = new DdfbsglDto();
								ddfbsglDto.setProcessinstanceid(String.valueOf(result_map.get("process_instance_id")));
								ddfbsglDto.setFwqm(urlPrefix);
								ddfbsglDto.setCljg("1");
								ddfbsglDto.setFwqmc(systemname);
								ddfbsglDto.setSpywlx(shgcDto.getShlb());
								ddfbsglDto.setHddz(applicationurl);
								ddfbsglDto.setYwlx(jcsjDto_dd.getCskz1());
								ddfbsglDto.setYwmc(operator.getZsxm()+"提交的"+jcsjDto_dd.getCsmc());
								ddfbsglDto.setWbcxid(operator.getWbcxid());
								boolean result_t = ddfbsglService.saveDistributedMsg(ddfbsglDto);
								if(!result_t)
									return false;
								//主站保留一份
								if(StringUtils.isNotBlank(registerurl)){
									boolean result = t_restTemplate.postForObject(registerurl + "/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
									if(!result)
										return false;
								}
								//若钉钉审批提交成功，则关联审批实例ID
								llglDto.setDdspid(String.valueOf(result_map.get("process_instance_id")));
							}else {
								throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+t_map.get("message"));
							}
						}else {
							throw new BusinessException("msg","发起钉钉审批失败!错误信息:"+t_map.get("message"));
						}

					} catch (BusinessException e) {
						// TODO: handle exception
						throw new BusinessException("msg",e.getMsg());
					}catch (Exception e) {
						// TODO: handle exception
						throw new BusinessException("msg","异常!异常信息:"+ e);
					}

//						//如果提交审批时，申请人属于下一个岗位时，则直接将xlcxh设置为2，跳过当前审批流程
//						if(spgwcyDtos!=null && spgwcyDtos.size()>0 && "1".equals(shgcDto.getXlcxh())) {
//							for(int i=0;i<spgwcyDtos.size();i++) {
//								if(spgwcyDtos.get(i).getYhid().equals(shgcDto.getSqr())) {
//									shgcDto.setXlcxh("2");
//								}
//							}
//						}

				}else{
					String ICOMM_SH00071 = xxglService.getMsg("ICOMM_SH00071");
						// 发送钉钉消息 普通领料审核
						if (!CollectionUtils.isEmpty(spgwcyDtos)) {
							try {
								for (SpgwcyDto spgwcyDto : spgwcyDtos) {
									if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
										//获取下一步审核人用户名
										//钉钉小程序
										@SuppressWarnings("deprecation")
										String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/auditpage/receivegoods/receivegoodsView&type=1&ywzd=llid&ywid=" + llglDto_t.getLlid() + "&auditType=" + shgcDto.getAuditType() + "&urlPrefix=" + urlPrefix,StandardCharsets.UTF_8);
										//外网访问
										//String external=dingtalkurl+"page=/pages/index/auditpage/inspectiongoods/inspectionGoodsView?type=1&ywzd=dhjyid&ywid="+dhjyDto.getDhjyid()+"&auditType="+shgcDto.getAuditType()+"&urlPrefix="+urlPrefix;
										List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
										OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
										btnJsonList.setTitle("小程序");
										btnJsonList.setActionUrl(internalbtn);
										btnJsonLists.add(btnJsonList);
										talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00003, StringUtil.replaceMsg(ICOMM_SH00071,
												operator.getZsxm(), systemname,shgcDto.getShlbmc(), llglDto_t.getLldh(),
												llglDto_t.getCklbmc(), llglDto_t.getJlbh(), llglDto_t.getSqbmmc(),
												DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");

									}
								}
							} catch (Exception e) {
								throw new BusinessException("msg",e.getMessage());
							}
						}
				}

                //发送钉钉消息--取消审核人员
                if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())){
                    int size = shgcDto.getNo_spgwcyDtos().size();
                    for(int i=0;i<size;i++){
                        if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,llglDto_t.getLldh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                        }
                    }
                }
			}
//			}
			update(llglDto);
		}

	return true;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		String s = "";
		// TODO Auto-generated method stub
		if (shgcList == null || shgcList.isEmpty()) {
			return true;
		}
		if (auditParam.isCancelOpe()) {
		// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String llid = shgcDto.getYwid();
				LlglDto llglDto = new LlglDto();
				llglDto.setXgry(operator.getYhid());
				llglDto.setLlid(llid);
				llglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				
				modSavePicking(llglDto);


			}
		} else {
			// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String llid = shgcDto.getYwid();
				LlglDto llglDto = new LlglDto();
				llglDto.setXgry(operator.getYhid());
				llglDto.setLlid(llid);
				llglDto.setZt(StatusEnum.CHECK_NO.getCode());
				//查出领料数量
				List<HwllxxDto> hwllxxList = hwllxxDao.getDtoHwllxxListByLlid(shgcDto.getYwid());
				//判断是否是取样的单子
				if("0".equals(hwllxxList.get(0).getQybj())) {
					
					List<HwllmxDto> hwllmxList = hwllmxService.getDtoHwllmxListByLlid(shgcDto.getYwid());
					//存更新的库存数据
					List<CkhwxxDto> ckhwxxList = new ArrayList<>();
					List<HwxxDto> hwxxList = new ArrayList<>();
					//组装更新的数据
					for (HwllxxDto hwllxxDto : hwllxxList) {
						CkhwxxDto ckhwxxDto = new CkhwxxDto();
						ckhwxxDto.setCkhwid(hwllxxDto.getCkhwid());
						ckhwxxDto.setYdsbj("0");
						ckhwxxDto.setYds(hwllxxDto.getQlsl());
						ckhwxxList.add(ckhwxxDto);
					}
					//更新仓库货物信息预定数
					boolean result = ckhwxxService.updateByCkhwid(ckhwxxList);
					if(!result)
						throw new BusinessException("msg","更新库存预定数失败！");
						
					if(!CollectionUtils.isEmpty(hwllmxList)) {
						for (HwllmxDto hwllmxDto : hwllmxList) {
							HwxxDto hwxxDto = new HwxxDto();
							hwxxDto.setHwid(hwllmxDto.getHwid());
							hwxxDto.setYdsbj("0");
							hwxxDto.setYds(hwllmxDto.getYxsl());
							hwxxList.add(hwxxDto);
						}
						//更新货物信息预定数
						result = hwxxService.updateHwxxDtos(hwxxList);
						if(!result)
							throw new BusinessException("msg","更新货物预定数失败！");
					}
					HwllmxDto hwllmxDto = new HwllmxDto();
					hwllmxDto.setIds(shgcDto.getYwid());
					hwllmxDto.setScbj("1");
					hwllmxService.deleteByllid(hwllmxDto);
				}
				//OA取消审批的同时组织钉钉审批
				LlglDto llglDto1=dao.getDtoByllId(llid);
				if(llglDto1!=null && StringUtils.isNotBlank(llglDto1.getDdspid())) {
					Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), llglDto1.getDdspid(), "", operator.getDdid());
					//若撤回成功将实例ID至为空
					String success=String.valueOf(cancelResult.get("message"));
					@SuppressWarnings("unchecked")
					Map<String,Object> result_map=JSON.parseObject(success,Map.class);
					boolean bo1= (boolean) result_map.get("success");
					if(bo1)
						dao.updateDdslidToNull(llglDto1);
				}
				update(llglDto);
			}
		}
		return true;
	}

	/**
	 * 高级修改领料信息
	 * @param llglDto
	 * @return
	 * @throws BusinessException 
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public boolean seniormodSavePicking(LlglDto llglDto) throws BusinessException {
		//更新领料管理信息
		boolean result = update(llglDto);
		if(!result)
			return false;
		
		//文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(llglDto.getFjids())) {
			for (int i = 0; i < llglDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(llglDto.getFjids().get(i),llglDto.getLlid());
				if(!saveFile)
					throw new BusinessException("msg","领料附件保存失败!");
			}
		}
		if(!("1".equals(llglDto.getQybj())) && StringUtil.isNotBlank(llglDto.getHwllmx_json())) {
			//获取货物领料明细信息
			List<HwllmxDto> hwllmxlist= JSON.parseArray(llglDto.getHwllmx_json(),HwllmxDto.class);
			//获取该领料管理的领料明细集合，如果存在做更新，不存在做添加
			HwllmxDto hwllmxDto = new HwllmxDto();
			hwllmxDto.setLlid(llglDto.getLlid());
			List<HwllmxDto> hwllmxs = hwllmxService.getDtoList(hwllmxDto);
			
			//存更新的货物领料明细信息
			List<HwllmxDto> hwllmxDtos_mod = new ArrayList<>();
			//存更新的货物领料明细信息
			List<HwllmxDto> hwllmxDtos_add = new ArrayList<>();
			StringBuilder ids = new StringBuilder();
			for (HwllmxDto hwllmxDto_h : hwllmxlist) {
				boolean Judgment =true;
				for (HwllmxDto hwllmxDto_s : hwllmxs) {
					if(hwllmxDto_h.getHwllid().equals(hwllmxDto_s.getHwllid())&&hwllmxDto_h.getHwid().equals(hwllmxDto_s.getHwid())) {
						hwllmxDtos_mod.add(hwllmxDto_h);
						Judgment=false;
						break;
					}
				}
				if(Judgment) {
					if(Double.parseDouble(StringUtil.isNotBlank(hwllmxDto_h.getYxsl())?hwllmxDto_h.getYxsl():"0")>0.00) {
						hwllmxDtos_add.add(hwllmxDto_h);
						ids.append(",").append(hwllmxDto_h.getHwid());
					}
				}
			}
			
			//判断hwids是否为空
			if(StringUtil.isNotBlank(ids.toString())) {
				ids = new StringBuilder(ids.substring(1));
				HwxxDto hw = new HwxxDto();
				hw.setIds(ids.toString());
				List<HwxxDto> hwList = hwxxService.getDtoList(hw);
				
				//判断领料类型是否是OA领料,如果是，判断是否存在其他到货类型
				StringBuilder wlxx = new StringBuilder();
				if(!CollectionUtils.isEmpty(hwList)) {
					for (HwxxDto hwxx : hwList) {
						if("1".equals(llglDto.getLllx()) && !"OA".equals(hwxx.getDhlxdm())) {
							wlxx.append(";").append("物料编码：").append(hwxx.getWlbm()).append(",").append("到货单号:").append(hwxx.getDhdh());
							result = false;
						}
						if(!"1".equals(llglDto.getLllx()) && "OA".equals(hwxx.getDhlxdm())) {
							wlxx.append(";").append("物料编码：").append(hwxx.getWlbm()).append(",").append("到货单号:").append(hwxx.getDhdh());
							result = false;
						}
					}
				}
				
				if(!result) {
					wlxx = new StringBuilder(wlxx.substring(1));
					throw new BusinessException("msg",
							"1".equals(llglDto.getLllx())? wlxx +"这些物料不允许OA领料出库!":wlxx + "这些物料不允许普通领料出库!");
				}
			}
			
			
			
			if(!CollectionUtils.isEmpty(hwllmxDtos_mod)) {				
				//更新货物领料明细
				boolean result_mx = hwllmxService.updateHwllmxs(hwllmxDtos_mod);
				if(!result_mx)
					throw new BusinessException("msg","货物领料明细更新失败!");
				}
			if(!CollectionUtils.isEmpty(hwllmxDtos_add)) {
					//新增货物领料明细
					boolean result_mx = hwllmxService.insertHwllmxs(hwllmxDtos_add);
					if(!result_mx)
						throw new BusinessException("msg","货物领料明细新增失败!");
			}		
			//存货物信息
			List<HwxxDto> hwxxDtos = new ArrayList<>();
			//存货物领料信息
			List<HwllxxDto> hwllxxDtos = new ArrayList<>();
			for (HwllmxDto hwllmxDto_t : hwllmxlist) {
				HwxxDto hwxxDto = new HwxxDto();
				hwxxDto.setHwid(hwllmxDto_t.getHwid());
				double yds = Double.parseDouble(StringUtil.isNotBlank(hwllmxDto_t.getYxsl())?hwllmxDto_t.getYxsl():"0") - Double.parseDouble(StringUtil.isNotBlank(hwllmxDto_t.getQyxsl())?hwllmxDto_t.getQyxsl():"0");
				hwxxDto.setYdsbj("1");
				hwxxDto.setYds(Double.toString(yds));
				hwxxDto.setXgry(llglDto.getXgry());
				hwxxDtos.add(hwxxDto);
			}
			List<HwllmxDto> hwllmxDtos_t = hwllmxService.getDtoGroupByLlid(llglDto.getLlid());
			for (HwllmxDto hwllmxDto_mx : hwllmxDtos_t) {
				HwllxxDto hwllxxDto = new HwllxxDto();
				hwllxxDto.setHwllid(hwllmxDto_mx.getHwllid());
				hwllxxDto.setYxsl(hwllmxDto_mx.getZyxsl());		
				hwllxxDtos.add(hwllxxDto);
			}
			//2.更新货物信息预定数
			boolean result_hw = hwxxService.updateHwxxDtos(hwxxDtos);
			if(!result_hw)
				throw new BusinessException("msg","货物预定数更新失败!");
			//3.更新货物领料信息实领数量
			if(!CollectionUtils.isEmpty(hwllxxDtos)) {
				boolean result_hwll = hwllxxDao.updateHwllxxDtos(hwllxxDtos) > 0;
				if(!result_hwll)
					throw new BusinessException("msg","货物领料信息实领数量更新失败!");
			}
		}
		return true;
	}

	/**
	 * 领料删除
	 * @param llglDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteLlgl(LlglDto llglDto, List<LyrkxxDto> list) throws BusinessException {
		
		//查出所有领料单
		List<LlglDto> llglDtos = getDtoList(llglDto);
		//存不是取样的单子
		StringBuilder ids_notQy = new StringBuilder();
		//存取样的单子
		StringBuilder ids_Qy = new StringBuilder();
		//存所有的领料单
		StringBuilder ids_all = new StringBuilder();
		for (LlglDto llglDto_s : llglDtos) {
			if("0".equals(llglDto_s.getQybj())) {
				ids_notQy.append(",").append(llglDto_s.getLlid());
			}
			if("1".equals(llglDto_s.getQybj())) {
				ids_Qy.append(",").append(llglDto_s.getLlid());
			}
			ids_all.append(",").append(llglDto_s.getLlid());
		}

		if(StringUtil.isNotBlank(ids_notQy.toString())) {
			ids_notQy = new StringBuilder(ids_notQy.substring(1));
			llglDto.setIds(ids_notQy.toString()); //替换ids
			//查出所有hwllxx
			HwllxxDto hwllxx = new HwllxxDto();
			//通过领料id查询
			hwllxx.setIds(llglDto.getIds());
			List<HwllxxDto> hwllxxList = hwllxxDao.getDtoList(hwllxx);
			//存放库存数据
			List<CkhwxxDto> ckhwxxList = new ArrayList<>();
			
			//处理库存
			for (HwllxxDto hwllxxDto : hwllxxList) {
				//实领数量请领数量为空处理
				hwllxxDto.setSlsl(StringUtil.isNotBlank(hwllxxDto.getSlsl())?hwllxxDto.getSlsl():"0");
				hwllxxDto.setQlsl(StringUtil.isNotBlank(hwllxxDto.getQlsl())?hwllxxDto.getQlsl():"0");
				//处理领料审核通过的货物
				if(StatusEnum.CHECK_PASS.getCode().equals(hwllxxDto.getZt())) {
					//判断是否出库，出库需要修改库存量，未出库需要修改预定数
					if(StatusEnum.CHECK_PASS.getCode().equals(hwllxxDto.getCkzt())) {
						CkhwxxDto ckhwxxDto = new CkhwxxDto();
						ckhwxxDto.setKcl(hwllxxDto.getSlsl()); //库存量=实领数量
						ckhwxxDto.setKclbj("1"); //库存量标记，1+，0-
						ckhwxxDto.setCkhwid(hwllxxDto.getCkhwid()); //仓库货物id
						ckhwxxList.add(ckhwxxDto);
					}else {
						CkhwxxDto ckhwxxDto = new CkhwxxDto();
						ckhwxxDto.setYds(hwllxxDto.getQlsl()); //预定数 = 请领数量
						ckhwxxDto.setYdsbj("0"); //库存量标记，1+，0-
						ckhwxxDto.setCkhwid(hwllxxDto.getCkhwid()); //仓库货物id
						ckhwxxList.add(ckhwxxDto);
					}
				}
				//审核中
				if(StatusEnum.CHECK_SUBMIT.getCode().equals(hwllxxDto.getZt())) {
					CkhwxxDto ckhwxxDto = new CkhwxxDto();
					ckhwxxDto.setYds(hwllxxDto.getQlsl()); //预定数 = 请领数量
					ckhwxxDto.setYdsbj("0"); //预定数标记，1+，0-
					ckhwxxDto.setCkhwid(hwllxxDto.getCkhwid()); //仓库货物id
					ckhwxxList.add(ckhwxxDto);
				}
				//审核未通过，未提交由于未修改预定数，无需操作。
			}
			
			
			//查出所有hwllmx
			HwllmxDto hwllmx = new HwllmxDto();
			//通过领料id查询
			hwllmx.setIds(llglDto.getIds());
			List<HwllmxDto> hwllmxList = hwllmxService.getDtoList(hwllmx);
			//组装hwllmx数据
			List<HwxxDto> hwxxList = new ArrayList<>();
			List<SbysDto> sbysDtos = new ArrayList<>();
			for (HwllmxDto hwllmxDto : hwllmxList) {
				if ("3".equals(hwllmxDto.getLbcskz1())){
					SbysDto sbysDto = new SbysDto();
					sbysDto.setSbysid(hwllmxDto.getSbysid());
					sbysDto.setTkbj(hwllmxDto.getTkbj());
					sbysDto.setXgry(llglDto.getScry());
					sbysDtos.add(sbysDto);
				}
				//允许数量实领数量为空处理
				hwllmxDto.setSlsl(StringUtil.isNotBlank(hwllmxDto.getSlsl())?hwllmxDto.getSlsl():"0");
				hwllmxDto.setYxsl(StringUtil.isNotBlank(hwllmxDto.getYxsl())?hwllmxDto.getYxsl():"0");
				//审核通过
				if(StatusEnum.CHECK_PASS.getCode().equals(hwllmxDto.getZt())) {
					//已出库
					if(StatusEnum.CHECK_PASS.getCode().equals(hwllmxDto.getCkzt())) {
						HwxxDto hwxx = new HwxxDto();
						hwxx.setHwid(hwllmxDto.getHwid()); //货物id
						hwxx.setAddkcl(hwllmxDto.getSlsl()); //增加的库存量=实领数量
						hwxx.setXgry(llglDto.getScry()); //修改人员
						hwxxList.add(hwxx);
					}else {
						//未出库
						HwxxDto hwxx = new HwxxDto();
						hwxx.setHwid(hwllmxDto.getHwid()); //货物id
						hwxx.setYdsbj("0"); //预定数标记 1+，0-
						hwxx.setYds(hwllmxDto.getYxsl()); //预定数=允许数量
						hwxx.setXgry(llglDto.getScry()); //修改人员
						hwxxList.add(hwxx);
					}
				}
				//审核中
				if(StatusEnum.CHECK_SUBMIT.getCode().equals(hwllmxDto.getZt())) {
					HwxxDto hwxx = new HwxxDto();
					hwxx.setHwid(hwllmxDto.getHwid()); //货物id
					hwxx.setYdsbj("0"); //预定数标记 1+，0-
					hwxx.setYds(hwllmxDto.getYxsl()); //预定数=允许数量
					hwxx.setXgry(llglDto.getScry()); //修改人员
					hwxxList.add(hwxx);
				}
				//审核未通过，未提交由于未修改预定数，无需操作。
			}
			if (!CollectionUtils.isEmpty(sbysDtos)){
				sbysService.updateTzsWithNull(sbysDtos);
			}
			boolean result;
			
			//批量更新库存
			if(!CollectionUtils.isEmpty(ckhwxxList)) {
				result = ckhwxxService.updateByCkhwid(ckhwxxList);
				if(!result)
					throw new BusinessException("msg","库存修改失败!");			
			}
			
			//批量更新货物库存
			if(!CollectionUtils.isEmpty(hwxxList)) {
				result = hwxxService.updateHwxxDtos(hwxxList);
				if(!result)
					throw new BusinessException("msg","货物库存修改失败!");
			}
		}
		ids_all = new StringBuilder(ids_all.substring(1));
		llglDto.setIds(ids_all.toString());
		//批量更新领料信息
		boolean result_d = delete(llglDto);
		if(!result_d)
			throw new BusinessException("msg","领料删除失败!");
		
		//删除审核过程
		List<String> ywids = llglDto.getIds();
		if(!CollectionUtils.isEmpty(ywids))
			shgcService.deleteByYwids(ywids);
		
		//批量更新货物领料信息
		HwllxxDto hwllxx_xx  = new HwllxxDto();
		hwllxx_xx.setIds(llglDto.getIds());
		hwllxx_xx.setScry(llglDto.getScry());
		hwllxx_xx.setScbj(llglDto.getScbj());
		result_d = hwllxxDao.deleteByllid(hwllxx_xx) > 0;
		if(!result_d)
			throw new BusinessException("msg","货物领料信息删除失败!");
				
		//批量更新货物领料明细
		HwllmxDto hwllmx_mx = new HwllmxDto();
		hwllmx_mx.setScry(llglDto.getScry());
		hwllmx_mx.setScbj(llglDto.getScbj());
		hwllmx_mx.setIds(llglDto.getIds());
		hwllmxService.deleteByllid(hwllmx_mx);
		
		//删除出库信息
		CkglDto ckglDto = new CkglDto();
		ckglDto.setScry(llglDto.getScry());
		ckglDto.setScbj(llglDto.getScbj());
		ckglDto.setIds(llglDto.getIds());	
		//获取出库信息
		List<CkglDto> ckgls = ckglService.getDtoByLlids(ckglDto);
		if(!CollectionUtils.isEmpty(ckgls)) {
			result_d = ckglService.deleteByLlid(ckglDto);
			if(!result_d)
				throw new BusinessException("msg","废弃出库信息失败!");
			StringBuilder ids_ck = new StringBuilder();
			for (CkglDto ckglDto_t : ckgls) {
				ids_ck.append(",").append(ckglDto_t.getCkid());
			}
			ids_ck = new StringBuilder(ids_ck.substring(1));

			CkmxDto ckmxDto = new CkmxDto();
			ckmxDto.setScry(llglDto.getScry());
			ckmxDto.setScbj(llglDto.getScbj());
			ckmxDto.setIds(ids_ck.toString());
			result_d = ckmxService.deleteByCkid(ckmxDto);
			if(!result_d)
				throw new BusinessException("msg","废弃出库明细失败!");
		}
		LyrkxxDto lyrkxxDto=new LyrkxxDto();
		lyrkxxDto.setIds(llglDto.getIds());
		lyrkxxDto.setScry(llglDto.getScry());
		lyrkxxService.delete(lyrkxxDto);
		if(!CollectionUtils.isEmpty(list)){
			List<LykcxxDto> updateList=new ArrayList<>();
			for(LyrkxxDto dto:list){
				dto.setScbj("1");
				LykcxxDto lykcxxDto=new LykcxxDto();
				lykcxxDto.setLykcid(dto.getLykcid());
				lykcxxDto.setKcl(dto.getSl());
				lykcxxDto.setKclbj("0");
				lykcxxDto.setXgry(llglDto.getScry());
				updateList.add(lykcxxDto);
			}
            boolean isSuccess = lykcxxService.updateKclListByBj(updateList);
            if(!isSuccess){
                throw new BusinessException("msg","留样库存信息修改失败！");
            }
        }
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String, Object> params, LlglDto llglDto){
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuilder sqlParam = new StringBuilder();
		for (DcszDto dcszDto : choseList){
			if (dcszDto == null || dcszDto.getDczd() == null)
				continue;
			sqlParam.append(",");
			if (StringUtil.isNotBlank(dcszDto.getSqlzd()))
			{
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		llglDto.setSqlParam(sqlParam.toString());
	}
	/**
	 * 领料列表选中导出
	 * @param params
	 * @return
	 */
    public List<LlglDto> getListForSelectExp(Map<String, Object> params){
    	LlglDto llglDto = (LlglDto) params.get("entryData");
        queryJoinFlagExport(params,llglDto);
		return dao.getListForSelectExp(llglDto);
	}
	/**
	 * 领料列表生产领料选中导出
	 * @param params
	 * @return
	 */
	public List<LlglDto> getListForProductionSelectExp(Map<String, Object> params){
		LlglDto llglDto = (LlglDto) params.get("entryData");
		queryJoinFlagExport(params,llglDto);
		List<LlglDto> llglDtos = dao.getListForProductionSelectExp(llglDto);
		if (!CollectionUtils.isEmpty(llglDtos)&&(llglDto.getSqlParam().contains("zje")||llglDto.getSqlParam().contains("hsdj"))){
			List<String> wlbms = llglDtos.stream().map(LlglDto::getWlbm_t).distinct().collect(Collectors.toList());
			IA_SummaryDto iaSummaryDto = new IA_SummaryDto();
			iaSummaryDto.setcInvCodes(wlbms);
			iaSummaryDto.setiMonth(DateUtils.getCustomFomratCurrentDate("MM"));
			iaSummaryDto.setiYear(DateUtils.getCustomFomratCurrentDate("yyyy"));
			List<IA_SummaryDto> priceForcInvCodes = iaSummaryDao.getPriceForcInvCodes(iaSummaryDto);
			for (LlglDto dto : llglDtos) {
				for (IA_SummaryDto priceForcInvCode : priceForcInvCodes) {
					if (dto.getWlbm_t().equals(priceForcInvCode.getcInvCode())){
						BigDecimal iMoneyBig = new BigDecimal(priceForcInvCode.getiMoney());
						dto.setZje(String.valueOf(iMoneyBig.multiply(new BigDecimal(dto.getSlsl())).setScale(6, RoundingMode.HALF_UP)));
						dto.setHsdj(String.valueOf(iMoneyBig.setScale(6,RoundingMode.HALF_UP)));
						break;
					}
				}
			}
		}
		return llglDtos;
	}
	/**
	 * 根据搜索条件获取导出条数
	 * @param
	 * @return
	 */
	public int getCountForSearchExp(LlglDto llglDto,Map<String,Object> params){
		if ("sclldc".equals(llglDto.getDcflag())){
			return dao.getProductionCountForSearchExp(llglDto);
		}else {
			return dao.getCountForSearchExp(llglDto);
		}
	}
	
	/**
	 * 根据搜索条件分页获取导出信息
	 * @param
	 * @return
	 */
	public List<LlglDto> getListForSearchExp(Map<String,Object> params){
		LlglDto llglDto = (LlglDto)params.get("entryData");
		queryJoinFlagExport(params,llglDto);
		if ("sclldc".equals(llglDto.getDcflag())){
			List<LlglDto> llglDtos = dao.getListForProductionSearchExp(llglDto);
			if (!CollectionUtils.isEmpty(llglDtos)&&(llglDto.getSqlParam().contains("zje")||llglDto.getSqlParam().contains("hsdj"))){
				List<String> wlbms = llglDtos.stream().map(LlglDto::getWlbm_t).distinct().collect(Collectors.toList());
				IA_SummaryDto iaSummaryDto = new IA_SummaryDto();
				iaSummaryDto.setcInvCodes(wlbms);
				iaSummaryDto.setiMonth(DateUtils.getCustomFomratCurrentDate("MM"));
				iaSummaryDto.setiYear(DateUtils.getCustomFomratCurrentDate("yyyy"));
				List<IA_SummaryDto> priceForcInvCodes = iaSummaryDao.getPriceForcInvCodes(iaSummaryDto);
				for (LlglDto dto : llglDtos) {
					for (IA_SummaryDto priceForcInvCode : priceForcInvCodes) {
						if (dto.getWlbm_t().equals(priceForcInvCode.getcInvCode())){
							BigDecimal iMoneyBig = new BigDecimal(priceForcInvCode.getiMoney());
							dto.setZje(String.valueOf(iMoneyBig.multiply(new BigDecimal(dto.getSlsl())).setScale(6, RoundingMode.HALF_UP)));
							dto.setHsdj(String.valueOf(iMoneyBig.setScale(6,RoundingMode.HALF_UP)));
							break;
						}
					}
				}
			}
			return llglDtos;
		}else{
			return dao.getListForSearchExp(llglDto);
		}
	}

	/**
	 * 出库修改领料信息
	 * @param llglDto
	 * @return
	 * @throws BusinessException
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deliverymodSavePicking(LlglDto llglDto) throws BusinessException {
		llglDto.setCkzt("80");
		llglDto.setWcbj("1");
		//更新领料管理信息
		boolean result = dao.updateForCkzt(llglDto);
		if(!result)
			throw new BusinessException("msg","已经出库，不允许重复出库！");
		
		//获取货物领料明细信息
		List<HwllmxDto> hwllmxlist= JSON.parseArray(llglDto.getHwllmx_json(),HwllmxDto.class);
		if(CollectionUtils.isEmpty(hwllmxlist)) {
			throw new BusinessException("msg","货物领料明细为空，出库失败!");
		}
		//更新货物领料明细
		boolean result_mx = hwllmxService.updateHwllmxs(hwllmxlist);
		if(!result_mx)
			throw new BusinessException("msg","货物领料明细更新失败!");
		//根据仓库id分组，判断领料明细所在仓库，如果存在多个不同的仓库，则生成多个不同的出库单
		List<HwllmxDto> hwllmxs = hwllmxService.groupByCkid(llglDto.getLlid());
		//存要新增的出库单
		List<CkglDto> ckglList = new ArrayList<>();
		//生成出库单 生成规则: CK-20201022-01 LL-年份日期-流水号 。
		String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		String prefix = "CK" + "-" + date + "-";
		String serial = ckglService.generateDjh(prefix);
		int serial_int = Integer.parseInt(serial)-1;
		//存要新增的出库明细
		List<CkmxDto> ckmxList = new ArrayList<>();
		//根据不同的仓库，生成不同的出库单
		for (HwllmxDto hwllmxDto : hwllmxs) {
			//生成出库单
			CkglDto ckglDto = new CkglDto();
			//生成出库单号 prefix +（00+（流水号+1）转换成字符，截取后三位）String.SubString(str.length()-3)
			serial_int = serial_int +1;
			String ckdh="00"+ serial_int;
			ckglDto.setCkdh(prefix+ckdh.substring(ckdh.length()-3));
			ckglDto.setCkid(StringUtil.generateUUID());
			ckglDto.setBm(llglDto.getSqbm());
			ckglDto.setBz(llglDto.getBz());
			ckglDto.setCk(hwllmxDto.getCk());
			ckglDto.setCklb(llglDto.getCklb());
			ckglDto.setCkrq(llglDto.getCkrq());
			ckglDto.setLrry(llglDto.getXgry());
			ckglDto.setLlr(llglDto.getLlr());
			ckglDto.setFlr(llglDto.getFlry());
			ckglDto.setLlid(llglDto.getLlid());
			ckglDto.setZt(StatusEnum.CHECK_NO.getCode());
			ckglList.add(ckglDto);
			for (HwllmxDto hwllmx : hwllmxlist) {
				if(hwllmx.getCk().equals(hwllmxDto.getCk())) {
					//如果领料明细大于0得才存入出库明细
					if(Double.parseDouble(StringUtil.isNotBlank(hwllmx.getSlsl())?hwllmx.getSlsl():"0")>0) {
						HwllmxDto hwllmx_r = hwllmxService.queryHwllmxs(hwllmx);
						hwllmx.setCkid(ckglDto.getCkid());
						CkmxDto ckmxDto = new CkmxDto();
						ckmxDto.setCkid(ckglDto.getCkid());
						ckmxDto.setCkmxid(StringUtil.generateUUID());
						ckmxDto.setCksl(hwllmx.getSlsl());
						ckmxDto.setHwid(hwllmx.getHwid());
						ckmxDto.setLrry(llglDto.getXgry());
						ckmxDto.setLlmxid(hwllmx_r.getLlmxid());
						ckmxList.add(ckmxDto);
					}					
				}
			}
		}
		
		//新增出库单
		boolean result_ckgl = ckglService.insertCkgls(ckglList);
		if(!result_ckgl)
			throw new BusinessException("msg","出库单新增失败!");

		//新增出库明细
		boolean result_ckmx = ckmxService.insertckmxs(ckmxList);
		if(!result_ckmx)
			throw new BusinessException("msg","出库明细新增失败!");
		
		//更新货物领料明细
		result_mx = hwllmxService.updateHwllmxs(hwllmxlist);
		if(!result_mx)
			throw new BusinessException("msg","货物领料明细更新失败!");
		
		//存货物信息
		List<HwxxDto> hwxxDtos = new ArrayList<>();
		//存货物领料信息
		List<HwllxxDto> hwllxxDtos = new ArrayList<>();
		//存仓库货物信息
		List<CkhwxxDto> ckhwxxDtos = new ArrayList<>();
		if("1".equals(llglDto.getQybj())) {
			for (HwllmxDto hwllmxDto_t : hwllmxlist) {
				HwxxDto hwxxDto = new HwxxDto();
				hwxxDto.setHwid(hwllmxDto_t.getHwid());
				//货物数量-实领数量 (实领数量=取样量)
				hwxxDto.setSl(hwllmxDto_t.getSlsl());
				hwxxDtos.add(hwxxDto);
			}
			// boolean result_sl = hwxxService.updateRksl(hwxxDtos);
			// if(!result_sl) {
			// 	throw new BusinessException("msg","货物数量更新失败!");
			// }
		}
		if(!("1".equals(llglDto.getQybj()))) {
			HwllmxDto hwllmx = new HwllmxDto();
			hwllmx.setLlid(llglDto.getLlid());
			List<HwllmxDto> hwllmxDtos = hwllmxService.getDtoList(hwllmx);
			for (HwllmxDto hwllmxDto_t : hwllmxDtos) {
				HwxxDto hwxxDto = new HwxxDto();
				hwxxDto.setHwid(hwllmxDto_t.getHwid());
				//库存量减去实领数量
				hwxxDto.setModkcl(hwllmxDto_t.getSlsl());
				//预定数减去允许数量，因为之前新增加的是允许数量
				hwxxDto.setYdsbj("0");
				hwxxDto.setYds(hwllmxDto_t.getYxsl());
				hwxxDto.setXgry(llglDto.getXgry());
				hwxxDtos.add(hwxxDto);
			}
		}		
		List<HwllmxDto> hwllmxDtos_t = hwllmxService.getDtoGroupByLlxx(llglDto.getLlid());
		for (HwllmxDto hwllmxDto_mx : hwllmxDtos_t) {
			HwllxxDto hwllxxDto = new HwllxxDto();
			hwllxxDto.setHwllid(hwllmxDto_mx.getHwllid());
			hwllxxDto.setSlsl(hwllmxDto_mx.getZslsl());		
			hwllxxDtos.add(hwllxxDto);
			if(!("1".equals(llglDto.getQybj()))) {
				CkhwxxDto ckhwxxDto = new CkhwxxDto();
				ckhwxxDto.setCkhwid(hwllmxDto_mx.getCkhwid());
				//预定数减去清领数
				double yds = Double.parseDouble(hwllmxDto_mx.getCkyds()) - Double.parseDouble(hwllmxDto_mx.getHwllqlsl());
				ckhwxxDto.setYds(Double.toString(yds));
				//库存量减去实领数量
				double kcl = Double.parseDouble(hwllmxDto_mx.getCkkcl()) - Double.parseDouble(StringUtil.isNotBlank(hwllmxDto_mx.getZslsl())?hwllmxDto_mx.getZslsl():"0");
				ckhwxxDto.setKcl(Double.toString(kcl));
				ckhwxxDtos.add(ckhwxxDto);
			}			
		}
		if(!("1".equals(llglDto.getQybj()))) {
			//2.更新货物信息预定数
			boolean result_hw = hwxxService.updateHwxxDtos(hwxxDtos);
			if(!result_hw)
				throw new BusinessException("msg","货物预定数更新失败!");
			//3.更新仓库货物领料信息预定数，库存量
			boolean result_ck = ckhwxxService.updateCkhwxxs(ckhwxxDtos);
			if(!result_ck)
				throw new BusinessException("msg","仓库货物信息更新失败!");
		}	
		//4.更新货物领料信息实领数量
		boolean result_hwll = hwllxxDao.updateHwllxxDtos(hwllxxDtos) > 0;
		if(!result_hwll)
			throw new BusinessException("msg","货物领料信息实领数量更新失败!");
		
		LlglDto llglDto_t = getDtoById(llglDto.getLlid());
		//出库U8操作
		llglDto_t.setZsxm(llglDto.getZsxm());
		//判断是否是OA新增
		if(!"1".equals(llglDto_t.getLllx())&&!"4".equals(llglDto_t.getLllx()) && !"1".equals(systemreceiveflg) && StringUtil.isNotBlank(matridxdsflag)) {
			Map<String, Object> map = rdRecordService.addU8CkData(llglDto_t);
			@SuppressWarnings("unchecked")
			List<CkglDto> ckglDtos = (List<CkglDto>) map.get("ckglList");
			
			@SuppressWarnings("unchecked")
			List<CkmxDto> ckmxs = (List<CkmxDto>) map.get("ckmxList");
			boolean result_ck = ckmxService.updateCkmxList(ckmxs);
			if(!result_ck) {
				throw new BusinessException("msg","批量更新出库明细关联id失败！");
			}
			//更新出库关联id
			boolean result_upck = ckglService.updateCkgls(ckglDtos);
			if(!result_upck)
				throw new BusinessException("msg","出库id更新失败！");
		}
		//判断留样领料出库增加操作
		if("2".equals(llglDto.getLllx())||"4".equals(llglDto.getLllx())){
			List<LyrkxxDto> lyrkxxDtos=new ArrayList<>();
			for (HwllmxDto hwllmx : hwllmxlist) {
				LyrkxxDto lyrkxxDto=new LyrkxxDto();
				lyrkxxDto.setLyrkid(StringUtil.generateUUID());
				lyrkxxDto.setLlid(hwllmx.getLlid());
				lyrkxxDto.setHwid(hwllmx.getHwid());
				lyrkxxDto.setSl(hwllmx.getSlsl());
				lyrkxxDto.setLrry(llglDto.getSqry());
				lyrkxxDtos.add(lyrkxxDto);
			}
			boolean isSuccess = lyrkxxService.insertList(lyrkxxDtos);
			if(!isSuccess){
				throw new BusinessException("msg","留样入库信息新增失败！");
			}
			List<HwllmxDto> sumGroup = hwllmxService.getSumGroup(llglDto.getLlid());
			if(!CollectionUtils.isEmpty(sumGroup)) {
				List<LykcxxDto> updateList=new ArrayList<>();
				List<LykcxxDto> addList=new ArrayList<>();
				for(HwllmxDto dto:sumGroup){
					if(StringUtil.isNotBlank(dto.getLykcid())){
						LykcxxDto lykcxxDto=new LykcxxDto();
						lykcxxDto.setLykcid(dto.getLykcid());
						lykcxxDto.setKcl(dto.getZslsl());
						lykcxxDto.setKclbj("1");
						lykcxxDto.setXgry(llglDto.getSqry());
						updateList.add(lykcxxDto);
					}else{
						LykcxxDto lykcxxDto=new LykcxxDto();
						lykcxxDto.setLykcid(StringUtil.generateUUID());
						lykcxxDto.setWlid(dto.getWlid());
						lykcxxDto.setScph(dto.getScph());
						lykcxxDto.setScrq(dto.getScrq());
						lykcxxDto.setYxq(dto.getYxq());
						lykcxxDto.setKcl(dto.getZslsl());
						lykcxxDto.setLrry(llglDto.getSqry());
						addList.add(lykcxxDto);
					}
				}
				if(!CollectionUtils.isEmpty(updateList)){
					isSuccess=lykcxxService.updateKclListByBj(updateList);
					if(!isSuccess){
						throw new BusinessException("msg","留样库存信息修改失败！");
					}
				}
				if(!CollectionUtils.isEmpty(addList)){
					isSuccess=lykcxxService.insertList(addList);
					if(!isSuccess){
						throw new BusinessException("msg","留样库存信息新增失败！");
					}
				}
			}
		}
		return true;
	}

	/**
	 * 自动生成记录编号
	 * @param
	 * @return
	 */
	@Override
	public String generateJlbh() {
		// TODO Auto-generated method stub
		// 生成规则: LL-20201022-01 LL-年份日期-流水号 。
		String date = DateUtils.getCustomFomratCurrentDate("yyyy-MMdd");
		String prefix = "Q" + "-" + date + "-";
		// 查询流水号
		String serial = dao.getJlbhSerial(prefix);
		return prefix + serial;
	}

	/**
	 * 领料新增自动生成领料编号
	 */
	@Override
	public String generateLljlbh(LlglDto llglDto) {
		// 生成规则: IT-20210820-01    部门参数扩展-年份日期-流水号 。
		String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		String prefix = llglDto.getJgdh() + "-" + date + "-";
		//查询流水号
		String serial = dao.generateLljlbh(prefix);
		return prefix+serial;
	}

	@Override
	public String getlljgdh(LlglDto llglDto) {
		// TODO Auto-generated method stub
		return dao.getlljgdh(llglDto);
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
		Map<String, Object> map = new HashMap<>();
		LlglDto llglDto = dao.getDtoById(shgcDto.getYwid());
		map.put("jgid",llglDto.getSqbm());
		return map;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
		Map<String, Object> map =new HashMap<>();
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>)param.get("ywids");
		LlglDto llglDto = new LlglDto();
		llglDto.setIds(ids);
		List<LlglDto> dtoList = dao.getDtoList(llglDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(LlglDto dto:dtoList){
				list.add(dto.getLlid());
			}
		}
		map.put("list",list);
		return map;
	}

	@Override
	public boolean callbackLlglAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException {
		String result=obj.getString("result");//正常结束时result为agree，拒绝时result为refuse，审批终止时没这个值，redirect转交
		String type=obj.getString("type");//审批正常结束（同意或拒绝）的type为finish，审批终止的type为terminate
		String processInstanceId=obj.getString("processInstanceId");//审批实例id
		String staffId=obj.getString("staffId");//审批人钉钉ID
		String remark=obj.getString("remark");//审核意见
		String content = obj.getString("content");//评论
		String finishTime=obj.getString("finishTime");//审批完成时间
		String title= obj.getString("title");
		String processCode=obj.getString("processCode");
		String wbcxidString  = obj.getString("wbcxid"); //获取外部程序id
		log.error("回调参数获取---------result:"+result+",type:"+type+",processInstanceId:"+processInstanceId+",staffId:"+staffId+",remark:"+remark+",finishTime"+finishTime+",外部程序id"+wbcxidString);
		//分布式服务器保存钉钉审批信息
		DdfbsglDto ddfbsglDto=new DdfbsglDto();
		ddfbsglDto.setProcessinstanceid(processInstanceId);
		ddfbsglDto=ddfbsglService.getDtoById(processInstanceId);
		DdspglDto ddspglDto=new DdspglDto();
		DdspglDto t_ddspglDto=new DdspglDto();
		t_ddspglDto.setProcessinstanceid(processInstanceId);
		t_ddspglDto.setType("finish");
		t_ddspglDto.setEventtype(DingTalkUtil.BPMS_TASK_CHANGE);
		List<DdspglDto> ddspgllist=ddspglService.getDtoList(t_ddspglDto);
		try {
//			int a=1/0;
			if(ddfbsglDto==null)
				throw new BusinessException("message","未获取到相应的钉钉分布式信息！");
			if(StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {			
				if(!CollectionUtils.isEmpty(ddspgllist)) {
					ddspglDto=ddspgllist.get(0);
				}
			}
			//根据钉钉审批实例ID查询关联领料单
			LlglDto llglDto =dao.getDtoByDdslid(processInstanceId);
			//若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
			if(llglDto!=null) {
				//获取审批人信息
				User user=new User();
				user.setDdid(staffId);
				user.setWbcxid(wbcxidString);
				user = commonService.getByddwbcxid(user);
				if (user == null)
					return false;
				//获取审批人角色信息
				List<LlglDto> dd_sprjs=dao.getSprjsBySprid(user.getYhid());
				// 获取当前审核过程
				ShgcDto t_shgcDto = shgcService.getDtoByYwid(llglDto.getLlid());
				if(t_shgcDto!=null) {
					// 获取的审核流程列表
					ShlcDto shlcParam = new ShlcDto();
					shlcParam.setShid(t_shgcDto.getShid());
					shlcParam.setGcid(t_shgcDto.getGcid());// 处理旧流程判断用
					@SuppressWarnings("unused")
					List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);

					if (("start").equals(type)) {
						//更新分布式管理表信息
						DdfbsglDto t_ddfbsglDto=new DdfbsglDto();
						t_ddfbsglDto.setProcessinstanceid(processInstanceId);
						t_ddfbsglDto.setYwlx(processCode);
						t_ddfbsglDto.setYwmc(title);
						ddfbsglService.update(t_ddfbsglDto);
					}
					if(!CollectionUtils.isEmpty(dd_sprjs)) {
						//审批正常结束（同意或拒绝）
						ShxxDto shxxDto=new ShxxDto();
						shxxDto.setGcid(t_shgcDto.getGcid());
						shxxDto.setLcxh(t_shgcDto.getXlcxh());
						shxxDto.setShlb(t_shgcDto.getShlb());
						shxxDto.setShyj(remark);
						shxxDto.setYwid(llglDto.getLlid());
						shxxDto.setYwglmc(llglDto.getLldh());
						if (StringUtil.isNotBlank(finishTime)){
							shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
						}
						String lastlcxh=null;//回退到指定流程

						if(("finish").equals(type)) {
							//如果审批通过,同意
							if(("agree").equals(result)) {
								log.error("同意------");
								shxxDto.setSftg("1");
								if(StringUtils.isBlank(t_shgcDto.getXlcxh()))
									throw new BusinessException("ICOM99019","现流程序号为空");
								lastlcxh=String.valueOf(Integer.parseInt(t_shgcDto.getXlcxh())+1);
							}
							//如果审批未通过，拒绝
							else if(("refuse").equals(result)) {
								log.error("拒绝------");
								shxxDto.setSftg("0");
								shxxDto.setLcxh(null);
								lastlcxh="1";
								shxxDto.setThlcxh(null);
							}
							//如果审批被转发
							else if(("redirect").equals(result)) {
								String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(finishTime) / 1000));
								log.error("DingTalkMaterPurchaseAudit(钉钉物料领料审批转发提醒)------转发人员:"+user.getZsxm()+",人员ID:"+user.getYhid()+",单据号:"+llglDto.getLldh()+",单据ID:"+llglDto.getLlid()+",转发时间:"+date);
								return true;
							}
							//调用审核方法
							Map<String, List<String>> map= ToolUtil.reformRequest(request);
							log.error("map:"+map);
							List<String> list=new ArrayList<>();
							list.add(llglDto.getLlid());
							map.put("llid", list);
							//若一个用户多个角色导致审核异常时
							for(int i=0;i<dd_sprjs.size();i++) {
								try {
									//取下一个角色
									user.setDqjs(dd_sprjs.get(i).getSprjsid());
									user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
									if(("refuse").equals(result)){
										shgcService.terminateAudit(shxxDto, user,request,lastlcxh,obj);
									}else{
										shgcService.doManyBackAudit(shxxDto, user,request,lastlcxh,obj);
									}
									//更新审批管理信息
									ddspglDto.setCljg("1");
									ddspglService.updatecljg(ddspglDto);
									break;
								} catch (Exception e) {
									log.error("callbackQgglAduit-Exception:" + e.getMessage());
									t_map.put("ddspglid", ddspglDto.getDdspglid());
									t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());

									if(i==dd_sprjs.size()-1)
										throw new BusinessException("ICOM99019",e.getMessage());
								}
							}
						}
						//撤销审批
						else if(("terminate").equals(type)) {
							//退回到采购部
							shxxDto.setThlcxh(null);
							shxxDto.setYwglmc(llglDto.getLldh());
							LlglDto llglDto1=new LlglDto();
							llglDto1.setLlid(llglDto.getLlid());
							dao.updateDdslidToNull(llglDto1);
							log.error("撤销------");
							shxxDto.setSftg("0");
							//调用审核方法
							Map<String, List<String>> map=ToolUtil.reformRequest(request);
							List<String> list=new ArrayList<>();
							list.add(llglDto.getLlid());
							map.put("llid", list);
							for(int i=0;i<dd_sprjs.size();i++) {
								try {
									//取下一个角色
									user.setDqjs(dd_sprjs.get(i).getSprjsid());
									user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
									shgcService.terminateAudit(shxxDto, user,request,lastlcxh,obj);
//										shgcService.doAudit(shxxDto, user,request);
									//更新审批管理信息
									ddspglDto.setCljg("1");
									ddspglService.updatecljg(ddspglDto);
									break;
								} catch (Exception e) {
									t_map.put("ddspglid", ddspglDto.getDdspglid());
									t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());

									if(i==dd_sprjs.size()-1)
										throw new BusinessException("ICOM99019",e.toString());
								}
							}
						}else if(("comment").equals(type)) {
							//评论时保存评论信息，添加至审核信息表
							ShgcDto shgc = shgcService.getDtoByYwid(shxxDto.getYwid());//获得已有审核过程
							ShxxDto shxx = new ShxxDto();
							String shxxid =StringUtil.generateUUID();
							shxx.setShxxid(shxxid);
							shxx.setSqr(shgc.getSqr());
							shxx.setLcxh(null);
							shxx.setShid(shgc.getShid());
							shxx.setSftg("1");
							shxx.setGcid(shgc.getGcid());
							shxx.setYwid(shxxDto.getYwid());
							shxx.setShyj("评论:"+content.substring(0, Math.min(content.length(), 6000)));//这里写死，备注钉钉审批被提交人员撤销了
							shxx.setLrry(user.getYhid());
							shxxService.insert(shxx);
						}
					}
				}else {
					if(("comment").equals(type)) {
						//评论时保存评论信息，添加至审核信息表
						ShxxDto shxx = new ShxxDto();
						String shxxid =StringUtil.generateUUID();
						shxx.setShxxid(shxxid);
						shxx.setSftg("1");
						shxx.setYwid(llglDto.getLlid());
						shxx.setShlb(t_shgcDto.getShlb());
						List<ShxxDto> shxxlist=shxxService.getShxxOrderByPass(shxx);
						if(!CollectionUtils.isEmpty(shxxlist)) {
							shxx.setShid(shxxlist.get(0).getShid());
							shxx.setShyj("评论:"+content.substring(0, Math.min(content.length(), 6000)));//这里写死，备注钉钉审批被提交人员撤销了
							shxx.setLrry(user.getYhid());
							shxxService.insert(shxx);
						}
					}
				}
			}
		}catch(BusinessException e) {
			log.error("BusinessException:" + e.getMessage());
			throw new BusinessException("ICOM99019",e.getMsg());
		}catch (Exception e) {
			log.error("Exception:" + e.getMessage());
			throw new BusinessException("ICOM99019",e.toString());
		}
		return true;
	}

	@Override
	public List<LlglDto> getLldhByIds(List<String> list) {
		return dao.getLldhByIds(list);
	}

	@Override
	public boolean updateLllx(LlglDto llglDto) {
		return dao.updateLllx(llglDto);
	}

	@Override
	public List<LlglDto> getLllxByIds(LlglDto llglDto) {
		return dao.getLllxByIds(llglDto);
	}

	@Override
	public void updateFzrByYfzr(LlglDto llglDto) {
		dao.updateFzrByYfzr(llglDto);
	}

	public boolean updateWlxx(LlglDto llglDto) {
		return dao.updateWlxx(llglDto);
	}

}
