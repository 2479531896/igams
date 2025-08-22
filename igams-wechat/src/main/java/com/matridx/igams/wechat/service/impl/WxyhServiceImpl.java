package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import com.matridx.igams.wechat.dao.entities.WxyhModel;
import com.matridx.igams.wechat.dao.post.IWxyhDao;
import com.matridx.igams.wechat.service.svcinterface.IWxyhService;
import com.matridx.igams.wechat.util.WeChatUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.Encrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class WxyhServiceImpl extends BaseBasicServiceImpl<WxyhDto, WxyhModel, IWxyhDao> implements IWxyhService{
	
	@Value("${matridx.wechat.menuurl:}")
	private String menuurl;
//	@Autowired
//	RedisUtil redisUtil;
	private Logger log = LoggerFactory.getLogger(WxyhServiceImpl.class);
	
	/**
	 * 新增用户信息
	 * @param wxyhDto
	 */
	@Override
	public void subscibe(WxyhDto wxyhDto) {
		try {
			//通过微信ID查询用户信息
			List<WxyhDto> yhList= dao.getWxyhListByWxid(wxyhDto);
			if(yhList !=null && yhList.size() > 0) {
				//更新用户信息
				dao.updateScbjByWxyh(wxyhDto);
			}else{
				//新增用户信息
				insertDto(wxyhDto);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
	}

	/** 
	 * 插入微信用户信息到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(WxyhDto wxyhDto){
		if(StringUtil.isBlank(wxyhDto.getYhid())){
			wxyhDto.setYhid(StringUtil.generateUUID());
		}
		int result = dao.insert(wxyhDto);
        return result != 0;
    }

	/**
	 * 删除用户信息
	 * @param wxyhDto
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void unsubscibe(WxyhDto wxyhDto) {
		try {
			//删除用户信息
			delete(wxyhDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
	}

	/**
	 * 更新、新增用户信息（用户授权信息后）
	 * @param wxyhDto
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void authorize(WxyhDto wxyhDto) {
		try {
			//通过微信ID查询用户信息
			List<WxyhDto> yhList= dao.getWxyhListByWxid(wxyhDto);
			if(yhList !=null && yhList.size() > 0) {
				//更新用户信息
				dao.updateScbjByWxyh(wxyhDto);
			}else{
				//新增用户信息
				insertDto(wxyhDto);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
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
	 * 根据用户id查询微信用户信息
	 */
	@Override
	public WxyhDto WxyhDto(WxyhDto wxyhdto) {
		return dao.WxyhDto(wxyhdto);
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
	 * 获取用户列表
	 * @return
	 */
	public List<WxyhDto> getPagedDtoListXtyh(WxyhDto wxyhdto){
		return dao.getPagedDtoListXtyh(wxyhdto);
	}
	

	/**
	 * 将用户列表用户信息更新到微信用户
	 * @param wxyhdto
	 * @return
	 */
	public boolean updatewxyh(WxyhDto wxyhdto) {
		return dao.updatewxyh(wxyhdto);
	}
	
	/**
	 * 获取微信用户
	 * @return
	 */
	@Override
	public List<WxyhDto> getWxyhDto(){
		// TODO Auto-generated method stub
		return dao.getWxyhDto();
	}

	/**
	 * 获取阿里服务器微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	@Override
	public boolean getUserList(WxyhDto wxyhDto) {
		// TODO Auto-generated method stub
		String url = menuurl+"/wechat/getWeChatUserList";
		RestTemplate t_restTemplate = new RestTemplate();
		while(true){
			List<WxyhDto> wxyhList = WeChatUtil.getUserList(url, t_restTemplate, wxyhDto);
			if(wxyhList != null && wxyhList.size() > 0){
				List<WxyhDto> list = JSONObject.parseArray(((JSON) wxyhList).toJSONString(), WxyhDto.class);
				wxyhDto.setLrsj(list.get(list.size()-1).getLrsj());
				//保存至本地
				dao.insertOrUpdateWxyh(wxyhList);
				if(wxyhList.size() <= 50){
					break;
				}
			}else{
				break;
			}
		}
		return true;
	}

	/**
	 * 根据接收时间获取用户信息（周报）
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<WxyhDto> selectDtoByJsrq(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.selectDtoByJsrq(sjxxDto);
	}

	/**
	 * 根据系统用户id查询微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	public List<WxyhDto> getListByXtyhid(WxyhDto wxyhDto){
		return dao.getListByXtyhid(wxyhDto);
	}
	
	/**
	 * 根据wxid获取同一系统用户或同一unionid的微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	public List<WxyhDto> getListBySameId(WxyhDto wxyhDto){
		return dao.getListBySameId(wxyhDto);
	}

	/**
	 * 更新微信用户和系统用户信息
	 * @param wxyhdto
	 * @return
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean upateYhxx(WxyhDto wxyhdto) {
		// TODO Auto-generated method stub
		//删除已有微信的登录功能
		dao.deleteFromClient(wxyhdto);
		//清空关联数据
		wxyhdto.setWechatid(wxyhdto.getWxid());
		dao.updteXtyhByWxid(wxyhdto);
		dao.updateWxyhByXtyhid(wxyhdto);
		boolean result = updatewxyh(wxyhdto);
		if(!result)
			return false;
		dao.updteXtyh(wxyhdto);
		//添加微信ID的登录功能
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		String str_encode = Encrypt.encrypt(wxyhdto.getWechatid());
		wxyhdto.setMm(bpe.encode(str_encode));
		result = dao.addToClient(wxyhdto);
        return result;
		//将有登陆功能的微信信息存入redis
		//确认redis里有没有用户， 20230115 因为信息不全，数据不存放redis，由登录功能负责存放
//		User redisUser = redisUtil.hugetDto("Users", wxyhdto.getWxid());
//		if(redisUser != null) {
//			redisUser.setSfsd("0");
//			redisUser.setSdtime(null);
//		}else {
//			redisUser = new com.matridx.igams.common.dao.entities.User();
//			redisUser.setSfsd("0");
//			redisUser.setYhm(wxyhdto.getWechatid());
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			Date now = new Date();
//			redisUser.setMmxgsj(sdf.format(now));
//		}
//		
//		redisUtil.hset("Users",wxyhdto.getWechatid(),redisUser,-1);
    }

	/**
	 * 新增或更新微信用户信息
	 * @param wxyhDto
	 */
	@Override
	public void userMod(WxyhDto wxyhDto) {
		// TODO Auto-generated method stub
		try {
			//通过微信ID查询用户信息
			List<WxyhDto> yhList= dao.getWxyhListByWxid(wxyhDto);
			if(yhList !=null && yhList.size() > 0) {
				//更新用户信息
				dao.updateScbjByWxyh(wxyhDto);
			}else{
				//新增用户信息
				insertDto(wxyhDto);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}
	}

	/**
	 * 根据微信ID获取微信用户信息
	 * @param wxyhDto
	 * @return
	 */
	@Override
	public List<WxyhDto> getListByIds(WxyhDto wxyhDto) {
		// TODO Auto-generated method stub
		return dao.getListByIds(wxyhDto);
	}
}
