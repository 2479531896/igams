package com.matridx.igams.common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/systemmain")
public class BasicDataController extends BaseController{
	
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	IJcsjService jcsjService;
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;
    @Value("${matridx.rabbit.flg:}")
    private String prefixFlg;
    @Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
    @Override
	public String getPrefix(){
		return urlPrefix;
	}
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	RedisUtil redisUtil;
	@Value("${matridx.rabbit.preflg:}")
	private String preRabbitFlg;
	/**
	 * 基础数据列表
	 */
	@RequestMapping(value ="/data/pageListBasicData")
	public ModelAndView pageListBasicData(){
		ModelAndView mav = new ModelAndView("systemmain/data/basicdata_list");
		List<Map<String, String>> jcsjModels = jcsjService.getJclbList();
		mav.addObject("jclbList", jcsjModels);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 基础数据列表_vue
	 */
	@RequestMapping(value ="/data/page_ListBasicData_vue")
	@ResponseBody
	public Map<String,Object> pageListBasicData_vue(){
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");

		try {
			List<Map<String, String>> jcsjModels = jcsjService.getJclbList();
			resMap.put("jclbList", jcsjModels);
			resMap.put("urlPrefix", urlPrefix);

		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}
		return resMap;
	}
	
	/**
	 * 基础数据列表明细
	 */
	@RequestMapping(value ="/data/pageGetListBasicData")
	@ResponseBody
	public Map<String,Object> listBasicData(JcsjDto jcsjDto){
		List<JcsjDto> t_List = jcsjService.getPagedDtoList(jcsjDto);
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", jcsjDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 基础数据查看
	 */
	@RequestMapping(value="/data/viewBasicData")
	public ModelAndView viewBasicData(JcsjDto jcsjDto){
		ModelAndView mav = new ModelAndView("systemmain/data/basicdata_view");
		mav.addObject("t_jcsjDto", jcsjDto);
		JcsjDto jcsjDto_t = jcsjService.getDtoById(jcsjDto.getCsid());
		List<Map<String, String>> jcsjModels = jcsjService.getJclbList();
		for (Map<String, String> jcsjModel : jcsjModels) {
			if (jcsjDto_t.getJclb().equals(jcsjModel.get("id"))) {
				jcsjDto_t.setJclbmc(jcsjModel.get("name"));
			}
		}
		mav.addObject("jcsjDto", jcsjDto_t);
		List<FjcfbDto> fjcfList =fjcfbService.selectFjcfbDtoByYwid(jcsjDto.getCsid());
		mav.addObject("fjcfList",fjcfList);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 基础数据查看 vue
	 */
	@RequestMapping(value="/data/viewBasicData_vue")
	@ResponseBody
	public Map<String,Object> viewBasicData_vue(JcsjDto jcsjDto){
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try {
			resMap.put("t_jcsjDto", jcsjDto);
			JcsjDto jcsjDto_t = jcsjService.getDtoById(jcsjDto.getCsid());
			List<Map<String, String>> jcsjModels = jcsjService.getJclbList();
			for (Map<String, String> jcsjModel : jcsjModels) {
				if (jcsjDto_t.getJclb().equals(jcsjModel.get("id"))) {
					jcsjDto_t.setJclbmc(jcsjModel.get("name"));
				}
			}
			resMap.put("jcsjDto", jcsjDto_t);
			List<FjcfbDto> fjcfList =fjcfbService.selectFjcfbDtoByYwid(jcsjDto.getCsid());
			resMap.put("fjcfList",fjcfList);
			resMap.put("urlPrefix", urlPrefix);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}

		return resMap;
	}
	
	/**
	 * 基础数据新增页面
	 */
	@RequestMapping(value="/data/addBasicData")
	public ModelAndView addBasicData(JcsjDto jcsjDto){
		ModelAndView mav = new ModelAndView("systemmain/data/basicdata_edit");
		
		List<Map<String, String>> jcsjModels = jcsjService.getJclbList();
		//循环获取基础类别信息，如果有父结构，则获取父节点信息
		for (Map<String, String> jcsjModel : jcsjModels) {
			if (jcsjDto.getJclb().equals(jcsjModel.get("id"))) {
				if (StringUtil.isNotBlank(jcsjModel.get("parentId"))) {
					JcsjDto tJcsjDto = new JcsjDto();
					tJcsjDto.setJclb(jcsjModel.get("parentId"));
					List<JcsjDto> fList = jcsjService.getDtoList(tJcsjDto);
					mav.addObject("fList", fList);
				}
				if (StringUtil.isNotBlank(jcsjModel.get("parentFId"))) {
					JcsjDto tJcsjDto = new JcsjDto();
					tJcsjDto.setJclb(jcsjModel.get("parentFId"));
					List<JcsjDto> ffList = jcsjService.getDtoList(tJcsjDto);
					mav.addObject("ffList", ffList);
				}
				break;
			}
		}
		mav.addObject("jclbList",jcsjModels);
		mav.addObject("jcsjDto",jcsjDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 基础数据新增页面 vue
	 */
	@RequestMapping(value="/data/addBasicData_vue")
	@ResponseBody
	public Map<String,Object> addBasicData_vue(JcsjDto jcsjDto){
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try{
			List<Map<String, String>> jcsjModels = jcsjService.getJclbList();
			//循环获取基础类别信息，如果有父结构，则获取父节点信息
			for (Map<String, String> jcsjModel : jcsjModels) {
				if (jcsjDto.getJclb().equals(jcsjModel.get("id"))) {
					if (StringUtil.isNotBlank(jcsjModel.get("parentId"))) {
						JcsjDto tJcsjDto = new JcsjDto();
						tJcsjDto.setJclb(jcsjModel.get("parentId"));
						List<JcsjDto> fList = jcsjService.getDtoList(tJcsjDto);
						resMap.put("fList", fList);
					}
					if (StringUtil.isNotBlank(jcsjModel.get("parentFId"))) {
						JcsjDto tJcsjDto = new JcsjDto();
						tJcsjDto.setJclb(jcsjModel.get("parentFId"));
						List<JcsjDto> ffList = jcsjService.getDtoList(tJcsjDto);
						resMap.put("ffList", ffList);
					}
					break;
				}
			}
			resMap.put("jclbList",jcsjModels);
			resMap.put("jcsjDto",jcsjDto);
			resMap.put("urlPrefix", urlPrefix);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}
		return resMap;
	}
	
	/**
	 * 基础数据新增保存
	 */
	@RequestMapping(value="/data/addSaveBasicData")
	@ResponseBody
	public Map<String, Object> addSaveBasicData(JcsjDto jcsjDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		//获取用户信息
		User user = getLoginInfo(request);
		jcsjDto.setLrry(user.getYhid());
		jcsjDto.setPrefix(prefixFlg);
		checkXSSValue(jcsjDto);
		boolean isSuccess;
		try {
			isSuccess = jcsjService.insertJcsj(jcsjDto);
			if("1".equals(jcsjDto.getSfgb()) && isSuccess) {
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.jcsj.update",JSONObject.toJSONString(jcsjDto));
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			map.put("status","fail");
			map.put("message", e.getMsg());
			return map;
		}
		
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 基础数据推送
	 */
	@RequestMapping(value="/data/pushBasicData")
	@ResponseBody
	public Map<String, Object> pushBasicData(JcsjDto jcsjDto){
		return jcsjService.pushJcsjLisToWechat(jcsjDto);
	}
	
	/**
	 * 基础数据修改
	 */
	@RequestMapping(value="/data/modBasicData")
	public ModelAndView modBasicData(JcsjDto jcsjDto){
		ModelAndView mav = new ModelAndView("systemmain/data/basicdata_edit");
		
		List<Map<String, String>> jcsjModels = jcsjService.getJclbList();
		Map<String, String> currentMap = null;
		//循环获取基础类别信息，如果有父结构，则获取父节点信息
		for (Map<String, String> jcsjModel : jcsjModels) {
			if (jcsjDto.getJclb().equals(jcsjModel.get("id"))) {
				if (StringUtil.isNotBlank(jcsjModel.get("parentId"))) {
					JcsjDto tJcsjDto = new JcsjDto();
					tJcsjDto.setJclb(jcsjModel.get("parentId"));
					List<JcsjDto> fList = jcsjService.getDtoList(tJcsjDto);
					mav.addObject("fList", fList);
				}
				if (StringUtil.isNotBlank(jcsjModel.get("parentFId"))) {
					JcsjDto tJcsjDto = new JcsjDto();
					tJcsjDto.setJclb(jcsjModel.get("parentFId"));
					List<JcsjDto> ffList = jcsjService.getDtoList(tJcsjDto);
					mav.addObject("ffList", ffList);
				}
				currentMap = jcsjModel;
				break;
			}
		}
		mav.addObject("jclbList",jcsjModels);
		
		jcsjDto = jcsjService.getDtoById(jcsjDto.getCsid());
		if(jcsjDto == null){
			jcsjDto = new JcsjDto(); 
		}
		jcsjDto.setFormAction("mod");
		mav.addObject("jcsjDto", jcsjDto);
		
		if(currentMap!=null){
			if(StringUtil.isNotBlank(currentMap.get("basic_file_1"))) {
				mav.addObject("fjList", getFjList(jcsjDto.getCsid(),currentMap.get("basic_file_1")));
			}
			if(StringUtil.isNotBlank(currentMap.get("basic_file_2"))) {
				mav.addObject("fjList2", getFjList(jcsjDto.getCsid(),currentMap.get("basic_file_2")));
			}
			if(StringUtil.isNotBlank(currentMap.get("basic_file_3"))) {
				mav.addObject("fjList3", getFjList(jcsjDto.getCsid(),currentMap.get("basic_file_3")));
			}
		}
		
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 基础数据修改 vue
	 */
	@RequestMapping(value="/data/modBasicData_vue")
	@ResponseBody
	public Map<String,Object> modBasicData_vue(JcsjDto jcsjDto){
		Map<String,Object> resMap = new HashMap<>();
		resMap.put("status", "success");
		try{
			List<Map<String, String>> jcsjModels = jcsjService.getJclbList();
			Map<String, String> currentMap = null;
			//循环获取基础类别信息，如果有父结构，则获取父节点信息
			for (Map<String, String> jcsjModel : jcsjModels) {
				if (jcsjDto.getJclb().equals(jcsjModel.get("id"))) {
					if (StringUtil.isNotBlank(jcsjModel.get("parentId"))) {
						JcsjDto tJcsjDto = new JcsjDto();
						tJcsjDto.setJclb(jcsjModel.get("parentId"));
						List<JcsjDto> fList = jcsjService.getDtoList(tJcsjDto);
						resMap.put("fList", fList);
					}
					if (StringUtil.isNotBlank(jcsjModel.get("parentFId"))) {
						JcsjDto tJcsjDto = new JcsjDto();
						tJcsjDto.setJclb(jcsjModel.get("parentFId"));
						List<JcsjDto> ffList = jcsjService.getDtoList(tJcsjDto);
						resMap.put("ffList", ffList);
					}
					currentMap = jcsjModel;
					break;
				}
			}
			resMap.put("jclbList",jcsjModels);

			jcsjDto = jcsjService.getDtoById(jcsjDto.getCsid());
			if(jcsjDto == null){
				jcsjDto = new JcsjDto();
			}
			jcsjDto.setFormAction("mod");
			resMap.put("jcsjDto", jcsjDto);

			if(currentMap!=null){
				if(StringUtil.isNotBlank(currentMap.get("basic_file_1"))) {
					resMap.put("fjList", getFjList(jcsjDto.getCsid(),currentMap.get("basic_file_1")));
				}
				if(StringUtil.isNotBlank(currentMap.get("basic_file_2"))) {
					resMap.put("fjList2", getFjList(jcsjDto.getCsid(),currentMap.get("basic_file_2")));
				}
				if(StringUtil.isNotBlank(currentMap.get("basic_file_3"))) {
					resMap.put("fjList3", getFjList(jcsjDto.getCsid(),currentMap.get("basic_file_3")));
				}
			}

			resMap.put("urlPrefix", urlPrefix);
		}catch (Exception e){
			resMap.put("status", "fail");
			resMap.put("message", e.getMessage());
		}

		return resMap;
	}
	
	/**
	 * 根据 参数ID 和 业务类型，获取附件存放表里的复检信息
	 */
	private List<FjcfbDto> getFjList(String csid,String fileType){
		FjcfbDto fjcfbDto_D=new FjcfbDto();
		fjcfbDto_D.setYwid(csid);
		fjcfbDto_D.setYwlx(fileType);
		return fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto_D);
	}
	
	/**
	 * 基础数据修改保存
	 */
	@RequestMapping(value="/data/modSaveBasicData")
	@ResponseBody
	public Map<String, Object> modSaveBasicData(JcsjDto jcsjDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		jcsjDto.setXgry(user.getYhid());
		jcsjDto.setPrefix(prefixFlg);
		checkXSSValue(jcsjDto);
		boolean isSuccess;
		try {
			isSuccess = jcsjService.updateJcsj(jcsjDto);
			if("1".equals(jcsjDto.getSfgb()) && isSuccess) {
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.jcsj.update",JSONObject.toJSONString(jcsjDto));
			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			map.put("status","fail");
			map.put("message", e.getMsg());
			return map;
		}
		
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 删除基础数据
	 */
	@RequestMapping(value="/data/delBasicData")
	@ResponseBody
	public Map<String, Object> delBasicData(JcsjDto jcsjDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		jcsjDto.setScry(user.getYhid());
		jcsjDto.setPrefix(prefixFlg);
		jcsjDto.setScbj("1");
		List<JcsjDto> JscjDtos=jcsjService.getJcsjByids(jcsjDto);
		List<String> ids=new ArrayList<>();
		boolean isSuccess = jcsjService.delete(jcsjDto);
		if(!JscjDtos.isEmpty()) {
			for (JcsjDto jscjDto : JscjDtos) {
				if ("1".equals(jscjDto.getSfgb())) {
					ids.add(jscjDto.getCsid());
				}
			}
			//更新该基础类别的基础数据
			jcsjService.resetRedisJcsjList(jcsjDto.getJclb());
		}
		if(!ids.isEmpty()) {
			jcsjDto.setIds(ids);
			if(isSuccess) {
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.jcsj.del",JSONObject.toJSONString(jcsjDto));
			}
		}
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 启用基础数据
	 */
	@RequestMapping(value="/data/enableBasicData")
	@ResponseBody
	public Map<String, Object> enableBasicData(JcsjDto jcsjDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		jcsjDto.setScry(user.getYhid());
		boolean isSuccess = jcsjService.delete(jcsjDto);
		//更新该基础类别的基础数据
		jcsjService.resetRedisJcsjList(jcsjDto.getJclb());
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 停用基础数据
	 */
	@RequestMapping(value="/data/disableBasicData")
	@ResponseBody
	public Map<String, Object> disableBasicData(JcsjDto jcsjDto,HttpServletRequest request){
		return enableBasicData(jcsjDto,request);
	}

	
	/**
	 * 异步获取基础数据子类别
	 */
	@RequestMapping(value ="data/ansyGetJcsjList")
	@ResponseBody
	public List<JcsjDto> ansyGetJcsjList(JcsjDto jcsjDto){

		return jcsjService.getJcsjDtoList(jcsjDto);
	}
	
	/**
	 * 异步获取基础数据子类别(包含停用)
	 */
	@RequestMapping(value ="data/ansyGetJcsjListInStop")
	@ResponseBody
	public List<JcsjDto> ansyGetJcsjListInStop(JcsjDto jcsjDto){
		return jcsjService.getJcsjDtoListInStop(jcsjDto);
	}
	
	/**
	 * 检查输入项里的非法字符
	 */
	private boolean checkXSSValue(JcsjDto jcsjDto){
		if(jcsjDto == null)
			return true;
		
		jcsjDto.setCsdm(StringUtil.changeXSSInfo(jcsjDto.getCsdm()));
		jcsjDto.setCsmc(StringUtil.changeXSSInfo(jcsjDto.getCsmc()));
		jcsjDto.setBz(StringUtil.changeXSSInfo(jcsjDto.getBz()));
		jcsjDto.setCskz1(StringUtil.changeXSSInfo(jcsjDto.getCskz1()));
		jcsjDto.setCskz2(StringUtil.changeXSSInfo(jcsjDto.getCskz2()));
		jcsjDto.setCskz3(StringUtil.changeXSSInfo(jcsjDto.getCskz3()));
		jcsjDto.setCskz4(StringUtil.changeXSSInfo(jcsjDto.getCskz4()));
		return true;
	}
}
