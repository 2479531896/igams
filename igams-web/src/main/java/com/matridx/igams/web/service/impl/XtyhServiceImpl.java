package com.matridx.igams.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.aliyun.dingtalkhrm_1_0.models.QueryHrmEmployeeDismissionInfoResponseBody;
import com.aliyun.dingtalkworkflow_1_0.models.GetProcessInstanceResponseBody;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeV2ListResponse.EmpFieldDataVo;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeV2ListResponse.EmpRosterFieldVo;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse;
import com.dingtalk.api.response.OapiV2UserGetResponse;
import com.dingtalk.api.response.OapiV2UserListResponse.ListUserResponse;
import com.google.common.collect.Lists;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.InterfaceModel;
import com.matridx.igams.common.dao.entities.InterfaceReturnModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.JsxtqxDto;
import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.igams.common.dao.entities.SpgwDto;
import com.matridx.igams.common.dao.entities.SpgwcyDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.post.IJsxtqxDao;
import com.matridx.igams.common.dao.post.ISpgwcyDao;
import com.matridx.igams.common.dao.post.IXtszDao;
import com.matridx.igams.common.enums.ApproveStatusEnum;
import com.matridx.igams.common.enums.CharacterEnum;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.enums.RosterEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.security.IgamsGrantedAuthority;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.IJsxtqxService;
import com.matridx.igams.common.service.svcinterface.ISpgwService;
import com.matridx.igams.common.service.svcinterface.ISpgwcyService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.hrm.dao.entities.YghmcDto;
import com.matridx.igams.hrm.dao.entities.YghtxxDto;
import com.matridx.igams.hrm.dao.entities.YglzxxDto;
import com.matridx.igams.hrm.dao.post.IYghtxxDao;
import com.matridx.igams.hrm.dao.post.IYglzxxDao;
import com.matridx.igams.hrm.service.svcinterface.IYghmcService;
import com.matridx.igams.web.config.security.CommonRabbitMqConfigTt;
import com.matridx.igams.web.dao.entities.JszyczbDto;
import com.matridx.igams.web.dao.entities.WbcxDto;
import com.matridx.igams.web.dao.entities.WbzyDto;
import com.matridx.igams.web.dao.entities.WbzyqxDto;
import com.matridx.igams.web.dao.entities.XtjsDto;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.dao.entities.XtyhModel;
import com.matridx.igams.web.dao.entities.XtzyDto;
import com.matridx.igams.web.dao.entities.YhjgqxDto;
import com.matridx.igams.web.dao.entities.YhjsDto;
import com.matridx.igams.web.dao.entities.YhjsModel;
import com.matridx.igams.web.dao.entities.YhssjgDto;
import com.matridx.igams.web.dao.post.IJszyczbDao;
import com.matridx.igams.web.dao.post.IWbcxDao;
import com.matridx.igams.web.dao.post.IXtjsDao;
import com.matridx.igams.web.dao.post.IXtyhDao;
import com.matridx.igams.web.dao.post.IXtzyDao;
import com.matridx.igams.web.dao.post.IYhjgqxDao;
import com.matridx.igams.web.dao.post.IYhjsDao;
import com.matridx.igams.web.service.svcinterface.IJszyczbService;
import com.matridx.igams.web.service.svcinterface.IWbzyqxService;
import com.matridx.igams.web.service.svcinterface.IXtyhService;
import com.matridx.igams.web.service.svcinterface.IYhjgqxService;
import com.matridx.igams.web.service.svcinterface.IYhjsService;
import com.matridx.igams.web.service.svcinterface.IYhssjgService;
import com.matridx.igams.wechat.dao.entities.HbqxDto;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import com.matridx.igams.wechat.dao.entities.YhqtxxDto;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.IKhzyfpService;
import com.matridx.igams.wechat.service.svcinterface.IYhqtxxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.encrypt.Encrypt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class XtyhServiceImpl extends BaseBasicServiceImpl<XtyhDto, XtyhModel, IXtyhDao> implements IXtyhService{

	//private String deterpret_8000_url="https://mngs.matridx.com/core/create_user/";//8000系统添加外部用户地址
	//private String deterpret_8002_url="http://dx.matridx.com:8002/BCL/api/create-org/";//测特8002
	@Value("${matridx.fileupload.prefix}")
	private String prefix = null;
	
	@Value("${matridx.fileupload.releasePath}")
	private String releaseFilePath = null;

	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	//是否发送rabbit标记     1：发送
  	@Value("${matridx.rabbit.configflg:1}")
	private String configflg;
	  //8002系统添加外部用户地址
	@Value("${matridx.wechat.biosequrl:}")
	private String biosequrl;
	//8000特检审核系统
	@Value("${matridx.wechat.bioaudurl:}")
	private String bioaudurl;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IWbcxDao wbcxDao;
	@Autowired
	IYhqtxxService yhqtxxService;

	@Autowired
	IYhjsDao yhjsDao;
	@Autowired
	IXtyhDao xtyhDao;
	@Autowired
	IXtjsDao xtjsDao;
	@Autowired
	IXtzyDao xtzyDao;
	@Autowired
	IJszyczbDao jszyczbDao;
	@Autowired
	IJsxtqxDao jsxtqxDao;
	@Autowired
	IXtszDao xtszDao;
	@Autowired
	private DataSource postsqlDataSource;
	@Autowired
	IJgxxService jgxxService;
	@Autowired
	IYhjgqxService yhjgqxService;
	@Autowired
	IYhssjgService yhssjgService;
	//@Autowired
	//private TokenStore tokenStore;
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	IYhjsService yhjsService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	ISpgwcyDao spgwcyDao;
	@Autowired
	ISpgwService spgwService;
	@Autowired
	IYhjgqxDao yhjgqxDao;
	@Autowired
	ICommonService commonService;
	@Autowired
	ISpgwcyService spgwcyService;

	@Autowired
	IHbqxService hbqxService;
	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	IXxdyService xxdyService;
	@Autowired
	IYghmcService yghmcService;
	@Autowired
	IYghtxxDao yghtxxDao;
	@Autowired
	IYglzxxDao yglzxxDao;
	@Autowired
	IWbzyqxService wbzyqxService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	IDdxxglService ddxxglService;
	@Autowired
	IKhzyfpService khzyfpService;
	@Autowired
	IJsxtqxService jsxtqxService;
	@Autowired
	IJszyczbService jszyczbService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	Logger logger = LoggerFactory.getLogger(XtyhServiceImpl.class);

	@Override
	public XtyhDto getDtoByName(String yhm) {
		// TODO Auto-generated method stub
		return dao.getDtoByName(yhm);
	}
	
	@Override
	public List<XtyhDto> getPagedDtoList(XtyhDto xtyhDto){
		//PageHelper.startPage(2, 1);
		List<XtyhDto> xtyhDtos = dao.getPagedDtoList(xtyhDto);
		if(!CollectionUtils.isEmpty(xtyhDtos)){
			List<FjcfbDto> fjcfbDtos = new ArrayList<>();
			for (int i = 0; i < xtyhDtos.size(); i++) {
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setYwid(xtyhDtos.get(i).getYhm());
				fjcfbDto.setXh(String.valueOf(i));
				fjcfbDtos.add(fjcfbDto);
			}
			List<FjcfbDto> t_fjcfbDtos = fjcfbService.getListByYwidAndXh(fjcfbDtos);
			if(!CollectionUtils.isEmpty(t_fjcfbDtos)){
                for (FjcfbDto tFjcfbDto : t_fjcfbDtos) {
                    if (StringUtil.isNotBlank(tFjcfbDto.getFjid())) {
                        xtyhDtos.get(Integer.parseInt(tFjcfbDto.getXh())).setSignflg("1");
                    }
                }
			}
		}
		
		return xtyhDtos;
	}

	//参考JdbcTokenStore 类
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//是否外部用户
		boolean isExtendUser = false;
		//是否从redis获取标记
		boolean isFromRedisflg = false;
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(username)){
			throw new BadCredentialsException("用户："+ username + "或者密码不正确！");
		}
		//先从redis中查询用户信息
		com.matridx.igams.common.dao.entities.User user = redisUtil.hugetDto("Users", username);

		//若用户存在，判断是否为外部用户
		if (user != null) {
			logger.error("登录 redis里获取到用户信息：" + username + " :" + JSON.toJSONString(user));
			//若用户是锁定的，抛异常
			if ("1".equals(user.getSfsd())){
				//因为页面无法得到错误信息，所以需要在这里做一个假的登录成功操作
				//然后交由 IgamsClientDetailService 的 loadClientByClientId抛出异常
				logger.error("用户已经锁定，username="+ username + " 查找到的用户名为：" + user.getYhm());
				if(StringUtil.isBlank(user.getYhm())) {
					return new User("1","1",new ArrayList<>());
				}
				return new User(user.getYhm(),"1",new ArrayList<>());
			}
			//判断是否为外部用户,比如钉钉或者微信
			if("1".equals(user.getLoginFlg())) {
				isExtendUser= true;
			}
			isFromRedisflg = true;
		}else {
			logger.error("登录 redis里未取到相应信息：" + username);
			//如果redis里没有用户信息，从数据库取用户信息
			user = dao.getUserByName(username);
		}

		//删除oauth_access_token中client_id和用户名相同的
		dao.deleteOauthAccessById(username);
		//如果为本系统登录（非微信或者钉钉），但用户不存在的情况，则判断可能为钉钉或者微信
		if(user == null){
			//判断是否为钉钉或者微信用户
			List<com.matridx.igams.common.dao.entities.User> users = xtyhDao.getUserByDdORWechat(username);
			//若用户全为锁定，取第一个；若用户中有不锁定的，取第一个不锁定的用户
			if (!CollectionUtils.isEmpty(users)){
				for (com.matridx.igams.common.dao.entities.User lsuser : users) {
					if ("0".equals(lsuser.getSfsd())){
						user = lsuser;
						break;
					}
				}
				if(user!=null) {
					user.setLoginFlg("1");
					//获取角色信息
					getJsInfoByUser(user,true);
				}
			}

			if(user == null){
				throw new BadCredentialsException("用户："+ username + "或者密码不正确！");
			}
			//为外部用户
			isExtendUser= true;
		}
		
		//如果不是微信或者钉钉登录，则查询权限
		List<String> xtqxList = new ArrayList<>();
		List<String> zyqxList = new ArrayList<>();
		if(!isExtendUser || StringUtil.isBlank(user.getDqjs()) ) {
			//获取用户的所有权限，并且springsecurity 整合		
			user = getAuthorByUser(user,true,xtqxList,zyqxList);
		}else {
			//因为考虑当前角色可能跟 角色List里匹配不上，就是更改过角色，但没有更新。所以需要确认
			//当不为空的时候，确认当前角色是否是 角色List 里的，如果不是重置当前角色
			List<String> jsidStrings = user.getJsids();
			boolean isFind = false;
			if(jsidStrings!=null) {
				for(String jsid: jsidStrings) {
					if(jsid.equals(user.getDqjs())) {
						isFind = true;
						break;
					}
				}
				if(!isFind) {
					user.setDqjs(jsidStrings.get(0));
					//用户有修改同步redis
					user.setUpRedis(true);
				}
			}
			//获取权限信息
			getAuthor(user,zyqxList);
		}

		if(!isFromRedisflg || user.getUpRedis()) {
			//若有查到用户信息，存入redis
			com.matridx.igams.common.dao.entities.User redisUser = new com.matridx.igams.common.dao.entities.User();
			redisUser.setWbcxid(user.getWbcxid());
			redisUser.setClient_id(user.getClient_id());
			redisUser.setYhm(user.getYhm());
			redisUser.setZsxm(user.getZsxm());
			redisUser.setSfsd(user.getSfsd());
			redisUser.setSdtime(user.getSdtime());
			redisUser.setJstime(user.getJstime());
			//redisUser.setJsids(user.getJsids());
			redisUser.setJsmcs(user.getJsmcs());
			redisUser.setYhid(user.getYhid());
			redisUser.setDqjs(user.getDqjs());
			redisUser.setDqjsdm(user.getDqjsdm());
			redisUser.setDqjsmc(user.getDqjsmc());
			redisUser.setDqjsdwxdbj(user.getDqjsdwxdbj());
			redisUser.setDdid(user.getDdid());
			redisUser.setCkqx(user.getCkqx());
			redisUser.setJgid(user.getJgid());
			redisUser.setMm(user.getMm());
			redisUser.setMmxgsj(user.getMmxgsj());
			redisUser.setGwmc(user.getGwmc());
			redisUser.setDdtxlj(user.getDdtxlj());
			redisUser.setEmail(user.getEmail());
			redisUser.setUnionid(user.getUnionid());
			//因为是用微信ID或 钉钉ID登录的，所以需要设置标记
			redisUser.setLoginFlg(user.getLoginFlg());
			logger.error(" loadUserByUsername redisUtil.hset Users :" + username + ":" + JSON.toJSONString(redisUser) );
			redisUtil.hset("Users",username,JSON.toJSONString(redisUser),-1);
		}
		GrantedAuthority grantedAuthority = new IgamsGrantedAuthority(user);
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		
		grantedAuthorities.add(grantedAuthority);
		//更新用户登录信息
		//XtyhModel xtyhModel = new XtyhModel();
		//xtyhModel.setYhid(user.getYhid());
		//dao.updateLastLogin(xtyhModel);
		//返回一个springSecurity需要的用户对象
		logger.debug("-------------------user信息-----------------"+JSON.toJSONString(user));
		if(isExtendUser) {
            return new User(username,user.getMm(),grantedAuthorities);
        } else {
            return new User(user.getYhm(),user.getMm(),grantedAuthorities);
        }
	}
	
	/**
	 * 客户端用户登录的时候验证用户信息
	 */
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		// TODO Auto-generated method stub
	/*	if(StringUtil.isBlank(clientId)){
			throw new BadCredentialsException("用户为空！");
		}
		//把用户名转为小写
		String lowerUserName = clientId.toLowerCase();
		//查找用户信息
		XtyhDto xtyhDto = dao.getDtoByName(lowerUserName);
		*/
		
		
		return null;
	}
	
	/**
	 * 登录用户验证,暂时未启用，启用时需设置 IgamsSecurityConfig 的 authenticationProvider 方法
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public XtyhDto authUser(XtyhDto dto){
		// TODO Auto-generated method stub
		//把用户名转为小写
		String lowerUserName = dto.getYhm().toLowerCase();
		//查找用户信息
		XtyhDto xtyhDto = dao.getDtoByName(lowerUserName);
		//用户不存在的情况，抛出异常
		if(xtyhDto == null){
			throw new BadCredentialsException("用户名或者密码不正确！");
		}
		
		//获取用户的所有权限，并且springsecurity 整合
		//Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		//根据用户信息查找权限信息
		YhjsDto searchDto = new YhjsDto();
		searchDto.setYhid(xtyhDto.getYhid());
		List<YhjsDto> yhjsDtos = yhjsDao.getDtoList(searchDto);
		
		xtyhDto.setYhjsDtos(yhjsDtos);
		
		if(yhjsDtos==null){
			throw new BadCredentialsException("用户："+ lowerUserName + "没有权限！");
		}
		//返回一个springSecurity需要的用户对象
		return xtyhDto;
	}

	/**
	 * 对密码进行加密后保存到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insert(XtyhModel model){
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		model.setMm(bpe.encode(model.getMm()));
		int result = dao.insert(model);

        return result > 0;
    }
	
	/**
	 * 对密码进行加密后保存到数据库
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean insertDto(XtyhDto xtyhDto){
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		//初始密码
        XtszDto xtszDto = xtszDao.getDtoById("user.initpw");
		xtyhDto.setMm(bpe.encode(xtszDto.getSzz()));
		xtyhDto.setYhid(StringUtil.generateUUID());
		if(StringUtil.isNotBlank(xtyhDto.getJgid())) {
			YhssjgDto yhssjgDto=new YhssjgDto();
			yhssjgDto.setYhid(xtyhDto.getYhid());
			yhssjgDto.setJgid(xtyhDto.getJgid());
			yhssjgDto.setXh("1");
			yhssjgDto.setPrefix(prefixFlg);
			boolean result1=yhssjgService.insert(yhssjgDto);
			if(!result1) {
                return false;
            }
			if("1".equals(configflg)) {
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.SSJG_ADD.getCode() + JSONObject.toJSONString(yhssjgDto));
			}
		}
		if(!CollectionUtils.isEmpty(xtyhDto.getXtjsDtos())){
			List<YhjsModel> yhjsList= new ArrayList<>();
			for(int i=0;i<xtyhDto.getXtjsDtos().size();i++){
				if(xtyhDto.getXtjsDtos().get(i).getJsid()!=null) {
					YhjsModel yhjsModel = new YhjsModel();
					yhjsModel.setYhid(xtyhDto.getYhid());
					yhjsModel.setJsid(xtyhDto.getXtjsDtos().get(i).getJsid());
					yhjsModel.setPrefix(prefixFlg);
					yhjsList.add(yhjsModel);
					if("1".equals(configflg)) {
						amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.YHJS_ADD.getCode() + JSONObject.toJSONString(yhjsModel));
					}
				}
				//添加审批岗位
				List<SpgwcyDto> spgwcyList= new ArrayList<>();
				//需要同步的审批岗位
				List<SpgwcyDto> t_spgwcyList = new ArrayList<>();
				if(!CollectionUtils.isEmpty(xtyhDto.getXtjsDtos().get(i).getSpgwDtos())) {
					for (int j = 0; j < xtyhDto.getXtjsDtos().get(i).getSpgwDtos().size(); j++){
						if(xtyhDto.getXtjsDtos().get(i).getSpgwDtos().get(j).getGwid()!=null) {
							SpgwcyDto spgwcyDto=new SpgwcyDto();
							spgwcyDto.setGwid(xtyhDto.getXtjsDtos().get(i).getSpgwDtos().get(j).getGwid());
							spgwcyDto.setJsid(xtyhDto.getXtjsDtos().get(i).getJsid());
							spgwcyDto.setYhid(xtyhDto.getYhid());
							spgwcyDto.setPrefix(prefixFlg);
							spgwcyList.add(spgwcyDto);
							//根据是否广播判断是否发送rabbitmq
							SpgwDto spgwDto = spgwService.getDtoById(xtyhDto.getXtjsDtos().get(i).getSpgwDtos().get(j).getGwid());
							if("1".equals(spgwDto.getSfgb())){
								t_spgwcyList.add(spgwcyDto);
							}
						}
					}
					boolean isSuccess = spgwcyDao.toSelected(spgwcyList);
					if(isSuccess && !CollectionUtils.isEmpty(t_spgwcyList) && "1".equals(configflg)){
						amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.GWCY_ADD.getCode() + JSONObject.toJSONString(t_spgwcyList));
					}
				}
				
			}
			yhjsDao.batchInsert(yhjsList);
			xtyhDto.setDqjs(yhjsList.get(0).getJsid());
		}
		//在插入角色后才执行用户插入，主要是为了更新当前角色
		int result = dao.insert(xtyhDto);
		//添加用户机构权限
		if(!CollectionUtils.isEmpty(xtyhDto.getYhjgqxDtos())) {
			for (int i = 0; i < xtyhDto.getYhjgqxDtos().size(); i++){
				List<YhjgqxDto> yhjgqxList= new ArrayList<>();
				if(xtyhDto.getYhjgqxDtos().get(i).getJgids()!=null&&xtyhDto.getYhjgqxDtos().get(i).getJsid()!=null) {
					for (int j = 0; j < xtyhDto.getYhjgqxDtos().get(i).getJgids().size(); j++){
						YhjgqxDto yhjgqxDto=new YhjgqxDto();
						yhjgqxDto.setYhid(xtyhDto.getYhid());
						yhjgqxDto.setXh(j+1+"");
						yhjgqxDto.setJsid(xtyhDto.getYhjgqxDtos().get(i).getJsid());
						yhjgqxDto.setJgid(xtyhDto.getYhjgqxDtos().get(i).getJgids().get(j));
						yhjgqxDto.setPrefix(prefixFlg);
						yhjgqxList.add(yhjgqxDto);
					}
					if(!CollectionUtils.isEmpty(yhjgqxList)){
						yhjgqxDao.insertList(yhjgqxList);
						if("1".equals(configflg)) {
							amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.JGQX_ADD.getCode() + JSONObject.toJSONString(yhjgqxList));
						}
					}
				}	
			}
		}
		/*ClientDto clientDetail = new ClientDto();
		clientDetail.setYhid(xtyhDto.getYhid());
		clientDetail.setYhm(xtyhDto.getYhm());
		clientDetail.setClientSecret(xtyhDto.getMm());
		Set<String> authorizedGrantTypes = new HashSet<String>();
		authorizedGrantTypes.add("client_credentials");
		authorizedGrantTypes.add("refresh_token");
		authorizedGrantTypes.add("password");
		clientDetail.setAuthorizedGrantTypes(authorizedGrantTypes );
		clientDetail.setAdditionalInformation(new HashMap<String, Object>());
		
		Set<String> scope = new HashSet<String>();
		scope.add("all");
		clientDetail.setScope(scope);*/

		/*JdbcClientDetailsService clientService = new JdbcClientDetailsService(postsqlDataSource);
		clientService.addClientDetails(clientDetail);*/
		
		dao.addClientDetails(xtyhDto);
		
		xtyhDto.setPrefix(prefixFlg);
		if(result > 0){
			if("1".equals(configflg)) {
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.XTYH_MOD.getCode() + JSONObject.toJSONString(xtyhDto));
			}
			return true;
		}
		return false;
	}

	/**
	 * 获取主页显示内容
	 */
	@Override
	public Map<String, Object> initMenu(String tokenid) {
		// TODO Auto-generated method stub
		Encrypt ecp = new Encrypt();
		//先把页面上传递过来的tokenid进行转换
		String token_id = ecp.extractTokenKey(tokenid);
		//再根据转换后的token查询用户信息
		XtyhDto xtyhDto = dao.getDtoByToken(token_id);
		
		if(xtyhDto == null) {
            return null;
        }
		List<XtzyDto> xtzyDtos = null;
		//如果用户的上次角色信息没有的话，重新从权限表获取数据，随机取一个角色
		List<YhjsDto> yhjsDtos = yhjsDao.getDtoListById(xtyhDto.getYhid());
		if(StringUtil.isBlank(xtyhDto.getDqjs())){
			if(!CollectionUtils.isEmpty(yhjsDtos)){
				xtzyDtos = xtzyDao.getDtoListById(yhjsDtos.get(0).getJsid());
				XtyhDto t_xtyhDto = new XtyhDto();
				t_xtyhDto.setYhid(xtyhDto.getYhid());
				t_xtyhDto.setDqjs(yhjsDtos.get(0).getJsid());
				t_xtyhDto.setXgry(xtyhDto.getYhid());
				dao.update(t_xtyhDto);
				xtyhDto.setDqjs(yhjsDtos.get(0).getJsid());
			}
		}else{
			boolean isFind = false;
            for (YhjsDto yhjsDto : yhjsDtos) {
                if (yhjsDto.getJsid().equals(xtyhDto.getDqjs())) {
                    isFind = true;
                    break;
                }
            }
			if(!isFind){
				xtyhDto.setDqjs(yhjsDtos.get(0).getJsid());
				XtyhDto t_xtyhDto = new XtyhDto();
				t_xtyhDto.setYhid(xtyhDto.getYhid());
				t_xtyhDto.setDqjs(yhjsDtos.get(0).getJsid());
				t_xtyhDto.setXgry(xtyhDto.getYhid());
				dao.update(t_xtyhDto);
			}
			xtzyDtos = xtzyDao.getDtoListById(xtyhDto.getDqjs());
		}
		
		Map<String, List<XtzyDto>> menuMap = dealMenuList(xtzyDtos);
		
		if(menuMap == null) {
            return null;
        }
		//用于判断是否是OA新首页
		if (!CollectionUtils.isEmpty(yhjsDtos)&&StringUtil.isNotBlank(xtyhDto.getDqjs())){
			List<YhjsDto> collect = yhjsDtos.stream().filter(e -> e.getJsid().equals(xtyhDto.getDqjs())).collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(collect)){
				xtyhDto.setSylxdm(collect.get(0).getSylxdm());
				xtyhDto.setSylxlj(collect.get(0).getSylxlj());
			}
		}
		Map<String, Object> res = new HashMap<>();
		
		res.put("xtyhDto", xtyhDto);
		res.put("topmenu", menuMap.get("topmenu"));
		res.put("menuList", menuMap.get("menuList"));
		res.put("mobiletopmenu", menuMap.get("mobiletopmenu"));
		res.put("jsxxList", yhjsDtos);
		res.put("vuemenu",menuMap.get("vuemenu"));
		
		return res;
	}
	
	/**
	 * 切换用户角色
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public Map<String, Object> switchMenu(XtyhDto xtyhDto){
		//更换当前角色
		dao.update(xtyhDto);
		
		//根据用户ID查询最新的用户信息，包括更改后的角色
		XtyhDto t_xtyhDto = dao.getDtoById(xtyhDto.getYhid());
		
		if(t_xtyhDto == null) {
            return null;
        }
		
		//如果用户的上次角色信息没有的话，重新从权限表获取数据，随机取一个角色
		List<YhjsDto> yhjsDtos = yhjsDao.getDtoListById(xtyhDto.getYhid());

		List<XtzyDto> xtzyDtos = xtzyDao.getDtoListById(xtyhDto.getDqjs());
		
		// 重新new一个token，因为Authentication中的权限是不可变的,更改上下文信息
		changeToken(t_xtyhDto);
		
		Map<String, List<XtzyDto>> menuMap = dealMenuList(xtzyDtos);
		
		if(menuMap == null) {
            return null;
        }
		//用于判断是否是OA新首页
		if (!CollectionUtils.isEmpty(yhjsDtos)&&StringUtil.isNotBlank(xtyhDto.getDqjs())){
			List<YhjsDto> collect = yhjsDtos.stream().filter(e -> e.getJsid().equals(xtyhDto.getDqjs())).collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(collect)){
				t_xtyhDto.setSylxdm(collect.get(0).getSylxdm());
				t_xtyhDto.setSylxlj(collect.get(0).getSylxlj());
			}
		}
		Map<String, Object> res = new HashMap<>();
		
		t_xtyhDto.setAccess_token(xtyhDto.getAccess_token());
		t_xtyhDto.setRef(xtyhDto.getRef());
		res.put("xtyhDto", t_xtyhDto);
		
		res.put("topmenu", menuMap.get("topmenu"));
		res.put("menuList", menuMap.get("menuList"));
		res.put("mobiletopmenu", menuMap.get("mobiletopmenu"));
		res.put("jsxxList", yhjsDtos);
		res.put("vuemenu", menuMap.get("vuemenu"));
		return res;
	}
	
	/**
	 * 错误更新次数,因为IP无法获取，暂时无法使用
	 * @param user
	 */
	public void dealWithFailedLogin(com.matridx.igams.common.dao.entities.User user){
//		XtyhModel xtyh = new XtyhModel();
		
		//错误次数(为空则是0)
		int cwcs = Integer.parseInt(StringUtil.isBlank(user.getCwcs())?"0":user.getCwcs());
		//上次登录时间
		String dlsj = user.getDlsj();
		//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String dqsj = sdf.format(new Date());
		//最大限制数
        XtszDto xtszDto = xtszDao.getDtoById("login.maxErrorLimit");
        int maxLimit = 5;
        if(xtszDto != null && StringUtil.isNotBlank(xtszDto.getSzz())){
        	maxLimit = Integer.parseInt(xtszDto.getSzz());
        }
		
		try{
			//增加一次,判断当前时间与上次登录时间是否一致
			if(dqsj.equals(dlsj)){//若为第一次登录，则错误次数为1
				cwcs++;
				//判断是否大于等于最大限制数
//				if(cwcs>=maxLimit){
//					xtyh.setSfsd("1");
//					xtyh.setCwcs("0");
//				}else{
//					xtyh.setCwcs(String.valueOf(cwcs));
//				}
				
				//在当日判断错误次数（若错误次数数大于3，则报提示）
				if (maxLimit > 3 && cwcs >= 3) {
					if (cwcs < maxLimit) {
						String num = String.valueOf(maxLimit - cwcs);
						throw new BadCredentialsException("请注意，您还有"+ num + "次机会尝试，之后账户将被锁定。");
					}else {
						throw new BadCredentialsException("您已经超过最高限制！");
					}
				}
			}
//			else{
//				//日期不同则为错误第一次
//				xtyh.setCwcs("1");
//			}
		}finally{
	/*		xtyh.setDlh(user.getDlh());//登录号
	        xtyh.setDlip(request.getRemoteAddr());//登录ip
	        updateForLogined(xtyh);*/
		}
	}
	

	/**
	 * 根据页面填写信息更新用户信息，用户角色，还有审批权限信息
	 * @param xtyhDto
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updateYhxx(XtyhDto xtyhDto){
		//获取原有用户的用户名，跟现有用户名进行比较，如果有更改，则更新 oauth表里的数据信息
		XtyhDto y_XtyhDto = dao.getDto(xtyhDto);
		if(!y_XtyhDto.getYhm().equals(xtyhDto.getYhm())){
			xtyhDto.setYyhm(y_XtyhDto.getYhm());
			dao.updateOauthClinetId(xtyhDto);
		}
		YhqtxxDto yhqtxxDto = new YhqtxxDto();
		yhqtxxDto.setYhid(xtyhDto.getYhid());
		yhqtxxDto.setZj(xtyhDto.getZj());
		yhqtxxDto.setXgry(xtyhDto.getXgry());
		yhqtxxService.updateZj(yhqtxxDto);
		YhssjgDto yhssjgDto=new YhssjgDto();
		yhssjgDto.setIds(xtyhDto.getYhid());
		yhssjgService.deleteYhssjgByYhid(yhssjgDto);
		yhssjgDto.setYhid(xtyhDto.getYhid());
		yhssjgDto.setJgid(xtyhDto.getJgid());
		yhssjgDto.setXh("1");
		boolean result1=yhssjgService.insert(yhssjgDto);
		if("1".equals(configflg)) {
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.SSJG_ADD.getCode() + JSONObject.toJSONString(yhssjgDto));
		}
		if(!result1) {
            return false;
        }
		//判断用户是否锁定,如果没有锁定，更新错误次数为0
		if("0".equals(xtyhDto.getSfsd())) {
			xtyhDto.setCwcs("0");
		}
		//若用户锁定，则根据用户名删除access_token表中信息
		if("1".equals(xtyhDto.getSfsd())) {
			dao.deleteOauthAccessById(xtyhDto.getYhm());
			dao.deleteInCompleteTaskById(xtyhDto.getYhid());
		}
		//删除已有用户角色信息
		yhjsDao.deleteById(xtyhDto.getYhid());
		com.matridx.igams.common.dao.entities.User user = new com.matridx.igams.common.dao.entities.User();
		user.setYhid(xtyhDto.getYhid());
		user.setPrefix(prefixFlg);
		if("1".equals(configflg)) {
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.YHJS_DEL.getCode() + JSONObject.toJSONString(user));
		}
		//先查询原有岗位成员，判断是否发送删除消息
		List<SpgwcyDto> s_spgwcyList = commonService.selectSpgwcyByYhid(xtyhDto.getYhid());
		if(!CollectionUtils.isEmpty(s_spgwcyList)){
			for(int i = s_spgwcyList.size()-1; i>=0; i--){
				s_spgwcyList.get(i).setPrefix(prefixFlg);
				if(!"1".equals(s_spgwcyList.get(i).getSfgb())){
					s_spgwcyList.remove(i);
				}
			}
		}
		spgwcyDao.deleteByYhid(xtyhDto.getYhid());
		if(!CollectionUtils.isEmpty(s_spgwcyList) && "1".equals(configflg)){
//			amqpTempl.convertAndSend("sys.igams", "sys.igams.spgwcy.batchdel",JSONObject.toJSONString(s_spgwcyList));
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.GWCY_DEL.getCode() + JSONObject.toJSONString(s_spgwcyList));
		}
		//添加用户角色表
		if(!CollectionUtils.isEmpty(xtyhDto.getXtjsDtos())){
			List<YhjsModel> yhjsList= new ArrayList<>();
			boolean isExist =false;
			for(int i=0;i<xtyhDto.getXtjsDtos().size();i++){
				if(xtyhDto.getXtjsDtos().get(i).getJsid()!=null) {
					YhjsModel yhjsModel = new YhjsModel();
					yhjsModel.setYhid(xtyhDto.getYhid());
					yhjsModel.setJsid(xtyhDto.getXtjsDtos().get(i).getJsid());
					yhjsModel.setPrefix(prefixFlg);
					yhjsList.add(yhjsModel);
					if(yhjsModel.getJsid().equals(y_XtyhDto.getDqjs())) {
						isExist = true;
					}
					if("1".equals(configflg)) {
						amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.YHJS_ADD.getCode() + JSONObject.toJSONString(yhjsModel));
					}
				}
				//添加审批岗位
				List<SpgwcyDto> spgwcyList= new ArrayList<>();
				//需要同步的审批岗位
				List<SpgwcyDto> t_spgwcyList = new ArrayList<>();
				if(!CollectionUtils.isEmpty(xtyhDto.getXtjsDtos().get(i).getSpgwDtos())) {
					for (int j = 0; j < xtyhDto.getXtjsDtos().get(i).getSpgwDtos().size(); j++){
						if(xtyhDto.getXtjsDtos().get(i).getSpgwDtos().get(j).getGwid()!=null) {
							SpgwcyDto spgwcyDto=new SpgwcyDto();
							spgwcyDto.setGwid(xtyhDto.getXtjsDtos().get(i).getSpgwDtos().get(j).getGwid());
							spgwcyDto.setJsid(xtyhDto.getXtjsDtos().get(i).getJsid());
							spgwcyDto.setYhid(xtyhDto.getYhid());
							spgwcyDto.setPrefix(prefixFlg);
							spgwcyList.add(spgwcyDto);
							//根据是否广播判断是否发送rabbitmq
							SpgwDto spgwDto = spgwService.getDtoById(xtyhDto.getXtjsDtos().get(i).getSpgwDtos().get(j).getGwid());
							if("1".equals(spgwDto.getSfgb())){
								t_spgwcyList.add(spgwcyDto);
							}
						}
					}
					boolean isSuccess = spgwcyDao.toSelected(spgwcyList);
					if(isSuccess && !CollectionUtils.isEmpty(t_spgwcyList) && "1".equals(configflg)){
//						amqpTempl.convertAndSend("sys.igams", "sys.igams.spgwcy.insert",JSONObject.toJSONString(t_spgwcyList));
						amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.GWCY_ADD.getCode() + JSONObject.toJSONString(t_spgwcyList));
					}
				}
			}
			yhjsDao.batchInsert(yhjsList);
			//如果当前用户角色不在角色列表中，则更新当前角色
			if(!isExist) {
				xtyhDto.setDqjs(yhjsList.get(0).getJsid());
			}
		}
		//更新用户信息
		int result = dao.update(xtyhDto);
		//添加用户机构权限
		YhjgqxDto yhjgqxDto_t=new YhjgqxDto();
		yhjgqxDto_t.setIds(xtyhDto.getYhid());
		yhjgqxService.deleteJgqxxxByYhid(yhjgqxDto_t);
		if(!CollectionUtils.isEmpty(xtyhDto.getYhjgqxDtos())) {
			for (int i = 0; i < xtyhDto.getYhjgqxDtos().size(); i++){
				List<YhjgqxDto> yhjgqxList= new ArrayList<>();
				if(!CollectionUtils.isEmpty(xtyhDto.getYhjgqxDtos())) {
					if(!CollectionUtils.isEmpty(xtyhDto.getYhjgqxDtos().get(i).getJgids()) && StringUtil.isNotBlank(xtyhDto.getYhjgqxDtos().get(i).getJsid())) {
						for (int j = 0; j < xtyhDto.getYhjgqxDtos().get(i).getJgids().size(); j++){
							YhjgqxDto yhjgqxDto=new YhjgqxDto();
							yhjgqxDto.setYhid(xtyhDto.getYhid());
							yhjgqxDto.setXh(j+1+"");
							yhjgqxDto.setJsid(xtyhDto.getYhjgqxDtos().get(i).getJsid());
							yhjgqxDto.setJgid(xtyhDto.getYhjgqxDtos().get(i).getJgids().get(j));
							yhjgqxDto.setPrefix(prefixFlg);
							yhjgqxList.add(yhjgqxDto);
						}		
						yhjgqxDao.insertList(yhjgqxList);
						if("1".equals(configflg)) {
							amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.JGQX_ADD.getCode() + JSONObject.toJSONString(yhjgqxList));
						}
					}
				}
			}
		}
		xtyhDto.setPrefix(prefixFlg);
		if(result > 0){
			//删除，不再重新获取，在登录的时候再把所有数据获取设置到redis里
			redisUtil.hdel("Users",y_XtyhDto.getYhm());
			if (StringUtil.isNotBlank(y_XtyhDto.getDdid())){
				redisUtil.hdel("ClientDtos",y_XtyhDto.getDdid());
			}
			if (StringUtil.isNotBlank(y_XtyhDto.getWechatid())){
				redisUtil.hdel("ClientDtos",y_XtyhDto.getWechatid());
			}
			if("1".equals(configflg)) {
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.XTYH_MOD.getCode() + JSONObject.toJSONString(xtyhDto));
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 修改密码
	 * @return
	 * @throws BusinessException 
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean updatePass(XtyhDto xtyhDto) throws BusinessException {
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		XtyhDto tXtyhDto = dao.getDtoById(xtyhDto.getYhid());
		if (tXtyhDto==null || !bpe.matches(xtyhDto.getYmm(), tXtyhDto.getMm())) {
            throw new BusinessException("ICOM99045");
        }
		xtyhDto.setMm(bpe.encode(xtyhDto.getMm()));
		// 更新用户信息
		int result = dao.updatePass(xtyhDto);
		xtyhDto.setYhm(tXtyhDto.getYhm());
		dao.updateOauthPass(xtyhDto);
		
		//删除oauth_access_token中client_id和用户名相同的
		dao.deleteOauthAccessById(tXtyhDto.getYhm());

		redisUtil.hdel("Users",xtyhDto.getYhm());
		if (StringUtil.isNotBlank(tXtyhDto.getDdid())){
			redisUtil.hdel("ClientDtos",tXtyhDto.getDdid());
		}
		if (StringUtil.isNotBlank(tXtyhDto.getWechatid())){
			redisUtil.hdel("ClientDtos",tXtyhDto.getWechatid());
		}

		return result > 0;
	}
	
	/**
	 * 切换用户需要更新Token
	 * @param t_xtyhDto
	 */
	private void changeToken(XtyhDto t_xtyhDto){
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		
		// 获取当前认证了的 principal(当事人),或者 request token (令牌)
		// 如果没有认证，会是 null
		OAuth2Authentication authentication = (OAuth2Authentication)securityContext.getAuthentication();

		// 获取当事人信息对象，返回结果是 Object 类型，但实际上可以是应用程序自定义的带有更多应用相关信息的某个类型。
		// 很多情况下，该对象是 Spring Security 核心接口 UserDetails 的一个实现类，你可以把 UserDetails 想像
		// 成我们数据库中保存的一个用户信息到 SecurityContextHolder 中 Spring Security 需要的用户信息格式的
		// 一个适配器。
		Collection<GrantedAuthority> c_authorities = authentication.getAuthorities();
		List<GrantedAuthority> authorities = new ArrayList<>(c_authorities);
		IgamsGrantedAuthority authority = (IgamsGrantedAuthority)authorities.get(0);
		
		com.matridx.igams.common.dao.entities.User user = authority.getYhxx();
		
		//设置当前角色信息
		user.setDqjs(t_xtyhDto.getDqjs());
		user.setDqjsdm(t_xtyhDto.getDqjsdm());
		user.setDqjsmc(t_xtyhDto.getDqjsmc());

		user.setDqjsdwxdbj(t_xtyhDto.getDwxdbj());
		//因为切换角色，所以需要更新redis
		com.matridx.igams.common.dao.entities.User redisUser = new com.matridx.igams.common.dao.entities.User();
		redisUser.setYhm(user.getYhm());
		redisUser.setZsxm(user.getZsxm());
		redisUser.setSfsd(user.getSfsd());
		redisUser.setSdtime(user.getSdtime());
		redisUser.setJstime(user.getJstime());
		//redisUser.setJsids(user.getJsids());
		redisUser.setJsmcs(user.getJsmcs());
		redisUser.setYhid(user.getYhid());
		redisUser.setDqjs(user.getDqjs());
		redisUser.setDqjsdm(user.getDqjsdm());
		redisUser.setDqjsmc(user.getDqjsmc());
		redisUser.setDqjsdwxdbj(user.getDqjsdwxdbj());
		redisUser.setDdid(user.getDdid());
		redisUser.setCkqx(user.getCkqx());
		redisUser.setJgid(user.getJgid());
		redisUser.setMm(user.getMm());
		redisUser.setMmxgsj(user.getMmxgsj());
		redisUser.setGwmc(user.getGwmc());
		redisUser.setDdtxlj(user.getDdtxlj());
		redisUser.setEmail(user.getEmail());
		redisUser.setUnionid(user.getUnionid());
		//因为是用微信ID或 钉钉ID登录的，所以需要设置标记
		redisUser.setLoginFlg(user.getLoginFlg());
		redisUser.setWbcxid(user.getWbcxid());
		redisUtil.hset("Users",redisUser.getYhm(),JSON.toJSONString(redisUser),-1);

		if (StringUtil.isNotBlank(t_xtyhDto.getMiniappid()) && StringUtil.isNotBlank(t_xtyhDto.getDdid())){
			DBEncrypt p = new DBEncrypt();
			String dCode = p.dCode(t_xtyhDto.getMiniappid());
			redisUser.setLoginFlg("1");
			logger.error(" changeToken redisUtil.hset Users :" + t_xtyhDto.getDdid()+"@"+dCode + ":" + JSON.toJSONString(redisUser) );
			redisUtil.hset("Users",t_xtyhDto.getDdid()+"@"+dCode,JSON.toJSONString(redisUser),-1);
		}
		QxModel t_qxDto = new QxModel();
		t_qxDto.setJsid(t_xtyhDto.getDqjs());
		//查询当前角色的资源操作表
		Object o_jsqx = redisUtil.hget("Users_QxModel",user.getDqjs());
		if(o_jsqx!=null) {
			List<QxModel> qxModels = JSON.parseArray((String)o_jsqx,QxModel.class);
			//获取资源权限
			user.setQxModels(qxModels);

		}else {
			List<QxModel> qxModels = jszyczbDao.getQxModelList(t_qxDto);
			//设置当前角色的资源操作表
			user.setQxModels(qxModels);
		}
		//获取资源权限
		Object o_NobtnlistModels = redisUtil.hget("Users_NobtnlistModels",user.getDqjs());
		if(o_NobtnlistModels!=null) {
			List<QxModel> nobtnmenuList = JSON.parseArray((String)o_NobtnlistModels,QxModel.class);
			user.setNobtnlistModels(nobtnmenuList);

		}else {
			//查询当前角色的系统资源表
			List<QxModel> nobtnmenuList = jszyczbDao.getNoButtonMenu(t_qxDto);
			user.setNobtnlistModels(nobtnmenuList);
		}
		
		//查询用户的系统权限
		/*JsxtqxDto t_xtqxDto = new JsxtqxDto();
		t_xtqxDto.setJsid(t_xtyhDto.getDqjs());
		List<JsxtqxDto> xtqxModels = jsxtqxDao.getQxModelList(t_xtqxDto);
		//设置当前角色的系统权限表
		user.setXtqxDtos(xtqxModels);*/
		
		//因当前角色等信息是保留在数据库的，则直接更改数据库信息。**是否合适以后讨论
	    JdbcTokenStore jdbcToken = new JdbcTokenStore(postsqlDataSource);
	    OAuth2AccessToken existingAccessToken = jdbcToken.getAccessToken(authentication);
	    jdbcToken.storeAccessToken(existingAccessToken, authentication);
	}
	
	/**
	 * 根据所有资源信息，拆分出一级菜单，以及第一个一级菜单下的二级菜单
	 * @param xtzyDtos
	 * @return
	 */
	private Map<String, List<XtzyDto>> dealMenuList(List<XtzyDto> xtzyDtos){
		if(CollectionUtils.isEmpty(xtzyDtos)) {
            return null;
        }
		
		List<XtzyDto> firstMenu = new ArrayList<>();
		List<XtzyDto> subMenu = null;
		List<XtzyDto> mobileMenu = new ArrayList<>();
		List<XtzyDto> vueMenu = null;//vue框架返回菜单

        for (XtzyDto dto : xtzyDtos) {
            if (StringUtil.isBlank(dto.getFjd())) {
                firstMenu.add(dto);
            } else if (firstMenu.size() == 1) {
                subMenu = addMenuList(subMenu, dto);
            }
            if ("00".equals(dto.getXskzbj())) {
                mobileMenu = addMenuList(mobileMenu, (XtzyDto) dto.clone());
            }
            vueMenu = addMenuList(vueMenu, (XtzyDto) dto.clone());
        }
		Map<String, List<XtzyDto>> res = new HashMap<>();
		res.put("topmenu", firstMenu);
		res.put("menuList", subMenu);
		res.put("mobiletopmenu", mobileMenu);
		res.put("vuemenu",vueMenu);
		
		return res;
	}
	
	/**
	 * 往链表里增加菜单信息
	 * @param menuList
	 * @param zyDto
	 * @return
	 */
	private List<XtzyDto> addMenuList(List<XtzyDto> menuList,XtzyDto zyDto){
		if(zyDto == null) {
            return menuList;
        }
		if(CollectionUtils.isEmpty(menuList)){
			menuList = new ArrayList<>();
			menuList.add(zyDto);
			return menuList;
		}
		if(menuList.get(0).getCdcc().equals(zyDto.getCdcc())) {
			menuList.add(zyDto);
		}else {
			menuList.get(menuList.size()-1).setSubzydtos(addMenuList(menuList.get(menuList.size()-1).getSubzydtos(),zyDto));
		}
		return menuList;
	}
	
	/**
	 * 根据用户ID,获取用户的权限信息。现主要是登录验证时使用
	 * @param user
	 * @return
	 */
	public com.matridx.igams.common.dao.entities.User getAuthorByUser(com.matridx.igams.common.dao.entities.User user,boolean isThrowFlg,List<String> xtqxList,List<String> zyqxList){
		//获取角色信息
		getJsInfoByUser(user,isThrowFlg);
		//获取权限信息,判断是否存在当前角色，不存在不去获取权限信息
		if(StringUtil.isNotBlank(user.getDqjs())) {
			//如果当前角色的系统资源为空不再查询
			if(!zyqxList.contains(user.getDqjs())) {
				getAuthor(user,zyqxList);
			}
			if (!CollectionUtils.isEmpty(user.getJsids())){
				List<JsxtqxDto> xtqxModels = new ArrayList<>();
				for (String jsid : user.getJsids()) {
					Object o_xtqx = redisUtil.hget("Users_Xtqx",jsid);
					if(o_xtqx != null) {
						List<JsxtqxDto> js_xtqxModels = JSON.parseArray((String)o_xtqx,JsxtqxDto.class);
						//判断资源是否为空，判断当前角色是否再系统权限为空的list，如果资源为空，角色资源权限为空的list不存在当前角色，从系统中查
						if(CollectionUtils.isEmpty(js_xtqxModels) && !xtqxList.contains(jsid)) {
							//查询用户的系统权限
							JsxtqxDto t_xtqxDto = new JsxtqxDto();
							t_xtqxDto.setJsid(jsid);
							js_xtqxModels = jsxtqxDao.getQxModelList(t_xtqxDto);
							//判断当前角色的系统权限是否为空，如果为空放入为空的list
							if(CollectionUtils.isEmpty(js_xtqxModels)) {
								xtqxList.add(jsid);
							}
							redisUtil.hset("Users_Xtqx",jsid,JSON.toJSONString(js_xtqxModels),-1);
						}
						xtqxModels.addAll(js_xtqxModels);
					}else if(!xtqxList.contains(jsid)){
						//查询用户的系统权限
						JsxtqxDto t_xtqxDto = new JsxtqxDto();
						t_xtqxDto.setJsid(jsid);
						List<JsxtqxDto> js_xtqxModels = jsxtqxDao.getQxModelList(t_xtqxDto);
						//判断当前角色的系统权限是否为空，如果为空放入为空的list
						if(CollectionUtils.isEmpty(js_xtqxModels)) {
							xtqxList.add(jsid);
						}
						redisUtil.hset("Users_Xtqx",jsid,JSON.toJSONString(xtqxModels),-1);
						xtqxModels.addAll(js_xtqxModels);
					}
				}
				//设置当前角色的系统权限表
				user.setXtqxDtos(xtqxModels);
			}else {
				Object o_xtqx = redisUtil.hget("Users_Xtqx",user.getDqjs());
				if(o_xtqx != null) {
					List<JsxtqxDto> xtqxModels = JSON.parseArray((String)o_xtqx,JsxtqxDto.class);
					//判断资源是否为空，判断当前角色是否再系统权限为空的list，如果资源为空，角色资源权限为空的list不存在当前角色，从系统中查
					if(CollectionUtils.isEmpty(xtqxModels) && !xtqxList.contains(user.getDqjs())) {
						//查询用户的系统权限
						JsxtqxDto t_xtqxDto = new JsxtqxDto();
						t_xtqxDto.setJsid(user.getDqjs());
						xtqxModels = jsxtqxDao.getQxModelList(t_xtqxDto);
						//判断当前角色的系统权限是否为空，如果为空放入为空的list
						if(CollectionUtils.isEmpty(xtqxModels)) {
							xtqxList.add(user.getDqjs());
						}
						redisUtil.hset("Users_Xtqx",user.getDqjs(),JSON.toJSONString(xtqxModels),-1);
					}
					//设置当前角色的系统权限表
					user.setXtqxDtos(xtqxModels);
				}else if(!xtqxList.contains(user.getDqjs())){
					//查询用户的系统权限
					JsxtqxDto t_xtqxDto = new JsxtqxDto();
					t_xtqxDto.setJsid(user.getDqjs());
					List<JsxtqxDto> xtqxModels = jsxtqxDao.getQxModelList(t_xtqxDto);
					//判断当前角色的系统权限是否为空，如果为空放入为空的list
					if(CollectionUtils.isEmpty(xtqxModels)) {
						xtqxList.add(user.getDqjs());
					}
					//设置当前角色的系统权限表
					user.setXtqxDtos(xtqxModels);

					redisUtil.hset("Users_Xtqx",user.getDqjs(),JSON.toJSONString(xtqxModels),-1);
				}
			}
		}		
		return user;
	}
	
	
	
	/**
	 * 根据用户ID,获取用户的权限信息。（项目启动时）
	 * @param user
	 * @return
	 */
	public com.matridx.igams.common.dao.entities.User getRunAuthorByUser(com.matridx.igams.common.dao.entities.User user,List<String> xtqxList,List<String> zyqxList,List<YhjsDto> yhqxDtos){
		//获取角色信息
		getRunJsInfoByUser(user,yhqxDtos);
		//获取权限信息,判断是否存在当前角色，不存在不去获取权限信息
		if(StringUtil.isNotBlank(user.getDqjs())) {
			//如果当前角色的系统资源为空不再查询
			if(!zyqxList.contains(user.getDqjs())) {
				getAuthor(user,zyqxList);
			}			
			Object o_xtqx = redisUtil.hget("Users_Xtqx",user.getDqjs());
			if(o_xtqx != null) {
				List<JsxtqxDto> xtqxModels = JSON.parseArray((String)o_xtqx,JsxtqxDto.class);
				//判断资源是否为空，判断当前角色是否再系统权限为空的list，如果资源为空，角色资源权限为空的list不存在当前角色，从系统中查
				if(CollectionUtils.isEmpty(xtqxModels) && !xtqxList.contains(user.getDqjs())) {
					//查询用户的系统权限
					JsxtqxDto t_xtqxDto = new JsxtqxDto();
					t_xtqxDto.setJsid(user.getDqjs());
					xtqxModels = jsxtqxDao.getQxModelList(t_xtqxDto);
					//判断当前角色的系统权限是否为空，如果为空放入为空的list
					if(CollectionUtils.isEmpty(xtqxModels)) {
						xtqxList.add(user.getDqjs());
					}
					redisUtil.hset("Users_Xtqx",user.getDqjs(),JSON.toJSONString(xtqxModels),-1);
				}
				//设置当前角色的系统权限表
				user.setXtqxDtos(xtqxModels);
			}else if(!xtqxList.contains(user.getDqjs())){
				//查询用户的系统权限
				JsxtqxDto t_xtqxDto = new JsxtqxDto();
				t_xtqxDto.setJsid(user.getDqjs());
				List<JsxtqxDto> xtqxModels = jsxtqxDao.getQxModelList(t_xtqxDto);
				//判断当前角色的系统权限是否为空，如果为空放入为空的list
				if(CollectionUtils.isEmpty(xtqxModels)) {
					xtqxList.add(user.getDqjs());
				}
				//设置当前角色的系统权限表
				user.setXtqxDtos(xtqxModels);
				
				redisUtil.hset("Users_Xtqx",user.getDqjs(),JSON.toJSONString(xtqxModels),-1);
			}
		}		
		return user;
	}

	/**
	 * 根据用户信息获取角色信息
	 * @param user
	 * @param isThrowFlg
	 */
	public void getJsInfoByUser(com.matridx.igams.common.dao.entities.User user,boolean isThrowFlg){
		if(CollectionUtils.isEmpty(user.getJsids()) || StringUtil.isBlank(user.getDqjs())) {
			//用户有修改同步redis
			user.setUpRedis(true);
			//没有用户ID，不允许登录
			if(StringUtil.isBlank(user.getYhid())) {
				if(StringUtil.isBlank(user.getClient_id())) {
					if(isThrowFlg) {
	                    throw new BadCredentialsException("用户："+ user.getYhm() + "没有权限！");
	                } else {
	                    return;
	                }
				}else {
					return;
				}
			}
				
			//根据用户信息查找权限信息
			YhjsDto searchDto = new YhjsDto();
			searchDto.setYhid(user.getYhid());
			List<YhjsDto> yhqxDtos = yhjsDao.getDtoList(searchDto);
			
			if(CollectionUtils.isEmpty(yhqxDtos)){
				if(isThrowFlg) {
                    throw new BadCredentialsException("用户："+ user.getYhm() + "没有权限！");
                } else {
                    return;
                }
			}
			
			YhjsDto dq_yhjs = null;
			List<String> t_jsids = new ArrayList<>();
			List<String> t_jsmcs = new ArrayList<>();
            for (YhjsDto yhqxDto : yhqxDtos) {
                if (yhqxDto.getJsid().equals(user.getDqjs())) {
                    dq_yhjs = yhqxDto;
                }
                t_jsids.add(yhqxDto.getJsid());
                t_jsmcs.add(yhqxDto.getJsmc());
            }
			if(dq_yhjs==null) {
                dq_yhjs = yhqxDtos.get(0);
				com.matridx.igams.common.dao.entities.User user_redis = redisUtil.hugetDto("Users", user.getYhm());
				if(user_redis!=null && dq_yhjs!=null) {
					user_redis.setDqjs(dq_yhjs.getJsid());
					redisUtil.hset("Users",user.getYhm(),JSON.toJSONString(user_redis),-1);
				}
            }
	
			//保存用户的角色列表
			user.setJsids(t_jsids);
			user.setJsmcs(t_jsmcs);
			//设置当前角色信息
			user.setDqjs(dq_yhjs.getJsid());
			user.setDqjsdm(dq_yhjs.getJsdm());
			user.setDqjsmc(dq_yhjs.getJsmc());
			user.setDqjsdwxdbj(dq_yhjs.getDwxdbj());
			//设置当前角色仓库权限
			user.setCkqx(dq_yhjs.getCkqx());		
			//设置用户机构信息 2019-10-21 用于机构权限限制
			user.setJgid(dq_yhjs.getJgid());
		}else {
			//当不为空的时候，确认当前角色是否是 角色List 里的，如果不是重置当前角色
			List<String> jsidStrings = user.getJsids();
			boolean isFind = false;
			for(String jsid: jsidStrings) {
				if(jsid.equals(user.getDqjs())) {
					isFind = true;
					break;
				}
			}
			if(!isFind) {
				user.setDqjs(jsidStrings.get(0));
				//用户有修改同步redis
				user.setUpRedis(true);
			}
		}
	}
	
	
	/**
	 * 根据用户信息获取角色信息（项目启动时）
	 * @param user
	 * @param yhqxDtos
	 */
	public void getRunJsInfoByUser(com.matridx.igams.common.dao.entities.User user,List<YhjsDto> yhqxDtos){
		if(StringUtil.isBlank(user.getDqjs()) || CollectionUtils.isEmpty(user.getJsids())) {
			YhjsDto dq_yhjs = null;
			List<String> t_jsids = new ArrayList<>();
			List<String> t_jsmcs = new ArrayList<>();
            for (YhjsDto yhqxDto : yhqxDtos) {
                if (yhqxDto.getJsid().equals(user.getDqjs()) && yhqxDto.getYhid().equals(user.getYhid())) {
                    dq_yhjs = yhqxDto;
                    t_jsids.add(yhqxDto.getJsid());
                    t_jsmcs.add(yhqxDto.getJsmc());
                }
            }
			if(dq_yhjs==null) {
				return;
			}
				
	
			//保存用户的角色列表
			user.setJsids(t_jsids);
			user.setJsmcs(t_jsmcs);
			//设置当前角色信息
			user.setDqjs(dq_yhjs.getJsid());
			user.setDqjsdm(dq_yhjs.getJsdm());
			user.setDqjsmc(dq_yhjs.getJsmc());
			user.setDqjsdwxdbj(dq_yhjs.getDwxdbj());
			//设置当前角色仓库权限
			user.setCkqx(dq_yhjs.getCkqx());		
			//设置用户机构信息 2019-10-21 用于机构权限限制
			user.setJgid(dq_yhjs.getJgid());
		}else {
			//当不为空的时候，确认当前角色是否是 角色List 里的，如果不是重置当前角色
			List<String> jsidStrings = user.getJsids();
			boolean isFind = false;
			for(String jsid: jsidStrings) {
				if(jsid.equals(user.getDqjs())) {
					isFind = true;
					break;
				}
			}
			if(!isFind) {
				user.setDqjs(jsidStrings.get(0));
			}
		}
	}
	
	/**
	 * 获取权限信息,如果当前用户已经在系统里存在，则不需要再去获取当前角色信息.
	 * @param user
	 */
	public void getAuthor(com.matridx.igams.common.dao.entities.User user,List<String> zyqxList){
		Object o_jsqx = null;
		
		if(StringUtil.isNotBlank(user.getDqjs())) {
            o_jsqx = redisUtil.hget("Users_QxModel",user.getDqjs());
        }
		
		if(o_jsqx != null) {
			List<QxModel> qxmodels = JSON.parseArray((String)o_jsqx,QxModel.class);
			//获取资源权限
			Object o_NobtnlistModels = redisUtil.hget("Users_NobtnlistModels",user.getDqjs());

			List<QxModel> nobtnmenuList = JSON.parseArray((String)o_NobtnlistModels,QxModel.class);
			//设置当前角色的资源操作表
			user.setQxModels(qxmodels);

			user.setNobtnlistModels(nobtnmenuList);
			
		}else {
			//查询当前角色的资源操作表
			QxModel t_qxDto = new QxModel();
			if(StringUtil.isNotBlank(user.getDqjs())) {
                t_qxDto.setJsid(user.getDqjs());
            } else {
                t_qxDto.setJsid(user.getJsids().get(0));
            }
			List<QxModel> qxModels = jszyczbDao.getQxModelList(t_qxDto);

			if(!CollectionUtils.isEmpty(qxModels)) {
				List<QxModel> nobtnmenuList = new ArrayList<>();
				List<QxModel> temp_qxModels = new ArrayList<>();
				//String prezyid = "";
                for (QxModel qxModel : qxModels) {
                    if ("menupower".equals(qxModel.getCzdm())) {
                        nobtnmenuList.add(qxModel);
                    } else {
                        temp_qxModels.add(qxModel);
                    }
                }
				user.setNobtnlistModels(nobtnmenuList);
				//设置当前角色的资源操作表
				user.setQxModels(temp_qxModels);
			}else {
				zyqxList.add(user.getDqjs());
			}
			
			redisUtil.hset("Users_QxModel",user.getDqjs(),JSON.toJSONString(user.getQxModels()),-1);
			redisUtil.hset("Users_NobtnlistModels",user.getDqjs(),JSON.toJSONString(user.getNobtnlistModels()),-1);
		}
		//List<QxModel> nobtnmenuList = jszyczbDao.getNoButtonMenu(t_qxDto);
	}

	@Override
	public boolean initPass(XtyhDto xtyhDto) {
		//获取初始化密码
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		XtszDto xtszDto = xtszDao.getDtoById("user.initpw");
		xtyhDto.setMm(bpe.encode(xtszDto.getSzz()));
		int result = 0;
		if(!CollectionUtils.isEmpty(xtyhDto.getIds())){
			for (int i = 0; i < xtyhDto.getIds().size(); i++) {
				xtyhDto.setYhid(xtyhDto.getIds().get(i));
				XtyhDto tXtyhDto = dao.getDtoById(xtyhDto.getIds().get(i));
				//更新用户信息
				int updatePass = dao.updatePass(xtyhDto);
				result = result+updatePass;
				xtyhDto.setYhm(tXtyhDto.getYhm());
				dao.updateOauthPass(xtyhDto);
			}
		}
		return result > 0;
	}

