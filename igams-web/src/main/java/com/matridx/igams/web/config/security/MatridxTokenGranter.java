package com.matridx.igams.web.config.security;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.web.dao.entities.XtyhDto;
import com.matridx.igams.web.service.svcinterface.IXtyhService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.AbstractTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * NpriClientCredentialsTokenEndpointFilter 验证通过后会执行 /oauth/token,从而调用各种扩展的 NpriAuthentication
 * 比如短信验证，手机验证等
 * 
 * 但也可以把这种验证放到NpriDaoAuthenticationProvider 里做
 * @author linwu
 *
 */
public class MatridxTokenGranter extends AbstractTokenGranter{
	private static final String GRANT_TYPE = "matridx";
	public final String USERNAME = "client_id";
	public final String PASSWORD = "client_secret";
	public final String SING = "sign";
	protected OAuth2RequestFactory requestFactory;
	private final Logger log = LoggerFactory.getLogger(MatridxTokenGranter.class);
	public MatridxTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
		this(tokenServices, clientDetailsService, requestFactory, MatridxTokenGranter.GRANT_TYPE);
	}

	public MatridxTokenGranter(AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory, String grantType) {
		super(tokenServices, clientDetailsService, requestFactory, grantType);
		this.requestFactory = requestFactory;
	}

	protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
		Map<String, String> parameters = new LinkedHashMap<>(tokenRequest.getRequestParameters());
		
		String username = (String)parameters.get(USERNAME);
		String password = (String)parameters.get(PASSWORD);
		String sign = (String)parameters.get(SING);
		parameters.remove(PASSWORD);
		if(StringUtil.isNotBlank(password))
		{
			Base64 base64 = new Base64();
            password = new String(base64.decode(password), StandardCharsets.UTF_8);
        }
        
		try {
			IXtyhService xtyhService = (IXtyhService)ServiceFactory.getService("xtyhServiceImpl");//获取方法所在class
			DataSource postsqlDataSource = (DataSource)ServiceFactory.getService("postsqlDataSource");//获取方法所在class
			//从redis获取用户
			RedisUtil redisUtil = (RedisUtil)ServiceFactory.getService("redisUtil");
			User user = redisUtil != null ? redisUtil.hugetDto("Users", username) : null;
			XtyhDto t_xtyhDto = new XtyhDto();
			//判断redis中是否存在数据，如果为空，从数据库查询
			if(user == null) {
				t_xtyhDto = xtyhService.getDtoByName(username);
				//判断是否存在用户信息,不存在根据ddid查询
				if(t_xtyhDto == null) {
					XtyhDto tmp_xtyhDto = new XtyhDto();
					tmp_xtyhDto.setDdid(username);
					//判断是否为钉钉或者微信用户
					t_xtyhDto = xtyhService.getXtyhByUserid(tmp_xtyhDto);
					t_xtyhDto.setLoginFlg("1");
				}
				//判断有没有查到用户信息，查到存入redis
                User redisUser = new User();
                redisUser.setYhm(t_xtyhDto.getYhm());
                redisUser.setZsxm(t_xtyhDto.getZsxm());
                redisUser.setSfsd(t_xtyhDto.getSfsd());
                redisUser.setSdtime(t_xtyhDto.getSdtime());
                redisUser.setJstime(t_xtyhDto.getJstime());
                redisUser.setYhid(t_xtyhDto.getYhid());
                redisUser.setDqjs(t_xtyhDto.getDqjs());
                redisUser.setDdid(t_xtyhDto.getDdid());
                redisUser.setWechatid(t_xtyhDto.getWechatid());
                redisUser.setMm(t_xtyhDto.getMm());
                redisUser.setMmxgsj(t_xtyhDto.getMmxgsj());
                redisUser.setGwmc(t_xtyhDto.getGwmc());
                redisUser.setDdtxlj(t_xtyhDto.getDdtxlj());
                redisUser.setEmail(t_xtyhDto.getEmail());
                redisUser.setUnionid(t_xtyhDto.getUnionid());
                redisUser.setWbcxid(t_xtyhDto.getWbcxid());
                //因为是用微信ID或 钉钉ID登录的，所以需要设置标记
                redisUser.setLoginFlg(t_xtyhDto.getLoginFlg());

                log.error(" getOAuth2Authentication redisUtil.hset Users :" + username + ":" + JSON.toJSONString(redisUser) );
                redisUtil.hset("Users",username,JSON.toJSONString(redisUser),-1);
            }else { //如果存在用户信息，数据放入t_xtyhDto
				t_xtyhDto.setYhm(user.getYhm());
				t_xtyhDto.setSfsd(user.getSfsd());
				t_xtyhDto.setSdtime(user.getSdtime());
				t_xtyhDto.setJstime(user.getJstime());
				t_xtyhDto.setYhid(user.getYhid());
				t_xtyhDto.setDqjs(user.getDqjs());
				t_xtyhDto.setDdid(user.getDdid());
				t_xtyhDto.setWechatid(user.getWechatid());
				t_xtyhDto.setMm(user.getMm());
				t_xtyhDto.setMmxgsj(user.getMmxgsj());
				t_xtyhDto.setGwmc(user.getGwmc());
				t_xtyhDto.setDdtxlj(user.getDdtxlj());
				t_xtyhDto.setEmail(user.getEmail());
				t_xtyhDto.setUnionid(user.getUnionid());
				t_xtyhDto.setLoginFlg(user.getLoginFlg());
				t_xtyhDto.setWbcxid(user.getWbcxid());
			}
			
			JdbcClientDetailsService jdbcClientDetailsService= new JdbcClientDetailsService(postsqlDataSource);
	        ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(username);
	        //如果为钉钉扫码登录，则判断相应的验证信息
	        if(StringUtil.isNotBlank(sign)) {
	        	DBEncrypt dbencry = new DBEncrypt();
	        	if(dbencry.dCode(sign).equals(password)) {
	        		//登录成功，更新错误次数
					loginSucess(xtyhService,t_xtyhDto);
	        	}else {
					loginFail(xtyhService,t_xtyhDto);
					throw new InvalidGrantException("用户名或者密码错误，请重新输入。");
	        	}
	        } else{
				BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
				if (!bpe.matches(password, clientDetails.getClientSecret())) {
					loginFail(xtyhService,t_xtyhDto);
					throw new InvalidGrantException("用户名或者密码错误，请重新输入。");
				}else {
					//登录成功，更新错误次数，判断是否是钉钉登录，钉钉登陆不更新错误次数
					if(!"1".equals(t_xtyhDto.getLoginFlg())) {
						loginSucess(xtyhService,t_xtyhDto);
					}					
				}
			}
			//userAuth = this.authenticationManager.authenticate(userAuth);
			Authentication userAuth = new UsernamePasswordAuthenticationToken(username, password,client.getAuthorities());
			((AbstractAuthenticationToken)userAuth).setDetails(parameters);
			OAuth2Request storedOAuth2Request = this.getRequestFactory().createOAuth2Request(client, tokenRequest);
			
			return new OAuth2Authentication(storedOAuth2Request, userAuth);
			
		} catch (AccountStatusException | BadCredentialsException var8) {
			throw new InvalidGrantException(var8.getMessage());
		}
    }
	
	/**
	 * 密码错误时更新用户的错误次数，并进行提示
	 * @param xtyhDto
	 * @return
	 */
	private boolean loginFail(IXtyhService xtyhService,XtyhDto xtyhDto) {
//		 User user = xtyhService.getYhInfo(xtyhDto.getYhm());
		 String time ="5分钟";
		if("1".equals(xtyhDto.getSfsd())){
			sdtime(xtyhDto,time);
		}
		//最大限制数
		int maxLimit = 5;
		String errString = null;
        //在当日判断错误次数（若错误次数数大于3，则报提示）
		int currentInt = 0;
		if(StringUtil.isNotBlank(xtyhDto.getCwcs()))
		{
			currentInt = Integer.parseInt(xtyhDto.getCwcs());
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  		if (maxLimit > currentInt + 1) {
  			if ((maxLimit - currentInt - 1) <= 2) {
  				String num = String.valueOf(maxLimit - currentInt - 1);
				errString = "请注意，您还有"+ num + "次机会尝试，之后账户将被锁定。请使用忘记密码 功能进行找回";
			}
  		}else {
  			xtyhDto.setSfsd("1");
  			int count = currentInt+1;
  			String str = "login.ErrorLimit"+count;
  			if (count>=9){
  				str = "login.ErrorLimitLater";
			}
			long sdsj = Long.parseLong(xtyhService.selectXtsz(str));
			DateFormat format2 = new SimpleDateFormat("HH时mm分ss秒");
			DateFormat format3 = new SimpleDateFormat("mm分ss秒");
			DateFormat format4 = new SimpleDateFormat("ss秒");
			Date date1 = new Date(sdsj - 28800000L);
			if (sdsj >= 3600000L){
				time = format2.format(date1);
			}else if (sdsj < 60000 && sdsj > 0){
				time = format4.format(date1);
			}else{
				time = format3.format(date1);
			}
  			errString = "您已被锁定！锁定剩余时间："+time;
			Date now = new Date();
			xtyhDto.setSdtime(sdf.format(now));
			xtyhDto.setJstime(sdf.format(new Date(now.getTime() + sdsj) ));
			User redisUser = new User();
			redisUser.setYhm(xtyhDto.getYhm());
			redisUser.setZsxm(xtyhDto.getZsxm());
			redisUser.setSfsd(xtyhDto.getSfsd());
			redisUser.setSdtime(xtyhDto.getSdtime());
			redisUser.setJstime(xtyhDto.getJstime());
			//redisUser.setJsids(xtyhDto.getJsids());
			redisUser.setYhid(xtyhDto.getYhid());
			redisUser.setDqjs(xtyhDto.getDqjs());
			redisUser.setDqjsdm(xtyhDto.getDqjsdm());
			redisUser.setDqjsmc(xtyhDto.getDqjsmc());
			redisUser.setDdid(xtyhDto.getDdid());
			redisUser.setJgid(xtyhDto.getJgid());
			redisUser.setMm(xtyhDto.getMm());
			redisUser.setMmxgsj(xtyhDto.getMmxgsj());
			redisUser.setGwmc(xtyhDto.getGwmc());
			redisUser.setDdtxlj(xtyhDto.getDdtxlj());
			redisUser.setEmail(xtyhDto.getEmail());
			redisUser.setUnionid(xtyhDto.getUnionid());
			redisUser.setWbcxid(xtyhDto.getWbcxid());
			RedisUtil redisUtil = (RedisUtil) ServiceFactory.getService("redisUtil");
			redisUtil.hset("Users",redisUser.getYhm(),JSON.toJSONString(redisUser),-1);
  		}
  		xtyhDto.setXgry(xtyhDto.getYhid());
  		xtyhDto.setCwcs(String.valueOf(currentInt + 1));
    	xtyhService.updateLoginInfo(xtyhDto);
    	
    	if(StringUtil.isNotBlank(errString)) {
    		throw new InvalidGrantException(errString);
    	}
  		return true;
	}
	
	private boolean loginSucess(IXtyhService xtyhService,XtyhDto xtyhDto) {

//		User user = xtyhService.getYhInfo(xtyhDto.getYhm());
		String time ="5分钟";
		if("1".equals(xtyhDto.getSfsd())){
			sdtime(xtyhDto,time);
		}
		//在当日判断错误次数（若错误次数数大于3，则报提示）
		xtyhDto.setCwcs("0");
		xtyhDto.setXgry(xtyhDto.getYhid());
		// TODO: handle exception
    	xtyhService.updateLoginInfo(xtyhDto);
  		return true;
	}

	private void sdtime(XtyhDto xtyhDto,String time){
		DateFormat format = new SimpleDateFormat("HH时mm分ss秒");
		DateFormat format3 = new SimpleDateFormat("mm分ss秒");
		DateFormat format4 = new SimpleDateFormat("ss秒");
		DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String jstime = xtyhDto.getJstime();
		if (StringUtil.isBlank(jstime)){
			throw new InvalidGrantException( xtyhDto.getYhm() + "用户初始状态为锁定状态！请先联系管理员解锁");
		}
		try {
			Date date = new Date();
			Date date2 = format2.parse(jstime);
			long sdLongTime = date2.getTime() - date.getTime();
			Date date1 = new Date(sdLongTime - 28800000L);
			if (sdLongTime >= 3600000){
				time = format.format(date1);
			}else if (sdLongTime < 60000 && sdLongTime > 0){
				time = format4.format(date1);
			}else if (sdLongTime < 0){
				throw new InvalidGrantException( xtyhDto.getYhm() + "正在解锁请稍候！");
			}else{
				time = format3.format(date1);
			}
		} catch (ParseException e) {
			log.error(e.toString());
		}
		throw new InvalidGrantException( xtyhDto.getYhm() + "已被锁定，请先解锁！锁定剩余时间："+time);
	}
}
