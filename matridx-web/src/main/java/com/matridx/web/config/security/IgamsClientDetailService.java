package com.matridx.web.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;

import com.matridx.igams.common.dao.entities.JsxtqxDto;
import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.igams.common.dao.post.IJsxtqxDao;
import com.matridx.igams.common.security.IgamsGrantedAuthority;
import com.matridx.web.dao.entities.ClientDto;
import com.matridx.web.dao.entities.YhjsDto;
import com.matridx.web.dao.post.IJszyczbDao;
import com.matridx.web.dao.post.IXtyhDao;
import com.matridx.web.dao.post.IYhjsDao;

public class IgamsClientDetailService implements ClientDetailsService{

	@Autowired
	private DataSource postsqlDataSource;
	@Autowired
	private IXtyhDao xtyhDao;
	@Autowired
	private IYhjsDao yhjsDao;
	@Autowired
	private IJszyczbDao jszyczbDao;
	@Autowired
	private IJsxtqxDao jsxtqxDao;
	
	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		// TODO Auto-generated method stub
		JdbcClientDetailsService jdbcClientDetailsService= new JdbcClientDetailsService(postsqlDataSource);
        ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(clientId);

        ClientDto clientDto = new ClientDto();
        copyClientDetails(clientDto,clientDetails);
        //查找用户信息
        com.matridx.igams.common.dao.entities.User user = xtyhDao.getUserByName(clientId);
        
        if(user == null){
			throw new BadCredentialsException("用户："+ clientId + "或者密码不正确！");
		}
      	user = getAuthorByUser(user);
        GrantedAuthority grantedAuthority = new IgamsGrantedAuthority(user);
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
		for(int i=0;i<yhqxDtos.size();i++){
			if(yhqxDtos.get(i).getJsid().equals(user.getDqjs())) {
                dq_yhjs = yhqxDtos.get(i);
            }
			t_jsids.add(yhqxDtos.get(i).getJsid());
			t_jsmcs.add(yhqxDtos.get(i).getJsmc());
		}
		if(dq_yhjs==null) {
            dq_yhjs = yhqxDtos.get(0);
        }

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
		//clientDto.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds());
		//当用户信息里没有设置过期时间时，则会采用 DefaultTokenServices 的 accessTokenValiditySeconds 变量，是12个小时
		//clientDto.setAccessTokenValiditySeconds(clientDetails.getAccessTokenValiditySeconds());
		// 现在设置为根据 超时 12小时
		clientDto.setAccessTokenValiditySeconds( 60 * 60 * 12);
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
