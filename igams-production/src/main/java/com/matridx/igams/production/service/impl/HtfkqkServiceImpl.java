package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.dao.entities.external.ConsumeAmount;
import com.matridx.igams.common.dao.entities.external.Expense;
import com.matridx.igams.common.dao.entities.external.ExpenseRequestDto;
import com.matridx.igams.common.dao.entities.external.PayeeAccount;
import com.matridx.igams.common.dao.entities.external.ReimburseDto;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.common.util.ExternalUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.production.dao.entities.*;
import com.matridx.igams.production.dao.post.IHtfkqkDao;
import com.matridx.igams.production.service.svcinterface.IHtfkmxService;
import com.matridx.igams.production.service.svcinterface.IHtfkqkService;
import com.matridx.igams.production.service.svcinterface.IHtfktxService;
import com.matridx.igams.production.service.svcinterface.IHtglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.apache.commons.lang.StringUtils;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HtfkqkServiceImpl extends BaseBasicServiceImpl<HtfkqkDto, HtfkqkModel, IHtfkqkDao>
		implements IHtfkqkService, IAuditService {

	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.wechat.registerurl:}")
	private String registerurl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.systemflg.systemname:}")
	private String systemname;
	@Value("${matridx.systemflg.servicecode:}")
	private String servicecode;
	@Value("${matridx.mk.mkflag:}")
	private String mkflag;
	@Autowired
	IHtglService htglService;
	@Autowired
	ICommonService commonService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IShgcService shgcService;
	@Autowired
	IShxxService shxxService;
	@Autowired
	IShlcService shlcService;
	@Autowired
	IDdfbsglService ddfbsglService;
	@Autowired
	IDdspglService ddspglService;
	@Autowired
	IHtfkmxService htfkmxService;
	@Autowired
	IHtfktxService htfktxService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ExternalUtil externalUtil;

	private final Logger log = LoggerFactory.getLogger(HtfkqkServiceImpl.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertDto(HtfkqkDto htfkqkDto) {
		if (StringUtil.isBlank(htfkqkDto.getHtfkid())) {
			htfkqkDto.setHtfkid(StringUtil.generateUUID());
		}
		htfkqkDto.setZt(StatusEnum.CHECK_NO.getCode());
		int result = dao.insert(htfkqkDto);
		return result > 0;
	}

	/**
	 * 校验付款金额是否超过合同未付款金额
	 */
	@Override
	public Map<String, Object> checkMoney(HtfkqkDto htfkqkDto) {
		Map<String, Object> map = new HashMap<>();
		List<HtfkmxDto> htfkmxDtos = JSON.parseArray(htfkqkDto.getFkmxJson(), HtfkmxDto.class);
		List<HtfkmxDto> t_htfkmxDtos = htfkmxService.getHtfkqkMessage(htfkmxDtos);
		if (!CollectionUtils.isEmpty(t_htfkmxDtos)) {
			for (HtfkmxDto htfkmxDto : t_htfkmxDtos) {
				//若该合同付款总金额大于未付款金额则不允许保存
				BigDecimal zfkje = new BigDecimal(StringUtil.isNotBlank(htfkmxDto.getZfkje()) ? htfkmxDto.getZfkje() : "0");
				BigDecimal wfkje = new BigDecimal(StringUtil.isNotBlank(htfkmxDto.getWfkje()) ? htfkmxDto.getWfkje() : "0");
				if (zfkje.compareTo(wfkje) > 0) {
					map.put("status", "fail");
					map.put("message", "保存失败，合同编号为" + htfkmxDto.getHtnbbh() + "的合同付款总金额超过未付款金额！");
					return map;
				}
			}
		}
		map.put("status", "success");
		return map;
	}

	/**
	 * 合同列表付款维护新增保存
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertFkqk(HtfkqkDto htfkqkDto) {
		//保存合同付款信息
		boolean addHtfk = insertDto(htfkqkDto);
		if (!addHtfk)
			return false;
		List<HtfkmxDto> htfkmxDtos = JSON.parseArray(htfkqkDto.getFkmxJson(), HtfkmxDto.class);
		List<String> fktxids = new ArrayList<>();
		if (!CollectionUtils.isEmpty(htfkmxDtos)) {
			for (HtfkmxDto htfkmxDto : htfkmxDtos) {
				htfkmxDto.setLrry(htfkqkDto.getLrry());
				htfkmxDto.setHtfkid(htfkqkDto.getHtfkid());
				htfkmxDto.setHtfkmxid(StringUtil.generateUUID());
				htfkmxDto.setFkbfb(htfkmxDto.getFkbfb().replaceAll("%", ""));
				if (StringUtils.isNotBlank(htfkmxDto.getFktxid())) {
					fktxids.add(htfkmxDto.getFktxid());
				}
			}
			boolean result = htfkmxService.insertDtoList(htfkmxDtos);
			if (!result)
				return false;
		}
		if (!CollectionUtils.isEmpty(fktxids)) {
			//更新付款提醒表中的fkid
			HtfktxDto htfktxDto = new HtfktxDto();
			htfktxDto.setIds(fktxids);
			htfktxDto.setHtfkid(htfkqkDto.getHtfkid());
			return htfktxService.updateHtfkid(htfktxDto);
		}
		return true;
	}


	/**
	 * 合同付款修改保存
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateFkqk(HtfkqkDto htfkqkDto) {
		boolean b = update(htfkqkDto);
		if (!b) {
			return false;
		}
		List<HtfkmxDto> list = JSON.parseArray(htfkqkDto.getFkmxJson(), HtfkmxDto.class);
		if (!CollectionUtils.isEmpty(list)) {
			for (HtfkmxDto htfkmxDto : list) {
				double fkje = Double.parseDouble(htfkmxDto.getFkje());
				double zje = Double.parseDouble(StringUtil.isNotBlank(htfkmxDto.getXzje())?htfkmxDto.getXzje():htfkmxDto.getZje());
				// 计算商
				double quotient = fkje / zje;
				// 设置转换格式
				DecimalFormat df = new DecimalFormat("0.00%");
				// 转换格式
				String result = df.format(quotient).replaceAll("%", "");
				htfkmxDto.setFkbfb(result);
				htfkmxDto.setXgry(htfkqkDto.getXgry());
			}
			int result = htfkmxService.updateList(list);
			if (result < 1) {
				return false;
			}
		}
		HtfkqkDto htfkqkDto_t = dao.getDtoById(htfkqkDto.getHtfkid());
		if (htfkqkDto_t != null) {
			if ("80".equals(htfkqkDto_t.getZt())) {
				//获取付款明细信息
				HtfkmxDto htfkmxDto = new HtfkmxDto();
				htfkmxDto.setHtfkid(htfkqkDto_t.getHtfkid());
				List<HtfkmxDto> htfkmxDtos = htfkmxService.getDtoList(htfkmxDto);
				List<HtglDto> htglDtos = new ArrayList<>();
				if (!CollectionUtils.isEmpty(htfkmxDtos)) {
					for (HtfkmxDto htfkmxDto_t : htfkmxDtos) {
						// 获取合同已付金额
						BigDecimal yfjenum = new BigDecimal(htfkmxDto_t.getYfje());
						// 获取付款金额
						BigDecimal fkjenum = new BigDecimal(htfkmxDto_t.getFkje());
						// 存入新的合同已付金额
						BigDecimal result = yfjenum.subtract(yfjenum).add(fkjenum).setScale(2, RoundingMode.HALF_UP);
						HtglDto htglDto = new HtglDto();
						htglDto.setYfje(result.toString());
						BigDecimal zjenum = new BigDecimal(htglDto.getZje()).setScale(2, RoundingMode.HALF_UP);
						if (StringUtil.isNotBlank(htglDto.getFpje())) {
							BigDecimal fpje = new BigDecimal(htglDto.getFpje()).setScale(2, RoundingMode.HALF_UP);
							// 判断已付金额是否大于合同总金额
							if (!(result.compareTo(zjenum) < 0 && zjenum.compareTo(fpje) < 0)) {
								// 更新完成标记
								htglDto.setWcbj("1");
							} else {
								htglDto.setWcbj("0");
							}
						}
						htglDto.setXgry(htfkqkDto_t.getXgry());
						htglDtos.add(htglDto);
					}
				}
				//更新合同付款信息和完成标记
				if (!CollectionUtils.isEmpty(htglDtos)) {
					return htglService.updateFkxxByList(htglDtos);
				}
			}
		}
		return true;
	}

	@Override
	public List<HtfkqkDto> getPagedAuditHtfkqk(HtfkqkDto htfkqkDto) {
		// 获取人员ID和履历号
		List<HtfkqkDto> t_sbyzList = dao.getPagedAuditHtfkqk(htfkqkDto);
		if (CollectionUtils.isEmpty(t_sbyzList))
			return t_sbyzList;

		List<HtfkqkDto> sqList = dao.getAuditListHtfkqk(t_sbyzList);

		commonService.setSqrxm(sqList);

		return sqList;
	}

	@Override
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
		HtfkqkDto htfkqkDto = (HtfkqkDto) baseModel;
		htfkqkDto.setXgry(operator.getYhid());
		//判断该合同下发票号码是否重复
		return updateFkqk(htfkqkDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
		if (shgcList == null || shgcList.isEmpty()) {
			return true;
		}
		for (ShgcDto shgcDto : shgcList) {
			boolean messageFlag = commonService.queryAuthMessage(operator.getYhid(),shgcDto.getShlb());
			String htfkid = shgcDto.getYwid();
			HtfkqkDto htfkqkDto_t = dao.getDtoById(htfkid);
			// 审核退回
			if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
				HtfkqkDto htfkqkDto = new HtfkqkDto();
				htfkqkDto.setXgry(operator.getYhid());
				htfkqkDto.setHtfkid(htfkid);
				htfkqkDto.setZt(StatusEnum.CHECK_NO.getCode());
				dao.update(htfkqkDto);
				//更新相关合同付款中金额
				HtfkmxDto htfkmxDto = new HtfkmxDto();
				htfkmxDto.setHtfkid(htfkid);
				List<HtfkmxDto> htfkmxDtos = htfkmxService.getDtoList(htfkmxDto);
				List<HtfkmxDto> t_list = new ArrayList<>();
				if (!CollectionUtils.isEmpty(htfkmxDtos)) {
					for (HtfkmxDto htfkmxDto_t : htfkmxDtos) {
						boolean sfcf = false;
						if (!CollectionUtils.isEmpty(t_list)) {
							for (HtfkmxDto htfkmxDto_s : t_list) {
								if (htfkmxDto_s.getHtid().equals(htfkmxDto_t.getHtid())) {
									//若合同ID相同则累计金额
									BigDecimal yfkzje = new BigDecimal(htfkmxDto_s.getFkzje() != null ? htfkmxDto_s.getFkzje() : "0");//原付款中金额
									BigDecimal xfkzje = yfkzje.subtract(new BigDecimal(htfkmxDto_t.getFkje() != null ? htfkmxDto_t.getFkje() : "0"));
									htfkmxDto_s.setFkzje(String.valueOf(xfkzje));
									sfcf = true;
								}
							}
						}
						if (!sfcf) {
							HtfkmxDto htfkmxDto_n = new HtfkmxDto();
							htfkmxDto_n.setHtid(htfkmxDto_t.getHtid());
							htfkmxDto_n.setHtfkid(htfkmxDto_t.getHtfkid());
							BigDecimal yfkzje = new BigDecimal(htfkmxDto_t.getFkzje() != null ? htfkmxDto_t.getFkzje() : "0");//原付款中金额
							BigDecimal xfkzje = yfkzje.subtract(new BigDecimal(htfkmxDto_t.getFkje() != null ? htfkmxDto_t.getFkje() : "0"));
							htfkmxDto_n.setFkzje(String.valueOf(xfkzje));
							t_list.add(htfkmxDto_n);
						}
					}
					//更新合同付款中金额
                    boolean result = htfkmxService.updateContractFkzje(t_list);
                    if (!result)
                        return false;
                }
				htfkqkDto_t.setXgry(operator.getYhid());
				htfkqkDto_t.setZt(StatusEnum.CHECK_UNPASS.getCode());
				//审核通过
			} else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
				htfkqkDto_t.setZt(StatusEnum.CHECK_PASS.getCode());
				//更新合同已付金额
				//获取付款明细信息
				HtfkmxDto htfkmxDto = new HtfkmxDto();
				htfkmxDto.setHtfkid(htfkqkDto_t.getHtfkid());
				List<HtfkmxDto> htfkmxDtos = htfkmxService.getDtoList(htfkmxDto);
				List<HtfkmxDto> htfkmxDtoList = htfkmxService.getListGroupByHt(htfkmxDto);
				List<HtglDto> htglDtos = new ArrayList<>();
				if (!CollectionUtils.isEmpty(htfkmxDtoList)) {
					for (HtfkmxDto htfkmxDto_t : htfkmxDtoList) {
						// 获取合同已付金额
						BigDecimal yfjenum = new BigDecimal(htfkmxDto_t.getYfje() != null ? htfkmxDto_t.getYfje() : "0");
						// 获取付款金额
						BigDecimal fkjenum = new BigDecimal(htfkmxDto_t.getFkje() != null ? htfkmxDto_t.getFkje() : "0");
						// 存入新的合同已付金额
                        HtglDto htglDto = new HtglDto();
						htglDto.setYfje(yfjenum.add(fkjenum).setScale(2, RoundingMode.HALF_UP).toString());
						htglDto.setZje(htfkmxDto_t.getZje());
						htglDto.setHtid(htfkmxDto_t.getHtid());
						BigDecimal zjenum = new BigDecimal(htglDto.getZje()).setScale(2, RoundingMode.HALF_UP);
						if (StringUtil.isNotBlank(htglDto.getFpje())) {
							BigDecimal fpje = new BigDecimal(htglDto.getFpje()).setScale(2, RoundingMode.HALF_UP);
							// 判断已付金额是否大于合同总金额
							if (!(yfjenum.add(fkjenum).setScale(2, RoundingMode.HALF_UP).compareTo(zjenum) < 0 && zjenum.compareTo(fpje) < 0)) {
								// 更新完成标记
								htglDto.setWcbj("1");
							} else {
								htglDto.setWcbj("0");
							}
						} else {
							htglDto.setWcbj("0");
						}
						htglDto.setXgry(htfkqkDto_t.getXgry());
						htglDtos.add(htglDto);
					}
				}
				//更新合同付款信息和完成标记
				if (!CollectionUtils.isEmpty(htglDtos)) {
					boolean updateHtfk = htglService.updateFkxxByList(htglDtos);
					if (!updateHtfk)
						return false;
				}

				//更新付款中金额
				//如果为提交审核第一步操作，则更新合同付款中金额
				List<HtfkmxDto> t_list = new ArrayList<>();
				if (!CollectionUtils.isEmpty(htfkmxDtos)) {
					for (HtfkmxDto htfkmxDto_t : htfkmxDtos) {
						boolean sfcf = false;
						if (!CollectionUtils.isEmpty(t_list)) {
							for (HtfkmxDto htfkmxDto_s : t_list) {
								if (htfkmxDto_s.getHtid().equals(htfkmxDto_t.getHtid())) {
									//若合同ID相同则累计金额
									BigDecimal yfkzje = new BigDecimal(htfkmxDto_s.getFkzje() != null ? htfkmxDto_s.getFkzje() : "0");//原付款中金额
									BigDecimal xfkzje = yfkzje.subtract(new BigDecimal(htfkmxDto_t.getFkje() != null ? htfkmxDto_t.getFkje() : "0"));
									htfkmxDto_s.setFkzje(String.valueOf(xfkzje));
									sfcf = true;
								}
							}
						}
						if (!sfcf) {
							HtfkmxDto htfkmxDto_n = new HtfkmxDto();
							htfkmxDto_n.setHtid(htfkmxDto_t.getHtid());
							htfkmxDto_n.setHtfkid(htfkmxDto_t.getHtfkid());
							BigDecimal yfkzje = new BigDecimal(htfkmxDto_t.getFkzje() != null ? htfkmxDto_t.getFkzje() : "0");//原付款中金额
							BigDecimal xfkzje = yfkzje.subtract(new BigDecimal(htfkmxDto_t.getFkje() != null ? htfkmxDto_t.getFkje() : "0"));
							htfkmxDto_n.setFkzje(String.valueOf(xfkzje));
							t_list.add(htfkmxDto_n);
						}
					}
					//更新合同付款中金额
                    boolean result = htfkmxService.updateContractFkzje(t_list);
                    if (!result)
                        return false;
                }
				//是否同步至每刻数据
				if ("1".equals(mkflag) && !CollectionUtils.isEmpty(htfkmxDtos)) {
					//组装报销单费用导入数据
					ExpenseRequestDto expenseRequestDto = new ExpenseRequestDto();
					expenseRequestDto.setEmployeeId(operator.getYhm());
					List<Expense> expenseList = new ArrayList<>();
					for (HtfkmxDto dto : htfkmxDtos) {
						Expense expense = new Expense();
						expense.setConsumeAmount(new ConsumeAmount(Double.parseDouble(dto.getFkje()), "CNY"));
						//是否对公费用
						expense.setCorpExpense(true);
						//行政费用19003；行政采购类别编码见群里附件；采购货款19002；
						expense.setExpenseTypeBizCode("19002");
						expense.setCorpType("ALL_RECEIPTS");
						//到票时间
						expense.setReceiptDate(new Date().getTime());
						//支付对象供应商业务编码
						expense.setTradingPartnerBizCode(htfkqkDto_t.getYwbm());
						//自定义字段 必传参数
						Map<String, Object> map = new HashMap<>();
						map.put("CF47", dto.getHtnbbh());
						expense.setCustomObject(map);
						expense.setComments(htfkqkDto_t.getBz());
						expenseList.add(expense);
					}
					expenseRequestDto.setExpenseList(expenseList);
					//导入费用成功后，将返回每刻系统内的唯一费用标识Code，该Code可用于报销单导入时，作为费用expenseCodes的参数使用
					List<String> expenseCodes = externalUtil.receivExpense(JSON.toJSONString(expenseRequestDto));
					if (CollectionUtils.isEmpty(expenseCodes)) {
						throw new BusinessException("msg", "每刻报销单费用导入失败！");
					}
					//组装报销单导入数据
					ReimburseDto reimburseDto = new ReimburseDto();
					reimburseDto.setFormCode(htfkqkDto_t.getFkdh());
					//单据类型编码
					reimburseDto.setFormSubTypeBizCode("FT16250966831532077");
					//申请人工号
					reimburseDto.setSubmittedUserEmployeeId(operator.getYhm());
					reimburseDto.setReimburseName(htfkqkDto_t.getFksy());
					//公司抬头业务编码
					reimburseDto.setLegalEntityBizCode(servicecode);
					//承担人工号
					reimburseDto.setCoverUserEmployeeId(operator.getYhm());
					//用款部门jgid
					reimburseDto.setCoverDepartmentBizCode(htfkqkDto_t.getYkbm());
					reimburseDto.setRequestDepartment(htfkqkDto_t.getSqbmmc());
					// 自定义字段 最晚支付日期
					Map<String, Object> customMap = new HashMap<>();
					try {
						Map<String, Object> dateMap = new HashMap<>();
						dateMap.put("currentTime", DateUtils.parse(htfkqkDto_t.getZwzfrq()).getTime());
						customMap.put("CF50", dateMap);
					} catch (Exception e) {
						log.error("最晚支付日期转换long失败");
					}
					reimburseDto.setCustomObject(customMap);
					PayeeAccount payeeAccount = new PayeeAccount();
					payeeAccount.setBankAcctName(htfkqkDto_t.getZffkhh());
					payeeAccount.setBankAcctNumber(htfkqkDto_t.getZffyhzh());
					// 支付类型 ALIPAY-支付宝,BANK-银行账户,CASH-现金
					payeeAccount.setPaymentType("BANK");
					// 账户性质 个人-PERSONAL,公司-CORP
					payeeAccount.setAccountType("CORP");
					reimburseDto.setPayeeAccount(payeeAccount);
					//导入费用成功后，将返回每刻系统内的唯一费用标识Code，该Code可用于报销单导入时，作为费用expenseCodes的参数使用
					reimburseDto.setExpenseCodes(expenseCodes);
					reimburseDto.setTradingPartnerBizCode(htfkqkDto_t.getYwbm());
					reimburseDto.setSubmittedTime(new Date().getTime());
					//是否校验其他数据 false为校验 true为不校验（只校验重要数据）
					reimburseDto.setStagingFlag(false);
					boolean isSuccess = externalUtil.receiveReimburse(JSON.toJSONString(reimburseDto));
					if (!isSuccess) {
						throw new BusinessException("msg", "每刻报销单导入失败！");
					}
				}
				if (!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos()) && messageFlag) {
					int size = shgcDto.getSpgwcyDtos().size();
					for (int i = 0; i < size; i++) {
						if (StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())) {
							talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(),
									xxglService.getMsg("ICOMM_SH00006"), xxglService.getMsg("ICOMM_SH00052",
											operator.getZsxm(), shgcDto.getShlbmc(),
											"合同付款申请", shgcDto.getShxx_shyj(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}

				//更新审核通过时间
				SimpleDateFormat tempDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String datetime = tempDate.format(new java.util.Date());
				HtfkqkDto htfkqkDto = new HtfkqkDto();
				htfkqkDto.setHtfkid(htfkid);
				htfkqkDto.setShtgsj(datetime);
				dao.update(htfkqkDto);
				//审核中
			} else {
				//String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
				htfkqkDto_t.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				HtfkmxDto htfkmxDto = new HtfkmxDto();
				htfkmxDto.setHtfkid(htfkqkDto_t.getHtfkid());
				List<HtfkmxDto> htfkmxDtos = htfkmxService.getDtoList(htfkmxDto);
				List<HtfkmxDto> htddslidList = htfkmxService.queryHtddslid(htfkmxDto);
				StringBuilder htfknbbhs = new StringBuilder();
				StringBuilder htfkwbbhs = new StringBuilder();
				List<String> glspd = new ArrayList<>();//关联审批单
				if (!CollectionUtils.isEmpty(htddslidList)) {
					for (HtfkmxDto htDdslids : htddslidList) {
						if (StringUtil.isNotBlank(htDdslids.getHtddslid())) {
							glspd.add(htDdslids.getHtddslid());
						}
					}
				}
				//如果为提交审核第一步操作，则更新合同付款中金额
				List<HtfkmxDto> t_list = new ArrayList<>();
				if ("1".equals(shgcDto.getXlcxh())) {
					if (!CollectionUtils.isEmpty(htfkmxDtos)) {
						for (HtfkmxDto htfkmxDto_t : htfkmxDtos) {
							htfknbbhs.append(",").append(htfkmxDto_t.getHtnbbh());
							htfkwbbhs.append(",").append(htfkmxDto_t.getHtwbbh());
							boolean sfcf = false;
							if (!CollectionUtils.isEmpty(t_list)) {
								for (HtfkmxDto htfkmxDto_s : t_list) {
									if (htfkmxDto_s.getHtid().equals(htfkmxDto_t.getHtid())) {
										//若合同ID相同则累计金额
										BigDecimal yfkzje = new BigDecimal(htfkmxDto_s.getFkzje() != null ? htfkmxDto_s.getFkzje() : "0");//原付款中金额
										BigDecimal xfkzje = yfkzje.add(new BigDecimal(htfkmxDto_t.getFkje() != null ? htfkmxDto_t.getFkje() : "0"));
										htfkmxDto_s.setFkzje(String.valueOf(xfkzje));
										sfcf = true;
									}
								}
							}
							if (!sfcf) {
								HtfkmxDto htfkmxDto_n = new HtfkmxDto();
								htfkmxDto_n.setHtid(htfkmxDto_t.getHtid());
								htfkmxDto_n.setHtfkid(htfkmxDto_t.getHtfkid());
								BigDecimal yfkzje = new BigDecimal(htfkmxDto_t.getFkzje() != null ? htfkmxDto_t.getFkzje() : "0");//原付款中金额
								BigDecimal xfkzje = yfkzje.add(new BigDecimal(htfkmxDto_t.getFkje() != null ? htfkmxDto_t.getFkje() : "0"));
								htfkmxDto_n.setFkzje(String.valueOf(xfkzje));
								t_list.add(htfkmxDto_n);
							}
						}
						htfknbbhs = new StringBuilder(htfknbbhs.substring(1));
						htfkwbbhs = new StringBuilder(htfkwbbhs.substring(1));
					}
					//更新合同付款中金额
					if (!CollectionUtils.isEmpty(t_list)) {
						boolean result = htfkmxService.updateContractFkzje(t_list);
						if (!result)
							return false;
					}
				}

				if ("1".equals(shgcDto.getXlcxh()) && shgcDto.getShxx() == null) {
					try {
						Map<String, Object> template = talkUtil.getTemplateCode(operator.getYhm(), "付款申请");//获取审批模板ID
						String templateCode = (String) template.get("message");
						//获取申请人信息(付款申请应该为采购部门)
						User user = new User();
						user.setYhid(shgcDto.getSqr());
						user = commonService.getUserInfoById(user);
						if (user == null)
							throw new BusinessException("ICOM99019", "未获取到申请人信息！");
						if (StringUtils.isBlank(user.getDdid()))
							throw new BusinessException("ICOM99019", "未获取到申请人钉钉ID！");
						String userid = user.getDdid();
						String dept = user.getJgid();
						Map<String, String> map = new HashMap<>();
						Map<String, String> t_map = new HashMap<>();
						List<Map<String, String>> mxlist = new ArrayList<>();
						map.put("费用归属", systemname);
						List<String> bms = new ArrayList<>();
						bms.add(htfkqkDto_t.getYkbm());
						map.put("用款部门", JSON.toJSONString(bms));
						map.put("付款事由", htfkqkDto_t.getFksy());
						map.put("关联审批单", glspd.isEmpty()?"":JSON.toJSONString(glspd));
						map.put("付款总额", htfkqkDto_t.getFkzje());
						map.put("单据传达方式", htfkqkDto_t.getDjcdfsmc());
						t_map.put("对应合同外部编号", htfkwbbhs.toString());
						t_map.put("该合同/订单内部编号", htfknbbhs.toString());
						t_map.put("付款金额（元）", htfkqkDto_t.getFkzje());
						t_map.put("付款方式", htfkqkDto_t.getFkfsmc());
						t_map.put("付款方", htfkqkDto_t.getFkfmc());
						t_map.put("最晚支付日期", htfkqkDto_t.getZwzfrq());
						t_map.put("支付对象", htfkqkDto_t.getZfdxmc());
						t_map.put("支付方开户行", htfkqkDto_t.getZffkhh());
						t_map.put("支付方银行账户", htfkqkDto_t.getZffyhzh());
						log.error("钉钉合同付款参数获取--用款部门:" + htfkqkDto_t.getYkbmmc() + ",关联审批单:" + JSON.toJSONString(glspd) + ",付款总额:" + htfkqkDto_t.getFkzje() + ",付款金额（元）:" + htfkmxDtos.get(0).getFkje()
								+ ",付款方式:" + htfkqkDto_t.getFkfsmc() + ",付款方:" + htfkqkDto_t.getFkfmc() + ",最晚支付日期:" + htfkqkDto_t.getZwzfrq() + ",支付对象:" + htfkqkDto_t.getZfdxmc());
						t_map.put("说明", "详细信息:" + applicationurl + urlPrefix + "/ws/payment/getPaymentUrl?htfkid=" + htfkqkDto_t.getHtfkid() + "&urlPrefix=" + urlPrefix);
						mxlist.add(t_map);
						Map<String, Object> result = talkUtil.createInstance(operator.getYhm(), templateCode, null, null, userid, dept, map, mxlist, null);
						String str = (String) result.get("message");
						String status = (String) result.get("status");
						if ("success".equals(status)) {
							@SuppressWarnings("unchecked")
							Map<String, Object> result_map = JSON.parseObject(str, Map.class);
							if (("0").equals(String.valueOf(result_map.get("errcode")))) {
								//保存至钉钉分布式管理表(主站)
								RestTemplate t_restTemplate = new RestTemplate();
								MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
								paramMap.add("ddslid", String.valueOf(result_map.get("process_instance_id")));
								paramMap.add("fwqm", urlPrefix);
								paramMap.add("cljg", "1");
								paramMap.add("fwqmc", systemname);
								paramMap.add("hddz", applicationurl);
								paramMap.add("spywlx", shgcDto.getShlb());

								//根据审批类型获取钉钉审批的业务类型，业务名称
								JcsjDto jcsjDto_dd = new JcsjDto();
								jcsjDto_dd.setCsdm(shgcDto.getAuditType());
								jcsjDto_dd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
								jcsjDto_dd = jcsjService.getDtoByCsdmAndJclb(jcsjDto_dd);
								if (StringUtil.isBlank(jcsjDto_dd.getCsmc()) || StringUtil.isBlank(jcsjDto_dd.getCskz1())) {
									throw new BusinessException("msg", "请设置" + shgcDto.getShlbmc() + "的钉钉审批回调类型基础数据！");
								}
								paramMap.add("ywmc", operator.getZsxm() + "提交的" + jcsjDto_dd.getCsmc());
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
								ddfbsglDto.setYwmc(operator.getZsxm() + "提交的" + jcsjDto_dd.getCsmc());
								ddfbsglDto.setWbcxid(operator.getWbcxid());
								boolean result_t = ddfbsglService.saveDistributedMsg(ddfbsglDto);
								if (!result_t)
									return false;
								//主站保留一份
								if (StringUtils.isNotBlank(registerurl)) {
									boolean b_result = t_restTemplate.postForObject(registerurl + "/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
									if (!b_result)
										return false;
								}
								//若钉钉审批提交成功，则关联审批实例ID
								htfkqkDto_t.setDdslid(String.valueOf(result_map.get("process_instance_id")));
								htfkqkDto_t.setXgry(operator.getYhid());
								dao.update(htfkqkDto_t);
							} else {
								throw new BusinessException("msg", "发起钉钉审批失败!错误信息:" + result.get("message"));
							}
						} else {
							throw new BusinessException("msg", "发起钉钉审批失败!错误信息:" + result.get("message"));
						}
					} catch (BusinessException e) {
						throw new BusinessException("msg", e.getMsg());
					}
				}

				//发送钉钉消息--取消审核人员
				if (!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos()) && messageFlag) {
					try {
						int size = shgcDto.getNo_spgwcyDtos().size();
						for (int i = 0; i < size; i++) {
							if (StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())) {
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
										shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"), xxglService.getMsg("ICOMM_SH00005", operator.getZsxm(), shgcDto.getShlbmc(), htfkqkDto_t.getHtnbbh(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
			}
			//获取下一步审核人员，保存至审核过程表
			if(StringUtil.isNotBlank(htfkqkDto_t.getDdslid())){
				String token = talkUtil.getDingTokenByUserId(operator.getYhm());
				Map<String, String> result_y = talkUtil.getProcessMessage(htfkqkDto_t.getDdslid(),token);
				Map<String, String> body=JSON.parseObject(result_y.get("body"),Map.class);
				Map<String,String> process_instance=JSON.parseObject(JSON.toJSONString(body.get("process_instance")),Map.class);
				List<Map> taskArray= JSON.parseArray(JSON.toJSONString(process_instance.get("tasks")),Map.class);

				for(int i=0;i<taskArray.size();i++){
					Map<String,String> job =taskArray.get(i);
					if("RUNNING".equals(job.get("task_status"))){
						User user=commonService.getYhid(job.get("userid"));
						shgcDto.setDqshr(user.getZsxm());
					}
				}
			}
			dao.update(htfkqkDto_t);
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
				String htfkid = shgcDto.getYwid();
				HtfkqkDto htfkqkDto = new HtfkqkDto();
				htfkqkDto.setXgry(operator.getYhid());
				htfkqkDto.setHtfkid(htfkid);
				htfkqkDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				dao.update(htfkqkDto);
			}
		} else {
			// 审核回调方法
			for (ShgcDto shgcDto : shgcList) {
				String htfkid = shgcDto.getYwid();
				HtfkqkDto htfkqkDto = new HtfkqkDto();
				htfkqkDto.setXgry(operator.getYhid());
				htfkqkDto.setHtfkid(htfkid);
				htfkqkDto.setZt(StatusEnum.CHECK_NO.getCode());
				dao.update(htfkqkDto);
				//更新相关合同付款中金额
				HtfkmxDto htfkmxDto = new HtfkmxDto();
				htfkmxDto.setHtfkid(htfkid);
				List<HtfkmxDto> htfkmxDtos = htfkmxService.getDtoList(htfkmxDto);
				List<HtfkmxDto> t_list = new ArrayList<>();
				if (!CollectionUtils.isEmpty(htfkmxDtos)) {
					for (HtfkmxDto htfkmxDto_t : htfkmxDtos) {
						boolean sfcf = false;
						if (!CollectionUtils.isEmpty(t_list)) {
							for (HtfkmxDto htfkmxDto_s : t_list) {
								if (htfkmxDto_s.getHtid().equals(htfkmxDto_t.getHtid())) {
									//若合同ID相同则累计金额
									BigDecimal yfkzje = new BigDecimal(htfkmxDto_s.getFkzje() != null ? htfkmxDto_s.getFkzje() : "0");//原付款中金额
									BigDecimal xfkzje = yfkzje.subtract(new BigDecimal(htfkmxDto_t.getFkje() != null ? htfkmxDto_t.getFkje() : "0"));
									htfkmxDto_s.setFkzje(String.valueOf(xfkzje));
									sfcf = true;
								}
							}
						}
						if (!sfcf) {
							HtfkmxDto htfkmxDto_n = new HtfkmxDto();
							htfkmxDto_n.setHtid(htfkmxDto_t.getHtid());
							htfkmxDto_n.setHtfkid(htfkmxDto_t.getHtfkid());
							BigDecimal yfkzje = new BigDecimal(htfkmxDto_t.getFkzje() != null ? htfkmxDto_t.getFkzje() : "0");//原付款中金额
							BigDecimal xfkzje = yfkzje.subtract(new BigDecimal(htfkmxDto_t.getFkje() != null ? htfkmxDto_t.getFkje() : "0"));
							htfkmxDto_n.setFkzje(String.valueOf(xfkzje));
							t_list.add(htfkmxDto_n);
						}
					}
					//更新合同付款中金额
                    boolean result = htfkmxService.updateContractFkzje(t_list);
                    if (!result)
                        return false;
                }
				//OA取消审批的同时组织钉钉审批
				HtfkqkDto t_htfkqkDto = dao.getDtoById(htfkqkDto.getHtfkid());
				if (t_htfkqkDto != null && StringUtils.isNotBlank(t_htfkqkDto.getDdslid())) {
					Map<String, Object> cancelResult = talkUtil.cancelDingtalkAudit(operator.getYhm(), t_htfkqkDto.getDdslid(), "", operator.getDdid());
					//若撤回成功将实例ID至为空
					String success = String.valueOf(cancelResult.get("message"));
					@SuppressWarnings("unchecked")
					Map<String, Object> result_map = JSON.parseObject(success, Map.class);
					boolean bo1 = (boolean) result_map.get("success");
					if (bo1)
						dao.updateDdslidToNull(t_htfkqkDto);
				}
			}
		}
		return true;
	}

	/**
	 * 合同付款列表（查询审核状态）
	 */
	@Override
	public List<HtfkqkDto> getPagedDtoList(HtfkqkDto htfkqkDto) {
		List<HtfkqkDto> list = dao.getPagedDtoList(htfkqkDto);
		try {
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_CONTRACT_PAYMENT.getCode(), "zt", "htfkid",
					new String[]{StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return list;
	}

	/**
	 * 钉钉合同付款审批回调
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean callbackHtfkqkAduit(JSONObject obj, HttpServletRequest request, Map<String, Object> t_map) throws BusinessException {
		HtfkqkDto htfkqkDto = new HtfkqkDto();
		String result = obj.getString("result");// 正常结束时result为agree，拒绝时result为refuse，审批终止时没这个值，redirect转交
		String type = obj.getString("type");// 审批正常结束（同意或拒绝）的type为finish，审批终止的type为terminate
		String processInstanceId = obj.getString("processInstanceId");// 审批实例id
		String staffId = obj.getString("staffId");// 审批人钉钉ID
		String remark = obj.getString("remark");// 审核意见
		String content = obj.getString("content");//评论
		String title = obj.getString("title");
		String processCode = obj.getString("processCode");
		String finishTime = obj.getString("finishTime");// 审批完成时间
		String wbcxidString = obj.getString("wbcxid"); //获取外部程序id
		log.error("回调参数获取---------result:" + result + ",type:" + type + ",processInstanceId:" + processInstanceId + ",staffId:" + staffId + ",remark:" + remark + ",finishTime" + finishTime + ",外部程序id" + wbcxidString);
		DdfbsglDto ddfbsglDto = new DdfbsglDto();
		ddfbsglDto.setProcessinstanceid(processInstanceId);
		ddfbsglDto = ddfbsglService.getDtoById(processInstanceId);
		DdspglDto ddspglDto = new DdspglDto();
		DdspglDto t_ddspglDto = new DdspglDto();
		t_ddspglDto.setProcessinstanceid(processInstanceId);
		t_ddspglDto.setType("finish");
		t_ddspglDto.setEventtype(DingTalkUtil.BPMS_TASK_CHANGE);
		List<DdspglDto> ddspgllist = ddspglService.getDtoList(t_ddspglDto);
		try {
			if (ddfbsglDto == null)
				throw new BusinessException("message", "未获取到相应的钉钉分布式信息！");
			if (StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {
				if (!CollectionUtils.isEmpty(ddspgllist)) {
					ddspglDto = ddspgllist.get(0);
				}
			}
			htfkqkDto.setDdslid(processInstanceId);
			// 根据钉钉审批实例ID查询关联请购单
			htfkqkDto = dao.getDtoByDdslid(htfkqkDto);
			// 若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
			if (htfkqkDto != null) {
				//获取审批人信息
				User user = new User();
				user.setDdid(staffId);
				user.setWbcxid(wbcxidString);
				user = commonService.getByddwbcxid(user);
				if (user == null)
					return false;
				log.error("user-yhid:" + user.getYhid() + "---user-zsxm:" + user.getZsxm() + "----user-yhm"
						+ user.getYhm() + "---user-ddid" + user.getDdid());
				// 获取审批人角色信息
				List<HtglDto> dd_sprjs = htglService.getSprjsBySprid(user.getYhid());
				// 获取当前审核过程
				ShgcDto t_shgcDto = shgcService.getDtoByYwid(htfkqkDto.getHtfkid());
				if (t_shgcDto != null) {
					// 获取的审核流程列表
					ShlcDto shlcParam = new ShlcDto();
					shlcParam.setShid(t_shgcDto.getShid());
					shlcParam.setGcid(t_shgcDto.getGcid());// 处理旧流程判断用
					@SuppressWarnings("unused")
					List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);

					if (("start").equals(type)) {
						//更新分布式管理表信息
						DdfbsglDto t_ddfbsglDto = new DdfbsglDto();
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
						shxxDto.setYwid(htfkqkDto.getHtfkid());
						if (StringUtil.isNotBlank(finishTime)) {
							shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
						}
						log.error("shxxDto-lcxh-" + t_shgcDto.getXlcxh() + "shxxDto-shlb"
								+ t_shgcDto.getShlb() + "shxxDto-shyj" + remark + "shxxDto-ywid"
								+ htfkqkDto.getHtfkid());
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
								log.error("DingTalkMaterPurchaseAudit(钉钉合同付款审批转发提醒)------转发人员:" + user.getZsxm()
										+ ",人员ID:" + user.getYhid() + ",外部编号:" + htfkqkDto.getHtwbbh() + ",单据ID:"
										+ htfkqkDto.getHtfkid() + ",转发时间:" + date);
								return true;
							}
							// 调用审核方法
							Map<String, List<String>> map = ToolUtil.reformRequest(request);
							List<String> list = new ArrayList<>();
							list.add(htfkqkDto.getHtfkid());
							map.put("htfkid", list);
							for (int i = 0; i < dd_sprjs.size(); i++) {
								try {
									//取下一个角色
									user.setDqjs(dd_sprjs.get(i).getSprjsid());
									user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
									shxxDto.setYwids(new ArrayList<>());
									shgcService.doManyBackAudit(shxxDto, user, request, lastlcxh, obj);
									//更新审批管理信息
									ddspglDto.setCljg("1");
									ddspglService.updatecljg(ddspglDto);
									break;
								} catch (Exception e) {
									t_map.put("ddspglid", ddspglDto.getDdspglid());
									t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());
									if (i == dd_sprjs.size() - 1)
										throw new BusinessException("msg", e.getMessage());
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
										User user_t=commonService.getYhid(job.get("userid"));
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
							HtfkqkDto t_htfkqkDto = new HtfkqkDto();
							t_htfkqkDto.setHtfkid(htfkqkDto.getHtfkid());
							dao.updateDdslidToNull(t_htfkqkDto);
							log.error("撤销------");
							shxxDto.setSftg("0");
							// 调用审核方法
							Map<String, List<String>> map = ToolUtil.reformRequest(request);
							List<String> list = new ArrayList<>();
//								list.add(htfkqkDto.getHtid());
							map.put("htfkid", list);
							for (int i = 0; i < dd_sprjs.size(); i++) {
								try {
									//取下一个角色
									user.setDqjs(dd_sprjs.get(i).getSprjsid());
									user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
									shxxDto.setYwids(new ArrayList<>());
									shgcService.terminateAudit(shxxDto, user, request, lastlcxh, obj);
									//更新审批管理信息
									ddspglDto.setCljg("1");
									ddspglService.updatecljg(ddspglDto);
									break;
								} catch (Exception e) {
									t_map.put("ddspglid", ddspglDto.getDdspglid());
									t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());
									if (i == dd_sprjs.size() - 1)
										throw new BusinessException("msg", e.toString());
								}
							}
						} else if (("comment").equals(type)) {
							//评论时保存评论信息，添加至审核信息表
							ShgcDto shgc = shgcService.getDtoByYwid(shxxDto.getYwid());//获得已有审核过程
							ShxxDto shxx = new ShxxDto();
							String shxxid = StringUtil.generateUUID();
							shxx.setShxxid(shxxid);
							shxx.setSqr(shgc.getSqr());
							shxx.setLcxh(null);
							shxx.setShid(shgc.getShid());
							shxx.setSftg("1");
							shxx.setGcid(shgc.getGcid());
							shxx.setYwid(shxxDto.getYwid());
							shxx.setShyj("评论:" + content.substring(0, Math.min(content.length(), 6000)));//这里写死，备注钉钉审批被提交人员撤销了
							shxx.setLrry(user.getYhid());
							shxxService.insert(shxx);
						}
					}
				} else {
					if (("comment").equals(type)) {
						//评论时保存评论信息，添加至审核信息表
						ShxxDto shxx = new ShxxDto();
						String shxxid = StringUtil.generateUUID();
						shxx.setShxxid(shxxid);
						shxx.setSftg("1");
						shxx.setYwid(htfkqkDto.getHtfkid());
						shxx.setShlb(AuditTypeEnum.AUDIT_CONTRACT_PAYMENT.getCode());
						List<ShxxDto> shxxlist = shxxService.getShxxOrderByPass(shxx);
						if (!CollectionUtils.isEmpty(shxxlist)) {
							shxx.setShid(shxxlist.get(0).getShid());
							shxx.setShyj("评论:" + content.substring(0, Math.min(content.length(), 6000)));//这里写死，备注钉钉审批被提交人员撤销了
							shxx.setLrry(user.getYhid());
							shxxService.insert(shxx);
						}
					}
				}
			}
		} catch (BusinessException e) {
			log.error(e.getMessage());
			throw new BusinessException("msg", e.getMsg());
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BusinessException("msg", e.toString());
		}

		return true;
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
		return null;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
		Map<String, Object> map = new HashMap<>();
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>) param.get("ywids");
		HtfkqkDto htfkqkDto = new HtfkqkDto();
		htfkqkDto.setIds(ids);
		List<HtfkqkDto> dtoList = dao.getDtoList(htfkqkDto);
		List<String> list = new ArrayList<>();
		if (!CollectionUtils.isEmpty(dtoList)) {
			for (HtfkqkDto dto : dtoList) {
				list.add(dto.getHtfkid());
			}
		}
		map.put("list", list);
		return map;
	}

	/**
	 * 自动生成付款单号

	 */
	@Override
	public String generatePayDjh(HtfkqkDto HtfkqkDto) {
		// TODO Auto-generated method stub
		// 生成规则: FK-2020-1022-01
		String year = DateUtils.getCustomFomratCurrentDate("yyyy");
		String date = DateUtils.getCustomFomratCurrentDate("MMdd");
		String prefix;
		String serial;
		prefix = "FK-" + year + "-" + date + "-";
		serial = dao.getDjhSerial(prefix);
		return prefix + serial;
	}

	/**
	 * 删除合同付款信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean delHtfkqk(HtfkqkDto htfkqkDto) throws BusinessException {
		//查询删除信息
		HtfkmxDto htfkmxDto = new HtfkmxDto();
		htfkmxDto.setIds(htfkqkDto.getIds());
		List<HtfkmxDto> list = htfkmxService.getDtoList(htfkmxDto);
		//要更新得合同信息
		List<HtglDto> htglList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(list)) {
			for (HtfkmxDto htfkmx : list) {
				HtglDto htgl = new HtglDto();
				//付款金额
				BigDecimal fkje = StringUtil.isNotBlank(htfkmx.getFkje()) ? new BigDecimal(htfkmx.getFkje()) : new BigDecimal("0");
				//判断付款单状态，审核通过得修改合同已付金额
				if (StatusEnum.CHECK_PASS.getCode().equals(htfkmx.getZt())) {
					BigDecimal yfje = StringUtil.isNotBlank(htfkmx.getYfje()) ? new BigDecimal(htfkmx.getYfje()) : new BigDecimal("0");
					htgl.setYfje(yfje.subtract(fkje).toString());
					htgl.setHtid(htfkmx.getHtid());
					htglList.add(htgl);
					//审核中修改付款中金额
				} else if (StatusEnum.CHECK_SUBMIT.getCode().equals(htfkmx.getZt())) {
					BigDecimal fkzje = StringUtil.isNotBlank(htfkmx.getFkzje()) ? new BigDecimal(htfkmx.getFkzje()) : new BigDecimal("0");
					htgl.setFkzje(fkzje.subtract(fkje).toString());
					htgl.setHtid(htfkmx.getHtid());
					htglList.add(htgl);
				}
			}
		}

		//删除付款明细
		HtfkmxDto htfkmxDto_t = new HtfkmxDto();
		htfkmxDto_t.setIds(htfkqkDto.getIds());
		htfkmxDto_t.setScry(htfkqkDto.getScry());
		boolean result = htfkmxService.delete(htfkmxDto_t);
		if (!result)
			throw new BusinessException("msg", "付款明细删除失败！");

		//删除付款单
		result = delete(htfkqkDto);
		if (!result)
			throw new BusinessException("msg", "付款单删除失败!");

		//修改合同付款提醒
		HtfktxDto htfktxDto = new HtfktxDto();
		htfktxDto.setIds(htfkqkDto.getIds());
		result = htfktxService.updateHtfkidToNull(htfktxDto);

		//修改合同信息
		if (!CollectionUtils.isEmpty(htglList)) {
			result = htglService.updateHtglDtos(htglList);
			if (!result)
				throw new BusinessException("msg", "合同信息修改失败!");
		}
		return result;
	}

	/**
	 * 导出
	 */
	@Override
	public int getCountForSearchExp(HtfkqkDto htfkqkDto, Map<String, Object> params) {
		return dao.getCountForSearchExp(htfkqkDto);
	}

	/**
	 * 根据搜索条件获取导出信息
	 */
	public List<HtfkqkDto> getListForSearchExp(Map<String, Object> params) {
		HtfkqkDto htfkqkDto = (HtfkqkDto) params.get("entryData");
		queryJoinFlagExport(params, htfkqkDto);
		return dao.getListForSearchExp(htfkqkDto);
	}

	/**
	 * 根据选择信息获取导出信息
	 */
	public List<HtfkqkDto> getListForSelectExp(Map<String, Object> params) {
		HtfkqkDto htfkqkDto = (HtfkqkDto) params.get("entryData");
		queryJoinFlagExport(params, htfkqkDto);
		return dao.getListForSelectExp(htfkqkDto);
	}

	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String, Object> params, HtfkqkDto htfkqkDto) {
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
		htfkqkDto.setSqlParam(sqlcs);
	}
}
