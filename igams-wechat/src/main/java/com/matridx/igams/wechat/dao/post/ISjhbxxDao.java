package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.SjhbxxModel;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface ISjhbxxDao extends BaseBasicDao<SjhbxxDto, SjhbxxModel>{
	/**
	 * 查询合作伙伴
	 * @return
	 */
    List<SjhbxxDto> getDB();
	
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
	 * 根据接受日期获取代表信息(周报)
	 * @param sjxxDto
	 * @return
	 */
    List<SjhbxxDto> selectDtoByJsrq(SjxxDto sjxxDto);

	/**
	 * 查询合作伙伴分类信息
	 * @param sjhbxxDto 
	 * @return
	 */
    List<SjhbxxDto> selectFl(SjhbxxDto sjhbxxDto);
	
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
	 * 根据用户id伙伴权限查询伙伴统计信息 
	 * @param sjhbxxDto
	 * @return
	 */
    List<SjhbxxDto> getSjhbDtoListByHbqx(SjhbxxDto sjhbxxDto);

	/**
	 * 根据钉钉ID获取代表
	 * @param sjxxDto
	 * @return
	 */
    List<SjhbxxDto> getDbByDdid(SjxxDto sjxxDto);

	/**
	 * 根据微信ID获取代表
	 * @param wxyhlist
	 * @return
	 */
    List<SjhbxxDto> getDbByWxid(List<WxyhDto> wxyhlist);
	
	/**
	 * 恢复删除伙伴
	 * @param sjhbxxDto
	 * @return
	 */
    boolean resumepartner(SjhbxxDto sjhbxxDto);

	/**
	 * 查询伙伴为无的报告模板
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
	 * 根据搜索条件获取导出条数
	 * @param sjhbxxDto
	 * @return
	 */
    int getCountForSearchExp(SjhbxxDto sjhbxxDto);
	/**
	 * 从数据库分页获取导出物料数据
	 * @param sjhbxxDto
	 * @return
	 */
    List<SjhbxxDto> getListForSearchExp(SjhbxxDto sjhbxxDto);

	/**
	 * 从数据库分页获取导出送检信息数据
	 * @param sjhbxxDto
	 * @return
	 */
    List<SjhbxxDto> getListForSelectExp(SjhbxxDto sjhbxxDto);
	/**
	 * 查询角色检测单位限制
	 * @param jsid
	 * @return
	 */
	List<Map<String,String>> getJsjcdwByjsid(String jsid);
	/**
	 * 查询重复绑定大区的送检伙伴
	 * @return
	 */
	List<SjhbxxDto> getRepeatHbxx();
	/**
	 * 更新
	 */
	boolean updatePageEvent(SjhbxxDto sjhbxxDto);

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
	/**
	 * 查询用户删除或者锁定的伙伴
	 */
	List<SjhbxxDto> getLockedUserList();
	/**
	 * 备份伙伴信息
	 */
	boolean backUpPartnerInfo(String date);

	/**
	 * 获取上次备份时间
	 */
	String getBackUpPartnerMaxDate();
}
