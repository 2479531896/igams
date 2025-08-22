package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjkdxxDto;
import com.matridx.igams.wechat.dao.entities.SjkdxxModel;

import java.util.List;
import java.util.Map;

public interface ISjkdxxService extends BaseBasicService<SjkdxxDto, SjkdxxModel>{

	void inserDtoList(List<SjkdxxDto> resultList);

	/**
	 * 送检快递表中查询出所有未签收的数据
	 * @return
	 */
	List<SjkdxxDto> getAllDto();

	/**
	 * 通过送检ID和快递单号查找某一条数据
	 * @param sjkd
	 * @return
	 */
	int getSjkdDto(SjkdxxDto sjkd);

	/**
	 * 根据快递类型和快递状态0查找快递信息，只返回快递单号list
	 * @param sjkdxxDto
	 * @return
	 */
	List<String> getKdhListByKdlx(SjkdxxDto sjkdxxDto);

	/**
	 * 根据快递类型和快递状态0查找快递信息，返回sjkdxx的list
	 * @param sjkdxxDto
	 * @return
	 */
	List<SjkdxxDto> getSjkdDtosNotAccept(SjkdxxDto sjkdxxDto);

	//根据快递号更新送检快递表的快递开始时间
    boolean updateStarttimeByMailno(SjkdxxDto sjkdxxDto1);

	//根据快递号更新送检快递表的快递结束时间和签收状态
	boolean updateEndtimeByMailno(SjkdxxDto sjkdxxDto1);

	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
    int updateList(List<SjkdxxDto> list);

    void updateByMailno(SjkdxxDto sjkdxxDto);
	
	/**
	 * 自动生成快递单号
	 * @return
	 */
    String generateMailno();

	/**
	 * 将旧的快递单号更改为新的快递单号
	 */
	void updateMailnoByOld(Map<String, String> map);

	/**
	 * 通过快递单号获取业务id
	 * @param mailno
	 * @return
	 */
    List<String> getDtoListByMailno(String mailno);

	/**
	 * 插入送检派单快递信息
	 * @param
	 * @return
	 */
	Boolean insertSjkdxxInfo(String ywid,String gldh,String getjsfsmc,String pdh,String ywlx);

	/**
	 * 批量更新或插入
	 * @param list
	 * @return
	 */
	boolean insertOrUpdateList(List<SjkdxxDto> list);

	/**
	 * @Description: 新增修改快递信息
	 * @param list
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2025/7/2 10:54
	 */
	boolean modSjkdxxByList(List<SjkdxxDto> list);
}
