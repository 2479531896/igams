package com.matridx.igams.storehouse.service.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.matridx.igams.production.dao.entities.*;
import com.matridx.igams.production.service.svcinterface.ICpxqjhService;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.igams.storehouse.dao.entities.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.matridxsql.CurrentStockDao;
import com.matridx.igams.production.dao.matridxsql.IPU_AppVouchDao;
import com.matridx.igams.production.dao.matridxsql.PO_PodetailsDao;
import com.matridx.igams.production.dao.matridxsql.RdRecordDao;
import com.matridx.igams.production.dao.matridxsql.RdRecordsDao;
import com.matridx.igams.production.dao.matridxsql.SCM_CommitEntryBufferDao;
import com.matridx.igams.production.dao.matridxsql.SCM_ItemDao;
import com.matridx.igams.production.dao.matridxsql.TransVouchDao;
import com.matridx.igams.production.dao.matridxsql.TransVouchsDao;
import com.matridx.igams.production.dao.matridxsql.UA_IdentityDao;
import com.matridx.igams.production.service.svcinterface.IHtmxService;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.storehouse.dao.post.IHwxxDao;
import com.matridx.igams.storehouse.dao.post.IRkglDao;
import com.matridx.igams.storehouse.service.svcinterface.ICkhwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IDhxxService;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.IRkcglService;
import com.matridx.igams.storehouse.service.svcinterface.IRkglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.springframework.util.CollectionUtils;