//  原本钉钉更新按钮逻辑
//	@Override
//	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
//	public boolean initDingDing(XtyhDto xtyhDto) {
//		
//		String token = talkUtil.getToken();
//		List<Department> list = talkUtil.getAllDepartments(token);
//		
//		List<XtyhDto> XtyhDto_list = dao.getUserList();
//		List<JgxxDto> JgxxDto_list = jgxxService.getJgxxList();
//		//List<YhjgqxDto> Yhjgqx_List = yhjgqxService.getYhjgqxList();
//		List<YhssjgDto> Yhssjg_List = yhssjgService.getYhssjgList();
//		boolean isNotFind;
//		//获取部门
//		for (Department department : list) {
//			List<Userlist> userlist = talkUtil.getAllUsers(token,department.getId());
//		    //添加机构信息表数据,判断是否存在
//			isNotFind = true;
//		    for (int i = JgxxDto_list.size()-1; i >= 0; i--) {
//				if(JgxxDto_list.get(i).getJgid().equals(String.valueOf(department.getId()))){
//					isNotFind = false;
//					JgxxDto_list.remove(JgxxDto_list.get(i));
//				}
//			}
//		    if(isNotFind){
//		    	JgxxDto jgxxDto = new JgxxDto();
//		    	jgxxDto.setJgid(String.valueOf(department.getId()));
//		    	jgxxDto.setJgmc(department.getName());
//		    	jgxxDto.setLrry(xtyhDto.getXgry());
//		    	boolean success = jgxxService.insert(jgxxDto);
//		    	if(!success){
//		    		return false;
//		    	}
//		    	jgxxDto.setPrefix(prefixFlg);
//		    	amqpTempl.convertAndSend("sys.igams", "sys.igams.jgxx.update",JSONObject.toJSONString(jgxxDto));
//		    	JgxxDto_list.add(jgxxDto);
//		    }
//			//获取成员
//			for (Userlist user : userlist) {
//				//与数据库数据对比更新
//			    //String preYhid = " ";
//				for (int i = XtyhDto_list.size()-1; i >= 0; i--) {
//					if(XtyhDto_list.get(i).getYhm().equals(user.getJobnumber())){
//						
//						//添加用户机构权限表数据
//						/*
//						 * isFind = true; for (int j = 0; j < Yhjgqx_List.size(); j++) {
//						 * if(StringUtil.isNotBlank(Yhjgqx_List.get(j).getJsid()) &&
//						 * Yhjgqx_List.get(j).getYhid().equals(XtyhDto_list.get(i).getYhid()) &&
//						 * Yhjgqx_List.get(j).getJsid().equals(XtyhDto_list.get(i).getJsid())){ isFind =
//						 * false; } } if(isFind){ YhjgqxDto yhjgqxDto = new YhjgqxDto();
//						 * yhjgqxDto.setYhid(XtyhDto_list.get(i).getYhid());
//						 * yhjgqxDto.setJgid(String.valueOf(department.getId()));
//						 * yhjgqxDto.setJsid(XtyhDto_list.get(i).getJsid()); yhjgqxDto.setXh("1");
//						 * boolean success = yhjgqxService.insert(yhjgqxDto); if(!success){ return
//						 * false; } Yhjgqx_List.add(yhjgqxDto); }
//						 */
//						
//					    //添加用户所属机构表数据
//						isNotFind = true;
//						for (int j = 0; j < Yhssjg_List.size(); j++) {
//							if(Yhssjg_List.get(j).getYhid().equals(XtyhDto_list.get(i).getYhid())){
//								isNotFind = false;
//								break;
//							}
//						}
//						if(isNotFind){
//							YhssjgDto yhssjgDto = new YhssjgDto();
//							yhssjgDto.setYhid(XtyhDto_list.get(i).getYhid());
//							yhssjgDto.setJgid(String.valueOf(department.getId()));
//							yhssjgDto.setXh("1");
//							boolean success = yhssjgService.insert(yhssjgDto);
//							if(!success){
//					    		return false;
//					    	}
//							yhssjgDto.setPrefix(prefixFlg);
//					    	amqpTempl.convertAndSend("sys.igams", "sys.igams.yhssjg.insert",JSONObject.toJSONString(yhssjgDto));
//							Yhssjg_List.add(yhssjgDto);
//						}
//
//						//更新用户表ddid和gwmc,钉钉头像路径
//						if(StringUtil.isBlank(XtyhDto_list.get(i).getDdid()) || StringUtil.isBlank(XtyhDto_list.get(i).getGwmc()) || StringUtils.isBlank(XtyhDto_list.get(i).getDdtxlj())){
//							XtyhDto_list.get(i).setDdid(user.getUserid());
//							XtyhDto_list.get(i).setGwmc(user.getPosition());
//							XtyhDto_list.get(i).setDdtxlj(user.getAvatar());
//							dao.updateDingDing(XtyhDto_list.get(i));
//							
//							XtyhDto dd_xtyhDto = new XtyhDto();
//							dd_xtyhDto.setYhm(user.getUserid());
//							dd_xtyhDto.setYhid(user.getUserid());
//
//							List<String> yhms = new ArrayList<String>();
//							yhms.add(user.getUserid());
//							dd_xtyhDto.setYhms(yhms);
//							List<XtyhDto> ttDtos = dao.getclientByYhm(dd_xtyhDto);
//							if(ttDtos == null || ttDtos.size() ==0) {
//								BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
//								String str_encode = Encrypt.encrypt(user.getUserid());
//								dd_xtyhDto.setMm(bpe.encode(str_encode));
//								dao.addClientDetails(dd_xtyhDto);
//							}
//							XtyhDto_list.get(i).setPrefix(prefixFlg);
//							amqpTempl.convertAndSend("sys.igams", "sys.igams.xtyh.update",JSONObject.toJSONString(XtyhDto_list.get(i)));
//						}
//						break;
//						//preYhid = XtyhDto_list.get(i).getYhid();
//					}
//				}
//			}
//		}
//		return true;
//	}
	
	/**
	 * 用来定时任务调用钉钉更新按钮
	 * @return
	 */
	public boolean initDingDing() {
		XtyhDto xtyhDto = new XtyhDto();
		String xgry = "TimingTaskToInit";
		xtyhDto.setXgry(xgry);
		xtyhDto.setLrry(xgry);
		return this.initDingDing(xtyhDto);
	}
	
	/**
	 *更改后的钉钉更新按钮逻辑： （ 任务：定时更新用户列表，增加钉钉中的新用户到oa系统中，并且更新所有用户的ddid等信息
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean initDingDing(XtyhDto xtyhDto) {
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		List<XtyhDto> xtyhList = dao.getDBAllUserList();//查找的是包括删除标记为1的所有数据
		List<JgxxDto> jgxxList = jgxxService.getAllJgxxList();
		List<YhssjgDto> yhssjgList = yhssjgService.getYhssjgList();
		List<String> ty_jgids=new ArrayList<>();
		for(JgxxDto dto:jgxxList){
			if(StringUtil.isNotBlank(dto.getSfty())&&"1".equals(dto.getSfty())){
				ty_jgids.add(dto.getJgid());
			}
		}
		boolean isNotFind;

		DBEncrypt dbEncrypt = new DBEncrypt();
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setLx(CharacterEnum.DINGDING.getCode());
		wbcxDto.setSftb("1");
		List<WbcxDto> dtoList = wbcxDao.getDtoList(wbcxDto);
		//修改机构信息
		List<JgxxDto> jgxxDtos = new ArrayList<>();
		List<String> allAddJg = new ArrayList<>();//用来提醒指定人员，新增了机构信息，维护相应数据
		for (WbcxDto dto : dtoList) {
			try {
				List<OapiV2DepartmentListsubResponse.DeptBaseResponse> departmentList = talkUtil.getAllDepartmentsV2ByKeyAndSecret(dto.getAppid(),dto.getSecret());//得到钉钉所有部门
				List<String> jgids = new ArrayList<>();

				//当获取不到部门，则代表所有人员都在一个默认部门下，所以设置为1
				if(!CollectionUtils.isEmpty(departmentList)) {
					OapiV2DepartmentListsubResponse.DeptBaseResponse department = new OapiV2DepartmentListsubResponse.DeptBaseResponse();
					department.setDeptId(1L);
					departmentList.add(department);
					//倒序遍历解决一个用户属于多个机构问题（更新最上级机构为用户所属机构）
					for(int jg = departmentList.size()-1; jg >=0; jg--) {
						jgids.add(String.valueOf(departmentList.get(jg).getDeptId()));
						JgxxDto jgxxDto_wb = new JgxxDto();
						jgxxDto_wb.setWbcxid(dto.getWbcxid());
						jgxxDto_wb.setJgmc(departmentList.get(jg).getName());
						jgxxDto_wb.setJgid(String.valueOf(departmentList.get(jg).getDeptId()));
						jgxxDto_wb.setFjgid(String.valueOf(departmentList.get(jg).getParentId()));
						jgxxDto_wb.setPrefix(prefixFlg);
						jgxxDtos.add(jgxxDto_wb);
						List<ListUserResponse> userlist = talkUtil.getAllUsersV2ByKeyAndSecret(dto.getAppid(),dto.getSecret(), departmentList.get(jg).getDeptId());
						isNotFind = true;
						// Set<JgxxDto> jgxxModSet = new HashSet<>();
						// List<JgxxDto> jgxxModList = new ArrayList<>();
						//更新部门名称变更的数据
						//目的：钉钉中的每个部门对比oa的数据库，如果钉钉存在了新的部门，而oa还没有，则在oa中添加该部门
						//jgxxlist最后保存的是所有钉钉新加入的部门list
						for(int i = jgxxList.size()-1; i >=0 ; i--) {
							if(jgxxList.get(i).getJgid().equals(String.valueOf(departmentList.get(jg).getDeptId()))) {
								//当钉钉和数据库的部门名称不相同的时候，加入set中后续更新数据库的部门名称
								//jgxxModList存放的是钉钉部门名称更改的部门数据
								//-------------由于增加全局处理修改机构信息所以注释
								// if (StringUtil.isNotBlank(jgxxList.get(i).getJgmc())){
								// 	if ( !jgxxList.get(i).getJgmc().equals(department.getName()) ){
								// 		JgxxDto jgxxModDto = new JgxxDto();
								// 		jgxxModDto.setJgid(jgxxList.get(i).getJgid());
								// 		jgxxModDto.setJgmc(department.getName());
								// 		jgxxModDto.setPrefix(prefixFlg);
								// 		jgxxModSet.add(jgxxModDto);
								// 	}
								// }
								isNotFind = false;
								jgxxList.remove(jgxxList.get(i));
							}
						}
						// if (jgxxModSet.size()>0){
						// 	jgxxModList.addAll(jgxxModSet);
						// 	jgxxService.updateJgxxList(jgxxModList);
						// }
						//处理钉钉新增的部门
						if(isNotFind && departmentList.get(jg).getDeptId()!=null && departmentList.get(jg).getDeptId() != 1L) {
							JgxxDto jgxxDto = new JgxxDto();
							jgxxDto.setJgid(String.valueOf(departmentList.get(jg).getDeptId()));
							jgxxDto.setJgmc(departmentList.get(jg).getName());
							jgxxDto.setLrry(xtyhDto.getXgry());
							jgxxDto.setSfty("0");
							jgxxDto.setFbsbj(urlPrefix);
							boolean success = jgxxService.insert(jgxxDto);
							if(!success) {
								return false;
							}
							allAddJg.add(jgxxDto.getJgmc());
							jgxxDto.setPrefix(prefixFlg);
							if("1".equals(configflg)) {
								amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.jgxx.update",JSONObject.toJSONString(jgxxDto));
							}

							jgxxList.add(jgxxDto);
						}
						for(ListUserResponse user: userlist) {
							//如果是部门主管
							if (user.getLeader()){
								for (JgxxDto jgxxDto : jgxxDtos) {
									//判断当前部门
									if (jgxxDto.getJgid().equals(jgxxDto_wb.getJgid())){
										//修改部门主管
										if (StringUtil.isNotBlank(jgxxDto.getBmzgs())){
											jgxxDto.setBmzgs(jgxxDto.getBmzgs()+","+user.getUserid());
										}else {
											jgxxDto.setBmzgs(user.getUserid());
										}
										break;
									}
								}
							}
							//如果用户的工号为空，则不处理
							if (user == null || StringUtil.isBlank(user.getJobNumber())){
								continue;
							}
							boolean isAdd = true;
							//比较钉钉中每个部门中的所有员工和oa数据库中的用户，找到用户后则比较用户和所属机构
							for(int i = xtyhList.size()-1; i >=0; i--) {
								if (user.getJobNumber().equals(xtyhList.get(i).getYhm())) {
									//更新用户其它信息
									logger.error("yhm:" + xtyhList.get(i).getYhm() + " job:" + user.getJobNumber() + " zsxm:" + xtyhList.get(i).getZsxm() + " name:" + user.getName());
									isAdd = false;
									//================================
									//处理所属机构
									isNotFind = true;
									for (YhssjgDto value : yhssjgList) {
										if (value.getYhid().equals(xtyhList.get(i).getYhid())) {
											isNotFind = false;
											break;
										}
									}
									if (isNotFind) {
										YhssjgDto yhssjgDto = new YhssjgDto();
										yhssjgDto.setYhid(xtyhList.get(i).getYhid());
										String jgid=String.valueOf(departmentList.get(jg).getDeptId());
										boolean isLastFind=false;
										while(!isLastFind&&ty_jgids.contains(jgid)){
											for(int x=0;x<departmentList.size();x++){
												if(jgid.equals(String.valueOf(departmentList.get(x).getDeptId()))){
													if(departmentList.get(x).getParentId()!=null&&!"1".equals(String.valueOf(departmentList.get(x).getParentId()))){
														jgid=String.valueOf(departmentList.get(x).getParentId());
														break;
													}else{
														jgid=String.valueOf(departmentList.get(jg).getDeptId());
														isLastFind=true;
														break;
													}
												}
											}
										}
										yhssjgDto.setJgid(jgid);
										yhssjgDto.setXh("1");
										boolean success = yhssjgService.insert(yhssjgDto);
										if (!success) {
											return false;
										}
										yhssjgDto.setPrefix(prefixFlg);
										if ("1".equals(configflg)) {
											amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate", RabbitEnum.SSJG_ADD.getCode() + JSONObject.toJSONString(yhssjgDto));
										}
										yhssjgList.add(yhssjgDto);
									}
									//=====================================
									if (StringUtil.isNotBlank(user.getJobNumber()))//针对高级科学家Liangliang，加一个yhm不为空的才更新的条件
									{
										//更新用户所属机构
										if (StringUtil.isNotBlank(xtyhList.get(i).getJgid()) && (!String.valueOf(departmentList.get(jg).getDeptId()).equals(xtyhList.get(i).getJgid()) ||ty_jgids.contains(xtyhList.get(i).getJgid()))) {
											YhssjgDto yhssjgDto = new YhssjgDto();
											yhssjgDto.setYhid(xtyhList.get(i).getYhid());
											yhssjgDto.setXh(xtyhList.get(i).getXh());
											String jgid=String.valueOf(departmentList.get(jg).getDeptId());
											boolean isLastFind=false;
											while(!isLastFind&&ty_jgids.contains(jgid)){
												for(int x=0;x<departmentList.size();x++){
													if(jgid.equals(String.valueOf(departmentList.get(x).getDeptId()))){
														if(departmentList.get(x).getParentId()!=null&&!"1".equals(String.valueOf(departmentList.get(x).getParentId()))){
															jgid=String.valueOf(departmentList.get(x).getParentId());
															break;
														}else{
															jgid=String.valueOf(departmentList.get(jg).getDeptId());
															isLastFind=true;
															break;
														}
													}
												}
											}
											yhssjgDto.setJgid(jgid);
											yhssjgDto.setPrefix(prefixFlg);
											yhssjgService.update(yhssjgDto);
											if ("1".equals(configflg)) {
												amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate", RabbitEnum.SSJG_MOD.getCode() + JSONObject.toJSONString(yhssjgDto));
											}
										}
										//更新用户表ddid和gwmc,钉钉头像路径
										if (StringUtil.isBlank(xtyhList.get(i).getDdid()) || !(user.getTitle()).equals(xtyhList.get(i).getGwmc()) || StringUtil.isBlank(xtyhList.get(i).getDdtxlj())|| StringUtil.isBlank(xtyhList.get(i).getEmail())|| StringUtil.isBlank(xtyhList.get(i).getUnionid())) {
											xtyhList.get(i).setDdid(user.getUserid());
											xtyhList.get(i).setGwmc(user.getTitle());
											xtyhList.get(i).setDdtxlj(user.getAvatar());
											//企业邮箱
											xtyhList.get(i).setEmail(user.getOrgEmail());
											xtyhList.get(i).setUnionid(user.getUnionid());
											dao.updateDingDing(xtyhList.get(i));

											//更新时增加用户角色信息(xtyh和yhjs表)
											//角色添加方式：去查询信息对应表的原信息(存着岗位职称)，找到原信息对应的角色ID
											XtyhDto jsmod_xtyh = xtyhList.get(i);
											String jsidStr = xxdyService.getJsidByGwmc(jsmod_xtyh.getGwmc());
											YhjsDto yhjsdto = new YhjsDto();
											yhjsdto.setYhid(jsmod_xtyh.getYhid());
											yhjsdto.setJsid(jsidStr);
											if (StringUtil.isNotBlank(jsidStr)) {
												jsmod_xtyh.setDqjs(jsidStr);
												List<YhjsModel> yhjs_judgeRepeat = yhjsDao.isRepeatJsByYhid(jsmod_xtyh.getYhid());
												if (CollectionUtils.isEmpty(yhjs_judgeRepeat)) {
													yhjsDao.insert(yhjsdto);
													if ("1".equals(configflg)) {
														amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate", RabbitEnum.YHJS_ADD.getCode() + JSONObject.toJSONString(yhjsdto));
													}
												}
											}

											XtyhDto dd_xtyhDto = new XtyhDto();
											dd_xtyhDto.setYhm(user.getUserid() + (StringUtil.isBlank(dto.getMiniappid())?"":"@"+dbEncrypt.dCode(dto.getMiniappid())));
											dd_xtyhDto.setYhid(user.getUserid());

											List<String> yhms = new ArrayList<>();
											yhms.add(user.getUserid() + (StringUtil.isBlank(dto.getMiniappid())?"":"@"+dbEncrypt.dCode(dto.getMiniappid())));
											dd_xtyhDto.setYhms(yhms);
											List<XtyhDto> ttDtos = dao.getclientByYhm(dd_xtyhDto);
											if (CollectionUtils.isEmpty(ttDtos)) {
												dd_xtyhDto.setMm(bpe.encode(user.getUserid()));
												dao.addClientDetails(dd_xtyhDto);
											}
											xtyhList.get(i).setPrefix(prefixFlg);
											//将用户信息存入redis
											com.matridx.igams.common.dao.entities.User redisUser = new com.matridx.igams.common.dao.entities.User();
											redisUser.setYhm(xtyhList.get(i).getYhm());
											redisUser.setWbcxid(dto.getWbcxid());
											redisUser.setZsxm(xtyhList.get(i).getZsxm());
											redisUser.setSfsd(xtyhList.get(i).getSfsd());
											redisUser.setSdtime(xtyhList.get(i).getSdtime());
											redisUser.setJstime(xtyhList.get(i).getJstime());
											//redisUser.setJsids(xtyhList.get(i).getJsids());
											redisUser.setYhid(xtyhList.get(i).getYhid());
											redisUser.setDqjs(xtyhList.get(i).getDqjs());
											redisUser.setDqjsdm(xtyhList.get(i).getDqjsdm());
											redisUser.setDqjsmc(xtyhList.get(i).getDqjsmc());
											redisUser.setDdid(xtyhList.get(i).getDdid());
											redisUser.setWechatid(xtyhList.get(i).getWechatid());
											redisUser.setJgid(xtyhList.get(i).getJgid());
											redisUser.setMm(xtyhList.get(i).getMm());
											redisUser.setMmxgsj(xtyhList.get(i).getMmxgsj());
											redisUser.setGwmc(xtyhList.get(i).getGwmc());
											redisUser.setDdtxlj(xtyhList.get(i).getDdtxlj());
											redisUser.setEmail(xtyhList.get(i).getEmail());
											redisUser.setUnionid(xtyhList.get(i).getUnionid());
											redisUtil.hset("Users", redisUser.getYhm(), JSON.toJSONString(redisUser), -1);
											if ("1".equals(configflg)) {
												amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate", RabbitEnum.XTYH_MOD.getCode() + JSONObject.toJSONString(xtyhList.get(i)));
											}
										}
										//=======================================
									}
									break;
								}
							}
							if(isAdd) {
								//添加用户xtyhDto
								String yhid = StringUtil.generateUUID();
								XtyhDto xtyhdto = new XtyhDto();
								xtyhdto.setYhm(user.getJobNumber());
								xtyhdto.setZsxm(user.getName());
								//设置初始密码
								XtszDto xtszDto = xtszDao.getDtoById("user.initpw");
								xtyhdto.setMm(bpe.encode(xtszDto.getSzz()));//新增用户的初始密码为000000
								xtyhdto.setSfsd("0");
								xtyhdto.setYhid(yhid);
								xtyhdto.setDdid(user.getUserid());
								xtyhdto.setGwmc(user.getTitle());
								xtyhdto.setDdtxlj(user.getAvatar());
								xtyhdto.setEmail(user.getOrgEmail());
								xtyhdto.setUnionid(user.getUnionid());

								//新增时添加用户角色信息((xtyh和yhjs表))
								//角色添加方式：去查询信息对应表的原信息(存着岗位职称)，找到原信息对应的角色ID
								String jsid_str = xxdyService.getJsidByGwmc(xtyhdto.getGwmc());
								YhjsDto yhjsDto = new YhjsDto();
								yhjsDto.setYhid(xtyhdto.getYhid());
								yhjsDto.setJsid(jsid_str);
								if(StringUtil.isNotBlank(jsid_str)) {
									xtyhdto.setDqjs(jsid_str);
									yhjsDao.insert(yhjsDto);
									if("1".equals(configflg)) {
										amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.YHJS_ADD.getCode() + JSONObject.toJSONString(yhjsDto));
									}
								}

								boolean addNewStaff = insert(xtyhdto);
								if(!addNewStaff) {
									return false;
								}
								//更新用户其它信息
								YhqtxxDto yhqtxxDto = new YhqtxxDto();
								yhqtxxDto.setYhid(yhid);
								yhqtxxDto.setWbcxid(dto.getWbcxid());
								yhqtxxDto.setLrry(xtyhDto.getXgry());
								yhqtxxDto.setPrefix(prefixFlg);
								yhqtxxService.insert(yhqtxxDto);
								if ("1".equals(configflg)) {
									amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate", RabbitEnum.YHQT_ADD.getCode() + JSONObject.toJSONString(yhqtxxDto));
								}
								xtyhdto.setPrefix(prefixFlg);
								//将用户信息存入redis
								com.matridx.igams.common.dao.entities.User redisUser = new com.matridx.igams.common.dao.entities.User();
								redisUser.setYhm(xtyhdto.getYhm());
								redisUser.setWbcxid(dto.getWbcxid());
								redisUser.setZsxm(xtyhdto.getZsxm());
								redisUser.setSfsd(xtyhdto.getSfsd());
								redisUser.setSdtime(xtyhdto.getSdtime());
								redisUser.setJstime(xtyhdto.getJstime());
								//redisUser.setJsids(xtyhdto.getJsids());
								redisUser.setYhid(xtyhdto.getYhid());
								redisUser.setDqjs(xtyhdto.getDqjs());
								redisUser.setDqjsdm(xtyhdto.getDqjsdm());
								redisUser.setDqjsmc(xtyhdto.getDqjsmc());
								redisUser.setDdid(xtyhdto.getDdid());
								redisUser.setWechatid(xtyhdto.getWechatid());
								redisUser.setJgid(xtyhdto.getJgid());
								redisUser.setMm(xtyhdto.getMm());
								redisUser.setMmxgsj(xtyhdto.getMmxgsj());
								redisUser.setGwmc(xtyhdto.getGwmc());
								redisUser.setDdtxlj(xtyhdto.getDdtxlj());
								redisUser.setEmail(xtyhdto.getEmail());
								redisUser.setUnionid(xtyhdto.getUnionid());
								//将用户名存入redis
								redisUtil.hset("Users",redisUser.getYhm(),JSON.toJSONString(redisUser),-1);
								//将钉钉id存入redis
								//redisUser.setYhm(xtyhdto.getDdid());
								redisUser.setLoginFlg("1");

								logger.error(" initDingDing redisUtil.hset Users :" + xtyhdto.getDdid() + (StringUtil.isBlank(dto.getMiniappid())?"":"@"+dbEncrypt.dCode(dto.getMiniappid())) + ":" + JSON.toJSONString(redisUser) );

								redisUtil.hset("Users",xtyhdto.getDdid() + (StringUtil.isBlank(dto.getMiniappid())?"":"@"+dbEncrypt.dCode(dto.getMiniappid())) ,JSON.toJSONString(redisUser),-1);
								if("1".equals(configflg)) {
									amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.XTYH_MOD.getCode() + JSONObject.toJSONString(xtyhdto));
								}
								xtyhList.add(xtyhdto);

								//添加用户所属机构信息用户所属机构
								YhssjgDto yhssjgdto = new YhssjgDto();
								yhssjgdto.setYhid(xtyhdto.getYhid());
								yhssjgdto.setJgid(String.valueOf(departmentList.get(jg).getDeptId()));
								yhssjgdto.setXh("1");
								boolean success = yhssjgService.insert(yhssjgdto);
								if(!success) {
									return false;
								}
								yhssjgdto.setPrefix(prefixFlg);
								if("1".equals(configflg)) {
									amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.SSJG_ADD.getCode() + JSONObject.toJSONString(yhssjgdto));
								}
								yhssjgList.add(yhssjgdto);
								//新增oauth_client_details前确认client_id是否存在
								List<XtyhDto> clientIds = dao.getByClientId(xtyhdto);
								if (CollectionUtils.isEmpty(clientIds)){
									dao.addClientDetails(xtyhdto);
								}
								xtyhdto.setIds(Arrays.asList(xtyhdto.getYhid()));
								initPass(xtyhdto);
								XtyhDto dd_xtyhDto = new XtyhDto();
								dd_xtyhDto.setYhm(StringUtil.isBlank(dto.getMiniappid()) ?user.getUserid():(user.getUserid()+"@"+dbEncrypt.dCode(dto.getMiniappid())));
								dd_xtyhDto.setYhid(user.getUserid());
								List<String> yhms = new ArrayList<>();
								yhms.add(user.getUserid() + (StringUtil.isBlank(dto.getMiniappid())?"":"@"+dbEncrypt.dCode(dto.getMiniappid())));
								dd_xtyhDto.setYhms(yhms);
								List<XtyhDto> ttDtos = dao.getclientByYhm(dd_xtyhDto);
								if (CollectionUtils.isEmpty(ttDtos)) {
									dd_xtyhDto.setMm(bpe.encode(user.getUserid()));
									dao.addClientDetails(dd_xtyhDto);
								}
							}
						}
					}
				}
				if (!CollectionUtils.isEmpty(jgids)){
					//将钉钉上没有的机构删除
					JgxxDto jgxxDto_del = new JgxxDto();
					jgxxDto_del.setIds(jgids);
					jgxxDto_del.setScry("initDingDing");
					jgxxDto_del.setWbcxid(dto.getWbcxid());
					jgxxDto_del.setPrefix(prefixFlg);
					jgxxService.deleteByWbcxid(jgxxDto_del);
					if("1".equals(configflg)) {
						amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.jgxx.dels",JSONObject.toJSONString(jgxxDto_del));
					}
				}
			} catch (Exception e) {
				logger.error("异常："+e.getMessage());
				logger.error("appid:"+dto.getAppid()+"----------secret:"+dto.getSecret());
			}
		}
		if (!CollectionUtils.isEmpty(jgxxDtos)){
			List<List<JgxxDto>> partition = Lists.partition(jgxxDtos, 100);
			for (List<JgxxDto> jgxxs : partition) {
				jgxxService.updateJgxxList(jgxxs);
			}
			if("1".equals(configflg)) {
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.jgxx.updateList",JSONObject.toJSONString(jgxxDtos));
			}
		}
		if (!CollectionUtils.isEmpty(allAddJg)){
			List<DdxxglDto> ddxxglDtos = ddxxglService.selectByDdxxlx(DingMessageType.NEW_DEPARTMENT_REMINDER.getCode());
			sendAddDepartmentMsg(ddxxglDtos,allAddJg);
		}
		return true;
	}

	private void sendAddDepartmentMsg(List<DdxxglDto> ddxxglDtos, List<String> allAddJg) {
		if (!CollectionUtils.isEmpty(ddxxglDtos)){
			String ICOMM_JG00003 = xxglService.getMsg("ICOMM_JG00003");
			String ICOMM_JG00004 = xxglService.getMsg("ICOMM_JG00004");
			try{
				for (DdxxglDto ddxxglDto : ddxxglDtos) {
					if(StringUtil.isNotBlank(ddxxglDto.getYhm())){
						talkUtil.sendWorkMessage(ddxxglDto.getYhm(),
								ddxxglDto.getYhid(), ICOMM_JG00003,StringUtil.replaceMsg(ICOMM_JG00004,StringUtil.join(allAddJg,","),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
					}
				}
			}catch(Exception e){
				logger.error(e.toString());
			}
		}
	}

	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public void syncRosterInfo() {
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setLx(CharacterEnum.DINGDING.getCode());
		wbcxDto.setSftb("1");
		List<WbcxDto> dtoList = wbcxDao.getDtoList(wbcxDto);
		for (WbcxDto wbcxDto_t : dtoList) {
			String token = talkUtil.getToken(wbcxDto_t.getWbcxid());
			XtyhDto xtyhDto_y = new XtyhDto();
			xtyhDto_y.setWbcxid(wbcxDto_t.getWbcxid());
			xtyhDto_y.setSfsd("0");
			List<XtyhDto> xtyhDtos = dao.getAllUserList(xtyhDto_y);//查找的是不包括删除标记为1的所有数据
			Map<Long, String> allBm = new HashMap<>();
			List<OapiV2DepartmentListsubResponse.DeptBaseResponse> departmentList = talkUtil.getAllDepartmentsV2(token);//得到钉钉所有部门
			for (OapiV2DepartmentListsubResponse.DeptBaseResponse deptBaseResponse : departmentList) {
				allBm.put(deptBaseResponse.getDeptId(),deptBaseResponse.getName());
			}
			//当获取不到部门，则代表所有人员都在一个默认部门下，所以设置为1
			if(!CollectionUtils.isEmpty(departmentList)) {
				OapiV2DepartmentListsubResponse.DeptBaseResponse department = new OapiV2DepartmentListsubResponse.DeptBaseResponse();
				department.setDeptId(1L);
				departmentList.add(department);
			}
			List<YghmcDto> updateYgList = new ArrayList<>();
			List<XtyhDto> updateYhList = new ArrayList<>();
			//获取在职人员列表
			List<String> userids = talkUtil.getAllJobEmployee(token);
			//获取离职人员列表
			List<String> separatingEmployeeList = talkUtil.getSeparatingEmployeeListV2(token);
			if (!CollectionUtils.isEmpty(separatingEmployeeList)){
				for (String ddid : separatingEmployeeList) {
					XtyhDto xtyhDto = new XtyhDto();
					xtyhDto.setDdid(ddid);
					xtyhDto.setSfsd("1");
					xtyhDto.setWbcxid(wbcxDto_t.getWbcxid());
					xtyhDto.setPrefix(prefixFlg);
					updateYhList.add(xtyhDto);
				}
				userids.addAll(separatingEmployeeList);
				userids = userids.stream().distinct().collect(Collectors.toList());
			}
			if (!CollectionUtils.isEmpty(separatingEmployeeList)) {
				List<List<String>> partition = Lists.partition(separatingEmployeeList, 50);
				for (List<String> list : partition) {
					//获取离职人员信息
					List<QueryHrmEmployeeDismissionInfoResponseBody.QueryHrmEmployeeDismissionInfoResponseBodyResult> separatingEmployees = talkUtil.getSeparatingEmployeeInfoV2(list, token);
					if (!CollectionUtils.isEmpty(separatingEmployees)) {
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						Date date = new Date();
						for (QueryHrmEmployeeDismissionInfoResponseBody.QueryHrmEmployeeDismissionInfoResponseBodyResult separatingEmployee : separatingEmployees) {
							YghmcDto yghmcDto = new YghmcDto();
							if (null != separatingEmployee.getLastWorkDay()) {
								date.setTime(separatingEmployee.getLastWorkDay());
								yghmcDto.setLzrq(format.format(date));
							}
							yghmcDto.setDdid(separatingEmployee.getUserId());
							yghmcDto.setSflz("1");

							yghmcDto.setBm(String.valueOf(separatingEmployee.getMainDeptId()));
							updateYgList.add(yghmcDto);
						}
					}
				}
			}

			if (!CollectionUtils.isEmpty(userids)) {
				List<List<String>> lists = Lists.partition(userids, 100);
				List<YghmcDto> yghmcDtos = new ArrayList<>();
				for (List<String> list : lists) {
					List<EmpRosterFieldVo> result = talkUtil.getRosterInfo(list, token, this.getFieldFilterStr());
					if (!CollectionUtils.isEmpty(result)){
						for (EmpRosterFieldVo empRosterFieldVo : result) {
							List<EmpFieldDataVo> fieldDataList = empRosterFieldVo.getFieldDataList();
							YghmcDto yghmcDto = new YghmcDto();
							yghmcDto.setDdid(empRosterFieldVo.getUserid());
							yghmcDto.setYghmcid(StringUtil.generateUUID());
							yghmcDto.setSflz("0");
							yghmcDto.setDqtx("1");
							yghmcDto.setPrefix(prefixFlg);
							yghmcDto.setWbcxid(wbcxDto_t.getWbcxid());
							for (EmpFieldDataVo empFieldDataVo : fieldDataList) {
								RosterEnum rosterEnum = RosterEnum.getByCode(empFieldDataVo.getFieldCode());
								switch (rosterEnum) {
									case SEX_TYPE: yghmcDto.setSex(empFieldDataVo.getFieldValueList().get(0).getValue());break;
									case NATION_TYPE: yghmcDto.setMz(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case CERT_NO: yghmcDto.setSfz(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case EMPLOYEE_AGE: yghmcDto.setNl(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case CERT_END_TIME: yghmcDto.setZjyxq(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case BIRTH_TIME: yghmcDto.setCsrq(empFieldDataVo.getFieldValueList().get(0).getLabel());yghmcDto.setCsyf(StringUtil.isNotBlank(empFieldDataVo.getFieldValueList().get(0).getLabel())?empFieldDataVo.getFieldValueList().get(0).getLabel().substring(5,7):null);break;
									case HIGHEST_EDU: yghmcDto.setXl(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case GRADUATE_SCHOOL: yghmcDto.setByyx(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case MAJOR: yghmcDto.setZy(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case MOBILE: yghmcDto.setSjhm(StringUtil.isNotBlank(empFieldDataVo.getFieldValueList().get(0).getLabel())?empFieldDataVo.getFieldValueList().get(0).getLabel().substring(4):null);break;
									case CONFIRM_JOIN_TIME: yghmcDto.setRzrq(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case REGULAR_TIME: yghmcDto.setZzrq(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case JOIN_WORKING_TIME: yghmcDto.setCjgzsj(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case ENTRYAGE: yghmcDto.setSl(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case MARRIAGE: yghmcDto.setHyzk(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case POLITICAL_STATUS: yghmcDto.setZzmm(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case POSITION: yghmcDto.setZc(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case RESIDENCE_TYPE: yghmcDto.setHjxz(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case CERT_ADDRESS: yghmcDto.setSfzdz(empFieldDataVo.getFieldValueList().get(0).getLabel());yghmcDto.setJg(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case ADDRESS: yghmcDto.setXjdz(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case URGENT_CONTACTS_NAME: yghmcDto.setJjlxr(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case URGENT_CONTACTS_PHONE: yghmcDto.setLxrdh(StringUtil.isNotBlank(empFieldDataVo.getFieldValueList().get(0).getLabel())?StringUtil.join(empFieldDataVo.getFieldValueList().get(0).getLabel().split(" ")):null);break;
									case FIRST_CONTRACT_STARTTIME: yghmcDto.setSchtqsr(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case FIRST_CONTRACT_END_TIME: yghmcDto.setSchtdqr(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case BANKACCOUNT_NO: yghmcDto.setYhkh(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case ACCOUNT_BANK: yghmcDto.setKhh(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case WORK_PLACE: yghmcDto.setBgdd(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case NOW_CONTRACT_START_TIME: yghmcDto.setXhtqsr(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case NOW_CONTRACT_END_TIME: yghmcDto.setXhtdqr(empFieldDataVo.getFieldValueList().get(0).getLabel());break;

									case EMPLOYEE_NAME: yghmcDto.setXm(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case JOB_NUMBER: yghmcDto.setYhm(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									//花名册离职人员没有部门
									case MAIN_DEPTID: yghmcDto.setBm(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
									case CONTRACT_COMPANY_NAME: yghmcDto.setSsgs(empFieldDataVo.getFieldValueList().get(0).getLabel());break;
								}
							}
							yghmcDtos.add(yghmcDto);
						}
					}
				}
				//更新离职信息及部门
				if (!CollectionUtils.isEmpty(updateYgList)) {
					for (YghmcDto yghmcDto : yghmcDtos) {
						for (YghmcDto dto : updateYgList) {
							if (yghmcDto.getDdid().equals(dto.getDdid())){
								yghmcDto.setLzrq(dto.getLzrq());
								yghmcDto.setSflz(dto.getSflz());
								yghmcDto.setBm(dto.getBm());
							}
						}
					}
				}
				//去掉部门信息为空的
				Iterator<YghmcDto> kbmyghmcDtos = yghmcDtos.iterator();
				while (kbmyghmcDtos.hasNext()){
					YghmcDto next = kbmyghmcDtos.next();
					boolean blank = StringUtil.isBlank(next.getBm());
					if (blank){
						kbmyghmcDtos.remove();
					}
				}
				for (YghmcDto yghmcDto : yghmcDtos) {
					//更新在职人员的直属主管 和上级部门信息
					if ("0".equals(yghmcDto.getSflz())) {
						OapiV2UserGetResponse.UserGetResponse userDeails = talkUtil.getUserDeails(yghmcDto.getDdid(), token);
						if (userDeails != null) {
							yghmcDto.setZszg(userDeails.getManagerUserid());
							List<Long> deptIdList = userDeails.getDeptIdList();
							if (!CollectionUtils.isEmpty(deptIdList)) {
								StringBuilder sjbm = new StringBuilder();
								for (Long aLong : deptIdList) {
									sjbm.append("-").append(allBm.get(aLong));
								}
								sjbm = new StringBuilder(sjbm.substring(1));
								yghmcDto.setSjbm(sjbm.toString());
								if (!CollectionUtils.isEmpty(deptIdList)) {
									yghmcDto.setDyjbm(allBm.get(deptIdList.get(0)));
								}
								if (deptIdList.size() > 1) {
									yghmcDto.setDejbm(allBm.get(deptIdList.get(1)));
								}
								if (deptIdList.size() > 2) {
									yghmcDto.setDsjbm(allBm.get(deptIdList.get(2)));
								}
							}
						}
					}
					for (XtyhDto xtyhDto : xtyhDtos) {
						if (yghmcDto.getDdid().equals(xtyhDto.getDdid()) && "0".equals(yghmcDto.getSflz())){
							yghmcDto.setYhid(xtyhDto.getYhid());
						}
					}
				}
				//对应不到用户的不再处理
				yghmcDtos.removeIf(e -> StringUtil.isBlank(e.getYhid()));
				List<String> ygYhids = yghmcService.getAllYgYhid();
				//通过系统用户去新增员工花名册
				List<YghmcDto> addYghmcs = new ArrayList<>();
				addYghmcs.addAll(yghmcDtos);
				//通过已有的去剔除
				if (!CollectionUtils.isEmpty(ygYhids)){
                    addYghmcs.removeIf(yghmcDto -> ygYhids.contains(yghmcDto.getYhid()) || "1".equals(yghmcDto.getSflz()));
				}
				//新增员工花名册信息
				if (!CollectionUtils.isEmpty(addYghmcs)) {
					List<List<YghmcDto>> yghmcDtoLists = Lists.partition(addYghmcs, 100);
					for (List<YghmcDto> yghmcList : yghmcDtoLists) {
						yghmcService.insertYghmcDtos(yghmcList);
					}
					if("1".equals(configflg)) {
						amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.HMC_ADD.getCode() + JSONObject.toJSONString(addYghmcs));
					}
				}
				//锁定离职用户
				if (!CollectionUtils.isEmpty(updateYhList)) {
					List<List<XtyhDto>> listYhs = Lists.partition(updateYhList, 100);
					for (List<XtyhDto> list : listYhs) {
						xtyhDao.updateXtyhDtos(list);
					}
					if("1".equals(configflg)) {
						amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.XTYH_MODLIST.getCode() + JSONObject.toJSONString(updateYhList));
					}
				}
				//修改员工花名册信息
				List<List<YghmcDto>> yghmcDtoLists = Lists.partition(yghmcDtos, 100);
				for (List<YghmcDto> yghmcList : yghmcDtoLists) {
					yghmcService.updateYghmcDtos(yghmcList);
				}
				if("1".equals(configflg)) {
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.HMC_MOD.getCode() + JSONObject.toJSONString(yghmcDtos));
				}
			}
			List<YghmcDto> htYghmcDtos = yghmcService.getAllYghtxxByHmc();
			List<YghtxxDto> yghtxxDtos = new ArrayList<>();
			List<YglzxxDto> yglzxxDtos = new ArrayList<>();
			if (!CollectionUtils.isEmpty(htYghmcDtos)){
				for (YghmcDto htYghmcDto : htYghmcDtos) {
					if (StringUtil.isBlank(htYghmcDto.getYghtid())){
						YghtxxDto yghtxxDto = new YghtxxDto();
						yghtxxDto.setYghtid(StringUtil.generateUUID());
						yghtxxDto.setYghmcid(htYghmcDto.getYghmcid());
						yghtxxDto.setHtqsr(htYghmcDto.getSchtqsr());
						yghtxxDto.setHtdqr(htYghmcDto.getSchtdqr());
						yghtxxDto.setLrry("admin");
						yghtxxDto.setPrefix(prefixFlg);
						yghtxxDtos.add(yghtxxDto);
					}else {
						if (StringUtil.isNotBlank(htYghmcDto.getSchtqsr())&&StringUtil.isNotBlank(htYghmcDto.getSchtdqr())){
							boolean flag = true;
							String yghmcid = htYghmcDto.getYghmcid();
							for (YghmcDto yghmcDto : htYghmcDtos) {
                                if (yghmcid.equals(yghmcDto.getYghmcid()) && htYghmcDto.getSchtqsr().equals(yghmcDto.getHtqsr()) && htYghmcDto.getSchtdqr().equals(yghmcDto.getHtdqr())) {
                                    flag = false;
                                    break;
                                }
							}
							if (flag){
								YghtxxDto yghtxxDto = new YghtxxDto();
								yghtxxDto.setYghtid(StringUtil.generateUUID());
								yghtxxDto.setYghmcid(htYghmcDto.getYghmcid());
								yghtxxDto.setHtqsr(htYghmcDto.getSchtqsr());
								yghtxxDto.setHtdqr(htYghmcDto.getSchtdqr());
								yghtxxDto.setLrry("admin");
								yghtxxDto.setPrefix(prefixFlg);
								yghtxxDtos.add(yghtxxDto);
							}
						}
					}
				}
			}
			List<YghmcDto> lzYghmcDtos = yghmcService.getAllYgLzxxByHmc();
			if (!CollectionUtils.isEmpty(lzYghmcDtos)){
				for (YghmcDto lzYghmcDto : lzYghmcDtos) {
					if (StringUtil.isNotBlank(lzYghmcDto.getLzrq())) {
						if (StringUtil.isBlank(lzYghmcDto.getYglzid())) {
							YglzxxDto yglzxxDto = new YglzxxDto();
							yglzxxDto.setYghmcid(lzYghmcDto.getYghmcid());
							yglzxxDto.setYglzid(StringUtil.generateUUID());
							yglzxxDto.setLzrq(lzYghmcDto.getLzrq());
							yglzxxDto.setRzrq(lzYghmcDto.getRzrq());
							yglzxxDto.setLrry("admin");
							yglzxxDto.setPrefix(prefixFlg);
							yglzxxDtos.add(yglzxxDto);
						} else {
							if (StringUtil.isNotBlank(lzYghmcDto.getRzrq()) && StringUtil.isNotBlank(lzYghmcDto.getLzrq())) {
								boolean flag = true;
								String yghmcid = lzYghmcDto.getYghmcid();
								for (YghmcDto yghmcDto : lzYghmcDtos) {
									if (yghmcid.equals(yghmcDto.getYghmcid())) {
										if (lzYghmcDto.getRzrq().equals(yghmcDto.getYgrzrq()) && lzYghmcDto.getLzrq().equals(yghmcDto.getYglzrq())) {
                                            flag = false;
                                        }
									}
								}
								if (flag) {
									YglzxxDto yglzxxDto = new YglzxxDto();
									yglzxxDto.setYghmcid(lzYghmcDto.getYghmcid());
									yglzxxDto.setYglzid(StringUtil.generateUUID());
									yglzxxDto.setLzrq(lzYghmcDto.getLzrq());
									yglzxxDto.setRzrq(lzYghmcDto.getRzrq());
									yglzxxDto.setLrry("admin");
									yglzxxDto.setPrefix(prefixFlg);
									yglzxxDtos.add(yglzxxDto);
								}
							}
						}
					}
				}
			}
			if (!CollectionUtils.isEmpty(yghtxxDtos)){
				List<List<YghtxxDto>> yghtxxLists = Lists.partition(yghtxxDtos, 100);
				for (List<YghtxxDto> yghtxxs : yghtxxLists) {
					yghtxxDao.insertYghtxxDtos(yghtxxs);
				}
				if("1".equals(configflg)) {
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.YHT_ADD.getCode() + JSONObject.toJSONString(yghtxxDtos));
				}
			}
			if (!CollectionUtils.isEmpty(yglzxxDtos)){
				List<List<YglzxxDto>> yglzxxLists = Lists.partition(yglzxxDtos, 100);
				for (List<YglzxxDto> yglzxxs : yglzxxLists) {
					yglzxxDao.insertYglzxxDtos(yglzxxs);
				}
				if("1".equals(configflg)) {
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.YLZ_ADD.getCode() + JSONObject.toJSONString(yglzxxDtos));
				}
			}
		}
	}
	private String getFieldFilterStr() {
		List<String> list = new ArrayList<>();
		list.add(RosterEnum.SEX_TYPE.code);
		list.add(RosterEnum.NATION_TYPE.code);
		list.add(RosterEnum.CERT_NO.code);
		list.add(RosterEnum.EMPLOYEE_AGE.code);
		list.add(RosterEnum.CERT_END_TIME.code);
		list.add(RosterEnum.BIRTH_TIME.code);
		list.add(RosterEnum.HIGHEST_EDU.code);
		list.add(RosterEnum.GRADUATE_SCHOOL.code);
		list.add(RosterEnum.MAJOR.code);
		list.add(RosterEnum.MOBILE.code);
		list.add(RosterEnum.CONFIRM_JOIN_TIME.code);
		list.add(RosterEnum.REGULAR_TIME.code);
		list.add(RosterEnum.JOIN_WORKING_TIME.code);
		list.add(RosterEnum.ENTRYAGE.code);
		list.add(RosterEnum.MARRIAGE.code);
		list.add(RosterEnum.POLITICAL_STATUS.code);
		list.add(RosterEnum.POSITION.code);
		list.add(RosterEnum.RESIDENCE_TYPE.code);
		list.add(RosterEnum.CERT_ADDRESS.code);
		list.add(RosterEnum.ADDRESS.code);
		list.add(RosterEnum.URGENT_CONTACTS_NAME.code);
		list.add(RosterEnum.URGENT_CONTACTS_PHONE.code);
		list.add(RosterEnum.FIRST_CONTRACT_STARTTIME.code);
		list.add(RosterEnum.FIRST_CONTRACT_END_TIME.code);
		list.add(RosterEnum.BANKACCOUNT_NO.code);
		list.add(RosterEnum.ACCOUNT_BANK.code);
		list.add(RosterEnum.WORK_PLACE.code);
		list.add(RosterEnum.EMPLOYEE_NAME.code);
		list.add(RosterEnum.NOW_CONTRACT_START_TIME.code);
		list.add(RosterEnum.NOW_CONTRACT_END_TIME.code);
		list.add(RosterEnum.JOB_NUMBER.code);
		list.add(RosterEnum.CONTRACT_COMPANY_NAME.code);
		list.add(RosterEnum.MAIN_DEPTID.code);
		return StringUtil.join(list, ",");
	}



	/**
	 * 获取微信用户列表
	 * @return
	 */
	public List<WxyhDto> getPagedDtoListWxyh(WxyhDto wxyhdto){
		return dao.getPagedDtoListWxyh(wxyhdto);
	}
	
	/**
	 * 将微信用户列表微信ID更新到用户列表
	 * @param wxyhdto
	 * @return
	 */
	public boolean updatextyh(WxyhDto wxyhdto) {
		return dao.updatextyh(wxyhdto);
	}

	/**
	 * 对用户进行认证
	 * @param inModel
	 * @return
	 */
	public InterfaceReturnModel outsideAuth(InterfaceModel inModel) {
		
		InterfaceReturnModel returnModel = new InterfaceReturnModel();
		/*
		 * @SuppressWarnings("unchecked") Map<String, String> map =
		 * JSONObject.parseObject(inModel.getData(),Map.class);
		 * 
		 * DBEncrypt dbEncrypt = new DBEncrypt(); String username =
		 * dbEncrypt.dCodeDes(map.get("username"),inModel.getKey()); String password =
		 * dbEncrypt.dCodeDes(map.get("password"),inModel.getKey());
		 * 
		 * //String paramString = "grant_type=password&username=" + username +
		 * "&password="+ password; try { } catch (Exception e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */
		returnModel.setStatus("-3");
		returnModel.setMsg("认证用户未成功！");
		return returnModel;
	}
	
	/**
	 * 对用户是否为已登录过的进行确认
	 * @param inModel
	 * @return
	 */
	public String outsideAuthCheck(InterfaceModel inModel) {
		InterfaceReturnModel returnModel = new InterfaceReturnModel();
		@SuppressWarnings("unchecked")
		Map<String, String> map = JSONObject.parseObject(inModel.getData(),Map.class);
		
		String access_token = map.get("access_token");
		
		try {
			//再根据转换后的token查询用户信息
			XtyhDto xtyhDto = checkToken(access_token);
			
			if(xtyhDto == null)
			{
				returnModel.setStatus("-5");
				returnModel.setMsg("用户未认证！");
				return JSONObject.toJSONString(returnModel);
			}
			
			returnModel.setStatus("0");
			SimplePropertyPreFilter filter = new SimplePropertyPreFilter(XtyhDto.class, "username","yhm","xbmc","zsxm","gwmc");
			SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[5];
			listFilters[0] = filter;
			returnModel.setData(JSONObject.toJSONString(xtyhDto,listFilters));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(e.toString());
		}
		return JSONObject.toJSONString(returnModel);
	}
	
	/**
	 * 对token进行验证
	 * @param tokenid
	 * @return
	 */
	public XtyhDto checkToken(String tokenid) {
		
		Encrypt ecp = new Encrypt();
		//先把页面上传递过来的tokenid进行转换
		String token_id = ecp.extractTokenKey(tokenid);
		//再根据转换后的token查询用户信息
		XtyhDto xtyhDto = dao.getDtoByToken(token_id);
		
		TokenStore tokenStore = (TokenStore) ServiceFactory.getService("tokenStore");
		
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(token_id);
		//用户不存在或者token过期
		if(xtyhDto == null || accessToken == null || accessToken.isExpired()) {
            return null;
        }
		
		return xtyhDto;
	}
	
	/**
	 * 检测用户名和密码，如果用户已经登录，则直接返回相应的access_token,没有则重定向到登录接口
	 * @param grant_type
	 * @param username
	 * @param password
	 * @param url
	 * @return
	 */
	public Map<String, String> outsideAuthLogin(String grant_type,String username,String password,String url) {
		
		Map<String, String> result = new HashMap<>();
		
    	if(!"password".equals(grant_type)) {
    		result.put("status", "401");
    		result.put("error", "Unauthorize");
    		result.put("message", "Unauthorize");
    		return result;
    	}
		
    	BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		
    	XtyhDto tXtyhDto = dao.selectOauthAccessByName(username);
    	
    	if(tXtyhDto == null)
    	{
    		result.put("status", "401");
    		result.put("error", "Unauthorize");
    		result.put("message", "Unauthorize");
    		return result;
    	}
		
		if(!bpe.matches(password, tXtyhDto.getMm())) {
			result.put("status", "401");
    		result.put("error", "Unauthorize");
    		result.put("message", "Unauthorize");
    		return result;
		}

    	
    	// TODO Auto-generated method stub
		//Encrypt ecp = new Encrypt();
		//先把页面上传递过来的tokenid进行转换
		//String token_id = ecp.extractTokenKey(tXtyhDto.getToken_id());
		TokenStore tokenStore = (TokenStore)ServiceFactory.getService("tokenStore");
		
		OAuth2AccessToken accessToken = tokenStore.readAccessToken(tXtyhDto.getToken_id());
		if (accessToken == null || accessToken.isExpired()) {
			
			/*
			 * String params =
			 * "grant_type="+grant_type+"username="+username+"password="+password; try {
			 * 
			 * URL tokenurl = new URL("http://"+url+"/oauth/token"); // 打开和URL之间的连接
			 * HttpURLConnection connection = (HttpURLConnection) tokenurl.openConnection();
			 * connection.setRequestMethod("POST"); // 设置通用的请求属性
			 * connection.setUseCaches(false); connection.setDoOutput(true);
			 * connection.setDoInput(true); // 得到请求的输出流对象 DataOutputStream out = new
			 * DataOutputStream(connection.getOutputStream());
			 * out.write(params.getBytes("UTF-8")); out.flush(); out.close();
			 * 
			 * // 建立实际的连接 connection.connect(); // 定义 BufferedReader输入流来读取URL的响应
			 * BufferedReader in = null; in = new BufferedReader( new
			 * InputStreamReader(connection.getInputStream(), "UTF-8")); String resultString
			 * = ""; String getLine; while ((getLine = in.readLine()) != null) {
			 * resultString += getLine; } in.close();
			 * 
			 * @SuppressWarnings("unchecked") Map<String, String> resultMap=
			 * JSONObject.parseObject(resultString, Map.class);
			 * 
			 * return resultMap; }catch(Exception e) { e.printStackTrace(); }
			 */
    		return null;
		}
		result.put("access_token", tXtyhDto.getToken_id());
		
		return result;
	}

	/**
	 * 根据用户Ids查询用户信息
	 * @param xtyhDto
	 * @return
	 */
	@Override
	public List<XtyhDto> getListByIds(XtyhDto xtyhDto) {
		// TODO Auto-generated method stub
		return dao.getListByIds(xtyhDto);
	}

	/**
	 * 查询用户列表
	 * @return
	 */
	@Override
	public List<XtyhDto> getUserList() {
		// TODO Auto-generated method stub
		return dao.getUserList();
	}
	
	/**
	 * 根据用户名删除oauth_client_details表中的数据 
	 * @param xtyhDto
	 * @return
	 */
	public boolean deleteclientByYhm(XtyhDto xtyhDto) {
		int result=dao.deleteclientByYhm(xtyhDto);
        return result > 0;
    }
	
	/**
	 * 删除系统用户
	 * @throws BusinessException 
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteXtyh(XtyhDto xtyhDto) throws BusinessException {
		xtyhDto.setPrefix(prefixFlg);
		int scxtyh=dao.delete(xtyhDto);
		//删除redis中的用户
		if (!CollectionUtils.isEmpty(xtyhDto.getYhms())){
			for (String yhm : xtyhDto.getYhms()) {
				redisUtil.hdel("Users",yhm);
//				logger.error("Users Redis Del:"+ yhm);
			}
		}
		if("1".equals(configflg)) {
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.xtyh.del",JSONObject.toJSONString(xtyhDto));
		}
		int scclient;
		List<XtyhDto> clientlist=dao.getclientByYhm(xtyhDto);
		if(!CollectionUtils.isEmpty(clientlist)) {
			scclient=dao.deleteclientByYhm(xtyhDto);
		}else {
			throw new BusinessException("msg",xxglService.getMsg("ICOM00004"));
		}
		if(scxtyh>0 && scclient>0) {
			SpgwcyDto spgwcyDto = new SpgwcyDto();
			spgwcyDto.setYhidlist(xtyhDto.getIds());
			spgwcyService.delete(spgwcyDto);
			YhjsDto yhjsDto=new YhjsDto();
			yhjsDto.setIds(xtyhDto.getIds());
			List<YhjsDto> yxjslist=yhjsService.getYxyhList(xtyhDto);
			if(!CollectionUtils.isEmpty(yxjslist)) {
				yhjsDto.getIds().clear();
				yhjsDto.getIds().addAll(yxjslist.stream().map(YhjsDto::getYhid).collect(Collectors.toList()));
				yhjsService.toOptional(yhjsDto);
			}
			YhjgqxDto yhjgqxDto=new YhjgqxDto();
			yhjgqxDto.setIds(xtyhDto.getIds());
			List<YhjgqxDto> yhjgqxlist=yhjgqxService.selectYhqxjg(yhjgqxDto);
			if(!CollectionUtils.isEmpty(yhjgqxlist)) {
				yhjgqxDto.getIds().clear();
				yhjgqxDto.getIds().addAll(yhjgqxlist.stream().map(YhjgqxDto::getYhid).collect(Collectors.toList()));
				yhjgqxService.deleteJgqxxxByYhid(yhjgqxDto);
			}
			YhssjgDto yhssjgDto=new YhssjgDto();
			yhssjgDto.setIds(xtyhDto.getIds());
			List<YhssjgDto> csyhssjglist=yhssjgService.getYhssjgByIds(yhssjgDto);
			if(!CollectionUtils.isEmpty(csyhssjglist)) {
				yhssjgDto.getIds().clear();
				yhssjgDto.getIds().addAll(csyhssjglist.stream().map(YhssjgDto::getYhid).collect(Collectors.toList()));
				yhssjgService.deleteYhssjgByYhid(yhssjgDto);
			}
			return true;
		}else {
			return false;
		}
	}

	/**
	 * 根据用户名模糊查询
	 * @param xtyhDto
	 * @return
	 */
	@Override
	public List<XtyhDto> getListByYhm(XtyhDto xtyhDto) {
		// TODO Auto-generated method stub
		return dao.getListByYhm(xtyhDto);
	}
	
	/**
	 * 根据token获取用户信息
	 * @param tokenid
	 * @return
	 */
	public com.matridx.igams.common.dao.entities.User getUserByToken(String tokenid) {
		
		Encrypt ecp = new Encrypt();
		//先把页面上传递过来的tokenid进行转换
		String token_id = ecp.extractTokenKey(tokenid);
		//再根据转换后的token查询用户信息

        return dao.getUserByToken(token_id);
	}
	
	/**
	 * 根据钉钉id或微信id获取用户信息
	 */
	public XtyhDto getXtyhByUserid(XtyhDto xtyhDto) {
		XtyhDto xtyhxx=new XtyhDto();
		if(StringUtil.isNotBlank(xtyhDto.getWechatid())) {
			xtyhxx=dao.getYhxxByWxid(xtyhDto);
		}else if(StringUtil.isNotBlank(xtyhDto.getDdid())) {
			if (xtyhDto.getDdid().contains("@")) {
				String ddid = xtyhDto.getDdid();
				xtyhDto.setMiniappid(new DBEncrypt().eCode(ddid.split("@")[1]));
				xtyhDto.setDdid(ddid.split("@")[0]);
			}
			xtyhxx=dao.getYhxxByDdid(xtyhDto);
		}
		return xtyhxx;
	}

	/**
	 * 根据钉钉id或微信id获取用户信息(先从redis中获取，如果redis中没有再从数据库中获取)
	 */
	public XtyhDto getXtyhRedisAndDbByUserid(XtyhDto xtyhDto) {
		XtyhDto xtyhxx = new XtyhDto();
		if(StringUtil.isNotBlank(xtyhDto.getWechatid())) {
			com.matridx.igams.common.dao.entities.User user = redisUtil.hugetDto("Users", xtyhDto.getWechatid());
			if (user != null){
				xtyhxx = userToXtyhDto(user);
			}else {
				xtyhxx = dao.getYhxxByWxid(xtyhDto);
			}
		}else if(StringUtil.isNotBlank(xtyhDto.getDdid())) {
			com.matridx.igams.common.dao.entities.User user = redisUtil.hugetDto("Users", xtyhDto.getDdid());
			if (user != null){
				xtyhxx=userToXtyhDto(user);
			}else {
				xtyhxx=dao.getYhxxByDdid(xtyhDto);
			}
		}
		return xtyhxx;
	}

	public XtyhDto userToXtyhDto(com.matridx.igams.common.dao.entities.User user){
		XtyhDto xtyhDto = new XtyhDto();
		xtyhDto.setYhid(user.getYhid());
		xtyhDto.setYhm(user.getYhm());
		xtyhDto.setZsxm(user.getZsxm());
		xtyhDto.setMm(user.getMm());
		xtyhDto.setSfsd(user.getSfsd());
		xtyhDto.setDqjs(user.getDqjs());
		xtyhDto.setDqjsmc(user.getDqjsmc());
		xtyhDto.setDlip(user.getDlip());
		xtyhDto.setDlrq(user.getDlsj());
		xtyhDto.setCwcs(user.getCwcs());
		xtyhDto.setDdid(user.getDdid());
		xtyhDto.setWechatid(user.getWechatid());
		xtyhDto.setGwmc(user.getGwmc());
		xtyhDto.setDdtxlj(user.getDdtxlj());
		xtyhDto.setMmxgsj(user.getMmxgsj());
		xtyhDto.setSdtime(user.getSdtime());
		xtyhDto.setJstime(user.getJstime());
		xtyhDto.setGrouping(user.getGrouping());
		xtyhDto.setDqjsmc(user.getDqjsmc());
		xtyhDto.setDqjsdm(user.getDqjsdm());
		xtyhDto.setJgid(user.getJgid());
		xtyhDto.setJgmc(user.getJgmc());
		return xtyhDto;
	}
	/**
	 * 上传签名保存
	 * @param xtyhDto
	 * @return
	 */
	@Override
	public boolean uploadSaveSignature(XtyhDto xtyhDto) {
		// TODO Auto-generated method stub
		//文件复制到正式文件夹，插入信息至正式表
		if(!CollectionUtils.isEmpty(xtyhDto.getFjids())){
			for (int i = 0; i < xtyhDto.getFjids().size(); i++) {
				Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+xtyhDto.getFjids().get(i));
				//如果文件信息不存在，则返回错误
				if(mFile == null) {
                    return false;
                }
				//文件名
				String wjm = (String)mFile.get("wjm");
				String fileName = wjm.substring(0, wjm.lastIndexOf("."));
				if(StringUtil.isNotBlank(fileName) && fileName.length() > 5){
					fileName = fileName.substring(0, 5);
				}
				wjm = fileName + ".jpg";
				//分文件名
				String fwjm = (String)mFile.get("fwjm");
				String filefName = fwjm.substring(0, fwjm.lastIndexOf("."));
				fwjm = filefName + ".jpg";
				//文件全路径
				String wjlj = (String)mFile.get("wjlj");
				//业务类型
				String ywlx = (String)mFile.get("ywlx");
				//分文件路径
				String real_path = prefix + releaseFilePath + xtyhDto.getYwlx();
				
				mkDirs(real_path);
				//把文件从临时文件夹中移动到正式文件夹中，如果已经存在则覆盖
				CutFile(wjlj,real_path+"/"+fwjm,true);
				//将正式文件夹路径更新至数据库
				DBEncrypt bpe = new DBEncrypt();
				FjcfbDto fjcfbDto = new FjcfbDto();
				fjcfbDto.setFjid(xtyhDto.getFjids().get(i));
				fjcfbDto.setYwid(fileName);
				fjcfbDto.setXh("1");
				fjcfbDto.setYwlx(ywlx);
				fjcfbDto.setWjm(wjm);
				fjcfbDto.setWjlj(bpe.eCode(real_path+"/"+fwjm));
				fjcfbDto.setFwjlj(bpe.eCode(real_path));
				fjcfbDto.setFwjm(bpe.eCode(fwjm));
				fjcfbDto.setZhbj("0");
				List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
				if(fjcfbDtos != null && fjcfbDtos.size() == 1){
					fjcfbDtos.get(0).setWjlj(bpe.eCode(real_path+"/"+fwjm));
					fjcfbDtos.get(0).setFwjm(bpe.eCode(fwjm));
					boolean result = fjcfbService.update(fjcfbDtos.get(0));
					if(!result) {
                        return false;
                    }
				}else if( CollectionUtils.isEmpty(fjcfbDtos)){
					boolean result = fjcfbService.insert(fjcfbDto);
					if(!result) {
                        return false;
                    }
				}
				fjcfbDto.setPrefix(prefixFlg);
				if("1".equals(configflg)) {
					//发送rabbit
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.xtyh.sign",JSONObject.toJSONString(fjcfbDto));
				}
			}
			
			return true;
		}
		return false;
	}
	
	/**
	 * 根据路径创建文件
	 * @param storePath
	 * @return
	 */
	private boolean mkDirs(String storePath){
		File file = new File(storePath);
		if (file.isDirectory()) {
			return true;
		}
		return file.mkdirs();
	}

	/**
	 * 从原路径剪切到目标路径
	 * @param s_srcFile
	 * @param s_distFile
	 * @param coverFlag
	 * @return
	 */
	private boolean CutFile(String s_srcFile,String s_distFile,boolean coverFlag) {
		boolean flag = false;
		//路径如果为空则直接返回错误
		if(StringUtil.isBlank(s_srcFile)|| StringUtil.isBlank(s_distFile)) {
            return false;
        }
		
		File srcFile = new File(s_srcFile);
		File distFile = new File(s_distFile);
		//文件不存在则直接返回错误
		if(!srcFile.exists()) {
            return false;
        }
		//目标文件已经存在
		if(distFile.exists()) {
			if(coverFlag) {
				srcFile.renameTo(distFile);
				flag = true;
			}
		}else {
			srcFile.renameTo(distFile);
			flag = true;
		}
		return flag;
	}

	@Override
	public boolean modPass(XtyhDto xtyhDto) {
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		xtyhDto.setMm(bpe.encode(xtyhDto.getMm()));
		int result = 0;
		XtyhDto tXtyhDto = dao.getDtoById(xtyhDto.getYhid());
		// 更新用户信息
		int updatePass = dao.updatePass(xtyhDto);
		result = result + updatePass;
		xtyhDto.setYhm(tXtyhDto.getYhm());
		dao.updateOauthPass(xtyhDto);
		return result > 0;
	}

	@Override
	public XtyhDto getDtoByYhm(XtyhDto xtyhDto) {
		// TODO Auto-generated method stub
		return dao.getDtoByYhm(xtyhDto);
	}

	/**
	 * 更新用户错误次数
	 * @param xtyhDto
	 * @return
	 */
	public boolean updateLoginInfo(XtyhDto xtyhDto) {
		int result = dao.updateLoginInfo(xtyhDto);
		if (result != 0 && Integer.parseInt(xtyhDto.getCwcs())>=5){

			amqpTempl.convertAndSend(CommonRabbitMqConfigTt.ZC_DELAY_EXCHANGE, CommonRabbitMqConfigTt.ZC_ROUTING_KEY, JSONObject.toJSONString(xtyhDto), message -> {
                String str = "login.ErrorLimit" + xtyhDto.getCwcs();
                if ( Integer.parseInt(xtyhDto.getCwcs())>8){
                    str = "login.ErrorLimitLater";
                }
                String sdDate = xtyhDao.selectXtsz(str).getSzz();
                message.getMessageProperties().setExpiration(sdDate);
                return message;
            });
		}
		return result > 0;
	}

	@Override
	public String selectXtsz(String szlb) {
		return xtyhDao.selectXtsz(szlb).getSzz();
	}

	/**
	 * 根据token获取用户信息
	 * @param tokenid
	 * @return
	 */
	public XtyhDto getDtoByToken(String tokenid){
		return dao.getDtoByToken(tokenid);
	}

	@Override
	public com.matridx.igams.common.dao.entities.User getYhInfo(String yhm) {
		return dao.getUserByName(yhm);
	}

	@Override
	public boolean updateSfsd(XtyhDto xtyhDto) {
		return xtyhDao.updateSfsd(xtyhDto) !=0;
	}


	/**
	 * 个人信息包含部门信息
	 * @param xtyhDto
	 */
	@Override
	public XtyhDto getPersonalData(XtyhDto xtyhDto) {
        return dao.getPersonalData(xtyhDto);
	}

	/**
	 * 机构名称
	 * @return
	 */
	public List<XtyhDto> getJgmc(){
        return dao.getJgmc();
	}

	/**
	 * 更新用户机构信息
	 * @param xtyhDto
	 */
	public boolean updateJg(XtyhDto xtyhDto){
        return dao.updateJg(xtyhDto);
	}

	/**
	 * 根据token_id查询钉钉用户信息
	 * @param token_id
	 * @return
	 */
	public XtyhDto getDdByToken(String token_id){
		return dao.getDdByToken(token_id);
	}

	/**
	 * 用户组列表选择可选择用户
	 * @param xtyhDto
	 * @return
	 */
	@Override
	public List<XtyhDto> getPagedYhzOptionalList(XtyhDto xtyhDto) {
		return dao.getPagedYhzOptionalList(xtyhDto);
	}

	/**
	 * 用户组列表选择已选择用户
	 * @param xtyhDto
	 * @return
	 */
	@Override
	public List<XtyhDto> getPagedYhzSelectedList(XtyhDto xtyhDto) {
		return dao.getPagedYhzSelectedList(xtyhDto);
	}

	/**
	 * 查询用户列表用于查询考勤
	 * @return
	 */
	public List<XtyhDto> getDtoList(){
		return dao.getDtoList();
	}
	/**
	 * 根据ddid查找yhid
	 * @param xtyhDto
	 */
	public XtyhDto getYhid(XtyhDto xtyhDto){
		return dao.getYhid(xtyhDto);
	}
	/**
     * 查询所有用户信息（yhm,sfsd,sdtime,jstime）
     * @return
     */
	public List<com.matridx.igams.common.dao.entities.User> getUsersList(){
		return dao.getUsersList();
	}
	/**
     * 查询oauth_client_details表里钉钉的用户信息
     * @return
     */
	public List<com.matridx.igams.common.dao.entities.User> getUsersByDdORWechatList(){
		return dao.getUsersByDdORWechatList();
	}
	/**
     * 查询外部安全的用户信息，用于验证外部接口用户
     * @return
     */
	public List<com.matridx.igams.common.dao.entities.User> getWbaqUsersList(){
		return dao.getWbaqUsersList();
	}
	@Override
	public boolean deleteInCompleteTaskById(String yhid) {
		return dao.deleteInCompleteTaskById(yhid);
	}

	@Override
	public boolean deleteInCompleteTaskByIds(XtyhDto xtyhDto) {
		return dao.deleteInCompleteTaskByIds(xtyhDto);
	}

	@Override
	public void getWbzyqx(com.matridx.igams.common.dao.entities.User user, String cdcc) {
		WbzyDto wbzyDto=new WbzyDto();
		if(StringUtils.isBlank(cdcc)) {
            cdcc="3";
        }
		wbzyDto.setCdcc(cdcc);
		List<YhjsDto> yhjslist = new ArrayList<>();
		List<WbzyqxDto> wbzyqxlist;
		YhjsDto yhjsDto=new YhjsDto();
		yhjsDto.setYhid(user.getYhid());
		yhjsDto.setJsid(user.getDqjs());
		yhjslist.add(yhjsDto);

		Object o_ddmenu = redisUtil.hget("Users_DingdingMenu",yhjslist.get(0).getJsid());
		if(o_ddmenu==null){
			wbzyDto.setYhjslist(yhjslist);
			wbzyDto.setDdzylb("Matridx_WeChat");
			wbzyqxlist=wbzyqxService.getWbzyqxList(wbzyDto);
			//用户角色只是用了一个角色
			redisUtil.hset("Users_DingdingMenu",yhjslist.get(0).getJsid(),JSON.toJSONString(wbzyqxlist),-1);
		}

		Object o_wcmenu = redisUtil.hget("Users_WeChatMenu",yhjslist.get(0).getJsid());
		if(o_wcmenu==null){
			wbzyDto.setZylb("Matridx_WeChat");
			wbzyDto.setDdzylb(null);
			wbzyqxlist=wbzyqxService.getWbzyqxList(wbzyDto);
			redisUtil.hset("Users_WeChatMenu",yhjslist.get(0).getJsid(),JSON.toJSONString(wbzyqxlist),-1);
		}
	}

	/**
	 * 导出
	 *
	 * @param xtyhDto
	 * @return
	 */
	public int getCountForSearchExp(XtyhDto xtyhDto, Map<String, Object> params) {
		return dao.getCountForSearchExp(xtyhDto);
	}

	/**
	 * 根据搜索条件获取导出信息
	 *
	 * @param params
	 * @return
	 */
	public List<XtyhDto> getListForSearchExp(Map<String, Object> params) {
		XtyhDto xtyhDto = (XtyhDto) params.get("entryData");
		queryJoinFlagExport(params, xtyhDto);
		return dao.getListForSearchExp(xtyhDto);
	}

	/**
	 * 根据选择信息获取导出信息
	 *
	 * @param params
	 * @return
	 */
	public List<XtyhDto> getListForSelectExp(Map<String, Object> params) {
		XtyhDto xtyhDto = (XtyhDto) params.get("entryData");
		queryJoinFlagExport(params, xtyhDto);
		return dao.getListForSelectExp(xtyhDto);
	}
	private void queryJoinFlagExport(Map<String, Object> params, XtyhDto xtyhDto) {
		List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
		StringBuilder sqlParam = new StringBuilder();
		for (DcszDto dcszDto : choseList) {
			if (dcszDto == null || dcszDto.getDczd() == null) {
                continue;
            }

			sqlParam.append(",");
			if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
				sqlParam.append(dcszDto.getSqlzd());
			}
			sqlParam.append(" ");
			sqlParam.append(dcszDto.getDczd());
		}
		String sqlcs = sqlParam.toString();
		xtyhDto.setSqlParam(sqlcs);
	}

	@Override
	public List<XtyhDto> getAllUserList(XtyhDto xtyhDto) {
		return dao.getAllUserList(xtyhDto);
	}

	/**
	 * 查询用户列表(不分页)
	 * @return
	 */
	public List<XtyhDto> getNoPagedDtoList(XtyhDto xtyhDto){
		return dao.getNoPagedDtoList(xtyhDto);
	}

	@Override
	public boolean aduitDimissionCallback(String processInstanceId, String processCode, String wbcxid) {
		logger.error("aduitDimissionCallback processInstanceId: " + processInstanceId);
		logger.error("aduitDimissionCallback processCode: " + processCode);
		logger.error("aduitDimissionCallback wbcxid: " + wbcxid);
		String token = talkUtil.getToken(wbcxid);
		WbcxDto dtoById = wbcxDao.getDtoById(wbcxid);
		GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult approveInfo = talkUtil.getApproveInfo(token, processInstanceId);
		if (ApproveStatusEnum.COMPLETED.code.equals(approveInfo.getStatus())&&"agree".equals(approveInfo.getResult())) {
			List<XtyhDto> xtyhDtos = new ArrayList<>();
			XtyhDto xtyhDto = new XtyhDto();
			xtyhDto.setWbcxid(wbcxid);
			xtyhDto.setSfsd("1");
			//发起人钉钉id
			xtyhDto.setDdid(approveInfo.getOriginatorUserId());
			xtyhDtos.add(xtyhDto);
			//锁定离职用户
			dao.updateXtyhDtos(xtyhDtos);
			if("1".equals(configflg)&&"1".equals(dtoById.getSfgx())) {
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.XTYH_MODLIST.getCode() + JSONObject.toJSONString(xtyhDtos));
			}
			if (StringUtil.isNotBlank(dtoById.getHddz())){
				List<String> yhms = dao.getYhmByUsers(xtyhDtos);
				MultiValueMap<String, String> qj_paramMap = new LinkedMultiValueMap<>();
				qj_paramMap.add("yhmsJson", JSON.toJSONString(yhms));
				RestTemplate restTemplate_t = new RestTemplate();
				restTemplate_t.postForObject(dtoById.getHddz() + "/ws/web/lockUser", qj_paramMap, Map.class);
			}
		}
		return true;
	}
	@Override
	public boolean lockUser(String yhmsJson) {
		List<String> yhms = JSON.parseArray(yhmsJson, String.class);
		dao.lockUsersByYhms(yhms);
		khzyfpService.deleteByYhms(yhms);
		return true;
	}

	/**
	 * @Description 获取指定时间内的离职审批信息
	 * @Param day 天数 最多120天
	 * @Return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void getDimissionAuditInfo(Map<String,String> map) throws BusinessException {
		int day = Integer.parseInt(map.get("day"));
		if (day<-120||day>0){
			throw new BusinessException("msg","获取审批信息不能超过120天或大于0");
		}
		WbcxDto wbcxDto = new WbcxDto();
		wbcxDto.setLx(CharacterEnum.DINGDING.getCode());
		wbcxDto.setSftb("1");
		List<WbcxDto> dtoList = wbcxDao.getDtoList(wbcxDto);
		for (WbcxDto wbcxDto_t : dtoList) {
			String token = talkUtil.getToken(wbcxDto_t.getWbcxid());
			JcsjDto jcsjDto_dd = new JcsjDto();
			jcsjDto_dd.setCsdm("L");
			jcsjDto_dd.setJclb("DINGTALK_AUDTI_CALLBACK_TYPE");
			jcsjDto_dd.setCskz3(wbcxDto_t.getWbcxid());
			jcsjDto_dd = jcsjService.getDtoByCsdmAndJclb(jcsjDto_dd);
			if (StringUtil.isBlank(jcsjDto_dd.getCsmc()) || StringUtil.isBlank(jcsjDto_dd.getCskz1())) {
				throw new BusinessException("msg", "请设置离职的钉钉审批回调类型基础数据！");
			}
			Date date = new Date();
			List<String> approveList = talkUtil.getApproveList(token, jcsjDto_dd.getCskz1(), DateUtils.getDate(date, day).getTime(), date.getTime(), null);
			if (!CollectionUtils.isEmpty(approveList)) {
				List<XtyhDto> xtyhDtos = new ArrayList<>();
				for (String processInstanceId : approveList) {
					GetProcessInstanceResponseBody.GetProcessInstanceResponseBodyResult approveInfo = talkUtil.getApproveInfo(token, processInstanceId);
					//审批完成的
					if (ApproveStatusEnum.COMPLETED.code.equals(approveInfo.getStatus()) && "agree".equals(approveInfo.getResult())) {
						XtyhDto xtyhDto = new XtyhDto();
						xtyhDto.setWbcxid(wbcxDto_t.getWbcxid());
						xtyhDto.setSfsd("1");
						//发起人钉钉id
						xtyhDto.setDdid(approveInfo.getOriginatorUserId());
						xtyhDtos.add(xtyhDto);
					}
				}
				if (!CollectionUtils.isEmpty(xtyhDtos)) {
					//锁定离职用户
					xtyhDao.updateXtyhDtos(xtyhDtos);
					if("1".equals(configflg)) {
						amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.XTYH_MODLIST.getCode() + JSONObject.toJSONString(xtyhDtos));
					}
				}
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String,String> insertExternalUser(XtyhDto xtyhDto,HttpServletRequest request){
		Map<String,String> map=new HashMap<>();
		map.put("status","success");
		map.put("message","保存成功!");
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		//初始密码
		XtszDto xtszDto = xtszDao.getDtoById("user.initpw");
		xtyhDto.setMm(bpe.encode(xtszDto.getSzz()));
		if(StringUtil.isNotBlank(xtyhDto.getJgid())) {
			YhssjgDto yhssjgDto=new YhssjgDto();
			yhssjgDto.setYhid(xtyhDto.getYhid());
			yhssjgDto.setJgid(xtyhDto.getJgid());
			yhssjgDto.setXh("1");
			yhssjgDto.setPrefix(prefixFlg);
			boolean result1=yhssjgService.insert(yhssjgDto);
			if(!result1) {
				map.put("status","fail");
				map.put("message","保存失败!");
				return map;
			}
			if("1".equals(configflg)) {
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.SSJG_ADD.getCode() + JSONObject.toJSONString(yhssjgDto));
			}
		}
		if(!CollectionUtils.isEmpty(xtyhDto.getXtjsDtos())){
			List<YhjsModel> yhjsList= new ArrayList<>();
			for(int i=0;i<xtyhDto.getXtjsDtos().size();i++){
				if(StringUtil.isBlank(xtyhDto.getXtjsDtos().get(i).getJsid())) {
					//如果角色ID为空则判断为新添加一个角色
					XtjsDto xtjsDto=new XtjsDto();
					xtjsDto.setJsid(StringUtil.generateUUID());
					xtyhDto.getXtjsDtos().get(i).setJsid(xtjsDto.getJsid());
					xtjsDto.setJsmc(xtyhDto.getXtjsDtos().get(i).getJsmc());
					xtjsDto.setDwxdbj("1");
					xtjsDao.insert(xtjsDto);
					JszyczbDto jszyczbDto=new JszyczbDto();
					jszyczbDto.setJsid(xtjsDto.getJsid());
					jszyczbDto.setCzdm("menupower");
					Object xtsz=redisUtil.hget("matridx_xtsz","default.menu.power");
					if(xtsz!=null) {
						XtszDto t_xtszDto = JSON.parseObject(String.valueOf(xtsz), XtszDto.class);
						if(StringUtil.isNotBlank(t_xtszDto.getSzz())){
							jszyczbDto.setZyid(t_xtszDto.getSzz());
						}
					}else{
						jszyczbDto.setZyid("000101");
					}
					jszyczbService.insert(jszyczbDto);
					amqpTempl.convertAndSend("sys.igams",preRabbitFlg + "sys.igams.xtjs.update",JSONObject.toJSONString(xtjsDto));

				}
				YhjsModel yhjsModel = new YhjsModel();
				yhjsModel.setYhid(xtyhDto.getYhid());
				yhjsModel.setJsid(xtyhDto.getXtjsDtos().get(i).getJsid());
				yhjsModel.setPrefix(prefixFlg);
				yhjsList.add(yhjsModel);
				if("1".equals(configflg)) {
					amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.YHJS_ADD.getCode() + JSONObject.toJSONString(yhjsModel));
				}
			}
			yhjsDao.batchInsert(yhjsList);
			xtyhDto.setDqjs(yhjsList.get(0).getJsid());
		}
		//在插入角色后才执行用户插入，主要是为了更新当前角色
		int result = dao.insert(xtyhDto);
		//添加用户机构权限
		if(!CollectionUtils.isEmpty(xtyhDto.getYhjgqxDtos())) {
			for (int i = 0; i < xtyhDto.getYhjgqxDtos().size(); i++){
				List<YhjgqxDto> yhjgqxList= new ArrayList<>();
				if(xtyhDto.getYhjgqxDtos().get(i).getJgids()!=null&&xtyhDto.getYhjgqxDtos().get(i).getJsid()!=null) {
					for (int j = 0; j < xtyhDto.getYhjgqxDtos().get(i).getJgids().size(); j++){
						YhjgqxDto yhjgqxDto=new YhjgqxDto();
						yhjgqxDto.setYhid(xtyhDto.getYhid());
						yhjgqxDto.setXh(j+1+"");
						yhjgqxDto.setJsid(xtyhDto.getYhjgqxDtos().get(i).getJsid());
						yhjgqxDto.setJgid(xtyhDto.getYhjgqxDtos().get(i).getJgids().get(j));
						yhjgqxDto.setPrefix(prefixFlg);
						yhjgqxList.add(yhjgqxDto);
					}
					if(!CollectionUtils.isEmpty(yhjgqxList)){
						yhjgqxDao.insertList(yhjgqxList);
						if("1".equals(configflg)) {
							amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.JGQX_ADD.getCode() + JSONObject.toJSONString(yhjgqxList));
						}
					}
				}
			}
		}

		dao.addClientDetails(xtyhDto);

		xtyhDto.setPrefix(prefixFlg);
		if(result > 0){
			if("1".equals(configflg)) {
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate",RabbitEnum.XTYH_MOD.getCode() + JSONObject.toJSONString(xtyhDto));
			}
		}

		//处理系统权限
		List<String> xtqxlist=xtyhDto.getXtqxlist();
		List<JsxtqxDto> jsxtqxDtos=new ArrayList<>();
		JsxtqxDto t_jsxtqxDto=new JsxtqxDto();
		t_jsxtqxDto.setJsid(xtyhDto.getXtjsDtos().get(0).getJsid());
		t_jsxtqxDto.setJsmc(xtyhDto.getXtjsDtos().get(0).getJsmc());
		if(!CollectionUtils.isEmpty(xtqxlist)){
			for(String xtqx:xtqxlist){
				JsxtqxDto jsxtqxDto=new JsxtqxDto();
				jsxtqxDto.setXtid(xtqx);
				jsxtqxDtos.add(jsxtqxDto);
			}
			t_jsxtqxDto.setJsxtqxDtos(jsxtqxDtos);
		}
		jsxtqxService.insertJsxtqxDto(t_jsxtqxDto);//插入角色系统权限信息
		return map;
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String,String> postToSxSaveUser(XtyhDto xtyhDto, HttpServletRequest request){
		String bgfsdm="";
		List<HbqxDto> hbqxDtos=hbqxService.getHbxxByYhid(xtyhDto.getYhid());
		if(!CollectionUtils.isEmpty(hbqxDtos)){
			bgfsdm=hbqxDtos.get(0).getBgfsdm();
		}
		Map<String,String> msg=new HashMap<>();
		msg.put("status","success");
		msg.put("message","保存成功!");
		//获取已勾选的系统权限
		JcsjDto jcsjDto=new JcsjDto();
		jcsjDto.setIds(xtyhDto.getXtqxlist());
		List<JcsjDto> jcsjDtos=jcsjService.getDtoList(jcsjDto);
		if(!CollectionUtils.isEmpty(jcsjDtos)){
			String access_token=xtyhDto.getAccess_token();
			RestTemplate restTemplate=new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			for(JcsjDto jcsjDto1:jcsjDtos){
				try {
					if("deterpret".equals(jcsjDto1.getCsdm())){
						Map<String,Object> map=new HashMap<>();
						map.put("username",xtyhDto.getYhm());//用户名
						map.put("name",xtyhDto.getZsxm());//账号名称
						map.put("is_external_user",true);//是否外部用户
						map.put("external_lab",request.getParameter("jcdwmc"));//检测单位
						map.put("permissions",xtyhDto.getPermissions());//权限列表
						map.put("tag_name",bgfsdm);//报告方式代码
						map.put("display_tag", "1".equals(request.getParameter("display_tag")));//样本是否需要展示给报告组
						//请求8000系统添加外部用户
						HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(map), headers);
						//"https://mngs.matridx.com/core/create_user/"
						if(bioaudurl.endsWith("/"))
							bioaudurl = bioaudurl.substring(0, bioaudurl.length() - 1);
						restTemplate.postForObject(bioaudurl +"/core/create_user/?access_token="+access_token, entity,Map.class);

					}else if("bcl".equals(jcsjDto1.getCsdm())){
						//测特8002
						Map<String,Object> map=new HashMap<>();
						map.put("username",xtyhDto.getYhm());//用户名
						map.put("organization",request.getParameter("jcdwmc"));
						map.put("machine_uid",request.getParameter("cxybh"));//测序仪编号
						map.put("machine_name",request.getParameter("mc"));//测序仪名称
						map.put("index2_reversed","1".equals(request.getParameter("jt2bj")));//接头2标记
						map.put("machine_model",request.getParameter("xh"));
						HttpEntity<String> entity = new HttpEntity<>(JSONObject.toJSONString(map), headers);
						if(biosequrl.endsWith("/"))
							biosequrl = biosequrl.substring(0, biosequrl.length() - 1);
						restTemplate.postForObject(biosequrl + "/BCL/api/create-org/" +"?access_token="+access_token,entity,Map.class);
					}
				}catch (Exception e){
					msg.put("status","fail");
					msg.put("message",e.getMessage());
				}
			}
		}
		return msg;
	}

	/**
	 * 通过yhm查找用户信息
	 * @param xtyhDto
	 * @return
	 */
	public List<XtyhDto> getListByYhms(XtyhDto xtyhDto){
		return dao.getListByYhms(xtyhDto);
	}


}
