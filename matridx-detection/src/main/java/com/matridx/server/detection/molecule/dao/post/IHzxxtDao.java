package com.matridx.server.detection.molecule.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.detection.molecule.dao.entities.HzxxtDto;
import com.matridx.server.detection.molecule.dao.entities.HzxxtModel;


@Mapper
public interface IHzxxtDao extends BaseBasicDao<HzxxtDto, HzxxtModel>{

	/**
	 * 通过身份证号查找未被删除的患者信息
	 * @param hzxxDto
	 * @return
	 */
	HzxxtDto getHzxxByZjh(HzxxtDto hzxxDto);

	/**
	 * 更新患者信息表的是否确认字段
	 * @param hzxxDto
	 */
	void updateSfqr(HzxxtDto hzxxDto);

	/**
	 * 通过患者id查询患者信息表
	 * @param hzxxDto
	 * @return
	 */
	HzxxtDto getHzxxByHzid(HzxxtDto hzxxDto);

	/*根据微信id查询患者信息*/
	List<HzxxtDto> getHzxxListByWxid(HzxxtDto hzxxDto);
	/*根据sfzh查询患者信息*/
	List<HzxxtDto> getHzxxDtoByZjh(HzxxtDto hzxxDto);
	/*根据hzid查询患者信息*/
	HzxxtDto getHzxxDtoByHzid(HzxxtDto hzxxDto);
	/*根据患者id查询预约检测项目*/
	List<HzxxtDto> getFzjcxmListByHzid(HzxxtDto hzxxDto);
	/*预约新增患者信息*/
	boolean insertDetectionAppointmentHzxx(HzxxtDto hzxxDto);
	/*预约修改患者信息*/
	boolean updateDetectionAppointmentHzxx(HzxxtDto hzxxDto);


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
	int updatePatient(HzxxtDto hzxxDto);

	/**
	 * 修改新冠患者信息
	 * @param hzxxDto
	 * @return
	 */
	boolean updateCovidPatient(HzxxtDto hzxxDto);

	/*查找当前微信号最近录入的一条预约信息*/
	HzxxtDto getLastHzxxByWxid(HzxxtDto hzxxDto);
}
