package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.LshkDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.SerialNumTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.ILshkService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.production.dao.entities.ComputationUnitModel;
import com.matridx.igams.production.dao.entities.InventoryModel;
import com.matridx.igams.production.dao.entities.SbysDto;
import com.matridx.igams.production.dao.entities.WlglDto;
import com.matridx.igams.production.dao.entities.WlgllsbDto;
import com.matridx.igams.production.dao.entities.WlgllsbModel;
import com.matridx.igams.production.dao.matridxsql.IMatridxInventoryDao;
import com.matridx.igams.production.dao.post.IWlgllsbDao;
import com.matridx.igams.production.service.svcinterface.ISbysService;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.igams.production.service.svcinterface.IWlgllsbService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
public class WlgllsbServiceImpl extends BaseBasicServiceImpl<WlgllsbDto, WlgllsbModel, IWlgllsbDao> implements IWlgllsbService,IAuditService{
	
	@Autowired
	ILshkService lshkservice;
	@Autowired
	IWlglService wlglService;
	@Autowired
	ICommonService commonservice;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IMatridxInventoryDao matridxInventoryDao;
	@Autowired
	DingTalkUtil talkUtil;
	@Value("${sqlserver.matridxds.flag:}")
	private String matridxdsflag;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	ISbysService sbysService;
	@Autowired
	private ICommonService commonService;
	private final Logger log = LoggerFactory.getLogger(WlgllsbServiceImpl.class);

	/** 
	 * 插入物料信息到临时表
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(WlgllsbDto wlgllsbDto){
		wlgllsbDto.setLsid(StringUtil.generateUUID());
		if(wlgllsbDto.getZt() == null || "".equals(wlgllsbDto.getZt())){
			wlgllsbDto.setZt(StatusEnum.CHECK_NO.getCode());
		}
		int result = dao.insert(wlgllsbDto);

		return result > 0;
	}
	/**
	 * 修改记录
	 */
	public boolean updateDto(WlgllsbDto wlgllsbDto){
		wlgllsbDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
		int result = dao.update(wlgllsbDto);

		return result > 0;
	}

	@Override
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
		// TODO Auto-generated method stub
		WlgllsbDto wlgllsbDto = (WlgllsbDto)baseModel;
		wlgllsbDto.setXgry(operator.getYhid());
		return update(wlgllsbDto);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}
		WlglDto wlglDto = new WlglDto();
		wlglDto.setXgry(operator.getYhid());
		WlgllsbDto wlgllsbDto = new WlgllsbDto();
		wlgllsbDto.setXgry(operator.getYhid());
		wlglDto.setXgryzsxm(operator.getZsxm());
