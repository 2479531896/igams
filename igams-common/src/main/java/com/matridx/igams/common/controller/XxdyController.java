package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/xxdy")
public class XxdyController extends BaseController{
	@Autowired
	private IXxdyService xxdyService;
	@Autowired
	private IXxglService xxglService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;

	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;

	@Autowired
	private IJgxxService jgxxService;

	@Override
	public String getPrefix(){
		return urlPrefix;
	}

	/**
	 * 增加一个信息对应的页面模块
	 */
	@RequestMapping("/xxdy/pageListXxdy")
	public ModelAndView pageXxdy(HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("common/xxdy/xxdy_list");
		mav.addObject("urlPrefix",urlPrefix);
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.XXDY_TYPE});
		mav.addObject("xxdyTypelist",jclist.get(BasicDataTypeEnum.XXDY_TYPE.getCode()));
		return mav;
	}

	/**
	 * 显示信息对应列表
	 */
	@RequestMapping("/xxdy/pageGetListXxdy")
	@ResponseBody
	public Map<String, Object> getXxdyList(XxdyDto xxdyDto){
		Map<String,Object> map=new HashMap<>();
		List<XxdyDto> xxdylist = xxdyService.getPagedDtoList(xxdyDto);
		map.put("total",xxdyDto.getTotalNumber());
		map.put("rows",xxdylist);
		return map;
	}


	/**
	 * 获取对应列表
	 */
	@RequestMapping("/xxdy/pagedataXxdyList")
	@ResponseBody
	public Map<String, Object> pagedataXxdyList(XxdyDto xxdyDto){
		return getXxdyList(xxdyDto);
	}

	/**
	 * 查看信息对应详情
	 */
	@RequestMapping("/xxdy/viewXxdy")
	@ResponseBody
	public ModelAndView viewXxdyList(XxdyDto xxdyDto){
		ModelAndView mav=new ModelAndView("common/xxdy/xxdy_view");
		xxdyDto = xxdyService.getDto(xxdyDto);
		mav.addObject("xxdyDto", xxdyDto);
		return mav;
	}

	/**
	 * 点击新增信息对应的页面
	 */
	@RequestMapping("/xxdy/addXxdy")
	@ResponseBody
	public ModelAndView addXxdyList(XxdyDto xxdyDto){
		ModelAndView mav=new ModelAndView("common/xxdy/xxdy_add");
		xxdyDto.setFormAction("addSaveXxdy");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.XXDY_TYPE});
		mav.addObject("xxdyDto", xxdyDto);
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("xxdyTypelist",jclist.get(BasicDataTypeEnum.XXDY_TYPE.getCode()));
		return mav;
	}

	/**
	 * 增加信息到数据库
	 */
    @RequestMapping("/xxdy/addSaveXxdy")
	@ResponseBody
    public Map<String, Object> addSaveXxdy(XxdyDto xxdyDto,HttpServletRequest request){
    	Map<String, Object> map=new HashMap<>();
    	boolean isSuccess;
    	if ("XM".equals(xxdyDto.getCskz())||"XMCSS".equals(xxdyDto.getCskz())){
			XxdyDto dto = xxdyService.getInfo(xxdyDto);
			if (null != dto){
				map.put("status", "fail");
				map.put("message", "消息已经存在，请核实后添加！");
				return map;
			}
		}
    	int num = xxdyService.getCount(xxdyDto);
    	if(num>0) {
    		map.put("status", "fail");
    		map.put("message", "消息已经存在，请核实后添加！");
    	}else if(num==0) {
    		//设置dyid和录入人员
    		xxdyDto.setDyid(StringUtil.generateUUID());
    		User user = getLoginInfo(request);
    		xxdyDto.setLrry(user.getYhid());
			dealDto(xxdyDto);//补充dto的属性
			isSuccess=xxdyService.insertDto(xxdyDto);
			redisUtil.hset("XXDY:"+xxdyDto.getDylxcsdm(),xxdyDto.getYxx(), JSON.toJSONString(xxdyDto));
			amqpTempl.convertAndSend("wechat.exchange", "matridx.xxdy.operate", RabbitEnum.XXDY_ADD.getCode() + JSONObject.toJSONString(xxdyDto));
        	map.put("status",isSuccess?"success":"fail");
    		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
    	}
		return map;
    }

	/**
	 * 修改时新增一个页面
	 */
	@RequestMapping("/xxdy/modXxdy")
	@ResponseBody
	public ModelAndView modXxdyList(XxdyDto xxdyDto){
		ModelAndView mav=new ModelAndView("common/xxdy/xxdy_add");
		xxdyDto = xxdyService.getDto(xxdyDto);
		xxdyDto.setFormAction("modSaveXxdy");
//		根据dto去取相应的用来显示对应信息的list
		dealDto(xxdyDto);
		if("RY".equals(xxdyDto.getCskz1())) {
    		List<User> resultList = xxdyService.getUserList();
    		mav.addObject("resultList",resultList);
    	}else if("JS".equals(xxdyDto.getCskz1())) {
    		List<Role> resultList = xxdyService.getRoleList();
    		mav.addObject("resultList",resultList);
    	}else if("JCSJ".equals(xxdyDto.getCskz1()))  {
    		//其余根据基础数据类别去查找
    		if(StringUtil.isNotBlank(xxdyDto.getCskz2())) {
    			JcsjDto jcsjDto = new JcsjDto();
    			jcsjDto.setJclb(xxdyDto.getCskz2());
    			List<JcsjDto> resultList=jcsjService.getJcsjDtoList(jcsjDto);
    			mav.addObject("resultList",resultList);
    		}
    	}else if("XM".equals(xxdyDto.getCskz1()))  {
			List<JcsjDto> resultList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_PROJECT_TYPE.getCode());
			List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
			List<JcsjDto> dtoList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
			mav.addObject("resultList",resultList);
			mav.addObject("list",list);
			mav.addObject("dtoList", JSONObject.toJSONString(dtoList));
		}else if("LS".equals(xxdyDto.getCskz1()))  {
				List<JcsjDto> resultList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_PROJECT_TYPE.getCode());
				List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
				List<JcsjDto> dtoList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
			mav.addObject("resultList",resultList);
			mav.addObject("list",list);
			mav.addObject("dtoList", JSONObject.toJSONString(dtoList));
		}else if("XMCSS".equals(xxdyDto.getCskz1()))  {
			List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
			List<JcsjDto> dtoList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
			mav.addObject("list",list);
			mav.addObject("dtoList",JSONObject.toJSONString(dtoList));
		}else if("BM".equals(xxdyDto.getCskz1()))  {
			List<JgxxDto>jgxxList=jgxxService.getJgxxList();
			mav.addObject("resultList",jgxxList);
		}else if("YJ".equals(xxdyDto.getCskz1()))  {
			List<Map<String,Object>>jgxxList=xxdyService.getSjwzxxList();
			mav.addObject("resultList",jgxxList);
		}

		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.XXDY_TYPE});
		mav.addObject("xxdyDto", xxdyDto);
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("xxdyTypelist",jclist.get(BasicDataTypeEnum.XXDY_TYPE.getCode()));
		return mav;
	}

	@RequestMapping("/xxdy/pagedataDylxdeaList")
	@ResponseBody
	public Map<String,Object> pagedataDylxdeaList(@RequestBody List<XxdyDto> xxdyList){
		List<Map<String,Object>> arrlist=new ArrayList<>();
		Map<String,Object>returnMap=new HashMap<>();
		for( XxdyDto xxdyDto:xxdyList){
			Map<String,Object> map = new HashMap<>();
			if("RY".equals(xxdyDto.getCskz1())) {
				List<User> resultList = xxdyService.getUserList();
				map.put("resultList",resultList);
			}else if("JS".equals(xxdyDto.getCskz1())) {
				List<Role> resultList = xxdyService.getRoleList();
				map.put("resultList",resultList);
			}else if("JCSJ".equals(xxdyDto.getCskz1()))  {
				//其余根据基础数据类别去查找
				if(StringUtil.isNotBlank(xxdyDto.getCskz2())) {
					JcsjDto jcsjDto = new JcsjDto();
					jcsjDto.setJclb(xxdyDto.getCskz2());
					List<JcsjDto> resultList=redisUtil.lgetDto("List_matridx_jcsj:"+xxdyDto.getCskz2());
					map.put("resultList",resultList);
				}
			}else if("XM".equals(xxdyDto.getCskz1()))  {
				//其余根据基础数据类别去查找
				if(StringUtil.isNotBlank(xxdyDto.getCskz2())) {
					List<JcsjDto> resultList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_PROJECT_TYPE.getCode());
					List<JcsjDto> list = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
					List<JcsjDto> dtoList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
					map.put("resultList",resultList);
					map.put("list",list);
					map.put("dtoList",dtoList);
				}
			}else if("XMCSS".equals(xxdyDto.getCskz1()))  {
				List<JcsjDto> list = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
				List<JcsjDto> dtoList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
				map.put("list",list);
				map.put("dtoList",dtoList);
			}else if("LS".equals(xxdyDto.getCskz1()))  {
				List<JcsjDto> resultList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_PROJECT_TYPE.getCode());
				map.put("resultList",resultList);
			}else if("BM".equals(xxdyDto.getCskz1()))  {
				List<JgxxDto>jgxxList=jgxxService.getJgxxList();
				map.put("resultList",jgxxList);
			}else if("YJ".equals(xxdyDto.getCskz1()))  {
				List<Map<String,Object>>jgxxList=xxdyService.getSjwzxxList();
				map.put("resultList",jgxxList);
			}
			arrlist.add(map);
		}
		returnMap.put("list",arrlist);
		return returnMap;
	}
	/**
	 * 修改消息到数据库
	 */
	@RequestMapping("/xxdy/modSaveXxdy")
	@ResponseBody
	public Map<String,Object> modSaveXxdy(XxdyDto xxdyDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		boolean isSuccess;
		if ("XM".equals(xxdyDto.getCskz())||"XMCSS".equals(xxdyDto.getCskz())){
			XxdyDto dto = xxdyService.getInfo(xxdyDto);
			if (null != dto){
				map.put("status","fail");
				map.put("message","消息已经存在，请核实后添加！");
				return map;
			}
		}
		User user = getLoginInfo(request);
		xxdyDto.setXgry(user.getYhid());
		dealDto(xxdyDto);//补充dto的属性
		isSuccess=xxdyService.updateDto(xxdyDto);
		redisUtil.hset("XXDY:"+xxdyDto.getDylxcsdm(),xxdyDto.getYxx(), JSON.toJSONString(xxdyDto));
		amqpTempl.convertAndSend("wechat.exchange", "matridx.xxdy.operate", RabbitEnum.XXDY_MOD.getCode() + JSONObject.toJSONString(xxdyDto));
    	map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 删除消息
	 */
	@RequestMapping("/xxdy/delXxdy")
	@ResponseBody
	public Map<String,Object> deleteXxdyList(XxdyDto xxdyDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		xxdyDto.setScry(getLoginInfo(request).getYhid());
		boolean isSuccess = xxdyService.delete(xxdyDto);
		Map<Object, Object> hmget = redisUtil.hmget("XXDY:YBLX");
		for (String id : xxdyDto.getIds()) {
			for(Object obj : hmget.values()) {
				XxdyDto j = JSON.parseObject(obj.toString(),XxdyDto.class);
				if (id.equals(j.getDyid())){
					redisUtil.hdel("XXDY:YBLX",j.getYxx());
				}
			}
		}
		amqpTempl.convertAndSend("wechat.exchange", "matridx.xxdy.operate", RabbitEnum.XXDY_DEL.getCode() + JSONObject.toJSONString(xxdyDto));
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

    @RequestMapping("/xxdy/pagedataDylxdeal")
	@ResponseBody
    public Map<String,Object> pagedataDylxdeal(XxdyDto xxdyDto){
    	Map<String,Object> map = new HashMap<>();
    	if("RY".equals(xxdyDto.getCskz1())) {
    		List<User> resultList = xxdyService.getUserList();
    		map.put("resultList",resultList);
    	}else if("JS".equals(xxdyDto.getCskz1())) {
    		List<Role> resultList = xxdyService.getRoleList();
    		map.put("resultList",resultList);
    	}else if("JCSJ".equals(xxdyDto.getCskz1()))  {
    		//其余根据基础数据类别去查找
    		if(StringUtil.isNotBlank(xxdyDto.getCskz2())) {
    			JcsjDto jcsjDto = new JcsjDto();
    			jcsjDto.setJclb(xxdyDto.getCskz2());
				List<JcsjDto> resultList = redisUtil.lgetDto("List_matridx_jcsj:"+xxdyDto.getCskz2());
    			map.put("resultList",resultList);
    		}
    	}else if("XM".equals(xxdyDto.getCskz1()))  {
			//其余根据基础数据类别去查找
			if(StringUtil.isNotBlank(xxdyDto.getCskz2())) {
				List<JcsjDto> resultList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_PROJECT_TYPE.getCode());
				List<JcsjDto> list = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
				List<JcsjDto> dtoList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
				map.put("resultList",resultList);
				map.put("list",list);
				map.put("dtoList",dtoList);
			}
		}else if("XMCSS".equals(xxdyDto.getCskz1()))  {
			List<JcsjDto> list = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
			List<JcsjDto> dtoList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode());
			map.put("list",list);
			map.put("dtoList",dtoList);
		}else if("LS".equals(xxdyDto.getCskz1()))  {
			List<JcsjDto> resultList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_PROJECT_TYPE.getCode());
			map.put("resultList",resultList);
		}else if("BM".equals(xxdyDto.getCskz1()))  {
			List<JgxxDto>jgxxList=jgxxService.getJgxxList();
			map.put("resultList",jgxxList);
		}else if("YJ".equals(xxdyDto.getCskz1()))  {
			List<Map<String,Object>>jgxxList=xxdyService.getSjwzxxList();
			map.put("resultList",jgxxList);
		}
		return map;
    }

	/**
	 * 在原有 Dto上，该方法补充XxdyDto的cskz1，cskz2和dyxxmc信息
	 */
	private XxdyDto dealDto(XxdyDto xxdyDto) {
		XxdyDto xxdy_cskz;
		xxdy_cskz = xxdyService.getCskz(xxdyDto);
		xxdyDto.setDylxcsdm(xxdy_cskz.getDylxcsdm());
		xxdyDto.setCskz1(xxdy_cskz.getCskz1());
		xxdyDto.setCskz2(xxdy_cskz.getCskz2());
		if("RY".equals(xxdy_cskz.getCskz1()) ) {
			String rymc = xxdyService.getRyXxdymc(xxdyDto.getDyxx());
			xxdyDto.setDyxxmc(rymc);
		}else if("JS".equals(xxdy_cskz.getCskz1()) ) {
			String jsmc = xxdyService.getJsXxdymc(xxdyDto.getDyxx());
			xxdyDto.setDyxxmc(jsmc);
		}else if("JCSJ".equals(xxdy_cskz.getCskz1()) || "XM".equals(xxdy_cskz.getCskz1()) || "LS".equals(xxdy_cskz.getCskz1())) {
			String csmc = xxdyService.getJcsjXxdymc(xxdyDto.getDyxx());
			xxdyDto.setDyxxmc(csmc);
		}else if("XMCSS".equals(xxdy_cskz.getCskz1())){
			xxdyDto.setDyxx(xxdyDto.getDyxx_input());
			xxdyDto.setDyxxmc(xxdyDto.getDyxx_input());
		}else if(StringUtil.isBlank(xxdy_cskz.getCskz1())){
			xxdyDto.setDyxx(xxdyDto.getDyxx_input());
			xxdyDto.setDyxxmc(xxdyDto.getDyxx_input());
		}
		return xxdyDto;
	}

	/**
	 * 信息对应导入
	 */
	@RequestMapping(value ="/xxdy/pageImportXxdy")
	public ModelAndView pageImportXxdy(){
		ModelAndView mav = new ModelAndView("common/xxdy/xxdy_import");
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_CORRESPOND_INFORMATION.getCode());
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("fjcfbDto", fjcfbDto);
		return mav;
	}
}
