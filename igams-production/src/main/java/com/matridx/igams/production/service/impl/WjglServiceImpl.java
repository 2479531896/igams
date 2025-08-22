package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DepartmentDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.SyxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.post.IXtszDao;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum_pub;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.enums.TaskNameEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.file.AttachHelper;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IGzglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.ISyxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.production.dao.entities.FfjlDto;
import com.matridx.igams.production.dao.entities.GlwjxxDto;
import com.matridx.igams.production.dao.entities.SbglwjDto;
import com.matridx.igams.production.dao.entities.WjglDto;
import com.matridx.igams.production.dao.entities.WjglModel;
import com.matridx.igams.production.dao.entities.WjqxDto;
import com.matridx.igams.production.dao.entities.WjssdwDto;
import com.matridx.igams.production.dao.post.IWjglDao;
import com.matridx.igams.production.service.svcinterface.IFfjlService;
import com.matridx.igams.production.service.svcinterface.IGlwjxxService;
import com.matridx.igams.production.service.svcinterface.ISbglwjService;
import com.matridx.igams.production.service.svcinterface.IWjglService;
import com.matridx.igams.production.service.svcinterface.IWjqxService;
import com.matridx.igams.production.service.svcinterface.IWjssdwService;
import com.matridx.igams.production.util.XWPFDocumentUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.RandomUtil;
import com.matridx.springboot.util.file.upload.ZipUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
public class WjglServiceImpl extends BaseBasicServiceImpl<WjglDto, WjglModel, IWjglDao> implements IWjglService,IAuditService,IFileImport{
	
	/**
	 * FTP服务器地址
	 * 在SpringBoot中使用@Value只能给普通变量赋值，不能给静态变量赋值
	 */
	@Value("${matridx.ftp.url:}")
	private final String FTP_URL = null;
	/**
	 * 文档转换完成OK
	 */
	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private final String DOC_OK = null;
	
	@Value("${matridx.fileupload.prefix}")
	private final String prefix = null;
	
	@Value("${matridx.fileupload.releasePath}")
	private final String releaseFilePath = null;
	@Autowired
	IGlwjxxService glwjxxService;

