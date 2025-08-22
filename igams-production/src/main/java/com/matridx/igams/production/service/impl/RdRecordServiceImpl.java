package com.matridx.igams.production.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.DepartmentDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.production.dao.gmsql.IGmInventoryDao;
import com.matridx.igams.production.dao.post.IHtmxDao;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.*;
import com.matridx.igams.production.dao.matridxsql.*;
import com.matridx.igams.production.service.svcinterface.*;
import com.matridx.igams.storehouse.dao.entities.*;
import com.matridx.igams.storehouse.dao.post.*;
import com.matridx.igams.storehouse.service.svcinterface.*;
import com.matridx.igams.warehouse.dao.entities.GdzcglDto;
import com.matridx.igams.warehouse.dao.entities.GysxxDto;
import com.matridx.igams.warehouse.service.svcinterface.IGysxxService;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.util.CollectionUtils;

@Service
public class RdRecordServiceImpl implements IRdRecordService {
	@Autowired
	IXsmxService xsmxService;

	@Autowired
	SO_SOMainDao so_soMainDao;

	@Autowired
	IGmInventoryDao iGmInventoryDao;
	
	@Autowired
	IMatridxInventoryDao matridxInventoryDao;

	@Autowired
	Bom_bomDao bom_bomDao;

	@Autowired
	Bom_opcomponentoptDao bom_opcomponentoptDao;

	@Autowired
	Bom_opcomponentDao bom_opcomponentDao;

	@Autowired
	Bom_parentDao bom_parentDao;

	@Autowired
	Bom_quepartDao bom_quepartDao;

	@Autowired
	RdRecordDao rdRecordDao;

	@Autowired
	IJcjymxService jcjymxService;

	@Autowired
	IHwxxDao hwxxDao;

	@Autowired
	DingTalkUtil talkUtil;

	@Autowired
	IXxglService xxglService;

	@Autowired
	IDdxxglService ddxxglService;

	@Autowired
	IKctbcwxxService kctbcwxxService;

	@Autowired
	IQtckglService qtckglService;

	@Autowired
	IWlglService wlglService;

	@Autowired
	IQgmxService qgmxService;

	@Autowired
	IJcsjService jcsjService;

	@Autowired
	IGysxxService gysxxService;

	@Autowired
	ICkhwxxService ckhwxxService;

	@Autowired
	HY_DZ_BorrowOutBackDao hy_dz_borrowOutBackDao;

	@Autowired
	HY_DZ_BorrowOutBacksDao hy_dz_borrowOutBacksDao;

	@Autowired
	HY_DZ_BorrowOutDao hy_dz_borrowOutDao;

	@Autowired
	HY_DZ_BorrowOutsDao hy_dz_borrowOutsDao;

	@Autowired
	IHtmxService htmxService;

	@Autowired
	ICkxxService ckxxService;

	@Autowired
	IDhxxDao dhxxDao;

	@Autowired
	ICommonService commonService;

	@Value("${matridx.accountSet.accountName:}")
	private String accountName;

	@Autowired
	UA_IdentityDao uA_IdentityDao;

	@Autowired
	RdRecordsDao rdRecordsDao;

	@Autowired
	PO_PodetailsDao pO_PodetailsDao;

	@Autowired
	CurrentStockDao currentStockDao;

	@Autowired
	IPU_AppVouchDao PU_AppVouchDao;

	@Autowired
	SCM_ItemDao sCM_ItemDao;

	@Autowired
	IHwxxService hwxxService;

	@Autowired
	IHwllxxDao hwllxxDao;
	
	@Autowired
	TransVouchDao transVouchDao;
	
	@Autowired
	TransVouchsDao transVouchsDao;
	
	@Autowired
	IDhjyDao dhjyDao;
	
	@Autowired
	IRkglDao rkglDao;
	
	@Autowired
	MaterialAppVouchDao materialAppVouchDao;
	
	@Autowired
	IHwllmxService hwllmxService;
	
	@Autowired
	MaterialAppVouchsDao materialAppVouchsDao;

	@Autowired
	RdRecord11Dao rdRecord11Dao;
	
	@Autowired
	RdRecords11Dao rdRecords11Dao;
	
	@Autowired
	InvPositionDao invPositionDao;
	
	@Autowired
	InvPositionSumDao invPositionSumDao;
	
	@Autowired
	IA_ST_UnAccountVouchDao iA_ST_UnAccountVouchDao;
	
	@Autowired
	AA_BatchPropertyDao aA_BatchPropertyDao;
	
	@Autowired
	ICkglDao ckglDao;
	
	@Autowired
	IDbglDao dbglDao;
	
	@Autowired
	IDbmxService dbmxService;
	
	@Autowired
	VoucherHistoryDao voucherHistoryDao;
	
	@Autowired
	ICkmxService ckmxService;
	
	@Autowired
	DispatchListDao dispatchListDao;
	
	@Autowired
	IFhmxService fhmxService;
	
	@Autowired
	IFhglDao fhglDao;
	
	@Autowired
	DispatchListsDao dispatchListsDao;
	
	@Autowired
	SO_SOMainDao sO_SOMainDao;

	@Autowired
	SO_SODetailsDao sO_SODetailsDao;

	@Autowired
	IPU_AppVouchDao pu_appvouchDao;
	@Autowired
	IJcghmxService jcghmxService;
	@Autowired
	RedisUtil redisUtil;

	@Autowired
	CustomerDao customerDao;

	@Autowired
	Customer_extradefineDao customer_extradefineDao;

	@Autowired
	Tc_tmp_duplicationDao tc_tmp_duplicationDao;

	@Autowired
	Sa_invoicecustomersDao sa_invoicecustomersDao;

	@Autowired
	PO_PomainDao pO_PomainDao;

	@Autowired
	OM_MOMainDao om_moMainDao;

	@Autowired
	OM_MODetailsDao om_moDetailsDao;

	@Autowired
	OM_MOMaterialsDao om_moMaterialsDao;

	@Autowired
	IHtmxDao htmxDao;

	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	Fa_ObjectsDao fa_objectsDao;
	@Autowired
	Fa_CardsDao fa_cardsDao;
	@Autowired
	Fa_Cards_DetailDao fa_cards_detailDao;
	@Autowired
	Fa_ZWVouchersDao fa_zwVouchersDao;
	@Autowired
	Fa_DeprTransactions_DetailDao fa_deprTransactions_detailDao;
	@Autowired
	Fa_DeptScaleDao fa_deptScaleDao;
	@Autowired
	Fa_ItemsManualDao fa_itemsManualDao;
	@Autowired
	GL_accvouchDao gl_accvouchDao;
	@Autowired
	GL_CashTableDao gl_cashTableDao;
	@Autowired
	IJcjyglDao jcjyglDao;
	@Autowired
	GL_mendDao gl_mendDao;
	private final Logger log = LoggerFactory.getLogger(RdRecordServiceImpl.class);
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public Map<String, Object> pendingU8Data(DhxxDto dhxxDto,List<DhxxDto> dhxxDtos) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		// U8生成入库单
		List<HwxxDto> hwxxs = hwxxDao.getListByDhid(dhxxDto.getDhid());
		//List<DhxxDto> dhxxDtos = dhxxService.getDtoList(dhxxDto);

		RdRecordDto rdRecordDto = new RdRecordDto();

		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String prefix = sdf_d.format(date);
		VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
		voucherHistoryDto.setcSeed(prefix);
		voucherHistoryDto.setCardNumber("24");
		//获取最大流水号
		VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
		if(voucherHistoryDto_t!=null) {
			String serial = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
			rdRecordDto.setcCode(prefix+(serial.length()>1?serial:"0"+serial)); // 到货单号
			rdRecordDto.setPrefix(prefix);
			rdRecordDto.setSflxbj("1");
			List<RdRecordDto> rdRecordDtos_t = rdRecordDao.getDtoList(rdRecordDto);
			int ccode_new=0;
			if (rdRecordDtos_t != null && rdRecordDtos_t.size() > 0) {
				List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(rdRecordDto);
				ccode_new = Integer.parseInt(rdRecordDto.getcCode()) + 1;
				for (RdRecordDto rdRecordDto_c : rdList) {
					if(ccode_new-Integer.parseInt(rdRecordDto_c.getcCode())==0) {
						ccode_new = ccode_new+1;
					}else {
						break;
					}
				}
			}
			if(ccode_new!=0) {
				rdRecordDto.setcCode(Integer.toString(ccode_new)); // 到货单号
				voucherHistoryDto_t.setcNumber(Integer.parseInt(Integer.toString(ccode_new).substring(8))+""); //去除多余0
			}else {
				voucherHistoryDto_t.setcNumber(serial);
			}
			//更新最大单号值
			int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
			if(result_ccode<1)
				throw new BusinessException("msg", "更新U8最大单号失败！");
		}else {
			voucherHistoryDto.setiRdFlagSeed("1");
			voucherHistoryDto.setcContent("日期");
			voucherHistoryDto.setcContentRule("日");
			voucherHistoryDto.setbEmpty("0");
			voucherHistoryDto.setcNumber("1");
			rdRecordDto.setcCode(prefix+"01");
			//单号最大表增加数据
			int result_ccode = voucherHistoryDao.insert(voucherHistoryDto);
			if(result_ccode<1)
				throw new BusinessException("msg", "新增U8最大单号失败！");
		}

		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); // 存入账套
		uA_IdentityDto.setcVouchType("rd"); // 存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); // 获取主键最大值
		if(uA_IdentityDto_t!=null) {
			if (StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				uA_IdentityDto_t.setiFatherId(String.valueOf(id + 1));
				rdRecordDto.setID(id + 1000000001);
			}
		}else {
			rdRecordDto.setID(1000000001);
			uA_IdentityDto.setiFatherId("1");
		}

		rdRecordDto.setcSource("采购订单");
		rdRecordDto.setcMaker(dhxxDtos.get(0).getQgsqr()); // 请购申请人
		rdRecordDto.setcDefine1(dhxxDtos.get(0).getJlbh()); // 请购记录编号
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		rdRecordDto.setDnmaketime(sdf.format(date));
		rdRecordDto.setdVeriDate(sdf.format(date));
		rdRecordDto.setDnverifytime(sdf.format(date));
		rdRecordDto.setBredvouch("1");
		rdRecordDto.setbCredit("0");
		rdRecordDto.setcVouchType("01");
		rdRecordDto.setCsysbarcode("||st01|" + rdRecordDto.getcCode()); // "||st01|"+入库单
		rdRecordDto.setIpurorderid(dhxxDtos.get(0).getU8poid()); // 合同关联U8id
		rdRecordDto.setcWhCode(dhxxDtos.get(0).getCkdm()); // 仓库代码
		rdRecordDto.setiTaxRate(dhxxDtos.get(0).getHtmxsl()); // 税率
		rdRecordDto.setcExch_Name(dhxxDtos.get(0).getHtbz()); // 币种
		rdRecordDto.setcBusType("普通采购"); // 采购类型)
		rdRecordDto.setcHandler(dhxxDto.getZsxm()); // 审核人
		rdRecordDto.setdDate(dhxxDtos.get(0).getDhrq()); // 入库日期
		rdRecordDto.setcVenCode(dhxxDtos.get(0).getGysdm()); // 供应商代码
		rdRecordDto.setcOrderCode(dhxxDtos.get(0).getHtnbbh()); // 订单编号
		rdRecordDto.setcDepCode(dhxxDtos.get(0).getJgdm()); // 机构代码
		rdRecordDto.setcRdCode("1"); 
		rdRecordDto.setcPTCode("01");

		int result = rdRecordDao.insert(rdRecordDto);
		if (result < 1) {
			throw new BusinessException("msg", "入库数据存入U8失败！");
		}

		// 存入关联id
		int rdRecordId = rdRecordDto.getID();
		dhxxDto.setGlid(Integer.toString(rdRecordId));
		map.put("dhxxDto", dhxxDto);

		// 存货物信息
		List<HwxxDto> hwxxDtoList = new ArrayList<>();
		// 存批量更新的U8请购信息
		List<PU_AppVouchsDto> PU_AppVouchsDtos = new ArrayList<>();
		// 存批量更新的U8合同明细信息
		List<PO_PodetailsDto> pO_PodetailsDtos = new ArrayList<>();
		// 存批量更新U8入库明细数据
		List<RdRecordsDto> rdRecordsDtoList = new ArrayList<>();
		// 存物料编码，用于查库存
		StringBuilder ids = new StringBuilder();
		int irowno = 1;
		int id_s = 0;
		if(uA_IdentityDto_t!=null) {
			id_s = Integer.parseInt(uA_IdentityDto_t.getiChildId());
		}
		//存货位调整信息
		List<InvPositionDto> invPositionDtos = new ArrayList<>();
		//存记账记录数据
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos = new ArrayList<>();
		for (HwxxDto hwxxDto : hwxxs) {

			ids.append(",").append(hwxxDto.getWlbm());
			RdRecordsDto rdRecordsDto = new RdRecordsDto();
			if (StringUtil.isNotBlank(String.valueOf(id_s))) {
				rdRecordsDto.setAutoID(id_s + 1000000001);
				id_s = id_s + 1;
			} else {
				throw new BusinessException("msg", "生成U8明细主键id失败！");
			}
			// 算出入库数量 iOriTax
			double sl = Double.parseDouble(hwxxDto.getDhsl());
			rdRecordsDto.setID(rdRecordDto.getID()); // 关联id
			rdRecordsDto.setcInvCode(hwxxDto.getWlbm()); // 物料编码
			rdRecordsDto.setiQuantity(Double.toString(sl)); // 数量
			rdRecordsDto.setiUnitCost(hwxxDto.getWsdj()); // 原币单价
			// 无税总金额 = 含税总金额 /(1+税率/100)
			BigDecimal hjje = new BigDecimal(sl).multiply(new BigDecimal(StringUtil.isNotBlank(hwxxDto.getHsdj())?hwxxDto.getHsdj():"0")); // 总金额
			hjje=hjje.setScale(2,RoundingMode.HALF_UP);
			BigDecimal suil = new BigDecimal(StringUtil.isNotBlank(hwxxDto.getSuil())?hwxxDto.getSuil():"0").divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
			BigDecimal wszje = hjje.divide(suil.add(new BigDecimal("1")), 2, RoundingMode.HALF_UP);
			rdRecordsDto.setiPrice(wszje.toString()); // 原币金额
			rdRecordsDto.setiAPrice(wszje.toString());// 原币金额
			rdRecordsDto.setcBatch(hwxxDto.getScph()); // 生产批号
			rdRecordsDto.setcItem_class(StringUtil.isNotBlank(hwxxDto.getXmdldm())?hwxxDto.getXmdldm():""); // 项目大类代码
			rdRecordsDto.setcItemCode(StringUtil.isNotBlank(hwxxDto.getXmdldm())?hwxxDto.getXmbmdm():""); // 项目编码代码
			rdRecordsDto.setcItemCName(StringUtil.isNotBlank(hwxxDto.getXmdldm())?hwxxDto.getXmbmmc():""); // 项目编码名称
			if (("0").equals(hwxxDto.getBzqflg())) {
				rdRecordsDto.setcMassUnit("2");
				rdRecordsDto.setiMassDate(hwxxDto.getBzq()); // 有效期数
			} else {
				rdRecordsDto.setcMassUnit("1");
				rdRecordsDto.setiMassDate("99"); // 有效期数
			}
			rdRecordsDto.setcPosition(hwxxDto.getCskwdm()); // 库位
			rdRecordsDto.setdVDate(hwxxDto.getYxq());// 失效日期
			rdRecordsDto.setiPOsID(hwxxDto.getU8mxid());// 合同明细U8 ID
			rdRecordsDto.setfACost(hwxxDto.getWsdj());// 无税单价
			rdRecordsDto.setcName(hwxxDto.getXmbmmc());// 项目编码名称
			rdRecordsDto.setcItemCName(hwxxDto.getXmdlmc());// 项目大类名称
			rdRecordsDto.setiNQuantity(Double.toString(sl));// 数量
			rdRecordsDto.setdMadeDate(hwxxDto.getScrq());// 生产日期
			rdRecordsDto.setChVencode(hwxxDto.getGysdm());// 供应商代码
			rdRecordsDto.setiOriTaxCost(hwxxDto.getHsdj()); // 原币含税单价
			rdRecordsDto.setiOriCost(hwxxDto.getWsdj()); // 原币单价
			rdRecordsDto.setiOriMoney(wszje.toString()); // 原币金额
			// 计算税额
			BigDecimal se = hjje.subtract(wszje);
			rdRecordsDto.setiOriTaxPrice(se.toString()); // 原币税额
			rdRecordsDto.setIoriSum(hjje.toString());// 原币价税总额
			rdRecordsDto.setiTaxRate(hwxxDto.getSuil()); // 税率
			rdRecordsDto.setiTaxPrice(se.toString()); // 税额
			rdRecordsDto.setiSum(hjje.toString()); // 原币价税总额
			rdRecordsDto.setcPOID(hwxxDto.getHtnbbh()); // 关联合同单
			rdRecordsDto.setIrowno(Integer.toString(irowno));
			rdRecordsDto.setCbsysbarcode(rdRecordDto.getCsysbarcode() + "|" + irowno);
			irowno = irowno + 1;
			rdRecordsDto.setiSQuantity("0");
			rdRecordsDto.setiSNum("0");
			rdRecordsDto.setiMoney("0");
			rdRecordsDto.setbTaxCost("0");
//			rdRecordsDto.setcDefine29(hwxxDto.getYchh()); // 货号
//			rdRecordsDto.setcDefine23(hwxxDto.getGysmc());// 供应商名称
//			rdRecordsDto.setcDefine22(hwxxDto.getZsh());// 追溯号
			rdRecordsDto.setCbMemo(StringUtil.isNotBlank(hwxxDto.getDhbz())?(hwxxDto.getDhbz().length()>255?hwxxDto.getDhbz().substring(0,255):hwxxDto.getDhbz()):null); //到货备注
			rdRecordsDtoList.add(rdRecordsDto);

			//组装记账记录数据
			IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto = new IA_ST_UnAccountVouchDto();
			iA_ST_UnAccountVouchDto.setIDUN(String.valueOf(rdRecordsDto.getID()));
			iA_ST_UnAccountVouchDto.setIDSUN(String.valueOf(rdRecordsDto.getAutoID()));
			iA_ST_UnAccountVouchDto.setcVouTypeUN("01");
			iA_ST_UnAccountVouchDto.setcBUstypeUN("采购入库");
			iA_ST_UnAccountVouchDtos.add(iA_ST_UnAccountVouchDto);

			// 存入关联id
			HwxxDto hwxx_list = new HwxxDto();
			int hwglid = rdRecordsDto.getAutoID();
			hwxx_list.setGlid(Integer.toString(hwglid));
			hwxx_list.setU8rkdh(rdRecordDto.getcCode());
			hwxx_list.setHwid(hwxxDto.getHwid());
			hwxx_list.setXgry(dhxxDto.getXgry());
			hwxxDtoList.add(hwxx_list);
			map.put("hwxxDtoList", hwxxDtoList);

			// 存入要更新的U8的请购明细
			if (StringUtil.isNotBlank(hwxxDto.getQgmxid())) {
				PU_AppVouchsDto pU_AppVouchsDto = new PU_AppVouchsDto();
				pU_AppVouchsDto.setAutoID(hwxxDto.getQgglid());
				pU_AppVouchsDto.setiReceivedQTY(Double.toString(sl)); // 入库数量
				PU_AppVouchsDtos.add(pU_AppVouchsDto);
			}

			// 存入要更新的U8的合同明细
			PO_PodetailsDto pO_PodetailsDto = new PO_PodetailsDto();
			pO_PodetailsDto.setID(Integer.parseInt(hwxxDto.getHtglid()));
			pO_PodetailsDto.setiReceivedQTY(Double.toString(sl));// 入库数量
			// 计算入库金额 入库金额 = 无税单价 * 数量
			String wsdj_t;
			if (StringUtil.isNotBlank(hwxxDto.getWsdj())) {
				wsdj_t = hwxxDto.getWsdj(); // 无税单价
			} else {
				BigDecimal suil_t = new BigDecimal(hwxxDto.getSuil()).divide(new BigDecimal(100), 5,
						RoundingMode.HALF_UP);
				wsdj_t = new BigDecimal(hwxxDto.getHsdj())
						.divide(suil_t.add(new BigDecimal(1)), 5, RoundingMode.HALF_UP).toString();
			}
			BigDecimal wsdj = new BigDecimal(wsdj_t);
			BigDecimal sl_t = new BigDecimal(Double.toString(sl)); // 入库数量
			BigDecimal rkje = wsdj.multiply(sl_t);
			rkje = rkje.setScale(2, RoundingMode.HALF_UP);// 四舍五入保留两位小数
			pO_PodetailsDto.setiReceivedMoney(rkje.toString());// 入库金额
			pO_PodetailsDto.setiReceivedNum("0");
			pO_PodetailsDtos.add(pO_PodetailsDto);

			//处理货位调整表
			InvPositionDto invPositionDto = new InvPositionDto();
			invPositionDto.setRdsID(String.valueOf(rdRecordsDto.getAutoID()));
			invPositionDto.setRDID(String.valueOf(rdRecordDto.getID()));
			invPositionDto.setcWhCode(dhxxDtos.get(0).getCkdm());
			invPositionDto.setcPosCode(hwxxDto.getCskwdm());
			invPositionDto.setcInvCode(hwxxDto.getWlbm());
			invPositionDto.setcBatch(hwxxDto.getScph());
			invPositionDto.setdVDate(hwxxDto.getYxq());
			invPositionDto.setiQuantity(Double.toString(sl));
			invPositionDto.setcHandler(dhxxDto.getZsxm());
			invPositionDto.setdDate(dhxxDtos.get(0).getDhrq());
			invPositionDto.setbRdFlag("1");
			invPositionDto.setdMadeDate(hwxxDto.getScrq());
			if (("0").equals(hwxxDto.getBzqflg())) {
				invPositionDto.setcMassUnit("2");
				invPositionDto.setiMassDate(hwxxDto.getBzq()); // 有效期数
			} else {
				invPositionDto.setcMassUnit("1");
				invPositionDto.setiMassDate("99"); // 有效期数
			}
			invPositionDto.setCvouchtype("01");
			Date date_t = new Date();
			invPositionDto.setdVouchDate(sdf.format(date_t));
			invPositionDtos.add(invPositionDto);
		}

		// 更新U8入库明细表
		//若List过大进行截断,10个明细为一组进行添加
		int result_rds;
		List<List<RdRecordsDto>> partition = Lists.partition(rdRecordsDtoList, 10);
		for (List<RdRecordsDto> rdRecordsDtos : partition) {
			result_rds = rdRecordsDao.insertRds(rdRecordsDtos);
			if (result_rds < 1)
				throw new BusinessException("msg", "更新U8入库明细表失败！");
		}
		// 更新最大值表
		boolean result_ui = false;
		//判断是新增还是修改
		if(uA_IdentityDto_t!=null) {
			//更新
			uA_IdentityDto_t.setiChildId(String.valueOf(id_s));
			result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
		}
		if(!result_ui) {
			uA_IdentityDto.setiChildId(String.valueOf(id_s));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}

		// 更新U8请购表入库数量
		//若List过大进行截断,10个明细为一组进行添加
		int result_PU_App;
		List<List<PU_AppVouchsDto>> PU_AppVouchsDtosList = Lists.partition(PU_AppVouchsDtos, 10);
		for (List<PU_AppVouchsDto> pu_appVouchsDtos : PU_AppVouchsDtosList) {
			result_PU_App = PU_AppVouchDao.updateIReceivedQTY(pu_appVouchsDtos);
			if (result_PU_App < 1)
				throw new BusinessException("msg", "更新U8请购入库数量失败！");
		}
		// 更新U8合同明细入库信息
		//若List过大进行截断,10个明细为一组进行添加
		int result_pO_Pode;
		List<List<PO_PodetailsDto>> pO_PodetailsDtosList = Lists.partition(pO_PodetailsDtos, 10);
		for (List<PO_PodetailsDto> po_podetailsDtos : pO_PodetailsDtosList) {
			result_pO_Pode = pO_PodetailsDao.updateRkxx(po_podetailsDtos);
			if (result_pO_Pode < 1)
				throw new BusinessException("msg", "更新U8合同入库信息失败！");
		}
		//若List过大进行截断,10个明细为一组进行添加
		int result_hw;
		List<List<InvPositionDto>> invPositionDtosList = Lists.partition(invPositionDtos, 10);
		for (List<InvPositionDto> positionDtos : invPositionDtosList) {
			result_hw = invPositionDao.insertDtos(positionDtos);
			if (result_hw < 1)
				throw new BusinessException("msg", "新增U8货位调整失败！");
		}
		//若List过大进行截断,10个明细为一组进行添加
		boolean result_av;
		List<List<IA_ST_UnAccountVouchDto>> iA_ST_UnAccountVouchDtosList = Lists.partition(iA_ST_UnAccountVouchDtos, 10);
		for (List<IA_ST_UnAccountVouchDto> ia_st_unAccountVouchDtos : iA_ST_UnAccountVouchDtosList) {
			result_av = addAccountVs(ia_st_unAccountVouchDtos,"01");
			if(!result_av)
				throw new BusinessException("msg", "记账记录新增失败！");
		}
		// 根据生产批号物料分组
		List<HwxxDto> hwxxList = hwxxService.queryByDhid(dhxxDto.getDhid());
		// 获取U8已经存在得库存
		ids = new StringBuilder(ids.substring(1));
		CurrentStockDto currentStockDto_t = new CurrentStockDto();
		currentStockDto_t.setIds(ids.toString());
		List<CurrentStockDto> currentStockDtos_t = currentStockDao.queryBycInvCode(currentStockDto_t);
		// 存要更新得货物id,更新库存关联id
		List<HwxxDto> hwxxDto_hs = new ArrayList<>();
		// 存入批量更新得U8库存数据
		List<CurrentStockDto> currentStockDtos = new ArrayList<>();
		//存货位货物总数信息
		List<InvPositionSumDto> invPositionSumDtos_add = new ArrayList<>();
		List<InvPositionSumDto> invPositionSumDtos_mod = new ArrayList<>();
		for (HwxxDto hwxxDto_list : hwxxList) {
			for (HwxxDto hwxx_hwxxs : hwxxs) {
				if (hwxxDto_list.getScph().equals(hwxx_hwxxs.getScph())
						&& hwxxDto_list.getWlid().equals(hwxx_hwxxs.getWlid())) {
					// 判断U8是否存在该物料
					if (currentStockDtos_t.size() > 0) {
						boolean modflg = true;
						for (CurrentStockDto current_t : currentStockDtos_t) {
							if (dhxxDtos.get(0).getCkdm().equals(current_t.getcWhCode())
									&& hwxx_hwxxs.getWlbm().equals(current_t.getcInvCode())
									&& hwxx_hwxxs.getScph().equals(current_t.getcBatch())) {
								// 如果仓库，物料编码和生产批号都相同，就修改库存数量
								CurrentStockDto currentStockDto_cu = new CurrentStockDto();
								double sl = Double
										.parseDouble(StringUtils.isNotBlank(hwxxDto_list.getU8rksl())
												? hwxxDto_list.getU8rksl()
												: "0")
										+ Double.parseDouble(StringUtils.isNotBlank(current_t.getiQuantity())
										? current_t.getiQuantity()
										: "0");
								currentStockDto_cu.setiQuantity(Double.toString(sl)); // 库存数量
								currentStockDto_cu.setAutoID(current_t.getAutoID()); // 存入主键id
								currentStockDto_cu.setcBatch(current_t.getcBatch());
								currentStockDto_cu.setcWhCode(current_t.getcWhCode());
								currentStockDto_cu.setcInvCode(current_t.getcInvCode());
								currentStockDtos.add(currentStockDto_cu);

								// 存入关联id
								HwxxDto hwxx_hs = new HwxxDto();
								hwxx_hs.setWlid(hwxx_hwxxs.getWlid());
								hwxx_hs.setDhid(hwxx_hwxxs.getDhid());
								hwxx_hs.setScph(hwxx_hwxxs.getScph());
								hwxx_hs.setKcglid(String.valueOf(currentStockDto_cu.getAutoID())); // 存入关联id
								hwxxDto_hs.add(hwxx_hs);
								modflg = false;
								break;
							}
						}
						if (modflg) {
							// 新增得U8库存数据
							CurrentStockDto current_add = new CurrentStockDto();
							current_add.setcInvCode(hwxx_hwxxs.getWlbm()); // 物料编码
							current_add.setcWhCode(dhxxDtos.get(0).getCkdm());// 仓库编码
							boolean itemidflg = true;
							for (CurrentStockDto current_t : currentStockDtos_t) {
								if (hwxx_hwxxs.getWlbm().equals(current_t.getcInvCode())) {
									// 如果物料编码一样，追溯号不一样，ItemId字段相同
									current_add.setItemId(current_t.getItemId());
									itemidflg = false;
								}
							}

							if (itemidflg) {
								SCM_ItemDto sCM_ItemDto_c  = sCM_ItemDao.getDtoBycInvVode(current_add.getcInvCode());
								if(sCM_ItemDto_c!=null) {
									current_add.setItemId(sCM_ItemDto_c.getId());
								}else {
									// 取SCM_Item表中最大值
									int num_ItemId = sCM_ItemDao.getMaxItemId() + 1;
									current_add.setItemId(Integer.toString(num_ItemId));
									// 存入新增的物料
									SCM_ItemDto sCM_ItemDto = new SCM_ItemDto();
									sCM_ItemDto.setcInvCode(current_add.getcInvCode()); // 物料编码
									int result_scm = sCM_ItemDao.insert(sCM_ItemDto);
									if (result_scm < 1) {
										throw new BusinessException("msg", "sCM_Item新增失败！");
									}
								}
							}
							current_add.setiQuantity(hwxxDto_list.getU8rksl()); // 现存数量
							current_add.setcBatch(hwxx_hwxxs.getScph()); // 生产批号
							current_add.setdVDate(hwxx_hwxxs.getYxq()); // 失效日期
							current_add.setdMdate(hwxx_hwxxs.getScrq()); // 生产日期
							current_add.setiExpiratDateCalcu("0");
							if (("0").equals(hwxx_hwxxs.getBzqflg())) {
								current_add.setcMassUnit("2"); // 有效单位
								current_add.setiMassDate(hwxx_hwxxs.getBzq()); // 有效期数
							} else {
								current_add.setcMassUnit("1"); // 有效单位
								current_add.setiMassDate("99"); // 有效期数
							}
							int result_add = currentStockDao.insert(current_add);
							if (result_add < 1) {
								throw new BusinessException("msg", "U8库存新增失败！");
							}

							// 存入关联id
							HwxxDto hwxx_hs = new HwxxDto();
							hwxx_hs.setWlid(hwxx_hwxxs.getWlid());
							hwxx_hs.setDhid(hwxx_hwxxs.getDhid());
							hwxx_hs.setScph(hwxx_hwxxs.getScph());
							hwxx_hs.setKcglid(String.valueOf(current_add.getAutoID())); // 存入关联id
							hwxxDto_hs.add(hwxx_hs);
						}
					} else {
						// 新增得U8库存数据
						CurrentStockDto current_add_t = new CurrentStockDto();
						current_add_t.setcInvCode(hwxx_hwxxs.getWlbm()); // 物料编码
						current_add_t.setcWhCode(dhxxDtos.get(0).getCkdm());// 仓库编码
						SCM_ItemDto sCM_ItemDto_c  = sCM_ItemDao.getDtoBycInvVode(hwxx_hwxxs.getWlbm());
						if(sCM_ItemDto_c!=null) {
							current_add_t.setItemId(sCM_ItemDto_c.getId());
						}else {
							// 存入新增的物料
							SCM_ItemDto sCM_ItemDto = new SCM_ItemDto();
							sCM_ItemDto.setcInvCode(current_add_t.getcInvCode()); // 物料编码
							int result_scm = sCM_ItemDao.insert(sCM_ItemDto);
							if (result_scm < 1) {
								throw new BusinessException("msg", "sCM_Item新增失败！");
							}
							SCM_ItemDto sCM_ItemDto_t  = sCM_ItemDao.getDtoBycInvVode(hwxx_hwxxs.getWlbm());
							current_add_t.setItemId(sCM_ItemDto_t.getId());
						}
						current_add_t.setiQuantity(hwxxDto_list.getU8rksl()); // 现存数量
						current_add_t.setcBatch(hwxx_hwxxs.getScph()); // 生产批号
						current_add_t.setdVDate(hwxx_hwxxs.getYxq()); // 失效日期
						current_add_t.setdMdate(hwxx_hwxxs.getScrq()); // 生产日期
						current_add_t.setiExpiratDateCalcu("0");
						if (("0").equals(hwxx_hwxxs.getBzqflg())) {
							current_add_t.setcMassUnit("2"); // 有效单位
							current_add_t.setiMassDate(hwxx_hwxxs.getBzq()); // 有效期数
						} else {
							current_add_t.setcMassUnit("1"); // 有效单位
							current_add_t.setiMassDate("99"); // 有效期数
						}
						int result_add = currentStockDao.insert(current_add_t);
						if (result_add < 1) {
							throw new BusinessException("msg", "U8库存添加失败！");
						}

						// 存入关联id
						HwxxDto hwxx_hs = new HwxxDto();
						hwxx_hs.setWlid(hwxx_hwxxs.getWlid());
						hwxx_hs.setDhid(hwxx_hwxxs.getDhid());
						hwxx_hs.setScph(hwxx_hwxxs.getScph());
						hwxx_hs.setKcglid(String.valueOf(current_add_t.getAutoID()));
						hwxxDto_hs.add(hwxx_hs);
					}
					//处理货位货物总数数据
					InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
					invPositionSumDto.setcWhCode(dhxxDtos.get(0).getCkdm());
					invPositionSumDto.setcPosCode(hwxx_hwxxs.getCskwdm());
					invPositionSumDto.setcInvCode(hwxx_hwxxs.getWlbm());
					invPositionSumDto.setcBatch(hwxx_hwxxs.getScph());
					//判断是否存在  仓库，物料编码，库位，生产批号一样的数据
					InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
					if(invPositionSumDto_t!=null) {
						//存在，更新货物货物数量
						invPositionSumDto_t.setJsbj("0"); //计算标记,相加
						invPositionSumDto_t.setiQuantity(hwxxDto_list.getU8rksl());
						invPositionSumDtos_mod.add(invPositionSumDto_t);
					}else {
						//不存在，做新增
						invPositionSumDto.setiQuantity(hwxxDto_list.getU8rksl());
						invPositionSumDto.setInum("0");
						if (("0").equals(hwxx_hwxxs.getBzqflg())) {
							invPositionSumDto.setcMassUnit("2");
							invPositionSumDto.setiMassDate(hwxx_hwxxs.getBzq()); // 保质期
						} else {
							invPositionSumDto.setcMassUnit("1");
							invPositionSumDto.setiMassDate("99"); // 保质期
						}
						invPositionSumDto.setdMadeDate(hwxx_hwxxs.getScrq());
						invPositionSumDto.setdVDate(hwxx_hwxxs.getYxq());
						invPositionSumDtos_add.add(invPositionSumDto);
					}
					break;
				}

			}
		}

		//更新库存
		if(currentStockDtos.size()>0) {
			//若List过大进行截断,10个明细为一组进行添加
			boolean reulu_mod;
			if(currentStockDtos.size()>10) {
				int length = (int)Math.ceil((double)currentStockDtos.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<CurrentStockDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = currentStockDtos.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(currentStockDtos.get(j));
					}
					reulu_mod = currentStockDao.updateKwAndSl(list);
					if(!reulu_mod) {
						throw new BusinessException("msg","库存数量更新失败！");
					}
				}
			}else{
				reulu_mod = currentStockDao.updateKwAndSl(currentStockDtos);
				if(!reulu_mod) {
					throw new BusinessException("msg","库存数量更新失败！");
				}
			}
		}
		//若List过大进行截断,10个明细为一组进行添加
		//新增货物总数信息
		int result_add;
		List<List<InvPositionSumDto>> invPositionSumDtos_addList = Lists.partition(invPositionSumDtos_add, 10);
		for (List<InvPositionSumDto> invPositionSumDtos : invPositionSumDtos_addList) {
			result_add = invPositionSumDao.insertDtos(invPositionSumDtos);
			if(result_add<1) {
				throw new BusinessException("msg", "新增U8货位总数失败！");
			}
		}
		//修改货物总数信息
		int result_mod;
		List<List<InvPositionSumDto>> invPositionSumDtos_modList = Lists.partition(invPositionSumDtos_mod, 10);
		for (List<InvPositionSumDto> invPositionSumDtos : invPositionSumDtos_modList) {
			result_mod = invPositionSumDao.updateDtos(invPositionSumDtos);
			if(result_mod<1) {
				throw new BusinessException("msg", "更新U8货位总数失败！");
			}
		}
		//更新库存关联id
		map.put("hwxxDto_hs", hwxxDto_hs);
		return map;
	}

	/**
	 * 入库审核处理U8数据
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public Map<String,Object> dealRecordData(RkglDto rkglDto_t,List<HwxxDto> hwxxs_lb) throws BusinessException {
		Map<String,Object> map=new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf_code = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		List<HwxxDto> hwxxs = new ArrayList<>();
//
		//存要更新的货物信息库存量
		List<HwxxDto> hwxxs_kcl = new ArrayList<>();
		//入库还是调拨标记
		boolean isRk = false;
		//判断是否是成品入库
		boolean isCprk = false;
		for (HwxxDto hwxxDto_lb : hwxxs_lb) {
			HwxxDto hwxx_kcl = new HwxxDto();
			hwxx_kcl.setKcl(hwxxDto_lb.getSl());
			hwxx_kcl.setHwid(hwxxDto_lb.getHwid());
			hwxx_kcl.setZt("99");
			hwxx_kcl.setDhlxdm(hwxxDto_lb.getDhlxdm());
			hwxxs_kcl.add(hwxx_kcl);
			if("1".equals(hwxxDto_lb.getLbbj())) {
				hwxxs.add(hwxxDto_lb);
			}else {
				isRk =true;
			}
			if("1".equals(hwxxDto_lb.getRklxcskz1())) {
				isCprk = true;
			}
		}
		if(isRk) {
			Map<String,Object> map_t = new HashMap<>();
			String dhlxdm = !CollectionUtils.isEmpty(hwxxs_lb)?hwxxs_lb.get(0).getDhlxdm():"";
			if(!"OA".equals(dhlxdm)){
				if(isCprk) {
					//成品入库
					map_t = addU8CprkDataByRk(rkglDto_t);
				}else {
					//采购入库
					map_t = addU8RkDataRk(rkglDto_t);
				}
			}
			map_t.put("hwxxs_kcl", hwxxs_kcl);
			map_t.put("isCprk", isCprk?"1":"0");
			map_t.put("isRk", "1");
			return map_t;
		}else {
			//存入入库标记，1入库，0调拨
			map.put("isRk", "0");
			map.put("hwxxs_kcl", hwxxs_kcl);
			String dhlxdm = !CollectionUtils.isEmpty(hwxxs_lb)?hwxxs_lb.get(0).getDhlxdm():"";
			if(!"OA".equals(dhlxdm)){
				if(!CollectionUtils.isEmpty(hwxxs)) {
					UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
					uA_IdentityDto.setcAcc_Id(accountName); //存入账套
					uA_IdentityDto.setcVouchType("tr");	//存入表标识
					UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
					//U8生成调拨单
					if(CollectionUtils.isEmpty(hwxxs))
						throw new BusinessException("ICOM99019","货物明细为空！");
					DhxxDto dhxxDto=dhxxDao.getDtoById(hwxxs.get(0).getDhid());//获取到货时仓库信息
					TransVouchDto transVouchDto = new TransVouchDto();
					//生成单号
					VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
					voucherHistoryDto.setCardNumber("0304");
					//获取最大流水号
					VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
					if(voucherHistoryDto_t!=null) {
						String serialnum = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
						String serial = "0000000000"+ serialnum;
						serial = serial.substring(serial.length()-10);
						transVouchDto.setcTVCode(serial); // 到货单号
						//判断单号是否存在
						int ccode_new=0;
						TransVouchDto transVouchDto_t = transVouchDao.getDtoBycTVCode(transVouchDto);
						if(transVouchDto_t!=null) {
							List<TransVouchDto> rdList = transVouchDao.getcTVCode(transVouchDto);
							ccode_new = Integer.parseInt(transVouchDto.getcTVCode()) + 1;
							for (TransVouchDto transVouchDto_c : rdList) {
								if(ccode_new-Integer.parseInt(transVouchDto_c.getcTVCode())==0) {
									ccode_new = ccode_new+1;
								}else {
									break;
								}
							}
						}
						if(ccode_new!=0) {
							transVouchDto.setcTVCode(("000000000"+ccode_new).substring(serial.length()-10,serial.length())); // 调拨单号
							voucherHistoryDto_t.setcNumber(Integer.toString(ccode_new));
						}else {
							voucherHistoryDto_t.setcNumber(serialnum);
						}
						//更新最大单号值
						int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
						if(result_ccode<1)
							throw new BusinessException("msg", "更新U8最大单号失败！");
					}else {
						voucherHistoryDto.setcNumber("1");
						voucherHistoryDto.setbEmpty("0");
						transVouchDto.setcTVCode("0000000001");
						//单号最大表增加数据
						int result_ccode_in = voucherHistoryDao.insert(voucherHistoryDto);
						if(result_ccode_in<1)
							throw new BusinessException("msg", "新增U8最大单号失败！");
					}

					transVouchDto.setcOWhCode(dhxxDto.getCkdm());//转出仓库
					transVouchDto.setcIWhCode(rkglDto_t.getCkdm());//转入仓库
					transVouchDto.setcTVMemo(StringUtil.isNotBlank(rkglDto_t.getBz())?(rkglDto_t.getBz().length()>255?rkglDto_t.getBz().substring(0,255):rkglDto_t.getBz()):null);//备注
					transVouchDto.setcMaker(rkglDto_t.getLrrymc());//录入人员名称
					transVouchDto.setcIRdCode("3");
					transVouchDto.setcORdCode("7");
					transVouchDto.setiNetLock("0");
					transVouchDto.setCsysbarcode("||st12|"+transVouchDto.getcTVCode());
					transVouchDto.setcVerifyPerson(rkglDto_t.getZsxm());
					if(uA_IdentityDto_t!=null) {
						if(StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
							int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
							uA_IdentityDto_t.setiFatherId(String.valueOf(id +1));
							transVouchDto.setID(String.valueOf((id + 1000000001)));
						}
					}else {
						transVouchDto.setID("1000000001");
						uA_IdentityDto.setiFatherId("1");
						uA_IdentityDto.setiChildId("0");
					}

					boolean insertTransVouch=transVouchDao.insertTransVouch(transVouchDto);

					map.put("dbglid", transVouchDto.getID());//返回调拨单关联ID

					if(!insertTransVouch)
						throw new BusinessException("msg","U8添加调拨单失败！");

					//处理调拨入库主表
					RdRecordDto inrdRecordDto=new RdRecordDto();

					SimpleDateFormat sdf_d = new SimpleDateFormat("yyyyMM");
					String prefix_in = sdf_d.format(date);
					VoucherHistoryDto inVoucherHistoryDto = new VoucherHistoryDto();
					inVoucherHistoryDto.setcSeed(prefix_in);
					inVoucherHistoryDto.setCardNumber("0301");
					//获取最大流水号 210900053
					VoucherHistoryDto inVoucherHistoryDto_t = voucherHistoryDao.getMaxSerial(inVoucherHistoryDto);
					if(inVoucherHistoryDto_t!=null) {
						String serial_in = String.valueOf(Integer.parseInt(inVoucherHistoryDto_t.getcNumber())+1);
						inrdRecordDto.setcCode(prefix_in.substring(2)+"000"+(serial_in.length()>1?serial_in:"0"+serial_in)); // 到货单号
						inrdRecordDto.setPrefix(prefix_in.substring(2));
						inrdRecordDto.setSflxbj("8");
						RdRecordDto rdRecordDtos_t = rdRecordDao.queryByCode(inrdRecordDto);
						int ccode_new_in=0;
						if (rdRecordDtos_t != null ) {
							List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(inrdRecordDto);
							ccode_new_in = Integer.parseInt(inrdRecordDto.getcCode()) + 1;
							for (RdRecordDto rdRecordDto_c_in : rdList) {
								if(ccode_new_in-Integer.parseInt(rdRecordDto_c_in.getcCode())==0) {
									ccode_new_in = ccode_new_in+1;
								}else {
									break;
								}
							}
						}
						if(ccode_new_in!=0) {
							inrdRecordDto.setcCode(Integer.toString(ccode_new_in)); // 到货单号
							inVoucherHistoryDto_t.setcNumber(Integer.parseInt(Integer.toString(ccode_new_in).substring(4))+""); //去除多余0
						}else {
							inVoucherHistoryDto_t.setcNumber(serial_in);
						}
						//更新最大单号值
						int result_ccode_in = voucherHistoryDao.update(inVoucherHistoryDto_t);
						if(result_ccode_in<1)
							throw new BusinessException("msg", "更新U8最大单号失败！");
					}else {
						inVoucherHistoryDto.setiRdFlagSeed("1");
						inVoucherHistoryDto.setcContent("日期");
						inVoucherHistoryDto.setcContentRule("月");
						inVoucherHistoryDto.setbEmpty("0");
						inVoucherHistoryDto.setcNumber("1");
						inrdRecordDto.setcCode(prefix_in+"00001");
						//单号最大表增加数据
						int result_ccode_in = voucherHistoryDao.insert(inVoucherHistoryDto);
						if(result_ccode_in<1)
							throw new BusinessException("msg", "新增U8最大单号失败！");
					}


					inrdRecordDto.setcBusCode(transVouchDto.getcTVCode());
					UA_IdentityDto rk_uA_IdentityDto = new UA_IdentityDto();
					rk_uA_IdentityDto.setcAcc_Id(accountName); //存入账套
					rk_uA_IdentityDto.setcVouchType("rd");	//存入表标识
					UA_IdentityDto rk_uA_IdentityDto_t = uA_IdentityDao.getMax(rk_uA_IdentityDto); //获取主键最大值
					if(rk_uA_IdentityDto_t!=null) {
						if(StringUtil.isNotBlank(rk_uA_IdentityDto_t.getiFatherId())) {
							int id = Integer.parseInt(rk_uA_IdentityDto_t.getiFatherId());
							rk_uA_IdentityDto_t.setiFatherId(String.valueOf(id +1));
							inrdRecordDto.setID(id + 1000000001);
						}
					}else {
						rk_uA_IdentityDto.setiFatherId("1");
						rk_uA_IdentityDto.setiChildId("0");
						inrdRecordDto.setID(1000000001);
					}
					inrdRecordDto.setcSource("调拨");
					inrdRecordDto.setcMaker(rkglDto_t.getLrrymc()); //请购申请人
					inrdRecordDto.setDnmaketime(sdf.format(date));
					inrdRecordDto.setcVouchType("08");
					inrdRecordDto.setCsysbarcode("||st08|"+inrdRecordDto.getcCode());	//	"||st01|"+入库单
					inrdRecordDto.setcWhCode(rkglDto_t.getCkdm()); //仓库代码
					inrdRecordDto.setcBusType("调拨入库"); // 类型)
					inrdRecordDto.setdDate(sdf_code.format(date)); // 入库日期
					inrdRecordDto.setcRdCode("3");
					inrdRecordDto.setcHandler(rkglDto_t.getZsxm());
//				inrdRecordDto.setVT_ID("67");

					boolean result = rdRecordDao.insertRd08(inrdRecordDto);
					if(!result) {
						throw new BusinessException("msg","入库数据存入U8失败！");
					}
					map.put("qtrkglid", inrdRecordDto.getID());//其他入库单关联ID

					//处理调拨出库
					RdRecordDto outrdRecordDto=new RdRecordDto();
					VoucherHistoryDto outVoucherHistoryDto = new VoucherHistoryDto();
					outVoucherHistoryDto.setcSeed(prefix_in);
					outVoucherHistoryDto.setCardNumber("0302");
					//获取最大流水号 210900053
					VoucherHistoryDto outVoucherHistoryDto_t = voucherHistoryDao.getMaxSerial(outVoucherHistoryDto);
					if(outVoucherHistoryDto_t!=null) {
						String serial_out = String.valueOf(Integer.parseInt(outVoucherHistoryDto_t.getcNumber())+1);
						outrdRecordDto.setcCode(prefix_in.substring(2)+"000"+(serial_out.length()>1?serial_out:"0"+serial_out)); // 到货单号
						outrdRecordDto.setPrefix(prefix_in.substring(2));
						outrdRecordDto.setSflxbj("9");
						RdRecordDto outrdRecordDtos_t = rdRecordDao.queryByCode(outrdRecordDto);
						int ccode_new_out=0;
						if (outrdRecordDtos_t != null ) {
							List<RdRecordDto> rdList_out = rdRecordDao.getcCodeRd(outrdRecordDto);
							ccode_new_out = Integer.parseInt(inrdRecordDto.getcCode()) + 1;
							for (RdRecordDto rdRecordDto_c_out : rdList_out) {
								if(ccode_new_out-Integer.parseInt(rdRecordDto_c_out.getcCode())==0) {
									ccode_new_out = ccode_new_out+1;
								}else {
									break;
								}
							}
						}
						if(ccode_new_out!=0) {
							outrdRecordDto.setcCode(Integer.toString(ccode_new_out)); // 到货单号
							outVoucherHistoryDto_t.setcNumber(Integer.parseInt(Integer.toString(ccode_new_out).substring(4))+""); //去除多余0
						}else {
							outVoucherHistoryDto_t.setcNumber(serial_out);
						}
						//更新最大单号值
						int result_ccode_in = voucherHistoryDao.update(outVoucherHistoryDto_t);
						if(result_ccode_in<1)
							throw new BusinessException("msg", "更新U8最大单号失败！");
					}else {
						outVoucherHistoryDto.setiRdFlagSeed("2");
						outVoucherHistoryDto.setcContent("日期");
						outVoucherHistoryDto.setcContentRule("月");
						outVoucherHistoryDto.setbEmpty("0");
						outVoucherHistoryDto.setcNumber("1");
						outrdRecordDto.setcCode(prefix_in+"00001");
						//单号最大表增加数据
						int result_ccode_in = voucherHistoryDao.insert(outVoucherHistoryDto);
						if(result_ccode_in<1)
							throw new BusinessException("msg", "新增U8最大单号失败！");
					}

					outrdRecordDto.setcBusCode(transVouchDto.getcTVCode());
					outrdRecordDto.setcRdCode("7");
					outrdRecordDto.setVT_ID("85");

					if(rk_uA_IdentityDto_t!=null) {
						if(StringUtil.isNotBlank(rk_uA_IdentityDto_t.getiFatherId())) {
							int id = Integer.parseInt(rk_uA_IdentityDto_t.getiFatherId());
							rk_uA_IdentityDto_t.setiFatherId(String.valueOf(id +1));
							outrdRecordDto.setID(id + 1000000001);
						}
					}else {
						if(StringUtil.isNotBlank(rk_uA_IdentityDto.getiFatherId())) {
							int id = Integer.parseInt(rk_uA_IdentityDto.getiFatherId());
							rk_uA_IdentityDto.setiFatherId(String.valueOf(id +1));
							outrdRecordDto.setID(id + 1000000001);
						}
					}
					outrdRecordDto.setcSource("调拨");
					outrdRecordDto.setcMaker(rkglDto_t.getLrrymc()); //请购申请人
					outrdRecordDto.setDnmaketime(sdf.format(date));
					outrdRecordDto.setcVouchType("09");
					outrdRecordDto.setCsysbarcode("||st09|"+outrdRecordDto.getcCode());	//	"||st09|"+出库单
					outrdRecordDto.setcWhCode(dhxxDto.getCkdm()); //仓库代码
					outrdRecordDto.setcBusType("调拨出库"); // 类型)
					outrdRecordDto.setdDate(sdf_code.format(date)); // 出库日期
					outrdRecordDto.setcHandler(rkglDto_t.getZsxm());
					boolean ck_result = rdRecordDao.insertRd09(outrdRecordDto);
					if(!ck_result) {
						throw new BusinessException("msg","入库数据存入U8失败！");
					}
					map.put("qtckglid", outrdRecordDto.getID());//其他出库单关联ID

					List<TransVouchsDto> transVouchList=new ArrayList<>();
					List<String> cinvcodes=new ArrayList<>();//存放库存关联id
					List<HwxxDto> hwxxlist_t = new ArrayList<>();
					for (HwxxDto hwxxDto : hwxxs) {
						//set库存关联ID
						cinvcodes.add(hwxxDto.getWlbm());
						HwxxDto hwxx_t = new HwxxDto();
						hwxx_t.setHwid(hwxxDto.getHwid());
						hwxx_t.setWlbm(hwxxDto.getWlbm());
						hwxx_t.setScph(hwxxDto.getScph());
						hwxxlist_t.add(hwxx_t);
					}
					map.put("hwxxlist_t", hwxxlist_t);
					List<RdRecordsDto> inRdRecordsDtos=new ArrayList<>();//调拨入库
					List<RdRecordsDto> outRdRecordsDtos=new ArrayList<>();//调拨出库
					CurrentStockDto curr = new CurrentStockDto();
					curr.setIds(cinvcodes);
					List<CurrentStockDto> currentStockDtos=currentStockDao.queryBycInvCode(curr);
					if(currentStockDtos==null || currentStockDtos.size()<=0)
						throw new BusinessException("msg","未获取到U8关联库存信息！");
					List<CurrentStockDto> t_addcurrentStockList=new ArrayList<>();//存放需要新增的数据
					List<CurrentStockDto> addcurrentStockList=new ArrayList<>();//新增(复制实体类)
					List<CurrentStockDto> modcurrentStockList=new ArrayList<>();//修改(复制实体类，存放需要修改的数据)
					for(CurrentStockDto currentStockDto : currentStockDtos) {
						try {
							modcurrentStockList.add((CurrentStockDto)currentStockDto.clone());
							addcurrentStockList.add((CurrentStockDto)currentStockDto.clone());
						} catch (CloneNotSupportedException e) {
							// TODO Auto-generated catch block
							throw new BusinessException("CurrentStockDto克隆出错！");
						}
					}
					HwxxDto hwxxDto_t=new HwxxDto();
					hwxxDto_t.setRkid(rkglDto_t.getRkid());
					List<HwxxDto> hwxxDtos_ts=hwxxService.queryByRkid(hwxxDto_t);//查询入库货物信息根据物料和生产批号分组
					//存货位调整
					List<InvPositionDto> invPositionDtos = new ArrayList<>();
					//存货位货物总数
					List<InvPositionSumDto> invPositionSumDtos_mod = new ArrayList<>();
					List<InvPositionSumDto> invPositionSumDtos_add = new ArrayList<>();
					//先处理修改的
					for (HwxxDto hwxxDto : hwxxs) {
						for(CurrentStockDto modcurrentStock : modcurrentStockList) {
							if(StringUtil.isNotBlank(hwxxDto.getScph()) && StringUtil.isNotBlank(modcurrentStock.getcBatch())) {
								if(hwxxDto.getWlbm().equals(modcurrentStock.getcInvCode()) && hwxxDto.getScph().equals(modcurrentStock.getcBatch()) && hwxxDto.getDhckdm().equals(modcurrentStock.getcWhCode())) {
									BigDecimal oldiQuantity=new BigDecimal(modcurrentStock.getiQuantity());//调拨前数量
									BigDecimal subiQuantity=new BigDecimal(hwxxDto.getSl());//要调拨的数量
									BigDecimal newiQuantity = oldiQuantity.subtract(subiQuantity);//调拨前数量-要调拨的数量
									if(newiQuantity.compareTo(BigDecimal.ZERO) < 0) {
										throw new BusinessException("msg","U8库存不足，不允许其他出库！");
									}
									newiQuantity=newiQuantity.setScale(2, RoundingMode.HALF_UP);//四舍五入至2位小数
									modcurrentStock.setiQuantity(newiQuantity.toString());
								}
							}else {
								throw new BusinessException("msg","货物生产批号为空！");
							}
						}
					}
					//处理拆分的，需要新增的，需要叠加数量的
					if (!CollectionUtils.isEmpty(hwxxDtos_ts)) {
						for(HwxxDto hwxxDtos_t : hwxxDtos_ts) {
							for (HwxxDto hwxxDto : hwxxs) {
								if(StringUtil.isNotBlank(hwxxDtos_t.getScph())) {
									if (StringUtil.isNotBlank(hwxxDto.getScph()) && hwxxDtos_t.getScph().equals(hwxxDto.getScph())
											&& hwxxDtos_t.getWlid().equals(hwxxDto.getWlid())) {
										CurrentStockDto currentStockDto_mod = new CurrentStockDto();
										currentStockDto_mod.setcInvCode(hwxxDto.getWlbm());
										currentStockDto_mod.setcBatch(hwxxDto.getScph());
										currentStockDto_mod.setcWhCode(hwxxDto.getCkdm());
										CurrentStockDto had_currentStock = currentStockDao.getCurrentStocksByDto(currentStockDto_mod);
										if(had_currentStock!=null) {
											BigDecimal oldiQuantity=new BigDecimal(had_currentStock.getiQuantity());//调拨前数量
											BigDecimal subiQuantity=new BigDecimal(hwxxDtos_t.getU8rksl());//要调拨的数量
											BigDecimal newiQuantity=oldiQuantity.add(subiQuantity);//调拨前数量-要调拨的数量
											newiQuantity=newiQuantity.setScale(2, RoundingMode.HALF_UP);//四舍五入至2位小数
											had_currentStock.setiQuantity(newiQuantity.toString());
											HwxxDto hwxxDto_mod = new HwxxDto();
											hwxxDto_mod.setHwid(hwxxDto.getDrhwid());
											hwxxDto_mod.setKcglid(String.valueOf(had_currentStock.getAutoID()));
											hwxxlist_t.add(hwxxDto_mod);
											modcurrentStockList.add(had_currentStock);
										}else {
											for(CurrentStockDto addcurrentStock : addcurrentStockList) {
												if(hwxxDto.getWlbm().equals(addcurrentStock.getcInvCode()) && hwxxDto.getScph().equals(addcurrentStock.getcBatch()) && hwxxDto.getDhckdm().equals(addcurrentStock.getcWhCode())) {
													addcurrentStock.setcWhCode(hwxxDto.getCkdm());//新的库位
													addcurrentStock.setiQuantity(hwxxDtos_t.getU8rksl());//调拨数量
													addcurrentStock.setiExpiratDateCalcu("0");
													t_addcurrentStockList.add(addcurrentStock);
												}
											}
										}
										break;
									}
								}else {
									throw new BusinessException("msg","货物生产批号为空！");
								}
							}
						}
					}
					//存入库类型记账数据
					List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos_in = new ArrayList<>();
					//存出库类型记账数据
					List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos_out = new ArrayList<>();
					for (HwxxDto hwxxDto : hwxxs) {
						String transvouchsId="";
						//保存调拨单明细
						TransVouchsDto transVouchsDto = new TransVouchsDto();
						transVouchsDto.setcTVCode(transVouchDto.getcTVCode());//调拨单号,这里采取入库单号
						transVouchsDto.setcInvCode(hwxxDto.getWlbm());//物料编码
						transVouchsDto.setiTVQuantity(hwxxDto.getSl());//数量
//					transVouchsDto.setiTVACost(hwxxDto.getHsdj());//含税单价
						transVouchsDto.setcTVBatch(hwxxDto.getScph());//批号
						transVouchsDto.setdDisDate(hwxxDto.getYxq());//失效日期
						transVouchsDto.setCinposcode(hwxxDto.getKwbhdm());//调入库位
						transVouchsDto.setCoutposcode(hwxxDto.getCskwdm());//调出库位
						transVouchsDto.setdMadeDate(hwxxDto.getScrq()); //生产日期
						if (("0").equals(hwxxDto.getBzqflg())){
							transVouchsDto.setcMassUnit("2");
							transVouchsDto.setiMassDate(hwxxDto.getBzq());//保质期
						}else{
							transVouchsDto.setcMassUnit("1");
							transVouchsDto.setiMassDate("99");//保质期
						}
						if(StringUtil.isBlank(hwxxDto.getScrq())) {
							transVouchsDto.setcMassUnit("0");
							transVouchsDto.setiMassDate(null);//保质期
						}
						if(uA_IdentityDto_t!=null) {
							if(StringUtil.isNotBlank(uA_IdentityDto_t.getiChildId())) {
								int id = Integer.parseInt(uA_IdentityDto_t.getiChildId());
								uA_IdentityDto_t.setiChildId(String.valueOf(id +1));
								transVouchsDto.setAutoID((id + 1000000001));
								transvouchsId=String.valueOf(id + 1000000001);
							}
						}else {
							int id = Integer.parseInt(uA_IdentityDto.getiChildId());
							uA_IdentityDto.setiChildId(String.valueOf(id+1));
							transVouchsDto.setAutoID((id + 1000000001));
							transvouchsId=String.valueOf(id + 1000000001);
						}
						transVouchsDto.setID(transVouchDto.getID());//关联调拨单主表ID
						hwxxDto.setDbmxglid(transvouchsId);//调拨单明细主键ID，AutoID
						transVouchList.add(transVouchsDto);

						//处理调拨入库明细,R8
						RdRecordsDto in_rdRecordsDto = new RdRecordsDto();
						if(rk_uA_IdentityDto_t!=null) {
							if(StringUtil.isNotBlank(rk_uA_IdentityDto_t.getiChildId())) {
								int id_s = Integer.parseInt(rk_uA_IdentityDto_t.getiChildId());
								rk_uA_IdentityDto_t.setiChildId(String.valueOf(id_s+1)); //存入副表最大值
								in_rdRecordsDto.setAutoID(id_s + 1000000001);
							}
						}else {
							if(StringUtil.isNotBlank(rk_uA_IdentityDto.getiChildId())) {
								int id_s = Integer.parseInt(rk_uA_IdentityDto.getiChildId());
								rk_uA_IdentityDto.setiChildId(String.valueOf(id_s+1)); //存入副表最大值
								in_rdRecordsDto.setAutoID(id_s + 1000000001);
							}
						}
						hwxxDto.setQtrkmxid(String.valueOf(in_rdRecordsDto.getAutoID()));//存放其他入库明细ID
						in_rdRecordsDto.setID(inrdRecordDto.getID()); // 关联id
						in_rdRecordsDto.setcInvCode(hwxxDto.getWlbm()); // 物料编码
						in_rdRecordsDto.setiQuantity(hwxxDto.getSl()); // 数量
						in_rdRecordsDto.setiTrIds(transvouchsId);//调拨单明细的AutoID
						in_rdRecordsDto.setcBatch(hwxxDto.getScph()); //生产批号
						in_rdRecordsDto.setcItem_class(hwxxDto.getXmdldm()); //项目大类代码
						in_rdRecordsDto.setcItemCode(hwxxDto.getXmbmdm()); //项目编码代码
						in_rdRecordsDto.setcItemCName(hwxxDto.getXmbmmc()); //项目编码名称
						if (("0").equals(hwxxDto.getBzqflg())){
							in_rdRecordsDto.setcMassUnit("2");
							in_rdRecordsDto.setiMassDate(hwxxDto.getBzq()); //有效期数
						}else{
							in_rdRecordsDto.setcMassUnit("1");
							in_rdRecordsDto.setiMassDate("99"); //有效期数
						}
						if(StringUtil.isBlank(hwxxDto.getScrq())) {
							in_rdRecordsDto.setcMassUnit("0");
							in_rdRecordsDto.setiMassDate(null); //有效期数
						}
						in_rdRecordsDto.setdVDate(hwxxDto.getYxq());//失效日期
						in_rdRecordsDto.setiPOsID(hwxxDto.getU8mxid());//合同明细U8 ID
						in_rdRecordsDto.setcName(hwxxDto.getXmbmmc());//项目编码名称
						in_rdRecordsDto.setcItemCName(hwxxDto.getXmdlmc());//项目大类名称
						in_rdRecordsDto.setiNQuantity(hwxxDto.getDhsl());// 数量
						in_rdRecordsDto.setdMadeDate(hwxxDto.getScrq());//生产日期
						in_rdRecordsDto.setChVencode(hwxxDto.getGysdm());//供应商代码
						in_rdRecordsDto.setCbsysbarcode("||st08|"+inrdRecordDto.getcCode()+"|");
						in_rdRecordsDto.setcDefine29(hwxxDto.getYchh()); //货号
						in_rdRecordsDto.setcDefine23(hwxxDto.getGysmc());//供应商名称
						in_rdRecordsDto.setcDefine22(hwxxDto.getZsh());//追溯号
						in_rdRecordsDto.setcPosition(hwxxDto.getKwbhdm());//库位
						if(StringUtil.isNotBlank(hwxxDto.getKwbhdm())) {
							in_rdRecordsDto.setIposflag("1");
						}
						inRdRecordsDtos.add(in_rdRecordsDto);
						//组装记账记录数据
						IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto_in = new IA_ST_UnAccountVouchDto();
						iA_ST_UnAccountVouchDto_in.setIDUN(String.valueOf(in_rdRecordsDto.getID()));
						iA_ST_UnAccountVouchDto_in.setIDSUN(String.valueOf(in_rdRecordsDto.getAutoID()));
						iA_ST_UnAccountVouchDto_in.setcVouTypeUN("08");
						iA_ST_UnAccountVouchDto_in.setcBUstypeUN("调拨入库");
						iA_ST_UnAccountVouchDtos_in.add(iA_ST_UnAccountVouchDto_in);
						//处理调拨出库明细，R9
						RdRecordsDto out_rdRecordsDto = new RdRecordsDto();
						if(rk_uA_IdentityDto_t!=null) {
							if(StringUtil.isNotBlank(rk_uA_IdentityDto_t.getiChildId())) {
								int id_s = Integer.parseInt(rk_uA_IdentityDto_t.getiChildId());
								rk_uA_IdentityDto_t.setiChildId(String.valueOf(id_s+1)); //存入副表最大值
								out_rdRecordsDto.setAutoID(id_s + 1000000001);
							}
						}else {
							if(StringUtil.isNotBlank(rk_uA_IdentityDto.getiChildId())) {
								int id_s = Integer.parseInt(rk_uA_IdentityDto.getiChildId());
								rk_uA_IdentityDto.setiChildId(String.valueOf(id_s+1)); //存入副表最大值
								out_rdRecordsDto.setAutoID(id_s + 1000000001);
							}
						}
						hwxxDto.setQtckmxid(String.valueOf(out_rdRecordsDto.getAutoID()));//存放其他出库明细ID
						out_rdRecordsDto.setID(outrdRecordDto.getID()); // 关联id
						out_rdRecordsDto.setcInvCode(hwxxDto.getWlbm()); // 物料编码
						out_rdRecordsDto.setiQuantity(hwxxDto.getSl()); // 数量
						out_rdRecordsDto.setcBatch(hwxxDto.getScph()); //生产批号
						out_rdRecordsDto.setcItem_class(hwxxDto.getXmdldm()); //项目大类代码
						out_rdRecordsDto.setcItemCode(hwxxDto.getXmbmdm()); //项目编码代码
						out_rdRecordsDto.setcItemCName(hwxxDto.getXmbmmc()); //项目编码名称
						if (("0").equals(hwxxDto.getBzqflg())){
							out_rdRecordsDto.setcMassUnit("2");
							out_rdRecordsDto.setiMassDate(hwxxDto.getBzq()); //有效期数
						}else{
							out_rdRecordsDto.setcMassUnit("1");
							out_rdRecordsDto.setiMassDate("99"); //有效期数
						}
						if(StringUtil.isBlank(hwxxDto.getScrq())) {
							out_rdRecordsDto.setcMassUnit("0");
							out_rdRecordsDto.setiMassDate(null); //有效期数
						}
						out_rdRecordsDto.setdVDate(hwxxDto.getYxq());//失效日期
						out_rdRecordsDto.setcName(hwxxDto.getXmbmmc());//项目编码名称
						out_rdRecordsDto.setcItemCName(hwxxDto.getXmdlmc());//项目大类名称
						out_rdRecordsDto.setiNQuantity(hwxxDto.getSl());// 数量
						out_rdRecordsDto.setdMadeDate(hwxxDto.getScrq());//生产日期
						out_rdRecordsDto.setChVencode(hwxxDto.getGysdm());//供应商代码
						out_rdRecordsDto.setiTrIds(transvouchsId);//调拨单明细的AutoID
						out_rdRecordsDto.setcPOID(hwxxDto.getHtnbbh()); // 关联合同单
						out_rdRecordsDto.setcPosition(hwxxDto.getCskwdm());//库位
						if(StringUtil.isNotBlank(hwxxDto.getCskwdm())) {
							out_rdRecordsDto.setIposflag("1");
						}
						out_rdRecordsDto.setCbsysbarcode("||st09|"+outrdRecordDto.getcCode()+"|");
						out_rdRecordsDto.setcDefine29(hwxxDto.getYchh()); //货号
						out_rdRecordsDto.setcDefine23(hwxxDto.getGysmc());//供应商名称
						out_rdRecordsDto.setcDefine22(hwxxDto.getZsh());//追溯号
						outRdRecordsDtos.add(out_rdRecordsDto);

						//组装记账记录数据
						IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto_out = new IA_ST_UnAccountVouchDto();
						iA_ST_UnAccountVouchDto_out.setIDUN(String.valueOf(out_rdRecordsDto.getID()));
						iA_ST_UnAccountVouchDto_out.setIDSUN(String.valueOf(out_rdRecordsDto.getAutoID()));
						iA_ST_UnAccountVouchDto_out.setcVouTypeUN("09");
						iA_ST_UnAccountVouchDto_out.setcBUstypeUN("调拨出库");
						iA_ST_UnAccountVouchDtos_out.add(iA_ST_UnAccountVouchDto_out);

						//处理货位调整表
						InvPositionDto invPositionDto_in = new InvPositionDto();
						invPositionDto_in.setRdsID(String.valueOf(in_rdRecordsDto.getAutoID()));
						invPositionDto_in.setRDID(String.valueOf(inrdRecordDto.getID()));
						invPositionDto_in.setcWhCode(rkglDto_t.getCkdm());
						invPositionDto_in.setcPosCode(hwxxDto.getKwbhdm());
						invPositionDto_in.setcInvCode(hwxxDto.getWlbm());
						invPositionDto_in.setcBatch(StringUtil.isNotBlank(hwxxDto.getScph())?hwxxDto.getScph():"");
						invPositionDto_in.setdVDate(hwxxDto.getYxq());
						invPositionDto_in.setiQuantity(hwxxDto.getSl());
						invPositionDto_in.setcHandler(rkglDto_t.getZsxm());
						SimpleDateFormat sdf_t = new SimpleDateFormat("yyyy-MM-dd");
						invPositionDto_in.setdDate(sdf_t.format(date));
						invPositionDto_in.setbRdFlag("1");
						invPositionDto_in.setdMadeDate(hwxxDto.getScrq());
						if (("0").equals(hwxxDto.getBzqflg())) {
							invPositionDto_in.setcMassUnit("2");
							invPositionDto_in.setiMassDate(hwxxDto.getBzq()); // 有效期数
						} else {
							invPositionDto_in.setcMassUnit("1");
							invPositionDto_in.setiMassDate("99"); // 有效期数
						}
						if(StringUtil.isBlank(hwxxDto.getScph())) {
							invPositionDto_in.setcMassUnit("0");
							invPositionDto_in.setiMassDate(null); // 有效期数
						}
						invPositionDto_in.setCvouchtype("08");
						Date date_t = new Date();
						invPositionDto_in.setdVouchDate(sdf.format(date_t));
						invPositionDtos.add(invPositionDto_in);

						InvPositionDto invPositionDto_out = new InvPositionDto();
						invPositionDto_out.setRdsID(String.valueOf(out_rdRecordsDto.getAutoID()));
						invPositionDto_out.setRDID(String.valueOf(outrdRecordDto.getID()));
						invPositionDto_out.setcWhCode(dhxxDto.getCkdm());
						invPositionDto_out.setcPosCode(hwxxDto.getCskwdm());
						invPositionDto_out.setcInvCode(hwxxDto.getWlbm());
						invPositionDto_out.setcBatch(StringUtil.isNotBlank(hwxxDto.getScph())?hwxxDto.getScph():"");
						invPositionDto_out.setdVDate(hwxxDto.getYxq());
						invPositionDto_out.setiQuantity(hwxxDto.getSl());
						invPositionDto_out.setcHandler(rkglDto_t.getZsxm());
						invPositionDto_out.setdDate(sdf_t.format(date));
						invPositionDto_out.setbRdFlag("0");
						invPositionDto_out.setdMadeDate(hwxxDto.getScrq());
						if (("0").equals(hwxxDto.getBzqflg())) {
							invPositionDto_out.setcMassUnit("2");
							invPositionDto_out.setiMassDate(hwxxDto.getBzq()); // 有效期数
						} else {
							invPositionDto_out.setcMassUnit("1");
							invPositionDto_out.setiMassDate("99"); // 有效期数
						}
						if(StringUtil.isBlank(hwxxDto.getScph())) {
							invPositionDto_out.setcMassUnit("0");
							invPositionDto_out.setiMassDate(null); // 有效期数
						}
						invPositionDto_out.setCvouchtype("09");
						Date date_t_o = new Date();
						invPositionDto_out.setdVouchDate(sdf.format(date_t_o));
						invPositionDtos.add(invPositionDto_out);
					}

					map.put("hwxxs",hwxxs);//用于更新其他出入库明细关联ID
					if (!CollectionUtils.isEmpty(hwxxDtos_ts)) {
						for (HwxxDto hwxxDto_ts : hwxxDtos_ts) {
							for (HwxxDto hwxx_s : hwxxs) {
								if (StringUtil.isNotBlank(hwxxDto_ts.getScph()) && StringUtil.isNotBlank(hwxx_s.getScph())) {
									if (hwxxDto_ts.getWlid().equals(hwxx_s.getWlid()) && hwxxDto_ts.getScph().equals(hwxx_s.getScph())) {
										//处理货位货物总数数据
										//1.获取出库信息，更新出库数量
										InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
										invPositionSumDto.setcWhCode(dhxxDto.getCkdm());
										invPositionSumDto.setcPosCode(hwxx_s.getCskwdm());
										invPositionSumDto.setcInvCode(hwxx_s.getWlbm());
										invPositionSumDto.setcBatch(hwxx_s.getScph());
										//判断是否存在  仓库，物料编码，库位，生产批号一样的数据
										InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
										if (invPositionSumDto_t != null) {
											//更新货物货物数量
											invPositionSumDto_t.setJsbj("1"); //计算标记,相减
											invPositionSumDto_t.setiQuantity(hwxxDto_ts.getU8rksl());
											invPositionSumDtos_mod.add(invPositionSumDto_t);
										} else {
											throw new BusinessException("msg", "未找到调出库位货物信息！");
										}

										//2.找到入库库位
										invPositionSumDto.setcWhCode(rkglDto_t.getCkdm());
										invPositionSumDto.setcPosCode(hwxx_s.getKwbhdm());
										//判断是否存在  仓库，物料编码，库位，生产批号一样的数据
										InvPositionSumDto invPositionSumDto_l = invPositionSumDao.getDto(invPositionSumDto);
										if (invPositionSumDto_l != null) {
											//存在更新
											invPositionSumDto_l.setJsbj("0"); //计算标记,相加
											invPositionSumDto_l.setiQuantity(hwxxDto_ts.getU8rksl());
											invPositionSumDtos_mod.add(invPositionSumDto_l);
										} else {
											//不存在新增
											invPositionSumDto.setiQuantity(hwxxDto_ts.getU8rksl());
											invPositionSumDto.setInum(null);
											if (("0").equals(hwxx_s.getBzqflg())) {
												invPositionSumDto.setcMassUnit("2");
												invPositionSumDto.setiMassDate(hwxx_s.getBzq()); // 保质期
											} else {
												invPositionSumDto.setcMassUnit("1");
												invPositionSumDto.setiMassDate("99"); // 保质期
											}
											invPositionSumDto.setdMadeDate(hwxx_s.getScrq());
											invPositionSumDto.setdVDate(hwxx_s.getYxq());
											invPositionSumDtos_add.add(invPositionSumDto);
										}
										break;
									}
								}
								if (StringUtil.isBlank(hwxxDto_ts.getScph()) && StringUtil.isBlank(hwxx_s.getScph())) {
									if (hwxxDto_ts.getWlid().equals(hwxx_s.getWlid())) {
										//处理货位货物总数数据
										//1.获取出库信息，更新出库数量
										InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
										invPositionSumDto.setcWhCode(dhxxDto.getCkdm());
										invPositionSumDto.setcPosCode(hwxx_s.getCskwdm());
										invPositionSumDto.setcInvCode(hwxx_s.getWlbm());
										invPositionSumDto.setcBatch("");
										//判断是否存在  仓库，物料编码，库位，生产批号一样的数据
										InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
										if (invPositionSumDto_t != null) {
											//更新货物货物数量
											invPositionSumDto_t.setJsbj("1"); //计算标记,相减
											invPositionSumDto_t.setiQuantity(hwxxDto_ts.getU8rksl());
											invPositionSumDtos_mod.add(invPositionSumDto_t);
										} else {
											throw new BusinessException("msg", "未找到调出库位货物信息！");
										}

										//2.找到入库库位
										invPositionSumDto.setcWhCode(rkglDto_t.getCkdm());
										invPositionSumDto.setcPosCode(hwxx_s.getKwbhdm());
										//判断是否存在  仓库，物料编码，库位，生产批号一样的数据
										InvPositionSumDto invPositionSumDto_l = invPositionSumDao.getDto(invPositionSumDto);
										if (invPositionSumDto_l != null) {
											//存在更新
											invPositionSumDto_l.setJsbj("0"); //计算标记,相加
											invPositionSumDto_l.setiQuantity(hwxxDto_ts.getU8rksl());
											invPositionSumDtos_mod.add(invPositionSumDto_l);
										} else {
											//不存在新增
											invPositionSumDto.setiQuantity(hwxxDto_ts.getU8rksl());
											invPositionSumDto.setInum(null);
											invPositionSumDto.setcMassUnit("0");
											invPositionSumDto.setiMassDate(null); // 保质期
											invPositionSumDto.setdMadeDate(null);
											invPositionSumDto.setdVDate(null);
											invPositionSumDtos_add.add(invPositionSumDto);
										}
										break;
									}
								}

							}
						}
					}
					// 更新最大值表
					boolean result_ui_t = false;
					//判断是新增还是修改
					if(rk_uA_IdentityDto_t!=null) {
						//更新
						result_ui_t = uA_IdentityDao.update(rk_uA_IdentityDto_t)>0;
					}
					if(!result_ui_t) {
						//新增
						result_ui_t = uA_IdentityDao.insert(rk_uA_IdentityDto)>0;
					}
					if(!result_ui_t) {
						throw new BusinessException("msg", "最大id值更新失败!");
					}

					//新增U8调拨单明细数据
					//若List过大进行截断,10个明细为一组进行添加
					boolean insertTransVouchs;
					List<List<TransVouchsDto>> transVouchLists = Lists.partition(transVouchList, 10);
					for (List<TransVouchsDto> vouchList : transVouchLists) {
						insertTransVouchs = transVouchsDao.insertTransVouchs(vouchList);
						if(!insertTransVouchs)
							throw new BusinessException("msg","添加调拨单明细数据失败！");
					}
					//更新主表id最大值
					// 更新最大值表
					boolean result_ui = false;
					//判断是新增还是修改
					if(uA_IdentityDto_t!=null) {
						result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
					}
					if(!result_ui) {
						result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
					}
					if(!result_ui) {
						throw new BusinessException("msg", "最大id值更新失败!");
					}

					//修改仓库库位信息和数量
					//若List过大进行截断,10个明细为一组进行添加
					List<List<CurrentStockDto>> modcurrentStockLists = Lists.partition(modcurrentStockList, 10);
					boolean modcurrentStocks;
					for (List<CurrentStockDto> stockList : modcurrentStockLists) {
						modcurrentStocks = currentStockDao.updateKwAndSl(stockList);
						if(!modcurrentStocks)
							throw new BusinessException("msg","更新U8仓库库位和数量信息失败！");
					}
					//添加调拨后的仓库库位信息
					if(!CollectionUtils.isEmpty(t_addcurrentStockList)) {
						//若List过大进行截断,10个明细为一组进行添加
						List<String> addcurrentStocks;
						if(t_addcurrentStockList.size()>10){
							int length=(int)Math.ceil((double)t_addcurrentStockList.size()/10);//向上取整
							for(int i = 0; i<length; i++){
								List<CurrentStockDto> list=new ArrayList<>();
								int t_length;
								if(i!=length-1){
									t_length=(i+1)*10;
								}else{
									t_length=t_addcurrentStockList.size();
								}
								for(int j=i*10;j<t_length;j++){
									list.add(t_addcurrentStockList.get(j));
								}
								addcurrentStocks = currentStockDao.addCurrentStocks(list);
								if(addcurrentStocks==null || addcurrentStocks.size()<=0)
									throw new BusinessException("msg","添加U8调拨库存信息失败！");
								for(int j=0;j<list.size();j++){
									t_addcurrentStockList.get(i*10+j).setAutoID(Integer.parseInt(addcurrentStocks.get(j)));
								}
							}
						}else{
							addcurrentStocks = currentStockDao.addCurrentStocks(t_addcurrentStockList);
							if(addcurrentStocks==null || addcurrentStocks.size()<=0)
								throw new BusinessException("msg","添加U8调拨库存信息失败！");
							for(int i=0;i<t_addcurrentStockList.size();i++){
								t_addcurrentStockList.get(i).setAutoID(Integer.parseInt(addcurrentStocks.get(i)));
							}
						}
					}
					map.put("t_addcurrentStockList", t_addcurrentStockList);
					//添加调拨入库数据
					//若List过大进行截断,10个明细为一组进行添加
					boolean result_in;
					if(inRdRecordsDtos.size()>10){
						int length=(int)Math.ceil((double)inRdRecordsDtos.size()/10);//向上取整
						for(int i = 0; i<length; i++){
							List<RdRecordsDto> list=new ArrayList<>();
							int t_length;
							if(i!=length-1){
								t_length=(i+1)*10;
							}else{
								t_length=inRdRecordsDtos.size();
							}
							for(int j=i*10;j<t_length;j++){
								list.add(inRdRecordsDtos.get(j));
							}
							result_in = rdRecordsDao.insertRd8(list);
							if(!result_in)
								throw new BusinessException("msg","添加U8调拨入库信息失败！");
						}
					}else{
						result_in = rdRecordsDao.insertRd8(inRdRecordsDtos);
						if(!result_in)
							throw new BusinessException("msg","添加U8调拨入库信息失败！");
					}

					//添加调拨出库数据
					//若List过大进行截断,10个明细为一组进行添加
					boolean result_out;
					if(outRdRecordsDtos.size()>10){
						int length=(int)Math.ceil((double)outRdRecordsDtos.size()/10);//向上取整
						for(int i = 0; i<length; i++){
							List<RdRecordsDto> list=new ArrayList<>();
							int t_length;
							if(i!=length-1){
								t_length=(i+1)*10;
							}else{
								t_length=outRdRecordsDtos.size();
							}
							for(int j=i*10;j<t_length;j++){
								list.add(outRdRecordsDtos.get(j));
							}
							result_out = rdRecordsDao.insertRd9(list);
							if(!result_out)
								throw new BusinessException("msg","添加U8调拨出库信息失败！");
						}
					}else{
						result_out = rdRecordsDao.insertRd9(outRdRecordsDtos);
						if(!result_out)
							throw new BusinessException("msg","添加U8调拨出库信息失败！");
					}

					//添加U8其他出库相关表数据
					//若List过大进行截断,10个明细为一组进行添加
					int result_sub;
					if(outRdRecordsDtos.size()>10){
						int length=(int)Math.ceil((double)outRdRecordsDtos.size()/10);//向上取整
						for(int i = 0; i<length; i++){
							List<RdRecordsDto> list=new ArrayList<>();
							int t_length;
							if(i!=length-1){
								t_length=(i+1)*10;
							}else{
								t_length=outRdRecordsDtos.size();
							}
							for(int j=i*10;j<t_length;j++){
								list.add(outRdRecordsDtos.get(j));
							}
							result_sub = rdRecordsDao.insertRds09Sub(list);
							if(result_sub<1) {
								throw new BusinessException("msg", "新增rdrecords09sub失败！");
							}
						}
					}else{
						result_sub = rdRecordsDao.insertRds09Sub(outRdRecordsDtos);
						if(result_sub<1) {
							throw new BusinessException("msg", "新增rdrecords09sub失败！");
						}
					}

					//若List过大进行截断,10个明细为一组进行添加
					int result_hw;
					if(invPositionDtos.size()>10){
						int length=(int)Math.ceil((double)invPositionDtos.size()/10);//向上取整
						for(int i = 0; i<length; i++){
							List<InvPositionDto> list=new ArrayList<>();
							int t_length;
							if(i!=length-1){
								t_length=(i+1)*10;
							}else{
								t_length=invPositionDtos.size();
							}
							for(int j=i*10;j<t_length;j++){
								list.add(invPositionDtos.get(j));
							}
							result_hw = invPositionDao.insertDtos(list);
							if(result_hw<1) {
								throw new BusinessException("msg", "更新U8货位调整失败！");
							}
						}
					}else{
						result_hw = invPositionDao.insertDtos(invPositionDtos);
						if(result_hw<1) {
							throw new BusinessException("msg", "更新U8货位调整失败！");
						}
					}

					if(invPositionSumDtos_add.size()>0) {
						//若List过大进行截断,10个明细为一组进行添加
						int result_add;
						if(invPositionSumDtos_add.size()>10){
							int length=(int)Math.ceil((double)invPositionSumDtos_add.size()/10);//向上取整
							for(int i = 0; i<length; i++){
								List<InvPositionSumDto> list=new ArrayList<>();
								int t_length;
								if(i!=length-1){
									t_length=(i+1)*10;
								}else{
									t_length=invPositionSumDtos_add.size();
								}
								for(int j=i*10;j<t_length;j++){
									list.add(invPositionSumDtos_add.get(j));
								}
								result_add = invPositionSumDao.insertDtos(list);
								if(result_add<1) {
									throw new BusinessException("msg", "新增U8货位总数失败！");
								}
							}
						}else{
							result_add = invPositionSumDao.insertDtos(invPositionSumDtos_add);
							if(result_add<1) {
								throw new BusinessException("msg", "新增U8货位总数失败！");
							}
						}
					}

					if(invPositionSumDtos_mod.size()>0) {
						//若List过大进行截断,10个明细为一组进行添加
						int result_mod;
						if(invPositionSumDtos_mod.size()>10){
							int length=(int)Math.ceil((double)invPositionSumDtos_mod.size()/10);//向上取整
							for(int i = 0; i<length; i++){
								List<InvPositionSumDto> list=new ArrayList<>();
								int t_length;
								if(i!=length-1){
									t_length=(i+1)*10;
								}else{
									t_length=invPositionSumDtos_mod.size();
								}
								for(int j=i*10;j<t_length;j++){
									list.add(invPositionSumDtos_mod.get(j));
								}
								result_mod = invPositionSumDao.updateDtos(list);
								if(result_mod<1) {
									throw new BusinessException("msg", "更新U8货位总数失败！");
								}
							}
						}else{
							result_mod = invPositionSumDao.updateDtos(invPositionSumDtos_mod);
							if(result_mod<1) {
								throw new BusinessException("msg", "更新U8货位总数失败！");
							}
						}
					}

					//更新记账记录
					//若List过大进行截断,10个明细为一组进行添加
					boolean result_av;
					if(iA_ST_UnAccountVouchDtos_in.size()>10){
						int length=(int)Math.ceil((double)iA_ST_UnAccountVouchDtos_in.size()/10);//向上取整
						for(int i = 0; i<length; i++){
							List<IA_ST_UnAccountVouchDto> list=new ArrayList<>();
							int t_length;
							if(i!=length-1){
								t_length=(i+1)*10;
							}else{
								t_length=iA_ST_UnAccountVouchDtos_in.size();
							}
							for(int j=i*10;j<t_length;j++){
								list.add(iA_ST_UnAccountVouchDtos_in.get(j));
							}
							result_av = addAccountVs(list,"08");
							if(!result_av)
								throw new BusinessException("msg", "记账记录新增失败！");
						}
					}else{
						result_av = addAccountVs(iA_ST_UnAccountVouchDtos_in,"08");
						if(!result_av)
							throw new BusinessException("msg", "记账记录新增失败！");
					}
					//若List过大进行截断,10个明细为一组进行添加
					if(iA_ST_UnAccountVouchDtos_out.size()>10){
						int length=(int)Math.ceil((double)iA_ST_UnAccountVouchDtos_out.size()/10);//向上取整
						for(int i = 0; i<length; i++){
							List<IA_ST_UnAccountVouchDto> list=new ArrayList<>();
							int t_length;
							if(i!=length-1){
								t_length=(i+1)*10;
							}else{
								t_length=iA_ST_UnAccountVouchDtos_out.size();
							}
							for(int j=i*10;j<t_length;j++){
								list.add(iA_ST_UnAccountVouchDtos_out.get(j));
							}
							result_av = addAccountVs(list,"09");
							if(!result_av)
								throw new BusinessException("msg", "记账记录新增失败！");
						}
					}else{
						result_av = addAccountVs(iA_ST_UnAccountVouchDtos_out,"09");
						if(!result_av)
							throw new BusinessException("msg", "记账记录新增失败！");
					}
				}else {
					List<HwxxDto> hwxx = new ArrayList<>();
					map.put("hwxxlist_t",hwxx);
				}
			}
			return map;
		}
	}

	/**
	 * 到货新增U8入库数据
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8RkData(DhxxDto dhxxDto,List<DhxxDto> dhxxDtos,boolean flag) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		// U8生成入库单
		List<HwxxDto> hwxxs = hwxxDao.getListByDhid(dhxxDto.getDhid());
		//List<DhxxDto> dhxxDtos = dhxxService.getDtoList(dhxxDto);

		RdRecordDto rdRecordDto = new RdRecordDto();

		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String prefix = sdf_d.format(date);
		VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
		voucherHistoryDto.setcSeed(prefix);
		voucherHistoryDto.setCardNumber("24");
		//获取最大流水号
		VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
		if(voucherHistoryDto_t!=null) {
			String serial = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
			rdRecordDto.setcCode(prefix+(serial.length()>1?serial:"0"+serial)); // 到货单号
			rdRecordDto.setPrefix(prefix);
			rdRecordDto.setSflxbj("1");
			List<RdRecordDto> rdRecordDtos_t = rdRecordDao.getDtoList(rdRecordDto);
			int ccode_new=0;
	        if (rdRecordDtos_t != null && rdRecordDtos_t.size() > 0) {
	            List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(rdRecordDto);
	            ccode_new = Integer.parseInt(rdRecordDto.getcCode()) + 1;
	            for (RdRecordDto rdRecordDto_c : rdList) {
					if(ccode_new-Integer.parseInt(rdRecordDto_c.getcCode())==0) {
						ccode_new = ccode_new+1;
					}else {
						break;
					}
				}
	        }
	        if(ccode_new!=0) {
	        	rdRecordDto.setcCode(Integer.toString(ccode_new)); // 到货单号
	        	voucherHistoryDto_t.setcNumber(Integer.parseInt(Integer.toString(ccode_new).substring(8))+""); //去除多余0
	        }else {
	        	voucherHistoryDto_t.setcNumber(serial);
	        }
			//更新最大单号值		
			int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
			if(result_ccode<1)
				throw new BusinessException("msg", "更新U8最大单号失败！");
		}else {
			voucherHistoryDto.setiRdFlagSeed("1");
			voucherHistoryDto.setcContent("日期");
			voucherHistoryDto.setcContentRule("日");
			voucherHistoryDto.setbEmpty("0");
			voucherHistoryDto.setcNumber("1");
			rdRecordDto.setcCode(prefix+"01");
			//单号最大表增加数据
			int result_ccode = voucherHistoryDao.insert(voucherHistoryDto);
			if(result_ccode<1)
				throw new BusinessException("msg", "新增U8最大单号失败！");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); // 存入账套
		uA_IdentityDto.setcVouchType("rd"); // 存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); // 获取主键最大值
		if(uA_IdentityDto_t!=null) {
			if (StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				uA_IdentityDto_t.setiFatherId(String.valueOf(id + 1));
				rdRecordDto.setID(id + 1000000001);
			}
		}else {
			rdRecordDto.setID(1000000001);
			uA_IdentityDto.setiFatherId("1");
		}
		if ("2".equals(dhxxDtos.get(0).getHtlx())) {
			rdRecordDto.setcSource("委外订单");
			rdRecordDto.setVT_ID("27");//模板编号27
			rdRecordDto.setIpurorderid(dhxxDtos.get(0).getU8omid()); // 合同关联U8id
			rdRecordDto.setcRdCode(null); // 入库类型代码
			rdRecordDto.setcDepCode(null); // 机构代码
			rdRecordDto.setcPTCode(null);
			rdRecordDto.setiExchRate(dhxxDtos.get(0).getHl());//汇率
			rdRecordDto.setbCredit("0");
			rdRecordDto.setcMemo(StringUtil.isNotBlank(dhxxDtos.get(0).getBz())?(dhxxDtos.get(0).getBz().length()>255?dhxxDtos.get(0).getBz().substring(0,255):dhxxDtos.get(0).getBz()):null);//备注
			rdRecordDto.setdVeriDate(sdf_d.format(date));//审核日期
			rdRecordDto.setDnverifytime(sdf.format(date));//审核时间
		}else {
			rdRecordDto.setcSource("采购订单");
			rdRecordDto.setIpurorderid(dhxxDtos.get(0).getU8poid()); // 合同关联U8id
			rdRecordDto.setcRdCode("1");
			rdRecordDto.setcDepCode(dhxxDtos.get(0).getJgdm()); // 机构代码
			rdRecordDto.setcPTCode(dhxxDtos.get(0).getRklxdm());// 入库类型代码
			rdRecordDto.setiExchRate("1");//汇率
		}
		rdRecordDto.setcMaker(dhxxDtos.get(0).getQgsqr()); // 请购申请人
		rdRecordDto.setcDefine1(dhxxDtos.get(0).getJlbh()); // 请购记录编号
		rdRecordDto.setDnmaketime(sdf.format(date));
		rdRecordDto.setcVouchType("01");
		rdRecordDto.setCsysbarcode("||st01|" + rdRecordDto.getcCode()); // "||st01|"+入库单
		rdRecordDto.setcWhCode(dhxxDtos.get(0).getCkdm()); // 仓库代码
		rdRecordDto.setiTaxRate(dhxxDtos.get(0).getHtmxsl()); // 税率
		rdRecordDto.setcExch_Name(dhxxDtos.get(0).getHtbz()); // 币种
		rdRecordDto.setcBusType(dhxxDtos.get(0).getCglxmc()); // 采购类型)
		rdRecordDto.setcHandler(dhxxDto.getZsxm()); // 审核人
		rdRecordDto.setdDate(dhxxDtos.get(0).getDhrq()); // 入库日期
		rdRecordDto.setcVenCode(dhxxDtos.get(0).getGysdm()); // 供应商代码
		rdRecordDto.setcOrderCode(dhxxDtos.get(0).getHtnbbh()); // 订单编号

		int result = rdRecordDao.insert(rdRecordDto);
		if (result < 1) {
			throw new BusinessException("msg", "入库数据存入U8失败！");
		}

		// 存入关联id
		int rdRecordId = rdRecordDto.getID();
		dhxxDto.setGlid(Integer.toString(rdRecordId));
		map.put("dhxxDto", dhxxDto);

		// 存货物信息
		List<HwxxDto> hwxxDtoList = new ArrayList<>();

		// 存批量更新的U8合同明细信息
		List<PO_PodetailsDto> pO_PodetailsDtos = new ArrayList<>();
		ArrayList<OM_MODetailsDto> om_moDetailsDtos = new ArrayList<>();//htlx=2时维护
		// 存批量更新U8入库明细数据
		List<RdRecordsDto> rdRecordsDtoList = new ArrayList<>();
		// 存物料编码，用于查库存
		StringBuilder ids = new StringBuilder();
		int irowno = 1;
		int id_s = 0;
		if(uA_IdentityDto_t!=null) {
			id_s = Integer.parseInt(uA_IdentityDto_t.getiChildId());
		}
		//存货位调整信息
		List<InvPositionDto> invPositionDtos = new ArrayList<>();
		//存记账记录数据
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos = new ArrayList<>();
		for (HwxxDto hwxxDto : hwxxs) {
			//只有是ABC类的物料录入U8
			if("1".equals(hwxxDto.getLbbj()) || flag) {
				ids.append(",").append(hwxxDto.getWlbm());
				RdRecordsDto rdRecordsDto = new RdRecordsDto();
				if (StringUtil.isNotBlank(String.valueOf(id_s))) {
					rdRecordsDto.setAutoID(id_s + 1000000001);
					id_s = id_s + 1;
				} else {
					throw new BusinessException("msg", "生成U8明细主键id失败！");
				}



				// 算出入库数量
				double sl = Double.parseDouble(hwxxDto.getDhsl())
						- Double.parseDouble(StringUtil.isNotBlank(hwxxDto.getCythsl()) ? hwxxDto.getCythsl() : "0");
				rdRecordsDto.setID(rdRecordDto.getID()); // 关联id
				rdRecordsDto.setcInvCode(hwxxDto.getWlbm()); // 物料编码
				rdRecordsDto.setiQuantity(Double.toString(sl)); // 数量

				// 无税总金额 = 含税总金额 /(1+税率/100)
				BigDecimal hjje = new BigDecimal(sl).multiply(new BigDecimal(StringUtil.isNotBlank(hwxxDto.getHsdj())?hwxxDto.getHsdj():"0")); // 总金额
				hjje=hjje.setScale(2,RoundingMode.HALF_UP);
				BigDecimal suil = new BigDecimal(StringUtil.isNotBlank(hwxxDto.getSuil())?hwxxDto.getSuil():"0").divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
				BigDecimal wszje = hjje.divide(suil.add(new BigDecimal("1")), 2, RoundingMode.HALF_UP);
				rdRecordsDto.setcBatch(hwxxDto.getScph()); // 生产批号
				rdRecordsDto.setcItem_class(StringUtil.isNotBlank(hwxxDto.getXmdldm())?hwxxDto.getXmdldm():""); // 项目大类代码
				rdRecordsDto.setcItemCode(StringUtil.isNotBlank(hwxxDto.getXmdldm())?hwxxDto.getXmbmdm():""); // 项目编码代码
				rdRecordsDto.setcItemCName(StringUtil.isNotBlank(hwxxDto.getXmdldm())?hwxxDto.getXmbmmc():""); // 项目编码名称
				if (("0").equals(hwxxDto.getBzqflg())) {
					rdRecordsDto.setcMassUnit("2");
					rdRecordsDto.setiMassDate(hwxxDto.getBzq()); // 有效期数
				} else {
					rdRecordsDto.setcMassUnit("1");
					rdRecordsDto.setiMassDate("99"); // 有效期数
				}
				rdRecordsDto.setcPosition(hwxxDto.getKwbhdm()); // 库位
				rdRecordsDto.setdVDate(hwxxDto.getYxq());// 失效日期
				rdRecordsDto.setiPOsID(hwxxDto.getU8mxid());// 合同明细U8 ID

				rdRecordsDto.setcName(hwxxDto.getXmbmmc());// 项目编码名称
				rdRecordsDto.setcItemCName(hwxxDto.getXmdlmc());// 项目大类名称
				rdRecordsDto.setiNQuantity(Double.toString(sl));// 数量
				rdRecordsDto.setdMadeDate(hwxxDto.getScrq());// 生产日期
				rdRecordsDto.setChVencode(hwxxDto.getGysdm());// 供应商代码

				// 计算税额
				BigDecimal se = hjje.subtract(wszje);
				rdRecordsDto.setcPOID(hwxxDto.getHtnbbh()); // 关联合同单
				rdRecordsDto.setIrowno(Integer.toString(irowno));
				rdRecordsDto.setCbsysbarcode(rdRecordDto.getCsysbarcode() + "|" + irowno);
				irowno = irowno + 1;
				rdRecordsDto.setiSQuantity("0");
				rdRecordsDto.setiSNum("0");
				rdRecordsDto.setiMoney("0");

				rdRecordsDto.setCbMemo(StringUtil.isNotBlank(hwxxDto.getDhbz())?(hwxxDto.getDhbz().length()>255?hwxxDto.getDhbz().substring(0,255):hwxxDto.getDhbz()):null); //到货备注

				rdRecordsDto.setbTaxCost("1");
                if ("2".equals(hwxxDto.getHtlx())){
                    rdRecordsDto.setiPrice(wszje.toString()); // 原币金额
                    rdRecordsDto.setiAPrice(wszje.toString());// 原币金额
                    rdRecordsDto.setiUnitCost(hwxxDto.getWsdj()); // 原币单价
                    rdRecordsDto.setcDefine22(null);// 追溯号
                    rdRecordsDto.setcDefine23(null);// 供应商名称
                    rdRecordsDto.setfACost(hwxxDto.getWsdj());// 无税单价
                    rdRecordsDto.setcDefine29(null); // 货号
                    rdRecordsDto.setiOriTaxCost(hwxxDto.getHsdj()); // 原币含税单价
                    rdRecordsDto.setiOriCost(hwxxDto.getWsdj()); // 原币单价
                    rdRecordsDto.setiOriMoney(wszje.toString()); // 原币金额
                    rdRecordsDto.setiOriTaxPrice(se.toString()); // 原币税额
                    rdRecordsDto.setIoriSum(hjje.toString());// 原币价税总额
                    rdRecordsDto.setiTaxRate("0"); // 税率
                    rdRecordsDto.setiTaxPrice(se.toString()); // 税额
                    rdRecordsDto.setiSum(hjje.toString()); // 原币价税总额
					rdRecordsDto.setiProductType("0");

                    rdRecordsDto.setiProcessCost(wszje.toString());//原币单价（不含税）
                    if (StringUtil.isNotBlank(Double.toString(sl))&&StringUtil.isNotBlank(hwxxDto.getWsdj())){
                        BigDecimal wsdj = new BigDecimal(hwxxDto.getWsdj());
                        BigDecimal bsl = new BigDecimal(Double.toString(sl));
                        BigDecimal iProcessFee = wsdj.multiply(bsl);
                        String iProcessFeeStr = iProcessFee.setScale(2, RoundingMode.HALF_UP).toString();
                        rdRecordsDto.setiProcessFee(iProcessFeeStr);//原币单价×入库数量
                    }
                }else {
                    rdRecordsDto.setiPrice(wszje.toString()); // 原币金额
                    rdRecordsDto.setiAPrice(wszje.toString());// 原币金额
                    rdRecordsDto.setiUnitCost(hwxxDto.getWsdj()); // 原币单价
                    rdRecordsDto.setcDefine22(hwxxDto.getZsh());// 追溯号
                    rdRecordsDto.setcDefine23(hwxxDto.getGysmc());// 供应商名称
                    rdRecordsDto.setfACost(hwxxDto.getWsdj());// 无税单价
                    rdRecordsDto.setcDefine29(hwxxDto.getYchh()); // 货号

                    rdRecordsDto.setiOriTaxCost(hwxxDto.getHsdj()); // 原币含税单价
                    rdRecordsDto.setiOriCost(hwxxDto.getWsdj()); // 原币单价
                    rdRecordsDto.setiOriMoney(wszje.toString()); // 原币金额
                    rdRecordsDto.setiOriTaxPrice(se.toString()); // 原币税额
                    rdRecordsDto.setIoriSum(hjje.toString());// 原币价税总额
                    rdRecordsDto.setiTaxRate(hwxxDto.getSuil()); // 税率
                    rdRecordsDto.setiTaxPrice(se.toString()); // 税额
                    rdRecordsDto.setiSum(hjje.toString()); // 原币价税总额
                }
				rdRecordsDto.setiOMoDID(hwxxDto.getU8ommxid());//OM_MODetails表 MODetailsID
				rdRecordsDtoList.add(rdRecordsDto);

				//组装记账记录数据
				IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto = new IA_ST_UnAccountVouchDto();
				iA_ST_UnAccountVouchDto.setIDUN(String.valueOf(rdRecordsDto.getID()));
				iA_ST_UnAccountVouchDto.setIDSUN(String.valueOf(rdRecordsDto.getAutoID()));
				iA_ST_UnAccountVouchDto.setcVouTypeUN("01");
				if ("2".equals(dhxxDtos.get(0).getHtlx())){
					iA_ST_UnAccountVouchDto.setcBUstypeUN("委外加工");
				}else {
					iA_ST_UnAccountVouchDto.setcBUstypeUN("采购入库");
				}
				iA_ST_UnAccountVouchDtos.add(iA_ST_UnAccountVouchDto);

				// 存入关联id
				HwxxDto hwxx_list = new HwxxDto();
				int hwglid = rdRecordsDto.getAutoID();
				hwxx_list.setGlid(Integer.toString(hwglid));
				hwxx_list.setU8rkdh(rdRecordDto.getcCode());
				hwxx_list.setHwid(hwxxDto.getHwid());
				hwxx_list.setXgry(dhxxDto.getXgry());
				hwxxDtoList.add(hwxx_list);
				map.put("hwxxDtoList", hwxxDtoList);

				if ("2".equals(hwxxDto.getHtlx())) {
					// 存入要更新的U8的合同明细
					OM_MODetailsDto om_moDetailsDto = new OM_MODetailsDto();
					om_moDetailsDto.setMODetailsID(hwxxDto.getU8ommxid());
					om_moDetailsDto.setiReceivedQTY(sl+"");// 入库数量
					om_moDetailsDto.setiReceivedNum("0.000000");
					om_moDetailsDto.setiReceivedMoney("0.00");
					om_moDetailsDtos.add(om_moDetailsDto);
				}else {
					// 存入要更新的U8的合同明细
					PO_PodetailsDto pO_PodetailsDto = new PO_PodetailsDto();
					pO_PodetailsDto.setID(Integer.parseInt(hwxxDto.getHtglid()));
					pO_PodetailsDto.setiReceivedQTY(Double.toString(sl));// 入库数量
					// 计算入库金额 入库金额 = 无税单价 * 数量
					String wsdj_t;
					if (StringUtil.isNotBlank(hwxxDto.getWsdj())) {
						wsdj_t = hwxxDto.getWsdj(); // 无税单价
					} else {
						BigDecimal suil_t = new BigDecimal(hwxxDto.getSuil()).divide(new BigDecimal(100), 5,
								RoundingMode.HALF_UP);
						wsdj_t = new BigDecimal(hwxxDto.getHsdj())
								.divide(suil_t.add(new BigDecimal(1)), 5, RoundingMode.HALF_UP).toString();
					}
					BigDecimal wsdj = new BigDecimal(wsdj_t);
					BigDecimal sl_t = new BigDecimal(Double.toString(sl)); // 入库数量
					BigDecimal rkje = wsdj.multiply(sl_t);
					rkje = rkje.setScale(2, RoundingMode.HALF_UP);// 四舍五入保留两位小数
					pO_PodetailsDto.setiReceivedMoney(rkje.toString());// 入库金额
					pO_PodetailsDto.setiReceivedNum("0");
					pO_PodetailsDtos.add(pO_PodetailsDto);
				}
				//处理货位调整表
				InvPositionDto invPositionDto = new InvPositionDto();
				invPositionDto.setRdsID(String.valueOf(rdRecordsDto.getAutoID()));
				invPositionDto.setRDID(String.valueOf(rdRecordDto.getID()));
				invPositionDto.setcWhCode(dhxxDtos.get(0).getCkdm());
				invPositionDto.setcPosCode(hwxxDto.getKwbhdm());
				invPositionDto.setcInvCode(hwxxDto.getWlbm());
				invPositionDto.setcBatch(hwxxDto.getScph());
				invPositionDto.setdVDate(hwxxDto.getYxq());
				invPositionDto.setiQuantity(Double.toString(sl));
				invPositionDto.setcHandler(dhxxDto.getZsxm());
				invPositionDto.setdDate(dhxxDtos.get(0).getDhrq());
				invPositionDto.setbRdFlag("1");
				invPositionDto.setdMadeDate(hwxxDto.getScrq());
				if (("0").equals(hwxxDto.getBzqflg())) {
					invPositionDto.setcMassUnit("2");
					invPositionDto.setiMassDate(hwxxDto.getBzq()); // 有效期数
				} else {
					invPositionDto.setcMassUnit("1");
					invPositionDto.setiMassDate("99"); // 有效期数
				}
				invPositionDto.setCvouchtype("01");
				Date date_t = new Date();
				invPositionDto.setdVouchDate(sdf.format(date_t));
				invPositionDtos.add(invPositionDto);
			}
		}

		// 更新U8入库明细表
		//若List过大进行截断,10个明细为一组进行添加
		int result_rds;
		if(rdRecordsDtoList.size()>10) {
			int length = (int)Math.ceil((double)rdRecordsDtoList.size() / 10);//向上取整
			for (int i = 0; i < length; i++) {
				List<RdRecordsDto> list = new ArrayList<>();
				int t_length;
				if (i != length - 1) {
					t_length = (i + 1) * 10;
				} else {
					t_length = rdRecordsDtoList.size();
				}
				for (int j = i * 10; j < t_length; j++) {
					list.add(rdRecordsDtoList.get(j));
				}
				result_rds = rdRecordsDao.insertRds(list);
				if (result_rds < 1)
					throw new BusinessException("msg", "更新U8入库明细表失败！");
			}
		}else{
			result_rds = rdRecordsDao.insertRds(rdRecordsDtoList);
			if (result_rds < 1)
				throw new BusinessException("msg", "更新U8入库明细表失败！");
		}

		// 更新最大值表
		boolean result_ui = false;
		//判断是新增还是修改
		if(uA_IdentityDto_t!=null) {
			//更新
			uA_IdentityDto_t.setiChildId(String.valueOf(id_s));
			result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
		}
		if(!result_ui) {
			uA_IdentityDto.setiChildId(String.valueOf(id_s));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}


		// 更新U8合同明细入库信息
		//若List过大进行截断,10个明细为一组进行添加
		int result_pO_Pode =0;
		if(pO_PodetailsDtos.size()>10) {
			int length = (int)Math.ceil((double)pO_PodetailsDtos.size() / 10);//向上取整
			for (int i = 0; i < length; i++) {
				List<PO_PodetailsDto> list = new ArrayList<>();
				int t_length;
				if (i != length - 1) {
					t_length = (i + 1) * 10;
				} else {
					t_length = pO_PodetailsDtos.size();
				}
				for (int j = i * 10; j < t_length; j++) {
					list.add(pO_PodetailsDtos.get(j));
				}
				result_pO_Pode = pO_PodetailsDao.updateRkxx(list);
			}
		}else{
			result_pO_Pode = pO_PodetailsDao.updateRkxx(pO_PodetailsDtos);
		}

		// 更新U8合同明细入库信息
		//若List过大进行截断,10个明细为一组进行添加
		int result_om =0;
		if(om_moDetailsDtos.size()>10) {
			int length = (int)Math.ceil((double)om_moDetailsDtos.size() / 10);//向上取整
			for (int i = 0; i < length; i++) {
				List<OM_MODetailsDto> list = new ArrayList<>();
				int t_length;
				if (i != length - 1) {
					t_length = (i + 1) * 10;
				} else {
					t_length = om_moDetailsDtos.size();
				}
				for (int j = i * 10; j < t_length; j++) {
					list.add(om_moDetailsDtos.get(j));
				}
				result_om = om_moDetailsDao.updateRkxx(list);
			}
		}else{
			result_om = om_moDetailsDao.updateRkxx(om_moDetailsDtos);
		}

		if (result_om < 1 && result_pO_Pode < 1){
			throw new BusinessException("msg", "更新U8合同入库信息失败！");
		}



		//若List过大进行截断,10个明细为一组进行添加
		int result_hw;
		if(invPositionDtos.size()>10) {
			int length = (int)Math.ceil((double)invPositionDtos.size() / 10);//向上取整
			for (int i = 0; i < length; i++) {
				List<InvPositionDto> list = new ArrayList<>();
				int t_length;
				if (i != length - 1) {
					t_length = (i + 1) * 10;
				} else {
					t_length = invPositionDtos.size();
				}
				for (int j = i * 10; j < t_length; j++) {
					list.add(invPositionDtos.get(j));
				}
				result_hw = invPositionDao.insertDtos(list);
				if (result_hw < 1)
					throw new BusinessException("msg", "新增U8货位调整失败！");
			}
		}else{
			result_hw = invPositionDao.insertDtos(invPositionDtos);
			if (result_hw < 1)
				throw new BusinessException("msg", "新增U8货位调整失败！");
		}

		//若List过大进行截断,10个明细为一组进行添加
		boolean result_av;
		if(iA_ST_UnAccountVouchDtos.size()>10) {
			int length = (int)Math.ceil((double)iA_ST_UnAccountVouchDtos.size() / 10);//向上取整
			for (int i = 0; i < length; i++) {
				List<IA_ST_UnAccountVouchDto> list = new ArrayList<>();
				int t_length;
				if (i != length - 1) {
					t_length = (i + 1) * 10;
				} else {
					t_length = iA_ST_UnAccountVouchDtos.size();
				}
				for (int j = i * 10; j < t_length; j++) {
					list.add(iA_ST_UnAccountVouchDtos.get(j));
				}
				result_av = addAccountVs(list,"01");
				if(!result_av)
					throw new BusinessException("msg", "记账记录新增失败！");
			}
		}else{
			result_av = addAccountVs(iA_ST_UnAccountVouchDtos,"01");
			if(!result_av)
				throw new BusinessException("msg", "记账记录新增失败！");
		}

		// 根据生产批号物料分组
		List<HwxxDto> hwxxList = hwxxService.queryByDhid(dhxxDto.getDhid());

		HwxxDto scphHwxx = new HwxxDto();
		scphHwxx.setDhid(dhxxDto.getDhid());
		//根据生产批号查询货物信息
		List<HwxxDto> scphList = hwxxService.queryBycBatch(scphHwxx);
		//新增U8数据
		//若List过大进行截断,10个明细为一组进行添加
		boolean result_bp;
		if(scphList.size()>10) {
			int length = (int)Math.ceil((double)scphList.size() / 10);//向上取整
			for (int i = 0; i < length; i++) {
				List<HwxxDto> list = new ArrayList<>();
				int t_length;
				if (i != length - 1) {
					t_length = (i + 1) * 10;
				} else {
					t_length = scphList.size();
				}
				for (int j = i * 10; j < t_length; j++) {
					list.add(scphList.get(j));
				}
				result_bp = addBatchPs(list);
				if(!result_bp)
					throw new BusinessException("msg", "新增U8数据（AA_BatchProperty）失败！");
			}
		}else{
			result_bp = addBatchPs(scphList);
			if(!result_bp)
				throw new BusinessException("msg", "新增U8数据（AA_BatchProperty）失败！");
		}
		
		// 获取U8已经存在得库存
		ids = new StringBuilder(ids.substring(1));
		CurrentStockDto currentStockDto_t = new CurrentStockDto();
		currentStockDto_t.setIds(ids.toString());
		List<CurrentStockDto> currentStockDtos_t = currentStockDao.queryBycInvCode(currentStockDto_t);
		// 存要更新得货物id,更新库存关联id
		List<HwxxDto> hwxxDto_hs = new ArrayList<>();
		// 存入批量更新得U8库存数据
		List<CurrentStockDto> currentStockDtos = new ArrayList<>();
		//存货位货物总数信息
		List<InvPositionSumDto> invPositionSumDtos_add = new ArrayList<>();
		List<InvPositionSumDto> invPositionSumDtos_mod = new ArrayList<>();
		for (HwxxDto hwxxDto_list : hwxxList) {
			for (HwxxDto hwxx_hwxxs : hwxxs) {
				//只有是ABC类的物料录入U8
				if("1".equals(hwxx_hwxxs.getLbbj()) || flag) {
					if (hwxxDto_list.getScph().equals(hwxx_hwxxs.getScph())
							&& hwxxDto_list.getWlid().equals(hwxx_hwxxs.getWlid())) {
						// 判断U8是否存在该物料
						if (currentStockDtos_t.size() > 0) {
							boolean modflg = true;
							for (CurrentStockDto current_t : currentStockDtos_t) {
								if (dhxxDtos.get(0).getCkdm().equals(current_t.getcWhCode())
										&& hwxx_hwxxs.getWlbm().equals(current_t.getcInvCode())
										&& hwxx_hwxxs.getScph().equals(current_t.getcBatch())) {
									// 如果仓库，物料编码和生产批号都相同，就修改库存数量
									CurrentStockDto currentStockDto_cu = new CurrentStockDto();
									double sl = Double
											.parseDouble(StringUtils.isNotBlank(hwxxDto_list.getU8rksl())
													? hwxxDto_list.getU8rksl()
													: "0")
											+ Double.parseDouble(StringUtils.isNotBlank(current_t.getiQuantity())
													? current_t.getiQuantity()
													: "0");
									currentStockDto_cu.setiQuantity(Double.toString(sl)); // 库存数量
									currentStockDto_cu.setAutoID(current_t.getAutoID()); // 存入主键id
									currentStockDto_cu.setcBatch(current_t.getcBatch());
									currentStockDto_cu.setcWhCode(current_t.getcWhCode());
									currentStockDto_cu.setcInvCode(current_t.getcInvCode());
									currentStockDtos.add(currentStockDto_cu);
									
									// 存入关联id
									HwxxDto hwxx_hs = new HwxxDto();
									hwxx_hs.setWlid(hwxx_hwxxs.getWlid());
									hwxx_hs.setDhid(hwxx_hwxxs.getDhid());
									hwxx_hs.setScph(hwxx_hwxxs.getScph());
									hwxx_hs.setKcglid(String.valueOf(currentStockDto_cu.getAutoID())); // 存入关联id
									hwxxDto_hs.add(hwxx_hs);
									modflg = false;
									break;
								}
							}
							if (modflg) {
								// 新增得U8库存数据
								CurrentStockDto current_add = new CurrentStockDto();
								current_add.setcInvCode(hwxx_hwxxs.getWlbm()); // 物料编码
								current_add.setcWhCode(dhxxDtos.get(0).getCkdm());// 仓库编码
								boolean itemidflg = true;
								for (CurrentStockDto current_t : currentStockDtos_t) {
									if (hwxx_hwxxs.getWlbm().equals(current_t.getcInvCode())) {
										// 如果物料编码一样，追溯号不一样，ItemId字段相同
										current_add.setItemId(current_t.getItemId());
										itemidflg = false;
									}
								}

								if (itemidflg) {
									SCM_ItemDto sCM_ItemDto_c  = sCM_ItemDao.getDtoBycInvVode(current_add.getcInvCode());
									if(sCM_ItemDto_c!=null) {
										current_add.setItemId(sCM_ItemDto_c.getId());
									}else {
										// 取SCM_Item表中最大值
										int num_ItemId = sCM_ItemDao.getMaxItemId() + 1;
										current_add.setItemId(Integer.toString(num_ItemId));
										// 存入新增的物料
										SCM_ItemDto sCM_ItemDto = new SCM_ItemDto();
										sCM_ItemDto.setcInvCode(current_add.getcInvCode()); // 物料编码
										int result_scm = sCM_ItemDao.insert(sCM_ItemDto);
										if (result_scm < 1) {
											throw new BusinessException("msg", "sCM_Item新增失败！");
										}
									}								
								}
								current_add.setiQuantity(hwxxDto_list.getU8rksl()); // 现存数量
								current_add.setcBatch(hwxx_hwxxs.getScph()); // 生产批号
								current_add.setdVDate(hwxx_hwxxs.getYxq()); // 失效日期
								current_add.setdMdate(hwxx_hwxxs.getScrq()); // 生产日期
								current_add.setiExpiratDateCalcu("0");
								if (("0").equals(hwxx_hwxxs.getBzqflg())) {
									current_add.setcMassUnit("2"); // 有效单位
									current_add.setiMassDate(hwxx_hwxxs.getBzq()); // 有效期数
								} else {
									current_add.setcMassUnit("1"); // 有效单位
									current_add.setiMassDate("99"); // 有效期数
								}
								int result_add = currentStockDao.insert(current_add);
								if (result_add < 1) {
									throw new BusinessException("msg", "U8库存新增失败！");
								}
								
								// 存入关联id
								HwxxDto hwxx_hs = new HwxxDto();
								hwxx_hs.setWlid(hwxx_hwxxs.getWlid());
								hwxx_hs.setDhid(hwxx_hwxxs.getDhid());
								hwxx_hs.setScph(hwxx_hwxxs.getScph());
								hwxx_hs.setKcglid(String.valueOf(current_add.getAutoID())); // 存入关联id
								hwxxDto_hs.add(hwxx_hs);
							}
						} else {
							// 新增得U8库存数据
							CurrentStockDto current_add_t = new CurrentStockDto();
							current_add_t.setcInvCode(hwxx_hwxxs.getWlbm()); // 物料编码
							current_add_t.setcWhCode(dhxxDtos.get(0).getCkdm());// 仓库编码
							SCM_ItemDto sCM_ItemDto_c  = sCM_ItemDao.getDtoBycInvVode(hwxx_hwxxs.getWlbm());
							if(sCM_ItemDto_c!=null) {
								current_add_t.setItemId(sCM_ItemDto_c.getId());
							}else {
								// 存入新增的物料
								SCM_ItemDto sCM_ItemDto = new SCM_ItemDto();
								sCM_ItemDto.setcInvCode(current_add_t.getcInvCode()); // 物料编码
								int result_scm = sCM_ItemDao.insert(sCM_ItemDto);
								if (result_scm < 1) {
									throw new BusinessException("msg", "sCM_Item新增失败！");
								}
								SCM_ItemDto sCM_ItemDto_t  = sCM_ItemDao.getDtoBycInvVode(hwxx_hwxxs.getWlbm());
								current_add_t.setItemId(sCM_ItemDto_t.getId());
							}	
							current_add_t.setiQuantity(hwxxDto_list.getU8rksl()); // 现存数量
							current_add_t.setcBatch(hwxx_hwxxs.getScph()); // 生产批号
							current_add_t.setdVDate(hwxx_hwxxs.getYxq()); // 失效日期
							current_add_t.setdMdate(hwxx_hwxxs.getScrq()); // 生产日期
							current_add_t.setiExpiratDateCalcu("0");
							if (("0").equals(hwxx_hwxxs.getBzqflg())) {
								current_add_t.setcMassUnit("2"); // 有效单位
								current_add_t.setiMassDate(hwxx_hwxxs.getBzq()); // 有效期数
							} else {
								current_add_t.setcMassUnit("1"); // 有效单位
								current_add_t.setiMassDate("99"); // 有效期数
							}
							int result_add = currentStockDao.insert(current_add_t);
							if (result_add < 1) {
								throw new BusinessException("msg", "U8库存添加失败！");
							}
							
							// 存入关联id
							HwxxDto hwxx_hs = new HwxxDto();
							hwxx_hs.setWlid(hwxx_hwxxs.getWlid());
							hwxx_hs.setDhid(hwxx_hwxxs.getDhid());
							hwxx_hs.setScph(hwxx_hwxxs.getScph());
							hwxx_hs.setKcglid(String.valueOf(current_add_t.getAutoID())); 
							hwxxDto_hs.add(hwxx_hs);
						}
						
						//处理货位货物总数数据
						InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
						invPositionSumDto.setcWhCode(dhxxDtos.get(0).getCkdm());
						invPositionSumDto.setcPosCode(hwxx_hwxxs.getKwbhdm());
						invPositionSumDto.setcInvCode(hwxx_hwxxs.getWlbm());
						invPositionSumDto.setcBatch(hwxx_hwxxs.getScph());
						//判断是否存在  仓库，物料编码，库位，生产批号一样的数据
						InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
						if(invPositionSumDto_t!=null) {
							//存在，更新货物货物数量
							invPositionSumDto_t.setJsbj("0"); //计算标记,相加
							invPositionSumDto_t.setiQuantity(hwxxDto_list.getU8rksl());
							invPositionSumDtos_mod.add(invPositionSumDto_t);
						}else {
							//不存在，做新增
							invPositionSumDto.setiQuantity(hwxxDto_list.getU8rksl());
							invPositionSumDto.setInum("0");
							if (("0").equals(hwxx_hwxxs.getBzqflg())) {
								invPositionSumDto.setcMassUnit("2");
								invPositionSumDto.setiMassDate(hwxx_hwxxs.getBzq()); // 保质期
							} else {
								invPositionSumDto.setcMassUnit("1");
								invPositionSumDto.setiMassDate("99"); // 保质期
							}
							invPositionSumDto.setdMadeDate(hwxx_hwxxs.getScrq());
							invPositionSumDto.setdVDate(hwxx_hwxxs.getYxq());
							invPositionSumDtos_add.add(invPositionSumDto);
						}
						break;
					}
				}
			}
		}
		
		//更新库存
		if(currentStockDtos.size()>0) {
			//若List过大进行截断,10个明细为一组进行添加
			boolean reulu_mod;
			if(currentStockDtos.size()>10) {
				int length = (int)Math.ceil((double)currentStockDtos.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<CurrentStockDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = currentStockDtos.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(currentStockDtos.get(j));
					}
					reulu_mod = currentStockDao.updateKwAndSl(list);
					if(!reulu_mod) {
						throw new BusinessException("msg","库存数量更新失败！");
					}
				}
			}else{
				reulu_mod = currentStockDao.updateKwAndSl(currentStockDtos);
				if(!reulu_mod) {
					throw new BusinessException("msg","库存数量更新失败！");
				}
			}
		}
		//新增货物总数信息
		if(invPositionSumDtos_add.size()>0) {
			//若List过大进行截断,10个明细为一组进行添加
			int result_add;
			if(invPositionSumDtos_add.size()>10) {
				int length = (int)Math.ceil((double)invPositionSumDtos_add.size() / 10);//向上取整   2
				for (int i = 0; i < length; i++) {
					List<InvPositionSumDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = invPositionSumDtos_add.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(invPositionSumDtos_add.get(j));
					}
					result_add = invPositionSumDao.insertDtos(list);
					if(result_add<1) {
						throw new BusinessException("msg", "新增U8货位总数失败！");
					}
				}
			}else{
				result_add = invPositionSumDao.insertDtos(invPositionSumDtos_add);
				if(result_add<1) {
					throw new BusinessException("msg", "新增U8货位总数失败！");
				}
			}
		}
		
		//修改货物总数信息
		if(invPositionSumDtos_mod.size()>0) {
			//若List过大进行截断,10个明细为一组进行添加
			int result_mod;
			if(invPositionSumDtos_mod.size()>10) {
				int length = (int)Math.ceil((double)invPositionSumDtos_mod.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<InvPositionSumDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = invPositionSumDtos_mod.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(invPositionSumDtos_mod.get(j));
					}
					result_mod = invPositionSumDao.updateDtos(list);
					if(result_mod<1) {
						throw new BusinessException("msg", "更新U8货位总数失败！");
					}
				}
			}else{
				result_mod = invPositionSumDao.updateDtos(invPositionSumDtos_mod);
				if(result_mod<1) {
					throw new BusinessException("msg", "更新U8货位总数失败！");
				}
			}
		}
		
		//更新库存关联id
		map.put("hwxxDto_hs", hwxxDto_hs);
		return map;
	}

	
	/**
	 * 检验退回U8做调拨
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8DBData(DhjyDto dhjyDto) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		
		//生成调拨单
		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); //存入账套
		uA_IdentityDto.setcVouchType("tr");	//存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
		TransVouchDto transVouchDto = new TransVouchDto();
		List<DhjyDto> dhjyDtos = dhjyDao.getListByDhjyid(dhjyDto.getDhjyid());
		//生成单号
		VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
		voucherHistoryDto.setCardNumber("0304");
		//获取最大流水号
		VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
		String serialnum = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
		String serial = "0000000000"+ serialnum;
		serial = serial.substring(serial.length()-10);
		transVouchDto.setcTVCode(serial); // 到货单号
		//判断单号是否存在
		int ccode_new=0;
		TransVouchDto transVouchDto_t = transVouchDao.getDtoBycTVCode(transVouchDto);			
		if(transVouchDto_t!=null) {
			List<TransVouchDto> rdList = transVouchDao.getcTVCode(transVouchDto);
			ccode_new = Integer.parseInt(transVouchDto.getcTVCode()) + 1;
			for (TransVouchDto transVouchDto_c : rdList) {
				if(ccode_new-Integer.parseInt(transVouchDto_c.getcTVCode())==0) {
					ccode_new = ccode_new+1;
				}else {
					break;
				}
			}				
		}
		if(ccode_new!=0) {
			transVouchDto.setcTVCode(("000000000"+ccode_new).substring(serial.length()-10,serial.length())); // 调拨单号
        	voucherHistoryDto_t.setcNumber(Integer.toString(ccode_new)); 
        }else {
        	voucherHistoryDto_t.setcNumber(serialnum);
        }    
		//更新最大单号值		
		int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
		if(result_ccode<1)
			throw new BusinessException("msg", "更新U8最大单号失败！");		
		
		transVouchDto.setcOWhCode(dhjyDtos.get(0).getCkdm());//转出仓库
		transVouchDto.setcIWhCode("20");//转入仓库(固定不合格区)
		transVouchDto.setcMaker(dhjyDtos.get(0).getLlrymc());//录入人员名称
		transVouchDto.setcIRdCode("3");
		transVouchDto.setcORdCode("7");
		transVouchDto.setiNetLock("0");
		transVouchDto.setCsysbarcode("||st12|"+transVouchDto.getcTVCode());
		transVouchDto.setcVerifyPerson(dhjyDtos.get(0).getLlrymc());
		if(StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
			int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
			uA_IdentityDto_t.setiFatherId(String.valueOf(id +1));
			transVouchDto.setID(String.valueOf((id + 1000000001)));
			dhjyDto.setBhgglid(String.valueOf((id + 1000000001)));
		}else {
			throw new BusinessException("msg","调拨单主表id生成失败！");
		}
		boolean insertTransVouch=transVouchDao.insertTransVouch(transVouchDto);
		
		if(!insertTransVouch)
			throw new BusinessException("msg","U8添加调拨单失败！");
		
		//更新关联id
		map.put("dhjyDto_d", dhjyDto);
		
		//处理调拨入库主表
		RdRecordDto inrdRecordDto=new RdRecordDto();
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyyMM");
		Date date = new Date();
		String prefix_in = sdf_d.format(date);
		VoucherHistoryDto inVoucherHistoryDto = new VoucherHistoryDto();
		inVoucherHistoryDto.setcSeed(prefix_in);
		inVoucherHistoryDto.setCardNumber("0301");
		//获取最大流水号 210900053
		VoucherHistoryDto inVoucherHistoryDto_t = voucherHistoryDao.getMaxSerial(inVoucherHistoryDto);
		if(inVoucherHistoryDto_t!=null) {
			String serial_in = String.valueOf(Integer.parseInt(inVoucherHistoryDto_t.getcNumber())+1);
			inrdRecordDto.setcCode(prefix_in.substring(2)+"000"+(serial_in.length()>1?serial_in:"0"+serial_in)); // 到货单号
			inrdRecordDto.setPrefix(prefix_in.substring(2));
			inrdRecordDto.setSflxbj("8");
			RdRecordDto rdRecordDtos_t = rdRecordDao.queryByCode(inrdRecordDto);
			int ccode_new_in=0;
	        if (rdRecordDtos_t != null ) {
	            List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(inrdRecordDto);
	            ccode_new_in = Integer.parseInt(inrdRecordDto.getcCode()) + 1;
	            for (RdRecordDto rdRecordDto_c_in : rdList) {
					if(ccode_new_in-Integer.parseInt(rdRecordDto_c_in.getcCode())==0) {
						ccode_new_in = ccode_new_in+1;
					}else {
						break;
					}
				}
	        }
	        if(ccode_new_in!=0) {
	        	inrdRecordDto.setcCode(Integer.toString(ccode_new_in)); // 到货单号
	        	inVoucherHistoryDto_t.setcNumber(Integer.parseInt(Integer.toString(ccode_new_in).substring(4))+""); //去除多余0
	        }else {
	        	inVoucherHistoryDto_t.setcNumber(serial_in);
	        }        
			//更新最大单号值		
			int result_ccode_in = voucherHistoryDao.update(inVoucherHistoryDto_t);
			if(result_ccode_in<1)
				throw new BusinessException("msg", "更新U8最大单号失败！");
		}else {
			inVoucherHistoryDto.setiRdFlagSeed("1");
			inVoucherHistoryDto.setcContent("日期");
			inVoucherHistoryDto.setcContentRule("月");
			inVoucherHistoryDto.setbEmpty("0");
			inVoucherHistoryDto.setcNumber("1");
			inrdRecordDto.setcCode(prefix_in+"00001");
			//单号最大表增加数据
			int result_ccode_in = voucherHistoryDao.insert(inVoucherHistoryDto);
			if(result_ccode_in<1)
				throw new BusinessException("msg", "新增U8最大单号失败！");
		}

		
		
		inrdRecordDto.setcBusCode(transVouchDto.getcTVCode());
		
		UA_IdentityDto rk_uA_IdentityDto = new UA_IdentityDto();
		rk_uA_IdentityDto.setcAcc_Id(accountName); //存入账套
		rk_uA_IdentityDto.setcVouchType("rd");	//存入表标识
		rk_uA_IdentityDto = uA_IdentityDao.getMax(rk_uA_IdentityDto); //获取主键最大值
		if(StringUtil.isNotBlank(rk_uA_IdentityDto.getiFatherId())) {
			int id = Integer.parseInt(rk_uA_IdentityDto.getiFatherId());
			rk_uA_IdentityDto.setiFatherId(String.valueOf(id +1));
			inrdRecordDto.setID(id + 1000000001);
		}else {
			throw new BusinessException("msg","调拨入库主表id生成失败！");
		}
		
		inrdRecordDto.setcSource("调拨");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf_code = new SimpleDateFormat("yyyy-MM-dd");
		inrdRecordDto.setDnmaketime(sdf.format(date));
		inrdRecordDto.setcVouchType("08");
		inrdRecordDto.setCsysbarcode("||st08|"+inrdRecordDto.getcCode());
		inrdRecordDto.setcWhCode("20"); //仓库代码(不合格区代码)
		inrdRecordDto.setcBusType("调拨入库"); // 类型)
		inrdRecordDto.setdDate(sdf_code.format(date)); // 入库日期 
		inrdRecordDto.setcRdCode("3");
		inrdRecordDto.setcMaker(dhjyDtos.get(0).getLlrymc()); //制单人
		boolean result_rd = rdRecordDao.insertRd08(inrdRecordDto);
		if(!result_rd) {
			throw new BusinessException("msg","调拨入库存入U8失败！");
		}
		
		//更新主表id最大值
		int rk_result_ua = uA_IdentityDao.update(rk_uA_IdentityDto);
		if(rk_result_ua<1) {
			throw new BusinessException("msg","更新U8主表id最大值失败！");
		}
		
		//处理调拨出库
		RdRecordDto outrdRecordDto=new RdRecordDto();
		VoucherHistoryDto outVoucherHistoryDto = new VoucherHistoryDto();
		outVoucherHistoryDto.setcSeed(prefix_in);
		outVoucherHistoryDto.setCardNumber("0302");
		//获取最大流水号 210900053
		VoucherHistoryDto outVoucherHistoryDto_t = voucherHistoryDao.getMaxSerial(outVoucherHistoryDto);
		if(outVoucherHistoryDto_t!=null) {
			String serial_out = String.valueOf(Integer.parseInt(outVoucherHistoryDto_t.getcNumber())+1);
			outrdRecordDto.setcCode(prefix_in.substring(2)+"000"+(serial_out.length()>1?serial_out:"0"+serial_out)); // 到货单号
			outrdRecordDto.setPrefix(prefix_in.substring(2));
			outrdRecordDto.setSflxbj("9");
			RdRecordDto outrdRecordDtos_t = rdRecordDao.queryByCode(outrdRecordDto);
			int ccode_new_out=0;
	        if (outrdRecordDtos_t != null ) {
	            List<RdRecordDto> rdList_out = rdRecordDao.getcCodeRd(outrdRecordDto);
	            ccode_new_out = Integer.parseInt(inrdRecordDto.getcCode()) + 1;
	            for (RdRecordDto rdRecordDto_c_out : rdList_out) {
					if(ccode_new_out-Integer.parseInt(rdRecordDto_c_out.getcCode())==0) {
						ccode_new_out = ccode_new_out+1;
					}else {
						break;
					}
				}
	        }
	        if(ccode_new_out!=0) {
	        	outrdRecordDto.setcCode(Integer.toString(ccode_new_out)); // 到货单号
	        	outVoucherHistoryDto_t.setcNumber(Integer.parseInt(Integer.toString(ccode_new_out).substring(4))+""); //去除多余0
	        }else {
	        	outVoucherHistoryDto_t.setcNumber(serial_out);
	        }        
			//更新最大单号值		
			int result_ccode_in = voucherHistoryDao.update(outVoucherHistoryDto_t);
			if(result_ccode_in<1)
				throw new BusinessException("msg", "更新U8最大单号失败！");
		}else {
			outVoucherHistoryDto.setiRdFlagSeed("2");
			outVoucherHistoryDto.setcContent("日期");
			outVoucherHistoryDto.setcContentRule("月");
			outVoucherHistoryDto.setbEmpty("0");
			outVoucherHistoryDto.setcNumber("1");
			outrdRecordDto.setcCode(prefix_in+"00001");
			//单号最大表增加数据
			int result_ccode_in = voucherHistoryDao.insert(outVoucherHistoryDto);
			if(result_ccode_in<1)
				throw new BusinessException("msg", "新增U8最大单号失败！");
		}
		
		outrdRecordDto.setcBusCode(transVouchDto.getcTVCode());
		outrdRecordDto.setcRdCode("7");
		outrdRecordDto.setVT_ID("85");
		
		
		if(StringUtil.isNotBlank(rk_uA_IdentityDto.getiFatherId())) {
			int id = Integer.parseInt(rk_uA_IdentityDto.getiFatherId());
			rk_uA_IdentityDto.setiFatherId(String.valueOf(id +1));
			outrdRecordDto.setID(id + 1000000001);
		}else {
			throw new BusinessException("msg","调拨出库主表id生成失败！");
		}
		
		HwxxDto hwxxDto = new HwxxDto();
		List<String> dhjyids = new ArrayList<>();
		dhjyids.add(dhjyDto.getDhjyid());
		hwxxDto.setDhjyids(dhjyids);
		List<HwxxDto> hwxxList = hwxxService.getListByDhjyid(hwxxDto);
		
		outrdRecordDto.setcSource("调拨");
		outrdRecordDto.setDnmaketime(sdf.format(date));
		outrdRecordDto.setcVouchType("09");
		outrdRecordDto.setCsysbarcode("||st09|"+outrdRecordDto.getcCode());
		outrdRecordDto.setcWhCode(hwxxList.get(0).getCkdm()); //仓库代码
		outrdRecordDto.setcBusType("调拨出库"); // 类型)
		outrdRecordDto.setdDate(sdf_code.format(date)); // 出库日期 
		outrdRecordDto.setcMaker(dhjyDtos.get(0).getLlrymc()); //制单人
		boolean ck_result = rdRecordDao.insertRd09(outrdRecordDto);
		if(!ck_result) {
			throw new BusinessException("msg","入库数据存入U8失败！");
		}
		
		List<TransVouchsDto> transVouchList=new ArrayList<>();
		List<String> autoIds=new ArrayList<>();//存放库存关联id
		List<HwxxDto> hwxxs = hwxxService.getDtoListByJyId(dhjyDto.getDhjyid());		
		for (HwxxDto hwxxDto_db : hwxxs) {
			if(!"0".equals(hwxxDto_db.getZjthsl())) {
				//set库存关联ID
				autoIds.add(hwxxDto_db.getKcglid());				
			}
		}
		List<RdRecordsDto> inRdRecordsDtos=new ArrayList<>();//调拨入库
		List<RdRecordsDto> outRdRecordsDtos=new ArrayList<>();//调拨出库
		List<CurrentStockDto> currentStockDtos=currentStockDao.getCurrentStockByAutoIDs(autoIds);
		if(currentStockDtos==null || currentStockDtos.size()<=0)
			throw new BusinessException("msg","未获取到U8关联库存信息！");
		List<CurrentStockDto> t_addcurrentStockList=new ArrayList<>();//存放需要新增的数据
		List<CurrentStockDto> addcurrentStockList=new ArrayList<>();//需要新增
		List<CurrentStockDto> modcurrentStockList=new ArrayList<>();//需要修改
		for(CurrentStockDto currentStockDto : currentStockDtos) {
			try {
				modcurrentStockList.add((CurrentStockDto)currentStockDto.clone());
				addcurrentStockList.add((CurrentStockDto)currentStockDto.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				throw new BusinessException("CurrentStockDto克隆出错！");
			}
		}
		List<HwxxDto> hwxx_glid = new ArrayList<>();
		
		HwxxDto hwxxDto_t=new HwxxDto();
		hwxxDto_t.setDhjyid(dhjyDto.getDhjyid());
		List<HwxxDto> hwxxDtos_ts=hwxxService.queryByJyid(hwxxDto_t);//查询入库货物信息根据物料和生产批号分组
		//存货位调整表
		List<InvPositionDto> invPositionDtos = new ArrayList<>();
		//先处理修改的
		for (HwxxDto hwxxDto_db : hwxxs) {
			if(Double.parseDouble(StringUtils.isNotBlank(hwxxDto_db.getZjthsl()) ? hwxxDto_db.getZjthsl() : "0.00")>Double.parseDouble("0.00")) {
				for(CurrentStockDto modcurrentStock : modcurrentStockList) {
					if(String.valueOf(hwxxDto_db.getKcglid()).equals(String.valueOf(modcurrentStock.getAutoID()))) {
						BigDecimal oldiQuantity=new BigDecimal(modcurrentStock.getiQuantity());//调拨前数量
						BigDecimal subiQuantity=new BigDecimal(hwxxDto_db.getZjthsl());//要调拨的数量
						BigDecimal newiQuantity=oldiQuantity.subtract(subiQuantity);//调拨前数量-要调拨的数量
						newiQuantity=newiQuantity.setScale(2, RoundingMode.HALF_UP);//四舍五入至2位小数
						modcurrentStock.setiQuantity(newiQuantity.toString());
					}
				}
			}
		}
		//处理拆分的，需要新增的，需要叠加数量的
		if(!CollectionUtils.isEmpty(hwxxDtos_ts)) {
			for(HwxxDto hwxxDtos_t : hwxxDtos_ts) {
				for (HwxxDto hwxxDto_db : hwxxs) {
					if(Double.parseDouble(StringUtils.isNotBlank(hwxxDto_db.getZjthsl()) ? hwxxDto_db.getZjthsl() : "0.00")>Double.parseDouble("0.00")) {
						if (hwxxDtos_t.getScph().equals(hwxxDto_db.getScph())
								&& hwxxDtos_t.getWlid().equals(hwxxDto_db.getWlid())) {
							CurrentStockDto currentStockDto_mod = new CurrentStockDto();
							currentStockDto_mod.setcInvCode(hwxxDto_db.getWlbm());
							currentStockDto_mod.setcBatch(hwxxDto_db.getScph());
							currentStockDto_mod.setcWhCode("20");
							CurrentStockDto had_currentStock = currentStockDao.getCurrentStocksByDto(currentStockDto_mod);
							if(had_currentStock!=null) {
								BigDecimal oldiQuantity=new BigDecimal(had_currentStock.getiQuantity());//调拨前数量
								BigDecimal subiQuantity=new BigDecimal(hwxxDtos_t.getZjthsl());//要调拨的数量
								BigDecimal newiQuantity=oldiQuantity.add(subiQuantity);//已退货数量+要退货数量
								newiQuantity=newiQuantity.setScale(2, RoundingMode.HALF_UP);//四舍五入至2位小数
								had_currentStock.setiQuantity(newiQuantity.toString());
								modcurrentStockList.add(had_currentStock);
							}else {
								for(CurrentStockDto addcurrentStock : addcurrentStockList) {
									if(String.valueOf(hwxxDto_db.getKcglid()).equals(String.valueOf(addcurrentStock.getAutoID()))) {
										addcurrentStock.setcWhCode("20");//新的库位
										addcurrentStock.setiQuantity(hwxxDtos_t.getZjthsl());//质检退回数量
										t_addcurrentStockList.add(addcurrentStock);
									}
								}
							}
							break;
						}
					}
				}
			}
		}
		
		//存入库类型记账数据
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos_in = new ArrayList<>();
		//存出库类型记账数据
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos_out = new ArrayList<>();
		for (HwxxDto hwxxDto_db : hwxxs) {
			if(Double.parseDouble(StringUtil.isNotBlank(hwxxDto_db.getZjthsl())?hwxxDto_db.getZjthsl():"0.00")>Double.parseDouble("0.00")) {
				//保存调拨单明细
				TransVouchsDto transVouchsDto = new TransVouchsDto();
				transVouchsDto.setcTVCode(transVouchDto.getcTVCode());//调拨单号,这里采取入库单号
				transVouchsDto.setcInvCode(hwxxDto_db.getWlbm());//物料编码
				transVouchsDto.setiTVQuantity(hwxxDto_db.getSl());//数量
				transVouchsDto.setcTVBatch(hwxxDto_db.getScph());//批号
				transVouchsDto.setdDisDate(hwxxDto_db.getYxq());//失效日期
				transVouchsDto.setCinposcode("20000");//调入库位
				transVouchsDto.setCoutposcode(hwxxDto_db.getCskwdm());//调出库位
				transVouchsDto.setdMadeDate(sdf.format(date));
				if (("0").equals(hwxxDto_db.getBzqflg())){
					transVouchsDto.setcMassUnit("2");
					transVouchsDto.setiMassDate(hwxxDto_db.getBzq());//保质期
				}else{
					transVouchsDto.setcMassUnit("1");
					transVouchsDto.setiMassDate("99");//保质期
				}
				
				if(StringUtil.isNotBlank(uA_IdentityDto_t.getiChildId())) {
					int id = Integer.parseInt(uA_IdentityDto_t.getiChildId());
					uA_IdentityDto_t.setiChildId(String.valueOf(id +1));
					transVouchsDto.setAutoID((id + 1000000001));
					HwxxDto hw_glid = new HwxxDto();
					hw_glid.setHwid(hwxxDto_db.getHwid());
					hw_glid.setBhgglid(String.valueOf((id + 1000000001)));
					hwxx_glid.add(hw_glid);
				}else {
					throw new BusinessException("调拨单明细id生成失败！");
				}
				
				transVouchsDto.setID(transVouchDto.getID());//关联调拨单主表ID
				transVouchList.add(transVouchsDto);
				
				//处理调拨入库明细,R8
				RdRecordsDto in_rdRecordsDto = new RdRecordsDto();
				if(StringUtil.isNotBlank(rk_uA_IdentityDto.getiChildId())) {
					int id_s = Integer.parseInt(rk_uA_IdentityDto.getiChildId());
					rk_uA_IdentityDto.setiChildId(String.valueOf(id_s+1)); //存入副表最大值
					in_rdRecordsDto.setAutoID(id_s + 1000000001);
				}else {
					throw new BusinessException("调拨入库明细id生成失败！");
				}
				
				in_rdRecordsDto.setID(inrdRecordDto.getID()); // 关联id
				in_rdRecordsDto.setcInvCode(hwxxDto_db.getWlbm()); // 物料编码
				in_rdRecordsDto.setiQuantity(hwxxDto_db.getZjthsl()); // 数量
				in_rdRecordsDto.setcBatch(hwxxDto_db.getScph()); //生产批号
				in_rdRecordsDto.setcItem_class(hwxxDto_db.getXmdldm()); //项目大类代码
				in_rdRecordsDto.setcItemCode(hwxxDto_db.getXmbmdm()); //项目编码代码
				in_rdRecordsDto.setcItemCName(hwxxDto_db.getXmbmmc()); //项目编码名称
				if (("0").equals(hwxxDto_db.getBzqflg())){
					in_rdRecordsDto.setcMassUnit("2");
					in_rdRecordsDto.setiMassDate(hwxxDto_db.getBzq()); //有效期数
				}else{
					in_rdRecordsDto.setcMassUnit("1");
					in_rdRecordsDto.setiMassDate("99"); //有效期数
				}
				in_rdRecordsDto.setiTrIds(String.valueOf(transVouchsDto.getAutoID()));
				in_rdRecordsDto.setdVDate(hwxxDto_db.getYxq());//失效日期
				in_rdRecordsDto.setiPOsID(hwxxDto_db.getU8mxid());//合同明细U8 ID
				in_rdRecordsDto.setfACost(hwxxDto_db.getWsdj());// 无税单价
				in_rdRecordsDto.setcName(hwxxDto_db.getXmbmmc());//项目编码名称
				in_rdRecordsDto.setcItemCName(hwxxDto_db.getXmdlmc());//项目大类名称
				in_rdRecordsDto.setiNQuantity(hwxxDto_db.getZjthsl());// 数量
				in_rdRecordsDto.setdMadeDate(hwxxDto_db.getScrq());//生产日期
				in_rdRecordsDto.setChVencode(hwxxDto_db.getGysdm());//供应商代码
				in_rdRecordsDto.setCbsysbarcode(inrdRecordDto.getCsysbarcode()+"|");
				in_rdRecordsDto.setcDefine29(hwxxDto_db.getYchh()); //货号
				in_rdRecordsDto.setcDefine23(hwxxDto_db.getGysmc());//供应商名称
				in_rdRecordsDto.setcDefine22(hwxxDto_db.getZsh());//追溯号
				in_rdRecordsDto.setcPosition("20000");
				inRdRecordsDtos.add(in_rdRecordsDto);
				
				//组装记账记录数据
				IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto_in = new IA_ST_UnAccountVouchDto();
				iA_ST_UnAccountVouchDto_in.setIDUN(String.valueOf(in_rdRecordsDto.getID()));
				iA_ST_UnAccountVouchDto_in.setIDSUN(String.valueOf(in_rdRecordsDto.getAutoID()));
				iA_ST_UnAccountVouchDto_in.setcVouTypeUN("08");
				iA_ST_UnAccountVouchDto_in.setcBUstypeUN("调拨入库");
				iA_ST_UnAccountVouchDtos_in.add(iA_ST_UnAccountVouchDto_in);
				
				//处理调拨出库明细，R9
				RdRecordsDto out_rdRecordsDto = new RdRecordsDto();
				if(StringUtil.isNotBlank(rk_uA_IdentityDto.getiChildId())) {
					int id_s = Integer.parseInt(rk_uA_IdentityDto.getiChildId());
					rk_uA_IdentityDto.setiChildId(String.valueOf(id_s+1)); //存入副表最大值
					out_rdRecordsDto.setAutoID(id_s + 1000000001);
				}else {
					throw new BusinessException("其他出库明细id生成失败！");
				}
				out_rdRecordsDto.setID(outrdRecordDto.getID()); // 关联id
				out_rdRecordsDto.setcInvCode(hwxxDto_db.getWlbm()); // 物料编码
				out_rdRecordsDto.setiQuantity(hwxxDto_db.getZjthsl()); // 数量
				out_rdRecordsDto.setcBatch(hwxxDto_db.getScph()); //生产批号
				out_rdRecordsDto.setcItem_class(hwxxDto_db.getXmdldm()); //项目大类代码
				out_rdRecordsDto.setcItemCode(hwxxDto_db.getXmbmdm()); //项目编码代码
				out_rdRecordsDto.setcItemCName(hwxxDto_db.getXmbmmc()); //项目编码名称
				if (("0").equals(hwxxDto_db.getBzqflg())){
					out_rdRecordsDto.setcMassUnit("2");
					out_rdRecordsDto.setiMassDate(hwxxDto_db.getBzq()); //有效期数
				}else{
					out_rdRecordsDto.setcMassUnit("1");
					out_rdRecordsDto.setiMassDate("99"); //有效期数
				}
				
				out_rdRecordsDto.setiTrIds(String.valueOf(transVouchsDto.getAutoID()));
				out_rdRecordsDto.setdVDate(hwxxDto_db.getYxq());//失效日期
				out_rdRecordsDto.setcName(hwxxDto_db.getXmbmmc());//项目编码名称
				out_rdRecordsDto.setcItemCName(hwxxDto_db.getXmdlmc());//项目大类名称
				out_rdRecordsDto.setiNQuantity(hwxxDto_db.getZjthsl());// 数量
				out_rdRecordsDto.setdMadeDate(hwxxDto_db.getScrq());//生产日期
				out_rdRecordsDto.setChVencode(hwxxDto_db.getGysdm());//供应商代码
				out_rdRecordsDto.setcPosition(hwxxDto_db.getCskwdm());//库位
				if(StringUtil.isNotBlank(hwxxDto_db.getCskwdm())) {
					out_rdRecordsDto.setIposflag("1");
				}else {
					out_rdRecordsDto.setIposflag(null);
				}
				out_rdRecordsDto.setCbsysbarcode(outrdRecordDto.getCsysbarcode()+"|");
				out_rdRecordsDto.setcDefine29(hwxxDto_db.getYchh()); //货号
				out_rdRecordsDto.setcDefine23(hwxxDto_db.getGysmc());//供应商名称
				out_rdRecordsDto.setcDefine22(hwxxDto_db.getZsh());//追溯号
				outRdRecordsDtos.add(out_rdRecordsDto);	
				
				//组装记账记录数据
				IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto_out = new IA_ST_UnAccountVouchDto();
				iA_ST_UnAccountVouchDto_out.setIDUN(String.valueOf(out_rdRecordsDto.getID()));
				iA_ST_UnAccountVouchDto_out.setIDSUN(String.valueOf(out_rdRecordsDto.getAutoID()));
				iA_ST_UnAccountVouchDto_out.setcVouTypeUN("09");
				iA_ST_UnAccountVouchDto_out.setcBUstypeUN("调拨出库");
				iA_ST_UnAccountVouchDtos_out.add(iA_ST_UnAccountVouchDto_out);
				
				//处理货位调整表
				InvPositionDto invPositionDto_in = new InvPositionDto();
				invPositionDto_in.setRdsID(String.valueOf(in_rdRecordsDto.getAutoID()));
				invPositionDto_in.setRDID(String.valueOf(inrdRecordDto.getID()));
				invPositionDto_in.setcWhCode("20");
				invPositionDto_in.setcPosCode("20000");
				invPositionDto_in.setcInvCode(hwxxDto_db.getWlbm());
				invPositionDto_in.setcBatch(hwxxDto_db.getScph());
				invPositionDto_in.setdVDate(hwxxDto_db.getYxq());
				invPositionDto_in.setiQuantity(hwxxDto_db.getZjthsl());
				invPositionDto_in.setcHandler(dhjyDto.getZsxm());
				SimpleDateFormat sdf_t = new SimpleDateFormat("yyyy-MM-dd");
				invPositionDto_in.setdDate(sdf_t.format(date));
				invPositionDto_in.setbRdFlag("1");
				invPositionDto_in.setdMadeDate(hwxxDto_db.getScrq());
				if (("0").equals(hwxxDto_db.getBzqflg())) {
					invPositionDto_in.setcMassUnit("2");
					invPositionDto_in.setiMassDate(hwxxDto_db.getBzq()); // 有效期数
				} else {
					invPositionDto_in.setcMassUnit("1");
					invPositionDto_in.setiMassDate("99"); // 有效期数
				}
				invPositionDto_in.setCvouchtype("08");
				Date date_t = new Date();
				invPositionDto_in.setdVouchDate(sdf.format(date_t));
				invPositionDtos.add(invPositionDto_in);
				
				InvPositionDto invPositionDto_out = new InvPositionDto();
				invPositionDto_out.setRdsID(String.valueOf(out_rdRecordsDto.getAutoID()));
				invPositionDto_out.setRDID(String.valueOf(outrdRecordDto.getID()));
				invPositionDto_out.setcWhCode(hwxxList.get(0).getCkdm());
				invPositionDto_out.setcPosCode(hwxxDto_db.getCskwdm());
				invPositionDto_out.setcInvCode(hwxxDto_db.getWlbm());
				invPositionDto_out.setcBatch(hwxxDto_db.getScph());
				invPositionDto_out.setdVDate(hwxxDto_db.getYxq());
				invPositionDto_out.setiQuantity(hwxxDto_db.getZjthsl());
				invPositionDto_out.setcHandler(dhjyDto.getZsxm());
				invPositionDto_out.setdDate(sdf_t.format(date));
				invPositionDto_out.setbRdFlag("0");
				invPositionDto_out.setdMadeDate(hwxxDto_db.getScrq());
				if (("0").equals(hwxxDto_db.getBzqflg())) {
					invPositionDto_out.setcMassUnit("2");
					invPositionDto_out.setiMassDate(hwxxDto_db.getBzq()); // 有效期数
				} else {
					invPositionDto_out.setcMassUnit("1");
					invPositionDto_out.setiMassDate("99"); // 有效期数
				}
				invPositionDto_out.setCvouchtype("09");
				Date date_t_o = new Date();
				invPositionDto_out.setdVouchDate(sdf.format(date_t_o));
				invPositionDtos.add(invPositionDto_out);			
			}
		}
		
		//存货位货物数量信息
		List<InvPositionSumDto> invPositionSumDtos_add = new ArrayList<>();
		List<InvPositionSumDto> invPositionSumDtos_mod = new ArrayList<>();
		if(!CollectionUtils.isEmpty(hwxxDtos_ts)) {
			for (HwxxDto hwxx_ts : hwxxDtos_ts) {
				if (Double.parseDouble(StringUtil.isNotBlank(hwxx_ts.getZjthsl()) ? hwxx_ts.getZjthsl() : "0.00") > Double.parseDouble("0.00")) {
					for (HwxxDto hwxx_s : hwxxs) {
						if (hwxx_ts.getWlid().equals(hwxx_s.getWlid()) && hwxx_ts.getScph().equals(hwxx_s.getScph())) {
							//处理货位货物总数数据
							//1.获取出库信息，更新出库数量
							InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
							invPositionSumDto.setcWhCode(hwxxList.get(0).getCkdm());
							invPositionSumDto.setcPosCode(hwxx_s.getCskwdm());
							invPositionSumDto.setcInvCode(hwxx_s.getWlbm());
							invPositionSumDto.setcBatch(hwxx_s.getScph());
							//判断是否存在  仓库，物料编码，库位，生产批号一样的数据
							InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
							if (invPositionSumDto_t != null) {
								//更新货物货物数量
								invPositionSumDto_t.setJsbj("1"); //计算标记,相减
								invPositionSumDto_t.setiQuantity(hwxx_ts.getZjthsl());
								invPositionSumDtos_mod.add(invPositionSumDto_t);
							} else {
								throw new BusinessException("msg", "未找到调出库位货物信息！");
							}

							//2.找到入库库位
							invPositionSumDto.setcWhCode("20");
							invPositionSumDto.setcPosCode("20000");
							//判断是否存在  仓库，物料编码，库位，生产批号一样的数据
							InvPositionSumDto invPositionSumDto_l = invPositionSumDao.getDto(invPositionSumDto);
							if (invPositionSumDto_l != null) {
								//存在更新
								invPositionSumDto_l.setJsbj("0"); //计算标记,相加
								invPositionSumDto_l.setiQuantity(hwxx_ts.getZjthsl());
								invPositionSumDtos_mod.add(invPositionSumDto_l);
							} else {
								//不存在新增
								invPositionSumDto.setiQuantity(hwxx_s.getZjthsl());
								invPositionSumDto.setInum(null);
								if (("0").equals(hwxx_s.getBzqflg())) {
									invPositionSumDto.setcMassUnit("2");
									invPositionSumDto.setiMassDate(hwxx_s.getBzq()); // 保质期
								} else {
									invPositionSumDto.setcMassUnit("1");
									invPositionSumDto.setiMassDate("99"); // 保质期
								}
								invPositionSumDto.setdMadeDate(hwxx_s.getScrq());
								invPositionSumDto.setdVDate(hwxx_s.getYxq());
								invPositionSumDtos_add.add(invPositionSumDto);
							}
							break;
						}
					}
				}
			}
		}
		//更新主表id最大值
		int ck_result_ua = uA_IdentityDao.update(rk_uA_IdentityDto);
		if(ck_result_ua<1) {
			throw new BusinessException("msg","更新U8主表id最大值失败！");
		}
		
		//新增U8调拨单明细数据
		boolean insertTransVouchs = transVouchsDao.insertTransVouchs(transVouchList);
		if(!insertTransVouchs)
			throw new BusinessException("msg","添加调拨单明细数据失败！");
		
		//更新主表id最大值
		int result_ua = uA_IdentityDao.update(uA_IdentityDto_t);
		if(result_ua<1) {
			throw new BusinessException("msg","更新U8主表id最大值失败！");
		}
		
		//修改仓库库位信息和数量
		boolean modcurrentStocks = currentStockDao.updateKwAndSl(modcurrentStockList);
		if(!modcurrentStocks)
			throw new BusinessException("msg","更新U8仓库库位和数量信息失败！");
		map.put("modcurrentStockList",modcurrentStockList);
		
		//添加调拨后的仓库库位信息
		if(!CollectionUtils.isEmpty(t_addcurrentStockList)) {
			List<String> addcurrentStocks = currentStockDao.addCurrentStocks(t_addcurrentStockList);
			if(addcurrentStocks==null || addcurrentStocks.size()<=0)
				throw new BusinessException("msg","添加U8调拨库存信息失败！");
		}
		//添加调拨入库数据
		boolean result_in = rdRecordsDao.insertRd8(inRdRecordsDtos);
		if(!result_in)
			throw new BusinessException("msg","添加U8调拨入库信息失败！");
		
		//添加调拨出库数据
		boolean result_out = rdRecordsDao.insertRd9(outRdRecordsDtos);
		if(!result_out)
			throw new BusinessException("msg","添加U8调拨出库信息失败！");
		
		//添加U8其他出库相关表数据
		int result_sub = rdRecordsDao.insertRds09Sub(outRdRecordsDtos);
		if(result_sub<1) {
			throw new BusinessException("msg", "新增rdrecords09sub失败！");
		}
		
		int result_hw = invPositionDao.insertDtos(invPositionDtos);
		if(result_hw<1) {
			throw new BusinessException("msg", "更新U8货位调整失败！");
		}
		
		if(invPositionSumDtos_add.size()>0) {
			int result_add = invPositionSumDao.insertDtos(invPositionSumDtos_add);
			if(result_add<1) {
				throw new BusinessException("msg", "新增U8货位总数失败！");
			}
		}
		
		if(invPositionSumDtos_mod.size()>0) {
			int result_mod = invPositionSumDao.updateDtos(invPositionSumDtos_mod);
			if(result_mod<1) {
				throw new BusinessException("msg", "更新U8货位总数失败！");
			}
		}
		
		//更新记账记录		
		boolean result_av = addAccountVs(iA_ST_UnAccountVouchDtos_in,"08");
		if(!result_av)
			throw new BusinessException("msg", "记账记录新增失败！");
		result_av = addAccountVs(iA_ST_UnAccountVouchDtos_out,"09");
		if(!result_av)
			throw new BusinessException("msg", "记账记录新增失败！");
		
		map.put("hwxx_glid", hwxx_glid);		
		return map;
	}


	/**
	 * 领料新增U8做领料
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8LlData(LlglDto llglDto) throws BusinessException {
		
		Map<String, Object> map = new HashMap<>();
		//一、新增U8主表
		MaterialAppVouchDto materialAppVouchDto = new MaterialAppVouchDto();
		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); //存入账套
		uA_IdentityDto.setcVouchType("mv");	//存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
		if(uA_IdentityDto_t!=null) {
			if(StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				uA_IdentityDto_t.setiFatherId(String.valueOf(id +1));
				materialAppVouchDto.setID(String.valueOf(id + 1000000001));
			}
		}else {
			uA_IdentityDto.setiFatherId("1");
			materialAppVouchDto.setID("1000000001");
		}
		materialAppVouchDto.setdDate(llglDto.getSqrq()); //申请日期
		VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
		voucherHistoryDto.setCardNumber("0413");
		//获取最大流水号
		VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
		if(voucherHistoryDto_t!=null) {
			String serialnum = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
			String serial = "0000000000"+ serialnum;
			serial = serial.substring(serial.length()-10);
			materialAppVouchDto.setcCode(serial); // 到货单号
			//判断单号是否存在
			int ccode_new=0;
			MaterialAppVouchDto materialAppVouchDtot = materialAppVouchDao.queryByCode(materialAppVouchDto);			
			if(materialAppVouchDtot!=null) {
				List<MaterialAppVouchDto> rdList = materialAppVouchDao.getcCode(materialAppVouchDto);
				ccode_new = Integer.parseInt(materialAppVouchDto.getcCode()) + 1;
				for (MaterialAppVouchDto materialAppVouchDto_c : rdList) {
					if(ccode_new-Integer.parseInt(materialAppVouchDto_c.getcCode())==0) {
						ccode_new = ccode_new+1;
					}else {
						break;
					}
				}				
			}
			if(ccode_new!=0) {
				materialAppVouchDto.setcCode(("000000000"+ccode_new).substring(serial.length()-10,serial.length())); // 到货单号
	        	voucherHistoryDto_t.setcNumber(Integer.toString(ccode_new)); 
	        }else {
	        	voucherHistoryDto_t.setcNumber(serialnum);
	        }    
			//更新最大单号值		
			int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
			if(result_ccode<1)
				throw new BusinessException("msg", "更新U8最大单号失败！");
		}else {
			voucherHistoryDto.setcNumber("1");
			voucherHistoryDto.setbEmpty("0");
			materialAppVouchDto.setcCode("0000000001");
			int result_ccode_in = voucherHistoryDao.insert(voucherHistoryDto);
			if(result_ccode_in<1)
				throw new BusinessException("msg", "新增U8最大单号失败！");
		}
		
		
		materialAppVouchDto.setcRdCode(llglDto.getCkcsdm()); //出库类别参数代码
		materialAppVouchDto.setcDepCode(llglDto.getJgdm()); //机构代码
		materialAppVouchDto.setcMaker(llglDto.getSqrmc()); //制单人
		materialAppVouchDto.setcHandler(llglDto.getZsxm()); //审核人
		materialAppVouchDto.setVT_ID("30718");
		materialAppVouchDto.setcMemo(llglDto.getJlbh());
		SimpleDateFormat sdf_f = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		materialAppVouchDto.setdVeriDate(sdf_f.format(date));
		materialAppVouchDto.setDnmaketime(sdf.format(date));
		Date date_t = new Date();
		materialAppVouchDto.setDnverifytime(sdf.format(date_t));
		materialAppVouchDto.setCsysbarcode("||st64|"+materialAppVouchDto.getcCode());
		int result_mv = materialAppVouchDao.insert(materialAppVouchDto);
		if(result_mv<1)
			throw new BusinessException("msg","U8主表新增失败！");
		
		//存入关联id
		llglDto.setGlid(materialAppVouchDto.getID());
		map.put("llglDto", llglDto);
		
		//二、新增U8明细
		String childid = "0";
		if(uA_IdentityDto_t!=null) {
			childid = uA_IdentityDto_t.getiChildId();
		}		
		int cbsysbarcode = 1;
		HwllmxDto hwllmxDto = new HwllmxDto();
		hwllmxDto.setLlid(llglDto.getLlid());
		List<HwllxxDto> hwllxxDtos = hwllxxDao.getDtoHwllxxListByLlid(llglDto.getLlid());
		List<HwllmxDto> hwllmxDtos = hwllmxService.getDtoByLlid(hwllmxDto);
		List<MaterialAppVouchsDto> materialAppVouchsDtos = new ArrayList<>();
		for (HwllmxDto hwllmxDto_t : hwllmxDtos) {
			for (HwllxxDto hwllxxDto : hwllxxDtos){
				if (hwllxxDto.getHwllid().equals(hwllmxDto_t.getHwllid())){
					MaterialAppVouchsDto materialAppVouchsDto = new MaterialAppVouchsDto();
					int AutoID = Integer.parseInt(childid);
					childid = (String.valueOf(AutoID +1));
					materialAppVouchsDto.setAutoID(String.valueOf(AutoID + 1000000001));
					hwllmxDto_t.setGlid(materialAppVouchsDto.getAutoID());
					materialAppVouchsDto.setID(materialAppVouchDto.getID());
					if (StringUtil.isNotBlank(hwllxxDto.getBz())){
						materialAppVouchsDto.setCbMemo(hwllxxDto.getBz());
					}
					materialAppVouchsDto.setcInvCode(hwllmxDto_t.getWlbm()); //物料编码
					materialAppVouchsDto.setiQuantity(hwllmxDto_t.getYxsl()); //允许数量
					materialAppVouchsDto.setdVDate(StringUtil.isNotBlank(hwllmxDto_t.getYxq())?hwllmxDto_t.getYxq():null); //失效日期
					materialAppVouchsDto.setcItem_class(hwllmxDto_t.getXmdlcsdm()); //项目大类代码
					materialAppVouchsDto.setcItemCode(hwllmxDto_t.getXmbmcsdm()); //项目编码代码
					materialAppVouchsDto.setcName(hwllmxDto_t.getXmbmcsmc()); //项目编码名称
					materialAppVouchsDto.setcItemCName(hwllmxDto_t.getXmdlcsmc()); //项目大类名称
					materialAppVouchsDto.setdMadeDate(StringUtil.isNotBlank(hwllmxDto_t.getScrq())?hwllmxDto_t.getScrq():null); //生产日期
					if (("0").equals(hwllmxDto_t.getBzqflg())){
						materialAppVouchsDto.setcMassUnit("2"); //保质期标记
						materialAppVouchsDto.setiMassDate(hwllmxDto_t.getBzq()); //保质期
					}else{
						materialAppVouchsDto.setcMassUnit("1"); //保质期标记
						materialAppVouchsDto.setiMassDate("99"); //保质期
					}
					if(StringUtil.isBlank(hwllmxDto_t.getScrq())) {
						materialAppVouchsDto.setcMassUnit("0"); //保质期标记
						materialAppVouchsDto.setiMassDate(null); //保质期
					}
					materialAppVouchsDto.setcWhCode(hwllmxDto_t.getCkdm()); //仓库代码
					materialAppVouchsDto.setCbMemo(hwllmxDto_t.getHwllbz()); //备注
					materialAppVouchsDto.setcBatch(StringUtil.isNotBlank(hwllmxDto_t.getScph())?hwllmxDto_t.getScph():null); //生产批号
					materialAppVouchsDto.setIrowno(Integer.toString(cbsysbarcode)); //排序字段
					materialAppVouchsDto.setCbsysbarcode(materialAppVouchDto.getCsysbarcode()+"|"+cbsysbarcode);
					cbsysbarcode = cbsysbarcode + 1;
					materialAppVouchsDtos.add(materialAppVouchsDto);
				}
			}
		}

		if(materialAppVouchsDtos.size()>0) {
			//若List过大进行截断,10个明细为一组进行添加
			int result_mvs;
			if(materialAppVouchsDtos.size()>10) {
				int length = (int)Math.ceil((double)materialAppVouchsDtos.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<MaterialAppVouchsDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = materialAppVouchsDtos.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(materialAppVouchsDtos.get(j));
					}
					result_mvs = materialAppVouchsDao.insertList(list);
					if(result_mvs<1) {
						throw new BusinessException("msg", "U8明细表新增失败！");
					}
				}
			}else{
				result_mvs = materialAppVouchsDao.insertList(materialAppVouchsDtos);
				if(result_mvs<1) {
					throw new BusinessException("msg", "U8明细表新增失败！");
				}
			}
		}
		
		
		
		// 更新最大值表
		boolean result_ui = false;
		//判断是新增还是修改
		if(uA_IdentityDto_t!=null) {
			//更新
			uA_IdentityDto_t.setiChildId(String.valueOf(childid));
			result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
		}
		if(!result_ui) {
			uA_IdentityDto.setiChildId(String.valueOf(childid));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}

		//更新关联id
		map.put("hwllmxDtos", hwllmxDtos);		
		return map;
	}



	/**
	 * 借用U8操作
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8JyData(JcjyglDto jcjyglDto) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		boolean success;
		JcjymxDto jcjymxDto1 = new JcjymxDto();
		jcjymxDto1.setJcjyid(jcjyglDto.getJcjyid());
		List<JcjymxDto> jcjymxDtos = jcjymxService.getDtoList(jcjymxDto1);
		List<JcjymxDto> jcjymxDtoList = jcjymxService.getDtoListGroupByCk(jcjymxDto1);
		String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		List<HwxxDto> hwxxDtos = new ArrayList<>();
		for (JcjymxDto jcjymxDto:jcjymxDtos) {
			HwxxDto hwxxDto1 = new HwxxDto();
			hwxxDto1.setHwid(jcjymxDto.getHwid());
			hwxxDto1.setXgry(jcjyglDto.getXgry());
			hwxxDto1.setYds(String.valueOf(Double.parseDouble(jcjymxDto.getYds()) - Double.parseDouble(jcjymxDto.getJysl())));
			hwxxDto1.setKcl(String.valueOf(Double.parseDouble(jcjymxDto.getKcl()) - Double.parseDouble(jcjymxDto.getJysl())));
			hwxxDtos.add(hwxxDto1);
//			CkhwxxDto ckhwxxDto = new CkhwxxDto();
//			ckhwxxDto.setCkhwid(jcjymxDto.getCkhwid());
//			ckhwxxDto.setKcl(String.valueOf(Double.parseDouble(jcjymxDto.getCkkcl()) - Double.parseDouble(jcjymxDto.getJysl())));
////			ckhwxxDto.setYds(String.valueOf(Double.parseDouble(jcjymxDto.getCkyds()) - Double.parseDouble(jcjymxDto.getJysl())));
//			ckhwxxDtos.add(ckhwxxDto);
		}

		//一、新增U8主表
		HY_DZ_BorrowOutDto hy_dz_borrowOutDto = new HY_DZ_BorrowOutDto();
		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); //存入账套
		uA_IdentityDto.setcVouchType("HYJCGH001");	//存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
		if(uA_IdentityDto_t!=null) {
			if(StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				uA_IdentityDto_t.setiFatherId(String.valueOf(id +1));
				uA_IdentityDto_t.setiChildId(String.valueOf(id +1));
				hy_dz_borrowOutDto.setID(String.valueOf(id + 1000000001));
			}
		}else {
			uA_IdentityDto.setiFatherId("1");
			uA_IdentityDto.setiChildId("1");
			hy_dz_borrowOutDto.setID("1000000001");
		}
		hy_dz_borrowOutDto.setDdate(date); //申请日期
		String jydh;
		VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
		voucherHistoryDto.setCardNumber("HYJCGH001");
		//获取最大流水号
		VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
		if(voucherHistoryDto_t!=null) {
			String serialnum;
			String serial;
			//List<JcjyglDto> jcjyglDtos= new ArrayList<>();
			//int i = 1;

			serialnum = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber()) + 1);
			serial = "0000000000" + serialnum;
			serial = serial.substring(serial.length() - 10);
			jydh = serial;
			voucherHistoryDto_t.setcNumber(jydh);
			//更新最大单号值
			int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
			if(result_ccode<1)
				throw new BusinessException("msg", "更新U8最大单号失败！");
		}else {
			voucherHistoryDto.setcNumber("1");
			voucherHistoryDto.setbEmpty("0");
			jydh = "0000000001";
			voucherHistoryDao.insert(voucherHistoryDto);
		}
		hy_dz_borrowOutDto.setcCODE(jydh); // 到货单号
		jcjyglDto.setU8jydh(jydh);
//		VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
//		voucherHistoryDto.setCardNumber("HYJCGH001");
//		//获取最大流水号
//		VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
//		if(voucherHistoryDto_t!=null) {
//			String serialnum = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
//			String serial = "0000000000"+ serialnum;
//			serial = serial.substring(serial.length()-10,serial.length());
//			hy_dz_borrowOutDto.setcCODE(serial); // 到货单号
			//判断单号是否存在
//			int ccode_new=0;
			HY_DZ_BorrowOutDto hy_dz_borrowOutDto1 = hy_dz_borrowOutDao.queryByCode(hy_dz_borrowOutDto);
			if(hy_dz_borrowOutDto1!=null) {
				throw new BusinessException("msg","该借用单号已存在，请更新借用单号！");
			}
//			if(ccode_new!=0) {
//				hy_dz_borrowOutDto.setcCODE(("000000000"+ccode_new).substring(serial.length()-10,serial.length())); // 到货单号
////				voucherHistoryDto_t.setcNumber(ccode_new.toString());
//			}else {
//				voucherHistoryDto_t.setcNumber(serialnum);
//			}
//			//更新最大单号值
//			int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
//			if(result_ccode<1)
//				throw new BusinessException("msg", "更新U8最大单号失败！");
//		}else {
//			voucherHistoryDto.setcNumber("79");
//			voucherHistoryDto.setbEmpty("0");
//			hy_dz_borrowOutDto.setcCODE("0000000001");
//			int result_ccode_in = voucherHistoryDao.insert(voucherHistoryDto);
//			if(result_ccode_in<1)
//				throw new BusinessException("msg", "新增U8最大单号失败！");
//		}

		jcjyglDto.setGlid(hy_dz_borrowOutDto.getID());
		hy_dz_borrowOutDto.setcType(jcjyglDto.getDwlxmc());
		hy_dz_borrowOutDto.setcCreateType("新增单据");
		hy_dz_borrowOutDto.setbObjectCode(jcjyglDto.getDwdm());
		hy_dz_borrowOutDto.setCdepcode(jcjyglDto.getBmdm());
		hy_dz_borrowOutDto.setiVTID("131061");
		hy_dz_borrowOutDto.setDmDate(jcjyglDto.getLrsj());
		hy_dz_borrowOutDto.setCfreight(jcjyglDto.getSfzfyf());
		hy_dz_borrowOutDto.setcMaker(jcjyglDto.getLrrymc());
		hy_dz_borrowOutDto.setcHandler(jcjyglDto.getLrrymc());
		hy_dz_borrowOutDto.setVoucherType("HYJCGH001");
		hy_dz_borrowOutDto.setdVeriDate(jcjyglDto.getLrsj());
		hy_dz_borrowOutDto.setCsysbarcode("||stjc|"+hy_dz_borrowOutDto.getcCODE());
		hy_dz_borrowOutDto.setCmemo(jcjyglDto.getBz());
		hy_dz_borrowOutDto.setcContactperson(jcjyglDto.getLxr());
		hy_dz_borrowOutDto.setcContactWay(jcjyglDto.getLxfs());
		int result_mv = hy_dz_borrowOutDao.insert(hy_dz_borrowOutDto);
		if(result_mv<1)
			throw new BusinessException("msg","U8主表新增失败！");
		// 更新最大值表
		boolean result_ui = false;
		//判断是新增还是修改
		if(uA_IdentityDto_t!=null) {
			//更新
//			uA_IdentityDto_t.setiChildId(String.valueOf(Integer.parseInt(uA_IdentityDto_t.getiChildId()) + jcjymxDtos.size()));
			result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
		}
		if(!result_ui) {
//			uA_IdentityDto.setiChildId(String.valueOf(jcjymxDtos.size()));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}
		
		//生成明细id
		int lsh = 0;
		UA_IdentityDto uA_IdentityDto1 = new UA_IdentityDto();
		uA_IdentityDto1.setcAcc_Id(accountName); //存入账套
		uA_IdentityDto1.setcVouchType("hy_DZ_BorrowOuts");	//存入表标识
		UA_IdentityDto uA_IdentityDto1_t = uA_IdentityDao.getMax(uA_IdentityDto1); //获取主键最大值
		if(uA_IdentityDto1_t!=null) {
			if(StringUtil.isNotBlank(uA_IdentityDto1_t.getiChildId())) {
				lsh = Integer.parseInt(uA_IdentityDto1_t.getiChildId());
			}
		}else {
			uA_IdentityDto1.setiChildId("0");
			uA_IdentityDto1.setiFatherId("0");
		}

 		int k = 0 ;
		for (JcjymxDto jcjymxDto: jcjymxDtos) {
			k++;
			HY_DZ_BorrowOutsDto hyDzBorrowOutsDto = new HY_DZ_BorrowOutsDto();
			lsh = lsh+1;
			hyDzBorrowOutsDto.setAutoID(String.valueOf(lsh+1000000000));			
			jcjymxDto.setMxglid(hyDzBorrowOutsDto.getAutoID());
			hyDzBorrowOutsDto.setID(hy_dz_borrowOutDto.getID());
			hyDzBorrowOutsDto.setCbatch(jcjymxDto.getScph());
			hyDzBorrowOutsDto.setBackdate(jcjymxDto.getYjghrq());
			hyDzBorrowOutsDto.setCinvcode(jcjymxDto.getWlbm());
			hyDzBorrowOutsDto.setCwhcode(jcjymxDto.getCkdm());
			hyDzBorrowOutsDto.setcPosition(null);
			hyDzBorrowOutsDto.setIquantity(jcjymxDto.getJysl());
			hyDzBorrowOutsDto.setCmemo("预计归还"+DateUtils.getCustomFomratCurrentDate("MMdd"));
			hyDzBorrowOutsDto.setInum("0");
			hyDzBorrowOutsDto.setCbsysbarcode("||stjc|"+hy_dz_borrowOutDto.getcCODE()+"|"+k);
			hyDzBorrowOutsDto.setDmadedate(jcjymxDto.getScrq());
			hyDzBorrowOutsDto.setDvdate(jcjymxDto.getYxq());
//			hyDzBorrowOutsDto.setCmassunit(jcjymxDto.getYxqbz());
//			hyDzBorrowOutsDto.setImassdate(jcjymxDto.getBzq());
			if (("0").equals(jcjymxDto.getYxqbz())){
				hyDzBorrowOutsDto.setCmassunit("2");
				hyDzBorrowOutsDto.setImassdate(jcjymxDto.getBzq()); //有效期数
			}else{
				hyDzBorrowOutsDto.setCmassunit("1");
				hyDzBorrowOutsDto.setImassdate("99"); //有效期数
			}
			if(StringUtil.isBlank(jcjymxDto.getScrq())) {
				hyDzBorrowOutsDto.setCmassunit("0");
				hyDzBorrowOutsDto.setImassdate(null); //有效期数
			}
			hyDzBorrowOutsDto.setiQtyOut(jcjymxDto.getJysl());//借用数量
			hyDzBorrowOutsDto.setiQtyOut2("0");
			result_mv = hy_dz_borrowOutsDao.insert(hyDzBorrowOutsDto);
			if(result_mv<1)
				throw new BusinessException("msg","U8明细新增失败！");
		}
		if(uA_IdentityDto1_t!=null) {
			uA_IdentityDto1_t.setiChildId(String.valueOf(lsh));
			uA_IdentityDto1_t.setiFatherId(String.valueOf(lsh));
			result_ui = uA_IdentityDao.update(uA_IdentityDto1_t)>0;
		}
		if(!result_ui) {
			uA_IdentityDto1.setiChildId(String.valueOf(lsh));
			uA_IdentityDto1.setiFatherId(String.valueOf(lsh));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto1)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}


		//存出库数据
		List<CkglDto> ckglDtos = new ArrayList<>();
		//存出库明细表数据
		List<CkmxDto> ckmxDtos = new ArrayList<>();
		//生成出库单 生成规则: CK-20201022-01 LL-年份日期-流水号 。
		String prefix = "CK" + "-" + date + "-";
		String serial1 = ckglDao.getDjhSerial(prefix);
		int serial_int = Integer.parseInt(serial1)-1;

		for (JcjymxDto jcjymxDto:jcjymxDtoList) {
			CkglDto ckglDto = new CkglDto();
			ckglDto.setCkid(StringUtil.generateUUID()); //出库id
			ckglDto.setBm(jcjyglDto.getBm()); //部门
			ckglDto.setFlr(jcjyglDto.getXgry()); //经手人
			ckglDto.setCk(jcjymxDto.getCkid()); //出库仓库
			CkxxDto ckxxDto = ckxxService.getDtoById(jcjymxDto.getCkid());
			ckglDto.setCkdm(ckxxDto.getCkdm());
			serial_int = serial_int +1;
			String ckdh="00"+ serial_int;
			ckglDto.setCkdh(prefix+ckdh.substring(ckdh.length()-3)); //出库单号
			Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DELIVERY_TYPE});
			List<JcsjDto> jcsjDtos = jclist.get(BasicDataTypeEnum.DELIVERY_TYPE.getCode());
			for (JcsjDto jcsjDto:jcsjDtos) {
				if (StringUtil.isNotBlank(jcsjDto.getSfmr())){
					if ("1".equals(jcsjDto.getSfmr())){
						ckglDto.setCklb(jcsjDto.getCsid()); //出库类别
					}
				}
			}
			ckglDto.setBmdm(jcjyglDto.getBmdm());
			ckglDto.setJcjyid(jcjyglDto.getJcjyid());
			ckglDto.setLrry(jcjyglDto.getXgry()); //录入人员
			ckglDto.setCkrq(date); //出库日期
			ckglDto.setBz(jcjyglDto.getBz());
			ckglDtos.add(ckglDto);

			for (JcjymxDto jcjymxDto3 : jcjymxDtos) {
				if(jcjymxDto3.getCkid().equals(ckglDto.getCk())) {
					//组装出库明细数据
					CkmxDto ckmx = new CkmxDto();
					ckmx.setBzqflg(jcjymxDto3.getYxqbz());
					ckmx.setCkid(ckglDto.getCkid()); //出库id
					ckmx.setCkmxid(StringUtil.generateUUID()); //生成明细id
					ckmx.setHwid(jcjymxDto3.getHwid()); //货物id
					ckmx.setCksl(jcjymxDto3.getJysl()); //出库数量
					ckmx.setKcglid(jcjymxDto3.getKcglid()); //库存关联id
					ckmx.setLrry(jcjyglDto.getXgry()); //录入人员
					ckmx.setJymxid(jcjymxDto3.getJymxid()); //录入人员
					ckmx.setWlbm(jcjymxDto3.getWlbm());
					ckmx.setScph(jcjymxDto3.getScph());
					ckmx.setYxq(jcjymxDto3.getYxq());
					ckmx.setScrq(jcjymxDto3.getScrq());
					ckmx.setBzq(jcjymxDto3.getBzq());
					ckmx.setBz(jcjymxDto3.getBz());
					ckmx.setU8jcid(jcjyglDto.getGlid());
					ckmx.setKwbhcsdm(jcjymxDto3.getKwdm());
					ckmx.setU8jcmxid(jcjymxDto3.getMxglid());
					ckmx.setCkdm(ckglDto.getCkdm());
					ckmx.setXmdlcsdm(jcjymxDto3.getDldm());
					ckmx.setXmdlcsmc(jcjymxDto3.getDlmc());
					ckmx.setXmbmcsdm(jcjymxDto3.getBmdm());
					ckmx.setXmbmcsmc(jcjymxDto3.getBmmc());
					ckmxDtos.add(ckmx);
				}
			}
		}
		prefix = date;
		for (CkglDto ckglDto : ckglDtos) {
			//出库存入U8
			RdRecordDto rdRecordDto = new RdRecordDto();
			VoucherHistoryDto voucherHistoryDtock = new VoucherHistoryDto();
			voucherHistoryDtock.setcSeed(date);
			voucherHistoryDtock.setCardNumber("0302");
			//获取最大流水号
			VoucherHistoryDto voucherHistoryDtock_t = voucherHistoryDao.getMaxSerial(voucherHistoryDtock);
			if(voucherHistoryDtock_t!=null) {
				String serial = String.valueOf(Integer.parseInt(voucherHistoryDtock_t.getcNumber())+1);
				rdRecordDto.setcCode(prefix.substring(2,6)+"000"+(serial.length()>1?serial:"0"+serial)); // 到货单号
				rdRecordDto.setPrefix(prefix.substring(2,6));
				rdRecordDto.setSflxbj("9");
				List<RdRecordDto> rdRecordDtos_t = rdRecordDao.getcCodeRd(rdRecordDto);
				int ccode_new=0;
				if (rdRecordDtos_t != null && rdRecordDtos_t.size() > 0) {
					List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(rdRecordDto);
					ccode_new = Integer.parseInt(rdRecordDto.getcCode()) + 1;
					for (RdRecordDto rdRecordDto_c : rdList) {
						if(ccode_new-Integer.parseInt(rdRecordDto_c.getcCode())==0) {
							ccode_new = ccode_new+1;
						}else {
							break;
						}
					}
				}
				if(ccode_new!=0) {
					rdRecordDto.setcCode(Integer.toString(ccode_new)); // 到货单号
					voucherHistoryDtock_t.setcNumber(Integer.parseInt(Integer.toString(ccode_new).substring(8))+""); //去除多余0
				}else {
					voucherHistoryDtock_t.setcNumber(serial);
				}
				//更新最大单号值
				int result_ccode = voucherHistoryDao.update(voucherHistoryDtock_t);
				if(result_ccode<1)
					throw new BusinessException("msg", "更新U8最大单号失败！");
			}else {
				voucherHistoryDtock.setiRdFlagSeed("2");
				voucherHistoryDtock.setcContent("日期");
				voucherHistoryDtock.setcContentRule("月");
				voucherHistoryDtock.setbEmpty("0");
				voucherHistoryDtock.setcNumber("1");
				rdRecordDto.setcCode(prefix.substring(2,6)+"00001");
				//单号最大表增加数据
				int result_ccode = voucherHistoryDao.insert(voucherHistoryDtock);
				if(result_ccode<1)
					throw new BusinessException("msg", "新增U8最大单号失败！");
			}

			UA_IdentityDto uA_IdentityDtock = new UA_IdentityDto();
			uA_IdentityDtock.setcAcc_Id(accountName); //存入账套
			uA_IdentityDtock.setcVouchType("rd");	//存入表标识
			UA_IdentityDto uA_IdentityDtock_t = uA_IdentityDao.getMax(uA_IdentityDtock); //获取主键最大值
			if(uA_IdentityDtock_t!=null) {
				int id = Integer.parseInt(uA_IdentityDtock_t.getiFatherId());
				uA_IdentityDtock_t.setiFatherId(String.valueOf(id +1));
				rdRecordDto.setID(id + 1000000001);
			}else {
				uA_IdentityDtock.setiFatherId("1");
				rdRecordDto.setID(1000000001);
			}

			//关联出库id
			ckglDto.setGlid(String.valueOf(rdRecordDto.getID()));
			ckglDto.setZt(StatusEnum.CHECK_PASS.getCode());

			rdRecordDto.setcBusType("其他出库"); //出库类别名称
			rdRecordDto.setcSource("借出借用单"); //出库类型
			rdRecordDto.setcWhCode(ckglDto.getCkdm()); //仓库代码
			rdRecordDto.setbRdFlag("0");
			rdRecordDto.setcVouchType("09");
			rdRecordDto.setcBusCode(hy_dz_borrowOutDto.getcCODE());
			rdRecordDto.setdDate(ckglDto.getCkrq()); //出库日期
			rdRecordDto.setcDepCode(ckglDto.getBmdm()); //机构代码
			rdRecordDto.setcCUsCode(jcjyglDto.getDwdm());
			rdRecordDto.setcHandler(jcjyglDto.getZsxm()); //审核人
			rdRecordDto.setcMemo(StringUtil.isNotBlank(jcjyglDto.getBz())?(jcjyglDto.getBz().length()>255?jcjyglDto.getBz().substring(0,255):jcjyglDto.getBz()):null);//备注
			rdRecordDto.setcMaker(jcjyglDto.getZsxm()); //制单人
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date1 = new Date();
			rdRecordDto.setdVeriDate(sdf.format(date1));
			rdRecordDto.setVT_ID("85");
			rdRecordDto.setDnmaketime(sdf.format(date1));
			rdRecordDto.setDnverifytime(sdf.format(date1));
			rdRecordDto.setCsysbarcode("||st09|"+rdRecordDto.getcCode());	//	"||st09|"+出库单
			rdRecordDto.setVT_ID("85");
			rdRecordDto.setbIsSTQc("0");

			boolean ck_result = rdRecordDao.insertRd09jy(rdRecordDto);
			if(!ck_result) {
				throw new BusinessException("msg","入库数据存入U8失败！");
			}
			//存明细数据用来批量更新
			List<RdRecordsDto> rdRecordsList = new ArrayList<>();
			//明细表主键id最大值
			int childid = 0;
			if(uA_IdentityDtock_t!=null) {
				childid = Integer.parseInt(uA_IdentityDtock_t.getiChildId());
			}
			//明细数据排序
			int irowno = 1;
			//存更新的库存信息
			List<CurrentStockDto> currentStockList = new ArrayList<>();
			//存货位调整
			List<InvPositionDto> invPositionDtos = new ArrayList<>();
			//存货位总数信息
			List<InvPositionSumDto> invPositionSumDtos_mod = new ArrayList<>();
			//存记账记录数据
			List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos = new ArrayList<>();


			for (CkmxDto ckmxDto_t : ckmxDtos) {
				if(!"0.00".equals(ckmxDto_t.getCksl()) && StringUtil.isNotBlank(ckmxDto_t.getCksl()) && ckmxDto_t.getCkid().equals(ckglDto.getCkid())) {
					//组装U8明细数据
					RdRecordsDto rdRecordsDto = new RdRecordsDto();
					rdRecordsDto.setID(rdRecordDto.getID()); // 关联id
					//存入主键id
					rdRecordsDto.setAutoID(childid + 1000000001);
					childid = childid + 1; //明细表主键最大值+1
					ckmxDto_t.setCkmxglid(String.valueOf(rdRecordsDto.getAutoID()));
					ckmxDto_t.setCkmxid(ckmxDto_t.getCkmxid());
					rdRecordsDto.setcInvCode(ckmxDto_t.getWlbm()); //物料编码
					rdRecordsDto.setiNum("0");
					rdRecordsDto.setiNNum("0");
					rdRecordsDto.setcItemCName(ckmxDto_t.getXmdlcsmc());
					rdRecordsDto.setcItem_class(ckmxDto_t.getXmdlcsdm());
					rdRecordsDto.setcName(ckmxDto_t.getXmbmcsmc());
					rdRecordsDto.setcItemCode(ckmxDto_t.getXmbmcsdm());
					rdRecordsDto.setiQuantity(ckmxDto_t.getCksl()); //出库数量
					rdRecordsDto.setcBatch(StringUtil.isNotBlank(ckmxDto_t.getScph())?ckmxDto_t.getScph():null); //生产批号
					rdRecordsDto.setdVDate(StringUtil.isNotBlank(ckmxDto_t.getYxq())?ckmxDto_t.getYxq():null); //失效日期
					rdRecordsDto.setcPosition(StringUtil.isNotBlank(ckmxDto_t.getKwbhcsdm())?ckmxDto_t.getKwbhcsdm():null); //库位编号/货位
					rdRecordsDto.setiNQuantity(ckmxDto_t.getCksl()); //实领数量
					rdRecordsDto.setCbMemo(StringUtil.isNotBlank(ckmxDto_t.getBz())?(ckmxDto_t.getBz().length()>255?ckmxDto_t.getBz().substring(0,255):ckmxDto_t.getBz()):null); //备注
					rdRecordsDto.setdMadeDate(StringUtil.isNotBlank(ckmxDto_t.getScrq())?ckmxDto_t.getScrq():null); //生产日期
					rdRecordsDto.setIdebitchildids(ckmxDto_t.getU8jcmxid());
					rdRecordsDto.setiDebitIDs(ckmxDto_t.getU8jcmxid());
					if (("0").equals(ckmxDto_t.getBzqflg())){
						rdRecordsDto.setcMassUnit("2");
						rdRecordsDto.setiMassDate(ckmxDto_t.getBzq()); //有效期数
					}else{
						rdRecordsDto.setcMassUnit("1");
						rdRecordsDto.setiMassDate("99"); //有效期数
					}
					if(StringUtil.isBlank(ckmxDto_t.getScrq())) {
						rdRecordsDto.setcMassUnit("0");
						rdRecordsDto.setiMassDate(null); //有效期数
					}

					rdRecordsDto.setIrowno(Integer.toString(irowno)); //当前明细排序
					rdRecordsDto.setCbsysbarcode("||st09|" + rdRecordDto.getcCode() + "|" + irowno);
					rdRecordsDto.setIsotype("0");
					if(StringUtil.isBlank(ckmxDto_t.getKwbhcsdm())) {
						rdRecordsDto.setIposflag(null);
					}else {
						rdRecordsDto.setIposflag("1");
					}
					irowno = irowno + 1;
					rdRecordsList.add(rdRecordsDto);

					//组装记账记录表
					IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto = new IA_ST_UnAccountVouchDto();
					iA_ST_UnAccountVouchDto.setIDUN(String.valueOf(rdRecordsDto.getID()));
					iA_ST_UnAccountVouchDto.setIDSUN(String.valueOf(rdRecordsDto.getAutoID()));
					iA_ST_UnAccountVouchDto.setcVouTypeUN("09");
					iA_ST_UnAccountVouchDto.setcBUstypeUN("其他出库");
					iA_ST_UnAccountVouchDtos.add(iA_ST_UnAccountVouchDto);
					int insertAccountV09s = iA_ST_UnAccountVouchDao.insertAccountV09s(iA_ST_UnAccountVouchDtos);
					if (insertAccountV09s<1){
						throw new BusinessException("msg","新增IA_ST_UnAccountVouch失败！");
					}
					//组装库存数据
					CurrentStockDto currentStockDto = new CurrentStockDto();
					currentStockDto.setcInvCode(StringUtil.isNotBlank(ckmxDto_t.getWlbm())?ckmxDto_t.getWlbm():null);//物料编码
					currentStockDto.setcBatch(StringUtil.isNotBlank(ckmxDto_t.getScph())?ckmxDto_t.getScph():null);//生成批号
					currentStockDto.setcWhCode(StringUtil.isNotBlank(ckmxDto_t.getCkdm())?ckmxDto_t.getCkdm():null);//仓库代码
					CurrentStockDto currentStockDto1 = currentStockDao.getCurrentStocksByDto(currentStockDto);
//					currentStockDto.setAutoID(Integer.parseInt(ckmxDto_t.getKcglid())); //主键id
//					CurrentStockDto currentStockDto1 = currentStockDao.getCurrentStockByAutoID(currentStockDto);
					if (currentStockDto1 == null){
						throw new BusinessException("msg","未找到U8库存信息！");
					}
					String kcl = currentStockDto1.getiQuantity();
					if (Double.parseDouble(kcl) < Double.parseDouble(ckmxDto_t.getCksl())){
						throw new BusinessException("msg","U8库存不足！");
					}
					currentStockDto.setiQuantity(ckmxDto_t.getCksl() ); //出库数量
					currentStockList.add(currentStockDto);
					//处理货位调整表
					if(StringUtil.isNotBlank(ckmxDto_t.getKwbhcsdm())) {
						InvPositionDto invPositionDto_out = new InvPositionDto();
						invPositionDto_out.setRdsID(String.valueOf(rdRecordsDto.getAutoID()));
						invPositionDto_out.setRDID(String.valueOf(rdRecordDto.getID()));
						invPositionDto_out.setcWhCode(ckglDto.getCkdm());
						invPositionDto_out.setcPosCode(ckmxDto_t.getKwbhcsdm());
						invPositionDto_out.setcInvCode(ckmxDto_t.getWlbm());
						invPositionDto_out.setcBatch(StringUtil.isNotBlank(ckmxDto_t.getScph())?ckmxDto_t.getScph():"");
						invPositionDto_out.setdVDate(StringUtil.isNotBlank(ckmxDto_t.getYxq())?ckmxDto_t.getYxq():null);
						invPositionDto_out.setiQuantity(ckmxDto_t.getCksl());
						invPositionDto_out.setcHandler(jcjyglDto.getZsxm());
						invPositionDto_out.setdDate(sdf.format(date1));
						invPositionDto_out.setbRdFlag("0");
						invPositionDto_out.setdMadeDate(StringUtil.isNotBlank(ckmxDto_t.getScrq())?ckmxDto_t.getScrq():null);
						if (("0").equals(ckmxDto_t.getBzqflg())) {
							invPositionDto_out.setcMassUnit("2");
							invPositionDto_out.setiMassDate(ckmxDto_t.getBzq()); // 有效期数
						} else {
							invPositionDto_out.setcMassUnit("1");
							invPositionDto_out.setiMassDate("99"); // 有效期数
						}
						if(StringUtil.isBlank(ckmxDto_t.getScrq())) {
							invPositionDto_out.setcMassUnit("0");
							invPositionDto_out.setiMassDate(null); // 有效期数
						}
						invPositionDto_out.setCvouchtype("09");
						invPositionDto_out.setdVouchDate(sdf.format(date1));
						invPositionDtos.add(invPositionDto_out);

						//组装货位总数信息
						InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
						invPositionSumDto.setcWhCode(ckglDto.getCkdm());
						invPositionSumDto.setcPosCode(ckmxDto_t.getKwbhcsdm());
						invPositionSumDto.setcInvCode(ckmxDto_t.getWlbm());
						invPositionSumDto.setcBatch(StringUtil.isNotBlank(ckmxDto_t.getScph())?ckmxDto_t.getScph():"");
						//判断是否存在  仓库，物料编码，库位，追溯号一样的数据
						InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
						if(invPositionSumDto_t!=null) {
							if (Double.parseDouble(invPositionSumDto_t.getiQuantity()) < Double.parseDouble(ckmxDto_t.getCksl())){
								throw new BusinessException("msg","U8货物库存不足！");
							}
							//更新货物货物数量
							invPositionSumDto_t.setJsbj("1"); //计算标记,相减
							invPositionSumDto_t.setiQuantity(ckmxDto_t.getCksl());
							invPositionSumDtos_mod.add(invPositionSumDto_t);
						}else {
							throw new BusinessException("msg","未找到出库库位货物信息！");
						}
					}
				}
			}

			//判断是新增还是修改
			if(uA_IdentityDtock_t!=null) {
				//更新
				uA_IdentityDtock_t.setiChildId(String.valueOf(childid));
				result_ui = uA_IdentityDao.update(uA_IdentityDtock_t)>0;
			}
			if(!result_ui) {
				uA_IdentityDtock.setiChildId(String.valueOf(childid));
				result_ui = uA_IdentityDao.insert(uA_IdentityDtock)>0;
			}
			if(!result_ui) {
				throw new BusinessException("msg", "最大id值更新失败!");
			}

			if(rdRecordsList.size()>10){
				int length=(int)Math.ceil((double)rdRecordsList.size()/10);//向上取整
				for(int i = 0; i<length; i++){
					List<RdRecordsDto> list=new ArrayList<>();
					int t_length;
					if(i!=length-1){
						t_length=(i+1)*10;
					}else{
						t_length=rdRecordsList.size();
					}
					for(int j=i*10;j<t_length;j++){
						list.add(rdRecordsList.get(j));
					}
					success = rdRecordsDao.insertRds9jy(list);
					if(!success)
						throw new BusinessException("msg","添加U8调拨出库信息失败！");
				}
			}else{
				success = rdRecordsDao.insertRds9jy(rdRecordsList);
				if(!success)
					throw new BusinessException("msg","添加U8调拨出库信息失败！");
			}

			//添加U8其他出库相关表数据
			//若List过大进行截断,10个明细为一组进行添加
			int result_sub;
			if(rdRecordsList.size()>10){
				int length=(int)Math.ceil((double)rdRecordsList.size()/10);//向上取整
				for(int i = 0; i<length; i++){
					List<RdRecordsDto> list=new ArrayList<>();
					int t_length;
					if(i!=length-1){
						t_length=(i+1)*10;
					}else{
						t_length=rdRecordsList.size();
					}
					for(int j=i*10;j<t_length;j++){
						list.add(rdRecordsList.get(j));
					}
					result_sub = rdRecordsDao.insertRds09Sub(list);
					if(result_sub<1) {
						throw new BusinessException("msg", "新增rdrecords09sub失败！");
					}
				}
			}else{
				result_sub = rdRecordsDao.insertRds09Sub(rdRecordsList);
				if(result_sub<1) {
					throw new BusinessException("msg", "新增rdrecords09sub失败！");
				}
			}


			//更新U8库存
			int result_kc = currentStockDao.updateList(currentStockList);
			if(result_kc<1) {
				throw new BusinessException("msg","批量更新U8库存失败！");
			}

			if(invPositionDtos.size()>0) {
				int result_hw;
				if(invPositionDtos.size()>10) {
					int length = (int)Math.ceil((double)invPositionDtos.size() / 10);//向上取整
					for (int i = 0; i < length; i++) {
						List<InvPositionDto> list = new ArrayList<>();
						int t_length;
						if (i != length - 1) {
							t_length = (i + 1) * 10;
						} else {
							t_length = invPositionDtos.size();
						}
						for (int j = i * 10; j < t_length; j++) {
							list.add(invPositionDtos.get(j));
						}
						result_hw = invPositionDao.insertDtos(list);
						if (result_hw < 1)
							throw new BusinessException("msg", "新增U8货位调整失败！");
					}
				}else{
					result_hw = invPositionDao.insertDtos(invPositionDtos);
					if (result_hw < 1)
						throw new BusinessException("msg", "新增U8货位调整失败！");
				}
			}

			//修改货物总数信息
			if(invPositionSumDtos_mod.size()>0) {
				//若List过大进行截断,10个明细为一组进行添加
				int result_mod;
				if(invPositionSumDtos_mod.size()>10) {
					int length = (int)Math.ceil((double)invPositionSumDtos_mod.size() / 10);//向上取整
					for (int i = 0; i < length; i++) {
						List<InvPositionSumDto> list = new ArrayList<>();
						int t_length;
						if (i != length - 1) {
							t_length = (i + 1) * 10;
						} else {
							t_length = invPositionSumDtos_mod.size();
						}
						for (int j = i * 10; j < t_length; j++) {
							list.add(invPositionSumDtos_mod.get(j));
						}
						result_mod = invPositionSumDao.updateDtos(list);
						if(result_mod<1) {
							throw new BusinessException("msg", "更新U8货位总数失败！");
						}
					}
				}else{
					result_mod = invPositionSumDao.updateDtos(invPositionSumDtos_mod);
					if(result_mod<1) {
						throw new BusinessException("msg", "更新U8货位总数失败！");
					}
				}
			}

			boolean result_av = addAccountVs(iA_ST_UnAccountVouchDtos,"11");
			if(!result_av) {
				throw new BusinessException("msg","新增记账信息失败！");
			}
		}
		map.put("hwxxDtos", hwxxDtos);
		//map.put("ckhwxxDtos", ckhwxxDtos);
		map.put("ckglDtos", ckglDtos);
		map.put("ckmxDtos", ckmxDtos);
		//更新关联id
		map.put("jcjyglDto", jcjyglDto);
		map.put("jcjymxDtos", jcjymxDtos);
		return map;
	}

	/**
	 * 归还U8操作
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8GhData(JcghglDto jcghglDto) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		
		JcghmxDto jcghmxDto = new JcghmxDto();
		jcghmxDto.setJcghid(jcghglDto.getJcghid());
		List<JcghmxDto> jcghmxDtos = jcghmxService.getDtoList(jcghmxDto);
		List<JcghmxDto> mxlist = jcghmxService.getDtoListGroupByCk(jcghmxDto);
		// 入库单号：RK-年月日-流水号
		String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");

		HY_DZ_BorrowOutBackDto hy_dz_borrowOutBackDto=new HY_DZ_BorrowOutBackDto();
		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); //存入账套
		uA_IdentityDto.setcVouchType("HYJCGH005");	//存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
		if(uA_IdentityDto_t!=null) {
			if(StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				uA_IdentityDto_t.setiFatherId(String.valueOf(id +1));
				uA_IdentityDto_t.setiChildId(String.valueOf(id +1));
				hy_dz_borrowOutBackDto.setID(String.valueOf(id + 1000000001));
			}
		}else {
			uA_IdentityDto.setiFatherId("1");
			hy_dz_borrowOutBackDto.setID("1000000001");
		}
		String ghdh;
		VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
		voucherHistoryDto.setCardNumber("HYJCGH001");
		//获取最大流水号
		VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
		if(voucherHistoryDto_t!=null) {
			String serialnum;
			String serial;
			//int i = 1;

			serialnum = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber()) + 1);
			serial = "0000000000" + serialnum;
			serial = serial.substring(serial.length() - 10);
			ghdh = serial;
			voucherHistoryDto_t.setcNumber(ghdh);
			//更新最大单号值
			int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
			if(result_ccode<1)
				throw new BusinessException("msg", "更新U8最大单号失败！");
		}else {
			voucherHistoryDto.setcNumber("1");
			voucherHistoryDto.setbEmpty("0");
			ghdh = "0000000001";
			voucherHistoryDao.insert(voucherHistoryDto);
		}
		hy_dz_borrowOutBackDto.setcCODE(ghdh);
		HY_DZ_BorrowOutBackDto hy_dz_borrowOutBackDto1 = hy_dz_borrowOutBackDao.queryByCode(hy_dz_borrowOutBackDto);
		if(hy_dz_borrowOutBackDto1!=null) {
				throw new BusinessException("msg","该归还单号已存在，请更新归还单号！");
			}


		jcghglDto.setGlid(hy_dz_borrowOutBackDto.getID());
		jcghglDto.setU8ghdh(ghdh);
		hy_dz_borrowOutBackDto.setcType(jcghglDto.getDwlxmc());
		hy_dz_borrowOutBackDto.setbObjectCode(jcghglDto.getKhdm());
		hy_dz_borrowOutBackDto.setCpersoncode(jcghglDto.getUsercode());
		hy_dz_borrowOutBackDto.setCdepcode(jcghglDto.getBmdm());
		hy_dz_borrowOutBackDto.setDdate(jcghglDto.getGhrq());
		hy_dz_borrowOutBackDto.setCmemo(jcghglDto.getBz());
		hy_dz_borrowOutBackDto.setCfreight("1".equals(jcghglDto.getSfzfyf())?"是":"否");
		hy_dz_borrowOutBackDto.setDmDate(jcghglDto.getGhrq());
		hy_dz_borrowOutBackDto.setcMaker(jcghglDto.getZsxm());
		hy_dz_borrowOutBackDto.setcHandler(jcghglDto.getZsxm());
		hy_dz_borrowOutBackDto.setdVeriDate(date);
		hy_dz_borrowOutBackDto.setiStatus("2");
		hy_dz_borrowOutBackDto.setMycdefineT3(date);
		hy_dz_borrowOutBackDto.setiVTID("131063");
		hy_dz_borrowOutBackDto.setType_id("25");
		hy_dz_borrowOutBackDto.setCsysbarcode("||stcg|"+hy_dz_borrowOutBackDto.getcCODE());
		int result_mv = hy_dz_borrowOutBackDao.insert(hy_dz_borrowOutBackDto);
		if(result_mv<1)
			throw new BusinessException("msg","U8主表新增失败！");
		// 更新最大值表
		boolean result_ui = false;
		//判断是新增还是修改
		if(uA_IdentityDto_t!=null) {
			//更新
			uA_IdentityDto_t.setiChildId(String.valueOf(Integer.parseInt(uA_IdentityDto_t.getiChildId()) + jcghmxDtos.size()));
			result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
		}
		if(!result_ui) {
			uA_IdentityDto.setiChildId(String.valueOf(jcghmxDtos.size()));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}
		JcjyglDto jcjyglDto = jcjyglDao.getDtoById(jcghglDto.getJcjyid());

		//生成明细id
		int lsh = 0;
		UA_IdentityDto uA_IdentityDto1 = new UA_IdentityDto();
		uA_IdentityDto1.setcAcc_Id(accountName); //存入账套
		uA_IdentityDto1.setcVouchType("hy_DZ_BorrowOutBacks");	//存入表标识
		UA_IdentityDto uA_IdentityDto1_t = uA_IdentityDao.getMax(uA_IdentityDto1); //获取主键最大值
		if(uA_IdentityDto1_t!=null) {
			if(StringUtil.isNotBlank(uA_IdentityDto1_t.getiChildId())) {
				lsh = Integer.parseInt(uA_IdentityDto1_t.getiChildId());
			}
		}else {
			uA_IdentityDto1.setiChildId("0");
			uA_IdentityDto1.setiFatherId("0");
		}

		//生成明细id
		int lshT = 0;
		UA_IdentityDto uA_IdentityDto2 = new UA_IdentityDto();
		uA_IdentityDto2.setcAcc_Id(accountName); //存入账套
		uA_IdentityDto2.setcVouchType("HYJCGH00501");	//存入表标识
		UA_IdentityDto uA_IdentityDto2_t = uA_IdentityDao.getMax(uA_IdentityDto2); //获取主键最大值
		if(uA_IdentityDto2_t!=null) {
			if(StringUtil.isNotBlank(uA_IdentityDto2_t.getiChildId())) {
				lshT = Integer.parseInt(uA_IdentityDto2_t.getiChildId());
			}
		}else {
			uA_IdentityDto2.setiChildId("0");
			uA_IdentityDto2.setiFatherId("0");
		}
		List<HY_DZ_BorrowOutsDto> hyDzBorrowOutsDtos = new ArrayList<>();
		int k = 0 ;
		for (JcghmxDto jcghmxDto_t: jcghmxDtos) {
			k++;
			HY_DZ_BorrowOutBacksDto hyDzBorrowOutBacksDto = new HY_DZ_BorrowOutBacksDto();
			lsh = lsh+1;
			hyDzBorrowOutBacksDto.setAutoID(String.valueOf(lsh+1000000000));
			jcghmxDto_t.setMxglid(hyDzBorrowOutBacksDto.getAutoID());
			jcghmxService.update(jcghmxDto_t);
			hyDzBorrowOutBacksDto.setID(hy_dz_borrowOutBackDto.getID());
			if("0".equals(jcghmxDto_t.getSfyzgh())){
				hyDzBorrowOutBacksDto.setDiffBack("否");
			}else if("1".equals(jcghmxDto_t.getSfyzgh())){
				hyDzBorrowOutBacksDto.setDiffBack("是");
			}

			hyDzBorrowOutBacksDto.setUpAutoID(jcghmxDto_t.getJymxglid());
			hyDzBorrowOutBacksDto.setCinvcode(jcghmxDto_t.getWlbm());
			hyDzBorrowOutBacksDto.setCwhcode(jcghmxDto_t.getCkdm());
			hyDzBorrowOutBacksDto.setcPosition(jcghmxDto_t.getKwdm());
			hyDzBorrowOutBacksDto.setIquantity(jcghmxDto_t.getGhsl());
			hyDzBorrowOutBacksDto.setCmemo(jcghmxDto_t.getBz());
			hyDzBorrowOutBacksDto.setCbatch(jcghmxDto_t.getScph());
			hyDzBorrowOutBacksDto.setCbsysbarcode("||stcg|"+hy_dz_borrowOutBackDto.getcCODE()+ "|" + k);
			hyDzBorrowOutBacksDto.setDmadedate(jcghmxDto_t.getScrq());
			hyDzBorrowOutBacksDto.setDvdate(jcghmxDto_t.getYxq());
			hyDzBorrowOutBacksDto.setCmassunit(jcghmxDto_t.getYxqbz());
			if ("0".equals(jcghmxDto_t.getBzqflg())){
				hyDzBorrowOutBacksDto.setCmassunit("2");
				hyDzBorrowOutBacksDto.setImassdate(jcghmxDto_t.getBzq()); //有效期数
			}else{
				hyDzBorrowOutBacksDto.setCmassunit("1");
				hyDzBorrowOutBacksDto.setImassdate("99"); //有效期数
			}
			if(StringUtil.isBlank(jcghmxDto_t.getScrq())) {
				hyDzBorrowOutBacksDto.setCmassunit("0");
				hyDzBorrowOutBacksDto.setImassdate(null); //有效期数
			}
			hyDzBorrowOutBacksDto.setInum("0");
			hyDzBorrowOutBacksDto.setiQtyRK(jcghmxDto_t.getGhsl());
			hyDzBorrowOutBacksDto.setiQtyRK2("0");
			hyDzBorrowOutBacksDto.setIexpiratdatecalcu("0");

			result_mv = hy_dz_borrowOutBacksDao.insert(hyDzBorrowOutBacksDto);
			if(result_mv<1)
				throw new BusinessException("msg","U8明细新增失败！");
			lshT = lshT+1;
			hyDzBorrowOutBacksDto.setAutoID(String.valueOf(lshT+1000000000));
			hyDzBorrowOutBacksDto.setUpID(hy_dz_borrowOutBackDto.getID());
			hyDzBorrowOutBacksDto.setUpAutoID(jcghmxDto_t.getJymxglid());
			hyDzBorrowOutBacksDto.setVoucherType("HYJCGH005");
			hyDzBorrowOutBacksDto.setIinvexchrate("0");
			hyDzBorrowOutBacksDto.setMycdefineB8(jcghmxDto_t.getGhsl());
			hyDzBorrowOutBacksDto.setMycdefineB9("0");
			jcghmxDto_t.setMxglid2(hyDzBorrowOutBacksDto.getAutoID());
			jcghmxService.update(jcghmxDto_t);
			result_mv = hy_dz_borrowOutBacksDao.insert2(hyDzBorrowOutBacksDto);
			if(result_mv<1)
				throw new BusinessException("msg","U8hyDzBorrowOutBacks2新增失败！");
			if(StringUtil.isNotBlank(jcghmxDto_t.getJymxglid())){
				HY_DZ_BorrowOutsDto hyDzBorrowOutsDto = new HY_DZ_BorrowOutsDto();
				hyDzBorrowOutsDto.setAutoID(jcghmxDto_t.getJymxglid());
				hyDzBorrowOutsDto.setiQtyBack(jcghmxDto_t.getGhsl());
				hyDzBorrowOutsDto.setiQtyBack2("0");
				hyDzBorrowOutsDtos.add(hyDzBorrowOutsDto);
			}
		}
		if(!CollectionUtils.isEmpty(hyDzBorrowOutsDtos)){
			result_ui = hy_dz_borrowOutsDao.updateList(hyDzBorrowOutsDtos);
			if(!result_ui){
				throw new BusinessException("msg", "根据借出单归还数量失败!");
			}
		}
		if(uA_IdentityDto1_t!=null) {
			uA_IdentityDto1_t.setiChildId(String.valueOf(lsh));
			uA_IdentityDto1_t.setiFatherId(String.valueOf(lsh));
			result_ui = uA_IdentityDao.update(uA_IdentityDto1_t)>0;
		}
		if(!result_ui) {
			uA_IdentityDto1.setiChildId(String.valueOf(lsh));
			uA_IdentityDto1.setiFatherId(String.valueOf(lsh));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto1)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}
		boolean result_ui2 = false;
		if(uA_IdentityDto2_t!=null) {
			uA_IdentityDto2_t.setiChildId(String.valueOf(lshT));
			uA_IdentityDto2_t.setiFatherId(String.valueOf(lshT));
			result_ui2 = uA_IdentityDao.update(uA_IdentityDto2_t)>0;
		}
		if(!result_ui2) {
			uA_IdentityDto2.setiChildId(String.valueOf(lshT));
			uA_IdentityDto2.setiFatherId(String.valueOf(lshT));
			result_ui2 = uA_IdentityDao.insert(uA_IdentityDto2)>0;
		}
		if(!result_ui2) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}
		for (JcghmxDto dto:mxlist) {
			//出库存入U8
			RdRecordDto rdRecordDto = new RdRecordDto();
			VoucherHistoryDto voucherHistoryDtock = new VoucherHistoryDto();
			voucherHistoryDtock.setcSeed(date);
			voucherHistoryDtock.setCardNumber("0302");
			//获取最大流水号
			VoucherHistoryDto voucherHistoryDtock_t = voucherHistoryDao.getMaxSerial(voucherHistoryDtock);
			if(voucherHistoryDtock_t!=null) {
				String serial = String.valueOf(Integer.parseInt(voucherHistoryDtock_t.getcNumber())+1);
				rdRecordDto.setcCode(date.substring(2,6)+"000"+(serial.length()>1?serial:"0"+serial)); // 到货单号
				rdRecordDto.setPrefix(date.substring(2,6));
				rdRecordDto.setSflxbj("9");
				List<RdRecordDto> rdRecordDtos_t = rdRecordDao.getcCodeRd(rdRecordDto);
				int ccode_new=0;
				if (rdRecordDtos_t != null && rdRecordDtos_t.size() > 0) {
					List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(rdRecordDto);
					ccode_new = Integer.parseInt(rdRecordDto.getcCode()) + 1;
					for (RdRecordDto rdRecordDto_c : rdList) {
						if(ccode_new-Integer.parseInt(rdRecordDto_c.getcCode())==0) {
							ccode_new = ccode_new+1;
						}else {
							break;
						}
					}
				}
				if(ccode_new!=0) {
					rdRecordDto.setcCode(Integer.toString(ccode_new)); // 到货单号
					voucherHistoryDtock_t.setcNumber(Integer.parseInt(Integer.toString(ccode_new).substring(8))+""); //去除多余0
				}else {
					voucherHistoryDtock_t.setcNumber(serial);
				}
				//更新最大单号值
				int result_ccode = voucherHistoryDao.update(voucherHistoryDtock_t);
				if(result_ccode<1)
					throw new BusinessException("msg", "更新U8最大单号失败！");
			}else {
				voucherHistoryDtock.setiRdFlagSeed("2");
				voucherHistoryDtock.setcContent("日期");
				voucherHistoryDtock.setcContentRule("月");
				voucherHistoryDtock.setbEmpty("0");
				voucherHistoryDtock.setcNumber("1");
				rdRecordDto.setcCode(date.substring(2,6)+"00001");
				//单号最大表增加数据
				int result_ccode = voucherHistoryDao.insert(voucherHistoryDtock);
				if(result_ccode<1)
					throw new BusinessException("msg", "新增U8最大单号失败！");
			}

			UA_IdentityDto uA_IdentityDtock = new UA_IdentityDto();
			uA_IdentityDtock.setcAcc_Id(accountName); //存入账套
			uA_IdentityDtock.setcVouchType("rd");	//存入表标识
			UA_IdentityDto uA_IdentityDtock_t = uA_IdentityDao.getMax(uA_IdentityDtock); //获取主键最大值
			if(uA_IdentityDtock_t!=null) {
				int id = Integer.parseInt(uA_IdentityDtock_t.getiFatherId());
				uA_IdentityDtock_t.setiFatherId(String.valueOf(id +1));
				rdRecordDto.setID(id + 1000000001);
			}else {
				uA_IdentityDtock.setiFatherId("1");
				rdRecordDto.setID(1000000001);
			}


			rdRecordDto.setcBusType("其它入库"); //出库类别名称
			rdRecordDto.setcSource("借出归还单"); //出库类型
			CkxxDto ckxxDto = ckxxService.getDtoById(dto.getCkid());
			rdRecordDto.setcWhCode(ckxxDto.getCkdm()); //仓库代码
			rdRecordDto.setbRdFlag("1");
			rdRecordDto.setcVouchType("08");
			rdRecordDto.setcBusCode(hy_dz_borrowOutBackDto.getcCODE());
			rdRecordDto.setdDate(date); //日期
			rdRecordDto.setcDepCode(jcghglDto.getBmdm()); //机构代码
			rdRecordDto.setcHandler(jcghglDto.getZsxm()); //审核人
			rdRecordDto.setcMemo(StringUtil.isNotBlank(jcghglDto.getBz())?(jcghglDto.getBz().length()>255?jcghglDto.getBz().substring(0,255):jcghglDto.getBz()):null);//备注
			rdRecordDto.setcMaker(jcghglDto.getZsxm()); //制单人
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			Date date1 = new Date();
			rdRecordDto.setdVeriDate(sdf.format(date1));
			rdRecordDto.setVT_ID("67");
			rdRecordDto.setDnmaketime(sdf.format(date1));
			rdRecordDto.setDnverifytime(sdf.format(date1));
			rdRecordDto.setCsysbarcode("||st08|"+rdRecordDto.getcCode());
			rdRecordDto.setcPersonCode(jcjyglDto.getGrouping());

			int rk_result = rdRecordDao.insertQtrk08(rdRecordDto);
			if(rk_result==0) {
				throw new BusinessException("msg","入库数据存入U8失败！");
			}
			//存明细数据用来批量更新
			List<RdRecordsDto> rdRecordsList = new ArrayList<>();
			//明细表主键id最大值
			int childid = 0;
			if(uA_IdentityDtock_t!=null) {
				childid = Integer.parseInt(uA_IdentityDtock_t.getiChildId());
			}
			//明细数据排序
			int irowno = 1;
			//存更新的库存信息
			List<CurrentStockDto> modcurrentStockList = new ArrayList<>();
			//存货位调整
			List<InvPositionDto> invPositionDtos = new ArrayList<>();
			//存货位总数信息
			List<InvPositionSumDto> invPositionSumDtos_mod = new ArrayList<>();
			//存货位货物总数信息
			List<InvPositionSumDto> invPositionSumDtos_add = new ArrayList<>();
			//存记账记录数据
			List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos = new ArrayList<>();


			for (JcghmxDto jcghmxDto_t : jcghmxDtos) {
				if(dto.getCkid().equals(jcghmxDto_t.getCkid())){
					//组装U8明细数据
					RdRecordsDto rdRecordsDto = new RdRecordsDto();
					rdRecordsDto.setID(rdRecordDto.getID()); // 关联id
					//存入主键id
					rdRecordsDto.setAutoID(childid + 1000000001);
					childid = childid + 1; //明细表主键最大值+1
					rdRecordsDto.setcInvCode(jcghmxDto_t.getWlbm()); //物料编码
					rdRecordsDto.setiQuantity(jcghmxDto_t.getGhsl());
					rdRecordsDto.setcBatch(StringUtil.isNotBlank(jcghmxDto_t.getScph())?jcghmxDto_t.getScph():null); //生产批号
					rdRecordsDto.setdVDate(StringUtil.isNotBlank(jcghmxDto_t.getYxq())?jcghmxDto_t.getYxq():null); //失效日期
					rdRecordsDto.setiNQuantity(jcghmxDto_t.getGhsl());
					rdRecordsDto.setCbMemo(StringUtil.isNotBlank(jcghmxDto_t.getBz())?(jcghmxDto_t.getBz().length()>255?jcghmxDto_t.getBz().substring(0,255):jcghmxDto_t.getBz()):null); //备注
					rdRecordsDto.setdMadeDate(StringUtil.isNotBlank(jcghmxDto_t.getScrq())?jcghmxDto_t.getScrq():null); //生产日期
					rdRecordsDto.setIdebitchildids(jcghmxDto_t.getMxglid2());
					rdRecordsDto.setiDebitIDs(jcghmxDto_t.getMxglid());
					rdRecordsDto.setiNNum("0");
					rdRecordsDto.setiNum("0");
					if ("0".equals(jcghmxDto_t.getBzqflg())){
						rdRecordsDto.setcMassUnit("2");
						rdRecordsDto.setiMassDate(jcghmxDto_t.getBzq()); //有效期数
					}else{
						rdRecordsDto.setcMassUnit("1");
						rdRecordsDto.setiMassDate("99"); //有效期数
					}
					if(StringUtil.isBlank(jcghmxDto_t.getScrq())) {
						rdRecordsDto.setcMassUnit("0");
						rdRecordsDto.setiMassDate(null); //有效期数
					}

					rdRecordsDto.setIrowno(irowno+""); //当前明细排序
					rdRecordsDto.setCbsysbarcode("||st08|" + rdRecordDto.getcCode() + "|" + irowno);
					rdRecordsDto.setcPosition(jcghmxDto_t.getKwdm());
					irowno = irowno + 1;
					rdRecordsList.add(rdRecordsDto);

					//组装记账记录表
					IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto = new IA_ST_UnAccountVouchDto();
					iA_ST_UnAccountVouchDto.setIDUN(String.valueOf(rdRecordsDto.getID()));
					iA_ST_UnAccountVouchDto.setIDSUN(String.valueOf(rdRecordsDto.getAutoID()));
					iA_ST_UnAccountVouchDto.setcVouTypeUN("08");
					iA_ST_UnAccountVouchDto.setcBUstypeUN("其他入库");
					iA_ST_UnAccountVouchDtos.add(iA_ST_UnAccountVouchDto);
					//组装库存数据
					CurrentStockDto currentStockDto_mod = new CurrentStockDto();
					currentStockDto_mod.setcInvCode(jcghmxDto_t.getWlbm());
					currentStockDto_mod.setcBatch(jcghmxDto_t.getScph());
					currentStockDto_mod.setcWhCode(jcghmxDto_t.getCkdm());
					CurrentStockDto had_currentStock = currentStockDao.getCurrentStocksByDto(currentStockDto_mod);
					if(had_currentStock!=null) {
						had_currentStock.setiQuantity(jcghmxDto_t.getGhsl());
						had_currentStock.setiQuantitybj("1");
						modcurrentStockList.add(had_currentStock);
					}else {
						// 新增得U8库存数据
						SCM_ItemDto sCM_ItemDto_c  = sCM_ItemDao.getDtoBycInvVode(currentStockDto_mod.getcInvCode());
						if(sCM_ItemDto_c!=null) {
							currentStockDto_mod.setItemId(sCM_ItemDto_c.getId());
						}else {
							// 取SCM_Item表中最大值
							int num_ItemId = sCM_ItemDao.getMaxItemId() + 1;
							currentStockDto_mod.setItemId(Integer.toString(num_ItemId));
							// 存入新增的物料
							SCM_ItemDto sCM_ItemDto = new SCM_ItemDto();
							sCM_ItemDto.setcInvCode(currentStockDto_mod.getcInvCode()); // 物料编码
							int result_scm = sCM_ItemDao.insert(sCM_ItemDto);
							if (result_scm < 1) {
								throw new BusinessException("msg", "sCM_Item新增失败！");
							}
						}
						currentStockDto_mod.setiQuantity(jcghmxDto_t.getGhsl()); // 现存数量
						currentStockDto_mod.setdVDate(jcghmxDto_t.getYxq()); // 失效日期
						currentStockDto_mod.setdMdate(jcghmxDto_t.getScrq()); // 生产日期
						currentStockDto_mod.setiExpiratDateCalcu("0");
						if (("0").equals(jcghmxDto_t.getBzqflg())) {
							currentStockDto_mod.setcMassUnit("2"); // 有效单位
							currentStockDto_mod.setiMassDate(jcghmxDto_t.getBzq()); // 有效期数
						} else {
							currentStockDto_mod.setcMassUnit("1"); // 有效单位
							currentStockDto_mod.setiMassDate("99"); // 有效期数
						}
						int result_add = currentStockDao.insert(currentStockDto_mod);
						if (result_add < 1) {
							throw new BusinessException("msg", "U8库存新增失败！");
						}
					}
					//处理货位调整表
					if(StringUtil.isNotBlank(jcghmxDto_t.getKwdm())) {
						InvPositionDto invPositionDto_out = new InvPositionDto();
						invPositionDto_out.setRdsID(String.valueOf(rdRecordsDto.getAutoID()));
						invPositionDto_out.setRDID(String.valueOf(rdRecordDto.getID()));
						invPositionDto_out.setcWhCode(jcghmxDto_t.getCkdm());
						invPositionDto_out.setcPosCode(jcghmxDto_t.getKwdm());
						invPositionDto_out.setcInvCode(jcghmxDto_t.getWlbm());
						invPositionDto_out.setcBatch(StringUtil.isNotBlank(jcghmxDto_t.getScph())?jcghmxDto_t.getScph():"");
						invPositionDto_out.setdVDate(StringUtil.isNotBlank(jcghmxDto_t.getYxq())?jcghmxDto_t.getYxq():null);
						invPositionDto_out.setiQuantity(jcghmxDto_t.getGhsl());
						invPositionDto_out.setcHandler(jcghglDto.getZsxm());
						invPositionDto_out.setdDate(sdf.format(date1));
						invPositionDto_out.setbRdFlag("0");
						invPositionDto_out.setdMadeDate(StringUtil.isNotBlank(jcghmxDto_t.getScrq())?jcghmxDto_t.getScrq():null);
						if (("0").equals(jcghmxDto_t.getBzqflg())) {
							invPositionDto_out.setcMassUnit("2");
							invPositionDto_out.setiMassDate(jcghmxDto_t.getBzq()); // 有效期数
						} else {
							invPositionDto_out.setcMassUnit("1");
							invPositionDto_out.setiMassDate("99"); // 有效期数
						}
						if(StringUtil.isBlank(jcghmxDto_t.getScrq())) {
							invPositionDto_out.setcMassUnit("0");
							invPositionDto_out.setiMassDate(null); // 有效期数
						}
						invPositionDto_out.setCvouchtype("09");
						invPositionDto_out.setdVouchDate(sdf.format(date1));
						invPositionDtos.add(invPositionDto_out);

						//组装货位总数信息
						InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
						invPositionSumDto.setcWhCode(jcghmxDto_t.getCkdm());
						invPositionSumDto.setcPosCode(jcghmxDto_t.getKwdm());
						invPositionSumDto.setcInvCode(jcghmxDto_t.getWlbm());
						invPositionSumDto.setcBatch(jcghmxDto_t.getScph());
						//判断是否存在  仓库，物料编码，库位，追溯号一样的数据
						InvPositionSumDto invPositionSumDto_l = invPositionSumDao.getDto(invPositionSumDto);
						if(invPositionSumDto_l!=null) {
							//存在更新
							invPositionSumDto_l.setJsbj("0"); //计算标记,相加
							invPositionSumDto_l.setiQuantity(jcghmxDto_t.getGhsl());
							invPositionSumDtos_mod.add(invPositionSumDto_l);
						}else {
							//不存在新增
							invPositionSumDto.setiQuantity(jcghmxDto_t.getGhsl());
							invPositionSumDto.setInum(null);
							if (("0").equals(jcghmxDto_t.getBzqflg())) {
								invPositionSumDto.setcMassUnit("2");
								invPositionSumDto.setiMassDate(jcghmxDto_t.getBzq()); // 保质期
							} else {
								invPositionSumDto.setcMassUnit("1");
								invPositionSumDto.setiMassDate("99"); // 保质期
							}
							invPositionSumDto.setdMadeDate(jcghmxDto_t.getScrq());
							invPositionSumDto.setdVDate(jcghmxDto_t.getYxq());
							invPositionSumDtos_add.add(invPositionSumDto);
						}
					}
				}
			}

//			判断是新增还是修改
			if(uA_IdentityDtock_t!=null) {
				//更新
				uA_IdentityDtock_t.setiChildId(String.valueOf(childid));
				result_ui = uA_IdentityDao.update(uA_IdentityDtock_t)>0;
			}
			if(!result_ui) {
				uA_IdentityDtock.setiChildId(String.valueOf(childid));
				result_ui = uA_IdentityDao.insert(uA_IdentityDtock)>0;
			}
			if(!result_ui) {
				throw new BusinessException("msg", "最大id值更新失败!");
			}

			if(rdRecordsList.size()>10){
				int length=(int)Math.ceil((double)rdRecordsList.size()/10);//向上取整
				for(int i = 0; i<length; i++){
					List<RdRecordsDto> list=new ArrayList<>();
					int t_length;
					if(i!=length-1){
						t_length=(i+1)*10;
					}else{
						t_length=rdRecordsList.size();
					}
					for(int j=i*10;j<t_length;j++){
						list.add(rdRecordsList.get(j));
					}
					int result_rds = rdRecordsDao.insertQtRds08(list);
					if (result_rds<1)
						throw new BusinessException("msg", "更新U8入库明细表失败！");
				}
			}else{
				int result_rds = rdRecordsDao.insertQtRds08(rdRecordsList);
				if (result_rds<1)
					throw new BusinessException("msg", "更新U8入库明细表失败！");
			}

			//添加U8其他出库相关表数据
			//若List过大进行截断,10个明细为一组进行添加
			int result_sub;
			if(rdRecordsList.size()>10){
				int length=(int)Math.ceil((double)rdRecordsList.size()/10);//向上取整
				for(int i = 0; i<length; i++){
					List<RdRecordsDto> list=new ArrayList<>();
					int t_length;
					if(i!=length-1){
						t_length=(i+1)*10;
					}else{
						t_length=rdRecordsList.size();
					}
					for(int j=i*10;j<t_length;j++){
						list.add(rdRecordsList.get(j));
					}
					result_sub = rdRecordsDao.insertRds09Sub(list);
					if(result_sub<1) {
						throw new BusinessException("msg", "新增rdrecords09sub失败！");
					}
				}
			}else{
				result_sub = rdRecordsDao.insertRds09Sub(rdRecordsList);
				if(result_sub<1) {
					throw new BusinessException("msg", "新增rdrecords09sub失败！");
				}
			}


			//更新U8库存
			if(!CollectionUtils.isEmpty(modcurrentStockList)){
				int result_kc = currentStockDao.updateIQuantityList(modcurrentStockList);
				if(result_kc<1) {
					throw new BusinessException("msg","批量更新U8库存失败！");
				}
			}


			if(invPositionDtos.size()>0) {
				int result_hw;
				if(invPositionDtos.size()>10) {
					int length = (int)Math.ceil((double)invPositionDtos.size() / 10);//向上取整
					for (int i = 0; i < length; i++) {
						List<InvPositionDto> list = new ArrayList<>();
						int t_length;
						if (i != length - 1) {
							t_length = (i + 1) * 10;
						} else {
							t_length = invPositionDtos.size();
						}
						for (int j = i * 10; j < t_length; j++) {
							list.add(invPositionDtos.get(j));
						}
						result_hw = invPositionDao.insertDtos(list);
						if (result_hw < 1)
							throw new BusinessException("msg", "新增U8货位调整失败！");
					}
				}else{
					result_hw = invPositionDao.insertDtos(invPositionDtos);
					if (result_hw < 1)
						throw new BusinessException("msg", "新增U8货位调整失败！");
				}
			}

			//修改货物总数信息
			if(invPositionSumDtos_mod.size()>0) {
				//若List过大进行截断,10个明细为一组进行添加
				int result_mod;
				if(invPositionSumDtos_mod.size()>10) {
					int length = (int)Math.ceil((double)invPositionSumDtos_mod.size() / 10);//向上取整
					for (int i = 0; i < length; i++) {
						List<InvPositionSumDto> list = new ArrayList<>();
						int t_length;
						if (i != length - 1) {
							t_length = (i + 1) * 10;
						} else {
							t_length = invPositionSumDtos_mod.size();
						}
						for (int j = i * 10; j < t_length; j++) {
							list.add(invPositionSumDtos_mod.get(j));
						}
						result_mod = invPositionSumDao.updateDtos(list);
						if(result_mod<1) {
							throw new BusinessException("msg", "更新U8货位总数失败！");
						}
					}
				}else{
					result_mod = invPositionSumDao.updateDtos(invPositionSumDtos_mod);
					if(result_mod<1) {
						throw new BusinessException("msg", "更新U8货位总数失败！");
					}
				}
			}

			//新增货物总数信息
			if(invPositionSumDtos_add.size()>0) {
				//若List过大进行截断,10个明细为一组进行添加
				int result_add;
				if(invPositionSumDtos_add.size()>10) {
					int length = (int)Math.ceil((double)invPositionSumDtos_add.size() / 10);//向上取整   2
					for (int i = 0; i < length; i++) {
						List<InvPositionSumDto> list = new ArrayList<>();
						int t_length;
						if (i != length - 1) {
							t_length = (i + 1) * 10;
						} else {
							t_length = invPositionSumDtos_add.size();
						}
						for (int j = i * 10; j < t_length; j++) {
							list.add(invPositionSumDtos_add.get(j));
						}
						result_add = invPositionSumDao.insertDtos(list);
						if(result_add<1) {
							throw new BusinessException("msg", "新增U8货位总数失败！");
						}
					}
				}else{
					result_add = invPositionSumDao.insertDtos(invPositionSumDtos_add);
					if(result_add<1) {
						throw new BusinessException("msg", "新增U8货位总数失败！");
					}
				}
			}

			boolean result_av = addAccountVs(iA_ST_UnAccountVouchDtos,"08");
			if(!result_av) {
				throw new BusinessException("msg","新增记账信息失败！");
			}
		}
		map.put("jcghglDto",jcghglDto);
		return map;
	}


	/**
	 * 出库新增U8做出库
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8CkData(LlglDto llglDto) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		List<CkglDto> ckglList = ckglDao.groupByCkid(llglDto.getLlid());
		List<CkmxDto> ckmxList_mx = new ArrayList<>();
		for (CkglDto ckglDto : ckglList) {
			//出库存入U8
			RdRecordDto rdRecordDto = new RdRecordDto();
			SimpleDateFormat sdf_d = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			String prefix = sdf_d.format(date);
			VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
			voucherHistoryDto.setcSeed(prefix);
			voucherHistoryDto.setCardNumber("0412");
			//获取最大流水号
			VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
			if(voucherHistoryDto_t!=null) {
				String serial = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
				rdRecordDto.setcCode(prefix+(serial.length()>1?serial:"0"+serial)); // 到货单号
				rdRecordDto.setPrefix(prefix);
				rdRecordDto.setSflxbj("11");
				List<RdRecordDto> rdRecordDtos_t = rdRecordDao.getcCodeRd(rdRecordDto);
				int ccode_new=0;
		        if (rdRecordDtos_t != null && rdRecordDtos_t.size() > 0) {
		            List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(rdRecordDto);
		            ccode_new = Integer.parseInt(rdRecordDto.getcCode()) + 1;
		            for (RdRecordDto rdRecordDto_c : rdList) {
						if(ccode_new-Integer.parseInt(rdRecordDto_c.getcCode())==0) {
							ccode_new = ccode_new+1;
						}else {
							break;
						}
					}
		        }
		        if(ccode_new!=0) {
		        	rdRecordDto.setcCode(Integer.toString(ccode_new)); // 到货单号
		        	voucherHistoryDto_t.setcNumber(Integer.parseInt(Integer.toString(ccode_new).substring(8))+""); //去除多余0
		        }else {
		        	voucherHistoryDto_t.setcNumber(serial);
		        }        
				//更新最大单号值		
				int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
				if(result_ccode<1)
					throw new BusinessException("msg", "更新U8最大单号失败！");
			}else {
				voucherHistoryDto.setiRdFlagSeed("2");
				voucherHistoryDto.setcContent("日期");
				voucherHistoryDto.setcContentRule("日");
				voucherHistoryDto.setbEmpty("0");
				voucherHistoryDto.setcNumber("1");
				rdRecordDto.setcCode(prefix+"01");
				//单号最大表增加数据
				int result_ccode = voucherHistoryDao.insert(voucherHistoryDto);
				if(result_ccode<1)
					throw new BusinessException("msg", "新增U8最大单号失败！");
			}
			
			UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
			uA_IdentityDto.setcAcc_Id(accountName); //存入账套
			uA_IdentityDto.setcVouchType("rd");	//存入表标识
			UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
			if(uA_IdentityDto_t!=null) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				uA_IdentityDto_t.setiFatherId(String.valueOf(id +1));
				rdRecordDto.setID(id + 1000000001);
			}else {
				uA_IdentityDto.setiFatherId("1");
				rdRecordDto.setID(1000000001);
			}
			
			//关联出库id
			ckglDto.setGlid(String.valueOf(rdRecordDto.getID()));
			ckglDto.setZt(StatusEnum.CHECK_PASS.getCode());
			
			rdRecordDto.setcBusType("领料"); //出库类别名称
			rdRecordDto.setcSource("领料申请单"); //出库类型
			MaterialAppVouchDto materialAppVouchDto_t = materialAppVouchDao.getDtoById(llglDto.getGlid());
			rdRecordDto.setcBusCode(materialAppVouchDto_t.getcCode()); //领料单号
			rdRecordDto.setcMemo(llglDto.getJlbh()); //记录编号
			rdRecordDto.setcWhCode(ckglDto.getCkdm()); //仓库代码
			rdRecordDto.setdDate(ckglDto.getCkrq()); //出库日期
			rdRecordDto.setcRdCode(ckglDto.getCklbcsdm()); //出库参数代码
			rdRecordDto.setcDepCode(ckglDto.getBmdm()); //机构代码
			rdRecordDto.setcHandler(llglDto.getZsxm()); //审核人
			rdRecordDto.setcMaker(ckglDto.getFlrmc()); //制单人
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");		  
			rdRecordDto.setdVeriDate(sdf.format(date));
			SimpleDateFormat sdf_t = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			rdRecordDto.setDnmaketime(sdf_t.format(date));
			rdRecordDto.setDnverifytime(sdf_t.format(date));
			rdRecordDto.setCsysbarcode("||st11|"+rdRecordDto.getcCode());	//	"||st11|"+出库单
			//获取出库明细
			CkmxDto ckmxDto = new CkmxDto();
			ckmxDto.setCkid(ckglDto.getCkid());
			List<CkmxDto> ckmxList = ckmxService.getDtoList(ckmxDto);
			rdRecordDto.setbOMFirst("0");
			rdRecordDto.setBmotran("0");
			rdRecordDto.setbHYVouch("0");
			//判断是否存在货位为空
			for (CkmxDto ckmxDto_f : ckmxList) {
				if(StringUtil.isBlank(ckmxDto_f.getKwbhcsdm())) {
					rdRecordDto.setbOMFirst(null);
					rdRecordDto.setBmotran(null);
					rdRecordDto.setbHYVouch(null);
				}
			}
			int result = rdRecord11Dao.insert(rdRecordDto);
			if(result<1) {
				throw new BusinessException("msg","出库数据存入U8失败！");
			}
			ckglDto.setU8ckdh(rdRecordDto.getcCode());
			//存明细数据用来批量更新
			List<RdRecordsDto> rdRecordsList = new ArrayList<>();
			//明细表主键id最大值
			int childid = 0;
			if(uA_IdentityDto_t!=null) {
				childid = Integer.parseInt(uA_IdentityDto_t.getiChildId());
			}			
			//明细数据排序
			int irowno = 1;
			//存更新的库存信息
			List<CurrentStockDto> currentStockList = new ArrayList<>();
			//存更新的领料信息
			List<MaterialAppVouchsDto> materialAppVouchsList = new ArrayList<>();
			//存货位调整
			List<InvPositionDto> invPositionDtos = new ArrayList<>();
			//存货位总数信息
			List<InvPositionSumDto> invPositionSumDtos_mod = new ArrayList<>();
			//存记账记录数据
			List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos = new ArrayList<>();
			List<String> cinvcodes = new ArrayList<>();
			
			for (CkmxDto ckmxDto_t : ckmxList) {
				if(!"0.00".equals(ckmxDto_t.getCksl()) && StringUtil.isNotBlank(ckmxDto_t.getCksl())) {
					cinvcodes.add(ckmxDto_t.getWlbm());
					//组装U8明细数据
					RdRecordsDto rdRecordsDto = new RdRecordsDto();
					rdRecordsDto.setID(rdRecordDto.getID()); // 关联id
					//存入主键id
					rdRecordsDto.setAutoID(childid + 1000000001);
					childid = childid + 1; //明细表主键最大值+1
					CkmxDto ckmx=new CkmxDto();
					ckmx.setCkmxglid(String.valueOf(rdRecordsDto.getAutoID()));
					ckmx.setCkmxid(ckmxDto_t.getCkmxid());
					ckmxList_mx.add(ckmx);
					rdRecordsDto.setcDefine23(llglDto.getKhmc()); //客户
					rdRecordsDto.setcInvCode(ckmxDto_t.getWlbm()); //物料编码
					rdRecordsDto.setiQuantity(ckmxDto_t.getCksl()); //出库数量			
					rdRecordsDto.setcBatch(StringUtil.isNotBlank(ckmxDto_t.getScph())?ckmxDto_t.getScph():null); //生产批号
					rdRecordsDto.setdVDate(StringUtil.isNotBlank(ckmxDto_t.getYxq())?ckmxDto_t.getYxq():null); //失效日期
					rdRecordsDto.setcPosition(StringUtil.isNotBlank(ckmxDto_t.getKwbhcsdm())?ckmxDto_t.getKwbhcsdm():null); //库位编号/货位
					rdRecordsDto.setcItem_class(ckmxDto_t.getXmdlcsdm()); //项目大类代码
					rdRecordsDto.setcItemCode(ckmxDto_t.getXmbmcsdm()); //项目编码代码
					rdRecordsDto.setcName(ckmxDto_t.getXmbmcsmc()); //项目编码名称
					rdRecordsDto.setcItemCName(ckmxDto_t.getXmdlcsmc()); //项目大类名称
					rdRecordsDto.setiNQuantity(ckmxDto_t.getCksl()); //实领数量
					rdRecordsDto.setdMadeDate(StringUtil.isNotBlank(ckmxDto_t.getScrq())?ckmxDto_t.getScrq():null); //生产日期
					if (("0").equals(ckmxDto_t.getBzqflg())){
						rdRecordsDto.setcMassUnit("2");
						rdRecordsDto.setiMassDate(ckmxDto_t.getBzq()); //有效期数
					}else{
						rdRecordsDto.setcMassUnit("1");
						rdRecordsDto.setiMassDate("99"); //有效期数
					}
					if(StringUtil.isBlank(ckmxDto_t.getScrq())) {
						rdRecordsDto.setcMassUnit("0");
						rdRecordsDto.setiMassDate(null); //有效期数
					}
					rdRecordsDto.setiMaIDs(ckmxDto_t.getGlid()); //关联申请单id
					rdRecordsDto.setIrowno(Integer.toString(irowno)); //当前明细排序 hwllbz
					rdRecordsDto.setCbMemo(StringUtil.isNotBlank(ckmxDto_t.getHwllbz())?(ckmxDto_t.getHwllbz().length()>255?ckmxDto_t.getHwllbz().substring(0,255):ckmxDto_t.getHwllbz()):null); //到货备注
					rdRecordsDto.setCbsysbarcode("||st11|" + rdRecordDto.getcCode() + "|" + irowno);	
					if(StringUtil.isBlank(ckmxDto_t.getKwbhcsdm())) {
						rdRecordsDto.setIposflag(null);
					}else {
						rdRecordsDto.setIposflag("1");
					}
					irowno = irowno + 1;
					rdRecordsList.add(rdRecordsDto);

					//组装记账记录表
					IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto = new IA_ST_UnAccountVouchDto();
					iA_ST_UnAccountVouchDto.setIDUN(String.valueOf(rdRecordsDto.getID()));
					iA_ST_UnAccountVouchDto.setIDSUN(String.valueOf(rdRecordsDto.getAutoID()));
					iA_ST_UnAccountVouchDto.setcVouTypeUN("11");
					iA_ST_UnAccountVouchDto.setcBUstypeUN("领料");
					iA_ST_UnAccountVouchDtos.add(iA_ST_UnAccountVouchDto);
					
					//组装U8领料数据
					MaterialAppVouchsDto materialAppVouchsDto = new MaterialAppVouchsDto();
					materialAppVouchsDto.setfOutQuantity(ckmxDto_t.getCksl()); //出库数量
					materialAppVouchsDto.setAutoID(ckmxDto_t.getGlid()); //领料主键id
					materialAppVouchsList.add(materialAppVouchsDto);
					
					//处理货位调整表
					if(StringUtil.isNotBlank(ckmxDto_t.getKwbhcsdm())) {
						InvPositionDto invPositionDto_out = new InvPositionDto();
						invPositionDto_out.setRdsID(String.valueOf(rdRecordsDto.getAutoID()));
						invPositionDto_out.setRDID(String.valueOf(rdRecordDto.getID()));
						invPositionDto_out.setcWhCode(ckglDto.getCkdm());
						invPositionDto_out.setcPosCode(ckmxDto_t.getKwbhcsdm());
						invPositionDto_out.setcInvCode(ckmxDto_t.getWlbm());
						invPositionDto_out.setcBatch(StringUtil.isNotBlank(ckmxDto_t.getScph())?ckmxDto_t.getScph():"");
						invPositionDto_out.setdVDate(StringUtil.isNotBlank(ckmxDto_t.getYxq())?ckmxDto_t.getYxq():null);
						invPositionDto_out.setiQuantity(ckmxDto_t.getCksl());
						invPositionDto_out.setcHandler(llglDto.getZsxm());
						invPositionDto_out.setdDate(sdf_t.format(date));
						invPositionDto_out.setbRdFlag("0");
						invPositionDto_out.setdMadeDate(StringUtil.isNotBlank(ckmxDto_t.getScrq())?ckmxDto_t.getScrq():null);
						if (("0").equals(ckmxDto_t.getBzqflg())) {
							invPositionDto_out.setcMassUnit("2");
							invPositionDto_out.setiMassDate(ckmxDto_t.getBzq()); // 有效期数
						} else {
							invPositionDto_out.setcMassUnit("1");
							invPositionDto_out.setiMassDate("99"); // 有效期数
						}
						if(StringUtil.isBlank(ckmxDto_t.getScrq())) {
							invPositionDto_out.setcMassUnit("0");
							invPositionDto_out.setiMassDate(null); // 有效期数
						}
						invPositionDto_out.setCvouchtype("11");
						Date date_t_o = new Date();
						invPositionDto_out.setdVouchDate(sdf.format(date_t_o));
						invPositionDtos.add(invPositionDto_out);
						
						//组装货位总数信息
						InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
						invPositionSumDto.setcWhCode(ckglDto.getCkdm());
						invPositionSumDto.setcPosCode(ckmxDto_t.getKwbhcsdm());
						invPositionSumDto.setcInvCode(ckmxDto_t.getWlbm());
						invPositionSumDto.setcBatch(StringUtil.isNotBlank(ckmxDto_t.getScph())?ckmxDto_t.getScph():"");
						//判断是否存在  仓库，物料编码，库位，追溯号一样的数据
						InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
						if(invPositionSumDto_t!=null) {
							//更新货物货物数量
							invPositionSumDto_t.setJsbj("1"); //计算标记,相减
							invPositionSumDto_t.setiQuantity(ckmxDto_t.getCksl());
							invPositionSumDtos_mod.add(invPositionSumDto_t);
						}else {
							throw new BusinessException("msg","未找到出库库位货物信息！");
						}
					}			
				}
			}
			
			//处理库存量
			CurrentStockDto curr = new CurrentStockDto();
			curr.setIds(cinvcodes);
			List<CurrentStockDto> currentStockDtos = currentStockDao.queryBycInvCode(curr);
			List<CkmxDto> ckmxs = ckmxService.getDtoGroupBy(ckglDto.getCkid());
			StringBuilder message = new StringBuilder();
			StringBuilder message_t = new StringBuilder();
			for (CkmxDto ckmxDto_s : ckmxs) {
				boolean flg = false;
				for (CurrentStockDto currentStockDto_t : currentStockDtos) {
					if(StringUtil.isNotBlank(ckmxDto_s.getScph()) && StringUtil.isNotBlank(currentStockDto_t.getcBatch())) {
						if(ckmxDto_s.getWlbm().equals(currentStockDto_t.getcInvCode()) && ckmxDto_s.getCkdm().equals(currentStockDto_t.getcWhCode()) && ckmxDto_s.getScph().equals(currentStockDto_t.getcBatch())) {
							BigDecimal newiQuantity=new BigDecimal(currentStockDto_t.getiQuantity()) .subtract(new BigDecimal(ckmxDto_s.getZcksl()));
							if(newiQuantity.compareTo(BigDecimal.ZERO) < 0) {
								message_t.append(",").append(ckmxDto_s.getWlbm()).append("(").append(ckmxDto_s.getScph()).append(")");
							}
							CurrentStockDto currentStockDto = new CurrentStockDto();
							currentStockDto.setiQuantity(ckmxDto_s.getZcksl()); //出库数量
							currentStockDto.setcInvCode(ckmxDto_s.getWlbm()); //物料编码
							currentStockDto.setcWhCode(ckmxDto_s.getCkdm()); //仓库代码
							currentStockDto.setcBatch(ckmxDto_s.getScph()); //生产批号
							currentStockList.add(currentStockDto);
							flg = true;
						}
					}
					if(StringUtil.isBlank(ckmxDto_s.getScph()) && StringUtil.isBlank(currentStockDto_t.getcBatch())) {
						if(ckmxDto_s.getWlbm().equals(currentStockDto_t.getcInvCode()) && ckmxDto_s.getCkdm().equals(currentStockDto_t.getcWhCode()) ) {
							BigDecimal newiQuantity=new BigDecimal(currentStockDto_t.getiQuantity()) .subtract(new BigDecimal(ckmxDto_s.getZcksl()));
							if(newiQuantity.compareTo(BigDecimal.ZERO) < 0) {
								message_t.append(",").append(ckmxDto_s.getWlbm()).append("(").append(ckmxDto_s.getScph()).append(")");
							}
							CurrentStockDto currentStockDto = new CurrentStockDto();
							currentStockDto.setiQuantity(ckmxDto_s.getZcksl()); //出库数量
							currentStockDto.setcInvCode(ckmxDto_s.getWlbm()); //物料编码
							currentStockDto.setcWhCode(ckmxDto_s.getCkdm()); //仓库代码
							currentStockDto.setcBatch(""); //生产批号
							currentStockList.add(currentStockDto);
							flg = true;
						}
					}
					
				}
				if(!flg) {
					message.append(",").append(ckmxDto_s.getWlbm()).append("(").append(ckmxDto_s.getScph()).append(")");
				}
			}
			if(StringUtil.isNotBlank(message.toString())) {
				message = new StringBuilder(message.substring(1));
				throw new BusinessException("msg","U8库存未找到"+message+"!");
			}
			if(StringUtil.isNotBlank(message_t.toString())) {
				message_t = new StringBuilder(message_t.substring(1));
				throw new BusinessException("msg","U8库存不足，不允许出库"+message_t+"！");
			}
			
			//更新U8库存
			int result_kc;
			if(currentStockList.size()>10) {
				int length = (int)Math.ceil((double)currentStockList.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<CurrentStockDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = currentStockList.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(currentStockList.get(j));
					}
					result_kc = currentStockDao.updateList(list);
					if(result_kc<1) {
						throw new BusinessException("msg", "批量更新U8库存失败！");
					}
				}
			}else{
				result_kc = currentStockDao.updateList(currentStockList);
				if(result_kc<1) {
					throw new BusinessException("msg","批量更新U8库存失败！");
				}
			}
			
			// 更新最大值表
			boolean result_ui = false;
			//判断是新增还是修改
			if(uA_IdentityDto_t!=null) {
				//更新
				uA_IdentityDto_t.setiChildId(String.valueOf(childid));
				result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
			}
			if(!result_ui) {
				uA_IdentityDto.setiChildId(String.valueOf(childid));
				result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
			}
			if(!result_ui) {
				throw new BusinessException("msg", "最大id值更新失败!");
			}
		
			//U8新增出库明细
			//若List过大进行截断,10个明细为一组进行添加
			if(rdRecordsList.size()>10){
				int length=(int)Math.ceil((double)rdRecordsList.size()/10);//向上取整
				for(int i = 0; i<length; i++){
					List<RdRecordsDto> list=new ArrayList<>();
					int t_length;
					if(i!=length-1){
						t_length=(i+1)*10;
					}else{
						t_length=rdRecordsList.size();
					}
					for(int j=i*10;j<t_length;j++){
						list.add(rdRecordsList.get(j));
					}
					int result_rd  = rdRecords11Dao.insertList(list);
					if(result_rd<1)
						throw new BusinessException("msg","批量更新U8出库明细失败！");
				}
			}else{
				int result_rd = rdRecords11Dao.insertList(rdRecordsList);
				if(result_rd<1) {
					throw new BusinessException("msg","批量更新U8出库明细失败！");
				}
			}
			
			if(invPositionDtos.size()>0) {
				int result_hw;
				if(invPositionDtos.size()>10) {
					int length = (int)Math.ceil((double)invPositionDtos.size() / 10);//向上取整
					for (int i = 0; i < length; i++) {
						List<InvPositionDto> list = new ArrayList<>();
						int t_length;
						if (i != length - 1) {
							t_length = (i + 1) * 10;
						} else {
							t_length = invPositionDtos.size();
						}
						for (int j = i * 10; j < t_length; j++) {
							list.add(invPositionDtos.get(j));
						}
						result_hw = invPositionDao.insertDtos(list);
						if (result_hw < 1)
							throw new BusinessException("msg", "新增U8货位调整失败！");
					}
				}else{
					result_hw = invPositionDao.insertDtos(invPositionDtos);
					if (result_hw < 1)
						throw new BusinessException("msg", "新增U8货位调整失败！");
				}
			}			
			
			//修改货物总数信息
			if(invPositionSumDtos_mod.size()>0) {
				//若List过大进行截断,10个明细为一组进行添加
				int result_mod;
				if(invPositionSumDtos_mod.size()>10) {
					int length = (int)Math.ceil((double)invPositionSumDtos_mod.size() / 10);//向上取整
					for (int i = 0; i < length; i++) {
						List<InvPositionSumDto> list = new ArrayList<>();
						int t_length;
						if (i != length - 1) {
							t_length = (i + 1) * 10;
						} else {
							t_length = invPositionSumDtos_mod.size();
						}
						for (int j = i * 10; j < t_length; j++) {
							list.add(invPositionSumDtos_mod.get(j));
						}
						result_mod = invPositionSumDao.updateDtos(list);
						if(result_mod<1) {
							throw new BusinessException("msg", "更新U8货位总数失败！");
						}
					}
				}else{
					result_mod = invPositionSumDao.updateDtos(invPositionSumDtos_mod);
					if(result_mod<1) {
						throw new BusinessException("msg", "更新U8货位总数失败！");
					}
				}
			}

			//更新U8领料单
			MaterialAppVouchDto materialAppVouchDto = new MaterialAppVouchDto();
			materialAppVouchDto.setID(llglDto.getGlid());
			int result_ma = materialAppVouchDao.updateDto(materialAppVouchDto);
			if(result_ma<1) {
				throw new BusinessException("msg","批量更新U8领料信息失败！");
			}
			if(materialAppVouchsList.size()>10) {
				int length = (int)Math.ceil((double)materialAppVouchsList.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<MaterialAppVouchsDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = materialAppVouchsList.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(materialAppVouchsList.get(j));
					}
					result_ma = materialAppVouchsDao.updateList(list);
					if(result_ma<1) {
						throw new BusinessException("msg", "批量更新U8领料信息失败！");
					}
				}
			}else{
				result_ma = materialAppVouchsDao.updateList(materialAppVouchsList);
				if(result_ma<1) {
					throw new BusinessException("msg", "批量更新U8领料信息失败！");
				}
			}

			boolean result_av = false;
			if(iA_ST_UnAccountVouchDtos.size()>10) {
				int length = (int)Math.ceil((double)iA_ST_UnAccountVouchDtos.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<IA_ST_UnAccountVouchDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = iA_ST_UnAccountVouchDtos.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(iA_ST_UnAccountVouchDtos.get(j));
					}
					result_av = addAccountVs(list,"11");
					if(!result_av) {
						throw new BusinessException("msg","新增记账信息失败！");
					}
				}
			}else{
				result_av = addAccountVs(iA_ST_UnAccountVouchDtos,"11");
				if(!result_av) {
					throw new BusinessException("msg","新增记账信息失败！");
				}
			}
		}
		map.put("ckmxList", ckmxList_mx);
		map.put("ckglList", ckglList);
		return map;
	}
		

	/**
	 * 成品出库其他入库新增U8做出库(OA到货新增)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8CprkData(DhxxDto dhxxDto,List<DhxxDto> dhxxDtos,boolean flag) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		List<HwxxDto> hwxxs = hwxxDao.getListByDhid(dhxxDto.getDhid());

		//List<DhxxDto> dhxxDtos = dhxxService.getDtoList(dhxxDto);
		RdRecordDto rdRecordDto = new RdRecordDto();
		if("c".equals(dhxxDtos.get(0).getRklxdm())) {
			VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
			voucherHistoryDto.setCardNumber("0411");
			//获取最大流水号
			VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
			if(voucherHistoryDto_t!=null) {
				String serialnum = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
				String serial = "0000000000"+ serialnum;
				serial = serial.substring(serial.length()-10);
				rdRecordDto.setcCode(serial); // 到货单号
				rdRecordDto.setSflxbj("10");
				//判断单号是否存在
				int ccode_new=0;
				RdRecordDto rdRecordDtos_t = rdRecordDao.queryByCode(rdRecordDto);			
				if(rdRecordDtos_t!=null) {
					List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(rdRecordDto);
					ccode_new = Integer.parseInt(rdRecordDto.getcCode()) + 1;
					for (RdRecordDto rdRecordDto10 : rdList) {
						if(ccode_new-Integer.parseInt(rdRecordDto10.getcCode())==0) {
							ccode_new = ccode_new+1;
						}else {
							break;
						}
					}				
				}
				if(ccode_new!=0) {
		        	rdRecordDto.setcCode(("000000000"+ccode_new).substring(serial.length()-10,serial.length())); // 到货单号
		        	voucherHistoryDto_t.setcNumber(Integer.toString(ccode_new)); 
		        }else {
		        	voucherHistoryDto_t.setcNumber(serialnum);
		        }    
				//更新最大单号值		
				int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
				if(result_ccode<1)
					throw new BusinessException("msg", "更新U8最大单号失败！");	
			}else {
				voucherHistoryDto.setcNumber("1");
				voucherHistoryDto.setbEmpty("0");
				rdRecordDto.setcCode("0000000001"); // 到货单号
				//单号最大表增加数据
				int result_ccode = voucherHistoryDao.insert(voucherHistoryDto);
				if(result_ccode<1)
					throw new BusinessException("msg", "新增U8最大单号失败！");
			}
		

		}
		if("3".equals(dhxxDtos.get(0).getRklxdm())) {
			SimpleDateFormat sdf_d = new SimpleDateFormat("yyyyMM");
			Date date = new Date();
			String prefix = sdf_d.format(date);
			VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
			voucherHistoryDto.setcSeed(prefix);
			voucherHistoryDto.setCardNumber("0301");
			//获取最大流水号 210900053
			VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
			if(voucherHistoryDto_t!=null) {
				String serial = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
				rdRecordDto.setcCode(prefix.substring(2)+"000"+(serial.length()>1?serial:"0"+serial)); // 到货单号
				rdRecordDto.setPrefix(prefix.substring(2));
				rdRecordDto.setSflxbj("8");
				RdRecordDto rdRecordDtos_t = rdRecordDao.queryByCode(rdRecordDto);
				int ccode_new=0;
		        if (rdRecordDtos_t != null ) {
		            List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(rdRecordDto);
		            ccode_new = Integer.parseInt(rdRecordDto.getcCode()) + 1;
		            for (RdRecordDto rdRecordDto_c : rdList) {
						if(ccode_new-Integer.parseInt(rdRecordDto_c.getcCode())==0) {
							ccode_new = ccode_new+1;
						}else {
							break;
						}
					}
		        }
		        if(ccode_new!=0) {
		        	rdRecordDto.setcCode(Integer.toString(ccode_new)); // 到货单号
		        	voucherHistoryDto_t.setcNumber(Integer.parseInt(Integer.toString(ccode_new).substring(4))+""); //去除多余0
		        }else {
		        	voucherHistoryDto_t.setcNumber(serial);
		        }        
				//更新最大单号值		
				int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
				if(result_ccode<1)
					throw new BusinessException("msg", "更新U8最大单号失败！");
			}else {
				voucherHistoryDto.setiRdFlagSeed("1");
				voucherHistoryDto.setcContent("日期");
				voucherHistoryDto.setcContentRule("月");
				voucherHistoryDto.setbEmpty("0");
				voucherHistoryDto.setcNumber("1");
				rdRecordDto.setcCode(prefix+"00001");
				//单号最大表增加数据
				int result_ccode = voucherHistoryDao.insert(voucherHistoryDto);
				if(result_ccode<1)
					throw new BusinessException("msg", "新增U8最大单号失败！");
			}
		}
		if("ZP".equals(dhxxDtos.get(0).getRklxdm())) {
			SimpleDateFormat sdf_d = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			String prefix = sdf_d.format(date);
			VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
			voucherHistoryDto.setcSeed(prefix);
			voucherHistoryDto.setCardNumber("24");
			//获取最大流水号 210900053
			VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
			if(voucherHistoryDto_t!=null) {
				String serial = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
				rdRecordDto.setcCode(prefix+(serial.length()>1?serial:"0"+serial)); // 到货单号
				rdRecordDto.setPrefix(prefix);
				rdRecordDto.setSflxbj("1");
				List<RdRecordDto> rdRecordDtos_t = rdRecordDao.getDtoList(rdRecordDto);
				int ccode_new=0;
				if (rdRecordDtos_t != null && rdRecordDtos_t.size() > 0) {
					List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(rdRecordDto);
					ccode_new = Integer.parseInt(rdRecordDto.getcCode()) + 1;
					for (RdRecordDto rdRecordDto_c : rdList) {
						if(ccode_new-Integer.parseInt(rdRecordDto_c.getcCode())==0) {
							ccode_new = ccode_new+1;
						}else {
							break;
						}
					}
				}
				if(ccode_new!=0) {
					rdRecordDto.setcCode(Integer.toString(ccode_new)); // 到货单号
					voucherHistoryDto_t.setcNumber(Integer.parseInt(Integer.toString(ccode_new).substring(8))+""); //去除多余0
				}else {
					voucherHistoryDto_t.setcNumber(serial);
				}
				//更新最大单号值
				int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
				if(result_ccode<1)
					throw new BusinessException("msg", "更新U8最大单号失败！");

			}else {
				voucherHistoryDto.setiRdFlagSeed("1");
				voucherHistoryDto.setcContent("日期");
				voucherHistoryDto.setcContentRule("日");
				voucherHistoryDto.setbEmpty("0");
				voucherHistoryDto.setcNumber("1");
				rdRecordDto.setcCode(prefix+"01");
				//单号最大表增加数据
				int result_ccode = voucherHistoryDao.insert(voucherHistoryDto);
				if(result_ccode<1)
					throw new BusinessException("msg", "新增U8最大单号失败！");
			}
		}

		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); // 存入账套
		uA_IdentityDto.setcVouchType("rd"); // 存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); // 获取主键最大值
		if(uA_IdentityDto_t!=null) {
			if (StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				uA_IdentityDto_t.setiFatherId(String.valueOf(id + 1));
				rdRecordDto.setID(id + 1000000001);
			}
		}else {
			rdRecordDto.setID(1000000001);
			uA_IdentityDto.setiFatherId("1");
		}
		
		//成品入库新增
		if("c".equals(dhxxDtos.get(0).getRklxdm())) {
			rdRecordDto.setcVouchType("10");	
			rdRecordDto.setbRdFlag("1");
			rdRecordDto.setcBusType("成品入库");
			rdRecordDto.setcSource("库存");
			rdRecordDto.setCsysbarcode("||st10|"+rdRecordDto.getcCode());
		}
		//其他入库新增
		if("3".equals(dhxxDtos.get(0).getRklxdm())) {
			rdRecordDto.setcVouchType("08");		
			rdRecordDto.setbRdFlag("1");
			rdRecordDto.setcBusType("其他入库");
			rdRecordDto.setcSource("库存");
			rdRecordDto.setCsysbarcode("||st08|"+rdRecordDto.getcCode());
		}

		//其他入库新增
		if("ZP".equals(dhxxDtos.get(0).getRklxdm())) {
			rdRecordDto.setcVouchType("01");
			rdRecordDto.setbRdFlag("1");
			rdRecordDto.setcPTCode("01");
			rdRecordDto.setcBusType("普通采购");
			rdRecordDto.setcSource("库存");
			rdRecordDto.setcVenCode(dhxxDtos.get(0).getGysdm()); // 供应商代码
			rdRecordDto.setCsysbarcode("||st01|"+rdRecordDto.getcCode());
			rdRecordDto.setiTaxRate("0"); // 税率
			rdRecordDto.setbCredit("0");
			rdRecordDto.setcExch_Name("人民币"); // 币种
			rdRecordDto.setiExchRate("0");//汇率
		}
		rdRecordDto.setcWhCode(dhxxDtos.get(0).getCkdm()); //仓库代码
		rdRecordDto.setdDate(dhxxDtos.get(0).getDhrq()); //入库日期
		rdRecordDto.setcRdCode(dhxxDtos.get(0).getRklxdm()); //入库类别代码
		//判断是否是医检所系统，如果是，收发类别用其他入库
		if ("/labigams".equals(urlPrefix)) {
			rdRecordDto.setcRdCode("3");
		}
		rdRecordDto.setcDepCode(dhxxDtos.get(0).getJgdm()); //部门代码
		rdRecordDto.setcHandler(dhxxDto.getZsxm()); //审核人
		rdRecordDto.setcMaker(dhxxDtos.get(0).getLrrymc()); //制单人
		rdRecordDto.setcMemo(StringUtil.isNotBlank(dhxxDtos.get(0).getBz())?(dhxxDtos.get(0).getBz().length()>255?dhxxDtos.get(0).getBz().substring(0,255):dhxxDtos.get(0).getBz()):null); //备注
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 
		SimpleDateFormat sdf_h = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		rdRecordDto.setdVeriDate(sdf.format(date)); //审核日期
		rdRecordDto.setDnmaketime(sdf_h.format(date)); //制单日期
		rdRecordDto.setDnverifytime(sdf_h.format(date)); //审核日期
		if("c".equals(dhxxDtos.get(0).getRklxdm())) {
			int result = rdRecordDao.insertRd10(rdRecordDto);
			if(result<1) {
				throw new BusinessException("msg","新增U8成品入库信息失败！");
			}
		}
		if("ZP".equals(dhxxDtos.get(0).getRklxdm())) {
			int result = rdRecordDao.insert(rdRecordDto);
			if (result < 1) {
				throw new BusinessException("msg", "入库数据存入U8失败！");
			}
		}
		if("3".equals(dhxxDtos.get(0).getRklxdm())) {
			int result = rdRecordDao.insertQtrk08(rdRecordDto);
			if(result<1) {
				throw new BusinessException("msg","新增U8其他入库信息失败！");
			}
		}
		// 存入关联id
		int rdRecordId = rdRecordDto.getID();
		dhxxDto.setGlid(String.valueOf(rdRecordId));
		map.put("dhxxDto", dhxxDto);		
		// 存货物信息
		List<HwxxDto> hwxxDtoList = new ArrayList<>();
		// 存批量更新U8入库明细数据
		List<RdRecordsDto> rdRecordsDtoList = new ArrayList<>();
		// 存物料编码，用于查库存
		StringBuilder ids = new StringBuilder();
		int irowno = 1;
		int id_s = 0;
		if(uA_IdentityDto_t!=null) {
			id_s = Integer.parseInt(uA_IdentityDto_t.getiChildId());
		}
		//存货位调整信息
		List<InvPositionDto> invPositionDtos = new ArrayList<>();
		//存记账记录数据（成品入库）
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos_10 = new ArrayList<>();
		//存记账记录数据（赠品入库）
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos_01 = new ArrayList<>();
		//存记账记录数据（其他入库）
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos_08 = new ArrayList<>();
		for (HwxxDto hwxxDto : hwxxs) {
			//判断类别是不是ABC类
			if("1".equals(hwxxDto.getLbbj()) || flag) {
				ids.append(",").append(hwxxDto.getWlbm());
				RdRecordsDto rdRecordsDto = new RdRecordsDto();
				rdRecordsDto.setAutoID(id_s + 1000000001);
				id_s = id_s + 1;
				rdRecordsDto.setID(rdRecordDto.getID()); // 关联id
				rdRecordsDto.setcInvCode(hwxxDto.getWlbm()); // 物料编码
				rdRecordsDto.setiQuantity(hwxxDto.getSl()); // 数量
				rdRecordsDto.setcBatch(hwxxDto.getScph()); // 生产批号
				rdRecordsDto.setdVDate(hwxxDto.getYxq());// 失效日期
				rdRecordsDto.setcPosition(hwxxDto.getKwbhdm()); // 库位
				rdRecordsDto.setdMadeDate(hwxxDto.getScrq());// 生产日期
				rdRecordsDto.setIrowno(String.valueOf(irowno));
				rdRecordsDto.setCbsysbarcode(rdRecordDto.getCsysbarcode() + "|" + irowno);
				irowno = irowno + 1;
				rdRecordsDto.setCbMemo(StringUtil.isNotBlank(hwxxDto.getDhbz())?(hwxxDto.getDhbz().length()>255?hwxxDto.getDhbz().substring(0,255):hwxxDto.getDhbz()):null); //到货备注
				if (("0").equals(hwxxDto.getBzqflg())) {
					rdRecordsDto.setcMassUnit("2"); // 有效单位
					rdRecordsDto.setiMassDate(hwxxDto.getBzq()); // 有效期数
				} else {
					rdRecordsDto.setcMassUnit("1"); // 有效单位
					rdRecordsDto.setiMassDate("99"); // 有效期数
				}
				if("3".equals(dhxxDtos.get(0).getRklxdm())) {
					rdRecordsDto.setcDefine22(hwxxDto.getZsh()); //追溯号
					rdRecordsDto.setiNQuantity(hwxxDto.getSl());//数量
				}
				if("ZP".equals(dhxxDtos.get(0).getRklxdm())) {
					rdRecordsDto.setcDefine22(hwxxDto.getZsh()); //追溯号
					rdRecordsDto.setiNQuantity(hwxxDto.getSl());//数量
					rdRecordsDto.setChVencode(hwxxDto.getGysdm());// 供应商代码
					rdRecordsDto.setiSQuantity("0");
					rdRecordsDto.setiSNum("0");
					rdRecordsDto.setiMoney("0");
					rdRecordsDto.setiTaxRate("0"); // 税率
					rdRecordsDto.setbTaxCost("0");
					rdRecordsDto.setbVMIUsed("0");
				}

				rdRecordsDtoList.add(rdRecordsDto);
				
				// 存入关联id
				HwxxDto hwxx_list = new HwxxDto();
				int hwglid = rdRecordsDto.getAutoID();
				hwxx_list.setGlid(String.valueOf(hwglid));
				hwxx_list.setU8rkdh(rdRecordDto.getcCode());
				hwxx_list.setHwid(hwxxDto.getHwid());
				hwxx_list.setXgry(dhxxDto.getXgry());
				hwxxDtoList.add(hwxx_list);
				map.put("hwxxDtoList", hwxxDtoList);
				
				IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto = new IA_ST_UnAccountVouchDto();
				iA_ST_UnAccountVouchDto.setIDUN(String.valueOf(rdRecordsDto.getID()));
				iA_ST_UnAccountVouchDto.setIDSUN(String.valueOf(rdRecordsDto.getAutoID()));
				//处理货位调整表
				InvPositionDto invPositionDto = new InvPositionDto();
				invPositionDto.setRdsID(String.valueOf(rdRecordsDto.getAutoID()));
				invPositionDto.setRDID(String.valueOf(rdRecordDto.getID()));
				invPositionDto.setcWhCode(hwxxDto.getCkdm());
				invPositionDto.setcPosCode(hwxxDto.getKwbhdm());
				invPositionDto.setcInvCode(hwxxDto.getWlbm());
				invPositionDto.setcBatch(hwxxDto.getScph());
				invPositionDto.setdVDate(hwxxDto.getYxq());
				invPositionDto.setiQuantity(hwxxDto.getSl());
				invPositionDto.setcHandler(dhxxDto.getZsxm());
				invPositionDto.setdDate(hwxxDto.getDhrq());
				if (("0").equals(hwxxDto.getBzqflg())) {
					invPositionDto.setcMassUnit("2"); // 有效单位
					invPositionDto.setiMassDate(hwxxDto.getBzq()); // 有效期数
				} else {
					invPositionDto.setcMassUnit("1"); // 有效单位
					invPositionDto.setiMassDate("99"); // 有效期数
				}
				if("c".equals(dhxxDtos.get(0).getRklxdm())) {
					invPositionDto.setbRdFlag("1");
					invPositionDto.setCvouchtype("10");
					iA_ST_UnAccountVouchDto.setcVouTypeUN("10");
					iA_ST_UnAccountVouchDto.setcBUstypeUN("成品入库");
					iA_ST_UnAccountVouchDtos_10.add(iA_ST_UnAccountVouchDto);
				}
				if("ZP".equals(dhxxDtos.get(0).getRklxdm())) {
					invPositionDto.setbRdFlag("1");
					invPositionDto.setCvouchtype("01");
					iA_ST_UnAccountVouchDto.setcVouTypeUN("01");
					iA_ST_UnAccountVouchDto.setcBUstypeUN("普通采购");
					iA_ST_UnAccountVouchDtos_01.add(iA_ST_UnAccountVouchDto);
				}
				if("3".equals(dhxxDtos.get(0).getRklxdm())) {
					invPositionDto.setbRdFlag("1");
					invPositionDto.setCvouchtype("08");
					iA_ST_UnAccountVouchDto.setcVouTypeUN("08");
					iA_ST_UnAccountVouchDto.setcBUstypeUN("其他入库");
					iA_ST_UnAccountVouchDtos_08.add(iA_ST_UnAccountVouchDto);
				}
				invPositionDto.setdMadeDate(hwxxDto.getScrq());
				SimpleDateFormat sdf_t = new SimpleDateFormat("yyyy-MM-dd");
				Date date_t = new Date();
				invPositionDto.setdVouchDate(sdf_t.format(date_t));
				invPositionDtos.add(invPositionDto);	
			}
		}

		// 更新U8入库明细表
		if("c".equals(dhxxDtos.get(0).getRklxdm())) {
			int result_rds = rdRecordsDao.insertRds10(rdRecordsDtoList);
			if (result_rds < 1)
				throw new BusinessException("msg", "更新U8入库明细表失败！");
		}
		// 更新U8入库明细表
		if("ZP".equals(dhxxDtos.get(0).getRklxdm())) {
			int result_rds = rdRecordsDao.insertRds(rdRecordsDtoList);
			if (result_rds < 1)
				throw new BusinessException("msg", "更新U8入库明细表失败！");
		}
		if("3".equals(dhxxDtos.get(0).getRklxdm())) {
			int result_rds = rdRecordsDao.insertRds08(rdRecordsDtoList);
			if (result_rds<1)
				throw new BusinessException("msg", "更新U8入库明细表失败！");
		}
		// 更新最大值表
		boolean result_ui = false;
		//判断是新增还是修改
		if(uA_IdentityDto_t!=null) {
			//更新
			uA_IdentityDto_t.setiChildId(String.valueOf(id_s));
			result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
		}
		if(!result_ui) {
			uA_IdentityDto.setiChildId(String.valueOf(id_s));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}

		int result_hw = invPositionDao.insertDtos(invPositionDtos);
		if(result_hw<1) {
			throw new BusinessException("msg", "新增U8货位调整失败！");
		}
		
		//新增记账记录
		if(iA_ST_UnAccountVouchDtos_08.size()>0) {
			boolean result_av = addAccountVs(iA_ST_UnAccountVouchDtos_08,"08");
			if(!result_av)
				throw new BusinessException("msg", "新增其他入库记账记录信息失败！");
		}
		//新增记账记录
		if(iA_ST_UnAccountVouchDtos_01.size()>0) {
			boolean result_av = addAccountVs(iA_ST_UnAccountVouchDtos_01,"01");
			if(!result_av)
				throw new BusinessException("msg", "新增其他入库记账记录信息失败！");
		}
		if(iA_ST_UnAccountVouchDtos_10.size()>0) {
			boolean result_av_t = addAccountVs(iA_ST_UnAccountVouchDtos_10,"10");
			if(!result_av_t)
				throw new BusinessException("msg", "新增成品入库记账记录信息失败！");
		}	
		// 根据生产批号号物料分组
		List<HwxxDto> hwxxList = hwxxService.queryByDhid(dhxxDto.getDhid());
		HwxxDto scphHwxx = new HwxxDto();
		scphHwxx.setDhid(dhxxDto.getDhid());
		List<HwxxDto> scphList = hwxxService.queryBycBatch(scphHwxx);
		//新增U8数据
		boolean result_bp = addBatchPs(scphList);
		if(!result_bp)
			throw new BusinessException("msg", "新增U8数据（AA_BatchProperty）失败！");
		
		// 获取U8已经存在得库存
		ids = new StringBuilder(ids.substring(1));
		CurrentStockDto currentStockDto_t = new CurrentStockDto();
		currentStockDto_t.setIds(ids.toString());
		List<CurrentStockDto> currentStockDtos_t = currentStockDao.queryBycInvCode(currentStockDto_t);
		// 存要更新得货物id,更新库存关联id
		List<HwxxDto> hwxxDto_hs = new ArrayList<>();
		// 存入批量更新得U8库存数据
		List<CurrentStockDto> currentStockDtos = new ArrayList<>();
		//存货位货物总数信息
		List<InvPositionSumDto> invPositionSumDtos_add = new ArrayList<>();
		List<InvPositionSumDto> invPositionSumDtos_mod = new ArrayList<>();
		for (HwxxDto hwxxDto_list : hwxxList) {
			for (HwxxDto hwxx_hwxxs : hwxxs) {
				//判断类别是不是ABC类
				if("1".equals(hwxx_hwxxs.getLbbj()) || flag) {
					if (hwxxDto_list.getScph().equals(hwxx_hwxxs.getScph())
							&& hwxxDto_list.getWlid().equals(hwxx_hwxxs.getWlid())) {
						// 判断U8是否存在该物料
						if (currentStockDtos_t.size() > 0) {
							boolean modflg = true;
							for (CurrentStockDto current_t : currentStockDtos_t) {
								if (dhxxDtos.get(0).getCkdm().equals(current_t.getcWhCode())
										&& hwxx_hwxxs.getWlbm().equals(current_t.getcInvCode())
										&& hwxx_hwxxs.getScph().equals(current_t.getcBatch())) {
									// 如果仓库，物料编码和生产批号都相同，就修改库存数量
									CurrentStockDto currentStockDto_cu = new CurrentStockDto();
									double sl = Double
											.parseDouble(StringUtils.isNotBlank(hwxxDto_list.getU8rksl())
													? hwxxDto_list.getU8rksl()
													: "0")
											+ Double.parseDouble(StringUtils.isNotBlank(current_t.getiQuantity())
													? current_t.getiQuantity()
													: "0");
									currentStockDto_cu.setiQuantity(Double.toString(sl)); // 库存数量
									currentStockDto_cu.setAutoID(current_t.getAutoID()); // 存入主键id
									currentStockDto_cu.setcWhCode(current_t.getcWhCode());
									currentStockDto_cu.setcBatch(current_t.getcBatch());
									currentStockDto_cu.setcInvCode(current_t.getcInvCode());
									currentStockDtos.add(currentStockDto_cu);
									
									// 存入关联id
									HwxxDto hwxx_hs = new HwxxDto();
									hwxx_hs.setWlid(hwxx_hwxxs.getWlid());
									hwxx_hs.setScph(hwxx_hwxxs.getScph());
	                                hwxx_hs.setDhid(hwxx_hwxxs.getDhid());
									hwxx_hs.setKcglid(String.valueOf(currentStockDto_cu.getAutoID())); // 存入关联id
									hwxxDto_hs.add(hwxx_hs);
									modflg = false;
									break;
								}
							}
							if (modflg) {
								// 新增得U8库存数据
								CurrentStockDto current_add = new CurrentStockDto();
								current_add.setcInvCode(hwxx_hwxxs.getWlbm()); // 物料编码
								current_add.setcWhCode(dhxxDtos.get(0).getCkdm());// 仓库编码
								boolean itemidflg = true;
								for (CurrentStockDto current_t : currentStockDtos_t) {
									if (hwxx_hwxxs.getWlbm().equals(current_t.getcInvCode())) {
										// 如果物料编码一样，追溯号不一样，ItemId字段相同
										current_add.setItemId(current_t.getItemId());
										itemidflg = false;
									}
								}

								if (itemidflg) {
									SCM_ItemDto sCM_ItemDto_c  = sCM_ItemDao.getDtoBycInvVode(current_add.getcInvCode());
									if(sCM_ItemDto_c!=null) {
										current_add.setItemId(sCM_ItemDto_c.getId());
									}else {
										// 取SCM_Item表中最大值
										int num_ItemId = sCM_ItemDao.getMaxItemId() + 1;
										current_add.setItemId(String.valueOf(num_ItemId));
										// 存入新增的物料
										SCM_ItemDto sCM_ItemDto = new SCM_ItemDto();
										sCM_ItemDto.setcInvCode(current_add.getcInvCode()); // 物料编码
										int result_scm = sCM_ItemDao.insert(sCM_ItemDto);
										if (result_scm < 1) {
											throw new BusinessException("msg", "sCM_Item新增失败！");
										}
									}
								}
								current_add.setiQuantity(hwxxDto_list.getU8rksl()); // 现存数量
								current_add.setcBatch(hwxx_hwxxs.getScph()); // 生产批号
								current_add.setdVDate(hwxx_hwxxs.getYxq()); // 失效日期
								current_add.setdMdate(hwxx_hwxxs.getScrq()); // 生产日期
								current_add.setiExpiratDateCalcu("0");
								if (("0").equals(hwxx_hwxxs.getBzqflg())) {
									current_add.setcMassUnit("2"); // 有效单位
									current_add.setiMassDate(hwxx_hwxxs.getBzq()); // 有效期数
								} else {
									current_add.setcMassUnit("1"); // 有效单位
									current_add.setiMassDate("99"); // 有效期数
								}
								int result_add = currentStockDao.insert(current_add);
								if (result_add < 1) {
									throw new BusinessException("msg", "U8库存新增失败！");
								}
								
								// 存入关联id
								HwxxDto hwxx_hs = new HwxxDto();
								hwxx_hs.setWlid(hwxx_hwxxs.getWlid());
								hwxx_hs.setScph(hwxx_hwxxs.getScph());
                                hwxx_hs.setDhid(hwxx_hwxxs.getDhid());
								hwxx_hs.setKcglid(String.valueOf(current_add.getAutoID())); // 存入关联id
								hwxxDto_hs.add(hwxx_hs);
							}
						} else {
							// 新增得U8库存数据
							CurrentStockDto current_add_t = new CurrentStockDto();
							current_add_t.setcInvCode(hwxx_hwxxs.getWlbm()); // 物料编码
							current_add_t.setcWhCode(dhxxDtos.get(0).getCkdm());// 仓库编码
							SCM_ItemDto sCM_ItemDto_c  = sCM_ItemDao.getDtoBycInvVode(hwxx_hwxxs.getWlbm());
							if(sCM_ItemDto_c!=null) {
								current_add_t.setItemId(sCM_ItemDto_c.getId());
							}else {
								// 存入新增的物料
								SCM_ItemDto sCM_ItemDto = new SCM_ItemDto();
								sCM_ItemDto.setcInvCode(current_add_t.getcInvCode()); // 物料编码
								int result_scm = sCM_ItemDao.insert(sCM_ItemDto);
								if (result_scm < 1) {
									throw new BusinessException("msg", "sCM_Item新增失败！");
								}
								SCM_ItemDto sCM_ItemDto_t  = sCM_ItemDao.getDtoBycInvVode(hwxx_hwxxs.getWlbm());
								current_add_t.setItemId(sCM_ItemDto_t.getId());
							}		
							current_add_t.setiQuantity(hwxxDto_list.getU8rksl()); // 现存数量
							current_add_t.setcBatch(hwxx_hwxxs.getScph()); // 生产批号
							current_add_t.setdVDate(hwxx_hwxxs.getYxq()); // 失效日期
							current_add_t.setdMdate(hwxx_hwxxs.getScrq()); // 生产日期
							current_add_t.setiExpiratDateCalcu("0");
							if (("0").equals(hwxx_hwxxs.getBzqflg())) {
								current_add_t.setcMassUnit("2"); // 有效单位
								current_add_t.setiMassDate(hwxx_hwxxs.getBzq()); // 有效期数
							} else {
								current_add_t.setcMassUnit("1"); // 有效单位
								current_add_t.setiMassDate("99"); // 有效期数
							}
							int result_add = currentStockDao.insert(current_add_t);
							if (result_add < 1) {
								throw new BusinessException("msg", "U8库存添加失败！");
							}
							
							// 存入关联id
							HwxxDto hwxx_hs = new HwxxDto();
							hwxx_hs.setWlid(hwxx_hwxxs.getWlid());
							hwxx_hs.setScph(hwxx_hwxxs.getScph());
                            hwxx_hs.setDhid(hwxx_hwxxs.getDhid());
							hwxx_hs.setKcglid(String.valueOf(current_add_t.getAutoID())); 
							hwxxDto_hs.add(hwxx_hs);
						}
						
						//处理货位货物总数数据
						InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
						invPositionSumDto.setcWhCode(dhxxDtos.get(0).getCkdm());
						invPositionSumDto.setcPosCode(hwxx_hwxxs.getKwbhdm());
						invPositionSumDto.setcInvCode(hwxx_hwxxs.getWlbm());
						invPositionSumDto.setcBatch(hwxx_hwxxs.getScph());
						//判断是否存在  仓库，物料编码，库位，生产批号一样的数据
						InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
						if(invPositionSumDto_t!=null) {
							//存在，更新货物货物数量
							invPositionSumDto_t.setJsbj("0"); //计算标记,相加
							invPositionSumDto_t.setiQuantity(hwxxDto_list.getU8rksl());
							invPositionSumDtos_mod.add(invPositionSumDto_t);
						}else {
							//不存在，做新增
							invPositionSumDto.setiQuantity(hwxxDto_list.getU8rksl());
							invPositionSumDto.setInum("0");					
							if (("0").equals(hwxx_hwxxs.getBzqflg())) {
								invPositionSumDto.setcMassUnit("2");
								invPositionSumDto.setiMassDate(hwxx_hwxxs.getBzq()); // 保质期
							} else {
								invPositionSumDto.setcMassUnit("1");
								invPositionSumDto.setiMassDate("99"); // 保质期
							}
							invPositionSumDto.setdMadeDate(hwxx_hwxxs.getScrq());
							invPositionSumDto.setdVDate(hwxx_hwxxs.getYxq());
							invPositionSumDtos_add.add(invPositionSumDto);
						}
						break;
					}
				}
			}
		}
		
		//更新库存
		if(currentStockDtos.size()>0) {
			boolean reulu_mod = currentStockDao.updateKwAndSl(currentStockDtos);
			if(!reulu_mod) {
				throw new BusinessException("msg","库存数量更新失败！");
			}
		}
		//新增货物总数信息
		if(invPositionSumDtos_add.size()>0) {
			int result_add = invPositionSumDao.insertDtos(invPositionSumDtos_add);
			if(result_add<1) {
				throw new BusinessException("msg", "新增U8货位总数失败！");
			}
		}
		
		//修改货物总数信息
		if(invPositionSumDtos_mod.size()>0) {
			int result_mod = invPositionSumDao.updateDtos(invPositionSumDtos_mod);
			if(result_mod<1) {
				throw new BusinessException("msg", "更新U8货位总数失败！");
			}
		}
		
		//更新库存关联id
		map.put("hwxxDto_hs", hwxxDto_hs);
		return map;	
	}

	/**
	 * 新增记账记录
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public boolean addAccountVs(List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos,String type) {
		int result_i = 0;
		//判断收发类型
		if("01".equals(type)) {
			result_i = iA_ST_UnAccountVouchDao.insertAccountV01s(iA_ST_UnAccountVouchDtos);
		}else if("08".equals(type)) {
			result_i = iA_ST_UnAccountVouchDao.insertAccountV08s(iA_ST_UnAccountVouchDtos);
		}else if("09".equals(type)) {
			result_i = iA_ST_UnAccountVouchDao.insertAccountV09s(iA_ST_UnAccountVouchDtos);
		}else if("10".equals(type)) {
			result_i = iA_ST_UnAccountVouchDao.insertAccountV10s(iA_ST_UnAccountVouchDtos);
		}else if("11".equals(type)) {
			result_i = iA_ST_UnAccountVouchDao.insertAccountV11s(iA_ST_UnAccountVouchDtos);
		}else if("32".equals(type)) {
			result_i = iA_ST_UnAccountVouchDao.insertAccountV32s(iA_ST_UnAccountVouchDtos);
		}
		
		return result_i>0;
	}

	/**
	 * 新增U8数据
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public boolean addBatchPs(List<HwxxDto> hwxxDtos) {
		//组装查询条件
		List<AA_BatchPropertyDto> aA_BatchPropertyDtos = new ArrayList<>();
		for (HwxxDto hwxxDto : hwxxDtos) {
			AA_BatchPropertyDto aA_BatchPropertyDto = new AA_BatchPropertyDto();
			aA_BatchPropertyDto.setcBatch(hwxxDto.getScph());
			aA_BatchPropertyDto.setcInvCode(hwxxDto.getWlbm());
			aA_BatchPropertyDtos.add(aA_BatchPropertyDto);
		}
		//根据物料编码追溯号查询
		List<AA_BatchPropertyDto> aA_BatchPropertyDtos_t = aA_BatchPropertyDao.queryByInvAndBat(aA_BatchPropertyDtos);
		//存要新增的U8数据
		List<AA_BatchPropertyDto> aA_BatchPropertyDtos_add = new ArrayList<>();
		
		for (HwxxDto hwxxDto_t : hwxxDtos) {
			//判断有没有相同的数据
			if(aA_BatchPropertyDtos_t.size()>0) {
				boolean resulr = false;
				for (AA_BatchPropertyDto aa_BatchPropertyDto : aA_BatchPropertyDtos_t) {
					if(aa_BatchPropertyDto.getcBatch().equals(hwxxDto_t.getScph()) || aa_BatchPropertyDto.getcInvCode().equals(hwxxDto_t.getWlbm())) {
						resulr = true;
						break;	//发现一样的中断，换下一个
					}
				}
				//如果没有和此货物一样的，就新增
				if(!resulr) {
					AA_BatchPropertyDto aA_BatchPropertyDto = new AA_BatchPropertyDto();
					aA_BatchPropertyDto.setcBatch(hwxxDto_t.getScph());
					aA_BatchPropertyDto.setcInvCode(hwxxDto_t.getWlbm());
					aA_BatchPropertyDtos_add.add(aA_BatchPropertyDto);
				}
			}else {//没有的话直接全部新增
				AA_BatchPropertyDto aA_BatchPropertyDto = new AA_BatchPropertyDto();
				aA_BatchPropertyDto.setcBatch(hwxxDto_t.getScph());
				aA_BatchPropertyDto.setcInvCode(hwxxDto_t.getWlbm());
				aA_BatchPropertyDtos_add.add(aA_BatchPropertyDto);
			}		
		}
		//批量新增U8数据
		int result_bp = 1;
//		if(aA_BatchPropertyDtos_add.size()>0) {
//			result_bp = aA_BatchPropertyDao.insertBatchPs(aA_BatchPropertyDtos_add);
//		}
		return result_bp>0;
	}
	
	/**
	 * 到货新增U8入库数据
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8RkDataRk(RkglDto rkglDto) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		// U8生成入库单
		List<HwxxDto> hwxxs = hwxxDao.getDtoListById(rkglDto.getRkid());
		List<RkglDto> rkglDtos = rkglDao.getDtoList(rkglDto);

		RdRecordDto rdRecordDto = new RdRecordDto();
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String prefix = sdf_d.format(date);
		VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
		voucherHistoryDto.setcSeed(prefix);
		voucherHistoryDto.setCardNumber("24");
		//获取最大流水号
		VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
		if(voucherHistoryDto_t!=null) {
			String serial = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
			rdRecordDto.setcCode(prefix+(serial.length()>1?serial:"0"+serial)); // 到货单号
			rdRecordDto.setPrefix(prefix);
			rdRecordDto.setSflxbj("1");
			List<RdRecordDto> rdRecordDtos_t = rdRecordDao.getDtoList(rdRecordDto);
			int ccode_new=0;
	        if (rdRecordDtos_t != null && rdRecordDtos_t.size() > 0) {
	            List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(rdRecordDto);
	            ccode_new = Integer.parseInt(rdRecordDto.getcCode()) + 1;
	            for (RdRecordDto rdRecordDto_c : rdList) {
					if(ccode_new-Integer.parseInt(rdRecordDto_c.getcCode())==0) {
						ccode_new = ccode_new+1;
					}else {
						break;
					}
				}
	        }
	        if(ccode_new!=0) {
	        	rdRecordDto.setcCode(String.valueOf(ccode_new)); // 到货单号
	        	voucherHistoryDto_t.setcNumber(String.valueOf(ccode_new).substring(8)); //去除多余0
	        }else {
	        	voucherHistoryDto_t.setcNumber(serial);
	        }        
			//更新最大单号值		
			int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
			if(result_ccode<1)
				throw new BusinessException("msg", "更新U8最大单号失败！");
		}else {
			voucherHistoryDto.setiRdFlagSeed("1");
			voucherHistoryDto.setcContent("日期");
			voucherHistoryDto.setcContentRule("日");
			voucherHistoryDto.setbEmpty("0");
			voucherHistoryDto.setcNumber("1");
			rdRecordDto.setcCode(prefix+"01");
			//单号最大表增加数据
			int result_ccode = voucherHistoryDao.insert(voucherHistoryDto);
			if(result_ccode<1)
				throw new BusinessException("msg", "新增U8最大单号失败！");
		}

		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); // 存入账套
		uA_IdentityDto.setcVouchType("rd"); // 存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); // 获取主键最大值
		if(uA_IdentityDto_t!=null) {
			if (StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				uA_IdentityDto_t.setiFatherId(String.valueOf(id + 1));
				rdRecordDto.setID(id + 1000000001);
			}
		}else {
			rdRecordDto.setID(1000000001);
			uA_IdentityDto.setiFatherId("1");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if ("2".equals(rkglDtos.get(0).getHtlx())){
			rdRecordDto.setcSource("委外订单");
			rdRecordDto.setIpurorderid(rkglDtos.get(0).getU8omid()); // 合同关联U8id
			rdRecordDto.setcDepCode(null); // 机构代码
			rdRecordDto.setcRdCode(null); // 入库类型代码
			rdRecordDto.setVT_ID("27");//模板编号27
			rdRecordDto.setbCredit("0");
			rdRecordDto.setcPTCode(null);
			rdRecordDto.setDnverifytime(sdf.format(date));//审核时间
			rdRecordDto.setdVeriDate(sdf_d.format(date));//审核日期
			rdRecordDto.setcMemo(StringUtil.isNotBlank(rkglDtos.get(0).getBz())?(rkglDtos.get(0).getBz().length()>255?rkglDtos.get(0).getBz().substring(0,255):rkglDtos.get(0).getBz()):null);//备注
		}else {
			rdRecordDto.setcSource("采购订单");
			rdRecordDto.setIpurorderid(rkglDtos.get(0).getU8poid()); // 合同关联U8id
			rdRecordDto.setcDepCode(rkglDtos.get(0).getJgdm()); // 机构代码
			rdRecordDto.setcRdCode("1");
			rdRecordDto.setcPTCode(rkglDtos.get(0).getRklxdm());// 入库类型代码
		}
		rdRecordDto.setiExchRate(rkglDtos.get(0).getHl());//汇率
		rdRecordDto.setcMaker(rkglDtos.get(0).getQgsqr()); // 请购申请人
		rdRecordDto.setcDefine1(rkglDtos.get(0).getJlbh()); // 请购记录编号
		rdRecordDto.setDnmaketime(sdf.format(date));
		rdRecordDto.setcVouchType("01");
		rdRecordDto.setCsysbarcode("||st01|" + rdRecordDto.getcCode()); // "||st01|"+入库单
		rdRecordDto.setcWhCode(rkglDtos.get(0).getCkdm()); // 仓库代码
		rdRecordDto.setiTaxRate(rkglDtos.get(0).getHtmxsl()); // 税率
		rdRecordDto.setcExch_Name(rkglDtos.get(0).getHtbz()); // 币种
		rdRecordDto.setcBusType(rkglDtos.get(0).getCglxmc()); // 采购类型)
		rdRecordDto.setcHandler(rkglDto.getZsxm()); // 审核人
		rdRecordDto.setdDate(rkglDtos.get(0).getRkrq()); // 入库日期
		rdRecordDto.setcVenCode(rkglDtos.get(0).getGysdm()); // 供应商代码
		rdRecordDto.setcOrderCode(rkglDtos.get(0).getHtnbbh()); // 订单编号


		int result = rdRecordDao.insert(rdRecordDto);
		if (result < 1) {
			throw new BusinessException("msg", "入库数据存入U8失败！");
		}

		// 存入关联id
		int rdRecordId = rdRecordDto.getID();
		rkglDto.setGlid(String.valueOf(rdRecordId));
		map.put("rkglDto", rkglDto);

		// 存货物信息
		List<HwxxDto> hwxxDtoList = new ArrayList<>();
		// // 存批量更新的U8请购信息
		// List<PU_AppVouchsDto> PU_AppVouchsDtos = new ArrayList<>();
		// 存批量更新的U8合同明细信息
		List<PO_PodetailsDto> pO_PodetailsDtos = new ArrayList<>();
		List<OM_MODetailsDto> om_moDetailsDtos = new ArrayList<>();
		// 存批量更新U8入库明细数据
		List<RdRecordsDto> rdRecordsDtoList = new ArrayList<>();
		// 存物料编码，用于查库存
		StringBuilder ids = new StringBuilder();
		int irowno = 1;
		int id_s = 0;
		if(uA_IdentityDto_t!=null) {
			id_s = Integer.parseInt(uA_IdentityDto_t.getiChildId());
		}
		 
		//存货位调整信息
		List<InvPositionDto> invPositionDtos = new ArrayList<>();
		//存记账记录数据
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos = new ArrayList<>();
		for (HwxxDto hwxxDto : hwxxs) {
			ids.append(",").append(hwxxDto.getWlbm());
			RdRecordsDto rdRecordsDto = new RdRecordsDto();
			rdRecordsDto.setAutoID(id_s + 1000000001);
			id_s = id_s + 1;
			// 算出入库数量
			double sl = Double.parseDouble(hwxxDto.getDhsl())
					- Double.parseDouble(StringUtil.isNotBlank(hwxxDto.getCythsl()) ? hwxxDto.getCythsl() : "0")
					- Double.parseDouble(StringUtil.isNotBlank(hwxxDto.getZjthsl()) ? hwxxDto.getZjthsl() : "0")
					- Double.parseDouble(StringUtil.isNotBlank(hwxxDto.getQyl()) ? hwxxDto.getQyl() : "0");
			rdRecordsDto.setID(rdRecordDto.getID()); // 关联id
			rdRecordsDto.setcInvCode(hwxxDto.getWlbm()); // 物料编码
			rdRecordsDto.setiQuantity(Double.toString(sl)); // 数量

			// 无税总金额 = 含税总金额 /(1+税率/100)
			BigDecimal hjje = new BigDecimal(sl).multiply(new BigDecimal(hwxxDto.getHsdj())); // 总金额
			hjje=hjje.setScale(2,RoundingMode.HALF_UP);
			BigDecimal suil = new BigDecimal(hwxxDto.getSuil()).divide(new BigDecimal(100), 5, RoundingMode.HALF_UP);
			BigDecimal wszje = hjje.divide(suil.add(new BigDecimal("1")), 2, RoundingMode.HALF_UP);

			rdRecordsDto.setcBatch(hwxxDto.getScph()); // 生产批号
			rdRecordsDto.setcItem_class(hwxxDto.getXmdldm()); // 项目大类代码
			rdRecordsDto.setcItemCode(hwxxDto.getXmbmdm()); // 项目编码代码
			rdRecordsDto.setcItemCName(hwxxDto.getXmbmmc()); // 项目编码名称
			if (("0").equals(hwxxDto.getBzqflg())) {
				rdRecordsDto.setcMassUnit("2");
				rdRecordsDto.setiMassDate(hwxxDto.getBzq()); // 有效期数
			} else {
				rdRecordsDto.setcMassUnit("1");
				rdRecordsDto.setiMassDate("99"); // 有效期数
			}
			rdRecordsDto.setcPosition(hwxxDto.getKwbhdm()); // 库位
			rdRecordsDto.setdVDate(hwxxDto.getYxq());// 失效日期
			rdRecordsDto.setiPOsID(hwxxDto.getU8mxid());// 合同明细U8 ID

			rdRecordsDto.setcName(hwxxDto.getXmbmmc());// 项目编码名称
			rdRecordsDto.setcItemCName(hwxxDto.getXmdlmc());// 项目大类名称
			rdRecordsDto.setiNQuantity(Double.toString(sl));// 数量
			rdRecordsDto.setdMadeDate(hwxxDto.getScrq());// 生产日期
			rdRecordsDto.setChVencode(hwxxDto.getGysdm());// 供应商代码

			// 计算税额
			BigDecimal se = hjje.subtract(wszje);


			rdRecordsDto.setcPOID(hwxxDto.getHtnbbh()); // 关联合同单
			rdRecordsDto.setIrowno(String.valueOf(irowno));
			rdRecordsDto.setCbsysbarcode(rdRecordDto.getCsysbarcode() + "|" + irowno);
			irowno = irowno + 1;
			rdRecordsDto.setiSQuantity("0");
			rdRecordsDto.setiSNum("0");
			rdRecordsDto.setiMoney("0");


			rdRecordsDto.setCbMemo(StringUtil.isNotBlank(hwxxDto.getDhbz())?(hwxxDto.getDhbz().length()>255?hwxxDto.getDhbz().substring(0,255):hwxxDto.getDhbz()):null); //到货备注
			rdRecordsDto.setiProcessCost(hwxxDto.getWsdj());//原币单价（不含税）
			rdRecordsDto.setbTaxCost("1");
			if ("2".equals(hwxxDto.getHtlx())){
				rdRecordsDto.setiPrice(wszje.toString()); // 原币金额
				rdRecordsDto.setiAPrice(wszje.toString());// 原币金额
				rdRecordsDto.setiUnitCost(hwxxDto.getWsdj()); // 原币单价
				rdRecordsDto.setcDefine22(null);// 追溯号
				rdRecordsDto.setcDefine23(null);// 供应商名称
				rdRecordsDto.setfACost(hwxxDto.getWsdj());// 无税单价
				rdRecordsDto.setcDefine29(null); // 货号
				rdRecordsDto.setiOriTaxCost(hwxxDto.getHsdj()); // 原币含税单价
				rdRecordsDto.setiOriCost(hwxxDto.getWsdj()); // 原币单价
				rdRecordsDto.setiOriMoney(wszje.toString()); // 原币金额
				rdRecordsDto.setiOriTaxPrice(se.toString()); // 原币税额
				rdRecordsDto.setIoriSum(hjje.toString());// 原币价税总额
				rdRecordsDto.setiTaxRate("0"); // 税率
				rdRecordsDto.setiTaxPrice(se.toString()); // 税额
				rdRecordsDto.setiSum(hjje.toString()); // 原币价税总额
				rdRecordsDto.setiProductType("0");

				if (StringUtil.isNotBlank(Double.toString(sl))&&StringUtil.isNotBlank(hwxxDto.getWsdj())){
					BigDecimal wsdj = new BigDecimal(hwxxDto.getWsdj());
					BigDecimal bsl = new BigDecimal(Double.toString(sl));
					BigDecimal iProcessFee = wsdj.multiply(bsl);
					String iProcessFeeStr = iProcessFee.setScale(2, RoundingMode.HALF_UP).toString();
					rdRecordsDto.setiProcessFee(iProcessFeeStr);//原币单价×入库数量
				}
			}else {
				rdRecordsDto.setiPrice(wszje.toString()); // 原币金额
				rdRecordsDto.setiAPrice(wszje.toString());// 原币金额
				rdRecordsDto.setiUnitCost(hwxxDto.getWsdj()); // 原币单价
				rdRecordsDto.setcDefine23(hwxxDto.getGysmc());// 供应商名称
				rdRecordsDto.setcDefine22(hwxxDto.getZsh());// 追溯号
				rdRecordsDto.setfACost(hwxxDto.getWsdj());// 无税单价
				rdRecordsDto.setcDefine29(hwxxDto.getYchh()); // 货号

				rdRecordsDto.setiOriTaxCost(hwxxDto.getHsdj()); // 原币含税单价
				rdRecordsDto.setiOriCost(hwxxDto.getWsdj()); // 原币单价
				rdRecordsDto.setiOriMoney(wszje.toString()); // 原币金额
				rdRecordsDto.setiOriTaxPrice(se.toString()); // 原币税额
				rdRecordsDto.setIoriSum(hjje.toString());// 原币价税总额
				rdRecordsDto.setiTaxRate(hwxxDto.getSuil()); // 税率
				rdRecordsDto.setiTaxPrice(se.toString()); // 税额
				rdRecordsDto.setiSum(hjje.toString()); // 原币价税总额
			}

			rdRecordsDto.setiOMoDID(hwxxDto.getU8ommxid());//OM_MODetails表 MODetailsID
			rdRecordsDtoList.add(rdRecordsDto);

			//组装记账记录数据
			IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto = new IA_ST_UnAccountVouchDto();
			iA_ST_UnAccountVouchDto.setIDUN(String.valueOf(rdRecordsDto.getID()));
			iA_ST_UnAccountVouchDto.setIDSUN(String.valueOf(rdRecordsDto.getAutoID()));
			iA_ST_UnAccountVouchDto.setcVouTypeUN("01");
			if ("2".equals(hwxxDto.getHtlx())){
				iA_ST_UnAccountVouchDto.setcBUstypeUN("委外加工");
			}else {
				iA_ST_UnAccountVouchDto.setcBUstypeUN("采购入库");
			}
			iA_ST_UnAccountVouchDtos.add(iA_ST_UnAccountVouchDto);
			
			// 存入关联id
			HwxxDto hwxx_list = new HwxxDto();
			int hwglid = rdRecordsDto.getAutoID();
			hwxx_list.setGlid(String.valueOf(hwglid));
			hwxx_list.setU8rkdh(rdRecordDto.getcCode());
			hwxx_list.setHwid(hwxxDto.getHwid());
			hwxx_list.setXgry(rkglDto.getXgry());
			hwxxDtoList.add(hwxx_list);
			map.put("hwxxDtoList", hwxxDtoList);

			// // 存入要更新的U8的请购明细
			// if (StringUtil.isNotBlank(hwxxDto.getQgmxid())) {
			// 	PU_AppVouchsDto pU_AppVouchsDto = new PU_AppVouchsDto();
			// 	pU_AppVouchsDto.setAutoID(hwxxDto.getQgglid());
			// 	pU_AppVouchsDto.setiReceivedQTY(sl.toString()); // 入库数量
			// 	PU_AppVouchsDtos.add(pU_AppVouchsDto);
			// }


			if ("2".equals(hwxxDto.getHtlx())) {
				// 存入要更新的U8的合同明细
				OM_MODetailsDto om_moDetailsDto = new OM_MODetailsDto();
				om_moDetailsDto.setMODetailsID(hwxxDto.getU8ommxid());
				om_moDetailsDto.setiReceivedQTY(sl+"");// 入库数量
				om_moDetailsDto.setiReceivedNum("0.000000");
				om_moDetailsDto.setiReceivedMoney("0.00");
				om_moDetailsDtos.add(om_moDetailsDto);
			}else {
				// 存入要更新的U8的合同明细
				PO_PodetailsDto pO_PodetailsDto = new PO_PodetailsDto();
				pO_PodetailsDto.setID(Integer.parseInt(hwxxDto.getHtglid()));
				pO_PodetailsDto.setiReceivedQTY(Double.toString(sl));// 入库数量
				// 计算入库金额 入库金额 = 无税单价 * 数量
				String wsdj_t;
				if (StringUtil.isNotBlank(hwxxDto.getWsdj())) {
					wsdj_t = hwxxDto.getWsdj(); // 无税单价
				} else {
					BigDecimal suil_t = new BigDecimal(hwxxDto.getSuil()).divide(new BigDecimal(100), 5,
							RoundingMode.HALF_UP);
					wsdj_t = new BigDecimal(hwxxDto.getHsdj())
							.divide(suil_t.add(new BigDecimal(1)), 5, RoundingMode.HALF_UP).toString();
				}
				BigDecimal wsdj = new BigDecimal(wsdj_t);
				BigDecimal sl_t = new BigDecimal(Double.toString(sl)); // 入库数量
				BigDecimal rkje = wsdj.multiply(sl_t);
				rkje = rkje.setScale(2, RoundingMode.HALF_UP);// 四舍五入保留两位小数
				pO_PodetailsDto.setiReceivedMoney(rkje.toString());// 入库金额
				pO_PodetailsDto.setiReceivedNum("0");
				pO_PodetailsDtos.add(pO_PodetailsDto);
			}
			//处理货位调整表
			InvPositionDto invPositionDto = new InvPositionDto();
			invPositionDto.setRdsID(String.valueOf(rdRecordsDto.getAutoID()));
			invPositionDto.setRDID(String.valueOf(rdRecordDto.getID()));
			invPositionDto.setcWhCode(rkglDtos.get(0).getCkdm());
			invPositionDto.setcPosCode(hwxxDto.getKwbhdm());
			invPositionDto.setcInvCode(hwxxDto.getWlbm());
			invPositionDto.setcBatch(hwxxDto.getScph());
			invPositionDto.setdVDate(hwxxDto.getYxq());
			invPositionDto.setiQuantity(Double.toString(sl));
			invPositionDto.setcHandler(rkglDto.getZsxm());
			invPositionDto.setdDate(rkglDtos.get(0).getRkrq());
			invPositionDto.setbRdFlag("1");
			invPositionDto.setdMadeDate(hwxxDto.getScrq());
			if (("0").equals(hwxxDto.getBzqflg())) {
				invPositionDto.setcMassUnit("2");
				invPositionDto.setiMassDate(hwxxDto.getBzq()); // 有效期数
			} else {
				invPositionDto.setcMassUnit("1");
				invPositionDto.setiMassDate("99"); // 有效期数
			}
			invPositionDto.setCvouchtype("01");
			Date date_t = new Date();
			invPositionDto.setdVouchDate(sdf.format(date_t));
			invPositionDtos.add(invPositionDto);	
		}

		// 更新U8入库明细表
		//若List过大进行截断,10个明细为一组进行添加
		int result_rds;
		if(rdRecordsDtoList.size()>10) {
			int length = (int)Math.ceil((double)rdRecordsDtoList.size() / 10);//向上取整
			for (int i = 0; i < length; i++) {
				List<RdRecordsDto> list = new ArrayList<>();
				int t_length;
				if (i != length - 1) {
					t_length = (i + 1) * 10;
				} else {
					t_length = rdRecordsDtoList.size();
				}
				for (int j = i * 10; j < t_length; j++) {
					list.add(rdRecordsDtoList.get(j));
				}
				result_rds = rdRecordsDao.insertRds(list);
				if (result_rds < 1)
					throw new BusinessException("msg", "更新U8入库明细表失败！");
			}
		}else{
			result_rds = rdRecordsDao.insertRds(rdRecordsDtoList);
			if (result_rds < 1)
				throw new BusinessException("msg", "更新U8入库明细表失败！");
		}

		// 更新最大值表
		boolean result_ui = false;
		//判断是新增还是修改
		if(uA_IdentityDto_t!=null) {
			//更新
			uA_IdentityDto_t.setiChildId(String.valueOf(id_s));
			result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
		}
		if(!result_ui) {
			uA_IdentityDto.setiChildId(String.valueOf(id_s));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}
		// // 更新U8请购表入库数量
		// //若List过大进行截断,10个明细为一组进行添加
		// int result_PU_App =0;
		// if(PU_AppVouchsDtos.size()>10) {
		// 	int length = (int)Math.ceil((double)PU_AppVouchsDtos.size() / 10);//向上取整
		// 	for (int i = 0; i < length; i++) {
		// 		List<PU_AppVouchsDto> list = new ArrayList<>();
		// 		int t_length = 0;
		// 		if (i != length - 1) {
		// 			t_length = (i + 1) * 10;
		// 		} else {
		// 			t_length = PU_AppVouchsDtos.size();
		// 		}
		// 		for (int j = i * 10; j < t_length; j++) {
		// 			list.add(PU_AppVouchsDtos.get(j));
		// 		}
		// 		result_PU_App = PU_AppVouchDao.updateIReceivedQTY(list);
		// 		if (result_PU_App < 1)
		// 			throw new BusinessException("msg", "更新U8请购入库数量失败！");
		// 	}
		// }else{
		// 	if(PU_AppVouchsDtos.size()>0){
		// 		result_PU_App = PU_AppVouchDao.updateIReceivedQTY(PU_AppVouchsDtos);
		// 		if (result_PU_App < 1)
		// 			throw new BusinessException("msg", "更新U8请购入库数量失败！");
		// 	}
		// }

		/// 更新U8合同明细入库信息
		//若List过大进行截断,10个明细为一组进行添加
		int result_pO_Pode =0;
		if(pO_PodetailsDtos.size()>10) {
			int length = (int)Math.ceil((double)pO_PodetailsDtos.size() / 10);//向上取整
			for (int i = 0; i < length; i++) {
				List<PO_PodetailsDto> list = new ArrayList<>();
				int t_length;
				if (i != length - 1) {
					t_length = (i + 1) * 10;
				} else {
					t_length = pO_PodetailsDtos.size();
				}
				for (int j = i * 10; j < t_length; j++) {
					list.add(pO_PodetailsDtos.get(j));
				}
				result_pO_Pode = pO_PodetailsDao.updateRkxx(list);
			}
		}else{
			result_pO_Pode = pO_PodetailsDao.updateRkxx(pO_PodetailsDtos);
		}

		// 更新U8合同明细入库信息
		//若List过大进行截断,10个明细为一组进行添加
		int result_om =0;
		if(om_moDetailsDtos.size()>10) {
			int length = (int)Math.ceil((double)om_moDetailsDtos.size() / 10);//向上取整
			for (int i = 0; i < length; i++) {
				List<OM_MODetailsDto> list = new ArrayList<>();
				int t_length;
				if (i != length - 1) {
					t_length = (i + 1) * 10;
				} else {
					t_length = om_moDetailsDtos.size();
				}
				for (int j = i * 10; j < t_length; j++) {
					list.add(om_moDetailsDtos.get(j));
				}
				result_om = om_moDetailsDao.updateRkxx(list);
			}
		}else{
			result_om = om_moDetailsDao.updateRkxx(om_moDetailsDtos);
		}

		if (result_om < 1 && result_pO_Pode < 1){
			throw new BusinessException("msg", "更新U8合同入库信息失败！");
		}
		//若List过大进行截断,10个明细为一组进行添加
		int result_hw;
		if(invPositionDtos.size()>10) {
			int length = (int)Math.ceil((double)invPositionDtos.size() / 10);//向上取整
			for (int i = 0; i < length; i++) {
				List<InvPositionDto> list = new ArrayList<>();
				int t_length;
				if (i != length - 1) {
					t_length = (i + 1) * 10;
				} else {
					t_length = invPositionDtos.size();
				}
				for (int j = i * 10; j < t_length; j++) {
					list.add(invPositionDtos.get(j));
				}
				result_hw = invPositionDao.insertDtos(list);
				if (result_hw < 1)
					throw new BusinessException("msg", "新增U8货位调整失败！");
			}
		}else{
			result_hw = invPositionDao.insertDtos(invPositionDtos);
			if (result_hw < 1)
				throw new BusinessException("msg", "新增U8货位调整失败！");
		}

		//若List过大进行截断,10个明细为一组进行添加
		boolean result_av;
		if(iA_ST_UnAccountVouchDtos.size()>10) {
			int length = (int)Math.ceil((double)iA_ST_UnAccountVouchDtos.size() / 10);//向上取整
			for (int i = 0; i < length; i++) {
				List<IA_ST_UnAccountVouchDto> list = new ArrayList<>();
				int t_length;
				if (i != length - 1) {
					t_length = (i + 1) * 10;
				} else {
					t_length = iA_ST_UnAccountVouchDtos.size();
				}
				for (int j = i * 10; j < t_length; j++) {
					list.add(iA_ST_UnAccountVouchDtos.get(j));
				}
				result_av = addAccountVs(list,"01");
				if(!result_av)
					throw new BusinessException("msg", "记账记录新增失败！");
			}
		}else{
			result_av = addAccountVs(iA_ST_UnAccountVouchDtos,"01");
			if(!result_av)
				throw new BusinessException("msg", "记账记录新增失败！");
		}
		HwxxDto hwxx_rk = new HwxxDto();
		hwxx_rk.setRkid(rkglDto.getRkid());
		// 根据生产批号物料分组
		List<HwxxDto> hwxxList = hwxxService.queryByRkid(hwxx_rk);
		List<HwxxDto> scphList = hwxxService.queryBycBatch(hwxx_rk);
		//新增U8数据
		//若List过大进行截断,10个明细为一组进行添加
		boolean result_bp;
		if(scphList.size()>10) {
			int length = (int)Math.ceil((double)scphList.size() / 10);//向上取整
			for (int i = 0; i < length; i++) {
				List<HwxxDto> list = new ArrayList<>();
				int t_length;
				if (i != length - 1) {
					t_length = (i + 1) * 10;
				} else {
					t_length = scphList.size();
				}
				for (int j = i * 10; j < t_length; j++) {
					list.add(scphList.get(j));
				}
				result_bp = addBatchPs(list);
				if(!result_bp)
					throw new BusinessException("msg", "新增U8数据（AA_BatchProperty）失败！");
			}
		}else{
			result_bp = addBatchPs(scphList);
			if(!result_bp)
				throw new BusinessException("msg", "新增U8数据（AA_BatchProperty）失败！");
		}
		
		// 获取U8已经存在得库存
		ids = new StringBuilder(ids.substring(1));
		CurrentStockDto currentStockDto_t = new CurrentStockDto();
		currentStockDto_t.setIds(ids.toString());
		List<CurrentStockDto> currentStockDtos_t = currentStockDao.queryBycInvCode(currentStockDto_t);
		// 存要更新得货物id,更新库存关联id
		List<HwxxDto> hwxxDto_hs = new ArrayList<>();
		// 存入批量更新得U8库存数据
		List<CurrentStockDto> currentStockDtos = new ArrayList<>();
		//存货位货物总数信息
		List<InvPositionSumDto> invPositionSumDtos_add = new ArrayList<>();
		List<InvPositionSumDto> invPositionSumDtos_mod = new ArrayList<>();
		for (HwxxDto hwxxDto_list : hwxxList) {
			for (HwxxDto hwxx_hwxxs : hwxxs) {
				if (hwxxDto_list.getScph().equals(hwxx_hwxxs.getScph())
						&& hwxxDto_list.getWlid().equals(hwxx_hwxxs.getWlid())) {
					// 判断U8是否存在该物料
					if (currentStockDtos_t.size() > 0) {
						boolean modflg = true;
						for (CurrentStockDto current_t : currentStockDtos_t) {
							if (rkglDtos.get(0).getCkdm().equals(current_t.getcWhCode())
									&& hwxx_hwxxs.getWlbm().equals(current_t.getcInvCode())
									&& hwxx_hwxxs.getScph().equals(current_t.getcBatch())) {
								// 如果仓库，物料编码和生产批号都相同，就修改库存数量
								CurrentStockDto currentStockDto_cu = new CurrentStockDto();
								double sl = Double
										.parseDouble(StringUtils.isNotBlank(hwxxDto_list.getU8rksl())
												? hwxxDto_list.getU8rksl()
												: "0")
										+ Double.parseDouble(StringUtils.isNotBlank(current_t.getiQuantity())
												? current_t.getiQuantity()
												: "0");
								currentStockDto_cu.setiQuantity(Double.toString(sl)); // 库存数量
								currentStockDto_cu.setAutoID(current_t.getAutoID()); // 存入主键id
								currentStockDto_cu.setcBatch(current_t.getcBatch());
								currentStockDto_cu.setcInvCode(current_t.getcInvCode());
								currentStockDto_cu.setcWhCode(current_t.getcWhCode());
								currentStockDtos.add(currentStockDto_cu);
								
								// 存入关联id
								HwxxDto hwxx_hs = new HwxxDto();
								hwxx_hs.setWlid(hwxx_hwxxs.getWlid());
								hwxx_hs.setRkid(hwxx_hwxxs.getRkid());
								hwxx_hs.setScph(hwxx_hwxxs.getScph());
								hwxx_hs.setKcglid(String.valueOf(currentStockDto_cu.getAutoID())); // 存入关联id
								hwxxDto_hs.add(hwxx_hs);
								modflg = false;
								break;
							}
						}
						if (modflg) {
							// 新增得U8库存数据
							CurrentStockDto current_add = new CurrentStockDto();
							current_add.setcInvCode(hwxx_hwxxs.getWlbm()); // 物料编码
							current_add.setcWhCode(rkglDtos.get(0).getCkdm());// 仓库编码
							boolean itemidflg = true;
							for (CurrentStockDto current_t : currentStockDtos_t) {
								if (hwxx_hwxxs.getWlbm().equals(current_t.getcInvCode())) {
									// 如果物料编码一样，追溯号不一样，ItemId字段相同
									current_add.setItemId(current_t.getItemId());
									itemidflg = false;
								}
							}

							if (itemidflg) {
								SCM_ItemDto sCM_ItemDto_c  = sCM_ItemDao.getDtoBycInvVode(current_add.getcInvCode());
								if(sCM_ItemDto_c!=null) {
									current_add.setItemId(sCM_ItemDto_c.getId());
								}else {
									// 取SCM_Item表中最大值
									int num_ItemId = sCM_ItemDao.getMaxItemId() + 1;
									current_add.setItemId(String.valueOf(num_ItemId));
									// 存入新增的物料
									SCM_ItemDto sCM_ItemDto = new SCM_ItemDto();
									sCM_ItemDto.setcInvCode(current_add.getcInvCode()); // 物料编码
									int result_scm = sCM_ItemDao.insert(sCM_ItemDto);
									if (result_scm < 1) {
										throw new BusinessException("msg", "sCM_Item新增失败！");
									}
								}								
							}
							current_add.setiQuantity(hwxxDto_list.getU8rksl()); // 现存数量
							current_add.setcBatch(hwxx_hwxxs.getScph()); // 生产批号
							current_add.setdVDate(hwxx_hwxxs.getYxq()); // 失效日期
							current_add.setdMdate(hwxx_hwxxs.getScrq()); // 生产日期
							current_add.setiExpiratDateCalcu("0");
							if (("0").equals(hwxx_hwxxs.getBzqflg())) {
								current_add.setcMassUnit("2"); // 有效单位
								current_add.setiMassDate(hwxx_hwxxs.getBzq()); // 有效期数
							} else {
								current_add.setcMassUnit("1"); // 有效单位
								current_add.setiMassDate("99"); // 有效期数
							}
							int result_add = currentStockDao.insert(current_add);
							if (result_add < 1) {
								throw new BusinessException("msg", "U8库存新增失败！");
							}
							
							// 存入关联id
							HwxxDto hwxx_hs = new HwxxDto();
							hwxx_hs.setWlid(hwxx_hwxxs.getWlid());
							hwxx_hs.setRkid(hwxx_hwxxs.getRkid());
							hwxx_hs.setScph(hwxx_hwxxs.getScph());
							hwxx_hs.setKcglid(String.valueOf(current_add.getAutoID())); // 存入关联id
							hwxxDto_hs.add(hwxx_hs);
						}
					} else {
						// 新增得U8库存数据
						CurrentStockDto current_add_t = new CurrentStockDto();
						current_add_t.setcInvCode(hwxx_hwxxs.getWlbm()); // 物料编码
						current_add_t.setcWhCode(rkglDtos.get(0).getCkdm());// 仓库编码
						SCM_ItemDto sCM_ItemDto_c  = sCM_ItemDao.getDtoBycInvVode(hwxx_hwxxs.getWlbm());
						if(sCM_ItemDto_c!=null) {
							current_add_t.setItemId(sCM_ItemDto_c.getId());
						}else {
							// 存入新增的物料
							SCM_ItemDto sCM_ItemDto = new SCM_ItemDto();
							sCM_ItemDto.setcInvCode(current_add_t.getcInvCode()); // 物料编码
							int result_scm = sCM_ItemDao.insert(sCM_ItemDto);
							if (result_scm < 1) {
								throw new BusinessException("msg", "sCM_Item新增失败！");
							}
							SCM_ItemDto sCM_ItemDto_t  = sCM_ItemDao.getDtoBycInvVode(hwxx_hwxxs.getWlbm());
							current_add_t.setItemId(sCM_ItemDto_t.getId());
						}	
						current_add_t.setiQuantity(hwxxDto_list.getU8rksl()); // 现存数量
						current_add_t.setcBatch(hwxx_hwxxs.getScph()); // 生产批号
						current_add_t.setdVDate(hwxx_hwxxs.getYxq()); // 失效日期
						current_add_t.setdMdate(hwxx_hwxxs.getScrq()); // 生产日期
						current_add_t.setiExpiratDateCalcu("0");
						if (("0").equals(hwxx_hwxxs.getBzqflg())) {
							current_add_t.setcMassUnit("2"); // 有效单位
							current_add_t.setiMassDate(hwxx_hwxxs.getBzq()); // 有效期数
						} else {
							current_add_t.setcMassUnit("1"); // 有效单位
							current_add_t.setiMassDate("99"); // 有效期数
						}
						int result_add = currentStockDao.insert(current_add_t);
						if (result_add < 1) {
							throw new BusinessException("msg", "U8库存添加失败！");
						}
						
						// 存入关联id
						HwxxDto hwxx_hs = new HwxxDto();
						hwxx_hs.setWlid(hwxx_hwxxs.getWlid());
						hwxx_hs.setRkid(hwxx_hwxxs.getRkid());
						hwxx_hs.setScph(hwxx_hwxxs.getScph());
						hwxx_hs.setKcglid(String.valueOf(current_add_t.getAutoID())); 
						hwxxDto_hs.add(hwxx_hs);
					}
					
					//处理货位货物总数数据
					InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
					invPositionSumDto.setcWhCode(rkglDtos.get(0).getCkdm());
					invPositionSumDto.setcPosCode(hwxx_hwxxs.getKwbhdm());
					invPositionSumDto.setcInvCode(hwxx_hwxxs.getWlbm());
					invPositionSumDto.setcBatch(hwxx_hwxxs.getScph());
					//判断是否存在  仓库，物料编码，库位，生产批号一样的数据
					InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
					if(invPositionSumDto_t!=null) {
						//存在，更新货物货物数量
						invPositionSumDto_t.setJsbj("0"); //计算标记,相加
						invPositionSumDto_t.setiQuantity(hwxxDto_list.getU8rksl());
						invPositionSumDtos_mod.add(invPositionSumDto_t);
					}else {
						//不存在，做新增
						invPositionSumDto.setiQuantity(hwxxDto_list.getU8rksl());
						invPositionSumDto.setInum("0");
						if (("0").equals(hwxx_hwxxs.getBzqflg())) {
							invPositionSumDto.setcMassUnit("2");
							invPositionSumDto.setiMassDate(hwxx_hwxxs.getBzq()); // 保质期
						} else {
							invPositionSumDto.setcMassUnit("1");
							invPositionSumDto.setiMassDate("99"); // 保质期
						}
						invPositionSumDto.setdMadeDate(hwxx_hwxxs.getScrq());
						invPositionSumDto.setdVDate(hwxx_hwxxs.getYxq());
						invPositionSumDtos_add.add(invPositionSumDto);
					}
					break;
				}
			}
		}
		
		//更新库存
		if(currentStockDtos.size()>0) {
			//若List过大进行截断,10个明细为一组进行添加
			boolean reulu_mod;
			if(currentStockDtos.size()>10) {
				int length = (int)Math.ceil((double)currentStockDtos.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<CurrentStockDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = currentStockDtos.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(currentStockDtos.get(j));
					}
					reulu_mod = currentStockDao.updateKwAndSl(list);
					if(!reulu_mod) {
						throw new BusinessException("msg","库存数量更新失败！");
					}
				}
			}else{
				reulu_mod = currentStockDao.updateKwAndSl(currentStockDtos);
				if(!reulu_mod) {
					throw new BusinessException("msg","库存数量更新失败！");
				}
			}
		}
		//新增货物总数信息
		if(invPositionSumDtos_add.size()>0) {
			//若List过大进行截断,10个明细为一组进行添加
			int result_add;
			if(invPositionSumDtos_add.size()>10) {
				int length = (int)Math.ceil((double)invPositionSumDtos_add.size() / 10);//向上取整   2
				for (int i = 0; i < length; i++) {
					List<InvPositionSumDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = invPositionSumDtos_add.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(invPositionSumDtos_add.get(j));
					}
					result_add = invPositionSumDao.insertDtos(list);
					if(result_add<1) {
						throw new BusinessException("msg", "新增U8货位总数失败！");
					}
				}
			}else{
				result_add = invPositionSumDao.insertDtos(invPositionSumDtos_add);
				if(result_add<1) {
					throw new BusinessException("msg", "新增U8货位总数失败！");
				}
			}
		}
		
		//修改货物总数信息
		if(invPositionSumDtos_mod.size()>0) {
			//若List过大进行截断,10个明细为一组进行添加
			int result_mod;
			if(invPositionSumDtos_mod.size()>10) {
				int length = (int)Math.ceil((double)invPositionSumDtos_mod.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<InvPositionSumDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = invPositionSumDtos_mod.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(invPositionSumDtos_mod.get(j));
					}
					result_mod = invPositionSumDao.updateDtos(list);
					if(result_mod<1) {
						throw new BusinessException("msg", "更新U8货位总数失败！");
					}
				}
			}else{
				result_mod = invPositionSumDao.updateDtos(invPositionSumDtos_mod);
				if(result_mod<1) {
					throw new BusinessException("msg", "更新U8货位总数失败！");
				}
			}
		}
		
		//更新库存关联id
		map.put("hwxxDto_hs", hwxxDto_hs);
		return map;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public void updateDhxxupdate(DhxxDto dhxxDto) {
		DhxxDto dhxxDto1 = dhxxDao.getDtoById(dhxxDto.getDhid());
		HwxxDto hwxxDto = new HwxxDto();
		hwxxDto.setDhid(dhxxDto.getDhid());
		List<HwxxDto> hwxxDtos = dhxxDto.getHwxxDtos();
		List<HwxxDto> hwxxDtoList = hwxxService.getHwxxListByDhid(hwxxDto);

		if (dhxxDto1.getZt().equals("80")){
			//更改rdRecord01数据
			RdRecordDto recordDto = new RdRecordDto();
			recordDto.setID(Integer.parseInt(dhxxDto1.getGlid()));
			recordDto.setcDefine1(hwxxDtoList.get(0).getQgjlbh());
			CkxxDto ckxxDto = ckxxService.getDtoById(dhxxDto.getCkid());
			recordDto.setcWhCode(ckxxDto.getCkdm());
			JcsjDto jcsjDto = jcsjService.getDtoById(dhxxDto.getCglx());
			recordDto.setcBusType(jcsjDto.getCsmc());
			recordDto.setdDate(dhxxDto.getDhrq());
			recordDto.setcCode(dhxxDto.getDhdh());
			recordDto.setcDepCode(hwxxDtoList.get(0).getSqbm());
			recordDto.setCsysbarcode("||st01|"+dhxxDto.getDhdh());
			rdRecordDao.update(recordDto);
			JcsjDto jcsjDto1 = jcsjService.getDtoById(dhxxDto.getRklb());
			recordDto.setcRdCode(jcsjDto1.getCsmc());
			GysxxDto gysxxDto = gysxxService.getDtoById(hwxxDtos.get(0).getGysid());
			recordDto.setcVenCode(gysxxDto.getGysdm());
			for (HwxxDto hwxx: hwxxDtoList) {
				for (HwxxDto hwxxs: hwxxDtos) {
					if (hwxx.getHwid().equals(hwxxs.getHwid())){
						double htdhsl = Double.parseDouble(hwxx.getHtdhsl())+Double.parseDouble(hwxx.getDhsl())-Double.parseDouble(hwxxs.getDhsl());
						HtmxDto htmxDto = new HtmxDto();
						htmxDto.setDhsl(Double.toString(htdhsl));
						htmxDto.setHtmxid(hwxx.getHtmxid());
						htmxDto.setXgry(dhxxDto.getXgry());
						List<HtmxDto> htmxDtos = new ArrayList<>();
						htmxDtos.add(htmxDto);
						htmxService.updateHtmxDtos(htmxDtos);
						RdRecordsDto rdRecordsDto = new RdRecordsDto();
						rdRecordsDto.setAutoID(Integer.parseInt(hwxx.getGlid()));
						rdRecordsDto.setiQuantity(hwxxs.getDhsl());
						double ioriSum = Double.parseDouble(hwxxs.getDhsl())*Double.parseDouble(hwxx.getHsdj());
						rdRecordsDto.setIoriSum(Double.toString(ioriSum));
						rdRecordsDto.setiSum(Double.toString(ioriSum));
						double iPrice = ioriSum/(Double.parseDouble(hwxx.getSuil())+1);
						double iOriTaxPrice = ioriSum-iPrice;
						rdRecordsDto.setiOriTaxPrice(Double.toString(iOriTaxPrice));
						rdRecordsDto.setiTaxPrice(Double.toString(iOriTaxPrice));
						rdRecordsDto.setiPrice(Double.toString(iPrice));
						rdRecordsDto.setiAPrice(Double.toString(iPrice));
						rdRecordsDto.setiOriMoney(Double.toString(iPrice));
						rdRecordsDto.setcBatch(hwxxs.getScph());
						CkxxDto ckxxDto4 = ckxxService.getDtoById(dhxxDto.getCkid());
						rdRecordsDto.setcPosition(ckxxDto4.getCkdm());
						rdRecordsDto.setdMadeDate(hwxxs.getScrq());
						String gysdm = gysxxService.getDtoById(hwxxs.getGysid()).getGysdm();
						rdRecordsDto.setChVencode(gysdm);
						rdRecordsDto.setdVDate(hwxxs.getYxq());
						rdRecordsDto.setcItem_class(hwxx.getXmdldm());
						rdRecordsDto.setcItemCode(hwxx.getXmbmdm());
						rdRecordsDto.setcItemCName(hwxx.getXmdlmc());
						rdRecordsDto.setcName(hwxx.getXmbmmc());
						rdRecordsDto.setAutoID(Integer.parseInt(hwxx.getGlid()));
						rdRecordsDto.setcDefine23(hwxxs.getGysmc());
						rdRecordsDto.setcDefine22(hwxxs.getZsh());
						rdRecordsDto.setCbMemo(StringUtil.isNotBlank(hwxxs.getDhbz())?(hwxxs.getDhbz().length()>255?hwxxs.getDhbz().substring(0,255):hwxxs.getDhbz()):null); //备注
						rdRecordsDto.setCbsysbarcode("||st01|"+dhxxDto.getDhdh()+"|"+rdRecordsDao.getRds01(Integer.parseInt(hwxx.getGlid())).getIrowno());
						rdRecordsDao.update(rdRecordsDto);
						PO_PodetailsDto po_podetailsDto = new PO_PodetailsDto();
						po_podetailsDto.setID(Integer.parseInt(hwxx.getU8mxid()));
						po_podetailsDto.setiReceivedQTY(hwxxs.getDhsl());
						if (StringUtil.isNotBlank(hwxx.getWsdj())){
							double iReceivedMoney =  Double.parseDouble(hwxxs.getDhsl())*Double.parseDouble(hwxx.getWsdj());
							po_podetailsDto.setiReceivedMoney(Double.toString(iReceivedMoney));
						}
						pO_PodetailsDao.update(po_podetailsDto);
						if (StringUtil.isNotBlank(hwxx.getQgmxid())){
							double qgdhsl = Double.parseDouble(hwxx.getQgdhsl())+Double.parseDouble(hwxx.getDhsl())-Double.parseDouble(hwxxs.getDhsl());
							QgmxDto qgmxDto = new QgmxDto();
							qgmxDto.setDhsl(Double.toString(qgdhsl));
							qgmxDto.setQgmxid(hwxx.getQgmxid());
							qgmxDto.setXgry(dhxxDto.getXgry());
							qgmxService.updateQxqgxx(qgmxDto);
							// PU_AppVouchsDto pu_appVouchsDto = new PU_AppVouchsDto();
							// pu_appVouchsDto.setAutoID(hwxx.getGlid());
							// pu_appVouchsDto.setiReceivedQTY(hwxxs.getDhsl());
							// PU_AppVouchDao.updatePUAppVouchs(pu_appVouchsDto);
						}
						CkxxDto ckxxDto1 = ckxxService.getDtoById(hwxxs.getKwbh());
						CurrentStockDto currentStockDto = new CurrentStockDto();
						currentStockDto.setAutoID(Integer.parseInt(hwxx.getKcglid()));
						CurrentStockDto currentStockDto1 = currentStockDao.getCurrentStockByAutoID(currentStockDto);
						if (!dhxxDto.getCkid().equals(dhxxDto1.getCkid()) || !hwxx.getScph().equals(hwxxs.getScph())){
							if (hwxx.getLbbj().equals("1")){
								if (!hwxx.getRkzt().equals("80")){
									if (currentStockDto1 !=null){
										double iQuantity = Double.parseDouble(currentStockDto1.getiQuantity())-Double.parseDouble(hwxx.getDhsl());
										currentStockDto.setiQuantity(Double.toString(iQuantity));
										currentStockDao.update(currentStockDto);
									}else{
										currentStockDto.setcInvCode(hwxx.getWlbm());
										currentStockDto.setcBatch(hwxxs.getScph());
										currentStockDto.setcWhCode(ckxxDto1.getCkdm());
										CurrentStockDto currentStockDto2 = currentStockDao.getCurrentStocksByDto(currentStockDto);
										if (currentStockDto2 != null){
											double iQuantity2 = Double.parseDouble(currentStockDto2.getiQuantity())+Double.parseDouble(hwxxs.getDhsl());
											CurrentStockDto currentStockDtoNew = new CurrentStockDto();
											currentStockDtoNew.setiQuantity(Double.toString(iQuantity2));
											currentStockDao.update(currentStockDtoNew);
										}else{
											SCM_ItemDto sCM_ItemDto_t  = sCM_ItemDao.getDtoBycInvVode(hwxx.getWlbm());
											CurrentStockDto currentStockDtoNew = new CurrentStockDto();
											currentStockDtoNew.setiQuantity(hwxxs.getDhsl());
											currentStockDtoNew.setcBatch(hwxxs.getScph());
											currentStockDtoNew.setcInvCode(hwxx.getWlbm());
											currentStockDtoNew.setcWhCode(ckxxDto1.getCkdm());
											if (sCM_ItemDto_t !=null){
												currentStockDtoNew.setItemId(sCM_ItemDto_t.getId());
												currentStockDao.insert(currentStockDtoNew);
												currentStockDtoNew = currentStockDao.getCurrentStocksByDto(currentStockDtoNew);
												hwxxs.setKcglid(String.valueOf(currentStockDtoNew.getAutoID()));
											}else{
												SCM_ItemDto scm_itemDto = new SCM_ItemDto();
												scm_itemDto.setcInvCode(hwxx.getWlbm());
												sCM_ItemDao.insert(scm_itemDto);
												SCM_ItemDto sCM_ItemDto_t2  = sCM_ItemDao.getDtoBycInvVode(hwxx.getWlbm());
												currentStockDtoNew.setItemId(sCM_ItemDto_t2.getId());
												currentStockDtoNew.setcInvCode(hwxx.getWlbm());
												currentStockDao.insert(currentStockDtoNew);
												currentStockDtoNew = currentStockDao.getCurrentStocksByDto(currentStockDtoNew);
												hwxxs.setKcglid(String.valueOf(currentStockDtoNew.getAutoID()));
											}
										}
									}
								}
							}else{
								double iQuantity = Double.parseDouble(currentStockDto1.getiQuantity())-Double.parseDouble(hwxx.getDhsl())+Double.parseDouble(hwxxs.getDhsl());
								currentStockDto.setiQuantity(Double.toString(iQuantity));
								currentStockDao.update(currentStockDto);
							}
						}
						InvPositionDto invPositionDto = new InvPositionDto();
						invPositionDto.setRDID(dhxxDto1.getGlid());
						invPositionDto.setRdsID(hwxx.getGlid());
						invPositionDto.setcWhCode(ckxxDto.getCkdm());
						invPositionDto.setcPosCode(ckxxDto1.getCkdm());
						invPositionDto.setcBatch(hwxxs.getScph());
						invPositionDto.setdVDate(hwxxs.getYxq());
						invPositionDto.setiQuantity(hwxxs.getDhsl());
						invPositionDto.setdDate(dhxxDto.getDhrq());
						invPositionDto.setdMadeDate(hwxx.getScrq());
						invPositionDao.updateByRDIDAndRDSID(invPositionDto);
						CkxxDto ckxxDto2 = ckxxService.getDtoById(hwxx.getKwbh());
						CkxxDto ckxxDto3 = ckxxService.getDtoById(hwxx.getCkid());
						InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
						invPositionSumDto.setcWhCode(ckxxDto3.getCkdm());
						invPositionSumDto.setcPosCode(ckxxDto2.getCkdm());
						invPositionSumDto.setcInvCode(hwxx.getWlbm());
						invPositionSumDto.setcBatch(hwxx.getScph());
						InvPositionSumDto invPositionSumDto1 = invPositionSumDao.getDto(invPositionSumDto);
						InvPositionSumDto invPositionSumDto2 = new InvPositionSumDto();
						invPositionSumDto2.setcWhCode(ckxxDto.getCkdm());
						invPositionSumDto2.setcPosCode(ckxxDto1.getCkdm());
						invPositionSumDto2.setcInvCode(hwxx.getWlbm());
						invPositionSumDto2.setcBatch(hwxxs.getScph());
						InvPositionSumDto invPositionSumDto3 = invPositionSumDao.getDto(invPositionSumDto2);
						if ( !hwxx.getScph().equals(hwxxs.getScph()) || !hwxx.getKwbh().equals(hwxxs.getKwbh()) || !dhxxDto.getCkid().equals(dhxxDto1.getCkid())){
							if (hwxx.getRkzt().equals("80")){
								double invSumIq = Double.parseDouble(invPositionSumDto1.getiQuantity())- Double.parseDouble(hwxx.getKcl());
								invPositionSumDto1.setiQuantity(Double.toString(invSumIq));
							}else{
								double invSumIq = Double.parseDouble(invPositionSumDto1.getiQuantity())- Double.parseDouble(hwxx.getDhsl());
								invPositionSumDto1.setiQuantity(Double.toString(invSumIq));
							}
							invPositionSumDao.update(invPositionSumDto1);
							if (invPositionSumDto3 != null){
								double invSumIq = Double.parseDouble(invPositionSumDto1.getiQuantity()) + Double.parseDouble(hwxxs.getDhsl());
								invPositionSumDto3.setiQuantity(Double.toString(invSumIq));
								invPositionSumDao.update(invPositionSumDto3);
							}else{
								invPositionSumDto2.setiQuantity(hwxxs.getDhsl());
								invPositionSumDto2.setInum("0");
								if (hwxx.getBzqflg().equals("0")){
									invPositionSumDto2.setcMassUnit("2");
									invPositionSumDto2.setiMassDate(hwxx.getBzq());
								}
								if (hwxx.getBzqflg().equals("1")){
									invPositionSumDto2.setcMassUnit("1");
									invPositionSumDto2.setiMassDate("99");
								}
								invPositionSumDto2.setdMadeDate(hwxx.getScrq());
								invPositionSumDto2.setdVDate(hwxxs.getYxq());
								List<InvPositionSumDto> invPositionSumDtos = new ArrayList<>();
								invPositionSumDtos.add(invPositionSumDto2);
								invPositionSumDao.insertDtos(invPositionSumDtos);
							}
						}else{
							double invSumIq = Double.parseDouble(invPositionSumDto1.getiQuantity()) - Double.parseDouble(hwxx.getDhsl()) + Double.parseDouble(hwxxs.getDhsl());
							invPositionSumDto1.setiQuantity(Double.toString(invSumIq));
							invPositionSumDto1.setdMadeDate(hwxx.getScrq());
							invPositionSumDto1.setdVDate(hwxxs.getYxq());
							invPositionSumDao.update(invPositionSumDto1);
						}
						double sl = Double.parseDouble(hwxxs.getDhsl())-Double.parseDouble(hwxx.getCythsl())-Double.parseDouble(hwxx.getZjthsl())-Double.parseDouble(hwxx.getQyl());
						double nqyl = Double.parseDouble(hwxxs.getDhsl())-Double.parseDouble(hwxx.getCythsl())-Double.parseDouble(hwxx.getZjthsl());
						double Quantity = Double.parseDouble(hwxx.getSl()) +sl;
						if (!hwxx.getRkzt().equals("80")){
							hwxxs.setSl(Double.toString(sl));
							if (!hwxx.getLbbj().equals("1")){
								hwxxs.setCkid(dhxxDto.getCkid());
								hwxxs.setKwbh(ckxxDto4.getCkdm());
							}
						}else{
							double cksl = Double.parseDouble(hwxx.getSl())-Double.parseDouble(hwxx.getKcl());
							double kcl = sl - cksl;
							hwxxs.setKcl(Double.toString(kcl));
							CkhwxxDto ckhwxxDto = new CkhwxxDto();
							double ckkcl = Double.parseDouble(hwxx.getCkkcl()) - Double.parseDouble(hwxx.getKcl()) + kcl;
							ckhwxxDto.setWlid(hwxx.getWlid());
							ckhwxxDto.setKcl(Double.toString(ckkcl));
							List<CkhwxxDto> ckhwxxDtos = new ArrayList<>();
							ckhwxxDtos.add(ckhwxxDto);
							ckhwxxService.updateCkhwxxs(ckhwxxDtos);
							hwxx.setSl(Double.toString(sl));
							if (!hwxx.getLbbj().equals("1")){
								hwxxs.setCkid(dhxxDto.getCkid());
								hwxxs.setKwbh(ckxxDto4.getCkdm());
							}else{
								TransVouchDto transVouchDto = new TransVouchDto();
								transVouchDto.setcOWhCode(ckxxDto.getCkdm());
								transVouchDto.setcTVCode(hwxx.getRkdbglid());
								transVouchDao.updateByKey(transVouchDto);
								RdRecordDto recordDto1 = new RdRecordDto();
								recordDto1.setcWhCode(ckxxDto.getCkdm());
								recordDto1.setID(Integer.parseInt(hwxx.getRkdbglid()));
								rdRecordDao.updateRd09(recordDto1);
								TransVouchsDto transVouchsDto = new TransVouchsDto();
								transVouchsDto.setAutoID(Integer.parseInt(hwxx.getDbmxglid()));
								transVouchsDto = transVouchsDao.getInfoByAutoId(transVouchsDto);
								double iTVQuantity = Double.parseDouble(transVouchsDto.getiTVQuantity()) - Quantity;
								transVouchsDto.setiTVQuantity(Double.toString(iTVQuantity));
								transVouchsDto.setcTVBatch(hwxxs.getScph());
								transVouchsDto.setdDisDate(hwxxs.getYxq());
								transVouchsDto.setCoutposcode(ckxxDto4.getCkdm());
								transVouchsDto.setdMadeDate(hwxxs.getScrq());
								transVouchsDao.updateByid(transVouchsDto);
								RdRecordsDto rdRecordsDto1 = rdRecordsDao.getRds08(Integer.parseInt(hwxx.getQtrkmxid()));
								double rds08i = Double.parseDouble(rdRecordsDto1.getiQuantity())-Quantity;
								rdRecordsDto1.setiQuantity(Double.toString(rds08i));
								double rds08Price = sl * Double.parseDouble(hwxx.getWsdj());
								rdRecordsDto1.setiPrice(Double.toString(rds08Price));
								rdRecordsDto1.setiAPrice(Double.toString(rds08Price));
								rdRecordsDto1.setcBatch(hwxxs.getScph());
								rdRecordsDto1.setdVDate(hwxxs.getYxq());
								double rds08iN = Double.parseDouble(rdRecordsDto1.getiNQuantity())-Quantity;
								rdRecordsDto1.setiNQuantity(Double.toString(rds08iN));
								rdRecordsDto1.setdMadeDate(hwxxs.getScrq());
								rdRecordsDto1.setChVencode(gysdm);
//								rdRecordsDto1.setiOriMoney(rds08Price.toString());
								//Double iOriTaxPricerds08 = sl*(Double.parseDouble(hwxx.getHsdj())/rds08Price);
//								rdRecordsDto1.setiOriTaxPrice(iOriTaxPricerds08.toString());
//								rdRecordsDto1.setiTaxPrice(iOriTaxPricerds08.toString());
								//Double IoriSumRds08 = sl*Double.parseDouble(hwxx.getHsdj());
//								rdRecordsDto1.setIoriSum(IoriSumRds08.toString());
//								rdRecordsDto1.setiSum(IoriSumRds08.toString());
								rdRecordsDto1.setcDefine23(hwxxs.getGysmc());
								rdRecordsDto1.setcDefine22(hwxxs.getZsh());
								rdRecordsDao.updateRdRecords08Info(rdRecordsDto1);
								RdRecordsDto rdRecordsDto2 = rdRecordsDao.getRds09(Integer.parseInt(hwxx.getQtckmxid()));
								double rds09i = Double.parseDouble(rdRecordsDto2.getiQuantity())-Quantity;
								rdRecordsDto2.setiQuantity(Double.toString(rds09i));
								rdRecordsDto2.setiPrice(Double.toString(rds08Price));
								rdRecordsDto2.setiAPrice(Double.toString(rds08Price));
//								rdRecordsDto2.setiOriMoney(rds08Price.toString());
								rdRecordsDto2.setcBatch(hwxxs.getScph());
								rdRecordsDto2.setdVDate(hwxxs.getYxq());
								rdRecordsDto2.setdMadeDate(hwxxs.getScrq());
//								rdRecordsDto2.setChVencode(gysdm);
								double rds09iN = Double.parseDouble(rdRecordsDto2.getiNQuantity())-Quantity;
								rdRecordsDto2.setiNQuantity(Double.toString(rds09iN));
//								rdRecordsDto2.setIoriSum(IoriSumRds08.toString());
//								rdRecordsDto2.setiTaxPrice(iOriTaxPricerds08.toString());
//								rdRecordsDto2.setiOriTaxPrice(iOriTaxPricerds08.toString());
//								rdRecordsDto2.setiSum(IoriSumRds08.toString());
								rdRecordsDto2.setcDefine23(hwxxs.getGysmc());
								rdRecordsDto2.setcDefine22(hwxxs.getZsh());
								rdRecordsDto2.setcPosition(ckxxDto4.getCkdm());
								rdRecordsDao.updateRdRecords09Info(rdRecordsDto2);
								InvPositionDto invPositionDto1 = new InvPositionDto();
								invPositionDto1.setRdsID(hwxx.getQtrkmxid());
								invPositionDto1.setRDID(hwxx.getRkdbglid());
								invPositionDto1.setcBatch(hwxxs.getScph());
								invPositionDto1.setdVDate(hwxxs.getYxq());
								invPositionDto1.setiQuantity(Double.toString(sl));
								invPositionDto1.setdMadeDate(hwxxs.getScrq());
								invPositionDao.updateByRDIDAndRDSID(invPositionDto1);
								invPositionDto1.setRdsID(hwxx.getQtckmxid());
								invPositionDto1.setRDID(hwxx.getQtckglid());
								invPositionDto1.setcBatch(hwxxs.getScph());
								invPositionDto1.setdVDate(hwxxs.getYxq());
								invPositionDto1.setdMadeDate(hwxxs.getScrq());
								invPositionDto1.setcWhCode(ckxxDto.getCkdm());
								invPositionDto1.setcPosCode(ckxxDto4.getCkdm());
								invPositionDto1.setiQuantity(Double.toString(nqyl));
								invPositionDao.updateByRDIDAndRDSID(invPositionDto1);
								CurrentStockDto currentStockDtos = new CurrentStockDto();
								currentStockDtos.setAutoID(Integer.parseInt(hwxx.getKcglid()));
								currentStockDtos = currentStockDao.getCurrentStockByAutoID(currentStockDtos);
								if (!hwxxs.getCkid().equals(hwxx.getCkid()) || !hwxxs.getScph().equals(hwxx.getScph())){
									double cQuantity = Double.parseDouble(currentStockDtos.getiQuantity()) - Double.parseDouble(hwxx.getKcl());
									currentStockDtos.setiQuantity(Double.toString(cQuantity));
									currentStockDao.update(currentStockDtos);
									currentStockDtos.setcInvCode(hwxx.getWlbm());
									currentStockDtos.setcBatch(hwxxs.getScph());
									currentStockDtos.setcWhCode(ckxxDto1.getCkdm());
									CurrentStockDto currentStockDtot = currentStockDao.getCurrentStocksByDto(currentStockDtos);
									if (currentStockDtot != null){
										double iQuantity2 = Double.parseDouble(currentStockDtot.getiQuantity())+Double.parseDouble(hwxxs.getDhsl());
										currentStockDtot.setiQuantity(Double.toString(iQuantity2));
										currentStockDao.update(currentStockDtot);
									}else{
										SCM_ItemDto sCM_ItemDto_t  = sCM_ItemDao.getDtoBycInvVode(hwxx.getWlbm());
										CurrentStockDto currentStockDtoNew = new CurrentStockDto();
										currentStockDtoNew.setiQuantity(hwxxs.getDhsl());
										currentStockDtoNew.setcBatch(hwxxs.getScph());
										currentStockDtoNew.setcWhCode(ckxxDto1.getCkdm());
										currentStockDtoNew.setcInvCode(hwxx.getWlbm());
										if (sCM_ItemDto_t !=null){
											currentStockDtoNew.setItemId(sCM_ItemDto_t.getId());
											currentStockDao.insert(currentStockDtoNew);
											currentStockDtoNew = currentStockDao.getCurrentStocksByDto(currentStockDtoNew);
											hwxxs.setKcglid(String.valueOf(currentStockDtoNew.getAutoID()));
										}else{
											SCM_ItemDto scm_itemDto = new SCM_ItemDto();
											scm_itemDto.setcInvCode(hwxx.getWlbm());
											sCM_ItemDao.insert(scm_itemDto);
											SCM_ItemDto sCM_ItemDto_t2  = sCM_ItemDao.getDtoBycInvVode(hwxx.getWlbm());
											currentStockDtoNew.setItemId(sCM_ItemDto_t2.getId());
											currentStockDtoNew.setcInvCode(hwxx.getWlbm());
											currentStockDao.insert(currentStockDtoNew);
											currentStockDtoNew = currentStockDao.getCurrentStocksByDto(currentStockDtoNew);
											hwxxs.setKcglid(String.valueOf(currentStockDtoNew.getAutoID()));
										}
									}
								}else {
									double iQuantity = Double.parseDouble(currentStockDtos.getiQuantity())-Double.parseDouble(hwxx.getKcl())+Double.parseDouble(hwxxs.getDhsl()) +nqyl;
									currentStockDtos.setiQuantity(Double.toString(iQuantity));
									currentStockDtos.setdMdate(hwxxs.getScrq());
									currentStockDtos.setdVDate(hwxxs.getYxq());
									currentStockDao.update(currentStockDtos);
								}
								InvPositionSumDto invPositionSumDtotb = new InvPositionSumDto();
								invPositionSumDtotb.setcWhCode(ckxxDto3.getCkdm());
								invPositionSumDtotb.setcPosCode(ckxxDto2.getCkdm());
								invPositionSumDtotb.setcInvCode(hwxx.getWlbm());
								invPositionSumDtotb.setcBatch(hwxx.getScph());
								InvPositionSumDto invPositionSumDtotbs = invPositionSumDao.getDto(invPositionSumDtotb);
								if ( !hwxx.getScph().equals(hwxxs.getScph()) || !hwxx.getKwbh().equals(hwxxs.getKwbh()) || !dhxxDto.getCkid().equals(dhxxDto1.getCkid())){

									double invSumIq = Double.parseDouble(invPositionSumDtotbs.getiQuantity())- Double.parseDouble(hwxx.getKcl());
									invPositionSumDtotbs.setiQuantity(Double.toString(invSumIq));
									invPositionSumDao.update(invPositionSumDtotbs);
									InvPositionSumDto invPositionSumDtot = new InvPositionSumDto();
									invPositionSumDtot.setcWhCode(ckxxDto.getCkdm());
									invPositionSumDtot.setcPosCode(ckxxDto1.getCkdm());
									invPositionSumDtot.setcInvCode(hwxx.getWlbm());
									invPositionSumDtot.setcBatch(hwxxs.getScph());
									InvPositionSumDto invPositionSumDto4 = invPositionSumDao.getDto(invPositionSumDtot);
									if (invPositionSumDto4 != null){
										double invSumIq3 = Double.parseDouble(invPositionSumDtotbs.getiQuantity()) + nqyl;
										invPositionSumDto4.setiQuantity(Double.toString(invSumIq3));
										invPositionSumDao.update(invPositionSumDto4);
									}else{
										invPositionSumDtot.setiQuantity(Double.toString(nqyl));
										invPositionSumDtot.setInum("0");
										if (hwxx.getBzqflg().equals("0")){
											invPositionSumDtot.setcMassUnit("2");
											invPositionSumDtot.setiMassDate(hwxx.getBzq());
										}
										if (hwxx.getBzqflg().equals("1")){
											invPositionSumDtot.setcBatch("1");
											invPositionSumDtot.setcBatch("99");
										}
										invPositionSumDtot.setdMadeDate(hwxx.getScrq());
										invPositionSumDtot.setdVDate(hwxxs.getYxq());
										List<InvPositionSumDto> invPositionSumDtos = new ArrayList<>();
										invPositionSumDtos.add(invPositionSumDto);
										invPositionSumDao.insertDtos(invPositionSumDtos);
									}
								}else{
									double invSumIq = Double.parseDouble(invPositionSumDtotbs.getiQuantity()) - Double.parseDouble(hwxx.getKcl()) + nqyl;
									invPositionSumDtotbs.setiQuantity(Double.toString(invSumIq));
									invPositionSumDtotbs.setdMadeDate(hwxx.getScrq());
									invPositionSumDtotbs.setdVDate(hwxxs.getYxq());
									invPositionSumDao.update(invPositionSumDtotbs);
								}
							}
						}
						if(hwxx.getLlzt().equals("80")){
							MaterialAppVouchsDto materialAppVouchsDto = new MaterialAppVouchsDto();
							materialAppVouchsDto.setdVDate(hwxxs.getYxq());
							materialAppVouchsDto.setdMadeDate(hwxxs.getScrq());
							materialAppVouchsDto.setcBatch(hwxxs.getScph());
							materialAppVouchsDto.setAutoID(hwxxs.getHwmxglid());
							materialAppVouchsDao.update(materialAppVouchsDto);
						}
						if(hwxx.getCkzt().equals("80")){
							RdRecordsDto rdRecordsDto1 = new RdRecordsDto();
							rdRecordsDto1.setAutoID(Integer.parseInt(hwxx.getMxckglid()));
							rdRecordsDto1.setcBatch(hwxxs.getScph());
							rdRecordsDto1.setdVDate(hwxxs.getYxq());
							rdRecordsDto1.setdMadeDate(hwxxs.getScrq());
							rdRecords11Dao.update(rdRecordsDto1);
							rdRecordsDto1 = rdRecords11Dao.getDtoByAutoId(rdRecordsDto1);
							InvPositionDto invPositionDto1 = new InvPositionDto();
							invPositionDto1.setRdsID(String.valueOf(rdRecordsDto1.getAutoID()));
							invPositionDto1.setRDID(String.valueOf(rdRecordsDto1.getID()));
							if (hwxx.getLbbj().equals("1")){
								invPositionDto1.setcWhCode(ckxxDto.getCkdm());
								invPositionDto1.setcPosCode(ckxxDto4.getCkdm());
							}
							invPositionDto1.setcBatch(hwxxs.getScph());
							invPositionDto1.setdVDate(hwxxs.getYxq());
							invPositionDto1.setdMadeDate(hwxxs.getScrq());
							invPositionDao.updateByRDIDAndRDSID(invPositionDto1);
						}
					}
				}
			}
		}
		hwxxService.updateHwxxDtos(hwxxDtos);
	}
	/**
	 * 更改取样量
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public void pdQyl(List<HwxxDto> hwxxlist,List<HwxxDto> hwxxDtos,double sumSaveQYL,double sumQYL){
		
		HwxxDto hwxxDto_a = new HwxxDto();
		for (HwxxDto dto : hwxxDtos) {
			if (StringUtil.isNotBlank(dto.getQyrq())) {
				hwxxDto_a = dto;
				break;
			}
		}
		double mxqyl = Double.parseDouble(hwxxDto_a.getMxqlsl())+sumSaveQYL-sumQYL;
		double xxqyl = Double.parseDouble(hwxxDto_a.getXxqlsl())+sumSaveQYL-sumQYL;
		HwllmxDto hwllmxDto = new HwllmxDto();
		hwllmxDto.setQlsl(Double.toString(mxqyl));
		hwllmxDto.setLlmxid(hwxxDto_a.getLlmxid());
		hwllmxService.updateqlsl(hwllmxDto);
		HwllxxDto hwllxxDto = new HwllxxDto();
		hwllxxDto.setQlsl(Double.toString(xxqyl));
		hwllxxDto.setHwllid(hwxxDto_a.getHwllid());
		hwllxxDao.updateqlsl(hwllxxDto);

		for (HwxxDto hwxxDto : hwxxDtos){
			for (HwxxDto hwxxDto1 : hwxxlist){
				if (hwxxDto1.getHwid().equals(hwxxDto.getHwid())){
					double sl = Double.parseDouble(hwxxDto.getDhsl())- Double.parseDouble(hwxxDto.getCythsl())- Double.parseDouble(hwxxDto1.getZjthsl())-Double.parseDouble(hwxxDto1.getQyl());
					hwxxDto.setSl(Double.toString(sl));
					if (hwxxDto.getRkzt().endsWith("80")){
						double hwxxkcl = Double.parseDouble(hwxxDto.getKcl())+Double.parseDouble(hwxxDto.getQyl())-Double.parseDouble(hwxxDto1.getQyl());
						hwxxDto.setKcl(Double.toString(hwxxkcl));
						hwxxService.updateByHwId(hwxxDto);
						CkhwxxDto ckhwxxDto = ckhwxxService.getCkhwInfo(hwxxDto.getWlid());
						double xkhwkcl = Double.parseDouble(ckhwxxDto.getKcl())+Double.parseDouble(hwxxDto.getQyl())-Double.parseDouble(hwxxDto1.getQyl());
						ckhwxxDto.setKcl(Double.toString(xkhwkcl));
						ckhwxxService.updateCkhwxx(ckhwxxDto);
						//u8操作
						//transVouchs表
						TransVouchsDto transVouchsDto = new TransVouchsDto();
						transVouchsDto.setAutoID(Integer.parseInt(hwxxDto.getDbmxglid()));
						transVouchsDto.setiTVQuantity(Double.toString(sl));
						transVouchsDao.updateByid(transVouchsDto);
						//rdRecords08
						RdRecordsDto rdRecordsDto = new RdRecordsDto();
						rdRecordsDto.setAutoID(Integer.parseInt(hwxxDto.getQtrkmxid()));
						rdRecordsDto.setiQuantity(Double.toString(sl));
						Double wszje = Double.parseDouble(hwxxDto.getWsdj())*sl;
						rdRecordsDto.setiPrice(wszje.toString());
						rdRecordsDto.setiAPrice(wszje.toString());
						rdRecordsDto.setiOriMoney(wszje.toString());
						Double hszje = Double.parseDouble(hwxxDto.getHsdj())*sl;
						double se = hszje-wszje;
						rdRecordsDto.setiOriTaxPrice(Double.toString(se));
						rdRecordsDto.setiTaxPrice(Double.toString(se));
						rdRecordsDao.updateRdRecords08Info(rdRecordsDto);
						//invPositionDto
						InvPositionDto invPositionDto = new InvPositionDto();
						invPositionDto.setRdsID(String.valueOf(rdRecordsDto.getAutoID()));
						invPositionDto.setiQuantity(Double.toString(sl));
						invPositionDao.updateByRdsID(invPositionDto);
						//rdRecords09
						rdRecordsDto.setAutoID(Integer.parseInt(hwxxDto.getQtckmxid()));
						rdRecordsDto.setiNQuantity(Double.toString(sl));
						rdRecordsDao.updateRdRecords09Info(rdRecordsDto);
						//invPositionDto
						invPositionDto.setRdsID(String.valueOf(rdRecordsDto.getAutoID()));
						invPositionDao.updateByRdsID(invPositionDto);
						//invPositionSum
						InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
						invPositionSumDto.setcWhCode(hwxxDto.getCkdm());
						invPositionSumDto.setcPosCode(hwxxDto.getKwbhdm());
						invPositionSumDto.setcInvCode(hwxxDto.getWlbm());
						invPositionSumDto.setcBatch(hwxxDto.getScph());
						invPositionSumDto.setiQuantity(Double.toString(sl));
						invPositionSumDao.updateByHwxx(invPositionSumDto);
						//currentStock
						CurrentStockDto currentStockDto = new CurrentStockDto();
						currentStockDto.setAutoID(Integer.parseInt(hwxxDto.getKcglid()));
						currentStockDto.setiQuantity(Double.toString(sl));
						currentStockDao.update(currentStockDto);

					}else{
						hwxxService.updateByHwId(hwxxDto);
					}
				}
			}
		}
	}

	@Override
	public void updateRkglUpdate(RkglDto rkglDto, RkglDto rkglDto_h) {
	}

	/**
	 * 更改退回数量
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public void ggthsl(List<HwxxDto> hwxxlist,List<HwxxDto> hwxxDtos,double sumSaveQYL,double sumQYL){
		pdQyl(hwxxlist,hwxxDtos,sumSaveQYL,sumQYL);
		for (HwxxDto hwxxDto : hwxxDtos){
			for (HwxxDto hwxxDto1 : hwxxlist){
				if (hwxxDto1.getHwid().equals(hwxxDto.getHwid())){
					//u8操作
					//transVouchs表
					TransVouchsDto transVouchsDto = new TransVouchsDto();
					transVouchsDto.setAutoID(Integer.parseInt(hwxxDto.getBhgglid()));
					transVouchsDto.setiTVQuantity(hwxxDto1.getZjthsl());
					transVouchsDao.updateByid(transVouchsDto);
					//rdRecords08
					RdRecordsDto rdRecordsDto = new RdRecordsDto();
					rdRecordsDto.setAutoID(Integer.parseInt(hwxxDto.getQtrkmxid()));
					rdRecordsDto.setiQuantity(hwxxDto1.getZjthsl());
					Double thwszje = Double.parseDouble(hwxxDto.getWsdj())*Double.parseDouble(hwxxDto1.getZjthsl());
					rdRecordsDto.setiPrice(thwszje.toString());
					rdRecordsDto.setiAPrice(thwszje.toString());
					rdRecordsDto.setiOriMoney(thwszje.toString());
					Double thhszje = Double.parseDouble(hwxxDto.getHsdj())*Double.parseDouble(hwxxDto1.getZjthsl());
					double thse = thhszje-thwszje;
					rdRecordsDto.setiOriTaxPrice(Double.toString(thse));
					rdRecordsDto.setiTaxPrice(Double.toString(thse));
					rdRecordsDao.updateRdRecords08Info(rdRecordsDto);
					//invPositionDto
					InvPositionDto invPositionDto = new InvPositionDto();
					invPositionDto.setRdsID(String.valueOf(rdRecordsDto.getAutoID()));
					invPositionDto.setiQuantity(hwxxDto1.getZjthsl());
					invPositionDao.updateByRdsID(invPositionDto);
					//rdRecords09
					rdRecordsDto.setAutoID(Integer.parseInt(hwxxDto.getQtckmxid()));
					rdRecordsDto.setiNQuantity(hwxxDto1.getZjthsl());
					rdRecordsDao.updateRdRecords09Info(rdRecordsDto);
					//invPositionDto
					invPositionDto.setRdsID(String.valueOf(rdRecordsDto.getAutoID()));
					invPositionDao.updateByRdsID(invPositionDto);
					//invPositionSum
					InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
					invPositionSumDto.setcWhCode(hwxxDto.getCkdm());
					invPositionSumDto.setcPosCode(hwxxDto.getKwbhdm());
					invPositionSumDto.setcInvCode(hwxxDto.getWlbm());
					invPositionSumDto.setcBatch(hwxxDto.getScph());
					double zjthsl =Double.parseDouble(hwxxDto.getZjthsl()) - Double.parseDouble(hwxxDto1.getZjthsl());
					invPositionSumDto.setiQuantity(Double.toString(zjthsl));
					invPositionSumDao.updateByHwxx(invPositionSumDto);
					//currentStock
					CurrentStockDto currentStockDto = new CurrentStockDto();
					currentStockDto.setAutoID(Integer.parseInt(hwxxDto.getKcglid()));
					currentStockDto.setiQuantity(Double.toString(zjthsl));
					currentStockDao.update(currentStockDto);

					currentStockDto.setcInvCode(hwxxDto.getWlbm());
					currentStockDto.setcBatch(hwxxDto.getScph());
					currentStockDto.setcWhCode("12");
					currentStockDto.setiQuantity(hwxxDto1.getZjthsl());
					currentStockDao.updateByHwxx(currentStockDto);
				}
			}
		}
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public void updateLlglUpdate(LlglDto llglDto, LlglDto llglDto_h) {
		List<HwllxxDto> hwllxxlist = JSON.parseArray(llglDto.getLlmx_json(),HwllxxDto.class);
		List<HwllxxDto> hwllxxDtos = hwllxxDao.getDtoHwllxxListByLlid(llglDto.getLlid());
		MaterialAppVouchDto materialAppVouchDto = new MaterialAppVouchDto();
		materialAppVouchDto.setdDate(llglDto.getSqrq());
		materialAppVouchDto.setcCode(llglDto.getLldh());
		DepartmentDto departmentDto = commonService.getJgxxInfo(llglDto.getSqbm());
		materialAppVouchDto.setcDepCode(departmentDto.getJgdm());
		materialAppVouchDto.setCsysbarcode("||st64|"+llglDto.getLldh());
		materialAppVouchDto.setID(llglDto_h.getGlid());
		materialAppVouchDao.update(materialAppVouchDto);
		for (HwllxxDto hwllxxDto : hwllxxDtos){
			for (HwllxxDto hwllxxDto1 : hwllxxlist){
				if (hwllxxDto1.getHwllid().equals(hwllxxDto.getHwllid())){
					MaterialAppVouchsDto materialAppVouchsDto = materialAppVouchsDao.getMatersInfo(hwllxxDto.getLlmxglid());
					materialAppVouchsDto.setiQuantity(hwllxxDto1.getQlsl());
					if (StringUtil.isNotBlank(hwllxxDto1.getBz())){
						materialAppVouchsDto.setCbMemo(hwllxxDto1.getBz());
					}
					materialAppVouchsDto.setCbsysbarcode(materialAppVouchDto.getCsysbarcode()+"|"+materialAppVouchsDto.getIrowno());
					materialAppVouchsDao.update(materialAppVouchsDto);
					HwxxDto hwxxDto = new HwxxDto();
					hwxxDto.setHwid(hwllxxDto.getHwid());
					double yds = Double.parseDouble(hwllxxDto.getYds())-Double.parseDouble(hwllxxDto.getQlsl())+Double.parseDouble(hwllxxDto1.getQlsl());
					hwxxDto.setYds(Double.toString(yds));
					hwxxService.updateByHwId(hwxxDto);
					HwllmxDto hwllmxDto = new HwllmxDto();
					hwllmxDto.setLlmxid(hwllxxDto.getLlmxid());
					hwllmxDto.setQlsl(hwllxxDto1.getQlsl());
					hwllmxService.updateqlsl(hwllmxDto);
				}
			}
		}
		if("80".equals(llglDto_h.getCkzt())) {
			RdRecordDto rdRecordDto = new RdRecordDto();
			rdRecordDto.setID(Integer.parseInt(llglDto_h.getCkglid()));
			rdRecordDto.setcBusCode(llglDto.getLldh());
			rdRecord11Dao.update(rdRecordDto);
		}
	}

	/**
	 * 调拨新增U8调拨
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public Map<String, Object> greqtU8DbData(DbglDto dbglDto) throws BusinessException {
		
		Map<String,Object> map=new HashMap<>();
		List<HwxxDto> hwxxs = dbmxService.queryByDbid(dbglDto.getDbid());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdf_ccode = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); //存入账套
		uA_IdentityDto.setcVouchType("tr");	//存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
		//U8生成调拨单
		if(hwxxs==null || hwxxs.size()<=0)
			throw new BusinessException("ICOM99019","货物明细为空！");
		DbglDto dbglDto_t = dbglDao.getDtoById(dbglDto.getDbid());
		TransVouchDto transVouchDto = new TransVouchDto();
		//生成单号
		VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
		voucherHistoryDto.setCardNumber("0304");
		//获取最大流水号
		VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
		if(voucherHistoryDto_t!=null) {
			String serialnum = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
			String serial = "0000000000"+ serialnum;
			serial = serial.substring(serial.length()-10);
			transVouchDto.setcTVCode(serial); // 到货单号
			//判断单号是否存在
			int ccode_new=0;
			TransVouchDto transVouchDto_t = transVouchDao.getDtoBycTVCode(transVouchDto);			
			if(transVouchDto_t!=null) {
				List<TransVouchDto> rdList = transVouchDao.getcTVCode(transVouchDto);
				ccode_new = Integer.parseInt(transVouchDto.getcTVCode()) + 1;
				for (TransVouchDto transVouchDto_c : rdList) {
					if(ccode_new-Integer.parseInt(transVouchDto_c.getcTVCode())==0) {
						ccode_new = ccode_new+1;
					}else {
						break;
					}
				}				
			}
			if(ccode_new!=0) {
				transVouchDto.setcTVCode(("000000000"+ccode_new).substring(("000000000"+ccode_new).length()-10)); // 调拨单号
	        	voucherHistoryDto_t.setcNumber(String.valueOf(ccode_new));
	        }else {
	        	voucherHistoryDto_t.setcNumber(serialnum);
	        }    
			//更新最大单号值		
			int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
			if(result_ccode<1)
				throw new BusinessException("msg", "更新U8最大单号失败！");
		}else {
			voucherHistoryDto.setcNumber("1");
			voucherHistoryDto.setbEmpty("0");
			transVouchDto.setcTVCode("0000000001");
			int result_ccode_in = voucherHistoryDao.insert(voucherHistoryDto);
			if(result_ccode_in<1)
				throw new BusinessException("msg", "新增U8最大单号失败！");
		}
		
		transVouchDto.setcOWhCode(dbglDto_t.getZcckdm());//转出仓库
		transVouchDto.setcIWhCode(dbglDto_t.getZrckdm());//转入仓库
		transVouchDto.setcTVMemo(StringUtil.isNotBlank(dbglDto_t.getBz())?(dbglDto_t.getBz().length()>255?dbglDto_t.getBz().substring(0,255):dbglDto_t.getBz()):null);//备注
		transVouchDto.setcMaker(dbglDto_t.getLrrymc());//录入人员名称
		transVouchDto.setcIRdCode("3");
		transVouchDto.setcORdCode("7");
		transVouchDto.setiNetLock("0");
		transVouchDto.setCsysbarcode("||st12|"+transVouchDto.getcTVCode());
		transVouchDto.setcVerifyPerson(dbglDto_t.getLrrymc());
		if(uA_IdentityDto_t!=null) {
			if(StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				uA_IdentityDto_t.setiFatherId(String.valueOf(id +1));
				transVouchDto.setID(String.valueOf((id + 1000000001)));
			}
		}else {
			uA_IdentityDto.setiFatherId("1");
			uA_IdentityDto.setiChildId("0");
			transVouchDto.setID("1000000001");
		}
		boolean insertTransVouch=transVouchDao.insertTransVouch(transVouchDto);
		
		dbglDto.setGlid(transVouchDto.getID());//返回调拨单关联ID
		
		if(!insertTransVouch)
			throw new BusinessException("msg","U8添加调拨单失败！");
		
		//处理调拨入库主表
		RdRecordDto inrdRecordDto=new RdRecordDto();
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyyMM");
		String prefix_in = sdf_d.format(date);
		VoucherHistoryDto inVoucherHistoryDto = new VoucherHistoryDto();
		inVoucherHistoryDto.setcSeed(prefix_in);
		inVoucherHistoryDto.setCardNumber("0301");
		//获取最大流水号 210900053
		VoucherHistoryDto inVoucherHistoryDto_t = voucherHistoryDao.getMaxSerial(inVoucherHistoryDto);
		if(inVoucherHistoryDto_t!=null) {
			String serial_in = String.valueOf(Integer.parseInt(inVoucherHistoryDto_t.getcNumber())+1);
			inrdRecordDto.setcCode(prefix_in.substring(2)+"000"+(serial_in.length()>1?serial_in:"0"+serial_in)); // 到货单号
			inrdRecordDto.setPrefix(prefix_in.substring(2));
			inrdRecordDto.setSflxbj("8");
			RdRecordDto rdRecordDtos_t = rdRecordDao.queryByCode(inrdRecordDto);
			int ccode_new_in=0;
	        if (rdRecordDtos_t != null ) {
	            List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(inrdRecordDto);
	            ccode_new_in = Integer.parseInt(inrdRecordDto.getcCode()) + 1;
	            for (RdRecordDto rdRecordDto_c_in : rdList) {
					if(ccode_new_in-Integer.parseInt(rdRecordDto_c_in.getcCode())==0) {
						ccode_new_in = ccode_new_in+1;
					}else {
						break;
					}
				}
	        }
	        if(ccode_new_in!=0) {
	        	inrdRecordDto.setcCode(String.valueOf(ccode_new_in)); // 到货单号
	        	inVoucherHistoryDto_t.setcNumber(String.valueOf(ccode_new_in).substring(4)); //去除多余0
	        }else {
	        	inVoucherHistoryDto_t.setcNumber(serial_in);
	        }        
			//更新最大单号值		
			int result_ccode_in = voucherHistoryDao.update(inVoucherHistoryDto_t);
			if(result_ccode_in<1)
				throw new BusinessException("msg", "更新U8最大单号失败！");
		}else {
			inVoucherHistoryDto.setiRdFlagSeed("1");
			inVoucherHistoryDto.setcContent("日期");
			inVoucherHistoryDto.setcContentRule("月");
			inVoucherHistoryDto.setbEmpty("0");
			inVoucherHistoryDto.setcNumber("1");
			inrdRecordDto.setcCode(prefix_in+"00001");
			//单号最大表增加数据
			int result_ccode_in = voucherHistoryDao.insert(inVoucherHistoryDto);
			if(result_ccode_in<1)
				throw new BusinessException("msg", "新增U8最大单号失败！");
		}
		inrdRecordDto.setcBusCode(transVouchDto.getcTVCode());
		List<RdRecordDto> inrdRecordDtos_t = rdRecordDao.getDtoListFromRd8(inrdRecordDto);
		if(inrdRecordDtos_t!=null && inrdRecordDtos_t.size()>0) {
			throw new BusinessException("msg","U8此单号已存在！");
		}
		UA_IdentityDto rk_uA_IdentityDto = new UA_IdentityDto();
		rk_uA_IdentityDto.setcAcc_Id(accountName); //存入账套
		rk_uA_IdentityDto.setcVouchType("rd");	//存入表标识
		UA_IdentityDto rk_uA_IdentityDto_t = uA_IdentityDao.getMax(rk_uA_IdentityDto); //获取主键最大值
		if(rk_uA_IdentityDto_t!=null) {
			if(StringUtil.isNotBlank(rk_uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(rk_uA_IdentityDto_t.getiFatherId());
				rk_uA_IdentityDto_t.setiFatherId(String.valueOf(id +1));
				inrdRecordDto.setID(id + 1000000001);
			}
		}else {
			rk_uA_IdentityDto.setiFatherId("1");
			rk_uA_IdentityDto.setiChildId("0");
			inrdRecordDto.setID(1000000001);
		}
		inrdRecordDto.setcSource("调拨");
		inrdRecordDto.setcMaker(dbglDto_t.getLrrymc()); //请购申请人
		inrdRecordDto.setDnmaketime(sdf.format(date));
		inrdRecordDto.setcVouchType("08");
		inrdRecordDto.setCsysbarcode("||st08|"+inrdRecordDto.getcCode());	//	"||st01|"+入库单	
		inrdRecordDto.setcWhCode(dbglDto_t.getZrckdm()); //仓库代码
		inrdRecordDto.setcBusType("调拨入库"); // 类型)
		inrdRecordDto.setdDate(sdf_ccode.format(date)); // 入库日期 
		inrdRecordDto.setcRdCode("3");
		inrdRecordDto.setcHandler(dbglDto_t.getLrrymc());
//				inrdRecordDto.setVT_ID("67");
		
		boolean result = rdRecordDao.insertRd08(inrdRecordDto);
		if(!result) {
			throw new BusinessException("msg","其他入库数据存入U8失败！");
		}
		
		dbglDto.setQtrkglid(String.valueOf(inrdRecordDto.getID()));//其他入库单关联ID

	
		//处理调拨出库
		RdRecordDto outrdRecordDto=new RdRecordDto();
		VoucherHistoryDto outVoucherHistoryDto = new VoucherHistoryDto();
		outVoucherHistoryDto.setcSeed(prefix_in);
		outVoucherHistoryDto.setCardNumber("0302");
		//获取最大流水号 210900053
		VoucherHistoryDto outVoucherHistoryDto_t = voucherHistoryDao.getMaxSerial(outVoucherHistoryDto);
		if(outVoucherHistoryDto_t!=null) {
			String serial_out = String.valueOf(Integer.parseInt(outVoucherHistoryDto_t.getcNumber())+1);
			outrdRecordDto.setcCode(prefix_in.substring(2)+"000"+(serial_out.length()>1?serial_out:"0"+serial_out)); // 到货单号
			outrdRecordDto.setPrefix(prefix_in.substring(2));
			outrdRecordDto.setSflxbj("9");
			RdRecordDto outrdRecordDtos_t = rdRecordDao.queryByCode(outrdRecordDto);
			int ccode_new_out=0;
	        if (outrdRecordDtos_t != null ) {
	            List<RdRecordDto> rdList_out = rdRecordDao.getcCodeRd(outrdRecordDto);
	            ccode_new_out = Integer.parseInt(inrdRecordDto.getcCode()) + 1;
	            for (RdRecordDto rdRecordDto_c_out : rdList_out) {
					if(ccode_new_out-Integer.parseInt(rdRecordDto_c_out.getcCode())==0) {
						ccode_new_out = ccode_new_out+1;
					}else {
						break;
					}
				}
	        }
	        if(ccode_new_out!=0) {
	        	outrdRecordDto.setcCode(String.valueOf(ccode_new_out)); // 到货单号
	        	outVoucherHistoryDto_t.setcNumber(String.valueOf(ccode_new_out).substring(4)); //去除多余0
	        }else {
	        	outVoucherHistoryDto_t.setcNumber(serial_out);
	        }        
			//更新最大单号值		
			int result_ccode_in = voucherHistoryDao.update(outVoucherHistoryDto_t);
			if(result_ccode_in<1)
				throw new BusinessException("msg", "更新U8最大单号失败！");
		}else {
			outVoucherHistoryDto.setiRdFlagSeed("2");
			outVoucherHistoryDto.setcContent("日期");
			outVoucherHistoryDto.setcContentRule("月");
			outVoucherHistoryDto.setbEmpty("0");
			outVoucherHistoryDto.setcNumber("1");
			outrdRecordDto.setcCode(prefix_in+"00001");
			//单号最大表增加数据
			int result_ccode_in = voucherHistoryDao.insert(outVoucherHistoryDto);
			if(result_ccode_in<1)
				throw new BusinessException("msg", "新增U8最大单号失败！");
		}
		outrdRecordDto.setcBusCode(transVouchDto.getcTVCode());
		outrdRecordDto.setcRdCode("7");
		outrdRecordDto.setVT_ID("85");
		
		if(rk_uA_IdentityDto_t!=null) {
			if(StringUtil.isNotBlank(rk_uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(rk_uA_IdentityDto_t.getiFatherId());
				rk_uA_IdentityDto_t.setiFatherId(String.valueOf(id +1));
				outrdRecordDto.setID(id + 1000000001);
			}
		}else {
			if(StringUtil.isNotBlank(rk_uA_IdentityDto.getiFatherId())) {
				int id = Integer.parseInt(rk_uA_IdentityDto.getiFatherId());
				rk_uA_IdentityDto.setiFatherId(String.valueOf(id +1));
				outrdRecordDto.setID(id + 1000000001);
			}
		}
		outrdRecordDto.setcSource("调拨");
		outrdRecordDto.setcMaker(dbglDto_t.getLrrymc()); //请购申请人
		outrdRecordDto.setDnmaketime(sdf.format(date));
		outrdRecordDto.setcVouchType("09");
		outrdRecordDto.setCsysbarcode("||st09|"+outrdRecordDto.getcCode());	//	"||st09|"+出库单	
		outrdRecordDto.setcWhCode(dbglDto_t.getZcckdm()); //仓库代码
		outrdRecordDto.setcBusType("调拨出库"); // 类型)
		outrdRecordDto.setdDate(sdf_ccode.format(date)); // 出库日期 
		outrdRecordDto.setcHandler(dbglDto_t.getLrrymc());
		boolean ck_result = rdRecordDao.insertRd09(outrdRecordDto);
		if(!ck_result) {
			throw new BusinessException("msg","入库数据存入U8失败！");
		}
		
		dbglDto.setQtckglid(String.valueOf(outrdRecordDto.getID()));//其他出库单关联ID
		map.put("dbglDto", dbglDto);
		List<TransVouchsDto> transVouchList=new ArrayList<>();
		List<String> cinvcodes=new ArrayList<>();//存放库存关联id
		List<HwxxDto> hwxxlist_t = new ArrayList<>();
		for (HwxxDto hwxxDto : hwxxs) {
			//set库存关联ID
			cinvcodes.add(hwxxDto.getWlbm());
			HwxxDto hwxx_t = new HwxxDto();
			hwxx_t.setXgry(dbglDto_t.getLrry());
			hwxx_t.setHwid(hwxxDto.getDrhwid());
			hwxx_t.setWlbm(hwxxDto.getWlbm());
			hwxx_t.setZsh(hwxxDto.getZsh());
			hwxx_t.setScph(hwxxDto.getScph());
			hwxxlist_t.add(hwxx_t);
		}
		List<RdRecordsDto> inRdRecordsDtos=new ArrayList<>();//调拨入库
		List<RdRecordsDto> outRdRecordsDtos=new ArrayList<>();//调拨出库
		CurrentStockDto curr = new CurrentStockDto();
		curr.setIds(cinvcodes);
		List<CurrentStockDto> currentStockDtos=currentStockDao.queryBycInvCode(curr);
		if(currentStockDtos==null || currentStockDtos.size()<=0)
			throw new BusinessException("msg","未获取到U8关联库存信息！");
		List<CurrentStockDto> t_addcurrentStockList=new ArrayList<>();//存放需要新增的数据
		List<CurrentStockDto> addcurrentStockList=new ArrayList<>();//新增(复制实体类)
		List<CurrentStockDto> modcurrentStockList=new ArrayList<>();//修改(复制实体类，存放需要修改的数据)
		for(CurrentStockDto currentStockDto : currentStockDtos) {
			try {
				modcurrentStockList.add((CurrentStockDto)currentStockDto.clone());
				addcurrentStockList.add((CurrentStockDto)currentStockDto.clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				throw new BusinessException("CurrentStockDto克隆出错！");
			}
		}
		HwxxDto hwxxDto_t=new HwxxDto();
		hwxxDto_t.setDbid(dbglDto_t.getDbid());
		List<HwxxDto> hwxxDtos_ts=hwxxService.queryByRkid(hwxxDto_t);//查询入库货物信息根据物料和生产批号分组
		//存货位调整
		List<InvPositionDto> invPositionDtos = new ArrayList<>();
		//存货位货物总数
		List<InvPositionSumDto> invPositionSumDtos_mod = new ArrayList<>();
		List<InvPositionSumDto> invPositionSumDtos_add = new ArrayList<>();
		//先处理修改的
		for (HwxxDto hwxxDto : hwxxs) {
			for(CurrentStockDto modcurrentStock : modcurrentStockList) {
				if(StringUtil.isNotBlank(modcurrentStock.getcBatch()) && StringUtil.isNotBlank(hwxxDto.getScph())) {
					if(hwxxDto.getWlbm().equals(modcurrentStock.getcInvCode()) && hwxxDto.getScph().equals(modcurrentStock.getcBatch()) && hwxxDto.getZcckdm().equals(modcurrentStock.getcWhCode())) {
						BigDecimal oldiQuantity=new BigDecimal(modcurrentStock.getiQuantity());//调拨前数量
						BigDecimal subiQuantity=new BigDecimal(hwxxDto.getDbsl());//要调拨的数量
						BigDecimal newiQuantity=oldiQuantity.subtract(subiQuantity);//调拨前数量-要调拨的数量
						if(newiQuantity.compareTo(BigDecimal.ZERO) < 0) {
							throw new BusinessException("msg","U8库存不足，不允许其他出库！");
						}
						newiQuantity=newiQuantity.setScale(2, RoundingMode.HALF_UP);//四舍五入至2位小数
						modcurrentStock.setiQuantity(newiQuantity.toString());
					}
				} 
				if(StringUtil.isBlank(modcurrentStock.getcBatch()) && StringUtil.isBlank(hwxxDto.getScph())) {
					if(hwxxDto.getWlbm().equals(modcurrentStock.getcInvCode()) && hwxxDto.getZcckdm().equals(modcurrentStock.getcWhCode())) {
						BigDecimal oldiQuantity=new BigDecimal(modcurrentStock.getiQuantity());//调拨前数量
						BigDecimal subiQuantity=new BigDecimal(hwxxDto.getDbsl());//要调拨的数量
						BigDecimal newiQuantity=oldiQuantity.subtract(subiQuantity);//调拨前数量-要调拨的数量
						if(newiQuantity.compareTo(BigDecimal.ZERO) < 0) {
							throw new BusinessException("msg","U8库存不足，不允许其他出库！");
						}
						newiQuantity=newiQuantity.setScale(2, RoundingMode.HALF_UP);//四舍五入至2位小数
						modcurrentStock.setiQuantity(newiQuantity.toString());
						modcurrentStock.setcBatch("");
					}
				}				
			}
		}
		//处理拆分的，需要新增的，需要叠加数量的
		if (!CollectionUtils.isEmpty(hwxxDtos_ts)) {
			for(HwxxDto hwxxDtos_t : hwxxDtos_ts) {
				for (HwxxDto hwxxDto : hwxxs) {
					if(StringUtil.isNotBlank(hwxxDtos_t.getScph()) && StringUtil.isNotBlank(hwxxDto.getScph())) {
						if (hwxxDtos_t.getScph().equals(hwxxDto.getScph())
								&& hwxxDtos_t.getWlid().equals(hwxxDto.getWlid())) {
							CurrentStockDto currentStockDto_mod = new CurrentStockDto();
							currentStockDto_mod.setcInvCode(hwxxDto.getWlbm());
							currentStockDto_mod.setcBatch(hwxxDto.getScph());
							currentStockDto_mod.setcWhCode(hwxxDto.getZrckdm());
							CurrentStockDto had_currentStock = currentStockDao.getCurrentStocksByDto(currentStockDto_mod);
							if(had_currentStock!=null) {
								BigDecimal oldiQuantity=new BigDecimal(had_currentStock.getiQuantity());//调拨前数量
								BigDecimal subiQuantity=new BigDecimal(hwxxDtos_t.getU8dbsl());//要调拨的数量
								BigDecimal newiQuantity=oldiQuantity.add(subiQuantity);//调拨前数量-要调拨的数量
								newiQuantity=newiQuantity.setScale(2, RoundingMode.HALF_UP);//四舍五入至2位小数
								had_currentStock.setiQuantity(newiQuantity.toString());
								HwxxDto hwxxDto_mod = new HwxxDto();
								hwxxDto_mod.setHwid(hwxxDto.getDrhwid());
								hwxxDto_mod.setKcglid(String.valueOf(had_currentStock.getAutoID()));
								hwxxlist_t.add(hwxxDto_mod);
								modcurrentStockList.add(had_currentStock);
							}else {
								//新增
								
								for(CurrentStockDto addcurrentStock : addcurrentStockList) {
									if(hwxxDto.getWlbm().equals(addcurrentStock.getcInvCode()) && hwxxDto.getZcckdm().equals(addcurrentStock.getcWhCode()) && hwxxDto.getScph().equals(addcurrentStock.getcBatch())) {
										addcurrentStock.setcWhCode(hwxxDto.getZrckdm());//新的库位
										addcurrentStock.setiQuantity(hwxxDtos_t.getU8dbsl());//调拨数量
										addcurrentStock.setiExpiratDateCalcu("0");
										t_addcurrentStockList.add(addcurrentStock);
									}
								}
							}
							break;
						}
					}
					if(StringUtil.isBlank(hwxxDtos_t.getScph()) && StringUtil.isBlank(hwxxDto.getScph())) {
						if (hwxxDtos_t.getWlid().equals(hwxxDto.getWlid())) {
							CurrentStockDto currentStockDto_mod = new CurrentStockDto();
							currentStockDto_mod.setcInvCode(hwxxDto.getWlbm());
							currentStockDto_mod.setcBatch("");
							currentStockDto_mod.setcWhCode(hwxxDto.getZrckdm());
							CurrentStockDto had_currentStock = currentStockDao.getCurrentStocksByDto(currentStockDto_mod);
							if(had_currentStock!=null) {
								BigDecimal oldiQuantity=new BigDecimal(had_currentStock.getiQuantity());//调拨前数量
								BigDecimal subiQuantity=new BigDecimal(hwxxDtos_t.getU8dbsl());//要调拨的数量
								BigDecimal newiQuantity=oldiQuantity.add(subiQuantity);//调拨前数量-要调拨的数量
								newiQuantity=newiQuantity.setScale(2, RoundingMode.HALF_UP);//四舍五入至2位小数
								had_currentStock.setiQuantity(newiQuantity.toString());
								had_currentStock.setcBatch("");
								HwxxDto hwxxDto_mod = new HwxxDto();
								hwxxDto_mod.setHwid(hwxxDto.getDrhwid());
								hwxxDto_mod.setKcglid(String.valueOf(had_currentStock.getAutoID()));
								hwxxlist_t.add(hwxxDto_mod);
								modcurrentStockList.add(had_currentStock);
							}else {
								for(CurrentStockDto addcurrentStock : addcurrentStockList) {
									if(hwxxDto.getWlbm().equals(addcurrentStock.getcInvCode()) && hwxxDto.getZcckdm().equals(addcurrentStock.getcWhCode()) && StringUtil.isBlank(addcurrentStock.getcBatch())) {
										addcurrentStock.setcWhCode(hwxxDto.getZrckdm());//新的库位
										addcurrentStock.setiQuantity(hwxxDtos_t.getU8dbsl());//调拨数量
										addcurrentStock.setcBatch("");
										addcurrentStock.setiExpiratDateCalcu("0");
										t_addcurrentStockList.add(addcurrentStock);
									}
								}
							}
							break;
						}
					}
				}
			}
		}
		//存入库类型记账数据
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos_in = new ArrayList<>();
		//存出库类型记账数据
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos_out = new ArrayList<>();
		//存要更新的dbmx
		List<DbmxDto> dbmxList = new ArrayList<>();
		for (HwxxDto hwxxDto : hwxxs) {
			DbmxDto dbmxDto_t = new DbmxDto();
			String transvouchsId="";
			//保存调拨单明细
			TransVouchsDto transVouchsDto = new TransVouchsDto();
			transVouchsDto.setcTVCode(transVouchDto.getcTVCode());//调拨单号,这里采取入库单号
			transVouchsDto.setcInvCode(hwxxDto.getWlbm());//物料编码
			transVouchsDto.setiTVQuantity(hwxxDto.getDbsl());//数量
			transVouchsDto.setcTVBatch(StringUtil.isNotBlank(hwxxDto.getScph())?hwxxDto.getScph():null);//批号
			transVouchsDto.setdDisDate(StringUtil.isNotBlank(hwxxDto.getYxq())?hwxxDto.getYxq():null);//失效日期
			transVouchsDto.setCinposcode(hwxxDto.getDrhwdm());//调入库位
			transVouchsDto.setCoutposcode(StringUtil.isNotBlank(hwxxDto.getDchwdm())?hwxxDto.getDchwdm():null);//调出库位
			transVouchsDto.setdMadeDate(StringUtil.isNotBlank(hwxxDto.getScrq())?hwxxDto.getScrq():null);
			if (("0").equals(hwxxDto.getBzqflg())){
				transVouchsDto.setcMassUnit("2");
				transVouchsDto.setiMassDate(hwxxDto.getBzq());//保质期
			}else{
				transVouchsDto.setcMassUnit("1");
				transVouchsDto.setiMassDate("99");//保质期
			}
			//生产日期为空的情况
			if(StringUtil.isBlank(hwxxDto.getScrq())) {
				transVouchsDto.setcMassUnit("0");
				transVouchsDto.setiMassDate(null);//保质期
			}
			if(uA_IdentityDto_t!=null) {
				if(StringUtil.isNotBlank(uA_IdentityDto_t.getiChildId())) {
					int id = Integer.parseInt(uA_IdentityDto_t.getiChildId());
					uA_IdentityDto_t.setiChildId(String.valueOf(id +1));
					transVouchsDto.setAutoID((id + 1000000001));
					transvouchsId=String.valueOf(id + 1000000001);
				}
			}else {
				if(StringUtil.isNotBlank(uA_IdentityDto.getiChildId())) {
					int id = Integer.parseInt(uA_IdentityDto.getiChildId());
					uA_IdentityDto.setiChildId(String.valueOf(id +1));
					transVouchsDto.setAutoID((id + 1000000001));
					transvouchsId=String.valueOf(id + 1000000001);
				}
			}
			transVouchsDto.setID(transVouchDto.getID());//关联调拨单主表ID
			dbmxDto_t.setMxglid(transvouchsId);//调拨单明细主键ID，AutoID
			transVouchList.add(transVouchsDto);
			
			//处理调拨入库明细,R8
			RdRecordsDto in_rdRecordsDto = new RdRecordsDto();
			if(rk_uA_IdentityDto_t!=null) {
				if(StringUtil.isNotBlank(rk_uA_IdentityDto_t.getiChildId())) {
					int id_s = Integer.parseInt(rk_uA_IdentityDto_t.getiChildId());
					rk_uA_IdentityDto_t.setiChildId(String.valueOf(id_s+1)); //存入副表最大值
					in_rdRecordsDto.setAutoID(id_s + 1000000001);
				}
			}else {
				if(StringUtil.isNotBlank(rk_uA_IdentityDto.getiChildId())) {
					int id_s = Integer.parseInt(rk_uA_IdentityDto.getiChildId());
					rk_uA_IdentityDto.setiChildId(String.valueOf(id_s+1)); //存入副表最大值
					in_rdRecordsDto.setAutoID(id_s + 1000000001);
				}
			}
			dbmxDto_t.setQtrkmxglid(String.valueOf(in_rdRecordsDto.getAutoID()));//存放其他入库明细ID
			in_rdRecordsDto.setID(inrdRecordDto.getID()); // 关联id
			in_rdRecordsDto.setcInvCode(hwxxDto.getWlbm()); // 物料编码
			in_rdRecordsDto.setiQuantity(hwxxDto.getDbsl()); // 数量
			in_rdRecordsDto.setiTrIds(transvouchsId);//调拨单明细的AutoID
			in_rdRecordsDto.setcBatch(StringUtil.isNotBlank(hwxxDto.getScph())?hwxxDto.getScph():null); //生产批号
			in_rdRecordsDto.setcItem_class(hwxxDto.getXmdldm()); //项目大类代码
			in_rdRecordsDto.setcItemCode(hwxxDto.getXmbmdm()); //项目编码代码
			in_rdRecordsDto.setcItemCName(hwxxDto.getXmbmmc()); //项目编码名称
			if (("0").equals(hwxxDto.getBzqflg())){
				in_rdRecordsDto.setcMassUnit("2");
				in_rdRecordsDto.setiMassDate(hwxxDto.getBzq()); //有效期数
			}else{
				in_rdRecordsDto.setcMassUnit("1");
				in_rdRecordsDto.setiMassDate("99"); //有效期数
			}
			if(StringUtil.isBlank(hwxxDto.getScrq())) {
				in_rdRecordsDto.setcMassUnit("0");
				in_rdRecordsDto.setiMassDate(null); //有效期数
			}
			in_rdRecordsDto.setdVDate(StringUtil.isNotBlank(hwxxDto.getYxq())?hwxxDto.getYxq():null);//失效日期
			in_rdRecordsDto.setcName(hwxxDto.getXmbmmc());//项目编码名称
			in_rdRecordsDto.setcItemCName(hwxxDto.getXmdlmc());//项目大类名称
			in_rdRecordsDto.setiNQuantity(hwxxDto.getDbsl());// 数量
			in_rdRecordsDto.setdMadeDate(hwxxDto.getScrq());//生产日期
			in_rdRecordsDto.setChVencode(hwxxDto.getGysdm());//供应商代码
			in_rdRecordsDto.setCbsysbarcode("||st08|"+inrdRecordDto.getcCode()+"|");
			in_rdRecordsDto.setcDefine29(hwxxDto.getYchh()); //货号
			in_rdRecordsDto.setcDefine23(hwxxDto.getGysmc());//供应商名称
			in_rdRecordsDto.setcDefine22(StringUtil.isNotBlank(hwxxDto.getZsh())?hwxxDto.getZsh():null);//追溯号
			in_rdRecordsDto.setcPosition(StringUtil.isNotBlank(hwxxDto.getDrhwdm())?hwxxDto.getDrhwdm():null);//库位
			inRdRecordsDtos.add(in_rdRecordsDto);
			//组装记账记录数据
			IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto_in = new IA_ST_UnAccountVouchDto();
			iA_ST_UnAccountVouchDto_in.setIDUN(String.valueOf(in_rdRecordsDto.getID()));
			iA_ST_UnAccountVouchDto_in.setIDSUN(String.valueOf(in_rdRecordsDto.getAutoID()));
			iA_ST_UnAccountVouchDto_in.setcVouTypeUN("08");
			iA_ST_UnAccountVouchDto_in.setcBUstypeUN("调拨入库");
			iA_ST_UnAccountVouchDtos_in.add(iA_ST_UnAccountVouchDto_in);
			//处理调拨出库明细，R9
			RdRecordsDto out_rdRecordsDto = new RdRecordsDto();
			if(rk_uA_IdentityDto_t!=null) {
				if(StringUtil.isNotBlank(rk_uA_IdentityDto_t.getiChildId())) {
					int id_s = Integer.parseInt(rk_uA_IdentityDto_t.getiChildId());
					rk_uA_IdentityDto_t.setiChildId(String.valueOf(id_s+1)); //存入副表最大值
					out_rdRecordsDto.setAutoID(id_s + 1000000001);
				}
			}else {
				if(StringUtil.isNotBlank(rk_uA_IdentityDto.getiChildId())) {
					int id_s = Integer.parseInt(rk_uA_IdentityDto.getiChildId());
					rk_uA_IdentityDto.setiChildId(String.valueOf(id_s+1)); //存入副表最大值
					out_rdRecordsDto.setAutoID(id_s + 1000000001);
				}
			}
			dbmxDto_t.setQtckmxglid(String.valueOf(out_rdRecordsDto.getAutoID()));//存放其他出库明细ID
			dbmxDto_t.setDbmxid(hwxxDto.getDbmxid());
			dbmxDto_t.setXgry(dbglDto_t.getXgry());
			dbmxList.add(dbmxDto_t);
			out_rdRecordsDto.setID(outrdRecordDto.getID()); // 关联id
			out_rdRecordsDto.setcInvCode(hwxxDto.getWlbm()); // 物料编码
			out_rdRecordsDto.setiQuantity(hwxxDto.getDbsl()); // 数量
			out_rdRecordsDto.setcBatch(StringUtil.isNotBlank(hwxxDto.getScph())?hwxxDto.getScph():null); //生产批号
			out_rdRecordsDto.setcItem_class(hwxxDto.getXmdldm()); //项目大类代码
			out_rdRecordsDto.setcItemCode(hwxxDto.getXmbmdm()); //项目编码代码
			out_rdRecordsDto.setcItemCName(hwxxDto.getXmbmmc()); //项目编码名称
			if (("0").equals(hwxxDto.getBzqflg())){
				out_rdRecordsDto.setcMassUnit("2");
				out_rdRecordsDto.setiMassDate(hwxxDto.getBzq()); //有效期数
			}else{
				out_rdRecordsDto.setcMassUnit("1");
				out_rdRecordsDto.setiMassDate("99"); //有效期数
			}
			if(StringUtil.isBlank(hwxxDto.getScrq())) {
				out_rdRecordsDto.setcMassUnit("0");
				out_rdRecordsDto.setiMassDate(null); //有效期数
			}
			out_rdRecordsDto.setdVDate(StringUtil.isNotBlank(hwxxDto.getYxq())?hwxxDto.getYxq():null);//失效日期
			out_rdRecordsDto.setcName(hwxxDto.getXmbmmc());//项目编码名称
			out_rdRecordsDto.setcItemCName(hwxxDto.getXmdlmc());//项目大类名称
			out_rdRecordsDto.setiNQuantity(hwxxDto.getDbsl());// 数量
			out_rdRecordsDto.setdMadeDate(StringUtil.isNotBlank(hwxxDto.getScrq())?hwxxDto.getScrq():null);//生产日期
			out_rdRecordsDto.setChVencode(hwxxDto.getGysdm());//供应商代码
			out_rdRecordsDto.setiTrIds(transvouchsId);//调拨单明细的AutoID
			out_rdRecordsDto.setcPosition(StringUtil.isNotBlank(hwxxDto.getDchwdm())?hwxxDto.getDchwdm():null);//库位
			if(StringUtil.isNotBlank(hwxxDto.getDrhwdm())) {
				out_rdRecordsDto.setIposflag("1");
			}
			out_rdRecordsDto.setCbsysbarcode("||st09|"+outrdRecordDto.getcCode()+"|");
			out_rdRecordsDto.setcDefine29(hwxxDto.getYchh()); //货号
			out_rdRecordsDto.setcDefine23(hwxxDto.getGysmc());//供应商名称
			out_rdRecordsDto.setcDefine22(hwxxDto.getZsh());//追溯号
			outRdRecordsDtos.add(out_rdRecordsDto);
			
			//组装记账记录数据
			IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto_out = new IA_ST_UnAccountVouchDto();
			iA_ST_UnAccountVouchDto_out.setIDUN(String.valueOf(out_rdRecordsDto.getID()));
			iA_ST_UnAccountVouchDto_out.setIDSUN(String.valueOf(out_rdRecordsDto.getAutoID()));
			iA_ST_UnAccountVouchDto_out.setcVouTypeUN("09");
			iA_ST_UnAccountVouchDto_out.setcBUstypeUN("调拨出库");
			iA_ST_UnAccountVouchDtos_out.add(iA_ST_UnAccountVouchDto_out);
			SimpleDateFormat sdf_t = new SimpleDateFormat("yyyy-MM-dd");
			if(StringUtil.isNotBlank(hwxxDto.getDrhwdm())) {
				//处理货位调整表
				InvPositionDto invPositionDto_in = new InvPositionDto();
				invPositionDto_in.setRdsID(String.valueOf(in_rdRecordsDto.getAutoID()));
				invPositionDto_in.setRDID(String.valueOf(inrdRecordDto.getID()));
				invPositionDto_in.setcWhCode(hwxxDto.getZrckdm());
				invPositionDto_in.setcPosCode(hwxxDto.getDrhwdm());
				invPositionDto_in.setcInvCode(hwxxDto.getWlbm());
				invPositionDto_in.setcBatch(StringUtil.isNotBlank(hwxxDto.getScph())?hwxxDto.getScph():"");
				invPositionDto_in.setdVDate(StringUtil.isNotBlank(hwxxDto.getYxq())?hwxxDto.getYxq():null);
				invPositionDto_in.setiQuantity(hwxxDto.getDbsl());
				invPositionDto_in.setcHandler(dbglDto_t.getLrrymc());
				invPositionDto_in.setdDate(sdf_t.format(date));
				invPositionDto_in.setbRdFlag("1");
				invPositionDto_in.setdMadeDate(StringUtil.isNotBlank(hwxxDto.getScrq())?hwxxDto.getScrq():null);
				if (("0").equals(hwxxDto.getBzqflg())) {
					invPositionDto_in.setcMassUnit("2");
					invPositionDto_in.setiMassDate(hwxxDto.getBzq()); // 有效期数
				} else {
					invPositionDto_in.setcMassUnit("1");
					invPositionDto_in.setiMassDate("99"); // 有效期数
				}
				if(StringUtil.isBlank(hwxxDto.getScrq())) {
					invPositionDto_in.setcMassUnit("0");
					invPositionDto_in.setiMassDate(null); // 有效期数
				}
				invPositionDto_in.setCvouchtype("08");
				Date date_t = new Date();
				invPositionDto_in.setdVouchDate(sdf.format(date_t));
				invPositionDtos.add(invPositionDto_in);
			}
			
			if(StringUtil.isNotBlank(hwxxDto.getDchwdm())) {
				InvPositionDto invPositionDto_out = new InvPositionDto();
				invPositionDto_out.setRdsID(String.valueOf(out_rdRecordsDto.getAutoID()));
				invPositionDto_out.setRDID(String.valueOf(outrdRecordDto.getID()));
				invPositionDto_out.setcWhCode(hwxxDto.getZcckdm());
				invPositionDto_out.setcPosCode(hwxxDto.getDchwdm());
				invPositionDto_out.setcInvCode(hwxxDto.getWlbm());
				invPositionDto_out.setcBatch(StringUtil.isNotBlank(hwxxDto.getScph())?hwxxDto.getScph():"");
				invPositionDto_out.setdVDate(hwxxDto.getYxq());
				invPositionDto_out.setiQuantity(hwxxDto.getDbsl());
				invPositionDto_out.setcHandler(dbglDto_t.getLrrymc());
				invPositionDto_out.setdDate(sdf_t.format(date));
				invPositionDto_out.setbRdFlag("0");
				invPositionDto_out.setdMadeDate(hwxxDto.getScrq());
				if (("0").equals(hwxxDto.getBzqflg())) {
					invPositionDto_out.setcMassUnit("2");
					invPositionDto_out.setiMassDate(hwxxDto.getBzq()); // 有效期数
				} else {
					invPositionDto_out.setcMassUnit("1");
					invPositionDto_out.setiMassDate("99"); // 有效期数
				}
				if(StringUtil.isBlank(hwxxDto.getScrq())) {
					invPositionDto_out.setcMassUnit("0");
					invPositionDto_out.setiMassDate(null); // 有效期数
				}
				invPositionDto_out.setCvouchtype("09");
				Date date_t_o = new Date();
				invPositionDto_out.setdVouchDate(sdf.format(date_t_o));
				invPositionDtos.add(invPositionDto_out);
			}
				
		}
		
		map.put("dbmxList",dbmxList);//用于更新其他出入库明细关联ID
		if (!CollectionUtils.isEmpty(hwxxDtos_ts)) {
			for (HwxxDto hwxxDto_ts : hwxxDtos_ts) {
				for (HwxxDto hwxx_s : hwxxs) {
					if (StringUtil.isNotBlank(hwxxDto_ts.getScph()) && StringUtil.isNotBlank(hwxx_s.getScph())) {
						if (hwxxDto_ts.getWlid().equals(hwxx_s.getWlid()) && hwxxDto_ts.getScph().equals(hwxx_s.getScph())) {
							//判断出库库位是否存在
							InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
							invPositionSumDto.setcWhCode(hwxx_s.getZcckdm());
							invPositionSumDto.setcPosCode(hwxx_s.getDchwdm());
							invPositionSumDto.setcInvCode(hwxx_s.getWlbm());
							invPositionSumDto.setcBatch(hwxx_s.getScph());
							if (StringUtil.isNotBlank(hwxx_s.getDchwdm())) {
								//判断是否存在  仓库，物料编码，库位，生产批号一样的数据
								InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
								if (invPositionSumDto_t != null) {
									//更新货物货物数量
									invPositionSumDto_t.setJsbj("1"); //计算标记,相减
									invPositionSumDto_t.setiQuantity(hwxxDto_ts.getU8dbsl());
									invPositionSumDtos_mod.add(invPositionSumDto_t);
								} else {
									throw new BusinessException("msg", "未找到调出库位货物信息！");
								}
							}


							//2.找到入库库位
							if (StringUtil.isNotBlank(hwxx_s.getDrhwdm())) {
								invPositionSumDto.setcWhCode(hwxx_s.getZrckdm());
								invPositionSumDto.setcPosCode(hwxx_s.getDrhwdm());
								//判断是否存在  仓库，物料编码，库位，生产批号一样的数据
								InvPositionSumDto invPositionSumDto_l = invPositionSumDao.getDto(invPositionSumDto);
								if (invPositionSumDto_l != null) {
									//存在更新
									invPositionSumDto_l.setJsbj("0"); //计算标记,相加
									invPositionSumDto_l.setiQuantity(hwxxDto_ts.getU8dbsl());
									invPositionSumDtos_mod.add(invPositionSumDto_l);
								} else {
									//不存在新增
									invPositionSumDto.setiQuantity(hwxxDto_ts.getU8dbsl());
									invPositionSumDto.setInum(null);
									if (("0").equals(hwxx_s.getBzqflg())) {
										invPositionSumDto.setcMassUnit("2");
										invPositionSumDto.setiMassDate(hwxx_s.getBzq()); // 保质期
									} else {
										invPositionSumDto.setcMassUnit("1");
										invPositionSumDto.setiMassDate("99"); // 保质期
									}
									invPositionSumDto.setdMadeDate(hwxx_s.getScrq());
									invPositionSumDto.setdVDate(hwxx_s.getYxq());
									invPositionSumDtos_add.add(invPositionSumDto);
								}
							}
							break;
						}
					}
					if (StringUtil.isBlank(hwxxDto_ts.getScph()) && StringUtil.isBlank(hwxx_s.getScph())) {
						if (hwxxDto_ts.getWlid().equals(hwxx_s.getWlid())) {
							//处理货位货物总数数据
							//1.获取出库信息，更新出库数量
							//判断出库库位是否存在
							InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
							invPositionSumDto.setcWhCode(hwxx_s.getZcckdm());
							invPositionSumDto.setcPosCode(hwxx_s.getDchwdm());
							invPositionSumDto.setcInvCode(hwxx_s.getWlbm());
							invPositionSumDto.setcBatch(StringUtil.isNotBlank(hwxx_s.getScph()) ? hwxx_s.getScph() : "");
							if (StringUtil.isNotBlank(hwxx_s.getDchwdm())) {
								//判断是否存在  仓库，物料编码，库位，生产批号一样的数据
								InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
								if (invPositionSumDto_t != null) {
									//更新货物货物数量
									invPositionSumDto_t.setJsbj("1"); //计算标记,相减
									invPositionSumDto_t.setiQuantity(hwxxDto_ts.getU8dbsl());
									invPositionSumDtos_mod.add(invPositionSumDto_t);
								} else {
									throw new BusinessException("msg", "未找到调出库位货物信息！");
								}
							}


							//2.找到入库库位
							if (StringUtil.isNotBlank(hwxx_s.getDrhwdm())) {
								invPositionSumDto.setcWhCode(hwxx_s.getZrckdm());
								invPositionSumDto.setcPosCode(hwxx_s.getDrhwdm());
								//判断是否存在  仓库，物料编码，库位，生产批号一样的数据
								InvPositionSumDto invPositionSumDto_l = invPositionSumDao.getDto(invPositionSumDto);
								if (invPositionSumDto_l != null) {
									//存在更新
									invPositionSumDto_l.setJsbj("0"); //计算标记,相加
									invPositionSumDto_l.setiQuantity(hwxxDto_ts.getU8dbsl());
									invPositionSumDtos_mod.add(invPositionSumDto_l);
								} else {
									//不存在新增
									invPositionSumDto.setiQuantity(hwxxDto_ts.getU8dbsl());
									invPositionSumDto.setInum(null);
									invPositionSumDto.setcMassUnit("0");
									invPositionSumDto.setiMassDate(null); // 保质期
									invPositionSumDto.setdMadeDate(null);
									invPositionSumDto.setdVDate(null);
									invPositionSumDtos_add.add(invPositionSumDto);
								}
							}
							break;
						}
					}

				}
			}
		}
		// 更新最大值表
		boolean result_ui = false;
		//判断是新增还是修改
		if(rk_uA_IdentityDto_t!=null) {
			//更新
			result_ui = uA_IdentityDao.update(rk_uA_IdentityDto_t)>0;
		}
		if(!result_ui) {
			result_ui = uA_IdentityDao.insert(rk_uA_IdentityDto)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}
		
		//新增U8调拨单明细数据
		//若List过大进行截断,10个明细为一组进行添加
		boolean insertTransVouchs;
		if(transVouchList.size()>10){
			int length=(int)Math.ceil((double)transVouchList.size()/10);//向上取整
			for(int i = 0; i<length; i++){
				List<TransVouchsDto> list=new ArrayList<>();
				int t_length;
				if(i!=length-1){
					t_length=(i+1)*10;
				}else{
					t_length=transVouchList.size();
				}
				for(int j=i*10;j<t_length;j++){
					list.add(transVouchList.get(j));
				}
				insertTransVouchs = transVouchsDao.insertTransVouchs(list);
				if(!insertTransVouchs)
					throw new BusinessException("msg","添加调拨单明细数据失败！");
			}
		}else{
			insertTransVouchs = transVouchsDao.insertTransVouchs(transVouchList);
			if(!insertTransVouchs)
				throw new BusinessException("msg","添加调拨单明细数据失败！");
		}
		
		
		// 更新最大值表
		boolean result_ui_t = false;
		//判断是新增还是修改
		if(uA_IdentityDto_t!=null) {
			//更新
			result_ui_t = uA_IdentityDao.update(uA_IdentityDto_t)>0;
		}
		if(!result_ui_t) {
			result_ui_t = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!result_ui_t) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}

		//修改仓库库位信息和数量
		//若List过大进行截断,10个明细为一组进行添加
		boolean modcurrentStocks;
		if(modcurrentStockList.size()>10){
			int length=(int)Math.ceil((double)modcurrentStockList.size()/10);//向上取整
			for(int i = 0; i<length; i++){
				List<CurrentStockDto> list=new ArrayList<>();
				int t_length;
				if(i!=length-1){
					t_length=(i+1)*10;
				}else{
					t_length=modcurrentStockList.size();
				}
				for(int j=i*10;j<t_length;j++){
					list.add(modcurrentStockList.get(j));
				}
				modcurrentStocks = currentStockDao.updateKwAndSl(list);
				if(!modcurrentStocks)
					throw new BusinessException("msg","更新U8仓库库位和数量信息失败！");
			}
		}else{
			modcurrentStocks = currentStockDao.updateKwAndSl(modcurrentStockList);
			if(!modcurrentStocks)
				throw new BusinessException("msg","更新U8仓库库位和数量信息失败！");
		}
		//添加调拨后的仓库库位信息
		if(!CollectionUtils.isEmpty(t_addcurrentStockList)) {
			//若List过大进行截断,10个明细为一组进行添加
			List<String> addcurrentStocks;
			if(t_addcurrentStockList.size()>10){
				int length=(int)Math.ceil((double)t_addcurrentStockList.size()/10);//向上取整
				for(int i = 0; i<length; i++){
					List<CurrentStockDto> list=new ArrayList<>();
					int t_length;
					if(i!=length-1){
						t_length=(i+1)*10;
					}else{
						t_length=t_addcurrentStockList.size();
					}
					for(int j=i*10;j<t_length;j++){
						list.add(t_addcurrentStockList.get(j));
					}
					addcurrentStocks = currentStockDao.addCurrentStocks(list);
				if(addcurrentStocks==null || addcurrentStocks.size()<=0)
						throw new BusinessException("msg","添加U8调拨库存信息失败！");
					for(int j=0;j<list.size();j++){
						t_addcurrentStockList.get(i*10+j).setAutoID(Integer.parseInt(addcurrentStocks.get(j)));
					}
				}
			}else{
				addcurrentStocks = currentStockDao.addCurrentStocks(t_addcurrentStockList);
			if(addcurrentStocks==null || addcurrentStocks.size()<=0)
					throw new BusinessException("msg","添加U8调拨库存信息失败！");
				for(int i=0;i<t_addcurrentStockList.size();i++){
					t_addcurrentStockList.get(i).setAutoID(Integer.parseInt(addcurrentStocks.get(i)));
				}
			}
		}
		map.put("t_addcurrentStockList", t_addcurrentStockList);
		//添加调拨入库数据
		//若List过大进行截断,10个明细为一组进行添加
		boolean result_in;
		if(inRdRecordsDtos.size()>10){
			int length=(int)Math.ceil((double)inRdRecordsDtos.size()/10);//向上取整
			for(int i = 0; i<length; i++){
				List<RdRecordsDto> list=new ArrayList<>();
				int t_length;
				if(i!=length-1){
					t_length=(i+1)*10;
				}else{
					t_length=inRdRecordsDtos.size();
				}
				for(int j=i*10;j<t_length;j++){
					list.add(inRdRecordsDtos.get(j));
				}
				result_in = rdRecordsDao.insertRd8(list);
				if(!result_in)
					throw new BusinessException("msg","添加U8调拨入库信息失败！");
			}
		}else{
			result_in = rdRecordsDao.insertRd8(inRdRecordsDtos);
			if(!result_in)
				throw new BusinessException("msg","添加U8调拨入库信息失败！");
		}

		//添加调拨出库数据
		//若List过大进行截断,10个明细为一组进行添加
		boolean result_out;
		if(outRdRecordsDtos.size()>10){
			int length=(int)Math.ceil((double)outRdRecordsDtos.size()/10);//向上取整
			for(int i = 0; i<length; i++){
				List<RdRecordsDto> list=new ArrayList<>();
				int t_length;
				if(i!=length-1){
					t_length=(i+1)*10;
				}else{
					t_length=outRdRecordsDtos.size();
				}
				for(int j=i*10;j<t_length;j++){
					list.add(outRdRecordsDtos.get(j));
				}
				result_out = rdRecordsDao.insertRd9(list);
				if(!result_out)
					throw new BusinessException("msg","添加U8调拨出库信息失败！");
			}
		}else{
			result_out = rdRecordsDao.insertRd9(outRdRecordsDtos);
			if(!result_out)
				throw new BusinessException("msg","添加U8调拨出库信息失败！");
		}
		
		//添加U8其他出库相关表数据
		//若List过大进行截断,10个明细为一组进行添加
		int result_sub;
		if(outRdRecordsDtos.size()>10){
			int length=(int)Math.ceil((double)outRdRecordsDtos.size()/10);//向上取整
			for(int i = 0; i<length; i++){
				List<RdRecordsDto> list=new ArrayList<>();
				int t_length;
				if(i!=length-1){
					t_length=(i+1)*10;
				}else{
					t_length=outRdRecordsDtos.size();
				}
				for(int j=i*10;j<t_length;j++){
					list.add(outRdRecordsDtos.get(j));
				}
				result_sub = rdRecordsDao.insertRds09Sub(list);
				if(result_sub<1) {
					throw new BusinessException("msg", "新增rdrecords09sub失败！");
				}
			}
		}else{
			result_sub = rdRecordsDao.insertRds09Sub(outRdRecordsDtos);
			if(result_sub<1) {
				throw new BusinessException("msg", "新增rdrecords09sub失败！");
			}
		}

		//若List过大进行截断,10个明细为一组进行添加
		int result_hw;
		
		if(invPositionDtos.size()>0) {
			if(invPositionDtos.size()>10){
				int length=(int)Math.ceil((double)invPositionDtos.size()/10);//向上取整
				for(int i = 0; i<length; i++){
					List<InvPositionDto> list=new ArrayList<>();
					int t_length;
					if(i!=length-1){
						t_length=(i+1)*10;
					}else{
						t_length=invPositionDtos.size();
					}
					for(int j=i*10;j<t_length;j++){
						list.add(invPositionDtos.get(j));
					}
					result_hw = invPositionDao.insertDtos(list);
					if(result_hw<1) {
						throw new BusinessException("msg", "更新U8货位调整失败！");
					}
				}
			}else{
				result_hw = invPositionDao.insertDtos(invPositionDtos);
				if(result_hw<1) {
					throw new BusinessException("msg", "更新U8货位调整失败！");
				}
			}
		}

		
		if(invPositionSumDtos_add.size()>0) {
			//若List过大进行截断,10个明细为一组进行添加
			int result_add;
			if(invPositionSumDtos_add.size()>10){
				int length=(int)Math.ceil((double)invPositionSumDtos_add.size()/10);//向上取整
				for(int i = 0; i<length; i++){
					List<InvPositionSumDto> list=new ArrayList<>();
					int t_length;
					if(i!=length-1){
						t_length=(i+1)*10;
					}else{
						t_length=invPositionSumDtos_add.size();
					}
					for(int j=i*10;j<t_length;j++){
						list.add(invPositionSumDtos_add.get(j));
					}
					result_add = invPositionSumDao.insertDtos(list);
					if(result_add<1) {
						throw new BusinessException("msg", "新增U8货位总数失败！");
					}
				}
			}else{
				result_add = invPositionSumDao.insertDtos(invPositionSumDtos_add);
				if(result_add<1) {
					throw new BusinessException("msg", "新增U8货位总数失败！");
				}
			}
		}
		
		if(invPositionSumDtos_mod.size()>0) {
			//若List过大进行截断,10个明细为一组进行添加
			int result_mod;
			if(invPositionSumDtos_mod.size()>10){
				int length=(int)Math.ceil((double)invPositionSumDtos_mod.size()/10);//向上取整
				for(int i = 0; i<length; i++){
					List<InvPositionSumDto> list=new ArrayList<>();
					int t_length;
					if(i!=length-1){
						t_length=(i+1)*10;
					}else{
						t_length=invPositionSumDtos_mod.size();
					}
					for(int j=i*10;j<t_length;j++){
						list.add(invPositionSumDtos_mod.get(j));
					}
					result_mod = invPositionSumDao.updateDtos(list);
					if(result_mod<1) {
						throw new BusinessException("msg", "更新U8货位总数失败！");
					}
				}
			}else{
				result_mod = invPositionSumDao.updateDtos(invPositionSumDtos_mod);
				if(result_mod<1) {
					throw new BusinessException("msg", "更新U8货位总数失败！");
				}
			}
		}
		
		//更新记账记录
		//若List过大进行截断,10个明细为一组进行添加
		boolean result_av;
		if(iA_ST_UnAccountVouchDtos_in.size()>10){
			int length=(int)Math.ceil((double)iA_ST_UnAccountVouchDtos_in.size()/10);//向上取整
			for(int i = 0; i<length; i++){
				List<IA_ST_UnAccountVouchDto> list=new ArrayList<>();
				int t_length;
				if(i!=length-1){
					t_length=(i+1)*10;
				}else{
					t_length=iA_ST_UnAccountVouchDtos_in.size();
				}
				for(int j=i*10;j<t_length;j++){
					list.add(iA_ST_UnAccountVouchDtos_in.get(j));
				}
				result_av = addAccountVs(list,"08");
				if(!result_av)
					throw new BusinessException("msg", "记账记录新增失败！");
			}
		}else{
			result_av = addAccountVs(iA_ST_UnAccountVouchDtos_in,"08");
			if(!result_av)
				throw new BusinessException("msg", "记账记录新增失败！");
		}
		//若List过大进行截断,10个明细为一组进行添加
		if(iA_ST_UnAccountVouchDtos_out.size()>10){
			int length=(int)Math.ceil((double)iA_ST_UnAccountVouchDtos_out.size()/10);//向上取整
			for(int i = 0; i<length; i++){
				List<IA_ST_UnAccountVouchDto> list=new ArrayList<>();
				int t_length;
				if(i!=length-1){
					t_length=(i+1)*10;
				}else{
					t_length=iA_ST_UnAccountVouchDtos_out.size();
				}
				for(int j=i*10;j<t_length;j++){
					list.add(iA_ST_UnAccountVouchDtos_out.get(j));
				}
				result_av = addAccountVs(list,"09");
				if(!result_av)
					throw new BusinessException("msg", "记账记录新增失败！");
			}
		}else{
			result_av = addAccountVs(iA_ST_UnAccountVouchDtos_out,"09");
			if(!result_av)
				throw new BusinessException("msg", "记账记录新增失败！");
		}
		map.put("hwxxlist_t", hwxxlist_t);
		
		return map;
	}

	/**
	 * 成品入库审核处理U8数据(OA入库新增)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8CprkDataByRk(RkglDto rkglDto) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		List<HwxxDto> hwxxs = hwxxDao.getDtoListById(rkglDto.getRkid());
		RdRecordDto rdRecordDto = new RdRecordDto();
		if("c".equals(rkglDto.getRklxdm())) {
			VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
			voucherHistoryDto.setCardNumber("0411");
			//获取最大流水号
			VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
			if(voucherHistoryDto_t!=null) {
				String serialnum = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
				String serial = "0000000000"+ serialnum;
				serial = serial.substring(serial.length()-10);
				rdRecordDto.setcCode(serial); // 到货单号
				rdRecordDto.setSflxbj("10");
				//判断单号是否存在
				int ccode_new=0;
				RdRecordDto rdRecordDtos_t = rdRecordDao.queryByCode(rdRecordDto);			
				if(rdRecordDtos_t!=null) {
					List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(rdRecordDto);
					ccode_new = Integer.parseInt(rdRecordDto.getcCode()) + 1;
					for (RdRecordDto rdRecordDto10 : rdList) {
						if(ccode_new-Integer.parseInt(rdRecordDto10.getcCode())==0) {
							ccode_new = ccode_new+1;
						}else {
							break;
						}
					}				
				}
				if(ccode_new!=0) {
		        	rdRecordDto.setcCode(("000000000"+ccode_new).substring(serial.length()-10,serial.length())); // 到货单号
		        	voucherHistoryDto_t.setcNumber(String.valueOf(ccode_new));
		        }else {
		        	voucherHistoryDto_t.setcNumber(serialnum);
		        }    
				//更新最大单号值		
				int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
				if(result_ccode<1)
					throw new BusinessException("msg", "更新U8最大单号失败！");	
			}else {
				voucherHistoryDto.setcNumber("1");
				voucherHistoryDto.setbEmpty("0");
				rdRecordDto.setcCode("0000000001");
				//单号最大表增加数据
				int result_ccode = voucherHistoryDao.insert(voucherHistoryDto);
				if(result_ccode<1)
					throw new BusinessException("msg", "新增U8最大单号失败！");
			}
					

		}
		if("3".equals(rkglDto.getRklxdm())) {
			SimpleDateFormat sdf_d = new SimpleDateFormat("yyyyMM");
			Date date = new Date();
			String prefix = sdf_d.format(date);
			VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
			voucherHistoryDto.setcSeed(prefix);
			voucherHistoryDto.setCardNumber("0301");
			//获取最大流水号 210900053
			VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
			if(voucherHistoryDto_t!=null) {
				String serial = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
				rdRecordDto.setcCode(prefix.substring(2)+"000"+(serial.length()>1?serial:"0"+serial)); // 到货单号
				rdRecordDto.setPrefix(prefix.substring(2));
				rdRecordDto.setSflxbj("8");
				RdRecordDto rdRecordDtos_t = rdRecordDao.queryByCode(rdRecordDto);
				int ccode_new=0;
		        if (rdRecordDtos_t != null ) {
		            List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(rdRecordDto);
		            ccode_new = Integer.parseInt(rdRecordDto.getcCode()) + 1;
		            for (RdRecordDto rdRecordDto_c : rdList) {
						if(ccode_new-Integer.parseInt(rdRecordDto_c.getcCode())==0) {
							ccode_new = ccode_new+1;
						}else {
							break;
						}
					}
		        }
		        if(ccode_new!=0) {
		        	rdRecordDto.setcCode(String.valueOf(ccode_new)); // 到货单号
		        	voucherHistoryDto_t.setcNumber(String.valueOf(ccode_new).substring(4)); //去除多余0
		        }else {
		        	voucherHistoryDto_t.setcNumber(serial);
		        }        
				//更新最大单号值		
				int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
				if(result_ccode<1)
					throw new BusinessException("msg", "更新U8最大单号失败！");
			}else {
				voucherHistoryDto.setiRdFlagSeed("1");
				voucherHistoryDto.setcContent("日期");
				voucherHistoryDto.setcContentRule("月");
				voucherHistoryDto.setbEmpty("0");
				voucherHistoryDto.setcNumber("1");
				rdRecordDto.setcCode(prefix+"00001");
				//单号最大表增加数据
				int result_ccode = voucherHistoryDao.insert(voucherHistoryDto);
				if(result_ccode<1)
					throw new BusinessException("msg", "新增U8最大单号失败！");
			}
		}
		
		
		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); // 存入账套
		uA_IdentityDto.setcVouchType("rd"); // 存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); // 获取主键最大值
		if(uA_IdentityDto_t!=null) {
			if (StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				uA_IdentityDto_t.setiFatherId(String.valueOf(id + 1));
				rdRecordDto.setID(id + 1000000001);
			} 
		}else {
			rdRecordDto.setID(1000000001);
			uA_IdentityDto.setiFatherId("1");
		}
		
		//成品入库新增
		if("c".equals(rkglDto.getRklxdm())) {
			rdRecordDto.setcVouchType("10");	
			rdRecordDto.setbRdFlag("1");
			rdRecordDto.setcBusType("成品入库");
			rdRecordDto.setcSource("库存");
			rdRecordDto.setCsysbarcode("||st10|"+rdRecordDto.getcCode());
		}
		//其他入库新增
		if("3".equals(rkglDto.getRklxdm())) {
			rdRecordDto.setcVouchType("08");		
			rdRecordDto.setbRdFlag("1");
			rdRecordDto.setcBusType("其他入库");
			rdRecordDto.setcSource("库存");
			rdRecordDto.setCsysbarcode("||st08|"+rdRecordDto.getcCode());
		}
		rdRecordDto.setcWhCode(rkglDto.getCkdm()); //仓库代码
		rdRecordDto.setdDate(rkglDto.getRkrq()); //入库日期
		rdRecordDto.setcRdCode(rkglDto.getRklxdm()); //入库类别代码
		rdRecordDto.setcDepCode(rkglDto.getJgdm()); //部门代码
		rdRecordDto.setcHandler(rkglDto.getZsxm()); //审核人
		rdRecordDto.setcMaker(rkglDto.getLrrymc()); //制单人
		rdRecordDto.setcMemo(StringUtil.isNotBlank(rkglDto.getBz())?(rkglDto.getBz().length()>255?rkglDto.getBz().substring(0,255):rkglDto.getBz()):null);//备注
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");// 
		SimpleDateFormat sdf_h = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		rdRecordDto.setdVeriDate(sdf.format(date)); //审核日期
		rdRecordDto.setDnmaketime(sdf_h.format(date)); //制单日期
		rdRecordDto.setDnverifytime(sdf_h.format(date)); //审核日期
		if("c".equals(rkglDto.getRklxdm())) {
			int result = rdRecordDao.insertRd10(rdRecordDto);
			if(result<1) {
				throw new BusinessException("msg","新增U8成品入库信息失败！");
			}
		}
		if("3".equals(rkglDto.getRklxdm())) {
			int result = rdRecordDao.insertQtrk08(rdRecordDto);
			if(result<1) {
				throw new BusinessException("msg","新增U8其他入库信息失败！");
			}
		}
		// 存入关联id
		int rdRecordId = rdRecordDto.getID();
		rkglDto.setGlid(String.valueOf(rdRecordId));
		map.put("rkglDto", rkglDto);		
		// 存货物信息
		List<HwxxDto> hwxxDtoList = new ArrayList<>();
		// 存批量更新U8入库明细数据
		List<RdRecordsDto> rdRecordsDtoList = new ArrayList<>();
		// 存物料编码，用于查库存
		StringBuilder ids = new StringBuilder();
		int irowno = 1;
		int id_s = 0;
		if(uA_IdentityDto_t!=null) {
			id_s = Integer.parseInt(uA_IdentityDto_t.getiChildId());
		}
		//存货位调整信息
		List<InvPositionDto> invPositionDtos = new ArrayList<>();
		//存记账记录数据（成品入库）
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos_10 = new ArrayList<>();
		//存记账记录数据（其他入库）
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos_08 = new ArrayList<>();
		for (HwxxDto hwxxDto : hwxxs) {
			//判断类别是不是ABC类
			ids.append(",").append(hwxxDto.getWlbm());
			RdRecordsDto rdRecordsDto = new RdRecordsDto();
			rdRecordsDto.setAutoID(id_s + 1000000001);
			id_s = id_s + 1;
			rdRecordsDto.setID(rdRecordDto.getID()); // 关联id
			rdRecordsDto.setcInvCode(hwxxDto.getWlbm()); // 物料编码
			rdRecordsDto.setiQuantity(hwxxDto.getSl()); // 数量
			rdRecordsDto.setcBatch(hwxxDto.getScph()); // 生产批号
			rdRecordsDto.setdVDate(hwxxDto.getYxq());// 失效日期
			rdRecordsDto.setcPosition(hwxxDto.getKwbhdm()); // 库位
			rdRecordsDto.setdMadeDate(hwxxDto.getScrq());// 生产日期
			rdRecordsDto.setIrowno(String.valueOf(irowno));
			rdRecordsDto.setCbsysbarcode(rdRecordDto.getCsysbarcode() + "|" + irowno);
			irowno = irowno + 1;
			rdRecordsDto.setCbMemo(StringUtil.isNotBlank(hwxxDto.getDhbz())?(hwxxDto.getDhbz().length()>255?hwxxDto.getDhbz().substring(0,255):hwxxDto.getDhbz()):null); //到货备注
			if (("0").equals(hwxxDto.getBzqflg())) {
				rdRecordsDto.setcMassUnit("2"); // 有效单位
				rdRecordsDto.setiMassDate(hwxxDto.getBzq()); // 有效期数
			} else {
				rdRecordsDto.setcMassUnit("1"); // 有效单位
				rdRecordsDto.setiMassDate("99"); // 有效期数
			}
			if("3".equals(rkglDto.getRklxdm())) {
				rdRecordsDto.setcDefine22(hwxxDto.getZsh()); //追溯号
				rdRecordsDto.setiNQuantity(hwxxDto.getSl());//数量
			}
			rdRecordsDtoList.add(rdRecordsDto);
			
			// 存入关联id
			HwxxDto hwxx_list = new HwxxDto();
			int hwglid = rdRecordsDto.getAutoID();
			hwxx_list.setGlid(String.valueOf(hwglid));
			hwxx_list.setU8rkdh(rdRecordDto.getcCode());
			hwxx_list.setHwid(hwxxDto.getHwid());
			hwxx_list.setXgry(rkglDto.getXgry());
			hwxxDtoList.add(hwxx_list);
			map.put("hwxxDtoList", hwxxDtoList);
			
			IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto = new IA_ST_UnAccountVouchDto();
			iA_ST_UnAccountVouchDto.setIDUN(String.valueOf(rdRecordsDto.getID()));
			iA_ST_UnAccountVouchDto.setIDSUN(String.valueOf(rdRecordsDto.getAutoID()));
			//处理货位调整表
			InvPositionDto invPositionDto = new InvPositionDto();
			invPositionDto.setRdsID(String.valueOf(rdRecordsDto.getAutoID()));
			invPositionDto.setRDID(String.valueOf(rdRecordDto.getID()));
			invPositionDto.setcWhCode(hwxxDto.getCkdm());
			invPositionDto.setcPosCode(hwxxDto.getKwbhdm());
			invPositionDto.setcInvCode(hwxxDto.getWlbm());
			invPositionDto.setcBatch(hwxxDto.getScph());
			invPositionDto.setdVDate(hwxxDto.getYxq());
			invPositionDto.setiQuantity(hwxxDto.getSl());
			invPositionDto.setcHandler(rkglDto.getZsxm());
			invPositionDto.setdDate(rkglDto.getRkrq());
			if (("0").equals(hwxxDto.getBzqflg())) {
				invPositionDto.setcMassUnit("2"); // 有效单位
				invPositionDto.setiMassDate(hwxxDto.getBzq()); // 有效期数
			} else {
				invPositionDto.setcMassUnit("1"); // 有效单位
				invPositionDto.setiMassDate("99"); // 有效期数
			}
			if("c".equals(rkglDto.getRklxdm())) {
				invPositionDto.setbRdFlag("1");
				invPositionDto.setCvouchtype("10");
				iA_ST_UnAccountVouchDto.setcVouTypeUN("10");
				iA_ST_UnAccountVouchDto.setcBUstypeUN("成品入库");
				iA_ST_UnAccountVouchDtos_10.add(iA_ST_UnAccountVouchDto);
			}
			if("3".equals(rkglDto.getRklxdm())) {
				invPositionDto.setbRdFlag("1");
				invPositionDto.setCvouchtype("08");
				iA_ST_UnAccountVouchDto.setcVouTypeUN("08");
				iA_ST_UnAccountVouchDto.setcBUstypeUN("其他入库");
				iA_ST_UnAccountVouchDtos_08.add(iA_ST_UnAccountVouchDto);
			}
			invPositionDto.setdMadeDate(hwxxDto.getScrq());
			SimpleDateFormat sdf_t = new SimpleDateFormat("yyyy-MM-dd");
			Date date_t = new Date();
			invPositionDto.setdVouchDate(sdf_t.format(date_t));
			invPositionDtos.add(invPositionDto);	
		}

		// 更新U8入库明细表
		if("c".equals(rkglDto.getRklxdm())) {
			int result_rds = rdRecordsDao.insertRds10(rdRecordsDtoList);
			if (result_rds < 1)
				throw new BusinessException("msg", "更新U8入库明细表失败！");
		}
		if("3".equals(rkglDto.getRklxdm())) {
			int result_rds = rdRecordsDao.insertRds08(rdRecordsDtoList);
			if (result_rds<1)
				throw new BusinessException("msg", "更新U8入库明细表失败！");
		}
		
		// 更新最大值表
		boolean result_ui = false;
		//判断是新增还是修改
		if(uA_IdentityDto_t!=null) {
			//更新
			uA_IdentityDto_t.setiChildId(String.valueOf(id_s));
			result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
		}
		if(!result_ui) {
			uA_IdentityDto.setiChildId(String.valueOf(id_s));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}

		int result_hw = invPositionDao.insertDtos(invPositionDtos);
		if(result_hw<1) {
			throw new BusinessException("msg", "新增U8货位调整失败！");
		}
		
		//新增记账记录
		if(iA_ST_UnAccountVouchDtos_08.size()>0) {
			boolean result_av = addAccountVs(iA_ST_UnAccountVouchDtos_08,"08");
			if(!result_av)
				throw new BusinessException("msg", "新增其他入库记账记录信息失败！");
		}
		if(iA_ST_UnAccountVouchDtos_10.size()>0) {
			boolean result_av_t = addAccountVs(iA_ST_UnAccountVouchDtos_10,"10");
			if(!result_av_t)
				throw new BusinessException("msg", "新增成品入库记账记录信息失败！");
		}	
		HwxxDto hwxx = new HwxxDto();
		hwxx.setRkid(rkglDto.getRkid());
		// 根据生产批号号物料分组
		List<HwxxDto> hwxxList = hwxxService.queryByRkid(hwxx);
		//根据生产批号查询货物信息
		List<HwxxDto> scphList = hwxxService.queryBycBatch(hwxx);

		//新增U8数据
		boolean result_bp = addBatchPs(scphList);
		if(!result_bp)
			throw new BusinessException("msg", "新增U8数据（AA_BatchProperty）失败！");
		
		// 获取U8已经存在得库存
		ids = new StringBuilder(ids.substring(1));
		CurrentStockDto currentStockDto_t = new CurrentStockDto();
		currentStockDto_t.setIds(ids.toString());
		List<CurrentStockDto> currentStockDtos_t = currentStockDao.queryBycInvCode(currentStockDto_t);
		// 存要更新得货物id,更新库存关联id
		List<HwxxDto> hwxxDto_hs = new ArrayList<>();
		// 存入批量更新得U8库存数据
		List<CurrentStockDto> currentStockDtos = new ArrayList<>();
		//存货位货物总数信息
		List<InvPositionSumDto> invPositionSumDtos_add = new ArrayList<>();
		List<InvPositionSumDto> invPositionSumDtos_mod = new ArrayList<>();
		for (HwxxDto hwxxDto_list : hwxxList) {
			for (HwxxDto hwxx_hwxxs : hwxxs) {
				//判断类别是不是ABC类
					if (hwxxDto_list.getScph().equals(hwxx_hwxxs.getScph())
							&& hwxxDto_list.getWlid().equals(hwxx_hwxxs.getWlid())) {
						// 判断U8是否存在该物料
						if (currentStockDtos_t.size() > 0) {
							boolean modflg = true;
							for (CurrentStockDto current_t : currentStockDtos_t) {
								if (rkglDto.getCkdm().equals(current_t.getcWhCode())
										&& hwxx_hwxxs.getWlbm().equals(current_t.getcInvCode())
										&& hwxx_hwxxs.getScph().equals(current_t.getcBatch())) {
									// 如果仓库，物料编码和生产批号都相同，就修改库存数量
									CurrentStockDto currentStockDto_cu = new CurrentStockDto();
									double sl = Double
											.parseDouble(StringUtils.isNotBlank(hwxxDto_list.getU8rksl())
													? hwxxDto_list.getU8rksl()
													: "0")
											+ Double.parseDouble(StringUtils.isNotBlank(current_t.getiQuantity())
													? current_t.getiQuantity()
													: "0");
									currentStockDto_cu.setiQuantity(Double.toString(sl)); // 库存数量
									currentStockDto_cu.setAutoID(current_t.getAutoID()); // 存入主键id
									currentStockDto_cu.setcWhCode(current_t.getcWhCode()); //仓库代码
									currentStockDto_cu.setcBatch(current_t.getcBatch()); //生产批号
									currentStockDto_cu.setcInvCode(current_t.getcInvCode()); //物料编码
									currentStockDtos.add(currentStockDto_cu);
									
									// 存入关联id
									HwxxDto hwxx_hs = new HwxxDto();
									hwxx_hs.setWlid(hwxx_hwxxs.getWlid());
									hwxx_hs.setRkid(hwxx_hwxxs.getRkid());
									hwxx_hs.setScph(hwxx_hwxxs.getScph());
									hwxx_hs.setKcglid(String.valueOf(currentStockDto_cu.getAutoID())); // 存入关联id
									hwxxDto_hs.add(hwxx_hs);
									modflg = false;
									break;
								}
							}
							if (modflg) {
								// 新增得U8库存数据
								CurrentStockDto current_add = new CurrentStockDto();
								current_add.setcInvCode(hwxx_hwxxs.getWlbm()); // 物料编码
								current_add.setcWhCode(rkglDto.getCkdm());// 仓库编码
								boolean itemidflg = true;
								for (CurrentStockDto current_t : currentStockDtos_t) {
									if (hwxx_hwxxs.getWlbm().equals(current_t.getcInvCode())) {
										// 如果物料编码一样，追溯号不一样，ItemId字段相同
										current_add.setItemId(current_t.getItemId());
										itemidflg = false;
									}
								}

								if (itemidflg) {
									//根据物料编码查itemid
									SCM_ItemDto sCM_ItemDto_t = sCM_ItemDao.getDtoBycInvVode(hwxx_hwxxs.getWlbm());
									if(sCM_ItemDto_t!=null) {
										current_add.setItemId(sCM_ItemDto_t.getId());
									}else {
										// 取SCM_Item表中最大值
										SCM_ItemDto sCM_ItemDto = new SCM_ItemDto();
										int num_ItemId = sCM_ItemDao.getMaxItemId() + 1;
										current_add.setItemId(String.valueOf(num_ItemId));
										// 存入新增的物料
										sCM_ItemDto.setcInvCode(current_add.getcInvCode()); // 物料编码
										int result_scm = sCM_ItemDao.insert(sCM_ItemDto);
										if (result_scm < 1) {
											throw new BusinessException("msg", "sCM_Item新增失败！");
										}
									}									
								}
								current_add.setiQuantity(hwxxDto_list.getU8rksl()); // 现存数量
								current_add.setcBatch(hwxx_hwxxs.getScph()); // 生产批号
								current_add.setdVDate(hwxx_hwxxs.getYxq()); // 失效日期
								current_add.setdMdate(hwxx_hwxxs.getScrq()); // 生产日期
								current_add.setiExpiratDateCalcu("0");
								if (("0").equals(hwxx_hwxxs.getBzqflg())) {
									current_add.setcMassUnit("2"); // 有效单位
									current_add.setiMassDate(hwxx_hwxxs.getBzq()); // 有效期数
								} else {
									current_add.setcMassUnit("1"); // 有效单位
									current_add.setiMassDate("99"); // 有效期数
								}
								int result_add = currentStockDao.insert(current_add);
								if (result_add < 1) {
									throw new BusinessException("msg", "U8库存新增失败！");
								}
								
								// 存入关联id
								HwxxDto hwxx_hs = new HwxxDto();
								hwxx_hs.setWlid(hwxx_hwxxs.getWlid());
								hwxx_hs.setRkid(hwxx_hwxxs.getRkid());
								hwxx_hs.setScph(hwxx_hwxxs.getScph());
								hwxx_hs.setKcglid(String.valueOf(current_add.getAutoID())); // 存入关联id
								hwxxDto_hs.add(hwxx_hs);
							}
						} else {
							// 新增得U8库存数据
							CurrentStockDto current_add_t = new CurrentStockDto();
							current_add_t.setcInvCode(hwxx_hwxxs.getWlbm()); // 物料编码
							current_add_t.setcWhCode(rkglDto.getCkdm());// 仓库编码
							SCM_ItemDto sCM_ItemDto_c  = sCM_ItemDao.getDtoBycInvVode(hwxx_hwxxs.getWlbm());
							if(sCM_ItemDto_c!=null) {
								current_add_t.setItemId(sCM_ItemDto_c.getId());
							}else {
								// 存入新增的物料
								SCM_ItemDto sCM_ItemDto = new SCM_ItemDto();
								sCM_ItemDto.setcInvCode(current_add_t.getcInvCode()); // 物料编码
								int result_scm = sCM_ItemDao.insert(sCM_ItemDto);
								if (result_scm < 1) {
									throw new BusinessException("msg", "sCM_Item新增失败！");
								}
								SCM_ItemDto sCM_ItemDto_t  = sCM_ItemDao.getDtoBycInvVode(hwxx_hwxxs.getWlbm());
								current_add_t.setItemId(sCM_ItemDto_t.getId());
							}		
							current_add_t.setiQuantity(hwxxDto_list.getU8rksl()); // 现存数量
							current_add_t.setcBatch(hwxx_hwxxs.getScph()); // 生产批号
							current_add_t.setdVDate(hwxx_hwxxs.getYxq()); // 失效日期
							current_add_t.setdMdate(hwxx_hwxxs.getScrq()); // 生产日期
							current_add_t.setiExpiratDateCalcu("0");
							if (("0").equals(hwxx_hwxxs.getBzqflg())) {
								current_add_t.setcMassUnit("2"); // 有效单位
								current_add_t.setiMassDate(hwxx_hwxxs.getBzq()); // 有效期数
							} else {
								current_add_t.setcMassUnit("1"); // 有效单位
								current_add_t.setiMassDate("99"); // 有效期数
							}
							int result_add = currentStockDao.insert(current_add_t);
							if (result_add < 1) {
								throw new BusinessException("msg", "U8库存添加失败！");
							}
							
							// 存入关联id
							HwxxDto hwxx_hs = new HwxxDto();
							hwxx_hs.setWlid(hwxx_hwxxs.getWlid());
							hwxx_hs.setRkid(hwxx_hwxxs.getRkid());
							hwxx_hs.setScph(hwxx_hwxxs.getScph());
							hwxx_hs.setKcglid(String.valueOf(current_add_t.getAutoID())); 
							hwxxDto_hs.add(hwxx_hs);
						}
						
						//处理货位货物总数数据
						InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
						invPositionSumDto.setcWhCode(rkglDto.getCkdm());
						invPositionSumDto.setcPosCode(hwxx_hwxxs.getKwbhdm());
						invPositionSumDto.setcInvCode(hwxx_hwxxs.getWlbm());
						invPositionSumDto.setcBatch(hwxx_hwxxs.getScph());
						//判断是否存在  仓库，物料编码，库位，生产批号一样的数据
						InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
						if(invPositionSumDto_t!=null) {
							//存在，更新货物货物数量
							invPositionSumDto_t.setJsbj("0"); //计算标记,相加
							invPositionSumDto_t.setiQuantity(hwxxDto_list.getU8rksl());
							invPositionSumDtos_mod.add(invPositionSumDto_t);
						}else {
							//不存在，做新增
							invPositionSumDto.setiQuantity(hwxxDto_list.getU8rksl());
							invPositionSumDto.setInum("0");					
							if (("0").equals(hwxx_hwxxs.getBzqflg())) {
								invPositionSumDto.setcMassUnit("2");
								invPositionSumDto.setiMassDate(hwxx_hwxxs.getBzq()); // 保质期
							} else {
								invPositionSumDto.setcMassUnit("1");
								invPositionSumDto.setiMassDate("99"); // 保质期
							}
							invPositionSumDto.setdMadeDate(hwxx_hwxxs.getScrq());
							invPositionSumDto.setdVDate(hwxx_hwxxs.getYxq());
							invPositionSumDtos_add.add(invPositionSumDto);
						}
						break;
					}
			}
		}
		
		//更新库存
		if(currentStockDtos.size()>0) {
			boolean reulu_mod = currentStockDao.updateKwAndSl(currentStockDtos);
			if(!reulu_mod) {
				throw new BusinessException("msg","库存数量更新失败！");
			}
		}
		//新增货物总数信息
		if(invPositionSumDtos_add.size()>0) {
			int result_add = invPositionSumDao.insertDtos(invPositionSumDtos_add);
			if(result_add<1) {
				throw new BusinessException("msg", "新增U8货位总数失败！");
			}
		}
		
		//修改货物总数信息
		if(invPositionSumDtos_mod.size()>0) {
			int result_mod = invPositionSumDao.updateDtos(invPositionSumDtos_mod);
			if(result_mod<1) {
				throw new BusinessException("msg", "更新U8货位总数失败！");
			}
		}
		
		//更新库存关联id
		map.put("hwxxDto_hs", hwxxDto_hs);
		return map;	
	}


	/**
	 * 定时查询U8库存信息
	 */
	public void getCurrentStockInfo() {
		List<CurrentStockDto> currentStockDtos = currentStockDao.getDtoAll();
		for (CurrentStockDto currentStockDto:currentStockDtos) {
			if (Double.parseDouble(currentStockDto.getiQuantity()) < 0 ){
				//发送钉钉消息
				JcsjDto jcsjDto = new JcsjDto();
				jcsjDto.setJclb("DINGMESSAGETYPE");
				jcsjDto.setCsdm("KC_TYPE");
				List<DdxxglDto> ddxxList=ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
//				String token =talkUtil.getToken();
				CkxxDto ckxxDto = ckxxService.getDtoByCkdm(currentStockDto.getcWhCode());
				if(ddxxList.size()>0) {
					for (DdxxglDto ddxxglDto : ddxxList) {
						if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
							talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), xxglService.getMsg("ICOMM_DO0001"), xxglService.getMsg("ICOMM_DO0002", currentStockDto.getcInvCode(), ckxxDto.getCkmc(), currentStockDto.getiQuantity(), currentStockDto.getcBatch()));
						}
					}
				}
			}
		}
	}


	
	/**
	 * 出库单同步定时库存
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public void lendOrderSynchronized() throws BusinessException {
		RdRecordDto recordDto = new RdRecordDto();
		Date date = new Date();
		long time = date.getTime() - 6L*24L*3600L*1000L;
		Date beforDate = new Date(time);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String nowTime = simpleDateFormat.format(date);
		String beforTime =  simpleDateFormat.format(beforDate);
		recordDto.setNowTime(nowTime);
		recordDto.setBeforTime(beforTime);
		recordDto.setcSource("借出借用单");
		List<RdRecordDto> recordDtos = rdRecordDao.get09DtoList(recordDto);
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb("DINGMESSAGETYPE");
		jcsjDto.setCsdm("JCTB_TYPE");
		JcsjDto jcsjDto1 = jcsjService.getDtoByCsdmAndJclb(jcsjDto);
		List<DdxxglDto> ddxxList=ddxxglService.selectByDdxxlx(jcsjDto1.getCsdm());
//		String token =talkUtil.getToken();
		if (CollectionUtils.isEmpty(recordDtos)){
			if(ddxxList.size()>0) {
				for (DdxxglDto ddxxglDto : ddxxList) {
					if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
						talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), xxglService.getMsg("ICOMM_JCTB006"), xxglService.getMsg("ICOMM_JCTB007", "否", "0"));
					}
				}
			}
			throw new BusinessException("未同步库存！");
		}
		recordDto.setValue("hwxx");
		List<RdRecordDto> rdRecordDtoList = rdRecordDao.get09DtoList(recordDto);
		if ( null == rdRecordDtoList || rdRecordDtoList.size()==0){
			if(ddxxList.size()>0) {
				for (DdxxglDto ddxxglDto : ddxxList) {
					if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
						talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), xxglService.getMsg("ICOMM_JCTB006"), xxglService.getMsg("ICOMM_JCTB007", "否", "0"));
					}
				}
			}
			throw new BusinessException("未同步库存！");
		}
		boolean success;
		List<KctbcwxxDto> kctbcwxxDtos = new ArrayList<>();
		for (RdRecordDto re:recordDtos) {
			CkhwxxDto ckhwxxDto = new CkhwxxDto();
			ckhwxxDto.setWlbm(re.getcInvCode());
			ckhwxxDto.setCkdm(re.getcWhCode());
			List<CkhwxxDto> ckhwxxDtos = ckhwxxService.getDtoByWlbmAndCkdm(ckhwxxDto);
			Double sl = Double.parseDouble(re.getSumIQuantity());
			boolean result = true;
			if ( null != ckhwxxDtos && ckhwxxDtos.size()>0){
				for (CkhwxxDto ckhwxxDto1:ckhwxxDtos) {
					Double kcl = Double.parseDouble(ckhwxxDto1.getKcl());
					if (sl <= kcl){
						ckhwxxDto1.setKcl(String.valueOf(kcl-sl));
					}else{
						KctbcwxxDto kctbcwxxDto = new KctbcwxxDto();
						kctbcwxxDto.setKctbcwid(StringUtil.generateUUID());
						kctbcwxxDto.setTsxx("库存不足！");
						kctbcwxxDto.setWlbm(ckhwxxDto.getWlbm());
						kctbcwxxDto.setCkdm(ckhwxxDto.getCkdm());
						kctbcwxxDto.setSl(sl.toString());
						kctbcwxxDto.setTblx(jcsjDto1.getCsid());
						kctbcwxxDtos.add(kctbcwxxDto);
						result = false;
//						if(ddxxList.size()>0) {
//							for (int i = 0; i < ddxxList.size();i++){
//								if(ddxxList.get(i).getDdid()!=null &&StringUtil.isNotBlank(ddxxList.get(i).getDdid())) {
//									talkUtil.sendWorkMessage("null",ddxxList.get(i).getDdid(),xxglService.getMsg("ICOMM_JCTB001"),xxglService.getMsg("ICOMM_JCTB003",ckhwxxDto.getWlbm(),ckhwxxDto.getCkdm(),sl.toString(),nowTime));
//								}
//							}
//						}
					}
				}
			}else{
				KctbcwxxDto kctbcwxxDto = new KctbcwxxDto();
				kctbcwxxDto.setKctbcwid(StringUtil.generateUUID());
				kctbcwxxDto.setTsxx("未找到库存！");
				kctbcwxxDto.setWlbm(ckhwxxDto.getWlbm());
				kctbcwxxDto.setCkdm(ckhwxxDto.getCkdm());
				kctbcwxxDto.setSl(sl.toString());
				kctbcwxxDto.setTblx(jcsjDto1.getCsid());
				kctbcwxxDtos.add(kctbcwxxDto);
				result = false;
//				if(ddxxList.size()>0) {
//					for (int i = 0; i < ddxxList.size();i++){
//						if(ddxxList.get(i).getDdid()!=null &&StringUtil.isNotBlank(ddxxList.get(i).getDdid())) {
//							talkUtil.sendWorkMessage("null",ddxxList.get(i).getDdid(),xxglService.getMsg("ICOMM_JCTB001"),xxglService.getMsg("ICOMM_JCTB002",ckhwxxDto.getWlbm(),ckhwxxDto.getCkdm(),sl.toString(),nowTime));
//						}
//					}
//				}
			}
			if (result){
				success = ckhwxxService.updateCkhwxxs(ckhwxxDtos);
				if (!success){
					throw new BusinessException("库存库存量更改失败！");
				}
			}
		}

		for (RdRecordDto re:rdRecordDtoList) {
			HwxxDto hwxxDto = new HwxxDto();
			hwxxDto.setWlbm(re.getcInvCode());
			hwxxDto.setScph(re.getcBatch());
			hwxxDto.setCkdm(re.getcWhCode());
			hwxxDto.setKwbhdm(re.getcPosition());
			List<HwxxDto> hwxxDtos1 = hwxxService.getDtoByWlbmAndCkdmAndKwbhdmAndscph(hwxxDto);
			double sl = Double.parseDouble(re.getSumIQuantity());
			boolean result = true;
			if ( null != hwxxDtos1 && hwxxDtos1.size()>0){
				for (HwxxDto hwInfo:hwxxDtos1) {
					double kcl = Double.parseDouble(hwInfo.getKcl());
					if (sl > kcl){
						sl -= kcl;
						hwInfo.setKcl("0");
					}else{
						kcl -= sl;
						hwInfo.setKcl(Double.toString(kcl));
						sl = 0.00;
					}
				}
				if (sl >0){
					KctbcwxxDto kctbcwxxDto = new KctbcwxxDto();
					kctbcwxxDto.setKctbcwid(StringUtil.generateUUID());
					kctbcwxxDto.setTsxx("货物不足！");
					kctbcwxxDto.setWlbm(hwxxDto.getWlbm());
					kctbcwxxDto.setCkdm(hwxxDto.getCkdm());
					kctbcwxxDto.setKwdm(hwxxDto.getKwbhdm());
					kctbcwxxDto.setScph(hwxxDto.getScph());
					kctbcwxxDto.setSl(Double.toString(sl));
					kctbcwxxDto.setTblx(jcsjDto1.getCsid());
					kctbcwxxDtos.add(kctbcwxxDto);
					result = false;
//					if(ddxxList.size()>0) {
//						for (int i = 0; i < ddxxList.size();i++){
//							if(ddxxList.get(i).getDdid()!=null &&StringUtil.isNotBlank(ddxxList.get(i).getDdid())) {
//								talkUtil.sendWorkMessage("null",ddxxList.get(i).getDdid(),xxglService.getMsg("ICOMM_JCTB001"),xxglService.getMsg("ICOMM_JCTB004",hwxxDto.getWlbm(),hwxxDto.getScph(),hwxxDto.getCkdm(),hwxxDto.getKwbhdm(),sl.toString(),nowTime));
//							}
//						}
//					}
				}
			}else{
				KctbcwxxDto kctbcwxxDto = new KctbcwxxDto();
				kctbcwxxDto.setKctbcwid(StringUtil.generateUUID());
				kctbcwxxDto.setTsxx("未找到货物！");
				kctbcwxxDto.setWlbm(hwxxDto.getWlbm());
				kctbcwxxDto.setCkdm(hwxxDto.getCkdm());
				kctbcwxxDto.setKwdm(hwxxDto.getKwbhdm());
				kctbcwxxDto.setScph(hwxxDto.getScph());
				kctbcwxxDto.setSl(Double.toString(sl));
				kctbcwxxDto.setTblx(jcsjDto1.getCsid());
				kctbcwxxDtos.add(kctbcwxxDto);
				result = false;
//				if(ddxxList.size()>0) {
//					for (int i = 0; i < ddxxList.size();i++){
//						if(ddxxList.get(i).getDdid()!=null &&StringUtil.isNotBlank(ddxxList.get(i).getDdid())) {
//							talkUtil.sendWorkMessage("null",ddxxList.get(i).getDdid(),xxglService.getMsg("ICOMM_JCTB001"),xxglService.getMsg("ICOMM_JCTB005",hwxxDto.getWlbm(),hwxxDto.getScph(),hwxxDto.getCkdm(),hwxxDto.getKwbhdm(),sl.toString(),nowTime));
//						}
//					}
//				}
			}
			if (result){
				success = hwxxService.updateHwxxDtos(hwxxDtos1);
				if (!success){
					throw new BusinessException("货物库存量更改失败！");
				}
			}
		}
		if (!CollectionUtils.isEmpty(kctbcwxxDtos)){
			success = kctbcwxxService.insertKctbcwxxList(kctbcwxxDtos);
			if (!success){
				throw new BusinessException("库存同步错误信息更新失败！");
			}
			if(ddxxList.size()>0) {
				for (DdxxglDto ddxxglDto : ddxxList) {
					if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
						talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), xxglService.getMsg("ICOMM_JCTB006"), xxglService.getMsg("ICOMM_JCTB007", "是", String.valueOf(kctbcwxxDtos.size())));
					}
				}
			}
		}else{
			if(ddxxList.size()>0) {
				for (DdxxglDto ddxxglDto : ddxxList) {
					if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
						talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), xxglService.getMsg("ICOMM_JCTB006"), xxglService.getMsg("ICOMM_JCTB007", "是", "0"));
					}
				}
			}
		}
	}


	/**
	 * 出库单同步定时库存
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public void repaidOrderSynchronized() throws BusinessException {
		RdRecordDto recordDto = new RdRecordDto();
		Date date = new Date();
		long time = date.getTime() - 6L*24L*3600L*1000L;
		Date beforDate = new Date(time);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String nowTime = simpleDateFormat.format(date);
		String beforTime =  simpleDateFormat.format(beforDate);
		recordDto.setNowTime(nowTime);
		recordDto.setBeforTime(beforTime);
		recordDto.setcSource("借出归还单");
		List<RdRecordDto> recordDtos = rdRecordDao.get08DtoList(recordDto);
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb("DINGMESSAGETYPE");
		jcsjDto.setCsdm("GHTB_TYPE");
		JcsjDto jcsjDto1 = jcsjService.getDtoByCsdmAndJclb(jcsjDto);
		List<DdxxglDto> ddxxList=ddxxglService.selectByDdxxlx(jcsjDto1.getCsdm());
//		String token =talkUtil.getToken();
		if (CollectionUtils.isEmpty(recordDtos)){
			if(ddxxList.size()>0) {
				for (DdxxglDto ddxxglDto : ddxxList) {
					if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
						talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), xxglService.getMsg("ICOMM_GHTB001"), xxglService.getMsg("ICOMM_JCTB007", "否", "0"));
					}
				}
			}
			throw new BusinessException("未同步库存！");
		}
		recordDto.setValue("hwxx");
		List<RdRecordDto> rdRecordDtoList = rdRecordDao.get08DtoList(recordDto);
		if ( null == rdRecordDtoList || rdRecordDtoList.size()==0){
			if(ddxxList.size()>0) {
				for (DdxxglDto ddxxglDto : ddxxList) {
					if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
						talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), xxglService.getMsg("ICOMM_GHTB001"), xxglService.getMsg("ICOMM_JCTB007", "否", "0"));
					}
				}
			}
			throw new BusinessException("未同步库存！");
		}
		boolean success;
		List<KctbcwxxDto> kctbcwxxDtos = new ArrayList<>();
		for (RdRecordDto re:recordDtos) {
			CkhwxxDto ckhwxxDto = new CkhwxxDto();
			ckhwxxDto.setWlbm(re.getcInvCode());
			ckhwxxDto.setCkdm(re.getcWhCode());
			List<CkhwxxDto> ckhwxxDtos = ckhwxxService.getDtoByWlbmAndCkdm(ckhwxxDto);
			double sl = Double.parseDouble(re.getSumIQuantity());
			boolean result = true;
			if ( null != ckhwxxDtos && ckhwxxDtos.size()>0){
				for (CkhwxxDto ckhwxxDto1:ckhwxxDtos) {
					double kcl = Double.parseDouble(ckhwxxDto1.getKcl());
					if (sl > 0 ){
						ckhwxxDto1.setKcl(String.valueOf(kcl + sl));
					}else{
						KctbcwxxDto kctbcwxxDto = new KctbcwxxDto();
						kctbcwxxDto.setKctbcwid(StringUtil.generateUUID());
						kctbcwxxDto.setTsxx("归还数有误！");
						kctbcwxxDto.setWlbm(ckhwxxDto.getWlbm());
						kctbcwxxDto.setCkdm(ckhwxxDto.getCkdm());
						kctbcwxxDto.setSl(Double.toString(sl));
						kctbcwxxDto.setTblx(jcsjDto1.getCsid());
						kctbcwxxDtos.add(kctbcwxxDto);
						result = false;
					}
				}
			}else{
				KctbcwxxDto kctbcwxxDto = new KctbcwxxDto();
				kctbcwxxDto.setKctbcwid(StringUtil.generateUUID());
				kctbcwxxDto.setTsxx("未找到库存！");
				kctbcwxxDto.setWlbm(ckhwxxDto.getWlbm());
				kctbcwxxDto.setCkdm(ckhwxxDto.getCkdm());
				kctbcwxxDto.setSl(Double.toString(sl));
				kctbcwxxDto.setTblx(jcsjDto1.getCsid());
				kctbcwxxDtos.add(kctbcwxxDto);
				result = false;
			}
			if (result){
				success = ckhwxxService.updateCkhwxxs(ckhwxxDtos);
				if (!success){
					throw new BusinessException("库存库存量更改失败！");
				}
			}
		}

		for (RdRecordDto re:rdRecordDtoList) {
			boolean result = true;
			double sl = Double.parseDouble(re.getSumIQuantity());
			HwxxDto hwxxDto = new HwxxDto();
			hwxxDto.setWlbm(re.getcInvCode());
			WlglDto wlglDto = wlglService.getWlidByWlbm(re.getcInvCode());
			hwxxDto.setWlid(wlglDto.getWlid());
			hwxxDto.setScph(re.getcBatch());
			hwxxDto.setHwid(StringUtil.generateUUID());
			hwxxDto.setZt("99");
			hwxxDto.setKcl(re.getSumIQuantity());
			hwxxDto.setSl(re.getSumIQuantity());
			CkxxDto ck = ckxxService.getDtoByCkdm(re.getcWhCode());
			//CkxxDto kw = ckxxService.getDtoByCkdm(re.getcPosition());
			hwxxDto.setCkid(ck.getCkid());
			hwxxDto.setKwbh(ck.getCkid());
			if (sl < 0 ){
				KctbcwxxDto kctbcwxxDto = new KctbcwxxDto();
				kctbcwxxDto.setKctbcwid(StringUtil.generateUUID());
				kctbcwxxDto.setTsxx("归还数有误！");
				kctbcwxxDto.setWlbm(hwxxDto.getWlbm());
				kctbcwxxDto.setCkdm(re.getcWhCode());
				kctbcwxxDto.setSl(Double.toString(sl));
				kctbcwxxDto.setTblx(jcsjDto1.getCsid());
				kctbcwxxDtos.add(kctbcwxxDto);
				result = false;
			}

			CurrentStockDto currentStockDto = new CurrentStockDto();
			currentStockDto.setcInvCode(hwxxDto.getWlbm());
			currentStockDto.setcBatch(hwxxDto.getScph());
			currentStockDto.setcWhCode(re.getcWhCode());
			CurrentStockDto currentStockDto1 = currentStockDao.getCurrentStocksByDto(currentStockDto);
			if (null != currentStockDto1){
				hwxxDto.setKcglid(String.valueOf(currentStockDto1.getAutoID()));
				hwxxDto.setZsh(currentStockDto1.getcBatch());
				hwxxDto.setScrq(currentStockDto1.getdMdate());
				hwxxDto.setYxq(currentStockDto1.getdVDate());
			}else{
				KctbcwxxDto kctbcwxxDto = new KctbcwxxDto();
				kctbcwxxDto.setKctbcwid(StringUtil.generateUUID());
				kctbcwxxDto.setTsxx("未找到U8库存！");
				kctbcwxxDto.setWlbm(hwxxDto.getWlbm());
				kctbcwxxDto.setCkdm(re.getcWhCode());
				kctbcwxxDto.setScph(hwxxDto.getScph());
				kctbcwxxDto.setTblx(jcsjDto1.getCsid());
				kctbcwxxDtos.add(kctbcwxxDto);
				result = false;
			}
			if (result){
				//hw新增
				success = hwxxService.insertDto(hwxxDto);
				if (!success){
					KctbcwxxDto kctbcwxxDto = new KctbcwxxDto();
					kctbcwxxDto.setKctbcwid(StringUtil.generateUUID());
					kctbcwxxDto.setTsxx("货物新增失败！");
					kctbcwxxDto.setWlbm(hwxxDto.getWlbm());
					kctbcwxxDto.setCkdm(re.getcWhCode());
					kctbcwxxDto.setScph(hwxxDto.getScph());
					kctbcwxxDto.setTblx(jcsjDto1.getCsid());
					kctbcwxxDtos.add(kctbcwxxDto);
				}
			}
		}
		if (!CollectionUtils.isEmpty(kctbcwxxDtos)){
			success = kctbcwxxService.insertKctbcwxxList(kctbcwxxDtos);
			if (!success){
				throw new BusinessException("库存同步错误信息更新失败！");
			}
			if(ddxxList.size()>0) {
				for (DdxxglDto ddxxglDto : ddxxList) {
					if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
						talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), xxglService.getMsg("ICOMM_GHTB001"), xxglService.getMsg("ICOMM_JCTB007", "是", String.valueOf(kctbcwxxDtos.size())));
					}
				}
			}
		}else{
			if(ddxxList.size()>0) {
				for (DdxxglDto ddxxglDto : ddxxList) {
					if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
						talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), xxglService.getMsg("ICOMM_GHTB001"), xxglService.getMsg("ICOMM_JCTB007", "是", "0"));
					}
				}
			}
		}
	}

	/**
	 * 退货u8操作
	 */

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public Map<String, Object> thsave(FhglDto fhglDto,List<FhmxDto> fhmxDtosLater,List<FhmxDto> fhmxDtosBefor) throws BusinessException {
		Map<String,Object> map=new HashMap<>();

		//生成主表单号
		DispatchListDto dispatchListDto = new DispatchListDto();
		//生成单号
		VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
		voucherHistoryDto.setCardNumber("01");
		dispatchListDto.setcDLCode(fhglDto.getFhdh()); //存入发货单号
		//判断单号是否存在
		DispatchListDto dispatchListDto_t = dispatchListDao.getDtoByDh(dispatchListDto); //按单号排序查询
		if(dispatchListDto_t!=null) {
			throw new BusinessException("msg", "此单号已存在，请刷新！");
		}
		VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
		voucherHistoryDto_t.setcNumber(fhglDto.getFhdh());  //移除单号前的 0，存入流水号
		//更新最大单号值
		int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
		if(result_ccode<1)
			throw new BusinessException("msg", "更新U8最大单号失败！");

		//生成主键id
		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); // 存入账套
		uA_IdentityDto.setcVouchType("DISPATCH"); // 存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); // 获取主键最大值
		if (StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
			int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
			uA_IdentityDto_t.setiFatherId(String.valueOf(id + 1));
			dispatchListDto.setDLID(String.valueOf(id + 1000000001)); //存入主键id
		} else {
			throw new BusinessException("msg", "生成U8主表主键id失败！");
		}

		fhglDto.setGlid(dispatchListDto.getDLID()); //存入glid

		dispatchListDto.setcVouchType("05"); //业务类型
		dispatchListDto.setcSTCode(fhglDto.getXslxdm()); //销售类型
		dispatchListDto.setdDate(fhglDto.getDjrq()); //发货日期
		dispatchListDto.setcDepCode(fhglDto.getXsbmdm()); //部门代码
		dispatchListDto.setcCusCode(fhglDto.getKhdm()); //客户代码
		dispatchListDto.setcShipAddress(fhglDto.getShdz()); //收获地址
		dispatchListDto.setcMemo(fhglDto.getBz()); //备注
		dispatchListDto.setcVerifier(fhglDto.getZsxm()); //审核人
		dispatchListDto.setcMaker(fhglDto.getZsxm()); //制单人
		dispatchListDto.setcCusName(fhglDto.getKhmc()); //客户名称
		dispatchListDto.setcBusType("普通销售"); //业务类型名称
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		Date date = new Date();
		dispatchListDto.setDverifydate(sdf.format(date)); //审核时间
		dispatchListDto.setDcreatesystime(sdf.format(date)); //创建时间
		dispatchListDto.setDverifysystime(sdf.format(date)); //审核时间
		dispatchListDto.setcSysBarCode("||SA01"+"|"+dispatchListDto.getcDLCode()); //||SA01|+单号
		dispatchListDto.setCinvoicecompany(fhglDto.getKhdm());
		dispatchListDto.setiTaxRate(fhglDto.getSuil()); //税率
		dispatchListDto.setiExchRate(fhglDto.getHl()); //汇率

		// TODO
		dispatchListDto.setCexch_name(StringUtil.isNotBlank(fhglDto.getBiz())?fhglDto.getBiz():"人民币"); //币种
		dispatchListDto.setiVTid("71"); //模板
		dispatchListDto.setbReturnFlag("0");
		dispatchListDto.setBneedbill("1");
		int result = dispatchListDao.insert(dispatchListDto);
		if(result<1) {
			throw new BusinessException("msg","新增发货单主表失败！");
		}
		return map;
	}


	/**
	 * 销售发货
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8DataByDeliver(FhglDto fhglDto) throws BusinessException {
		Map<String,Object> map=new HashMap<>();
		//获取发货表信息
		FhglDto fhglDto_t = fhglDao.getDtoByid(fhglDto);
		
		//生成主表单号
		DispatchListDto dispatchListDto = new DispatchListDto();
		//生成单号
		VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
		voucherHistoryDto.setCardNumber("01");

		VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);

		if(voucherHistoryDto_t!=null) {
			String serialnum = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
			String serial = "0000000000"+ serialnum;
			serial = serial.substring(serial.length()-10);
			//判断单号是否存在
			int ccode_new=0;
			dispatchListDto.setcDLCode(serial);
			DispatchListDto dispatchListDto_t = dispatchListDao.getDtoByDh(dispatchListDto);
			if(dispatchListDto_t!=null) {
				List<DispatchListDto> dlList = dispatchListDao.getcCode(dispatchListDto_t);
				ccode_new = Integer.parseInt(dispatchListDto_t.getcDLCode()) + 1;
				for (DispatchListDto dispatchListDto_c : dlList) {
					if(ccode_new-Integer.parseInt(dispatchListDto_c.getcDLCode())==0) {
						ccode_new = ccode_new+1;
					}else {
						break;
					}
				}
			}
			if(ccode_new!=0) {
				String ccode_newStr = "0000000000"+ccode_new;
				ccode_newStr = ccode_newStr.substring(ccode_newStr.length()-10);
				dispatchListDto.setcDLCode(ccode_newStr); //存入退货单号
				voucherHistoryDto_t.setcNumber(ccode_newStr); //去除多余0
			}else {
				voucherHistoryDto_t.setcNumber(serial); //去除多余0
			}
			//更新最大单号值
			int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
			if(result_ccode<1)
				throw new BusinessException("msg", "更新U8最大单号失败！");
		}else {
			voucherHistoryDto.setcNumber("1");
			voucherHistoryDto.setbEmpty("0");
			dispatchListDto.setcDLCode("0000000001");
			voucherHistoryDao.insert(voucherHistoryDto);
		}
		
		//生成主键id
		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); // 存入账套
		uA_IdentityDto.setcVouchType("DISPATCH"); // 存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); // 获取主键最大值
		if(uA_IdentityDto_t!=null) {
			if (StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				uA_IdentityDto_t.setiFatherId(String.valueOf(id + 1));
				dispatchListDto.setDLID(String.valueOf(id + 1000000001)); //存入主键id
			}
		}else {
			uA_IdentityDto.setiFatherId("1");
			dispatchListDto.setDLID("1000000001");
			uA_IdentityDto.setiChildId("0");
		}

		fhglDto_t.setGlid(dispatchListDto.getDLID()); //存入glid		
		dispatchListDto.setcVouchType("05"); //业务类型
		dispatchListDto.setcSTCode(fhglDto_t.getXslxdm()); //销售类型
		dispatchListDto.setdDate(fhglDto_t.getDjrq()); //发货日期
		dispatchListDto.setcDepCode(fhglDto_t.getXsbmdm()); //部门代码
		dispatchListDto.setcCusCode(fhglDto_t.getKhdm()); //客户代码
		dispatchListDto.setcShipAddress(fhglDto_t.getShdz()); //收获地址
		dispatchListDto.setcMemo(fhglDto_t.getBz()); //备注
		dispatchListDto.setcVerifier(fhglDto.getZsxm()); //审核人
		dispatchListDto.setcMaker(fhglDto.getZsxm()); //制单人
		dispatchListDto.setcCusName(fhglDto_t.getKhmc()); //客户名称
		dispatchListDto.setcBusType("普通销售"); //业务类型名称
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat sdfr = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		dispatchListDto.setDverifydate(sdfr.format(date)); //审核时间
		dispatchListDto.setDcreatesystime(sdf.format(date)); //创建时间
		dispatchListDto.setDverifysystime(sdf.format(date)); //审核时间
		dispatchListDto.setcSysBarCode("||SA01"+"|"+dispatchListDto.getcDLCode()); //||SA01|+单号
		dispatchListDto.setCinvoicecompany(fhglDto_t.getKhdm());
		dispatchListDto.setiTaxRate(fhglDto_t.getSuil()); //税率
		dispatchListDto.setiExchRate("1"); //汇率
		dispatchListDto.setCexch_name(StringUtil.isNotBlank(fhglDto_t.getBiz())?fhglDto_t.getBiz():"人民币"); //币种
		dispatchListDto.setiVTid("71"); //模板
		dispatchListDto.setiNetLock(null);
		dispatchListDto.setbReturnFlag("0");
		dispatchListDto.setBneedbill("1");
		CkglDto ckglDto = new CkglDto();
		ckglDto.setIds(fhglDto.getCkid());
		//新增销售出库单数据
		List<CkglDto> ckglDtos = ckglDao.getDtoByCkids(ckglDto);
		if(CollectionUtils.isEmpty(ckglDtos))
			throw new BusinessException("msg", "未找到出库信息！");
		
		//存更新的库存信息
		List<CurrentStockDto> currentStockList = new ArrayList<>();
		//存货位调整
		List<InvPositionDto> invPositionDtos = new ArrayList<>();
		//存货位总数信息
		List<InvPositionSumDto> invPositionSumDtos_mod = new ArrayList<>();
		//存记账记录数据
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos = new ArrayList<>();
		//存U8发货明细数据
		List<DispatchListsDto> dispatchListsDtos = new ArrayList<>();
		//存U8出库明细数据
		List<RdRecordsDto> rdRecordsDtos = new ArrayList<>();
		//存U8销售订单数据
		List<SO_SOMainDto> sO_SOMainDtos = new ArrayList<>();
		List<SO_SODetailsDto> sO_SODetailsDtos = new ArrayList<>();
		//存销售出库单信息
		List<RdRecordDto> rdRecordDtos = new ArrayList<>();
		//存发货明细List
		List<FhmxDto> fhmxList = new ArrayList<>();
		
		//发货流水号
		int serial_dl = 1000000001;
		int lsh = 0;
		//获取发货明细，出库明细
		FhmxDto fhmxDto = new FhmxDto();
		fhmxDto.setFhid(fhglDto_t.getFhid());
		List<FhmxDto> fhmxDtos = fhmxService.getDtoAllByFhid(fhmxDto);
		if(StringUtil.isBlank(dispatchListDto.getcSOCode())) {
			dispatchListDto.setcSOCode(fhmxDtos.get(0).getU8xsdh()); //订单号，如果存在多个，拿第一个
		}

		//存出库单得单号
		StringBuilder ckdh = new StringBuilder();
		for (CkglDto ckglDto_t : ckglDtos) {
			RdRecordDto rdRecordDto=new RdRecordDto();
			SimpleDateFormat sdf_d = new SimpleDateFormat("yyyyMMdd");
			String prefix_in = sdf_d.format(date);
			VoucherHistoryDto inVoucherHistoryDto = new VoucherHistoryDto();
			inVoucherHistoryDto.setcSeed(prefix_in);
			inVoucherHistoryDto.setCardNumber("0303");
			//获取最大流水号 CK2021102504
			VoucherHistoryDto inVoucherHistoryDto_t = voucherHistoryDao.getMaxSerial(inVoucherHistoryDto);
			if(inVoucherHistoryDto_t!=null) {
				String serial_in = String.valueOf(Integer.parseInt(inVoucherHistoryDto_t.getcNumber())+1);
				rdRecordDto.setcCode("CK"+prefix_in+(serial_in.length()>1?serial_in:"0"+serial_in)); // 销售出库单号
				rdRecordDto.setPrefix("CK"+prefix_in);
				rdRecordDto.setSflxbj("32");
				RdRecordDto rdRecordDtos_t = rdRecordDao.queryByCode(rdRecordDto);
				int ccode_new_in=0;
		        if (rdRecordDtos_t != null ) {
		            List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(rdRecordDto);
		            ccode_new_in = Integer.parseInt(rdRecordDto.getcCode().substring(2)) + 1;
		            for (RdRecordDto rdRecordDto_c_in : rdList) {
						if(ccode_new_in-Integer.parseInt(rdRecordDto_c_in.getcCode().substring(2))==0) {
							ccode_new_in = ccode_new_in+1;
						}else {
							break;
						}
					}
		        }
		        if(ccode_new_in!=0) {
		        	rdRecordDto.setcCode("CK"+ccode_new_in); //出库单号
		        	inVoucherHistoryDto_t.setcNumber(String.valueOf(ccode_new_in).substring(2)); //去除多余0
		        }else {
		        	inVoucherHistoryDto_t.setcNumber(serial_in);
		        }        
				//更新最大单号值		
				int result_ccode_in = voucherHistoryDao.update(inVoucherHistoryDto_t);
				if(result_ccode_in<1)
					throw new BusinessException("msg", "更新U8最大单号失败！");
			}else {
				inVoucherHistoryDto.setcContent("日期");
				inVoucherHistoryDto.setcContentRule("日");
				inVoucherHistoryDto.setbEmpty("0");
				inVoucherHistoryDto.setcNumber("1");
				rdRecordDto.setcCode("CK"+prefix_in+"01");
				//单号最大表增加数据
				int result_ccode_in = voucherHistoryDao.insert(inVoucherHistoryDto);
				if(result_ccode_in<1)
					throw new BusinessException("msg", "新增U8最大单号失败！");
			}
			ckdh.append(",").append(rdRecordDto.getcCode());
			
			//生成主键id
			UA_IdentityDto uA_IdentityDto_rd = new UA_IdentityDto();
			uA_IdentityDto_rd.setcAcc_Id(accountName); // 存入账套
			uA_IdentityDto_rd.setcVouchType("rd"); // 存入表标识
			UA_IdentityDto uA_IdentityDto_t_rd = uA_IdentityDao.getMax(uA_IdentityDto_rd); // 获取主键最大值
			if(uA_IdentityDto_t_rd!=null) {
				if (StringUtil.isNotBlank(uA_IdentityDto_t_rd.getiFatherId())) {
					int id = Integer.parseInt(uA_IdentityDto_t_rd.getiFatherId());
					uA_IdentityDto_t_rd.setiFatherId(String.valueOf(id + 1));
					rdRecordDto.setID(id + 1000000001); //存入主键id
				}
			}else {
				uA_IdentityDto_rd.setiFatherId("1");
				uA_IdentityDto_rd.setiChildId("0");
				rdRecordDto.setID(1000000001); //存入主键id
			}
			
			ckglDto_t.setGlid(String.valueOf(rdRecordDto.getID()));
			ckglDto_t.setU8ckdh(rdRecordDto.getcCode());
			rdRecordDto.setbRdFlag("0");
			rdRecordDto.setcVouchType("32");
			rdRecordDto.setcRdCode("7");
			if("/matridxigams".equals(urlPrefix)){
				rdRecordDto.setcRdCode("d"); //生物的为销售出库，其他账套为其他出库
			}
			rdRecordDto.setVT_ID("87"); //模板号
			rdRecordDto.setdDate(sdfr.format(date)); //出库日期
			rdRecordDto.setcDepCode(ckglDto_t.getJgdm()); //部门代码
			rdRecordDto.setcMaker(ckglDto_t.getZsxm()); //制单人
			rdRecordDto.setCsysbarcode("||st32|"+rdRecordDto.getcCode());
			rdRecordDto.setcMemo(StringUtil.isNotBlank(ckglDto_t.getBz())?(ckglDto_t.getBz().length()>255?ckglDto_t.getBz().substring(0,255):ckglDto_t.getBz()):null);//备注
			rdRecordDto.setcBusType("普通销售");
			rdRecordDto.setcSource("发货单");
			rdRecordDto.setbOMFirst("0");
			rdRecordDto.setcBusCode(dispatchListDto.getcDLCode()); //发货单号
			rdRecordDto.setcWhCode(ckglDto_t.getCkdm()); //仓库代码
			rdRecordDto.setcStCode(dispatchListDto.getcSTCode()); //销售类型
			rdRecordDto.setcCUsCode(dispatchListDto.getcCusCode()); //客户代码
			rdRecordDto.setcDLCode(dispatchListDto.getDLID()); //发货ID
			rdRecordDto.setcShipAddress(dispatchListDto.getcShipAddress()); //收货地址
			rdRecordDto.setCinvoicecompany(dispatchListDto.getcCusCode()); //客户代码
			rdRecordDto.setDnmaketime(sdf.format(date)); //制单时间
			rdRecordDto.setcPersonCode(fhglDto_t.getGrouping()); //业务员
			rdRecordDtos.add(rdRecordDto);
			
			//流水号
			int serial = 1000000001;

			
			//处理销售出库明细数据
			for (FhmxDto fhmxDto_t : fhmxDtos) {
				if(fhmxDto_t.getCkid().equals(ckglDto_t.getCkid())) {
					lsh++;
					CurrentStockDto currentStockDto2 = new CurrentStockDto();
					currentStockDto2.setcInvCode(fhmxDto_t.getWlbm());
					currentStockDto2.setcBatch(fhmxDto_t.getScph());
					currentStockDto2.setcWhCode(fhmxDto_t.getCkdm());
					CurrentStockDto currentStockDto1 = currentStockDao.getCurrentStocksByDto(currentStockDto2);
					BigDecimal newiQuantity=new BigDecimal(currentStockDto1.getiQuantity()) .subtract(new BigDecimal(fhmxDto_t.getFhsl()));
					if(newiQuantity.compareTo(BigDecimal.ZERO) < 0) {
						throw new BusinessException("msg","U8库存不足，不允许出库！");
					}
					currentStockDto2.setiQuantitybj("0");
					currentStockDto2.setiQuantity(fhmxDto_t.getFhsl());
					currentStockList.add(currentStockDto2);

					//组装U8发货单明细数据
					DispatchListsDto dispatchListsDto = new DispatchListsDto();
					String dlsid=String.valueOf(Integer.parseInt(uA_IdentityDto_t!=null?uA_IdentityDto_t.getiChildId():uA_IdentityDto.getiChildId())+serial_dl);
					fhmxDto_t.setFhmxglid(dlsid);
					dispatchListsDto.setiDLsID(dlsid);
					dispatchListsDto.setDLID(dispatchListDto.getDLID()); //主表ID
					dispatchListsDto.setiCorID("0"); //0/如果存在不同仓库的物料第一个仓库存0，后面的存NULL
					dispatchListsDto.setIsettleNum("0");
					dispatchListsDto.setiSettleQuantity("0");
					dispatchListsDto.setiBatch("0");
					dispatchListsDto.setfEnSettleQuan("0");
					dispatchListsDto.setfEnSettleSum("0");
					dispatchListsDto.setFsumsignquantity("0");
					dispatchListsDto.setFsumsignnum("0");
					dispatchListsDto.setBsignover(null); //null/如果存在不同仓库的物料第一个仓库存null，后面的存0
					dispatchListsDto.setBneedloss(null);
					dispatchListsDto.setFrlossqty(null);
					if (lsh != 1){
						dispatchListsDto.setiCorID(null);
						dispatchListsDto.setIsettleNum(null);
						dispatchListsDto.setiSettleQuantity(null);
						dispatchListsDto.setiBatch(null);
						dispatchListsDto.setfEnSettleQuan(null);
						dispatchListsDto.setfEnSettleSum(null);
						dispatchListsDto.setFsumsignquantity(null);
						dispatchListsDto.setFsumsignnum(null);
						dispatchListsDto.setBsignover("0");
						dispatchListsDto.setBneedloss("0");
						dispatchListsDto.setFrlossqty("0.000000");
					}
					dispatchListsDto.setcWhcode(fhmxDto_t.getCkdm()); //仓库代码
					dispatchListsDto.setcInvCode(fhmxDto_t.getWlbm()); //物料编码
					dispatchListsDto.setiQuantity(fhmxDto_t.getFhsl()); //发货数量
					dispatchListsDto.setcBatch(fhmxDto_t.getScph()); //生产批号
					dispatchListsDto.setcMemo(fhmxDto_t.getBz()); //备注
					dispatchListsDto.setdMDate(fhmxDto_t.getScrq()); //生产日期
					dispatchListsDto.setdVDate(fhmxDto_t.getYxq()); //失效日期
					dispatchListsDto.setiSosID(fhmxDto_t.getMxglid()); //U8销售订单明细ID
					dispatchListsDto.setcInvName(fhmxDto_t.getWlmc()); //物料名称
					dispatchListsDto.setfOutQuantity(fhmxDto_t.getFhsl()); //发货数量
					dispatchListsDto.setcSoCode(fhmxDto_t.getU8xsdh()); //订单号
					if (("0").equals(fhmxDto_t.getBzqflg())){
						dispatchListsDto.setcMassUnit("2");
						dispatchListsDto.setiMassDate(fhmxDto_t.getBzq());//保质期
					}else{
						dispatchListsDto.setcMassUnit("1");
						dispatchListsDto.setiMassDate("99");//保质期
					}
					if(StringUtil.isBlank(fhmxDto_t.getScph())) {
						dispatchListsDto.setcMassUnit(null);
						dispatchListsDto.setiMassDate(null);//保质期
					}
					dispatchListsDto.setCordercode(fhmxDto_t.getU8xsdh()); //销售订单
					dispatchListsDto.setIorderrowno(String.valueOf(lsh)); //订单明细流水号
					dispatchListsDto.setIrowno(String.valueOf(serial_dl-1000000000)); //明细流水号
					dispatchListsDto.setCbSysBarCode("||SA01|"+dispatchListDto.getcDLCode()+"|"+dispatchListsDto.getIrowno());
					

					dispatchListsDto.setKL("100"); //扣率
					dispatchListsDto.setKL2("100"); //扣率2
					dispatchListsDto.setiTaxRate(fhmxDto_t.getSuil()); //税率
					dispatchListsDto.setiQuotedPrice(StringUtil.isNotBlank(fhmxDto_t.getBj())?fhmxDto_t.getBj():"0"); //报价
					dispatchListsDto.setiUnitPrice(fhmxDto_t.getWsdj()); //无税单价
					dispatchListsDto.setiTaxUnitPrice(fhmxDto_t.getHsdj()); //含税单价
					if(StringUtil.isNotBlank(fhmxDto_t.getWsdj())) {
						dispatchListsDto.setiNatUnitPrice(fhmxDto_t.getWsdj()); //无税单价
						BigDecimal wszje = new BigDecimal(fhmxDto_t.getWsdj()).multiply(new BigDecimal(fhmxDto_t.getFhsl()));
						dispatchListsDto.setiMoney(wszje.setScale(5,RoundingMode.HALF_UP).toString()); //无税总价 (保留两位小数)
						dispatchListsDto.setiNatMoney(wszje.setScale(5,RoundingMode.HALF_UP).toString()); //无税总金额
					}
					if(StringUtil.isNotBlank(fhmxDto_t.getHsdj())) {
						BigDecimal hszje = new BigDecimal(fhmxDto_t.getHsdj()).multiply(new BigDecimal(fhmxDto_t.getFhsl()));
						dispatchListsDto.setiSum(hszje.setScale(5,RoundingMode.HALF_UP).toString()); //含税总金额
						dispatchListsDto.setiNatSum(hszje.setScale(5,RoundingMode.HALF_UP).toString()); //含税总金额
					}
					if(StringUtil.isNotBlank(fhmxDto_t.getWsdj()) && StringUtil.isNotBlank(fhmxDto_t.getHsdj())) {
						BigDecimal se = new BigDecimal(fhmxDto_t.getHsdj()).multiply(new BigDecimal(fhmxDto_t.getFhsl())).subtract(new BigDecimal(fhmxDto_t.getWsdj()).multiply(new BigDecimal(fhmxDto_t.getFhsl())));
						dispatchListsDto.setiTax(se.setScale(5,RoundingMode.HALF_UP).toString()); //税额
						dispatchListsDto.setiNatTax(se.setScale(5,RoundingMode.HALF_UP).toString()); //税额
					}
					if(StringUtil.isNotBlank(fhmxDto_t.getBj()) && StringUtil.isNotBlank(fhmxDto_t.getHsdj())) {
						//(报价-含税单价)×数量
						BigDecimal bjce = (new BigDecimal(fhmxDto_t.getBj()).subtract(new BigDecimal(fhmxDto_t.getHsdj()))).multiply(new BigDecimal(fhmxDto_t.getFhsl()));
						dispatchListsDto.setiDisCount(bjce.setScale(5,RoundingMode.HALF_UP).toString()); //报价差额
						dispatchListsDto.setiNatDisCount(bjce.setScale(5,RoundingMode.HALF_UP).toString()); //报价差额
					}else {
						dispatchListsDto.setiDisCount("0");
						dispatchListsDto.setiNatDisCount("0");
					}
					dispatchListsDto.setBmpforderclosed("0");
					dispatchListsDtos.add(dispatchListsDto);
					
					//处理销售出库明细数据
					RdRecordsDto rdRecordsDto = new RdRecordsDto();
					rdRecordsDto.setAutoID(serial+Integer.parseInt(uA_IdentityDto_t_rd!=null?uA_IdentityDto_t_rd.getiChildId():uA_IdentityDto_rd.getiChildId()));
					fhmxDto_t.setCkmxglid(String.valueOf(rdRecordsDto.getAutoID()));
					rdRecordsDto.setID(rdRecordDto.getID());
					rdRecordsDto.setcInvCode(fhmxDto_t.getWlbm()); //物料编码
					rdRecordsDto.setiQuantity(fhmxDto_t.getFhsl()); //发货数量
					rdRecordsDto.setcBatch(fhmxDto_t.getScph()); //生产批号
					rdRecordsDto.setdVDate(fhmxDto_t.getYxq()); //失效日期
					rdRecordsDto.setiDLsID(dispatchListsDto.getiDLsID()); //关联发货单明细
					rdRecordsDto.setiNQuantity(fhmxDto_t.getFhsl()); //发货数量
					rdRecordsDto.setdMadeDate(fhmxDto_t.getScrq()); //生产日期
					rdRecordsDto.setcPosition(fhmxDto_t.getKwckdm()); //库位
					rdRecordsDto.setIposflag(StringUtil.isNotBlank(fhmxDto_t.getKwckdm())?"1":null);
					
					if (("0").equals(fhmxDto_t.getBzqflg())){
						rdRecordsDto.setcMassUnit("2");
						rdRecordsDto.setiMassDate(fhmxDto_t.getBzq());//保质期
					}else{
						rdRecordsDto.setcMassUnit("1");
						rdRecordsDto.setiMassDate("99");//保质期
					}
					if(StringUtil.isBlank(fhmxDto_t.getScrq())) {
						rdRecordsDto.setcMassUnit("0");
						rdRecordsDto.setiMassDate(null);//保质期
					}
					rdRecordsDto.setCbdlcode(dispatchListDto.getcDLCode()); //发货单号
					rdRecordsDto.setIorderdid(fhmxDto_t.getMxglid()); //关联订单明细
					rdRecordsDto.setIordercode(fhmxDto_t.getU8xsdh()); //销售订单
					rdRecordsDto.setIorderseq(fhmxDto_t.getDdxh()); //销售订单流水
					rdRecordsDto.setIpesoseq(fhmxDto_t.getDdxh()); //销售订单流水
					rdRecordsDto.setIordertype("1");
					if(StringUtil.isNotBlank(fhmxDto_t.getDdmxid())) {
						rdRecordsDto.setIordertype("1");
					}
					rdRecordsDto.setIpesodid(fhmxDto_t.getMxglid()); //关联订单明细
					rdRecordsDto.setCpesocode(fhmxDto_t.getU8xsdh()); //销售订单
					rdRecordsDto.setIpesotype("1");
					if(StringUtil.isNotBlank(fhmxDto_t.getMxglid())) {
						rdRecordsDto.setIpesotype("1");
					}
					rdRecordsDto.setBneedbill("1");
					rdRecordsDto.setCbMemo(StringUtil.isNotBlank(fhmxDto_t.getBz())?(fhmxDto_t.getBz().length()>255?fhmxDto_t.getBz().substring(0,255):fhmxDto_t.getBz()):null); //备注
					rdRecordsDto.setIrowno(String.valueOf(serial-1000000000));//流水号
					rdRecordsDto.setCbsysbarcode("||st32|"+rdRecordDto.getcCode()+"|"+rdRecordsDto.getIrowno());
					rdRecordsDtos.add(rdRecordsDto);
					
					//流水号+1
					serial = serial + 1;
					serial_dl = serial_dl + 1;
					//组装销售订单数据
					SO_SOMainDto  sO_SOMainDto = new SO_SOMainDto();
					sO_SOMainDto.setcSOCode(fhmxDto_t.getU8xsdh()); //销售单号
					sO_SOMainDto.setiPrintCount(null);
					sO_SOMainDtos.add(sO_SOMainDto);
					
					//组装销售明细数据
					SO_SODetailsDto sO_SODetailsDto = new SO_SODetailsDto();
//					sO_SODetailsDto.setiSOsID(fhmxDto_t.getDdmxid()); //销售订单明细id
					sO_SODetailsDto.setcInvCode(fhmxDto_t.getWlbm());
					sO_SODetailsDto.setID(fhmxDto_t.getGlid());
					sO_SODetailsDto.setcSOCode(fhmxDto_t.getU8xsdh());
					sO_SODetailsDto.setiFHNum("0");
					sO_SODetailsDto.setiFHQuantity(fhmxDto_t.getFhsl()); //发货数量
					if(StringUtil.isNotBlank(fhmxDto_t.getHsdj())) {
						BigDecimal hszje_t = new BigDecimal(fhmxDto_t.getHsdj()).multiply(new BigDecimal(fhmxDto_t.getFhsl()));
						sO_SODetailsDto.setiFHMoney(hszje_t.setScale(2,RoundingMode.HALF_UP).toString()); //发货含税总价
						sO_SODetailsDto.setfVeriDispSum(hszje_t.setScale(2,RoundingMode.HALF_UP).toString()); //发货含税总价
					}
					sO_SODetailsDto.setFoutquantity(fhmxDto_t.getFhsl()); //发货数量
					sO_SODetailsDto.setFoutnum("0");
					sO_SODetailsDto.setfVeriDispQty(fhmxDto_t.getFhsl()); //发货数量
					sO_SODetailsDtos.add(sO_SODetailsDto);
					
					//组装记账记录表
					IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto = new IA_ST_UnAccountVouchDto();
					iA_ST_UnAccountVouchDto.setIDUN(String.valueOf(rdRecordsDto.getID()));
					iA_ST_UnAccountVouchDto.setIDSUN(String.valueOf(rdRecordsDto.getAutoID()));
					iA_ST_UnAccountVouchDto.setcVouTypeUN("32");
					iA_ST_UnAccountVouchDto.setcBUstypeUN("普通销售");
					iA_ST_UnAccountVouchDtos.add(iA_ST_UnAccountVouchDto);
					
					//处理货位调整表
					if(StringUtil.isNotBlank(fhmxDto_t.getKwckdm())) {
						InvPositionDto invPositionDto_out = new InvPositionDto();
						invPositionDto_out.setRdsID(String.valueOf(rdRecordsDto.getAutoID()));
						invPositionDto_out.setRDID(String.valueOf(rdRecordDto.getID()));
						invPositionDto_out.setcWhCode(fhmxDto_t.getCkdm());
						invPositionDto_out.setcPosCode(fhmxDto_t.getKwckdm());
						invPositionDto_out.setcInvCode(fhmxDto_t.getWlbm());
						invPositionDto_out.setcBatch(StringUtil.isNotBlank(fhmxDto_t.getScph())?fhmxDto_t.getScph():"");
						invPositionDto_out.setdVDate(fhmxDto_t.getYxq());
						invPositionDto_out.setiQuantity(fhmxDto_t.getFhsl());
						invPositionDto_out.setcHandler(fhglDto_t.getZsxm());
						invPositionDto_out.setdDate(sdf.format(date));
						invPositionDto_out.setbRdFlag("0");
						invPositionDto_out.setdMadeDate(fhmxDto_t.getScrq());
						if (("0").equals(fhmxDto_t.getBzqflg())) {
							invPositionDto_out.setcMassUnit("2");
							invPositionDto_out.setiMassDate(fhmxDto_t.getBzq()); // 有效期数
						} else {
							invPositionDto_out.setcMassUnit("1");
							invPositionDto_out.setiMassDate("99"); // 有效期数
						}
						if(StringUtil.isBlank(fhmxDto_t.getScrq())) {
							invPositionDto_out.setcMassUnit("0");
							invPositionDto_out.setiMassDate(null); // 有效期数
						}
						invPositionDto_out.setCvouchtype("32");
						Date date_t_o = new Date();
						invPositionDto_out.setdVouchDate(sdf.format(date_t_o));
						invPositionDtos.add(invPositionDto_out);
						
						//组装货位总数信息
						InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
						invPositionSumDto.setcWhCode(fhmxDto_t.getCkdm());
						invPositionSumDto.setcPosCode(fhmxDto_t.getKwckdm());
						invPositionSumDto.setcInvCode(fhmxDto_t.getWlbm());
						invPositionSumDto.setcBatch(StringUtil.isNotBlank(fhmxDto_t.getScph())?fhmxDto_t.getScph():"");
						//判断是否存在  仓库，物料编码，库位，追溯号一样的数据
						InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
						if(invPositionSumDto_t!=null) {
							//更新货物货物数量
							invPositionSumDto_t.setJsbj("1"); //计算标记,相减
							invPositionSumDto_t.setiQuantity(fhmxDto_t.getFhsl());
							invPositionSumDtos_mod.add(invPositionSumDto_t);
						}else {
							throw new BusinessException("msg","未找到出库库位货物信息！");
						}
					}
				}
	
			}

			
			fhmxList.addAll(fhmxDtos);
			
			// 更新最大值表
			boolean result_ui = false;
			//判断是新增还是修改
			if(uA_IdentityDto_t_rd!=null) {
				//更新
				uA_IdentityDto_t_rd.setiChildId(String.valueOf(serial-1000000001+Integer.parseInt(uA_IdentityDto_t_rd.getiChildId())));
				result_ui = uA_IdentityDao.update(uA_IdentityDto_t_rd)>0;
			}
			if(!result_ui) {
				uA_IdentityDto_rd.setiChildId(String.valueOf(serial-1000000001+Integer.parseInt(uA_IdentityDto_rd.getiChildId())));
				result_ui = uA_IdentityDao.insert(uA_IdentityDto_rd)>0;
			}
			if(!result_ui) {
				throw new BusinessException("msg", "最大id值更新失败!");
			}		

		}

		dispatchListDto.setcSaleOut(ckdh.substring(1)); //出库单号，如果存在多个，逗号拼接
		
		// 更新最大值表
		boolean result_ui = false;
		//判断是新增还是修改
		if(uA_IdentityDto_t!=null) {
			//更新
			uA_IdentityDto_t.setiChildId(String.valueOf(serial_dl-1000000001+Integer.parseInt(uA_IdentityDto_t.getiChildId())));
			result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
		}
		if(!result_ui) {
			uA_IdentityDto.setiChildId(String.valueOf(serial_dl-1000000001+Integer.parseInt(uA_IdentityDto.getiChildId())));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}
		
		
		
		int result;
		//新增发货单
		result = dispatchListDao.insert(dispatchListDto);
		if(result<1) {
			throw new BusinessException("msg","新增发货单主表失败！");
		}
		List<List<DispatchListsDto>> dispatchListsDtosLists = Lists.partition(dispatchListsDtos, 10);
		for (List<DispatchListsDto> dispatchListsDtosList : dispatchListsDtosLists) {
			//新增发货明细
			result = dispatchListsDao.insertListDtos(dispatchListsDtosList);
			if(result<1) {
				throw new BusinessException("msg","新增发货单明细失败！");
			}
		}
		//新增出库单
		result = rdRecordDao.insertList(rdRecordDtos);
		if(result<1) {
			throw new BusinessException("msg","新增出库单失败！");
		}
		//新增出库明细
		List<List<RdRecordsDto>> rdRecordsDtosLists = Lists.partition(rdRecordsDtos, 10);
		for (List<RdRecordsDto> rdRecordsDtosList : rdRecordsDtosLists) {
			result = rdRecordsDao.insertListDtos(rdRecordsDtosList);
			if(result<1) {
				throw new BusinessException("msg","新增出库明细失败！");
			}
		}
		//修改销售订单
		result = sO_SOMainDao.updateList(sO_SOMainDtos);
		if(result<1) {
			throw new BusinessException("msg","修改销售订单失败！");
		}
		//修改销售订单明细
		result = sO_SODetailsDao.updateListDtos(sO_SODetailsDtos);
		if(result<1) {
			throw new BusinessException("msg","修改销售订单明细失败！");
		}
		//更新U8库存
		result = currentStockDao.updateIQuantityList(currentStockList);
		if(result<1) {
			throw new BusinessException("msg","批量更新U8库存失败！");
		}
		
		if(invPositionDtos.size()>0) {
			int result_hw;
			if(invPositionDtos.size()>10) {
				int length = (int)Math.ceil((double)invPositionDtos.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<InvPositionDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = invPositionDtos.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(invPositionDtos.get(j));
					}
					result_hw = invPositionDao.insertDtos(list);
					if (result_hw < 1)
						throw new BusinessException("msg", "新增U8货位调整失败！");
				}
			}else{
				result_hw = invPositionDao.insertDtos(invPositionDtos);
				if (result_hw < 1)
					throw new BusinessException("msg", "新增U8货位调整失败！");
			}
		}			
		
		//修改货物总数信息
		if(invPositionSumDtos_mod.size()>0) {
			//若List过大进行截断,10个明细为一组进行添加
			int result_mod;
			if(invPositionSumDtos_mod.size()>10) {
				int length = (int)Math.ceil((double)invPositionSumDtos_mod.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<InvPositionSumDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = invPositionSumDtos_mod.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(invPositionSumDtos_mod.get(j));
					}
					result_mod = invPositionSumDao.updateDtos(list);
					if(result_mod<1) {
						throw new BusinessException("msg", "更新U8货位总数失败！");
					}
				}
			}else{
				result_mod = invPositionSumDao.updateDtos(invPositionSumDtos_mod);
				if(result_mod<1) {
					throw new BusinessException("msg", "更新U8货位总数失败！");
				}
			}
		}
		boolean result_av = addAccountVs(iA_ST_UnAccountVouchDtos,"32");
		if(!result_av) {
			throw new BusinessException("msg","新增记账信息失败！");
		}
		map.put("fhglDto", fhglDto_t);
		map.put("ckglDtos", ckglDtos);
		map.put("fhmxDtos", fhmxList);
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8DataCpjg(CpjgDto cpjgDto,List<CpjgmxDto> cpjgmxDtos) throws BusinessException {
		boolean success = false;
		Map<String,Object> map=new HashMap<>();
		Bom_bomDto bom_bomDto = new Bom_bomDto();
		//生成主键id
		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); // 存入账套
		uA_IdentityDto.setcVouchType("bom_bom"); // 存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); // 获取主键最大值
		if(uA_IdentityDto_t!=null) {
			if (StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				uA_IdentityDto_t.setiFatherId(String.valueOf(id + 1));
				bom_bomDto.setBomId(String.valueOf(id + 1000000001)); //存入主键id
				success = uA_IdentityDao.update(uA_IdentityDto_t)>0;
			}
		}else {
			uA_IdentityDto.setiFatherId("1");
			uA_IdentityDto.setiChildId("0");
			bom_bomDto.setBomId("1000000001");
			uA_IdentityDto.setiChildId("0");
			success = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!success) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}
		CpjgDto cpjgDto1 = new CpjgDto();
		cpjgDto1.setCpjgid(cpjgDto.getCpjgid());
		cpjgDto1.setU8cpjgid(bom_bomDto.getBomId());
		bom_bomDto.setBomType(cpjgDto.getLbdm());
		bom_bomDto.setVersion(cpjgDto.getBbdh());
		bom_bomDto.setVersionDesc(cpjgDto.getBbsm());
		bom_bomDto.setVersionEffDate(cpjgDto.getBbrq());
		bom_bomDto.setVersionEndDate(cpjgDto.getSxsj());
		bom_bomDto.setCreateDate(cpjgDto.getLrsj());
		bom_bomDto.setCreateUser(cpjgDto.getGrouping());
		bom_bomDto.setUpdCount("0");
		bom_bomDto.setVTid("30443");
		bom_bomDto.setStatus("3");
		bom_bomDto.setOrgStatus("1");
		bom_bomDto.setRelsDate(cpjgDto.getLrsj());
		bom_bomDto.setRelsUser(cpjgDto.getGrouping());
		bom_bomDto.setRelsTime(cpjgDto.getLrsj());
		bom_bomDto.setCreateTime(cpjgDto.getBbrq());

		bom_bomDto.setModifyDate(null);
		bom_bomDto.setModifyUser(null);
		bom_bomDto.setModifyTime(null);
		bom_bomDto.setiPrintCount("0");
		bom_bomDto.setIsWFControlled("0");
		bom_bomDto.setiVerifyState("0");
		bom_bomDto.setiReturnCount("0");
		bom_bomDto.setAuditStatus("1");
		bom_bomDto.setbProcessProduct("0");
		bom_bomDto.setbProcessMaterial("0");

		UA_IdentityDto ua_identityDto = new UA_IdentityDto();
		ua_identityDto.setcAcc_Id(accountName); // 存入账套
		ua_identityDto.setcVouchType("bom_opcomponent"); // 存入表标识
		UA_IdentityDto uaIdentityDto = uA_IdentityDao.getMax(ua_identityDto); // 获取主键最大值
		int pos = 0;
		if(uaIdentityDto!=null) {
			if (StringUtil.isNotBlank(uaIdentityDto.getiChildId())) {
				pos = Integer.parseInt(uaIdentityDto.getiChildId());
			}
		}else {
			ua_identityDto.setiChildId("0");
			ua_identityDto.setiFatherId("0");
			success = uA_IdentityDao.insert(ua_identityDto)>0;
		}
		if(!success) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}

		UA_IdentityDto identityDto = new UA_IdentityDto();
		identityDto.setcAcc_Id(accountName); // 存入账套
		identityDto.setcVouchType("bom_opcomponentopt"); // 存入表标识
		UA_IdentityDto identityDaoMax = uA_IdentityDao.getMax(identityDto); // 获取主键最大值
		int count = 0;
		if(identityDaoMax!=null) {
			if (StringUtil.isNotBlank(identityDaoMax.getiChildId())) {
				count = Integer.parseInt(identityDaoMax.getiChildId());
			}
		}else {
			identityDto.setiChildId("0");
			identityDto.setiFatherId("0");
			success = uA_IdentityDao.insert(identityDto)>0;
		}
		if(!success) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}
		Bas_partModel bas_partModel_m = new Bas_partModel();
		bas_partModel_m.setInvCode(cpjgDto.getWlbm());
		Bas_partModel partModel_m = matridxInventoryDao.select_Inventory_partid_cInvCode(bas_partModel_m);
		List<Bom_opcomponentDto> bom_opcomponentDtos = new ArrayList<>();
		List<Bom_opcomponentoptDto> bom_opcomponentoptDtos = new ArrayList<>();
		List<Bom_parentDto> bom_parentDtos = new ArrayList<>();
		List<Bom_quepartDto> bom_quepartDtos = new ArrayList<>();

		List<CpjgmxDto> cpjgmxDtoList = new ArrayList<>();
		for (CpjgmxDto cpjgmxDto : cpjgmxDtos) {
			pos ++;
			count++;
			CpjgmxDto dto = new CpjgmxDto();
			dto.setCpjgmxid(cpjgmxDto.getCpjgmxid());

			Bom_opcomponentDto bom_opcomponentDto = new Bom_opcomponentDto();
			bom_opcomponentDto.setOpComponentId(String.valueOf(1000000000+pos));
			dto.setU8cpjgmxid(bom_opcomponentDto.getOpComponentId());


			bom_opcomponentDto.setBomId(bom_bomDto.getBomId());
			bom_opcomponentDto.setSortSeq(cpjgmxDto.getZjhh());
			bom_opcomponentDto.setOpSeq(cpjgmxDto.getGxhh());

			Bas_partModel bas_partModel = new Bas_partModel();
			bas_partModel.setInvCode(cpjgmxDto.getWlbm());
			Bas_partModel partModel = matridxInventoryDao.select_Inventory_partid_cInvCode(bas_partModel);
			if (null !=partModel){
				bom_opcomponentDto.setComponentId(partModel.getPartId());
			}else{
				throw new BusinessException("msg", "未找到U8物料!");
			}

			bom_opcomponentDto.setEffBegDate(cpjgmxDto.getScrq());
			bom_opcomponentDto.setEffEndDate(cpjgmxDto.getSxrq());
			bom_opcomponentDto.setFVFlag(cpjgmxDto.getGdyl());
			bom_opcomponentDto.setBaseQtyN(cpjgmxDto.getJbyl());
			bom_opcomponentDto.setBaseQtyD(cpjgmxDto.getJcsl());
			bom_opcomponentDto.setCompScrap(cpjgmxDto.getZjshl());
			bom_opcomponentDto.setByproductFlag(cpjgmxDto.getCcp());
			bom_opcomponentDto.setOptionsId(String.valueOf(1000000000+count));
			bom_opcomponentDto.setRemark(cpjgmxDto.getBz());
			bom_opcomponentDtos.add(bom_opcomponentDto);

			Bom_opcomponentoptDto bom_opcomponentoptDto = new Bom_opcomponentoptDto();
			bom_opcomponentoptDto.setOptionsId(String.valueOf(1000000000+count));
			bom_opcomponentoptDto.setWIPType(cpjgmxDto.getGylxdm());
			bom_opcomponentoptDto.setAccuCostFlag(cpjgmxDto.getCcp());
			bom_opcomponentoptDto.setDrawDeptCode(null);
			bom_opcomponentoptDto.setWhcode(cpjgmxDto.getCkdm());
			bom_opcomponentoptDtos.add(bom_opcomponentoptDto);
			dto.setOpcomponentoptid(bom_opcomponentoptDto.getOptionsId());

			Bom_parentDto bom_parentDto = new Bom_parentDto();
			bom_parentDto.setBomId(bom_bomDto.getBomId());
			if (null !=partModel_m){
				bom_parentDto.setParentId(partModel_m.getPartId());
			}else{
				throw new BusinessException("msg", "未找到U8物料!");
			}

			bom_parentDto.setParentScrap(cpjgDto.getMjshl());
			bom_parentDtos.add(bom_parentDto);

			cpjgmxDtoList.add(dto);
		}

		Bom_quepartDto bom_quepartDto = new Bom_quepartDto();
		if (null !=partModel_m){
			if (StringUtil.isBlank(partModel_m.getbPartId())){
				bom_quepartDto.setPartId(partModel_m.getPartId());
				bom_quepartDtos.add(bom_quepartDto);
			}
		}else{
			throw new BusinessException("msg", "未找到U8物料!");
		}
		cpjgDto1.setQuepartid(partModel_m.getPartId());

		ua_identityDto.setiChildId(String.valueOf(pos));
		success = uA_IdentityDao.update(ua_identityDto)>0;
		if(!success) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}
		identityDto.setiChildId(String.valueOf(count));
		success = uA_IdentityDao.update(identityDto)>0;
		if(!success) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}

		success = bom_bomDao.insert(bom_bomDto)!=0;
		if(!success)
			throw new BusinessException("msg", "U8主表信息新增失败!");
		success = bom_opcomponentDao.insertList(bom_opcomponentDtos)!=0;
		if(!success)
			throw new BusinessException("msg", "U8明细信息新增失败!");
		success = bom_opcomponentoptDao.insertList(bom_opcomponentoptDtos)!=0;
		if(!success)
			throw new BusinessException("msg", "U8 opt表信息新增失败!");
		if (!CollectionUtils.isEmpty(bom_quepartDtos)){
			success = bom_quepartDao.insertList(bom_quepartDtos)!=0;
			if(!success)
				throw new BusinessException("msg", "U8 quepart表信息新增失败!");
		}
		success = bom_parentDao.insertList(bom_parentDtos)!=0;
		if(!success)
			throw new BusinessException("msg", "U8 parent表信息新增失败!");
		map.put("cpjgDto", cpjgDto1);
		map.put("cpjgmxDtoList", cpjgmxDtoList);
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public void delU8DataCpjg(CpjgDto cpjgDto, List<CpjgmxDto> cpjgmxDtos) throws BusinessException {
		boolean success;
		Map<String,Object> map=new HashMap<>();
		Bom_bomDto bom_bomDto = new Bom_bomDto();
		bom_bomDto.setBomId(cpjgDto.getU8cpjgid());

		Bom_quepartDto bom_quepartDto = new Bom_quepartDto();
		bom_quepartDto.setPartId(cpjgDto.getQuepartid());

		Bom_opcomponentDto bom_opcomponentDto = new Bom_opcomponentDto();
		bom_opcomponentDto.setBomId(cpjgDto.getU8cpjgid());

		Bom_parentDto bom_parentDto = new Bom_parentDto();
		bom_parentDto.setBomId(cpjgDto.getU8cpjgid());

		if (!CollectionUtils.isEmpty(cpjgmxDtos)){
			Bom_opcomponentoptDto bom_opcomponentoptDto = new Bom_opcomponentoptDto();
			List<String> strings = new ArrayList<>();
			for (CpjgmxDto cpjgmxDto : cpjgmxDtos) {
				strings.add(cpjgmxDto.getOpcomponentoptid());
			}
            bom_opcomponentoptDto.setIds(strings);
            success = bom_opcomponentoptDao.delete(bom_opcomponentoptDto)!=0;
            if(!success)
                throw new BusinessException("msg", "U8 opt表信息更改失败!");
        }
		success = bom_parentDao.delete(bom_parentDto)!=0;
		if(!success)
			throw new BusinessException("msg", "U8 parent表信息更改失败!");
		bom_quepartDao.delete(bom_quepartDto);
		// if(!success)
		// 	throw new BusinessException("msg", "U8 quepart表信息更改失败!");
		success = bom_opcomponentDao.delete(bom_opcomponentDto)!=0;
		if(!success)
			throw new BusinessException("msg", "U8明细信息更改失败!");
		success = bom_bomDao.delete(bom_bomDto)!=0;
		if(!success)
			throw new BusinessException("msg", "U8主表信息更改失败!");
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public Map<String,Object> addPurchaseToU8(Map<String,Object> data,List<QgmxDto> qgmxlist,QgglDto qgglDto_t) throws BusinessException{
		Map<String,Object> map=new HashMap<>();
		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); //存入账套
		uA_IdentityDto.setcVouchType("PuApp");	//存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
		if(uA_IdentityDto_t!=null){
			if(StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				int id_t = id + 1000000001;
				uA_IdentityDto_t.setiFatherId(String.valueOf(id+1));
				data.put("ID",String.valueOf(id_t));
			}
		}else{
			data.put("ID","1000000001");
			uA_IdentityDto.setiFatherId("1");
		}

		PU_AppVouchDto addCgdToU8=pu_appvouchDao.insertPUAppVouch(data);
		if(!StringUtils.isNotBlank(addCgdToU8.getID()))
			throw new BusinessException("ICOM99019","U8系统保存失败！");

		map.put("glid",addCgdToU8.getID());
		int serial = 0;
		if(uA_IdentityDto_t!=null) {
			serial=Integer.parseInt(uA_IdentityDto_t.getiChildId());
		}

		for(int i=0;i<qgmxlist.size();i++) {
			PU_AppVouchsDto pu_appvouchsDto=new PU_AppVouchsDto();
			pu_appvouchsDto.setID(addCgdToU8.getID());
			pu_appvouchsDto.setcInvCode(qgmxlist.get(i).getWlbm());
			pu_appvouchsDto.setfQuantity(qgmxlist.get(i).getSl());
			pu_appvouchsDto.setdRequirDate(qgmxlist.get(i).getQwrq());
			pu_appvouchsDto.setiPerTaxRate("16.0000");//税率
			pu_appvouchsDto.setiReceivedQTY("0.00000");
			pu_appvouchsDto.setcItem_class(qgglDto_t.getXmdldm());
			pu_appvouchsDto.setcItemCode(qgglDto_t.getXmbmdm());
			pu_appvouchsDto.setCItemName(qgglDto_t.getXmbmmc());
			pu_appvouchsDto.setbTaxCost("1");
			pu_appvouchsDto.setCexch_name("人民币");
			pu_appvouchsDto.setiExchRate("1.000");//兑率
			pu_appvouchsDto.setCbsysbarcode("||pupr|"+qgglDto_t.getDjh()+"|"+(i+1));
			//U8cbMemo字段长度为255，防止过长报错
			if(StringUtils.isNotBlank(qgmxlist.get(i).getBz())){
				if(qgmxlist.get(i).getBz().length()>255){
					pu_appvouchsDto.setCbMemo(qgmxlist.get(i).getBz().substring(0,255));
				}else{
					pu_appvouchsDto.setCbMemo(qgmxlist.get(i).getBz());
				}
			}
			pu_appvouchsDto.setIvouchrowno(String.valueOf(i+1));
			int id = serial + 1000000001;
			pu_appvouchsDto.setAutoID(String.valueOf(id));
			serial=serial+1;
			PU_AppVouchsDto addQgmxToU8=pu_appvouchDao.insertPUAppVouchs(pu_appvouchsDto);
			if(!StringUtils.isNotBlank(addQgmxToU8.getAutoID()))
				throw new BusinessException("ICOM99019","U8系统保存请购明细失败！");
			qgmxlist.get(i).setGlid(addQgmxToU8.getAutoID());
			qgmxlist.get(i).setPrefixFlg(prefixFlg);
			map.put("qgmxlist",qgmxlist);
		}
		// 更新最大值表
		boolean result_ui = false;
		//判断是新增还是修改
		if(uA_IdentityDto_t!=null) {
			//更新
			uA_IdentityDto_t.setiChildId(String.valueOf(serial));
			result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
		}
		if(!result_ui) {
			uA_IdentityDto.setiChildId(String.valueOf(serial));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}
		return map;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public Map<String,Object> updatePurchaseToU8(List<QgmxDto> qgmxList,QgglDto qgglDto,List<QgmxDto> insertList,List<QgmxDto> delQgmxDtos) throws BusinessException{
		Map<String,Object> map=new HashMap<>();
		// 更新U8中请购单信息
		PU_AppVouchDto pu_appvouchDto = new PU_AppVouchDto();
		pu_appvouchDto.setcCode(qgglDto.getDjh());
		pu_appvouchDto.setCsysbarcode("||pupr|" + qgglDto.getDjh());
		pu_appvouchDto.setcDefine1(qgglDto.getJlbh());
		pu_appvouchDto.setcMaker(qgglDto.getSqrmc());
		pu_appvouchDto.setdDate(qgglDto.getSqrq());
		if(StringUtils.isBlank(qgglDto.getSqbmdm()))
			throw new BusinessException("ICOM99019","申请部门部门代码不允许为空！");
		pu_appvouchDto.setcDepCode(qgglDto.getSqbmdm());
		pu_appvouchDto.setcMemo(qgglDto.getBz());
		pu_appvouchDto.setID(qgglDto.getGlid());
		boolean updatepu_appvouch = pu_appvouchDao.updatePUAppVouch(pu_appvouchDto);
		if (!updatepu_appvouch)
			throw new BusinessException("ICOM00002", "U8系统保存失败！");
		if(insertList != null && insertList.size() > 0) {
			qgmxList.addAll(insertList);
		}
		List<QgmxDto> addlist=new ArrayList<>();//传递回去的参数
		for (int i = 0; i < qgmxList.size(); i++) {
			PU_AppVouchsDto pu_appvouchsDto = new PU_AppVouchsDto();
			pu_appvouchsDto.setID(qgglDto.getGlid());
			pu_appvouchsDto.setcInvCode(qgmxList.get(i).getWlbm());
			pu_appvouchsDto.setfQuantity(qgmxList.get(i).getSl());
			pu_appvouchsDto.setdRequirDate(qgmxList.get(i).getQwrq());
			pu_appvouchsDto.setiPerTaxRate("16.0000");// 税率
			pu_appvouchsDto.setiReceivedQTY("0.00000");
			pu_appvouchsDto.setcItem_class(qgglDto.getXmdldm());
			pu_appvouchsDto.setcItemCode(qgglDto.getXmbmdm());
			pu_appvouchsDto.setCItemName(qgglDto.getXmmc());
			pu_appvouchsDto.setbTaxCost("1");
			pu_appvouchsDto.setCexch_name("人民币");
			pu_appvouchsDto.setiExchRate("1.000");// 兑率
			pu_appvouchsDto.setCbsysbarcode("||pupr|" + qgglDto.getDjh() + "|" + (i + 1));
			//U8cbMemo字段长度为255，防止过长报错
			if(qgmxList.get(i).getBz().length()>255){
				pu_appvouchsDto.setCbMemo(qgmxList.get(i).getBz().substring(0,255));
			}else{
				pu_appvouchsDto.setCbMemo(qgmxList.get(i).getBz());
			}
			pu_appvouchsDto.setAutoID(qgmxList.get(i).getGlid());
			pu_appvouchsDto.setIvouchrowno(String.valueOf(i + 1));
			// 如果关联ID存在，则更新U8明细数据,否则新增
			if (StringUtils.isNotBlank(qgmxList.get(i).getGlid())) {
				boolean updateQgmx = pu_appvouchDao.updatePUAppVouchs(pu_appvouchsDto);
				if (!updateQgmx)
					throw new BusinessException("ICOM00002", "更新U8明细数据失败！");
			} else {
				if(StringUtil.isBlank(pu_appvouchsDto.getAutoID())) {
					UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
					uA_IdentityDto.setcAcc_Id(accountName); //存入账套
					uA_IdentityDto.setcVouchType("PuApp");	//存入表标识
					UA_IdentityDto uA_IdentityDto_s = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
					if(StringUtil.isNotBlank(uA_IdentityDto_s.getiChildId())) {
						int id = Integer.parseInt(uA_IdentityDto_s.getiChildId());
						int id_s = id + 1000000001;
						uA_IdentityDto.setiChildId(String.valueOf(id+1)); //存入副表最大值
						pu_appvouchsDto.setAutoID(String.valueOf(id_s));
					}else {
						pu_appvouchsDto.setAutoID("1000000001");
						uA_IdentityDto.setiChildId("1"); //存入副表最大值
					}
					//更新主表id最大值
					int result_ua = uA_IdentityDao.update(uA_IdentityDto);
					if(result_ua<1) {
						throw new BusinessException("msg","更新U8主表id最大值失败！");
					}
				}
				PU_AppVouchsDto addQgmxToU8 = pu_appvouchDao.insertPUAppVouchs(pu_appvouchsDto);

				if (StringUtils.isNotBlank(addQgmxToU8.getAutoID())) {
					qgmxList.get(i).setGlid(addQgmxToU8.getAutoID());
					addlist.add(qgmxList.get(i));
				}
			}
		}
		map.put("addlist",addlist);
		if(delQgmxDtos != null && delQgmxDtos.size() > 0) {
			pu_appvouchDao.deletePUAppVouchs(delQgmxDtos);
		}
		return map;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public Map<String,Object> cancelPurchaseToU8(QgglDto qgglDto,List<QgmxDto> qgmxList) throws BusinessException{
		Map<String,Object> map=new HashMap<>();
		PU_AppVouchDto pu_appvouchDto = new PU_AppVouchDto();
		pu_appvouchDto.setcCode(qgglDto.getDjh());
		pu_appvouchDto.setCsysbarcode("||pupr|" + qgglDto.getDjh());
		pu_appvouchDto.setcDefine1(qgglDto.getJlbh());
		pu_appvouchDto.setcMaker(qgglDto.getSqrmc());
		pu_appvouchDto.setdDate(qgglDto.getSqrq());
		if(StringUtils.isBlank(qgglDto.getSqbmdm()))
			throw new BusinessException("ICOM99019","申请部门部门代码不允许为空！");
		pu_appvouchDto.setcDepCode(qgglDto.getSqbmdm());
		pu_appvouchDto.setcMemo(qgglDto.getBz());
		pu_appvouchDto.setID(qgglDto.getGlid());
		boolean updatepu_appvouch = pu_appvouchDao.updatePUAppVouch(pu_appvouchDto);
		if (!updatepu_appvouch)
			throw new BusinessException("ICOM00002", "U8系统保存失败！");
		List<QgmxDto> addlist=new ArrayList<>();
		for (int i = 0; i < qgmxList.size(); i++) {
			PU_AppVouchsDto pu_appvouchsDto = new PU_AppVouchsDto();
			pu_appvouchsDto.setID(qgglDto.getGlid());
			pu_appvouchsDto.setcInvCode(qgmxList.get(i).getWlbm());
			pu_appvouchsDto.setfQuantity(qgmxList.get(i).getSl());
			pu_appvouchsDto.setdRequirDate(qgmxList.get(i).getQwrq());
			pu_appvouchsDto.setiPerTaxRate("16.0000");// 税率
			pu_appvouchsDto.setiReceivedQTY("0.00000");
			pu_appvouchsDto.setcItem_class(qgglDto.getXmdldm());
			pu_appvouchsDto.setcItemCode(qgglDto.getXmbmdm());
			pu_appvouchsDto.setCItemName(qgglDto.getXmmc());
			pu_appvouchsDto.setbTaxCost("1");
			pu_appvouchsDto.setCexch_name("人民币");
			pu_appvouchsDto.setiExchRate("1.000");// 兑率
			pu_appvouchsDto.setCbsysbarcode("||pupr|" + qgglDto.getDjh() + "|" + (i + 1));
			//U8cbMemo字段长度为255，防止过长报错
			if(qgmxList.get(i).getBz().length()>255){
				pu_appvouchsDto.setCbMemo(qgmxList.get(i).getBz().substring(0,255));
			}else{
				pu_appvouchsDto.setCbMemo(qgmxList.get(i).getBz());
			}
			pu_appvouchsDto.setAutoID(qgmxList.get(i).getGlid());
			pu_appvouchsDto.setIvouchrowno(String.valueOf(i + 1));
			// 如果关联ID存在，则更新U8明细数据,否则新增
			if (StringUtils.isNotBlank(qgmxList.get(i).getGlid())) {
				boolean updateQgmx = pu_appvouchDao.updatePUAppVouchs(pu_appvouchsDto);
				if (!updateQgmx)
					throw new BusinessException("ICOM00002", "更新U8明细数据失败！");
			} else {
				PU_AppVouchsDto addQgmxToU8 = pu_appvouchDao.insertPUAppVouchs(pu_appvouchsDto);
				if (StringUtils.isNotBlank(addQgmxToU8.getAutoID())) {
					qgmxList.get(i).setGlid(addQgmxToU8.getAutoID());
					addlist.add(qgmxList.get(i));
				}
			}
		}
		map.put("addlist",addlist);
		return map;
	}

	/**
	 * 领料出库红冲单
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public Map<String, Object> redInkOffsetOfDeliveryOrder(RdRecordDto rdRecordDto) throws BusinessException {
		//获得出库主表数据
		List<RdRecordDto> rdRecordDtos = rdRecord11Dao.queryRd(rdRecordDto);
		//获得出库明细数据
		RdRecordsDto rdRecordsDto = new RdRecordsDto();
		rdRecordsDto.setIds(rdRecordDto.getIds());
		List<RdRecordsDto> rdRecordsDtos = rdRecords11Dao.getDtoList(rdRecordsDto);
		//存U8红冲单的关联id
		List<CkglDto> ckglDtos = new ArrayList<>();
		List<CkmxDto> ckmxDtos = new ArrayList<>();
		//存更新的库存信息
		List<CurrentStockDto> currentStockList = new ArrayList<>();
		//存更新的领料信息
		List<MaterialAppVouchsDto> materialAppVouchsList = new ArrayList<>();
		//存货位调整
		List<InvPositionDto> invPositionDtos = new ArrayList<>();
		//存货位总数信息
		List<InvPositionSumDto> invPositionSumDtos_mod = new ArrayList<>();
		//存记账记录数据
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos = new ArrayList<>();
		
		//循环生成出库红冲单
		for (RdRecordDto rdRecordDto_t : rdRecordDtos) {
			SimpleDateFormat sdf_d = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			String prefix = sdf_d.format(date);
			VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
			voucherHistoryDto.setcSeed(prefix);
			voucherHistoryDto.setCardNumber("0412");
			//获取最大流水号
			VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
			if(voucherHistoryDto_t!=null) {
				String serial = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
				rdRecordDto_t.setcCode(prefix+(serial.length()>1?serial:"0"+serial)); // 出库单号
				rdRecordDto_t.setPrefix(prefix);
				rdRecordDto_t.setSflxbj("11");
				List<RdRecordDto> rdRecordDtos_l = rdRecordDao.getcCodeRd(rdRecordDto_t);
				int ccode_new=0;
		        if (rdRecordDtos_l != null && rdRecordDtos_l.size() > 0) {
		            List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(rdRecordDto_t);
		            ccode_new = Integer.parseInt(rdRecordDto_t.getcCode()) + 1;
		            for (RdRecordDto rdRecordDto_c : rdList) {
						if(ccode_new-Integer.parseInt(rdRecordDto_c.getcCode())==0) {
							ccode_new = ccode_new+1;
						}else {
							break;
						}
					}
		        }
		        if(ccode_new!=0) {
		        	rdRecordDto_t.setcCode(String.valueOf(ccode_new)); // 出库单号
		        	voucherHistoryDto_t.setcNumber(String.valueOf(ccode_new).substring(8)); //去除多余0
		        }else {
		        	voucherHistoryDto_t.setcNumber(serial);
		        }        
				//更新最大单号值		
				int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
				if(result_ccode<1)
					throw new BusinessException("msg", "更新U8最大单号失败！");
			}else {
				voucherHistoryDto.setiRdFlagSeed("2");
				voucherHistoryDto.setcContent("日期");
				voucherHistoryDto.setcContentRule("日");
				voucherHistoryDto.setbEmpty("0");
				voucherHistoryDto.setcNumber("1");
				rdRecordDto_t.setcCode(prefix+"01");
				//单号最大表增加数据
				int result_ccode = voucherHistoryDao.insert(voucherHistoryDto);
				if(result_ccode<1)
					throw new BusinessException("msg", "新增U8最大单号失败！");
			}
			//生成主键id
			int rd_id;
			UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
			uA_IdentityDto.setcAcc_Id(accountName); //存入账套
			uA_IdentityDto.setcVouchType("rd");	//存入表标识
			UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
			if(uA_IdentityDto_t!=null) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				uA_IdentityDto_t.setiFatherId(String.valueOf(id +1));
				rd_id = id + 1000000001;
			}else {
				uA_IdentityDto.setiFatherId("1");
				rd_id=1000000001;
			}
			//明细表主键id最大值
			int childid = 0;
			if(uA_IdentityDto_t!=null) {
				childid = Integer.parseInt(uA_IdentityDto_t.getiChildId());
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			rdRecordDto_t.setdDate(sdf.format(date));

			for (RdRecordsDto rdRecordsDto_t : rdRecordsDtos) {
				if(rdRecordDto_t.getID()==rdRecordsDto_t.getID()) {
					CkmxDto ckmxDto = new CkmxDto();
					ckmxDto.setCkmxglid(String.valueOf(rdRecordsDto_t.getAutoID()));
					
					rdRecordsDto_t.setID(rd_id); // 关联id
					//存入主键id
					rdRecordsDto_t.setAutoID(childid + 1000000001);
					childid = childid + 1; //明细表主键最大值+1
					double sl = -Double.parseDouble(rdRecordsDto_t.getiQuantity());
					rdRecordsDto_t.setiQuantity(Double.toString(sl)); //出库数量
					rdRecordsDto_t.setiNQuantity(Double.toString(sl));//实际出库数量
					rdRecordsDto_t.setCbsysbarcode("||st11|" + rdRecordDto_t.getcCode() + "|" + rdRecordsDto_t.getIrowno());
					rdRecordsDto_t.setCorufts(null);
					rdRecordsDto_t.setBcanreplace("0");
					
					ckmxDto.setHcmxglid(String.valueOf(rdRecordsDto_t.getAutoID()));
					ckmxDtos.add(ckmxDto);
					//组装记账记录表
					IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto = new IA_ST_UnAccountVouchDto();
					iA_ST_UnAccountVouchDto.setIDUN(String.valueOf(rdRecordsDto_t.getID()));
					iA_ST_UnAccountVouchDto.setIDSUN(String.valueOf(rdRecordsDto_t.getAutoID()));
					iA_ST_UnAccountVouchDto.setcVouTypeUN("11");
					iA_ST_UnAccountVouchDto.setcBUstypeUN("领料");
					iA_ST_UnAccountVouchDtos.add(iA_ST_UnAccountVouchDto);
					
					//组装U8领料数据
					MaterialAppVouchsDto materialAppVouchsDto = new MaterialAppVouchsDto();
					materialAppVouchsDto.setfOutQuantity(Double.toString(sl)); //出库数量
					materialAppVouchsDto.setAutoID(rdRecordsDto_t.getiMaIDs()); //领料明细id
					materialAppVouchsList.add(materialAppVouchsDto);
					
					//处理货位调整表
					if(StringUtil.isNotBlank(rdRecordsDto_t.getcPosition())) {
						InvPositionDto invPositionDto_out = new InvPositionDto();
						invPositionDto_out.setRdsID(String.valueOf(rdRecordsDto_t.getAutoID()));
						invPositionDto_out.setRDID(String.valueOf(rdRecordsDto_t.getID()));
						invPositionDto_out.setcWhCode(rdRecordDto_t.getcWhCode());
						invPositionDto_out.setcPosCode(rdRecordsDto_t.getcPosition());
						invPositionDto_out.setcInvCode(rdRecordsDto_t.getcInvCode());
						invPositionDto_out.setcBatch(StringUtil.isNotBlank(rdRecordsDto_t.getcBatch())?rdRecordsDto_t.getcBatch():"");
						invPositionDto_out.setdVDate(StringUtil.isNotBlank(rdRecordsDto_t.getdVDate())?rdRecordsDto_t.getdVDate():null);
						invPositionDto_out.setiQuantity(Double.toString(sl));
						invPositionDto_out.setcHandler(rdRecordDto_t.getcHandler());
						invPositionDto_out.setdDate(rdRecordDto_t.getdDate());
						invPositionDto_out.setbRdFlag("0");
						invPositionDto_out.setdMadeDate(StringUtil.isNotBlank(rdRecordsDto_t.getdMadeDate())?rdRecordsDto_t.getdMadeDate():null);

						invPositionDto_out.setcMassUnit(rdRecordsDto_t.getcMassUnit());
						invPositionDto_out.setiMassDate(rdRecordsDto_t.getiMassDate()); // 有效期数

						if(StringUtil.isBlank(rdRecordsDto_t.getdMadeDate())) {
							invPositionDto_out.setcMassUnit("0");
							invPositionDto_out.setiMassDate(null); // 有效期数
						}
						invPositionDto_out.setCvouchtype("11");
						Date date_t_o = new Date();
						invPositionDto_out.setdVouchDate(sdf.format(date_t_o));
						invPositionDtos.add(invPositionDto_out);
						
						//组装货位总数信息
						InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
						invPositionSumDto.setcWhCode(rdRecordDto_t.getcWhCode());
						invPositionSumDto.setcPosCode(rdRecordsDto_t.getcPosition());
						invPositionSumDto.setcInvCode(rdRecordsDto_t.getcInvCode());
						invPositionSumDto.setcBatch(StringUtil.isNotBlank(rdRecordsDto_t.getcBatch())?rdRecordsDto_t.getcBatch():"");
						//判断是否存在  仓库，物料编码，库位，追溯号一样的数据
						InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
						if(invPositionSumDto_t!=null) {
							//更新货物货物数量
							invPositionSumDto_t.setJsbj("1"); //计算标记,相减
							invPositionSumDto_t.setiQuantity(Double.toString(sl));
							invPositionSumDtos_mod.add(invPositionSumDto_t);
						}else {
							throw new BusinessException("msg","未找到出库库位货物信息！");
						}
					}
					
					//处理库存量
					CurrentStockDto currentStockDto = new CurrentStockDto();
					currentStockDto.setiQuantity(Double.toString(sl)); //出库数量
					currentStockDto.setcInvCode(rdRecordsDto_t.getcInvCode()); //物料编码
					currentStockDto.setcWhCode(rdRecordDto_t.getcWhCode()); //仓库代码
					currentStockDto.setcBatch(StringUtil.isNotBlank(rdRecordsDto_t.getcBatch())?rdRecordsDto_t.getcBatch():""); //生产批号
					currentStockList.add(currentStockDto);
				}
			}
			
			CkglDto ckglDto = new CkglDto();
			ckglDto.setGlid(String.valueOf(rdRecordDto_t.getID()));
			rdRecordDto_t.setID(rd_id);
			ckglDto.setHcglid(String.valueOf(rdRecordDto_t.getID()));
			ckglDto.setU8hcdh(rdRecordDto_t.getcCode());
			ckglDtos.add(ckglDto);
			rdRecordDto_t.setCsysbarcode("||st11|"+rdRecordDto.getcCode());	//	"||st11|"+出库单
			rdRecordDto_t.setiMQuantity("0");
			rdRecordDto_t.setbOMFirst("0");
			rdRecordDto_t.setBmotran("0");
			rdRecordDto_t.setbHYVouch("0");
			
			// 更新最大值表
			boolean result_ui = false;
			//判断是新增还是修改
			if(uA_IdentityDto_t!=null) {
				//更新
				uA_IdentityDto_t.setiChildId(String.valueOf(childid));
				result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
			}
			if(!result_ui) {
				uA_IdentityDto.setiChildId(String.valueOf(childid));
				result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
			}
			if(!result_ui) {
				throw new BusinessException("msg", "最大id值更新失败!");
			}
			
		}
		
		//更新U8库存
		int result_kc = currentStockDao.updateList(currentStockList);
		if(result_kc<1) {
			throw new BusinessException("msg","批量更新U8库存失败！");
		}
		
		//新增出库
		int result_c = rdRecord11Dao.insertList(rdRecordDtos);
		if(result_c<1) {
			throw new BusinessException("msg","批量新增出库单失败！");
		}
				
		//U8新增出库明细
		//若List过大进行截断,10个明细为一组进行添加
		if(rdRecordsDtos.size()>10){
			int length=(int)Math.ceil((double)rdRecordsDtos.size()/10);//向上取整
			for(int i = 0; i<length; i++){
				List<RdRecordsDto> list=new ArrayList<>();
				int t_length;
				if(i!=length-1){
					t_length=(i+1)*10;
				}else{
					t_length=rdRecordsDtos.size();
				}
				for(int j=i*10;j<t_length;j++){
					list.add(rdRecordsDtos.get(j));
				}
				int result_rd  = rdRecords11Dao.insertList(list);
				if(result_rd<1)
					throw new BusinessException("msg","批量更新U8出库明细失败！");
			}
		}else{
			int result_rd = rdRecords11Dao.insertList(rdRecordsDtos);
			if(result_rd<1) {
				throw new BusinessException("msg","批量更新U8出库明细失败！");
			}
		}
		
		if(invPositionDtos.size()>0) {
			int result_hw;
			if(invPositionDtos.size()>10) {
				int length = (int)Math.ceil((double)invPositionDtos.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<InvPositionDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = invPositionDtos.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(invPositionDtos.get(j));
					}
					result_hw = invPositionDao.insertDtos(list);
					if (result_hw < 1)
						throw new BusinessException("msg", "新增U8货位调整失败！");
				}
			}else{
				result_hw = invPositionDao.insertDtos(invPositionDtos);
				if (result_hw < 1)
					throw new BusinessException("msg", "新增U8货位调整失败！");
			}
		}
		
		//修改货物总数信息
		if(invPositionSumDtos_mod.size()>0) {
			//若List过大进行截断,10个明细为一组进行添加
			int result_mod;
			if(invPositionSumDtos_mod.size()>10) {
				int length = (int)Math.ceil((double)invPositionSumDtos_mod.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<InvPositionSumDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = invPositionSumDtos_mod.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(invPositionSumDtos_mod.get(j));
					}
					result_mod = invPositionSumDao.updateDtos(list);
					if(result_mod<1) {
						throw new BusinessException("msg", "更新U8货位总数失败！");
					}
				}
			}else{
				result_mod = invPositionSumDao.updateDtos(invPositionSumDtos_mod);
				if(result_mod<1) {
					throw new BusinessException("msg", "更新U8货位总数失败！");
				}
			}
		}
		
		boolean result_av = addAccountVs(iA_ST_UnAccountVouchDtos,"11");
		if(!result_av) {
			throw new BusinessException("msg","新增记账信息失败！");
		}	
		int result_ma = materialAppVouchsDao.updateList(materialAppVouchsList);
		if(result_ma<1) {
			throw new BusinessException("msg","批量更新U8领料信息失败！");
		}
		Map<String,Object> map=new HashMap<>();
		map.put("ckglDtos",ckglDtos);
		map.put("ckmxDtos",ckmxDtos);
		
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8DataByOutDepot(CkglDto ckglDto) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		//获取出库明细
		CkmxDto ckmxDto = new CkmxDto();
		ckmxDto.setCkid(ckglDto.getCkid());
		List<CkmxDto> ckmxList;
		if("WW".equals(ckglDto.getCklbcskz1())){
			ckmxList=ckmxService.getWWDtoList(ckmxDto);
		}else{
			ckmxList=ckmxService.getHcDtoList(ckmxDto);
		}
		//出库存入U8
		RdRecordDto rdRecordDto = new RdRecordDto();
		List<CkmxDto> ckmxList_mx = new ArrayList<>();
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String prefix = sdf_d.format(date);
		VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
		voucherHistoryDto.setcSeed(prefix);
		voucherHistoryDto.setCardNumber("0412");
		//获取最大流水号
		VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
		if(voucherHistoryDto_t!=null) {
			String serial = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
			rdRecordDto.setcCode(prefix+(serial.length()>1?serial:"0"+serial)); // 到货单号
			rdRecordDto.setPrefix(prefix);
			rdRecordDto.setSflxbj("11");
			List<RdRecordDto> rdRecordDtos_t = rdRecordDao.getcCodeRd(rdRecordDto);
			int ccode_new=0;
			if (rdRecordDtos_t != null && rdRecordDtos_t.size() > 0) {
				List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(rdRecordDto);
				ccode_new = Integer.parseInt(rdRecordDto.getcCode()) + 1;
				for (RdRecordDto rdRecordDto_c : rdList) {
					if(ccode_new-Integer.parseInt(rdRecordDto_c.getcCode())==0) {
						ccode_new = ccode_new+1;
					}else {
						break;
					}
				}
			}
			if(ccode_new!=0) {
				rdRecordDto.setcCode(String.valueOf(ccode_new)); // 到货单号
				voucherHistoryDto_t.setcNumber(String.valueOf(ccode_new).substring(8)); //去除多余0
			}else {
				voucherHistoryDto_t.setcNumber(serial);
			}
			//更新最大单号值
			int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
			if(result_ccode<1)
				throw new BusinessException("msg", "更新U8最大单号失败！");
		}else {
			voucherHistoryDto.setiRdFlagSeed("2");
			voucherHistoryDto.setcContent("日期");
			voucherHistoryDto.setcContentRule("日");
			voucherHistoryDto.setbEmpty("0");
			voucherHistoryDto.setcNumber("1");
			rdRecordDto.setcCode(prefix+"01");
			//单号最大表增加数据
			int result_ccode = voucherHistoryDao.insert(voucherHistoryDto);
			if(result_ccode<1)
				throw new BusinessException("msg", "新增U8最大单号失败！");
		}

		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); //存入账套
		uA_IdentityDto.setcVouchType("rd");	//存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
		if(uA_IdentityDto_t!=null) {
			int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
			uA_IdentityDto_t.setiFatherId(String.valueOf(id +1));
			rdRecordDto.setID(id + 1000000001);
		}else {
			uA_IdentityDto.setiFatherId("1");
			rdRecordDto.setID(1000000001);
		}
		//关联出库id
		ckglDto.setGlid(String.valueOf(rdRecordDto.getID()));
		ckglDto.setU8ckdh(rdRecordDto.getcCode());
		SimpleDateFormat sdf_t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if("WW".equals(ckglDto.getCklbcskz1())){
			rdRecordDto.setbRdFlag("0");
			rdRecordDto.setcVouchType("11");
			rdRecordDto.setcBusType("委外发料"); //出库类别名称
			rdRecordDto.setcSource("委外订单"); //出库类型
			rdRecordDto.setcWhCode(ckglDto.getCkdm()); //仓库代码
			rdRecordDto.setdDate(ckglDto.getCkrq()); //出库日期
			rdRecordDto.setcDepCode(ckglDto.getJgdm()); //机构代码
			rdRecordDto.setcVenCode(ckmxList.get(0).getGysdm());
			rdRecordDto.setcHandler(ckglDto.getZsxm()); //审核人
			rdRecordDto.setcMemo(ckmxList.get(0).getHtbz()); //记录编号
			rdRecordDto.setcMaker(ckglDto.getZsxm()); //制单人
			rdRecordDto.setcDefine1(ckmxList.get(0).getQgjlbh());
			rdRecordDto.setDveriDate(sdf_t.format(date));

			//判断是否引用一条订单明细
			String htmxid=ckmxList.get(0).getWwhtmxid();
			boolean flag=true;
			for (CkmxDto ckmxDto_f : ckmxList) {
				if(!htmxid.equals(ckmxDto_f.getWwhtmxid())) {
					flag=false;
					break;
				}
			}
			if(flag){
				rdRecordDto.setiMQuantity(ckmxList.get(0).getSl());
				rdRecordDto.setcPsPcode(ckmxList.get(0).getHtwlbm());
			}
			rdRecordDto.setVT_ID("65");
			rdRecordDto.setcMPoCode(ckmxList.get(0).getHtnbbh());
			rdRecordDto.setIproorderid(ckmxList.get(0).getU8omid());
			rdRecordDto.setDnmaketime(sdf_t.format(date));
			rdRecordDto.setDnverifytime(sdf_t.format(date));
			rdRecordDto.setCsysbarcode("||st11|"+rdRecordDto.getcCode());	//	"||st11|"+出库单
		}else{
			rdRecordDto.setcBusType("领料"); //出库类别名称
			rdRecordDto.setcSource("库存"); //出库类型
//		MaterialAppVouchDto materialAppVouchDto_t = materialAppVouchDao.getDtoById(llglDto.getGlid());
//		rdRecordDto.setcBusCode(""); //领料单号
			rdRecordDto.setcMemo(ckglDto.getJlbh()); //记录编号
			rdRecordDto.setcWhCode(ckglDto.getCkdm()); //仓库代码
			rdRecordDto.setdDate(ckglDto.getCkrq()); //出库日期
			rdRecordDto.setcRdCode("b"); //出库参数代码
			rdRecordDto.setcDepCode(ckglDto.getJgdm()); //机构代码
			rdRecordDto.setcHandler(ckglDto.getZsxm()); //审核人
			rdRecordDto.setcMaker(ckglDto.getZsxm()); //制单人
			rdRecordDto.setDveriDate(sdf_t.format(date));
			rdRecordDto.setDnmaketime(sdf_t.format(date));
			rdRecordDto.setiMQuantity("0");
			rdRecordDto.setDnverifytime(sdf_t.format(date));
			rdRecordDto.setCsysbarcode("||st11|"+rdRecordDto.getcCode());	//	"||st11|"+出库单
			rdRecordDto.setbOMFirst("0");
			rdRecordDto.setBmotran("0");
			rdRecordDto.setbHYVouch("0");
			rdRecordDto.setIproorderid("0");
			//判断是否存在货位为空
			for (CkmxDto ckmxDto_f : ckmxList) {
				if(StringUtil.isBlank(ckmxDto_f.getKwbhcsdm())) {
					rdRecordDto.setbOMFirst(null);
					rdRecordDto.setBmotran(null);
					rdRecordDto.setbHYVouch(null);
				}
			}
		}

		int result = rdRecord11Dao.insert(rdRecordDto);
		if(result<1) {
			throw new BusinessException("msg","出库数据存入U8失败！");
		}
		//存明细数据用来批量更新
		List<RdRecordsDto> rdRecordsList = new ArrayList<>();
		//明细表主键id最大值
		int childid = 0;
		if(uA_IdentityDto_t!=null) {
			childid = Integer.parseInt(uA_IdentityDto_t.getiChildId());
		}
		//明细数据排序
		int irowno = 1;
		//存更新的库存信息
		List<CurrentStockDto> currentStockList = new ArrayList<>();
//		//存更新的领料信息
//		List<MaterialAppVouchsDto> materialAppVouchsList = new ArrayList<>();
		//存货位调整
		List<InvPositionDto> invPositionDtos = new ArrayList<>();
		//存货位总数信息
		List<InvPositionSumDto> invPositionSumDtos_mod = new ArrayList<>();
		List<InvPositionSumDto> invPositionSumDtos_add = new ArrayList<>();
		List<OM_MODetailsDto> om_moDetailsDtos = new ArrayList<>();
		List<OM_MOMaterialsDto> om_omMaterialsDtos = new ArrayList<>();
		//存记账记录数据
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos = new ArrayList<>();
		//领料明细表
		List<MaterialAppVouchsDto> materialAppVouchsDtos = new ArrayList<>();
		for (CkmxDto ckmxDto_t : ckmxList) {
			if(!"0.00".equals(ckmxDto_t.getCksl()) && StringUtil.isNotBlank(ckmxDto_t.getCksl())) {
				if (!"WW".equals(ckglDto.getCklbcskz1())) {
					//出库红字修改领料单的fOutQuantity
					MaterialAppVouchsDto materialAppVouchsDto = new MaterialAppVouchsDto();
					materialAppVouchsDto.setAutoID(ckmxDto_t.getGlid());
					materialAppVouchsDto.setfOutQuantity(ckmxDto_t.getCksl());
					materialAppVouchsDtos.add(materialAppVouchsDto);
				}
				//组装U8明细数据
				RdRecordsDto rdRecordsDto = new RdRecordsDto();
				rdRecordsDto.setID(rdRecordDto.getID()); // 关联id
				//存入主键id
				rdRecordsDto.setAutoID(childid + 1000000001);
				childid = childid + 1; //明细表主键最大值+1
				CkmxDto ckmx=new CkmxDto();
				ckmx.setCkmxglid(String.valueOf(rdRecordsDto.getAutoID()));
				ckmx.setCkmxid(ckmxDto_t.getCkmxid());
				ckmx.setXgry(ckglDto.getXgry());
				ckmxList_mx.add(ckmx);
				rdRecordsDto.setcInvCode(ckmxDto_t.getWlbm()); //物料编码
				rdRecordsDto.setcBatch(StringUtil.isNotBlank(ckmxDto_t.getScph())?ckmxDto_t.getScph():null); //生产批号
				rdRecordsDto.setdVDate(StringUtil.isNotBlank(ckmxDto_t.getYxq())?ckmxDto_t.getYxq():null); //失效日期
				rdRecordsDto.setcPosition(StringUtil.isNotBlank(ckmxDto_t.getKwbhcsdm())?ckmxDto_t.getKwbhcsdm():null); //库位编号/货位
				rdRecordsDto.setcItem_class(ckmxDto_t.getXmdlcsdm()); //项目大类代码
				rdRecordsDto.setcItemCode(ckmxDto_t.getXmbmcsdm()); //项目编码代码
				rdRecordsDto.setcName(ckmxDto_t.getXmbmcsmc()); //项目编码名称
				rdRecordsDto.setcItemCName(ckmxDto_t.getXmdlcsmc()); //项目大类名称
//				rdRecordsDto.setiNQuantity(ckmxDto_t.getCksl()); //实领数量
				rdRecordsDto.setdMadeDate(StringUtil.isNotBlank(ckmxDto_t.getScrq())?ckmxDto_t.getScrq():null); //生产日期
				if (("0").equals(ckmxDto_t.getBzqflg())){
					rdRecordsDto.setcMassUnit("2");
					rdRecordsDto.setiMassDate(ckmxDto_t.getBzq()); //有效期数
				}else{
					rdRecordsDto.setcMassUnit("1");
					rdRecordsDto.setiMassDate("99"); //有效期数
				}
				if(StringUtil.isBlank(ckmxDto_t.getScrq())) {
					rdRecordsDto.setcMassUnit("0");
					rdRecordsDto.setiMassDate(null); //有效期数
				}
				if("WW".equals(ckglDto.getCklbcskz1())) {
					OM_MODetailsDto om_moDetailsDto=new OM_MODetailsDto();
					om_moDetailsDto.setMODetailsID(ckmxDto_t.getU8ommxid());
					om_moDetailsDto.setiMaterialSendQty(ckmxDto_t.getCkzsl());
					om_moDetailsDtos.add(om_moDetailsDto);
					if(ckmxDto_t.getZcksl().equals(ckmxDto_t.getCksl())){
						rdRecordsDto.setBcanreplace("0");
						rdRecordsDto.setbOutMaterials("0");
					}
					rdRecordsDto.setiQuantity(ckmxDto_t.getCksl()); //出库数量
					rdRecordsDto.setiNQuantity(ckmxDto_t.getKcksl()); //可出数量
					rdRecordsDto.setiOMoDID(ckmxDto_t.getU8ommxid());
					rdRecordsDto.setComcode(ckmxDto_t.getHtnbbh());
					rdRecordsDto.setIpesotype("8");
					rdRecordsDto.setCpesocode(ckmxDto_t.getHtnbbh());
					rdRecordsDto.setInvcode(ckmxDto_t.getHtwlbm());
					OM_MOMaterialsDto om_moMaterialsDto=new OM_MOMaterialsDto();
					om_moMaterialsDto.setMoDetailsID(ckmxDto_t.getU8ommxid());
					om_moMaterialsDto.setcInvCode(ckmxDto_t.getWlbm());
					om_moMaterialsDto.setiSendQTY(ckmxDto_t.getCksl());
					om_moMaterialsDto.setiSendNum("0");
					om_omMaterialsDtos.add(om_moMaterialsDto);
					OM_MOMaterialsDto dto = om_moMaterialsDao.getDto(om_moMaterialsDto);
					if(dto!=null){
						rdRecordsDto.setiOMoMID(dto.getMOMaterialsID());
						rdRecordsDto.setIpesodid(dto.getMOMaterialsID());
					}
				}else{
					rdRecordsDto.setiQuantity(String.valueOf(Double.parseDouble(ckmxDto_t.getCksl())*-1.00)); //出库数量
					rdRecordsDto.setiMaIDs(ckmxDto_t.getGlid()); //关联申请单id
					rdRecordsDto.setIpesotype("0");
				}
				rdRecordsDto.setIrowno(String.valueOf(irowno)); //当前明细排序 hwllbz
				rdRecordsDto.setCbMemo(StringUtil.isNotBlank(ckmxDto_t.getHwllbz())?(ckmxDto_t.getHwllbz().length()>255?ckmxDto_t.getHwllbz().substring(0,255):ckmxDto_t.getHwllbz()):null); //到货备注
				rdRecordsDto.setCbsysbarcode("||st11|" + rdRecordDto.getcCode() + "|" + irowno);
				if(StringUtil.isBlank(ckmxDto_t.getKwbhcsdm())) {
					rdRecordsDto.setIposflag(null);
				}else {
					rdRecordsDto.setIposflag("1");
				}
				irowno = irowno + 1;
				rdRecordsList.add(rdRecordsDto);

				//组装记账记录表
				IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto = new IA_ST_UnAccountVouchDto();
				iA_ST_UnAccountVouchDto.setIDUN(String.valueOf(rdRecordsDto.getID()));
				iA_ST_UnAccountVouchDto.setIDSUN(String.valueOf(rdRecordsDto.getAutoID()));
				iA_ST_UnAccountVouchDto.setcVouTypeUN("11");
				if("WW".equals(ckglDto.getCklbcskz1())) {
					iA_ST_UnAccountVouchDto.setcBUstypeUN("委外发料");
				}else {
					iA_ST_UnAccountVouchDto.setcBUstypeUN("红冲");
				}
				iA_ST_UnAccountVouchDtos.add(iA_ST_UnAccountVouchDto);


				//处理货位调整表
				if(StringUtil.isNotBlank(ckmxDto_t.getKwbhcsdm())) {
					InvPositionDto invPositionDto_out = new InvPositionDto();
					invPositionDto_out.setRdsID(String.valueOf(rdRecordsDto.getAutoID()));
					invPositionDto_out.setRDID(String.valueOf(rdRecordDto.getID()));
					invPositionDto_out.setcWhCode(ckglDto.getCkdm());
					invPositionDto_out.setcPosCode(ckmxDto_t.getKwbhcsdm());
					invPositionDto_out.setcInvCode(ckmxDto_t.getWlbm());
					invPositionDto_out.setcBatch(StringUtil.isNotBlank(ckmxDto_t.getScph())?ckmxDto_t.getScph():"");
					invPositionDto_out.setdVDate(StringUtil.isNotBlank(ckmxDto_t.getYxq())?ckmxDto_t.getYxq():null);
					invPositionDto_out.setiQuantity(ckmxDto_t.getCksl());
					invPositionDto_out.setcHandler(ckglDto.getZsxm());
					invPositionDto_out.setdDate(sdf_t.format(date));
					invPositionDto_out.setbRdFlag("0");
					invPositionDto_out.setdMadeDate(StringUtil.isNotBlank(ckmxDto_t.getScrq())?ckmxDto_t.getScrq():null);
					if (("0").equals(ckmxDto_t.getBzqflg())) {
						invPositionDto_out.setcMassUnit("2");
						invPositionDto_out.setiMassDate(ckmxDto_t.getBzq()); // 有效期数
					} else {
						invPositionDto_out.setcMassUnit("1");
						invPositionDto_out.setiMassDate("99"); // 有效期数
					}
					if(StringUtil.isBlank(ckmxDto_t.getScrq())) {
						invPositionDto_out.setcMassUnit("0");
						invPositionDto_out.setiMassDate(null); // 有效期数
					}
					invPositionDto_out.setCvouchtype("11");
					Date date_t_o = new Date();
					invPositionDto_out.setdVouchDate(sdf_t.format(date_t_o));
					invPositionDtos.add(invPositionDto_out);

					//组装货位总数信息
					InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
					invPositionSumDto.setcWhCode(ckglDto.getCkdm());
					invPositionSumDto.setcPosCode(ckmxDto_t.getKwbhcsdm());
					invPositionSumDto.setcInvCode(ckmxDto_t.getWlbm());
					invPositionSumDto.setcBatch(StringUtil.isNotBlank(ckmxDto_t.getScph())?ckmxDto_t.getScph():"");
					//判断是否存在  仓库，物料编码，库位，追溯号一样的数据
					InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
					if(invPositionSumDto_t!=null) {
						//更新货物货物数量
						if("WW".equals(ckglDto.getCklbcskz1())) {
							invPositionSumDto_t.setJsbj("1"); //计算标记,相减
						}else {
							invPositionSumDto_t.setJsbj("0"); //计算标记,相加
						}
						invPositionSumDto_t.setiQuantity(ckmxDto_t.getCksl());
						invPositionSumDtos_mod.add(invPositionSumDto_t);
					}else {
						//不存在，做新增
						invPositionSumDto.setiQuantity(ckmxDto_t.getCksl());
						invPositionSumDto.setInum("0");
						invPositionSumDto.setcMassUnit("1");
						invPositionSumDto.setiMassDate("99"); // 保质期
						invPositionSumDto.setdMadeDate(ckmxDto_t.getScrq());
						invPositionSumDto.setdVDate(ckmxDto_t.getYxq());
						invPositionSumDtos_add.add(invPositionSumDto);
					}
				}

			}
		}


		List<CkmxDto> ckmxs = ckmxService.getDtoGroupBy(ckglDto.getCkid());

		for (CkmxDto ckmxDto_s : ckmxs) {
			//处理库存
			CurrentStockDto currentStockDto2 = new CurrentStockDto();
			currentStockDto2.setcInvCode(ckmxDto_s.getWlbm());
			currentStockDto2.setcBatch(ckmxDto_s.getScph());
			currentStockDto2.setcWhCode(ckmxDto_s.getCkdm());
			CurrentStockDto currentStockDto1 = currentStockDao.getCurrentStocksByDto(currentStockDto2);
			if (null != currentStockDto1){
//				if(newiQuantity.compareTo(BigDecimal.ZERO) == -1 ) {
				//BigDecimal newiQuantity=new BigDecimal(currentStockDto1.getiQuantity()) .add(new BigDecimal(ckmxDto_s.getZcksl()));
				CurrentStockDto currentStockDto = new CurrentStockDto();

				if("WW".equals(ckglDto.getCklbcskz1())) {
					currentStockDto.setiQuantitybj("0");
				}else {
					currentStockDto.setiQuantitybj("1");
				}
				currentStockDto.setiQuantity(ckmxDto_s.getZcksl()); //出库数量
				currentStockDto.setcInvCode(ckmxDto_s.getWlbm()); //物料编码
				currentStockDto.setcWhCode(ckmxDto_s.getCkdm()); //仓库代码
				currentStockDto.setcBatch(ckmxDto_s.getScph()); //生产批号
				currentStockList.add(currentStockDto);
			}else{
				CurrentStockDto currentStockDto = new CurrentStockDto();
				currentStockDto.setiQuantity(ckmxDto_s.getZcksl()); //出库数量
				currentStockDto.setcInvCode(ckmxDto_s.getWlbm()); //物料编码
				currentStockDto.setcWhCode(ckmxDto_s.getCkdm()); //仓库代码
				currentStockDto.setcBatch(ckmxDto_s.getScph()); //生产批号
				boolean itemidflg = true;
				//处理库存量
				List<String> ids = new ArrayList<>();
				ids.add(ckmxDto_s.getWlbm());
				CurrentStockDto curr = new CurrentStockDto();
				curr.setIds(ids);
				List<CurrentStockDto> currentStockDtos = currentStockDao.queryBycInvCode(curr);
				if (null != currentStockDtos && currentStockDtos.size()>0) {
					// 如果物料编码一样，追溯号不一样，ItemId字段相同
					currentStockDto.setItemId(currentStockDtos.get(0).getItemId());
					itemidflg = false;
				}
				if (itemidflg) {
					SCM_ItemDto sCM_ItemDto_c  = sCM_ItemDao.getDtoBycInvVode(currentStockDto.getcInvCode());
					if(sCM_ItemDto_c!=null) {
						currentStockDto.setItemId(sCM_ItemDto_c.getId());
					}else {
						// 取SCM_Item表中最大值
						int num_ItemId = sCM_ItemDao.getMaxItemId() + 1;
						currentStockDto.setItemId(String.valueOf(num_ItemId));
						// 存入新增的物料
						SCM_ItemDto sCM_ItemDto = new SCM_ItemDto();
						sCM_ItemDto.setcInvCode(currentStockDto.getcInvCode()); // 物料编码
						int result_scm = sCM_ItemDao.insert(sCM_ItemDto);
						if (result_scm < 1) {
							throw new BusinessException("msg", "sCM_Item新增失败！");
						}
					}
				}
				currentStockDto.setdVDate(ckmxDto_s.getYxq()); // 失效日期
				currentStockDto.setdMdate(ckmxDto_s.getScrq()); // 生产日期
				currentStockDto.setiExpiratDateCalcu("0");
				currentStockDto.setcMassUnit("1"); // 有效单位
				currentStockDto.setiMassDate("99"); // 有效期数
				int result_add = currentStockDao.insert(currentStockDto);
				if (result_add < 1) {
					throw new BusinessException("msg", "U8库存新增失败！");
				}
			}

		}
		if (!CollectionUtils.isEmpty(currentStockList)){
			//更新U8库存
			int result_kc = currentStockDao.updateIQuantityList(currentStockList);
			if(result_kc<1) {
				throw new BusinessException("msg","批量更新U8库存失败！");
			}
		}

		if (!CollectionUtils.isEmpty(om_moDetailsDtos)){
			//更新U8库存
			int result_t = om_moDetailsDao.updateRkxx(om_moDetailsDtos);
			if(result_t<1) {
				throw new BusinessException("msg","批量更新U8库存失败！");
			}
		}

		if (!CollectionUtils.isEmpty(om_omMaterialsDtos)){
			//更新U8库存
			int result_a = om_moMaterialsDao.updateList(om_omMaterialsDtos);
			if(result_a<1) {
				throw new BusinessException("msg","批量更新U8库存失败！");
			}
		}
		if (!CollectionUtils.isEmpty(materialAppVouchsDtos)){
			int result_a = materialAppVouchsDao.updateFOutQuantitys(materialAppVouchsDtos);
			if(result_a<1) {
				throw new BusinessException("msg","批量更新U8货物领料明细数量失败！");
			}
		}


		// 更新最大值表
		boolean result_ui = false;
		//判断是新增还是修改
		if(uA_IdentityDto_t!=null) {
			//更新
			uA_IdentityDto_t.setiChildId(String.valueOf(childid));
			result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
		}
		if(!result_ui) {
			uA_IdentityDto.setiChildId(String.valueOf(childid));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}

		//U8新增出库明细
		//若List过大进行截断,10个明细为一组进行添加
		if(rdRecordsList.size()>10){
			int length=(int)Math.ceil((double)rdRecordsList.size()/10);//向上取整
			for(int i = 0; i<length; i++){
				List<RdRecordsDto> list=new ArrayList<>();
				int t_length;
				if(i!=length-1){
					t_length=(i+1)*10;
				}else{
					t_length=rdRecordsList.size();
				}
				for(int j=i*10;j<t_length;j++){
					list.add(rdRecordsList.get(j));
				}
				int result_rd;
				if("WW".equals(ckglDto.getCklbcskz1())) {
					result_rd=rdRecords11Dao.insertWWCKList(list);
				}else{
					result_rd=rdRecords11Dao.insertCKHCList(list);
				}

				if(result_rd<1)
					throw new BusinessException("msg","批量更新U8出库明细失败！");
			}
		}else{
			int result_rd;
			if("WW".equals(ckglDto.getCklbcskz1())) {
				result_rd=rdRecords11Dao.insertWWCKList(rdRecordsList);
			}else{
				result_rd=rdRecords11Dao.insertCKHCList(rdRecordsList);
			}
			if(result_rd<1) {
				throw new BusinessException("msg","批量更新U8出库明细失败！");
			}
		}

		if(invPositionDtos.size()>0) {
			int result_hw;
			if(invPositionDtos.size()>10) {
				int length = (int)Math.ceil((double)invPositionDtos.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<InvPositionDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = invPositionDtos.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(invPositionDtos.get(j));
					}
					result_hw = invPositionDao.insertDtos(list);
					if (result_hw < 1)
						throw new BusinessException("msg", "新增U8货位调整失败！");
				}
			}else{
				result_hw = invPositionDao.insertDtos(invPositionDtos);
				if (result_hw < 1)
					throw new BusinessException("msg", "新增U8货位调整失败！");
			}
		}

		//修改货物总数信息
		if(invPositionSumDtos_mod.size()>0) {
			//若List过大进行截断,10个明细为一组进行添加
			int result_mod;
			if(invPositionSumDtos_mod.size()>10) {
				int length = (int)Math.ceil((double)invPositionSumDtos_mod.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<InvPositionSumDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = invPositionSumDtos_mod.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(invPositionSumDtos_mod.get(j));
					}
					result_mod = invPositionSumDao.updateDtos(list);
					if(result_mod<1) {
						throw new BusinessException("msg", "更新U8货位总数失败！");
					}
				}
			}else{
				result_mod = invPositionSumDao.updateDtos(invPositionSumDtos_mod);
				if(result_mod<1) {
					throw new BusinessException("msg", "更新U8货位总数失败！");
				}
			}
		}

		//新增货物总数信息
		if(invPositionSumDtos_add.size()>0) {
			//若List过大进行截断,10个明细为一组进行添加
			int result_add;
			if(invPositionSumDtos_add.size()>10) {
				int length = (int)Math.ceil((double)invPositionSumDtos_add.size() / 10);//向上取整   2
				for (int i = 0; i < length; i++) {
					List<InvPositionSumDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = invPositionSumDtos_add.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(invPositionSumDtos_add.get(j));
					}
					result_add = invPositionSumDao.insertDtos(list);
					if(result_add<1) {
						throw new BusinessException("msg", "新增U8货位总数失败！");
					}
				}
			}else{
				result_add = invPositionSumDao.insertDtos(invPositionSumDtos_add);
				if(result_add<1) {
					throw new BusinessException("msg", "新增U8货位总数失败！");
				}
			}
		}
		boolean result_av = addAccountVs(iA_ST_UnAccountVouchDtos,"11");
		if(!result_av) {
			throw new BusinessException("msg","新增记账信息失败！");
		}
		map.put("ckmxList", ckmxList_mx);
		map.put("ckgl", ckglDto);
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8SaleData(XsglDto xsglDto,List<XsmxDto> dtoList) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		SO_SOMainDto so_soMainDto=new SO_SOMainDto();
		VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
		voucherHistoryDto.setCardNumber("17");
		//获取最大流水号
		VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);
		if(voucherHistoryDto_t!=null) {
			String serialnum = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
			String serial = "0000000000"+ serialnum;
			serial = serial.substring(serial.length()-10);
			so_soMainDto.setcSOCode(serial);
			//判断单号是否存在
			int ccode_new=0;
			SO_SOMainDto so_soMainDto_t = so_soMainDao.getcSOCode(so_soMainDto);
			if(so_soMainDto_t!=null) {
				List<SO_SOMainDto> list = so_soMainDao.getcSOCodeRd(so_soMainDto);
				ccode_new = Integer.parseInt(so_soMainDto.getcSOCode()) + 1;
				for (SO_SOMainDto dto : list) {
					if(ccode_new-Integer.parseInt(dto.getcSOCode())==0) {
						ccode_new = ccode_new+1;
					}else {
						break;
					}
				}
			}
			if(ccode_new!=0) {
				so_soMainDto.setcSOCode(("000000000"+ccode_new).substring(serial.length()-10,serial.length())); // 调拨单号
				voucherHistoryDto_t.setcNumber(ccode_new+"");
			}else {
				voucherHistoryDto_t.setcNumber(serialnum);
			}
			//更新最大单号值
			int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
			if(result_ccode<1)
				throw new BusinessException("msg", "更新U8最大单号失败！");
		}else {
			voucherHistoryDto.setcNumber("1");
			voucherHistoryDto.setbEmpty("0");
			so_soMainDto.setcSOCode("0000000001");
			//单号最大表增加数据
			int result_ccode_in = voucherHistoryDao.insert(voucherHistoryDto);
			if(result_ccode_in<1)
				throw new BusinessException("msg", "新增U8最大单号失败！");
		}
		xsglDto.setU8xsdh(so_soMainDto.getcSOCode());
		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); //存入账套
		uA_IdentityDto.setcVouchType("SOMain");	//存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
		if(uA_IdentityDto_t!=null) {
			if(StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				uA_IdentityDto_t.setiFatherId(String.valueOf(id +1));
				so_soMainDto.setID(String.valueOf(id + 1000000001));
			}
		}else {
			uA_IdentityDto.setiFatherId("1");
			so_soMainDto.setID("1000000001");
		}
		xsglDto.setGlid(so_soMainDto.getID());
		so_soMainDto.setcSTCode(xsglDto.getXslxdm());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = new Date();
		so_soMainDto.setdDate(sdf.format(date1));
		so_soMainDto.setcCusCode(xsglDto.getKhdm());
		so_soMainDto.setcDepCode(xsglDto.getXsbmdm());
		so_soMainDto.setcCusOAddress(xsglDto.getShdz());
		so_soMainDto.setcMemo(StringUtil.isNotBlank(xsglDto.getBz())?xsglDto.getBz(): "客户终端："+xsglDto.getKhzd()+",运输要求："+xsglDto.getYsyq());
		so_soMainDto.setcMaker(xsglDto.getZsxm());
		so_soMainDto.setcVerifier(xsglDto.getZsxm());
		so_soMainDto.setiStatus("1");
		so_soMainDto.setbDisFlag("0");
		so_soMainDto.setcCusName(xsglDto.getKhjcmc());
		so_soMainDto.setcBusType("普通销售");
		so_soMainDto.setiVTid("95");
		so_soMainDto.setdPreDateBT(sdf.format(date1));
		so_soMainDto.setdPreMoDateBT(sdf.format(date1));
		so_soMainDto.setIverifystate("0");
		so_soMainDto.setIswfcontrolled("0");
		so_soMainDto.setCmodifier(xsglDto.getZsxm());
		so_soMainDto.setDmoddate(sdf.format(date1));
		so_soMainDto.setDverifydate(sdf.format(date1));
		so_soMainDto.setDcreatesystime(sdf.format(date1));
		so_soMainDto.setDverifysystime(sdf.format(date1));
		so_soMainDto.setDmodifysystime(sdf.format(date1));
		so_soMainDto.setcPersonCode(xsglDto.getGrouping());
		so_soMainDto.setBcashsale("0");
		so_soMainDto.setBmustbook("0");
		so_soMainDto.setcSysBarCode("||SA17"+"|"+so_soMainDto.getcSOCode()); //||SA01|+单号
		so_soMainDto.setContract_status("1");
		so_soMainDto.setCinvoicecompany(xsglDto.getKhdm());
		so_soMainDto.setCbcode("001");
		so_soMainDto.setCexch_name("人民币");
		so_soMainDto.setiExchRate("1");
		so_soMainDto.setiTaxRate(xsglDto.getSuil());
		so_soMainDto.setbReturnFlag("0");
		so_soMainDto.setbOrder("0");
		int result_mv = so_soMainDao.insert(so_soMainDto);
		if(result_mv<1)
			throw new BusinessException("msg","U8主表新增失败！");
		// 更新最大值表
		boolean result_ui = false;

		List<SO_SODetailsDto> list=new ArrayList<>();
		int icid=0;
		if(uA_IdentityDto_t!=null) {
			if(StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				icid = Integer.parseInt(uA_IdentityDto_t.getiChildId());
			}
		}else {
			icid=1;
		}
		for(int k=0;k<dtoList.size();k++){
			BigDecimal sl=new BigDecimal(dtoList.get(k).getSl());
			BigDecimal hsdj=new BigDecimal(dtoList.get(k).getHsdj());
			BigDecimal bj=new BigDecimal(dtoList.get(k).getBj());
			BigDecimal jsze=new BigDecimal(dtoList.get(k).getJsze());
			BigDecimal wszje=new BigDecimal(dtoList.get(k).getWszje());
			//BigDecimal percent=new BigDecimal(100);
			SO_SODetailsDto so_soDetailsDto=new SO_SODetailsDto();
			icid+=1;
			so_soDetailsDto.setiSOsID(String.valueOf(1000000000+icid));
			dtoList.get(k).setMxglid(so_soDetailsDto.getiSOsID());
			so_soDetailsDto.setcSOCode(so_soMainDto.getcSOCode());
			so_soDetailsDto.setcInvCode(dtoList.get(k).getWlbm());
			so_soDetailsDto.setdPreDate(dtoList.get(k).getYfhrq());
			so_soDetailsDto.setiQuantity(dtoList.get(k).getSl());
			so_soDetailsDto.setiQuotedPrice(dtoList.get(k).getBj());
			so_soDetailsDto.setiUnitPrice(dtoList.get(k).getWsdj());
			so_soDetailsDto.setiTaxUnitPrice(dtoList.get(k).getHsdj());
			so_soDetailsDto.setiMoney((wszje.setScale(2,RoundingMode.HALF_UP)).toString());
			so_soDetailsDto.setiTax(String.valueOf(jsze.subtract(wszje).setScale(2,RoundingMode.HALF_UP)));
			so_soDetailsDto.setiSum((hsdj.compareTo(BigDecimal.ZERO)==0)?"0":String.valueOf(sl.multiply(hsdj)));
			BigDecimal subtract=bj.subtract(hsdj);
			if(subtract.compareTo(BigDecimal.ZERO) == 0){
				so_soDetailsDto.setiDisCount("0");
			}else{
				so_soDetailsDto.setiDisCount(String.valueOf(subtract.multiply(sl)));
			}
			so_soDetailsDto.setiNatUnitPrice(dtoList.get(k).getWsdj());
			so_soDetailsDto.setiNatMoney((wszje.setScale(2,RoundingMode.HALF_UP)).toString());
			so_soDetailsDto.setiNatTax(String.valueOf(jsze.subtract(wszje).setScale(2,RoundingMode.HALF_UP)));
			so_soDetailsDto.setiNatSum((hsdj.compareTo(BigDecimal.ZERO)==0)?"0":String.valueOf(sl.multiply(hsdj)));
			if(subtract.compareTo(BigDecimal.ZERO) == 0){
				so_soDetailsDto.setiNatDisCount("0");
			}else{
				so_soDetailsDto.setiNatDisCount(String.valueOf(subtract.multiply(sl)));
			}
			so_soDetailsDto.setcInvName(dtoList.get(k).getWlmc());
			so_soDetailsDto.setiTaxRate(dtoList.get(k).getSuil());
			so_soDetailsDto.setID(so_soMainDto.getID());
			so_soDetailsDto.setFcusminprice("0");
			so_soDetailsDto.setBallpurchase("0");
			so_soDetailsDto.setdPreMoDate(sdf.format(date1));
			so_soDetailsDto.setbOrderBOM("0");
			so_soDetailsDto.setbOrderBOMOver("0");
			so_soDetailsDto.setBusecusbom("0");
			so_soDetailsDto.setBsaleprice("1");
			so_soDetailsDto.setBgift("0");
			so_soDetailsDto.setfSaleCost("0");
			so_soDetailsDto.setfSalePrice("0");
			so_soDetailsDto.setiRowNo(String.valueOf(k+1));
			dtoList.get(k).setXh(String.valueOf(k+1));
			so_soDetailsDto.setKL("100");
			so_soDetailsDto.setKL2("100");
			so_soDetailsDto.setCbSysBarCode("||SA17|"+so_soMainDto.getcSOCode()+"|"+ (k + 1));
			list.add(so_soDetailsDto);
		}

		if(uA_IdentityDto_t!=null) {
			//更新
			uA_IdentityDto_t.setiChildId(String.valueOf(icid));
			result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
		}
		if(!result_ui) {
			uA_IdentityDto.setiChildId(String.valueOf(icid));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}
		result_mv = sO_SODetailsDao.insertList(list);
		if(result_mv<1)
			throw new BusinessException("msg","U8明细新增失败！");

		map.put("xsglDto",xsglDto);
		map.put("xsmxlist",dtoList);
		return map;
	}

	/**
	 * 新增U8固定资产数据
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public boolean addU8Asset(GdzcglDto gdzcglDto) throws BusinessException {
		Fa_ObjectsDto fa_objectsDto_a = fa_objectsDao.getlMaxIDByiObjectNum("5");
		if(fa_objectsDto_a!=null){
			int i = Integer.parseInt(fa_objectsDto_a.getlMaxID());
			Fa_ObjectsDto fa_objectsDto=new Fa_ObjectsDto();
			fa_objectsDto.setiObjectNum("5");
			fa_objectsDto.setlMaxID(String.valueOf(i+1));
			int update = fa_objectsDao.update(fa_objectsDto);
			if(update<=0){
				throw new BusinessException("msg","fa_objects表更新失败！");
			}
		}else{
			Fa_ObjectsDto fa_objectsDto=new Fa_ObjectsDto();
			fa_objectsDto.setiObjectNum("5");
			fa_objectsDto.setlMaxID("1");
			int insert = fa_objectsDao.insert(fa_objectsDto);
			if(insert<=0){
				throw new BusinessException("msg","fa_objects表更新失败！");
			}
		}
		Fa_CardsDto fa_cardsDto=new Fa_CardsDto();
		Fa_ObjectsDto fa_objectsDto_t = fa_objectsDao.getlMaxIDByiObjectNum("1");
		if(fa_objectsDto_t!=null){
			int i = Integer.parseInt(fa_objectsDto_t.getlMaxID());
			Fa_ObjectsDto fa_objectsDto=new Fa_ObjectsDto();
			fa_objectsDto.setiObjectNum("1");
			fa_objectsDto.setlMaxID(String.valueOf(i+1));
			fa_cardsDto.setsCardID(String.valueOf(i+1));
			int update = fa_objectsDao.update(fa_objectsDto);
			if(update<=0){
				throw new BusinessException("msg","fa_objects表更新失败！");
			}
		}else{
			Fa_ObjectsDto fa_objectsDto=new Fa_ObjectsDto();
			fa_objectsDto.setiObjectNum("1");
			fa_objectsDto.setlMaxID("1");
			fa_cardsDto.setsCardID("1");
			int insert = fa_objectsDao.insert(fa_objectsDto);
			if(insert<=0){
				throw new BusinessException("msg","fa_objects表更新失败！");
			}
		}
		fa_cardsDto.setsCardNum(gdzcglDto.getKpbh());
		fa_cardsDto.setsAssetNum(gdzcglDto.getGdzcbh());
		fa_cardsDto.setsAssetName(gdzcglDto.getWlmc());
		Fa_ObjectsDto fa_objectsDto_x = fa_objectsDao.getlMaxIDByiObjectNum("2");
		if(fa_objectsDto_x!=null){
			int i = Integer.parseInt(fa_objectsDto_x.getlMaxID());
			Fa_ObjectsDto fa_objectsDto=new Fa_ObjectsDto();
			fa_objectsDto.setiObjectNum("2");
			fa_objectsDto.setlMaxID(String.valueOf(i+1));
			fa_cardsDto.setlOptID(String.valueOf(i+1));
			fa_cardsDto.setsZWVoucherNum(String.valueOf(i+1));
			int update = fa_objectsDao.update(fa_objectsDto);
			if(update<=0){
				throw new BusinessException("msg","fa_objects表更新失败！");
			}
		}else{
			Fa_ObjectsDto fa_objectsDto=new Fa_ObjectsDto();
			fa_objectsDto.setiObjectNum("2");
			fa_objectsDto.setlMaxID("1");
			fa_cardsDto.setlOptID("1");
			fa_cardsDto.setsZWVoucherNum("1");
			int insert = fa_objectsDao.insert(fa_objectsDto);
			if(insert<=0){
				throw new BusinessException("msg","fa_objects表更新失败！");
			}
		}
		fa_cardsDto.setiOptType("2");
		fa_cardsDto.setiNewType("2");
		fa_cardsDto.setsDeptNum(gdzcglDto.getBmdm());
		fa_cardsDto.setsTypeNum(gdzcglDto.getLbdm());
		fa_cardsDto.setsOrgID(gdzcglDto.getZjfsdm());
		fa_cardsDto.setsOrgAddID(gdzcglDto.getZjfscskz1());
		fa_cardsDto.setsStatusID(gdzcglDto.getSyzkdm());
		fa_cardsDto.setsDeprMethodID(gdzcglDto.getZjffdm());
		fa_cardsDto.setsCurrency(gdzcglDto.getBizmc());
		fa_cardsDto.setsStyle(gdzcglDto.getGg());
		fa_cardsDto.setsSite(gdzcglDto.getSydd());
		fa_cardsDto.setDblTransInDeprTCard(gdzcglDto.getLjzj());
		fa_cardsDto.setlLife(gdzcglDto.getSynx());
		fa_cardsDto.setdStartdate(gdzcglDto.getKssyrq());
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String createdate = sdf.format(date);
		fa_cardsDto.setdInputDate(createdate);
		fa_cardsDto.setsOperator(gdzcglDto.getLrrymc());
		fa_cardsDto.setDblNetValueAct(gdzcglDto.getJz());
		fa_cardsDto.setlDeprMonthsAct(gdzcglDto.getYjtyf());
		fa_cardsDto.setDblValue(gdzcglDto.getYz());
		fa_cardsDto.setDblBV(gdzcglDto.getJcz());
		fa_cardsDto.setDblBVRate(gdzcglDto.getJczl());
		fa_cardsDto.setlUsedMonths(gdzcglDto.getYjtyf());
		fa_cardsDto.setbMultiDept("0");
		fa_cardsDto.setsDeptNames(gdzcglDto.getBmmc());
		fa_cardsDto.setsGroupNum(gdzcglDto.getZczdm());
		fa_cardsDto.setDblValueAndTax(gdzcglDto.getYz());
		int insert = fa_cardsDao.insert(fa_cardsDto);
		if(insert<=0){
			throw new BusinessException("msg","fa_cards表新增失败！");
		}
		Fa_Cards_DetailDto fa_cards_detailDto=new Fa_Cards_DetailDto();
		fa_cards_detailDto.setsCardID(fa_cardsDto.getsCardID());
		fa_cards_detailDto.setsCardNum(fa_cardsDto.getsCardNum());
		fa_cards_detailDto.setsDeptNum(fa_cardsDto.getsDeptNum());
		fa_cards_detailDto.setlOptID(fa_cardsDto.getlOptID());
		fa_cards_detailDto.setDblValue(fa_cardsDto.getDblValue());
		fa_cards_detailDto.setDblBV(fa_cardsDto.getDblBV());
		fa_cards_detailDto.setDblValueAndTax(fa_cardsDto.getDblValueAndTax());
		fa_cards_detailDto.setDblTransInDeprTCard(fa_cardsDto.getDblTransInDeprTCard());
		int insert1 = fa_cards_detailDao.insert(fa_cards_detailDto);
		if(insert1<=0){
			throw new BusinessException("msg","fa_cards_detail表新增失败！");
		}
		String[] split = createdate.split("-");
		Fa_DeprTransactions_DetailDto fa_deprTransactions_detailDto=new Fa_DeprTransactions_DetailDto();
		fa_deprTransactions_detailDto.setsCardNum(fa_cardsDto.getsCardNum());
		fa_deprTransactions_detailDto.setsDeptNum(fa_cardsDto.getsDeptNum());
		fa_deprTransactions_detailDto.setDblDeprT1(gdzcglDto.getLjzj());
		fa_deprTransactions_detailDto.setDblMonthValue2(gdzcglDto.getYz());
		fa_deprTransactions_detailDto.setDblMonthValue3(gdzcglDto.getYz());
		fa_deprTransactions_detailDto.setDblMonthValue4(gdzcglDto.getYz());
		fa_deprTransactions_detailDto.setDblMonthValue5(gdzcglDto.getYz());
		fa_deprTransactions_detailDto.setDblMonthValue6(gdzcglDto.getYz());
		fa_deprTransactions_detailDto.setDblMonthValue7(gdzcglDto.getYz());
		fa_deprTransactions_detailDto.setDblMonthValue8(gdzcglDto.getYz());
		fa_deprTransactions_detailDto.setDblMonthValue9(gdzcglDto.getYz());
		fa_deprTransactions_detailDto.setDblMonthValue10(gdzcglDto.getYz());
		fa_deprTransactions_detailDto.setDblMonthValue11(gdzcglDto.getYz());
		fa_deprTransactions_detailDto.setDblMonthValue12(gdzcglDto.getYz());
		fa_deprTransactions_detailDto.setDblMonthValue13(gdzcglDto.getYz());
		fa_deprTransactions_detailDto.setIyear(split[0]);
		int insert3 = fa_deprTransactions_detailDao.insert(fa_deprTransactions_detailDto);
		if(insert3<=0){
			throw new BusinessException("msg","fa_deprTransactions_detail表新增失败！");
		}
		Fa_DeptScaleDto fa_deptScaleDto=new Fa_DeptScaleDto();
		fa_deptScaleDto.setsCardNum(fa_cardsDto.getsCardNum());
		fa_deptScaleDto.setlOptID(fa_cardsDto.getlOptID());
		fa_deptScaleDto.setsDeptNum(fa_cardsDto.getsDeptNum());
		fa_deptScaleDto.setDblScale("1");
		fa_deptScaleDto.setsDeprSubjectNum(gdzcglDto.getDyzjkmdm());
		fa_deptScaleDto.setsDeprSubjectName(gdzcglDto.getDyzjkmmc());
		fa_deptScaleDto.setsProjectNum(gdzcglDto.getXmdm());
		fa_deptScaleDto.setsProjectName(gdzcglDto.getXmmc());
		fa_deptScaleDto.setcItemclsId(fa_cardsDto.getsTypeNum());
		int insert4 = fa_deptScaleDao.insert(fa_deptScaleDto);
		if(insert4<=0){
			throw new BusinessException("msg","fa_deptScale表新增失败！");
		}
		Fa_ItemsManualDto fa_itemsManualDto=new Fa_ItemsManualDto();
		fa_itemsManualDto.setsCardNum(fa_cardsDto.getsCardNum());
		fa_itemsManualDto.setMan1063s("0");
		int insert5 = fa_itemsManualDao.insert(fa_itemsManualDto);
		if(insert5<=0){
			throw new BusinessException("msg","fa_itemsManual表新增失败！");
		}
		int xh=0;
		Fa_ZWVouchersDto newFa_zwVouchersDto=new Fa_ZWVouchersDto();
		newFa_zwVouchersDto.setCoutaccset(accountName);
		newFa_zwVouchersDto.setIoutyear(split[0]);
		newFa_zwVouchersDto.setCoutsysname("FA");
		newFa_zwVouchersDto.setCoutsysver("8.1");
		newFa_zwVouchersDto.setDoutbilldate(createdate);
		newFa_zwVouchersDto.setIoutperiod(split[1]);
		newFa_zwVouchersDto.setCoutsign("卡片");
		newFa_zwVouchersDto.setCoutno_id(gdzcglDto.getKpbh());
		newFa_zwVouchersDto.setInid(String.valueOf(xh+1));
		xh=xh+1;
		newFa_zwVouchersDto.setDoutdate(createdate);
		newFa_zwVouchersDto.setCoutbillsign("新增资产");
		newFa_zwVouchersDto.setCbill(gdzcglDto.getLrrymc());
		newFa_zwVouchersDto.setCdigest(gdzcglDto.getZjfsmc()+"资产");
		newFa_zwVouchersDto.setCcode(gdzcglDto.getDyzjkmdm());
		newFa_zwVouchersDto.setMd(gdzcglDto.getYz());
		newFa_zwVouchersDto.setMc("0");
		newFa_zwVouchersDto.setCexch_name(gdzcglDto.getBizmc());
		int insert_t = fa_zwVouchersDao.insert(newFa_zwVouchersDto);
		if(insert_t<=0){
			throw new BusinessException("msg","fa_zwVouchers表新增失败！");
		}
		if(StringUtil.isNotBlank(gdzcglDto.getJczl())&&(!"0".equals(gdzcglDto.getJczl()))){
			Fa_ZWVouchersDto fa_zwVouchersDto=new Fa_ZWVouchersDto();
			fa_zwVouchersDto.setCoutaccset(accountName);
			fa_zwVouchersDto.setIoutyear(split[0]);
			fa_zwVouchersDto.setCoutsysname("FA");
			fa_zwVouchersDto.setCoutsysver("8.1");
			fa_zwVouchersDto.setDoutbilldate(createdate);
			fa_zwVouchersDto.setIoutperiod(split[1]);
			fa_zwVouchersDto.setCoutsign("卡片");
			fa_zwVouchersDto.setCoutno_id(gdzcglDto.getKpbh());
			fa_zwVouchersDto.setInid(String.valueOf(xh+1));
			xh=xh+1;
			fa_zwVouchersDto.setDoutdate(createdate);
			fa_zwVouchersDto.setCoutbillsign("新增资产");
			fa_zwVouchersDto.setCbill(gdzcglDto.getLrrymc());
			fa_zwVouchersDto.setCdigest(gdzcglDto.getZjfsmc()+"资产");
			fa_zwVouchersDto.setCcode(gdzcglDto.getDyzjkmdm());
			fa_zwVouchersDto.setMd("0");
			fa_zwVouchersDto.setMc(gdzcglDto.getJz());
			fa_zwVouchersDto.setCexch_name(gdzcglDto.getBizmc());
			int insert2 = fa_zwVouchersDao.insert(fa_zwVouchersDto);
			if(insert2<=0){
				throw new BusinessException("msg","fa_zwVouchers表新增失败！");
			}
		}
		if(StringUtil.isNotBlank(gdzcglDto.getLjzj())&&(!"0".equals(gdzcglDto.getLjzj()))){
			Fa_ZWVouchersDto fa_zwVouchersDto=new Fa_ZWVouchersDto();
			fa_zwVouchersDto.setCoutaccset(accountName);
			fa_zwVouchersDto.setIoutyear(split[0]);
			fa_zwVouchersDto.setCoutsysname("FA");
			fa_zwVouchersDto.setCoutsysver("8.1");
			fa_zwVouchersDto.setDoutbilldate(createdate);
			fa_zwVouchersDto.setIoutperiod(split[1]);
			fa_zwVouchersDto.setCoutsign("卡片");
			fa_zwVouchersDto.setCoutno_id(gdzcglDto.getKpbh());
			fa_zwVouchersDto.setInid(String.valueOf(xh+1));
			fa_zwVouchersDto.setDoutdate(createdate);
			fa_zwVouchersDto.setCoutbillsign("新增资产");
			fa_zwVouchersDto.setCbill(gdzcglDto.getLrrymc());
			fa_zwVouchersDto.setCdigest(gdzcglDto.getZjfsmc()+"资产");
			fa_zwVouchersDto.setCcode(gdzcglDto.getDyzjkmdm());
			fa_zwVouchersDto.setMd("0");
			fa_zwVouchersDto.setMc(gdzcglDto.getLjzj());
			fa_zwVouchersDto.setCexch_name(gdzcglDto.getBizmc());
			int insert2 = fa_zwVouchersDao.insert(fa_zwVouchersDto);
			if(insert2<=0){
				throw new BusinessException("msg","fa_zwVouchers表新增失败！");
			}
		}
		return true;
	}


	//新增客户管理同步四张表数据
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public boolean addCustomerAndCustomer_extradefineAndSa_invoicecustomersAndTc_tmp_duplication(KhglDto khglDto) throws BusinessException {
		//Customer表
		CustomerDto customerDto = new CustomerDto();
		customerDto.setcCusCode(khglDto.getKhdm());
		customerDto.setcCusName(khglDto.getKhmc());
		customerDto.setcCusAbbName(khglDto.getKhjc());
		customerDto.setcCCCode(khglDto.getSfdm());
		customerDto.setdCusDevDate(khglDto.getFzrq());
		customerDto.setcCusHeadCode(khglDto.getKhdm());
		customerDto.setcCreatePerson(khglDto.getLrrymc());
		customerDto.setiId(khglDto.getKhdm());
		customerDto.setcInvoiceCompany(khglDto.getKhdm());
		customerDto.setcCusExch_name(khglDto.getBizmc());
		customerDto.setcCusCreditCompany(khglDto.getKhdm());
		customerDto.setdCusCreateDatetime(khglDto.getLrsj());
		customerDto.setcCusMngTypeCode(khglDto.getKhgllxdm());

		int insert = customerDao.insert(customerDto);
		//CustomerAndCustomer_extradefine表
		Customer_extradefineDto customer_extradefineDto = new Customer_extradefineDto();
		customer_extradefineDto.setcCusCode(khglDto.getKhdm());
		int insert1 = customer_extradefineDao.insert(customer_extradefineDto);
		//Sa_invoicecustomers表
		Sa_invoicecustomersDto sa_invoicecustomersDto = new Sa_invoicecustomersDto();
		sa_invoicecustomersDto.setCcuscode(khglDto.getKhdm());
		sa_invoicecustomersDto.setCinvoicecompany(khglDto.getKhdm());
		int insert2 = sa_invoicecustomersDao.insert(sa_invoicecustomersDto);
		//Tc_tmp_duplication表
		Tc_tmp_duplicationDto tc_tmp_duplicationDto = new Tc_tmp_duplicationDto();
		tc_tmp_duplicationDto.setCode(khglDto.getKhdm());
		int insert3 = tc_tmp_duplicationDao.insert(tc_tmp_duplicationDto);

		if (insert==0||insert1==0||insert2==0||insert3==0){
			throw new BusinessException("msg","新增客户管理信息失败");
		}
		return true;
	}
	//修改客户管理同步四张表数据
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public boolean updateCustomerAndCustomer_extradefineAndSa_invoicecustomersAndTc_tmp_duplication(KhglDto khglDto) throws BusinessException {
		//Customer表
		CustomerDto customerDto = new CustomerDto();
		customerDto.setcCusCode(khglDto.getKhdm());
		customerDto.setcCusName(khglDto.getKhmc());
		customerDto.setcCusAbbName(khglDto.getKhjc());
		customerDto.setcCCCode(khglDto.getSfdm());
		customerDto.setdCusDevDate(khglDto.getFzrq());
		customerDto.setcCusHeadCode(khglDto.getKhdm());
		customerDto.setcModifyPerson(khglDto.getXgrymc());
		customerDto.setdModifyDate(khglDto.getXgsj());
		customerDto.setiId(khglDto.getKhdm());
		customerDto.setcInvoiceCompany(khglDto.getKhdm());
		customerDto.setcCusExch_name(khglDto.getBizmc());
		customerDto.setcCusCreditCompany(khglDto.getKhdm());
		customerDto.setcCusMngTypeCode(khglDto.getKhgllxdm());
		customerDto.setXgqkhdm(khglDto.getXgqkhdm());
		int update = customerDao.update(customerDto);
		// //CustomerAndCustomer_extradefine表
		// Customer_extradefineDto customer_extradefineDto = new Customer_extradefineDto();
		// customer_extradefineDto.setcCusCode(khglDto.getKhdm());
		// customer_extradefineDto.setXgqkhdm(khglDto.getXgqkhdm());
		// int update1 = customer_extradefineDao.update(customer_extradefineDto);
		// //Sa_invoicecustomers表
		// Sa_invoicecustomersDto sa_invoicecustomersDto = new Sa_invoicecustomersDto();
		// sa_invoicecustomersDto.setCcuscode(khglDto.getKhdm());
		// sa_invoicecustomersDto.setCinvoicecompany(khglDto.getKhdm());
		// sa_invoicecustomersDto.setXgqkhdm(khglDto.getXgqkhdm());
		// int update2 = sa_invoicecustomersDao.update(sa_invoicecustomersDto);
		// //Tc_tmp_duplication表
		// Tc_tmp_duplicationDto tc_tmp_duplicationDto = new Tc_tmp_duplicationDto();
		// tc_tmp_duplicationDto.setCode(khglDto.getKhdm());
		// tc_tmp_duplicationDto.setXgqkhdm(khglDto.getXgqkhdm());
		// int update3 = tc_tmp_duplicationDao.update(tc_tmp_duplicationDto);
		if (update==0){
			throw new BusinessException("msg","修改客户管理信息失败");
		}
		return true;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addPurchaseToU8ForWW(Map<String,Object> data,List<QgmxDto> qgmxlist,QgglDto qgglDto_t) throws BusinessException {
		Map<String,Object> map=new HashMap<>();
		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); //存入账套
		uA_IdentityDto.setcVouchType("PuApp");	//存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
		if(uA_IdentityDto_t!=null){
			if(StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				int id_t = id + 1000000001;
				uA_IdentityDto_t.setiFatherId(String.valueOf(id+1));
				data.put("ID",String.valueOf(id_t));
			}
		}else{
			data.put("ID","1000000001");
			uA_IdentityDto.setiFatherId("1");
		}

		PU_AppVouchDto addCgdToU8=pu_appvouchDao.insertPUAppVouchForWW(data);
		if(!StringUtils.isNotBlank(addCgdToU8.getID()))
			throw new BusinessException("ICOM99019","U8系统保存失败！");
		map.put("glid",addCgdToU8.getID());
		int serial = 0;
		if(uA_IdentityDto_t!=null) {
			serial=Integer.parseInt(uA_IdentityDto_t.getiChildId());
		}
		List<PU_AppVouchsDto> pu_appVouchsDtos = new ArrayList<>();
		for(int i=0;i<qgmxlist.size();i++) {
			PU_AppVouchsDto pu_appvouchsDto=new PU_AppVouchsDto();
			pu_appvouchsDto.setID(addCgdToU8.getID());
			pu_appvouchsDto.setcInvCode(qgmxlist.get(i).getWlbm());
			pu_appvouchsDto.setfQuantity(qgmxlist.get(i).getSl());
			if (StringUtil.isNotBlank(qgmxlist.get(i).getSl())&&StringUtil.isNotBlank(qgmxlist.get(i).getJg())){
				//单价
				BigDecimal dj = new BigDecimal(qgmxlist.get(i).getJg());
				BigDecimal suil = new BigDecimal("0.16");
				BigDecimal sl = new BigDecimal(qgmxlist.get(i).getSl());
				pu_appvouchsDto.setfUnitPrice(dj.toString());
				pu_appvouchsDto.setiOriCost(dj.toString());
				//单价*税率+单价h
				BigDecimal hsj = dj.multiply(suil).add(dj);
				String hsjStr = hsj.setScale(2, RoundingMode.HALF_UP).toString();
				pu_appvouchsDto.setfTaxPrice(hsjStr);
				pu_appvouchsDto.setiOriTaxCost(hsjStr);
				//(单价*税率+单价)*数量
				BigDecimal xsj = hsj.multiply(sl);
				String xsjStr = xsj.setScale(2, RoundingMode.HALF_UP).toString();
				pu_appvouchsDto.setfMoney(xsjStr);
				pu_appvouchsDto.setiOriSum(xsjStr);
				//单价*数量
				BigDecimal djcsl = dj.multiply(sl);
				pu_appvouchsDto.setiOriMoney(djcsl.setScale(2, RoundingMode.HALF_UP).toString());
				pu_appvouchsDto.setiMoney(djcsl.setScale(2, RoundingMode.HALF_UP).toString());
				//单价*税率*数量
				BigDecimal dsl = dj.multiply(suil).multiply(sl);
				String dslStr = dsl.setScale(2, RoundingMode.HALF_UP).toString();
				pu_appvouchsDto.setiOriTaxPrice(dslStr);
				pu_appvouchsDto.setiTaxPrice(dslStr);
			}
			pu_appvouchsDto.setdRequirDate(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));//制单日期
			pu_appvouchsDto.setdArriveDate(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));//审核日期
			pu_appvouchsDto.setiPerTaxRate("16.0000");//税率
			pu_appvouchsDto.setcItem_class(qgglDto_t.getXmdldm());
			pu_appvouchsDto.setcItemCode(qgglDto_t.getXmbmdm());
			pu_appvouchsDto.setCItemName(qgglDto_t.getXmbmmc());
			pu_appvouchsDto.setCexch_name("人民币");
			pu_appvouchsDto.setCbsysbarcode("||ompr|"+qgglDto_t.getDjh()+"|"+(i+1));
			//U8cbMemo字段长度为255，防止过长报错
			if(StringUtils.isNotBlank(qgmxlist.get(i).getBz())){
				if(qgmxlist.get(i).getBz().length()>255){
					pu_appvouchsDto.setCbMemo(qgmxlist.get(i).getBz().substring(0,255));
				}else{
					pu_appvouchsDto.setCbMemo(qgmxlist.get(i).getBz());
				}
			}
			pu_appvouchsDto.setIvouchrowno(String.valueOf(i+1));
			int id = serial + 1000000001;
			pu_appvouchsDto.setAutoID(String.valueOf(id));
			serial=serial+1;
			qgmxlist.get(i).setGlid(pu_appvouchsDto.getAutoID());
			qgmxlist.get(i).setPrefixFlg(prefixFlg);
			map.put("qgmxlist",qgmxlist);
			pu_appVouchsDtos.add(pu_appvouchsDto);
		}
		// 更新U8入库明细表
		//若List过大进行截断,10个明细为一组进行添加
		int result_pus;
		List<List<PU_AppVouchsDto>> pu_appVouchsDtosList = Lists.partition(pu_appVouchsDtos, 10);
		for (List<PU_AppVouchsDto> puAppVouchsDtos : pu_appVouchsDtosList) {
			result_pus = pu_appvouchDao.insertPUAppVouchsListForWW(puAppVouchsDtos);
			if (result_pus < 1)
				throw new BusinessException("msg", "U8系统保存委外请购明细失败！");
		}
		// 更新最大值表
		boolean result_ui = false;
		//判断是新增还是修改
		if(uA_IdentityDto_t!=null) {
			//更新
			uA_IdentityDto_t.setiChildId(String.valueOf(serial));
			result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
		}
		if(!result_ui) {
			uA_IdentityDto.setiChildId(String.valueOf(serial));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}
		return map;
	}
	/*
	 * 合同审核通过同步u8
	 * */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8ContractData(HtglDto htglDto_t, List<HtmxDto> htmxDtos,QgglDto qgglDto_h, User operator,HtglDto htglDto) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		PO_PomainDto pO_PomainDto = new PO_PomainDto();
		pO_PomainDto.setcVenCode(htglDto_t.getGysdm()); // 供应商代码
		pO_PomainDto.setcDepCode(htglDto_t.getJqdm()); // 申请部门代码
		pO_PomainDto.setcPTCode("01"); // 单据类型?
		pO_PomainDto.setCexch_name(htglDto_t.getBizmc()); // 币种
		pO_PomainDto.setNflat("1"); // 1
		pO_PomainDto.setiTaxRate(htglDto_t.getSl()); // 税率
		pO_PomainDto.setiCost("0"); // 0
		pO_PomainDto.setiBargain("0"); // 0
		pO_PomainDto.setcMemo(htglDto_t.getBz()); // 备注?
		pO_PomainDto.setcState("1"); // 1
		pO_PomainDto.setiVTid("8173"); // 8173
		pO_PomainDto.setiDiscountTaxType("0"); // 0
		pO_PomainDto.setIverifystateex("2"); // 2
		pO_PomainDto.setIsWfControlled("0"); // 0
		pO_PomainDto.setCmaketime(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss")); // 制单时间
		pO_PomainDto.setIflowid("0");
		pO_PomainDto.setiPrintCount("0");

		String djh = qgglDto_h.getDjh();
		pO_PomainDto.setcDefine1(djh);
		pO_PomainDto.setCappcode(djh); // 记录编号
		pO_PomainDto.setcPOID(htglDto_t.getHtnbbh()); // cpoid = cjrq+serial
		pO_PomainDto.setCsysbarcode("||pupo|" + htglDto_t.getHtnbbh());
		pO_PomainDto.setcMaker(htglDto_t.getFzrmc()); // 制单人
		pO_PomainDto.setcVerifier(htglDto_t.getFzrmc()); // 审核人
		List<PU_AppVouchsDto> PU_AppVouchsDtos = new ArrayList<>();
		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); //存入账套
		uA_IdentityDto.setcVouchType("Pomain");    //存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
		if (uA_IdentityDto_t != null) {
			if (StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				// 不为空，设关联ID POID为最大值+1
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				int id_s = id + 1;
				uA_IdentityDto_t.setiFatherId(String.valueOf(id_s));
				pO_PomainDto.setPOID(id + 1000000001);
			}
		} else {
			// 为空，设关联ID POID为1
			pO_PomainDto.setPOID(1000000001);
			uA_IdentityDto.setiFatherId("1");
		}
		pO_PomainDto.setdPODate(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
		pO_PomainDto.setcBusType("普通采购"); // 采购方式
		pO_PomainDto.setCmaketime(htglDto_t.getSqrq());
		pO_PomainDto.setcAuditTime(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
		pO_PomainDto.setcAuditDate(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
		int intPomain = pO_PomainDao.insert(pO_PomainDto);
		if (intPomain < 1) {
			log.error("添加失败，返回主键ID:" + pO_PomainDto.getcPOID());
			throw new BusinessException("msg", "添加失败，返回主键ID:" + pO_PomainDto.getcPOID());
		} else {
			int serial = 0;
			if (uA_IdentityDto_t != null) {
				serial = Integer.parseInt(uA_IdentityDto_t.getiChildId());
			}
			htglDto.setU8cgid(pO_PomainDto.getcPOID());
			htglDto.setU8poid("" + pO_PomainDto.getPOID());
			PO_PodetailsDto pO_PodetailsDto = new PO_PodetailsDto();
			for (HtmxDto htmxDto : htmxDtos) {
				if ("MATERIAL".equals(htmxDto.getQglbdm()) || org.apache.commons.lang.StringUtils.isBlank(htmxDto.getQglbdm()) || "DEVICE".equals(htmxDto.getQglbdm())) {
					pO_PodetailsDto.setID(serial + 1000000001);
					serial = serial + 1;
					pO_PodetailsDto.setcInvCode(htmxDto.getWlbm()); // 物料编码
					pO_PodetailsDto.setiQuantity(htmxDto.getSl()); // 数量
					pO_PodetailsDto.setiUnitPrice(htmxDto.getWsdj());// 原币单价
					// 无税总金额 = 含税总金额 /(1+税率/100)
					BigDecimal hjje = new BigDecimal(htmxDto.getHjje());
					BigDecimal suil = new BigDecimal(htglDto_t.getSl()).divide(new BigDecimal(100), 5,
							RoundingMode.HALF_UP);
					BigDecimal wszje = hjje.divide(suil.add(new BigDecimal("1")), 2, RoundingMode.HALF_UP);
					pO_PodetailsDto.setiMoney(String.valueOf(wszje)); // 无税总额
					pO_PodetailsDto.setiNatMoney(String.valueOf(wszje)); // 无税总额

					// 计算税额
					BigDecimal se = hjje.subtract(wszje);
					pO_PodetailsDto.setiNatTax(se.toString()); // 原币税额
					pO_PodetailsDto.setiNatSum(htmxDto.getHjje()); // 原币价税总额
					pO_PodetailsDto.setiNatUnitPrice(htmxDto.getWsdj()); // 原币单价
					pO_PodetailsDto.setiTax(se.toString()); // 原币税额
					pO_PodetailsDto.setiSum(htmxDto.getHjje()); // 原币价税总额
					pO_PodetailsDto.setPOID(pO_PomainDto.getPOID()); // 关联id
					pO_PodetailsDto.setdArriveDate(htmxDto.getJhdhrq()); // 计划到货日期
					pO_PodetailsDto.setiPerTaxRate(htmxDto.getSuil()); // 税率
					pO_PodetailsDto.setcItemCode(htmxDto.getXmcsdm()); // 010002
					pO_PodetailsDto.setcItem_class(htmxDto.getFcsdm()); // 00SoType
					pO_PodetailsDto.setSoType("0");
					pO_PodetailsDto.setcItemName(htmxDto.getXmcsmc()); // MAR002
					pO_PodetailsDto.setiTaxPrice(htmxDto.getHsdj()); // 税额bTaxCost
					pO_PodetailsDto.setbTaxCost("1");
					pO_PodetailsDto.setiAppIds(htmxDto.getGlid()); // 关联请购单详细里的AutoID(glid)
					pO_PodetailsDto.setcSource("app"); // app
					pO_PodetailsDto.setCupsocode(htmxDto.getDjh()); // 记录编号
					pO_PodetailsDto.setIordertype("0"); // 0iBG_Ctrl
					pO_PodetailsDto.setiBG_Ctrl(null);
					int numXh = pO_PodetailsDao.getXhMax(pO_PodetailsDto);
					int ivo = numXh + 1;
					// 不为空，设序号为最大值+1
					pO_PodetailsDto.setIvouchrowno(String.valueOf(ivo));
					pO_PodetailsDto.setCbMemo(htmxDto.getBz()); // 备注
					pO_PodetailsDto.setCbsysbarcode(
							pO_PomainDto.getCsysbarcode() + "|" + pO_PodetailsDto.getIvouchrowno()); // 系统用
					pO_PodetailsDto.setBgift("0"); // 0
					int intPodetails = pO_PodetailsDao.insert(pO_PodetailsDto);
					if (intPodetails < 1) {
						log.error("添加失败，返回主键ID:" + pO_PodetailsDto.getID());
						throw new BusinessException("msg", "添加失败，返回主键ID:" + pO_PodetailsDto.getID());
					} else {
						int U8mxid = pO_PodetailsDto.getID();
						htmxDto.setU8mxid(String.valueOf(U8mxid));
						htmxDto.setXgry(operator.getZsxm());
					}
					// 存入要更新的U8的请购明细
					if (StringUtil.isNotBlank(htmxDto.getQgmxid())) {
						PU_AppVouchsDto pU_AppVouchsDto = new PU_AppVouchsDto();
						pU_AppVouchsDto.setAutoID(htmxDto.getGlid());
						pU_AppVouchsDto.setiReceivedQTY(htmxDto.getSl());
						PU_AppVouchsDtos.add(pU_AppVouchsDto);
					}
				}
			}
			// 更新最大值表
			boolean result_ui = false;
			//判断是新增还是修改
			if (uA_IdentityDto_t != null) {
				//更新
				uA_IdentityDto_t.setiChildId(String.valueOf(serial));
				result_ui = uA_IdentityDao.update(uA_IdentityDto_t) > 0;
			}
			if (!result_ui) {
				uA_IdentityDto.setiChildId(String.valueOf(serial));
				result_ui = uA_IdentityDao.insert(uA_IdentityDto) > 0;
			}
			if (!result_ui) {
				throw new BusinessException("msg", "最大id值更新失败!");
			}
			List<List<PU_AppVouchsDto>> partition = Lists.partition(PU_AppVouchsDtos, 10);
			for (List<PU_AppVouchsDto> pu_appVouchsDtos : partition) {
				int i = PU_AppVouchDao.updateIReceivedQTY(pu_appVouchsDtos);
				if(i<1){
					throw new BusinessException("msg", "更新U8请购合同引用数量失败！");
				}
			}
			map.put("htmxDtos",htmxDtos);
			map.put("htglDto",htglDto);
			return map;
		}
	}
	/*
	 * 委外合同审核通过同步u8
	 * */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8ContractDataForWW(HtglDto htglDto_t, List<HtmxDto> htmxDtos, QgglDto qgglDto_h, User operator, HtglDto htglDto) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		OM_MOMainDto om_moMainDto = new OM_MOMainDto();
		String htnbbh = htglDto_t.getHtnbbh();
		om_moMainDto.setcCode(htnbbh); // 合同单号
		om_moMainDto.setdDate(htglDto_t.getCjrq());//合同日期
		om_moMainDto.setcVenCode(htglDto_t.getGysdm()); // 供应商代码
		om_moMainDto.setcDepCode(htglDto_t.getJgdm());//部门代码
		om_moMainDto.setcPTCode("1");//采购类型代码
		om_moMainDto.setCexch_name(htglDto_t.getBizmc()); // 币种名称
		om_moMainDto.setNflat(htglDto_t.getHl()); // 汇率
		om_moMainDto.setiTaxRate(htglDto_t.getSl()); // 税率
		om_moMainDto.setcMemo(htglDto_t.getBz()); // 备注

		om_moMainDto.setcMaker(htglDto_t.getFzrmc()); // 制单人
		om_moMainDto.setcVerifier(htglDto_t.getFzrmc()); // 审核人
		String jlbh = qgglDto_h.getJlbh();
		om_moMainDto.setcDefine1(jlbh);//记录编号
		om_moMainDto.setiVTid("8157"); // 模板号
		om_moMainDto.setcBusType("委外加工");
		om_moMainDto.setcLocker(htglDto_t.getFzrmc());//审核人
		om_moMainDto.setdCreateTime(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));//创建时间
		om_moMainDto.setdVerifyDate(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));//审核日期
		om_moMainDto.setdVerifyTime(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));//审核时间
		String djh =  qgglDto_h.getDjh();
		om_moMainDto.setCsrccode(djh);//请购单号
		om_moMainDto.setCsysbarcode("||omom|" +htnbbh);//||omom|合同单号

		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); //存入账套
		uA_IdentityDto.setcVouchType("OM_MO");    //存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); //获取主键最大值
		if (uA_IdentityDto_t != null) {
			if (StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				// 不为空，设关联ID MOID为最大值+1
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				int id_s = id + 1;
				uA_IdentityDto_t.setiFatherId(String.valueOf(id_s));
				om_moMainDto.setMOID(id + 1000000001 + "");
			}
		} else {
			// 为空，设关联ID MOID为1
			om_moMainDto.setMOID(1000000001 + "");
			uA_IdentityDto.setiFatherId("1");
		}
		int intMoMain = om_moMainDao.insert(om_moMainDto);
		if (intMoMain < 1) {
			log.error("添加失败，返回主键ID:" + om_moMainDto.getMOID());
			throw new BusinessException("msg", "添加失败，返回主键ID:" + om_moMainDto.getMOID());
		} else {
			int serial = 0;
			if (uA_IdentityDto_t != null) {
				serial = Integer.parseInt(uA_IdentityDto_t.getiChildId());
			}
			htglDto.setU8cgid(qgglDto_h.getDjh());
			htglDto.setU8omid(om_moMainDto.getMOID());
			List<PU_AppVouchsDto> PU_AppVouchsDtos = new ArrayList<>();
			List<OM_MODetailsDto> om_moDetailsDtos = new ArrayList<>();
			int iVouchRowNo = 1;
			int bomLsh = 1;
			int serialM = 0;
			UA_IdentityDto ua_identityDto_MA = new UA_IdentityDto();
			ua_identityDto_MA.setcAcc_Id(accountName); //存入账套
			ua_identityDto_MA.setcVouchType("OM_Materials");    //存入表标识
			UA_IdentityDto uA_IdentityDto_MAT = uA_IdentityDao.getMax(ua_identityDto_MA); //获取主键最大值
			if (uA_IdentityDto_MAT != null) {
				serialM = Integer.parseInt(uA_IdentityDto_MAT.getiChildId());
			}
			int id = 1;
			if (uA_IdentityDto_MAT != null) {
				if (StringUtil.isNotBlank(uA_IdentityDto_MAT.getiFatherId())) {
					id = Integer.parseInt(uA_IdentityDto_MAT.getiFatherId());
					id = id + 1;
					uA_IdentityDto_MAT.setiFatherId(String.valueOf(id));
				}
			} else {
				ua_identityDto_MA.setiFatherId(String.valueOf(id));
			}
			id = id-1;
			List<OM_MOMaterialsDto> OM_MOMaterialsDtos = new ArrayList<>();
			for (HtmxDto htmxDto : htmxDtos)
				if ("OUTSOURCE".equals(htmxDto.getQglbdm()) || StringUtils.isBlank(htmxDto.getQglbdm())) {
					OM_MODetailsDto om_moDetailsDto = new OM_MODetailsDto();
					om_moDetailsDto.setMODetailsID(serial + 1000000001 + "");//MODetailsID
					om_moDetailsDto.setMOID(om_moMainDto.getMOID());//关联主表id
					serial = serial + 1;
					om_moDetailsDto.setcInvCode(htmxDto.getWlbm()); // 物料编码
					// 数量Str
					String slStr = htmxDto.getSl();
					om_moDetailsDto.setiQuantity(slStr);
					// 原币单价Str
					String wsdjStr = htmxDto.getWsdj();
					om_moDetailsDto.setiUnitPrice(wsdjStr);

					String hsdjStr = htmxDto.getHsdj();//含税单价
					String suilStr = htmxDto.getSuil();//税率

					if (StringUtil.isNotBlank(slStr) && StringUtil.isNotBlank(wsdjStr)) {
						BigDecimal sl = new BigDecimal(slStr);// 数量
						BigDecimal wsdj = new BigDecimal(wsdjStr);// 原币单价
						BigDecimal suil = new BigDecimal(suilStr);//税率
						suil = suil.divide(new BigDecimal("100"),2, RoundingMode.HALF_UP);
						BigDecimal hsdj = new BigDecimal(hsdjStr);//含税单价
						BigDecimal ze = wsdj.multiply(sl);//原币单价*数量

						String zeStr = ze.setScale(2, RoundingMode.HALF_UP).toString();
						om_moDetailsDto.setiMoney(zeStr);//原币单价*数量

						BigDecimal hsze = ze.multiply(suil);
						String hszeStr = hsze.setScale(2, RoundingMode.HALF_UP).toString();
						om_moDetailsDto.setiTax(hszeStr);//原币单价×税率×数量

						BigDecimal xse = wsdj.multiply(suil).add(wsdj);
						BigDecimal xsze = xse.multiply(sl);
						String xszeStr = xsze.setScale(2, RoundingMode.HALF_UP).toString();
						om_moDetailsDto.setiSum(xszeStr);//（原币单价×税率+原币单价）×数量

						om_moDetailsDto.setiNatUnitPrice(wsdjStr);// 原币单价Str
						om_moDetailsDto.setiNatMoney(zeStr);//原币单价*数量
						om_moDetailsDto.setiNatTax(hszeStr);//原币单价×税率×数量
						om_moDetailsDto.setiNatSum(xszeStr);//（原币单价×税率+原币单价）×数量
						if (StringUtil.isNotBlank(hsdjStr)) {
							BigDecimal iTaxprice = hsdj.multiply(suil).add(hsdj);
							String iTaxpriceStr = iTaxprice.setScale(2, RoundingMode.HALF_UP).toString();
							om_moDetailsDto.setiTaxPrice(iTaxpriceStr);//（含税单价×税率+含税单价）
						}
					}
					om_moDetailsDto.setdStartDate(htglDto_t.getCjrq());//创建日期
					om_moDetailsDto.setdArriveDate(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));//审核日期
					om_moDetailsDto.setiPerTaxRate(suilStr);//税率
					om_moDetailsDto.setcItemCode(qgglDto_h.getXmbmdm());//项目编码代码
					om_moDetailsDto.setcItem_class(qgglDto_h.getXmdldm());//项目大类代码
					om_moDetailsDto.setcItemName(qgglDto_h.getXmbmmc());//项目编码名称


					List<HtmxDto> htmxDto_cpjgs = htmxService.getCpjgxxs(htmxDto);
					if(!CollectionUtils.isEmpty(htmxDto_cpjgs)){				
						om_moDetailsDto.setBomID(htmxDto_cpjgs.get(0).getU8cpjgid());//关联产品结构主表id
						om_moDetailsDto.setfParentScrp(htmxDto_cpjgs.get(0).getMjshl());//母件损耗率
						om_moDetailsDto.setBomType(htmxDto_cpjgs.get(0).getBomlbdm()); // bom类型						
					}
					om_moDetailsDto.setiVTids("8159");//模板号8159
					om_moDetailsDto.setCupsocode(djh);//请购单号
					om_moDetailsDto.setCupsoids(htmxDto.getGlid());//请购单明细id
					om_moDetailsDto.setiVouchRowNo(iVouchRowNo+"");
					iVouchRowNo++;
					om_moDetailsDto.setCbMemo(htmxDto.getBz()); // 备注
					om_moDetailsDto.setCbsysbarcode(
							om_moMainDto.getCsysbarcode() + "|" + om_moDetailsDto.getiVouchRowNo()); //||ommo|合同单号|流水号
					om_moDetailsDto.setImrpqty(htmxDto.getSl());//数量
					om_moDetailsDtos.add(om_moDetailsDto);

					int U8mxid = Integer.parseInt(om_moDetailsDto.getMODetailsID());
					htmxDto.setU8ommxid(String.valueOf(U8mxid));
					htmxDto.setXgry(operator.getZsxm());

                    // // 存入要更新的U8的请购明细
                    if (StringUtil.isNotBlank(htmxDto.getQgmxid())) {
                        PU_AppVouchsDto pU_AppVouchsDto = new PU_AppVouchsDto();
                        pU_AppVouchsDto.setAutoID(htmxDto.getGlid());
                        pU_AppVouchsDto.setiReceivedQTY(htmxDto.getSl());
                        pU_AppVouchsDto.setiReceivedNum("0.000000");
						PU_AppVouchsDtos.add(pU_AppVouchsDto);
                    }
					id++;
					if(!CollectionUtils.isEmpty(htmxDto_cpjgs)){
						for (HtmxDto htmxDto_cpjg : htmxDto_cpjgs) {
							OM_MOMaterialsDto om_moMaterialsDto = new OM_MOMaterialsDto();
							//OM_MOMaterials
							om_moMaterialsDto.setMOMaterialsID(serialM + 1000000001 + "");//根据uA_Identity表维护 OM_Materials 拿ichidID
							serialM = serialM + 1;
							om_moMaterialsDto.setMoDetailsID(om_moDetailsDto.getMODetailsID());//关联OM_MODetails ID
							om_moMaterialsDto.setMOID(om_moMainDto.getMOID());//关联订单主表id
							om_moMaterialsDto.setcInvCode(htmxDto_cpjg.getZjwlbm());//物料编码
							BigDecimal sl = new BigDecimal(htmxDto.getSl());
							BigDecimal cpjgsysl = new BigDecimal(htmxDto_cpjg.getCpsysl());
							BigDecimal iQuantity = sl.multiply(cpjgsysl);
							String iQuantityStr = iQuantity.setScale(2, RoundingMode.HALF_UP).toString();
							om_moMaterialsDto.setiQuantity(iQuantityStr);//合同明细数量*产品结构使用数量
							om_moMaterialsDto.setdRequiredDate(htmxDto.getJhdhrq());//计划到货日期
							om_moMaterialsDto.setfBaseQtyN(htmxDto_cpjg.getJbyl());//基本用量
							om_moMaterialsDto.setfBaseQtyD(htmxDto_cpjg.getJcsl());//基础数量
							om_moMaterialsDto.setfCompScrp(htmxDto_cpjg.getZjshl());//子件损耗率
							om_moMaterialsDto.setiUnitQuantity(htmxDto_cpjg.getCpsysl());//使用数量
							om_moMaterialsDto.setOpComponentId(htmxDto_cpjg.getU8cpjgmxid());//关联bom_opcomponent表主键id`
							om_moMaterialsDto.setCbMemo(htmxDto.getBz());//备注
							om_moMaterialsDto.setCsubsysbarcode(om_moMainDto.getCsysbarcode() + "|" + htmxDto.getXh() + "|" + bomLsh);//||ommo|单号|合同明细流水|bom表流水
							bomLsh++;
							OM_MOMaterialsDtos.add(om_moMaterialsDto);
						}
					}
				}
			int intOm_moDetails = om_moDetailsDao.insertList(om_moDetailsDtos);
			if (intOm_moDetails < 1) {
				log.error("合同审核同步U8失败");
				throw new BusinessException("msg", "合同审核同步U8失败");
			}
			int intOm_moMaterials = om_moMaterialsDao.insertList(OM_MOMaterialsDtos);
			if (intOm_moMaterials < 1) {
				log.error("合同审核同步U8失败");
				throw new BusinessException("msg", "合同审核同步U8失败");
			}
			// 更新最大值表	OM_MO
			boolean result_OM_MO = false;
			//判断是新增还是修改
			if (uA_IdentityDto_t != null) {
				//更新
				uA_IdentityDto_t.setiChildId(String.valueOf(serial));
				result_OM_MO = uA_IdentityDao.update(uA_IdentityDto_t) > 0;
			}
			if (!result_OM_MO) {
				uA_IdentityDto.setiChildId(String.valueOf(serial));
				result_OM_MO = uA_IdentityDao.insert(uA_IdentityDto) > 0;
			}
			if (!result_OM_MO) {
				throw new BusinessException("msg", "最大id值更新失败!");
			}

			// 更新最大值表 OM_MOMaterials
			boolean result_OM_MOMaterials = false;
			//判断是新增还是修改
			if (uA_IdentityDto_MAT != null) {
				//更新
				uA_IdentityDto_MAT.setiFatherId(String.valueOf(id));
				uA_IdentityDto_MAT.setiChildId(String.valueOf(serialM));
				result_OM_MOMaterials = uA_IdentityDao.update(uA_IdentityDto_MAT) > 0;
			}
			if (!result_OM_MOMaterials) {
				ua_identityDto_MA.setiFatherId(String.valueOf(id));
				ua_identityDto_MA.setiChildId(String.valueOf(serialM));
				result_OM_MOMaterials = uA_IdentityDao.insert(ua_identityDto_MA) > 0;
			}
			if (!result_OM_MOMaterials) {
				throw new BusinessException("msg", "最大id值更新失败!");
			}
			// 更新U8请购表合同引用
			//若List过大进行截断,10个明细为一组进行添加
			int result_PU_App;
			if(PU_AppVouchsDtos.size()>10) {
				int length = (int)Math.ceil((double)PU_AppVouchsDtos.size() / 10);//向上取整
				for (int i = 0; i < length; i++) {
					List<PU_AppVouchsDto> list = new ArrayList<>();
					int t_length;
					if (i != length - 1) {
						t_length = (i + 1) * 10;
					} else {
						t_length = PU_AppVouchsDtos.size();
					}
					for (int j = i * 10; j < t_length; j++) {
						list.add(PU_AppVouchsDtos.get(j));
					}
					result_PU_App = PU_AppVouchDao.updateIReceivedQTYAndIReceivedNum(list);
					if (result_PU_App < 1)
						throw new BusinessException("msg", "更新U8请购合同引用数量失败！");
				}
			}else{
				if(PU_AppVouchsDtos.size()>0){
					result_PU_App = PU_AppVouchDao.updateIReceivedQTYAndIReceivedNum(PU_AppVouchsDtos);
					if (result_PU_App < 1)
						throw new BusinessException("msg", "更新U8请购合同引用数量失败！");
				}
			}
			map.put("htmxDtos", htmxDtos);
			map.put("htglDto", htglDto);
			return map;
		}
	}

	/**
	 * 新增U8 报销数据
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class,transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8Reimburse(DdbxglDto ddbxglDto,List<DdbxmxDto> list) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat year = new SimpleDateFormat("yyyy");
		SimpleDateFormat month = new SimpleDateFormat("yyyyMM");
		SimpleDateFormat allsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String, Object> map = new HashMap<>();
		//差旅费
		List<GL_accvouchDto> gl_accvouchDtos=new ArrayList<>();
		StringBuilder ids= new StringBuilder();
		//获取当前月份
		Calendar calendar = Calendar.getInstance();
		int month_t = calendar.get(Calendar.MONTH) + 1;
		GL_CashTableDto GL_CashTableDto = new GL_CashTableDto();
		GL_CashTableDto.setiPeriod(String.valueOf(month_t));
		GL_CashTableDto.setIyear(year.format(new Date()));
		String id=gl_cashTableDao.getMaxId(GL_CashTableDto);
		int xh=0;
		String kmbm="";
		String dfkmbm="";
		String meetingType = "";
		if("PROC-DFCCB981-37A5-4C25-80A6-B78B7F495930".equals(ddbxglDto.getBxlxcskz3())){
			kmbm=ddbxglDto.getBxlxcskz1();
			dfkmbm=ddbxglDto.getBxlxcskz2();
		}else if("PROC-F933F40F-0524-4767-9349-3DE4F1C5E01E".equals(ddbxglDto.getBxlxcskz3())){
			meetingType = "报销会议费";
			List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MEETING_FEE_EXPENSE_SUBJECT.getCode());//会议费报销科目
			if(!CollectionUtils.isEmpty(jcsjDtos)){
				for(JcsjDto dto:jcsjDtos){
					if(ddbxglDto.getBmkzcs1().equals(dto.getCsdm())){
						kmbm=dto.getCskz1();
						dfkmbm=dto.getCskz2();
						break;
					}
				}
			}
		}
		for(int i=0;i<list.size();i++){
			GL_accvouchDto gl_accvouchDto=new GL_accvouchDto();
			String cashItem = "07";
			gl_accvouchDto.setIperiod(String.valueOf(month_t));//当前月份
			String csign="付";
			String Isignseq="2";
			if(ddbxglDto.getSsgs().equals("杰毅生物")){
				Isignseq="5";
				csign="银付";
				cashItem = "04";
			}
			gl_accvouchDto.setCsign(csign);
			gl_accvouchDto.setIsignseq(Isignseq);
			gl_accvouchDto.setIno_id(id);
			xh+=1;
			gl_accvouchDto.setInid(String.valueOf(xh));
			gl_accvouchDto.setDbill_date(sdf.format(new Date()));
			gl_accvouchDto.setIdoc("-1");
			gl_accvouchDto.setCbill(ddbxglDto.getSqrmc());
			gl_accvouchDto.setIbook("0");
			gl_accvouchDto.setCcashier(null);
			gl_accvouchDto.setCdigest(ddbxglDto.getSqrmc()+meetingType);
			gl_accvouchDto.setCcode(kmbm);
			gl_accvouchDto.setMd(list.get(i).getJe());
			gl_accvouchDto.setMc("0");
			gl_accvouchDto.setMd_f("0");
			gl_accvouchDto.setMc_f("0");
			gl_accvouchDto.setNfrat("0");
			gl_accvouchDto.setNd_s("0");
			gl_accvouchDto.setNc_s("0");
			gl_accvouchDto.setDt_date(allsdf.format(new Date()));
			if(xh==1){
				gl_accvouchDto.setCdept_id(ddbxglDto.getJgdm());
				gl_accvouchDto.setCperson_id(ddbxglDto.getU8yhid());
				gl_accvouchDto.setcDefine1(ddbxglDto.getSsdq());
			}
			gl_accvouchDto.setCcode_equal(dfkmbm);
			gl_accvouchDto.setBdelete("0");
			gl_accvouchDto.setDoutbilldate(sdf.format(new Date()));
//			gl_accvouchDto.setCoutno_id("daiding");//暂定
			gl_accvouchDto.setBvouchedit("1");
			gl_accvouchDto.setBvouchAddordele("0");
			gl_accvouchDto.setBvouchmoneyhold("0");
			gl_accvouchDto.setBvalueedit("1");
			gl_accvouchDto.setBcodeedit("1");
			gl_accvouchDto.setbPCSedit("1");
			gl_accvouchDto.setbDeptedit("1");
			gl_accvouchDto.setbItemedit("1");
			gl_accvouchDto.setbCusSupInput("0");
			gl_accvouchDto.setbFlagOut("0");
//			gl_accvouchDto.setRowGuid("daiding");//暂定
			gl_accvouchDto.setIyear(year.format(new Date()));
			gl_accvouchDto.setiYPeriod(month.format(new Date()));
			gl_accvouchDto.setTvouchtime(allsdf.format(new Date()));
			gl_accvouchDto.setCcodeexch_equal(dfkmbm);
//			gl_accvouchDto.setCpzchcode("daiding");//暂定
			gl_accvouchDto.setIprice("0");
			gl_accvouchDto.setCErrReason("");
			gl_accvouchDtos.add(gl_accvouchDto);
			GL_accvouchDto gl_accvouchDto_t=new GL_accvouchDto();
			gl_accvouchDto_t.setIperiod(String.valueOf(month_t));
			gl_accvouchDto_t.setCsign(csign);
			gl_accvouchDto_t.setIsignseq(Isignseq);
			gl_accvouchDto_t.setIno_id(id);
			xh+=1;
			gl_accvouchDto_t.setInid(String.valueOf(xh));
			gl_accvouchDto_t.setDbill_date(sdf.format(new Date()));
			gl_accvouchDto_t.setIdoc("-1");
			gl_accvouchDto_t.setDt_date(allsdf.format(new Date()));
			gl_accvouchDto_t.setCbill(ddbxglDto.getSqrmc());
			gl_accvouchDto_t.setIbook("0");
			gl_accvouchDto_t.setCcashier(null);
			gl_accvouchDto_t.setCdigest(ddbxglDto.getSqrmc()+meetingType);
			gl_accvouchDto_t.setCcode(dfkmbm);
			gl_accvouchDto_t.setMd("0");
			gl_accvouchDto_t.setMc(list.get(i).getJe());
			gl_accvouchDto_t.setMd_f("0");
			gl_accvouchDto_t.setMc_f("0");
			gl_accvouchDto_t.setNfrat("0");
			gl_accvouchDto_t.setNd_s("0");
			gl_accvouchDto_t.setNc_s("0");
			gl_accvouchDto_t.setCcode_equal(kmbm);
			gl_accvouchDto_t.setBdelete("0");
			gl_accvouchDto_t.setDoutbilldate(sdf.format(new Date()));
//			gl_accvouchDto_t.setCoutno_id("daiding");//暂定
			gl_accvouchDto_t.setBvouchedit("1");
			gl_accvouchDto_t.setBvouchAddordele("0");
			gl_accvouchDto_t.setBvouchmoneyhold("0");
			gl_accvouchDto_t.setBvalueedit("1");
			gl_accvouchDto_t.setBcodeedit("1");
			gl_accvouchDto_t.setbPCSedit("1");
			gl_accvouchDto_t.setbDeptedit("1");
			gl_accvouchDto_t.setbItemedit("1");
			gl_accvouchDto_t.setbCusSupInput("0");
			gl_accvouchDto_t.setbFlagOut("0");
//			gl_accvouchDto_t.setRowGuid("daiding");//暂定
			gl_accvouchDto_t.setIyear(year.format(new Date()));
			gl_accvouchDto_t.setiYPeriod(month.format(new Date()));
			gl_accvouchDto_t.setTvouchtime(allsdf.format(new Date()));
			gl_accvouchDto_t.setCcodeexch_equal(kmbm);
//			gl_accvouchDto_t.setCpzchcode("daiding");//暂定
			gl_accvouchDto_t.setIprice("0");
			gl_accvouchDtos.add(gl_accvouchDto_t);
			GL_CashTableDto gl_cashTableDto=new GL_CashTableDto();
			gl_cashTableDto.setiPeriod(String.valueOf(month_t));
			gl_cashTableDto.setiSignSeq(Isignseq);
			gl_cashTableDto.setiNo_id(id);
			gl_cashTableDto.setInid(String.valueOf(i+1));
			gl_cashTableDto.setcCashItem(cashItem);
			gl_cashTableDto.setMd(list.get(i).getJe());
			gl_cashTableDto.setMc("0");
			gl_cashTableDto.setCcode(kmbm);
			gl_cashTableDto.setMd_f("0");
			gl_cashTableDto.setMc_f("0");
			gl_cashTableDto.setNd_s("0");
			gl_cashTableDto.setNc_s("0");
			gl_cashTableDto.setDbill_date(sdf.format(new Date()));
			gl_cashTableDto.setCsign(csign);
			gl_cashTableDto.setIyear(year.format(new Date()));
			gl_cashTableDto.setiYPeriod(month.format(new Date()));
//			gl_cashTableDto.setRowGuid("daiding");//暂定
			gl_cashTableDao.insert(gl_cashTableDto);
			list.get(i).setU8glid(gl_cashTableDto.getI_id());
			ids.append(",").append(gl_cashTableDto.getI_id());
		}
		if(!CollectionUtils.isEmpty(gl_accvouchDtos)){
			gl_accvouchDao.insertList(gl_accvouchDtos);
		}
		ddbxglDto.setZy("贷款");
		ddbxglDto.setPzbh(id);
		if(ids.length()>0){
			ddbxglDto.setU8glid(ids.substring(1));
		}
		map.put("ddbxglDto",ddbxglDto);
		map.put("ddbxmxDtos",list);
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public boolean updatePO_PomainAndPO_Podetails(HtmxDto htmxDto) throws BusinessException{
		PO_PomainDto po_pomainDto = new PO_PomainDto();
		po_pomainDto.setcState(htmxDto.getcState());
		po_pomainDto.setPOID(Integer.parseInt(htmxDto.getU8poid()));
		PO_PodetailsDto po_podetailsDto = new PO_PodetailsDto();
		if (StringUtil.isNotBlank(htmxDto.getGbrymc())){
			po_podetailsDto.setCbCloser(htmxDto.getGbrymc());
			po_podetailsDto.setCbCloseTime(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss.SSS"));
			po_podetailsDto.setCbCloseDate(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
		}else {
			po_podetailsDto.setCbCloser(htmxDto.getDkrymc());
			po_podetailsDto.setCbCloseTime(null);
			po_podetailsDto.setCbCloseDate(null);
		}
		if (StringUtil.isNotBlank(htmxDto.getU8mxid())){
			po_podetailsDto.setID(Integer.parseInt(htmxDto.getU8mxid()));
			int detail = pO_PodetailsDao.updatePo(po_podetailsDto);
			if(detail<1){
				throw new BusinessException("msg", "更新U8行状态失败！");
			}
		}
		if (!CollectionUtils.isEmpty(htmxDto.getGlids())){
			po_podetailsDto.setIds(htmxDto.getGlids().stream().map(Integer::parseInt).collect(Collectors.toList()));
			int detail = pO_PodetailsDao.updatePos(po_podetailsDto);
			if(detail<1){
				throw new BusinessException("msg", "更新U8行状态失败！");
			}
		}
		int main = pO_PomainDao.update(po_pomainDto);
		if(main<1){
			throw new BusinessException("msg", "更新U8行状态失败！");
		}

		return true;
	}
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public boolean updateOM_MOMainAndOM_MODetails(HtmxDto htmxDto) throws BusinessException {
		OM_MOMainDto om_moMainDto = new OM_MOMainDto();
		OM_MODetailsDto om_moDetailsDto = new OM_MODetailsDto();

		om_moMainDto.setcState(htmxDto.getcState());
		om_moMainDto.setMOID(htmxDto.getU8omid());

		if (StringUtil.isNotBlank(htmxDto.getGbrymc())){
			om_moDetailsDto.setCbCloser(htmxDto.getGbrymc());
			om_moDetailsDto.setCbCloseTime(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss.SSS"));
			om_moDetailsDto.setCbCloseDate(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
		}else {
			om_moDetailsDto.setCbCloser(htmxDto.getDkrymc());
			om_moDetailsDto.setCbCloseTime(null);
			om_moDetailsDto.setCbCloseDate(null);
		}
		if (StringUtil.isNotBlank(htmxDto.getU8ommxid())){
			om_moDetailsDto.setMODetailsID(htmxDto.getU8ommxid());
			int detail = om_moDetailsDao.update(om_moDetailsDto);
			if(detail<1){
				throw new BusinessException("msg", "更新U8行状态失败！");
			}
		}
		if (!CollectionUtils.isEmpty(htmxDto.getGlids())){
			om_moDetailsDto.setIds(htmxDto.getGlids());
			int detail = om_moDetailsDao.updates(om_moDetailsDto);
			if(detail<1){
				throw new BusinessException("msg", "更新U8行状态失败！");
			}
		}
		int main = om_moMainDao.update(om_moMainDto);
		if(main<1){
			throw new BusinessException("msg", "更新U8行状态失败！");
		}

		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public Map<String, Object> addU8ThData(ThglDto thglDto, List<ThmxDto> dtoList) throws BusinessException {
		Map<String,Object> map=new HashMap<>();

		//生成主表单号
		DispatchListDto dispatchListDto = new DispatchListDto();
		//生成单号
		VoucherHistoryDto voucherHistoryDto = new VoucherHistoryDto();
		voucherHistoryDto.setCardNumber("01");

		VoucherHistoryDto voucherHistoryDto_t = voucherHistoryDao.getMaxSerial(voucherHistoryDto);

		if(voucherHistoryDto_t!=null) {
			String serialnum = String.valueOf(Integer.parseInt(voucherHistoryDto_t.getcNumber())+1);
			String serial = "0000000000"+ serialnum;
			serial = serial.substring(serial.length()-10);
			//判断单号是否存在
			int ccode_new=0;
			dispatchListDto.setcDLCode(serial);
			DispatchListDto dispatchListDto_t = dispatchListDao.getDtoByDh(dispatchListDto);
			if(dispatchListDto_t!=null) {
				List<DispatchListDto> dlList = dispatchListDao.getcCode(dispatchListDto_t);
				ccode_new = Integer.parseInt(dispatchListDto_t.getcDLCode()) + 1;
				for (DispatchListDto dispatchListDto_c : dlList) {
					if(ccode_new-Integer.parseInt(dispatchListDto_c.getcDLCode())==0) {
						ccode_new = ccode_new+1;
					}else {
						break;
					}
				}
			}
			if(ccode_new!=0) {
				String ccode_newStr = "0000000000"+ccode_new;
				ccode_newStr = ccode_newStr.substring(ccode_newStr.length()-10);
				dispatchListDto.setcDLCode(ccode_newStr); //存入退货单号
				voucherHistoryDto_t.setcNumber(ccode_newStr); //去除多余0
			}else {
				voucherHistoryDto_t.setcNumber(serial); //去除多余0
			}
			//更新最大单号值
			int result_ccode = voucherHistoryDao.update(voucherHistoryDto_t);
			if(result_ccode<1)
				throw new BusinessException("msg", "更新U8最大单号失败！");
		}else {
			voucherHistoryDto.setcNumber("1");
			voucherHistoryDto.setbEmpty("0");
			dispatchListDto.setcDLCode("00000000001");
			voucherHistoryDao.insert(voucherHistoryDto);
		}
		thglDto.setU8thdh(dispatchListDto.getcDLCode());
		//生成主键id
		UA_IdentityDto uA_IdentityDto = new UA_IdentityDto();
		uA_IdentityDto.setcAcc_Id(accountName); // 存入账套
		uA_IdentityDto.setcVouchType("DISPATCH"); // 存入表标识
		UA_IdentityDto uA_IdentityDto_t = uA_IdentityDao.getMax(uA_IdentityDto); // 获取主键最大值
		if(uA_IdentityDto_t!=null) {
			if (StringUtil.isNotBlank(uA_IdentityDto_t.getiFatherId())) {
				int id = Integer.parseInt(uA_IdentityDto_t.getiFatherId());
				uA_IdentityDto_t.setiFatherId(String.valueOf(id + 1));
				dispatchListDto.setDLID(String.valueOf(id + 1000000001)); //存入主键id
			}
		}else {
			uA_IdentityDto.setiFatherId("1");
			dispatchListDto.setDLID("1000000001");
			uA_IdentityDto.setiChildId("0");
		}

		thglDto.setU8glid(dispatchListDto.getDLID()); //存入glid
		dispatchListDto.setcVouchType("05"); //业务类型
		dispatchListDto.setcSTCode(thglDto.getXslxdm()); //销售类型
		dispatchListDto.setdDate(thglDto.getDjrq()); //发货日期
		dispatchListDto.setcDepCode(thglDto.getXsbmdm()); //部门代码
		dispatchListDto.setcCusCode(thglDto.getKhdm()); //客户代码
		dispatchListDto.setcShipAddress(thglDto.getShdz()); //收获地址
		dispatchListDto.setcMemo(thglDto.getBz()); //备注
		dispatchListDto.setcVerifier(thglDto.getZsxm()); //审核人
		dispatchListDto.setcMaker(thglDto.getZsxm()); //制单人
		dispatchListDto.setcCusName(thglDto.getKhmc()); //客户名称
		dispatchListDto.setcBusType("普通销售"); //业务类型名称
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		dispatchListDto.setDverifydate(simpleDateFormat.format(date)); //审核时间
		dispatchListDto.setDcreatesystime(sdf.format(date)); //创建时间
		dispatchListDto.setDverifysystime(sdf.format(date)); //审核时间
		dispatchListDto.setcSysBarCode("||SA03"+"|"+dispatchListDto.getcDLCode()); //||SA03|+单号
		dispatchListDto.setCinvoicecompany(thglDto.getKhdm());
		dispatchListDto.setiTaxRate(dtoList.get(0).getSuil()); //税率
		dispatchListDto.setiExchRate("1"); //汇率
		dispatchListDto.setCexch_name(StringUtil.isNotBlank(thglDto.getBiz())?thglDto.getBiz():"人民币"); //币种
		dispatchListDto.setiVTid("75"); //模板
		dispatchListDto.setiNetLock(null);
		dispatchListDto.setbReturnFlag("1");
		dispatchListDto.setBneedbill("0");
		dispatchListDto.setcDefine7("0");
		List<String> list = new ArrayList<>();
		for (ThmxDto thmxDto : dtoList) {
			list.add(thmxDto.getCkid());
		}
		CkglDto ckglDto = new CkglDto();
		ckglDto.setIds(list);
		//新增销售出库单数据
		List<CkglDto> ckglDtos = ckglDao.getDtoByCkids(ckglDto);
		if(CollectionUtils.isEmpty(ckglDtos))
			throw new BusinessException("msg", "未找到出库信息！");

		//存更新的库存信息
		List<CurrentStockDto> currentStockList = new ArrayList<>();
		//存货位调整
		List<InvPositionDto> invPositionDtos = new ArrayList<>();
		//存货位总数信息
		List<InvPositionSumDto> invPositionSumDtos_mod = new ArrayList<>();
		//存货位总数信息
		List<InvPositionSumDto> invPositionSumDtos_add = new ArrayList<>();
		//存记账记录数据
		List<IA_ST_UnAccountVouchDto> iA_ST_UnAccountVouchDtos = new ArrayList<>();
		//存U8退货明细数据
		List<DispatchListsDto> dispatchListsDtos = new ArrayList<>();
		//存U8出库明细数据
		List<RdRecordsDto> rdRecordsDtos = new ArrayList<>();
		//存U8销售订单数据
		List<SO_SODetailsDto> sO_SODetailsDtos = new ArrayList<>();
		//存销售出库单信息
		List<RdRecordDto> rdRecordDtos = new ArrayList<>();
		//存退货明细List
		List<ThmxDto> thmxDtos = new ArrayList<>();
		//修改U8发货明细数据
		List<DispatchListsDto> updispatchListsDtos = new ArrayList<>();

		//发货流水号
		int serial_dl = 1000000001;
		//获取发货明细，出库明细
		if(StringUtil.isBlank(dispatchListDto.getcSOCode())) {
			dispatchListDto.setcSOCode(dtoList.get(0).getU8xsdh()); //订单号，如果存在多个，拿第一个
		}

		//存出库单得单号
		StringBuilder ckdh = new StringBuilder();
		for (CkglDto ckglDto_t : ckglDtos) {
			RdRecordDto rdRecordDto=new RdRecordDto();
			SimpleDateFormat sdf_d = new SimpleDateFormat("yyyyMMdd");
			String prefix_in = sdf_d.format(date);
			VoucherHistoryDto inVoucherHistoryDto = new VoucherHistoryDto();
			inVoucherHistoryDto.setcSeed(prefix_in);
			inVoucherHistoryDto.setCardNumber("0303");
			//获取最大流水号 CK2021102504
			VoucherHistoryDto inVoucherHistoryDto_t = voucherHistoryDao.getMaxSerial(inVoucherHistoryDto);
			if(inVoucherHistoryDto_t!=null) {
				String serial_in = String.valueOf(Integer.parseInt(inVoucherHistoryDto_t.getcNumber())+1);
				rdRecordDto.setcCode("CK"+prefix_in+(serial_in.length()>1?serial_in:"0"+serial_in)); // 销售出库单号
				rdRecordDto.setPrefix("CK"+prefix_in);
				rdRecordDto.setSflxbj("32");
				RdRecordDto rdRecordDtos_t = rdRecordDao.queryByCode(rdRecordDto);
				int ccode_new_in=0;
				if (rdRecordDtos_t != null ) {
					List<RdRecordDto> rdList = rdRecordDao.getcCodeRd(rdRecordDto);
					ccode_new_in = Integer.parseInt(rdRecordDto.getcCode().substring(2)) + 1;
					for (RdRecordDto rdRecordDto_c_in : rdList) {
						if(ccode_new_in-Integer.parseInt(rdRecordDto_c_in.getcCode().substring(2))==0) {
							ccode_new_in = ccode_new_in+1;
						}else {
							break;
						}
					}
				}
				if(ccode_new_in!=0) {
					rdRecordDto.setcCode("CK"+ccode_new_in); //出库单号
					inVoucherHistoryDto_t.setcNumber(String.valueOf(ccode_new_in).substring(2)); //去除多余0
				}else {
					inVoucherHistoryDto_t.setcNumber(serial_in);
				}
				//更新最大单号值
				int result_ccode_in = voucherHistoryDao.update(inVoucherHistoryDto_t);
				if(result_ccode_in<1)
					throw new BusinessException("msg", "更新U8最大单号失败！");
			}else {
				inVoucherHistoryDto.setcContent("日期");
				inVoucherHistoryDto.setcContentRule("日");
				inVoucherHistoryDto.setbEmpty("0");
				inVoucherHistoryDto.setcNumber("1");
				rdRecordDto.setcCode("CK"+prefix_in+"01");
				//单号最大表增加数据
				int result_ccode_in = voucherHistoryDao.insert(inVoucherHistoryDto);
				if(result_ccode_in<1)
					throw new BusinessException("msg", "新增U8最大单号失败！");
			}
			ckdh.append(",").append(rdRecordDto.getcCode());

			//生成主键id
			UA_IdentityDto uA_IdentityDto_rd = new UA_IdentityDto();
			uA_IdentityDto_rd.setcAcc_Id(accountName); // 存入账套
			uA_IdentityDto_rd.setcVouchType("rd"); // 存入表标识
			UA_IdentityDto uA_IdentityDto_t_rd = uA_IdentityDao.getMax(uA_IdentityDto_rd); // 获取主键最大值
			if(uA_IdentityDto_t_rd!=null) {
				if (StringUtil.isNotBlank(uA_IdentityDto_t_rd.getiFatherId())) {
					int id = Integer.parseInt(uA_IdentityDto_t_rd.getiFatherId());
					uA_IdentityDto_t_rd.setiFatherId(String.valueOf(id + 1));
					rdRecordDto.setID(id + 1000000001); //存入主键id
				}
			}else {
				uA_IdentityDto_rd.setiFatherId("1");
				uA_IdentityDto_rd.setiChildId("0");
				rdRecordDto.setID(1000000001); //存入主键id
			}

			ckglDto_t.setGlid(String.valueOf(rdRecordDto.getID()));
			ckglDto_t.setU8ckdh(rdRecordDto.getcCode());
			rdRecordDto.setbRdFlag("0");
			rdRecordDto.setcVouchType("32");
			rdRecordDto.setcRdCode("7");
			if("/matridxigams".equals(urlPrefix)){
				rdRecordDto.setcRdCode("d");
			}
			rdRecordDto.setVT_ID("87"); //模板号
			rdRecordDto.setdDate(simpleDateFormat.format(date)); //出库日期
			rdRecordDto.setcDepCode(ckglDto_t.getJgdm()); //部门代码
			rdRecordDto.setcMaker(ckglDto_t.getZsxm()); //制单人
			rdRecordDto.setCsysbarcode("||st32|"+rdRecordDto.getcCode());
			rdRecordDto.setcMemo(StringUtil.isNotBlank(ckglDto_t.getBz())?(ckglDto_t.getBz().length()>255?ckglDto_t.getBz().substring(0,255):ckglDto_t.getBz()):null);//备注
			rdRecordDto.setcBusType("普通销售");
			rdRecordDto.setcSource("发货单");
			rdRecordDto.setbOMFirst("0");
			rdRecordDto.setcBusCode(dispatchListDto.getcDLCode()); //退货单号
			rdRecordDto.setcWhCode(ckglDto_t.getCkdm()); //仓库代码
			rdRecordDto.setcStCode(dispatchListDto.getcSTCode()); //销售类型
			rdRecordDto.setcCUsCode(dispatchListDto.getcCusCode()); //客户代码
			rdRecordDto.setcDLCode(dispatchListDto.getDLID()); //退货ID
			rdRecordDto.setcShipAddress(dispatchListDto.getcShipAddress()); //收货地址
			rdRecordDto.setCinvoicecompany(dispatchListDto.getcCusCode()); //客户代码
			rdRecordDto.setDnmaketime(sdf.format(date)); //制单时间
			rdRecordDto.setcPersonCode(thglDto.getGrouping()); //业务员
			rdRecordDto.setcDefine7("0"); //业务员
			rdRecordDtos.add(rdRecordDto);
			//流水号
			int serial = 1000000001;
			for (ThmxDto thmxDto : dtoList) {
				if(thmxDto.getCkid().equals(ckglDto_t.getCkid())) {
					CurrentStockDto currentStockDto2 = new CurrentStockDto();
					currentStockDto2.setcInvCode(thmxDto.getWlbm());
					currentStockDto2.setcBatch(thmxDto.getScph());
					currentStockDto2.setcWhCode(thmxDto.getCkdm());
					CurrentStockDto currentStockDto1 = currentStockDao.getCurrentStocksByDto(currentStockDto2);
					if (currentStockDto1==null){
						boolean itemidflg = true;
						//处理库存量
						List<String> ids = new ArrayList<>();
						ids.add(thmxDto.getWlbm());
						CurrentStockDto curr = new CurrentStockDto();
						curr.setIds(ids);
						List<CurrentStockDto> currentStockDtos = currentStockDao.queryBycInvCode(curr);
						if (null != currentStockDtos && currentStockDtos.size()>0) {
							// 如果物料编码一样，追溯号不一样，ItemId字段相同
							currentStockDto2.setItemId(currentStockDtos.get(0).getItemId());
							itemidflg = false;
						}
						if (itemidflg) {
							SCM_ItemDto sCM_ItemDto_c  = sCM_ItemDao.getDtoBycInvVode(currentStockDto2.getcInvCode());
							if(sCM_ItemDto_c!=null) {
								currentStockDto2.setItemId(sCM_ItemDto_c.getId());
							}
						}
						currentStockDto2.setiQuantity(thmxDto.getThsl());
						currentStockDto2.setdVDate(thmxDto.getYxq()); // 失效日期
						currentStockDto2.setdMdate(thmxDto.getScrq()); // 生产日期
						currentStockDto2.setiExpiratDateCalcu("0");
						currentStockDto2.setcMassUnit("1"); // 有效单位
						currentStockDto2.setiMassDate("99"); // 有效期数
						int result_add = currentStockDao.insert(currentStockDto2);
						if (result_add < 1) {
							throw new BusinessException("msg", "U8库存新增失败！");
						}
					}else {
						currentStockDto2.setiQuantitybj("1");
						currentStockDto2.setiQuantity(thmxDto.getThsl());
						currentStockList.add(currentStockDto2);
					}
					//组装U8退货单明细数据
					DispatchListsDto dispatchListsDto = new DispatchListsDto();
					String dlsid=uA_IdentityDto_t!=null?String.valueOf(Integer.parseInt(uA_IdentityDto_t.getiChildId())+serial_dl):String.valueOf(Integer.parseInt(uA_IdentityDto.getiChildId())+serial_dl);
					thmxDto.setU8mxglid(dlsid);
					dispatchListsDto.setiDLsID(dlsid);
					dispatchListsDto.setDLID(dispatchListDto.getDLID()); //主表ID
					dispatchListsDto.setiCorID(thmxDto.getFhmxglid()); //退货存发货iDLsID
					dispatchListsDto.setIsettleNum("0");
					dispatchListsDto.setiSettleQuantity("0");
					dispatchListsDto.setiBatch("0");
					dispatchListsDto.setfEnSettleQuan("0");
					dispatchListsDto.setfEnSettleSum("0");
					dispatchListsDto.setFsumsignquantity("0");
					dispatchListsDto.setFsumsignnum("0");
					dispatchListsDto.setBsignover(null); //null/如果存在不同仓库的物料第一个仓库存null，后面的存0
					dispatchListsDto.setBneedloss(null);
					dispatchListsDto.setFrlossqty(null);
					DispatchListDto dispatchListDto1 = new DispatchListDto();
					dispatchListDto1.setDLID(thmxDto.getFhglid());
					dispatchListDto1 = dispatchListDao.getcDLCode(dispatchListDto1);
					if (dispatchListDto1==null){
						throw new BusinessException("msg", "未找到对应发货单信息!");
					}
					dispatchListsDto.setcCorCode(dispatchListDto1.getcDLCode());
					String thsl = String.valueOf(-Double.parseDouble(thmxDto.getThsl()));
					dispatchListsDto.setcWhcode(thmxDto.getCkdm()); //仓库代码
					dispatchListsDto.setcInvCode(thmxDto.getWlbm()); //物料编码
					dispatchListsDto.setiQuantity(thsl); //退货数量
					dispatchListsDto.setcBatch(thmxDto.getScph()); //生产批号
					dispatchListsDto.setcMemo(thmxDto.getBz()); //备注
					dispatchListsDto.setdMDate(thmxDto.getScrq()); //生产日期
					dispatchListsDto.setdVDate(thmxDto.getYxq()); //失效日期
					dispatchListsDto.setiSosID(thmxDto.getMxglid()); //U8销售订单明细ID
					dispatchListsDto.setcInvName(thmxDto.getWlmc()); //物料名称
					dispatchListsDto.setfOutQuantity(thsl); //退货数量
					dispatchListsDto.setcSoCode(thmxDto.getU8xsdh()); //订单号
					if (("0").equals(thmxDto.getBzqflg())){
						dispatchListsDto.setcMassUnit("2");
						dispatchListsDto.setiMassDate(thmxDto.getBzq());//保质期
					}else{
						dispatchListsDto.setcMassUnit("1");
						dispatchListsDto.setiMassDate("99");//保质期
					}
					if(StringUtil.isBlank(thmxDto.getScph())) {
						dispatchListsDto.setcMassUnit(null);
						dispatchListsDto.setiMassDate(null);//保质期
					}
					dispatchListsDto.setCordercode(thmxDto.getU8xsdh()); //销售订单
					SO_SODetailsDto sO_SODetails_ino = new SO_SODetailsDto();
					sO_SODetails_ino.setiSOsID(thmxDto.getMxglid());
					sO_SODetails_ino = sO_SODetailsDao.getIrownoByiSOsID(sO_SODetails_ino);
					if (sO_SODetails_ino==null){
						throw new BusinessException("msg", "未找到对应销售单信息!");
					}
					dispatchListsDto.setIorderrowno(sO_SODetails_ino.getiRowNo()); //订单明细流水号
					dispatchListsDto.setIrowno(String.valueOf(serial_dl-1000000000)); //明细流水号
					dispatchListsDto.setCbSysBarCode("||SA03|"+dispatchListDto.getcDLCode()+"|"+dispatchListsDto.getIrowno());
					dispatchListsDto.setiRetQuantity("0");
					dispatchListsDto.setFretsum("0");
					dispatchListsDto.setFretqtywkp("0");
					dispatchListsDto.setFretqtyykp("0");
					dispatchListsDto.setFretsumykp("0");
					dispatchListsDto.setKL("100"); //扣率
					dispatchListsDto.setKL2("100"); //扣率2
					dispatchListsDto.setiTaxRate(thmxDto.getSuil()); //税率
					dispatchListsDto.setiQuotedPrice(StringUtil.isNotBlank(thmxDto.getBj())?thmxDto.getBj():"0"); //报价
					dispatchListsDto.setiUnitPrice(thmxDto.getWsdj()); //无税单价
					dispatchListsDto.setiTaxUnitPrice(thmxDto.getHsdj()); //含税单价
					if(StringUtil.isNotBlank(thmxDto.getWsdj())) {
						dispatchListsDto.setiNatUnitPrice(thmxDto.getWsdj()); //无税单价
						BigDecimal wszje = new BigDecimal(thmxDto.getWsdj()).multiply(new BigDecimal(thsl));
						dispatchListsDto.setiMoney(wszje.setScale(5,RoundingMode.HALF_UP).toString()); //无税总价 (保留两位小数)
						dispatchListsDto.setiNatMoney(wszje.setScale(5,RoundingMode.HALF_UP).toString()); //无税总金额
					}
					if(StringUtil.isNotBlank(thmxDto.getHsdj())) {
						BigDecimal hszje = new BigDecimal(thmxDto.getHsdj()).multiply(new BigDecimal(thsl));
						dispatchListsDto.setiSum(hszje.setScale(5,RoundingMode.HALF_UP).toString()); //含税总金额
						dispatchListsDto.setiNatSum(hszje.setScale(5,RoundingMode.HALF_UP).toString()); //含税总金额
					}
					if(StringUtil.isNotBlank(thmxDto.getWsdj()) && StringUtil.isNotBlank(thmxDto.getHsdj())) {
						BigDecimal se = new BigDecimal(thmxDto.getHsdj()).multiply(new BigDecimal(thsl)).subtract(new BigDecimal(thmxDto.getWsdj()).multiply(new BigDecimal(thsl)));
						dispatchListsDto.setiTax(se.setScale(5,RoundingMode.HALF_UP).toString()); //税额
						dispatchListsDto.setiNatTax(se.setScale(5,RoundingMode.HALF_UP).toString()); //税额
					}
					if(StringUtil.isNotBlank(thmxDto.getBj()) && StringUtil.isNotBlank(thmxDto.getHsdj())) {
						//(报价-含税单价)×数量
						BigDecimal bjce = (new BigDecimal(thmxDto.getBj()).subtract(new BigDecimal(thmxDto.getHsdj()))).multiply(new BigDecimal(thsl));
						dispatchListsDto.setiDisCount(bjce.setScale(5,RoundingMode.HALF_UP).toString()); //报价差额
						dispatchListsDto.setiNatDisCount(bjce.setScale(5,RoundingMode.HALF_UP).toString()); //报价差额
					}else {
						dispatchListsDto.setiDisCount("0");
						dispatchListsDto.setiNatDisCount("0");
					}
					dispatchListsDto.setFretailrealamount("0");
					dispatchListsDto.setFretailsettleamount("0");
					dispatchListsDto.setBmpforderclosed(null);
					dispatchListsDtos.add(dispatchListsDto);
					//组装要修改的u8发货数据
					DispatchListsDto updispatchListsDto = new DispatchListsDto();
					updispatchListsDto.setiDLsID(thmxDto.getFhmxglid());
					updispatchListsDto.setfVeriRetQty(thmxDto.getThsl());
					if(StringUtil.isNotBlank(thmxDto.getHsdj())) {
						BigDecimal hszje = new BigDecimal(thmxDto.getHsdj()).multiply(new BigDecimal(thmxDto.getThsl())).setScale(6, RoundingMode.HALF_UP);
						updispatchListsDto.setfVeriRetSum(String.valueOf(hszje));
						updispatchListsDto.setFretsum(String.valueOf(hszje));
					}
					updispatchListsDto.setiRetQuantity(thmxDto.getThsl());
					updispatchListsDto.setFretqtywkp(thmxDto.getThsl());
					updispatchListsDto.setFretqtyykp(null);
					updispatchListsDto.setFrettbquantity("0");
					updispatchListsDtos.add(updispatchListsDto);
					//处理销售出库明细数据
					RdRecordsDto rdRecordsDto = new RdRecordsDto();
					rdRecordsDto.setAutoID(serial+Integer.parseInt(uA_IdentityDto_t_rd!=null?uA_IdentityDto_t_rd.getiChildId():uA_IdentityDto_rd.getiChildId()));
					thmxDto.setCkmxglid(String.valueOf(rdRecordsDto.getAutoID()));
					rdRecordsDto.setID(rdRecordDto.getID());
					rdRecordsDto.setcInvCode(thmxDto.getWlbm()); //物料编码
					rdRecordsDto.setiQuantity(thsl); //退货数量
					rdRecordsDto.setcBatch(thmxDto.getScph()); //生产批号
					rdRecordsDto.setdVDate(thmxDto.getYxq()); //失效日期
					rdRecordsDto.setiDLsID(dispatchListsDto.getiDLsID()); //关联发货单明细
					rdRecordsDto.setiNQuantity(thsl); //退货数量
					rdRecordsDto.setdMadeDate(thmxDto.getScrq()); //生产日期
					rdRecordsDto.setcPosition(thmxDto.getKwckdm()); //库位
					rdRecordsDto.setbGsp("0");
					rdRecordsDto.setIposflag(StringUtil.isNotBlank(thmxDto.getKwckdm())?"1":null);

					if (("0").equals(thmxDto.getBzqflg())){
						rdRecordsDto.setcMassUnit("2");
						rdRecordsDto.setiMassDate(thmxDto.getBzq());//保质期
					}else{
						rdRecordsDto.setcMassUnit("1");
						rdRecordsDto.setiMassDate("99");//保质期
					}
					if(StringUtil.isBlank(thmxDto.getScrq())) {
						rdRecordsDto.setcMassUnit("0");
						rdRecordsDto.setiMassDate(null);//保质期
					}
					rdRecordsDto.setCbdlcode(dispatchListDto.getcDLCode()); //发货单号
					rdRecordsDto.setIorderdid(thmxDto.getMxglid()); //关联订单明细
					rdRecordsDto.setIordercode(thmxDto.getU8xsdh()); //销售订单
					rdRecordsDto.setIorderseq(thmxDto.getDdxh()); //销售订单流水
					rdRecordsDto.setIpesoseq(null); //销售订单流水
					rdRecordsDto.setIordertype("1");
					if(StringUtil.isNotBlank(thmxDto.getDdmxid())) {
						rdRecordsDto.setIordertype("1");
					}
					rdRecordsDto.setBneedbill("0");
					rdRecordsDto.setIpesodid(null); //关联订单明细
					rdRecordsDto.setCpesocode(null); //销售订单
					rdRecordsDto.setIpesotype("0");
					rdRecordsDto.setCorufts(null);
					rdRecordsDto.setCbMemo(StringUtil.isNotBlank(thmxDto.getBz())?(thmxDto.getBz().length()>255?thmxDto.getBz().substring(0,255):thmxDto.getBz()):null); //备注
					rdRecordsDto.setIrowno(String.valueOf(serial-1000000000));//流水号
					rdRecordsDto.setCbsysbarcode("||st32|"+rdRecordDto.getcCode()+"|"+rdRecordsDto.getIrowno());
					rdRecordsDtos.add(rdRecordsDto);

					//流水号+1
					serial = serial + 1;
					serial_dl = serial_dl + 1;

					//组装要修改的销售明细数据
					SO_SODetailsDto sO_SODetailsDto = new SO_SODetailsDto();
					sO_SODetailsDto.setiSOsID(thmxDto.getMxglid());
					sO_SODetailsDto.setiFHQuantity(thmxDto.getThsl());//退货数量
					sO_SODetailsDto.setiFHNum("0");
					if(StringUtil.isNotBlank(thmxDto.getHsdj())) {
						BigDecimal hszje_t = new BigDecimal(thmxDto.getHsdj()).multiply(new BigDecimal(thmxDto.getThsl()));
						sO_SODetailsDto.setiFHMoney(hszje_t.setScale(2,RoundingMode.HALF_UP).toString()); //退货含税总价
						sO_SODetailsDto.setfVeriDispSum(hszje_t.setScale(2,RoundingMode.HALF_UP).toString());
					}
					sO_SODetailsDto.setFoutquantity(thmxDto.getThsl());
					sO_SODetailsDto.setFoutnum("0");
					sO_SODetailsDto.setFretquantity(thmxDto.getThsl());

					sO_SODetailsDtos.add(sO_SODetailsDto);

					//组装记账记录表
					IA_ST_UnAccountVouchDto iA_ST_UnAccountVouchDto = new IA_ST_UnAccountVouchDto();
					iA_ST_UnAccountVouchDto.setIDUN(String.valueOf(rdRecordsDto.getID()));
					iA_ST_UnAccountVouchDto.setIDSUN(String.valueOf(rdRecordsDto.getAutoID()));
					iA_ST_UnAccountVouchDto.setcVouTypeUN("32");
					iA_ST_UnAccountVouchDto.setcBUstypeUN("普通销售");
					iA_ST_UnAccountVouchDtos.add(iA_ST_UnAccountVouchDto);

					//处理货位调整表
					if(StringUtil.isNotBlank(thmxDto.getKwckdm())) {
						InvPositionDto invPositionDto_out = new InvPositionDto();
						invPositionDto_out.setRdsID(String.valueOf(rdRecordsDto.getAutoID()));
						invPositionDto_out.setRDID(String.valueOf(rdRecordDto.getID()));
						invPositionDto_out.setcWhCode(thmxDto.getCkdm());
						invPositionDto_out.setcPosCode(thmxDto.getKwckdm());
						invPositionDto_out.setcInvCode(thmxDto.getWlbm());
						invPositionDto_out.setcBatch(StringUtil.isNotBlank(thmxDto.getScph())?thmxDto.getScph():"");
						invPositionDto_out.setdVDate(thmxDto.getYxq());
						invPositionDto_out.setiQuantity(thsl);
						invPositionDto_out.setcHandler(thglDto.getZsxm());
						invPositionDto_out.setdDate(sdf.format(date));
						invPositionDto_out.setbRdFlag("0");
						invPositionDto_out.setdMadeDate(thmxDto.getScrq());
						if (("0").equals(thmxDto.getBzqflg())) {
							invPositionDto_out.setcMassUnit("2");
							invPositionDto_out.setiMassDate(thmxDto.getBzq()); // 有效期数
						} else {
							invPositionDto_out.setcMassUnit("1");
							invPositionDto_out.setiMassDate("99"); // 有效期数
						}
						if(StringUtil.isBlank(thmxDto.getScrq())) {
							invPositionDto_out.setcMassUnit("0");
							invPositionDto_out.setiMassDate(null); // 有效期数
						}
						invPositionDto_out.setCvouchtype("32");
						Date date_t_o = new Date();
						invPositionDto_out.setdVouchDate(sdf.format(date_t_o));
						invPositionDtos.add(invPositionDto_out);

						//组装货位总数信息
						InvPositionSumDto invPositionSumDto = new InvPositionSumDto();
						invPositionSumDto.setcWhCode(thmxDto.getCkdm());
						invPositionSumDto.setcPosCode(thmxDto.getKwckdm());
						invPositionSumDto.setcInvCode(thmxDto.getWlbm());
						invPositionSumDto.setcBatch(StringUtil.isNotBlank(thmxDto.getScph())?thmxDto.getScph():"");
						//判断是否存在  仓库，物料编码，库位，追溯号一样的数据
						InvPositionSumDto invPositionSumDto_t = invPositionSumDao.getDto(invPositionSumDto);
						if(invPositionSumDto_t!=null) {
							//更新货物货物数量
							invPositionSumDto_t.setJsbj("0"); //计算标记,相加
							invPositionSumDto_t.setiQuantity(thmxDto.getThsl());
							invPositionSumDtos_mod.add(invPositionSumDto_t);
						}else {
							invPositionSumDto.setiQuantity(thmxDto.getThsl());
							invPositionSumDto.setInum("0");
							invPositionSumDto.setcMassUnit("1");
							invPositionSumDto.setiMassDate("99"); // 保质期
							invPositionSumDto.setdMadeDate(thmxDto.getScrq());
							invPositionSumDto.setdVDate(thmxDto.getYxq());
							invPositionSumDtos_add.add(invPositionSumDto);
						}
					}
				}

			}


			thmxDtos.addAll(dtoList);

			// 更新最大值表
			boolean result_ui = false;
			//判断是新增还是修改
			if(uA_IdentityDto_t_rd!=null) {
				//更新
				uA_IdentityDto_t_rd.setiChildId(String.valueOf(serial-1000000001+Integer.parseInt(uA_IdentityDto_t_rd.getiChildId())));
				result_ui = uA_IdentityDao.update(uA_IdentityDto_t_rd)>0;
			}
			if(!result_ui) {
				uA_IdentityDto_rd.setiChildId(String.valueOf(serial-1000000001+Integer.parseInt(uA_IdentityDto_rd.getiChildId())));
				result_ui = uA_IdentityDao.insert(uA_IdentityDto_rd)>0;
			}
			if(!result_ui) {
				throw new BusinessException("msg", "最大id值更新失败!");
			}

		}

		dispatchListDto.setcSaleOut(ckdh.substring(1)); //出库单号，如果存在多个，逗号拼接

		// 更新最大值表
		boolean result_ui = false;
		//判断是新增还是修改
		if(uA_IdentityDto_t!=null) {
			//更新
			uA_IdentityDto_t.setiChildId(String.valueOf(serial_dl-1000000001+Integer.parseInt(uA_IdentityDto_t.getiChildId())));
			result_ui = uA_IdentityDao.update(uA_IdentityDto_t)>0;
		}
		if(!result_ui) {
			uA_IdentityDto.setiChildId(String.valueOf(serial_dl-1000000001+Integer.parseInt(uA_IdentityDto.getiChildId())));
			result_ui = uA_IdentityDao.insert(uA_IdentityDto)>0;
		}
		if(!result_ui) {
			throw new BusinessException("msg", "最大id值更新失败!");
		}



		int result;
		//新增退货单
		result = dispatchListDao.insert(dispatchListDto);
		if(result<1) {
			throw new BusinessException("msg","新增退货单主表失败！");
		}
		//新增退货明细
		result = dispatchListsDao.insertListDtos(dispatchListsDtos);
		if(result<1) {
			throw new BusinessException("msg","新增退货单明细失败！");
		}
		//修改发货明细
		result = dispatchListsDao.updateListDtos(updispatchListsDtos);
		if(result<1) {
			throw new BusinessException("msg","修改发货明细失败！");
		}
		//新增出库单
		result = rdRecordDao.insertDtos(rdRecordDtos);
		if(result<1) {
			throw new BusinessException("msg","新增出库单失败！");
		}
		//新增出库明细
		result = rdRecordsDao.insertListDtos(rdRecordsDtos);
		if(result<1) {
			throw new BusinessException("msg","新增出库明细失败！");
		}
		//修改销售订单明细
		result = sO_SODetailsDao.updateSoListDtos(sO_SODetailsDtos);
		if(result<1) {
			throw new BusinessException("msg","修改销售订单明细失败！");
		}
		if (!CollectionUtils.isEmpty(currentStockList)){
			//更新U8库存
			result = currentStockDao.updateIQuantityList(currentStockList);
			if(result<1) {
				throw new BusinessException("msg","批量更新U8库存失败！");
			}
		}
		if (!CollectionUtils.isEmpty(invPositionDtos)){
			int result_hw;
			List<List<InvPositionDto>> partition = Lists.partition(invPositionDtos, 10);
			for (List<InvPositionDto> positionDtos : partition) {
				result_hw = invPositionDao.insertDtos(positionDtos);
				if (result_hw < 1)
					throw new BusinessException("msg", "新增U8货位调整失败！");
			}
		}
		if (!CollectionUtils.isEmpty(invPositionSumDtos_mod)){
			int result_mod;
			List<List<InvPositionSumDto>> partition = Lists.partition(invPositionSumDtos_mod, 10);
			for (List<InvPositionSumDto> invPositionSumDtos : partition) {
				result_mod = invPositionSumDao.updateDtos(invPositionSumDtos);
				if(result_mod<1) {
					throw new BusinessException("msg", "更新U8货位总数失败！");
				}
			}
		}
		if (!CollectionUtils.isEmpty(invPositionSumDtos_add)){
			int result_add;
			List<List<InvPositionSumDto>> partition = Lists.partition(invPositionSumDtos_add, 10);
			for (List<InvPositionSumDto> invPositionSumDtos : partition) {
				result_add = invPositionSumDao.insertDtos(invPositionSumDtos);
				if(result_add<1) {
					throw new BusinessException("msg", "新增U8货位总数失败！");
				}
			}
		}
		boolean result_av = addAccountVs(iA_ST_UnAccountVouchDtos,"32");
		if(!result_av) {
			throw new BusinessException("msg","新增记账信息失败！");
		}
		map.put("thglDto", thglDto);
		map.put("ckglDtos", ckglDtos);
		map.put("thmxDtos", thmxDtos);
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, transactionManager = "MatridxTransactionManager")
	public boolean determine_Entry(String rq) {
		rq=rq.replace("-","");
		rq=rq.substring(0,6);
		return !"1".equals(gl_mendDao.queryBFlag_PU(rq));
	}
}
