package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import com.matridx.igams.wechat.dao.entities.WxyhModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IWxyhDao extends BaseBasicDao<WxyhDto, WxyhModel>{

	/**
	 * 通过微信ID查询用户信息
	 * @param wxid
	 * @return
	 */
	List<WxyhDto> selectYhmByWxid(String wxid);

	/**
	 * 根据微信用户信息更新删除标记
	 * @param wxyhDto
	 * @return
	 */
	boolean updateScbjByWxyh(WxyhDto wxyhDto);

	/**
	 * 获取所有微信用户信息
	 * @return
	 */
	List<WxyhDto> getPagedDtoList();
	
	/**
	 * 根据用户id查询微信用户信息
	 * @return
	 */
	WxyhDto WxyhDto(WxyhDto wxyhdto);
	
	/**
	 * 根据标签id获取标签名
	 * @param wxyhdto
	 * @return
	 */
	List<WxyhDto> selectbqmbybqlbid(WxyhDto wxyhdto);
	
	/**
	 * 根据用户id修改微信用户信息
	 * @param wxyhdto
	 * @return
	 */
	boolean updateWxyh(WxyhDto wxyhdto);

	/**
	 * 根据用户id删除微信用户信息
	 * @param wxydto
	 * @return
	 */
	boolean deleteWxyhbyyhid(WxyhDto wxydto);
	
	/**
	 * 获取用户列表
	 * @return
	 */
	List<WxyhDto> getPagedDtoListXtyh(WxyhDto wxyhdto);
	/**
	 * 将用户列表真实姓名更新到微信用户列表
	 * @param wxyhdto
	 * @return
	 */
	boolean updatewxyh(WxyhDto wxyhdto);

	/**
	 * 获取微信用户
	 * @return
	 */
    List<WxyhDto>  getWxyhDto();

	/**
	 * 根据获取结果更新或新增微信用户信息
	 * @param wxyhList
	 * @return
	 */
	int insertOrUpdateWxyh(List<WxyhDto> wxyhList);

	/**
	 * 根据接收时间获取用户信息（周报）
	 * @param sjxxDto
	 * @return
	 */
	List<WxyhDto> selectDtoByJsrq(SjxxDto sjxxDto);
	
	/**
	 * 通过用户id获取wxyh信息
	 * @param yhid
	 * @return
	 */
	WxyhDto getWxyhByYhid(String yhid);
	
	/**
	 * 根据系统用户id查询微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	List<WxyhDto> getListByXtyhid(WxyhDto wxyhDto);
	
	/**
	 * 根据wxid获取同一系统用户或同一unionid的微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	List<WxyhDto> getListBySameId(WxyhDto wxyhDto);

	/**
	 * 修改系统用户信息
	 * @param wxyhdto
	 * @return
	 */
	boolean updteXtyh(WxyhDto wxyhdto);

	/**
	 * 根据微信ID清除系统用户表信息
	 * @param wxyhdto
	 * @return
	 */
	boolean updteXtyhByWxid(WxyhDto wxyhdto);

	/**
	 * 根据系统用户ID清除微信用户表信息
	 * @param wxyhdto
	 * @return
	 */
	boolean updateWxyhByXtyhid(WxyhDto wxyhdto);

	/**
	 * 根据微信ID获取微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	List<WxyhDto> getListByIds(WxyhDto wxyhDto);
	
	/**
	 * 根据用户ID查找原有的微信ID，然后删除相应的登录信息
	 * @param wxyhdto
	 * @return
	 */
	boolean deleteFromClient(WxyhDto wxyhdto);
	
	/**
	 * 根据微信ID，创建登录信息
	 * @param wxyhdto
	 * @return
	 */
	boolean addToClient(WxyhDto wxyhdto);
	/**
	 * 根据微信id获取微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	List<WxyhDto> getWxyhListByWxid(WxyhDto wxyhDto);
	WxyhDto getWxyhByWxid(WxyhDto wxyhDto);
}
