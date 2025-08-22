package com.matridx.igams.web.config.security;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 2021-05-03  --3
 * 
 * 默认的DaoAuthenticationProvider 无法增加 更新错误次数等扩展，所以需要 继承然后在NpriClientCredentialsTokenEndpointFilter
 * 里进行设置 Provider
 * 
 * 暂时设置为不作任何处理，所有的验证都交由后面的进行，否则手机验证等做不了
 * @author linwu
 *
 */
public class MatridxDaoAuthenticationProvider extends DaoAuthenticationProvider{
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");

			throw new BadCredentialsException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials",
					"Bad credentials"));
		}
		/*
		IXtyhService xtyhService = (IXtyhService)ServiceFactory.getService("xtyhServiceImpl");//获取方法所在class
		
		XtyhDto t_xtyhDto = xtyhService.getDtoByName(authentication.getName());
		//如果用户已经锁定
		if("1".equals(t_xtyhDto.getSfsd())) {
			
		}
		
		String presentedPassword = authentication.getCredentials().toString();

		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		if (!bpe.matches(presentedPassword, userDetails.getPassword())) {
			
			
			updateLoginFail(t_xtyhDto);

			throw new BadCredentialsException(messages.getMessage(
					"AbstractUserDetailsAuthenticationProvider.badCredentials",
					"Bad credentials"));
		}
		*/
	}
	
	/**
	 * 密码错误时更新用户的错误次数，并进行提示
	 * @param xtyhDto
	 * @return
	 */
	/*
	private boolean updateLoginFail(XtyhDto xtyhDto) {

		//最大限制数
		int maxLimit = 5;
		
		
		// TODO: handle exception
    	xtyhService.updateLoginFaild(xtyhDto);
    	
        //在当日判断错误次数（若错误次数数大于3，则报提示）
    	int currentInt = Integer.parseInt(xtyhDto.getCwcs());
    	
  		if (maxLimit > currentInt) {
  			if ((maxLimit - currentInt - 1) <= 2) {
  				String num = String.valueOf(maxLimit - currentInt - 1);
  				throw new InvalidGrantException("请注意，您还有"+ num + "次机会尝试，之后账户将被锁定。");
  			}
  		}else {
			throw new InvalidGrantException("您已经超过最高限制！");
  		}
  		return true;
	}*/
}
