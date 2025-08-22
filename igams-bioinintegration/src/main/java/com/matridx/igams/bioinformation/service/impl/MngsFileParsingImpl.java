package com.matridx.igams.bioinformation.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.bioinformation.dao.entities.*;
import com.matridx.igams.bioinformation.service.svcinterface.*;
import com.matridx.igams.bioinformation.util.log2Util;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.YesNotEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import io.minio.MinioClient;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 文件解析service
 */
@Service
public class MngsFileParsingImpl implements IMngsFileParsingService {

	@Autowired
	IWkcxService wkcxService;
	@Autowired
	IWkcxbbService wkcxbbService;
	@Autowired
	IMngsmxjgService mngsmxjgService;
	@Autowired
	IBioXpxxService xpxxService;
	@Autowired
	IOtherService otherService;
	@Autowired
	IMngszjg2Service mngszjg2Service;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	private IDlfxjgService dlfxjgService;
	@Autowired
	private INyfxjgService nyfxjgService;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	IFjcfbService fjcfbService;
	@Value("${matridx.fileupload.releasePath:}")
	private String releasePath;
	@Autowired(required=false)
	private AmqpTemplate amqpTempl;
	private final Logger log = LoggerFactory.getLogger(MngsFileParsingImpl.class);

	private final String[] taxidArr=new String[]{"470", "287", "573", "1280", "562", "354276", "40324", "727", "83558", "2104", "1313", "480"};
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.wechat.biosequrl:}")
	private String biosequrl;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String,Object> FileParseingByType(ReceiveFileModel receiveFileModel, String data) throws Exception {
		Map<String,Object> map=new HashMap<>();
		try {
			creatXpxx(receiveFileModel.getChip());
		}catch (Exception e){
			redisUtil.del(receiveFileModel.getChip());
			throw new Exception("新增芯片信息，错误信息为："+e.getMessage());
		}
		//
		//final.report文件解析
		if (StringUtil.isNotBlank(receiveFileModel.getReportPath())){
			try{
				while (!redisUtil.setIfAbsent("redis-lock"+receiveFileModel.getChip()+receiveFileModel.getSample(), receiveFileModel.getChip()+receiveFileModel.getSample(), 5L, TimeUnit.MINUTES)) {
					Thread.sleep(500);
				}
				Map<String, Object> wkcx = createWkcx(receiveFileModel.getSample(),receiveFileModel.getChip(),receiveFileModel.getProgram(), data);
				if(wkcx==null)
					return null;
				if(StringUtil.isNotBlank(receiveFileModel.getQcPath())&&StringUtil.isNotBlank(receiveFileModel.getSample())){
					fileParsingQc(wkcx,receiveFileModel.getQcPath());
				}
				redisUtil.del("redis-lock"+receiveFileModel.getChip()+receiveFileModel.getSample());
				while (!redisUtil.setIfAbsent("redis-lock"+receiveFileModel.getChip(), receiveFileModel.getChip(), 5L, TimeUnit.MINUTES)) {
						Thread.sleep(500);
				}

				if(wkcx!=null)
					createMngsInfo(receiveFileModel.getReportPath(),receiveFileModel.getSample(),receiveFileModel.getChip(),receiveFileModel.getProgram(),
						data,wkcx.get("id")==null?"":wkcx.get("id").toString(),wkcx.get("yblxdm")==null?"":wkcx.get("yblxdm").toString(),
								wkcx.get("dilutionf")==null?"":wkcx.get("dilutionf").toString(),wkcx.get("xpid").toString(),wkcx);
				redisUtil.del("redis-lock"+receiveFileModel.getChip());
			}catch (Exception e){
				redisUtil.del("redis-lock"+receiveFileModel.getChip());
				redisUtil.del(receiveFileModel.getChip());
				redisUtil.del("redis-lock"+receiveFileModel.getChip()+receiveFileModel.getSample());
				throw new Exception("final.report文件解析出错，错误信息为："+e.getMessage() + " " + receiveFileModel.getReportPath());
			}
			modChipInfo(receiveFileModel.getChip());
		}
		//耐药结果文件分析
		if(StringUtil.isNotBlank(receiveFileModel.getCard_statPath())){
			try{
				fileParsingCard_stat1(receiveFileModel.getCard_statPath(), receiveFileModel.getSample(),receiveFileModel.getChip());
			}catch (Exception e){
				redisUtil.del(receiveFileModel.getChip());
				throw new Exception("耐药检测结果统计.txt文件解析出错，错误信息为："+e.getMessage() + " " + receiveFileModel.getCard_statPath());
			}
		}
		//耐药结果文件分析
		if(StringUtil.isNotBlank(receiveFileModel.getCard_Path())){
			try{
				fileParsingCard_stat(receiveFileModel.getCard_Path(), receiveFileModel.getSample(),receiveFileModel.getChip());
			}catch (Exception e){
				redisUtil.del(receiveFileModel.getChip());
				throw new Exception("耐药检测结果统计.json文件解析出错，错误信息为："+e.getMessage() + " " + receiveFileModel.getCard_Path());
			}
		}
		//毒力结果文件分析
		if(StringUtil.isNotBlank(receiveFileModel.getVfdb_statPath())){
			try{
				fileParsing_vfdb_stat1(receiveFileModel.getVfdb_statPath(),receiveFileModel.getSample(), receiveFileModel.getChip());
			}catch (Exception e){
				redisUtil.del(receiveFileModel.getChip());
				throw new Exception("毒力报告结果.txt文件解析出错，错误信息为："+e.getMessage() + " " + receiveFileModel.getVfdb_statPath());
			}
		}
		//毒力结果文件分析
		if(StringUtil.isNotBlank(receiveFileModel.getVfdb_Path())){
			try{
				fileParsing_vfdb_stat(receiveFileModel.getVfdb_Path(),receiveFileModel.getSample(), receiveFileModel.getChip());
			}catch (Exception e){
				redisUtil.del(receiveFileModel.getChip());
				throw new Exception("毒力报告结果.json文件解析出错，错误信息为："+e.getMessage() + " " + receiveFileModel.getVfdb_Path());
			}
		}

		//cov，json
		if(StringUtil.isNotBlank(receiveFileModel.getCoveragePath())){
			try{
				fileParsingCov(receiveFileModel.getCoveragePath(), receiveFileModel.getSample(),receiveFileModel.getChip());
			}catch (Exception e){
				redisUtil.del(receiveFileModel.getChip());
				throw new Exception("coverage.json文件解析出错，错误信息为："+e.getMessage() + " " + receiveFileModel.getCoveragePath());
			}
		}
		//mt结果文件
		if(StringUtil.isNotBlank(receiveFileModel.getMt_result_filePath())){
			try{
				fileParsing_mtResult(receiveFileModel.getMt_result_filePath(),receiveFileModel.getProgram(),receiveFileModel.getSample(),receiveFileModel.getChip());
			}catch (Exception e){
				redisUtil.del(receiveFileModel.getChip());
				throw new Exception("mtresult.json文件解析出错，错误信息为："+e.getMessage() + " " + receiveFileModel.getMt_result_filePath());
			}
		}
		//Ai结果文件
		if(StringUtil.isNotBlank(receiveFileModel.getDoctor_aiPath())){
			try{
				fileParsingAi(receiveFileModel.getDoctor_aiPath(), receiveFileModel.getSample(),receiveFileModel.getChip());
			}catch (Exception e){
				redisUtil.del(receiveFileModel.getChip());
				throw new Exception("ai.report文件解析出错，错误信息为："+e.getMessage() + " " + receiveFileModel.getDoctor_aiPath());
			}
		}
		//dash.json文件
		if(StringUtil.isNotBlank(receiveFileModel.getDashPath())){
			try{
				fileParsing_dash(receiveFileModel.getDashPath(),receiveFileModel.getChip());
			}catch (Exception e){
				redisUtil.del(receiveFileModel.getChip());
				throw new Exception("dash.json文件解析出错，错误信息为："+e.getMessage() + " " + receiveFileModel.getDashPath());
			}
		}
		if(StringUtil.isNotBlank(receiveFileModel.getBgPath())){
			try{
				fileParsing_bg(receiveFileModel.getBgPath(),receiveFileModel.getSample(),receiveFileModel.getChip());
			}catch (Exception e){
				redisUtil.del(receiveFileModel.getChip());
				throw new Exception("bg.json文件解析出错，错误信息为："+e.getMessage() + " " + receiveFileModel.getBgPath());
			}

		}

		if(StringUtil.isNotBlank(receiveFileModel.getFxPath())){
			try{
				fileParsing_fx(receiveFileModel.getFxPath(),receiveFileModel.getSample(),receiveFileModel.getChip());
			}catch (Exception e){
				redisUtil.del(receiveFileModel.getChip());
				throw new Exception("fx.json文件解析出错，错误信息为："+e.getMessage() + " " + receiveFileModel.getFxPath());
			}

		}
		redisUtil.del(receiveFileModel.getChip());

		return  map;
	}

	/**
	 * 创建芯片to
	 */
	public void creatXpxx(String chipId) throws InterruptedException {
		BioXpxxDto cxdto=new BioXpxxDto();
		cxdto.setXpm(chipId);
		while (!redisUtil.setIfAbsent(chipId, chipId, 5L, TimeUnit.MINUTES)){
			Thread.sleep(500);
		}
		BioXpxxDto resDto=xpxxService.getDto(cxdto);

		if(resDto==null){
			BioXpxxDto bioXpxxDto=new BioXpxxDto();
			bioXpxxDto.setXpid(StringUtil.generateUUID());
			bioXpxxDto.setXpm(chipId);
			//没有设置测序仪，造成第二次检索，因为无法关联基础数据测序仪，所以还是检索不出来，还是没有解决问题
			String[] s_chipid =chipId.split("_");
			if(s_chipid.length>1) {
				log.error("确认芯片信息切割情况：芯片代码为" + s_chipid[1]);
				//根据芯片里的测序仪代码获取测序仪信息
				List<JcsjDto> sequencer_list= redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SEQUENCER_CODE.getCode());//得到测序仪
				if(sequencer_list!=null && !sequencer_list.isEmpty()) {
					for (JcsjDto jcsjDto : sequencer_list) {
						if (jcsjDto.getCsdm().equals(s_chipid[1])) {
							bioXpxxDto.setCxyid(jcsjDto.getCsid());
							log.error("找到相应的测序仪信息，设置测序仪csid：" + jcsjDto.getCsid());
							break;
						}else if(s_chipid.length>2 && jcsjDto.getCsdm().equals(s_chipid[1] + "_" +s_chipid[2])){
							bioXpxxDto.setCxyid(jcsjDto.getCsid());
							log.error("找到相应的测序仪信息2，设置测序仪csid：" + jcsjDto.getCsid());
							break;
						}
					}
				}
			}
			bioXpxxDto.setFm("0");
			bioXpxxDto.setScbj("0");
			bioXpxxDto.setLrsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
			xpxxService.insertDto(bioXpxxDto);
		}
	}
	/***
	 * 根据版本号创建文库测序信息
	 */
	public Map<String,Object> createWkcx(String sampleId,String chipId,String program,String data){
		Map<String,Object> map=new HashMap<>();
		WkcxDto wkcxDto = new WkcxDto();
		wkcxDto.setWkbh(sampleId);
		wkcxDto.setXpm(chipId);
		WkcxDto dto1 = wkcxService.getDto(wkcxDto);
		BioXpxxDto bioXpxxDto = new BioXpxxDto();
		bioXpxxDto.setXpm(chipId);
		BioXpxxDto dto = xpxxService.getDto(bioXpxxDto);
		if(dto==null){
			log.error("createWkcx 未找到指定芯片信息：" + chipId + "为保证后续流程继续处理，本次采用new一个新的");
			dto = new BioXpxxDto();
		}
		OtherDto otherDto = new OtherDto();
		String[] split = sampleId.split("-");
		boolean flagNbbm=false;
		if (sampleId.startsWith("MDL")){
			if (!"PC".equals(split[1]) && !"NC".equals(split[1])){
				otherDto.setNbbm(split[1]);
			}else{
				flagNbbm=true;
				for(int x=split.length-1;x>=0;x--){
					if("DNA".equals(split[x])||"HS".equals(split[x])||"RNA".equals(split[x])||"tNGS".equals(split[x])
							||"TBtNGS".equals(split[x])||"Onco".equals(split[x])||"XD".equals(split[x])||"tNGSA".equals(split[x])){
						String nbbm="";

						for(int i=1;i<x;i++){
							if(StringUtil.isBlank(nbbm)){
								nbbm+=split[i];
							}else{
								nbbm+="-"+split[i];
							}
						}
						otherDto.setNbbm(nbbm);
						break;
					}
				}
			}
		}else{
			for(int x=split.length-1;x>=0;x--){
				if("DNA".equals(split[x])||"HS".equals(split[x])||"RNA".equals(split[x])||"tNGS".equals(split[x])
						||"TBtNGS".equals(split[x])||"Onco".equals(split[x])||"XD".equals(split[x])){
					StringBuffer s_splitString = new StringBuffer("");
					for(int y=0;y<x;y++){
						if(y!=0)
							s_splitString.append("-");
						s_splitString.append(split[y]);
					}
					otherDto.setNbbm(s_splitString.toString());
					break;
				}
			}
			if(StringUtil.isBlank(otherDto.getNbbm()))
				otherDto.setNbbm(split[0]);
		}
		OtherDto sjxxDto = null;
		if(StringUtil.isNotBlank(otherDto.getNbbm())) {
			List<OtherDto> sjxxDtoList = otherService.getSjxxDtoList(otherDto);
			for(OtherDto _dto:sjxxDtoList){
				if(sampleId.contains(_dto.getNbbm())){
					sjxxDto=_dto;
					break;
				}
			}
		}else {
			log.error("ERROR: 未找到相应的内部编码！");
			return null;
		}

		if (null != sjxxDto){
			wkcxDto.setSjid(sjxxDto.getSjid());
			String yblxdm="";
			if(flagNbbm){
				yblxdm=split[1].substring(split[1].length()-1);
			}else{
				yblxdm=sjxxDto.getNbbm().substring(sjxxDto.getNbbm().length()-1);
			}
			map.put("yblxdm",yblxdm);
			WkcxDto wkcxDto_t=new WkcxDto();
			wkcxDto_t.setSjid(sjxxDto.getSjid());
			if(!(sampleId.indexOf("-PC-")!=-1||sampleId.indexOf("-NC-")!=-1||sampleId.indexOf("-DC-")!=-1)){
				List<WkcxDto> dtoList = wkcxService.getDtoList(wkcxDto_t);

				if(dtoList!=null&&dtoList.size()>0){
					MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
					paramMap.add("wkbh", sampleId);
					RestTemplate t_restTemplate = new RestTemplate();
					//发送文件到服务器
					String json = t_restTemplate.postForObject(applicationurl+"/ws/inspection/getRecheckMessage", paramMap, String.class);
					JSONObject jsonObject = JSONObject.parseObject(json);
					String message = jsonObject.getString("message");
					if(StringUtil.isNotBlank(message)){
						JSONObject jsonObject_t = JSONObject.parseObject(message);
						String fjlxdm = jsonObject_t.getString("fjlxdm");
						if(StringUtil.isNotBlank(fjlxdm)){
							switch (fjlxdm) {
								case "ADDDETECT":
									wkcxDto.setFcjc("2");
									break;
								case "RECHECK":
									wkcxDto.setFcjc("1");
									break;
								case "REM":
									wkcxDto.setFcjc("3");
									break;
								case "PK":
									wkcxDto.setFcjc("4");
									break;
								case "LAB_RECHECK":
									wkcxDto.setFcjc("4");
									break;
								case "LAB_REM":
									wkcxDto.setFcjc("5");
									break;
								case "LAB_ADDDETECT":
									wkcxDto.setFcjc("6");
									break;
							}
						}
					}
				}
			}
		}
		String jcxm = "";
		if (sampleId.contains("-DNA-RNA")){
			wkcxDto.setWklx("DNA");
			jcxm = "D";
		}else if (sampleId.contains("-RNA-DNA")){
			wkcxDto.setWklx("RNA");
			jcxm = "R";
		}else if (sampleId.contains("-DNA-")||sampleId.contains("-Onco-")||sampleId.contains("-XD-")||sampleId.contains("-HS-")||
				sampleId.contains("-tNGS")||sampleId.contains("-TBtNGS-")){
			wkcxDto.setWklx("DNA");
			jcxm = "D";
		}else if (sampleId.contains("-RNA-")){
			wkcxDto.setWklx("RNA");
			jcxm = "R";
		}else if (sampleId.contains("-DR-")){
			if (sjxxDto != null && StringUtil.isNotBlank(sjxxDto.getNbbm()) && sjxxDto.getNbbm().contains("01D")) {
				wkcxDto.setWklx("DNA");
				jcxm = "D";
			}
			if (sjxxDto != null && StringUtil.isNotBlank(sjxxDto.getNbbm()) && sjxxDto.getNbbm().contains("01R")) {
				wkcxDto.setWklx("RNA");
				jcxm = "R";
			}
		}else{
			wkcxDto.setWklx("DNA");
			jcxm = "D";
		}
		map.put("ybbh",otherDto.getNbbm());
		map.put("jcxm",jcxm);
		String t_xpdata=chipId.split("_")[0];
		//月份截取错误对应，有时候  202506110314，也有时候 2506110314
		String str_year = "";
		String str_month = "";
		String str_day = "";
		if(t_xpdata.startsWith("20")) {
			String str_xpdate = t_xpdata.substring(0, 8);
			str_year = str_xpdate.substring(0, 4);
			str_month = str_xpdate.substring(4, 6);
			str_day = str_xpdate.substring(6, 8);
		}
		else {
			String str_xpdate = t_xpdata.substring(0, 6);
			str_year = "20" + str_xpdate.substring(0, 2);
			str_month = str_xpdate.substring(2, 4);
			str_day = str_xpdate.substring(4, 6);
		}
		String d_year = data.substring(0,4);
		String path = BusTypeEnum.IMP_BIOINFORMATION.getCode()+"/UP"+d_year+"/UP"+d_year+str_month+"/UP"+d_year+str_month+str_day+"/"+chipId;
		String path1 = d_year+"/"+str_month+"/"+str_day+"/"+chipId;
		map.put("path",path);
		log.error("mkpath:"+path);
		if (null != dto1){
			wkcxDto.setWkcxid(dto1.getWkcxid());
			dto1.setFxbb(dto1.getFxbb()+","+program);
			dto1.setWklx(wkcxDto.getWklx());
			dto1.setZxbb(program);
			dto1.setXgry("admin");
			wkcxService.update(dto1);
			map.put("dilutionf",dto1.getDilutionf()==null?"":dto1.getDilutionf());
			map.put("xpid",dto1.getXpid());
		}else{
			wkcxDto.setWkcxid(StringUtil.generateUUID());
			wkcxDto.setZt("01");
			JcsjDto _dto= new JcsjDto();
			_dto.setCsid(dto.getCxyid());
			if(StringUtil.isNotBlank(dto.getCxyid())){
				JcsjDto jcsjDto =jcsjService.getDto(_dto);
				wkcxDto.setCxid(dto.getCxyid());
				wkcxDto.setSysid(jcsjDto.getFcsid());
			}
			wkcxDto.setXpid(dto.getXpid());
			wkcxDto.setXgsj(data);
			map.put("xpid",dto.getXpid());
			wkcxDto.setFxbb(program);
			//wkcxDto.setSysid(bioXpxxDto.getFcsid());
			wkcxDto.setLrsj(data);
			wkcxDto.setSffsbg(YesNotEnum.NOT.getCode());
			//更新wksjmx mngs分析完成时间
			OtherDto otherDto1=new OtherDto();
			otherDto1.setWkbm(sampleId);
			otherDto1.setMngsfxwcsj(data);
			otherService.updateWksjmx(otherDto1);
			List<Map<String,String>> getYbbh=otherService.getWksjmxByWkbmOrYbbh(otherDto1);
			if(!CollectionUtils.isEmpty(getYbbh)){
				Map<String,String> ybbhMap=getYbbh.get(0);
				OtherDto otherDtoybbh=new OtherDto();
				otherDtoybbh.setYbbh(ybbhMap.get("ybbh"));
				LocalDate localDateStart = LocalDate.now();
				LocalDate localDateEnd = localDateStart.minus(1, ChronoUnit.DAYS);
				otherDtoybbh.setMngsfxwcsj_start(localDateStart.toString()+" 12:00:00");
				otherDtoybbh.setMngsfxwcsj_end(localDateEnd.toString()+" 12:00:00");
				List<Map<String,String>> getybbhList=otherService.getWksjmxByWkbmOrYbbh(otherDtoybbh);
				if(CollectionUtils.isEmpty(getybbhList)){
					Map<String,String> resMap=new HashMap<>();
					resMap.put("ybbh",ybbhMap.get("ybbh"));
					resMap.put("fxwcsj",data);
					amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.ADD_FXWCSJ.getCode(),  JSONObject.toJSONString(resMap));
				}

			}
			// 调用接口获取
			RestTemplate restTemplate=new RestTemplate();
			String reString="";
			boolean flag=true;
			try{
				//"http://dx.matridx.com:8002/BCL/api/qcinfo/"+sampleId+"/?token=eba3f34c24943e1c", String.class);
				if(biosequrl.endsWith("/"))
					biosequrl = biosequrl.substring(0, biosequrl.length() - 1);
				reString = restTemplate.getForObject(biosequrl + "/BCL/api/qcinfo/"+sampleId+"/?token=eba3f34c24943e1c", String.class);
				log.error("获取生信的文库信，sampleID=" + sampleId + " 结果为：" + reString);
			}catch (Exception e){
				flag=false;
				log.error("获取生信的文库信息失败，接口地址=BCL/api/qcinfo，错误信息为：" + e.getMessage());
			}
			if(flag){
				JSONObject jsonObject = JSONObject.parseObject(reString);
				JSONObject object = JSONObject.parseObject(jsonObject.get("data").toString());
				wkcxDto.setCleangc(object.get("cleangc").toString());
				wkcxDto.setCleanq20(object.get("cleanq20").toString());
				wkcxDto.setCleanq30(object.get("cleanq30").toString());
				wkcxDto.setAdapter(object.get("adapter").toString());

				wkcxDto.setClean(object.get("clean").toString());
				wkcxDto.setDilutionf(object.get("dilution_factor")==null?"":object.get("dilution_factor").toString());
				map.put("dilutionf",object.get("dilution_factor")==null?"":object.get("dilution_factor").toString());
				wkcxDto.setLibccon(object.get("con.lib")==null?"0":object.get("con.lib").toString());
				wkcxDto.setNuccon(object.get("con.dna")==null?"0":object.get("con.dna").toString());
				wkcxDto.setQc(object.toJSONString());
				BigDecimal hu = new BigDecimal(100);
				BigDecimal rawreads   =  new BigDecimal(object.get("rawreads").toString());
				BigDecimal cleanreads   =  new BigDecimal(object.get("cleanreads").toString());
				BigDecimal div_rawreads = rawreads;
				if(rawreads.compareTo(BigDecimal.ZERO) == 0)
					div_rawreads = new BigDecimal(1);
				BigDecimal barcodematch = new BigDecimal(object.get("barcode_all_match")==null?"0":object.get("barcode_all_match").toString()).multiply(hu).divide(div_rawreads,5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
				wkcxDto.setBarcodematch(barcodematch.toString());
				wkcxDto.setRawreads(rawreads.toString());
				wkcxDto.setCleanreads(cleanreads.toString());
				wkcxDto.setComment(object.get("comment").toString());
				//更新wknd
				OtherDto otherDto2=new OtherDto();
				otherDto2.setWkbm(sampleId);
				otherDto2.setWknd(wkcxDto.getLibccon());
				BigDecimal big_libccon = new BigDecimal("0");
				try{
					// 因为上机表经常上传一些错误数据，需要进行处理
					String s_libccon = wkcxDto.getLibccon();
					if(StringUtil.isNotBlank(s_libccon)){
						if(s_libccon.contains("e")){
							big_libccon = new BigDecimal(s_libccon);
						}else if(s_libccon.contains("-")){
							String[] sp = s_libccon.split("-");
							big_libccon = new BigDecimal(sp[0]);
						}else{
							big_libccon = new BigDecimal(s_libccon);
						}
					}
				} catch (Exception e) {
					log.error("上机表数据转换失败。原始数据：" + wkcxDto.getLibccon());
				}
				otherDto2.setDlnd(String.valueOf(big_libccon.divide(new BigDecimal("20"), 2, RoundingMode.HALF_UP)));
				otherDto2.setMngsfxwcsj(data);
				otherService.updateWksjmx(otherDto2);
			}
			wkcxDto.setZxbb(program);

			wkcxService.insert(wkcxDto);
			if(flag||sampleId.contains("DR")){
				saveMinioClientFile(BusTypeEnum.IMP_LIBRARY_SEQUENCING_IMG.getCode(),path1,wkcxDto.getWkcxid(),sampleId+".png",path);
			}
		}
		map.put("wkcxDto",wkcxDto);
		map.put("id",wkcxDto.getWkcxid());
		return  map;
	}

	private Map<String, Object> fileParsingQc(Map<String,Object>map,String path)throws IOException{
		WkcxDto wkcxDto=(WkcxDto)map.get("wkcxDto");
		File file = new File(path);
		String jsonString = FileUtils.readFileToString(file, "UTF-8");
		JSONObject object = JSONObject.parseObject(jsonString);
		wkcxDto.setCleangc(object.get("cleangc").toString());
		wkcxDto.setCleanq20(object.get("cleanq20").toString());
		wkcxDto.setCleanq30(object.get("cleanq30").toString());
		wkcxDto.setAdapter(object.get("adapter").toString());
		wkcxDto.setClean(object.get("clean").toString());
		wkcxDto.setDilutionf(object.get("dilution_factor")==null?"":object.get("dilution_factor").toString());
		wkcxDto.setLibccon(object.get("con.lib").toString());
		wkcxDto.setNuccon(object.get("con.dna")==null?"0":object.get("con.dna").toString());
		wkcxDto.setQc(object.toJSONString());
		BigDecimal hu = new BigDecimal(100);
		BigDecimal cleanreads   =  new BigDecimal(object.get("cleanreads").toString());
		BigDecimal rawreads   =  new BigDecimal(object.get("rawreads").toString());
		BigDecimal div_rawreads = rawreads;
		if(rawreads.compareTo(BigDecimal.ZERO) == 0)
			div_rawreads = new BigDecimal(1);
		BigDecimal barcodematch = new BigDecimal(object.get("barcode_all_match").toString()).multiply(hu).divide(div_rawreads,5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
		wkcxDto.setBarcodematch(barcodematch.toString());
		wkcxDto.setRawreads(rawreads.toString());
		wkcxDto.setCleanreads(cleanreads.toString());
		wkcxDto.setComment(object.get("comment").toString());
		wkcxService.insert(wkcxDto);
		map.put("dilutionf",wkcxDto.getDilutionf());
		return map;
	}
	private  Boolean saveMinioClientFile(String ywlx,String path,String ywid,String wjm,String path1) {
		boolean flag = false;
		MinioClient minioClient;
		try {
			minioClient = new MinioClient("https://minio.matridx.com", "ituser", "Matridx@2022");
			InputStream inputStream = minioClient.getObject("qcplot",path+"/"+wjm);
			BufferedInputStream input = null;
			byte[] buffer = new byte[4096];
			FileOutputStream fos = null;
			BufferedOutputStream output = null;
			try {
				input = new BufferedInputStream(inputStream);

				fos = new FileOutputStream(releasePath +path1+"/"+wjm);
				output = new BufferedOutputStream(fos);
				int n;
				while ((n = input.read(buffer, 0, 4096)) > -1) {
					output.write(buffer, 0, n);
				}
				flag = true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeStream(new Closeable[] {
						inputStream, input, output, fos });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (flag){
			DBEncrypt bpe = new DBEncrypt();
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setFjid(StringUtil.generateUUID());
			fjcfbDto.setYwid(ywid);
			fjcfbDto.setZywid("");
			fjcfbDto.setXh("1");
			fjcfbDto.setYwlx(ywlx);
			fjcfbDto.setWjm(wjm);
			fjcfbDto.setWjlj(bpe.eCode(releasePath +path1+"/"+wjm));
			fjcfbDto.setFwjlj(bpe.eCode(releasePath +path1));
			fjcfbDto.setFwjm(bpe.eCode(wjm));
			fjcfbDto.setZhbj("0");
			flag = fjcfbService.insert(fjcfbDto);
		}

		return flag;
	}


	/**
	 * 关闭流
	 */
	private static void closeStream(Closeable[] streams) {
		if(streams==null || streams.length < 1)
			return;
		for (Closeable closeable : streams) {
			try {
				if (null != closeable) {
					closeable.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/***
	 * 保留n位小数 四舍五入
	 */
	public BigDecimal numberConversion(Double number,Integer count){
		BigDecimal bigDecimal = new BigDecimal(number);
		return bigDecimal.setScale(count, RoundingMode.HALF_UP);
	}

	/***
	 * 根据版本号创建临时mngs明细信息
	 */
	public Map<String,Object> createMngsInfo(String filePath,String sampleId,String chipId,String program,String data,String wkcxid,String yblxdm,String dilutionf,String xpid,Map<String, Object> wkcx) throws Exception {
		Map<String,Object> map=new HashMap<>();
		Map<String,Object> listMap=FileParseing_Final_report(filePath);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> list = (List<Map<String, Object>>)listMap.get("list");
		long count = Long.parseLong(list.get(0).get("readsaccum").toString());
		List<Map<String, Object>> maps = tree_Final_report(list);
		WkcxbbDto wkcxbbDto = new WkcxbbDto();
		wkcxbbDto.setJgid(StringUtil.generateUUID());
		Mngszjg2Dto mngszjg2Dto = new Mngszjg2Dto();
		mngszjg2Dto.setJgid(wkcxbbDto.getJgid());
		mngszjg2Dto.setWkcxid(wkcxid);
		mngszjg2Dto.setWkbh(sampleId);
		mngszjg2Dto.setFinaljson(JSONObject.toJSONString(maps));
		mngszjg2Service.insert(mngszjg2Dto);

		for (Map<String, Object> stringObjectMap : list) {
			if ("1".equals(stringObjectMap.get("taxid"))) {
				count += Long.parseLong(stringObjectMap.get("readsaccum").toString());
				break;
			}
		}
		if (count < 3000000 && !"NC".equals(wkcx.get("ybbh")) && !"PC".equals(wkcx.get("ybbh"))&& !"DC".equals(wkcx.get("ybbh"))){
			Map<String, Object> request = new HashMap<>();
			request.put("ybbh",wkcx.get("ybbh")==null? "":wkcx.get("ybbh").toString());
			request.put("lx", "RECHECK_TYPE");
			request.put("jcxm", wkcx.get("jcxm")==null? "":wkcx.get("jcxm").toString());
			request.put("yy", "数据量不足");
			RestTemplate restTemplate=new RestTemplate();
			restTemplate.postForObject(applicationurl+"/ws/recheck/submitRecheckAudit", request, String.class);
			WkcxDto wkcxDto = new WkcxDto();
			wkcxDto.setWkcxid(wkcxid);
			wkcxDto.setFcjc("1");
			wkcxDto.setZt("04");
			wkcxService.update(wkcxDto);
		}
		int pos;
		long coefficient;

		if ("B".equals(yblxdm)){
			pos = 1;
			coefficient = 100000L;
		}else{
			pos = 2;
			coefficient = 1887805L;
		}
		if (sampleId.contains("REM")){
			coefficient = 94390L;
		}
		long spike = 1L;
		for (int i = list.size()-1; i >= 0 ; i--) {
			if (pos ==1){
				if (list.get(i).get("name").toString().contains("spike.s1")){
					spike = Long.parseLong(list.get(i).get("readsaccum").toString());
					break;
				}
			}
			if (pos ==2){
				if (list.get(i).get("name").toString().contains("spike.1")){
					spike = Long.parseLong(list.get(i).get("readsaccum").toString());
					break;
				}
			}

		}
		List<MngsmxjgDto> dtos = new ArrayList<>();
		List<MngsmxjgDto> dtos1 = new ArrayList<>();
		List<Map<String, Object>> spikes = new ArrayList<>();
		BigDecimal decimal = new BigDecimal(count);
		BigDecimal bigDecimal1 = new BigDecimal(1000000);
		BigDecimal hundred = new BigDecimal(100);
		BigDecimal sp = new BigDecimal(spike);
		BigDecimal sp_in = new BigDecimal(listMap.get("16776960")==null?"0":listMap.get("16776960").toString());
		BigDecimal add = sp.add(new BigDecimal(1));
		BigDecimal cb = new BigDecimal(coefficient);
		BigDecimal dp = new BigDecimal(StringUtil.isBlank(dilutionf)?"1":dilutionf);
		//List<Map<String,Object>> taxidList=new ArrayList<>();
		WkcxDto dtoNew = new WkcxDto();
		dtoNew.setLrsj(data.replace("-",""));
		List<MngsmxjgDto> pylist=new ArrayList<>();
		//物种质控list
		List<Map<String,String>>mrzkList=new ArrayList<>();

		Map<String,String>wkgl=new HashMap<>();
		if(sampleId.contains("PC-")||sampleId.contains("NC-")||sampleId.contains("DC-")){
			wkgl=otherService.getWkgl(sampleId);
		}
		for (int i = 0; i < list.size(); i++) {

			MngsmxjgDto mngsmxjgDto = new MngsmxjgDto();
			//Map<String,Object> taxidMap = new HashMap<>();
			MngsmxjgDto pydto=new MngsmxjgDto();
			String[] dataArr=data.split(" ");
			if (dataArr!=null && dataArr.length>1){
				if(dataArr[0].length() > 8)
					mngsmxjgDto.setLrsj(data);
				else
					mngsmxjgDto.setLrsj(dtoNew.getLrsj().substring(0,4)+"-"+dtoNew.getLrsj().substring(4,6)+"-"+dtoNew.getLrsj().substring(6,8)+" "+dataArr[1]);
			}
			mngsmxjgDto.setJgid(wkcxbbDto.getJgid());
			mngsmxjgDto.setXpid(xpid);
			mngsmxjgDto.setLssj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(4,6));
			mngsmxjgDto.setWkbh(sampleId);
			mngsmxjgDto.setMxjgid(StringUtil.generateUUID());
			mngsmxjgDto.setXm(list.get(i).get("name").toString().trim());
			list.get(i).put("mxjgid",mngsmxjgDto.getMxjgid());
			mngsmxjgDto.setTaxid(list.get(i).get("taxid").toString());
			mngsmxjgDto.setReadsaccum(list.get(i).get("readsaccum").toString());
			mngsmxjgDto.setFid(list.get(i).get("ftaxid")==null?list.get(i).get("taxid").toString():list.get(i).get("ftaxid").toString());
			mngsmxjgDto.setReadscount(list.get(i).get("readscount").toString());
			mngsmxjgDto.setBbh(program);
			mngsmxjgDto.setAijg("5");
			mngsmxjgDto.setGzd("5");
			mngsmxjgDto.setHead(YesNotEnum.YES.getCode());
			mngsmxjgDto.setWkcxid(wkcxid);
			mngsmxjgDto.setXh(String.valueOf(i));
			mngsmxjgDto.setCj(list.get(i).get("cj")==null?"0":list.get(i).get("cj").toString());
			BigDecimal bigDecimal = new BigDecimal(list.get(i).get("readsaccum").toString());
			BigDecimal div_decimal = decimal;
			if(decimal.compareTo(BigDecimal.ZERO) == 0)
				div_decimal = new BigDecimal(1);
			mngsmxjgDto.setRpq(bigDecimal.multiply(bigDecimal1).divide(div_decimal,5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).toString());
			BigDecimal rpq = new BigDecimal(mngsmxjgDto.getRpq());
			BigDecimal div_dp = dp;
			if(dp.compareTo(BigDecimal.ZERO) == 0)
				div_dp = new BigDecimal(1);
			mngsmxjgDto.setAdjusted(rpq.divide(div_dp,2, RoundingMode.HALF_UP).toString());

			BigDecimal div_sp = sp;
			if(sp.compareTo(BigDecimal.ZERO) == 0)
				div_sp = new BigDecimal(1);
			double log2 = log2Util.log2(cb.multiply(new BigDecimal(mngsmxjgDto.getReadsaccum())).multiply(dp).divide(div_sp,5, RoundingMode.HALF_UP).add(new BigDecimal(1)).setScale(6, RoundingMode.HALF_UP).doubleValue());
			mngsmxjgDto.setQz(numberConversion(log2,2).toPlainString());
			pydto.setQz(numberConversion(log2,2).toPlainString());
			pydto.setTaxid(list.get(i).get("taxid").toString());
			pydto.setYblx(yblxdm);
			pylist.add(pydto);

			//double qzpw = log2Util.GaussianKernelSmoothing("/matridx/python/qindex.py", yblxdm, mngsmxjgDto.getTaxid(), Double.parseDouble(mngsmxjgDto.getQz()), "/matridx/python/");
			//double qzpw = log2Util.GaussianKernelSmoothing("D:/qindex.py", yblxdm, mngsmxjgDto.getTaxid(), Double.parseDouble(mngsmxjgDto.getQz()), "D:/");
			//mngsmxjgDto.setQzpw(numberConversion(qzpw,2).toString());
			 //mngsmxjgDto.setQzpw("30.11");
//				String[] strings = list.get(i).get("name").toString().split(" ");
//				Integer length = 0;
//				for (String string : strings) {
//					if (StringUtil.isBlank(string)){
//						length++;
//					}else{
//						break;
//					}
//				}
//				if ( length == 0 ){
//					mngsmxjgDto.setFid(mngsmxjgDto.getMxjgid());
//				}else{
//					for (int j = i; j >= 0; j--) {
//						String[] split = list.get(j).get("name").toString().split(" ");
//						Integer pos = 0;
//						for (String string : split) {
//							if (StringUtil.isBlank(string)){
//								pos++;
//							}else{
//								break;
//							}
//						}
//						if ( pos == length-1){
//							mngsmxjgDto.setFid(list.get(j).get("mxjgid").toString());
//							break;
//						}
//					}
//				}
			if (list.get(i).get("name").toString().contains("spike")){
				spikes.add(list.get(i));
			}
			dtos.add(mngsmxjgDto);

			if(sampleId.contains("PC-")||sampleId.contains("NC-")||sampleId.contains("DC-")){
				Map<String,String> mrzkMap=new HashMap<>();
				boolean flag=false;
				if(sampleId.contains("PC-")){
					mrzkMap.put("zklx","PC");
					flag=true;
				}else if(sampleId.contains("NC-")){
					mrzkMap.put("zklx","NC");
					// 先判断当前redis里是否存在
					Object o_wzzbx = redisUtil.hget("WZ_ZBX",mngsmxjgDto.getTaxid());
					List<Map<String,Object>> wzzbx = null;
					// 物种致病性不存在
					if(o_wzzbx==null){
						Map<String,String> param=new HashMap<>();
						param.put("wzid", mngsmxjgDto.getTaxid());
						//物种致病信息
						wzzbx = otherService.getwzzbx(param);
						if(wzzbx!=null && wzzbx.size()>0)
							redisUtil.hset("WZ_ZBX",mngsmxjgDto.getTaxid(),JSONObject.toJSONString(wzzbx));
						else
							redisUtil.hset("WZ_ZBX",mngsmxjgDto.getTaxid(),"");
					}else{
						if(StringUtil.isNotBlank(o_wzzbx.toString())){
							wzzbx = JSONObject.parseObject(o_wzzbx.toString(),List.class);
						}
					}
					if(wzzbx!=null && wzzbx.size()>0){
						flag=true;
					}
				}else if(sampleId.contains("DC-")){
					mrzkMap.put("zklx","DC");
					flag=true;
				}
				mrzkMap.put("mrzkid",StringUtil.generateUUID());
				mrzkMap.put("xpm",chipId);
				WkcxDto wkcxDto=(WkcxDto)wkcx.get("wkcxDto");
				mrzkMap.put("xpid",xpid);
				mrzkMap.put("rpm",mngsmxjgDto.getRpq());
				mrzkMap.put("wzid",mngsmxjgDto.getTaxid());
				mrzkMap.put("sjid",wkcxDto.getSjid());
				mrzkMap.put("jcdw",wkcxDto.getSysid());
				mrzkMap.put("wkrq",wkgl==null?null:wkgl.get("wkrq"));
				mrzkMap.put("ybbh",wkcx.get("ybbh")==null?"":wkcx.get("ybbh").toString());
				if(flag){
					mrzkList.add(mrzkMap);
				}
			}
		}
		String porReplaceStr = program.replace(":","_");
		if(StringUtil.isNotBlank(yblxdm)){
			String xpdata=chipId.split("_")[0];
			String s_data = data.replace("-", "");
			String s_year = s_data.substring(0,4);
			String taxidPath=releasePath+ BusTypeEnum.IMP_BIOINFORMATION.getCode() +"/UP"+s_year+"/UP"+s_year+xpdata.substring(2,4)+"/UP"+s_year+xpdata.substring(2,4)+xpdata.substring(4,6)+"/"+chipId+"/"+sampleId+"/"+porReplaceStr+"/";
			log.error(taxidPath);
			String returnPath=log2Util.GaussianKernelSmoothingList(taxidPath,"/matridx/python/qindexlist.py",pylist , "/matridx/python/");
			try {
				if(StringUtil.isBlank(returnPath)){
					log.error("没有获取到q值排位");
				}else {
					File file = new File(returnPath);
					String jsonString = FileUtils.readFileToString(file, "UTF-8");
					JSONObject parseObject = JSON.parseObject(jsonString);
					for (MngsmxjgDto dto : dtos) {
						dto.setQzpw(parseObject.get(dto.getTaxid()) == null ? "0" : parseObject.get(dto.getTaxid()) + "");
					}
				}


			}catch (Exception e){
				log.error(e.getMessage());
				log.error("读取python文件错误");
			}
		}

		if(dtos.size()>0){
			if(dtos.size()>100){
				for(int a=1;a<=dtos.size();a++){
					dtos1.add(dtos.get(a-1));
					if(a!=0&&a%100==0){
						mngsmxjgService.batchInsertLs(dtos1);
						dtos1.clear();
					}

					if(a>dtos.size()-1&&dtos1.size()>0){
						mngsmxjgService.batchInsertLs(dtos1);
					}
				}

			}else{
				mngsmxjgService.batchInsertLs(dtos);
			}
		}
		List<Map<String,String>>mrzkList1=new ArrayList<>();
		if(mrzkList.size()>0){
			if(mrzkList.size()>50){
				for(int a=1;a<=mrzkList.size();a++){
					mrzkList1.add(mrzkList.get(a-1));
					if(a!=0&&a%50==0){
						otherService.batchInsertMrzk(mrzkList1);
						mrzkList1.clear();
					}

					if(a>mrzkList.size()-1&&mrzkList1.size()>0){
						otherService.batchInsertMrzk(mrzkList1);
					}
				}

			}else{
				otherService.batchInsertMrzk(mrzkList);
			}
		}
		MngsmxjgDto mngsmxjgDto = new MngsmxjgDto();
		mngsmxjgDto.setBbh(program);
		mngsmxjgDto.setWkbh(sampleId);
		mngsmxjgDto.setWkcxid(wkcxid);
		mngsmxjgDto.setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(4,6));
		mngsmxjgService.updateWzxx(mngsmxjgDto);
		List<MngsmxjgDto> mngsmxjgDtoList = mngsmxjgService.getLsAll(mngsmxjgDto);
		mngsmxjgDtoList.sort((x, y) -> Integer.compare(Integer.parseInt(x.getXh()), Integer.parseInt(y.getXh())));//这方法需要jdk1.8以上
		//List<BioWzglDto> wzglDtoList=wzglService.getDtoList(new BioWzglDto());

		for(int i=mngsmxjgDtoList.size()-1;i>=0;i--){
			MngsmxjgDto mngsmxjgDto1=mngsmxjgDtoList.get(i);
			int cj=Integer.parseInt(mngsmxjgDto1.getCj());

			mngsmxjgDtoList.get(i).setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(4,6));
			for(int x=i;x>0;x--){
				if(Integer.parseInt(mngsmxjgDtoList.get(x).getCj())==(cj-1)){
					boolean flag= false;
					for(MngsmxjgDto wzglDto:mngsmxjgDtoList){
						if(wzglDto.getTaxid().equals(mngsmxjgDtoList.get(x).getTaxid())&&StringUtil.isNotBlank(wzglDto.getFl())){
							flag=true;
							break;
						}
					}
					if(flag){
						mngsmxjgDtoList.get(i).setScid(mngsmxjgDtoList.get(x).getTaxid());
						break;
					}else{
						cj-=1;
					}

				}
			}
		}
		//MngsmxjgDto getDtoNull = mngsmxjgService.getDtoNull(mngsmxjgDto);
//			if(mngsmxjgDtoList!=null&mngsmxjgDtoList.size()>0){
//				if(mngsmxjgDtoList.size()>100){
//					for(int a=0;a<mngsmxjgDtoList.size();a++){
//						mngsmxjgDtoList1.add(mngsmxjgDtoList.get(a));
//						if(a!=0&&a%100==0){
//							mngsmxjgService.updateListLs(mngsmxjgDtoList1);
//							mngsmxjgDtoList1.clear();
//						}
//
//						if(a>=mngsmxjgDtoList.size()){
//							mngsmxjgService.updateListLs(mngsmxjgDtoList1);
//						}
//					}
//
//				}else{
//					mngsmxjgService.updateListLs(mngsmxjgDtoList);
//				}
//			}


		//mngsmxjgService.deleteByNull(mngsmxjgDto);
		BigDecimal div_decimal = decimal;
		if(decimal.compareTo(BigDecimal.ZERO) == 0)
			div_decimal = new BigDecimal(1);

		BigDecimal Unmapped = new BigDecimal(listMap.get("0").toString()).multiply(hundred).divide(div_decimal,4, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP);
		List<MngsmxjgDto> groupBy = mngsmxjgService.getDtoListGroupBy(mngsmxjgDto);
		//List<MngsmxjgDto> dtoList = mngsmxjgService.getDtoListLs(mngsmxjgDto);
		List<MngsmxjgDto> dtoList1 = new ArrayList<>();
		BigDecimal others = new BigDecimal(0);
		BigDecimal Par = null;
		for (MngsmxjgDto dto : groupBy) {
			if (StringUtil.isNotBlank(dto.getWzdl())) {
				if ("Parasite".equals(dto.getWzdl())){
					Par = new BigDecimal(dto.getNum());
					BigDecimal Parasite =  Par.multiply(hundred).divide(div_decimal,5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP);
					others = others.add(Parasite);
					wkcxbbDto.setParasite(String.valueOf(Parasite));
					break;
				}
			}
		}
		if (!StringUtil.isNotBlank(wkcxbbDto.getParasite())){
			Par = new BigDecimal(0);
			wkcxbbDto.setParasite("0.000");
		}

		if ( null != listMap.get("4751")){
			BigDecimal F = new BigDecimal(listMap.get("4751").toString());
			BigDecimal Fungi = F.multiply(hundred).divide(div_decimal,5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP);
			others = others.add(Fungi);
			if (Par != null) {
				Par = Par.add(F);
			}
			wkcxbbDto.setFungi(String.valueOf(Fungi));
		}else{
			wkcxbbDto.setFungi("0.000");
		}


		if ( null != listMap.get("9606")){
			BigDecimal Homo = new BigDecimal(listMap.get("9606").toString()).multiply(hundred).divide(div_decimal,5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP);
			others = others.add(Homo);
			wkcxbbDto.setHomo(String.valueOf(Homo));
		}else{
			wkcxbbDto.setHomo("0.000");
		}

		if ( null != listMap.get("10239")){
			BigDecimal V = new BigDecimal(listMap.get("10239").toString());
			BigDecimal Viruses = V.multiply(hundred).divide(div_decimal,5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP);
			others = others.add(Viruses);
			if (Par != null) {
				Par = Par.add(V);
			}
			wkcxbbDto.setViruses(String.valueOf(Viruses));

		}else{
			wkcxbbDto.setViruses("0.000");
		}

		String taxid_2=listMap.get("2") ==null ? "0" :listMap.get("2").toString();
		String taxid_2751=listMap.get("2751") ==null ? "0" :listMap.get("2751").toString();
		BigDecimal B = new BigDecimal(taxid_2).add(new BigDecimal(taxid_2751));
		if (Par != null) {
			Par = Par.add(B);
		}
		BigDecimal Bacteria = B.multiply(hundred).divide(div_decimal,5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP);
			others = others.add(Bacteria);
			wkcxbbDto.setBacteria(String.valueOf(Bacteria));


			for (int i = 0; i < mngsmxjgDtoList.size(); i++) {
				mngsmxjgDtoList.get(i).setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(4,6));
				if (StringUtil.isNotBlank(mngsmxjgDtoList.get(i).getWzdl())){
					if ("G".equals(mngsmxjgDtoList.get(i).getFldj()) || "F".equals(mngsmxjgDtoList.get(i).getFldj())){
						mngsmxjgDtoList.get(i).setGrc(mngsmxjgDtoList.get(i).getReadsaccum());
						if (Par != null) {
							if(Par.toString().equals("0")){
								mngsmxjgDtoList.get(i).setAbd("0");
							}else{
								mngsmxjgDtoList.get(i).setAbd(new BigDecimal(mngsmxjgDtoList.get(i).getReadsaccum()).multiply(hundred).divide(Par,5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).toString());
							}
						}
					}else{
						if (Par != null) {
							if(Par.toString().equals("0")){
								mngsmxjgDtoList.get(i).setAbd("0");
							}else{
								mngsmxjgDtoList.get(i).setAbd(new BigDecimal(mngsmxjgDtoList.get(i).getReadsaccum()).multiply(hundred).divide(Par,5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).toString());
							}
						}
						String value = "";
						for (int j = i-1; j >=0; j--) {
							if (StringUtil.isNotBlank(value)){
								if (mngsmxjgDtoList.get(j).getTaxid().equals(value)){
									if ("G".equals(mngsmxjgDtoList.get(j).getFldj())  || "F".equals(mngsmxjgDtoList.get(j).getFldj())){
										mngsmxjgDtoList.get(i).setGrc(mngsmxjgDtoList.get(j).getReadsaccum());
										break;
									}
									value = mngsmxjgDtoList.get(j).getFid();
								}
							}else{
								if (mngsmxjgDtoList.get(j).getTaxid().equals(mngsmxjgDtoList.get(i).getFid())){
									if ("G".equals(mngsmxjgDtoList.get(j).getFldj())  || "F".equals(mngsmxjgDtoList.get(j).getFldj())){
										mngsmxjgDtoList.get(i).setGrc(mngsmxjgDtoList.get(j).getReadsaccum());
										break;
									}
									value = mngsmxjgDtoList.get(j).getFid();
								}
							}
						}

					}
				}

			}
			if(mngsmxjgDtoList.size()>0){
				if(mngsmxjgDtoList.size()>100){
					for(int a=1;a<=mngsmxjgDtoList.size();a++){
						dtoList1.add(mngsmxjgDtoList.get(a-1));
						if(a!=0&&a%100==0){
							mngsmxjgService.updateListLs(dtoList1);
							dtoList1.clear();
						}

						if(a>mngsmxjgDtoList.size()-1&&dtoList1.size()>0){
							mngsmxjgService.updateListLs(dtoList1);
						}
					}

				}else{
					mngsmxjgService.updateListLs(mngsmxjgDtoList);
				}
			}

			mngsmxjgDto.setJgid(wkcxbbDto.getJgid());
			List<MngsmxjgDto> dtoList=mngsmxjgService.getGidAndsfdc(mngsmxjgDto);
			for(MngsmxjgDto _dto:dtoList){
				_dto.setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(4,6));
			}
			mngsmxjgService.updateGidAndsfdc(dtoList);
//			MngsmxjgDto _dto =new MngsmxjgDto();
//			_dto.setBbh(program);
//			_dto.setWkbh(sampleId);
//			_dto.setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(4,6));
//			mngsmxjgService.updateListLsFl(_dto);
			wkcxbbDto.setWkcxid(wkcxid);
			wkcxbbDto.setSpike(JSONObject.toJSON(spikes).toString());
			wkcxbbDto.setTotalreads(Long.toString(count));
			wkcxbbDto.setWkbh(sampleId);
			wkcxbbDto.setBbh(program);
			if(org.apache.commons.collections.CollectionUtils.isNotEmpty(spikes)){
				Map<String,String>spikesMap=new HashMap<>();
				for(Map<String,Object>spike_map:spikes){
					spikesMap.put(spike_map.get("name").toString().trim(),spike_map.get("readscount").toString());
				}
				wkcxbbDto.setSpikejson(JSONObject.toJSON(spikesMap).toString());
			}

			wkcxbbDto.setUnmapped(String.valueOf(Unmapped));
			wkcxbbDto.setLrsj(data);

			sp_in = sp_in.multiply(bigDecimal1).divide(div_decimal,5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP);
			wkcxbbDto.setSpikeinrpm( sp_in.setScale(2, RoundingMode.HALF_UP).toString());
			wkcxbbDto.setSpikein(sp_in.divide(new BigDecimal(10000),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP).toString());

			BigDecimal one = new BigDecimal(100).subtract(others).subtract(sp_in.divide(new BigDecimal(10000),5, RoundingMode.HALF_UP)).subtract(Unmapped).setScale(4, RoundingMode.HALF_UP);
			wkcxbbDto.setOthers(String.valueOf(one));
			if(listMap.get("9606")==null){
				wkcxbbDto.setRyzs("0");
			}else{
				double log2 = log2Util.log2(cb.multiply(new BigDecimal(listMap.get("9606")==null?"0":listMap.get("9606").toString())).multiply(dp).divide(add,4, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP).doubleValue());
				if(log2==Double.NEGATIVE_INFINITY)
					log2 = 0;
				wkcxbbDto.setRyzs(StringUtil.isBlank(dilutionf)?"0": numberConversion(log2, 2).toPlainString());
			}
			if(StringUtil.isNotBlank(yblxdm)){
				double rypw = log2Util.GaussianKernelSmoothing("/matridx/python/qindex.py", yblxdm, "9606", Double.parseDouble(wkcxbbDto.getRyzs()), "/matridx/python/");
				if(rypw==Double.NEGATIVE_INFINITY)
					rypw = 0;
				wkcxbbDto.setRypw(numberConversion(rypw,2).toPlainString());
			}

			//wkcxbbDto.setRypw("20.77");
			wkcxbbDto.setSfzwqckj("1");
			wkcxbbService.insert(wkcxbbDto);
			BioXpxxDto bioXpxxDto = new BioXpxxDto();
			bioXpxxDto.setXpm(chipId);
			BioXpxxDto dto = xpxxService.getDto(bioXpxxDto);
			mngsmxjgDto.setXpid(dto.getXpid());
			List<String> strings = new ArrayList<>();
			List<String> strings1 = new ArrayList<>();
			for (MngsmxjgDto mngsmxjgDto1 : mngsmxjgDtoList) {
				strings.add(mngsmxjgDto1.getTaxid());
			}
			mngsmxjgDto.setIds(strings);
			String s_data = data.replace("-", "");
			String s_year = s_data.substring(0,4);
			String s_month = s_data.substring(4,6);
			String s_day = s_data.substring(6,8);
			String time=s_year+"-"+s_month+"-"+data.substring(6,8);
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
			Date date =sdf.parse(time);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			String day = String.valueOf(calendar.get(Calendar.DATE));//也可以用DAY_OF_MONTH
			Calendar cal = Calendar.getInstance();
			//设置年份
			cal.set(Calendar.YEAR,Integer.parseInt(s_year));
			//设置月份
			cal.set(Calendar.MONTH, Integer.parseInt(s_month));
			//获取当月最小值
			int lastDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
			//设置日历中的月份，当月+1月-1天=当月最后一天
			cal.set(Calendar.DAY_OF_MONTH, lastDay-1);
			String lastday = String.valueOf(cal.get(Calendar.DATE));
			String thismonth=String.valueOf(calendar.get(Calendar.MONTH)+1);
			if(thismonth.length()==1){
				thismonth="0"+thismonth;
			}
			calendar.add(Calendar.MONTH,-1);
			String lastmonth=String.valueOf(calendar.get(Calendar.MONTH)+1);
			if(lastmonth.length()==1){
				lastmonth="0"+lastmonth;
			}

			calendar.add(Calendar.MONTH,+2);
			String nextmonth=String.valueOf(calendar.get(Calendar.MONTH)+1);
			if(nextmonth.length()==1){
				nextmonth="0"+nextmonth;
			}
			mngsmxjgDto.setThismonth(s_year+thismonth);

			if("1".equals(day)){
				if("01".equals(thismonth)){
					if(lastday.equals(s_day)&&mngsmxjgService.existsTable("igams_mngsmxjg_"+(Integer.parseInt(s_year)-1)+lastmonth)==1){
						mngsmxjgDto.setLastmonth((Integer.parseInt(s_year)-1)+nextmonth);
					}
				}else{
					if(lastday.equals(s_day)&&mngsmxjgService.existsTable("igams_mngsmxjg_"+s_year+lastmonth)==1){
						mngsmxjgDto.setLastmonth(s_year+lastmonth);
					}

				}

			}
//			if("12".equals(thismonth)){
//				if(lastday.equals(data.substring(6,8))&&mngsmxjgService.existsTable("igams_mngsmxjg_"+(Integer.valueOf(data.substring(0,4))+1)+nextmonth)==1){
//					mngsmxjgDto.setLastmonth((Integer.valueOf(data.substring(0,4))+1)+nextmonth);
//				}
//
//			}else{
//				if(lastday.equals(data.substring(6,8))&&mngsmxjgService.existsTable("igams_mngsmxjg_"+data.substring(0,4)+nextmonth)==1){
//					mngsmxjgDto.setLastmonth(data.substring(0,4)+nextmonth);
//				}
//
//			}

			if(strings.size()>0){
				if(strings.size()>100){
					for(int a=1;a<=strings.size();a++){
						strings1.add(strings.get(a-1));
						if(a!=0&&(a%100==0)){
							mngsmxjgDto.setIds(strings1);
							mngsmxjgService.updateListFz(mngsmxjgDto);
							strings1.clear();
						}

						if(a>strings.size()-1&&strings1.size()>0){
							mngsmxjgDto.setIds(strings1);
							mngsmxjgService.updateListFz(mngsmxjgDto);
						}
					}

				}else{
					mngsmxjgService.updateListFz(mngsmxjgDto);
				}
			}
		return map;
	}

	/***
	 * 解析final.report文件
	 */

	private Map<String,Object> FileParseing_Final_report(String filePath) throws IOException {
		//读取文件
		FileReader fr=new FileReader(filePath);
		BufferedReader br=new BufferedReader(fr);
		String line;
		String[] arrs;
		List<Map<String,Object>> list=new ArrayList<>();
		Map<String,Object> taxidmap=new HashMap<>();
		while ((line=br.readLine())!=null) {
			arrs=line.split("\t");
			List<String>stringList= Arrays.asList(arrs);
			Map<String,Object>map=new HashMap<>();
			map.put("ratio",stringList.get(0));
			map.put("readsaccum",stringList.get(1));
			map.put("readscount",stringList.get(2));
			map.put("rankcode",stringList.get(3));
			map.put("taxid",stringList.get(4));
			map.put("name",stringList.get(5));

			if(stringList.get(4).equals("9606")||stringList.get(4).equals("2")||stringList.get(4).equals("2157")||stringList.get(4).equals("4751")||stringList.get(4).equals("10239")||stringList.get(4).equals("0")||stringList.get(4).equals("16776960")){
				taxidmap.put(stringList.get(4),stringList.get(1));
			}
			list.add(map);
		}
		taxidmap.put("list",list);
		br.close();
		fr.close();
		return taxidmap;
	}

	/***
	 * 解析mt结果文件
	 */
	private void fileParsing_mtResult(String path,String bbh,String wkbh,String xpm) throws Exception {
			File file = new File(path);
			String jsonString = FileUtils.readFileToString(file, "UTF-8");
			org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
			List<MngsmxjgDto> mngsmxjgList = new ArrayList<>();
			List<MngsmxjgDto> mngsmxjgList1 = new ArrayList<>();
			List<MngsmxjgDto> gzdMngsmxjgList = new ArrayList<>();
			WkcxbbDto hbwkcx=new WkcxbbDto();
			hbwkcx.setWkbh(wkbh);
			hbwkcx.setXpm(xpm);
			WkcxbbDto dtoNew = wkcxbbService.getZxbbDtoByXpAndWKbh(hbwkcx);
			if(dtoNew==null){
				log.error("mt文件解析时候芯片名称"+xpm+"文库"+wkbh+"没有找到");
				throw new Exception("dtoNew没有找到");
			}
			//获取所有的key
			Iterator<?> keys = jsonObject.keys();
			WkcxDto wkcxDto = new WkcxDto();
			wkcxDto.setWkbh(wkbh);
			wkcxDto.setXpm(xpm);
			OtherDto otherDto = new OtherDto();
			String[] split = wkbh.split("-");
			boolean flagNbbm=false;
			if (wkbh.startsWith("MDL")){
				if (!"PC".equals(split[1]) && !"NC".equals(split[1])){
					otherDto.setNbbm(split[1]);
				}else{
					flagNbbm=true;
					for(int x=split.length-1;x>=0;x--){
						if("DNA".equals(split[x])||"HS".equals(split[x])||"RNA".equals(split[x])||"tNGS".equals(split[x])
								||"TBtNGS".equals(split[x])||"Onco".equals(split[x])||"XD".equals(split[x])){
							String nbbm="";

							for(int i=1;i<x;i++){
								if(StringUtil.isBlank(nbbm)){
									nbbm+=split[i];
								}else{
									nbbm+="-"+split[i];
								}
							}
							otherDto.setNbbm(nbbm);
							break;
						}
					}
				}
			}else{
				for(int x=split.length-1;x>=0;x--){
					if("DNA".equals(split[x])||"HS".equals(split[x])||"RNA".equals(split[x])||"tNGS".equals(split[x])
							||"TBtNGS".equals(split[x])||"Onco".equals(split[x])||"XD".equals(split[x])){
						StringBuffer s_splitString = new StringBuffer("");
						for(int y=0;y<x;y++){
							if(y!=0)
								s_splitString.append("-");
							s_splitString.append(split[y]);
						}
						otherDto.setNbbm(s_splitString.toString());
						break;
					}
				}
				if(StringUtil.isBlank(otherDto.getNbbm()))
					otherDto.setNbbm(split[0]);
			}
			OtherDto sjxxDto = null;
			List<OtherDto> sjxxDtoList = otherService.getSjxxDtoList(otherDto);
		for(OtherDto _dto:sjxxDtoList){
				if(wkbh.contains(_dto.getNbbm())){
					sjxxDto=_dto;
					break;
				}
			}

			String yblxdm="";
			if (null != sjxxDto){
				wkcxDto.setSjid(sjxxDto.getSjid());
				if(flagNbbm){
					yblxdm=split[1].substring(split[1].length()-1);
				}else{
					yblxdm=sjxxDto.getNbbm().substring(sjxxDto.getNbbm().length()-1);
				}
			}
			String jcxm="";
			if (wkbh.contains("-DNA-RNA")){
				wkcxDto.setWklx("DNA");
				jcxm = "D";
			}else if (wkbh.contains("-RNA-DNA")){
				wkcxDto.setWklx("RNA");
				jcxm = "R";
			}else if (wkbh.contains("-DNA-")||wkbh.contains("-Onco-")||wkbh.contains("-XD-")||wkbh.contains("-HS-")||
					wkbh.contains("-tNGS-")||wkbh.contains("-TBtNGS-")){
				wkcxDto.setWklx("DNA");
				jcxm = "D";
			}else if (wkbh.contains("-RNA-")){
				wkcxDto.setWklx("RNA");
				jcxm = "R";
			}else if (wkbh.contains("-DR-")){
				if (sjxxDto != null && StringUtil.isNotBlank(sjxxDto.getNbbm()) && sjxxDto.getNbbm().contains("01D")) {
					wkcxDto.setWklx("DNA");
					jcxm = "D";
				}
				if (sjxxDto != null && StringUtil.isNotBlank(sjxxDto.getNbbm()) && sjxxDto.getNbbm().contains("01R")) {
					wkcxDto.setWklx("RNA");
					jcxm = "R";
				}
			}else{
				wkcxDto.setWklx("DNA");
				jcxm = "D";
			}
			List<String> arrList= Arrays.asList(taxidArr);
			while(keys.hasNext()){
				String key=keys.next().toString();
				MngsmxjgDto mngsmxjgDto = new MngsmxjgDto();
				//除脑脊液以外的DNA文库（不包括REM文库），
				// 检出{470, 287, 573, 1280, 562, 354276, 40324, 727, 83558, 2104, 1313, 480}这些taxid
				// 并且sig reads>0, barcode match>0的时候，默认关注
				if("D".equals(jcxm)&&!wkbh.contains("REM")&&!"N".equals(yblxdm)){
					if(arrList.stream().filter(x -> x.equals(key)).findFirst().isPresent()){
						JSONObject mtOjb=JSONObject.parseObject(jsonObject.get(key).toString());
						if(mtOjb.get("barcode_all_match")!=null&&Long.parseLong(mtOjb.get("barcode_all_match").toString())>0&&mtOjb.get("signification_accum")!=null&&Long.parseLong(mtOjb.get("signification_accum").toString())>0){
							MngsmxjgDto gzdmngsmxjgDto = new MngsmxjgDto();
							gzdmngsmxjgDto.setTaxid(key);
							gzdmngsmxjgDto.setAijg("3");
							gzdmngsmxjgDto.setBbh(bbh);
							gzdmngsmxjgDto.setWkcxid(dtoNew.getWkcxid());
							gzdmngsmxjgDto.setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(5,7));
							gzdmngsmxjgDto.setWkbh(wkbh);
							gzdMngsmxjgList.add(gzdmngsmxjgDto);
						}
					}
				}
				mngsmxjgDto.setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(5,7));
				mngsmxjgDto.setTaxid(key);
				mngsmxjgDto.setJtxlxx(String.valueOf(jsonObject.get(key))  );
				mngsmxjgDto.setBbh(bbh);
				mngsmxjgDto.setWkbh(wkbh);
				mngsmxjgDto.setWkcxid(dtoNew.getWkcxid());
				mngsmxjgList.add(mngsmxjgDto);
			}
			if(gzdMngsmxjgList.size()>0){
				mngsmxjgService.batchUpdateLs(gzdMngsmxjgList);
				mngsmxjgService.updateGzdToAijgList(gzdMngsmxjgList);
				MngsmxjgDto mngsmxjgDto=new MngsmxjgDto();
				mngsmxjgDto.setWkbh(gzdMngsmxjgList.get(0).getWkbh());
				mngsmxjgDto.setWkcxid(gzdMngsmxjgList.get(0).getWkcxid());
				mngsmxjgDto.setBbh(gzdMngsmxjgList.get(0).getBbh());
				mngsmxjgDto.setLrsj(gzdMngsmxjgList.get(0).getLrsj());
				mngsmxjgService.updateGzdToFour(mngsmxjgDto);
			}
			if(mngsmxjgList.size()>0){
				if(mngsmxjgList.size()>100){
					for(int a=1;a<=mngsmxjgList.size();a++){
						mngsmxjgList1.add(mngsmxjgList.get(a-1));
						if(a!=0&&a%100==0){
							mngsmxjgService.batchUpdateLs(mngsmxjgList1);
							mngsmxjgList1.clear();
						}

						if(a>mngsmxjgList.size()-1&&mngsmxjgList1.size()>0){
							mngsmxjgService.batchUpdateLs(mngsmxjgList1);
						}
					}

				}else{
						mngsmxjgService.batchUpdateLs(mngsmxjgList);
				}
			}
	}

	/**
	 * 新冠分型文件解析
	 */
	private void fileParsing_fx(String path,String wkbh,String xpm)throws Exception{
		File file = new File(path);
		String jsonString = FileUtils.readFileToString(file, "UTF-8");
		JSONObject jsonObject=JSONObject.parseObject(jsonString);
		if(jsonObject!=null&&jsonObject.size()>0){
			List<MngsmxjgDto>list=new ArrayList<>();
			WkcxbbDto hbwkcx=new WkcxbbDto();
			hbwkcx.setWkbh(wkbh);
			hbwkcx.setXpm(xpm);
			WkcxbbDto dtoNew = wkcxbbService.getZxbbDtoByXpAndWKbh(hbwkcx);
			if(dtoNew==null){
				log.error("新冠分析解析时候芯片名称"+xpm+"文库"+wkbh+"没有找到");
				throw new Exception("dtoNew没有找到");
			}
			MngsmxjgDto mngsmxjgDto= new MngsmxjgDto();
			mngsmxjgDto.setWkcxid(dtoNew.getWkcxid());
			mngsmxjgDto.setWkbh(wkbh);
			mngsmxjgDto.setBbh(dtoNew.getBbh());
			mngsmxjgDto.setTaxid(jsonObject.get("uid").toString());
			mngsmxjgDto.setFx(jsonObject.get("clade").toString());
			mngsmxjgDto.setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(5,7));
			list.add(mngsmxjgDto);
			mngsmxjgService.updateBjtppath(list);

		}
	}

	/***
	 * bg.json文件解析
	 */
	private void fileParsing_bg(String path,String wkbh,String xpm)throws Exception{
		File file = new File(path);
		String jsonString = FileUtils.readFileToString(file, "UTF-8");
		JSONObject jsonObject=JSONObject.parseObject(jsonString);
		if(jsonObject!=null&&jsonObject.size()>0){
			List<MngsmxjgDto>list=new ArrayList<>();
			WkcxbbDto hbwkcx=new WkcxbbDto();
			hbwkcx.setWkbh(wkbh);
			hbwkcx.setXpm(xpm);
			WkcxbbDto dtoNew = wkcxbbService.getZxbbDtoByXpAndWKbh(hbwkcx);
			if(dtoNew==null){
				log.error("bg文件解析时候芯片名称"+xpm+"文库"+wkbh+"没有找到");
				throw new Exception("dtoNew没有找到");
			}
			for(String str:jsonObject.keySet()){
				MngsmxjgDto mngsmxjgDto= new MngsmxjgDto();
				mngsmxjgDto.setWkcxid(dtoNew.getWkcxid());
				mngsmxjgDto.setWkbh(wkbh);
				mngsmxjgDto.setBbh(dtoNew.getBbh());
				mngsmxjgDto.setTaxid(str);
				mngsmxjgDto.setBg(jsonObject.get(str).toString());
				mngsmxjgDto.setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(5,7));
				list.add(mngsmxjgDto);
			}
			mngsmxjgService.updateBjtppath(list);

		}

	}
	/***
	 * 解析毒力报告结果
	 */
	private void fileParsing_vfdb_stat1(String path, String wkbh, String xpm) throws Exception {
			FileReader fr=new FileReader(path);
			BufferedReader br=new BufferedReader(fr);
			String line;
			String[] arrs;
			List<DlfxjgDto> dlfxjglist = new ArrayList<>();
			WkcxbbDto hbwkcx=new WkcxbbDto();
			hbwkcx.setWkbh(wkbh);
			hbwkcx.setXpm(xpm);
			WkcxbbDto dtoNew = wkcxbbService.getZxbbDtoByXpAndWKbh(hbwkcx);
			if(dtoNew==null){
				log.error("vfdb1文件解析时候芯片名称"+xpm+"文库"+wkbh+"没有找到");
				br.close();
				throw new Exception("dtoNew没有找到");
			}
			while ((line=br.readLine())!=null) {
				if (line.contains("毒力因子") && line.contains("毒力相关类别") && line.contains("毒力基因") && line.contains("比对reads数") && line.contains("VFDB参考物种")){
					continue;
				}
				if("[]".equals(line)){
					break;
				}
				arrs=line.replace(" ","").split("\t");
//			System.out.println(arrs[0] + " : " + arrs[1] + " : " + arrs[2]);
				List<String> list= Arrays.asList(arrs);
				org.json.JSONObject jsonObject = new org.json.JSONObject();
				if (list.size()>0){
					jsonObject.put("dlyz",list.get(0));
					jsonObject.put("dlxglb",list.get(1));
					jsonObject.put("dljy",list.get(2));
					jsonObject.put("dbreads",list.get(3));
					jsonObject.put("vfdb",list.get(4));
				}

				DlfxjgDto dlfxjgDto = new DlfxjgDto();
				dlfxjgDto.setDlfxid(StringUtil.generateUUID());
				dlfxjgDto.setBbh(dtoNew.getBbh());
				dlfxjgDto.setSfbg("1");
				dlfxjgDto.setNr(String.valueOf(jsonObject));
				if (StringUtil.isNotBlank(dtoNew.getWkcxid())){
					dlfxjgDto.setWkcxid(dtoNew.getWkcxid());
				}
				dlfxjglist.add(dlfxjgDto);
			}
			if(dlfxjglist.size()>0){
				dlfxjgService.insertList(dlfxjglist);
			}
			br.close();
			fr.close();
	}
	private void fileParsing_vfdb_stat(String path, String wkbh, String xpm) throws Exception {
			File file = new File(path);
			String jsonString = FileUtils.readFileToString(file, "UTF-8");
			JSONArray jsonArray = JSON.parseArray(jsonString);
			if(jsonArray!=null&&jsonArray.size()>0){
				List<DlfxjgDto> dlfxjglist = new ArrayList<>();
				WkcxbbDto hbwkcx=new WkcxbbDto();
				hbwkcx.setWkbh(wkbh);
				hbwkcx.setXpm(xpm);
				WkcxbbDto dtoNew = wkcxbbService.getZxbbDtoByXpAndWKbh(hbwkcx);
				if(dtoNew==null){
					log.error("vfdb文件解析时候芯片名称"+xpm+"文库"+wkbh+"没有找到");
					throw new Exception("dtoNew没有找到");
				}
				for(int i=0;i<jsonArray.size();i++){
					JSONObject job=JSON.parseObject(jsonArray.getString(i));
					org.json.JSONObject jsonObject = new org.json.JSONObject();
					jsonObject.put("dlyz",job.get("virulence_factor"));
					jsonObject.put("dlxglb",job.get("virulence_category"));
					jsonObject.put("dljy",job.get("gene"));
					jsonObject.put("dbreads",job.get("reads_count"));
					jsonObject.put("vfdb",job.get("related_species"));

					DlfxjgDto dlfxjgDto = new DlfxjgDto();
					dlfxjgDto.setDlfxid(StringUtil.generateUUID());
					dlfxjgDto.setBbh(dtoNew.getBbh());
					dlfxjgDto.setSfbg("1");
					dlfxjgDto.setNr(String.valueOf(jsonObject));
					if (StringUtil.isNotBlank(dtoNew.getWkcxid())){
						dlfxjgDto.setWkcxid(dtoNew.getWkcxid());
					}
					dlfxjglist.add(dlfxjgDto);
				}
				if(dlfxjglist.size()>0){
					dlfxjgService.insertList(dlfxjglist);
				}
			}


	}
	/***
	 * 耐药结果文件分析
	 */
	private void fileParsingCard_stat1 (String path, String wkbh, String xpm) throws Exception {
			FileReader fr = new FileReader(path);
			BufferedReader br=new BufferedReader(fr);
			String line;
			String[] arrs;
			List<NyfxjgDto> nyfxjgDtoList = new ArrayList<>();
			Map<String,Integer> resmap = new HashMap<>();
			WkcxbbDto hbwkcx=new WkcxbbDto();
			hbwkcx.setWkbh(wkbh);
			hbwkcx.setXpm(xpm);
			WkcxbbDto dtoNew = wkcxbbService.getZxbbDtoByXpAndWKbh(hbwkcx);
			if(dtoNew==null){
				log.error("card1文件解析时候芯片名称"+xpm+"文库"+wkbh+"没有找到");
				br.close();
				throw new Exception("dtoNew没有找到");
			}
			//耐药类别	耐药家族	重点耐药基因	top5基因	top5基因的检出信息	参考物种	参考物种TaxID	耐药机制
			while ((line=br.readLine())!=null) {
				if (  line.contains("耐药类别") && line.contains("耐药家族") && line.contains("重点耐药基因") && line.contains("top5基因") && line.contains("top5基因的检出信息")
						&& line.contains("参考物种")  && line.contains("参考物种TaxID")  && line.contains("耐药机制") ){
					continue;
				}
				if("[]".equals(line)){
					break;
				}
				arrs=line.replace(" ","").split("\t");
				List<String> list= Arrays.asList(arrs);
				org.json.JSONObject jsonObject = new org.json.JSONObject();


				NyfxjgDto nyfxjgDto = new NyfxjgDto();
				nyfxjgDto.setNyjgid(StringUtil.generateUUID());
				if (StringUtil.isNotBlank(dtoNew.getWkcxid())){
					nyfxjgDto.setWkcxid(dtoNew.getWkcxid());
				}
				if (list.size()>0){
					jsonObject.put("nylb",list.get(0));
					jsonObject.put("nyjzu",list.get(1));
					jsonObject.put("zdnyjy",list.get(2));
					jsonObject.put("topjy",list.get(3));
					jsonObject.put("topjydjcxx",list.get(4));
					jsonObject.put("ckwz",list.get(5));
					jsonObject.put("ckwztaxid",list.get(6));
					jsonObject.put("nyjz",list.get(7));
					nyfxjgDto.setNylb(list.get(0));
					nyfxjgDto.setNyjzu(list.get(1));
				}
				nyfxjgDto.setNr(String.valueOf(jsonObject));
				nyfxjgDto.setBbh(dtoNew.getBbh());
				nyfxjgDto.setSfbg("1");
				nyfxjgDto.setNyjy(String.valueOf(jsonObject.get("topjy")));
				if (resmap.containsKey((nyfxjgDto.getNyjzu()))){
					//map存在该值，即存在重复耐药家族数据，处理以后在放入list中
					for (int i=0; i<nyfxjgDtoList.size(); i++) {
						NyfxjgDto dto = nyfxjgDtoList.get(i);
						if (nyfxjgDto.getNyjzu().equals(dto.getNyjzu())){
							String nylb = nyfxjgDto.getNylb()+";"+dto.getNylb();
							nyfxjgDto.setNylb(nylb);
							org.json.JSONObject object = new org.json.JSONObject(dto.getNr());
							object.put("nylb",nylb);
							nyfxjgDto.setNr(String.valueOf(object));
							nyfxjgDtoList.set(i,nyfxjgDto);
						}
					}
				}else {
					resmap.put(nyfxjgDto.getNyjzu(),1);
					nyfxjgDtoList.add(nyfxjgDto);
				}
			}
			if (nyfxjgDtoList.size()>0){
				nyfxjgService.insertList(nyfxjgDtoList);
			}
			br.close();
			fr.close();
	}

	private void fileParsingCard_stat (String path, String wkbh, String xpm) throws Exception {

			File file = new File(path);
			String jsonString = FileUtils.readFileToString(file, "UTF-8");
			JSONArray jsonArray = JSON.parseArray(jsonString);
			if(jsonArray!=null&&jsonArray.size()>0){
				//获取所有的key
				List<NyfxjgDto> nyfxjgDtoList = new ArrayList<>();
				Map<String,Integer> resmap = new HashMap<>();
				WkcxbbDto hbwkcx=new WkcxbbDto();
				hbwkcx.setWkbh(wkbh);
				hbwkcx.setXpm(xpm);
				WkcxbbDto dtoNew = wkcxbbService.getZxbbDtoByXpAndWKbh(hbwkcx);
				if(dtoNew==null){
					log.error("card文件解析时候芯片名称"+xpm+"文库"+wkbh+"没有找到");
					throw new Exception("dtoNew没有找到");
				}
				//耐药类别	耐药家族	重点耐药基因	top5基因	top5基因的检出信息	参考物种	参考物种TaxID	耐药机制
				for(int x=0;x<jsonArray.size();x++){
					JSONObject job=JSON.parseObject(jsonArray.getString(x));
					org.json.JSONObject jsonObjectny = new org.json.JSONObject();


					NyfxjgDto nyfxjgDto = new NyfxjgDto();
					nyfxjgDto.setNyjgid(StringUtil.generateUUID());
					if (StringUtil.isNotBlank(dtoNew.getWkcxid())){
						nyfxjgDto.setWkcxid(dtoNew.getWkcxid());
					}
					jsonObjectny.put("nylb",job.get("drug_class"));
					jsonObjectny.put("nyjzu",job.get("gene_family"));
					jsonObjectny.put("zdnyjy",job.get("top_gene"));
					jsonObjectny.put("topjy",job.get("genes"));
					jsonObjectny.put("topjydjcxx",job.get("reads"));
					jsonObjectny.put("ckwz",job.get("ref_species"));
					jsonObjectny.put("ckwztaxid",job.get("taxid"));
					jsonObjectny.put("nyjz",job.get("main_mechanism"));
					nyfxjgDto.setNylb(job.get("drug_class")+"");
					nyfxjgDto.setNyjzu(job.get("gene_family")+"");
					nyfxjgDto.setNr(String.valueOf(jsonObjectny));
					nyfxjgDto.setBbh(dtoNew.getBbh());
					nyfxjgDto.setSfbg("1");
					nyfxjgDto.setNyjy(String.valueOf(jsonObjectny.get("topjy")));
					if (resmap.containsKey((nyfxjgDto.getNyjzu()))){
						//map存在该值，即存在重复耐药家族数据，处理以后在放入list中
						for (int i=0; i<nyfxjgDtoList.size(); i++) {
							NyfxjgDto dto = nyfxjgDtoList.get(i);
							if (nyfxjgDto.getNyjzu().equals(dto.getNyjzu())){
								String nylb = nyfxjgDto.getNylb()+";"+dto.getNylb();
								nyfxjgDto.setNylb(nylb);
								org.json.JSONObject object = new org.json.JSONObject(dto.getNr());
								object.put("nylb",nylb);
								nyfxjgDto.setNr(String.valueOf(object));
								nyfxjgDtoList.set(i,nyfxjgDto);
							}
						}
					}else {
						resmap.put(nyfxjgDto.getNyjzu(),1);
						nyfxjgDtoList.add(nyfxjgDto);
					}
				}

				if (nyfxjgDtoList.size()>0){
					nyfxjgService.insertList(nyfxjgDtoList);
				}
			}

	}
	/***
	 * ai结果文件分析
	 */
	private void fileParsingAi (String path, String wkbh, String xpm) throws Exception {
			FileReader fr = new FileReader(path);
			BufferedReader br=new BufferedReader(fr);
			String line;
			String[] arrs;
			List<MngsmxjgDto> mngsmxjgDtoList = new ArrayList<>();
			List<MngsmxjgDto> mngsmxjgDtoList1 = new ArrayList<>();
			WkcxbbDto hbwkcx=new WkcxbbDto();
			hbwkcx.setWkbh(wkbh);
			hbwkcx.setXpm(xpm);
			WkcxbbDto dtoNew = wkcxbbService.getZxbbDtoByXpAndWKbh(hbwkcx);
			if(dtoNew==null){
				log.error("ai文件解析时候芯片名称"+xpm+"文库"+wkbh+"没有找到");
				br.close();
				throw new Exception("dtoNew没有找到");
			}
			List<MngsmxjgDto> gzdMngsmxjgList=new ArrayList<>();
			//耐药类别	耐药家族	重点耐药基因	top5基因	top5基因的检出信息	参考物种	参考物种TaxID	耐药机制
			while ((line=br.readLine())!=null) {
				if (  line.contains("Sample") && line.contains("Taxid") && line.contains("Decision") && line.contains("Type") && line.contains("Library")
						&& line.contains("reads_count")  && line.contains("rab")  && line.contains("rpm") ){
					continue;
				}
				arrs=line.replace(" ","").split("\t");
				List<String> list= Arrays.asList(arrs);

				//Sample	Taxid	Decision	Type	Library	reads_count	rab	rpm	Srpm	Qindex	Hindex	Bmp	Tax_rank
				// Chip_rank	History_rank	Score	Cla

				MngsmxjgDto mngsmxjgDto = new MngsmxjgDto();
				if (list.size()>0){
					mngsmxjgDto.setTaxid(list.get(1));

//					mngsmxjgDto.setGzd(list.get(2));
					if (StringUtil.isNotBlank(list.get(2))){

						if ("High".equals(list.get(2))){
							mngsmxjgDto.setAijg("1");
						}else if ("background".equals(list.get(2))){
							mngsmxjgDto.setAijg("3");
						}else if ("Low".equals(list.get(2))){
							mngsmxjgDto.setAijg("2");
						}else {
							mngsmxjgDto.setAijg("5");
						}
					}else {
						mngsmxjgDto.setAijg("5");
					}
					mngsmxjgDto.setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(5,7));

				}
				mngsmxjgDto.setBbh(dtoNew.getBbh());
				mngsmxjgDto.setWkbh(wkbh);
				mngsmxjgDto.setWkcxid(dtoNew.getWkcxid());
				mngsmxjgDtoList.add(mngsmxjgDto);
				mngsmxjgDto.setTaxid(list.get(1));
				gzdMngsmxjgList.add(mngsmxjgDto);
				System.out.println(list);
			}
			if(mngsmxjgDtoList.size()>0){
				if(mngsmxjgDtoList.size()>100){
					for(int a=1;a<=mngsmxjgDtoList.size();a++){
						mngsmxjgDtoList1.add(mngsmxjgDtoList.get(a-1));
						if(a!=0&&a%100==0){
							mngsmxjgService.batchUpdateLs(mngsmxjgDtoList1);
							mngsmxjgDtoList1.clear();
						}
						if(a>mngsmxjgDtoList.size()-1&&mngsmxjgDtoList1.size()>0){
							mngsmxjgService.batchUpdateLs(mngsmxjgDtoList1);
						}
					}
				}else{
					mngsmxjgService.batchUpdateLs(mngsmxjgDtoList);
				}

			}
			//修改临时表中gzd字段值为2（代表不关注），且fl为0核心库的则将gzd改为4疑似
			MngsmxjgDto updateDto =new MngsmxjgDto();
			updateDto.setBbh(dtoNew.getBbh());
			updateDto.setWkbh(wkbh);
			updateDto.setWkcxid(dtoNew.getWkcxid());
			updateDto.setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(5,7));

			//mngsmxjgService.updateGzdToAijg(updateDto);
			mngsmxjgService.updateGzdToAijgList(gzdMngsmxjgList);
			mngsmxjgService.updateGzdToFour(updateDto);

			br.close();
			fr.close();
	}

	/***
	 * 解析coverage.json
	 */
	private void fileParsingCov(String path, String wkbh, String chipId) throws Exception{
			File file = new File(path);
			String jsonString = FileUtils.readFileToString(file, "UTF-8");
			//JSONArray jsonArray = JSON.parseArray(jsonString);
			org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
			List<MngsmxjgDto> mngsmxjgList = new ArrayList<>();
			List<MngsmxjgDto> mngsmxjgList_bj = new ArrayList<>();
			List<MngsmxjgDto> mngsmxjgList1 = new ArrayList<>();
			//获取所有的key
			Iterator<?> keys = jsonObject.keys();
			WkcxbbDto hbwkcx=new WkcxbbDto();
			hbwkcx.setWkbh(wkbh);
			hbwkcx.setXpm(chipId);
			WkcxbbDto dtoNew = wkcxbbService.getZxbbDtoByXpAndWKbh(hbwkcx);
			if(dtoNew==null){
				log.error("cov文件解析时候芯片名称"+chipId+"文库"+wkbh+"没有找到");
				throw new Exception("dtoNew没有找到");
			}
//			WkcxDto  getXpDto=new WkcxDto();
//			getXpDto.setWkbh(wkbh);
			//WkcxDto xpmDto=wkcxService.getXpmBywkhb(getXpDto);
			String savepath=releasePath+ BusTypeEnum.IMP_BIOINFORMATION.getCode()+"/UP"+dtoNew.getLrsj().substring(0,4)+"/UP"+dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(5,7)+"/UP"+dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(5,7)+dtoNew.getLrsj().substring(8,10)+"/"+chipId+"/"+wkbh+"/bjimg/";
			String miniopath=dtoNew.getLrsj().substring(0,4)+"/"+dtoNew.getLrsj().substring(5,7)+"/"+dtoNew.getLrsj().substring(8,10)+"/"+chipId+"/"+wkbh+"/coverage_plot/";

			OtherDto sjxxDto = null;
			OtherDto otherDto = new OtherDto();
			String[] split = wkbh.split("-");
			if (wkbh.startsWith("MDL")){
				if (!"PC".equals(split[1]) && !"NC".equals(split[1])){
					otherDto.setNbbm(split[1]);
				}else{
					for(int x=split.length-1;x>=0;x--){
						if("DNA".equals(split[x])||"HS".equals(split[x])||"RNA".equals(split[x])||"tNGS".equals(split[x])
								||"TBtNGS".equals(split[x])||"Onco".equals(split[x])||"XD".equals(split[x])){
							String nbbm="";

							for(int i=1;i<x;i++){
								if(StringUtil.isBlank(nbbm)){
									nbbm+=split[i];
								}else{
									nbbm+="-"+split[i];
								}
							}
							otherDto.setNbbm(nbbm);
							break;
						}
					}
				}
			}else{
				for(int x=split.length-1;x>=0;x--){
					if("DNA".equals(split[x])||"HS".equals(split[x])||"RNA".equals(split[x])||"tNGS".equals(split[x])
							||"TBtNGS".equals(split[x])||"Onco".equals(split[x])||"XD".equals(split[x])){
						StringBuffer s_splitString = new StringBuffer("");
						for(int y=0;y<x;y++){
							if(y!=0)
								s_splitString.append("-");
							s_splitString.append(split[y]);
						}
						otherDto.setNbbm(s_splitString.toString());
						break;
					}
				}
				if(StringUtil.isBlank(otherDto.getNbbm()))
					otherDto.setNbbm(split[0]);
			}
			List<OtherDto> sjxxDtoList = otherService.getSjxxDtoList(otherDto);
			for(OtherDto _dto:sjxxDtoList){
				if(wkbh.contains(_dto.getNbbm())){
					sjxxDto=_dto;
					break;
				}
			}
			RestTemplate restTemplate = new RestTemplate();
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("ybbh", sjxxDto==null?"":sjxxDto.getYbbh());
			String url=applicationurl+"/ws/getApplyByCode/";
			//log.error("BlastUrl: "+url);
			Map<String,Object> resultMap;
			resultMap=  restTemplate.postForObject(url, paramMap, Map.class);
			JSONObject jsonObject1=null;
			if(resultMap!=null&&((resultMap.get("bgfsdm")!=null&&("BJD".equals(resultMap.get("bgfsdm").toString())||"BJ".equals(resultMap.get("bgfsdm").toString())))||(resultMap.get("db")!=null&&resultMap.get("db").toString().contains("博精")))){
				MinioClient minioClient;
				try {
					minioClient = new MinioClient("https://minio.matridx.com", "ituser", "Matridx@2022");
					InputStream inputStream = minioClient.getObject("mngs", miniopath + "coverage.json");
					byte[] buffer = new byte[1024];
					BufferedInputStream bis;
					bis = new BufferedInputStream(inputStream);
					int i;
					StringBuffer cover = new StringBuffer();
					while ((i = bis.read(buffer)) != -1) {
						cover.append(new String(buffer, 0, i));
					}
					bis.close();
					System.out.println(cover);
					jsonObject1 = JSONObject.parseObject(cover.toString());
				}catch (Exception e){
						throw new Exception("dtoNew没有找到,minio错误");
					}

			}
			while(keys.hasNext()){
				String key=keys.next().toString();
				MngsmxjgDto mngsmxjgDto = new MngsmxjgDto();
				mngsmxjgDto.setTaxid(key);
				mngsmxjgDto.setBbh(dtoNew.getBbh());
				mngsmxjgDto.setWkbh(wkbh);
				mngsmxjgDto.setWkcxid(dtoNew.getWkcxid());
				mngsmxjgDto.setFgdxx(String.valueOf(jsonObject.get(key))  );
				mngsmxjgDto.setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(5,7));
				if(jsonObject1!=null&&jsonObject1.get(key)!=null){
					JSONObject xxObject=JSONObject.parseObject(jsonObject1.get(key).toString());
					JSONObject covObject= JSONObject.parseObject(jsonObject.get(key).toString());
					covObject.put("coverage",xxObject.get("coverage"));
					covObject.put("cover_length",xxObject.get("cover_length"));
					covObject.put("depth",xxObject.get("depth"));
					mngsmxjgDto.setFgdxx(String.valueOf(covObject)  );
					MinioClient minioClient;
					try{
						minioClient = new MinioClient("https://minio.matridx.com", "ituser", "Matridx@2022");
						InputStream inputStream = minioClient.getObject("mngs",miniopath+key+".png");
						File toFile;
						toFile = new File(savepath + File.separator + key+".png");
						String savepath1 = toFile.getCanonicalPath();
						mngsmxjgDto.setBjtppath(savepath1);
						/*判断路径中的文件夹是否存在，如果不存在，先创建文件夹*/
						String dirPath = savepath1.substring(0, savepath1.lastIndexOf(File.separator));
						File dir = new File(dirPath);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						OutputStream os = new FileOutputStream(toFile);
						int bytesRead;
						byte[] buffer = new byte[8192];
						while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
							os.write(buffer, 0, bytesRead);
						}
						os.close();
						inputStream.close();
						mngsmxjgList_bj.add(mngsmxjgDto);
					}catch (Exception e){
						throw new Exception("dtoNew没有找到");
					}


				}
				mngsmxjgList.add(mngsmxjgDto);

			}
			if(mngsmxjgList_bj.size()>0){
					mngsmxjgService.updateBjtppath(mngsmxjgList_bj);
			}

			if(mngsmxjgList.size()>0){
					if(mngsmxjgList.size()>100){
						for(int a=1;a<=mngsmxjgList.size();a++){
							mngsmxjgList1.add(mngsmxjgList.get(a-1));
							if(a!=0&&a%100==0){
								mngsmxjgService.batchUpdateLs(mngsmxjgList1);
								mngsmxjgList1.clear();
							}
							if(a>mngsmxjgList.size()-1&&mngsmxjgList1.size()>0){
								mngsmxjgService.batchUpdateLs(mngsmxjgList1);
							}
						}
					}else{
						mngsmxjgService.batchUpdateLs(mngsmxjgList);
					}
			}
			//查询igams_wkjgmx_ls的结果集添加到igams_wkjgmx中，然后情况igams_wkjgmx_ls表
