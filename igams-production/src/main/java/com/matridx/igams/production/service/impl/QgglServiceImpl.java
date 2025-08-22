package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShlcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum_pub;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShlcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.ISpgwService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.ToolUtil;
import com.matridx.igams.production.dao.entities.QgcglDto;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgglModel;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.dao.entities.WlglDto;
import com.matridx.igams.production.dao.post.IQgglDao;
import com.matridx.igams.production.service.svcinterface.IQgcglService;
import com.matridx.igams.production.service.svcinterface.IQgglService;
import com.matridx.igams.production.service.svcinterface.IQgmxService;
import com.matridx.igams.production.service.svcinterface.IRdRecordService;
import com.matridx.igams.production.service.svcinterface.IWjglService;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.igams.production.util.XWPFPurchaseUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.RandomUtil;
import com.matridx.springboot.util.file.upload.ZipUtil;
import org.apache.commons.lang3.StringUtils;
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

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QgglServiceImpl extends BaseBasicServiceImpl<QgglDto, QgglModel, IQgglDao> implements IQgglService,IAuditService,IFileImport{

	@Autowired
	RedisUtil redisUtil;
	
	@Autowired
	IQgmxService qgmxService;
	
	@Autowired
	IQgcglService qgcglService;
	
	@Autowired
	private DingTalkUtil talkUtil;
	
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	ICommonService commonservice;
	
	@Autowired
	IDdxxglService ddxxglService;
	
	@Autowired
	IShgcService shgcService;
	
	@Autowired
	IShlcService shlcService;
	
	@Autowired
	IFjcfbService fjcfbService;
	
	@Autowired
	IDdfbsglService ddfbsglService;
	
	@Autowired
	IShxxService shxxService;
	
	//@Autowired
	//IQgqxglService qgqxglService;
	
	@Autowired
	IWlglService wlglService;
	
	@Autowired
	IDdspglService ddspglService;

	@Autowired
	IRdRecordService rdRecordService;

	@Autowired
	IJcsjService jcsjService;

	@Autowired
	ICommonService commonService;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.wechat.registerurl:}")
	private String registerurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Value("${matridx.systemflg.systemname:}")
    private String systemname;
    @Value("${matridx.rabbit.systemreceiveflg:}")
	private String systemreceiveflg;
    //是否发送rabbit标记     1：发送
  	@Value("${matridx.rabbit.systemconfigflg:}")
  	private String systemconfigflg;
  	@Value("${matridx.rabbit.preflg:}")
  	private String preRabbitFlg;
  	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
  	@Value("${matridx.fileupload.releasePath}")
	private String releaseFilePath;
	@Value("${matridx.fileupload.tempPath}")
	private String tempPath;
	@Value("${matridx.ftp.url:}")
	private final String FTP_URL = null;

	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private final String DOC_OK = null;
  	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
	@Value("${matridx.fileupload.prefix}")
	private String prefix;
	@Value("${matridx.fileupload.tempPath}")
	private String tempFilePath;
	@Value("${matridx.dingtalk.jumpdingtalkurl:}")
	private String jumpdingtalkurl;
	@Value("${sqlserver.matridxds.flag:}")
	private String matridxdsflag;
	@Autowired
	IWjglService wjglService;
	@Autowired
	IXtszService xtszService;
	@Autowired
	ISpgwService spgwService;
	@Autowired
	private IXxdyService xxdyService;
	
	private final Logger log = LoggerFactory.getLogger(QgglServiceImpl.class);
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(QgglDto qgglDto) {
		if(StringUtil.isBlank(qgglDto.getQgid())) {
			qgglDto.setQgid(StringUtil.generateUUID());
		}
		if(StringUtil.isBlank(qgglDto.getQglx())) {
			qgglDto.setQglx("0");
		}
		qgglDto.setZt(StatusEnum.CHECK_NO.getCode());
		int result=dao.insert(qgglDto);
		return result > 0;
	}
	
	/**
	 * 保存请购单
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean addSaveProdution (QgglDto qgglDto) throws BusinessException {
		boolean result=insertDto(qgglDto);
		if(!result) 
			return false;
		if (!CollectionUtils.isEmpty(qgglDto.getFjids())) {
			for (int j = 0; j < qgglDto.getFjids().size(); j++) {
				if(StringUtil.isNotBlank(qgglDto.getFjids().get(j))) {
					fjcfbService.save2RealFile(qgglDto.getFjids().get(j), qgglDto.getQgid());
					FjcfbDto fjcfbDto=new FjcfbDto();
					fjcfbDto.setFjid(qgglDto.getFjids().get(j));
					FjcfbDto t_fjcfbDto=fjcfbService.getDto(fjcfbDto);
					int begin=t_fjcfbDto.getWjm().lastIndexOf(".");
					String wjmkzm=t_fjcfbDto.getWjm().substring(begin);
					DBEncrypt p = new DBEncrypt();
					if((wjmkzm.equalsIgnoreCase(".doc"))||(wjmkzm.equalsIgnoreCase(".docx"))||(wjmkzm.equalsIgnoreCase(".xls"))||(wjmkzm.equalsIgnoreCase(".xlsx"))) {
						String wjljjm=p.dCode(t_fjcfbDto.getWjlj());
						//连接服务器
						boolean sendFlg=wjglService.sendWordFile(wjljjm);
						if(sendFlg) {
							Map<String,String> t_map=new HashMap<>();
							String fwjm=p.dCode(t_fjcfbDto.getFwjm());
							t_map.put("wordName", fwjm);
							t_map.put("fwjlj",t_fjcfbDto.getFwjlj());
							t_map.put("fjid",t_fjcfbDto.getFjid());
							t_map.put("ywlx",t_fjcfbDto.getYwlx());
							t_map.put("MQDocOkType",DOC_OK);
							//发送Rabbit消息转换pdf
							amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(t_map));
						}
					}
				}
			}
		}

		//获取请购明细数据
		List<QgmxDto> qgmxlist=qgglDto.getQgmxlist();
		if(!CollectionUtils.isEmpty(qgmxlist)) {
			//保存明细数据
			for (QgmxDto qgmxDto : qgmxlist) {
				qgmxDto.setLrry(qgglDto.getLrry());
				qgmxDto.setQgid(qgglDto.getQgid());
				qgmxDto.setSysl(qgmxDto.getSl());
				qgmxDto.setYssl(qgmxDto.getSl());
			}
			boolean addSaveQgmx=qgmxService.insertQgmx(qgmxlist);
			if(!addSaveQgmx)
				return false;

			for (QgmxDto qgmxDto : qgmxlist) {
				log.error("请购addSaveProdution-qgmxDto={}",JSON.toJSON(qgmxDto));
				//保存明细附件
				if (!CollectionUtils.isEmpty(qgmxDto.getFjids())) {
					String[] fjids = qgmxDto.getFjids().get(0).split(",");
					for (String fjid : fjids) {

						boolean saveFile = fjcfbService.save3RealFile(fjid, qgglDto.getQgid(), qgmxDto.getQgmxid());
						if (!saveFile)
							throw new BusinessException("ICOM00002", "请购明细附件保存失败!");
					}
				}
			}

			//清空采购车
			QgcglDto qgcglDto=new QgcglDto();
			qgcglDto.setRyid(qgglDto.getLrry());
			qgcglService.cleanShoppingCar(qgcglDto);
			return true;
		}
		return false;
	}
	
	/**
	 * 根据单据号查询采购单信息(可用于判断采购单号是否重复)
	 */
	public QgglDto getDtoByDjh(QgglDto qgglDto) {
		return dao.getDtoByDjh(qgglDto);
	}
  
	/**
	 * 修改请购单
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateProdution(QgglDto qgglDto,List<QgmxDto> qgmxList) throws BusinessException{
		// TODO Auto-generated method stub
		boolean result = update(qgglDto);
		if(!result) {
			throw new BusinessException("ICOM00002","请购修改失败!");
		}

		//文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(qgglDto.getFjids())){
			for (int j = 0; j < qgglDto.getFjids().size(); j++) {
				if(StringUtil.isNotBlank(qgglDto.getFjids().get(j))) {
					fjcfbService.save2RealFile(qgglDto.getFjids().get(j), qgglDto.getQgid());
					FjcfbDto fjcfbDto=new FjcfbDto();
					fjcfbDto.setFjid(qgglDto.getFjids().get(j));
					FjcfbDto t_fjcfbDto=fjcfbService.getDto(fjcfbDto);
					int begin=t_fjcfbDto.getWjm().lastIndexOf(".");
					String wjmkzm=t_fjcfbDto.getWjm().substring(begin);
					DBEncrypt p = new DBEncrypt();
					if((wjmkzm.equalsIgnoreCase(".doc"))||(wjmkzm.equalsIgnoreCase(".docx"))||(wjmkzm.equalsIgnoreCase(".xls"))||(wjmkzm.equalsIgnoreCase(".xlsx"))) {
						String wjljjm=p.dCode(t_fjcfbDto.getWjlj());
						//连接服务器
						boolean sendFlg=wjglService.sendWordFile(wjljjm);
						if(sendFlg) {
							Map<String,String> t_map=new HashMap<>();
							String fwjm=p.dCode(t_fjcfbDto.getFwjm());
							t_map.put("wordName", fwjm);
							t_map.put("fwjlj",t_fjcfbDto.getFwjlj());
							t_map.put("fjid",t_fjcfbDto.getFjid());
							t_map.put("ywlx",t_fjcfbDto.getYwlx());
							t_map.put("MQDocOkType",DOC_OK);
							//发送Rabbit消息转换pdf
							amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(t_map));
						}
					}
				}
			}
		}

		// 修改请购明细
		qgglDto.setLrry(qgglDto.getXgry());
		qgglDto.setScry(qgglDto.getXgry());
		//由于updatePurchaseDetail 会删除一些数据 所以做个专门保存附件的list
		List<QgmxDto> fjQgmxDtos = new ArrayList<>(qgmxList);
		result = qgmxService.updatePurchaseDetail(qgmxList, qgglDto);
		if(!result) {
			throw new BusinessException("ICOM00002","请购明细修改失败!");
		}

		log.error("请购updateProdution-qgmxDto={}",JSON.toJSON(fjQgmxDtos));
		//保存明细附件
		for (QgmxDto qgmxDto : fjQgmxDtos) {
			if (!CollectionUtils.isEmpty(qgmxDto.getFjids())) {
				String[] fjids = qgmxDto.getFjids().get(0).split(",");
				for (String fjid : fjids) {
					boolean saveFile = fjcfbService.save3RealFile(fjid, qgglDto.getQgid(), qgmxDto.getQgmxid());
					if (!saveFile)
						throw new BusinessException("ICOM00002", "请购明细附件保存失败!");
				}
			}
		}

		return true;
	}
	
	/**
	 * 获取默认申请人信息
	 */
	public QgglDto getMrsqrxxByYhid(QgglDto qgglDto) {
		return dao.getMrsqrxxByYhid(qgglDto);
	}
	
 	/**
	 * 请购列表（查询审核状态）
	 */
	@Override
	public List<QgglDto> getPagedList(QgglDto qgglDto) {
		// TODO Auto-generated method stub
		List<QgglDto> list=dao.getPagedDtoList(qgglDto);
		try{
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONS.getCode(), "zt", "qgid", new String[]{
				StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONS_ADMINISTRATION.getCode(), "zt", "qgid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONS_DEVICE.getCode(), "zt", "qgid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONS_SERVICE.getCode(), "zt", "qgid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONSTWO.getCode(), "zt", "qgid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return list;
	}
	
	/**
	 * 审核列表
	 */
	@Override
	public List<QgglDto> getPagedAuditQggl(QgglDto qgglDto) {
		// TODO Auto-generated method stub
		List<QgglDto> t_sbyzList=dao.getPagedAuditQggl(qgglDto);
		if(CollectionUtils.isEmpty(t_sbyzList)) 
			return t_sbyzList;
		
		List<QgglDto> sqList=dao.getAuditListQggl(t_sbyzList);
		
		commonservice.setSqrxm(sqList);
		
		return sqList;
	}

	@Override
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam)
			throws BusinessException {
		// TODO Auto-generated method stub
		QgglDto qgglDto=(QgglDto) baseModel;
		//文件复制到正式文件夹，插入信息至正式表

		if(!CollectionUtils.isEmpty(qgglDto.getFjids())){
			for (int i = 0; i < qgglDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(qgglDto.getFjids().get(i),qgglDto.getQgid());
				if(!saveFile)
					throw new BusinessException("ICOM99019","请购附件保存失败!");
			}
		}

		Date date=new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		qgglDto.setXgry(operator.getYhid());
		qgglDto.setShsj(df.format(date));
		List<QgmxDto> qgmxlist= JSON.parseArray(qgglDto.getQgmx_json(), QgmxDto.class);
		qgglDto.setFlg("0");
		int count = getCount(qgglDto);
		if (count > 0) {
			throw new BusinessException("msg","请购单号不允许重复!");
		}
		boolean updateQggl = updateQgglxx(qgglDto);
		if(!updateQggl)
			return false;
		if("1".equals(systemconfigflg)) {
			qgglDto.setPrefixFlg(prefixFlg);
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qggl.updateQgglxx",JSONObject.toJSONString(qgglDto));
		}
		if(!CollectionUtils.isEmpty(qgmxlist)) {
			// 修改请购明细
			qgglDto.setScry(operator.getYhid());
			qgglDto.setLrry(operator.getYhid());
			//由于updatePurchaseDetail 会删除一些数据 所以做个专门保存附件的list
			List<QgmxDto> fjQgmxDtos = new ArrayList<>(qgmxlist);
			boolean result = qgmxService.updatePurchaseDetail(qgmxlist, qgglDto);
			if(!result) {
				throw new BusinessException("ICOM99019","请购明细修改失败!");
			}
			if("1".equals(systemconfigflg)) {
				qgglDto.setPrefixFlg(prefixFlg);
				qgglDto.setQgmxlist(qgmxlist);
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qgmx.updatePurchase",JSONObject.toJSONString(qgglDto));
			}

			//保存明细附件
			for (QgmxDto qgmxDto : fjQgmxDtos) {
				if (!CollectionUtils.isEmpty(qgmxDto.getFjids())) {
					String[] fjids = qgmxDto.getFjids().get(0).split(",");
					for (String fjid : fjids) {
						boolean saveFile = fjcfbService.save3RealFile(fjid, qgglDto.getQgid(), qgmxDto.getQgmxid());
						if (!saveFile)
							throw new BusinessException("ICOM99019", "请购明细附件保存失败!");
					}
				}
			}

		}
		return true;
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam)
			throws BusinessException {
		try {
			if(shgcList == null || shgcList.isEmpty()){
				return true;
			}
			for (ShgcDto shgcDto :shgcList){
				QgglDto qgglDto=new QgglDto();
				qgglDto.setQgid(shgcDto.getYwid());
				qgglDto.setXgry(operator.getYhid());
				QgglDto qgglDto_t=getDtoById(qgglDto.getQgid());
				shgcDto.setSqbm(qgglDto_t.getSqbm());
				List<SpgwcyDto> spgwcyDtos = commonservice.siftJgList(shgcDto.getSpgwcyDtos(),qgglDto_t.getSqbm());
				if(AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {//审核退回
					qgglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
					if(!CollectionUtils.isEmpty(spgwcyDtos)) {
						for (SpgwcyDto spgwcyDto : spgwcyDtos) {
							if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
										spgwcyDto.getYhid(),
										xxglService.getMsg("ICOMM_SH00026"), xxglService.getMsg("ICOMM_SH00097", operator.getZsxm(), qgglDto_t.getDjh(), qgglDto_t.getSqbmmc(), qgglDto_t.getSqly(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}
					//若已发起了钉钉审批，则直接撤回
					if(StringUtils.isNotBlank(qgglDto_t.getDdslid())) {
						Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), qgglDto_t.getDdslid(), shgcDto.getShxx().getShyj(), operator.getDdid());
						//若撤回成功将实例ID至为空
						String success=String.valueOf(cancelResult.get("message"));
						@SuppressWarnings("unchecked")
						Map<String,Object> result_map=JSON.parseObject(success,Map.class);
						boolean bo1= (boolean) result_map.get("success");
						if(bo1)
							dao.updateDdslidToNull(qgglDto_t);
					}
				}else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {//审核通过
					// 查询物料子类别统称基础数据
					List<QgmxDto> qgmxDtos = qgmxService.selectByQgid(qgglDto.getQgid());
					if(!CollectionUtils.isEmpty(qgmxDtos)) {
						StringBuilder errorWlmc = new StringBuilder();
						for(int i = 0; i < qgmxDtos.size(); i++) {
							if(i == 0) {
								errorWlmc = new StringBuilder(qgmxDtos.get(i).getWlmc());
							}else {
								errorWlmc.append(",").append(qgmxDtos.get(i).getWlmc());
							}
						}
						throw new BusinessException("ICOM99019","物料:"+ errorWlmc +",未填写子类别统称!");
					}
					qgglDto.setZt(StatusEnum.CHECK_PASS.getCode());
					if(!CollectionUtils.isEmpty(spgwcyDtos)){
						for (SpgwcyDto spgwcyDto : spgwcyDtos) {
							if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(),
										spgwcyDto.getYhid(),
										xxglService.getMsg("ICOMM_SH00006"), xxglService.getMsg("ICOMM_SH00123",
												operator.getZsxm(), shgcDto.getShlbmc(), qgglDto_t.getDjh(), qgglDto_t.getSqrmc(), qgglDto_t.getSqbmmc(),
												DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}
					//判断u8标记，0：不操作
					if(StringUtil.isNotBlank(matridxdsflag) && !"1".equals(systemreceiveflg) && ("DEVICE".equals(qgglDto_t.getQglbdm()) || "MATERIAL".equals(qgglDto_t.getQglbdm()) || StringUtils.isBlank(qgglDto_t.getQglbdm())) && !"1".equals(qgglDto_t.getQglx())) {
						//审核通过后开始添加U8数据
						//根据申请部门获取部门代码
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat t_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date=new Date();
						String a_date=sdf.format(date);
						String b_date=t_sdf.format(date);
						Map<String,Object> map=new HashMap<>();
						String cVerifier="";//部门经理名称
						ShxxDto shxxParam = new ShxxDto();
						shxxParam.setShlb(shgcDto.getShlb());
						shxxParam.setYwid(shgcDto.getYwid());
						List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
						if(!CollectionUtils.isEmpty(shxxList)) {
							for (ShxxDto shxxDto : shxxList) {
								if ("1".equals(shxxDto.getLcxh())) {
									cVerifier = shxxDto.getShrmc();
									break;
								}
							}
						}
						map.put("cAuditTime", b_date);
						map.put("cAuditDate", a_date);
						map.put("cVerifier", cVerifier);
						map.put("cBusType", "普通采购");
						map.put("cCode", qgglDto_t.getDjh());
						map.put("csysbarcode","||pupr|"+qgglDto_t.getDjh());
						map.put("cDefine1",qgglDto_t.getJlbh());
						map.put("cMaker",qgglDto_t.getSqrmc());
						map.put("dDate",qgglDto_t.getSqrq());
						map.put("ID",qgglDto_t.getQgid());
						if(StringUtils.isBlank(qgglDto_t.getSqbmdm()))
							throw new BusinessException("ICOM99019","申请部门部门代码不允许为空！");
						map.put("cDepCode",qgglDto_t.getSqbmdm());
						map.put("iVTid", "8171");
						map.put("cMemo",qgglDto_t.getBz());

						QgmxDto qgmxDto=new QgmxDto();
						qgmxDto.setQgid(qgglDto_t.getQgid());
						qgmxDto.setZt(StatusEnum.CHECK_PASS.getCode());
						List<QgmxDto> qgmxlist=qgmxService.getQgmxList(qgmxDto);
						if (!rdRecordService.determine_Entry(qgglDto_t.getSqrq())){
							throw new BusinessException("ICOM99019","该月份已结账，不允许再录入U8数据，请修改单据日期!");
						}
						//保存数据至U8系统
						@SuppressWarnings("unchecked")
						Map<String,Object> result=rdRecordService.addPurchaseToU8(map,qgmxlist,qgglDto_t);

						//若添加成功，返回U8系统中相关数据关联到本地
						qgglDto.setGlid(result.get("glid").toString());
						boolean updateglid=updateQgglxx(qgglDto);
						if(!updateglid)
							return false;
						if("1".equals(systemconfigflg)) {
							qgglDto.setPrefixFlg(prefixFlg);
							amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qggl.updateQgglxx",JSONObject.toJSONString(qgglDto));
						}

						@SuppressWarnings("unchecked")
						List<QgmxDto> t_list= (List<QgmxDto>) result.get("qgmxlist");
						boolean updatemxglid=qgmxService.updateGlidDtos(t_list);
						if(!updatemxglid)
							throw new BusinessException("ICOM99019","U8系统更新关联ID失败！");
						if("1".equals(systemconfigflg)) {
							amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qgmx.update", JSONObject.toJSONString(t_list));
						}
					}else if (StringUtil.isNotBlank(matridxdsflag) && !"1".equals(systemreceiveflg) && ("OUTSOURCE".equals(qgglDto_t.getQglbdm())) && !"1".equals(qgglDto_t.getQglx())){
						//审核通过后开始添加U8数据
						//根据申请部门获取部门代码
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						SimpleDateFormat t_sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
						Date date=new Date();
						String a_date=sdf.format(date);
						String b_date=t_sdf.format(date);
						Map<String,Object> map=new HashMap<>();
						String cVerifier="";//部门经理名称
						ShxxDto shxxParam = new ShxxDto();
						shxxParam.setShlb(shgcDto.getShlb());
						shxxParam.setYwid(shgcDto.getYwid());
						List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
						if(!CollectionUtils.isEmpty(shxxList)) {
							for (ShxxDto shxxDto : shxxList) {
								if ("1".equals(shxxDto.getLcxh())) {
									cVerifier = shxxDto.getShrmc();
									break;
								}
							}
						}
						map.put("cAuditTime", b_date);
						map.put("cAuditDate", a_date);
						map.put("cVerifier", cVerifier);
						map.put("cBusType", "委外加工");
						map.put("cCode", qgglDto_t.getDjh());
						map.put("csysbarcode","||ompr|"+qgglDto_t.getDjh());
						map.put("cDefine1",qgglDto_t.getJlbh());
						map.put("cMaker",qgglDto_t.getSqrmc());
						map.put("dDate",qgglDto_t.getSqrq());
						if(StringUtils.isBlank(qgglDto_t.getSqbmdm()))
							throw new BusinessException("ICOM99019","申请部门部门代码不允许为空！");
						map.put("cDepCode",qgglDto_t.getSqbmdm());
						map.put("iVTid", "8171");
						map.put("cMemo",qgglDto_t.getBz());

						QgmxDto qgmxDto=new QgmxDto();
						qgmxDto.setQgid(qgglDto_t.getQgid());
						qgmxDto.setZt(StatusEnum.CHECK_PASS.getCode());
						List<QgmxDto> qgmxlist=qgmxService.getQgmxList(qgmxDto);
						if (!rdRecordService.determine_Entry(qgglDto_t.getSqrq())){
							throw new BusinessException("ICOM99019","该月份已结账，不允许再录入U8数据，请修改单据日期!");
						}
						//保存数据至U8系统
						@SuppressWarnings("unchecked")
						Map<String,Object> result=rdRecordService.addPurchaseToU8ForWW(map,qgmxlist,qgglDto_t);

						//若添加成功，返回U8系统中相关数据关联到本地
						qgglDto.setGlid(result.get("glid").toString());
						boolean updateglid=updateQgglxx(qgglDto);
						if(!updateglid) {
							throw new BusinessException("msg","U8关联id更新失败！");
						}
						@SuppressWarnings("unchecked")
						List<QgmxDto> t_list= (List<QgmxDto>) result.get("qgmxlist");
						boolean updatemxglid=qgmxService.updateGlidDtos(t_list);
						if(!updatemxglid)
							throw new BusinessException("ICOM99019","U8系统更新关联ID失败！");
					}
					if(!"ADMINISTRATION".equals(qgglDto_t.getQglbdm())){
						String shys = commonService.getCommonAuditTime(shgcDto.getYwid(),shgcDto.getShxx().getGwid(),"approval.qg");
						qgglDto.setShys(shys);
					}
				} else {//新增审核
					// 查询物料子类别统称基础数据
					List<QgmxDto> qgmxDtos = qgmxService.selectByQgid(qgglDto.getQgid());
					if(!CollectionUtils.isEmpty(qgmxDtos)) {
						StringBuilder errorWlmc = new StringBuilder();
						for(int i = 0; i < qgmxDtos.size(); i++) {
							if(i == 0) {
								errorWlmc = new StringBuilder(qgmxDtos.get(i).getWlmc());
							}else {
								errorWlmc.append(",").append(qgmxDtos.get(i).getWlmc());
							}
						}
						throw new BusinessException("ICOM99019","物料:"+ errorWlmc +",未填写子类别统称!");
					}
					qgmxDtos=qgmxService.getListByQgid(qgglDto.getQgid());//

					qgglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
					if(!CollectionUtils.isEmpty(spgwcyDtos)){
						if(("1".equals(shgcDto.getLclbcskz2()) && !"ADMINISTRATION".equals(qgglDto_t.getQglbdm())) || ("ADMINISTRATION".equals(qgglDto_t.getQglbdm()) && 1>Integer.parseInt(shgcDto.getXlcxh()))) {
							for (SpgwcyDto spgwcyDto : spgwcyDtos) {
								if (StringUtil.isNotBlank(spgwcyDto.getYhm())) {
									//获取下一步审核人用户名
									User user = new User();
									user.setYhid(spgwcyDto.getYhid());
									User user_t = commonService.getUserInfoById(user);
									String sign = URLEncoder.encode(commonservice.getSign(qgglDto_t.getQgid()), StandardCharsets.UTF_8);
									DBEncrypt p = new DBEncrypt();
									String dingtalkurl = p.dCode(jumpdingtalkurl);
									//如果是行政请购审核 则发送钉钉小程序链接
									if ("AUDIT_REQUISITIONS_ADMINISTRATION".equals(shgcDto.getShlb())) {
										String internalbtn = dingtalkurl + "page=/pages/matridxFunction/purchase/purchaseAdministration/purchaseAdministrationAuditPage/purchaseAdministrationAuditPage" + URLEncoder.encode(("?urlPrefix=" + urlPrefix + "&ywid=" + qgglDto_t.getQgid() + "&ywzd=qgid&userid=" + user_t.getDdid() + "&username=" + user_t.getZsxm()), StandardCharsets.UTF_8);
										List<BtnJsonList> btnJsonLists = new ArrayList<>();
										BtnJsonList btnJsonList = new BtnJsonList();
										btnJsonList.setTitle("小程序");
										btnJsonList.setActionUrl(internalbtn);
										btnJsonLists.add(btnJsonList);
										talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00003"),
												StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SH00035"), shgcDto.getShlbmc(), systemname,
														qgglDto_t.getSqbmmc(), qgglDto_t.getSqrmc(), qgglDto_t.getDjh(), qgglDto_t.getSqly(),
														DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
									} else {
										//内网访问
										String internalbtn = applicationurl + urlPrefix + "/ws/auditProcess/auditPhone?shxx_shry=" + spgwcyDto.getYhid() + "&shxx_shjs=" + spgwcyDto.getJsid() + "&ywid=" + qgglDto_t.getQgid() + "&sign=" + sign + "&xlcxh=" + shgcDto.getXlcxh() + "&business_url=/production/purchase/modPurchase&ywzd=qgid&shlbmc=" + shgcDto.getShlbmc();
										//外网访问
										//String external=externalurl+urlPrefix+"/ws/auditProcess/auditPhone?shxx_shry="+spgwcyDtos.get(i).getYhid()+"&shxx_shjs="+spgwcyDtos.get(i).getJsid()+"&ywid="+qgglDto_t.getQgid()+"&sign="+sign+"&xlcxh="+shgcDto.getXlcxh()+"&business_url=/production/purchase/modPurchase&ywzd=qgid&shlbmc="+shgcDto.getShlbmc();
										List<BtnJsonList> btnJsonLists = new ArrayList<>();
										BtnJsonList btnJsonList = new BtnJsonList();
										btnJsonList.setTitle("详细");
										btnJsonList.setActionUrl(internalbtn);
										btnJsonLists.add(btnJsonList);
										talkUtil.sendCardDyxxMessage(shgcDto.getShlb(),spgwcyDto.getYhid(),spgwcyDto.getYhm(), spgwcyDto.getYhid(), xxglService.getMsg("ICOMM_SH00003"),
												StringUtil.replaceMsg(xxglService.getMsg("ICOMM_SH00035"), shgcDto.getShlbmc(), systemname,
														qgglDto_t.getSqbmmc(), qgglDto_t.getSqrmc(), qgglDto_t.getDjh(), qgglDto_t.getSqly(),
														DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
									}
								}
							}
						}

					}
					//判断u8标记，0：不操作
					if(!"1".equals(systemreceiveflg)) {
						if(((StringUtil.isNotBlank(shgcDto.getDdhdlxcskz1())&&(shgcDto.getShxx() == null || "1".equals(shgcDto.getShxx().getSftg())))&&!"ADMINISTRATION".equals(qgglDto_t.getQglbdm()))
								|| ("ADMINISTRATION".equals(qgglDto_t.getQglbdm()) && "1".equals(shgcDto.getXlcxh()))){
							// 根据申请部门查询审核流程
//							List<DdxxglDto> ddxxglDtos = new ArrayList<>();
//							if(!"ADMINISTRATION".equals(qgglDto_t.getQglbdm())){
//								ddxxglDtos = ddxxglService.getProcessByJgid(qgglDto_t.getSqbm(),DdAuditTypeEnum.SP_QG.getCode());
//								if(CollectionUtils.isEmpty(ddxxglDtos))
//									throw new BusinessException("ICOM99019","未找到所属部门审核流程！");
//								if(ddxxglDtos.size() > 1)
//									throw new BusinessException("ICOM99019","找到多条所属部门审核流程！");
//							}
//
//							String spr=null;
//							StringBuilder csr= new StringBuilder();
//							if(!CollectionUtils.isEmpty(ddxxglDtos)){
//								spr = ddxxglDtos.get(0).getSpr();
//								csr = new StringBuilder(StringUtil.isNotBlank(ddxxglDtos.get(0).getCsr())?ddxxglDtos.get(0).getCsr():"");
//							}
							//如果修改了钉钉审批流程则采用修改后的钉钉审批流程
//							if(StringUtil.isNotBlank(shgcDto.getDdspid())) {
//								DdxxglDto ddxxglDto=ddxxglService.getProcessBySpid(shgcDto.getDdspid());
//								if(ddxxglDto!=null) {
//									if(StringUtils.isNotBlank(ddxxglDto.getSpr())) {
//										spr=ddxxglDto.getSpr();
//									}
//									if(StringUtils.isNotBlank(ddxxglDto.getCsr())) {
//										csr = new StringBuilder(ddxxglDto.getCsr());
//									}
//								}
//							}
							//行政请购抄送人员获取
//							if("ADMINISTRATION".equals(qgglDto_t.getQglbdm())){
//								JcsjDto jcsjDto = new JcsjDto();
//								jcsjDto.setJclb("DINGMESSAGETYPE");
//								jcsjDto.setCsdm("PURCHASE_AUDIT_XZ_CC");
//								if(jcsjService.getDtoByCsdmAndJclb(jcsjDto)!=null){
//									log.error("行政请购审核进入获取抄送人信息方法#####################");
//									List<DdxxglDto> xz_ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
//									if(!CollectionUtils.isEmpty(xz_ddxxglDtos)){
//										for(DdxxglDto ddxxglDto : xz_ddxxglDtos){
//											if(StringUtils.isNotBlank(ddxxglDto.getDdid())){
//												csr.append(",").append(String.join(",", ddxxglDto.getDdid()));
//												if(",".equals(csr.substring(0,1))) {
//													csr = new StringBuilder(csr.substring(1));
//												}
//											}
//										}
//									}
//								}
//							}
//							log.error("请购审核进入查看拼接抄送人csr1 = "+csr+"######################");
//							if(StringUtil.isNotBlank(shgcDto.getCsrs())) {
//								String[] split = shgcDto.getCsrs().split(",|，");
//								// 根据抄送人查询钉钉ID
//								List<String> yhids = Arrays.asList(split);
//								List<User> yhxxs = commonservice.getDdidByYhids(yhids);
//								List<String> ddids=new ArrayList<>();
//								if(!CollectionUtils.isEmpty(yhxxs)) {
//									for(User user : yhxxs) {
//										if(StringUtils.isBlank(user.getDdid()))
//											throw new BusinessException("ICOM99019","该用户("+user.getZsxm()+")未获取到钉钉ID！");
//										ddids.add(user.getDdid());
//									}
//								}
//								csr.append(",").append(String.join(",", ddids));
//								if(",".equals(csr.substring(0,1))) {
//									csr = new StringBuilder(csr.substring(1));
//								}
//							}
//							log.error("请购审核进入查看拼接抄送人csr2 = "+csr+"######################");
//							//提交审核至钉钉审批
//							String cc_list = csr.toString(); // 抄送人信息
//							String approvers = spr; // 审核人信息

							QgglDto sqrxx=dao.getMrsqrxxByYhid(qgglDto_t);//获取申请人信息
							if(sqrxx==null)
								throw new BusinessException("ICOM99019","未获取到申请人信息！");
							if(StringUtils.isBlank(sqrxx.getDdid()))
								throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
							String userid=operator.getDdid();//申请人(设置为审核的申请人)
							//备注：由于物料请购是由采购部提交钉钉审批，所以这里查审批人的用户信息
							User user=new User();
							user.setYhid(operator.getYhid());
							user=commonservice.getUserInfoById(user);
							if(user==null)
								throw new BusinessException("ICOM99019","未获取到申请人信息！");
							if(StringUtils.isBlank(user.getJgid()))
								throw new BusinessException("ICOM99019","未获取到申请人部门信息！");
							if(StringUtils.isBlank(userid))
								throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
							String dept=qgglDto_t.getSqbm();//部门ID
							Map<String,String> map=new HashMap<>();
							List<Map<String,String>> mxlist=new ArrayList<>();
							if("ADMINISTRATION".equals(qgglDto_t.getQglbdm())){
								//这里需要由提交人提交钉钉审批，所以要查申请人的用户信息
								user.setYhid(qgglDto_t.getSqr());
								user=commonservice.getUserInfoById(user);
								if(user==null)
									throw new BusinessException("ICOM99019","未获取到申请人信息！");
								if(StringUtils.isBlank(user.getJgid()))
									throw new BusinessException("ICOM99019","未获取到申请人部门信息！");
								if(StringUtils.isBlank(userid))
									throw new BusinessException("ICOM99019","未获取到申请人钉钉ID！");
								dept=user.getJgid();
								userid=user.getDdid();//如果为行政请购，设置为提交人
								//行政请购流程修改，请购审核到部门领导审核发起钉钉审批，审批流程：申请人--部门领导--苏总
								//log.error("行政请购审核进入查看拼接抄送人cc_list = "+cc_list+"######################");
								Map<String,Object> t_map=dealAdministrationPurchase(qgglDto_t,operator.getYhm(),qgmxDtos,null,null,userid,dept);//处理行政请购
								String str=(String) t_map.get("message");
								String status=(String) t_map.get("status");
								if("success".equals(status)) {
									@SuppressWarnings("unchecked")
									Map<String,Object> result_map=JSON.parseObject(str,Map.class);
									if(("0").equals(String.valueOf(result_map.get("errcode")))) {
										//保存至钉钉分布式管理表
										RestTemplate t_restTemplate = new RestTemplate();
										MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
										paramMap.add("ddslid", String.valueOf(result_map.get("process_instance_id")));
										paramMap.add("fwqm", urlPrefix);
										paramMap.add("cljg", "1");
										paramMap.add("fwqmc", systemname);
										paramMap.add("hddz",applicationurl);
										paramMap.add("spywlx",shgcDto.getShlb());
										//根据审批类型获取钉钉审批的业务类型，业务名称
										JcsjDto jcsjDto_dd = new JcsjDto();
										jcsjDto_dd.setCsdm(shgcDto.getAuditType());
										jcsjDto_dd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
										jcsjDto_dd = jcsjService.getDtoByCsdmAndJclb(jcsjDto_dd);
										if(StringUtil.isBlank(jcsjDto_dd.getCsmc()) || StringUtil.isBlank(jcsjDto_dd.getCskz1())) {
											throw new BusinessException("msg","请设置"+shgcDto.getShlbmc()+"的钉钉审批回调类型基础数据！");
										}
										paramMap.add("ywmc", operator.getZsxm()+"提交的"+jcsjDto_dd.getCsmc());
										paramMap.add("ywlx", jcsjDto_dd.getCskz1());
										paramMap.add("wbcxid", operator.getWbcxid());//存入外部程序id
										
										//分布式保留
										DdfbsglDto ddfbsglDto = new DdfbsglDto();
										ddfbsglDto.setProcessinstanceid(String.valueOf(result_map.get("process_instance_id")));
										ddfbsglDto.setFwqm(urlPrefix);
										ddfbsglDto.setCljg("1");
										ddfbsglDto.setFwqmc(systemname);
										ddfbsglDto.setSpywlx(shgcDto.getShlb());
										ddfbsglDto.setHddz(applicationurl);
										ddfbsglDto.setYwlx(jcsjDto_dd.getCskz1());
										ddfbsglDto.setYwmc(operator.getZsxm()+"提交的"+jcsjDto_dd.getCsmc());
										ddfbsglDto.setWbcxid(operator.getWbcxid()); //存入外部程序id
										boolean result_t = ddfbsglService.saveDistributedMsg(ddfbsglDto);
										if(!result_t)
											return false;
										//主站保留一份
										if(StringUtils.isNotBlank(registerurl)){
											boolean result = t_restTemplate.postForObject(registerurl + "/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
											if(!result)
												return false;
										}
										//若钉钉审批提交成功，则关联审批实例ID
										QgglDto qggl_dd=new QgglDto();
										qggl_dd.setDdslid(String.valueOf(result_map.get("process_instance_id")));
										qggl_dd.setXgry(operator.getYhid());
										qggl_dd.setQgid(qgglDto_t.getQgid());
										updateQgglxx(qggl_dd);
									}else {
										throw new BusinessException("ICOM99019","发起钉钉审批失败！错误信息："+t_map.get("message"));
									}
								}else {
									throw new BusinessException("ICOM99019","发起钉钉审批失败！错误信息："+t_map.get("message"));
								}

								if("ADMINISTRATION".equals(qgglDto_t.getQglbdm())) {//行政请购专属处理，手机端需要预览附件
									//文件转换为PDF
									FjcfbDto fjcfbDto=new FjcfbDto();
									fjcfbDto.setYwid(qgglDto_t.getQgid());
									fjcfbDto.setYwlx(BusTypeEnum.IMP_PURCHASE.getCode());
									List<FjcfbDto> fjcfbDtos=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
									if(!CollectionUtils.isEmpty(fjcfbDtos)) {
										for (FjcfbDto dto : fjcfbDtos) {
											//连接服务器
											int begin = dto.getWjm().lastIndexOf(".");
											String wjmkzm = dto.getWjm().substring(begin);
											if ((wjmkzm.equalsIgnoreCase(".doc")) || (wjmkzm.equalsIgnoreCase(".docx")) || (wjmkzm.equalsIgnoreCase(".xls")) || (wjmkzm.equalsIgnoreCase(".xlsx"))) {
												DBEncrypt p = new DBEncrypt();
												String wjljjm = p.dCode(dto.getWjlj());
												// 上传Word文件
												boolean sendFlg = fjcfbService.sendWordFile(wjljjm);
												if (sendFlg) {
													Map<String, String> fj_map = new HashMap<>();
													String fwjmc = p.dCode(dto.getFwjm());
													fj_map.put("wordName", fwjmc);
													fj_map.put("fwjlj", dto.getFwjlj());
													fj_map.put("fjid", dto.getFjid());
													fj_map.put("ywlx", dto.getYwlx());
													fj_map.put("MQDocOkType", DOC_OK);
													// 发送Rabbit消息转换pdf
													amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(), JSONObject.toJSONString(fj_map));
												}
											}
										}
									}
								}
							}
							//审核走到调用完善请购信息的页面，对接钉钉审批
							else if(!"ADMINISTRATION".equals(qgglDto_t.getQglbdm())) {
								/*-- 自定义审核流程修改 start 2020-10-26 lishangyuan --*/
								map.put("所属公司", systemname);
								map.put("请购单号", qgglDto_t.getDjh());
								map.put("请购类型","1".equals(qgglDto_t.getQglx())?"OA请购":"普通请购");
								map.put("申请事由", qgglDto_t.getSqly());
								List<String> bms = new ArrayList<>();
								bms.add(qgglDto_t.getSqbm());
								map.put("申请部门", JSON.toJSONString(bms));
								map.put("请购明细",applicationurl+urlPrefix+"/ws/production/purchase/getPurchaseMessage?qgid="+qgglDto_t.getQgid());
								List<String> xqspslids=dao.selectXqSpSlIds(qgglDto);
								if(!CollectionUtils.isEmpty(xqspslids)){
									map.put("关联审批单",xqspslids.isEmpty() ? "" : JSON.toJSONString(xqspslids));
								}
								Map<String,Object> t_map = new HashMap<>();
								if ("COMMON_AUDIT_REQUISITIONS".equals(shgcDto.getLclbcskz3())){
									t_map = talkUtil.createInstance(operator.getYhm(), shgcDto.getDdhdlxcskz1(), null, null, userid, dept, map,mxlist,null);
								}else if (shgcDto.getShxx()!=null && "1".equals(shgcDto.getShxx().getSftg())){
									map.put("采购类型",qgglDto_t.getJgfwmc());
									map.put("支付方式", qgglDto_t.getZffsmc());
									map.put("付款方", qgglDto_t.getFkfmc());
									t_map = talkUtil.createInstance(operator.getYhm(), shgcDto.getDdhdlxcskz1(), null, null, userid, dept, map,mxlist,null);
								}
								String str=(String) t_map.get("message");
								String status=(String) t_map.get("status");
								if("success".equals(status)) {
									@SuppressWarnings("unchecked")
									Map<String,Object> result_map=JSON.parseObject(str,Map.class);
									if(("0").equals(String.valueOf(result_map.get("errcode")))) {
										//保存至钉钉分布式管理表
										RestTemplate t_restTemplate = new RestTemplate();
										MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
										paramMap.add("ddslid", String.valueOf(result_map.get("process_instance_id")));
										paramMap.add("fwqm", urlPrefix);
										paramMap.add("cljg", "1");
										paramMap.add("fwqmc", systemname);
										paramMap.add("hddz",applicationurl);
										//根据审批类型获取钉钉审批的业务类型，业务名称
										JcsjDto jcsjDto_dd = new JcsjDto();
										jcsjDto_dd.setCsdm(shgcDto.getAuditType());
										if ("COMMON_AUDIT_REQUISITIONS".equals(shgcDto.getLclbcskz3())){
											jcsjDto_dd.setCsdm("COMMON_AUDIT_REQUISITIONS");
											paramMap.add("spywlx","COMMON_AUDIT_REQUISITIONS");
										}else if (shgcDto.getShxx()!=null && "1".equals(shgcDto.getShxx().getSftg())){
											jcsjDto_dd.setCsdm(shgcDto.getShlb());
											paramMap.add("spywlx",shgcDto.getShlb());
										}
										jcsjDto_dd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
										jcsjDto_dd = jcsjService.getDtoByCsdmAndJclb(jcsjDto_dd);
										if(StringUtil.isBlank(jcsjDto_dd.getCsmc()) || StringUtil.isBlank(jcsjDto_dd.getCskz1())) {
											throw new BusinessException("msg","请设置"+shgcDto.getShlbmc()+"的钉钉审批回调类型基础数据！");
										}
										paramMap.add("ywmc", operator.getZsxm()+"提交的"+jcsjDto_dd.getCsmc());
										paramMap.add("ywlx", jcsjDto_dd.getCskz1());
										paramMap.add("wbcxid", operator.getWbcxid());
										//分布式保留
										DdfbsglDto ddfbsglDto = new DdfbsglDto();
										ddfbsglDto.setProcessinstanceid(String.valueOf(result_map.get("process_instance_id")));
										ddfbsglDto.setFwqm(urlPrefix);
										ddfbsglDto.setCljg("1");
										ddfbsglDto.setFwqmc(systemname);

										if ("COMMON_AUDIT_REQUISITIONS".equals(shgcDto.getLclbcskz3())){
											ddfbsglDto.setSpywlx("COMMON_AUDIT_REQUISITIONS");
										}else if (shgcDto.getShxx()!=null && "1".equals(shgcDto.getShxx().getSftg())){
											ddfbsglDto.setSpywlx(shgcDto.getShlb());
										}
										ddfbsglDto.setHddz(applicationurl);
										ddfbsglDto.setYwlx(jcsjDto_dd.getCskz1());
										ddfbsglDto.setYwmc(operator.getZsxm()+"提交的"+jcsjDto_dd.getCsmc());
										ddfbsglDto.setWbcxid(operator.getWbcxid());
										boolean result_t = ddfbsglService.saveDistributedMsg(ddfbsglDto);
										if(!result_t)
											return false;
										//主站保留一份
										if(StringUtils.isNotBlank(registerurl)){
											boolean result = t_restTemplate.postForObject(registerurl + "/ws/purchase/saveDistributedMsg", paramMap, boolean.class);
											if(!result)
												return false;
										}
										//若钉钉审批提交成功，则关联审批实例ID
//										QgglDto qggl_dd=new QgglDto();
//										qggl_dd.setDdslid(String.valueOf(result_map.get("process_instance_id")));
//										qggl_dd.setXgry(operator.getYhid());
//										qggl_dd.setQgid(qgglDto_t.getQgid());
//										updateQgglxx(qggl_dd);
										qgglDto.setDdslid(String.valueOf(result_map.get("process_instance_id")));
									}else {
										throw new BusinessException("ICOM99019","发起钉钉审批失败！错误信息："+t_map.get("message"));
									}
								}else {
									throw new BusinessException("ICOM99019","发起钉钉审批失败！错误信息："+t_map.get("message"));
								}
							}
						}
					}
					//发送钉钉消息--取消审核人员
					if(!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())){
						int size = shgcDto.getNo_spgwcyDtos().size();
						for(int i=0;i<size;i++){
							if(StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())){
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(), shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"),xxglService.getMsg("ICOMM_SH00005",operator.getZsxm(),shgcDto.getShlbmc() ,qgglDto_t.getDjh(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					}
				}
				boolean isSuccess = updateQgglxx(qgglDto);
				if("1".equals(systemconfigflg) && isSuccess) {
					qgglDto.setPrefixFlg(prefixFlg);
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qggl.updateQgglxx",JSONObject.toJSONString(qgglDto));
				}
			}
			return true;
		} catch (BusinessException e) {
			log.error(e.getMsg() + ":" + e.getMessage());
			// TODO: handle exception
			throw new BusinessException("ICOM99019",e.getMsg());
		} catch (Exception e) {
			log.error(e.getMessage());
			// TODO: handle exception
			throw new BusinessException("ICOM99019","审核异常错误！"+ e);
		}

	}

	/**
	 * 处理行政钉钉审批流程
	 */
	public Map<String,Object> dealAdministrationPurchase(QgglDto qgglDto_t, String yhm, List<QgmxDto> qgmxDtos, String approvers, String cc_list, String userid, String dept){
		Map<String,String> map=new HashMap<>();
		List<Map<String,String>> mxlist=new ArrayList<>();
		Map<String,Object> template = talkUtil.getTemplateCode(yhm, "行政用品需求审批");
		String templateCode=(String) template.get("message");
		BigDecimal zjg = new BigDecimal("0");
		if(!CollectionUtils.isEmpty(qgmxDtos)) {
			for (QgmxDto qgmxDto : qgmxDtos) {
				String jg = qgmxDto.getJg();
				String sl = qgmxDto.getSl();
				if (StringUtils.isNotBlank(jg) && StringUtils.isNotBlank(sl)) {
					zjg = zjg.add(new BigDecimal(jg).multiply(new BigDecimal(sl)));
				}
			}
			zjg = zjg.setScale(2, RoundingMode.UP);
			map.put("所属公司", systemname);
			map.put("请购单号", qgglDto_t.getDjh());
			map.put("请购类型", "1".equals(qgglDto_t.getQglx()) ? "OA请购" : "普通请购");
			map.put("申请部门", qgglDto_t.getSqbmmc());
			map.put("申请人", qgglDto_t.getSqrmc());
			map.put("预估总价(元)", zjg.toString());
			map.put("期望交付日期", qgmxDtos.get(0).getQwrq());
			map.put("申请事由", qgglDto_t.getSqly());
			map.put("说明", "详细信息：" + applicationurl + urlPrefix + "/ws/production/purchase/getPurchaseMessage?qgid=" + qgglDto_t.getQgid());
			return talkUtil.createInstance(yhm, templateCode, null, null, userid, dept, map, null, mxlist);
		}
		return null;
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
				String qgid = shgcDto.getYwid();
				QgglDto qgglDto= new QgglDto();
				qgglDto.setXgry(operator.getYhid());
				qgglDto.setQgid(qgid);
				qgglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				boolean isSuccess = updateQgglxx(qgglDto);
				if("1".equals(systemconfigflg) && isSuccess) {
					qgglDto.setPrefixFlg(prefixFlg);
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qggl.updateQgglxx",JSONObject.toJSONString(qgglDto));
				}
				//OA取消审批的同时组织钉钉审批
				QgglDto t_qgglDto=dao.getDto(qgglDto);
				if(t_qgglDto!=null && StringUtils.isNotBlank(t_qgglDto.getDdslid())) {
					if(("3".equals(shgcDto.getXlcxh()) && !"ADMINISTRATION".equals(t_qgglDto.getQglbdm())) || ("1".equals(shgcDto.getXlcxh()) && "ADMINISTRATION".equals(t_qgglDto.getQglbdm()))) {
						Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), t_qgglDto.getDdslid(), "", operator.getDdid());
						//若撤回成功将实例ID至为空
						String success=String.valueOf(cancelResult.get("message"));
						@SuppressWarnings("unchecked")
						Map<String,Object> result_map=JSON.parseObject(success,Map.class);
						boolean bo1= (boolean) result_map.get("success");
						if(bo1)
							dao.updateDdslidToNull(t_qgglDto);
					}
				}
			}
		}else {
			//审核回调方法
			for(ShgcDto shgcDto : shgcList){
				String qgid = shgcDto.getYwid();
				QgglDto qgglDto =new QgglDto();
				qgglDto.setXgry(operator.getYhid());
				qgglDto.setQgid(qgid);
				qgglDto.setZt(StatusEnum.CHECK_NO.getCode());
				boolean isSuccess = updateQgglxx(qgglDto);
				if("1".equals(systemconfigflg) && isSuccess) {
					qgglDto.setPrefixFlg(prefixFlg);
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qggl.updateQgglxx",JSONObject.toJSONString(qgglDto));
				}
				//OA取消审批的同时组织钉钉审批
				QgglDto t_qgglDto=dao.getDto(qgglDto);
				Map<String,Object> cancelResult=talkUtil.cancelDingtalkAudit(operator.getYhm(), t_qgglDto.getDdslid(), "", operator.getDdid());
				//若撤回成功将实例ID至为空
				String success=String.valueOf(cancelResult.get("message"));
				@SuppressWarnings("unchecked")
				Map<String,Object> result_map=JSON.parseObject(success,Map.class);
				boolean bo1= (boolean) result_map.get("success");
				if(bo1)
					dao.updateDdslidToNull(t_qgglDto);
			}
		}
		return true;
	}
	
	public boolean updateQgglxx(QgglDto qgglDto) {
		int result=dao.update(qgglDto);
		return result > 0;
	}
	
	
	/**
	 * 钉钉审批回调
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean callbackQgglAduit(JSONObject obj,HttpServletRequest request,Map<String,Object> t_map) throws BusinessException {
		QgglDto qgglDto=new QgglDto();
		String result=obj.getString("result");//正常结束时result为agree，拒绝时result为refuse，审批终止时没这个值，redirect转交
		String type=obj.getString("type");//审批正常结束（同意或拒绝）的type为finish，审批终止的type为terminate
		String processInstanceId=obj.getString("processInstanceId");//审批实例id
		String staffId=obj.getString("staffId");//审批人钉钉ID
		String remark=obj.getString("remark");//审核意见
		String content = obj.getString("content");//评论
		String finishTime=obj.getString("finishTime");//审批完成时间
		String title= obj.getString("title");
		String processCode=obj.getString("processCode");
		String wbcxidString  = obj.getString("wbcxid"); //获取外部程序id
		log.error("回调参数获取---------result:"+result+",type:"+type+",processInstanceId:"+processInstanceId+",staffId:"+staffId+",remark:"+remark+",finishTime"+finishTime+",外部程序id"+wbcxidString);
		//分布式服务器保存钉钉审批信息
		DdfbsglDto ddfbsglDto=new DdfbsglDto();
		ddfbsglDto.setProcessinstanceid(processInstanceId);
		ddfbsglDto=ddfbsglService.getDtoById(processInstanceId);
		DdspglDto ddspglDto=new DdspglDto();
		DdspglDto t_ddspglDto=new DdspglDto();
		t_ddspglDto.setProcessinstanceid(processInstanceId);
		t_ddspglDto.setType("finish");
		t_ddspglDto.setEventtype(DingTalkUtil.BPMS_TASK_CHANGE);
		List<DdspglDto> ddspgllist=ddspglService.getDtoList(t_ddspglDto); //根据录入时间倒序排序
		try {
//			int a=1/0;
		if(ddfbsglDto==null) 
			throw new BusinessException("message","未获取到相应的钉钉分布式信息！");
		if(StringUtils.isNotBlank(ddfbsglDto.getFwqm())) {
			if(!CollectionUtils.isEmpty(ddspgllist)) {
				ddspglDto=ddspgllist.get(0); //存入最近得一次审批
			}
		}
		qgglDto.setDdslid(processInstanceId);
		//根据钉钉审批实例ID查询关联请购单
		qgglDto=dao.getDtoByDdslid(qgglDto);
			//若没有实例ID，可能不是本地传到钉钉的审批事件，直接返回true
			if(qgglDto!=null) {
				//获取审批人信息
				User user=new User();
				user.setDdid(staffId);
				user.setWbcxid(wbcxidString);
				user = commonService.getByddwbcxid(user);
				//判断查出得信息是否为空
				if(user==null) 
					return false;

				//获取审批人角色信息
				List<QgglDto> dd_sprjs=dao.getSprjsBySprid(user.getYhid());
				// 获取当前审核过程
				ShgcDto t_shgcDto = shgcService.getDtoByYwid(qgglDto.getQgid());
				if(t_shgcDto!=null) {
					// 获取的审核流程列表
					ShlcDto shlcParam = new ShlcDto();
					shlcParam.setShid(t_shgcDto.getShid());
					shlcParam.setGcid(t_shgcDto.getGcid());// 处理旧流程判断用
					@SuppressWarnings("unused")
					List<ShlcDto> shlcList = shlcService.getDtoList(shlcParam);
					String spywlx = ddfbsglDto.getSpywlx();
					List<ShlcDto> collect = shlcList.stream().filter((e) -> spywlx.equals(e.getLclbcskz3())).collect(Collectors.toList());
					if (("start").equals(type)) {
						//更新分布式管理表信息
						DdfbsglDto t_ddfbsglDto=new DdfbsglDto();
						t_ddfbsglDto.setProcessinstanceid(processInstanceId);
						t_ddfbsglDto.setYwlx(processCode);
						t_ddfbsglDto.setYwmc(title);
						ddfbsglService.update(t_ddfbsglDto);
					}
					if(!CollectionUtils.isEmpty(dd_sprjs)) {
						//审批正常结束（同意或拒绝）
						ShxxDto shxxDto=new ShxxDto();
						shxxDto.setGcid(t_shgcDto.getGcid());
						shxxDto.setLcxh(t_shgcDto.getXlcxh());
						shxxDto.setShlb(t_shgcDto.getShlb());
						shxxDto.setShyj(remark);
						shxxDto.setYwid(qgglDto.getQgid());
						if (StringUtil.isNotBlank(finishTime)){
							shxxDto.setShsj(DateUtils.getCustomFomratCurrentDate(new Date(Long.parseLong(finishTime)), "yyyy-MM-dd HH:mm:ss"));
						}
						String lastlcxh=null;//回退到指定流程
						

						if(("finish").equals(type)) {
							//如果审批通过,同意
							if(("agree").equals(result)) {
								log.error("同意------");
								shxxDto.setSftg("1");
								if(StringUtils.isBlank(t_shgcDto.getXlcxh()))
									throw new BusinessException("ICOM99019","现流程序号为空");
							}
							//如果审批未通过，拒绝
							else if(("refuse").equals(result)) {
								log.error("拒绝------");
								shxxDto.setSftg("0");
								lastlcxh = String.valueOf(Integer.parseInt(collect.get(0).getLcxh())-1);
								shxxDto.setThlcxh(lastlcxh);
							}
							//如果审批被转发
							else if(("redirect").equals(result)) {
								String date=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(Long.parseLong(finishTime) / 1000));
								log.error("DingTalkMaterPurchaseAudit(钉钉物料请购审批转发提醒)------转发人员:"+user.getZsxm()+",人员ID:"+user.getYhid()+",单据号:"+qgglDto.getDjh()+",单据ID:"+qgglDto.getQgid()+",转发时间:"+date);
								return true;
							}
							//调用审核方法
							Map<String, List<String>> map=ToolUtil.reformRequest(request);
							log.error("map:"+map);
							List<String> list=new ArrayList<>();
							list.add(qgglDto.getQgid());
							map.put("qgid", list);
							//若一个用户多个角色导致审核异常时
							for(int i=0;i<dd_sprjs.size();i++) {
								try {
									//取下一个角色
									user.setDqjs(dd_sprjs.get(i).getSprjsid());
									user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
//									if(("refuse").equals(result)){
//										shgcService.DingtalkRecallAudit(shxxDto, user,request,lastlcxh,obj);
//									}else{
										shgcService.doManyBackAudit(shxxDto, user,request,lastlcxh,obj);
									//}
									//更新审批管理信息
									ddspglDto.setCljg("1");
									ddspglService.updatecljg(ddspglDto);
									break;
								} catch (Exception e) {
									log.error("callbackQgglAduit-Exception:" + e.getMessage());
									t_map.put("ddspglid", ddspglDto.getDdspglid());
									t_map.put("processInstanceId", ddspglDto.getProcessinstanceid());
									
									if(i==dd_sprjs.size()-1)
										throw new BusinessException("ICOM99019",e.getMessage());
								}
							}
						}
						//撤销审批
						else if(("terminate").equals(type)) {
							//退回到采购部
							shxxDto.setThlcxh(String.valueOf(Integer.parseInt(collect.get(0).getLcxh())-1));
							if ("0".equals(shxxDto.getThlcxh())){
								shxxDto.setThlcxh(null);
							}
							shxxDto.setYwglmc(qgglDto.getDjh());
							QgglDto t_qgglDto=new QgglDto();
							t_qgglDto.setQgid(qgglDto.getQgid());
							dao.updateDdslidToNull(t_qgglDto);
							log.error("撤销------");
							shxxDto.setSftg("0");
							//调用审核方法
							Map<String, List<String>> map=ToolUtil.reformRequest(request);
							List<String> list=new ArrayList<>();
							list.add(qgglDto.getQgid());
							map.put("qgid", list);
							for(int i=0;i<dd_sprjs.size();i++) {
								try {
									//取下一个角色
									user.setDqjs(dd_sprjs.get(i).getSprjsid());
									user.setDqjsmc(dd_sprjs.get(i).getSprjsmc());
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
						shxx.setYwid(qgglDto.getQgid());
						shxx.setShlb(AuditTypeEnum.AUDIT_REQUISITIONS.getCode());
						List<ShxxDto> shxxlist=shxxService.getShxxOrderByPass(shxx);
						if(!CollectionUtils.isEmpty(shxxlist)) {
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
		}
		return true;
	}
	

	/**
	 * 删除请购单 暂时不做u8同步删除
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean deleteQggl(QgglDto qgglDto) throws BusinessException{
		// TODO Auto-generated method stub
		boolean result = delete(qgglDto);
		if(!result)
			throw new BusinessException("ICOM00004","请购信息删除失败!");
		if(!CollectionUtils.isEmpty(qgglDto.getIds())) {
			List<String> ywids=qgglDto.getIds();
			QgmxDto qgmxDto = new QgmxDto();
			qgmxDto.setIds(qgglDto.getIds());
			qgmxDto.setScry(qgglDto.getScry());
			boolean isSuccess = qgmxService.delete(qgmxDto);
			if(!isSuccess)
				throw new BusinessException("ICOM00004","请购明细删除失败!");
			shgcService.deleteByYwids(ywids);//删除审核过程,否则审批延期会有脏数据(虽然审核中不允许删除)
		}
		return true;
	}

	@Override
	public boolean baoxiaoQggl(QgglDto qgglDto) {
		return dao.baoxiaoQggl(qgglDto);
	}

	/**
	 * 高级修改请购信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean advancedModPurchase(QgglDto qgglDto, List<QgmxDto> qgmxList) throws BusinessException {
		// TODO Auto-generated method stub
		// 保存本地请购信息
		boolean result = update(qgglDto);
		if (!result)
			throw new BusinessException("ICOM00002", "请购修改失败!");

		// 文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(qgglDto.getFjids())) {
			for (int i = 0; i < qgglDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(qgglDto.getFjids().get(i), qgglDto.getQgid());
				if (!saveFile)
					throw new BusinessException("ICOM00002", "请购附件保存失败!");
			}
		}

		// 用于新增的List
		List<QgmxDto> insertList = new ArrayList<>();
		// 先保存明细数据，
		for (int i = qgmxList.size() - 1; i > -1; i--) {
			if (StringUtil.isBlank(qgmxList.get(i).getQgmxid())) {
				qgmxList.get(i).setYssl(qgmxList.get(i).getSl());
				qgmxList.get(i).setSysl(qgmxList.get(i).getSl());
				qgmxList.get(i).setLrry(qgglDto.getLrry());
				qgmxList.get(i).setQgid(qgglDto.getQgid());
				insertList.add(qgmxList.get(i));
				qgmxList.remove(i);
			} else {
				qgmxList.get(i).setXgry(qgglDto.getLrry());
				qgmxList.get(i).setQgid(qgglDto.getQgid());
			}
		}
		List<QgmxDto> selectQgmxDtos = qgmxService.getListByQgid(qgglDto.getQgid());
		//获取删除的请购明细
		List<String> delQgmxids = new ArrayList<>();
		List<QgmxDto> delQgmxDtos = new ArrayList<>();
		for (int i = selectQgmxDtos.size() - 1; i >= 0; i--) {
			boolean isDel = true;
			for (QgmxDto qgmxDto : qgmxList) {
				if (selectQgmxDtos.get(i).getQgmxid().equals(qgmxDto.getQgmxid())) {
					if (StringUtil.isNotBlank(selectQgmxDtos.get(i).getSysl()) && StringUtil.isNotBlank(selectQgmxDtos.get(i).getSl())) {
						// 判断能否修改
						Double sysl = Double.parseDouble(selectQgmxDtos.get(i).getSysl()) - (Double.parseDouble(selectQgmxDtos.get(i).getSl()) - Double.parseDouble(qgmxDto.getSl()));
						if (sysl < 0 || (StringUtil.isNotBlank(selectQgmxDtos.get(i).getYdsl()) && sysl < Double.parseDouble(selectQgmxDtos.get(i).getYdsl()))) {
							throw new BusinessException("ICOM00002", "修改数量小于合同已使用的数量!" + selectQgmxDtos.get(i).getWlbm());
						} else {
							qgmxDto.setSysl(String.valueOf(sysl));
						}
						isDel = false;
						break;
					} else {
						qgmxDto.setSysl(qgmxDto.getSl());
						isDel = false;
					}
				}
			}
			if (isDel) {
				// 判断能否删除
				if((StringUtil.isNotBlank(selectQgmxDtos.get(i).getYdsl()) && Double.parseDouble(selectQgmxDtos.get(i).getYdsl()) > 0) 
						|| Double.parseDouble(selectQgmxDtos.get(i).getSl()) > Double.parseDouble(selectQgmxDtos.get(i).getSysl())) {
					throw new BusinessException("ICOM00002", "合同已采用的请购明细不允许删除！"+selectQgmxDtos.get(i).getWlbm());
				}
				delQgmxids.add(selectQgmxDtos.get(i).getQgmxid());
				selectQgmxDtos.get(i).setXgry(qgglDto.getXgry());
				delQgmxDtos.add(selectQgmxDtos.get(i));
				selectQgmxDtos.remove(i);
			}
		}
		if(!CollectionUtils.isEmpty(insertList)) {
			result = qgmxService.insertQgmx(insertList);
			if (!result)
				throw new BusinessException("ICOM00002", "请购明细修改-新增失败!");
		}
		if(!CollectionUtils.isEmpty(qgmxList)) {
			result = qgmxService.updateQgmx(qgmxList);
			if (!result)
				throw new BusinessException("ICOM00002", "请购明细修改-修改失败!");
		}
		if(!CollectionUtils.isEmpty(delQgmxids)) {
			QgmxDto qgmxDto = new QgmxDto();
			qgmxDto.setIds(delQgmxids);
			qgmxDto.setScbj("1");
			qgmxDto.setScry(qgglDto.getScry());
			result = qgmxService.deleteQgmxDtos(qgmxDto);
			if(!result)
				throw new BusinessException("ICOM00002", "请购明细修改-删除失败!");
		}

		// 保存明细附件
		for (QgmxDto qgmxDto : qgmxList) {
			if (!CollectionUtils.isEmpty(qgmxDto.getFjids())) {
				String[] fjids = qgmxDto.getFjids().get(0).split(",");
				for (String fjid : fjids) {
					boolean saveFile = fjcfbService.save3RealFile(fjid, qgglDto.getQgid(), qgmxDto.getQgmxid());
					if (!saveFile)
						throw new BusinessException("ICOM00002", "请购明细附件保存失败!");
				}
			}
		}

		//判断u8标记，0：不操作
		if(StringUtil.isNotBlank(matridxdsflag) && !"1".equals(systemreceiveflg)) {
			if ("MATERIAL".equals(qgglDto.getQglbdm())||"OUTSOURCE".equals(qgglDto.getQglbdm())){
				QgglDto t_qgglDto = getDto(qgglDto);
				// 审核通过是时更新u8
				if("80".equals(t_qgglDto.getZt())) {
					// u8更新。。。 获取关联ID
					QgglDto getQgglglid = dao.getDtoById(qgglDto.getQgid());
					qgglDto.setGlid(getQgglglid.getGlid());
					Map<String,Object> map=rdRecordService.updatePurchaseToU8(qgmxList,qgglDto,insertList,delQgmxDtos);
					if(!CollectionUtils.isEmpty((List<QgmxDto>) map.get("addlist"))){
						boolean updateglid=qgmxService.updateGlidDtos((List<QgmxDto>) map.get("addlist"));
						if(!updateglid)
							throw new BusinessException("ICOM00002","更新U8关联ID失败!");
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * 保存本地请购信息
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateLocalPurchase(QgglDto qgglDto, List<QgmxDto> qgmxList) throws BusinessException{
		boolean result = update(qgglDto);
		if (!result)
			throw new BusinessException("ICOM00002", "请购修改失败!");

		// 文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(qgglDto.getFjids())) {
			for (int i = 0; i < qgglDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(qgglDto.getFjids().get(i), qgglDto.getQgid());
				if (!saveFile)
					throw new BusinessException("ICOM00002", "请购附件保存失败!");
			}
		}

		// 用于新增的List
		List<QgmxDto> insertList = new ArrayList<>();
		// 先保存明细数据，
		for (int i = qgmxList.size() - 1; i > -1; i--) {
			qgmxList.get(i).setYssl(qgmxList.get(i).getSl());
			if (StringUtil.isBlank(qgmxList.get(i).getQgmxid())) {
				qgmxList.get(i).setLrry(qgglDto.getLrry());
				qgmxList.get(i).setQgid(qgglDto.getQgid());
				qgmxList.get(i).setSysl(qgmxList.get(i).getSl());
				qgmxList.get(i).setYssl(qgmxList.get(i).getSl());
				insertList.add(qgmxList.get(i));
				qgmxList.remove(i);
			} else {
				qgmxList.get(i).setXgry(qgglDto.getLrry());
				qgmxList.get(i).setQgid(qgglDto.getQgid());
			}
		}
		boolean result2 = qgmxService.updateQgmx(qgmxList);
		if (!result2)
			throw new BusinessException("ICOM00002", "请购明细修改失败!");
		if (!CollectionUtils.isEmpty(insertList)) {
			boolean isTure = qgmxService.insertQgmx(insertList);
			if (!isTure)
				throw new BusinessException("ICOM00002", "请购明细修改失败!");
		}

		// 保存明细附件
		for (QgmxDto qgmxDto : qgmxList) {
			if (!CollectionUtils.isEmpty(qgmxDto.getFjids())) {
				String[] fjids = qgmxDto.getFjids().get(0).split(",");
				for (String fjid : fjids) {
					boolean saveFile = fjcfbService.save3RealFile(fjid, qgglDto.getQgid(), qgmxDto.getQgmxid());
					if (!saveFile)
						throw new BusinessException("ICOM00002", "请购明细附件保存失败!");
				}
			}
		}

		return true;
	}

	/**
	 * 导出
	 */
	public int getCountForSearchExp(QgglDto qgglDto, Map<String, Object> params) {
		return dao.getCountForSearchExp(qgglDto);
	}

	/**
	 * 根据搜索条件获取导出信息
	 */
	public List<QgglDto> getListForSearchExp(Map<String, Object> params) {
		QgglDto qgglDto = (QgglDto) params.get("entryData");
		queryJoinFlagExport(params, qgglDto);
		return dao.getListForSearchExp(qgglDto);
	}

	/**
	 * 根据选择信息获取导出信息
	 */
	public List<QgglDto> getListForSelectExp(Map<String,Object> params){
		QgglDto qgglDto = (QgglDto)params.get("entryData");
		queryJoinFlagExport(params,qgglDto);
		return dao.getListForSelectExp(qgglDto);
	}
	@SuppressWarnings("unchecked")
	private void queryJoinFlagExport(Map<String,Object> params,QgglDto qgglDto){
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuilder sqlParam = new StringBuilder();
		for(DcszDto dcszDto:choseList){
			if(dcszDto == null || dcszDto.getDczd() == null)
				continue;
		
			sqlParam.append(",");
			if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		String sqlcs=sqlParam.toString();
		qgglDto.setSqlParam(sqlcs);
	}

	/**
	 * 自动生成单据号
	 */
	@Override
	public String generateDjh(QgglDto qgglDto) {
		// TODO Auto-generated method stub
		// 生成规则: XZ-R1-2020-1022-01 请购类别代码-机构代码-年份-日期-流水号 机构代码从机构信息的扩展参数1 里获取；请购类别代码从基础数据中获取。
		String year = DateUtils.getCustomFomratCurrentDate("yyyy");
		String date = DateUtils.getCustomFomratCurrentDate("MMdd");
		String prefix = qgglDto.getJgdh() + "-" + year + "-" + date + "-";
		if (qgglDto.getCskz1()!=null){
			prefix = qgglDto.getCskz1() + prefix;
		}
		// 查询流水号
		String serial = dao.getDjhSerial(prefix);
		return prefix + serial;
	}

	@Override
	public List<QgglDto> getQgList(QgglDto qgglDto) {
		return dao.getQgList(qgglDto);
	}
	
	 //取消请购更新请购信息和取消请购信息
	/*public boolean savePurchaseCancel(QgglDto qgglDto,List<QgmxDto> qgmxList) throws BusinessException{
		// TODO Auto-generated method stub
		// 保存本地请购信息
		boolean updateLocalPurchase = updateLocalPurchase(qgglDto, qgmxList);
		if(!updateLocalPurchase)
			return false;
		//保存取消请购信息
		QgqxglDto qgqxglDto=new QgqxglDto();
		qgqxglDto.setQgid(qgglDto.getQgid());
		qgqxglDto.setDjh(qgglDto.getDjh());
		qgqxglDto.setSqr(qgglDto.getSqr());
		qgqxglDto.setSqbm(qgglDto.getSqbm());
		qgqxglDto.setSqrq(qgglDto.getSqrq());
		qgqxglDto.setLrry(qgglDto.getLrry());
		qgqxglDto.setSqly(qgglDto.getSqly());
		qgqxglDto.setBz(qgglDto.getBz());
		boolean insertQxqg=qgqxglService.insertDto(qgqxglDto);
		if(!insertQxqg)
			return false;
		//判断u8标记，0：不操作
		if(!"1".equals(systemreceiveflg)) {
			// u8更新。。。 获取关联ID
			QgglDto getQgglglid = dao.getDtoById(qgglDto.getQgid());
			qgglDto.setGlid(getQgglglid.getGlid());
			// 更新U8中请购单信息
			Map<String,Object> map=rdRecordService.cancelPurchaseToU8(qgglDto,qgmxList);
			if((List<QgmxDto>) map.get("addlist")!=null && ((List<QgmxDto>) map.get("addlist")).size()>0){
				boolean result=qgmxService.updateGlidDtos((List<QgmxDto>) map.get("addlist"));
				if(!result)
					throw new BusinessException("ICOM00002","更新U8关联ID失败！");
			}
		}
		return true;
	}*/

	/**
	 * 批量更新请购完成标记
	 */
	@Override
	public boolean updateWcbjs(QgglDto qgglDto) {
		// TODO Auto-generated method stub
		int result = dao.updateWcbjs(qgglDto);
		return result != 0;
	}
	
	/**
	 * 根据请购ids获取请购信息
	 */
	public List<QgglDto> getDtoListByIds(QgglDto qgglDto){
		return dao.getDtoListByIds(qgglDto);
	}	
	
	public List<FjcfbDto> getBatchUploadFile(File[] file,String ywlx) {
		List<FjcfbDto> fjbfblist=new ArrayList<>();
		for(File t_fileChildDir : file) {
    		if(t_fileChildDir.isFile()) {
    			FjcfbDto fjcfbDto=new FjcfbDto();
    			String wjm = t_fileChildDir.getName();
    			
    			//根据日期创建文件夹
    			String storePath = prefix + tempFilePath + ywlx +"/" + "UP" +
    					DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" +
    					DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/" + "UP" +
    					DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
    			mkDirs(storePath);
    			String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
				String t_suffix = t_fileChildDir.getName().substring(t_fileChildDir.getName().lastIndexOf("."));
				String saveName = t_name + t_suffix;
				//将上传的文件存放到：路径前缀+指定路径(临时存放目录)
				uploadFile(t_fileChildDir, storePath + "/" + saveName);
    			fjcfbDto.setFjid(StringUtil.generateUUID());
    			fjcfbDto.setWjm(wjm);
    			fjcfbDto.setFwjlj(storePath);
    			fjcfbDto.setFwjm(saveName);
    			fjcfbDto.setWjlj(storePath + "/" + saveName);
    			fjcfbDto.setYwlx(ywlx);
    			fjbfblist.add(fjcfbDto);
    			//将解压后的文件信息存放到redis
    			Map<String,Object> mFile = new HashMap<>();
    			mFile.put("wjm", wjm);
    			mFile.put("fwjlj", fjcfbDto.getFwjlj());
				mFile.put("fwjm", fjcfbDto.getFwjm());
				mFile.put("ywlx", fjcfbDto.getYwlx());
				mFile.put("wjlj", fjcfbDto.getWjlj());
				mFile.put("kzcs1", fjcfbDto.getKzcs1());
				mFile.put("kzcs2", fjcfbDto.getKzcs2());
				mFile.put("kzcs3", fjcfbDto.getKzcs3());
				//失效时间1小时
				redisUtil.hmset("IMP_:_"+fjcfbDto.getFjid(), mFile,3600);
    		}else {
				return getBatchUploadFile(t_fileChildDir.listFiles(),ywlx);
    		}
    	}
		return fjbfblist;
	}

	
	/**
	 * 根据钉钉实例ID获取在钉钉进行请购审批的请购单信息
	 */
	public QgglDto getDtoByDdslid(QgglDto qgglDto) {
		return dao.getDtoByDdslid(qgglDto);
	}
	
	/**
	 * 查询数据生成请购
	 */
	@Override
	public List<Map<String, Object>> getParamForPurchase(QgglDto qgglDto) {
		// TODO Auto-generated method stub
		//删除原有文件
		FjcfbDto fjDto=new FjcfbDto();
		fjDto.setYwid(qgglDto.getQgid());
		fjDto.setYwlx(BusTypeEnum.IMP_PURCHASE_GENERATE.getCode());
		List<FjcfbDto> fjlist=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjDto);
		if(!CollectionUtils.isEmpty(fjlist)) {
			DBEncrypt bpe = new DBEncrypt();
			for(FjcfbDto fjcfbDto : fjlist) {
				if(StringUtils.isNotBlank(fjcfbDto.getWjlj())) {
					String path=bpe.dCode(fjcfbDto.getWjlj());
					File file=new File(path);
					if(file.exists())
						file.delete();
				}
				if(StringUtils.isNotBlank(fjcfbDto.getZhwjxx())) {
					String path=bpe.dCode(fjcfbDto.getZhwjxx());
					File file=new File(path);
					if(file.exists())
						file.delete();
				}
			}
			//删除数据表信息
			fjcfbService.deleteByYwid(fjDto);
		}
		XWPFPurchaseUtil xwpfPurchaseUtil = new XWPFPurchaseUtil();
		Map<String, Object> map = dao.getPurchaseMap(qgglDto);
		String ddbh = map.get("number") != null ? map.get("number").toString() : null;
		if (StringUtil.isNotBlank(ddbh)) {
			int lastIndex = ddbh.lastIndexOf("-");
			if (lastIndex > -1) {
				String tmpStr = ddbh.substring(lastIndex - 9, lastIndex);
				if (StringUtil.isNotBlank(tmpStr)) {
					String year = tmpStr.substring(0, 4);
					if (StringUtil.isNotBlank(year)) {
						map.put("year", year);
					}
					String mon = tmpStr.substring(5, 7);
					if (StringUtil.isNotBlank(mon)) {
						map.put("mon", mon);
					}
					String day = tmpStr.substring(7, 9);
					if (StringUtil.isNotBlank(day)) {
						map.put("day", day);
					}
				}
			}
		}
		List<Map<String, Object>> resultmap_list=new ArrayList<>();
		List<Map<String, Object>> listMap = dao.getPurchaseListMap(qgglDto);//一条明细生成一个文件
		if(!CollectionUtils.isEmpty(listMap)) {
			for(int i=0;i<listMap.size();i++) {
				List<Map<String, Object>> t_listMap=new ArrayList<>();
				t_listMap.add(listMap.get(i));
				map.put("qgmx", t_listMap);
				map.put("releaseFilePath", releaseFilePath); // 正式文件路径
				map.put("tempPath", tempPath); // 临时文件路径
				Map<String, Object> resultMap = xwpfPurchaseUtil.replacePurchase(map, fjcfbService, FTP_URL, DOC_OK, amqpTempl,String.valueOf(i+1));
				resultmap_list.add(resultMap);
			}
		}
		return resultmap_list;
	}
	
	
	/**
	 * 处理压缩文件，将图纸分配到对应的请购明细中
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Map<String,Object> dealBatchFileSec(QgglDto qgglDto){
		Map<String,Object> map=new HashMap<>();
		if(!CollectionUtils.isEmpty(qgglDto.getYsfjids())) {
			List<FjcfbDto> fjcfblist=new ArrayList<>();//用于存放解压后的文件
			List<FjcfbDto> redisList = fjcfbService.getRedisList(qgglDto.getYsfjids());
			log.error("图纸获取redis(1)--t_redisLis={}",JSON.toJSONString(redisList));
			for (FjcfbDto value : redisList) {
				String ywlx = value.getYwlx();
				String fjid = value.getFjid();
				//判断是否为压缩文件
				String wjlj = value.getWjlj();
				int index = (wjlj.lastIndexOf(".") > 0) ? wjlj.lastIndexOf(".") : wjlj.length() - 1;
				String suffix = wjlj.substring(index);
				if (".zip".equals(suffix)) {
					//解压，分配到对应的请购物料明细中
					int t_index = wjlj.lastIndexOf("/");
					String folder = wjlj.substring(0, t_index);
					String unZipFile = ZipUtil.unZipFile(folder, wjlj);
					File dirFile = new File(unZipFile);
					if (dirFile.exists()) {
						File[] files = dirFile.listFiles();
						if (files != null) {
							for (File fileChildDir : files) {
								if (fileChildDir.isFile()) {
									//文件名
									if (fileChildDir.getName().lastIndexOf(".") == -1) {
										redisUtil.hset("IMP_:_" + fjid, "errorMessage", "压缩文件格式缺失：" + fileChildDir.getName(), 3600);
										map.put("status", "fail");
										map.put("message", "压缩文件格式缺失:" + fileChildDir.getName());
										return map;
									}
									FjcfbDto fjcfbDto = new FjcfbDto();
									String wjm = fileChildDir.getName();
									//根据日期创建文件夹
									String storePath = prefix + tempFilePath + ywlx + "/" + "UP" +
											DateUtils.getCustomFomratCurrentDate("yyyy") + "/" + "UP" +
											DateUtils.getCustomFomratCurrentDate("yyyyMM") + "/" + "UP" +
											DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
									mkDirs(storePath);
									String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
									String t_suffix = fileChildDir.getName().substring(fileChildDir.getName().lastIndexOf("."));
									String saveName = t_name + t_suffix;
									//将上传的文件存放到：路径前缀+指定路径(临时存放目录)
									uploadFile(fileChildDir, storePath + "/" + saveName);
									fjcfbDto.setFjid(StringUtil.generateUUID());
									fjcfbDto.setWjm(wjm);
									fjcfbDto.setFwjlj(storePath);
									fjcfbDto.setFwjm(saveName);
									fjcfbDto.setWjlj(storePath + "/" + saveName);
									fjcfbDto.setYwlx(ywlx);
									fjcfblist.add(fjcfbDto);
									//将解压后的文件信息存放到redis
									Map<String, Object> mFile = new HashMap<>();
									mFile.put("wjm", wjm);
									mFile.put("fwjlj", fjcfbDto.getFwjlj());
									mFile.put("fwjm", fjcfbDto.getFwjm());
									mFile.put("ywlx", fjcfbDto.getYwlx());
									mFile.put("wjlj", fjcfbDto.getWjlj());
									mFile.put("kzcs1", fjcfbDto.getKzcs1());
									mFile.put("kzcs2", fjcfbDto.getKzcs2());
									mFile.put("kzcs3", fjcfbDto.getKzcs3());
									//失效时间1小时
									redisUtil.hmset("IMP_:_" + fjcfbDto.getFjid(), mFile, 3600);
								} else {
									File[] file = fileChildDir.listFiles();
									List<FjcfbDto> t_fjcfblist = getBatchUploadFile(file, ywlx);//调用递归方法去获取文件
									fjcfblist.addAll(t_fjcfblist);
								}
							}
						}
					}
				} else {
					map.put("status", "fail");
					map.put("message", "请选择zip格式文件进行上传！");
					return map;
				}
			}
			if(!CollectionUtils.isEmpty(fjcfblist)) {
				StringBuilder noimgstr=new StringBuilder();
				//获取请购明细数据
				List<QgmxDto> qgmxlist= JSON.parseArray(qgglDto.getQgmx_json(), QgmxDto.class);
				if(!CollectionUtils.isEmpty(qgmxlist)) {
					List<String> wlids=new ArrayList<>();
					for (QgmxDto value : qgmxlist) {
						wlids.add(value.getWlid());
					}
					//获取明细物料基本信息
					List<WlglDto> wlglList=wlglService.getWlglDtoListByDtos(wlids);
					List<FjcfbDto> zs_fjcfbDtos = fjcfbService.selectFjcfbDtoByYwid(qgglDto.getQgid());
					log.error("图纸获取--数据库={}",JSON.toJSONString(zs_fjcfbDtos));
					//以物料去对应图纸(通过规格对应)
					for (QgmxDto qgmxDto : qgmxlist) {
						for (WlglDto wlglDto : wlglList) {
							if (qgmxDto.getWlid().equals(wlglDto.getWlid())) {
								qgmxDto.setGg(wlglDto.getGg());
								break;
							}
						}
						//获取明细中已有的附件，对比文件名称，若相同的则进行覆盖，若不同的则进行添加
						List<FjcfbDto> t_redisList = fjcfbService.getRedisList(qgmxDto.getFjids());
						log.error("图纸获取redis(2)--t_redisLis={}",JSON.toJSONString(t_redisList));
						List<FjcfbDto> tzlist = new ArrayList<>();
						for (FjcfbDto dto : fjcfblist) {
							if (dto.getWjm().contains(qgmxDto.getGg())) {
								log.error("图纸比较 规格相同:"+ dto.getWjm());
								if ((t_redisList == null || t_redisList.size() <= 0) && (zs_fjcfbDtos == null || zs_fjcfbDtos.size() <= 0)) {
									//如果该文件未被使用过，直接对应到明细上
									if (!"1".equals(dto.getSybj())) {
										tzlist.add(dto);
										dto.setSybj("1");
									} else {
										FjcfbDto t_fjcfbDto = reSetFjcfbDto(dto);
										tzlist.add(t_fjcfbDto);
									}
								} else {
									boolean sfxwj = true;//是否为新文件
									if(!CollectionUtils.isEmpty(t_redisList)) {
										for (FjcfbDto fjcfbDto : t_redisList) {
											//若文件名相同则替换掉明细的文件,否则则添加
											if (dto.getWjm().equals(fjcfbDto.getWjm())) {
												for (int l = 0; l < qgmxDto.getFjids().size(); l++) {
													qgmxDto.getFjids().removeIf(e -> e.equals(fjcfbDto.getFjid()));
												}
											}
										}
									}
									if(!CollectionUtils.isEmpty(zs_fjcfbDtos)) {
										for (FjcfbDto zs_fjcfbDto : zs_fjcfbDtos) {
											//若文件名相同则替换掉明细的文件,否则则添加
											if (dto.getWjm().equals(zs_fjcfbDto.getWjm())) {
												FjcfbDto fjcfbDto = new FjcfbDto();
												fjcfbDto.setFjid(zs_fjcfbDto.getFjid());
												fjcfbService.delete(fjcfbDto);
												//如果该文件未被使用过，直接对应到明细上
												if (!"1".equals(dto.getSybj())) {
													tzlist.add(dto);
													dto.setSybj("1");
												} else {
													FjcfbDto t_fjcfbDto = reSetFjcfbDto(dto);
													tzlist.add(t_fjcfbDto);
												}
												sfxwj = false;
											}
										}
									}
									if (sfxwj) {
										//如果该文件未被使用过，直接对应到明细上
										if (!"1".equals(dto.getSybj())) {
											tzlist.add(dto);
											dto.setSybj("1");
										} else {
											FjcfbDto t_fjcfbDto = reSetFjcfbDto(dto);
											tzlist.add(t_fjcfbDto);
										}
									}
								}
							}
						}
						qgmxDto.setTzlist(tzlist);
						if ("1".equals(qgmxDto.getCskz1()) && (qgmxDto.getTzlist() == null || qgmxDto.getTzlist().size() <= 0)) {
							noimgstr.append(qgmxDto.getWlbm());
						}
					}
				}
				if(noimgstr.length()>0) {
					map.put("status", "fail");
					map.put("message", "物料编码为"+ noimgstr +"的物料需上传图纸，未能匹配到对应图纸！");
					map.put("qgmxlist", qgmxlist);
				}else {
					map.put("status", "success");
					map.put("message", "图纸对应成功！");
					map.put("qgmxlist", qgmxlist);
				}
				qgglDto.setQgmxlist(qgmxlist);
			}
		}
		return map;
	}

/**
	 * 获取请购单数量(月)
	 */
	public List<Map<String,Object>> getRequisitionMapByMon(QgglDto qgglDto){
		List<Map<String, Object>> requisitionMapByMon = dao.getRequisitionMapByMon(qgglDto);
		List<Map<String, Object>> maps = new ArrayList<>();
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM");
		try {
			Date dateStart = sdf.parse(qgglDto.getSqrqstart());
			Date dateEnd = sdf.parse(qgglDto.getSqrqend());
			Calendar calendarStart = Calendar.getInstance();
			Calendar calendarEnd = Calendar.getInstance();
			calendarStart.setTime(dateStart);
			calendarEnd.setTime(dateEnd);
			//String[] dateStartList = sdf.format(calendarStart.getTime()).split("-");
			//String[] dateEndList = sdf.format(calendarEnd.getTime()).split("-");
			int size = 11;
			//若返回的长度与月份长度不符，找出缺失的月份插入
			if (size!=requisitionMapByMon.size()){
				for (int i = 0; i < size; i++) {
					Map<String, Object> lsmap = new HashMap<>();
					calendarEnd.add(Calendar.MONTH, -1);
					Object num = 0;
					for (Map<String, Object> stringObjectMap : requisitionMapByMon) {
						if (sdf.format(calendarEnd.getTime()).equals(stringObjectMap.get("sqrq"))) {
							num = stringObjectMap.get("count");
							break;
						}
					}
					lsmap.put("rq",sdf.format(calendarEnd.getTime()));
					lsmap.put("count",num);
					maps.add(lsmap);
				}
			}
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		return maps;
	}
	/**
	 * 获取请购单数量(周)
	 */
	public List<Map<String,Object>> getRequisitionMapByWeek(QgglDto qgglDto){
		return dao.getRequisitionMapByWeek(qgglDto);
	}
	
	/**
	 * 获取请购物料数量(月)

	 */
	public List<Map<String,Object>> getPurchaseMaterialMapByMon(QgglDto qgglDto){
		List<Map<String, Object>> purchaseMaterialMapByMon = dao.getPurchaseMaterialMapByMon(qgglDto);
		List<Map<String, Object>> maps = new ArrayList<>();
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM");
		try {
			Date dateStart = sdf.parse(qgglDto.getSqrqstart());
			Date dateEnd = sdf.parse(qgglDto.getSqrqend());
			Calendar calendarStart = Calendar.getInstance();
			Calendar calendarEnd = Calendar.getInstance();
			calendarStart.setTime(dateStart);
			calendarEnd.setTime(dateEnd);
			//String[] dateStartList = sdf.format(calendarStart.getTime()).split("-");
			//String[] dateEndList = sdf.format(calendarEnd.getTime()).split("-");
			int size = 11;
			//若返回的长度与月份长度不符，找出缺失的月份插入
			if (size!=purchaseMaterialMapByMon.size()){
				for (int i = 0; i < size; i++) {
					Map<String, Object> lsmap = new HashMap<>();
					calendarEnd.add(Calendar.MONTH, -1);
					Object num = 0;
					for (Map<String, Object> stringObjectMap : purchaseMaterialMapByMon) {
						if (sdf.format(calendarEnd.getTime()).equals(stringObjectMap.get("sqrq"))) {
							num = stringObjectMap.get("count");
							break;
						}
					}
					lsmap.put("rq",sdf.format(calendarEnd.getTime()));
					lsmap.put("count",num);
					maps.add(lsmap);
				}
			}
		} catch (ParseException e) {
			log.error(e.getMessage());
		}
		return maps;
	}
	/**
	 * 获取请购物料数量(周)
	 */
	public List<Map<String,Object>> getPurchaseMaterialMapByWeek(QgglDto qgglDto){
		return dao.getPurchaseMaterialMapByWeek(qgglDto);
	}
	/**
	 * 根据路径创建文件
	 */
	private void mkDirs(String storePath)
	{
		File file = new File(storePath);
		if (file.isDirectory()) {
			return;
		}
		file.mkdirs();
	}
	
	/**
	 * 把文件转存到相应路径下
	 */
	private void uploadFile(File file, String filePath)
	{
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
				System.out.println(e.getMessage());
			}
		}
	}
	
	/**
	 * //复制一份一样的文件(只要重新设置一个fjid,指向同一个文件)
	 */
	private FjcfbDto reSetFjcfbDto(FjcfbDto fjcfbDto) {
		FjcfbDto t_fjcfbDto=new FjcfbDto();
		t_fjcfbDto.setFjid(StringUtil.generateUUID());
		t_fjcfbDto.setWjm(fjcfbDto.getWjm());
		t_fjcfbDto.setFwjlj(fjcfbDto.getFwjlj());
		t_fjcfbDto.setFwjm(fjcfbDto.getFwjm());
		t_fjcfbDto.setWjlj(fjcfbDto.getWjlj());
		t_fjcfbDto.setYwlx(fjcfbDto.getYwlx());
		//将解压后的文件信息存放到redis
		Map<String,Object> mFile = new HashMap<>();
		mFile.put("wjm", t_fjcfbDto.getWjm());
		mFile.put("fwjlj", t_fjcfbDto.getFwjlj());
		mFile.put("fwjm", t_fjcfbDto.getFwjm());
		mFile.put("ywlx", t_fjcfbDto.getYwlx());
		mFile.put("wjlj", t_fjcfbDto.getWjlj());
		mFile.put("kzcs1", t_fjcfbDto.getKzcs1());
		mFile.put("kzcs2", t_fjcfbDto.getKzcs2());
		mFile.put("kzcs3", t_fjcfbDto.getKzcs3());
		//失效时间1小时
		redisUtil.hmset("IMP_:_"+t_fjcfbDto.getFjid(), mFile,3600);
		return t_fjcfbDto;
	}

	/**
	 *根据请购明细id更新完成标记
	 */
	@Override
	public boolean updateByQgmxid(QgglDto qgglDto) {
		int result = dao.updateByQgmxid(qgglDto);
		return result>0;
	}

	/**
	 * 行政请购列表数据
	 */
	@Override
	public List<QgglDto> getPagedListAdministration(QgglDto qgglDto) {
		// TODO Auto-generated method stub
		List<QgglDto> list=dao.getPagedListAdministration(qgglDto);
		try{
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONS.getCode(), "zt", "qgid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONS_ADMINISTRATION.getCode(), "zt", "qgid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONS_DEVICE.getCode(), "zt", "qgid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			shgcService.addShgcxxByYwid(list, AuditTypeEnum.AUDIT_REQUISITIONS_SERVICE.getCode(), "zt", "qgid", new String[]{
					StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
		} catch (BusinessException e){
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return list;
	}

	/**
	 * 行政请购库存列表数据
	 */
	public List<QgglDto> getPagedListAdminStock(QgglDto qgglDto){
		return dao.getPagedListAdminStock(qgglDto);
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) {
		Map<String, Object> map = new HashMap<>();
		QgglDto qgglDto = dao.getDtoById(shgcDto.getYwid());
		map.put("jgid",qgglDto.getSqbm());
		return map;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) {
		Map<String, Object> map =new HashMap<>();
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>)param.get("ywids");
		QgglDto qgglDto = new QgglDto();
		qgglDto.setScbj("0");
		qgglDto.setIds(ids);
		List<QgglDto> dtoList = dao.getDtoList(qgglDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(QgglDto dto:dtoList){
				list.add(dto.getQgid());
			}
		}
		map.put("list",list);
		return map;
	}

	/**
	 * 更新确认标记
	 */
	public boolean updateQrbj(QgglDto qgglDto){
		return dao.updateQrbj(qgglDto);
	}
	/**
	 * 单条物料点击查看
	 */
	@Override
	public QgglDto queryByQgid(QgglDto qgglDto) {
		return dao.queryByQgid(qgglDto);
	}

	@Override
	public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
		// TODO Auto-generated method stub
		if(tranTrack.equalsIgnoreCase("QG001")){
			WlglDto wlglDto=new WlglDto();
			wlglDto.setWlbm(value);
			WlglDto t_wlglDto=wlglService.getDto(wlglDto);
			if(t_wlglDto==null) {
				errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
				.append("列，找不到对应的物料，单元格值为：").append(value).append("；<br>");
				return null;
			}else if(!t_wlglDto.getZt().equals(StatusEnum.CHECK_PASS.getCode())) {
					errorMessage.append("第").append(recModel.getRowNum()+3).append("行的第").append(recModel.getColumnNum()+1)
					.append("列，物料未通过审核，不允许请购，单元格值为：").append(value).append("；<br>");
			}
			return t_wlglDto.getWlbm();
		}
		return null;
	}

	@Override
	public boolean existCheck(String fieldName, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages) {
		// TODO Auto-generated method stub
		try{
			// TODO Auto-generated method stub
			QgmxDto qgmxDto = (QgmxDto)baseModel;
			if(StringUtils.isNotBlank(qgmxDto.getQgid())) {
				Date date=new Date();
				SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
				String str_date=formatter.format(date);
				//获取请购类别
				JcsjDto jcsjDto=new JcsjDto();
				jcsjDto.setCsdm(qgmxDto.getQglbdm());
				jcsjDto.setJclb(BasicDataTypeEnum.PURCHASE_SUB_TYPE.getCode());
				jcsjDto=jcsjService.getDto(jcsjDto);
				QgglDto qgglDto=new QgglDto();
				qgglDto.setQgid(qgmxDto.getQgid());
				qgglDto.setSqr(qgmxDto.getLrry());
				qgglDto.setLrry(qgmxDto.getLrry());
				qgglDto.setSqrq(str_date);
				qgglDto.setQglb(jcsjDto.getCsid());
				qgglDto.setQglx("0");
				qgglDto.setZt(StatusEnum.CHECK_NO.getCode());
				QgglDto mrqgglDto=getMrsqrxxByYhid(qgglDto);//获取默认申请人信息
				JcsjDto qglb = jcsjService.getDtoById(qgglDto.getQglb());
				mrqgglDto.setCskz1(qglb.getCskz1());
				if(StringUtil.isNotBlank(mrqgglDto.getJgdh())) {
					String djh = generateDjh(mrqgglDto);
					qgglDto.setDjh(djh);
					qgglDto.setJlbh(djh);
				}
				QgglDto djhsfcf=getDtoByDjh(qgglDto);
				if(djhsfcf!=null) {
					throw new BusinessException("msg","请购单号重复，请尝试重新导入！");
				}
				qgglDto.setSqr(mrqgglDto.getSqr());
				qgglDto.setSqbm(mrqgglDto.getSqbm());
				QgglDto t_qggl=getDto(qgglDto);
				if(t_qggl==null)
					insert(qgglDto);
			}
			if("ADMINISTRATION".equals(qgmxDto.getQglbdm())) {
				qgmxDto.setQgmxid(StringUtil.generateUUID());
				qgmxDto.setSysl(qgmxDto.getSl());
				qgmxDto.setYssl(qgmxDto.getSl());
				qgmxDto.setZt(StatusEnum.CHECK_PASS.getCode());
			}else{
				WlglDto wlglDto=new WlglDto();
				wlglDto.setWlbm(qgmxDto.getWlbm());
				WlglDto t_wlglDto=wlglService.getDto(wlglDto);
				String qgmxid = StringUtil.generateUUID();
				qgmxDto.setWlid(t_wlglDto.getWlid());
				qgmxDto.setQgmxid(qgmxid);
				qgmxDto.setSysl(qgmxDto.getSl());
				qgmxDto.setYssl(qgmxDto.getSl());
				qgmxDto.setZt(StatusEnum.CHECK_PASS.getCode());
			}

			qgmxService.insert(qgmxDto);
		}catch(Exception e){
			log.error(e.toString());
		}
		return true;
	}

	@Override
	public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkDefined(List<Map<String, String>> defined) {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 导出
	 */
	@Override
	public int getCountForAuditingSearchExp(QgglDto qgglDto, Map<String, Object> params) {
		return dao.getCountForAuditingSearchExp(qgglDto);
	}
	/**
	 * 根据搜索条件获取审核导出信息
	 */
	public List<QgglDto> getListForAuditingSearchExp(Map<String, Object> params) {
		QgglDto qgglDto = (QgglDto) params.get("entryData");
		queryJoinFlagExport(params, qgglDto);
		return dao.getListForAuditingSearchExp(qgglDto);
	}

	/**
	 * 根据选择信息获取审核导出信息
	 */
	public List<QgglDto> getListForAuditingSelectExp(Map<String, Object> params) {
		QgglDto qgglDto = (QgglDto) params.get("entryData");
		queryJoinFlagExport(params, qgglDto);
		return dao.getListForAuditingSelectExp(qgglDto);
	}
	/**
	 * 导出
	 */
	@Override
	public int getCountForCgAuditingSearchExp(QgglDto qgglDto, Map<String, Object> params) {
		return dao.getCountForCgAuditingSearchExp(qgglDto);
	}
	/**
	 * 根据搜索条件获取审核导出信息
	 */
	public List<QgglDto> getListForCgAuditingSearchExp(Map<String, Object> params) {
		QgglDto qgglDto = (QgglDto) params.get("entryData");
		queryJoinFlagExport(params, qgglDto);
		return dao.getListForCgAuditingSearchExp(qgglDto);
	}

	/**
	 * 根据选择信息获取审核导出信息
	 */
	public List<QgglDto> getListForCgAuditingSelectExp(Map<String, Object> params) {
		QgglDto qgglDto = (QgglDto) params.get("entryData");
		queryJoinFlagExport(params, qgglDto);
		return dao.getListForCgAuditingSelectExp(qgglDto);
	}
	/**
	 * 请购单统计
	 */
	public List<QgglDto> getCountStatistics(String year){
		return dao.getCountStatistics(year);
	}

	@Override
	public List<QgglDto> getTodeyCount(QgglDto qgglDto) {
		return dao.getTodeyCount(qgglDto);
	}

	@Override
	public List<QgglDto> getTodayList(QgglDto qgglDto) {
		return dao.getTodayList(qgglDto);
	}

	@Override
	public List<QgglDto> getPurchasesByRkid(String rkid) {
		return dao.getPurchasesByRkid(rkid);
	}

	@Override
	public boolean updateRkztList(List<QgglDto> list) {
		return dao.updateRkztList(list);
	}

	@Override
	public List<QgglDto> getPurchaseByQgmxids(List<QgmxDto> qgmxDtos) {
		return dao.getPurchaseByQgmxids(qgmxDtos);
	}
	@Override
	public List<QgglDto> getRkslByQgmxids(List<String> ids) {
		return dao.getRkslByQgmxids(ids);
	}
	@Override
	public List<QgglDto> getXzRkslByQgmxids(List<String> ids) {
		return dao.getXzRkslByQgmxids(ids);
	}

	@Override
	public Map<String, Object> queryOverTime(String sftx) {
		Map<String, Object> resultMap = new HashMap<>();
		XtszDto xtszDto = xtszService.selectById("approval.qg");
		if (xtszDto != null) {
			if (StringUtil.isNotBlank(xtszDto.getSzz())) {
				Map<String, Object> map = JSON.parseObject(xtszDto.getSzz(), new TypeReference<Map<String, Object>>() {
				});
				if(map.get("postCg")!=null && map.get("postStart")!=null && map.get("postEnd")!=null && map.get("overTime")!=null && map.get("overdueTime")!=null && map.get("rule")!=null && map.get("unit")!=null){
					String overdueTime = map.get("overdueTime").toString();
					String overTime = map.get("overTime").toString();
					String postStart = map.get("postStart").toString();
					String postEnd = map.get("postEnd").toString();
					String unit = map.get("unit").toString();
					String rule = map.get("rule").toString();
					String postCg = map.get("postCg").toString();
					resultMap.put("postStart",postStart);
					resultMap.put("postEnd",postEnd);
					resultMap.put("overTime",overTime);
					resultMap.put("overdueTime",overdueTime);
					BigDecimal overdueTimeB = new BigDecimal(overdueTime);
					BigDecimal overTimeB = new BigDecimal(overTime);
					QgglDto qgglDto = new QgglDto();
					qgglDto.setPostEnd(postEnd);
					qgglDto.setPostStart(postStart);
					qgglDto.setPostCg(postCg);
					if(StringUtil.isNotBlank(sftx)){
						qgglDto.setSftx(sftx);
					}
					List<QgglDto> qgglDtoList = dao.queryOverTime(qgglDto);
					List<QgglDto> overdueList = new ArrayList<>();
					List<QgglDto> overList = new ArrayList<>();
					if(!CollectionUtils.isEmpty(qgglDtoList)){
						if("0".equals(rule)){
							if("day".equals(unit)){
								overdueTimeB = new BigDecimal(overdueTime).multiply(new BigDecimal("12"));
								overTimeB = new BigDecimal(overTime).multiply(new BigDecimal("12"));
							}
							for (QgglDto qggl:qgglDtoList){
								qggl.setOverTime(overTimeB.toString());
								qggl.setOverdueTime(overdueTimeB.toString());
								if(new BigDecimal(qggl.getTimeLag()).compareTo(overTimeB)>0){
									overList.add(qggl);
								}else if(new BigDecimal(qggl.getTimeLag()).compareTo(overdueTimeB)>0){
									overdueList.add(qggl);
								}
							}
						}else{
							BigDecimal dayHouts = new BigDecimal("8");
							XtszDto attendanceTime = xtszService.selectById("attendance.time");
							if(attendanceTime!=null){
								if(StringUtil.isNotBlank(attendanceTime.getSzz())){
									Map<String, Object> attendanceTimeMap = JSON.parseObject(attendanceTime.getSzz(), new TypeReference<Map<String, Object>>(){});
									if(attendanceTimeMap.get("officeHours")!=null
											&& attendanceTimeMap.get("closingTime")!=null && attendanceTimeMap.get("beginTime")!=null  && attendanceTimeMap.get("endTime")!=null){
										String officeHours = attendanceTimeMap.get("officeHours").toString();
										String closingTime = attendanceTimeMap.get("closingTime").toString();
										String beginTime = attendanceTimeMap.get("beginTime").toString();
										String endTime = attendanceTimeMap.get("endTime").toString();
										dayHouts = commonService.getHours(officeHours,closingTime).subtract(commonService.getHours(beginTime,endTime));
										if("day".equals(unit)){
											overdueTimeB = new BigDecimal(overdueTime).multiply(dayHouts);
											overTimeB = new BigDecimal(overTime).multiply(dayHouts);
										}
										SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
										SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
										DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
										DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
										Calendar calendar = Calendar.getInstance();
										Date currentDate = calendar.getTime();
										String endString = sdf.format(currentDate);
										String endFormatter = sdf2.format(currentDate);
										Timestamp endStamp = new Timestamp(System.currentTimeMillis());
										for (QgglDto qggl:qgglDtoList){
											BigDecimal timeString = new BigDecimal(qggl.getTimeLag());
											Timestamp shsjStamp = Timestamp.valueOf(qggl.getShsj());
											BigDecimal workDayHours = new BigDecimal("0");
											String startString = sdf.format(shsjStamp);
											//判断今天和开始岗位的审核时间是否为节假日星期天
											XxdyDto xxdyDto = new XxdyDto();
											xxdyDto.setDylxcsdm("Holidays");
											xxdyDto.setBeginTime(startString);
											xxdyDto.setEndTime(endString);
											List<XxdyDto> xxdyDtoList = xxdyService.queryHolidays(xxdyDto);
											List<LocalDate> localDates = commonService.getAllDates(startString,endString);
											LocalDateTime dateTime = LocalDateTime.parse(qggl.getShsj(), formatter);
											String outputString = dateTime.format(outputFormatter);
											LocalDateTime endDateTime = LocalDateTime.parse(endFormatter, formatter);
											String endOutputString = endDateTime.format(outputFormatter);
											LocalDate localDate = dateTime.toLocalDate();
											LocalDate endLocalDate = endDateTime.toLocalDate();
											if (localDate.isEqual(endLocalDate)) {
												if(!org.apache.commons.collections.CollectionUtils.isEmpty(xxdyDtoList)){
													timeString = new BigDecimal("0");
												}else {
													timeString = commonservice.oneDayGetHours(outputString,endOutputString,officeHours,closingTime,beginTime,endTime);
												}
											}else{
												if(!CollectionUtils.isEmpty(xxdyDtoList)){
													int i = 0;
													BigDecimal sumTime = new BigDecimal("0");
													boolean startFlg = false;
													boolean endFlg = false;
													for (XxdyDto xxdy:xxdyDtoList){
														if("1".equals(xxdy.getFlag())){
															if(xxdy.getDyxx().equals(startString)){
																sumTime = sumTime.add(new BigDecimal("24").subtract(commonService.calculateTime(shsjStamp)));
																startFlg = true;
															}
															if(xxdy.getDyxx().equals(endString)){
																sumTime = sumTime.add(commonService.calculateTime(endStamp));
																endFlg = true;
															}
														}else{
															i = i+1;
														}
													}
													timeString = timeString.subtract(sumTime).subtract(new BigDecimal(i).multiply(new BigDecimal("24")));
													int l = localDates.size()-xxdyDtoList.size();
													if(!startFlg){
														workDayHours = workDayHours.add(commonService.beginDayGetHours(outputString,officeHours,closingTime,beginTime,endTime));
														l = l - 1;
														timeString = timeString.subtract(new BigDecimal("24").subtract(commonService.calculateTime(shsjStamp)));
													}
													if(!endFlg){
														workDayHours = workDayHours.add(commonService.endDayGetHours(endOutputString,officeHours,closingTime,beginTime,endTime));
														l = l - 1;
														timeString = timeString.subtract(commonService.calculateTime(endStamp));
													}
													String dayString = String.valueOf(l);
													timeString = timeString.add(workDayHours).subtract(dayHouts.multiply(new BigDecimal(dayString)));

												}else{
													String dayInt = String.valueOf(localDates.size()-2);
													workDayHours = workDayHours.add(commonService.beginDayGetHours(outputString,officeHours,closingTime,beginTime,endTime));
													workDayHours = workDayHours.add(commonService.endDayGetHours(endOutputString,officeHours,closingTime,beginTime,endTime));
													timeString = timeString.subtract(new BigDecimal(dayInt).multiply(dayHouts))
															.subtract(new BigDecimal("24").subtract(commonService.calculateTime(shsjStamp)))
															.subtract(commonService.calculateTime(endStamp))
															.add(workDayHours);
												}
											}
											qggl.setTimeLag(timeString.toString());
											qggl.setOverTime(overTimeB.toString());
											qggl.setOverdueTime(overdueTimeB.toString());
											if(timeString.compareTo(overTimeB)>0){
												overList.add(qggl);
											}else if(timeString.compareTo(overdueTimeB)>0){
												overdueList.add(qggl);
											}
										}
									}
								}
							}
						}
					}
					if(!CollectionUtils.isEmpty(overdueList)){
						resultMap.put("overdueList",overdueList);
					}
					if(!CollectionUtils.isEmpty(overList)){
						resultMap.put("overList",overList);
					}
				}
			}
		}
		return resultMap;
	}

	@Override
	public List<QgglDto> queryOverTimeTable(QgglDto qgglDto) {
		List<QgglDto> qgglDtoList = new ArrayList<>();
		if(StringUtil.isNotBlank(qgglDto.getFlg())){
			Map<String,Object> map = queryOverTime("");
			if(!CollectionUtils.isEmpty(map)){
				List<QgglDto> overList = (List<QgglDto>) map.get("overList");
				List<QgglDto> overdueList = (List<QgglDto>) map.get("overdueList");
				if(!CollectionUtils.isEmpty(overList) && "cs".equals(qgglDto.getFlg())){
					qgglDtoList = overList;
				}
				if(!CollectionUtils.isEmpty(overdueList) && "jj".equals(qgglDto.getFlg())){
					qgglDtoList = overdueList;
				}
			}
		}
		if(!CollectionUtils.isEmpty(qgglDtoList)){
			try{
				shgcService.addShgcxxByYwid(qgglDtoList, AuditTypeEnum.AUDIT_REQUISITIONS.getCode(), "zt", "qgid", new String[]{
						StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
				shgcService.addShgcxxByYwid(qgglDtoList, AuditTypeEnum.AUDIT_REQUISITIONS_DEVICE.getCode(), "zt", "qgid", new String[]{
						StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
				shgcService.addShgcxxByYwid(qgglDtoList, AuditTypeEnum.AUDIT_REQUISITIONS_SERVICE.getCode(), "zt", "qgid", new String[]{
						StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
				shgcService.addShgcxxByYwid(qgglDtoList, AuditTypeEnum.AUDIT_REQUISITIONSTWO.getCode(), "zt", "qgid", new String[]{
						StatusEnum.CHECK_SUBMIT.getCode(),StatusEnum.CHECK_UNPASS.getCode()});
			} catch (BusinessException e){
				log.error(e.getMessage());
			}
		}
		Collections.sort(qgglDtoList, (p1, p2) -> Double.compare(Double.valueOf(p2.getTimeLag()), Double.valueOf(p1.getTimeLag())));
		return qgglDtoList;
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateSftxList(List<QgglDto> list) {
		return dao.updateSftxList(list);
	}

	@Override
	public List<QgglDto> getQgOverTimeTable(QgglDto qgglDto) {
		XtszDto xtszDto = xtszService.selectById("approval.qg");
		List<QgglDto> list = new ArrayList<>();
		if (xtszDto != null) {
			if (StringUtil.isNotBlank(xtszDto.getSzz())) {
				Map<String, Object> map = JSON.parseObject(xtszDto.getSzz(), new TypeReference<Map<String, Object>>() {
				});
				if (map.get("postCg") != null && map.get("overTime") != null && map.get("rule") != null && map.get("unit") != null) {
					qgglDto.setOverTime(map.get("overTime").toString());
					qgglDto.setUnit(map.get("unit").toString());
					qgglDto.setRule(map.get("rule").toString());
					qgglDto.setPostCg(map.get("postCg").toString());
					list =dao.getPagedOverTimeList(qgglDto);
				}
			}
		}
		return list;
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateDispose(QgglDto qgglDto) {
		if(StringUtil.isBlank(qgglDto.getCszt())){
			qgglDto.setCszt("1");
		}
		if(StringUtil.isBlank(qgglDto.getCsbz())){
			qgglDto.setCsbz("");
		}
		return dao.updateDispose(qgglDto)>0;
	}

	@Override
	public Map<String, Object> getOverTimeTable(String rqstart, String rqend) {
		Map<String,Object> resultMap = new HashMap<>();
		QgglDto qgglDto = new QgglDto();
		qgglDto.setSqrqstart(rqstart);
		qgglDto.setSqrqend(rqend);
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(rqend, inputFormatter);
		String year = String.valueOf(date.getYear());
		resultMap.put("sqrqYear",year);
		resultMap.put("sqrqTime",rqstart+"~"+rqend);
		String pjsxString = "";
		XtszDto xtszDto = xtszService.selectById("approval.qg");
		List<QgglDto> list = new ArrayList<>();
		if (xtszDto != null) {
			if (StringUtil.isNotBlank(xtszDto.getSzz())) {
				Map<String, Object> map = JSON.parseObject(xtszDto.getSzz(), new TypeReference<Map<String, Object>>() {
				});
				if (map.get("overTime") != null && map.get("rule") != null && map.get("unit") != null) {
					qgglDto.setOverTime(map.get("overTime").toString());
					qgglDto.setUnit(map.get("unit").toString());
					qgglDto.setRule(map.get("rule").toString());
					List<Map<String, Object>> tableMap =dao.getOverTimeTable(qgglDto);
					resultMap.put("Table",tableMap);
					String timeliness = dao.getTimeliness(qgglDto);
					if(StringUtil.isNotBlank(timeliness)){
						String unitString = "";
						String ruleString = "";
						if("hour".equals(map.get("unit").toString())){
							unitString = "小时";
						}
						if("day".equals(map.get("unit").toString())){
							unitString = "天";
						}
						if("0".equals(map.get("rule").toString())){
							ruleString = "不去除节假日";
						}
						if("1".equals(map.get("rule").toString())){
							ruleString = "去除节假日";
						}
						pjsxString = "平均时效性("+ruleString+"):"+timeliness+unitString;
					}
					qgglDto.setSqrq(year);
					List<QgglDto> qgglDtoList = dao.getMonthTj(qgglDto);
					resultMap.put("month",qgglDtoList);
					List<QgglDto> timeList = dao.getTimeTj(qgglDto);
					resultMap.put("time",timeList);
					List<QgglDto> percentageList = dao.getPercentageTj(qgglDto);
					resultMap.put("percentage",percentageList);
				}
			}
		}
		resultMap.put("timeliness",pjsxString);
		return resultMap;
	}
}
