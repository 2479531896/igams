package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.dingtalk.api.request.OapiV2UserGetRequest;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.SpgwDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
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
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.ISpgwService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.production.dao.entities.DbxxDto;
import com.matridx.igams.production.dao.entities.HtfktxDto;
import com.matridx.igams.production.dao.entities.HtglDto;
import com.matridx.igams.production.dao.entities.HtglModel;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.PO_PodetailsDto;
import com.matridx.igams.production.dao.entities.PO_PomainDto;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.dao.entities.ShysDto;
import com.matridx.igams.production.dao.entities.UA_IdentityDto;
import com.matridx.igams.production.dao.matridxsql.PO_PodetailsDao;
import com.matridx.igams.production.dao.matridxsql.PO_PomainDao;
import com.matridx.igams.production.dao.matridxsql.UA_IdentityDao;
import com.matridx.igams.production.dao.post.IHtglDao;
import com.matridx.igams.production.dao.post.IHtmxDao;
import com.matridx.igams.production.service.svcinterface.IDbxxService;
import com.matridx.igams.production.service.svcinterface.IHtfktxService;
import com.matridx.igams.production.service.svcinterface.IHtglService;
import com.matridx.igams.production.service.svcinterface.IHtmxService;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.production.service.svcinterface.IShysService;
import com.matridx.igams.production.util.ChineseYuanUtil;
import com.matridx.igams.production.util.XWPFContractUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.taobao.api.ApiException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
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
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class HtglServiceImpl extends BaseBasicServiceImpl<HtglDto, HtglModel, IHtglDao>
		implements IHtglService, IAuditService {

	@Autowired
	IHtmxService htmxService;
	@Value("${matridx.systemflg.contractemail:}")
	private String contractemail;
	@Value("${matridx.rabbit.systemreceiveflg:}")
	private String systemreceiveflg;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	@Autowired
	ICommonService commonservice;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	IShlcService shlcService;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.wechat.registerurl:}")
	private String registerurl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.fileupload.releasePath}")
	private String releaseFilePath;
	@Value("${matridx.fileupload.tempPath}")
	private String tempPath;
	@Value("${matridx.systemflg.systemname:}")
	private String systemname;
	@Autowired
	IShgcService shgcService;
	@Autowired
	PO_PomainDao pO_PomainDao;

	@Autowired
	PO_PodetailsDao pO_PodetailsDao;

	@Autowired
	IHtmxDao htmxDao;

	@Autowired
	IQgglService qgglService;

	@Autowired
	IQgmxService qgmxService;

	@Autowired
	IFjcfbService fjcfbService;
	
	@Autowired
	IDdfbsglService ddfbsglService;
	
	@Autowired
	IDdspglService ddspglService;
	
	@Autowired
	IShxxService shxxService;

	@Autowired
	IHtfktxService htfktxService;

	@Autowired
	IRdRecordService rdRecordService;

	@Autowired
	IXxdyService xxdyService;
	@Value("${matridx.ftp.url:}")
	private final String FTP_URL = null;

	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private final String DOC_OK = null;

	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	
	//是否发送rabbit标记     1：发送
  	@Value("${matridx.rabbit.systemconfigflg:}")
  	private String systemconfigflg;
  	@Value("${matridx.rabbit.preflg:}")
  	private String preRabbitFlg;
  	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	@Value("${matridx.accountSet.accountName:}")
	private String accountName;
	
	@Autowired
	UA_IdentityDao uA_IdentityDao;
	@Autowired
	IJgxxService jgxxService;
	@Value("${sqlserver.matridxds.flag:}")
	private String matridxdsflag;
	@Autowired
	private IXtszService xtszService;
	@Autowired
	private IShysService shysService;
	@Autowired
	private IDbxxService dbxxService;
	@Autowired
	private ISpgwService spgwService;


	private final Logger log = LoggerFactory.getLogger(HtglServiceImpl.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertDto(HtglDto htglDto) {
		if (StringUtil.isBlank(htglDto.getHtid())) {
			htglDto.setHtid(StringUtil.generateUUID());
		}
		if(StringUtil.isBlank(htglDto.getHtlx())) {
			htglDto.setHtlx("0");
		}
		htglDto.setZt(StatusEnum.CHECK_NO.getCode());
		int result = dao.insert(htglDto);
		return result != 0;
	}

	/**
	 * 导出
	 */
	@Override
	public int getCountForSearchExp(HtglDto htglDto, Map<String, Object> params) {
		return dao.getCountForSearchExp(htglDto);
	}

	/**
	 * 根据搜索条件获取导出信息
	 */
	public List<HtglDto> getListForSearchExp(Map<String, Object> params) {
		HtglDto htglDto = (HtglDto) params.get("entryData");
		queryJoinFlagExport(params, htglDto);
		return dao.getListForSearchExp(htglDto);
	}

	/**
	 * 根据选择信息获取导出信息
	 */
	public List<HtglDto> getListForSelectExp(Map<String, Object> params) {
		HtglDto htglDto = (HtglDto) params.get("entryData");
		queryJoinFlagExport(params, htglDto);
		return dao.getListForSelectExp(htglDto);
	}

	/**
	 * 导出
	 */
	@Override
	public int getCountForAuditingSearchExp(HtglDto htglDto, Map<String, Object> params) {
		return dao.getCountForAuditingSearchExp(htglDto);
	}
	/**
	 * 根据搜索条件获取审核导出信息
	 */
	public List<HtglDto> getListForAuditingSearchExp(Map<String, Object> params) {
		HtglDto htglDto = (HtglDto) params.get("entryData");
		queryJoinFlagExport(params, htglDto);
		return dao.getListForAuditingSearchExp(htglDto);
	}

	/**
	 * 根据选择信息获取审核导出信息
	 */
	public List<HtglDto> getListForAuditingSelectExp(Map<String, Object> params) {
		HtglDto htglDto = (HtglDto) params.get("entryData");
		queryJoinFlagExport(params, htglDto);
		return dao.getListForAuditingSelectExp(htglDto);
	}

	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String, Object> params, HtglDto htglDto) {
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
		htglDto.setSqlParam(sqlcs);
	}

	/**
	 * 计算金额
	 */
	private void calculateAmount(HtglDto htglDto, List<HtmxDto> htmxDtos) {
		// TODO Auto-generated method stub
		String zje = null;
		if (!CollectionUtils.isEmpty(htmxDtos)) {
			for (HtmxDto htmxDto : htmxDtos) {
				htmxDto.setDdrq(htglDto.getDdrq());
				String hjje = htmxDto.getHjje();
				if (StringUtil.isNotBlank(hjje)) {
					if (StringUtil.isBlank(zje)) {
						zje = hjje;
					} else {
						zje = String.valueOf(new BigDecimal(zje).add(new BigDecimal(hjje)));
					}
				}
				// 无税单价= 含税单价 / (1+ 税率/100)
				if (StringUtil.isNotBlank(htmxDto.getHsdj()) && StringUtil.isNotBlank(htglDto.getSl())) {
					BigDecimal sl = new BigDecimal(htglDto.getSl()).divide(new BigDecimal(100), 5,
							RoundingMode.HALF_UP);
					String wsdj = new BigDecimal(htmxDto.getHsdj())
							.divide(sl.add(new BigDecimal(1)), 5, RoundingMode.HALF_UP).toString();
					htmxDto.setWsdj(wsdj);
				}
			}
		}
		htglDto.setZje(zje);
	}

	/**
	 * 新增合同保存
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addSaveContract(HtglDto htglDto) throws BusinessException {
		//校验供应商资质过期时间
		checkGys(htglDto);
		// TODO Auto-generated method stub
		List<HtmxDto> htmxDtos = JSON.parseArray(htglDto.getHtmxJson(), HtmxDto.class);
		calculateAmount(htglDto, htmxDtos);
		List<String> wlids = htmxDtos.stream().map(HtmxDto::getWlid).collect(Collectors.toList());
		//修改请购单产品注册号
		List<QgmxDto> qgmxDtos = new ArrayList<>();
		if(!CollectionUtils.isEmpty(htmxDtos)) {
			for (HtmxDto htmxDto : htmxDtos) {
				htmxDto.setSffpwh("0");
				if (StringUtil.isBlank(htmxDto.getHtmxidbj())) {
					htmxDto.setHtmxidbj(StringUtil.generateUUID());
				}
				QgmxDto qgmxDto = new QgmxDto();
				qgmxDto.setQgmxid(htmxDto.getQgmxid());
				qgmxDto.setCpzch(htmxDto.getCpzch());
				qgmxDto.setXgry(htglDto.getLrry());
				qgmxDtos.add(qgmxDto);
			}
			boolean upCpzch = qgmxService.updateCpzch(qgmxDtos);
			if (!upCpzch){
				throw new BusinessException("msg", "修改请购明细产品注册号失败!");
			}
		}
		//委外订单做判断
		if ("2".equals(htglDto.getHtlx())){
			HtmxDto htmxDto_c = new HtmxDto();
			htmxDto_c.setWlids(wlids);
			boolean sfcz = true;
			List<HtmxDto> htmxDtos_c = htmxService.getCpjgByWlids(htmxDto_c);
			HashSet<String> strs = new HashSet<>();
			for (HtmxDto htmxDto : htmxDtos_c) {
				if (StringUtil.isBlank(htmxDto.getCpjgid())){
					sfcz = false;
					strs.add(htmxDto.getWlmc());
				}
			}
			String wlmc = StringUtils.join(strs, ",");
			if (!sfcz){
				throw new BusinessException("msg", "物料"+wlmc+"未做资料维护，不允许保存!");
			}
		}
		String str = JSON.toJSONString(htmxDtos);
		htglDto.setHtmxJson(str);
		// 双章标记默认为否
		htglDto.setSzbj("0");
		htglDto.setSffpwh("0");
		boolean result = insertDto(htglDto);
		if (!result)
			return false;

		// 文件复制到正式文件夹，插入信息至正式表
		if (!CollectionUtils.isEmpty(htglDto.getFjids())) {
			for (int i = 0; i < htglDto.getFjids().size(); i++) {
				fjcfbService.save2RealFile(htglDto.getFjids().get(i), htglDto.getHtid());
			}
		}

		// 更新合同明细
		htglDto.setXgry(htglDto.getLrry());
		htglDto.setScry(htglDto.getLrry());
		result = htmxService.updateHtmxDtos(htmxDtos, htglDto);
		if (!result)
			return false;
		//处理合同付款提醒
		List<HtfktxDto> htfktxDtos = JSON.parseArray(htglDto.getFktxJson(), HtfktxDto.class);
		if(!CollectionUtils.isEmpty(htfktxDtos)){
			HtfktxDto htfktxDto=new HtfktxDto();
			htfktxDto.setHtid(htglDto.getHtid());
			BigDecimal zje=new BigDecimal(htglDto.getZje());
			for (HtfktxDto dto : htfktxDtos) {
				//计算预付比例
				BigDecimal yfje = new BigDecimal(dto.getYfje());
				dto.setYfbl((yfje.divide(zje, 2, RoundingMode.HALF_UP)).toString());
				dto.setHtid(htglDto.getHtid());
			}
			htfktxService.saveContractPayRemind(htfktxDto,htfktxDtos);
		}
		return true;
	}

	private void checkGys(HtglDto htglDto) throws BusinessException {
		if (StringUtil.isNotBlank(htglDto.getGys())){
			FjcfbDto fjcfbDto=new FjcfbDto();
			fjcfbDto.setYwid(htglDto.getGys());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBSUPPLIER.getCode());
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			//是否过期
			boolean isGq = true;
			//格式错误
			boolean GSCW = false;
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
								isGq = false;
							}
						}else {
							GSCW = true;
						}
					}else {
						GSCW = true;
					}
				}
			}else {
				throw new BusinessException("msg", "供应商信息没有相关附件！请先上传相关附件再录入！<br>供应商资质附件格式：<br>例:文件名(2022.01.01-2023.01.01)!");
			}
			if (isGq){
				if (GSCW){
					throw new BusinessException("msg", "请修改供应商资质附件格式!<br>例:文件名(2022.01.01-2023.01.01)!");
				}
				throw new BusinessException("msg", "该供应商资质已过期，请上传该供应商最新资质！");
			}
		}
	}

	/**
	 * 修改合同保存
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean modSaveContract(HtglDto htglDto) throws BusinessException{
		checkGys(htglDto);

		// 文件复制到正式文件夹，插入信息至正式表
		if (!CollectionUtils.isEmpty(htglDto.getFjids())) {
			for (int i = 0; i < htglDto.getFjids().size(); i++) {
				fjcfbService.save2RealFile(htglDto.getFjids().get(i), htglDto.getHtid());
			}
		}

		// TODO Auto-generated method stub
		List<HtmxDto> htmxDtos = JSON.parseArray(htglDto.getHtmxJson(), HtmxDto.class);
		//修改请购单产品注册号
		List<QgmxDto> qgmxDtos = new ArrayList<>();
		if (!CollectionUtils.isEmpty(htmxDtos)) {
			calculateAmount(htglDto, htmxDtos);
			for (HtmxDto htmxDto : htmxDtos) {
				if (StringUtil.isBlank(htmxDto.getHtmxid())) {
					htmxDto.setHtmxidbj(StringUtil.generateUUID());
				}
				QgmxDto qgmxDto = new QgmxDto();
				qgmxDto.setQgmxid(htmxDto.getQgmxid());
				qgmxDto.setCpzch(htmxDto.getCpzch());
				qgmxDto.setXgry(htglDto.getXgry());
				qgmxDtos.add(qgmxDto);
			}
			qgmxService.updateCpzch(qgmxDtos);
		}
		String str = JSON.toJSONString(htmxDtos);
		htglDto.setHtmxJson(str);
		if ("0".equals(htglDto.getSfkj())){
			htglDto.setKjhtid(null);
			htglDto.setKjhtidnull("1");
		}
		int count = dao.updateContract(htglDto);
		if (count == 0){
			return false;
		}
		//补充合同不修改请购信息
		if (StringUtil.isBlank(htglDto.getBchtid())){
			boolean updateQgmx;
			//更新请购明细合同日期信息
			List<HtmxDto> selectHtmxDtos=htmxDtos;
			if(CollectionUtils.isEmpty(selectHtmxDtos)) {
				selectHtmxDtos = htmxService.getListByHtid(htglDto.getHtid());
				if(!CollectionUtils.isEmpty(selectHtmxDtos)) {
					for (HtmxDto selectHtmxDto : selectHtmxDtos) {
						selectHtmxDto.setDdrq(htglDto.getDdrq());
					}
				}
			}
			updateQgmx=htmxService.updateQgmxDtos(selectHtmxDtos);
			if(!updateQgmx){
				return false;
			}
		}
		htglDto.setLrry(htglDto.getXgry());
		htglDto.setScry(htglDto.getXgry());
		boolean result = htmxService.updateHtmxDtos(htmxDtos, htglDto);
		if (!result){
			return false;
		}
		//处理合同付款提醒
		List<HtfktxDto> htfktxDtos = JSON.parseArray(htglDto.getFktxJson(), HtfktxDto.class);
		if(!CollectionUtils.isEmpty(htfktxDtos)){
			HtfktxDto htfktxDto=new HtfktxDto();
			htfktxDto.setHtid(htglDto.getHtid());
			BigDecimal zje=new BigDecimal(htglDto.getZje());
			for (HtfktxDto dto : htfktxDtos) {
				//计算预付比例
				BigDecimal yfje = new BigDecimal(dto.getYfje());
				dto.setYfbl((yfje.divide(zje, 2, RoundingMode.HALF_UP)).toString());
				dto.setHtid(htglDto.getHtid());
			}
			htfktxService.deleteByHtid(htfktxDto);
			htfktxService.saveContractPayRemind(htfktxDto,htfktxDtos);
		}
		return true;
	}

	/**
	 * 合同高级修改保存
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean advancedModSaveContract(HtglDto htglDto) throws BusinessException {
		// TODO Auto-generated method stub
		HtglDto htglDto_h = getDtoById(htglDto.getHtid());
		if(!"80".equals(htglDto_h.getZt())) {
			return modSaveContract(htglDto);
		}

		// 文件复制到正式文件夹，插入信息至正式表
		if (!CollectionUtils.isEmpty(htglDto.getFjids())) {
			for (int i = 0; i < htglDto.getFjids().size(); i++) {
				fjcfbService.save2RealFile(htglDto.getFjids().get(i), htglDto.getHtid());
			}
		}

		String qgid = StringUtils.substringBefore(htglDto.getQgid(), ",");
		QgglDto qgglDto = qgglService.getDtoById(qgid);
		String djh = qgglDto.getDjh();
		htglDto.setU8cgid(djh);
		List<HtmxDto> htmxDtos = JSON.parseArray(htglDto.getHtmxJson(), HtmxDto.class);
		calculateAmount(htglDto, htmxDtos);
		//修改请购单产品注册号
		List<QgmxDto> qgmxDtos = new ArrayList<>();
		if(!CollectionUtils.isEmpty(htmxDtos)) {
			for (HtmxDto htmxDto : htmxDtos) {
				if (StringUtil.isBlank(htmxDto.getHtmxid())) {
					htmxDto.setHtmxidbj(StringUtil.generateUUID());
				}
				QgmxDto qgmxDto = new QgmxDto();
				qgmxDto.setQgmxid(htmxDto.getQgmxid());
				qgmxDto.setCpzch(htmxDto.getCpzch());
				qgmxDto.setXgry(htglDto.getXgry());
				qgmxDtos.add(qgmxDto);
			}
			qgmxService.updateCpzch(qgmxDtos);
		}
		String str = JSON.toJSONString(htmxDtos);
		htglDto.setHtmxJson(str);
		int count = dao.updateContract(htglDto);
		if (count == 0)
			return false;
		htglDto.setLrry(htglDto.getXgry());
		htglDto.setScry(htglDto.getXgry());
		List<HtmxDto> addHtmxDtos = new ArrayList<>();
		// 明细不能为空
		if (!CollectionUtils.isEmpty(htmxDtos) && StringUtil.isNotBlank(htglDto.getHtid())) {
			List<HtmxDto> selectHtmxDtos = htmxService.getListByHtid(htglDto.getHtid());
			// 获取新增的合同明细
			for (int i = htmxDtos.size() - 1; i >= 0; i--) {
				htmxDtos.get(i).setHtid(htglDto.getHtid());
				htmxDtos.get(i).setXgry(htglDto.getXgry());
				htmxDtos.get(i).setLrry(htglDto.getLrry());
				htmxDtos.get(i).setScry(htglDto.getScry());
				htmxDtos.get(i).setSuil(htglDto.getSl());
				//处理原计划到货日期
				for(HtmxDto htmxDto : selectHtmxDtos){
					if(htmxDto.getHtmxid().equals(htmxDtos.get(i).getHtmxid()))
						htmxDtos.get(i).setYjhdhrq(htmxDto.getJhdhrq());
				}
				if (StringUtils.isBlank(htmxDtos.get(i).getHtmxid())) {
					if(StringUtils.isNotBlank(htmxDtos.get(i).getQgid())){
						QgglDto qgglDto_q = qgglService.getDtoById(htmxDtos.get(i).getQgid());
						htmxDtos.get(i).setXmcsdm(qgglDto_q.getXmbmdm());
						htmxDtos.get(i).setFcsdm(qgglDto_q.getXmdldm());
						htmxDtos.get(i).setXmcsmc(qgglDto_q.getXmbmmc());
					}
					htmxDtos.get(i).setHtmxid(StringUtil.generateUUID());
					addHtmxDtos.add(htmxDtos.get(i));
					htmxDtos.remove(i);
				}
			}
			// 获取删除的合同明细
			List<String> delHtmxids = new ArrayList<>();
			List<HtmxDto> delHtmxDtos = new ArrayList<>();
			for (int i = selectHtmxDtos.size() - 1; i >= 0; i--) {
				boolean isDel = true;
				for (HtmxDto htmxDto : htmxDtos) {
					if (selectHtmxDtos.get(i).getHtmxid().equals(htmxDto.getHtmxid())) {
						htmxDto.setU8mxid(selectHtmxDtos.get(i).getU8mxid());
						isDel = false;
						break;
					}
				}
				if (isDel) {
					delHtmxids.add(selectHtmxDtos.get(i).getHtmxid());
					selectHtmxDtos.get(i).setXgry(htglDto.getXgry());
					delHtmxDtos.add(selectHtmxDtos.get(i));
				}
			}
			boolean result = htmxService.insertHtmxDtos(addHtmxDtos);
			if (!result)
				return false;
			HtmxDto htmxDto = new HtmxDto();
			htmxDto.setIds(delHtmxids);
			htmxDto.setScbj("1");
			htmxDto.setScry(htglDto.getScry());
			result = htmxService.deleteHtmxDtos(htmxDto);
			if (!result)
				return false;
			// 获取修改的合同明细信息
			List<HtmxDto> preHtmxDtos = null;
			if (!CollectionUtils.isEmpty(htmxDtos)) {
				preHtmxDtos = htmxService.getUpdateHtmxDtos(htmxDtos);
				// 获取修改的合同明细(剩余的htmxDtos)
				result = htmxService.updateHtmxDtos(htmxDtos);
				if (!result)
					return false;
			}
			//补充合同不修改请购数量
			if (StringUtil.isBlank(htglDto.getBchtid())){
				// 审核通过后请购明细数量修改
				result = updatePurchaseLastQuantity(addHtmxDtos, delHtmxDtos, preHtmxDtos);
			}
			if (!result)
				return false;
		}
		// 判断u8标记，0：不存U8
		if (!"1".equals(systemreceiveflg) && StringUtils.isNotBlank(htglDto_h.getU8poid())&&!CollectionUtils.isEmpty(htmxDtos)) {
			// 保存到u8
			PO_PomainDto pO_PomainDto = new PO_PomainDto();
			pO_PomainDto.setPOID(Integer.parseInt(htglDto_h.getU8poid()));// u8主键
			pO_PomainDto.setcVenCode(htglDto.getGysdm()); // 供应商代码
			pO_PomainDto.setcDepCode(htglDto.getJqdm()); // 申请部门代码
			pO_PomainDto.setCexch_name(htglDto.getBizmc()); // 币种
			pO_PomainDto.setiTaxRate(htglDto.getSl()); // 税率
			pO_PomainDto.setcMemo(htglDto.getBz()); // 备注
			pO_PomainDto.setCmaketime(htglDto.getSqrq()); // 申请日期
			pO_PomainDto.setcDefine1(djh);
			pO_PomainDto.setCappcode(djh); // 记录编号
			pO_PomainDto.setcPOID(htglDto.getHtnbbh()); // cpoid = cjrq+serial
			pO_PomainDto.setCsysbarcode("||pupo|" + htglDto.getHtnbbh());
			pO_PomainDao.update(pO_PomainDto);
			for (HtmxDto addHtmxDto : addHtmxDtos) {
				//判断合同明细的类型是否为物料
				if ("MATERIAL".equals(addHtmxDto.getQglbdm()) || StringUtils.isNotBlank(addHtmxDto.getWlid())) {
					PO_PodetailsDto pO_PodetailsDto = u8AmountCalculate(htglDto, new PO_PodetailsDto(), addHtmxDto);
					UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
					uA_IdentityDto.setcAcc_Id(accountName); //存入账套
					uA_IdentityDto.setcVouchType("Pomain");    //存入表标识
					// 为空，设关联ID POID为1，不为空，设关联ID POID为最大值+1
					UA_IdentityDto uA_IdentityDto_s = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
					if (uA_IdentityDto_s != null) {
						if (StringUtil.isNotBlank(uA_IdentityDto_s.getiChildId())) {
							// 不为空，设关联ID POID为最大值+1
							int id_s = Integer.parseInt(uA_IdentityDto_s.getiChildId());
							Integer id = id_s + 1;
							pO_PodetailsDto.setID(id_s + 1000000001);
							uA_IdentityDto.setiChildId(String.valueOf(id)); //存入副表最大值
						}
					} else {
						// 为空，设关联ID POID为1
						pO_PodetailsDto.setID(1000000001);
						uA_IdentityDto.setiChildId("1"); //存入副表最大值
					}
					pO_PodetailsDto.setPOID(Integer.parseInt(htglDto_h.getU8poid()));
					pO_PodetailsDto.setcInvCode(addHtmxDto.getWlbm()); // 物料编码
					pO_PodetailsDto.setCbMemo(addHtmxDto.getBz()); // 备注
					pO_PodetailsDto.setiPerTaxRate(addHtmxDto.getSuil()); // 税率
					pO_PodetailsDto.setdArriveDate(addHtmxDto.getJhdhrq()); // 计划到货日期
					pO_PodetailsDto.setCupsocode(pO_PomainDto.getcPOID()); // 记录编号

					Integer numXh = pO_PodetailsDao.getXhMax(pO_PodetailsDto);
					if (numXh == null) {
						// 为空，设序号为1
						pO_PodetailsDto.setIvouchrowno("1");
					} else {
						int ivo = numXh + 1;
						// 不为空，设序号为最大值+1
						pO_PodetailsDto.setIvouchrowno(Integer.toString(ivo));
					}
					pO_PodetailsDto.setCbsysbarcode(pO_PomainDto.getCsysbarcode() + "|" + pO_PodetailsDto.getIvouchrowno()); // 系统用
					pO_PodetailsDto.setiAppIds(String.valueOf(addHtmxDto.getGlid())); // 关联请购单详细里的AutoID(glid)
					pO_PodetailsDto.setiInvNum("0");
					pO_PodetailsDto.setcItemCode(addHtmxDto.getXmcsdm());
					pO_PodetailsDto.setcItem_class(addHtmxDto.getFcsdm());
					pO_PodetailsDto.setcItemName(addHtmxDto.getXmcsmc());
					pO_PodetailsDto.setcSource("app");
					pO_PodetailsDto.setIordertype("0");
					pO_PodetailsDto.setBgift("0");
					pO_PodetailsDto.setSoType("0");
					pO_PodetailsDto.setbTaxCost("1");
					pO_PodetailsDto.setiBG_Ctrl(null);
					pO_PodetailsDao.insert(pO_PodetailsDto);
					//更新合同明细U8关联ID
					addHtmxDto.setU8mxid(String.valueOf(pO_PodetailsDto.getID()));
					htmxService.updateU8mxid(addHtmxDto);
					//更新副表id最大值
					uA_IdentityDao.update(uA_IdentityDto);
				}
			}
			for (HtmxDto htmxDto : htmxDtos) {
				if ("MATERIAL".equals(htmxDto.getQglbdm()) || StringUtils.isNotBlank(htmxDto.getWlid())) {
					if (StringUtils.isNotBlank(htmxDto.getU8mxid()) && StringUtils.isNotBlank(htglDto_h.getU8poid())) {
						PO_PodetailsDto pO_PodetailsDto = new PO_PodetailsDto();
						pO_PodetailsDto.setID(Integer.parseInt(htmxDto.getU8mxid()));
						pO_PodetailsDto.setPOID(Integer.parseInt(htglDto_h.getU8poid()));
						// u8合同明细金额计算
						u8AmountCalculate(htglDto_h, pO_PodetailsDto, htmxDto);
						pO_PodetailsDto.setcInvCode(htmxDto.getWlbm()); // 物料编码
						pO_PodetailsDto.setCbMemo(htmxDto.getBz()); // 备注
						pO_PodetailsDto.setiPerTaxRate(htmxDto.getSuil()); // 税率
						pO_PodetailsDto.setdArriveDate(htmxDto.getJhdhrq()); // 计划到货日期
						pO_PodetailsDao.update(pO_PodetailsDto);
					}
				}
			}
		}
		//处理合同付款提醒
		List<HtfktxDto> htfktxDtos = JSON.parseArray(htglDto.getFktxJson(), HtfktxDto.class);
		if(!CollectionUtils.isEmpty(htfktxDtos)){
			HtfktxDto htfktxDto=new HtfktxDto();
			htfktxDto.setHtid(htglDto.getHtid());
			BigDecimal zje=new BigDecimal(htglDto.getZje());
			for (HtfktxDto dto : htfktxDtos) {
				//计算预付比例
				BigDecimal yfje = new BigDecimal(dto.getYfje());
				dto.setYfbl((yfje.divide(zje, 2, RoundingMode.HALF_UP)).toString());
				dto.setHtid(htglDto.getHtid());
			}
			htfktxService.deleteByHtid(htfktxDto);
			htfktxService.saveContractPayRemind(htfktxDto,htfktxDtos);
		}
		return true;
	}

	/**
	 * 审核通过后请购明细数量修改
	 */
	private boolean updatePurchaseLastQuantity(List<HtmxDto> addHtmxDtos, List<HtmxDto> delHtmxDtos,
			List<HtmxDto> preHtmxDtos) {
		// TODO Auto-generated method stub
		// 新增合同明细请购数量修改(审核后) sysl -= sl
		boolean result = htmxService.updateQuantityAddLast(addHtmxDtos);
		if (!result)
			return false;
		// 删除合同明细请购数量修改 sysl += sl
		result = htmxService.updateQuantityDelLast(delHtmxDtos);
		if (!result)
			return false;
		//修改删除的明细的请购单的完成标记
		if(!CollectionUtils.isEmpty(delHtmxDtos)) {
			//根据明细id获取请购单
			StringBuilder ids = new StringBuilder();
			for (HtmxDto htmxDto : delHtmxDtos) {
				ids.append(",").append(htmxDto.getQgmxid());
			}
			ids = new StringBuilder(ids.substring(1));
			//根据请购明细id更新请购完成标记，同一更新为未完成0
			QgmxDto qgmxDto = new QgmxDto();
			qgmxDto.setIds(ids.toString());
			List<HtmxDto> qgmxList = qgmxService.getListByQgmxids(qgmxDto,"1");
			if(!CollectionUtils.isEmpty(qgmxList)) {
				StringBuilder qg_ids = new StringBuilder();
				for (HtmxDto qgmxDto_t : qgmxList) {
					qg_ids.append(",").append(qgmxDto_t.getQgid());
				}
				qg_ids = new StringBuilder(qg_ids.substring(1));
				QgglDto qggl = new QgglDto();
				qggl.setIds(qg_ids.toString());
				qggl.setWcbj("0");
				boolean resulr_qg = qgglService.updateByQgmxid(qggl);
				if(!resulr_qg)
					return false;
			}
		}
		// 修改合同明细请购数量修改 sysl -= sl-presl
		return htmxService.updateQuantityAddLast(preHtmxDtos);
	}

	/**
	 * u8金额参数计算组装
	 */
	private PO_PodetailsDto u8AmountCalculate(HtglDto htglDto, PO_PodetailsDto pO_PodetailsDto, HtmxDto htmxDto) {
		// TODO Auto-generated method stub
		pO_PodetailsDto.setiQuantity(htmxDto.getSl()); // 数量
		pO_PodetailsDto.setiUnitPrice(htmxDto.getWsdj()); // 无税单价
		pO_PodetailsDto.setiNatUnitPrice(htmxDto.getWsdj()); // 无税单价
		pO_PodetailsDto.setiTaxPrice(htmxDto.getHsdj()); // 含税单价
		
		if(StringUtil.isNotBlank(htmxDto.getHjje())) {
			BigDecimal hjje = new BigDecimal(htmxDto.getHjje());
			pO_PodetailsDto.setiSum(htmxDto.getHjje()); // 含税总金额
			pO_PodetailsDto.setiNatSum(htmxDto.getHjje()); // 含税总金额
			
			// 无税总金额 = 含税总金额 /(1+税率/100)
			BigDecimal suil = new BigDecimal(htglDto.getSl()).divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
			BigDecimal wszje = hjje.divide(suil.add(new BigDecimal("1")), 2, RoundingMode.HALF_UP);
			pO_PodetailsDto.setiMoney(String.valueOf(wszje)); // 无税总额
			pO_PodetailsDto.setiNatMoney(String.valueOf(wszje)); // 无税总额
			
			// 总税额 = 含税总金额 - 无税总金额
			BigDecimal zse = hjje.subtract(wszje);
			pO_PodetailsDto.setiTax(String.valueOf(zse)); // 总税额
			pO_PodetailsDto.setiNatTax(String.valueOf(zse)); // 总税额
		}
		return pO_PodetailsDto;

	}

	/**
	 * 生成合同内部编号
	 */
	@Override
	public Map<String, Object> generateContractCode(HtglDto htglDto) {
		Map<String, Object> map = new HashMap<>();
		// TODO Auto-generated method stub
		// 生成规则：合同类型参数扩展一+ - + 部门参数扩展一+合同类型参数扩展三+ - +年月日+两位流水号。
		String date = DateUtils.getCustomFomratCurrentDate("yyMMdd");
		String prefix;
		// 判断业务类型为服务的合同，服务的业务类型
		JcsjDto jcsjDto = jcsjService.getDtoById(htglDto.getYwlx());
		if (StringUtil.isBlank(jcsjDto.getCskz1())){
			map.put("status", "fail");
			map.put("message", "未配置基础数据参数扩展1");
			return map;
		}
		User user = new User();
		user.setYhid(htglDto.getFzr());
		User userInfoById = commonservice.getUserInfoById(user);
		JgxxDto jgxxDto = new JgxxDto();
		jgxxDto.setJgid(userInfoById.getJgid());
		JgxxDto dtoById = jgxxService.selectJgxxByJgid(jgxxDto);
		if (StringUtil.isBlank(dtoById.getKzcs1())){
			map.put("status", "fail");
			map.put("message", "未配置机构参数扩展1");
			return map;
		}
		// 查询流水号
		String serial;
		//补充合同 生成规则：选中的合同的合同编号+- + 选中合同的一位流水+(+当前月日+),如：MDL-GQ-230405-03-1(0910)
		if (StringUtil.isNotBlank(htglDto.getYyhtnbbh())){
			prefix  = htglDto.getYyhtnbbh() + "-";
			serial = dao.getContractSerial(prefix,"1");
		}else {
			prefix = jcsjDto.getCskz1() + "-"  + dtoById.getKzcs1();
			if (StringUtil.isNotBlank(jcsjDto.getCskz3())){
				prefix = prefix + jcsjDto.getCskz3();
			}
			prefix = prefix + "-" + date + "-";
			serial = dao.getContractSerial(prefix,"2");
		}
		//框架合同不查询U8
		if (!"3".equals(htglDto.getHtlx())&&StringUtil.isBlank(htglDto.getYyhtnbbh())){
			// 查询u8流水号
			String u8Serial = pO_PomainDao.getContractSerial(prefix,"2");
			if (Integer.parseInt(u8Serial) > Integer.parseInt(serial)) {
				serial = u8Serial;
			}
		}
		if (StringUtil.isNotBlank(htglDto.getYyhtnbbh())){
			serial = serial + "("+DateUtils.getCustomFomratCurrentDate("yyMMdd")+")";
		}
		map.put("status", "success");
		map.put("message", prefix + serial);
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam)
			throws BusinessException {
		HtglDto htglDto = (HtglDto) baseModel;
		htglDto.setYwlx(htglDto.getHtywlx());
		htglDto.setXgry(operator.getYhid());
		htglDto.setShsj(operator.getXgsj());
		// 判断合同内部编号是否重复
		boolean isrename = isHtnbbhRepeat(htglDto.getHtnbbh(), htglDto.getHtid());
		if(!isrename) {
			throw new BusinessException("msg","合同内部编号不允许重复!");
		}
		boolean isSuccess;
		//是否钉钉审批
		if ("0".equals(htglDto.getSfddsp())){
			if ("3".equals(htglDto.getHtlx())){
				isSuccess = modFrameworkSaveContract(htglDto);
			}else {
				isSuccess = modSaveContract(htglDto);
				if("1".equals(systemconfigflg) && isSuccess) {
					htglDto.setPrefixFlg(prefixFlg);
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htgl.update",JSONObject.toJSONString(htglDto));
				}
			}
		}else {
			isSuccess = updateContract(htglDto)>0;
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
//		String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
		String ICOMM_SH00004 = xxglService.getMsg("ICOMM_SH00004");
		String ICOMM_SH00005 = xxglService.getMsg("ICOMM_SH00005");
		String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
		String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
		String ICOMM_SH00041 = xxglService.getMsg("ICOMM_SH00041");
		String ICOMM_SH00042 = xxglService.getMsg("ICOMM_SH00042");
		for (ShgcDto shgcDto : shgcList) {
			HtglDto htglDto = new HtglDto();

			htglDto.setHtid(shgcDto.getYwid());

			htglDto.setXgry(operator.getYhid());
			HtglDto htglDto_t = getDtoById(htglDto.getHtid());
			htglDto_t.setSqbm(htglDto_t.getSqbm());
			htglDto.setBchtid(htglDto_t.getBchtid());
			List<SpgwcyDto> spgwcyDtos = commonservice.siftJgList(shgcDto.getSpgwcyDtos(), htglDto_t.getSqbm());
			// 审核退回
			if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
				//由于合同提交时直接发起了钉钉审批，所以审核不通过时直接采用钉钉的审批拒绝消息即可
				htglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				// 发送钉钉消息
				if (!CollectionUtils.isEmpty(spgwcyDtos)) {
					for (SpgwcyDto spgwcyDto : spgwcyDtos) {
						if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
							talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00026,
									xxglService.getMsg("ICOMM_SH00040", operator.getZsxm(), shgcDto.getShlbmc(),
											htglDto_t.getHtnbbh(), htglDto_t.getYwlxmc(), htglDto_t.getSqbmmc(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
				//OA取消审批的同时组织钉钉审批
				if (StringUtils.isNotBlank(htglDto_t.getDdslid())) {
					Map<String, Object> cancelResult = talkUtil.cancelDingtalkAudit(operator.getYhm(), htglDto_t.getDdslid(), shgcDto.getShxx().getShyj(), operator.getDdid());
					//若撤回成功将实例ID至为空
					String success = String.valueOf(cancelResult.get("message"));
					@SuppressWarnings("unchecked")
					Map<String, Object> result_map = JSON.parseObject(success, Map.class);
					boolean bo1 = (boolean) result_map.get("success");
					if (bo1)
						dao.updateDdslidToNull(htglDto_t);
				}
				// 审核通过
			} else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
				if(StringUtil.isNotBlank(htglDto_t.getBchtid())){
					HtglDto htglDto_D = new HtglDto();
					htglDto_D.setBchtid(htglDto_t.getBchtid());
					htglDto_D.setHtid(htglDto_t.getHtid());
					HtglDto htglDtoT = dao.queryBchtZje(htglDto_D);
					htglDto_D.setXzje(htglDtoT.getXzje());
					htglDto_D.setHtid(htglDto_t.getBchtid());
					update(htglDto_D);
				}
				htglDto.setZt(StatusEnum.CHECK_PASS.getCode());
				//框架合同没有总金额
				if (!"3".equals(htglDto_t.getHtlx())) {
					//判断合同总金额是否为0，为0：更新完成标记
					if (new BigDecimal(htglDto_t.getZje()).setScale(2, RoundingMode.HALF_UP).compareTo(new BigDecimal("0")) == 0) {
						htglDto.setWcbj("1");
					}
				}
				// 判断是否需要重新生成内部编号
				if (StringUtil.isBlank(htglDto_t.getHtnbbh())) {
					String date = DateUtils.getCustomFomratCurrentDate("yyMMdd");
					String prefix;
					// 判断业务类型为服务的合同，服务的业务类型
					JcsjDto jcsjDto = jcsjService.getDtoById(htglDto_t.getYwlx());
					JgxxDto jgxxDto = new JgxxDto();
					jgxxDto.setJgid(htglDto_t.getSqbm());
					JgxxDto dtoById = jgxxService.selectJgxxByJgid(jgxxDto);
					// 查询流水号
					String serial;
					//补充合同 生成规则：选中的合同的合同编号+- + 选中合同的一位流水+(+当前月日+),如：MDL-GQ-230405-03-1(0910)
					if (StringUtil.isNotBlank(htglDto_t.getYyhtnbbh())){
						prefix  = htglDto_t.getYyhtnbbh() + "-";
						serial = dao.getContractSerial(prefix,"1");
					}else {
						prefix = jcsjDto.getCskz1() + "-"  + dtoById.getKzcs1();
						if (StringUtil.isNotBlank(jcsjDto.getCskz3())){
							prefix = prefix + jcsjDto.getCskz3();
						}
						prefix = prefix + "-" + date + "-";
						serial = dao.getContractSerial(prefix,"2");
					}
					//框架合同 和 补充合同 不查询U8
					if (!"3".equals(htglDto_t.getHtlx())&&StringUtil.isBlank(htglDto_t.getYyhtnbbh())){
						// 查询u8流水号
						String u8Serial = pO_PomainDao.getContractSerial(prefix,"2");

						if (Integer.parseInt(u8Serial) > Integer.parseInt(serial)) {
							serial = u8Serial;
						}
					}
					if (StringUtil.isNotBlank(htglDto_t.getYyhtnbbh())){
						serial = serial + "("+DateUtils.getCustomFomratCurrentDate("MMdd")+")";
					}
					htglDto.setHtnbbh(prefix + serial);
					htglDto_t.setHtnbbh(prefix + serial);
					if (StringUtil.isBlank(htglDto.getHtwbbh())) {
						htglDto.setHtwbbh(prefix + serial);
						htglDto_t.setHtwbbh(prefix + serial);
					}
				}
				//如果是普通合同的补充合同 判断是否修改行状态
				if (StringUtil.isNotBlank(htglDto_t.getBchtid())&&!"3".equals(htglDto_t.getHtlx())&&!"1".equals(htglDto_t.getHtlx())){
					List<HtmxDto> htmxDtos = htmxService.getOriginalContractU8(htglDto.getHtid());
					List<String> ids = new ArrayList<>();
					List<String> glids = new ArrayList<>();
					HtmxDto htmxDto_up = new HtmxDto();
					for (HtmxDto htmxDto : htmxDtos) {
						if ("0".equals(htmxDto.getSfty())){
							htmxDto_up.setHtlx(htmxDto.getHtlx());
							ids.add(htmxDto.getBchtmxid());
							if ("0".equals(htmxDto.getHtlx())){
								htmxDto_up.setU8poid(htmxDto.getU8poid());
								if (StringUtil.isNotBlank(htmxDto.getU8mxid())){
									glids.add(htmxDto.getU8mxid());
								}
							}else {
								htmxDto_up.setU8omid(htmxDto.getU8omid());
								if (StringUtil.isNotBlank(htmxDto.getU8ommxid())){
									glids.add(htmxDto.getU8ommxid());
								}
							}
						}
					}
					if (!CollectionUtils.isEmpty(glids)){
						htmxDto_up.setIds(ids);
						htmxDto_up.setGlids(glids);
						htmxDto_up.setGbrymc(operator.getZsxm());
						htmxDto_up.setGbry(operator.getYhid());
						htmxService.closeContracts(htmxDto_up);
					}
				}
				//框架合同不处理请购数据和U8数据
				if (!"3".equals(htglDto_t.getHtlx())&&StringUtil.isBlank(htglDto_t.getBchtid())) {
					List<HtmxDto> htmxDtos = htmxService.getListByHtid(htglDto.getHtid());
					// 审核通过时修改请购明细数量
					boolean result = updatePurchaseCompQuantity(htmxDtos, operator.getYhid(), htglDto_t);
					if (!result)
						throw new BusinessException("msg", "请购明细数量修改失败！");
					boolean sfscu8qgd = false;//是否生成U8合同(若合同明细中有物料则生成)
					if (!CollectionUtils.isEmpty(htmxDtos)) {
						for (HtmxDto htmxDto : htmxDtos) {
							if (("MATERIAL".equals(htmxDto.getQglbdm()) || "OUTSOURCE".equals(htmxDto.getQglbdm()) || "DEVICE".equals(htmxDto.getQglbdm()) || StringUtils.isBlank(htmxDto.getQglbdm())) && StringUtils.isNotBlank(htmxDto.getWlid())) {
								sfscu8qgd = true;
								break;
							}
						}
					}
					// 判断u8操作标记 0不操作
					if (StringUtil.isNotBlank(matridxdsflag) && !"1".equals(systemreceiveflg) && sfscu8qgd && !"1".equals(htglDto_t.getHtlx()) && !"2".equals(htglDto_t.getHtlx()) && StringUtil.isBlank(htglDto_t.getBchtid())) {
						// 截取第一个qgid
						String qgid = org.apache.commons.lang.StringUtils.substringBefore(htglDto_t.getQgid(), ",");
						QgglDto qgglDto_h = qgglService.getDtoById(qgid);
						if ("OUTSOURCE".equals(qgglDto_h.getQglbdm()) || "MATERIAL".equals(qgglDto_h.getQglbdm()) || "DEVICE".equals(qgglDto_h.getQglbdm())) {
							if (!rdRecordService.determine_Entry(htglDto_t.getCjrq())) {
								throw new BusinessException("ICOM99019", "该月份已结账，不允许再录入U8数据，请修改单据日期!");
							}
						}

						@SuppressWarnings("unchecked")
						Map<String, Object> map = rdRecordService.addU8ContractData(htglDto_t, htmxDtos, qgglDto_h, operator, htglDto);
						htmxDtos = (List<HtmxDto>) map.get("htmxDtos");
						htglDto = ((HtglDto) map.get("htglDto"));

						int count = htmxDao.updateHtmxDtos(htmxDtos);
						if (count < 1) {
							log.error("更新合同明细U8id失败");
							throw new BusinessException("msg", "更新合同明细U8id失败");
						}
						if ("1".equals(systemconfigflg)) {
							HtglDto htglDto_ht = new HtglDto();
							htglDto_ht.setPrefixFlg(prefixFlg);
							htglDto_ht.setHtmxJson(JSONObject.toJSONString(htmxDtos));
							amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htmx.updateHtmxDtos", JSONObject.toJSONString(htglDto_ht));
						}
						// 发送钉钉消息
//						if (!CollectionUtils.isEmpty(spgwcyDtos)) {
//							int size = spgwcyDtos.size();
//							for (int i = 0; i < size; i++) {
//								if (StringUtil.isNotBlank(spgwcyDtos.get(i).getDdid())) {
//									talkUtil.sendWorkMessage("null", spgwcyDtos.get(i).getDdid(), ICOMM_SH00006,
//											xxglService.getMsg("ICOMM_SH00040", operator.getZsxm(), shgcDto.getShlbmc(),
//													htglDto_t.getHtnbbh(), htglDto_t.getYwlxmc(), htglDto_t.getSqbmmc(),
//													DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
//								}
//							}
//						}
						// 发送申请人消息
						HtglDto htglDto_ht = getDtoById(htglDto.getHtid());
						String qgids = htglDto_ht.getQgid();
						List<String> list = Arrays.asList(qgids.split(","));
						QgglDto qgglDto_hs = new QgglDto();
						qgglDto_hs.setIds(list);
						qgglDto_hs.setScbj("0");
						List<QgglDto> qgglList = qgglService.getDtoList(qgglDto_hs);
						if (!CollectionUtils.isEmpty(qgglList)) {
							try {
								for (QgglDto qgglDto_t : qgglList) {
									if (StringUtil.isNotBlank(qgglDto_t.getDdid())) {
										String sign = URLEncoder.encode(commonservice.getSign(qgglDto_t.getQgid()),
												StandardCharsets.UTF_8);
										// 内网访问
										String internalbtn = applicationurl + urlPrefix
												+ "/ws/purchase/purchase/purchaseWsView?qgid=" + qgglDto_t.getQgid()
												+ "&htid=" + htglDto_ht.getHtid() + "&sign=" + sign;
										// 外网访问
										/*String external=externalurl + urlPrefix + "/ws/purchase/purchase/purchaseWsView?qgid="
												+ qgglDto_t.getQgid() + "&htid=" + htglDto_ht.getHtid() + "&sign="
												+ sign;*/
										List<BtnJsonList> btnJsonLists = new ArrayList<>();
										BtnJsonList btnJsonList = new BtnJsonList();
										btnJsonList.setTitle("详细");
										btnJsonList.setActionUrl(internalbtn);
										btnJsonLists.add(btnJsonList);
										/*btnJsonList = new BtnJsonList();
										btnJsonList.setTitle("外网访问");
										btnJsonList.setActionUrl(external);
										btnJsonLists.add(btnJsonList);*/
										talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),qgglDto_t.getSqr(),qgglDto_t.getSqryhm(), qgglDto_t.getDdid(), ICOMM_SH00041,
												StringUtil.replaceMsg(ICOMM_SH00042, qgglDto_t.getDjh(),
														htglDto_ht.getHtnbbh(),
														DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
												btnJsonLists, "1");
									}
								}
							} catch (Exception e) {
								log.error(e.getMessage());
							}
						}
					} else if (StringUtil.isNotBlank(matridxdsflag) && !"1".equals(systemreceiveflg) && sfscu8qgd && "2".equals(htglDto_t.getHtlx())) {
						// 截取第一个qgid
						String qgid = StringUtils.substringBefore(htglDto_t.getQgid(), ",");
						QgglDto qgglDto_h = qgglService.getDtoById(qgid);
						@SuppressWarnings("unchecked")
						Map<String, Object> map = rdRecordService.addU8ContractDataForWW(htglDto_t, htmxDtos, qgglDto_h, operator, htglDto);
						htmxDtos = (List<HtmxDto>) map.get("htmxDtos");
						htglDto = ((HtglDto) map.get("htglDto"));

						int count = htmxDao.updateHtmxDtos(htmxDtos);
						if (count < 1) {
							log.error("更新合同明细U8id失败");
							throw new BusinessException("msg", "更新合同明细U8id失败");
						}
						// 发送申请人消息
						HtglDto htglDto_ht = getDtoById(htglDto.getHtid());
						String qgids = htglDto_ht.getQgid();
						List<String> list = Arrays.asList(qgids.split(","));
						QgglDto qgglDto_hs = new QgglDto();
						qgglDto_hs.setIds(list);
						qgglDto_hs.setScbj("0");
						List<QgglDto> qgglList = qgglService.getDtoList(qgglDto_hs);
						if (!CollectionUtils.isEmpty(qgglList)) {
							try {
								for (QgglDto qgglDto_t : qgglList) {
									if (StringUtil.isNotBlank(qgglDto_t.getDdid())) {
										String sign = URLEncoder.encode(commonservice.getSign(qgglDto_t.getQgid()),
												StandardCharsets.UTF_8);
										// 内网访问
										String internalbtn = applicationurl + urlPrefix
												+ "/ws/purchase/purchase/purchaseWsView?qgid=" + qgglDto_t.getQgid()
												+ "&htid=" + htglDto_ht.getHtid() + "&sign=" + sign;
										// 外网访问
										/*String external=externalurl + urlPrefix + "/ws/purchase/purchase/purchaseWsView?qgid="
												+ qgglDto_t.getQgid() + "&htid=" + htglDto_ht.getHtid() + "&sign="
												+ sign;*/
										List<BtnJsonList> btnJsonLists = new ArrayList<>();
										BtnJsonList btnJsonList = new BtnJsonList();
										btnJsonList.setTitle("详细");
										btnJsonList.setActionUrl(internalbtn);
										btnJsonLists.add(btnJsonList);
										/*btnJsonList = new BtnJsonList();
										btnJsonList.setTitle("外网访问");
										btnJsonList.setActionUrl(external);
										btnJsonLists.add(btnJsonList);*/
										talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),qgglDto_t.getSqr(),qgglDto_t.getSqryhm(), qgglDto_t.getDdid(), ICOMM_SH00041,
												StringUtil.replaceMsg(ICOMM_SH00042, qgglDto_t.getDjh(),
														htglDto_ht.getHtnbbh(),
														DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
												btnJsonLists, "1");
									}
								}
							} catch (Exception e) {
								log.error(e.getMessage());
							}
						}
					} else {
						// 发送钉钉消息
						if (!CollectionUtils.isEmpty(spgwcyDtos)) {
							for (SpgwcyDto spgwcyDto : spgwcyDtos) {
								if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
									talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00006,
											xxglService.getMsg("ICOMM_SH00040", operator.getZsxm(), shgcDto.getShlbmc(),
													htglDto_t.getHtnbbh(), htglDto_t.getYwlxmc(), htglDto_t.getSqbmmc(),
													DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
								}
							}
						}
						// 发送申请人消息
						HtglDto htglDto_ht = getDtoById(htglDto.getHtid());
						String qgids = htglDto_ht.getQgid();
						List<String> list = Arrays.asList(qgids.split(","));
						QgglDto qgglDto_hs = new QgglDto();
						qgglDto_hs.setIds(list);
						qgglDto_hs.setScbj("0");
						List<QgglDto> qgglList = qgglService.getDtoList(qgglDto_hs);
						if (!CollectionUtils.isEmpty(qgglList)) {
							try {
								for (QgglDto qgglDto_t : qgglList) {
									if (StringUtil.isNotBlank(qgglDto_t.getDdid())) {
										String sign = URLEncoder.encode(commonservice.getSign(qgglDto_t.getQgid()),
												StandardCharsets.UTF_8);
										// 内网访问
										String internalbtn = applicationurl + urlPrefix
												+ "/ws/purchase/purchase/purchaseWsView?qgid=" + qgglDto_t.getQgid()
												+ "&htid=" + htglDto_ht.getHtid() + "&sign=" + sign;
										// 外网访问
									/*String external = externalurl+urlPrefix + "/ws/purchase/purchase/purchaseWsView?qgid="
											+ qgglDto_t.getQgid() + "&htid=" + htglDto_ht.getHtid() + "&sign="
											+ sign;*/
										List<BtnJsonList> btnJsonLists = new ArrayList<>();
										BtnJsonList btnJsonList = new BtnJsonList();
										btnJsonList.setTitle("详细");
										btnJsonList.setActionUrl(internalbtn);
										btnJsonLists.add(btnJsonList);
									/*btnJsonList = new BtnJsonList();
									btnJsonList.setTitle("外网访问");
									btnJsonList.setActionUrl(external);
									btnJsonLists.add(btnJsonList);*/
										talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),qgglDto_t.getSqr(),qgglDto_t.getSqryhm(), qgglDto_t.getDdid(), ICOMM_SH00041,
												StringUtil.replaceMsg(ICOMM_SH00042, qgglDto_t.getDjh(),
														htglDto_ht.getHtnbbh(),
														DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
												btnJsonLists, "1");
									}
								}
							} catch (Exception e) {
								log.error(e.getMessage());
							}
						}
					}
					//合同审核通过时若为预付全款自动添加付款提醒信息
					if ("001".equals(htglDto_t.getFkfsdm())) {
						List<HtfktxDto> addlist = new ArrayList<>();
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						HtfktxDto htfktxDto = new HtfktxDto();
						htfktxDto.setHtid(htglDto_t.getHtid());
						htfktxDto.setFktxid(StringUtil.generateUUID());
						htfktxDto.setYfje(htglDto_t.getZje());
						//预付日期设置为当天
						htfktxDto.setYfrq(format.format(new Date()));
						htfktxDto.setLrry(htglDto_t.getFzr());
						htfktxDto.setYfbl("100");//预付全款设置为全部
						addlist.add(htfktxDto);
						htfktxService.insertDtoList(addlist);
					}
				}
				if(!"3".equals(htglDto_t.getHtlx()) && StringUtil.isBlank(htglDto_t.getBchtid())){
					XtszDto xtszDto = xtszService.selectById("approval.ht");
					if(xtszDto!=null && StringUtil.isNotBlank(xtszDto.getSzz())){
						Map<String, Object> xtszDtoMap = JSON.parseObject(xtszDto.getSzz(), new TypeReference<Map<String, Object>>() {
						});
						if(xtszDtoMap.get("postEnd")!=null){
							HtglDto htglDtoT = new HtglDto();
							htglDtoT.setHtid(shgcDto.getYwid());
							htglDtoT.setShxx_gwmc(xtszDtoMap.get("postEnd").toString());
							List<HtglDto> htglDtoList = dao.queryOrderByQgAndHt(htglDtoT);
							if(!CollectionUtils.isEmpty(htglDtoList)){
								XtszDto attendanceTime = xtszService.selectById("attendance.time");
								if(attendanceTime!=null) {
									if (StringUtil.isNotBlank(attendanceTime.getSzz())) {
										Map<String, Object> attendanceTimeMap = JSON.parseObject(attendanceTime.getSzz(), new TypeReference<Map<String, Object>>() {
										});
										String formattedDateTime = "";
										if (attendanceTimeMap.get("officeHours") != null && attendanceTimeMap.get("closingTime") != null && attendanceTimeMap.get("beginTime") != null && attendanceTimeMap.get("endTime") != null) {
											List<ShysDto> shysDtoList = new ArrayList<>();
											SpgwDto spgwDto = spgwService.getDtoById(shgcDto.getShxx().getGwid());
											if(spgwDto!=null && xtszDtoMap.get("postEnd").toString().equals(spgwDto.getGwmc())){
												LocalDateTime currentDateTime = LocalDateTime.now();
												DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
												formattedDateTime = currentDateTime.format(formatter);
											}
											for (HtglDto htglDto1:htglDtoList){
												if(StringUtil.isNotBlank(formattedDateTime)){
													htglDto1.setPostEnd(formattedDateTime);
													BigDecimal timeLag = commonservice.getHoursByD(htglDto1.getPostStart(),htglDto1.getPostEnd());
													htglDto1.setTimeLag(timeLag.toString());
												}
												String shys = commonservice.getAuditTime(htglDto1.getPostStart(),htglDto1.getPostEnd(),htglDto1.getTimeLag(),
														attendanceTimeMap.get("officeHours").toString(),
														attendanceTimeMap.get("closingTime").toString(),
														attendanceTimeMap.get("beginTime").toString(),
														attendanceTimeMap.get("endTime").toString());
												if(StringUtil.isNotBlank(shys)){
													ShysDto shysDto = new ShysDto();
													shysDto.setId(htglDto1.getHtid());
													shysDto.setAutoid(htglDto1.getQgid());
													shysDto.setShys(shys);
													shysDto.setLrry(operator.getYhid());
													shysDtoList.add(shysDto);
												}
											}
											if(!CollectionUtils.isEmpty(shysDtoList)){
												boolean resultShys = shysService.insertDtoList(shysDtoList);
												if(!resultShys){
													throw new BusinessException("msg","审核用时新增失败！");
												}
											}
										}
									}
								}
							}
						}
					}
				}
			// 审核中
		} else {
				htglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				// 发送钉钉消息
				// 判断u8操作标记 0：不操作
				if (!"1".equals(systemreceiveflg)) {
					if("1".equals(shgcDto.getXlcxh()) && shgcDto.getShxx()==null) {
						try {
							// 根据申请部门查询审核流程
//							List<DdxxglDto> ddxxglDtos = ddxxglService.getProcessByJgid(htglDto_t.getSqbm(),DdAuditTypeEnum.SP_HT.getCode());
//							if(CollectionUtils.isEmpty(ddxxglDtos))
//								throw new BusinessException("msg","未找到所属部门审核流程！");
//							if(ddxxglDtos.size() > 1)
//								throw new BusinessException("msg","找到多条所属部门审核流程！");
//							String spr = ddxxglDtos.get(0).getSpr();
//							String csr = ddxxglDtos.get(0).getCsr();
							//如果修改了钉钉审批流程则采用修改后的钉钉审批流程
//							if(StringUtil.isNotBlank(htglDto_t.getDdspid())) {
//								DdxxglDto ddxxglDto=ddxxglService.getProcessBySpid(htglDto_t.getDdspid());
//								if(ddxxglDto!=null) {
//									if(StringUtils.isNotBlank(ddxxglDto.getSpr())) {
//										spr=ddxxglDto.getSpr();
//									}
//									if(StringUtils.isNotBlank(ddxxglDto.getCsr())) {
//										csr=ddxxglDto.getCsr();
//									}
//								}
//							}
//							if(!StringUtils.isNotBlank(spr)) {
//								spr="";
//							}
//							if(!StringUtils.isNotBlank(csr)) {
//								csr="";
//							}
//							if(StringUtil.isNotBlank(htglDto_t.getCsrs())) {
//								String[] split = htglDto_t.getCsrs().split(",|，");
//								// 根据抄送人查询钉钉ID
//								List<String> yhids = Arrays.asList(split);
//								List<User> yhxxs = commonservice.getDdidByYhids(yhids);
//								List<String> ddids=new ArrayList<>();
//								if(!CollectionUtils.isEmpty(yhxxs)) {
//									for(User user : yhxxs) {
//										if(StringUtils.isBlank(user.getDdid()))
//											throw new BusinessException("ICOM99019","该用户("+user.getZsxm()+")未获取到钉钉ID！");
//										ddids.add(user.getDdid());
//									}
//								}
//								if(!CollectionUtils.isEmpty(ddids)) {
//									ddids.removeIf(StringUtil::isBlank);
//						            csr += "," + String.join(",", ddids);
//						            if(",".equals(csr.substring(0,1))) {
//						              csr=csr.substring(1);
//						            }
//								}
//							}
							//提交审核至钉钉审批
//							String cc_list = csr; // 抄送人信息
//		                    String approvers = spr; // 审核人信息
							Map<String,Object> template=talkUtil.getTemplateCode(operator.getYhm(), "合同或订单审批");//获取审批模板ID
							String templateCode=(String) template.get("message");
							//获取申请人信息(合同申请应该为采购部门)
							User user=new User();
							user.setYhid(shgcDto.getSqr());
							user=commonservice.getUserInfoById(user);
							if(user==null)
								throw new BusinessException("ICOM99019","未获取到申请人信息！");
							if(StringUtils.isBlank(user.getDdid()))
								throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
							String userid=user.getDdid();
							String dept=user.getJgid ();
							//审批人与申请人做比较，若流程中前面与申请人相同则省略相同的流程
//		                    String[] t_sprs=spr.split(",");
//		                    List<String> sprs_list=Arrays.asList(t_sprs);
//		                    StringBuilder t_str= new StringBuilder();
//		                    if(!CollectionUtils.isEmpty(sprs_list)) {
//								boolean sflx=true;
//								for (String s : sprs_list) {
//									if (StringUtils.isNotBlank(userid)) {
//										if (sflx) {
//											if (!userid.equals(s)) {
//												t_str.append(",").append(s);
//												sflx = false;
//											}
//										} else {
//											t_str.append(",").append(s);
//										}
//									}
//								}
//		                    		if(t_str.length()>0)
//		                    			approvers=t_str.substring(1);
//		                    }
							Map<String,String> map=new HashMap<>();
							SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
							Date qydate = sdf.parse(htglDto_t.getCjrq());
							String qyrq=sdf.format(qydate);
							List<HtmxDto> htmxlist=htmxService.getListByHtid(htglDto_t.getHtid());
							List<Map<String,String>> mxlist=new ArrayList<>();
							StringBuilder cgsqdhs= new StringBuilder();//采购申请单号
							List<String> glspds = new ArrayList<>();//关联审批单
							if(!CollectionUtils.isEmpty(htmxlist)) {
								//框架合同不关联附件
								if (!"3".equals(htglDto_t.getHtlx())){
									List<String> ids=new ArrayList<>();
									for (HtmxDto htmxDto : htmxlist) {
										if (StringUtil.isNotBlank(htmxDto.getDjh())) {
											if (!cgsqdhs.toString().contains(htmxDto.getDjh())) {
												ids.add(htmxDto.getQgid());
												cgsqdhs.append("/").append(htmxDto.getDjh());
											}
										}
									}
									cgsqdhs = new StringBuilder(cgsqdhs.substring(1));
									if(!CollectionUtils.isEmpty(ids)) {
										QgglDto qgglDto=new QgglDto();
										qgglDto.setIds(ids);
										List<QgglDto> qggllist=qgglService.getDtoListByIds(qgglDto);
										if(!CollectionUtils.isEmpty(qggllist)) {
											for (QgglDto dto : qggllist) {
												if (StringUtil.isNotBlank(dto.getDdslid())) {
													glspds.add(dto.getDdslid());
												}
											}
										}
									}
								}
								//如果是补充合同
								if (StringUtil.isNotBlank(htglDto_t.getBchtid())){
									glspds = new ArrayList<>();
									if (StringUtil.isNotBlank(htglDto_t.getYhtddslid())){
										glspds.add(htglDto_t.getYhtddslid());
									}
								}
								if(htmxlist.size()>5) {
									Map<String,String> t_map=new HashMap<>();
									t_map.put("拟购货物名称及货号","详见附件");
									t_map.put("该物料所属研发项目", "详见附件");
									t_map.put("物料编码", "详见附件");
									t_map.put("货物规格型号", "详见附件");
									t_map.put("拟购货物技术参数", "详见附件");
									t_map.put("数量", "详见附件");
									t_map.put("单价（元）", "详见附件");
									t_map.put("涉及金额（元）", "3".equals(htglDto_t.getHtlx()) ? "0" : htglDto_t.getZje());
									mxlist.add(t_map);
								}else {
									for (HtmxDto htmxDto : htmxlist) {
										Map<String, String> t_map = new HashMap<>();
										t_map.put("拟购货物名称及货号", (StringUtils.isNotBlank(htmxDto.getWlmc()) ? htmxDto.getWlmc() : htmxDto.getHwmc()) + "/" + (StringUtils.isNotBlank(htmxDto.getYchh()) ? htmxDto.getYchh() : ""));
										t_map.put("该物料所属研发项目", "");
										t_map.put("物料编码", StringUtils.isNotBlank(htmxDto.getWlbm()) ? htmxDto.getWlbm() : "/");
										t_map.put("货物规格型号", "详见附件");
										t_map.put("拟购货物技术参数", "详见附件");
										t_map.put("数量", "3".equals(htglDto_t.getHtlx()) ? "0" : htmxDto.getSl());
										t_map.put("单价（元）", htmxDto.getHsdj());
										t_map.put("涉及金额（元）", "3".equals(htglDto_t.getHtlx()) ? "0" : htglDto_t.getZje());
										mxlist.add(t_map);
									}
								}
							}
							boolean flg = false;
							for(HtmxDto htmxDto:htmxlist){
								if("3".equals(htmxDto.getLbcskz1())){
									flg = true;
									break;
								}
							}
							String htlx = "";
							if(StringUtil.isNotBlank(htglDto_t.getZje())){
								BigDecimal zje = new BigDecimal(htglDto_t.getZje());
								if(zje.compareTo(new BigDecimal("300000"))>0){
									htlx = "30万以上合同";
								}else if(zje.compareTo(new BigDecimal("2000"))>0 || flg){
									htlx = "2000元以上合同或设备类采购";
								}else{
									htlx = "2000元以下合同";
								}
							}
							if("3".equals(htglDto_t.getHtlx())){
								htlx = "2000元以下合同";
							}
							map.put("合同类型", "采购合同");
							map.put("采购合同类型", htlx);
							map.put("所属公司", systemname);
							map.put("用章类型", "合同章");
							map.put("合同风险类型",htglDto_t.getHtfxmc());
							map.put("采购类型","1".equals(htglDto_t.getHtlx())?"OA采购":"3".equals(htglDto_t.getHtlx())?"框架合同":"普通采购");
							map.put("合同编号（外部编号）", StringUtils.isNotBlank(htglDto_t.getHtwbbh()) ? htglDto_t.getHtwbbh():"");
							map.put("合同/订单内部编号", StringUtils.isNotBlank(htglDto_t.getHtnbbh()) ? htglDto_t.getHtnbbh():"");
							map.put("采购申请单号", cgsqdhs.toString());
							map.put("签约日期", qyrq);
							map.put("我方负责人", htglDto_t.getFzrmc());
							//判断是否初次合作
							HtglDto gys = new HtglDto();
							gys.setGys(htglDto_t.getGys());
							List<HtglDto> htglList = dao.getDtoList(gys);
							map.put("对方单位名称", (htglList!=null && htglList.size()>1)?htglDto_t.getGysmc():htglDto_t.getGysmc()+"(*初次合作)");
							map.put("对方负责人", htglDto_t.getGysfzr());
							if(StringUtils.isBlank(htglDto_t.getGysfzr()))
								throw new BusinessException("msg","发起钉钉审批失败!错误信息:供应商负责人信息不能为空！");
							map.put("对方联系方式", htglDto_t.getGysfzrdh());
							if(StringUtils.isBlank(htglDto_t.getGysfzrdh()))
								throw new BusinessException("msg","发起钉钉审批失败!错误信息:供应商负责人联系方式不能为空！");
							map.put("质量责任条款", "详见附件");
							map.put("付款条件", htglDto_t.getFkfsmc());
							map.put("付款方", htglDto_t.getFkfmc());
							map.put("合计金额（元）","3".equals(htglDto_t.getHtlx())?"0":htglDto_t.getZje());
							map.put("关联审批单", glspds.isEmpty() ? "" : JSON.toJSONString(glspds));
							map.put("是否经过法务", StringUtils.isNotBlank(htglDto_t.getSfjgfw()) ? ("1".equals(htglDto_t.getSfjgfw())?"是":"否"):"");
							map.put("合同内容","详细信息："+applicationurl+urlPrefix+"/ws/contract/getContractUrl?htid="+htglDto_t.getHtid());
						
							Map<String,Object> t_map=talkUtil.createInstance(operator.getYhm(), templateCode, null, null, userid, dept, map,mxlist,null);
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
									HtglDto htgl_dd=new HtglDto();
									htgl_dd.setDdslid(String.valueOf(result_map.get("process_instance_id")));
									htglDto_t.setDdslid(String.valueOf(result_map.get("process_instance_id")));
									htgl_dd.setXgry(operator.getYhid());
									htgl_dd.setHtid(htglDto_t.getHtid());
									dao.updateContract(htgl_dd);
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
							throw new BusinessException("msg","异常!异常信息:"+e);
						}
						
						//如果提交审批时，申请人属于下一个岗位时，则直接将xlcxh设置为2，跳过当前审批流程
//						if(!CollectionUtils.isEmpty(spgwcyDtos) && "1".equals(shgcDto.getXlcxh())) {
//							for (SpgwcyDto spgwcyDto : spgwcyDtos) {
//								if (spgwcyDto.getYhid().equals(shgcDto.getSqr())) {
//									shgcDto.setXlcxh("2");
//								}
//							}
//						}
					}
				}
				// 发送钉钉消息--取消审核人员
				if (!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())) {
					try {
						int size = shgcDto.getNo_spgwcyDtos().size();
						for (int i = 0; i < size; i++) {
							if (StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())) {
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
										shgcDto.getNo_spgwcyDtos().get(i).getYhid(),
										ICOMM_SH00004,
										StringUtil.replaceMsg(ICOMM_SH00005, operator.getZsxm(),
												shgcDto.getShlbmc(), htglDto_t.getHtid(),
												DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
			}
			if ("3".equals(htglDto_t.getHtlx())){
				updateContract(htglDto);
			}else {
				boolean isSuccess = modSaveContract(htglDto);
				if("1".equals(systemconfigflg) && isSuccess) {
					htglDto.setPrefixFlg(prefixFlg);
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htgl.update",JSONObject.toJSONString(htglDto));
				}
			}
			//获取下一步审核人员，保存至审核过程表
			if(StringUtil.isNotBlank(htglDto_t.getDdslid())){
				String token = talkUtil.getDingTokenByUserId(operator.getYhm());
				Map<String, String> result_y = talkUtil.getProcessMessage(htglDto_t.getDdslid(),token);
				Map<String, String> body=JSON.parseObject(result_y.get("body"),Map.class);
				Map<String,String> process_instance=JSON.parseObject(JSON.toJSONString(body.get("process_instance")),Map.class);
				List<Map> taskArray= JSON.parseArray(JSON.toJSONString(process_instance.get("tasks")),Map.class);

				for(int i=0;i<taskArray.size();i++){
					Map<String,String> job =taskArray.get(i);
					if("RUNNING".equals(job.get("task_status"))){
						User user=commonservice.getYhid(job.get("userid"));
						shgcDto.setDqshr(user.getZsxm());
					}
				}
			}
		}
		return true;
	}

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
				String wlid = shgcDto.getYwid();
				HtglDto htglDto = new HtglDto();
				htglDto.setXgry(operator.getYhid());
				htglDto.setHtid(wlid);
				htglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				boolean isSuccess = updateContract(htglDto)>0;
				if("1".equals(systemconfigflg) && isSuccess) {
					htglDto.setPrefixFlg(prefixFlg);
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htgl.update",JSONObject.toJSONString(htglDto));
				}
			}
		} else {
			// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String wlid = shgcDto.getYwid();
				HtglDto htglDto = new HtglDto();
				htglDto.setXgry(operator.getYhid());
				htglDto.setHtid(wlid);
				htglDto.setZt(StatusEnum.CHECK_NO.getCode());
				boolean isSuccess = updateContract(htglDto)>0;
				if("1".equals(systemconfigflg) && isSuccess) {
					htglDto.setPrefixFlg(prefixFlg);
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htgl.update",JSONObject.toJSONString(htglDto));
				}
				//OA取消审批的同时组织钉钉审批
				HtglDto t_htglDto=dao.getDto(htglDto);
				if(t_htglDto!=null && StringUtils.isNotBlank(t_htglDto.getDdslid())) {
					Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), t_htglDto.getDdslid(), "", operator.getDdid());
					//若撤回成功将实例ID至为空
					String success=String.valueOf(cancelResult.get("message"));
					@SuppressWarnings("unchecked")
					Map<String,Object> result_map=JSON.parseObject(success,Map.class);
					boolean bo1= (boolean) result_map.get("success");
					if(bo1)
						dao.updateDdslidToNull(t_htglDto);
				}
			}
		}
		return true;
	}

	/**
	 * 审核通过时修改请购明细数量
	 */
	private boolean updatePurchaseCompQuantity(List<HtmxDto> htmxDtos, String yhid, HtglDto htglDto) {
		// TODO Auto-generated method stub
		// 修改请购明细数量 sysl-sl ydsl-sl
		if (!CollectionUtils.isEmpty(htmxDtos)) {
			for (HtmxDto htmxDto : htmxDtos) {
				htmxDto.setXgry(yhid);
				htmxDto.setHtnbbh(htglDto.getHtnbbh());
			}
			boolean result = htmxService.updateQuantityComp(htmxDtos);
			if (!result)
				return false;
			if("1".equals(systemconfigflg)) {
				HtglDto htglDto_ht = new HtglDto();
				htglDto_ht.setPrefixFlg(prefixFlg);
                htglDto_ht.setHtmxJson(JSONObject.toJSONString(htmxDtos));
                amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.htmx.updateQuantityComp",JSONObject.toJSONString(htglDto_ht));
			}
			// 判断更新请购完成标记，1.根据查询相关请购信息 2.查询请购下明细信息判断是否修改
			List<String> qgids = dao.isPurchaseComp(htglDto.getHtid());
			
			if (!CollectionUtils.isEmpty(qgids)) {
				// 批量更新完成标记
				QgglDto qgglDto = new QgglDto();
				qgglDto.setXgry(yhid);
				qgglDto.setIds(qgids);
				qgglDto.setWcbj("1");
				boolean isSuccess = qgglService.updateWcbjs(qgglDto);
				if("1".equals(systemconfigflg) && isSuccess) {
					qgglDto.setPrefixFlg(prefixFlg);
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qggl.updateWcbjs",JSONObject.toJSONString(qgglDto));
				}
			}
		}
		return true;
	}

	@Override
	public List<HtglDto> getPagedAuditHtgl(HtglDto htglDto) {
		// 获取人员ID和履历号
		List<HtglDto> t_sbyzList = dao.getPagedAuditHtgl(htglDto);
		if (CollectionUtils.isEmpty(t_sbyzList))
			return t_sbyzList;

		List<HtglDto> sqList = dao.getAuditListHtgl(t_sbyzList);

		commonservice.setSqrxm(sqList);

		return sqList;
	}

	/**
	 * 合同列表（查询审核状态）
	 */
	@Override
	public List<HtglDto> getPagedDtoList(HtglDto htglDto) {
		List<HtglDto> list = dao.getPagedDtoList(htglDto);
		try {
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_CONTRACT.getCode(), "zt", "htid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_CONTRACT_OA.getCode(), "zt", "htid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return list;
	}

	/**
	 * 查询数据生成合同
	 */
	@Override
	public Map<String, Object> getParamForContract(HtglDto htglDto) {
		// TODO Auto-generated method stub
		XWPFContractUtil xwpfContractUtil = new XWPFContractUtil();
		Map<String, Object> map = dao.getContractMap(htglDto);
		String ddbh = map.get("number") != null ? map.get("number").toString() : null;
		if (StringUtil.isNotBlank(ddbh)) {
			int lastIndex = ddbh.lastIndexOf("-");
			if (lastIndex > -1) {
				String tmpStr = ddbh.substring(lastIndex - 8, lastIndex);
				if (StringUtil.isNotBlank(tmpStr)) {
					String year = tmpStr.substring(0, 4);
					if (StringUtil.isNotBlank(year)) {
						map.put("year", year);
					}
					String mon = tmpStr.substring(4, 6);
					if (StringUtil.isNotBlank(mon)) {
						map.put("mon", mon);
					}
					String day = tmpStr.substring(6, 8);
					if (StringUtil.isNotBlank(day)) {
						map.put("day", day);
					}
				}
			}
		}
		List<Map<String, String>> listMap = dao.getContractListMap(htglDto);
		if (!CollectionUtils.isEmpty(listMap)) {
			BigDecimal hjje = new BigDecimal("0");
			DecimalFormat df = new DecimalFormat("0.00");
			for (Map<String, String> stringStringMap : listMap) {
				BigDecimal xj = new BigDecimal("0");
				if (StringUtils.isNotBlank(stringStringMap.get("xj")))
					xj = new BigDecimal(stringStringMap.get("xj"));
				hjje = hjje.add(xj);
			}
			map.put("hjje", df.format(hjje));
			String dxje = ChineseYuanUtil.convert(df.format(hjje));
			map.put("dxje", dxje);
		}

		map.put("buyerEmail", contractemail);
		map.put("htmx", listMap);
		map.put("releaseFilePath", releaseFilePath); // 正式文件路径
		map.put("tempPath", tempPath); // 临时文件路径
		return xwpfContractUtil.replaceContract(map, fjcfbService, FTP_URL, DOC_OK, amqpTempl);
	}

	/**
	 * 钉钉合同审批回调
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean callbackHtglAduit(JSONObject obj, HttpServletRequest request,Map<String,Object> t_map) throws BusinessException {
		HtglDto htglDto = new HtglDto();
		String result = obj.getString("result");// 正常结束时result为agree，拒绝时result为refuse，审批终止时没这个值，redirect转交
		String type = obj.getString("type");// 审批正常结束（同意或拒绝）的type为finish，审批终止的type为terminate
		String processInstanceId = obj.getString("processInstanceId");// 审批实例id
		String staffId = obj.getString("staffId");// 审批人钉钉ID
		String remark = obj.getString("remark");// 审核意见
		String content = obj.getString("content");//评论
		String title= obj.getString("title");
		String processCode=obj.getString("processCode");
		String finishTime = obj.getString("finishTime");// 审批完成时间
		String wbcxidString  = obj.getString("wbcxid"); //获取外部程序id
		log.error("回调参数获取---------result:"+result+",type:"+type+",processInstanceId:"+processInstanceId+",staffId:"+staffId+",remark:"+remark+",finishTime"+finishTime+",外部程序id"+wbcxidString);
		DdfbsglDto ddfbsglDto = new DdfbsglDto();
		ddfbsglDto.setProcessinstanceid(processInstanceId);
		ddfbsglDto=ddfbsglService.getDtoById(processInstanceId);
		DdspglDto ddspglDto=new DdspglDto();
		DdspglDto t_ddspglDto=new DdspglDto();
		t_ddspglDto.setProcessinstanceid(processInstanceId);
		t_ddspglDto.setType("finish");
		t_ddspglDto.setEventtype(DingTalkUtil.BPMS_TASK_CHANGE);
		List<DdspglDto> ddspgllist=ddspglService.getDtoList(t_ddspglDto);
		try {
			if(ddfbsglDto==null) 
				throw new BusinessException("message","未获取到相应的钉钉分布式信息！");
			if(StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {			
				if(!CollectionUtils.isEmpty(ddspgllist)) {
					ddspglDto=ddspgllist.get(0);
				}
			}
			htglDto.setDdslid(processInstanceId);
			// 根据钉钉审批实例ID查询关联请购单
			htglDto = dao.getDtoByDdslid(htglDto);
			// 若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
			if (htglDto != null) {
				//获取审批人信息
				User user=new User();
				user.setDdid(staffId);
				user.setWbcxid(wbcxidString);
				user = commonservice.getByddwbcxid(user);
				if (user == null)
					return false;
				log.error("user-yhid:" + user.getYhid() + "---user-zsxm:" + user.getZsxm() + "----user-yhm"
						+ user.getYhm() + "---user-ddid" + user.getDdid());
				// 获取审批人角色信息
				List<HtglDto> dd_sprjs = dao.getSprjsBySprid(user.getYhid());
				// 获取当前审核过程
				ShgcDto t_shgcDto = shgcService.getDtoByYwid(htglDto.getHtid());
				if (t_shgcDto != null) {
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
					if (!CollectionUtils.isEmpty(dd_sprjs)) {
						// 审批正常结束（同意或拒绝）
						ShxxDto shxxDto = new ShxxDto();
						shxxDto.setGcid(t_shgcDto.getGcid());
						shxxDto.setLcxh(t_shgcDto.getXlcxh());
						shxxDto.setShlb(t_shgcDto.getShlb());
						shxxDto.setShyj(remark);
						shxxDto.setAuditType(t_shgcDto.getShlb());
						shxxDto.setYwid(htglDto.getHtid());
						if (StringUtil.isNotBlank(finishTime)){
							shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
						}
						log.error("shxxDto-lcxh:" + t_shgcDto.getXlcxh() + " shxxDto-shlb:"
								+ t_shgcDto.getShlb() + " shxxDto-shyj:" + remark + " shxxDto-ywid:"
								+ htglDto.getHtid());
						String lastlcxh = null;// 回退到指定流程

							if (("finish").equals(type)) {
								// 如果审批通过,同意
								if (("agree").equals(result)) {
									log.error("同意------");
									shxxDto.setSftg("1");
									if (StringUtils.isBlank(t_shgcDto.getXlcxh()))
										throw new BusinessException("msg", "回调失败，现流程序号为空！");
									lastlcxh = String.valueOf(Integer.parseInt(t_shgcDto.getXlcxh()) + 1);
								}
								// 如果审批未通过，拒绝
								else if (("refuse").equals(result)) {
									log.error("拒绝------");
									shxxDto.setSftg("0");
									lastlcxh = "1";
									shxxDto.setThlcxh("-1");
								}
								// 如果审批被转发
								else if (("redirect").equals(result)) {
									String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
											.format(new Date(Long.parseLong(finishTime) / 1000));
									log.error("DingTalkMaterPurchaseAudit(钉钉合同审批转发提醒)------转发人员:" + user.getZsxm()
											+ ",人员ID:" + user.getYhm() + ",外部编号:" + htglDto.getHtwbbh() + ",单据ID:"
											+ htglDto.getHtid() + ",转发时间:" + date);
									return true;
								}
								// 调用审核方法
								Map<String, List<String>> map = ToolUtil.reformRequest(request);
								log.error("map:"+map);
								List<String> list = new ArrayList<>();
								list.add(htglDto.getHtid());
								map.put("htid", list);
								for(int i=0;i<dd_sprjs.size();i++) {
									try {
										//取下一个角色
										user.setDqjs(dd_sprjs.get(i).getSprjsid());
										user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
										shxxDto.setYwids(new ArrayList<>());
										shgcService.doManyBackAudit(shxxDto, user,request,lastlcxh,obj);
										//更新审批管理信息
										ddspglDto.setCljg("1");
										ddspglService.updatecljg(ddspglDto);
										break;
									} catch (Exception e) {
										log.error("callbackHtglAduit-Exception:" + e.getMessage());
										t_map.put("ddspglid", ddspglDto.getDdspglid());
										t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());
										if(i==dd_sprjs.size()-1)
											throw new BusinessException("msg",e.getMessage());
									}
								}
								//更新当前审批人信息
								if(StringUtil.isNotBlank(processInstanceId)){
									String token = talkUtil.getDingTokenByUserId(user.getYhm());
									Map<String, String> result_y = talkUtil.getProcessMessage(processInstanceId,token);
									Map<String, String> body=JSON.parseObject(result_y.get("body"),Map.class);
									Map<String,String> process_instance=JSON.parseObject(JSON.toJSONString(body.get("process_instance")),Map.class);
									List<Map> taskArray= JSON.parseArray(JSON.toJSONString(process_instance.get("tasks")),Map.class);

									for(int i=0;i<taskArray.size();i++){
										Map<String,String> job =taskArray.get(i);
										if("RUNNING".equals(job.get("task_status"))){
											User user_t=commonservice.getYhid(job.get("userid"));
											t_shgcDto.setDqshr(user_t.getZsxm());
										}
									}
									//更新当前审核人
									shgcService.update(t_shgcDto);
								}
							}
							// 撤销审批
							else if (("terminate").equals(type)) {
								shxxDto.setThlcxh(null);
								shxxDto.setYwglmc(htglDto.getHtnbbh());
								HtglDto t_htglDto = new HtglDto();
								t_htglDto.setHtid(htglDto.getHtid());
								dao.updateDdslidToNull(t_htglDto);
								log.error("撤销------");
								shxxDto.setSftg("0");
								// 调用审核方法
								Map<String, List<String>> map = ToolUtil.reformRequest(request);
								List<String> list = new ArrayList<>();
								list.add(htglDto.getHtid());
								map.put("htid", list);
								for(int i=0;i<dd_sprjs.size();i++) {
									try {
										//取下一个角色
										user.setDqjs(dd_sprjs.get(i).getSprjsid());
										user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
										shxxDto.setYwids(new ArrayList<>());
										shgcService.terminateAudit(shxxDto, user,request,lastlcxh,obj);
										//更新审批管理信息
										ddspglDto.setCljg("1");
										ddspglService.updatecljg(ddspglDto);
										break;
									} catch (Exception e) {
										t_map.put("ddspglid", ddspglDto.getDdspglid());
										t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());
										if(i==dd_sprjs.size()-1)
											throw new BusinessException("msg",e.toString());
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
						shxx.setYwid(htglDto.getHtid());
						shxx.setShlb(AuditTypeEnum.AUDIT_CONTRACT.getCode());
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
			log.error(e.getMsg());
			throw new BusinessException("msg",e.getMsg());
		}catch (Exception e) {
			log.error(e.getMessage());
			throw new BusinessException("msg",e.toString());
		}
		
		return true;
	}

	/**
	 * 判断内部编号是否重复
	 */
	@Override
	public boolean isHtnbbhRepeat(String htnbbh, String htid) {
		// TODO Auto-generated method stub
		if (StringUtil.isNotBlank(htnbbh)) {
			HtglDto htglDto = new HtglDto();
			htglDto.setHtnbbh(htnbbh);
			htglDto.setHtid(htid);
			List<HtglDto> htglDtos = dao.getListByNbbh(htglDto);
			return CollectionUtils.isEmpty(htglDtos);
		}
		return true;
	}

	/**
	 * 合同删除
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteHtgl(HtglDto htglDto) throws BusinessException {
		List<HtglDto> htglDto_List = dao.getDtoList(htglDto);
		List<String> ywids=new ArrayList<>();
		//存与合同关联的请购id
		StringBuilder ids = new StringBuilder();
		String bchtids = "";
		for (HtglDto htglDto_t : htglDto_List) {
			if(StringUtil.isNotBlank(htglDto_t.getBchtid())){
				bchtids = bchtids + "," + htglDto_t.getBchtid();
			}
			if (!"3".equals(htglDto_t.getHtlx())&&StringUtil.isBlank(htglDto_t.getBchtid())) {
				ids.append(",").append(htglDto_t.getQgid());
				ywids.add(htglDto_t.getHtid());
				if (!htglDto_t.getScbj().equals("2")) {
					HtmxDto htmxDto_h = new HtmxDto();
					htmxDto_h.setHtid(htglDto_t.getHtid());
					List<HtmxDto> htmxDto_List = htmxService.getDtoList(htmxDto_h);
					for (HtmxDto htmxDto_t : htmxDto_List) {
						QgmxDto qgmxDto = qgmxService.getDtoById(htmxDto_t.getQgmxid());
						if (qgmxDto != null) {
							if (StringUtil.isBlank(qgmxDto.getYdsl())) {
								qgmxDto.setYdsl("0");
							}
							if (htglDto_t.getZt().equals("80")) {
								BigDecimal sysl = new BigDecimal(qgmxDto.getSysl()).add(new BigDecimal(htmxDto_t.getSl()));
								qgmxDto.setSysl(sysl.toString());
								boolean result_q = qgmxService.updateDto(qgmxDto);
								if (!result_q)
									throw new BusinessException("msg", "请购明细信息修改失败!");
							} else {
								BigDecimal ydsl = new BigDecimal(qgmxDto.getYdsl()).subtract(new BigDecimal(htmxDto_t.getSl()));
								qgmxDto.setYdsl(ydsl.toString());
								boolean result_q = qgmxService.updateDto(qgmxDto);
								if (!result_q)
									throw new BusinessException("msg", "请购明细信息修改失败!");
							}
						}
					}

				}
			}
		}
		if (StringUtil.isNotBlank(ids.toString())){
			//批量更新请购完成标记
			QgglDto qgglDto = new QgglDto();
			ids = new StringBuilder(ids.substring(1));
			qgglDto.setIds(ids.toString());
			qgglDto.setXgry(htglDto.getScry());
			qgglDto.setWcbj("0");
			boolean result_qg = qgglService.updateWcbjs(qgglDto);
			if (!result_qg)
				throw new BusinessException("msg", "请购完成标记更新失败!");
		}
		htglDto.setScbj(htglDto.getFqbj());
		boolean result = delete(htglDto);
		if (!result)
			throw new BusinessException("msg", "合同信息删除失败!");
		if (!CollectionUtils.isEmpty(htglDto.getIds())) {
			HtmxDto htmxDto = new HtmxDto();
			htmxDto.setIds(htglDto.getIds());
			htmxDto.setScry(htglDto.getScry());
			boolean isSuccess = htmxService.delete(htmxDto);
			if (!isSuccess)
				throw new BusinessException("msg", "合同明细删除失败!");
		}
		if(StringUtil.isNotBlank(bchtids)){
			bchtids = bchtids.substring(1);
			htglDto.setIds(bchtids);
			List<HtglDto> htglDtoList = dao.queryBchtZjeList(htglDto);
			if(!htglDtoList.isEmpty()){
				result = updateHtglDtos(htglDtoList);
				if(!result){
					throw new BusinessException("msg", "更新现金额失败!");
				}
			}
		}
		if(!CollectionUtils.isEmpty(ywids))
			shgcService.deleteByYwids(ywids);//删除审核过程,否则审批延期会有脏数据(虽然审核中不允许删除)
		return true;
	}

	/**
	 * 合同明细数量校验
	 */
	@Override
	public Map<String, Object> checkQuantity(HtglDto htglDto) {
		// TODO Auto-generated method stub
		Map<String, Object> map = new HashMap<>();
		List<HtmxDto> htmxDtos = JSON.parseArray(htglDto.getHtmxJson(), HtmxDto.class);
		// 判断是否需要校验起订量
		if (!"0".equals(htglDto.getCheckqdl())) {
			// 查询数量校验信息(物料)
			List<HtmxDto> htmxList_wl = htmxService.selectCheckWlglInfo(htmxDtos);
			if (!CollectionUtils.isEmpty(htmxList_wl)) {
				map.put("message", "合同明细数量不能少于物料起订量！");
				map.put("htmxList_wlgl", htmxList_wl);
				return map;
			}
		}

		// 查询数量校验信息(请购明细)
		List<HtmxDto> htmxList_qgmx = htmxService.selectCheckQgmxInfo(htmxDtos);
		if (!CollectionUtils.isEmpty(htmxList_qgmx)) {
			map.put("message", "合同明细数量不能大于请购现存数量！");
			for (HtmxDto htmxListQgmx : htmxList_qgmx) {
				BigDecimal sl = new BigDecimal(htmxListQgmx.getSysl())
						.subtract(new BigDecimal(htmxListQgmx.getYdsl()));
				if (StringUtil.isNotBlank(htmxListQgmx.getQdl())
						&& new BigDecimal(htmxListQgmx.getQdl()).compareTo(sl) > 0) {
					map.put("message", "合同明细数量不能大于请购现存数量，如不足起订量需增加一条明细！");
					break;
				}
			}
			map.put("htmxList_qgmx", htmxList_qgmx);
			return map;
		}
		return null;
	}

	/**
	 * 合同外发盖章
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean OutgrowthContract(HtglDto htglDto) {
		return dao.outgrowthContract(htglDto);
	}
	
	/**
	 * 根据钉钉实例ID查询合同信息
	 */
	public HtglDto getDtoByDdslid(HtglDto htglDto) {
		return dao.getDtoByDdslid(htglDto);
	}

	@Override
	public List<HtglDto> getDtoListByGys(HtglDto htglDto) {
		return dao.getDtoListByGys(htglDto);
	}

	/**
	 * 获取请购物料数量(月)
	 */
	public List<Map<String,Object>> getContractMapByMon(HtglDto htglDto){
		List<Map<String, Object>> contractMapByMon = dao.getContractMapByMon(htglDto);
		List<Map<String, Object>> maps = new ArrayList<>();
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM");
		try {
			Date dateStart = sdf.parse(htglDto.getShsjstart());
			Date dateEnd = sdf.parse(htglDto.getShsjend());
			Calendar calendarStart = Calendar.getInstance();
			Calendar calendarEnd = Calendar.getInstance();
			calendarStart.setTime(dateStart);
			calendarEnd.setTime(dateEnd);
			//String[] dateStartList = sdf.format(calendarStart.getTime()).split("-");
			//String[] dateEndList = sdf.format(calendarEnd.getTime()).split("-");
			int size = 11;
			//若返回的长度与月份长度不符，找出缺失的月份插入
			if (size!=contractMapByMon.size()){
				for (int i = 0; i < size; i++) {
					Map<String, Object> lsmap = new HashMap<>();
					calendarEnd.add(Calendar.MONTH, -1);
					Object num = 0;
					for (Map<String, Object> stringObjectMap : contractMapByMon) {
						if (sdf.format(calendarEnd.getTime()).equals(stringObjectMap.get("cjrq"))) {
							num = stringObjectMap.get("count");
							break;
						}
					}
					lsmap.put("rq",sdf.format(calendarEnd.getTime()));
					lsmap.put("count",num);
					maps.add(lsmap);
				}
			}
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		return maps;
	}

	/**
	 * 更新合同数据
	 */
	public int updateContract(HtglDto htglDto){
		return dao.updateContract(htglDto);
	}

	/**
	 * 根据钉钉iD获取审批人信息
	 */
	public HtglDto getSprxxByDdid(String ddid){
		return dao.getSprxxByDdid(ddid);
	}

	/**
	 * 根据用户id获取角色信息
	 */
	public List<HtglDto> getSprjsBySprid(String sprid){
		return dao.getSprjsBySprid(sprid);
	}

	/**
	 * 查询未完成到货的合同
	 */
	public List<HtglDto> getPagedDtoKdhList(HtglDto htglDto){
		return dao.getPagedDtoKdhList(htglDto);
	}

	/**
	 * 查询未发票维护的合同
	 */
	public List<HtglDto> getPageListContract(HtglDto htglDto){
		return dao.getPageListContract(htglDto);
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
		Map<String, Object> map = new HashMap<>();
		HtglDto htglDto = dao.getDtoById(shgcDto.getYwid());
		map.put("jgid",htglDto.getSqbm());
		return map;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
		Map<String, Object> map =new HashMap<>();
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>)param.get("ywids");
		HtglDto htglDto = new HtglDto();
		htglDto.setIds(ids);
		List<HtglDto> dtoList = dao.getDtoList(htglDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(HtglDto dto:dtoList){
				list.add(dto.getHtid());
			}
		}
		map.put("list",list);
		return map;
	}

	/**
	 * 检查合同信息是否已完善
	 */
	public boolean checkHtMsg(HtglDto htglDto){
		HtmxDto htmxDto=new HtmxDto();
		htmxDto.setHtid(htglDto.getHtid());
		List<HtmxDto> htmxDtoList=htmxService.getHtmxList(htmxDto);
		if(!CollectionUtils.isEmpty(htmxDtoList)){
			for (HtmxDto dto : htmxDtoList) {
				//若明细中有价格，数量，计划到货日期其中一个为空则提示错误
				if (StringUtils.isBlank(dto.getSl()) || StringUtils.isBlank(dto.getJhdhrq()) || StringUtils.isBlank(dto.getHsdj()))
					return false;
			}
		}
		return true;
	}

	/**
	 * 更新合同管理付款信息
	 */
	public boolean updateFkxxByList(List<HtglDto> htglDtos){
		return dao.updateFkxxByList(htglDtos);
	}

	/**
	 * 校验是否上传附件
	 */
	@Override
	public boolean isUploadAttachments(HtglDto htglDto) {
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_SUBCONTRACT.getCode());
		fjcfbDto.setYwid(htglDto.getHtid());
		List<FjcfbDto> t_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		return !CollectionUtils.isEmpty(t_fjcfbDtos) || !CollectionUtils.isEmpty(htglDto.getFjids());
	}

	/**
	 * 更新合同已付金额和付款中金额
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateHtglDtos(List<HtglDto> htglDtos) {
		return dao.updateHtglDtos(htglDtos)>0;
	}

	@Override
	public List<Map<String,Object>> getCountOrdersByRq(HtglDto htglDto) {
		return dao.getCountOrdersByRq(htglDto);
	}

	@Override
	public Map<String,Object> getPurchaseAuditTimeByRq(HtglDto htglDto) {
		return dao.getPurchaseAuditTimeByRq(htglDto);
	}

	@Override
	public Map<String,Object> getContractAuditTimeByRq(HtglDto htglDto) {
		return dao.getContractAuditTimeByRq(htglDto);
	}

	@Override
	public Map<String,Object> getPurchaseAuditToContractSubmit(HtglDto htglDto) {
		return dao.getPurchaseAuditToContractSubmit(htglDto);
	}

	@Override
	public Map<String,Object> getContractPayAuditTimeByRq(HtglDto htglDto) {
		return dao.getContractPayAuditTimeByRq(htglDto);
	}

	@Override
	public List<Map<String,Object>> getContractAmountByRq(HtglDto htglDto) {
		return dao.getContractAmountByRq(htglDto);
	}
	/**
	 * 合同单统计
	 */
	public List<HtglDto> getCountStatistics(String year){
		return dao.getCountStatistics(year);
	}

	@Override
	public List<Map<String, Object>> getMatterArrivePer(HtglDto htglDto) {
		return dao.getMatterArrivePer(htglDto);
	}

	@Override
	public List<Map<String, Object>> getContractClassArrivePer(HtglDto htglDto) {
		return dao.getContractClassArrivePer(htglDto);
	}

	@Override
	public List<Map<String, Object>> getChargePerArrivePer(HtglDto htglDto) {
		return dao.getChargePerArrivePer(htglDto);
	}

	@Override
	public List<Map<String, Object>> getSupplierArrivePer(HtglDto htglDto) {
		return dao.getSupplierArrivePer(htglDto);
	}

	@Override
	public String getSupplierID() {
		return dao.getSupplierID();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addSaveFrameworkContract(HtglDto htglDto) throws BusinessException{
		// TODO Auto-generated method stub
		htglDto.setSzbj("0");
		htglDto.setSffpwh("0");
		boolean result = insertDto(htglDto);
		if (!result)
			throw new BusinessException("msg","保存框架合同失败！");
		List<HtmxDto> htmxDtos = JSON.parseArray(htglDto.getHtmxJson(), HtmxDto.class);
		for (HtmxDto htmxDto : htmxDtos) {
			htmxDto.setHtid(htglDto.getHtid());
			htmxDto.setHtmxid(StringUtil.generateUUID());
			htmxDto.setLrry(htglDto.getLrry());
		}
		result = htmxService.insertFrameworks(htmxDtos);
		if (!result)
			throw new BusinessException("msg","保存框架合同明细失败！");

			// 文件复制到正式文件夹，插入信息至正式表
			if (!CollectionUtils.isEmpty(htglDto.getFjids())) {
				for (int i = 0; i < htglDto.getFjids().size(); i++) {
					fjcfbService.save2RealFile(htglDto.getFjids().get(i), htglDto.getHtid());
				}
			}

		return true;
	}
	/**
	 * @description 新增保存框架合同
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean modFrameworkSaveContract(HtglDto htglDto) throws BusinessException {
		boolean isSuccess;
		isSuccess = updateContract(htglDto)>0;
		List<HtmxDto> htmxDtos = JSON.parseArray(htglDto.getHtmxJson(), HtmxDto.class);
		//钉钉审批回调没有明细
		if (!CollectionUtils.isEmpty(htmxDtos)){
			List<HtmxDto> addHtmxDtos = new ArrayList<>();
			List<HtmxDto> modHtmxDtos = new ArrayList<>();
			List<HtmxDto> yHtmxs = htmxService.getListByHtid(htglDto.getHtid());
			for (HtmxDto htmxDto : htmxDtos) {
				//剩下的是要删除的
				yHtmxs.removeIf(e->e.getHtmxid().equals(htmxDto.getHtmxid()));
				//新增
				if (StringUtil.isBlank(htmxDto.getHtmxid())){
					htmxDto.setHtmxid(StringUtil.generateUUID());
					htmxDto.setHtid(htglDto.getHtid());
					htmxDto.setLrry(htglDto.getXgry());
					addHtmxDtos.add(htmxDto);
				}else {
					//修改
					htmxDto.setXgry(htglDto.getXgry());
					modHtmxDtos.add(htmxDto);
				}
			}
			if (!CollectionUtils.isEmpty(addHtmxDtos)){
				isSuccess = htmxService.insertFrameworks(addHtmxDtos);
				if (!isSuccess){
					throw new BusinessException("msg","新增框架合同明细失败！");
				}
			}
			if (!CollectionUtils.isEmpty(modHtmxDtos)){
				isSuccess = htmxService.updateHtmxDtos(modHtmxDtos);
				if (!isSuccess){
					throw new BusinessException("msg","删除框架合同明细失败！");
				}
			}
			if (!CollectionUtils.isEmpty(yHtmxs)){
				HtmxDto htmxDto_del = new HtmxDto();
				List<String> htmxids = new ArrayList<>();
				for (HtmxDto yhtmx : yHtmxs) {
					htmxids.add(yhtmx.getHtmxid());
				}
				htmxDto_del.setScry(htglDto.getXgry());
				htmxDto_del.setScbj("1");
				htmxDto_del.setIds(htmxids);
				isSuccess = htmxService.deleteHtmxDtos(htmxDto_del);
				if (!isSuccess){
					throw new BusinessException("msg","删除框架合同明细失败！");
				}
			}

			// 文件复制到正式文件夹，插入信息至正式表
			if (!CollectionUtils.isEmpty(htglDto.getFjids())) {
				for (int i = 0; i < htglDto.getFjids().size(); i++) {
					fjcfbService.save2RealFile(htglDto.getFjids().get(i), htglDto.getHtid());
				}
			}

		}
		return isSuccess;
	}

	@Override
	public HtglDto getContractByGysWithWgq(HtglDto htglDto) {
		return dao.getContractByGysWithWgq(htglDto);
	}

	@Override
	public List<HtglDto> getNotAllInvoice(HtglDto htglDto) {
		return dao.getNotAllInvoice(htglDto);
	}

	@Override
	public List<HtglDto> getNotAllPay(HtglDto htglDto) {
		return dao.getNotAllPay(htglDto);
	}

	@Override
	public List<HtglDto> getExistStock(HtglDto htglDto) {
		return dao.getExistStock(htglDto);
	}

	@Override
	public List<HtglDto> queryByHtid(HtglDto htglDto) {
		return dao.queryByHtid(htglDto);
	}

	@Override
	public HtglDto queryBchtglDto(HtglDto htglDto) {
		return dao.queryBchtglDto(htglDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveSzht(HtglDto htglDto) {
		if (!CollectionUtils.isEmpty(htglDto.getFjids())) {
			for (int i = 0; i < htglDto.getFjids().size(); i++) {
				fjcfbService.save2RealFile(htglDto.getFjids().get(i), htglDto.getHtid());
			}
		}
		return dao.updateContract(htglDto)>0;
	}

	@Override
	public Map<String, Object> queryOverTime(String sftx) {
		Map<String, Object> resultMap = new HashMap<>();
		XtszDto xtszDto = xtszService.selectById("approval.ht");
		if (xtszDto != null) {
			if (StringUtil.isNotBlank(xtszDto.getSzz())) {
				Map<String, Object> map = JSON.parseObject(xtszDto.getSzz(), new TypeReference<Map<String, Object>>() {
				});
				if(map.get("postCg")!=null && map.get("postEnd")!=null && map.get("overTime")!=null && map.get("overdueTime")!=null && map.get("rule")!=null && map.get("unit")!=null){
					String overdueTime = map.get("overdueTime").toString();
					String overTime = map.get("overTime").toString();
					String postEnd = map.get("postEnd").toString();
					String unit = map.get("unit").toString();
					String rule = map.get("rule").toString();
					String postCg = map.get("postCg").toString();
					resultMap.put("postEnd",postEnd);
					resultMap.put("overTime",overTime);
					resultMap.put("overdueTime",overdueTime);
					BigDecimal overdueTimeB = new BigDecimal(overdueTime);
					BigDecimal overTimeB = new BigDecimal(overTime);
					HtglDto htglDto = new HtglDto();
					htglDto.setShxx_gwmc(postEnd);
					htglDto.setPostCg(postCg);
					if(StringUtil.isNotBlank(sftx)){
						htglDto.setSftx(sftx);
					}
					List<HtglDto> htglDtoList = dao.queryOverTime(htglDto);
					List<HtglDto> overdueList = new ArrayList<>();
					List<HtglDto> overList = new ArrayList<>();
					if(!CollectionUtils.isEmpty(htglDtoList)){
						if("0".equals(rule)){
							if("day".equals(unit)){
								overdueTimeB = new BigDecimal(overdueTime).multiply(new BigDecimal("12"));
								overTimeB = new BigDecimal(overTime).multiply(new BigDecimal("12"));
							}
							for (HtglDto htgl:htglDtoList){
								htgl.setOverTime(overTimeB.toString());
								htgl.setOverdueTime(overdueTimeB.toString());
								if(new BigDecimal(htgl.getTimeLag()).compareTo(overTimeB)>0){
									overList.add(htgl);
								}else if(new BigDecimal(htgl.getTimeLag()).compareTo(overdueTimeB)>0){
									overdueList.add(htgl);
								}
							}
						}else{
							BigDecimal dayHouts = new BigDecimal("8");
							XtszDto attendanceTime = xtszService.selectById("attendance.time");
							if(attendanceTime!=null){
								if(StringUtil.isNotBlank(attendanceTime.getSzz())){
									Map<String, Object> attendanceTimeMap = JSON.parseObject(attendanceTime.getSzz(), new TypeReference<Map<String, Object>>(){});
									if(attendanceTimeMap.get("officeHours")!=null
											&& attendanceTimeMap.get("closingTime")!=null && attendanceTimeMap.get("beginTime")!=null  && attendanceTimeMap.get("endTime")!=null){
										String officeHours = attendanceTimeMap.get("officeHours").toString();
										String closingTime = attendanceTimeMap.get("closingTime").toString();
										String beginTime = attendanceTimeMap.get("beginTime").toString();
										String endTime = attendanceTimeMap.get("endTime").toString();
										dayHouts = commonservice.getHours(officeHours,closingTime).subtract(commonservice.getHours(beginTime,endTime));
										if("day".equals(unit)){
											overdueTimeB = new BigDecimal(overdueTime).multiply(dayHouts);
											overTimeB = new BigDecimal(overTime).multiply(dayHouts);
										}
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
										SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
										DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
										Calendar calendar = Calendar.getInstance();
										Date currentDate = calendar.getTime();
										String endString = sdf.format(currentDate);
										String endFormatter = sdf2.format(currentDate);
										Timestamp endStamp = new Timestamp(System.currentTimeMillis());
										for (HtglDto htgl:htglDtoList){
											BigDecimal timeString = new BigDecimal(htgl.getTimeLag());
											Timestamp shsjStamp = Timestamp.valueOf(htgl.getQgshsj());
											BigDecimal workDayHours = new BigDecimal("0");
											String startString = sdf.format(shsjStamp);
											//判断今天和开始岗位的审核时间是否为节假日星期天
											XxdyDto xxdyDto = new XxdyDto();
											xxdyDto.setDylxcsdm("Holidays");
											xxdyDto.setBeginTime(startString);
											xxdyDto.setEndTime(endString);
											List<XxdyDto> xxdyDtoList = xxdyService.queryHolidays(xxdyDto);
											List<LocalDate> localDates = commonservice.getAllDates(startString,endString);
											LocalDateTime dateTime = LocalDateTime.parse(htgl.getQgshsj(), formatter);
											String outputString = dateTime.format(outputFormatter);
											LocalDateTime endDateTime = LocalDateTime.parse(endFormatter, formatter);
											String endOutputString = endDateTime.format(outputFormatter);
											LocalDate localDate = dateTime.toLocalDate();
											LocalDate endLocalDate = endDateTime.toLocalDate();
											if (localDate.isEqual(endLocalDate)) {
												if(!org.apache.commons.collections.CollectionUtils.isEmpty(xxdyDtoList)){
													timeString = new BigDecimal("0");
												}else {
													timeString = commonservice.oneDayGetHours(outputString,endOutputString,officeHours,closingTime,beginTime,endTime);
												}
											}else{
												if(!CollectionUtils.isEmpty(xxdyDtoList)){
													int i = 0;
													BigDecimal sumTime = new BigDecimal("0");
													boolean startFlg = false;
													boolean endFlg = false;
													for (XxdyDto xxdy:xxdyDtoList){
														if("1".equals(xxdy.getFlag())){
															if(xxdy.getDyxx().equals(startString)){
																sumTime = sumTime.add(new BigDecimal("24").subtract(commonservice.calculateTime(shsjStamp)));
																startFlg = true;
															}
															if(xxdy.getDyxx().equals(endString)){
																sumTime = sumTime.add(commonservice.calculateTime(endStamp));
																endFlg = true;
															}
														}else{
															i = i+1;
														}
													}
													timeString = timeString.subtract(sumTime).subtract(new BigDecimal(i).multiply(new BigDecimal("24")));
													int l = localDates.size()-xxdyDtoList.size();
													if(!startFlg){
														workDayHours = workDayHours.add(commonservice.beginDayGetHours(outputString,officeHours,closingTime,beginTime,endTime));
														l = l - 1;
														timeString = timeString.subtract(new BigDecimal("24").subtract(commonservice.calculateTime(shsjStamp)));
													}
													if(!endFlg){
														workDayHours = workDayHours.add(commonservice.endDayGetHours(endOutputString,officeHours,closingTime,beginTime,endTime));
														l = l - 1;
														timeString = timeString.subtract(commonservice.calculateTime(endStamp));
													}
													String dayString = String.valueOf(l);
													timeString = timeString.add(workDayHours).subtract(dayHouts.multiply(new BigDecimal(dayString)));
												}else{
													String dayInt = String.valueOf(localDates.size()-2);
													workDayHours = workDayHours.add(commonservice.beginDayGetHours(outputString,officeHours,closingTime,beginTime,endTime));
													workDayHours = workDayHours.add(commonservice.endDayGetHours(endOutputString,officeHours,closingTime,beginTime,endTime));
													timeString = timeString.subtract(new BigDecimal(dayInt).multiply(dayHouts))
															.subtract(new BigDecimal("24").subtract(commonservice.calculateTime(shsjStamp)))
															.subtract(commonservice.calculateTime(endStamp))
															.add(workDayHours);
												}
											}
											htgl.setTimeLag(timeString.toString());
											htgl.setOverTime(overTimeB.toString());
											htgl.setOverdueTime(overdueTimeB.toString());
											if(timeString.compareTo(overTimeB)>0){
												overList.add(htgl);
											}else if(timeString.compareTo(overdueTimeB)>0){
												overdueList.add(htgl);
											}
										}
									}
								}
							}
						}
					}
					if(!CollectionUtils.isEmpty(overdueList)){
						resultMap.put("overdueList",overdueList);
					}
					if(!CollectionUtils.isEmpty(overList)){
						resultMap.put("overList",overList);
					}
				}
			}
		}
		return resultMap;
	}

	@Override
	public List<HtglDto> queryOverTimeTable(HtglDto htglDto) {
		List<HtglDto> htglDtoList = new ArrayList<>();
		if(StringUtil.isNotBlank(htglDto.getFlg())){
			Map<String,Object> map = queryOverTime("");
			if(!CollectionUtils.isEmpty(map)){
				List<HtglDto> overList = (List<HtglDto>) map.get("overList");
				List<HtglDto> overdueList = (List<HtglDto>) map.get("overdueList");
				if(!CollectionUtils.isEmpty(overList) && "cs".equals(htglDto.getFlg())){
					htglDtoList = overList;
				}
				if(!CollectionUtils.isEmpty(overdueList) && "jj".equals(htglDto.getFlg())){
					htglDtoList = overdueList;
				}
			}
		}
		if(!CollectionUtils.isEmpty(htglDtoList)){
			try {
				shgcService.addShgcxxByYwid(htglDtoList, AuditTypeEnum.AUDIT_CONTRACT.getCode(), "zt", "htid",
						new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
				shgcService.addShgcxxByYwid(htglDtoList, AuditTypeEnum.AUDIT_CONTRACT_OA.getCode(), "zt", "htid",
						new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		}
		htglDtoList.sort(Comparator.comparing(HtglDto::getDjh));
		return htglDtoList;
	}

	@Override
	public List<HtglDto> getHtOverTimeTable(HtglDto htglDto) {
		XtszDto xtszDto = xtszService.selectById("approval.ht");
		List<HtglDto> list = new ArrayList<>();
		if (xtszDto != null) {
			if (StringUtil.isNotBlank(xtszDto.getSzz())) {
				Map<String, Object> map = JSON.parseObject(xtszDto.getSzz(), new TypeReference<Map<String, Object>>() {
				});
				if (map.get("postCg") != null && map.get("overTime") != null && map.get("rule") != null && map.get("unit") != null) {
					htglDto.setOverTime(map.get("overTime").toString());
					htglDto.setUnit(map.get("unit").toString());
					htglDto.setRule(map.get("rule").toString());
					htglDto.setPostCg(map.get("postCg").toString());
					list =dao.getPagedOverTimeList(htglDto);
				}
			}
		}
		return list;
	}

	@Override
	public Map<String, Object> getOverTimeTableMap(String rqstart, String rqend) {
		Map<String,Object> resultMap = new HashMap<>();
		HtglDto htglDto = new HtglDto();
		htglDto.setShsjstart(rqstart);
		htglDto.setShsjend(rqend);
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(rqend, inputFormatter);
		String year = String.valueOf(date.getYear());
		resultMap.put("shsjYear",year);
		resultMap.put("shsjTime",rqstart+"~"+rqend);
		String pjsxString = "";
		XtszDto xtszDto = xtszService.selectById("approval.ht");
		List<HtglDto> list = new ArrayList<>();
		if (xtszDto != null) {
			if (StringUtil.isNotBlank(xtszDto.getSzz())) {
				Map<String, Object> map = JSON.parseObject(xtszDto.getSzz(), new TypeReference<Map<String, Object>>() {
				});
				if (map.get("overTime") != null && map.get("rule") != null && map.get("unit") != null) {
					htglDto.setOverTime(map.get("overTime").toString());
					htglDto.setUnit(map.get("unit").toString());
					htglDto.setRule(map.get("rule").toString());
					List<Map<String, Object>> tableMap =dao.getOverTimeTableMap(htglDto);
					resultMap.put("Table",tableMap);
					String timeliness = dao.getTimeliness(htglDto);
					if(StringUtil.isNotBlank(timeliness)){
						String unitString = "";
						String ruleString = "";
						if("hour".equals(map.get("unit").toString())){
							unitString = "小时";
						}
						if("day".equals(map.get("unit").toString())){
							unitString = "天";
						}
						if("0".equals(map.get("rule").toString())){
							ruleString = "不去除节假日";
						}
						if("1".equals(map.get("rule").toString())){
							ruleString = "去除节假日";
						}
						pjsxString = "平均时效性("+ruleString+"):"+timeliness+unitString;
					}
					htglDto.setCjrq(year);
					List<HtglDto> htglDtoList = dao.getMonthTj(htglDto);
					resultMap.put("month",htglDtoList);
					List<HtglDto> timeList = dao.getTimeTj(htglDto);
					resultMap.put("time",timeList);
					List<HtglDto> percentageList = dao.getPercentageTj(htglDto);
					resultMap.put("percentage",percentageList);

				}
			}
		}
		resultMap.put("timeliness",pjsxString);
		return resultMap;
	}

	@Override
	public Map<String, Object> getGrOverTimeTableMap(HtglDto htglDto) {
		Map<String,Object> resultMap = new HashMap<>();
		List<HtglDto> htglDtoList = dao.getGrOverTimeTableMap(htglDto);
		Map<String, BigDecimal> htglDtoMap = new HashMap<>();
		List<HtglDto> groupByMonthList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(htglDtoList)){
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			if("1".equals(htglDto.getFlg())){
				BigDecimal dayHouts = new BigDecimal("8");
				XtszDto attendanceTime = xtszService.selectById("attendance.time");
				if(attendanceTime!=null){
					if(StringUtil.isNotBlank(attendanceTime.getSzz())){
						Map<String, Object> attendanceTimeMap = JSON.parseObject(attendanceTime.getSzz(), new TypeReference<Map<String, Object>>(){});
						if(attendanceTimeMap.get("officeHours")!=null
								&& attendanceTimeMap.get("closingTime")!=null && attendanceTimeMap.get("beginTime")!=null  && attendanceTimeMap.get("endTime")!=null){
							String officeHours = attendanceTimeMap.get("officeHours").toString();
							String closingTime = attendanceTimeMap.get("closingTime").toString();
							String beginTime = attendanceTimeMap.get("beginTime").toString();
							String endTime = attendanceTimeMap.get("endTime").toString();
							dayHouts = commonservice.getHours(officeHours,closingTime).subtract(commonservice.getHours(beginTime,endTime));
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
							DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
							for (HtglDto htgl:htglDtoList){
								LocalDate lodate = LocalDate.parse(htgl.getPostEnd(), formatter);
								String endString = lodate.format(formatter2);
								String endFormatter = htgl.getPostEnd();
								Timestamp endStamp = Timestamp.valueOf(htgl.getPostEnd());
								BigDecimal timeString = new BigDecimal(htgl.getTimeLag());
								Timestamp shsjStamp = Timestamp.valueOf(htgl.getPostStart());
								BigDecimal workDayHours = new BigDecimal("0");
								String startString = sdf.format(shsjStamp);
								XxdyDto xxdyDto = new XxdyDto();
								xxdyDto.setDylxcsdm("Holidays");
								xxdyDto.setBeginTime(startString);
								xxdyDto.setEndTime(endString);
								List<XxdyDto> xxdyDtoList = xxdyService.queryHolidays(xxdyDto);
								List<LocalDate> localDates = commonservice.getAllDates(startString,endString);
								LocalDateTime dateTime = LocalDateTime.parse(htgl.getPostStart(), formatter);
								String outputString = dateTime.format(outputFormatter);
								LocalDateTime endDateTime = LocalDateTime.parse(endFormatter, formatter);
								String endOutputString = endDateTime.format(outputFormatter);
								LocalDate localDate = dateTime.toLocalDate();
								LocalDate endLocalDate = endDateTime.toLocalDate();
								if (localDate.isEqual(endLocalDate)) {
									if(!org.apache.commons.collections.CollectionUtils.isEmpty(xxdyDtoList)){
										timeString = new BigDecimal("0");
									}else {
										timeString = commonservice.oneDayGetHours(outputString,endOutputString,officeHours,closingTime,beginTime,endTime);
									}
								}else {
									if(!CollectionUtils.isEmpty(xxdyDtoList)){
										int i = 0;
										BigDecimal sumTime = new BigDecimal("0");
										boolean startFlg = false;
										boolean endFlg = false;
										for (XxdyDto xxdy:xxdyDtoList){
											if("1".equals(xxdy.getFlag())){
												if(xxdy.getDyxx().equals(startString)){
													sumTime = sumTime.add(new BigDecimal("24").subtract(commonservice.calculateTime(shsjStamp)));
													startFlg = true;
												}
												if(xxdy.getDyxx().equals(endString)){
													sumTime = sumTime.add(commonservice.calculateTime(endStamp));
													endFlg = true;
												}
											}else{
												i = i+1;
											}
										}
										timeString = timeString.subtract(sumTime).subtract(new BigDecimal(i).multiply(new BigDecimal("24")));
										int l = localDates.size()-xxdyDtoList.size();
										if(!startFlg){
											workDayHours = workDayHours.add(commonservice.beginDayGetHours(outputString,officeHours,closingTime,beginTime,endTime));
											l = l - 1;
											timeString = timeString.subtract(new BigDecimal("24").subtract(commonservice.calculateTime(shsjStamp)));
										}
										if(!endFlg){
											workDayHours = workDayHours.add(commonservice.endDayGetHours(endOutputString,officeHours,closingTime,beginTime,endTime));
											l = l - 1;
											timeString = timeString.subtract(commonservice.calculateTime(endStamp));
										}
										String dayString = String.valueOf(l);
										timeString = timeString.add(workDayHours).subtract(dayHouts.multiply(new BigDecimal(dayString)));
									}else{
										String dayInt = String.valueOf(localDates.size()-2);
										workDayHours = workDayHours.add(commonservice.beginDayGetHours(outputString,officeHours,closingTime,beginTime,endTime));
										workDayHours = workDayHours.add(commonservice.endDayGetHours(endOutputString,officeHours,closingTime,beginTime,endTime));
										timeString = timeString.subtract(new BigDecimal(dayInt).multiply(dayHouts))
												.subtract(new BigDecimal("24").subtract(commonservice.calculateTime(shsjStamp)))
												.subtract(commonservice.calculateTime(endStamp))
												.add(workDayHours);
									}
								}
								htgl.setTimeLag(timeString.toString());
							}
						}
					}
				}
			}
			DateTimeFormatter formatterMM = DateTimeFormatter.ofPattern("MM");
			for(HtglDto htglDto1:htglDtoList){
				LocalDate lodate = LocalDate.parse(htglDto1.getPostEnd(), formatter);
				String endString = lodate.format(formatterMM);
				htglDto1.setPostEnd(endString);
			}
			htglDtoMap = htglDtoList.stream()
					.collect(Collectors.groupingBy(
							HtglDto::getPostEnd,
							Collectors.mapping(
									obj -> new BigDecimal(obj.getTimeLag()),
									Collectors.collectingAndThen(
											Collectors.toList(), // 收集为 List<BigDecimal>
											values -> {
												BigDecimal sum = values.stream()
														.reduce(BigDecimal.ZERO, BigDecimal::add); // 计算总和
												return sum.divide(new BigDecimal(values.size()), 2, RoundingMode.HALF_UP); // 计算平均值并保留两位小数
											}
									)
							)
					));
			htglDtoMap.forEach((group, average) -> {
				HtglDto htglDtoT = new HtglDto();
				htglDtoT.setCjrq(group);
				htglDtoT.setCount(average.toString());
				groupByMonthList.add(htglDtoT);
			});
		}
		resultMap.put("month",groupByMonthList);
		return resultMap;
	}


	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public void sendTimeOutData() throws ApiException {
		Map<String,Object> map = queryOverTime("1");
		List<HtglDto> htList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(map)){
			List<HtglDto> overList = (List<HtglDto>) map.get("overList");
			List<HtglDto> overdueList = (List<HtglDto>) map.get("overdueList");
			if(!CollectionUtils.isEmpty(overList)){
				for (HtglDto htglDto:overList){
					htglDto.setFlg("cs");
				}
				htList.addAll(overList);
			}
			if(!CollectionUtils.isEmpty(overdueList)){
				for (HtglDto htglDto:overdueList){
					htglDto.setFlg("jj");
				}
				htList.addAll(overdueList);
			}
		}
		Map<String,Object> qgMap = qgglService.queryOverTime("1");
		List<QgglDto> qgList = new ArrayList<>();
		if(!CollectionUtils.isEmpty(qgMap)){
			List<QgglDto> qgOverList = (List<QgglDto>) qgMap.get("overList");
			List<QgglDto> qgOverdueList = (List<QgglDto>) qgMap.get("overdueList");
			if(!CollectionUtils.isEmpty(qgOverList)){
				for (QgglDto qgglDto:qgOverList){
					qgglDto.setFlg("cs");
				}
				qgList.addAll(qgOverList);
			}
			if(!CollectionUtils.isEmpty(qgOverdueList)){
				for (QgglDto qgglDto:qgOverdueList){
					qgglDto.setFlg("jj");
				}
				qgList.addAll(qgOverdueList);
			}
		}
		String accessToken = talkUtil.getRobotAccessToken();
		List<DbxxDto> dbxxDtos = new ArrayList<>();
		List<QgglDto> qgglDtos = new ArrayList<>();
		if(!CollectionUtils.isEmpty(htList)){
			Map<String, List<HtglDto>> groupedHtByDdid = htList.stream()
					.collect(Collectors.groupingBy(HtglDto::getDdid, Collectors.toList()));
			groupedHtByDdid.forEach((key, value) -> {
				if(StringUtil.isNotBlank(key)){
					String unionid = "";
					try {
						DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
						OapiV2UserGetRequest req = new OapiV2UserGetRequest();
						req.setUserid(key);
						req.setLanguage("zh_CN");
						OapiV2UserGetResponse rsp = client.execute(req, accessToken);
						Map<String, Object> mapT = JSON.parseObject(rsp.getBody(), new TypeReference<Map<String, Object>>(){});
						String s  = com.alibaba.fastjson.JSONObject.toJSONString(mapT.get("result"));
						Map<String, Object> map2 = JSON.parseObject(s, new TypeReference<Map<String, Object>>(){});
						unionid = map2.get("unionid").toString();
					} catch (ApiException e) {
						e.printStackTrace();
					}
					for (HtglDto htglDto : value) {
						String subject="";
						String description = "";
						if("jj".equals(htglDto.getFlg())){
							if(StringUtil.isNotBlank(htglDto.getHtnbbh())){
								subject = "合同单"+ htglDto.getHtnbbh()+ "(" + htglDto.getDjh() +")即将超时，请尽快处理!("+systemname+")";
							}else{
								subject = "请购单" + htglDto.getDjh() +"还有物料未做合同单，即将超时，请尽快处理!("+systemname+")";
							}
							String timeString = new BigDecimal(htglDto.getOverTime()).subtract(new BigDecimal(htglDto.getTimeLag())).toString();
							description = "请购单"+htglDto.getDjh()+"审核通过已经过去"+htglDto.getTimeLag()+"小时,还有"+ timeString + "小时即将超时，";
							if(StringUtil.isNotBlank(htglDto.getHtnbbh())){
								description = description + "请尽快审核通过合同单"+htglDto.getHtnbbh();
							}else{
								description = description + "请尽快进行采购！";
							}
						}
						if("cs".equals(htglDto.getFlg())){
							if(StringUtil.isNotBlank(htglDto.getHtnbbh())){
								subject = "合同单"+ htglDto.getHtnbbh()+ "(" + htglDto.getDjh() +")已经超时，请尽快处理!("+systemname+")";
							}else{
								subject = "请购单" + htglDto.getDjh() +"还有物料未做合同单，已经超时，请尽快处理!("+systemname+")";
							}
							String timeString = new BigDecimal(htglDto.getTimeLag()).subtract(new BigDecimal(htglDto.getOverTime())).toString();
							description = "请购单"+htglDto.getDjh()+"审核通过已经过去"+htglDto.getTimeLag()+"小时,已经超时"+ timeString + "小时，";
							if(StringUtil.isNotBlank(htglDto.getHtnbbh())){
								description = description + "请尽快审核通过合同单"+htglDto.getHtnbbh();
							}else{
								description = description + "请尽快进行采购！";
							}
						}
						boolean result = false;
						try {
							result = talkUtil.sendBackLog(unionid,subject,description,accessToken);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
						if(result){
							DbxxDto dbxxDto = new DbxxDto();
							dbxxDto.setId(StringUtil.isNotBlank(htglDto.getHtid())?htglDto.getHtid():"1");
							dbxxDto.setAutoid(htglDto.getQgid());
							dbxxDtos.add(dbxxDto);
						}
					}
				}
			});
		}
		if(!CollectionUtils.isEmpty(qgList)){
			Map<String, List<QgglDto>> groupedQgByDdid = qgList.stream()
					.collect(Collectors.groupingBy(QgglDto::getDdid, Collectors.toList()));
			groupedQgByDdid.forEach((key, value) -> {
				if(StringUtil.isNotBlank(key)){
					String unionid = "";
					try {
						DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/get");
						OapiV2UserGetRequest req = new OapiV2UserGetRequest();
						req.setUserid(key);
						req.setLanguage("zh_CN");
						OapiV2UserGetResponse rsp = client.execute(req, accessToken);
						Map<String, Object> mapT = JSON.parseObject(rsp.getBody(), new TypeReference<Map<String, Object>>(){});
						String s  = com.alibaba.fastjson.JSONObject.toJSONString(mapT.get("result"));
						Map<String, Object> map2 = JSON.parseObject(s, new TypeReference<Map<String, Object>>(){});
						unionid = map2.get("unionid").toString();
					} catch (ApiException e) {
						e.printStackTrace();
					}
					for (QgglDto qgglDto : value) {
						String subject="";
						String description = "";
						if("jj".equals(qgglDto.getFlg())){
							subject = "请购单"+ qgglDto.getDjh() +"即将超时，请尽快处理!("+systemname+")";
							String timeString = new BigDecimal(qgglDto.getOverTime()).subtract(new BigDecimal(qgglDto.getTimeLag())).toString();
							description = "请购单"+qgglDto.getDjh()+"部门审核通过已经"+qgglDto.getTimeLag()+"小时,还有"+ timeString + "小时即将超时，请尽快审核通过！";
						}
						if("cs".equals(qgglDto.getFlg())){
							subject = "请购单"+ qgglDto.getDjh() +"已经超时，请尽快处理!("+systemname+")";
							String timeString = new BigDecimal(qgglDto.getTimeLag()).subtract(new BigDecimal(qgglDto.getOverTime())).toString();
							description = "请购单"+qgglDto.getDjh()+"部门审核通过已经"+qgglDto.getTimeLag()+"小时,已经超时"+ timeString + "小时，请尽快审核通过！";
						}
						boolean result = false;
						try {
							result = talkUtil.sendBackLog(unionid,subject,description,accessToken);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
						if(result){
							qgglDto.setSftx("1");
							qgglDtos.add(qgglDto);
						}
					}
				}
			});
		}
		if(!CollectionUtils.isEmpty(dbxxDtos)){
			dbxxService.insertList(dbxxDtos);
		}
		if(!CollectionUtils.isEmpty(qgglDtos)){
			qgglService.updateSftxList(qgglDtos);
		}
	}



}
