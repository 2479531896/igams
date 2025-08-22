package com.matridx.las.home.controller;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.las.home.dao.entities.SysszDto;
import com.matridx.las.home.dao.entities.YqxxDto;
import com.matridx.las.home.service.svcinterface.ICommonService;
import com.matridx.las.home.service.svcinterface.ISysszService;
import com.matridx.las.home.service.svcinterface.IYqxxInfoService;
import com.matridx.las.home.service.svcinterface.IYqxxService;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.SendBaseCommand;
import com.matridx.las.netty.channel.domain.Command;
import com.matridx.las.netty.dao.entities.FrontMaterialModel;
import com.matridx.las.netty.dao.entities.JkywzszDto;
import com.matridx.las.netty.dao.entities.YblcsjDto;
import com.matridx.las.netty.enums.InstrumentStatusEnum;
import com.matridx.las.netty.enums.MedicalRobotProcessEnum;
import com.matridx.las.netty.enums.RobotStatesEnum;
import com.matridx.las.netty.global.InstrumentStateGlobal;
import com.matridx.las.netty.service.svcinterface.*;
import com.matridx.las.netty.util.SendMessgeToHtml;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/yblcsj")
public class YblcsjController extends BaseController{
@Autowired
private IYblcsjService yblcsjService;
	private static final Logger logger = LoggerFactory.getLogger(SendMessgeToHtml.class);

	/**
	 * 时间流程查询列表
	 */
	@RequestMapping("/yblcsj/queryInfoList")
	@ResponseBody
	public Map<String,Object> queryInfoList(YblcsjDto yblcsjDto, HttpServletRequest request){
		Map<String, Object> map=new HashMap<String, Object>();
		List<YblcsjDto> yblcsjDtos = yblcsjService.getPagedDtoList(yblcsjDto);
		map.put("rows", yblcsjDtos);
		map.put("total", yblcsjDto.getTotalNumber());
		logger.info(JSONObject.toJSONString(map));
		return map;
	}
}
