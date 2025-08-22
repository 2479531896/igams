package com.matridx.igams.web.config.interceptor;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 本类用于拦截  @ResponseBody 方法，并往里面增加一些信息
 * @author linwu
 *
 */
@ControllerAdvice
public class MatridxResponseBodyAdvice implements ResponseBodyAdvice<Object>{

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		// TODO Auto-generated method stub
		
//		String path = request.getURI().getPath();
		
		//兼容原来的接口返回
//		if(body instanceof HashMap && path.indexOf("/getListInit")!=-1) {
			//因无法解决从request里获取 上传的参数信息，自动存放按钮信息的功能暂时废弃
			
			//String name = request.getRemoteUser();
    		// 获取安全上下文对象，就是那个保存在 ThreadLocal 里面的安全上下文对象
    		// 总是不为null(如果不存在，则创建一个authentication属性为null的empty安全上下文对象)
			/*
			 * SecurityContext securityContext = SecurityContextHolder.getContext();
			 * 
			 * // 获取当前认证了的 principal(当事人),或者 request token (令牌) // 如果没有认证，会是 null
			 * Authentication authentication = securityContext.getAuthentication();
			 * 
			 * List<IgamsGrantedAuthority> authorities =
			 * (List<IgamsGrantedAuthority>)authentication.getAuthorities();
			 * 
			 * IgamsGrantedAuthority authority = authorities.get(0);
			 * 
			 * List<QxModel> qxDtos = authority.getYhxx().getQxModels();
			 * 
			 * List<QxModel> now_jsczDtos = new ArrayList<QxModel>(); // for(int
			 * i=0;i<qxDtos.size();i++){ // QxModel qxModel = qxDtos.get(i); //
			 * if(qxModel.getZyid().equals(request..getBody().getParameter("zyid"))){ //
			 * now_jsczDtos.add(qxModel); // } // } Map<String, Object> resultMap =
			 * (Map<String, Object>)body; List<String> testList = new ArrayList<String>();
			 * testList.add("aaa"); resultMap.put("czdmlist", testList);
			 */
//		}
		return body;
	}

}
