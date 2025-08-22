package com.matridx.server.wechat.service.svcinterface;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.matridx.igams.common.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;

import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.server.wechat.dao.entities.SjxxDto;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WeChatTextModel;
import com.matridx.server.wechat.dao.entities.WeChatUserModel;

public interface IWeChatService {
	/**
	 * 获取授权
	 */
    String authorization(String wxid) throws BusinessException;
	/**
	 * 获取accessToken
	 */
    String getAccessToken(String code) throws BusinessException;
	
	/**
	 * 返回微信的扫一扫验证信息
	 * @param request
	 * @param wbcxdm 
	 * @return
	 */
    Map<String, Object> getWechatJsApiInfo(HttpServletRequest request, String wbcxdm);
	
	/**
	 * 验证签名
	 * @param signature 微信加密签名
	 * @param timestamp 时间戳
	 * @param nonce 随机数
	 * @param wbcxDto 
	 * @return
	 */
    boolean checkSignature(String signature, String timestamp, String nonce, WbcxDto wbcxDto);
	
	/**
	 * 取消关注
	 * @param weChatTextModel 消息
	 * @param wbcxDto 
	 * @return
	 */
    boolean unsubscribe(WeChatTextModel weChatTextModel, WbcxDto wbcxDto);
	
	/**
	 * 取消关注(杰毅生物)
	 * @param weChatTextModel 消息
	 * @param wbcxDto 
	 * @return
	 */
    boolean unsubscribeMatridx(WeChatTextModel weChatTextModel, WbcxDto wbcxDto);
	
	/**
	 * 增加关注
	 * @param weChatTextModel 消息
	 * @param wbcxDto 
	 * @return
	 */
    boolean subscribe(WeChatTextModel weChatTextModel, WbcxDto wbcxDto);
	
	/**
	 * 增加关注(杰毅生物)
	 * @param weChatTextModel 消息
	 * @param wbcxDto 
	 * @return
	 */
    boolean subscribeMatridx(WeChatTextModel weChatTextModel, WbcxDto wbcxDto);
	
	/**
	 * 点击菜单拉取消息时的事件推
	 * @param weChatTextModel 消息
	 * @param wbcxDto 
	 * @return
	 */
    boolean clickEvent(WeChatTextModel weChatTextModel, WbcxDto wbcxDto);
	
	/**
	 * 点击菜单跳转链接时的事件推送
	 * @param weChatTextModel 消息
	 * @param wbcxDto 
	 * @return
	 */
    boolean viewEvent(WeChatTextModel weChatTextModel, WbcxDto wbcxDto);
	
	/**
	 * 客户发送消息处理
	 * @param weChatTextModel 消息
	 * @param wbcxDto 
	 * @return
	 */
    boolean textDeal(WeChatTextModel weChatTextModel, WbcxDto wbcxDto, String msgType);

	/**
	 * 获取本地推送菜单信息
	 * @param request
	 * @return
	 */
    boolean createMenu(HttpServletRequest request);
	
	/**
	 * 根据用户信息返回初期化送检信息
	 * @param code
	 * @param state
	 * @param wbcxdm
	 * @param organ 
	 * @return
	 */
    SjxxDto getReportInfoByUserAuth(String code, String state, String wbcxdm, String organ);
	
	/**
	 * 根据微信信息获取用户信息
	 * @param code
	 * @param state
	 * @param wbcxdm
	 * @return
	 */
    WeChatUserModel getReportListPageByUserAuth(String code, String state, String wbcxdm);
	
	/**
	 * 根据微信用户信息返回系统的用户信息
	 * @param code
	 * @param state
	 * @param wbcxdm
	 * @param organ 
	 * @return
	 */
    SjxxDto getInspectionInfoByUserAuth(String code, String state, String wbcxdm, String organ);
	
	/**
	 * 保存文件到服务器(删除原有数据)
	 * @param file
	 * @param fjcfbModel
	 * @return
	 */
    boolean saveFile(MultipartFile file, FjcfbModel fjcfbModel);
	
	/**
	 * 测试用账号，用于注册rabbitmq
	 * @return
	 */
    boolean test();

	/**
	 * 保存文件到服务器(直接保存)
	 * @param file
	 * @param fjcfbModel
	 * @return
	 */
    boolean saveFileOnly(MultipartFile file, FjcfbModel fjcfbModel);

	/**
	 * 保存多文件到服务器(直接保存)
	 * @param files
	 * @param fjcfbModels
	 * @return
	 */
    boolean saveFilesOnly(List<MultipartFile> files, List<FjcfbModel> fjcfbModels);

	/**
	 * 扫描带参数二维码
	 * @param weChatTextModel
	 * @param wbcxDto
	 */
    void scanEvent(WeChatTextModel weChatTextModel, WbcxDto wbcxDto);

}
