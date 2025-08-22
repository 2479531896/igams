package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.WkmxPcrResultModel;
import com.matridx.igams.common.dao.entities.XtshDto;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.PersonalSettingEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.SjyzDto;
import com.matridx.igams.wechat.dao.entities.SjyzModel;
import com.matridx.igams.wechat.dao.entities.SjyzjgDto;
import com.matridx.igams.wechat.dao.post.ISjxxDao;
import com.matridx.igams.wechat.dao.post.ISjyzDao;
import com.matridx.igams.wechat.service.svcinterface.ISjkzxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxCommonService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxWsService;
import com.matridx.igams.wechat.service.svcinterface.ISjyzService;
import com.matridx.igams.wechat.service.svcinterface.ISjyzjgService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SjyzServiceImpl extends BaseBasicServiceImpl<SjyzDto, SjyzModel, ISjyzDao> implements ISjyzService,IAuditService,IFileImport{
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IShgcService shgcService;
	@Autowired
	ICommonService commonservice;
	@Autowired
	IDdxxglService ddxxglService;
//	@Autowired
//	IFjsqService fjsqService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.wechat.externalurl:}")
	private String externalurl;
	// 微信通知标本状态的模板ID
	@Value("${matridx.wechat.ybzt_templateid:}")
	private String ybzt_templateid = null;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	@Autowired
	ISjyzjgService sjyzjgService;
	@Autowired
	ISjxxDao sjxxDao;
	@Autowired
	ISjxxCommonService sjxxCommonService;
	@Autowired
	IXxglService xxglservice;
	@Autowired
	IGrszService grszService;
	@Value("${matridx.fileupload.releasePath:}")
	private String releaseFilePath;
	@Value("${matridx.fileupload.prefix}")
	private String prefix;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	private ICommonService commonService;
	@Autowired
	private ISjkzxxService sjkzxxService;
	@Autowired
	private IXxdyService xxdyService;
	@Autowired
	ISjxxWsService sjxxWsService;
	
	private Logger logger = LoggerFactory.getLogger(SjyzServiceImpl.class);
	/**
	 * 插入送检验证信息
	 */
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(SjyzDto sjyzDto) {
		if(StringUtil.isBlank(sjyzDto.getYzid())){
			sjyzDto.setYzid(StringUtil.generateUUID());
		}
		String yzjg = "";
		if (StringUtil.isNotBlank(sjyzDto.getYzjg())){
			yzjg = sjyzDto.getYzjg();
			String[] temp = yzjg.split(",");
			String newYzjg = "";
			for (int i = 0; i < temp.length; i++) {
				if (!temp[i].contains("HEX")){
					newYzjg += ","+temp[i];
				}
			}
			sjyzDto.setYzjg(newYzjg.substring(1));
		}
		if ("1".equals(sjyzDto.getXsbj())){
			sjyzDto.setYzjg(null);
		}
		int result=dao.insert(sjyzDto);
		if(result==0)
			return false;
		if (StringUtil.isBlank(yzjg)){
			List<JcsjDto> matridx_jcsj_verify_result = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.VERIFY_RESULT.getCode());
			for (JcsjDto jcsjDto : matridx_jcsj_verify_result) {
				if (jcsjDto.getFcsid().equals(sjyzDto.getYzlb())){
					yzjg +=","+jcsjDto.getCsmc()+":";
				}
			}
			if (StringUtil.isNotBlank(yzjg)){
				yzjg = yzjg.substring(1);
			}
		}
		if (StringUtil.isNotBlank(yzjg)){
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setFcsid(sjyzDto.getYzlb());
			List<JcsjDto> jcsjDtos = jcsjService.getJcsjDtoList(jcsjDto);
			SjyzjgDto sjyzjgDto = new SjyzjgDto();
			sjyzjgDto.setLrry(sjyzDto.getLrry());
			sjyzjgDto.setYzid(sjyzDto.getYzid());
			sjyzjgDto.setYzlb(sjyzDto.getYzlb());
//			Integer xh = 1;
			for (JcsjDto jcsj:jcsjDtos) {
				sjyzjgDto.setYzjg(jcsj.getCsid());
				String[] temp = yzjg.split(",");
				for (int i = 0; i < temp.length; i++) {
					String[] str = temp[i].split(":");
					for (int j = 0; j < str.length; j++) {
						if (str[j].equals(jcsj.getCsmc())) {
							sjyzjgDto.setYzjgid(StringUtil.generateUUID());
							sjyzjgDto.setYzjg(jcsj.getCsid());
							Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.VERIFICATION_RESULT});
						    List<JcsjDto> jcsjDtoList = jcsjlist.get(BasicDataTypeEnum.VERIFICATION_RESULT.getCode()); //报告结果
							if (j+1 < str.length){
									for (JcsjDto jcsjDto1:jcsjDtoList) {
										if (str[j+1].equals(jcsjDto1.getCsmc())){
											sjyzjgDto.setJl(jcsjDto1.getCsid());
										}
									}
							}else{
								sjyzjgDto.setJl(null);
							}
							boolean success = sjyzjgService.insert(sjyzjgDto);
							if(!success)
								return false;
						}
					}
				}
			}
		}
		//同步至微信服务器
		if (StringUtil.isNotBlank(menuurl))
			amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.ADD_INSPECTION_VERIFICATION.getCode(), JSONObject.toJSONString(sjyzDto));
		return true;
	}
	
	
	/**
	 * 修改保存送检验证信息
	 * @param sjyzDto
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateSjyzxx(SjyzDto sjyzDto) {
		// TODO Auto-generated method stub
		String yzjg = "";
		if (StringUtil.isNotBlank(sjyzDto.getYzjg())){
			yzjg = sjyzDto.getYzjg();
			String[] temp = yzjg.split(",");
			String newYzjg = "";
			for (int i = 0; i < temp.length; i++) {
				if (!temp[i].contains("HEX")){
					newYzjg += ","+temp[i];
				}
			}
			sjyzDto.setYzjg(newYzjg.substring(1));
		}
		if (StringUtil.isBlank(sjyzDto.getXsbj()) || !"1".equals(sjyzDto.getXsbj()) || !"80".equals(sjyzDto.getZt())){
			sjyzDto.setYzjg(null);
		}
		int result = dao.update(sjyzDto);
		if(result==0) {
			return false;
		}

		if (StringUtil.isNotBlank(yzjg)){
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setFcsid(sjyzDto.getYzlb());
			List<JcsjDto> jcsjDtos = jcsjService.getJcsjDtoList(jcsjDto);
			SjyzjgDto sjyzjgDto = new SjyzjgDto();
			sjyzjgDto.setYzid(sjyzDto.getYzid());
			sjyzjgDto.setYzlb(sjyzDto.getYzlb());
			if (sjyzDto.getFjids() != null && sjyzDto.getFjids().size() > 0){
				sjyzjgDto.setScry(sjyzDto.getXgry());
				sjyzjgService.deleteByYzid(sjyzjgDto);
				sjyzjgDto.setScry(null);
			}



			for (JcsjDto jcsj:jcsjDtos) {
				sjyzjgDto.setYzjg(jcsj.getCsid());
				String[] temp = yzjg.split(",");
				for (int i = 0; i < temp.length; i++) {
					String[] str = temp[i].split(":");
					for (int j = 0; j < str.length; j++) {
						if (str[j].equals(jcsj.getCsmc())) {
							sjyzjgDto.setYzjg(jcsj.getCsid());
							Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.VERIFICATION_RESULT});
							List<JcsjDto> jcsjDtoList = jcsjlist.get(BasicDataTypeEnum.VERIFICATION_RESULT.getCode()); //报告结果
							if (j+1 < str.length){
									for (JcsjDto jcsjDto1:jcsjDtoList) {
										if (str[j+1].equals(jcsjDto1.getCsmc())){
											sjyzjgDto.setJl(jcsjDto1.getCsid());
										}
									}
							}else{
								sjyzjgDto.setJl(null);
							}
							sjyzjgDto.setXgry(sjyzDto.getXgry());
							boolean success = sjyzjgService.update(sjyzjgDto);
							if(!success){
								sjyzjgDto.setYzjgid(StringUtil.generateUUID());
								sjyzjgDto.setLrry(sjyzDto.getXgry());
								sjyzjgDto.setXgry(null);
								boolean right = sjyzjgService.insert(sjyzjgDto);
								sjyzjgDto.setXgry(null);
								sjyzjgDto.setLrry(null);
								sjyzjgDto.setScry(sjyzjgDto.getXgry());
								sjyzjgService.delete(sjyzjgDto);
								if(!right)
									return false;
							}
						}
					}
				}
			}

			JcsjDto dtoById = jcsjService.getDtoById(sjyzDto.getYzlb());
			sjyzjgDto.setCsdm(dtoById.getCsdm());
			sjyzjgDto.setYzlb(dtoById.getCsmc());
			if (sjyzDto.getFjids() != null && sjyzDto.getFjids().size() > 0){
				//区分代码
				//String csdm=dao.getCsdm(sjyzDto);
				//String mrcsdm=dao.getMrCsdm(BasicDataTypeEnum.DISTINGUISH.getCode());

				for (int i = 0; i < sjyzDto.getFjids().size(); i++){
					boolean saveFile = fjcfbService.save2RealFile(sjyzDto.getFjids().get(i), sjyzDto.getYzid());
					if (!saveFile)
						return false;
				}
				try {

					FjcfbDto fjcfbDto = new FjcfbDto();
					fjcfbDto.setIds(sjyzDto.getFjids());
					List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
					if (fjcfbDtos != null && fjcfbDtos.size() > 0) {
						for (FjcfbDto fjcfbDto_t :fjcfbDtos) {
							String wjm=fjcfbDto_t.getWjm(); //文件名
							String wjmhz=wjm.substring(wjm.lastIndexOf("."), wjm.length());
							String wjlj=fjcfbDto_t.getWjlj();//加密后的文件路径
							DBEncrypt encrypt=new DBEncrypt();
							String wjlj_d=encrypt.dCode(wjlj);//解密后的文件路径
							if(".xlsx".equals(wjmhz)) {
								creatXSSFWorkbook(wjlj_d,sjyzjgDto);
							}else if(".xls".equals(wjmhz)) {
								creatHSSFWorkbook(wjlj_d,sjyzjgDto);
							}
						}
					}
				} catch (Exception e) {
					logger.error("PCR文件上传错误：",e.toString());
					e.printStackTrace();
//					return false;

				}
			}
		}
		//同步至微信服务器
		if (StringUtil.isNotBlank(menuurl))
			amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.MOD_INSPECTION_VERIFICATION.getCode(), JSONObject.toJSONString(sjyzDto));

		return true;
	}
	
 	/**
	 * 送检验证列表（查询审核状态）
	 * @param sjyzDto
	 * @return
	 */
	@Override
	public List<SjyzDto> getPagedList(SjyzDto sjyzDto) {
		// TODO Auto-generated method stub
		List<SjyzDto> list=dao.getPagedDtoList(sjyzDto);
		try{
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_VERIFICATION.getCode(), "zt", "yzid", new String[]{
				StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_LAB_VERIFICATION.getCode(), "zt", "yzid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 审核列表
	 * @param sjyzDto
	 * @return
	 */
	@Override
	public List<SjyzDto> getPagedAuditSjyz(SjyzDto sjyzDto) {
		// TODO Auto-generated method stub
		if(sjyzDto.getSfnbbms()!=null){//根据前端传来的数据给是否内部编码赋值
			if(sjyzDto.getSfnbbms().length==1){
					if(("00").equals(sjyzDto.getSfnbbms()[0])){
						sjyzDto.setSfnbbm("0");
					}
					else if(("10").equals(sjyzDto.getSfnbbms()[0])){
						sjyzDto.setSfnbbm("1");
					}
			}
			else {
				sjyzDto.setSfnbbm(null);
			}
		}
		else {
			sjyzDto.setSfnbbm(null);
		}
		List<SjyzDto> t_sbyzList=dao.getPagedAuditSjyz(sjyzDto);
		if(t_sbyzList==null || t_sbyzList.size()==0) 
			return t_sbyzList;
		
		List<SjyzDto> sqList=dao.getAuditListSjyz(t_sbyzList);
		
		commonservice.setSqrxm(sqList);
		
		return sqList;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam)
			throws BusinessException {
		// TODO Auto-generated method stub
		SjyzDto sjyzDto=(SjyzDto) baseModel;
		sjyzDto.setXgry(operator.getYhid());
		List<ShgcDto> dataList = operator.getBycsList();
		if (AuditStateEnum.AUDITED.equals(dataList.get(0).getAuditState())){
			sjyzDto.setZt(StatusEnum.CHECK_PASS.getCode());
		}
		boolean isSuccess =updateSjyzxx(sjyzDto);
		GrszDto grszDto = new GrszDto();
		grszDto.setYhid(operator.getYhid());
		grszDto.setSzlbs(new String[]{PersonalSettingEnum.REAGENT_SETTINGS.getCode()+"_"+sjyzDto.getYzlb(),PersonalSettingEnum.PREIMER_NUMBER.getCode()});
		Map<String, GrszDto> grszDtoMap = grszService.selectGrszMapByYhidAndSzlb(grszDto);
		grszDto.setSzlb(PersonalSettingEnum.REAGENT_SETTINGS.getCode()+"_"+sjyzDto.getYzlb());
		GrszDto grszxx_s = grszDtoMap.get(PersonalSettingEnum.REAGENT_SETTINGS.getCode()+"_"+sjyzDto.getYzlb());
		if (grszxx_s == null) {
			grszDto.setSzz(sjyzDto.getSjph());
			grszDto.setLrry(operator.getYhid());
			grszService.insertDto(grszDto);
		} else {
			if (grszxx_s.getSzz()==null || !grszxx_s.getSzz().equals(sjyzDto.getSjph())) {
				grszDto.setSzz(sjyzDto.getSjph());
				grszService.updateByYhidAndSzlb(grszDto);
			}
		}
		GrszDto grszDto_yw = new GrszDto();
		grszDto_yw.setYhid(operator.getYhid());
		grszDto_yw.setSzlb(PersonalSettingEnum.PREIMER_NUMBER.getCode());
		GrszDto grszxx_ywbh = grszDtoMap.get(PersonalSettingEnum.PREIMER_NUMBER.getCode());
		if (grszxx_ywbh == null) {
			grszDto_yw.setSzz(sjyzDto.getYwbh());
			grszDto_yw.setLrry(operator.getYhid());
			grszService.insertDto(grszDto_yw);
		} else {
			if (grszxx_ywbh.getSzz()== null || !grszxx_ywbh.getSzz().equals(sjyzDto.getYwbh())) {
				grszDto_yw.setSzz(sjyzDto.getYwbh());
				grszService.updateByYhidAndSzlb(grszDto_yw);
			}
		}
		return isSuccess;
	}
	

	@SuppressWarnings("deprecation")
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}
		
//		String token = talkUtil.getToken();
		for (ShgcDto shgcDto :shgcList){
			SjyzDto sjyzDto=new SjyzDto();
			sjyzDto.setYzid(shgcDto.getYwid());
			sjyzDto.setXgry(operator.getYhid());
			SjyzDto sjyzDto_t=getDtoById(sjyzDto.getYzid());
			List<SpgwcyDto> spgwcyDtos = commonservice.siftJcdwList(shgcDto.getSpgwcyDtos(),sjyzDto_t.getJcdw());
			boolean hbMessageFlag = commonService.queryAuthMessage(sjyzDto_t.getHbid(),"INFORM_HB00003");
			shgcDto.setSpgwcyDtos(spgwcyDtos);
			if(AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {//审核退回
				sjyzDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				if(shgcDto.getSpgwcyDtos()!=null && shgcDto.getSpgwcyDtos().size()>0) {
					for (int i = 0; i < shgcDto.getSpgwcyDtos().size(); i++){
						if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
							talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
									shgcDto.getSpgwcyDtos().get(i).getYhid(),
									xxglService.getMsg("ICOMM_SH00026"),xxglService.getMsg("ICOMM_SH00034",
											operator.getZsxm(),shgcDto.getShlbmc() ,
											sjyzDto_t.getYzlbmc(),sjyzDto_t.getHzxm(),
											sjyzDto_t.getYblxmc(),shgcDto.getShxx_shyj(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
				//审核退回通知(钉钉消息设置通知人员)
				JcsjDto jcsjDto = new JcsjDto();
				jcsjDto.setJclb("DINGMESSAGETYPE");
				jcsjDto.setCsdm("VERIFICATION_AUDIT_FAIL");
				List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
				if(ddxxglDtos!=null && ddxxglDtos.size()>0) {
					for(int i=0;i<ddxxglDtos.size();i++) {
						if(StringUtil.isNotBlank(ddxxglDtos.get(i).getDdid())) {
							talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),ddxxglDtos.get(i).getYhid(),ddxxglDtos.get(i).getYhm(), ddxxglDtos.get(i).getDdid(),
									xxglService.getMsg("ICOMM_SH00026"),xxglService.getMsg("ICOMM_SH00034",
											operator.getZsxm(),shgcDto.getShlbmc() ,
											sjyzDto_t.getYzlbmc(),sjyzDto_t.getHzxm(),
											sjyzDto_t.getYblxmc(),shgcDto.getShxx_shyj(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
				//通知客户
				if("1".equals(sjyzDto_t.getSftz()) && hbMessageFlag) {
					SjxxDto sjxxDto =new SjxxDto();
					sjxxDto.setSjid(sjyzDto_t.getSjid());
					sendVerificationMessage(sjyzDto_t,"UNPASS");
				}
			}else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {//审核通过
				//检验检测项目扩展参数3是否为IMP_REPORT_PCR
//				if(!"IMP_REPORT_PCR".equals(sjyzDto_t.getJcxmcskz3())) {	2023/3/17 姜勇坤注释(废弃)
//					// 校验报告是否上传
//					if(!"1".equals(sjyzDto_t.getSfsc()))
//						throw new BusinessException("msg", "报告还未上传，验证无法通过，请再次确认报告发送情况！");
//				}
				sjyzDto.setZt(StatusEnum.CHECK_PASS.getCode());
				SjyzjgDto sjyzjgDto = new SjyzjgDto();
				sjyzjgDto.setYzid(sjyzDto_t.getYzid());
				List<SjyzjgDto> dtoList = sjyzjgService.getInfoByYbbh(sjyzjgDto);
				if (!CollectionUtils.isEmpty(dtoList)){
					for (SjyzjgDto dto : dtoList) {
						if (StringUtil.isNotBlank(dto.getCskz2()) && StringUtil.isNotBlank(dto.getJlmc())){
							String[] strings = dto.getCskz2().split(",");
							if ("阴性".equals(dto.getJlmc())){
								if (StringUtil.isNotBlank(sjyzDto_t.getJcjg())){
									for (String string : strings) {
										if (sjyzDto_t.getJcjg().contains(string)){
											sjyzDto.setSffh("0");
											break;
										}
									}
									if (StringUtil.isNotBlank(sjyzDto.getSffh()))
										break;
								}
								if (StringUtil.isNotBlank(sjyzDto_t.getYsjg())){
									for (String string : strings) {
										if (sjyzDto_t.getYsjg().contains(string)){
											sjyzDto.setSffh("0");
											break;
										}
									}
									if (StringUtil.isNotBlank(sjyzDto.getSffh()))
										break;
								}
							}else if ("阳性".equals(dto.getJlmc())){
								if (StringUtil.isNotBlank(sjyzDto_t.getJcjg())){
									for (String string : strings) {
										if (sjyzDto_t.getJcjg().contains(string)){
											sjyzDto.setSffh("1");
											break;
										}
									}

								}
							}else{
								if (StringUtil.isNotBlank(sjyzDto_t.getYsjg())){
									for (String string : strings) {
										if ( sjyzDto_t.getYsjg().contains(string)){
											sjyzDto.setSffh("1");
											break;
										}
									}
								}
							}
						}
					}
					if (StringUtil.isBlank(sjyzDto.getSffh()))
						sjyzDto.setSffh("1");
				}
				if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0){
					int size = shgcDto.getSpgwcyDtos().size();
					for(int i=0;i<size;i++){
						if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
							talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(),
									shgcDto.getSpgwcyDtos().get(i).getYhid(),
									xxglService.getMsg("ICOMM_SH00006"),xxglService.getMsg("ICOMM_SH00034",
											operator.getZsxm(),shgcDto.getShlbmc() ,
											sjyzDto_t.getYzlbmc(),sjyzDto_t.getHzxm(),
											sjyzDto_t.getYblxmc(),shgcDto.getShxx_shyj(),
											DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
				JcsjDto jcsjDto = new JcsjDto();
				jcsjDto.setJclb("DINGMESSAGETYPE");
				jcsjDto.setCsdm("VERIFICATION_RESULT_TYPE");
				List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
				//内网访问
				String internalbtn = applicationurl+"/common/view/displayView?view_url=/ws/verification/viewVerifi?yzid="+sjyzDto.getYzid();
				//外网访问
				String external=externalurl+"/common/view/displayView?view_url=/ws/verification/viewVerifi?yzid="+sjyzDto.getYzid();
				List<BtnJsonList> btnJsonLists = new ArrayList<>();
				BtnJsonList btnJsonList = new BtnJsonList();
				btnJsonList.setTitle("内网访问");
				btnJsonList.setActionUrl(internalbtn);
				btnJsonLists.add(btnJsonList);
				btnJsonList = new BtnJsonList();
				btnJsonList.setTitle("外网访问");
				btnJsonList.setActionUrl(external);
				btnJsonLists.add(btnJsonList);
				String yzjgmc=sjyzDto_t.getYzjgmc();
				String qfmc=sjyzDto_t.getQfmc();
				String khtzmc=sjyzDto_t.getKhtzmc();
				String yzlbmc=sjyzDto_t.getYzlbmc();
				if(StringUtil.isBlank(yzjgmc)) {
					yzjgmc="";
				}else{
					String[] strs = yzjgmc.split(",");
					yzjgmc="";
					if (null != strs && strs.length>0){
						for (String str: strs) {
							if (StringUtil.isNotBlank(yzjgmc)){
								yzjgmc += "  \r\n";
							}
							String[] temps = str.split(":");
							if (null != temps && temps.length>0){
								for (String temp: temps) {
									if ("阳性".equals(temp)){
										temp = "<font color=\"#DC143C\">"+temp+"</font>";
									} else if ("阴性".equals(temp)){
										temp = "<font color=\"#00dddd\">"+temp+"</font>";
									}else{
										temp = "<font color=\"#FFA500\">"+temp+":</font>";
									}
									yzjgmc += temp;
								}
							}
						}
					}
				}
				if(StringUtil.isBlank(qfmc)) {
					qfmc="";
				}
				if(StringUtil.isBlank(khtzmc)) {
					khtzmc="";
				}
				if(StringUtil.isBlank(yzlbmc)) {
					yzlbmc="";
				}
				if(ddxxglDtos!=null && ddxxglDtos.size()>0) {
					for(int i=0;i<ddxxglDtos.size();i++) {
						talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),ddxxglDtos.get(i).getYhid(),ddxxglDtos.get(i).getYhm(), ddxxglDtos.get(i).getDdid(),xxglService.getMsg("ICOMM_SJ00038"),StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SJ00037"),sjyzDto_t.getYbbh(),sjyzDto_t.getYblxmc(),sjyzDto_t.getHzxm() ,yzlbmc,yzjgmc,qfmc,khtzmc,DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
					}
				}
				if("1".equals(sjyzDto_t.getSftz()) && hbMessageFlag) {
					SjxxDto sjxxDto=new SjxxDto();
					sjxxDto.setSjid(sjyzDto_t.getSjid());
					SjxxDto sjxxDto_t=sjxxDao.getDto(sjxxDto);
					//发送送检报告
					boolean result = false;
					try{
						result = sjxxWsService.sendVerificationReport(dtoList,sjxxDto_t,sjyzDto_t,operator.getYhm());
					}catch (Exception e){
						logger.error("发送报告失败!"+e.getMessage());
					}
					if(!result){
						Map<String, Object> msgMap = new HashMap<>();
						msgMap.put("sjxxDto",sjxxDto_t);
						msgMap.put("sjyzDto",sjyzDto_t);
						String ICOMM_SJ00045=xxglService.getMsg("ICOMM_SJ00045");//标题
//					String ICOMM_SJ00046=xxglService.getMsg("ICOMM_SJ00046",sjyzDto_t.getHzxm());//内容
						String ICOMM_SJ00046=xxglService.getReplaceMsg("ICOMM_SJ00046",msgMap);//内容
//					String ICOMM_SJ00049=xxglService.getMsg("ICOMM_SJ00049", sjyzDto_t.getHzxm(),sjyzDto_t.getYbbh(),sjyzDto_t.getHzxm(),sjyzDto_t.getNl(),sjxxDto_t.getXbmc(),sjxxDto_t.getSjdwmc(),sjxxDto_t.getKsmc(),sjxxDto_t.getDb(),sjxxDto_t.getJcxmmc(),sjxxDto_t.getYblxmc(),sjyzDto_t.getYzlbmc(),sjyzDto_t.getYzjgmc());
						String ICOMM_SJ00049=xxglService.getReplaceMsg("ICOMM_SJ00049",msgMap);
						String keyword1 = DateUtils.getCustomFomratCurrentDate("HH:mm:ss");
						String keyword2 = sjyzDto_t.getYbbh();
						Map<String,Object> map= new HashMap<>();
						map.put("yxbt", ICOMM_SJ00045);//邮箱 标题
						map.put("yxnr", ICOMM_SJ00049);//邮箱 内容
						map.put("ddbt", ICOMM_SJ00045);//钉钉 标题
						map.put("ddnr", ICOMM_SJ00046);//钉钉 内容
						map.put("wxbt", ICOMM_SJ00045);//微信 标题
						map.put("remark", ICOMM_SJ00046);//微信 内容
						map.put("keyword2", keyword1);
						map.put("keyword1", keyword2);
						map.put("keyword3", ICOMM_SJ00046);
						map.put("templateid", ybzt_templateid);
						map.put("xxmb","TEMPLATE_EXCEPTION");
						// 组装微信查看路径
						String reporturl = menuurl +"/wechat/view/displayView?view_url=/common/view/verificationView?flag=1%26yzid="+sjyzDto.getYzid();
						map.put("reporturl", reporturl);
						sjxxCommonService.sendMessage(sjxxDto, map);
					}
				}
			}else {//新增审核
				sjyzDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				//审核被退回
				if(!auditParam.isCommitOpe()&&!auditParam.isPassOpe()) {
					//通知提交人员
					if(StringUtil.isNotBlank(shgcDto.getSqrddid())) {
						String spgwmc="";
						if(shgcDto.getSpgwcyDtos()!=null && shgcDto.getSpgwcyDtos().size()>0) {
							spgwmc=shgcDto.getSpgwcyDtos().get(0).getGwmc();
						}
						talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getYhid(),shgcDto.getYhm(),shgcDto.getSqrddid(),xxglService.getMsg("ICOMM_SJ00052"),StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SJ00053"),operator.getZsxm(),sjyzDto_t.getYbbh(),sjyzDto_t.getHzxm(),sjyzDto_t.getYblxmc(),spgwmc,shgcDto.getShxx_shyj()!=null?shgcDto.getShxx_shyj():"",DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
					}
					//通知客户
					if("1".equals(sjyzDto_t.getSftz()) && hbMessageFlag) {
						SjxxDto sjxxDto =new SjxxDto();
						sjxxDto.setSjid(sjyzDto_t.getSjid());
						sendVerificationMessage(sjyzDto_t,"BACK");
					}
				}else if(auditParam.isCommitOpe()){
					//通知客户
					if("1".equals(sjyzDto_t.getSftz()) && hbMessageFlag) {
						SjxxDto sjxxDto =new SjxxDto();
						sjxxDto.setSjid(sjyzDto_t.getSjid());
						sendVerificationMessage(sjyzDto_t,"SUBMIT");
					}
				}
				//				//查询有检测单位权限用户
//				List<String> yhids = fjsqService.getXtyhByJcdw(sjyzDto_t.getJcdw());
				if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0){
					try{
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								String sign = URLEncoder.encode(commonservice.getSign(sjyzDto_t.getYzid()),"UTF-8");
								//内网访问
								String internalbtn = applicationurl+"/ws/auditProcess/auditPhone?shxx_shry="+shgcDto.getSpgwcyDtos().get(i).getYhid()+"&shxx_shjs="+shgcDto.getSpgwcyDtos().get(i).getJsid()+"&ywid="+sjyzDto_t.getYzid()+"&sign="+sign+"&xlcxh="+shgcDto.getXlcxh()+"&business_url=/verification/audit_Verifi&ywzd=yzid&shlbmc="+shgcDto.getShlbmc();
								String dingtalkbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/verification/addverification/addverification&auditflag=1&yzid="+sjyzDto_t.getYzid()+"&sjid="+sjyzDto_t.getSjid(),StandardCharsets.UTF_8);
								List<BtnJsonList> btnJsonLists = new ArrayList<>();
								BtnJsonList btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("详细情况");
								btnJsonList.setActionUrl(internalbtn);
								btnJsonLists.add(btnJsonList);
								btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("小程序");
								btnJsonList.setActionUrl(dingtalkbtn);
								btnJsonLists.add(btnJsonList);
								String shyj=shgcDto.getShxx_shyj();
								String yzlbmc=sjyzDto_t.getYzlbmc();
								if(StringUtil.isBlank(shyj)) {
									shyj="";
								}
								if(StringUtil.isBlank(yzlbmc)) {
									yzlbmc="";
								}
								talkUtil.sendCardDyxxMessageThread(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(),xxglService.getMsg("ICOMM_SH00032"),
										StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SH00033"),
												shgcDto.getSqrxm(),shgcDto.getShlbmc(),yzlbmc,
												sjyzDto_t.getHzxm(),sjyzDto_t.getYblxmc(),shyj ,
												DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
							}
						}
					}catch(Exception e){
						logger.error(e.getMessage());
					}
					//同步至微信服务器
					if (StringUtil.isNotBlank(menuurl))
						amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.MOD_INSPECTION_VERIFICATION.getCode(), JSONObject.toJSONString(sjyzDto_t));
				}
				//只有在提交审核时发送验证通知
				if("1".equals(shgcDto.getXlcxh()) ) {
					//内网访问
					String internalbtn = applicationurl+"/common/view/displayView?view_url=/ws/verification/viewVerifi?yzid="+sjyzDto.getYzid();
					//外网访问
					//String external=externalurl+"/common/view/displayView?view_url=/ws/verification/viewVerifi?yzid="+sjyzDto.getYzid();
					List<BtnJsonList> btnJsonLists = new ArrayList<>();
					BtnJsonList btnJsonList = new BtnJsonList();
					btnJsonList.setTitle("详细情况");
					btnJsonList.setActionUrl(internalbtn);
					btnJsonLists.add(btnJsonList);
//					btnJsonList = new BtnJsonList();
//					btnJsonList.setTitle("外网访问");
//					btnJsonList.setActionUrl(external);
//					btnJsonLists.add(btnJsonList);
					JcsjDto jcsjDto = new JcsjDto();
					jcsjDto.setJclb("DINGMESSAGETYPE");
					jcsjDto.setCsdm("VERIFICATION_TYPE");
					List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
					String yzlbmc=sjyzDto_t.getYzlbmc();
					String qfmc=sjyzDto_t.getQfmc();
					if(StringUtil.isBlank(yzlbmc)) {
						yzlbmc="";
					}
					if(StringUtil.isBlank(qfmc)) {
						qfmc="";
					}
					if(ddxxglDtos!=null && ddxxglDtos.size()>0) {
						for(int i=0;i<ddxxglDtos.size();i++) {
							talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),ddxxglDtos.get(i).getYhid(),ddxxglDtos.get(i).getYhm(), ddxxglDtos.get(i).getDdid(),xxglService.getMsg("ICOMM_SJ00033"),StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SJ00034"),sjyzDto_t.getYbbh(),sjyzDto_t.getYblxmc(),sjyzDto_t.getHzxm() ,yzlbmc,qfmc,DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
						}
					}
					SjxxDto sjxxDto=sjxxDao.getDtoById(sjyzDto_t.getSjid());
					JcsjDto jcsjDto_t = new JcsjDto();
					jcsjDto_t.setJclb("DINGMESSAGETYPE");
					jcsjDto_t.setCsdm("VERIFICATION_OPERATE_TYPE");
					List<DdxxglDto> ddxxyzyyDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto_t).getCsdm());
					if(sjxxDto != null && StringUtil.isBlank(sjxxDto.getBgrq())) {
						for(int i=0;i<ddxxyzyyDtos.size();i++) {
							talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),ddxxyzyyDtos.get(i).getYhid(),ddxxyzyyDtos.get(i).getYhm(), ddxxyzyyDtos.get(i).getDdid(),xxglService.getMsg("ICOMM_SJ00035"),StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SJ00036"),sjyzDto_t.getHzxm(),sjyzDto_t.getYbbh(),sjyzDto_t.getYblxmc(),qfmc),btnJsonLists,"1");
						}
					}
				}
				//发送钉钉消息--取消审核人员
				if(shgcDto.getNo_spgwcyDtos() != null && shgcDto.getNo_spgwcyDtos().size() > 0){
					try{
						int size = shgcDto.getNo_spgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,sjyzDto_t.getYzlbmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						logger.error(e.getMessage());
					}
				}
			}
			updateSjyzxx(sjyzDto);
		}
		return true;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}
		if(auditParam.isCancelOpe()) {
			//审核回调方法
			for(ShgcDto shgcDto : shgcList){
				String yzid = shgcDto.getYwid();
				SjyzDto sjyzDto= new SjyzDto();
				sjyzDto.setXgry(operator.getYhid());
				sjyzDto.setYzid(yzid);
				sjyzDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				updateSjyzxx(sjyzDto);
			}
		}else {
			//审核回调方法
			for(ShgcDto shgcDto : shgcList){
				String yzid = shgcDto.getYwid();
				SjyzDto sjyzDto =new SjyzDto();
				sjyzDto.setXgry(operator.getYhid());
				sjyzDto.setYzid(yzid);
				sjyzDto.setZt(StatusEnum.CHECK_NO.getCode());
				updateSjyzxx(sjyzDto);
			}
		}
		return true;
	}
	/**
	 * 根据送检验证id查询送检验证结果
	 *
	 * @param
	 * @return
	 */
	@Override
	public SjyzDto getDto(SjyzDto sjyzDto)
	{
		if (StringUtil.isBlank(sjyzDto.getYzid()))
			return null;
		// 获取送检信息
		SjyzDto resSjxxDto = dao.getDto(sjyzDto);
		if (resSjxxDto != null)
		{
			if (StringUtil.isNotBlank(resSjxxDto.getYzjg()))
			{
				String[] temps = resSjxxDto.getYzjg().split(",");
				List<List<String>> stringList = new ArrayList<>();
				for (String str:temps) {
					List<String> strings = new ArrayList<>();
					String[] arrs = str.split(":");
					for (int i = 0; i < arrs.length; i++) {
						if ((i&1) ==0 ){
							arrs[i] +=":";
						}
						strings.add(arrs[i]);
					}
//					for (String arr:arrs) {
//						strings.add(arr);
//					}
					stringList.add(strings);
				}
				resSjxxDto.setYzjgnr(stringList);
			}

		}
		return resSjxxDto;
	}
	@Override
	public boolean existCheck(String fieldName, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * .xlsx文件解析
	 * @param wjlj
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void creatXSSFWorkbook(String wjlj,SjyzjgDto sjyzjgDto) throws FileNotFoundException, IOException {
		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(wjlj));
 		XSSFSheet sheet =workbook.getSheetAt(0);
		int rows=sheet.getPhysicalNumberOfRows();
	 	if("孔位".equals(sheet.getRow(0).getCell(0).toString())) {
			XSSFRow headrow=sheet.getRow(0);
			Map<String,Integer> headInfo = getKzHeadInfo(headrow);
			List<WkmxPcrResultModel> resultModels = new ArrayList<>();
			for (int i = 1; i < rows; i++) {
				XSSFRow row=sheet.getRow(i);
				if(row.getCell(headInfo.get("ybbh"))==null && row.getCell(headInfo.get("nbbm"))==null)
					continue;
				WkmxPcrResultModel wkmxPcrResultModel = new WkmxPcrResultModel();
				wkmxPcrResultModel.setSampleName(row.getCell(headInfo.get("nbbm"))!=null?row.getCell(headInfo.get("nbbm")).toString():"");//样本名称
				wkmxPcrResultModel.setWell(row.getCell(headInfo.get("kw"))!=null?row.getCell(headInfo.get("kw")).toString():"");//孔位
				wkmxPcrResultModel.setGeneName(row.getCell(headInfo.get("mbjy"))!=null?row.getCell(headInfo.get("mbjy")).toString():"");//目标基因
				wkmxPcrResultModel.setDyeName(row.getCell(headInfo.get("yg"))!=null?row.getCell(headInfo.get("yg")).toString():"");//荧光
				wkmxPcrResultModel.setCtVaule(row.getCell(headInfo.get("ct"))!=null?row.getCell(headInfo.get("ct")).toString():"");//ct
				wkmxPcrResultModel.setSampleNumber(row.getCell(headInfo.get("ybbh"))!=null?row.getCell(headInfo.get("ybbh")).toString():"");//ybbh
				resultModels.add(wkmxPcrResultModel);
			}
			pcrInfoMod(resultModels,sjyzjgDto);
		}
	}

	/**
	 * Pcr数据修改
	 *
	 */
	public void pcrInfoMod(List<WkmxPcrResultModel> resultModels,SjyzjgDto sjyzjgDto){
		List<WkmxPcrResultModel> wkmxPcrResultModelList = new ArrayList<>();
		List<WkmxPcrResultModel> wkmxPcrResultModels = new ArrayList<>();
		List<SjyzjgDto> sjyzjgDtoList = sjyzjgService.getInfoByYbbh(sjyzjgDto);
		if (null != sjyzjgDtoList && sjyzjgDtoList.size()>0) {
			if (resultModels != null && resultModels.size() > 0) {
				for (WkmxPcrResultModel pr : resultModels) {
					if (StringUtil.isNotBlank(pr.getSampleName()) && (pr.getSampleName().contains("PC") || pr.getSampleName().contains("NC"))){
						if(StringUtil.isNotBlank(pr.getSampleName())){
							pr.setSampleNumber(pr.getSampleName());
						}
						wkmxPcrResultModelList.add(pr);
					}else{
						if (StringUtil.isNotBlank(sjyzjgDtoList.get(0).getQfcskz()) && sjyzjgDtoList.get(0).getQfcskz().equals("REM")){
							if (StringUtil.isNotBlank(pr.getSampleNumber()) && pr.getSampleNumber().split("-").length>1 && pr.getSampleNumber().startsWith(sjyzjgDtoList.get(0).getNbbm())){
								wkmxPcrResultModels.add(pr);
							}
						}else if(StringUtil.isNotBlank(pr.getSampleNumber()) && (pr.getSampleNumber().equals(sjyzjgDtoList.get(0).getNbbm())||pr.getSampleNumber().split(" ")[0].equals(sjyzjgDtoList.get(0).getNbbm()))){
							wkmxPcrResultModels.add(pr);
						}
					}
				}
			}
			Map<String, List<WkmxPcrResultModel>> map = wkmxPcrResultModels.stream().collect(Collectors.groupingBy(WkmxPcrResultModel::getWell));
			if (null != map && map.size()>0){
				for (int i = 0; i < sjyzjgDtoList.size(); i++) {
					int ctbs = 1;
					int nctbs = 1;
					int pctbs = 1;
					SjyzjgDto dto = new SjyzjgDto();
					dto.setYzjgid(sjyzjgDtoList.get(i).getYzjgid());
					dto.setJcqf(sjyzjgDtoList.get(i).getCskz1());
					dto.setMbjy(sjyzjgDto.getCsdm());
					Iterator<Map.Entry<String, List<WkmxPcrResultModel>>> entries = map.entrySet().iterator();
					while (entries.hasNext()) {
						Map.Entry<String,  List<WkmxPcrResultModel>> entry = entries.next();
						List<WkmxPcrResultModel> resultModelList = entry.getValue();
						if (null != resultModelList && resultModelList.size() > 0){
							int pos = 0;
							for (WkmxPcrResultModel pr:resultModelList) {
								if (StringUtil.isNotBlank(pr.getGeneName())){
									if (("MTM".equals(sjyzjgDto.getCsdm()) && (pr.getGeneName().contains(sjyzjgDto.getCsdm()) || pr.getGeneName().contains("结核"))) ||
											("ACP".equals(sjyzjgDto.getCsdm()) && (pr.getGeneName().contains(sjyzjgDto.getCsdm()) || pr.getGeneName().contains("耶氏")))
									){
										pos = 1;
										break;
									}
								}
							}
							if ( pos == 1){
								for (WkmxPcrResultModel pr:resultModelList) {
									if (StringUtil.isNotBlank(sjyzjgDtoList.get(i).getCskz1()) && StringUtil.isNotBlank(pr.getDyeName())){
										if (sjyzjgDtoList.get(i).getCskz1().equals(pr.getDyeName()) && (StringUtil.isNotBlank(pr.getSampleName()) || StringUtil.isNotBlank(pr.getSampleNumber()))){
											switch (ctbs){
												case 1:
													dto.setKw1(pr.getWell());
													dto.setBz1(pr.getSampleName());
													if (StringUtil.isNotBlank(pr.getCtVaule())) {
														if (!NumberUtils.isCreatable(pr.getCtVaule())) {
															dto.setCt1("-");
														} else {
															dto.setCt1(pr.getCtVaule());
															dto.setJl(sjyzjgService.judgmentConclusion(pr.getCtVaule()));
														}
													}
													break;
												case 2:
													dto.setKw2(pr.getWell());
													dto.setBz2(pr.getSampleName());
													if (StringUtil.isNotBlank(pr.getCtVaule())) {
														if (!NumberUtils.isCreatable(pr.getCtVaule())) {
															dto.setCt2("-");
														} else {
															dto.setCt2(pr.getCtVaule());
														}
													}
													break;
												case 3:
													dto.setKw3(pr.getWell());
													if (StringUtil.isNotBlank(pr.getCtVaule())) {
														if (!NumberUtils.isCreatable(pr.getCtVaule())) {
															dto.setCt3("-");
														} else {
															dto.setCt3(pr.getCtVaule());
														}
													}
													break;
											}
											ctbs++;
										}
									}
								}
							}
						}
					}
					if (wkmxPcrResultModelList != null && wkmxPcrResultModelList.size() > 0) {
						for (WkmxPcrResultModel wkmxPcrResultModel : wkmxPcrResultModelList) {
							if (StringUtil.isNotBlank(sjyzjgDtoList.get(i).getYzlbcs()) && StringUtil.isNotBlank(wkmxPcrResultModel.getDyeName())) {
								if (wkmxPcrResultModel.getSampleName().contains(sjyzjgDtoList.get(i).getYzlbcs())  && (wkmxPcrResultModel.getSampleName().contains("PC") || wkmxPcrResultModel.getSampleName().contains("NC"))) {
									if (sjyzjgDtoList.get(i).getCskz1().equals(wkmxPcrResultModel.getDyeName()) && wkmxPcrResultModel.getSampleName().contains("NC")){
										switch (nctbs){
											case 1:
												dto.setNkw1(wkmxPcrResultModel.getWell());
												if (StringUtil.isNotBlank(wkmxPcrResultModel.getCtVaule())) {
													if (!NumberUtils.isCreatable(wkmxPcrResultModel.getCtVaule())) {
														dto.setNct1("-");
													} else {
														dto.setNct1(wkmxPcrResultModel.getCtVaule());
													}
												}
												break;
											case 2:
												dto.setNkw2(wkmxPcrResultModel.getWell());
												if (StringUtil.isNotBlank(wkmxPcrResultModel.getCtVaule())) {
													if (!NumberUtils.isCreatable(wkmxPcrResultModel.getCtVaule())) {
														dto.setNct2("-");
													} else {
														dto.setNct2(wkmxPcrResultModel.getCtVaule());
													}
												}
												break;
											case 3:
												dto.setNkw3(wkmxPcrResultModel.getWell());
												if (StringUtil.isNotBlank(wkmxPcrResultModel.getCtVaule())) {
													if (!NumberUtils.isCreatable(wkmxPcrResultModel.getCtVaule())) {
														dto.setNct3("-");
													} else {
														dto.setNct3(wkmxPcrResultModel.getCtVaule());
													}
												}
												break;
										}
										nctbs++;
									}else if (sjyzjgDtoList.get(i).getCskz1().equals(wkmxPcrResultModel.getDyeName()) && wkmxPcrResultModel.getSampleName().contains("PC")){
										switch (pctbs){
											case 1:
												dto.setPkw1(wkmxPcrResultModel.getWell());
												if (StringUtil.isNotBlank(wkmxPcrResultModel.getCtVaule())) {
													if (!NumberUtils.isCreatable(wkmxPcrResultModel.getCtVaule())) {
														dto.setPct1("-");
													} else {
														dto.setPct1(wkmxPcrResultModel.getCtVaule());
													}
												}
												break;
											case 2:
												dto.setPkw2(wkmxPcrResultModel.getWell());
												if (StringUtil.isNotBlank(wkmxPcrResultModel.getCtVaule())) {
													if (!NumberUtils.isCreatable(wkmxPcrResultModel.getCtVaule())) {
														dto.setPct2("-");
													} else {
														dto.setPct2(wkmxPcrResultModel.getCtVaule());
													}
												}
												break;
											case 3:
												dto.setPkw3(wkmxPcrResultModel.getWell());
												if (StringUtil.isNotBlank(wkmxPcrResultModel.getCtVaule())) {
													if (!NumberUtils.isCreatable(wkmxPcrResultModel.getCtVaule())) {
														dto.setPct3("-");
													} else {
														dto.setPct3(wkmxPcrResultModel.getCtVaule());
													}
												}
												break;
										}
										pctbs++;
									}
								}
							}
						}
					}
//					dto.setScbj("1");
	//					dto.setCtbs(String.valueOf(ctbs));
	//					dto.setNctbs(String.valueOf(nctbs));
	//					dto.setPctbs(String.valueOf(pctbs));
					sjyzjgService.updateDto(dto);
				}

			}
		}
	}
	/**
	 * .xls文件解析
	 * @param wjlj
	 * @return
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public void creatHSSFWorkbook(String wjlj,SjyzjgDto sjyzjgDto) throws FileNotFoundException, IOException {
		@SuppressWarnings("resource")
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(wjlj));
		HSSFSheet sheet =workbook.getSheetAt(0);
		int rows=sheet.getPhysicalNumberOfRows();
//		if("Block Type".equals(sheet.getRow(0).getCell(0).toString())) {
//			String mbjy=null;
//
//			HSSFRow headrow=sheet.getRow(7);
//			Map<String,Integer> headInfo = getStepHeadInfo(headrow);
//			for (int i = 8; i < rows; i++) {
//				HSSFRow row=sheet.getRow(i);
//				String xls_nbbm = row.getCell(headInfo.get("nbbm")).toString();
//				if(StringUtil.isNotBlank(xls_nbbm)) {
//					if(sjyzDto_t.getYznbbm().endsWith(xls_nbbm) || sjyzDto_t.getYzmrnbbm().endsWith(xls_nbbm)) {
//						isFind = true;
//						sjyzjgDtos.add(saveSjyzmx_Step_xls(headInfo,row,sjyzDto_t));
//						if(row.getCell(headInfo.get("mbjy"))!=null)
//		 				mbjy=row.getCell(headInfo.get("mbjy")).toString();
//		 			}
//				}
//			}
//
//			if(isFind) {
//	 			for (int j = 8; j < rows; j++) {
//	 				HSSFRow row=sheet.getRow(j);
//		 			if(("NC".equals(row.getCell(headInfo.get("nbbm")).toString())||"PC".equals(row.getCell(headInfo.get("nbbm")).toString()))
//		 					&&row.getCell(headInfo.get("mbjy")).toString().equals(mbjy)) {
//						sjyzjgDtos.add(saveSjyzmx_Step_xls(headInfo,row,sjyzDto_t));
//		 			}
//				}
//			}
//		}else
		if("孔位".equals(sheet.getRow(0).getCell(0).toString())) {
			HSSFRow headrow=sheet.getRow(0);
			//得到具体属性位于文档的第几列
			Map<String,Integer> headInfo = getKzHeadInfo(headrow);
			List<WkmxPcrResultModel> resultModels = new ArrayList<>();
			for (int i = 1; i < rows; i++) {
				HSSFRow row=sheet.getRow(i);
				if(row.getCell(headInfo.get("ybbh"))==null && row.getCell(headInfo.get("nbbm"))==null)
					continue;
				WkmxPcrResultModel wkmxPcrResultModel = new WkmxPcrResultModel();
				wkmxPcrResultModel.setSampleName(row.getCell(headInfo.get("nbbm"))!=null?row.getCell(headInfo.get("nbbm")).toString():"");//样本名称
				wkmxPcrResultModel.setWell(row.getCell(headInfo.get("kw"))!=null?row.getCell(headInfo.get("kw")).toString():"");//孔位
				wkmxPcrResultModel.setGeneName(row.getCell(headInfo.get("mbjy"))!=null?row.getCell(headInfo.get("mbjy")).toString():"");//目标基因
				wkmxPcrResultModel.setDyeName(row.getCell(headInfo.get("yg"))!=null?row.getCell(headInfo.get("yg")).toString():"");//荧光
				wkmxPcrResultModel.setCtVaule(row.getCell(headInfo.get("ct"))!=null?row.getCell(headInfo.get("ct")).toString():"");//ct
				wkmxPcrResultModel.setSampleNumber(row.getCell(headInfo.get("ybbh"))!=null?row.getCell(headInfo.get("ybbh")).toString():"");//ybbh
				resultModels.add(wkmxPcrResultModel);
			}
			pcrInfoMod(resultModels,sjyzjgDto);
		}
	}

//	/**
//	 * Step表添加送检验证明细
//	 * @param row
//	 * @param sjyzDto
//	 * @return
//	 */
//	public SjyzjgDto saveSjyzmx_Step_xlsx(Map<String,Integer> headInfo,XSSFRow row,SjyzDto sjyzDto) {
//		SjyzjgDto sjyzjgDto=new SjyzjgDto();
//		sjyzjgDto.setYzid(sjyzDto.getYzid());
//		sjyzjgDto.setKw(row.getCell(headInfo.get("kw")).toString());
//		sjyzjgDto.setBh(row.getCell(headInfo.get("nbbm")).toString());
//		sjyzjgDto.setMbjy(row.getCell(headInfo.get("mbjy")).toString());
//		if(headInfo.get("ct")!=null) {
//			String cellValue = row.getCell(headInfo.get("ct")).toString();
//			if(!isNumber(cellValue))
//				sjyzjgDto.setCt("0");
//			else
//				sjyzjgDto.setCt(cellValue);
//		}
//		if(headInfo.get("tm1")!=null) {
//			String cellValue = row.getCell(headInfo.get("tm1")).toString();
//			if(!isNumber(cellValue))
//				sjyzjgDto.setTm1("0");
//			else
//				sjyzjgDto.setTm1(cellValue);
//		}
//		if(headInfo.get("tm2")!=null) {
//			String cellValue = row.getCell(headInfo.get("tm2")).toString();
//			if(!isNumber(cellValue))
//				sjyzjgDto.setTm2("0");
//			else
//				sjyzjgDto.setTm2(cellValue);
//		}
//		if(headInfo.get("tm3")!=null) {
//			String cellValue = row.getCell(headInfo.get("tm3")).toString();
//			if(!isNumber(cellValue))
//				sjyzjgDto.setTm3("0");
//			else
//				sjyzjgDto.setTm3(cellValue);
//		}
//
//		return sjyzjgDto;
//	}
//
//	/**
//	 * 扩增表添加送检验证明细
//	 * @param row
//	 * @param sjyzDto
//	 * @param
//	 * @return
//	 */
//	public SjyzjgDto saveSjyzmx_KZ_xls(Map<String,Integer> headInfo,HSSFRow row,SjyzDto sjyzDto) {
//		SjyzjgDto sjyzjgDto=new SjyzjgDto();
//		sjyzjgDto.setYzid(sjyzDto.getYzid());
//		sjyzjgDto.setKw(row.getCell(0).toString());
//		sjyzjgDto.setBh(row.getCell(headInfo.get("nbbm")).toString());
//		sjyzjgDto.setJcqf(row.getCell(headInfo.get("yg")).toString());
//		sjyzjgDto.setMbjy(row.getCell(headInfo.get("mbjy")).toString());
//		if(headInfo.get("ct")!=null) {
//			String cellValue = row.getCell(headInfo.get("ct")).toString();
//			if(!isNumber(cellValue))
//				sjyzjgDto.setCt("0");
//			else
//				sjyzjgDto.setCt(cellValue);
//		}
//		if(headInfo.get("tm1")!=null) {
//			String cellValue = row.getCell(headInfo.get("tm1")).toString();
//			if(!isNumber(cellValue))
//				sjyzjgDto.setTm1("0");
//			else
//				sjyzjgDto.setTm1(cellValue);
//		}
//		if(headInfo.get("fz1")!=null) {
//			String cellValue = row.getCell(headInfo.get("fz1")).toString();
//			if(!isNumber(cellValue))
//				sjyzjgDto.setFz1("0");
//			else
//				sjyzjgDto.setFz1(cellValue);
//		}
//		if(headInfo.get("tm2")!=null) {
//			String cellValue = row.getCell(headInfo.get("tm2")).toString();
//			if(!isNumber(cellValue))
//				sjyzjgDto.setTm2("0");
//			else
//				sjyzjgDto.setTm2(cellValue);
//		}
//		if(headInfo.get("fz2")!=null) {
//			String cellValue = row.getCell(headInfo.get("fz2")).toString();
//			if(!isNumber(cellValue))
//				sjyzjgDto.setFz2("0");
//			else
//				sjyzjgDto.setFz2(cellValue);
//		}
//		if(headInfo.get("tm3")!=null) {
//			String cellValue = row.getCell(headInfo.get("tm3")).toString();
//			if(!isNumber(cellValue))
//				sjyzjgDto.setTm3("0");
//			else
//				sjyzjgDto.setTm3(cellValue);
//		}
//		if(headInfo.get("fz3")!=null) {
//			String cellValue = row.getCell(headInfo.get("fz3")).toString();
//			if(!isNumber(cellValue))
//				sjyzjgDto.setFz3("0");
//			else
//				sjyzjgDto.setFz3(cellValue);
//		}
//		return sjyzjgDto;
//	}
	
	/**
	 * 保存验证明细
	 * @param sjyzmxDto
	 * @param sjyzDto
	 * @return
	 */
//	private boolean saveYzmx(SjyzmxDto sjyzmxDto,SjyzDto sjyzDto) {
//		//判断当前的验证明细是否存在，如果存在进行修改，如果不存在，进行添加
//		int count=sjyzmxService.getCount(sjyzmxDto);
//		boolean result=false;
//		if(count>0) {
//			sjyzmxDto.setXgry(sjyzDto.getXgry());
//			result=sjyzmxService.update(sjyzmxDto);
//		}else if(count==0){
//			if(StringUtil.isBlank(sjyzmxDto.getYzmxid())) {
//				sjyzmxDto.setYzmxid(StringUtil.generateUUID());
//			}
//			sjyzmxDto.setLrry(sjyzDto.getXgry());
//			int maxXh=sjyzmxService.getMaxXh(sjyzDto.getYzid())+1;
//			sjyzmxDto.setXh(maxXh+"");
//			result=sjyzmxService.insert(sjyzmxDto);
//		}
//		return result;
//	}
	
	/**
	 * 判断是否是数字类型
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
	    String reg = "^[0-9]+(.[0-9]+)?$";
	    return str.matches(reg);
	}
	
	/**
	 * 获取KZ的标题信息
	 * @param headrow
	 * @return
	 */
	private Map<String,Integer> getKzHeadInfo(XSSFRow headrow){
		Map<String,Integer> headInfo = new HashMap<>();
		int cellLength = headrow.getPhysicalNumberOfCells();
		//获取各提取字段所在的位置
		for (int i = 0; i < cellLength; i++) {
			String cellValue = headrow.getCell(i).toString();
			SetKzHeadInfo(headInfo,cellValue,i);
		}
		
		return headInfo;
	}
	
	/**
	 * 获取KZ的标题信息
	 * @param headrow
	 * @return
	 */
	private Map<String,Integer> getKzHeadInfo(HSSFRow headrow){
		Map<String,Integer> headInfo = new HashMap<>();
		int cellLength = headrow.getPhysicalNumberOfCells();
		//获取各提取字段所在的位置
		for (int i = 0; i < cellLength; i++) {
			String cellValue = headrow.getCell(i).toString();
			SetKzHeadInfo(headInfo,cellValue,i);
		}

		return headInfo;
	}
	
	/**
	 * 提取KZ标题信息
	 * @param headInfo
	 * @param cellValue
	 * @param index
	 */
	private void SetKzHeadInfo(Map<String,Integer> headInfo,String cellValue,int index) {
		if("样本名称".equals(cellValue)) {
			headInfo.put("nbbm",index);
		}else if("样本类型".equals(cellValue)) {
			headInfo.put("yblx",index);
		}else if("荧光".equals(cellValue)) {
			headInfo.put("yg",index);
		}else if("目标基因".equals(cellValue)) {
			headInfo.put("mbjy",index);
		}else if("Ct".equals(cellValue)) {
			headInfo.put("ct",index);
		}else if("Tm1（℃）".equals(cellValue)) {
			headInfo.put("tm1",index);
		}else if("峰值1".equals(cellValue)) {
			headInfo.put("fz1",index);
		}else if("Tm2（℃）".equals(cellValue)) {
			headInfo.put("tm2",index);
		}else if("峰值2".equals(cellValue)) {
			headInfo.put("fz2",index);
		}else if("Tm3（℃）".equals(cellValue)) {
			headInfo.put("tm3",index);
		}else if("峰值3".equals(cellValue)) {
			headInfo.put("fz3",index);
		}else if("孔位".equals(cellValue)) {
			headInfo.put("kw",index);
		}else if("样本编号".equals(cellValue)) {
			headInfo.put("ybbh",index);
		}
	}
	
	/**
	 * 获取KZ的标题信息
	 * @param headrow
	 * @return
	 */
	/*private Map<String,Integer> getStepHeadInfo(XSSFRow headrow){
		Map<String,Integer> headInfo = new HashMap<String,Integer>();
		int cellLength = headrow.getPhysicalNumberOfCells();
		//获取各提取字段所在的位置
		for (int i = 0; i < cellLength; i++) {
			String cellValue = headrow.getCell(i).toString();
			SetStepHeadInfo(headInfo,cellValue,i);
		}
		
		return headInfo;
	}*/
	
	/**
	 * 获取KZ的标题信息
	 * @param headrow
	 * @return
	 */
	/*private Map<String,Integer> getStepHeadInfo(HSSFRow headrow){
		Map<String,Integer> headInfo = new HashMap<String,Integer>();
		int cellLength = headrow.getPhysicalNumberOfCells();
		//获取各提取字段所在的位置
		for (int i = 0; i < cellLength; i++) {
			String cellValue = headrow.getCell(i).toString();
			SetStepHeadInfo(headInfo,cellValue,i);
		}
		
		return headInfo;
	}*/
	

	/**
	 * 提取KZ标题信息
	 * @param headInfo
	 * @param cellValue
	 * @param index
	 */
	/*private void SetStepHeadInfo(Map<String,Integer> headInfo,String cellValue,int index) {
		if("Well".equals(cellValue)) {
			headInfo.put("kw",index);
		}else if("Sample Name".equals(cellValue)) {
			headInfo.put("nbbm",index);
		}else if("Task".equals(cellValue)) {
			headInfo.put("yblx",index);
		}else if("Reporter".equals(cellValue)) {
			headInfo.put("yg",index);
		}else if("Target Name".equals(cellValue)) {
			headInfo.put("mbjy",index);
		}else if("Cт".equals(cellValue)) {
			headInfo.put("ct",index);
		}else if("Tm1".equals(cellValue)) {
			headInfo.put("tm1",index);
		}else if("Tm2".equals(cellValue)) {
			headInfo.put("tm2",index);
		}else if("Tm3".equals(cellValue)) {
			headInfo.put("tm3",index);
		}
	}*/
	
	/**
	 * 检查标题定义，主要防止模板信息过旧
	 * @param defined
	 * @return
	 */
	public boolean checkDefined(List<Map<String,String>> defined) {
		return true;
	}
	
	/**
	 * 选中导出
	 * 
	 * @param params
	 * @return
	 */
	public List<SjyzDto> getListForSelectExp(Map<String, Object> params)
	{
		SjyzDto sjyzDto = (SjyzDto) params.get("entryData");
		queryJoinFlagExport(params, sjyzDto);
        return dao.getListForSelectExp(sjyzDto);
	}
	
	/**
	 * 根据搜索条件获取导出条数
	 * 
	 * @param params
	 * @return
	 */
	public int getCountForSearchExp(SjyzDto sjyzDto,Map<String, Object> params) {
		//高级筛选增加，审核岗位xlcxh和审核岗位名称进行数据查找
		//根据系统审核审核类别查找审核流程
		if (sjyzDto.getXlcxhs() != null && sjyzDto.getXlcxhs().length >0){
			List<XtshDto> xtshDtos = getSjyzShlc(sjyzDto);//获取验证流程的审核岗位步骤
			List<String> spgwids = new ArrayList<>();
			for (XtshDto xtshDto: xtshDtos){
				spgwids.add(xtshDto.getGwid());
			}
			sjyzDto.setGwids(spgwids);
		}
        return dao.getCountForSearchExp(sjyzDto);
	}
	
	/**
	 * 根据搜索条件分页获取导出信息
	 * @param params
	 * @return
	 */
	public List<SjyzDto> getListForSearchExp(Map<String,Object> params){
		SjyzDto sjyzDto = (SjyzDto)params.get("entryData");
		//高级筛选增加，审核岗位xlcxh和审核岗位名称进行数据查找
		//根据系统审核审核类别查找审核流程
		if (sjyzDto.getXlcxhs() != null && sjyzDto.getXlcxhs().length >0){
			List<XtshDto> xtshDtos = getSjyzShlc(sjyzDto);//获取验证流程的审核岗位步骤
			List<String> spgwids = new ArrayList<>();
			for (XtshDto xtshDto: xtshDtos){
				spgwids.add(xtshDto.getGwid());
			}
			sjyzDto.setGwids(spgwids);
		}
		queryJoinFlagExport(params,sjyzDto);
		return dao.getListForSearchExp(sjyzDto);
	}

	
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String, Object> params, SjyzDto sjyzDto)
	{
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuffer sqlParam = new StringBuffer();
		for (DcszDto dcszDto : choseList)
		{
			
			if (dcszDto == null || dcszDto.getDczd() == null)
				 continue; 
		 	if(dcszDto.getDczd().equalsIgnoreCase("YBLXMC")) { 
		 		sjyzDto.setYblx_flg("Y");
		 	}else if(dcszDto.getDczd().equalsIgnoreCase("YZDM")||dcszDto.getDczd().equalsIgnoreCase("YZLBMC")) {
		 		sjyzDto.setYzlb_flg("Y");
		 	}else if(dcszDto.getDczd().equalsIgnoreCase("QFMC")) {
		 		sjyzDto.setQf_flg("Y");
		 	}else if(dcszDto.getDczd().equalsIgnoreCase("YZJGMC")) {
		 		sjyzDto.setYzjg_flg("Y");
		 	}else if(dcszDto.getDczd().equalsIgnoreCase("KHTZMC")) {
		 		sjyzDto.setKhtz_flg("Y");
		 	}else if(dcszDto.getDczd().equalsIgnoreCase("lRRYMC")) {
		 		sjyzDto.setLrry_flg("Y"); 
		 	}
			sqlParam.append(",");
			if (StringUtil.isNotBlank(dcszDto.getSqlzd()))
			{
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		sjyzDto.setSqlparam(sqlParam.toString());
	}

	/**
	 * 发送送检验证通知
	 * @param sjyzDto
	 * @return
	 */
	private boolean sendVerificationMessage(SjyzDto sjyzDto,String auditState) {
		// TODO Auto-generated method stub
		// 如果没有设置URL，则直接返回，不发送消息。此为内容修改使用
		if (StringUtil.isBlank(menuurl))
			return true;
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(sjyzDto.getSjid());
		Map<String, Object> map= new HashMap<>();
		String ICOMM_SJ00054=xxglservice.getModelById("ICOMM_SJ00054").getXxnr();
		String ICOMM_SJ00055=xxglservice.getModelById("ICOMM_SJ00055").getXxnr();
		String ICOMM_SJ00056=xxglservice.getModelById("ICOMM_SJ00056").getXxnr();
		String ICOMM_SJ00057=xxglservice.getModelById("ICOMM_SJ00057").getXxnr();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		String nowDate=sdf.format(date);
		String first=ICOMM_SJ00054+"\r\n\r\n"+"患者姓名:"+sjyzDto.getHzxm();
		String remark ="验证类别:"+sjyzDto.getYzlbmc()+"\r\n\r\n"+"标本类型:"+sjyzDto.getYblxmc();
		try {
			map.put("templateid", ybzt_templateid);
			map.put("yxbt", ICOMM_SJ00054);
			map.put("wxbt", first);
			map.put("ddbt", ICOMM_SJ00054);
			if(auditState.equals("SUBMIT")) {
				map.put("yxnr", ICOMM_SJ00055+"\r\n\r\n"+"患者姓名:"+sjyzDto.getHzxm()+remark);
				map.put("keyword2", nowDate);
				map.put("keyword1", ICOMM_SJ00055);
				map.put("remark", remark);
				map.put("ddnr",ICOMM_SJ00055+"\r\n\r\n"+"患者姓名:"+sjyzDto.getHzxm()+remark);
			}else if(auditState.equals("BACK")) {
				map.put("yxnr", ICOMM_SJ00056+"\r\n\r\n"+"患者姓名:"+sjyzDto.getHzxm()+remark);
				map.put("keyword2", nowDate);
				map.put("keyword1",ICOMM_SJ00056 );
				map.put("remark", remark);
				map.put("ddnr",ICOMM_SJ00056+"\r\n\r\n"+"患者姓名:"+sjyzDto.getHzxm()+remark);
			}else if(auditState.equals("UNPASS")) {
				map.put("yxnr", ICOMM_SJ00057+"\r\n\r\n"+"患者姓名:"+sjyzDto.getHzxm()+remark);
				map.put("keyword2", nowDate);
				map.put("keyword1", ICOMM_SJ00057);
				map.put("remark", remark);
				map.put("ddnr", ICOMM_SJ00057+"\r\n\r\n"+"患者姓名:"+sjyzDto.getHzxm()+remark);
			}
			map.put("keyword3", sjyzDto.getHzxm()+"-"+sjyzDto.getYblxmc());
			map.put("xxmb","TEMPLATE_EXCEPTION");
			String reporturl = menuurl +"/wechat/statistics/wxRemark?remark="+remark;

			map.put("reporturl",reporturl);
			sjxxCommonService.sendMessage(sjxxDto,map);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 查询审批岗位钉钉
	 * @param sjyzDto
	 * @return
	 */
	@Override
	public List<SjyzDto> getSpgwcyList(SjyzDto sjyzDto) {
		// TODO Auto-generated method stub
        return dao.getSpgwcyList(sjyzDto);
	}

	/**
	 * 修改检测单位时发送信息通知相关人员
	 * @param sjyzDto
	 * @return
	 */
	@Override
	public void sendUpdateJcdwMessage(SjyzDto sjyzDto) {
		// TODO Auto-generated method stub
//		String token = talkUtil.getToken();
		List<SjyzDto> gwcyList=getSpgwcyList(sjyzDto);
		if(gwcyList!=null && gwcyList.size()>0) {
			SjyzDto sjyzDto_t=getDto(sjyzDto);
			String ICOMM_SJ00002 = xxglService.getMsg("ICOMM_SJ00002");
			String ICOMM_SH00044 = xxglService.getMsg("ICOMM_SH00044");
			for (int i = 0; i < gwcyList.size(); i++) {
				if (StringUtil.isNotBlank(gwcyList.get(i).getDdid())){
					talkUtil.sendWorkMessage(gwcyList.get(i).getYhm(),gwcyList.get(i).getDdid(),ICOMM_SJ00002,
							StringUtil.replaceMsg(ICOMM_SH00044,sjyzDto_t.getHzxm(),sjyzDto_t.getNbbm()!=null?sjyzDto_t.getNbbm():"",sjyzDto_t.getJcxmmc(),sjyzDto_t.getYzlbmc(),sjyzDto_t.getQfmc(),sjyzDto_t.getJcdwmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));

				}
			}
		}
	}

    /**
     * 为生成pcr对接文档做准备(查找送检验证中区分为正常的数据)
     */
	@Override
	public List<Map<String,String>>  getDtoForPcrReady(List<Map<String,String>> list) {
		// TODO Auto-generated method stub
		return dao.getDtoForPcrReady(list);
	}

	/**
	 * 为生成pcr对接文档做准备(查找送检验证中区分为去人源的数据)
	 */
	@Override
	public List<Map<String, String>> getREMDtoForPcrReady(List<Map<String, String>> nbbmREMlist) {
		return dao.getREMDtoForPcrReady(nbbmREMlist);
	}

	/**
	 * 根据sjid获取sjyzDto
	 * @param sjid
	 * @return
	 */
	public List<SjyzDto> getDtoListBySjid(String sjid) {
		List<SjyzDto> t_List = dao.getDtoListBySjid(sjid);
		try{
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_VERIFICATION.getCode(), "zt", "yzid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_LAB_VERIFICATION.getCode(), "zt", "yzid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t_List;
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) throws BusinessException {
		return null;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) throws BusinessException {
		Map<String, Object> map =new HashMap<>();
		List<String> ids = (List<String>)param.get("ywids");
		SjyzDto sjyzDto = new SjyzDto();
		sjyzDto.setIds(ids);
		List<SjyzDto> dtoList = dao.getDtoByIds(sjyzDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(SjyzDto dto:dtoList){
				list.add(dto.getYzid());
			}
		}
		map.put("list",list);
		return map;
	}

	@Override
	public List<SjyzDto> getByYzlbAndSjid(SjyzDto sjyzDto) {
		List<SjyzDto> list = dao.getByYzlbAndSjid(sjyzDto);
		try{
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_VERIFICATION.getCode(), "zt", "yzid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_LAB_VERIFICATION.getCode(), "zt", "yzid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		}catch (BusinessException e){
			logger.error("getByYzlbAndSjid" , e.getMessage());
		}
		return list;
	}

	/**
	 * 根据验证IDS获取数据
	 * @param sjyzDto
	 * @return
	 */
	@Override
	public List<SjyzDto> getDtoByIds(SjyzDto sjyzDto) {
		return dao.getDtoByIds(sjyzDto);
	}

	@Override
	public SjyzDto getSjyzDto(SjyzDto sjyzDto) {
		return dao.getSjyzDto(sjyzDto);
	}

	@Override
	public boolean updateListYzjg(List<SjyzDto> sjyzDtoList) {
		int i = dao.updateListYzjg( sjyzDtoList);
        return i > 0;
	}

	private String judegJsonIsCorrect(MultipartFile file, String jsonparam) {
		//先判断字符串是否正确
		if (StringUtil.isBlank(jsonparam)){
			logger.error("天隆调用上传接口/tl/uploadTlFile，传入json为空字符，文件名为 "+file.getOriginalFilename()+",json为 "+jsonparam);
			return "解析json出错，json为空字符串";
		}
		if (!file.getOriginalFilename().endsWith(".tlpd")){
			logger.error("天隆调用上传接口/tl/uploadTlFile，未上传文件后缀为.tlpd的源文件，方法结束");
			return "请上传文件后缀为.tlpd的源文件";
		}
		try{
			JSON.parse(jsonparam);
		} catch (Exception e){
			logger.error("天隆调用上传接口/tl/uploadTlFile，传入json格式错误，文件名为： "+file.getOriginalFilename()+",json为 "+jsonparam);
			return "传入文件json错误，解析json出错，json格式错误，json信息: "+jsonparam;
		}
		return "";
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public String uploadTlFile(MultipartFile file, String jsonparam) throws IOException {
		logger.error("天隆调用上传接口/tl/uploadTlFile，上传文件的文件名称："+ file.getOriginalFilename()+"   json为"+ jsonparam );
		String ret = judegJsonIsCorrect(file, jsonparam);
		if (StringUtil.isNotBlank(ret)){
			return ret;
		}
		String wjm = file.getOriginalFilename().split(".tlpd")[0];
		String tlpdFileName = StringUtil.generateUUID();
		String xlsxFileName = StringUtil.generateUUID();
		//根据日期创建文件夹
		String storePath = prefix + releaseFilePath + BusTypeEnum.IMP_VERIFIFCATION+"/"+ "UP"+
				DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
				DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
				DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		File file1 = new File(storePath);
		if (!file1.exists()) {
			file1.mkdirs();
		}
		String tlpdPath = storePath+"/"+tlpdFileName+".tlpd";
		String xlsxPath = storePath+"/"+xlsxFileName+".xlsx";

		//解析json，生成xlsx文档
		List<Map<String,String>> nbbmlist = new ArrayList<>();
		List<Map<String,String>> nbbmREMlist = new ArrayList<>();
		List<WkmxPcrResultModel> pcrlist = new ArrayList<>();
		if (StringUtil.isNotBlank(jsonparam) && JSONObject.parseObject(jsonparam).containsKey("result")){
//			result = JSONObject.parseObject(jsonparam).getJSONArray("result");
			pcrlist= JSONObject.parseArray(String.valueOf(JSONObject.parseObject(jsonparam).get("result")), WkmxPcrResultModel.class);
		}
//		pcrlist.sort(null);
		Collections.sort(pcrlist, new Comparator<>() {
            @Override
            public int compare(WkmxPcrResultModel w1, WkmxPcrResultModel w2) {
                return Integer.parseInt(w1.getWell().substring(1)) - Integer.parseInt(w2.getWell().substring(1));
            }
        });
		Collections.sort(pcrlist, new Comparator<>() {
            @Override
            public int compare(WkmxPcrResultModel w1, WkmxPcrResultModel w2) {
                // well如A1,B2，先根据数字升序排序，数字相同时候根据字母升序排序
                int flag = Integer.parseInt(w1.getWell().substring(1)) - Integer.parseInt(w2.getWell().substring(1));
                if (flag < 0) {
                    return -1;
                } else if (flag > 0) {
                    // 0的含义是在两个元素相同时，不交换顺序（为了排序算法的稳定性，可以使用1来代替0，不要用-1来代替0）
                    return 0;
                } else {
                    // 字母升序排序
                    if (w1.getWell().substring(0, 1).compareTo(w2.getWell().substring(0, 1)) < 0) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
        });
		/*-------------------修改生成xlsx表格顺序文件开始------------------*/
//		boolean isAbandon = false;
		DecimalFormat df = new DecimalFormat("0.00");
		if (pcrlist != null && pcrlist.size()>0){
			// 1、创建工作表
			XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
			XSSFSheet xssfSheet = xssfWorkbook.createSheet("sheet1");
			XSSFRow xssfRow; // 行
			XSSFCell xssfCell; // 列
			//先创建好固定表头
			xssfRow = xssfSheet.getRow(0);
			if (xssfRow == null) {
				xssfRow = xssfSheet.createRow(0);
			}
			for ( int j=0; j<16; j++){
				// 3、创建单元格
				xssfCell = xssfRow.createCell(j);
				// 4、设置单元格内容
				switch (j){
					case 0:
						xssfCell.setCellValue("孔位");
						break;//孔位
					case 1:
						xssfCell.setCellValue("样本编号");
						break;//样本编号
					case 2:
						xssfCell.setCellValue("样本名称");
						break;//样本名称
					case 3:
						xssfCell.setCellValue("样本类型");
						break;
					case 4:
						xssfCell.setCellValue("荧光");
						break;//荧光
					case 5:
						xssfCell.setCellValue("目标基因");
						break;//目标基因
					case 6:
						xssfCell.setCellValue("项目名称");
						break;//项目名称
					case 7:
						xssfCell.setCellValue("Ct");
						break;//ct
					case 8:
						xssfCell.setCellValue("浓度");
						break;//浓度
					case 9:
						xssfCell.setCellValue("检测结果");
						break;//检测结果
					case 10:
						xssfCell.setCellValue("浓度单位");
						break;//浓度单位
					case 11:
						xssfCell.setCellValue("标准浓度");
						break;//标准浓度
					case 12:
						xssfCell.setCellValue("参比荧光");
						break;//参比荧光
					case 13:
						xssfCell.setCellValue("唯一性编号");
						break;//唯一性编号
					case 14:
						xssfCell.setCellValue("重复组");
						break;//重复组
					case 15:
						xssfCell.setCellValue("QC");
						break;//QC
					default:
				}
			}
			for (int i=0; i<pcrlist.size(); i++){
				Map<String,String> map1 = new HashMap<>();
				Map<String,String> mapREM = new HashMap<>();
				//判断数据是否报废，报废了则后续上传的操作都不执行
				//正常情况：PC的CT值全部有值，NC的CT值都为空值/或HEX通道存在值但值>40，NC和PC出现其他情况都是有污染，数据报废
//				if (result.getJSONObject(i).getString("SampleName") != null && result.getJSONObject(i).getString("SampleName").contains("PC")){
//					if ( Integer.parseInt(result.getJSONObject(i).getString("CtVaule"))/100 < 0){//ct值无值表现为-1，其余值表现为大于0
//						isAbandon = true;
//						log.error("天隆调用上传接口/tl/uploadTlFile，PC阳性对照的CT值不满足全部有值的情况，数据不上传");
//						ret = "PC阳性对照的CT值不满足全部有值的情况，数据不上传";
//						break;
//					}
//				}
//				if (result.getJSONObject(i).getString("SampleName") != null && result.getJSONObject(i).getString("SampleName").contains("NC")){
//					if ( Integer.parseInt(result.getJSONObject(i).getString("CtVaule"))/100 >= 0 ){
//						if ("HEX".equals(result.getJSONObject(i).getString("DyeName"))  ){
//							if ( Integer.parseInt( result.getJSONObject(i).getString("CtVaule") )/100 < 40 ){//<40
//								isAbandon = true;
//								log.error("天隆调用上传接口/tl/uploadTlFile，NC阴性对照HEX的CT值不满足>=40的情况，数据不上传");
//								ret = "NC阴性对照HEX的CT值不满足>=40的情况，数据不上传";
//								break;
//							}
//						}else{
//							isAbandon = true;
//							log.error("天隆调用上传接口/tl/uploadTlFile，NC阴性对照的CT值不满足全部空值的情况，数据不上传");
//							ret = "NC阴性对照的CT值不满足全部空值或HEX通道CT值>=40的情况，数据不上传";
//							break;
//						}
//					}
//				}
				if ( "FAM".equals(pcrlist.get(i).getDyeName()) ){
					String sampleNumber = pcrlist.get(i).getSampleNumber();
					String sampleName = pcrlist.get(i).getSampleName();
					String geneName = pcrlist.get(i).getGeneName();
					String nbbm = StringUtil.isBlank(sampleNumber)?sampleName:sampleNumber;
					if (StringUtil.isNotBlank(nbbm)) {
						if (nbbm.endsWith("-REM")) {
							nbbm = nbbm.split("-REM")[0];
							mapREM.put("nbbm", nbbm);
							if ("结核".equals(geneName)) {
								mapREM.put("yzdm", "MTM");
							} else if ("耶氏".equals(geneName)) {
								mapREM.put("yzdm", "ACP");
							} else {
								mapREM.put("yzdm", geneName);
							}
							nbbmREMlist.add(mapREM);
						} else {
							map1.put("nbbm", nbbm);
							if ("结核".equals(geneName)) {
								map1.put("yzdm", "MTM");
							} else if ("耶氏".equals(geneName)) {
								map1.put("yzdm", "ACP");
							} else {
								map1.put("yzdm", geneName);
							}
							nbbmlist.add(map1);
						}
					}
				}
				// 2、在sheet中创建行，注意判断第i行是否已经创建，否则会覆盖之前的数据
				xssfRow = xssfSheet.getRow(i+1);
				if (xssfRow == null) {
					xssfRow = xssfSheet.createRow(i+1);
				}
				for ( int j=0; j<16; j++){
					// 3、创建单元格
					xssfCell = xssfRow.createCell(j);
					// 4、设置单元格内容
					switch (j){
						case 0:
							xssfCell.setCellValue(pcrlist.get(i).getWell());
							break;//孔位
						case 1:
							xssfCell.setCellValue(pcrlist.get(i).getSampleNumber());
							break;//样本编号
						case 2:
							xssfCell.setCellValue(pcrlist.get(i).getSampleName());
							break;//样本名称
						case 3:
							switch(pcrlist.get(i).getSampleType()){//样本类型
								case "1": xssfCell.setCellValue("待测"); break;
								case "2": xssfCell.setCellValue("标准"); break;
								case "3": xssfCell.setCellValue("阳性对照"); break;
								case "4": xssfCell.setCellValue("阴性对照"); break;
								default: xssfCell.setCellValue(""); break;
							}
							break;
						case 4:
							xssfCell.setCellValue(pcrlist.get(i).getDyeName());
							break;//荧光
						case 5:
							xssfCell.setCellValue(pcrlist.get(i).getGeneName());
							break;//目标基因
						case 6:
							break;//项目名称
						case 7:
							if ( Integer.parseInt(pcrlist.get(i).getCtVaule())/100 < 0 ){
								xssfCell.setCellValue("-");
							}else {
								xssfCell.setCellValue( df.format( Double.parseDouble(pcrlist.get(i).getCtVaule())/100) );
							}
							break;//ct
						case 8:
							xssfCell.setCellValue(pcrlist.get(i).getConcentration());
							break;//浓度
						case 9:
							break;//检测结果
						case 10:
							xssfCell.setCellValue(pcrlist.get(i).getConcentrationUnit());
							break;//浓度单位
						case 11:
							xssfCell.setCellValue(pcrlist.get(i).getStdConcentration());
							break;//标准浓度
						case 12:
							xssfCell.setCellValue(pcrlist.get(i).getReferenceDye());
							break;//参比荧光
						case 13:
							if (StringUtil.isNotBlank(pcrlist.get(i).getSampleUID()) ){
								xssfCell.setCellValue(pcrlist.get(i).getSampleUID());
							}
							break;//唯一性编号
						case 14:
							xssfCell.setCellValue(pcrlist.get(i).getReplicatedGroup());
							break;//重复组
						case 15:
							xssfCell.setCellValue(pcrlist.get(i).getQcResult());
							break;//QC
						default:
					}
				}
			}
//			if (!isAbandon){
			// 5、导出excel
			File ff = new File(xlsxPath);
			OutputStream out = new FileOutputStream(ff);
			xssfWorkbook.write(out);
			out.close();
			xssfWorkbook.close();
			logger.error("天隆调用上传接口/tl/uploadTlFile，上传xlsx文件成功，文件路径为"+xlsxPath);
//			}
		}
		/*-------------------生成xlsx表格文件结束------------------*/
//		/*-------------------生成xlsx表格文件开始------------------*/
////		boolean isAbandon = false;
//		DecimalFormat df = new DecimalFormat("0.00");
//		if (result != null && result.size()>0){
//			// 1、创建工作表
//			XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
//			XSSFSheet xssfSheet = xssfWorkbook.createSheet("sheet1");
//			XSSFRow xssfRow; // 行
//			XSSFCell xssfCell; // 列
//			//先创建好固定表头
//			xssfRow = xssfSheet.getRow(0);
//			if (xssfRow == null) {
//				xssfRow = xssfSheet.createRow(0);
//			}
//			for ( int j=0; j<16; j++){
//				// 3、创建单元格
//				xssfCell = xssfRow.createCell(j);
//				// 4、设置单元格内容
//				switch (j){
//					case 0:
//						xssfCell.setCellValue("孔位");
//						break;//孔位
//					case 1:
//						xssfCell.setCellValue("样本编号");
//						break;//样本编号
//					case 2:
//						xssfCell.setCellValue("样本名称");
//						break;//样本名称
//					case 3:
//						xssfCell.setCellValue("样本类型");
//						break;
//					case 4:
//						xssfCell.setCellValue("荧光");
//						break;//荧光
//					case 5:
//						xssfCell.setCellValue("目标基因");
//						break;//目标基因
//					case 6:
//						xssfCell.setCellValue("项目名称");
//						break;//项目名称
//					case 7:
//						xssfCell.setCellValue("Ct");
//						break;//ct
//					case 8:
//						xssfCell.setCellValue("浓度");
//						break;//浓度
//					case 9:
//						xssfCell.setCellValue("检测结果");
//						break;//检测结果
//					case 10:
//						xssfCell.setCellValue("浓度单位");
//						break;//浓度单位
//					case 11:
//						xssfCell.setCellValue("标准浓度");
//						break;//标准浓度
//					case 12:
//						xssfCell.setCellValue("参比荧光");
//						break;//参比荧光
//					case 13:
//						xssfCell.setCellValue("唯一性编号");
//						break;//唯一性编号
//					case 14:
//						xssfCell.setCellValue("重复组");
//						break;//重复组
//					case 15:
//						xssfCell.setCellValue("QC");
//						break;//QC
//					default:
//				}
//			}
//			for (int i=0; i<result.size(); i++){
//				Map<String,String> map1 = new HashMap<>();
//				Map<String,String> mapREM = new HashMap<>();
//				//判断数据是否报废，报废了则后续上传的操作都不执行
//				//正常情况：PC的CT值全部有值，NC的CT值都为空值/或HEX通道存在值但值>40，NC和PC出现其他情况都是有污染，数据报废
////				if (result.getJSONObject(i).getString("SampleName") != null && result.getJSONObject(i).getString("SampleName").contains("PC")){
////					if ( Integer.parseInt(result.getJSONObject(i).getString("CtVaule"))/100 < 0){//ct值无值表现为-1，其余值表现为大于0
////						isAbandon = true;
////						log.error("天隆调用上传接口/tl/uploadTlFile，PC阳性对照的CT值不满足全部有值的情况，数据不上传");
////						ret = "PC阳性对照的CT值不满足全部有值的情况，数据不上传";
////						break;
////					}
////				}
////				if (result.getJSONObject(i).getString("SampleName") != null && result.getJSONObject(i).getString("SampleName").contains("NC")){
////					if ( Integer.parseInt(result.getJSONObject(i).getString("CtVaule"))/100 >= 0 ){
////						if ("HEX".equals(result.getJSONObject(i).getString("DyeName"))  ){
////							if ( Integer.parseInt( result.getJSONObject(i).getString("CtVaule") )/100 < 40 ){//<40
////								isAbandon = true;
////								log.error("天隆调用上传接口/tl/uploadTlFile，NC阴性对照HEX的CT值不满足>=40的情况，数据不上传");
////								ret = "NC阴性对照HEX的CT值不满足>=40的情况，数据不上传";
////								break;
////							}
////						}else{
////							isAbandon = true;
////							log.error("天隆调用上传接口/tl/uploadTlFile，NC阴性对照的CT值不满足全部空值的情况，数据不上传");
////							ret = "NC阴性对照的CT值不满足全部空值或HEX通道CT值>=40的情况，数据不上传";
////							break;
////						}
////					}
////				}
//				if ( "FAM".equals(result.getJSONObject(i).getString("DyeName")) ){
//					String sampleNumber = result.getJSONObject(i).getString("SampleNumber");
//					String sampleName = result.getJSONObject(i).getString("SampleName");
//					String geneName = result.getJSONObject(i).getString("GeneName");
//					String nbbm = StringUtil.isBlank(sampleNumber)?sampleName:sampleNumber;
//					if (StringUtil.isNotBlank(nbbm)) {
//						if (nbbm.endsWith("-REM")) {
//							nbbm = nbbm.split("-REM")[0];
//							mapREM.put("nbbm", nbbm);
//							if ("结核".equals(geneName)) {
//								mapREM.put("yzdm", "MTM");
//							} else if ("耶氏".equals(geneName)) {
//								mapREM.put("yzdm", "ACP");
//							} else {
//								mapREM.put("yzdm", geneName);
//							}
//							nbbmREMlist.add(mapREM);
//						} else {
//							map1.put("nbbm", nbbm);
//							if ("结核".equals(geneName)) {
//								map1.put("yzdm", "MTM");
//							} else if ("耶氏".equals(geneName)) {
//								map1.put("yzdm", "ACP");
//							} else {
//								map1.put("yzdm", geneName);
//							}
//							nbbmlist.add(map1);
//						}
//					}
//				}
//				// 2、在sheet中创建行，注意判断第i行是否已经创建，否则会覆盖之前的数据
//				xssfRow = xssfSheet.getRow(i+1);
//				if (xssfRow == null) {
//					xssfRow = xssfSheet.createRow(i+1);
//				}
//				for ( int j=0; j<16; j++){
//					// 3、创建单元格
//					xssfCell = xssfRow.createCell(j);
//					// 4、设置单元格内容
//					switch (j){
//						case 0:
//							xssfCell.setCellValue(result.getJSONObject(i).getString("Well"));
//							break;//孔位
//						case 1:
//							xssfCell.setCellValue(result.getJSONObject(i).getString("SampleNumber"));
//							break;//样本编号
//						case 2:
//							xssfCell.setCellValue(result.getJSONObject(i).getString("SampleName"));
//							break;//样本名称
//						case 3:
//							switch(result.getJSONObject(i).getString("SampleType")){//样本类型
//								case "1": xssfCell.setCellValue("待测"); break;
//								case "2": xssfCell.setCellValue("标准"); break;
//								case "3": xssfCell.setCellValue("阳性对照"); break;
//								case "4": xssfCell.setCellValue("阴性对照"); break;
//								default: xssfCell.setCellValue(""); break;
//							}
//							break;
//						case 4:
//							xssfCell.setCellValue(result.getJSONObject(i).getString("DyeName"));
//							break;//荧光
//						case 5:
//							xssfCell.setCellValue(result.getJSONObject(i).getString("GeneName"));
//							break;//目标基因
//						case 6:
//							break;//项目名称
//						case 7:
//							if ( Integer.parseInt(result.getJSONObject(i).getString("CtVaule"))/100 < 0 ){
//								xssfCell.setCellValue("-");
//							}else {
//								xssfCell.setCellValue( df.format( Double.parseDouble(result.getJSONObject(i).getString("CtVaule"))/100) );
//							}
//							break;//ct
//						case 8:
//							xssfCell.setCellValue(result.getJSONObject(i).getString("Concentration"));
//							break;//浓度
//						case 9:
//							break;//检测结果
//						case 10:
//							xssfCell.setCellValue(result.getJSONObject(i).getString("ConcentrationUnit"));
//							break;//浓度单位
//						case 11:
//							xssfCell.setCellValue(result.getJSONObject(i).getString("StdConcentration"));
//							break;//标准浓度
//						case 12:
//							xssfCell.setCellValue(result.getJSONObject(i).getString("ReferenceDye"));
//							break;//参比荧光
//						case 13:
//							if (StringUtil.isNotBlank(result.getJSONObject(i).getString("SampleUID")) ){
//								xssfCell.setCellValue(result.getJSONObject(i).getString("SampleUID"));
//							}
//							break;//唯一性编号
//						case 14:
//							xssfCell.setCellValue(result.getJSONObject(i).getString("ReplicatedGroup"));
//							break;//重复组
//						case 15:
//							xssfCell.setCellValue(result.getJSONObject(i).getString("QcResult"));
//							break;//QC
//						default:
//					}
//				}
//			}
////			if (!isAbandon){
//			// 5、导出excel
//			File ff = new File(xlsxPath);
//			OutputStream out = new FileOutputStream(ff);
//			xssfWorkbook.write(out);
//			out.close();
//			xssfWorkbook.close();
//			logger.error("天隆调用上传接口/tl/uploadTlFile，上传xlsx文件成功，文件路径为"+xlsxPath);
////			}
//		}
//		/*-------------------生成xlsx表格文件结束------------------*/
		/*-------------------上传tlpd文件+更新附件存放表信息开始------------------*/
//		if (!isAbandon){
//			上传tlpd文件
		saveMultipartFile(file, tlpdPath);
		logger.error("天隆调用上传接口/tl/uploadTlFile，上传tlpd文件成功，文件路径为"+tlpdPath);
		/*---------------------根据nbbmlist生成多条fjcfb---------------------*/
		DBEncrypt bpe = new DBEncrypt();
		List<Map<String,String>> yzAndWjs = new ArrayList<>();
		if (nbbmlist.size()>0){
			yzAndWjs = getDtoForPcrReady(nbbmlist);//去重sjyzService
		}
		if (nbbmREMlist.size()>0){
			List<Map<String,String>> yzAndWjsREM = getREMDtoForPcrReady(nbbmREMlist);//去重sjyzService
			if (yzAndWjsREM.size()>0){
				yzAndWjs.addAll(yzAndWjsREM);
			}
		}
		List<String> yzids = new ArrayList<>();
		List<String> itlpds = new ArrayList<>();
		List<String> ixlsxs = new ArrayList<>();
		List<String> utlpds = new ArrayList<>();
		List<String> uxlsxs = new ArrayList<>();
		for (Map<String,String> map : yzAndWjs) {
			String id = map.get("yzid");
			String wjhz = "";
			if (map.containsKey("wjhz")){
				wjhz = map.get("wjhz");
			}
			if (!yzids.contains(id)){
				yzids.add(id);
			}
			if (StringUtil.isBlank(wjhz)){
				itlpds.add(id);
				ixlsxs.add(id);
				continue;
			}
			if (".tlpd".equals(wjhz)){
				utlpds.add(id);
			} else if (".xlsx".equals(wjhz)){
				uxlsxs.add(id);
			}
		}
		for (String yzid : yzids) {
			if (!itlpds.contains(yzid) && !utlpds.contains(yzid)){
				itlpds.add(yzid);
			}
			if (!ixlsxs.contains(yzid) && !uxlsxs.contains(yzid)){
				ixlsxs.add(yzid);
			}
		}
		List<FjcfbDto> fjcfbDtos1 = new ArrayList<>();
		List<FjcfbDto> fjcfbDtos2 = new ArrayList<>();
//			List<FjcfbModel> fjcfbModels = new ArrayList<>();
		List<FjcfbDto> fjcfbModels = new ArrayList<>();
		//更新tlpd文件
		if ( new File(tlpdPath).exists() && utlpds.size()>0 ){
			for (String yzid : utlpds){
				FjcfbDto fjcfbtlpd = new FjcfbDto();
				fjcfbtlpd.setYwid( yzid );
				fjcfbtlpd.setWjmhz(".tlpd");
				fjcfbtlpd.setWjm("AUTO_"+wjm+".tlpd");
				fjcfbtlpd.setWjlj(bpe.eCode(tlpdPath));
				fjcfbtlpd.setFwjlj(bpe.eCode(storePath));
				fjcfbtlpd.setFwjm(bpe.eCode(tlpdFileName+".tlpd"));
				//***新增赋值
				fjcfbtlpd.setFjid(StringUtil.generateUUID());
				fjcfbtlpd.setYwlx(BusTypeEnum.IMP_VERIFIFCATION.getCode());
				fjcfbtlpd.setXh("1");
				fjcfbtlpd.setZhbj("0");
				fjcfbDtos1.add(fjcfbtlpd);
			}
			if (fjcfbDtos1.size()>0){
				fjcfbService.updateByYwidAndWjmhz(fjcfbDtos1);//根据ywid、ywlx、wjm为AUTO_开头和后缀名更新数据的scbj为2
				//***新增
				fjcfbService.batchInsertFjcfb(fjcfbDtos1);
			}
		}
		//更新xlsx文件
		if ( new File(xlsxPath).exists() && uxlsxs.size()>0 ){
			for (String yzid : uxlsxs){
				FjcfbDto fjcfbxlsx = new FjcfbDto();
				fjcfbxlsx.setYwid( yzid );
				fjcfbxlsx.setWjmhz(".xlsx");
				fjcfbxlsx.setWjm("AUTO_"+wjm+".xlsx");
				fjcfbxlsx.setWjlj(bpe.eCode(xlsxPath));
				fjcfbxlsx.setFwjlj(bpe.eCode(storePath));
				fjcfbxlsx.setFwjm(bpe.eCode(xlsxFileName+".xlsx"));
				//***新增赋值
				fjcfbxlsx.setFjid(StringUtil.generateUUID());
				fjcfbxlsx.setYwlx(BusTypeEnum.IMP_VERIFIFCATION.getCode());
				fjcfbxlsx.setXh("1");
				fjcfbxlsx.setZhbj("0");
				fjcfbDtos2.add(fjcfbxlsx);
			}
			if (fjcfbDtos2.size()>0){
				fjcfbService.updateByYwidAndWjmhz(fjcfbDtos2);//根据ywid、ywlx、wjm为AUTO_开头和后缀名更新数据的scbj为2
				//***新增
				fjcfbService.batchInsertFjcfb(fjcfbDtos2);
			}
		}
		//新增tlpd/xlsx文件
		if ( new File(tlpdPath).exists() && new File(xlsxPath).exists() && itlpds.size()>0 ){
			for (String yzid : itlpds){
				FjcfbDto fjcfbtlpd = new FjcfbDto();
				fjcfbtlpd.setFjid(StringUtil.generateUUID());//
				fjcfbtlpd.setYwid( yzid );//sjyz.getYzid()
				fjcfbtlpd.setWjm("AUTO_"+wjm+".tlpd");//----
				fjcfbtlpd.setYwlx(BusTypeEnum.IMP_VERIFIFCATION.getCode());//
				fjcfbtlpd.setWjlj(bpe.eCode(tlpdPath));//----
				fjcfbtlpd.setFwjlj(bpe.eCode(storePath));//----
				fjcfbtlpd.setFwjm(bpe.eCode(tlpdFileName+".tlpd"));//----
				fjcfbtlpd.setXh("1");//
				fjcfbtlpd.setZhbj("0");//
				fjcfbModels.add(fjcfbtlpd);
			}
		}
		if ( new File(tlpdPath).exists() && new File(xlsxPath).exists() && ixlsxs.size()>0 ){
			for (String yzid : ixlsxs){
				FjcfbDto fjcfbxlsx = new FjcfbDto();
				fjcfbxlsx.setFjid(StringUtil.generateUUID());
				fjcfbxlsx.setYwid( yzid );
				fjcfbxlsx.setWjm("AUTO_"+wjm+".xlsx");
				fjcfbxlsx.setYwlx(BusTypeEnum.IMP_VERIFIFCATION.getCode());
				fjcfbxlsx.setWjlj(bpe.eCode(xlsxPath));
				fjcfbxlsx.setFwjlj(bpe.eCode(storePath));
				fjcfbxlsx.setFwjm(bpe.eCode(xlsxFileName+".xlsx"));
				fjcfbxlsx.setXh("1");
				fjcfbxlsx.setZhbj("0");
				fjcfbModels.add(fjcfbxlsx);
			}
		}
		if (fjcfbModels.size()>0){
			fjcfbService.batchInsertFjcfb(fjcfbModels);
		}
//		}
		/*-------------------上传tlpd文件+更新附件存放表信息结束------------------*/
		/*------------------验证结果预判开始-----------------------*/
//		if (!isAbandon){
		//按照内部编码进行分组
		Map<String, List<WkmxPcrResultModel>> mapPcr = pcrlist.stream() .collect(Collectors.groupingBy(WkmxPcrResultModel::getWell));
		//验证类别基础数据,及存在两个基础数据（ACP类别和MTM类别）
		JcsjDto ACPTypeJcsj = new JcsjDto();
		JcsjDto MTMTypeJcsj = new JcsjDto();
		List<JcsjDto> yzlbJcsjlist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.VERIFICATION_TYPE.getCode());
		for (JcsjDto jcsj : yzlbJcsjlist){
			if ("MTM".equals(jcsj.getCsdm()))
				MTMTypeJcsj = jcsj;
			if ("ACP".equals(jcsj.getCsdm()))
				ACPTypeJcsj = jcsj;
		}
		//区分基础数据，及存在两个区分（正常A，去人源P）
		JcsjDto QryJcsj = new JcsjDto();
		JcsjDto ZcJcsj = new JcsjDto();
		List<JcsjDto> qfJcsjlist = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.DISTINGUISH.getCode());
		for (JcsjDto jcsj : qfJcsjlist){
			if ("A".equals(jcsj.getCsdm()))
				ZcJcsj = jcsj;
			if ("P".equals(jcsj.getCsdm()))
				QryJcsj = jcsj;
		}
		//验证结果基础数据，分为两个基础数据list，一个是fcsid为MTM的基础数据、一个是ACP的基础数据
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setFcsid(MTMTypeJcsj.getCsid());
		List<JcsjDto> yzjgMtmjcsjDtos = jcsjService.getJcsjDtoList(jcsjDto);
		jcsjDto.setFcsid(ACPTypeJcsj.getCsid());
		List<JcsjDto> yzjgAcpjcsjDtos = jcsjService.getJcsjDtoList(jcsjDto);
		//存放需要更新的送检验证结果
		List<SjyzjgDto> sjyzjgDtoList = new ArrayList<>();
//		List<SjyzDto> sjyzDtoList = new ArrayList<>();
		if (mapPcr != null && mapPcr.size()>0) {
			Iterator<Map.Entry<String, List<WkmxPcrResultModel>>> entries = mapPcr.entrySet().iterator();
			while (entries.hasNext()) {
				Map.Entry<String,  List<WkmxPcrResultModel>> entry = entries.next();
				List<WkmxPcrResultModel> resultModelList = entry.getValue();
				//先根据孔位中的FAM通道，由区分、验证类别、内部编码确定唯一送检验证信息
				SjyzDto sjyzDto = new SjyzDto();
				if (resultModelList != null && resultModelList.size() > 0){
					for (WkmxPcrResultModel resultModel:resultModelList) {
						if ("FAM".equals(resultModel.getDyeName())&&
								((StringUtil.isNotBlank(resultModel.getSampleName())
										&&!resultModel.getSampleName().contains("NC-")
										&&!resultModel.getSampleName().contains("PC-")
										&&!resultModel.getSampleName().contains("DC-"))
										||(StringUtil.isNotBlank(resultModel.getSampleNumber()))
										&&!resultModel.getSampleNumber().contains("NC-")
										&&!resultModel.getSampleNumber().contains("PC-")
										&&!resultModel.getSampleNumber().contains("DC-"))){
							String sampleNumber = resultModel.getSampleNumber();
							String sampleName = resultModel.getSampleName();
							String geneName = resultModel.getGeneName();
							String nbbm = StringUtil.isBlank(sampleNumber)?sampleName:sampleNumber;
							if (StringUtil.isNotBlank(nbbm)) {
								if (nbbm.endsWith("-REM")) {
									nbbm = nbbm.split("-REM")[0];
									sjyzDto.setNbbm(nbbm);
									sjyzDto.setQf(QryJcsj.getCsid());
									if ("结核".equals(geneName)||"MTM".equals(geneName)) {
										sjyzDto.setYzlb(MTMTypeJcsj.getCsid());
									} else if ("耶氏".equals(geneName)||"ACP".equals(geneName)) {
										sjyzDto.setYzlb(ACPTypeJcsj.getCsid());
									}
								} else {
									sjyzDto.setNbbm(nbbm);
									sjyzDto.setQf(ZcJcsj.getCsid());
									if ("结核".equals(geneName)||"MTM".equals(geneName)) {
										sjyzDto.setYzlb(MTMTypeJcsj.getCsid());
									} else if ("耶氏".equals(geneName)||"ACP".equals(geneName)) {
										sjyzDto.setYzlb(ACPTypeJcsj.getCsid());
									}
								}
								//根据nbbm、yzlb、qf可以确定唯一送检验证数据
								//上面判断可能出现yzlb为空的情况，为空时候根据nbbm和qf可能查出多数据，故验证类别不为空才进行查找
								if (StringUtil.isNotBlank(sjyzDto.getYzlb())){
									sjyzDto = getSjyzDto(sjyzDto);//sjyzService
								}
							}
							break;
						}
					}
				}
				//遍历孔位下的所有通道，遍历fcsid为验证类别的验证结果基础数据做对比
				//遍历fcsid为验证类别MTM的基础数据
				if (sjyzDto !=null && StringUtil.isNotBlank(sjyzDto.getYzid()) && "MTM".equals(sjyzDto.getYzdm())){
//					String yzjgmc = "";
					for (WkmxPcrResultModel pcrModel:resultModelList) {
						for (JcsjDto yzjgJcsj : yzjgMtmjcsjDtos){
							if (StringUtil.isNotBlank(pcrModel.getDyeName()) && StringUtil.isNotBlank(yzjgJcsj.getCskz1()) && pcrModel.getDyeName().equals(yzjgJcsj.getCskz1()) ){
								//若dyename通道名称和验证结果cskz1通道名称相同（FAM\HEX\ROX...），比较ct值得出结果是阴性还是阳性还是灰区
								SjyzjgDto sjyzjgDto = new SjyzjgDto();
								sjyzjgDto.setYzid(sjyzDto.getYzid());
								sjyzjgDto.setYzjg(yzjgJcsj.getCsid());
								sjyzjgDto.setYzlb(sjyzDto.getYzlb());
								String jlstr = sjyzjgService.drawAconclusion(pcrModel.getCtVaule());
								if (StringUtil.isNotBlank(jlstr)){
									sjyzjgDto.setJl(jlstr);
									sjyzjgDtoList.add(sjyzjgDto);
								}
								break;
							}
						}
					}
//					if (StringUtil.isNotBlank(yzjgmc)){
//						sjyzDto.setYzjg(yzjgmc.substring(1));
//						sjyzDtoList.add(sjyzDto);
//					}
				}else if (sjyzDto !=null && StringUtil.isNotBlank(sjyzDto.getYzid()) && "ACP".equals(sjyzDto.getYzdm())){
//					String yzjgmc = "";
					for (WkmxPcrResultModel pcrModel:resultModelList) {
						for (JcsjDto yzjgJcsj : yzjgAcpjcsjDtos){
							if (StringUtil.isNotBlank(pcrModel.getDyeName()) && StringUtil.isNotBlank(yzjgJcsj.getCskz1()) && pcrModel.getDyeName().equals(yzjgJcsj.getCskz1()) ){
								//若dyename通道名称和验证结果cskz1通道名称相同（FAM\HEX\ROX...），比较ct值得出结果是阴性还是阳性还是灰区
								SjyzjgDto sjyzjgDto = new SjyzjgDto();
								sjyzjgDto.setYzid(sjyzDto.getYzid());
								sjyzjgDto.setYzjg(yzjgJcsj.getCsid());
								sjyzjgDto.setYzlb(sjyzDto.getYzlb());
								String jlstr = sjyzjgService.drawAconclusion(pcrModel.getCtVaule());
								if (StringUtil.isNotBlank(jlstr)){
									sjyzjgDto.setJl(jlstr);
									sjyzjgDtoList.add(sjyzjgDto);
								}
								break;
							}
						}
					}
//					if (StringUtil.isNotBlank(yzjgmc)){
//						sjyzDto.setYzjg(yzjgmc.substring(1));
//						sjyzDtoList.add(sjyzDto);
//					}
				}
			}
		}
		//更新送检验证表的yzjg字段
//		if (sjyzDtoList.size()>0){
//			updateListYzjg(sjyzDtoList);//sjyzService
//		}
		//新增送检验证结果/更新送检验证结果的jl字段
		List<SjyzjgDto> insetSjyzjgList = new ArrayList<>();
		List<SjyzjgDto> updateSjyzjgList = new ArrayList<>();
		if (sjyzjgDtoList.size()>0){
			Map<String, SjyzjgDto> map = new ConcurrentHashMap<>();
			sjyzjgDtoList = sjyzjgDtoList.stream()
					// 通过 map 对数据进行去重，map 只用在这里进行去重
					.filter(sjyzjgStream -> map.put(sjyzjgStream.getYzid()+sjyzjgStream.getYzjg()+sjyzjgStream.getYzlb(), sjyzjgStream) == null)
					.collect(Collectors.toList());
//				sjyzjgService.updateListJl(sjyzjgDtoList);
			List<SjyzjgDto> sjyzjgDtos = sjyzjgService.getByYzlbYzidYzjg(sjyzjgDtoList);
			if (sjyzjgDtos !=null && sjyzjgDtos.size()>0){
				for (SjyzjgDto sjyzjg:sjyzjgDtos){
					for (SjyzjgDto sjyzjgDto : sjyzjgDtoList) {
						if (sjyzjg.getYzid().equals(sjyzjgDto.getYzid())&&
								sjyzjg.getYzlb().equals(sjyzjgDto.getYzlb())&&
								sjyzjg.getYzjg().equals(sjyzjgDto.getYzjg())){
							sjyzjgDto.setYzjgid(sjyzjg.getYzjgid());
							break;
						}
					}
				}
			}
			for (SjyzjgDto sjyzjgd:sjyzjgDtoList){
				if (StringUtil.isNotBlank(sjyzjgd.getYzjgid())){//修改
					updateSjyzjgList.add(sjyzjgd);
				}else {//新增
					sjyzjgd.setYzjgid(StringUtil.generateUUID());
					insetSjyzjgList.add(sjyzjgd);
				}
			}
		}
		if (updateSjyzjgList != null && updateSjyzjgList.size()>0){
			sjyzjgService.updateListJl(updateSjyzjgList);
		}
		if (insetSjyzjgList != null && insetSjyzjgList.size()>0){
			sjyzjgService.insertList(insetSjyzjgList);
		}
//		}
		/*------------------验证结果预判结束-----------------------*/
		return ret;
	}

	/**
	 * 送检验证审核流程步骤
	 * @return
	 */
	@Override
	public List<XtshDto> getSjyzShlc(SjyzDto sjyzDto) {
		return dao.getSjyzShlc(sjyzDto);
	}

	public String saveMultipartFile(MultipartFile file, String targetDirPath){
		if (file.equals("") || file.getSize() <= 0) {
			return null;
		} else {
			/*获取文件原名称*/
//			String originalFilename = file.getOriginalFilename();
//			toFile = new File(targetDirPath + File.separator + originalFilename);
			File toFile = new File(targetDirPath);
			String absolutePath = null;
			try {
				absolutePath = toFile.getCanonicalPath();
				/*判断路径中的文件夹是否存在，如果不存在，先创建文件夹*/
				String dirPath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
				File dir = new File(dirPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				InputStream ins = file.getInputStream();
				inputStreamToFile(ins, toFile);
				ins.close();
			} catch (IOException e) {
				logger.error("天隆上传tlpd(saveMultipartFile)报错："+e.getMessage());
				e.printStackTrace();
			}
			return absolutePath;
		}
	}
	//获取流文件
	private void inputStreamToFile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			logger.error("天隆上传tlpd(inputStreamToFile)报错："+e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public boolean rereadVerify(SjyzDto sjyzDto) throws IOException {
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(sjyzDto.getYzid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_VERIFIFCATION.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
		if (!CollectionUtils.isEmpty(fjcfbDtos)) {
			SjyzjgDto sjyzjgDto = new SjyzjgDto();
			sjyzjgDto.setXgry(sjyzDto.getXgry());
			SjyzDto dtoById = dao.getDtoById(sjyzDto.getYzid());
			sjyzjgDto.setCsdm(dtoById.getYzdm());
			sjyzjgDto.setYzid(dtoById.getYzid());
			for (FjcfbDto fjcfbDto_t :fjcfbDtos) {
				String wjm=fjcfbDto_t.getWjm(); //文件名
				String wjmhz=wjm.substring(wjm.lastIndexOf("."), wjm.length());
				String wjlj=fjcfbDto_t.getWjlj();//加密后的文件路径
				DBEncrypt encrypt=new DBEncrypt();
				String wjlj_d=encrypt.dCode(wjlj);//解密后的文件路径
				if(".xlsx".equals(wjmhz)) {
					creatXSSFWorkbook(wjlj_d,sjyzjgDto);
				}else if(".xls".equals(wjmhz)) {
					creatHSSFWorkbook(wjlj_d,sjyzjgDto);
				}
			}
		}
		return true;
	}

	@Override
	public Integer getUnFinishedReportCount() {
		return dao.getUnFinishedReportCount();
	}
	@Override
	public List<Map<String, Object>> getUnFinishedReportList() {
		return dao.getUnFinishedReportList();
	}
}
