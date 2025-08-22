package com.matridx.igams.common.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OtherController extends BaseController{
	
	@Value("${spring.application.name:}")
    private String systemName;
	
	@Value("${matridx.prefix.igamsweb:}")
    private String webName;
	
	@Value("${matridx.prefix.urlprefix:}")
    private String urlprefix;
	
	@RequestMapping(value="/actuator/info")
	@ResponseBody
	public String systemInfo() {
		return "This System Name is " + systemName + "<br/> Connect Web is " + webName + "<br/> Url prefix is " + urlprefix;
	}
}
