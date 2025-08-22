package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.SjycDto;
import com.matridx.igams.common.dao.entities.SjycModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISjycDao extends BaseBasicDao<SjycDto, SjycModel>{

	/**
	 * 根据确认人获取钉钉id
	 * sjycDto
	 * 
	 */
	List<SjycDto> getDdidByQrr(SjycDto sjycDto);

	/**
	 * 根据异常id获取异常信息（包括fjid）
	 * sjycDto
	 * 
	 */
	List<SjycDto> getDtoWithFijd(SjycDto sjycDto);
	/**
	 * 根据确认角色获取钉钉id
	 * sjycDto
	 * 
	 */
	List<SjycDto> getDdidByQrjs(SjycDto sjycDto);
	
	/**
	 * 根据送检id获取异常信息
	 * sjycDto
	 * 
	 */
	List<SjycDto> getDtoBySjid(SjycDto sjycDto);
	
	/**
	 * 从数据库分页获取导出异常信息
	 * sjxxDto
	 * 
	 */
	List<SjycDto> getListForSelectExp(SjycDto sjycDto);
	
	/**
	 * 查询可以导出的条数
	 * sjycDto
	 * 
	 */
	int getCountForSearchExp(SjycDto sjycDto);
	
	/**
	 * 从数据库分页获取导出送检异常
	 * sjxxDto
	 * 
	 */
	List<SjycDto> getListForSearchExp(SjycDto sjycDto);
	
	
	/**
	 * 根据通知人员获取通知人员的钉钉id
	 * sjycDto
	 * 
	 */
	List<SjycDto> getDdidByTzrys(SjycDto sjycDto);
	
	/**
	 * 结束异常任务
	 * sjycDto
	 * 
	 */
	boolean finishYc(SjycDto sjycDto);
	
	/**
	 * 根据通知部门获取通知人员的钉钉id
	 * sjycDto
	 * 
	 */
	List<SjycDto> getDdidByTzjgs(SjycDto sjycDto);
	
	/**
	 * 小程序获取异常清单
	 * sjycDto
	 * 
	 */
	List<SjycDto> getMiniDtoList(SjycDto sjycDto);
	
	/**
	 * 异常置顶
	 * sjycDto
	 * 
	 */
	boolean setExceptionTop(SjycDto sjycDto);
	
	/**
	 * 小程序个人清单(被通知的用户都可以看到)
	 * sjycDto
	 * 
	 */
	List<SjycDto> getMiniPersonalList(SjycDto sjycDto);
	/**
	 * 获取用户所有角色
	 * sjycDto
	 * 
	 */
	List<SjycDto> getYhjsList(SjycDto sjycDto);
	
	/**
	 * 更新确认人
	 * sjycDto
	 * 
	 */
	boolean updateQrr(SjycDto sjycDto);
	/**
	 * 获取用户所有角色
	 * sjycDto
	 * 
	 */
	List<String> getxtjsmcs(List<String> list);
	/**
	 * 获取用户选择的所有通知用户
	 * sjycDto
	 * 
	 */
	List<String> getxtyhmcs(List<String> list);
	/**
	 * 查询角色检测单位限制
	 * jsid
	 * 
	 */
	List<Map<String, String>> getJsjcdwByjsid(String jsid);
	/**
	 * 根据业务ID获取送检信息
	 * ywid
	 * 
	 */
	SjycDto getSjxxByYwid(String ywid);
	List<SjycDto> getSjxxByYwids(List<String> sjids);
	/**
	 * 根据yhid查询钉钉id
	 * list
	 * 
	 */
	List<SjycDto> getDdidByYhids(List<SjycDto> list);
	/**
	 * 异常评价保存
	 * sjycDto
	 * 
	 */
	boolean evaluation(SjycDto sjycDto);
	/**
	 * 更新投诉标记字段
	 * sjycDto
	 *
	 */
	boolean updateTsbj(SjycDto sjycDto);

}
