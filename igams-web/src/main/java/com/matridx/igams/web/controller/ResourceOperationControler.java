package com.matridx.igams.web.controller;

import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.web.dao.entities.WbzyczDto;
import com.matridx.igams.web.service.svcinterface.IWbzyczService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/operation")
public class ResourceOperationControler extends BaseController{
	@Autowired
	private IWbzyczService wbzyczService;
	
	/**
	 * 钉钉查询外部资源操作
	 * @param wbzyczDto
	 * @return
	 */
	@RequestMapping("/operation/minidataGetListForDingtalk")
	@ResponseBody
	public Map<String,Object> minidataGetListForDingtalk(WbzyczDto wbzyczDto,HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		List<WbzyczDto> wbzyczList=wbzyczService.getDtoList(wbzyczDto);
		if(CollectionUtils.isEmpty(wbzyczList)) {
			wbzyczList= new ArrayList<>();
		}
		map.put("wbzyczList", wbzyczList);
		screenClassColumns(request,map);
		return map;
	}
}
