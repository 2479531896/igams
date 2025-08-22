package com.matridx.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.response.OapiV2DepartmentListsubResponse.DeptBaseResponse;
import com.dingtalk.api.response.OapiV2UserListResponse.ListUserResponse;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.post.IXtszDao;
import com.matridx.igams.common.enums.CharacterEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.security.IgamsGrantedAuthority;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.Encrypt;
import com.matridx.web.dao.entities.XtyhDto;
import com.matridx.web.dao.entities.XtyhModel;
import com.matridx.web.dao.entities.XtzyDto;
import com.matridx.web.dao.entities.YhjgqxDto;
import com.matridx.web.dao.entities.YhjsDto;
import com.matridx.web.dao.entities.YhjsModel;
import com.matridx.web.dao.entities.YhssjgDto;
import com.matridx.web.dao.post.IJszyczbDao;
import com.matridx.web.dao.post.IXtyhDao;
import com.matridx.web.dao.post.IXtzyDao;
import com.matridx.web.dao.post.IYhjsDao;
import com.matridx.web.service.svcinterface.IXtyhService;
import com.matridx.web.service.svcinterface.IYhjgqxService;
import com.matridx.web.service.svcinterface.IYhssjgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TokenEndpoint
@Service
public class XtyhServiceImpl extends BaseBasicServiceImpl<XtyhDto, XtyhModel, IXtyhDao> implements IXtyhService{

	@Autowired
	IYhjsDao yhjsDao;
	@Autowired
	IXtzyDao xtzyDao;
	@Autowired
	IJszyczbDao jszyczbDao;
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
	@Autowired
	DingTalkUtil talkUtil;
	@Autowired
	RedisUtil redisUtil;
	@Override
	public XtyhDto getDtoByName(String yhm) {
		// TODO Auto-generated method stub
		return dao.getDtoByName(yhm);
	}
	
	@Override
	public List<XtyhDto> getPagedDtoList(XtyhDto xtyhDto){
		//PageHelper.startPage(2, 1);
		return dao.getPagedDtoList(xtyhDto);
	}

