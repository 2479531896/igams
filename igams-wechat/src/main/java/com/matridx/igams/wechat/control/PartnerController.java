package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.GrszDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XxglModel;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IGrszService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.RabbitUtils;
import com.matridx.igams.wechat.dao.entities.HbbgmbglDto;
import com.matridx.igams.wechat.dao.entities.HbdwqxDto;
import com.matridx.igams.wechat.dao.entities.HbhzkhDto;
import com.matridx.igams.wechat.dao.entities.HbqxDto;
import com.matridx.igams.wechat.dao.entities.HbsfbzDto;
import com.matridx.igams.wechat.dao.entities.HbxxzDto;
import com.matridx.igams.wechat.dao.entities.SjhbxxDto;
import com.matridx.igams.wechat.dao.entities.WxyhDto;
import com.matridx.igams.wechat.dao.entities.YhqtxxDto;
import com.matridx.igams.wechat.service.svcinterface.IHbbgmbglService;
import com.matridx.igams.wechat.service.svcinterface.IHbdwqxService;
import com.matridx.igams.wechat.service.svcinterface.IHbhzkhService;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.IHbsfbzService;
import com.matridx.igams.wechat.service.svcinterface.IHbxxzService;
import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
import com.matridx.igams.wechat.service.svcinterface.IWxyhService;
import com.matridx.igams.wechat.service.svcinterface.IYhqtxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/partner")
public class PartnerController extends BaseController {
	@Autowired 
	private ISjhbxxService sjhbservice;
	@Autowired
	private IXxglService xxglservice;
	@Autowired
	private IHbsfbzService sfbzservice;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	private IWxyhService wxyhservice;
	@Autowired
	private IHbqxService hbqxService;
	@Autowired
    IXxglService xxglService;
	@Autowired
    IYhqtxxService yhqtxxService;
	@Autowired
    IHbhzkhService hbhzkhService;
	@Autowired
    IHbdwqxService hbdwqxService;
	@Autowired
	IHbbgmbglService hbbgmbglService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IGrszService grszService;

