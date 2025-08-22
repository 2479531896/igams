package com.matridx.igams.detection.molecule.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.detection.molecule.dao.entities.HzxxDto;
import com.matridx.igams.detection.molecule.dao.entities.HzxxModel;

@Mapper
public interface IHzxxDao extends BaseBasicDao<HzxxDto, HzxxModel>{

	/**
	 * 通过身份证号查找未被删除的患者信息
	 */
	HzxxDto getHzxxByZjh(HzxxDto hzxxDto);

	/**
	 * 更新患者信息表的是否确认字段
	 */
	boolean updateSfqr(HzxxDto hzxxDto);

	/**
	 * 通过患者id查询患者信息表
	 */
	HzxxDto getHzxxByHzid(HzxxDto hzxxDto);


	/*根据微信id查询患者信息*/
	List<HzxxDto> getHzxxListByWxid(HzxxDto hzxxDto);
	/*根据sfzh查询患者信息*/
	HzxxDto getHzxxDtoByZjh(HzxxDto hzxxDto);
	/*根据sfzh查询患者信息（是否有同一天）*/
	List<HzxxDto> getHzxxDtoByZjhAndDate(HzxxDto hzxxDto);
	/*根据hzid查询患者信息*/
	HzxxDto getHzxxDtoByHzid(HzxxDto hzxxDto);
	/*根据患者id查询预约检测项目*/
	List<HzxxDto> getFzjcxmListByHzid(HzxxDto hzxxDto);
	/*预约新增患者信息*/
	boolean insertDetectionAppointmentHzxx(HzxxDto hzxxDto);
	/*预约修改患者信息*/
	boolean updateDetectionAppointmentHzxx(HzxxDto hzxxDto);

	/**
	 * 通过wxid查询患者信息
	 */
	HzxxDto getDtoListByWxid(String wxid);
	/**
	 * 通过sj查询患者信息
	 */
	List<HzxxDto> getDtoListBySj(String sj);
	/**
	 * 查询验证码
	 */
	HzxxDto getCode(HzxxDto hzxxDto);
	/**
	 * 修改wxid
	 */
	int updateWxid(HzxxDto hzxxDto);
	/**
	 * 修改患者信息
	 */
	int updatePatient(HzxxDto hzxxDto);

	/**
	 * 查询患者信息详情
	 */
	List<HzxxDto> viewPatientDetails(HzxxDto hzxxDto);

	/**
	 * 查询证件类型
	 */
	List<HzxxDto> getZjlx();
	/**
	 * 转义证件类型
	 */
	HzxxDto escapeZjlx(HzxxDto hzxxDto);

	/**
	 * 修改新冠患者信息
	 */
	boolean updateCovidPatient(HzxxDto hzxxDto);
	/**
	 * 删除新冠患者信息
	 */
	boolean deleteCovidPatient(HzxxDto hzxxDto);

	/**
	 * 查找患者预约信息
	 */
    List<HzxxDto> getOrderHzxxDtoByZjh(HzxxDto hzxxDto);
}
