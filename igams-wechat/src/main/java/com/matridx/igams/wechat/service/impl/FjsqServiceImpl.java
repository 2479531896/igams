package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.post.IShgcDao;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DetectionTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.security.IgamsGrantedAuthority;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.wechat.dao.entities.*;
import com.matridx.igams.wechat.dao.post.IFjsqDao;
import com.matridx.igams.wechat.dao.post.ISjxxDao;
import com.matridx.igams.wechat.service.svcinterface.*;
import com.matridx.igams.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FjsqServiceImpl extends BaseBasicServiceImpl<FjsqDto, FjsqModel, IFjsqDao> implements IFjsqService,IAuditService{

	@Autowired
	private ISjjcxmService sjjcxmService;
	@Autowired
	DingTalkUtil talkUtil;
	
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	IShgcService shgcService;
	
	@Autowired
	ICommonService commonservice;
	
	@Autowired
	IJcsjService jcsjService;
	
	@Autowired
	IDdxxglService ddxxglService;
	
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.wechat.externalurl:}")
	private String externalurl;
	
	@Autowired
	IShgcDao shgcDao;
	
	@Autowired
	IHbqxService hbqxService;
	
	@Autowired
	ISjxxDao sjxxDao;
	
	@Autowired
	IXxglService xxglservice;

	@Autowired
	IFjsqyyService fjsqyyService;
	// 微信通知标本状态的模板ID
	@Value("${matridx.wechat.ybzt_templateid:}")
	private String ybzt_templateid = null;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	@Autowired
	ISjxxCommonService sjxxCommonService;
	@Autowired
	IHbsfbzService hbsfbzService;
	private Logger log = LoggerFactory.getLogger(FjsqServiceImpl.class);
	
	@Autowired
	private ISjybztService sjybztService;
	@Autowired
	ICommonService commonService;
//	@Value("${matridx.wechat.registerurl:}")
//	String registerurl;
//	@Value("${matridx.prefix.urlprefix:}")
//	String urlPrefix;
	@Autowired
	IDdspglService ddspglService;
	@Autowired
	IDdfbsglService ddfbsglService;
	@Autowired
	IShlcService shlcService;
	@Autowired
	IShxxService shxxService;

	@Autowired
	ISjsyglService sjsyglService;

	@Autowired
	IXmsyglService xmsyglService;
