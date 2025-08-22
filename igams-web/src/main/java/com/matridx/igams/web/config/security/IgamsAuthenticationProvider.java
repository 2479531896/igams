package com.matridx.igams.web.config.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.service.svcinterface.IXtyhService;

/**
 * 自定义授权功能 关联IgamsSecurityConfig  暂未使用，功能已经做到 xtyhService里
 * 
 * UserDetailsService是一个纯粹用于获取用户数据的DAO，没有任何其他功能，除了提供框架中其他组件需要的数据。
 * 特别的，其并不验证用户，验证是通过 AuthenticationManager完成。
 * 在很多场景下，如果我们需要自定义的验证过程，需要直接实现 AuthenticationProvider。
 * @author linwu
 *
 */
@Component
public class IgamsAuthenticationProvider implements AuthenticationProvider{

	@Autowired
	private IXtyhService xtyhService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		// TODO Auto-generated method stub
		String name = authentication.getName();
	    String password = authentication.getCredentials().toString();
	    //设置用户信息
	    XtyhDto dto = new XtyhDto();
	    dto.setYhm(name);
	    dto.setMm(password);

	    XtyhDto checkDto = xtyhService.authUser(dto);
	    if (checkDto == null) {  
	    	throw new AuthenticationCredentialsNotFoundException("Account is not found.");  
	    }  
	  
	    List<GrantedAuthority> grantedAuths = new ArrayList<>();
	    for(int i=0;i<checkDto.getYhjsDtos().size();i++){
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(checkDto.getYhjsDtos().get(i).getJsmc());
			
			grantedAuths.add(grantedAuthority);
		}
	    return new UsernamePasswordAuthenticationToken(name, password, grantedAuths);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		// TODO Auto-generated method stub
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
