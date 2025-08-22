package com.matridx.server.wechat.dao.post;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.SjdwxxDto;
import com.matridx.server.wechat.dao.entities.SjwzxxDto;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.SjxxModel;

@Mapper
public interface ISjxxDao extends BaseBasicDao<SjxxDto, SjxxModel>{

	/**
	 * 更新确认信息
	 * @param sjxxDto
	 * @return
	 */
    int updateConfirmInfo(SjxxDto sjxxDto);

	/**
	 * 根据状态，患者姓名和时间查询信息
	 * @param sjxxDto
	 * @return
	 */
    SjxxDto extractUserInfo(SjxxDto sjxxDto);

	/**
	 * 保存修改金额
	 * @param sjxxDto
	 * @return
	 */
    int amountSaveEdit(SjxxDto sjxxDto);
	/**
	 * 列表查询
	 * @param map
	 * @return
	 */
    List<SjxxDto> getListWithMap(Map<String, Object> map);

	/**
	 * 根据送检id查询物种信息
	 * @param sjxxDto
	 * @return
	 */
	List<SjwzxxDto> selectWzxxBySjid(SjxxDto sjxxDto);

	/**
	 * 根据订单号更新支付结果
	 * @param sjxxDto
	 * @return
	 */
	public int updateByDdh(SjxxDto sjxxDto);

	/**
	 * 查询相同用户标本类型是否输入重复
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> isYblxRepeat(SjxxDto sjxxDto);

	/**
	 * 根据订单号查询送检信息
	 * @param sjxxDto
	 * @return
	 */
	public SjxxDto getDtoByDdh(SjxxDto sjxxDto);

	/**
	 * 小程序获取送检清单
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getSjxxDtoList(SjxxDto sjxxDto);

	/**
	 * 查询相同的医生电话和患者姓名
	 * @param sjxxDto
	 * @return
	 */
	public int getcountByysdh(SjxxDto sjxxDto);

	/**
	 * 根据送检信息查询出送检销售人员
	 * @return
	 */
	public List<Map<String,String>> getLrry();

	/**
	 * 根据收费情况进行统计收样(按照不收费的标本变化，收费标本的变化)
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String, String>> getSjxxBySy(SjxxDto sjxxDto);

	/**
	 * 根据收费不收费和废弃标本做统计
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String,String>> getYbxxByWeek(SjxxDto sjxxDto);

	/**
	 * 查询当前销售人员的送检单位
	 * @param sjxxDto
	 * @return
	 */
	public List<String> getSjdw(SjxxDto sjxxDto);

	/**
	 * 查询当前销售人员送检单位的送检数量
	 * @param sjxxDto
	 * @return
	 */
	public  List<Map<String,String>> getYbxxBySjdw(SjxxDto sjxxDto);

	/**
	 * 获取查看报告列表
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getPagedReportList(SjxxDto sjxxDto);

	/**
	 * 根据收费情况以周为单位进行统计收样(按照不收费的标本变化，收费标本的变化)
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String, String>> getSjxxWeekBySy(SjxxDto sjxxDto);

	/**
	 * 修改临床反馈
	 * @param sjxxDto
	 * @return
	 */
	public boolean updateFeedBack(SjxxDto sjxxDto);

	/**
	 * 医生小程序获取送检清单
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getDocMiniInsplist(SjxxDto sjxxDto);

	/**
	 * 送检结果类型统计
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getDetectionResultStatis(SjxxDto sjxxDto);

	/**
	 * 查询没有进行过反馈的送检信息
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getNofeefback(SjxxDto sjxxDto);

	/**
	 * 用一条sql 查询送检信息的其他信息
	 * @param sjid
	 * @return
	 */
	public Map<String,String> getAllSjxxOther(String sjid);

	/**
	 * 分页查询送检单位和科室信息
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getPagedUnitList(SjxxDto sjxxDto);

	/**
	 * 更新小程序送检录入第二页信息
	 * @param sjxxDto
	 * @return
	 */
	public boolean updateSecMiniSjxx(SjxxDto sjxxDto);

	/**
	 * 同步信息修改方法
	 * @param sjxxDto
	 * @return
	 */
	public int updateForbiont(SjxxDto sjxxDto);

	/**
	 * 修改送检表支付信息(同步)
	 * @param sjxxDto
	 * @return
	 */
	public int modInspPayinfo(SjxxDto sjxxDto);

	/**
	 * 检测标本编号是否重复
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> isYbbhRepeat(SjxxDto sjxxDto);

	/**
	 * 查询角色检测单位限制
	 * @param jsid
	 * @return
	 */
	public List<Map<String, String>> getJsjcdwByjsid(String jsid);


	/**
	 * 查询科室信息
	 * @return
	 */
	public List<SjdwxxDto> getSjdwxx();

	/**
	 * 病原体列表
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getPagedInspection(SjxxDto sjxxDto);

	/**
	 * 查询最近一次送检信息
	 * @param sjxxDto
	 * @return
	 */
	public SjxxDto getLastInfo(SjxxDto sjxxDto);

	/**
	 * 清空检测子项目
	 * @param sjxxDto
	 * @return
	 */
	public int emptySubDetect(SjxxDto sjxxDto);

	/*获取未完善的送检信息*/
	public List<SjxxDto> getPagedPerfectDtoList(SjxxDto sjxxDto);

	/**
	 * 根据代表名查询接收报告结果人员列表
	 * @param sjxxDto
	 * @return
	 */
	public List<SjxxDto> getReceiveUserByDb(SjxxDto sjxxDto);
	/**
	 * 修改合作伙伴
	 * @param sjxxDto
	 */
	public boolean updateDB(SjxxDto sjxxDto);
	/**
	 * 修改参数扩展（只同步是否收费信息）
	 * @param sjxxDto
	 */
	public boolean updateCskz(SjxxDto sjxxDto);
	/**
	 * 修改特殊申请
	 * @param sjxxDto
	 * @return
	 */
	public boolean updateTssq(SjxxDto sjxxDto);
	/**
	 * 修改检测项目名称
	 * @param sjxxDto
	 * @return
	 */
	public boolean updateJcxmmc(SjxxDto sjxxDto);
	/**
	 * 修改检测单位
	 * @param sjxxDto
	 * @return
	 */
	public boolean updateJcdw(SjxxDto sjxxDto);

    void updateSfjsInfo(List<SjxxDto> list);

	void updateFxwcsjByYbbh(SjxxDto sjxxDto);

	boolean updateFkje(List<SjxxDto> list);
}