//	@Autowired(required=false)
//	AmqpTemplate amqpTempl;
	@Autowired
	RedisUtil redisUtil;
	
	/**
	 * 新增复检申请
	 */
	@Override
	public boolean insertFjsq(FjsqDto fjsqDto){
		// TODO Auto-generated method stub
		int result=dao.insert(fjsqDto);
        return result != 0;
    }

	@Override
	public int updateListDzInfo(List<FjsqDto> list) {
		return dao.updateListDzInfo(list);
	}

	/**
	 * 修改复检信息
	 * @param fjsqDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateJcdw(FjsqDto fjsqDto,String yjcdw) {
		boolean result=dao.update(fjsqDto)!=0;
		if (!result)
			return false;
		if (StringUtil.isBlank(yjcdw))
			return true;
		FjsqDto dtoById = dao.getDtoById(fjsqDto.getFjid());

		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(dtoById.getSjid());
		sjxxDto = sjxxDao.getDto(sjxxDto);
		
		//修改送检实验管理数据和项目实验管理数据
		addOrUpdateSyData(dtoById,sjxxDto);
		
		return true;
	}

	/**
	 * 修改复检信息
	 * @param fjsqDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateFjsq(FjsqDto fjsqDto){
		FjsqDto fjsqDto1=dao.getDtoById(fjsqDto.getFjid());
		SjxxDto sjxxDto = null;
		List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> fjlxlist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.RECHECK.getCode());
		JcsjDto jcsj_jcxm = new JcsjDto();
		String shlx="";
		if(list!=null&&list.size()>0){
			for(JcsjDto jcsjDto:list){
				if(jcsjDto.getCsid().equals(fjsqDto.getJcxm())){
					jcsj_jcxm = jcsjDto;
					if("F".equals(jcsjDto.getCskz1())){
						fjsqDto.setShlx(AuditTypeEnum.AUDIT_RFS_FC.getCode());
					}
					break;
				}
			}
		}

		//当复测加测项目为RFS项目时走RFS复测审核流程
		if("F".equals(jcsj_jcxm.getCskz1())){
			shlx=AuditTypeEnum.AUDIT_RFS_FC.getCode();
		}else{
			for(JcsjDto dto:fjlxlist){
				if(dto.getCsid().equals(fjsqDto.getLx())){
					if("RECHECK".equals(dto.getCsdm())){
						shlx=AuditTypeEnum.AUDIT_RECHECK.getCode();
					}else if("ADDDETECT".equals(dto.getCsdm())){
						shlx=AuditTypeEnum.AUDIT_ADDCHECK.getCode();
					}else if("REM".equals(dto.getCsdm())){
						shlx=AuditTypeEnum.AUDIT_ADDCHECK_REM.getCode();
					}else if("LAB_RECHECK".equals(dto.getCsdm())){
						shlx=AuditTypeEnum.AUDIT_LAB_RECHECK.getCode();
					}else if("LAB_ADDDETECT".equals(dto.getCsdm())){
						shlx=AuditTypeEnum.AUDIT_LAB_ADDCHECK.getCode();
					}else if("LAB_REM".equals(dto.getCsdm())){
						shlx=AuditTypeEnum.AUDIT_LAB_ADDCHECK_REM.getCode();
					}
					break;
				}
			}
		}
//		try {
//			checkCanFj(fjsqDto,shlx,jcsj_jcxm,fjsqDto.getFjlxdm(),fjlxlist);
//		} catch (BusinessException e) {
//			return false;
//		}
		if(!fjsqDto1.getJcxm().equals(fjsqDto.getJcxm()) &&"ADDDETECT".equals(fjsqDto.getFjlxdm())){
			sjxxDto=sjxxDao.getDtoById(fjsqDto.getSjid());
			if(sjxxDto!=null&&StringUtil.isNotBlank(fjsqDto.getSfff())&&"1".equals(fjsqDto.getSfff())&&StringUtil.isNotBlank(sjxxDto.getDb())){
				HbsfbzDto hbsfbzDto=new HbsfbzDto();
				hbsfbzDto.setHbmc(sjxxDto.getDb());
				List<HbsfbzDto> hbsfbzDtos = hbsfbzService.getDtoList(hbsfbzDto);
				if(hbsfbzDtos!=null&&hbsfbzDtos.size()>0){
					for(HbsfbzDto dto:hbsfbzDtos){
						if(fjsqDto.getJcxm().equals(dto.getXm())){
							if(StringUtil.isNotBlank(fjsqDto.getJczxm())){
								if(fjsqDto.getJczxm().equals(dto.getZxm())){
									fjsqDto.setYfje(dto.getSfbz());
									break;
								}
							}else{
								fjsqDto.setYfje(dto.getSfbz());
								break;
							}
						}
					}
				}
			}
		}
		if(StringUtil.isNotBlank(fjsqDto.getSfje())){
			BigDecimal sfje=new BigDecimal(fjsqDto.getSfje());
			if(sfje.compareTo(BigDecimal.ZERO) == 1){
				fjsqDto.setFkbj("1");
			}else{
				fjsqDto.setFkbj("0");
			}
		}else{
			fjsqDto.setFkbj("0");
		}
		int updatefjsq=dao.update(fjsqDto);
		if(updatefjsq<=0)
			return false;
		//更新复检原因，先删除，后新增
		if(fjsqDto.getYys()!=null && fjsqDto.getYys().length>0){
			FjsqyyDto fjsqyyDto=new FjsqyyDto();
			fjsqyyDto.setFjid(fjsqDto.getFjid());
			fjsqyyService.delete(fjsqyyDto);//删除
			List<FjsqyyDto> fjsqyyList=new ArrayList<>();
			for(int i=0;i<fjsqDto.getYys().length;i++){
				FjsqyyDto t_fjsqyyDto=new FjsqyyDto();
				t_fjsqyyDto.setFjid(fjsqDto.getFjid());
				t_fjsqyyDto.setYy(fjsqDto.getYys()[i]);
				fjsqyyList.add(t_fjsqyyDto);
			}
			fjsqyyService.addDtoList(fjsqyyList);
		}
		//如果前面没有查询，则这个地方重新查询，否则不再重复执行
		if(sjxxDto == null) {
			SjxxDto tt_sjxxDto=new SjxxDto();
			tt_sjxxDto.setSjid(fjsqDto.getSjid());
			sjxxDto = sjxxDao.getDto(tt_sjxxDto);
		}
		
		addOrUpdateSyData(fjsqDto,sjxxDto);
		return true;
	}
	
	/**
	 * 根据页面传递的送检实验数据，结合原有的数据，原本就存在的检测类型数据，则进行更新，没有的数据则进行新增
	 * 涉及 项目实验管理表，送检实验管理表
	 * @param fjsqDto
	 * @param sjxxDto
	 * @return
	 */
	public boolean addOrUpdateSyData(FjsqDto fjsqDto,SjxxDto sjxxDto) {
		//为防止未设置id，删除数据太多
		if( fjsqDto==null || StringUtil.isBlank(fjsqDto.getFjid()) || StringUtil.isBlank(fjsqDto.getSjid()))
			return false;
		
		//先确认该复检是否已经存在做过实验的，如果已经有实验日期，则不更新  实验相关数据
		FjsqDto fjsqDto_t = new FjsqDto();
		fjsqDto_t.setFjid(fjsqDto.getFjid());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		String nowDate=sdf.format(date);
		fjsqDto_t.setDsyrq(nowDate);
		List<FjsqDto> resultFjDtos = dao.getSyxxByFj(fjsqDto_t);
		//指定复检ID存在已经超过时限的实验数据，则直接返回不进行更新
		if(resultFjDtos != null && resultFjDtos.size() > 0)
			return false;
					
		List<JcsjDto> yblxList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
		List<JcsjDto> jcdwList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		List<JcsjDto> sjqfList=redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.INSPECTION_DIVISION.getCode());
		String yblxdm="";
		String jcdwmc="";
		String sjqfdm="";
		//判断是否已有内部编码，如果有取内部编码最后一位
		if(StringUtil.isNotBlank(sjxxDto.getNbbm())){
			String nbbm=sjxxDto.getNbbm();
			yblxdm=nbbm.substring(nbbm.length()-1);
		}else{
			if(yblxList!=null&&yblxList.size()>0){
				for(JcsjDto jcsjDto:yblxList){
					if(sjxxDto.getYblx().equals(jcsjDto.getCsid())){
						yblxdm=jcsjDto.getCsdm();
						break;
					}
				}
				//判断如果样本类型选择其他时，并且其他标本类型内容为"全血"或者"血浆"时，标本类型传"B"
				// 20240904 "其它"样本类型的csdm由XXX改为G
				if("XXX".equals(yblxdm) || "G".equals(yblxdm)){
					if(StringUtil.isNotBlank(sjxxDto.getYblxmc())){
						if("全血".equals(sjxxDto.getYblxmc())||"血浆".equals(sjxxDto.getYblxmc())){
							yblxdm="B";
						}
					}
				}
			}
		}
		if(jcdwList!=null&&jcdwList.size()>0){
			for(JcsjDto jcsjDto:jcdwList){
				if(fjsqDto.getJcdw().equals(jcsjDto.getCsid())){
					jcdwmc=jcsjDto.getCsmc();
					break;
				}
			}
		}
		if(sjqfList!=null&&sjqfList.size()>0){
			for(JcsjDto jcsjDto:sjqfList){
				if(sjxxDto.getSjqf().equals(jcsjDto.getCsid())){
					sjqfdm=jcsjDto.getCsdm();
					break;
				}
			}
		}
		fjsqDto.setYblxdm(yblxdm);
		fjsqDto.setJcdwmc(jcdwmc);
		fjsqDto.setSjqfdm(sjqfdm);
		
		//筛选实验数据的时候需要去除已经出过报告的送检信息，以及已经过时效的复检加测信息
		//清除之前的数据，重新设置sjid查询所有复检信息
		fjsqDto_t.setFjid(null);
		fjsqDto_t.setSjid(fjsqDto.getSjid());
		resultFjDtos = dao.getSyxxByFj(fjsqDto_t);
		if(resultFjDtos!=null && resultFjDtos.size() > 0) {
			List<String> ids = new ArrayList<String>();
			for(FjsqDto res_FjsqDto:resultFjDtos) {
				ids.add(res_FjsqDto.getFjid());
			}
			fjsqDto.setIds(ids);
		}
		
		List<SjsyglDto> insertInfo =sjsyglService.getDetectionInfo(null,fjsqDto,DetectionTypeEnum.DETECT_FJ.getCode());

		//为防止已有的 sjsygl 数据未处理（因为更改类型或者项目造成数据没有关联出来的情况)需要取一下当前ywid的实验数据
		SjsyglDto sjsyglDto_t=new SjsyglDto();
		sjsyglDto_t.setYwid(fjsqDto.getFjid());
		List<SjsyglModel> beforSy_list = sjsyglService.getModelList(sjsyglDto_t);
		
		XmsyglDto xmsyglDto_t = new XmsyglDto();
		xmsyglDto_t.setYwid(fjsqDto.getFjid());
		xmsyglDto_t.setScry(fjsqDto.getXgry());
		xmsyglService.deleteInfo(xmsyglDto_t);
		xmsyglService.delInfo(xmsyglDto_t);
		if (null != insertInfo && insertInfo.size()>0){
			List<SjsyglDto> insertList =  new ArrayList<>();
			List<SjsyglDto> updateList=new ArrayList<>();
			List<XmsyglDto> insertXmsyDtos = new ArrayList<>();
			List<XmsyglDto> updateXmsyDtos = new ArrayList<>();
			List<String> ids = new ArrayList<>();
			for (SjsyglDto dto : insertInfo) {
				if(!dto.getJcdw().equals(fjsqDto.getJcdw()) && StringUtil.isBlank(dto.getSyglid())&& StringUtil.isNotBlank(fjsqDto.getJcdw())) {
					dto.setJcdw(fjsqDto.getJcdw());
				}else if(StringUtil.isBlank(dto.getJcdw())) {
					dto.setJcdw(fjsqDto.getJcdw());
				}
				if(StringUtil.isNotBlank(dto.getSyglid()))
					ids.add(dto.getSyglid());
			}
			if(beforSy_list != null && beforSy_list.size() > 0) {
				for(SjsyglModel sjsy:beforSy_list) {
					boolean isFindsy = false;
					for(String id:ids) {
						if(id.equals(sjsy.getSyglid())) {
							isFindsy = true;
							break;
						}
					}
					if(!isFindsy) {
						ids.add(sjsy.getSyglid());
					}
				}
			}
			
			sjsyglDto_t=new SjsyglDto();
			sjsyglDto_t.setIds(ids);
			//先保存原有数据
			List<SjsyglModel> befor_list = new ArrayList<>();
			if(ids.size() > 0) {
				befor_list = sjsyglService.getModelList(sjsyglDto_t);
			}
			
			sjsyglDto_t.setSjid(fjsqDto.getSjid());
			//sjsyglDto_t.setLx(DetectionTypeEnum.DETECT_FJ.getCode());
			sjsyglDto_t.setScry(fjsqDto.getXgry());
			//为防止对已有的数据的影响，所以先只删除标记为1的复检的数据
			if(StringUtil.isNotBlank(sjsyglDto_t.getSjid()))
				sjsyglService.deleteInfo(sjsyglDto_t);
			if(ids.size() > 0) {
				//对有关联的所有数据先打上删除标记
				sjsyglService.delInfo(sjsyglDto_t);
			}
				
			//list 通过检测单位分组
			Map<String, List<SjsyglDto>> map = insertInfo.stream().collect(Collectors.groupingBy(SjsyglDto::getJcdw));
			if (!CollectionUtils.isEmpty(map)){
				Iterator<Map.Entry<String, List<SjsyglDto>>> entries = map.entrySet().iterator();
				while (entries.hasNext()) {
					Map.Entry<String,  List<SjsyglDto>> entry = entries.next();
					List<SjsyglDto> resultModelList = entry.getValue();
					String jcdw = entry.getKey();
					if (StringUtil.isNotBlank(jcdw) && !CollectionUtils.isEmpty(resultModelList)){
						//在通过检测类型分组
						Map<String, List<SjsyglDto>> listMap = resultModelList.stream().collect(Collectors.groupingBy(SjsyglDto::getJclxid));
						if (!CollectionUtils.isEmpty(listMap)){
							Iterator<Map.Entry<String, List<SjsyglDto>>> entryIterator = listMap.entrySet().iterator();
							while (entryIterator.hasNext()) {
								Map.Entry<String,  List<SjsyglDto>> stringListEntry = entryIterator.next();
								String jclx = stringListEntry.getKey();
								List<SjsyglDto> sjsyglDtoList = stringListEntry.getValue();
								if (StringUtil.isNotBlank(jclx) && !CollectionUtils.isEmpty(sjsyglDtoList)){
									sjsyglDtoList=sjsyglDtoList.stream().sorted(Comparator.comparing(SjsyglDto::getPx)).collect(Collectors.toList());
									boolean flag = true;
									//用修改之前的实验数据和修改后的对比如果检测单位和检测类型都相等则不用新增，是修改
									for (SjsyglModel sjsyglModel : befor_list) {
										if (jcdw.equals(sjsyglModel.getJcdw()) && jclx.equals(sjsyglModel.getJclxid())){
											SjsyglDto sjsy_dto = new SjsyglDto();
											sjsy_dto.setSyglid(sjsyglModel.getSyglid());
											sjsy_dto.setScbj("0");
											sjsy_dto.setXgry(fjsqDto.getXgry());
											//当原是在福州进行检验，然后复测时改回到杭州，此处会造成单位重新改回到送检信息的检出单位 福州 2024-04-22
											/*if(!sjsyglModel.getJcdw().equals(sjxxDto.getJcdw())){
												sjsy_dto.setJcdw(sjxxDto.getJcdw());
											}*/
											String jcxmmc = "";
											String wksxbm = "";
											for (SjsyglDto sjsyglDto : sjsyglDtoList) {
												//判断项目实验明细是否存在  存在修改 不存在新增
												if(jcxmmc.indexOf(sjsyglDto.getJcxmmc())==-1) {
													jcxmmc += ","+sjsyglDto.getJcxmmc();
													wksxbm += ","+sjsyglDto.getWksxbm();
												}
												//同一个项目但不同的对应ID（检测单位和标本类型不一样所造成），如果检测类型xmsygl表的数据应该重新创建
												if(sjsyglDto.getYwid().equals(fjsqDto.getFjid())) {
													if (StringUtil.isNotBlank(sjsyglDto.getXmsyglid())){
														XmsyglDto xmsyglDto = new XmsyglDto();
														xmsyglDto.setXmsyglid(sjsyglDto.getXmsyglid());
														xmsyglDto.setXgry(fjsqDto.getXgry());
														xmsyglDto.setYwid(sjsyglDto.getYwid());
														xmsyglDto.setSyglid(sjsyglModel.getSyglid());
														updateXmsyDtos.add(xmsyglDto);
													}else{
														XmsyglDto xmsyglDto = new XmsyglDto();
														xmsyglDto.setXmsyglid(StringUtil.generateUUID());
														xmsyglDto.setSyglid(sjsyglModel.getSyglid());
														xmsyglDto.setLrry(fjsqDto.getXgry());
														xmsyglDto.setWkdm(sjsyglDto.getNbzbm());
														xmsyglDto.setDyid(sjsyglDto.getDyid());
														xmsyglDto.setYwlx(DetectionTypeEnum.DETECT_FJ.getCode());
														xmsyglDto.setJcxmid(sjsyglDto.getJcxmid());
														xmsyglDto.setJczxmid(sjsyglDto.getJczxmid());
														xmsyglDto.setYwid(sjsyglDto.getYwid());
														xmsyglDto.setJclxid(sjsyglDto.getJclxid());
														insertXmsyDtos.add(xmsyglDto);
													}
												}
											}
											//在先录入DNA，然后加测RNA，后又修改送检信息未D+R，则数据不需要跟加测分开
											sjsy_dto.setDyid(sjsyglDtoList.get(0).getDyid());
											sjsy_dto.setJcxmid(sjsyglDtoList.get(0).getJcxmid());
											sjsy_dto.setJczxmid(sjsyglDtoList.get(0).getJczxmid());
											sjsy_dto.setNbzbm(sjsyglDtoList.get(0).getNbzbm());
											sjsy_dto.setWksxbm(wksxbm.length() > 0?wksxbm.substring(1):"");
											sjsy_dto.setYwid(sjsyglDtoList.get(0).getYwid());
											sjsy_dto.setSjid(sjsyglDtoList.get(0).getSjid());
											sjsy_dto.setXmmc(jcxmmc.length() > 0?jcxmmc.substring(1):"");
											updateList.add(sjsy_dto);
											flag = false;
											break;
										}
									}
									if (flag){
										String syglid = StringUtil.generateUUID();
										sjsyglDtoList.get(0).setSyglid(syglid);
										sjsyglDtoList.get(0).setYwlx(DetectionTypeEnum.DETECT_FJ.getCode());
										String jcxmmc = "";
										String wksxbm = "";
										for (SjsyglDto sjsyglDto : sjsyglDtoList) {
											if(jcxmmc.indexOf(sjsyglDto.getJcxmmc())==-1) {
												jcxmmc += ","+sjsyglDto.getJcxmmc();
												wksxbm += ","+sjsyglDto.getWksxbm();
											}
											if(sjsyglDto.getYwid().equals(fjsqDto.getFjid())) {
												XmsyglDto xmsyglDto = new XmsyglDto();
												xmsyglDto.setXmsyglid(StringUtil.generateUUID());
												xmsyglDto.setSyglid(syglid);
												xmsyglDto.setWkdm(sjsyglDto.getNbzbm());
												xmsyglDto.setDyid(sjsyglDto.getDyid());
												xmsyglDto.setYwlx(DetectionTypeEnum.DETECT_FJ.getCode());
												xmsyglDto.setJcxmid(sjsyglDto.getJcxmid());
												xmsyglDto.setJczxmid(sjsyglDto.getJczxmid());
												xmsyglDto.setYwid(sjsyglDto.getYwid());
												xmsyglDto.setLrry(fjsqDto.getXgry());
												xmsyglDto.setJclxid(sjsyglDto.getJclxid());
												insertXmsyDtos.add(xmsyglDto);
											}
										}
										if(jcdw.equals(sjxxDto.getJcdw())){
											sjsyglDtoList.get(0).setJcdw(sjxxDto.getJcdw());
											if("1".equals(sjxxDto.getSfjs())){
												sjsyglDtoList.get(0).setSfjs(sjxxDto.getSfjs());
												sjsyglDtoList.get(0).setJsrq(sjxxDto.getJsrq());
												sjsyglDtoList.get(0).setJsry(sjxxDto.getJsry());
											}
										}
										sjsyglDtoList.get(0).setXgry(fjsqDto.getXgry());
										sjsyglDtoList.get(0).setXmmc(jcxmmc.length() > 0?jcxmmc.substring(1):"");
										sjsyglDtoList.get(0).setWksxbm(wksxbm.length() > 0?wksxbm.substring(1):"");
										insertList.add(sjsyglDtoList.get(0));
									}
								}
							}
						}
					}
				}
			}
			if(updateList!=null&&updateList.size() > 0){
				sjsyglService.modAllList(updateList);
			}
			if(insertList!=null&&insertList.size() > 0){
				sjsyglService.insertList(insertList);
			}
			if (!CollectionUtils.isEmpty(updateXmsyDtos)){
				xmsyglService.modToNormal(updateXmsyDtos);
			}
			if (!CollectionUtils.isEmpty(insertXmsyDtos)) {
				xmsyglService.insertList(insertXmsyDtos);
				//因为存在送检和加测同时提交的情况，这个时候加测的 sjsygl 表里的是否接收 字段为空，所以需要更新
				if(insertList!=null&&insertList.size() > 0) {
					sjsyglService.updateFjJsrq(insertList);
				}
			}
		}
		return true;
	}

	@Override
	public boolean updateFjsjByFjid(FjsqDto fjsqDto) {
		return dao.updateFjsjByFjid(fjsqDto);
	}

	/**
	 * 复检列表
	 * @param fjsqDto
	 * @return
	 */
	public List<FjsqDto> getPagedDtoList(FjsqDto fjsqDto){
		List<FjsqDto> t_List=dao.getPagedDtoList(fjsqDto);
		try{
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_RECHECK.getCode(), "zt", "fjid", new String[]{
				StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_RFS_FC.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_OUT_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_DING_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_ADDCHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_ADDCHECK_REM.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_LAB_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_LAB_ADDCHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_LAB_ADDCHECK_REM.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t_List;
	}

    /**
	 * 根据标本编号获取信息
	 */
	public FjsqDto getDtoByYbbh(FjsqDto fjsqDto){
		return dao.getDtoByYbbh(fjsqDto);
	}
	
	/**
	 *复检申请审核列表
	 */
	@Override
	public List<FjsqDto> getPagedAuditRecheck(FjsqDto fjsqDto){
		// TODO Auto-generated method stub
		List<FjsqDto> t_fjsqList=dao.getPagedAuditRecheck(fjsqDto);
		if(t_fjsqList==null || t_fjsqList.size()==0) 
			return t_fjsqList;
		
		List<FjsqDto> sqList=dao.getAuditListRecheck(t_fjsqList);
		commonservice.setSqrxm(sqList);
		jcsjService.handleCodeToValue(sqList, new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE}, new String[] {"jcxm:jcxmmc"});
		
		return sqList;
	}

	@Override
	public List<FjsqDto> getInfoList(FjsqDto fjsqDto) {
		return dao.getInfoList(fjsqDto);
	}

	@Override
	public Boolean updateList(List<FjsqDto> list) {
		return dao.updateList(list);
	}

	@Override
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException{
		// TODO Auto-generated method stub
		FjsqDto fjsqDto = (FjsqDto) baseModel;
		fjsqDto.setXgry(operator.getYhid());
		/*SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(fjsqDto.getSjid());
		sjxxDto.setSyrq(fjsqDto.getSyrq());
		sjxxDto.setDsyrq(fjsqDto.getDsyrq());
		sjxxDto.setQtsyrq(fjsqDto.getQtsyrq());
		sjxxDto.setXgry(operator.getYhid());
		boolean result=updateFjsq(fjsqDto,sjxxDto);*/
		return update(fjsqDto);
	}

	private boolean sendVerificationMessage(FjsqDto fjsqDto,ShgcDto shgcDto,User operator) {
		// TODO Auto-generated method stub
		// 如果没有设置URL，则直接返回，不发送消息。此为内容修改使用
		if (StringUtil.isBlank(menuurl))
			return true;
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(fjsqDto.getSjid());
		Map<String, Object> map= new HashMap<>();
		String ICOMM_SH00026=xxglservice.getModelById("ICOMM_SH00026").getXxnr();
		String ICOMM_SH00036=xxglservice.getModelById("ICOMM_SH00036").getXxnr();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		String nowDate=sdf.format(date);
		String remarkx =StringUtil.replaceMsg(ICOMM_SH00036, operator.getZsxm(), fjsqDto.getHzxm(), shgcDto.getShlbmc(), fjsqDto.getLxmc(), shgcDto.getShxx_shyj(), DateUtils.getCustomFomratCurrentDate("YYYY-MM-dd HH:mm:ss"));
		try {
			map.put("templateid", ybzt_templateid);
			map.put("yxbt", ICOMM_SH00026);
			map.put("wxbt", ICOMM_SH00026);
			map.put("ddbt", ICOMM_SH00026);
			map.put("yxnr", remarkx);
			map.put("keyword2", nowDate);
			map.put("keyword1", remarkx);
			map.put("keyword3", fjsqDto.getHzxm()+"-"+fjsqDto.getLxmc());
			map.put("remark", remarkx);
			map.put("ddnr",remarkx);
			map.put("xxmb","TEMPLATE_EXCEPTION");
			String reporturl = menuurl +"/wechat/statistics/wxRemark?remark="+remarkx;

			map.put("reporturl",reporturl);
			sjxxCommonService.sendMessage(sjxxDto,map);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			log.error("复检审核未通过，发送用户通知出错！"+e.getMessage());
		}
		return true;
	}

	@Override
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException{
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}
		for (ShgcDto shgcDto :shgcList){
			FjsqDto fjsqDto=new FjsqDto();
			fjsqDto.setFjid(shgcDto.getYwid());
			fjsqDto.setXgry(operator.getYhid());
			FjsqDto  t_fjsqDto=dao.getDto(fjsqDto);
			boolean hbMessageFlag = commonService.queryAuthMessage(t_fjsqDto.getHbid(),"INFORM_HB00001");
			SjxxDto sjxxDto=new SjxxDto();
			sjxxDto.setSjid(t_fjsqDto.getSjid());
			sjxxDto = sjxxDao.getDto(sjxxDto);
			SjsyglDto sjsyglDto=new SjsyglDto();
			sjsyglDto.setYwid(t_fjsqDto.getFjid());
			sjsyglDto.setSfjs("1");
			sjsyglDto.setJsry(t_fjsqDto.getLrry());
			sjsyglDto.setJsrq(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			sjsyglDto.setXgry(operator.getYhid());
			if(auditParam!=null){
				if(auditParam.isPassOpe()) {
					if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())||AuditStateEnum.AUDITING.equals(shgcDto.getAuditState())) {
						SjybztDto sjybztDto =new SjybztDto();
						List<String> ybztcskz1s=new ArrayList<>();
						ybztcskz1s.add("S");
						ybztcskz1s.add("T");
						sjybztDto.setSjid(t_fjsqDto.getSjid());
						sjybztDto.setYbztCskz1s(ybztcskz1s);
//		        	审核时不提示量少仅一次   2021-03-30  modfiy  zhanghan 2021-04-03 废弃
						List<SjybztDto> ybztList=sjybztService.getDtoList(sjybztDto);
						if(ybztList!=null&& ybztList.size()>0) {
							for(int i=0;i<ybztList.size();i++){
								if(!StatusEnum.CHECK_NO.getCode().equals(t_fjsqDto.getZt())) {
									if("S".equals(ybztList.get(i).getYbztCskz1())){
										throw new BusinessException("msg","量仅一次！");
									}
									if (!t_fjsqDto.getJcxmcskz3().contains("REM") && "T".equals(ybztList.get(i).getYbztCskz1())) {//如果不是去人源，则提示量仅一次
										throw new BusinessException("msg", "仅可去人源！");
									}
								}
							}
						}
					}
				}
			}
			// 审核不通过
			if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
				try {
					fjsqDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
					String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
					String ICOMM_SH00036 = xxglService.getMsg("ICOMM_SH00036");
					if (shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0) {
						for (int i = 0; i < shgcDto.getSpgwcyDtos().size(); i++) {
							if (StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())) {
								String sign = URLEncoder.encode(commonservice.getSign(t_fjsqDto.getFjid()), "UTF-8");
								// 内网访问
								String internalbtn = applicationurl + "/ws/recheck/viewRecheck?fjid="
										+ t_fjsqDto.getFjid() + "&sign=" + sign;
								// 外网访问
								String external = externalurl + "/ws/recheck/viewRecheck?fjid=" + t_fjsqDto.getFjid()
										+ "&sign=" + sign;
								List<BtnJsonList> btnJsonLists = new ArrayList<>();
								BtnJsonList btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("内网访问");
								btnJsonList.setActionUrl(internalbtn);
								btnJsonLists.add(btnJsonList);
								btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("外网访问");
								btnJsonList.setActionUrl(external);
								btnJsonLists.add(btnJsonList);
								talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00026,
										StringUtil.replaceMsg(ICOMM_SH00036, operator.getZsxm(), t_fjsqDto.getHzxm(),
												shgcDto.getShlbmc(), t_fjsqDto.getYblxmc(), shgcDto.getShxx_shyj(),
												DateUtils.getCustomFomratCurrentDate("YYYY-MM-dd HH:mm:ss")),
										btnJsonLists, "1");
							}
						}
					}
					if(hbMessageFlag){
						sendVerificationMessage(t_fjsqDto,shgcDto,operator);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				sjsyglDto.setSfjs("0");
				sjsyglDto.setXgry(operator.getYhid());
				sjsyglService.updateSfjs(sjsyglDto);
				//审核通过
			}else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())){
				fjsqDto.setZt(StatusEnum.CHECK_PASS.getCode());
				String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
				String ICOMM_SH00016 = xxglService.getMsg("ICOMM_SH00016");
				//发送钉钉消息
				if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0){
					try{
						int size = shgcDto.getSpgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_SH00006,StringUtil.replaceMsg(ICOMM_SH00016,operator.getZsxm(),
										shgcDto.getShlbmc() ,t_fjsqDto.getLxmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				if(hbMessageFlag && "1".equals(t_fjsqDto.getBgbj())){
					//发送消息通知客户
					sendRecheckMessage(t_fjsqDto);
				}
				//修改送检实验管理数据和项目实验管理数据
				addOrUpdateSyData(t_fjsqDto,sjxxDto);
				sjsyglService.updateSfjs(sjsyglDto);
			}else {
				//新增提交审核
				fjsqDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
				String ICOMM_SH00025 = xxglService.getMsg("ICOMM_SH00025");
				String ICOMM_SH00022 = xxglService.getMsg("ICOMM_SH00022");
				String ICOMM_SH00021 = xxglService.getMsg("ICOMM_SH00021");
				String ICOMM_SH00004 = xxglService.getMsg("ICOMM_SH00004");
				String ICOMM_SH00005 = xxglService.getMsg("ICOMM_SH00005");
				//审核驳回
				String ICOMM_SH00037 = xxglService.getMsg("ICOMM_SH00037");
				String ICOMM_SH00038 = xxglService.getMsg("ICOMM_SH00038");
				if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0){
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
									String sign = URLEncoder.encode(commonservice.getSign(t_fjsqDto.getFjid()),"UTF-8");
									//内网访问
									String internalbtn = applicationurl+"/ws/auditProcess/auditPhone?shxx_shry="+yhid+"&shxx_shjs="+jsid+"&ywid="+t_fjsqDto.getFjid()+"&sign="+sign+
											"&xlcxh="+shgcDto.getXlcxh()+"&business_url="+"/recheck/modRecheck&ywzd=fjid&shlbmc="+shgcDto.getShlbmc();
									//外网访问
									String external=externalurl+"/ws/auditProcess/auditPhone?shxx_shry="+yhid+"&shxx_shjs="+jsid+"&ywid="+t_fjsqDto.getFjid()+"&sign="+sign+
											"&xlcxh="+shgcDto.getXlcxh()+"&business_url="+"/recheck/modRecheck&ywzd=fjid&shlbmc="+shgcDto.getShlbmc();
									List<BtnJsonList> btnJsonLists = new ArrayList<>();
									BtnJsonList btnJsonList = new BtnJsonList();
									btnJsonList.setTitle("内网访问");
									btnJsonList.setActionUrl(internalbtn);
									btnJsonLists.add(btnJsonList);
									btnJsonList = new BtnJsonList();
									btnJsonList.setTitle("外网访问");
									btnJsonList.setActionUrl(external);
									btnJsonLists.add(btnJsonList);
									if(shgcDto.isBack()) {
										talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),yhid,yhm, ddid,ICOMM_SH00037,StringUtil.replaceMsg(ICOMM_SH00038,operator.getZsxm(),t_fjsqDto.getLxmc(),
												t_fjsqDto.getHzxm(),t_fjsqDto.getYblxmc(),t_fjsqDto.getNbbm()+"("+t_fjsqDto.getYbbh()+")",shgcDto.getShxx()!=null?shgcDto.getShxx().getShyj():"",DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
									}else {
										talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),yhid,yhm, ddid,ICOMM_SH00003,StringUtil.replaceMsg(ICOMM_SH00025,operator.getZsxm(),shgcDto.getShlbmc(),t_fjsqDto.getLxmc(),
												t_fjsqDto.getHzxm(),t_fjsqDto.getYblxmc(),t_fjsqDto.getNbbm()!=null?t_fjsqDto.getNbbm():"-"+"("+t_fjsqDto.getYbbh()+")",DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
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
					jcsjDto.setCsdm("RECHECK_TYPE");
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
									talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),ddxxglDtos.get(i).getYhid(),ddxxglDtos.get(i).getYhid(), ddxxglDtos.get(i).getDdid(), ICOMM_SH00022, StringUtil.replaceMsg(ICOMM_SH00021,operator.getZsxm(),
											shgcDto.getShlbmc(),t_fjsqDto.getHzxm(),t_fjsqDto.getYblxmc(), t_fjsqDto.getLxmc(),t_fjsqDto.getBgbjmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
								} catch (UnsupportedEncodingException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
					}
				}
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
				//医学部复测审核通过后发送消息通知客户
				if(hbMessageFlag && "2".equals(shgcDto.getXlcxh()) && "1".equals(shgcDto.getShxx().getSftg()) && ("RECHECK".equals(t_fjsqDto.getFjlxdm()) || "REM".equals(t_fjsqDto.getFjlxdm())) && "1".equals(t_fjsqDto.getBgbj())){
					sendRecheckMessageSec(t_fjsqDto);
				}
				//修改送检实验管理数据和项目实验管理数据
				addOrUpdateSyData(t_fjsqDto,sjxxDto);
				sjsyglService.updateSfjs(sjsyglDto);
			}
			dao.updateZt(fjsqDto);
		}
		return true;
	}

	@Override
	public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException{
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}
//		String token = talkUtil.getToken();
		if(auditParam.isCancelOpe()) {
			//审核回调方法
			for(ShgcDto shgcDto : shgcList){
				String fjid = shgcDto.getYwid();
				FjsqDto fjsqDto = new 	FjsqDto();
				fjsqDto.setXgry(operator.getYhid());
				fjsqDto.setFjid(fjid);
				fjsqDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				FjsqDto t_fjsqDto=dao.getDtoById(shgcDto.getYwid());
				if(t_fjsqDto!=null){
					fjsqDto.setJczxm(t_fjsqDto.getJczxm());
				}
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
				FjsqDto t_fjsqDto=dao.getDtoById(shgcDto.getYwid());
				if(t_fjsqDto!=null){
					fjsqDto.setJczxm(t_fjsqDto.getJczxm());
				}
				dao.update(fjsqDto);
				SjsyglDto sjsyglDto=new SjsyglDto();
				sjsyglDto.setYwid(t_fjsqDto.getFjid());
				sjsyglDto.setSfjs("0");
				sjsyglDto.setXgry(operator.getYhid());
				sjsyglService.updateSfjs(sjsyglDto);
				//OA取消审批的同时组织钉钉审批
//				FjsqDto fjsqDto_t=dao.getDtoById(fjid);
//				if(fjsqDto_t!=null && StringUtils.isNotBlank(fjsqDto_t.getDdslid())) {
//					Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(token, fjsqDto_t.getDdslid(), "", operator.getDdid());
//					//若撤回成功将实例ID至为空
//					String success=String.valueOf(cancelResult.get("message"));
//					@SuppressWarnings("unchecked")
//					Map<String,Object> result_map=JSON.parseObject(success,Map.class);
//					Boolean bo1=new Boolean((boolean) result_map.get("success"));
//					if(Boolean.valueOf(bo1)==true)
//						dao.updateDdslidToNull(fjsqDto_t);
//				}
			}
		}
		return true;
	}

	/**
	 * 复检统计时，点击查看附件列表
	 * @param fjsqDto
	 * @return
	 */
	@Override
	public List<FjsqDto> getPagedStatisRecheck(FjsqDto fjsqDto){
		// TODO Auto-generated method stub
		return dao.getPagedStatisRecheck(fjsqDto);
	}
	
	/**
	 * 根据复检日期确认是否完成，如果存在复检，或者增加检测项目的，并且要发送报告的，则确认日期是否正确
	 * @param fjsqDto
	 * @return
	 */
	public List<FjsqDto> checkFjbgSfwc(FjsqDto fjsqDto){
		return dao.checkFjbgSfwc(fjsqDto);
	}
	
	/**
	 * 删除复检申请
	 * @param fjsqDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteFjsq(FjsqDto fjsqDto){
		// TODO Auto-generated method stub
		boolean result=delete(fjsqDto);
		List<String> ywids= new ArrayList<>();
		if(fjsqDto.getIds()!=null && fjsqDto.getIds().size()>0) {
			ywids=fjsqDto.getIds();
		}else if(fjsqDto.getFjid()!=null) {
			ywids.add(fjsqDto.getFjid());
		}
		if(ywids.size()>0) {
			shgcDao.deleteByYwids(ywids);
			XmsyglDto xmsyglDto=new XmsyglDto();
			xmsyglDto.setYwids(ywids);
			xmsyglDto.setScry(fjsqDto.getScry());
			List<XmsyglDto> xmsyglModels=xmsyglService.getDtoListByYwids(xmsyglDto);//获取要删除数据的syglid
			xmsyglService.deleteByYwids(xmsyglDto);

			List<String> scsyglids=new ArrayList<>();
			if(!CollectionUtils.isEmpty(xmsyglModels)){
				for( XmsyglModel xmsyglDto1:xmsyglModels){
					scsyglids.add(xmsyglDto1.getSyglid());
				}
				xmsyglDto.setIds(scsyglids);
				List<XmsyglDto> xmsyglDtos=xmsyglService.getDtoListBySyglids(xmsyglDto);
				//若通过以上scsyglids还能查到数据，说明这部分数据不能删除
				if(!CollectionUtils.isEmpty(xmsyglDtos)){
					for(int i=scsyglids.size()-1;i>=0;i--){
						for(XmsyglDto xmsyglDto1:xmsyglDtos){
							if(scsyglids.get(i).equals(xmsyglDto1.getSyglid()))
								scsyglids.remove(i);
						}
					}
					String syglid="";
					SjsyglDto sjsyglDto=new SjsyglDto();
					List<SjsyglDto> sjsyglDtoList=new ArrayList<>();
					for(int j=0;j<xmsyglDtos.size();j++){
						if(StringUtil.isNotBlank(syglid) && !syglid.equals(xmsyglDtos.get(j).getSyglid())){
							sjsyglDtoList.add(sjsyglDto);
							sjsyglDto=new SjsyglDto();
							sjsyglDto.setXmmc(xmsyglDtos.get(j).getJcxmmc());
						}else if(syglid.equals(xmsyglDtos.get(j).getSyglid())){
							sjsyglDto.setXmmc(sjsyglDto.getXmmc()+","+xmsyglDtos.get(j).getJcxmmc());
						}else{
							sjsyglDto.setXmmc(xmsyglDtos.get(j).getJcxmmc());
						}
						syglid=xmsyglDtos.get(j).getSyglid();
						if(j==xmsyglDtos.size()-1)
							sjsyglDtoList.add(sjsyglDto);
						sjsyglService.updateList(sjsyglDtoList);//更新项目名称
					}
				}
			}
			SjsyglDto sjsyglDto=new SjsyglDto();
			sjsyglDto.setScry(fjsqDto.getScry());
			if(!CollectionUtils.isEmpty(scsyglids)){//为了适应历史数据
				sjsyglDto.setIds(scsyglids);
				sjsyglService.deleteBySyglids(sjsyglDto);
			}
		}
		return result;
	}
	
    
    /**
     * 钉钉查看当天复检申请列表
     * @param fjsqDto
     * @return
     */
    @Override
    public List<FjsqDto> getListAuditPhone(FjsqDto fjsqDto){
        return dao.getListAuditPhone(fjsqDto);
    }
    
    /**
	 * 送检列表查看复检申请信息
	 * @param fjsqDto
	 * @return
	 */
	@Override
	public List<FjsqDto> getListBySjid(FjsqDto fjsqDto){
		// TODO Auto-generated method stub
		List<FjsqDto> t_List=dao.getListBySjid(fjsqDto);
		try{
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_RFS_FC.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_OUT_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_DING_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_ADDCHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_ADDCHECK_REM.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_LAB_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_LAB_ADDCHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.AUDIT_LAB_ADDCHECK_REM.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t_List;
	}

	/**
	 * 根据送检ID和复检类型查询复检Dto
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public FjsqDto getDtoZt(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getDtoZt(sjxxDto);
	}

	/**
	 * 修改审核状态
	 * @param fjsqDto
	 */
	@Override
	public boolean updateZt(FjsqDto fjsqDto) {
		// TODO Auto-generated method stub
		return dao.updateZt(fjsqDto);
	}


	/**
	 * 生信部获取最近的复检信息
	 * @param fjsqDto
	 * @return
	 */
	@Override
	public List<FjsqDto> getDtoListOrderLrsj(FjsqDto fjsqDto){
		return dao.getDtoListOrderLrsj(fjsqDto);
	}
	
	/**
	 * 选中导出
	 * 
	 * @param params
	 * @return
	 */
	public List<FjsqDto> getListForSelectExp(Map<String, Object> params)
	{
		FjsqDto fjsqDto = (FjsqDto) params.get("entryData");
		queryJoinFlagExport(params, fjsqDto);
        return dao.getListForSelectExp(fjsqDto);
	}
	
	/**
	 * 根据搜索条件获取导出条数
	 * 
	 * @param fjsqDto
	 * @param params
	 * @return
	 */
	public int getCountForSearchExp(FjsqDto fjsqDto,Map<String, Object> params) {
		User user=new User();
		user.setYhid(fjsqDto.getYhid());
		user=commonService.getUserInfoById(user);

		if(("1").equals(fjsqDto.getAuth_flag())) {
			List<String> hbids=hbqxService.getHbidByYhid(fjsqDto.getYhid());
			if(hbids!=null && hbids.size()>0) {
				fjsqDto.setHbids(hbids);
			}
		}
		List<Map<String, String>> jcdwList = sjxxDao.getJsjcdwByjsid(user.getDqjs());
		if (jcdwList != null && jcdwList.size() > 0) {
			if ("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				List<String> strList = new ArrayList<>();
				for (int i = 0; i < jcdwList.size(); i++) {
					if (jcdwList.get(i).get("jcdw") != null) {
						strList.add(jcdwList.get(i).get("jcdw"));
					}
				}
				if (strList != null && strList.size() > 0) {
					fjsqDto.setJcdwxz(strList);
				}
			}
		}
        return dao.getCountForSearchExp(fjsqDto);
	}
	
	/**
	 * 根据搜索条件分页获取导出信息
	 * @param params
	 * @return
	 */
	public List<FjsqDto> getListForSearchExp(Map<String,Object> params) {
		FjsqDto fjsqDto = (FjsqDto) params.get("entryData");
		User user = new User();
		user.setYhid(fjsqDto.getYhid());
		user = commonService.getUserInfoById(user);

		if (("1").equals(fjsqDto.getAuth_flag())) {
			List<String> hbids = hbqxService.getHbidByYhid(fjsqDto.getYhid());
			if (hbids != null && hbids.size() > 0) {
				fjsqDto.setHbids(hbids);
			}
		}
		List<Map<String, String>> jcdwList = sjxxDao.getJsjcdwByjsid(user.getDqjs());
		if (jcdwList != null && jcdwList.size() > 0) {
			if ("1".equals(jcdwList.get(0).get("dwxdbj"))) {
				List<String> strList = new ArrayList<>();
				for (int i = 0; i < jcdwList.size(); i++) {
					if (jcdwList.get(i).get("jcdw") != null) {
						strList.add(jcdwList.get(i).get("jcdw"));
					}
				}
				if (strList != null && strList.size() > 0) {
					fjsqDto.setJcdwxz(strList);
				}
			}
		}
		queryJoinFlagExport(params, fjsqDto);
		return dao.getListForSearchExp(fjsqDto);
	}
	
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String, Object> params, FjsqDto fjsqDto)
	{
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuffer sqlParam = new StringBuffer();
		for (DcszDto dcszDto : choseList)
		{
			
			if (dcszDto == null || dcszDto.getDczd() == null)
				 continue; 
		 	if(dcszDto.getDczd().equalsIgnoreCase("YBLXMC")) { 
		 		fjsqDto.setYblx_flg("Y");
		 	}else if(dcszDto.getDczd().equalsIgnoreCase("JCXMMC")) {
		 		fjsqDto.setJcxm_flg("Y"); 
		 	}else if(dcszDto.getDczd().equalsIgnoreCase("lRRYMC")) {
		 		fjsqDto.setLrry_flg("Y"); 
		 	}else if(dcszDto.getDczd().equalsIgnoreCase("LXMC")) {
		 		fjsqDto.setFjlx_flg("Y");
		 	}
			sqlParam.append(",");
			if (StringUtil.isNotBlank(dcszDto.getSqlzd()))
			{
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		fjsqDto.setSqlparam(sqlParam.toString());
	}

	/**
	 * 修改复检申请
	 * @param fjsqDto
	 * @param sjxxDto
	 * @return
	 */
	/*@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateFjsq(FjsqDto fjsqDto, SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		boolean result=update(fjsqDto);
		if(result) {
			if(StringUtil.isNotBlank(sjxxDto.getSyrq())|| StringUtil.isNotBlank(sjxxDto.getDsyrq())||StringUtil.isNotBlank(sjxxDto.getQtsyrq())) {
				sjxxService.updateSyrq(sjxxDto); 
			}
		}
		return result;
	}*/

	/**
	 * 获取未发送报告
	 * @param fjsqDto
	 * @return
	 */
	@Override
	public List<FjsqDto> queryFjbg(FjsqDto fjsqDto) {
		
		return dao.queryFjbg(fjsqDto);
	}
	
	/**
	 * 获取总数
	 * @param fjsqDto
	 * @return
	 */
	@Override
	public Integer querynum(FjsqDto fjsqDto) {
		return dao.querynum(fjsqDto);
	}
	
	/**
	 * 修改检测单位时发送信息通知相关人员
	 * @param fjsqDto
	 * @return
	 */
	@Override
	public void sendUpdateJcdwMessage(FjsqDto fjsqDto) {
		// TODO Auto-generated method stub
//		 String token = talkUtil.getToken();
		 List<FjsqDto> gwcyList=getSpgwcyList(fjsqDto);
		 if(gwcyList!=null && gwcyList.size()>0) {
			 FjsqDto fjsqDto_t=getDto(fjsqDto);
			 String ICOMM_SJ00002 = xxglService.getMsg("ICOMM_SJ00002");
			 String ICOMM_SH00043 = xxglService.getMsg("ICOMM_SH00043");
			 for (int i = 0; i < gwcyList.size(); i++) {
			 	if(StringUtil.isNotBlank(gwcyList.get(i).getDdid())){
					talkUtil.sendWorkMessage(gwcyList.get(i).getYhm(),gwcyList.get(i).getDdid(),ICOMM_SJ00002,
							StringUtil.replaceMsg(ICOMM_SH00043,fjsqDto_t.getHzxm(),fjsqDto_t.getNbbm()!=null?fjsqDto_t.getNbbm():"",fjsqDto_t.getJcxmmc(),fjsqDto_t.getLxmc(),fjsqDto_t.getJcdwmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
				}
			 }
		 }
	}

	/**
	 * 查询审批岗位钉钉
	 * @param fjsqDto
	 * @return
	 */
	@Override
	public List<FjsqDto> getSpgwcyList(FjsqDto fjsqDto) {
		// TODO Auto-generated method stub
        return dao.getSpgwcyList(fjsqDto);
	}
	
	/**
	 * 发送复检通知
	 * @param fjsqDto
	 * @return
	 */
	@Override
	public boolean sendRecheckMessage(FjsqDto fjsqDto) {
		// TODO Auto-generated method stub
		// 如果没有设置URL，则直接返回，不发送消息。此为内容修改使用
		if (StringUtil.isBlank(menuurl))
			return true;
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(fjsqDto.getSjid());
		Map<String, Object> map= new HashMap<>();
		String ICOMM_SH00045=StringUtil.replaceMsg(xxglservice.getModelById("ICOMM_SH00045").getXxnr(),fjsqDto.getHzxm(),fjsqDto.getYblxmc());
		String ICOMM_SH00046=StringUtil.replaceMsg(xxglservice.getModelById("ICOMM_SH00046").getXxnr(),fjsqDto.getHzxm(),fjsqDto.getYblxmc(),fjsqDto.getLxmc());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		String nowDate=sdf.format(date);
		try {
			map.put("templateid", ybzt_templateid);
			map.put("yxbt", ICOMM_SH00045);
			map.put("yxnr", ICOMM_SH00046);
			map.put("wxbt", ICOMM_SH00045);
			map.put("keyword2", nowDate);
			map.put("keyword1", ICOMM_SH00046);
			map.put("keyword3", fjsqDto.getHzxm()+"-"+fjsqDto.getYblxmc());
			map.put("ddbt", ICOMM_SH00045);
			map.put("ddnr", ICOMM_SH00046);
			map.put("xxmb","TEMPLATE_EXCEPTION");
			String reporturl = menuurl +"/wechat/statistics/wxRemark?remark="+ICOMM_SH00046;

			map.put("reporturl",reporturl);
			sjxxCommonService.sendMessage(sjxxDto,map);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 医学部审核完成后发送复检通知
	 * @param fjsqDto
	 * @return
	 */
	@Override
	public boolean sendRecheckMessageSec(FjsqDto fjsqDto) {
		// TODO Auto-generated method stub
		// 如果没有设置URL，则直接返回，不发送消息。此为内容修改使用
		if (StringUtil.isBlank(menuurl))
			return true;
		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setSjid(fjsqDto.getSjid());
		Map<String, Object> map= new HashMap<>();
		String ICOMM_SH00045=StringUtil.replaceMsg(xxglservice.getModelById("ICOMM_SH00045").getXxnr(),fjsqDto.getHzxm(),fjsqDto.getYblxmc());
		String xxnr=StringUtil.replaceMsg(xxglservice.getModelById("ICOMM_SH00047").getXxnr(),fjsqDto.getHzxm(),fjsqDto.getYblxmc(),fjsqDto.getJcxmmc());
		if("REM".equals(fjsqDto.getFjlxdm()))
			xxnr=StringUtil.replaceMsg(xxglservice.getModelById("ICOMM_SH00054").getXxnr(),fjsqDto.getHzxm(),fjsqDto.getYblxmc(),fjsqDto.getJcxmmc());
		if("F".equals(fjsqDto.getJcxmcskz()))
			xxnr=StringUtil.replaceMsg(xxglservice.getModelById("RFS_SH000004").getXxnr(),fjsqDto.getHzxm(),fjsqDto.getYblxmc(),fjsqDto.getJcxmmc());
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date date=new Date();
		String nowDate=sdf.format(date);
		try {
			map.put("templateid", ybzt_templateid);
			map.put("yxbt", ICOMM_SH00045);
			map.put("yxnr", xxnr);
			map.put("wxbt", ICOMM_SH00045);
			map.put("keyword2", nowDate);
			map.put("keyword1", xxnr);
			map.put("keyword3", fjsqDto.getHzxm()+"-"+fjsqDto.getYblxmc());
			map.put("ddbt", ICOMM_SH00045);
			map.put("ddnr", xxnr);
			map.put("xxmb","TEMPLATE_EXCEPTION");
			String reporturl = menuurl +"/wechat/statistics/wxRemark?remark="+xxnr;

			map.put("reporturl",reporturl);
			sjxxCommonService.sendMessage(sjxxDto,map);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 同步复检支付信息
	 * @param fjsqDto
	 */
	@Override
	public void modRecheckPayinfo(FjsqDto fjsqDto) {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(fjsqDto.getFjid())) {
			int result = dao.modRecheckPayinfo(fjsqDto);
			if(result == 0) 
				log.error("复检信息同步失败！");
			return;
		}
		log.error("未获取到复检ID！");
    }

	/**
	 * 点击实验按钮时先去查询出dna/rna/其他的实验日期和检测标记
	 * @param fjsqDto
	 * @return
	 */
	@Override
	public List<FjsqDto> getFjxxByfjids(FjsqDto fjsqDto) {
		// TODO Auto-generated method stub
		return dao.getFjxxByfjids(fjsqDto);
	}

	/**
	 * 点击实验按钮判断检查项目
	 */
	@Override
	public List<FjsqDto> checkJcxm(FjsqDto fjsqDto) {
		// TODO Auto-generated method stub
		return dao.checkJcxm(fjsqDto);
	}

	/**
	 * 实验按钮修改检测标记
	 * @param fjsqDto
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	public boolean updateJcbjByid(FjsqDto fjsqDto) throws BusinessException{
		// TODO Auto-generated method stub
		if (StringUtil.isBlank(fjsqDto.getJcbj()))
		{
			fjsqDto.setJcbj("0");
			fjsqDto.setSyrq(null);
		}
		if (StringUtil.isBlank(fjsqDto.getDjcbj()))
		{
			fjsqDto.setDjcbj("0");
			fjsqDto.setDsyrq(null);
		}
		if (StringUtil.isBlank(fjsqDto.getQtjcbj()))
		{
			fjsqDto.setQtjcbj("0");
			fjsqDto.setQtsyrq(null);
		}
		boolean result = dao.updateJcbjByid(fjsqDto);
		SjxxDto sjxxDto= new SjxxDto();
		sjxxDto.setAccess_token(fjsqDto.getAccess_token());
		sjxxDto.setDataFilterModel(fjsqDto.getDataFilterModel());
		sjxxDto.setDataPermissionModel(fjsqDto.getDataPermissionModel());
		sjxxDto.setJcbj(fjsqDto.getJcbj());
		sjxxDto.setDjcbj(fjsqDto.getDjcbj());
		sjxxDto.setQtjcbj(fjsqDto.getQtjcbj());
		sjxxDto.setSyrq(fjsqDto.getSyrq());
		sjxxDto.setDsyrq(fjsqDto.getDsyrq());
		sjxxDto.setQtsyrq(fjsqDto.getQtsyrq());
		sjxxDto.setJcdw(fjsqDto.getJcdw());
		sjxxDto.setSfsytz(fjsqDto.getSfsytz());
		sjxxDto.setXgry(fjsqDto.getXgry());
		sjxxDto.setIds(fjsqDto.getIds());//这里传入的是fjid,但下面的发送信息需要的是sjid,在循环中去拿到sjid
		//发送实验通知
		if(result && !"F".equals(fjsqDto.getCskz1())) {
			if("1".equals(sjxxDto.getSfsytz())) {
				SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
				Date date=new Date();
				String dqrq=formatter.format(date);//当前日期
				Calendar calendar = new GregorianCalendar();
				calendar.setTime(date);
				calendar.add(Calendar.DATE,1);
				date=calendar.getTime();
				String mtrq=formatter.format(date);//明天
				String ICOMM_SJ00048=xxglService.getMsg("ICOMM_SJ00048");
				
				for(int i=0;i<sjxxDto.getIds().size();i++) {
					String sjid = dao.getSjidFromFjid(sjxxDto.getIds().get(i));
					SjxxDto t_sjxxDto=sjxxDao.getDtoById(sjid);
					if(t_sjxxDto!=null) {
						boolean messageBoolean = commonService.queryAuthMessage(t_sjxxDto.getHbid(),"INFORM_HB00008");
						if(messageBoolean){
							String ICOMM_SJ00047=xxglService.getMsg("ICOMM_SJ00047", t_sjxxDto.getHzxm(),t_sjxxDto.getCsmc(),dqrq,mtrq);
							String keyword1 = DateUtils.getCustomFomratCurrentDate("HH:mm:ss");
							String keyword2 = t_sjxxDto.getYbbh();
							Map<String,Object> map= new HashMap<>();
							map.put("yxbt", ICOMM_SJ00048);
							map.put("yxnr", ICOMM_SJ00047);
							map.put("ddbt", ICOMM_SJ00048);
							map.put("ddnr", ICOMM_SJ00047);
							map.put("wxbt", ICOMM_SJ00048);
							map.put("remark", ICOMM_SJ00047);
							map.put("keyword2", keyword1);
							map.put("keyword1", keyword2);
							map.put("keyword3", map.put("keyword3", t_sjxxDto.getHzxm()+"-"+t_sjxxDto.getCsmc()));
							map.put("templateid", ybzt_templateid);
							map.put("xxmb","TEMPLATE_EXCEPTION");
							String reporturl = menuurl +"/wechat/statistics/wxRemark?remark="+ICOMM_SJ00047;

							map.put("reporturl",reporturl);
							sjxxCommonService.sendMessage(t_sjxxDto, map);
						}
					}
				}
			}
		}
		return result;
		
		
		
		// TODO Auto-generated method stub
//		return dao.updateJcbjByid(fjsqDto);
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) throws BusinessException {
		return null;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) throws BusinessException {
		Map<String, Object> map =new HashMap<>();
		@SuppressWarnings("unchecked")
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

	/**
	 * 获取用户登录信息
	 * @return
	 */
	protected User getLoginInfo(){
		//String name = request.getRemoteUser();
		// 获取安全上下文对象，就是那个保存在 ThreadLocal 里面的安全上下文对象
		// 总是不为null(如果不存在，则创建一个authentication属性为null的empty安全上下文对象)
		SecurityContext securityContext = SecurityContextHolder.getContext();

		// 获取当前认证了的 principal(当事人),或者 request token (令牌)
		// 如果没有认证，会是 null
		Authentication authentication = securityContext.getAuthentication();

		// 获取当事人信息对象，返回结果是 Object 类型，但实际上可以是应用程序自定义的带有更多应用相关信息的某个类型。
		// 很多情况下，该对象是 Spring Security 核心接口 UserDetails 的一个实现类，你可以把 UserDetails 想像
		// 成我们数据库中保存的一个用户信息到 SecurityContextHolder 中 Spring Security 需要的用户信息格式的
		// 一个适配器。
		@SuppressWarnings("unchecked")
		List<IgamsGrantedAuthority> authorities = (List<IgamsGrantedAuthority>)authentication.getAuthorities();

		IgamsGrantedAuthority authority = authorities.get(0);

		return authority.getYhxx();
	}

	/**
	 * 选中导出
	 *
	 * @param params
	 * @return
	 */
	public List<FjsqDto> getListForSelectExpAudit(Map<String, Object> params)
	{
		FjsqDto fjsqDto = (FjsqDto) params.get("entryData");
		queryJoinFlagExport(params, fjsqDto);
        return dao.getListForSelectExpAudit(fjsqDto);
	}


	/**
	 * 根据搜索条件获取审核导出条数
	 *
	 * @param fjsqDto
	 * @param params
	 * @return
	 */
	@Override
	public int getCountForSearchExpAudit(FjsqDto fjsqDto,Map<String, Object> params) {
		User user=getLoginInfo();
		fjsqDto.setYhid(user.getYhid());
		fjsqDto.setJsid(user.getDqjs());
		fjsqDto.setZt("10");
		return dao.getCountForSearchExpAudit(fjsqDto);
	}

	/**
	 * 根据搜索条件分页获取导出信息
	 * @param params
	 * @return
	 */
	public List<FjsqDto> getListForSearchExpAudit(Map<String,Object> params){
		FjsqDto fjsqDto = (FjsqDto)params.get("entryData");
		fjsqDto.setZt("10");
		queryJoinFlagExport(params,fjsqDto);
		return dao.getListForSearchExpAudit(fjsqDto);
	}
	
		@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean callbackRecheckAduit(JSONObject obj, HttpServletRequest request, Map<String,Object> t_map) throws BusinessException{
		FjsqDto fjsqDto=new FjsqDto();
		String result=obj.getString("result");//正常结束时result为agree，拒绝时result为refuse，审批终止时没这个值，redirect转交
		String type=obj.getString("type");//审批正常结束（同意或拒绝）的type为finish，审批终止的type为terminate
		String processInstanceId=obj.getString("processInstanceId");//审批实例id
		String staffId=obj.getString("staffId");//审批人钉钉ID
		String remark=obj.getString("remark");//审核意见
		String content = obj.getString("content");//评论
		String finishTime=obj.getString("finishTime");//审批完成时间
		String title= obj.getString("title");
		String processCode=obj.getString("processCode");
		String ddspbcbj=request.getParameter("ddspbcbj");
		String wbcxid=request.getParameter("wbcxid");
		log.error("回调参数获取---------result:"+result+",type:"+type+",processInstanceId:"+processInstanceId+",staffId:"+staffId+",remark:"+remark+",finishTime"+finishTime);
		//分布式服务器保存钉钉审批信息
		DdfbsglDto ddfbsglDto=new DdfbsglDto();
		ddfbsglDto.setProcessinstanceid(processInstanceId);
		ddfbsglDto=ddfbsglService.getDtoById(processInstanceId);
		DdspglDto ddspglDto;
		DdspglDto t_ddspglDto=new DdspglDto();
		t_ddspglDto.setProcessinstanceid(processInstanceId);
		t_ddspglDto.setType("finish");
		t_ddspglDto.setEventtype(DingTalkUtil.BPMS_TASK_CHANGE);
		List<DdspglDto> ddspgllist=ddspglService.getDtoList(t_ddspglDto);
		try {
//			int a=1/0;
			if(ddfbsglDto==null)
				throw new BusinessException("message","未获取到相应的钉钉分布式信息！");
			if("1".equals(ddspbcbj)) {
				ddspglDto=ddspglService.insertInfo(obj);
			}else {
				if(ddspgllist!=null && ddspgllist.size()>0) {
					ddspglDto=ddspgllist.get(0);
				}else{
					ddspglDto=ddspglService.insertInfo(obj);
				}
			}
			fjsqDto.setDdslid(processInstanceId);
			//根据钉钉审批实例ID查询
			fjsqDto=dao.getDtoByDdslid(fjsqDto);
			//若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
			if(fjsqDto!=null) {
				User t_user=new User();
				t_user.setDdid(staffId);
				t_user.setWbcxid(wbcxid);
				//获取审批人信息
				FjsqDto dd_sprxx=dao.getSprxxByDdid(t_user);
				if(dd_sprxx==null)
					return false;
				User user=new User();
				user.setYhid(dd_sprxx.getSprid());
				user.setZsxm(dd_sprxx.getSprxm());
				user.setYhm(dd_sprxx.getSpryhm());
				user.setDdid(dd_sprxx.getSprddid());
				//获取审批人角色信息
				List<FjsqDto> dd_sprjs=dao.getSprjsBySprid(dd_sprxx.getSprid());
				// 获取当前审核过程
				ShgcDto t_shgcDto = shgcService.getDtoByYwid(fjsqDto.getFjid());
				if(t_shgcDto!=null) {
					// 获取的审核流程列表
					ShlcDto shlcParam = new ShlcDto();
					shlcParam.setShid(t_shgcDto.getShid());
					shlcParam.setGcid(t_shgcDto.getGcid());// 处理旧流程判断用
					@SuppressWarnings("unused")
					List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);

					if (("start").equals(type)) {
						//更新分布式管理表信息
						DdfbsglDto t_ddfbsglDto=new DdfbsglDto();
						t_ddfbsglDto.setProcessinstanceid(processInstanceId);
						t_ddfbsglDto.setYwlx(processCode);
						t_ddfbsglDto.setYwmc(title);
						ddfbsglService.update(t_ddfbsglDto);
					}
					if(dd_sprjs!=null && dd_sprjs.size()>0) {
						//审批正常结束（同意或拒绝）
						ShxxDto shxxDto=new ShxxDto();
						shxxDto.setLcxh(t_shgcDto.getXlcxh());
						shxxDto.setShlb(t_shgcDto.getShlb());
						shxxDto.setShyj(remark);
						shxxDto.setYwid(fjsqDto.getFjid());
						shxxDto.setGcid(t_shgcDto.getGcid());
						if (StringUtil.isNotBlank(finishTime)){
							shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
						}
						String lastlcxh=null;//回退到指定流程

						if(("finish").equals(type)) {
							//如果审批通过,同意
							if(("agree").equals(result)) {
								log.error("同意------");
								shxxDto.setSftg("1");
								if(org.apache.commons.lang3.StringUtils.isBlank(t_shgcDto.getXlcxh()))
									throw new BusinessException("ICOM99019","现流程序号为空");
								lastlcxh=String.valueOf(Integer.parseInt(t_shgcDto.getXlcxh())+1);
							}
							//如果审批未通过，拒绝
							else if(("refuse").equals(result)) {
								log.error("拒绝------");
								shxxDto.setSftg("0");
								shxxDto.setThlcxh(null);
							}
							//如果审批被转发
							else if(("redirect").equals(result)) {
								String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(finishTime) / 1000));
								log.error("DingTalkMaterPurchaseAudit(钉钉加测审批转发提醒)------转发人员:"+dd_sprxx.getSprxm()+",人员ID:"+dd_sprxx.getSprxm()+",标本编号:"+fjsqDto.getYbbh()+",单据ID:"+fjsqDto.getFjid()+",转发时间:"+date);
								return true;
							}
							//调用审核方法
							Map<String, List<String>> map= ToolUtil.reformRequest(request);
							log.error("map:"+map);
							List<String> list= new ArrayList<>();
							list.add(fjsqDto.getFjid());
							map.put("fjid", list);
							//若一个用户多个角色导致审核异常时
							for(int i=0;i<dd_sprjs.size();i++) {
								try {
									//取下一个角色
									user.setDqjs(dd_sprjs.get(i).getSprjsid());
									user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
									shxxDto.setYwids(new ArrayList<>());
									if(("refuse").equals(result)){
										shgcService.terminateAudit(shxxDto, user,request,lastlcxh,obj);
									}else{
										shgcService.doManyBackAudit(shxxDto, user,request,lastlcxh,obj);
									}
									//更新审批管理信息
									ddspglDto.setCljg("1");
									ddspglService.updatecljg(ddspglDto);
									break;
								} catch (Exception e) {
									log.error("callbackRecheckAduit-Exception:" + e.getMessage());
									t_map.put("ddspglid", ddspglDto.getDdspglid());
									t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());

									if(i==dd_sprjs.size()-1)
										throw new BusinessException("ICOM99019",e.getMessage());
								}
							}
						}
						//撤销审批
						else if(("terminate").equals(type)) {
							shxxDto.setThlcxh(null);
							shxxDto.setYwglmc(fjsqDto.getYbbh());
							FjsqDto fjsqDto_t = new FjsqDto();
							dao.updateDdslidToNull(fjsqDto_t);
							log.error("撤销------");
							shxxDto.setSftg("0");
							//调用审核方法
							Map<String, List<String>> map=ToolUtil.reformRequest(request);
							List<String> list= new ArrayList<>();
							list.add(fjsqDto.getFjid());
							map.put("fjid", list);
							for(int i=0;i<dd_sprjs.size();i++) {
								try {
									//取下一个角色
									user.setDqjs(dd_sprjs.get(i).getSprjsid());
									user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
									shxxDto.setYwids(new ArrayList<>());
									shgcService.terminateAudit(shxxDto, user,request,lastlcxh,obj);
//										shgcService.doAudit(shxxDto, user,request);
									//更新审批管理信息
									ddspglDto.setCljg("1");
									ddspglService.updatecljg(ddspglDto);
									break;
								} catch (Exception e) {
									t_map.put("ddspglid", ddspglDto.getDdspglid());
									t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());

									if(i==dd_sprjs.size()-1)
										throw new BusinessException("ICOM99019",e.toString());
								}
							}
						}else if(("comment").equals(type)) {
							//评论时保存评论信息，添加至审核信息表
							ShgcDto shgc = shgcService.getDtoByYwid(shxxDto.getYwid());//获得已有审核过程
							ShxxDto shxx = new ShxxDto();
							String shxxid =StringUtil.generateUUID();
							shxx.setShxxid(shxxid);
							shxx.setSqr(shgc.getSqr());
							shxx.setLcxh(null);
							shxx.setShid(shgc.getShid());
							shxx.setSftg("1");
							shxx.setGcid(shgc.getGcid());
							shxx.setYwid(shxxDto.getYwid());
							shxx.setShyj("评论:"+content.substring(0, Math.min(content.length(), 6000)));//这里写死，备注钉钉审批被提交人员撤销了
							shxx.setLrry(user.getYhid());
							shxxService.insert(shxx);
						}
					}
				}else {
					if(("comment").equals(type)) {
						//评论时保存评论信息，添加至审核信息表
						ShxxDto shxx = new ShxxDto();
						String shxxid =StringUtil.generateUUID();
						shxx.setShxxid(shxxid);
						shxx.setSftg("1");
						shxx.setYwid(fjsqDto.getFjid());
						shxx.setShlb(AuditTypeEnum.AUDIT_RECHECK.getCode());
						List<ShxxDto> shxxlist=shxxService.getShxxOrderByPass(shxx);
						if(shxxlist!=null && shxxlist.size()>0) {
							shxx.setShid(shxxlist.get(0).getShid());
							shxx.setShyj("评论:"+content.substring(0, Math.min(content.length(), 6000)));//这里写死，备注钉钉审批被提交人员撤销了
							shxx.setLrry(user.getYhid());
							shxxService.insert(shxx);
						}
					}
				}
			}
		}catch(BusinessException e) {
			log.error("BusinessException:" + e.getMessage());
			throw new BusinessException("ICOM99019",e.getMsg());
		}catch (Exception e) {
			log.error("Exception:" + e.getMessage());
			throw new BusinessException("ICOM99019",e.toString());
		}finally {
			if(ddfbsglDto!=null) {
				if(org.apache.commons.lang3.StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {
					if("1".equals(ddspbcbj)) {
						t_map.put("sfbcspgl", "1");//是否返回上一层新增钉钉审批管理信息
					}
				}
			}
		}
		return true;
	}

	/**
	 * 更新取样信息
	 * @param fjsqDto
	 * @return
	 */
	public boolean updateSampleInfo(FjsqDto fjsqDto){
		return dao.updateSampleInfo(fjsqDto);
	}

	/**
	 * 通过fjid去取到sjid,这里的sjid用于后续的发送实验通知的sendMessage方法
	 * @param fjid
	 * @return
	 */
	public String getSjidFromFjid(String fjid){
		return dao.getSjidFromFjid(fjid);
	}

	/**
	 * 更新导出标记
	 * @param fjsqDto
	 * @return
	 */
	public boolean updateDcbj(FjsqDto fjsqDto){
		return dao.updateDcbj(fjsqDto);
	}
	/**
	 * 根据检测单位和审批岗位对应yhms查询系统用户
	 * @param map
	 * @return
	 */
	public List<Map<String,String>> getXtyhByMap(Map<String, Object> map){
		return dao.getXtyhByMap(map);
	}
	/**
	 * 获取历史数据
	 * @param
	 * @return
	 */
	public List<FjsqDto> getHistoryList(FjsqDto fjsqDto){
		List<FjsqDto> list = dao.getHistoryList(fjsqDto);
		try{
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_RFS_FC.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_OUT_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_DING_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_ADDCHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_ADDCHECK_REM.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_LAB_RECHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_LAB_ADDCHECK.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_LAB_ADDCHECK_REM.getCode(), "zt", "fjid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		}catch (BusinessException e){
			e.printStackTrace();
		}
		return list;
	}


	
	/**
	 * 获取实验日期小于今天的送检实验日期清单
	 * @param
	 * @return
	 */
	public List<FjsqDto> getSyxxByFj(FjsqDto fjsqDto){
		if(fjsqDto == null)
			return null;
		if(StringUtil.isBlank(fjsqDto.getSjid())  &&StringUtil.isBlank(fjsqDto.getFjid()))
			return null;
		return dao.getSyxxByFj(fjsqDto);
	}
	
	/**
	 * 根据送检ID获取审核中或者还未实验的复检数据
	 * @param
	 * @return
	 */
	public List<FjsqDto> getNotSyFjxxBySjxx(FjsqDto fjsqDto){
		return dao.getNotSyFjxxBySjxx(fjsqDto);
	}
	
	/**
	 * 确认是否可以发起加测或复测
	 * @param fjsqDto
	 * @param shlx
	 * @param jcsj_jcxm
	 * @param csdm
	 * @param list
	 * @return
	 * @throws BusinessException
	 */

	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean checkCanFj(FjsqDto fjsqDto,String shlx,JcsjDto jcsj_jcxm,String csdm,List<JcsjDto> list) throws BusinessException {
		FjsqDto fjsqDto_search = new FjsqDto();
		fjsqDto_search.setSjid(fjsqDto.getSjid());
		List<FjsqDto> historyList = getHistoryList(fjsqDto_search);
		List<String> jcxms = sjjcxmService.getSjjcxm(fjsqDto.getSjid());//样本送检项目
		if (AuditTypeEnum.AUDIT_ADDCHECK.getCode().equals(shlx) || AuditTypeEnum.AUDIT_LAB_ADDCHECK.getCode().equals(shlx)){
			//判断加测
			//判断与原项目是否有冲突， 若冲突，不允许复测
			if (!CollectionUtils.isEmpty(jcxms)){
				for (String jcxm : jcxms) {
					JcsjDto jcxmDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(), jcxm);
					if (jcxmDto != null && jcxmDto.getCskz3().equals(jcsj_jcxm.getCskz3())){
						//若该加测的检测项目与原有检测项目的cskz3相同，则进行下一步判断
						if (jcxmDto.getCskz1().equals(jcsj_jcxm.getCskz1()) || ("C".equals(jcsj_jcxm.getCskz1()) && "D,R".contains(jcxmDto.getCskz1())) || ("C".equals(jcxmDto.getCskz1()) && "D,R".contains(jcsj_jcxm.getCskz1()))){
							throw new BusinessException("加测项目与样本原项目有冲突，不可进行加测！");
						}
					}
				}
			}
			//判断与已有加测项目是否有冲突， 若冲突，不允许加测
			if (!CollectionUtils.isEmpty(historyList)){
				for (FjsqDto dto : historyList) {
					JcsjDto jcxmDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(), dto.getJcxm());
					if (!dto.getFjid().equals(fjsqDto.getFjid()) && fjsqDto.getLx().equals(dto.getLx()) && jcxmDto != null && jcxmDto.getCskz3().equals(jcsj_jcxm.getCskz3())){
						//若该加测的检测项目与原有检测项目的cskz3相同，则进行下一步判断
						if (jcxmDto.getCskz1().equals(jcsj_jcxm.getCskz1()) || ("C".equals(jcsj_jcxm.getCskz1()) && "D,R".contains(jcxmDto.getCskz1())) || ("C".equals(jcxmDto.getCskz1()) && "D,R".contains(jcsj_jcxm.getCskz1()))){
							//状态不为审核不通过，提示错误
							if (!StatusEnum.CHECK_UNPASS.getCode().equals(dto.getZt())){
								throw new BusinessException("当前加测项目已存在或与已有加测项目有冲突，不可进行加测！");
							}
						}
					}
				}
			}
		}
		else if (AuditTypeEnum.AUDIT_RECHECK.getCode().equals(shlx) || AuditTypeEnum.AUDIT_LAB_ADDCHECK_REM.getCode().equals(shlx)) {
			//判断复测，
			boolean canFc = false;//默认不允许复测
			//判断与原项目是否有重合， 若重合，允许复测
			if (!CollectionUtils.isEmpty(jcxms)){
				for (String jcxm : jcxms) {
					JcsjDto jcxmDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(), jcxm);
					if (jcxmDto != null && jcxmDto.getCskz3().equals(jcsj_jcxm.getCskz3())){
						//若该复测的检测项目与原有检测项目的cskz3相同，则进行下一步判断
						if (jcxmDto.getCskz1().equals(jcsj_jcxm.getCskz1()) || ("C".equals(jcsj_jcxm.getCskz1()) && "D,R".contains(jcxmDto.getCskz1())) || ("C".equals(jcxmDto.getCskz1()) && "D,R".contains(jcsj_jcxm.getCskz1()))){
							canFc = true;
							break;
						}
					}
				}
			}
			//判断与已有加测项目是否有重合， 若重合，允许复测
			if (!CollectionUtils.isEmpty(historyList)){
				for (FjsqDto dto : historyList) {
					JcsjDto jcxmDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(), dto.getJcxm());
					if (jcxmDto != null && jcxmDto.getCskz3().equals(jcsj_jcxm.getCskz3())){
						//若该复测的检测项目与历史复测检测项目的cskz3相同，则进行下一步判断
						if (jcxmDto.getCskz1().equals(jcsj_jcxm.getCskz1()) || ("C".equals(jcxmDto.getCskz1()) && "D,R".contains(jcsj_jcxm.getCskz1()))){
							if (!dto.getFjid().equals(fjsqDto.getFjid()) && StatusEnum.CHECK_NO.getCode().equals(dto.getZt()) && StatusEnum.CHECK_SUBMIT.getCode().equals(dto.getZt())){
								//若历史项目的状态为审核中或者未提交,直接不可保存
								canFc = false;
								break;
							}
							canFc = true;
						}
					}
				}
			}
			if (!canFc){
				throw new BusinessException("当前项目不可进行复测");
			}
		}
		else if (AuditTypeEnum.AUDIT_ADDCHECK_REM.getCode().equals(shlx) || AuditTypeEnum.AUDIT_LAB_RECHECK.getCode().equals(shlx)) {
			//判断REM，加测去人源和实验室加测去人源合并，只能存在一个
			//boolean canREM = false;//默认不允许复测
			List<String> lxs=new ArrayList<>();
			if(StringUtil.isNotBlank(csdm)){
				String[] strings = csdm.split("_");
				for(JcsjDto dto:list){
					if(dto.getCsdm().indexOf(strings[strings.length-1])!=-1){
						lxs.add(dto.getCsid());
					}
				}
			}
			FjsqDto fjsqDto_t=new FjsqDto();
			fjsqDto_t.setSjid(fjsqDto.getSjid());
			fjsqDto_t.setFjlxs(lxs);
			int num=getCount(fjsqDto_t);//查询当前送检信息是否已经存在
			if(num>0){ //num>0说明已经存在，然后去判断状态
				List<FjsqDto> fjsqDtos = getDtoList(fjsqDto_t);
				if(fjsqDtos!=null && fjsqDtos.size() > 0) {
					for (int i = 0; i < fjsqDtos.size(); i++) {
						if(!fjsqDtos.get(i).getFjid().equals(fjsqDto.getFjid()) && StatusEnum.CHECK_NO.getCode().equals(fjsqDtos.get(i).getZt()) || StatusEnum.CHECK_SUBMIT.getCode().equals(fjsqDtos.get(i).getZt())){
							throw new BusinessException("当前项目不可进行复测");
						}
					}
				}
			}
		}
		return true;
	}

	/**
	 * 根据文库编码获取复检数据
	 * @param fjsqDto
	 * @return
	 */
	public List<FjsqDto> getDtoByWkbms(FjsqDto fjsqDto){
		return dao.getDtoByWkbms(fjsqDto);
	}

	/**
	 * 根据复检申请ID更新报告日期
	 * @param fjsqDtos
	 * @return
	 */
	public boolean updateBgrqByDtos(List<FjsqDto> fjsqDtos){
		return dao.updateBgrqByDtos(fjsqDtos);
	}
}
