package com.matridx.igams.detection.molecule.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.common.util.XWPFUtil;
import com.matridx.igams.detection.molecule.dao.entities.FzbbztDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxmModel;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.igams.detection.molecule.dao.post.IFzbbztDao;
import com.matridx.igams.detection.molecule.dao.post.IFzjcxmPJDao;
import com.matridx.igams.detection.molecule.enums.DetMQTypeEnum;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcjgPJService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxmPJService;
import com.matridx.igams.detection.molecule.service.svcinterface.IFzjcxxPJService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class FzjcxmPJServiceImpl extends BaseBasicServiceImpl<FzjcxmDto, FzjcxmModel, IFzjcxmPJDao> implements IFzjcxmPJService, IAuditService {
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IFzjcjgPJService fzjcjgPJService;
	@Autowired
	IShxxService shxxService;
	@Value("${matridx.fileupload.releasePath}")
	private String releaseFilePath;
	@Value("${matridx.fileupload.tempPath}")
	private String tempPath;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	@Value("${matridx.ftp.url:}")
	private String FTP_URL = null;
	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private String DOC_OK = null;
	@Autowired
	IXxglService xxglService;
	@Autowired
	ICommonService commonservice;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IFzbbztDao fzbbztDao;
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
	@Lazy
	@Autowired
	IFzjcxxPJService fzjcxxPJService;
	@Autowired
	IShgcService shgcService;
	@Autowired
	ICommonService commonService;
	/**
	 * 根据条件下载报告压缩包
	 */
	private final Logger log = LoggerFactory.getLogger(FzjcxmPJServiceImpl.class);
    /**
     * 根据分子检测id查询有关的检测项目信息
     */
    public List<FzjcxmDto> getDtoListByFzjcid(String fzjcid){
        return dao.getDtoListByFzjcid(fzjcid);
    }

	/**
	 * 根据分子检测id查询有关的检测项目信息
	 */
	public List<FzjcxmDto> getXmGroupList(FzjcxmDto fzjcxmDto){
		List<FzjcxmDto> xmGroupList = dao.getXmGroupList(fzjcxmDto);
		try {
			shgcService.addShgcxxByYwid(xmGroupList, AuditTypeEnum.AUDIT_GENERAL_INSPECTION.getCode(), "zt", "fzxmid",
					new String[] { StatusEnum.CHECK_SUBMIT.getCode(), StatusEnum.CHECK_UNPASS.getCode() });
		} catch (BusinessException e) {
			log.error(e.getMessage());
		}
		return xmGroupList;
	}

	/**
	 * 根据分子检测id查询分子检测基本信息
	 */
	public List<Map<String,Object>> getFzjcxxInfoForGenerateReport(FzjcxmDto fzjcxmDto) {
		return dao.getFzjcxxInfoForGenerateReport(fzjcxmDto);
	}

	@Override
	public int insertListJcxmAndjczxm(List<FzjcxmDto> list) {
		return dao.insertListJcxmAndjczxm(list);
	}

	/**
	 * 跟进分子检测ID获取检测项目信息
	 * @param fzjcxxDto
	 * @return
	 */
	public List<FzjcxmDto> getJcxmListAndBgByFzjcid(FzjcxxDto fzjcxxDto){
		List<FzjcxmDto> fzjcxmDtos = dao.getJcxmListByFzjcid(fzjcxxDto.getFzjcid());
		if (!CollectionUtils.isEmpty(fzjcxmDtos)){
			List<String> ywids = fzjcxmDtos.stream().map(FzjcxmDto::getFzxmid).collect(Collectors.toList());
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwids(ywids);
			if ("TYPE_FLU".equals(fzjcxxDto.getJclxdm())){
				fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_FLU_WORD.getCode());
			}else if("TYPE_JHPCR".equals(fzjcxxDto.getJclxdm())){
				fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_JHPCR_WORD.getCode());
			}else {
				fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_HPV_WORD.getCode());
			}
			List<FjcfbDto> allWordList = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto);
			if (!CollectionUtils.isEmpty(allWordList)){
				for (FzjcxmDto fzjcxmDto : fzjcxmDtos) {
					fzjcxmDto.setWordList(allWordList.stream().filter(e->e.getYwid().equals(fzjcxmDto.getFzxmid())).collect(Collectors.toList()));
				}
			}
			if ("TYPE_FLU".equals(fzjcxxDto.getJclxdm())){
				fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_FLU.getCode());
			}else if("TYPE_JHPCR".equals(fzjcxxDto.getJclxdm())){
				fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_JHPCR.getCode());
			}else {
				fjcfbDto.setYwlx(BusTypeEnum.IMP_REPORT_HPV.getCode());
			}
			List<FjcfbDto> allPdfList = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto);
			if (!CollectionUtils.isEmpty(allPdfList)){
				for (FzjcxmDto fzjcxmDto : fzjcxmDtos) {
					fzjcxmDto.setPdfList(allPdfList.stream().filter(e->e.getYwid().equals(fzjcxmDto.getFzxmid())).collect(Collectors.toList()));
				}
			}
		}
		return fzjcxmDtos;
	}

	@Override
	public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
		// TODO Auto-generated method stub
		FzjcxmDto fzjcxmDto = (FzjcxmDto)baseModel;
		fzjcxmDto.setXgry(operator.getYhid());
		fzjcxmDto.setFzxmid(auditParam.getShgcDto().getYwid());
		dao.updateZt(fzjcxmDto);
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
		for (ShgcDto shgcDto : shgcList) {
			FzjcxmDto fzjcxmDto = new FzjcxmDto();
			fzjcxmDto.setFzxmid(shgcDto.getYwid());
			FzjcxmDto fzjcxmDto_sel = dao.getDto(fzjcxmDto);
			fzjcxmDto.setXgry(operator.getYhid());
			fzjcxmDto.setXm(fzjcxmDto_sel.getXm());
			fzjcxmDto.setJclxdm(fzjcxmDto_sel.getJclxdm());
			//审核退回
			if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {

				fzjcxmDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
				//审核通过
			} else if (AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())) {
				fzjcxmDto.setZt(StatusEnum.CHECK_PASS.getCode());
				fzjcxmDto.setShry(operator.getYhid());
				fzjcxmDto.setBgrq(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
				dealAndGenerateReport(fzjcxmDto);
				FzjcxxDto fzjcxxDto = new FzjcxxDto();
				fzjcxxDto.setFzjcid(fzjcxmDto_sel.getFzjcid());
				int bggcs = Integer.parseInt(String.valueOf(fzjcxmDto_sel.getBgwcs()))+1;
				int xms = Integer.parseInt(String.valueOf(fzjcxmDto_sel.getXms()));
				if (bggcs==xms){
					fzjcxxDto.setZt("80");
				}
				fzjcxxDto.setBgwcs(String.valueOf(bggcs));
				if (StringUtil.isBlank(fzjcxmDto_sel.getZbgrq())){
					fzjcxxDto.setBgrq(fzjcxmDto.getBgrq());
				}
				fzjcxxPJService.updatePjBgrqAndBgwcs(fzjcxxDto);
				amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.NORMAL_INSPECT_TOWECHAT.getCode(), RabbitEnum.PJXX_AUB.getCode() + JSONObject.toJSONString(fzjcxxDto));
			} else { //审核中
				fzjcxmDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				//发送钉钉消息--取消审核人员
				if (!CollectionUtils.isEmpty(shgcDto.getNo_spgwcyDtos())) {
					try {
						int size = shgcDto.getNo_spgwcyDtos().size();
						for (int i = 0; i < size; i++) {
							if (StringUtil.isNotBlank(shgcDto.getNo_spgwcyDtos().get(i).getYhm())) {
								talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getNo_spgwcyDtos().get(i).getYhid(),shgcDto.getNo_spgwcyDtos().get(i).getYhm(),
										shgcDto.getNo_spgwcyDtos().get(i).getYhid(), xxglService.getMsg("ICOMM_SH00004"), xxglService.getMsg("ICOMM_SH00005", operator.getZsxm(), shgcDto.getShlbmc(), fzjcxmDto_sel.getYbbh(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
							}
						}
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
			}
			//更新状态
			dao.updateZt(fzjcxmDto);
			amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.NORMAL_INSPECT_TOWECHAT.getCode(), RabbitEnum.PJXX_AUT.getCode() + JSONObject.toJSONString(fzjcxmDto));
		}
		return true;
	}

	@Override
	public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
		// TODO Auto-generated method stub
		if(shgcList == null || shgcList.isEmpty()){
			return true;
		}
		if(auditParam.isCancelOpe()) {
			//审核回调方法
			for(ShgcDto shgcDto : shgcList){
				String fzxmid = shgcDto.getYwid();
				FzjcxmDto fzjcxmDto = new 	FzjcxmDto();
				fzjcxmDto.setFzxmid(fzxmid);
				fzjcxmDto=getDto(fzjcxmDto);
				fzjcxmDto.setXgry(operator.getYhid());
				fzjcxmDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
				dao.updateZt(fzjcxmDto);
			}
		}else {
			//审核回调方法
			for(ShgcDto shgcDto : shgcList){
				String fzxmid = shgcDto.getYwid();
				FzjcxmDto fzjcxmDto = new 	FzjcxmDto();
				fzjcxmDto.setFzxmid(fzxmid);
				fzjcxmDto=getDto(fzjcxmDto);
				fzjcxmDto.setXgry(operator.getYhid());
				fzjcxmDto.setZt(StatusEnum.CHECK_NO.getCode());
				dao.updateZt(fzjcxmDto);
			}
		}
		return true;
	}

	@Override
	public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) throws BusinessException {
		return null;
	}

	@Override
	public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) throws BusinessException {
		Map<String, Object> map =new HashMap<>();
		@SuppressWarnings("unchecked")
		List<String> ids = (List<String>)param.get("ywids");
		FzjcxmDto fzjcxmDto = new FzjcxmDto();
		fzjcxmDto.setIds(ids);
		List<FzjcxmDto> dtoList = dao.getDtoList(fzjcxmDto);
		List<String> list=new ArrayList<>();
		if(!CollectionUtils.isEmpty(dtoList)){
			for(FzjcxmDto dto:dtoList){
				list.add(dto.getFzxmid());
			}
		}
		map.put("list",list);
		return map;
	}
	/**
	 * 审核完成后处理,为了获取到更新后的报告日期，所以重写基类的方法
	 * @param dto	审核信息，主要是业务ID列表，ywids
	 * @param operator  操作人
	 * @param param 审核传递的额外参数信息
	 */
	public boolean updateAuditEnd(ShxxDto dto, User operator, Map<String, String> param) {
		return true;
	}

	/**
	 * 处理并生成报告
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean dealAndGenerateReport(FzjcxmDto fzjcxmDto) throws BusinessException{
		boolean isSuccess = true;
		//获取基本信息
		List<Map<String, Object>> reportMapList = dao.getFzjcxxInfoForGenerateReport(fzjcxmDto);
		for (Map<String, Object> reportMap : reportMapList) {
			//对基本信息进行处理
			String fzjcid = (String) reportMap.get("fzjcid");
			//分子检测项目的主键id
			String fzxmid = (String) reportMap.get("fzxmid");
			//基础数据的检测子项目csid
			String fzjczxmid = (String) reportMap.get("fzjczxmid");
			List<FzbbztDto> listByFzjcid = fzbbztDao.getListByFzjcid(fzjcid);
			if (!CollectionUtils.isEmpty(listByFzjcid)) {
				StringBuilder bbztmc = new StringBuilder();
				for (FzbbztDto fzbbztDto : listByFzjcid) {
					bbztmc.append(",").append(fzbbztDto.getZtmc());
				}
				bbztmc = new StringBuilder(bbztmc.substring(1));
				reportMap.put("bbzt", bbztmc.toString());
			}
			List<Map<String, Object>> jcjgList = fzjcjgPJService.getFzjcjgListById(fzxmid);
			if(!"TYPE_JHPCR".equals(fzjcxmDto.getJclxdm())){
				while (jcjgList.size()<23) {
					Map<String, Object> jcjgMap = new HashMap<>();
					jcjgMap.put("isBlank", "true");
					jcjgList.add(jcjgMap);
				}
			}
			reportMap.put("jcjgList", jcjgList);
			reportMap.put("fzjczxmid",fzjczxmid);//模板id
			reportMap.put("gzlx", jcjgList.get(0).get("gzlx"));
			reportMap.put("ywid",fzxmid);
			String shry = fzjcxmDto.getShry();
			if (StringUtil.isNotBlank(shry)){
				reportMap.put("shry",shry);
			}
			reportMap.put("sendToAli",true);
			reportMap.put("menuurl",menuurl);
			reportMap.put("jclxdm",fzjcxmDto.getJclxdm());
			//生成替换报告
			generateReport(reportMap);
		}
		return isSuccess;
	}
	/**
	 * 报告生成方法
	 */
	public void generateReport(Map<String,Object> map) throws BusinessException{
		//报告日期
		if (map.get("bgrq") == null) {
			map.put("bgrq", DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd HH:mm:ss"));
		}
		//检验人签名
		String jyryhm = (String) map.get("jyryhm");
		//审核人签名
		String shryhm = "";
		if (map.get("shry")!=null && StringUtil.isNotBlank((String)map.get("shry"))) {
			//如果map中审核人不为空，则使用传入的审核人
			User user=new User();
			user.setYhid((String)map.get("shry"));
			user=commonservice.getUserInfoById(user);
			shryhm = user.getYhm();
		}else{
			ShxxDto shxxParam = new ShxxDto();
			shxxParam.setShlb(AuditTypeEnum.AUDIT_GENERAL_INSPECTION.getCode());
			shxxParam.setYwid(map.get("fzxmid").toString());
			List<ShxxDto> shxxList = shxxService.getShxxOrderByShsj(shxxParam);
			if(!CollectionUtils.isEmpty(shxxList)){
				shryhm=shxxList.get(0).getShryhm();
			}
		}
		if(StringUtil.isBlank(shryhm)){
			throw new BusinessException("msg","缺少审核人信息！");
		}
		//查询签名路径
		String yhmList = jyryhm + "," + shryhm;
		String[] yhms = yhmList.split(",");
		List<String> ywids = Arrays.asList(yhms);
		FjcfbDto fj = new FjcfbDto();
		fj.setYwlx(BusTypeEnum.IMP_SIGNATURE.getCode());
		fj.setYwids(ywids);
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fj);
		if (fjcfbDtos == null || fjcfbDtos.size() <=0){
			throw new BusinessException("msg","缺少电子签名！"+yhmList);
		}
		String[] imageFilePath = new String[ywids.size()];
		DBEncrypt p = new DBEncrypt();
		for (int i = 0; i < ywids.size(); i++) {
			for (FjcfbDto fjcfbDto : fjcfbDtos) {
				if (fjcfbDto.getYwid().equals(ywids.get(i))) {
					imageFilePath[i] = p.dCode(fjcfbDto.getWjlj());
					break;
				}
			}
		}
		if(ywids.size()>1){
			map.put("jyr_Pic",imageFilePath[0]);
			map.put("shr_Pic",imageFilePath[1]);
		}
		log.error("普检报告----------签名获取成功"+yhmList);
		map.put("releaseFilePath", releaseFilePath); // 正式文件路径
		map.put("tempPath", tempPath); // 临时文件路径
		String templateCode = BusTypeEnum.IMP_SCREENING_TEMPLATE.getCode();
		String ywlx="";
		//甲乙流
		if ("TYPE_FLU".equals(map.get("jclxdm"))){
			ywlx = BusTypeEnum.IMP_REPORT_FLU.getCode();
		}else if("TYPE_HPV".equals(map.get("jclxdm"))){
			ywlx = BusTypeEnum.IMP_REPORT_HPV.getCode();
		} else if ("TYPE_JHPCR".equals(map.get("jclxdm"))) {
			ywlx = BusTypeEnum.IMP_REPORT_JHPCR.getCode();
		}
		map.put("ywlx",ywlx);//业务类型
		FjcfbDto fjcfbDto_t = new FjcfbDto();
		fjcfbDto_t.setYwlx(templateCode);
		fjcfbDto_t.setYwid(String.valueOf(map.get("fzjczxmid")));
		List<FjcfbDto> fjcfbDto_ts = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fjcfbDto_t);
		if(fjcfbDto_ts == null || fjcfbDto_ts.size() <= 0){
			throw new BusinessException("msg","缺少模板文件！");
		}
		map.put("wjlj", fjcfbDto_ts.get(0).getWjlj()); // 模板路径
		map.put("wjm", fjcfbDto_ts.get(0).getWjm()); // 模版文件名
		new Thread(() -> {
			XWPFUtil xwpfUtil = new XWPFUtil();
			xwpfUtil.GenerateReport(map, fjcfbService, amqpTempl, DOC_OK, FTP_URL, true);
		}).start();
	}

	@Override
	public void delFzjcxmByIds(FzjcxmDto fzjcxmDto) {
		dao.delFzjcxmByIds(fzjcxmDto);
	}

	@Override
	public void delFzjcxmByFzjc(FzjcxmDto fzjcxmDto) {
		dao.delFzjcxmByFzjc(fzjcxmDto);
	}

	public boolean updateFzjcxmDtos(List<FzjcxmDto> fzjcxmDtos){
        return dao.updateFzjcxmDtos(fzjcxmDtos);
    }

	public List<FzjcxmDto> getJcxmListByFzjcid(String fzjcid){
		return dao.getJcxmListByFzjcid(fzjcid);
	}
}
