package com.matridx.igams.wechat.util;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.PictureFloat;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum_pub;
import com.matridx.igams.common.service.impl.MatridxByteArrayResource;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.util.ImgQrCodeUtil;
import com.matridx.igams.common.util.valuate.EvaluableExpression;
import com.matridx.igams.common.util.valuate.ValuateFunction;
import com.matridx.igams.common.util.valuate.function.Function;
import com.matridx.igams.wechat.config.RabbitMq2Utils;
import com.matridx.igams.wechat.dao.entities.*;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.TextAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.dom4j.DocumentException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGraphicalObject;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTAnchor;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDrawing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcBorders;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class XWPFWordUtil extends BaseWord {
	private Logger log = LoggerFactory.getLogger(XWPFWordUtil.class);

	//判断特定的标本类型 需要生成特定的报告
	String[] yblxs=new String[] {"B","L","N","T"};
	//全局变量  检测类型
	String globalJclx=null;
	//全局变量   模板标记
	String tmpate_flag=null;
	//是否收费 0 是不收费
	boolean free=false;
	//是否添加水印  1不添加 其他添加
	boolean addWatermark=false;

	Map<String,Object> stringObjectMap = new HashMap<>();
	{
		stringObjectMap.put("中枢","N,A");
		stringObjectMap.put("血流","B");
	}

	// 需要初始化的公式
	private List<String> functions = Arrays.asList("isNotBlank","isBlank","substring","length","indexOf","append","equals","replaceAll","dateParse","dateFormat","stringJoin");

	private Map<String, Function> functionMap;

	@Autowired(required = false)
	private RabbitMq2Utils rabbitMq2Utils;
	@Autowired(required = false)
	private CustomerConfirmCallback confirmCallback;
	@Autowired(required = false)
	private ReturnCallback returnCallback;
	public  static XWPFWordUtil xwpfWordUtil;

	@PostConstruct
	public void init(){
		xwpfWordUtil=this;
		xwpfWordUtil.rabbitMq2Utils=this.rabbitMq2Utils;
		xwpfWordUtil.confirmCallback=confirmCallback;
		xwpfWordUtil.returnCallback=returnCallback;
	}
	/**
	 * 初始化格式化公式
	 * @return
	 */
	public void initFormatFunction(){
		Map<String, Function> temp_functionMap = new HashMap<>();
		if (!CollectionUtils.isEmpty(functions)){
			for (String function : functions) {
				switch (function) {
					case "isNotBlank":
						temp_functionMap.put("isNotBlank", ValuateFunction.isNotBlank);// isNotBlank(a)
						break;
					case "isBlank":
						temp_functionMap.put("isBlank", ValuateFunction.isBlank);// isBlank(a)
						break;
					case "substring":
						temp_functionMap.put("substring", ValuateFunction.substring);// substring(string,beginIndex,endIndex)
						break;
					case "length":
						temp_functionMap.put("length", ValuateFunction.length);// length(a)
						break;
					case "indexOf":
						temp_functionMap.put("indexOf", ValuateFunction.indexOf);// indexOf(String, a)
						break;
					case "append":
						temp_functionMap.put("append", ValuateFunction.append);// append(a,b,c)
						break;
					case "equals":
						temp_functionMap.put("equals", ValuateFunction.equals);// equals(a,b)
						break;
					case "replaceAll":
						temp_functionMap.put("replaceAll", ValuateFunction.replaceAll);// replaceAll(String str, String regex, String replacement)
						break;
					case "dateParse":
						temp_functionMap.put("dateParse", ValuateFunction.dateParse);// dateParse(a,日期格式)
						break;
					case "dateFormat":
						temp_functionMap.put("dateFormat", ValuateFunction.dateFormat);// dateFormat(a,日期格式)
						break;
					case "stringJoin":
						temp_functionMap.put("stringJoin", ValuateFunction.stringJoin);// stringJoin(delimiter,elements)
						break;
				}
			}
		}
		this.functionMap = temp_functionMap;
	}

	public boolean GenerateReport(Map<String, Object> map,IFjcfbService fjcfbService,AmqpTemplate amqpTempl,String DOC_OK,String FTP_URL,ISjxxService sjxxService) {
		//生成正式报告---根据模板生成报告
		ExportWord(map, fjcfbService, amqpTempl, DOC_OK, FTP_URL,true,sjxxService);
		//测试用报告---合作伙伴为无的
//		Map<String, Object> map_hbmc_no=map;
//		map_hbmc_no.put("templPath", map.get("templPath_no"));
//		map_hbmc_no.put("wjm",map.get("wjm_no"));
//		map_hbmc_no.put("hbmc",map.get("hbmc_no"));
//		map_hbmc_no.put("ywlx",map.get("ywlx_no"));
//		map_hbmc_no.put("tmpate_flag",map.get("tmpate_flag_no"));
//		String s = (String) map_hbmc_no.get("tmpate_flag");
//		ExportWord(map_hbmc_no, fjcfbService, amqpTempl, DOC_OK, FTP_URL,false,null);
		return true;
	}



	/**
	 * 生成报告
	 * @param map
	 * @param fjcfbService
	 * @param amqpTempl
	 * @param DOC_OK
	 * @param FTP_URL
	 * @param ToPDF
	 * @param sjxxService
	 * @return
	 */
	public boolean ExportWord(Map<String, Object> map,IFjcfbService fjcfbService,AmqpTemplate amqpTempl,String DOC_OK,String FTP_URL,boolean ToPDF,ISjxxService sjxxService) {
		//先默认一个 参考文献（refs  以refs_definition为默认）
		initFormatFunction();
		//参考文献根据不同的模板有不同的规则
		ZipSecureFile.setMinInflateRatio(-1.0d);
		String refs=map.get("refs_definition")==null?"":map.get("refs_definition").toString();
		map.put("refs",refs);
		log.error("开启线程：生成报告--------------开始");
		FileInputStream fileInputStream = null;
		XWPFDocument document = null;
		OutputStream output = null;
		OutputStream output_back;
		try {
			String totalValue = "";
			for(String key:map.keySet()){
				Object o_value = map.get(key);
				String value = "[null]";
				if(o_value!=null) {
                    value = o_value.toString();
                }
				totalValue = totalValue + key +":"+ value + ";";
			}
			log.error(totalValue);
			// 如果模板文件不存在则直接返回
			String templateFilePath = (String) map.get("templPath");
			String tempFilePath=(String) map.get("tempFilePath");
			String ywlx=(String) map.get("ywlx");
			String wjm=(String) map.get("wjm");
			String prefix=(String) map.get("prefix");
			String releaseFilePath = (String) map.get("releaseFilePath");
			File templateFile = new File(templateFilePath);
			if (StringUtil.isBlank(templateFilePath) || !templateFile.exists()) {
				log.error("生成报告--------------模板不存在！请重新确认模板！" +templateFilePath);
				return false;
			}
			//生成文件编号
			StringBuilder wjbhdm = new StringBuilder();
			String wjbh = "";
			String yblxdm=(String) map.get("yblxdm");
			//判断是Q还是其他
			if(isHave(yblxs, yblxdm)) {
				//Q
				wjbhdm.append("Q");
			}else {
				//其他
				wjbhdm.append("N");
			}
			//判断是去人源还是不去人源
			String sampleId = "";
			if (map.get("sample_id")!=null){
				sampleId = map.get("sample_id").toString().toUpperCase();
			}
			if (sampleId.contains("-REM")){
				wjbhdm.append("R");
			}else {
				wjbhdm.append("N");
			}
			int wjbhindex = -1;
			if ("QN".equals(wjbhdm.toString())){
				//Q正常
				wjbhindex=0;
			}else if ("QR".equals(wjbhdm.toString())){
				//Q去人源
				wjbhindex=1;
			}else if ("NN".equals(wjbhdm.toString())){
				//非Q正常
				wjbhindex=2;
			}else if ("NR".equals(wjbhdm.toString())){
				//非Q去人源
				wjbhindex=3;
			}
			if (map.get("bgmcCskz3")!=null){
				String[] bgmcCskz3s = map.get("bgmcCskz3").toString().split(",");
				if (bgmcCskz3s.length==4 && wjbhindex!=-1) {
					wjbh = bgmcCskz3s[wjbhindex];
				}
			}
			map.put("wjbh",wjbh);
			//判断是否是为免费
			free= map.get("sfsf") != null && ("0".equals(map.get("sfsf").toString()));
			// 判断PDF是否添加水印  mod 添加水印方式更改 扩展参数不为1时加水印  2021-6-9 lsy 
			addWatermark= map.get("cskz2") == null || !"1".equals(map.get("cskz2"));
			// 把文件放入流
			fileInputStream = new FileInputStream(templateFile);
			// 读取word文件
			document = new XWPFDocument(fileInputStream);
			// 判断检测项目类型，进行修改模板
			String jclx = (String) map.get("jclx");
			//针对医院等特殊模板显示检测结果
			String pathogens_hospital=map.get("pathogens_hospital")!=null?(String) map.get("pathogens_hospital"):null;
			//把检测类型作为全局变量
			globalJclx=jclx;
			//判断不同的检测类型所对应不同的信息
			if("R".equals(globalJclx)) {
				map.put("jcxm_bm", "核糖核酸（RNA）");
				map.put("jcxm", "RNA测序");
				map.put("jcxmlx", "RNA");
				map.put("RNACheckOut","是");
				//«DELETE_ROW» 为删除标记，只要出现，即删除，不用判断其他条件
				map.put("DNACheckOut","«DELETE_ROW»");
				if(StringUtil.isBlank(pathogens_hospital)) {
					map.put("pathogens_hospital","RNA病毒检测阴性");
				}
			}else if("D".equals(globalJclx)) {
				map.put("jcxm_bm", "脱氧核糖核酸（DNA）");
				map.put("jcxm", "DNA测序");
				map.put("jcxmlx", "DNA");
				map.put("DNACheckOut","是");
				//«DELETE_ROW» 为删除标记，只要出现，即删除，不用判断其他条件
				map.put("RNACheckOut","«DELETE_ROW»");
				if(StringUtil.isBlank(pathogens_hospital)) {
					map.put("pathogens_hospital","DNA测序结果考虑阴性");
				}
			}else if("C".equals(globalJclx)) {
				map.put("jcxm_bm", "脱氧核糖核酸和核糖核酸（DNA+RNA）");
				map.put("jcxm", "DNA+RNA测序");
				map.put("jcxmlx", "DNA+RNA");
				map.put("RNACheckOut","是");
				map.put("DNACheckOut","是");
			}
			//判断模板标记是否为空，为空时直接返回
			if(map.get("tmpate_flag")==null) {
				log.error("未识别出模板类型");
				return false;
			}
			//判断是否是生成测试报告（合作伙伴为无）
			tmpate_flag =map.get("tmpate_flag").toString();
			//提前生成word附件id
			String fjid = StringUtil.generateUUID();
			map.put("qrCodeFjid",fjid);
			log.error("替换模板中信息--------------开始");
			String jcxmdm=(String) map.get("jcxmdm");
			if ("061".equals(jcxmdm)){
				boolean flag = true;
				for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
					if (entry.getValue().toString().contains(yblxdm)){
						removeSuperfluousTable(document,"1",entry.getKey());
						flag = false;
						break;
					}
				}
				if (flag){
					removeSuperfluousTable(document, "1", "呼吸");
				}
			}
			//如果模板标记为IMP_REPORT_QINDEX_TEMEPLATE或者IMP_REPORT_ONCO_QINDEX_TEMEPLATE 或者 IMP_SPEED生成新Q-mNGS报告
			if("IMP_REPORT_QINDEX_TEMEPLATE".equals(tmpate_flag) || "IMP_REPORT_ONCO_QINDEX_TEMEPLATE".equals(tmpate_flag) || (StringUtil.isNotBlank(tmpate_flag) && tmpate_flag.contains("IMP_REPORT_SEQ_")) || "IMP_REPORT_QINDEX_TEMEPLATE_REM".equals(tmpate_flag) || "IMP_REPORT_ONCO_QINDEX_TEMEPLATE_REM".equals(tmpate_flag) || "IMP_SPEED".equals(tmpate_flag) || "IMP_SPEED_REM".equals(tmpate_flag)|| "IMP_REPORT_RFS_TEMEPLATE".equals(tmpate_flag)|| "IMP_REPORT_TNGS_TEMEPLATE".equals(tmpate_flag)) {
				//判断标本类型代码是否是  B L N生成Q-index报告
				if(isHave(yblxs, yblxdm)) {
					log.error("生成定量报告");
					map.put("Qtitle","定量");
					//特殊的报告
					new_template_special(document,map,jclx);
				}else {
					log.error("生成病原报告");
					map.put("Qtitle","病原");
					//生成一般报告
					new_template(document,map,jclx);
				}
			}else {
				log.error("生成普通PDC-seq报告");
				//如果不是生成普通PDC-seq报告
				old_template(document,map,jclx);
			}
			log.error("替换模板中信息--------------结束");
			
			// 关闭流
			fileInputStream.close();
			// 写到临时文件夹
			
			//根据日期创建文件夹
			String storePath = mkDirs(ywlx,tempFilePath);
			int index = (wjm.lastIndexOf(".") > 0) ? wjm.lastIndexOf(".") : wjm.length() - 1;
			String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
			String suffix =wjm.substring(index);
			String saveName = t_name + suffix;
			String backName = t_name + "_back" + suffix;
			output = new FileOutputStream(storePath + "/" + saveName);
			output_back=new FileOutputStream(storePath + "/" + backName);
			//写入文件
			document.write(output);
			document.write(output_back);
			//关闭流
			document.close();
			output.close();
			//保存到正式文件夹并保存到附件存放表中
			
			//文件路径
			String pathString = storePath;
			//文件名
			String report_wjm;

			//若关联合作伙伴存在文件名规则，则将发送的文件名替换，扩展名不变
			if (map.get("sjhbxxwjmgz")!=null && StringUtil.isNotBlank(map.get("sjhbxxwjmgz").toString())){
				if(map.get("sfqry")!=null&&"1".equals(map.get("sfqry").toString())) {
					report_wjm = ("1".equals(map.get("bfwjbj"))?"备案_":"") + replaceWjm(map,map.get("sjhbxxwjmgz").toString()) +"——(去人源)"+suffix;
				}else {
					report_wjm = ("1".equals(map.get("bfwjbj"))?"备案_":"") + replaceWjm(map,map.get("sjhbxxwjmgz").toString()) + suffix;
				}
			}else {
				if(map.get("sfqry")!=null&&"1".equals(map.get("sfqry").toString())) {
					//如果扩展参数为1 生成的文件名字 加  去人源
					if(map.get("bgmbcskz1")!=null&&"1".equals(map.get("bgmbcskz1").toString())) {
						report_wjm=("1".equals(map.get("bfwjbj"))?"备案_":"") + (!String.valueOf(map.get("hbmc")).contains("艾迪康") ?(map.get("mcpjdqbg_cskz1"))+"_":"")+(map.get("ybbh")!=null?(map.get("ybbh")):"")+suffix;
					}else {
						report_wjm =("1".equals(map.get("bfwjbj"))?"备案_":"") + (!String.valueOf(map.get("hbmc")).contains("艾迪康") ?(map.get("mcpjdqbg_cskz1"))+"_":"")+ map.get("hzxm") + "(" + map.get("yblxmc") + ")"+(map.get("sjdw")!=null?("_"+map.get("sjdw")):"") +(map.get("ybbh")!=null?("_"+map.get("ybbh")):"")+"——(去人源)"+suffix;
					}
				}else {
					if(map.get("bgmbcskz1")!=null&&"1".equals(map.get("bgmbcskz1").toString())) {
						report_wjm=("1".equals(map.get("bfwjbj"))?"备案_":"") + (!String.valueOf(map.get("hbmc")).contains("艾迪康") ?(map.get("mcpjdqbg_cskz1"))+"_":"")+(StringUtil.isNotBlank(String.valueOf(map.get("ybbh")))?(map.get("ybbh")):"")+suffix;
					}else {
						report_wjm =("1".equals(map.get("bfwjbj"))?"备案_":"") + (!String.valueOf(map.get("hbmc")).contains("艾迪康") ?(map.get("mcpjdqbg_cskz1"))+"_":"")+ map.get("hzxm") + "(" + map.get("yblxmc") + ")"+(map.get("sjdw")!=null?("_"+map.get("sjdw")):"")  +(map.get("ybbh")!=null?("_"+map.get("ybbh")):"")+suffix;
					}
				}
			}
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(map.get("sjid").toString());
			fjcfbDto.setFjid(fjid);
			fjcfbDto.setYwlx(ywlx);
			fjcfbService.deleteByYwidAndYwlx(fjcfbDto);


			Map<String,String> paramMap=new HashMap<>();
			paramMap.put("wjm",report_wjm);
			paramMap.put("ywid",map.get("sjid").toString());
			List<Map<String,String>> fjList=sjxxService.getFjByWjm(paramMap);
			if(!CollectionUtils.isEmpty(fjList)){
				Map<String,String> fjMap=fjList.get(0);
				if(fjMap!=null&&report_wjm.equals(fjMap.get("wjm"))){
					String[] wjmArr=report_wjm.split(suffix);
					report_wjm=wjmArr[0]+" _{cnt}"+suffix;
				}
			}
			report_wjm=replaceWjm(report_wjm);
			//分文件名
			String fwjm = saveName;
			//文件全路径
			String wjlj = storePath + "/" + saveName;
			
			//根据临时文件夹创建正式文件
			String t_path = pathString.substring(prefix.length() + tempFilePath.length());
			//分文件路径
			String real_path = prefix + releaseFilePath + t_path;
			
			mkDirs(null,real_path);
			//把文件从临时文件夹中移动到正式文件夹中，如果已经存在则覆盖
			CutFile(wjlj,real_path+"/"+fwjm,true);
			//将正式文件夹路径更新至数据库
			DBEncrypt bpe = new DBEncrypt();

			fjcfbDto.setYwid(map.get("sjid").toString());
			fjcfbDto.setZywid("");
			fjcfbDto.setXh("1");
			fjcfbDto.setWjm(report_wjm);
			fjcfbDto.setWjlj(bpe.eCode(real_path+"/"+fwjm));
			fjcfbDto.setFwjlj(bpe.eCode(real_path));
			fjcfbDto.setFwjm(bpe.eCode(fwjm));
			fjcfbDto.setZhbj("0");
			boolean isTrue=fjcfbService.insert(fjcfbDto);
			if(map.get("hblx")!=null) {
				if("4".equals(map.get("hblx").toString())) {
					if(sjxxService!=null) {
						sjxxService.sendFileToAli(fjcfbDto);
					}
				}
			}
// 			if(isTrue) {
//				//刪除之前的word
//				fjcfbService.deleteByYwidAndYwlx(fjcfbDto);
//			}
			if(ToPDF) {
				//转换PDF
				String wjljjm=bpe.dCode(fjcfbDto.getWjlj());
				//连接服务器
				boolean sendFlg=sendWordFile(wjljjm,FTP_URL);
				log.error(wjljjm+" 发送word状态："+sendFlg);
				if(sendFlg) {
					log.error("进入发送rabbit");
					Map<String,String> pdfMap= new HashMap<>();
					if(free&&addWatermark) {
						pdfMap.put("watermark", "1");
					}
					pdfMap.put("wordName", fwjm);
					pdfMap.put("fwjlj",fjcfbDto.getFwjlj());
					pdfMap.put("fjid",fjcfbDto.getFjid());
					pdfMap.put("ywlx",map.get("ywlxToPDF").toString());
					pdfMap.put("MQDocOkType",DOC_OK);
					//暂时处理，盖章只针对2.0和3.0和onco项目
					//增加resFirst报告的盖章2023.04.17
					if (map.get("ywlx").toString().indexOf("IMP_REPORT_ONCO_QINDEX_TEMEPLATE") >-1 || map.get("ywlx").toString().indexOf("IMP_REPORT_SEQ_") >-1 || map.get("ywlx").toString().indexOf("IMP_REPORT_RFS_TEMEPLATE") >-1){
						pdfMap.put("gzlxmc", (String)map.get("gzlxmc"));
					}
					pdfMap.put("shrq", (String)map.get("shsj"));
					pdfMap.put("shry", (String)map.get("shryzsxm"));
					pdfMap.put("jyry", (String)map.get("jyryzsxm"));
					pdfMap.put("fsbs", (String)map.get("bfwjbj"));
					pdfMap.put("jclxid", (String)map.get("jclxid"));
					pdfMap.put("jclx", (String)map.get("jclx"));
					if(map.get("first_report_accurate_time")!=null) {
						pdfMap.put("scbgrq", (String) map.get("first_report_accurate_time"));
					}
					if(map.get("sendFlag")!=null){
						pdfMap.put("sendFlag", (String)map.get("sendFlag"));
					}
					if(map.get("sendmail")!=null){
						pdfMap.put("sendmail", (String)map.get("sendmail"));
					}
					//删除PDF
					log.error("进入发送rabbit删除PDF");
					FjcfbDto fjcfbDto_t=new FjcfbDto();
					fjcfbDto_t.setYwlx(pdfMap.get("ywlx"));
					fjcfbDto_t.setYwid(map.get("sjid").toString());
					fjcfbService.deleteByYwidAndYwlx(fjcfbDto_t);
					log.error("进入发送rabbit删除完成");
					try{
						log.error("发送转换wordtopdf 开始--------------:"+JSONObject.toJSONString(pdfMap));

						RabbitTemplate rabbitTemplate=(RabbitTemplate)amqpTempl;
						rabbitTemplate.setConfirmCallback(xwpfWordUtil.confirmCallback);
						rabbitTemplate.setReturnCallback(xwpfWordUtil.returnCallback);
						rabbitTemplate.convertAndSend("doc2pdf_exchange",MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(pdfMap));
						log.error("发送转换wordtopdf 结束-------------:"+JSONObject.toJSONString(pdfMap));
						//发送Rabbit消息转换pdf

					}catch (Exception e){
						log.error(e.getMessage());
						if(xwpfWordUtil.rabbitMq2Utils!=null){
							xwpfWordUtil.rabbitMq2Utils.sendDownloadReportMsg(pdfMap);
						}
					}

				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			log.error(e.fillInStackTrace().toString());
		} finally {
			try {
				if (output != null) {
                    output.close();
                }
				if (document != null) {
                    document.close();
                }
				if (fileInputStream != null) {
					fileInputStream.close();
				}
			} catch (Exception e) {
				log.error(e.toString());
			}
		}
		return true;

	}

	public String replaceWjm(String report_wjm){
		if(StringUtil.isNotBlank(report_wjm)){
			// 循环处理所有前缀下划线
			while(report_wjm.startsWith("_")) {
				report_wjm = report_wjm.substring(1);
			}
		}
		return report_wjm;
	}

	/**
	 * 文件名替换方法
	 * @param map
	 * @param sjhbxxwjmgz
	 * @return
	 */
	private String replaceWjm(Map<String, Object> map,String sjhbxxwjmgz){
		//遍历sjhbxxwjmgz内容中«»的值，设为map的key，获取map对应的value，并替换«»的内容
		for (;sjhbxxwjmgz.contains("«");){
			int startIndex = sjhbxxwjmgz.indexOf("«")+1;
			int endIndex = sjhbxxwjmgz.indexOf("»");
			String lsythnr = sjhbxxwjmgz.substring(startIndex,endIndex);
			Object o = map.get(lsythnr);
			String lsxthnr = "";
			if (o!=null && o!=""){
				lsxthnr = o.toString();
			}
			sjhbxxwjmgz = sjhbxxwjmgz.replace("«"+lsythnr+"»",lsxthnr);
		}

		return sjhbxxwjmgz;
	}

	/**
	 * 替换表格中的信息，每一个表格执行一次。
	 * 先根据检测类型 是D 还是R，对表格里的特殊变量进行处理，D要隐藏R的表格，R里要删除D的表格
	 * 进行整个表格的循环处理，获取最后一行的第一个cell，判断是否为 List. ，如果是则进行替换，2025-08-17增加对多条件(多个==)判断的支持
	 * 如果是 «Iterate. ,则进行循环体，主要处理 病原体解释等，只有一列的情况
	 * 最后调用dealPtTable 对普通表格里的额字段进行替换。 2025-08-17 在普通处理增加对循环体 «Iterate的处理支持
	 *
	 * 当前在
	 * @param table   //当前迭代的表格
	 * @param map //需要替换的数据
	 */
	private void replaceTable(XWPFTable table, Map<String, Object> map, String jclx) {
		if("R".equals(jclx)) {
			if(table.getRows().size()==1) {
				String text=table.getRows().get(0).getCell(0).getText();
				if(text.contains("«results")) {
					for (int j = table.getRows().size()-1; j > -1; j--) {
						table.removeRow(j);
					}
					return;
				}
			}else if(table.getRows().size()==2) {
				String text=table.getRows().get(1).getCell(0).getText();
				if(text.contains("«italic_Gene")||text.contains("«Gene")) {
					for (int j = table.getRows().size()-1; j > -1; j--) {
						table.removeRow(j);
					}
					return;
				}
			}else if(table.getRows().size()==3) {
				String text=table.getRows().get(2).getCell(0).getText();
				if(text.contains("«Viruses_D")||text.contains("«QBacteria")||text.contains("«Mycobacteria")||text.contains("«MCR")||text.contains("«Bacteria")||text.contains("«Fungi")||text.contains("«Parasite")||text.contains("«Background")) {
					for (int j = table.getRows().size()-1; j > -1; j--) {
						table.removeRow(j);
					}
					return;
				}
			}else if(table.getRows().size()==5) {
				String text=table.getRows().get(2).getCell(0).getText();
				if(text.contains("«Viruses_D")||text.contains("«QBacteria")||text.contains("«Mycobacteria")||text.contains("«MCR")||text.contains("«Bacteria")||text.contains("«Fungi")||text.contains("«Parasite")||text.contains("«Background")) {
					for (int j = table.getRows().size()-1; j > -1; j--) {
						table.removeRow(j);
					}
					return;
				}
			}
		}else if("D".equals(jclx)) {
			if(table.getRows().size()>=3) {
				String text=table.getRows().get(2).getCell(0).getText();
				if(text.contains("«Viruses_R")) {
					XWPFTableRow row=table.createRow();
					row.setHeight(table.getRow(0).getHeight());
					row.addNewTableCell();
					row.addNewTableCell();
					row.addNewTableCell();
					row.addNewTableCell();
					for (int i = 0; i < row.getTableCells().size(); i++) {
						XWPFTableCell cell=row.getTableCells().get(i);
						if(i ==0) {
							cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART); 
						}else {
							cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);  
						}
					}
					XWPFTableCell cell= row.getCell(0);
					CTTc cttc = cell.getCTTc();
					CTTcPr ctPr = cttc.addNewTcPr();
					ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
					cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
					XWPFParagraph paragraph=cell.getParagraphs().get(0);
					XWPFRun run=paragraph.createRun();
					//run.setFontFamily("思源黑体");
					if (StringUtil.isNotBlank(table.getRows().get(2).getCell(0).getParagraphs().get(0).getRuns().get(0).getFontFamily())){
						run.setFontFamily(table.getRows().get(2).getCell(0).getParagraphs().get(0).getRuns().get(0).getFontFamily());
					}
					if (table.getRows().get(2).getCell(0).getParagraphs().get(0).getRuns().get(0).getFontSize()>0){
						run.setFontSize(table.getRows().get(2).getCell(0).getParagraphs().get(0).getRuns().get(0).getFontSize());
					}else {
						run.setFontSize(12);
					}
					run.setText("未进行RNA测序流程");
				}
			}
		}
		if(table.getRows()==null||table.getRows().size()==0) {
			return;
		}
		XWPFTableRow tmpRow = table.getRows().get(table.getRows().size() - 1);
		// 获取到最后行的第一列 ；
		XWPFTableCell lastcell = tmpRow.getCell(0);
		List<XWPFParagraph> lastcellParList = lastcell.getParagraphs();
		StringBuffer s_cellText = new StringBuffer();
		for (int p = 0; lastcellParList != null && p < lastcellParList.size(); p++) {
			String t_cellText = lastcellParList.get(p).getText();
			if (StringUtil.isNotBlank(t_cellText)) {
                s_cellText.append(t_cellText);
            }
		}
		String cellText = s_cellText.toString();
		int lastStartTagPos = cellText.indexOf("«");
		boolean sfthhg=true;//判断是否替换横杠
		// 判断如果为循环表格
		if (cellText != null && lastStartTagPos > -1 && cellText.indexOf("List.") > -1) {
			sfthhg=false;
			// 获取循环表格的变量
			int lastEndTagPos = cellText.indexOf("»");
			// 寻找到需要替换的变量，如果没有找到则到下一个run
			if (lastStartTagPos > -1 && lastEndTagPos > 0 && lastEndTagPos > lastStartTagPos) {
				String varString = cellText.substring(lastStartTagPos + 1, lastEndTagPos);
				//判断新模板标记,如果为true,生成新报告方法
				if("IMP_REPORT_QINDEX_TEMEPLATE".equals(tmpate_flag.replaceAll("_REM","")) || "IMP_REPORT_ONCO_QINDEX_TEMEPLATE".equals(tmpate_flag.replaceAll("_REM","")) || (StringUtil.isNotBlank(tmpate_flag) && tmpate_flag.contains("IMP_REPORT_SEQ_")) || "IMP_SPEED".equals(tmpate_flag.replaceAll("_REM","")) || "IMP_REPORT_TNGS_TEMEPLATE".equals(tmpate_flag)) {
					String yblxdm=map.get("yblxdm").toString();
					if(isHave(yblxs, yblxdm)) {
						//需要增加独立因子等表格的处理
						replaceListTable_special(table, map, varString);
					}else {
						//新模板下的表格处理
						replaceListTable(table, map, varString);
					}
				}else {
					//如果为false，生成旧模板方法
					replaceSingleTable(table, map, varString);
				}
				//对taxFlg_List.special_flg 的表格进行特殊处理
				replaceSpecialSpeciesTable(table, map, varString);
			}
		} else if (cellText != null && lastStartTagPos > -1 && cellText.indexOf("«Iterate.") > -1 && cellText.indexOf("«/Iterate.") > -1){
			sfthhg=false;
			log.error("表格中循环体替换lastcellParList");
			getIterateIndexAndReplace(lastcell, map,true);
		}
		dealPtTable(table,map,sfthhg);
	}

	/**
	 * 处理表格里的信息，如果有变量则进行变量替换
	 * @param table
	 * @param map
	 * @param sfthhg
	 */
	public void dealPtTable(XWPFTable table, Map<String, Object> map,boolean sfthhg){
		// 普通表格，则进行替换操作
		// 获取到table 的行数 rows（多）
		int rcount = table.getNumberOfRows();
		for (int i = 0; i < rcount; i++) {
			// 拿到当前行的内容
			XWPFTableRow row = table.getRow(i);
			// 获取当前行的列数
			List<XWPFTableCell> cells = row.getTableCells();
			// 循环列（cells 多）
			for (XWPFTableCell cell : cells) {
				//如果cell体里是«Iterate这种循环体，则直接调用通用方法执行循环体的处理
				String celltext = cell.getText();
				if(celltext.indexOf("«Iterate.") > -1){
					getIterateIndexAndReplace(cell, map,true);
					String t_celltext = cell.getText();
					if(StringUtil.isBlank(t_celltext.trim()))
						cell.setText("未检出");
				}
				// 拿到当前列的 段落（多） 注意：poi操作word有bug，段落获取不完整。所以模板中需要直接粘贴，不能手动输入。
				List<XWPFParagraph> cellParList = cell.getParagraphs();
				// 循环每个段落
				for (int p = 0; cellParList != null && p < cellParList.size(); p++) {
					// 拿到段落中的runs；
					List<XWPFRun> runs = cellParList.get(p).getRuns();
					// 循环runs
					for (int q = 0; runs != null && q < runs.size(); q++) {
						// 获取到run；run为word文档中最小的变量
						String oneparaString = runs.get(q).getText(runs.get(q).getTextPosition());
						boolean bold = runs.get(q).isBold();
						// 过滤掉空的run
						if (StringUtil.isBlank(oneparaString)) {
							continue;
						}
						// 循环替换的数据
						// 为对应一个Run里多个变量，需要进行循环
						if(oneparaString.contains("_break")) {
							cellParList.get(p).setIndentationFirstLine(200);
						}
						while (true) {
							int startTagPos = oneparaString.indexOf("«");
							int endTagPos = oneparaString.indexOf("»");
							// 寻找到需要替换的变量，如果没有找到则到下一个run
							if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
								//由于trim会把空格去掉，后面replace会进入死循环，故将trim去掉
//									String varString = oneparaString.substring(startTagPos + 1, endTagPos).trim();
								String varString = oneparaString.substring(startTagPos + 1, endTagPos);
								if(varString.indexOf("List.") > -1 && varString.indexOf("taxFlg_List.") ==-1){
									return;
								}
								String startPos="";
								String endPos=null;
								if(varString.indexOf("?")!=-1){
									endPos="";
								}
								if(varString.indexOf(":")!=-1) {
									String[] strs=varString.split(":");
									if(strs.length==2) {
										startPos=strs[0];
										endPos=strs[1];
									}else if(strs.length==1){
										startPos=strs[0];
										endPos="";
									}else {
										break;
									}
								}else {
									startPos=varString;
								}
								if(startPos!=null && startPos!="") {
									if("pathogen_comment_old".equals(startPos)) {
										String refs=map.get("refs_default")==null?"":map.get("refs_default").toString();
										map.put("refs",refs);
									}else if("pathogen_comment_new".equals(startPos) || "pathogen_comment_widthEnglish".equals(startPos)) {
										String refs=map.get("refs_definition")==null?"":map.get("refs_definition").toString();
										map.put("refs",refs);
									}else if ("backgroundInfo_comment".equals(startPos) || "commensal_comment".equals(startPos)){
										List<WechatReferencesModel> refs= (List<WechatReferencesModel>)map.get("refs_List");
										List<SjwzxxDto> sjwzxxlist=new ArrayList<>();
										sjwzxxlist.addAll((List<SjwzxxDto>)map.get("Pathogen_List"));//这里add顺序和模板的排版相对应
										sjwzxxlist.addAll((List<SjwzxxDto>)map.get("BackgroundInfo_List"));
										sjwzxxlist.addAll((List<SjwzxxDto>)map.get("Commensal_List"));
										map.put("refs_other",sortRefs(refs,sjwzxxlist));
									}else if (startPos.indexOf("taxFlg_List.")!=-1) {
										startPos = varString;
										String replceStr = varString;
										while (replceStr.contains("taxFlg_List.")){
											String subTaxFlgKeys = "";
											int flagIndexA = replceStr.indexOf("taxFlg_List.");
											int flagIndexB = replceStr.indexOf("{taxFlg_List.");
											if (flagIndexA >= flagIndexB+1 && flagIndexB != -1){
												//第一个为{taxFlg_List.***.xxx}
												replceStr = replceStr.substring(replceStr.indexOf("{taxFlg_List."));
												subTaxFlgKeys = replceStr.substring(replceStr.indexOf("{taxFlg_List.")+1,replceStr.indexOf("}"));
												replceStr = replceStr.substring(replceStr.indexOf("}")+1);
											}else {
												//第一个为taxFlg_List.***.xxx
												subTaxFlgKeys = replceStr;
												replceStr = replceStr.replace(replceStr,"");
											}
											String[] split = subTaxFlgKeys.split("\\.");
											String taxFlgKey = split[1];
											String taxFlgObjectKey = split[2];
											Map<String,WeChatInspectionSpeciesModel> refsMap = (Map<String, WeChatInspectionSpeciesModel>) map.get("taxFlg_List");
											Map<String,WeChatInspectionSpeciesModel> specialTaxFlg_list = (Map<String, WeChatInspectionSpeciesModel>) map.get("specialTaxFlg_List");
											if(taxFlgKey.indexOf(",")!=-1) {
												//若一个key中有多个taxid,则将多个taxid的取出，去map中匹配，如果匹配到任意一个，则存入该key
												String[] taxFlgKeys = taxFlgKey.split("_");
												String[] keysSplit = taxFlgKeys[0].split(",");
												boolean isReplace = false;
												for (String key : keysSplit) {
													if(refsMap.get(key+"_"+taxFlgKeys[1])!=null && !isReplace) {
														WeChatInspectionSpeciesModel refs = refsMap.get(key+"_"+taxFlgKeys[1]);
														if(refs!=null){
//															String tmpVar=taxFlgSplit[taxFlgSplit.length-1];
															String TotmpVar = taxFlgObjectKey.substring(0, 1).toUpperCase() + taxFlgObjectKey.substring(1);
															Method t_method = null;
															try {
																t_method = refs.getClass().getMethod("get" + TotmpVar);
																map.put("taxFlg_List."+taxFlgKey+"."+taxFlgObjectKey,(String) t_method.invoke(refs));
															} catch (Exception e) {
																e.printStackTrace();
															}
														}
														isReplace = true;
													}
													specialTaxFlg_list.remove(key);
												}
											}else {
												WeChatInspectionSpeciesModel refs = refsMap.get(taxFlgKey);
												if(refs!=null){
//													String tmpVar=taxFlgSplit[taxFlgSplit.length-1];
													String TotmpVar = taxFlgObjectKey.substring(0, 1).toUpperCase() + taxFlgObjectKey.substring(1);
													try {
														Method t_method = refs.getClass().getMethod("get" + TotmpVar);
														map.put("taxFlg_List."+taxFlgKey+"."+taxFlgObjectKey,(String) t_method.invoke(refs));
														specialTaxFlg_list.remove(taxFlgKey);
													} catch (Exception e) {
														e.printStackTrace();
													}
												}
											}
										}
									}
								}
								if("host_percentile".equals(startPos)&&isHave(yblxs, map.get("yblxdm").toString())) {
									List<PictureFloat> list= new ArrayList<>();
									DBEncrypt Encrypt=new DBEncrypt();
									if(map.get("wjlj") != null) {
										PictureFloat imageFloat1=new PictureFloat(Encrypt.dCode(map.get("wjlj").toString()), 250, 50, 0, 0);
										list.add(imageFloat1);
										if(map.get("ryzswz")!=null) {
											int left=(int) (217 * Float.parseFloat(map.get("ryzswz").toString())+10);
											PictureFloat imageFloat2=new PictureFloat(map.get("cursor").toString(), 15, 15, left, 33);
											list.add(imageFloat2);
										}
										overlayPictures(runs.get(q),list);
									}else {
										log.error("开启线程：报告8 没有人源信息---------");
									}
									break;
								}else if(startPos.contains("REM")&&map.get("sfqry")!=null){
									if("1".equals(map.get("sfqry").toString())) {
										String text=runs.get(q).getText(runs.get(q).getTextPosition()).replace("«REM", "").replace("»", "");
										runs.get(q).setText(text,0);
										runs.get(q).setBold(true);
										break;
									}else {
										cellParList.get(p).removeRun(q);
										break;
									}
								}else{
									//替换方法优化，进行判断处理  2022/9/8  姚嘉伟
									Object object_value=replaceJudge(startPos,map,cell);
//										Object object_value = map.get(startPos);
									if (object_value instanceof String) {
										String rep_value = (String) object_value;
										if (bold){
											rep_value = "{br}{b}"+rep_value.replaceAll("\\{br}\\{n}","\\{bn}").replaceAll("\\{br}","").replaceAll("\\{bn}","\\{br}\\{n}\\{b}")+"{br}";
										}
										//设置颜色，特殊处理
										if (startPos.indexOf("taxFlg_List.")!=-1) {
											if (functions.stream().anyMatch(startPos::contains)){
												try {
													String formatStr = startPos;
													Map<String, Object> parameters = new HashMap<>();
													int index = 0;
													while (formatStr.contains("{") && formatStr.contains("}")){
														String key = formatStr.substring(formatStr.indexOf("{") + 1, formatStr.indexOf("}"));
														String value = map.get(key) != null ? map.get(key).toString() : "";
														formatStr = formatStr.replace("{"+key+"}","parameter"+index);
														parameters.put("parameter"+index,value);
														index ++;
													}
													EvaluableExpression evaluable = new EvaluableExpression(formatStr,functionMap);
													Object evaluate = evaluable.evaluate(parameters);
													rep_value = evaluate.toString().replaceAll("「","{").replaceAll("」","}");
												} catch (Exception e) {
													log.error("替换公式错误:{}",startPos);
													rep_value = "";
												}
												endPos = rep_value;
											}else {
												if (rep_value.contains("{color:")) {
													int begin = rep_value.indexOf("{color:");
													String lstext = rep_value.substring(begin);
													int end = lstext.indexOf("}") + begin;
													String flg = rep_value.substring(begin, end + 1);
													String color = flg.substring(1, flg.length() - 1).split(":")[1];
													runs.get(q).setColor(color);
													rep_value = rep_value.replace(flg, "");
												}
												if(rep_value.contains("{b}")) {
													runs.get(q).setBold(true);
													rep_value = rep_value.replace("{b}", "");
												}
											}
										}

										// 如果找不到相应的替换问题，则替换成空字符串
										if (StringUtil.isBlank(rep_value)) {
											if(endPos!=null) {
												oneparaString = oneparaString.replace("«" + varString + "»", endPos);
											}else if(sfthhg){
												oneparaString = oneparaString.replace("«" + varString + "»", "一");
											}else{
												oneparaString = oneparaString.replace("«" + varString + "»", "");
											}
										} else {
											oneparaString = oneparaString.replace("«" + varString + "»", rep_value);
										}
										runs.get(q).setText(oneparaString, 0);

									} else {
										if(endPos!=null) {
											oneparaString = oneparaString.replace("«" + varString + "»", endPos);
										}else {
											//特殊判断，若替换的是taxFlg_List中的内容，则替换为“阴性“
											if(oneparaString.indexOf("«taxFlg_List")!=-1) {
												if (oneparaString.indexOf(".wzbgpz")!=-1 || oneparaString.indexOf(".wzbgpzall")!=-1) {
													oneparaString = oneparaString.replace("«" + varString + "»", "阴性");
												} else if (oneparaString.indexOf(".reportType")!=-1){
													oneparaString = oneparaString.replace("«" + varString + "»", map.get(startPos) != null ? map.get(startPos).toString() : "");
												}else {
													oneparaString = oneparaString.replace("«" + varString + "»", "未检出");
												}
											}else {
												//特殊判断一下，如果内容是  REM（去人源结果）替换为 ""  而不是 一
												if(startPos.contains("REM（去人源结果）")) {
													oneparaString = oneparaString.replace("«" + varString + "»", "");
												}else if(sfthhg){
													oneparaString = oneparaString.replace("«" + varString + "»", "一");
												}else{
													oneparaString = oneparaString.replace("«" + varString + "»", "");
												}
											}
										}
//											if("".equals(endPos)) {
//												//特殊判断一下，如果内容是  REM（去人源结果）替换为 ""  而不是 一
//												if("REM（去人源结果）".equals(startPos)) {
//													oneparaString = oneparaString.replace("«" + varString + "»", "");
//												}else {
//													oneparaString = oneparaString.replace("«" + varString + "»", "一");
//												}
//
//											}else {
//												oneparaString = oneparaString.replace("«" + varString + "»", endPos);
//											}
										runs.get(q).setText(oneparaString, 0);

									}
								}
							} else {
								break;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 特殊情况对参考文献进行重新排序
	 * @param refs
	 * @param sjwzxxlist
	 */
	public String sortRefs(List<WechatReferencesModel> refs,List<SjwzxxDto> sjwzxxlist) {
		// 更新参考文献信息(根据wzid和标本类型删除原有文献，新增新文献)
		StringBuffer ces = new StringBuffer("");
		List<WechatReferencesModel> refsList = new ArrayList<>();//文献List
		if (refs != null && refs.size() > 0) {
			for(int x = 0;x < sjwzxxlist.size();x++)
			{
				if(x>0)
					ces.append("{br}{\\n}");
				else
					ces.append("\n");
				for (int i = 0; i < refs.size(); i++) {
					if(refs.get(i).getId().equals(sjwzxxlist.get(x).getWxid())) {
						refs.get(i).setYyxh(String.valueOf(x + 1));
						refs.get(i).setCxpx(String.valueOf(x + 1));
						refsList.add(refs.get(i));
						ces.append("["+refs.get(i).getYyxh()+"]"+ refs.get(i).getAuthor()+ refs.get(i).getTitle()+ refs.get(i).getJournal());
						break;
					}
				}
			}
		}
		return ces.toString();
	}

	/**
	 * 判断替换
	 * @param startPos
	 * @param map
	 * @return
	 */
	public Object replaceJudge(String startPos,Map<String,Object> map,XWPFTableCell cell){
		Object object=new Object();
		boolean check_result=false;
		int judgePos = startPos.indexOf("?");//
		if(judgePos>0){//第一位为?不做处理
			String check_String =startPos.substring(0,judgePos).replaceAll(" ","");//对？前的公式进行空格替换处理
			String value="";
			String endString = startPos.substring(judgePos+1);
			int end_sPos = endString.lastIndexOf(";");
			while(true){
				String secString = check_String;//过滤空格
				int check_index_yu= secString.indexOf("&&");
				int check_index_huo= secString.indexOf("||");
				if(check_index_yu>-1) {
					secString = secString.substring(0, check_index_yu);//获取判断内容
				}
				if(check_index_huo>-1) {
					secString = secString.substring(0, check_index_huo);//获取判断内容
				}
				//判断是否自带判断条件，目前只提供 == ，！=
				int check_index_eq= secString.indexOf("==");
				int check_index_neq= secString.indexOf("!=");
				Object t_object=new Object();
				t_object = map.get(secString);
				if(check_index_eq>-1){
					t_object = map.get(secString.substring(0,check_index_eq));
				}else if(check_index_neq>-1){
					t_object = map.get(secString.substring(0,check_index_neq));
				}else{
					t_object = map.get(secString);
				}
				if("1".equals(map.get("wz_flag"))){
					//特殊处理,物种信息部分替换直接传入Dto
					try {
						t_object=map.get("wzxxDto");
						String tmpVar = secString.trim()
								.split("\\.")[1];
						if(check_index_eq>-1) {
							tmpVar = tmpVar.substring(0, tmpVar.indexOf("=="));
						}else{
							if(tmpVar.indexOf("!=") > -1)
								tmpVar = tmpVar.substring(0, tmpVar.indexOf("!="));
						}
						String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);
						Method t_method = t_object.getClass().getMethod("get" + TotmpVar);
						t_object=(String) t_method.invoke(t_object);
					} catch (Exception e) {
						log.error(e.toString());
					}
				}

				if(end_sPos>-1){//«bacterias_rpm?0;测试{bacterias_rpm}»
					if((t_object!=null && !"".equals(t_object) && !ArrayList.class.equals(t_object.getClass())) || (t_object!=null && !"".equals(t_object) && ArrayList.class.equals(t_object.getClass()) && ((ArrayList<?>) t_object).size()>0)) {//不为空,取;前面
						if(check_index_eq>-1){//判断是否自带判断条件,==
                            check_result= t_object.equals(secString.substring(check_index_eq + 2));
						}else if (check_index_neq>-1){//!=
                            check_result= !t_object.equals(secString.substring(check_index_neq + 2));
						}else{//其他
							check_result=true;
						}
					}else{//否则取;后面
						check_result=false;
					}
				}else{//如果没有；例如«bacterias_rpm?测试{bacterias_rpm}»
                    //为空,取?后面
                    //否则取空值
                    check_result= (t_object != null && !ArrayList.class.equals(t_object.getClass())) || (t_object != null && ArrayList.class.equals(t_object.getClass()) && ((ArrayList<?>) t_object).size() > 0);
				}
				if(check_index_huo>-1){
					check_String=check_String.substring(check_index_huo+2);
				}else if (check_index_yu>-1){
					check_String=check_String.substring(check_index_yu+2);
				}
				if(check_index_huo==-1 && check_index_yu==-1){//没有&&或者||
					break;
				}else if(check_index_huo>-1 && check_result) {//如果是||的判断，则只要有false直接break，取前面
					break;
				}else if(check_index_yu>-1 && !check_result){//如果是&&的判断，则只要有false直接break，取后面
					break;
				}
			}
			if(end_sPos>-1) {
				if (check_result) {
					value = endString.substring(0, end_sPos);//取;前面
				} else {
					value = endString.substring(end_sPos + 1);//取;后面
				}
			}else{
				if (check_result) {
					value = endString;
				} else {
					return "";
				}
			}
			//对{}进行替换和拼接,对[]循环数据进行处理
			object=replaceForOrNomal(null,value,map,cell);
		}else{//没有?或者?在开头，不做处理,原值返回
			object=map.get(startPos);
			if (!startPos.contains(".") && !startPos.contains(":") && object == null){
				object = "";
			}
		}
		return object;
	}

	private boolean getConditionConform(List<String> conditions,Map<String,Object> map,List<String> list_condition){
		return true;
	}

	/**
	 * 判断条件是否符合，支持多条件情况
	 * @param condition
	 * @return 符合返回true，不符合返回false
	 */
	private boolean isConditionConform(String condition,Map<String,Object> map,List<String> list_condition){
		//判断是否自带判断条件，目前只提供 == ，！=
		int check_index_eq= condition.indexOf("==");
		int check_index_neq= condition.indexOf("!=");
		String str_info = "";
		String value = "";
		if(check_index_eq>-1){
			str_info = condition.substring(0,check_index_eq).trim();
			value = condition.substring(check_index_eq+2).trim();
		}else if(check_index_neq>-1){
			str_info = condition.substring(0,check_index_neq).trim();
			value = condition.substring(check_index_eq+2).trim();
		}else{
			str_info = condition.trim();
		}
		Object t_object=new Object();
		//如果条件里有.则根据前面的参数获取变量，否则根据参数名获取变量
		if(str_info.contains(".")){
			String[] str_info_array = str_info.split("\\.");
			t_object = map.get(str_info_array[0]);
			if(t_object instanceof List<?>) {
				list_condition.add(condition);
				return true;
			}
			for(int i = 1; i < str_info_array.length; i++){
				String subString = str_info_array[i];
				if (str_info_array[i-1].contains("map") || str_info_array[i-1].contains("json")) {
					Map<String, Object> t_map = (Map<String, Object>) t_object;
					t_object = t_map.get(subString);
				}else {
					String TotmpVar = str_info_array[i].substring(0, 1).toUpperCase() + str_info_array[i].substring(1);
					try {
						Method t_method = t_object.getClass().getMethod("get" + TotmpVar);
						t_object = (String) t_method.invoke(t_object);
					} catch (Exception e) {
						log.error(e.toString());
					}
				}
			}
		}else{
			t_object = map.get(str_info);
		}

		if(t_object==null) {
			if ("1".equals(map.get("wz_flag"))) {
				//特殊处理,物种信息部分替换直接传入Dto
				try {
					t_object = map.get("wzxxDto");
					String tmpVar = condition.trim()
							.split("\\.")[1];
					if (check_index_eq > -1) {
						tmpVar = tmpVar.substring(0, tmpVar.indexOf("=="));
					} else {
						if (tmpVar.indexOf("!=") > -1)
							tmpVar = tmpVar.substring(0, tmpVar.indexOf("!="));
					}
					String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);
					Method t_method = t_object.getClass().getMethod("get" + TotmpVar);
					t_object = (String) t_method.invoke(t_object);
				} catch (Exception e) {
					log.error(e.toString());
				}
			}
		}
        return t_object != null && value.equals(t_object.toString());
	}

	/**
	 * 根据条件判断，返回相应的true或者false结果
	 * @param list_condition 复数的条件
	 * @param o_dto 信息对象
	 * @param typeFlg 判断类型 1 代表and 0 代表 or
	 * @param trueValue 条件为真时的返回内容
	 * @param falseValue 条件为假时的返回内容
	 * @return
	 */
	private String getConditionResult(List<String> list_condition,Object o_dto,int typeFlg,String trueValue,String falseValue){
		boolean check_result=false;
		String resultValue = null;
		//根据和条件判断 判断所有条件，此处只能判断DTO的属性值或者变量值，但如果遇到List的，因为需要循环判断，则此处直接判断为true,然后把条件加的 list里，设置value的时候使用
		for(int x=0;x<list_condition.size();x++){
			String[] equal_split = list_condition.get(x).split("==");
			String[] noequal_split = list_condition.get(x).split("!=");
			if (equal_split.length == 2) {
				try {
					//暂时不考虑循环体外的条件，当前都为list.开始的变量,因为暂时没有传递相应的map集合
					if(equal_split[0].indexOf(".") > -1){
						String tt_condition = equal_split[0].split("\\.")[1];
						Method method = o_dto.getClass().getMethod("get" + tt_condition.substring(0, 1).toUpperCase() + tt_condition.substring(1));
						resultValue = (String) method.invoke(o_dto);
					}else{
						//非对象里的变量，而是直接变量的情况，因为传入的是object，所以暂时不考虑
					}
				} catch (Exception e) {
					log.error("equal_split 对象中没有get" + equal_split[0].substring(0, 1).toUpperCase() + equal_split[0].substring(1) + "方法");
				}
				//但凡有一个不符合，则返回false值
				if (!equal_split[1].equals(resultValue)) {
					check_result = false;
				}else
					check_result = true;
			}else if (noequal_split.length == 2) {
				try {
					//暂时不考虑循环体外的条件，当前都为list.开始的变量,因为暂时没有传递相应的map集合
					if(noequal_split[0].indexOf(".") > -1){
						String tt_condition = noequal_split[0].split("\\.")[1];
						Method method = o_dto.getClass().getMethod("get" + tt_condition.substring(0, 1).toUpperCase() + tt_condition.substring(1));
						resultValue = (String) method.invoke(o_dto);
					}else{
						//非list.开始的变量，暂不处理
					}
				} catch (Exception e) {
					log.error("noequal_split 对象中没有get" + noequal_split[0].substring(0, 1).toUpperCase() + noequal_split[0].substring(1) + "方法");
				}
				//但凡有一个不符合，则返回false值
				if (noequal_split[1].equals(resultValue)) {
					check_result = false;
				}else
					check_result = true;
			}else{
				try {
					//存在 list.wzid的
					if(noequal_split[0].indexOf(".") > -1){
						String tt_condition = noequal_split[0].split("\\.")[1];
						Method method = o_dto.getClass().getMethod("get" + tt_condition.substring(0, 1).toUpperCase() + tt_condition.substring(1));
						resultValue = (String) method.invoke(o_dto);
					}else{
						//非list.开始的变量
						Method method = o_dto.getClass().getMethod("get" + list_condition.get(x).substring(0, 1).toUpperCase() + list_condition.get(x).substring(1));
						resultValue = (String) method.invoke(o_dto);
					}
				} catch (Exception e) {
					log.error("普通对象中没有get" + list_condition.get(x).substring(0, 1).toUpperCase() + list_condition.get(x).substring(1) + "方法");
				}
				if (StringUtil.isNotBlank(resultValue)) {
					check_result = true;
				}else{
					check_result = false;
				}
			}
			// 如果为and（1）条件，则有false直接返回，如果为or（2）条件，则只要有一个true，则返回true
			if(typeFlg==1){
				if(!check_result){
					return falseValue;
				}
			}else if(typeFlg==2){
				if(check_result){
					return trueValue;
				}
			}
		}
		//这为了不是一步到位直接替换实际值，而是根据条件只留下相应的 true的变量值（还带有《等），或者false值，然后在后面的处理里再进行实际值的替换
		//全部检查完成，没有提前返回，代表都不符合前面的特殊条件
		if(typeFlg==1){
			return trueValue;
		}else if(typeFlg==2){
			return falseValue;
		}
		return trueValue;
	}

	/**
	 * 处理需要循环[]的组装，如果没有[]，直接调用replaceSec
	 * @param value
	 * @param map
	 * @return
	 */
	public String replaceForOrNomal(Map<String,Object> listCondition,String value,Map<String,Object> map,XWPFTableCell cell){
		String final_value="";
		int forindex_start = value.indexOf("[");//获取循环开始的节点
		int forindex_end = value.indexOf("]");//获取循环结束的节点
		int value_length = value.length();//获取整个value长度
		if(forindex_start>-1 && forindex_end>-1){//若包含循环，则做循环处理
			String forout_start = value.substring(0,forindex_start);//获取循环外层开头内容
			String forout_end = value.substring(forindex_end+1,value_length);//获取循环外层结束内容
			//获取循环内容，通过第一个[,@截取k。 @前面代表变量名，[Viruses_D_List@{list.wzid}?{list.wzzwm};{list.szwm}@{br}{\n}]
			int flagindex_start = value.indexOf("@");
			int flagindex_end = value.indexOf("@",flagindex_start+1);//第二个@截取
			String list_name = value.substring(forindex_start+1,flagindex_start);//获取list名称
			String forstr_flag = value.substring(flagindex_end+1,forindex_end);//获取每次循环需要拼接的内容
			if(map.get(list_name)!=null){
				//判断是否为List类型
				List<Object> list = (ArrayList)map.get(list_name);
				if(list!=null && list.size()>0){
					for(Object object : list){
						final_value=final_value+forstr_flag+replaceSec(value.substring(flagindex_start+1,flagindex_end),map,list_name,object,cell);
					}
					return forout_start+final_value.substring(forstr_flag.length())+forout_end;
				}else{
					return "";
				}
			}else{
				return "";
			}
		}else{//如果没有循环直接调用replaceSec
			return replaceSec(value,map,null,null,cell);
		}
	}

	/**
	 * 对内容进行替换，但里面当前考虑了List的情况，对问号进行了判断
	 * @param value
	 * @param map
	 * @param listname
	 * @param objectDto
	 * @param cell
	 * @return
	 */
	public String replaceSec(String value,Map<String,Object> map,String listname,Object objectDto,XWPFTableCell cell) {
		if(value.indexOf("?")>-1){//若进行list内部判断
			boolean check_result=false;
			int judgePos = value.indexOf("?");
			if(judgePos>0){
				String check_String =value.substring(0,judgePos).replaceAll(" ","");//对？前的公式进行空格替换处理
				String endString = value.substring(judgePos+1);
				int end_sPos = endString.lastIndexOf(";");
				while(true){
					String secString = check_String;//过滤空格
					int check_index_yu= secString.indexOf("&&");
					int check_index_huo= secString.indexOf("||");
					if(check_index_yu>-1) {
						secString = secString.substring(0, check_index_yu);//获取判断内容
					}
					if(check_index_huo>-1) {
						secString = secString.substring(0, check_index_huo);//获取判断内容
					}
					//判断是否自带判断条件，目前只提供 == ，！=
					int check_index_eq= secString.indexOf("==");
					int check_index_neq= secString.indexOf("!=");
					Object t_object=new Object();
					if(check_index_eq>-1){//由于是对list进行判断，所以截取完要带上"}"
						t_object = replaceSec(secString.substring(0,check_index_eq)+"}",map,listname,objectDto,cell);
					}else if(check_index_neq>-1){
						t_object = replaceSec(secString.substring(0,check_index_neq)+"}",map,listname,objectDto,cell);
					}else{
						t_object = replaceSec(secString,map,listname,objectDto,cell);
					}

					if(end_sPos>-1){//
						if((t_object!=null && !"".equals(t_object) && !ArrayList.class.equals(t_object.getClass())) || (t_object!=null && !"".equals(t_object) && ArrayList.class.equals(t_object.getClass()) && ((ArrayList<?>) t_object).size()>0)) {//不为空,取;前面
							if(check_index_eq>-1){//判断是否自带判断条件,==
								check_result= t_object.equals(secString.substring(check_index_eq + 2,secString.length()-1));//这里由于secString自带list类型的{},所以需要截取掉末尾
							}else if (check_index_neq>-1){//!=
								check_result= !t_object.equals(secString.substring(check_index_neq + 2,secString.length()-1));
							}else{//其他
								check_result=true;
							}
						}else{//否则取;后面
							check_result=false;
						}
					}else{//如果没有
						//为空,取?后面
						//否则取空值
						check_result= (t_object != null && !ArrayList.class.equals(t_object.getClass())) || (t_object != null && ArrayList.class.equals(t_object.getClass()) && ((ArrayList<?>) t_object).size() > 0);
					}
					if(check_index_huo>-1){
						check_String=check_String.substring(check_index_huo+2);
					}else if (check_index_yu>-1){
						check_String=check_String.substring(check_index_yu+2);
					}
					if(check_index_huo==-1 && check_index_yu==-1){//没有&&或者||
						break;
					}else if(check_index_huo>-1 && check_result) {//如果是||的判断，则只要有false直接break，取前面
						break;
					}else if(check_index_yu>-1 && !check_result){//如果是&&的判断，则只要有false直接break，取后面
						break;
					}
				}
				if(end_sPos>-1) {
					if (check_result) {
						value = endString.substring(0, end_sPos);//取;前面
					} else {
						value = endString.substring(end_sPos + 1);//取;后面
					}
				}else{
					if (check_result) {
						value = endString;
					} else {
						return "";
					}
				}
			}
		}
		//进行最终替换
		String final_value="";
		boolean result=false;
		if(value.indexOf("{")>-1) {
			result = true;
		}else{
			final_value=value;
		}
		try {
			//对{}进行替换和拼接
			while (result) {
				int indexstart = value.indexOf("{");
				int indexend = value.indexOf("}");
				String endStr="";
				if (indexstart == -1 && indexend == -1 && StringUtil.isNotBlank(value)) {
					final_value = final_value + value;
					result = false;
				}
				if (indexend == -1) {
                    break;
                }
				if("1".equals(map.get("wz_flag"))){
					//特殊处理,物种信息部分替换直接传入Dto
					SjwzxxDto t_object=(SjwzxxDto) map.get("wzxxDto");
					String tmpVar = value.substring(indexstart + 1, indexend).trim()
							.split("\\.")[1];
					String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);
					Method t_method = t_object.getClass().getMethod("get" + TotmpVar);
					if("Percentile".equals(TotmpVar) && cell!=null){//如果为index的图片特殊处理
						if(StringUtil.isNotBlank(t_object.getJzryzswz())) {
							File file = new File(t_object.getPercentile_path());
							if (!file.exists()) {
								break;
							}
							for (int k = cell.getParagraphs().size()-1; k >-1; k--) {
								cell.removeParagraph(k);
							}
							XWPFParagraph p=cell.addParagraph();
							XWPFRun r=p.createRun();
							List<PictureFloat> list= new ArrayList<>();
							PictureFloat imageFloat1=new PictureFloat(t_object.getPercentile_path(), 250, 50, 0, 0);
							list.add(imageFloat1);
							int left=(int) (217 * Float.parseFloat(t_object.getJzryzswz())+10);
							PictureFloat imageFloat2=new PictureFloat(t_object.getCursor_path(), 15, 15, left, 33);
							list.add(imageFloat2);
							overlayPictures(r,list);
						}
						return "";//直接结束替换方法
					}
					endStr=(String) t_method.invoke(t_object);
				}else{
					endStr=(String)map.get(value.substring(indexstart + 1, indexend));
				}
				if (value.substring(indexstart + 1, indexend).indexOf("list.") > -1) {//取list里的属性
					String name = value.substring(indexstart + 1, indexend).replace("list.", "");
					String TotmpVar = name.substring(0, 1).toUpperCase() + name.substring(1);
					// 反射方法 把map的value 插入到数据中
					Method t_method = objectDto.getClass().getMethod("get" + TotmpVar);
					endStr=(String)t_method.invoke(objectDto);
				}
                if(endStr==null)
                    endStr="";//处理当endStr返回null时，会导致list.的情况下逻辑错误
                if (indexstart > 0) {
					final_value = final_value + value.substring(0, indexstart) + endStr;
				} else {
					final_value = final_value + endStr;
				}
				value = value.substring(indexend + 1);
			}
		}catch(Exception e){
			// TODO Auto-generated catch block
			log.error(e.toString());
		}
		return final_value;
	}

	/**
	 * 循环多个表格（针对新模板三行）
	 * @param table
	 * @param map
	 * @param varString
	 */
	private void replaceListTable(XWPFTable table, Map<String, Object> map, String varString) {
		String listName = varString.split("\\.")[0];
		if(listName.contains("italic_")) {
			listName=listName.replace("italic_", "");
		}
		Object o_list = map.get(listName);

		boolean isListFlg = o_list instanceof ArrayList<?>;
		if (o_list == null || !isListFlg) {
            return;
        }

        List<SjwzxxDto> sjwzxxDtoList = new ArrayList<SjwzxxDto>();
        List<SjnyxDto> sjnyxDtoList = new ArrayList<SjnyxDto>();
        for (Object item : (List<?>) o_list) {
            if (item instanceof SjwzxxDto) {
                sjwzxxDtoList = (List<SjwzxxDto>) o_list;
                break;
            }else if((item instanceof SjnyxDto)){
                sjnyxDtoList = (List<SjnyxDto>) o_list;
                break;
            }
        }

		// 如果没有数据，则清除掉空的row，显示为 --
		if (sjwzxxDtoList.size() == 0 && sjnyxDtoList.size() ==0) {
			return;
		}
		
		if("Viruses_List".equals(listName)|| "Bacteria_List".equals(listName)|| "Mycobacteria_List".equals(listName)|| "MCR_List".equals(listName)|| "Fungi_List".equals(listName)
				|| "Viruses_D_List".equals(listName)|| "Viruses_R_List".equals(listName)|| "Parasite_List".equals(listName)|| "sjwzxx_List".equals(listName) || "TBT_SjnyxList".equals(listName) ) {
			addNewRows_other(table ,sjwzxxDtoList,map);
		}else if("Background_List".equals(listName)|| "Gene_List".equals(listName)|| "Pathogen_List".equals(listName) || "Sjwzxx_List".equals(listName)  || "Sjnyx_List".equals(listName) || "Commensal_List".equals(listName)
				|| "BackgroundInfo_List".equals(listName) || "TBTSjnyx_List".equals(listName)) {
			// 获取表格的列数，根据最后一列
			int rowCnt = table.getRows().size();
			addNewRows(table ,sjwzxxDtoList,map);
 			// 删除模版行
 			table.removeRow(rowCnt - 1);
		}else if("nyx_List".equals(listName)) {
			// 获取表格的列数，根据最后一列
			int rowCnt = table.getRows().size();
			addNewObjectRows(table ,sjnyxDtoList,map);
			// 删除模版行
			table.removeRow(rowCnt - 1);
		}else if ("VirulenceFactorStat_List".equals(listName)){
			List<WechatVirulenceFactorStatModel> vfsList = (List<WechatVirulenceFactorStatModel>) o_list;
			if (vfsList.size() == 0) {
				return;
			}
			// 获取表格的列数，根据最后一列
			int rowCnt = table.getRows().size();
			addNewRowsVrf(table ,vfsList);
			// 删除模版行
			table.removeRow(rowCnt - 1);
		}else if (listName.contains("_MapList")){
			//毒力因子替换
			List<Map<String, Object>> mapList = (List<Map<String, Object>>) o_list;
			if (mapList.size() == 0) {
				return;
			}
			// 获取表格的列数，根据最后一列
			int rowCnt = table.getRows().size();
			addNewRowsMapList(table ,mapList);
			// 删除模版行
			table.removeRow(rowCnt - 1);
		}else if(listName.contains("_List")){
			// 获取表格的列数，根据最后一列
			// 获取表格的列数，根据最后一列
			int rowCnt = table.getRows().size();
			addNewRows(table ,sjwzxxDtoList,map);
			// 删除模版行
			table.removeRow(rowCnt - 1);
		}
	}
	
	/**
	 * 循环单个表格（针对旧模板 一行）
	 * @param table
	 * @param map
	 * @param varString
	 */
	private void replaceSingleTable(XWPFTable table, Map<String, Object> map, String varString) {
		String listName = varString.split("\\.")[0];
		if(listName.contains("italic_")) {
			listName=listName.replace("italic_", "");
		}
		Object o_list = map.get(listName);

		boolean isListFlg = o_list instanceof ArrayList<?>;
		if (o_list == null || !isListFlg) {
            return;
        }

        List<SjwzxxDto> sjwzxxDtoList = new ArrayList<SjwzxxDto>();
        List<SjnyxDto> sjnyxDtoList = new ArrayList<SjnyxDto>();
        for (Object item : (List<?>) o_list) {
            if (item instanceof SjwzxxDto) {
                sjwzxxDtoList = (List<SjwzxxDto>) o_list;
                break;
            }else if((item instanceof SjnyxDto)){
                sjnyxDtoList = (List<SjnyxDto>) o_list;
                break;
            }
        }

		// 如果没有数据，则清除掉空的row，显示为 --
		if (sjwzxxDtoList.size() == 0 && sjnyxDtoList.size() ==0) {
			return;
		}

		// 获取表格的列数，根据最后一列
		int rowCnt = table.getRows().size();
		XWPFTableRow tmpRow = table.getRows().get(table.getRows().size() - 1);
		List<XWPFTableCell> tmpCells = tmpRow.getTableCells();
		int cellsize = tmpCells.size();
		for (int x = 0; x < sjwzxxDtoList.size(); x++) {
			// 创建一个新的行 createRow
			XWPFTableRow row = table.createRow();
			// 设置新建行的高度（同模板的高度一样）
			row.setHeight(tmpRow.getHeight());
			// 注意：createRow，新增一行的列数，是按照表格第一行的列数来创建的。因为模板中第一行只有2列，不符合我们的需求，所以需要再额外创建 列
			// int rowsize 为需要再添加的列数
			int rowsize = cellsize - row.getTableCells().size();
			for (int i = 0; i < rowsize && rowsize > 0; i++) {
				row.addNewTableCell();
			}

			SjwzxxDto sjwzxxDto = sjwzxxDtoList.get(x);

			try {

				List<XWPFTableCell> cells = row.getTableCells();
				// 插入的行会填充与表格第一行相同的列数
				for (int i = 0; i < cellsize; i++) {
					// 由于 list的 size 和 List<XWPFTableCell> cells的 size 相等，所以可以直接用一个索引
					XWPFTableCell cell = cells.get(i);

					XWPFTableCell tmpCell = tmpCells.get(i);

					String tmpText = tmpCell.getText();
					if (StringUtil.isNotBlank(tmpText)) {
						int index = getXwpfParagraphIndex(tmpText);
						boolean isItalic = false;
						boolean isBreak=false;
						for (int j = 0; j < index; j++) {
							for (int l = 0; l < index; l++) {
								int star = tmpText.indexOf("«");
								int end = tmpText.indexOf("»");
								if (star > -1 && end > -1) {
									String content = tmpText.substring(star, end + 1).toString();
									// 判断 是否需要斜体
									if (content.contains("italic_")) {
										isItalic = true;
										content=content.replace("italic_", "");
									}
									if(content.contains("break_")) {
										isBreak = true;
										content=content.replace("break_", "");
									}
									int startTagPos = content.indexOf("«");
									int endTagPos = content.indexOf("»");
									// 寻找到需要替换的变量，如果没有找到则到下一个run
									if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {

										String tmpVar = content.substring(startTagPos + 1, endTagPos).trim()
												.split("\\.")[1];
										String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);
										// 反射方法 把map的value 插入到数据中
										Method t_method = sjwzxxDto.getClass().getMethod("get" + TotmpVar);

										String s_value = (String) t_method.invoke(sjwzxxDto);

										setCellText(tmpCells.get(i), cell, s_value, isItalic,isBreak,true);
									}
									tmpText = tmpText.replace(tmpText.substring(star, end + 1), "");
								}
							}
						}
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error(e.toString());
			}
		}
		// 删除模版行
		table.removeRow(rowCnt - 1);
	}
	
	/**
	 * 查看关键字“«” 出现的次数
	 * @param text
	 * @return
	 */
	public int getXwpfParagraphIndex(String text) {
		int count = 0;
		char param = '«';
		for (int i = 0; i < text.length(); i++) {
			char cm = text.charAt(i);
			if (cm == param) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 往新建行中插入数据
	 * 
	 * @param tmpCell  模板列，用于获取列的样式风格等等
	 * @param cell     需要插入数据的新建列
	 * @param text     需要插入的数据
	 * @param isItalic 是否需要斜体
	 * @throws Exception
	 */
	private void setCellText(XWPFTableCell tmpCell, XWPFTableCell cell, String text, boolean isItalic,boolean isBreak,boolean setBorder)
			throws Exception {
		// TODO Auto-generated method stub
		XWPFRun tmprun=null;
		for (int i = 0; i < tmpCell.getParagraphs().size(); i++) {
			if(tmprun!=null) {
                break;
            }
			XWPFParagraph paragraph=tmpCell.getParagraphs().get(i);
			for (int j = 0; j < paragraph.getRuns().size(); j++) {
				if(tmprun!=null) {
                    break;
                }
				XWPFRun run=paragraph.getRuns().get(j);
				if(StringUtil.isNotBlank(run.text())) {
					tmprun=run;
					break;
				}
			}
			
		}
		int fontSize=0;
		String fontFamily = "";
		if(tmprun!=null) {
			if(tmprun.getFontSize()>-1) {
				fontSize=tmprun.getFontSize();
			}
			if (StringUtil.isNotBlank(tmprun.getFontFamily())) {
				fontFamily = tmprun.getFontFamily();
			}
		}
		CTTc cttc2 = tmpCell.getCTTc();
		CTTcPr ctPr2 = cttc2.getTcPr();

		CTTc cttc = cell.getCTTc();
		CTTcPr ctPr = cttc.addNewTcPr();
		cell.setColor(tmpCell.getColor());
		cell.setVerticalAlignment(tmpCell.getVerticalAlignment());
		if (ctPr2.getTcW() != null) {
			ctPr.addNewTcW().setW(ctPr2.getTcW().getW());
		}
		if (ctPr2.getVAlign() != null) {
			ctPr.addNewVAlign().setVal(ctPr2.getVAlign().getVal());
		}
		if (cttc2.getPList().size() > 0) {
			CTP ctp = cttc2.getPList().get(0);
			if (ctp.getPPr() != null) {
				if (ctp.getPPr().getJc() != null) {
					cttc.getPList().get(0).addNewPPr().addNewJc().setVal(ctp.getPPr().getJc().getVal());
				}
			}
		}
		if(setBorder) {
			if (ctPr2.getTcBorders() != null) {
				ctPr.setTcBorders(ctPr2.getTcBorders());
			}
		}
		XWPFParagraph cellP = cell.getParagraphs().get(0);
		cellP.setSpacingBetween(tmpCell.getParagraphs().get(0).getSpacingBetween(), LineSpacingRule.EXACT);//这里面的18指的是磅值  //LineSpacingRule.EXACT指的是指定值
		cellP.setAlignment(tmpCell.getParagraphs().get(0).getAlignment());
		List<XWPFRun> runList = cellP.getRuns();
		XWPFRun cellR = cellP.createRun();
		cellR.getCTR().setRPr(runList.get(0).getCTR().getRPr());
		if(fontSize>0) {
			cellR.setFontSize(fontSize);
		}
		// 设置换行
		if(isBreak) {
			cellR.addBreak();
		}
		//cellR.setFontFamily("思源黑体");
		if (StringUtil.isNotBlank(fontFamily)) {
			cellR.setFontFamily(fontFamily);
		}
		// 设置斜体
		cellR.setItalic(isItalic);
		cellR.setText(text);
	}

	/**
	 * 清除掉空的row，显示为 --
	 * @param table
	 */
	private void clearNullCell(XWPFTable table,Map<String,Object> map) {
		int countRow = table.getNumberOfRows();
		for (int i = 0; i < countRow; i++) {
			XWPFTableRow row = table.getRow(i);
			List<XWPFTableCell> cells = row.getTableCells();
			for (XWPFTableCell cell : cells) {
				replaceCell(cell,map);
			}
		}
	}
	
	/**
	 * 替换空的段落内容为一
	 * @param paragraph
	 */
	private void replaceParagraph(XWPFParagraph paragraph) {
		List<XWPFRun> runs = paragraph.getRuns();
		int xh=0;
		for (int k = 0; k < runs.size() && runs != null; k++) {
			String text = runs.get(k).getText(runs.get(k).getTextPosition());
			if (StringUtil.isNotBlank(text)) {
				int startTagPos=text.indexOf("«");
				int endTagPos=text.indexOf("»");
				if(startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
					if(tmpate_flag.contains("IMP_REPORT_QINDEX_TEMEPLATE") || tmpate_flag.contains("IMP_REPORT_ONCO_QINDEX_TEMEPLATE") || tmpate_flag.contains("IMP_REPORT_SEQ_") || tmpate_flag.contains("IMP_SPEED") || tmpate_flag.contains("IMP_REPORT_TNGS_TEMEPLATE")) {
						if(text.contains(".percentile")) {
							runs.get(k).setBold(false);
							text = text.replace(text, "一");
						}else if(text.contains("rstmhdt")){
							text = text.replace(text, "");
						}else {
							text = text.replace(text, "");
						}
					}else {
						if(xh==0) {
							if(text.contains("rstmhdt")){
								text = text.replace(text, "");
							}else{
								text = text.replace(text, "一");
								xh++;
							}
						}else if(xh>0){
							text = text.replace(text, "");
						}
					}
				}
			}
			runs.get(k).setText(text, 0);
		}
	}
	/**
	 * 替换空的段落内容为一
	 * @param cell
	 */
	private void replaceCell(XWPFTableCell cell,Map<String,Object> map) {
		List<XWPFParagraph> cellParagraph = cell.getParagraphs();
		int xh = 0;
		for (int j = 0; j < cellParagraph.size() && cellParagraph != null; j++) {
			List<XWPFRun> runs = cellParagraph.get(j).getRuns();
			for (int k = 0; k < runs.size() && runs != null; k++) {
				String text = runs.get(k).getText(runs.get(k).getTextPosition());
				if (StringUtil.isNotBlank(text)) {
					int startTagPos=text.indexOf("«");
					int endTagPos=text.indexOf("»");
					if(startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
						if(tmpate_flag.contains("IMP_REPORT_QINDEX_TEMEPLATE") || tmpate_flag.contains("IMP_REPORT_ONCO_QINDEX_TEMEPLATE") || tmpate_flag.contains("IMP_REPORT_SEQ_") || tmpate_flag.contains("IMP_SPEED") || tmpate_flag.contains("IMP_REPORT_TNGS_TEMEPLATE")) {
							if(text.contains(".percentile")) {
								runs.get(k).setBold(false);
								text = text.replace(text, "一");
							}else if(text.contains("rstmhdt")){
								text = text.replace(text, "");
							}else if ((!text.contains("«nullasprint:") && text.contains("deleteTable")) || text.contains("forcedDeleteTable")){//补丁，用于«has_commensal==false?deleteTable;属（Genus）»公式替换
								text = text.replace(text.substring(startTagPos,endTagPos+1),String.valueOf(replaceJudge(text.substring(startTagPos+1,endTagPos),map,cell)));
							}else if (!text.contains("«nullasprint:")){
								text = text.replace(text, "");
							}
						}else if(tmpate_flag.contains("IMP_REPORT_RFS_TEMEPLATE")) {
							if(xh==0) {
								text = text.replace(text, "未检出");
								xh ++;
							}else if(xh>0){
								if (cellParagraph.size()-1 == j){
									cell.removeParagraph(j);
									break;
								}
								text = text.replace(text, "");
							}
						}else {
							if(xh==0) {
								if(text.contains("rstmhdt")){
									text = text.replace(text, "");
								}else{
									text = text.replace(text, "一");
									xh++;
								}
							}else if(xh>0){
								text = text.replace(text, "");
							}
						}
					}
				}
				runs.get(k).setText(text, 0);
			}
		}
	}

	/**
	 * 替换文本中的信息，不在table内的信息
	 * @param paragraph 当前段落
	 * @param map       替换数据
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	private void replaceText(XWPFParagraph paragraph, Map<String, Object> map,String jclx,int index,XWPFDocument document)
			throws InvalidFormatException, IOException {
		if("R".equals(jclx)) {
			if(paragraph.getText().contains("«Bacteria:")||paragraph.getText().contains("«Mycobacteria:")||paragraph.getText().contains("«MCR:")||paragraph.getText().contains("«Fungi:")||paragraph.getText().contains("«Viruses_D:")
					||paragraph.getText().contains("«Parasite:")||paragraph.getText().contains("«Background:")||paragraph.getText().contains("«Gene:")||paragraph.getText().contains("«Results:")||paragraph.getText().contains("«VirulenceFactorResults:")||paragraph.getText().contains("«QBacteria:")||paragraph.getText().contains("«Commensal:")) {
				document.removeBodyElement(document.getPosOfParagraph(paragraph));
				return;
			}else if(paragraph.getText().contains("«Viruses_R:")){
				String[] titles=paragraph.getText().split("\\.").length<2?paragraph.getText().split("\\、"):paragraph.getText().split("\\.");
				String title = "";
				if(titles.length <= 1)
					title=titles[0].replace("»", "");
				else
					title=titles[1].replace("»", "");
				XWPFRun tmprun=null;
				for (int i = 0; i < paragraph.getRuns().size(); i++) {
					if(StringUtil.isNotBlank(paragraph.getRuns().get(i).text())) {
						tmprun=paragraph.getRuns().get(i);
						break;
					}
				}
				//String fontFamily="思源黑体";
				String fontFamily="";
				if (StringUtil.isNotBlank(tmprun.getFontFamily())) {
					fontFamily = tmprun.getFontFamily();
				}
				int fontSize=tmprun.getFontSize();
				String fontColor=tmprun.getColor();
				for (int i = paragraph.getRuns().size()-1; i >-1 ; i--) {
					paragraph.removeRun(i);
				}
				XWPFRun run=paragraph.createRun();
				//run.setFontFamily(fontFamily);
				if (StringUtil.isNotBlank(fontFamily)) {
					run.setFontFamily(fontFamily);
				}
				run.setFontSize(fontSize);
				run.setBold(true);
				run.setColor(fontColor);
				run.setText(title);
			}
		}
		//判断一下，如果类型为其他（脑脊液等），删除掉去人源解释
		String P_text=paragraph.getText();
		if(!isHave(yblxs, map.get("yblxdm").toString())&&P_text.contains("«host»")) {
			document.removeBodyElement(document.getPosOfParagraph(paragraph));
		}else {
			List<XWPFRun> runs = paragraph.getRuns();
 			for (int i = 0; i < runs.size() && runs != null; i++) {
				// 获取文本信息
				String text = runs.get(i).getText(runs.get(i).getTextPosition());
				if (StringUtil.isNotBlank(text)) {
					// 为对应一个Run里多个变量，需要进行循环
					if (text.contains("images")) {
						int startTagPos=text.indexOf("«");
						int endTagPos=text.indexOf("»");
						if(startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
							String  varString = text.substring(startTagPos + 1, endTagPos);
							if(varString.contains("images")) {
								@SuppressWarnings("unchecked")
								List<String> wjljList=  (List<String>) map.get("images");
								if(wjljList.size()>0) {
									for (int j = 0; j < wjljList.size(); j++) {
										String imgFile=wjljList.get(j);
										try {
											FileInputStream is =new FileInputStream(imgFile);
											BufferedImage image = ImageIO.read(new File(imgFile));
											BigDecimal divide = new BigDecimal(image.getWidth()).divide(new BigDecimal(image.getHeight()), 2, RoundingMode.HALF_EVEN);
											int imgWidth = new BigDecimal(200).multiply(divide).intValue();
											if(imgWidth>550){//限制最大宽度
												imgWidth=550;
											}
											runs.get(i).setText("",0);
											runs.get(i).addPicture(is, XWPFDocument.PICTURE_TYPE_PNG, imgFile, Units.toEMU(imgWidth), Units.toEMU(200));
										}catch(Exception e) {
											//当没有相应质量文件时的处理
											log.error("报告生成时未找到相应图片：" + imgFile);
										}
									}
								}else {
									runs.get(i).setText("",0);
								}
							}
						}
					}else {
						while (true) {
							int startTagPos = text.indexOf("«");
							int endTagPos = text.indexOf("»");
							// 寻找到需要替换的变量，如果没有找到则到下一个run
							if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
								String varString = text.substring(startTagPos + 1, endTagPos);
								Object object_value = map.get(varString);
								if (object_value instanceof String) {
									String rep_value = (String) object_value;
									// 如果找不到相应的替换问题，则替换成空字符串
									if (StringUtil.isBlank(rep_value)) {
										if("host".equals(varString)) {
											text = text.replace("«" + varString + "»", "");
										}else {
											text = text.replace("«" + varString + "»", "一");
										}
									} else {
										text = text.replace("«" + varString + "»", rep_value);
									}
									runs.get(i).setText(text, 0);
								} else {
									if("host".equals(varString)) {
										text = text.replace("«" + varString + "»", "");
									}else if(varString.contains("REM")){
										if(map.get("sfqry")!=null&&"1".equals(map.get("sfqry").toString())) {
											text=text.replace("«REM", "").replace("»", "");
										}else {
											text="";
										}
									}else {
                                       break;
									}
									runs.get(i).setText(text, 0);
								}
							} else {
								break;
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 替换头部的信息
	 * 
	 * @param paragraph 当前段落
	 * @param map       替换数据
	 */
	private void replaceHead(XWPFParagraph paragraph, Map<String, Object> map) throws DocumentException, XmlException {
		for (int i = 0; i < paragraph.getCTP().sizeOfRArray(); i++) {
			XmlObject xml_obj = paragraph.getCTP().getRArray(i);
			String xml_str = xml_obj.toString();
			String t_xml_str = xml_str;
			while (StringUtils.isNotBlank(t_xml_str)) {
				int startTagPos = t_xml_str.indexOf("«");
				int endTagPos = t_xml_str.indexOf("»");
				if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
					String varString = t_xml_str.substring(startTagPos + 1, endTagPos).trim();
//					Object object_value = map.get(varString);
					Object object_value = replaceJudge(varString,map,null);
					if (object_value instanceof String) {
						String rep_value = (String) object_value;
						while(xml_str.indexOf("«" + varString + "»")>-1){
							if ("Break".equals(rep_value)) {
								varString = "<w:t>"+"«" + varString + "»"+"</w:t>";
								rep_value = "<w:br w:type=\"page\"/>"+"<w:t></w:t>";
								xml_str = xml_str.replace(varString, rep_value);
							}else {
								xml_str = xml_str.replace("«" + varString + "»", rep_value);
							}
						}
						XmlObject xobj = XmlObject.Factory.parse(xml_str);
						xml_obj.set(xobj);
					}
				}else {
					break;
				}
				if(endTagPos >=0) {
                    t_xml_str = t_xml_str.substring(endTagPos + 1);
                } else {
                    t_xml_str = "";
                }
			}
		}
	}

	/**
	 * 合并相同的行
	 * 
	 * @param table
	 */
	private void tableColspan(XWPFTable table) {
		int countRow = table.getNumberOfRows();
		for (int i = 0; i < countRow; i++) {// 每一行
			if (i == countRow - 1) {
				break;
			} else {
				XWPFTableRow row = table.getRow(i);
				List<XWPFTableCell> cells = row.getTableCells();
				if(cells.size()==6) {
					XWPFTableCell cell00 = table.getRow(i).getCell(0);
					XWPFTableCell cell01 = table.getRow(i).getCell(1);
					XWPFTableCell cell02 = table.getRow(i).getCell(2);
					if (  table.getRow(i + 1).getTableCells().size() >= 3 ){
						XWPFTableCell cell10 = table.getRow(i + 1).getCell(0);
						XWPFTableCell cell11 = table.getRow(i + 1).getCell(1);
						XWPFTableCell cell12 = table.getRow(i + 1).getCell(2);
						if (!StringUtil.isAnyBlank(cell00.getText(), cell01.getText(), cell02.getText(), cell10.getText(), cell11.getText(), cell12.getText())) {
							if ( cell00.getText().equals(cell10.getText()) && cell01.getText().equals(cell11.getText()) && cell02.getText().equals(cell12.getText()) ) {
								cell00.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
								cell10.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
								table.getRow(i).getCell(1).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
								table.getRow(i + 1).getCell(1).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
								table.getRow(i).getCell(2).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
								table.getRow(i + 1).getCell(2).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
							}
						}
					}

				}else if(cells.size()==7) {
					XWPFTableCell cell00 = table.getRow(i).getCell(0);
					XWPFTableCell cell01 = table.getRow(i).getCell(1);
					XWPFTableCell cell02 = table.getRow(i).getCell(2);
					XWPFTableCell cell03 = table.getRow(i).getCell(3);
					if( table.getRow(i + 1).getTableCells().size()>1) {
						XWPFTableCell cell10=table.getRow(i + 1).getCell(0);
						XWPFTableCell cell11=table.getRow(i + 1).getCell(1);
						if ( table.getRow(i + 1).getTableCells().size()>=4){
							XWPFTableCell cell12=table.getRow(i + 1).getCell(2);
							XWPFTableCell cell13=table.getRow(i + 1).getCell(3);
							if ( StringUtil.isNotBlank(cell01.getText()) && StringUtil.isNotBlank(cell11.getText()) && StringUtil.isNotBlank(cell02.getText()) && StringUtil.isNotBlank(cell12.getText()) && StringUtil.isNotBlank(cell03.getText()) && StringUtil.isNotBlank(cell13.getText()) ) {
								if ( cell00.getText().equals(cell10.getText()) && cell01.getText().equals(cell11.getText()) && cell02.getText().equals(cell12.getText()) && cell03.getText().equals(cell13.getText()) ) {
									table.getRow(i).getCell(0).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
									table.getRow(i + 1).getCell(0).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
									cell01.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
									cell11.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
									table.getRow(i).getCell(2).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
									table.getRow(i + 1).getCell(2).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
									table.getRow(i).getCell(3).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
									table.getRow(i + 1).getCell(3).getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
								}
							}
						}

					}

				} else if (tmpate_flag.contains("IMP_REPORT_RFS_TEMEPLATE")) {
					//ResFirst报告，若表格最后一行的所有单元格内容相同，则合并
					int cellSize = table.getRow(countRow - 1).getTableCells().size();
					if( cellSize>2) {
						boolean merge = true;
						String cellText = table.getRow(countRow - 1).getCell(0).getText();
						if (StringUtil.isNotBlank(cellText)){
							for (int cn = 1; cn < cellSize; cn++) {
								if (!cellText.equals(table.getRow(countRow - 1).getCell(cn).getText())) {
									merge = false;
								}
							}
							if (merge){
								table.getRow(countRow - 1).getCell(0).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
								for (int cn = 1; cn < cellSize; cn++) {
									table.getRow(countRow - 1).getCell(cn).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 设置标题
	 * @param paragraph
	 */
	private void replacTitle(XWPFParagraph paragraph,XWPFDocument document,Map<String,Object> map) {
		if(paragraph.getText().contains("«Bacteria:")||paragraph.getText().contains("«Mycobacteria:")||paragraph.getText().contains("«MCR:")||paragraph.getText().contains("«Fungi:")||paragraph.getText().contains("«Viruses_D:")||paragraph.getText().contains("«Viruses_R:")
				||paragraph.getText().contains("«Parasite:")||paragraph.getText().contains("«Background:")||paragraph.getText().contains("«VirulenceFactorStat:")||paragraph.getText().contains("«Gene:")||paragraph.getText().contains("«Results:")||paragraph.getText().contains("«VirulenceFactorResults:")||paragraph.getText().contains("«Viruses:")||paragraph.getText().contains("«Commensal:")) {
//			String title=paragraph.getText().split("\\:")[1].replace("»", "");
			String title=paragraph.getText().replace(paragraph.getText().substring(paragraph.getText().indexOf("«"),paragraph.getText().indexOf(":")+1),"").replace("»", "");
			XWPFRun tmprun=null;
			for (int i = 0; i < paragraph.getRuns().size(); i++) {
				if(StringUtil.isNotBlank(paragraph.getRuns().get(i).text())) {
					tmprun=paragraph.getRuns().get(i);
					break;
				}
			}
			//String fontFamily="思源黑体";
			String fontFamily="";
			if (StringUtil.isNotBlank(tmprun.getFontFamily())) {
				fontFamily = tmprun.getFontFamily();
			}
			int fontSize=tmprun.getFontSize();
			String fontColor=tmprun.getColor();
			for (int i = paragraph.getRuns().size()-1; i >-1 ; i--) {
				paragraph.removeRun(i);
			}
			XWPFRun run=paragraph.createRun();
			//run.setFontFamily(fontFamily);
			if (StringUtil.isNotBlank(fontFamily)) {
				run.setFontFamily(fontFamily);
			}
			run.setFontSize(fontSize);
			run.setBold(true);
			run.setColor(fontColor);
			run.setText(title);
		}else if(!isHave(yblxs,map.get("yblxdm").toString())&&paragraph.getText().contains("«host»")) {
			document.removeBodyElement(document.getPosOfParagraph(paragraph));
		}
	}
	
	/**
	 * 表格里边设置加粗，斜体
	 * @param table
	 */
	private  void  addBlodForTable(XWPFTable table) {
		List<XWPFTableRow> rows=table.getRows();
		for (XWPFTableRow row:rows) {
			List<XWPFTableCell> cells=row.getTableCells();
			for (XWPFTableCell cell:cells) {
				if(cell.getText()!=null) {
					if(cell.getText().contains("{br}")) {
						String[] texts=cell.getText().split("\\{br}");
						if(texts.length>0) {
							for (int i =cell.getParagraphs().size()-1; i >0 ; i--) {
								cell.removeParagraph(i);
							}
							XWPFParagraph paragraph=cell.getParagraphs().get(0);
//							if(paragraph.getText().contains("<Indent>")) {
//								paragraph.setIndentationHanging(700);
//							}
							XWPFRun tmprun=null;
							for (int i = 0; i < paragraph.getRuns().size(); i++) {
								if(StringUtil.isNotBlank(paragraph.getRuns().get(i).text())) {
									tmprun=paragraph.getRuns().get(i);
									break;
								}
							}
//							String fontFamily="思源黑体";
							String fontFamily="";
							if(StringUtil.isNotBlank(tmprun.getFontFamily())) {
								fontFamily=tmprun.getFontFamily();
							}
                            int fontSize=0;
							if(tmprun.getFontSize()>-1) {
								fontSize=tmprun.getFontSize();
							}
                            for (int j = paragraph.getRuns().size()-1; j >-1; j--) {
								paragraph.removeRun(j);
							}
							for (int i = 0; i < texts.length; i++) {
								XWPFRun run=paragraph.createRun();
								//run.setFontFamily(fontFamily);
								if (StringUtil.isNotBlank(fontFamily)) {
									run.setFontFamily(fontFamily);
								}
								if(fontSize>0) {
									run.setFontSize(fontSize);
								}
								String text = "";
								if(texts[i]!=null) {
                                    text=texts[i].replace("null", "");
                                }
 								if(text.contains("{\\n}")||text.contains("{n}")){
									run.addBreak();
									text="  "+text.replace("{\\n}", "").replace("{n}", "");
								}
								if(text.contains("{b+i}")) {
									run.setBold(true);
									run.setItalic(true);
									text=text.replace("{b+i}", "");
								}
								if(text.contains("{b}")) {
									run.setBold(true);
									text=text.replace("{b}", "");
									if(text.contains(":")){//耐药基因检测结果解释/微生物检测结果解释/疑似或人体共生
										//增加小标题和内容之前的空格 ,改if代码框于 ssm 2021/9/23添加
										text=text.replace(":", ": ");
									}
								}
//								if(text.contains("【") && text.contains("】")){//修改论文引用为上标
//									String text_cope = text;
//									String sub = text.substring(    text.indexOf("【"),  text.indexOf("】")+1   );
//									run.setSubscript(VerticalAlign.SUPERSCRIPT);
//								}
								if(text.contains("{i}")) {
									run.setItalic(true);
									text=text.replace("{i}", "");
								}
								if(text.contains("{color:")){
									int begin=text.indexOf("{color:");
									String lstext = text.substring(begin);
									int end=lstext.indexOf("}")+begin;
									String flg=text.substring(begin, end+1);
									String color=flg.substring(1,flg.length()-1).split(":")[1];
									run.setColor(color);
									text=text.replace(flg, "");
								}
								run.setText(text);
							}
						}
					}else if (cell.getText().contains("{color:") && cell.getText().contains("{/color}")){
						List<XWPFParagraph> paragraphs = cell.getParagraphs();
						for (XWPFParagraph paragraph : paragraphs) {
							String text = paragraph.getText();
							if (text.contains("{color:") && text.contains("{/color}")){
								List<XWPFRun> runs = paragraph.getRuns();
								int size = runs.size();
								for (int i = runs.size() - 1; i >= 0; i--) {
									String runtext = runs.get(i).text();
									if (runtext.contains("{color:") && runtext.contains("{/color}")){
										for (int j = 0; j < i; j++) {
											XWPFRun run = paragraph.createRun();
											run.getCTR().setRPr(runs.get(j).getCTR().getRPr());
											run.setText(runs.get(j).text());
										}
										XWPFRun beforeRun = paragraph.createRun();
										beforeRun.getCTR().setRPr(runs.get(i).getCTR().getRPr());
										beforeRun.setText(runtext.substring(0, runtext.indexOf("{color:")));
										XWPFRun replaceRun = paragraph.createRun();
										replaceRun.getCTR().setRPr(runs.get(i).getCTR().getRPr());
										String color = runtext.substring(runtext.indexOf("{color:")+7, runtext.indexOf("}"));
										replaceRun.setText(runtext.substring(runtext.indexOf("{color:"+color+"}")+("{color:"+color+"}").length(), runtext.indexOf("{/color}")));
										replaceRun.setColor(color);
										XWPFRun afterRun = paragraph.createRun();
										afterRun.getCTR().setRPr(runs.get(i).getCTR().getRPr());
										afterRun.setText(runtext.substring(runtext.indexOf("{/color}")+8));
										for (int j = i+1; j < size; j++) {
											XWPFRun run = paragraph.createRun();
											run.getCTR().setRPr(runs.get(j).getCTR().getRPr());
											run.setText(runs.get(j).text());
										}
										for (int h = size; h > i; h--) {
											paragraph.removeRun(h-1);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 段落里边设置加粗，斜体
	 * @param paragraph
	 */
	private  void  addBlodForParagraph(XWPFParagraph paragraph) {
		if(paragraph.getText().contains("{br}")){
			String[] texts=paragraph.getText().split("\\{br}");
			XWPFRun tmprun=null;
			for (int i = 0; i < paragraph.getRuns().size(); i++) {
				tmprun=paragraph.getRuns().get(i);
				if(StringUtil.isNotBlank(paragraph.getRuns().get(i).text())) {
					tmprun=paragraph.getRuns().get(i);
					break;
				}
			}
			String fontFamily="";
			if(StringUtil.isNotBlank(tmprun.getFontFamily())) {
				fontFamily=tmprun.getFontFamily();
			}
            int fontSize = Math.max(tmprun.getFontSize(), 0);
			if(texts.length>0) {
				for (int j = paragraph.getRuns().size()-1; j >-1; j--) {
					paragraph.removeRun(j);
				}
				for (int i = 0; i < texts.length; i++) {
					XWPFRun run=paragraph.createRun();
					//run.setFontFamily(fontFamily);
					if (StringUtil.isNotBlank(fontFamily)) {
						run.setFontFamily(fontFamily);
					}
					if(fontSize>0) {
						run.setFontSize(fontSize);
					}
					String text=texts[i].replace("null", "");
					if(text.contains("{\\n}")||text.contains("{n}")){
						run.addBreak();
						text="  "+text.replace("{\\n}", "").replace("{n}", "");
					}
					if(text.contains("{b+i}")) {
						run.setBold(true);
						run.setItalic(true);
						text=texts[i].replace("{b+i}", "");
					}
					if(text.contains("{b}")) {
						run.setBold(true);
						text=texts[i].replace("{b}", "");
					}
					if(text.contains("{i}")) {
						run.setItalic(true);
						text=texts[i].replace("{i}", "");
					}
					if(text.contains("{color:")){
						int begin=texts[i].indexOf("{");
						int end=texts[i].indexOf("}");
						String flg=texts[i].substring(begin, end+1);
						String color=flg.substring(1,flg.length()-1).split(":")[1];
						run.setColor(color);
						text=texts[i].replace(flg, "");
					}
					run.setText(text);
				}
			}
		}
	}
	/**
	 * 上传Word文件
	 * @return
	 */
	private boolean sendWordFile(String fileName,String FTP_URL) {
		//连接服务器
		//FTPClient ftp=FtpUtil.connect(FTPWORD_PATH, FTP_URL, FTP_PORT, FTP_USER, FTP_PD );
		//上传到服务器word下边的文件夹
		//FtpUtil.upload(new File(wjljjm), ftp);
		try {
			File t_file = new File(fileName);
			//文件不存在不做任何操作
			if(!t_file.exists()) {
                return true;
            }
			
			byte[] bytesArray = new byte[(int) t_file.length()];
			
			FileInputStream t_fis = new FileInputStream(t_file);
			t_fis.read(bytesArray); //read file into bytes[]
			t_fis.close();
			//需要给文件的名称
			MatridxByteArrayResource contentsAsResource = new MatridxByteArrayResource(bytesArray);
			contentsAsResource.setFilename(t_file.getName());
			
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("file", contentsAsResource);
			RestTemplate t_restTemplate = new RestTemplate();
			//发送文件到服务器
			String reString = t_restTemplate.postForObject("http://" + FTP_URL + ":8756/file/uploadWordFile", paramMap, String.class);
			return "OK".equals(reString);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 图片叠加方法(png格式，第一张为背景图可以传透明图片，从第二张开始浮动)
	 * @param run
	 * @param picList
	 * @return
	 */
	public boolean overlayPictures(XWPFRun run, List<PictureFloat> picList) {
		// 图片路径
		FileInputStream image = null;
		try {
			if(picList != null && picList.size() > 0) {
				run.setText("",0);
				for (int i = 0; i < picList.size(); i++) {
					PictureFloat pictureFloat = picList.get(i);
					image = new FileInputStream(pictureFloat.getWjlj());
					run.addPicture(image, XWPFDocument.PICTURE_TYPE_PNG, pictureFloat.getWjlj(), Units.toEMU(pictureFloat.getWidth()), Units.toEMU(pictureFloat.getHeight()));
					if(i > 0) {
						imageFloatDeal(run, i, pictureFloat.getWidth(),
								pictureFloat.getHeight(), pictureFloat.getLeft(), pictureFloat.getTop());
					}
					image.close();
				}
			}
	        return true;
		} catch (Exception e) {
			log.error(e.toString());
		} finally {
			try {
				if(image != null) {
                    image.close();
                }
			} catch (IOException e) {
				log.error(e.toString());
			}
		}
		return false;
	}

	public boolean upsetPictures(XWPFRun run, List<PictureFloat> picList){
		// 图片路径
		FileInputStream image = null;
		try {
			if(picList != null && picList.size() > 0) {
				run.setText("",0);
				for (int i = 0; i < picList.size(); i++) {
					PictureFloat pictureFloat = picList.get(i);
					image = new FileInputStream(pictureFloat.getWjlj());
					run.addPicture(image, XWPFDocument.PICTURE_TYPE_PNG, pictureFloat.getWjlj(), Units.toEMU(pictureFloat.getWidth()), Units.toEMU(pictureFloat.getHeight()));
					if(i > 0) {
						imageUpsetDeal(run, i, pictureFloat.getWidth(),
								pictureFloat.getHeight(), pictureFloat.getLeft(), pictureFloat.getTop());
					}
					image.close();
					CTDrawing drawingArray = run.getCTR().getDrawingArray(0);
					CTGraphicalObject graphic = drawingArray.getInlineArray(0).getGraphic();
					Random random = new Random();
					int number = random.nextInt(999)+ 1;
					CTAnchor anchorWithGraphic = getAnchorWithGraphic(graphic, "Seal" + number,Units.toEMU(pictureFloat.getWidth()), Units.toEMU(pictureFloat.getHeight()),Units.toEMU(pictureFloat.getLeft()), Units.toEMU(pictureFloat.getTop()), false);
					drawingArray.setAnchorArray(new CTAnchor[]{anchorWithGraphic});
					drawingArray.removeInline(0);

				}
			}
			return true;
		} catch (Exception e) {
			log.error(e.toString());
		} finally {
			try {
				if(image != null) {
                    image.close();
                }
			} catch (IOException e) {
				log.error(e.toString());
			}
		}
		return false;
	}

	/**图片浮动处理
	 * @param run
	 * @param i
	 * @param width
	 * @param height
	 * @param left
	 * @param top
	 * @throws XmlException
	 */
	
	private void imageFloatDeal(XWPFRun run, int i, int width, int height, int left, int top) throws XmlException {
		// 获取到浮动图片数据
        CTDrawing mid_drawing = run.getCTR().getDrawingArray(i);
        CTGraphicalObject mid_graphicalobject = mid_drawing.getInlineArray(0).getGraphic();
        // 拿到新插入的图片替换添加CTAnchor 设置浮动属性 删除inline属性
        CTAnchor mid_anchor = getAnchorWithGraphic(mid_graphicalobject, "mid",
                Units.toEMU(width), Units.toEMU(height),//图片大小
                Units.toEMU(left), Units.toEMU(top), false);//相对当前段落位置 需要计算段落已有内容的左偏移
        mid_drawing.setAnchorArray(new CTAnchor[]{mid_anchor});//添加浮动属性
        mid_drawing.removeInline(0);//删除行内属性
	}
	/**图片浮动处理
	 * @param run
	 * @param i
	 * @param width
	 * @param height
	 * @param left
	 * @param top
	 * @throws XmlException
	 */

	private void imageUpsetDeal(XWPFRun run, int i, int width, int height, int left, int top) throws XmlException {
		// 获取到浮动图片数据
        CTDrawing mid_drawing = run.getCTR().getDrawingArray(i);
        CTGraphicalObject mid_graphicalobject = mid_drawing.getInlineArray(0).getGraphic();
        // 拿到新插入的图片替换添加CTAnchor 设置浮动属性 删除inline属性
        CTAnchor mid_anchor = getAnchorWithGraphic(mid_graphicalobject, "mid",
                Units.toEMU(width), Units.toEMU(height),//图片大小
                Units.toEMU(left), Units.toEMU(top), true);//相对当前段落位置 需要计算段落已有内容的左偏移
        mid_drawing.setAnchorArray(new CTAnchor[]{mid_anchor});//添加浮动属性
        mid_drawing.removeInline(0);//删除行内属性
	}
	
    /**word添加图片
     * @param ctGraphicalObject 图片数据
     * @param deskFileName      图片描述
     * @param width             宽
     * @param height            高
     * @param leftOffset        水平偏移 left
     * @param topOffset         垂直偏移 top
     * @param behind            文字上方，文字下方
     * @return
     * @throws XmlException 
     * @throws Exception
     */
	private CTAnchor getAnchorWithGraphic(CTGraphicalObject ctGraphicalObject,
			String deskFileName, int width, int height,
			int leftOffset, int topOffset, boolean behind) throws XmlException {
		String anchorXML = "<wp:anchor xmlns:wp=\"http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing\" "
			+ "simplePos=\"0\" relativeHeight=\"0\" behindDoc=\"" + ((behind) ? 1 : 0) + "\" locked=\"0\" layoutInCell=\"1\" allowOverlap=\"1\">"
			+ "<wp:simplePos x=\"0\" y=\"0\"/>"
			+ "<wp:positionH relativeFrom=\"column\">"
			+ "<wp:posOffset>" + leftOffset + "</wp:posOffset>"
			+ "</wp:positionH>"
			+ "<wp:positionV relativeFrom=\"paragraph\">"
			+ "<wp:posOffset>" + topOffset + "</wp:posOffset>"
			+ "</wp:positionV>"
			+ "<wp:extent cx=\"" + width + "\" cy=\"" + height + "\"/>"
			+ "<wp:effectExtent l=\"0\" t=\"0\" r=\"0\" b=\"0\"/>"
			+ "<wp:wrapNone/>"
			+ "<wp:docPr id=\"1\" name=\"Drawing 0\" descr=\"" + deskFileName + "\"/><wp:cNvGraphicFramePr/>"
			+ "</wp:anchor>";
		CTDrawing drawing = null;
		drawing = CTDrawing.Factory.parse(anchorXML);
		CTAnchor anchor = drawing.getAnchorArray(0);
		anchor.setGraphic(ctGraphicalObject);
		return anchor;
	}
	
	/**
	 * 不需要添加图片的表格
	 * @param table
	 * @param sjwzxxList
	 */
	private void addNewRows(XWPFTable table,List<SjwzxxDto> sjwzxxList,Map<String,Object> t_map) {
		
		XWPFTableRow tmpRow = table.getRows().get(table.getRows().size() - 1);
		List<XWPFTableCell> tmpCells = tmpRow.getTableCells();
		String tmpCellText = tmpCells.get(0).getText();
		String style = "";
		String columns = "";
		if (tmpCellText.indexOf("[") !=-1 && tmpCellText.indexOf("]") != -1){
			//[seg:2-1,align:center]
			style = tmpCellText.substring(tmpCellText.indexOf("[")+1,tmpCellText.indexOf("]"));
			//将content转换成List
			String[] styles = style.split(",");
			String seg = "";
			for (String s : styles) {
				if (s.contains(":")){
					String[] split = s.split(":");
					if ("seg".equals(split[0])){
						seg = split[1];
					}
				}
			}
			if (StringUtil.isNotBlank(seg)){
				String[] split = seg.split("-");
				columns = split[0];
			}
		}

		int cellsize = tmpCells.size();
		if (StringUtil.isNotBlank(columns)){
			int copySize = sjwzxxList.size()/Integer.parseInt(columns) + (sjwzxxList.size()%Integer.parseInt(columns)>0?1:0);
			for (int i = 0; i < copySize; i++) {
				XWPFTableRow row = copy(table, tmpRow, table.getRows().size());
				try {
					List<XWPFTableCell> cells = row.getTableCells();
					// 插入的行会填充与表格第一行相同的列数
					for (int j = 0; j < cellsize; j++) {
						// 由于 list的 size 和 List<XWPFTableCell> cells的 size 相等，所以可以直接用一个索引
						XWPFTableCell cell = cells.get(j);
						XWPFTableCell tmpCell = tmpCells.get(j);
						String tmpText = tmpCell.getText();
						String cellText = cell.getText();
						if (StringUtil.isNotBlank(tmpText)) {
							List<XWPFParagraph> paragraphs = cell.getParagraphs();
							if (paragraphs!=null && paragraphs.size()>0){
								for (XWPFParagraph paragraph : paragraphs) {
									List<XWPFRun> runs = paragraph.getRuns();
									if (runs!=null && runs.size()>0){
										for (XWPFRun run : runs) {
											String text = run.getText(run.getTextPosition()!=-1?run.getTextPosition():0);
											int index = getXwpfParagraphIndex(text);
											String s_value="";
											for (int y = 0; y < index; y++) {
												int startTagPos = text.indexOf("«");
												int endTagPos = text.indexOf("»");
												// 寻找到需要替换的变量，如果没有找到则到下一个run
												if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
													//[seg:2-1,align:center]
													String cellstyle = cellText.substring(cellText.indexOf("[")+1,cellText.indexOf("]"));
													//将content转换成List
													String[] cellstyles = cellstyle.split(",");
													String cellseg = "";
													for (String s : cellstyles) {
														if (s.contains(":")){
															String[] split = s.split(":");
															if ("seg".equals(split[0])){
																cellseg = split[1];
															}
														}
													}
													String cellindex = "";
													if (StringUtil.isNotBlank(cellseg)){
														String[] split = cellseg.split("-");
														cellindex = split[1];
													}
													int listindex = i*2+Integer.parseInt(cellindex)-1;
													SjwzxxDto sjwzxxDto = sjwzxxList.get(listindex);
													String tmpVar = text.substring(startTagPos + 1, endTagPos).trim();
													tmpVar = tmpVar.replace(cellText.substring(cellText.indexOf("["),cellText.indexOf("]")+1),"");
													if (functions.stream().anyMatch(tmpVar::contains)){
														try {
															String formatStr = tmpVar;
															Map<String, Object> parameters = new HashMap<>();
															int tmpVarIndex = 0;
															while (formatStr.contains("{") && formatStr.contains("}")){
																String key = formatStr.substring(formatStr.indexOf("{") + 1, formatStr.indexOf("}"));
																Method t_method = sjwzxxDto.getClass().getMethod("get" + key.split("\\.")[1].substring(0, 1).toUpperCase() + key.split("\\.")[1].substring(1));
																Object value = t_method.invoke(sjwzxxDto);
																formatStr = formatStr.replace("{"+key+"}","parameter"+tmpVarIndex);
																parameters.put("parameter"+tmpVarIndex,value);
																index ++;
															}
															EvaluableExpression evaluable = new EvaluableExpression(formatStr,functionMap);
															Object evaluate = evaluable.evaluate(parameters);
															tmpVar = evaluate.toString().replaceAll("「","{").replaceAll("」","}");
														} catch (Exception e) {
															log.error("替换公式错误:{}",tmpVar);
															tmpVar = "";
														}
														s_value = tmpVar;
													} else {
														tmpVar = tmpVar.split("\\.")[1];
														String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);
														if (text.indexOf("?") >-1 && text.indexOf(":")>-1){
															if (listindex >= sjwzxxList.size()){
																s_value = "";
															}else {
																s_value = getJudgeValue(text, startTagPos, endTagPos, sjwzxxDto,listindex);
															}
														} else if (text.indexOf("?")>-1) {
															if (listindex >= sjwzxxList.size()){
																s_value = "";
															}else {
																Map<String,Object> map=new HashMap<>();
																map.put("wz_flag","1");
																map.put("wzxxDto",sjwzxxDto);
																s_value = (String)replaceJudge(text.substring(startTagPos+1, endTagPos),map,cell);
															}
														} else {
															if (listindex >= sjwzxxList.size()){
																s_value = "";
															}else {
																// 反射方法 把map的value 插入到数据中
																Method t_method = sjwzxxDto.getClass().getMethod("get" + TotmpVar);
																s_value = (String) t_method.invoke(sjwzxxDto);
															}
														}
													}
													text = text.replace(text.substring(startTagPos, endTagPos+1),s_value);
												}
											}
											run.setText(text,run.getTextPosition()!=-1?run.getTextPosition():0);
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error(e.toString());
				}

			}
		} else {
			int schs=0;//删除行数//物种注释对应文献编号
			for (int x = 0; x < sjwzxxList.size(); x++) {
				XWPFTableRow row = copy(table, tmpRow, table.getRows().size());
				SjwzxxDto sjwzxxDto = sjwzxxList.get(x);
				try {
					List<XWPFTableCell> cells = row.getTableCells();
					// 插入的行会填充与表格第一行相同的列数
					for (int i = 0; i < cellsize; i++) {
						// 由于 list的 size 和 List<XWPFTableCell> cells的 size 相等，所以可以直接用一个索引
						XWPFTableCell cell = cells.get(i);
						XWPFTableCell tmpCell = tmpCells.get(i);
						String tmpText = tmpCell.getText();
						String cellText = cell.getText();
						if (StringUtil.isNotBlank(tmpText)) {
							if (cellText.contains("italic_") || cellText.contains("break_")){
								int index = getXwpfParagraphIndex(tmpText);
								boolean isItalic = false;
								boolean isBreak=false;
								for (int j = 0; j < index; j++) {
									for (int l = 0; l < index; l++) {
										int star = tmpText.indexOf("«");
										int end = tmpText.indexOf("»");
										String s_value="";
										if (star > -1 && end > -1) {
											String content = tmpText.substring(star, end + 1).toString();
											// 判断 是否需要斜体
											if (content.contains("italic_")) {
												isItalic = true;
												content=content.replace("italic_", "");
											}
											if(content.contains("break_")) {
												isBreak = true;
												content=content.replace("break_", "");
											}
											int startTagPos = content.indexOf("«");
											int endTagPos = content.indexOf("»");
											// 寻找到需要替换的变量，如果没有找到则到下一个run
											if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
												String tmpVar = content.substring(startTagPos + 1, endTagPos).trim();
												if (functions.stream().anyMatch(tmpVar::contains)){
													try {
														String formatStr = tmpVar;
														Map<String, Object> parameters = new HashMap<>();
														int tmpVarIndex = 0;
														while (formatStr.contains("{") && formatStr.contains("}")){
															String key = formatStr.substring(formatStr.indexOf("{") + 1, formatStr.indexOf("}"));
															Method t_method = sjwzxxDto.getClass().getMethod("get" + key.split("\\.")[1].substring(0, 1).toUpperCase() + key.split("\\.")[1].substring(1));
															Object value = t_method.invoke(sjwzxxDto);
															formatStr = formatStr.replace("{"+key+"}","parameter"+tmpVarIndex);
															parameters.put("parameter"+tmpVarIndex,value);
															index ++;
														}
														EvaluableExpression evaluable = new EvaluableExpression(formatStr,functionMap);
														Object evaluate = evaluable.evaluate(parameters);
														tmpVar = evaluate.toString().replaceAll("「","{").replaceAll("」","}");
													} catch (Exception e) {
														log.error("替换公式错误:{}",tmpVar);
														tmpVar = "";
													}
													s_value = tmpVar;
												} else {
													s_value = getJudgeValue(content, startTagPos, endTagPos, sjwzxxDto,x);
													if (star>0){
														s_value=tmpText.substring(0, star ) + s_value ;
													}
													if (l==index-1 && end!=tmpText.length() && end<=tmpText.length()){
														s_value = s_value + tmpText.substring(end+1);
													}
												}
												setCellText(tmpCells.get(i), cell, s_value, isItalic,isBreak,true);
											}
//									tmpText = tmpText.replace(tmpText.substring(star, end + 1), "");
											tmpText = tmpText.replace(tmpText.substring(0, end + 1), "");
										}
									}
								}
							}
							else {
								List<XWPFParagraph> paragraphs = cell.getParagraphs();
								if (paragraphs!=null && paragraphs.size()>0){
									for (XWPFParagraph paragraph : paragraphs) {
										List<XWPFRun> runs = paragraph.getRuns();
										if (runs!=null && runs.size()>0){
											for (XWPFRun run : runs) {
												String text = run.getText(run.getTextPosition());
												int index = getXwpfParagraphIndex(text);
												String s_value="";
												for (int y = 0; y < index; y++) {
													int startTagPos = text.indexOf("«");
													int endTagPos = text.indexOf("»");
													// 寻找到需要替换的变量，如果没有找到则到下一个run
													if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
														String tmpVar = text.substring(startTagPos + 1, endTagPos).trim();
														if (functions.stream().anyMatch(tmpVar::contains)){
															try {
																String formatStr = tmpVar;
																Map<String, Object> parameters = new HashMap<>();
																int tmpVarIndex = 0;
																while (formatStr.contains("{") && formatStr.contains("}")){
																	String key = formatStr.substring(formatStr.indexOf("{") + 1, formatStr.indexOf("}"));
																	Method t_method = sjwzxxDto.getClass().getMethod("get" + key.split("\\.")[1].substring(0, 1).toUpperCase() + key.split("\\.")[1].substring(1));
																	Object value = t_method.invoke(sjwzxxDto);
																	formatStr = formatStr.replace("{"+key+"}","parameter"+tmpVarIndex);
																	parameters.put("parameter"+tmpVarIndex,value);
																	index ++;
																}
																EvaluableExpression evaluable = new EvaluableExpression(formatStr,functionMap);
																Object evaluate = evaluable.evaluate(parameters);
																tmpVar = evaluate.toString().replaceAll("「","{").replaceAll("」","}");
															} catch (Exception e) {
																log.error("替换公式错误:{}",tmpVar);
																tmpVar = "";
															}
															s_value = tmpVar;
														} else {
															tmpVar = tmpVar.split("\\.")[1];
															String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);
															if (text.indexOf("?") > -1 && text.indexOf(":") > -1) {
																s_value = getJudgeValue(text, startTagPos, endTagPos, sjwzxxDto, x);
															} else if (text.indexOf("?") > -1) {
																Map<String, Object> map = new HashMap<>();
																map.putAll(t_map);
																map.put("wz_flag", "1");
																map.put("wzxxDto", sjwzxxDto);
																s_value = (String) replaceJudge(text.substring(startTagPos + 1, endTagPos), map,cell);
															} else {
																// 反射方法 把map的value 插入到数据中
																Method t_method = sjwzxxDto.getClass().getMethod("get" + TotmpVar);
																s_value = (String) t_method.invoke(sjwzxxDto);
															}
														}

														text = text.replace(text.substring(startTagPos, endTagPos+1),s_value);
													}
												}
												run.setText(text,run.getTextPosition()!=-1?run.getTextPosition():0);
											}
										}
									}
								}
							}
						}
					}
					List<XWPFTableCell> tableCells = row.getTableCells();
					int num = 0;
					for (int j = 0; j < tableCells.size(); j++) {
						if (StringUtil.isBlank(tableCells.get(j).getText().replace("null", ""))){
							num +=1;
						}
					}
					if (num ==  tableCells.size() && (x<sjwzxxList.size()-1 || schs!=x) ){
						schs=schs+1;
						table.removeRow(table.getRows().size()-1);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error(e.toString());
				}
			}
		}

	}

	/**
	 * 不需要添加图片的表格
	 * @param table
	 * @param sjwzxxList
	 */
	private void addNewObjectRows(XWPFTable table,List<?> sjwzxxList,Map<String,Object> t_map) {

		XWPFTableRow tmpRow = table.getRows().get(table.getRows().size() - 1);
		List<XWPFTableCell> tmpCells = tmpRow.getTableCells();
		String tmpCellText = tmpCells.get(0).getText();
		String style = "";
		if (tmpCellText.indexOf("[") !=-1 && tmpCellText.indexOf("]") != -1){
			//[seg:2-1,align:center]
			style = tmpCellText.substring(tmpCellText.indexOf("[")+1,tmpCellText.indexOf("]"));
		}
		//将content转换成List
		String[] styles = style.split(",");
		String seg = "";
		for (String s : styles) {
			if (s.contains(":")){
				String[] split = s.split(":");
				if ("seg".equals(split[0])){
					seg = split[1];
				}
			}
		}
		String columns = "";
		if (StringUtil.isNotBlank(seg)){
			String[] split = seg.split("-");
			columns = split[0];
		}

		int cellsize = tmpCells.size();
		if (StringUtil.isNotBlank(columns)){
			int copySize = sjwzxxList.size()/Integer.parseInt(columns) + (sjwzxxList.size()%Integer.parseInt(columns)>0?1:0);
			for (int i = 0; i < copySize; i++) {
				XWPFTableRow row = copy(table, tmpRow, table.getRows().size());
				try {
					List<XWPFTableCell> cells = row.getTableCells();
					// 插入的行会填充与表格第一行相同的列数
					for (int j = 0; j < cellsize; j++) {
						// 由于 list的 size 和 List<XWPFTableCell> cells的 size 相等，所以可以直接用一个索引
						XWPFTableCell cell = cells.get(j);
						XWPFTableCell tmpCell = tmpCells.get(j);
						String tmpText = tmpCell.getText();
						String cellText = cell.getText();
						if (StringUtil.isNotBlank(tmpText)) {
							List<XWPFParagraph> paragraphs = cell.getParagraphs();
							if (paragraphs!=null && paragraphs.size()>0){
								for (XWPFParagraph paragraph : paragraphs) {
									List<XWPFRun> runs = paragraph.getRuns();
									if (runs!=null && runs.size()>0){
										for (XWPFRun run : runs) {
											String text = run.getText(run.getTextPosition()!=-1?run.getTextPosition():0);
											int index = getXwpfParagraphIndex(text);
											String s_value="";
											for (int y = 0; y < index; y++) {
												int startTagPos = text.indexOf("«");
												int endTagPos = text.indexOf("»");
												// 寻找到需要替换的变量，如果没有找到则到下一个run
												if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
													//[seg:2-1,align:center]
													String cellstyle = cellText.substring(cellText.indexOf("[")+1,cellText.indexOf("]"));
													//将content转换成List
													String[] cellstyles = cellstyle.split(",");
													String cellseg = "";
													for (String s : cellstyles) {
														if (s.contains(":")){
															String[] split = s.split(":");
															if ("seg".equals(split[0])){
																cellseg = split[1];
															}
														}
													}
													String cellindex = "";
													if (StringUtil.isNotBlank(cellseg)){
														String[] split = cellseg.split("-");
														cellindex = split[1];
													}
													int listindex = i*2+Integer.parseInt(cellindex)-1;
													Object sjwzxxDto = sjwzxxList.get(listindex);
													String tmpVar = text.substring(startTagPos + 1, endTagPos).trim();
													tmpVar = tmpVar.replace(cellText.substring(cellText.indexOf("["),cellText.indexOf("]")+1),"");
													if (functions.stream().anyMatch(tmpVar::contains)){
														try {
															String formatStr = tmpVar;
															Map<String, Object> parameters = new HashMap<>();
															int tmpVarIndex = 0;
															while (formatStr.contains("{") && formatStr.contains("}")){
																String key = formatStr.substring(formatStr.indexOf("{") + 1, formatStr.indexOf("}"));
																Method t_method = sjwzxxDto.getClass().getMethod("get" + key.split("\\.")[1].substring(0, 1).toUpperCase() + key.split("\\.")[1].substring(1));
																Object value = t_method.invoke(sjwzxxDto);
																formatStr = formatStr.replace("{"+key+"}","parameter"+tmpVarIndex);
																parameters.put("parameter"+tmpVarIndex,value);
																index ++;
															}
															EvaluableExpression evaluable = new EvaluableExpression(formatStr,functionMap);
															Object evaluate = evaluable.evaluate(parameters);
															tmpVar = evaluate.toString().replaceAll("「","{").replaceAll("」","}");
														} catch (Exception e) {
															log.error("替换公式错误:{}",tmpVar);
															tmpVar = "";
														}
														s_value = tmpVar;
													} else {
														tmpVar = tmpVar.split("\\.")[1];
														String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);
														if (text.indexOf("?") >-1 && text.indexOf(":")>-1){
															if (listindex >= sjwzxxList.size()){
																s_value = "";
															}else {
																s_value = getJudgeValue(text, startTagPos, endTagPos, sjwzxxDto,listindex);
															}
														} else if (text.indexOf("?")>-1) {
															if (listindex >= sjwzxxList.size()){
																s_value = "";
															}else {
																Map<String,Object> map=new HashMap<>();
																map.put("wz_flag","1");
																map.put("wzxxDto",sjwzxxDto);
																s_value = (String)replaceJudge(text.substring(startTagPos+1, endTagPos),map,cell);
															}
														} else {
															if (listindex >= sjwzxxList.size()){
																s_value = "";
															}else {
																// 反射方法 把map的value 插入到数据中
																Method t_method = sjwzxxDto.getClass().getMethod("get" + TotmpVar);
																s_value = (String) t_method.invoke(sjwzxxDto);
															}
														}
													}
													text = text.replace(text.substring(startTagPos, endTagPos+1),s_value);
												}
											}
											run.setText(text,run.getTextPosition()!=-1?run.getTextPosition():0);
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error(e.toString());
				}

			}
		} else {
			int schs=0;//删除行数//物种注释对应文献编号
			for (int x = 0; x < sjwzxxList.size(); x++) {
				XWPFTableRow row = copy(table, tmpRow, table.getRows().size());
				Object sjwzxxDto = sjwzxxList.get(x);
				try {
					List<XWPFTableCell> cells = row.getTableCells();
					// 插入的行会填充与表格第一行相同的列数
					for (int i = 0; i < cellsize; i++) {
						// 由于 list的 size 和 List<XWPFTableCell> cells的 size 相等，所以可以直接用一个索引
						XWPFTableCell cell = cells.get(i);
						XWPFTableCell tmpCell = tmpCells.get(i);
						String tmpText = tmpCell.getText();
						String cellText = cell.getText();
						if (StringUtil.isNotBlank(tmpText)) {
							if (cellText.contains("italic_") || cellText.contains("break_")){
								int index = getXwpfParagraphIndex(tmpText);
								boolean isItalic = false;
								boolean isBreak=false;
								for (int j = 0; j < index; j++) {
									for (int l = 0; l < index; l++) {
										int star = tmpText.indexOf("«");
										int end = tmpText.indexOf("»");
										String s_value="";
										if (star > -1 && end > -1) {
											String content = tmpText.substring(star, end + 1).toString();
											// 判断 是否需要斜体
											if (content.contains("italic_")) {
												isItalic = true;
												content=content.replace("italic_", "");
											}
											if(content.contains("break_")) {
												isBreak = true;
												content=content.replace("break_", "");
											}
											int startTagPos = content.indexOf("«");
											int endTagPos = content.indexOf("»");
											// 寻找到需要替换的变量，如果没有找到则到下一个run
											if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
												String tmpVar = content.substring(startTagPos + 1, endTagPos).trim();
												if (functions.stream().anyMatch(tmpVar::contains)){
													try {
														String formatStr = tmpVar;
														Map<String, Object> parameters = new HashMap<>();
														int tmpVarIndex = 0;
														while (formatStr.contains("{") && formatStr.contains("}")){
															String key = formatStr.substring(formatStr.indexOf("{") + 1, formatStr.indexOf("}"));
															Method t_method = sjwzxxDto.getClass().getMethod("get" + key.split("\\.")[1].substring(0, 1).toUpperCase() + key.split("\\.")[1].substring(1));
															Object value = t_method.invoke(sjwzxxDto);
															formatStr = formatStr.replace("{"+key+"}","parameter"+tmpVarIndex);
															parameters.put("parameter"+tmpVarIndex,value);
															index ++;
														}
														EvaluableExpression evaluable = new EvaluableExpression(formatStr,functionMap);
														Object evaluate = evaluable.evaluate(parameters);
														tmpVar = evaluate.toString().replaceAll("「","{").replaceAll("」","}");
													} catch (Exception e) {
														log.error("替换公式错误:{}",tmpVar);
														tmpVar = "";
													}
													s_value = tmpVar;
												} else {
													s_value = getJudgeValue(content, startTagPos, endTagPos, sjwzxxDto,x);
													if (star>0){
														s_value=tmpText.substring(0, star ) + s_value ;
													}
													if (l==index-1 && end!=tmpText.length() && end<=tmpText.length()){
														s_value = s_value + tmpText.substring(end+1);
													}
												}
												setCellText(tmpCells.get(i), cell, s_value, isItalic,isBreak,true);
											}
//									tmpText = tmpText.replace(tmpText.substring(star, end + 1), "");
											tmpText = tmpText.replace(tmpText.substring(0, end + 1), "");
										}
									}
								}
							}
							else {
								List<XWPFParagraph> paragraphs = cell.getParagraphs();
								if (paragraphs!=null && paragraphs.size()>0){
									for (XWPFParagraph paragraph : paragraphs) {
										List<XWPFRun> runs = paragraph.getRuns();
										if (runs!=null && runs.size()>0){
											for (XWPFRun run : runs) {
												String text = run.getText(run.getTextPosition());
												int index = getXwpfParagraphIndex(text);
												String s_value="";
												for (int y = 0; y < index; y++) {
													int startTagPos = text.indexOf("«");
													int endTagPos = text.indexOf("»");
													// 寻找到需要替换的变量，如果没有找到则到下一个run
													if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
														String tmpVar = text.substring(startTagPos + 1, endTagPos).trim();
														if (functions.stream().anyMatch(tmpVar::contains)){
															try {
																String formatStr = tmpVar;
																Map<String, Object> parameters = new HashMap<>();
																int tmpVarIndex = 0;
																while (formatStr.contains("{") && formatStr.contains("}")){
																	String key = formatStr.substring(formatStr.indexOf("{") + 1, formatStr.indexOf("}"));
																	Method t_method = sjwzxxDto.getClass().getMethod("get" + key.split("\\.")[1].substring(0, 1).toUpperCase() + key.split("\\.")[1].substring(1));
																	Object value = t_method.invoke(sjwzxxDto);
																	formatStr = formatStr.replace("{"+key+"}","parameter"+tmpVarIndex);
																	parameters.put("parameter"+tmpVarIndex,value);
																	index ++;
																}
																EvaluableExpression evaluable = new EvaluableExpression(formatStr,functionMap);
																Object evaluate = evaluable.evaluate(parameters);
																tmpVar = evaluate.toString().replaceAll("「","{").replaceAll("」","}");
															} catch (Exception e) {
																log.error("替换公式错误:{}",tmpVar);
																tmpVar = "";
															}
															s_value = tmpVar;
														} else {
															tmpVar = tmpVar.split("\\.")[1];
															String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);
															if (text.indexOf("?") > -1 && text.indexOf(":") > -1) {
																s_value = getJudgeValue(text, startTagPos, endTagPos, sjwzxxDto, x);
															} else if (text.indexOf("?") > -1) {
																Map<String, Object> map = new HashMap<>();
																map.put("wz_flag", "1");
																map.put("wzxxDto", sjwzxxDto);
																s_value = (String) replaceJudge(text.substring(startTagPos + 1, endTagPos), map,cell);
															} else {
																// 反射方法 把map的value 插入到数据中
																Method t_method = sjwzxxDto.getClass().getMethod("get" + TotmpVar);
																s_value = (String) t_method.invoke(sjwzxxDto);
															}
														}

														text = text.replace(text.substring(startTagPos, endTagPos+1),s_value);
													}
												}
												run.setText(text,run.getTextPosition()!=-1?run.getTextPosition():0);
											}
										}
									}
								}
							}
						}
					}
					List<XWPFTableCell> tableCells = row.getTableCells();
					int num = 0;
					for (int j = 0; j < tableCells.size(); j++) {
						if (StringUtil.isBlank(tableCells.get(j).getText().replace("null", ""))){
							num +=1;
						}
					}
					if (num ==  tableCells.size() && (x<sjwzxxList.size()-1 || schs!=x) ){
						schs=schs+1;
						table.removeRow(table.getRows().size()-1);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error(e.toString());
				}
			}
		}

	}

	/**
	 * 替换判断语句
	 * @param content
	 * @param startTagPos
	 * @param endTagPos
	 * @param obj
	 * @param index 在list序号
	 * @return
	 */
	private String getJudgeValue(String content,int startTagPos,int endTagPos,Object obj,int index){
		String startText = content.substring(0,content.indexOf("«"));
		String endText = content.substring(content.indexOf("»")+1);
		String value = content.substring(startTagPos+1,endTagPos);
		if (value.indexOf("?")>-1 && value.indexOf(":")>-1){
			value = "";
			String judgeText = content.substring(startTagPos+1,content.indexOf("?"));
			String trueValue = content.substring(content.indexOf("?")+1, content.indexOf(":"));
			String falseValue = content.indexOf(":")<content.length()?content.substring(content.indexOf(":")+1,endTagPos):"";

			//从支持一个条件更改为支持多条件联合，暂不支持不同判断条件的联合 2025/08/18
			List<String> and_condition = new ArrayList<>();
			List<String> or_condition = new ArrayList<>();
			//先判断是否为多个条件同时起效，组成条件判断List
			String[] and_sec = judgeText.split("&&");
			if(and_sec.length>1){
				and_condition.addAll(Arrays.asList(and_sec));
				value = getConditionResult(and_condition,obj,1,trueValue,falseValue);
			}else{
				String[] or_sec = judgeText.split("\\|\\|");
				if(or_sec.length>1){
					or_condition.addAll(Arrays.asList(and_sec));
				}else{
					or_condition.add(judgeText);
				}
				value = getConditionResult(or_condition,obj,2,trueValue,falseValue);
			}

			if(StringUtil.isNotBlank(value) && value.indexOf("{") > -1){
				String pre_const = value.substring(0,value.indexOf("{"));
				String s_var = value.substring(value.indexOf("{") + 1, value.indexOf("}"));
				String after_const = value.substring(value.indexOf("}") + 1);
				
				String t_valueString = null;
				if(obj instanceof Map){
					t_valueString = (String) ((Map<String,Object>) obj).get(s_var.trim().split("\\.")[1]);
				} else {
					try {
						String tmpVar = s_var.trim().split("\\.")[1];
						String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);
						if ("indexNum".equals(tmpVar) || "indexNumCircle".equals(tmpVar) || "indexNumRoman".equals(tmpVar) || "indexNumChinese".equals(tmpVar)){
							String t_value = String.valueOf(index+1);
							t_valueString = replaceIndexNum(t_value,tmpVar);
						}else {
							try {
								Method method = obj.getClass().getMethod("get" + TotmpVar);
								t_valueString = (String) method.invoke(obj);
							} catch (Exception e) {
								log.error("对象中没有get" + TotmpVar + "方法");
							}
						}
					} catch (Exception e) {
						log.error("对象中没有get" + value.substring(0, 1).toUpperCase() + value.substring(1) + "方法");
					}
				}
				value = pre_const + (t_valueString==null?"":t_valueString) + after_const;
			}
		}else{
			try {
				if(value.indexOf("\\.") > -1) {
					String t_valueString = null;
					String tmpVar = value.trim().split("\\.")[1];
					String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);
					if ("indexNum".equals(tmpVar) || "indexNumCircle".equals(tmpVar) || "indexNumRoman".equals(tmpVar) || "indexNumChinese".equals(tmpVar)) {
						String t_value = String.valueOf(index + 1);
						t_valueString = replaceIndexNum(t_value, tmpVar);
					} else {
						try {
							Method method = obj.getClass().getMethod("get" + TotmpVar);
							t_valueString = (String) method.invoke(obj);
						} catch (Exception e) {
							log.error("对象list中没有get" + TotmpVar + "方法");
						}
					}
					value = (t_valueString == null ? "" : t_valueString);
				}else{
					String t_valueString = null;
					try {
						Method method = obj.getClass().getMethod("get" + value);
						t_valueString = (String) method.invoke(obj);
					} catch (Exception e) {
						log.error("对象2中没有get" + value + "方法");
					}
					value = (t_valueString == null ? "" : t_valueString);
				}
			} catch (Exception e) {
				log.error("对象中没有get" + value.substring(0, 1).toUpperCase() + value.substring(1) + "方法");
			}
		}
		return startText+value+endText;
	}
	/**
	 * 不需要添加图片的表格
	 * @param table
	 * @param vfsList
	 */
	private void addNewRowsVrf(XWPFTable table,List<WechatVirulenceFactorStatModel> vfsList) {

		XWPFTableRow tmpRow = table.getRows().get(table.getRows().size() - 1);
		List<XWPFTableCell> tmpCells = tmpRow.getTableCells();
		String tmpCellText = tmpCells.get(0).getText();
		String style = "";
		if (tmpCellText.indexOf("[") !=-1 && tmpCellText.indexOf("]") != -1){
			//[seg:2-1,align:center]
			style = tmpCellText.substring(tmpCellText.indexOf("[")+1,tmpCellText.indexOf("]"));
		}
		//将style转换成List
		String[] styles = style.split(",");
		String seg = "";
		for (String s : styles) {
			if (s.contains(":")){
				String[] split = s.split(":");
				if ("seg".equals(split[0])){
					seg = split[1];
				}
			}
		}
		String columns = "";
		if (StringUtil.isNotBlank(seg)){
			String[] split = seg.split("-");
			columns = split[0];
		}
		int cellsize = tmpCells.size();
		if (StringUtil.isNotBlank(columns)){
			int copySize = vfsList.size()/Integer.parseInt(columns) + (vfsList.size()%Integer.parseInt(columns)>0?1:0);
			for (int i = 0; i < copySize; i++) {
				XWPFTableRow row = copy(table, tmpRow, table.getRows().size());
				try {
					List<XWPFTableCell> cells = row.getTableCells();
					// 插入的行会填充与表格第一行相同的列数
					for (int j = 0; j < cellsize; j++) {
						// 由于 list的 size 和 List<XWPFTableCell> cells的 size 相等，所以可以直接用一个索引
						XWPFTableCell cell = cells.get(j);
						XWPFTableCell tmpCell = tmpCells.get(j);
						String tmpText = tmpCell.getText();
						String cellText = cell.getText();
						if (StringUtil.isNotBlank(tmpText)) {
							List<XWPFParagraph> paragraphs = cell.getParagraphs();
							if (paragraphs!=null && paragraphs.size()>0){
								for (XWPFParagraph paragraph : paragraphs) {
									List<XWPFRun> runs = paragraph.getRuns();
									if (runs!=null && runs.size()>0){
										for (XWPFRun run : runs) {
											String text = run.getText(run.getTextPosition()!=-1?run.getTextPosition():0);
											int index = getXwpfParagraphIndex(text);
											String s_value="";
											for (int y = 0; y < index; y++) {
												int startTagPos = text.indexOf("«");
												int endTagPos = text.indexOf("»");
												// 寻找到需要替换的变量，如果没有找到则到下一个run
												if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
													String tmpVar = text.substring(startTagPos + 1, endTagPos).trim()
															.split("\\.")[1];
													//[seg:2-1,align:center]
													String cellstyle = cellText.substring(cellText.indexOf("[")+1,cellText.indexOf("]"));
													//将content转换成List
													String[] cellstyles = cellstyle.split(",");
													String cellseg = "";
													for (String s : cellstyles) {
														if (s.contains(":")){
															String[] split = s.split(":");
															if ("seg".equals(split[0])){
																cellseg = split[1];
															}
														}
													}
													String cellindex = "";
													if (StringUtil.isNotBlank(cellseg)){
														String[] split = cellseg.split("-");
														cellindex = split[1];
													}
													int listindex = i*2+Integer.parseInt(cellindex)-1;
													WechatVirulenceFactorStatModel vfsModel = vfsList.get(listindex);
													tmpVar = tmpVar.replace(cellText.substring(cellText.indexOf("["),cellText.indexOf("]")+1),"");
													String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);

													if (text.indexOf("?") >-1 && text.indexOf(":")>-1){
														if (listindex >= vfsList.size()){
															s_value = "";
														}else {
															s_value = getJudgeValue(text, startTagPos, endTagPos, vfsModel,listindex);
														}
													} else if (text.indexOf("?")>-1) {
														if (listindex >= vfsList.size()){
															s_value = "";
														}else {
															Map<String,Object> map=new HashMap<>();
															map.put("wz_flag","1");
															map.put("wzxxDto",vfsModel);
															s_value = (String)replaceJudge(text.substring(startTagPos+1, endTagPos),map,cell);
														}
													}else {
														if (listindex >= vfsList.size()){
															s_value = "";
														}else {
															// 反射方法 把map的value 插入到数据中
															Method t_method = vfsModel.getClass().getMethod("get" + TotmpVar);
															s_value = (String) t_method.invoke(vfsModel);
														}
													}
													text = text.replace(text.substring(startTagPos, endTagPos+1),s_value);
												}
											}
											run.setText(text,run.getTextPosition()!=-1?run.getTextPosition():0);
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error(e.toString());
				}

			}
		} else {
			for (int x = 0; x < vfsList.size(); x++) {
				XWPFTableRow row = copy(table, tmpRow, table.getRows().size());
				WechatVirulenceFactorStatModel vfsModel = vfsList.get(x);
				try {
					List<XWPFTableCell> cells = row.getTableCells();
					// 插入的行会填充与表格第一行相同的列数
					for (int i = 0; i < cellsize; i++) {
						// 由于 list的 size 和 List<XWPFTableCell> cells的 size 相等，所以可以直接用一个索引
						XWPFTableCell cell = cells.get(i);
						XWPFTableCell tmpCell = tmpCells.get(i);
						String tmpText = tmpCell.getText();
//						String cellText = cell.getText();
						if (StringUtil.isNotBlank(tmpText)) {
							List<XWPFParagraph> paragraphs = cell.getParagraphs();
							if (paragraphs!=null && paragraphs.size()>0){
								for (XWPFParagraph paragraph : paragraphs) {
									List<XWPFRun> runs = paragraph.getRuns();
									if (runs!=null && runs.size()>0){
										for (XWPFRun run : runs) {
											String text = run.getText(run.getTextPosition());
											int index = getXwpfParagraphIndex(text);
											String s_value="";
											for (int y = 0; y < index; y++) {
												int startTagPos = text.indexOf("«");
												int endTagPos = text.indexOf("»");
												// 寻找到需要替换的变量，如果没有找到则到下一个run
												if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
													String tmpVar = text.substring(startTagPos + 1, endTagPos).trim()
															.split("\\.")[1];
													String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);
													if (text.indexOf("?") >-1 && text.indexOf(":")>-1){
														s_value = getJudgeValue(text, startTagPos, endTagPos, vfsModel,x);
													} else if (text.indexOf("?")>-1) {
														Map<String,Object> map=new HashMap<>();
														map.put("wz_flag","1");
														map.put("wzxxDto",vfsModel);
														s_value = (String)replaceJudge(text.substring(startTagPos+1, endTagPos),map,cell);
													} else {
														// 反射方法 把map的value 插入到数据中
														Method t_method = vfsModel.getClass().getMethod("get" + TotmpVar);
														s_value = (String) t_method.invoke(vfsModel);
													}
													text = text.replace(text.substring(startTagPos, endTagPos+1),s_value);
												}
											}
											run.setText(text,run.getTextPosition()!=-1?run.getTextPosition():0);
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error(e.toString());
				}
			}
		}

	}
	/**
	 * 不需要添加图片的表格
	 * @param table
	 * @param mapList
	 */
	private void addNewRowsMapList(XWPFTable table,List<Map<String, Object>> mapList) {

		XWPFTableRow tmpRow = table.getRows().get(table.getRows().size() - 1);
		List<XWPFTableCell> tmpCells = tmpRow.getTableCells();
		String tmpCellText = tmpCells.get(0).getText();
		String style = "";
		if (tmpCellText.indexOf("[") !=-1 && tmpCellText.indexOf("]") != -1){
			//[seg:2-1,align:center]
			style = tmpCellText.substring(tmpCellText.indexOf("[")+1,tmpCellText.indexOf("]"));
		}
		//将style转换成List
		String[] styles = style.split(",");
		String seg = "";
		for (String s : styles) {
			if (s.contains(":")){
				String[] split = s.split(":");
				if ("seg".equals(split[0])){
					seg = split[1];
				}
			}
		}
		String columns = "";
		if (StringUtil.isNotBlank(seg)){
			String[] split = seg.split("-");
			columns = split[0];
		}
		int cellsize = tmpCells.size();
		if (StringUtil.isNotBlank(columns)){
			int copySize = mapList.size()/Integer.parseInt(columns) + (mapList.size()%Integer.parseInt(columns)>0?1:0);
			for (int i = 0; i < copySize; i++) {
				XWPFTableRow row = copy(table, tmpRow, table.getRows().size());
				try {
					List<XWPFTableCell> cells = row.getTableCells();
					// 插入的行会填充与表格第一行相同的列数
					for (int j = 0; j < cellsize; j++) {
						// 由于 list的 size 和 List<XWPFTableCell> cells的 size 相等，所以可以直接用一个索引
						XWPFTableCell cell = cells.get(j);
						XWPFTableCell tmpCell = tmpCells.get(j);
						String tmpText = tmpCell.getText();
						String cellText = cell.getText();
						if (StringUtil.isNotBlank(tmpText)) {
							List<XWPFParagraph> paragraphs = cell.getParagraphs();
							if (paragraphs!=null && paragraphs.size()>0){
								for (XWPFParagraph paragraph : paragraphs) {
									List<XWPFRun> runs = paragraph.getRuns();
									if (runs!=null && runs.size()>0){
										for (XWPFRun run : runs) {
											String text = run.getText(run.getTextPosition()!=-1?run.getTextPosition():0);
											int index = getXwpfParagraphIndex(text);
											String s_value="";
											for (int y = 0; y < index; y++) {
												int startTagPos = text.indexOf("«");
												int endTagPos = text.indexOf("»");
												// 寻找到需要替换的变量，如果没有找到则到下一个run
												if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
													String tmpVar = text.substring(startTagPos + 1, endTagPos).trim()
															.split("\\.")[1];
													//[seg:2-1,align:center]
													String cellstyle = cellText.substring(cellText.indexOf("[")+1,cellText.indexOf("]"));
													//将content转换成List
													String[] cellstyles = cellstyle.split(",");
													String cellseg = "";
													for (String s : cellstyles) {
														if (s.contains(":")){
															String[] split = s.split(":");
															if ("seg".equals(split[0])){
																cellseg = split[1];
															}
														}
													}
													String cellindex = "";
													if (StringUtil.isNotBlank(cellseg)){
														String[] split = cellseg.split("-");
														cellindex = split[1];
													}
													int listindex = i*2+Integer.parseInt(cellindex)-1;
													Map<String, Object> mapModel = mapList.get(listindex);
													tmpVar = tmpVar.replace(cellText.substring(cellText.indexOf("["),cellText.indexOf("]")+1),"");

													if (text.indexOf("?") >-1 && text.indexOf(":")>-1){
														if (listindex >= mapList.size()){
															s_value = "";
														}else {
															s_value = getJudgeValue(text, startTagPos, endTagPos, mapModel,listindex);
														}
													} else if (text.indexOf("?")>-1) {
														if (listindex >= mapList.size()){
															s_value = "";
														}else {
															s_value = (String)replaceJudge(text.substring(startTagPos+1, endTagPos),mapModel,cell);
														}
													}else {
														if (listindex >= mapList.size()){
															s_value = "";
														}else {
															s_value = (String) mapModel.get(tmpVar);
														}
													}
													text = text.replace(text.substring(startTagPos, endTagPos+1),s_value);
												}
											}
											run.setText(text,run.getTextPosition()!=-1?run.getTextPosition():0);
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error(e.toString());
				}

			}
		} else {
			for (int x = 0; x < mapList.size(); x++) {
				XWPFTableRow row = copy(table, tmpRow, table.getRows().size());
				Map<String, Object> mapModel = mapList.get(x);
				try {
					List<XWPFTableCell> cells = row.getTableCells();
					// 插入的行会填充与表格第一行相同的列数
					for (int i = 0; i < cellsize; i++) {
						// 由于 list的 size 和 List<XWPFTableCell> cells的 size 相等，所以可以直接用一个索引
						XWPFTableCell cell = cells.get(i);
						XWPFTableCell tmpCell = tmpCells.get(i);
						String tmpText = tmpCell.getText();
//						String cellText = cell.getText();
						if (StringUtil.isNotBlank(tmpText)) {
							List<XWPFParagraph> paragraphs = cell.getParagraphs();
							if (paragraphs!=null && paragraphs.size()>0){
								for (XWPFParagraph paragraph : paragraphs) {
									List<XWPFRun> runs = paragraph.getRuns();
									if (runs!=null && runs.size()>0){
										for (XWPFRun run : runs) {
											String text = run.getText(run.getTextPosition());
											int index = getXwpfParagraphIndex(text);
											String s_value="";
											for (int y = 0; y < index; y++) {
												int startTagPos = text.indexOf("«");
												int endTagPos = text.indexOf("»");
												// 寻找到需要替换的变量，如果没有找到则到下一个run
												if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
													String tmpVar = text.substring(startTagPos + 1, endTagPos).trim()
															.split("\\.")[1];
													if (text.indexOf("?") >-1 && text.indexOf(":")>-1){
														s_value = getJudgeValue(text, startTagPos, endTagPos, mapModel,x);
													} else if (text.indexOf("?")>-1) {
														s_value = (String)replaceJudge(text.substring(startTagPos+1, endTagPos),mapModel,cell);
													} else {
														s_value = (String) mapModel.get(tmpVar);
													}
													text = text.replace(text.substring(startTagPos, endTagPos+1),s_value);
												}
											}
											run.setText(text);
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.error(e.toString());
				}
			}
		}

	}


	/**
	 * 需要添加图片的表格
	 * @param table
	 * @param sjwzxxList
	 */
	private void addNewRows_image(XWPFTable table,List<SjwzxxDto> sjwzxxList,Map<String,Object> map) {
		int rownum =  table.getRows().size();
		if(rownum<4) {
			int rowCnt = table.getRows().size();
			addNewRows(table,sjwzxxList,map);
			// 删除模版行
 			table.removeRow(rowCnt - 1);
		}else {
			XWPFTableRow tmpRow = table.getRows().get(2);
			List<XWPFTableCell> tmpCells = tmpRow.getTableCells();
			String tmpCellText = tmpCells.get(0).getText();
			String style = "";
			if (tmpCellText.indexOf("[") !=-1 && tmpCellText.indexOf("]") != -1){
				//[seg:2-1,align:center]
				style = tmpCellText.substring(tmpCellText.indexOf("[")+1,tmpCellText.indexOf("]"));
			}
			//将content转换成List
			String[] styles = style.split(",");
			String seg = "";
			for (String s : styles) {
				if (s.contains(":")){
					String[] split = s.split(":");
					if ("seg".equals(split[0])){
						seg = split[1];
					}
				}
			}
			String columns = "";
			if (StringUtil.isNotBlank(seg)){
				String[] split = seg.split("-");
				columns = split[0];
			}

//			int cellsize = tmpCells.size();
			if (StringUtil.isNotBlank(columns)){
				int copySize = sjwzxxList.size()/Integer.parseInt(columns) + (sjwzxxList.size()%Integer.parseInt(columns)>0?1:0);
				for (int j = 0; j < copySize; j++) {
					//复制第一行
					XWPFTableRow tmpRow1 = copy(table, table.getRows().get(2), table.getRows().size());
					replaceMultiRowsTextSeg(tmpRow1,sjwzxxList,j);
					boolean ifcopy=false;
					for (int k=tmpRow1.getTableCells().size()-1;k>=0;k--){
						if(StringUtil.isNotBlank(tmpRow1.getTableCells().get(k).getText())){
							ifcopy=true;
						}
					}
					if(ifcopy){//只有物种内容替换有值的时候才复制第四行模板行
						copy(table, table.getRows().get(3), table.getRows().size());
					}

					//复制第三行,判断table是否超过3行，如果超过，复制第五行
					XWPFTableRow tmpRow3 = copy(table, table.getRows().get(4), table.getRows().size());
					replaceMultiRowsTextSeg(tmpRow3,sjwzxxList,j);


					for (int i = 0; i < tmpRow1.getTableCells().size(); i++) {
						XWPFTableCell cell=tmpRow1.getCell(i);
						CTTc ctTc = cell.getCTTc();
						CTTcPr tcPr = ctTc.getTcPr();
						CTTcBorders borders = tcPr.addNewTcBorders();
						CTBorder lBorder=borders.addNewTop();
						lBorder.setVal(STBorder.Enum.forString("single"));
						lBorder.setSz(new BigInteger("25"));
					}
				}
			}else {
				for (int x = 0; x < sjwzxxList.size(); x++) {
					//复制第一行
					XWPFTableRow tmpRow1 = copy(table, table.getRows().get(2), table.getRows().size());
					replaceMultiRowsText(tmpRow1,sjwzxxList.get(x),x);
					boolean ifcopy=false;
					for (int j=tmpRow1.getTableCells().size()-1;j>=0;j--){
						if(StringUtil.isNotBlank(tmpRow1.getTableCells().get(j).getText())){
							ifcopy=true;
						}
					}
					if(ifcopy){//只有物种内容替换有值的时候才复制第四行模板行
						copy(table, table.getRows().get(3), table.getRows().size());
					}


					//复制第三行,判断table是否超过3行，如果超过，复制第五行
					XWPFTableRow tmpRow3 = copy(table, table.getRows().get(4), table.getRows().size());
					replaceMultiRowsText(tmpRow3,sjwzxxList.get(x),x);


					for (int i = 0; i < tmpRow1.getTableCells().size(); i++) {
						XWPFTableCell cell=tmpRow1.getCell(i);
						CTTc ctTc = cell.getCTTc();
						CTTcPr tcPr = ctTc.getTcPr();
						CTTcBorders borders = tcPr.addNewTcBorders();
						CTBorder lBorder=borders.addNewTop();
						lBorder.setVal(STBorder.Enum.forString("single"));
						lBorder.setSz(new BigInteger("25"));
					}
				}
			}

			table.removeRow(4);
			table.removeRow(3);
			table.removeRow(2);

			//删除所有内容为空的行
			for(int i=table.getRows().size()-1;i>=0;i--){
				boolean sfscrow=true;
				XWPFTableRow row = table.getRows().get(i);
				for (int j=row.getTableCells().size()-1;j>=0;j--){
					if(StringUtil.isNotBlank(row.getTableCells().get(j).getText())){
						sfscrow=false;
					}
				}
				if(sfscrow && table.getRows().size()>3){//保留前三行，为了检出阴性时显示未检出
					table.removeRow(i);
				}
			}
		}

	}
	
	/**
	 * 复制一行
	 * @param table
	 * @param sourceRow
	 * @param rowIndex
	 * @return
	 */
	public XWPFTableRow copy(XWPFTable table,XWPFTableRow sourceRow,int rowIndex){
	    //在表格指定位置新增一行
		XWPFTableRow targetRow = table.insertNewTableRow(rowIndex);
		//复制行属性
		targetRow.getCtRow().setTrPr(sourceRow.getCtRow().getTrPr());
		List<XWPFTableCell> cellList = sourceRow.getTableCells();
		if (null == cellList) {
		    return targetRow;
		}
		//复制列及其属性和内容
		XWPFTableCell targetCell = null;
		for (XWPFTableCell sourceCell : cellList) {
		    targetCell = targetRow.addNewTableCell();
		    //列属性
		    targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
		    //段落属性
		    if(sourceCell.getParagraphs()!=null&&sourceCell.getParagraphs().size()>0){
				for (int i = 0; i < sourceCell.getParagraphs().size(); i++) {
					if (targetCell.getParagraphs().size()<sourceCell.getParagraphs().size()){
						targetCell.addParagraph();
					}
					targetCell.getParagraphs().get(i).getCTP().setPPr(sourceCell.getParagraphs().get(i).getCTP().getPPr());
					if(sourceCell.getParagraphs().get(i).getRuns()!=null&&sourceCell.getParagraphs().get(i).getRuns().size()>0){
						for (int j = 0; j < sourceCell.getParagraphs().get(i).getRuns().size(); j++) {
							XWPFRun cellR = targetCell.getParagraphs().get(i).createRun();
							cellR.setText(sourceCell.getParagraphs().get(i).getRuns().get(j).text());
							cellR.setBold(sourceCell.getParagraphs().get(i).getRuns().get(j).isBold());
							cellR.getCTR().setRPr(sourceCell.getParagraphs().get(i).getRuns().get(j).getCTR().getRPr());
							if ( null != sourceCell.getParagraphs().get(i).getRuns() && sourceCell.getParagraphs().get(i).getRuns().size()>0){
								if (StringUtil.isNotBlank(sourceCell.getParagraphs().get(i).getRuns().get(j).getFontFamily())){
									cellR.setFontFamily(sourceCell.getParagraphs().get(i).getRuns().get(j).getFontFamily());
								}
							}
						}
					}else{
						targetCell.setText(sourceCell.getText());
					}
				}
	        }else{
	        	targetCell.setText(sourceCell.getText());
	        }
	    }
		return targetRow;
	}
	
	/**
	 * 多行数据替换（包含图片）
	 * @param tmpRow
	 * @param sjwzxxDto
	 */
	private void replaceMultiRowsText(XWPFTableRow tmpRow,SjwzxxDto sjwzxxDto,int wzIndex) {
		// 创建一个新的行 createRow
		for (int i = 0; i < tmpRow.getTableCells().size(); i++) {
			XWPFTableCell cell = tmpRow.getTableCells().get(i);
			String tmpText = cell.getText();
			if (StringUtil.isNotBlank(tmpText)) {
				try {
					if (tmpText.contains("italic_") || tmpText.contains("break_")){
						int index = getXwpfParagraphIndex(tmpText);
						boolean isItalic = false;
						boolean isBreak=false;
						for (int j = 0; j < index; j++) {
							for (int l = 0; l < index; l++) {
								int star = tmpText.indexOf("«");
								int end = tmpText.indexOf("»");
								if (star > -1 && end > -1) {
									String content = tmpText.substring(star, end + 1).toString();
									// 判断 是否需要斜体
									if (content.contains("italic_")) {
										isItalic = true;
										content=content.replace("italic_", "");
									}
									if(content.contains("break_")) {
										isBreak = true;
										content=content.replace("break_", "");
									}
									int startTagPos = content.indexOf("«");
									int endTagPos = content.indexOf("»");
									// 寻找到需要替换的变量，如果没有找到则到下一个run
									if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
										String tmpVar = content.substring(startTagPos + 1, endTagPos).trim()
												.split("\\.")[1];
										if("percentile".equals(tmpVar)) {
											if(StringUtil.isNotBlank(sjwzxxDto.getJzryzswz())) {
												File file = new File(sjwzxxDto.getPercentile_path());
												if (!file.exists()) {
													break;
												}
												for (int k = cell.getParagraphs().size()-1; k >-1; k--) {
													cell.removeParagraph(k);
												}
												XWPFParagraph paragraph=cell.addParagraph();
												XWPFRun run=paragraph.createRun();
												List<PictureFloat> list= new ArrayList<>();
												PictureFloat imageFloat1=new PictureFloat(sjwzxxDto.getPercentile_path(), 250, 50, 0, 0);
												list.add(imageFloat1);
												int left=(int) (217 * Float.parseFloat(sjwzxxDto.getJzryzswz())+10);
												PictureFloat imageFloat2=new PictureFloat(sjwzxxDto.getCursor_path(), 15, 15, left, 33);
												list.add(imageFloat2);
												overlayPictures(run,list);
											}
										}else {
											String s_value = getJudgeValue(content, startTagPos, endTagPos, sjwzxxDto,wzIndex);

											setCellText(cell, cell, s_value, isItalic,isBreak,false);
										}
									}
									tmpText = tmpText.replace(tmpText.substring(star, end + 1), "");
								}
							}
						}
					}else {
						List<XWPFParagraph> paragraphs = cell.getParagraphs();
						if (paragraphs!=null && paragraphs.size()>0){
							for (XWPFParagraph paragraph : paragraphs) {
								List<XWPFRun> runs = paragraph.getRuns();
								if (runs!=null && runs.size()>0){
									for (XWPFRun run : runs) {
										String text = run.getText(run.getTextPosition());
										int index = getXwpfParagraphIndex(text);
										String s_value="";
										for (int y = 0; y < index; y++) {
											int startTagPos = text.indexOf("«");
											int endTagPos = text.indexOf("»");
											// 寻找到需要替换的变量，如果没有找到则到下一个run
											if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
												String tmpVar = text.substring(startTagPos + 1, endTagPos).trim()
														.split("\\.")[1];
												if("percentile".equals(tmpVar)) {
													if(StringUtil.isNotBlank(sjwzxxDto.getJzryzswz())) {
														File file = new File(sjwzxxDto.getPercentile_path());
														if (!file.exists()) {
															break;
														}
														for (int k = cell.getParagraphs().size()-1; k >-1; k--) {
															cell.removeParagraph(k);
														}
														XWPFParagraph p=cell.addParagraph();
														XWPFRun r=p.createRun();
														List<PictureFloat> list= new ArrayList<>();
														PictureFloat imageFloat1=new PictureFloat(sjwzxxDto.getPercentile_path(), 250, 50, 0, 0);
														list.add(imageFloat1);
														int left=(int) (217 * Float.parseFloat(sjwzxxDto.getJzryzswz())+10);
														PictureFloat imageFloat2=new PictureFloat(sjwzxxDto.getCursor_path(), 15, 15, left, 33);
														list.add(imageFloat2);
														overlayPictures(r,list);
													}
												}else {
													Map<String,Object> map=new HashMap<>();
													String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);
													if (text.indexOf("?") >-1 && text.indexOf(":")>-1){
														s_value = getJudgeValue(text, startTagPos, endTagPos, sjwzxxDto,wzIndex);
													} else if (text.indexOf("?")>-1) {
														map.put("wz_flag","1");
														map.put("wzxxDto",sjwzxxDto);
														s_value = (String)replaceJudge(text.substring(startTagPos+1, endTagPos),map,cell);
													} else {
														// 反射方法 把map的value 插入到数据中
														Method t_method = sjwzxxDto.getClass().getMethod("get" + TotmpVar);
														s_value = (String) t_method.invoke(sjwzxxDto);
													}
													text = text.replace(text.substring(startTagPos, endTagPos + 1), s_value);
													run.setText(text,0);
												}
											}
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 多行数据替换（包含图片）(一行多列)
	 * @param tmpRow
	 * @param sjwzxxList
	 */
	private void replaceMultiRowsTextSeg(XWPFTableRow tmpRow,List<SjwzxxDto> sjwzxxList,int copySizeIndex) {
		// 创建一个新的行 createRow
		for (int i = 0; i < tmpRow.getTableCells().size(); i++) {
			XWPFTableCell cell = tmpRow.getTableCells().get(i);
			String tmpText = cell.getText();
			if (StringUtil.isNotBlank(tmpText)) {
				try {
					if (tmpText.contains("italic_") || tmpText.contains("break_")){
						int index = getXwpfParagraphIndex(tmpText);
						boolean isItalic = false;
						boolean isBreak=false;
						for (int j = 0; j < index; j++) {
							for (int l = 0; l < index; l++) {
								int star = tmpText.indexOf("«");
								int end = tmpText.indexOf("»");
								if (star > -1 && end > -1) {
									String content = tmpText.substring(star, end + 1).toString();
									// 判断 是否需要斜体
									if (content.contains("italic_")) {
										isItalic = true;
										content=content.replace("italic_", "");
									}
									if(content.contains("break_")) {
										isBreak = true;
										content=content.replace("break_", "");
									}
									int startTagPos = content.indexOf("«");
									int endTagPos = content.indexOf("»");
									// 寻找到需要替换的变量，如果没有找到则到下一个run
									if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
										String s_value = "";
										String tmpVar = content.substring(startTagPos + 1, endTagPos).trim()
												.split("\\.")[1];
										//[seg:2-1,align:center]
										String runstyle = content.substring(content.indexOf("[")+1,content.indexOf("]"));
										//将content转换成List
										String[] runstyles = runstyle.split(",");
										String runseg = "";
										for (String s : runstyles) {
											if (s.contains(":")){
												String[] split = s.split(":");
												if ("seg".equals(split[0])){
													runseg = split[1];
												}
											}
										}
										String runindex = "";
										if (StringUtil.isNotBlank(runseg)){
											String[] split = runseg.split("-");
											runindex = split[1];
										}
										int listindex = copySizeIndex*2+Integer.parseInt(runindex)-1;
										if (listindex>=sjwzxxList.size()){
											setCellText(cell, cell, s_value, isItalic,isBreak,false);
										}else {
											SjwzxxDto sjwzxxDto = sjwzxxList.get(listindex);
											if("percentile".equals(tmpVar)) {
												if(StringUtil.isNotBlank(sjwzxxDto.getJzryzswz())) {
													File file = new File(sjwzxxDto.getPercentile_path());
													if (!file.exists()) {
														break;
													}
													for (int k = cell.getParagraphs().size()-1; k >-1; k--) {
														cell.removeParagraph(k);
													}
													XWPFParagraph paragraph=cell.addParagraph();
													XWPFRun run=paragraph.createRun();
													List<PictureFloat> list= new ArrayList<>();
													PictureFloat imageFloat1=new PictureFloat(sjwzxxDto.getPercentile_path(), 250, 50, 0, 0);
													list.add(imageFloat1);
													int left=(int) (217 * Float.parseFloat(sjwzxxDto.getJzryzswz())+10);
													PictureFloat imageFloat2=new PictureFloat(sjwzxxDto.getCursor_path(), 15, 15, left, 33);
													list.add(imageFloat2);
													overlayPictures(run,list);
												}
											}else {
												s_value = getJudgeValue(content, startTagPos, endTagPos, sjwzxxDto,listindex);

												setCellText(cell, cell, s_value, isItalic,isBreak,false);
											}
										}
									}
									tmpText = tmpText.replace(tmpText.substring(star, end + 1), "");
								}
							}
						}
					}else {
						List<XWPFParagraph> paragraphs = cell.getParagraphs();
						if (paragraphs!=null && paragraphs.size()>0){
							for (XWPFParagraph paragraph : paragraphs) {
								List<XWPFRun> runs = paragraph.getRuns();
								if (runs!=null && runs.size()>0){
									for (XWPFRun run : runs) {
										String text = run.getText(run.getTextPosition()!=-1?run.getTextPosition():0);
										int index = getXwpfParagraphIndex(text);
										String s_value="";
										for (int y = 0; y < index; y++) {
											int startTagPos = text.indexOf("«");
											int endTagPos = text.indexOf("»");
											// 寻找到需要替换的变量，如果没有找到则到下一个run
											if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
												String tmpVar = text.substring(startTagPos + 1, endTagPos).trim()
														.split("\\.")[1];
												//[seg:2-1,align:center]
												String runstyle = text.substring(text.indexOf("[")+1,text.indexOf("]"));
												//将content转换成List
												String[] runstyles = runstyle.split(",");
												String runseg = "";
												for (String s : runstyles) {
													if (s.contains(":")){
														String[] split = s.split(":");
														if ("seg".equals(split[0])){
															runseg = split[1];
														}
													}
												}
												String runindex = "";
												if (StringUtil.isNotBlank(runseg)){
													String[] split = runseg.split("-");
													runindex = split[1];
												}
												int listindex = copySizeIndex*2+Integer.parseInt(runindex)-1;
												if (listindex>=sjwzxxList.size()){
													text = text.replace(text.substring(startTagPos, endTagPos + 1), s_value);
													run.setText(text,0);
												}else {
													SjwzxxDto sjwzxxDto = sjwzxxList.get(listindex);
													tmpVar = tmpVar.replace(text.substring(text.indexOf("["),text.indexOf("]")+1),"");
													if("percentile".equals(tmpVar)) {
														if(StringUtil.isNotBlank(sjwzxxDto.getJzryzswz())) {
															File file = new File(sjwzxxDto.getPercentile_path());
															if (!file.exists()) {
																break;
															}
															for (int k = cell.getParagraphs().size()-1; k >-1; k--) {
																cell.removeParagraph(k);
															}
															XWPFParagraph p=cell.addParagraph();
															XWPFRun r=p.createRun();
															List<PictureFloat> list= new ArrayList<>();
															PictureFloat imageFloat1=new PictureFloat(sjwzxxDto.getPercentile_path(), 250, 50, 0, 0);
															list.add(imageFloat1);
															int left=(int) (217 * Float.parseFloat(sjwzxxDto.getJzryzswz())+10);
															PictureFloat imageFloat2=new PictureFloat(sjwzxxDto.getCursor_path(), 15, 15, left, 33);
															list.add(imageFloat2);
															overlayPictures(r,list);
														}
													}else {
														String TotmpVar = tmpVar.substring(0, 1).toUpperCase() + tmpVar.substring(1);
														if (text.indexOf("?") >-1 && text.indexOf(":")>-1){
															s_value = getJudgeValue(text, startTagPos, endTagPos, sjwzxxDto,listindex);
														} else if (text.indexOf("?")>-1) {
															Map<String,Object> map=new HashMap<>();
															map.put("wz_flag","1");
															map.put("wzxxDto",sjwzxxDto);
															s_value = (String)replaceJudge(text.substring(startTagPos+1, endTagPos),map,cell);
														} else {
															// 反射方法 把map的value 插入到数据中
															Method t_method = sjwzxxDto.getClass().getMethod("get" + TotmpVar);
															s_value = (String) t_method.invoke(sjwzxxDto);
														}
														text = text.replace(text.substring(startTagPos, endTagPos + 1), s_value);
														run.setText(text,0);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 替换图片（包括图片,«»中包含Pic即可）
	 * 若其中包含leftBreak或者allBreak,则在该图片前换行
	 * 若其中包含rightBreak或者allBreak,则在该图片后换行
	 * 若其中包含qrCode，则生成防伪码
	 */
	private void replacePic(XWPFParagraph paragraph,Map<String,Object> map) throws IOException{
		List<XWPFRun> runs = paragraph.getRuns();
		for (int q = 0; runs != null && q < runs.size(); q++) {
			String oneparaString = runs.get(q).getText(runs.get(q).getTextPosition());
			if (StringUtil.isBlank(oneparaString)) {
                continue;
            }
			int startTagPos = oneparaString.indexOf("«");
			int endTagPos = oneparaString.indexOf("»");
			if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
				String varString = oneparaString.substring(startTagPos + 1, endTagPos).trim();
				if (StringUtil.isNotBlank(varString)) {
					//«gzlxdz_FloatPic_size:120_left:300_top:-40»
					if (varString.contains("_FloatPic")){
						oneparaString = oneparaString.replace("«" + varString + "»", "");
						List<PictureFloat> list=new ArrayList<>();
						String imageObj = varString.split("_")[0];
						Object imaget = map.get(imageObj);
						if (imaget!=null) {
							String imagetFile = imaget.toString();
							String[] varStrings = varString.split("_");
							int size = 120;
							int left = 300;
							int top = -60;
							if (varStrings.length>1){
								for (String string : varStrings) {
									if (StringUtil.isNotBlank(string) && string.indexOf("size:")>-1){
										size = Integer.parseInt(string.replace("size:",""));
										top = -size/2;
									}
									if (StringUtil.isNotBlank(string) && string.indexOf("left:")>-1){
										left += Integer.parseInt(string.replace("left:",""));
									}
									if (StringUtil.isNotBlank(string) && string.indexOf("top:")>-1){
										top += Integer.parseInt(string.replace("top:",""));
									}
								}
							}
							//获取图片长宽，以及长宽的比列
							PictureFloat imageFloat=new PictureFloat(imagetFile, size, size, left, top);
							list.add(imageFloat);
							upsetPictures(runs.get(q),list);
						}
						runs.get(q).setText(oneparaString, 0);
					} else if (varString.contains("Pic")){
						oneparaString = oneparaString.replace("«" + varString + "»", "");
						boolean leftBreak = false;
						boolean rightBreak = false;
						if (varString.indexOf("leftBreak")>-1 || varString.indexOf("allBreak")>-1) {
							leftBreak = true;
							varString = varString.replace("_leftBreak", "");
							varString = varString.replace("leftBreak_", "");
						}
						if (varString.indexOf("rightBreak")>-1 || varString.indexOf("allBreak")>-1) {
							rightBreak = true;
							varString = varString.replace("_rightBreak", "");
							varString = varString.replace("rightBreak_", "");
							varString = varString.replace("_allBreak", "");
							varString = varString.replace("allBreak_", "");
						}
						//图片前方换行
						if (leftBreak) {
							runs.get(q).addBreak();
						}
						List<PictureFloat> list=new ArrayList<>();
						Object imaget = map.get(varString);
						int picHeight = 20;
						if (varString.contains("qrCode")) {
							String[] varStrings = varString.split("_");
							boolean isPerpendicular = "p".equals(varStrings[1]);
							picHeight = Integer.parseInt(varStrings[2]);
							Map<String,Object> qrCodeMap = new HashMap<>();
							qrCodeMap.put("qrCodePath", mkDirs(BusTypeEnum.IMP_QRCODE_IMG.getCode(), (String) map.get("tempFilePath")));
							qrCodeMap.put("qrCodeContent","https://service.matridx.com/wechat/qrCodeViewReport?fjid="+map.get("qrCodeFjid")+"&ywlx="+map.get("ywlx"));
							qrCodeMap.put("imgPath",mkDirs(isPerpendicular?BusTypeEnum.IMP_ACQRCODEINFO_P_IMG.getCode():BusTypeEnum.IMP_ACQRCODEINFO_H_IMG.getCode(), (String) map.get("tempFilePath")));
							qrCodeMap.put("frameWidth", 10);
							qrCodeMap.put("ybbh", map.get("ybbh"));
							qrCodeMap.put("hzxm", map.get("hzxm"));
							qrCodeMap.put("jcxmmc", map.get("jcxmmc"));
							ImgQrCodeUtil imgQrCodeUtil = new ImgQrCodeUtil();
							String qrCodePath = "";
							try {
								qrCodePath = imgQrCodeUtil.generateQrcodeImg(qrCodeMap, isPerpendicular);
								imaget = qrCodePath;
							} catch (Exception e) {
								log.error(e.getMessage());
							}
						}
						if (imaget!=null) {
							String imagetFile =imaget.toString();
							File picture = new File(imagetFile);
							BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
							//获取图片长宽，以及长宽的比列
							int height = sourceImg.getHeight();
							int width = sourceImg.getWidth();
							BigDecimal divide = new BigDecimal(width).divide(new BigDecimal(height), 2, RoundingMode.HALF_EVEN);
							int imgWidth = new BigDecimal(picHeight).multiply(divide).intValue();
							PictureFloat imageFloat=new PictureFloat(imagetFile, imgWidth, picHeight, 0, 0);
							list.add(imageFloat);
							overlayPictures(runs.get(q),list);

						}
						//图片后方换行
						if (rightBreak) {
							runs.get(q).addBreak();
						}
						runs.get(q).setText(oneparaString, 0);
					}
				}
			}
		}
	}
	private void removeSuperfluousTable(XWPFDocument document,String flag,String value){
		Iterator<XWPFTable> deleteTable = document.getTablesIterator();
		while (deleteTable.hasNext()) {
			XWPFTable table = (XWPFTable) deleteTable.next();
			String text = table.getRow(0).getCell(0).getText();
			if (StringUtil.isNotBlank(text) && text.contains("«remove_flag_"+flag+"_")){
				if (!text.contains("remove_flag_"+flag+"_"+value)){
					for (int i = table.getRows().size()-1; i >=0 ; i--) {
						table.removeRow(i);
					}
				}
			}
		}
	}

	/**
	 * 生成特殊模板
	 * @param document
	 * @param map
	 * @param jclx
	 * @return
	 * @throws DocumentException
	 * @throws XmlException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	private boolean new_template_special(XWPFDocument document,Map<String, Object> map,String jclx) throws DocumentException, XmlException, InvalidFormatException, IOException {
		/**
		 * 替换循环体
		 */
		replaceIterativeParagraph(document,map);

		/**
		 * 替换签名、图片（包括图片,«»中包含Pic即可）
		 */
		List<XWPFParagraph> picParagraph = document.getParagraphs();
		for (int i = picParagraph.size()-1; i>-1; i--) {
			XWPFParagraph paragraph = picParagraph.get(i);
			replacePic(paragraph,map);
		}
		/**
		 * 删除阴性注释结果（若结果不为阴性）
		 */
		removeNegativeInfo(document,map);

		/**
		 * 替换图片（染色体曼哈顿图）
		 */
		List<XWPFParagraph> iterators_images = document.getParagraphs();
		for (int i = iterators_images.size()-1; i>-1; i--) {
			XWPFParagraph paragraph = iterators_images.get(i);
			replaceImage(paragraph,map);
		}
		/**
		 * 1.删除多余的表格
		 */
		Iterator<XWPFTable> deleteTable = document.getTablesIterator();
		while (deleteTable.hasNext()) {
			XWPFTable table = (XWPFTable) deleteTable.next();
			removeXWPFTable(table);
		}
		
		/**
		 * 2.删除多余的段落
		 */
		List<XWPFParagraph> deleteParagraph = document.getParagraphs();
		for (int i = deleteParagraph.size()-1; i > -1; i--) {
			XWPFParagraph paragraph = deleteParagraph.get(i);
			removeXWPFParagraph(document,paragraph);
		}
		
		/**
		 * 3.替换封面信息：封面内容为XML格式
		 */
		for (int i = 0; i < document.getParagraphs().size(); i++) {
			replaceHead(document.getParagraphs().get(i), map);
		}
		
		/**
		 * 4.替换表格中的内容
		 */
		Iterator<XWPFTable> itTable = document.getTablesIterator();
		while (itTable.hasNext()) {
			XWPFTable table = (XWPFTable) itTable.next();
			// 替换表中的数据
			replaceTable(table, map,jclx);
		}

		/**
		 * 5.处理内容为空的表格
		 */
		Iterator<XWPFTable> clear_Table = document.getTablesIterator();
		while (clear_Table.hasNext()) {
			XWPFTable table = (XWPFTable) clear_Table.next();
			//删除带有删除标记的行
			removeRow(table);
			//替换空的table为一占位符
			clearNullCell(table,map);
			//删除空的表格
			dropNullCell(table);
		}
		
		 /**
		  * 6.合并表格相同的列
		  */
		Iterator<XWPFTable> col_Table = document.getTablesIterator();
		while (col_Table.hasNext()) {
			XWPFTable table = (XWPFTable) col_Table.next();
			tableColspan(table);
		}
		
		/**
		 * 7.处理表格中需要加粗、斜体的内容
		 */
		Iterator<XWPFTable> bold_Table = document.getTablesIterator(); 
		while (bold_Table.hasNext()) {
			XWPFTable table = (XWPFTable) bold_Table.next();
			addBlodForTable(table); 
		}
		
		/**
		 * 8.替换掉文本中的信息
		 */
		List<XWPFParagraph> iterators = document.getParagraphs();
		for (int i = iterators.size()-1; i>-1; i--) {
			XWPFParagraph paragraph = iterators.get(i);
			replaceText(paragraph, map,jclx,i,document);
		}
		
		/**
		 * 9.设置标题格式 ：先整理标题格式
		 */
		List<XWPFParagraph> iterators_title = document.getParagraphs();
		for (int i = iterators_title.size()-1; i > -1; i--) {
			XWPFParagraph paragraph = iterators_title.get(i);
			replacTitle(paragraph,document,map);
		}
		
		/**
		 * 10.替换掉空的段落
		 */
		List<XWPFParagraph> iterators_s = document.getParagraphs();
		for (int i = iterators_s.size()-1; i > -1; i--) {
			XWPFParagraph paragraph = iterators_s.get(i);
			replaceParagraph(paragraph);
			addBlodForParagraph(paragraph);
		}

		/**
		 * 11.替换页眉
		 */
		List<XWPFHeader> headerList = document.getHeaderList();
		for (XWPFHeader header : headerList) {
			List<XWPFParagraph> paragraphs = header.getParagraphs();
			for (XWPFParagraph paragraph : paragraphs) {
				replaceHeader(paragraph,map);
			}
			List<XWPFTable> tables = header.getTables();
			for (XWPFTable table : tables) {
				if (table != null){
					replaceTable(table, map,jclx);
				}
			}
		}
		/**
		 * 12.替换页脚
		 */
		List<XWPFFooter> FooterList = document.getFooterList();
		for (XWPFFooter footer : FooterList) {
			List<XWPFParagraph> paragraphs = footer.getParagraphs();
			for (XWPFParagraph paragraph : paragraphs) {
				replaceFooter(paragraph,map);
			}
			List<XWPFTable> tables = footer.getTables();
			for (XWPFTable table : tables) {
				if (table != null){
					replaceTable(table, map,jclx);
				}
			}
		}
		return true;
	}
	
	/**
	 * 生成Q-index报告
	 * @param document
	 * @param map
	 * @param jclx
	 * @return
	 * @throws DocumentException
	 * @throws XmlException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	private boolean new_template(XWPFDocument document,Map<String, Object> map,String jclx) throws DocumentException, XmlException, InvalidFormatException, IOException {
		/**
		 * 4.替换表格中的内容
		 */
		Iterator<XWPFTable> itTable = document.getTablesIterator();
		while (itTable.hasNext()) {
			XWPFTable table = (XWPFTable) itTable.next();
			// 替换表中的数据
			replaceTable(table, map,jclx);
		}
		/**
		 * 替换循环体
		 */
		replaceIterativeParagraph(document,map);
		/**
		 * 替换图片（包括图片,«»中包含Pic即可）
		 */
		List<XWPFParagraph> picParagraph = document.getParagraphs();
		for (int i = picParagraph.size()-1; i>-1; i--) {
			XWPFParagraph paragraph = picParagraph.get(i);
			replacePic(paragraph,map);
		}

		/**
		 * 替换图片
		 */
		List<XWPFParagraph> iterators_images = document.getParagraphs();
		for (int i = iterators_images.size()-1; i>-1; i--) {
			XWPFParagraph paragraph = iterators_images.get(i);
			replaceImage(paragraph,map);
		}
		/**
		 * 删除阴性注释结果（若结果不为阴性）
		 */
		removeNegativeInfo(document,map);
		/**
		 * 1.删除多余的表格
		 */
		Iterator<XWPFTable> deleteTable = document.getTablesIterator();
		while (deleteTable.hasNext()) {
			XWPFTable table = (XWPFTable) deleteTable.next();
			removeXWPFTable(table);
		}
		
		/**
		 * 2.删除多余的段落	
		 */
		List<XWPFParagraph> deleteParagraph = document.getParagraphs();
		for (int i = deleteParagraph.size()-1; i > -1; i--) {
			XWPFParagraph paragraph = deleteParagraph.get(i);
			removeXWPFParagraph(document,paragraph);
		}
		
		/**
		 * 3.替换封面信息：封面内容为XML格式
		 */
		for (int i = 0; i < document.getParagraphs().size(); i++) {
			replaceHead(document.getParagraphs().get(i), map);
		}
		


		/**
		 * 5.处理内容为空的表格
		 */
		Iterator<XWPFTable> clear_Table = document.getTablesIterator();
		while (clear_Table.hasNext()) {
			XWPFTable table = (XWPFTable) clear_Table.next();
			//删除带有删除标记的行
			removeRow(table);
			//替换空的table为空
			clearCell_other(table,map);
			//删除空的表格
			dropNullCell(table);
		}
		
		/**
		  * 6.合并表格相同的列
		  */
		Iterator<XWPFTable> col_Table = document.getTablesIterator();
		while (col_Table.hasNext()) {
			XWPFTable table = (XWPFTable) col_Table.next();
			tableColspan(table);
		}
				
		/**
		 * 7.处理表格中需要加粗、斜体的内容
		 */
		Iterator<XWPFTable> bold_Table = document.getTablesIterator(); 
		while (bold_Table.hasNext()) {
			XWPFTable table = (XWPFTable) bold_Table.next();
			addBlodForTable(table); 
		}
		
		/**
		 * 8.替换掉文本中的信息
		 */
		List<XWPFParagraph> iterators = document.getParagraphs();
		for (int i = iterators.size()-1; i>-1; i--) {
			XWPFParagraph paragraph = iterators.get(i);
			replaceText(paragraph, map,jclx,i,document);
		}
		
		/**
		 * 9.设置标题格式 ：先整理标题格式
		 */
		List<XWPFParagraph> iterators_title = document.getParagraphs();
		for (int i = iterators_title.size()-1; i > -1; i--) {
			XWPFParagraph paragraph = iterators_title.get(i);
			replacTitle(paragraph,document,map);
		}
		
		/**
		 * 10.替换掉空的段落
		 */
		List<XWPFParagraph> iterators_s = document.getParagraphs();
		for (int i = iterators_s.size()-1; i > -1; i--) {
			XWPFParagraph paragraph = iterators_s.get(i);
			replacTitle(paragraph,document,map);
			replaceParagraph(paragraph);
			addBlodForParagraph(paragraph);
		}
		
		/**
		 * 11.替换页眉
		 */
		List<XWPFHeader> headerList = document.getHeaderList();
		for (XWPFHeader header : headerList) {
			List<XWPFParagraph> paragraphs = header.getParagraphs();
			for (XWPFParagraph paragraph : paragraphs) {
				replaceHeader(paragraph,map);
			}
			List<XWPFTable> tables = header.getTables();
			for (XWPFTable table : tables) {
				if (table != null){
					replaceTable(table, map,jclx);
				}			}
		}
		/**
		 * 12.替换页脚
		 */
		List<XWPFFooter> FooterList = document.getFooterList();
		for (XWPFFooter footer : FooterList) {
			List<XWPFParagraph> paragraphs = footer.getParagraphs();
			for (XWPFParagraph paragraph : paragraphs) {
				replaceFooter(paragraph,map);
			}
			List<XWPFTable> tables = footer.getTables();
			for (XWPFTable table : tables) {
				if (table != null){
					replaceTable(table, map,jclx);
				}
			}
		}
		return true;
	}
	
	/**
	 * 生成普通报告
	 * @param document
	 * @param map
	 * @param jclx
	 * @return
	 * @throws DocumentException
	 * @throws XmlException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	private boolean old_template(XWPFDocument document,Map<String, Object> map,String jclx) throws DocumentException, XmlException, InvalidFormatException, IOException {
		List<SjwzxxDto> Bacteria_List_old=(List<SjwzxxDto>) map.get("Bacteria_List_old");
		map.put("QBacteria_List", (List<SjwzxxDto>) map.get("Bacteria_List"));
		map.put("Bacteria_List", Bacteria_List_old);
		/**
		 * 替换循环体
		 */
		replaceIterativeParagraph(document,map);
		/**
		 * 替换图片（包括图片,«»中包含Pic即可）
		 */
		List<XWPFParagraph> picParagraph = document.getParagraphs();
		for (int i = picParagraph.size()-1; i>-1; i--) {
			XWPFParagraph paragraph = picParagraph.get(i);
			replacePic(paragraph,map);
		}
		
		/**
		 * 替换图片
		 */
		List<XWPFParagraph> iterators_images = document.getParagraphs();
		for (int i = iterators_images.size()-1; i>-1; i--) {
			XWPFParagraph paragraph = iterators_images.get(i);
			replaceImage(paragraph,map);
		}

		/**
		 * 删除阴性注释结果（若结果不为阴性）
		 */
		removeNegativeInfo(document,map);
		/**		 
		 * 1.删除多余的表格
		 */
		Iterator<XWPFTable> deleteTable = document.getTablesIterator();
		while (deleteTable.hasNext()) {
			XWPFTable table = (XWPFTable) deleteTable.next();
			removeXWPFTable(table);
		}
		
		/**
		 * 2.删除多余的段落	
		 */
		List<XWPFParagraph> deleteParagraph = document.getParagraphs();
		for (int i = deleteParagraph.size()-1; i > -1; i--) {
			XWPFParagraph paragraph = deleteParagraph.get(i);
			removeXWPFParagraph(document,paragraph);
		}
		
		/**
		 * 3.替换封面信息：封面内容为XML格式
		 */
		for (int i = 0; i < document.getParagraphs().size(); i++) {
			replaceHead(document.getParagraphs().get(i), map);
		}
		
		/**
		 * 4.替换表格中的内容
		 */
		Iterator<XWPFTable> itTable = document.getTablesIterator();
		while (itTable.hasNext()) {
			XWPFTable table = (XWPFTable) itTable.next();
			// 替换表中的数据
			replaceTable(table, map,jclx);
		}
		
		/**
		 * 5.处理内容为空的表格
		 */
		Iterator<XWPFTable> table_clear = document.getTablesIterator();
		while (table_clear.hasNext()) {
			XWPFTable table = (XWPFTable) table_clear.next();
			//删除带有删除标记的行
			removeRow(table);
			//替换空的table为--
			clearNullCell(table,map);
			// 合并相同的列
			tableColspan(table);
		}
		
		/**
		 * 6.处理表格中需要加粗、斜体的内容
		 */
		List<XWPFTable> xWPFTables = document.getTables();
		for (XWPFTable table:xWPFTables) { 
			addBlodForTable(table); 
		}
		
		/**
		 * 7.替换掉文本中的信息
		 */
		List<XWPFParagraph> iterators = document.getParagraphs();
		for (int i = iterators.size()-1; i>-1; i--) {
			XWPFParagraph paragraph = iterators.get(i);
			replaceText(paragraph, map,jclx,i,document);
		}
		
		/**
		 * 8.设置标题格式 ：先整理标题格式
		 */
		List<XWPFParagraph> iterators_title = document.getParagraphs();
		for (int i = iterators_title.size()-1; i > -1; i--) {
			XWPFParagraph paragraph = iterators_title.get(i);
			replacTitle(paragraph,document,map);
		}
		
		/**
		 * 9.处理段落：空内容、字体（加粗，换行，颜色）
		 */
		List<XWPFParagraph> iterators_s = document.getParagraphs();
		for (int i = iterators_s.size()-1; i > -1; i--) {
			XWPFParagraph paragraph = iterators_s.get(i);
			replaceParagraph(paragraph);
			addBlodForParagraph(paragraph);
		}
		/**
		 * 10.替换页眉
		 */
		List<XWPFHeader> headerList = document.getHeaderList();
		for (XWPFHeader header : headerList) {
			List<XWPFParagraph> paragraphs = header.getParagraphs();
			for (XWPFParagraph paragraph : paragraphs) {
				replaceHeader(paragraph,map);
			}
			List<XWPFTable> tables = header.getTables();
			for (XWPFTable table : tables) {
				if (table != null){
					replaceTable(table, map,jclx);
				}
			}
		}
		/**
		 * 11.替换页脚
		 */
		List<XWPFFooter> FooterList = document.getFooterList();
		for (XWPFFooter footer : FooterList) {
			List<XWPFParagraph> paragraphs = footer.getParagraphs();
			for (XWPFParagraph paragraph : paragraphs) {
				replaceFooter(paragraph,map);
			}
			List<XWPFTable> tables = footer.getTables();
			for (XWPFTable table : tables) {
				if (table != null){
					replaceTable(table, map,jclx);
				}
			}
		}
		return true;
	}
	
	/**
	 * 新模板删除表格
	 * @param table
	 */
	private void dropNullCell(XWPFTable table) {
		if(table.getRows().size()>0) {
			XWPFTableRow tmprow = table.getRow(0);
			String tmptext = tmprow.getCell(0).getText();
			String defaultText = "";
			ParagraphAlignment pa = ParagraphAlignment.CENTER;
			int cellfontSize = -1;
			String cellfontFamily = "";
			String cellfontColor = "";
			boolean deleteTable = false;
			if((tmptext.contains("deleteTable") && tmptext.indexOf("nullasprint")==-1) || tmptext.contains("forcedDeleteTable")){//若获取到的值是deleteTable，直接删除表格
				for (int i = table.getRows().size() - 1; i >= 0; i--) {
					table.removeRow(i);
				}
				return;
			}
			if (tmptext.contains("«nullasprint:")){
				List<XWPFParagraph> paragraphs = tmprow.getCell(0).getParagraphs();
				for (XWPFParagraph paragraph : paragraphs) {
					List<XWPFRun> runs = paragraph.getRuns();
					for (XWPFRun run : runs) {
						String runText = run.getText(run.getTextPosition()!=-1?run.getTextPosition():0);
						if (runText.contains("«nullasprint:")){
							//«nullasprint:[default:没有检出,align:center]»
							int start = runText.indexOf("«nullasprint:")+"«nullasprint:".length();
							int end = runText.indexOf("»");
							//[default:没有检出,align:center]
							String content = runText.substring(start+1, end-1).toString();
							//将content转换成List
							String[] contents = content.split(",");
							for (String s : contents) {
								if (s.contains(":")){
									String[] split = s.split(":");
									if ("default".equals(split[0])){
										defaultText = split[1];
									} else if ("align".equals(split[0])){
										if ("center".equals(split[1])){
											pa = ParagraphAlignment.CENTER;
										} else if ("left".equals(split[1])){
											pa = ParagraphAlignment.LEFT;
										} else if ("right".equals(split[1])){
											pa = ParagraphAlignment.RIGHT;
										}
									} else if ("fontsize".equals(split[0])){
										cellfontSize = Integer.parseInt(split[1]);
									} else if ("fontfamily".equals(split[0])){
										cellfontFamily = split[1];
									} else if ("fontcolor".equals(split[0])){
										cellfontColor = split[1];
									} else if ("deleteTable".equals(split[0])){
										deleteTable = "true".equals(split[1]);
									}
								}
							}
							String value = runText.replace(runText.substring(runText.indexOf("«nullasprint:"), end+1).toString(), "");
							run.setText(value,run.getTextPosition()!=-1?run.getTextPosition():0);
							if (!value.equals(run.text())){
								run.setText(value,0);
							}
						}
					}
				}
			}
			XWPFTableRow tmpRow=table.getRow(table.getRows().size()-1);
			if(tmpRow.getTableCells().size()>0) {
				XWPFTableCell tmpCell=tmpRow.getCell(0);
				if(tmpCell.getText().contains("未进行RNA测序流程")) {
					if (deleteTable){
						for (int i = table.getRows().size() - 1; i >= 0; i--) {
							table.removeRow(i);
						}
					}else {
						if (StringUtil.isNotBlank(tmpCell.getParagraphs().get(0).getRuns().get(0).getFontFamily()) && StringUtil.isNotBlank(cellfontFamily)){
							cellfontFamily = tmpCell.getParagraphs().get(0).getRuns().get(0).getFontFamily();
						}
						if (tmpCell.getParagraphs().get(0).getRuns().get(0).getFontSize()>0 && cellfontSize != -1){
							cellfontSize = tmpCell.getParagraphs().get(0).getRuns().get(0).getFontSize();
						} else {
							cellfontSize = 12;
						}
						XWPFTableRow row=copy(table, table.getRows().get(2), table.getRows().size());
						for (int i = table.getRows().size()-2; i > 1; i--) {
							table.removeRow(i);
						}
						for (int i = 0; i < row.getTableCells().size(); i++) {
							XWPFTableCell cell=row.getTableCells().get(i);
							if(i ==0) {
								cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
							}else {
								cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
							}
						}
						for (int i = row.getCell(0).getParagraphs().size()-1; i >-1; i--) {
							row.getCell(0).removeParagraph(i);
						}
						XWPFParagraph paragraph=row.getCell(0).addParagraph();
						paragraph.setAlignment(pa);
						XWPFRun run=paragraph.createRun();
						//run.setFontFamily("思源黑体");
						if (StringUtil.isNotBlank(cellfontFamily)){
							run.setFontFamily(cellfontFamily);
						}
						if (StringUtil.isNotBlank(cellfontColor)){
							run.setColor(cellfontColor);
						}
						if (cellfontSize != -1){
							run.setFontSize(cellfontSize);
						}
						run.setText(StringUtil.isNotBlank(defaultText)?defaultText:"未进行RNA测序流程");
						return;
					}
				}
			}
			int start=0;
			for (int i = 0; i < table.getRows().size(); i++) {
				XWPFTableRow row =table.getRows().get(i);
				String text=row.getTableCells().get(0).getText().replace("null", "");
				if(StringUtil.isBlank(text)) {
					List<XWPFTableCell> tableCells = row.getTableCells();
					int num = 1;
					for (int j = 1; j < tableCells.size(); j++) {
						if (StringUtil.isBlank(tableCells.get(j).getText().replace("null", ""))){
							num +=1;
						}
					}
					if (num ==  tableCells.size()){
						start=i;
						break;
					}
				}
			}
			if(start>0) {
				if (deleteTable){
					for (int i = table.getRows().size() - 1; i >= 0; i--) {
                        table.removeRow(i);
					}
				}else {
					for (int i = table.getRows().size()-1; i > start; i--) {
						table.removeRow(i);
					}
					CTRPr rPr = null;
					XWPFTableRow row=copy(table, table.getRows().get(table.getRows().size()-1), table.getRows().size());
					for (int i = 0; i < row.getTableCells().size(); i++) {
						XWPFTableCell cell=row.getTableCells().get(i);
						if (null != cell.getParagraphs().get(0).getRuns() && cell.getParagraphs().get(0).getRuns().size()>0){
							if (StringUtil.isNotBlank(cell.getParagraphs().get(0).getRuns().get(0).getFontFamily()) && StringUtil.isNotBlank(cellfontFamily)){
								cellfontFamily = cell.getParagraphs().get(0).getRuns().get(0).getFontFamily();
							}
							if (cell.getParagraphs().get(0).getRuns().get(0).getFontSize()>0  && cellfontSize != -1){
								cellfontSize = cell.getParagraphs().get(0).getRuns().get(0).getFontSize();
							} else {
								cellfontSize = 12;
							}
							if (cell.getParagraphs().get(0).getRuns().get(0).getCTR()!=null) {
								rPr = cell.getParagraphs().get(0).getRuns().get(0).getCTR().getRPr();
							}
						}
						if(i ==0) {
							cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
						}else {
							cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
						}
					}
					for (int i = table.getRows().size()-2; i > start-1; i--) {
						table.removeRow(i);
					}
					for (int i = row.getTableCells().size()-1; i >0; i--) {
						row.removeCell(i);
					}
					for (int i = row.getCell(0).getParagraphs().size()-1; i >-1; i--) {
						row.getCell(0).removeParagraph(i);
					}
					XWPFParagraph paragraph=row.getCell(0).addParagraph();
					//继承样式
					paragraph.getCTP().setPPr(table.getRows().get(0).getCell(0).getParagraphs().get(0).getCTP().getPPr());
					//设置左右居中
					paragraph.setAlignment(pa);
					//设置上下居中
					paragraph.setVerticalAlignment(TextAlignment.CENTER);
					XWPFRun run=paragraph.createRun();
					//获取行高并设置
					int height = table.getRows().get(0).getHeight();
					row.setHeight(height);
					//继承样式
					if (rPr != null){
						try {
							run.getCTR().setRPr(rPr);
							if (StringUtil.isNotBlank(cellfontFamily)){
								run.setFontFamily(cellfontFamily);
							}
							if (StringUtil.isNotBlank(cellfontColor)){
								run.setColor(cellfontColor);
							}
							if (cellfontSize != -1){
								run.setFontSize(cellfontSize);
							}
						}catch(Exception e) {
							log.error("dropNullCell: 设置继承样式时出错。" + e.getMessage());
							if (StringUtil.isNotBlank(cellfontFamily)){
								run.setFontFamily(cellfontFamily);
							}
							if (StringUtil.isNotBlank(cellfontColor)){
								run.setColor(cellfontColor);
							}
							if (cellfontSize != -1){
								run.setFontSize(cellfontSize);
							}
						}
					}else {
						//run.setFontFamily("思源黑体");
						if (StringUtil.isNotBlank(cellfontFamily)){
							run.setFontFamily(cellfontFamily);
						}
						if (StringUtil.isNotBlank(cellfontColor)){
							run.setColor(cellfontColor);
						}
						if (cellfontSize != -1){
							run.setFontSize(cellfontSize);
						}
					}
					run.setText(StringUtil.isNotBlank(defaultText)?defaultText:"未检出");
				}
			}
		}
	}
	
	/**
	 * 新模板其他标本类型
	 * @param table
	 * @param map
	 * @param varString
	 */
	private void replaceListTable_special(XWPFTable table, Map<String, Object> map, String varString) {
		String listName = varString.split("\\.")[0];
		if(listName.contains("italic_")) {
			listName=listName.replace("italic_", "");
		}
		Object o_list = map.get(listName);

		boolean isListFlg = o_list instanceof ArrayList<?>;
		if (o_list == null || !isListFlg) {
            return;
        }

		List<SjwzxxDto> sjwzxxDtoList = new ArrayList<SjwzxxDto>();
		List<SjnyxDto> sjnyxDtoList = new ArrayList<SjnyxDto>();
		for (Object item : (List<?>) o_list) {
			if (item instanceof SjwzxxDto) {
				sjwzxxDtoList = (List<SjwzxxDto>) o_list;
				break;
			}else if((item instanceof SjnyxDto)){
				sjnyxDtoList = (List<SjnyxDto>) o_list;
				break;
			}
		}

		// 如果没有数据，则清除掉空的row，显示为 --
		if (sjwzxxDtoList.size() == 0 && sjnyxDtoList.size() == 0) {
			return;
		}
		
		if("Viruses_List".equals(listName) || "Bacteria_List".equals(listName)|| "Mycobacteria_List".equals(listName)|| "MCR_List".equals(listName)|| "Fungi_List".equals(listName)
				|| "Viruses_D_List".equals(listName)|| "Viruses_R_List".equals(listName)|| "Parasite_List".equals(listName)|| "Mycoplasma_List".equals(listName)
				|| "Chlamydia_List".equals(listName)|| "Rickettsia_List".equals(listName)|| "Spirochete_List".equals(listName)|| "sjwzxx_List".equals(listName) ) {
			addNewRows_image(table, sjwzxxDtoList,map);
		}else if("Background_List".equals(listName)|| "Gene_List".equals(listName)|| "Pathogen_List".equals(listName)|| "Sjwzxx_List".equals(listName)  || "Sjnyx_List".equals(listName) || "TBTSjnyx_List".equals(listName) || "Commensal_List".equals(listName)
				|| "BackgroundInfo_List".equals(listName) || "yzjgList".equals(listName) || "zkjgList".equals(listName)) {
			// 获取表格的列数，根据最后一列
			int rowCnt = table.getRows().size();
			addNewRows(table ,sjwzxxDtoList,map);
 			// 删除模版行
 			table.removeRow(rowCnt - 1);
		}else if ("VirulenceFactorStat_List".equals(listName)){
			//毒力因子替换
			List<WechatVirulenceFactorStatModel> vfsList = (List<WechatVirulenceFactorStatModel>) o_list;
			if (vfsList.size() == 0) {
				return;
			}
			// 获取表格的列数，根据最后一列
			int rowCnt = table.getRows().size();
			addNewRowsVrf(table ,vfsList);
			// 删除模版行
			table.removeRow(rowCnt - 1);
		}else if (listName.contains("_MapList")){
			//毒力因子替换
			List<Map<String, Object>> mapList = (List<Map<String, Object>>) o_list;
			if (mapList.size() == 0) {
				return;
			}
			// 获取表格的列数，根据最后一列
			int rowCnt = table.getRows().size();
			addNewRowsMapList(table ,mapList);
			// 删除模版行
			table.removeRow(rowCnt - 1);
		}

	}
	
	/**
	 * 判断字符串是否在数组中存在
	 * @param strs
	 * @param s
	 * @return
	 */
	public static boolean isHave(String[] strs,String s){ 
		/*此方法有两个参数，第一个是要查找的字符串数组，第二个是要查找的字符或字符串*/
		for(int i=0;i<strs.length;i++){
			if(strs[i].indexOf(s)!=-1){//循环查找字符串数组中的每个字符串中是否包含所有查找的内容
				return true;//查找到了就返回真，不在继续查询
			}
		}
		return false;//没找到返回false
	} 
	
	/**
	 * @param table
	 * @param sjwzxxList
	 */
	private void addNewRows_other(XWPFTable table,List<SjwzxxDto> sjwzxxList,Map<String,Object> map) {
		int rownum =  table.getRows().size();
		if(rownum<4) {
			int rowCnt = table.getRows().size();
			addNewRows(table,sjwzxxList,map);
			// 删除模版行
			table.removeRow(rowCnt - 1);
		}else {
			table.removeRow(4);
			table.removeRow(3);
			XWPFTableRow tmpRow=table.getRow(table.getRows().size()-1);
			for (int x = 0; x < sjwzxxList.size(); x++) {
				XWPFTableRow tmpRow1 = copy(table, tmpRow, table.getRows().size());
				replaceMultiRowsText(tmpRow1,sjwzxxList.get(x),x);
			}
			table.removeRow(2);
		}
	}
	
	/**
	 * 清空没数据的表格
	 * @param table
	 */
	private void clearCell_other(XWPFTable table,Map<String,Object> map) {
		int countRow = table.getNumberOfRows();
		for (int i = 0; i < countRow; i++) {
			XWPFTableRow row = table.getRow(i);
			List<XWPFTableCell> cells = row.getTableCells();
			for (XWPFTableCell cell : cells) {
				//这里进行一个判断，查询到去人源这个表格，直接删除
				String  cellText=cell.getText();
				if(cellText!=null &&cellText.contains("Host index")) {
					for (int j = table.getRows().size()-1; j >-1 ; j--) {
						table.removeRow(j);
					}
					return;
				}else {
					List<XWPFParagraph> cellParagraph = cell.getParagraphs();
					for (int j = 0; j < cellParagraph.size() && cellParagraph != null; j++) {
						XWPFParagraph paragraph=cellParagraph.get(j);
						List<XWPFRun> runs = paragraph.getRuns();
						for (int k = 0; k < runs.size() && runs != null; k++) {
							String text = runs.get(k).getText(runs.get(k).getTextPosition());
							if (StringUtil.isNotBlank(text)) {
								String heard = text.substring(0, 1);
								String boot = text.substring(text.length() - 1);
								if (heard != null && boot != null) {
									if ("«".equals(heard) && "»".equals(boot) && !text.contains("«nullasprint")) {
										text = text.replace(text, "");
									}else if((!text.contains("«nullasprint:") && text.contains("deleteTable")) || text.contains("forcedDeleteTable") ){//补丁，处理表格第一行deleteTable公式时，没有对内容进行替换的问题
										int startTagPos=text.indexOf("«");
										int endTagPos=text.indexOf("»");
										if(startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos){
											text = text.replace(text.substring(startTagPos,endTagPos+1),String.valueOf(replaceJudge(text.substring(startTagPos+1,endTagPos),map,cell)));
										}
									}
								}
							}
							runs.get(k).setText(text, 0);
						}
					}
				}
			}
		}
	}
	
	/**
	 * 删除带有删除标记得表格--行
	 * @param table
	 */
	private void removeRow(XWPFTable table) {
		for (int i =table.getRows().size()-1; i >-1; i--) {
			XWPFTableRow row=table.getRows().get(i);
			for (int j =row.getTableCells().size()-1; j >-1; j--) {
				XWPFTableCell cell=row.getTableCells().get(j);
				String text=cell.getText();
				if(text!=null&& "«DELETE_ROW»".equals(text)) {
					table.removeRow(i);
					break;
				}
			}
		}
	}
	
	/**
	 * 删除模板上多余的table
	 * 因为只有一个模板，有些内容在特定情况下不需要显示，所以要删除掉
	 * @param table
	 */
	private void removeXWPFTable(XWPFTable table) {
		if("D".equals(globalJclx)) {
			for (int i = 0; i < table.getRows().size(); i++) {
				XWPFTableRow  row =table.getRows().get(i);
				for (XWPFTableCell cell : row.getTableCells()) {
					String cellText=cell.getText();
					if(StringUtil.isNotBlank(cellText)) {
						if(cellText.contains("«DDELETE»")) {
							table.removeRow(i);
						}else if(cellText.contains("«RDELETE»")||cellText.contains("«CDELETE»")){
							for (int j = 0; j < cell.getParagraphs().size(); j++) {
								XWPFParagraph paragraph=cell.getParagraphs().get(j);
								for (int k = 0; k < paragraph.getRuns().size(); k++) {
									List<XWPFRun> runs=paragraph.getRuns();
									for (int l = 0; l < runs.size(); l++) {
										String runText=runs.get(l).getText(runs.get(l).getTextPosition());
										if(StringUtil.isNotBlank(runText)) {
											if(runText.contains("«RDELETE»")||runText.contains("«CDELETE»")) {
												String newText=runText.replace("«RDELETE»", "").replace("«CDELETE»", "");
												runs.get(l).setText(newText,0);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}else if("R".equals(globalJclx)) {
			for (int i = 0; i < table.getRows().size(); i++) {
				XWPFTableRow  row =table.getRows().get(i);
				for (XWPFTableCell cell : row.getTableCells()) {
					String cellText=cell.getText();
					if(StringUtil.isNotBlank(cellText)) {
						if(cellText.contains("«RDELETE»")) {
							table.removeRow(i);
						}else if(cellText.contains("«DDELETE»")||cellText.contains("«CDELETE»")){
							for (int j = 0; j < cell.getParagraphs().size(); j++) {
								XWPFParagraph paragraph=cell.getParagraphs().get(j);
								for (int k = 0; k < paragraph.getRuns().size(); k++) {
									List<XWPFRun> runs=paragraph.getRuns();
									for (int l = 0; l < runs.size(); l++) {
										String runText=runs.get(l).getText(runs.get(l).getTextPosition());
										if(StringUtil.isNotBlank(runText)) {
											if(runText.contains("«DDELETE»")||runText.contains("«CDELETE»")) {
												String newText=runText.replace("«DDELETE»", "").replace("«CDELETE»", "");
												runs.get(l).setText(newText,0);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}else if("C".equals(globalJclx)) {
			for (int i = 0; i < table.getRows().size(); i++) {
				XWPFTableRow  row =table.getRows().get(i);
				for (XWPFTableCell cell : row.getTableCells()) {
					String cellText=cell.getText();
					if(StringUtil.isNotBlank(cellText)) {
						if(cellText.contains("«CDELETE»")) {
							table.removeRow(i);
						}else if(cellText.contains("«DDELETE»")||cellText.contains("«RDELETE»")){
							for (int j = 0; j < cell.getParagraphs().size(); j++) {
								XWPFParagraph paragraph=cell.getParagraphs().get(j);
								for (int k = 0; k < paragraph.getRuns().size(); k++) {
									List<XWPFRun> runs=paragraph.getRuns();
									for (int l = 0; l < runs.size(); l++) {
										String runText=runs.get(l).getText(runs.get(l).getTextPosition());
										if(StringUtil.isNotBlank(runText)) {
											if(runText.contains("«DDELETE»")||runText.contains("«RDELETE»")) {
												String newText=runText.replace("«DDELETE»", "").replace("«RDELETE»", "");
												runs.get(l).setText(newText,0);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 删除模板上多余的段落
	 * 因为只有一个模板，有些内容在特定情况下不需要显示，所以要删除掉
	 * @param document
	 * @param paragraph
	 */
	private void removeXWPFParagraph(XWPFDocument document, XWPFParagraph paragraph) {
		if("D".equals(globalJclx)) {
			String paragraphText=paragraph.getText();
			if(StringUtil.isNotBlank(paragraphText)) {
				if(paragraphText.contains("«DDELETE»")) {
					document.removeBodyElement(document.getPosOfParagraph(paragraph));
				}else if(paragraphText.contains("«RDELETE»")||paragraphText.contains("«CDELETE»")) {
					List<XWPFRun> runs=paragraph.getRuns();
					for (int l = 0; l < runs.size(); l++) {
						String runText=runs.get(l).getText(runs.get(l).getTextPosition());
						if(StringUtil.isNotBlank(runText)) {
							if(runText.contains("«RDELETE»")||runText.contains("«CDELETE»")) {
								String newText=runText.replace("«RDELETE»", "").replace("«CDELETE»", "");
								runs.get(l).setText(newText,0);
							}
						}
					}
				}
			}
		}else if("R".equals(globalJclx)) {
			String paragraphText=paragraph.getText();
			if(StringUtil.isNotBlank(paragraphText)) {
				if(paragraphText.contains("«RDELETE»")) {
					document.removeBodyElement(document.getPosOfParagraph(paragraph));
				}else if(paragraphText.contains("«DDELETE»")||paragraphText.contains("«CDELETE»")) {
					List<XWPFRun> runs=paragraph.getRuns();
					for (int l = 0; l < runs.size(); l++) {
						String runText=runs.get(l).getText(runs.get(l).getTextPosition());
						if(StringUtil.isNotBlank(runText)) {
							if(runText.contains("«DDELETE»")||runText.contains("«CDELETE»")) {
								String newText=runText.replace("«DDELETE»", "").replace("«CDELETE»", "");
								runs.get(l).setText(newText,0);
							}
						}
					}
				}
			}
		}else if("C".equals(globalJclx)){
			String paragraphText=paragraph.getText();
			if(StringUtil.isNotBlank(paragraphText)) {
				if(paragraphText.contains("«CDELETE»")) {
					document.removeBodyElement(document.getPosOfParagraph(paragraph));
				}else if(paragraphText.contains("«DDELETE»")||paragraphText.contains("«RDELETE»")) {
					List<XWPFRun> runs=paragraph.getRuns();
					for (int l = 0; l < runs.size(); l++) {
						String runText=runs.get(l).getText(runs.get(l).getTextPosition());
						if(StringUtil.isNotBlank(runText)) {
							if(runText.contains("«DDELETE»")||runText.contains("«RDELETE»")) {
								String newText=runText.replace("«DDELETE»", "").replace("«RDELETE»", "");
								runs.get(l).setText(newText,0);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 设置页眉
	 */
	private void replaceHeader(XWPFParagraph paragraph,Map<String,Object> map) throws XmlException {
		try {
			List<XWPFRun> runs = paragraph.getRuns();
			for (int i = 0; i < runs.size() && runs != null; i++) {
				// 获取文本信息
				String text = runs.get(i).getText(runs.get(i).getTextPosition());
				if (text == null){
					continue;
				}
				while (true){ 
					int startTagPos = text.indexOf("«");
					int endTagPos = text.indexOf("»");
					// 寻找到需要替换的变量，如果没有找到则到下一个run
					if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
						String varString = text.substring(startTagPos + 1, endTagPos);
						Object object_value = map.get(varString);
						if (object_value instanceof String) {
							String rep_value = (String) object_value;
							// 如果找不到相应的替换问题，则替换成空字符串
							if (StringUtil.isBlank(rep_value)) {
								text = text.replace("«" + varString + "»", "");
							} else {
								text = text.replace("«" + varString + "»", rep_value);
							}
							runs.get(i).setText(text, 0);
						} else {
							text = text.replace("«" + varString + "»", "");
							runs.get(i).setText(text, 0);
						}
					} else {
						break;
					}
				}
			}
			for (int i = 0; i < paragraph.getCTP().sizeOfRArray(); i++) {
				XmlObject xml_obj = paragraph.getCTP().getRArray(i);
				String xml_str = xml_obj.toString();
				String t_xml_str = xml_str;
				while (StringUtils.isNotBlank(t_xml_str)) {
					int startTagPos = t_xml_str.indexOf("«");
					int endTagPos = t_xml_str.indexOf("»");
					if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
						String varString = t_xml_str.substring(startTagPos + 1, endTagPos).trim();
						Object object_value = map.get(varString);
						if (object_value instanceof String) {
							String rep_value = (String) object_value;
							xml_str = xml_str.replaceAll("«" + varString + "»", rep_value);
							XmlObject xobj = XmlObject.Factory.parse(xml_str);
							xml_obj.set(xobj);
						}
					}else {
						break;
					}
					if(endTagPos >=0) {
                        t_xml_str = t_xml_str.substring(endTagPos + 1);
                    } else {
                        t_xml_str = "";
                    }
				}
			}
		}catch(Exception e) {
			log.error("replaceHeader Exception:" + e.getMessage());
		}
	}
	/**
	 * 设置页脚
	 */
	private void replaceFooter(XWPFParagraph paragraph,Map<String,Object> map){
		try {
			List<XWPFRun> runs = paragraph.getRuns();
			for (int i = 0; i < runs.size() && runs != null; i++) {
				// 获取文本信息
				String text = runs.get(i).getText(runs.get(i).getTextPosition());
				if (text == null){
					continue;
				}
				while (true){
					int startTagPos = text.indexOf("«");
					int endTagPos = text.indexOf("»");
					// 寻找到需要替换的变量，如果没有找到则到下一个run
					if (startTagPos > -1 && endTagPos > -1 && endTagPos > startTagPos) {
						String varString = text.substring(startTagPos + 1, endTagPos);
						Object object_value = map.get(varString);
						if (object_value instanceof String) {
							String rep_value = (String) object_value;
							// 如果找不到相应的替换问题，则替换成空字符串
							if (StringUtil.isBlank(rep_value)) {
								text = text.replace("«" + varString + "»", "");
							} else {
								text = text.replace("«" + varString + "»", rep_value);
							}
							runs.get(i).setText(text, 0);
						} else {
							text = text.replace("«" + varString + "»", "");
							runs.get(i).setText(text, 0);
						}
					} else {
						break;
					}
				}
			}
		}catch(Exception e) {
			log.error("replaceFooter Exception:" + e.getMessage());
		}
	}
	/**
	 * 设置图片
	 */
	private void replaceImage(XWPFParagraph paragraph,Map<String,Object> map){
		if (paragraph.getText().contains("«rstmhdt»")){
//			XWPFRun tmprun = null;
			Object rstmhdts = map.get("rstmhdt");
			if (rstmhdts!=null) {
				for (Object rstmhdtPath : (ArrayList) rstmhdts) {
					File file = new File(rstmhdtPath.toString());
					if (file.exists()){
						XmlCursor xmlCursor = paragraph.getCTP().newCursor();
						XWPFParagraph newParagraph = paragraph.getDocument().insertNewParagraph(xmlCursor);
						XWPFRun run = newParagraph.createRun();
						List<XWPFRun> runs = paragraph.getRuns();
						String text = paragraph.getText();

						for (XWPFRun xwpfRun : runs) {
							run.setFontSize(xwpfRun.getFontSize());
							text = text.replace("null", "");
							if(text.contains("{\\n}")||text.contains("{n}")){
								run.addBreak();
								text = "  "+text.replace("{\\n}", "").replace("{n}", "");
							}
							if(text.contains("{b+i}")) {
								run.setBold(true);
								run.setItalic(true);
								text = text.replace("{b+i}", "");
							}
							if(text.contains("{b}")) {
								run.setBold(true);
								text = text.replace("{b}", "");
							}
							if(text.contains("{i}")) {
								run.setItalic(true);
								text = text.replace("{i}", "");
							}
							if(text.contains("{color:")){
								int begin = text.indexOf("{");
								int end = text.indexOf("}");
								String flg=text.substring(begin, end+1);
								String color=flg.substring(1,flg.length()-1).split(":")[1];
								run.setColor(color);
								text=text.replace(flg, "");
							}
//							run.setText(text);
						}
						List<PictureFloat> list = new ArrayList<>();
						PictureFloat rstmhdt = new PictureFloat(rstmhdtPath.toString(),500,100,0,0);
						list.add(rstmhdt);
						run.setText(text.substring(0,text.indexOf("«rstmhdt»")),0);
						// 图片路径
						FileInputStream image = null;
						try {
							if(list != null && list.size() > 0) {
								for (int i = 0; i < list.size(); i++) {
									PictureFloat imageFloat = list.get(i);
									image = new FileInputStream(imageFloat.getWjlj());
									run.addPicture(image, XWPFDocument.PICTURE_TYPE_PNG, imageFloat.getWjlj(), Units.toEMU(imageFloat.getWidth()), Units.toEMU(imageFloat.getHeight()));
									if(i > 0) {
										imageFloatDeal(run, i, imageFloat.getWidth(),
												imageFloat.getHeight(), imageFloat.getLeft(), imageFloat.getTop());
									}
									image.close();
								}
							}
						} catch (Exception e) {
							log.error(e.toString());
						} finally {
							try {
								if(image != null) {
                                    image.close();
                                }
							} catch (IOException e) {
								log.error(e.toString());
							}
						}
						run.setText(text.substring(text.lastIndexOf("«rstmhdt»")+"«rstmhdt»".length()),1);
					}
				}
			}

		}
	}
	/**
	 * Word中添加图章
	 * String srcPath, 源Word路径
	 * String storePath, 添加图章后的路径
	 * String sealPath, 图章路径（即图片）
	 * String tabText, 在Word中盖图章的标识字符串，如：(签字/盖章)
	 * int width, 图章宽度
	 * int height, 图章高度
	 * int leftOffset, 图章在编辑段落向左偏移量
	 * int topOffset, 图章在编辑段落向上偏移量
	 * boolean behind，图章是否在文字下面
	 * @return void
	 */
	private void  sealInWord(String srcPath, String storePath,String sealPath,String tabText, int width, int height, int leftOffset, int topOffset, boolean behind) throws Exception {
		File fileTem = new File(srcPath);
		InputStream is = new FileInputStream(fileTem);
		XWPFDocument document = new XWPFDocument(is);
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		XWPFRun targetRun = null;
		for(XWPFParagraph  paragraph : paragraphs){
			if(!"".equals(paragraph.getText()) && paragraph.getText().contains(tabText)){
				List<XWPFRun> runs = paragraph.getRuns();
				targetRun = runs.get(runs.size()-1);
			}
		}
		if(targetRun != null){
			InputStream in = new FileInputStream(sealPath);//设置图片路径
			if(width <= 0){
				width = 100;
			}
			if(height <= 0){
				height = 100;
			}
			//创建Random类对象
			Random random = new Random();
			//产生随机数
			int number = random.nextInt(999) + 1;
			targetRun.addPicture(in, Document.PICTURE_TYPE_PNG, "Seal"+number, Units.toEMU(width), Units.toEMU(height));
			in.close();
			// 2. 获取到图片数据
			CTDrawing drawing = targetRun.getCTR().getDrawingArray(0);
			CTGraphicalObject graphicalobject = drawing.getInlineArray(0).getGraphic();

			//拿到新插入的图片替换添加CTAnchor 设置浮动属性 删除inline属性
			CTAnchor anchor = getAnchorWithGraphic(graphicalobject, "Seal"+number, Units.toEMU(width), Units.toEMU(height),//图片大小
					Units.toEMU(leftOffset), Units.toEMU(topOffset), behind);//相对当前段落位置 需要计算段落已有内容的左偏移
			drawing.setAnchorArray(new CTAnchor[]{anchor});//添加浮动属性
			drawing.removeInline(0);//删除行内属性
		}
		document.write(new FileOutputStream(storePath));
		document.close();
	}

	/**
	 * 删除阴性注释结果（若结果不为阴性）
	 * @param document
	 * @param map
	 */
	private void removeNegativeInfo(XWPFDocument document,Map<String, Object> map){
		Iterator<XWPFTable> tables = document.getTablesIterator();
		while (tables.hasNext()){
			XWPFTable table = (XWPFTable) tables.next();
			boolean deleteTable = false;
			for (int j = 0; j < table.getRows().size(); j++) {
				XWPFTableRow  row =table.getRows().get(j);
				for (XWPFTableCell cell : row.getTableCells()) {
					String cellText=cell.getText();
					if (cellText.contains("«Negative:")){
						if ("阴性".equals(map.get("jyjg"))){
							String text = cellText.replace(cellText.substring(cellText.indexOf("«"),cellText.indexOf(":")+1),"").replace("»", "");
							String fontFamily="";
//							XWPFParagraph tmpparagraph = null;
							XWPFRun tmprun = null;
							for (XWPFParagraph paragraph : cell.getParagraphs()){
								if(StringUtil.isNotBlank(paragraph.getText())){
									for (XWPFRun run : paragraph.getRuns()) {
										if (StringUtil.isNotBlank(run.text())){
											tmprun = run;
											break;
										}
									}
									break;
								}
							}
							if (StringUtil.isNotBlank(tmprun.getFontFamily())) {
								fontFamily = tmprun.getFontFamily();
							}
							int fontSize=tmprun.getFontSize();
							String fontColor=tmprun.getColor();
							boolean bold = tmprun.isBold();
							for (int i = cell.getParagraphs().size()-1; i >0 ; i--){
								cell.removeParagraph(i);
							}
							XWPFParagraph tempParagraph = cell.getParagraphs().get(0);
							for (int i = tempParagraph.getRuns().size()-1; i >-1 ; i--) {
								tempParagraph.removeRun(i);
							}
							XWPFRun run=tempParagraph.createRun();
							//run.setFontFamily(fontFamily);
							if (StringUtil.isNotBlank(fontFamily)) {
								run.setFontFamily(fontFamily);
							}
							run.setFontSize(fontSize);
							run.setBold(bold);
							run.setColor(fontColor);
							run.setText(text);
						}else{
							deleteTable = true;
							break;
						}
					}
				}
				if (deleteTable){
					break;
				}
			}
			if (deleteTable){
				document.removeBodyElement( document.getPosOfTable( table ) );
				break;
			}
		}
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		for (int num = 0; num < paragraphs.size(); num++) {
			XWPFParagraph paragraph = paragraphs.get(num);
			if(paragraph.getText().contains("«Negative:")) {
				if ("阴性".equals(map.get("jyjg"))){
					String title=paragraph.getText().replace(paragraph.getText().substring(paragraph.getText().indexOf("«"),paragraph.getText().indexOf(":")+1),"").replace("»", "");
					XWPFRun tmprun=null;
					for (int i = 0; i < paragraph.getRuns().size(); i++) {
						if(StringUtil.isNotBlank(paragraph.getRuns().get(i).text())) {
							tmprun=paragraph.getRuns().get(i);
							break;
						}
					}
					//String fontFamily="思源黑体";
					String fontFamily="";
					if (StringUtil.isNotBlank(tmprun.getFontFamily())) {
						fontFamily = tmprun.getFontFamily();
					}
					int fontSize=tmprun.getFontSize();
					String fontColor=tmprun.getColor();
					boolean bold = tmprun.isBold();
					for (int i = paragraph.getRuns().size()-1; i >-1 ; i--) {
						paragraph.removeRun(i);
					}
					XWPFRun run=paragraph.createRun();
					//run.setFontFamily(fontFamily);
					if (StringUtil.isNotBlank(fontFamily)) {
						run.setFontFamily(fontFamily);
					}
					run.setFontSize(fontSize);
					run.setBold(bold);
					run.setColor(fontColor);
					run.setText(title);
				}else{
					document.removeBodyElement(document.getPosOfParagraph(paragraph));
					break;
				}
			}
		}
	}


	/**
	 * 替换循环变量
	 * @param document
	 * @param map
	 */
	private void replaceIterativeParagraph(XWPFDocument document, Map<String, Object> map) {
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		if (paragraphs != null && paragraphs.size() > 0) {
			getIterateIndexAndReplace(paragraphs, map ,false);
		}
	}

	/**
	 * 获取循环体的段落索引和run索引
	 * @param map 变量map
	 * @param isCell 是否是表格单元格
	 */
	private void getIterateIndexAndReplace(Object object,Map<String, Object> map,boolean isCell) {
		List<XWPFParagraph> paragraphs = null;
		XWPFTableCell cell = null;
		if (isCell){
			cell = (XWPFTableCell) object;
			paragraphs = cell.getParagraphs();
		}else{
			paragraphs = (List<XWPFParagraph>) object;
		}
		boolean isContinue = true;
		while (isContinue){
			for (int startParIndex = 0; startParIndex < paragraphs.size(); startParIndex++) {
				int iterateStartParIndex = -1;//循环开始段落索引
				int iterateStartRunIndex = -1;//循环开始run索引
				int iterateEndParIndex = -1;//循环结束段落索引
				int iterateEndRunIndex = -1;//循环结束run索引
				if (paragraphs.get(startParIndex) != null) {
					String pText = paragraphs.get(startParIndex).getText();
					if (pText.contains("«Iterate.")){
						//若当前段落包含循环开始标签，则获取循环开始段落索引
						iterateStartParIndex = startParIndex;
						//在当前段落中获取循环开始run索引
						iterateStartRunIndex = getRunIndexInPar(paragraphs.get(startParIndex), "«Iterate.");
						//判断当前段落中是否包含循环结束标签
						if (pText.contains("«/Iterate.")) {
							//若当前段落包含循环结束标签，则说明循环体在同一个段落中，获取循环结束段落索引
							iterateEndParIndex = startParIndex;
						}else {
							//若当前段落不包含循环结束标签，则说明循环体在多个段落中，从下一个段落开始去获取循环结束段落索引
							for (int endParIndex = startParIndex+1; endParIndex < paragraphs.size(); endParIndex++) {
								if (paragraphs.get(endParIndex) != null) {
									String pTextNext = paragraphs.get(endParIndex).getText();
									if (pTextNext.contains("«/Iterate.")) {
										//若当前段落包含循环结束标签，则说明循环体在同一个段落中，获取循环结束段落索引
										iterateEndParIndex = endParIndex;
										break;
									}
								}
							}
						}
						//在当前段落中获取循环结束run索引
						iterateEndRunIndex = getRunIndexInPar(paragraphs.get(iterateEndParIndex), "«/Iterate.");
					}
				}
				if (iterateStartParIndex != -1 && iterateEndParIndex != -1 && iterateStartRunIndex != -1 && iterateEndRunIndex != -1){
					XWPFParagraph startPar = paragraphs.get(iterateStartParIndex);
					String iterateName = startPar.getText();
					String iterateListName = iterateName.substring(iterateName.indexOf("«Iterate.")+9, iterateName.indexOf("»"));//循环变量名
					String iterateStartText = "«Iterate." + iterateListName + "»";//循环体开始标记
					String iterateEndText = "«/Iterate." + iterateListName + "»";//循环体结束标记
					String iterateJudge = "";//循环体标题中的判断条件
					if (iterateListName.indexOf("?")>-1){
						iterateJudge = iterateListName.substring(iterateListName.indexOf("?")+1);
						iterateListName = iterateListName.substring(0, iterateListName.indexOf("?"));
					}
					List<Object> iterateList = (List<Object>) map.get(iterateListName);//获取循环变量值
					if (StringUtil.isNotBlank(iterateJudge) && iterateList != null && iterateList.size()>0){
						iterateList = getIterateList(iterateList,iterateJudge,map);
					}else{
						if(iterateList != null && iterateList.size()>0){
							for(Object o:iterateList){
								String zswxbs=map.get("zswxbs")!=null?String.valueOf(map.get("zswxbs")):"0";
								dealWzAndRefXh(o,map,Integer.valueOf(zswxbs)+1);
								map.put("zswxbs",String.valueOf(Integer.valueOf(zswxbs)+1));
							}
						}
					}
					if (iterateList != null && iterateList.size()>0){
						int listSize = iterateList.size();
						if (iterateEndParIndex > iterateStartParIndex){
							//循环体在多个段落中,多个段落一起循环
//						List<XWPFRun> prefixHalfParRunList = new ArrayList<>();//循环体开始段落中的run集合
							List<XWPFParagraph> paragraphList = new ArrayList<>();//循环体段落集合
//						List<XWPFRun> suffixHalfParRunList = new ArrayList<>();//循环体结束段落中的run集合
							//获取循环体段落
							XWPFParagraph endPar = paragraphs.get(iterateEndParIndex);//循环体结束段落
//						XWPFRun startRun = startPar.getRuns().get(iterateStartRunIndex);//循环体开始run
//						int startRunSize = startPar.getRuns().size() - 1 - iterateStartRunIndex;//循环体开始段落中的run数量
//						XWPFRun endRun = endPar.getRuns().get(iterateEndRunIndex);//循环体结束run

							//获取循环体段落
							if (iterateEndParIndex-iterateStartParIndex>1){
								for (int i = iterateStartParIndex+1; i < iterateEndParIndex; i++) {
									paragraphList.add(paragraphs.get(i));
								}
							}
							if (listSize>0) {
								boolean isFirst = true;
								for (int i = 0; i < listSize; i++) {
									Object o = iterateList.get(i);
									int insertParIndex = iterateEndParIndex;
//								int insertRunIndex = iterateEndRunIndex;
									if (paragraphList.size()>0){
										for (XWPFParagraph fromPar : paragraphList) {
											insertParIndex = insertParIndex + 1;
											XmlCursor xmlCursor = startPar.getCTP().newCursor();//在模板行最后一段处新加一个段落
											//在tmpPar之后插入新的段落
											XWPFParagraph toPar = null;
											if (isCell){
												toPar = cell.insertNewParagraph(xmlCursor);
											}else {
												XWPFDocument document = startPar.getDocument();
												toPar = document.insertNewParagraph(xmlCursor);
											}
											toPar = copyPar(fromPar,toPar);
											List<XWPFRun> fromRuns = fromPar.getRuns();
											if (fromRuns.size()>0){
												for (XWPFRun fromRun : fromRuns) {
													XWPFRun toRun = toPar.createRun();
													isFirst = replaceIterateRun(toRun, fromRun, o, iterateStartText, iterateEndText, i,isFirst);
												}
											}
										}
									}
								}
							}
							iterateStartParIndex = paragraphs.indexOf(startPar);
							iterateEndParIndex = paragraphs.indexOf(endPar);
							//移除循环体模板
							for (int i = iterateEndParIndex; i >= iterateStartParIndex; i--) {
								if (isCell){
									cell.removeParagraph(i);
								}else {
									XWPFDocument document = startPar.getDocument();
									document.removeBodyElement(document.getPosOfParagraph(paragraphs.get(i)));
								}
							}
							//移除循环体模板中«/Iterate.»部分
							if (endPar != null){
								if (isCell){
									if (paragraphs.indexOf(endPar) > -1){
										cell.removeParagraph(paragraphs.indexOf(endPar));
									}
								}else {
									XWPFDocument document = endPar.getDocument();
									if (document.getPosOfParagraph(endPar) > -1){
										document.removeBodyElement(document.getPosOfParagraph(endPar));
									}
								}
							}
						}else {
							//循环体在同一个段落中
							//获取循环体段落中的run集合
							List<XWPFRun> runList = new ArrayList<>();
							XWPFRun beforeRun = startPar.getRuns().get(iterateStartRunIndex);
							String beforeRunText = beforeRun.text();
							String iterateInBefore = beforeRunText.substring(Math.min((beforeRunText.indexOf(iterateStartText) + iterateStartText.length() + 1), beforeRunText.length()));//循环前，循环体中的内容
							String iterateOutBefore = beforeRunText.substring(0, beforeRunText.indexOf(iterateStartText));//循环前，循环体外的内容
							beforeRun.setText(iterateOutBefore, 0);
							if (StringUtil.isNotBlank(iterateInBefore)){
								XWPFRun tmpBeforeRun = startPar.insertNewRun(iterateStartRunIndex+1);
								tmpBeforeRun.getCTR().setRPr(beforeRun.getCTR().getRPr());
								runList.add(tmpBeforeRun);
							}
							for (int i = iterateStartRunIndex+1; i < iterateEndRunIndex; i++) {
								runList.add(startPar.getRuns().get(i));
							}
							XWPFRun afterRun = startPar.getRuns().get(iterateEndRunIndex);
							String afterRunText = afterRun.text();
							if(afterRunText.indexOf(iterateEndText) >0) {
								String iterateInAfter = afterRunText.substring(0, afterRunText.indexOf(iterateEndText));//循环后，循环体中的内容
								if (StringUtil.isNotBlank(iterateInAfter)){
									XWPFRun tmpBeforeRun = startPar.insertNewRun(iterateEndRunIndex);
									tmpBeforeRun.getCTR().setRPr(beforeRun.getCTR().getRPr());
									runList.add(tmpBeforeRun);
								}
							}
							String iterateOutAfter = afterRunText.substring(Math.min((afterRunText.indexOf(iterateEndText) + iterateEndText.length() + 1), afterRunText.length()));//循环后，循环体外的内容
							afterRun.setText(iterateOutAfter, 0);
							boolean isFirst = true;
							/*for (int i = listSize-1; i >=  0; i--) {
								Object o = iterateList.get(i);
								int insertRunIndex = iterateStartRunIndex+runList.size();
								for (XWPFRun fromRun : runList) {
									insertRunIndex = insertRunIndex + 1;
									XWPFRun toRun = startPar.insertNewRun(insertRunIndex);
									isFirst = replaceIterateRun(toRun, fromRun, o, iterateStartText, iterateEndText, i,isFirst);
								}
							}*/
							for (int i = 0; i <  listSize; i++) {
								Object o = iterateList.get(i);
								//int insertRunIndex = iterateStartRunIndex+runList.size();
								for (XWPFRun fromRun : runList) {
									XWPFRun toRun = startPar.createRun();
									isFirst = replaceIterateRun(toRun, fromRun, o, iterateStartText, iterateEndText, i,isFirst);
								}
							}
							//删除模版run
							for (int i = iterateEndRunIndex-1; i > iterateStartRunIndex; i--) {
								startPar.removeRun(i);
							}
						}
					}else{
						//移除循环体模板
						for (int i = iterateEndParIndex; i >= iterateStartParIndex; i--) {
							if (isCell){
								cell.removeParagraph(paragraphs.indexOf(paragraphs.get(i)));
							}else {
								XWPFDocument document = startPar.getDocument();
								document.removeBodyElement(document.getPosOfParagraph(paragraphs.get(i)));
							}
						}
					}
					startParIndex = -1;
				}
			}
			if (isCell){
                List<XWPFParagraph> cellParList = cell.getParagraphs();
                StringBuffer s_cellText = new StringBuffer();
                for (int p = 0; cellParList != null && p < cellParList.size(); p++) {
                    String t_cellText = cellParList.get(p).getText();
                    if (StringUtil.isNotBlank(t_cellText)) {
                        s_cellText.append(t_cellText);
                    }
                }
                String cellText = s_cellText.toString();
				if (!(cellText.indexOf("«Iterate.") > -1 && cellText.indexOf("«/Iterate.") > -1)){
					isContinue = false;
				}
			} else {
				isContinue = false;
				break;
			}
		}
	}

	/**
	 * 特殊处理，处理陕西友谊物种标识和文献对应
	 * @param o
	 * @param map
	 */
	public void dealWzAndRefXh(Object o,Map<String,Object> map,Integer bs){
		try{
			//往sjwzxxDto的yxxh更新标识的值
			String setFieldMethodName = "setCxpx";
			Method setMethod = o.getClass().getMethod(setFieldMethodName, String.class);
			setMethod.invoke(o, String.valueOf(bs));
			Method method = o.getClass().getMethod("getWzid");
			String value = (String) method.invoke(o);
			//将文献与物种标识进行对应并重新排序
			List<WechatReferencesModel> refs= (List<WechatReferencesModel>)map.get("refs_List");
			if(!CollectionUtils.isEmpty(refs)){
				for(WechatReferencesModel ref:refs){
					if(ref.getSpecies_taxid().equals(value)){
						ref.setCxpx(String.valueOf(bs));
					}
				}
				Collections.sort(refs, (o1, o2) -> {
					String cxpx1 = o1.getCxpx();
					String cxpx2 = o2.getCxpx();

					// 如果o1的cxpx是null，返回大于0的数，表示o1应该排在o2之后
					if (cxpx1 == null) {
						return 1;
					}
					// 如果o2的cxpx是null，返回小于0的数，表示o2应该排在o1之后
					else if (cxpx2 == null) {
						return -1;
					}
					// 如果都不是null，则按cxpx的值排序
					else {
						return Integer.valueOf(cxpx1).compareTo(Integer.valueOf(cxpx2));
					}
				});
				StringBuffer str_refs = new StringBuffer("");
				for (int i = 0; i < refs.size(); i++) {
					if(i>0)
						str_refs.append("{br}{\\n}");
					str_refs.append("["+refs.get(i).getCxpx()+"]"+ refs.get(i).getAuthor()+ refs.get(i).getTitle()+ refs.get(i).getJournal());
				}
				map.put("refs",str_refs.toString());
			}
		}catch (NoSuchMethodException ex){
		}catch (Exception e){
			log.error(e.getMessage());
		}
	}

	/**
	 * 根据条件筛选list
	 * @param list
	 * @param judgeText
	 * @return
	 */
	private List<Object> getIterateList(List<Object> list,String judgeText,Map<String,Object> map){
		List<Object> iterateList = new ArrayList<>();

		if(map.get("zswxbs")==null || StringUtil.isBlank(String.valueOf(map.get("zswxbs")))){
			map.put("zswxbs",0);//特殊处理，物种注释和文献标识，初始化为1
		}
		Integer bs=Integer.valueOf(String.valueOf(map.get("zswxbs")));
		boolean check_result=false;

		for (Object o : list) {
			String checkString=judgeText;
			while (true) {
				String secString=checkString.replaceAll(" ","");
				int check_index_yu = secString.indexOf("&&");
				int check_index_huo = secString.indexOf("||");
				if (check_index_yu > -1) {
					secString = secString.substring(0, check_index_yu);//获取判断内容
				}
				if (check_index_huo > -1) {
					secString = secString.substring(0, check_index_huo);//获取判断内容
				}
				//判断是否自带判断条件，目前只提供 == ，！=
				int check_index_eq = secString.indexOf("==");
				int check_index_neq = secString.indexOf("!=");
				String key = "";
				String okValue = "";
				Boolean wzflg=o instanceof SjwzxxDto;
				try {
					if (check_index_eq > -1) {
						key=secString.substring(0,check_index_eq);
						okValue = secString.substring(check_index_eq + 2);
					} else if (check_index_neq > -1) {
						key=secString.substring(0,check_index_neq);
						okValue = secString.substring(check_index_neq + 2);
					} else {
						bs=bs+1;
						map.put("zswxbs",bs);
						iterateList.add(o);
						if(wzflg)
							dealWzAndRefXh(o, map, bs);
						break;
					}

					Method method = o.getClass().getMethod("get" + key.substring(0, 1).toUpperCase() + key.substring(1));
					String value = (String) method.invoke(o);
					check_result = okValue.equals(value);
					if(check_index_huo>-1){
						checkString=checkString.substring(check_index_huo+2);
					}else if (check_index_yu>-1){
						checkString=checkString.substring(check_index_yu+2);
					}
					if (check_index_huo == -1 && check_index_yu == -1 && check_result) {//没有&&或者||
						bs=bs+1;
						map.put("zswxbs",bs);
						iterateList.add(o);
						if(wzflg)
							dealWzAndRefXh(o, map, bs);
						break;
					}else if (check_index_huo == -1 && check_index_yu == -1 && !check_result) {//没有&&或者||
						break;
					}else if (check_index_huo > -1 && check_result) {//如果是||的判断，则只要有false直接break
						bs=bs+1;
						map.put("zswxbs",bs);
						iterateList.add(o);
						if(wzflg)
							dealWzAndRefXh(o, map, bs);
						break;
					} else if (check_index_yu > -1 && !check_result) {//如果是&&的判断，则只要有false直接break，
						break;
					}
				} catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
					log.error("get" + key.substring(0, 1).toUpperCase() + key.substring(1) + "方法不存在或为获取到内容");
				}
			}
		}
		return iterateList;
	}

	/**
	 * 获取段落中指定字符串的Run索引
	 * @param paragraph 段落
	 * @param text 指定字符串
	 * @return run索引
	 */
	private int getRunIndexInPar(XWPFParagraph paragraph, String text) {
		int runIndex = -1;
		List<XWPFRun> runs = paragraph.getRuns();
		if (runs != null && runs.size() > 0) {
			for (int i = 0; i < runs.size(); i++) {
				if (runs.get(i) != null && runs.get(i).text().contains(text)) {
					runIndex = i;
					return runIndex;
				}
			}
		}
		return runIndex;
	}


	/**
	 * 复制run
	 * @param fromRun 从哪个run复制
	 * @param toRun 复制到哪个run
	 * @return
	 */
	private XWPFRun copyRun(XWPFRun fromRun, XWPFRun toRun) {
		toRun.getCTR().setRPr(fromRun.getCTR().getRPr());
		return toRun;
	}

	private XWPFParagraph copyPar(XWPFParagraph fromPar, XWPFParagraph toPar) {
		toPar.getCTP().setPPr(fromPar.getCTP().getPPr());
		return toPar;
	}

	private boolean replaceIterateRun(XWPFRun toRun,XWPFRun fromRun,Object o_dto,String iterateStartText,String iterateEndText,int currentIndex,boolean isFirst) {
		toRun = copyRun(fromRun, toRun);
		String fromText = fromRun.text();
		while (fromText.contains("«list.")){
			if (fromText.contains("«list.pic")){
				String objName = fromText.substring(fromText.indexOf("«list.pic")+9, fromText.indexOf("»"));
				String replaceText = fromText.substring(fromText.indexOf("«list.pic"), fromText.indexOf("»")+1);
				int h = 0;
				int w = 0;
				if (objName.indexOf(".height")>-1){
					objName = objName.substring(objName.indexOf(".height")+7);
					h = Integer.parseInt(objName.substring(0, objName.indexOf(".")));
				}else if (objName.indexOf(".width")>-1){
					objName = objName.substring(objName.indexOf(".width")+6);
					w = Integer.parseInt(objName.substring(0, objName.indexOf(".")));
				}
				objName = objName.substring(objName.indexOf(".")+1);
				String totmName = objName.substring(0,1).toUpperCase()+objName.substring(1);
				//反射方法 把对象中的value插入到数据中
				String value = "";
				try {
					Method method = o_dto.getClass().getMethod("get" + totmName);
					value = (String) method.invoke(o_dto);
				} catch (Exception e) {
					log.error("对象中没有get"+totmName+"方法");
				}
				if (StringUtil.isNotBlank(value)){
					try {
						List<PictureFloat> list = new ArrayList<>();
						File picture = new File(value);
						BufferedImage sourceImg = ImageIO.read(new FileInputStream(picture));
						//获取图片长宽，以及长宽的比列
						int height = sourceImg.getHeight();
						int width = sourceImg.getWidth();
						BigDecimal divide = new BigDecimal(width).divide(new BigDecimal(height), 2, RoundingMode.HALF_EVEN);
						if (h>0){
							height = h;
							width = divide.multiply(new BigDecimal(height)).intValue();
						}
						if (w>0){
							width = w;
							height = new BigDecimal(width).divide(divide, 2, RoundingMode.HALF_EVEN).intValue();
						}
						PictureFloat imageFloat=new PictureFloat(value, width, height, 0, 0);
						list.add(imageFloat);
						overlayPictures(toRun,list);
					} catch (IOException e) {
						log.error("图片替换失败"+e.getMessage());
					}
				}
				fromText = fromText.replace(replaceText, "");
			} else if (fromText.contains("«list.")){
				String objName = fromText.substring(fromText.indexOf("«")+1, fromText.indexOf("»"));
				//反射方法 把对象中的value插入到数据中
				String value = "";
				if (objName.indexOf("?")>-1 && objName.indexOf(":")>-1){
					String judgeText = objName.substring(0, objName.indexOf("?"));
					String trueValue = objName.substring(objName.indexOf("?")+1, objName.indexOf(":"));
					String falseValue = objName.indexOf(":")<objName.length()?objName.substring(objName.indexOf(":")+1):"";
//					String endText = fromText.substring(fromText.indexOf("»")+1);
					//从支持一个条件更改为支持多条件联合，暂不支持不同判断条件的联合 2025/08/18
					List<String> and_condition = new ArrayList<>();
					List<String> or_condition = new ArrayList<>();
					//先判断是否为多个条件同时起效，组成条件判断List
					String[] and_sec = judgeText.split("&&");
					if(and_sec.length>1){
						and_condition.addAll(Arrays.asList(and_sec));
						value = getConditionResult(and_condition,o_dto,1,trueValue,falseValue);
					}else{
						String[] or_sec = judgeText.split("\\|\\|");
						if(or_sec.length>1){
							or_condition.addAll(Arrays.asList(and_sec));
						}else{
							or_condition.add(judgeText);
						}
						value = getConditionResult(or_condition,o_dto,2,trueValue,falseValue);
					}
					//这为了不是一步到位直接替换实际值，而是根据条件只留下相应的 true的变量值（还带有《等），或者false值，然后在后面的处理里再进行实际值的替换
					//如果直接用《包含，则因为可能存在变量以外的内容，所以建议用 {}把变量 包裹起来 ,比如«list.wzzwm==结核分枝杆菌?{list.wzzwm},:» 这种带有逗号分隔的
					if (value.contains("{") && value.contains("}")){
						String pre_const = value.substring(0,value.indexOf("{"));
						String s_var = value.substring(value.indexOf("{") + 1, value.indexOf("}"));
						String after_const = value.substring(value.indexOf("}") + 1);
						fromText = fromText.replace("«"+objName+"»",pre_const + "«"+ s_var+"»" + after_const);
					}else {
						if(StringUtil.isNotBlank(value))
							fromText = fromText.replace("«" + objName + "»", "«" + value + "»");
						else{
							fromText = fromText.replace("«" + objName + "»", "");
						}
					}
					continue;
				}
				//只有在objName 不包含？和：的时候才会进入到这个地方,也就是前面都处理替换完成后，会进入到这个地方进行最后的替换
				objName = objName.replace("list.","");
				String totmName = objName.substring(0,1).toUpperCase()+objName.substring(1);
				//如果是序号，且值为空，则默认值为当前序号
				if (("indexNum".equals(objName) || "indexNumCircle".equals(objName) || "indexNumRoman".equals(objName) || "indexNumChinese".equals(objName)) && StringUtil.isBlank(value)){
					value = String.valueOf(currentIndex+1);
					value = replaceIndexNum(value,objName);
				}else {
					try {
						String addNum = null;
						String subNum = null;
						if (totmName.indexOf("+")>-1){
							addNum = totmName.substring(totmName.indexOf("+")+1);
							totmName = totmName.substring(0,totmName.indexOf("+"));
						}
						if (totmName.indexOf("-")>-1){
							subNum = totmName.substring(totmName.indexOf("+")+1);
							totmName = totmName.substring(0,totmName.indexOf("+"));
						}
						Method method = o_dto.getClass().getMethod("get" + totmName);
						value = (String) method.invoke(o_dto);
						if (StringUtil.isNotBlank(value) && StringUtil.isNotBlank(addNum)){
							BigDecimal bigDecimalvalue = new BigDecimal(value);
							BigDecimal bigDecimaladd = new BigDecimal(addNum);
							BigDecimal endvalue = bigDecimalvalue.add(bigDecimaladd);
							value = endvalue.toString();
						}
						if (StringUtil.isNotBlank(value) && StringUtil.isNotBlank(subNum)){
							BigDecimal bigDecimalvalue = new BigDecimal(value);
							BigDecimal bigDecimalsub = new BigDecimal(subNum);
							BigDecimal endvalue = bigDecimalvalue.subtract(bigDecimalsub);
							value = endvalue.toString();
						}
					} catch (Exception e) {
						log.error("对象中没有get"+totmName+"方法");
					}
				}
				fromText = fromText.replace("«list."+objName+"»", value!=null?value:"");
			} else if (fromText.contains(iterateStartText)) {
				fromText = fromText.replace(iterateStartText, "");
			} else if (fromText.contains(iterateEndText)) {
				fromText = fromText.replace(iterateEndText, "");
			} else {
				//toRun.setText(fromText);
				if(isFirst && fromText.startsWith(",")){
					toRun.setText(fromText.substring(1));
				}else
					toRun.setText(fromText);
			}
		}
		// 循环里重复内容有点难判断，比如多个病原以逗号隔开，但最后一个病原却又不需要逗号，这个时候会多一个逗号。此处暂时做一个临时处理，以后考虑替换掉 2025-08-18
		// 科氏葡萄球菌,胞内分枝杆菌,人疱疹病毒1型,
		if(isFirst && fromText.startsWith(",")){
			toRun.setText(fromText.substring(1));
			isFirst= false;
		}else
			toRun.setText(fromText);
		return isFirst;
	}
	/**
	 * 特殊物种table替换方法；循环单个表格（针对旧模板 一行）
	 * @param table
	 * @param map
	 * @param varString
	 */
	private void replaceSpecialSpeciesTable(XWPFTable table, Map<String, Object> map, String varString) {
		if (varString.indexOf("taxFlg_List.special_flg")>-1){
			Map<String,WeChatInspectionSpeciesModel> specialTaxFlg_list = (Map<String, WeChatInspectionSpeciesModel>) map.get("specialTaxFlg_List");
			if (specialTaxFlg_list!=null && specialTaxFlg_list.size()>0){
//				int size = specialTaxFlg_list.size();
				XWPFTableRow sourceRow = table.getRows().get(2);
				List<String> keys = new ArrayList<>();
				keys.addAll(specialTaxFlg_list.keySet());
				for (int i = keys.size() - 1; i >= 0; i--) {
					WeChatInspectionSpeciesModel valueObj = specialTaxFlg_list.get(keys.get(i));
					XWPFTableRow copyRow = copy(table, sourceRow, 2);
					replaceSpecialSpeciesRow(copyRow, valueObj);
				}
				table.removeRow(table.getRows().size()-1);
			}
		}
	}

	/**
	 * 替换特殊物种row方法
	 * @param row
	 * @param valueObj
	 */
	private void replaceSpecialSpeciesRow(XWPFTableRow row, WeChatInspectionSpeciesModel valueObj) {
		List<XWPFTableCell> cells = row.getTableCells();
		// 插入的行会填充与表格第一行相同的列数
		for (XWPFTableCell cell : cells) {
			String cellText = cell.getText();
			if (StringUtil.isNotBlank(cellText)) {
				List<XWPFParagraph> paragraphs = cell.getParagraphs();
				if (paragraphs != null && paragraphs.size() > 0) {
					for (XWPFParagraph paragraph : paragraphs) {
						List<XWPFRun> runs = paragraph.getRuns();
						if (runs != null && runs.size() > 0) {
							for (XWPFRun run : runs) {
								String text = run.text();
								if (StringUtil.isNotBlank(text) && text.indexOf("«taxFlg_List.special_flg.")>-1){
									String taxFlgKey = text.substring(text.indexOf("«taxFlg_List.special_flg.")+"«taxFlg_List.special_flg.".length(),text.indexOf("»"));
									String value = "";
									if(valueObj!=null){
										String TotmpVar = taxFlgKey.substring(0, 1).toUpperCase() + taxFlgKey.substring(1);
										try {
											Method t_method = valueObj.getClass().getMethod("get" + TotmpVar);
											value = (String) t_method.invoke(valueObj);
										} catch (Exception e) {
											log.error("对象中没有get"+TotmpVar+"方法");
										}
										text = text.replace("«taxFlg_List.special_flg."+taxFlgKey+"»", value);
									}
									run.setText(text,0);
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 转换数字格式
	 * @param num
	 * @param type
	 * indexNumCircle 1-20转换成①-⑳
	 * indexNumRoman 1-3999转换成罗马数字
	 * indexNumChinese 1-10000转换成中文数字
	 * @return
	 */
	public String replaceIndexNum(String num, String type){
		if ("indexNumCircle".equals(type)){
			//将序号转换成特殊符号数字，将1-9转换成①-⑨
			if ("1".equals(num)){
				num = num.replace("1","①");
			} else if ("2".equals(num)) {
				num = num.replace("2","②");
			} else if ("3".equals(num)) {
				num = num.replace("3","③");
			} else if ("4".equals(num)) {
				num = num.replace("4","④");
			} else if ("5".equals(num)) {
				num = num.replace("5","⑤");
			} else if ("6".equals(num)) {
				num = num.replace("6","⑥");
			} else if ("7".equals(num)) {
				num = num.replace("7","⑦");
			} else if ("8".equals(num)) {
				num = num.replace("8","⑧");
			} else if ("9".equals(num)) {
				num = num.replace("9","⑨");
			} else if ("10".equals(num)) {
				num = num.replace("10","⑩");
			} else if ("11".equals(num)) {
				num = num.replace("11","⑪");
			} else if ("12".equals(num)) {
				num = num.replace("12","⑫");
			} else if ("13".equals(num)) {
				num = num.replace("13","⑬");
			} else if ("14".equals(num)) {
				num = num.replace("14","⑭");
			} else if ("15".equals(num)) {
				num = num.replace("15","⑮");
			} else if ("16".equals(num)) {
				num = num.replace("16","⑯");
			} else if ("17".equals(num)) {
				num = num.replace("17","⑰");
			} else if ("18".equals(num)) {
				num = num.replace("18","⑱");
			} else if ("19".equals(num)) {
				num = num.replace("19","⑲");
			} else if ("20".equals(num)) {
				num = num.replace("20","⑳");
			}
		} else if ("indexNumRoman".equals(type)) {
			//将序号转换成罗马数字
			int romanNum = Integer.parseInt(num);
			if (romanNum >= 1 && romanNum <= 3999) {
				int[] DIGITS = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
				String[] ROMAN_NAMES = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
				StringBuilder result = new StringBuilder();
				int lastIndex = 0;
				for (int i = 0; i < DIGITS.length; i++) {
					int currentIndex = Math.max(lastIndex, i);
					while (romanNum >= DIGITS[currentIndex]) {
						result.append(ROMAN_NAMES[currentIndex]);
						romanNum -= DIGITS[currentIndex];
						lastIndex = currentIndex;
					}
				}
				num = result.reverse().toString();
			}
		} else if ("indexNumChinese".equals(type)) {
			String[] NUMBER_LIST = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
			String[] SYMBOLS = {"十", "百", "千", "万"};
			int number = Integer.parseInt(num);
			if (number >= 0 && number <= 10000) {
				StringBuilder result = new StringBuilder();
				// 判断数字的位数
				if (number == 0) {
					result.append("零");
				} else if (number < 0) {
					result.append("负");
					number = -number;
				} else if (number >= 10000) {
					int i = 0;
					while (number >= 1000) {
						result.append(SYMBOLS[i++]);
						number -= 1000;
					}
					result.append(NUMBER_LIST[i]);
				} else if (number >= 100) {
					int i = 0;
					while (number >= 100) {
						result.append(SYMBOLS[i++]);
						number -= 100;
					}
					result.append(NUMBER_LIST[i]);
				} else if (number >= 10) {
					int i = 0;
					while (number >= 10) {
						result.append(SYMBOLS[i++]);
						number -= 10;
					}
					result.append(NUMBER_LIST[i]);
				} else {
					result.append(NUMBER_LIST[number]);
				}
				num = result.reverse().toString();
			}
		}
		return num;
	}
}