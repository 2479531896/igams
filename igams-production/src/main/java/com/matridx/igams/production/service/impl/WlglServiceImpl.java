package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.dao.post.IJcsjDao;
import com.matridx.igams.common.dao.post.ILshkDao;
import com.matridx.igams.common.dao.post.IShgcDao;
import com.matridx.igams.common.enums.*;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.production.dao.entities.*;
import com.matridx.igams.production.dao.matridxsql.IMatridxInventoryDao;
import com.matridx.igams.production.dao.post.IWlglDao;
import com.matridx.igams.production.dao.post.IWlgllsbDao;
import com.matridx.igams.production.service.svcinterface.IBmwlService;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class WlglServiceImpl extends BaseBasicServiceImpl<WlglDto, WlglModel, IWlglDao> implements IWlglService,IAuditService,IFileImport{
	
	@Autowired
	IShgcService shgcService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ILshkService lshkservice;
	@Autowired
	ICommonService commonservice;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IMatridxInventoryDao matridxInventoryDao;
	@Autowired
	IJcsjDao jcsjdao;
	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	DingTalkUtil talkUtil;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Autowired
	IShgcDao shgcDao;
	@Autowired
	IWlgllsbDao wlgllsbDao;
	@Value("${sqlserver.matridxds.flag:}")
	private String matridxdsflag;
	private final Logger log = LoggerFactory.getLogger(WlglServiceImpl.class);
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	IWlglDao wlglDao;
	@Autowired
	IBmwlService bmwlService;
	@Autowired
	IXtszService xtszService;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	@Value("${matridx.rabbit.systemreceiveflg:}")
	private String systemreceiveflg;
	@Autowired
	ILshkDao lshkDao;
	@Autowired
	private ICommonService commonService;
	/** 
	 * 插入物料信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(WlglDto wlglDto){
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.MATERIELQUALITY_TYPE,BasicDataTypeEnum.MATERIEL_SUBTYPE});
		List<JcsjDto> list=jclist.get(BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode());
		for (JcsjDto jcsjDto : list) {
			if (("1").equals(jcsjDto.getSfmr())) {
				wlglDto.setLb(jcsjDto.getCsid());
			}
		}
		wlglDto.setAqkc("0");
		wlglDto.setWlid(StringUtil.generateUUID());
		wlglDto.setZt(StatusEnum.CHECK_NO.getCode());
		int result = dao.insert(wlglDto);

		return result > 0;
	}
	
	@Override 
	public List<WlglDto> getPagedDtoList(WlglDto wlglDto){
		//获取审核信息
		try {
			List<WlglDto> t_List = dao.getPagedDtoList(wlglDto);
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_METRIEL.getCode(), "zt", "wlid", new String[]{
				StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode(),StatusEnum.CHECK_MALMOD.getCode()
			});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_METRIELMOD.getCode(), "zt", "lsid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode(),StatusEnum.CHECK_MALMOD.getCode()
				});
			jcsjService.handleCodeToValue(t_List,
					new BasicDataTypeEnum[] {
							BasicDataTypeEnum.MATERIEL_TYPE,BasicDataTypeEnum.MATERIEL_SUBTYPE,BasicDataTypeEnum.MATERIELQUALITY_TYPE}, new String[] {
					"wllb:wllbmc","wlzlb:wlzlbmc","lb:lbmc"});
			return t_List;
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return null;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
		// TODO Auto-generated method stub
		WlglDto wlglDto = (WlglDto)baseModel;
		wlglDto.setXgry(operator.getYhid());
		return update(wlglDto);
		
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam)throws BusinessException {
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}
//		String token = talkUtil.getToken();
		String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
		String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
		String ICOMM_SH00007 = xxglService.getMsg("ICOMM_SH00007");
		String ICOMM_SH00017 = xxglService.getMsg("ICOMM_SH00017");
		String ICOMM_SH00004 = xxglService.getMsg("ICOMM_SH00004");
		String ICOMM_SH00005 = xxglService.getMsg("ICOMM_SH00005");
		String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
		String ICOMM_SH00096 = xxglService.getMsg("ICOMM_SH00096");
		//toDO：如果审核通过的话，则先确认物料的必填项是否都已经有数据了，主要是用于批量导入的问题 2020-09-07 start
		List<WlglDto> nullLists = dao.getNullRequiredDtos(shgcList);
		if(!CollectionUtils.isEmpty(nullLists)) {
			throw new BusinessException("ICOM99019","物料信息缺失，请重新确认");
		}
		//toDO：如果审核通过的话，则先确认物料的必填项是否都已经有数据了，主要是用于批量导入的问题 2020-09-07 end
		
		for(ShgcDto shgcDto : shgcList){
			WlglDto wlglDto = new WlglDto();
			
			wlglDto.setWlid(shgcDto.getYwid());
			wlglDto=wlglDao.getDtoById(wlglDto.getWlid());

			wlglDto.setXgry(operator.getYhid());
			
			//审核退回
			if(AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())){
				wlglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				//发送钉钉消息
				if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())){
					try{
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
										shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00026,StringUtil.replaceMsg(ICOMM_SH00096, operator.getZsxm(),shgcDto.getShlbmc() ,wlglDto.getWlmc(),wlglDto.getWllbmc(),wlglDto.getWlzlbmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						log.error(e.getMessage());
					}
				}
			//审核通过
			}else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())){

				WlglDto t_wlglDto = dao.getDtoById(shgcDto.getYwid());
				t_wlglDto.setLbdm(null);
				//获取设备质量类别id
				Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.MATERIELQUALITY_TYPE,BasicDataTypeEnum.MATERIEL_SUBTYPE});
				List<JcsjDto> list=jclist.get(BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode());
				List<JcsjDto> zlblist=jclist.get(BasicDataTypeEnum.MATERIEL_SUBTYPE.getCode());
				String sbzllb = "";
				String lbdm = "";
				for (JcsjDto dto : list) {
					if ("3".equals(dto.getCskz1())) {
						sbzllb = dto.getCsid();
						lbdm = dto.getCsdm();
					}
				}
				//判断物料类别是否为设备
				if (StringUtil.isNotBlank(t_wlglDto.getWlzlb())) {
					for (JcsjDto jcsjDto : zlblist) {
						if ("DEVICE".equals(jcsjDto.getCskz3()) 
								&& t_wlglDto.getWlzlb().equals(jcsjDto.getCsid())
								&& t_wlglDto.getWllb().equals(jcsjDto.getFcsid())) {
							//如果是，设置物料质量类别为设备
							wlglDto.setLb(sbzllb);
							t_wlglDto.setLbdm(lbdm);
						}
					}
				}
//				ComputationUnitModel selectInventoryGm = new ComputationUnitModel();
//				if(StringUtil.isNotBlank(gmdsflag)) {
//					//判断单位是否存在
//					selectInventoryGm = gmInventoryDao.selectmatridx(t_wlglDto.getJldw());
//					if(selectInventoryGm==null)
//						throw new BusinessException("msg","计量单位不存在或有误！(gm)");
//				}
//				ComputationUnitModel selectInventory = new ComputationUnitModel();
//				ComputationUnitModel selectInventory2017 = new ComputationUnitModel();
//				ComputationUnitModel selectInventory2020 = new ComputationUnitModel();
//				if(StringUtil.isNotBlank(matridxdsflag)) {
//					selectInventory = matridxInventoryDao.selectInventory(t_wlglDto.getJldw());
//					selectInventory2017 = matridxInventoryDao.selectInventory2017(t_wlglDto.getJldw());
//					selectInventory2020 = matridxInventoryDao.selectInventory2020(t_wlglDto.getJldw());
//					if(selectInventory==null || selectInventory2017==null || selectInventory2020==null)
//						throw new BusinessException("msg","计量单位不存在或有误！(matridx)");
//				}
				ComputationUnitModel selectInventory = new ComputationUnitModel();
				if(StringUtil.isNotBlank(matridxdsflag)) {
					selectInventory = matridxInventoryDao.selectInventory(t_wlglDto.getJldw());
					if(selectInventory==null){
						throw new BusinessException("msg","计量单位不存在或有误！");
					}
				}
				// 判断是否生成物料编码
				String wlbm = t_wlglDto.getWlbm();
				if(StringUtil.isBlank(t_wlglDto.getWlbm())) {
					//生成内部用流水号
					LshkDto lshkDto = new LshkDto();
					lshkDto.setWllb(t_wlglDto.getWllbdm());
					lshkDto.setWlzlb(t_wlglDto.getWlzlbdm());
					wlbm = lshkservice.doMakeSerNum(SerialNumTypeEnum.MATER_CODE, lshkDto);
					t_wlglDto.setWlbm(wlbm);
				}
//				if(StringUtil.isNotBlank(gmdsflag)) {
//					//判断物料是否已经存在
//					if(gmInventoryDao.select_matridx_InvName(wlbm)!=null)
//						 throw new BusinessException("msg","U8系统物料已经存在！(gm)");
//				}
				if(StringUtil.isNotBlank(matridxdsflag)) {
					//判断物料是否已经存在
					if(matridxInventoryDao.select_Inventory_InvName(wlbm)!=null)
						 throw new BusinessException("msg","U8系统物料已经存在！(matridx)");
				}
				wlglDto.setWlbm(wlbm);
				t_wlglDto.setPrefix(prefixFlg);
				//内部采用系统时间，所以只需随意设置就可以
				wlglDto.setShsj("1");
				wlglDto.setZt(StatusEnum.CHECK_PASS.getCode());
				/*  */
				//发送钉钉消息
				if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())){
					try{
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(), shgcDto.getSpgwcyDtos().get(i).getYhm(),
										shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00006,StringUtil.replaceMsg(ICOMM_SH00007, operator.getZsxm(),shgcDto.getShlbmc() ,t_wlglDto.getWlmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss"), wlbm));
							}
						}
					}catch(Exception e){
						log.error(e.getMessage());
					}
				}
				//把物料信息插入到U8系统中	
				insertInventory(t_wlglDto, selectInventory);
				if (StringUtil.isNotBlank(t_wlglDto.getJwlbm())){
					List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(DingMessageType.MATERIAL_ADDTZ.getCode());
					String ICOMM_SH00109 = xxglService.getMsg("ICOMM_SH00109");
					String ICOMM_SH00110 = xxglService.getMsg("ICOMM_SH00110");
					if (!CollectionUtils.isEmpty(ddxxglDtos)) {
						for (DdxxglDto ddxxglDto : ddxxglDtos) {
							if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
								// 内网访问
								String internalbtn = applicationurl + urlPrefix
										+ "/ws/production/materiel/viewMateriel?wlid=" + t_wlglDto.getWlid();
								List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
								OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
								btnJsonList.setTitle("详细");
								btnJsonList.setActionUrl(internalbtn);
								btnJsonLists.add(btnJsonList);
								talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),ddxxglDto.getYhid(),ddxxglDto.getYhm(), ddxxglDto.getDdid(), ICOMM_SH00109, StringUtil.replaceMsg(ICOMM_SH00110,
												wlbm, t_wlglDto.getWlmc(),
												t_wlglDto.getJwlbm(), shgcDto.getSqrxm(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
										btnJsonLists, "1");
							}
						}
					}
				}
				t_wlglDto.setZt(StatusEnum.CHECK_PASS.getCode());
				RabbitUtils.sendSyncWlRabbitMsg(JSON.toJSONString(t_wlglDto),"sys.igams.wlgl.insertWlgl");
