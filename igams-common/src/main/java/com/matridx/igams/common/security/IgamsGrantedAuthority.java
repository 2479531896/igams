package com.matridx.igams.common.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

import com.matridx.igams.common.dao.entities.User;

public class IgamsGrantedAuthority implements GrantedAuthority{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//用于存放用户信息
    final Map<String,Object> role= new HashMap<>();
	
	public IgamsGrantedAuthority(User user){
        role.put("user",user);
    }
	
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		User t_user = (User)role.get("user");
		if(t_user== null)
			return null;
		return t_user.getDqjsdm();
	}

	public User getYhxx(){
		return (User)role.get("user");
	}
}
