package com.matridx.igams.storehouse.controller;


import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.cloud.controller.BaseBasicController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.storehouse.dao.entities.CkxxDto;
import com.matridx.igams.storehouse.dao.entities.CpjgDto;
import com.matridx.igams.storehouse.dao.entities.CpjgmxDto;
import com.matridx.igams.storehouse.service.svcinterface.ICkxxService;
import com.matridx.igams.storehouse.service.svcinterface.ICpjgService;
import com.matridx.igams.storehouse.service.svcinterface.ICpjgmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 成品结构
 * @author hmz
 *
 */
@Controller
@RequestMapping("/storehouse")
public class ProductStructureController extends BaseBasicController {

	@Autowired
	RedisUtil redisUtil;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	private ICkxxService ckxxService;
	@Autowired
	private ICpjgService cpjgService;
	@Autowired
	private IXxglService xxglService;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	@Autowired
	private ICpjgmxService cpjgmxService;

	/**
	 * 产品结构资列表
	 */
	@RequestMapping(value = "/structure/pageListProduction")
	public ModelAndView pageListProduction() {
		ModelAndView mav = new ModelAndView("storehouse/structure/structure_list");
		List<JcsjDto> bomList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.BOM_TYPE.getCode());
		CpjgDto cpjgDto1 = new CpjgDto();
		mav.addObject("cpjgDto", cpjgDto1);
		mav.addObject("bomList",bomList);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 获取产品结构列表
	 *
	 * @return
	 */
	@RequestMapping("/structure/pageGetListProduction")
	@ResponseBody
	public Map<String, Object> pageGetListProduction(CpjgDto cpjgDto){
		List<CpjgDto> cpjgDtoList=cpjgService.getPagedDtoList(cpjgDto);
		Map<String, Object> map = new HashMap<>();
		map.put("rows",cpjgDtoList);
		map.put("total",cpjgDto.getTotalNumber());
		return map;
	}
	/**
	 * 查看
	 */
	@RequestMapping(value = "/structure/viewProduction")
	public ModelAndView getStructureInfo(CpjgDto cpjgDto) {
		ModelAndView mav = new ModelAndView("storehouse/structure/structure_view");
		cpjgDto=cpjgService.getDtoById(cpjgDto.getCpjgid());
		mav.addObject("cpjgDto",cpjgDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 获取产品结构明细
	 */
	@RequestMapping("/structure/pagedataGetListStructureInfo")
	@ResponseBody
	public Map<String, Object> getListStructureInfo(CpjgmxDto cpjgmxDto) {
		Map<String, Object> map = new HashMap<>();
		List<CpjgmxDto> list = cpjgmxService.getPagedDtoList(cpjgmxDto);
		map.put("rows", list);
		map.put("total", cpjgmxDto.getTotalNumber());
		return map;
	}
	/**
	 * 成品新增页面
	 *
	 * @return
	 */
	@RequestMapping(value = "/structure/addProduct")
	public ModelAndView addProduct(CpjgDto cpjgDto) {
		ModelAndView mav = new ModelAndView("storehouse/structure/structure_edit");
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		cpjgDto.setBbrq(simpleDateFormat.format(date));
		cpjgDto.setMjshl("0");
		cpjgDto.setFormAction("/storehouse/structure/addSaveProduct");
		mav.addObject("cpjgDto", cpjgDto);
		List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.BOM_TYPE.getCode());
		List<JcsjDto> jcsjDtoList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SUPPLY_TYPE.getCode());
		List<JcsjDto> dtoList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.OUTPUT_TYPE.getCode());
		CkxxDto ckxxDto = new CkxxDto();
		ckxxDto.setFckflg("1");
		List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
		mav.addObject("jcsjDtos", jcsjDtos);
		mav.addObject("jcsjDtoList", JSONObject.toJSONString(jcsjDtoList));
		mav.addObject("dtoList", JSONObject.toJSONString(dtoList));
		mav.addObject("ckxxDtos", JSONObject.toJSONString(ckxxDtos));
		mav.addObject("url", "/storehouse/structure/pagedataAddProductInfo");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 成品新增明细
	 *
	 * @return
	 */
	@RequestMapping("/structure/pagedataAddProductInfo")
	@ResponseBody
	public Map<String, Object> pagedataAddProductInfo(){
		List<CpjgmxDto> dtoMxList = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("rows",dtoMxList);
		return map;
	}

