package com.matridx.server.wechat.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.server.wechat.dao.entities.PayinfoDto;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WxyhDto;
import com.matridx.server.wechat.dao.entities.WxyhModel;
import com.matridx.server.wechat.enums.IdentityTypeEnum;

public interface IWxyhService extends BaseBasicService<WxyhDto, WxyhModel>{

	/**
	 * 通过微信ID查询用户信息
	 * @param openid
	 * @return
	 */
	List<WxyhDto> selectYhmByWxid(String openid);

	/**
	 * 根据微信用户信息更新删除标记
	 * @param wxyhDto
	 * @return
	 */
	boolean updateScbjByWxyh(WxyhDto wxyhDto);
	
	/**
	 * 查询所有微信用户信息
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
	 * @param wxyhdto
	 * @return
	 */
    boolean deleteWxyhbyyhid(WxyhDto wxyhdto);

	/**
	 * 批量添加用户标签保存
	 * @param wxyhDto
	 * @return
	 */
	boolean setSaveTagUser(WxyhDto wxyhDto);

	/**
	 * 批量取消用户标签保存
	 * @param wxyhDto
	 * @return
	 */
	boolean cancleSaveTagUser(WxyhDto wxyhDto);

	/**
	 * 获取身份类型枚举列表
	 * @return
	 */
    boolean updateSj(WxyhDto wxydto);
	
	/**
	 * 查询所有手机号
	 * @return
	 */
	List<WxyhDto> getAllSj();
		/**
	 * 获取身份类型枚举列表
	 * @return
	 */
	List<IdentityTypeEnum> getIdentityType();

	/**
	 * 修改用户信息
	 * @param wxyhDto
	 * @return
	 * @throws BusinessException 
	 */
	boolean modSaveWeChatUser(WxyhDto wxyhDto) throws BusinessException;

	/**
	 * 发送短信验证码
	 * @param wxyhDto
	 * @return
	 */
	boolean getSms(WxyhDto wxyhDto);

	/**
	 * 获取身份类型
	 * @return
	 */
	Map<String, String> getIdentityTypeMap();
	
	/**
	 * 发送手机短信
	 * @param phoneNumbers
	 * @param signName
	 * @param templateCode
	 * @param templateParam
	 * @return
	 */
    boolean sendSms(String phoneNumbers, String signName, String templateCode, String templateParam);

	/**
	 * 更新现有微信用户信息
	 * @param wbcxDto
	 * @return
	 */
	boolean updateUserList(WbcxDto wbcxDto);

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
	 * 更新、新增用户信息（用户授权信息后）
	 * @param wxyhDto
	 */
	boolean authorize(WxyhDto wxyhDto);
	/**
	 * 根据yhid修改微信吗
	 * @param wxyhDto
	 * @return
	 */
	boolean updateWxmByid(WxyhDto wxyhDto);
}
