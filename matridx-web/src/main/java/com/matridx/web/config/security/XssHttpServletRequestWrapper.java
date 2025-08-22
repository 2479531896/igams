package com.matridx.web.config.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 防止XSS攻击
 * @author linwu
 *
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper{

	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getParameter(String name){
		String value = super.getParameter(name);
		if(null != value){
			return cleanXSS(value);
		}
		return value;
	}
	
	@Override
	public String[] getParameterValues(String name){
		String[] values = super.getParameterValues(name);
		if (values == null)  {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
	}
 
    /**
     * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
     * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
     * getHeaderNames 也可能需要覆盖
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value != null) {
            value = cleanXSS(value);
        }
        return value;
    }
	
	private String cleanAllXSS(String value){
		value = cleanXSS(value);
		value = value.replaceAll("&", "＆");
		return value;
	}
	
	private String cleanXSS(String value){
		value = value.replaceAll("<", "＜").replaceAll(">", "＞");
		value = value.replaceAll("#", "＃");
		return value;
	}
}
