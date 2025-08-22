package com.matridx.web.config.security;

import com.matridx.igams.common.dao.entities.JsxtqxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.security.IgamsGrantedAuthority;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.web.dao.entities.XtyhModel;
import com.matridx.web.dao.post.IXtyhDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public class MatridxTokenServices extends DefaultTokenServices{
	
	@Autowired
	IXtyhDao xtyhDao;
	
	@Override
	@Transactional
	public OAuth2AccessToken createAccessToken(OAuth2Authentication authentication) throws AuthenticationException {
		//确认用户的系统代码权限和当前登录系统是否一致
		Map<String,String> par = authentication.getOAuth2Request().getRequestParameters();
		
		List<GrantedAuthority> authorities = (List<GrantedAuthority>)authentication.getAuthorities();
		
		IgamsGrantedAuthority authority = (IgamsGrantedAuthority)authorities.get(0);
		User user = (User)authority.getYhxx();
		
		if(user == null) {
            return null;
        }
		//更新登录时间
		XtyhModel xtyhModel = new XtyhModel();
		xtyhModel.setYhid(user.getYhid());
		xtyhDao.updateLastLogin(xtyhModel);
		
		//如果不为medlab-springboot ，则证明是从其他系统过来，则要进行系统权限判断
		String s_code = par.get("systemcode");
		//'if' 语句具有空体,故注释，2023/10/27
		if(StringUtil.isBlank(s_code)) {
			return super.createAccessToken(authentication);
		}else if(!"medlab-springboot".equals(s_code)&&!"matridx-springboot".equals(s_code)) {
			
			if(user.getXtqxDtos() == null) {
                return null;
            }
			boolean isFind = false;
			List<JsxtqxDto> jsxts = user.getXtqxDtos();
			for(JsxtqxDto jsxt:jsxts) {
				if(jsxt.getJsdm().equals(s_code)) {
					isFind = true;
					break;
				}
			}
			if(!isFind) {
                return null;
            }
		}
        return super.createAccessToken(authentication);
	}
}
