package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.SjycDto;
import com.matridx.igams.common.dao.entities.SjycModel;
import com.matridx.igams.common.dao.entities.SjycfkDto;

import java.util.List;
import java.util.Map;

public interface ISjycService extends BaseBasicService<SjycDto, SjycModel>{

	/**
	 * 新增送检异常信息
	 */
	 boolean addSaveException(SjycDto sjycDto);
	
	/**
	 * 修改送检异常信息(异常结束)
	 */
	 boolean modSaveException(SjycDto sjycDto);
	
	/**
	 * 删除送检异常信息
	 */
	 boolean delException (SjycDto sjycDto);
	
	/**
	 * 根据送检id获取异常信息
	 */
	List<SjycDto> getDtoBySjid(SjycDto sjycDto);
	
	/**
	 * 结束异常任务
	 */
	 boolean finishYc(SjycDto sjycDto);
	
	/**
	 * 异常转发
	 */
	 boolean exceptionRepeat(SjycDto sjycDto);

	/**
	 * 小程序获取异常清单
	 */
	 List<SjycDto> getMiniDtoList(SjycDto sjycDto);
	
	/**
	 * 异常置顶
	 */
	boolean setExceptionTop(SjycDto sjycDto);
	
	/**
	 * 小程序个人清单(被通知的用户都可以看到)
	 */
	List<SjycDto> getMiniPersonalList(SjycDto sjycDto);
	/**
	 * 获取用户所有角色
	 */
	List<SjycDto> getYhjsList(SjycDto sjycDto);
	/**
	 * 更新异常权限
	 */
	 boolean updatePower(SjycDto sjycDto);
	/**
	 * 获取用户所有角色
	 */
	List<String> getxtjsmcs(List<String> list);
	/**
	 * 获取用户选择的所有通知用户
	 */
	List<String> getxtyhmcs(List<String> list);

	/**
	 * 保存异常反馈信息
	 */
	 boolean addSaveExceptionFeedback(SjycfkDto sjycfkDto,SjycDto sjycDto);
	/**
	 * 查询角色检测单位限制
	 */
	 List<Map<String, String>> getJsjcdwByjsid(String jsid);
	/**
	 * 根据业务ID获取送检信息
	 */
	SjycDto getSjxxByYwid(String ywid);
	List<SjycDto> getSjxxByYwids(List<String> sjids);

	boolean sendFkMessage(List<SjycDto>sjycDtoList,SjycfkDto sjycfkDto,boolean isFin);
	/**
	 * 异常评价保存
	 */
	boolean evaluation(SjycDto sjycDto);
	/**
	 * 更新投诉标记字段
	 * sjycDto
	 *
	 */
	boolean updateTsbj(SjycDto sjycDto);

	void fxsjaddException(SjycDto sjycDto);
}
