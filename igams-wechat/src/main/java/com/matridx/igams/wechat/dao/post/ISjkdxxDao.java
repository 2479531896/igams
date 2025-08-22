package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjkdxxDto;
import com.matridx.igams.wechat.dao.entities.SjkdxxModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ISjkdxxDao extends BaseBasicDao<SjkdxxDto, SjkdxxModel>{

	void inserDtoList(List<SjkdxxDto> resultList);

	/**
	 * 查询出所有未签收的送检快递信息
	 * @return
	 */
	List<SjkdxxDto> getAllDto();

	/**
	 * 通过送检ID和快递号查找一条数据
	 * @param sjkd
	 * @return
	 */
	int getSjkdDto(SjkdxxDto sjkd);

	//根据快递类型和快递状态0查找快递信息
	List<String> getKdhListByKdlx(SjkdxxDto sjkdxxDto);

	//根据快递类型和快递状态0查找快递信息，返回sjkdxx的list
	List<SjkdxxDto> getSjkdDtosNotAccept(SjkdxxDto sjkdxxDto);

	//根据快递号更新送检快递表的快递开始时间
	boolean updateStarttimeByMailno(SjkdxxDto sjkdxxDto1);

	//根据快递号更新送检快递表的快递结束时间和签收状态
	boolean updateEndtimeByMailno(SjkdxxDto sjkdxxDto);

	/**
	 * 批量更新
	 * @param list
	 * @return
	 */
	public int updateList(List<SjkdxxDto> list);
	
    /**
	* 更新旧快递单号为新快递单号
	*/
    void updateByMailno(SjkdxxDto sjkdxxDto);
	/**
	 * 获取快递单号
	 * @param str
	 * @return
	 */
	public String getMailnoSerial(String str);

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
