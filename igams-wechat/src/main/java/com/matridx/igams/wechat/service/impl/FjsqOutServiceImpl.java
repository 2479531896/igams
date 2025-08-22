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
import com.matridx.igams.wechat.dao.entities.SjsyglDto;
import com.matridx.igams.wechat.dao.entities.SjybztDto;
import com.matridx.igams.wechat.dao.post.IFjsqDao;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.igams.wechat.service.svcinterface.ISjsyglService;
import com.matridx.igams.wechat.service.svcinterface.ISjybztService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FjsqOutServiceImpl extends BaseBasicServiceImpl<FjsqDto, FjsqModel, IFjsqDao> implements IAuditService{

	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	ISjybztService sjybztService;
	@Autowired
	ICommonService commonservice;
	@Autowired
	ISjsyglService sjsyglService;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.wechat.externalurl:}")
	private String externalurl;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	private IFjsqService fjsqService;
	@Autowired
	ICommonService commonService;
	private Logger log = LoggerFactory.getLogger(FjsqServiceImpl.class);
	
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
			boolean hbMessageFlag = commonService.queryAuthMessage(t_fjsqDto.getHbid(),"INFORM_HB00004");
			if(auditParam.isPassOpe()) {
		        if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())||AuditStateEnum.AUDITING.equals(shgcDto.getAuditState())) {
		        	SjybztDto sjybztDto =new SjybztDto();
		        	List<String> ybztCskz1s=new ArrayList<>();
					ybztCskz1s.add("S");
					ybztCskz1s.add("T");
		        	sjybztDto.setSjid(t_fjsqDto.getSjid());
		        	sjybztDto.setYbztCskz1s(ybztCskz1s);
		        	List<SjybztDto> ybztList=sjybztService.getDtoList(sjybztDto);
		        	if(ybztList!=null&& ybztList.size()>0) {
		        		for(int i=0;i<ybztList.size();i++){
							if(!StatusEnum.CHECK_NO.getCode().equals(t_fjsqDto.getZt()) && "S".equals(ybztList.get(i).getYbztCskz1())) {
								log.error("量仅一次！");
								throw new BusinessException("msg","量仅一次！");
							}
							if(!t_fjsqDto.getJcxmcskz3().contains("REM") && "T".equals(ybztList.get(i).getYbztCskz1())){
								log.error("仅可去人源！");
								if(!StatusEnum.CHECK_NO.getCode().equals(t_fjsqDto.getZt())) {
									throw new BusinessException("msg","仅可去人源！");
								}
							}
						}
		        	}
		        }
			}
			//审核不通过
			if(AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
				String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
				String ICOMM_SH00001 = xxglService.getMsg("ICOMM_SH00001");
				fjsqDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				if(shgcDto.getSpgwcyDtos()!=null && shgcDto.getSpgwcyDtos().size()>0) {
					for (int i = 0; i < shgcDto.getSpgwcyDtos().size(); i++){
						if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
							talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00026,StringUtil.replaceMsg(ICOMM_SH00001,operator.getZsxm(),t_fjsqDto.getHzxm(), shgcDto.getShlbmc() ,t_fjsqDto.getLxmc(),shgcDto.getShxx_shyj(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
				SjsyglDto sjsyglDto=new SjsyglDto();
				sjsyglDto.setYwid(t_fjsqDto.getFjid());
				sjsyglDto.setSfjs("0");
				sjsyglDto.setXgry(operator.getYhid());
				sjsyglService.updateSfjs(sjsyglDto);
				//审核通过
			}else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())){
				String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
				String ICOMM_SH00016 = xxglService.getMsg("ICOMM_SH00016");
				fjsqDto.setZt(StatusEnum.CHECK_PASS.getCode());
				//发送钉钉消息
				if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0){
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
				String ICOMM_SH00023 = xxglService.getMsg("ICOMM_SH00023");
				String ICOMM_SH00018 = xxglService.getMsg("ICOMM_SH00018");
				String ICOMM_SH00004 = xxglService.getMsg("ICOMM_SH00004");
				String ICOMM_SH00005 = xxglService.getMsg("ICOMM_SH00005");
				String ICOMM_SH00028 = xxglService.getMsg("ICOMM_SH00028");
				//新增提交审核
				fjsqDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0 ){
					//获取审批岗位对应的所有yhm并且过滤yhm为空的数据
					List<String> yhms = shgcDto.getSpgwcyDtos().stream().filter((e) -> {return StringUtil.isNotBlank(e.getYhm());}).map(SpgwcyDto::getYhm).collect(Collectors.toList());
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
									String internalbtn;
									String external;
									if(StringUtil.isNotBlank(shgcDto.getXlcxh()) && "1".equals(shgcDto.getXlcxh())){
										String sign = URLEncoder.encode(commonservice.getSign(yhid),"UTF-8");
										//内网访问
										internalbtn = applicationurl+"/ws/auditProcess/auditPhoneList?yhid="+yhid+"&shxx_shjs="+jsid+"&shlx="+shgcDto.getShlb()+"&lrsj="+DateUtils.format(new Date())+"&sign="+sign;
										//外网访问
										external = externalurl+"/ws/auditProcess/auditPhoneList?yhid="+yhid+"&shxx_shjs="+jsid+"&shlx="+shgcDto.getShlb()+"&lrsj="+DateUtils.format(new Date())+"&sign="+sign;
									}else{
										String sign = URLEncoder.encode(commonservice.getSign(t_fjsqDto.getFjid()),"UTF-8");
										//内网访问
										internalbtn = applicationurl+"/ws/auditProcess/auditPhone?shxx_shry="+yhid+"&shxx_shjs="+jsid+"&ywid="+t_fjsqDto.getFjid()+"&sign="+sign+"&xlcxh="+shgcDto.getXlcxh()+"&business_url="+"/recheck/modRecheck&ywzd=fjid&shlbmc="+shgcDto.getShlbmc();
										//外网访问
										external = externalurl+"/ws/auditProcess/auditPhone?shxx_shry="+yhid+"&shxx_shjs="+jsid+"&ywid="+t_fjsqDto.getFjid()+"&sign="+sign+"&xlcxh="+shgcDto.getXlcxh()+"&business_url="+"/recheck/modRecheck&ywzd=fjid&shlbmc="+shgcDto.getShlbmc();
									}
									List<BtnJsonList> btnJsonLists = new ArrayList<>();
									BtnJsonList btnJsonList = new BtnJsonList();
									btnJsonList.setTitle("内网访问");
									btnJsonList.setActionUrl(internalbtn);
									btnJsonLists.add(btnJsonList);
									btnJsonList = new BtnJsonList();
									btnJsonList.setTitle("外网访问");
									btnJsonList.setActionUrl(external);
									btnJsonLists.add(btnJsonList);
									if(StringUtil.isNotBlank(ddid)){
										FjsqDto fjsqxx=dao.getDtoById(shgcDto.getYwid());
										if(StringUtil.isNotBlank(shgcDto.getXlcxh()) && "1".equals(shgcDto.getXlcxh())){
											talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),yhid,yhm, ddid,ICOMM_SH00003,StringUtil.replaceMsg(ICOMM_SH00028,shgcDto.getShlbmc(),DateUtils.format(new Date())),btnJsonLists,"1");
										}else{
											talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),yhid,yhm, ddid,ICOMM_SH00003,StringUtil.replaceMsg(ICOMM_SH00025,
													operator.getZsxm(),shgcDto.getShlbmc(),fjsqxx.getHzxm(),fjsqxx.getYblxmc(),fjsqxx.getNbbm()+"("+fjsqxx.getYbbh()+")",DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
										}
									}
								}
							}catch(Exception e){
								e.printStackTrace();
							}
						}
					}
				}
				//医学部复测审核通过后发送消息通知客户
				if("2".equals(shgcDto.getXlcxh()) && "1".equals(shgcDto.getShxx().getSftg()) && ("RECHECK".equals(t_fjsqDto.getFjlxdm()) || "REM".equals(t_fjsqDto.getFjlxdm()))&& "1".equals(t_fjsqDto.getBgbj()) && hbMessageFlag){
					fjsqService.sendRecheckMessageSec(t_fjsqDto);
				}
				//最后一步之前按照消息管理发送消息
				if(shgcDto.isEntryLastStep()){
					JcsjDto jcsjDto = new JcsjDto();
					jcsjDto.setJclb("DINGMESSAGETYPE");
					jcsjDto.setCsdm("RECHECK_OUT_TYPE");
					List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
					if(ddxxglDtos != null && ddxxglDtos.size() > 0){
						for(int i=0;i<ddxxglDtos.size();i++){
							if(StringUtil.isNotBlank(ddxxglDtos.get(i).getDdid())){
								try {
									String sign = URLEncoder.encode(commonservice.getSign(t_fjsqDto.getFjid()),"UTF-8");
									//内网访问
									String internalbtn =applicationurl+"/ws/recheck/viewRecheck?fjid="+t_fjsqDto.getFjid()+"&sign="+sign;
									//外网访问
									String external=externalurl+"/ws/recheck/viewRecheck?fjid="+t_fjsqDto.getFjid()+"&sign="+sign;
									List<BtnJsonList> btnJsonLists = new ArrayList<>();
									BtnJsonList btnJsonList = new BtnJsonList();
									btnJsonList.setTitle("内网访问");
									btnJsonList.setActionUrl(internalbtn);
									btnJsonLists.add(btnJsonList);
									btnJsonList = new BtnJsonList();
									btnJsonList.setTitle("外网访问");
									btnJsonList.setActionUrl(external);
									btnJsonLists.add(btnJsonList);
									talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),ddxxglDtos.get(i).getYhid(),ddxxglDtos.get(i).getYhm(), ddxxglDtos.get(i).getDdid(), ICOMM_SH00023, StringUtil.replaceMsg(ICOMM_SH00018,operator.getZsxm(),
											shgcDto.getShlbmc(),t_fjsqDto.getHzxm(),t_fjsqDto.getYblxmc(), t_fjsqDto.getLxmc(),t_fjsqDto.getBgbjmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
				SjsyglDto sjsyglDto=new SjsyglDto();
				sjsyglDto.setYwid(t_fjsqDto.getFjid());
				sjsyglDto.setSfjs("1");
				sjsyglDto.setJsry(t_fjsqDto.getLrry());
				sjsyglDto.setJsrq(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				sjsyglDto.setXgry(operator.getYhid());
				sjsyglService.updateSfjs(sjsyglDto);
				//发送钉钉消息--取消审核人员
				if(shgcDto.getNo_spgwcyDtos() != null && shgcDto.getNo_spgwcyDtos().size() > 0){
					try{
						int size = shgcDto.getNo_spgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), ICOMM_SH00004,StringUtil.replaceMsg(ICOMM_SH00005,operator.getZsxm(),shgcDto.getShlbmc() ,t_fjsqDto.getLxmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
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
				SjsyglDto sjsyglDto=new SjsyglDto();
				sjsyglDto.setYwid(fjid);
				sjsyglDto.setSfjs("0");
				sjsyglDto.setXgry(operator.getYhid());
				sjsyglService.updateSfjs(sjsyglDto);
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
