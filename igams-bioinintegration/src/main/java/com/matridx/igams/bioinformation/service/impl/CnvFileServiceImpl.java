package com.matridx.igams.bioinformation.service.impl;


import com.matridx.igams.bioinformation.dao.entities.*;
import com.matridx.igams.bioinformation.service.svcinterface.*;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CnvFileServiceImpl  implements ICnvFileService {
	@Autowired
	ICnvjgService cnvjgService;
	@Autowired
    IBioXpxxService xpxxService;
	@Autowired
	ICnvjgxqService cnvjgxqService;
	@Autowired
	IWkcxService wkcxService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IOtherService otherService;



	/***
	 *
	 * @param receiveFileModel 多个文件路径的json
	 * @param date 接收时间
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String,Object> FileParseingByType(ReceiveFileModel receiveFileModel, String date) throws Exception {
		Map<String,Object> map=new HashMap<>();
		if(StringUtil.isNotBlank(receiveFileModel.getQcPath())&&StringUtil.isBlank(receiveFileModel.getSample())){
			try {
				FileParseing_Cnv_Qc(receiveFileModel.getQcPath(),receiveFileModel.getChip(),date);
			}catch (IOException e){
				throw new IOException("cnv.qc.txt文件解析出错，错误信息为："+e.getMessage());
			}
		}
		if(StringUtil.isNotBlank(receiveFileModel.getResultPath())){
			try {
				FileParseing_Cnv_Split_Result(receiveFileModel.getResultPath(),receiveFileModel.getChip(),date,map);
			}catch (IOException e){
				throw new IOException("cnv_split_result.txt文件解析出错，错误信息为："+e.getMessage());
			}
		}
		if(StringUtil.isNotBlank(receiveFileModel.getSexPath())){
			try {
				FileParseing_Sex(receiveFileModel.getSexPath(), map,receiveFileModel.getChip());
			}catch (IOException e){
				throw new IOException("sex.txt文件解析出错，错误信息为："+e.getMessage());
			}
		}
		if(StringUtil.isNotBlank(receiveFileModel.getModel_predictPath())){
			try {
				FileParseing_Onco_Result(receiveFileModel.getModel_predictPath(), map,receiveFileModel.getChip());
			}catch (IOException e){
				throw new IOException("onco_result.csv文件解析出错，错误信息为："+e.getMessage());
			}
		}
		if(StringUtil.isNotBlank(receiveFileModel.getCnvPath())){
			try{
				fileParsing_cnv(receiveFileModel.getCnvPath());
			}catch (Exception e){
				throw new Exception("cnv.json文件解析出错，错误信息为："+e.getMessage());
			}
		}
		return  map;

	}

	/***
	 * 解析cnv.json文件
	 */
	private void fileParsing_cnv(String path) throws Exception {
		File file = new File(path);
		String jsonString = FileUtils.readFileToString(file, "UTF-8");
		org.json.JSONObject jsonObject = new org.json.JSONObject(jsonString);
		JSONObject cnv_qc = jsonObject.getJSONObject("cnv_qc");
		CnvjgDto cnvjgDto=new CnvjgDto();
		cnvjgDto.setCnvjgid(StringUtil.generateUUID());
		cnvjgDto.setWkbh(jsonObject.getString("sample"));
		cnvjgDto.setSjl(cnv_qc.getString("total_reads"));
		cnvjgDto.setBdxls(cnv_qc.getString("mapped_reads"));
		cnvjgDto.setGchl(cnv_qc.getString("gc"));
		cnvjgDto.setWaviness(cnv_qc.getString("waviness"));
		cnvjgDto.setKaryotype(cnv_qc.getString("karyotype"));
		if("F".equals(cnv_qc.getString("sex"))){
			cnvjgDto.setXb("2");
		}else if("M".equals(cnv_qc.getString("sex"))){
			cnvjgDto.setXb("1");
		}
		BioXpxxDto bioXpxxDto =new BioXpxxDto();
		bioXpxxDto.setXpm(jsonObject.getString("chip"));
		BioXpxxDto xpxxDto = xpxxService.getDto(bioXpxxDto);
		if(xpxxDto!=null){
			cnvjgDto.setXpid(xpxxDto.getXpid());
			WkcxDto wkcxDto=new WkcxDto();
			wkcxDto.setWkbh(jsonObject.getString("sample"));
			wkcxDto.setXpid(cnvjgDto.getXpid());
			WkcxDto dto = wkcxService.getDto(wkcxDto);
			if(dto!=null){
				cnvjgDto.setWkcxid(dto.getWkcxid());
			}
		}
		cnvjgDto.setQcfs(cnv_qc.getString("qc"));
		if(Float.parseFloat(cnv_qc.getString("mapped_reads"))<(15*100000)){
			JcsjDto jcsjDto= redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TUMOR_JUDGEMENT.getCode()).stream().filter(item->"QC未通过".equals(item.getCsmc())).findFirst().orElse(null);
			if(jcsjDto!=null){
				cnvjgDto.setShjg(jcsjDto.getCsid());
			}
		}
		SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		cnvjgDto.setLrsj(dateFm.format(new Date()));
		cnvjgDto.setOnresult(cnv_qc.getString("model_predict"));
		List<JcsjDto> jcsjList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MODEL_PREDICTION.getCode());
		String yinx="";
		String yangx="";
		for(JcsjDto jcsjDto:jcsjList){
			if("NEGATIVE".equals(jcsjDto.getCsdm())){
				yinx=jcsjDto.getCsid();
			}else if("POSITIVE".equals(jcsjDto.getCsdm())){
				yangx=jcsjDto.getCsid();
			}
		}
		BigDecimal aijg=new BigDecimal(cnv_qc.getString("model_predict"));
		BigDecimal bigDecimal=new BigDecimal("0.3");
		if(aijg.compareTo(bigDecimal)>=0){
			cnvjgDto.setAijg(yangx);
		}else{
			cnvjgDto.setAijg(yinx);
		}
		cnvjgService.insert(cnvjgDto);
		JSONArray cnv_result = jsonObject.getJSONArray("cnv_result");
		List<CnvjgxqDto> list=new ArrayList<>();
		for(int i=0;i<cnv_result.length();i++){
			org.json.JSONObject object = new org.json.JSONObject(cnv_result.getString(i));
			CnvjgxqDto cnvjgxqDto=new CnvjgxqDto();
			cnvjgxqDto.setCnvjgxqid(StringUtil.generateUUID());
			cnvjgxqDto.setCnvjgid(cnvjgDto.getCnvjgid());
			cnvjgxqDto.setRst(object.getString("chr"));
			cnvjgxqDto.setQsqd(object.getString("zone_start"));
			cnvjgxqDto.setZzqd(object.getString("zone_end"));
			cnvjgxqDto.setCnvlx(object.getString("cnv_type"));
			if("true".equals(object.getString("mos"))){
				cnvjgxqDto.setSfqh("1");
			}else if("false".equals(object.getString("mos"))){
				cnvjgxqDto.setSfqh("0");
			}
			if("true".equals(object.getString("chr_level_var"))){
				cnvjgxqDto.setSffzsb("1");
			}else if("false".equals(object.getString("chr_level_var"))){
				cnvjgxqDto.setSffzsb("0");
			}
			cnvjgxqDto.setQswz(object.getString("pos_start"));
			cnvjgxqDto.setZzwz(object.getString("pos_end"));
			cnvjgxqDto.setKbs(object.getString("copy_num"));
			cnvjgxqDto.setCnvjg(object.getString("detail"));
			cnvjgxqDto.setCnvxq(object.getString("result"));
			cnvjgxqDto.setSfsdtj("0");
			cnvjgxqDto.setXpid(cnvjgDto.getXpid());
			cnvjgxqDto.setWkcxid(cnvjgDto.getWkcxid());
			cnvjgxqDto.setLrsj(cnvjgDto.getLrsj());
			list.add(cnvjgxqDto);
		}
		if(!list.isEmpty()){
			cnvjgxqService.insertList(list);
		}
	}

	/***
	 * 解析cnv.qc文件并存储数据库
	 */
	public Map<String,Object> FileParseing_Cnv_Qc(String filePath,String chip,String date) throws IOException {
		//读取文件
		Map<String,Object> returnMap=new HashMap<>();
		FileReader fr=new FileReader(filePath);
		BufferedReader br=new BufferedReader(fr);
		String line;
		String[] arrs;
		int row=0;
		List<CnvjgDto> list=new ArrayList<>();
		BioXpxxDto bioXpxxDto =new BioXpxxDto();
		bioXpxxDto.setXpm(chip);
		BioXpxxDto dto = xpxxService.getDto(bioXpxxDto);
		String xpid="";
		if(dto!=null){
			xpid=dto.getXpid();
		}
		WkcxDto wkcxDto=new WkcxDto();
		if (dto != null) {
			wkcxDto.setXpid(dto.getXpid());
		}
		List<WkcxDto> dtoList = wkcxService.getDtoList(wkcxDto);
		List<String> wkbhs=new ArrayList<>();
		while ((line=br.readLine())!=null) {
			//跳过文档第一行
			if(row>0){
				arrs=line.split("\t");
//            System.out.println(arrs[0] + " : " + arrs[1] + " : " + arrs[2]);
				//list= Arrays.asList(arrs);
				List<String>stringList= Arrays.asList(arrs);
				CnvjgDto cnvjgDto=new CnvjgDto();
				cnvjgDto.setCnvjgid(StringUtil.generateUUID());
				cnvjgDto.setWkbh(stringList.get(0));
				cnvjgDto.setSjl(stringList.get(1));
				cnvjgDto.setBdxls(stringList.get(2));
				BigDecimal bdl=new BigDecimal(stringList.get(3));
				BigDecimal bigDecimal=new BigDecimal(100);
				cnvjgDto.setBdl(String.valueOf(bdl.multiply(bigDecimal)));
				cnvjgDto.setUniqxls(stringList.get(4));
				cnvjgDto.setGchl(stringList.get(7));
				cnvjgDto.setWaviness(stringList.get(8));
				if(Float.parseFloat(stringList.get(2))<(15*100000)){
					JcsjDto jcsjDto= redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TUMOR_JUDGEMENT.getCode()).stream().filter(item->"QC未通过".equals(item.getCsmc())).findFirst().orElse(null);
					if(jcsjDto!=null){
						cnvjgDto.setShjg(jcsjDto.getCsid());
					}
				}

				cnvjgDto.setLrsj(date);
				cnvjgDto.setXpid(xpid);
				if(!wkbhs.contains(cnvjgDto.getWkbh())){
					list.add(cnvjgDto);
					wkbhs.add(cnvjgDto.getWkbh());
				}
			}
			row++;
		}
		br.close();
		fr.close();
		for(CnvjgDto dto_t:list){
			for(WkcxDto wkcxDto_t:dtoList){
				if(dto_t.getWkbh().equals(wkcxDto_t.getWkbh())){
					dto_t.setWkcxid(wkcxDto_t.getWkcxid());
				}
			}
		}
		cnvjgService.insertList(list);
		returnMap.put("cnvjgDtos",list);
		return returnMap;
	}

	/***
	 * 解析cnv_split_result文件并存储数据库
	 */
	public Map<String,Object> FileParseing_Cnv_Split_Result(String filePath,String chip,String date,Map<String,Object> map) throws IOException {
		Map<String,Object> returnMap=new HashMap<>();
		List<CnvjgDto> cnvjgDtos = (List<CnvjgDto>) map.get("cnvjgDtos");
		List<String> wkbhs=new ArrayList<>();
		List<OtherDto> otherDtoList=new ArrayList<>();
		//读取文件
		FileReader fr=new FileReader(filePath);
		BufferedReader br=new BufferedReader(fr);
		String line;
		String[] arrs;
		int row=0;
		List<CnvjgxqDto> list=new ArrayList<>();
		BioXpxxDto bioXpxxDto =new BioXpxxDto();
		bioXpxxDto.setXpm(chip);
		BioXpxxDto bioXpxxDto_t = xpxxService.getDto(bioXpxxDto);
		String xpid="";
		if(bioXpxxDto_t !=null){
			xpid= bioXpxxDto_t.getXpid();
		}
		while ((line=br.readLine())!=null) {
			//跳过文档第一行
			if(row>0){
				arrs=line.split("\t");
//            System.out.println(arrs[0] + " : " + arrs[1] + " : " + arrs[2]);
				//list= Arrays.asList(arrs);
				List<String>stringList= Arrays.asList(arrs);
				CnvjgxqDto cnvjgxqDto=new CnvjgxqDto();
				OtherDto otherDto=new OtherDto();
				cnvjgxqDto.setCnvjgxqid(StringUtil.generateUUID());
				cnvjgxqDto.setWkbh(stringList.get(0));
				otherDto.setWkbm(stringList.get(0));
				cnvjgxqDto.setKaryotype(stringList.get(1));
				cnvjgxqDto.setRst(stringList.get(2));
				cnvjgxqDto.setQsqd(stringList.get(3));
				cnvjgxqDto.setZzqd(stringList.get(4));
				cnvjgxqDto.setCnvlx(stringList.get(6));
				if("True".equals(stringList.get(7))){
					cnvjgxqDto.setSfqh("1");
				}else if("False".equals(stringList.get(7))){
					cnvjgxqDto.setSfqh("0");
				}
				if("True".equals(stringList.get(8))){
					cnvjgxqDto.setSffzsb("1");
				}else if("False".equals(stringList.get(8))){
					cnvjgxqDto.setSffzsb("0");
				}
				cnvjgxqDto.setQswz(stringList.get(9));
				cnvjgxqDto.setZzwz(stringList.get(10));
				cnvjgxqDto.setKbs(stringList.get(11));
				cnvjgxqDto.setCnvxq(stringList.get(12));
				cnvjgxqDto.setCnvjg(stringList.get(13));
				cnvjgxqDto.setSfsdtj("0");
				cnvjgxqDto.setLrsj(date);
				otherDto.setOncofxwcsj(date);
				list.add(cnvjgxqDto);
				wkbhs.add(stringList.get(0));
				otherDtoList.add(otherDto);
			}
			row++;
		}
		br.close();
		fr.close();
		if(cnvjgDtos==null|| cnvjgDtos.isEmpty()){
			CnvjgDto cnvjgDto=new CnvjgDto();
			cnvjgDto.setWkbhs(wkbhs);
			cnvjgDto.setXpid(xpid);
			cnvjgDtos=cnvjgService.getListByWkbhs(cnvjgDto);
		}
		for(CnvjgDto dto:cnvjgDtos){
			for(CnvjgxqDto cnvjgxqDto:list){
				if(dto.getWkbh().equals(cnvjgxqDto.getWkbh())){
					cnvjgxqDto.setCnvjgid(dto.getCnvjgid());
					dto.setKaryotype(cnvjgxqDto.getKaryotype());
					cnvjgxqDto.setXpid(xpid);
					cnvjgxqDto.setWkcxid(dto.getWkcxid());
				}
			}
		}

		cnvjgxqService.insertList(list);
		otherService.updateWksjmxList(otherDtoList);
		cnvjgService.updateList(cnvjgDtos);
		return returnMap;
	}

	/***
	 * 解析sex文件并存储数据库
	 */
	public Map<String,Object> FileParseing_Sex(String filePath, Map<String,Object> map, String chip) throws IOException {
		Map<String,Object> returnMap=new HashMap<>();
		List<CnvjgDto> cnvjgDtos = (List<CnvjgDto>) map.get("cnvjgDtos");
		List<String> wkbhs=new ArrayList<>();
		//读取文件
		FileReader fr=new FileReader(filePath);
		BufferedReader br=new BufferedReader(fr);
		String line;
		String[] arrs;
		List<CnvjgDto> list=new ArrayList<>();
		while ((line=br.readLine())!=null) {
			//跳过文档第一行
				arrs=line.split("\t");
//            System.out.println(arrs[0] + " : " + arrs[1] + " : " + arrs[2]);
				//list= Arrays.asList(arrs);
				List<String>stringList= Arrays.asList(arrs);
				CnvjgDto cnvjgDto=new CnvjgDto();
				cnvjgDto.setWkbh(stringList.get(0));
				if("F".equals(stringList.get(1))){
					cnvjgDto.setXb("2");
				}else if("M".equals(stringList.get(1))){
					cnvjgDto.setXb("1");
				}
				list.add(cnvjgDto);
				wkbhs.add(stringList.get(0));
		}
		br.close();
		fr.close();
		BioXpxxDto bioXpxxDto =new BioXpxxDto();
		bioXpxxDto.setXpm(chip);
		BioXpxxDto bioXpxxDto_t = xpxxService.getDto(bioXpxxDto);
		String xpid="";
		if(bioXpxxDto_t !=null){
			xpid= bioXpxxDto_t.getXpid();
		}
		if(cnvjgDtos==null|| cnvjgDtos.isEmpty()){
			CnvjgDto cnvjgDto=new CnvjgDto();
			cnvjgDto.setWkbhs(wkbhs);
			cnvjgDto.setXpid(xpid);
			cnvjgDtos=cnvjgService.getListByWkbhs(cnvjgDto);
		}
		for(CnvjgDto dto:cnvjgDtos){
			for(CnvjgDto cnvjgDto:list){
				if(dto.getWkbh().equals(cnvjgDto.getWkbh())){
					dto.setXb(cnvjgDto.getXb());
					break;
				}
			}
		}
		cnvjgService.updateList(cnvjgDtos);
		return returnMap;
	}


	/***
	 * 解析onco_result文件并存储数据库
	 */
	public Map<String,Object> FileParseing_Onco_Result(String filePath, Map<String,Object> map, String chip) throws IOException {
		Map<String,Object> returnMap=new HashMap<>();
		List<CnvjgDto> cnvjgDtos = (List<CnvjgDto>) map.get("cnvjgDtos");
		List<String> wkbhs=new ArrayList<>();
		List<JcsjDto> jcsjList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MODEL_PREDICTION.getCode());
		String yinx="";
		String yangx="";
		String qcwtg="";
		for(JcsjDto dto:jcsjList){
			if("NEGATIVE".equals(dto.getCsdm())){
				yinx=dto.getCsid();
			}else if("POSITIVE".equals(dto.getCsdm())){
				yangx=dto.getCsid();
			}else if("QCFAIL".equals(dto.getCsdm())){
				qcwtg=dto.getCsid();
			}
		}
		//读取文件
		FileReader fr=new FileReader(filePath);
		BufferedReader br=new BufferedReader(fr);
		String line;
		String[] arrs;
		int row=0;
		List<CnvjgDto> list=new ArrayList<>();
		while ((line=br.readLine())!=null) {
			//跳过文档第一行
			if(row>0){
				arrs=line.split(",");
//            System.out.println(arrs[0] + " : " + arrs[1] + " : " + arrs[2]);
				//list= Arrays.asList(arrs);
				List<String>stringList= Arrays.asList(arrs);
				CnvjgDto cnvjgDto=new CnvjgDto();
				cnvjgDto.setWkbh(stringList.get(0));
				cnvjgDto.setQcfs(stringList.get(2));
				cnvjgDto.setOnresult(stringList.get(1));
				BigDecimal aijg=new BigDecimal(stringList.get(1));
				BigDecimal bigDecimal=new BigDecimal("0.3");
				if(aijg.compareTo(bigDecimal)>=0){
					cnvjgDto.setAijg(yangx);
				}else{
					cnvjgDto.setAijg(yinx);
				}
				list.add(cnvjgDto);
				wkbhs.add(stringList.get(0));
			}
			row++;
		}
		br.close();
		fr.close();
		BioXpxxDto bioXpxxDto =new BioXpxxDto();
		bioXpxxDto.setXpm(chip);
		BioXpxxDto bioXpxxDto_t = xpxxService.getDto(bioXpxxDto);
		String xpid="";
		if(bioXpxxDto_t !=null){
			xpid= bioXpxxDto_t.getXpid();
		}
		if(cnvjgDtos==null|| cnvjgDtos.isEmpty()){
			CnvjgDto cnvjgDto=new CnvjgDto();
			cnvjgDto.setWkbhs(wkbhs);
			cnvjgDto.setXpid(xpid);
			cnvjgDtos=cnvjgService.getListByWkbhs(cnvjgDto);
		}
		for(CnvjgDto dto:cnvjgDtos){
			boolean flag=false;
			for(CnvjgDto cnvjgDto:list){
				if(dto.getWkbh().equals(cnvjgDto.getWkbh())){
					dto.setAijg(cnvjgDto.getAijg());
					dto.setQcfs(cnvjgDto.getQcfs());
					dto.setOnresult(cnvjgDto.getOnresult());
					flag=true;
					break;
				}
			}
			if(!flag){
				dto.setAijg(qcwtg);
			}
		}
		cnvjgService.updateList(cnvjgDtos);
		return returnMap;
	}
	/**
	 * @param targetDirPath 存储MultipartFile文件的目标文件夹
	 * @return 文件的存储的绝对路径
	 */
	public String saveMultipartFile(MultipartFile file, String targetDirPath){
		File toFile;
		if (file.getSize() <= 0) {
			return null;
		} else {

			/*获取文件原名称*/
			String originalFilename = file.getOriginalFilename();
			toFile = new File(targetDirPath + File.separator + originalFilename);

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
				e.printStackTrace();
			}



			return absolutePath;
		}
	}


	//获取流文件
	private static void inputStreamToFile(InputStream ins, File file) {
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
			e.printStackTrace();
		}
	}


}