@Service
public class RkglServiceImpl extends BaseBasicServiceImpl<RkglDto, RkglModel, IRkglDao>
		implements IRkglService, IAuditService {
	@Autowired
	RdRecordDao rdRecordDao;

	@Autowired
	RdRecordsDao rdRecordsDao;

	@Autowired
	IHwxxDao hwxxDao;

	@Autowired
	IHwxxService hwxxService;

	@Autowired
	DingTalkUtil talkUtil;

	@Autowired
	IXxglService xxglService;

	@Autowired
	ICommonService commonservice;
	
	@Autowired
	IShgcService shgcService;
	
	@Autowired
	IRkcglService rkcglService;
	
	@Autowired
	ICkhwxxService ckhwxxService;
	
	@Autowired
	PO_PodetailsDao pO_PodetailsDao;
	
	@Autowired
	CurrentStockDao currentStockDao;
	
	@Autowired
	IPU_AppVouchDao PU_AppVouchDao;
	
	@Autowired
	IQgmxService qgmxService;
	
	@Autowired
	IHtmxService htmxService;
	
	@Autowired
	IDhxxService dhxxService;
	
	@Autowired
	TransVouchDao transVouchDao;
	
	@Autowired
	TransVouchsDao transVouchsDao;
	
	@Autowired
	IRdRecordService rdRecordService;
	
	@Autowired
	UA_IdentityDao uA_IdentityDao;

	@Autowired
	SCM_ItemDao sCM_ItemDao;
	
	@Autowired
	SCM_CommitEntryBufferDao sCM_CommitEntryBufferDao;
	
	@Autowired
	IJcsjService jcsjService;
	
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	
	@Autowired
	ICommonService commonService;
	@Autowired
	private IQgglService qgglService;
	@Autowired
	ICpxqjhService cpxqjhService;
	@Value("${matridx.rabbit.systemreceiveflg:}")
	private String systemreceiveflg;
	@Value("${sqlserver.matridxds.flag:}")
	private String matridxdsflag;
	
	private final Logger log = LoggerFactory.getLogger(RkglServiceImpl.class);
	/**
	 * 生成入库单号
	 */
	@Override
	public String generatePutInStorageCode(RkglDto rkglDto) {
		// 入库单号：RK-年月日-流水号
		String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		String prefix = "RK-" + date + "-";
		// 查询流水号
		String serial = dao.getRkdhSerial(prefix);
		return prefix + serial;
	}

	/**
	 * 判断入库单是否重复
	 * 
	 * @return
	 */
	@Override
	public boolean isRkdhRepeat(String rkdh, String rkid) {
		if (StringUtil.isNotBlank(rkdh)) {
			RkglDto rkglDto = new RkglDto();
			rkglDto.setRkdh(rkdh);
			rkglDto.setRkid(rkid);
			List<RkglDto> rkglDtos = dao.queryByRkdh(rkglDto);
			return rkglDtos == null || rkglDtos.size() <= 0;
		}
		return true;
	}

	/**
	 * 保存入库信息
	 * 
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addSavePutInStorage(RkglDto rkglDto) throws BusinessException{
		boolean result = insertDto(rkglDto);
		if (!result)
			return false;
		List<HwxxDto> hwxxDtos = JSON.parseArray(rkglDto.getHwxx_json(), HwxxDto.class);
		if ("1".equals(rkglDto.getBs())){
			List<HwxxDto> hwxxDtoList = new ArrayList<>();
			for (HwxxDto hwxxDto : hwxxDtos) {
				HwxxDto dto = new HwxxDto();
				dto.setBhghw(hwxxDto.getHwid());
				dto.setHwid(StringUtil.generateUUID());
				dto.setSl(hwxxDto.getYclsl());
				dto.setRkid(rkglDto.getRkid());
				dto.setCkid(rkglDto.getCkid());
				dto.setRkry(rkglDto.getLrry());
				dto.setRkrq(rkglDto.getRkrq());
				dto.setKwbh(hwxxDto.getKwbh());
				dto.setScbj("0");
				dto.setRkbz(hwxxDto.getRkbz());
				dto.setLrry(rkglDto.getLrry());
				hwxxDtoList.add(dto);

				hwxxDto.setYclsbj("1");
				hwxxDto.setRkbz(null);
				hwxxDto.setKwbh(null);
				hwxxDto.setByycls(hwxxDto.getYclsl());
				hwxxDto.setYclsl(null);
			}
			boolean result_t = hwxxService.updateHwxxDtos(hwxxDtos);
			if (!result_t)
				throw new BusinessException("msg", "更新货物已处理数量失败！");
			result_t = hwxxService.copyInsertHwxxList(hwxxDtoList);
			if (!result_t)
				throw new BusinessException("msg", "更新货物信息失败！");
		}else {
			// 更新货物信息
			StringBuilder ids = new StringBuilder();
			for (HwxxDto hwxxDto : hwxxDtos) {
				hwxxDto.setHwid(hwxxDto.getHwid());
				hwxxDto.setRkid(rkglDto.getRkid());
				hwxxDto.setCkid(rkglDto.getCkid());
				hwxxDto.setRkry(rkglDto.getLrry());
				hwxxDto.setRkrq(rkglDto.getRkrq());
				ids.append(",").append(hwxxDto.getHwid());
			}
			boolean result_t = hwxxService.updateHwxxDtos(hwxxDtos);
			if (!result_t)
				throw new BusinessException("msg","更新货物信息失败！");
			//清除入库车数据
			ids = new StringBuilder(ids.substring(1));
			RkcglDto rkcglDto = new RkcglDto();
			rkcglDto.setIds(ids.toString());
			rkcglService.delete(rkcglDto);
		}
		return true;
	}

	/**
	 * 入库新增
	 * 
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertDto(RkglDto rkglDto) {
		if (StringUtil.isBlank(rkglDto.getRkid())) {
			rkglDto.setRkid(StringUtil.generateUUID());
		}
		rkglDto.setZt(StatusEnum.CHECK_NO.getCode());
		int result = dao.insert(rkglDto);
		return result != 0;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam)
			throws BusinessException {
		RkglDto rkglDto = (RkglDto) baseModel;
		rkglDto.setXgry(operator.getYhid());
		return updateRkgl(rkglDto);
	}

	/**
	 * 入库审核
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		if (shgcList == null || shgcList.isEmpty()) {
			return true;
		}
//		String token = talkUtil.getToken();
		String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
		String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
		String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
		String ICOMM_SH00004 = xxglService.getMsg("ICOMM_SH00004");
		String ICOMM_SH00061 = xxglService.getMsg("ICOMM_SH00061");
		String ICOMM_RK00001 = xxglService.getMsg("ICOMM_RK00001");
		String ICOMM_RK00002 = xxglService.getMsg("ICOMM_RK00002");
		String ICOMM_DH00001 = xxglService.getMsg("ICOMM_DH00001");
		String ICOMM_DH00002 = xxglService.getMsg("ICOMM_DH00002");
		for (ShgcDto shgcDto : shgcList) {
			RkglDto rkglDto = new RkglDto();

			rkglDto.setRkid(shgcDto.getYwid());

			rkglDto.setXgry(operator.getYhid());
			RkglDto rkglDto_t = getDtoById(rkglDto.getRkid());
			rkglDto_t.setZsxm(operator.getZsxm());
			shgcDto.setSqbm(rkglDto_t.getBm());
			List<SpgwcyDto> spgwcyDtos = commonservice.siftJgList(shgcDto.getSpgwcyDtos(), rkglDto_t.getBm());
			// 审核退回
			if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
				// 更新状态
				rkglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				// 发送钉钉消息
				if (!CollectionUtils.isEmpty(spgwcyDtos)) {
					for (SpgwcyDto spgwcyDto : spgwcyDtos) {
						if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
							talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
									spgwcyDto.getYhid(), ICOMM_SH00026,
									xxglService.getMsg("ICOMM_SH000100", operator.getZsxm(), shgcDto.getShlbmc(),
											rkglDto_t.getRkdh(), rkglDto_t.getRklxmc(), rkglDto_t.getCglxmc(), rkglDto_t.getSqbmmc(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
				// 审核通过
			} else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
				rkglDto_t.setZsxm(operator.getZsxm());
				HwxxDto hwxxDto_t=new HwxxDto();
				hwxxDto_t.setRkid(rkglDto_t.getRkid());
				List<HwxxDto> hwxxDtoList_t=hwxxService.getListRkId(hwxxDto_t);
				if ("bhg".equals(rkglDto_t.getRklxdm())){
					List<HwxxDto> hwxxDtoList = new ArrayList<>();
					List<CkhwxxDto> ckhwxxDtos = new ArrayList<>();
					for (HwxxDto hwxxDto : hwxxDtoList_t) {
						HwxxDto dto = new HwxxDto();
						dto.setHwid(hwxxDto.getHwid());
						dto.setKcl(hwxxDto.getSl());
						dto.setZt("99");
						dto.setXgry(rkglDto.getXgry());
						hwxxDtoList.add(dto);

						CkhwxxDto ckhwxxDto = new CkhwxxDto();
						ckhwxxDto.setWlid(hwxxDto.getWlid());
						ckhwxxDto.setCkid(hwxxDto.getCkid());
						ckhwxxDto.setCkqxlx(rkglDto_t.getCkqxlx());
						ckhwxxDto.setKcl(hwxxDto.getSl());
						ckhwxxDto.setKclbj("1");
						boolean success = ckhwxxService.updateDto(ckhwxxDto);
						if (!success) {
							ckhwxxDto.setCkhwid(StringUtil.generateUUID());
							ckhwxxDto.setYds("0");
							ckhwxxDtos.add(ckhwxxDto);
						}
					}
					if(!CollectionUtils.isEmpty(ckhwxxDtos)) {
						boolean isSuccess= ckhwxxService.insertCkhwxxs(ckhwxxDtos);
						if (!isSuccess) {
							throw new  BusinessException("mag","更新仓库货物库存量失败！");
						}
					}
					
					boolean isSuccess= hwxxService.updateHwxxDtos(hwxxDtoList);
					if (!isSuccess) {
						throw new  BusinessException("mag","更新货物库存量失败！");
					}
				}else {

					if (!CollectionUtils.isEmpty(hwxxDtoList_t) && "0".equals(rkglDto_t.getCskz1()) && StringUtil.isNotBlank(hwxxDtoList_t.get(0).getQgmxid())){
						boolean isSuccess= qgmxService.updateRksl(hwxxDtoList_t) != 0;
						if (!isSuccess)
						{
							throw new  BusinessException("mag","更新明细库存量失败！");
						}
					}
					List<HwxxDto> hwxxs_lb = hwxxService.getDtoListById(rkglDto_t.getRkid());
					List<HwxxDto> hwxxs_kcl = new ArrayList<>();

					if(!"1".equals(systemreceiveflg) && !"OA".equals(hwxxs_lb.get(0).getDhlxdm()) && StringUtil.isNotBlank(matridxdsflag)){
						if (!rdRecordService.determine_Entry(rkglDto_t.getRkrq())){
							throw new BusinessException("ICOM99019","该月份已结账，不允许再录入U8数据，请修改单据日期!");
						}
						//处理U8入库数据
						Map<String,Object> map=rdRecordService.dealRecordData(rkglDto_t,hwxxs_lb);
						hwxxs_kcl=(List<HwxxDto>) map.get("hwxxs_kcl");

						//判断是调拨还是入库
						if("1".equals(map.get("isRk"))) {
							if("1".equals(map.get("isCprk"))) {
								List<HwxxDto> hwxxDtoList = (List<HwxxDto>) map.get("hwxxDtoList");
								//更新货物信息中 关联u8货物明细字段 和 关联u8库存字段
								boolean result = hwxxService.updateHwxxDtos(hwxxDtoList);
								if(!result) {
									throw new BusinessException("msg","更新货物信息关联id失败！");
								}
								List<HwxxDto> hwxxDto_hs = (List<HwxxDto>) map.get("hwxxDto_hs");
								//更新库存关联id
								boolean result_hs = hwxxService.updateByWlidAndZsh(hwxxDto_hs);
								if(!result_hs) {
									throw new BusinessException("msg","更新货物信息关联id失败！");
								}
							}else {
								List<HwxxDto> hwxxDtoList = (List<HwxxDto>) map.get("hwxxDtoList");
								//更新货物信息中 关联u8货物明细字段 和 关联u8库存字段
								boolean result_h2 = hwxxService.updateHwxxDtos(hwxxDtoList);
								if(!result_h2) {
									throw new BusinessException("msg","更新货物信息关联id失败！");
								}
								List<HwxxDto> hwxxDto_hs = (List<HwxxDto>) map.get("hwxxDto_hs");
								//更新库存关联id
								boolean result_hs = hwxxService.updateByWlidAndZsh(hwxxDto_hs);
								if(!result_hs) {
									throw new BusinessException("msg","更新货物信息关联id失败！");
								}
							}
							rkglDto = (RkglDto) map.get("rkglDto");
						}else {
							//更新关联id
							List<HwxxDto> hwxxlist_t=(List<HwxxDto>) map.get("hwxxlist_t");
							if(!CollectionUtils.isEmpty(hwxxlist_t)) {
								//更新入库关联表关联U8调拨单号
								rkglDto_t.setDbglid(map.get("dbglid").toString());
								rkglDto_t.setQtckglid(map.get("qtckglid").toString());
								rkglDto_t.setQtrkglid(map.get("qtrkglid").toString());
								boolean updateGlDb=dao.updateGldb(rkglDto_t);
								if(!updateGlDb)
									throw new BusinessException("msg","更新调拨关联ID及其他出入库ID失败！");

								//更新其他出入库明细ID
								List<HwxxDto> qtcrklist=(List<HwxxDto>) map.get("hwxxs");
								boolean updateqtcrk=hwxxService.updateqtcrk(qtcrklist);
								if(!updateqtcrk)
									throw new BusinessException("msg","更新其他出入库明细关联ID失败！");
								//更新货物关联ID
								List<CurrentStockDto> t_addcurrentStockList=(List<CurrentStockDto>) map.get("t_addcurrentStockList");
								if(!CollectionUtils.isEmpty(t_addcurrentStockList)) {
									for(CurrentStockDto currentStockDto : t_addcurrentStockList) {
										for(HwxxDto hwxxDto : hwxxlist_t) {
											if(currentStockDto.getcInvCode().equals(hwxxDto.getWlbm()) && currentStockDto.getcBatch().equals(hwxxDto.getScph())) {
												hwxxDto.setKcglid(String.valueOf(currentStockDto.getAutoID()));
											}
										}
									}
								}
								boolean result_g = hwxxService.updateHwxxDtos(hwxxlist_t);
								if(!result_g) {
									throw new BusinessException("msg","更新货物信息库存关联id失败！");
								}
							}
						}
					}
					if(hwxxs_kcl.isEmpty()){
						for (HwxxDto hwxxDto_lb : hwxxs_lb) {
							HwxxDto hwxx_kcl = new HwxxDto();
							hwxx_kcl.setKcl(hwxxDto_lb.getSl());
							hwxx_kcl.setHwid(hwxxDto_lb.getHwid());
							hwxx_kcl.setZt("99");
							hwxx_kcl.setDhlxdm(hwxxDto_lb.getDhlxdm());
							hwxxs_kcl.add(hwxx_kcl);
						}
					}

					//更新货物信息库存量
					boolean result_h = hwxxService.updateHwxxDtos(hwxxs_kcl);
	        		if(!result_h) {
	        			throw new BusinessException("msg","更新货物信息库存量失败！");
	        		}

				}
				List<QgglDto> qgglDtoList=qgglService.getPurchasesByRkid(rkglDto_t.getRkid());
				List<QgglDto> qgglDtoListAllInRk=new ArrayList<>();
				List<QgglDto> qgglDtoListNotAllInRk=new ArrayList<>();
				if (!CollectionUtils.isEmpty(qgglDtoList)) {
					for (QgglDto qgglDto : qgglDtoList) {
						if (StringUtil.isNotBlank(qgglDto.getRksl()) && StringUtil.isNotBlank(qgglDto.getSl())) {
							if (Double.parseDouble(qgglDto.getRksl()) == Double.parseDouble(qgglDto.getSl())) {
								qgglDto.setRkzt("03");
								qgglDtoListAllInRk.add(qgglDto);
							} else {
								qgglDtoListNotAllInRk.add(qgglDto);
								qgglDto.setRkzt("02");
							}
						}
					}
					if (!CollectionUtils.isEmpty(qgglDtoListAllInRk)) {
						boolean isSuccess = qgglService.updateRkztList(qgglDtoListAllInRk);
						if (!isSuccess) {
							throw new BusinessException("mag", "更新请购管理表入库状态失败！");
						}
					}
					if (!CollectionUtils.isEmpty(qgglDtoListNotAllInRk)) {
						boolean isSuccess = qgglService.updateRkztList(qgglDtoListNotAllInRk);
						if (!isSuccess) {
							throw new BusinessException("mag", "更新请购管理表入库状态失败！");
						}
					}
				}

				rkglDto.setZt(StatusEnum.CHECK_PASS.getCode());
				rkglDto.setXgry(operator.getYhid());
                //更新仓库信息
				if (!"bhg".equals(rkglDto_t.getRklxdm())){
					boolean resulr_c = checkPassMod(rkglDto);
					if(!resulr_c) {
						throw new BusinessException("msg","更新仓库货物信息失败！");
					}
				}
				// 发送钉钉消息
				if (!CollectionUtils.isEmpty(spgwcyDtos)) {
					for (SpgwcyDto spgwcyDto : spgwcyDtos) {
						if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
							talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
									spgwcyDto.getYhid(), ICOMM_SH00006,
									xxglService.getMsg("ICOMM_SH00060", operator.getZsxm(), shgcDto.getShlbmc(),
											rkglDto_t.getRkdh(), rkglDto_t.getRklxmc(), rkglDto_t.getCglxmc(), rkglDto_t.getSqbmmc(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
				//通过入库id 查询请购的通知人
				HwxxDto hwxxDto = new HwxxDto();
				hwxxDto.setRkid(rkglDto.getRkid());
				List<HwxxDto> qgtzlist = hwxxService.getHwxxByRkid(hwxxDto);
				//通知采购人员到货消息
				if(!CollectionUtils.isEmpty(qgtzlist)) {
					for (HwxxDto dto : qgtzlist) {
						if (StringUtils.isNotBlank(dto.getQgrddid())) {
							//发送到货通知
							//小程序访问
							@SuppressWarnings("deprecation")
							String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/viewpage/arrivalgoods/arrivalGoodsView&type=1&dhid=" + dto.getDhid() + "&urlPrefix=" + urlPrefix,StandardCharsets.UTF_8);
							List<BtnJsonList> btnJsonLists = new ArrayList<>();
							BtnJsonList btnJsonList = new BtnJsonList();
							btnJsonList.setTitle("小程序");
							btnJsonList.setActionUrl(internalbtn);
							btnJsonLists.add(btnJsonList);
							if (StringUtil.isNotBlank(dto.getSqryhm())) {
								talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),dto.getSqr(),dto.getSqryhm(), dto.getQgrddid(), ICOMM_DH00001, StringUtil.replaceMsg(ICOMM_DH00002,
										dto.getDhdh(), dto.getDhrq(), dto.getQgdh(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
							}
						}
					}
				}
				
			    //先找到入库通知需要通知的人员和信息list
		        List<RkglDto> rktzList = dao.getRktzList(rkglDto);
		        if(rktzList != null && rktzList.size()>0 ) {
					for (RkglDto dto : rktzList) {
						if (StringUtil.isNotBlank(dto.getSqry())) {
							//发送入库通知
							//小程序访问   pages/index/auditpage/inspectiongoods/inspectionGoodsView.axml
							@SuppressWarnings("deprecation")
							String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/viewpage/putInStorage/putInStorage&type=1&rkid=" + rkglDto.getRkid() + "&urlPrefix=" + urlPrefix,StandardCharsets.UTF_8);
							List<BtnJsonList> btnJsonLists = new ArrayList<>();
							BtnJsonList btnJsonList = new BtnJsonList();
							btnJsonList.setTitle("小程序");
							btnJsonList.setActionUrl(internalbtn);
							btnJsonLists.add(btnJsonList);
							if (StringUtil.isNotBlank(dto.getDdid())) {
								talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),dto.getSqr(),dto.getSqryhm(), dto.getDdid(), ICOMM_RK00001, StringUtil.replaceMsg(ICOMM_RK00002,
										dto.getRkdh(), dto.getRkrq(), dto.getDjh(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
							}
						}
					}
		        }		
				
				// 审核中
			} else {
				String ICOMM_SH00017 = xxglService.getMsg("ICOMM_SH00017");
				rkglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				// 发送钉钉消息
				if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())){
					try{
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){	
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								//获取下一步审核人用户名
								//小程序访问
                                @SuppressWarnings("deprecation")
								String external="page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/auditpage/putInStorage/putInStorageAuditView&type=1&ywzd=rkid&ywid="+rkglDto_t.getRkid()+"&auditType="+AuditTypeEnum.AUDIT_GOODS_STORAGE.getCode()+"&urlPrefix="+urlPrefix,StandardCharsets.UTF_8) ;
								log.error("---小程序访问链接---- url:"+external);
								List<BtnJsonList> btnJsonLists = new ArrayList<>();
								BtnJsonList btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("小程序");
								btnJsonList.setActionUrl(external);
								btnJsonLists.add(btnJsonList);
								talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
										shgcDto.getSpgwcyDtos().get(i).getYhid(),ICOMM_SH00003,StringUtil.replaceMsg(ICOMM_SH00017,
										operator.getZsxm(),shgcDto.getShlbmc() ,rkglDto_t.getRkdh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
							}
						}
					}catch(Exception e){
						log.error(e.getMessage());
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
										StringUtil.replaceMsg(ICOMM_SH00061, operator.getZsxm(), shgcDto.getShlbmc(),
												rkglDto_t.getRkdh(), rkglDto_t.getRklxmc(), rkglDto_t.getCglxmc(), rkglDto_t.getSqbmmc(),
												DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
			}
			boolean resulr_r = update(rkglDto);
			if(!resulr_r) {
				throw new BusinessException("msg","更新入库信息失败！");
			}
			if (StatusEnum.CHECK_PASS.getCode().equals(rkglDto.getZt())){
				HwxxDto hwxxDto_t=new HwxxDto();
				hwxxDto_t.setRkid(rkglDto.getRkid());
				List<HwxxDto> hwxxDtoList_t=hwxxService.getListRkId(hwxxDto_t);
				if (!CollectionUtils.isEmpty(hwxxDtoList_t)){
					Set<String> xqjhids = new HashSet<>();
					for (HwxxDto hwxxDto : hwxxDtoList_t) {
						if (StringUtil.isNotBlank(hwxxDto.getXqjhid())){
							xqjhids.add(hwxxDto.getXqjhid());
						}
					}
					cpxqjhService.dealXqrkzt(xqjhids);
				}
			}
		}
		return true;
	}

	@Override
	public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
		if (shgcList == null || shgcList.isEmpty()) {
			return true;
		}
		if (auditParam.isCancelOpe()) {
			// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String wlid = shgcDto.getYwid();
				RkglDto rkglDto = new RkglDto();
				rkglDto.setXgry(operator.getYhid());
				rkglDto.setRkid(wlid);
				rkglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				update(rkglDto);
			}
		} else {
			// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String wlid = shgcDto.getYwid();
				RkglDto rkglDto = new RkglDto();
				rkglDto.setXgry(operator.getYhid());
				rkglDto.setRkid(wlid);
				rkglDto.setZt(StatusEnum.CHECK_NO.getCode());
				update(rkglDto);
			}
		}
		return true;
	}
	
	/**
	 * 审核通过仓库货物信息添加
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean checkPassMod(RkglDto rkglDto) throws BusinessException{
		//获取货物信息
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setRkid(rkglDto.getRkid());
		List<HwxxDto> hwxxDtos = hwxxService.getDtoList(hwxxDto);
		//根据货物id查询仓库货物信息
		List<CkhwxxDto> CkhwxxDtos = new ArrayList<>();
		for (HwxxDto hwxxDto_t : hwxxDtos) {
			CkhwxxDto ckhwxxDto = new CkhwxxDto();
			ckhwxxDto.setWlid( hwxxDto_t.getWlid());
			ckhwxxDto.setCkqxlx(hwxxDto_t.getCkqxlx());
			ckhwxxDto.setCkid(hwxxDto_t.getCkid());
			ckhwxxDto = ckhwxxService.getDtoByWlidAndCkid(ckhwxxDto);
			if (ckhwxxDto != null)
				CkhwxxDtos.add(ckhwxxDto);
		}
		//存新增的货物的物料id
		StringBuilder ids_add = new StringBuilder();
		//区分存在哪些物料，存在的增加，不存在的新增。
		for (HwxxDto hwxxDto_h : hwxxDtos) {
			boolean result_t = true;
			if(!CollectionUtils.isEmpty(CkhwxxDtos)) {
				for (CkhwxxDto ckhwxxDto_h : CkhwxxDtos) {
					if(hwxxDto_h.getWlid().equals(ckhwxxDto_h.getWlid())) {
						BigDecimal kcl = new BigDecimal(StringUtils.isNotBlank(ckhwxxDto_h.getKcl()) ? ckhwxxDto_h.getKcl() : "0")
								.add(new BigDecimal(StringUtils.isNotBlank(hwxxDto_h.getSl()) ? hwxxDto_h.getSl() : "0"));
						ckhwxxDto_h.setKcl(kcl.toString());
						result_t = false;
						break;
					}
				}
			}
			//如果没有相同的物料，做新增
			if(result_t) {
				ids_add.append(",").append(hwxxDto_h.getHwid());
			}
		}
		//处理存在的物料信息
		if(!CollectionUtils.isEmpty(CkhwxxDtos)) {
			boolean result = ckhwxxService.updateCkhwxxs(CkhwxxDtos);
			if(!result)
				throw new BusinessException("msg","更新库存失败！");
		}	
		
		//处理新增的库存信息
		if(StringUtil.isNotBlank(ids_add.toString())) {
			ids_add = new StringBuilder(ids_add.substring(1));
			hwxxDto.setIds(ids_add.toString());
			//物料id分组查询
			List<HwxxDto> hwxxDtos_hw = hwxxDao.getDtoListGroupBywlid(hwxxDto);
			//存要新增的仓库信息
			List<CkhwxxDto> ckhwxxDtos_add = new ArrayList<>();
			for (HwxxDto hwxxDto_t : hwxxDtos_hw) {
				CkhwxxDto ckhwxxDto_s = new CkhwxxDto();
				ckhwxxDto_s.setCkhwid(StringUtil.generateUUID());
				ckhwxxDto_s.setWlid(hwxxDto_t.getWlid());
				ckhwxxDto_s.setKcl(hwxxDto_t.getZkcl());
				ckhwxxDto_s.setYds(null);
				ckhwxxDto_s.setCkqxlx(hwxxDto_t.getCkqxlx());
				ckhwxxDto_s.setCkid(hwxxDto_t.getCkid());
				ckhwxxDtos_add.add(ckhwxxDto_s);
			}
			boolean result_add = ckhwxxService.insertCkhwxxs(ckhwxxDtos_add);
			if(!result_add)
				throw new BusinessException("msg","新增库存失败！");
		}
		
		return true;
	}
	
	/**
	 * 入库修改
	 * 
	 * @return
	 * 	一、审核通过
	 * 	1.更新入库管理表
	 * 	2.更新货物信息表，获取删除的货物信息，
	 * 		仓库货物信息库存量-删除的货物信息数量
	 * 		请购入库数量-删除的货物信息数量
	 * 		合同入库数量-删除的货物信息数量
	 * 	3.更细货物信息表
	 *  4.更新请购明细库存数量
	 * 	5.更新合同明细库存数量
	 * 	6.更新U8入库表
	 * 	7.更新U8明细表
	 * 		货物信息存在关联id的做更新
	 * 		删除的货物信息不做处理
	 */		
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean modSavePutInStorage(RkglDto rkglDto) throws BusinessException{
		//1.更新入库管理表
		boolean result = update(rkglDto);
		if (!result)
			return false;
		//2.更新仓库货物信息
		//获取修改前的货物信息
		List<HwxxDto> hwxxDtos_t = hwxxDao.getDtoListById(rkglDto.getRkid());
		//获取修改的货物信息
		List<HwxxDto> hwxxDtos = JSON.parseArray(rkglDto.getHwxx_json(), HwxxDto.class);
		//获取移除的货物信息
		StringBuilder ids = new StringBuilder();
		// StringBuilder ids_d = new StringBuilder();
		for (int i = hwxxDtos_t.size() - 1; i >= 0; i--) {
			boolean isDel = true;
			for (HwxxDto hwxxDto : hwxxDtos) {
				// ids_d.append(",").append(hwxxDto.getHwid());
				if (hwxxDtos_t.get(i).getHwid().equals(hwxxDto.getHwid())) {
					isDel = false;
					break;
				}
			}
			if (isDel) {
				ids.append(",").append(hwxxDtos_t.get(i).getHwid());
			}else {
				hwxxDtos_t.remove(i);
			}
		}
		
		//获取更新后的货物信息
		HwxxDto hwxxDto = new HwxxDto();
		// ids_d = new StringBuilder(ids_d.substring(1));
		//创建仓库货物信息集合
		List<CkhwxxDto> ckhwxxDtos = new ArrayList<>();
		//判断有没有删除货物明细
		if(StringUtil.isNotBlank(ids.toString())) {
			//存请购明细
			List<QgmxDto> qgmxDtos = new ArrayList<>();
			//存合同明细
			List<HtmxDto> htmxDtos = new ArrayList<>();
			//遍历判断移除的货物信息里是都有货物的库存量与数量不相同
			for (HwxxDto hwxxDto_qh : hwxxDtos_t) {
				if(!(new BigDecimal(hwxxDto_qh.getSl()).compareTo(new BigDecimal(hwxxDto_qh.getKcl())) == 0)) {
					throw new BusinessException("msg","删除的货物信息与入库数量不一致！");
				}
				if(StringUtil.isNotBlank(hwxxDto_qh.getQgmxid())) {
					QgmxDto qgmxDto = new QgmxDto();
					qgmxDto.setXgry(rkglDto.getXgry());
					qgmxDto.setQgmxid(hwxxDto_qh.getQgmxid());
					qgmxDto.setRksl(hwxxDto_qh.getSl());
					qgmxDto.setRksldel("1");
					qgmxDtos.add(qgmxDto);
				}
				if(StringUtil.isNotBlank(hwxxDto_qh.getHtmxid())) {
					HtmxDto htmxDto = new HtmxDto();
					htmxDto.setXgry(rkglDto.getXgry());
					htmxDto.setHtmxid(hwxxDto_qh.getHtmxid());
					htmxDto.setRksl(hwxxDto_qh.getSl());
					htmxDto.setRksldel("1");
					htmxDtos.add(htmxDto);
				}			
			}

			ids = new StringBuilder(ids.substring(1));
			hwxxDto.setIds(ids.toString());
			// 按物料分组获取每个物料库存量总数(修改仓库货物信息用)
			List<HwxxDto> hwxxDtos_hw = hwxxDao.getDtoListGroupBywlid(hwxxDto);
			for (HwxxDto hwxxDto_hw : hwxxDtos_hw) {
				CkhwxxDto ckhwxxDto_mod = new CkhwxxDto();
				ckhwxxDto_mod.setCkid(hwxxDto_hw.getCkid());
				ckhwxxDto_mod.setWlid(hwxxDto_hw.getWlid());
				ckhwxxDto_mod.setKcl(hwxxDto_hw.getZkcl());
				ckhwxxDto_mod.setKclbj("0");
				ckhwxxDtos.add(ckhwxxDto_mod);
			}
			//3.更细货物信息表
			boolean result_ckhwxx = ckhwxxService.updateByWlidAndCkid(ckhwxxDtos);
			if(!result_ckhwxx)
				throw new BusinessException("msg","仓库货物信息更新失败！");
		
			//4.更新请购明细
			if(qgmxDtos.size()>0) {
				boolean result_qgmx = qgmxService.qgmxDtoModDhxx(qgmxDtos);
				if(!result_qgmx) {
					throw new BusinessException("msg","更新请购明细的入库数量失败！");
				}
			}
			
			//5.更新合同明细
			if(!CollectionUtils.isEmpty(htmxDtos)) {
				boolean result_htmx = htmxService.htmxDtoModDhxx(htmxDtos);
				if(!result_htmx) {
					throw new BusinessException("msg","更新合同明细的入库数量失败！");
				}
			}	
		}	

		if(!CollectionUtils.isEmpty(hwxxDtos_t)) {
			//把更新之前的货物信息里的入库信息设置为空
			for (HwxxDto hwxxDto_sta : hwxxDtos_t) {
				hwxxDto_sta.setRkid(null);
				hwxxDto_sta.setXgry(rkglDto.getXgry());
				hwxxDto_sta.setCkid(null);
				hwxxDto_sta.setRkrq(null);
				hwxxDto_sta.setRkbz(null);
				hwxxDto_sta.setGlid(null);
				hwxxDto_sta.setKwbh(null);
				hwxxDto_sta.setKcl(null);
				hwxxDto_sta.setKcglid(null);
				hwxxDto_sta.setZt("03");
			}
			boolean result_sta = hwxxService.updateRkglDtos(hwxxDtos_t);
			if(!result_sta)
				throw new BusinessException("msg","更新删除的货物信息失败！");
		}
		
		//更新修改的货物信息
		return hwxxService.updateHwxxDtos(hwxxDtos);
	}
	
	/**
	 * 入库列表（查询审核状态）
	 * 
	 * @return
	 */
	@Override
	public List<RkglDto> getPagedDtoList(RkglDto rkglDto) {
		List<RkglDto> list = dao.getPagedDtoList(rkglDto);
		try {
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_GOODS_STORAGE.getCode(), "zt", "rkid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return list;
	}

	@Override
	public List<RkglDto> getPagedAuditRkgl(RkglDto rkglDto) {
		// 获取人员ID和履历号
		List<RkglDto> t_sbyzList= dao.getPagedAuditRkgl(rkglDto);
		if(CollectionUtils.isEmpty(t_sbyzList))
			return t_sbyzList;

		List<RkglDto> sqList = dao.getAuditListRkgl(t_sbyzList);

		commonservice.setSqrxm(sqList);

		return sqList;
	}

	/**
	 * 入库删除废弃
	 * @param
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteRkgl(RkglDto rkglDto) throws BusinessException {
		HwxxDto hwxxDto = new HwxxDto();

		boolean result_c;

		// 创建仓库货物信息集合
		List<CkhwxxDto> ckhwxxDtos = new ArrayList<>();
		// 创建货物信息集合
		List<HwxxDto> hwxxDtoList = new ArrayList<>();
		// 创建不合格货物信息集合
		List<HwxxDto> hwxxDtos_bhg = new ArrayList<>();
		// 删除的入库信息状态
		List<RkglDto> rkglDtos = dao.queryByRkdh(rkglDto);
		//存要删除的货物信息
		// StringBuilder ids = new StringBuilder();
		//需求计划ids
		Set<String> xqjhids = new HashSet<>();
		HwxxDto dto = new HwxxDto();
		dto.setIds(rkglDto.getIds());
		//获取入库单得llmxid 判断是否出库
		List<HwxxDto> dtos = hwxxDao.getDtoLlListByrkids(dto);
		if(!CollectionUtils.isEmpty(dtos)) {
			for (HwxxDto dto1 : dtos) {
				if (StringUtil.isNotBlank(dto1.getLlmxid())){
					throw new BusinessException("msg", "入库单"+dto1.getRkdh()+"中的"+dto1.getWlbm()+"已出库，禁止删除！");
				}
				if (StatusEnum.CHECK_PASS.getCode().equals(dto1.getZt())&&StringUtil.isNotBlank(dto1.getXqjhid())){
					xqjhids.add(dto1.getXqjhid());
				}
			}
		}else{
			throw new BusinessException("msg", "未获取到相关得货物入库信息!");
		}
		List<String> qgmxids=new ArrayList<>();
		for (RkglDto rkglDto_r : rkglDtos) {
			hwxxDto.setRkid(rkglDto_r.getRkid());
			// 按物料分组获取每个物料库存量总数(修改仓库货物信息用)
			List<HwxxDto> hwxxDtos = hwxxDao.getDtoListGroupBywlid(hwxxDto);
			// 获取所有货物信息(修改货物信息用)
			List<HwxxDto> hwxxDtos_all = hwxxDao.getDtoListByrkids(hwxxDto);
			List<QgmxDto> qgmxDtos=new ArrayList<>();
			if (StringUtil.isNotBlank(rkglDto_r.getRklxdm()) && "bhg".equals(rkglDto_r.getRklxdm())){
				for (HwxxDto hwxxDto_all : hwxxDtos_all) {
					HwxxDto hwxxDto_mod = new HwxxDto();
					hwxxDto_mod.setHwid(hwxxDto_all.getBhghw());
					hwxxDto_mod.setXgry(rkglDto.getScry());
					hwxxDto_mod.setByycls(hwxxDto_all.getSl());
					hwxxDto_mod.setYclsbj("0");
					hwxxDtos_bhg.add(hwxxDto_mod);
					// ids.append(",").append(hwxxDto_all.getHwid());
				}
			}else{
				//判断入库类别
				if("0".equals(rkglDto_r.getCskz1())) {
					// 判断库存量是否和数量一致
					for (HwxxDto hwxxDto_all : hwxxDtos_all) {
						if ("99".equals(hwxxDto_all.getZt()) && !(new BigDecimal(hwxxDto_all.getSl()).compareTo(new BigDecimal(hwxxDto_all.getKcl())) == 0)) {
							throw new BusinessException("msg", "库存量与数量不符的货物，不允许删除!");
						}else {
							// 更新入库信息数据
							hwxxDto_all.setRkid(null);
							hwxxDto_all.setRkrq(null);
							hwxxDto_all.setCkid(null);
							hwxxDto_all.setRkry(null);
							hwxxDto_all.setRkbz(null);
							hwxxDto_all.setKwbh(null);
							hwxxDto_all.setKcl(null);
							hwxxDto_all.setXgry(rkglDto.getScry());
							hwxxDto_all.setZt("03");
							hwxxDtoList.add(hwxxDto_all);
							qgmxids.add(hwxxDto_all.getQgmxid());
							QgmxDto qgmxDto=new QgmxDto();
							qgmxDto.setRksldel("1");
							qgmxDto.setQgmxid(hwxxDto_all.getQgmxid());
							qgmxDto.setRksl(hwxxDto_all.getRksl());
							qgmxDtos.add(qgmxDto);
						}
					}
				}else {
					for (HwxxDto hwxxDto_all : hwxxDtos_all) {
						if (StatusEnum.CHECK_PASS.getCode().equals(rkglDto_r.getZt())) {
							if (!(new BigDecimal(hwxxDto_all.getSl()).compareTo(new BigDecimal(hwxxDto_all.getKcl())) == 0)) {
								throw new BusinessException("msg", "库存量与数量不符的货物，不允许删除!");
							}else {
								// 更新入库信息数据
								hwxxDto_all.setRkid(null);
								hwxxDto_all.setRkrq(null);
								hwxxDto_all.setCkid(null);
								hwxxDto_all.setRkry(null);
								hwxxDto_all.setRkbz(null);
								hwxxDto_all.setKwbh(null);
								hwxxDto_all.setKcl(null);
								hwxxDto_all.setXgry(rkglDto.getScry());
								hwxxDto_all.setZt("03");
								hwxxDtoList.add(hwxxDto_all);
							}
						}else{
							// 更新入库信息数据
							hwxxDto_all.setRkid(null);
							hwxxDto_all.setRkrq(null);
							hwxxDto_all.setCkid(null);
							hwxxDto_all.setRkry(null);
							hwxxDto_all.setRkbz(null);
							hwxxDto_all.setKwbh(null);
							hwxxDto_all.setKcl(null);
							hwxxDto_all.setXgry(rkglDto.getScry());
							hwxxDto_all.setZt("03");
							hwxxDtoList.add(hwxxDto_all);
						}
					}
				}
			}

			// 一、 审核通过，操作仓库货物信息
			if (StatusEnum.CHECK_PASS.getCode().equals(rkglDto_r.getZt())) {
				for (HwxxDto hwxxDto_hw : hwxxDtos) {
					CkhwxxDto ckhwxxDto_mod = new CkhwxxDto();
					ckhwxxDto_mod.setCkid(hwxxDto_hw.getCkid());
					ckhwxxDto_mod.setWlid(hwxxDto_hw.getWlid());
					ckhwxxDto_mod.setKcl(hwxxDto_hw.getZkcl());
					ckhwxxDto_mod.setKclbj("0");
					ckhwxxDtos.add(ckhwxxDto_mod);
				}
				if (!CollectionUtils.isEmpty(qgmxDtos)){
					boolean isSuccess = qgmxService.updateRkslByIds(qgmxDtos);
					if (!isSuccess){
						throw new BusinessException("msg","同步数量失败!");
					}
				}
			}

		}
		if(!CollectionUtils.isEmpty(ckhwxxDtos)) {
			result_c = ckhwxxService.updateByWlidAndCkid(ckhwxxDtos);
			if (!result_c)
				throw new BusinessException("msg", "仓库货物信息修改失败!");
		}
		if(!CollectionUtils.isEmpty(hwxxDtos_bhg)) {
			boolean result = hwxxService.updateHwxxDtos(hwxxDtos_bhg);
			if (!result)
				throw new BusinessException("msg", "不合格货物信息修改失败!");
		}
		if(!CollectionUtils.isEmpty(hwxxDtoList)) {
			boolean result = hwxxService.updateRkglDtos(hwxxDtoList);
			if (!result)
				throw new BusinessException("msg", "货物信息修改失败!");
		}
		if(!CollectionUtils.isEmpty(qgmxids)) {
			List<QgglDto> qgglDtoList=qgglService.getRkslByQgmxids(qgmxids);
			if (!CollectionUtils.isEmpty(qgglDtoList)) {
				List<QgglDto> qgglDtoListAllInRk = new ArrayList<>();
				List<QgglDto> qgglDtoListNotAllInRk = new ArrayList<>();
				for (QgglDto qgglDto : qgglDtoList) {
					if (Double.parseDouble(qgglDto.getSl()) == 0) {
						qgglDto.setRkzt("01");
						qgglDtoListAllInRk.add(qgglDto);
					} else {
						qgglDtoListNotAllInRk.add(qgglDto);
						qgglDto.setRkzt("02");
					}
				}
				if (!CollectionUtils.isEmpty(qgglDtoListAllInRk)) {
					boolean isSuccess = qgglService.updateRkztList(qgglDtoListAllInRk);
					if (!isSuccess) {
						throw new BusinessException("mag", "更新请购管理表入库状态失败！");
					}
				}
				if (!CollectionUtils.isEmpty(qgglDtoListNotAllInRk)) {
					boolean isSuccess = qgglService.updateRkztList(qgglDtoListNotAllInRk);
					if (!isSuccess) {
						throw new BusinessException("mag", "更新请购管理表入库状态失败！");
					}
				}
			}
		}

		// if(StringUtil.isNotBlank(ids)) {
		// 	ids=ids.substring(1);
		// 	HwxxDto hw_sc = new HwxxDto();
		// 	hw_sc.setScry(rkglDto.getScry());
		// 	hw_sc.setIds(ids);
		// 	boolean result_sc = hwxxService.deleteHwxxDtos(hw_sc);
		// 	if(!result_sc) {
		// 		throw new BusinessException("msg", "删除货物信息失败!");
		// 	}
		// }
		int result_rk = dao.delete(rkglDto);
		if (result_rk < 1)
			return false;
		shgcService.deleteByYwids(rkglDto.getIds());
		cpxqjhService.dealXqrkzt(xqjhids);
		return true;
	}


	   /**
     * 导出
     * 
     * @param rkglDto
     * @return
     */
    @Override
    public int getCountForSearchExp(RkglDto rkglDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(rkglDto);
    }
    
    /**
     * 根据搜索条件获取导出信息
     * 
     * @return
     */
    public List<RkglDto> getListForSearchExp(Map<String, Object> params) {
    	RkglDto rkglDto = (RkglDto) params.get("entryData");
        queryJoinFlagExport(params, rkglDto);
        return dao.getListForSearchExp(rkglDto);
    }

    /**
     * 根据选择信息获取导出信息
     * 
     * @return
     */
    public List<RkglDto> getListForSelectExp(Map<String, Object> params) {
    	RkglDto rkglDto = (RkglDto) params.get("entryData");
        queryJoinFlagExport(params, rkglDto);
        return dao.getListForSelectExp(rkglDto);
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, RkglDto rkglDto) {
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
        rkglDto.setSqlParam(sqlcs);
    }

    
    /**
     *	入库修改
     * 
     * @return
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateRkgl(RkglDto rkglDto) throws BusinessException {
		if(StringUtil.isBlank(rkglDto.getDdbj())) {
			//更新入库信息
			boolean result = update(rkglDto);
			if (!result)
				return false;
			//JcsjDto jcsjDto = jcsjService.getDtoById(rkglDto.getRklb());
			//修改之前入库的货物信息
			List<HwxxDto> hwxxDtos_t = hwxxDao.getDtoListById(rkglDto.getRkid());
			//修改后的入库数据
			List<HwxxDto> hwxxDtos = JSON.parseArray(rkglDto.getHwxx_json(), HwxxDto.class);
				if(!CollectionUtils.isEmpty(hwxxDtos_t)) {

					if ("2".equals(rkglDto.getBs())){
						for (int i = hwxxDtos_t.size()-1; i >=0 ; i--) {
							for (HwxxDto hwxxDto : hwxxDtos) {
								if (hwxxDtos_t.get(i).getHwid().equals(hwxxDto.getHwid())){
									hwxxDtos_t.remove(i);
								}
							}
						}
						List<HwxxDto> hwxxDtoList = new ArrayList<>();
						StringBuilder ids = new StringBuilder();
						if(!CollectionUtils.isEmpty(hwxxDtos_t)) {
							for (HwxxDto hwxxDto_t : hwxxDtos_t) {
								hwxxDto_t.setScry(rkglDto.getXgry());
								ids.append(",").append(hwxxDto_t.getHwid());

								HwxxDto hwxxDto = new HwxxDto();
								hwxxDto.setXgry(rkglDto.getXgry());
								hwxxDto.setYdsbj("0");
								hwxxDto.setByycls(hwxxDto_t.getSl());
								hwxxDto.setHwid(hwxxDto_t.getBhghw());
								hwxxDtoList.add(hwxxDto);
							}
							ids = new StringBuilder(ids.substring(1));
							HwxxDto dto = new HwxxDto();
							dto.setScry(rkglDto.getXgry());
							dto.setIds(ids.toString());
							boolean result_h = hwxxService.deleteHwxxDtos(dto);
							if (!result_h)
								throw new BusinessException("msg", "更新货物入库信息失败!");
							result_h = hwxxService.updateHwxxDtos(hwxxDtoList);
							if (!result_h)
								throw new BusinessException("msg", "更新货物信息失败！");
						}
					}else{
						for (HwxxDto hwxxDto_t : hwxxDtos_t) {
							hwxxDto_t.setXgry(rkglDto.getXgry());
							hwxxDto_t.setRkid(null);
							hwxxDto_t.setRkrq(null);
							hwxxDto_t.setCkid(null);
							hwxxDto_t.setRkry(null);
							hwxxDto_t.setRkbz(null);
							hwxxDto_t.setKwbh(null);
							hwxxDto_t.setKcl(null);
//						hwxxDto_t.setYclsl("0");
							hwxxDto_t.setZt("03");
						}
						//更新货物修改前的入库信息
						boolean result_h = hwxxService.updateRkglDtos(hwxxDtos_t);
						if (!result_h)
							throw new BusinessException("msg", "更新货物入库信息失败!");
					}
				}
				

				if(!CollectionUtils.isEmpty(hwxxDtos)){
					for (HwxxDto hwxxDto : hwxxDtos) {
						hwxxDto.setXgry(rkglDto.getXgry());
						hwxxDto.setRkid(rkglDto.getRkid());
						hwxxDto.setCkid(rkglDto.getCkid());
						hwxxDto.setRkry(rkglDto.getXgry());
						hwxxDto.setRkrq(rkglDto.getRkrq());			
					}
					boolean result_t = hwxxService.updateHwxxDtos(hwxxDtos);
					if (!result_t)
						throw new BusinessException("msg", "更新货物入库信息失败!");
				}
//			}
//			else {
//				//修改之前入库的货物信息
//				List<HwxxDto> hwxxDtos_t = hwxxDao.getDtoListById(rkglDto.getRkid());
//				List<HwxxDto> hwxxDtos = (List<HwxxDto>) JSON.parseArray(rkglDto.getHwxx_json(), HwxxDto.class);
//				for (int i = hwxxDtos.size()-1; i >= 0; i--) {
//					for (int j = 0; j < hwxxDtos_t.size(); j++) {
//						if(hwxxDtos.get(i).getHwid().equals(hwxxDtos_t.get(j).getHwid())) {
//							//移除货物id相等的，剩下的是新增的
//							hwxxDtos.remove(i);
//							//移除货物id相等的，剩下的是删除的
//							hwxxDtos_t.remove(j);
//							break;
//						}
//					}
//				}
//				String ids = "";
//				if(!CollectionUtils.isEmpty(hwxxDtos_t)) {
//					for (HwxxDto hwxxDto_t : hwxxDtos_t) {
//						ids = ids +"," + hwxxDto_t.getHwid();
//					}
//					ids = ids.substring(1);
//					HwxxDto hwxx_s = new HwxxDto();
//					hwxx_s.setIds(ids);
//					boolean result_t = hwxxService.deleteHwxxDtos(hwxx_s);
//					if(!result_t) {
//						throw new BusinessException("msg", "删除入库前货物信息失败!");
//					}
//				}
//				if(hwxxDtos.size()>0) {
//					for (HwxxDto hwxxDto : hwxxDtos) {
//						hwxxDto.setHwid(hwxxDto.getHwid());
//						hwxxDto.setRkid(rkglDto.getRkid());
//						hwxxDto.setCkid(rkglDto.getCkid());
//						hwxxDto.setRkry(rkglDto.getLrry());
//						hwxxDto.setRkrq(rkglDto.getRkrq());
//					}
//					boolean result_hw = hwxxService.insertHwxxList(hwxxDtos);
//					if(!result_hw) {
//						throw new BusinessException("msg","新增货物信息失败！");
//					}
//				}
//			}
		}		
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean advancedUpdateRkgl(RkglDto rkglDto) throws BusinessException {
		RkglDto rkglDto_h = dao.getDtoById(rkglDto.getRkid());
		if(!"80".equals(rkglDto_h.getZt())) {
			return updateRkgl(rkglDto);
		}else{
			rdRecordService.updateRkglUpdate(rkglDto,rkglDto_h);
		}
		//更新入库信息
		boolean result = update(rkglDto);
		if (!result)
			return false;
		//修改之前入库的货物信息
		List<HwxxDto> hwxxDtos_t = hwxxService.getDtoListById(rkglDto.getRkid());
		List<HwxxDto> hwxxDtos = JSON.parseArray(rkglDto.getHwxx_json(), HwxxDto.class);
		if(!CollectionUtils.isEmpty(hwxxDtos_t) && !CollectionUtils.isEmpty(hwxxDtos)) {
			for (HwxxDto hwxx: hwxxDtos_t) {
				for (HwxxDto hwxxs: hwxxDtos) {
					if (StringUtil.isNotBlank(hwxx.getLbbj())){
						if (hwxx.getHwid().equals(hwxxs.getHwid())){
							if (hwxx.getLbbj().equals("1")) {
								hwxx.setXgry(rkglDto.getXgry());
								hwxx.setRkid(null);
								hwxx.setRkrq(null);
								hwxx.setCkid(null);
								hwxx.setRkry(null);
								hwxx.setRkbz(null);
								hwxx.setKwbh(null);
								hwxx.setKcl(null);
								hwxxs.setXgry(rkglDto.getXgry());
								hwxxs.setRkid(rkglDto.getRkid());
								hwxxs.setCkid(rkglDto.getCkid());
								hwxxs.setRkry(rkglDto.getXgry());
								hwxxs.setRkrq(rkglDto.getRkrq());
							}
						}
					}
				}
			}
			//更新货物修改前的入库信息
			boolean result_h = hwxxService.updateRkglDtos(hwxxDtos_t);
			if (!result_h)
				throw new BusinessException("msg", "更新货物入库信息失败!");
			boolean result_t = hwxxService.updateHwxxDtos(hwxxDtos);
			if (!result_t)
				throw new BusinessException("msg", "更新货物入库信息失败!");
		}
		return true;
	}

	/**
	 * 根据入库ids获取入库信息（共通页面）
	 * @param rkglDto
	 * @return
	 */
	public List<RkglDto> getCommonDtoListByRkids(RkglDto rkglDto){
		return dao.getCommonDtoListByRkids(rkglDto);
	}
	
	/**
	 * 自动生成u8调拨出入库单号
	 * @return
	 */
	@Override
	public String generatecCodeInRd8() {
		// TODO Auto-generated method stub
		// 生成规则: R1-2020-1022-01 机构代码-年份-日期-流水号 机构代码从机构信息的扩展参数1 里获取。
		String year = DateUtils.getCustomFomratCurrentDate("yy");
		String date = DateUtils.getCustomFomratCurrentDate("MM");
		String prefix = year + date;
		// 查询流水号
		String serial = rdRecordDao.getcCodeSerialInRd8(prefix);
		if(StringUtils.isBlank(serial))
			return prefix+"001";
		int index=Integer.parseInt(serial.substring(4));
		String newNum = "00"+ (index + 1);
		return prefix + newNum.substring(newNum.length() - 3);
	}
	
	/**
	 * 自动生成u8调拨出入库单号
	 * @return
	 */
	@Override
	public String generatecCodeInRd9() {
		// TODO Auto-generated method stub
		// 生成规则: R1-2020-1022-01 机构代码-年份-日期-流水号 机构代码从机构信息的扩展参数1 里获取。
		String year = DateUtils.getCustomFomratCurrentDate("yy");
		String date = DateUtils.getCustomFomratCurrentDate("MM");
		String prefix = year + date;
		// 查询流水号
		String serial = rdRecordDao.getcCodeSerialInRd9(prefix);
		if(StringUtils.isBlank(serial))
			return prefix+"001";
		int index=Integer.parseInt(serial.substring(4));
		String newNum = "00"+ (index + 1);
		return prefix + newNum.substring(newNum.length() - 3);
	}

	@Override
	public List<RkglDto> getPagedDtoListDingTalk(RkglDto rkglDto) {
		return dao.getPagedDtoListDingTalk(rkglDto);
	}

	/**
	 * 更新入库关联ID
	 * @param rkglDto
	 * @return
	 */
	public boolean updateGldb(RkglDto rkglDto) {
		return dao.updateGldb(rkglDto);
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
		Map<String, Object> map = new HashMap<>();
		RkglDto rkglDto = dao.getDtoById(shgcDto.getYwid());
		map.put("jgid",rkglDto.getBm());
		return map;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
		Map<String, Object> map =new HashMap<>();
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>)param.get("ywids");
		RkglDto rkglDto = new RkglDto();
		rkglDto.setIds(ids);
		List<RkglDto> dtoList = dao.getDtoList(rkglDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(RkglDto dto:dtoList){
				list.add(dto.getRkid());
			}
		}
		map.put("list",list);
		return map;
	}
}