//		String token = talkUtil.getToken();
		
		for(ShgcDto shgcDto : shgcList){
			wlgllsbDto.setLsid(shgcDto.getYwid());
			WlgllsbDto t_wlgllsbDto = dao.getDtoById(wlgllsbDto.getLsid());
			wlglDto.setWlid(t_wlgllsbDto.getWlid());
			WlglDto t_wlglDto = wlglService.getDtoById(wlglDto.getWlid());
			t_wlglDto.setWlid(shgcDto.getYwid());
			//审核退回
			if(AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())){

				wlgllsbDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				wlglDto.setZt(StatusEnum.CHECK_PASS.getCode());
				//更新状态
				dao.updateZt(wlgllsbDto);
				wlglService.update(wlglDto);
				
				//发送钉钉消息
				if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())){
					try{
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
										shgcDto.getSpgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00003"),xxglService.getMsg("ICOMM_SH00001",operator.getZsxm(),shgcDto.getShlbmc() ,t_wlglDto.getWlmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						log.error(e.getMessage());
					}
				}
				
				//审核通过
			}else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())){
				//判断单位和物料是否存在
//				if(StringUtil.isNotBlank(gmdsflag)) {
//					ComputationUnitModel selectInventory2016 = gmInventoryDao.selectmatridx(t_wlgllsbDto.getJldw());
//					if(selectInventory2016==null)
//						throw new BusinessException("msg","计量单位不存在或有误！(gm)");
//					if(gmInventoryDao.select_matridx_InvName(t_wlgllsbDto.getWlbm()) == null)
//						throw new BusinessException("msg","U8系统物料不存在(gm)");
//				}
//				if(StringUtil.isNotBlank(matridxdsflag)) {
//					ComputationUnitModel selectInventory = matridxInventoryDao.selectInventory(t_wlgllsbDto.getJldw());
//					ComputationUnitModel selectInventory2017 = matridxInventoryDao.selectInventory2017(t_wlgllsbDto.getJldw());
//					ComputationUnitModel selectInventory2020 = matridxInventoryDao.selectInventory2020(t_wlgllsbDto.getJldw());
//					if(selectInventory==null || selectInventory2017==null || selectInventory2020==null)
//						throw new BusinessException("msg","计量单位不存在或有误！(matridx)");
//					if(matridxInventoryDao.select_Inventory_InvName(t_wlgllsbDto.getWlbm()) == null)
//						throw new BusinessException("msg","U8系统物料不存在(matridx)");
//				}
				//判断是否为设备 并且物料名称或规格有修改
				if ("3".equals(t_wlglDto.getLbcskz1())&&(!t_wlglDto.getWlmc().equals(t_wlgllsbDto.getWlmc())||!t_wlglDto.getGg().equals(t_wlgllsbDto.getGg()))){
					//修改sbys表数据的sbmc和ggxh
					SbysDto sbysDto = new SbysDto();
					sbysDto.setXgry(operator.getYhid());
					sbysDto.setWlid(t_wlgllsbDto.getWlid());
					sbysDto.setSbmc(t_wlgllsbDto.getWlmc());
					sbysDto.setGgxh(t_wlgllsbDto.getGg());
					sbysService.updateForWlid(sbysDto);
				}
				if(StringUtil.isNotBlank(matridxdsflag)) {
					ComputationUnitModel selectInventory = matridxInventoryDao.selectInventory(t_wlgllsbDto.getJldw());
					if(selectInventory==null)
						throw new BusinessException("msg","计量单位不存在或有误！(matridx)");
					if(matridxInventoryDao.select_Inventory_InvName(t_wlgllsbDto.getWlbm()) == null)
						throw new BusinessException("msg","U8系统物料不存在(matridx)");
				}
				LshkDto lshkDto = new LshkDto();
				if(!t_wlgllsbDto.getWllb().equals(t_wlglDto.getWllb()) || !t_wlgllsbDto.getWlzlb().equals(t_wlglDto.getWlzlb())){
					//生成内部用流水号
					lshkDto.setWllb(t_wlgllsbDto.getWllbdm());
					lshkDto.setWlzlb(t_wlgllsbDto.getWlzlbdm());
					String wlbm = lshkservice.doMakeSerNum(SerialNumTypeEnum.MATER_CODE, lshkDto);
					t_wlgllsbDto.setWlbm(wlbm);
				}
				//将临时表数据同步至正式表
				wlglDto.setWlbm(t_wlgllsbDto.getWlbm());
				wlglDto.setJwlbm(t_wlgllsbDto.getJwlbm());
				wlglDto.setWllb(t_wlgllsbDto.getWllb());
				wlglDto.setWlzlb(t_wlgllsbDto.getWlzlb());
				wlglDto.setWlmc(t_wlgllsbDto.getWlmc());
				wlglDto.setDlgys(t_wlgllsbDto.getDlgys());
				wlglDto.setScs(t_wlgllsbDto.getScs());
				wlglDto.setYchh(t_wlgllsbDto.getYchh());
				wlglDto.setGg(t_wlgllsbDto.getGg());
				wlglDto.setJldw(t_wlgllsbDto.getJldw());
				wlglDto.setBctj(t_wlgllsbDto.getBctj());
				wlglDto.setBzq(t_wlgllsbDto.getBzq());
				wlglDto.setBzqflg(t_wlgllsbDto.getBzqflg());
				wlglDto.setSfwxp(t_wlgllsbDto.getSfwxp());
				wlglDto.setBz(t_wlgllsbDto.getBz());
				wlglDto.setZt(StatusEnum.CHECK_PASS.getCode());
				wlglDto.setScbj(t_wlgllsbDto.getScbj());
				wlglDto.setQdl(t_wlgllsbDto.getQdl());
				wlglDto.setHq(t_wlgllsbDto.getHq());
				wlglDto.setWlzlbtc(t_wlgllsbDto.getWlzlbtc());
				wlglDto.setCpzch(t_wlgllsbDto.getCpzch());
				wlglDto.setRf(t_wlgllsbDto.getRf());
				//删除临时表此条数据
				t_wlgllsbDto.setScry(operator.getYhid());
				t_wlgllsbDto.setZt(StatusEnum.CHECK_PASS.getCode());
				dao.updateSc(t_wlgllsbDto);
				wlglService.update(wlglDto);
//				amqpTempl.convertAndSend("sys.igams", "sys.igams.wlgl.update",JSONObject.toJSONString(wlglDto));
				//发送钉钉消息
				if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())){
					try{
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
										shgcDto.getSpgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00006"),xxglService.getMsg("ICOMM_SH00007",operator.getZsxm(),shgcDto.getShlbmc() ,t_wlglDto.getWlmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss"), t_wlglDto.getWlbm()));
							}
						}
					}catch(Exception e){
						log.error(e.getMessage());
					}
				}
				updateInventory(wlglDto);
				wlglDto.setSyncFlag("updateWlgl");
				RabbitUtils.sendSyncWlRabbitMsg(JSON.toJSONString(wlglDto),"sys.igams.wlgl.updateWlgl(deleteWlgl,updateKcwh)");
			//审核中
			}else{
				wlgllsbDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				wlglDto.setZt(StatusEnum.CHECK_MALMOD.getCode());
				//更新状态
				dao.updateZt(wlgllsbDto);
				wlglService.update(wlglDto);
				
				//发送钉钉消息--审核人员
				if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())){
					try{
						int size = shgcDto.getSpgwcyDtos().size();
						String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
						String ICOMM_SH00017 = xxglService.getMsg("ICOMM_SH00017");
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								String sign = URLEncoder.encode(commonservice.getSign(t_wlglDto.getWlid()), StandardCharsets.UTF_8);
								//内网访问
								String internalbtn = applicationurl + urlPrefix + "/ws/auditProcess/auditPhone?shxx_shry="+shgcDto.getSpgwcyDtos().get(i).getYhid()+"&shxx_shjs="+shgcDto.getSpgwcyDtos().get(i).getJsid()+"&ywid="+t_wlglDto.getWlid()+"&sign="+sign+"&xlcxh="+shgcDto.getXlcxh()+"&business_url="+"/production/materiel/modMaterTemp&ywzd=wlid&shlbmc="+shgcDto.getShlbmc();
								//外网访问
								//String external=externalurl+"/ws/auditProcess/auditPhone?shxx_shry="+shgcDto.getSpgwcyDtos().get(i).getYhid()+"&shxx_shjs="+shgcDto.getSpgwcyDtos().get(i).getJsid()+"&ywid="+t_wlglDto.getWlid()+"&sign="+sign+"&xlcxh="+shgcDto.getXlcxh()+"&business_url="+"/production/materiel/modMaterTemp&ywzd=wlid&shlbmc="+shgcDto.getShlbmc();
								List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
								OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
								btnJsonList.setTitle("详细");
								btnJsonList.setActionUrl(internalbtn);
								btnJsonLists.add(btnJsonList);
								/*btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
								btnJsonList.setTitle("外网访问");
								btnJsonList.setActionUrl(external);
								btnJsonLists.add(btnJsonList);*/
								talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(),ICOMM_SH00003,StringUtil.replaceMsg(ICOMM_SH00017,
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
						int size = shgcDto.getNo_spgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,t_wlglDto.getWlmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						log.error(e.getMessage());
					}
				}
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

		//审核回调方法
		for(ShgcDto shgcDto : shgcList){
			String lsid = shgcDto.getYwid();
			//定义物料基本信息
			WlgllsbDto wlgllsbDto = new WlgllsbDto();
			wlgllsbDto.setXgry(operator.getYhid());
			wlgllsbDto.setLsid(lsid);
			
			wlgllsbDto.setZt(StatusEnum.CHECK_NO.getCode());
			//更新状态
			dao.updateZt(wlgllsbDto);
			
			WlgllsbDto t_WlgllsbDto = dao.getDtoById(lsid);
			
			WlglDto wlglDto = new WlglDto();
			wlglDto.setXgry(operator.getYhid());
			wlglDto.setWlid(t_WlgllsbDto.getWlid());
			wlglDto.setZt(StatusEnum.CHECK_PASS.getCode());
			
			wlglService.update(wlglDto);
		}
		
		return true;
	}

	@Override
	public List<WlgllsbDto> getPagedModAuditList(WlgllsbDto wlgllsbDto) {
		// TODO Auto-generated method stub
		//获取人员ID和履历号
		List<WlgllsbDto> t_sqList = dao.getPagedModAuditIdList(wlgllsbDto);
		
		if(CollectionUtils.isEmpty(t_sqList))
			return t_sqList;
		
		List<WlgllsbDto> sqList = dao.getModAuditListByIds(t_sqList);
		
		commonservice.setSqrxm(sqList);
		
		jcsjService.handleCodeToValue(sqList,
				new BasicDataTypeEnum[] {
						BasicDataTypeEnum.MATERIEL_TYPE,BasicDataTypeEnum.MATERIEL_SUBTYPE}, new String[] {
				"wllb:wllbmc","wlzlb:wlzlbmc"});
		return sqList;
	}
	
	/**
	 * 通过物料id查询临时表数据
	 */
	@Override
	public List<WlgllsbDto> getDtoListByWlid(WlgllsbDto wlgllsbDto) {
		return dao.getDtoListByWlid(wlgllsbDto);
	}
	
	/**
	 * 物料临时修改提交保存
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public WlgllsbDto modSubmitSaveMaterTemp(WlgllsbDto wlgllsbDto) {
		//判断临时表中是否有该物料信息
		List<WlgllsbDto> wlgllsbDtoList = getDtoListByWlid(wlgllsbDto);
		if(CollectionUtils.isEmpty(wlgllsbDtoList)){
			//插入物料信息至临时表
			WlgllsbDto t_wlgllsbDto = wlglService.selectWllsbDtoByWlid(wlgllsbDto.getWlid());
			boolean isSuccess = insertDto(t_wlgllsbDto);
			if(!isSuccess){
				return null;
			}
		}
		//判断临时表数据状态 
		if(StatusEnum.CHECK_UNPASS.getCode().equals(wlgllsbDto.getZt())){
			//修改临时表数据
			boolean isSuccess = updateDto(wlgllsbDto);
			if(!isSuccess){
				return null;
			}
		}else{
			//新增数据至临时表
			wlgllsbDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
			boolean isSuccess = insertDto(wlgllsbDto);
			if(!isSuccess){
				return null;
			}
		}
		return wlgllsbDto;
	}

	/**
	 *
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void updateInventory(WlglDto wlglDto){
		InventoryModel inModel = new InventoryModel();
		//从基础数据里边查询单位
		ComputationUnitModel cModel = new ComputationUnitModel();
//		if(StringUtil.isNotBlank(gmdsflag)) {
//			cModel = gmInventoryDao.selectmatridx(wlglDto.getJldw());
//		}else
		if(StringUtil.isNotBlank(matridxdsflag)){
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
		inModel.setcInvCode(wlglDto.getWlbm());
		inModel.setcInvName(wlglDto.getWlmc());
		inModel.setcInvDefine2(wlglDto.getScs());
		inModel.setcInvDefine3(wlglDto.getYchh());
		inModel.setcInvStd(wlglDto.getGg());				//规格
		inModel.setcInvDefine6(wlglDto.getBctj());
		inModel.setcModifyPerson(wlglDto.getXgryzsxm()); //修改人员
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
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
		return null;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
		Map<String, Object> map =new HashMap<>();
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>)param.get("ywids");
		WlgllsbDto wlgllsbDto = new WlgllsbDto();
		wlgllsbDto.setIds(ids);
		List<WlgllsbDto> dtoList = dao.getDtoList(wlgllsbDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(WlgllsbDto dto:dtoList){
				list.add(dto.getLsid());
			}
		}
		map.put("list",list);
		return map;
	}
}
