package com.matridx.igams.production.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matridx.igams.production.dao.entities.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.matridxsql.IPU_AppVouchDao;
import com.matridx.igams.production.dao.post.IQgqxglDao;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.production.service.svcinterface.IQgqxglService;
import com.matridx.igams.production.service.svcinterface.IQgqxmxService;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.springframework.util.CollectionUtils;

@Service
public class QgqxglServiceImpl extends BaseBasicServiceImpl<QgqxglDto, QgqxglModel, IQgqxglDao> implements IQgqxglService,IAuditService{

	@Autowired
	IQgqxmxService qgqxmxService;
	
	@Autowired
	IQgmxService qgmxService;
	
	@Autowired
	ICommonService commonservice;
	
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	DingTalkUtil talkUtil;
	
	@Autowired
	IShgcService shgcService;

	
	@Autowired
	IQgglService qgglService;
	
	@Autowired
	IPU_AppVouchDao pu_appvouchDao;
	
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    //是否发送rabbit标记     1：发送
  	@Value("${matridx.rabbit.systemconfigflg:}")
  	private String systemconfigflg;
  	@Value("${matridx.rabbit.preflg:}")
  	private String preRabbitFlg;
  	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
  	@Value("${matridx.rabbit.systemreceiveflg:}")
	private String systemreceiveflg;
  	
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	private final Logger log = LoggerFactory.getLogger(QgqxglServiceImpl.class);

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(QgqxglDto qgqxglDto) {
		if(StringUtil.isBlank(qgqxglDto.getQgqxid())) {
			qgqxglDto.setQgqxid(StringUtil.generateUUID());
		}		
		qgqxglDto.setZt(StatusEnum.CHECK_NO.getCode());
		int result=dao.insert(qgqxglDto);
		return result > 0;
	}
	
	/**
	 * 请购取消管理保存
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addSavePurchaseCancel(QgqxglDto qgqxglDto) {
		//添加请购取消管理表
		boolean addQgqxgl=insertDto(qgqxglDto);
		if(addQgqxgl) {
			List<QgqxmxDto> qgqxmxlist= JSON.parseArray(qgqxglDto.getQgqxmx_json(), QgqxmxDto.class);
			if(!CollectionUtils.isEmpty(qgqxmxlist)) {
				List<QgqxmxDto> t_qgqxmxlist= new ArrayList<>();
				for (QgqxmxDto dto : qgqxmxlist) {
					if (StringUtils.isNotBlank(dto.getQxsl()) && !"0".equals(dto.getQxsl())) {
						QgqxmxDto qgqxmxDto = new QgqxmxDto();
						qgqxmxDto.setQgqxmxid(StringUtil.generateUUID());
						qgqxmxDto.setLrry(qgqxglDto.getLrry());
						qgqxmxDto.setQgqxid(qgqxglDto.getQgqxid());
						qgqxmxDto.setQgmxid(dto.getQgmxid());
						qgqxmxDto.setQgid(qgqxglDto.getQgid());
						qgqxmxDto.setWlid(dto.getWlid());
						qgqxmxDto.setQxsl(dto.getQxsl());
						qgqxmxDto.setBz(dto.getBz());
						qgqxmxDto.setZt(dto.getZt());
						t_qgqxmxlist.add(qgqxmxDto);
					}
				}
				//添加请购取消明细
				boolean addQgqxmx=qgqxmxService.insertDtoList(t_qgqxmxlist);
				if(addQgqxmx) {
					return true;
				}
			}
		}
		return true;
	}
	
	/**
	 * 请购取消管理修改保存
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean modSavePurchaseCancel(QgqxglDto qgqxglDto) {
		//添加请购取消管理表
		int addQgqxgl=dao.update(qgqxglDto);
		if(addQgqxgl>0) {
			List<QgqxmxDto> qgqxmxlist= JSON.parseArray(qgqxglDto.getQgqxmx_json(), QgqxmxDto.class);
			if(!CollectionUtils.isEmpty(qgqxmxlist)) {
				List<QgqxmxDto> t_qgqxmxlist= new ArrayList<>();
				for (QgqxmxDto dto : qgqxmxlist) {
					QgqxmxDto qgqxmxDto = new QgqxmxDto();
					qgqxmxDto.setQgqxmxid(dto.getQgqxmxid());
					qgqxmxDto.setXgry(qgqxglDto.getXgry());
					qgqxmxDto.setQgqxid(qgqxglDto.getQgqxid());
					qgqxmxDto.setQgmxid(dto.getQgmxid());
					qgqxmxDto.setQgid(qgqxglDto.getQgid());
					qgqxmxDto.setWlid(dto.getWlid());
					qgqxmxDto.setQxsl(dto.getQxsl());
					qgqxmxDto.setBz(dto.getBz());
					qgqxmxDto.setZt(dto.getZt());
					t_qgqxmxlist.add(qgqxmxDto);
				}
				//添加请购取消明细
				return qgqxmxService.updateDtoList(t_qgqxmxlist);
			}
		}else {
			return false;
		}
		return false;
	}
	
	/**
	 * 审核列表
	 */
	@Override
	public List<QgqxglDto> getPagedAuditQgqxgl(QgqxglDto qgqxglDto) {
		// TODO Auto-generated method stub
		List<QgqxglDto> t_sbyzList=dao.getPagedAuditQgqxgl(qgqxglDto);
		if(CollectionUtils.isEmpty(t_sbyzList)) 
			return t_sbyzList;
		
		List<QgqxglDto> sqList=dao.getAuditListQgqxgl(t_sbyzList);
		
		commonservice.setSqrxm(sqList);
		
		return sqList;
	}
	
