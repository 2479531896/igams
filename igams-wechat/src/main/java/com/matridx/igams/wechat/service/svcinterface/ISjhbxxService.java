package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjhbxxModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;

import java.util.List;
import java.util.Map;

public interface ISjhbxxService extends BaseBasicService<SjhbxxDto, SjhbxxModel>{
	/**
	 * 查询合作伙伴
	 * @return
	 */
    List<SjhbxxDto> getDB();
	
	
	/**
	 * 添加合作伙伴，然后添加收费标准
	 * @param sjhbxxDto
	 * @return
	 */
    boolean insertAll(SjhbxxDto sjhbxxDto);
	
	/**
	 * 修改所有
	 * @param sjhbxxDto
	 * @return
	 */
    boolean updateAll(SjhbxxDto sjhbxxDto);
	
	/**
	 * 删除
	 * @param sjhbxxDto
	 * @return
	 */
    boolean deletepartner(SjhbxxDto sjhbxxDto);
	
	/**
	 * 查询用户id
	 * @return
	 */
    List<SjhbxxDto> getXtyh();

	/**
	 * 查询伙伴为'无'的用户信息
	 * @return
	 */
    List<SjhbxxDto> getXtyhByHbmc();
	
	/**
	 * 查询合作伙伴是否存在
	 * @param sjhbxxDto
	 * @return
	 */
    SjhbxxDto selectSjhb(SjhbxxDto sjhbxxDto);
	
	/**
	 * 获取合作伙伴发送方式
	 * @return
	 */
    List<String> getSendMode();

	/**
	 * 根据接受日期获取代表信息(周报)
	 * @param sjxxDto
	 * @return
	 */
    List<SjhbxxDto> selectDtoByJsrq(SjxxDto sjxxDto);

	/**
	 * 查询合作伙伴分类信息
	 * @return
	 */
    List<SjhbxxDto> selectFl();
	/**
	 * 查询合作伙伴分类信息
	 * @return
	 */
    List<SjhbxxDto> getSjhbFlAndZfl();
	
	/**
	 * 验证合作伙伴是否已经存在
	 * @param sjhbxxDto
	 * @return
	 */
    int getCountSjhb(SjhbxxDto sjhbxxDto);
	
	/**
	 * 通过hbid查询 合作伙伴
	 * @param list
	 * @return
	 */
    List<String> getHbmcByHbid(List<String> list);

	/**
	 * 查询伙伴统计信息
	 * @param sjhbxxDto
	 * @return
	 */
    List<SjhbxxDto> getTjDtoList(SjhbxxDto sjhbxxDto);
	
	/**
	 * 微信用户查询伙伴权限分类信息 
	 * @param sjhbxxDto
	 * @return
	 */
    List<SjhbxxDto> selectFlByHbqx(SjhbxxDto sjhbxxDto);
	
	/**
	 * 微信周报伙伴分类查询
	 * @param sjhbxxDto
	 * @return
	 */
    List<SjhbxxDto> selectFlByWeekly(SjhbxxDto sjhbxxDto);
	
	/**
	 * 根据用户id伙伴权限查询伙伴统计信息 
	 * @param sjhbxxDto
	 * @return
	 */
    List<SjhbxxDto> getSjhbDtoListByHbqx(SjhbxxDto sjhbxxDto);

	/**
	 * 查询代表信息(权限)
	 * @param ddid
	 * @param wxid
	 * @return
	 */
    List<SjhbxxDto> getDbList(String ddid, String wxid);
	
	/**
	 * 恢复删除伙伴
	 * @param sjhbxxDto
	 * @return
	 */
    boolean resumepartner(SjhbxxDto sjhbxxDto);

	/**
	 * 查询伙伴为无的报告模板
	 * @param sjhbxxDto
	 * @return
	 */
    List<SjhbxxDto> getBgmbByHbmc(SjhbxxDto sjhbxxDto);
	
	/**
	 * 启用
	 * @param sjhbxxDto
	 * @return
	 */
    boolean enablepartner(SjhbxxDto sjhbxxDto);
	
	/**
	 * 停用
	 * @param sjhbxxDto
	 * @return
	 */
    boolean disablepartner(SjhbxxDto sjhbxxDto);
	/**
	 * 查询不为空的伙伴统计信息 
	 * @param sjxxDto
	 * @return
	 */

    List<Map<String, Object>> getnotNullDb(SjxxDto sjxxDto);
	/**
	 * 查询不为空的伙伴统计信息 (接收日期)
	 * @param sjxxDto
	 * @return
	 */

    List<Map<String, Object>> getnotNullDbByJsrq(SjxxDto sjxxDto);

	/**
	 * 更新直销的cskz3为1
	 * @return
	 */
    boolean updateCskz3();
	/**
	 * 更新大区信息
	 * @return
	 */
    boolean updateDqxx();

	/**
	 * 查询重复绑定大区的送检伙伴
	 * @return
	 */
    List<SjhbxxDto> getRepeatHbxx();
	/**
	 * 获取共创平台
	 */
    List<SjhbxxDto> getWbcxDtoList();

	/**
	 * 根据用户获取平台信息
	 * @param str
	 * @return
	 */
    List<String> getPtgsByYhid(String str);

	/**
	 * 根据平台归属查询伙伴统计信息
	 * @param sjhbxxDto
	 * @return
	 */
    List<SjhbxxDto> getListFromXxdy(SjhbxxDto sjhbxxDto);
	/**
	 * 查询不为空的伙伴统计信息 (接收日期)
	 * @param sjxxDto
	 * @return
	 */
    List<Map<String, Object>> getNotNullDbFromXxdy(SjxxDto sjxxDto);

    boolean insertsfbz(SjhbxxDto sjhbxxDto);

	/**
	 * 批量修改伙伴收费标准（有则修改，无则跳过）
	 * @param sjhbxxDto
	 * @return
	 */
	Map<String,Object> batchModSfbz(SjhbxxDto sjhbxxDto);
}
