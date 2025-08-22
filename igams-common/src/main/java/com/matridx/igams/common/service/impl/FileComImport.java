package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.CheckTypeEnum;
import com.matridx.igams.common.enums.MessageTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.util.RedisXmlConfig;
import com.matridx.springboot.util.base.RegexUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.file.excel.ExcelHelper;
import com.matridx.springboot.util.file.upload.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FileComImport{
	public final static String SYMBOL_SPLIT = ",";
	public final static String SYMBOL_COLON = ":";
//	public final static String SYMBOL_CURLY_OPEN = "{";
//	public final static String SYMBOL_CURLY_CLOSE = "}";
	
	final static Logger logger = LoggerFactory.getLogger(FileComImport.class);
	
	protected final ExcelHelper eh = new ExcelHelper();
	
	//正在处理的文件序号
	protected int fileNum;
	
	protected List<String> headinfo;
	
	//要导入的数据
	//protected List<Map<String,String>> rows = new ArrayList<Map<String,String>>();
	//校验定义
	//protected List<Map<String, String>> defined = new ArrayList<Map<String, String>>();
	//表头信息
	//protected Map<String,String> headList = new HashMap<String,String>();
	//配置信息
	//protected Map<String,Object> configMap = new HashMap<String,Object>();
	
	//错误信息
	protected final StringBuffer errorMessage = new StringBuffer();
	//确认信息
	protected final StringBuffer confirmMessage = new StringBuffer();
	//警告信息
	protected final StringBuffer warnMessage = new StringBuffer();
	//提示信息
	protected final StringBuffer hintMessage = new StringBuffer();
	//记录导入文件不可以重复的全部的列值
	protected final StringBuffer exists = new StringBuffer();
	//记录导入文件不可以重复的全部的列值（记得多字段单行内容）
	protected Map<String,String> multiKeys = new HashMap<>();
	//记录导入文件不可以重复的全部的列值（多字段判断）
	protected final List<Map<String,String>> multiExists = new ArrayList<>();
	//警告记录信息
	protected final List<ImportRecordModel> warnRecList = new ArrayList<>();
	//确认记录信息
	protected final List<ImportRecordModel> confirmRecList = new ArrayList<>();
	//是否为隔断记录
//	protected boolean isPluralRec = false;
	
	private FjcfbDto fjcfbDto;
	private RedisUtil redisUtil;
	private IFileImport fileImport;
	private RedisXmlConfig redisXmlConfig;
	private String fjid;
	private User user;
	
	public void init(FjcfbDto fjcfbDto,RedisXmlConfig redisXmlConfig,IFileImport fileImport,User user){
		this.fjcfbDto = fjcfbDto;
		this.redisUtil = redisXmlConfig.getRedisUtil();
		this.fileImport = fileImport;
		this.redisXmlConfig = redisXmlConfig;
		this.fjid = fjcfbDto.getFjid();
		this.user = user;
	}
	
	public void before() {
		redisUtil.hset("IMP_:_"+fjid,"msg", "导入处理开始",3600);
	}

	public void after() {
		redisUtil.hset("IMP_:_"+fjid,"endflg", "1");
		redisUtil.hset("IMP_:_"+fjid,"msg", "导入处理结束。");
	}
	
	/**
	 * 上传文件并对文件进行检查，同时反馈给客户端
	 */
	public void FileImportCheck() throws BusinessException{
		try{
			redisUtil.hset("IMP_:_"+fjid,"endflg", "0");
			
			before();
			String file = (String) redisUtil.hget("IMP_:_" + fjid, "wjlj");
			if (file.endsWith(".txt") || file.endsWith(".zip")){
				List<Map<String,String>> rows = new ArrayList<>();
//				readTxtOrZip(rows,file);
			} else {
				List<Map<String,String>> rows = read((String)redisUtil.hget("IMP_:_"+fjid, "wjlj"));
				List<String> dtoFiledsName = parseHead(rows);
				List<Map<String,String>> defined = parseCheckInfo(rows);
				//检查模板标题
				boolean isSucess = fileImport.checkDefined(	defined);
				if(isSucess) {
					check(rows,defined,dtoFiledsName);

					transform(rows,defined);
					transToPage();
				}else {
					errorMessage.append("模板校验不通过!请下载最新的文件模板!");
				}
			}
			redisUtil.hset("IMP_:_"+fjid,"errorMessage", errorMessage);
			redisUtil.hset("IMP_:_"+fjid,"confirmMessage", confirmMessage);
			redisUtil.hset("IMP_:_"+fjid,"warnMessage", warnMessage);
			redisUtil.hset("IMP_:_"+fjid,"hintMessage", hintMessage);
			redisUtil.hset("IMP_:_"+fjid,"warnRecList", warnRecList);
			
			after();
			
		}catch(Exception e){
			redisUtil.hset("IMP_:_"+fjid,"errorMessage", e.getMessage());
			throw new BusinessException("",e.getMessage());
		}
	}
	
	/**
	 * 普通文件导入，只获取文件信息进行展示，不在进行Dto分析
	 */
	public void FileImportNormalCheck() throws BusinessException{
		try{
			redisUtil.hset("IMP_:_"+fjid,"endflg", "0");
			
			before();
			
			List<Map<String,String>> rows = read((String)redisUtil.hget("IMP_:_"+fjid, "wjlj"));
			List<String> dtoFiledsName = parseNormalHead(rows);
			transToNormalPage(dtoFiledsName,rows);
			
			redisUtil.hset("IMP_:_"+fjid,"errorMessage", errorMessage);
			redisUtil.hset("IMP_:_"+fjid,"confirmMessage", confirmMessage);
			redisUtil.hset("IMP_:_"+fjid,"warnMessage", warnMessage);
			redisUtil.hset("IMP_:_"+fjid,"hintMessage", hintMessage);
			redisUtil.hset("IMP_:_"+fjid,"warnRecList", warnRecList);
			
			after();
			
		}catch(Exception e){
			redisUtil.hset("IMP_:_"+fjid,"errorMessage", e.getMessage());
			throw new BusinessException("",e.getMessage());
		}
	}
	
	/**
	 * 特殊文件导入，也就是说头部和内容不一定统一，但格式是固定的。只获取文件信息进行展示，不在进行Dto分析
	 */
	public void FileImportSpecialCheck() throws BusinessException{
		try{
			redisUtil.hset("IMP_:_"+fjid,"endflg", "0");
			
			before();
			//因为没有头部信息，所以只需获取相应的头部信息
			List<Map<String,String>> rows = readAll((String)redisUtil.hget("IMP_:_"+fjid, "wjlj"));
			transToSpecialPage(rows);
			
			redisUtil.hset("IMP_:_"+fjid,"errorMessage", errorMessage);
			redisUtil.hset("IMP_:_"+fjid,"confirmMessage", confirmMessage);
			redisUtil.hset("IMP_:_"+fjid,"warnMessage", warnMessage);
			redisUtil.hset("IMP_:_"+fjid,"hintMessage", hintMessage);
			redisUtil.hset("IMP_:_"+fjid,"warnRecList", warnRecList);
			
			after();
			
		}catch(Exception e){
			redisUtil.hset("IMP_:_"+fjid,"errorMessage", e.getMessage());
			throw new BusinessException("",e.getMessage());
		}
	}

	/**
	 * 1、读文件(excle)
	 */
	public List<Map<String,String>> read(String file) throws Exception {
		logger.error("read " + file);
		redisUtil.hset("IMP_:_"+fjid,"msg", "文件读取中！");
		List<Map<String,String>> rows = new ArrayList<>();
		//txt\zip文件数据回显页面
		if (file.endsWith(".txt") || file.endsWith(".zip")){
			readTxtOrZip(rows,file);
		} else {
			rows = eh.readExcel(file);
		}
		return rows;
	}

	/**
	 * 1、读文件(txt\zip)
	 */
	public List<Map<String, String>> readTxtOrZip(List<Map<String, String>> rows, String file) throws Exception {
		logger.error("read " + file);
		redisUtil.hset("IMP_:_" + fjid, "msg", "文件读取中！");
		Object o_settings = redisUtil.hget("IMP_:_" + fjid, "settings");
		Map<String,String> settingJson = new HashMap<>();
		if (o_settings!=null){
			settingJson = JSON.parseObject(o_settings.toString(), Map.class);
		}
		if (file.endsWith(".txt")) {
			BufferedReader bufferedReader = null;
			try {
				String txtSet = "ybbh,hzxm,xb,qtxx.csrq,nl,sjys,ks,yblxmc,cyrq,qtxx.CollectionTime,qqzd,qtxx.hzlx,cwh,sjdwmc,jsrq,jcxm,zyh";
				if (settingJson != null && StringUtil.isNotBlank(settingJson.get("txtSet"))){
					txtSet = settingJson.get("txtSet");
				}
				List<String> txtList = Arrays.asList(txtSet.split(","));
				String db = "黄石中心";
				if (settingJson != null && StringUtil.isNotBlank(settingJson.get("db"))){
					db = settingJson.get("db");
				}
				String sjdwmc = "黄石市中心医院";
				if (settingJson != null && StringUtil.isNotBlank(settingJson.get("sjdwmc"))){
					sjdwmc = settingJson.get("sjdwmc");
				}
				String jcdwdm = "87";
				if (settingJson != null && StringUtil.isNotBlank(settingJson.get("jcdwdm"))){
					jcdwdm = settingJson.get("jcdwdm");
				}
				String sjqf = "入院";
				String jcxmpp = "病原微生物宏基因组检测-DNA:IMP_REPORT_SEQ_INDEX_TEMEPLATE,D;病原微生物宏基因组检测-RNA:IMP_REPORT_ONCO_QINDEX_TEMEPLATE,R;病原微生物宏基因组检测(DNA和RNA):IMP_REPORT_ONCO_QINDEX_TEMEPLATE,C;多种病原体靶向检测:IMP_REPORT_SEQ_TNGS_E,C";
				if (settingJson != null && StringUtil.isNotBlank(settingJson.get("jcxmpp"))){
					jcxmpp = settingJson.get("jcxmpp");
				}
				String[] pps = jcxmpp.split(";");
				List<JcsjDto> jcdwList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
				List<JcsjDto> jcxmList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
				JcsjDto jcdw = null;
				try {
					String finaljcdw = jcdwdm;
					jcdw = jcdwList.stream().filter(item -> finaljcdw.equals(item.getCsdm())).collect(Collectors.toList()).get(0);
				} catch (Exception e) {
					throw new Exception("检测单位不存在：" + jcdwdm);
				}
				bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					String[] split = line.split("\\t");
					Map<String, String> lineMap = new HashMap<>();
					lineMap.put("db", db);//合作伙伴
					lineMap.put("sjdwmc", sjdwmc);//送检单位名称
					lineMap.put("sjqf", sjqf);//送检单位名称
					lineMap.put("jcdw", jcdw.getCsmc());//检测单位名称
					//其余字段
					for (int i = 0; i < split.length; i++) {
						if (i < txtList.size()) {
							lineMap.put(txtList.get(i), split[i]);
							if ("jcxm".equals(txtList.get(i))){
								lineMap.put("qtxx.yyjcxmmc",split[i]);
							} else if ("cyrq".equals(txtList.get(i))){
								lineMap.put("qtxx.yycysj",split[i]);
							}
						}
					}
					if (settingJson != null && StringUtil.isNotBlank(settingJson.get("sjdw"))){
						lineMap.put("sjdw", settingJson.get("sjdw"));
					}
					if (lineMap.get("ybbh")!=null){
						lineMap.put("wbbm", lineMap.get("ybbh"));
					}
					//匹配检测项目
					try {
						for (String pp : pps) {
							String[] ppnr = pp.split(":");
							if (StringUtil.isNotBlank(lineMap.get("jcxm")) && lineMap.get("jcxm").equals(ppnr[0])) {
								String jcxmppdm = ppnr[1];
								JcsjDto jc_jcxm = jcxmList.stream().filter(item -> jcxmppdm.split(",")[0].equals(item.getCskz3()) && jcxmppdm.split(",")[1].equals(item.getCskz1())).collect(Collectors.toList()).get(0);
								lineMap.put("jcxm", jc_jcxm.getCsmc());
								break;
							}
						}
					} catch (Exception e) {
						logger.error("检测项目匹配失败：" + lineMap.get("jcxm"));
					}
					rows.add(lineMap);
				}
			} catch (IOException e) {
				logger.error(file + "读取失败");
			} finally {
				if (bufferedReader!=null){
					bufferedReader.close();
				}
			}
		} else if (file.endsWith(".zip")) {
			String unzippath = file.substring(0, file.lastIndexOf("/"));
			ZipUtil.unZipFile(unzippath, file);
			File zipFile = new File(file.substring(0, file.lastIndexOf(".")));
			if (zipFile.exists() && zipFile.isDirectory()) {
				if (zipFile.listFiles().length > 0) {
					for (File listFile : zipFile.listFiles()) {
						readTxtOrZip(rows, listFile.getAbsolutePath());
					}
				}
			}
		}
		return rows;
	}
	
	/**
	 * 1.0.1、读文件，不限定列数，自由读取，内容准确，效率会降低。这个方法是相对于read方法
	 * @author goofus
	 */
	public List<Map<String,String>> readAll(String file) throws Exception {
		logger.error("read " + file);
		redisUtil.hset("IMP_:_"+fjid,"msg", "文件读取中！");
		return eh.readExcelALL(file);
	}

	/**
	 * 2.1、解析表头数据，同时返回第一行的字段列表信息
	 * @author goofus
	 */
	protected List<String> parseHead(List<Map<String,String>> rows) {
		
		logger.error("parseHead 第" + fileNum + "个文件");
		
		redisUtil.hset("IMP_:_"+fjid,"msg", "文件表头解析中！");
		/*
		 * 解析表头及持久化定义信息
		 */
		//表格第1行约定为中文标示及入库配置的ID和DTO属性名称
		Map<String, String> headList = rows.get(0);
		rows.remove(0);
		
		List<String> dtoFieldNames = new ArrayList<>();
		//表头信息解析
		for(int a=0,al=headList.size(); a<al; a++){
			String k = String.valueOf(a);
			String head = headList.get(k);

			String dtoFieldName;
			if(!head.contains("[")){
				dtoFieldNames.add(null);//不入库的列，但必须占位
				continue;
			}
			headList.put(k, head.substring(0, head.indexOf("[")));//还原表头为名称

			String config = head.substring(head.indexOf("[")+1, head.indexOf("]"));
			if(StringUtil.isBlank(config)) {
				dtoFieldNames.add(null);//不入库的列，但必须占位
				continue;
			}

			//视为DTO属性名
			dtoFieldName = config;  
			
			dtoFieldNames.add(dtoFieldName);
		}
		
		return dtoFieldNames;
	}
	
	/**
	 * 2.1（2）、解析表头数据，同时返回第一行的字段列表信息
	 * @author goofus
	 */
	protected List<String> parseNormalHead(List<Map<String,String>> rows) {
		
		logger.error("parseHead 第" + fileNum + "个文件");
		
		redisUtil.hset("IMP_:_"+fjid,"msg", "文件表头解析中！");
		/*
		 * 解析表头及持久化定义信息
		 */
		//表格第1行约定为中文标示及入库配置的ID和DTO属性名称
		Map<String, String> headList = rows.get(0);
		rows.remove(0);
		
		List<String> dtoFieldNames = new ArrayList<>();
		//表头信息解析
		for(int a=0,al=headList.size(); a<al; a++){
			String k = String.valueOf(a);
			String head = headList.get(k);
			dtoFieldNames.add(head);
		}
		
		return dtoFieldNames;
	}
	
	/**
	 * 2.2对第二行的各种检查进行确认
	 */
	protected List<Map<String,String>> parseCheckInfo(List<Map<String,String>> rows) throws Exception {
		
		logger.error("parseCheckInfo 第" + fileNum + "个文件");
		
		redisUtil.hset("IMP_:_"+fjid,"msg", "文件检查定义校验中！");
		
		List<Map<String, String>> defined = new ArrayList<>();
		/*
		 * 解析校验定义信息
		 */
		//表格第2行约定为校验定义
		Map<String,String> checkDefinedMap = rows.get(0);
		rows.remove(0);
		//校验定义解析{nullable:no,maxlength:256,minlength:1}
		if(null != checkDefinedMap){
			for(int a=0,al=checkDefinedMap.size(); a<al; a++){
				String idx = String.valueOf(a);
				String checkDefined = checkDefinedMap.get(idx);
				if(StringUtil.isBlank(checkDefined)){
					Map<String, String> defi = new HashMap<>();
					defined.add(defi);
					continue;
				}
				checkDefined = checkDefined.replaceAll(
						"\\{", "").replaceAll(
								"\\}", "").replaceAll(" ", "");
				String[] checkDefineds = checkDefined.split(SYMBOL_SPLIT);
				if(checkDefineds.length < 1){
					continue;
				}
				Map<String, String> defi = new HashMap<>();
				for(String s : checkDefineds){
					if(StringUtil.isBlank(s) || !s.contains(SYMBOL_COLON)) continue;
					String[] ss = s.split(SYMBOL_COLON);
					if(StringUtil.isBlank(ss[0]) || StringUtil.isBlank(ss[1])) continue;
					checkReferenceValue(idx, ss[0], ss[1]);

					defi.put(ss[0], ss[1]);
				}
				defined.add(defi);
			}
		}
		
		return defined;
	}

	/**
	 * 校验校验参考量
	 * @author goofus
	 */
	private void checkReferenceValue(String idx, String k, String v) throws Exception {
		if(CheckTypeEnum.MAXLENGTH.getCode().equalsIgnoreCase(k) || CheckTypeEnum.MINLENGTH.getCode().equalsIgnoreCase(k)){
			if(!RegexUtil.matches(v, RegexUtil.REGEX_NUMBER)){
				errorMessage.append("第").append(Integer.parseInt(idx) + 1).append("列校验参考量不符合要求，应为数字，校验类型：maxlength或minlength，参考量：");
				errorMessage.append(v).append("。<br>");
				throw new Exception(errorMessage.toString());
			}
		}
	}

	/**
	 * 3、数据数据定义进行校验
	 * @author goofus
	 */
	public void check(List<Map<String,String>> rows,List<Map<String,String>> defined,List<String> dtoFiledsName) {
		
		logger.error("check 第" + fileNum + "个文件");
		
		if(rows==null||rows.isEmpty()) return;
		int size = rows.size();
		for(int a = 0; a< size; a++){
			redisUtil.hset("IMP_:_"+fjid,"msg", "数据格式检验：" + a + "/" + size + "行");
			Map<String,String> row = rows.get(a);
			if(row == null){
				continue;
			}
			multiKeys = new HashMap<>();//当前行的多值重复判断存放
			for(int b=0,bl=row.size(); b<bl; b++){
				String val = row.get(""+b);//业务数据，单元格的值
				if(defined.size() > b){
					cellCheck(defined.get(b),dtoFiledsName.get(b),a, b, val);//校验一个单元格	
				}
			}
			if(multiKeys.size()>=2){//存在多值重复判断的情况
				boolean isSame = !multiExists.isEmpty();
				for (Map<String,String> line : multiExists) {
					isSame = true;
					for (String key : line.keySet()) {
						String val = StringUtil.defaultString(line.get(key)).trim();
						String currentVal = StringUtil.defaultString(multiKeys.get(key)).trim();
						if(!currentVal.equals(val)){
							isSame = false;
						}
					}
					if(isSame) break;
				}
				if(isSame){
					errorMessage.append("第").append(a+3).append("行，在导入文件中重复了，判重的列为");
					for(int b=0,bl=row.size(); b<bl; b++){
						if(multiKeys.containsKey(""+b)){
							errorMessage.append("，第").append(b + 1).append("列单元格值为：").append(StringUtil.defaultString(multiKeys.get(""+b)));
						}
					}
					errorMessage.append("；<br>");
				}else{
					multiExists.add(multiKeys);
				}
			}
		}
	}

	/**
	 * 校验一个单元格值
	 * @author goofus
	 */
	protected boolean cellCheck(Map<String,String> cellconfig,String filedName,int a, int b, String val) {
		//按枚举有序进行校验
		Set<String> keySet = cellconfig.keySet();
		boolean isErr = false;
		//是否为空
		CheckTypeEnum cte = CheckTypeEnum.NULLABLE;
		for (String k : keySet) {
			if (cte.getCode().equalsIgnoreCase(k)) {
				//判定参考量
				String v = cellconfig.get(k);
				//单元格值判定
				if ("no".equalsIgnoreCase(v) || "n".equalsIgnoreCase(v) || "false".equalsIgnoreCase(v)) {
					if (StringUtil.isBlank(val)) {
						isErr = true;
						//校验结果，不可以为空，但值为空，校验不通过
						errorMessage.append("第").append(a + 3).append("行的第").append(b + 1)
								.append("列，不应为空，单元格值为：").append(val).append(";<br>");
					}
				} else {
					if (StringUtil.isBlank(val)) {
						return false;
					}
				}
				break;
			}
		}
		//最大长度检查
		cte = CheckTypeEnum.MAXLENGTH;
		for (String k : keySet) {
			if (cte.getCode().equalsIgnoreCase(k)) {
				//判定参考量
				String v = cellconfig.get(k);
				int vi = Integer.parseInt(v);
				//单元格值判定
				if (StringUtil.stringLength(val) > vi) {
					isErr = true;
					errorMessage.append("第").append(a + 3).append("行的第").append(b + 1)
							.append("列，长度大于约定值，单元格值为：").append(val).append(";<br>");//校验结果
				}
				break;
			}
		}
		//最小长度检查
		cte = CheckTypeEnum.MINLENGTH;
		for (String k : keySet) {
			if (cte.getCode().equalsIgnoreCase(k)) {
				//判定参考量
				String v = cellconfig.get(k);
				int vi = Integer.parseInt(v);
				//单元格值判定
				if (StringUtil.stringLength(val) < vi) {
					isErr = true;
					errorMessage.append("第").append(a + 3).append("行的第").append(b + 1)
							.append("列，长度小于约定值，单元格值为：").append(val).append(";<br>");//校验结果
				}
				break;
			}
		}

		//如果单元格值为空，则不检查类型
		if(StringUtil.isBlank(val))
			return isErr;

		//数据类型检查
		cte = CheckTypeEnum.DATATYPE;
		for (String k : keySet) {
			if (cte.getCode().equalsIgnoreCase(k)) {
				//判定参考量
				String v = cellconfig.get(k);
				//单元格值判定
				if (("long".equalsIgnoreCase(v) || "number".equalsIgnoreCase(v) || "int".equalsIgnoreCase(v))
						&& !RegexUtil.matches(val, RegexUtil.REGEX_NUMBER)) {
					isErr = true;
					errorMessage.append("第").append(a + 3).append("行的第").append(b + 1)
							.append("列，应为数字，单元格值为：").append(val).append(";<br>");//校验结果
				} //else if(){}  此处可扩展数据类型
				break;
			}
		}
		//日期格式检查
		cte = CheckTypeEnum.DATEFORMAT;
		for (String k : keySet) {
			if (cte.getCode().equalsIgnoreCase(k)) {
				//判定参考量
				String v = cellconfig.get(k);
				//单元格值判定；
				if (!RegexUtil.matches(val, RegexUtil.REGEX_DATE_1)) {
					isErr = true;
					errorMessage.append("第").append(a + 3).append("行的第").append(b + 1)
							.append("列，日期格式应为：").append(v).append("，单元格值为：")
							.append(val).append("；<br>");//校验结果
				}
				break;
			}
		}
		//有效选项 检查 输入的值是否在知道范围内
		cte = CheckTypeEnum.OPTIONS;//有效选项
		for (String k : keySet) {
			if (cte.getCode().equalsIgnoreCase(k)) {
				//判定参考量
				String v = cellconfig.get(k);
				//单元格值判定；
				if (!v.contains("#" + val)) {
					isErr = true;
					errorMessage.append("第").append(a + 3).append("行的第").append(b + 1)
							.append("列，有效选项：").append(v).append("，单元格值为：")
							.append(val).append("；<br>");//校验结果
				}
				break;
			}
		}
		//判断是否重复
		cte = CheckTypeEnum.EXISTS;//判重
		for (String k : keySet) {
			if (cte.getCode().equalsIgnoreCase(k)) {
				//判定参考量
				String v = cellconfig.get(k);
				//单元格值判定；
				if ("false".equalsIgnoreCase(v) || "0".equalsIgnoreCase(v)) {
					//List<String> dtoFieldNames = (List<String>) configMap.get("dtoFieldNames");
					//判断表里是否已经存在
					if (fileImport.existCheck(filedName, val)) {
						warnMessage.append("第").append(a + 3).append("行的第").append(b + 1)
								.append("列，数据库中已经存在，单元格值为：")
								.append(val).append("；")
								.append("<br>");//校验结果
						ImportRecordModel recModel = new ImportRecordModel();
						recModel.setFileNum(fileNum);
						recModel.setRowNum(a);
						recModel.setColumnNum(b);
						recModel.setMessType(MessageTypeEnum.WARN.getCode());
						recModel.setCheckType(CheckTypeEnum.EXISTS.getCode());
						recModel.setValue(val);
						warnRecList.add(recModel);
					}
					//判断导入文件是否存在重名的
					if (exists.indexOf(val + "~@~") != -1) {
						isErr = true;
						errorMessage.append("第").append(a + 3).append("行的第").append(b + 1)
								.append("列，在导入文件中重复了，单元格值为：")
								.append(val).append("；<br>");
					}
					exists.append(val).append("~@~");
				}
				break;
			}
		}
		
		cte = CheckTypeEnum.MULTIEXISTS;//判重
		for (String k : keySet) {
			if (cte.getCode().equalsIgnoreCase(k)) {
				//判定参考量
				String v = cellconfig.get(k);
				//单元格值判定；
				if ("true".equalsIgnoreCase(v) || "1".equalsIgnoreCase(v)) {
					multiKeys.put("" + b, val);
				}
				break;
			}
		}

		return isErr;
	}
	
	/**
	 * 4、转换
	 * @author goofus
	 */
	public void transform(List<Map<String,String>> rows,List<Map<String,String>> defined) throws Exception {
		
		if(rows==null||rows.isEmpty()) return;

		ImportRecordModel recModel;
		int size = rows.size();
		for(int a = 0; a< size; a++){
			
			redisUtil.hset("IMP_:_"+fjid,"msg", "数据转换：" + a + "/" + size + "行");
			
			Map<String,String> row = rows.get(a);
			if(row == null){
				continue;
			}
			recModel = new ImportRecordModel();

			for(int b=0,bl=row.size(); b<bl; b++){
				String k = String.valueOf(b);
				String val = row.get(k);//业务数据，单元格的值
				if(val != null) val = val.trim();
				
				recModel.setRowNum(a);
				recModel.setColumnNum(b);

				String transformVal = cellTransform(defined,val,recModel);//单元格值转换
				row.put(k, transformVal);
				if(eh==null)
					redisUtil.hset("IMP_:_"+fjid,"msg", "文件信息不正确，无法获取文件。");
				eh.setValue(a+2, b, transformVal);
			}
		}
		eh.saveFile();
	}

	/**
	 * 转换一个单元格值
	 */
	private String cellTransform(List<Map<String,String>> defined,String val,ImportRecordModel recModel){
		Map<String, String> defi = defined.get(recModel.getColumnNum());
	    if (!"condition".equalsIgnoreCase(defi.get(CheckTypeEnum.NULLABLE.getCode().toLowerCase())) && StringUtil.isEmpty(val)) {
	      return val;
	    }
		String transformVal = null;
		Set<String> keySet = defi.keySet();

		boolean isTran =false;
		CheckTypeEnum cte = CheckTypeEnum.TRANSFORM;
		for (String k : keySet) {
			if (cte.getCode().equalsIgnoreCase(k)) {
				isTran = true;
				//判定参考量
				String v = defi.get(k);
				if (StringUtil.isBlank(v)) break;
				v = v.toUpperCase();
				//转换标识判定
				if (v.equalsIgnoreCase("C001")) {//

					//找不到匹配的转换值
					if (StringUtil.isBlank(transformVal)) {
						errorMessage.append("第").append(recModel.getRowNum() + 3).append("行的第").append(recModel.getColumnNum() + 1)
								.append("列，找不到对应的科研方向的编码，单元格值为：").append(val).append("；<br>");
					}
					break;//跳出外层循环
				} else {
					transformVal = fileImport.cellTransform(v, val, recModel, errorMessage);
				}
			}
		}
		if(StringUtil.isBlank(transformVal) && !isTran) transformVal = val;
		return transformVal;
	}

	/**
	 * 根据检查信息，组装页面显示内容
	 */
	public void transToPage(){
		if(confirmRecList.isEmpty())
			return;
		int rowNo = 1;

		for(int row_num = 0; row_num < confirmRecList.size() ; row_num++){
			ImportRecordModel rec = confirmRecList.get(row_num);

			if(rec == null)
				continue;

			if(MessageTypeEnum.CONFIRM.getCode().equals(rec.getMessType())){
				StringBuilder sb = new StringBuilder();
				sb.append("<tr>");
				sb.append("<input type=\"hidden\" name=\"recordList[").append(row_num).append("].fileNum\" value=\"").append(rec.getFileNum()).append("\" id=\"fileNum_").append(row_num).append("\">");
				sb.append("<input type=\"hidden\" name=\"recordList[").append(row_num).append("].rowNum\" value=\"").append(rec.getRowNum()).append("\" id=\"rowNum_").append(row_num).append("\">");
				sb.append("<input type=\"hidden\" name=\"recordList[").append(row_num).append("].columnNum\" value=\"").append(rec.getColumnNum()).append("\" id=\"columnNum_").append(row_num).append("\">");
				sb.append("<td>").append("<p style=\"float:left;\">").append(rowNo).append("</p>");
				if(rec.isRepeat()){
					sb.append("<p style=\"color:red;float:left;\" title=\"系统中已存在同名记录！\">[重复]</p><input id=\"isrepeat_").append(row_num).append("\" type=\"hidden\" name=\"recordList[").append(row_num).append("].isrepeat\" value=\"Y\">");
					sb.append("<input type=\"hidden\" name=\"recordList[").append(row_num).append("].repeatId\" value=\"").append(rec.getRepeatId()).append("\" id=\"repeatId_").append(row_num).append("\">");
				}
				sb.append("</td>");
				sb.append("<td id=\"gh_").append(rowNo).append("\">");
				sb.append("</td>");
				sb.append("</tr>");
				if(rowNo == 1 && headinfo != null){
					confirmMessage.append("<input type=\"hidden\" value=\"").append(confirmRecList.size()).append("\" id=\"recSize\" name=\"recSize\" />");
					confirmMessage.append("<table class=\"table table-striped table-bordered text-left\" style=\"background-color:#fff;\" id=\"confirmtable\"><thead><tr>");
					for (String s : headinfo) {
						confirmMessage.append("<th>");
						confirmMessage.append(s);
						confirmMessage.append("</th>");
					}
					confirmMessage.append("</tr></thead><tbody id=\"tb_thesisProject\">");
				}
				confirmMessage.append(sb);

				rowNo ++;
			}
		}
		if(confirmMessage!= null && StringUtil.isNotBlank(confirmMessage.toString()))
			confirmMessage.append("</tbody></table>");
	}
	
	/**
	 * 根据检查信息，组装普通页面显示内容
	 */
	public void transToNormalPage(List<String> dtoFiledsName,List<Map<String,String>> rows){
		int maxRow;
		int maxCol = 0;
		confirmMessage.append("<input type=\"hidden\" value=\"").append(rows.size()).append("\" id=\"recSize\"  name=\"recSize\" />");
		confirmMessage.append("<table class=\"table table-striped table-bordered text-left\" style=\"background-color:#fff;\" id=\"confirmtable\"><tbody id=\\\\\\\"tb_thesisProject\\\\\\\">");
		maxRow = Math.min(rows.size(), 10);
		for(int rownum =0;rownum < maxRow;rownum ++) {
			if(maxCol < rows.get(rownum).size()) {
				maxCol = rows.get(rownum).size();
			}
		}
		confirmMessage.append("<tr>");
		for (String s : dtoFiledsName) {
			confirmMessage.append("<th>");
			confirmMessage.append(s);
			confirmMessage.append("</th>");
		}
		confirmMessage.append("</tr>");
		for(int rownum =0;rownum < maxRow;rownum ++) {
			confirmMessage.append("<tr>");
			for(int colnum =0;colnum < maxCol;colnum ++) {
				confirmMessage.append("<td>");
				if(colnum >= rows.get(rownum).size())
					confirmMessage.append("&nbsp;");
				else
					confirmMessage.append(rows.get(rownum).get(String.valueOf(colnum)));
				confirmMessage.append("</td>");
			}
			confirmMessage.append("</tr>");
		}
		confirmMessage.append("<tr><td colspan=");
		confirmMessage.append(dtoFiledsName.size());
		confirmMessage.append(">一共有记录 ");
		confirmMessage.append(rows.size());
		confirmMessage.append("条</td></tr>");
		
		confirmMessage.append("</tbody></table>");
		
	}
	
	/**
	 * 根据检查信息，组装普通页面显示内容
	 */
	public void transToSpecialPage(List<Map<String,String>> rows){
		int maxRow;
		int maxCol = 0;
		confirmMessage.append("<input type=\"hidden\" value=\"").append(rows.size()).append("\" id=\"recSize\"  name=\"recSize\"/>");
		confirmMessage.append("<table class=\"table table-striped table-bordered text-left\" style=\"background-color:#fff;\" id=\"confirmtable\"><tbody id=\\\\\\\"tb_thesisProject\\\\\\\">");
		maxRow = Math.min(rows.size(), 10);
		for(int rownum =0;rownum < maxRow;rownum ++) {
			if(maxCol < rows.get(rownum).size()) {
				maxCol = rows.get(rownum).size();
			}
		}
		for(int rownum =0;rownum < maxRow;rownum ++) {
			confirmMessage.append("<tr>");
			for(int colnum =0;colnum < maxCol;colnum ++) {
				confirmMessage.append("<td>");
				if(colnum >= rows.get(rownum).size())
					confirmMessage.append("&nbsp;");
				else
					confirmMessage.append(rows.get(rownum).get(String.valueOf(colnum)));
				confirmMessage.append("</td>");
			}
			confirmMessage.append("</tr>");
		}
		confirmMessage.append("</tbody></table>");
	}

	/**
	 * 持久化结果集
	 * 注意：别忘了事务那点事
	 * @author goofus
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean persistentFileAfterCheck() {

		StringBuffer row_errorMessage = new StringBuffer();
		try{
			//判断是否需要解压文件
			/*if(StringUtil.isNotBlank(fjcfbDto.getYsblj())){
				int index = fjcfbDto.getYsblj().lastIndexOf("/"); 
	            if ((index >-1) && (index < (fjcfbDto.getYsblj().length()))) { 
	                String folder = fjcfbDto.getYsblj().substring(0, index);
	                ZipUtil.unZipFile(folder, fjcfbDto.getYsblj());
	            }
			}*/
			String wjlj = (String) redisUtil.hget("IMP_:_" + fjid, "wjlj");
			List<Map<String, String>> rows = new ArrayList<>();
			if (wjlj.endsWith(".txt") || wjlj.endsWith(".zip")){
				readTxtOrZip(rows,wjlj);
				redisUtil.hset("IMP_:_"+fjid,"insertEndflg", "0");
				redisUtil.hset("IMP_:_"+fjid,"insertMsg", "文件开始导入。");
				if(!rows.isEmpty()) {
					Map<String, String> impConfig = redisXmlConfig.getImpConfig(fjcfbDto.getYwlx());
					String dtoName = impConfig.get("class-entity");
					Class<?> dtoClass = Class.forName(dtoName);

					for(int a=0,al=rows.size(); a<al; a++){
						StringBuffer row_errorMessages = new StringBuffer();
						Map<String, String> row = rows.get(a);//Excel的一行记录 对应 一个DTO实例
						Object dtoInstance = dtoClass.newInstance();//一个DTO实例 对应 Excel的一行记录
						//值赋到DTO实例
						Map<String,Map<String,String>> otherMap = new HashMap<>();
						Set<String> keySets = row.keySet();
						for (String keySet : keySets) {
							if (keySet.contains(".")){
								Map<String, String> keyMap = otherMap.get(keySet.split("\\.")[0]);
								if (keyMap == null){
									keyMap = new HashMap<String,String>();
								}
								keyMap.put(keySet.split("\\.")[1],row.get(keySet));
								otherMap.put(keySet.split("\\.")[0],keyMap);
							}else {
								String setFieldMethodName = "set" + StringUtil.firstToUpper(keySet);
								Method setMethod = dtoClass.getMethod(setFieldMethodName, String.class);
								setMethod.invoke(dtoInstance, row.get(keySet));
							}
						}
						//其他Map
						if (otherMap != null && otherMap.keySet().size()>0){
							for (String s : otherMap.keySet()) {
								String setQtxxMethodName = "set" + StringUtil.firstToUpper(s);
								Method setQtxx = dtoClass.getMethod(setQtxxMethodName, String.class);
								setQtxx.invoke(dtoInstance, JSON.toJSONString(otherMap.get(s)));
							}
						}
						//设置录入人员
						String setLrryMethodName = "set" + StringUtil.firstToUpper("lrry");
						Method setLrry = dtoClass.getMethod(setLrryMethodName, String.class);
						setLrry.invoke(dtoInstance, fjcfbDto.getLrry());
						//设置删除标记
						String setScbjMethodName = "set" + StringUtil.firstToUpper("scbj");
						Method setScbj = dtoClass.getMethod(setScbjMethodName, String.class);
						setScbj.invoke(dtoInstance, "0");
						try {
							fileImport.insertImportRec((BaseModel)dtoInstance,user,a+1,row_errorMessages);
							redisUtil.hset("IMP_:_"+fjid,"insertMsg", "数据导入中，请稍候！导入进度为：" + (a+1) + "/" + al);
						} catch (BusinessException e) {
							logger.error("persistentFileAfterCheck----------"+e.getMessage());
							redisUtil.hset("IMP_:_"+fjid,"insertMsg", "数据导入中，请稍候！导入进度为：" + (a+1) + "/" + al + "-----"+e.getMessage());
						}
						errorMessage.append(row_errorMessages);
					}
				}
			} else {
				rows = read((String)redisUtil.hget("IMP_:_"+fjid, "wjlj"));
				redisUtil.hset("IMP_:_"+fjid,"insertEndflg", "0");
				redisUtil.hset("IMP_:_"+fjid,"insertMsg", "文件开始导入。");
				if(null != rows) {
					Map<String, String> impConfig = redisXmlConfig.getImpConfig(fjcfbDto.getYwlx());
					String dtoName = impConfig.get("class-entity");
					Class<?> dtoClass = Class.forName(dtoName);
					List<String> dtoFiledsNames = parseHead(rows);
					//再次删除第二行的检查信息
					rows.remove(0);

					String tsid="";//针对于请购导入的特殊处理，传递Qgid

					row_errorMessage = new StringBuffer();
					for(int a=0,al=rows.size(); a<al; a++){

						Map<String, String> row = rows.get(a);//Excel的一行记录 对应 一个DTO实例
						Object dtoInstance = dtoClass.newInstance();//一个DTO实例 对应 Excel的一行记录
						//值赋到DTO实例
						for(int b=0,bl=row.size(); b<bl; b++){
							if(dtoFiledsNames.size() <= b)continue;
							String dtoFieldName = dtoFiledsNames.get(b);
							if(StringUtil.isBlank(dtoFieldName)) continue;//不入库的列

							String cellValue = row.get(String.valueOf(b));

							if(dtoFieldName.split(",").length>0){
								for(String fieldName:dtoFieldName.split(",")){
									String setFieldMethodName = "set" + StringUtil.firstToUpper(fieldName);
									Method setMethod = dtoClass.getMethod(setFieldMethodName, String.class);
									setMethod.invoke(dtoInstance, cellValue);
								}
							}else{
								String setFieldMethodName = "set" + StringUtil.firstToUpper(dtoFieldName);
								Method setMethod = dtoClass.getMethod(setFieldMethodName, String.class);
								setMethod.invoke(dtoInstance, cellValue);
							}
						}
						//设置录入人员
						String setLrryMethodName = "set" + StringUtil.firstToUpper("lrry");
						Method setLrry = dtoClass.getMethod(setLrryMethodName, String.class);
						setLrry.invoke(dtoInstance, fjcfbDto.getLrry());
						//设置删除标记
						String setScbjMethodName = "set" + StringUtil.firstToUpper("scbj");
						Method setScbj = dtoClass.getMethod(setScbjMethodName, String.class);
						setScbj.invoke(dtoInstance, "0");
						//设置压缩文件路径
						if(StringUtil.isNotBlank(fjcfbDto.getYwlx()) && fjcfbDto.getYwlx().equals(BusTypeEnum.IMP_DOCUMENT_BATCH.getCode())){
							String setYsbljMethodName = "set" + StringUtil.firstToUpper("ysblj");
							Method setYsblj = dtoClass.getMethod(setYsbljMethodName, String.class);
							setYsblj.invoke(dtoInstance, fjcfbDto.getYsblj());
						}
						//当循环第一次执行时并且业务类型为请购所对应的类型，将UUid赋值给tsid
						if(BusTypeEnum.IMP_PURCHASE.getCode().equals(fjcfbDto.getYwlx())) {
							if(a==0) {
								tsid=StringUtil.generateUUID();
							}
							//设置请购ID
							String setTsidMethodName = "set" + StringUtil.firstToUpper("qgid");
							Method setQgid = dtoClass.getMethod(setTsidMethodName, String.class);
							setQgid.invoke(dtoInstance, tsid);
						}else if(BusTypeEnum.IMP_REPORT.getCode().equals(fjcfbDto.getYwlx())) {
							if(a==0) {
								tsid=StringUtil.generateUUID();
							}
							//设置患者ID
							String setTsidMethodName = "set" + StringUtil.firstToUpper("hzid");
							Method setHzid = dtoClass.getMethod(setTsidMethodName, String.class);
							setHzid.invoke(dtoInstance, tsid);
						}
						//当循环第一次执行时并且业务类型为领料导入所对应的类型，将UUid赋值给tsid
						else if(BusTypeEnum.IMP_REQUISITION.getCode().equals(fjcfbDto.getYwlx())) {
							if(a==0) {
								tsid=StringUtil.generateUUID();
							}
							//设置领料ID
							String setTsidMethodName = "set" + StringUtil.firstToUpper("llid");
							Method setLlid = dtoClass.getMethod(setTsidMethodName, String.class);
							setLlid.invoke(dtoInstance, tsid);
						}
						//当循环第一次执行时并且业务类型为领料导入所对应的类型，将UUid赋值给tsid
						else if(BusTypeEnum.IMP_SCREENING.getCode().equals(fjcfbDto.getYwlx())) {
							if(a==0) {
								tsid=StringUtil.generateUUID();
							}
							//设置领料ID
							String setTsidMethodName = "set" + StringUtil.firstToUpper("fzjcid");
							Method setFzjcid = dtoClass.getMethod(setTsidMethodName, String.class);
							setFzjcid.invoke(dtoInstance, tsid);
						}
						//当循环第一次执行时并且业务类型为领料导入所对应的类型，将UUid赋值给tsid
						else if(BusTypeEnum.BOM_IMPORT.getCode().equals(fjcfbDto.getYwlx())) {
							if(a==0) {
								tsid=StringUtil.generateUUID();
							}
							//设置领料ID
							String setTsidMethodName = "set" + StringUtil.firstToUpper("bomid");
							Method setBomid = dtoClass.getMethod(setTsidMethodName, String.class);
							setBomid.invoke(dtoInstance, tsid);
						}
						//当循环第一次执行时并且业务类型为领料导入所对应的类型，将UUid赋值给tsid
						else if(BusTypeEnum.IMP_PERFORMANCE_TEMPLATE.getCode().equals(fjcfbDto.getYwlx())) {
							if(a==0) {
								tsid=StringUtil.generateUUID();
							}
							//设置绩效目标ID
							String setTsidMethodName = "set" + StringUtil.firstToUpper("jxmbid");
							Method setJxmbid = dtoClass.getMethod(setTsidMethodName, String.class);
							setJxmbid.invoke(dtoInstance, tsid);
						}
						fileImport.insertImportRec((BaseModel)dtoInstance,user,a + 1,row_errorMessage);
						redisUtil.hset("IMP_:_"+fjid,"insertMsg", "数据导入中，请稍候！导入进度为：" + (a+1) + "/" + al);
					}
					errorMessage.append(row_errorMessage);
				}
			}
			String msg = "数据导入完成！";
			if(StringUtil.isNotBlank(errorMessage.toString())) {
				msg = msg + "以下数据存在问题，未导入成功。<br/>" + errorMessage.toString();
			}
			redisUtil.hset("IMP_:_"+fjid,"insertMsg", msg);
			redisUtil.hset("IMP_:_"+fjid,"insertEndflg", "1");
		}catch(BusinessException e){
			redisUtil.hset("IMP_:_"+fjid,"insertEndflg", "1");
			redisUtil.hset("IMP_:_"+fjid,"insertMsg",  errorMessage.toString() + row_errorMessage.toString()+ "<br/>" + e.getMessage());
			redisUtil.hset("IMP_:_"+fjid,"insertWarn",  errorMessage.toString() + row_errorMessage.toString() + "<br/>" + e.getMessage());
		}catch(Exception e){
			redisUtil.hset("IMP_:_"+fjid,"insertEndflg", "1");
			redisUtil.hset("IMP_:_"+fjid,"insertMsg", "数据导入失败!" + errorMessage.toString() + row_errorMessage.toString() + "<br/>" + e.getMessage());
			redisUtil.hset("IMP_:_"+fjid,"insertWarn", "数据导入失败!" + errorMessage.toString() + row_errorMessage.toString() + "<br/>"  + e.getMessage());
			logger.error(e.toString());
		}
		return true;
	}
	
	/**
	 * 持久化结果集（2）
	 * 注意：别忘了事务那点事
	 * @author goofus
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean persistentNormalFileAfterCheck() {
		boolean warn = true;
		StringBuffer msg = new StringBuffer();
		try{
			List<Map<String, String>> rows = read((String)redisUtil.hget("IMP_:_"+fjid, "wjlj"));
			String kzcs1 = (String)redisUtil.hget("IMP_:_"+fjid, "kzcs1");
			String kzcs2 = (String)redisUtil.hget("IMP_:_"+fjid, "kzcs2");
			String kzcs3 = (String)redisUtil.hget("IMP_:_"+fjid, "kzcs3");
	
			redisUtil.hset("IMP_:_"+fjid,"insertEndflg", "0");
			redisUtil.hset("IMP_:_"+fjid,"insertMsg", "文件开始导入。");
			
			if(null != rows) {
				msg.append("<table class=\"table table-striped table-bordered text-left\" style=\"background-color:#fff;\" id=\"confirmtable\"><tbody id=\\\\\\\"tb_thesisProject\\\\\\\">");
				msg.append("<tr>");
				for(int headNum =0;headNum < rows.get(0).size();headNum ++){
					msg.append("<th>");
					msg.append(rows.get(0).get(String.valueOf(headNum)));
					msg.append("</th>");
				}
				msg.append("</tr>");
				int size = rows.get(0).size();
				//再次删除第二行的检查信息
				rows.remove(0);
				int maxRow;
				int maxCol = 0;
				maxRow = Math.min(rows.size(), 10);
				for(int rownum =0;rownum < maxRow;rownum ++) {
					if(maxCol < rows.get(rownum).size()) {
						maxCol = rows.get(rownum).size();
					}
				}
				
				for(int a=0,al=rows.size(); a<al; a++){
					
					Map<String, String> row = rows.get(a);//Excel的一行记录 对应 一个DTO实例
					row.put("size", String.valueOf(size));
					row.put("kzcs1", kzcs1);
					row.put("kzcs2", kzcs2);
					row.put("kzcs3", kzcs3);
					row.put("kzcs4", fjcfbDto.getKzcs4());
					row.put("number", String.valueOf(a+1));
					row.put("dxmb", fjcfbDto.getDxmb());
					row.put("sms_sign", fjcfbDto.getSms_sign());
					row.put("file_name", (String)redisUtil.hget("IMP_:_"+fjid, "wjm"));
					
					boolean result = fileImport.insertNormalImportRec(row,user);
					if(!result){
						msg.append("<tr>");
						for(int colnum =0;colnum < maxCol;colnum ++) {
							msg.append("<td>");
							if(colnum >= rows.get(a).size())
								msg.append("&nbsp;");
							else
								msg.append(rows.get(a).get(String.valueOf(colnum)));
							msg.append("</td>");
						}
						msg.append("</tr>");
						warn = false;
					}
					redisUtil.hset("IMP_:_"+fjid,"insertMsg", "数据导入中，请稍候！导入进度为：" + (a+1) + "/" + al);
				}
				msg.append("<tr><td colspan=");
				msg.append(size);
				msg.append(">一共有记录 ");
				msg.append(rows.size());
				msg.append("条</td></tr>");
				msg.append("</tbody></table>");
			}
			if(!warn){
				redisUtil.hset("IMP_:_"+fjid, "insertWarn", msg);
			}
			redisUtil.hset("IMP_:_"+fjid,"insertMsg", "数据导入完成!");
			redisUtil.hset("IMP_:_"+fjid,"insertEndflg", "1");
		}catch(Exception e){
			redisUtil.hset("IMP_:_"+fjid,"insertEndflg", "1");
			redisUtil.hset("IMP_:_"+fjid,"insertMsg", "数据导入失败!");
		}
		return true;
	}
	
	/**
	 * 持久化结果集（2）
	 * 注意：别忘了事务那点事
	 * @author goofus
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean persistentSpecialFileAfterCheck() {

		try{
			List<Map<String, String>> rows = read((String)redisUtil.hget("IMP_:_"+fjid, "wjlj"));
			String kzcs1 = (String)redisUtil.hget("IMP_:_"+fjid, "kzcs1");
			String kzcs2 = (String)redisUtil.hget("IMP_:_"+fjid, "kzcs2");
			String kzcs3 = (String)redisUtil.hget("IMP_:_"+fjid, "kzcs3");
	
			redisUtil.hset("IMP_:_"+fjid,"insertEndflg", "0");
			redisUtil.hset("IMP_:_"+fjid,"insertMsg", "文件开始导入。");
			
			if(null != rows) {
				
				for(int a=0,al=rows.size(); a<al; a++){
	
					Map<String, String> row = rows.get(a);//Excel的一行记录 对应 一个DTO实例
					
					row.put("kzcs1", kzcs1);
					row.put("kzcs2", kzcs2);
					row.put("kzcs3", kzcs3);
					row.put("number", String.valueOf(a+1));
					
					fileImport.insertNormalImportRec(row,user);
	
					redisUtil.hset("IMP_:_"+fjid,"insertMsg", "数据导入中，请稍候！导入进度为：" + (a+1) + "/" + al);
				}
			}
			redisUtil.hset("IMP_:_"+fjid,"insertMsg", "数据导入完成!");
			redisUtil.hset("IMP_:_"+fjid,"insertEndflg", "1");
		}catch(Exception e){
			redisUtil.hset("IMP_:_"+fjid,"insertEndflg", "1");
			redisUtil.hset("IMP_:_"+fjid,"insertMsg", "数据导入失败!");
		}
		return true;
	}
	
	/**
	 * 文件模块批量导入处理
	 */
	public void FileDocumentImportCheck() throws BusinessException{
		try{
			redisUtil.hset("IMP_:_"+fjid,"endflg", "0");
			
			before();
			
			//判断是否为压缩文件
			String wjlj = (String)redisUtil.hget("IMP_:_"+fjid, "wjlj");
			int index = (wjlj.lastIndexOf(".") > 0) ? wjlj.lastIndexOf(".") : wjlj.length() - 1;
			String suffix = wjlj.substring(index);
			if(".zip".equals(suffix) || ".rar".equals(suffix)){
				//判断是否已经上传文件数据
				String prefjid = (String)redisUtil.hget("IMP_:_"+fjid, "prefjid");
				if(StringUtil.isBlank(prefjid)){
					redisUtil.hset("IMP_:_"+fjid,"errorMessage", "请先上传文件数据信息！");
					after();
					return;
				}
				List<Map<String,String>> t_rows = (List<Map<String, String>>) redisUtil.hget("IMP_:_"+prefjid,"wjglList");
				if(t_rows==null||t_rows.isEmpty()){
					redisUtil.hset("IMP_:_"+fjid,"errorMessage", "请先上传文件数据信息！");
					after();
					return;
				}
				//解压，重新生成数据list更改缓存
				int t_index = wjlj.lastIndexOf("/");
				String folder = wjlj.substring(0, t_index);
                String unZipFile = ZipUtil.unZipFile(folder, wjlj);
    			File dirFile = new File(unZipFile);
    			if(dirFile.exists()){
    				// 递归获取全部文件
    				List<File> files = getAllFile(new ArrayList<>() , dirFile);
    				if (files != null) {
    					for (File fileChildDir : files) {
    		            	if (fileChildDir.isFile()) {
    		                	//文件名
    		            		if(fileChildDir.getName().lastIndexOf(".") == -1){
    		            			redisUtil.hset("IMP_:_"+fjid,"errorMessage", "压缩文件格式缺失："+fileChildDir.getName());
    		            			logger.error("压缩文件格式缺失："+fileChildDir.getName());
    		            			return;
    		            		}
    		                	String fileName = fileChildDir.getName().substring(0,fileChildDir.getName().lastIndexOf("."));
								for (Map<String, String> t_row : t_rows) {
									if (fileName.equals(t_row.get("wjmc"))) {
										t_row.put("sfsc", "是");
									}
								}
    		                }
    		            }
    				}     
    			}
    			redisUtil.hset("IMP_:_"+fjid,"wjglList", t_rows);
			}else{
				//正常校验文件，生成数据list加入缓存
				List<Map<String,String>> rows = read((String)redisUtil.hget("IMP_:_"+fjid, "wjlj"));
				List<String> dtoFiledsName = parseHead(rows);
				List<Map<String,String>> defined = parseCheckInfo(rows);
				//检查模板标题
				boolean isSucess = fileImport.checkDefined(defined);
				if(isSucess) {
					check(rows,defined,dtoFiledsName);
					transform(rows,defined);
					transToPage();
					//表头信息解析
					for (Map<String, String> rowinfo : rows) {
						for (int i = 0, j = dtoFiledsName.size(); i < j; i++) {
							String value = rowinfo.get(i + "");
							rowinfo.remove(i + "");
							rowinfo.put(dtoFiledsName.get(i), value);
						}
						rowinfo.put("sfsc", "否");
					}
					redisUtil.hset("IMP_:_"+fjid,"wjglList", rows);
				}
			}
			redisUtil.hset("IMP_:_"+fjid,"errorMessage", errorMessage);
			redisUtil.hset("IMP_:_"+fjid,"confirmMessage", confirmMessage);
			redisUtil.hset("IMP_:_"+fjid,"warnMessage", warnMessage);
			redisUtil.hset("IMP_:_"+fjid,"hintMessage", hintMessage);
			redisUtil.hset("IMP_:_"+fjid,"warnRecList", warnRecList);
			
			after();
			
		}catch(Exception e){
			redisUtil.hset("IMP_:_"+fjid,"errorMessage", e.getMessage());
			throw new BusinessException("",e.getMessage());
		}
	}
	
	/**
	 * 获取目录下所有的文件并返回所有文件列表，使用递归方式完成
	 */
	public List<File> getAllFile(List<File> listFile, File paramFile) {
		if (paramFile.isFile()) {// 文件，添加到文件列表中，本次调用结束，返回文件列表
			listFile.add(paramFile);
			return listFile;
		} else {// 目录
			File[] localFiles = paramFile.listFiles();
			if (localFiles != null) {
				for (File localFile : localFiles) {
					getAllFile(listFile, localFile);
				}
			}
			// 空目录，本次调用结束，返回文件列表
			return listFile;
		}
	}
	
}
