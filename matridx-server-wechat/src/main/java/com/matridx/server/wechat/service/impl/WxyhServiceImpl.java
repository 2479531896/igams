package com.matridx.server.wechat.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.matridx.igams.common.redis.RedisUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.server.wechat.dao.entities.PayinfoDto;
import com.matridx.server.wechat.dao.entities.WbcxDto;
import com.matridx.server.wechat.dao.entities.WeChatUserModel;
import com.matridx.server.wechat.dao.entities.WxyhDto;
import com.matridx.server.wechat.dao.entities.WxyhModel;
import com.matridx.server.wechat.dao.post.IWxyhDao;
import com.matridx.server.wechat.enums.IdentityTypeEnum;
import com.matridx.server.wechat.enums.MQWechatTypeEnum;
import com.matridx.server.wechat.service.svcinterface.IWbcxService;
import com.matridx.server.wechat.service.svcinterface.IWxyhService;
import com.matridx.server.wechat.service.svcinterface.IYhbqService;
import com.matridx.server.wechat.util.WeChatUtils;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;

@Service
public class WxyhServiceImpl extends BaseBasicServiceImpl<WxyhDto, WxyhModel, IWxyhDao> implements IWxyhService, IFileImport{

	@Autowired
	RestTemplate restTemplate;
	@Autowired
	IYhbqService yhbqSerivce;
	@Value("${matridx.aliyunSms.accessKeyId:'abc'}")
	private String accessKeyId;
	@Value("${matridx.aliyunSms.accessSecret:'abc'}")
	private String accessSecret;
	@Value("${matridx.aliyunSms.signName:'abc'}")
	private String signName;
	@Value("${matridx.aliyunSms.templateCode:'abc'}")
	private String templateCode;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	@Autowired
	IWbcxService wbcxSerivce;
	@Autowired
	private RedisUtil redisUtil;
	private Logger log = LoggerFactory.getLogger(WxyhServiceImpl.class);
	/** 
	 * 插入送检信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(WxyhDto wxyhDto){
		wxyhDto.setYhid(StringUtil.generateUUID());
		int result = dao.insert(wxyhDto);
		if(result == 0)
			return false;
		
		return true;
	}

	/**
	 * 通过微信ID查询用户信息
	 * @param wxid
	 * @return
	 */
	@Override
	public List<WxyhDto> selectYhmByWxid(String wxid) {
		// TODO Auto-generated method stub
		return dao.selectYhmByWxid(wxid);
	}

	/**
	 * 根据微信用户信息更新删除标记
	 * @param wxyhDto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateScbjByWxyh(WxyhDto wxyhDto) {
		// TODO Auto-generated method stub
		return dao.updateScbjByWxyh(wxyhDto);
	}
	
	/**
	 * 获取所有微信用户信息
	 */
	@Override
	public List<WxyhDto> getPagedDtoList() {
		// TODO Auto-generated method stub
		return dao.getPagedDtoList();
	}
	
	/**
	 * 根据用户id修改微信用户信息
	 * @param wxyhdto
	 * @return
	 */
	public boolean updateWxyh(WxyhDto wxyhdto) {
		return dao.updateWxyh(wxyhdto);
	}
	/**
	 * 根据标签id获取标签名
	 */
	@Override
	public List<WxyhDto> selectbqmbybqlbid(WxyhDto wxyhdto) {
		return dao.selectbqmbybqlbid(wxyhdto);
	}
	/**
	 * 根据用户id删除微信用户信息
	 */
	public boolean deleteWxyhbyyhid(WxyhDto wxyhdto){
		return dao.deleteWxyhbyyhid(wxyhdto);
	}

	/**
	 * 批量添加用户标签保存
	 * @param wxyhDto
	 * @return
	 */
	@Override
	public boolean setSaveTagUser(WxyhDto wxyhDto) {
		// TODO Auto-generated method stub
		WbcxDto wbcxDto = wbcxSerivce.getDtoById(wxyhDto.getWbcxid());
		wxyhDto.setWbcxdm(wbcxDto.getWbcxdm());
		boolean result = WeChatUtils.setTagUser(wxyhDto, restTemplate,redisUtil);
		if(!result)
			return false;
		//更新信息至用户标签表
		yhbqSerivce.deleteByWxids(wxyhDto);
		result = yhbqSerivce.insertByWxids(wxyhDto);
		if(!result)
			return false;
		return true;
	}

	/**
	 * 批量取消用户标签保存
	 * @param wxyhDto
	 * @return
	 */
	@Override
	public boolean cancleSaveTagUser(WxyhDto wxyhDto) {
		// TODO Auto-generated method stub
		WbcxDto wbcxDto = wbcxSerivce.getDtoById(wxyhDto.getWbcxid());
		wxyhDto.setWbcxdm(wbcxDto.getWbcxdm());
		boolean result = WeChatUtils.cancleTagUser(wxyhDto, restTemplate,redisUtil);
		if(!result)
			return false;
		//更新信息至用户标签表
		result = yhbqSerivce.deleteByWxids(wxyhDto);
		if(!result)
			return false;
		return true;
	}

