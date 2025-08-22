package com.matridx.igams.common.controller;

import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.RyczxgDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.HabitsTypeEnum;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IRyczxgService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/common")
public class HabitController extends BaseController {

	@Autowired
	IRyczxgService ryczxgService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IGrszService grszService;
	
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	
	/**
	 * 更新人员操作习惯
	 */
	@RequestMapping("/habit/commonModUserHabit")
	@ResponseBody
	public Map<String,Object> commonModUserHabit(RyczxgDto ryczxgDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		//获取用户信息
		User user = getLoginInfo(request);
		ryczxgDto.setYhid(user.getYhid());
		ryczxgDto.setQf(HabitsTypeEnum.USER_HABITS.getCode());
		boolean isSuccess = ryczxgService.insertOrUpdate(ryczxgDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	@RequestMapping("/habit/pagedataModGrsz")
	@ResponseBody
	public Map<String,Object> pagedataModGrsz(GrszDto grszDto, HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		grszDto.setYhid(user.getYhid());
		boolean isSuccess = grszService.insertOrUpdate(grszDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
