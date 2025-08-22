package com.matridx.igams.web.config.security;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.JsxtqxDto;
import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.post.IJsxtqxDao;
import com.matridx.igams.common.dao.post.IXtszDao;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.security.IgamsGrantedAuthority;
import com.matridx.igams.web.dao.entities.ClientDto;
import com.matridx.igams.web.dao.entities.YhjsDto;
import com.matridx.igams.web.dao.post.IJszyczbDao;
import com.matridx.igams.web.dao.post.IXtyhDao;
import com.matridx.igams.web.dao.post.IYhjsDao;
import org.eclipse.jetty.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class IgamsClientDetailService implements ClientDetailsService{

	@Autowired
	private DataSource postsqlDataSource;

	@Autowired
	private IJsxtqxDao jsxtqxDao;
	@Autowired
	private IXtyhDao xtyhDao;
	@Autowired
	private IYhjsDao yhjsDao;
	@Autowired
	private IJszyczbDao jszyczbDao;
	@Autowired
	private IXtszDao xtszDao;
	@Autowired
	RedisUtil redisUtil;
	
	Logger logger = LoggerFactory.getLogger(IgamsClientDetailService.class);
	
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		
		SecurityContext securityContext = SecurityContextHolder.getContext();
		
		// 获取当前认证了的 principal(当事人),或者 request token (令牌)
		// 如果没有认证，会是 null
		Authentication authentication = securityContext.getAuthentication();

		if(authentication!=null)
		{
			@SuppressWarnings("unchecked")
			List<IgamsGrantedAuthority> authorities = (List<IgamsGrantedAuthority>)authentication.getAuthorities();

			if(!CollectionUtils.isEmpty(authorities)) {
				// TODO Auto-generated method stub
				ClientDto clientDto = new ClientDto();
				//判断redis是否存在数据，如果不存在，去数据库拿
				String s_obj = (String)redisUtil.hget("ClientDtos", clientId);
				
				if(s_obj != null) {
					clientDto = JSON.parseObject(s_obj, ClientDto.class);
					Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
					grantedAuthorities.add(authorities.get(0));
					clientDto.setAuthorities(grantedAuthorities);
				}else {
					JdbcClientDetailsService jdbcClientDetailsService= new JdbcClientDetailsService(postsqlDataSource);
					ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(clientId);
			        //复制数据到clientDto
					copyClientDetails(clientDto,clientDetails);
					Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
					//将数据放入redis
			        redisUtil.hset("ClientDtos",clientDto.getClientId(),JSON.toJSONString(clientDto),-1);
					
					grantedAuthorities.add(authorities.get(0));
					clientDto.setAuthorities(grantedAuthorities);
				}
				/*
				JdbcClientDetailsService jdbcClientDetailsService= new JdbcClientDetailsService(postsqlDataSource);
				ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(clientId);
		        //复制数据到clientDto
				copyClientDetails(clientDto,clientDetails);
				Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
				
				grantedAuthorities.add(authorities.get(0));
				clientDto.setAuthorities(grantedAuthorities);
				*/
		        return clientDto;
			}
		}
		
		//先从redis中查询用户信息
		com.matridx.igams.common.dao.entities.User t_user = redisUtil.hugetDto("Users", clientId);
		//是否存入redis标记
		boolean flg = false;
		
		if ( t_user != null) {
			//若用户是锁定的，抛异常
			if ("1".equals(t_user.getSfsd())){
				
				String jsTime = t_user.getJstime();
				String errString = "您的账号已被无限制锁定，请联系管理员解决！";
				if(StringUtil.isNotBlank(jsTime)) {
					try {
						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						Date d_jstime = sdf.parse(jsTime);
						long jssj = d_jstime.getTime();
						DateFormat format2 = new SimpleDateFormat("HH时mm分ss秒");
						Date now = new Date();
						if (jssj - now.getTime() > 0){
							Date date1 = new Date(jssj - now.getTime() - 28800000L);
							String time = format2.format(date1);
							errString = "您已被锁定！锁定剩余时间："+time;
						}

					}catch(Exception e) {
						logger.error("jsTime:" + jsTime + " : " + e.getMessage());
					}
				}
				throw new BadCredentialsException(errString);
			}
		}else {
			flg = true;
		}
		
		JdbcClientDetailsService jdbcClientDetailsService= new JdbcClientDetailsService(postsqlDataSource);
        ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(clientId);
        ClientDto clientDto = new ClientDto();
        //复制数据到clientDto
        copyClientDetails(clientDto,clientDetails);
		//判断user是否为null,为null从数据库获取
		if(t_user == null) {
			t_user = xtyhDao.getUserByName(clientId);
			if(t_user!=null)
				t_user.setLoginFlg("0");
		}
        
        if(t_user == null){
			//判断是否为钉钉或者微信用户
			List<User> users = xtyhDao.getUserByDdORWechat(clientId);
			//若用户全为锁定，取第一个；若用户中有不锁定的，取第一个不锁定的用户
			if (!CollectionUtils.isEmpty(users)){
				t_user = users.get(0);
				t_user.setLoginFlg("1");
				for (User lsuser : users) {
					if ("0".equals(lsuser.getSfsd())){
						t_user = lsuser;
						t_user.setLoginFlg("1");
						break;
					}
				}
			}
			
			if(t_user == null) {
				throw new BadCredentialsException("用户："+ clientId + "或者密码不正确！");
			}
				
		}
        
        if(flg) {
        	t_user.setLhmc("loadClientByClientId");
        	logger.error(" loadClientByClientId redisUtil.hset Users :" + clientId + ":" + JSON.toJSONString(t_user) );
        	redisUtil.hset("Users",clientId,JSON.toJSONString(t_user),-1);
        }
        
        if(!"1".equals(t_user.getLoginFlg())) {
            getAuthorByUser(t_user);
        }
        redisUtil.hset("ClientDtos",clientDto.getClientId(),JSON.toJSONString(clientDto),-1);
        GrantedAuthority grantedAuthority = new IgamsGrantedAuthority(t_user);
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		
		grantedAuthorities.add(grantedAuthority);
		clientDto.setAuthorities(grantedAuthorities);
		
        return clientDto;
	}

	/**
	 * 根据用户ID,获取用户的权限信息。现主要是登录验证时使用
	 * @param user
	 * @return
	 */
	private com.matridx.igams.common.dao.entities.User getAuthorByUser(com.matridx.igams.common.dao.entities.User user){
		
		//根据用户信息查找权限信息
		YhjsDto searchDto = new YhjsDto();
		searchDto.setYhid(user.getYhid());
		List<YhjsDto> yhqxDtos = yhjsDao.getDtoList(searchDto);
		
		if(yhqxDtos==null){
			throw new BadCredentialsException("用户："+ user.getYhm() + "没有权限！");
		}
		
		YhjsDto dq_yhjs = null;
		List<String> t_jsids = new ArrayList<>();
		List<String> t_jsmcs = new ArrayList<>();
        for (YhjsDto yhqxDto : yhqxDtos) {
            if (yhqxDto.getJsid().equals(user.getDqjs()))
                dq_yhjs = yhqxDto;
            t_jsids.add(yhqxDto.getJsid());
            t_jsmcs.add(yhqxDto.getJsmc());
        }
		if(dq_yhjs==null)
			dq_yhjs = yhqxDtos.get(0);

		//保存用户的角色列表
		user.setJsids(t_jsids);
		user.setJsmcs(t_jsmcs);
		//设置当前角色信息
		user.setDqjs(dq_yhjs.getJsid());
		user.setDqjsdm(dq_yhjs.getJsdm());
		user.setDqjsmc(dq_yhjs.getJsmc());
		user.setDqjsdwxdbj(dq_yhjs.getDwxdbj());
		//设置用户机构信息 2019-10-21 用于机构权限限制
		user.setJgid(dq_yhjs.getJgid());
		
		//查询当前角色的资源操作表
		QxModel t_qxDto = new QxModel();
		t_qxDto.setJsid(dq_yhjs.getJsid());
		List<QxModel> qxModels = jszyczbDao.getQxModelList(t_qxDto);
		//设置当前角色的资源操作表
		user.setQxModels(qxModels);

		//查询当前角色的系统资源表
		List<QxModel> nobtnmenuList = jszyczbDao.getNoButtonMenu(t_qxDto);
		user.setNobtnlistModels(nobtnmenuList);
		//查询用户的系统权限
		JsxtqxDto t_xtqxDto = new JsxtqxDto();
		t_xtqxDto.setJsid(dq_yhjs.getJsid());
		List<JsxtqxDto> xtqxModels = jsxtqxDao.getQxModelList(t_xtqxDto);
		//设置当前角色的系统权限表
		user.setXtqxDtos(xtqxModels);
		
		return user;
	}

	private boolean copyClientDetails(ClientDto clientDto,ClientDetails clientDetails) {
		clientDto.setYhid(clientDetails.getClientId());
		clientDto.setClientSecret(clientDetails.getClientSecret());
		//当用户信息里没有设置过期时间时，则会采用 DefaultTokenServices 的 accessTokenValiditySeconds 变量，是12个小时
		//clientDto.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds());
		// 现在设置为根据 系统设置来更改,先从基础数据查
        XtszDto xtszDto = xtszDao.getDtoById("login.token.maxtime");

		if(xtszDto != null && StringUtil.isNotBlank(xtszDto.getSzz()))
			clientDto.setAccessTokenValiditySeconds( 60 * 60 * Integer.parseInt(xtszDto.getSzz()));
		else
			clientDto.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds());
		clientDto.setAdditionalInformation(clientDetails.getAdditionalInformation());
		clientDto.setAuthorizedGrantTypes(clientDetails.getAuthorizedGrantTypes());
		clientDto.setRegisteredRedirectUri(clientDetails.getRegisteredRedirectUri());
		clientDto.setResourceIds(clientDetails.getResourceIds());
		clientDto.setScope(clientDetails.getScope());
		clientDto.setYhm(clientDetails.getClientId());
		clientDto.setRefreshTokenValiditySeconds(clientDetails.getRefreshTokenValiditySeconds());
		return true;
	}
}
