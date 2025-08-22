package com.matridx.igams.wechat.service.impl;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.dao.entities.CwglDto;
import com.matridx.igams.wechat.dao.entities.CwglModel;
import com.matridx.igams.wechat.dao.post.ICwglDao;
import com.matridx.igams.wechat.service.svcinterface.ICwglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CwglServiceImpl extends BaseBasicServiceImpl<CwglDto, CwglModel, ICwglDao> implements ICwglService,IAuditService{
	
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	@Autowired
	ICommonService commonservice;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
//	@Value("${matridx.wechat.externalurl:}")
//	String externalurl;
	@Autowired
	IShgcService shgcService;
	@Autowired
	ICommonService commonService;
	
	/**
	 * 错误列表
	 * @param cwglDto
	 * @return
	 */
	public List<CwglDto> getPagedDtoList(CwglDto cwglDto){
		List<CwglDto> t_List=dao.getPagedDtoList(cwglDto);
		try{
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_SAMPNUM.getCode(), "zt", "cwid", new String[]{
				StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t_List;
	}

    /**
	 * 插入错误信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(CwglDto cwglDto){
		if(StringUtil.isBlank(cwglDto.getCwid())){
			cwglDto.setCwid(StringUtil.generateUUID());
		}
		int result = dao.insert(cwglDto);
        return result != 0;
    }

	@Override
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam)
			throws BusinessException {
		// TODO Auto-generated method stub
		CwglDto cwglDto = (CwglDto)baseModel;
		cwglDto.setXgry(operator.getYhid());
		return true;
	}

	@Override
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}
//		String token = talkUtil.getToken();
		for(ShgcDto shgcDto : shgcList){
			CwglDto cwglDto = new CwglDto();
			
			cwglDto.setCwid(shgcDto.getYwid());
			
			cwglDto.setXgry(operator.getYhid());
			
			//审核退回
			if(AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())){

				cwglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				//发送钉钉消息
				if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0){
					try{
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
										shgcDto.getSpgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00003"),xxglService.getMsg("ICOMM_SH00001",operator.getZsxm(),shgcDto.getShlbmc() ,cwglDto.getYsnr(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				//审核通过
			}else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())){
				CwglDto t_cwglDto = dao.getDtoById(shgcDto.getYwid());
				//内部采用系统时间，所以只需随意设置就可以
				cwglDto.setZt(StatusEnum.CHECK_PASS.getCode());
				/*  */
				//发送钉钉消息
				if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0){
					try{
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
										shgcDto.getSpgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00006"),xxglService.getMsg("ICOMM_SH00007",operator.getZsxm(),shgcDto.getShlbmc() ,t_cwglDto.getYsnr(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				//审核中	
			}else{   //新增操作
				cwglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				//发送钉钉消息
				if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0){
					try{
						CwglDto t_cwglDto = dao.getDtoById(shgcDto.getYwid());
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								String sign = URLEncoder.encode(commonservice.getSign(t_cwglDto.getCwid()),"UTF-8");
								//内网访问
								String internalbtn = applicationurl+"/ws/auditProcess/auditPhone?shxx_shry="+shgcDto.getSpgwcyDtos().get(i).getYhid()+"&shxx_shjs="+shgcDto.getSpgwcyDtos().get(i).getJsid()+"&ywid="+t_cwglDto.getCwid()+"&sign="+sign+"&business_url="+"/samplenum/AuditSampleNum&ywzd=cwid&shlbmc="+shgcDto.getShlbmc();
								//外网访问
								//String external=externalurl+"/ws/auditProcess/auditPhone?shxx_shry="+shgcDto.getSpgwcyDtos().get(i).getYhid()+"&shxx_shjs="+shgcDto.getSpgwcyDtos().get(i).getJsid()+"&ywid="+t_cwglDto.getCwid()+"&sign="+sign+"&business_url="+"/samplenum/AuditSampleNum&ywzd=cwid&shlbmc="+shgcDto.getShlbmc();
								List<BtnJsonList> btnJsonLists = new ArrayList<>();
								BtnJsonList btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("详细");
								btnJsonList.setActionUrl(internalbtn);
								btnJsonLists.add(btnJsonList);
								talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
										shgcDto.getSpgwcyDtos().get(i).getYhid(),xxglService.getMsg("ICOMM_SH00003"),xxglService.getMsg("ICOMM_SH00017",
										operator.getZsxm(),shgcDto.getShlbmc() ,t_cwglDto.getYsnr(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				//发送钉钉消息--取消审核人员
				if(shgcDto.getNo_spgwcyDtos() != null && shgcDto.getNo_spgwcyDtos().size() > 0){
					try{
						CwglDto t_cwglDto = dao.getDtoById(shgcDto.getYwid());
						int size = shgcDto.getNo_spgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
										shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,t_cwglDto.getYsnr(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			//更新状态
			dao.updateZt(cwglDto);
		}
		return true;
	}

	@Override
	public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}
		if(auditParam.isCancelOpe()) {
			//审核回调方法
			for(ShgcDto shgcDto : shgcList){
				String cwid = shgcDto.getYwid();
				CwglDto cwglDto = new CwglDto();
				cwglDto.setXgry(operator.getYhid());
				cwglDto.setCwid(cwid);
				cwglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				dao.update(cwglDto);
			}
		}else {
			//审核回调方法
			for(ShgcDto shgcDto : shgcList){
				String cwid = shgcDto.getYwid();
				CwglDto cwglDto = new CwglDto();
				cwglDto.setXgry(operator.getYhid());
				cwglDto.setCwid(cwid);
				cwglDto.setZt(StatusEnum.CHECK_NO.getCode());
				dao.update(cwglDto);
			}
		}
		return true;
	}

	@Override
	public List<CwglDto> getPagedAuditList(CwglDto cwglDto) {
		// TODO Auto-generated method stub
		//获取人员ID和履历号
		List<CwglDto> t_sqList = dao.getPagedAuditIdList(cwglDto);
		
		if(t_sqList == null || t_sqList.size() == 0)
			return t_sqList;
		
		List<CwglDto> sqList = dao.getAuditListByIds(t_sqList);
		
		commonservice.setSqrxm(sqList);
		
		
		return sqList;
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) throws BusinessException {
		return null;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) throws BusinessException {
		Map<String, Object> map =new HashMap<>();
		List<String> ids = (List<String>)param.get("ywids");
		CwglDto cwglDto = new CwglDto();
		cwglDto.setIds(ids);
		List<CwglDto> dtoList = dao.getDtoList(cwglDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(CwglDto dto:dtoList){
				list.add(dto.getCwid());
			}
		}
		map.put("list",list);
		return map;
	}
}
