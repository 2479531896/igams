package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.YyxxDto;
import com.matridx.igams.common.dao.post.IFjcfbDao;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum_pub;
import com.matridx.igams.common.enums.PersonalSettingEnum;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IFileInspectionImport;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IYyxxService;
import com.matridx.igams.common.util.FileDocumentImportThread;
import com.matridx.igams.common.util.FileImportThread;
import com.matridx.igams.common.util.FileInsertThread;
import com.matridx.igams.common.util.FileInspectionImportThread;
import com.matridx.igams.common.util.FileInspectionInsertThread;
import com.matridx.igams.common.util.FileNormalImportThread;
import com.matridx.igams.common.util.FileNormalInsertThread;
import com.matridx.igams.common.util.FileSpecialImportThread;
import com.matridx.igams.common.util.FileSpecialInsertThread;
import com.matridx.igams.common.util.RedisXmlConfig;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.excel.ExcelHelper;
import com.matridx.springboot.util.file.upload.RandomUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FjcfbServiceImpl extends BaseBasicServiceImpl<FjcfbDto, FjcfbModel, IFjcfbDao> implements IFjcfbService{
	@Value("${matridx.fileupload.prefix}")
	private String prefix;
	
	@Value("${matridx.fileupload.tempPath}")
	private String tempFilePath;
	
	@Value("${matridx.fileupload.releasePath}")
	private String releaseFilePath;
	
	/**
	 * 转换最大次数
	 */
	@Value("${matridx.file.translimit:5}")
	private final int FILE_TRANS_LIMIT = 5;
	
	/**
	 * FTP服务器下word的路径
	 */
	@Value("${matridx.ftp.wordpath:}")
	private final String FTPWORD_PATH = null;
	
	/**
	 * FTP服务器下pdf的路径
	 */
	@Value("${matridx.ftp.pdfpath:}")
	private final String FTPPDF_PATH = null;
	
	/**
	 * FTP服务器地址
	 * 在SpringBoot中使用@Value只能给普通变量赋值，不能给静态变量赋值
	 */
	@Value("${matridx.ftp.url:}")
	private final String FTP_URL = null;
	
	/**
	 * FTP服务器的用户名
	 * 在SpringBoot中使用@Value只能给普通变量赋值，不能给静态变量赋值
	 */
	@Value("${matridx.ftp.user:}")
	private final String FTP_USER = null;
	
	/**
	 * FTP服务器的密码
	 */
	@Value("${matridx.ftp.pd:}")
	private final String FTP_PD = null;

	/**
	 * FTP服务器的密码端口
	 */
	@Value("${matridx.ftp.port:}")
	private final String FTP_PORT = null;
	
	/**
	 * 文档转换完成OK
	 */
	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private final String DOC_OK = null;
	
	@Autowired
	IYyxxService yyxxService;
	@Autowired
	IGrszService grszService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	RedisXmlConfig redisXmlConfig;
	@Autowired(required=false)
	private AmqpTemplate amqpTempl;
	
	private final Logger log = LoggerFactory.getLogger(FjcfbServiceImpl.class);

	/**
	 * 保存文件到临时目录中
	 */
	public boolean save2TempFile(MultipartFile[] files,FjcfbDto fjcfbDto,User user){
		
		//根据日期创建文件夹
		String storePath = prefix + tempFilePath + fjcfbDto.getYwlx()+"/"+ "UP"+ 
				DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+ 
				DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
				DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		mkDirs(storePath);

		for (MultipartFile multipartFile : files) {
			if (multipartFile!=null&&!multipartFile.isEmpty()) {
				Map<String, Object> mFile = new HashMap<>();
				if (StringUtil.isNotBlank(fjcfbDto.getPrefjid())) {
					mFile.put("prefjid", fjcfbDto.getPrefjid());
				}
				String fileID = StringUtil.generateUUID();
				fjcfbDto.setFjid(fileID);
				String wjm = StringUtil.isNotBlank(fjcfbDto.getWjm())?fjcfbDto.getWjm():multipartFile.getOriginalFilename();
				mFile.put("wjm", wjm);
				fjcfbDto.setWjm(wjm);
				int index = (wjm.lastIndexOf(".") > 0) ? wjm.lastIndexOf(".") : wjm.length() - 1;

				String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
				String suffix = wjm.substring(index);
				String saveName = t_name + suffix;

				mFile.put("fwjlj", storePath);
				mFile.put("fwjm", saveName);
				mFile.put("ywlx", fjcfbDto.getYwlx());
				//将上传的文件存放到：路径前缀+指定路径(临时存放目录)
				boolean flag = uploadFile(multipartFile, storePath + "/" + saveName);
				if (!flag) {
					return false;
				}
				//为读取未转换过去的数据，还需保留一份文件，mod 判断类型  2020-10-13 lishangyaun
				if (".doc".equalsIgnoreCase(suffix) || ".docx".equalsIgnoreCase(suffix) || ".xls".equalsIgnoreCase(suffix)
						|| ".xlsx".equalsIgnoreCase(suffix) || ".pdf".equalsIgnoreCase(suffix) || ".txt".equalsIgnoreCase(suffix)) {
					String backName = t_name + "_back" + multipartFile.getOriginalFilename().substring(index);
					boolean backflag = uploadFile(multipartFile, storePath + "/" + backName);
					if (!backflag) {
						return false;
					}
					mFile.put("b_wjlj", storePath + "/" + backName);
				}
				mFile.put("wjlj", storePath + "/" + saveName);
				mFile.put("kzcs1", fjcfbDto.getKzcs1());
				mFile.put("kzcs2", fjcfbDto.getKzcs2());
				mFile.put("kzcs3", fjcfbDto.getKzcs3());

				fjcfbDto.setLsbcdz(storePath + "/" + saveName);
				//失效时间1小时
				redisUtil.hmset("IMP_:_" + fileID, mFile, 3600);

				//如果为导入，则开启线程进行导入
				if ("1".equals(fjcfbDto.getImpflg())) {
					if (BusTypeEnum.IMPORT_INSPECTION.getCode().equals(fjcfbDto.getYwlx())) {
						Map<String, String> impConfig = redisXmlConfig.getImpConfig(fjcfbDto.getYwlx());
						String classService = impConfig.get("class-service");
						IFileInspectionImport fileInspectionImport = (IFileInspectionImport) ServiceFactory.getService(classService);

						FileComInspectionImport fileComInspectionImport = new FileComInspectionImport();

						fileComInspectionImport.init(fjcfbDto, redisXmlConfig, fileInspectionImport, user);

						FileInspectionImportThread thread = new FileInspectionImportThread(fileComInspectionImport);

						thread.start();
					} else if (BusTypeEnum.IMP_DOCUMENT_BATCH.getCode().equals(fjcfbDto.getYwlx())) {
						Map<String, String> impConfig = redisXmlConfig.getImpConfig(fjcfbDto.getYwlx());
						String classService = impConfig.get("class-service");
						IFileImport fileImport = (IFileImport) ServiceFactory.getService(classService);

						FileComImport fileComImport = new FileComImport();

						fileComImport.init(fjcfbDto, redisXmlConfig, fileImport, user);

						FileDocumentImportThread thread = new FileDocumentImportThread(fileComImport);

						thread.start();
					} if (BusTypeEnum.IMP_SPECIALINSPECTION.getCode().equals(fjcfbDto.getYwlx())){
//						if (".zip".equals(suffix) || ".rar".equals(suffix))
//							return true;
						Map<String, String> impConfig = redisXmlConfig.getImpConfig(fjcfbDto.getYwlx());
						String classService = impConfig.get("class-service");
						IFileImport fileImport = (IFileImport) ServiceFactory.getService(classService);

						FileComImport fileComImport = new FileComImport();

						fileComImport.init(fjcfbDto, redisXmlConfig, fileImport, user);
						if(StringUtil.isBlank(fjcfbDto.getCheckFlg())){
							if (".xlsx".equals(suffix) || ".txt".equals(suffix) || ".zip".equals(suffix) || ".rar".equals(suffix)) {
								FileImportThread thread = new FileImportThread(fileComImport);
								thread.start();
							}
						}

					}else {
						if (".zip".equals(suffix) || ".rar".equals(suffix))
							return true;
						Map<String, String> impConfig = redisXmlConfig.getImpConfig(fjcfbDto.getYwlx());
						String classService = impConfig.get("class-service");
						IFileImport fileImport = (IFileImport) ServiceFactory.getService(classService);

						FileComImport fileComImport = new FileComImport();

						fileComImport.init(fjcfbDto, redisXmlConfig, fileImport, user);
						if (".xlsx".equals(suffix)) {
							FileImportThread thread = new FileImportThread(fileComImport);

							thread.start();
						}

					}
				}
				//如果为附件上传，则在相应业务的保存里把文件从临时文件夹里保存到正式文件夹里
//				else if ("2".equals(fjcfbDto.getImpflg())) {
//
//				}
				//如果为普通文件上传，则对文件进行确认，
				else if ("3".equals(fjcfbDto.getImpflg())) {

					Map<String, String> impConfig = redisXmlConfig.getImpConfig(fjcfbDto.getYwlx());
					String classService = impConfig.get("class-service");
					IFileImport fileImport = (IFileImport) ServiceFactory.getService(classService);

					FileComImport fileComImport = new FileComImport();

					fileComImport.init(fjcfbDto, redisXmlConfig, fileImport, user);

					FileNormalImportThread thread = new FileNormalImportThread(fileComImport);

					thread.start();

					//如果为送检上级表导入
					/*
					 * if("inspExperimentImport".equals(fjcfbDto.getImportType())) {
					 *
					 *
					 * FileNormalImportThread thread = new FileNormalImportThread(fileComImport);
					 *
					 * thread.start(); }
					 */
				} else if ("4".equals(fjcfbDto.getImpflg())) {
					//如果为其他固定样式
					Map<String, String> impConfig = redisXmlConfig.getImpConfig(fjcfbDto.getYwlx());
					String classService = impConfig.get("class-service");
					IFileImport fileImport = (IFileImport) ServiceFactory.getService(classService);

					FileComImport fileComImport = new FileComImport();

					fileComImport.init(fjcfbDto, redisXmlConfig, fileImport, user);

					FileSpecialImportThread thread = new FileSpecialImportThread(fileComImport);

					thread.start();
				}
			}
		}
		return true;
	}
	
	/**
	 * 根据文件ID从redis中获取信息后，把文件从临时文件夹中移动到正式文件夹中
	 */
	public boolean save2RealFile(String wjid,String ywid) {
		return saveFile(wjid,ywid,null);
	}

	/**
	 * 根据文件ID从redis中获取信息后，把文件从临时文件夹中移动到正式文件夹中
	 */
	public boolean save3RealFile(String wjid,String ywid,String zywid) {
		return saveFile(wjid,ywid,zywid);
	}
	/**
	 * 根据文件ID从redis中获取信息后，把文件从临时文件夹中移动到正式文件夹中,并创建新的fjid
	 */
	public boolean saveRealFileNewId(String wjid,String ywid) {
		return saveFileNewId(wjid,ywid,null);
	}
	

	/**
	 * 批量上传附件
	 */

	public boolean batchRealFile(String wjid,String ywid,String fjid) {
		Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+wjid);
		//如果文件信息不存在，则返回错误
		//文件路径
		String pathString = (String)mFile.get("fwjlj");
		//文件名
		String wjm = (String)mFile.get("wjm");
		//分文件名
		String fwjm = (String)mFile.get("fwjm");
		//文件全路径
		String wjlj = (String)mFile.get("wjlj");
		//业务类型
		String ywlx = (String)mFile.get("ywlx");

		//根据临时文件夹创建正式文件
		String t_path = pathString.substring(prefix.length() + tempFilePath.length());
		//分文件路径
		String real_path = prefix + releaseFilePath + t_path;

		mkDirs(real_path);
		//把文件从临时文件夹中移动到正式文件夹中，如果已经存在则覆盖
		CutFile(wjlj,real_path+"/"+fwjm);
		//将正式文件夹路径更新至数据库
		DBEncrypt bpe = new DBEncrypt();
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setFjid(fjid);
		fjcfbDto.setYwid(ywid);
		fjcfbDto.setXh("1");
		fjcfbDto.setYwlx(ywlx);
		fjcfbDto.setWjm(wjm);
		fjcfbDto.setWjlj(bpe.eCode(real_path+"/"+fwjm));
		fjcfbDto.setFwjlj(bpe.eCode(real_path));
		fjcfbDto.setFwjm(bpe.eCode(fwjm));
		fjcfbDto.setZhbj("0");
		dao.insert(fjcfbDto);
		return true;
	}
	/**
	 * 保存附件
	 */
	public boolean saveFile(String wjid,String ywid,String zywid) {
		Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+wjid);
		//如果文件信息不存在，则返回错误
		//文件路径
		String pathString = (String)mFile.get("fwjlj");
		//文件名
		String wjm = (String)mFile.get("wjm");
		//分文件名
		String fwjm = (String)mFile.get("fwjm");
		//文件全路径
		String wjlj = (String)mFile.get("wjlj");
		//业务类型
		String ywlx = (String)mFile.get("ywlx");
		
		//根据临时文件夹创建正式文件
		String t_path = pathString.substring(prefix.length() + tempFilePath.length());
		//分文件路径
		String real_path = prefix + releaseFilePath + t_path;
		
		mkDirs(real_path);
		//把文件从临时文件夹中移动到正式文件夹中，如果已经存在则覆盖
		CutFile(wjlj,real_path+"/"+fwjm);
		//将正式文件夹路径更新至数据库
		DBEncrypt bpe = new DBEncrypt();
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setFjid(wjid);
		fjcfbDto.setYwid(ywid);
		if(StringUtils.isNotBlank(zywid)) {
			fjcfbDto.setZywid(zywid);
		}else {
			fjcfbDto.setZywid("");
		}
		fjcfbDto.setXh("1");
		fjcfbDto.setYwlx(ywlx);
		fjcfbDto.setWjm(wjm);
		fjcfbDto.setWjlj(bpe.eCode(real_path+"/"+fwjm));
		fjcfbDto.setFwjlj(bpe.eCode(real_path));
		fjcfbDto.setFwjm(bpe.eCode(fwjm));
		fjcfbDto.setZhbj("0");
		dao.insert(fjcfbDto);
		return true;
	}
	/**
	 * 保存附件
	 */
	public boolean saveFileNewId(String wjid,String ywid,String zywid) {
		Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+wjid);
		//如果文件信息不存在，则返回错误
		//文件路径
		String pathString = (String)mFile.get("fwjlj");
		//文件名
		String wjm = (String)mFile.get("wjm");
		//分文件名
		String fwjm = (String)mFile.get("fwjm");
		//文件全路径
		String wjlj = (String)mFile.get("wjlj");
		//业务类型
		String ywlx = (String)mFile.get("ywlx");

		//根据临时文件夹创建正式文件
		String t_path = pathString.substring(prefix.length() + tempFilePath.length());
		//分文件路径
		String real_path = prefix + releaseFilePath + t_path;

		mkDirs(real_path);
		//把文件从临时文件夹中移动到正式文件夹中，如果已经存在则覆盖
		CutFile(wjlj,real_path+"/"+fwjm);
		//将正式文件夹路径更新至数据库
		DBEncrypt bpe = new DBEncrypt();
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setFjid(StringUtil.generateUUID());
		fjcfbDto.setYwid(ywid);
		if(StringUtils.isNotBlank(zywid)) {
			fjcfbDto.setZywid(zywid);
		}else {
			fjcfbDto.setZywid("");
		}
		fjcfbDto.setXh("1");
		fjcfbDto.setYwlx(ywlx);
		fjcfbDto.setWjm(wjm);
		fjcfbDto.setWjlj(bpe.eCode(real_path+"/"+fwjm));
		fjcfbDto.setFwjlj(bpe.eCode(real_path));
		fjcfbDto.setFwjm(bpe.eCode(fwjm));
		fjcfbDto.setZhbj("0");
		dao.insert(fjcfbDto);
		return true;
	}


	/**
	 * 保存带序号的附件
	 */
	public boolean saveXhFile(String wjid,String ywid,String zywid,String xh) {
		Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+wjid);
		//如果文件信息不存在，则返回错误
		//文件路径
		String pathString = (String)mFile.get("fwjlj");
		//文件名
		String wjm = (String)mFile.get("wjm");
		//分文件名
		String fwjm = (String)mFile.get("fwjm");
		//文件全路径
		String wjlj = (String)mFile.get("wjlj");
		//业务类型
		String ywlx = (String)mFile.get("ywlx");

		//根据临时文件夹创建正式文件
		String t_path = pathString.substring(prefix.length() + tempFilePath.length());
		//分文件路径
		String real_path = prefix + releaseFilePath + t_path;

		mkDirs(real_path);
		//把文件从临时文件夹中移动到正式文件夹中，如果已经存在则覆盖
		CutFile(wjlj,real_path+"/"+fwjm);
		//将正式文件夹路径更新至数据库
		DBEncrypt bpe = new DBEncrypt();
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setFjid(wjid);
		fjcfbDto.setYwid(ywid);
		if(StringUtils.isNotBlank(zywid)) {
			fjcfbDto.setZywid(zywid);
		}else {
			fjcfbDto.setZywid("");
		}
		fjcfbDto.setXh(xh);
		fjcfbDto.setYwlx(ywlx);
		fjcfbDto.setWjm(wjm);
		fjcfbDto.setWjlj(bpe.eCode(real_path+"/"+fwjm));
		fjcfbDto.setFwjlj(bpe.eCode(real_path));
		fjcfbDto.setFwjm(bpe.eCode(fwjm));
		fjcfbDto.setZhbj("0");
		dao.insert(fjcfbDto);
		return true;
	}

	@Override
	public List<FjcfbDto> selectFjcfbDtoByYwidAndYwlxOrderByYwlx(FjcfbDto fjcfbDto) {
		return dao.selectFjcfbDtoByYwidAndYwlxOrderByYwlx(fjcfbDto);
	}

	/**
	 * 批量保存附件到正式文件夹
	 */
	public boolean saveFormalFile(List<FjcfbDto> fjcfbDtos,String ywlx,String ywid,String zywid) {
		for (FjcfbDto fjcfbDto : fjcfbDtos) {
			//文件路径
//			String pathString = fjcfbDto.getFwjlj();
			//文件名
			String wjm = fjcfbDto.getWjm();
			//分文件名
//			String fwjm = fjcfbDtos.get(i).getFwjm();
			//文件全路径
			String wjlj = fjcfbDto.getWjlj();

			//根据日期创建文件夹
			String storePath = prefix + releaseFilePath + ywlx + "/" + "UP" +
					DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" +
					DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/" + "UP" +
					DateUtils.getCustomFomratCurrentDate("yyyyMMdd");

			mkDirs(storePath);
			int index = (wjm.lastIndexOf(".") > 0) ? wjm.lastIndexOf(".") : wjm.length() - 1;
			String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
			String suffix = wjm.substring(index);
			String saveName = t_name + suffix;
			//把文件从临时文件夹中移动到正式文件夹中，如果已经存在则覆盖
			CutFile(wjlj, storePath + "/" + saveName);
			//将正式文件夹路径更新至数据库
			DBEncrypt bpe = new DBEncrypt();
			fjcfbDto.setYwid(ywid);
			if (StringUtils.isNotBlank(zywid)) {
				fjcfbDto.setZywid(zywid);
			} else {
				fjcfbDto.setZywid("");
			}
			fjcfbDto.setXh("1");
			fjcfbDto.setYwlx(ywlx);
			fjcfbDto.setWjm(wjm);
			fjcfbDto.setWjlj(bpe.eCode(storePath + "/" + saveName));
			fjcfbDto.setFwjlj(bpe.eCode(storePath));
			fjcfbDto.setFwjm(bpe.eCode(saveName));
			fjcfbDto.setZhbj("0");
			dao.insert(fjcfbDto);
		}
		return true;
	}
	
	/**
	 * 根据路径创建文件
	 */
	private boolean mkDirs(String storePath)
	{
		File file = new File(storePath);
		if (file.isDirectory()) {
			return true;
		}
		return file.mkdirs();
	}
	
	/**
	 * 把文件转存到相应路径下
	 */
	private boolean uploadFile(MultipartFile file, String filePath)
	{
		boolean flag = false;
		byte[] buffer = new byte[4096];
		FileOutputStream fos = null;
		BufferedOutputStream output = null;

		InputStream fis = null;
		BufferedInputStream input = null;
		try {
			fis = file.getInputStream();
			input = new BufferedInputStream(fis);

			fos = new FileOutputStream(filePath);
			output = new BufferedOutputStream(fos);
			int n;
			while ((n = input.read(buffer, 0, 4096)) > -1) {
				output.write(buffer, 0, n);
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		} finally {
			closeStream(new Closeable[] { 
			fis, input, output, fos });
		}

		return flag;
	}
	
	/**
	 * 从原路径剪切到目标路径
	 */
	private boolean CutFile(String s_srcFile,String s_distFile) {
		//路径如果为空则直接返回错误
		if(StringUtil.isBlank(s_srcFile)|| StringUtil.isBlank(s_distFile))
			return false;
		
		File srcFile = new File(s_srcFile);
		File distFile = new File(s_distFile);
		//文件不存在则直接返回错误
		if(!srcFile.exists())
			return false;
		//目标文件已经存在
		if(distFile.exists()) {
			srcFile.renameTo(distFile);
		}else {
			srcFile.renameTo(distFile);
		}
		return true;
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
	
	/**
	 * 获取上传文件的信息列表
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getImpList(FjcfbDto fjcfbDto,HttpServletRequest request,User user){
		String fjid = fjcfbDto.getFjid();
		try{
			List<Map<String, String>> rows = new ArrayList<>();
			if(BusTypeEnum.IMPORT_INSPECTION.getCode().equals(fjcfbDto.getYwlx())){
				rows = (List<Map<String, String>>) redisUtil.hget("IMP_:_"+fjid,"sjxxList");
			}else if(BusTypeEnum.IMP_DOCUMENT_BATCH.getCode().equals(fjcfbDto.getYwlx())){
				rows = (List<Map<String, String>>) redisUtil.hget("IMP_:_"+fjid,"wjglList");
			}else{
				String bWjlj = String.valueOf(redisUtil.hget("IMP_:_" + fjid, "b_wjlj"));
				if (StringUtil.isBlank(bWjlj) || "null".equals(bWjlj)){
					bWjlj = String.valueOf(redisUtil.hget("IMP_:_" + fjid, "wjlj"));
				}
				//txt文件数据回显页面
				if (bWjlj.endsWith(".txt") || bWjlj.endsWith(".zip")){
					Map<String, String> impConfig = redisXmlConfig.getImpConfig(fjcfbDto.getYwlx());
					String classService = impConfig.get("class-service");
					IFileImport fileImport = (IFileImport) ServiceFactory.getService(classService);
					FileComImport fileComImport = new FileComImport();
					fileComImport.init(fjcfbDto, redisXmlConfig, fileImport,null);
					Map<String,String> jsonmap = new HashMap<>();
					String sjdw = request.getParameter("sjdw");
					String sjdwmc = request.getParameter("sjdwmc");
					if (StringUtil.isNotBlank(sjdw)){
						YyxxDto yyxxDto = yyxxService.getDtoById(sjdw);
						if (yyxxDto!= null){
							jsonmap.put("sjdw",sjdw);
							jsonmap.put("sjdwmc",yyxxDto.getDwmc());
							GrszDto grszDto=new GrszDto();
							grszDto.setYhid(user.getYhid());
							grszDto.setSzlb(PersonalSettingEnum.HOSPITAL_SELECT.getCode());
							grszService.delete(grszDto);
							grszDto.setSzid(StringUtil.generateUUID());
							grszDto.setSzz(JSONObject.toJSONString(jsonmap));
							grszService.insert(grszDto);
							jsonmap.put("jcxmpp",yyxxDto.getJcxmpp());
							jsonmap.put("txtSet",yyxxDto.getCskz4());
							if (StringUtil.isNotBlank(yyxxDto.getCskz3())) {
								String[] split = yyxxDto.getCskz3().split(",");
								jsonmap.put("db",split[1]);
								jsonmap.put("jcdwdm",split[3]);
							}
						}
					}

					redisUtil.hset("IMP_:_"+fjid,"settings", JSONObject.toJSONString(jsonmap));
					fileComImport.readTxtOrZip(rows,bWjlj);
				} else {
					ExcelHelper eh = new ExcelHelper();
					rows= eh.readExcel(String.valueOf(redisUtil.hget("IMP_:_"+fjid,"b_wjlj")));
					List<String> dtoFiledsName = parseHead(rows);
					changeMapInfo(rows,dtoFiledsName);
				}
			}
			return rows;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据附件ID获取导入信息
	 */
	public Map<String, Object> checkImpFileProcess(String fjid){

		Map<String, Object> result = new HashMap<>();
		
		Object o_error = redisUtil.hget("IMP_:_"+fjid,"errorMessage");
		String errorMessage = String.valueOf(o_error==null?"":o_error);
		Object o_warn = redisUtil.hget("IMP_:_"+fjid,"warnMessage");
		String warnMessage = String.valueOf(o_warn==null?"":o_warn);
		Object o_confirm = redisUtil.hget("IMP_:_"+fjid,"confirmMessage");
		String confirmMessage = String.valueOf(o_confirm==null?"":o_confirm);
		Object o_hint = redisUtil.hget("IMP_:_"+fjid,"hintMessage");
		String hintMessage = String.valueOf(o_hint==null?"":o_hint);
		Object o_msg = redisUtil.hget("IMP_:_"+fjid,"msg");
		String msg = String.valueOf(o_msg==null?"":o_msg);
		Object o_endflg = redisUtil.hget("IMP_:_"+fjid,"endflg");
		String endflg = String.valueOf(o_endflg==null?"":o_endflg);
		Object o_filename = redisUtil.hget("IMP_:_"+fjid,"wjm");
		String filename = String.valueOf(o_filename==null?"":o_filename);
		result.put("filename", filename);
		if(StringUtil.isNotBlank(errorMessage))
			result.put("errormsg", "<h3>错误信息：(因有错误，文件无法导入到系统中)</h3><br>" + errorMessage);
		if(StringUtil.isNotBlank(warnMessage))
			result.put("warnmsg", "<h3>警告信息：</h3><br>" + warnMessage);
		if(StringUtil.isNotBlank(confirmMessage))
			result.put("confirmmsg", "<h3>确认信息：</h3><br>" + confirmMessage);
		if(StringUtil.isNotBlank(hintMessage))
			result.put("infomsg", "<h3>提示信息：</h3><br>" + hintMessage);
		
		result.put("msg", msg);
		result.put("endflg", endflg);
		
		return result;
	}
	
	/**
	 * 保存导入信息到数据库
	 */
	public boolean save2Db(FjcfbDto fjcfbDto,User user){
		
		Map<String, String> impConfig = redisXmlConfig.getImpConfig(fjcfbDto.getYwlx());
		String classService = impConfig.get("class-service");
		IFileImport fileImport = (IFileImport)ServiceFactory.getService(classService);
		
		FileComImport fileComImport = new FileComImport();
		fileComImport.init(fjcfbDto, redisXmlConfig, fileImport,user);
		
		//如果为普通文件上传，则对文件进行确认，
		if("3".equals(fjcfbDto.getImpflg())) {
			FileNormalInsertThread thread = new FileNormalInsertThread(fileComImport);
			
			thread.start();
		}else if("4".equals(fjcfbDto.getImpflg())) {
			//如果为特殊文件上传，则对文件进行确认
			FileSpecialInsertThread thread = new FileSpecialInsertThread(fileComImport);
			
			thread.start();
		}else {
			FileInsertThread thread = new FileInsertThread(fileComImport);
			
			thread.start();
		}
		
		return true;
	}
	
	
	/**
	 * 保存送检报告
	 */
	@Override
	public boolean save2Inspection(FjcfbDto fjcfbDto, User user) {
		// TODO Auto-generated method stub
		Map<String, String> impConfig = redisXmlConfig.getImpConfig(fjcfbDto.getYwlx());
		String classService = impConfig.get("class-service");
		IFileInspectionImport fileInspectionImport = (IFileInspectionImport)ServiceFactory.getService(classService);
		
		FileComInspectionImport fileComInspectionImport = new FileComInspectionImport();
		fileComInspectionImport.init(fjcfbDto, redisXmlConfig, fileInspectionImport,user);
		FileInspectionInsertThread thread = new FileInspectionInsertThread(fileComInspectionImport);
		
		thread.start();
		return true;
	}
	
	/**
	 * 根据附件ID检查插入数据库进度
	 */
	public Map<String, Object> checkImpRecordProcess(String fjid){
		Map<String, Object> result = new HashMap<>();
		
		Object o_insertMsg = redisUtil.hget("IMP_:_"+fjid,"insertMsg");
		String insertMsg = String.valueOf(o_insertMsg==null?"":o_insertMsg);
		
		Object o_insertEndflg = redisUtil.hget("IMP_:_"+fjid,"insertEndflg");
		String insertEndflg = String.valueOf(o_insertEndflg==null?"":o_insertEndflg);
		
		Object o_insertWarn = redisUtil.hget("IMP_:_"+fjid,"insertWarn");
		String insertWarn = String.valueOf(o_insertWarn==null?"":o_insertWarn);
		
		Object o_insertError = redisUtil.hget("IMP_:_"+fjid,"insertError");
		String insertError = String.valueOf(o_insertError==null?"":o_insertError);
		
		result.put("insertMsg", insertMsg);
		result.put("insertEndflg", insertEndflg);
		result.put("insertWarn", insertWarn);
		result.put("insertError", insertError);
		
		return result;
	}
	
	/**
	  * 返回需要插入的附件
	 */
	public List<FjcfbModel> update(String busId, String relatedBusId, List<String> busTypes, List<FjcfbModel> fjs){
		
		DBEncrypt crypt = new DBEncrypt();
				
		FjcfbDto fModel = new FjcfbDto();
		fModel.setYwid(busId);
		fModel.setZywid(relatedBusId);
		fModel.setYwlxs(busTypes);
		List<FjcfbModel> list = dao.getModelListByYwid(fModel);//该id现有的附件
		List<FjcfbModel> addllList = new ArrayList<>();
		//如果fjs为空，则删除所有附件
		if((null == fjs || fjs.isEmpty()) && (list.isEmpty())){
			deleteByBus(busId, relatedBusId, busTypes);
			addllList=list;
		}
		//将不必要的附件删除
		List<String> delList = new ArrayList<>();
		
		for(FjcfbModel fj : list){
			//是否删除
			boolean isDel = true;
			for(FjcfbModel rfj : fjs){
				if(null == rfj){
					continue;
				}
				if(fj.getFjid().equals(rfj.getFjid())){
					isDel = false;
					break;
				}
			}
			if(isDel){
				//删除正式文件
				File file = new File(GlobalString.FILE_STOREPATH_PREFIX+crypt.dCode(fj.getWjlj()));
				file.delete();
				delList.add(fj.getFjid());
				addllList.add(fj);
			}
		}
		if(!delList.isEmpty()){
			super.batchDelete(delList);
		}
		//如果fjs为空，则删除所有附件
		if(fjs==null||fjs.isEmpty()){
			deleteByBus(busId, relatedBusId, busTypes);
			return fjs;
		}
		//如果list为空，则将fjs中所有的附件插入
		if(list==null||list.isEmpty()){
			// 去null
			List<FjcfbModel> fjResult = new ArrayList<>();
			for (FjcfbModel fj : fjs) {
				if (fj != null) {
					fjResult.add(fj);
				}
			}
			return fjResult;
		}
		//获取需要插入的附件
		List<FjcfbModel> result = new ArrayList<>();
		for(FjcfbModel fj : fjs){
			if(null == fj){
				continue;
			}
			//如果业务ID为空，则表示该附件需要插入
			if(StringUtil.isBlank(fj.getYwid()) && StringUtil.isBlank(fj.getZywid())){
				result.add(fj);
			}else{
				boolean isAdd = true;
				for(FjcfbModel mfj : list){
					if(mfj.getFjid().equals(fj.getFjid())){
						isAdd = false;
						break;
					}
				}
				if(isAdd){
					result.add(fj);
				}
			}
		}
		
		return result;
	}
	
	/**
	  * 删除
	 */
	public boolean deleteByBus(String ywid, String zywid, List<String> ywlxs) {
		FjcfbDto fj = new FjcfbDto();
		fj.setYwid(ywid);
		fj.setZywid(zywid);
		fj.setYwlxs(ywlxs);
		List<FjcfbModel> list = dao.getModelListByYwid(fj);
		
	  	DBEncrypt crypt = new DBEncrypt();
		if(list!=null&&!list.isEmpty()){
			List<String> ids = new ArrayList<>();
			for(FjcfbModel model : list){
				//删除正式文件
				File file = new File(GlobalString.FILE_STOREPATH_PREFIX+crypt.dCode(model.getWjlj()));
				file.delete();
				ids.add(model.getFjid());
			}
			return super.batchDelete(ids);
		}else{
			return true;
		}
	}
	
	@Override
	public String getDRNextSeqNum(FjcfbModel fjcfbModel) {
		return dao.getDRNextSeqNum(fjcfbModel);
	}

	@Override
	public boolean updateSeqNum(String ywid, List<String> ywlxs) {
		boolean reslut = true;
		for (String ywlx : ywlxs) {
			FjcfbModel fModel = new FjcfbModel();
			fModel.setYwid(ywid);
			fModel.setYwlx(ywlx);
			dao.updateSeqNum(fModel);
		}
		return reslut;
	}
	
	/**
	 * 2.1、解析表头数据，同时返回第一行的字段列表信息
	 * @author goofus
	 */
	private List<String> parseHead(List<Map<String,String>> rows) {
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
			
			if(StringUtil.isBlank(head)) {
				continue;
			}

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
		rows.remove(0);
		
		return dtoFieldNames;
	}
	
	/**
	 * 把文件信息的字段key转换成 字段key
	 */
	private void changeMapInfo(List<Map<String,String>> rows,List<String> dtoFieldNames){
		//表头信息解析
		for (Map<String, String> rowinfo : rows) {
			for (int i = 0, j = dtoFieldNames.size(); i < j; i++) {
				String value = rowinfo.get(i + "");
				rowinfo.remove(i + "");
				rowinfo.put(dtoFieldNames.get(i), value);
			}

		}
	}
	
	/**
	 * 转换失败更新相应的转换次数
	 */
	public int updateFjljFail(FjcfbModel fjcfbModel) {
		return dao.updateFjljFail(fjcfbModel);
	}
	
	public int getFileTransLimit() {
		return FILE_TRANS_LIMIT;
	}
	
	public String getFtpWordPath() {
		return FTPWORD_PATH;
	}
	
	public String getFtpPdfPath() {
		return FTPPDF_PATH;
	}
	@Override
	public String getFtpPath(){
		// TODO Auto-generated method stub
		return null;
	}
	public String getFtpUrl() {
		return FTP_URL;
	}
	
	public String getFtpUser() {
		return FTP_USER;
	}
	
	public String getFtpPD() {
		return FTP_PD;
	}
	
	public String getFtpPort() {
		return FTP_PORT;
	}

	/**
	 * 通过业务ID查询附件信息
	 */
	@Override
	public List<FjcfbDto> selectFjcfbDtoByYwid(String ywid) {
		// TODO Auto-generated method stub
		return dao.selectFjcfbDtoByYwid(ywid);
	}

	/**
	 * 通过业务ID删除附件信息
	 */
	@Override
	public boolean deleteByYwid(FjcfbModel fjcfbModel) {
		// TODO Auto-generated method stub
		int result = dao.deleteByYwid(fjcfbModel);
		return result != 0;
	}

	/**
	 * 更新转换标记
	 */
	@Override
	public boolean updateZhbj(FjcfbModel fjcfbModel) {
		// TODO Auto-generated method stub
		int result = dao.updateZhbj(fjcfbModel);
		return result != 0;
	}

	/**
	 * 通过业务ID和业务类型查询附件信息
	 */
	@Override
	public List<FjcfbDto> selectFjcfbDtoByYwidAndYwlx(FjcfbDto fjcfbDto) {
		// TODO Auto-generated method stub
		return dao.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
	}
	
	/**
	 * 附件排序
	 */
	@Override
	public boolean fileSort(FjcfbDto fjcfbDto) {
		// TODO Auto-generated method stub
		int result = dao.fileSort(fjcfbDto);
		return result != 0;
	}

	/**
	 * 删除文件
	 */
	@Override
	public boolean delFile(FjcfbDto fjcfbDto) {
		// TODO Auto-generated method stub
		String wjlj = fjcfbDto.getWjlj();
		boolean result = delete(fjcfbDto);
		String filePath;
		if(result){
			DBEncrypt crypt = new DBEncrypt();
			filePath = crypt.dCode(wjlj);
		}else{
			Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+fjcfbDto.getFjid());
			if(mFile!=null&&mFile.size()>0){
				redisUtil.del("IMP_:_"+fjcfbDto.getFjid());
				filePath = (String)mFile.get("wjlj");
			}else{
				return false;
			}
		}
		try {
			File file = new File(filePath);
			result = file.delete();
			if(!result)
				return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * 转换后的文件新增一个zhwjxx
	 */
	@Override
	public boolean updateZhwjxx(FjcfbModel fjcfbModel){
		// TODO Auto-generated method stub
		return dao.updateZhwjxx(fjcfbModel);
	}
	
	/**
	 * 查询没有转换过的文件
	 */
	@Override
	public List<FjcfbDto> selectWord(){
		// TODO Auto-generated method stub
		List<FjcfbDto> fjcfblist=dao.selectWord();
		for (int i = fjcfblist.size()-1; i >=0; i--){
			DBEncrypt p = new DBEncrypt();
			String wjljjm=p.dCode(fjcfblist.get(i).getWjlj());
			boolean sendFlg=sendWordFile(wjljjm);
			if(sendFlg) {
				Map<String,String> map= new HashMap<>();
				String fwjm=p.dCode(fjcfblist.get(i).getFwjm());
				map.put("wordName", fwjm);
				map.put("fwjlj",fjcfblist.get(i).getFwjlj());
				map.put("fjid",fjcfblist.get(i).getFjid());
				map.put("ywlx",fjcfblist.get(i).getYwlx());
				map.put("MQDocOkType",DOC_OK);
				//发送Rabbit消息转换pdf
				amqpTempl.convertAndSend("doc2pdf_exchange",MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(map));
			}
		}
		return null;
	}
	/**
	 * 上传Word文件
	 */
	public boolean sendWordFile(String fileName) {
		//连接服务器
		//FTPClient ftp=FtpUtil.connect(FTPWORD_PATH, FTP_URL, FTP_PORT, FTP_USER, FTP_PD );
		//上传到服务器word下边的文件夹
		//FtpUtil.upload(new File(wjljjm), ftp);
		try {
			File t_file = new File(fileName);
			//文件不存在不做任何操作
			if(!t_file.exists())
				return true;
			
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
	 * 根据业务ID和业务类型删除附件信息
	 */
	@Override
	public boolean deleteByYwidAndYwlx(FjcfbModel fjcfbModel) {
		// TODO Auto-generated method stub
		int result = dao.deleteByYwidAndYwlx(fjcfbModel);
		return result != 0;
	}

	/**
	 * 根据业务IDs和业务类型删除附件信息
	 */
	public int deleteByYwidsAndYwlx(FjcfbDto fjcfbDto){
		return dao.deleteByYwidsAndYwlx(fjcfbDto);
	}
	
	/**
	 * 查询是否有转换为pdf的文件
	 */
	public List<FjcfbDto> selectzhpdf(FjcfbDto fjcfbDto) {
		return dao.selectzhpdf(fjcfbDto);
	}

	/**
	 * 根据业务ID和序号查询
	 */
	@Override
	public List<FjcfbDto> getListByYwidAndXh(List<FjcfbDto> fjcfbDtos) {
		// TODO Auto-generated method stub
		return dao.getListByYwidAndXh(fjcfbDtos);
	}

	/**
	 * 替换文件
	 */
	@Override
	public boolean replaceFile(FjcfbModel fjcfbModel) {
		// TODO Auto-generated method stub
		int result = dao.replaceFile(fjcfbModel);
		return result != 0;
	}

	/**
	 * 接收word文件转换成pdf
	 */
	@Override
	public String receiveWord(MultipartFile file) {
		// TODO Auto-generated method stub
		String wjm = file.getOriginalFilename();
		//根据日期创建文件夹
		String fwjlj = prefix + tempFilePath + BusTypeEnum.IMP_TEMP_WORD.getCode();
		mkDirs(fwjlj);
		int index = (wjm.lastIndexOf(".") > 0) ? wjm.lastIndexOf(".") : wjm.length() - 1;
		String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
		String fwjm = t_name + wjm.substring(index);
		String wjlj = fwjlj + "/" + fwjm;
		
		byte[] buffer = new byte[4096];
		FileOutputStream fos = null;
		BufferedOutputStream output = null;

		InputStream fis = null;
		BufferedInputStream input = null;
		try {
			fis = file.getInputStream();
			input = new BufferedInputStream(fis);
			mkDirs(fwjlj);
			fos = new FileOutputStream(wjlj);
			output = new BufferedOutputStream(fos);
			int n;
			while ((n = input.read(buffer, 0, 4096)) > -1) {
				output.write(buffer, 0, n);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			closeStream(new Closeable[] { fis, input, output, fos });
		}
		
		int begin = wjm.lastIndexOf(".");
		String wjmkzm = wjm.substring(begin);
		if((wjmkzm.equalsIgnoreCase(".doc"))||(wjmkzm.equalsIgnoreCase(".docx"))||(wjmkzm.equalsIgnoreCase(".xls"))||(wjmkzm.equalsIgnoreCase(".xlsx"))) {
			//上传Word文件
			boolean sendFlg = sendWordFile(wjlj);
			if(sendFlg) {
				Map<String,String> map= new HashMap<>();
				DBEncrypt p = new DBEncrypt(); 
				map.put("wordName", fwjm);
				map.put("fwjlj",p.eCode(fwjlj));
				map.put("ywlx",BusTypeEnum.IMP_TEMP_WORD.getCode());
				map.put("MQDocOkType",DOC_OK);
				//发送Rabbit消息转换pdf
				amqpTempl.convertAndSend("doc2pdf_exchange",MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(map));
			}else{
				return null;
			}
		}
		return updateSuffix(fwjm);
	}
	
	/**
	 * 将后缀修改为pdf
	 */
	private String updateSuffix(String filename){
		if ((filename != null) && (!filename.isEmpty())) {
			int dot = filename.lastIndexOf('.'); 
			if (dot >-1) {
				String substring = filename.substring(0, dot);
				filename = substring.concat(".pdf");
				return filename;
			}
		}
        return filename;
	}
	
	/**
	 * 根据fjids删除附件
	 */
	public boolean delByFjids(FjcfbDto fjcfbDto) {
		return dao.delByFjids(fjcfbDto);
	}

	/**
	 * 从缓存中获取附件信息
	 */
	@Override
	public List<FjcfbDto> getRedisList(List<String> fjids) {
		// TODO Auto-generated method stub
		List<FjcfbDto> fjcfbDtos = new ArrayList<>();
		if(fjids!=null&&!fjids.isEmpty()) {
			for (String fjid : fjids) {
				Map<Object, Object> mFile = redisUtil.hmget("IMP_:_" + fjid);
				//如果文件信息不存在，则跳过
				if (mFile == null){
					continue;
				}
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setFjid(fjid);
				//文件名
				String wjm = (String) mFile.get("wjm");
				fjcfbDto.setWjm(wjm);
				//文件全路径
				String wjlj = (String) mFile.get("wjlj");
				//业务类型
				String ywlx = (String) mFile.get("ywlx");
				fjcfbDto.setYwlx(ywlx);
				fjcfbDto.setWjlj(wjlj);
				fjcfbDtos.add(fjcfbDto);
			}
		}
		return fjcfbDtos;
	}

	/**
	 * 根据业务ID和子业务ID查询附件信息
	 */
	@Override
	public List<FjcfbDto> getListByZywid(FjcfbDto fjcfbDto) {
		// TODO Auto-generated method stub
		return dao.getListByZywid(fjcfbDto);
	}

	/**
	 * 根据业务IDs和业务类型
	 */
	public List<FjcfbDto> selectFjcfbDtoByYwidsAndYwlx(FjcfbDto fjcfbDto){
		return dao.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto);
	}
	
	/**
	 * 通过附件IDs查询删除标记为0的附件信息
	 */
	public List<FjcfbDto> selectFjcfbDtoByFjids(FjcfbDto fjcfbDto){
		return dao.selectFjcfbDtoByFjids(fjcfbDto);
	}

	/**
	 * 批量更新附件存放表
	 */
	@Override
	public boolean updateByYwidAndWjmhz(List<FjcfbDto> fjcfbDtos) {
		int i = dao.updateByYwidAndWjmhz(fjcfbDtos);
		return i > 0;
	}
	
	@Override
	public boolean delFileOnlyFjcfb(FjcfbDto fjcfbDto) {
		return delete(fjcfbDto);
	}

	/**
	 * 重写getDto方法，防止无条件SQL执行
	 */
	public FjcfbDto getDto(FjcfbDto fjcfbDto){
		if(StringUtil.isBlank(fjcfbDto.getYwid()) && StringUtil.isBlank(fjcfbDto.getFjid()) && StringUtil.isBlank(fjcfbDto.getYwlx()) )
			return null;
		return dao.getDto(fjcfbDto);
	}

	/**
	 * 查询删除标记不等于1的附件信息
	 */
	public FjcfbDto getDtoWithScbjNotOne(FjcfbDto fjcfbDto){
		if(StringUtil.isBlank(fjcfbDto.getYwid()) && StringUtil.isBlank(fjcfbDto.getFjid()) && StringUtil.isBlank(fjcfbDto.getYwlx()) )
			return null;
		return dao.getDtoWithScbjNotOne(fjcfbDto);
	}

	@Override
	public List<FjcfbDto> selectFjcfbDtoByIds(FjcfbDto fjcfbDto) {
		return dao.selectFjcfbDtoByIds(fjcfbDto);
	}


	/**
	 * 批量新增附件存放表信息
	 */
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	@Override
	public boolean batchInsertFjcfb(List<FjcfbDto> list) {
		int result = dao.batchInsertFjcfb(list);
		return result > 0;
	}

	/**
	 * 通过业务ID查询删除标记为0的附件信息
	 */
	@Override
	public List<FjcfbDto> getFjcfbDtoByYwid(String yzid) {
		return dao.getFjcfbDtoByYwid(yzid);
	}
	
	/**
	 * 根据fjid 更新删除标记
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteByFjid(FjcfbDto fjcfbDto) {
		int result = dao.deleteByFjid(fjcfbDto);
		return result > 0;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateScbjYwidAndYwlxd(FjcfbDto fjcfbDto) {
		int result = dao.updateScbjYwidAndYwlxd(fjcfbDto);
		return result > 0;
	}

	@Override
	public boolean saveRealFileForPeo(String wjid, String ywid, String lrry) {
		return saveFileForPeo(wjid,ywid,null,lrry);
	}
	/**
	 * 保存附件
	 */
	public boolean saveFileForPeo(String wjid,String ywid,String zywid,String lrry) {
		Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+wjid);
		//如果文件信息不存在，则返回错误
		//文件路径
		String pathString = (String)mFile.get("fwjlj");
		//文件名
		String wjm = (String)mFile.get("wjm");
		//分文件名
		String fwjm = (String)mFile.get("fwjm");
		//文件全路径
		String wjlj = (String)mFile.get("wjlj");
		//业务类型
		String ywlx = (String)mFile.get("ywlx");

		//根据临时文件夹创建正式文件
		String t_path = pathString.substring(prefix.length() + tempFilePath.length());
		//分文件路径
		String real_path = prefix + releaseFilePath + t_path;

		mkDirs(real_path);
		//把文件从临时文件夹中移动到正式文件夹中，如果已经存在则覆盖
		CutFile(wjlj,real_path+"/"+fwjm);
		//将正式文件夹路径更新至数据库
		DBEncrypt bpe = new DBEncrypt();
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setFjid(wjid);
		fjcfbDto.setYwid(ywid);
		if(StringUtils.isNotBlank(zywid)) {
			fjcfbDto.setZywid(zywid);
		}else {
			fjcfbDto.setZywid("");
		}
		fjcfbDto.setXh("1");
		fjcfbDto.setYwlx(ywlx);
		fjcfbDto.setWjm(wjm);
		fjcfbDto.setWjlj(bpe.eCode(real_path+"/"+fwjm));
		fjcfbDto.setFwjlj(bpe.eCode(real_path));
		fjcfbDto.setFwjm(bpe.eCode(fwjm));
		fjcfbDto.setZhbj("0");
		fjcfbDto.setLrry(lrry);
		dao.insert(fjcfbDto);
		return true;
	}

	@Override
	public List<FjcfbDto> existFileInfo(FjcfbDto fjcfbDto) {
		return dao.existFileInfo(fjcfbDto);
	}

	/**
	 * 删除失效文件(指定文件夹、指定文件路径)
	 * @param map
	 */
	public void deleteInvalidFiles(Map<String, String> map){
		String ywlxs_str = map.get("ywlxs");
		List<String> ywlxs = new ArrayList<>();
		if (StringUtil.isNotBlank(ywlxs_str)) {
			ywlxs = Arrays.asList(ywlxs_str.split("@"));
		}
		String fileTypes_str = map.get("fileTypes");
		List<String> fileTypes = new ArrayList<>();
		if (StringUtil.isNotBlank(fileTypes_str)) {
			fileTypes = Arrays.asList(fileTypes_str.split("@"));
		}
		// 文件路径List(若未配置,则默认为临时文件夹+正式文件夹 + ywlxs)
		String filePaths_str = map.get("filePaths");
		List<String> filePaths = new ArrayList<>();
		if (StringUtil.isNotBlank(filePaths_str)) {
			filePaths = Arrays.asList(filePaths_str.split("@"));
		} else if (CollectionUtils.isEmpty(ywlxs)) {
			for (String ywlx : ywlxs) {
				filePaths.add(prefix + tempFilePath + ywlx);
				filePaths.add(prefix + releaseFilePath + ywlx);
			}
		} else {
			return;
		}
		String result = "";
		if(StringUtil.isNotBlank(map.get("startTime"))) {
			LocalDate dateAgo = LocalDate.now().minusDays(Long.parseLong(map.get("startTime")));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			result = dateAgo.format(formatter);
		}
		Map<String,Object> searchMap = new HashMap<>();
		if(StringUtil.isNotBlank(result)) {
			searchMap.put("startTime",result);
		}
		searchMap.put("fileTypes",fileTypes);
		searchMap.put("ywlxs",ywlxs);
		DBEncrypt dbEncrypt = new DBEncrypt();
		List<String> notDelFiles = new ArrayList<>();
		List<FjcfbDto> fjcfbDtos = dao.getListByFilePaths(searchMap);
		for (FjcfbDto fjcfbDto : fjcfbDtos) {
			notDelFiles.add(dbEncrypt.dCode(fjcfbDto.getWjlj()));
		}
		String time = map.get("time");
		String dateNum = "-" + (StringUtil.isNotBlank(time) ? time : "365");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, Integer.parseInt(dateNum));
		if (!CollectionUtils.isEmpty(filePaths) && !CollectionUtils.isEmpty(fileTypes)) {
			for (String filePath : filePaths) {
				File file = new File(filePath);
				deleteFiles(file, fileTypes, cal.getTimeInMillis(), notDelFiles);
			}
		}
	}

	public void deleteFiles(File file, List<String> fileTypes, long dateTime, List<String> notDelFiles) {
		if (file.exists()) {
			if (file.isFile()) {
				long lastModified = file.lastModified();// 文件最后修改时间
				if (lastModified >= dateTime) {
					return;
				}
				String filename = file.getName();
				for (String type : fileTypes) {
					if (filename.endsWith(type)) {
						String path = file.getPath().replaceAll("\\\\","/");
						if (!notDelFiles.contains(path)){
							log.error("删除失效文件：" + file.getPath());
							file.delete();
							break;
						}
					}
				}
			}
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File value : files) {
					deleteFiles(value, fileTypes, dateTime, notDelFiles);
				}
			}
		}
	}
}
