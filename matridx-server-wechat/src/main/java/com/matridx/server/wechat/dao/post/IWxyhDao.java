package com.matridx.server.wechat.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.server.wechat.dao.entities.DxtzDto;
import com.matridx.server.wechat.dao.entities.PayinfoDto;
import com.matridx.server.wechat.dao.entities.WxyhDto;
import com.matridx.server.wechat.dao.entities.WxyhModel;

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
	 * 修改微信手机号
	 * @param wxydto
	 * @return
	 */
    boolean updateSj(WxyhDto wxydto);
	
	/**
	 * 查询所有手机号
	 * @return
	 */
	List<WxyhDto> getAllSj();

	/**
	 * 查询短信列表
	 * @return
	 */
	List<DxtzDto> selectDxtzList();

	/**
	 * 根据关注平台查询用户
	 * @param gzpt
	 * @return
	 */
	List<WxyhDto> getListByGzpt(String gzpt);

	/**
	 * 查询支付消息通知用户
	 * @param payinfoDto
	 * @return
	 */
	List<WxyhDto> receivePayMsgUser(PayinfoDto payinfoDto);

	/**
	 * 根据微信id获取微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	List<WxyhDto> getWxyhListByWxid(WxyhDto wxyhDto);
	/**
	 * 根据手机获取微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	List<WxyhDto> getWxyhListBySj(WxyhDto wxyhDto);
	/**
	 * 通过微信ID和关注平台查询用户信息
	 * @param wxyhDto
	 * @return
	 */
	WxyhDto getDtoByIdAndGzpt(WxyhDto wxyhDto);

	/**
	 * 根据yhid修改微信吗
	 * @param wxyhDto
	 * @return
	 */
	boolean updateWxmByid(WxyhDto wxyhDto);
}