	public boolean checkPrechaseCancel(QgqxglDto qgqxglDto, AuditParam auditParam) throws BusinessException {
		List<QgqxmxDto> qgqxmxlist= JSON.parseArray(qgqxglDto.getQgqxmx_json(), QgqxmxDto.class);
		if(!CollectionUtils.isEmpty(qgqxmxlist)) {
			QgqxmxDto qgqxmxDto = new QgqxmxDto();
			qgqxmxDto.setQgqxid(qgqxglDto.getQgqxid());
			List<QgqxmxDto> cancelmxlist = qgqxmxService.getQgqxmxCancelList(qgqxmxDto);
			for(int i=0;i<qgqxmxlist.size();i++) {
				String qxsl=qgqxmxlist.get(i).getQxsl();//取消数量
				float i_qxsl = (float) 0;
				if(StringUtil.isNotBlank(qxsl))
					i_qxsl = Float.parseFloat(qxsl);
				QgqxmxDto t_qgqxmxDto = cancelmxlist.get(i);
				if(i_qxsl > (Float.parseFloat(t_qgqxmxDto.getSysl()) - Float.parseFloat(t_qgqxmxDto.getYdsl()) -Double.parseDouble(t_qgqxmxDto.getZqxsl())))
				{
					throw new BusinessException("ICOM99019","取消数量超过实际可取消数量！");
				}
				/*
				//查询审核流程等于或大于当前审核的其他审核中的取消请购明细信息(根据qgid和qgmxid限制)
				List<QgqxmxDto> qtqgqxmxlist=qgqxmxService.getOtherQxqgList(qgqxmxlist.get(i));
				if(qtqgqxmxlist!=null && qtqgqxmxlist.size()>0) {
					ShgcDto shgcDto=shgcService.getDtoByYwid(qtqgqxmxlist.get(0).getQgqxid());//查询该明细所在取消请购单审核流程信息
					for(int j=0;j<qtqgqxmxlist.size();j++) {
						if(shgcDto!=null) {
							//如果现审核流程大于等于当前审核流程,并且审核为通过时，则将取消数量加上
							if(StringUtils.isNotBlank(shgcDto.getXlcxh())) {
								if(Integer.parseInt(shgcDto.getXlcxh())>=Integer.parseInt(auditParam.getShgcDto().getXlcxh()) && "1".equals(auditParam.getShgcDto().getShxx().getSftg())) {
									Double sjqxsl=Double.parseDouble(qxsl)+Double.parseDouble(qtqgqxmxlist.get(j).getQxsl());//实际需取消数量
									if(sjqxsl>Double.parseDouble(kqxsl)) {
										WlglDto wlglDto=wlglService.getDtoById(qgqxmxlist.get(i).getWlid());
										if(wlglDto!=null)
											throw new BusinessException("ICOM99019",wlglDto.getWlmc()+"取消数量超过实际可取消数量！");
										throw new BusinessException("ICOM99019","取消数量超过实际可取消数量！未找到物料信息！");
									}
								}
							}else {
								if(Double.parseDouble(qxsl)>Double.parseDouble(kqxsl) && "1".equals(auditParam.getShgcDto().getShxx().getSftg())) {
									WlglDto wlglDto=wlglService.getDtoById(qgqxmxlist.get(i).getWlid());
									if(wlglDto!=null)
										throw new BusinessException("ICOM99019",wlglDto.getWlmc()+"取消数量超过实际可取消数量！");
									throw new BusinessException("ICOM99019","取消数量超过实际可取消数量！未找到物料信息！");
								}
							}
						}
					}
				}else {
					if(Double.parseDouble(qxsl)>Double.parseDouble(kqxsl) && "1".equals(auditParam.getShgcDto().getShxx().getSftg())) {
						WlglDto wlglDto=wlglService.getDtoById(qgqxmxlist.get(i).getWlid());
						if(wlglDto!=null)
							throw new BusinessException("ICOM99019",wlglDto.getWlmc()+"取消数量超过实际可取消数量！");
						throw new BusinessException("ICOM99019","取消数量超过实际可取消数量！未找到物料信息！");
					}
				}
				*/
			}
		}
		return true;
	}

