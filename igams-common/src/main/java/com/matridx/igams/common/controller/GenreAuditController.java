package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.ShlbDto;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IShlbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/systemmain")
public class GenreAuditController extends BaseController{

	@Autowired
	IShlbService shlbService;
	@Autowired
	IXxglService xxglService;
	@Autowired(required=false)
    private AmqpTemplate amqpTempl;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	@Autowired
	RedisUtil redisUtil;
	
	/**
	 * 跳转审核类别列表页面
	 */
	@RequestMapping(value = "/audit/pageListAuditGenre")
	public ModelAndView getAuditGenrePage() {
		ModelAndView mav=new ModelAndView("systemmain/audit/genre_list");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 审核类别列表
	 */
	@RequestMapping(value = "/audit/pageGetListGenreAuditList")
	@ResponseBody
	public Map<String,Object> getPagedAuditGenreList(ShlbDto shlbDto){
		List<ShlbDto> t_List = shlbService.getPagedDtoList(shlbDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", shlbDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 跳转新增界面
	 */
	@RequestMapping(value="/audit/addAuditGenre")
	public ModelAndView addAuditGenrePage(ShlbDto shlbDto) {
		ModelAndView mav=new ModelAndView("systemmain/audit/genre_edit");
		//数据库已有审核类别
		List<ShlbDto> yyshlblist = shlbService.getDtoList(null);
		//获取审核类别枚举类
		List<Map<String, String>> listType=new ArrayList<>();
		for(AuditTypeEnum shlblist:AuditTypeEnum.values()) {
			Map<String, String> map=new HashMap<>();
			int sfcz=0;//判断枚举类中的审核类别在数据库中是否存在  大于0说明已存在
			for (ShlbDto dto : yyshlblist) {
				if (shlblist.getCode().equals(dto.getShlb())) {
					sfcz = sfcz + 1;
				}
			}
		    if(sfcz<=0) {
		    	map.put("code",shlblist.getCode());
				listType.add(map);
		    }
		}
		shlbDto.setFormAction("add");
		mav.addObject("ShlbDto", shlbDto);
		mav.addObject("shlbList", listType);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 新增提交保存
	 */
	@RequestMapping(value="/audit/addSaveAuditGenre")
	@ResponseBody
	public Map<String,Object> addSaveAuditGenrePage(ShlbDto shlbDto){
		if(shlbDto.getQxxk()==null) {
			shlbDto.setQxxk("0");
		}
		boolean isSuccess = shlbService.insert(shlbDto);
		shlbDto.setPrefix(prefixFlg);
		if("1".equals(shlbDto.getSfgb()) && isSuccess) {
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.shlb.insert",JSONObject.toJSONString(shlbDto));
		}
		redisUtil.hdel("Grsz:MR",shlbDto.getShlb());
		redisUtil.hset("Grsz:MR",shlbDto.getShlb(),"1",-1);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 跳转修改界面
	 */
	@RequestMapping(value="/audit/modAuditGenre")
	public ModelAndView modAuditGenrePage(ShlbDto shlbDto) {
		ModelAndView mav=new ModelAndView("systemmain/audit/genre_edit");
		//数据库已有审核类别
		List<ShlbDto> yyshlblist = shlbService.getDtoList(null);
		//获取审核类别枚举类
		List<Map<String, String>> listType=new ArrayList<>();
		for(AuditTypeEnum shlblist:AuditTypeEnum.values()) {
			Map<String, String> map=new HashMap<>();
			int sfcz=0;//判断枚举类中的审核类别在数据库中是否存在  大于0说明已存在
			for (ShlbDto dto : yyshlblist) {
				if (shlblist.getCode().equals(dto.getShlb())) {
					sfcz = sfcz + 1;
				}
			}
		    if(sfcz<=0) {
		    	map.put("code",shlblist.getCode());
				listType.add(map);
		    }
		}
		ShlbDto shlcxx=shlbService.getShlbxxByShlb(shlbDto);
		shlcxx.setFormAction("mod");
		//加入当前审核类别
		Map<String, String> map=new HashMap<>();
		map.put("code",shlcxx.getShlb());
		listType.add(map);
		shlbDto.setFormAction("add");
		mav.addObject("ShlbDto", shlcxx);
		mav.addObject("shlbList", listType);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 修改提交保存
	 */
	@RequestMapping(value="/audit/modSaveAuditGenre")
	@ResponseBody
	public Map<String,Object> modSaveAuditGenrePage(ShlbDto shlbDto){
		if(shlbDto.getQxxk()==null) {
			shlbDto.setQxxk("0");
		}
		shlbDto.setPrefix(prefixFlg);
		boolean isSuccess = shlbService.update(shlbDto);
		if("1".equals(shlbDto.getSfgb()) && isSuccess) {
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.shlb.update",JSONObject.toJSONString(shlbDto));
		}
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 查看审核类别信息
	 */
	@RequestMapping(value="/audit/viewAuditGenre")
	public ModelAndView viewAuditGenrePage(ShlbDto shlbDto) {
		ModelAndView mav=new ModelAndView("systemmain/audit/genre_view");
		ShlbDto shlcxx=shlbService.getShlbxxByShlb(shlbDto);
		mav.addObject("ShlbDto", shlcxx);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 删除审核类别
	 */
	@RequestMapping(value="/audit/delAuditGenre")
	@ResponseBody
	public Map<String,Object> deleteShlb(ShlbDto shlbDto){
		List<ShlbDto> shlbDtos=shlbService.getShlbxxByIds(shlbDto);
		boolean isSuccess = shlbService.delete(shlbDto);
		List<String> ids=new ArrayList<>();
		//是否广播为1的才同步数据
		if(!shlbDtos.isEmpty()) {
			for (ShlbDto dto : shlbDtos) {
				if ("1".equals(dto.getSfgb())) {
					ids.add(dto.getShlb());
				}
			}
		}
		ShlbDto shlb=new ShlbDto();
		shlb.setIds(ids);
		shlb.setPrefix(prefixFlg);
		if(!ids.isEmpty()) {
			if(isSuccess) {
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.shlb.del",JSONObject.toJSONString(shlb));
			}
		}
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00003").getXxnr()):xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
}
