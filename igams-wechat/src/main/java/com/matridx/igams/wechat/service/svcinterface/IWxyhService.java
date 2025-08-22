package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import com.matridx.igams.wechat.dao.entities.WxyhModel;

import java.util.List;

public interface IWxyhService extends BaseBasicService<WxyhDto, WxyhModel>{

	/**
	 * 新增用户信息
	 * @param wxyhDto
	 */
	void subscibe(WxyhDto wxyhDto);

	/**
	 * 删除用户信息
	 * @param wxyhDto
	 */
	void unsubscibe(WxyhDto wxyhDto);

	/**
	 * 更新、新增用户信息（用户授权信息后）
	 * @param wxyhDto
	 */
	void authorize(WxyhDto wxyhDto);

	/**
	 * 查询所有微信用户信息
	 * @return
	 */
	List<WxyhDto> getPagedDtoList();
	/**
	 * 根据用户ID查询微信用户信息
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
	 * @param wxyhdto
	 * @return
	 */
    boolean deleteWxyhbyyhid(WxyhDto wxyhdto);
	
	/**
	 * 获取用户列表
	 * @return
	 */
	List<WxyhDto> getPagedDtoListXtyh(WxyhDto wxyhdto);
	/**
	 * 将用户列表用户信息更新到微信用户
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
	 * 获取阿里服务器微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	boolean getUserList(WxyhDto wxyhDto);

	/**
	 * 根据接受日期获取用户信息(周报)
	 * @param sjxxDto
	 * @return
	 */
	List<WxyhDto> selectDtoByJsrq(SjxxDto sjxxDto);
	
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
	 * 更新微信用户和系统用户信息
	 * @param wxyhdto
	 * @return
	 */
	boolean upateYhxx(WxyhDto wxyhdto);

	/**
	 * 新增或更新微信用户信息
	 * @param wxyhDto
	 */
	void userMod(WxyhDto wxyhDto);

	/**
	 * 根据微信ID获取微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	List<WxyhDto> getListByIds(WxyhDto wxyhDto);
	
}