	public static final String MSG = "message";	//返回信息
	public static final String RESULT = "result";	//执行结果
	/**
	 * 跳转页面
	 * @return
	 */
	@RequestMapping(value="/partner/pageListView")
	public ModelAndView View() {
		ModelAndView mav=new ModelAndView("wechat/sjxx/partner_List");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.CLASSIFY, BasicDataTypeEnum.SUBCLASSIFICATION,
				BasicDataTypeEnum.REPORT_TEMEPLATE, BasicDataTypeEnum.PLATFORM_OWNERSHIP, BasicDataTypeEnum.PLATFORM_OWNERSHIP_ORIGIN, BasicDataTypeEnum.PROVINCE});
		mav.addObject("classifylist",jclist.get(BasicDataTypeEnum.CLASSIFY.getCode()));//合作伙伴分类
		mav.addObject("subclassificationlist",jclist.get(BasicDataTypeEnum.SUBCLASSIFICATION.getCode()));//合作伙伴子分类
		mav.addObject("templatelist",jclist.get(BasicDataTypeEnum.REPORT_TEMEPLATE.getCode()));//报告模板
		mav.addObject("yptgslist",jclist.get(BasicDataTypeEnum.PLATFORM_OWNERSHIP_ORIGIN.getCode()));//平台归属
		mav.addObject("ptgslist",jclist.get(BasicDataTypeEnum.PLATFORM_OWNERSHIP.getCode()));//销售部门
		mav.addObject("sflist",jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));//省份
		return mav;
	}
	
	/**
	 * 合作伙伴设置
	 * @return
	 */
	@RequestMapping(value="/partner/partnerinstallHb")
	public ModelAndView getPartnerInstallPage(HbqxDto hbqxDto) {
		ModelAndView mav=new ModelAndView("wechat/partner/partner_install");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.PROVINCE, BasicDataTypeEnum.USER_DISTINGUISH, BasicDataTypeEnum.CLASSIFY,BasicDataTypeEnum.PLATFORM_OWNERSHIP,BasicDataTypeEnum.PLATFORM_OWNERSHIP_ORIGIN});
		List<YhqtxxDto> xtyhlist= yhqtxxService.getXtyh();
		List<HbqxDto> hbqxlist = new ArrayList<>();
		YhqtxxDto yhqtxxDto=yhqtxxService.getDtoById(hbqxDto.getYhid());
		if(yhqtxxDto==null) {
			yhqtxxDto=new YhqtxxDto();
		}
		if(!StringUtil.isBlank(hbqxDto.getYhid()))
		{
			hbqxlist=hbqxService.getHbqxxx(hbqxDto);
		}
		List<JcsjDto> sub_distinguishList= new ArrayList<>();
		if(yhqtxxDto.getYhqf()!=null) {
			JcsjDto jcsjDto=new JcsjDto();
			jcsjDto.setJclb(BasicDataTypeEnum.USER_SUB_DISTINGUISH.getCode());
			jcsjDto.setFcsid(yhqtxxDto.getYhqf());
			sub_distinguishList=jcsjService.getJcsjDtoList(jcsjDto);
		}
		List<JcsjDto> jcsjlist=jclist.get(BasicDataTypeEnum.CLASSIFY.getCode());
		List<JcsjDto> classifyList=new ArrayList<>();
		for(int i=0;i<jcsjlist.size();i++){
			if(jcsjlist.get(i).getScbj().equals("0")){
				classifyList.add(jcsjlist.get(i));
			}
		}
		mav.addObject("provinceList", jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));//省份
		mav.addObject("partnerList", jclist.get(BasicDataTypeEnum.CLASSIFY.getCode()));//省份
		mav.addObject("distinguishList", jclist.get(BasicDataTypeEnum.USER_DISTINGUISH.getCode()));//用户区分
		mav.addObject("xsbmList", jclist.get(BasicDataTypeEnum.PLATFORM_OWNERSHIP.getCode()));//销售部门
		mav.addObject("ptgsList", jclist.get(BasicDataTypeEnum.PLATFORM_OWNERSHIP_ORIGIN.getCode()));//平台归属
		mav.addObject("classifyList", classifyList);
		mav.addObject("sub_distinguishList",sub_distinguishList);//用户子区分
		mav.addObject("hbqxlist", hbqxlist);
		mav.addObject("hbqxDto", hbqxDto);
		mav.addObject("xtyhlist", xtyhlist);
		mav.addObject("yhqtxxDto", yhqtxxDto);
		return mav;
	}
	
	 /**
     * 查询子区分
     * @param jcsjDto
     * @return
     */
    @RequestMapping(value="/pagedataDistinguishList")
	@ResponseBody
    public List<JcsjDto> sub_DistinguishList(JcsjDto jcsjDto){
		 jcsjDto.setJclb(BasicDataTypeEnum.USER_SUB_DISTINGUISH.getCode());
        return jcsjService.getJcsjDtoList(jcsjDto);
    }
    
	/**
	 * 添加保存伙伴权限信息
	 * @param hbqxDto
	 * @return
	 */
	@RequestMapping(value="/partner/partnerinstallSaveHb")
	@ResponseBody
	public Map<String,Object> savePartnerInstall(HbqxDto hbqxDto, YhqtxxDto yhqtxxDto) {
		Map<String, Object> data = new HashMap<>();
		Boolean result = false;			
		try {
			Map<String, Object> map= new HashMap<>();
			Map<String,Object> map1 = new HashMap<>();//该map1主要用来存更新上级伙伴设置所需要的上级用户ids和当前用户选中的权限ids
			User user=getLoginInfo();
			yhqtxxDto.setLrry(user.getYhid());
			yhqtxxDto.setScry(user.getYhid());
			boolean isSuccess;
			isSuccess = hbqxService.insertHbqx(hbqxDto,yhqtxxDto);
			//删除redis中有关该用户的统计数据weekLeadStatis
			redisUtil.delChildLikeItem("weekLeadStatis", hbqxDto.getYhid());
			//根据保存时是否选择了上级用户和伙伴权限来更新上级用户的伙伴权限
			List<String> sjids = new ArrayList<>();
			YhqtxxDto yhqtxx = yhqtxxService.getDtoById(yhqtxxDto.getYhid());
			if (yhqtxx != null ){
				if (  !(yhqtxxDto.getYhid()).equals(yhqtxx.getSjid())  ){
					sjids = yhqtxxService.getSjidList(yhqtxxDto.getYhid());//上级的用户id号List，不包括自己本级
				}
			}
			map1.put("sjids",sjids);
			map1.put("ids",hbqxDto.getIds());
			if((sjids.size()>0 && sjids.get(0)!=null) && hbqxDto.getIds().size()>0) {//如果保存时，用户选择了上级用户并且也勾选了伙伴，此时要更新上级的伙伴权限
				hbqxService.insertSjhbqx(map1);
				for (String sjid : sjids) {
					//删除redis中有关该用户的统计数据weekLeadStatis
					redisUtil.delChildLikeItem("weekLeadStatis", sjid);
				}
			}
			map.put("status", isSuccess ? "success" : "fail");
			map.put("message", isSuccess ? (xxglService.getModelById("ICOM00001").getXxnr()) : xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			String mess = e.getMsg();
			XxglModel xxglModel = xxglService.getModelById(e.getMsgId());
			String idMsg = xxglModel==null?"":xxglModel.getXxnr();
			data.put("status", "fail");
			data.put(MSG, idMsg + (StringUtil.isNotBlank(idMsg) && StringUtil.isNotBlank(mess) ? "<br/>" : "")
					+ (StringUtil.isNotBlank(mess) ? mess : ""));

		}
		data.put(RESULT, result);
		return data;
	}
	
	/**
	 * 合作伙伴列表
	 * @param sjhbxxDto
	 * @return
	 */
    @RequestMapping(value="/partner/pageGetListView")
	@ResponseBody
	public Map<String, Object> getPagedDtoList(SjhbxxDto sjhbxxDto){
    	List<SjhbxxDto> sjhblist=sjhbservice.getPagedDtoList(sjhbxxDto);
    	Map<String, Object> map= new HashMap<>();
    	map.put("total", sjhbxxDto.getTotalNumber());
    	map.put("rows",  sjhblist);
		return map;
	}
    /**
	* 新增页面
     * @return
     */
    @RequestMapping(value="/partner/addPartner")
	public ModelAndView addPartner() {
		ModelAndView mav=new ModelAndView("wechat/sjxx/partner_Save");
		SjhbxxDto sjhbxxDto=new SjhbxxDto();
		sjhbxxDto.setFormAction("addSavePartner");
		List<HbbgmbglDto> bglist=new ArrayList<>();
		//List<JcsjDto> list = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
		JcsjDto t_jcsjDto = new JcsjDto();
		t_jcsjDto.setJclb(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> list = jcsjService.getDtoAndSubListByJclb(t_jcsjDto);
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				HbbgmbglDto hbbgmbglDto_t=new HbbgmbglDto();
				hbbgmbglDto_t.setXh(String.valueOf(i+1));
				hbbgmbglDto_t.setJcxmid(list.get(i).getFcsid());
				hbbgmbglDto_t.setJcxmmc(list.get(i).getFcsmc());
				hbbgmbglDto_t.setJczxmid(list.get(i).getCsid());
				hbbgmbglDto_t.setJczxmmc(list.get(i).getCsmc());
				bglist.add(hbbgmbglDto_t);
			}
		}
		//平台归属
		List<JcsjDto> yptgslist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PLATFORM_OWNERSHIP_ORIGIN.getCode());
		//销售部门
		List<JcsjDto> xsbmlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PLATFORM_OWNERSHIP.getCode());
		mav.addObject("yptgslist",yptgslist);
		mav.addObject("ptgslist",xsbmlist);
		mav.addObject("rows",bglist);
        mav.addObject("jcxmlist", JSON.toJSONString(bglist));
		List<JcsjDto> mblist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.REPORT_TEMEPLATE.getCode());
		mav.addObject("mblist",mblist);
		mav.addObject("sjhbxxDto", sjhbxxDto);
		mav.addObject("sflist",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PROVINCE.getCode()));
		mav.addObject("hbjblist",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PARTNER_LEVEL.getCode()));
		mav.addObject("sjqflist",redisUtil.lgetDto(("List_matridx_jcsj:"+BasicDataTypeEnum.INSPECTION_DIVISION)));
		mav.addObject("templatelist",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.REPORT_TEMEPLATE.getCode()));
		mav.addObject("classifylist",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.CLASSIFY.getCode()));
		mav.addObject("stampList",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.STAMP_TYPE.getCode()));
		mav.addObject("reportList",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.REPORTWAY_TYPE.getCode()));
		mav.addObject("xmList",JSONObject.toJSONString(redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode())));
		mav.addObject("zxmList",JSONObject.toJSONString(redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode())));
		Map<String,String> map=new HashMap<>();
		map.put("zdfs","");//自动发送报告
		map.put("zdfst","");//自动发送报告
		mav.addObject("map",map);
		return mav;
	}

	/**
	 * 批量修改收费标准页面
	 * @param
	 * @return
	 */
	@RequestMapping(value="/partner/batchmodSjhbSfbz")
	public ModelAndView batchmodSjhbSfbz(SjhbxxDto sjhbxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/partner_batchSfbz");
		sjhbxxDto.setFormAction("pagedataBatchmodSfbz");
		mav.addObject("sjhbxxDto", sjhbxxDto);
		mav.addObject("xmList",JSONObject.toJSONString(redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode())));
		mav.addObject("zxmList",JSONObject.toJSONString(redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode())));
		return mav;
	}

	/**
	 * 批量修改收费标准页面
	 * @return
	 */
	@RequestMapping(value="/partner/pagedataBatchmodSfbz")
	@ResponseBody
	public Map<String,Object> pagedataBatchmodSfbz(SjhbxxDto sjhbxxDto) {
		User user = getLoginInfo();
		sjhbxxDto.setLrry(user.getYhid());
		return sjhbservice.batchModSfbz(sjhbxxDto);
	}
    
	/**
	 * 获取当前角色可选检测单位
	 * @param jcsjDto,sjhbxxDto
	 * @return
	 */
	@RequestMapping(value ="/partner/pagedataListUnSelectJcdw")
	@ResponseBody
	public Map<String,Object> listUnSelectProcess(JcsjDto jcsjDto, SjhbxxDto sjhbxxDto){
		if(StringUtil.isEmpty(sjhbxxDto.getHbid())) {
			jcsjDto.setCskz1("");
		}else {
			jcsjDto.setCskz1(sjhbxxDto.getHbid());
		}
		jcsjDto.setJclb(BasicDataTypeEnum.DETECTION_UNIT.getCode());
		List<JcsjDto> jcdwlist = jcsjService.getPagesOptionJcdw(jcsjDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", jcsjDto.getTotalNumber());
		result.put("rows", jcdwlist);
		return result;
	}

	/**
	 * 获取当前角色已选检测单位
	 * @param 
	 * @return
	 */
	@RequestMapping(value ="/partner/pagedataListSelectedJcdw")
	@ResponseBody
	public Map<String,Object> listSelectedProcess(HbdwqxDto hbdwqxDto){
		if(StringUtil.isEmpty(hbdwqxDto.getHbid())) {
			hbdwqxDto.setHbid("");
		}
		List<HbdwqxDto> t_List = hbdwqxService.getPagedSelectedList(hbdwqxDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", hbdwqxDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
    
    
    /**
     * 根据省份去查城市
     * @param jcsjDto
     * @return
     */
    @RequestMapping(value="/pagedataJscjcity")
	@ResponseBody
    public List<JcsjDto> jcsjcity(JcsjDto jcsjDto){
		 jcsjDto.setJclb(BasicDataTypeEnum.CITY.getCode());
        return jcsjService.getJcsjDtoList(jcsjDto);
    }
    
    /**
     * 根据类别去查子类别
     * @param jcsjDto
     * @return
     */
    @RequestMapping(value="/pagedataJscjsubclassify")
	@ResponseBody
    public List<JcsjDto> jscjsubclassify(JcsjDto jcsjDto){
		 jcsjDto.setJclb(BasicDataTypeEnum.SUBCLASSIFICATION.getCode());
        return jcsjService.getJcsjDtoList(jcsjDto);
    }
    
    /**
	* 执行添加操作
     * @param sjhbxxDto
     * @return
     */
    @RequestMapping(value="/partner/addSavePartner")
	@ResponseBody
    public Map<String, Object> addSavePartner(SjhbxxDto sjhbxxDto){
    	Map<String, Object> map= new HashMap<>();
		List<HbsfbzDto> list=(List<HbsfbzDto>) JSON.parseArray(sjhbxxDto.getSfbz_json(),HbsfbzDto.class);
		sjhbxxDto.setHbsfbzs(list);
    	boolean isSuccess=false;
    	int num=sjhbservice.getCountSjhb(sjhbxxDto);
    	if(num>0) {
    		map.put("status",isSuccess?"success":"fail");
    		map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():"送检合作伙伴已经存在，请核实后添加！");
    	}else if(num==0) {
			User user = getLoginInfo();
			sjhbxxDto.setLrry(user.getYhid());
    		isSuccess=sjhbservice.insertAll(sjhbxxDto);
        	map.put("status",isSuccess?"success":"fail");
    		map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
    	}
		return map;
    }
    
    /**
	 * 修改页面
     * @param sjhbxxDto
     * @return
     */
    @RequestMapping(value="/partner/modSjhbxx")
	public ModelAndView modSjhbxx(SjhbxxDto sjhbxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/partner_Save");
		SjhbxxDto sjhbxxDtos=sjhbservice.getDtoById(sjhbxxDto.getHbid());
		JcsjDto jcsjDto =new JcsjDto();
		jcsjDto.setFcsid(sjhbxxDtos.getSf());
		List<JcsjDto> citylist=jcsjService.jcsjcity(jcsjDto);
		sjhbxxDtos.setFormAction("modSaveSjhbxx");
		JcsjDto jcsj_sub=new JcsjDto();
		jcsj_sub.setFcsid(sjhbxxDtos.getFl());
		jcsj_sub.setJclb(BasicDataTypeEnum.SUBCLASSIFICATION.getCode());
		List<JcsjDto> subclassifylist=jcsjService.getJcsjDtoList(jcsj_sub);
		HbbgmbglDto hbbgmbglDto=new HbbgmbglDto();
		hbbgmbglDto.setHbid(sjhbxxDto.getHbid());
        List<HbbgmbglDto> dtoList = hbbgmbglService.getDtoList(hbbgmbglDto);
		List<HbbgmbglDto> mb_list = new ArrayList<>();
        //List<JcsjDto> list = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
		JcsjDto t_jcsjDto = new JcsjDto();
		t_jcsjDto.setJclb(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> list = jcsjService.getDtoAndSubListByJclb(t_jcsjDto);
		String str="";
        if(dtoList!=null&&dtoList.size()>0) {
			for (HbbgmbglDto hbbgmbglDto_t : dtoList) {
				if(StringUtil.isNotBlank(hbbgmbglDto_t.getJczxmid())) {
					str = str + "," + hbbgmbglDto_t.getJcxmid() + "-" + hbbgmbglDto_t.getJczxmid();
				}else
					str = str + "," + hbbgmbglDto_t.getJcxmid();
			}
		}
		//循环此类别的所有基础数据
		for(int i=0;i<list.size();i++){
			String comparedId = "";
			if(StringUtil.isNotBlank(list.get(i).getCsid())) {
				comparedId = list.get(i).getFcsid()+ "-" + list.get(i).getCsid();
			}else{
				comparedId = list.get(i).getFcsid();
			}
			if(!str.contains(comparedId)){
				HbbgmbglDto hbbgmbglDto_t=new HbbgmbglDto();
				hbbgmbglDto_t.setXh(String.valueOf(i+1));
				hbbgmbglDto_t.setJcxmid(list.get(i).getFcsid());
				hbbgmbglDto_t.setJcxmmc(list.get(i).getFcsmc());
				hbbgmbglDto_t.setJczxmid(list.get(i).getCsid());
				hbbgmbglDto_t.setJczxmmc(list.get(i).getCsmc());
				mb_list.add(hbbgmbglDto_t);
			}else{
				//已设置的报告模板列表
				for(HbbgmbglDto dto:dtoList){
					if(StringUtil.isBlank(list.get(i).getCsid())) {
						if (list.get(i).getFcsid().equals(dto.getJcxmid())) {
							dto.setXh(String.valueOf(i + 1));
							mb_list.add(dto);
						}
					}else{
						if (list.get(i).getFcsid().equals(dto.getJcxmid()) && list.get(i).getCsid().equals(dto.getJczxmid())) {
							dto.setXh(String.valueOf(i + 1));
							mb_list.add(dto);
						}
					}
				}
			}
		}
		//平台归属
		List<JcsjDto> ptgslist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PLATFORM_OWNERSHIP_ORIGIN.getCode());
		//销售部门
		List<JcsjDto> xsbmlist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PLATFORM_OWNERSHIP.getCode());
		mav.addObject("yptgslist",ptgslist);
		mav.addObject("ptgslist",xsbmlist);
        mav.addObject("rows",mb_list);
        mav.addObject("jcxmlist", JSON.toJSONString(mb_list));
        List<JcsjDto> mblist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.REPORT_TEMEPLATE.getCode());
        mav.addObject("mblist",mblist);
		mav.addObject("sjhbxxDto",sjhbxxDtos);
		HbhzkhDto hbhzkhDto = new HbhzkhDto();
		hbhzkhDto.setHbid(sjhbxxDtos.getHbid());
		List<HbhzkhDto> customerList = hbhzkhService.getDtoList(hbhzkhDto);
		mav.addObject("customerList", JSONObject.toJSONString(customerList));
		mav.addObject("citylist", citylist);
		mav.addObject("subclassifylist", subclassifylist);
		mav.addObject("sflist",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PROVINCE.getCode()));
		mav.addObject("sjqflist",redisUtil.lgetDto(("List_matridx_jcsj:"+BasicDataTypeEnum.INSPECTION_DIVISION)));
		mav.addObject("hbjblist",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.PARTNER_LEVEL.getCode()));
		mav.addObject("datectlist",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode()));
		mav.addObject("templatelist",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.REPORT_TEMEPLATE.getCode()));
		mav.addObject("classifylist",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.CLASSIFY.getCode()));
		mav.addObject("stampList",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.STAMP_TYPE.getCode()));
		mav.addObject("reportList",redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.REPORTWAY_TYPE.getCode()));
		mav.addObject("xmList",JSONObject.toJSONString(redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode())));
		mav.addObject("zxmList",JSONObject.toJSONString(redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode())));
		Map<String,String> map=new HashMap<>();
		map.put("zdfs","");//mNGS自动发送
		map.put("zdfst","");//tNGS自动发送
		if(StringUtil.isNotBlank(sjhbxxDtos.getKzsz())){
			String json=sjhbxxDtos.getKzsz();
			ObjectMapper mapper = new ObjectMapper();
			try {
				JsonNode jsonNode = mapper.readTree(json);
				Map<String, String> tempMap = mapper.convertValue(jsonNode, new TypeReference<Map<String, String>>() {});
				// 动态更新已存在的key
				tempMap.forEach((k, v) -> {
					if (map.containsKey(k)) {
						map.put(k, v);
					}
				});
			}catch(Exception ex) {
				// 保持原始键值重置逻辑
				map.keySet().forEach(k -> map.put(k, ""));
			}
		}
		mav.addObject("map",map);
		return mav;
	}

	@RequestMapping(value="/partner/pagedataGetSfzbs")
	@ResponseBody
	public Map<String, Object> pagedataGetSfzbs(SjhbxxDto sjhbxxDto){
		Map<String, Object> map= new HashMap<>();
		List<HbsfbzDto> sfbzlist = new ArrayList<>();
		if (StringUtil.isNotBlank(sjhbxxDto.getHbid())){
			sfbzlist=sfbzservice.getDtosByHbid(sjhbxxDto);
			map.put("rows",sfbzlist);
		} else {
			map.put("rows",sfbzlist);
		}
		return map;
	}
    
    /**
	 * 执行修改操作
     * @param sjhbxxDto
     * @return
     */
    @RequestMapping(value="/partner/modSaveSjhbxx")
	@ResponseBody
    public Map<String, Object> updatepartner(SjhbxxDto sjhbxxDto){
    	Map<String, Object> map= new HashMap<>();
    	boolean isSuccess=false;
		List<HbsfbzDto> list=(List<HbsfbzDto>) JSON.parseArray(sjhbxxDto.getSfbz_json(),HbsfbzDto.class);
		sjhbxxDto.setHbsfbzs(list);
    	int num=sjhbservice.getCountSjhb(sjhbxxDto);
    	if(num>0) {
    		map.put("status", isSuccess?"success":"fail");
    		map.put("message", isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():"送检合作伙伴已经存在，请核实后添加！");
    	}else if(num==0) {
			User user = getLoginInfo();
			sjhbxxDto.setXgry(user.getYhid());
			sjhbxxDto.setLrry(user.getYhid());
    		isSuccess=sjhbservice.updateAll(sjhbxxDto);
    		map.put("status", isSuccess?"success":"fail");
    		map.put("message", isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
    	}
		return map;
    }
    
    /**
	* 删除合作伙伴
     * @param sjhbxxDto
     * @return
     */
    @RequestMapping(value="/partner/delpartner")
    @ResponseBody
    public Map<String, Object> delpartner(SjhbxxDto sjhbxxDto){
    	User user=getLoginInfo();
    	sjhbxxDto.setScry(user.getYhid());
    	boolean isSuccess=sjhbservice.deletepartner(sjhbxxDto);
    	Map<String, Object> map= new HashMap<>();
    	map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr());
		return map;
    }
    
    /**
 	 * 查看详细信息
     * @param sjhbxxDto
     * @return
     */
    @RequestMapping(value="/partner/viewpartner")
    public ModelAndView viewSjhbxx(SjhbxxDto sjhbxxDto) {
    	ModelAndView mav=new ModelAndView("wechat/sjxx/partner_View");
    	SjhbxxDto t_sjhbxxDto = sjhbservice.getDtoById(sjhbxxDto.getHbid());
    	//sjhbxxDtos.setFsfs(NoticeTypeEnum.getValueByCode(sjhbxxDtos.getFsfs()));
    	if(StringUtil.isNotBlank(t_sjhbxxDto.getCskz1())){
    		List<String> ids = new ArrayList<>();
    		ids = StringUtil.splitMsg(ids, t_sjhbxxDto.getCskz1(), "，|,");
    		WxyhDto wxyhDto = new WxyhDto();
    		wxyhDto.setIds(ids);
    		List<WxyhDto> wxyhDtos = wxyhservice.getListByIds(wxyhDto);
    		String cskz1mc = null;
    		for (int i = 0; i < wxyhDtos.size(); i++) {
    			if(i == 0){
    				cskz1mc = wxyhDtos.get(i).getWxm();
    			}else{
    				cskz1mc += "," + wxyhDtos.get(i).getWxm();
    			}
			}
    		t_sjhbxxDto.setCskz1mc(cskz1mc);
    	}
//    	==========增加检测单位的显示：方法为查找出选择的检测单位组装一个StringBuffer===========
    	HbdwqxDto hbdwqxDto = new HbdwqxDto();
    	hbdwqxDto.setHbid(sjhbxxDto.getHbid());
    	if(StringUtil.isEmpty(hbdwqxDto.getHbid())) {
			hbdwqxDto.setHbid("");
		}
    	List<HbdwqxDto> jcdw_List = hbdwqxService.getJcdwSelectedList(hbdwqxDto);//已经选择的检测单位
    	if(jcdw_List != null ) {
    		StringBuffer sb = new StringBuffer();
    		sb.setLength(0);
    		for(int j = 0 ; j < jcdw_List.size(); j++) {
    			if(j != 0)
    				sb.append(",  ");
    			sb.append(jcdw_List.get(j).getJcdwmc());
    		}
    		t_sjhbxxDto.setJcdwstr(sb.toString());
    	}

		HbbgmbglDto hbbgmbglDto=new HbbgmbglDto();
    	hbbgmbglDto.setHbid(sjhbxxDto.getHbid());
		List<HbbgmbglDto> dtoList = hbbgmbglService.getDtoList(hbbgmbglDto);
		//List<JcsjDto> list = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode());
		JcsjDto t_jcsjDto = new JcsjDto();
		t_jcsjDto.setJclb(BasicDataTypeEnum.DETECT_TYPE.getCode());
		List<JcsjDto> list = jcsjService.getDtoAndSubListByJclb(t_jcsjDto);
		List<HbbgmbglDto> mb_list = new ArrayList<>();
		if(dtoList!=null&&dtoList.size()>0){
			String str="";
			for(HbbgmbglDto hbbgmbglDto_t:dtoList){
				//str=str+","+hbbgmbglDto_t.getJcxmid();
				if(StringUtil.isNotBlank(hbbgmbglDto_t.getJczxmid())) {
					str = str + "," + hbbgmbglDto_t.getJcxmid() + "-" + hbbgmbglDto_t.getJczxmid();
				}else
					str = str + "," + hbbgmbglDto_t.getJcxmid();
			}
			for(int i=0;i<list.size();i++){
	            String comparedId = "";
	            if(StringUtil.isNotBlank(list.get(i).getCsid())) {
	                comparedId = list.get(i).getFcsid()+ "-" + list.get(i).getCsid();
	            }else{
	                comparedId = list.get(i).getFcsid();
	            }
	            if(!str.contains(comparedId)){
					HbbgmbglDto hbbgmbglDto_t=new HbbgmbglDto();
					hbbgmbglDto_t.setXh(String.valueOf(i+1));
	                hbbgmbglDto_t.setJcxmid(list.get(i).getFcsid());
	                hbbgmbglDto_t.setJcxmmc(list.get(i).getFcsmc());
	                hbbgmbglDto_t.setJczxmid(list.get(i).getCsid());
	                hbbgmbglDto_t.setJczxmmc(list.get(i).getCsmc());
					mb_list.add(hbbgmbglDto_t);
				}else{
					for(HbbgmbglDto dto:dtoList){
						if(StringUtil.isBlank(list.get(i).getCsid())) {
							if (list.get(i).getFcsid().equals(dto.getJcxmid())) {
								dto.setXh(String.valueOf(i + 1));
								mb_list.add(dto);
							}
						}else{
							if (list.get(i).getFcsid().equals(dto.getJcxmid()) && list.get(i).getCsid().equals(dto.getJczxmid())) {
								dto.setXh(String.valueOf(i + 1));
								mb_list.add(dto);
							}
						}
					}
				}
			}
		}
		mav.addObject("mblist",mb_list);

    	mav.addObject("sjhbxxDto", t_sjhbxxDto);
    	List<HbsfbzDto> sfbzlist=sfbzservice.viewByid(sjhbxxDto.getHbid());
    	mav.addObject("sfbzlist", sfbzlist);
		return mav;
    }
  
	/**
	 * 获取已选微信用户
	 * @param wxyhDto
	 * @return
	 */
	@RequestMapping(value = "/partner/pagedataWechatUser")
	@ResponseBody
	public Map<String, Object> getMemberList(WxyhDto wxyhDto){
		List<WxyhDto> wxyhDtos = wxyhservice.getListByIds(wxyhDto);
		Map<String, Object> result = new HashMap<>();
		result.put("wxyhDtos", wxyhDtos);
		return result;
	}
	
	/**
	 * 恢复删除伙伴界面
	 * @param sjhbxxDto
	 * @return
	 */
	@RequestMapping("/partner/resumeSjhbxx")
	@ResponseBody
	public ModelAndView resumeSjhbxxPage(SjhbxxDto sjhbxxDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/partner_Save");
		Map<String, List<JcsjDto>> jcsjlist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECT_TYPE, BasicDataTypeEnum.REPORT_TEMEPLATE, BasicDataTypeEnum.PROVINCE, BasicDataTypeEnum.CLASSIFY, BasicDataTypeEnum.STAMP_TYPE});
		SjhbxxDto sjhbxxDtos=sjhbservice.getDtoById(sjhbxxDto.getHbid());
		List<HbsfbzDto> sfbzlist=sfbzservice.selectByid(sjhbxxDto.getHbid());
		JcsjDto jcsjDto =new JcsjDto();
		jcsjDto.setFcsid(sjhbxxDtos.getSf());
		List<JcsjDto> citylist=jcsjService.jcsjcity(jcsjDto);
		sjhbxxDtos.setFormAction("resumeSaveSjhbxx");
		JcsjDto jcsj_sub=new JcsjDto();
		jcsj_sub.setFcsid(sjhbxxDtos.getFl());
		jcsj_sub.setJclb(BasicDataTypeEnum.SUBCLASSIFICATION.getCode());
		List<JcsjDto> subclassifylist=jcsjService.getJcsjDtoList(jcsj_sub);
		mav.addObject("sjhbxxDto",sjhbxxDtos);
		mav.addObject("sfbzlist", sfbzlist);
		mav.addObject("citylist", citylist);
		mav.addObject("subclassifylist", subclassifylist);
		mav.addObject("sflist",jcsjlist.get(BasicDataTypeEnum.PROVINCE.getCode()));
		mav.addObject("datectlist",jcsjlist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));
		mav.addObject("templatelist",jcsjlist.get(BasicDataTypeEnum.REPORT_TEMEPLATE.getCode()));
		mav.addObject("classifylist",jcsjlist.get(BasicDataTypeEnum.CLASSIFY.getCode()));
		mav.addObject("stampList",jcsjlist.get(BasicDataTypeEnum.STAMP_TYPE.getCode()));
		mav.addObject("xmList",JSONObject.toJSONString(redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECT_TYPE.getCode())));
		mav.addObject("zxmList",JSONObject.toJSONString(redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.DETECT_SUBTYPE.getCode())));
		return mav;
	}
	
	/**
	 * 恢复保存伙伴
	 * @param sjhbxxDto
	 * @return
	 */
	@RequestMapping("/partner/resumeSaveSjhbxx")
	@ResponseBody
	public Map<String,Object> resumeSjhb(SjhbxxDto sjhbxxDto){
		boolean isSuccess=false;
		Map<String, Object> map= new HashMap<>();
		int num=sjhbservice.getCountSjhb(sjhbxxDto);
    	if(num>0) {
    		map.put("status", isSuccess?"success":"fail");
    		map.put("message", isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():"送检合作伙伴已经存在，请核实后添加！");
    	}else {
    		isSuccess=sjhbservice.resumepartner(sjhbxxDto);
    		map.put("status",isSuccess?"success":"fail");
    		map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
    	}
		return map;
	}
	
    /**
	* 启用合作伙伴
     * @param sjhbxxDto
     * @return
     */
    @RequestMapping(value="/partner/enableSjhbxx")
    @ResponseBody
    public Map<String, Object> enableSjhbxx(SjhbxxDto sjhbxxDto){
    	User user=getLoginInfo();
    	sjhbxxDto.setXgry(user.getYhid());
    	boolean isSuccess=sjhbservice.enablepartner(sjhbxxDto);
    	Map<String, Object> map= new HashMap<>();
    	map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
    }
    /**
   	* 停用合作伙伴
    * @param sjhbxxDto
    * @return
    */
   @RequestMapping(value="/partner/disableSjhbxx")
   @ResponseBody
   public Map<String, Object> disableSjhbxx(SjhbxxDto sjhbxxDto){
   	User user=getLoginInfo();
   	sjhbxxDto.setXgry(user.getYhid());
   	boolean isSuccess=sjhbservice.disablepartner(sjhbxxDto);
   	Map<String, Object> map= new HashMap<>();
   	map.put("status",isSuccess?"success":"fail");
	map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
	return map;
   }

	/**
	 * 更新直销的cskz3为1
	 * @return
	 */
	@RequestMapping("/partner/oncopower")
	@ResponseBody
	public Map<String,Object> updateCskz3(){
   	   Map<String,Object> map=new HashMap<>();
   	   boolean isSuccess=sjhbservice.updateCskz3();
	   map.put("status",isSuccess?"success":"fail");
	   map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
	   return map;
   }
	/**
	 * 更新送检伙伴的大区信息
	 * @return
	 */
	@RequestMapping("/partner/maintenancedqxxSjhbxx")
	@ResponseBody
	public Map<String,Object> maintenancedqxxSjhbxx(){
		Map<String,Object> map=new HashMap<>();
		boolean isSuccess = false;
		String repeatHbxxStr = "";
		try {
			isSuccess = sjhbservice.updateDqxx();
		} catch (Exception e) {
			List<SjhbxxDto> repeatHbxx = sjhbservice.getRepeatHbxx();
			for (SjhbxxDto hbxx : repeatHbxx) {
				repeatHbxxStr += hbxx.getHbmc() +"("+hbxx.getZsxm() + "),";
			}
		}
		finally {
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr()+"以下送检伙伴可能重复绑定大区："+repeatHbxxStr);
			return map;
		}
   }

	/**
	 * 收费标准列表
	 * @param sjhbxxDto
	 * @return
	 */
	@RequestMapping(value="/partner/pageGetListSfbz")
	@ResponseBody
	public Map<String, Object> pageGetListSfbz(HttpServletRequest request,SjhbxxDto sjhbxxDto){
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.PLATFORM_OWNERSHIP, BasicDataTypeEnum.PROVINCE, BasicDataTypeEnum.DETECT_TYPE, BasicDataTypeEnum.DETECT_SUBTYPE});
		List<SjhbxxDto> sjhblist = sfbzservice.getPagedSfbzDtoList(sjhbxxDto);
		Map<String, Object> map= new HashMap<>();
		map.put("xsbmlist",jclist.get(BasicDataTypeEnum.PLATFORM_OWNERSHIP.getCode()));
		map.put("ptgslist",jclist.get(BasicDataTypeEnum.PLATFORM_OWNERSHIP_ORIGIN.getCode()));
		map.put("sflist",jclist.get(BasicDataTypeEnum.PROVINCE.getCode()));
		map.put("xmlist",jclist.get(BasicDataTypeEnum.DETECT_TYPE.getCode()));
		map.put("zxmlist",jclist.get(BasicDataTypeEnum.DETECT_SUBTYPE.getCode()));
		map.put("total", sjhbxxDto.getTotalNumber());
		map.put("rows",  sjhblist);
		super.setCzdmList(request,map);
		super.setTyszList(request,map);
		return map;
	}
	/**
	 * @Description: 个人消息订阅
	 * @param grszDto
	 * @param request
	 * @return org.springframework.web.servlet.ModelAndView
	 * @Author: 郭祥杰
	 * @Date: 2024/11/21 15:50
	 */
	@RequestMapping("/partner/setmessagePartner")
	public ModelAndView setmessagepartner(GrszDto grszDto,HttpServletRequest request) {
		ModelAndView mav=new ModelAndView("globalweb/message/subscribeMessage");
		Map<String, List<JcsjDto>> jclist =jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[]{BasicDataTypeEnum.MESSAGE_TYPE});
		List<JcsjDto> jcsjDtos = jclist.get(BasicDataTypeEnum.MESSAGE_TYPE.getCode());
		List<JcsjDto> yhJcsjDtos = new ArrayList<>();
		if(!CollectionUtils.isEmpty(jcsjDtos)){
			for (JcsjDto jcsjDto:jcsjDtos){
				if("HB".equals(jcsjDto.getCskz2())){
					mav.addObject("hbDto",jcsjDto);
				}else{
					yhJcsjDtos.add(jcsjDto);
				}
			}
		}
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setCsmc("审核订阅消息");
		yhJcsjDtos.add(jcsjDto);
		mav.addObject("yhJcsjDtos",yhJcsjDtos);
		Map<String,Object> map = grszService.queryMessage(grszDto);
		mav.addObject("hbList",map.get("sjhbxxDtos"));
		mav.addObject("dyxxMap",map.get("dyxxMap"));
		mav.addObject("hbJson",map.get("hbList"));
		mav.addObject("yhJson",map.get("yhList"));
		mav.addObject("zsxm",map.get("zsxm"));
		mav.addObject("yhid",grszDto.getYhid());
		mav.addObject("ids",map.get("hbid"));
		mav.addObject("formAction","/partner/partner/setmessageSavePartner");
		return mav;
	}

	/**
	 * @Description: 个人消息订阅保存
	 * @param grszDto
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/25 15:39
	 */
	@RequestMapping("/partner/setmessageSavePartner")
	@ResponseBody
	public Map<String,Object> setmessageSavePartner(GrszDto grszDto) {
		Map<String,Object> map = new HashMap<>();
		try {
			boolean isSuccess = grszService.modSaveMessage(grszDto);
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		}catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00002").getXxnr());
		}
		return  map;
	}

	@Autowired
	IHbxxzService hbxxzService;
	/**
	 * 伙伴X限制清单
	 * @return
	 */
	@RequestMapping(value="/partnerX/pageListPartnerX")
	public ModelAndView pageListPartnerX() {
		ModelAndView mav=new ModelAndView("wechat/sjxx/x_partner_List");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclb(new BasicDataTypeEnum[]{BasicDataTypeEnum.DETECTION_UNIT});
		mav.addObject("jcdwList",jclist.get(BasicDataTypeEnum.DETECTION_UNIT.getCode()));//检测单位
		return mav;
	}


	/**
	 * 伙伴X限制列表
	 * @param hbxxzDto
	 * @return
	 */
	@RequestMapping(value="/partnerX/pageGetListPartnerX")
	@ResponseBody
	public Map<String, Object> pageGetListPartnerX(HbxxzDto hbxxzDto){
		List<HbxxzDto> hbxxzList=hbxxzService.getPagedDtoList(hbxxzDto);
		Map<String, Object> map= new HashMap<>();
		map.put("total", hbxxzDto.getTotalNumber());
		map.put("rows",  hbxxzList);
		return map;
	}
	/**
	 * 查询伙伴
	 * @param sjhbxxDto
	 * @return
	 */
	@RequestMapping(value="/partnerX/pagedataHbList")
	@ResponseBody
	public Map<String, Object> pagedataHbList(SjhbxxDto sjhbxxDto){
		List<SjhbxxDto> hbList=sjhbservice.getDtoList(sjhbxxDto);
		Map<String, Object> map= new HashMap<>();
		map.put("hbList",  hbList);
		return map;
	}

	/**
	 * 新增伙伴X限制页面
	 * @return
	 */
	@RequestMapping(value="/partnerX/addPartner")
	public ModelAndView addXPartner() {
		ModelAndView mav = new ModelAndView("wechat/sjxx/x_partner_Save");
		HbxxzDto hbxxzDto = new HbxxzDto();
		hbxxzDto.setFormAction("addSavePartner");
		mav.addObject("hbxxzDto", hbxxzDto);
		mav.addObject("jcdwList", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		SjhbxxDto sjhbxxDto = new SjhbxxDto();
		List<SjhbxxDto> hbList = sjhbservice.getDtoList(sjhbxxDto);
		mav.addObject("hbList", hbList);
		return mav;
	}

	/**
	 * 执行添加操作
	 * @param hbxxzDto
	 * @return
	 */
	@RequestMapping(value="/partnerX/addSavePartner")
	@ResponseBody
	public Map<String, Object> addSaveXPartner(HbxxzDto hbxxzDto){
		Map<String, Object> map= new HashMap<>();
		HbxxzDto dto = hbxxzService.getDto(hbxxzDto);
		if (dto != null) {
			map.put("status", "fail");
			map.put("message", "该X限制已存在");
			return map;
		}
		User loginInfo = getLoginInfo();
		hbxxzDto.setLrry(loginInfo.getYhid());
		hbxxzDto.setHbxxzid(StringUtil.generateUUID());
		boolean insert = hbxxzService.insertDto(hbxxzDto);
		//发送rabbit同步到85端
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.HBXXZ_ADD.getCode() + JSONObject.toJSONString(hbxxzDto));
		map.put("status", insert?"success":"fail");
		map.put("message", insert?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 修改页面
	 * @param hbxxzDto
	 * @return
	 */
	@RequestMapping(value="/partnerX/modPartner")
	public ModelAndView modXPartner(HbxxzDto hbxxzDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/x_partner_Save");
		HbxxzDto dto = hbxxzService.getDto(hbxxzDto);
		dto.setFormAction("modSavePartner");
		mav.addObject("hbxxzDto", dto);
		mav.addObject("jcdwList", redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode()));
		SjhbxxDto sjhbxxDto = new SjhbxxDto();
		List<SjhbxxDto> hbList = sjhbservice.getDtoList(sjhbxxDto);
		mav.addObject("hbList", hbList);
		return mav;
	}


	/**
	 * 执行修改操作
	 * @param hbxxzDto
	 * @return
	 */
	@RequestMapping(value="/partnerX/modSavePartner")
	@ResponseBody
	public Map<String, Object> modSavePartner(HbxxzDto hbxxzDto){
		Map<String, Object> map= new HashMap<>();
		HbxxzDto dto = hbxxzService.getSameDto(hbxxzDto);
		if (dto != null) {
			map.put("status", "fail");
			map.put("message", "该X限制已存在");
			return map;
		}
		User loginInfo = getLoginInfo();
		hbxxzDto.setXgry(loginInfo.getYhid());
		boolean insert = hbxxzService.updateDto(hbxxzDto);
		//发送rabbit同步到85端
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.HBXXZ_MOD.getCode() + JSONObject.toJSONString(hbxxzDto));
		map.put("status", insert?"success":"fail");
		map.put("message", insert?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 执行修改操作
	 * @param hbxxzDto
	 * @return
	 */
	@RequestMapping(value="/partnerX/delPartner")
	@ResponseBody
	public Map<String, Object> delSavePartner(HbxxzDto hbxxzDto){
		Map<String, Object> map= new HashMap<>();
		boolean delete = hbxxzService.delete(hbxxzDto);
		//发送rabbit同步到85端
		RabbitUtils.convertAndSendUniqueMsg("wechat.exchange", MQTypeEnum.OPERATE_INSPECT.getCode(), RabbitEnum.HBXXZ_DEL.getCode() + JSONObject.toJSONString(hbxxzDto));
		map.put("status", delete?"success":"fail");
		map.put("message", delete?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	@RequestMapping(value = "/partnerX/viewPartner")
	@ResponseBody
	public ModelAndView viewPartner(HbxxzDto hbxxzDto) {
		ModelAndView mav=new ModelAndView("wechat/sjxx/x_partner_View");
		HbxxzDto dto = hbxxzService.getDto(hbxxzDto);
		mav.addObject("hbxxzDto", dto);
		return mav;
	}
}