//				amqpTempl.convertAndSend("sys.igams", "sys.igams.wlgl.update",JSONObject.toJSONString(t_wlglDto));
			}else{   //新增操作
				if(!auditParam.isCommitOpe()){
					WlglDto wlglDto_jldw = dao.getDtoById(shgcDto.getYwid());
//					if(StringUtil.isNotBlank(gmdsflag)) {
//						if(gmInventoryDao.selectmatridx(wlglDto_jldw.getJldw())==null) {
//							throw new BusinessException("msg","计量单位不存在或有误！(gm)");
//						}
//					}
					if(StringUtil.isNotBlank(matridxdsflag)) {
						if(matridxInventoryDao.selectInventory(wlglDto_jldw.getJldw())==null) {
							throw new BusinessException("msg","计量单位不存在或有误！(matridx)");
						}
					}
					
				}
				wlglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				//发送钉钉消息
				if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())){
					try{
						WlglDto t_wlglDto = dao.getDtoById(shgcDto.getYwid());
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								String sign = URLEncoder.encode(commonservice.getSign(t_wlglDto.getWlid()), StandardCharsets.UTF_8);
								//内网访问
								String internalbtn = applicationurl + urlPrefix + "/ws/auditProcess/auditPhone?shxx_shry="+shgcDto.getSpgwcyDtos().get(i).getYhid()+"&shxx_shjs="+shgcDto.getSpgwcyDtos().get(i).getJsid()+"&ywid="+t_wlglDto.getWlid()+"&sign="+sign+"&xlcxh="+shgcDto.getXlcxh()+"&business_url="+"/production/materiel/modMater&ywzd=wlid&shlbmc="+shgcDto.getShlbmc();
								//外网访问
								//String external=externalurl+"/ws/auditProcess/auditPhone?shxx_shry="+shgcDto.getSpgwcyDtos().get(i).getYhid()+"&shxx_shjs="+shgcDto.getSpgwcyDtos().get(i).getJsid()+"&ywid="+t_wlglDto.getWlid()+"&sign="+sign+"&xlcxh="+shgcDto.getXlcxh()+"&business_url="+"/production/materiel/modMater&ywzd=wlid&shlbmc="+shgcDto.getShlbmc();
								List<BtnJsonList> btnJsonLists = new ArrayList<>();
								BtnJsonList btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("详细");
								btnJsonList.setActionUrl(internalbtn);
								btnJsonLists.add(btnJsonList);
								/*btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("外网访问");
								btnJsonList.setActionUrl(external);
								btnJsonLists.add(btnJsonList);*/
								talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
										shgcDto.getSpgwcyDtos().get(i).getYhid(),ICOMM_SH00003,StringUtil.replaceMsg(ICOMM_SH00017,
										operator.getZsxm(),shgcDto.getShlbmc() ,t_wlglDto.getWlmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
							}
						}
					}catch(Exception e){
						log.error(e.getMessage());
					}
				}
				//发送钉钉消息--取消审核人员
				if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())){
					try{
						WlglDto t_wlglDto = dao.getDtoById(shgcDto.getYwid());
						int size = shgcDto.getNo_spgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
										shgcDto.getNo_spgwcyDtos().get(i).getYhid(), ICOMM_SH00004, StringUtil.replaceMsg(ICOMM_SH00005, operator.getZsxm(),shgcDto.getShlbmc() ,t_wlglDto.getWlmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						log.error(e.getMessage());
					}
				}
			}
			//更新状态
			dao.updateZt(wlglDto);
		}
		return true;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}
		if(auditParam.isCancelOpe()) {
			//审核回调方法
			for(ShgcDto shgcDto : shgcList){
				String wlid = shgcDto.getYwid();
				WlglDto wlglDto = new WlglDto();
				wlglDto.setXgry(operator.getYhid());
				wlglDto.setWlid(wlid);
				wlglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				dao.update(wlglDto);
			}
		}else {
			//审核回调方法
			for(ShgcDto shgcDto : shgcList){
				String wlid = shgcDto.getYwid();
				WlglDto wlglDto = new WlglDto();
				wlglDto.setXgry(operator.getYhid());
				wlglDto.setWlid(wlid);
				wlglDto.setZt(StatusEnum.CHECK_NO.getCode());
				dao.update(wlglDto);
			}
		}
		return true;
	}

	@Override
	public List<WlglDto> getPagedAuditList(WlglDto wlglDto) {
		// TODO Auto-generated method stub
		//获取人员ID和履历号
		List<WlglDto> t_sqList = dao.getPagedAuditIdList(wlglDto);
		
		if(CollectionUtils.isEmpty(t_sqList))
			return t_sqList;
		
		List<WlglDto> sqList = dao.getAuditListByIds(t_sqList);
		
		commonservice.setSqrxm(sqList);
		
		jcsjService.handleCodeToValue(sqList,
				new BasicDataTypeEnum[] {
						BasicDataTypeEnum.MATERIEL_TYPE,BasicDataTypeEnum.MATERIEL_SUBTYPE}, new String[] {
				"wllb:wllbmc","wlzlb:wlzlbmc"});
		
		return sqList;
	}

	@Override
	public boolean existCheck(String fieldName, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertImportRec(BaseModel baseModel,User user,int rowindex, StringBuffer errorMessages) {
		try{
			// TODO Auto-generated method stub
			WlglDto wlglDto = (WlglDto)baseModel;
			
			String wlid = StringUtil.generateUUID();
			
			wlglDto.setWlid(wlid);
			
			wlglDto.setZt(StatusEnum.CHECK_PASS.getCode());
			
			Pattern pattern = Pattern.compile("^\\d+(\\.\\d+)?$");
		    Matcher matcher = pattern.matcher(wlglDto.getBzq());
		    boolean result = matcher.matches();
		    
		    if(result)
		    	wlglDto.setBzqflg("0");
		    else
		    	wlglDto.setBzqflg("1");
		    
		    JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb(BasicDataTypeEnum.MATERIEL_TYPE.getCode());
			jcsjDto.setCsid(wlglDto.getWllb());
			JcsjDto t_jcsjDto = jcsjService.getDto(jcsjDto);
			
			jcsjDto.setJclb(BasicDataTypeEnum.MATERIEL_SUBTYPE.getCode());
			jcsjDto.setCsid(wlglDto.getWlzlb());
			jcsjDto.setFcsid(wlglDto.getWllb());
			JcsjDto t_subjcsjDto = jcsjService.getDto(jcsjDto);
			
			lshkservice.updateSerNum(SerialNumTypeEnum.MATER_CODE.getCode(), t_jcsjDto.getCsdm()+t_subjcsjDto.getCsdm(), wlglDto.getWlbm());
			
			dao.insert(wlglDto);
			
			shgcService.importBusinessCommit(wlglDto.getWlid(), AuditTypeEnum.AUDIT_METRIEL.getCode(), user);
			
		}catch(Exception e){
			log.error(e.toString());
		}
		
		return true;
	}

	@Override
	public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
		// TODO Auto-generated method stub
		if(tranTrack.equalsIgnoreCase("WL001")){//物料类别
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb(BasicDataTypeEnum.MATERIEL_TYPE.getCode());
			jcsjDto.setCsdm(value);
			JcsjDto t_jcsjDto = jcsjService.getDto(jcsjDto);
			if(t_jcsjDto== null){
				errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
				.append("列，找不到对应的物料类别的编码，单元格值为：").append(value).append("；<br>");
			}
			else{
				recModel.setWllb(t_jcsjDto.getCsid());
				return t_jcsjDto.getCsid();
			}
		}else if(tranTrack.equalsIgnoreCase("WL002")){//物料子类别
			if(StringUtil.isBlank(recModel.getWllb())){
				errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
				.append("列，请先设置相应的物料类别的编码，单元格值为：").append(value).append("；<br>");
				return null;
			}
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb(BasicDataTypeEnum.MATERIEL_SUBTYPE.getCode());
			jcsjDto.setCsdm(value);
			jcsjDto.setFcsid(recModel.getWllb());
			JcsjDto t_jcsjDto = jcsjService.getDto(jcsjDto);
			if(t_jcsjDto== null){
				errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
				.append("列，找不到对应的物料子类别的编码，单元格值为：").append(value).append("；<br>");
			}
			else {
				recModel.setZlbcskz2(t_jcsjDto.getCskz2());
				return t_jcsjDto.getCsid();
			}
		}else if(tranTrack.equalsIgnoreCase("WL003")){//是否危险品
			if(StringUtil.isNotBlank(value)) {
				JcsjDto jcsjDto_t = new JcsjDto();
				jcsjDto_t.setJclb(BasicDataTypeEnum.MATERIEL_SAFE_TYPE.getCode());
				jcsjDto_t.setCsdm(value);			
				List<JcsjDto> jcsjList = jcsjService.getDtoList(jcsjDto_t);
				for (JcsjDto jcsjDto : jcsjList) {
					if(value.equals(jcsjDto.getCsdm())) {
						return jcsjDto.getCsid();
					}
				}				
			}
			return "";	
		}else if(tranTrack.equalsIgnoreCase("WL004")){//物料子类别统称
			if(!"1".equals(recModel.getZlbcskz2())){
				if(StringUtils.isNotBlank(value)) {
					errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
					.append("列，该物料不需要设置物料子类别统称，单元格值为：").append(value).append("；<br>");
					return null;
				}else {
					return null;
				}
			}else {
				if(StringUtils.isBlank(value)) {
					errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
					.append("列，该物料子类别统称不能为空").append("；<br>");
					return null;
				}
			}
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb(BasicDataTypeEnum.MATERIEL_SUBTYPE_COLLECTIVELY.getCode());
			jcsjDto.setCsdm(value);
			JcsjDto t_jcsjDto = jcsjService.getDto(jcsjDto);
			if(t_jcsjDto== null){
				errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
				.append("列，找不到对应的物料子类别统称的编码，单元格值为：").append(value).append("；<br>");
			}
			else
				return t_jcsjDto.getCsid();
		}else if(tranTrack.equalsIgnoreCase("WL005")){ //物料编码
			if(StringUtil.isNotBlank(value)) {
				WlglDto wlglDto = dao.getWlidByWlbm(value);
				if(wlglDto!=null) {
					errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
					.append("列，物料编码重复，单元格值为：").append(value).append("；<br>");
				}else {
					return value;
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据搜索条件获取导出条数
	 */
	public int getCountForSearchExp(WlglDto wlglDto,Map<String, Object> params){
		return dao.getCountForSearchExp(wlglDto);
	}
	
	/**
	 * 根据搜索条件分页获取导出信息
	 */
	public List<WlglDto> getListForSearchExp(Map<String,Object> params){
		WlglDto wlglDto = (WlglDto)params.get("entryData");
		queryJoinFlagExport(params,wlglDto);
		return dao.getListForSearchExp(wlglDto);
	}
	
	/**
	 * 根据选择信息获取导出信息
	 */
	public List<WlglDto> getListForSelectExp(Map<String,Object> params){
		WlglDto wlglDto = (WlglDto)params.get("entryData");
		queryJoinFlagExport(params,wlglDto);
		//List<WlglDto> glist=dao.getListForSelectExp(wlglDto);
		return dao.getListForSelectExp(wlglDto);
	}
	
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String,Object> params,WlglDto wlglDto){
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuilder sqlParam = new StringBuilder();
		for(DcszDto dcszDto:choseList){
			if(dcszDto == null || dcszDto.getDczd() == null)
				continue;
			if(dcszDto.getDczd().equalsIgnoreCase("WLLBDM")){
				wlglDto.setWllb_flg("Y");
			}else if(dcszDto.getDczd().equalsIgnoreCase("WLLBMC")){
				wlglDto.setWllb_flg("Y");
			}else if(dcszDto.getDczd().equalsIgnoreCase("WLZLBDM")){
				wlglDto.setWlzlb_flg("Y");
			}else if(dcszDto.getDczd().equalsIgnoreCase("WLZLBMC")){
				wlglDto.setWlzlb_flg("Y");
			}else if(dcszDto.getDczd().equalsIgnoreCase("LBMC")){
				wlglDto.setWlzllb_flg("Y");
			}
			sqlParam.append(",");
			if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		wlglDto.setSqlParam(sqlParam.toString());
	}

	@Override
	public WlgllsbDto selectWllsbDtoByWlid(String wlid) {
		return dao.selectWllsbDtoByWlid(wlid);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public int updateWlgl(WlglDto wlglDto) throws BusinessException{
//		if(StringUtil.isNotBlank(gmdsflag)) {
//			if(gmInventoryDao.selectmatridx(wlglDto.getJldw()) == null) {
//				throw new BusinessException("msg","计量单位不存在或有误！(gm)");
//			}
//		}
		if(StringUtil.isNotBlank(matridxdsflag)) {
			if(matridxInventoryDao.selectInventory(wlglDto.getJldw()) == null) {
				throw new BusinessException("msg","计量单位不存在或有误！(matridx)");
			}
		}
		//判断修改的物料类别是否为设备
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.MATERIELQUALITY_TYPE,BasicDataTypeEnum.MATERIEL_SUBTYPE});
		List<JcsjDto> list=jclist.get(BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode());
		List<JcsjDto> zlblist=jclist.get(BasicDataTypeEnum.MATERIEL_SUBTYPE.getCode());
		//获取设备质量类别id
		String sbzllb = "";
		String lbdm = "";
		for (JcsjDto dto : list) {
			if ("3".equals(dto.getCskz1())) {
				sbzllb = dto.getCsid();
				lbdm = dto.getCsdm();
			}
		}
		//判断物料类别是否为设备
		if (StringUtil.isNotBlank(wlglDto.getWlzlb())) {
			for (JcsjDto jcsjDto : zlblist) {
				if ("F".equals(jcsjDto.getCsdm()) && "I".equals(jcsjDto.getFcsdm())
						&& wlglDto.getWlzlb().equals(jcsjDto.getCsid())
						&& wlglDto.getWllb().equals(jcsjDto.getFcsid())) {
					//如果是，设置物料质量类别为设备
					wlglDto.setLb(sbzllb);
					wlglDto.setLbdm(lbdm);
				}
			}
		}
		int result = dao.update(wlglDto);
		if(result!=0) {
			boolean result1=updateInventory(wlglDto);
			if (!result1){
				return 0;// 本地执行成功，但u8执行错误
			}else {
				WlglDto wlglDto_t=dao.getDto(wlglDto);
				if(wlglDto_t!=null) {
					if(StatusEnum.CHECK_PASS.getCode().equals(wlglDto_t.getZt())) {
						wlglDto.setPrefix(prefixFlg);
//						amqpTempl.convertAndSend("sys.igams", "sys.igams.wlgl.update",JSONObject.toJSONString(wlglDto));
					}
				}
			}
		}else {
			return 1;//本地执行失败，不再执行u8，直接返回；
		}
		return 2;//都执行成功
	}
	
	
	/**
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void insertInventory(WlglDto t_wlglDto, ComputationUnitModel selectInventory){
		//查询出来最大的partid并加1 进行添加
//		Bas_partModel part2016 = new Bas_partModel();
//		if(StringUtil.isNotBlank(gmdsflag)) {
//			part2016 = gmInventoryDao.select_matridx_partid();
//			//判断2016数据库中partid是否存在，
//			int partid_2016=Integer.parseInt(part2016.getPartId());
//			partid_2016++;
//			part2016.setPartId(partid_2016+"");  //如果不存在，就从1开始
//			part2016.setInvCode(t_wlglDto.getWlbm());
//		}
//		Bas_partModel part2019 = new Bas_partModel();
//		Bas_partModel part2017 = new Bas_partModel();
//		Bas_partModel part2020 = new Bas_partModel();
//		if(StringUtil.isNotBlank(matridxdsflag)) {
//			part2019 = matridxInventoryDao.select_Inventory_partid();
//			part2017 = matridxInventoryDao.select_Inventory2017_partid();
//			part2020 = matridxInventoryDao.select_Inventory2020_partid();
//
//			int  partid_2019=Integer.parseInt(part2019.getPartId());
//			partid_2019++;
//			part2019.setPartId(partid_2019+"");		//否则，partid+1 进行添加
//			part2019.setInvCode(t_wlglDto.getWlbm());
//
//			int  partid_2017=Integer.parseInt(part2017.getPartId());
//			partid_2017++;
//			part2017.setPartId(partid_2017+"");		//否则，partid+1 进行添加
//			part2017.setInvCode(t_wlglDto.getWlbm());
//
//			int  partid_2020=Integer.parseInt(part2020.getPartId());
//			partid_2020++;
//			part2020.setPartId(partid_2020+"");		//否则，partid+1 进行添加
//			part2020.setInvCode(t_wlglDto.getWlbm());
//		}
		Bas_partModel part = new Bas_partModel();
		if(StringUtil.isNotBlank(matridxdsflag)) {
			part = matridxInventoryDao.select_Inventory_partid();
			int  partid=Integer.parseInt(part.getPartId());
			partid++;
			part.setPartId(partid+"");        //否则，partid+1 进行添加
			part.setInvCode(t_wlglDto.getWlbm());
		}
		//实例化一个新的实体类用来存值
		InventoryModel inModel = new InventoryModel();
		//根据不同的保质期flg进行不同的存值
		if (("0").equals(t_wlglDto.getBzqflg())){
			inModel.setiMassDate(t_wlglDto.getBzq());
			inModel.setcMassUnit("2");
		}else{
			inModel.setiMassDate("99");
			inModel.setcMassUnit("1");
		}
		
		//从单位表里边查询单位对应的编号
		inModel.setcInvCode(t_wlglDto.getWlbm());
		inModel.setcInvCCode(StringUtil.leftPad(t_wlglDto.getWllbdm(), 3, "0")+StringUtil.rightPad(t_wlglDto.getWlzlbdm(), 3, "0")); //货存分类
		inModel.setcInvName(t_wlglDto.getWlmc());			//物料名称
		inModel.setcInvDefine2(t_wlglDto.getScs());			//生产商
		inModel.setcInvDefine3(t_wlglDto.getYchh());		//货号
		inModel.setcInvStd(t_wlglDto.getGg());				//规格
		inModel.setcInvDefine6(t_wlglDto.getBctj());		//保存条件
		inModel.setcInvDefine1(t_wlglDto.getCpzch());		//产品注册号
		//判断质量类别是否为空,不为空，存入U8
		if(StringUtil.isNotBlank(t_wlglDto.getLbdm()) && !"001".equals(t_wlglDto.getLbdm())) {
			inModel.setcInvABC(t_wlglDto.getLbdm());
		}
		Map<String, String> map=new HashMap<>();
		map.put("cInvSubCode",t_wlglDto.getWlbm());
//		if(StringUtil.isNotBlank(gmdsflag)) {
//			inModel.setcComUnitCode(selectmatridx.getcComunitCode());
//			gmInventoryDao.insertmatridx(inModel);
//			gmInventoryDao.insertsubmatridx(map);
//			gmInventoryDao.insert_matridx_Bas_part(part2016);  //添加2016数据库
//		}
//		if(StringUtil.isNotBlank(matridxdsflag)) {
//			inModel.setcComUnitCode(selectInventory.getcComunitCode());
//			matridxInventoryDao.insertInventory(inModel);
//			inModel.setcComUnitCode(selectInventory2017.getcComunitCode());
//			matridxInventoryDao.insertInventory2017(inModel);
//			inModel.setcComUnitCode(selectInventory2020.getcComunitCode());
//			matridxInventoryDao.insertInventory2020(inModel);
//
//			matridxInventoryDao.insertsubInventory(map);
//			matridxInventoryDao.insertsubInventory2017(map);
//			matridxInventoryDao.insertsubInventory2020(map);
//
//			matridxInventoryDao.insert_Inventory_Bas_part(part2019);//添加2019数据库
//			matridxInventoryDao.insert_Inventory2017_Bas_part(part2017);//添加2017数据库
//			matridxInventoryDao.insert_Inventory2020_Bas_part(part2020);//添加2020数据库
//		}
		if(StringUtil.isNotBlank(matridxdsflag)) {
			inModel.setcComUnitCode(selectInventory.getcComunitCode());
			matridxInventoryDao.insertInventory(inModel);
			matridxInventoryDao.insertsubInventory(map);
			matridxInventoryDao.insert_Inventory_Bas_part(part);//添加数据库
		}
	}
	
	/**
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateInventory(WlglDto wlglDto){
		InventoryModel inModel = new InventoryModel();
		//从基础数据里边查询单位
		ComputationUnitModel cModel = new ComputationUnitModel();
		/*if(StringUtil.isNotBlank(gmdsflag)) {
			cModel = gmInventoryDao.selectmatridx(wlglDto.getJldw());
		}else if(StringUtil.isNotBlank(matridxdsflag)) {
			cModel = matridxInventoryDao.selectInventory(wlglDto.getJldw());
		}*/
		if(StringUtil.isNotBlank(matridxdsflag)) {
			cModel = matridxInventoryDao.selectInventory(wlglDto.getJldw());
		}
		inModel.setcComUnitCode(cModel.getcComunitCode());
		
		if (("0").equals(wlglDto.getBzqflg())){
			inModel.setiMassDate(wlglDto.getBzq());
			inModel.setcMassUnit("2");
		}else{
			inModel.setiMassDate("99");
			inModel.setcMassUnit("1");	
		}
		//判断物料类别是否为空，不为空更新U8
		if(StringUtil.isNotBlank(wlglDto.getLbdm())) {
			inModel.setcInvABC(wlglDto.getLbdm());
		}
		inModel.setcInvCode(wlglDto.getWlbm());
		inModel.setcInvName(wlglDto.getWlmc());
		inModel.setcInvDefine2(wlglDto.getScs());
		inModel.setcInvDefine3(wlglDto.getYchh());
		inModel.setcInvStd(wlglDto.getGg());				//规格
		inModel.setcInvDefine6(wlglDto.getBctj());
		inModel.setcModifyPerson(wlglDto.getXgryzsxm());  //修改人员
		inModel.setcInvDefine1(wlglDto.getCpzch());
//		if(StringUtil.isNotBlank(gmdsflag)) {
//			gmInventoryDao.updatematridx(inModel);
//		}
//		if(StringUtil.isNotBlank(matridxdsflag)) {
//			matridxInventoryDao.updateInventory(inModel);
//			matridxInventoryDao.updateInventory2017(inModel);
//			matridxInventoryDao.updateInventory2020(inModel);
//		}
		if(StringUtil.isNotBlank(matridxdsflag)) {
			matridxInventoryDao.updateInventory(inModel);
		}
		return true;
	}

	/**
	 * 模糊查询物料信息
	 */
	@Override
	public List<WlglDto> selectSameMater(WlglDto wlglDto) {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(wlglDto.getWlmc())||StringUtil.isNotBlank(wlglDto.getGg())){
			return dao.selectSameMater(wlglDto);
		}
		return null;
	}

	@Override
	public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
		// TODO Auto-generated method stub
		return false;
	} 
	
	/**
	 * 删除物料管理同时删除审核过程
	 */
	public boolean deleteWlgl(WlglDto wlglDto){
		//删除物料管理表
		boolean result=delete(wlglDto);
		//删除物料管理审核过程
		shgcDao.deleteByYwids(wlglDto.getIds());
		//删除物料临时表
		WlgllsbDto wlgllsbDto=new WlgllsbDto();
		wlgllsbDto.setWlid(wlglDto.getWlid());
		wlgllsbDto.setIds(wlglDto.getIds());
		List<WlgllsbDto> list= wlgllsbDao.getDtoListByWlids(wlgllsbDto);
		if(!CollectionUtils.isEmpty(list)) {
			for (WlgllsbDto dto : list) {
				wlgllsbDao.updateSc(dto);
				//删除物料临时表审核过程
				shgcDao.deleteByYwid(dto.getLsid());
			}
		}
//		if(result) {
//			amqpTempl.convertAndSend("sys.igams", "sys.igams.wlgl.del",JSONObject.toJSONString(wlglDto));
//		}
		return result;
	}
	
	/**
	 * 根据查询条件查询物料信息
	 */
	public List<WlglDto> selectWl(WlglDto wlglDto){
		return dao.selectWl(wlglDto);
	}
	
	/**
	 * 根据物料编码获取多个物料信息
	 */
	public List<WlglDto> getDtosByIds(WlglDto wlglDto){
		return dao.getDtosByIds(wlglDto);
	}
	
	/**
	 * 检查标题定义，主要防止模板信息过旧
	 */
	public boolean checkDefined(List<Map<String,String>> defined) {
		boolean result=false;
		if(!CollectionUtils.isEmpty(defined)) {//校验导入的文件模板是否有物料子类别统称字段
			for (Map<String, String> map : defined) {
				if ("WL004".equals(map.get("transform"))) {
					result = true;
				}
			}
		}
		return result;
	}
	
	/**
	 * 新增或修改物料信息
	 */
	public void insertOrUpdateWlgl(WlglDto wlglDto) {
        dao.insertOrUpdateWlgl(wlglDto);
    }
	
	/**
	 * 根据物料id获取物料基本信息
	 */
	public List<WlglDto> getWlglDtoListByDtos(List<String> list){
		return dao.getWlglDtoListByDtos(list);
	}

	/**
	 * 库存维护修改U8数据
	 */
	@Override
	public boolean updateSafeStock(WlglDto wlglDto) {
		// 从数据库取修改前的安全库存
		String aqkc_before = dao.getDto(wlglDto).getAqkc();
		// 从页面取修改后的安全库存
		String aqkc_after = wlglDto.getAqkc();
		int result = dao.updateKcwh(wlglDto);
		if(result<1)
			return false;
		InventoryModel inModel = new InventoryModel();
		inModel.setcInvCode(wlglDto.getWlbm());
		if(StringUtil.isNotBlank(wlglDto.getLbdm())) {
			inModel.setcInvABC(wlglDto.getLbdm());
			if("001".equals(wlglDto.getLbdm()))
				inModel.setcInvABC(null);
		}
		if(StringUtil.isNotBlank(wlglDto.getXgry())) {
			inModel.setcModifyPerson(wlglDto.getXgryzsxm()); //修改人员
		}
		if (!aqkc_after.equals(aqkc_before)) {
			inModel.setIsafeNum(aqkc_after);
		}
//		if(StringUtil.isNotBlank(gmdsflag)) {
//			gmInventoryDao.updatematridx(inModel);
//		}
//		if(StringUtil.isNotBlank(matridxdsflag)) {
//			matridxInventoryDao.updateInventory(inModel);
//			matridxInventoryDao.updateInventory2017(inModel);
//			matridxInventoryDao.updateInventory2020(inModel);
//		}
		if(StringUtil.isNotBlank(matridxdsflag)) {
			matridxInventoryDao.updateInventory(inModel);
		}
		return true;
	}

	/**
	 * 库存维护修改数据
	 */
	public int updateKcwh(WlglDto wlglDto) {
		return dao.updateKcwh(wlglDto);
	}

	/**
	 * 获取前15条物料信息
	 */
	@Override
	public List<WlglDto> getWlgl(WlglDto wlglDto) {
		// TODO Auto-generated method stub
		return dao.getWlgl(wlglDto);
	}

	/**
	 * 通过wlbm获取wlid
	 */
	@Override
	public WlglDto getWlidByWlbm(String wlbm) {
		// TODO Auto-generated method stub
		return dao.getWlidByWlbm(wlbm);
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto ) {
		return null;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
		Map<String, Object> map =new HashMap<>();
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>)param.get("ywids");
		WlglDto wlglDto = new WlglDto();
		wlglDto.setIds(ids);
		List<WlglDto> dtoList = dao.getDtoList(wlglDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(WlglDto dto:dtoList){
				list.add(dto.getWlid());
			}
		}
		map.put("list",list);
		return map;
	}

	/**
	 * 废弃
	 */
	public boolean discard(WlglDto wlglDto){
		return dao.discard(wlglDto);
	}
	/**
	 * 单条物料点击查看接口
	 */

	@Override
	public WlglDto queryByWlid(WlglDto wlglDto) {
		return dao.queryByWlid(wlglDto);
	}
	/**
	 * 根据物料ids获取物料信息
	 */
	@Override
	public List<WlglDto> queryByWllxByWlids(WlglDto wlglDto) {
		return dao.queryByWllxByWlids(wlglDto);
	}

	@Override
	public List<WlglDto> getDtoListByWlid(WlglDto wlglDto) {
		return dao.getDtoListByWlid(wlglDto);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean depsettingSaveMater(WlglDto wlglDto, BmwlDto bmwlDto){
		BmwlDto dto = bmwlService.getDto(bmwlDto);
		if ("1".equals(wlglDto.getSfsz())){
			if (dto==null)
				bmwlService.insert(bmwlDto);
		}else {
			if (dto!=null)
				bmwlService.delete(bmwlDto);
		}
		// 从数据库取修改前的安全库存
		String aqkc_before = dao.getDto(wlglDto).getAqkc();
		// 从页面取修改后的安全库存
		String aqkc_after = wlglDto.getAqkc();
		InventoryDto inventoryDto = new InventoryDto();
		inventoryDto.setcInvCode(wlglDto.getWlbm());
		if (!aqkc_after.equals(aqkc_before) && StringUtil.isNotBlank(matridxdsflag)) {
			inventoryDto.setIsafeNum(aqkc_after);
			matridxInventoryDao.updateInventory(inventoryDto);
		}
		updateSafeStock(wlglDto);
		return true;
	}

	@Override
	public List<WlglDto> getOrderedDtoList(WlglDto wlglDto) {
		return dao.getOrderedDtoList(wlglDto);
	}

	@Override
	public String selectBiggestHq(String wlid) {
		return dao.selectBiggestHq(wlid);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean syncWlgl(WlglDto wlglDto)throws BusinessException {
		WlglDto wlglDto1=dao.getWlidByWlbm(wlglDto.getWlbm());
		if(wlglDto1==null){
			dao.insert(wlglDto);
		}else{
			throw new BusinessException("msg","系统物料已经存在！(matridx)");
		}
		if(StringUtil.isNotBlank(matridxdsflag)) {
			//判断物料是否已经存在
			if(matridxInventoryDao.select_Inventory_InvName(wlglDto.getWlbm())!=null)
				throw new BusinessException("msg","U8系统物料已经存在！(matridx)");
			ComputationUnitModel selectInventory = new ComputationUnitModel();

			selectInventory = matridxInventoryDao.selectInventory(wlglDto.getJldw());
			if(selectInventory==null){
				throw new BusinessException("msg","计量单位不存在或有误！");
			}
			insertInventory(wlglDto, selectInventory);
		}
		lshkservice.updateSerNum(SerialNumTypeEnum.MATER_CODE.getCode(), wlglDto.getWlbm().substring(0,2), wlglDto.getWlbm());
		return true;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void syncUpdateWlgl(WlglDto wlglDto) throws BusinessException {
		if(StringUtil.isNotBlank(matridxdsflag)) {
			if(matridxInventoryDao.selectInventory(wlglDto.getJldw()) == null) {
				throw new BusinessException("msg","计量单位不存在或有误！(matridx)");
			}
		}
		int result = dao.update(wlglDto);
		if(result!=0) {
			updateInventory(wlglDto);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void synclDeteWlgl(WlglDto wlglDto) {
		dao.delete(wlglDto);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void syncUpdateKcwh(WlglDto wlglDto) {
		// 从数据库取修改前的安全库存
		String aqkc_before = dao.getDto(wlglDto).getAqkc();
		// 从页面取修改后的安全库存
		String aqkc_after = wlglDto.getAqkc();
		dao.updateKcwh(wlglDto);
		InventoryModel inModel = new InventoryModel();
		inModel.setcInvCode(wlglDto.getWlbm());
		if(StringUtil.isNotBlank(wlglDto.getLbdm())) {
			inModel.setcInvABC(wlglDto.getLbdm());
			if("001".equals(wlglDto.getLbdm()))
				inModel.setcInvABC(null);
		}
		if(StringUtil.isNotBlank(wlglDto.getXgry())) {
			inModel.setcModifyPerson(wlglDto.getXgryzsxm()); //修改人员
		}
		if (!aqkc_after.equals(aqkc_before)) {
			inModel.setIsafeNum(aqkc_after);
		}
		if(StringUtil.isNotBlank(matridxdsflag)) {
			matridxInventoryDao.updateInventory(inModel);
		}
	}

	@Override
	public List<WlglDto> queryByGrsz(WlglDto wlglDto) {
		return dao.queryByGrsz(wlglDto);
	}

	@Override
	public List<WlglDto> queryByIds(WlglDto wlglDto) {
		return dao.queryByIds(wlglDto);
	}
}
