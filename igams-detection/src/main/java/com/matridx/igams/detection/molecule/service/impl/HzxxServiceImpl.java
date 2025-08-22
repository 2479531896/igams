package com.matridx.igams.detection.molecule.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.detection.molecule.dao.entities.HzxxDto;
import com.matridx.igams.detection.molecule.dao.entities.HzxxModel;
import com.matridx.igams.detection.molecule.dao.post.IHzxxDao;
import com.matridx.igams.detection.molecule.service.svcinterface.IHzxxService;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class HzxxServiceImpl extends BaseBasicServiceImpl<HzxxDto, HzxxModel, IHzxxDao> implements IHzxxService{
	@Autowired
	IFjcfbService fjcfbService;
	@Value("${matridx.aliyunSms.accessKeyId:'abc'}")
	private String accessKeyId;
	@Value("${matridx.aliyunSms.accessSecret:'abc'}")
	private String accessSecret;
	@Value("${matridx.aliyunSms.signName:'abc'}")
	private String signName;
	@Value("${matridx.aliyunSms.templateCode:'abc'}")
	private String templateCode;
	@Override
	public HzxxDto getHzxxByZjh(HzxxDto hzxxDto) {
		// TODO Auto-generated method stub
		return dao.getHzxxByZjh(hzxxDto);
	}
	/*根据微信id查询患者信息*/
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public List<HzxxDto> getHzxxListByWxid(HzxxDto hzxxDto) {
		return dao.getHzxxListByWxid(hzxxDto);
	}
	/*根据证件号码查询患者信息*/
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public HzxxDto getHzxxDtoByZjh(HzxxDto hzxxDto) {
		return dao.getHzxxDtoByZjh(hzxxDto);
	}
	/*根据患者id查询患者信息*/
	@Override
	@Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
	public HzxxDto getHzxxDtoByHzid(HzxxDto hzxxDto) {
		return dao.getHzxxDtoByHzid(hzxxDto);
	}
	/*根据患者id查询预约检测项目*/
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public List<String> getJcxmListByHzid(HzxxDto hzxxDto) {
		List<HzxxDto> hzxxDtoList = dao.getFzjcxmListByHzid(hzxxDto);
		List<String> jcxmList = new ArrayList<>();
		for (HzxxDto dto : hzxxDtoList) {
			jcxmList.add(dto.getJcxm());
		}
		return jcxmList;
	}
	/*预约新增患者信息*/
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDetectionAppointmentHzxx(HzxxDto hzxxDto){
		return dao.insertDetectionAppointmentHzxx(hzxxDto);
	}

	/*预约修改患者信息*/
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateDetectionAppointmentHzxx(HzxxDto hzxxDto){
		return dao.updateDetectionAppointmentHzxx(hzxxDto);
	}

	/**
	 * 通过wxid查询患者信息
	 */
	public HzxxDto getDtoListByWxid(String wxid){
		return dao.getDtoListByWxid(wxid);
	}
	/**
	 * 通过sj查询患者信息
	 */
	public List<HzxxDto> getDtoListBySj(String sj){
		return dao.getDtoListBySj(sj);
	}
	/**
	 * 查询验证码
	 */
	public HzxxDto getCode(HzxxDto hzxxDto){
		return dao.getCode(hzxxDto);
	}

	/**
	 * 修改wxid
	 */
	public int updateWxid(HzxxDto hzxxDto){
		return dao.updateWxid(hzxxDto);
	}

	/**
	 * 修改患者信息
	 */
	@Override
	public boolean updatePatient(HzxxDto hzxxDto) {
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
	 */
	public boolean sendCode(HzxxDto hzxxDto){
		// TODO Auto-generated method stub
		//获取四位验证码
		hzxxDto.setYzm(RandomStringUtils.random(4, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"));
		hzxxDto.setFssj(String.valueOf(System.currentTimeMillis()));
		boolean result = updatePatient(hzxxDto);
		if(!result)
			return false;
		result = sendSms(hzxxDto.getSj(), signName, templateCode, hzxxDto.getYzm());
		return result;
	}

	/**
	 * 发送手机短信
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
		} catch (ClientException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 查询患者信息详情
	 */
	public List<HzxxDto> viewPatientDetails(HzxxDto hzxxDto){
		return dao.viewPatientDetails(hzxxDto);
	}
	/**
	 * 查询证件类型
	 */
	public List<HzxxDto> getZjlx(){
		return dao.getZjlx();
	}
	/**
	 * 转义证件类型
	 */
	public HzxxDto escapeZjlx(HzxxDto hzxxDto){
		return dao.escapeZjlx(hzxxDto);
	}
	/**
	 * 修改新冠患者信息
	 */
	public boolean updateCovidPatient(HzxxDto hzxxDto){
		return dao.updateCovidPatient(hzxxDto);
	}
	/**
	 * 获取患者信息
	 */
	public HzxxDto getHzxxByHzid(HzxxDto hzxxDto){
		return dao.getHzxxByHzid(hzxxDto);
	}
	/**
	 * 删除新冠患者信息
	 */
	public boolean deleteCovidPatient(HzxxDto hzxxDto){
		return dao.deleteCovidPatient(hzxxDto);
	}

	/*根据sfzh查询患者信息（是否有同一天）*/
	public List<HzxxDto> getHzxxDtoByZjhAndDate(HzxxDto hzxxDto){
		return dao.getHzxxDtoByZjhAndDate(hzxxDto);
	}

	/**
	 * 更新患者信息表的是否确认字段
	 */
	public boolean updateSfqr(HzxxDto hzxxDto){
		return dao.updateSfqr(hzxxDto);
	}
}
