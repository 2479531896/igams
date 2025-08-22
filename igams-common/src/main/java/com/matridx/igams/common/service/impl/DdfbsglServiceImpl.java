package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.matridx.igams.common.dao.entities.DdfbsglDto;
import com.matridx.igams.common.dao.entities.DdfbsglModel;
import com.matridx.igams.common.dao.entities.DdspglDto;
import com.matridx.igams.common.dao.entities.DdspjlDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.post.IDdfbsglDao;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDdfbsglService;
import com.matridx.igams.common.service.svcinterface.IDdspglService;
import com.matridx.igams.common.service.svcinterface.IDdspjlService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.file.upload.RandomUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class DdfbsglServiceImpl extends BaseBasicServiceImpl<DdfbsglDto, DdfbsglModel, IDdfbsglDao> implements IDdfbsglService {

	@Autowired
	IDdspglService ddspglService;

	@Autowired
	IJcsjService jcsjService;

	@Autowired
	IDdxxglService ddxxglService;

	@Autowired
	IXxglService xxglService;

	@Autowired
	DingTalkUtil talkUtil;

	@Autowired(required=false)
	private AmqpTemplate amqpTempl;

	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;

	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;

	@Autowired
	DingTalkUtil dingTalkUtil;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	@Value("${matridx.rabbit.flg:}")
	private String rabbitFlg;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IDdspjlService ddspjlService;
	@Value("${matridx.fileupload.releasePath}")
	private String releaseFilePath;
	@Value("${matridx.fileupload.prefix}")
	private String prefix;
	@Autowired
	IFjcfbService fjcfbService;

	private final Logger log = LoggerFactory.getLogger(DdfbsglServiceImpl.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean insertDto(DdfbsglDto ddfbsglDto) {
		if (StringUtil.isBlank(ddfbsglDto.getDdfbsglid())) {
			ddfbsglDto.setDdfbsglid(StringUtil.generateUUID());
		}
		int result = dao.insert(ddfbsglDto);
		return result != 0;
	}

	/**
	 * 执行钉钉审批回调
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean excuteAudit(DdfbsglDto ddfbsglDto) throws BusinessException {
		ddfbsglDto = getDtoById(ddfbsglDto.getProcessinstanceid());
		DdspglDto ddspglDto = new DdspglDto();
		ddspglDto.setProcessinstanceid(ddfbsglDto.getProcessinstanceid());
		ddspglDto.setType("finish");
		ddspglDto.setCljg("0");
		ddspglDto.setEventtype(DingTalkUtil.BPMS_TASK_CHANGE);
		List<DdspglDto> ddspglList = ddspglService.getDtoList(ddspglDto);//获取类型为finish的处理失败的审批管理信息
		boolean sfcg = true;//是否成功回调
		boolean sfqbcg = true;//是否全部回调成功
		if (ddspglList != null && !ddspglList.isEmpty()) {
			RestTemplate t_restTemplate = new RestTemplate();
			boolean sfjxzx = true;//是否继续执行
			for (int i = 0; i < ddspglList.size(); i++) {
				if (i > 0) {
					if (!"1".equals(ddspglList.get(i - 1).getCljg())) {
						sfjxzx = false;
						sfcg = false;
					}
				}
				if (sfjxzx) {
					MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
					String str_obj = "{" + "\"processInstanceId\":" + "\"" + ddspglList.get(i).getProcessinstanceid() + "\"" + "," +
							"\"finishTime\":" + "\"" + ddspglList.get(i).getFinishtime() + "\"" + "," +
							"\"corpId\":" + "\"" + ddspglList.get(i).getCorpid() + "\"" + "," +
							"\"EventType\":" + "\"" + ddspglList.get(i).getEventtype() + "\"" + "," +
							"\"title\":" + "\"" + ddspglList.get(i).getTitle() + "\"" + "," +
							"\"type\":" + "\"" + ddspglList.get(i).getType() + "\"" + "," +
							"\"processCode\":" + "\"" + ddspglList.get(i).getProcesscode() + "\"" + "," +
							"\"result\":" + "\"" + ddspglList.get(i).getResult() + "\"" + "," +
							"\"createTime\":" + "\"" + ddspglList.get(i).getCreatetime() + "\"" + "," +
							"\"staffId\":" + "\"" + ddspglList.get(i).getStaffid() + "\"" +
							"}";
					JSONObject obj = JSON.parseObject(str_obj);
					paramMap.add("obj", obj);
					String fwqm = ddfbsglDto.getFwqm();
					if (StringUtils.isBlank(fwqm))
						fwqm = "";
					@SuppressWarnings("unchecked")
					Map<String, Object> result = t_restTemplate.postForObject(applicationurl + fwqm + ddspglList.get(i).getHddz(), paramMap, Map.class);
					log.info("审批回调完成,结果为------" + result);
					if (result != null) {
						if (result.get("status").equals("success")) {
							ddspglList.get(i).setCljg("1");
						} else if (result.get("status").equals("fail")) {
							ddspglList.get(i).setCljg("0");
							sfqbcg = false;
						} else if (result.get("status").equals("exception")) {
							ddspglList.get(i).setCljg("0");
							sfqbcg = false;
						}
					}
					boolean updateddspgl = ddspglService.updatecljg(ddspglList.get(i));
					if (!updateddspgl)
						return false;
				}
			}
			//查询回调后该审批信息是否所有操作处理结果都为1，如果是则
			if (sfqbcg) {
				ddfbsglDto.setCljg("1");
				boolean result = update(ddfbsglDto);
				if (!result)
					return false;
				//审核全部成功，更新主站钉钉审批管理表，钉钉分布式管理信息
				MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
				paramMap.add("processInstanceId", ddfbsglDto.getProcessinstanceid());
				boolean t_result = t_restTemplate.postForObject(applicationurl + "/ws/updateDdspAndDdfbs", paramMap, boolean.class);
				if (!t_result)
					return false;
			}
		} else {
			throw new BusinessException("msg", "没有可执行的审批回调！");
		}
		return sfcg;
	}

	public List<DdfbsglDto> getNoEndList(DdfbsglDto ddfbsglDto) {
		return dao.getNoEndList(ddfbsglDto);
	}

	public void dealCallback(JSONObject json, String plainText, String token,String wbcxid,Map<String,Object>map) throws BusinessException {
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb("DINGMESSAGETYPE"); //钉钉消息类型
		jcsjDto.setCsdm("DINKTALKAUDIT_CALLBACK_ERROR"); //钉钉审核回调错误通知
		List<DdxxglDto> ddxxglDtos = new ArrayList<>();
		if (jcsjService.getDtoByCsdmAndJclb(jcsjDto) != null) {
			ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());//获取通知人员
		}
		log.error("plainText:" + plainText);
		JSONObject obj = JSON.parseObject(plainText);
		//将外部程序id放入对象，ddspglDto新增得时候保存至ddspglDto表
		obj.put("wbcxid", wbcxid);
		
		String ddspbcbj = "0";//钉钉审批管理信息保存标记(分布式)
		log.error("解密处理完成1");
		//根据回调数据类型做不同的业务处理
		String eventType = obj.getString("EventType");
		String processCode = obj.getString("processCode");
		String processInstanceId = obj.getString("processInstanceId");
		String type = obj.getString("type");

		if(StringUtil.isNotBlank(type) && StringUtil.isNotBlank(eventType) && (map.get("hdcs")==null || (map.get("hdcs")!=null && Integer.parseInt(map.get("hdcs")+"")<2))){
			if(("bpms_instance_change".equals(eventType) && ("finish".equals(type)||"terminate".equals(type)))){
				saveApprovalRecords(obj,processCode,wbcxid,true);
			}
			if(("bpms_task_change".equals(eventType) && "comment".equals(type))){
				saveApprovalRecords(obj,processCode,wbcxid,false);
			}
		}
		
		DdfbsglDto ddfbsglDto = dao.getDtoById(processInstanceId);
		String[] processCodeArrs = {"PROC-FF6YHERSO2-N6LDYUMRQ0DA6XXGSKSP1-AA6SAHSI-D4","PROC-FF6YHERSO2-BE0C9H9IRH5HP1MZPA282-5D3RE9QI-X4",
				"PROC-EF6YLU2SO2-CE0CSA7CUYR0I3K7YQXU3-APARE9QI-57","PROC-A5F6E42B-81CB-45FE-9F5E-9EFBD8915E9F",
				"PROC-DFCCB981-37A5-4C25-80A6-B78B7F495930","PROC-F933F40F-0524-4767-9349-3DE4F1C5E01E",
				"PROC-77A95C90-3EB8-4EB8-AB88-0B14677003A4",
				//离职交接 生物
				"PROC-F36098F3-CC69-442C-A9CB-E2FFB5FB262D",
				//离职交接 毅芯
				"PROC-1B1324BD-F95C-4E48-B004-8CD9E79866F1",
				//离职交接 汇杰
				"PROC-AC860CF9-89D6-4B1C-A3A7-37745F317810",
				//离职交接 内蒙古聚毅医疗科
				"PROC-72F539E3-7EE6-4632-9AE6-E9C1C99A66CA",
				//离职交接 生命指数
				"PROC-53AA33CA-C4FD-4AC9-8A41-C9849F979D8E",
				"PROC-35828662-5ABE-47A2-92CE-E9EE1A784437"};
		List<String> processCodes = Arrays.asList(processCodeArrs);
		//2024-01-02 新增当找不到相应的分布式管理时，需要通知到人员，这样才可以进行人工介入，需要在公司OA的审核维护->钉钉审批管理 的点击执行 进行解决
		if(ddfbsglDto==null && !processCodes.contains(processCode)) {

			if(map.get("hdcs")!=null&&Integer.valueOf(map.get("hdcs")+"")>2){
				for (DdxxglDto ddxxglDto : ddxxglDtos) {
					if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
						talkUtil.sendWorkMessage(ddxxglDto.getYhid(), ddxxglDto.getDdid(),
								xxglService.getMsg("ICOMM_SH00048"), xxglService.getMsg("ICOMM_SH00049", "未找到相应的钉钉分布式管理信息",
										"EventType：" + eventType, processInstanceId, plainText,
										DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
					}
				}
				return;
			}
			log.error("钉钉回调异常延迟队列------发送");
			amqpTempl.convertAndSend("xx_delay_exchange", preRabbitFlg+"xx_delay_key"+rabbitFlg, JSONObject.toJSONString(map), new MessagePostProcessor() {
				@Override
				public Message postProcessMessage(Message message) throws AmqpException {
					message.getMessageProperties().setExpiration("5000");
					return message;
				}
			});
			return;
		}
		
		log.error("EventType=" + eventType);
		log.error("type=" + type);
		if (StringUtil.isNotBlank(eventType) && !DingTalkUtil.CHECK_URL.equals(eventType)) {
			// 保存审批信息(只保存基础数据钉钉回调类型中维护的类型)
			JcsjDto jc_ddsplx = null;
			Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DINGTALK_AUDTI_CALLBACK_TYPE,BasicDataTypeEnum.DINGTALK_CALLBACK_PREFIX});
			List<JcsjDto> ddsplxlist = jclist.get(BasicDataTypeEnum.DINGTALK_AUDTI_CALLBACK_TYPE.getCode());
			if (ddsplxlist != null && !ddsplxlist.isEmpty()) {
				for (JcsjDto dto : ddsplxlist) {
					if (ddfbsglDto != null) {
						if (dto.getCskz1().contains(processCode) && dto.getCsdm().equals(ddfbsglDto.getSpywlx())) {
							jc_ddsplx = dto;
							break;
						}
					} else {
						if (dto.getCskz1().contains(processCode)) {
							jc_ddsplx = dto;
							break;
						}
					}

				}
			}
			if (jc_ddsplx != null) {
				if (ddfbsglDto!=null&&StringUtil.isNotBlank(ddfbsglDto.getWbcxid())){
					obj.put("wbcxid",ddfbsglDto.getWbcxid());
				}
				//新增钉钉审批管理信息
				DdspglDto ddspglDto = ddspglService.insertInfo(obj);
				String callbackurl = "";
				if(ddfbsglDto!=null) {
					callbackurl = jc_ddsplx.getCskz2();
					
					if (StringUtil.isBlank(callbackurl))
						throw new BusinessException("msg", "回调地址为空,回调失败");
				}
				if (DingTalkUtil.BPMS_TASK_CHANGE.equals(eventType)) {
					if (ddfbsglDto != null) {//如果为TASK类型
						dealTaskCallBack(ddspglDto, ddfbsglDto, callbackurl, ddspbcbj, obj, ddxxglDtos, jc_ddsplx.getCsmc(),"1");
					}
				} else {//如果为INSTANCE类型
					log.error(jc_ddsplx.getCsmc() + obj.getString("processCode"));
					//特殊业务特殊处理，请假审批，加班审批，招聘审批，录用审批,差旅费报销审批，会议费付款审批，调岗，职位晋升
					if (processCodes.contains(processCode)) {
						if ("PROC-FF6YHERSO2-N6LDYUMRQ0DA6XXGSKSP1-AA6SAHSI-D4".equals(obj.getString("processCode"))){//如果是请假审批
							MultiValueMap<String, String> qj_paramMap = new LinkedMultiValueMap<>();
//							qj_paramMap.add("data", result.get("body"));
							qj_paramMap.add("processInstanceId", obj.getString("processInstanceId"));
//							qj_paramMap.add("mainProcessInstanceId", result.get("mainProcessInstanceId"));
							qj_paramMap.add("processCode", obj.getString("processCode"));
							qj_paramMap.add("wbcxid", obj.getString("wbcxid"));
//							qj_paramMap.add("result",obj.getString("result"));
//							qj_paramMap.add("status",obj.getString("status"));
							RestTemplate restTemplate = new RestTemplate();
							String urlPrefix="";
							log.error("请求路径:" + applicationurl + urlPrefix + jc_ddsplx.getCskz2());
							qj_paramMap.add("wbcxid",wbcxid);
							Map<String, Object> t_result = restTemplate.postForObject(applicationurl + urlPrefix + jc_ddsplx.getCskz2(), qj_paramMap, Map.class);
							if (t_result != null) {
								log.error(",结果:" + t_result.get("status"));
							}
						}
						//审批通过时保存到本地数据库
						if ("agree".equals(obj.getString("result")) && "finish".equals(obj.getString("type"))&&!"PROC-FF6YHERSO2-N6LDYUMRQ0DA6XXGSKSP1-AA6SAHSI-D4".equals(obj.getString("processCode"))) {
							Map<String, String> result = talkUtil.getProcessMessage(obj.getString("processInstanceId"),talkUtil.getToken(wbcxid));
							MultiValueMap<String, String> qj_paramMap = new LinkedMultiValueMap<>();
							qj_paramMap.add("data", result.get("body"));
							qj_paramMap.add("processInstanceId", obj.getString("processInstanceId"));
							qj_paramMap.add("processCode", obj.getString("processCode"));
							qj_paramMap.add("wbcxid", obj.getString("wbcxid"));
							RestTemplate restTemplate = new RestTemplate();
							String urlPrefix="";
							//PROC-DFCCB981-37A5-4C25-80A6-B78B7F495930
							if("////".equals(obj.getString("processCode"))){
								JSONObject jsonObject = JSONObject.parseObject(result.get("body"));
								JSONObject process_instance = jsonObject.getJSONObject("process_instance");
								JSONArray form_component_values = process_instance.getJSONArray("form_component_values");
								JSONObject fygs = form_component_values.getJSONObject(0);
								List<JcsjDto> ddspqzlist = jclist.get(BasicDataTypeEnum.DINGTALK_CALLBACK_PREFIX.getCode());
								if(ddspqzlist!=null&& !ddspqzlist.isEmpty()){
									for(JcsjDto dto:ddspqzlist){
										if(fygs.getString("value").equals(dto.getCsmc())){
											urlPrefix=dto.getCsdm();
										}
									}
								}
							}
							//PROC-F933F40F-0524-4767-9349-3DE4F1C5E01E
							if("PROC-F933F40F-0524-4767-9349-3DE4F1C5E01E".equals(obj.getString("processCode"))){
								JSONObject jsonObject = JSONObject.parseObject(result.get("body"));
								JSONObject process_instance = jsonObject.getJSONObject("process_instance");
								JSONArray form_component_values = process_instance.getJSONArray("form_component_values");
								JSONObject fygs = form_component_values.getJSONObject(1);
								List<JcsjDto> ddspqzlist = jclist.get(BasicDataTypeEnum.DINGTALK_CALLBACK_PREFIX.getCode());
								if(ddspqzlist!=null&& !ddspqzlist.isEmpty()){
									for(JcsjDto dto:ddspqzlist){
										if(fygs.getString("value").equals(dto.getCsmc())){
											urlPrefix=dto.getCsdm();
										}
									}
								}
							}
							log.error("请求路径:" + applicationurl + urlPrefix + jc_ddsplx.getCskz2());
							qj_paramMap.add("wbcxid",wbcxid);
							Map<String, Object> t_result = restTemplate.postForObject(applicationurl + urlPrefix + jc_ddsplx.getCskz2(), qj_paramMap, Map.class);
							if (t_result != null) {
								log.error(",结果:" + t_result.get("status"));
							}
						}
					} else {
						if (ddfbsglDto != null) {
							dealInstanceCallBack(obj, ddspbcbj, ddspglDto, ddxxglDtos, callbackurl, jc_ddsplx.getCsmc(),"1");
						}else {
							log.error("本地没有相关的" + jc_ddsplx.getCsmc());
						}
					}
				}
			}else {
				if (ddfbsglDto != null)
					log.error("未找到相应回调基础数据：" + obj.getString("processCode") + " " + ddfbsglDto.getSpywlx());
				else
					log.error("未找到相应回调基础数据2：" + obj.getString("processCode"));
			}
		}
	}

	/**
	 * @Description: 保存钉钉审批记录
	 * @param obj
	 * @param processCode
	 * @param wbcxidString
	 * @return void
	 * @Author: 郭祥杰
	 * @Date: 2025/5/23 17:10
	 */
	private void saveApprovalRecords(JSONObject obj, String processCode,String wbcxidString,boolean isType) {
		Map<String, List<JcsjDto>> map = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.APPROVAL_SYNCHRONIZATION_PERMISSIONS });
		List<JcsjDto> jcsjList = map.get(BasicDataTypeEnum.APPROVAL_SYNCHRONIZATION_PERMISSIONS.getCode());
		if(!CollectionUtils.isEmpty(jcsjList)) {
			boolean isExist = jcsjList.stream()
					.anyMatch(jcsjDto -> processCode.equals(jcsjDto.getCsmc()));
			if(isExist){
				String processInstanceId = obj.getString("processInstanceId");
				DdspjlDto dto = ddspjlService.getDtoById(processInstanceId);
				if(!isType && dto==null){
					return;
				}
				Object accessToken = redisUtil.hget("accessToken", wbcxidString);
				String token;
				if (accessToken!=null){
					token  = String.valueOf(accessToken);
				}else {
					token = talkUtil.getToken(wbcxidString);
				}
				try {
					Map<String,Object> dingMap = dingTalkUtil.processInstances(processInstanceId, token);
					if(!dingMap.isEmpty()) {
						dingMap.put("processInstanceId",processInstanceId);
						dingMap.put("processCode",processCode);
						boolean result = false;
						List<FjcfbDto> fjcfbDtos = saveFiles(dingMap,token,processInstanceId);
						if(!CollectionUtils.isEmpty(fjcfbDtos)) {
							result = fjcfbService.batchInsertFjcfb(fjcfbDtos);
							if(!result){
								log.error("附件保存失败!"+JSON.toJSONString(fjcfbDtos));
							}
						}
						DdspjlDto ddspjlDto = generatedRecord(dingMap);
						if(dto==null){
							result = ddspjlService.insert(ddspjlDto);
						}else {
							result = ddspjlService.update(ddspjlDto);
						}
						if(!result){
							log.error("审批记录保存失败!");
						}
					}
				} catch (Exception e) {
					log.error("审核通过或者撤回获取审批实例信息失败",e);
				}
			}
		}
	}

	/**
	 * @Description: 添加附件信息
	 * @param dingMap
	 * @param token
	 * @param processInstanceId
	 * @return void
	 * @Author: 郭祥杰
	 * @Date: 2025/6/5 14:18
	 */
	private List<FjcfbDto> saveFiles(Map<String,Object> dingMap,String token,String processInstanceId) throws Exception{
		List<FjcfbDto> fjcfbDtos = new ArrayList<>();
		List<Map<String,String>> mapList = new ArrayList<>();
		if(dingMap.get("formComponentValues")!=null && StringUtil.isNotBlank(dingMap.get("formComponentValues").toString())){
			List<Map<String,Object>> formComponentList = JSON.parseObject(dingMap.get("formComponentValues").toString(),new TypeReference<List<Map<String,Object>>>(){});
			if(!CollectionUtils.isEmpty(formComponentList)){
				for(Map<String,Object> formComponent:formComponentList) {
					if(formComponent.get("componentType")!=null && formComponent.get("value")!=null && "DDAttachment".equals(formComponent.get("componentType").toString()) && StringUtil.isNotBlank(formComponent.get("value").toString())){
						List<Map<String,String>> attachmentList = JSON.parseObject(formComponent.get("value").toString(),new TypeReference<List<Map<String,String>>>(){});
						for(Map<String,String> attachment:attachmentList) {
							String fileName = attachment.get("fileName");
							String fileId = attachment.get("fileId");
							Map<String,String> fileMap = new HashMap<>();
							fileMap.put("fileName",fileName);
							fileMap.put("fileId",fileId);
							fileMap.put("comFlag","false");
							String fjid =StringUtil.generateUUID();
							attachment.put("fjid",fjid);
							FjcfbDto fjcfbDto = new FjcfbDto();
							String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
							int index = (fileName.lastIndexOf(".") > 0) ? fileName.lastIndexOf(".") : fileName.length() - 1;
							String suffix = fileName.substring(index);
							String saveName = t_name + suffix;
							assembleFicfbDto(fjid,fileId,fileName,saveName,fjcfbDto);
							fjcfbDtos.add(fjcfbDto);
							fileMap.put("fwjm",saveName);
							mapList.add(fileMap);
						}
						formComponent.put("value",attachmentList);
					}
					if(formComponent.get("componentType")!=null && formComponent.get("value")!=null && "DDPhotoField".equals(formComponent.get("componentType").toString()) && StringUtil.isNotBlank(formComponent.get("value").toString())){
						List<String> imageList = JSON.parseObject(formComponent.get("value").toString(), new TypeReference<List<String>>() {});
						if(!CollectionUtils.isEmpty(imageList)) {
							List<Map<String,String>> imageListMap = new ArrayList<>();
							for (String image : imageList) {
								int lastSlashIndex = image.lastIndexOf('/');
								if (lastSlashIndex != -1 && lastSlashIndex < image.length() - 1) {
									String fileName = image.substring(lastSlashIndex + 1);
									Map<String,String> imageMap = new HashMap<>();
									imageMap.put("fileName",fileName);
									imageMap.put("imageUrl",image);
									String fjid =StringUtil.generateUUID();
									imageMap.put("fjid",fjid);
									imageListMap.add(imageMap);
									String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
									int index = (fileName.lastIndexOf(".") > 0) ? fileName.lastIndexOf(".") : fileName.length() - 1;
									String suffix = fileName.substring(index);
									String saveName = t_name + suffix;
									try {
										downloadFile(image,prefix + releaseFilePath +BusTypeEnum.IMP_DINGDING.getCode(),saveName);
									} catch (IOException e) {
										log.error("下载审批图片失败!imageUrl="+image,e);
									}
									FjcfbDto fjcfbDto = new FjcfbDto();
									assembleFicfbDto(fjid,null,fileName,saveName,fjcfbDto);
									fjcfbDtos.add(fjcfbDto);
								}
							}
							if(!CollectionUtils.isEmpty(imageListMap)){
								formComponent.put("value",imageListMap);
							}
						}
					}
				}
				dingMap.put("formComponentValues",formComponentList);
			}
		}
		if(dingMap.get("operationRecords")!=null && StringUtil.isNotBlank(dingMap.get("operationRecords").toString())) {
			List<Map<String, Object>> operationRecordsList = JSON.parseObject(dingMap.get("operationRecords").toString(), new TypeReference<List<Map<String, Object>>>() {});
			if (!CollectionUtils.isEmpty(operationRecordsList)) {
				for (Map<String, Object> operationRecord : operationRecordsList) {
					if(operationRecord.get("attachments")!=null && StringUtil.isNotBlank(operationRecord.get("attachments").toString())) {
						List<Map<String, String>> attachmentList = JSON.parseObject(operationRecord.get("attachments").toString(), new TypeReference<List<Map<String, String>>>() {
						});
						for (Map<String, String> attachment : attachmentList) {
							String fileName = attachment.get("fileName");
							String fileId = attachment.get("fileId");
							Map<String,String> fileMap = new HashMap<>();
							fileMap.put("fileName",fileName);
							fileMap.put("fileId",fileId);
							fileMap.put("comFlag","true");
							String fjid =StringUtil.generateUUID();
							attachment.put("fjid",fjid);
							FjcfbDto fjcfbDto = new FjcfbDto();
							String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
							int index = (fileName.lastIndexOf(".") > 0) ? fileName.lastIndexOf(".") : fileName.length() - 1;
							String suffix = fileName.substring(index);
							String saveName = t_name + suffix;
							assembleFicfbDto(fjid,fileId,fileName,saveName,fjcfbDto);
							fjcfbDtos.add(fjcfbDto);
							fileMap.put("fwjm",saveName);
							mapList.add(fileMap);
						}
						operationRecord.put("attachments",attachmentList);
					}
					if(operationRecord.get("images")!=null && StringUtil.isNotBlank(operationRecord.get("images").toString())) {
						List<String> imageList = JSON.parseObject(operationRecord.get("images").toString(), new TypeReference<List<String>>() {});
						if(!CollectionUtils.isEmpty(imageList)) {
							List<Map<String,String>> imageListMap = new ArrayList<>();
							for (String image : imageList) {
								int lastSlashIndex = image.lastIndexOf('/');
								if (lastSlashIndex != -1 && lastSlashIndex < image.length() - 1) {
									String fileName = image.substring(lastSlashIndex + 1);
									Map<String,String> imageMap = new HashMap<>();
									imageMap.put("fileName",fileName);
									imageMap.put("imageUrl",image);
									String fjid =StringUtil.generateUUID();
									imageMap.put("fjid",fjid);
									imageListMap.add(imageMap);
									String t_name = System.currentTimeMillis() + RandomUtil.getRandomString();
									int index = (fileName.lastIndexOf(".") > 0) ? fileName.lastIndexOf(".") : fileName.length() - 1;
									String suffix = fileName.substring(index);
									String saveName = t_name + suffix;
									try {
										downloadFile(image,prefix + releaseFilePath +BusTypeEnum.IMP_DINGDING.getCode(),saveName);
									} catch (IOException e) {
										log.error("下载评论图片失败!imageUrl="+image,e);
									}
									FjcfbDto fjcfbDto = new FjcfbDto();
									assembleFicfbDto(fjid,null,fileName,saveName,fjcfbDto);
									fjcfbDtos.add(fjcfbDto);
								}
							}
							if(!CollectionUtils.isEmpty(imageListMap)){
								operationRecord.put("images",imageListMap);
							}
						}
					}
				}
				dingMap.put("operationRecords",operationRecordsList);
			}
		}
		if(!CollectionUtils.isEmpty(mapList)) {
			int batchSize = 10;
			for (int i = 0; i < mapList.size(); i += batchSize) {
				int end = Math.min(i + batchSize, mapList.size());
				List<Map<String, String>> batch = mapList.subList(i, end);
				dingTalkUtil.authorizationToDownload(token,batch);
			}
			for (Map<String,String> fileMap:mapList) {
				String fileName = fileMap.get("fileName");
				String fileId = fileMap.get("fileId");
				String comFlag = fileMap.get("comFlag");
				String fwjm = fileMap.get("fwjm");
				String downloadUrl = dingTalkUtil.getDownloadUrl(token,fileId,processInstanceId,Boolean.parseBoolean(comFlag));
				downloadFile(downloadUrl,prefix + releaseFilePath +BusTypeEnum.IMP_DINGDING.getCode(),fwjm);
			}
		}
		return fjcfbDtos;
	}

	/**
	 * @Description: 组装附件存放表数据
	 * @param fjcfbDto
	 * @return void
	 * @Author: 郭祥杰
	 * @Date: 2025/6/6 14:48
	 */
	private void assembleFicfbDto(String fjid,String fileId,String fileName,String fwjm,FjcfbDto fjcfbDto){
		DBEncrypt bpe = new DBEncrypt();
		fjcfbDto.setFjid(fjid);
		fjcfbDto.setYwid(fileId);
		fjcfbDto.setZywid(null);
		fjcfbDto.setXh("1");
		fjcfbDto.setYwlx(BusTypeEnum.IMP_DINGDING.getCode());
		fjcfbDto.setWjm(fileName);
		fjcfbDto.setWjlj(bpe.eCode(prefix + releaseFilePath +BusTypeEnum.IMP_DINGDING.getCode()+"/"+fwjm));
		fjcfbDto.setFwjlj(bpe.eCode(prefix + releaseFilePath +BusTypeEnum.IMP_DINGDING.getCode()));
		fjcfbDto.setFwjm(bpe.eCode(fwjm));
		fjcfbDto.setZhbj("0");
	}


	/**
	 * @Description: 下载文件
	 * @param fileUrl 下载路径
	 * @param saveDir 保存路径
	 * @param fileName 文件名
	 * @return void
	 * @Author: 郭祥杰
	 * @Date: 2025/6/5 9:30
	 */
	private void downloadFile(String fileUrl, String saveDir,String fileName) throws IOException {
		Files.createDirectories(Paths.get(saveDir));
		String filePath = Paths.get(saveDir, fileName).toString();
		try (InputStream in = new URL(fileUrl).openStream();
			 OutputStream out = new FileOutputStream(filePath)) {
			byte[] buffer = new byte[8192];
			int bytesRead;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
		}
	}

	/**
	 * @Description: 组装钉钉审批记录信息
	 * @param dingMap
	 * @return com.matridx.igams.common.dao.entities.DdspjlDto
	 * @Author: 郭祥杰
	 * @Date: 2025/5/23 17:10
	 */
	private DdspjlDto generatedRecord(Map<String,Object> dingMap) {
		DdspjlDto ddspjlDto = new DdspjlDto();
		ddspjlDto.setProcessinstanceid(String.valueOf(dingMap.get("processInstanceId")!=null?dingMap.get("processInstanceId"):""));
		ddspjlDto.setProcesscode(String.valueOf(dingMap.get("processCode")!=null?dingMap.get("processCode"):""));
		ddspjlDto.setAttachedprocessinstanceids(JSON.toJSONString(dingMap.get("attachedProcessInstanceIds")!=null?dingMap.get("attachedProcessInstanceIds"):""));
		ddspjlDto.setBizaction(String.valueOf(dingMap.get("bizAction")!=null?dingMap.get("bizAction"):""));
		ddspjlDto.setBizdata(JSON.toJSONString(dingMap.get("bizData")!=null?dingMap.get("bizData"):""));
		ddspjlDto.setBusinessid(String.valueOf(dingMap.get("businessId")!=null?dingMap.get("businessId"):""));
		ddspjlDto.setCcuserids(String.valueOf(dingMap.get("ccUserIds")!=null?dingMap.get("ccUserIds"):""));
		ddspjlDto.setCreatetime(dingMap.get("createTime")!=null?convertIsoToUtcFormat(String.valueOf(dingMap.get("createTime"))):"");
		ddspjlDto.setFinishtime(dingMap.get("finishTime")!=null?convertIsoToUtcFormat(String.valueOf(dingMap.get("finishTime"))):"");
		ddspjlDto.setFormcomponentvalues(JSON.toJSONString(dingMap.get("formComponentValues")!=null?dingMap.get("formComponentValues"):""));
		ddspjlDto.setOperationrecords(JSON.toJSONString(dingMap.get("operationRecords")!=null?dingMap.get("operationRecords"):""));
		ddspjlDto.setOriginatordeptid(String.valueOf(dingMap.get("originatorDeptId")!=null?dingMap.get("originatorDeptId"):""));
		ddspjlDto.setOriginatordeptname(String.valueOf(dingMap.get("originatorDeptName")!=null?dingMap.get("originatorDeptName"):""));
		ddspjlDto.setOriginatoruserid(String.valueOf(dingMap.get("originatorUserId")!=null?dingMap.get("originatorUserId"):""));
		ddspjlDto.setResult(String.valueOf(dingMap.get("result")!=null?dingMap.get("result"):""));
		ddspjlDto.setStatus(String.valueOf(dingMap.get("status")!=null?dingMap.get("status"):""));
		ddspjlDto.setTasks(JSON.toJSONString(dingMap.get("tasks")!=null?dingMap.get("tasks"):""));
		ddspjlDto.setTitle(String.valueOf(dingMap.get("title")!=null?dingMap.get("title"):""));
		return ddspjlDto;
	}

	/**
	 * @Description: 持UTC时区格式 (yyyy-MM-dd HH:mm:ss)
	 * @param isoString
	 * @return java.lang.String
	 * @Author: 郭祥杰
	 * @Date: 2025/5/29 15:57
	 */
	private String convertIsoToUtcFormat(String isoString){
		DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
				.withResolverStyle(ResolverStyle.STRICT);
		Instant instant = formatter.parse(isoString, Instant::from);
		ZonedDateTime utcDateTime = instant.atZone(ZoneId.of("UTC"));
		return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(utcDateTime);
	}
	/**
	 * 处理审批回调为task类型的钉钉审批消息
	 */
	public void dealTaskCallBack(DdspglDto ddspglDto, DdfbsglDto ddfbsglDto, String callbackurl, String ddspbcbj, JSONObject obj, List<DdxxglDto> ddxxglDtos, String msg, String tbbj) {
		
		RestTemplate t_restTemplate = new RestTemplate();
		MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
		paramMap.add("obj", obj);
		String servicename = ddfbsglDto.getFwqm();
		String hddz = ddfbsglDto.getHddz();
		if (StringUtils.isBlank(hddz)) {//如果为空，默认回调190
			hddz = applicationurl;
		}
		if (StringUtils.isBlank(servicename)) {
			servicename = "";
		}
		if (StringUtils.isNotBlank(servicename) && !servicename.equals(urlPrefix) && (StringUtil.isNotBlank(ddfbsglDto.getHddz()) && !hddz.equals(ddfbsglDto.getHddz())))
			ddspbcbj = "1";
		paramMap.add("ddspbcbj", ddspbcbj);
		Map<String, String> result;
		log.error("审批回调开始,地址为------" + hddz + servicename + callbackurl);
		result = t_restTemplate.postForObject(hddz + servicename + callbackurl, paramMap, Map.class);
			
		log.error("审批回调完成,结果为------" + result);
		if (result != null) {
			if (result.get("status").equals("success")) {
				if ("finish".equals(obj.getString("type"))) {
					ddspglDto.setCljg("1");
				}
			}else if (result.get("status").equals("fail")) {
				ddspglDto.setCljg("0");
			} else if (result.get("status").equals("exception")) {
				ddspglDto.setCljg("0");
				if("1".equals(tbbj) && ddxxglDtos != null && !ddxxglDtos.isEmpty() && !"10001".equals(result.get("message"))) { //判断是否异常，同步标记是否为1，1:正常审批回调；0:定时任务回调
					for (DdxxglDto ddxxglDto : ddxxglDtos) {
						if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
							talkUtil.sendWorkMessage(ddxxglDto.getYhid(), ddxxglDto.getDdid(),
									xxglService.getMsg("ICOMM_SH00048"), xxglService.getMsg("ICOMM_SH00049", msg, ddfbsglDto.getFwqmc() + "(" + ddfbsglDto.getHddz() + ")", ddfbsglDto.getProcessinstanceid(), result.get("message"), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}

			}
		}
		if (!servicename.equals(urlPrefix)) {
			boolean updateddspgl = ddspglService.updatecljg(ddspglDto);
			log.error("更新钉钉审批处理结果:" + updateddspgl + "结果为:" + ddspglDto.getCljg());
		}
	}

	/**
	 * 处理instance钉钉消息类型的回调业务
	 */
	public void dealInstanceCallBack(JSONObject obj, String ddspbcbj, DdspglDto ddspglDto, List<DdxxglDto> ddxxglDtos, String callbackurl, String msg, String tbbj) {
		RestTemplate ht_restTemplate = new RestTemplate();
		MultiValueMap<String, Object> ht_paramMap = new LinkedMultiValueMap<>();
		ht_paramMap.add("obj", obj);
		String ht_processInstanceId = obj.getString("processInstanceId");
		DdfbsglDto ddfbsglDto = dao.getDtoById(ht_processInstanceId);
		String servicename;
		String hddz = ddfbsglDto.getHddz();
		if (StringUtils.isBlank(hddz)) {//如果为空，默认回调190
			hddz = applicationurl;
		}
		servicename = ddfbsglDto.getFwqm();
		if (StringUtils.isBlank(servicename)) {
			servicename = "";
		}
		if (StringUtils.isNotBlank(servicename) && !servicename.equals(urlPrefix) && (StringUtil.isNotBlank(ddfbsglDto.getHddz()) && !hddz.equals(ddfbsglDto.getHddz())) )
			ddspbcbj = "1";
		ht_paramMap.add("ddspbcbj", ddspbcbj);
		Map<String, String> result;
		log.error("审批回调中途," + hddz + servicename + callbackurl);
		result = ht_restTemplate.postForObject(hddz + servicename + callbackurl, ht_paramMap, Map.class);
		log.error("审批回调完成,结果为------" + result);
		if (result != null) {
			if (result.get("status").equals("success")) {
				ddspglDto.setCljg("1");
				if ("finish".equals(obj.getString("type")) || "terminate".equals(obj.getString("type"))) {
					//此时钉钉审批已结束。查询OA审批是否出现异常或失败，更新ddfbsgl表信息
					DdfbsglDto ddfbsglDto_b = new DdfbsglDto();
					ddfbsglDto_b.setJszt("1");
					ddfbsglDto_b.setProcessinstanceid(ddspglDto.getProcessinstanceid());
					dao.update(ddfbsglDto_b);
					log.error("servicename:" + servicename + "urlPrefix:" + urlPrefix);
					if (StringUtils.isNotBlank(servicename) && !servicename.equals(urlPrefix)) {
						ht_paramMap.add("jszt", "1");
						boolean result_t = ht_restTemplate.postForObject(hddz + servicename + "/ws/production/updateSaveDdfbsgl", ht_paramMap, boolean.class);
						log.error("事件结束时，添加分布式钉钉审批管理数据,结果:" + result_t);
					}
				} else if ("start".equals(obj.getString("type"))) {
					//审批事件发起时更新主站分布式管理表信息
					DdfbsglDto ddfbsglDto_c = new DdfbsglDto();
					ddfbsglDto_c.setYwmc(obj.getString("title"));
					ddfbsglDto_c.setYwlx(obj.getString("processCode"));
					ddfbsglDto_c.setProcessinstanceid(obj.getString("processInstanceId"));
					dao.update(ddfbsglDto_c);
					if (StringUtils.isNotBlank(servicename) && !servicename.equals(urlPrefix)) {
						boolean result_t = ht_restTemplate.postForObject(hddz + servicename + "/ws/production/updateSaveDdfbsgl", ht_paramMap, boolean.class);
						log.error("事件开始时，添加分布式钉钉审批管理数据,结果:" + result_t);
					}
				}
			}else if (result.get("status").equals("fail")) {
				ddspglDto.setCljg("0");
			} else if (result.get("status").equals("exception")) {
				ddspglDto.setCljg("0");
				if("1".equals(tbbj) && ddxxglDtos != null && !ddxxglDtos.isEmpty() && !"10001".equals(result.get("message"))) {
					for (DdxxglDto ddxxglDto : ddxxglDtos) {
						if (StringUtil.isNotBlank(ddxxglDto.getDdid())) {
							talkUtil.sendWorkMessage(ddxxglDto.getYhid(), ddxxglDto.getDdid(),
									xxglService.getMsg("ICOMM_SH00048"), xxglService.getMsg("ICOMM_SH00049", msg, ddfbsglDto.getFwqmc() + "(" + ddfbsglDto.getHddz() + ")", ddfbsglDto.getProcessinstanceid(), result.get("message"), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
						}
					}
				}

			}
		}

		if (!servicename.equals(urlPrefix)) {
			boolean updateddspgl = ddspglService.updatecljg(ddspglDto);
			log.error("更新钉钉审批处理结果:" + updateddspgl + "结果为:" + ddspglDto.getCljg());
		}
	}


	public void toexcuteBatchAudit() {
		excuteBatchAudit();
	}

	public void excuteBatchAudit() {
		DdfbsglDto ddfbsglDto=new DdfbsglDto();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		String rqend=sdf.format(new Date());
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH,-15);
		Date befordate=calendar.getTime();
		String rqstart=sdf.format(befordate);
		ddfbsglDto.setRqend(rqend);
		ddfbsglDto.setRqstart(rqstart);
		//获取两天内未结束的钉钉审批信息
		List<DdfbsglDto> ddfbsgllist=dao.getNoEndList(ddfbsglDto);
		if(ddfbsgllist!=null && !ddfbsgllist.isEmpty()){
			for(DdfbsglDto ddfbsgl:ddfbsgllist){
				try {
					callbackBatch(ddfbsgl);					
				}catch(Exception e) {
					log.error("同步异常---------"+ddfbsgl.getProcessinstanceid());
					log.error(e.getMessage());
				}
			}		
		}
	}

	/**
	 * 定时回调未完成的钉钉审批
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String,String> callbackBatch(DdfbsglDto ddfbsglDtos) {

		Map<String ,String> map;
		String processInstanceId;
		map = new HashMap<>();
		//进行消息组装
		log.error("开始获取审批结果--------------");
		List<String> strlist=new ArrayList<>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		processInstanceId=ddfbsglDtos.getProcessinstanceid();
		//根据miniappid获取token
		String token = dingTalkUtil.getDingTokenByWbcxid(ddfbsglDtos.getWbcxid());
		Map<String,String> result=talkUtil.getProcessMessage(ddfbsglDtos.getProcessinstanceid(),token);
		String obj=result.get("body");
		log.error("body------------:" + obj );
		JSONObject jsonObject = JSON.parseObject(obj);
		JSONObject process_instance = jsonObject.getJSONObject("process_instance");
		String operation_records = String.valueOf(process_instance.get("operation_records"));
		List<Object> process_instance_json=JSON.parseArray(operation_records);
		List<Object> EXECUTE_TASK_NORMAL_list=new ArrayList<Object>();
		for(Object msg:process_instance_json){
			String str=String.valueOf(msg);
			JSONObject json = JSON.parseObject(str);
			if("EXECUTE_TASK_NORMAL".equals(json.get("operation_type"))) //正常执行任务
				EXECUTE_TASK_NORMAL_list.add(msg);
		}
		
		DdspglDto ddspglDto=new DdspglDto();
		ddspglDto.setProcessinstanceid(processInstanceId);
		ddspglDto.setType("finish");
		ddspglDto.setCljg("1");
		List<DdspglDto> ddspglDtos=ddspglService.getDtoList(ddspglDto);//按时间升序排序
		try {			
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			if(ddspglDtos!=null && !ddspglDtos.isEmpty()){
				Date finishdate = null;	
				if(StringUtil.isNotBlank(ddspglDtos.get(ddspglDtos.size()-1).getFinishtime())) {
					finishdate = df.parse(ddspglDtos.get(ddspglDtos.size()-1).getFinishtime());
				}
				
				//取审批时间最近的一条数据的finishdate
				if(!EXECUTE_TASK_NORMAL_list.isEmpty()){
					for(int j=0;j<=EXECUTE_TASK_NORMAL_list.size()-1;j++){
						String str=String.valueOf(EXECUTE_TASK_NORMAL_list.get(j));
						JSONObject json = JSON.parseObject(str);
						Date dd_finishdate = df.parse(String.valueOf(json.get("date")));
						if (finishdate!=null && dd_finishdate.compareTo(finishdate)>0) {//比较日期,若获取到的审批信息中finishdate大于系统中的，说明消息丢失，需要重新发送
							log.error("processInstanceId:" + ddfbsglDtos.getProcessinstanceid() + "status:" + process_instance.get("status"));
							if (process_instance.get("status") != null) {
		//
								if (StringUtils.isNotBlank(String.valueOf(json.get("userid")))) {
									log.error("获取成功开始组装消息Task----------");
									String str_task = "{" + "\"processInstanceId\":" + "\"" + ddfbsglDtos.getProcessinstanceid() + "\"" + "," +
											"\"finishTime\":" + "\"" + dd_finishdate.getTime() + "\"" + "," +
											"\"corpId\":" + "\"" + "\"" + "," +
											"\"EventType\":" + "\"" + "bpms_task_change" + "\"" + "," +
											"\"title\":" + "\"" + process_instance.get("title") + "\"" + "," +
											"\"type\":" + "\"" + "finish" + "\"" + "," +
											"\"processCode\":" + "\"" + ddfbsglDtos.getYwlx() + "\"" + "," +
											"\"result\":" + "\"" + process_instance.get("result") + "\"" + "," +
											"\"createTime\":" + "\"" + sf.parse(String.valueOf(process_instance.get("create_time"))).getTime() + "\"" + "," +
											"\"staffId\":" + "\"" + json.get("userid") + "\"" + "," +
											"\"taskId\":" + "\"" + "\"" +
											"}";
									log.error("组装Task---" + str_task);
									if (StringUtils.isNotBlank(ddfbsglDtos.getYwlx()))
										strlist.add(str_task);
								}
								if("COMPLETED".equals(process_instance.get("status")) && j==EXECUTE_TASK_NORMAL_list.size()-1) {//事件结束
									log.error("获取成功开始组装消息Instance----------");
									String str_obj= "{"+"\"processInstanceId\":"+"\""+ddfbsglDtos.getProcessinstanceid()+"\""+","+
											"\"finishTime\":"+"\""+ dd_finishdate.getTime() +"\""+","+
											"\"corpId\":"+"\""+"\""+","+
											"\"EventType\":"+"\""+"bpms_instance_change"+"\""+","+
											"\"title\":"+"\""+process_instance.get("title")+"\""+","+
											"\"type\":"+"\""+"finish"+"\""+","+
											"\"processCode\":"+"\""+ddfbsglDtos.getYwlx()+"\""+","+
											"\"result\":"+"\""+process_instance.get("result")+"\""+","+
											"\"createTime\":"+"\""+sf.parse(String.valueOf(process_instance.get("create_time"))).getTime()+"\""+","+
											"\"staffId\":"+"\""+process_instance.get("originator_userid")+"\""+
											"}";
									log.error("组装Instance---"+str_obj);
									if(StringUtils.isNotBlank(ddfbsglDtos.getYwlx()))
										strlist.add(str_obj);
								}
							}
						}
					}
				}
			}else{
				//没有任何审批信息
				for(int j=0;j<=EXECUTE_TASK_NORMAL_list.size()-1;j++){
					String str=String.valueOf(EXECUTE_TASK_NORMAL_list.get(j));
					JSONObject json = JSON.parseObject(str);
					Date dd_finishdate = df.parse(String.valueOf(json.get("date")));
					log.error("没有任何审批信息 processInstanceId:" + ddfbsglDtos.getProcessinstanceid() + "status:" + process_instance.get("status"));
					if (process_instance.get("status") != null) {
		//
						if (StringUtils.isNotBlank(String.valueOf(json.get("userid")))) {
							log.error("获取成功开始组装消息Task----------");
							String str_task = "{" + "\"processInstanceId\":" + "\"" + ddfbsglDtos.getProcessinstanceid() + "\"" + "," +
									"\"finishTime\":" + "\"" + dd_finishdate.getTime() + "\"" + "," +
									"\"corpId\":" + "\"" + "\"" + "," +
									"\"EventType\":" + "\"" + "bpms_task_change" + "\"" + "," +
									"\"title\":" + "\"" + process_instance.get("title") + "\"" + "," +
									"\"type\":" + "\"" + "finish" + "\"" + "," +
									"\"processCode\":" + "\"" + ddfbsglDtos.getYwlx() + "\"" + "," +
									"\"result\":" + "\"" + process_instance.get("result") + "\"" + "," +
									"\"createTime\":" + "\"" + sf.parse(String.valueOf(process_instance.get("create_time"))).getTime() + "\"" + "," +
									"\"staffId\":" + "\"" + json.get("userid") + "\"" + "," +
									"\"taskId\":" + "\"" + "\"" +
									"}";
							log.error("组装Task---" + str_task);
							if (StringUtils.isNotBlank(ddfbsglDtos.getYwlx()))
								strlist.add(str_task);
						}
						if("COMPLETED".equals(process_instance.get("status")) && j==EXECUTE_TASK_NORMAL_list.size()-1) {//事件结束
							log.error("获取成功开始组装消息Instance----------");
							String str_obj= "{"+"\"processInstanceId\":"+"\""+ddfbsglDtos.getProcessinstanceid()+"\""+","+
									"\"finishTime\":"+"\""+ dd_finishdate.getTime() +"\""+","+
									"\"corpId\":"+"\""+"\""+","+
									"\"EventType\":"+"\""+"bpms_instance_change"+"\""+","+
									"\"title\":"+"\""+process_instance.get("title")+"\""+","+
									"\"type\":"+"\""+"finish"+"\""+","+
									"\"processCode\":"+"\""+ddfbsglDtos.getYwlx()+"\""+","+
									"\"result\":"+"\""+process_instance.get("result")+"\""+","+
									"\"createTime\":"+"\""+sf.parse(String.valueOf(process_instance.get("create_time"))).getTime()+"\""+","+
									"\"staffId\":"+"\""+process_instance.get("originator_userid")+"\""+
									"}";
							log.error("组装Instance---"+str_obj);
							if(StringUtils.isNotBlank(ddfbsglDtos.getYwlx()))
								strlist.add(str_obj);
						}
					}
				}
			}
		} catch (ParseException e) {
			log.error("回调失败1----");
			log.error(e.getMessage());
		}
		try {
			if(!strlist.isEmpty())
				startCallback(strlist,ddfbsglDtos.getWbcxid());//进行回调
		}catch (Exception e) {
			log.error("回调失败2----");
			log.error(e.getMessage());
		}
		return map;
	}

	/**
	 * 回调
	 */
	public void startCallback(List<String> stringList,String wbcxid) throws BusinessException {
		if (stringList != null && !stringList.isEmpty()) {
			for (String plainText : stringList) {
				JcsjDto jcsjDto = new JcsjDto();
				jcsjDto.setJclb("DINGMESSAGETYPE");
				jcsjDto.setCsdm("DINKTALKAUDIT_CALLBACK_ERROR");
				List<DdxxglDto> ddxxglDtos = new ArrayList<>();
				if (jcsjService.getDtoByCsdmAndJclb(jcsjDto) != null) {
					ddxxglDtos = ddxxglService.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
				}
				//从post请求的body中获取回调信息的加密数据进行解密处理
				log.error("plainText:" + plainText);
				JSONObject obj = JSON.parseObject(plainText);
				String ddspbcbj = "0";//钉钉审批管理信息保存标记(分布式)
				log.error("解密处理完成2");
				//根据回调数据类型做不同的业务处理
				String eventType = obj.getString("EventType");
				log.error("EventType=" + eventType);
				if (StringUtil.isNotBlank(eventType) && !DingTalkUtil.CHECK_URL.equals(eventType)) {
					// 保存审批信息(只保存基础数据钉钉回调类型中维护的类型)
					boolean resultinfo = false;
					Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DINGTALK_AUDTI_CALLBACK_TYPE});
					List<JcsjDto> ddsplxlist = jclist.get(BasicDataTypeEnum.DINGTALK_AUDTI_CALLBACK_TYPE.getCode());
					if (ddsplxlist != null && !ddsplxlist.isEmpty()) {
						for (JcsjDto dto : ddsplxlist) {
							if (dto.getCskz1().contains(obj.getString("processCode"))) {
								resultinfo = true;
								break;
							}
						}
					}
					if (resultinfo) {
						//根据cropid获取wbcxid
						// String wbcxidString = dao.getWbcxBycropid(obj.getString("cropid"));
						obj.put("wbcxid", wbcxid);
						DdspglDto ddspglDto = ddspglService.insertInfo(obj);
						String callbackurl = "";
						String processInstanceId = obj.getString("processInstanceId");
						DdfbsglDto ddfbsglDto = dao.getDtoById(processInstanceId);
						log.info("审批任务进度更新: " + plainText);
						// 可以根据 PROCESS_CODE 来判断这是属于哪个模板发起的流程
						if (StringUtils.isNotBlank(obj.getString("processCode"))) {
							JcsjDto ddsplx = null;
							for (JcsjDto dto : ddsplxlist) {
								if (dto.getCskz1().contains(obj.getString("processCode"))) {
									log.info(dto.getCsmc() + obj.getString("processCode"));
									if (StringUtils.isNotBlank(dto.getCskz2())) {
										if (StringUtils.isNotBlank(ddfbsglDto.getSpywlx())) {
											if (ddfbsglDto.getSpywlx().equals(dto.getCsdm())) {
												callbackurl = dto.getCskz2();
												ddsplx = dto;
												break;
											}
										} else {
											callbackurl = dto.getCskz2();
											ddsplx = dto;
											break;
										}
									}
								}
							}
							if (ddsplx == null)
								throw new BusinessException("msg", "未获取到相关业务类型");
							if (StringUtil.isBlank(callbackurl))
								throw new BusinessException("msg", "回调地址为空,回调失败");
							if (DingTalkUtil.BPMS_TASK_CHANGE.equals(eventType)) {//如果为TASK类型
								dealTaskCallBack(ddspglDto, ddfbsglDto, callbackurl, ddspbcbj, obj, ddxxglDtos, ddsplx.getCsmc(),"0");
							} else {//如果为INSTANCE类型
								log.error(ddsplx.getCsmc() + obj.getString("processCode"));
								//特殊业务特殊处理，请假审批，加班审批，招聘审批，录用审批
								if ("PROC-FF6YHERSO2-N6LDYUMRQ0DA6XXGSKSP1-AA6SAHSI-D4".equals(obj.getString("processCode"))
										|| "PROC-FF6YHERSO2-BE0C9H9IRH5HP1MZPA282-5D3RE9QI-X4".equals(obj.getString("processCode"))
										|| "PROC-EF6YLU2SO2-CE0CSA7CUYR0I3K7YQXU3-APARE9QI-57".equals(obj.getString("processCode"))
										|| "PROC-A5F6E42B-81CB-45FE-9F5E-9EFBD8915E9F".equals(obj.getString("processCode"))) {
									//审批通过时保存到本地数据库
									if ("agree".equals(obj.getString("result")) && "finish".equals(obj.getString("type"))) {
										Map<String, String> result = talkUtil.getProcessMessage(obj.getString("processInstanceId"),talkUtil.getToken(wbcxid));
										MultiValueMap<String, String> qj_paramMap = new LinkedMultiValueMap<>();
										qj_paramMap.add("data", result.get("body"));
										qj_paramMap.add("processInstanceId", obj.getString("processInstanceId"));
										qj_paramMap.add("wbcxid",wbcxid);
										RestTemplate restTemplate = new RestTemplate();
										Map<String, Object> t_result = restTemplate.postForObject(applicationurl + ddsplx.getCskz2(), qj_paramMap, Map.class);
										if (t_result != null) {
											log.error(",结果:" + t_result.get("status"));
										}
									}
								} else {
									dealInstanceCallBack(obj, ddspbcbj, ddspglDto, ddxxglDtos, callbackurl, ddsplx.getCsmc(),"0");
								}
							}
						} else {
							log.info("其他：" + obj.getString("processCode"));
						}
					}
				}
			}
		}
	}

	
	/**
     * 将钉钉审批信息保存至钉钉分布式管理表
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean saveDistributedMsg(DdfbsglDto ddfbsglDto) {
		DdfbsglDto ddfbsglDto_t=dao.getDtoById(ddfbsglDto.getProcessinstanceid());
		if(ddfbsglDto_t==null) {
			return insert(ddfbsglDto);
		}else {
			ddfbsglDto_t.setFwqm(ddfbsglDto.getFwqm());
			ddfbsglDto_t.setWbcxid(ddfbsglDto.getWbcxid());
			return update(ddfbsglDto);
		}
	}

	/**
	 * @Description: 如果只传入startTime参数，要求时间距离当前时间不能超过120天，endTime不传则默认取当前时间。
	 * 如果传入startTime参数和endTime参数，要求时间范围不能超过120天，同时startTime时间距当前时间不能超过365天。
	 * 批量获取的实例ID个数（循环获取），最多不能超过10000个。
	 * @param map
	 * @return void
	 * @Author: 郭祥杰
	 * @Date: 2025/6/5 14:43
	 */
	public void obtainApprovalData(Map<String, String> map) throws Exception{
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime oneMonthAgo = now.minusMonths(1);
		long startTime = now.toInstant().toEpochMilli();
		long endTime = oneMonthAgo.toInstant().toEpochMilli();
		if(StringUtil.isNotBlank(map.get("startTime")) && StringUtil.isNotBlank(map.get("endTime"))) {
			ZonedDateTime startTimeZoned = LocalDateTime.parse(map.get("startTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.systemDefault());
			ZonedDateTime endTimeZoned = LocalDateTime.parse(map.get("endTime"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.systemDefault());
			startTime = startTimeZoned.toInstant().toEpochMilli();
			endTime = endTimeZoned.toInstant().toEpochMilli();
		}
		String wbcxidString = map.get("wbcxid");
		if(StringUtil.isBlank(map.get("wbcxid"))) {
			wbcxidString = "matridxOA";
		}
		Map<String, List<JcsjDto>> jcsjMap = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[] { BasicDataTypeEnum.APPROVAL_SYNCHRONIZATION_PERMISSIONS });
		List<JcsjDto> jcsjList = jcsjMap.get(BasicDataTypeEnum.APPROVAL_SYNCHRONIZATION_PERMISSIONS.getCode());
		if (jcsjList != null && !jcsjList.isEmpty()) {
			String token = talkUtil.getToken(wbcxidString);
			for (JcsjDto jcsjDto : jcsjList) {
				getProcessInstanceId(token,jcsjDto.getCsmc(),startTime,endTime,0L);
			}
		}
	}

	/**
	 * @Description: 获取id,根据id获取详情保存
	 * @param token
	 * @param processCode
	 * @param startTime
	 * @param endTime
	 * @param nextToken
	 * @return void
	 * @Author: 郭祥杰
	 * @Date: 2025/6/5 16:40
	 */
	private void getProcessInstanceId(String token,String processCode,long startTime,long endTime,long nextToken) throws Exception{
		boolean result = false;
		Map<String,Object> processInstanceMap = dingTalkUtil.getProcessInstanceIdList(token, processCode,startTime,endTime,nextToken);
		if(processInstanceMap!=null && processInstanceMap.get("list")!=null && StringUtil.isNotBlank(processInstanceMap.get("list").toString())) {
			List<String> processInstanceIdList = JSON.parseArray(processInstanceMap.get("list").toString(), String.class);
			if(!CollectionUtils.isEmpty(processInstanceIdList)){
				DdspjlDto ddspjlDto = new DdspjlDto();
				ddspjlDto.setIds(processInstanceIdList);
				List<String> processInstanceIds = ddspjlService.filtrateDdslid(ddspjlDto);
				if(!CollectionUtils.isEmpty(processInstanceIds)) {
					List<FjcfbDto> addFjcfbDto = new ArrayList<>();
					List<DdspjlDto> addDdspjlDto = new ArrayList<>();
					for (String processInstanceId : processInstanceIds) {
						Map<String,Object> dingMap = dingTalkUtil.processInstances(processInstanceId, token);
						if(!dingMap.isEmpty()) {
							dingMap.put("processInstanceId",processInstanceId);
							dingMap.put("processCode",processCode);
							List<FjcfbDto> fjcfbDtos = saveFiles(dingMap,token,processInstanceId);
							addFjcfbDto.addAll(fjcfbDtos);
							DdspjlDto addDto = generatedRecord(dingMap);
							addDdspjlDto.add(addDto);
						}
					}
					if(!CollectionUtils.isEmpty(addDdspjlDto)){
						result = ddspjlService.insertList(addDdspjlDto);
						if(!result){
							log.error("保存钉钉审批记录失败!"+JSON.toJSONString(addDdspjlDto));
						}
					}
					if(!CollectionUtils.isEmpty(addFjcfbDto)) {
						result = fjcfbService.batchInsertFjcfb(addFjcfbDto);
						if(!result){
							log.error("保存钉钉审批附件失败!"+JSON.toJSONString(addFjcfbDto));
						}
					}
				}
			}
		}
		if(processInstanceMap!=null && processInstanceMap.get("nextToken")!=null && StringUtil.isNotBlank(processInstanceMap.get("nextToken").toString())){
			getProcessInstanceId(token,processCode,startTime,endTime,Long.parseLong(processInstanceMap.get("nextToken").toString()));
		}
	}
}