	private final Logger logger = LoggerFactory.getLogger(WjglServiceImpl.class);
	@Autowired
	IShgcService shgcService;
	@Autowired
	IShxxService shxxService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IWjqxService wjqxService;
	@Autowired
	ICommonService commonservice;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IGzglService gzglService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IWjssdwService wjssdwService;
	@Autowired
	IXtszDao xtszDao;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired(required=false)
	private AmqpTemplate amqpTempl; 
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Autowired
	ICommonService commonService;
	@Autowired
	AttachHelper attachHelper;
	@Autowired
	ISyxxService syxxService;
	@Autowired
	IFfjlService ffjlService;
	@Autowired
	ISbglwjService sbglwjService;
	@Autowired
	RedisUtil redisUtil;
	@Value("${matridx.fileupload.tempPath}")
	private String tempFilePath;
	private final Logger log = LoggerFactory.getLogger(WjglServiceImpl.class);
	@Override 
	public List<WjglDto> getPagedDtoList(WjglDto wjglDto){
		//获取审核信息
		try {
			List<WjglDto> t_List = dao.getPagedDtoList(wjglDto);
			if(!CollectionUtils.isEmpty(t_List)){
				List<String> ywids = new ArrayList<>();
				for (WjglDto dto : t_List) {
					ywids.add(dto.getWjid());
				}
				List<Map<String, String>> listMap = dao.selectGzglByWjids(ywids);
				for (WjglDto dto : t_List) {
					dto.setRwjd("0/0");
					if (!CollectionUtils.isEmpty(listMap)) {
						for (Map<String, String> map : listMap) {
							if (dto.getWjid().equals(map.get("ywid"))) {
								dto.setRwjd(map.get("countoff") + "/" + map.get("countall"));
								break;
							}
						}
					}
				}
			}
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.QUALITY_FILE_ADD.getCode(), "zt", "wjid", new String[]{
				StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode(),StatusEnum.CHECK_MALMOD.getCode()
			});
			shgcService.addShgcxxByYwid(t_List, AuditTypeEnum.QUALITY_FILE_MOD.getCode(), "zt", "wjid", new String[]{
				StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode(),StatusEnum.CHECK_MALMOD.getCode()
			});
//			jcsjService.handleCodeToValue(t_List,
//					new BasicDataTypeEnum[] {
//							BasicDataTypeEnum.DOCUMENT_TYPE,BasicDataTypeEnum.DOCUMENT_SUBTYPE}, new String[] {
//					"wjfl:wjflmc","wjlb:wjlbmc"});
			return t_List;
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * 根据ID获取文件信息
	 */
	@Override
	public WjglDto getDtoById(String wjid, HttpServletRequest request){
		//查询列表
		WjglDto wjglDto = dao.getDtoById(wjid);
//		Map<String, Object> map = commonservice.getUserInfo(urlPrefix, wjglDto.getBzry(), request);
//		User user = (User) map.get("user");
//		wjglDto.setZsxm(user.getZsxm());
		return wjglDto;
	}
	
	/**
	 * 文件新增保存
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addSaveDocument(WjglDto wjglDto) throws BusinessException {
		// TODO Auto-generated method stub
		List<WjglDto> wjglDtos = dao.selectDocumentRepeat(wjglDto);
		if(!CollectionUtils.isEmpty(wjglDtos)) {
			throw new BusinessException("msg",xxglService.getMsg("WCOM_DOC00003"));
		}
		insertDto(wjglDto);
		if (StringUtil.isNotBlank(wjglDto.getSbysid())){
			SbglwjDto sbglwjDto = new SbglwjDto();
			sbglwjDto.setSbglwjid(StringUtil.generateUUID());
			sbglwjDto.setSbysid(wjglDto.getSbysid());
			sbglwjDto.setSbglid(wjglDto.getSbglid());
			sbglwjDto.setLb(wjglDto.getWjlbcskz2());
			sbglwjDto.setWjid(wjglDto.getWjid());
			sbglwjService.insert(sbglwjDto);
		}
		//文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(wjglDto.getFjids())) {
			for (int i = 0; i < wjglDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(wjglDto.getFjids().get(i),wjglDto.getWjid());
				if(!saveFile)
					return false;
				FjcfbDto fjcfbDto=new FjcfbDto();
				fjcfbDto.setFjid(wjglDto.getFjids().get(i));
				FjcfbDto t_fjcfbDto=fjcfbService.getDto(fjcfbDto);
				int begin=t_fjcfbDto.getWjm().lastIndexOf(".");
				String wjmkzm=t_fjcfbDto.getWjm().substring(begin);
				DBEncrypt p = new DBEncrypt();
				if((wjmkzm.equalsIgnoreCase(".pptx"))||(wjmkzm.equalsIgnoreCase(".ppt"))||(wjmkzm.equalsIgnoreCase(".doc"))||(wjmkzm.equalsIgnoreCase(".docx"))||(wjmkzm.equalsIgnoreCase(".xls"))||(wjmkzm.equalsIgnoreCase(".xlsx"))) {
					String wjljjm=p.dCode(t_fjcfbDto.getWjlj());
						//连接服务器
					boolean sendFlg=sendWordFile(wjljjm);
					if(sendFlg) {
						Map<String,String> map=new HashMap<>();
						String fwjm=p.dCode(t_fjcfbDto.getFwjm());
						map.put("wordName", fwjm);
						map.put("fwjlj",t_fjcfbDto.getFwjlj());
						map.put("fjid",t_fjcfbDto.getFjid());
						map.put("ywlx",t_fjcfbDto.getYwlx());
						map.put("MQDocOkType",DOC_OK);
						//发送Rabbit消息转换pdf
						amqpTempl.convertAndSend("doc2pdf_exchange",MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(map));
					}
				}else if((wjmkzm.equalsIgnoreCase(".pdf")) || (wjmkzm.equalsIgnoreCase(".PDF"))){
					// 更改原始文件路径，复制一份转换文件
					t_fjcfbDto.setYswjlj(t_fjcfbDto.getWjlj());
					t_fjcfbDto.setYswjm(t_fjcfbDto.getWjm());
					String wjlj = p.dCode(t_fjcfbDto.getWjlj());
					String backWjlj = wjlj.substring(0, wjlj.lastIndexOf(".")) + "_back" + wjmkzm;
					copyFile(wjlj,backWjlj);
					t_fjcfbDto.setZhwjxx(p.eCode(backWjlj));
					boolean result = fjcfbService.replaceFile(t_fjcfbDto);
					if (!result)
						return false;
				}else{
					//备份文件，保存至原始文件路径
					String wjlj = p.dCode(t_fjcfbDto.getWjlj());
					String backWjlj = wjlj.substring(0, wjlj.lastIndexOf(".")) + "_back" + wjmkzm;
					boolean backflag = copyFile(wjlj,backWjlj);
					if (!backflag)
						return false;
				}
			}
		}
		//新增文件权限
		boolean result = wjqxService.insertByWjid(wjglDto);
		if(!result)
			return false;
		//新增文件所属单位
		WjssdwDto wjssdwDto= new WjssdwDto();
		wjssdwDto.setWjid(wjglDto.getWjid());
		wjssdwDto.setJgid(wjglDto.getJgid());
		wjssdwDto.setXh("1");
		boolean addwjssdw= wjssdwService.insert(wjssdwDto);
		if(!addwjssdw)
			return false;
		if (StringUtil.isNotBlank(wjglDto.getGlwj_json())){
			List<GlwjxxDto> glwjxxDtos = JSON.parseArray(wjglDto.getGlwj_json(), GlwjxxDto.class);
			if (!CollectionUtils.isEmpty(glwjxxDtos)){
				for (GlwjxxDto glwjxxDto : glwjxxDtos) {
					glwjxxDto.setGlwjid(StringUtil.generateUUID());
					glwjxxDto.setWjid(wjglDto.getWjid());
				}
				glwjxxService.insertGlwjxxDtos(glwjxxDtos);
			}
		}

		return true;
	}
	
	/** 
	 * 插入文件信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(WjglDto wjglDto){
		wjglDto.setWjid(StringUtil.generateUUID());
		wjglDto.setZt(StatusEnum.CHECK_NO.getCode());
		int result = dao.insert(wjglDto);
		return result != 0;
	}
	/**
	 * 文件修改保存
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean modSaveDocument(WjglDto wjglDto, HttpServletRequest request) {
		if("2".equals(wjglDto.getScbjFlag())) {
			wjglDto.setScbj(wjglDto.getScbjFlag());
		}
		// TODO Auto-generated method stub
//		String token = talkUtil.getToken();
		XtszDto xtszDto = xtszDao.getDtoById(GlobalString.DOCUMENT_TASKDEADLINE);
		//界面选择角色，新增
		List<String> t_jsid = wjglDto.getJsids();
		List<String> jsids = new ArrayList<>();
		//获取权限列表
		Map<String,List<WjglDto>> permissionMap = wjqxService.getPermission(wjglDto,request);
		//查询已有学习权限角色，删除
		List<WjglDto> wjglList = permissionMap.get("studyList");
		//保留角色
		List<String> jsidList = new ArrayList<>();
		for (int i = t_jsid.size()-1; i >= 0; i--) {
			boolean result = true;
			for (int j = wjglList.size()-1; j >= 0; j--) {
				if(t_jsid.get(i).equals(wjglList.get(j).getJsid())){
					jsidList.add(t_jsid.get(i));
					wjglList.remove(j);
					result = false;
					break;
				}
			}
			if(result){
				jsids.add(t_jsid.get(i));
			}
		}
		Map<String,Object> map = new HashMap<>();
		map.put("jsids", jsids);
		map.put("wjglList", wjglList);
		map.put("jsidList", jsidList);
		if(!CollectionUtils.isEmpty(wjglList)) {
			//查询删除人员
			List<String> yhids = new ArrayList<>();
			List<WjglDto> del_wjglDto = dao.selectDelXtyhByJsid(map);
			if(!CollectionUtils.isEmpty(del_wjglDto)) {
				for (WjglDto dto : del_wjglDto) {
					yhids.add(dto.getYhid());
				}
			}
			//发送钉钉消息
			/*try{
			 	String ICOMM_SH00012 = xxglService.getMsg("ICOMM_SH00012");
			 	String ICOMM_SH00005 = xxglService.getMsg("ICOMM_SH00005");
			 	if(!CollectionUtils.isEmpty(del_wjglDto)) {
					for(int k = 0 ;k < del_wjglDto.size(); k++){
						if(StringUtil.isNotBlank(del_wjglDto.get(k).getDdid())){
							talkUtil.sendWorkMessage("null", del_wjglDto.get(k).getDdid(), ICOMM_SH00012,StringUtil.replaceMsg(ICOMM_SH00005,wjglDto.getZsxm(),TaskNameEnum.FILE_STUDY.getCode(),wjglDto.getWjmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}
			}catch(Exception e){
				logger.error(e.getMessage());
			}*/
			if(!CollectionUtils.isEmpty(yhids)) {
	 			//从任务表删除
	 	 		WjglDto del_WjglDto = new WjglDto();
	 	 		del_WjglDto.setWjid(wjglDto.getWjid());
	 	 		del_WjglDto.setYhids(yhids);
	 	 		del_WjglDto.setScry(wjglDto.getXgry());
	 	 		dao.deleteGzglByYhid(del_WjglDto);
	 		}
		}
		if(!CollectionUtils.isEmpty(jsids)) {
			//查询新增人员
			List<WjglDto> add_wjglDto = dao.selectAddXtyhByJsid(map);
			List<GzglDto> add_gzglDto = new ArrayList<>();
			//发送钉钉消息
			try{
				String ICOMM_SH00008 = xxglService.getMsg("ICOMM_SH00008");
				String ICOMM_SH00010 = xxglService.getMsg("ICOMM_SH00010");
				for (WjglDto dto : add_wjglDto) {
					if (StringUtil.isNotBlank(dto.getDdid())) {
						talkUtil.sendWorkMessage(dto.getYhm(), dto.getDdid(), ICOMM_SH00008, StringUtil.replaceMsg(ICOMM_SH00010, wjglDto.getZsxm(), TaskNameEnum.FILE_STUDY.getCode(), wjglDto.getWjmc(), wjglDto.getWj_qwwcsj(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss"), ""));
					}
					GzglDto gzglDto = new GzglDto();
					gzglDto.setYwmc(wjglDto.getWjmc());
					gzglDto.setYwid(wjglDto.getWjid());
					gzglDto.setRwid("");
					gzglDto.setYwdz("/production/document/pagedataViewDocument?wjid=" + wjglDto.getWjid());
					gzglDto.setRwmc(TaskNameEnum.FILE_STUDY.getCode());
					gzglDto.setFzr(dto.getYhid());
					gzglDto.setJgid(dto.getJgid());
					gzglDto.setZt(StatusEnum.CHECK_NO.getCode());
					gzglDto.setRwqx(xtszDto.getSzz());
					gzglDto.setQwwcsj(wjglDto.getWj_qwwcsj());
					gzglDto.setGzid(StringUtil.generateUUID());
					gzglDto.setLrry(wjglDto.getXgry());
					add_gzglDto.add(gzglDto);
				}
			}catch(Exception e){
				logger.error(e.getMessage());
			}
			if(!CollectionUtils.isEmpty(add_gzglDto)) {
				//插入信息至工作管理
				boolean isSuccess = gzglService.insertDtoByWjsh(add_gzglDto);
				if(!isSuccess)
					return false;
			}
		}

		updateDto(wjglDto);
		if (StringUtil.isNotBlank(wjglDto.getSbysid())){
			SbglwjDto sbglwjDto = new SbglwjDto();
			sbglwjDto.setSbglwjid(wjglDto.getSbglwjid());
			sbglwjDto.setSbysid(wjglDto.getSbysid());
			sbglwjDto.setSbglid(wjglDto.getSbglid());
			if (StringUtil.isBlank(wjglDto.getSbglid())){
				sbglwjDto.setSbglid(null);
			}
			sbglwjDto.setLb(wjglDto.getWjlbcskz2());
			sbglwjDto.setWjid(wjglDto.getWjid());
			if (sbglwjService.getDtoById(wjglDto.getSbglwjid())!=null){
				sbglwjService.update(sbglwjDto);
			}else {
				sbglwjDto.setSbglwjid(StringUtil.generateUUID());
				sbglwjService.insert(sbglwjDto);
			}
		}else {
			SbglwjDto sbglwjDto = new SbglwjDto();
			sbglwjDto.setSbglwjid(wjglDto.getSbglwjid());
			sbglwjService.delete(sbglwjDto);
		}
		//更新文件所属单位信息
		WjssdwDto wjssdwDto=new WjssdwDto();
		wjssdwDto.setWjid(wjglDto.getWjid());
		wjssdwDto.setJgid(wjglDto.getJgid());
		wjssdwService.updateWjssdw(wjssdwDto);
		boolean updateByWjid = wjqxService.updateByWjid(wjglDto);
		if(!updateByWjid)
			return false;
		GlwjxxDto glwjxxDto_del = new GlwjxxDto();
		glwjxxDto_del.setWjid(wjglDto.getWjid());
		glwjxxService.delete(glwjxxDto_del);
		if (StringUtil.isNotBlank(wjglDto.getGlwj_json())){
			List<GlwjxxDto> glwjxxDtos = JSON.parseArray(wjglDto.getGlwj_json(), GlwjxxDto.class);
			if (!CollectionUtils.isEmpty(glwjxxDtos)){
				for (GlwjxxDto glwjxxDto : glwjxxDtos) {
					glwjxxDto.setGlwjid(StringUtil.generateUUID());
					glwjxxDto.setWjid(wjglDto.getWjid());
				}
				glwjxxService.insertGlwjxxDtos(glwjxxDtos);
			}
		}
		//文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(wjglDto.getFjids())) {
			for (int i = 0; i < wjglDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(wjglDto.getFjids().get(i),wjglDto.getWjid());
				if(!saveFile)
					return false;
				FjcfbDto fjcfbDto=new FjcfbDto();
				fjcfbDto.setFjid(wjglDto.getFjids().get(i));
				FjcfbDto t_fjcfbDto=fjcfbService.getDto(fjcfbDto);
				int begin=t_fjcfbDto.getWjm().lastIndexOf(".");
				String wjmkzm=t_fjcfbDto.getWjm().substring(begin);
				DBEncrypt p = new DBEncrypt();
				if((wjmkzm.equalsIgnoreCase(".pptx"))||(wjmkzm.equalsIgnoreCase(".ppt"))||(wjmkzm.equalsIgnoreCase(".doc"))||(wjmkzm.equalsIgnoreCase(".docx"))||(wjmkzm.equalsIgnoreCase(".xls"))||(wjmkzm.equalsIgnoreCase(".xlsx"))) {
					String wjljjm=p.dCode(t_fjcfbDto.getWjlj());
						//连接服务器
					boolean sendFlg=sendWordFile(wjljjm);
					if(sendFlg) {
						Map<String,String> t_map=new HashMap<>();
						String fwjm=p.dCode(t_fjcfbDto.getFwjm());
						t_map.put("wordName", fwjm);
						t_map.put("fwjlj",t_fjcfbDto.getFwjlj());
						t_map.put("fjid",t_fjcfbDto.getFjid());
						t_map.put("ywlx",t_fjcfbDto.getYwlx());
						t_map.put("MQDocOkType",DOC_OK);
						//发送Rabbit消息转换pdf
						amqpTempl.convertAndSend("doc2pdf_exchange",MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(t_map));
					}
				}else if((wjmkzm.equalsIgnoreCase(".pdf")) || (wjmkzm.equalsIgnoreCase(".PDF"))){
					// 更改原始文件路径，复制一份转换文件
					t_fjcfbDto.setYswjlj(t_fjcfbDto.getWjlj());
					t_fjcfbDto.setYswjm(t_fjcfbDto.getWjm());
					String wjlj = p.dCode(t_fjcfbDto.getWjlj());
					String backWjlj = wjlj.substring(0, wjlj.lastIndexOf(".")) + "_back" + wjmkzm;
					copyFile(wjlj,backWjlj);
					t_fjcfbDto.setZhwjxx(p.eCode(backWjlj));
					boolean result = fjcfbService.replaceFile(t_fjcfbDto);
					if (!result)
						return false;
				}else{
					//备份文件，保存至原始文件路径
					String wjlj = p.dCode(t_fjcfbDto.getWjlj());
					String backWjlj = wjlj.substring(0, wjlj.lastIndexOf(".")) + "_back" + wjmkzm;
					boolean backflag = copyFile(wjlj,backWjlj);
					if (!backflag)
						return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 文件权限批量修改保存
	 */
	@Override
	public boolean batchPermitSaveDocument(WjglDto w_wjglDto) {
		// TODO Auto-generated method stub
		List<String> ids = w_wjglDto.getIds();//文件ID(复)
		XtszDto xtszDto = xtszDao.getDtoById(GlobalString.DOCUMENT_TASKDEADLINE);
		if(!CollectionUtils.isEmpty(ids)) {
			List<String> t_jsid = w_wjglDto.getJsids();//界面选择学习的角色
//			String token = talkUtil.getToken();
			String ICOMM_SH00008 = xxglService.getMsg("ICOMM_SH00008");
			String ICOMM_SH00010 = xxglService.getMsg("ICOMM_SH00010");
			/*String ICOMM_SH00012 = xxglService.getMsg("ICOMM_SH00012");
		 	String ICOMM_SH00005 = xxglService.getMsg("ICOMM_SH00005");*/
			for (String id : ids) {
				WjglDto t_wjglDto = dao.getDtoById(id);
				WjglDto wjglDto = new WjglDto();
				wjglDto.setJsids(w_wjglDto.getJsids());
				wjglDto.setD_jsids(w_wjglDto.getD_jsids());
				wjglDto.setT_jsids(w_wjglDto.getT_jsids());
				wjglDto.setWjid(id);
				wjglDto.setWj_qwwcsj(w_wjglDto.getWj_qwwcsj());
				//创建保存新增角色的列表
				List<String> jsids = new ArrayList<>();
				//查询已有学习权限角色
				List<WjglDto> wjglList = wjqxService.getStudyJs(wjglDto);

				Map<String, Object> map = new HashMap<>();

				//判断新增的角色
				List<String> jsidList = new ArrayList<>();
				for (int i = t_jsid.size() - 1; i >= 0; i--) {
					boolean result = true;
					for (int j = wjglList.size() - 1; j >= 0; j--) {
						if (t_jsid.get(i).equals(wjglList.get(j).getJsid())) {
							jsidList.add(t_jsid.get(i));
							wjglList.remove(j);
							result = false;
							break;
						}
					}
					if (result) {
						jsids.add(t_jsid.get(i));
					}
				}
				map.put("jsids", jsids);
				map.put("wjglList", wjglList);
				map.put("jsidList", jsidList);

				if(!CollectionUtils.isEmpty(wjglList)) {
					//查询删除人员
					List<String> yhids = new ArrayList<>();
					List<WjglDto> del_wjglDto = dao.selectDelXtyhByJsid(map);
					if(!CollectionUtils.isEmpty(del_wjglDto)) {
						for (WjglDto dto : del_wjglDto) {
							/*if(StringUtil.isNotBlank(del_wjglDto.get(k).getDdid())){
								talkUtil.sendWorkMessage("null", del_wjglDto.get(k).getDdid(), ICOMM_SH00012,StringUtil.replaceMsg(ICOMM_SH00005,wjglDto.getZsxm(),TaskNameEnum.FILE_STUDY.getCode(),t_wjglDto.getWjmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}*/
							yhids.add(dto.getYhid());
						}
					}
					if(!CollectionUtils.isEmpty(yhids)) {
						//从任务表删除
						WjglDto del_WjglDto = new WjglDto();
						del_WjglDto.setWjid(wjglDto.getWjid());
						del_WjglDto.setYhids(yhids);
						del_WjglDto.setScry(wjglDto.getXgry());
						dao.deleteGzglByYhid(del_WjglDto);
					}
				}
				if(!CollectionUtils.isEmpty(jsids)) {
					//查询新增人员
					List<WjglDto> add_wjglDto = dao.selectAddXtyhByJsid(map);
					List<GzglDto> add_gzglDto = new ArrayList<>();
					//发送钉钉消息
					try {
						for (WjglDto dto : add_wjglDto) {
							if (StringUtil.isNotBlank(dto.getDdid())) {
								talkUtil.sendWorkMessage(dto.getYhm(), dto.getDdid(), ICOMM_SH00008, StringUtil.replaceMsg(ICOMM_SH00010, w_wjglDto.getZsxm(), TaskNameEnum.FILE_STUDY.getCode(), t_wjglDto.getWjmc(), wjglDto.getWj_qwwcsj(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss"), ""));
							}
							GzglDto gzglDto = new GzglDto();
							gzglDto.setYwmc(t_wjglDto.getWjmc());
							gzglDto.setYwid(wjglDto.getWjid());
							gzglDto.setRwid("");
							gzglDto.setYwdz("/production/document/pagedataViewDocument?wjid=" + wjglDto.getWjid());
							gzglDto.setRwmc(TaskNameEnum.FILE_STUDY.getCode());
							gzglDto.setFzr(dto.getYhid());
							gzglDto.setJgid(dto.getJgid());
							gzglDto.setZt(StatusEnum.CHECK_NO.getCode());
							gzglDto.setRwqx(xtszDto.getSzz());
							gzglDto.setQwwcsj(wjglDto.getWj_qwwcsj());
							gzglDto.setGzid(StringUtil.generateUUID());
							gzglDto.setLrry(wjglDto.getXgry());
							add_gzglDto.add(gzglDto);
						}
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					if(!CollectionUtils.isEmpty(add_gzglDto)) {
						//插入信息至工作管理
						boolean isSuccess = gzglService.insertDtoByWjsh(add_gzglDto);
						if (!isSuccess)
							return false;
					}
				}
				boolean updateByWjid = wjqxService.updateByWjid(wjglDto);
				if (!updateByWjid)
					return false;
			}
		}
		return true;
	}
	
	/** 
	 * 修改文件信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateDto(WjglDto wjglDto){
		int result = dao.update(wjglDto);
		return result != 0;
	}

	/** 
	 * 删除文件信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteDto(WjglDto wjglDto) {
		int result = dao.delete(wjglDto);
		if(result == 0)
			return false;
		WjssdwDto wjssdwDto=new WjssdwDto();
		wjssdwDto.setIds(wjglDto.getIds());
		//删除文件所属单位信息
		//wjssdwService.deleteWjssdw(wjssdwDto);
		gzglService.deleteByYwid(wjglDto.getIds());
		//清空与此文件关联的关联文件id
		List<String> wjids = wjglDto.getIds();
		if(!CollectionUtils.isEmpty(wjids)) {
			for (String wjid : wjids) {
				WjglDto t_wjglDto = dao.selectByGlwjid(wjid);
				if (t_wjglDto != null) {
					int update = dao.updateGlwjid(t_wjglDto);
					if (update == 0)
						return false;
				}
			}	
		}
		//清空审核过程
		shgcService.deleteByYwids(wjglDto.getIds());
		//清空权限
		wjqxService.deleteByWjid(wjglDto);
		return true;
	}
	
	/**
	 * 文件申请审核列表
	 */
	@Override
	public List<WjglDto> getPagedAuditList(WjglDto wjglDto) {
		//获取人员ID和履历号
		List<WjglDto> t_sqList = dao.getPagedAuditIdList(wjglDto);
		
		if(CollectionUtils.isEmpty(t_sqList))
			return t_sqList;
		
		List<WjglDto> sqList = dao.getAuditListByIds(t_sqList);
		if(!CollectionUtils.isEmpty(sqList)) {
			jcsjService.handleCodeToValue(sqList,
					new BasicDataTypeEnum[] {
							BasicDataTypeEnum.DOCUMENT_TYPE,BasicDataTypeEnum.DOCUMENT_SUBTYPE}, new String[] {
					"wjfl:wjflmc","wjlb:wjlbmc"});
			
			commonservice.setSqrxm(sqList);
		}
		return sqList;
	}

	@Override
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) {
		// TODO Auto-generated method stub
		WjglDto wjglDto = (WjglDto)baseModel;
		wjglDto.setXgry(operator.getYhid());
		boolean isSuccess = update(wjglDto);
		WjssdwDto wjssdwDto=new WjssdwDto();
		wjssdwDto.setWjid(wjglDto.getWjid());
		wjssdwDto.setJgid(wjglDto.getJgid());
		wjssdwService.updateWjssdw(wjssdwDto);
		if(!isSuccess)
			return false;
		if (StringUtil.isNotBlank(wjglDto.getSbysid())){
			SbglwjDto sbglwjDto = new SbglwjDto();
			sbglwjDto.setSbglwjid(wjglDto.getSbglwjid());
			sbglwjDto.setSbysid(wjglDto.getSbysid());
			sbglwjDto.setSbglid(wjglDto.getSbglid());
			if (StringUtil.isBlank(wjglDto.getSbglid())){
				sbglwjDto.setSbglid(null);
			}
			sbglwjDto.setLb(wjglDto.getWjlbcskz2());
			sbglwjDto.setWjid(wjglDto.getWjid());
			if (sbglwjService.getDtoById(wjglDto.getSbglwjid())!=null){
				sbglwjService.update(sbglwjDto);
			}else {
				sbglwjDto.setSbglwjid(StringUtil.generateUUID());
				sbglwjService.insert(sbglwjDto);
			}
		}else {
			SbglwjDto sbglwjDto = new SbglwjDto();
			sbglwjDto.setSbglwjid(wjglDto.getSbglwjid());
			sbglwjService.delete(sbglwjDto);
		}
		GlwjxxDto glwjxxDto_del = new GlwjxxDto();
		glwjxxDto_del.setWjid(wjglDto.getWjid());
		glwjxxService.delete(glwjxxDto_del);
		if (StringUtil.isNotBlank(wjglDto.getGlwj_json())){
			List<GlwjxxDto> glwjxxDtos = JSON.parseArray(wjglDto.getGlwj_json(), GlwjxxDto.class);
			if (!CollectionUtils.isEmpty(glwjxxDtos)){
				for (GlwjxxDto glwjxxDto : glwjxxDtos) {
					glwjxxDto.setGlwjid(StringUtil.generateUUID());
					glwjxxDto.setWjid(wjglDto.getWjid());
				}
				glwjxxService.insertGlwjxxDtos(glwjxxDtos);
			}
		}
		//文件复制到正式文件夹，插入信息至正式表
		List<String> list = new ArrayList<>();
		if(!CollectionUtils.isEmpty(wjglDto.getFjids())) {
			for (int i = 0; i < wjglDto.getFjids().size(); i++) {
				if (!list.contains(wjglDto.getFjids().get(i))) {
					list.add(wjglDto.getFjids().get(i));
					boolean saveFile = fjcfbService.save2RealFile(wjglDto.getFjids().get(i), wjglDto.getWjid());
					if (!saveFile)
						return false;
					FjcfbDto fjcfbDto = new FjcfbDto();
					fjcfbDto.setFjid(wjglDto.getFjids().get(i));
					FjcfbDto t_fjcfbDto = fjcfbService.getDto(fjcfbDto);
					int begin = t_fjcfbDto.getWjm().lastIndexOf(".");
					String wjmkzm = t_fjcfbDto.getWjm().substring(begin);
					DBEncrypt p = new DBEncrypt();
					if ((wjmkzm.equalsIgnoreCase(".pptx"))||(wjmkzm.equalsIgnoreCase(".ppt"))||(wjmkzm.equalsIgnoreCase(".doc")) || (wjmkzm.equalsIgnoreCase(".docx")) || (wjmkzm.equalsIgnoreCase(".xls")) || (wjmkzm.equalsIgnoreCase(".xlsx"))) {
						String wjljjm = p.dCode(t_fjcfbDto.getWjlj());
						//连接服务器
						boolean sendFlg = sendWordFile(wjljjm);
						if (sendFlg) {
							Map<String, String> map = new HashMap<>();
							String fwjm = p.dCode(t_fjcfbDto.getFwjm());
							map.put("wordName", fwjm);
							map.put("fwjlj", t_fjcfbDto.getFwjlj());
							map.put("fjid", t_fjcfbDto.getFjid());
							map.put("ywlx", t_fjcfbDto.getYwlx());
							map.put("MQDocOkType", DOC_OK);
							//发送Rabbit消息转换pdf
							amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(map));
						}
					} else if ((wjmkzm.equalsIgnoreCase(".pdf")) || (wjmkzm.equalsIgnoreCase(".PDF"))) {
						// 更改原始文件路径，复制一份转换文件
						t_fjcfbDto.setYswjlj(t_fjcfbDto.getWjlj());
						t_fjcfbDto.setYswjm(t_fjcfbDto.getWjm());
						String wjlj = p.dCode(t_fjcfbDto.getWjlj());
						String backWjlj = wjlj.substring(0, wjlj.lastIndexOf(".")) + "_back" + wjmkzm;
						copyFile(wjlj, backWjlj);
						t_fjcfbDto.setZhwjxx(p.eCode(backWjlj));
						boolean result = fjcfbService.replaceFile(t_fjcfbDto);
						if (!result)
							return false;
					} else {
						// 更改原始文件路径
						t_fjcfbDto.setYswjlj(t_fjcfbDto.getWjlj());
						t_fjcfbDto.setYswjm(t_fjcfbDto.getWjm());
						boolean replaceFile = fjcfbService.replaceFile(t_fjcfbDto);
						if (!replaceFile)
							return false;
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}

//		String token = talkUtil.getToken();
		for(ShgcDto shgcDto : shgcList){
			WjglDto wjglDto = new WjglDto();
			wjglDto.setWjid(shgcDto.getYwid());
			
			wjglDto.setXgry(operator.getYhid());
			
			//审核退回
			if(AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())){

				wjglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				WjglDto t_wjglDto = dao.getDtoById(shgcDto.getYwid());
				//若审核不通过，将根文件的关联文件id清空
//				WjglDto g_wjglDto=new WjglDto();
//				g_wjglDto.setWjid(t_wjglDto.getGwjid());
//				dao.updateGlwjid(g_wjglDto);
				shgcDto.setSqbm(t_wjglDto.getJgid());
				List<SpgwcyDto> spgwcyDtos = commonservice.siftJgList(shgcDto.getSpgwcyDtos(),t_wjglDto.getJgid());
				//发送钉钉消息
				if(!CollectionUtils.isEmpty(spgwcyDtos)){
					try{
						String ICOMM_SH00026 = xxglService.getMsg("ICOMM_SH00026");
						String ICOMM_SH00095 = xxglService.getMsg("ICOMM_SH00095");
						for (SpgwcyDto spgwcyDto : spgwcyDtos) {
							if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
										spgwcyDto.getYhid(), ICOMM_SH00026, StringUtil.replaceMsg(ICOMM_SH00095, operator.getZsxm(), shgcDto.getShlbmc(), t_wjglDto.getWjbh(), t_wjglDto.getWjlbmc(), t_wjglDto.getJgmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						logger.error(e.getMessage());
					}
				}
				//审核通过
			}else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())){
				//内部采用系统时间，所以只需随意设置就可以
				wjglDto.setPzsj("1");
				wjglDto.setZt(StatusEnum.CHECK_PASS.getCode());
				//根据条件保存至权限管理表
				wjglDto.setT_jsids(shgcDto.getT_jsids());
				wjglDto.setJsids(shgcDto.getJsids());
				wjglDto.setD_jsids(shgcDto.getD_jsids());
				wjqxService.deleteByWjid(wjglDto);
				boolean insertByWjid = wjqxService.insertByWjid(wjglDto);
				if(!insertByWjid)
					return false;
				//根据文件ID查询文件信息
				WjglDto t_wjglDto = dao.getDtoById(shgcDto.getYwid());
				wjglDto.setSxrq(t_wjglDto.getSxrq());//生效日期
				//查询有学习权限角色人员group by   根据文件id查询文件学习权限角色，和人员     
				List<WjglDto> wjglDtos = wjqxService.selectXtyhByWjid(wjglDto);
				if(!CollectionUtils.isEmpty(wjglDtos)) {
					List<GzglDto> gzglDtos = new ArrayList<>();
					XtszDto xtszDto = xtszDao.getDtoById(GlobalString.DOCUMENT_TASKDEADLINE);

					for (WjglDto dto : wjglDtos) {
						GzglDto gzglDto = new GzglDto();
						gzglDto.setYwmc(t_wjglDto.getWjmc());
						gzglDto.setYwid(t_wjglDto.getWjid());
						gzglDto.setRwid("");
						gzglDto.setYwdz("/production/document/pagedataViewDocument?wjid=" + wjglDto.getWjid());
						gzglDto.setRwmc(TaskNameEnum.FILE_STUDY.getCode());
						gzglDto.setFzr(dto.getYhid());
						gzglDto.setJgid(dto.getJgid());
						gzglDto.setZt(StatusEnum.CHECK_NO.getCode());
						gzglDto.setRwqx(xtszDto.getSzz());
						gzglDto.setQwwcsj(shgcDto.getQwwcsj());
						gzglDto.setGzid(StringUtil.generateUUID());
						gzglDto.setLrry(operator.getYhid());
						gzglDtos.add(gzglDto);
					}
					//插入信息至工作管理
					boolean isSuccess = gzglService.insertDtoByWjsh(gzglDtos);
					if(!isSuccess)
						return false;
					//发送钉钉消息
					try{
						String ICOMM_SH00008 = xxglService.getMsg("ICOMM_SH00008");
						for (WjglDto dto : wjglDtos) {
							if (StringUtil.isNotBlank(dto.getDdid())) {//学习人员发送钉钉消息提醒
								//talkUtil.sendWorkMessage("null", wjglDtos.get(i).getDdid(), ICOMM_SH00008,StringUtil.replaceMsg(ICOMM_SH00010,operator.getZsxm(),TaskNameEnum.FILE_STUDY.getCode(),t_wjglDto.getWjmc(),shgcDto.getQwwcsj(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss"),""));
								//组装访问路径
								//内网访问 &：%26        =：%3D  ?:%3F
								//String internalbtn = applicationurl + urlPrefix + "/common/view/displayView?view_url=/ws/projectdd/viewProjectTaskdd%3Frwid%3D"+xmjdrwDtos.getRwid();
								String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/tasknotification/tasktransfer/tasktransfer&applicationurl=" + applicationurl + "&urlPrefix=" + urlPrefix + "&rwlx=wj" + "&rwid=" + dto.getWjid(), StandardCharsets.UTF_8);
								List<BtnJsonList> btnJsonLists = new ArrayList<>();
								BtnJsonList btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("小程序");
								btnJsonList.setActionUrl(internalbtn);
								btnJsonLists.add(btnJsonList);
								//发送钉钉消息
								talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),dto.getYhid(),dto.getYhm(),
										dto.getDdid(),
										ICOMM_SH00008,
										StringUtil.replaceMsg(xxglService.getMsg("ICOMM_WjXX001"),
												operator.getZsxm(),
												TaskNameEnum.FILE_STUDY.getCode(),
												t_wjglDto.getWjmc(),
												shgcDto.getQwwcsj(),
												DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),
										btnJsonLists,
										"1");
							}
						}
					}catch(Exception e){
						logger.info(e.getMessage());
					}
				}
				//查询已完成审核信息
				ShxxDto shxxDto = new ShxxDto();
				shxxDto.setGcid(shgcDto.getGcid());
				List<ShxxDto> shxxDtos = shxxService.getShxxOrderByGcid(shxxDto);
				StringBuilder shrList = new StringBuilder();//审核人List
				StringBuilder shsjList = new StringBuilder();//审核时间List
				StringBuilder yhmList = new StringBuilder();//用户名List
				if(!CollectionUtils.isEmpty(shxxDtos)) {
					for (ShxxDto dto : shxxDtos) {
						shrList.append(",").append(dto.getLrry());
						shsjList.append(",").append(dto.getShsj());
						yhmList.append(",").append(dto.getYhm());
					}
					shrList.append(",").append(operator.getYhid());
					shsjList.append(",").append(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
					yhmList.append(",").append(operator.getYhm());
					shrList = new StringBuilder(shrList.substring(1, shrList.length()));
					shsjList = new StringBuilder(shsjList.substring(1, shsjList.length()));
					yhmList = new StringBuilder(yhmList.substring(1, yhmList.length()));
				}
				List<FjcfbDto> fjcfbDtos = dao.selectFjcfbDtoByWjid(t_wjglDto.getWjid());
				if(!CollectionUtils.isEmpty(fjcfbDtos)) {
					for (FjcfbDto fjcfbDto : fjcfbDtos) {
						int begin = fjcfbDto.getWjm().lastIndexOf(".");
						String wjmkzm = fjcfbDto.getWjm().substring(begin);
						DBEncrypt p = new DBEncrypt();
						if ((wjmkzm.equalsIgnoreCase(".ppt")) || (wjmkzm.equalsIgnoreCase(".pptx")) || (wjmkzm.equalsIgnoreCase(".doc")) || (wjmkzm.equalsIgnoreCase(".docx")) || (wjmkzm.equalsIgnoreCase(".xls")) || (wjmkzm.equalsIgnoreCase(".xlsx"))) {

							Map<String, String> map = new HashMap<>();
							String fwjm = p.dCode(fjcfbDto.getFwjm());
							map.put("wordName", fwjm);
							map.put("fwjlj", fjcfbDto.getFwjlj());
							map.put("fjid", fjcfbDto.getFjid());
							map.put("ywlx", fjcfbDto.getYwlx());
							map.put("shrList", shrList.toString());
							map.put("shsjList", shsjList.toString());
							map.put("yhmList", yhmList.toString());
							if (StringUtil.isBlank(t_wjglDto.getSxrq())) {
								map.put("sxrq", DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
							} else {
								map.put("sxrq", t_wjglDto.getSxrq());
							}
							map.put("wjid", t_wjglDto.getWjid());
							map.put("replaceflg", shgcDto.getReplaceflg());
							map.put("MQDocOkType", DOC_OK);
							map.put("wjlj", fjcfbDto.getWjlj());

							//获取编制人、审核人、批准人的签名路径
							//设置编制日期、审核日期、批准日期
							String[] shrqList = shsjList.toString().split(",");
							if(!CollectionUtils.isEmpty(shxxDtos)) {
								if (StringUtil.isNotBlank(shxxDtos.get(0).getWjlj()) && StringUtil.isNotBlank(shxxDtos.get(shxxDtos.size() - 1).getWjlj())) {
									if (shxxDtos.size() > 1) {
										if (StringUtil.isNotBlank(shxxDtos.get(1).getWjlj())) {
											map.put("bzr_Pic", shxxDtos.get(0).getWjlj());
											map.put("bzrq", shrqList[0]);
											map.put("shr_Pic", shxxDtos.get(1).getWjlj());
											map.put("shrq", shrqList[1]);
											FjcfbDto pzrxx = new FjcfbDto();
											pzrxx.setYwid(operator.getYhm());
											pzrxx.setYwlx(BusTypeEnum.IMP_SIGNATURE.getCode());
											List<FjcfbDto> pzrxxList = fjcfbService.selectFjcfbDtoByYwidAndYwlx(pzrxx);
											if(pzrxxList==null || pzrxxList.isEmpty()){
												throw new BusinessException("msg", "请上传相关人员签名！"+operator.getYhm());
											}
											pzrxx.setYwid(t_wjglDto.getYhm());
											List<FjcfbDto> bzryList = fjcfbService.selectFjcfbDtoByYwidAndYwlx(pzrxx);
											if(bzryList==null || bzryList.isEmpty()){
												throw new BusinessException("msg", "请上传相关人员签名！"+t_wjglDto.getYhm());
											}
											map.put("bzry_Pic", bzryList.get(0).getWjlj());//编制人员签名
											map.put("pzr_Pic", pzrxxList.get(0).getWjlj());
											map.put("pzrq", shrqList[shrqList.length - 1]);
											map.put("fbrq", shrqList[shrqList.length - 1]);
										} else {
											throw new BusinessException("msg", "请上传相关人员签名！");
										}
									}
								} else {
									throw new BusinessException("msg", "请上传相关人员签名！");
								}
							}
							map = replaceMap(map, t_wjglDto);
							if ("4".equals(t_wjglDto.getZlbcskz2())) {
								XWPFDocumentUtil xwpfDocumentUtil = new XWPFDocumentUtil();
								if ("0".equals(shgcDto.getSignflg())) {
									shgcDto.setSignflg("1");
								}
								xwpfDocumentUtil.replaceDocument(map, fjcfbService, FTP_URL, DOC_OK, amqpTempl);
							}
							map.put("signflg", shgcDto.getSignflg());
							//word文件发送至服务器转换
							String wjljjm = p.dCode(fjcfbDto.getWjlj());
							//连接服务器
							boolean sendFlg = sendWordFile(wjljjm);
							if (sendFlg) {
								//发送Rabbit消息转换pdf
								amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(map));
							}
						} else if ((wjmkzm.equalsIgnoreCase(".pdf")) || (wjmkzm.equalsIgnoreCase(".PDF"))) {
							// pdf文件, 生成一份转换pdf文件，本地重新组装Dto加水印
							String wjlj = p.dCode(fjcfbDto.getWjlj());
							String backWjlj = wjlj.substring(0, wjlj.lastIndexOf(".")) + "_back" + wjmkzm;
							copyFile(wjlj, backWjlj);
							fjcfbDto.setZhwjxx(p.eCode(backWjlj));
							String wjm = fjcfbDto.getWjm();
							String backWjm = wjm.substring(0, wjm.lastIndexOf(".")) + "_back" + wjmkzm;
							String back_Wjlj = wjlj.substring(wjlj.lastIndexOf("/") + 1);
							String backFwjm = back_Wjlj.substring(0, back_Wjlj.lastIndexOf(".")) + "_back" + wjmkzm;
							// 判断是否文件转换  1 表示转换，0 表示不转换
							if (StringUtil.isNotBlank(shgcDto.getReplaceflg()) && shgcDto.getReplaceflg().equals("1")) {
								fjcfbDto.setWjm(backWjm);
								fjcfbDto.setWjlj(p.eCode(backWjlj));
								fjcfbDto.setFwjm(p.eCode(backFwjm));
								fjcfbService.replaceFile(fjcfbDto);
							} else {
								fjcfbService.replaceFile(fjcfbDto);
								fjcfbDto.setWjm(backWjm);
								fjcfbDto.setWjlj(p.eCode(backWjlj));
								fjcfbDto.setFwjm(p.eCode(backFwjm));
							}
							fjcfbDto.setShrs(shrList.toString());
							fjcfbDto.setShsjs(shsjList.toString());
							fjcfbDto.setShyhms(yhmList.toString());

							if ("0".equals(shgcDto.getSignflg()) || "1".equals(shgcDto.getSignflg())) {
								//查询水印信息
								SyxxDto syxxDto = syxxService.getDtoByWjlb(BusTypeEnum.IMP_DOCUMENT.getCode());
								if (syxxDto != null) {
									if (StringUtil.isBlank(wjglDto.getSxrq())) {
										attachHelper.addWatermark(fjcfbDto, shgcDto.getSignflg(), DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"), syxxDto);
									} else {
										attachHelper.addWatermark(fjcfbDto, shgcDto.getSignflg(), wjglDto.getSxrq(), syxxDto);
									}
								} else {
									logger.error("水印信息不存在！");
								}
							}
						}
					}
				}
				wjglDto.setSfth(shgcDto.getReplaceflg());
				wjglDto.setZhgz(shgcDto.getSignflg());
				//发送钉钉消息
				List<SpgwcyDto> spgwcyDtos = shgcDto.getSpgwcyDtos();
				if(!CollectionUtils.isEmpty(spgwcyDtos)){
					try{
						String ICOMM_SH00006 = xxglService.getMsg("ICOMM_SH00006");
						String ICOMM_SH00011 = xxglService.getMsg("ICOMM_SH00011");
						for (SpgwcyDto spgwcyDto : spgwcyDtos) {
							if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(), spgwcyDto.getYhm(),
										spgwcyDto.getYhid(), ICOMM_SH00006, StringUtil.replaceMsg(ICOMM_SH00011, operator.getZsxm(), shgcDto.getShlbmc(), t_wjglDto.getWjmc(), shgcDto.getQwwcsj(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}catch(Exception e){
						logger.error(e.getMessage());
					}
				}
				//审核中
			}else{
				if("1".equals(shgcDto.getShxx_lcxh())){
					wjglDto.setShsj("1");
				}

				logger.error("sendCardMessage 文件管理  ywid="+ shgcDto.getYwid());
				wjglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				//发送钉钉消息
				WjglDto t_wjglDto = dao.getDtoById(shgcDto.getYwid());
				shgcDto.setSqbm(t_wjglDto.getJgid());
				List<SpgwcyDto> spgwcyDtos = commonservice.siftJgList(shgcDto.getSpgwcyDtos(),t_wjglDto.getJgid());
				if(spgwcyDtos!=null)
					logger.error("sendCardMessage 文件管理  spgwcyDtos.size="+ spgwcyDtos.size());
				if(!CollectionUtils.isEmpty(spgwcyDtos)){
					try{
						String ICOMM_SH00003 = xxglService.getMsg("ICOMM_SH00003");
						String ICOMM_SH00017 = xxglService.getMsg("ICOMM_SH00017");
						int size = spgwcyDtos.size();
						for(int i=0;i<size;i++){
							logger.error("sendCardMessage 文件管理  for 循环  i= "+ i);
							if(StringUtil.isNotBlank(spgwcyDtos.get(i).getYhm())){
								logger.error("sendCardMessage 文件管理  for 循环  Yhid= "+ spgwcyDtos.get(i).getYhid());
								String sign = URLEncoder.encode(commonservice.getSign(t_wjglDto.getWjid()), StandardCharsets.UTF_8);
								//内网访问
								String internalbtn = applicationurl+urlPrefix+"/ws/auditProcess/auditPhone?shxx_shry="+spgwcyDtos.get(i).getYhid()+"&shxx_shjs="+spgwcyDtos.get(i).getJsid()+"&ywid="+t_wjglDto.getWjid()+"&sign="+sign+"&xlcxh="+shgcDto.getXlcxh()+"&business_url="+"/production/document/modDocument&ywzd=wjid&shlbmc="+shgcDto.getShlbmc();
								//外网访问
								//String external=externalurl+urlPrefix+"/ws/auditProcess/auditPhone?shxx_shry="+spgwcyDtos.get(i).getYhid()+"&shxx_shjs="+spgwcyDtos.get(i).getJsid()+"&ywid="+t_wjglDto.getWjid()+"&sign="+sign+"&xlcxh="+shgcDto.getXlcxh()+"&business_url="+"/production/document/modDocument&ywzd=wjid&shlbmc="+shgcDto.getShlbmc();
								List<BtnJsonList> btnJsonLists = new ArrayList<>();
								BtnJsonList btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("详细");
								btnJsonList.setActionUrl(internalbtn);
								btnJsonLists.add(btnJsonList);
								/*btnJsonList = new BtnJsonList();
								btnJsonList.setTitle("外网访问");
								btnJsonList.setActionUrl(external);
								btnJsonLists.add(btnJsonList);*/
								talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),spgwcyDtos.get(i).getYhid(),spgwcyDtos.get(i).getYhm(), spgwcyDtos.get(i).getYhid(),ICOMM_SH00003,StringUtil.replaceMsg(ICOMM_SH00017, operator.getZsxm(),shgcDto.getShlbmc() ,t_wjglDto.getWjmc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
							}
						}
					}catch(Exception e){
						logger.error(e.getMessage());
					}
				}

				logger.error("sendCardMessage 文件管理  for 循环  钉钉发送完成 ");
				//发送钉钉消息--取消审核人员
				List<SpgwcyDto> no_spgwcyDtos = commonservice.siftJgList(shgcDto.getNo_spgwcyDtos(),t_wjglDto.getJgid());
				if(!CollectionUtils.isEmpty(no_spgwcyDtos)){
					try{
						String ICOMM_SH00004 = xxglService.getMsg("ICOMM_SH00004");
						String ICOMM_SH00005 = xxglService.getMsg("ICOMM_SH00005");
						talkUtil.sendWorkMessageBySpgwcyList(shgcDto.getShlb(),no_spgwcyDtos, ICOMM_SH00004, StringUtil.replaceMsg(ICOMM_SH00005, operator.getZsxm(), shgcDto.getShlbmc(), t_wjglDto.getWjmc(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
					}catch(Exception e){
						logger.error(e.getMessage());
					}
				}
			}
			//更新审核时间
			if("1".equals(shgcDto.getWjsh())){
				wjglDto.setShsj("1");
			}
			//更新状态
			if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())){
				if(StringUtil.isBlank(wjglDto.getSxrq())){
					wjglDto.setSxrq(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
				}
				dao.updatePzsj(wjglDto);
				//根据关联文件ID修改删除标记
				dao.updateScbj(wjglDto);
			}else{
				dao.updateZt(wjglDto);
			}
		}
		return true;
	}

	@Override
	public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) {
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}
		if(auditParam.isCancelOpe()) {
			//审核回调方法
			for(ShgcDto shgcDto : shgcList){
				String wjid = shgcDto.getYwid();
				WjglDto wjglDto = new WjglDto();
				wjglDto.setXgry(operator.getYhid());
				wjglDto.setWjid(wjid);
				wjglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				
				dao.update(wjglDto);
			}
		}else {
			//审核回调方法
			for(ShgcDto shgcDto : shgcList){
				String wjid = shgcDto.getYwid();
				WjglDto wjglDto = new WjglDto();
				wjglDto.setXgry(operator.getYhid());
				wjglDto.setWjid(wjid);
				wjglDto.setZt(StatusEnum.CHECK_NO.getCode());
				
				dao.update(wjglDto);
			}
		}
		return true;
	}

	/**
	 * 文件修订提交保存
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public WjglDto modSaveDocumentView(WjglDto wjglDto) {
		// TODO Auto-generated method stub
		WjglDto t_wjglDto = dao.getDtoById(wjglDto.getWjid());
		//新增文件修订数据
		wjglDto.setYwjid(t_wjglDto.getWjid());
		//查询原文件发放记录
		FfjlDto ffjlDto = new FfjlDto();
		ffjlDto.setWjid(wjglDto.getYwjid());
		List<FfjlDto> dtoList = ffjlService.getDtoList(ffjlDto);
		if (!CollectionUtils.isEmpty(dtoList)){
			wjglDto.setFfzt("0");
		}
		boolean result = insertDto(wjglDto);
		if(!result)
			return null;
		if (!CollectionUtils.isEmpty(dtoList)){
			for (FfjlDto dto : dtoList) {
				dto.setFfjlid(StringUtil.generateUUID());
				dto.setWjid(wjglDto.getWjid());
				dto.setSfff("0");
				dto.setFfrq(null);
			}
			ffjlService.insertFfjlDtos(dtoList);
		}
		//新增文件所属单位
		WjssdwDto addwjssdwDto= new WjssdwDto();
		addwjssdwDto.setWjid(wjglDto.getWjid());
		addwjssdwDto.setJgid(wjglDto.getJgid());
		addwjssdwDto.setXh("1");
		boolean addwjssdw= wjssdwService.insert(addwjssdwDto);
		if(!addwjssdw)
			return null;
		//更改原数据关联文件
		t_wjglDto.setGlwjid(wjglDto.getWjid());
		int update = dao.update(t_wjglDto);
		//更新文件所属单位信息
		WjssdwDto wjssdwDto=new WjssdwDto();
		wjssdwDto.setWjid(wjglDto.getWjid());
		wjssdwDto.setJgid(wjglDto.getJgid());
		wjssdwService.updateWjssdw(wjssdwDto);
		if(update < 1)
			return null;
		if (StringUtil.isNotBlank(wjglDto.getSbysid())){
			SbglwjDto sbglwjDto = new SbglwjDto();
			sbglwjDto.setSbglwjid(StringUtil.generateUUID());
			sbglwjDto.setSbysid(wjglDto.getSbysid());
			sbglwjDto.setSbglid(wjglDto.getSbglid());
			sbglwjDto.setLb(wjglDto.getWjlbcskz2());
			sbglwjDto.setWjid(wjglDto.getWjid());
			sbglwjService.insert(sbglwjDto);
		}
		if (StringUtil.isNotBlank(wjglDto.getGlwj_json())){
			List<GlwjxxDto> glwjxxDtos = JSON.parseArray(wjglDto.getGlwj_json(), GlwjxxDto.class);
			if (!CollectionUtils.isEmpty(glwjxxDtos)){
				for (GlwjxxDto glwjxxDto : glwjxxDtos) {
					glwjxxDto.setGlwjid(StringUtil.generateUUID());
					glwjxxDto.setWjid(wjglDto.getWjid());
				}
				glwjxxService.insertGlwjxxDtos(glwjxxDtos);
			}
		}
		//查询原文件权限
		List<WjqxDto> wjqxDtos = wjqxService.selectWjqxlist(t_wjglDto);
		if(!CollectionUtils.isEmpty(wjqxDtos)) {
			for (WjqxDto wjqxDto : wjqxDtos) {
				wjqxDto.setWjid(wjglDto.getWjid());
				wjqxDto.setQxid(StringUtil.generateUUID());
			}
			//新增文件权限
			result = wjqxService.insertByWjqxDtos(wjqxDtos);
			if(!result)
				return null;
		}
		//文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(wjglDto.getFjids())) {
			for (int i = 0; i < wjglDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(wjglDto.getFjids().get(i),wjglDto.getWjid());
				if(!saveFile)
					return null;
				FjcfbDto fjcfbDto=new FjcfbDto();
				fjcfbDto.setFjid(wjglDto.getFjids().get(i));
				FjcfbDto t_fjcfbDto=fjcfbService.getDto(fjcfbDto);
				int begin=t_fjcfbDto.getWjm().lastIndexOf(".");
				String wjmkzm=t_fjcfbDto.getWjm().substring(begin);
				DBEncrypt p = new DBEncrypt();
				if((wjmkzm.equalsIgnoreCase(".doc"))||(wjmkzm.equalsIgnoreCase(".ppt"))||(wjmkzm.equalsIgnoreCase(".pptx"))||(wjmkzm.equalsIgnoreCase(".docx"))||(wjmkzm.equalsIgnoreCase(".xls"))||(wjmkzm.equalsIgnoreCase(".xlsx"))) {
					String wjljjm=p.dCode(t_fjcfbDto.getWjlj());
						//连接服务器
					boolean sendFlg=sendWordFile(wjljjm);
					if(sendFlg) {
						Map<String,String> t_map=new HashMap<>();
						String fwjm=p.dCode(t_fjcfbDto.getFwjm());
						t_map.put("wordName", fwjm);
						t_map.put("fwjlj",t_fjcfbDto.getFwjlj());
						t_map.put("fjid",t_fjcfbDto.getFjid());
						t_map.put("ywlx",t_fjcfbDto.getYwlx());
						t_map.put("MQDocOkType",DOC_OK);
						//发送Rabbit消息转换pdf
						amqpTempl.convertAndSend("doc2pdf_exchange",MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(t_map));
					}
				}else if((wjmkzm.equalsIgnoreCase(".pdf")) || (wjmkzm.equalsIgnoreCase(".PDF"))){
					// 更改原始文件路径，复制一份转换文件
					t_fjcfbDto.setYswjlj(t_fjcfbDto.getWjlj());
					t_fjcfbDto.setYswjm(t_fjcfbDto.getWjm());
					String wjlj = p.dCode(t_fjcfbDto.getWjlj());
					String backWjlj = wjlj.substring(0, wjlj.lastIndexOf(".")) + "_back" + wjmkzm;
					copyFile(wjlj,backWjlj);
					t_fjcfbDto.setZhwjxx(p.eCode(backWjlj));
					boolean replaceFile = fjcfbService.replaceFile(t_fjcfbDto);
					if (!replaceFile)
						return null;
				}else{
					//备份文件
					String wjlj = p.dCode(t_fjcfbDto.getWjlj());
					String backWjlj = wjlj.substring(0, wjlj.lastIndexOf(".")) + "_back" + wjmkzm;
					boolean backflag = copyFile(wjlj,backWjlj);
					if (!backflag) {
						return null;
					}
				}
			}
		}
		return wjglDto;
	}

	@Override
	public List<WjglDto> getInsertStatsInfo(WjglDto wjglDto) {
		return dao.getInsertStatsInfo(wjglDto);
	}

	@Override
	public List<WjglDto> getUpdateStatsInfo(WjglDto wjglDto) {
		return dao.getUpdateStatsInfo(wjglDto);
	}

	/**
	 * 文件修改时间
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateTimeByWjid(WjglDto wjglDto) {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(wjglDto.getPzsj())){
			String shlb;
			if(StringUtil.isNotBlank(wjglDto.getYwjid())){
				shlb = AuditTypeEnum.QUALITY_FILE_MOD.getCode();
			}else{
				shlb = AuditTypeEnum.QUALITY_FILE_ADD.getCode();
			}
			ShxxDto shxxDto = new ShxxDto();
			shxxDto.setShlb(shlb);
			shxxDto.setYwid(wjglDto.getWjid());
			List<ShxxDto> shxxDtos = shxxService.getShxxOrderByPass(shxxDto);
			if(CollectionUtils.isEmpty(shxxDtos))
				return false;
			SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
			long ts;
			try {
				long d1 = formater.parse(wjglDto.getPzsj()).getTime();
				long d2 = formater.parse(shxxDtos.get(shxxDtos.size()-1).getShsj()).getTime();
				ts = (d1-d2)/(1000 * 60 * 60 * 24);
				
				//更新审核信息
				shxxDto.setTs(String.valueOf(ts));
				boolean isSuccess = shxxService.auditmodSaveTime(shxxDto);
				if(!isSuccess)
					return false;
				
				shxxDtos = shxxService.getShxxOrderByPass(shxxDto);
				//更新列表时间
				wjglDto.setBzsj(shxxDtos.get(0).getShsj());
				if(shxxDtos.size() > 1){
					wjglDto.setShsj(shxxDtos.get(1).getShsj());
				}else{
					wjglDto.setShsj(shxxDtos.get(0).getShsj());
				}
				wjglDto.setPzsj(shxxDtos.get(shxxDtos.size()-1).getShsj());
				int result = dao.updateTimeByWjid(wjglDto);
				if(result < 1)
					return false;
				//自动转换签名
				WjglDto t_wjglDto = new WjglDto();
				if(StringUtil.isNotBlank(wjglDto.getPresfth())){
					t_wjglDto.setSfth(wjglDto.getPresfth());
				}else if(StringUtil.isNotBlank(wjglDto.getSfth())){
					t_wjglDto.setSfth(wjglDto.getSfth());
				}else{
					t_wjglDto.setSfth("0");
				}
				if(StringUtil.isNotBlank(wjglDto.getPrezhgz())){
					t_wjglDto.setZhgz(wjglDto.getPrezhgz());
				}else if(StringUtil.isNotBlank(wjglDto.getZhgz())){
					t_wjglDto.setZhgz(wjglDto.getZhgz());
				}else{
					t_wjglDto.setZhgz("2");
				}
				t_wjglDto.setWjid(wjglDto.getWjid());
				transferDocument(t_wjglDto);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			return true;
		}
		return false;
	}

	/**
	 * 根据文件ID查询附件表信息
	 */
	@Override
	public List<FjcfbDto> selectFjcfbDtoByWjid(String wjid) {
		// TODO Auto-generated method stub
		List<FjcfbDto> fjcfbDtos= dao.selectFjcfbDtoByWjid(wjid);
		for (FjcfbDto fjcfbDto : fjcfbDtos) {
			String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
			fjcfbDto.setWjmhz(wjmhz);
		}
		return fjcfbDtos;
	}
	
	/**
	 * 根据文件ID查询工作管理信息
	 */
	@Override
	public List<GzglDto> selectGzglByWjid(WjglDto wjglDto) {
		// TODO Auto-generated method stub
		return dao.selectGzglByWjid(wjglDto);
	}
	
	/**
	 * 根据根文件ID查询文件管理信息
	 */
	@Override
	public List<WjglDto> getDtoListByGwjid(WjglDto wjglDto) {
		// TODO Auto-generated method stub
		List<WjglDto> wjglDtoList = dao.getDtoListByGwjid(wjglDto);
		for (WjglDto dto : wjglDtoList) {
			List<FjcfbDto> fjcfbDtos = dao.selectFjcfbDtoByWjid(dto.getWjid());
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				String wjmhz = fjcfbDto.getWjm().substring(fjcfbDto.getWjm().lastIndexOf(".") + 1);
				fjcfbDto.setWjmhz(wjmhz);
			}
			dto.setFjcfbDtos(fjcfbDtos);
		}
		return wjglDtoList;
	}
	
	/**
	 * 根据文件ID通过关联文件ID查询文件数量
	 */
	@Override
	public boolean selectGlwjidCountByWjid(WjglDto wjglDto) {
		// TODO Auto-generated method stub
		int result = dao.selectGlwjidCountByWjid(wjglDto);
		return result > 0;
	}
	
	/**
	 * 获取修改页面信息
	 */
	@Override
	public WjglDto modDocument(WjglDto wjglDto) {
		// TODO Auto-generated method stub
		WjglDto t_wjglDto = dao.getDto(wjglDto);
		//判断关联文件是否为空
		/*if(t_wjglDto==null || !StringUtil.isBlank(t_wjglDto.getGlwjid())){
			return null;
		}*/
		//判断文件审核类型
		boolean result = selectGlwjidCountByWjid(wjglDto);
		if(result){//QUALITY_FILE_MOD
			t_wjglDto.setAuditModType(AuditTypeEnum.QUALITY_FILE_MOD.getCode());
		}else{//QUALITY_FILE_ADD
			t_wjglDto.setAuditType(AuditTypeEnum.QUALITY_FILE_ADD.getCode());
		}
		if ("submit".equals(wjglDto.getLjbj())){
			t_wjglDto.setFormAction("submit");
		}else if ("advancedmod".equals(wjglDto.getLjbj())){
			t_wjglDto.setFormAction("advancedmod");
		}else if ("audit".equals(wjglDto.getLjbj())){
			t_wjglDto.setFormAction("audit");
		}else {
			t_wjglDto.setFormAction("mod");
		}
		//获取文件类型
		t_wjglDto.setYwlx(BusTypeEnum.IMP_DOCUMENT.getCode());
		return t_wjglDto;
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
			logger.error("开始传输文件到转换服务器，文件名为" + fileName);
			File t_file = new File(fileName);
			//文件不存在不做任何操作
			if(!t_file.exists())
				return true;
			
			byte[] bytesArray = new byte[(int) t_file.length()];
			
			FileInputStream t_fis = new FileInputStream(t_file);
			t_fis.read(bytesArray); //read file into bytes[]
			t_fis.close();
			//需要给文件的名称
			ByteArrayResource contentsAsResource = new ByteArrayResource(bytesArray) {
				@Override
			    public String getFilename() {
			        return t_file.getName();
			    }
			};
			MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
			paramMap.add("file", contentsAsResource);

			RestTemplate t_restTemplate = new RestTemplate();
			//发送文件到服务器
			String reString = t_restTemplate.postForObject("http://" + FTP_URL + ":8756/file/uploadWordFile", paramMap, String.class);

			logger.error("传输文件正常结束");
			
			return "OK".equals(reString);
		}catch(Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Map<String,Object> bulkimportsSaveDocument(WjglDto wjglDto) throws BusinessException{
		Map<String,Object> map = new HashMap<>();
		if(!CollectionUtils.isEmpty(wjglDto.getFjids())) {
			List<FjcfbDto> fjcfblist=new ArrayList<>();//用于存放解压后的文件
			List<FjcfbDto> redisList = fjcfbService.getRedisList(wjglDto.getFjids());
			log.error("文件获取redis(1)--t_redisLis={}",JSON.toJSONString(redisList));
			for (FjcfbDto value : redisList) {
				String ywlx = value.getYwlx();
				String fjid = value.getFjid();
				//判断是否为压缩文件
				String wjlj = value.getWjlj();
				int index = (wjlj.lastIndexOf(".") > 0) ? wjlj.lastIndexOf(".") : wjlj.length() - 1;
				String suffix = wjlj.substring(index);
				if (".zip".equals(suffix)) {
					int t_index = wjlj.lastIndexOf("/");
					String folder = wjlj.substring(0, t_index);
					String unZipFile = ZipUtil.unZipFile(folder, wjlj);
					File dirFile = new File(unZipFile);
					if (dirFile.exists()) {
						File[] files = dirFile.listFiles();
						if (files != null) {
							for (File fileChildDir : files) {
								if (fileChildDir.isFile()) {
									FjcfbDto fjcfbDto = new FjcfbDto();
									String wjm = fileChildDir.getName();
									//根据日期创建文件夹
									String storePath = prefix + releaseFilePath + ywlx + "/" + "UP" +
											DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" +
											DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/" + "UP" +
											DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
									mkDirs(storePath);
									String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
									String t_suffix = fileChildDir.getName().substring(fileChildDir.getName().lastIndexOf("."));
									String saveName = t_name + t_suffix;
									uploadFile(fileChildDir, storePath + "/" + saveName);
									fjcfbDto.setFjid(StringUtil.generateUUID());
									fjcfbDto.setWjm(wjm);
									int indexString = wjm.lastIndexOf('.');
									String preFjid = wjm.substring(0,indexString);
									fjcfbDto.setPrefjid(preFjid);
									DBEncrypt bpe = new DBEncrypt();
									fjcfbDto.setFwjlj(bpe.eCode(storePath));
									fjcfbDto.setFwjm(bpe.eCode(saveName));
									fjcfbDto.setWjlj(bpe.eCode(storePath + "/" + saveName));
									fjcfbDto.setYwlx(ywlx);
									fjcfbDto.setZhbj("0");
									fjcfblist.add(fjcfbDto);
								}
							}
						}
					}
					if(!CollectionUtils.isEmpty(fjcfblist)){
						List<FjcfbDto> newFjcfbDto = new ArrayList<>();
						fjcfblist.stream().filter(distinctByKey(p -> p.getPrefjid()))  //filter保留true的值
								.forEach(newFjcfbDto::add);
						List<WjglDto> wjglDtoList = new ArrayList<>();
						List<WjssdwDto> wjssdwDtoList = new ArrayList<>();
						String wjms = "";
						String wjbhs = "";
						String ywids = "";
						for (FjcfbDto fjcfbDto : newFjcfbDto){
							String wjmString = fjcfbDto.getPrefjid();
							if(StringUtil.isNotBlank(wjmString)){
								WjglDto wjglDtoT = new WjglDto();
								String wjid = StringUtil.generateUUID();
								wjglDtoT.setWjid(wjid);
								wjglDtoT.setGwjid(wjid);
								wjglDtoT.setWjlb(wjglDto.getWjlb());
								wjglDtoT.setWjfl(wjglDto.getWjfl());
								wjglDtoT.setWjbh(wjmString.substring(0, wjmString.indexOf('-')));
								wjglDtoT.setWjmc(wjmString.substring(wjmString.indexOf(' ') + 1));
								wjglDtoT.setBbh(wjmString.substring(wjmString.lastIndexOf('-') + 1, wjmString.indexOf(' ', wjmString.indexOf('-') + 1)));
								wjglDtoT.setSxrq(wjglDto.getSxrq());
								wjglDtoT.setBz(wjglDto.getBz());
								wjglDtoT.setZt(StatusEnum.CHECK_NO.getCode());
								wjglDtoT.setBzry(wjglDto.getBzry());
								wjglDtoT.setGgly("/");
								wjglDtoT.setSyxwj("/");
								wjglDtoT.setLrry(wjglDto.getBzry());
								wjglDtoList.add(wjglDtoT);
								ywids = ywids + "," + wjglDtoT.getWjid();
								wjms = wjms + "," + wjglDtoT.getWjmc();
								wjbhs = wjbhs + "," + wjglDtoT.getWjbh();
								WjssdwDto wjssdwDto = new WjssdwDto();
								wjssdwDto.setWjid(wjid);
								wjssdwDto.setJgid(wjglDto.getJgid());
								wjssdwDto.setXh("1");
								wjssdwDtoList.add(wjssdwDto);
								for (FjcfbDto fjcfbDtoT :fjcfblist){
									if(StringUtil.isNotBlank(fjcfbDto.getPrefjid()) && fjcfbDto.getPrefjid().equals(fjcfbDtoT.getPrefjid())){
										fjcfbDtoT.setYwid(wjid);
										String xh = String.valueOf(Integer.parseInt(StringUtil.isNotBlank(fjcfbDto.getXh())?fjcfbDto.getXh():"0")+1);
										fjcfbDto.setXh(xh);
										fjcfbDtoT.setXh(xh);
									}
								}
							}
						}
						ywids = ywids.substring(1);
						wjms = wjms.substring(1);
						wjbhs = wjbhs.substring(1);
						map.put("ywids",ywids);
						WjglDto wjglDtoS = new WjglDto();
						wjglDtoS.setWjbhs(wjbhs);
						wjglDtoS.setIds(wjms);
						List<WjglDto> wjglDtos = dao.queryWjglDto(wjglDtoS);
						if(!CollectionUtils.isEmpty(wjglDtos)){
							String msg = "";
							for (WjglDto wjglDtoL : wjglDtos){
								msg = msg + "," + wjglDtoL.getWjbh();
							}
							msg = msg.substring(1);
							throw new BusinessException("msg", "以下文件系统已存在(文件编码重复):"+msg);
						}
						boolean result = fjcfbService.batchInsertFjcfb(fjcfblist);
						if(!result){
							throw new BusinessException("msg", "批量新增附件失败!");
						}
						if(!CollectionUtils.isEmpty(wjglDtoList)){
							result = dao.insertWjglDtos(wjglDtoList);
							if(!result){
								throw new BusinessException("msg", "批量新增文件失败!");
							}
						}
						if(!CollectionUtils.isEmpty(wjssdwDtoList)){
							result = wjssdwService.insertDtoList(wjssdwDtoList);
							if(!result){
								throw new BusinessException("msg", "批量新增文件所属单位失败!");
							}
						}
					}else {
						throw new BusinessException("msg", "未找到压缩包内的文件!");
					}
				} else {
					throw new BusinessException("msg", "请选择zip格式文件进行上传!");
				}
			}
		}else {
			throw new BusinessException("msg", "未找到上传附件!");
		}
		return map;
	}

	static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {

		Map<Object,Boolean> seen = new ConcurrentHashMap<>();

//putIfAbsent方法添加键值对，如果map集合中没有该key对应的值，则直接添加，并返回null，如果已经存在对应的值，则依旧为原来的值。

//如果返回null表示添加数据成功(不重复)，不重复(null==null :TRUE)

		return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;

	}


	/**
	 * @Description: 文件上传
	 * @param file
	 * @param filePath
	 * @return void
	 * @Author: 郭祥杰
	 * @Date: 2024/12/5 16:05
	 */
	private void uploadFile(File file, String filePath) {
		byte[] buffer = new byte[4096];
		FileOutputStream fos = null;
		BufferedOutputStream output = null;

		InputStream fis = null;
		BufferedInputStream input = null;
		try {
			fis = new FileInputStream(file.getPath());
			input = new BufferedInputStream(fis);

			fos = new FileOutputStream(filePath);
			output = new BufferedOutputStream(fos);
			int n;
			while ((n = input.read(buffer, 0, 4096)) > -1) {
				output.write(buffer, 0, n);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			closeStream(new Closeable[] {
					fis, input, output, fos });
		}

	}
	/**
	 * @Description: 关闭流
	 * @param streams
	 * @return void
	 * @Author: 郭祥杰
	 * @Date: 2024/12/5 16:05
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
				System.out.println(e.getMessage());
			}
		}
	}
	/**
	 * 根据路径创建文件
	 */
	private void mkDirs(String storePath){
		File file = new File(storePath);
		if (file.isDirectory()) {
			return;
		}
		file.mkdirs();
	}
	/**
	 * 从原路径剪切到目标路径
	 */
	private boolean CutFile(String s_srcFile,String s_distFile) {
		boolean flag = false;
		//路径如果为空则直接返回错误
		if(StringUtil.isBlank(s_srcFile)|| StringUtil.isBlank(s_distFile))
			return flag;
		
		File srcFile = new File(s_srcFile);
		File distFile = new File(s_distFile);
		//文件不存在则直接返回错误
		if(!srcFile.exists())
			return flag;
		//目标文件已经存在
		if(distFile.exists()) {
			if(true) {
				srcFile.renameTo(distFile);
				flag = true;
			}
		}else {
			srcFile.renameTo(distFile);
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 文件转换
	 */
	@Override
	public boolean transferDocument(WjglDto wjglDto){
		boolean result = update(wjglDto);
		if (!result)
			return false;
		StringBuilder shrList = new StringBuilder();
		StringBuilder shsjList = new StringBuilder();
		StringBuilder yhmList = new StringBuilder();
		if("0".equals(wjglDto.getZhgz())){
			WjglDto t_wjglDto = dao.getDtoById(wjglDto.getWjid());
			wjglDto.setSxrq(t_wjglDto.getSxrq());
			List<String> shlbs = new ArrayList<>();
			shlbs.add(AuditTypeEnum.QUALITY_FILE_ADD.getCode());
			shlbs.add(AuditTypeEnum.QUALITY_FILE_MOD.getCode());
			ShxxDto shxxParam = new ShxxDto();
			shxxParam.setShlbs(shlbs);
			shxxParam.setYwid(wjglDto.getWjid());
			List<ShxxDto> shxxList = shxxService.getShxxOrderByPass(shxxParam);
			if(!CollectionUtils.isEmpty(shxxList)) {
				for (ShxxDto shxxDto : shxxList) {
					shrList.append(",").append(shxxDto.getLrry());
					shsjList.append(",").append(shxxDto.getShsj());
					yhmList.append(",").append(shxxDto.getYhm());
				}
				shrList = new StringBuilder(shrList.substring(1));
				shsjList = new StringBuilder(shsjList.substring(1));
				yhmList = new StringBuilder(yhmList.substring(1));
			}
		}
		List<FjcfbDto> fjcfbDtos = dao.selectFjcfbDtoByWjid(wjglDto.getWjid());
		if(!CollectionUtils.isEmpty(fjcfbDtos)) {
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				DBEncrypt p = new DBEncrypt();
				int begin = fjcfbDto.getWjm().lastIndexOf(".");
				String wjmkzm = fjcfbDto.getWjm().substring(begin);
				if ((wjmkzm.equalsIgnoreCase(".pptx"))||(wjmkzm.equalsIgnoreCase(".ppt"))||(wjmkzm.equalsIgnoreCase(".doc")) || (wjmkzm.equalsIgnoreCase(".docx")) || (wjmkzm.equalsIgnoreCase(".xls")) || (wjmkzm.equalsIgnoreCase(".xlsx"))) {
					String wjljjm = p.dCode(fjcfbDto.getWjlj());
					//连接服务器
					boolean sendFlg = sendWordFile(wjljjm);
					if (sendFlg) {
						Map<String, String> map = new HashMap<>();
						String fwjm = p.dCode(fjcfbDto.getFwjm());
						map.put("wordName", fwjm);
						map.put("fwjlj", fjcfbDto.getFwjlj());
						map.put("fjid", fjcfbDto.getFjid());
						map.put("ywlx", fjcfbDto.getYwlx());
						map.put("shrList", shrList.toString());
						map.put("shsjList", shsjList.toString());
						map.put("yhmList", yhmList.toString());
						map.put("sxrq", wjglDto.getSxrq());
						map.put("wjid", wjglDto.getWjid());
						map.put("signflg", wjglDto.getZhgz());
						map.put("replaceflg", wjglDto.getSfth());
						map.put("MQDocOkType", DOC_OK);
						//发送Rabbit消息转换pdf
						amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(map));
					}
				} else if ((wjmkzm.equalsIgnoreCase(".pdf")) || (wjmkzm.equalsIgnoreCase(".PDF"))) {
					if (StringUtil.isNotBlank(fjcfbDto.getYswjm())) {//判断如果有原始文件
						int ysbegin = fjcfbDto.getYswjm().lastIndexOf(".");
						String yswjmkzm = fjcfbDto.getYswjm().substring(ysbegin);
						if ((yswjmkzm.equalsIgnoreCase(".doc")) || (yswjmkzm.equalsIgnoreCase(".docx")) || (yswjmkzm.equalsIgnoreCase(".xls")) || (yswjmkzm.equalsIgnoreCase(".xlsx"))) {
							//如果为doc，直接修改
							fjcfbDto.setWjm(fjcfbDto.getYswjm());
							fjcfbDto.setWjlj(fjcfbDto.getYswjlj());
							int fbegin = p.dCode(fjcfbDto.getFwjm()).lastIndexOf(".");
							fjcfbDto.setFwjm(p.eCode(p.dCode(fjcfbDto.getFwjm()).substring(0, fbegin) + yswjmkzm));
							fjcfbService.updateFjljFail(fjcfbDto);
							//服务器转换
							String wjljjm = p.dCode(fjcfbDto.getYswjlj());
							//连接服务器
							boolean sendFlg = sendWordFile(wjljjm);
							if (sendFlg) {
								Map<String, String> map = new HashMap<>();
								String fwjm = p.dCode(fjcfbDto.getFwjm());
								map.put("wordName", fwjm);
								map.put("fwjlj", fjcfbDto.getFwjlj());
								map.put("fjid", fjcfbDto.getFjid());
								map.put("ywlx", fjcfbDto.getYwlx());
								map.put("shrList", shrList.toString());
								map.put("shsjList", shsjList.toString());
								map.put("yhmList", yhmList.toString());
								map.put("sxrq", wjglDto.getSxrq());
								map.put("wjid", wjglDto.getWjid());
								map.put("signflg", wjglDto.getZhgz());
								map.put("replaceflg", wjglDto.getSfth());
								map.put("MQDocOkType", DOC_OK);
								//发送Rabbit消息转换pdf
								amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(map));
							}
						} else if ((yswjmkzm.equalsIgnoreCase(".pdf")) || (yswjmkzm.equalsIgnoreCase(".PDF"))) {
							//如果为pdf，复制替换，然后本地转换
							String wjlj = p.dCode(fjcfbDto.getYswjlj());
							String backWjlj = wjlj.substring(0, wjlj.lastIndexOf(".")) + "_back" + wjmkzm;
							File file = new File(fjcfbDto.getYswjlj());
							if (file.exists()) {
								file.delete();
							}
							result = copyFile(wjlj, backWjlj);
							if (!result)
								return false;
							fjcfbDto.setWjlj(fjcfbDto.getYswjlj());
							fjcfbDto.setZhwjxx(p.eCode(backWjlj));

							String wjm = fjcfbDto.getYswjm();
							String backWjm = wjm.substring(0, wjm.lastIndexOf(".")) + "_back" + wjmkzm;
							String back_Wjlj = wjlj.substring(wjlj.lastIndexOf("/") + 1);
							String backFwjm = back_Wjlj.substring(0, back_Wjlj.lastIndexOf(".")) + "_back" + wjmkzm;
							// 判断是否文件转换  1 表示转换，0 表示不转换
							if (StringUtil.isNotBlank(wjglDto.getSfth()) && wjglDto.getSfth().equals("1")) {
								fjcfbDto.setWjm(backWjm);
								fjcfbDto.setWjlj(p.eCode(backWjlj));
								fjcfbDto.setFwjm(p.eCode(backFwjm));
								fjcfbService.replaceFile(fjcfbDto);
							} else {
								fjcfbDto.setWjm(fjcfbDto.getYswjm());
								fjcfbDto.setWjlj(fjcfbDto.getYswjlj());
								fjcfbDto.setFwjm(p.eCode(wjlj.substring(wjlj.lastIndexOf("/")) + 1));
								fjcfbService.replaceFile(fjcfbDto);
								fjcfbDto.setWjm(backWjm);
								fjcfbDto.setWjlj(p.eCode(backWjlj));
								fjcfbDto.setFwjm(p.eCode(backFwjm));
							}
							if ("0".equals(wjglDto.getZhgz()) || "1".equals(wjglDto.getZhgz())) {
								fjcfbDto.setShrs(shrList.toString());
								fjcfbDto.setShsjs(shsjList.toString());
								fjcfbDto.setShyhms(yhmList.toString());
								SyxxDto syxxDto = syxxService.getDtoByWjlb(BusTypeEnum.IMP_DOCUMENT.getCode());
								if (syxxDto != null) {
									attachHelper.addWatermark(fjcfbDto, wjglDto.getZhgz(), wjglDto.getSxrq(), syxxDto);
								} else {
									logger.error("水印信息不存在！");
								}
							}
						}
					} else {
						//没有原始文件，修改，然后本地转换
						String wjlj = p.dCode(fjcfbDto.getWjlj());
						String backWjlj = wjlj.substring(0, wjlj.lastIndexOf(".")) + "_back" + wjmkzm;
						boolean backflag = copyFile(wjlj, backWjlj);
						if (!backflag)
							return false;
						fjcfbDto.setYswjlj(fjcfbDto.getWjlj());
						fjcfbDto.setYswjm(fjcfbDto.getWjm());
						fjcfbDto.setZhwjxx(p.eCode(backWjlj));
						String wjm = fjcfbDto.getWjm();
						String backWjm = wjm.substring(0, wjm.lastIndexOf(".")) + "_back" + wjmkzm;
						String back_Wjlj = wjlj.substring(wjlj.lastIndexOf("/") + 1);
						String backFwjm = back_Wjlj.substring(0, back_Wjlj.lastIndexOf(".")) + "_back" + wjmkzm;
						// 判断是否文件转换  1 表示转换，0 表示不转换
						if (StringUtil.isNotBlank(wjglDto.getSfth()) && wjglDto.getSfth().equals("1")) {
							fjcfbDto.setWjm(backWjm);
							fjcfbDto.setWjlj(p.eCode(backWjlj));
							fjcfbDto.setFwjm(p.eCode(backFwjm));
							fjcfbService.replaceFile(fjcfbDto);
						} else {
							fjcfbService.replaceFile(fjcfbDto);
							fjcfbDto.setWjm(backWjm);
							fjcfbDto.setWjlj(p.eCode(backWjlj));
							fjcfbDto.setFwjm(p.eCode(backFwjm));
						}

						if ("0".equals(wjglDto.getZhgz()) || "1".equals(wjglDto.getZhgz())) {
							fjcfbDto.setShrs(shrList.toString());
							fjcfbDto.setShsjs(shsjList.toString());
							fjcfbDto.setShyhms(yhmList.toString());
							//查询水印信息
							SyxxDto syxxDto = syxxService.getDtoByWjlb(BusTypeEnum.IMP_DOCUMENT.getCode());
							if (syxxDto != null) {
								attachHelper.addWatermark(fjcfbDto, wjglDto.getZhgz(), wjglDto.getSxrq(), syxxDto);
							} else {
								logger.error("水印信息不存在！");
							}

						}
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 复制文件
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
			logger.error(e.getMessage());
		} finally {
	        try {
				if (in!=null){
					in.close();
				}
	        	if (out!=null){
					out.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error(e.getMessage());
			}
	    }
		return flag;
    }
    
	@Override
	public boolean existCheck(String fieldName, String value) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages) {
		// TODO Auto-generated method stub
		try{
			WjglDto wjglDto = (WjglDto)baseModel;
			/*//过滤重复文件名和文件编号
			List<WjglDto> wjglDtos = dao.selectDocumentRepeat(wjglDto);
			if(!CollectionUtils.isEmpty(wjglDtos)) {
				throw new BusinessException("msg",xxglService.getMsg("WCOM_DOC00003"));
			}*/
			//对应文件
			String s=wjglDto.getYsblj().substring(0,wjglDto.getYsblj().lastIndexOf("."));
			File dirFile = new File(s);
			if(dirFile.exists()){
				List<File> files = commonservice.getAllFile(new ArrayList<>(), dirFile);
				if(!CollectionUtils.isEmpty(files)) {
		            for (File fileChildDir : files) {
		                if (fileChildDir.isFile()) {
		                	//文件名
		                	String fileName = fileChildDir.getName().substring(0,fileChildDir.getName().lastIndexOf("."));
		                	if(fileName.equals(wjglDto.getWjmc())){
		                		//剪切文件至正式表，新增附件表
		                		String storePath = prefix + releaseFilePath + BusTypeEnum.IMP_DOCUMENT.getCode() +"/"+ "UP"+ 
		                				DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+ 
		                				DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
		                				DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		                		mkDirs(storePath);
		                		
		                		int index = (fileChildDir.getName().lastIndexOf(".") > 0) ? fileChildDir.getName().lastIndexOf(".") : fileChildDir.getName().length() - 1;
		        				String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
		        				String suffix = fileChildDir.getName().substring(index);
		        				String saveName = t_name + suffix;
		                		
		                		String newPath = storePath + "/" + saveName;
		                		String path = fileChildDir.getPath();
		                		boolean cutFile = CutFile(path, newPath);
		                		if(cutFile){
		                			//新增文件
		                			String wjid = StringUtil.generateUUID();
		                			wjglDto.setWjid(wjid);
		                			wjglDto.setBzry(user.getYhid());
		                			wjglDto.setZt(StatusEnum.CHECK_NO.getCode());
		                			dao.insert(wjglDto);
		                			wjglDto.setJgid("");
		                			if(StringUtil.isNotBlank(wjglDto.getJgid())){
		                				//新增文件所属单位
		                				WjssdwDto wjssdwDto= new WjssdwDto();
		                				wjssdwDto.setWjid(wjglDto.getWjid());
		                				wjssdwDto.setJgid(wjglDto.getJgid());
		                				wjssdwDto.setXh("1");
		                				boolean addwjssdw= wjssdwService.insert(wjssdwDto);
		                				if(!addwjssdw)
		                					return false;
		                			}else{
		                				//获取所属部门
			                			Map<String, Object> map = commonService.getDepartmentByUser(null, user.getYhid(), new HashMap<>());
			                			@SuppressWarnings("unchecked")
			                			List<DepartmentDto> departmentDtos = (List<DepartmentDto>) map.get("departmentDtos");
			                			if(!CollectionUtils.isEmpty(departmentDtos)){
			                				//新增文件所属单位
			                				WjssdwDto wjssdwDto= new WjssdwDto();
			                				wjssdwDto.setWjid(wjglDto.getWjid());
			                				wjssdwDto.setJgid(departmentDtos.get(0).getJgid());
			                				wjssdwDto.setXh("1");
			                				boolean addwjssdw= wjssdwService.insert(wjssdwDto);
			                				if(!addwjssdw)
			                					return false;
			                			}
		                			}
		                			shgcService.importBusinessCommit(wjglDto.getWjid(), AuditTypeEnum.QUALITY_FILE_ADD.getCode(), user);
		                			//新增附件
		                			//将正式文件夹路径更新至数据库
		                			DBEncrypt bpe = new DBEncrypt();
		                			FjcfbDto fjcfbDto = new FjcfbDto();
		                			fjcfbDto.setFjid(StringUtil.generateUUID());
		                			fjcfbDto.setYwid(wjid);
		                			fjcfbDto.setXh("1");
		                			fjcfbDto.setYwlx(BusTypeEnum.IMP_DOCUMENT.getCode());
		                			fjcfbDto.setWjm(fileChildDir.getName());
		                			fjcfbDto.setWjlj(bpe.eCode(newPath));
		                			fjcfbDto.setFwjlj(bpe.eCode(storePath));
		                			fjcfbDto.setFwjm(bpe.eCode(saveName));
		                			fjcfbDto.setZhbj("0");
		                			fjcfbService.insert(fjcfbDto);
		                			//word转换成pdf文件
		            				DBEncrypt p = new DBEncrypt();
		            				if((suffix.equalsIgnoreCase(".pptx"))||(suffix.equalsIgnoreCase(".ppt"))||(suffix.equalsIgnoreCase(".doc"))||(suffix.equalsIgnoreCase(".docx"))||(suffix.equalsIgnoreCase(".xls"))||(suffix.equalsIgnoreCase(".xlsx"))) {
		            					String wjljjm=p.dCode(fjcfbDto.getWjlj());
		            					//连接服务器
		            					boolean sendFlg=sendWordFile(wjljjm);
		            					if(sendFlg) {
		            						Map<String,String> paramMap=new HashMap<>();
		            						String fwjm=p.dCode(fjcfbDto.getFwjm());
		            						paramMap.put("wordName", fwjm);
		            						paramMap.put("fwjlj",fjcfbDto.getFwjlj());
		            						paramMap.put("fjid",fjcfbDto.getFjid());
		            						paramMap.put("ywlx",fjcfbDto.getYwlx());
		            						paramMap.put("MQDocOkType",DOC_OK);
		            						//发送Rabbit消息转换pdf
		            						amqpTempl.convertAndSend("doc2pdf_exchange",MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(paramMap));
		            					}
		            				}else if((suffix.equalsIgnoreCase(".pdf")) || (suffix.equalsIgnoreCase(".PDF"))){
		            					// 更改原始文件路径，复制一份转换文件
		            					fjcfbDto.setYswjlj(fjcfbDto.getWjlj());
		            					fjcfbDto.setYswjm(fjcfbDto.getWjm());
		            					String wjlj = p.dCode(fjcfbDto.getWjlj());
		            					String backWjlj = wjlj.substring(0, wjlj.lastIndexOf(".")) + "_back" + suffix;
		            					copyFile(wjlj,backWjlj);
		            					fjcfbDto.setZhwjxx(p.eCode(backWjlj));
		            					boolean replaceFile = fjcfbService.replaceFile(fjcfbDto);
		            					if (!replaceFile)
		            						return false;
		            				}else{
		            					//备份文件
		            					String wjlj = p.dCode(fjcfbDto.getWjlj());
		            					String backWjlj = wjlj.substring(0, wjlj.lastIndexOf(".")) + "_back" + suffix;
		            					boolean backflag = copyFile(wjlj,backWjlj);
		            					if (!backflag) {
		            						return false;
		            					}
		            				}
		                			break;
		                		}
		                	}
		                }
		            }
		        }
			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}
		
		return true;
	}
	@Override
	public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
		// TODO Auto-generated method stub
		if(tranTrack.equalsIgnoreCase("WJ001")){//文件分类
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb(BasicDataTypeEnum.DOCUMENT_TYPE.getCode());
			jcsjDto.setCsdm(value);
			JcsjDto t_jcsjDto = jcsjService.getDto(jcsjDto);
			if(t_jcsjDto== null){
				errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
				.append("列，找不到对应的文件分类的编码，单元格值为：").append(value).append("；<br>");
			}
			else{
				recModel.setWjfl(t_jcsjDto.getCsid());
				return t_jcsjDto.getCsid();
			}
		}else if(tranTrack.equalsIgnoreCase("WJ002")){//文件类别
			if(StringUtil.isBlank(recModel.getWjfl())){
				errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
				.append("列，请先设置相应的文件分类的编码，单元格值为：").append(value).append("；<br>");
				return null;
			}
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb(BasicDataTypeEnum.DOCUMENT_SUBTYPE.getCode());
			jcsjDto.setCsdm(value);
			jcsjDto.setFcsid(recModel.getWjfl());
			JcsjDto t_jcsjDto = jcsjService.getDto(jcsjDto);
			if(t_jcsjDto== null){
				errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
				.append("列，找不到对应的文件类别的编码，单元格值为：").append(value).append("；<br>");
			}
			else
				return t_jcsjDto.getCsid();
		}else if(tranTrack.equalsIgnoreCase("WJ003")){//用户所属机构
			List<DepartmentDto> departmentDtos = commonservice.getJgxxDtoByJgmc(value);
			if(departmentDtos != null && departmentDtos.size() == 1){
				return departmentDtos.get(0).getJgid();
			}else if(departmentDtos != null && departmentDtos.size() > 1){
				errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
				.append("列，匹配到多个机构，请完善机构名称，单元格值为：").append(value).append("；<br>");
			}else{
				errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
				.append("列，找不到对应的机构信息，单元格值为：").append(value).append("；<br>");
			}
		}
		return null;
	}
	@Override
	public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * 获取文件列表
	 */
	@Override
	public List<WjglDto> getPagedListDocument(WjglDto wjglDto) {
		// TODO Auto-generated method stub
		List<WjglDto> t_List = dao.getPagedListDocument(wjglDto);
		jcsjService.handleCodeToValue(t_List,
				new BasicDataTypeEnum[] {BasicDataTypeEnum.DOCUMENT_TYPE,BasicDataTypeEnum.DOCUMENT_SUBTYPE}, 
				new String[] {"wjfl:wjflmc","wjlb:wjlbmc"});
		return t_List;
	}
	
	/**
	 * 修改文件关联关系
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateRelated(WjglDto wjglDto) {
		// TODO Auto-generated method stub
		boolean result = dao.updatePreviousDto(wjglDto);
		if(!result)
			return false;
		result = dao.updateCurrentDto(wjglDto);
		if(!result)
			return false;
		result = dao.updateCurGwjid(wjglDto);
		return result;
	}
	
	/**
	 * 根据搜索条件获取导出条数
	 */
	public int getCountForSearchExp(WjglDto wjglDto,Map<String,Object> params){
		return dao.getCountForSearchExp(wjglDto);
	}
	
	/**
	 * 根据搜索条件分页获取导出信息
	 */
	public List<WjglDto> getListForSearchExp(Map<String,Object> params){
		WjglDto wjglDto = (WjglDto)params.get("entryData");
		queryJoinFlagExport(params,wjglDto);
		return dao.getListForSearchExp(wjglDto);
	}
	
	/**
	 * 选中导出
	 */
	public List<WjglDto> getListForSelectExp(Map<String,Object> params){
		WjglDto wjglDto = (WjglDto) params.get("entryData");
		queryJoinFlagExport(params,wjglDto);
		return dao.getListForSelectExp(wjglDto);
	}
	
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String,Object> params,WjglDto wjglDto){
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuilder sqlParam = new StringBuilder();
		for(DcszDto dcszDto:choseList){
			if(dcszDto == null || dcszDto.getDczd() == null)
				continue;
			if(dcszDto.getDczd().equalsIgnoreCase("WJFL")){
				wjglDto.setWjfl_flg("Y");
			}else if(dcszDto.getDczd().equalsIgnoreCase("WJLB")){
				wjglDto.setWjlb_flg("Y");
			}
			sqlParam.append(",");
			if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		String sqlcs=sqlParam.toString();
		wjglDto.setSqlParam(sqlcs);
	}
	
	/**
	 * 检查标题定义，主要防止模板信息过旧
	 */
	public boolean checkDefined(List<Map<String,String>> defined) {
		return true;
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
		Map<String, Object> map = new HashMap<>();
		WjglDto wjglDto = dao.getDtoById(shgcDto.getYwid());
		map.put("jgid",wjglDto.getJgid());
		return map;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
		Map<String, Object> map =new HashMap<>();
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>)param.get("ywids");
		WjglDto wjglDto = new WjglDto();
		wjglDto.setIds(ids);
		List<WjglDto> dtoList = dao.getDtoByIds(wjglDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(WjglDto dto:dtoList){
				list.add(dto.getWjid());
			}
		}
		map.put("list",list);
		return map;
	}

	/**
	 * 将dto中有值的时，放入map中
	 */
	public Map<String,String> replaceMap(Map <String,String> map,Object ODto){
		Class<?> cls = ODto.getClass();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				if (field.get(ODto)!=null && field.get(ODto)!="" && field.get(ODto) instanceof String){
					map.put(field.getName(), (String) field.get(ODto));
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		for (;cls.getSuperclass()!=null;cls = cls.getSuperclass()){
			Field[] superfields = cls.getDeclaredFields();
			for (Field field : superfields) {
				field.setAccessible(true);
				try {
					if (field.get(ODto)!=null && field.get(ODto)!="" && field.get(ODto) instanceof String){
						map.put(field.getName(), (String) field.get(ODto));
					}
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		}
		return map;
	}

	/**
	 * 文件分类统计
	 
	 */
	public List<WjglDto> getDocumentClassStats(){
		return dao.getDocumentClassStats();
	}
	/**
	 * 文件月份统计
	 
	 */
	public List<WjglDto> getDocumentMonthStats(WjglDto wjglDto){
		return dao.getDocumentMonthStats(wjglDto);
	}

	/**
	 * 获取统计年份
	 */
	public List<WjglDto> getYearGroup(){
		return dao.getYearGroup();
	}
	/**
	 * @description 修改发放状态
	 */
	@Override
	public boolean updateFfzt(WjglDto wjglDto) {
		return dao.updateFfzt(wjglDto);
	}

	@Override
	public List<WjglDto> getDtoByIds(WjglDto wjglDto) {
		return dao.getDtoByIds(wjglDto);
	}
}