//			MngsmxjgDto mngsmxjgDto = new MngsmxjgDto();
//			mngsmxjgDto.setBbh(dtoNew.getBbh());
//			mngsmxjgDto.setWkbh(wkbh);
//			mngsmxjgDto.setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(5,7));
			//List<MngsmxjgDto> mngsmxjgDtoList = mngsmxjgService.getLsAll(mngsmxjgDto);
//			List<MngsmxjgDto> mngsmxjgDtoList1=new ArrayList<>();
//			if(mngsmxjgDtoList.size()>=500){
//				for(int i=0 ;i<500;i++){
//					mngsmxjgDtoList.get(i).setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(5,7));
//					mngsmxjgDtoList1.add(mngsmxjgDtoList.get(i));
//				}
//				mngsmxjgService.insertLsAllToWkjgmx(mngsmxjgDtoList1);
//				mngsmxjgDtoList1.clear();
//				for(int i=500 ;i<mngsmxjgDtoList.size();i++){
//					mngsmxjgDtoList.get(i).setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(5,7));
//					mngsmxjgDtoList1.add(mngsmxjgDtoList.get(i));
//				}
//				mngsmxjgService.insertLsAllToWkjgmx(mngsmxjgDtoList1);//将临时表的数据插入到文库结果明细表
//			}else{
//				for (MngsmxjgDto dto : mngsmxjgDtoList) {
//					dto.setLrsj(dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(5,7));
//					mngsmxjgDtoList1.add(dto);
//				}
//				mngsmxjgService.insertLsAllToWkjgmx(mngsmxjgDtoList1);
//			}

			//mngsmxjgService.delLsAll(mngsmxjgDto);//删除临时表的所有数据

