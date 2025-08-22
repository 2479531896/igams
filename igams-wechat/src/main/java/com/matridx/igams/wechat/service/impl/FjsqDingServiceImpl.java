package com.matridx.igams.wechat.service.impl;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.FjsqModel;
import com.matridx.igams.wechat.dao.post.IFjsqDao;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FjsqDingServiceImpl extends BaseBasicServiceImpl<FjsqDto, FjsqModel, IFjsqDao> implements IAuditService{

	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	@Autowired
	ICommonService commonservice;
	@Autowired
	IDdxxglService ddxxglService;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	//@Value("${matridx.wechat.externalurl:}")
	//private String externalurl;
	@Autowired
	private IFjsqService fjsqService;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	ICommonService commonService;
	@Override
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam)
			throws BusinessException {
		// TODO Auto-generated method stub
		FjsqDto  fjsqDto = (FjsqDto) baseModel;
		int result=dao.update(fjsqDto);
        return result != 0;
    }

	@Override
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
	
			throws BusinessException {
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}
//		String token = talkUtil.getToken();
		for (ShgcDto shgcDto :shgcList){
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setFjid(shgcDto.getYwid());
			fjsqDto.setXgry(operator.getYhid());
			FjsqDto t_fjsqDto=dao.getDto(fjsqDto);
			boolean hbMessageFlag = commonService.queryAuthMessage(t_fjsqDto.getHbid(),"INFORM_HB00002");
			//审核不通过
			if(AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
				String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
				String ICOMM_SH00001 = xxglService.getMsg("ICOMM_SH00001");
				fjsqDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())) {
					for (int i = 0; i < shgcDto.getSpgwcyDtos().size(); i++){
						if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
							
							talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00026,StringUtil.replaceMsg(ICOMM_SH00001,operator.getZsxm(),t_fjsqDto.getHzxm(),shgcDto.getShlbmc() ,t_fjsqDto.getLxmc(),
									shgcDto.getShxx_shyj(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
				//审核通过
			}else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())){
				String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
				String ICOMM_SH00016 = xxglService.getMsg("ICOMM_SH00016");
				fjsqDto.setZt(StatusEnum.CHECK_PASS.getCode());
				//发送钉钉消息
				if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())){
					try{
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00006,StringUtil.replaceMsg(ICOMM_SH00016,operator.getZsxm(),shgcDto.getShlbmc() ,t_fjsqDto.getLxmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				//通知客户复检信息
				if("1".equals(t_fjsqDto.getBgbj()) && hbMessageFlag)
					fjsqService.sendRecheckMessage(t_fjsqDto);
			}else {
				String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
				String ICOMM_SH00025 = xxglService.getMsg("ICOMM_SH00025");
				String ICOMM_SH00024 = xxglService.getMsg("ICOMM_SH00024");
				String ICOMM_SH00020 = xxglService.getMsg("ICOMM_SH00020");
				String ICOMM_SH00004 = xxglService.getMsg("ICOMM_SH00004");
				String ICOMM_SH00005 = xxglService.getMsg("ICOMM_SH00005");
				//新增提交审核
				fjsqDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				if(!CollectionUtils.isEmpty(shgcDto.getSpgwcyDtos())){
					//获取审批岗位对应的所有yhm并且过滤yhm为空的数据
					List<String> yhms = shgcDto.getSpgwcyDtos().stream().map(SpgwcyDto::getYhm).filter(StringUtil::isNotBlank).collect(Collectors.toList());
					if (!CollectionUtils.isEmpty(yhms)){
						Map<String, Object> map = new HashMap<>();
						map.put("yhms",yhms);
						map.put("jcdw",t_fjsqDto.getJcdw());
						//查询有检测单位权限用户
						List<Map<String,String>> xtyhs = dao.getXtyhByMap(map);
						if (!CollectionUtils.isEmpty(xtyhs)){
							try{
								for (Map<String, String> xtyh : xtyhs) {
									String yhid = xtyh.get("yhid");
									String ddid = xtyh.get("ddid");
									String jsid = xtyh.get("jsid");
									String yhm = xtyh.get("yhm");
									String sign = URLEncoder.encode(commonservice.getSign(t_fjsqDto.getFjid()), StandardCharsets.UTF_8);
									//内网访问
									String internalbtn = applicationurl+"/ws/auditProcess/auditPhone?shxx_shry="+yhid+"&shxx_shjs="+jsid+"&ywid="+t_fjsqDto.getFjid()+"&sign="+sign+"&xlcxh="+shgcDto.getXlcxh()+"&business_url="+"/recheck/modRecheck&ywzd=fjid&shlbmc="+shgcDto.getShlbmc();
									//外网访问
									//String external=externalurl+"/ws/auditProcess/auditPhone?shxx_shry="+shgcDto.getSpgwcyDtos().get(i).getYhid()+"&shxx_shjs="+shgcDto.getSpgwcyDtos().get(i).getJsid()+"&ywid="+t_fjsqDto.getFjid()+"&sign="+sign+"&xlcxh="+shgcDto.getXlcxh()+"&business_url="+"/recheck/modRecheck&ywzd=fjid&shlbmc="+shgcDto.getShlbmc();
									List<BtnJsonList> btnJsonLists = new ArrayList<>();
									BtnJsonList btnJsonList = new BtnJsonList();
									btnJsonList.setTitle("详细");
									btnJsonList.setActionUrl(internalbtn);
									btnJsonLists.add(btnJsonList);
									/*btnJsonList = new BtnJsonList();
										btnJsonList.setTitle("外网访问");
										btnJsonList.setActionUrl(external);
										btnJsonLists.add(btnJsonList);*/
									if(StringUtil.isNotBlank(yhm)){
										FjsqDto fjsqxx=dao.getDtoById(shgcDto.getYwid());
										talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),yhid,yhm, ddid,ICOMM_SH00003,StringUtil.replaceMsg(ICOMM_SH00025,
												operator.getZsxm(),shgcDto.getShlbmc(),fjsqxx.getLxmc(),fjsqxx.getHzxm(),fjsqxx.getYblxmc(),fjsqxx.getNbbm()+"("+fjsqxx.getYbbh()+")",DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
									}
								}
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}
				}
				//最后一步之前按照消息管理发送消息
				if(shgcDto.isEntryLastStep()){
					JcsjDto jcsjDto = new JcsjDto();
		    		jcsjDto.setJclb("DINGMESSAGETYPE");
		    		jcsjDto.setCsdm("RECHECK_DING_TYPE");
		    		List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
					if(!CollectionUtils.isEmpty(ddxxglDtos)){
                        for (DdxxglDto ddxxglDto : ddxxglDtos) {
                            if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
                                String sign = URLEncoder.encode(commonservice.getSign(t_fjsqDto.getFjid()), StandardCharsets.UTF_8);
                                //内网访问
                                String internalbtn = applicationurl + "/ws/recheck/viewRecheck?fjid=" + t_fjsqDto.getFjid() + "&sign=" + sign;
                                //外网访问
                                //String external=externalurl+"/ws/recheck/viewRecheck?fjid="+t_fjsqDto.getFjid()+"&sign="+sign;
                                List<BtnJsonList> btnJsonLists = new ArrayList<>();
                                BtnJsonList btnJsonList = new BtnJsonList();
                                btnJsonList.setTitle("详细");
                                btnJsonList.setActionUrl(internalbtn);
                                btnJsonLists.add(btnJsonList);
									/*btnJsonList = new BtnJsonList();
									btnJsonList.setTitle("外网访问");
									btnJsonList.setActionUrl(external);
									btnJsonLists.add(btnJsonList);*/
                                talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),ddxxglDto.getYhid(),ddxxglDto.getYhm(), ddxxglDto.getDdid(), ICOMM_SH00024, StringUtil.replaceMsg(ICOMM_SH00020, operator.getZsxm(),
                                        shgcDto.getShlbmc(), t_fjsqDto.getHzxm(), t_fjsqDto.getYblxmc(), t_fjsqDto.getLxmc(), t_fjsqDto.getBgbjmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
                            }
                        }
					}
				}
				//发送钉钉消息--取消审核人员
				if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())){
					try{
						int size = shgcDto.getNo_spgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
										shgcDto.getNo_spgwcyDtos().get(i).getYhid(), ICOMM_SH00004,StringUtil.replaceMsg(ICOMM_SH00005,operator.getZsxm(),shgcDto.getShlbmc() ,t_fjsqDto.getLxmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
			dao.updateZt(fjsqDto);
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
				String fjid = shgcDto.getYwid();
				FjsqDto fjsqDto = new 	FjsqDto();
				fjsqDto.setXgry(operator.getYhid());
				fjsqDto.setFjid(fjid);
				fjsqDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				dao.update(fjsqDto);
			}
		}else {
			//审核回调方法
			for(ShgcDto shgcDto : shgcList){
				String fjid = shgcDto.getYwid();
				FjsqDto fjsqDto = new 	FjsqDto();
				fjsqDto.setXgry(operator.getYhid());
				fjsqDto.setFjid(fjid);
				fjsqDto.setZt(StatusEnum.CHECK_NO.getCode());
				dao.update(fjsqDto);
			}
		}
		return true;
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) throws BusinessException {
		return null;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) throws BusinessException {
		Map<String, Object> map =new HashMap<>();
		List<String> ids = (List<String>)param.get("ywids");
		FjsqDto fjsqDto = new FjsqDto();
		fjsqDto.setIds(ids);
		List<FjsqDto> dtoList = dao.getDtoList(fjsqDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(FjsqDto dto:dtoList){
				list.add(dto.getFjid());
			}
		}
		map.put("list",list);
		return map;
	}

}