	@Override
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
		// TODO Auto-generated method stub
		QgqxglDto qgqxglDto = (QgqxglDto)baseModel;
		qgqxglDto.setXgry(operator.getYhid());
		ShgcDto shgcDto = auditParam.getShgcDto();
		qgqxglDto.setQgqxid(shgcDto.getYwid());
		//校验可取消数量是否足够(要减去正在审核中的数量)
//		boolean checkqxsl = checkPrechaseCancel(qgqxglDto,auditParam);
		boolean isSuccess = modSavePurchaseCancel(qgqxglDto);
		if("1".equals(systemconfigflg) && isSuccess) {
        	qgqxglDto.setPrefixFlg(prefixFlg);
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qgqxgl.updateQgqxgl",JSONObject.toJSONString(qgqxglDto));
		}
		return isSuccess;
	}

	@Override
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		// TODO Auto-generated method stub
		if (shgcList == null || shgcList.isEmpty()) {
			return true;
		}
//		String token = talkUtil.getToken();
		String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
		String ICOMM_SH00004 = xxglService.getMsg("ICOMM_SH00004");
		String ICOMM_SH00005 = xxglService.getMsg("ICOMM_SH00005");
		String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
		for (ShgcDto shgcDto : shgcList) {
			QgqxglDto qgqxglDto = new QgqxglDto();

			qgqxglDto.setQgqxid(shgcDto.getYwid());
			
			qgqxglDto.setXgry(operator.getYhid());
			QgqxglDto qgqxglDto_t = getDtoById(qgqxglDto.getQgqxid());
			shgcDto.setSqbm(qgqxglDto_t.getSqbm());
			List<SpgwcyDto> spgwcyDtos = commonservice.siftJgList(shgcDto.getSpgwcyDtos(), qgqxglDto_t.getSqbm());
			// 审核退回
			if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
				qgqxglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				// 发送钉钉消息
				if (!CollectionUtils.isEmpty(spgwcyDtos)) {
					for (SpgwcyDto spgwcyDto : spgwcyDtos) {
						if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
							talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00026,
									xxglService.getMsg("ICOMM_SH00041", operator.getZsxm(), shgcDto.getShlbmc(),
											qgqxglDto_t.getDjh(), qgqxglDto_t.getYwlxmc(), qgqxglDto_t.getSqbmmc(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
				// 审核通过
			} else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
				//审核通过时，请购明细sl字段和sysl字段减去取消请购数量
				QgqxglDto t_qgqxglDto=new QgqxglDto();
				t_qgqxglDto.setQgid(qgqxglDto_t.getQgid());
				t_qgqxglDto.setQgqxid(qgqxglDto_t.getQgqxid());
				boolean isSuccess = qgqxmxService.updateQgmxByList(t_qgqxglDto);
				if("1".equals(systemconfigflg) && isSuccess) {
					t_qgqxglDto.setPrefixFlg(prefixFlg);
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qgqxmx.updateQgmxByList",JSONObject.toJSONString(qgqxglDto));
				}
				//
				QgmxDto qgmxDto=new QgmxDto();
				qgmxDto.setQgid(qgqxglDto_t.getQgid());
				List<QgmxDto> qgmxlist=qgmxService.getQgmxList(qgmxDto);
				if(!CollectionUtils.isEmpty(qgmxlist)) {
					boolean sfgxwcbj=true;//是否更新请购单完成标记
					for (QgmxDto dto : qgmxlist) {
						if (!"1".equals(dto.getQxqg())) {
							if (Float.parseFloat(dto.getSl()) == 0) {//更新取消请购标记
								boolean result = qgmxService.updateQxqg(dto.getQgmxid());
								if ("1".equals(systemconfigflg) && result) {
									QgqxmxDto qgqxmxDto = new QgqxmxDto();
									qgqxmxDto.setQgmxid(dto.getQgmxid());
									qgqxmxDto.setPrefixFlg(prefixFlg);
									amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qgqxmx.updateQxqg", JSONObject.toJSONString(qgqxmxDto));
								}
							}
						}
						if (Float.parseFloat(dto.getSysl()) != 0) {
							sfgxwcbj = false;
						}
					}
					if(sfgxwcbj) {
						QgglDto qgglDto=new QgglDto();
						List<String> ids=new ArrayList<>();
						ids.add(qgqxglDto_t.getQgid());
						qgglDto.setIds(ids);
						qgglDto.setXgry(operator.getYhid());
						qgglDto.setWcbj("1");
						boolean isSuccess2 = qgglService.updateWcbjs(qgglDto);
						if("1".equals(systemconfigflg) && isSuccess2) {
							qgglDto.setPrefixFlg(prefixFlg);
							amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qggl.updateWcbjs",JSONObject.toJSONString(qgglDto));
						}
					}
				}
				qgqxglDto.setZt(StatusEnum.CHECK_PASS.getCode());
				// 发送钉钉消息
				if (!CollectionUtils.isEmpty(spgwcyDtos)) {
					for (SpgwcyDto spgwcyDto : spgwcyDtos) {
						if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
							talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(), spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00003,
									xxglService.getMsg("ICOMM_SH00042", operator.getZsxm(), shgcDto.getShlbmc(),
											qgqxglDto_t.getDjh(), qgqxglDto_t.getYwlxmc(), qgqxglDto_t.getSqbmmc(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
				
				//更新U8请购数量
				if(!"1".equals(systemreceiveflg)) {
					QgmxDto t_qgmxDto=new QgmxDto();
					t_qgmxDto.setQgid(qgqxglDto_t.getQgid());
					List<QgmxDto> qgmxDtos=qgmxService.getQgmxList(t_qgmxDto);
					if(!CollectionUtils.isEmpty(qgmxDtos)) {
						for (QgmxDto dto : qgmxDtos) {
							PU_AppVouchsDto pu_appvouchsDto = new PU_AppVouchsDto();
							pu_appvouchsDto.setcInvCode(dto.getWlbm());
							pu_appvouchsDto.setAutoID(dto.getGlid());
							pu_appvouchsDto.setfQuantity(dto.getSl());
							if (StringUtil.isNotBlank(dto.getGlid())) {
								boolean result = pu_appvouchDao.updatePUAppVouchs(pu_appvouchsDto);
								if (!result)
									throw new BusinessException("ICOM99019", dto.getWlmc() + ",U8请购明细数量更新失败！");
							}
						}
					}
				}
				// 审核中
			} else {
				QgqxmxDto qgqxmxDto=new QgqxmxDto();
				qgqxmxDto.setQgqxid(qgqxglDto_t.getQgqxid());
				List<QgqxmxDto> qgqxmxlist=qgqxmxService.getQgqxmxCancelList(qgqxmxDto);
				if(!CollectionUtils.isEmpty(qgqxmxlist)) {
					for (QgqxmxDto dto : qgqxmxlist) {
						if (Float.parseFloat(dto.getQxsl()) > Float.parseFloat(dto.getKqxsl())) {
							throw new BusinessException("ICOM99019", dto.getWlmc() + "取消数量不得大于可取消数量!");
						}
					}
				}
				qgqxglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				// 发送钉钉消息
				if (!CollectionUtils.isEmpty(spgwcyDtos)) {
					try {
						int size = spgwcyDtos.size();
						String ICOMM_SH00047 = xxglService.getMsg("ICOMM_SH00047");
						for (SpgwcyDto spgwcyDto : spgwcyDtos) {
							if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
								String sign = URLEncoder.encode(commonservice.getSign(qgqxglDto_t.getQgqxid()), StandardCharsets.UTF_8);
								//内网访问
								String internalbtn = applicationurl + urlPrefix + "/ws/auditProcess/auditPhone?shxx_shry=" + spgwcyDto.getYhid() + "&shxx_shjs=" + spgwcyDto.getJsid() + "&ywid=" + qgqxglDto_t.getQgqxid() + "&sign=" + sign + "&xlcxh=" + shgcDto.getXlcxh() + "&business_url=/purchaseCancel/purchaseCancel/modPurchaseCancel&ywzd=qgqxid&shlbmc=" + shgcDto.getShlbmc();
								//外网访问
								//String external=externalurl+urlPrefix+"/ws/auditProcess/auditPhone?shxx_shry="+spgwcyDtos.get(i).getYhid()+"&shxx_shjs="+spgwcyDtos.get(i).getJsid()+"&ywid="+qgqxglDto_t.getQgqxid()+"&sign="+sign+"&xlcxh="+shgcDto.getXlcxh()+"&business_url=/purchaseCancel/purchaseCancel/modPurchaseCancel&ywzd=qgqxid&shlbmc="+shgcDto.getShlbmc();
								List<BtnJsonList> btnJsonLists = new ArrayList<>();
								BtnJsonList btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("详细");
								btnJsonList.setActionUrl(internalbtn);
								btnJsonLists.add(btnJsonList);
								/*btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("外网访问");
								btnJsonList.setActionUrl(external);
								btnJsonLists.add(btnJsonList);*/
								talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), ICOMM_SH00003,
										StringUtil.replaceMsg(ICOMM_SH00047, shgcDto.getShlbmc(), qgqxglDto_t.getSqrmc(),
												qgqxglDto_t.getSqbmmc(), qgqxglDto_t.getDjh(), qgqxglDto_t.getSqly(),
												DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
							}
						}
					} catch (Exception e) {
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
										StringUtil.replaceMsg(ICOMM_SH00005, operator.getZsxm(), shgcDto.getShlbmc(),
												qgqxglDto_t.getDjh(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
			}
			boolean isSuccess = updateQgqxglxx(qgqxglDto);
			if("1".equals(systemconfigflg) && isSuccess) {
				qgqxglDto.setPrefixFlg(prefixFlg);
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qgqxgl.updateQgqxglxx",JSONObject.toJSONString(qgqxglDto));
			}
		}
		return true;
	}

	@Override
	public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}
		if(auditParam.isCancelOpe()) {
			//审核回调方法
			for(ShgcDto shgcDto : shgcList){
				String qgid = shgcDto.getYwid();
				QgqxglDto qgqxglDto= new QgqxglDto();
				qgqxglDto.setXgry(operator.getYhid());
				qgqxglDto.setQgqxid(qgid);
				qgqxglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				boolean isSuccess = updateQgqxglxx(qgqxglDto);
				if("1".equals(systemconfigflg) && isSuccess) {
					qgqxglDto.setPrefixFlg(prefixFlg);
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qgqxgl.updateQgqxglxx",JSONObject.toJSONString(qgqxglDto));
				}
			}
		}else {
			//审核回调方法
			for(ShgcDto shgcDto : shgcList){
				String qgid = shgcDto.getYwid();
				QgqxglDto qgqxglDto =new QgqxglDto();
				qgqxglDto.setXgry(operator.getYhid());
				qgqxglDto.setQgqxid(qgid);
				qgqxglDto.setZt(StatusEnum.CHECK_NO.getCode());
				boolean isSuccess = updateQgqxglxx(qgqxglDto);
				if("1".equals(systemconfigflg) && isSuccess) {
					qgqxglDto.setPrefixFlg(prefixFlg);
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qgqxgl.updateQgqxglxx",JSONObject.toJSONString(qgqxglDto));
				}
			}
		}
		return true;
	}
	
	public boolean updateQgqxglxx(QgqxglDto qgqxglDto) {
		int result=dao.update(qgqxglDto);
		return result > 0;
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
		Map<String, Object> map = new HashMap<>();
		QgqxglDto qgqxglDto = dao.getDtoById(shgcDto.getYwid());
		map.put("jgid",qgqxglDto.getSqbm());
		return map;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
		Map<String, Object> map =new HashMap<>();
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>)param.get("ywids");
		QgqxglDto qgqxglDto = new QgqxglDto();
		qgqxglDto.setIds(ids);
		List<QgqxglDto> dtoList = dao.getDtoList(qgqxglDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(QgqxglDto dto:dtoList){
				list.add(dto.getQgqxid());
			}
		}
		map.put("list",list);
		return map;
	}

	/**
	 * 删除请购取消明细
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteQgqx(QgqxmxDto qgqxmxDto) throws BusinessException
	{
		QgqxglDto qgqxglDto = new QgqxglDto();
		qgqxglDto.setQgqxid(qgqxmxDto.getQgqxid());
		boolean result_t = delete(qgqxglDto);
		if(result_t)
			result_t = qgqxmxService.deleteQgqxmx(qgqxmxDto);

		if(!result_t)
			throw new BusinessException("msg", "请购取消信息修改失败(恢复请购)!");
		List<String> list = new ArrayList<>();
		list.add(qgqxmxDto.getQgqxid());
		shgcService.deleteByYwids(list);
		return result_t;
	}
}
