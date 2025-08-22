package com.matridx.igams.wechat.control;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.wechat.service.svcinterface.IToolUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/ws")
public class ToolUtilControl extends BaseController{
	
	@Autowired
	private IToolUtilService toolUtil;
	
	@RequestMapping("/toolutil/getReport")
	@ResponseBody
	public Map<String, Object> getReport(HttpServletRequest request){
		toolUtil.getPdfReport(request.getParameter("filepath"));
		Map<String, Object> map = new HashMap<>();
		map.put("result", "OK");
		return map;
	}
}
