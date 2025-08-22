package com.matridx.igams.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.matridx.igams.common.dao.entities.TyszmxDto;
import com.matridx.igams.common.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.QxModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.security.IgamsGrantedAuthority;
import com.matridx.springboot.util.base.JsonUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.base.UrlUtil;

public class BaseController {
	
	@Autowired
	protected RestTemplate restTemplate;
	//访问主程序端口
	@Value("${matridx.prefix.igamsweb:}")
	private String webPrefix;
	@Autowired
	private RedisUtil redisUtil;
	
	public String getPrefix(){
		return null;
	}
	
	/**
	 * 获取用户登录信息
	 */
	protected UserDetails getUserDetails(){
		
		//String name = request.getRemoteUser();
		// 获取安全上下文对象，就是那个保存在 ThreadLocal 里面的安全上下文对象
		// 总是不为null(如果不存在，则创建一个authentication属性为null的empty安全上下文对象)
		SecurityContext securityContext = SecurityContextHolder.getContext();
		
		// 获取当前认证了的 principal(当事人),或者 request token (令牌)
		// 如果没有认证，会是 null
		Authentication authentication = securityContext.getAuthentication();

		// 获取当事人信息对象，返回结果是 Object 类型，但实际上可以是应用程序自定义的带有更多应用相关信息的某个类型。
		// 很多情况下，该对象是 Spring Security 核心接口 UserDetails 的一个实现类，你可以把 UserDetails 想像
		// 成我们数据库中保存的一个用户信息到 SecurityContextHolder 中 Spring Security 需要的用户信息格式的
		// 一个适配器。
		Object principal = authentication.getAuthorities();
		
		return (UserDetails)principal;
		/*if (principal instanceof UserDetails) {
			String username = ((UserDetails)principal).getUsername();
		} else {
			String username = principal.toString();
		}*/
	}
	
	/**
	 * 获取用户登录信息
	 */
	protected User getLoginInfo(){
		//String name = request.getRemoteUser();
		// 获取安全上下文对象，就是那个保存在 ThreadLocal 里面的安全上下文对象
		// 总是不为null(如果不存在，则创建一个authentication属性为null的empty安全上下文对象)
		SecurityContext securityContext = SecurityContextHolder.getContext();
		
		// 获取当前认证了的 principal(当事人),或者 request token (令牌)
		// 如果没有认证，会是 null
		Authentication authentication = securityContext.getAuthentication();

		// 获取当事人信息对象，返回结果是 Object 类型，但实际上可以是应用程序自定义的带有更多应用相关信息的某个类型。
		// 很多情况下，该对象是 Spring Security 核心接口 UserDetails 的一个实现类，你可以把 UserDetails 想像
		// 成我们数据库中保存的一个用户信息到 SecurityContextHolder 中 Spring Security 需要的用户信息格式的
		// 一个适配器。
		@SuppressWarnings("unchecked")
		List<IgamsGrantedAuthority> authorities = (List<IgamsGrantedAuthority>)authentication.getAuthorities();
		
		IgamsGrantedAuthority authority = authorities.get(0);
		
		return authority.getYhxx();
	}
	
	/**
	 * 获取用户登录信息
	 */
	protected User getLoginInfo(HttpServletRequest request){

        String preFix = getPrefix();
        User user;
        if(StringUtil.isNotBlank(preFix)){
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add("access_token", request.getParameter("access_token"));
            String userString = restTemplate.postForObject(UrlUtil.getWebPrefix(webPrefix)+"/common/common/getLoginUserInfo", paramMap, String.class);
            user = JSON.parseObject(userString, User.class);
        }else{
			//String name = request.getRemoteUser();
			// 获取安全上下文对象，就是那个保存在 ThreadLocal 里面的安全上下文对象
			// 总是不为null(如果不存在，则创建一个authentication属性为null的empty安全上下文对象)
			SecurityContext securityContext = SecurityContextHolder.getContext();
			
			// 获取当前认证了的 principal(当事人),或者 request token (令牌)
			// 如果没有认证，会是 null
			Authentication authentication = securityContext.getAuthentication();

			// 获取当事人信息对象，返回结果是 Object 类型，但实际上可以是应用程序自定义的带有更多应用相关信息的某个类型。
			// 很多情况下，该对象是 Spring Security 核心接口 UserDetails 的一个实现类，你可以把 UserDetails 想像
			// 成我们数据库中保存的一个用户信息到 SecurityContextHolder 中 Spring Security 需要的用户信息格式的
			// 一个适配器。
			@SuppressWarnings("unchecked")
			List<IgamsGrantedAuthority> authorities = (List<IgamsGrantedAuthority>)authentication.getAuthorities();
			
			IgamsGrantedAuthority authority = authorities.get(0);
			user = authority.getYhxx();
		}
		return user;
	}
	