	@Override
	public XtyhDto getDtoByYhm(XtyhDto xtyhDto) {
		// TODO Auto-generated method stub
		return dao.getDtoByYhm(xtyhDto);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Logger logger = LoggerFactory.getLogger(XtyhServiceImpl.class);
		
		logger.debug("-------------------用戶登陆-----------------");
		
		// TODO Auto-generated method stub
		if(StringUtil.isBlank(username)){
			throw new BadCredentialsException("用户："+ username + "或者密码不正确！");
		}
		//删除oauth_access_token中client_id和用户名相同的
		dao.deleteOauthAccessById(username);
		//把用户名转为小写
		//String lowerUserName = username.toLowerCase();
		//查找用户信息
		com.matridx.igams.common.dao.entities.User user = dao.getUserByName(username);
		//用户不存在的情况，抛出异常
		if(user == null){
			throw new BadCredentialsException("用户："+ username + "或者密码不正确！");
		}else if("1".equals(user.getSfsd())){
			throw new BadCredentialsException("用户："+ username + "已被锁定，请先解锁！");
		}
		
		//获取用户的所有权限，并且springsecurity 整合
		user = getAuthorByUser(user);
		
		GrantedAuthority grantedAuthority = new IgamsGrantedAuthority(user);
		Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		
		grantedAuthorities.add(grantedAuthority);
		//更新用户登录信息
		XtyhModel xtyhModel = new XtyhModel();
		xtyhModel.setYhid(user.getYhid());
		dao.updateLastLogin(xtyhModel);
		//返回一个springSecurity需要的用户对象
		return new User(user.getYhm(),user.getMm(),grantedAuthorities);
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
		
		if(result > 0)
			return true;
		return false;
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
		int result = dao.insert(xtyhDto);
		
		if(xtyhDto.getJsids()!=null && xtyhDto.getJsids().size() > 0){
			for(int i=0;i<xtyhDto.getJsids().size();i++){
				YhjsModel yhjsModel = new YhjsModel();
				yhjsModel.setYhid(xtyhDto.getYhid());
				yhjsModel.setJsid(xtyhDto.getJsids().get(i));
				yhjsDao.insert(yhjsModel);
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
		
		if(result > 0)
			return true;
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
		
		if(xtyhDto == null)
			return null;
		
		List<XtzyDto> xtzyDtos = null;
		//如果用户的上次角色信息没有的话，重新从权限表获取数据，随机取一个角色
		List<YhjsDto> yhjsDtos = yhjsDao.getDtoListById(xtyhDto.getYhid());
		if(StringUtil.isBlank(xtyhDto.getDqjs())){
			if(yhjsDtos!=null && yhjsDtos.size() > 0){
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
			for(int i=0;i<yhjsDtos.size();i++){
				YhjsDto yhjsDto = yhjsDtos.get(i);
				if(yhjsDto.getJsid().equals(xtyhDto.getDqjs())){
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
		
		if(t_xtyhDto == null)
			return null;
		
		List<XtzyDto> xtzyDtos = null;
		//如果用户的上次角色信息没有的话，重新从权限表获取数据，随机取一个角色
		List<YhjsDto> yhjsDtos = yhjsDao.getDtoListById(xtyhDto.getYhid());
		
		xtzyDtos = xtzyDao.getDtoListById(xtyhDto.getDqjs());
		
		// 重新new一个token，因为Authentication中的权限是不可变的,更改上下文信息
		changeToken(t_xtyhDto);
		
		Map<String, List<XtzyDto>> menuMap = dealMenuList(xtzyDtos);
		
		if(menuMap == null)
			return null;
		//用于判断是否是OA新首页
		if (!CollectionUtils.isEmpty(yhjsDtos)&&StringUtil.isNotBlank(xtyhDto.getDqjs())){
			List<YhjsDto> collect = yhjsDtos.stream().filter(e -> e.getJsid().equals(xtyhDto.getDqjs())).collect(Collectors.toList());
			if (!CollectionUtils.isEmpty(collect)){
				t_xtyhDto.setSylxdm(collect.get(0).getSylxdm());
				t_xtyhDto.setSylxlj(collect.get(0).getSylxlj());
			}
		}
		Map<String, Object> res = new HashMap<String, Object>();
		
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
		XtyhModel xtyh = new XtyhModel();
		
		//错误次数(为空则是0)
		int cwcs = Integer.valueOf(StringUtil.isBlank(user.getCwcs())?"0":user.getCwcs());
		//上次登录时间
		String dlsj = user.getDlsj();
		//当前时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
        String dqsj = sdf.format(new Date());
		//最大限制数
        XtszDto xtszDto = xtszDao.getDtoById("login.maxErrorLimit");
        int maxLimit = 5;
        if(xtszDto != null && StringUtil.isNotBlank(xtszDto.getSzz())){
        	maxLimit = Integer.valueOf(xtszDto.getSzz());
        }
		
		try{
			//增加一次,判断当前时间与上次登录时间是否一致
			if(dqsj.equals(dlsj)){//若为第一次登录，则错误次数为1
				cwcs++;
				//判断是否大于等于最大限制数
				if(cwcs>=maxLimit){
					xtyh.setSfsd("1");
					xtyh.setCwcs("0");
				}else{
					xtyh.setCwcs(String.valueOf(cwcs));
				}
				
				//在当日判断错误次数（若错误次数数大于3，则报提示）
				if (maxLimit > 3 && cwcs >= 3) {
					if (cwcs < maxLimit) {
						String num = String.valueOf(maxLimit - cwcs);
						throw new BadCredentialsException("请注意，您还有"+ num + "次机会尝试，之后账户将被锁定。");
					}else {
						throw new BadCredentialsException("您已经超过最高限制！");
					}
				}
			}else{
				//日期不同则为错误第一次
				xtyh.setCwcs("1");
			}
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
		/*XtyhDto y_XtyhDto = dao.getDto(xtyhDto);
		if(!y_XtyhDto.getYhm().equals(xtyhDto.getYhm())){
			xtyhDto.setYyhm(xtyhDto.getYhm());
			dao.updateOauthClinetId(xtyhDto);
		}*/

		//判断用户是否锁定,如果没有锁定，更新错误次数为0
		if("0".equals(xtyhDto.getSfsd())) {
			xtyhDto.setCwcs("0");
		}
		//更新用户信息
		int result = dao.update(xtyhDto);
		//删除已有用户角色信息
		yhjsDao.deleteById(xtyhDto.getYhid());
		
		if(xtyhDto.getJsids()!=null && xtyhDto.getJsids().size() > 0){
			for(int i=0;i<xtyhDto.getJsids().size();i++){
				YhjsModel yhjsModel = new YhjsModel();
				yhjsModel.setYhid(xtyhDto.getYhid());
				yhjsModel.setJsid(xtyhDto.getJsids().get(i));
				yhjsDao.insert(yhjsModel);
			}
		}
		return result > 0 ?true:false;
	}
	
	/**
	 * 修改密码
	 * @return
	 * @throws BusinessException 
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean updatePass(XtyhDto xtyhDto) throws BusinessException{
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		
		XtyhDto tXtyhDto = dao.getDtoById(xtyhDto.getYhid());
		
		if(!bpe.matches(xtyhDto.getYmm(), tXtyhDto.getMm()))
			throw new BusinessException("ICOM99045");
		
		xtyhDto.setMm(bpe.encode(xtyhDto.getMm()));
		//更新用户信息
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
		return result > 0 ?true:false;
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
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(c_authorities);
		IgamsGrantedAuthority authority = (IgamsGrantedAuthority)authorities.get(0);
		
		com.matridx.igams.common.dao.entities.User user = authority.getYhxx();
		
		//设置当前角色信息
		user.setDqjs(t_xtyhDto.getDqjs());
		user.setDqjsdm(t_xtyhDto.getDqjsdm());
		user.setDqjsmc(t_xtyhDto.getDqjsmc());

		//查询当前角色的资源操作表
		QxModel t_qxDto = new QxModel();
		t_qxDto.setJsid(t_xtyhDto.getDqjs());
		List<QxModel> qxModels = jszyczbDao.getQxModelList(t_qxDto);
		//设置当前角色的资源操作表
		user.setQxModels(qxModels);
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
		if(xtzyDtos == null || xtzyDtos.size() == 0 )
			return null;
		
		List<XtzyDto> firstMenu = new ArrayList<XtzyDto>();
		List<XtzyDto> subMenu = null;
		List<XtzyDto> mobileMenu = new ArrayList<XtzyDto>();
		List<XtzyDto> vueMenu = null;//vue框架返回菜单

		for(int i=0;i<xtzyDtos.size();i++){
			XtzyDto dto = xtzyDtos.get(i);
			if(StringUtil.isBlank(dto.getFjd())){
				firstMenu.add(dto);
			}
			else if(firstMenu.size() ==1){
				subMenu = addMenuList(subMenu,dto);
			}
			if("00".equals(dto.getXskzbj())){
                mobileMenu = addMenuList(mobileMenu,(XtzyDto)dto.clone());
			}
			vueMenu = addMenuList(vueMenu, (XtzyDto) dto.clone());

		}
		Map<String, List<XtzyDto>> res = new HashMap<String, List<XtzyDto>>();
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
	private com.matridx.igams.common.dao.entities.User getAuthorByUser(com.matridx.igams.common.dao.entities.User user){
		//根据用户信息查找权限信息
		YhjsDto searchDto = new YhjsDto();
		searchDto.setYhid(user.getYhid());
		List<YhjsDto> yhqxDtos = yhjsDao.getDtoList(searchDto);
		
		if(yhqxDtos==null || yhqxDtos.size() ==0){
			throw new BadCredentialsException("用户："+ user.getYhm() + "没有权限！");
		}
		
		YhjsDto dq_yhjs = null;
		List<String> t_jsids = new ArrayList<String>();
		List<String> t_jsmcs = new ArrayList<String>();
		for(int i=0;i<yhqxDtos.size();i++){
			if(yhqxDtos.get(i).getJsid().equals(user.getDqjs()))
				dq_yhjs = yhqxDtos.get(i);
			t_jsids.add(yhqxDtos.get(i).getJsid());
			t_jsmcs.add(yhqxDtos.get(i).getJsmc());
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
		
		//查询当前角色的资源操作表
		QxModel t_qxDto = new QxModel();
		t_qxDto.setJsid(dq_yhjs.getJsid());
		List<QxModel> qxModels = jszyczbDao.getQxModelList(t_qxDto);
		//设置当前角色的资源操作表
		user.setQxModels(qxModels);
		
		return user;
	}

	@Override
	public boolean initPass(XtyhDto xtyhDto) {
		//获取初始化密码
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		XtszDto xtszDto = xtszDao.getDtoById("user.initpw");
		xtyhDto.setMm(bpe.encode(xtszDto.getSzz()));
		int result = 0;
		if(xtyhDto.getIds()!=null && xtyhDto.getIds().size() > 0){
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
		return result > 0 ? true : false;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public boolean initDingDing(XtyhDto xtyhDto) {
		RedisUtil redisUtil = new RedisUtil();
		List<Object> robotCodes = redisUtil.lGet("WbcxInfo");
		for (Object robotCode : robotCodes) {
			if (robotCode != null) {
				JSONObject parse = JSONObject.parseObject(JSON.toJSONString(robotCode));
				String appid = (String)parse.get("appid");
				String secret = (String)parse.get("secret");
				String lx = (String) parse.get("lx");
				String sftb = (String) parse.get("sftb");
				if (CharacterEnum.DINGDING.getCode().equals(lx)&&"1".equals(sftb)) {
					String token = talkUtil.getToken(appid, secret);
					List<DeptBaseResponse> list = talkUtil.getAllDepartmentsV2(token);

					List<XtyhDto> XtyhDto_list = dao.getUserList();
					List<JgxxDto> JgxxDto_list = jgxxService.getJgxxList();
					List<YhjgqxDto> Yhjgqx_List = yhjgqxService.getYhjgqxList();
					List<YhssjgDto> Yhssjg_List = yhssjgService.getYhssjgList();
					boolean isFind;
					//获取部门
					for (DeptBaseResponse department : list) {
						List<ListUserResponse> userlist = talkUtil.getAllUsersV2(token,department.getDeptId());
						//添加机构信息表数据,判断是否存在
						isFind = true;
						for (int i = JgxxDto_list.size()-1; i >= 0; i--) {
							if(JgxxDto_list.get(i).getJgid().equals(String.valueOf(department.getDeptId()))){
								isFind = false;
								JgxxDto_list.remove(JgxxDto_list.get(i));
							}
						}
						if(isFind){
							JgxxDto jgxxDto = new JgxxDto();
							jgxxDto.setJgid(String.valueOf(department.getDeptId()));
							jgxxDto.setJgmc(department.getName());
							jgxxDto.setLrry(xtyhDto.getXgry());
							boolean success = jgxxService.insert(jgxxDto);
							if(!success){
								return false;
							}
							JgxxDto_list.add(jgxxDto);
						}
						//获取成员
						for (ListUserResponse user : userlist) {

							//与数据库数据对比更新
							String preYhid = " ";
							for (int i = XtyhDto_list.size()-1; i >= 0; i--) {
								if(XtyhDto_list.get(i).getZsxm().equals(user.getName())){

									//添加用户机构权限表数据
									isFind = true;
									for (int j = 0; j < Yhjgqx_List.size(); j++) {
										if(Yhjgqx_List.get(j).getYhid().equals(XtyhDto_list.get(i).getYhid()) && Yhjgqx_List.get(j).getJsid().equals(XtyhDto_list.get(i).getJsid())){
											isFind = false;
										}
									}
									if(isFind){
										YhjgqxDto yhjgqxDto = new YhjgqxDto();
										yhjgqxDto.setYhid(XtyhDto_list.get(i).getYhid());
										yhjgqxDto.setJgid(String.valueOf(department.getDeptId()));
										yhjgqxDto.setJsid(XtyhDto_list.get(i).getJsid());
										yhjgqxDto.setXh("1");
										boolean success = yhjgqxService.insert(yhjgqxDto);
										if(!success){
											return false;
										}
										Yhjgqx_List.add(yhjgqxDto);
									}

									//添加用户所属机构表数据
									isFind = true;
									for (int j = 0; j < Yhssjg_List.size(); j++) {
										if(Yhssjg_List.get(j).getYhid().equals(XtyhDto_list.get(i).getYhid())){
											isFind = false;
											if(preYhid.equals(Yhssjg_List.get(j).getYhid())){
												continue;
											}
										}
									}
									if(isFind){
										YhssjgDto yhssjgDto = new YhssjgDto();
										yhssjgDto.setYhid(XtyhDto_list.get(i).getYhid());
										yhssjgDto.setJgid(String.valueOf(department.getDeptId()));
										yhssjgDto.setXh("1");
										boolean success = yhssjgService.insert(yhssjgDto);
										if(!success){
											return false;
										}
										Yhssjg_List.add(yhssjgDto);
									}
									preYhid = XtyhDto_list.get(i).getYhid();

									//更新用户表ddid和gwmc
									if(StringUtil.isBlank(XtyhDto_list.get(i).getDdid()) || StringUtil.isBlank(XtyhDto_list.get(i).getGwmc())){
										XtyhDto_list.get(i).setDdid(user.getUserid());
										XtyhDto_list.get(i).setGwmc(user.getTitle());
										dao.updateDingDing(XtyhDto_list.get(i));
									}
								}
							}
						}
					}
				}
			}
		}


		return true;
	}

	/**
	 * 个人信息包含部门信息
	 * @param xtyhDto
	 */
	@Override
	public XtyhDto getPersonalData(XtyhDto xtyhDto) {
		XtyhDto xtyhDto1 = dao.getPersonalData(xtyhDto);
		return xtyhDto1;
	}

	/**
	 * 机构名称
	 * @return
	 */
	public List<XtyhDto> getJgmc(){
		List<XtyhDto> jgmc1 = dao.getJgmc();
		return jgmc1;
	}

	/**
	 * 更新用户机构信息
	 * @param xtyhDto
	 */
	public boolean updateJg(XtyhDto xtyhDto){
		boolean iSsuccess = dao.updateJg(xtyhDto);
		return iSsuccess;
	}

	/**
	 * 更新用户错误次数
	 * @param xtyhDto
	 * @return
	 */
	public boolean updateLoginInfo(XtyhDto xtyhDto) {
		int result = dao.updateLoginInfo(xtyhDto);
		return result > 0 ? true : false;
	}
	
	/**
     * 查询所有用户信息（yhm,sfsd,sdtime,jstime）
     * @return
     */
	public List<com.matridx.igams.common.dao.entities.User> getUsersList(){
		return dao.getUsersList();
	}
	
	/**
     * 查询外部安全的用户信息，用于验证外部接口用户
     * @return
     */
	public List<com.matridx.igams.common.dao.entities.User> getWbaqUsersList(){
		return dao.getWbaqUsersList();
	}
}
