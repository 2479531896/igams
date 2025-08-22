package com.matridx.server.detection.molecule.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.detection.molecule.dao.entities.HzxxtDto;
import com.matridx.server.detection.molecule.dao.entities.HzxxtModel;

import java.util.List;
import java.util.Map;

public interface IHzxxtService extends BaseBasicService<HzxxtDto, HzxxtModel>{

	/**
	 * 通过身份证号获取患者信息
	 * @param hzxxDto
	 * @return
	 */
	HzxxtDto getHzxxByZjh(HzxxtDto hzxxDto);
	/*根据微信id查询患者信息*/
	List<HzxxtDto> getHzxxListByWxid(HzxxtDto hzxxDto);
	/*根据证件号码查询患者信息*/
	List<HzxxtDto> getHzxxDtoByZjh(HzxxtDto hzxxDto);
	/*根据患者id查询患者信息*/
	HzxxtDto getHzxxDtoByHzid(HzxxtDto hzxxDto);
	/*根据患者id查询预约检测项目*/
	List<String> getJcxmListByHzid(HzxxtDto hzxxDto);
	/*预约新增患者信息*/
	boolean insertDetectionAppointmentHzxx(HzxxtDto hzxxDto);

	/*预约修改患者信息*/
	boolean updateDetectionAppointmentHzxx(HzxxtDto hzxxDto);


	/**
	 * 发送短信验证码
	 * @param hzxxDto
	 * @return
	 */
	boolean sendCode(HzxxtDto hzxxDto);
	/**
	 * 发送短信验证码
	 * @param
	 * @return
	 */
	boolean sendSms(String phoneNumbers, String signName, String templateCode, String TemplateParam);
	/**
	 * 通过sj查询患者信息
	 * @param sj
	 * @return
	 */
	List<HzxxtDto> getDtoListBySj(String sj);
	/**
	 * 查询验证码
	 * @param hzxxDto
	 * @return
	 */
	HzxxtDto getCode(HzxxtDto hzxxDto);
	/**
	 * 修改wxid
	 * @param hzxxDto
	 * @return
	 */
	int updateWxid(HzxxtDto hzxxDto);
	/**
	 * 修改患者信息
	 * @param hzxxDto
	 * @return
	 */
	boolean updatePatient(HzxxtDto hzxxDto);
	
	/**
	 * 修改新冠患者信息
	 * @param hzxxDto
	 * @return
	 */
	boolean updateCovidPatient(HzxxtDto hzxxDto);

	/*查找当前微信号最近录入的一条预约信息*/
	HzxxtDto getLastHzxxByWxid(HzxxtDto hzxxDto);
	//预约新增
	Map<String, Object> detectionAppointmentAdd(HzxxtDto hzxxDto);
	//预约修改
	Map<String, Object> detectionAppointmentMod(HzxxtDto hzxxDto);
	//取消预约
	Map<String, Object> cancleAppointment(HzxxtDto hzxxDto);

    void updateSfqr(HzxxtDto hzxxDto);

	Map<String,Object> sendMessageCode(String sjh);
	Map<String,Object> checkMessageCode(String sjh,String yzm,String time);
}