//			WkcxbbDto wkcxbbDto=new WkcxbbDto();
//			wkcxbbDto.setWkbh(wkbh);
//			wkcxbbDto.setBbh(dtoNew.getBbh());
//			wkcxbbDto=wkcxbbService.getDtoByBbhAndWkbh(wkcxbbDto);
//			MngsmxjgDto updateDto=new MngsmxjgDto();
//			updateDto.setJgid(wkcxbbDto.getJgid());
//			updateDto.setLrsj(wkcxbbDto.getLrsj().substring(0,4)+wkcxbbDto.getLrsj().substring(5,7));
//			List<MngsmxjgDto> mxjgList=mngsmxjgService.getDtoList(updateDto);
//			List<MngsmxjgDto> mngsmxjgDtos=new ArrayList<>();
//			for(MngsmxjgDto dtos:mxjgList){
//				if(StringUtil.isNotBlank(dtos.getGzd())){
//					mngsmxjgDtos.add(dtos);
//				}
//			}
//			updateDto.setList(mngsmxjgDtos);
//			List<MngsmxjgDto> lsDtos=mngsmxjgService.getDtoListLsCs(updateDto);
//			List<MngsmxjgDto> doUpdateList=new ArrayList<>();
//			List<MngsmxjgDto> doUpdateList1=new ArrayList<>();
//			for(MngsmxjgDto dtos:lsDtos){
//				if(StringUtil.isBlank(dtos.getGzd())){
//					dtos.setLrsj(wkcxbbDto.getLrsj().substring(0,4)+wkcxbbDto.getLrsj().substring(5,7));
//					doUpdateList.add(dtos);
//				}
//			}
//			if(doUpdateList.size()>100){
//				for(int a=1;a<=doUpdateList.size();a++){
//					doUpdateList1.add(doUpdateList.get(a-1));
//					if(a!=0&&a%100==0){
//						mngsmxjgService.updateGzdSetMr(doUpdateList1);
//						doUpdateList1.clear();
//					}
//					if(a>doUpdateList.size()-1&&doUpdateList1.size()>0){
//						mngsmxjgService.updateGzdSetMr(doUpdateList1);
//					}
//				}
//			}else{
//				mngsmxjgService.updateGzdSetMr(doUpdateList);
//			}
	}

	/***
	 * 解析dash.json文件
	 */
	private void fileParsing_dash(String path,String xpm) throws Exception {
			BioXpxxDto bioXpxxDto =new BioXpxxDto();
			bioXpxxDto.setXpm(xpm);
			BioXpxxDto xpxxDto = xpxxService.getDto(bioXpxxDto);
			File file = new File(path);
			String jsonString = FileUtils.readFileToString(file, "UTF-8");
			org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
			List<WkcxDto> wkcxDtos = new ArrayList<>();
			List<WkcxDto> wkcxDtos1 = new ArrayList<>();
			//获取所有的key
			Iterator<?> keys = jsonObject.keys();

			while(keys.hasNext()){
				String key=keys.next().toString();
				WkcxDto wkcxDto = new WkcxDto();
				wkcxDto.setWkbh(key);
				if(xpxxDto!=null){
					wkcxDto.setXpid(xpxxDto.getXpid());
				}
				JSONObject jsonObject_t = JSONObject.parseObject(String.valueOf(jsonObject.get(key)));
				wkcxDto.setRnareadsrate(jsonObject_t.getString("RNA_reads_rate"));
				wkcxDtos.add(wkcxDto);
			}

			if(wkcxDtos.size()>0){
				if(wkcxDtos.size()>100){
					for(int a=1;a<=wkcxDtos.size();a++){
						wkcxDtos1.add(wkcxDtos.get(a-1));
						if(a!=0&&a%100==0){
							wkcxService.updateList(wkcxDtos1);
							wkcxDtos1.clear();
						}
						if(a>wkcxDtos.size()-1&&wkcxDtos1.size()>0){
							wkcxService.updateList(wkcxDtos1);
						}
					}
				}else{
					wkcxService.updateList(wkcxDtos);
				}

			}
	}

	/***
	 * 组成树结构json
	 */
	private List<Map<String,Object>> tree_Final_report(List<Map<String,Object>> list) {
		//整理成树形json
		int treeIndex=1;//需要整理得第几层   ，前面几个空格
		List<Map<String,Object>> list1=new ArrayList<>(); //用于判断循环得
		for (int i=1;i<list.size();i++){
			List<Map<String,Object>> mapList= new ArrayList<>();
			list.get(i).put("children",mapList);
			list1.add(list.get(i));
		}
		Map<String,Object> map0=list.get(0);
		list.remove(0);
		list1.remove(0);
		for (int i=list.size()-1;i>0;i--){
			String[] trimArr=list.get(i).get("name").toString().split(" ");

			int trimSize=0;
			for(int t=0;t<Arrays.asList(trimArr).size();t++){
				if(StringUtil.isBlank(Arrays.asList(trimArr).get(t))){
					trimSize++;
				}
			}
			if(trimSize>0){
				if(trimSize==treeIndex){
					for(int o=i-1;o>=0;o--){
						int trimSize_last=0;
						String[] trimArr_last=list.get(o).get("name").toString().split(" ");
						for(int t=0;t<Arrays.asList(trimArr_last).size();t++){
							if(StringUtil.isBlank(Arrays.asList(trimArr_last).get(t))){
								trimSize_last++;
							}
						}
						if(trimSize_last==trimSize-1){
							@SuppressWarnings("unchecked")
							List<Map<String,Object>> childrenList=(List<Map<String,Object>>)list.get(o).get("children");
							list.get(i).put("ftaxid",list.get(o).get("taxid"));
							list.get(i).put("cj",trimSize);
							childrenList.add(0,list.get(i));
							list1.remove(list.get(i));
							break;
						}
					}


				}

			}
			if(list1.size()!=0&&i==1){
				i=list.size();
				treeIndex++;
			}
		}
		list1=new ArrayList<>();
		list1.add(map0);
		list1.add(list.get(0));

		return  list1;
	}


	public String findDate(String[] strings, String date,int index){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		//DateTimeFormatter sdf = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 设置日期格式
		if (index <= 0){
			return simpleDateFormat.format(new Date());
		}else if (date.length() == 8){
			try{
				SimpleDateFormat stringDateFormat = new SimpleDateFormat("yyyyMMdd");
				Date d_date = stringDateFormat.parse(date);
				return simpleDateFormat.format(d_date);
			} catch (Exception e) {
				return simpleDateFormat.format(new Date());
			}
		}
		return findDate(strings,strings[index-1],index-1);
	}


	@Override
	public Map<String, Object> getBlast(WkcxDto wkcxDto,HttpServletResponse response) {
		Map<String, Object> map = new HashMap<>();
		WkcxDto xpmDto=wkcxService.getXpmBywkhb(wkcxDto);
		Object blast_token = redisUtil.get("BLAST_TOKEN");
		boolean accumFlag= wkcxDto.getBlastType().equals("RA");
		String appname="Blast";
		String accName=".fa.html";
		String dirName="blast";
		if(wkcxDto.getBlastType().equals("RA")) {
			accName = ".accum.fa.html";
		}
		else if(wkcxDto.getBlastType().equals("Co")){
			accName="."+wkcxDto.getCovIndex()+".json";
			appname="Coverage";
			dirName="coverage";
		}
		WkcxbbDto wkcxbbDto=new WkcxbbDto();
		wkcxbbDto.setWkbh(wkcxDto.getWkbh());
		wkcxbbDto.setBbh(wkcxDto.getBbh());
		wkcxbbDto.setXpm(xpmDto.getXpm());
		WkcxbbDto dtoNew = wkcxbbService.getZxbbDtoByXpAndWKbh(wkcxbbDto);
		String porStr=wkcxDto.getBbh().replace(":","_");
		String savepath=releasePath+ BusTypeEnum.IMP_BIOINFORMATION.getCode()+"/UP"+dtoNew.getLrsj().substring(0,4)+"/UP"+dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(5,7)+"/UP"+dtoNew.getLrsj().substring(0,4)+dtoNew.getLrsj().substring(5,7)+dtoNew.getLrsj().substring(8,10)+"/"+xpmDto.getXpm()+"/"+wkcxDto.getWkbh()+"/"+porStr+"/";
		String htmlStr;
		htmlStr=getBlastStr(savepath+"/"+wkcxDto.getTaxid()+accName,wkcxDto.getBlastType(),response);
		if(StringUtil.isNotBlank(htmlStr)){
			map.put("status", "success");
			map.put("htmlStr", htmlStr);
			map.put("code", "10002");
			map.put("type", wkcxDto.getBlastType());
			map.put("message", "");
		}else{
			if(blast_token!=null){
				JSONObject kwargs=new JSONObject();
				kwargs.put("taxid",Integer.parseInt(wkcxDto.getTaxid()));
				if(!wkcxDto.getBlastType().equals("Co")){
					kwargs.put("accum",accumFlag);
				}else{
					kwargs.put("step",wkcxDto.getCovIndex());
				}
				JSONObject data=new JSONObject();
				data.put("app_name",appname);
				data.put("seq_name",xpmDto.getXpm());
				data.put("lib_uid",wkcxDto.getWkbh());
				data.put("kwargs",kwargs);
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("data",data);
				jsonObject.put("start",true);
				String s = JSON.toJSONString(jsonObject);
				log.error("Blast所传Json: "+s);
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<String> request = new HttpEntity<>(s, headers);
				String url="http://bcl.matridx.top/BCL/appcenter/task/?access_token="+ blast_token;
				log.error("BlastUrl: "+url);

				Map<String,Object> resultMap=null;

				try {
					resultMap=  restTemplate.postForObject(url, request, Map.class);
				}catch(Exception e){
					String message = e.getMessage();
					if(message.contains("task already exists")){
						String chipdate = xpmDto.getXpm().substring(0,6);
						String date = "20"+chipdate.substring(0,2)+"/"+chipdate.substring(2,4)+"/"+chipdate.substring(4,6);
						String path=date+"/"+xpmDto.getXpm()+"/"+wkcxDto.getWkbh()+"/"+dirName+"/"+wkcxDto.getTaxid()+accName;
						MinioClient minioClient;
						try {
							minioClient = new MinioClient("https://minio.matridx.com", "ituser", "Matridx@2022");
							InputStream inputStream = minioClient.getObject("mngs",path);
							File toFile;
							toFile = new File(savepath + File.separator + wkcxDto.getTaxid()+accName);
							try {
								savepath = toFile.getCanonicalPath();

								/*判断路径中的文件夹是否存在，如果不存在，先创建文件夹*/
								String dirPath = savepath.substring(0, savepath.lastIndexOf(File.separator));
								File dir = new File(dirPath);
								if (!dir.exists()) {
									dir.mkdirs();
								}

								inputStreamToFile(inputStream, toFile,wkcxDto.getBlastType());
								htmlStr=getBlastStr(savepath,wkcxDto.getBlastType(),response);
							} catch (IOException ex) {
								ex.printStackTrace();
							}
							map.put("status", "success");
							map.put("htmlStr", htmlStr);
							map.put("code", "10002");
							map.put("type", wkcxDto.getBlastType());
							map.put("message", "");
							return map;
						} catch (Exception e1) {
							map.put("status", "fail");
							map.put("code", "10001");
							map.put("message", "任务正在进行中！");
							return map;
						}
					}
				}
				Object result = null;
				if (resultMap != null) {
					result = resultMap.get("data");
				}
				JSONObject jsonObject_t=JSONObject.parseObject(JSON.toJSONString(result));
				String status = jsonObject_t.getString("status");
				if("等待中".equals(status)){
					map.put("status", "success");
					map.put("code", "10001");
					map.put("message", "任务创建成功！");
				}
			}else{
				map.put("status", "fail");
				map.put("message", "从Redis中未获取到token！");
			}
		}


		return map;
	}
	private String getBlastStr(String savepath, String type, HttpServletResponse response){
		StringBuilder htmlStr= new StringBuilder();
		byte[] buffer = new byte[1024];
		BufferedInputStream bis = null;
		InputStream iStream;
		OutputStream os = null; //输出流
		File file=new File(savepath);
		if(type.equals("Co")){
			try {
				FileReader fr = new FileReader(savepath);
				BufferedReader br=new BufferedReader(fr);
				String line;
				while ((line=br.readLine())!=null) {
					htmlStr.append(line);

				}
				br.close();
				fr.close();
			}catch (Exception e){
				htmlStr = new StringBuilder();
			}
		}else{
			if(file.exists()){
				try {
					iStream = new FileInputStream(savepath);
					os = response.getOutputStream();
					bis = new BufferedInputStream(iStream);
					int i = bis.read(buffer);
					while(i != -1){
						os.write(buffer);
						os.flush();
						i = bis.read(buffer);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error("文件下载 写文件异常：" + e);
				}
				try {
					if (bis != null) {
						bis.close();
					}
					if (os != null) {
						os.flush();
						os.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					log.error("文件下载 关闭文件流异常：" + e);
				}
				htmlStr = new StringBuilder("1");
			}
		}





		return htmlStr.toString();
	}
	//获取流文件
	private  void inputStreamToFile(InputStream ins, File file,String type) {
		try {
			OutputStream os = new FileOutputStream(file);
			OutputStreamWriter osw=new OutputStreamWriter(os);
			int bytesRead;
			byte[] buffer = new byte[8192];
			try {
				if("Co".equals(type)){
					while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
						os.write(buffer, 0, bytesRead);
					}
				}else{
					BufferedReader br = new BufferedReader(new InputStreamReader(ins));
					String line;
					while ((line=br.readLine())!=null) {
						osw.write(line+"</br>");
					}
					osw.flush();
				}
			}catch (Exception e){
				osw.close();
				os.close();
				ins.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void modChipInfo(String xpm){
		String xpid = xpxxService.getXpidByXpm(xpm);
		if(StringUtil.isNotBlank(xpid)){
			String token = "eba3f34c24943e1c";//token固定永久有效
			RestTemplate resttemplate = new RestTemplate();
			//获取簇密度，荧光值，q30
			String getChipInfoUrl = "http://bcl.matridx.top/BCL/api/sequence/";
			@SuppressWarnings("unchecked")
			Map<String,Object> map=resttemplate.getForObject(getChipInfoUrl +xpid+"/?token="+token, Map.class);
			if(map!=null){
				@SuppressWarnings("unchecked")
				Map<String,Object> interop = (Map<String,Object>)map.get("interop");
				BioXpxxDto bioXpxxDto=new BioXpxxDto();
				bioXpxxDto.setXpm(xpm);
				Object density = interop.get("density");
				if(density!=null){
					bioXpxxDto.setDensity(density.toString());
				}
				Object intensity = interop.get("intensity");
				if(density!=null){
					bioXpxxDto.setIntensity(intensity.toString());
				}
				Object q30 = map.get("q30");
				if(q30!=null){
					bioXpxxDto.setQ30(q30.toString());
				}
				Object lib_count = map.get("lib_count");
				if(lib_count!=null){
					bioXpxxDto.setFm(lib_count.toString());
				}
				xpxxService.update(bioXpxxDto);
			}
		}
	}
}