	/**
	 * 物料列表页面
	 * @return
	 */
	@RequestMapping(value ="/materiel/pagedataListMater")
	public ModelAndView pageListMater(){
		ModelAndView mav = new ModelAndView("storehouse/structure/mater_choose");
		mav.addObject("wllblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MATERIEL_TYPE.getCode()));
		mav.addObject("wlzlbtclist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MATERIEL_SUBTYPE_COLLECTIVELY.getCode()));
		mav.addObject("lblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MATERIELQUALITY_TYPE.getCode()));
		mav.addObject("wlaqlblist", redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MATERIEL_SAFE_TYPE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 新增保存
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/structure/addSaveProduct")
	@ResponseBody
	public Map<String, Object> addSaveProductInfo(CpjgDto cpjgDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		cpjgDto.setLrry(user.getYhid());
		cpjgDto.setCpjgid(StringUtil.generateUUID());
		boolean isSuccess;
		try {
			isSuccess = cpjgService.saveAddProductInfo(cpjgDto);
		} catch (BusinessException e) {
			map.put("status","fail");
			map.put("message",e.getMsg());
			return map;
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 判断物料是否引用
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/structure/pagedataWhetherWl")
	@ResponseBody
	public Map<String, Object> pagedataWhetherWl(CpjgDto cpjgDto) {
		Map<String, Object> map = new HashMap<>();
		boolean success = false;
		List<CpjgDto> dtoList = cpjgService.getDtoList(cpjgDto);
		if (CollectionUtils.isEmpty(dtoList)){
			success = true;
		}
		map.put("status", success);
		return map;
	}


	/**
	 * 成品修改页面
	 *
	 * @return
	 */
	@RequestMapping(value = "/structure/modProduct")
	public ModelAndView modProduct(CpjgDto cpjgDto) {
		ModelAndView mav = new ModelAndView("storehouse/structure/structure_edit");
		CpjgDto dtoById = cpjgService.getDtoById(cpjgDto.getCpjgid());
		dtoById.setFormAction("/storehouse/structure/pagedataSaveProductInfo");
		mav.addObject("cpjgDto", dtoById);
		List<JcsjDto> jcsjDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.BOM_TYPE.getCode());
		List<JcsjDto> jcsjDtoList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SUPPLY_TYPE.getCode());
		List<JcsjDto> dtoList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.OUTPUT_TYPE.getCode());
		CkxxDto ckxxDto = new CkxxDto();
		ckxxDto.setFckflg("1");
		List<CkxxDto> ckxxDtos = ckxxService.getDtoList(ckxxDto);
		mav.addObject("jcsjDtos", jcsjDtos);
		mav.addObject("jcsjDtoList", JSONObject.toJSONString(jcsjDtoList));
		mav.addObject("dtoList", JSONObject.toJSONString(dtoList));
		mav.addObject("ckxxDtos", JSONObject.toJSONString(ckxxDtos));
		mav.addObject("url", "/storehouse/structure/pagedataProductInfo");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 成品修改明细
	 *
	 * @return
	 */
	@RequestMapping("/structure/pagedataProductInfo")
	@ResponseBody
	public Map<String, Object> modProductInfo(CpjgmxDto cpjgmxDto){
		List<CpjgmxDto> dtoMxList = cpjgmxService.getDtoList(cpjgmxDto);
		Map<String, Object> map = new HashMap<>();
		map.put("rows",dtoMxList);
		return map;
	}


	/**
	 * 修改保存
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/structure/pagedataSaveProductInfo")
	@ResponseBody
	public Map<String, Object> modSaveProductInfo(CpjgDto cpjgDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		cpjgDto.setLrry(user.getYhid());
		boolean isSuccess;
		try {
			isSuccess = cpjgService.saveModProductInfo(cpjgDto);
		} catch (BusinessException e) {
			map.put("status","fail");
			map.put("message",e.getMsg());
			return map;
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}


	/**
	 * 删除
	 * @param
	 * @param
	 * @return
	 */
	@RequestMapping("/structure/delProductInfo")
	@ResponseBody
	public Map<String, Object> delProductInfo(CpjgDto cpjgDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		cpjgDto.setScry(user.getYhid());
		boolean isSuccess;
		try {
			isSuccess = cpjgService.saveDelProductInfo(cpjgDto);
		} catch (BusinessException e) {
			map.put("status","fail");
			map.put("message",e.getMsg());
			return map;
		}
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
