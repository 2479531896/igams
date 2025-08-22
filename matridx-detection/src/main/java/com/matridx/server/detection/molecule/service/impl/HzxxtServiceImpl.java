package com.matridx.server.detection.molecule.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IDxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.server.detection.molecule.dao.entities.FzjcxmDto;
import com.matridx.server.detection.molecule.dao.entities.FzjcxxDto;
import com.matridx.server.detection.molecule.enums.DetMQTypeEnum;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxmService;
import com.matridx.server.detection.molecule.service.svcinterface.IFzjcxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.detection.molecule.dao.entities.HzxxtDto;
import com.matridx.server.detection.molecule.dao.entities.HzxxtModel;
import com.matridx.server.detection.molecule.dao.post.IHzxxtDao;
import com.matridx.server.detection.molecule.service.svcinterface.IHzxxtService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HzxxtServiceImpl extends BaseBasicServiceImpl<HzxxtDto, HzxxtModel, IHzxxtDao> implements IHzxxtService{
	@Autowired
	IFjcfbService fjcfbService;
	@Value("${matridx.aliyunSms.accessKeyId:}")
	private String accessKeyId;
	@Value("${matridx.aliyunSms.accessSecret:}")
	private String accessSecret;
	@Value("${matridx.aliyunSms.signName:}")
	private String signName;
	@Value("${matridx.aliyunSms.signNameMatridx:}")
	private String signNameMatridx;
	@Value("${matridx.aliyunSms.templateCode:}")
	private String templateCode;
	@Value("${matridx.aliyunSms.templateCodeXgyy:}")
	private String templateCodeXgyy;
	@Value("${matridx.aliyunSms.templateCodeXglj:}")
	private String templateCodeXglj;
	@Autowired
	IFzjcxxService fzjcxxService;
	@Autowired
	IFzjcxmService fzjcxmService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IDxglService dxglService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;

	@Override
	public HzxxtDto getHzxxByZjh(HzxxtDto hzxxDto) {
		// TODO Auto-generated method stub
		return dao.getHzxxByZjh(hzxxDto);
	}

	/*根据微信id查询患者信息*/
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public List<HzxxtDto> getHzxxListByWxid(HzxxtDto hzxxDto) {
		return dao.getHzxxListByWxid(hzxxDto);
	}

	/*根据证件号码查询患者信息*/
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public List<HzxxtDto> getHzxxDtoByZjh(HzxxtDto hzxxDto) {
		return dao.getHzxxDtoByZjh(hzxxDto);
	}

	/*根据患者id查询患者信息*/
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public HzxxtDto getHzxxDtoByHzid(HzxxtDto hzxxDto) {
		return dao.getHzxxDtoByHzid(hzxxDto);
	}

	/*根据患者id查询预约检测项目*/
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public List<String> getJcxmListByHzid(HzxxtDto hzxxDto) {
		List<HzxxtDto> hzxxDtoList = dao.getFzjcxmListByHzid(hzxxDto);
		List<String> jcxmList = new ArrayList<>();
		for (HzxxtDto dto : hzxxDtoList) {
			jcxmList.add(dto.getJcxm());
		}
		return jcxmList;
	}

	/*预约新增患者信息*/
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDetectionAppointmentHzxx(HzxxtDto hzxxDto){
		return dao.insertDetectionAppointmentHzxx(hzxxDto);
	};

	/*预约修改患者信息*/
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateDetectionAppointmentHzxx(HzxxtDto hzxxDto){
		return dao.updateDetectionAppointmentHzxx(hzxxDto);
	};

	/**
	 * 通过sj查询患者信息
	 * @param sj
	 * @return
	 */
	public List<HzxxtDto> getDtoListBySj(String sj){
		return dao.getDtoListBySj(sj);
	}

	/**
	 * 查询验证码
	 * @param hzxxDto
	 * @return
	 */
	public HzxxtDto getCode(HzxxtDto hzxxDto){
		return dao.getCode(hzxxDto);
	}

	/**
	 * 修改wxid
	 * @param hzxxDto
	 * @return
	 */
	public int updateWxid(HzxxtDto hzxxDto){
		return dao.updateWxid(hzxxDto);
	}

	/**
	 * 修改患者信息
	 * @param hzxxDto
	 * @return
	 */
	@Override
	public boolean updatePatient(HzxxtDto hzxxDto) {
		// TODO Auto-generated method stub
		int result = dao.updatePatient(hzxxDto);
		if(result == 0)
			return false;
		if(hzxxDto.getFjids()!=null && hzxxDto.getFjids().size() > 0){
			for (int i = 0; i < hzxxDto.getFjids().size(); i++) {
				boolean saveFile = fjcfbService.save2RealFile(hzxxDto.getFjids().get(i),hzxxDto.getHzid());
				if(!saveFile)
					return false;
			}
		}
		return true;
	}

	/**
	 * 发送短信验证码
	 * @param hzxxDto
	 * @return
	 */
	public boolean sendCode(HzxxtDto hzxxDto){
		// TODO Auto-generated method stub
		//获取四位验证码
		hzxxDto.setYzm(RandomStringUtils.random(6, "0123456789"));
		boolean result = updatePatient(hzxxDto);
		if(!result)
			return false;
		DBEncrypt dbEncrypt=new DBEncrypt();
		hzxxDto.setSj(dbEncrypt.dCode(hzxxDto.getSj()));
		result = sendSms(hzxxDto.getSj(), signName, templateCode, hzxxDto.getYzm());
		if(!result)
			return false;
		return true;
	}

	/**
	 * 发送手机短信
	 * @param phoneNumbers
	 * @param TemplateParam
	 * @return
	 */
	public boolean sendSms(String phoneNumbers, String signName, String templateCode, String TemplateParam) {
		// TODO Auto-generated method stub
		DBEncrypt crypt = new DBEncrypt();
		DefaultProfile profile = DefaultProfile.getProfile("RegionId", crypt.dCode(accessKeyId), crypt.dCode(accessSecret));
		IAcsClient client = new DefaultAcsClient(profile);

		CommonRequest request = new CommonRequest();
		//无需替换
		request.setMethod(MethodType.POST);
		request.setDomain("dysmsapi.aliyuncs.com");
		request.setVersion("2017-05-25");
		request.setAction("SendSms");

		//必填:短信签名-可在短信控制台中找到，你在签名管理里的内容
		request.putQueryParameter("SignName", signName);
		//必填:待发送手机号
		request.putQueryParameter("PhoneNumbers", phoneNumbers);
		//必填:短信模板-可在短信控制台中找到，你模板管理里的模板编号
		request.putQueryParameter("TemplateCode", templateCode);
		//可选:模板中的变量替换JSON串,如模板内容为"您的验证码为${code}"时,此处的值为
		request.putQueryParameter("TemplateParam", "{\"code\":\""+TemplateParam+"\"}");
		try {
			CommonResponse response = client.getCommonResponse(request);
			System.out.println(response.getData());
		} catch (ServerException e) {
			e.printStackTrace();
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean updateCovidPatient(HzxxtDto hzxxDto) {
		// TODO Auto-generated method stub
		return dao.updateCovidPatient(hzxxDto);
	}

	/*查找当前微信号最近录入的一条预约信息*/
	public HzxxtDto getLastHzxxByWxid(HzxxtDto hzxxDto){
		return dao.getLastHzxxByWxid(hzxxDto);
	}

	//预约新增
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Map<String, Object> detectionAppointmentAdd(HzxxtDto hzxxDto){
		Map<String, Object> map=new HashMap<String, Object>();
		Map<String, Object> rabbitMap=new HashMap<String, Object>();
		//设置检测对象类型为人
		List<JcsjDto> jcdxlxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_OBJECT.getCode());
		String jcdxlx = "";
		for (JcsjDto jcsj : jcdxlxs) {
			if ("R".equals(jcsj.getCsdm())){
				jcdxlx = jcsj.getCsid();
			}
		}
		//设置检测类型为新冠
		List<JcsjDto> jclxs = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MOLECULAR_DETECTION.getCode());
		String jclx = "";
		for (JcsjDto jcsj : jclxs) {
			if ("TYPE_COVID".equals(jcsj.getCsdm())){
				jclx = jcsj.getCsid();
			}
		}
		if (StringUtil.isNotBlank(hzxxDto.getYyjcrq())){
			//若是新的模板，预约日期范围为2022-01-14 15:00~16:00
			String yyjcrqfw = hzxxDto.getYyjcrq();
			rabbitMap.put("yyjcrqfw",yyjcrqfw);
			//将传递过来的预约检测日期分解2022-01-14 15:00~16:00 转成2022-01-14 15:00，即去掉~后半部分存入数据库
			hzxxDto.setYyjcrq(hzxxDto.getYyjcrq().split("~")[0]);
		}
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		//证件号和手机号进行加密存放
		DBEncrypt p = new DBEncrypt();
		hzxxDto.setSj(p.eCode(hzxxDto.getSj()));
		hzxxDto.setZjh(p.eCode(hzxxDto.getZjh()));
		//设置患者id
		String hzid = StringUtil.generateUUID();
		boolean isSuccess = true;
		//查询证件号码、证件类型是否存在
		HzxxtDto hzxx = getHzxxByZjh(hzxxDto);
		/*
		存在：下一步操作；
		不存在：新增 患者信息、分子检测信息、分子检测项目；
		 */
		if (hzxx != null){
			hzxxDto.setHzid(hzxx.getHzid());
			FzjcxxDto fzjcxxDto1 =new FzjcxxDto();
			fzjcxxDto1.setLrry(hzxxDto.getWxid());
			fzjcxxDto1.setHzid(hzxx.getHzid());
			fzjcxxDto1.setYyjcrq(hzxxDto.getYyjcrq());
			List<FzjcxxDto> fzjcxxDtos = fzjcxxService.beforeDayList(fzjcxxDto1);
			if (null != fzjcxxDtos && fzjcxxDtos.size()>0){
				map.put("message", "该用户已有预约相同的检测项目，<br/>且未检测！");
				map.put("status","fail");
				return map;
			}
			//若 证件类型、证件号存在：下一步操作
			/*//查询证件号码是否有未检测的预约
			List<HzxxtDto> lshzxxList = getHzxxDtoByZjh(hzxxDto);
			*//*
			当前新增预约项目与未检测的预约项目有冲突：不新增，提示消息
			当前新增预约项目与未检测的预约项目无冲突：下一步操作；
			 *//*
			if(lshzxxList !=null && lshzxxList.size()>0){
				for (HzxxtDto lshzxx : lshzxxList) {
					String[] lsjcxmids = lshzxx.getJcxmid().split(",");
					String[] hzxxjcxmids = hzxxDto.getJcxmid().split(",");
					for (String lsjcxmid : lsjcxmids) {
						for (String hzxxjcxmid : hzxxjcxmids) {
							if (lsjcxmid.equals(hzxxjcxmid)){
								//当前新增预约项目与未检测的预约项目有冲突：不执行操作；提示消息
								map.put("message", "您已有预约相同的检测项目，<br/>且未检测！");
								map.put("status","fail");
								return map;
							}
						}
					}
				}
				//当前新增预约项目与未检测的预约项目无冲突：下一步操作：
				*//*
				姓名不一致:不执行操作；提示消息
				姓名一致:修改患者信息表（手机、地址），新增分子检测信息，新增分子检测项目
				 *//*
				if (!hzxx.getXm().equals(hzxxDto.getXm())) {
					//姓名不一致:下一步操作
					*//*
					是否确认为1：不执行操作；提示信息
					是否确认为0：修改患者信息表，新增分子检测信息，新增分子检测项目；
					 *//*
					if ("1".equals(hzxx.getSfqr())) {
						//若 是否确认为1：不执行操作；提示信息
						map.put("message", "姓名与证件号码不匹配<br/>请确认后提交！");
						map.put("status", "fail");
						return map;
					}else {
						//是否确认为0：修改患者信息表，新增分子检测信息，新增分子检测项目；
						hzxxDto.setXgsj(dateformat.format(date));
						isSuccess = updateDetectionAppointmentHzxx(hzxxDto);
						rabbitMap.put("hzxxDto",hzxxDto);
						if (isSuccess) {
							FzjcxxDto fzjcxxDto = new FzjcxxDto();
							fzjcxxDto.setHzid(hzxx.getHzid());
							fzjcxxDto.setFzjcid(StringUtil.generateUUID());
							fzjcxxDto.setWxid(hzxxDto.getWxid());
							fzjcxxDto.setYyjcrq(hzxxDto.getYyjcrq());
							fzjcxxDto.setJcxmmc(hzxxDto.getJcxmmc());
							fzjcxxDto.setJcxmid(hzxxDto.getJcxmid());
							fzjcxxDto.setCyd(hzxxDto.getCyd());
							fzjcxxDto.setFkje(hzxxDto.getAmount());
							fzjcxxDto.setLrsj(dateformat.format(date));
							fzjcxxDto.setPt("杰毅医检公众号");
							fzjcxxDto.setJcdxlx(jcdxlx!=null?jcdxlx:"");
							map.put("fzjcid",fzjcxxDto.getFzjcid());
							isSuccess = fzjcxxService.insertDetectionAppointmentFzjcxx(fzjcxxDto);
							rabbitMap.put("fzjcxxDto",fzjcxxDto);
							if (isSuccess) {
								String[] jcxmidList = fzjcxxDto.getJcxmid().split(",");
								for (String jcxmid : jcxmidList) {
									FzjcxmDto fzjcxmDto = new FzjcxmDto();
									fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
									fzjcxmDto.setFzjcxmid(jcxmid);
									fzjcxmDto.setFzxmid(StringUtil.generateUUID());
									isSuccess = fzjcxmService.insertFzjcxmDto(fzjcxmDto);
									rabbitMap.put("fzjcxmDto",fzjcxmDto);
									amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.YYXX_MOD.getCode()+ JSONObject.toJSONString(rabbitMap));
									if (!isSuccess) {
										map.put("message", "分子检测项目表新增失败！");
										map.put("status", "fail");
										return map;
									} else {
										map.put("message", "提交成功！");
										map.put("status", "success");
										//设置采样点名称（若没有匹配到，则设置为默认采样点名称）
										String cydmc = "";
										String mrcydmc = "";
										List<JcsjDto> cyds = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
										for (JcsjDto cydDto : cyds) {
											if (cydDto.getCsid().equals(fzjcxxDto.getCyd())){
												cydmc = cydDto.getCsmc();
											}
											if ("1".equals(cydDto.getSfmr())){
												mrcydmc = cydDto.getCsmc();
											}
										}
										if (StringUtil.isBlank(cydmc)){
											cydmc = mrcydmc;
										}
										rabbitMap.put("cydmc",cydmc);
										//发送预约成功短信
										dxglService.sendReplaceMsg(hzxxDto.getSj(),"ICOMM_XG00005", rabbitMap, signNameMatridx, templateCodeXgyy);
										return map;
									}
								}
							} else {
								map.put("message", "分子检测信息表新增失败！");
								map.put("status", "fail");
								return map;
							}
						} else {
							map.put("message", "患者信息表修改失败！");
							map.put("status", "fail");
							return map;
						}
					}
				}else {
					//若 姓名一致 修改患者信息表（手机、地址），新增分子检测信息，新增分子检测项目
					hzxxDto.setXgsj(dateformat.format(date));
					isSuccess = updateDetectionAppointmentHzxx(hzxxDto);
					rabbitMap.put("hzxxDto",hzxxDto);
					if (isSuccess) {
						FzjcxxDto fzjcxxDto = new FzjcxxDto();
						fzjcxxDto.setHzid(hzxx.getHzid());
						fzjcxxDto.setFzjcid(StringUtil.generateUUID());
						fzjcxxDto.setWxid(hzxxDto.getWxid());
						fzjcxxDto.setYyjcrq(hzxxDto.getYyjcrq());
						fzjcxxDto.setJcxmmc(hzxxDto.getJcxmmc());
						fzjcxxDto.setJcxmid(hzxxDto.getJcxmid());
						fzjcxxDto.setCyd(hzxxDto.getCyd());
						fzjcxxDto.setFkje(hzxxDto.getAmount());
						fzjcxxDto.setLrsj(dateformat.format(date));
						fzjcxxDto.setPt("杰毅医检公众号");
						fzjcxxDto.setJcdxlx(jcdxlx!=null?jcdxlx:"");
						map.put("fzjcid",fzjcxxDto.getFzjcid());
						isSuccess = fzjcxxService.insertDetectionAppointmentFzjcxx(fzjcxxDto);
						rabbitMap.put("fzjcxxDto",fzjcxxDto);
						if (isSuccess) {
							String[] jcxmidList = fzjcxxDto.getJcxmid().split(",");
							for (String jcxmid : jcxmidList) {
								FzjcxmDto fzjcxmDto = new FzjcxmDto();
								fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
								fzjcxmDto.setFzjcxmid(jcxmid);
								fzjcxmDto.setFzxmid(StringUtil.generateUUID());
								isSuccess = fzjcxmService.insertFzjcxmDto(fzjcxmDto);
								rabbitMap.put("fzjcxmDto",fzjcxmDto);
								amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.YYXX_MOD.getCode()+ JSONObject.toJSONString(rabbitMap));
								if (!isSuccess) {
									map.put("message", "分子检测项目表新增失败！");
									map.put("status", "fail");
									return map;
								} else {
									map.put("message", "提交成功！");
									map.put("status", "success");
									//设置采样点名称（若没有匹配到，则设置为默认采样点名称）
									String cydmc = "";
									String mrcydmc = "";
									List<JcsjDto> cyds = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
									for (JcsjDto cydDto : cyds) {
										if (cydDto.getCsid().equals(fzjcxxDto.getCyd())){
											cydmc = cydDto.getCsmc();
										}
										if ("1".equals(cydDto.getSfmr())){
											mrcydmc = cydDto.getCsmc();
										}
									}
									if (StringUtil.isBlank(cydmc)){
										cydmc = mrcydmc;
									}
									rabbitMap.put("cydmc",cydmc);
									//发送预约成功短信
									dxglService.sendReplaceMsg(hzxxDto.getSj(),"ICOMM_XG00005", rabbitMap, signNameMatridx, templateCodeXgyy);
									return map;
								}
							}
						} else {
							map.put("message", "分子检测信息表新增失败！");
							map.put("status", "fail");
							return map;
						}
					} else {
						map.put("message", "患者信息表修改失败！");
						map.put("status", "fail");
						return map;
					}
				}
			}
			//该证件类型、证件号下不存在未检测的预约*/
			/*
			姓名一致:修改患者信息表（手机、地址），新增分子检测信息，新增分子检测项目
			姓名不一致:下一步操作
			 */
			if (!hzxx.getXm().equals(hzxxDto.getXm())) {
				//姓名不一致:下一步操作
				/*
				是否确认为1：不执行操作；提示信息
				是否确认为0：修改患者信息表，新增分子检测信息，新增分子检测项目；
				 */
				if ("1".equals(hzxx.getSfqr())) {
					//若 是否确认为1：不执行操作；提示信息
					map.put("message", "姓名与证件号码不匹配<br/>请确认后提交！");
					map.put("status", "fail");
					return map;
				}else {
					//是否确认为0：修改患者信息表，新增分子检测信息，新增分子检测项目；
					hzxxDto.setXgsj(dateformat.format(date));
					isSuccess = updateDetectionAppointmentHzxx(hzxxDto);
					rabbitMap.put("hzxxDto",hzxxDto);
					if (isSuccess) {
						FzjcxxDto fzjcxxDto = new FzjcxxDto();
						fzjcxxDto.setHzid(hzxx.getHzid());
						fzjcxxDto.setFzjcid(StringUtil.generateUUID());
						fzjcxxDto.setWxid(hzxxDto.getWxid());
						fzjcxxDto.setYyjcrq(hzxxDto.getYyjcrq());
						fzjcxxDto.setJcxmmc(hzxxDto.getJcxmmc());
						fzjcxxDto.setJcxmid(hzxxDto.getJcxmid());
						fzjcxxDto.setCyd(hzxxDto.getCyd());
						fzjcxxDto.setFkje(hzxxDto.getAmount());
						fzjcxxDto.setLrsj(dateformat.format(date));
						fzjcxxDto.setPt("杰毅医检公众号");
						fzjcxxDto.setJcdxlx(jcdxlx!=null?jcdxlx:"");
						fzjcxxDto.setJclx(jclx!=null?jclx:"");
						map.put("fzjcid",fzjcxxDto.getFzjcid());
						isSuccess = fzjcxxService.insertDetectionAppointmentFzjcxx(fzjcxxDto);
						rabbitMap.put("fzjcxxDto",fzjcxxDto);
						if (isSuccess) {
							String[] jcxmidList = fzjcxxDto.getJcxmid().split(",");
							for (String jcxmid : jcxmidList) {
								FzjcxmDto fzjcxmDto = new FzjcxmDto();
								fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
								fzjcxmDto.setFzjcxmid(jcxmid);
								fzjcxmDto.setFzxmid(StringUtil.generateUUID());
								isSuccess = fzjcxmService.insertFzjcxmDto(fzjcxmDto);
								rabbitMap.put("fzjcxmDto",fzjcxmDto);
								amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.YYXX_MOD.getCode()+ JSONObject.toJSONString(rabbitMap));
								if (!isSuccess) {
									map.put("message", "分子检测项目表新增失败！");
									map.put("status", "fail");
									return map;
								} else {
									map.put("message", "提交成功！");
									map.put("status", "success");
									//设置采样点名称（若没有匹配到，则设置为默认采样点名称）
									String cydmc = "";
									String mrcydmc = "";
									List<JcsjDto> cyds = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
									for (JcsjDto cydDto : cyds) {
										if (cydDto.getCsid().equals(fzjcxxDto.getCyd())){
											cydmc = cydDto.getCsmc();
										}
										if ("1".equals(cydDto.getSfmr())){
											mrcydmc = cydDto.getCsmc();
										}
									}
									if (StringUtil.isBlank(cydmc)){
										cydmc = mrcydmc;
									}
									rabbitMap.put("cydmc",cydmc);
									//发送预约成功短信
									dxglService.sendReplaceMsg(hzxxDto.getSj(),"ICOMM_XG00005", rabbitMap, signNameMatridx, templateCodeXgyy);
									return map;
								}
							}
						} else {
							map.put("message", "分子检测信息表新增失败！");
							map.put("status", "fail");
							return map;
						}
					} else {
						map.put("message", "患者信息表修改失败！");
						map.put("status", "fail");
						return map;
					}
				}
			}else {
				//若 姓名一致 修改患者信息表（手机、地址），新增分子检测信息，新增分子检测项目
				hzxxDto.setXgsj(dateformat.format(date));
				isSuccess = updateDetectionAppointmentHzxx(hzxxDto);
				rabbitMap.put("hzxxDto",hzxxDto);
				if (isSuccess) {
					FzjcxxDto fzjcxxDto = new FzjcxxDto();
					fzjcxxDto.setHzid(hzxx.getHzid());
					fzjcxxDto.setFzjcid(StringUtil.generateUUID());
					fzjcxxDto.setWxid(hzxxDto.getWxid());
					fzjcxxDto.setYyjcrq(hzxxDto.getYyjcrq());
					fzjcxxDto.setJcxmmc(hzxxDto.getJcxmmc());
					fzjcxxDto.setJcxmid(hzxxDto.getJcxmid());
					fzjcxxDto.setCyd(hzxxDto.getCyd());
					fzjcxxDto.setLrsj(dateformat.format(date));
					fzjcxxDto.setFkje(hzxxDto.getAmount());
					fzjcxxDto.setPt("杰毅医检公众号");
					fzjcxxDto.setJcdxlx(jcdxlx!=null?jcdxlx:"");
					fzjcxxDto.setJclx(jclx!=null?jclx:"");
					map.put("fzjcid",fzjcxxDto.getFzjcid());
					isSuccess = fzjcxxService.insertDetectionAppointmentFzjcxx(fzjcxxDto);
					rabbitMap.put("fzjcxxDto",fzjcxxDto);
					if (isSuccess) {
						String[] jcxmidList = fzjcxxDto.getJcxmid().split(",");
						for (String jcxmid : jcxmidList) {
							FzjcxmDto fzjcxmDto = new FzjcxmDto();
							fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
							fzjcxmDto.setFzjcxmid(jcxmid);
							fzjcxmDto.setFzxmid(StringUtil.generateUUID());
							isSuccess = fzjcxmService.insertFzjcxmDto(fzjcxmDto);
							rabbitMap.put("fzjcxmDto",fzjcxmDto);
							amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.YYXX_MOD.getCode()+ JSONObject.toJSONString(rabbitMap));
							if (!isSuccess) {
								map.put("message", "分子检测项目表新增失败！");
								map.put("status", "fail");
								return map;
							} else {
								map.put("message", "提交成功！");
								map.put("status", "success");
								//设置采样点名称（若没有匹配到，则设置为默认采样点名称）
								String cydmc = "";
								String mrcydmc = "";
								List<JcsjDto> cyds = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
								for (JcsjDto cydDto : cyds) {
									if (cydDto.getCsid().equals(fzjcxxDto.getCyd())){
										cydmc = cydDto.getCsmc();
									}
									if ("1".equals(cydDto.getSfmr())){
										mrcydmc = cydDto.getCsmc();
									}
								}
								if (StringUtil.isBlank(cydmc)){
									cydmc = mrcydmc;
								}
								rabbitMap.put("cydmc",cydmc);
								//发送预约成功短信
								dxglService.sendReplaceMsg(hzxxDto.getSj(),"ICOMM_XG00005", rabbitMap, signNameMatridx, templateCodeXgyy);
								return map;
							}
						}
					} else {
						map.put("message", "分子检测信息表新增失败！");
						map.put("status", "fail");
						return map;
					}
				} else {
					map.put("message", "患者信息表修改失败！");
					map.put("status", "fail");
					return map;
				}
			}
		}else {
			//若 证件类型、证件号 不存在，新增患者信息表，新增分子检测信息表、新增分子检测项目表
			hzxxDto.setHzid(hzid);
			hzxxDto.setLrsj(dateformat.format(date));
			isSuccess = insertDetectionAppointmentHzxx(hzxxDto);
			rabbitMap.put("hzxxDto",hzxxDto);
			//如果新增患者信息成功，则往分子检测信息表中增加数据
			if (isSuccess){
				FzjcxxDto fzjcxxDto = new FzjcxxDto();
				fzjcxxDto.setHzid(hzxxDto.getHzid());
				fzjcxxDto.setFzjcid(StringUtil.generateUUID());
				fzjcxxDto.setWxid(hzxxDto.getWxid());
				fzjcxxDto.setYyjcrq(hzxxDto.getYyjcrq());
				fzjcxxDto.setJcxmmc(hzxxDto.getJcxmmc());
				fzjcxxDto.setJcxmid(hzxxDto.getJcxmid());
				fzjcxxDto.setCyd(hzxxDto.getCyd());
				fzjcxxDto.setLrsj(dateformat.format(date));
				fzjcxxDto.setFkje(hzxxDto.getAmount());
				fzjcxxDto.setPt("杰毅医检公众号");
				fzjcxxDto.setJcdxlx(jcdxlx!=null?jcdxlx:"");
				fzjcxxDto.setJclx(jclx!=null?jclx:"");
				map.put("fzjcid",fzjcxxDto.getFzjcid());
				isSuccess = fzjcxxService.insertDetectionAppointmentFzjcxx(fzjcxxDto);
				rabbitMap.put("fzjcxxDto",fzjcxxDto);
				if (isSuccess){
					String[] jcxmidList = fzjcxxDto.getJcxmid().split(",");
					for (String jcxmid : jcxmidList) {
						FzjcxmDto fzjcxmDto = new FzjcxmDto();
						fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
						fzjcxmDto.setFzjcxmid(jcxmid);
						fzjcxmDto.setFzxmid(StringUtil.generateUUID());
						isSuccess = fzjcxmService.insertFzjcxmDto(fzjcxmDto);
						rabbitMap.put("fzjcxmDto",fzjcxmDto);
						amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.YYXX_ADD.getCode()+ JSONObject.toJSONString(rabbitMap));
						if (!isSuccess){
							map.put("message","分子检测项目表新增失败！");
							map.put("status", "fail");
							return map;
						}else {
							map.put("message","提交成功！");
							map.put("status", "success");
							//设置采样点名称（若没有匹配到，则设置为默认采样点名称）
							String cydmc = "";
							String mrcydmc = "";
							List<JcsjDto> cyds = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
							for (JcsjDto cydDto : cyds) {
								if (cydDto.getCsid().equals(fzjcxxDto.getCyd())){
									cydmc = cydDto.getCsmc();
								}
								if ("1".equals(cydDto.getSfmr())){
									mrcydmc = cydDto.getCsmc();
								}
							}
							if (StringUtil.isBlank(cydmc)){
								cydmc = mrcydmc;
							}
							rabbitMap.put("cydmc",cydmc);
							//发送预约成功短信
							dxglService.sendReplaceMsg(hzxxDto.getSj(),"ICOMM_XG00005", rabbitMap, signNameMatridx, templateCodeXgyy);
							return map;
						}
					}
				}else {
					map.put("message","分子检测信息表新增失败！");
					map.put("status", "fail");
					return map;
				}
			}else {
				map.put("message","患者信息表新增失败！");
				map.put("status", "fail");
				return map;
			}
		}
		return map;
	}

	//预约修改
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Map<String, Object> detectionAppointmentMod(HzxxtDto hzxxDto){
		Map<String, Object> map=new HashMap<String, Object>();
		Map<String, Object> rabbitMap=new HashMap<String, Object>();
		if (StringUtil.isNotBlank(hzxxDto.getYyjcrq())){
			//若是新的模板，预约日期范围为2022-01-14 15:00~16:00
			String yyjcrqfw = hzxxDto.getYyjcrq();
			rabbitMap.put("yyjcrqfw",yyjcrqfw);
			//将传递过来的预约检测日期分解2022-01-14 15:00~16:00 转成2022-01-14 15:00，即去掉~后半部分存入数据库
			hzxxDto.setYyjcrq(hzxxDto.getYyjcrq().split("~")[0]);
		}
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		boolean isSuccess = false;
		//证件号和手机号进行加密存放
		DBEncrypt p = new DBEncrypt();
		hzxxDto.setSj(p.eCode(hzxxDto.getSj()));
		hzxxDto.setZjh(p.eCode(hzxxDto.getZjh()));
		HzxxtDto hzxx = getHzxxDtoByHzid(hzxxDto);
		hzxxDto.setXgsj(dateformat.format(date));
		isSuccess = updateDetectionAppointmentHzxx(hzxxDto);
		rabbitMap.put("hzxxDto",hzxxDto);
		boolean sendFlag = false;
		if (hzxx.getYyjcrq()!=hzxxDto.getYyjcrq()){
			sendFlag = true;
		}
		if (isSuccess) {
			FzjcxxDto fzjcxxDto = new FzjcxxDto();
			fzjcxxDto.setHzid(hzxx.getHzid());
			fzjcxxDto.setFzjcid(hzxx.getFzjcid());
			fzjcxxDto.setWxid(hzxxDto.getWxid());
			fzjcxxDto.setYyjcrq(hzxxDto.getYyjcrq());
			fzjcxxDto.setJcxmmc(hzxxDto.getJcxmmc());
			fzjcxxDto.setJcxmid(hzxxDto.getJcxmid());
			fzjcxxDto.setCyd(hzxxDto.getCyd());
			fzjcxxDto.setXgsj(dateformat.format(date));
			isSuccess = fzjcxxService.updateDetectionAppointmentFzjcxx(fzjcxxDto);
			rabbitMap.put("fzjcxxDto",fzjcxxDto);
			if (isSuccess){
				FzjcxmDto fzjcxmDto_t = new FzjcxmDto();
				fzjcxmDto_t.setFzjcid(fzjcxxDto.getFzjcid());
				isSuccess=fzjcxmService.delFzjcxmByFzjcid(fzjcxmDto_t);
				rabbitMap.put("fzjcxmDto_t",fzjcxmDto_t);
				if (isSuccess){
					String[] jcxmidList = fzjcxxDto.getJcxmid().split(",");
					for (String jcxmid : jcxmidList) {
						FzjcxmDto fzjcxmDto = new FzjcxmDto();
						fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
						fzjcxmDto.setFzjcxmid(jcxmid);
						fzjcxmDto.setFzxmid(StringUtil.generateUUID());
						isSuccess = fzjcxmService.insertFzjcxmDto(fzjcxmDto);
						rabbitMap.put("fzjcxmDto",fzjcxmDto);
						amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.YYXX_UPD.getCode()+ JSONObject.toJSONString(rabbitMap));
						if (!isSuccess){
							map.put("message","分子检测项目表新增失败！");
							map.put("status","fail");
							return map;
						}else {
							map.put("message","修改成功！");
							map.put("status","success");
							if (sendFlag){
								//设置采样点名称（若没有匹配到，则设置为默认采样点名称）
								String cydmc = "";
								String mrcydmc = "";
								List<JcsjDto> cyds = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COLLECT_SAMPLES.getCode());
								for (JcsjDto cydDto : cyds) {
									if (cydDto.getCsid().equals(fzjcxxDto.getCyd())){
										cydmc = cydDto.getCsmc();
									}
									if ("1".equals(cydDto.getSfmr())){
										mrcydmc = cydDto.getCsmc();
									}
								}
								if (StringUtil.isBlank(cydmc)){
									cydmc = mrcydmc;
								}
								rabbitMap.put("cydmc",cydmc);
								//发送预约修改短信
//								dxglService.sendReplaceMsg(hzxxDto.getSj(),"ICOMM_XG00005", rabbitMap, signNameMatridx, templateCodeXgyy);
							}
							return map;
						}
					}
				}else {
					map.put("message","分子检测项目表删除失败！");
					map.put("status","fail");
					return map;
				}
			}else {
				map.put("message", "分子检测信息表更新失败！");
				map.put("status","fail");
				return map;
			}
		} else {
			map.put("message", "患者信息表更新失败！");
			map.put("status","fail");
			return map;
		}
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?"修改成功！":"修改失败！");
		return map;
	}

	//取消预约
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Map<String, Object> cancleAppointment(HzxxtDto hzxxDto){
		Map<String, Object> map=new HashMap<String, Object>();
		Map<String, Object> rabbitMap=new HashMap<String, Object>();
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date=new Date();
		boolean isSuccess = false;
		HzxxtDto hzxx = getHzxxDtoByHzid(hzxxDto);
		FzjcxxDto fzjcxxDto = new FzjcxxDto();
		fzjcxxDto.setWxid(hzxxDto.getWxid());
		fzjcxxDto.setHzid(hzxxDto.getHzid());
		fzjcxxDto.setFzjcid(hzxx.getFzjcid());
		fzjcxxDto.setScsj(dateformat.format(date));
		isSuccess=fzjcxxService.cancelDetectionAppointment(fzjcxxDto);
		rabbitMap.put("fzjcxxDto",fzjcxxDto);
		if (isSuccess){
			/*去掉 取消预约 删除分子检测项目表的代码 2022-02-08*/
            /*FzjcxmDto fzjcxmDto = new FzjcxmDto();
            fzjcxmDto.setFzjcid(fzjcxxDto.getFzjcid());
            isSuccess=fzjcxmService.delFzjcxmByFzjcid(fzjcxmDto);
            rabbitMap.put("fzjcxmDto",fzjcxmDto);*/
			amqpTempl.convertAndSend("detection.exchange", DetMQTypeEnum.APPOINTMENT_DETECTION.getCode(), RabbitEnum.YYXX_DEL.getCode()+ JSONObject.toJSONString(rabbitMap));
		}
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?"取消预约成功！":"取消预约失败！");
		return map;
	}

	@Override
	public void updateSfqr(HzxxtDto hzxxDto) {
		dao.updateSfqr(hzxxDto);
	}

	/**
	 * 调用公用短信发送接口
	 * @param sjh
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Map<String,Object> sendMessageCode(String sjh){
		String yzm = RandomStringUtils.random(6, "0123456789");
		return dxglService.sendCode(sjh,yzm,signName,templateCode,"60","5");
	}

	/**
	 * 调用公用短信验证接口
	 * @param sjh 手机号
	 * @param yzm 验证码
	 * @param time 过期时间
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Map<String,Object> checkMessageCode(String sjh,String yzm,String time){
		return dxglService.checkCode(sjh,yzm,time);
	}

}
