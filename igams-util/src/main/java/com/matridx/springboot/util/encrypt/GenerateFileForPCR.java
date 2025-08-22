package com.matridx.springboot.util.encrypt;

import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class GenerateFileForPCR {
	private static final Logger log = LoggerFactory.getLogger(GenerateFileForPCR.class);
//	public static final int INPUT_NAME_MAX_LEN = 128;// 最大通道
	public static final int MAX_CHANNE = 6;// 最大通道
	
	public static String createFile(String exportFilePath ,String mechineID, String wkmc, String wjlj ,List<Plate> listPlate) throws IOException {
		Logger log = LoggerFactory.getLogger(GenerateFileForPCR.class);
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");//设置日期格式，用于生成对接文档里面的参数
		SimpleDateFormat dff = new SimpleDateFormat("yyyy_MM_dd_HHmmss");

		String time  = df.format(new Date());
		
		String timename = dff.format(new Date());
		String[] s = timename.split("_");
		String year = s[0];
		String yearMonuth = s[0]+s[1];
		String date = s[0]+s[1]+s[2];
		String hms = s[3];
		String targetFilePath = exportFilePath + year+"/"+yearMonuth+"/"+date+"/"+hms+"WK_"+wkmc+".tlp27";
		File file = new File(targetFilePath);
		File fileParent = file.getParentFile();
		if(!fileParent.exists()) {
			fileParent.mkdirs();
		}

        //		String targetFilePath = "E:\\Documents\\MIinstrument\\tPCRfile.tlp27";
//		targetFilePath = "ftp://172.17.50.57/xxx/tPCRfile.tlp27";
		FileWriter out = new FileWriter(targetFilePath);
		BufferedReader read = new BufferedReader(new FileReader(wjlj));
		
		String str;
		while ((str = read.readLine()) != null) {
			if (str.contains("InstruType=")) {// 处理时间
				String instrutype="InstruType="+mechineID;
				out.write(instrutype);
				out.write("\r\n");
				out.flush();
			}else if(str.contains("datetime")) {// 处理时间
				String timestr="datetime="+time;
				out.write(timestr);
				out.write("\r\n");
				out.flush();
			}else if (str.contains("WELL")) {
				String wellstr;
				int m=0, i=0;
				for (int n = 0; n < 96; n++) {
					String front = "WELL" + "\\" + (n+1) + "\\" + "value" + "=";
					if(m<listPlate.size()) {
						if(	String.valueOf(n+1).equals(listPlate.get(m).getXh()) ) {
						    wellstr = createWELL(listPlate.get(m));
						    m++;
					    }else {
					    	wellstr = rightAddFormat("",2960,"0");
					    }
					}else {
						if( 89<=n+1 && n+1<= 94 ) {
							List<Plate> stdlist = createStandard();
							Plate plateStd = stdlist.get(i++);//89位置的孔
							wellstr = createWELL(plateStd);
						}else {
							wellstr = rightAddFormat("",2960,"0");
						}
					}
					String rowstr = front + wellstr;
					out.write(rowstr);
					out.write("\r\n");
					out.flush();
				}
				String lastrow = "WELL\\size=" + 96;
				out.write(lastrow);
				out.flush();
				break;
			}else {
				out.write(str);
				out.write("\r\n");
				out.flush();
			}
		}
		out.flush();
		out.close();
		read.close();
		log.error("GenerateFileForPCR---createFile："+targetFilePath);
		return targetFilePath;
	}
	
	public static String createFileForYz(String exportFilePath , String mechineID, String wjlj , List<Plate> listPlate) throws IOException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");//设置日期格式，用于生成对接文档里面的参数
		SimpleDateFormat dff = new SimpleDateFormat("yyyy_MM_dd_HHmmss");
		String time  = df.format(new Date());
		String timename = dff.format(new Date());
		String[] s = timename.split("_");
		String year = s[0];
		String yearMonuth = s[0]+s[1];
		String date = s[0]+s[1]+s[2];
		String hms = s[3];
		String targetFilePath = exportFilePath + year+"/"+yearMonuth+"/"+date+"/"+"YZ_"+date+"_"+hms+".tlpp";
		File file = new File(targetFilePath);
		File fileParent = file.getParentFile();
		if(!fileParent.exists()) {
			fileParent.mkdirs();
		}
        FileWriter out = new FileWriter(targetFilePath);
		BufferedReader read = new BufferedReader(new FileReader(wjlj));
		String str="";
		//先遍历模板内容获取到对应模板信息
		String normal_jh="";//普通标本+结核
		String normal_ys="";//普通标本+耶式
		String special_jh="";//特殊标本(NC,PC)+结核
		String special_ys="";//特殊标本(NC,PC)+耶式
		String nullString="";//无标本
		while ((str = read.readLine()) != null) {
			if (str.startsWith("Well\\")) {
				String[] s_keyvalue = str.split("=");
				String[] s_str = s_keyvalue[0].split("\\\\");
				if(s_str.length == 3&&"1".equals(s_str[1])){//普通标本+结核
					normal_jh=s_keyvalue[1];
				}else if(s_str.length == 3&&"2".equals(s_str[1])){//普通标本+耶式
					normal_ys=s_keyvalue[1];
				}else if(s_str.length == 3&&"3".equals(s_str[1])){//普通标本+耶式
					nullString=s_keyvalue[1];
				}else if(s_str.length == 3&&"13".equals(s_str[1])){//特殊标本(NC,PC)+结核
					special_jh=s_keyvalue[1];
				}else if(s_str.length == 3&&"14".equals(s_str[1])){//特殊标本(NC,PC)+耶式
					special_ys=s_keyvalue[1];
				}
				out.flush();
			}
		}
		read = new BufferedReader(new FileReader(wjlj));
		while ((str = read.readLine()) != null) {
			if (str.contains("InstruType=")) {// 处理时间
				String instrutype="InstruType="+mechineID;
				out.write(instrutype);
				out.write("\r\n");
				out.flush();
			}else if (str.contains("FileName=")){
				String txtContent = "FileName=" + "YZ_"+date+"_"+hms;
				out.write(txtContent);
				out.write("\r\n");
				out.flush();
			}else if (str.contains("CreateDate=")){
				String txtContent = "CreateDate=" + time;
				out.write(txtContent);
				out.write("\r\n");
				out.flush();
			}else if (str.contains("ModifyDate=")){
				String txtContent = "ModifyDate=" + time;
				out.write(txtContent);
				out.write("\r\n");
				out.flush();
			}else if (str.startsWith("Well")&&str.split("=")[0].split("\\\\").length==3&&"Value".equals(str.split("=")[0].split("\\\\")[2])) {
				String[] s_keyvalue = str.split("=");
				String[] s_str = s_keyvalue[0].split("\\\\");
				String wellstr=s_keyvalue[0]+"="+nullString;
				for (Plate plate : listPlate) {
					if (s_str[1].equals(plate.getXh())) {
						if("MTM".equals(plate.getSampleType())){
							if("normal".equals(plate.getType())){
								if(StringUtil.isNotBlank(normal_jh)){
									wellstr = assemble(s_keyvalue[0]+"="+normal_jh, plate,"0101");
								}
							}else{
								if("negative".equals(plate.getType())){
									if(StringUtil.isNotBlank(special_jh)){
										wellstr = assemble(s_keyvalue[0]+"="+special_jh, plate,"0104");
									}
								}else{
									if(StringUtil.isNotBlank(special_jh)){
										wellstr = assemble(s_keyvalue[0]+"="+special_jh, plate,"0103");
									}
								}
							}
						}else{
							if("normal".equals(plate.getType())){
								if(StringUtil.isNotBlank(normal_ys)){
									wellstr = assemble(s_keyvalue[0]+"="+normal_ys, plate,"0101");
								}
							}else{
								if("negative".equals(plate.getType())){
									if(StringUtil.isNotBlank(special_ys)){
										wellstr = assemble(s_keyvalue[0]+"="+special_ys, plate,"0104");
									}
								}else{
									if(StringUtil.isNotBlank(special_jh)){
										wellstr = assemble(s_keyvalue[0]+"="+special_ys, plate,"0103");
									}
								}
							}
						}
					}
				}
				out.write(wellstr);
				out.write("\r\n");
				out.flush();
			}else {
				out.write(str);
				out.write("\r\n");
				out.flush();
			}
		}
		out.flush();
		out.close();
		read.close();
		log.error("GenerateFileForPCR---createFileForYz："+targetFilePath);
		return targetFilePath;
	}

	public static String assemble(String str, Plate plate,String sampleCode){
		String[] s_keyvalue = str.split("=");
		String s_pre = s_keyvalue[1].substring(4, 24);
		StringBuilder sb_code = new StringBuilder();
		if(StringUtil.isNotBlank(plate.getSampleName())){
			byte[] b_str = plate.getSampleName().getBytes();
			for (byte b : b_str) {
				String code = Integer.toHexString(b);//字符串ASCIIstr2 为对应的ASCII字符串
				if (code.length() >= 2) {
					code = code.substring(code.length() - 2);
				} else {
					code = String.format("%2s", code).replace(" ", "0");
				}
				sb_code.append(code.toUpperCase());
			}
		}
		String s_code = String.format("%-" + 24 + "s", sb_code).replace(" ", "0");
		StringBuilder last_code = new StringBuilder();
		byte[] last_str = plate.getSampleUid().getBytes();
		for (byte b : last_str) {
			String code = Integer.toHexString(b);//字符串ASCIIstr2 为对应的ASCII字符串
			if (code.length() >= 2) {
				code = code.substring(code.length() - 2);
			} else {
				code = String.format("%2s", code).replace(" ", "0");
			}
			last_code.append(code.toUpperCase());
		}
		String after_code = String.format("%-" + 40 + "s", last_code).replace(" ", "0");
		String s_after = s_keyvalue[1].substring(48,216);
		String last_after = s_keyvalue[1].substring(256);
		return s_keyvalue[0] + "=" +sampleCode+ s_pre + s_code + s_after+after_code+last_after;
	}
	
	//生成文库使用的标准标本和阴性标本信息
	private static List<Plate> createStandard() {
		List<Plate> plateList = new ArrayList<>();
		
		String[] concentrationIsSet = {"10","00","00","00","00","00"};
		String[] channelisenable = {"10","00","00","00","00","00"};//FAM,ROX{"10","00","31","00","00","00"}
		
		String[] concentration = new String[6];
		String[] concentration2 = new String[6];
		String[] concentration3 = new String[6];
		String[] concentration4 = new String[6];
		String[] concentration5 = new String[6];
		concentration[0] = "2000";
		concentration2[0] = "200";
		concentration3[0] = "20";
		concentration4[0] = "2";
		concentration5[0] = "0.2";
		
		Plate standard1 = new Plate();
		standard1.setType("ST_Standard1");
		standard1.setXh("89");
		standard1.setConcentration(concentration);
		standard1.setConcentrationIsSet(concentrationIsSet);
		standard1.setChannelIsEnable(channelisenable);
		
		Plate standard2 = new Plate();
		standard2.setType("ST_Standard1");
		standard2.setXh("90");
		standard2.setConcentration(concentration2);
		standard2.setConcentrationIsSet(concentrationIsSet);
		standard2.setChannelIsEnable(channelisenable);
        
		Plate standard3 = new Plate();
		standard3.setType("ST_Standard1");
		standard3.setXh("91");
		standard3.setConcentration(concentration3);
		standard3.setConcentrationIsSet(concentrationIsSet);
		standard3.setChannelIsEnable(channelisenable);
		
		Plate standard4 = new Plate();
		standard4.setType("ST_Standard1");
		standard4.setXh("92");
		standard4.setConcentration(concentration4);
		standard4.setConcentrationIsSet(concentrationIsSet);
		standard4.setChannelIsEnable(channelisenable);
		
		Plate standard5 = new Plate();
		standard5.setType("ST_Standard1");
		standard5.setXh("93");
		standard5.setConcentration(concentration5);
		standard5.setConcentrationIsSet(concentrationIsSet);
		standard5.setChannelIsEnable(channelisenable);
		
		Plate negative = new Plate();
		negative.setType("ST_Negative");
		negative.setXh("95");
		String[] str_N = {"10","00","00","00","00","00"};//FAM,ROX{"10","00","31","00","00","00"}
		negative.setChannelIsEnable(str_N);
		
		plateList.add(standard1);
		plateList.add(standard2);
		plateList.add(standard3);
		plateList.add(standard4);
		plateList.add(standard5);
		plateList.add(negative);
		
		return plateList;
	}
		
	
	//生成well数据
	public static String createWELL(Plate plate) {
		String ybbhstr, wybmstr, ybmcstr;
		String[] targetItemsStr = new String[6];
		String ybbh = plate.getSampleUid();  String wybm = plate.getUid();
		String ybmc = plate.getSampleName();
		String[] targetItems = plate.getTargetItem();

		//基于模板（为固定的值）
		Plate res = new Plate();
		res.setEnable("1");
		plate.setEnable("1");

		if ("1".equals(plate.getEnable())) {
			//处理ybbh，wybm，ybmc为null的情况
			if(StringUtil.isBlank(ybbh)) {
				ybbhstr = rightAddFormat("",128*2,"0");
			}else {
				ybbhstr = StringToHexStr(ybbh, "UTF-8");// 标本编号
			}
			if(StringUtil.isBlank(wybm)) {
				wybmstr = rightAddFormat("",128*2,"0");
			}else {
				wybmstr = StringToHexStr(wybm, "UTF-8");// 唯一编码
			}
			if(StringUtil.isBlank(ybmc)) {
				ybmcstr = rightAddFormat("",128*2,"0");
			}else {
				ybmcstr = StringToHexStr(ybmc, "UTF-8");// 标本名称
			}
			for (int i=0; i<targetItems.length; i++){
				if(StringUtil.isNotBlank(targetItems[i])){
					targetItemsStr[i] = StringToHexStr(targetItems[i], "UTF-8");
				}
			}
			res.setUid(wybmstr);
			res.setSampleUid(ybbhstr);
			res.setSampleName(ybmcstr);

			switch (plate.getType()) {
			case "ST_Standard1":
			case "ST_Standard2":
			case "ST_Standard3":
			case "ST_Standard4":
				res.setSampleType("2");
				break;
			case "ST_StrongPositive":
			case "ST_WeakPositive":
				res.setSampleType("3");
				break;
			case "ST_Negative":
				res.setSampleType("4");
				break;
			default: res.setSampleType("1");
				break;
			}
			res.setChannelIsEnable(plate.getChannelIsEnable());
			res.setTargetItem(targetItemsStr);
			res.setConcentration(plate.getConcentration());
			res.setConcentrationIsSet(plate.getConcentrationIsSet());

		}
        return dealPlate(res);
	}
	
	//处理Dto的位数
	public static String dealPlate(Plate plate) {
		String sampleName = plate.getSampleName();//c
		String primerName = plate.getPrimerName();//c
		String remark = plate.getRemark(); //c
		String uid = plate.getUid();//c
		String sampleUid = plate.getSampleUid();//c
	    String[] targetItem = plate.getTargetItem(); //c
		String[] channelIsEnable = plate.getChannelIsEnable();
		String[] concentrationIsSet = plate.getConcentrationIsSet();//b
		String showCurve = plate.getShowCurve();//b
		String enable = plate.getEnable();//b
		String sampleType = plate.getSampleType(); //q16
		String[] concentration = plate.getConcentration();//double
	    String groupNo = plate.getGroupNo(); //q16


        primerName = rightAddFormat(Objects.requireNonNullElse(primerName, ""), 128 * 2, "0");

        remark = rightAddFormat(Objects.requireNonNullElse(remark, ""), 128 * 2, "0");

        uid = rightAddFormat(Objects.requireNonNullElse(uid, ""), 128 * 2, "0");

        sampleUid = rightAddFormat(Objects.requireNonNullElse(sampleUid, ""), 128 * 2, "0");
		
		if(targetItem == null || targetItem.length<1) {
			for(int i=0; i<MAX_CHANNE; i++) {
				targetItem[i] = rightAddFormat("",128*2,"0");
			}	
		}else {
			for(int i=0; i<MAX_CHANNE; i++) {
				if(targetItem[i] == null) {
					targetItem[i] = rightAddFormat("",128*2,"0");
				}else {
					targetItem[i] = rightAddFormat(targetItem[i],128*2,"0");//
				}
			}
		}
		
		if(channelIsEnable == null || channelIsEnable.length<1) {
			for(int i=0; i<MAX_CHANNE; i++) {
				channelIsEnable[i] = rightAddFormat("", 2,"0");
			}		
		}else {
			for(int i=0; i<MAX_CHANNE; i++) {
				if(channelIsEnable[i] == null) {
					channelIsEnable[i] = rightAddFormat("", 2,"0");
				}else {
					channelIsEnable[i] = rightAddFormat(channelIsEnable[i],2,"0");//
				}
			}
		}
		
		if(concentrationIsSet == null || concentrationIsSet.length<1) {
			for(int i=0; i<MAX_CHANNE; i++) {
				concentrationIsSet[i] = rightAddFormat("", 2,"0");
			}		
		}else {
			for(int i=0; i<MAX_CHANNE; i++) {
				if(concentrationIsSet[i] == null) {
					concentrationIsSet[i] = rightAddFormat("", 2,"0");
				}else {
					concentrationIsSet[i] = rightAddFormat(concentrationIsSet[i],2,"0");//
				}
			}
		}

        showCurve = rightAddFormat(Objects.requireNonNullElse(showCurve, ""), 2, "0");

        enable = rightAddFormat(Objects.requireNonNullElse(enable, ""), 2, "0");
		
		if(sampleType == null) {
			sampleType = rightAddFormat("",2*2,"0");
		}else {
			short sampletypeShort = Short.parseShort(sampleType);
			sampleType = ShortToHexStr(sampletypeShort, 2, "little");
		}
		
		if(concentration == null || concentration.length<1) {
			for(int i=0; i<MAX_CHANNE; i++) {
				concentration[i] = rightAddFormat("",8*2,"0");
			}		
		}else {
			for(int i=0; i<MAX_CHANNE; i++) {
				if(concentration[i] == null) {
					concentration[i] = rightAddFormat("",8*2,"0");
				}else {
					byte[] concentrationByte = double2Bytes(Double.parseDouble(concentration[i]));
					String concenString = StringUtil.bytesToHex(concentrationByte, "");
					concentration[i] = rightAddFormat(concenString,8*2,"0");
				}
			}
		}
		
		if(groupNo == null) {
			groupNo = rightAddFormat("",2*2,"0");
		}else {
			short groupNoShort = Short.parseShort(groupNo);
			groupNo = ShortToHexStr(groupNoShort, 2, "little");
		}
		
		return sampleName+primerName+remark+uid+sampleUid+targetItem[0]+targetItem[1]
				+targetItem[2]+targetItem[3]+targetItem[4]+targetItem[5]+channelIsEnable[0]
				+channelIsEnable[1]+channelIsEnable[2]+channelIsEnable[3]+channelIsEnable[4]
				+channelIsEnable[5]+concentrationIsSet[0]+concentrationIsSet[1]+concentrationIsSet[2]
				+concentrationIsSet[3]+concentrationIsSet[4]+concentrationIsSet[5]+showCurve
				+enable+sampleType+concentration[0]+concentration[1]+concentration[2]+concentration[3]
				+concentration[4]+concentration[5]+groupNo+"000000000000";
		
//		return sampleName+primerName+remark+uid+sampleUid+targetItem[0]+targetItem[1]
//				+targetItem[2]+targetItem[3]+targetItem[4]+targetItem[5]+channelIsEnable[0]
//				+channelIsEnable[1]+channelIsEnable[2]+channelIsEnable[3]+channelIsEnable[4]
//				+channelIsEnable[5]+concentrationIsSet[0]+concentrationIsSet[1]+concentrationIsSet[2]
//				+concentrationIsSet[3]+concentrationIsSet[4]+concentrationIsSet[5]+showCurve
//				+enable+sampleType+"concentration0:"+concentration[0]+"concentration1:"+concentration[1]+"concentration2:"+concentration[2]+"concentration3:"+concentration[3]
//				+"concentration4:"+concentration[4]+"concentration5:"+concentration[5]+"groupNo:"+groupNo+"000000000000";
	}

	//测试文档
	public static String createFileTest(String mechineID, String wjlj ,List<Plate> listPlate) throws IOException {
		Logger log = LoggerFactory.getLogger(GenerateFileForPCR.class);
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");//设置日期格式，用于生成对接文档里面的参数

		String time  = df.format(new Date());

		String targetFilePath = "E:\\yh\\tPCRfile.tlp27";
//		String targetFilePath = exportFilePath + year+"/"+yearMonuth+"/"+date+"/"+hms+"WK_"+wkmc+".tlp27";
		File file = new File(targetFilePath);
		File fileParent = file.getParentFile();
		if(!fileParent.exists()) {
			fileParent.mkdirs();
		}

        FileWriter out = new FileWriter(targetFilePath);
		BufferedReader read = new BufferedReader(new FileReader(wjlj));

		String str;
		while ((str = read.readLine()) != null) {
			if (str.contains("InstruType=")) {// 处理时间
				String instrutype="InstruType="+mechineID;
				out.write(instrutype);
				out.write("\r\n");
				out.flush();
			}else if(str.contains("datetime")) {// 处理时间
				String timestr="datetime="+time;
				out.write(timestr);
				out.write("\r\n");
				out.flush();
			}else if (str.contains("WELL")) {
				String wellstr;
				int m=0, i=0;
				for (int n = 0; n < 96; n++) {
					String front = "WELL" + "\\" + (n+1) + "\\" + "value" + "=";
					if(m<listPlate.size()) {
						if(	String.valueOf(n+1).equals(listPlate.get(m).getXh()) ) {
							wellstr = createWELL(listPlate.get(m));
							m++;
						}else {
							wellstr = rightAddFormat("",2960,"0");
						}
					}else {
						if( 89<=n+1 && n+1<= 94 ) {
							List<Plate> stdlist = createStandard();
							Plate plateStd = stdlist.get(i++);//89位置的孔
							wellstr = createWELL(plateStd);
						}else {
							wellstr = rightAddFormat("",2960,"0");
						}
					}
					String rowstr = front + wellstr;
					out.write(rowstr);
					out.write("\r\n");
					out.flush();
				}
				String lastrow = "WELL\\size=" + 96;
				out.write(lastrow);
				out.flush();
				break;
			}else {
				out.write(str);
				out.write("\r\n");
				out.flush();
			}
		}
		out.flush();
		out.close();
		read.close();
		log.error("GenerateFileForPCR---createFile："+targetFilePath);
		return targetFilePath;
	}


	public static void main(String[] args) throws Exception {
		List<Plate> listplate = new ArrayList<>();

		Plate plate1 = new Plate();
//        plate1.setSampleUid("TL0000");
//        plate1.setUid("TL0000");
        plate1.setSampleName("申双美");
        plate1.setEnable("1");
        plate1.setType("UNK");
        plate1.setXh("9");
//        String[] str1 = {"10","20","31","10","10","10"};
        String[] str1 = {"00","00","00","00","10","10"};
        plate1.setChannelIsEnable(str1);
		String[] xx= new String[6];
		xx[0]="非结核";
		xx[1]="IC";
		xx[2]="xxx";
		xx[3]="ddd";
		xx[4]="eee";
		xx[5]="fff";
		plate1.setTargetItem(xx);
        listplate.add(plate1);

        Plate plate2 = new Plate();
        plate2.setSampleName("HCV");
        plate2.setEnable("1");
        plate2.setType("UNK");
        plate2.setXh("10");
        String[] str2 = {"10","00","31","00","50","00"};
        plate2.setChannelIsEnable(str2);
        listplate.add(plate2);

        Plate plate3 = new Plate();
        plate3.setSampleName("HCV");
        plate3.setEnable("1");
        plate3.setType("ST_Standard1");
        plate3.setXh("11");
        String[] str3 = {"10","00","31","00","00","00"};
        plate3.setChannelIsEnable(str3);
        listplate.add(plate3);

		createFileTest("3","E:\\yh\\YZ_TEMPLATE.tlp27",listplate);

//	  String r = HexStrToSting("302e303200", "UTF-8");
//	  System.out.println("之前多一位字节的文件字符---"+r);
//
//	  String str = StringToHexStr("FAM","UTF-8");
//	  System.out.println("HCV字符转为HexStr数据：        "+str);
//
//	  Double db  =1.03;
//	  System.out.println(StringUtil.bytesToHex(double2Bytes(1.03),""));
	}
	
	/**
	 * double类型转化为byte数组
	 */
	public static byte[] double2Bytes(double d) {
	    long value = Double.doubleToRawLongBits(d);
	    byte[] byteRet = new byte[8];
	    for (int i = 0; i < 8; i++) {
	      byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
	    }    
	    return byteRet;
	  }

	/**
	 * 根据位数右边补充相应的字符
	 * 
	 * @param src     原始字符串
	 * @param length  规定位数
	 * @param addchar 添加的字符
	 */
	public static String rightAddFormat(String src, int length, String addchar) {
		StringBuilder result = new StringBuilder();

		if (src == null )
			src="";
		if (src.length() >= length)
			return src;
		else {
            result.append(String.valueOf(addchar).repeat(length - src.length()));
			result.insert(0, src);
		}
		return result.toString();
	}
	
	/**
	 * 根据位数左边补充相应的字符
	 * 
	 * @param src     原始字符串
	 * @param length  规定位数
	 * @param addchar 添加的字符
	 */
	public static String leftAddFormat(String src, int length, String addchar) {
		StringBuilder result = new StringBuilder();

		if (src.length() >= length)
			return src;
		else {
            result.append(String.valueOf(addchar).repeat(length - src.length()));
			result.append(src);
		}
		return result.toString();
	}

	/**
	 * 字符串转16进制字符串
	 * 
	 * @param str         普通字符串
	 * @param charsetName 编码名称
	 * @return 16进制字符串
	 */
	public static String StringToHexStr(String str, String charsetName) {
		try {
			byte[] conver = str.getBytes(charsetName);
			StringBuilder sb = new StringBuilder();
            for (byte b : conver) {
                sb.append(String.format("%02x", b));
            }
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
			return null;
		}
	}

	//16进制字符串转字符串
	public static String HexStrToSting(String hexStr, String charsetName) {
		byte[] conver = HexStrToByteArray(hexStr);
		return ByteArrayToSting(conver,charsetName);
	}
	
	//16进制字符串转字节数组
	public static byte[] HexStrToByteArray(String hexStr) {
		int strlen = hexStr.length();
		if(strlen % 2 !=0) {
			return null;
		}
		byte[] conver = new byte[strlen / 2];
		for(int i=0; i <strlen; i++) {
			if(i % 2 == 0) {
				conver[i/2] = (byte) Integer.parseInt(hexStr.substring(i,i+2),16);
			}
		}
		return conver;
	}
	
	/**
	 * 字节数组转字符串
	 * 
	 * @param b           字节数组
	 * @param charsetName 编码名称
	 * @return 普通字符串
	 */
	public static String ByteArrayToSting(byte[] b, String charsetName) {
		String str = null;
		try {
			str = new String(b, charsetName);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
		return str;
	}

    /**
              * 整数转字节数组
     * @param num 需要转化的整数
     * @param size 转化字节大小
     * @param order 字节顺序
     * @return 字节数组
     */
    public static byte[] ShortToByteArray(Short num,int size,String order){
        byte[] conver = new byte[size];
        if ("little".equals(order)){
            // 低字节序
            for (int i = 0; i < size; i++) {
                conver[i] = (byte) (num >> i*8 & 0xff);
            }
        }else if ("big".equals(order)){
            // 高字节序
            for (int i = 0; i < size; i++) {
                conver[size-(i+1)] = (byte) (num >> i*8 & 0xff);
            }
        }else {
            return null;
        }
        return conver;
    }
    
    /**
                * 整数转16进制字符串
     * @param num 待转整数
     * @param size 字节大小
     * @param order 字节顺序
     * @return 16进制字符串
     */
    public static String ShortToHexStr(Short num,int size,String order){
        byte[] conver = ShortToByteArray(num,size,order);
        return ByteArrayToHexStr(conver);
    }
    
    /**
     * 字节数组转16进制字符串
     * @param b 字节数组
     * @return 十六进制字符串
     */
    public static String ByteArrayToHexStr(byte[] b){
        StringBuilder sb = new StringBuilder();
        for (byte con : b) {
            sb.append(String.format("%02x", con));
        }
        return sb.toString();
    }

    /**
     * long数据得到byte数组，double先转为long再求byte数组
     */
    public static byte[] getBytes(long data)  
    {  
        byte[] bytes = new byte[8];  
        bytes[0] = (byte) (data & 0xff);  
        bytes[1] = (byte) ((data >> 8) & 0xff);  
        bytes[2] = (byte) ((data >> 16) & 0xff);  
        bytes[3] = (byte) ((data >> 24) & 0xff);  
        bytes[4] = (byte) ((data >> 32) & 0xff);  
        bytes[5] = (byte) ((data >> 40) & 0xff);  
        bytes[6] = (byte) ((data >> 48) & 0xff);  
        bytes[7] = (byte) ((data >> 56) & 0xff);  
        return bytes;  
    }  
  
    /**
     * 传入double数据，得到它的byte数组
     */
    public static byte[] getBytes(double data)  
    {  
        long intBits = Double.doubleToLongBits(data);  
        return getBytes(intBits);  
    } 

	//运行设置和模板的整合处理
	public static void dealRunMethod() throws IOException {
//		String sourceFilePath = "E:\\ssm\\PCRsource\\runmethod\\test.tlp27";//原完整模板
		String targetFilePath = "E:\\ssm\\PCRsource\\runmethod\\res.tlp27";	//生成的文件	
//		String sourceRunMethodPath = "E:\\ssm\\PCRsource\\runmethod\\method.tlp27";	//运行参数模板文件
		//文库运行参数模板
		String sourceRunMethodPath = "/matridx/fileupload/release/IMP_PCR_TEMEPLATE/UP2021/UP202106/UP20210610/1623307220431TDIAgs.tlp27";
		//PCR对接文件模板
		String sourceFilePath = "/matridx/fileupload/release/IMP_PCR_TEMEPLATE/UP2021/UP202106/UP20210610/1623307288501EpommF.tlp27";

		FileWriter out = new FileWriter(targetFilePath);
		BufferedReader readwhole = new BufferedReader(new FileReader(sourceFilePath));
		BufferedReader readmethod = new BufferedReader(new FileReader(sourceRunMethodPath));
		
		String str;
		while ((str = readwhole.readLine()) != null) {
			if (str.contains("[RunMethod]")) {// 处理时间
				String str2;
				while ((str2 = readmethod.readLine()) != null) {
					out.write(str2);
					out.write("\r\n");
				}
				out.flush();
				readmethod.close();
			}else {
				out.write(str);
				out.write("\r\n");
				out.flush();
			}
		}
		out.flush();
		out.close();
		readwhole.close();
	}

}