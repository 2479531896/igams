package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.post.ISjxxDao;
import com.matridx.igams.wechat.dao.post.ISjxxResStatisticDao;
import com.matridx.igams.wechat.service.svcinterface.ISjxxResStatisticService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class SjxxResStatisticServiceImpl implements ISjxxResStatisticService
{
	@Value("${matridx.fileupload.releasePath:}")
	private String releaseFilePath;
	@Value("${matridx.fileupload.tempPath:}")
	private String tempFilePath;
	@Value("${matridx.fileupload.prefix}")
	private String prefix;
	
	private Logger log = LoggerFactory.getLogger(SjxxResStatisticServiceImpl.class);
	@Autowired
	private ISjxxResStatisticDao dao;
	@Autowired
	private IFjcfbService fjcfbService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	private ISjxxDao sjxxdao;

	@Override
	public Map<String, String> getRYbStatisByDay(SjxxDto sjxxDto) {
		return dao.getRYbStatisByDay(sjxxDto);
	}

	@Override
	public List<Map<String, String>> getRYbxxByDay(SjxxDto sjxxDto) {
		return dao.getRYbxxByDay(sjxxDto);
	}

	@Override
	public Map<String, Integer> getSjFjNum(SjxxDto sjxxDto) {
		return dao.getSjFjNum(sjxxDto);
	}

	@Override
	public List<Map<String, String>> getAvgTime(SjxxDto sjxxDto) {
		List<String> rqs = new ArrayList<>();
		if ("week".equals(sjxxDto.getTj())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
		}else {
			rqs = getRqsByStartAndEnd(sjxxDto);
		}
		sjxxDto.setRqs(rqs);
		return dao.getAvgTime(sjxxDto);
	}

	@Override
	public Map<String, String>  getLifeCount(SjxxDto sjxxDto) {
		return dao.getLifeCount(sjxxDto);
	}

	@Override
	public List<Map<String, String>> getLifeCycle(SjxxDto sjxxDto) {
		List<String> rqs = new ArrayList<>();
		if ("week".equals(sjxxDto.getTj())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
		}else {
			rqs = getRqsByStartAndEnd(sjxxDto);
		}
		sjxxDto.setRqs(rqs);

		List<Map<String, String>> resultMaps = dao.getLifeCycle(sjxxDto);
		if (CollectionUtils.isEmpty(resultMaps)){
			return resultMaps;
		}
		String s_preRq = "";
		double sum =0,presum=0;
		for(int i=0;i<resultMaps.size();i++) {
			//日期有变化
			if(!s_preRq.equals(resultMaps.get(i).get("rq"))) {
				if(presum == 0 && i!=0) {
					resultMaps.get(i-1).put("bl", "0");
				}else if(presum != 0) {
					BigDecimal bg_sum = new BigDecimal(presum);
					resultMaps.get(i-1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum,0,RoundingMode.HALF_UP).toString());
				}
				presum = sum;
				s_preRq= String.valueOf(resultMaps.get(i).get("rq"));
				sum = Double.parseDouble(String.valueOf(resultMaps.get(i).get("zqsj")));
			}
		}
		if(presum == 0 && resultMaps.size() > 0) {
			resultMaps.get(resultMaps.size()-1).put("bl", "0");
		}else if(presum != 0) {
			BigDecimal bg_sum = new BigDecimal(presum);
			resultMaps.get(resultMaps.size()-1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum,0,RoundingMode.HALF_UP).toString());
		}
		return resultMaps;
	}

	@Override
	public List<Map<String, String>> getLifeTimeCount(SjxxDto sjxxDto) {
		List<Map<String, String>> lifeTimeCount = dao.getLifeTimeCount(sjxxDto);
		List<Map<String, String>> newLifeTimeCount = new ArrayList<>();
		Random random = new Random();
		if (!CollectionUtils.isEmpty(lifeTimeCount)) {
			for (Map<String, String> map : lifeTimeCount) {
				double v = random.nextDouble();
				while (!(v>0.2&&v<0.6)){
					v = random.nextDouble();
				}
				BigDecimal wz = new BigDecimal(v);
				wz = wz.multiply(new BigDecimal("1000")).setScale(2,RoundingMode.HALF_UP);
				Map<String, String> map_m = new HashMap<>();
				String jswqy = String.valueOf(map.get("jswqy"));
				String qywtq = String.valueOf(map.get("qywtq"));
				String tqwkz = String.valueOf(map.get("tqwkz"));
				String kzwjc = String.valueOf(map.get("kzwjc"));
				String jcwbg = String.valueOf(map.get("jcwbg"));
				String wzStr = String.valueOf(wz);
				if (!"-1".equals(jswqy)){
					map_m.put("sj",jswqy);
					map_m.put("name","接收");
					map_m.put("wz",wzStr);
					newLifeTimeCount.add(map_m);
				}else if (!"-1".equals(qywtq)){
					map_m.put("sj",qywtq);
					map_m.put("name","取样");
					map_m.put("wz",wzStr);
					newLifeTimeCount.add(map_m);
				}else if (!"-1".equals(tqwkz)){
					map_m.put("sj",tqwkz);
					map_m.put("name","提取");
					map_m.put("wz",wzStr);
					newLifeTimeCount.add(map_m);
				}else if (!"-1".equals(kzwjc)){
					map_m.put("sj",kzwjc);
					map_m.put("name","扩增");
					map_m.put("wz",wzStr);
					newLifeTimeCount.add(map_m);
				}else if (!"-1".equals(jcwbg)){
					map_m.put("sj",jcwbg);
					map_m.put("name","检测");
					map_m.put("wz",wzStr);
					newLifeTimeCount.add(map_m);
				}
			}
		}
		return newLifeTimeCount;
	}

	/**
	 * 根据接收日期的开始日期和结束日期，自动计算每一天的日期，形成一个list
	 *
	 * @param sjxxDto
	 * @return
	 */
	public List<String> getRqsByStartAndEnd(SjxxDto sjxxDto)
	{
		List<String> rqs = new ArrayList<>();
		try
		{
			if ("day".equals(sjxxDto.getTj()))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
				Calendar endcalendar = Calendar.getInstance();
				if (StringUtil.isNotBlank(sjxxDto.getJsrqend()))
				{
					endcalendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
				} else
				{
					endcalendar.setTime(new Date());
				}
				while (endcalendar.compareTo(calendar) >= 0)
				{
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.DATE, 1);
				}
			} else if ("mon".equals(sjxxDto.getTj()))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqMstart()));

				Calendar endcalendar = Calendar.getInstance();
				if (StringUtil.isNotBlank(sjxxDto.getJsrqMend()))
				{
					endcalendar.setTime(sdf.parse(sjxxDto.getJsrqMend()));
				} else
				{
					endcalendar.setTime(new Date());
				}

				while (endcalendar.compareTo(calendar) >= 0)
				{
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.MONTH, 1);
				}
			} else if ("year".equals(sjxxDto.getTj()))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqYstart()));

				Calendar endcalendar = Calendar.getInstance();
				if (StringUtil.isNotBlank(sjxxDto.getJsrqYend()))
				{
					endcalendar.setTime(sdf.parse(sjxxDto.getJsrqYend()));
				} else
				{
					endcalendar.setTime(new Date());
				}

				while (endcalendar.compareTo(calendar) >= 0)
				{
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.YEAR, 1);
				}
			}
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return rqs;
	}
	
	/**
	 * 接收天隆上传的文件，并对结果进行保存
	 * @param file
	 * @param params
	 * @return
	 */
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public String uploadResFirstResult(MultipartFile file,Map<String, String> params) {
		String nbbm = params.get("nbbm");
		String fluores = params.get("fluores");
		String jcdwdm = params.get("jcdwdm");
		if(StringUtil.isBlank(nbbm)) {
			return "未获取到内部编码。";
		}else if (file == null || file.getSize() <= 0) {
			return "未获取到文件信息。";
		}else if (StringUtil.isBlank(fluores)) {
			return "未获取到荧光信息。";
		}else if (StringUtil.isBlank(jcdwdm)) {
			return "未获取到检测单位信息。";
		}
		
		//保存附件，并以样本编码带A，B的进行保存
		SjxxDto sjxxDto = saveMultipartFile(file ,params);
		if(sjxxDto == null) {
			return "未获取到相应标本信息，内部编码：" + nbbm;
		}
		//为查询方便，同时生成excel文件，存放各个荧光值，暂不处理
		
		return null;
	}
	
	/**
	 * 
	 * @param file
	 *  		targetDirPath
	 *			nbbm
	 * @return
	 */
	private SjxxDto saveMultipartFile(MultipartFile file,Map<String, String> params){
		
		String nbbm = params.get("nbbm");
		//String bbh = params.get("bbh");
		
		//如果为NC标本，则保存文件后存放到redis里
		if(nbbm.startsWith("NC-")||nbbm.startsWith("DC-")) {
			//为了统一化,组成一个xlxs
			saveToXSSFWorkbook(params);
			return new SjxxDto();
		}else {
			String tlpdFileName = StringUtil.generateUUID();
			//根据日期创建文件夹
			String storePath = prefix + releaseFilePath + BusTypeEnum.IMP_FILE_RFS_TEMEPLATE + "/"+ "UP"+
					DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
					DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
					DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
			File file1 = new File(storePath);
			if (!file1.exists()) {
				file1.mkdirs();
			}
			String tlpdPath = storePath+"/" + tlpdFileName +".tlpd";
            /*获取文件原名称*/
			File toFile = new File(tlpdPath);
            try {
				String absolutePath = toFile.getCanonicalPath();
                /*判断路径中的文件夹是否存在，如果不存在，先创建文件夹*/
                String dirPath = absolutePath.substring(0, absolutePath.lastIndexOf(File.separator));
                File dir = new File(dirPath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                inputStreamToFile(file, toFile);
            } catch (Exception e) {
                log.error("天隆上传ResFirst的 tlpd(saveMultipartFile)报错："+e.getMessage());
                e.printStackTrace();
            }
			//根据样本编号查找ResFirst信息
			String[] s_resFStrings = nbbm.split(" ");
			String s_nbbm = "";
			if(s_resFStrings.length >= 1) {
				s_nbbm = s_resFStrings[0];
			}
			SjxxDto t_sjxxDto = new SjxxDto();
			t_sjxxDto.setNbbm(s_nbbm);
			List<SjxxDto> list = sjxxdao.getAccptList(t_sjxxDto);
			if(CollectionUtils.isEmpty(list))
				return null;
			
			DBEncrypt bpe = new DBEncrypt();
			FjcfbDto fjcfbtlpd = new FjcfbDto();
			fjcfbtlpd.setYwid(list.get(0).getSjid());
			fjcfbtlpd.setWjmhz(".tlpd");
			fjcfbtlpd.setWjm(file.getOriginalFilename());
			fjcfbtlpd.setWjlj(bpe.eCode(tlpdPath));
			fjcfbtlpd.setFwjlj(bpe.eCode(storePath));
			fjcfbtlpd.setFwjm(tlpdFileName +".tlpd");
			//***新增赋值
			fjcfbtlpd.setFjid(StringUtil.generateUUID());
			fjcfbtlpd.setYwlx(BusTypeEnum.IMP_FILE_RFS_TEMEPLATE.getCode());
			
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbtlpd);
			//判断文件是否重复
			boolean isFind = false;
			for(int i=0;i<fjcfbDtos.size();i++) {
				if(file.getOriginalFilename().equals(fjcfbDtos.get(i).getWjm())) {
					isFind = true;
					break;
				}
			}
			if(isFind)
				return list.get(0);
			
			fjcfbtlpd.setXh(String.valueOf(fjcfbDtos.size() + 1));
			fjcfbtlpd.setZhbj("0");
			//插入数据到附件存放表中
			fjcfbService.insert(fjcfbtlpd);
			//为了统一化,组成一个xlxs
			String filepath= saveToXSSFWorkbook(params);
			//
			fjcfbtlpd.setWjmhz(".xlsx");
			fjcfbtlpd.setWjm(file.getOriginalFilename().replace(".tlpd", ".xlsx"));
			fjcfbtlpd.setWjlj(bpe.eCode(filepath));
			fjcfbtlpd.setFwjlj(bpe.eCode(storePath));
			fjcfbtlpd.setFwjm(filepath.substring(filepath.lastIndexOf("/")+1));
			fjcfbtlpd.setFjid(params.get("fjid"));
			fjcfbtlpd.setXh(String.valueOf(fjcfbDtos.size() + 2));
			fjcfbService.insert(fjcfbtlpd);

			return list.get(0);
		}
	}
	
	/**
	 * 复制文件流到指定位置
	 * @param infile
	 * @param outfile
	 */
	private void inputStreamToFile(MultipartFile infile, File outfile) {
		InputStream ins = null;
		OutputStream os = null;
		try {
			ins = infile.getInputStream();
			os = new FileOutputStream(outfile);
			int bytesRead;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			log.error("天隆上传ResFirst的tlpd(inputStreamToFile)报错："+e.getMessage());
			e.printStackTrace();
		}finally
		{
			try{
				if(ins!=null)
					ins.close();
				if(os!=null){
					os.flush();
					os.close();
					os = null;
				}
			}catch(Exception ex){
				log.error("天隆上传ResFirst文件关闭报错："+ex.getMessage());
			}
		}
	}
	
	/**
	 * 根据上传的数据生成相应文件（参照原有手动上传的文件），方便对接原有手动上传文件的功能
	 * @param params
	 * @return
	 */
	private String saveToXSSFWorkbook(Map<String, String> params) {

		String nbbm = params.get("nbbm");
		String[] array_nbbm = nbbm.split(" ");
		String bbh = params.get("bbh");
		String[] array_bbh = bbh.split("-");
		String jcdwdm = params.get("jcdwdm");
		//String fluores = params.get("fluores");
		
		String tlpdFileName = StringUtil.generateUUID();

		String uploadTempinfo = (String)redisUtil.hget("resfirst_upload_temp",jcdwdm);
		Map<String, Object> uploadTempMap = JSONObject.parseObject(uploadTempinfo);

		XSSFWorkbook workbook = null;
		FileOutputStream outputStream = null;
		FileInputStream inputStream = null;
		String tlpdPath;
		try {
			//如果为NC的，则判断是否存在
			if(array_nbbm[0].startsWith("NC-") || array_nbbm[0].startsWith("DC-")) {
				//判断是否文件存在，如果存在则判断是否已经上传过，如果上传则覆盖，如果没有上出过则新增。如果文件不存在则新增
				if(uploadTempMap!=null && uploadTempMap.get("filepath")!= null) {
					inputStream = new FileInputStream((String)uploadTempMap.get("filepath"));
					workbook = new XSSFWorkbook(inputStream);
					XSSFSheet sheet;
					int num_sheet = workbook.getNumberOfSheets();
					boolean isFind = false;
					for(int i = 0;i < num_sheet;i++) {
						sheet = workbook.getSheetAt(i);
						if(nbbm.equals(sheet.getRow(4).getCell(3).getStringCellValue()) && bbh.equals(sheet.getRow(4).getCell(5).getStringCellValue())) {
							setXlsxInfo(sheet,params,false);
							isFind = true;
							break;
						}
					}
					//如果不存在相应的文件，则新增sheet
					if(!isFind) {
						sheet = workbook.createSheet(array_nbbm[0] + "_" + array_bbh[0]);
						setXlsxInfo(sheet,params,true);
					}
				}else {
					uploadTempMap = new HashMap<>();
					//如果没有上出过则新增。如果文件不存在则新增
					workbook = new XSSFWorkbook();
					XSSFSheet sheet;
					if(workbook.getNumberOfSheets() > 0)
						sheet = workbook.getSheetAt(0);
					else
						sheet = workbook.createSheet(array_nbbm[0] + "_" + array_bbh[0]);
					setXlsxInfo(sheet,params,true);
				}
				//根据日期创建文件夹
				String tmp_storePath = prefix + tempFilePath + BusTypeEnum.IMP_FILE_RFS_TEMEPLATE + "/"+ "UP"+
						DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
						DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
						DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
				File file1 = new File(tmp_storePath);
				if (!file1.exists()) {
					file1.mkdirs();
				}
				tlpdPath = tmp_storePath + "/" + tlpdFileName +".xlsx";
				//初次创建，生成map放到redis里
				uploadTempMap.put("filepath", tlpdPath);
				outputStream = new FileOutputStream(tlpdPath);
				redisUtil.hset("resfirst_upload_temp",jcdwdm,JSONObject.toJSONString(uploadTempMap));
			} else {
				//已经存在文件的情况，复制文件，里面包含了NC的信息
				if(uploadTempMap!=null && uploadTempMap.get("filepath")!= null) {
					inputStream = new FileInputStream((String)uploadTempMap.get("filepath"));
					workbook = new XSSFWorkbook(inputStream);
				}else {
					workbook = new XSSFWorkbook();
				}
				
				XSSFSheet sheet = workbook.createSheet(array_nbbm[0] + "_" + array_bbh[0]);
				setXlsxInfo(sheet,params,true);
				workbook.setActiveSheet(workbook.getNumberOfSheets() - 1);

				String release_storePath = prefix + releaseFilePath + BusTypeEnum.IMP_FILE_RFS_TEMEPLATE + "/"+ "UP"+
						DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
						DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
						DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
				
				File file2 = new File(release_storePath);
				if (!file2.exists()) {
					file2.mkdirs();
				}
				tlpdPath = release_storePath + "/" + tlpdFileName +".xlsx";
				outputStream = new FileOutputStream(tlpdPath);
			}
			if(inputStream != null) {
				inputStream.close();
				inputStream = null;
			}
			
			workbook.write(outputStream);
			
		}catch(Exception e) {
			log.error(e.getMessage());
			return null;
		}finally {
			try {
				if(inputStream != null) {
					inputStream.close();
					inputStream = null;
				}
				if(outputStream!=null) {
					outputStream.flush();
					outputStream.close();
				}
				if(workbook!=null)
					workbook.close();
			}catch(Exception ex) {
				log.error("close:" + ex.getMessage());
				return null;
			}
		}
		
		return tlpdPath;
	}
	
	/**
	 * 设置Xlxs文件里的信息
	 * @param sheet
	 * @param params
	 * @return
	 */
	private boolean setXlsxInfo(XSSFSheet sheet,Map<String, String> params,boolean isInit) {
		
		String nbbm = params.get("nbbm");
		String bbh = params.get("bbh");
		String fluores = params.get("fluores");
		
		String dateString = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss");
		//初始化ROW
		if(isInit) {
			for(int i=0 ; i<38; i++) {
				XSSFRow row = sheet.createRow(i);
				for(int j=0;j<14;j++)
					row.createCell(j);
			}
		}
		//设置仪器软件版本
		sheet.getRow(0).getCell(4).setCellValue("pcrExperiment");
		//设置日期
		sheet.getRow(4).getCell(1).setCellValue(dateString.split(" ")[0]);
		//设置时间
		sheet.getRow(5).getCell(1).setCellValue(dateString.split(" ")[1]);
		//设置内部编码
		sheet.getRow(4).getCell(3).setCellValue(nbbm);
		//设置版本号
		sheet.getRow(4).getCell(5).setCellValue(bbh);
		String[] array_fluores = fluores.split(",");
		//设置荧光信息
		for (int j = 28; j <= 36; j++) {
			for (int k = 0; k <=12 ; k++) {
				if(j==28) {
					sheet.getRow(j).getCell(k).setCellValue( k);
					continue;
				}else if(k==0)
				{
					sheet.getRow(j).getCell(k).setCellValue(String.valueOf((char) (65 + j -29)));
					continue;
				}
				int index = (j-29)*12 + k - 1;
				if(array_fluores.length > index)
					sheet.getRow(j).getCell(k).setCellValue(array_fluores[index]);
			}
		}
		return true;
	}
}