	/**
	 * 获取身份类型枚举列表
	 * @return
	 */

	public List<IdentityTypeEnum> getIdentityType() {
		// TODO Auto-generated method stub
		List<IdentityTypeEnum> identitylist = new ArrayList<IdentityTypeEnum>();
			for (IdentityTypeEnum dir : IdentityTypeEnum.values()) { 
				identitylist.add(dir);
			}
		return identitylist;
	}

	/**
	 * 获取身份类型枚举列表
	 * @return
	 */
	public Map<String, String> getIdentityTypeMap() {
		// TODO Auto-generated method stub
		Map<String, String> identitymap = new HashMap<String, String>();
		for (IdentityTypeEnum dir : IdentityTypeEnum.values()) {
			identitymap.put(dir.getCode(), dir.getValue());
		}
		return identitymap;
	}
	
	public boolean updateSj(WxyhDto wxydto){
		return dao.updateSj(wxydto);
	}
	
	@Override
	public List<WxyhDto> getAllSj(){
		// TODO Auto-generated method stub
		return dao.getAllSj();
	}

	/**
	 * 修改用户信息
	 * @param wxyhDto
	 * @return
	 * @throws BusinessException 
	 */
	@Override
	public boolean modSaveWeChatUser(WxyhDto wxyhDto) throws BusinessException {
		// TODO Auto-generated method stub
		if(StringUtil.isNotBlank(wxyhDto.getCskz1())){
			WxyhDto t_wxyhDto = dao.getDtoByIdAndGzpt(wxyhDto);
			if(StringUtil.isBlank(t_wxyhDto.getCskz3()) || (System.currentTimeMillis()-Long.parseLong(t_wxyhDto.getCskz3()))/1000 > 60){
				throw new BusinessException("msg","验证码失效，请重新发送！");
			}
			if(StringUtil.isBlank(t_wxyhDto.getCskz2()) || !wxyhDto.getSj().equals(t_wxyhDto.getCskz2())){
				throw new BusinessException("msg","手机号与验证手机号不符！");
			}
			if(StringUtil.isBlank(t_wxyhDto.getCskz1()) || !wxyhDto.getCskz1().equals(t_wxyhDto.getCskz1())){
				throw new BusinessException("msg","验证码有误请重新发送！");
			}
		}
		boolean result = update(wxyhDto);
		if(result){
			String jsonString = JSONObject.toJSONString(wxyhDto);
			amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.WECHAR_USER_MOD.getCode(), jsonString);
		}
		return result;
	}

	/**
	 * 获取短信验证码
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public boolean getSms(WxyhDto wxyhDto) {
		// TODO Auto-generated method stub
		//获取四位验证码
		wxyhDto.setCskz1(RandomStringUtils.random(4, "0123456789"));
		wxyhDto.setCskz3(String.valueOf(System.currentTimeMillis()));
		boolean result = update(wxyhDto);
		if(!result)
			return false;
		result = sendSms(wxyhDto.getCskz2(), signName, templateCode, wxyhDto.getCskz1());
		if(!result)
			return false;
		return true;
	}
	
	/**
	 * 发送手机短信
	 * @param PhoneNumbers
	 * @param TemplateParam
	 * @return
	 */
	public boolean sendSms(String phoneNumbers, String signName, String templateCode, Map<String, String> mapParam) {
		// TODO Auto-generated method stub
		DBEncrypt crypt = new DBEncrypt();
		DefaultProfile profile = DefaultProfile.getProfile("RegionId", crypt.dCode(accessKeyId), crypt.dCode(accessSecret));
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //无需替换
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        
        //必填:短信签名-可在短信控制台中找到，你在签名管理里的内容
        request.putQueryParameter("SignName", signName);
        //必填:待发送手机号
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        //必填:短信模板-可在短信控制台中找到，你模板管理里的模板编号
        request.putQueryParameter("TemplateCode", templateCode);
        for (String key : mapParam.keySet()) {
        	if(StringUtil.isNotBlank(mapParam.get(key))){
        		//可选:模板中的变量替换JSON串,如模板内容为"您的验证码为${kzcs1}..."时,此处的值为
                request.putQueryParameter("TemplateParam", "{\""+key+"\":\""+mapParam.get(key)+"\"}");
        	}
		}
        try {
            CommonResponse response = client.getCommonResponse(request);
            if(StringUtil.isNotBlank(response.getData())){
            	@SuppressWarnings("unchecked")
				Map<String, String> resMap = (Map<String, String>)JSON.parse(response.getData());
            	if("OK".equals(resMap.get("Code"))){
            		 return true;
            	}
            }
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
	}

	/**
	 * 发送手机短信
	 * @param PhoneNumbers
	 * @param TemplateParam
	 * @return
	 */
	public boolean sendSms(String phoneNumbers, String signName, String templateCode, String TemplateParam) {
		// TODO Auto-generated method stub
		DBEncrypt crypt = new DBEncrypt();
		DefaultProfile profile = DefaultProfile.getProfile("RegionId", crypt.dCode(accessKeyId), crypt.dCode(accessSecret));
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //无需替换
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        
        //必填:短信签名-可在短信控制台中找到，你在签名管理里的内容
        request.putQueryParameter("SignName", signName);
        //必填:待发送手机号
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        //必填:短信模板-可在短信控制台中找到，你模板管理里的模板编号
        request.putQueryParameter("TemplateCode", templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"您的验证码为${code}"时,此处的值为
        request.putQueryParameter("TemplateParam", "{\"code\":\""+TemplateParam+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return true;
	}

	@Override
	public boolean existCheck(String fieldName, String value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean insertImportRec(BaseModel baseModel, User user,int index, StringBuffer errorMessage) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
		// TODO Auto-generated method stub
		String sjh =  recMap.get("0");
		String code = recMap.get("dxmb");
		String sms_sign = recMap.get("sms_sign");
		String size = recMap.get("size");
		Integer length = Integer.valueOf(size);
		Map<String, String> map = new HashMap<String, String>();
		for (int i = 1; i < length; i++) {
			String index = String.valueOf(i);
			map.put("kzcs"+index, recMap.get(index));
		}
		return sendSms(sjh, sms_sign, code, map);
	}

	/**
	 * 更新现有微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updateUserList(WbcxDto wbcxDto) {
		// TODO Auto-generated method stub
		//根据外部程序代码查询外部程序ID
		WbcxDto t_wbcxDto = wbcxSerivce.getDto(wbcxDto);
		if(t_wbcxDto == null)
			return false;
		//获取用户信息
		List<WeChatUserModel> userlist = WeChatUtils.getUserList(restTemplate, wbcxDto.getWbcxdm(),redisUtil);
		if (userlist == null || userlist.size() == 0)
			return false;
		
		for (int i = 0; i < userlist.size(); i++) {
			WeChatUserModel userinfo = userlist.get(i);
			WxyhDto wxyhDto = new WxyhDto();
			wxyhDto.setWxid(userinfo.getOpenid());
			wxyhDto.setWxm(userinfo.getNickname());
			wxyhDto.setYhxb(userinfo.getSex());
			wxyhDto.setYhcs(userinfo.getCity());
			wxyhDto.setBqidlb(userinfo.getTagid_list());
			wxyhDto.setUnionid(userinfo.getUnionid());
			wxyhDto.setGzpt(t_wbcxDto.getWbcxid());
			wxyhDto.setLrry(wbcxDto.getLrry());
			wxyhDto.setXgry(wbcxDto.getLrry());
			wxyhDto.setWbcxdm(wbcxDto.getWbcxdm());
			//根据微信ID判断是否新增用户
			List<WxyhDto> wxList = getWxyhListByWxid(wxyhDto);
			if (wxList == null || wxList.size() == 0){
				insertDto(wxyhDto);
			}else{
				update(wxyhDto);
			}
		}
		
		return true;
	}

	/**
	 * 根据关注平台查询用户
	 * @param gzpt
	 * @return
	 */
	@Override
	public List<WxyhDto> getListByGzpt(String gzpt) {
		// TODO Auto-generated method stub
		return dao.getListByGzpt(gzpt);
	}

	/**
	 * 检查标题定义，主要防止模板信息过旧
	 * @param defined
	 * @return
	 */
	public boolean checkDefined(List<Map<String,String>> defined) {
		return true;
	}

	/**
	 * 查询支付消息通知用户
	 * @param payinfoDto
	 * @return
	 */
	@Override
	public List<WxyhDto> receivePayMsgUser(PayinfoDto payinfoDto) {
		return dao.receivePayMsgUser(payinfoDto);
	}

	/**
	 * 根据微信id获取微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	@Override
	public List<WxyhDto> getWxyhListByWxid(WxyhDto wxyhDto){
		return dao.getWxyhListByWxid(wxyhDto);
	}
	/**
	 * 根据手机获取微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	@Override
	public List<WxyhDto> getWxyhListBySj(WxyhDto wxyhDto){
		return dao.getWxyhListBySj(wxyhDto);
	}


	/**
	 * 更新、新增用户信息（用户授权信息后）
	 * @param wxyhDto
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean authorize(WxyhDto wxyhDto) {
		boolean isSuccess = false;
		try {
			//通过微信ID查询用户信息
			List<WxyhDto> yhList= dao.getWxyhListByWxid(wxyhDto);
			if(yhList !=null && yhList.size() > 0) {
				wxyhDto.setYhid(yhList.get(0).getYhid());
				//更新用户信息
				isSuccess = dao.updateScbjByWxyh(wxyhDto);
			}else{
				//新增用户信息
				isSuccess = insertDto(wxyhDto);
			}
			amqpTempl.convertAndSend("wechat.exchange", MQWechatTypeEnum.WECHAT_AUTHORIZE.getCode(), JSONObject.toJSONString(wxyhDto));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
		return isSuccess;
	}

	@Override
	public boolean updateWxmByid(WxyhDto wxyhDto) {
		return dao.updateWxmByid(wxyhDto);
	}
}