	/**
	 * 返回用户按钮权限
	 */
	protected List<QxModel> setCzdmList(HttpServletRequest request,Map<String,Object> resultMap){
		User user = getLoginInfo(request);
		
		List<QxModel> qxDtos = user.getQxModels();
		
		List<QxModel> now_jsczDtos = new ArrayList<>();
		for (QxModel qxModel : qxDtos) {
			if (qxModel.getZyid().equals(request.getParameter("zyid"))) {
				now_jsczDtos.add(qxModel);
			}
		}
		resultMap.put("czdmlist", now_jsczDtos);
		return now_jsczDtos;
	}

	/**
	 * 返回通用设置权限
	 */
	protected void setTyszList(HttpServletRequest request,Map<String,Object> resultMap){
		try{
			if(StringUtil.isNotBlank(request.getParameter("zyid"))){
				Object hget = redisUtil.hget("GeneralSetting", request.getParameter("zyid"));
				List<TyszmxDto> tyszmxDtoList = JSON.parseArray(hget.toString(), TyszmxDto.class);
				List<TyszmxDto> dtoList=new ArrayList<>();
				//排除子级的通用设置，只传递父级或者无子级的通用设置
				if(!tyszmxDtoList.isEmpty()){
					for(TyszmxDto dto:tyszmxDtoList){
						if(StringUtil.isBlank(dto.getFnrid())){
							dtoList.add(dto);
						}
					}
				}
				List<TyszmxDto> menuList=new ArrayList<>();//高级筛选
				List<TyszmxDto> searchList=new ArrayList<>();//模糊查找
				List<TyszmxDto> fieldList=new ArrayList<>();//列表字段
				List<TyszmxDto> buttonList=new ArrayList<>();//右键菜单
				if(!dtoList.isEmpty()){
					TyszmxDto tyszmxDto=dtoList.get(0);
					List<TyszmxDto> tyszmxDtos=new ArrayList<>();
					for(TyszmxDto dto:dtoList){
						//lx=1为右键菜单，=2为列表字段，=3为模糊查找
						if("1".equals(dto.getLx())){
							buttonList.add(dto);
						}else if("2".equals(dto.getLx())){
							fieldList.add(dto);
						}else if("3".equals(dto.getLx())){
							searchList.add(dto);
						}else{
							//遍历查找，将相同标题的内容合成一个List
							if(dto.getBtid().equals(tyszmxDto.getBtid())){
								//筛选类别=1代表是基础数据，=0代表是自定义
								if("1".equals(dto.getSxlb())){
									dto.setTyszmxDtos(redisUtil.hmgetDto("matridx_jcsj:" + dto.getNr()));
									menuList.add(dto);
								}else{
									//字段类型=2代表是时间类型
									if("2".equals(dto.getZdlx())){
										menuList.add(dto);
									}else{
										tyszmxDtos.add(dto);
									}
								}
							}else{
								if(!tyszmxDtos.isEmpty()){
									TyszmxDto tyszmxDto_t= tyszmxDto.clone();
									tyszmxDto_t.setTyszmxDtos(tyszmxDtos);
									menuList.add(tyszmxDto_t);
								}
								tyszmxDto=dto;
								tyszmxDtos=new ArrayList<>();
								if("1".equals(dto.getSxlb())){
									dto.setTyszmxDtos(redisUtil.hmgetDto("matridx_jcsj:" + dto.getNr()));
									menuList.add(dto);
								}else{
									if("2".equals(dto.getZdlx())){
										menuList.add(dto);
									}else{
										tyszmxDtos.add(dto);
									}
								}
							}
						}
					}
					if(!tyszmxDtos.isEmpty()){
						TyszmxDto tyszmxDto_t= tyszmxDto.clone();
						tyszmxDto_t.setTyszmxDtos(tyszmxDtos);
						menuList.add(tyszmxDto_t);
					}
				}
				resultMap.put("filterList",menuList);
				resultMap.put("buttonList",buttonList);
				resultMap.put("searchList",searchList);
				resultMap.put("fieldList",fieldList);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	
	/**
	 * 根据设定条件筛选字段，用于处理钉钉或者微信端的处理，防止返回信息过多
	 */
	protected Object screenClassColumns(HttpServletRequest request,Object source) {
		return screenClassColumns(request,source,"limitColumns");
	}
	
	/**
	 * 根据设定条件筛选字段，用于处理钉钉或者微信端的处理，防止返回信息过多
	 */
	@SuppressWarnings("unchecked")
	protected Object screenClassColumns(HttpServletRequest request,Object source,String paramName) {
		String limitColumns = request.getParameter(paramName);
		if(source == null || StringUtil.isBlank(limitColumns))
			return source;
		if(source.getClass().getSimpleName().equals("ArrayList")) {
			return JsonUtil.cleanAttribute(source,limitColumns);
		}else if(source.getClass().getSimpleName().equals("HashMap")) {
			HashMap<String,Object> o_map = (HashMap<String,Object>)source;
			if(o_map.isEmpty())
				return source;

			o_map.replaceAll((k, v) -> JsonUtil.cleanAttribute(o_map.get(k), limitColumns));
			return o_map;
		}else {
			return source;
		}
	}
}
