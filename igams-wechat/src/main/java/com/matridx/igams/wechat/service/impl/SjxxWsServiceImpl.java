package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JkdymxDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.WbaqyzDto;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.ErrorTypeEnum;
import com.matridx.igams.common.enums.InvokingChildTypeEnum;
import com.matridx.igams.common.enums.InvokingTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJkdymxService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IWbaqyzService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.wechat.dao.entities.*;
import com.matridx.igams.wechat.dao.post.ISjxxWsDao;
import com.matridx.igams.wechat.service.svcinterface.ICkwxglService;
import com.matridx.igams.wechat.service.svcinterface.ICwglService;
import com.matridx.igams.wechat.service.svcinterface.IDlyzzsService;
import com.matridx.igams.wechat.service.svcinterface.IFjsqService;
import com.matridx.igams.wechat.service.svcinterface.INyysxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjbgljxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjbgsmService;
import com.matridx.igams.wechat.service.svcinterface.ISjdlxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjjcxmService;
import com.matridx.igams.wechat.service.svcinterface.ISjkzxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjlcznService;
import com.matridx.igams.wechat.service.svcinterface.ISjnyxService;
import com.matridx.igams.wechat.service.svcinterface.ISjsyglService;
import com.matridx.igams.wechat.service.svcinterface.ISjwzxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxCommonService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxWsService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxjgService;
import com.matridx.igams.wechat.service.svcinterface.ISpikerpmService;
import com.matridx.igams.wechat.service.svcinterface.IWbhbyzService;
import com.matridx.igams.wechat.service.svcinterface.IWbsjxxService;
import com.matridx.igams.wechat.service.svcinterface.IWzglService;
import com.matridx.igams.wechat.service.svcinterface.IWzysxxService;
import com.matridx.igams.wechat.thread.JhnyDisposeFileThread;
import com.matridx.igams.wechat.thread.JhnyDisposeFileThreadSec;
import com.matridx.igams.common.util.InterfaceExceptionUtil;
import com.matridx.igams.wechat.util.MatchingUtil;
import com.matridx.igams.wechat.util.WordTemeplateThread;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.encrypt.Encrypt;
import com.matridx.springboot.util.file.upload.RandomUtil;
import com.matridx.springboot.util.file.upload.ZipUtil;
import com.matridx.springboot.util.xml.BasicXmlReader;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class SjxxWsServiceImpl extends BaseBasicServiceImpl<SjxxDto, SjxxModel, ISjxxWsDao>  implements ISjxxWsService{

	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.xiansheng.url:}")
	private String XIANSHENG_URL;
	@Autowired
	IWbaqyzService wbaqyzService;
	@Autowired
	IWbhbyzService wbhbyzService;
	@Autowired
	private ISjxxService sjxxService;
	@Autowired
	private IWbsjxxService wbsjxxService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	ICommonService commonService;
	@Autowired
	ISjwzxxService sjwzxxService;
	@Autowired
	ISjlcznService sjlcznService;
	@Autowired
	IWzglService wzglService;
	@Autowired
	ISjbgsmService sjbgsmService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IShgcService shgcService;
	@Autowired
	ISjnyxService sjnyxService;
	@Autowired
	IDlyzzsService dlyzzsService;
	@Autowired
	ICwglService cwglService;
	@Autowired
	ISjxxCommonService sjxxCommonService;
	@Autowired
	ISpikerpmService spikerpmService;
	@Autowired
	IFjsqService fjsqService;
	@Autowired
	ISjbgljxxService sjbgljxxService;
	@Autowired
	IJkdymxService jkdymxService;
	@Autowired
	IXxdyService xxdyService;
	@Autowired
	ISjdlxxService sjdlxxService;
	@Autowired
	IWzysxxService wzysxxService;
	@Autowired
	INyysxxService nyysxxService;
	@Autowired
	ISjxxjgService sjxxjgService;
	@Autowired
	ISjjcxmService sjjcxmService;
	@Autowired
	ISjsyglService sjsyglService;
	@Autowired
	ICkwxglService ckwxglService;
	@Autowired
	InterfaceExceptionUtil interfaceExceptionUtil;

	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	@Value("${matridx.fileupload.prefix}")
	private String prefix;
	@Value("${matridx.fileupload.tempPath}")
	private String tempFilePath;
	@Value("${matridx.fileupload.releasePath}")
	private String releaseFilePath;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	// 微信通知标本状态的模板ID
	@Value("${matridx.wechat.ybzt_templateid:}")
	private String ybzt_templateid = null;
	@Value("${matridx.wechat.biosequrl:}")
	private String biosequrl;
	@Autowired
	RedisUtil redisUtil;
	public static Map<String,Map<String,String>> reportsMap = new HashMap<String,Map<String,String>>();
	/**
	 * 文档转换完成OK
	 */
	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private String DOC_OK = null;
	/**
	 * FTP服务器地址 在SpringBoot中使用@Value只能给普通变量赋值，不能给静态变量赋值
	 */
	@Value("${matridx.ftp.url}")
	private String FTP_URL = null;
	
	private Logger log = LoggerFactory.getLogger(SjxxWsServiceImpl.class);

	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	ISjkzxxService sjkzxxService;

	/**
	 * 钉钉小程序获取送检列表
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getMiniSjxxList(SjxxDto sjxxDto){
		return dao.getMiniSjxxList(sjxxDto);
	}

	/**
	 * ddid获取到检测单位
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getJcdwByDdid(SjxxDto sjxxDto){
		return dao.getJcdwByDdid(sjxxDto);
	}
	
	/**
	 * 钉钉小程序获取未反馈列表
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getMiniFeedbackList(SjxxDto sjxxDto){
		return dao.getMiniFeedbackList(sjxxDto);
	}

	/**
	 * 微信小程序获取送检列表
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getWeChatSjxxList(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getWeChatSjxxList(sjxxDto);
	}

	/**
	 * 微信小程序获取未反馈列表
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getWeChatFeedbackList(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getWeChatFeedbackList(sjxxDto);
	}
	
	/**
	 * 根据外部编码下载报告
	 * @param request
	 * @param organ
	 * @param type
	 * @param code
	 * @param lastcode
	 * @param sign
	 * @param response
	 * @return
	 */
	@Override
	public Map<String, Object> downloadByCode(HttpServletRequest request, String organ, String type, String code, String lastcode, String sign, HttpServletResponse response){
		//针对迪安进行限制 2021-12-27
		if(StringUtil.isBlank(code))
			return new HashMap<>();
		
		Map<String,String> report = reportsMap.get(code);
		if(report != null) {
			String times = report.get("times");
			String lasttime = report.get("lasttime");
			if(StringUtil.isNotBlank(times)) {
				try {
					//如果连续3次
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					
					Calendar now_date = Calendar.getInstance();
					now_date.add(Calendar.MINUTE, -30);
					//将Calendar的对象转为Date对象
					Date dateC = now_date.getTime();
					
					if(StringUtil.isNotBlank(lasttime)) {
						Date d_lasttime = dateFormat.parse(lasttime);
						if(Integer.parseInt(times) > 3 && d_lasttime.after(dateC)) {
							Map<String, Object> map = new HashMap<>();
							map.put("status", "fail");
							map.put("errorCode", "未获取到相关报告信息!");
							return map;
						}else if(Integer.parseInt(times) > 3 && d_lasttime.before(dateC)) {
							report.put("times", "0");
							report.put("lasttime", "");
						}
					}
				}catch(Exception e) {
					log.error(e.getMessage());
				}
			}
		}else {
			report = new HashMap<>();
			reportsMap.put(code, report);
			report.put("times", "0");
			report.put("lasttime", "");
			
			//删除超过一天的数据
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			
			Date dateC = new Date();
			String nowDate =  dateFormat.format(dateC);
			Set<String> keys = reportsMap.keySet();
			if (CollectionUtils.isNotEmpty(keys)){
				Iterator<String> iterator = keys.iterator();
				while (iterator.hasNext()){
					String key = iterator.next();
					Map<String,String> t_report = reportsMap.get(key);
					if(t_report!=null && StringUtil.isNotBlank(t_report.get("lasttime"))) {
						if(!nowDate.equals(t_report.get("lasttime").substring(0,10))) {
							//reportsMap.remove(key);
							iterator.remove();
						}
					}
				}
			}
		}
		
		DBEncrypt crypt = new DBEncrypt();
		Map<String, Object> map=new HashMap<>();

		if(StringUtil.isBlank(request.getParameter("access_token"))){
			map = checkSecurity(organ, type, code, lastcode, sign, crypt);
			if(!"0".equals(map.get("errorCode")))
				return map;
		}

		//查询合作伙伴
		List<String> dbs = sjxxService.getSjhbByCode(organ);
		if(dbs == null || dbs.size() == 0){
			map.put("status", "fail");
			map.put("errorCode", "未查询到伙伴权限！");
			return map;
		}
		SjxxDto sjxxDto = new SjxxDto();
		sjxxDto.setDbs(dbs);
		if(StringUtil.isNotBlank(code)){
			//返回报告文件
			sjxxDto.setWbbm(code);
			List<SjxxDto> sjxxDtos = sjxxService.getListByWbbm(sjxxDto);
			if(sjxxDtos != null && sjxxDtos.size() > 0){
				//默认只取第一条数据
				String jclx = sjxxDtos.get(0).getJclx();
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(sjxxDtos.get(0).getSjid());
				List<String> ywlxs = new ArrayList<>();
				if(StringUtil.isBlank(type) || "pdf".equals(type)){
					ywlxs.add(sjxxDtos.get(0).getCskz3() + "_" + jclx);
					ywlxs.add(sjxxDtos.get(0).getCskz3() +"_REM"+ "_" + jclx);
//					ywlxs.add(sjxxDtos.get(0).getCskz3().replace("_ONCO", "") + "_" + jclx);
				}else{
					ywlxs.add(sjxxDtos.get(0).getCskz3() + "_" + jclx + "_WORD");
					ywlxs.add(sjxxDtos.get(0).getCskz3() +"_REM"+ "_" + jclx + "_WORD");
//					ywlxs.add(sjxxDtos.get(0).getCskz3().replace("_ONCO", "") + "_" + jclx + "_WORD");
				}
				fjcfbDto.setYwlxs(ywlxs);
//				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
				if(fjcfbDtos != null && fjcfbDtos.size() > 0){
					String wjlj = fjcfbDtos.get(0).getWjlj();
					String wjm = fjcfbDtos.get(0).getWjm();
					String filePath = crypt.dCode(wjlj);
					download(filePath, wjm, response);
					map.put("status", "success");
					return map;
				}else{
					map.put("status", "fail");
					map.put("errorCode", "未获取到相关报告信息!");
					log.error("未获取到相关报告信息!");
					int total_times = Integer.parseInt(report.get("times")) + 1;
					report.put("times", String.valueOf(total_times));
					if(total_times > 3) {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date dateC = new Date();
						String nowDate = dateFormat.format(dateC);
						report.put("lasttime", nowDate);
					}
					return map;
				}
			}else{
				map.put("status", "fail");
				map.put("errorCode", "未获取到相关送检信息!");
				log.error("未获取到相关送检信息!");
				
				int total_times = Integer.parseInt(report.get("times")) + 1;
				report.put("times", String.valueOf(total_times));
				if(total_times >= 3) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date dateC = new Date();
					String nowDate = dateFormat.format(dateC);
					report.put("lasttime", nowDate);
				}
				return map;
			}
		}else{
			//返回文件压缩包
			sjxxDto.setLastwbbm(lastcode);
			List<SjxxDto> sjxxDtos = sjxxService.getListByLastWbbm(sjxxDto);
			if(sjxxDtos!= null && sjxxDtos.size() > 0){
				for (int i = 0; i < sjxxDtos.size(); i++) {
					String jclx = sjxxDtos.get(i).getJclx();
					sjxxDtos.get(i).setDbs(dbs);
					if(StringUtil.isBlank(type) || "pdf".equals(type)){
						sjxxDtos.get(i).setYwlx(sjxxDtos.get(i).getCskz3() + "_" + jclx);
					}else{
						sjxxDtos.get(i).setYwlx(sjxxDtos.get(i).getCskz3() + "_" + jclx + "_WORD");
					}
				}
				List<SjxxDto> sjxxList = sjxxService.selectDownloadReportBySjxxDtos(sjxxDtos);
				String folderName = "UP" + String.valueOf(System.currentTimeMillis());
				String storePath = prefix + tempFilePath + BusTypeEnum.IMP_REPORTZIP.getCode() + "/" + folderName;
				mkDirs(storePath);
				for (int i = 0; i < sjxxList.size(); i++) {
					String wjlj = crypt.dCode(sjxxList.get(i).getWjlj());
					String wjm = sjxxList.get(i).getWjm();
					String newWjlj = storePath + "/" + wjm;
					File file = new File(wjlj);
					if (file.exists()) {
						copyFile(wjlj, newWjlj);
					}else{
						log.info("-----文件未找到:" + wjlj);
					}
				}
				//调用公共方法压缩文件
				String srcZip = storePath+".zip";
				ZipUtil.toZip(storePath, srcZip, true);
				String fileName = folderName+".zip";
				download(srcZip, fileName, response);
				map.put("status", "success");
				return map;
			}else{
				map.put("status", "fail");
				map.put("errorCode", "未获取到相关送检信息!");
				log.error("未获取到相关送检信息!");
				
				int total_times = Integer.parseInt(report.get("times")) + 1;
				report.put("times", String.valueOf(total_times));
				if(total_times > 3) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date dateC = new Date();
					String nowDate = dateFormat.format(dateC);
					report.put("lasttime", nowDate);
				}
				
				return map;
			}
		}
	}

	/**
	 * 根据外部编码下载报告Plus
	 * @param request
	 * @param organ
	 * @param type
	 * @param code
	 * @param lastcode
	 * @param sign
	 * @param response
	 * @return
	 */
	@Override
	public Map<String, Object> downloadByCodePlus(HttpServletRequest request, String organ, String type, String code, String lastcode, String sign, HttpServletResponse response){
		//针对迪安进行限制 2021-12-27
		Map<String,String> report = reportsMap.get(code);
		if(report != null) {
			String times = report.get("times");
			String lasttime = report.get("lasttime");
			if(StringUtil.isNotBlank(times)) {
				try {
					//如果连续3次
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

					Calendar now_date = Calendar.getInstance();
					now_date.add(Calendar.MINUTE, -30);
					//将Calendar的对象转为Date对象
					Date dateC = now_date.getTime();

					if(StringUtil.isNotBlank(lasttime)) {
						Date d_lasttime = dateFormat.parse(lasttime);
						if(Integer.parseInt(times) > 3 && d_lasttime.after(dateC)) {
							Map<String, Object> map = new HashMap<>();
							map.put("status", "fail");
							map.put("errorCode", "未获取到相关报告信息!");
							return map;
						}else if(Integer.parseInt(times) > 3 && d_lasttime.before(dateC)) {
							report.put("times", "0");
							report.put("lasttime", "");
						}
					}
				}catch(Exception e) {
					log.error(e.getMessage());
				}
			}
		}else {
			report = new HashMap<>();
			reportsMap.put(code, report);
			report.put("times", "0");
			report.put("lasttime", "");

			//删除超过一天的数据
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

			Date dateC = new Date();
			String nowDate =  dateFormat.format(dateC);
			Set<String> keys = reportsMap.keySet();
			if (CollectionUtils.isNotEmpty(keys)){
				for(String key:keys) {
					Map<String,String> t_report = reportsMap.get(key);
					if(t_report!=null && StringUtil.isNotBlank(t_report.get("lasttime"))) {
						if(!nowDate.equals(t_report.get("lasttime").substring(0,10))) {
							reportsMap.remove(key);
						}
					}
				}
			}
		}

		DBEncrypt crypt = new DBEncrypt();
		Map<String, Object> map=new HashMap<>();
		//校验toekn
		if(StringUtil.isBlank(request.getParameter("access_token"))){
			map = checkSecurity(organ, type, code, lastcode, sign, crypt);
			if(!"0".equals(map.get("errorCode")))
				return map;
		}
		//查询合作伙伴
		List<String> dbs = sjxxService.getSjhbByCode(organ);
		if(dbs == null || dbs.size() == 0){
			map.put("status", "fail");
			map.put("errorCode", "未查询到伙伴权限！");
			return map;
		}
		WbsjxxDto wbsjxxDto = new WbsjxxDto();
		wbsjxxDto.setDbs(dbs);
		if(StringUtil.isNotBlank(code)){
			wbsjxxDto.setSjbm(code);
			List<WbsjxxDto> wbsjxxDtos = wbsjxxService.getListByWbbm(wbsjxxDto);
			//返回报告文件
			if(wbsjxxDtos != null && wbsjxxDtos.size() > 0){
				//默认只取第一条数据
				String jcxmcskz1 = wbsjxxDtos.get(0).getJcxmcskz1();
				String jcxmcskz3 = wbsjxxDtos.get(0).getJcxmcskz3();
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(wbsjxxDtos.get(0).getSjid());
				List<String> ywlxs = new ArrayList<>();
				if(StringUtil.isBlank(type) || "pdf".equals(type)){
					ywlxs.add(jcxmcskz3 + "_" + jcxmcskz1);
					ywlxs.add(jcxmcskz3 +"_REM"+ "_" + jcxmcskz1 + "_WORD");
				}else{
					ywlxs.add(jcxmcskz3 + "_" + jcxmcskz1 + "_WORD");
					ywlxs.add(jcxmcskz3 +"_REM"+ "_" + jcxmcskz1 + "_WORD");
				}
				fjcfbDto.setYwlxs(ywlxs);
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlxOrderByYwlx(fjcfbDto);
				if(fjcfbDtos != null && fjcfbDtos.size() > 0){
					String wjlj = fjcfbDtos.get(0).getWjlj();
					String wjm = fjcfbDtos.get(0).getWjm();
					String filePath = crypt.dCode(wjlj);
					download(filePath, wjm, response);
					map.put("status", "success");
					return map;
				}else{
					map.put("status", "fail");
					map.put("errorCode", "未获取到相关报告信息!");
					log.error("未获取到相关报告信息!");
					int total_times = Integer.parseInt(report.get("times")) + 1;
					report.put("times", String.valueOf(total_times));
					if(total_times > 3) {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date dateC = new Date();
						String nowDate = dateFormat.format(dateC);
						report.put("lasttime", nowDate);
					}
					return map;
				}
			}else{
				SjxxDto sjxxDto = new SjxxDto();
				sjxxDto.setDbs(dbs);
				sjxxDto.setWbbm(code);
				return dealSjxxReturnMap(map,sjxxDto, report, crypt, type, response);
			}
		}else{
			//返回文件压缩包
			wbsjxxDto.setLastwbbm(lastcode);
			List<WbsjxxDto> wbsjxxDtos = wbsjxxService.getListByLastWbbm(wbsjxxDto);
			if(wbsjxxDtos!= null && wbsjxxDtos.size() > 0){
				List<SjxxDto> sjxxDtos = new ArrayList<>();
				for (int i = 0; i < wbsjxxDtos.size(); i++) {
					SjxxDto sjxxDto = new SjxxDto();
					String jcxmcskz1 = wbsjxxDtos.get(i).getJcxmcskz1();
					String jcxmcskz3 = wbsjxxDtos.get(i).getJcxmcskz3();
					sjxxDto.setDbs(dbs);
					sjxxDto.setSjid(wbsjxxDtos.get(i).getSjid());
					if(StringUtil.isBlank(type) || "pdf".equals(type)){
						sjxxDto.setYwlx(jcxmcskz3 + "_" + jcxmcskz1);
					}else{
						sjxxDto.setYwlx(jcxmcskz3 + "_" + jcxmcskz1 + "_WORD");
					}
					sjxxDtos.add(sjxxDto);
				}
				return dealSjxxDtosReturnMap(map,sjxxDtos,crypt,response);
			}else{
				SjxxDto sjxxDto = new SjxxDto();
				sjxxDto.setDbs(dbs);
				sjxxDto.setLastwbbm(lastcode);
				List<SjxxDto> sjxxDtos = sjxxService.getListByLastWbbm(sjxxDto);
				if(sjxxDtos!= null && sjxxDtos.size() > 0){
					for (int i = 0; i < sjxxDtos.size(); i++) {
						String jclx = sjxxDtos.get(i).getJclx();
						sjxxDtos.get(i).setDbs(dbs);
						if(StringUtil.isBlank(type) || "pdf".equals(type)){
							sjxxDtos.get(i).setYwlx(sjxxDtos.get(i).getCskz3() + "_" + jclx);
						}else{
							sjxxDtos.get(i).setYwlx(sjxxDtos.get(i).getCskz3() + "_" + jclx + "_WORD");
						}
					}
					return dealSjxxDtosReturnMap(map,sjxxDtos,crypt,response);
				}else{
					map.put("status", "fail");
					map.put("errorCode", "未获取到相关送检信息!");
					log.error("未获取到相关送检信息!");

					int total_times = Integer.parseInt(report.get("times")) + 1;
					report.put("times", String.valueOf(total_times));
					if(total_times > 3) {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date dateC = new Date();
						String nowDate = dateFormat.format(dateC);
						report.put("lasttime", nowDate);
					}

					return map;
				}
			}
		}
	}

	/**
	 * 处理送检信息返回map
	 * @param map
	 * @param sjxxDto
	 * @param report
	 * @param crypt
	 * @param type
	 * @param response
	 * @return
	 */
	public Map<String, Object> dealSjxxReturnMap(Map<String, Object>map,SjxxDto sjxxDto,Map<String,String> report,DBEncrypt crypt,String type,HttpServletResponse response){
		List<SjxxDto> sjxxDtos = sjxxService.getListByWbbm(sjxxDto);
		if(sjxxDtos != null && sjxxDtos.size() > 0){
			//默认只取第一条数据
			String jclx = sjxxDtos.get(0).getJclx();
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(sjxxDtos.get(0).getSjid());
			List<String> ywlxs = new ArrayList<>();
			if(StringUtil.isBlank(type) || "pdf".equals(type)){
				ywlxs.add(sjxxDtos.get(0).getCskz3() + "_" + jclx);
				ywlxs.add(sjxxDtos.get(0).getCskz3() +"_REM"+ "_" + jclx + "_WORD");
			}else{
				ywlxs.add(sjxxDtos.get(0).getCskz3() + "_" + jclx + "_WORD");
				ywlxs.add(sjxxDtos.get(0).getCskz3() +"_REM"+ "_" + jclx + "_WORD");
			}
			fjcfbDto.setYwlxs(ywlxs);
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlxOrderByYwlx(fjcfbDto);
			if(fjcfbDtos != null && fjcfbDtos.size() > 0){
				String wjlj = fjcfbDtos.get(0).getWjlj();
				String wjm = fjcfbDtos.get(0).getWjm();
				String filePath = crypt.dCode(wjlj);
				download(filePath, wjm, response);
				map.put("status", "success");
				return map;
			}else{
				map.put("status", "fail");
				map.put("errorCode", "未获取到相关报告信息!");
				log.error("未获取到相关报告信息!");
				int total_times = Integer.parseInt(report.get("times")) + 1;
				report.put("times", String.valueOf(total_times));
				if(total_times > 3) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date dateC = new Date();
					String nowDate = dateFormat.format(dateC);
					report.put("lasttime", nowDate);
				}
				return map;
			}
		}else{
			map.put("status", "fail");
			map.put("errorCode", "未获取到相关送检信息!");
			log.error("未获取到相关送检信息!");
			int total_times = Integer.parseInt(report.get("times")) + 1;
			report.put("times", String.valueOf(total_times));
			if(total_times >= 3) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dateC = new Date();
				String nowDate = dateFormat.format(dateC);
				report.put("lasttime", nowDate);
			}
			return map;
		}
	}


	/**
	 * 处理送检信息List返回map
	 * @param map
	 * @param sjxxDtos
	 * @param crypt
	 * @param response
	 * @return
	 */
	public Map<String, Object> dealSjxxDtosReturnMap(Map<String, Object>map,List<SjxxDto> sjxxDtos,DBEncrypt crypt,HttpServletResponse response){
		List<SjxxDto> sjxxList = sjxxService.selectDownloadReportBySjxxDtos(sjxxDtos);
		String folderName = "UP" + String.valueOf(System.currentTimeMillis());
		String storePath = prefix + tempFilePath + BusTypeEnum.IMP_REPORTZIP.getCode() + "/" + folderName;
		mkDirs(storePath);
		for (int i = 0; i < sjxxList.size(); i++) {
			String wjlj = crypt.dCode(sjxxList.get(i).getWjlj());
			String wjm = sjxxList.get(i).getWjm();
			String newWjlj = storePath + "/" + wjm;
			File file = new File(wjlj);
			if (file.exists()) {
				copyFile(wjlj, newWjlj);
			}else{
				log.info("-----文件未找到:" + wjlj);
			}
		}
		//调用公共方法压缩文件
		String srcZip = storePath+".zip";
		ZipUtil.toZip(storePath, srcZip, true);
		String fileName = folderName+".zip";
		download(srcZip, fileName, response);
		map.put("status", "success");
		return map;
	}
	/**
	 * 校验外部参数信息
	 * @param organ
	 * @param type
	 * @param code
	 * @param lastcode
	 * @param sign
	 * @param crypt
	 * @return
	 */
	@Override
	public Map<String, Object> checkSecurity(String organ, String type, String code, String lastcode, String sign, DBEncrypt crypt){
		Map<String, Object> map = checkSecurity(organ, type, code, lastcode, sign, true, crypt);
		map.remove("iswbbmmust");
		return map;
	}


	/**
	 * 校验外部参数信息
	 * @param organ
	 * @param type
	 * @param code
	 * @param lastcode
	 * @param sign
	 * @param flag 是否code、lastcode必填
	 * @param crypt
	 * @return
	 */
	@Override
	public Map<String, Object> checkSecurity(String organ, String type, String code, String lastcode, String sign, boolean flag, DBEncrypt crypt) {
		Map<String, Object> map = new HashMap<>();
		log.error("organ:" + organ + " type:" + type + " code:" + code + " lastcode:" + lastcode + " sign:" + sign + " flag:" + flag);
		//organ判断安全性
		if(StringUtil.isBlank(organ)){
			map.put("status", "fail");
			map.put("errorCode", "未获取到代码信息organ！");
			log.error("未获取到代码信息organ！");
			return map;
		}
		Object o_wbaqyz = redisUtil.get("Wbaqyz:" + organ);
		WbaqyzDto wbaqyzDto = null;
		if(o_wbaqyz!=null){
			wbaqyzDto = JSONObject.parseObject((String)o_wbaqyz,WbaqyzDto.class);
		}
		//WbaqyzDto wbaqyzDto = wbaqyzService.getDtoById(organ);
		if(wbaqyzDto == null){
			map.put("status", "fail");
			map.put("errorCode", "代码信息organ不正确，未获取到秘钥！" + organ);
			log.error("代码信息organ不正确，未获取到秘钥！");
			return map;
		}
		String iswbbmmust = wbaqyzDto.getIswbbmmust();//0表示外部编码非必须，1或者null表示外部编码必须
		if (StringUtil.isNotBlank(iswbbmmust) && "0".equals(iswbbmmust)) {
			map.put("iswbbmmust","0");
		}
		if(StringUtil.isBlank(code) && StringUtil.isBlank(lastcode) && flag && (!"0".equals(iswbbmmust)||iswbbmmust==null)){
			map.put("status", "fail");
			map.put("errorCode", "未获取到外部编码!");
			log.error("未获取到外部编码!");
			return map;
		}
		if(StringUtil.isBlank(sign)){
			map.put("status", "fail");
			map.put("errorCode", "未获取到sign签名!");
			log.error("未获取到sign签名!");
			return map;
		}
		String word = wbaqyzDto.getWord();
		word = crypt.dCode(word);
		String text = organ + (code==null?"":code) + (lastcode==null?"":lastcode) + (type==null?"":type) + word;

		String t_sign = Encrypt.encrypt(text, "SHA1");
		if(!sign.equalsIgnoreCase(t_sign)){
			map.put("status", "fail");
			map.put("errorCode", "sign信息不正确！");
			log.error("sign信息不正确！t_sign：" + t_sign + " text:"+text);
			return map;
		}
		map.put("errorCode", "0");
		return map;
	}


	public Map<String, Object> checkSecurity(String organ, String type, String code, String lastcode, boolean flag, DBEncrypt crypt) {
		Map<String, Object> map = new HashMap<>();
		log.error("organ:" + organ + " type:" + type + " code:" + code + " lastcode:" + lastcode  + " flag:" + flag);
		//organ判断安全性
		if(StringUtil.isBlank(organ)){
			map.put("status", "fail");
			map.put("errorCode", "未获取到代码信息organ！");
			log.error("未获取到代码信息organ！");
			return map;
		}
		//WbaqyzDto wbaqyzDto = wbaqyzService.getDtoById(organ);
		Object o_wbaqyz = redisUtil.get("Wbaqyz:" + organ);
		WbaqyzDto wbaqyzDto = null;
		if(o_wbaqyz!=null){
			wbaqyzDto = JSONObject.parseObject((String)o_wbaqyz,WbaqyzDto.class);
		}
		if(wbaqyzDto == null){
			map.put("status", "fail");
			map.put("errorCode", "代码信息organ不正确，未获取到秘钥！" + organ);
			log.error("代码信息organ不正确，未获取到秘钥！");
			return map;
		}
		String iswbbmmust = wbaqyzDto.getIswbbmmust();//0表示外部编码非必须，1或者null表示外部编码必须
		if (StringUtil.isNotBlank(iswbbmmust) && "0".equals(iswbbmmust)) {
			map.put("iswbbmmust","0");
		}
		if(StringUtil.isBlank(code) && StringUtil.isBlank(lastcode) && flag && !"0".equals(iswbbmmust)){
			map.put("status", "fail");
			map.put("errorCode", "未获取到外部编码!");
			log.error("未获取到外部编码!");
			return map;
		}

		//String word = wbaqyzDto.getWord();
		//word = crypt.dCode(word);
		//String text = organ + (code==null?"":code) + (lastcode==null?"":lastcode) + (type==null?"":type) + word;

		//String t_sign = Encrypt.encrypt(text, "SHA1");

		map.put("errorCode", "0");
		return map;
	}

	/**
	 * 校验IP是否在白名单中
	 * @param request
	 * @param db
	 * @return
	 */
	public boolean checkIp(HttpServletRequest request,String db){
		//校验IP是否在白名单中
		if(StringUtil.isNotBlank(db)){
			JcsjDto jcsjDto=new JcsjDto();
			List<JcsjDto> jcsjlist = redisUtil.hmgetDto("matridx_jcsj:"+BasicDataTypeEnum.WHITELIST_INSP_CONFIRM.getCode());
			if(!CollectionUtils.isEmpty(jcsjlist)){
				for(JcsjDto t_jcsjDto:jcsjlist){
					if(db.equals(t_jcsjDto.getCsmc())){
						jcsjDto=t_jcsjDto;
						break;
					}
				}
			}
			if(jcsjDto!=null && StringUtil.isNotBlank(jcsjDto.getCskz1())){
				String ips=jcsjDto.getCskz1();
				if(ips.contains(request.getRemoteAddr())){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 校验外部参数信息
	 * @param organ
	 * @param str
	 * @param sign
	 * @param crypt
	 * @return
	 */
	@Override
	public Map<String, Object> checkSecurityReceive(String organ, String str, String sign, DBEncrypt crypt,boolean isSignMustCompare){
		Map<String, Object> map = new HashMap<>();
		//organ判断安全性
		if(StringUtil.isBlank(organ)){
			map.put("status", "fail");
			map.put("errorCode", "103");
			map.put("errorInfo", "未获取到organ信息！");
			log.error("未获取到organ信息！");
			return map;
		}
		if(StringUtil.isBlank(str)){
			map.put("status", "fail");
			map.put("errorCode", "101");
			map.put("errorInfo", "未获取到json信息！");
			log.error("未获取到json信息！");
			return map;
		}
		if(StringUtil.isBlank(sign)){
			map.put("status", "fail");
			map.put("errorCode", "102");
			map.put("errorInfo", "未获取到sign信息！");
			log.error("未获取到sign信息！");
			return map;
		}
		//WbaqyzDto wbaqyzDto = wbaqyzService.getDtoById(organ);
		Object o_wbaqyz = redisUtil.get("Wbaqyz:" + organ);
		WbaqyzDto wbaqyzDto = null;
		if(o_wbaqyz!=null){
			wbaqyzDto = JSONObject.parseObject((String)o_wbaqyz,WbaqyzDto.class);
		}
		if(wbaqyzDto == null){
			map.put("status", "fail");
			map.put("errorCode", "104");
			map.put("errorInfo", "代码信息organ不正确，未获取到秘钥！" + organ);
			log.error("checkSecurityReceive errorCode:104 errorInfo:organ不正确，未获取到秘钥！" + organ);
			return map;
		}
		try {
			Encrypt crypts = new Encrypt();

			String word = wbaqyzDto.getWord();
			word = crypt.dCode(word);
			String text = str + organ + word;
			@SuppressWarnings("static-access")
			String t_sign = crypts.encrypt(text, "SHA1");
			//sign忽略大小写校验
			if(!sign.equalsIgnoreCase(t_sign)){

				log.error("checkSecurityReceive errorCode:102 errorInfo:sign信息不正确! 正确值应该为:" + t_sign);
				if (isSignMustCompare){
					map.put("status", "fail");
					map.put("errorCode", "102");
					map.put("errorInfo", "sign信息不正确！");
					return map;
				}
			}
		} catch (Exception e) {
			log.error("checkSecurityReceive 加解密失败！");
		}
		map.put("errorCode", "0");
		return map;
	}
	
	
	/**
	 * 文件下载(公用)
	 * @param filePath
	 * @param wjm
	 * @param response
	 */
    @Override
	public void download(String filePath, String wjm, HttpServletResponse response){
		log.error("准备给客户端下载文件 download filePath:" + filePath + " wjm:" + wjm);
		File downloadFile = new File(filePath); 
		try {
			if(wjm != null){
				wjm = URLEncoder.encode(wjm, "utf-8");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			log.error(e.toString());
		}
		response.setHeader("content-type", "application/octet-stream");
		response.setContentLength((int) downloadFile.length());
        //指明为下载
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + wjm);
        
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        InputStream iStream;
        OutputStream os = null; //输出流
        try {
        	iStream = new FileInputStream(filePath);
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
            log.error(e.toString());
        }
        try {
        	if(bis!=null)
        		bis.close();
        	if(os!=null) {
	            os.flush();
	            os.close();
        	}
        } catch (IOException e) {
            // TODO Auto-generated catch block
            log.error(e.toString());
        }
		log.error("文件下载完成");
	}
	
	/**
	 * 根据路径创建文件
	 * 
	 * @param storePath
	 * @return
	 */
	private boolean mkDirs(String storePath){
		File file = new File(storePath);
		if (file.isDirectory()){
			return true;
		}
		return file.mkdirs();
	}
	
	/**
	 * 复制文件
	 * @param src
	 * @param dest
	 * @return
	 */
    public boolean copyFile(String src, String dest){
    	boolean flag = false;
    	FileInputStream in = null;
    	FileOutputStream out = null;
		try {
			in = new FileInputStream(src);
			File file = new File(dest);
	        if(!file.exists())
	            file.createNewFile();
	        out = new FileOutputStream(file);
	        int c;
	        byte[] buffer = new byte[1024];
	        while((c = in.read(buffer)) != -1){
	            for(int i = 0; i < c; i++)
	                out.write(buffer[i]);        
	        }
	        flag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        try {
	        	in.close();
		        out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		return flag;
    }

    /**
	 * 调用金域接口获取数据
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public Map<String, Object> getGoldenData(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<>();
		// 动态调用webservice接口
		log.error("getGoldenData 准备动态调用金域接口！");
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
		Client client = dcf.createClient("https://extesb.kingmed.com.cn:4433/services/LB.SPECIMEN.SEND2");
		try {
			QName name = new QName("http://cxf.esb.lb.kingmed.com/", "Login");
			Object[] objects = client.invoke(name, "hzjysw", "hzjysw@2020.","001", true);
			if(objects != null && "0".equals(objects[0])){
				name = new QName("http://cxf.esb.lb.kingmed.com/", "GetRequestInfo4");
				Object[] t_objects = client.invoke(name, objects[1],sjxxDto.getYbbh());
				if(t_objects != null && "0".equals(t_objects[0])){
					//log.error("返回参数处理："+t_objects[1].toString());
					Map<String, Object> xmlMap = BasicXmlReader.readXmlToMap(t_objects[1].toString());
					if(xmlMap != null){
						log.error("readXmlToMap转换完成!");
						String requestCode = (String) xmlMap.get("RequestCode");// 金域条码 - 标本编号，外部编码
						String t_name = (String) xmlMap.get("Name");// 病人姓名
						String sex = (String) xmlMap.get("Sex");// 性别
						if("男".equals(sex)){
							sex = "1";
						}else if("女".equals(sex)){
							sex = "2";
						}else{
							sex = "3";
						}
						String age = (String) xmlMap.get("Age");// 年龄
						String ageUnit = (String) xmlMap.get("AgeUnit");// 年龄单位
						if(!"岁".equals(ageUnit) && !"个月".equals(ageUnit) && !"天".equals(ageUnit)){
							ageUnit = "无";
						}
						String patientTel = (String) xmlMap.get("PatientTel");// 病人电话
						String doctName = (String) xmlMap.get("DoctName");// 医生姓名
						if(StringUtil.isBlank(doctName)){
							doctName = "无";
						}
						String sectionOffice = (String) xmlMap.get("SectionOffice");// 医院及科室，-号分隔
						String bedNumber = (String) xmlMap.get("BedNumber");// 床号
						// String naturalItemName = (String) xmlMap.get("NaturalItemName");// 检测项目
						String naturalItem = (String) xmlMap.get("NaturalItem");// 检测项目代码 RNA：60648 DNA:60649
						if(naturalItem.contains("60648") && naturalItem.contains("60649")){
							naturalItem = "C";
						}else if(naturalItem.contains("60647")){
							naturalItem = "C";
						}else if(naturalItem.contains("60648")){
							naturalItem = "R";
						}else if(naturalItem.contains("60649")){
							naturalItem = "D";
						}else{
							map.put("status","fail");
							map.put("message","naturalItem信息获取有误："+sectionOffice);
							log.error("naturalItem信息获取有误："+sectionOffice);
							return map;
						}
						String specimenType = (String) xmlMap.get("SpecimenType");// 标本类型
						String address = (String) xmlMap.get("ADDRESS");// 标本体积
						String samplingDate = (String) xmlMap.get("SamplingDate");// 采样日期
						String diagnose = (String) xmlMap.get("Diagnose");// 临床症状和前期诊断
						//查询基础数据
						Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclb(
								new BasicDataTypeEnum[]{BasicDataTypeEnum.WECGATSAMPLE_TYPE,BasicDataTypeEnum.DETECT_TYPE, 
										BasicDataTypeEnum.SD_TYPE,BasicDataTypeEnum.DETECTION_UNIT});
						//科室处理
						String ks = null;
						String qtks= null;
						String sjdw = null;
						if(StringUtil.isNotBlank(sectionOffice)){
							String[] sectionOffices = sectionOffice.split("-", 2);
							sjdw = sectionOffices[0];
							List<SjdwxxDto> sjdwxxList = sjxxService.getSjdw();
							for (int l = 0; l < sjdwxxList.size(); l++) {
								if(sectionOffices.length < 2){
									if("1".equals(sjdwxxList.get(l).getKzcs())){
										ks = sjdwxxList.get(l).getDwid();
										qtks = "无";
										break;
									}
								}else{
									if(sjdwxxList.get(l).getDwmc().equals(sectionOffices[1])){
										ks = sjdwxxList.get(l).getDwid();
										qtks = null;
										break;
									}
									if("1".equals(sjdwxxList.get(l).getKzcs())){
										ks = sjdwxxList.get(l).getDwid();
										qtks = sectionOffices[1];
									}
								}
								
							}
						}
						//标本类型处理
						String yblx = null;
				    	String yblxmc = null;
						if(StringUtil.isNotBlank(specimenType)){
							if("全血".equals(specimenType)){
								specimenType = "外周血";
							}else if("尿".equals(specimenType)){
								specimenType = "尿液";
							}else if("痰".equals(specimenType)){
								specimenType = "痰液";
							}
							List<JcsjDto> samplelist = jclist.get(BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
							for (int l = 0; l < samplelist.size(); l++) {
								if(samplelist.get(l).getCsmc().equals(specimenType)){
									yblx = samplelist.get(l).getCsid();
									yblxmc = null;
									break;
								}
								if("1".equals(samplelist.get(l).getCskz1())){
									yblx = samplelist.get(l).getCsid();
									yblxmc = specimenType;
								}
							}
						}
						//检测项目处理
						List<JcsjDto> datectlist = jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode());
						String jcxmid = null;
						for (int l = 0; l < datectlist.size(); l++) {
							if(datectlist.get(l).getCskz1().equals(naturalItem)){
								jcxmid = datectlist.get(l).getCsid();
								break;
							}
						}
						//检测单位固定 - 杭州
						List<JcsjDto> decetionlist = jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode());
						String jcdw = null;
						for (int l = 0; l < decetionlist.size(); l++) {
							if("1".equals(decetionlist.get(l).getCsdm())){
								jcdw = decetionlist.get(l).getCsid();
								break;
							}
						}
						// 快递类型固定 - 无
						List<JcsjDto> expressage = jclist.get(BasicDataTypeEnum.SD_TYPE.getCode());
						String kdlx = null;
						for (int l = 0; l < expressage.size(); l++) {
							if("1".equals(expressage.get(l).getCskz2())){
								kdlx = expressage.get(l).getCsid();
								break;
							}
						}
						log.error("参数组合完成！");
						SjxxDto t_sjxxDto = new SjxxDto();
						t_sjxxDto.setYbbh(requestCode);
						t_sjxxDto.setWbbm(requestCode);
						t_sjxxDto.setHzxm(t_name);
						t_sjxxDto.setXb(sex);
						t_sjxxDto.setNl(age);
						t_sjxxDto.setNldw(ageUnit);
						t_sjxxDto.setDh(patientTel);
						t_sjxxDto.setSjys(doctName);
						t_sjxxDto.setSjdw(sjdw);
						t_sjxxDto.setKs(ks);
						t_sjxxDto.setQtks(qtks);
						t_sjxxDto.setJcdw(jcdw);
						t_sjxxDto.setDb("金域");
						t_sjxxDto.setCwh(bedNumber);
						t_sjxxDto.setKdlx(kdlx);
						t_sjxxDto.setKdh("无");
						t_sjxxDto.setJcxmid(jcxmid);
						t_sjxxDto.setYblx(yblx);
						t_sjxxDto.setYblxmc(yblxmc);
						t_sjxxDto.setYbtj(address);
						t_sjxxDto.setCyrq(samplingDate);
						t_sjxxDto.setQqzd(diagnose);
						t_sjxxDto.setLczz(diagnose);
						map.put("sjxxDto", t_sjxxDto);
					}
					map.put("status","success");
				}else{
					map.put("status","fail");
					map.put("message",t_objects!= null?t_objects.toString():"获取送检信息失败！");
					log.error(t_objects!= null?t_objects.toString():"获取送检信息失败！");
				}
			}else{
				map.put("status","fail");
				map.put("message",objects!= null?objects.toString():"验证用户名失败！");
				log.error(objects!= null?objects.toString():"验证用户名失败！");
			}
		} catch (java.lang.Exception e) {
			map.put("status","fail");
			map.put("message",e.toString());
			log.error(e.toString());
		}
		return map;
	}
	
	/**
	 * 接收送检结果信息生成报告
	 * author 姚嘉伟  add 2020/8/31
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String,Object> receiveInspectionGenerateReport(HttpServletRequest request,String sample_result,String userid,JkdymxDto jkdymxDto) throws BusinessException{
		// TODO Auto-generated method stub
		Map<String,Object> map=new HashMap<>();
		map.put("status","fail");
		try {
			if (sample_result!=null)
			{
				WeChatInspectionReportModel inspinfo = JSONObject.parseObject(sample_result, WeChatInspectionReportModel.class);
				User user=new User();
				if(StringUtils.isNotBlank(userid)) {
                    user=commonService.getDtoByYhm(userid);
				}
				
				map = dealInspectionReportInfoTwo(request,inspinfo,user,jkdymxDto);
				//boolean result = true;
				log.error("receiveInspectionGenerateReport -- result0000");
				if ("success".equals(map.get("status")) && !"1".equals(map.get("ljbj"))){//ljbj说明，TBT+泛感染项目发送直接过滤
					boolean result=generateReportTwo(inspinfo,map);
					if(result){
						map.put("status","success");

						// 在报告生成的最后，判断是否为先声的标本，如果是则调用先声接口把实验信息和结果信息传递给先声接口
						// 存在主线程还未结束，但分线程已经在处理的情况，造成数据不正确
						SjxxDto t_sjxxDto = (SjxxDto) map.get("sjxxDto");
						if("hospitalXiansheng".equals(t_sjxxDto.getLrry())&&!t_sjxxDto.getJc_cskz3().contains("PCR")){
							//回传先声报告
							xianShengExcel(t_sjxxDto,inspinfo);
						}
					}else{
						map.put("status","fail");
					}
					return map;
				}else if ("fail".equals(map.get("status")) && "1".equals(map.get("errorCode"))){
					return map;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error("receiveInspectionGenerateReport -- result catch:"+e.toString());
			throw new BusinessException("","receiveInspectionGenerateReport -- result catch:"+e.toString());
		}
		return map;
	}

	public void sendMessageInfo(SjxxDto sjxxDto,String yxbt,String yxnr,String ddbt,
			String ddnr,String wxbt,String remark,String keyword1,String keyword2,String templateid,String keyword3) throws BusinessException {
		Map<String,Object> map= new HashMap<>();
		map.put("yxbt", yxbt);
		map.put("yxnr", yxnr);
		map.put("ddbt", ddbt);
		map.put("ddnr", ddnr);
		map.put("wxbt", wxbt);
		map.put("remark", remark);
		map.put("keyword2", keyword1);
		map.put("keyword1", keyword2);
		map.put("keyword3", keyword3);
		map.put("templateid", ybzt_templateid);
		map.put("xxmb","TEMPLATE_EXCEPTION");
		String reporturl = menuurl +"/wechat/statistics/wxRemark?remark="+remark;

		map.put("reporturl",reporturl);
		sjxxCommonService.sendMessage(sjxxDto, map);
	}
	
	/**
	 *  * 疑似或人体共生微生物群结果解释
	 * @param sjwzxxList  背景微生物
	 * @param column 下标用
	 * @param references  新版参考文献
	 * @param papers  参考文献
	 * @return
	 */
	private Map<String,Object> getBackground_comment(List<SjwzxxDto> sjwzxxList,int column,String references,List<WechatReferencesModel> papers) {
		//疑似背景解释
		Map<String,Object> map= new HashMap<>();
		StringBuffer sb=new StringBuffer();
		StringBuffer sb2=new StringBuffer();
		List<SjwzxxDto> background_comment_List = new ArrayList<>();
		if(sjwzxxList!=null && sjwzxxList.size()>0) {
			for (int i = 1; i < sjwzxxList.size()+1; i++){
				if(i>1&&i!=sjwzxxList.size()+1)
					sb.append("{br}{\\n}");
				else
					sb.append("\n");
				sb.append("{br}{b}"+i+"."+sjwzxxList.get(i-1).getWzzwm()+":{br}"+sjwzxxList.get(i-1).getWzzs()+"["+(i+column)+"]。");
				sjwzxxList.get(i-1).setWzzs(sjwzxxList.get(i-1).getWzzs());
				sjwzxxList.get(i-1).setYyxh(String.valueOf(i+column));
				background_comment_List.add(sjwzxxList.get(i-1));
			}
			for (int i = 1; i < sjwzxxList.size()+1; i++){
				if(i>1&&i!=sjwzxxList.size()+1)
					sb2.append("{br}{\\n}");
				else
					sb2.append("\n");
				//sb2.append("{br}{b}"+i+"."+sjwzxxList.get(i-1).getWzzwm()+":{br}"+sjwzxxList.get(i-1).getWzzs()+"。");
				sb2.append("{br}{b}"+i+"."+sjwzxxList.get(i-1).getWzzwm()+":{br}"+sjwzxxList.get(i-1).getWzzs()+"["+sjwzxxList.get(i-1).getYyxh()+"]。");
			}
		}
		String background_comment=sb.toString();
		String background_comment_hospital=sb2.toString();
		map.put("sjwzxxList_background", sjwzxxList);
		map.put("background_comment", background_comment);
		map.put("background_comment_List", background_comment_List);
		map.put("background_comment_hospital", background_comment_hospital);
		return map;
	}	/**
	 *  * 疑似或人体共生微生物群结果解释
	 * @param sjwzxxList  背景微生物
	 * @param column 下标用
	 * @param references  新版参考文献
	 * @param papers  参考文献
	 * @return
	 */
	private Map<String,Object> getCommensal_comment(List<SjwzxxDto> sjwzxxList,int column,String references,List<WechatReferencesModel> papers) {
		//疑似背景解释
		Map<String,Object> map= new HashMap<>();
		StringBuffer sb=new StringBuffer();
		StringBuffer sb2=new StringBuffer();
		List<SjwzxxDto> commensal_comment_List = new ArrayList<>();
		if(sjwzxxList!=null && sjwzxxList.size()>0) {
			for (int i = 1; i < sjwzxxList.size()+1; i++){
				if(i>1&&i!=sjwzxxList.size()+1)
					sb.append("{br}{\\n}");
				else
					sb.append("\n");
				sb.append("{br}{b}"+i+"."+sjwzxxList.get(i-1).getWzzwm()+":{br}"+sjwzxxList.get(i-1).getWzzs()+"["+(i+column)+"]。");
				sjwzxxList.get(i-1).setWzzs(sjwzxxList.get(i-1).getWzzs());
				sjwzxxList.get(i-1).setYyxh(String.valueOf(i+column));
				commensal_comment_List.add(sjwzxxList.get(i-1));
			}
			for (int i = 1; i < sjwzxxList.size()+1; i++){
				if(i>1&&i!=sjwzxxList.size()+1)
					sb2.append("{br}{\\n}");
				else
					sb2.append("\n");
				//sb2.append("{br}{b}"+i+"."+sjwzxxList.get(i-1).getWzzwm()+":{br}"+sjwzxxList.get(i-1).getWzzs()+"。");
				sb2.append("{br}{b}"+i+"."+sjwzxxList.get(i-1).getWzzwm()+":{br}"+sjwzxxList.get(i-1).getWzzs()+"["+sjwzxxList.get(i-1).getYyxh()+"]。");
			}
		}
		String commensal_comment=sb.toString();
		String commensal_comment_hospital=sb2.toString();
		map.put("sjwzxxList_commensal", sjwzxxList);
		map.put("commensal_comment", commensal_comment);
		map.put("commensal_comment_List", commensal_comment_List);
		map.put("commensal_comment_hospital", commensal_comment_hospital);
		return map;
	}

	/**
	 *  * 疑似或人体共生微生物群结果解释
	 * @param sjwzxxList  背景微生物
	 * @param column 下标用
	 * @param references  新版参考文献
	 * @param papers  参考文献
	 * @return
	 */
	private Map<String,Object> getBackgroundInfo_comment(List<SjwzxxDto> sjwzxxList,int column,String references,List<WechatReferencesModel> papers) {
		//疑似背景解释
		Map<String,Object> map= new HashMap<>();
		StringBuffer sb=new StringBuffer();
		StringBuffer sb2=new StringBuffer();
		List<SjwzxxDto> backgroundInfo_comment_List = new ArrayList<>();
		if(sjwzxxList!=null && sjwzxxList.size()>0) {
			for (int i = 1; i < sjwzxxList.size()+1; i++){
				if(i>1&&i!=sjwzxxList.size()+1)
					sb.append("{br}{\\n}");
				else
					sb.append("\n");
				sb.append("{br}{b}"+i+"."+sjwzxxList.get(i-1).getWzzwm()+":{br}"+sjwzxxList.get(i-1).getWzzs()+"["+(i+column)+"]。");
				sjwzxxList.get(i-1).setWzzs(sjwzxxList.get(i-1).getWzzs());
				sjwzxxList.get(i-1).setYyxh(String.valueOf(i+column));
				backgroundInfo_comment_List.add(sjwzxxList.get(i-1));
			}
			for (int i = 1; i < sjwzxxList.size()+1; i++){
				if(i>1&&i!=sjwzxxList.size()+1)
					sb2.append("{br}{\\n}");
				else
					sb2.append("\n");
				//sb2.append("{br}{b}"+i+"."+sjwzxxList.get(i-1).getWzzwm()+":{br}"+sjwzxxList.get(i-1).getWzzs()+"。");
				sb2.append("{br}{b}"+i+"."+sjwzxxList.get(i-1).getWzzwm()+":{br}"+sjwzxxList.get(i-1).getWzzs()+"["+sjwzxxList.get(i-1).getYyxh()+"]。");
			}
		}
		String backgroundInfo_comment=sb.toString();
		String backgroundInfo_comment_hospital=sb2.toString();
		map.put("sjwzxxList_backgroundInfo", sjwzxxList);
		map.put("backgroundInfo_comment", backgroundInfo_comment);
		map.put("backgroundInfo_comment_List", backgroundInfo_comment_List);
		map.put("backgroundInfo_comment_hospital", backgroundInfo_comment_hospital);
		return map;
	}
	/**
	 * 关闭流
	 * 
	 * @param streams
	 */
	private static void closeStream(Closeable[] streams)
	{
		if (streams == null || streams.length < 1)
			return;
		for (int i = 0; i < streams.length; i++)
		{
			try
			{
				Closeable stream = streams[i];
				if (null != stream)
				{
					stream.close();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean upload(MultipartFile file, String dir,String projectType) {
		String wjm = file.getOriginalFilename();
		String fwjlj = prefix + releaseFilePath + dir;
		String wjlj = fwjlj + "/" + wjm;
		mkDirs(fwjlj);
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
			output.close();
			//根据临时文件夹创建正式文件
			String r_path = "IMP_TEMEPLATE_IMAGES"+projectType+"/R";
			String c_path = "IMP_TEMEPLATE_IMAGES"+projectType+"/C";
			//分文件路径
			String real_path = prefix + releaseFilePath + r_path;
			String realc_path = prefix + releaseFilePath + c_path;
			mkDirs(real_path);
			mkDirs(realc_path);
			//把文件从临时文件夹中移动到正式文件夹中，如果已经存在则覆盖
			boolean r = CutFile(wjlj,real_path+"/"+wjm);
			boolean c = CutFile(wjlj,realc_path+"/"+wjm);
            return r && c;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			closeStream(new Closeable[] { fis, input, output, fos });
		}
	}
	/**
	 * 从原路径剪切到目标路径
	 * @param s_srcFile
	 * @param s_distFile
	 * @return
	 */
	private boolean CutFile(String s_srcFile, String s_distFile) throws IOException {
    	boolean flag = false;
    	FileInputStream in = null;
    	FileOutputStream out = null;
		try {
			in = new FileInputStream(s_srcFile);
			File file = new File(s_distFile);
	        if(!file.exists())
	            file.createNewFile();
	        out = new FileOutputStream(file);
	        int c;
	        byte[] buffer = new byte[1024];
	        while((c = in.read(buffer)) != -1){
	            for(int i = 0; i < c; i++)
	                out.write(buffer[i]);        
	        }
	        flag = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
	        try {
	        	in.close();
		        out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		return flag;
	}
	/**
	 * 对 接收送检结果信息生成报告 接口中的结果集中的传染级别高的结果过滤
	 */
	public  WeChatInspectionReportModel doFilerList(WeChatInspectionReportModel info,User user,SjxxDto t_sjxxDto) {
		//删除已有的sjid的信息
		SjbgljxxDto t_sjbgljxxDto = new SjbgljxxDto();
		t_sjbgljxxDto.setSjid(t_sjxxDto.getSjid());
		 sjbgljxxService.delete(t_sjbgljxxDto);
		//检查里面的物种是不是需要被拦截
		//分表获取pathogen，possible，background参数的list
		List<WeChatInspectionSpeciesModel> pathogen = info.getPathogen();
		List<WeChatInspectionSpeciesModel> background = info.getBackground();
		List<WeChatInspectionSpeciesModel> possible = info.getPossible();
		List<WeChatInspectionSpeciesModel> pathogenNew = getListWz(pathogen,user,t_sjxxDto,"pathogen");
		List<WeChatInspectionSpeciesModel> backgroundNew = getListWz(background,user,t_sjxxDto,"background");
		List<WeChatInspectionSpeciesModel> possibleNew = getListWz(possible,user,t_sjxxDto,"possible");
		info.setBackground(backgroundNew);
		info.setPathogen(pathogenNew);
		info.setPossible(possibleNew);
		return info;
	}
	
	public  List<WeChatInspectionSpeciesModel> getListWz(List<WeChatInspectionSpeciesModel> infoJg,User user,SjxxDto t_sjxxDto,String jglx) {

		// 先遍历pathogen，检查里面的物种是不是需要被拦截
		if (infoJg != null && infoJg.size() > 0) {
			// 建立一个list放入需要发送的物种结果
			List<WeChatInspectionSpeciesModel> sendPathogen = new ArrayList<>();
			//List<WeChatInspectionSpeciesModel> savePathogen = new ArrayList<WeChatInspectionSpeciesModel>();
			List<WeChatInspectionSpeciesModel> list = wzglService.getInspectionSpeciesModelList(infoJg);
			for (WeChatInspectionSpeciesModel m : list) {
				if (m!=null) {
					//不拦截
					if(StringUtil.isBlank(m.getSflj())) {
						sendPathogen.add(m);
					}else if("0".equals(m.getSflj())) {
						sendPathogen.add(m);
					}
					//需要保存到sjbgljxx表
					if(StringUtil.isNotBlank(m.getCrbjb())) {
						SjbgljxxDto sjbgljxxDto = new SjbgljxxDto();
						sjbgljxxDto.setLjid(StringUtil.generateUUID());
						sjbgljxxDto.setSflj(m.getSflj());
						sjbgljxxDto.setBgrq(DateUtils.getCustomFomratCurrentDate(null));
						sjbgljxxDto.setSjid(t_sjxxDto.getSjid());
						sjbgljxxDto.setWzid(m.getTaxid());
						sjbgljxxDto.setJglx(jglx);
						sjbgljxxDto.setFid(m.getGenus_taxid());
						sjbgljxxDto.setDyr(user.getYhid());
						sjbgljxxService.insert(sjbgljxxDto);
					}
				}

			}
			return sendPathogen;

		}
		return infoJg;
	}

	/**
	 * 报告处理中判断项目信息，根据文库的项目代码信息找到相应的项目信息
	 * @param t_sjxxDtos
	 * @param inspinfo
	 * @return
	 * @throws BusinessException
	 */
	public SjxxDto mateProject(List<SjxxDto> t_sjxxDtos,WeChatInspectionReportModel inspinfo) throws BusinessException {
		SjxxDto t_sjxxDto=null;
		String[] xmdms= null;
		XxdyDto xxdyDto=new XxdyDto();
		String Detection_type = inspinfo.getDetection_type();
		// 接口处指定了项目信息
		if(StringUtil.isNotBlank(inspinfo.getProject_type())) {
			List<JcsjDto> ls_jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
			String project_type = inspinfo.getProject_type();
			if (ls_jcxms != null && ls_jcxms.size() > 0) {
				JcsjDto appoint_jcsjDto = null;
				for (JcsjDto jcxm : ls_jcxms) {
					// 查询基础数据，找到匹配相应项目扩展信息的数据
					if (project_type.equals(jcxm.getCskz3()) && Detection_type.equals(jcxm.getCskz1())) {
						appoint_jcsjDto = jcxm;
						break;
					}
				}
				// 指定项目时的处理
				if (appoint_jcsjDto != null) {
					//取第一条送检信息，重置项目信息
					t_sjxxDto = t_sjxxDtos.get(0);
					t_sjxxDto.setJcxmid(appoint_jcsjDto.getCsid());
					t_sjxxDto.setJc_cskz1(appoint_jcsjDto.getCskz1());
					t_sjxxDto.setJc_cskz3(appoint_jcsjDto.getCskz3());
					t_sjxxDto.setYjxmmc(t_sjxxDto.getJcxmmc());
					t_sjxxDto.setJcxmdm(appoint_jcsjDto.getCsdm());
					t_sjxxDto.setJcxmmc(appoint_jcsjDto.getCsmc());
					return t_sjxxDto;
				}else{
					//如果没有找到，则查找检测子项目
					List<JcsjDto> ls_jczxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
					if (ls_jczxms != null && ls_jczxms.size() > 0) {
						JcsjDto appoint_sub_jcsjDto = null;
						for (JcsjDto jcxm : ls_jczxms) {
							// 查询基础数据，找到匹配相应项目扩展信息的数据
							if (project_type.equals(jcxm.getCskz1()) ) {
								appoint_sub_jcsjDto = jcxm;
								break;
							}
						}
						// 指定项目时的处理
						if (appoint_sub_jcsjDto != null) {
							//取第一条送检信息，重置项目信息
							t_sjxxDto = t_sjxxDtos.get(0);
							t_sjxxDto.setJczxm(appoint_sub_jcsjDto.getCsid());
							t_sjxxDto.setJczxmmc(appoint_sub_jcsjDto.getCsmc());
							//重新查找检测项目
							Object o_jcxm = redisUtil.hget("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(), appoint_sub_jcsjDto.getFcsid());
							if(o_jcxm!=null) {
								JcsjDto j_jcxm = JSONObject.parseObject((String) o_jcxm, JcsjDto.class);
								t_sjxxDto.setJcxmid(j_jcxm.getCsid());
								t_sjxxDto.setJc_cskz1(j_jcxm.getCskz1());
								t_sjxxDto.setJc_cskz3(j_jcxm.getCskz3());
								t_sjxxDto.setJcxmdm(j_jcxm.getCsdm());
								t_sjxxDto.setJcxmmc(j_jcxm.getCsmc());
								return t_sjxxDto;
							}
						}
					}
				}
			}
		}
		//if("onco".equals(inspinfo.getReport_type()))
			//Detection_type = "O";
		if(StringUtil.isNotBlank(inspinfo.getXmdm()))
		{
			xmdms = inspinfo.getXmdm().split(",");
			if(xmdms!=null && xmdms.length>0){
				List<String> t_xmdms=new ArrayList<>();
				for(String xmdm:xmdms){
					if(StringUtil.isNotBlank(xmdm)) {
						xmdm=xmdm.substring(0,xmdm.length()-1)+"-";//替换掉最后一位
						t_xmdms.add(xmdm);
					}
				}
				xxdyDto.setDydms(t_xmdms);
				List<XxdyDto> xxdyDtos= xxdyService.getDtoByDydm(xxdyDto);
				boolean isFind = false;
				//查找跟原有项目匹配的，如果找不到则采用默认第一条 20230617 对应代码现在存在重复 tngs 有多个
				for(int i=0;i<xxdyDtos.size();i++) {
					for(int j=0;j<t_sjxxDtos.size();j++) {
						if(StringUtil.isBlank(t_sjxxDtos.get(j).getJczxm())) {
							if (t_sjxxDtos.get(j).getJcxmid().equals(xxdyDtos.get(i).getYxxid())) {
								isFind = true;
								xxdyDto = xxdyDtos.get(i);
								break;
							}
						}else{
							if (t_sjxxDtos.get(j).getJcxmid().equals(xxdyDtos.get(i).getYxxid()) &&
									t_sjxxDtos.get(j).getJczxm().equals(xxdyDtos.get(i).getZid())) {
								isFind = true;
								xxdyDto = xxdyDtos.get(i);
								break;
							}
						}
					}
					if(isFind)
						break;
				}
				if(!isFind) {
					if(xxdyDtos!=null && xxdyDtos.size()>0)//暂时取第一条，不限制cskz3不一样的情况
						xxdyDto=xxdyDtos.get(0);
				}
			}
		}
		// 项目模板对应，用于解决TBtNGS+tNGS泛感染时发送不同的项目模板的问题
		if(StringUtil.isNotBlank(xxdyDto.getKzcs8())){
			inspinfo.setXmmbdy(xxdyDto.getKzcs8());
		}
		//检测类型与项目能匹配
		if(t_sjxxDtos!=null && t_sjxxDtos.size()>0){
			for(int i=0;i<t_sjxxDtos.size();i++){
				if(Detection_type.equals(t_sjxxDtos.get(i).getJc_cskz1())){
					if(xmdms!=null && xmdms.length>0 && t_sjxxDtos.get(i).getJc_cskz3().equals(xxdyDto.getJcxmcskz3())){
						t_sjxxDto=t_sjxxDtos.get(i);
						break;
					}else{
						if(i==t_sjxxDtos.size()-1) {
							t_sjxxDto = t_sjxxDtos.get(i);
							break;
						}
					}
				}
			}
		}else{
			throw new BusinessException("","未获取到送检信息！");
		}

		//当传递的报告跟检测类型不一致时，按照第一个包含DRC的项目来处理
		if(t_sjxxDto==null) {
            for(int i=0;i<t_sjxxDtos.size();i++){
            	String jclx = t_sjxxDtos.get(i).getJc_cskz1();
                if(("D".equals(Detection_type)||"R".equals(Detection_type)||"C".equals(Detection_type)) && ("D".equals(jclx)||"R".equals(jclx)||"C".equals(jclx)))
                {
					if(xmdms!=null && xmdms.length>0 && t_sjxxDtos.get(i).getJc_cskz3().equals(xxdyDto.getJcxmcskz3())){
						t_sjxxDto=t_sjxxDtos.get(i);
						break;
					}else{
						t_sjxxDto=t_sjxxDtos.get(i);
						break;
					}
                }
            }
            
		}
		if(t_sjxxDto==null) {
			//检测类型与项目不能匹配
			List<JcsjDto> jcsjDtos_ls=new ArrayList<>();
			List<JcsjDto> ls_jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
			if(ls_jcxms!=null && ls_jcxms.size()>0){
				for(JcsjDto jcxm:ls_jcxms){
					if(Detection_type.equals(jcxm.getCskz1())){
						jcsjDtos_ls.add(jcxm);
					}
				}
				if(jcsjDtos_ls!=null && jcsjDtos_ls.size()>0){
					if(t_sjxxDtos!=null && t_sjxxDtos.size()>0){
						for(int i=0;i<t_sjxxDtos.size();i++){
							for(JcsjDto jcsjDto_ls:jcsjDtos_ls){
                                if (jcsjDto_ls.getCskz3().equals(t_sjxxDtos.get(i).getJc_cskz3()) && jcsjDto_ls.getCskz3().indexOf("REM") == -1) {
                                    t_sjxxDto = t_sjxxDtos.get(i);
                                    break;
                                }
							}
						}
					}else{
						try {
							throw new BusinessException("","未获取到送检信息！");
						} catch (BusinessException e) {
							throw new RuntimeException(e);
						}
					}
				}else{
					throw new BusinessException("","检测项目不存在!");
				}
			}else{
				throw new BusinessException("","检测项目不存在!");
			}
		}
		if(t_sjxxDto==null)
			throw new BusinessException("","检测项目匹配失败!");

		return t_sjxxDto;
	}
	
	/**
	 * 处理生信部报告数据
	 * @param request
	 * @param inspinfo
	 * @param user
	 * @return
	 * @throws BusinessException 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String,Object> dealInspectionReportInfoTwo(HttpServletRequest request,WeChatInspectionReportModel inspinfo,User user,JkdymxDto jkdymxDto) throws BusinessException {
		Map<String,Object> map = new HashMap<>();
		if (inspinfo != null) {
			if (StringUtil.isNotBlank(inspinfo.getStatus()) && !"success".equals(inspinfo.getStatus())){
				throw new BusinessException("", "inspinfo.getStatus is NotBlank and Success！");
			}
			// 查询是否有此标本信息
			String sampleid=inspinfo.getSample_id();
			//根据文库编号获取复检信息
			boolean updateFj=false;
			FjsqDto fjsqDto = new FjsqDto();
			List<String> wkbms=new ArrayList<>();
			String[] sampleids= sampleid.split("\\+");
			if(sampleids!=null && sampleids.length>0) {
				for (String t_sampleid: sampleids){
					if(StringUtil.isNotBlank(t_sampleid))
						wkbms.add(t_sampleid);
				}
			}
			fjsqDto.setWkbms(wkbms);
			List<FjsqDto> fjsqDtos = fjsqService.getDtoByWkbms(fjsqDto);
			SjxxDto sjxxDto = new SjxxDto();
			sjxxDto.setYbbh(inspinfo.getYbbh());
			sjxxDto.setJclx(inspinfo.getDetection_type());
			sjxxDto.setJczlx(inspinfo.getDetection_subtype());
			List<SjxxDto> t_sjxxDtos = sjxxService.getSjxxDtosByYbbh(sjxxDto);
			SjxxDto t_sjxxDto=mateProject(t_sjxxDtos,inspinfo);
			map.put("t_sjxxDtos", t_sjxxDto);
			if (t_sjxxDto != null) {
				map.put("yjxmmc", t_sjxxDto.getYjxmmc());
				//血液肿瘤处理
				if("onco".equals(inspinfo.getReport_type())){
					map = oncoInspectionReport(request,inspinfo,t_sjxxDto,jkdymxDto);
				}else{
					//处理接口里面的数据，过滤掉传染级别搞得物种信息
					doFilerListTwo(inspinfo,user,t_sjxxDto);
					// 接收报告的接口更改，检测类型改为csid  2020-9-14 姚嘉伟 查询检测项目ID处理 2020-9-24 lishangyuan modify start
					inspinfo.setDetection_cskz1(inspinfo.getDetection_type());
					if(StringUtil.isBlank(inspinfo.getDetection_cskz1())) {
						inspinfo.setDetection_cskz1("D");
					}
					//获取检测项目的参数扩展3信息，如果外部想要发送指定项目模板，则需要将参数扩展3信息传入接口，接口会根据目模板信息，然后进行匹配
					String cskz3 = t_sjxxDto.getJc_cskz3();

					//如果为去人源，先查询原来的检测项目，获取cskz3后加上"-REM"，再去查询对应的检测项目
					if(inspinfo.getSample_id().contains("-REM") ) {
						inspinfo.setSfqry("1");//去人源标记，判断是否发送去人源报告
						if(!cskz3.endsWith("_REM"))
							cskz3 += "_REM";
					}
					// 根据cskz3和类型获取检测项目ID
					List<JcsjDto> jc_jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
					List<JcsjDto> jc_jczxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
					boolean isFind = false;
					JcsjDto t_jcsjDto = null;
					//若project_type不为空，则将其作为kzc8的值，确认其模板
					/*if(StringUtil.isNotBlank(inspinfo.getProject_type())){
						int index=inspinfo.getProject_type().lastIndexOf("_");
						cskz3=inspinfo.getProject_type().substring(0,index);
						inspinfo.setDetection_cskz1(inspinfo.getProject_type().substring(index+1,inspinfo.getProject_type().length()));
					}*/
					if(StringUtil.isNotBlank(inspinfo.getXmmbdy()) && StringUtil.isBlank(inspinfo.getProject_type())){//如果信息对应的kzcs8不为空，则直接匹配项目模板
						for(int i =0;i< jc_jcxms.size();i ++){
							if(inspinfo.getXmmbdy().equals(jc_jcxms.get(i).getCskz3())){
								inspinfo.setDetection_type(jc_jcxms.get(i).getCsid());
								map.put("jcsjDto", jc_jcxms.get(i));
								map.put("bgjcxmmc", jc_jcxms.get(i).getCsmc());
								map.put("jcxmdm", jc_jcxms.get(i).getCsdm());
								isFind = true;
								break;
							}
						}
						if(!isFind) {
							for(int i =0;i< jc_jczxms.size();i ++){
								JcsjDto t_xmDto = jc_jczxms.get(i);
								if(inspinfo.getXmmbdy().equals(t_xmDto.getCskz1())){
									inspinfo.setDetection_type(t_xmDto.getFcsid());
									inspinfo.setDetection_subtype(t_xmDto.getCsid());
									map.put("jcsjDto", t_xmDto);
									/*Object o_jcsj = redisUtil.hget("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(), t_xmDto.getFcsid());
									if(o_jcsj==null)
										break;
									JcsjDto pa_jcsjDto = JSONObject.parseObject(o_jcsj.toString(), JcsjDto.class);
									map.put("bgjcxmmc", pa_jcsjDto.getCsmc() + "-" + t_xmDto.getCsmc());*/
									map.put("bgjcxmmc", t_xmDto.getCsmc());
									map.put("jcxmdm", t_xmDto.getCsdm());
									isFind = true;
									break;
								}
							}
						}
					}else if(jc_jcxms != null) {
						//需要按照 相同扩展参数3，以及inspinfo 里指定的Detection_cskz1 查询基础数据 2023-04-01
						for(int i =0;i< jc_jcxms.size();i ++){
							t_jcsjDto = jc_jcxms.get(i);

							if(inspinfo.getDetection_cskz1().equals(t_jcsjDto.getCskz1())) {
								if(StringUtil.isNotBlank(cskz3)){
									if(t_jcsjDto.getCskz3().equals(cskz3)) {
										inspinfo.setDetection_type(t_jcsjDto.getCsid());
										map.put("jcsjDto", t_jcsjDto);
										map.put("bgjcxmmc", t_jcsjDto.getCsmc());
										map.put("jcxmdm", t_jcsjDto.getCsdm());
										isFind = true;
										break;
									}
								}else {
									inspinfo.setDetection_type(t_jcsjDto.getCsid());
									map.put("jcsjDto", t_jcsjDto);
									map.put("bgjcxmmc", t_jcsjDto.getCsmc());
									map.put("jcxmdm", t_jcsjDto.getCsdm());
									isFind = true;
									break;
								}
							}
						}
						if(!isFind) {
							for(int i =0;i< jc_jczxms.size();i ++){
								t_jcsjDto = jc_jczxms.get(i);
								if(cskz3.equals(t_jcsjDto.getCskz3()) && inspinfo.getDetection_cskz1().equals(t_jcsjDto.getCskz1())) {
									inspinfo.setDetection_type(t_jcsjDto.getFcsid());
									inspinfo.setDetection_subtype(t_jcsjDto.getCsid());
									map.put("jcsjDto", t_jcsjDto);
									map.put("bgjcxmmc", t_jcsjDto.getCsmc());
									map.put("jcxmdm", t_jcsjDto.getCsdm());
									isFind = true;
									break;
								}
							}
						}
					}
					//如果没有找到相应项目，那么考虑不限制扩展参数1而直接采用 扩展参数3 来匹配，并且匹配为C的方式
					if(!isFind) {
						//需要按照 相同扩展参数3，以及inspinfo 里指定的Detection_cskz1 查询基础数据 2023-04-01
						for(int i =0;i< jc_jcxms.size();i ++){
							t_jcsjDto = jc_jcxms.get(i);

							if(StringUtil.isNotBlank(cskz3)){
								if(t_jcsjDto.getCskz3().equals(cskz3) && "C".equals(t_jcsjDto.getCskz1())) {
									inspinfo.setDetection_type(t_jcsjDto.getCsid());
									map.put("jcsjDto", t_jcsjDto);
									map.put("bgjcxmmc", t_jcsjDto.getCsmc());
									map.put("jcxmdm", t_jcsjDto.getCsdm());
									isFind = true;
									break;
								}
							}
						}
						if(!isFind) {
							for(int i =0;i< jc_jczxms.size();i ++){
								t_jcsjDto = jc_jczxms.get(i);
								if(cskz3.equals(t_jcsjDto.getCskz3()) && inspinfo.getDetection_cskz1().equals(t_jcsjDto.getCskz1())) {
									inspinfo.setDetection_type(t_jcsjDto.getFcsid());
									inspinfo.setDetection_subtype(t_jcsjDto.getCsid());
									map.put("jcsjDto", t_jcsjDto);
									map.put("bgjcxmmc", t_jcsjDto.getCsmc());
									map.put("jcxmdm", t_jcsjDto.getCsdm());
									isFind = true;
									break;
								}
							}
						}
					}
					if(!isFind) {
						throw new BusinessException("", "未查询到相应的检测项目！");
					}
					//接收报告的接口更改，检测类型改为csid  2020-9-14 姚嘉伟  2020-9-24 lishangyuan modify end
					if(request != null && request instanceof MultipartHttpServletRequest && ((MultipartHttpServletRequest) request).getFile("file") != null) {
						try {
							String ywlx=BusTypeEnum.IMP_SEQ_QUALITY.getCode();
							//处理文件流信息
							dealFileInput(request,t_sjxxDto.getSjid(),ywlx,"file");
						}catch(Exception e) {
							throw new BusinessException("",e.toString());
						}
					} else {
						log.error("未获取到文件流信息");
					}
					//接收报告的接口更改，检测类型改为csid  2020-9-14 姚嘉伟  2020-9-24 lishangyuan modify end
					if(request != null && request instanceof MultipartHttpServletRequest && ((MultipartHttpServletRequest) request).getFile("tbfile") != null) {
						try {
							String ywlx=BusTypeEnum.IMP_FILE_JHNY_TEMEPLATE.getCode();
							//处理文件流信息
							dealFileInput(request,t_sjxxDto.getSjid(),ywlx,"tbfile");
						}catch(Exception e) {
							throw new BusinessException("",e.toString());
						}
					} else {
						log.error("未获取到文件流信息");
					}
					inspinfo.setSjid(t_sjxxDto.getSjid());
					// 更新送检信息Q20,Q30,人源指数字段信息
					t_sjxxDto.setQ20(inspinfo.getQ20());
					t_sjxxDto.setQ30(inspinfo.getQ30());
					// 保存信息至送检临床指南表
					SjlcznDto sjlcznDto = new SjlcznDto();
					//List<String> ids = new ArrayList<>();

					SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					if (inspinfo.getGuidelines() != null && inspinfo.getGuidelines().size() > 0) {
						List<WechatClinicalGuideModel> guidelines = inspinfo.getGuidelines();

						List<SjlcznModel> l_sjlcznDtos = new ArrayList<>();
						try {
							for (int i=0;i<guidelines.size();i++) {
								SjlcznModel t_sjlcznDto = new SjlcznModel();
								guidelines.get(i).setXh(String.valueOf(i+1));
								//ids.add(guidelines.get(i).getId());
								if (StringUtil.isNotBlank(guidelines.get(i).getLast_update())){
									Date last_update = dateformat.parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
											new Date(Long.parseLong(guidelines.get(i).getLast_update()) * 1000)));
									String update_date = dateformat.format(last_update);
									guidelines.get(i).setLast_update(update_date);
								}
								t_sjlcznDto.setSjid(t_sjxxDto.getSjid());
								t_sjlcznDto.setZnid(guidelines.get(i).getId());
								if(StringUtil.isNotBlank(guidelines.get(i).getSpecies())) {
									t_sjlcznDto.setWzid(guidelines.get(i).getSpecies().replace("[", "").replace("]", "").replace("\"", ""));
								}

								t_sjlcznDto.setJclx(inspinfo.getDetection_type());
								t_sjlcznDto.setJczlx(inspinfo.getDetection_subtype());
								l_sjlcznDtos.add(t_sjlcznDto);
							}
						} catch (Exception e) {
							log.error(e.toString());
							throw new BusinessException("",e.toString());
						}
						map.put("guidelines", guidelines);
						sjlcznDto.setSjid(t_sjxxDto.getSjid());

						// 删除原来的
						sjlcznService.delete(sjlcznDto);
						// 添加新的
						sjlcznService.batchInsert(l_sjlcznDtos);
					}else {
						sjlcznDto.setSjid(t_sjxxDto.getSjid());
						sjlcznService.delete(sjlcznDto);
					}
					map.put("lib_concentration", inspinfo.getLib_concentration());
					map.put("nonhuman_reads", inspinfo.getNonhuman_reads());
					map.put("nuc_concentration", inspinfo.getNuc_concentration());
					if (StringUtil.isNotBlank(inspinfo.getTotal_reads())){
						try {
							BigDecimal total_reads = new BigDecimal(inspinfo.getTotal_reads());
							BigDecimal microbial_reads = new BigDecimal(inspinfo.getMicrobial_reads());
							map.put("wxwxlzb",microbial_reads.multiply(new BigDecimal(100)).divide(total_reads,2, RoundingMode.HALF_UP).doubleValue() + "%");
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					}
					List<WechatReferencesModel> papers = inspinfo.getPapers();
					//送检物种信息处理
					Map<String,Map<String,Object>> speciesMap = saveInspSpecies(t_sjxxDto, inspinfo, papers, user, map);

					//保存参考文献信息
					saveRefs(inspinfo,speciesMap, user,map);
					//更新送检信息
					//sjxxService.update(t_sjxxDto);
					// 同步至微信服务器
					//if (StringUtil.isNotBlank(menuurl))
					//	RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_MOD.getCode() + JSONObject.toJSONString(t_sjxxDto));

					jkdymxDto.setZywid(inspinfo.getDetection_type());
					jkdymxDto.setZxmid(inspinfo.getDetection_subtype());
					if(StringUtil.isNotBlank(inspinfo.getXmdm()) && inspinfo.getXmdm().length()>5 ){
						String ljbj=inspinfo.getXmdm().substring(4,5);
						//hbfs合并发送标记为1代表是要合并tngs和TBT结果，正常走报告发送
						if("G".equals(ljbj) && !"1".equals(inspinfo.getHbfs())){//若为TBT+Tngs的泛感染结果，则不发送报告，只保存数据库，项目代码为TCCTG-
								map.put("status", "success");
								map.put("ljbj", "1");
						}else if("1".equals(inspinfo.getHbfs())) {//若为合并发送，需要获取一下TBT的质控图
							log.error("获取质控图");
							FjcfbDto fjcfbDto = new FjcfbDto();
							fjcfbDto.setYwid(t_sjxxDto.getSjid());
							fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_JHNY_TEMEPLATE.getCode());
							List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
							DBEncrypt p = new DBEncrypt();
							for (FjcfbDto fjcfbDto1 : fjcfbDtos) {
								if (StringUtil.isNotBlank(fjcfbDto1.getWjlj())) {
									fjcfbDto1.setWjlj(p.dCode(fjcfbDto1.getWjlj()));
								}
							}
							map.put("Fj_List", fjcfbDtos);
							// 更新报告日期
							Date date = new Date();
							map.put("bgsj", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
							t_sjxxDto.setBgrq(date.toString());//若不是，则正常更新bgrq
							map.put("bgrq_sjbgsm", "1");
							updateFj=true;
						}else{
							// 更新报告日期
							Date date = new Date();
							map.put("bgsj",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
							t_sjxxDto.setBgrq(date.toString());//若不是，则正常更新bgrq
							map.put("bgrq_sjbgsm","1");
							updateFj=true;
						}
					}else{
						throw new BusinessException("","xmdm为空！");
					}
					//保存送检报告说明,并将文献信息整理
					saveSjbgsm(t_sjxxDto, inspinfo, speciesMap, map,jkdymxDto);

					// 复测处理，如果为量仅一次，则通知相应人员
					saveRecheckByRpm(t_sjxxDto,inspinfo,t_jcsjDto,user);

					// 保存送检耐药性
					saveDrugResistance(t_sjxxDto,inspinfo,map);

					//处理pathogen、possible、background检出物种的阳性flag
					dealTaxFlgList(map,inspinfo);
					//处理毒力因子数据
					dealVirulenceFactorStat(t_sjxxDto,map,inspinfo);
				}
				//存放是否含定值列表标记
				map.put("has_commensal",inspinfo.getHas_commensal() != null ?inspinfo.getHas_commensal().toString():"");

				//为防止更新掉sjxx的xmmc，比如送检时DNA，然后加测是DNA+RNA ，会照成把相应的项目名称(D+R)更新到XMMC，这是不对的 2024-02-21
				t_sjxxDto.setJcxmmc(null);

				if ("onco".equals(inspinfo.getReport_type())&&((!t_sjxxDto.getJc_cskz3().contains("IMP_REPORT_ONCO_QINDEX_TEMEPLATE") && !"IMP_SPEED".equals(t_sjxxDto.getJc_cskz3()) && StringUtil.isNotBlank(t_sjxxDto.getJc_cskz3()) && !t_sjxxDto.getJc_cskz3().contains("IMP_REPORT_SEQ_") )
						|| !"1".equals(t_sjxxDto.getHbcskz3()))){
					map.put("status", "fail");
					map.put("errorCode", "由于当前检测项目以及合作伙伴限制，该报告不生成！请在运营管理的合作伙伴列表里修改【Onco报告】的允许标记！");
					return map;
				}

				boolean result = sjxxService.update(t_sjxxDto);
				if (!result){
					throw new BusinessException("","更新报告日期失败！");
				}

				if(!CollectionUtils.isEmpty(fjsqDtos) && updateFj){
					fjsqService.updateBgrqByDtos(fjsqDtos);
				}

				if (StringUtil.isNotBlank(menuurl)){
					RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_MOD.getCode() + JSONObject.toJSONString(t_sjxxDto));
				}
				map.put("sjxxDto", t_sjxxDto);
			} else {
				// 不存在此标本，将数据加入错误信息表
				CwglDto cwglDto = new CwglDto();
				cwglDto.setCwlx(ErrorTypeEnum.INSPECTION_RESULT_TYPE.getCode());
				String str = JSONObject.toJSONString(inspinfo);
				if (str.length() > 4000) {
					str = str.substring(0, 4000);
				}
				cwglDto.setYsnr(str);
				cwglService.insertDto(cwglDto);
				throw new BusinessException("","不存在此标本，将数据加入错误信息表！");
			}
			log.error("dealInspectionReportInfo -- true");
			map.put("status", "success");
			map.put("errorCode", "0");
			return map;
		}
		throw new BusinessException("","inspinfo为空!");
	}
	
	/**
	 * 处理Onco（血液肿瘤）的报告结果信息，并保存到数据库中
	 * @param request
	 * @param inspinfo
	 * @param t_sjxxDto
	 * @return
	 * @throws BusinessException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private Map<String,Object> oncoInspectionReport(HttpServletRequest request,WeChatInspectionReportModel inspinfo,SjxxDto t_sjxxDto,JkdymxDto jkdymxDto)
			throws BusinessException
	{
		Map<String,Object> map = new HashMap<>();
//		Object oncoxtsz=redisUtil.hget("matridx_xtsz","onco.report.flag");
//		XtszDto xtszDto=new XtszDto();
//		boolean flag=false;//true为启动科室判断
//		if(oncoxtsz!=null){
//			xtszDto= (XtszDto)JSONArray.parse(String.valueOf(oncoxtsz));
//			if("1".equals(xtszDto.getSzz())){
//				flag=true;
//			}
//		}//设置科室限制


		// 接收报告的接口更改，检测类型改为csid  2020-9-14 姚嘉伟 查询检测项目ID处理 2020-9-24 lishangyuan modify start
		inspinfo.setDetection_cskz1(inspinfo.getDetection_type());
		if(StringUtil.isBlank(inspinfo.getDetection_cskz1())) {
			inspinfo.setDetection_cskz1("D");
		}
//		String cskz3 = t_sjxxDto.getJc_cskz3();
//		//如果为去人源，先查询原来的检测项目，获取cskz3后加上"-REM"，再去查询对应的检测项目
//		if(inspinfo.getSample_id().contains("-REM") && !cskz3.endsWith("_REM")) {
//			cskz3 += "_REM";
//		}
		// 根据cskz3和类型获取检测项目ID
		String jclb = BasicDataTypeEnum.DETECT_TYPE.getCode();
		SjbgsmDto sjbgsmDto=new SjbgsmDto();

		//保存Onco报告结果
		if("阳性".equals(inspinfo.getReview_result())){
			sjbgsmDto.setJyjg("1");
		}else if("阴性".equals(inspinfo.getReview_result())){
			sjbgsmDto.setJyjg("0");
		}else if("疑似".equals(inspinfo.getReview_result())){
			sjbgsmDto.setJyjg("2");
		}else{
			throw new BusinessException("", "review_result："+inspinfo.getReview_result()+" 检测结果为空！");
		}

		List<JcsjDto> jc_jcxms = redisUtil.hmgetDto("matridx_jcsj:" + jclb);
		boolean isFind = false;
		if(jc_jcxms != null) {
			for(int i =0;i< jc_jcxms.size();i ++){
				JcsjDto t_jcsjDto = jc_jcxms.get(i);
				if("033".equals(t_jcsjDto.getCsdm()))
				{
					inspinfo.setDetection_type(t_jcsjDto.getCsid());
					map.put("jcsjDto",t_jcsjDto);
					map.put("bgjcxmmc",t_jcsjDto.getCsmc());
					map.put("jcxmdm", t_jcsjDto.getCsdm());
					sjbgsmDto.setJclx(t_jcsjDto.getCsid());
					jkdymxDto.setZywid(t_jcsjDto.getCsid());
					isFind = true;
					break;
				}
			}
		}
		if(!isFind) {
			throw new BusinessException("", "未查询到相应的检测项目！");
		}
		sjbgsmDto.setSjid(t_sjxxDto.getSjid());

		String fzhxjg=inspinfo.getKaryotype();
		String t_fzhxjg="";
		if(StringUtils.isNotBlank(fzhxjg)){
			String[] str_fzhxjg=fzhxjg.split("\",\"");
			if(str_fzhxjg!=null && str_fzhxjg.length>0){
				str_fzhxjg[0]=str_fzhxjg[0].substring(2);
				str_fzhxjg[str_fzhxjg.length-1]=str_fzhxjg[str_fzhxjg.length-1].substring(0,str_fzhxjg[str_fzhxjg.length-1].length()-2);
				for(int i=0;i<str_fzhxjg.length;i++){
					t_fzhxjg=t_fzhxjg+","+str_fzhxjg[i];
				}
				t_fzhxjg=t_fzhxjg.substring(1);
			}
		}
		sjbgsmDto.setFzhxjg(t_fzhxjg);//分子核型结果
//		t_sjxxDto.setFzhxjg(t_fzhxjg);//分子核型结果
		JSONObject jsonObject=JSONObject.parseObject(inspinfo.getCnv_data());
		String t_result=jsonObject.get("result").toString();
		String detail=jsonObject.get("detail").toString();
		String f_result="";
		String f_detail="";
		if(StringUtils.isNotBlank(t_result)){
			String[] str_result=t_result.split("\",\"");
			String[] str_detail=detail.split("\",\"");
			if(str_result!=null && str_result.length>0){
				str_result[0]=str_result[0].substring(2);
				str_result[str_result.length-1]=str_result[str_result.length-1].substring(0,str_result[str_result.length-1].length()-2);
				for(int i=0;i<str_result.length;i++){
					f_result=f_result+";"+str_result[i];
				}
				f_result=f_result.substring(1);
//				f_result=f_result.replaceAll(";","\r\n");
			}
			if(str_detail!=null && str_detail.length>0){
				str_detail[0]=str_detail[0].substring(2);
				str_detail[str_detail.length-1]=str_detail[str_detail.length-1].substring(0,str_detail[str_detail.length-1].length()-2);
				for(int i=0;i<str_detail.length;i++){
					f_detail=f_detail+";"+str_detail[i];
				}
				f_detail=f_detail.substring(1);
//				f_detail=f_detail.replaceAll(";","\r\n");
			}
		}
		sjbgsmDto.setZxls(jsonObject.get("total_reads").toString());
		sjbgsmDto.setGc(jsonObject.get("gc").toString());
        sjbgsmDto.setCskz1(f_result);
		sjbgsmDto.setCskz2(f_detail);
		sjbgsmDto.setBgrq(DateUtils.getCustomFomratCurrentDate(null));
        //添加送检报告说明，先删除再新增
		List<SjbgsmDto> list = sjbgsmService.selectBySjbgsmDto(sjbgsmDto);
		if (CollectionUtils.isNotEmpty(list) && StringUtil.isNotBlank(list.get(0).getScbgrq())){
			sjbgsmDto.setScbgrq(list.get(0).getScbgrq());
		}else{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			sjbgsmDto.setScbgrq(simpleDateFormat.format(date));
		}
		sjbgsmService.deleteBySjbgsmDto(sjbgsmDto);
		boolean addbgsm = sjbgsmService.insert(sjbgsmDto);
		//保存送检报告说明
		map.put("sjbgsmDto", sjbgsmDto);
		if(!addbgsm){
			throw new BusinessException("","添加送检报告说明失败！");
		}
        if(request != null && ((MultipartHttpServletRequest) request).getFile("file") != null) {
            //处理unco文件流信息
            dealuncoFileInput(request,t_sjxxDto.getSjid(),map);
        }
		log.error("dealInspectionReportInfo2 -- true");
		map.put("sjxxDto", t_sjxxDto);
		map.put("status", "success");
		map.put("errorCode", "0");
		return map;
	}
	
	/**
	 * 处理生信系统传递过来的unco图片，需要进行截取
	 * @param request
	 * @param ywid
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void dealuncoFileInput(HttpServletRequest request,String ywid,Map<String,Object> resultmap) 
	throws BusinessException
	{
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(((MultipartHttpServletRequest) request).getFile("file").getName());
		MultipartFile[] imp_file = new MultipartFile[files.size()];
		files.toArray(imp_file);
		if(imp_file!=null&& imp_file.length>0){
			String ywlx=BusTypeEnum.IMP_SEQ_QUALITY_CHROMOSOME.getCode();
			FjcfbDto t_fjcfbDto = new FjcfbDto();
			t_fjcfbDto.setYwid(ywid);
			t_fjcfbDto.setYwlx(ywlx);
			fjcfbService.deleteByYwidAndYwlx(t_fjcfbDto);
			t_fjcfbDto.setYwlx(BusTypeEnum.IMP_SEQ_QUALITY_CHROMOSOME_DEAL.getCode());
			fjcfbService.deleteByYwidAndYwlx(t_fjcfbDto);
			
			for(int i=0;i<imp_file.length;i++){
				if(!imp_file[i].isEmpty()){
					MultipartFile file = imp_file[i];

					//把文件保存到附件存放表里
					String wjlj = fileSaveToFjcfb(file,ywlx,ywid,i);
					//原图片保存完成后重新获取图片并进行截取
					// 取得图片读入器
					Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("png");
					ImageReader reader = readers.next();
					try {

						String dl_fwjlj = prefix + releaseFilePath + BusTypeEnum.IMP_SEQ_QUALITY_CHROMOSOME_DEAL.getCode() + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + 
						"UP" + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
						
						String wjm = file.getOriginalFilename();
						int index = (wjm.lastIndexOf(".") > 0) ? wjm.lastIndexOf(".") : wjm.length() - 1;
						String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
						String fwjm = t_name + wjm.substring(index);
						String dl_wjlj = dl_fwjlj + "/" + fwjm;
						
						mkDirs(dl_wjlj);
						DBEncrypt bpe = new DBEncrypt();

						FjcfbDto dl_fjcfbDto = new FjcfbDto();
						dl_fjcfbDto.setFjid(StringUtil.generateUUID());
						dl_fjcfbDto.setYwid(ywid);
						dl_fjcfbDto.setZywid("");
						dl_fjcfbDto.setXh(String.valueOf(i+1));
						dl_fjcfbDto.setYwlx(BusTypeEnum.IMP_SEQ_QUALITY_CHROMOSOME_DEAL.getCode());
						dl_fjcfbDto.setWjm(wjm);
						dl_fjcfbDto.setWjlj(bpe.eCode(dl_wjlj));
						dl_fjcfbDto.setFwjlj(bpe.eCode(dl_fwjlj));
						dl_fjcfbDto.setFwjm(bpe.eCode(fwjm));
						dl_fjcfbDto.setZhbj("0");
						boolean dl_result = fjcfbService.insert(dl_fjcfbDto);
						if(!dl_result){
							throw new BusinessException("","新增附件存放表失败！");
						}
						// uno的CNV图片
						resultmap.put("cnv_img", dl_fjcfbDto);
						// 取得图片读入流
						InputStream source = new FileInputStream(wjlj);
						ImageInputStream iis = ImageIO.createImageInputStream(source);
						reader.setInput(iis, true);
						// 图片参数对象
						ImageReadParam param = reader.getDefaultReadParam();
						Rectangle rect = new Rectangle(5, 50, 3500, 1500);
						param.setSourceRegion(rect);
						BufferedImage bi = reader.read(0, param);
						ImageIO.write(bi, "png", new File(dl_wjlj));
						iis.close();
					} catch (Exception e) {
						log.error(e.getMessage());
						throw new BusinessException("",e.getMessage());
					}
					finally {
						reader.dispose();
					}
				}
			}
		}
	}
	
	
	/**
	 * 处理外部传递过来的文件流信息
	 * @param request
	 * @param ywid 业务ID
	 * @param ywlx 业务类型
	 * @throws BusinessException
	 */
	private void dealFileInput(HttpServletRequest request,String ywid,String ywlx,String paramNam) throws BusinessException{
		List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles(((MultipartHttpServletRequest) request).getFile(paramNam).getName());
		MultipartFile[] imp_file = new MultipartFile[files.size()];
		files.toArray(imp_file);
		if(imp_file!=null&& imp_file.length>0){
			FjcfbDto t_fjcfbDto = new FjcfbDto();
			t_fjcfbDto.setYwid(ywid);
			t_fjcfbDto.setYwlx(ywlx);
			fjcfbService.deleteByYwidAndYwlx(t_fjcfbDto);
			for(int i=0;i<imp_file.length;i++){
				if(!imp_file[i].isEmpty()){
					MultipartFile file = imp_file[i];
					//把文件保存到附件存放表里
					fileSaveToFjcfb(file,ywlx,ywid,i);
				}
			}
		}
	}
	
	/**
	 * 把文件保存到附件存放表里
	 * @param file 文件
	 * @param ywlx 业务类型
	 * @param ywid 业务ID
	 */
	private String fileSaveToFjcfb(MultipartFile file,String ywlx,String ywid,int xh) throws BusinessException {
		//文件处理
		String wjm = file.getOriginalFilename();
		// 根据日期创建文件夹
		String fwjlj = prefix + releaseFilePath + ywlx + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/"
				+ "UP" + DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		int index = (wjm.lastIndexOf(".") > 0) ? wjm.lastIndexOf(".") : wjm.length() - 1;
		String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
		String fwjm = t_name + wjm.substring(index);
		String wjlj = fwjlj + "/" + fwjm;
		mkDirs(fwjlj);
		DBEncrypt bpe = new DBEncrypt();
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setFjid(StringUtil.generateUUID());
		fjcfbDto.setYwid(ywid);
		fjcfbDto.setZywid("");
		fjcfbDto.setXh(String.valueOf(xh+1));
		fjcfbDto.setYwlx(ywlx);
		fjcfbDto.setWjm(wjm);
		fjcfbDto.setWjlj(bpe.eCode(wjlj));
		fjcfbDto.setFwjlj(bpe.eCode(fwjlj));
		fjcfbDto.setFwjm(bpe.eCode(fwjm));
		fjcfbDto.setZhbj("0");
		boolean result = fjcfbService.insert(fjcfbDto);
		if(!result){
			throw new BusinessException("","新增附件存放表失败！");
		}
		byte[] buffer = new byte[4096];
		FileOutputStream fos = null;
		BufferedOutputStream output = null;

		InputStream fis = null;
		BufferedInputStream input = null;
		try
		{
			fis = file.getInputStream();
			input = new BufferedInputStream(fis);
			mkDirs(fwjlj);
			fos = new FileOutputStream(wjlj);
			output = new BufferedOutputStream(fos);
			int n;
			while ((n = input.read(buffer, 0, 4096)) > -1)
			{
				output.write(buffer, 0, n);
			}
		} catch (Exception e)
		{
			log.error(e.getMessage());
			throw new BusinessException("",e.getMessage());
		} finally
		{
			closeStream(new Closeable[]
					{ fis, input, output, fos });
		}
		return wjlj;
	}
	
	/**
	 * 保存送检的检出结果，并把结果存放到送检物种信息表中，同时更新物种信息
	 * @param t_sjxxDto
	 * @param inspinfo
	 * @param papers
	 * @param user
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private Map<String,Map<String,Object>> saveInspSpecies(SjxxDto t_sjxxDto,WeChatInspectionReportModel inspinfo,List<WechatReferencesModel> papers,User user,Map<String,Object> map)
			throws BusinessException
	{
		Map<String,Map<String,Object>> resultMap = new HashMap<>();
		// 保存信息至送检物种管理表（）
		// 检验结果 0：阴性 1：阳性 2：无
		String s_jyjg = "0";
		//先删除原有物种信息
		SjwzxxDto sjwzxx=new SjwzxxDto();
		sjwzxx.setSjid(t_sjxxDto.getSjid());
		sjwzxx.setJclx(inspinfo.getDetection_type());
		sjwzxx.setJczlx(inspinfo.getDetection_subtype());
		sjwzxxService.deleteBysjwzxxDto(sjwzxx);

		Map<String, Object> possibleMap = null;//疑似物种
		Map<String, Object> pathogenMap = null;//高关物种
		Map<String, Object> backgroundMap = null;//背景物种
		Map<String, Object> commensalMap = null;//commensal物种
		Map<String, Object> backgroundInfoMap = null;//除去commensal的物种
		Map<String,Object> otherMap = new HashMap<>();
		//获取检测类型
		String jclx_str;
		if ("D".equals(inspinfo.getDetection_cskz1())){
			jclx_str="DNA";
		}else if ("C".equals(inspinfo.getDetection_cskz1())){
			jclx_str="DNA+RNA";
		}else if ("R".equals(inspinfo.getDetection_cskz1())){
			jclx_str="RNA";
		}else {
			jclx_str="--";
		}
		//获取inspinfo中的possible,pathogen,background,commensal并处理
		List<WeChatInspectionSpeciesModel> possible = inspinfo.getPossible();
		List<WeChatInspectionSpeciesModel> pathogen = inspinfo.getPathogen();
		List<WeChatInspectionSpeciesModel> background = inspinfo.getBackground();
		//处理并排序
		Map<String, List<SjwzxxDto>> listMap = sortBySpeciesMap(
				weChatInspectionSpeciesModelToSjwzxxDto(possible, papers, inspinfo, user.getYhid(), "possible", jclx_str),
				weChatInspectionSpeciesModelToSjwzxxDto(pathogen, papers, inspinfo, user.getYhid(), "pathogen", jclx_str),
				weChatInspectionSpeciesModelToSjwzxxDto(background, papers, inspinfo, user.getYhid(), "background", jclx_str));

		List<SjwzxxDto> possibles = listMap.get("possibles");
		List<SjwzxxDto> pathogens = listMap.get("pathogens");
		List<SjwzxxDto> backgrounds = listMap.get("backgrounds");
		List<SjwzxxDto> commensals = listMap.get("commensals");
		List<SjwzxxDto> backgroundInfos = listMap.get("backgroundInfos");
		List<SjwzxxDto> pathogensAndpossibles = listMap.get("pathogensAndpossibles");
		List<SjwzxxDto> sjwzxxDtos  = listMap.get("list");
		//commensal
		String commensalmc = "";//commensal的检测结果名称
		if (commensals != null && commensals.size() > 0) {
			commensalMap = DealSjwzxxWithSjwzxxs("4",commensals);
			commensalmc = (String) commensalMap.get("jcjgmc");
		}
		//background
		String backgroundmc = "";//背景的检测结果名称（疑似结果）
		if (backgrounds != null && backgrounds.size() > 0) {
			s_jyjg = "2";
			backgroundMap = DealSjwzxxWithSjwzxxs("3",backgrounds);
			backgroundmc = (String) backgroundMap.get("jcjgmc");
		}
		//backgroundInfo
		String backgroundInfomc = "";//背景的检测结果名称（疑似结果），不包含label为commensal的物种
		if (backgroundInfos != null && backgroundInfos.size() > 0) {
			backgroundInfoMap = DealSjwzxxWithSjwzxxs("3",backgroundInfos);
			backgroundInfomc = (String) backgroundInfoMap.get("jcjgmc");
		}
		//possible
		String possible_jcjgmc = "";//疑似物种 检测结果名称
		if (possibles != null && possibles.size() > 0) {
			// 检验结果 0：阴性 1：阳性 2：无
			s_jyjg = "1";
			//检测结果 存储possible的病原体名称
			possibleMap = DealSjwzxxWithSjwzxxs("2",possibles);
			possible_jcjgmc = (String) possibleMap.get("jcjgmc");
		}
		//pathogen
		String pathogen_jcjgmc = "";//高关物种 检测结果名称
		if (pathogens != null && pathogens.size() > 0) {
			// 检验结果 0：阴性 1：阳性 2：无
			s_jyjg = "1";
			//检测结果 存储pathogen的病原体名称
			pathogenMap = DealSjwzxxWithSjwzxxs("1",pathogens);
			pathogen_jcjgmc = (String) pathogenMap.get("jcjgmc");
		}

		//拼接疑似结果名称，存入疑似结果名称
		if(StringUtil.isNotBlank(backgroundmc)) {
			backgroundmc = backgroundmc.substring(1);
		}
		t_sjxxDto.setYsjg(backgroundmc);

		//拼接commensal结果名称，存入commensal结果名称
		if(StringUtil.isNotBlank(commensalmc)) {
			commensalmc = commensalmc.substring(1);
		}
		t_sjxxDto.setDzjg(commensalmc);

		//拼接backgroundInfomc结果名称，存入backgroundInfomc结果名称
		if(StringUtil.isNotBlank(backgroundInfomc)) {
			backgroundInfomc = backgroundInfomc.substring(1);
		}

		//拼接检测结果以及注释
		String jcjgmc = "";//检测结果名称
		StringBuffer comment = new StringBuffer();//描述
		if (pathogensAndpossibles != null && pathogensAndpossibles.size() > 0){
			for (int i = 0; i < pathogensAndpossibles.size(); i++) {
				if(i >0) {
					comment.append("\r\n");
				}
				if (StringUtils.isNotBlank(pathogensAndpossibles.get(i).getWzid())) {
					jcjgmc = jcjgmc + "," + ("--".equals(pathogensAndpossibles.get(i).getWzzwm())||StringUtil.isBlank(pathogensAndpossibles.get(i).getWzid())?pathogensAndpossibles.get(i).getSm():pathogensAndpossibles.get(i).getWzzwm());
					comment.append("1."+(i + 1)+pathogensAndpossibles.get(i).getWzzwm()+":"+pathogensAndpossibles.get(i).getWzzs()+"["+(i + 1)+"]。");
				} else {
					jcjgmc = jcjgmc + "," + (pathogensAndpossibles.get(i).getSzwm());
					comment.append("1."+(i+1)+pathogensAndpossibles.get(i).getSzwm()+":"+pathogensAndpossibles.get(i).getWzzs()+"["+(i + 1)+"]。");
				}
			}
		}
		if(StringUtil.isNotBlank(jcjgmc)) {
			jcjgmc = jcjgmc.substring(1);
		}
		t_sjxxDto.setJcjg(jcjgmc!=null && jcjgmc.length()>256?jcjgmc.substring(0,256):jcjgmc);
		if (sjwzxxDtos != null && sjwzxxDtos.size() > 0) {
			//组装数据
			boolean result = sjwzxxService.insertBysjwzxxDtos(sjwzxxDtos);

			if (!result){
				throw new BusinessException("","送检物种信息新增失败！");
			}
			//rabbit同步
			Map<String,Object> rabbitmap= new HashMap<>();
			String jsonObject = JSON.toJSON(sjwzxxDtos).toString();
			rabbitmap.put("sjwzxxDtos", jsonObject);
			rabbitmap.put("sjid",t_sjxxDto.getSjid());
			rabbitmap.put("jclx", inspinfo.getDetection_type());
			// 同步信息至微信服务器
			if (StringUtil.isNotBlank(menuurl))
				amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.RESULT_INSPECTION.getCode(), JSONObject.toJSONString(rabbitmap));
		}

		map.put("pathogenDtolist", pathogensAndpossibles);

		map.put("possibleList", possibles);
		map.put("pathogenList", pathogens);
		map.put("backgroundList", backgrounds);
		map.put("backgroundInfoList", backgroundInfos);
		map.put("commensalList", commensals);
		map.put("list",listMap.get("list"));

		map.put("jyjg", s_jyjg);
		map.put("jcjgmc", jcjgmc);
		map.put("backgroundmc", backgroundmc);
		map.put("backgroundInfomc", backgroundInfomc);
		map.put("commensalmc", commensalmc);
		map.put("pathogensAndpossiblescomment", comment.toString());
		map.put("backgroundcomment", backgroundMap!=null?backgroundMap.get("comment"):"");
		map.put("commensalcomment", commensalMap!=null?commensalMap.get("comment"):"");
		map.put("backgroundInfocomment", backgroundInfoMap!=null?backgroundInfoMap.get("comment"):"");

		// pathogen_comment possible_comment
		inspinfo.setPathogen_comment(pathogenMap==null?"": (String) pathogenMap.get("comment"));
		inspinfo.setPathogen_names(pathogen_jcjgmc);
		inspinfo.setPossible_comment(possibleMap==null?"": (String) possibleMap.get("comment"));
		inspinfo.setPossible_names(possible_jcjgmc);
		
		otherMap.put("jyjg", s_jyjg);
		otherMap.put("jcjgmc", jcjgmc);
		otherMap.put("backgroundmc", backgroundmc);
		otherMap.put("backgroundInfomc", backgroundInfomc);
		otherMap.put("commensalmc", commensalmc);
		otherMap.put("backgroundInfomc", backgroundInfomc);
		otherMap.put("comment", comment.toString());
		
		resultMap.put("possibleMap", possibleMap);
		resultMap.put("pathogenMap", pathogenMap);
		resultMap.put("backgroundMap", backgroundMap);
		resultMap.put("commensalMap", commensalMap);
		resultMap.put("backgroundInfoMap", backgroundInfoMap);
		resultMap.put("otherMap", otherMap);
		return resultMap;
	}

	/**
	 * 处理送检结果里的物种信息，需要把所有的结果都先整理到list里，然后在SQL里一次性进行 新增和根据时间来判断修改
	 * @param wzxxs 生信传递的物种信息
	 * @return 返回物种集合信息
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private Map<String,Object> DealSjwzxxWithSjwzxxs(String index,List<SjwzxxDto> wzxxs) {
		String jcjgmc = "";//检测结果名称
		StringBuffer comment = new StringBuffer();//描述
		List<SjwzxxDto> wxzzList = new ArrayList<>();
		Map<String,Object> resultMap = new HashMap<>();

		for (int i = 0; i < wzxxs.size(); i++) {
			if(i >1 &&i!=wzxxs.size()+1) {
				comment.append("\r\n");
			}
			if (StringUtils.isNotBlank(wzxxs.get(i).getWzid())) {
				jcjgmc = jcjgmc + "," + ("--".equals(wzxxs.get(i).getWzzwm())||StringUtil.isBlank(wzxxs.get(i).getWzid())?wzxxs.get(i).getSm():wzxxs.get(i).getWzzwm());
				comment.append(index + "."+(i + 1)+wzxxs.get(i).getWzzwm()+":"+wzxxs.get(i).getWzzs()+"["+(i + 1)+"]。");
				wxzzList.add(wzxxs.get(i));
			}else {
				jcjgmc = jcjgmc + "," + (wzxxs.get(i).getSzwm());
				comment.append(index +"."+(i+1)+wzxxs.get(i).getSzwm()+":"+wzxxs.get(i).getWzzs()+"["+(i + 1)+"]。");
				wzxxs.get(i).setYyxh(String.valueOf(i + 1));
				wxzzList.add(wzxxs.get(i));
			}
		}
		resultMap.put("jcjgmc", jcjgmc);
		resultMap.put("comment", comment.toString());
		resultMap.put("wxzzList", wxzzList);
		return resultMap;
	}
	
	/**
	 * 保存报告说明信息到数据库
	 * @param t_sjxxDto
	 * @param inspinfo
	 * @param speciesMap
	 * @throws BusinessException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void saveSjbgsm(SjxxDto t_sjxxDto, WeChatInspectionReportModel inspinfo, Map<String, Map<String, Object>> speciesMap, Map<String,Object> map, JkdymxDto jkdymxDto)

			throws BusinessException
	{
		Map<String, Object> otherMap = speciesMap.get("otherMap");
		Map<String, Object> backgroundMap = speciesMap.get("backgroundMap");
		Map<String, Object> commensalMap = speciesMap.get("commensalMap");
		String bgrqgxbj=String.valueOf(map.get("bgrq_sjbgsm"));
		// 保存送检报告说明
		SjbgsmDto sjbgsmDto = new SjbgsmDto();
		sjbgsmDto.setSjid(t_sjxxDto.getSjid());
		sjbgsmDto.setGgzdsm((String)otherMap.get("comment")!=null && ((String) otherMap.get("comment")).length()>2048?(String)((String) otherMap.get("comment")).substring(0,2048):(String)otherMap.get("comment"));
		sjbgsmDto.setGgzdzb((String)otherMap.get("jcjgmc")!=null && ((String) otherMap.get("jcjgmc")).length()>2048 ?(String)((String) otherMap.get("jcjgmc")).substring(0,2048):(String)otherMap.get("jcjgmc"));
		sjbgsmDto.setYssm(backgroundMap==null?"":(String)backgroundMap.get("comment"));
		sjbgsmDto.setYszb((String) otherMap.get("backgroundmc"));
		//定值指标、说明
		sjbgsmDto.setDzsm(commensalMap==null?"":(String)commensalMap.get("comment"));
		sjbgsmDto.setDzzb((String) otherMap.get("commensalmc"));
		sjbgsmDto.setCkwx(inspinfo.getRefs()!=null && inspinfo.getRefs().length()>8000?inspinfo.getRefs().substring(0,8000):inspinfo.getRefs());
		sjbgsmDto.setJclx(inspinfo.getDetection_type());
		sjbgsmDto.setJczlx(inspinfo.getDetection_subtype());
		sjbgsmDto.setRyzs(inspinfo.getHost_index());
		sjbgsmDto.setRyzsbfb(inspinfo.getHost_percentile());
		sjbgsmDto.setRyzswz(inspinfo.getHost_position());
		sjbgsmDto.setBgrq("1".equals(bgrqgxbj)?DateUtils.getCustomFomratCurrentDate(null):null);
		sjbgsmDto.setWkbh(inspinfo.getSample_id());
		sjbgsmDto.setSpikerpm(inspinfo.getHost_percentile());//增加阈值的保存
		sjbgsmDto.setSprpm(inspinfo.getSpike_rpm());
		sjbgsmDto.setGc(inspinfo.getGc());
		sjbgsmDto.setZxls(inspinfo.getTotal_reads());
		sjbgsmDto.setWswjcxls(inspinfo.getMicrobial_reads());
		sjbgsmDto.setSzxlbfb(inspinfo.getHomo_percentage());
		sjbgsmDto.setYxxls(inspinfo.getRaw_reads());
		sjbgsmDto.setJyjg((String) otherMap.get("jyjg"));
		//同步设置sjxx的检验结果
		t_sjxxDto.setJyjg((String) otherMap.get("jyjg"));
		t_sjxxDto.setDzjg((String) otherMap.get("commensalmc"));
		//审核人员
		sjbgsmDto.setShry(StringUtil.isNotBlank(inspinfo.getShry())?inspinfo.getShry():"");
		//验证人员
		sjbgsmDto.setJyry(StringUtil.isNotBlank(inspinfo.getJyry())? inspinfo.getJyry():"");

		List<SjbgsmDto> list = sjbgsmService.selectBySjbgsmDto(sjbgsmDto);
		if (CollectionUtils.isNotEmpty(list) && StringUtil.isNotBlank(list.get(0).getScbgrq())){
			sjbgsmDto.setScbgrq(list.get(0).getScbgrq());
		}else{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			sjbgsmDto.setScbgrq("1".equals(bgrqgxbj)?simpleDateFormat.format(date):null);
		}
		sjbgsmService.deleteBySjbgsmDto(sjbgsmDto);
		boolean result = sjbgsmService.insert(sjbgsmDto);
		if (!result){
			throw new BusinessException("","送检报告说明新增失败！");
		}
		map.put("sjbgsmDto", sjbgsmDto);
		// 同步信息至微信服务器
		if (StringUtil.isNotBlank(menuurl))
			amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.COMMENT_INSPECTION.getCode(), JSONObject.toJSONString(sjbgsmDto));
	}
	
	/**
	 * 保存送检耐药性信息
	 * @param t_sjxxDto
	 * @param inspinfo
	 * @throws BusinessException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void saveDrugResistance(SjxxDto t_sjxxDto,WeChatInspectionReportModel inspinfo,Map<String,Object> map)
			throws BusinessException
	{
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<SjnyxDto> all_nyxDtos=new ArrayList<>();
		//先处理TBT耐药信息，不可改变顺序
		int xh = 0;
		List<SjwzxxDto> tbt_sjnyxDtos=new ArrayList<>();
		List<NyjyzsglDto> tbt_nyjyzsglDtoList=new ArrayList<>();
		List<SjwzxxDto> tbt_drug_resistance = inspinfo.getTbt_drug_resistance_stat();//耐药性
//		List<SjwzxxDto> tbt_drug_resist_gene = inspinfo.getTbt_drug_resist_gene();//耐药基因注释
		if(!CollectionUtils.isEmpty(tbt_drug_resistance)){
			for(SjwzxxDto wznyxDto:tbt_drug_resistance){
				xh=xh+1;
				SjnyxDto sjnyxDto=new SjnyxDto();
				sjnyxDto.setJclx(inspinfo.getDetection_type());
				sjnyxDto.setJczlx(inspinfo.getDetection_subtype());
				sjnyxDto.setSjnyxid(StringUtil.generateUUID());
				sjnyxDto.setWkbh(inspinfo.getSample_id());
				sjnyxDto.setSjid(t_sjxxDto.getSjid());
				sjnyxDto.setXh(String.valueOf(xh));
				sjnyxDto.setJson(wznyxDto.getJson());
				sjnyxDto.setTbjy(wznyxDto.getTbjy());
				sjnyxDto.setNyx(wznyxDto.getNyx());
				sjnyxDto.setTbjg(wznyxDto.getTbjg());
				sjnyxDto.setTbsd(wznyxDto.getTbsd());
				sjnyxDto.setTbpl(wznyxDto.getTbpl());
				//修改报告显示，若有突变结果，突变基因内容为突变结果用_截图然后获取_前面的数据
				wznyxDto.setTbjy(StringUtil.isNotBlank(wznyxDto.getTbjg())?wznyxDto.getTbjg().split("_")[0]:wznyxDto.getTbjy());
				all_nyxDtos.add(sjnyxDto);
				tbt_sjnyxDtos.add(wznyxDto);
			}
		}
		map.put("TBTSjnyx_List",tbt_sjnyxDtos);
		map.put("Sjnyx_List",tbt_sjnyxDtos);
		map.put("nyx_List",all_nyxDtos);
//		map.put("TBT_NyjyzsList",tbt_nyjyzsglDtoList);未发现报告中需要结核非结核的耐药注释信息
		// 保存送检耐药性
		List<WeChatInspectionResistanceModel> drug_resistance = inspinfo.getDrug_resistance_stat();
		if (drug_resistance != null && drug_resistance.size() > 0) {
			List<SjnyxDto> sjnyxDtos = new ArrayList<>();
			List<SjwzxxDto> sjwzxxNyxs = new ArrayList<>();
			//耐药基因注释管理
			List<NyjyzsglDto>  nyjyzsglList = new ArrayList<>();
			for (int i = 0; i < drug_resistance.size(); i++) {
				if (StringUtils.isNotBlank(drug_resistance.get(i).getRelated_gene())) {
					String[] jys = drug_resistance.get(i).getRelated_gene().split(";");
					for (int j = 0; j < jys.length; j++) {
						xh = xh + 1;
						SjnyxDto sjnyxDto = new SjnyxDto();
						if(inspinfo.getDrug_resist_gene()!=null && inspinfo.getDrug_resist_gene().size()>0){
							for (int k = 0; k < inspinfo.getDrug_resist_gene().size(); k++) {
								if (inspinfo.getDrug_resist_gene().get(k).getName().equals(jys[j])) {
									sjnyxDto.setNyjyid(inspinfo.getDrug_resist_gene().get(k).getId());
								}
							}
						}
						SjwzxxDto sjwzxxDto = new SjwzxxDto();
						sjwzxxDto.setXls(drug_resistance.get(i).getReads());
						sjwzxxDto.setJyfx(drug_resistance.get(i).getGenes());
						sjwzxxDto.setJl(drug_resistance.get(i).getMain_mechanism());
						sjwzxxDto.setYp(drug_resistance.get(i).getDrug_class());
						sjwzxxDto.setQyz(drug_resistance.get(i).getOrigin_species());
						sjwzxxDto.setJy(jys[j]);
						sjwzxxDto.setReport_taxids(drug_resistance.get(i).getReport_taxids());
						sjwzxxDto.setReport_taxnames(drug_resistance.get(i).getReport_taxnames());
						sjwzxxNyxs.add(sjwzxxDto);
						sjnyxDto.setSjnyxid(StringUtil.generateUUID());
						sjnyxDto.setSjid(t_sjxxDto.getSjid());
						sjnyxDto.setYp(drug_resistance.get(i).getDrug_class());
						sjnyxDto.setDs(drug_resistance.get(i).getGene_count());
						sjnyxDto.setJy(jys[j]);
						sjnyxDto.setJl(drug_resistance.get(i).getMain_mechanism());
						sjnyxDto.setXgwz(drug_resistance.get(i).getRef_species());
						sjnyxDto.setXh(String.valueOf(xh));
						sjnyxDto.setJclx(inspinfo.getDetection_type());
						sjnyxDto.setJczlx(inspinfo.getDetection_subtype());
						sjnyxDto.setWkbh(inspinfo.getSample_id());
						sjnyxDto.setYsyp(drug_resistance.get(i).getDrug_name());
						sjnyxDto.setJyfx(drug_resistance.get(i).getGenes());
						sjnyxDto.setXls(drug_resistance.get(i).getReads());
						sjnyxDto.setQyz(drug_resistance.get(i).getOrigin_species());
						sjnyxDto.setReport_taxids(!CollectionUtils.isEmpty(drug_resistance.get(i).getReport_taxids())?String.join(",",drug_resistance.get(i).getReport_taxids()):"");
						sjnyxDto.setReport_taxnames(drug_resistance.get(i).getReport_taxnames());
						sjnyxDtos.add(sjnyxDto);
						all_nyxDtos.add(sjnyxDto);
					}
					//处理耐药基因注释和耐药基本保持顺序一致
					// 更新耐药基因注释
					List<WechatInspectionResistModel> drug_resist_gene = inspinfo.getDrug_resist_gene();
					if (drug_resist_gene != null && drug_resist_gene.size() > 0) {
						for (int j = 0; j < drug_resist_gene.size(); j++) {
							if(drug_resistance.get(i).getRelated_gene().equals(drug_resist_gene.get(j).getName())){//匹配基本名称相同
								NyjyzsglDto nyjyzsglDto = new NyjyzsglDto();
								nyjyzsglDto.setJyjzmc(drug_resist_gene.get(j).getName());
								nyjyzsglDto.setZsnr(drug_resist_gene.get(j).getComment());
								nyjyzsglList.add(nyjyzsglDto);
								try {
									Date last_update = dateformat.parse(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
											.format(new Date(Long.parseLong(drug_resist_gene.get(j).getLast_update()) * 1000)));
									String update_date = dateformat.format(last_update);
									drug_resist_gene.get(j).setLast_update(update_date);
								} catch (Exception e) {
									log.error(e.toString());
								}
								break;
							}
						}
					}
				}
			}
			map.put("nyjyzsglList", nyjyzsglList);
			sjnyxService.deleteBySjnyxDto(all_nyxDtos.get(0));
			boolean result = sjnyxService.insertBysjnyxDtos(all_nyxDtos);
			if (!result){
				throw new BusinessException("","送检耐药性新增失败！");
			}
			map.put("sjnyxlist",sjnyxDtos);
			map.put("sjwzxxNyxs",sjwzxxNyxs);			
			Map<String,Object> rabbitmap= new HashMap<>();
			String jsonObject = JSON.toJSON(sjnyxDtos).toString();
			rabbitmap.put("sjnyxDtos", jsonObject);
			rabbitmap.put("sjid",t_sjxxDto.getSjid());
			rabbitmap.put("jclx", inspinfo.getDetection_type());
			if (StringUtil.isNotBlank(menuurl)) // 同步信息至微信服务器
				amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.RESISTANCE_INSPECTION.getCode(), JSONObject.toJSONString(rabbitmap));
		}else {
			//若耐药性数据为空则删除耐药性数据，
			SjnyxDto sjnyxDto=new SjnyxDto();
			sjnyxDto.setJclx(inspinfo.getDetection_type());
			sjnyxDto.setJczlx(inspinfo.getDetection_subtype());
			sjnyxDto.setSjid(t_sjxxDto.getSjid());
			sjnyxService.deleteBySjnyxDto(sjnyxDto);
		}
	}
	
	/**
	 * 更新并保存参考文献信息
	 * @param inspinfo
	 * @param user
	 * @throws BusinessException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@SuppressWarnings("unchecked")
	private void saveRefs(WeChatInspectionReportModel inspinfo, Map<String,Map<String,Object>> speciesMap, User user, Map<String, Object> map) throws BusinessException
	{
		// 更新参考文献信息(根据wzid和标本类型删除原有文献，新增新文献)
		try {
			List<WechatReferencesModel> papers = inspinfo.getPapers();
			StringBuffer refs = new StringBuffer("");
			List<WechatReferencesModel> refsList = new ArrayList<>();//文献List
			if (papers != null && papers.size() > 0) {
				List<SjwzxxDto> sjwzCkwxList = new ArrayList<>();
				sjwzCkwxList.addAll((List<SjwzxxDto>) map.get("pathogenDtolist"));
				sjwzCkwxList.addAll(WzxxSort((List<SjwzxxDto>)map.get("backgroundList")));
				for(int x = 0;x < sjwzCkwxList.size();x++)
				{
					if(x>0)
						refs.append("{br}{\\n}");
					else
						refs.append("\n");
					for (int i = 0; i < papers.size(); i++) {
						if(StringUtil.isBlank(papers.get(i).getId()))
							continue;
						if(papers.get(i).getId().equals(sjwzCkwxList.get(x).getWxid())) {
							papers.get(i).setYyxh(String.valueOf(x + 1));
							refsList.add(papers.get(i));
							refs.append("["+papers.get(i).getYyxh()+"]"+ papers.get(i).getAuthor()+ papers.get(i).getTitle()+ papers.get(i).getJournal());
							break;
						}
					}
				}
				inspinfo.setPapers(refsList);
				inspinfo.setRefs(refs.toString());
			}
		} catch (Exception e) {
			throw new BusinessException("","参考文献信息有误!" + e.getMessage());
		}
	}
	
	/**
	 * 根据rpm的信息，查询阈值情况，如果为高人源的，则发起复测处理。如果为量仅一次，则通知客户
	 * @param t_sjxxDto
	 * @param inspinfo
	 * @param t_jcsjDto
	 * @param user
	 * @throws BusinessException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	private void saveRecheckByRpm(SjxxDto t_sjxxDto,WeChatInspectionReportModel inspinfo,JcsjDto t_jcsjDto,User user) throws BusinessException{
		/**如果接收的信息里除了背景没有其他信息， 并且根据 igams_spikerpm 获取相应标本类型和检测项目的阈值，
		 * 人源指数 低于该阈值。 检测项目为PDC的，则调用发送方法，发送高人源通知消息。 如果检测项目为Q的，
		 * 并且标本类型为肺泡灌洗液，脑脊液（根据标本类型参数代码来）的，则发送加做去人源信息，同时自动提交复检申请。
		 * 提交的区分应该根据指定扩展参数来获取。 如果检测项目为Q，并且标本类型为其他的，则发送高人源通知消息。
		 * （更改:PDC情况下D,C下脑脊液，肺泡灌洗液，痰液，血要发送高人源通知。R不做处理
		 Q情况下D,C脑脊液，肺泡灌洗液，痰液去人源通知，血发高人源通知，除血外做加测。R不做处理
		 ）
		 * 只有结果为阴性的时候，，才判断是否为高人源，以及是否加测去人源
		 * 姚嘉伟   2020/08/28  and  start **/
		boolean sfljyc= t_sjxxDto.getYbztmc() != null && t_sjxxDto.getYbztmc().contains("量仅一次");//是否量仅一次
//		SjybztDto sjybztDto=new SjybztDto();
//		sjybztDto.setSjid(t_sjxxDto.getSjid());
//		sjybztDto.setYbztCskz1("S");
//		List<SjybztDto> ybztlist=sjybztService.getDtoList(sjybztDto);
		//if(ybztlist!=null && ybztlist.size()>0) {
        //考虑阴性的报告里，如果人源指数高于80% ，则自动提交去人源。
		//现在考虑去除阴性限制，所有的报告的人源指数高于80%，则提交 2021-12-20
		//if(inspinfo.getPathogen().size()==0 && inspinfo.getPossible().size()==0) {
		//若Auto_rem_off为true则不提交去人源，否则则正常判断若人源指数高于80%，则提交去人源  2022-1-7  yao
		if(!"true".equalsIgnoreCase(inspinfo.getAuto_rem_off())  && StringUtil.isNotBlank(t_sjxxDto.getJc_cskz3()) && !t_sjxxDto.getJc_cskz3().contains("IMP_REPORT_SEQ_") ){
			if(StringUtils.isNotBlank(inspinfo.getHost_percentile())) {
				dealQrysq(inspinfo,t_sjxxDto,t_jcsjDto,sfljyc,user);
			}
		}
	}
	
	/**
	 * 生物部发送报告生成word文档
	 * @param inspinfo
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	@SuppressWarnings("unused")
	public boolean generateReportTwo(WeChatInspectionReportModel inspinfo,Map<String,Object> resultmap) throws BusinessException {
		log.error("组装报告信息-----开始");
		String ybbh=inspinfo.getYbbh();
		String jclx=inspinfo.getDetection_type();
		String jczlx=inspinfo.getDetection_subtype();
		String sfqry=inspinfo.getSfqry();
		String jclxcskz1=inspinfo.getDetection_cskz1();
		DBEncrypt dbEncrypt=new DBEncrypt();
		//查询基本送检信息
		SjxxDto sjxxDto_t=(SjxxDto)resultmap.get("sjxxDto");
		sjxxDto_t.setJclx(jclx);
		sjxxDto_t.setJczlx(jczlx);

		Map<String,Object> map=sjxxService.getMapBySjid(sjxxDto_t.getSjid(),jclx,jczlx);
		if (null == map.get("bzfwjlj")){
			throw new BusinessException("","当前合作伙伴的检测项目没有设置报告模板或者没有设置相应文件！");
		}
		if("onco".equals(inspinfo.getReport_type())){
			List<JcsjDto> jc_jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
			for (JcsjDto jc_jcxm : jc_jcxms) {
				if (jc_jcxm.getCsid().equals(jclx)){
					map.put("mcpjdqbg_cskz1",jc_jcxm.getCskz1());
				}
			}
		}else{
			map.put("mcpjdqbg_cskz1",inspinfo.getDetection_cskz1());
		}
		map.put("bgjcxmmc",resultmap.get("bgjcxmmc"));
		map.put("jcxmdm",resultmap.get("jcxmdm"));
		map.put("has_commensal",resultmap.get("has_commensal"));
		map.put("Fj_List",resultmap.get("Fj_List"));//TBT质控图
		map.put("yjxmmc",resultmap.get("yjxmmc"));
		//推荐用药信息
		List<Map<String, Object>> speciesDrug_List = new ArrayList<>();
		Map<String, List<Map<String, Object>>> speciesDrugs = inspinfo.getSpecies_drugs();
		if (speciesDrugs != null && speciesDrugs.keySet().size() > 0){
			for (String taxid : speciesDrugs.keySet()) {
				List<Map<String, Object>> mapList = speciesDrugs.get(taxid);
				mapList.forEach( speciesDrug ->  speciesDrug.put("taxid",taxid));
				speciesDrug_List.addAll(mapList);
			}
		}
		map.put("species_drug_MapList",speciesDrug_List);
		//处理wbsjxx，针对sjxx合并的情况，出报告时标本编号需要进行区分
		WbsjxxDto wbsjxxDto=new WbsjxxDto();
		wbsjxxDto.setSjid(String.valueOf(map.get("sjid")));
		wbsjxxDto.setJcxmcskz1(String.valueOf(map.get("jcxmcs1")));
		wbsjxxDto.setJcxmcskz3(String.valueOf(map.get("jcxm_kzcs3")));
		List<WbsjxxDto> wbsjList=wbsjxxService.getSimilarDtoList(wbsjxxDto);
		if(!CollectionUtils.isEmpty(wbsjList))
			dealYbbhByWbsjxx(map,wbsjList);
		//检验人员审核人员电子签名
		if (StringUtil.isNotBlank(inspinfo.getShry()) && StringUtil.isNotBlank(inspinfo.getJyry())){
			User shryDto = commonService.getDtoByYhm(inspinfo.getShry());
			User jyryDto = commonService.getDtoByYhm(inspinfo.getJyry());
			map.put("shryzsxm",shryDto.getZsxm());
			map.put("jyryzsxm",jyryDto.getZsxm());
			map.put("shsj",resultmap.get("bgsj"));

			FjcfbDto jyry = new FjcfbDto();
			jyry.setYwlx(BusTypeEnum.IMP_SIGNATURE.getCode());
			jyry.setYwid(inspinfo.getJyry());
			String[] imageFilePath = new String[2];
			DBEncrypt p = new DBEncrypt();
			//查询附件文件路径
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(jyry);
			if (fjcfbDtos == null || fjcfbDtos.size() <=0){
				log.error("检验人缺少电子签名！");
			}else {
				//检验人员
				imageFilePath[0] = p.dCode(fjcfbDtos.get(0).getWjlj());
				map.put("jyr_Pic",imageFilePath[0]);
				log.error(" 检验人电子签名文件名---------> "+fjcfbDtos.get(0).getWjm());
			}
			
			//审核人员电子签名
			FjcfbDto shry = new FjcfbDto();
			shry.setYwlx(BusTypeEnum.IMP_SIGNATURE.getCode());
			shry.setYwid(inspinfo.getShry());
			//查询附件文件路径
			List<FjcfbDto> fjcfbDtoList = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(shry);
			if (fjcfbDtoList == null || fjcfbDtoList.size() <=0){
				log.error("审核人缺少电子签名！");
			}else {
				//审核人员
				imageFilePath[1] = p.dCode(fjcfbDtoList.get(0).getWjlj());				
				map.put("shr_Pic",imageFilePath[1]);
				log.error(" 审核人电子签名文件名---------> "+fjcfbDtoList.get(0).getWjm());	
			}
		}
		String jyjg = "";
		//查询基本送检信息
		SjbgsmDto SjbgsmDto_t=(SjbgsmDto)resultmap.get("sjbgsmDto");
		
		if(SjbgsmDto_t!=null ){
			if ("1".equals(SjbgsmDto_t.getJyjg())){
				jyjg = "阳性";
			}else if ("0".equals(SjbgsmDto_t.getJyjg())){
				jyjg = "阴性";
			}else if ("2".equals(SjbgsmDto_t.getJyjg())){
				jyjg = "疑似";
			}
		}
		map.put("taxFlg_List",resultmap.get("taxFlg_List"));
		map.put("TBTSjnyx_List",resultmap.get("TBTSjnyx_List"));
		map.put("specialTaxFlg_List",resultmap.get("specialTaxFlg_List"));
		map.put("backgroundTaxFlg_List",resultmap.get("backgroundTaxFlg_List"));
		map.put("allTaxFlg_List",resultmap.get("allTaxFlg_List"));
		map.put("Pathogen_List",resultmap.get("pathogenDtolist"));
		map.put("Background_List",resultmap.get("backgroundList"));
		map.put("BackgroundInfo_List",resultmap.get("backgroundInfoList"));
		map.put("Commensal_List",resultmap.get("commensalList"));
		map.put("lib_concentration",resultmap.get("lib_concentration"));
		map.put("nonhuman_reads",resultmap.get("nonhuman_reads"));
		map.put("nuc_concentration",resultmap.get("nuc_concentration"));
		map.put("wxwxlzb",resultmap.get("wxwxlzb"));
		map.put("jyjg",jyjg);
		map.put("jclx", jclxcskz1);
		map.put("sfqry",sfqry);
		map.put("jclxid",jclx);
		map.put("jczlxid",jczlx);
		//送检报告说明
		descriptionReport(sjxxDto_t,map,resultmap);
		
		//染色体曼哈顿图 截图导入
		screenshotImport(sjxxDto_t,map);
		
		//标本编号
		map.put("sample_id",inspinfo.getSample_id());
		map.put("rem",inspinfo.getSample_id().contains("-REM")?"是":"否");
		//去人源图片
		map.put("cursor",releaseFilePath+BusTypeEnum.IMP_TEMEPLATE_IMAGES.getCode()+"/cursor/cursor.png");
		//文件业务类型
		JcsjDto jcsjDto=(JcsjDto)resultmap.get("jcsjDto");
		if(jcsjDto==null) {
			log.error("组装报告信息-----检测项目不存在!");
			throw new BusinessException("","组装报告信息-----检测项目不存在!");
		}
		//生成报告的业务类型（WORD）
		String ywlxToWord=jcsjDto.getCskz3()+"_"+jcsjDto.getCskz1()+"_"+"WORD";
		//先声这边回传实验结果
		if("hospitalXiansheng".equals(sjxxDto_t.getLrry())&&!ywlxToWord.contains("PCR")){
			boolean isSuccess = xianshengSaveExperimentResult(sjxxDto_t);
			if(!isSuccess){
				log.error("先声返回实验结果失败，需要走重新生成流程！");
				return isSuccess;
			}
		}
		//生成报告的业务类型（PDF）
		String ywlxToPDF=jcsjDto.getCskz3()+"_"+jcsjDto.getCskz1();
		map.put("ywlx",ywlxToWord);
		map.put("ywlxToPDF",ywlxToPDF); 
		
		//结果综述（新模板）pathogens_new 
		//因为参考文献需要与物种信息排序想对应，所以在这里组装参考文献
		//pathogens-结果综述   
		//references：参考文献
		//column:结果综述的条数
		Map<String, Object> pathogensMap=sjwzxxService.getPathogensMapNew(sjxxDto_t.getSjid(),jclx, inspinfo.getPapers(), resultmap);
		map.put("pathogens_new",pathogensMap.get("pathogens"));
		map.put("onlypathogens",pathogensMap.get("onlypathogens"));
		map.put("onlypossible",pathogensMap.get("onlypossible"));
		map.put("onlycommensal",pathogensMap.get("onlycommensal"));
		map.put("pathogens_background",pathogensMap.get("pathogens_background"));
		map.put("pathogens_commensal",pathogensMap.get("pathogens_commensal"));
		map.put("pathogens_hospital",pathogensMap.get("pathogens_hospital"));
		map.put("commensal_hospital",pathogensMap.get("commensal_hospital"));
		map.put("pathogens_backgroundInfo",pathogensMap.get("pathogens_backgroundInfo"));

		//结果综述(旧模板)pathogens--高关  possible--疑似
		Map<String,String> map1=sjwzxxService.getJcjgForOldTemplateNew(sjxxDto_t.getSjid(),jclx, resultmap);
		map.put("pathogens",map1.get("pathogens"));
		map.put("possible",map1.get("possible"));

		//参考文献(旧模板【区分高关低关】)refs
		String references_default=sjwzxxService.getReferences_defaultNew(sjxxDto_t.getSjid(),jclx,inspinfo.getPapers(),resultmap);
		map.put("refs_default",StringUtil.isNotBlank(references_default)?references_default:"无");
		//log.error("报告的结果综述,参考文献处理完成!");
		//DNA病毒Viruses_D_List   RNA病毒Viruses_R_List  旧模板（不区分 细菌 分枝杆菌 支原体） 细菌Bacteria_List
		//分支杆菌Mycobacteria_List   支原体/衣原体/立克次体(Mycoplasma/Chlamydia/Rickettsia) MCR
		//真菌Fungi_List   寄生虫Parasite_List  疑似人体共生微生物Background_List
		generateList(sjxxDto_t,map,resultmap);
		
		//log.error("报告的结果列表处理完成!");
		//送检耐药性信息Gene_List
		Object ob_sjwzxxNyxs = resultmap.get("sjwzxxNyxs");
		if(ob_sjwzxxNyxs!=null) {
			@SuppressWarnings("unchecked")
			List<SjwzxxDto> Gene_List=(List<SjwzxxDto>)ob_sjwzxxNyxs;
			if(Gene_List.size()>0) {
				String nyjy_jys="";
				for (SjwzxxDto sjwzxxDto : Gene_List) {
					if(StringUtil.isNotBlank(sjwzxxDto.getJyfx()))
						sjwzxxDto.setJyfx("\n\n"+sjwzxxDto.getJyfx().replaceAll(";","{br}{\\n}"));
					if(StringUtil.isNotBlank(sjwzxxDto.getXls())) {
						//总序列数
						BigDecimal zxls = new BigDecimal("0");
						String[] split = sjwzxxDto.getXls().split(";");
						for (String s : split) {
							if(StringUtil.isNotBlank(s)) {
								zxls = zxls.add(new BigDecimal(s));
							}
						}
						sjwzxxDto.setZxls(zxls.toString());
						sjwzxxDto.setXls( sjwzxxDto.getXls().replaceAll(";", "{br}{\\n}"));
					}
					nyjy_jys=nyjy_jys+"，"+sjwzxxDto.getJy();
				}
				if(nyjy_jys.length()>0){
					nyjy_jys=nyjy_jys.substring(1);
				}
				map.put("nyjy_Count",String.valueOf(Gene_List.size()));
				map.put("nyjy_jys",nyjy_jys);
			}
			map.put("Gene_List", Gene_List);
		}else{
			map.put("nyjy_Count","0");
		}
		
		//耐药基因检测结果说明results_break
		Object ob_nyjyzsglList = resultmap.get("nyjyzsglList");
		map.put("nyjyzsglList",resultmap.get("nyjyzsglList"));
		if(ob_nyjyzsglList!=null) {
			@SuppressWarnings("unchecked")
			List<NyjyzsglDto> list = (List<NyjyzsglDto>)ob_nyjyzsglList;
			StringBuffer sb=new StringBuffer();
			if(list.size()>0) {
				for (int i = 1; i <list.size()+1; i++) {
					if(i>1&& i!=list.size()+1) 
						sb.append("{br}{\\n}");
					else
						sb.append("\n");
					sb.append("{br}{b+i}"+i+"."+list.get(i-1).getJyjzmc()+":{br}"+list.get(i-1).getZsnr());
				}
			}
			String results_break =sb.toString();
			if(StringUtil.isNotBlank(results_break)) {
				map.put("results",results_break);
			}else {
				map.put("results","无");
			}
		}else {
			map.put("results","无");
		}

		//log.error("报告的耐药基因处理完成!");
		//毒力因子检测结果注释
		Object ob_vfsList = resultmap.get("vfsList");
		if(ob_vfsList != null) {
			@SuppressWarnings("unchecked")
			List<WechatVirulenceFactorStatModel> vfsList = (List<WechatVirulenceFactorStatModel>) ob_vfsList;
			if (vfsList.size()>0){
				List<String> dlyzNames = new ArrayList<>();
				DlyzzsDto dlyzzsDto = new DlyzzsDto();
				for (WechatVirulenceFactorStatModel vfs : vfsList) {
					if(vfs != null && StringUtil.isNotBlank(vfs.getVf_name()))
						dlyzNames.add(vfs.getVf_name());
				}
				String virulenceFactorResults = "";
				if (!CollectionUtils.isEmpty(dlyzNames)){
					dlyzzsDto.setNames(dlyzNames);
					List<DlyzzsDto> dlyzzsDtoList = dlyzzsService.queryByNames(dlyzzsDto);
					StringBuffer dlsb=new StringBuffer();
					if(dlyzzsDtoList!=null && dlyzzsDtoList.size()>0) {
						for (int i = 1; i <dlyzzsDtoList.size()+1; i++) {
							if(i>1&& i!=dlyzzsDtoList.size()+1)
								dlsb.append("{br}{\\n}");
							dlsb.append("{br}{b+i}"+i+"."+(StringUtil.isNotBlank(dlyzzsDtoList.get(i-1).getName())?dlyzzsDtoList.get(i-1).getName():"")+":{br}"+(StringUtil.isNotBlank(dlyzzsDtoList.get(i-1).getComment())?dlyzzsDtoList.get(i-1).getComment():""));
						}
					}
					virulenceFactorResults = dlsb.toString();
				}

				if(StringUtil.isNotBlank(virulenceFactorResults)) {
					map.put("virulenceFactorResults",virulenceFactorResults);
				}else {
					map.put("virulenceFactorResults","无");
				}
			}
		}else {
			map.put("virulenceFactorResults","无");
		}
		//log.error("报告的毒力因子检测结果处理完成!");
		//临床诊疗指南guide
		Object ob_guidelines = resultmap.get("guidelines");
		map.put("guide_List",ob_guidelines);
		if(ob_guidelines != null) {
			@SuppressWarnings("unchecked")
			List<WechatClinicalGuideModel> guidelines = (List<WechatClinicalGuideModel>)ob_guidelines;
			StringBuffer sb_t=new StringBuffer();
			if(guidelines!=null && guidelines.size()>0) {
				for (int i = 1; i < guidelines.size()+1; i++) {
					if(i>1)
						sb_t.append("{br}{\\n}");
					sb_t.append("["+i+"]"+guidelines.get(i-1).getDetail());
				}
			}
			String guide= sb_t.toString();
			if(StringUtil.isNotBlank(guide)) {
				map.put("guide",guide);
			}else {
				map.put("guide","无");
			}
		}else {
			map.put("guide","无");
		}
		
		//微生物检测结果说明(新模板)
		Map<String,Object> pathogen_comment=sjwzxxService.getPathogen_commentToStringNew(sjxxDto_t,resultmap);
		if(StringUtil.isNotBlank((String)pathogen_comment.get("pathogen_comment_new"))) {
			map.put("pathogen_comment_new",pathogen_comment.get("pathogen_comment_new"));
		}else {
			map.put("pathogen_comment_new","无");
		}
		if(StringUtil.isNotBlank((String)pathogen_comment.get("pathogen_comment_widthEnglish"))) {
			map.put("pathogen_comment_widthEnglish",pathogen_comment.get("pathogen_comment_widthEnglish"));
		}else {
			map.put("pathogen_comment_widthEnglish","无");
		}
		map.put("pathogen_comment_new_List",pathogen_comment.get("pathogen_comment_new_List"));
		map.put("pathogen_comment_List",pathogen_comment.get("pathogen_comment_list"));
		map.put("pathogen_comment_hospital",pathogen_comment.get("pathogen_comment_hospital"));
		
		//检测结果说明 pathogen_comment possible_comment
		Map<String,Object> map2 = sjwzxxService.getPathogenForOldTemplateNew(sjxxDto_t,resultmap);
		map.put("pathogen_comment_old", map2.get("pathogen_comment_old"));
		map.put("pathogen_comment_old_List", map2.get("pathogen_comment_old_List"));

		@SuppressWarnings("unchecked")
		List<SjwzxxDto> backgroundList = CollectionUtils.isEmpty((List<SjwzxxDto>)(map.get("Background_List")))?(List<SjwzxxDto>)(map.get("Background_List")):WzxxSort((List<SjwzxxDto>)(map.get("Background_List")));
		//疑似或人体共生微生物群结果解释 
		Map<String,Object> comment=getBackground_comment(backgroundList,Integer.parseInt(pathogensMap.get("column").toString()),null,null);
		map.remove("Background_List");
		map.put("Background_List",comment.get("sjwzxxList_background"));
		if(StringUtil.isNotBlank((String)comment.get("background_comment"))) {
			map.put("background_comment",comment.get("background_comment"));
		}else {
			map.put("background_comment","无");
		}
		map.put("background_comment_List",comment.get("background_comment_List"));
		map.put("background_comment_hospital",comment.get("background_comment_hospital"));
		@SuppressWarnings("unchecked")

		List<SjwzxxDto> backgroundInfoList =CollectionUtils.isEmpty((List<SjwzxxDto>)(map.get("BackgroundInfo_List")))?(List<SjwzxxDto>)(map.get("BackgroundInfo_List")):WzxxSort((List<SjwzxxDto>)(map.get("BackgroundInfo_List")));
		//疑似或人体共生微生物群结果解释
		Map<String,Object> backgrounInfoComment = getBackgroundInfo_comment(backgroundInfoList,Integer.parseInt(pathogensMap.get("column").toString()),null,null);
		map.remove("BackgroundInfo_List");
		map.put("BackgroundInfo_List",backgrounInfoComment.get("sjwzxxList_backgroundInfo"));
		if(StringUtil.isNotBlank((String)backgrounInfoComment.get("backgroundInfo_comment"))) {
			map.put("backgroundInfo_comment",backgrounInfoComment.get("backgroundInfo_comment"));
		}else {
			map.put("backgroundInfo_comment","");
		}
		map.put("backgroundInfo_comment_List",backgrounInfoComment.get("backgroundInfo_comment_List"));
		map.put("backgroundInfo_comment_hospital",backgrounInfoComment.get("backgroundInfo_comment_hospital"));
		List<SjwzxxDto> commensalList = WzxxSort((List<SjwzxxDto>)map.get("Commensal_List"));
		//疑似或人体共生微生物群结果解释
		Map<String,Object> commensalComment = getCommensal_comment(commensalList,Integer.parseInt(pathogensMap.get("column").toString())+(backgroundInfoList!=null?backgroundInfoList.size():0),null,null);
		map.remove("Commensal_List");
		map.put("Commensal_List",commensalComment.get("sjwzxxList_commensal"));
		if(StringUtil.isNotBlank((String)commensalComment.get("commensal_comment"))) {
			map.put("commensal_comment",commensalComment.get("commensal_comment"));
		}else {
			map.put("commensal_comment","");
		}
		map.put("commensal_comment_List",commensalComment.get("commensal_comment_List"));
		map.put("commensal_comment_hospital",commensalComment.get("commensal_comment_hospital"));
		//新模板参考文献
		if(StringUtil.isNotBlank(inspinfo.getRefs())) {
			map.put("refs_definition",inspinfo.getRefs());
		}else {
			map.put("refs_definition","无");
		}
		//新模板参考文献List
		if(inspinfo.getPapers()!=null && inspinfo.getPapers().size()>0) {
			map.put("refs_List",inspinfo.getPapers());
		}else {
			map.put("refs_List",null);
		}
		map.put("VirulenceFactorStat_List",resultmap.get("vfsList"));
		//审核备注
		if(StringUtil.isNotBlank(inspinfo.getAudit_comment())) {
			map.put("audit_comment",inspinfo.getAudit_comment());
		}else {
			map.put("audit_comment","无");
		}
		//报告模板信息
		reportTemplateInformation(inspinfo,map,sjxxDto_t);
		Map<String, Object> hashMap = new HashMap<>(map);
		if(null != map.get("sendFlag") && "1".equals(map.get("sendFlag").toString())) {
			hashMap.put("sendFlag",map.get("sendFlag"));
			if(map.get("jcdwcskz5")!=null && StringUtil.isNotBlank(map.get("jcdwcskz5").toString())) {
				try{
					Map<String, Object> cskzMap = JSON.parseObject(map.get("jcdwcskz5").toString(), new TypeReference<Map<String, Object>>(){});
					if(cskzMap!=null && cskzMap.get("sendmail")!=null && StringUtil.isNotBlank(cskzMap.get("sendmail").toString())){
						hashMap.put("sendmail",cskzMap.get("sendmail"));
					}
				}catch (Exception e){
					log.error("jcdwcskz5 json解析失败！");
				}
				map.remove("jcdwcskz5");
			}
			map.remove("sendFlag");
		}

		WordTemeplateThread thread=new WordTemeplateThread(map,fjcfbService,amqpTempl,DOC_OK,FTP_URL,sjxxService);
		thread.start();

		if (null != map.get("bzfwjlj2")){
			hashMap.put("bzfwjlj",map.get("bzfwjlj2"));
			hashMap.put("bzfwjm",map.get("bzfwjm2"));
			hashMap.put("bgmbcskz1",map.get("bgmb2cskz1"));
			hashMap.put("cskz2",map.get("bgmb2cskz2"));
			hashMap.put("ywlx","REPORT_"+map.get("ywlx"));
			hashMap.put("ywlxToPDF","REPORT_"+map.get("ywlxToPDF"));
			hashMap.put("first_report_accurate_time",map.get("first_report_accurate_time"));
			hashMap.put("bfwjbj","1");//备份文件标记
			//报告模板信息
			reportTemplateInformation(inspinfo,hashMap,sjxxDto_t);
			WordTemeplateThread thread_2=new WordTemeplateThread(hashMap,fjcfbService,amqpTempl,DOC_OK,FTP_URL,sjxxService);
			thread_2.start();
		}
		return true;
	}
	
	/**
	 * 送检报告说明
	 * @param 
	 * @return
	 */
	private void descriptionReport(SjxxDto sjxxDto,Map<String,Object> map,Map<String,Object> resultmap) {
		SjbgsmDto sjbgsmDto = (SjbgsmDto)resultmap.get("sjbgsmDto");
		if (sjbgsmDto!=null) {
			//分子核型结果
			map.put("fzhxjg", StringUtil.isNotBlank(sjbgsmDto.getFzhxjg())?sjbgsmDto.getFzhxjg():"");
			map.put("total_reads",sjbgsmDto.getZxls());
			map.put("raw_reads",sjbgsmDto.getYxxls());
			map.put("GC",StringUtil.isNotBlank(sjbgsmDto.getGc())?"　"+sjbgsmDto.getGc().replaceAll(";","{br}{\\n}"):"　");
			map.put("sjbgsm_cskz1",StringUtil.isNotBlank(sjbgsmDto.getCskz1())?"　"+sjbgsmDto.getCskz1().replaceAll(";","{br}{\\n}"):"");
			map.put("sjbgsm_cskz2",StringUtil.isNotBlank(sjbgsmDto.getCskz2())?"　"+sjbgsmDto.getCskz2().replaceAll(";","{br}{\\n}"):"　");
		}		
	}
	
	/**
	 * 染色体曼哈顿图 截图导入
	 * @param 
	 * @return
	 */
	private void screenshotImport(SjxxDto sjxxDto,Map<String,Object> map) {
		FjcfbDto rstmhdtDto = new FjcfbDto();
		DBEncrypt dbEncrypt=new DBEncrypt();
		rstmhdtDto.setYwid(sjxxDto.getSjid());
		List<String> ids = new ArrayList<>();
		ids.add(BusTypeEnum.IMP_SEQ_QUALITY_CHROMOSOME_DEAL.getCode());
		ids.add(BusTypeEnum.IMP_SEQ_QUALITY.getCode());
		rstmhdtDto.setYwlxs(ids);
		List<FjcfbDto> rstmhdtDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(rstmhdtDto);
		List<String> rstmhdtLj = new ArrayList<>();
		if (rstmhdtDtos !=null && rstmhdtDtos.size()>0){
			for (int i = 0; i < rstmhdtDtos.size(); i++) {
				if(BusTypeEnum.IMP_SEQ_QUALITY_CHROMOSOME_DEAL.getCode().equals(rstmhdtDtos.get(i).getYwlx())) {
					rstmhdtLj.add(dbEncrypt.dCode(rstmhdtDtos.get(i).getWjlj()));
				}			
			}
		}
		map.put("rstmhdt",rstmhdtLj);
		map.put("fjcfbDtoList", rstmhdtDtos);
	}

	/**
	 * 对物种信息进行排序(按序列号从高到低)
	 * @param list
	 * @return
	 */
	public List<SjwzxxDto> WzxxSort(List<SjwzxxDto> list){
		list=list.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList());
		return list;
	}
	
	/**
	 * DNA病毒Viruses_D_List   RNA病毒Viruses_R_List  旧模板（不区分 细菌 分枝杆菌 支原体） 细菌Bacteria_List
	  *  分支杆菌Mycobacteria_List   支原体/衣原体/立克次体(Mycoplasma/Chlamydia/Rickettsia) MCR
	  *  真菌Fungi_List   寄生虫Parasite_List  疑似人体共生微生物Background_List
	 * @param 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private void generateList(SjxxDto sjxxDto,Map<String,Object> map,Map<String,Object> resultMap) {
		//查出所有数据
		List<SjwzxxDto> sjwzxxList= new ArrayList<>();
		List<SjwzxxDto> pathogenAndPossibleList= new ArrayList<>();
		List<SjwzxxDto> possibleList = (List<SjwzxxDto>)resultMap.get("possibleList");
		List<SjwzxxDto> pathogenList = (List<SjwzxxDto>)resultMap.get("pathogenList");
		List<SjwzxxDto> backgroundList = (List<SjwzxxDto>)resultMap.get("backgroundList");
		List<SjwzxxDto> commensalList = (List<SjwzxxDto>)resultMap.get("commensalList");
		String bacterias="";//细菌
		String mycobacterias="";//分枝杆菌
		String mycoplasmas="";//支原体
		String chlamydias="";//衣原体
		String rickettsias="";//立克次体
		String spirochetes="";//螺旋体
		String mcrs="";//支原体/衣原体/立克次体
		String virusess_d="";//DNA病毒
		String virusess_r="";//RNA病毒
		String parasites="";//寄生虫
		String bacterias_rpm="";//细菌
		String mycobacterias_rpm="";//分枝杆菌
		String mcrs_rpm="";//支原体/衣原体/立克次体
		String virusess_d_rpm="";//DNA病毒
		String virusess_rpm="";//病毒
		String virusess_r_rpm="";//RNA病毒
		String parasites_rpm="";//寄生虫
		String fungi_rpm="";//真菌
		String pathogen_rpm = "";//高关
		String possible_rpm = "";//疑似
		String background_rpm = "";//人体共生
		String commensal_rpm = "";//定值
		String bacterias_rpm_new = "";
		String mycobacterias_rpm_new = "";
		String mcrs_rpm_new = "";
		String virusess_d_rpm_new = "";
		String virusess_r_rpm_new = "";
		String parasites_rpm_new = "";
		String fungi_rpm_new = "";
		String pathogen_rpm_new = "";
		String possible_rpm_new = "";
		String background_rpm_new = "";
		String commensal_rpm_new = "";
		if(possibleList!=null && possibleList.size()>0) {
			sjwzxxList.addAll(possibleList);
			pathogenAndPossibleList.addAll(possibleList);
			for (SjwzxxDto sjwzxxDto : possibleList) {
				possible_rpm=possible_rpm+","+(StringUtil.isNotBlank(sjwzxxDto.getWzid())?sjwzxxDto.getWzzwm():sjwzxxDto.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto.getRpm())?sjwzxxDto.getRpm():"")+")";
				if("1".equals(sjwzxxDto.getSfgl())) {
					possible_rpm_new += "," + "{br}{color:FF0000}"+(StringUtil.isNotBlank(sjwzxxDto.getWzid())?sjwzxxDto.getWzzwm():sjwzxxDto.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto.getRpm())?sjwzxxDto.getRpm():"")+")";
				}else {
					possible_rpm_new += ","+(StringUtil.isNotBlank(sjwzxxDto.getWzid())?sjwzxxDto.getWzzwm():sjwzxxDto.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto.getRpm())?sjwzxxDto.getRpm():"")+")";
				}
			}
			map.put("Possible_Count",String.valueOf(possibleList.size()));
		}else {
			map.put("Possible_Count","0");
		}
		if(pathogenList!=null && pathogenList.size()>0) {
			pathogenAndPossibleList.addAll(pathogenList);
			sjwzxxList.addAll(pathogenList);
			for (SjwzxxDto sjwzxxDto : pathogenList) {
				pathogen_rpm=pathogen_rpm+","+(StringUtil.isNotBlank(sjwzxxDto.getWzid())?sjwzxxDto.getWzzwm():sjwzxxDto.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto.getRpm())?sjwzxxDto.getRpm():"")+")";
				if("1".equals(sjwzxxDto.getSfgl())) {
					pathogen_rpm_new += "," + "{br}{color:FF0000}"+(StringUtil.isNotBlank(sjwzxxDto.getWzid())?sjwzxxDto.getWzzwm():sjwzxxDto.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto.getRpm())?sjwzxxDto.getRpm():"")+")";
				}else {
					pathogen_rpm_new += ","+(StringUtil.isNotBlank(sjwzxxDto.getWzid())?sjwzxxDto.getWzzwm():sjwzxxDto.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto.getRpm())?sjwzxxDto.getRpm():"")+")";
				}
			}
			map.put("Pathogen_Count",String.valueOf(pathogenList.size()));
		}else {
			map.put("Pathogen_Count","0");
		}
		if(backgroundList!=null && backgroundList.size()>0) {
			sjwzxxList.addAll(backgroundList);
			for (SjwzxxDto sjwzxxDto : backgroundList) {
				background_rpm=background_rpm+","+(StringUtil.isNotBlank(sjwzxxDto.getWzid())?sjwzxxDto.getWzzwm():sjwzxxDto.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto.getRpm())?sjwzxxDto.getRpm():"")+")";
				if("1".equals(sjwzxxDto.getSfgl())) {
					background_rpm_new += "," + "{br}{color:FF0000}"+(StringUtil.isNotBlank(sjwzxxDto.getWzid())?sjwzxxDto.getWzzwm():sjwzxxDto.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto.getRpm())?sjwzxxDto.getRpm():"")+")";
				}else {
					background_rpm_new += ","+(StringUtil.isNotBlank(sjwzxxDto.getWzid())?sjwzxxDto.getWzzwm():sjwzxxDto.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto.getRpm())?sjwzxxDto.getRpm():"")+")";
				}
			}
			map.put("Background_Count",String.valueOf(backgroundList.size()));
		}else {
			map.put("Background_Count","0");
		}
		if(commensalList!=null && commensalList.size()>0) {
			for (SjwzxxDto sjwzxxDto : commensalList) {
				commensal_rpm=commensal_rpm+","+(StringUtil.isNotBlank(sjwzxxDto.getWzid())?sjwzxxDto.getWzzwm():sjwzxxDto.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto.getRpm())?sjwzxxDto.getRpm():"")+")";
				if("1".equals(sjwzxxDto.getSfgl())) {
					commensal_rpm_new += "," + "{br}{color:FF0000}"+(StringUtil.isNotBlank(sjwzxxDto.getWzid())?sjwzxxDto.getWzzwm():sjwzxxDto.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto.getRpm())?sjwzxxDto.getRpm():"")+")";
				}else {
					commensal_rpm_new += ","+(StringUtil.isNotBlank(sjwzxxDto.getWzid())?sjwzxxDto.getWzzwm():sjwzxxDto.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto.getRpm())?sjwzxxDto.getRpm():"")+")";
				}
			}
			map.put("Commensal_Count",String.valueOf(commensalList.size()));
		}else {
			map.put("Commensal_Count","0");
		}
		//按类别组装List
		List<SjwzxxDto> Viruses_D_List = new ArrayList<>(); //DNA病毒Viruses_D_List
		List<SjwzxxDto> Viruses_List = new ArrayList<>(); //DNA病毒Viruses_D_List
		List<SjwzxxDto> Viruses_R_List = new ArrayList<>(); //RNA病毒Viruses_R_List
		List<SjwzxxDto> Bacteria_List_old = new ArrayList<>(); //旧模板（不区分 细菌 分枝杆菌 支原体）
		List<SjwzxxDto> Bacteria_List = new ArrayList<>(); //细菌Bacteria_List
		List<SjwzxxDto> Mycobacteria_List = new ArrayList<>(); //分支杆菌Mycobacteria_List
		List<SjwzxxDto> MCR_List = new ArrayList<>(); //支原体/衣原体/立克次体(Mycoplasma/Chlamydia/Rickettsia) MCR
		List<SjwzxxDto> Fungi_List = new ArrayList<>(); //真菌Fungi_List
		List<SjwzxxDto> Parasite_List = new ArrayList<>(); //寄生虫Parasite_List
		List<SjwzxxDto> Background_List = new ArrayList<>(); //疑似人体共生微生物Background_List
		List<SjwzxxDto> Commensal_List = new ArrayList<>(); //疑似人体共生微生物Background_List
		List<SjwzxxDto> BackgroundInfo_List = new ArrayList<>(); //疑似人体共生微生物Background_List
		List<SjwzxxDto> Mycoplasma_List = new ArrayList<>();//支原体
		List <SjwzxxDto> Chlamydia_List = new ArrayList<>();//衣原体
		List <SjwzxxDto> Rickettsia_List = new ArrayList<>();//立克次体
		List <SjwzxxDto> Spirochete_List = new ArrayList<>();//螺旋体
		List <SjwzxxDto> MCR_Mycobacteria_List = new ArrayList<>();//分支杆菌,支原体/衣原体/立克次体
		List<SjwzxxDto> Bacteria_Background_List = new ArrayList<>();//细菌疑似
		List<SjwzxxDto> Fungi_Background_List = new ArrayList<>();//真菌疑似
		List<SjwzxxDto> Viruses_Background_List = new ArrayList<>();//病毒疑似
		List<SjwzxxDto> Parasite_Background_List = new ArrayList<>();//寄生虫疑似
		List <SjwzxxDto> MCR_Mycobacteria_Background_List = new ArrayList<>();//分支杆菌,支原体/衣原体/立克次体
		List<SjwzxxDto> Bacteria_Commensal_List = new ArrayList<>();//细菌疑似
		List<SjwzxxDto> Fungi_Commensal_List = new ArrayList<>();//真菌疑似
		List<SjwzxxDto> Viruses_Commensal_List = new ArrayList<>();//病毒疑似
		List<SjwzxxDto> Parasite_Commensal_List = new ArrayList<>();//寄生虫疑似
		List <SjwzxxDto> MCR_Mycobacteria_Commensal_List = new ArrayList<>();//分支杆菌,支原体/衣原体/立克次体
		if(sjwzxxList!=null && sjwzxxList.size()>0) {
			for (SjwzxxDto sjwzxxDto_t : sjwzxxList) {
				if(StringUtil.isNotBlank(sjwzxxDto_t.getXgfx())) {
					sjwzxxDto_t.setWzzwm(sjwzxxDto_t.getWzzwm()+"("+sjwzxxDto_t.getXgfx()+")");
				}
				if("Mycobacteria".equals(sjwzxxDto_t.getTsfl()) && !"background".equals(sjwzxxDto_t.getJglx()) && !"commensal".equals(sjwzxxDto_t.getJglx())){
					mycobacterias=mycobacterias+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm());
				}else if("Mycoplasma".equals(sjwzxxDto_t.getTsfl()) && !"background".equals(sjwzxxDto_t.getJglx()) && !"commensal".equals(sjwzxxDto_t.getJglx())){
					mycoplasmas=mycoplasmas+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm());
					Mycoplasma_List.add(sjwzxxDto_t);
				}else if("Mhlamydia".equals(sjwzxxDto_t.getTsfl()) && !"background".equals(sjwzxxDto_t.getJglx()) && !"commensal".equals(sjwzxxDto_t.getJglx())){
					chlamydias=chlamydias+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm());
					Chlamydia_List.add(sjwzxxDto_t);
				}else if("Mickettsia".equals(sjwzxxDto_t.getTsfl()) && !"background".equals(sjwzxxDto_t.getJglx()) && !"commensal".equals(sjwzxxDto_t.getJglx())){
					rickettsias=rickettsias+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm());
					Rickettsia_List.add(sjwzxxDto_t);
				}else if("Spirochete".equals(sjwzxxDto_t.getTsfl()) && !"background".equals(sjwzxxDto_t.getJglx()) && !"commensal".equals(sjwzxxDto_t.getJglx())){
					spirochetes=spirochetes+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm());
					Spirochete_List.add(sjwzxxDto_t);
				}
				if("Fungi".equals(sjwzxxDto_t.getWzlx())) {
					sjwzxxDto_t.setWzlx("真菌");
				}else if("Viruses".equals(sjwzxxDto_t.getWzlx())) {
					sjwzxxDto_t.setWzlx("病毒");
				}else if("Parasite".equals(sjwzxxDto_t.getWzlx())) {
					sjwzxxDto_t.setWzlx("寄生虫");
				}
				if(sjwzxxDto_t.getJzryzs()==null&&sjwzxxDto_t.getJzryzsbfb()==null) {
					sjwzxxDto_t.setIndex("--");
				}else {
					sjwzxxDto_t.setIndex((StringUtil.isBlank(sjwzxxDto_t.getJzryzs())?"--":sjwzxxDto_t.getJzryzs())+",高于"+(StringUtil.isBlank(sjwzxxDto_t.getJzryzsbfb())?"--":sjwzxxDto_t.getJzryzsbfb())+"%的同类标本");
				}
				if(map!=null) {
					String projectPath = sjwzxxDto_t.getProject_type();
					if(StringUtil.isBlank(projectPath)) {
						projectPath = StringUtil.isNotBlank((String) map.get("jcxm_cskz4"))? (String) map.get("jcxm_cskz4") :"Q";
					}
					String path=releaseFilePath+BusTypeEnum.IMP_TEMEPLATE_IMAGES.getCode()+"/"+projectPath + "/" + (map.get("jcxmcs1")==null?"":map.get("jcxmcs1").toString())+"/"+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzid():sjwzxxDto_t.getSid()) +"_"+(map.get("yblxdm")==null?"":map.get("yblxdm").toString())+".jpg";
					String cursor=releaseFilePath+BusTypeEnum.IMP_TEMEPLATE_IMAGES.getCode()+"/cursor/cursor.png";
					sjwzxxDto_t.setPercentile_path(path);
					sjwzxxDto_t.setCursor_path(cursor);
				}

				if("Viruses".equals(sjwzxxDto_t.getWzfl()) && !"background".equals(sjwzxxDto_t.getJglx()) && !"commensal".equals(sjwzxxDto_t.getJglx())) { //病毒Viruses_List
					sjwzxxDto_t.setBdlx(sjwzxxDto_t.getWzlx());
					virusess_rpm=virusess_rpm+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+")";
					Viruses_List.add(sjwzxxDto_t);
				}else if("Viruses".equals(sjwzxxDto_t.getWzfl()) && "background".equals(sjwzxxDto_t.getJglx())){
						Viruses_Background_List.add(sjwzxxDto_t);
				}else if("Viruses".equals(sjwzxxDto_t.getWzfl()) && "commensal".equals(sjwzxxDto_t.getJglx())){
						Viruses_Commensal_List.add(sjwzxxDto_t);
				}
				if("Viruses".equals(sjwzxxDto_t.getWzfl()) && ("DNA病毒".equals(sjwzxxDto_t.getWzlx()) || "DNA".equals(sjwzxxDto_t.getWzlx()) || StringUtil.isBlank(sjwzxxDto_t.getWzlx())) && !"background".equals(sjwzxxDto_t.getJglx()) && !"commensal".equals(sjwzxxDto_t.getJglx())) { //DNA病毒Viruses_D_List
					Viruses_D_List.add(sjwzxxDto_t);
					virusess_d=virusess_d+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm());
					virusess_d_rpm=virusess_d_rpm+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+")";
					if("1".equals(sjwzxxDto_t.getSfgl())) {
						virusess_d_rpm_new += "," + "{color:FF0000}"+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+"){/color}";
					}else {
						virusess_d_rpm_new += ","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+")";
					}
				}
				if("Viruses".equals(sjwzxxDto_t.getWzfl()) && ("RNA病毒".equals(sjwzxxDto_t.getWzlx()) || "RNA".equals(sjwzxxDto_t.getWzlx())) && !"background".equals(sjwzxxDto_t.getJglx()) && !"commensal".equals(sjwzxxDto_t.getJglx())) { //RNA病毒Viruses_R_List
					Viruses_R_List.add(sjwzxxDto_t);
					virusess_r=virusess_r+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm());
					virusess_r_rpm=virusess_r_rpm+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+")";
					if("1".equals(sjwzxxDto_t.getSfgl())) {
						virusess_r_rpm_new += "," + "{color:FF0000}"+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+"){/color}";
					}else {
						virusess_r_rpm_new += ","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+")";
					}
				}
				if ("Bacteria".equals(sjwzxxDto_t.getWzglwzfl()) || "Mycobacteria".equals(sjwzxxDto_t.getWzglwzfl())
						|| "MCR".equals(sjwzxxDto_t.getWzglwzfl()) || "Bacteria".equals(sjwzxxDto_t.getWzsfl())
						|| "Mycobacteria".equals(sjwzxxDto_t.getWzsfl()) || "MCR".equals(sjwzxxDto_t.getWzsfl())) {//旧模板（不区分 细菌 分枝杆菌 支原体）
					if(!"background".equals(sjwzxxDto_t.getJglx()) && !"commensal".equals(sjwzxxDto_t.getJglx())) {
						Bacteria_List_old.add(sjwzxxDto_t);
					}					
				}
				if("Bacteria".equals(sjwzxxDto_t.getWzfl()) && !"background".equals(sjwzxxDto_t.getJglx()) && !"commensal".equals(sjwzxxDto_t.getJglx())) { //细菌Bacteria_List
					Bacteria_List.add(sjwzxxDto_t);
					bacterias=bacterias+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm());
					bacterias_rpm=bacterias_rpm+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+")";
					if("1".equals(sjwzxxDto_t.getSfgl())) {
						bacterias_rpm_new += "," + "{color:FF0000}"+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+"){/color}";
					}else {
						bacterias_rpm_new += ","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+")";
					}
				}else if("Bacteria".equals(sjwzxxDto_t.getWzfl()) && "background".equals(sjwzxxDto_t.getJglx())){
						Bacteria_Background_List.add(sjwzxxDto_t);
				}else if("Bacteria".equals(sjwzxxDto_t.getWzfl()) && "commensal".equals(sjwzxxDto_t.getJglx())){
						Bacteria_Commensal_List.add(sjwzxxDto_t);
				}
				if("Mycobacteria".equals(sjwzxxDto_t.getWzfl()) && !"background".equals(sjwzxxDto_t.getJglx()) && !"commensal".equals(sjwzxxDto_t.getJglx())) { //分支杆菌Mycobacteria_List
					Mycobacteria_List.add(sjwzxxDto_t);
					MCR_Mycobacteria_List.add(sjwzxxDto_t);
					mycobacterias_rpm=mycobacterias_rpm+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+")";
					if("1".equals(sjwzxxDto_t.getSfgl())) {
						mycobacterias_rpm_new += "," + "{color:FF0000}"+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+"){/color}";
					}else {
						mycobacterias_rpm_new += ","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+")";
					}
				} else if("Mycobacteria".equals(sjwzxxDto_t.getWzfl()) && "background".equals(sjwzxxDto_t.getJglx())){
					MCR_Mycobacteria_Background_List.add(sjwzxxDto_t);
				} else if("Mycobacteria".equals(sjwzxxDto_t.getWzfl()) && "commensal".equals(sjwzxxDto_t.getJglx())){
					MCR_Mycobacteria_Commensal_List.add(sjwzxxDto_t);
				}
				if("MCR".equals(sjwzxxDto_t.getWzfl()) && !"background".equals(sjwzxxDto_t.getJglx()) && !"commensal".equals(sjwzxxDto_t.getJglx())) { //支原体/衣原体/立克次体(Mycoplasma/Chlamydia/Rickettsia) MCR
					MCR_List.add(sjwzxxDto_t);
					MCR_Mycobacteria_List.add(sjwzxxDto_t);
					mcrs=mcrs+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm());
					mcrs_rpm=mcrs_rpm+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+")";
					if("1".equals(sjwzxxDto_t.getSfgl())) {
						mcrs_rpm_new += "," + "{color:FF0000}"+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+"){/color}";
					}else {
						mcrs_rpm_new += ","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+")";
					}
				} else if("MCR".equals(sjwzxxDto_t.getWzfl()) && "background".equals(sjwzxxDto_t.getJglx())){
					MCR_Mycobacteria_Background_List.add(sjwzxxDto_t);
				} else if("MCR".equals(sjwzxxDto_t.getWzfl()) && "commensal".equals(sjwzxxDto_t.getJglx())){
					MCR_Mycobacteria_Commensal_List.add(sjwzxxDto_t);
				}
				if("Fungi".equals(sjwzxxDto_t.getWzfl()) && !"background".equals(sjwzxxDto_t.getJglx()) && !"commensal".equals(sjwzxxDto_t.getJglx())) { //真菌Fungi_List
					Fungi_List.add(sjwzxxDto_t);
					fungi_rpm=fungi_rpm+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+")";
					if("1".equals(sjwzxxDto_t.getSfgl())) {
						fungi_rpm_new += "," + "{color:FF0000}"+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+"){/color}";
					}else {
						fungi_rpm_new += ","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+")";
					}
				}else if("Fungi".equals(sjwzxxDto_t.getWzfl()) && "background".equals(sjwzxxDto_t.getJglx())){
						Fungi_Background_List.add(sjwzxxDto_t);
				}else if("Fungi".equals(sjwzxxDto_t.getWzfl()) && "commensal".equals(sjwzxxDto_t.getJglx())){
						Fungi_Commensal_List.add(sjwzxxDto_t);
				}
				if("Parasite".equals(sjwzxxDto_t.getWzfl()) && !"background".equals(sjwzxxDto_t.getJglx()) && !"commensal".equals(sjwzxxDto_t.getJglx())) { //寄生虫Parasite_List
					Parasite_List.add(sjwzxxDto_t);
					parasites=parasites+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm());
					parasites_rpm=parasites_rpm+","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+")";
					if("1".equals(sjwzxxDto_t.getSfgl())) {
						parasites_rpm_new += "," + "{color:FF0000}"+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+"){/color}";
					}else {
						parasites_rpm_new += ","+(StringUtil.isNotBlank(sjwzxxDto_t.getWzid())?sjwzxxDto_t.getWzzwm():sjwzxxDto_t.getSzwm())+"(RPM:"+(StringUtil.isNotBlank(sjwzxxDto_t.getRpm())?sjwzxxDto_t.getRpm():"")+")";
					}
				}else if("Parasite".equals(sjwzxxDto_t.getWzfl()) && "background".equals(sjwzxxDto_t.getJglx())){
						Parasite_Background_List.add(sjwzxxDto_t);
				}else if("Parasite".equals(sjwzxxDto_t.getWzfl()) && "commensal".equals(sjwzxxDto_t.getJglx())){
						Parasite_Commensal_List.add(sjwzxxDto_t);
				}
				if("background".equals(sjwzxxDto_t.getJglx())) { //疑似人体共生微生物Background_List
					Background_List.add(sjwzxxDto_t);
					if("commensal".equals(sjwzxxDto_t.getLabel())) { //定值微生物BCommensal_List
						Commensal_List.add(sjwzxxDto_t);
					}else{
						BackgroundInfo_List.add(sjwzxxDto_t);
					}
				}
			}
		}
		if(!CollectionUtils.isEmpty(pathogenAndPossibleList)){
			for(SjwzxxDto sjwzxxDto:pathogenAndPossibleList){
				if("Bacteria".equals(sjwzxxDto.getWzfl())){
					map.put("Bacteria_Jy_flg","{color:FF0000}阳性{/color}");
				}
				if("MCR".equals(sjwzxxDto.getWzfl())){
					map.put("MCR_Jy_flg","{color:FF0000}阳性{/color}");
				}
				if("Fungi".equals(sjwzxxDto.getWzfl())) {
					map.put("Fungi_Jy_flg","{color:FF0000}阳性{/color}");
				}
				if("Viruses".equals(sjwzxxDto.getWzfl())) {
					map.put("Viruses_Jy_flg","{color:FF0000}阳性{/color}");
				}
				if("Parasite".equals(sjwzxxDto.getWzfl())) {
					map.put("Parasite_Jy_flg","{color:FF0000}阳性{/color}");
				}
				if("Mycobacteria".equals(sjwzxxDto.getWzfl())) {	
					map.put("Mycobacteria_Jy_flg","{color:FF0000}阳性{/color}");
				}
				if((StringUtil.isNotBlank(String.valueOf(map.get("Bacteria_Jy_flg"))) && map.get("Bacteria_Jy_flg")!=null) || (StringUtil.isNotBlank(String.valueOf(map.get("Mycobacteria_Jy_flg"))) && map.get("Mycobacteria_Jy_flg")!=null)){
					map.put("Bacteria_Mycobacteria_Jy_flg","{color:FF0000}阳性{/color}");
				}
				if((StringUtil.isNotBlank(String.valueOf(map.get("Fungi_Jy_flg"))) && map.get("Fungi_Jy_flg")!=null) || (StringUtil.isNotBlank(String.valueOf(map.get("Parasite_Jy_flg"))) && map.get("Parasite_Jy_flg")!=null)){
					map.put("Fungi_Parasite_Jy_flg","{color:FF0000}阳性{/color}");
				}
			}
		}
		
		//将组装数据放入map
		map.put("sjwzxx_List",pathogenAndPossibleList);//所有物种信息
		// 兼容TB结核报告，因为 大写S 的情况下会 删除模版行 table.removeRow(rowCnt - 1);
		map.put("Sjwzxx_List",pathogenAndPossibleList);//所有物种信息
		map.put("Bacteria_Background_List",Bacteria_Background_List);
		map.put("Fungi_Background_List",Fungi_Background_List);
		map.put("Parasite_Background_List",Parasite_Background_List);
		map.put("Viruses_Background_List",Viruses_Background_List);
		map.put("MCR_Mycobacteria_Background_List",MCR_Mycobacteria_Background_List);
		map.put("Bacteria_Commensal_List",Bacteria_Commensal_List);
		map.put("Fungi_Commensal_List",Fungi_Commensal_List);
		map.put("Parasite_Commensal_List",Parasite_Commensal_List);
		map.put("Viruses_Commensal_List",Viruses_Commensal_List);
		map.put("MCR_Mycobacteria_Commensal_List",MCR_Mycobacteria_Commensal_List);
		map.put("Mycoplasma_List",WzxxSort(Mycoplasma_List));
		map.put("Chlamydia_List",WzxxSort(Chlamydia_List));
		map.put("Rickettsia_List",WzxxSort(Rickettsia_List));
		map.put("Spirochete_List",WzxxSort(Spirochete_List));
		map.put("Commensal_List", WzxxSort(Commensal_List));
		map.put("BackgroundInfo_List", WzxxSort(BackgroundInfo_List));
		map.put("Parasite_List", WzxxSort(Parasite_List));//寄生虫
		map.put("spirochete_Count",String.valueOf(Spirochete_List.size()));//螺旋体总数
		map.put("SpirAndBact_Count",String.valueOf(Bacteria_List.size()+Spirochete_List.size()));//螺旋体加细菌总数


		map.put("Parasite_Count", String.valueOf(Parasite_List.size()));//寄生虫
		map.put("bacterias",StringUtil.isNotBlank(bacterias)?bacterias.substring(1):bacterias);//细菌
		map.put("mcrs",StringUtil.isNotBlank(mcrs)?mcrs.substring(1):mcrs);//支原体/衣原体/立克次体
		map.put("virusess_d",StringUtil.isNotBlank(virusess_d)?virusess_d.substring(1):virusess_d);//DNA病毒
		map.put("virusess_r",StringUtil.isNotBlank(virusess_r)?virusess_r.substring(1):virusess_r);//RNA病毒
		map.put("parasites",StringUtil.isNotBlank(parasites)?parasites.substring(1):parasites);//寄生虫
		map.put("mycobacterias",StringUtil.isNotBlank(mycobacterias)?mycobacterias.substring(1):mycobacterias);//分枝杆菌
		map.put("mycoplasmas",StringUtil.isNotBlank(mycoplasmas)?mycoplasmas.substring(1):mycoplasmas);//支原体
		map.put("chlamydias",StringUtil.isNotBlank(chlamydias)?chlamydias.substring(1):chlamydias);//衣原体
		map.put("rickettsias",StringUtil.isNotBlank(rickettsias)?rickettsias.substring(1):rickettsias);//立克次体
		map.put("spirochetes",StringUtil.isNotBlank(spirochetes)?spirochetes.substring(1):spirochetes);//螺旋体
		map.put("bacterias_rpm",StringUtil.isNotBlank(bacterias_rpm)?bacterias_rpm.substring(1):bacterias_rpm);
		map.put("mycobacterias_rpm",StringUtil.isNotBlank(mycobacterias_rpm)?mycobacterias_rpm.substring(1):mycobacterias_rpm);
		map.put("mcrs_rpm",StringUtil.isNotBlank(mcrs_rpm)?mcrs_rpm.substring(1):mcrs_rpm);
		map.put("virusess_d_rpm",StringUtil.isNotBlank(virusess_d_rpm)?virusess_d_rpm.substring(1):virusess_d_rpm);
		map.put("virusess_rpm",StringUtil.isNotBlank(virusess_rpm)?virusess_rpm.substring(1):virusess_rpm);
		map.put("virusess_r_rpm",StringUtil.isNotBlank(virusess_r_rpm)?virusess_r_rpm.substring(1):virusess_r_rpm);
		map.put("parasites_rpm",StringUtil.isNotBlank(parasites_rpm)?parasites_rpm.substring(1):parasites_rpm);
		map.put("fungi_rpm",StringUtil.isNotBlank(fungi_rpm)?fungi_rpm.substring(1):fungi_rpm);
		map.put("pathogen_rpm",StringUtil.isNotBlank(pathogen_rpm)?pathogen_rpm.substring(1):pathogen_rpm);
		map.put("possible_rpm",StringUtil.isNotBlank(possible_rpm)?possible_rpm.substring(1):possible_rpm);
		map.put("background_rpm",StringUtil.isNotBlank(background_rpm)?background_rpm.substring(1):background_rpm);
		map.put("commensal_rpm",StringUtil.isNotBlank(commensal_rpm)?commensal_rpm.substring(1):commensal_rpm);
		map.put("bacterias_rpm_new",StringUtil.isNotBlank(bacterias_rpm_new)?bacterias_rpm_new.substring(1):bacterias_rpm_new);
		map.put("mycobacterias_rpm_new",StringUtil.isNotBlank(mycobacterias_rpm_new)?mycobacterias_rpm_new.substring(1):mycobacterias_rpm_new);
		map.put("mcrs_rpm_new",StringUtil.isNotBlank(mcrs_rpm_new)?mcrs_rpm_new.substring(1):mcrs_rpm_new);
		map.put("virusess_d_rpm_new",StringUtil.isNotBlank(virusess_d_rpm_new)?virusess_d_rpm_new.substring(1):virusess_d_rpm_new);
		map.put("virusess_r_rpm_new",StringUtil.isNotBlank(virusess_r_rpm_new)?virusess_r_rpm_new.substring(1):virusess_r_rpm_new);
		map.put("parasites_rpm_new",StringUtil.isNotBlank(parasites_rpm_new)?parasites_rpm_new.substring(1):parasites_rpm_new);
		map.put("fungi_rpm_new",StringUtil.isNotBlank(fungi_rpm_new)?fungi_rpm_new.substring(1):fungi_rpm_new);
		map.put("pathogen_rpm_new",StringUtil.isNotBlank(pathogen_rpm_new)?pathogen_rpm_new.substring(1):pathogen_rpm_new);
		map.put("possible_rpm_new",StringUtil.isNotBlank(possible_rpm_new)?possible_rpm_new.substring(1):possible_rpm_new);
		map.put("background_rpm_new",StringUtil.isNotBlank(background_rpm_new)?background_rpm_new.substring(1):background_rpm_new);
		map.put("commensal_rpm_new",StringUtil.isNotBlank(commensal_rpm_new)?commensal_rpm_new.substring(1):commensal_rpm_new);
		if (null!= Parasite_List && Parasite_List.size()>0){
			String Parasite = "";
			for (SjwzxxDto sjwzxxDto:Parasite_List) {
				if (StringUtil.isNotBlank(Parasite)){
					Parasite += "、"+sjwzxxDto.getWzzwm()+" "+sjwzxxDto.getDqs()+" 条";
				}else{
					Parasite += "检出"+sjwzxxDto.getWzzwm()+" "+sjwzxxDto.getDqs()+" 条";
				}
			}
			map.put("jcjg_jsc",Parasite);
		}else{
			map.put("jcjg_jsc","未检出");
		}
		map.put("Fungi_List", WzxxSort(Fungi_List));//真菌
		map.put("Fungi_Count",String.valueOf(Fungi_List.size()));
		String Fungi = "";
		if (null!= Fungi_List && Fungi_List.size()>0){
			for (SjwzxxDto sjwzxxDto:Fungi_List) {
				if (StringUtil.isNotBlank(Fungi)){
					Fungi += "、"+sjwzxxDto.getWzzwm()+" "+sjwzxxDto.getDqs()+" 条";
				}else{
					Fungi += "检出"+sjwzxxDto.getWzzwm()+" "+sjwzxxDto.getDqs()+" 条";
				}
			}
		}else{
			Fungi = "未检出";
		}
		for (SjwzxxDto sjwzxxDto:Background_List) {
			if (StringUtil.isNotBlank(sjwzxxDto.getWzfl()) && "Fungi".equals(sjwzxxDto.getWzfl())){
				Fungi += "，检出人体常见定植菌（具体见疑似微生态列表）";
				break;
			}
		}
		map.put("jcjg_zj",Fungi);
		map.put("MCR_List", WzxxSort(MCR_List));//MCR
		map.put("MCR_Count", String.valueOf(MCR_List.size()));//MCR
		map.put("Mycobacteria_List", Mycobacteria_List);//分支杆菌
		map.put("MCR_Mycobacteria_List",MCR_Mycobacteria_List);
		map.put("Mycobacteria_Count",String.valueOf(Mycobacteria_List.size()));
		if ((null!= Mycobacteria_List && Mycobacteria_List.size()>0) || (MCR_List.size()>0)){
			String  Mycobacteria = "";
			if (null!= Mycobacteria_List && Mycobacteria_List.size()>0){
				for (SjwzxxDto sjwzxxDto:Mycobacteria_List) {
					if (StringUtil.isNotBlank( Mycobacteria)){
						Mycobacteria += "、"+sjwzxxDto.getWzzwm()+" "+sjwzxxDto.getDqs()+" 条";
					}else{
						Mycobacteria += "检出"+sjwzxxDto.getWzzwm()+" "+sjwzxxDto.getDqs()+" 条";
					}
				}
			}
			if (MCR_List.size()>0){
				for (SjwzxxDto sjwzxxDto:MCR_List) {
					if (StringUtil.isNotBlank( Mycobacteria)){
						Mycobacteria += "、"+sjwzxxDto.getWzzwm()+" "+sjwzxxDto.getDqs()+" 条";
					}else{
						Mycobacteria += "检出"+sjwzxxDto.getWzzwm()+" "+sjwzxxDto.getDqs()+" 条";
					}
				}
			}
			map.put("jcjg_qt",Mycobacteria);
		}else{
			map.put("jcjg_qt","未检出");
		}
		map.put("Bacteria_List", WzxxSort(Bacteria_List));//细菌
		map.put("Bacteria_Count",String.valueOf(Bacteria_List.size()));
		String Bacteria = "";
		if (null!= Bacteria_List && Bacteria_List.size()>0){
			for (SjwzxxDto sjwzxxDto:Bacteria_List) {
				if (StringUtil.isNotBlank(Bacteria)){
					if(StringUtil.isNotBlank(sjwzxxDto.getWzid())){
						Bacteria += "、"+sjwzxxDto.getWzzwm()+" "+sjwzxxDto.getDqs()+" 条";
					}else{
						Bacteria += "、"+sjwzxxDto.getSzwm()+" "+sjwzxxDto.getSdds()+" 条";
					}
				}else{
					if(StringUtil.isNotBlank(sjwzxxDto.getWzid())){
						Bacteria += "检出"+sjwzxxDto.getWzzwm()+" "+sjwzxxDto.getDqs()+" 条";
					}else{
						Bacteria += "检出"+sjwzxxDto.getSzwm()+" "+sjwzxxDto.getSdds()+" 条";
					}
				}
			}
		}else{
			Bacteria = "未检出";
		}
		for (SjwzxxDto sjwzxxDto:Background_List) {
			if (StringUtil.isNotBlank(sjwzxxDto.getWzfl()) && "Bacteria".equals(sjwzxxDto.getWzfl())){
				Bacteria += "，检出人体常见定植菌（具体见疑似微生态列表）";
				break;
			}
		}
		map.put("jcjg_xj",Bacteria);
		map.put("Bacteria_List_old", WzxxSort(Bacteria_List_old));
		map.put("Viruses_R_List", WzxxSort(Viruses_R_List));
		map.put("Viruses_R_Count",String.valueOf(Viruses_R_List.size()));
		map.put("Viruses_List", WzxxSort(Viruses_List));//病毒
		map.put("Viruses_Count", String.valueOf(Viruses_List.size()));//病毒
		if (Viruses_List.size()>0){
			String Viruses = "";
			for (SjwzxxDto sjwzxxDto:Viruses_List) {
				if (StringUtil.isNotBlank(Viruses)){
					Viruses += "、"+sjwzxxDto.getWzzwm()+" "+sjwzxxDto.getDqs()+" 条";
				}else{
					Viruses += "检出"+sjwzxxDto.getWzzwm()+" "+sjwzxxDto.getDqs()+" 条";
				}
			}
			map.put("jcjg_bd",Viruses);
		}else{
			map.put("jcjg_bd","未检出");
		}
		map.put("Viruses_D_List", WzxxSort(Viruses_D_List));
		map.put("Viruses_D_Count",String.valueOf(Viruses_D_List.size()));
	}
	
	private void reportTemplateInformation(WeChatInspectionReportModel inspinfo,Map<String,Object> map,SjxxDto sjxxDto_t) {
		//报告模板地址
		//String report_type=inspinfo.getReport_type();
		//String jclxcskz1=inspinfo.getDetection_cskz1();
		//String sfqry = inspinfo.getSfqry();
		//String jcxm_kzcs3 = (String) map.get("jcxm_kzcs3");
		DBEncrypt dbEncrypt=new DBEncrypt();
//		if((jcxm_kzcs3.contains("IMP_REPORT_ONCO_QINDEX_TEMEPLATE")||jcxm_kzcs3.contains("IMP_REPORT_SEQ_")) && StringUtil.isBlank(report_type))
//		{
		String wjlj=(String)map.get("bzfwjlj");
		String wjm=(String) map.get("bzfwjm");
//		}else{
//			wjlj=(String)map.get("fwjlj");
//			wjm=(String) map.get("fwjm");
//		}
		String templPath=dbEncrypt.dCode(wjlj)+"/"+dbEncrypt.dCode(wjm);
		map.put("templPath", templPath);
		map.put("wjm", dbEncrypt.dCode(wjm));
//		map.remove("fwjlj");
//		map.remove("fwjm");
		//生成文件信息（临时路径，正式路径,业务类型）
		map.put("tempFilePath", tempFilePath);
		map.put("releaseFilePath",releaseFilePath);
		map.put("prefix", prefix);
		//报告图片路径
		@SuppressWarnings("unchecked")
		List<FjcfbDto> fjcfbList = (List<FjcfbDto>)map.get("fjcfbDtoList");
		List<String> ljList= new ArrayList<>();
		if(fjcfbList!=null && fjcfbList.size()>0) {
			for (int i = 0; i < fjcfbList.size(); i++) {
				if(BusTypeEnum.IMP_SEQ_QUALITY.getCode().equals(fjcfbList.get(i).getYwlx())) {
					ljList.add(dbEncrypt.dCode(fjcfbList.get(i).getWjlj()));
				}			
			}
		}	
		map.put("images", ljList);
		
		//内部生成合作伙伴为无的Q-Index的模板
//		SjhbxxDto t_SjhbxxDto = new SjhbxxDto();
//		t_SjhbxxDto.setYwlx("IMP_REPORT_QINDEX_TEMEPLATE");
//		List<SjhbxxDto> sjhbxxDtos = sjhbxxService.getBgmbByHbmc(t_SjhbxxDto);
//		if(sjhbxxDtos != null && sjhbxxDtos.size() > 0) {
//			String templPath_no=dbEncrypt.dCode(sjhbxxDtos.get(0).getFwjlj())+"/"+dbEncrypt.dCode(sjhbxxDtos.get(0).getFwjm());
//			map.put("templPath_no", templPath_no);
//			map.put("wjm_no", dbEncrypt.dCode(sjhbxxDtos.get(0).getFwjm()));
//			map.put("hbmc_no", "无");
//			map.put("tmpate_flag_no", "IMP_REPORT_QINDEX_TEMEPLATE");
//			if("1".equals(sfqry)) {
//				if("D".equals(jclxcskz1)) {
//					map.put("ywlx_no",BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_D_REM.getCode());
//				}else if("R".equals(jclxcskz1)) {
//					map.put("ywlx_no",BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_R_REM.getCode());
//				}else if("C".equals(jclxcskz1)) {
//					map.put("ywlx_no",BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_C_REM.getCode());
//				}
//			}else {
//				if("D".equals(jclxcskz1)) {
//					map.put("ywlx_no",BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_D.getCode());
//				}else if("R".equals(jclxcskz1)) {
//					map.put("ywlx_no",BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_R.getCode());
//				}else if("C".equals(jclxcskz1)) {
//					map.put("ywlx_no",BusTypeEnum.IMP_WORD_DETECTION_HBMC_NO_C.getCode());
//				}
//			}
//		}
		map.put("gzlxmc", sjxxDto_t.getGzlxmc());
		map.put("header",inspinfo.getHeader());
		if (map.get("cskz3")!=null){
			map.put("bgmcCskz3",map.get("cskz3").toString());		
		}else {
			map.put("bgmcCskz3","");
		}
		map.put("sjhbxxwjmgz",sjxxDto_t.getWjmgz());
		if(StringUtil.isNotBlank(inspinfo.getProject_type())){
			if(StringUtil.isNotBlank(inspinfo.getDdbh())){
				map.put("ddbh",inspinfo.getDdbh());
			}
			if(StringUtil.isNotBlank(inspinfo.getYzlb())){
				map.put("yzlb",inspinfo.getYzlb());
			}
			if(StringUtil.isNotBlank(inspinfo.getJsrq())){
				map.put("jsrq",inspinfo.getJsrq());
			}
			if(StringUtil.isNotBlank(inspinfo.getSample_type_name())){
				map.put("sample_type_name",inspinfo.getSample_type_name());
			}
			if(!CollectionUtils.isEmpty(inspinfo.getYzjgList())){
				map.put("yzjgList",inspinfo.getYzjgList());
			}
			if(!CollectionUtils.isEmpty(inspinfo.getZkjgList())){
				map.put("zkjgList",inspinfo.getZkjgList());
			}
			if(StringUtil.isNotBlank(inspinfo.getJcxmmc())){
				map.put("jcxmmc",inspinfo.getJcxmmc());
			}
		}
	}
	
	
	/**
	 * 对 接收送检结果信息生成报告 接口中的结果集中的传染级别高的结果过滤
	 */
	public  WeChatInspectionReportModel doFilerListTwo(WeChatInspectionReportModel info,User user,SjxxDto t_sjxxDto) {
		//删除已有的sjid的信息
		SjbgljxxDto t_sjbgljxxDto = new SjbgljxxDto();
		t_sjbgljxxDto.setSjid(t_sjxxDto.getSjid());
		sjbgljxxService.delete(t_sjbgljxxDto);
		//检查里面的物种是不是需要被拦截
		//分表获取pathogen，possible，background参数的list
		List<WeChatInspectionSpeciesModel> allSpecies = new ArrayList<>();
		List<WeChatInspectionSpeciesModel> pathogen = info.getPathogen();
		List<WeChatInspectionSpeciesModel> background = info.getBackground();
		List<WeChatInspectionSpeciesModel> possible = info.getPossible();
		List<WeChatInspectionSpeciesModel> commensal = info.getCommensal();
		if(pathogen!=null&&pathogen.size()>0){
			for (WeChatInspectionSpeciesModel pathogen_t : pathogen) {
				pathogen_t.setJglx("pathogen");
			}
			allSpecies.addAll(pathogen);
		}
		if(background!=null&&background.size()>0){
			for (WeChatInspectionSpeciesModel background_t : background) {
				background_t.setJglx("background");
			}
			allSpecies.addAll(background);
		}
		if(possible!=null&&possible.size()>0){
			for (WeChatInspectionSpeciesModel possible_t : possible) {
				possible_t.setJglx("possible");
			}
			allSpecies.addAll(possible);
		}
		if(commensal!=null&&commensal.size()>0){
			for (WeChatInspectionSpeciesModel commensal_t : commensal) {
				commensal_t.setJglx("commensal");
			}
			allSpecies.addAll(commensal);
		}

		// 先遍历pathogen，检查里面的物种是不是需要被拦截
		if (allSpecies.size() > 0) {
			// 建立一个list放入需要发送的物种结果
			List<WeChatInspectionSpeciesModel> newpathogen = new ArrayList<>();
			List<WeChatInspectionSpeciesModel> newbackground= new ArrayList<>();
			List<WeChatInspectionSpeciesModel> newpossible = new ArrayList<>();
			List<WeChatInspectionSpeciesModel> newcommensal = new ArrayList<>();
			List<WeChatInspectionSpeciesModel> list = wzglService.getInspectionSpeciesModelList(allSpecies);
			List<SjbgljxxDto> sjbgljxxList = new ArrayList<>();
			//从信息对应表获取伙伴物种拦截信息， 若有数据，则以信息对应表为准，若没有则以物种管理为准
			XxdyDto xxdyDto=new XxdyDto();
			xxdyDto.setYxx(t_sjxxDto.getDb());
			xxdyDto.setDylxcsdm("HBWZLJ");
			List<XxdyDto> xxdyDtos=xxdyService.getDtoList(xxdyDto);
			StringBuilder ljwzidsBuilder = new StringBuilder();//需要拦截的物种id
			boolean xxdy_sflj=true;
			Set<String> ljWzidSet = new HashSet<>();
			if(!CollectionUtils.isEmpty(xxdyDtos)){
				for (XxdyDto xxdyDto_t : xxdyDtos) {
					if("1".equals(xxdyDto_t.getDyxx())){//	存入拦截的wzid
						if(StringUtil.isBlank(xxdyDto_t.getKzcs1()))
							continue;
						if (ljwzidsBuilder.length() > 0) {
							ljwzidsBuilder.append(",");
						}
						ljwzidsBuilder.append(xxdyDto_t.getKzcs1());
					}else{
						xxdy_sflj=false;
					}
				}
				String ljwzids = ljwzidsBuilder.toString();
				if(StringUtil.isNotBlank(ljwzids)){
					ljWzidSet = new HashSet<>(Arrays.asList(ljwzids.split(",")));
				}
			}

			for (WeChatInspectionSpeciesModel m : list) {
				if (m==null) continue;

				SjbgljxxDto sjbgljxxDto = new SjbgljxxDto();
				sjbgljxxDto.setJglx(m.getJglx());

				//设置背景图片路径
				allSpecies.stream()
						.filter(species -> StringUtil.isNotBlank(species.getTaxid()) && species.getTaxid().equals(m.getTaxid()))
						.findFirst()
						.ifPresent(species -> m.setBjtppath(species.getBjtppath()));

				// 统一处理拦截逻辑
				boolean shouldAdd = shouldAddSpecies(m, xxdy_sflj, ljWzidSet);
				switch (m.getJglx()) {
					case "pathogen":
						if (shouldAdd) newpathogen.add(m);
						break;
					case "background":
						if (shouldAdd) newbackground.add(m);
						break;
					case "possible":
						if (shouldAdd) newpossible.add(m);
						break;
					case "commensal":
						if (shouldAdd) newcommensal.add(m);
						break;
				}

				if(StringUtil.isNotBlank(m.getCrbjb())) {
					sjbgljxxDto.setLjid(StringUtil.generateUUID());
					sjbgljxxDto.setSflj(shouldAdd?"0":"1");//若需要被添加说明不拦截，设置为0，否则为1
					sjbgljxxDto.setBgrq(DateUtils.getCustomFomratCurrentDate(null));
					sjbgljxxDto.setSjid(t_sjxxDto.getSjid());
					sjbgljxxDto.setWzid(m.getTaxid());
					sjbgljxxDto.setFid(m.getGenus_taxid());
					sjbgljxxDto.setDyr(user.getYhid());
					sjbgljxxList.add(sjbgljxxDto);
				}
			}
			if(sjbgljxxList.size()>0) {
				sjbgljxxService.insertList(sjbgljxxList);
			}
			info.setBackground(newbackground);
			info.setPathogen(newpathogen);
			info.setPossible(newpossible);
			info.setCommensal(newcommensal);
		}
		List<WeChatInspectionResistanceModel> drugResistanceStat = info.getDrug_resistance_stat();
		if (!CollectionUtils.isEmpty(drugResistanceStat)) {
			for (WeChatInspectionResistanceModel m : drugResistanceStat) {
				List<String> reportTaxids = m.getReport_taxids();
                List<String> reportTaxNames = new ArrayList<>();
                if (!CollectionUtils.isEmpty(reportTaxids)){
					for (String reportTaxid : reportTaxids) {
						Optional<WeChatInspectionSpeciesModel> first = allSpecies.stream().filter(item -> reportTaxid.equals(item.getTaxid())).findFirst();
						if (first.isPresent()){
							WeChatInspectionSpeciesModel ism = first.get();
							reportTaxNames.add(StringUtil.isNotBlank(ism.getCn_name())?ism.getCn_name():StringUtil.isNotBlank(ism.getName())?ism.getName():"");
						}
					}
				}
				m.setReport_taxnames(reportTaxNames);
			}
		}
		return info;
	}

	private boolean shouldAddSpecies(WeChatInspectionSpeciesModel m, boolean xxdy_sflj, Set<String> ljWzidSet) {
		// 信息对应表设置不拦截时直接放行
		if (!xxdy_sflj) return true;

		// 白名单检查：物种ID在拦截列表直接拦截(首要判断条件)
		if(!CollectionUtils.isEmpty(ljWzidSet)){
			if(!ljWzidSet.contains(m.getTaxid())) return true;
			else return false;
		}
		//物种本身未被标记拦截(次要判断条件)
		if(!"1".equals(m.getSflj())){
			return true;
		}
		return false;
	}



	/**
	 * 发送报告时去人源申请处理
	 * @param inspinfo
	 * @param t_sjxxDto
	 * @param t_jcsjDto
	 * @param sfljyc
	 * @param user
	 * @throws BusinessException
	 */
	public void dealQrysq(WeChatInspectionReportModel inspinfo,SjxxDto t_sjxxDto,JcsjDto t_jcsjDto,boolean sfljyc,User user) throws BusinessException {
		try {
			SpikerpmDto spikerpmDto=new SpikerpmDto();//getDtoByJcxmAndYblx
			spikerpmDto.setYblx(t_sjxxDto.getYblx());
			spikerpmDto.setJcxm(t_jcsjDto.getCsid());
			spikerpmDto=spikerpmService.getDtoByJcxmAndYblx(spikerpmDto);
			if(spikerpmDto!=null) {
				if(Float.parseFloat(inspinfo.getHost_percentile())>=Float.parseFloat(spikerpmDto.getYz())) {
					String ICOMM_SJ00040=xxglService.getMsg("ICOMM_SJ00040",t_sjxxDto.getHzxm(), t_sjxxDto.getYblxmc());//高人源标题
					String ICOMM_SJ00041=xxglService.getMsg("ICOMM_SJ00041",t_sjxxDto.getHzxm(), t_sjxxDto.getYblxmc());//高人源内容
					String keyword1 = DateUtils.getCustomFomratCurrentDate("HH:mm:ss");
					String keyword2 = t_sjxxDto.getYbbh();
					if("IMP_REPORT_C_TEMEPLATE".equals(t_jcsjDto.getCskz3())) {
						//发送高人源消息
						//判断标本类型是否为肺泡灌洗液，脑脊液(根据参数代码)
						if("D".equals(t_jcsjDto.getCskz1()) || "C".equals(t_jcsjDto.getCskz1())) {
							if("L".equals(t_sjxxDto.getYblxdm()) || "N".equals(t_sjxxDto.getYblxdm()) || "T".equals(t_sjxxDto.getYblxdm()) || "B".equals(t_sjxxDto.getYblxdm())) {
								sendMessageInfo(t_sjxxDto,ICOMM_SJ00040,ICOMM_SJ00041,ICOMM_SJ00040,
										ICOMM_SJ00041,ICOMM_SJ00040,ICOMM_SJ00041,keyword1,keyword2,ybzt_templateid,t_sjxxDto.getHzxm()+"-"+t_sjxxDto.getYblxmc());
							}
						}
					}else if("IMP_REPORT_QINDEX_TEMEPLATE".equals(t_jcsjDto.getCskz3())||
							"IMP_REPORT_ONCO_QINDEX_TEMEPLATE".equals(t_jcsjDto.getCskz3())|| (StringUtil.isNotBlank(t_jcsjDto.getCskz3()) && t_jcsjDto.getCskz3().contains("IMP_REPORT_SEQ_")) ||
							"IMP_SPEED".equals(t_jcsjDto.getCskz3())) {
						//检测项目为D或C。R不做处理
						if("D".equals(t_jcsjDto.getCskz1()) || "C".equals(t_jcsjDto.getCskz1())) {
							//判断标本类型是否为肺泡灌洗液，脑脊液(根据参数代码)
							if("L".equals(t_sjxxDto.getYblxdm()) || "N".equals(t_sjxxDto.getYblxdm()) || "T".equals(t_sjxxDto.getYblxdm())) {
								//判断是否为量仅一次
								if(sfljyc) {
									String ICOMM_SJ00060=xxglService.getMsg("ICOMM_SJ00060",t_sjxxDto.getHzxm(), t_sjxxDto.getYblxmc());//量仅一次通知内容
									sendMessageInfo(t_sjxxDto,ICOMM_SJ00040,ICOMM_SJ00060,ICOMM_SJ00040,
											ICOMM_SJ00060,ICOMM_SJ00040,ICOMM_SJ00060,keyword1,keyword2,ybzt_templateid,t_sjxxDto.getHzxm()+"-"+t_sjxxDto.getYblxmc());
								}else {
									List<JcsjDto> jc_recheck = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.RECHECK.getCode());
									JcsjDto recheck_jcsjDto = null;
									if(jc_recheck != null) {
										for(int i =0;i< jc_recheck.size();i ++){
											JcsjDto tt_Dto = jc_recheck.get(i);
											if("REM".equals(tt_Dto.getCsdm())) {
												recheck_jcsjDto = tt_Dto;
												break;
											}
										}
									}
									if(recheck_jcsjDto != null) {
										//保存加测信息
										JcsjDto jc_jcxm = null;

										List<JcsjDto> jc_jcxms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
										if(jc_jcxms != null) {
											for(int i =0;i< jc_jcxms.size();i ++){
												JcsjDto tt_Dto = jc_jcxms.get(i);
												if("D".equals(tt_Dto.getCskz1()) && (t_sjxxDto.getCskz3()+"_REM").equals(tt_Dto.getCskz3()))
												{
													jc_jcxm = tt_Dto;
													break;
												}
											}
										}
										if(jc_jcxm!=null) {

											FjsqDto fjsqDto = new FjsqDto();
											fjsqDto.setLx(recheck_jcsjDto.getCsid());
											fjsqDto.setJcxm(jc_jcxm.getCsid());
											fjsqDto.setBgbj("1");
											fjsqDto.setBz(xxglService.getMsg("ICOMM_SJ00044"));
											fjsqDto.setSjid(t_sjxxDto.getSjid());
											List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.RECHECK.getCode());
											String csdm="";
											for(JcsjDto dto:list){
												if(dto.getCsid().equals(fjsqDto.getLx())){
													csdm=dto.getCsdm();
													break;
												}
											}
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
											int num = fjsqService.getCount(fjsqDto_t);//查询当前送检信息是否已经存在
											fjsqDto.setShlx(AuditTypeEnum.AUDIT_ADDCHECK_REM.getCode());
											//增加复测加测的检测单位 2021-01-02
											fjsqDto.setJcdw(t_sjxxDto.getJcdw());

											boolean sfczshxx = false;//是否已存在正在审核的数据
											boolean isSuccess;
											boolean sftjsq = false;//是否提交加测申请
											if (num > 0) { //num>0说明已经存在，然后去判断状态
												List<FjsqDto> fjsqDtos = fjsqService.getDtoList(fjsqDto_t);
												if (fjsqDtos != null && fjsqDtos.size() > 0) {
													for (int i = 0; i < fjsqDtos.size(); i++) {
														if (StatusEnum.CHECK_NO.getCode().equals(fjsqDtos.get(i).getZt())) {
															sfczshxx = true;
														}
														if (StatusEnum.CHECK_SUBMIT.getCode().equals(fjsqDtos.get(i).getZt())) {
															sfczshxx = true;
														}
														if (!StatusEnum.CHECK_PASS.getCode().equals(fjsqDtos.get(i).getZt()) && !StatusEnum.CHECK_UNPASS.getCode().equals(fjsqDtos.get(i).getZt())) {
															sfczshxx = true;
														}
													}
													//如果状态是审核完成，或者审核不通过，则进行修改操作
													if (!sfczshxx) {
														if (StringUtils.isNotBlank(user.getYhid()))
															fjsqDto.setLrry(user.getYhid());
														fjsqDto.setZt(StatusEnum.CHECK_NO.getCode());
														if (StringUtil.isBlank(fjsqDto.getFjid())) {
															fjsqDto.setFjid(StringUtil.generateUUID());
														}
														isSuccess = fjsqService.insertFjsq(fjsqDto);
														if (isSuccess) {
															sjxxService.checkSybgSfwc();
															sftjsq = true;
														}
													}
												}
											} else if (num == 0) {//如果num=0，说明不存在，直接进行添加操作
												if (StringUtils.isNotBlank(user.getYhid()))
													fjsqDto.setLrry(user.getYhid());
												fjsqDto.setZt(StatusEnum.CHECK_NO.getCode());
												if (StringUtil.isBlank(fjsqDto.getFjid())) {
													fjsqDto.setFjid(StringUtil.generateUUID());
												}
												isSuccess = fjsqService.insertFjsq(fjsqDto);
												if (isSuccess) {
													sjxxService.checkSybgSfwc();
													sftjsq = true;
												}
											}
											if (sftjsq) {
												//提交复检申请
												ShgcDto shgcDto = new ShgcDto();
												shgcDto.setExtend_1("[\"" + fjsqDto.getFjid() + "\"]");
												shgcDto.setShlb(AuditTypeEnum.AUDIT_ADDCHECK_REM.getCode());
												try {
													String ICOMM_SJ00042=xxglService.getMsg("ICOMM_SJ00042",t_sjxxDto.getHzxm(), t_sjxxDto.getYblxmc());//加做去人源标题
													String ICOMM_SJ00043=xxglService.getMsg("ICOMM_SJ00043",t_sjxxDto.getHzxm(), t_sjxxDto.getYblxmc());//加做去人源内容
													shgcService.checkAndCommit(shgcDto, user);
													sendMessageInfo(t_sjxxDto, ICOMM_SJ00042, ICOMM_SJ00043, ICOMM_SJ00042,
															ICOMM_SJ00043, ICOMM_SJ00042, ICOMM_SJ00043, keyword1, keyword2, ybzt_templateid,t_sjxxDto.getHzxm()+"-"+t_sjxxDto.getYblxmc());
												} catch (BusinessException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
											}
										}
									}
								}
							}else if("B".equals(t_sjxxDto.getYblxdm())) {
								String ICOMM_SJ00061=xxglService.getMsg("ICOMM_SJ00061",t_sjxxDto.getHzxm(), t_sjxxDto.getYblxmc());//仅高人源通知内容
								sendMessageInfo(t_sjxxDto,ICOMM_SJ00040,ICOMM_SJ00061,ICOMM_SJ00040,
										ICOMM_SJ00061,ICOMM_SJ00040,ICOMM_SJ00061,keyword1,keyword2,ybzt_templateid,t_sjxxDto.getHzxm()+"-"+t_sjxxDto.getYblxmc());
							}
							//标本类型为其他
							// 20240904 "其它"样本类型的csdm由XXX改为G
							else if("XXX".equals(t_sjxxDto.getYblxdm()) || "G".equals(t_sjxxDto.getYblxdm())) {
								if(sfljyc) {
									//添加过滤条件:内部编码最后一位是B不发送60量仅一次的消息
									boolean endB = false;
									if(t_sjxxDto.getNbbm()!=null&&!t_sjxxDto.getNbbm().equals("")) {
										String nbbm = t_sjxxDto.getNbbm().trim();
										if(nbbm.length()>0) {
											String nbbmB =  nbbm.substring(nbbm.length()-1, nbbm.length());
											if("B".equals(nbbmB))
												endB=true;
										}
									}
									if(!endB) {
										String ICOMM_SJ00060=xxglService.getMsg("ICOMM_SJ00060",t_sjxxDto.getHzxm(), t_sjxxDto.getYblxmc());//量仅一次通知内容
										sendMessageInfo(t_sjxxDto,ICOMM_SJ00040,ICOMM_SJ00060,ICOMM_SJ00040,
												ICOMM_SJ00060,ICOMM_SJ00040,ICOMM_SJ00060,keyword1,keyword2,ybzt_templateid,t_sjxxDto.getHzxm()+"-"+t_sjxxDto.getYblxmc());
									}
								}else {
									sendMessageInfo(t_sjxxDto,ICOMM_SJ00040,ICOMM_SJ00041,ICOMM_SJ00040,
											ICOMM_SJ00041,ICOMM_SJ00040,ICOMM_SJ00041,keyword1,keyword2,ybzt_templateid,t_sjxxDto.getHzxm()+"-"+t_sjxxDto.getYblxmc());
								}
							}
						}
					}
				}
			}else {
				log.error("阈值信息为空！");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
	}

	/**
	 * 用于接口对接的时候如果出现任何异常的时候进行重试
	 * @param params
	 * @return
	 * @throws BusinessException
	 */
	public Map<String,Object> resendNewReportByException(Map<String,String> params) throws BusinessException {
		String sjid = params.get("sjid");
		SjxxDto t_sjxxDto = sjxxService.getDtoById(sjid);
		List<JkdymxDto> jkdymxDtos = jkdymxService.getListByYwid(t_sjxxDto.getYbbh());
		String yhm = params.get("yhm");
		List<JcsjDto> jclxs = new ArrayList<JcsjDto>();
		Set<JcsjDto> jclxSet = new HashSet<JcsjDto>();
		if(jkdymxDtos!=null){
			//循环获取zywid，并去重，然后用【，】组成字符串
			for(JkdymxDto jkdymxDto:jkdymxDtos){
				if(StringUtil.isNotBlank(jkdymxDto.getZxmid())) {
					JcsjDto jc_lxDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode(), jkdymxDto.getZxmid());
					jclxSet.add(jc_lxDto);
				}else{
					JcsjDto jc_lxDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(), jkdymxDto.getZywid());
					jclxSet.add(jc_lxDto);
				}
			}
			jclxs.addAll(jclxSet);
		}
		return resendNewReport(t_sjxxDto,yhm,jclxs);
	}

	/**
	 * 组装重发报告信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public Map<String,Object> resendNewReport(SjxxDto sjxxDto, String yhm,List<JcsjDto> jcsj_lxs) throws BusinessException {
		// TODO Auto-generated method stub
		// 查询接收信息
		List<JkdymxDto> jkdymxDtos = jkdymxService.getListByYwid(sjxxDto.getYbbh());
		Map<String,Object> result = new HashMap<>();
		if(!CollectionUtils.isEmpty(jkdymxDtos)) {
			if(jcsj_lxs!=null && jcsj_lxs.size()>0){
				for(JcsjDto jc_lx:jcsj_lxs){
					JkdymxDto jkdymxDto=null;
					for(JkdymxDto dto:jkdymxDtos){
						if(StringUtil.isBlank(jc_lx.getFcsid()) && jc_lx.getCsid().equals(dto.getZywid())){
							jkdymxDto=dto;
							break;
						}else if(StringUtil.isNotBlank(jc_lx.getFcsid()) && jc_lx.getFcsid().equals(dto.getZywid())
								&& jc_lx.getCsid().equals(dto.getZxmid())){
							jkdymxDto=dto;
							break;
						}
					}
					if(jkdymxDto!=null){
						List<JcsjDto> list = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
						Optional<JcsjDto> optional = list.stream().filter(item -> "C".equals(item.getCskz1()) && "IMP_REPORT_SEQ_TNGS_N".equals(item.getCskz3())).findFirst();
						Optional<JcsjDto> optional_G = list.stream().filter(item -> "C".equals(item.getCskz1()) && "IMP_REPORT_SEQ_TNGS_G".equals(item.getCskz3())).findFirst();
						String zywid = "";
						String zywid_g="";
						if (optional.isPresent()){
							zywid = optional.get().getCsid();
						}
						if(optional_G.isPresent()){
							zywid_g = optional_G.get().getCsid();
						}
						log.error("报告重发sample_result:"+jkdymxDto.getNr());
						if (StringUtil.isNotBlank(zywid) && zywid.equals(jkdymxDto.getZywid())){
							SjxxDto parseObject = JSON.parseObject(jkdymxDto.getNr(), SjxxDto.class);
							boolean isSuccess = sendReportFile(parseObject);
							result.put("status",isSuccess?"success":"fail");
							result.put("msg",isSuccess?"报告重发成功":"报告重发失败");
						}else if(StringUtil.isNotBlank(zywid_g) && zywid_g.equals(jkdymxDto.getZywid())){
							SjxxDto parseObject = JSON.parseObject(jkdymxDto.getNr(), SjxxDto.class);
							User user=new User();
							user.setYhm(yhm);
							List<String> t_jclxs = new ArrayList<>();
							String t_jclx="";
							List<JcsjDto> jclxlist = redisUtil.lgetDto("All_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
							for (JcsjDto jcsjDto : jclxlist) {
								if ("IMP_REPORT_SEQ_TNGS_N".equals(jcsjDto.getCskz3())){
									t_jclx = jcsjDto.getCsid();
									t_jclxs.add(jcsjDto.getCsid());
								}
								//泛感染
								if("IMP_REPORT_SEQ_TNGS_E".equals(jcsjDto.getCskz3())){
									t_jclxs.add(jcsjDto.getCsid());
								}
							}
							parseObject.setJclxs(t_jclxs);
							parseObject.setJclx(t_jclx);
							boolean isSuccess = mergeSendReportFile(parseObject,user);
							result.put("status",isSuccess?"success":"fail");
							result.put("msg",isSuccess?"报告重发成功":"报告重发失败");
						}else {
							result = receiveInspectionGenerateReport(null, jkdymxDto.getNr(), yhm,jkdymxDto);
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 处理pathogen、possible、background检出物种的阳性flag
	 * @param map
	 * @param inspinfo
	 */
	public void dealTaxFlgList(Map<String,Object> map,WeChatInspectionReportModel inspinfo){
		List<WeChatInspectionSpeciesModel> pathogenList = inspinfo.getPathogen();
		List<WeChatInspectionSpeciesModel> possibleList = inspinfo.getPossible();
		List<WeChatInspectionSpeciesModel> backgroundList = inspinfo.getBackground();
		Map<String,WeChatInspectionSpeciesModel> taxFlgMap = new HashMap<>();
		String[] flgs11320 = {"102793","114727","114728","119214","119215","119216","119217","119218","119220","119221","140020","170500","222770","232441","273303","286279","286295","325678","329376","333278","342224","352775","352776","385680","465975","476651","571502","114729","119210"};
		List<String> flgList11320 = Arrays.asList(flgs11320);
		if (pathogenList!=null&&pathogenList.size()>0) {
			for (WeChatInspectionSpeciesModel pathogen : pathogenList) {
				String pathogenTaxId = pathogen.getTaxid();
				pathogen.setWzbgpz("{color:FF0000}阳性");
				pathogen.setWzbgpzall("{color:FF0000}阳性");
				pathogen.setReportType("pathogen");
				taxFlgMap.put(pathogenTaxId+"_flg",pathogen);
				if (flgList11320.contains(pathogen.getTaxid())){
					taxFlgMap.put("11320_flg",pathogen);//若汇报了flgs11320 中任一 ID：就把11320标记为阳性
				}
			}
		}
		if (possibleList!=null&&possibleList.size()>0) {
			for (WeChatInspectionSpeciesModel possible : possibleList) {
				String possibleTaxId = possible.getTaxid();
				possible.setWzbgpz("{color:FF0000}阳性");
				possible.setWzbgpzall("{color:FF0000}阳性");
				possible.setReportType("possible");
				taxFlgMap.put(possibleTaxId+"_flg",possible);
				if (flgList11320.contains(possible.getTaxid())){
					taxFlgMap.put("11320_flg",possible);//若汇报了flgs11320 中任一 ID：就把11320标记为阳性
				}
			}
		}
		Map<String,WeChatInspectionSpeciesModel> specialTaxFlg_List = new HashMap<>();
		specialTaxFlg_List.putAll(taxFlgMap);
		map.put("specialTaxFlg_List",specialTaxFlg_List);//特殊病原(pathogenList+possibleList)

		Map<String,WeChatInspectionSpeciesModel> backgroundTaxFlg_List = new HashMap<>();//特殊病原(backgroundList)
		if (backgroundList!=null&&backgroundList.size()>0) {
			for (WeChatInspectionSpeciesModel background : backgroundList) {
				String backgroundTaxId = background.getTaxid();
				background.setWzbgpz("{color:FF0000}阳性");
				background.setWzbgpzall("{b}疑似");
				background.setReportType("background");
				taxFlgMap.put(backgroundTaxId+"_flg",background);
				backgroundTaxFlg_List.put(backgroundTaxId+"_flg",background);
				if (flgList11320.contains(background.getTaxid())){
					taxFlgMap.put("11320_flg",background);//若汇报了flgs11320 中任一 ID：就把11320标记为阳性
				}
			}
		}
		map.put("taxFlg_List",taxFlgMap);

		map.put("backgroundTaxFlg_List",backgroundTaxFlg_List);//(backgroundList)
		Map<String,WeChatInspectionSpeciesModel> allTaxFlg_List = new HashMap<>();
		allTaxFlg_List.putAll(taxFlgMap);
		map.put("allTaxFlg_List",allTaxFlg_List);//(pathogenList+possibleList+backgroundList)

	}

	/**
	 * 先声回传实验结果
	 * @param sjxxDto1
	 * @throws BusinessException
	 */
	public boolean xianshengSaveExperimentResult(SjxxDto sjxxDto1) throws BusinessException {
		String ybbh=sjxxDto1.getYbbh();
		SjkzxxDto sjkzxxDto=new SjkzxxDto();
		sjkzxxDto.setSjid(sjxxDto1.getSjid());
		SjkzxxDto sjkzxxDto_t=sjkzxxService.getSjkzxxBySjid(sjkzxxDto);
		String qtxx=sjkzxxDto_t.getQtxx();
		//List<JcsjDto> xxdyList=redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.XXDY_TYPE.getCode());
		if(StringUtil.isNotBlank(qtxx)){
			Map<String,Object>map=new HashMap<>();

			try {
				Map<String, Object> qtxxmap = JSONObject.parseObject(qtxx, new TypeReference<>() {
				});

				map.put("serviceBillId", qtxxmap.get("orderId") == null ? "" : qtxxmap.get("orderId"));
				map.put("goodsCode", qtxxmap.get("limsTestCode") == null ? "" : qtxxmap.get("limsTestCode"));
				map.put("XIANSHENG_URL", qtxxmap.get("XIANSHENG_URL") == null ? "" : qtxxmap.get("XIANSHENG_URL"));
				map.put("samplerCode", qtxxmap.get("samplerCode") == null ? "" : qtxxmap.get("samplerCode"));
				XxdyDto xxdyDto = new XxdyDto();
				xxdyDto.setYxx("南京先声-" + map.get("goodsCode"));
				List<XxdyDto> xxdyDtoList = xxdyService.getDtoMsgByYxx(xxdyDto);
				Optional<XxdyDto> optional = xxdyDtoList.stream().filter(e -> ("南京先声-" + map.get("goodsCode")).equals(e.getYxx())).findFirst();
				if (optional.isPresent()) {
					map.put("nodeCode", optional.get().getKzcs1());
				}

				map.put("taskCode", qtxxmap.get("taskCode") == null ? "" : qtxxmap.get("taskCode"));
			} catch (Exception e) {
				log.error("先声信息qtxx(" + qtxx + ")解析异常：" + e.getMessage());
			}
			Map<String,Object>experimentResult=commonJsonResult(null,ybbh,"NJXS");
			List<Object>mapList=new ArrayList<>();
			mapList.add(experimentResult.get("libaryMap"));
			map.put("experimentResult",mapList);
			//回传实验结果，直接调用rest ，未使用任何内部类
			MatchingUtil matchingUtil=new MatchingUtil();
			boolean isSuccess = matchingUtil.saveResultInfo(map);
			if(!isSuccess){
				//因网络问题，重新发送报告
				Map<String,String> params=new HashMap<>();
				params.put("ybbh",ybbh);
				params.put("sjid",sjxxDto1.getSjid());
				params.put("yhm","IVD");
				params.put("subname","实验结果回传");
				params.put("resultinfo",sjxxDto1.getSjid());
				resetReportSend(params);
				return false;
			}
		}
		return true;
	}

	/**
	 * 导出先声的excel文件，用于接口回传使用
	 * @param sjxxDto1
	 * @param inspinfo
	 */
	private void xianShengExcel(SjxxDto sjxxDto1,WeChatInspectionReportModel inspinfo){
		Map<String,Object> matchingReportSendMap=new HashMap<>();

		matchingReportSendMap.put("method","uploadById");
		matchingReportSendMap.put("sjid",sjxxDto1.getSjid());
		matchingReportSendMap.put("ybbh",sjxxDto1.getYbbh());
		matchingReportSendMap.put("hzxm",sjxxDto1.getHzxm());
		matchingReportSendMap.put("yblxmc",sjxxDto1.getYblxmc());
		matchingReportSendMap.put("sjxxDto",sjxxDto1);
		matchingReportSendMap.put("biosequrl",biosequrl);
		matchingReportSendMap.put("inspinfo",inspinfo);
		matchingReportSendMap.put("XIANSHENG_URL",XIANSHENG_URL);
		sjxxService.matchingUtilNewRun(matchingReportSendMap,"xianshengInfo");
	}
	/**
	 * 处理毒力因子数据
	 * @param map
	 * @param inspinfo
	 */
	public void dealVirulenceFactorStat(SjxxDto sjxxDto,Map<String,Object> map,WeChatInspectionReportModel inspinfo){
		List<WechatVirulenceFactorStatModel> vfs = inspinfo.getVirulence_factor_stat();
		SjdlxxDto t_sjdlxxDto=new SjdlxxDto();
		t_sjdlxxDto.setSjid(sjxxDto.getSjid());
		t_sjdlxxDto.setWkbh(inspinfo.getSample_id());
		t_sjdlxxDto.setJclx(inspinfo.getDetection_type());
		sjdlxxService.delete(t_sjdlxxDto);
		if (vfs != null && vfs.size()>0){
			map.put("vfsList",vfs);
			//保存毒力信息
			List<SjdlxxDto> sjdlxxDtos=new ArrayList<>();
			for(WechatVirulenceFactorStatModel vf:vfs){
				SjdlxxDto sjdlxxDto=new SjdlxxDto();
				sjdlxxDto.setSjid(sjxxDto.getSjid());
				sjdlxxDto.setWkbh(inspinfo.getSample_id());
				sjdlxxDto.setJy(vf.getGene());
				sjdlxxDto.setDlyz(vf.getVf_name());
				sjdlxxDto.setMs(vf.getComment());
				sjdlxxDto.setDlyzlx(vf.getVf_category());
				sjdlxxDto.setZmc(vf.getSpecies_name());
				sjdlxxDto.setYsdlyz(vf.getVirulence_factor());
				sjdlxxDto.setXls(vf.getReads_count());
				sjdlxxDto.setDlyzid(vf.getVf_id());
				sjdlxxDto.setGlwzid(vf.getSpecies_taxid());
				sjdlxxDto.setKnlywz(vf.getRelated_species());
				sjdlxxDto.setJclx(inspinfo.getDetection_type());
				sjdlxxDtos.add(sjdlxxDto);
			}
			if(!CollectionUtils.isEmpty(sjdlxxDtos)){
				sjdlxxService.insertDtoList(sjdlxxDtos);
			}
		}
	}

	/**
	 * 根据文库名称获取文库信息
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getLibraryInfo(Map<String,Object> map) {
		List<Map<String,Object>> libraryInfoList = new ArrayList<>();
		//获取当天最新的一条文库信息
		Map<String,Object> recentlyLibraryInfo = dao.getRecentlyLibraryInfo(map);
		if (recentlyLibraryInfo!=null){
			String cjsj_s = (String) recentlyLibraryInfo.get("cjsj");
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Map<String, Object> sqlMap = new HashMap<>();
			sqlMap.put("nbbh",map.get("nbbh"));
			sqlMap.put("wkbm",map.get("wkbm"));
			try {
				Date cjsj = simpleDateFormat.parse(cjsj_s);
				int hours = 0;
				if(cjsj_s.length() >= 13)
					hours = Integer.parseInt(cjsj_s.substring(11,13));
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(cjsj);
				//判断创建时间是否为早8点后
				if (hours>=8) {
					String startDate = dateFormat.format(cal.getTime());//当天12点之后
					cal.add(Calendar.DATE, 1);
					String endDate = dateFormat.format(cal.getTime());//后一天8点之前
					sqlMap.put("startDate",startDate+" 12");
					sqlMap.put("endDate",endDate+" 08");
				}else {
					String endDate = dateFormat.format(cal.getTime());//当天8点之前
					cal.add(Calendar.DATE, -1);
					String startDate = dateFormat.format(cal.getTime());//前一天12点之后
					sqlMap.put("startDate",startDate+" 12");
					sqlMap.put("endDate",endDate+" 08");
				}
			} catch (ParseException e) {
				log.error("创建时间格式化失败！"+e.toString());
				return libraryInfoList;
			}
			libraryInfoList = dao.getLibraryList(sqlMap);
		}
		return libraryInfoList;
	}

	/**
	 * weChatInspectionSpeciesModel转换成SjwzxxDto
	 * @param originalList
	 * @param papers
	 * @param inspinfo
	 * @param yhid
	 * @param ywlx
	 * @param jclx_str
	 * @return
	 */
	public List<SjwzxxDto> weChatInspectionSpeciesModelToSjwzxxDto(List<WeChatInspectionSpeciesModel> originalList, List<WechatReferencesModel> papers, WeChatInspectionReportModel inspinfo, String yhid, String ywlx, String jclx_str){
		List<SjwzxxDto> resultList = new ArrayList<>();
		//总扩增子
		Map<String, WeChatInspectionAmpliconModel> amplicons = inspinfo.getAmplicon();

		if (originalList != null && originalList.size() > 0) {
			for (int i = 0; i < originalList.size(); i++) {
				SjwzxxDto sjwzxxDto = new SjwzxxDto();
				sjwzxxDto.setXh(String.valueOf(i + 1));
				sjwzxxDto.setSjwzid(StringUtil.generateUUID());
				sjwzxxDto.setSjid(inspinfo.getSjid());
				sjwzxxDto.setXpxx(inspinfo.getSample_id());
				sjwzxxDto.setJclx(inspinfo.getDetection_type());
				sjwzxxDto.setJczlx(inspinfo.getDetection_subtype());
				sjwzxxDto.setJglx(ywlx);
				sjwzxxDto.setJclxmc(jclx_str);
				sjwzxxDto.setSm(originalList.get(i).getGenus_name());
				sjwzxxDto.setRpm(originalList.get(i).getRpm());
				sjwzxxDto.setZcd(originalList.get(i).getRef_length());
				sjwzxxDto.setDqs(originalList.get(i).getReads_count());
				sjwzxxDto.setSid(originalList.get(i).getGenus_taxid());
				sjwzxxDto.setSfd(originalList.get(i).getGenus_abundance());
				sjwzxxDto.setWzid(originalList.get(i).getTaxid());
				sjwzxxDto.setWzfl(originalList.get(i).getSp_type());
				sjwzxxDto.setWzzs(originalList.get(i).getComment());
				sjwzxxDto.setXdfd(originalList.get(i).getAbundance());
				sjwzxxDto.setFldj(originalList.get(i).getRank_code());
				sjwzxxDto.setHslx(originalList.get(i).getLibrary_type());
				sjwzxxDto.setFgcd(originalList.get(i).getCover_length());
				sjwzxxDto.setSzwm(originalList.get(i).getGenus_cn_name());
				sjwzxxDto.setTsfl(originalList.get(i).getSpecial_genus());
				sjwzxxDto.setWzlx(originalList.get(i).getSpecies_category());
				sjwzxxDto.setSdds(originalList.get(i).getGenus_reads_accum());
				sjwzxxDto.setWzywm(originalList.get(i).getName());
				sjwzxxDto.setWzzwm(originalList.get(i).getCn_name());
				sjwzxxDto.setWzsfl(originalList.get(i).getSp_type());
				sjwzxxDto.setJyzfgd(originalList.get(i).getCoverage());
				sjwzxxDto.setWzfllx(originalList.get(i).getVirus_type());
				sjwzxxDto.setWzglwzfl(originalList.get(i).getSp_type());
				sjwzxxDto.setBjtppath(originalList.get(i).getBjtppath());
				sjwzxxDto.setXgfx(originalList.get(i).getClade());
				sjwzxxDto.setYgz(originalList.get(i).getYgz());
				sjwzxxDto.setJglxmc(originalList.get(i).getJglxmc());
				sjwzxxDto.setProject_type(originalList.get(i).getProject_type());
				sjwzxxDto.setLabel(originalList.get(i).getLabel());
				sjwzxxDto.setJcfl(originalList.get(i).getDetectType());
				//确认扩增子信息是否存在
				if(amplicons!=null){
					WeChatInspectionAmpliconModel wz_amplicon = amplicons.get(originalList.get(i).getTaxid());
					if(wz_amplicon!=null){
						//总扩增子
						sjwzxxDto.setZkzz(wz_amplicon.getRef_sequence());
						//比对成功扩增子
						sjwzxxDto.setBdcgkzz(wz_amplicon.getMapped_ref_seqs());
					}
				}
				if(StringUtils.isNotBlank(yhid)){
					sjwzxxDto.setLrry(yhid);
				}
				if(StringUtils.isNotBlank(originalList.get(i).getQ_index())){
					sjwzxxDto.setJzryzs(originalList.get(i).getQ_index());
				}
				if(StringUtils.isNotBlank(originalList.get(i).getQ_position())){
					sjwzxxDto.setJzryzswz(originalList.get(i).getQ_position());
				}
				if(StringUtils.isNotBlank(originalList.get(i).getQ_percentile())){
					sjwzxxDto.setJzryzsbfb(originalList.get(i).getQ_percentile());
				}
				//是否高亮
				if (StringUtils.isNotBlank(originalList.get(i).getHighlight()) && !originalList.get(i).getHighlight().equals("false")) {
					sjwzxxDto.setSfgl("1");
				}else {
					sjwzxxDto.setSfgl("0");
				}
				// 根据物种id放入文献id 如果只传属的话，属的物种id和文献的物种id对应；如果传属和种的话，种的物种id和文献的物种id对应
				String wzid = originalList.get(i).getTaxid();
				if(StringUtil.isBlank(wzid)){
					wzid = originalList.get(i).getGenus_taxid();
				}
				for (int j = 0; j < papers.size(); j++) {
					if(wzid.equals(papers.get(j).getSpecies_taxid())){
						sjwzxxDto.setWxid(papers.get(j).getId());
						break;
					}
				}
				sjwzxxDto = packageSjwzxx(sjwzxxDto);
				resultList.add(sjwzxxDto);
			}
		}
		return resultList;
	}

	/**
	 * 对list进行排序，先根据物种分类，再根据读取数
	 * @return
	 */
	public Map<String,List<SjwzxxDto>> sortBySpeciesMap(List<SjwzxxDto> possibles, List<SjwzxxDto> pathogens, List<SjwzxxDto> backgrounds) {
		Map<String,List<SjwzxxDto>> returnMap = new HashMap<>();
		List<SjwzxxDto> possibleList = new ArrayList<>();
		List<SjwzxxDto> pathogenList = new ArrayList<>();
		List<SjwzxxDto> backgroundList = new ArrayList<>();
		List<SjwzxxDto> commensalList = new ArrayList<>();
		List<SjwzxxDto> backgroundInfoList = new ArrayList<>();
		List<SjwzxxDto> pathogenAndpossibleList = new ArrayList<>();//possible+pathogen
		List<SjwzxxDto> list = new ArrayList<>();//possible+pathogen+background+commensals
		//all:possible+pathogen（+background+commensals）;不带前缀:possible或者pathogen或者background
		List<SjwzxxDto> Bacterialist = new ArrayList<>();
		List<SjwzxxDto> Mycobacterialist = new ArrayList<>();
		List<SjwzxxDto> MCRlist = new ArrayList<>();
		List<SjwzxxDto> Fungilist = new ArrayList<>();
		List<SjwzxxDto> Viruses_DNAlist = new ArrayList<>();
		List<SjwzxxDto> Viruses_RNAlist = new ArrayList<>();
		List<SjwzxxDto> Parasitelist = new ArrayList<>();
		List<SjwzxxDto> allBacterialist = new ArrayList<>();
		List<SjwzxxDto> allMycobacterialist = new ArrayList<>();
		List<SjwzxxDto> allMCRlist = new ArrayList<>();
		List<SjwzxxDto> allFungilist = new ArrayList<>();
		List<SjwzxxDto> allViruses_DNAlist = new ArrayList<>();
		List<SjwzxxDto> allViruses_RNAlist = new ArrayList<>();
		List<SjwzxxDto> allParasitelist = new ArrayList<>();
		if (possibles != null && possibles.size() > 0){
			for (SjwzxxDto possible : possibles) {
				if("Bacteria".equals(possible.getWzfl())){
					allBacterialist.add(possible);
					Bacterialist.add(possible);
				}
				if("Mycobacteria".equals(possible.getWzfl())){
					allMycobacterialist.add(possible);
					Mycobacterialist.add(possible);
				}
				if("MCR".equals(possible.getWzfl())) {
					allMCRlist.add(possible);
					MCRlist.add(possible);
				}
				if("Fungi".equals(possible.getWzfl())){
					allFungilist.add(possible);
					Fungilist.add(possible);
				}
				if("Viruses".equals(possible.getWzfl()) && ("DNA".equals(possible.getWzlx()) || "DNA病毒".equals(possible.getWzlx()))){
					allViruses_DNAlist.add(possible);
					Viruses_DNAlist.add(possible);
				}
				if("Viruses".equals(possible.getWzfl()) && ("RNA".equals(possible.getWzlx()) || "RNA病毒".equals(possible.getWzlx()))){
					allViruses_RNAlist.add(possible);
					Viruses_RNAlist.add(possible);
				}
				if("Parasite".equals(possible.getWzfl())){
					allParasitelist.add(possible);
					Parasitelist.add(possible);
				}
			}
			if (Bacterialist.size() > 0)
				possibleList.addAll(Bacterialist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (Mycobacterialist.size() > 0)
				possibleList.addAll(Mycobacterialist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (MCRlist.size() > 0)
				possibleList.addAll(MCRlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (Fungilist.size() > 0)
				possibleList.addAll(Fungilist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (Viruses_DNAlist.size() > 0)
				possibleList.addAll(Viruses_DNAlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (Viruses_RNAlist.size() > 0)
				possibleList.addAll(Viruses_RNAlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (Parasitelist.size() > 0)
				possibleList.addAll(Parasitelist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
		}
		//清空
		Bacterialist = new ArrayList<>();
		Mycobacterialist = new ArrayList<>();
		MCRlist = new ArrayList<>();
		Fungilist = new ArrayList<>();
		Viruses_DNAlist = new ArrayList<>();
		Viruses_RNAlist = new ArrayList<>();
		Parasitelist = new ArrayList<>();
		if (pathogens != null && pathogens.size() > 0){
			for (SjwzxxDto pathogen : pathogens) {
				if("Bacteria".equals(pathogen.getWzfl())){
					allBacterialist.add(pathogen);
					Bacterialist.add(pathogen);
				}
				if("Mycobacteria".equals(pathogen.getWzfl())){
					allMycobacterialist.add(pathogen);
					Mycobacterialist.add(pathogen);
				}
				if("MCR".equals(pathogen.getWzfl())) {
					allMCRlist.add(pathogen);
					MCRlist.add(pathogen);
				}
				if("Fungi".equals(pathogen.getWzfl())){
					allFungilist.add(pathogen);
					Fungilist.add(pathogen);
				}
				if("Viruses".equals(pathogen.getWzfl()) && ("DNA".equals(pathogen.getWzlx()) || "DNA病毒".equals(pathogen.getWzlx()))){
					allViruses_DNAlist.add(pathogen);
					Viruses_DNAlist.add(pathogen);
				}
				if("Viruses".equals(pathogen.getWzfl()) && ("RNA".equals(pathogen.getWzlx()) || "RNA病毒".equals(pathogen.getWzlx()))){
					allViruses_RNAlist.add(pathogen);
					Viruses_RNAlist.add(pathogen);
				}
				if("Parasite".equals(pathogen.getWzfl())){
					allParasitelist.add(pathogen);
					Parasitelist.add(pathogen);
				}
			}
			if (Bacterialist.size() > 0)
				pathogenList.addAll(Bacterialist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (Mycobacterialist.size() > 0)
				pathogenList.addAll(Mycobacterialist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (MCRlist.size() > 0)
				pathogenList.addAll(MCRlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (Fungilist.size() > 0)
				pathogenList.addAll(Fungilist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (Viruses_DNAlist.size() > 0)
				pathogenList.addAll(Viruses_DNAlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (Viruses_RNAlist.size() > 0)
				pathogenList.addAll(Viruses_RNAlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (Parasitelist.size() > 0)
				pathogenList.addAll(Parasitelist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
		}
		if (allBacterialist.size() > 0)
			pathogenAndpossibleList.addAll(allBacterialist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
		if (allMycobacterialist.size() > 0)
			pathogenAndpossibleList.addAll(allMycobacterialist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
		if (allMCRlist.size() > 0)
			pathogenAndpossibleList.addAll(allMCRlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
		if (allFungilist.size() > 0)
			pathogenAndpossibleList.addAll(allFungilist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
		if (allViruses_DNAlist.size() > 0)
			pathogenAndpossibleList.addAll(allViruses_DNAlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
		if (allViruses_RNAlist.size() > 0)
			pathogenAndpossibleList.addAll(allViruses_RNAlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
		if (allParasitelist.size() > 0)
			pathogenAndpossibleList.addAll(allParasitelist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));

		//清空
		Bacterialist = new ArrayList<>();
		Mycobacterialist = new ArrayList<>();
		MCRlist = new ArrayList<>();
		Fungilist = new ArrayList<>();
		Viruses_DNAlist = new ArrayList<>();
		Viruses_RNAlist = new ArrayList<>();
		Parasitelist = new ArrayList<>();
		if (backgrounds != null && backgrounds.size() > 0){
			for (SjwzxxDto background : backgrounds) {
				if("commensal".equals(background.getLabel())) {
					commensalList.add(background);
				}else{
					backgroundInfoList.add(background);
				}
				if("Bacteria".equals(background.getWzfl())){
					allMycobacterialist.add(background);
					Bacterialist.add(background);
				}
				if("Mycobacteria".equals(background.getWzfl())){
					allMycobacterialist.add(background);
					Mycobacterialist.add(background);
				}
				if("MCR".equals(background.getWzfl())){
					allMCRlist.add(background);
					MCRlist.add(background);
				}
				if("Fungi".equals(background.getWzfl())){
					allFungilist.add(background);
					Fungilist.add(background);
				}
				if("Viruses".equals(background.getWzfl()) && ("DNA".equals(background.getWzlx()) || "DNA病毒".equals(background.getWzlx()))){
					allViruses_DNAlist.add(background);
					Viruses_DNAlist.add(background);
				}
				if("Viruses".equals(background.getWzfl()) && ("RNA".equals(background.getWzlx()) || "RNA病毒".equals(background.getWzlx()))){
					allViruses_RNAlist.add(background);
					Viruses_RNAlist.add(background);
				}
				if("Parasite".equals(background.getWzfl())){
					allParasitelist.add(background);
					Parasitelist.add(background);
				}
			}
			if (Bacterialist.size() > 0)
				backgroundList.addAll(Bacterialist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (Mycobacterialist.size() > 0)
				backgroundList.addAll(Mycobacterialist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (MCRlist.size() > 0)
				backgroundList.addAll(MCRlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (Fungilist.size() > 0)
				backgroundList.addAll(Fungilist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (Viruses_DNAlist.size() > 0)
				backgroundList.addAll(Viruses_DNAlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (Viruses_RNAlist.size() > 0)
				backgroundList.addAll(Viruses_RNAlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
			if (Parasitelist.size() > 0)
				backgroundList.addAll(Parasitelist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
		}
		if (allBacterialist.size() > 0)
			list.addAll(allBacterialist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
		if (allMycobacterialist.size() > 0)
			list.addAll(allMycobacterialist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
		if (allMCRlist.size() > 0)
			list.addAll(allMCRlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
		if (allFungilist.size() > 0)
			list.addAll(allFungilist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
		if (allViruses_DNAlist.size() > 0)
			list.addAll(allViruses_DNAlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
		if (allViruses_RNAlist.size() > 0)
			list.addAll(allViruses_RNAlist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));
		if (allParasitelist.size() > 0)
			list.addAll(allParasitelist.stream().sorted(Comparator.comparing(SjwzxxDto::getIntDqs,Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList()));

		returnMap.put("possibles",possibleList);
		returnMap.put("pathogens",pathogenList);
		returnMap.put("backgrounds",backgroundList);
		returnMap.put("commensals",commensalList);
		returnMap.put("backgroundInfos",backgroundInfoList);//不包含label为commensal的物种
		returnMap.put("pathogensAndpossibles",pathogenAndpossibleList);
		returnMap.put("list",list);
		return returnMap;
	}

	/**
	 * 组装送检物种信息
	 * @param sjwzxxDto
	 * @return
	 */
	public SjwzxxDto packageSjwzxx(SjwzxxDto sjwzxxDto){
		//组装综合物种中文名，英文名，读取数
		if(StringUtil.isNotBlank(sjwzxxDto.getWzid())){
			sjwzxxDto.setZhwzzwm(sjwzxxDto.getWzzwm());
			sjwzxxDto.setZhwzywm(sjwzxxDto.getWzywm());
			sjwzxxDto.setZhdqs(sjwzxxDto.getDqs());
		}else{
			sjwzxxDto.setZhwzzwm(sjwzxxDto.getSzwm());
			sjwzxxDto.setZhwzywm(sjwzxxDto.getSm());
			sjwzxxDto.setZhdqs(sjwzxxDto.getSdds());
		}
		//组装综合物种分类："B"表示细菌，"B:G+"表示革兰阳性细菌，"B:G-"表示革兰阴性细菌，"P"表示支原体、衣原体或噬衣原体，
		//"F"表示真菌；"V"表示病毒；"I"表示其他，
		if("Bacteria".equals(sjwzxxDto.getWzfl())){//如果为细菌，set("B")
			sjwzxxDto.setZhwzfl("B");
			if("G-".equals(sjwzxxDto.getWzlx())){//物种类型如果为G-，set("B:G-")
				sjwzxxDto.setZhwzfl("B:G-");
			}else if("G+".equals(sjwzxxDto.getWzlx())){//物种类型如果为G-，set("B:G+")
				sjwzxxDto.setZhwzfl("B:G+");
			}
		}else if("Viruses".equals(sjwzxxDto.getWzfl())){//如果为病毒，set("V")
			sjwzxxDto.setZhwzfl("V");
		}else if("Fungi".equals(sjwzxxDto.getWzfl())){//如果为真菌，set("F")
			sjwzxxDto.setZhwzfl("F");
		}else if("MCR".equals(sjwzxxDto.getWzfl())){//如果为支原体、衣原体或噬衣原体，set("P")
			sjwzxxDto.setZhwzfl("P");
		}else{
			sjwzxxDto.setZhwzfl("I");
		}
		//属丰度
		if(StringUtil.isNotBlank(sjwzxxDto.getSfd())) {
			if(!"--".equals(sjwzxxDto.getSfd())) {
				try {
					BigDecimal sfd = new BigDecimal(sjwzxxDto.getSfd());
					BigDecimal sfd_compare = new BigDecimal("0.01");
					if(sfd.compareTo(sfd_compare)<= 0) {
						sjwzxxDto.setSfd("<0.01");
					}else {
						BigDecimal sfd_d = sfd.setScale(2, RoundingMode.HALF_UP);
						sjwzxxDto.setSfd(sfd_d.toString());
					}
				}catch(Exception e) {
					
				}
				//若该物种只传了属，则将属丰度放入综合相对丰度
				if(StringUtil.isBlank(sjwzxxDto.getWzid())){
					sjwzxxDto.setZhxdfd(sjwzxxDto.getSfd());
				}
			}
		}
		//相对丰度
		if(StringUtil.isNotBlank(sjwzxxDto.getXdfd())) {
			if(!"--".equals(sjwzxxDto.getXdfd())) {
				try {
					BigDecimal xdfd = new BigDecimal(sjwzxxDto.getXdfd());
					BigDecimal xdfd_compare = new BigDecimal("0.01");
					if(xdfd.compareTo(xdfd_compare)<= 0) {
						sjwzxxDto.setXdfd("<0.01");
					}else {
						BigDecimal xdfd_d = xdfd.setScale(2, RoundingMode.UP);
						sjwzxxDto.setXdfd(xdfd_d.toString());
					}
				}catch(Exception e) {
					
				}
				//若该物种传了属和种(wzid不为空)，则将种丰度放入综合相对丰度
				if(StringUtil.isNotBlank(sjwzxxDto.getWzid())){
					sjwzxxDto.setZhxdfd(sjwzxxDto.getXdfd());
				}
			}
		}
		//处理基因组覆盖度。取到的字符串不为空时：字符串转为float,转为保留两位小数的百分数
		if (StringUtil.isNotBlank(sjwzxxDto.getJyzfgd())){
			NumberFormat percentInstance = NumberFormat.getPercentInstance();
			percentInstance.setMinimumFractionDigits(2); //最小小数位数
			try {
				sjwzxxDto.setJyzfgd(  percentInstance.format(   Float.parseFloat(  sjwzxxDto.getJyzfgd()  )  ));
			}catch(Exception e) {
				
			}
		}
		return sjwzxxDto;
	}

	/**
	 * 补充数据用需删除
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getDtoByLrsj(SjxxDto sjxxDto){
		return dao.getDtoByLrsj(sjxxDto);
	}

	/**
	 * 补充数据用需删除
	 * @param sjxxDto
	 * @return
	 */
	public List<FjsqDto> getFjDtoByLrsj(SjxxDto sjxxDto){
		return dao .getFjDtoByLrsj(sjxxDto);
	}

	public List<Map<String,String>> getYbbhListByWbInfo(Map<String,Object> map){
		return dao.getYbbhListByWbInfo(map);
	}
	public List<Map<String,String>> getYbbhListByWbInfoPlus(Map<String,Object> map){
		return dao.getYbbhListByWbInfoPlus(map);
	}
	public List<Map<String,Object>> getJcxmFjInfoByYwids(List<String> ywids){
		List<Map<String,Object>> result = new ArrayList<>();
		Map<String,Object> sqlmap = new HashMap<>();
		sqlmap.put("ids",ywids);
		List<Map<String, String>> jcxmFjInfoByYwids = dao.getJcxmFjInfoByYwids(sqlmap);
		if (jcxmFjInfoByYwids!=null && jcxmFjInfoByYwids.size()>0) {
			DBEncrypt dbEncrypt = new DBEncrypt();
			for (int i = 0; i < ywids.size(); i++) {
				String ywid = ywids.get(i);
				String ybbh = "";
				String wbbm = "";
				Map<String,Object> resultmap = new HashMap<>();
				List<Map<String,String>> xminfoList = new ArrayList<>();
				for (int j = jcxmFjInfoByYwids.size() - 1; j >= 0; j--) {
					Map<String, String> info = jcxmFjInfoByYwids.get(j);
					if (info != null && ywid.equals(info.get("sjid"))) {
						try {
							Map<String,String> xminfo = new HashMap<>();
							String wjlj = dbEncrypt.dCode(info.get("wjlj"));
							File file = new File(wjlj);
							if (!file.exists()) {
								log.error("文件不存在：" + wjlj);
								break;
							}
							xminfo.put("item", info.get("jcxmmc"));
							xminfo.put("url", applicationurl + "/ws/file/limitDownload?fjid=" + info.get("fjid") + "&sign=" + URLEncoder.encode(commonService.getSign(),"utf-8"));
							xminfo.put("fileName", info.get("wjm"));
							xminfoList.add(xminfo);
							if (StringUtil.isBlank(ybbh)){
								ybbh = info.get("ybbh");
							}
							if (StringUtil.isBlank(wbbm)){
								wbbm = info.get("wbbm");
							}
							jcxmFjInfoByYwids.remove(j);
						} catch (Exception e) {
							log.error("文件解析存在问题：" + e.getMessage());
							break;
						}
					}
				}
				if (xminfoList!=null && xminfoList.size()>0){
					resultmap.put("code",ybbh);
					resultmap.put("wbbm",wbbm);
					resultmap.put("xminfoList",xminfoList);
					result.add(resultmap);
				}
			}
		}
		return result;
	}
	public List<Map<String,Object>> getJcxmFjInfoByYwidsPlus(List<Map<String, String>> ybInfoList,List<String> ywlxlikes,List<String> ywlxnotlikes){
		List<Map<String,Object>> result = new ArrayList<>();
		Map<String,Object> sqlmap = new HashMap<>();
		List<String> ywids = ybInfoList.stream().filter(item -> StringUtil.isNotBlank(item.get("id"))).map(a -> a.get("id")).collect(Collectors.toList());
		List<String> sjids = ybInfoList.stream().filter(item -> StringUtil.isBlank(item.get("id"))).map(a -> a.get("sjid")).collect(Collectors.toList());
		sqlmap.put("ywlxlikes",ywlxlikes);
		sqlmap.put("ywlxnotlikes",ywlxnotlikes);
		sqlmap.put("ids",ywids);
		if (!CollectionUtils.isEmpty(ywids)){
			List<Map<String, String>> jcxmFjInfoByYwids1 = dao.getJcxmFjInfoByYwidsPlus(sqlmap);
			if (jcxmFjInfoByYwids1!=null && jcxmFjInfoByYwids1.size()>0) {
				DBEncrypt dbEncrypt = new DBEncrypt();
				for (int i = 0; i < ywids.size(); i++) {
					String ywid = ywids.get(i);
					String ybbh = "";
					String wbbm = "";
					Map<String,Object> resultmap = new HashMap<>();
					List<Map<String,String>> xminfoList = new ArrayList<>();
					for (int j = jcxmFjInfoByYwids1.size() - 1; j >= 0; j--) {
						Map<String, String> info = jcxmFjInfoByYwids1.get(j);
						if (info != null && ywid.equals(info.get("id"))) {
							try {
								Map<String,String> xminfo = new HashMap<>();
								String wjlj = dbEncrypt.dCode(info.get("wjlj"));
								File file = new File(wjlj);
								if (!file.exists()) {
									log.error("文件不存在：" + wjlj);
									break;
								}
								xminfo.put("item", info.get("jcxmmc"));
								xminfo.put("url", applicationurl + "/ws/file/limitDownload?fjid=" + info.get("fjid") + "&sign=" + URLEncoder.encode(commonService.getSign(),"utf-8"));
								xminfo.put("fileName", info.get("wjm"));
								xminfoList.add(xminfo);
								if (StringUtil.isBlank(ybbh)){
									ybbh = info.get("ybbh");
								}
								if (StringUtil.isBlank(wbbm)){
									wbbm = info.get("wbbm");
								}
								jcxmFjInfoByYwids1.remove(j);
							} catch (Exception e) {
								log.error("文件解析存在问题：" + e.getMessage());
								break;
							}
						}
					}
					if (xminfoList!=null && xminfoList.size()>0){
						resultmap.put("code",ybbh);
						resultmap.put("wbbm",wbbm);
						resultmap.put("xminfoList",xminfoList);
						result.add(resultmap);
					}
				}
			}
		}
		if (!CollectionUtils.isEmpty(sjids)){
			sqlmap.put("ids",sjids);
			List<Map<String, String>> jcxmFjInfoByYwids2 = dao.getJcxmFjInfoByYwids(sqlmap);
			if (jcxmFjInfoByYwids2!=null && jcxmFjInfoByYwids2.size()>0) {
				DBEncrypt dbEncrypt = new DBEncrypt();
				for (int i = 0; i < sjids.size(); i++) {
					String ywid = sjids.get(i);
					String ybbh = "";
					String wbbm = "";
					Map<String,Object> resultmap = new HashMap<>();
					List<Map<String,String>> xminfoList = new ArrayList<>();
					for (int j = jcxmFjInfoByYwids2.size() - 1; j >= 0; j--) {
						Map<String, String> info = jcxmFjInfoByYwids2.get(j);
						if (info != null && ywid.equals(info.get("sjid"))) {
							try {
								Map<String,String> xminfo = new HashMap<>();
								String wjlj = dbEncrypt.dCode(info.get("wjlj"));
								File file = new File(wjlj);
								if (!file.exists()) {
									log.error("文件不存在：" + wjlj);
									break;
								}
								xminfo.put("item", info.get("jcxmmc"));
								xminfo.put("url", applicationurl + "/ws/file/limitDownload?fjid=" + info.get("fjid") + "&sign=" + URLEncoder.encode(commonService.getSign(),"utf-8"));
								xminfo.put("fileName", info.get("wjm"));
								xminfoList.add(xminfo);
								if (StringUtil.isBlank(ybbh)){
									ybbh = info.get("ybbh");
								}
								if (StringUtil.isBlank(wbbm)){
									wbbm = info.get("wbbm");
								}
								jcxmFjInfoByYwids2.remove(j);
							} catch (Exception e) {
								log.error("文件解析存在问题：" + e.getMessage());
								break;
							}
						}
					}
					if (xminfoList!=null && xminfoList.size()>0){
						resultmap.put("code",ybbh);
						resultmap.put("wbbm",wbbm);
						resultmap.put("xminfoList",xminfoList);
						result.add(resultmap);
					}
				}
			}
		}

		return result;
	}


	public List<Map<String,String>> getAndCheckYbxxInfo(HttpServletRequest request,boolean isPlus) throws BusinessException {
		String organ = request.getParameter("organ");
		String type = request.getParameter("type");
		String code = request.getParameter("code");
		String codes = request.getParameter("codes");
		String lastcode = request.getParameter("lastcode");
		String sign = request.getParameter("sign");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		DBEncrypt crypt = new DBEncrypt();
		Map<String, Object> map = checkSecurity(organ, type, StringUtil.isNotBlank(codes)?codes:code, lastcode, sign,false, crypt);
		Object iswbbmmust = map.get("iswbbmmust");
		if(!"0".equals(map.get("errorCode"))){
			throw new BusinessException(JSON.toJSONString(map));
		}
		//将codes转换成list
		List<String> ids = null;
		try {
			if (StringUtil.isNotBlank(codes)){
				ids = (List<String>) JSONArray.parse(codes);
			}
		} catch (Exception e) {
			map.put("status","fail");
			map.put("errorCode","codes解析错误，请检查codes格式!");
			throw new BusinessException(JSON.toJSONString(map));
		}
		//查询合作伙伴
		List<WbhbyzDto> wbhbyzDtos = wbhbyzService.getSjhbByCode(organ);
		if(wbhbyzDtos == null || wbhbyzDtos.size() == 0){
			map.put("status", "fail");
			map.put("errorCode", "未查询到伙伴权限！");
			throw new BusinessException(JSON.toJSONString(map));
		}
		String limit = "50";
		Object settting = redisUtil.hget("matridx_xtsz", "external.interface.settting");
		if (settting != null) {
			JSONObject setttingObj = JSONObject.parseObject(String.valueOf(settting));
			limit = setttingObj.getString("szz");
		}
		List<String> dbs = new ArrayList<>();
		for (int i = 0; i < wbhbyzDtos.size(); i++) {
			dbs.add(wbhbyzDtos.get(i).getHbmc());
		}
		Map<String, Object> sqlmap = new HashMap<>();
		sqlmap.put("limit",limit);
		sqlmap.put("dbs",dbs);
		if (ids != null && ids.size()>0){
			sqlmap.put("ids",ids);
			sqlmap.remove("limit");
		}else if (StringUtil.isNotBlank(code)){
			sqlmap.put("id",code);
		}else {
			sqlmap.put("bgrqstart",startDate);
			sqlmap.put("bgrqend",endDate);
		}
		if (StringUtil.isNotBlank(lastcode)){
			sqlmap.put("lastcode",lastcode);
		}
		sqlmap.put("iswbbmmust",iswbbmmust);
        return isPlus?getYbbhListByWbInfoPlus(sqlmap):getYbbhListByWbInfo(sqlmap);
	}

	public List<Map<String,String>> getAndCheckYbxxInfo(HttpServletRequest request,User user,boolean isPlus) throws BusinessException {
		String organ = user.getYhm();
		String type = request.getParameter("type");
		String code = request.getParameter("code");
		String codes = request.getParameter("codes");
		String lastcode = request.getParameter("lastcode");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		DBEncrypt crypt = new DBEncrypt();
		Map<String, Object> map = checkSecurity(organ, type, StringUtil.isNotBlank(codes)?codes:code, lastcode,false, crypt);
		Object iswbbmmust = map.get("iswbbmmust");
		if(!"0".equals(map.get("errorCode"))){
			throw new BusinessException(JSON.toJSONString(map));
		}
		//将codes转换成list
		List<String> ids = null;
		try {
			if (StringUtil.isNotBlank(codes)){
				ids = (List<String>) JSONArray.parse(codes);
			}
		} catch (Exception e) {
			map.put("status","fail");
			map.put("errorCode","codes解析错误，请检查codes格式!");
			throw new BusinessException(JSON.toJSONString(map));
		}
		//查询合作伙伴
		List<WbhbyzDto> wbhbyzDtos = wbhbyzService.getSjhbByCode(organ);
		if(wbhbyzDtos == null || wbhbyzDtos.size() == 0){
			map.put("status", "fail");
			map.put("errorCode", "未查询到伙伴权限！");
			throw new BusinessException(JSON.toJSONString(map));
		}
		String limit = "50";
		Object settting = redisUtil.hget("matridx_xtsz", "external.interface.settting");
		if (settting != null) {
			JSONObject setttingObj = JSONObject.parseObject(String.valueOf(settting));
			limit = setttingObj.getString("szz");
		}
		List<String> dbs = new ArrayList<>();
		for (int i = 0; i < wbhbyzDtos.size(); i++) {
			dbs.add(wbhbyzDtos.get(i).getHbmc());
		}
		Map<String, Object> sqlmap = new HashMap<>();
		sqlmap.put("limit",limit);
		sqlmap.put("dbs",dbs);
		if (ids != null && ids.size()>0){
			sqlmap.put("ids",ids);
			sqlmap.remove("limit");
		}else if (StringUtil.isNotBlank(code)){
			sqlmap.put("id",code);
		}else {
			sqlmap.put("bgrqstart",startDate);
			sqlmap.put("bgrqend",endDate);
		}
		if (StringUtil.isNotBlank(lastcode)){
			sqlmap.put("lastcode",lastcode);
		}
		sqlmap.put("iswbbmmust",iswbbmmust);
        return isPlus?getYbbhListByWbInfoPlus(sqlmap):getYbbhListByWbInfo(sqlmap);
	}

	@Override
	public List<Map<String, String>> getSjxxInfo(List<Map<String, String>> list) {
		return dao.getSjxxInfo(list);
	}

	@Override
	public Map<String, Object> disposeSaveFile(MultipartFile file) {
		Map<String, Object> result = new HashMap<>();
//		File toFile = null;
		if (file.getSize() <= 0) {
			result.put("status","fail");
			result.put("message","未获取到文件！");
		} else {
			String originalFilename = file.getOriginalFilename();
			String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
			String storePath = prefix + releaseFilePath + BusTypeEnum.IMP_FILE_JHNY_TEMEPLATE+"/"+ "UP"+
					DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
					DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
					DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
			boolean success = saveFile(file, t_name + originalFilename, storePath);
			if (!success){
				result.put("status","fail");
				result.put("message","文件保存失败！");
				return result;
			}
			String jclx = null;
			List<JcsjDto> list = redisUtil.lgetDto("All_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
			for (JcsjDto jcsjDto : list) {
				if ("072".equals(jcsjDto.getCsdm())){
					jclx = jcsjDto.getCsid();
					break;
				}
			}
			JhnyDisposeFileThreadSec jhnyDisposeFileThread = new JhnyDisposeFileThreadSec(storePath,t_name+originalFilename,this,jclx,sjwzxxService,sjnyxService,fjcfbService,wzysxxService,nyysxxService);
			jhnyDisposeFileThread.start();
			result.put("status","success");
			result.put("message","文件保存成功！");
		}
		return result;
	}	
	
	/**
	 * 重新执行结核耐药结果的流程，重新生信报告
	 * @return
	 */
	@Override
	public Map<String, Object> disposeSaveFileRetry() {
		
		Map<String, Object> result = new HashMap<>();
		
		String storePath = prefix + releaseFilePath + BusTypeEnum.IMP_FILE_JHNY_TEMEPLATE+"/"+ "UP"+
				DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
				DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
				DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		
		//查找现有问题件
		File file = new File(storePath);
		if(!file.isDirectory())
		{
			result.put("status","failed");
			result.put("message","指定路径不是文件夹！" + storePath);
			return result;
		}
		
		String[] filelist= file.list();
		if(filelist!=null && filelist.length > 0) {

			String jclx = null;
			List<JcsjDto> list = redisUtil.lgetDto("All_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
			for (JcsjDto jcsjDto : list) {
				if ("072".equals(jcsjDto.getCsdm())){
					jclx = jcsjDto.getCsid();
					break;
				}
			}
			
			for(int i=0;i<filelist.length;i++)
			{
				if(filelist[i].endsWith(".zip")) {
					JhnyDisposeFileThread jhnyDisposeFileThread = new JhnyDisposeFileThread(storePath,filelist[i],this,jclx,sjwzxxService,sjnyxService,fjcfbService,wzysxxService,nyysxxService);
					jhnyDisposeFileThread.start();
				}
			}
		}
		
		result.put("status","success");
		result.put("message","文件保存成功！");
			
		return result;
	}
	
	/**
	 * 恢复tBtngs处理
	 * @param path
	 * @return
	 */
	public boolean restoreDeal(String path) {
		
		String storePath = prefix + releaseFilePath + BusTypeEnum.IMP_FILE_JHNY_TEMEPLATE+"/"+ "UP"+
				DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
				DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
				DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		String jclx = null;
		List<JcsjDto> list = redisUtil.lgetDto("All_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
		for (JcsjDto jcsjDto : list) {
			if ("072".equals(jcsjDto.getCsdm())){
				jclx = jcsjDto.getCsid();
				break;
			}
		}
		JhnyDisposeFileThread jhnyDisposeFileThread = new JhnyDisposeFileThread(storePath,path,this,jclx,sjwzxxService,sjnyxService,fjcfbService,wzysxxService,nyysxxService);
		jhnyDisposeFileThread.start();
		return true;
	}

	private boolean saveFile(MultipartFile file, String originalFilename, String storePath) {
		boolean success = false;
		File toFile;
		InputStream ins = null;
		OutputStream os = null;
		try {
			/*获取文件原名称*/
			File file1 = new File(storePath);
			if (!file1.exists()) {
				file1.mkdirs();
			}
			toFile = new File(storePath+ "/" + originalFilename);
			ins = file.getInputStream();
			os = new FileOutputStream(toFile);
			int bytesRead;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			success = true;
		} catch (IOException e) {
			log.error("结核耐药上传保存报错："+e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				os.close();
				ins.close();
			} catch (IOException e) {
				log.error("结核耐药上传保存报错："+e.getMessage());
				e.printStackTrace();
			}
		}
		return success;
	}

	@Override
	public boolean sendReportFile(SjxxDto sjxxDto) throws BusinessException {
		SjxxDto dto = sjxxService.getDto(sjxxDto);
		return sendReportFile(sjxxDto,dto);
	}

	@Override
	public boolean mergeSendReportFile(SjxxDto sjxxDto,User user) throws BusinessException {
		SjxxDto dto = sjxxService.getDto(sjxxDto);
		return mergeSendReportFile(sjxxDto,dto,user);
	}

	@Override
	public boolean checkMessage(String message) {
		boolean result = true;
		try{
			String str = truncateWithStream(message,128);
			if(StringUtil.isNotBlank(str)) {
				Object obj = redisUtil.get("wsreceiveInspectInfo:" + str);
				if(obj!=null && StringUtil.isNotBlank(obj.toString()) && message.equals(obj.toString())){
					result = false;
				}
				redisUtil.set("wsreceiveInspectInfo:"+str,message,60*60);
			}
		}catch (Exception e) {
			log.error("去重判断异常:"+e.getMessage());
			return true;
		}
		return result;
	}

	/**
	 * @Description: 截取指定字符
	 * @param input
	 * @param maxChars
	 * @return java.lang.String
	 * @Author: 郭祥杰
	 * @Date: 2025/7/7 10:53
	 */
	private String truncateWithStream(String input, int maxChars) {
		if (input == null){
			return "";
		}
		return input.codePoints()
				.limit(maxChars)
				.collect(
						StringBuilder::new,
						StringBuilder::appendCodePoint,
						StringBuilder::append
				)
				.toString();
	}


	@Override
	public boolean mergeSendReportFile(SjxxDto sjxxDto,SjxxDto dto,User user) throws BusinessException {
		List<String> jclxs = sjxxDto.getJclxs();
		String tb_jclx=sjxxDto.getJclx();
		String tngs_jclx="";
		List<SjwzxxDto> sjnyList=new ArrayList<>();
		List<SjwzxxDto> sjwzxxList=new ArrayList<>();
		if (null != dto && StringUtil.isNotBlank(dto.getSjid()) && !CollectionUtils.isEmpty(jclxs)) {
			for(String jclx:jclxs){
				if(!jclx.equals(tb_jclx)){//分类出tb和tngs的检测类型，便于后续分类筛选
					tngs_jclx=jclx;
				}else{
					dto.setJclx(jclx);
					sjnyList.addAll(sjnyxService.getNyxMapBySjid(dto));
					sjwzxxList.addAll(sjxxService.selectWzxxBySjidAndJclx(dto));
				}
			}
			//从sjsygl获取TB文库对应的xmdm
			List<JcsjDto> list = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_PROJECT_TYPE.getCode());
			Optional<JcsjDto> tb_optional = list.stream().filter(item -> "TB".equals(item.getCsdm())).findFirst();
			if (!tb_optional.isPresent()) {
				throw new BusinessException("未获取到TB检测类型代码，请重新确认！");
			}
			SjsyglDto sjsyglDto=new SjsyglDto();
			sjsyglDto.setYwid(dto.getSjid());
			String xmdm="";
			List<SjsyglDto> sjsyglDtoList=sjsyglService.getSyxxViewByYwid(sjsyglDto);
			if(!CollectionUtils.isEmpty(sjsyglDtoList)){
				for(SjsyglDto t_sjsyglDto:sjsyglDtoList){
					if(tb_optional.get().getCsid().equals(t_sjsyglDto.getJclxid())){
						xmdm=t_sjsyglDto.getWksxbm();
						break;
					}
				}
			}
			if(StringUtil.isBlank(xmdm)){
				throw new BusinessException("未获取到TBT文库项目代码，请重新确认！");
			}
			final String finalTngsJclx = tngs_jclx;
			final String finalTbJclx = tb_jclx;
			final SjxxDto finalDto = dto;
			final SjxxDto finalSjxxDto = sjxxDto;
			final List<SjwzxxDto> final_sjnyList = sjnyList;
			final List<SjwzxxDto> final_sjwzxxList = sjwzxxList;
			final User finalUser = user;
			Thread thread = new Thread() {
				@Override
				public void run() {
					processData(finalDto,finalSjxxDto,final_sjwzxxList,final_sjnyList,finalTbJclx,finalTngsJclx,finalUser);
				}
			};
			thread.start();
			return true;
		}
		return false;
	}

	public void processData(SjxxDto dto,SjxxDto sjxxDto,List<SjwzxxDto> sjwzxxList,List<SjwzxxDto> sjnyList,String tb_jclx,String tngs_jclx,User user) {
		//1.1.获取信息对应设置的tngs+tbt合并发送时需要过滤的tngs结核物种id()，根据wzid和jclx过滤
		XxdyDto xxdyDto=new XxdyDto();
		xxdyDto.setDylxcsdm("TNGS_TB_SPECIES_FILTER");
		List<XxdyDto> xxdyDtos=xxdyService.getDtoList(xxdyDto);
		String filterWzList_tNGS="";
		if(!org.springframework.util.CollectionUtils.isEmpty(xxdyDtos)){
			for(XxdyDto xxdyDto_t:xxdyDtos){
				String[] xxdy=xxdyDto_t.getDyxx().split(",");
				for(int i=0;i<xxdy.length;i++){
					filterWzList_tNGS=filterWzList_tNGS+","+xxdy[i];
				}
			}
		}
		if(!CollectionUtils.isEmpty(sjnyList)){
			for(int i=sjnyList.size()-1;i>=0;i--){
				if(!"1".equals(sjnyList.get(i).getSfhb())){
					sjnyList.remove(i);
				}
			}
		}
		//1.3.过滤不发送物种，判断TBT检出物种是否包含
		if(!CollectionUtils.isEmpty(sjwzxxList)){
			for(int i=sjwzxxList.size()-1;i>=0;i--){
				//过滤是否汇报为0的数据
				if(!"1".equals(sjwzxxList.get(i).getSfhb())){
					sjwzxxList.remove(i);
				}
			}
		}
		//过滤wzid为null的数据
		if(!CollectionUtils.isEmpty(sjwzxxList)){
			for(int i=sjwzxxList.size()-1;i>=0;i--){
				if(StringUtil.isBlank(sjwzxxList.get(i).getWzid())){
					sjwzxxList.remove(i);
				}
			}
		}
		boolean ntm=false;//该部分判断若有改动需要前后端一起改
		boolean tb=false;
		for(SjwzxxDto sjwzxxDto:sjwzxxList){
			if("36809".equals(sjwzxxDto.getWzid()) && !"[]".equals(sjwzxxDto.getNtm()) && StringUtil.isNotBlank(sjwzxxDto.getNtm())){//若没检出该物种，则ntm中耐药不发送
				ntm=true;
			}
			if(!"[]".equals(sjwzxxDto.getTb()) && StringUtil.isNotBlank(sjwzxxDto.getTb())){
				tb=true;
			}
		}
		if(!CollectionUtils.isEmpty(sjnyList)){
			for(int i=sjnyList.size()-1;i>=0;i--){
				Map<String,Object> map = (Map<String, Object>)JSON.parseObject(sjnyList.get(i).getJson(), Map.class);
				if("ntm".equals(map.get("分类")) && !ntm){//过滤ntm
					sjnyList.remove(i);
					continue;
				}
				if("tb".equals(map.get("分类")) && !tb){//过滤tb
					sjnyList.remove(i);
					continue;
				}
				//处理耐药基因名称
				if(StringUtil.isNotBlank(sjnyList.get(i).getTbjg())){//突变结果不为空，截取_前面的内容作为突变基因内容
					sjnyList.get(i).setTbjy(sjnyList.get(i).getTbjg().split("_")[0]);
				}
			}
		}

		//获取tNGS结果
		String dynr="";
		JkdymxDto jkdymxDto=new JkdymxDto();
		List<String> dydzs=new ArrayList<>();
		dydzs.add("/ws/pathogen/receiveInspectionGenerateReport");
		dydzs.add("/ws/pathogen/receiveInspectionGenerateReportSec");
		jkdymxDto.setDydzs(dydzs);
		jkdymxDto.setYwid(dto.getYbbh());
		jkdymxDto.setDyfl(InvokingTypeEnum.INVOKING_INSPECTION.getCode());
		jkdymxDto.setDyzfl(InvokingChildTypeEnum.INVOKING_CHILD_REPORT.getCode());
		jkdymxDto.setZywid(tngs_jclx);//只查tngs的调用结果
		List<JkdymxDto> jkdymxDtos = jkdymxService.selectReportInfo(jkdymxDto);
		if(!CollectionUtils.isEmpty(jkdymxDtos)){
			for(JkdymxDto t_jkdymxDto:jkdymxDtos){
				WeChatInspectionReportModel inspinfo = JSONObject.parseObject(t_jkdymxDto.getNr(), WeChatInspectionReportModel.class);
				if(inspinfo.getSample_id().contains(dto.getNbbm() + "-tNGS")){
					dynr=t_jkdymxDto.getNr();
					break;
				}
			}
			if(StringUtil.isNotBlank(dynr)){
				WeChatInspectionReportModel inspinfo = JSONObject.parseObject(dynr, WeChatInspectionReportModel.class);
				inspinfo.setProject_type("IMP_REPORT_SEQ_TNGS_G");//设置指定模板
				//1.反向转换,物种信息,过滤TNGS中与信息对应设置相关的物种
				List<WeChatInspectionSpeciesModel> possible = filterWzxx(inspinfo.getPossible(),filterWzList_tNGS);
				List<WeChatInspectionSpeciesModel> pathogen = filterWzxx(inspinfo.getPathogen(),filterWzList_tNGS);
				List<WeChatInspectionSpeciesModel> background = filterWzxx(inspinfo.getBackground(),filterWzList_tNGS);
				inspinfo=filterCkwx(inspinfo,filterWzList_tNGS);//过滤参考文献
				List<SjwzxxDto> tbt_drug_resist_gene = new ArrayList<>();//耐药基因注释
				List<SjwzxxDto> tbt_drug_resistance_stat = new ArrayList<>();//送检耐药性
				List<CkwxglDto> tbt_ckwxglDtos = new ArrayList<>();
				for(SjwzxxDto sjwzxxDto:sjwzxxList){
					CkwxglDto ckwxglDto=new CkwxglDto();
					ckwxglDto.setWzid(sjwzxxDto.getWzid());
					ckwxglDto.setYblx(dto.getYblx());
					tbt_ckwxglDtos.add(ckwxglDto);
					if("possible".equals(sjwzxxDto.getJglx())){
						possible.add(dealWzxxToWeChatInspectionSpeciesModel(sjwzxxDto));
					}else if("pathogen".equals(sjwzxxDto.getJglx())){
						pathogen.add(dealWzxxToWeChatInspectionSpeciesModel(sjwzxxDto));
					}else if("background".equals(sjwzxxDto.getJglx())){
						background.add(dealWzxxToWeChatInspectionSpeciesModel(sjwzxxDto));
					}
				}
				//2.反向转换,耐药信息
				tbt_drug_resist_gene.addAll(sjnyList);//TBT耐药基因注释
				tbt_drug_resistance_stat.addAll(sjnyList);//TBT耐药性
				inspinfo.setTbt_drug_resist_gene(tbt_drug_resist_gene);
				inspinfo.setTbt_drug_resistance_stat(tbt_drug_resistance_stat);

				//3.替换审核人，检验人
				//检验人员审核人员电子签名
				if (StringUtil.isNotBlank(sjxxDto.getJyr())){
					String[] jyr_split = sjxxDto.getJyr().split("-");
					inspinfo.setJyry(jyr_split[1]);
				}
				//审核人员电子签名
				if (StringUtil.isNotBlank(sjxxDto.getShr())){
					String[] shr_split = sjxxDto.getShr().split("-");
					inspinfo.setShry(shr_split[1]);
				}
				inspinfo.setHbfs("1");//合并发送标记

				//4.整合文献
				if(!CollectionUtils.isEmpty(tbt_ckwxglDtos)){
					List<WechatReferencesModel> papers=inspinfo.getPapers();
					List<CkwxglDto> ckwxglDtos=ckwxglService.getListDtoByYblxAndWzid(tbt_ckwxglDtos);
					inspinfo.setPapers(dealCkwxglToWeChatInspectionReferencesModel(ckwxglDtos,dto,papers));
				}


				RestTemplate restTemplate = new RestTemplate();
				MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
				paramMap.add("sample_result", JSON.toJSONString(inspinfo));
				String url=applicationurl+"/ws/pathogen/receiveInspectionGenerateReportSec?userid="+user.getYhm();
				restTemplate.postForObject(url, paramMap, Map.class);
			}
		}

	}

	public List<WechatReferencesModel> dealCkwxglToWeChatInspectionReferencesModel(List<CkwxglDto> ckwxglDtos,SjxxDto dto,List<WechatReferencesModel> papers){
		if(!CollectionUtils.isEmpty(ckwxglDtos)){
			for(CkwxglDto ckwxglDto:ckwxglDtos){
				WechatReferencesModel wechatReferencesModel=new WechatReferencesModel();
				wechatReferencesModel.setAuthor(ckwxglDto.getWzzz());
				wechatReferencesModel.setJournal(ckwxglDto.getQkxx());
				wechatReferencesModel.setSpecies_taxid(ckwxglDto.getWzid());
				wechatReferencesModel.setId(ckwxglDto.getWxid());
				wechatReferencesModel.setTitle(ckwxglDto.getWzbt());
				//0和other是其他，1和PB是外周血，2和CSF是脑脊液，3和BALF是肺泡灌洗液
				if("XXX".equals(dto.getYblxdm())){
					wechatReferencesModel.setSample_type("other");
				}
				if("B".equals(dto.getYblxdm())){
					wechatReferencesModel.setSample_type("1");
				}
				if("N".equals(dto.getYblxdm())){
					wechatReferencesModel.setSample_type("2");
				}
				if("L".equals(dto.getYblxdm())){
					wechatReferencesModel.setSample_type("3");
				}
				papers.add(wechatReferencesModel);
			}
		}
		return papers;
	}

	public WeChatInspectionReportModel filterCkwx(WeChatInspectionReportModel weChatInspectionReportModel,String filterWzList_tNGS){
		if(!CollectionUtils.isEmpty(weChatInspectionReportModel.getPapers())){
			for(int i=weChatInspectionReportModel.getPapers().size()-1;i>=0;i--){
				if(filterWzList_tNGS.contains(weChatInspectionReportModel.getPapers().get(i).getSpecies_taxid()) && StringUtil.isNotBlank(weChatInspectionReportModel.getPapers().get(i).getSpecies_taxid())){
					weChatInspectionReportModel.getPapers().remove(i);
				}
			}
		}
		return weChatInspectionReportModel;
	}

	public List<WeChatInspectionSpeciesModel> filterWzxx(List<WeChatInspectionSpeciesModel> weChatInspectionSpeciesModels,String filterWzList_tNGS){
		if(!CollectionUtils.isEmpty(weChatInspectionSpeciesModels)){
			for(int i=weChatInspectionSpeciesModels.size()-1;i>=0;i--){
				if(filterWzList_tNGS.contains(weChatInspectionSpeciesModels.get(i).getTaxid()) && StringUtil.isNotBlank(weChatInspectionSpeciesModels.get(i).getTaxid())){
					weChatInspectionSpeciesModels.remove(i);
				}
			}
		}
		return weChatInspectionSpeciesModels;
	}



//	public WeChatInspectionResistanceModel dealSjnyxToWeChatInspectionResistanceModel(SjnyxDto sjnyxDto) {
//		// 保存送检耐药性
//		WeChatInspectionResistanceModel weChatInspectionResistanceModel = new WeChatInspectionResistanceModel();
//		weChatInspectionResistanceModel.setDrug_class(sjnyxDto.getYp());
//		weChatInspectionResistanceModel.setGene_count(sjnyxDto.getDs());
//		weChatInspectionResistanceModel.setMain_mechanism(sjnyxDto.getJl());
//		weChatInspectionResistanceModel.setRef_species(sjnyxDto.getXgwz());
//		weChatInspectionResistanceModel.setGenes(sjnyxDto.getJyfx());
//		weChatInspectionResistanceModel.setReads(sjnyxDto.getXls());
//		weChatInspectionResistanceModel.setOrigin_species(sjnyxDto.getQyz());
//		return weChatInspectionResistanceModel;
//	}

	/**
	 * 将sjwzxxDto转换为WeChatInspectionSpeciesModel
	 * @param sjwzxxDto
	 * @return
	 */
	public WeChatInspectionSpeciesModel dealWzxxToWeChatInspectionSpeciesModel(SjwzxxDto sjwzxxDto) {
		WeChatInspectionSpeciesModel weChatInspectionSpeciesModel = new WeChatInspectionSpeciesModel();
		weChatInspectionSpeciesModel.setGenus_name(sjwzxxDto.getSm());
		weChatInspectionSpeciesModel.setRpm(sjwzxxDto.getRpm());
		weChatInspectionSpeciesModel.setRef_length(sjwzxxDto.getZcd());
		weChatInspectionSpeciesModel.setReads_count(sjwzxxDto.getReads());
		weChatInspectionSpeciesModel.setGenus_taxid(sjwzxxDto.getSid());
		weChatInspectionSpeciesModel.setGenus_abundance(sjwzxxDto.getSfd());
		weChatInspectionSpeciesModel.setTaxid(sjwzxxDto.getWzid());
		weChatInspectionSpeciesModel.setSp_type(sjwzxxDto.getWzfl());
		weChatInspectionSpeciesModel.setComment(sjwzxxDto.getWzzs());
		weChatInspectionSpeciesModel.setAbundance(sjwzxxDto.getXdfd());
		weChatInspectionSpeciesModel.setRank_code(sjwzxxDto.getFldj());
		weChatInspectionSpeciesModel.setLibrary_type(sjwzxxDto.getHslx());
		weChatInspectionSpeciesModel.setCover_length(sjwzxxDto.getFgcd());
		weChatInspectionSpeciesModel.setGenus_cn_name(sjwzxxDto.getSzwm());
		weChatInspectionSpeciesModel.setSpecial_genus(sjwzxxDto.getTsfl());
		weChatInspectionSpeciesModel.setSpecies_category(sjwzxxDto.getWzlx());
		weChatInspectionSpeciesModel.setGenus_reads_accum(sjwzxxDto.getSdds());
		weChatInspectionSpeciesModel.setName(sjwzxxDto.getWzywm());
		weChatInspectionSpeciesModel.setCn_name(sjwzxxDto.getWzzwm());
		weChatInspectionSpeciesModel.setCoverage(sjwzxxDto.getJyzfgd());
		weChatInspectionSpeciesModel.setVirus_type(sjwzxxDto.getWzfllx());
		weChatInspectionSpeciesModel.setBjtppath(sjwzxxDto.getBjtppath());
		weChatInspectionSpeciesModel.setClade(sjwzxxDto.getXgfx());
		weChatInspectionSpeciesModel.setYgz(sjwzxxDto.getYgz());
		weChatInspectionSpeciesModel.setJglxmc(sjwzxxDto.getJglxmc());
		weChatInspectionSpeciesModel.setProject_type(sjwzxxDto.getProject_type());
		weChatInspectionSpeciesModel.setLabel(sjwzxxDto.getLabel());
		weChatInspectionSpeciesModel.setDetectType("TBT");//区分此为TBT项目的检出物种
		//是否高亮
		if (StringUtils.isNotBlank(sjwzxxDto.getSfgl()) && ("1").equals(sjwzxxDto.getSfgl())) {
			weChatInspectionSpeciesModel.setHighlight("true");
		}else {
			weChatInspectionSpeciesModel.setHighlight("false");
		}
		return weChatInspectionSpeciesModel;
	}

	@Override
	public boolean sendReportFile(SjxxDto sjxxDto,SjxxDto dto) throws BusinessException {
		log.error("结核耐药 sendReportFile 组装报告信息-----开始");
		String jclx = sjxxDto.getJclx();
		String jczlx = sjxxDto.getJczlx();
		if (null != dto && StringUtil.isNotBlank(dto.getSjid()) && StringUtil.isNotBlank(jclx)){
			dto.setJclx(jclx);
			dto.setJczlx(jczlx);
			List<SjwzxxDto> filterWzList=new ArrayList<>();
			List<SjnyxDto> filterNyxList=new ArrayList<>();
			List<SjnyxDto> sjnyxList = sjnyxService.getNyxListBySjid(dto);
            List<SjnyxDto> sjnyxYpList = sjnyxService.getNyxYpListBySjid(dto);
			List<SjwzxxDto> sjwzxxList = sjxxService.selectWzxxBySjidAndJclx(dto);
			//1.2.获取页面中人为选择不汇报的物种(只过滤TBT的)
			if(!CollectionUtils.isEmpty(sjnyxList)){
				for(int i=sjnyxList.size()-1;i>=0;i--){
					if(!"1".equals(sjnyxList.get(i).getSfhb())){
						sjnyxList.remove(i);
					}else{
						SjwzxxDto sjnyx_wzDto = new SjwzxxDto();
						sjnyx_wzDto.setSjid(sjnyxList.get(i).getSjid());
						sjnyx_wzDto.setSjnyxid(sjnyxList.get(i).getSjnyxid());
						sjnyx_wzDto.setXh(sjnyxList.get(i).getXh());
						sjnyx_wzDto.setJy(sjnyxList.get(i).getJy());
						sjnyx_wzDto.setYp(sjnyxList.get(i).getYp());
						sjnyx_wzDto.setJyfx(sjnyxList.get(i).getJyfx());
						sjnyx_wzDto.setXls(sjnyxList.get(i).getXls());
						sjnyx_wzDto.setQyz(sjnyxList.get(i).getQyz());
						sjnyx_wzDto.setJclx(sjnyxList.get(i).getJclx());
						sjnyx_wzDto.setJcxmid(sjnyxList.get(i).getJcxmid());
						sjnyx_wzDto.setSfhb(sjnyxList.get(i).getSfhb());
						sjnyx_wzDto.setTbjy(sjnyxList.get(i).getTbjy());
						sjnyx_wzDto.setTbjg(sjnyxList.get(i).getTbjg());
						sjnyx_wzDto.setNyx(sjnyxList.get(i).getNyx());
						sjnyx_wzDto.setTbsd(sjnyxList.get(i).getTbsd());
						sjnyx_wzDto.setTbpl(sjnyxList.get(i).getTbpl());
						sjnyx_wzDto.setBjtb(sjnyxList.get(i).getBjtb());
						sjnyx_wzDto.setJson(sjnyxList.get(i).getJson());
						filterWzList.add(sjnyx_wzDto);
					}
				}
			}
			//1.3.过滤不发送物种，判断TBT检出物种是否包含
			// "NTM"中结果包含【"Species" : "Mycobacteroides_abscessus"，..."taxid" : "36809"】时显示耐药结果文件
			if(!CollectionUtils.isEmpty(sjwzxxList)){
				for(int i=sjwzxxList.size()-1;i>=0;i--){
					if(!"1".equals(sjwzxxList.get(i).getSfhb())){
						sjwzxxList.remove(i);
					}
				}
			}
			boolean ntm=false;//该部分判断若有改动需要前后端一起改
			boolean tb=false;
			for(SjwzxxDto sjwzxxDto:sjwzxxList){
				if("36809".equals(sjwzxxDto.getWzid()) && !"[]".equals(sjwzxxDto.getNtm()) && StringUtil.isNotBlank(sjwzxxDto.getNtm())){//若没检出该物种，则ntm中耐药不发送
					ntm=true;
				}
				if(!"[]".equals(sjwzxxDto.getTb()) && StringUtil.isNotBlank(sjwzxxDto.getTb())){
					tb=true;
				}
			}
			if(!CollectionUtils.isEmpty(filterWzList)){
				for(int i=(filterWzList.size()-1);i>=0;i--){
					Map<String,Object> map = (Map<String, Object>)JSON.parseObject(filterWzList.get(i).getJson(), Map.class);
					if("ntm".equals(map.get("分类")) && !ntm){//过滤ntm
						filterWzList.remove(i);
						sjnyxList.remove(i);
						continue;
					}
					if("tb".equals(map.get("分类")) && !tb){//过滤tb
						filterWzList.remove(i);
						sjnyxList.remove(i);
						continue;
					}
					//处理耐药基因名称
					if(StringUtil.isNotBlank(filterWzList.get(i).getTbjg())){//突变结果不为空，截取_前面的内容作为突变基因内容
						filterWzList.get(i).setTbjy(filterWzList.get(i).getTbjg().split("_")[0]);
						sjnyxList.get(i).setTbjy(sjnyxList.get(i).getTbjg().split("_")[0]);
					}
				}
			}
			// 药品这边也需要删除一些耐药，否则会跟耐药结果不一致
			if(!CollectionUtils.isEmpty(sjnyxYpList)){
				for(int i=(sjnyxYpList.size()-1);i>=0;i--){
					Map<String,Object> map = (Map<String, Object>)JSON.parseObject(sjnyxYpList.get(i).getJson(), Map.class);
					if("ntm".equals(map.get("分类")) && !ntm){//过滤ntm
						sjnyxYpList.remove(i);
						continue;
					}
					if("tb".equals(map.get("分类")) && !tb){//过滤tb
						sjnyxYpList.remove(i);
						continue;
					}
					//处理耐药基因名称
					if(StringUtil.isNotBlank(sjnyxYpList.get(i).getTbjg())){//突变结果不为空，截取_前面的内容作为突变基因内容
						sjnyxYpList.get(i).setTbjy(sjnyxYpList.get(i).getTbjg().split("_")[0]);
					}
				}
			}
 			sendfinalReport(sjxxDto,dto,jclx,jczlx,filterWzList,sjnyxYpList,sjwzxxList);
		}
		return true;
	}

	/**
	 * 发送最终报告
	 * @param sjxxDto
	 * @param dto
	 * @param jclx
	 * @param sjnyList
	 * @param sjwzxxList
	 * @return
	 * @throws BusinessException
	 */
	public boolean sendfinalReport(SjxxDto sjxxDto,SjxxDto dto,String jclx,String jczlx,List<SjwzxxDto> sjnyList,List<SjnyxDto> nyxLis,List<SjwzxxDto> sjwzxxList)throws BusinessException{
		String ywlxToWord = "";
		String ywlxToPDF = "";
		List<JcsjDto> list = redisUtil.lgetDto("All_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
		for (JcsjDto jcsjDto : list) {
			if (jclx.equals(jcsjDto.getCsid())){
				//生成报告的业务类型（WORD）
				ywlxToWord=jcsjDto.getCskz3()+"_"+jcsjDto.getCskz1()+"_"+"WORD";
				//生成报告的业务类型（PDF）
				ywlxToPDF=jcsjDto.getCskz3()+"_"+jcsjDto.getCskz1();
				break;
			}
		}

		if (null != dto && StringUtil.isNotBlank(dto.getSjid()) && StringUtil.isNotBlank(jclx)){
			dto.setJclx(jclx);
			dto.setJczlx(jczlx);
			Map<String,WeChatInspectionSpeciesModel> taxFlgMap = new HashMap<>();
			Map<String,Object> hashMap=sjxxService.getMapBySjid(dto.getSjid(),jclx,jczlx);
			if (null == hashMap.get("bzfwjlj")){
				throw new BusinessException("","当前合作伙伴的检测项目没有设置报告模板或者没有设置相应文件！");
			}
			if (CollectionUtils.isNotEmpty(sjwzxxList)){
				for (int i = sjwzxxList.size()-1; i >= 0 ; i--) {
					if (StringUtil.isBlank(sjwzxxList.get(i).getWzid())){
						sjwzxxList.remove(i);
					}else {
						WeChatInspectionSpeciesModel t_model = new WeChatInspectionSpeciesModel();
						t_model.setTaxid(sjwzxxList.get(i).getWzid());
						t_model.setWzbgpz("{color:FF0000}阳性");
						t_model.setWzbgpzall("{color:FF0000}阳性");
						taxFlgMap.put(sjwzxxList.get(i).getWzid()+"_flg",t_model);
					}
				}
			}
			FjcfbDto fjcfbDto=new FjcfbDto();
			fjcfbDto.setYwid(dto.getSjid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_FILE_JHNY_TEMEPLATE.getCode());
			List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			DBEncrypt p = new DBEncrypt();
			for (FjcfbDto fjcfbDto1 : fjcfbDtos) {
				if (StringUtil.isNotBlank(fjcfbDto1.getWjlj())){
					fjcfbDto1.setWjlj(p.dCode(fjcfbDto1.getWjlj()));
				}
			}
			// 保存送检报告说明
			SjbgsmDto sjbgsmDto = new SjbgsmDto();
			sjbgsmDto.setSjid(dto.getSjid());
			sjbgsmDto.setJclx(jclx);
			sjbgsmDto.setJczlx(jczlx);
			//检验人员审核人员电子签名
			if (StringUtil.isNotBlank(sjxxDto.getJyr())){
				String[] jyr_split = sjxxDto.getJyr().split("-");
				hashMap.put("jyryzsxm",jyr_split[2]);
				sjbgsmDto.setJyry(jyr_split[0]);
				FjcfbDto jyry = new FjcfbDto();
				jyry.setYwlx(BusTypeEnum.IMP_SIGNATURE.getCode());
				jyry.setYwid(jyr_split[1]);
				//查询附件文件路径
				List<FjcfbDto> fjcfbDtoList = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(jyry);
				if (fjcfbDtoList == null || fjcfbDtoList.size() <=0){
					throw new BusinessException("msg","检验人缺少电子签名！");
				}else {
					//检验人员
					String imageFilePath = p.dCode(fjcfbDtoList.get(0).getWjlj());
					hashMap.put("jyr_Pic",imageFilePath);
					log.error(" 检验人电子签名文件名---------> "+fjcfbDtoList.get(0).getWjm());
				}
			}
			//审核人员电子签名
			if (StringUtil.isNotBlank(sjxxDto.getShr())){
				String[] shr_split = sjxxDto.getShr().split("-");
				hashMap.put("shryzsxm",shr_split[2]);
				sjbgsmDto.setShry(shr_split[0]);
				FjcfbDto jyry = new FjcfbDto();
				jyry.setYwlx(BusTypeEnum.IMP_SIGNATURE.getCode());
				jyry.setYwid(shr_split[1]);
				//查询附件文件路径
				List<FjcfbDto> fjcfbDtoList = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(jyry);
				if (fjcfbDtoList == null || fjcfbDtoList.size() <=0){
					throw new BusinessException("msg","审核人缺少电子签名！");
				}else {
					//审核人员
					String imageFilePath = p.dCode(fjcfbDtoList.get(0).getWjlj());
					hashMap.put("shr_Pic",imageFilePath);
					log.error(" 审核人电子签名文件名---------> "+fjcfbDtoList.get(0).getWjm());
				}
			}
			saveSjbgsmInfo(sjwzxxList, sjbgsmDto);
			hashMap.put("sjbgsmDto", sjbgsmDto);
			hashMap.put("jyjg", sjbgsmDto.getJyjg());
			hashMap.put("Sjwzxx_List",sjwzxxList);
			//物种标记
			hashMap.put("taxFlg_List",taxFlgMap);
			
			hashMap.put("Sjnyx_List",sjnyList);
			hashMap.put("nyx_List",nyxLis);
			hashMap.put("Fj_List",fjcfbDtos);
			hashMap.put("ywlx",ywlxToWord);
			hashMap.put("ywlxToPDF",ywlxToPDF);
			hashMap.put("jclx",hashMap.get("jcxmcs1"));
			hashMap.put("mcpjdqbg_cskz1",hashMap.get("jcxmcs1"));
			hashMap.put("report_date", DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
			Map<String, String> map= new HashMap<>();
			map.put("sjid", dto.getSjid());
			map.put("jglx", null);
			map.put("jclx", jclx);
			map.put("jczlx", jczlx);
			String jcjgmc="";
			StringBuffer pathogen_comment_new=new StringBuffer();
			List<SjwzxxDto> pathogen_comment_list=new ArrayList();
			if(sjwzxxList!=null && sjwzxxList.size()>0) {
				for (int i = 1; i < sjwzxxList.size() + 1; i++) {
					if (StringUtil.isNotBlank(sjwzxxList.get(i - 1).getWzid())) {
						if (i > 1 && i != sjwzxxList.size() + 1) {
							pathogen_comment_new.append("{br}{\\n}");
						} else {
							pathogen_comment_new.append("\n");
						}
						jcjgmc = jcjgmc + "," + ("--".equals(sjwzxxList.get(i - 1).getWzzwm()) || StringUtil.isBlank(sjwzxxList.get(i - 1).getWzid()) ? sjwzxxList.get(i - 1).getSm() : sjwzxxList.get(i - 1).getWzzwm());
						pathogen_comment_new.append("{br}{b}" + i + "." + sjwzxxList.get(i - 1).getWzzwm() + ":{br}" + sjwzxxList.get(i - 1).getWzzs() + "。");

						SjwzxxDto sjwzzsxm = new SjwzxxDto();
						sjwzzsxm.setWzid(sjwzxxList.get(i - 1).getWzid());
						sjwzzsxm.setWzzwm(sjwzxxList.get(i - 1).getWzzwm());
						sjwzzsxm.setWzzs(sjwzxxList.get(i - 1).getWzzs());
						pathogen_comment_list.add(sjwzzsxm);
					} else {
						jcjgmc = jcjgmc + "," + (sjwzxxList.get(i - 1).getSzwm());

						SjwzxxDto sjwzzsxm = new SjwzxxDto();
						sjwzzsxm.setWzzwm(sjwzxxList.get(i - 1).getSzwm());
						sjwzzsxm.setWzzs(sjwzxxList.get(i - 1).getSm());
						pathogen_comment_list.add(sjwzzsxm);
					}
				}
			}
			if(StringUtil.isNotBlank(jcjgmc)) {
				jcjgmc = jcjgmc.substring(1);
			}
			sjxxDto.setJcjg(jcjgmc!=null && jcjgmc.length()>256?jcjgmc.substring(0,256):jcjgmc);
			hashMap.put("pathogen_comment_new", pathogen_comment_new.toString());
			hashMap.put("pathogen_comment_List", pathogen_comment_list);
			reportTemplateInformation(new WeChatInspectionReportModel(),hashMap,dto);
			WordTemeplateThread thread=new WordTemeplateThread(hashMap,fjcfbService,amqpTempl,DOC_OK,FTP_URL,sjxxService);
			thread.start();
			if (null != hashMap.get("bzfwjlj2")){
				Map<String, Object> newmap = new HashMap<>(hashMap);
				newmap.put("bzfwjlj",hashMap.get("bzfwjlj2"));
				newmap.put("bzfwjm",hashMap.get("bzfwjm2"));
				newmap.put("bgmbcskz1",hashMap.get("bgmb2cskz1"));
				newmap.put("cskz2",hashMap.get("bgmb2cskz2"));
				newmap.put("ywlx","REPORT_"+hashMap.get("ywlx"));
				newmap.put("ywlxToPDF","REPORT_"+hashMap.get("ywlxToPDF"));
				newmap.put("bfwjbj","1");//备份文件标记
				//报告模板信息
				reportTemplateInformation(new WeChatInspectionReportModel(),newmap,dto);
				WordTemeplateThread thread_2=new WordTemeplateThread(newmap,fjcfbService,amqpTempl,DOC_OK,FTP_URL,sjxxService);
				thread_2.start();
			}


			sjxxDto.setBgrq(DateUtils.getCustomFomratCurrentDate(null));
			boolean result = sjxxService.updateReport(sjxxDto)!=0;
			if (!result){
				throw new BusinessException("msg","更新报告日期失败！");
			}
			if (StringUtil.isNotBlank(menuurl)){
				RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.INSP_MOD.getCode() + JSONObject.toJSONString(sjxxDto));
			}
		}
		return true;
	}

	/**
	 * 保存报告说明信息
	 * @param sjwzxxList
	 * @param sjbgsmDto
	 * @throws BusinessException
	 */
	private void saveSjbgsmInfo(List<SjwzxxDto> sjwzxxList, SjbgsmDto sjbgsmDto) throws BusinessException {
		sjbgsmDto.setBgrq(DateUtils.getCustomFomratCurrentDate(null));
		String gzzb = "";
		for (SjwzxxDto sjwzxxDto : sjwzxxList) {
			if (StringUtil.isNotBlank(sjwzxxDto.getWzid())){
				gzzb += ","+sjwzxxDto.getWzzwm();
			}
		}

		sjbgsmDto.setJyjg(StringUtil.isNotBlank(gzzb)?"1":"0");
		sjbgsmDto.setGgzdzb(StringUtil.isNotBlank(gzzb)?gzzb.substring(1):"");

		List<SjbgsmDto> dtos = sjbgsmService.selectBySjbgsmDto(sjbgsmDto);
		if (CollectionUtils.isNotEmpty(dtos) && StringUtil.isNotBlank(dtos.get(0).getScbgrq())){
			sjbgsmDto.setScbgrq(dtos.get(0).getScbgrq());
		}else{
			sjbgsmDto.setScbgrq(DateUtils.getCustomFomratCurrentDate(null));
		}
		sjbgsmService.deleteBySjbgsmDto(sjbgsmDto);
		boolean success = sjbgsmService.insert(sjbgsmDto);
		if(!success){
			throw new BusinessException("msg","添加送检报告说明失败！");
		}
		// 同步信息至微信服务器
		if (StringUtil.isNotBlank(menuurl))
			amqpTempl.convertAndSend("wechat.exchange", MQTypeEnum.COMMENT_INSPECTION.getCode(), JSONObject.toJSONString(sjbgsmDto));
	}

	/**
	 * 根据外部信息处理样本编号
	 * @param map
	 * @param wbsjxxDtos
	 */
	public void dealYbbhByWbsjxx(Map<String,Object> map,List<WbsjxxDto> wbsjxxDtos){
		for(WbsjxxDto wbsjxxDto:wbsjxxDtos){
			if(StringUtil.isBlank(wbsjxxDto.getJcxmid()) || (String.valueOf(map.get("jcxmcs1")).equals(wbsjxxDto.getJcxmcskz1()) && String.valueOf(map.get("jcxm_kzcs3")).equals(wbsjxxDto.getJcxmcskz3()))) {
				if(StringUtil.isNotBlank(wbsjxxDto.getSjbm())){
					map.put("ybbh", wbsjxxDto.getSjbm());
				}
				if(StringUtil.isNotBlank(wbsjxxDto.getWbqtxx())) {
					String qtxx = wbsjxxDto.getWbqtxx();
					try {
						Map<String,Object> qtxxmap = JSONObject.parseObject(qtxx, Map.class);
						if (qtxxmap!=null && !CollectionUtils.isEmpty(qtxxmap.keySet())){
							map.putAll(sjxxService.flatten(qtxxmap, "wb"));
						}
					} catch (Exception e) {
						log.error("解析wbsjxxDto.getWbqtxx()失败",e);
					}
				}
				break;
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void editSaveWzxx(SjxxDto sjxxDto, String sjwzxxJson) {
		List<SjwzxxDto> sjwzxxDtos = JSONObject.parseArray(sjwzxxJson, SjwzxxDto.class);
		List<SjwzxxDto> addSjwzxxDtos = new ArrayList<>();
		List<SjwzxxDto> modSjwzxxDtos = new ArrayList<>();
		//如果没有数据,直接return;
		if (CollectionUtils.isEmpty(sjwzxxDtos)){
			return;
		}else{
			List<SjwzxxDto> sjwzxxList = sjxxService.selectWzxxBySjid(sjxxDto);
			if (!CollectionUtils.isEmpty(sjwzxxList)){
				sjwzxxList = sjwzxxList.stream().filter(e->StringUtil.isNotBlank(e.getWzid())).collect(Collectors.toList());
			}
			for (SjwzxxDto sjwzxxDto_now : sjwzxxDtos) {
				sjwzxxDto_now.setSjid(sjxxDto.getSjid());
				sjwzxxDto_now.setJclx(sjxxDto.getJclx());
				sjwzxxDto_now.setJczlx(sjxxDto.getJczlx());
				if(StringUtil.isBlank(sjwzxxDto_now.getSjwzid())){
					sjwzxxDto_now.setSjwzid(StringUtil.generateUUID());
					addSjwzxxDtos.add(sjwzxxDto_now);
				}else{
					if (!CollectionUtils.isEmpty(sjwzxxList)){
						//剩下的是需要删除的
						Iterator<SjwzxxDto> iterator = sjwzxxList.iterator();
						while (iterator.hasNext()) {
							SjwzxxDto next = iterator.next();
							if (next.getSjwzid().equals(sjwzxxDto_now.getSjwzid())){
								sjwzxxDto_now.setJson(next.getJson());
								iterator.remove();
							}
						}
					}
					modSjwzxxDtos.add(sjwzxxDto_now);
				}
			}
			//新增
			if (!CollectionUtils.isEmpty(addSjwzxxDtos)){
				for (SjwzxxDto sjwzxxDto : addSjwzxxDtos) {
					Map<String, Object> map = new HashMap<>();
					map.put("MappingReads",sjwzxxDto.getMappingReads());
					map.put("refANI",sjwzxxDto.getRefANI());
					map.put("reads",sjwzxxDto.getReads());
					map.put("target",sjwzxxDto.getTarget());
					sjwzxxDto.setJson(JSON.toJSONString(map));
				}
				sjwzxxService.insertBysjwzxxDtos(addSjwzxxDtos);
			}
			//修改
			if (!CollectionUtils.isEmpty(modSjwzxxDtos)){
				for (SjwzxxDto sjwzxxDto : modSjwzxxDtos) {
					Map<String, Object> map = new HashMap<>();
					if (StringUtil.isNotBlank(sjwzxxDto.getJson())){
						map = (Map<String, Object>)JSON.parseObject(sjwzxxDto.getJson(), Map.class);
					}
					map.put("MappingReads",sjwzxxDto.getMappingReads());
					map.put("refANI",sjwzxxDto.getRefANI());
					map.put("reads",sjwzxxDto.getReads());
					map.put("target",sjwzxxDto.getTarget());
					sjwzxxDto.setJson(JSON.toJSONString(map));
				}
				sjwzxxService.updateBySjwzxxDtos(modSjwzxxDtos);
			}
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void editSaveNyx(SjxxDto sjxxDto, String sjnyxJson) {
		List<SjnyxDto> sjnyxDtos = JSONObject.parseArray(sjnyxJson, SjnyxDto.class);
		List<SjnyxDto> addSjnyxDtos = new ArrayList<>();
		List<SjnyxDto> modSjnyxDtos = new ArrayList<>();
		//如果没有数据 将原有数据删除 wzid为空的不删除
		if (CollectionUtils.isEmpty(sjnyxDtos)){
			return;
		}else{
			List<SjnyxDto> sjnyxList = sjnyxService.getNyxBySjid(sjxxDto);
			if (!CollectionUtils.isEmpty(sjnyxList)){
				sjnyxList = sjnyxList.stream().filter(e->StringUtil.isNotBlank(e.getTbjy())).collect(Collectors.toList());
			}
			for (SjnyxDto sjnyxDto_now : sjnyxDtos) {
				sjnyxDto_now.setSjid(sjxxDto.getSjid());
				sjnyxDto_now.setJclx(sjxxDto.getJclx());
				sjnyxDto_now.setJczlx(sjxxDto.getJczlx());
				if(StringUtil.isBlank(sjnyxDto_now.getSjnyxid())){
					sjnyxDto_now.setSjnyxid(StringUtil.generateUUID());
					addSjnyxDtos.add(sjnyxDto_now);
				}else{
					if (!CollectionUtils.isEmpty(sjnyxList)){
						//剩下的是需要删除的
						Iterator<SjnyxDto> iterator = sjnyxList.iterator();
						while (iterator.hasNext()) {
							SjnyxDto next = iterator.next();
							if (next.getSjnyxid().equals(sjnyxDto_now.getSjnyxid())){
								sjnyxDto_now.setJson(next.getJson());
								iterator.remove();
							}
						}
					}
					modSjnyxDtos.add(sjnyxDto_now);
				}
			}
			int xh=1;
			//修改
			if (!CollectionUtils.isEmpty(modSjnyxDtos)){
				for (SjnyxDto sjnyxDto : modSjnyxDtos) {
					Map<String, Object> map = new HashMap<>();
					if (StringUtil.isNotBlank(sjnyxDto.getJson())){
						map = (Map<String, Object>)JSON.parseObject(sjnyxDto.getJson(), Map.class);
					}
					map.put("ID",sjnyxDto.getWkbh());
					map.put("突变基因",sjnyxDto.getTbjy());
					map.put("突变结果",sjnyxDto.getTbjg());
					map.put("耐药性",sjnyxDto.getNyx());
					map.put("突变深度",sjnyxDto.getTbsd());
					map.put("突变频率",sjnyxDto.getTbpl());
					map.put("背景突变",sjnyxDto.getBjtb());
					String[] ids = sjnyxDto.getWkbh().split("-");
					if(ids.length>1) {
						if (ids[1].length()>6){
							map.put("nbbm",ids[1]);
						}else{
							if (!ids[1].startsWith("NC") && !ids[1].startsWith("PC") && ids.length>2){
								map.put("nbbm",ids[1]+"-"+ids[2]);
							}else{
								map.put("nbbm","");
							}
						}
					}
					sjnyxDto.setJson(JSON.toJSONString(map));
					sjnyxDto.setXh(String.valueOf(xh));
					xh++;
				}
				sjnyxService.updateBySjnyxDtos(modSjnyxDtos);
			}
			//新增
			if (!CollectionUtils.isEmpty(addSjnyxDtos)){
				for (int i=0;i<addSjnyxDtos.size(); i++) {
					Map<String, Object> map = new HashMap<>();
					map.put("ID",addSjnyxDtos.get(i).getWkbh());
					map.put("突变基因",addSjnyxDtos.get(i).getTbjy());
					map.put("突变结果",addSjnyxDtos.get(i).getTbjg());
					map.put("耐药性",addSjnyxDtos.get(i).getNyx());
					map.put("突变深度",addSjnyxDtos.get(i).getTbsd());
					map.put("突变频率",addSjnyxDtos.get(i).getTbpl());
					map.put("背景突变",addSjnyxDtos.get(i).getBjtb());
					String[] ids = addSjnyxDtos.get(i).getWkbh().split("-");
					if(ids.length>1) {
						if (ids[1].length()>6){
							map.put("nbbm",ids[1]);
						}else{
							if (!ids[1].startsWith("NC") && !ids[1].startsWith("PC") && ids.length>2){
								map.put("nbbm",ids[1]+"-"+ids[2]);
							}else{
								map.put("nbbm","");
							}
						}
					}
					addSjnyxDtos.get(i).setJson(JSON.toJSONString(map));
					addSjnyxDtos.get(i).setXh(String.valueOf(xh));
					xh++;
				}
				sjnyxService.insertBysjnyxDtos(addSjnyxDtos);
			}
		}
	}

	/**
	 * 发送异常报告消息
	 *
	 * @param parameterMap 参数映射
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void sendAbnormalReportMessage(Map<String,Object> parameterMap){
		String reportTimeout = "-3";// 默认检查报告日期超过3分钟的数据
		if (parameterMap.containsKey("reportTimeout") && parameterMap.get("reportTimeout") != null){
			reportTimeout = parameterMap.get("reportTimeout").toString();
		}
		String reportOffset = "0";// 默认报告日期偏移量为0,即限制报告日期为当天的数据
		if (parameterMap.containsKey("reportOffset") && parameterMap.get("reportOffset") != null){
			reportOffset = parameterMap.get("reportOffset").toString();
		}
		// 定时检查报告日期超过3分钟，但是否上传为否的记录，可以限制报告日期为当天的。
		// 获取 当天日期
		SimpleDateFormat sdfdate = new SimpleDateFormat("yyyy-MM-dd");
		Calendar caldate = Calendar.getInstance();
		String today = sdfdate.format(caldate.getTime());
		caldate.add(Calendar.DATE, Integer.parseInt(reportOffset));
		String startRq = sdfdate.format(caldate.getTime());
		SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm");
		Calendar caltime = Calendar.getInstance();
		caltime.add(Calendar.MINUTE, Integer.parseInt(reportTimeout));
		String time = sdftime.format(caltime.getTime());
		Map<String,Object> searchMap = new HashMap();
		searchMap.put("rq", startRq);
		searchMap.put("time", today + " " + time);
		List<Map<String,Object>> list = sjxxService.getAbnormalReportList(searchMap);
		if (!CollectionUtils.isEmpty(list)){
			StringBuilder msgTitle = new StringBuilder("有报告发送异常!");
			StringBuilder msgContent = new StringBuilder(msgTitle + "总计 "+ list.size() + " 条数据!");
			List<String> idsList= new ArrayList<>();
			for (int i = 0; i < list.size() && i < 5; i++) {
				msgContent.append("\n\n").append(list.get(i).get("nbbm"));
				idsList.add(list.get(i).get("sjid").toString());
			}
			List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.ABNORMAL_REPORT_INFO.getCode());
			if (!CollectionUtils.isEmpty(ddxxglDtolist)) {
				for (DdxxglDto ddxxglDto : ddxxglDtolist) {
					if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
						talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), msgTitle.toString(), msgContent.toString());
					}
				}
			}
			SjbgsmDto sjbgsmDto=new SjbgsmDto();
			if(CollectionUtils.isEmpty(idsList)) {
				return;
			}
			for(String sjid : idsList){
				try {
					SjxxDto sjxxDto = sjxxService.selectReportBySjid(sjid);
					if(sjxxDto==null) {
						continue;
					}

					String jclx = sjxxDto.getJclx();
					String jczlx = sjxxDto.getJczlx();

					String[] s_jclxs = jclx.split(",");
					String[] s_jczlxs = new String[0];
					if(StringUtil.isNotBlank(jczlx)){
						s_jczlxs = jczlx.split(",");
					}
					List<JcsjDto> lxs = new ArrayList<>();
					for(int i=0;i<s_jclxs.length;i++) {
						String str_jclx = s_jclxs[i];
						String str_jczlx = "";
						if (s_jczlxs.length > i) {
							str_jczlx = s_jczlxs[i];
						}
						if (StringUtil.isNotBlank(str_jczlx) && StringUtil.isNotBlank(str_jclx)) {
							Object o_sub_jcsj = redisUtil.hget("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode(), str_jczlx);
							if (o_sub_jcsj == null)
								continue;
							JcsjDto sub_jcsj_dto = JSONObject.parseObject(o_sub_jcsj.toString(), JcsjDto.class);
							Object o_jcsj = redisUtil.hget("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(), str_jclx);
							if (o_jcsj == null)
								continue;
							JcsjDto jcsj_dto = JSONObject.parseObject(o_jcsj.toString(), JcsjDto.class);
							sub_jcsj_dto.setFcsdm(jcsj_dto.getCsdm());
							sub_jcsj_dto.setFcsmc(jcsj_dto.getCsmc());
							lxs.add(sub_jcsj_dto);
						}else if (StringUtil.isNotBlank(str_jclx)){
							Object o_jcsj = redisUtil.hget("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode(), str_jclx);
							if (o_jcsj == null)
								continue;
							JcsjDto jcsj_dto = JSONObject.parseObject(o_jcsj.toString(), JcsjDto.class);
							lxs.add(jcsj_dto);
						}
					}

					resendNewReport(sjxxDto, sjxxDto.getYhm(),lxs);
				} catch (Exception e) {
					log.error("发送报告失败！");
					log.error(e.getMessage());
				}
			}
		}
	}

	@Override
	public boolean checkSend(SjxxDto sjxxDto,List<String> jcxmids){
		if(CollectionUtils.isEmpty(jcxmids)){
			return false;
		}
		SjjcxmDto sjjcxmDto=new SjjcxmDto();
		sjjcxmDto.setSjid(sjxxDto.getSjid());
		//验证检测项目表，或者复检申请表含有带泛感染项目即可通过校验，前面已经校验了必须有TBT结果，所以这里只判断有泛感染就行
		List<SjjcxmDto> sjjcxmDtos=sjjcxmService.getDtoList(sjjcxmDto);
		if(!CollectionUtils.isEmpty(sjjcxmDtos)){
			for(SjjcxmDto t_sjjcxmDto:sjjcxmDtos){
				for(String jcxmid:jcxmids){
					if(t_sjjcxmDto.getJcxmid().equals(jcxmid)){
						return true;
					}
				}
			}
		}
		FjsqDto fjsqDto=new FjsqDto();
		fjsqDto.setSjid(sjxxDto.getSjid());
		List<FjsqDto> fjsqDtos=fjsqService.getDtoList(fjsqDto);
		if(!CollectionUtils.isEmpty(fjsqDtos)){
			for(FjsqDto t_fjsqDto:fjsqDtos){
				for(String jcxmid:jcxmids){
					if(t_fjsqDto.getJcxm().equals(jcxmid)){
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/**
	 * 先声保存实验结果json格式数据整合
	 * @param request
	 * @param code_param
	 * @param organ_param
	 * @return
	 * @throws BusinessException
	 */
	@Override
	public Map<String,Object> commonJsonResult(HttpServletRequest request,String code_param,String organ_param)throws BusinessException{

		Map<String,Object>returnMap=new HashMap<>();
		DBEncrypt crypt = new DBEncrypt();
		String code =code_param;
		String organ=organ_param;
		if(request!=null){
			organ = request.getParameter("organ");
			String type = request.getParameter("type");
			code = request.getParameter("code");
//		String codes = request.getParameter("codes");
			String lastcode = request.getParameter("lastcode");
			String sign = request.getParameter("sign");
//		//校验toekn
			if(StringUtil.isBlank(request.getParameter("access_token"))){
				returnMap = checkSecurity(organ, type, code, lastcode, sign,false, crypt);
				if(!"0".equals(returnMap.get("errorCode")))
					return returnMap;
			}
		}

		SjxxDto sjxxDto=new SjxxDto();
		sjxxDto.setWbbm(code);
		//查询合作伙伴
		List<String> dbs = sjxxService.getSjhbByCode(organ);
		if(dbs == null || dbs.size() == 0){
			returnMap.put("status", "fail");
			returnMap.put("errorCode", "未查询到伙伴权限！");
			return returnMap;
		}
		sjxxDto.setDbs(dbs);
		List<SjxxDto> sjxxDtos=sjxxService.getListByWbbm(sjxxDto);

		Map<String,String>paramMap=new HashMap<>();
		paramMap.put("ybbh",code);
		List<Map<String,String>>wkList=dao.selectWksjInfo(paramMap);
		String qtxxMc="";
		if(wkList !=null&&wkList.size()>0){
			qtxxMc=wkList.get(0).get("qtxx");
			SjxxDto sjxxDtoFirst=sjxxDtos.get(0);
			SjkzxxDto paramDto=new SjkzxxDto();
			paramDto.setSjid(sjxxDtoFirst.getSjid());
			SjkzxxDto sjkzxxDto=sjkzxxService.getSjkzxxBySjid(paramDto);
			String qtxx=sjkzxxDto.getQtxx();
            Map<String,Object> qtxxmap = JSONObject.parseObject(qtxx, new TypeReference<>() {});
			String limsTestCode=qtxxmap.get("limsTestCode")+"";
			String samplerCode=qtxxmap.get("samplerCode")+"";
			Map<String,Object> map=new HashMap<>();
			map.put("sampleCode",samplerCode);
			List<JcsjDto> xxdyList=redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.XXDY_TYPE.getCode());
			XxdyDto xxdyDto = new XxdyDto();
			xxdyDto.setYxx("南京先声-"+limsTestCode);
			List<XxdyDto> xxdyDtoList = xxdyService.getDtoMsgByYxx(xxdyDto);
			Optional<XxdyDto> optionalxs = xxdyDtoList.stream().filter(e -> ("南京先声-"+limsTestCode).equals(e.getYxx())).findFirst();
			if(!optionalxs.isPresent()){
				throw new BusinessException("先声SampleCode对应错误");
			}
			XxdyDto xxdyDto1=optionalxs.get();
			if(sjxxDtoFirst.getCskz3().indexOf("_"+xxdyDto1.getKzcs3().toUpperCase())!=-1){
				Optional<Map<String,String>> optional =wkList.stream().filter(e->(e.get("wkbm").indexOf(xxdyDto1.getKzcs3())!=-1||e.get("wkbm").indexOf(xxdyDto1.getKzcs3().toLowerCase())!=-1)).findFirst();
				if(optional.isPresent()){

					Map<String,String> wkMap=optional.get();

					//Qubit浓度
					map.put("sampleQubitCon",new BigDecimal(wkMap.get("wknd")).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
					//核酸体积(μL)
					map.put("DNAVolume","70.00");//固定值，刘泺提供
					//直接采用文库总量，也就是文库浓度(pm/ul) * 文库体积(ul) / 转换系数(pm/ng)
					//qubit总量(ng)
					map.put("qubit",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
					//建库投入体积（ul）
					map.put("LibraryVolume","10ul");
					//文库浓度 / 转换系数
					//文库浓度
					map.put("PreLibraryCon",new BigDecimal(wkMap.get("wknd")).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
					//文库体积（μL）
					map.put("PreLibraryVolume",StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"));
					//文库浓度(pm/ul) * 文库体积(ul) / 转换系数(pm/ng)
					//文库总量（ng）
					map.put("PreLibraryTotal",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
					//建库结果
					map.put("SWKGJ_QC","合格");
					//文库类型
					map.put("Library_type","PCRFree");
					//文库浓度 * 文库体积 / （文库总体积 * 转换系数）
					//上机浓度
					map.put("runConcentration",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
					//预计下机时间
					map.put("expeOffTime",wkMap.get("yjxjsj"));
					//测序仪器
					map.put("machine",wkMap.get("cxybh"));

				}

			}else{
				if("C".equals(xxdyDto1.getKzcs3())){
					Optional<Map<String,String>> optional_hs =wkList.stream().filter(e->e.get("wkbm").indexOf("HS")!=-1).findFirst();
					String nbbmlast=sjxxDtoFirst.getNbbm().charAt(sjxxDtoFirst.getNbbm().length()-1)+"";
					if(nbbmlast.equals("B")){
						Optional<Map<String,String>> optional_dna =wkList.stream().filter(e->e.get("wkbm").indexOf("DNA")!=-1).findFirst();
						if(optional_hs.isPresent()&&optional_dna.isPresent()){
							Map<String,String> wkMap=optional_hs.get();
							Map<String,String> wkMap_dna=optional_dna.get();
							//DNA文库总量
							map.put("PreLibraryTotal",new BigDecimal(wkMap_dna.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap_dna.get("tj"))?"1":wkMap_dna.get("tj"))).divide(new BigDecimal(wkMap_dna.get("zhxs")),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
							//RNA文库总量
							map.put("RNAPreLibraryTotal",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
							//DNA数据量
							map.put("DNASJL",wkMap_dna.get("cleanreads"));
							//RNA数据量
							map.put("RNASJL",wkMap.get("cleanreads"));
							//文库编号
							map.put("Library_Name",wkMap.get("wkbm"));
							//预计下机时间
							map.put("expeOffTime",wkMap.get("yjxjsj"));
							//文库浓度 * 文库体积 / （文库总体积 * 转换系数）
							//DNA 上机浓度（pM)
							map.put("runConcentration",new BigDecimal(wkMap_dna.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap_dna.get("tj"))?"1":wkMap_dna.get("tj"))).divide(new BigDecimal(wkMap_dna.get("zhxs")).multiply(new BigDecimal(StringUtil.isBlank(wkMap_dna.get("tj"))?"1":wkMap_dna.get("tj"))),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
							//RNA 上机浓度（pM)
							map.put("RNArunConcentration",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
							//文库浓度 / 转换系数
							//DNA文库浓度（ng/μL）
							map.put("PreLibraryCon",new BigDecimal(wkMap_dna.get("wknd")).divide(new BigDecimal(wkMap_dna.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
							//RNA文库浓度（ng/μL）
							map.put("RNAPreLibraryCon",new BigDecimal(wkMap_dna.get("wknd")).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
							//DNA文库类型
							map.put("Library_type","PCRFree");
							//DNA文库类型
							map.put("RNALibrary_type","PCRFree");
							//DNA文库体积（μL）
							map.put("PreLibraryVolume",StringUtil.isBlank(wkMap_dna.get("tj"))?"1":wkMap_dna.get("tj"));
							//RNA文库体积（μL）
							map.put("RNAPreLibraryVolume",StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"));
							//核酸体积(μL)-DNA
							map.put("DNAVolume","70.00");//固定值，刘泺提供
							//核酸体积(μL)-RNA
							map.put("DNAVolumeRNA","70.00");//固定值，刘泺提供
							//测序仪器
							map.put("machine",wkMap.get("cxybh"));
							//直接采用文库浓度，也就是文库浓度 / 转换系数 Qubit浓度
							//qubit浓度(ng/μL)-DNA
							map.put("sampleQubitCon",new BigDecimal(wkMap_dna.get("wknd")).divide(new BigDecimal(wkMap_dna.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
							//qubit浓度(ng/μL)-RNA
							map.put("sampleQubitConRNA",new BigDecimal(wkMap.get("wknd")).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
							//直接采用文库总量，也就是文库浓度(pm/ul) * 文库体积(ul) / 转换系数(pm/ng)
							//qubit总量(ng)-DNA
							map.put("qubit",new BigDecimal(wkMap_dna.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap_dna.get("tj"))?"1":wkMap_dna.get("tj"))).divide(new BigDecimal(wkMap_dna.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
							//qubit总量(ng)-RNA
							map.put("qubitRNA",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
							map.put("Reads1",wkMap.get("-"));
							map.put("Reads2",wkMap.get("-"));
							map.put("RNAReads1",wkMap.get("-"));
							map.put("RNAReads2",wkMap.get("-"));
							map.put("DNAWKGJ_QC","合格");
							map.put("RNAWKGJ_QC","合格");
						}
					}else{
						if(optional_hs.isPresent()){
							Map<String,String> wkMap=optional_hs.get();

							//DNA文库总量
							map.put("PreLibraryTotal",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
							//RNA文库总量
							map.put("RNAPreLibraryTotal",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
							//DNA数据量
							map.put("DNASJL",wkMap.get("cleanreads"));
							//DNA数据量
							map.put("RNASJL",wkMap.get("cleanreads"));
							//文库编号
							map.put("Library_Name",wkMap.get("wkbm"));
							//预计下机时间
							map.put("expeOffTime",wkMap.get("yjxjsj"));
							//文库浓度 * 文库体积 / （文库总体积 * 转换系数）
							//DNA 上机浓度（pM)
							map.put("runConcentration",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
							//DNA 上机浓度（pM)
							map.put("RNArunConcentration",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
							//文库浓度 / 转换系数
							//DNA文库浓度（ng/μL）
							map.put("PreLibraryCon",new BigDecimal(wkMap.get("wknd")).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
							//RNA文库浓度（ng/μL）
							map.put("RNAPreLibraryCon",new BigDecimal(wkMap.get("wknd")).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
							//DNA文库类型
							map.put("Library_type","PCRFree");
							//DNA文库类型
							map.put("RNALibrary_type","PCRFree");
							//DNA文库体积（μL）
							map.put("PreLibraryVolume",StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"));
							//RNA文库体积（μL）
							map.put("RNAPreLibraryVolume",StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"));
							//核酸体积(μL)-DNA
							map.put("DNAVolume","70.00");//固定值，刘泺提供
							//核酸体积(μL)-RNA
							map.put("DNAVolumeRNA","70.00");//固定值，刘泺提供
							//测序仪器
							map.put("machine",wkMap.get("cxybh"));
							//直接采用文库浓度，也就是文库浓度 / 转换系数 Qubit浓度
							//qubit浓度(ng/μL)-DNA
							map.put("sampleQubitCon",new BigDecimal(wkMap.get("wknd")).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
							//qubit浓度(ng/μL)-RNA
							map.put("sampleQubitConRNA",new BigDecimal(wkMap.get("wknd")).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
							//直接采用文库总量，也就是文库浓度(pm/ul) * 文库体积(ul) / 转换系数(pm/ng)
							//qubit总量(ng)-DNA
							map.put("qubit",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
							//qubit总量(ng)-RNA
							map.put("qubitRNA",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
							map.put("Reads1",wkMap.get("-"));
							map.put("Reads2",wkMap.get("-"));
							map.put("RNAReads1",wkMap.get("-"));
							map.put("RNAReads2",wkMap.get("-"));
							map.put("DNAWKGJ_QC","合格");
							map.put("RNAWKGJ_QC","合格");
						}
					}

				}else if("D".equals(xxdyDto1.getKzcs3())){
					Optional<Map<String,String>> optional =wkList.stream().filter(e->e.get("wkbm").indexOf("DNA")!=-1).findFirst();
					if(optional.isPresent()){
						Map<String,String> wkMap=optional.get();
						map.put("PreLibraryTotal",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
						//文库编号
						map.put("Library_Name",wkMap.get("wkbm"));
						//预计下机时间
						map.put("expeOffTime",wkMap.get("yjxjsj"));
						//文库浓度 * 文库体积 / （文库总体积 * 转换系数）
						//上机浓度
						map.put("runConcentration",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
						//文库浓度 / 转换系数
						//文库浓度
						map.put("PreLibraryCon",new BigDecimal(wkMap.get("wknd")).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
						//文库类型
						map.put("Library_type","PCRFree");
						//文库体积（μL）
						map.put("PreLibraryVolume",StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"));
						//核酸体积(μL)
						map.put("DNAVolume","70.00");//固定值，刘泺提供
						//测序仪器
						map.put("machine",wkMap.get("cxybh"));
						//直接采用文库浓度，也就是文库浓度 / 转换系数 Qubit浓度
						//Qubit浓度
						map.put("sampleQubitCon",new BigDecimal(wkMap.get("wknd")).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
						//直接采用文库总量，也就是文库浓度(pm/ul) * 文库体积(ul) / 转换系数(pm/ng)
						//qubit总量(ng)
						map.put("qubit",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
						map.put("Reads1",wkMap.get("-"));
						map.put("Reads2",wkMap.get("-"));
					}else{
						Optional<Map<String,String>> hs_optional =wkList.stream().filter(e->e.get("wkbm").indexOf("HS")!=-1).findFirst();
						if(hs_optional.isPresent()){
							Map<String,String> wkMap=hs_optional.get();
							map.put("PreLibraryTotal",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
							//文库编号
							map.put("Library_Name",wkMap.get("wkbm"));
							//预计下机时间
							map.put("expeOffTime",wkMap.get("yjxjsj"));
							//文库浓度 * 文库体积 / （文库总体积 * 转换系数）
							//上机浓度
							map.put("runConcentration",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP));
							//文库浓度 / 转换系数
							//文库浓度
							map.put("PreLibraryCon",new BigDecimal(wkMap.get("wknd")).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
							//文库类型
							map.put("Library_type","PCRFree");
							//文库体积（μL）
							map.put("PreLibraryVolume",StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"));
							//核酸体积(μL)
							map.put("DNAVolume","70.00");//固定值，刘泺提供
							//测序仪器
							map.put("machine",wkMap.get("cxybh"));
							//直接采用文库浓度，也就是文库浓度 / 转换系数 Qubit浓度
							//Qubit浓度
							map.put("sampleQubitCon",new BigDecimal(wkMap.get("wknd")).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
							//直接采用文库总量，也就是文库浓度(pm/ul) * 文库体积(ul) / 转换系数(pm/ng)
							//qubit总量(ng)
							map.put("qubit",new BigDecimal(wkMap.get("wknd")).multiply(new BigDecimal(StringUtil.isBlank(wkMap.get("tj"))?"1":wkMap.get("tj"))).divide(new BigDecimal(wkMap.get("zhxs")),5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP));
							map.put("Reads1",wkMap.get("-"));
							map.put("Reads2",wkMap.get("-"));
						}
					}

				}
			}



			returnMap.put("libaryMap",map);
		}
		String sjid = "";
		if(sjxxDtos != null && sjxxDtos.size() > 0){
			sjid=sjxxDtos.get(0).getSjid();
		}

		List<Map<String,Object>> pdfList=new ArrayList<>();
		if(StringUtil.isNotBlank(sjid)){
			MatchingUtil matchingUtil=new MatchingUtil();
			List<FjcfbDto>fjcfbDtos=fjcfbService.getFjcfbDtoByYwid(sjid);
			List<JcsjDto> jcsjDtoList=redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.DETECT_TYPE.getCode());
			if(!CollectionUtils.isEmpty(fjcfbDtos)){
				for(FjcfbDto fjcfbDto:fjcfbDtos){
					Map<String,Object> pdfMap=new HashMap<>();
					if(fjcfbDto.getWjm().contains(".pdf")){
						pdfMap.put("base64Str",matchingUtil.getPdfMsg(fjcfbDto.getWjlj()));
						int lastindex=fjcfbDto.getYwlx().lastIndexOf("_");
						String cskz3=fjcfbDto.getYwlx().substring(0,lastindex);
						String cskz1=fjcfbDto.getYwlx().substring(lastindex+1,fjcfbDto.getYwlx().length());
						Optional<JcsjDto> jcsjDto=jcsjDtoList.stream().filter(x->x.getCskz3().equals(cskz3)).filter(x->x.getCskz1().equals(cskz1)).findFirst();
						if(jcsjDto.isPresent()){
							JcsjDto jcxmDto=jcsjDto.get();
							pdfMap.put("TestItemName",jcxmDto.getCsmc());
						}
						pdfMap.put("detectionType",qtxxMc);
					}

				}
			}
		}
		returnMap.put("pdfList",pdfList);
		return returnMap;
	}
	/**
	 * @Description: 发送报告
	 * @param dtoList
	 * @param sjxxDto_t
	 * @param sjyzDto_t
	 * @param yhm
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2025/6/18 11:00
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public boolean sendVerificationReport(List<SjyzjgDto> dtoList,SjxxDto sjxxDto_t,SjyzDto sjyzDto_t,String yhm) throws BusinessException{
		try{
			//发送报告
			Map<String,Object> jsonMap = new HashMap<>();
			jsonMap.put("hzxm",sjxxDto_t.getHzxm());
			jsonMap.put("sample_type_name",sjxxDto_t.getYblxmc());
			jsonMap.put("sample_type",sjxxDto_t.getYblxdm());
			jsonMap.put("jcxm",sjxxDto_t.getJcxmmc());
			jsonMap.put("ybbh",sjxxDto_t.getYbbh());
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime dateTime = LocalDateTime.parse(sjxxDto_t.getJsrq(), inputFormatter);
			String output = dateTime.format(outputFormatter);
			jsonMap.put("jsrq",output);
			jsonMap.put("yzlb",sjyzDto_t.getYzlbmc());
			jsonMap.put("shry",yhm);
			jsonMap.put("jyry","");
			jsonMap.put("sample_id","MDL004-"+sjxxDto_t.getNbbm()+"-DNA-A45-20250114");
			String detectionType = sjyzDto_t.getYzlbcskz2().substring(2, 3);
			jsonMap.put("detection_type",detectionType);
			XxdyDto xxdyDto = new XxdyDto();
			xxdyDto.setKzcs7(sjyzDto_t.getYzlbcskz2());
			xxdyDto.setDylx(BasicDataTypeEnum.XXDY_TYPE.getCode());
			List<XxdyDto> xxdyDtos = xxdyService.getJcxmByKzcs7(xxdyDto);
			if(!org.springframework.util.CollectionUtils.isEmpty(xxdyDtos)){
				jsonMap.put("project_type",xxdyDtos.get(0).getJcxmcskz3());
			}
			jsonMap.put("xmdm",sjyzDto_t.getYzlbcskz2());
			SjkzxxDto sjkzxxDto = sjkzxxService.getDtoById(sjxxDto_t.getSjid());
			if(sjkzxxDto!=null && StringUtil.isNotBlank(sjkzxxDto.getQtxx())){
				Map<String,Object> qtxxMap = JSONObject.parseObject(sjkzxxDto.getQtxx(),new TypeReference<Map<String,Object>>(){});
				if(qtxxMap!=null && qtxxMap.get("orderId")!=null && StringUtil.isNotBlank(qtxxMap.get("orderId").toString())) {
					jsonMap.put("ddbh",qtxxMap.get("orderId").toString());
				}
				if(qtxxMap!=null && qtxxMap.get("jcxmmc")!=null && StringUtil.isNotBlank(qtxxMap.get("jcxmmc").toString())) {
					jsonMap.put("jcxmmc",qtxxMap.get("jcxmmc").toString());
				}
			}
			List<Map<String,Object>> list = new ArrayList<>();
			List<Map<String,Object>> zkjglist = new ArrayList<>();
			if(!org.springframework.util.CollectionUtils.isEmpty(dtoList)){
				for (SjyzjgDto dto : dtoList) {
					Map<String,Object> mapT = new HashMap<>();
					Map<String,Object> mapP = new HashMap<>();
					if("HEX".equals(dto.getCskz1())){
						mapP.put("xm","内标");
						mapP.put("pdbz","Ct≤40");
						BigDecimal ct1 = new BigDecimal("0");
						mapP.put("jcjg","不合格");
						if(StringUtil.isNotBlank(dto.getCt1())){
							try{
								ct1 = new BigDecimal(dto.getCt1());
								mapP.put("jcjg",ct1.compareTo(new BigDecimal("40"))<= 0?"合格":"不合格");
							} catch (Exception e) {
							}
						}
						mapP.put("ctz",StringUtil.isNotBlank(dto.getCt1())?dto.getCt1():"-");
						zkjglist.add(0,mapP);
					}else{
						mapT.put("xm",dto.getYzjgjgmc().indexOf('-') == -1 ? dto.getYzjgjgmc() : dto.getYzjgjgmc().substring(dto.getYzjgjgmc().indexOf('-') + 1));
						mapT.put("jcjg","检测灰区".equals(dto.getJlmc())?"弱阳性":dto.getJlmc());
						mapT.put("ctz",StringUtil.isNotBlank(dto.getCt1())?dto.getCt1():"-");
						list.add(mapT);
						mapP.put("xm","阳性质控-"+(dto.getYzjgjgmc().indexOf('-') == -1 ? dto.getYzjgjgmc() : dto.getYzjgjgmc().substring(dto.getYzjgjgmc().indexOf('-') + 1)));
						mapP.put("pdbz","Ct≤35");
						mapP.put("jcjg","不合格");
						BigDecimal pct1 = new BigDecimal("0");
						if(StringUtil.isNotBlank(dto.getPct1())){
							try{
								pct1 = new BigDecimal(dto.getPct1());
								mapP.put("jcjg",pct1.compareTo(new BigDecimal("35"))<= 0?"合格":"不合格");
							} catch (Exception e) {
							}
						}
						mapP.put("ctz",StringUtil.isNotBlank(dto.getPct1())?dto.getPct1():"-");
						zkjglist.add(mapP);
					}
				}
				for (SjyzjgDto dto : dtoList) {
					Map<String,Object> mapP = new HashMap<>();
					if(!"HEX".equals(dto.getCskz1())){
						mapP.put("xm","阴性质控-"+(dto.getYzjgjgmc().indexOf('-') == -1 ? dto.getYzjgjgmc() : dto.getYzjgjgmc().substring(dto.getYzjgjgmc().indexOf('-') + 1)));
						mapP.put("pdbz","无Ct值或Ct＞40");
						BigDecimal nct1 = new BigDecimal("0");
						mapP.put("jcjg","合格");
						if(StringUtil.isNotBlank(dto.getNct1())){
							try{
								nct1 = new BigDecimal(dto.getNct1());
								mapP.put("jcjg",nct1.compareTo(new BigDecimal("40"))> 0?"合格":"不合格");
							} catch (Exception e) {
							}
						}
						mapP.put("ctz",StringUtil.isNotBlank(dto.getNct1())?dto.getNct1():"-");
						zkjglist.add(mapP);
					}
				}
				jsonMap.put("yzjgList",list);
				jsonMap.put("zkjgList",zkjglist);
			}
			String jsonStr = JSON.toJSONString(jsonMap);
			log.error("送检验证生成报告json:"+jsonStr);
			Map<String,Object> result = receiveInspectionGenerateReport(null,jsonStr,null,new JkdymxDto());
			log.error("receiveInspectionGenerateReport -- result:"+result.get("status") + " info:"+result.get("errorCode"));
		}catch (Exception e){
			log.error("发送送检验证报告失败!"+e.getMessage());
			throw new BusinessException("发送送检验证报告失败"+ e.getMessage());
		}
		return true;
	}

	/**
	 * 重新执行报告生成。先声
	 * @param map
	 * @return
	 */
	private void resetReportSend(Map<String,String> map) {
		//boolean result = true;
		log.error("开始进入南京先声重发报告结果流程。");
		try {
			Map<String, String> params = new HashMap<String, String>();
			params.put("service", "sjxxWsServiceImpl");
			params.put("method", "resendNewReportByException");
			//params.put("resettime", "1,2,3,4,5");
			String resultinfo = map.get("resultinfo");
			if (StringUtil.isBlank(resultinfo)) {
				resultinfo = "";
			} else if (resultinfo.length() > 128) {
				resultinfo = resultinfo.substring(0, 128);
			}
			params.put("key", "先声:sendreport:" + map.get("ybbh") + ":" + resultinfo);
			//第三次的时候进行提醒
			params.put("resetRemindcnt", "3");
			params.put("ifuser", "南京先声");
			params.put("ifname", "报告发送流程" + map.get("subname"));
			params.put("ifmsg", "标本编码" + map.get("ybbh") + "报告发送失败");
			//重试机制的个性化参数
			params.put("sjid", map.get("sjid"));
			params.put("yhm", map.get("yhm"));

			interfaceExceptionUtil.comResetMechanism(params);
		} catch (Exception e) {
			log.error("开始进入南京先声重建报告信息发送流程 发生异常：" + e.getMessage());
		}
	}
}
