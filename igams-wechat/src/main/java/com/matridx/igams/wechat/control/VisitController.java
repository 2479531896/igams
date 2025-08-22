package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.ISjycService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.*;
import com.matridx.igams.wechat.service.svcinterface.*;
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
@RequestMapping("/visit")
public class VisitController extends BaseController{
	@Autowired
	private IBfglService bfglService;
	@Autowired
	private IBfdxglService bfdxglService;
	@Autowired
	private IXxglService xxglservice;
	@Autowired
	private IBfdxlxrglService bfdxlxrglService;
	@Autowired
	private IKhzyfpService khzyfpService;
	@Autowired
	private IBfplszService bfplszService;
	@Autowired
	ISjxxService sjxxService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	ISjycService sjycService;
	@Autowired
	IXtszService xtszService;
	@Autowired
	IYhqtxxService yhqtxxService;
	/**
	 * 拜访对象列表
	 */
	@RequestMapping("/visit/pageGetListVisitingObject")
	@ResponseBody
	public Map<String,Object> pageGetListVisitingObject(BfdxglDto bfdxglDto, HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		super.setCzdmList(request,map);
		super.setTyszList(request,map);
		List<BfdxglDto> bfdxglList=bfdxglService.getPagedDtoList(bfdxglDto);
		map.put("visit_classify", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_CLASSIFY.getCode()));
		map.put("visit_type", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_TYPE.getCode()));
		map.put("visit_grade", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_GRADE.getCode()));
		map.put("visit_province",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.PROVINCE.getCode()));
		map.put("visit_source",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITCUSTOMER_SOURCE.getCode()));
		map.put("total", bfdxglDto.getTotalNumber());
		map.put("rows", bfdxglList);
		return map;
	}


	/**
	 * 查看拜访对象
	 */
	@RequestMapping("/visit/viewVisitingObject")
	@ResponseBody
	public Map<String,Object> viewVisitingObject(BfdxglDto bfdxglDto){
		Map<String,Object> map= new HashMap<>();
		BfdxglDto dto = bfdxglService.getDtoById(bfdxglDto.getDwid());
		BfdxlxrglDto bfdxlxrglDto=new BfdxlxrglDto();
		bfdxlxrglDto.setDwid(bfdxglDto.getDwid());
		List<BfdxlxrglDto> bfdxlxrglDtos = bfdxlxrglService.getDtoList(bfdxlxrglDto);
		KhzyfpDto khzyfpDto=new KhzyfpDto();
		khzyfpDto.setDwid(bfdxglDto.getDwid());
		List<KhzyfpDto> khzyfpDtos = khzyfpService.getDtoList(khzyfpDto);
		BfglDto bfglDto=new BfglDto();
		bfglDto.setDwid(bfdxglDto.getDwid());
		List<BfglDto> bfglDtos = bfglService.getDtoList(bfglDto);
		List<Map<String,String>> list_khkz=new ArrayList<>();
		XtszDto xtszDto=new XtszDto();
		xtszDto.setSzlb("crm.customer.extend");
		xtszDto=xtszService.getDto(xtszDto);
		if(xtszDto!=null){
			Map<String,String> khkzmap=(Map<String,String>)JSON.parse(xtszDto.getSzz());
			for (Map.Entry<String, String> entry : khkzmap.entrySet()) {
				Map<String,String> t_map=new HashMap<>();
				t_map.put("key",entry.getKey());
				t_map.put("value",entry.getValue());
				list_khkz.add(t_map);
				if(dto!=null&&StringUtil.isNotBlank(dto.getKzzd())){
					Map<String,String> kzzdmap=(Map<String,String>)JSON.parse(dto.getKzzd());
					for(Map.Entry<String, String> t_entry : kzzdmap.entrySet()){
						if(entry.getKey().equals(t_entry.getKey())){
							t_map.put("value",t_entry.getValue());
						}
					}
				}
			}
		}
		map.put("khzyextend",list_khkz);
		map.put("bfdxglDto", dto);
		map.put("bfdxlxrglDtos", bfdxlxrglDtos);
		map.put("khzyfpDtos", khzyfpDtos);
		map.put("bfglDtos", bfglDtos);
		return map;
	}

	/**
	 * 新增拜访对象
	 */
	@RequestMapping("/visit/addVisitingObject")
	@ResponseBody
	public Map<String,Object> addVisitingObject(BfdxglDto bfdxglDto){
		Map<String,Object> map= new HashMap<>();
		List<Map<String,String>> list_khkz=new ArrayList<>();
		XtszDto xtszDto=new XtszDto();
		xtszDto.setSzlb("crm.customer.extend");
		xtszDto=xtszService.getDto(xtszDto);
		if(xtszDto!=null){
			Map<String,String> khkzmap=(Map<String,String>)JSON.parse(xtszDto.getSzz());
			for (Map.Entry<String, String> entry : khkzmap.entrySet()) {
				Map<String,String> t_map=new HashMap<>();
				t_map.put("key",entry.getKey());
				t_map.put("value",entry.getValue());
				list_khkz.add(t_map);
			}
		}
		map.put("khzyextend",list_khkz);
		map.put("visit_classify", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_CLASSIFY.getCode()));
		map.put("visit_type", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_TYPE.getCode()));
		map.put("visit_grade", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_GRADE.getCode()));
		map.put("visit_province",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.PROVINCE.getCode()));
		map.put("visit_source",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITCUSTOMER_SOURCE.getCode()));
		map.put("visit_rating",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_RATING.getCode()));
		return map;
	}

	public String dealZdkz(BfdxglDto bfdxglDto){
		String kzzd=bfdxglDto.getKzzd();
		String result="";
		if(StringUtil.isNotBlank(kzzd)){
			List<Map<String,String>> maplist=(List<Map<String, String>>) JSONArray.parse(kzzd);
			for(Map<String,String> map:maplist){
				result=result+",\""+map.get("key")+"\":"+"\""+map.get("value")+"\"";
			}
			return "{"+result.substring(1)+"}";
		}else{
			return null;
		}
	}
	/**
	 * 新增保存拜访对象
	 */
	@RequestMapping("/visit/addSaveVisitingObject")
	@ResponseBody
	public Map<String,Object> addSaveVisitingObject(BfdxglDto bfdxglDto){
		Map<String,Object> map= new HashMap<>();
		bfdxglDto.setKzzd(dealZdkz(bfdxglDto));
		User user=getLoginInfo();
		bfdxglDto.setLrry(user.getYhid());
		bfdxglDto.setYwy(user.getYhid());
		//校验客户名称
		List<BfdxglDto> bfdxglDto1=bfdxglService.getDtoByDwmc(bfdxglDto);
		if(!CollectionUtils.isEmpty(bfdxglDto1)){
			map.put("status","fail");
			map.put("message","已存在该客户资源，不允许重复添加，若未找到请联系相关人员分配该客户资源！");
			return map;
		}
		boolean iSsuccess=bfdxglService.addSaveVisitingObject(bfdxglDto);
		map.put("status",iSsuccess?"success":"fail");
		map.put("message",iSsuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 修改拜访对象
	 */
	@RequestMapping("/visit/modVisitingObject")
	@ResponseBody
	public Map<String,Object> modVisitingObject(BfdxglDto bfdxglDto){
		Map<String,Object> map= new HashMap<>();
		BfdxglDto dto = bfdxglService.getDto(bfdxglDto);
		BfdxlxrglDto bfdxlxrglDto=new BfdxlxrglDto();
		bfdxlxrglDto.setDwid(bfdxglDto.getDwid());
		List<BfdxlxrglDto> bfdxlxrglDtos = bfdxlxrglService.getDtoList(bfdxlxrglDto);
		List<Map<String,String>> list_khkz=new ArrayList<>();
		XtszDto xtszDto=new XtszDto();
		xtszDto.setSzlb("crm.customer.extend");
		xtszDto=xtszService.getDto(xtszDto);
		if(xtszDto!=null){
			Map<String,String> khkzmap=(Map<String,String>)JSON.parse(xtszDto.getSzz());
			for (Map.Entry<String, String> entry : khkzmap.entrySet()) {
				Map<String,String> t_map=new HashMap<>();
				t_map.put("key",entry.getKey());
				t_map.put("value",entry.getValue());
				list_khkz.add(t_map);
				if(dto!=null&&StringUtil.isNotBlank(dto.getKzzd())){
					Map<String,String> kzzdmap=(Map<String,String>)JSON.parse(dto.getKzzd());
					for(Map.Entry<String, String> t_entry : kzzdmap.entrySet()){
						if(entry.getKey().equals(t_entry.getKey())){
							t_map.put("value",t_entry.getValue());
						}
					}
				}
			}
		}
		map.put("khzyextend",list_khkz);
		map.put("visit_classify", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_CLASSIFY.getCode()));
		map.put("visit_type", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_TYPE.getCode()));
		map.put("visit_grade", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_GRADE.getCode()));
		map.put("visit_province",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.PROVINCE.getCode()));
		map.put("visit_source",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITCUSTOMER_SOURCE.getCode()));
		map.put("visit_rating",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_RATING.getCode()));
		map.put("bfdxglDto", dto);
		map.put("bfdxlxrglDtos", bfdxlxrglDtos);
		return map;
	}

	/**
	 * 修改保存拜访对象
	 */
	@RequestMapping("/visit/modSaveVisitingObject")
	@ResponseBody
	public Map<String,Object> modSaveVisitingObject(BfdxglDto bfdxglDto){
		Map<String,Object> map= new HashMap<>();
		bfdxglDto.setKzzd(dealZdkz(bfdxglDto));
		User user=getLoginInfo();
		bfdxglDto.setXgry(user.getYhid());
		//校验客户名称
		List<BfdxglDto> bfdxglDto1=bfdxglService.getDtoByDwmc(bfdxglDto);
		if(!CollectionUtils.isEmpty(bfdxglDto1)){
			map.put("status","fail");
			map.put("message","该客户名称已存在，不允许修改");
			return map;
		}
		boolean iSsuccess=bfdxglService.modSaveVisitingObject(bfdxglDto);
		map.put("status",iSsuccess?"success":"fail");
		map.put("message",iSsuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 删除拜访对象
	 */
	@RequestMapping("/visit/delVisitingObject")
	@ResponseBody
	public Map<String,Object> delVisitingObject(BfdxglDto bfdxglDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		bfdxglDto.setScry(user.getYhid());
		boolean iSsuccess=bfdxglService.delVisitingObject(bfdxglDto);
		map.put("status",iSsuccess?"success":"fail");
		map.put("message",iSsuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 认可拜访对象
	 */
	@RequestMapping("/visit/approveVisitingObject")
	@ResponseBody
	public Map<String,Object> approveVisitingObject(BfdxglDto bfdxglDto){
		Map<String,Object> map= new HashMap<>();
		BfdxglDto dto = bfdxglService.getDto(bfdxglDto);
		dto.setDxbm(bfdxglService.generateCode(dto));
		BfdxlxrglDto bfdxlxrglDto=new BfdxlxrglDto();
		bfdxlxrglDto.setDwid(bfdxglDto.getDwid());
		List<BfdxlxrglDto> bfdxlxrglDtos = bfdxlxrglService.getDtoList(bfdxlxrglDto);
		map.put("visit_classify", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_CLASSIFY.getCode()));
		map.put("visit_type", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_TYPE.getCode()));
		map.put("visit_grade", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_GRADE.getCode()));
		map.put("visit_province",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.PROVINCE.getCode()));
		map.put("visit_source",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITCUSTOMER_SOURCE.getCode()));
		map.put("bfdxglDto", dto);
		map.put("bfdxlxrglDtos", bfdxlxrglDtos);
		return map;
	}

	/**
	 * 认可保存拜访对象
	 */
	@RequestMapping("/visit/approveSaveVisitingObject")
	@ResponseBody
	public Map<String,Object> approveSaveVisitingObject(BfdxglDto bfdxglDto){
		bfdxglDto.setSfrk("1");
		return this.modSaveVisitingObject(bfdxglDto);
	}

	/**
	 * 合并
	 */
	@RequestMapping("/visit/mergeVisitingObject")
	@ResponseBody
	public Map<String,Object> mergeVisitingObject(BfdxglDto bfdxglDto){
		Map<String,Object> map= new HashMap<>();
		List<BfdxglDto> bfdxglDtos = bfdxglService.getDtoList(bfdxglDto);
		map.put("bfdxglDtos",bfdxglDtos);
		BfdxlxrglDto bfdxlxrglDto=new BfdxlxrglDto();
		bfdxlxrglDto.setIds(bfdxglDto.getIds());
		List<BfdxlxrglDto> bfdxlxrglDtos = bfdxlxrglService.getDtoList(bfdxlxrglDto);
		map.put("bfdxlxrglDtos",bfdxlxrglDtos);
		KhzyfpDto khzyfpDto=new KhzyfpDto();
		khzyfpDto.setIds(bfdxglDto.getIds());
		List<KhzyfpDto> khzyfpDtos = khzyfpService.getDtoList(khzyfpDto);
		map.put("khzyfpDtos",khzyfpDtos);
		return map;
	}

	/**
	 * 合并保存
	 */
	@RequestMapping("/visit/mergeSaveVisitingObject")
	@ResponseBody
	public Map<String,Object> mergeSaveVisitingObject(BfdxglDto bfdxglDto){
		Map<String,Object> map= new HashMap<>();
		boolean iSsuccess = bfdxglService.mergeSaveVisitingObject(bfdxglDto);
		map.put("status",iSsuccess?"success":"fail");
		map.put("message",iSsuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 权限复制
	 */
	@RequestMapping("/visit/copypermissionsVisitingObject")
	@ResponseBody
	public Map<String,Object> copyPermissionsVisitingObject(KhzyfpDto khzyfpDto){
		Map<String,Object> map= new HashMap<>();
		List<KhzyfpDto> khzyfpDtos = khzyfpService.getDtoList(khzyfpDto);
		map.put("khzyfpDtos",khzyfpDtos);
		return map;
	}

	/**
	 * 权限复制保存
	 */
	@RequestMapping("/visit/copypermissionsSaveVisitingObject")
	@ResponseBody
	public Map<String,Object> copyPermissionsSaveVisitingObject(KhzyfpDto khzyfpDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		List<KhzyfpDto> khzyfpDtos=(List<KhzyfpDto>) JSON.parseArray(khzyfpDto.getZyfp_json(), KhzyfpDto.class);
		for(KhzyfpDto dto:khzyfpDtos){
			dto.setZyfpid(StringUtil.generateUUID());
			dto.setLrry(user.getYhid());
		}
		boolean iSsuccess = khzyfpService.insertList(khzyfpDtos);
		map.put("status",iSsuccess?"success":"fail");
		map.put("message",iSsuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 模糊查找客户信息
	 */
	@RequestMapping("/visit/pagedataGetVisitingObject")
	@ResponseBody
	public Map<String,Object> pagedataGetVisitingObject(BfdxglDto bfdxglDto){
		Map<String,Object> map= new HashMap<>();
		List<BfdxglDto> bfdxglDtos = bfdxglService.getDtoList(bfdxglDto);
		map.put("bfdxglDtos",bfdxglDtos);
		return map;
	}

	/**
	 * 资源分配
	 */
	@RequestMapping("/visit/resourcedispenseVisitingObject")
	@ResponseBody
	public Map<String,Object> resourceDispenseVisitingObject(KhzyfpDto khzyfpDto){
		Map<String,Object> map= new HashMap<>();
		List<KhzyfpDto> khzyfpDtos = khzyfpService.getDtoList(khzyfpDto);
		map.put("khzyfpDtos",khzyfpDtos);
		return map;
	}
	/**
	 * 资源分配
	 */
	@RequestMapping("/visit/resourcedispenseSaveVisitingObject")
	@ResponseBody
	public Map<String,Object> resourceDispenseSaveVisitingObject(KhzyfpDto khzyfpDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		khzyfpDto.setScry(user.getYhid());
		boolean iSsuccess = khzyfpService.resourceDispenseSaveVisitingObject(khzyfpDto);
		map.put("status",iSsuccess?"success":"fail");
		map.put("message",iSsuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}


	/**
	 * 拜访清单
	 */
	@RequestMapping("/visit/pageGetListVisitRecords")
	@ResponseBody
	public Map<String,Object> pageGetListVisitRecords(BfglDto bfglDto, HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		super.setCzdmList(request,map);
		super.setTyszList(request,map);
		List<BfglDto> list=bfglService.getPagedDtoList(bfglDto);
		map.put("visit_classify", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_CLASSIFY.getCode()));
		map.put("visitrecords_type", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITRECORDS_TYPE.getCode()));
		map.put("visitrecords_distinguish", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITRECORDS_DISTINGUISH.getCode()));
		map.put("total", bfglDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 新增拜访记录
	 */
	@RequestMapping("/visit/addVisitRecord")
	@ResponseBody
	public Map<String,Object> addVisitRecord(){
		Map<String,Object> map= new HashMap<>();
		map.put("visitrecords_type", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITRECORDS_TYPE.getCode()));
		map.put("visitrecords_distinguish", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITRECORDS_DISTINGUISH.getCode()));
		map.put("ywlx", BusTypeEnum.IMP_VISIT_RECORDS.getCode());
		return map;
	}

	/**
	 * 修改拜访记录
	 */
	@RequestMapping("/visit/modVisitRecord")
	@ResponseBody
	public Map<String,Object> modVisitRecord(BfglDto bfglDto){
		Map<String,Object> map= new HashMap<>();
		BfglDto dto = bfglService.getDto(bfglDto);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_VISIT_RECORDS.getCode());
		fjcfbDto.setYwid(bfglDto.getBfid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		map.put("bfglDto",dto);
		map.put("fjcfbDtos",fjcfbDtos);
		map.put("visitrecords_type", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITRECORDS_TYPE.getCode()));
		map.put("visitrecords_distinguish", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITRECORDS_DISTINGUISH.getCode()));
		map.put("ywlx", BusTypeEnum.IMP_VISIT_RECORDS.getCode());
		return map;
	}

	/**
	 * 查看拜访记录
	 */
	@RequestMapping("/visit/viewVisitRecord")
	@ResponseBody
	public Map<String,Object> viewVisitRecord(BfglDto bfglDto){
		Map<String,Object> map= new HashMap<>();
		BfglDto dto = bfglService.getDto(bfglDto);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwlx(BusTypeEnum.IMP_VISIT_RECORDS.getCode());
		fjcfbDto.setYwid(bfglDto.getBfid());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		map.put("bfglDto",dto);
		map.put("fjcfbDtos",fjcfbDtos);
		return map;
	}

	/**
	 * 删除拜访记录
	 */
	@RequestMapping("/visit/delVisitRecord")
	@ResponseBody
	public Map<String,Object> delVisitRecord(BfglDto bfglDto){
		Map<String,Object> map= new HashMap<>();
		User user = getLoginInfo();
		bfglDto.setScry(user.getYhid());
		boolean iSsuccess = bfglService.batchDelete(bfglDto);
		map.put("status",iSsuccess?"success":"fail");
		map.put("message",iSsuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 拜访联系人清单
	 */
	@RequestMapping("/visit/pageGetListContacts")
	@ResponseBody
	public Map<String,Object> pageGetListContacts(BfdxlxrglDto bfdxlxrglDto,HttpServletRequest request){
		Map<String,Object> map= new HashMap<>();
		super.setCzdmList(request,map);
		super.setTyszList(request,map);
		List<BfdxlxrglDto> list=bfdxlxrglService.getPagedDtoList(bfdxlxrglDto);
		map.put("total", bfdxlxrglDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 查看拜访联系人
	 */
	@RequestMapping("/visit/viewContact")
	@ResponseBody
	public Map<String,Object> viewContact(BfdxlxrglDto bfdxlxrglDto){
		Map<String,Object> map= new HashMap<>();
		BfdxlxrglDto bfdxlxrglDto_t=bfdxlxrglService.getDto(bfdxlxrglDto);
		map.put("bfdxlxrglDto", bfdxlxrglDto_t);
		return map;
	}

	/**
	 * 钉钉-待拜访列表
	 */
	@RequestMapping("/visit/pagedataToBeVisitedList")
	@ResponseBody
	public Map<String,Object> pagedataToBeVisitedList(BfplszDto bfplszDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		bfplszDto.setYhid(user.getYhid());
		List<BfplszDto> list = bfplszService.getPagedToBeVisitedList(bfplszDto);
		map.put("bfdwfls", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_CLASSIFY.getCode()));
		map.put("khfjs", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITUNIT_RATING.getCode()));
		map.put("rows", list);
		return map;
	}

	/**
	 * 钉钉-客户列表
	 */
	@RequestMapping("/visit/pagedataCustomerList")
	@ResponseBody
	public Map<String,Object> pagedataCustomerList(KhzyfpDto khzyfpDto){
		Map<String,Object> map= new HashMap<>();
		User user = getLoginInfo();
		List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(!CollectionUtils.isEmpty(dwAndbjlist)) {
			if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
				khzyfpDto.setDwxzbj("1");
				List<String> xjlist=yhqtxxService.getXjyhList(user.getYhid());
				xjlist.add(user.getYhid());
				khzyfpDto.setYhlist(xjlist);
			}
		}
		List<KhzyfpDto> list = khzyfpService.getPagedCustomerList(khzyfpDto);
		map.put("rows", list);
		return map;
	}

	/**
	 * 钉钉-拜访记录列表
	 */
	@RequestMapping("/visit/pagedataVisitRecordsList")
	@ResponseBody
	public Map<String,Object> pagedataVisitRecordsList(KhzyfpDto khzyfpDto){
		Map<String,Object> map= new HashMap<>();
		User user = getLoginInfo();
		khzyfpDto.setYhid(user.getYhid());
		List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(!CollectionUtils.isEmpty(dwAndbjlist)) {
			if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
				khzyfpDto.setDwxzbj("1");
				List<String> xjlist=yhqtxxService.getXjyhList(user.getYhid());
				xjlist.add(user.getYhid());
				khzyfpDto.setYhlist(xjlist);
			}
		}
		List<KhzyfpDto> list = khzyfpService.getPagedVisitRecordsList(khzyfpDto);
		map.put("rows", list);
		return map;
	}

	/**
	 * 钉钉-查看客户
	 */
	@RequestMapping("/visit/pagedataViewCustomer")
	@ResponseBody
	public Map<String,Object> pagedataViewCustomer(BfdxglDto bfdxglDto){
		Map<String,Object> map= new HashMap<>();
		BfdxglDto dto = bfdxglService.getDto(bfdxglDto);
		map.put("bfdxglDto", dto);
		BfdxlxrglDto bfdxlxrglDto=new BfdxlxrglDto();
		bfdxlxrglDto.setDwid(bfdxglDto.getDwid());
		List<BfdxlxrglDto> bfdxlxrglDtos = bfdxlxrglService.getDtoList(bfdxlxrglDto);
		map.put("bfdxlxrglDtos", bfdxlxrglDtos);
		BfglDto bfglDto=new BfglDto();
		bfglDto.setDwid(bfdxglDto.getDwid());
		User user = getLoginInfo();
		bfglDto.setYhid(user.getYhid());
		List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(!CollectionUtils.isEmpty(dwAndbjlist)) {
			if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {//如果是单位限制，则只能查看自己的拜访记录
				bfglDto.setBfr(user.getYhid());
			}
		}
		List<BfglDto> bfglDtos = bfglService.getDtoList(bfglDto);
		List<Map<String,String>> list_khkz=new ArrayList<>();
		XtszDto xtszDto=new XtszDto();
		xtszDto.setSzlb("crm.customer.extend");
		xtszDto=xtszService.getDto(xtszDto);
		if(xtszDto!=null){
			Map<String,String> khkzmap=(Map<String,String>)JSON.parse(xtszDto.getSzz());
			for (Map.Entry<String, String> entry : khkzmap.entrySet()) {
				Map<String,String> t_map=new HashMap<>();
				t_map.put("key",entry.getKey());
				t_map.put("value",entry.getValue());
				list_khkz.add(t_map);
				if(dto!=null&&StringUtil.isNotBlank(dto.getKzzd())){
					Map<String,String> kzzdmap=(Map<String,String>)JSON.parse(dto.getKzzd());
					for(Map.Entry<String, String> t_entry : kzzdmap.entrySet()){
						if(entry.getKey().equals(t_entry.getKey())){
							t_map.put("value",t_entry.getValue());
						}
					}
				}
			}
		}
		map.put("khzyextend",list_khkz);
		map.put("bfglDtos", bfglDtos);
		return map;
	}

	/**
	 * 钉钉-查看拜访记录
	 */
	@RequestMapping("/visit/pagedataViewVisitRecords")
	@ResponseBody
	public Map<String,Object> pagedataViewVisitRecords(BfglDto bfglDto){
		return this.viewVisitRecord(bfglDto);
	}

	/**
	 * 钉钉-删除拜访记录
	 */
	@RequestMapping("/visit/pagedataDelVisitRecords")
	@ResponseBody
	public Map<String,Object> pagedataDelVisitRecords(BfglDto bfglDto){
		Map<String,Object> map= new HashMap<>();
		User user = getLoginInfo();
		bfglDto.setScry(user.getYhid());
		boolean iSsuccess = bfglService.delVisitRecord(bfglDto);
		map.put("status",iSsuccess?"success":"fail");
		map.put("message",iSsuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 钉钉-客户新增
	 */
	@RequestMapping("/visit/pagedataAddVisitingObject")
	@ResponseBody
	public Map<String,Object> pagedataAddVisitingObject(BfdxglDto bfdxglDto){
		return this.addVisitingObject(bfdxglDto);
	}

	/**
	 * 钉钉-客户新增保存
	 */
	@RequestMapping("/visit/pagedataAddSaveVisitingObject")
	@ResponseBody
	public Map<String,Object> pagedataAddSaveVisitingObject(BfdxglDto bfdxglDto){
		return this.addSaveVisitingObject(bfdxglDto);
	}

	/**
	 * 钉钉-客户修改
	 */
	@RequestMapping("/visit/pagedataModVisitingObject")
	@ResponseBody
	public Map<String,Object> pagedataModVisitingObject(BfdxglDto bfdxglDto){
		return this.modVisitingObject(bfdxglDto);
	}

	/**
	 * 钉钉-客户修改保存
	 */
	@RequestMapping("/visit/pagedataModSaveVisitingObject")
	@ResponseBody
	public Map<String,Object> pagedataModSaveVisitingObject(BfdxglDto bfdxglDto){
		return this.modSaveVisitingObject(bfdxglDto);
	}

	/**
	 * 钉钉-删除客户
	 */
	@RequestMapping("/visit/pagedataDelVisitingObject")
	@ResponseBody
	public Map<String,Object> pagedataDelVisitingObject(BfdxglDto bfdxglDto){
		return this.delVisitingObject(bfdxglDto);
	}

	/**
	 * 钉钉-频率设置初始化
	 */
	@RequestMapping("/visit/pagedataInitFrequencySetting")
	@ResponseBody
	public Map<String,Object> pagedataInitFrequencySetting(BfplszDto bfplszDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		bfplszDto.setYhid(user.getYhid());
		BfplszDto dto = bfplszService.getDto(bfplszDto);
		map.put("frequency_type", redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.FREQUENCY_TYPE.getCode()));
		map.put("bfplszDto", dto);
		return map;
	}

	/**
	 * 钉钉-频率设置保存
	 */
	@RequestMapping("/visit/pagedataSaveFrequencySetting")
	@ResponseBody
	public Map<String,Object> pagedataSaveFrequencySetting(BfplszDto bfplszDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		bfplszDto.setYhid(user.getYhid());
		boolean iSsuccess=bfplszService.SaveFrequencySetting(bfplszDto);
		map.put("status",iSsuccess?"success":"fail");
		map.put("message",iSsuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 钉钉-频率设置删除
	 */
	@RequestMapping("/visit/pagedataDelFrequencySetting")
	@ResponseBody
	public Map<String,Object> pagedataDelFrequencySetting(BfplszDto bfplszDto){
		Map<String,Object> map= new HashMap<>();
		boolean iSsuccess=bfplszService.delete(bfplszDto);
		map.put("status",iSsuccess?"success":"fail");
		map.put("message",iSsuccess?xxglservice.getModelById("ICOM00003").getXxnr():xxglservice.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 钉钉-拜访记录初始化
	 */
	@RequestMapping("/visit/pagedataInitVisitRecord")
	@ResponseBody
	public Map<String,Object> pagedataInitVisitRecord(BfglDto bfglDto){
		Map<String,Object> map= new HashMap<>();
		List<FjcfbDto> fjcfbDtos =new ArrayList<>();
		if(StringUtil.isNotBlank(bfglDto.getBfid())){
			bfglDto = bfglService.getDto(bfglDto);
			FjcfbDto fjcfbDto=new FjcfbDto();
			fjcfbDto.setYwlx(BusTypeEnum.IMP_VISIT_RECORDS.getCode());
			fjcfbDto.setYwid(bfglDto.getBfid());
			fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		}
		map.put("visitrecords_type",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITRECORDS_TYPE.getCode()));
		map.put("visitrecords_distinguish",redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.VISITRECORDS_DISTINGUISH.getCode()));
		map.put("ywlx", BusTypeEnum.IMP_VISIT_RECORDS.getCode());
		map.put("bfglDto",bfglDto);
		map.put("fjcfbDtos",fjcfbDtos);
		return map;
	}

	/**
	 * 钉钉-拜访记录保存
	 */
	@RequestMapping("/visit/pagedataSaveVisitRecord")
	@ResponseBody
	public Map<String,Object> pagedataSaveVisitRecord(BfglDto bfglDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		bfglDto.setBfr(user.getYhid());
		boolean iSsuccess=bfglService.saveVisitRecord(bfglDto);
		map.put("status",iSsuccess?"success":"fail");
		map.put("message",iSsuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 钉钉-客户列表
	 */
	@RequestMapping("/visit/pagedataVisitingObjectList")
	@ResponseBody
	public Map<String,Object> pagedataVisitingObjectList(BfdxglDto bfdxglDto, HttpServletRequest request){
		return this.pageGetListVisitingObject(bfdxglDto,request);
	}

	/**
	 * 钉钉-联系人列表
	 */
	@RequestMapping("/visit/pagedataContactsList")
	@ResponseBody
	public Map<String,Object> pagedataContactsList(BfdxlxrglDto bfdxlxrglDto){
		Map<String,Object> map= new HashMap<>();
		List<BfdxlxrglDto> list = bfdxlxrglService.getPagedDtoList(bfdxlxrglDto);
		map.put("total", bfdxlxrglDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 钉钉-联系人新增保存
	 */
	@RequestMapping("/visit/pagedataAddSaveContact")
	@ResponseBody
	public Map<String,Object> pagedataAddSaveContact(BfdxlxrglDto bfdxlxrglDto){
		Map<String,Object> map= new HashMap<>();
		User user=getLoginInfo();
		bfdxlxrglDto.setLxrid(StringUtil.generateUUID());
		bfdxlxrglDto.setLrry(user.getYhid());
		List<BfdxlxrglDto> list=new ArrayList<>();
		list.add(bfdxlxrglDto);
		boolean iSsuccess = bfdxlxrglService.insertList(list);
		map.put("status",iSsuccess?"success":"fail");
		map.put("message",iSsuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 钉钉-销售统计
	 */
	@RequestMapping("/visit/pagedataGetStatisticsByBfr")
	@ResponseBody
	public Map<String,Object> pagedataGetStatisticsByBfr(BfglDto bfglDto){
		Map<String,Object> map= new HashMap<>();
		User user = getLoginInfo();
		bfglDto.setYhid(user.getYhid());
		List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(!CollectionUtils.isEmpty(dwAndbjlist)) {
			if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
				bfglDto.setDwxzbj("1");
			}
		}
		List<BfglDto> list = bfglService.getStatisticsListByBfr(bfglDto);
		if(!list.isEmpty()){
			int count=Integer.parseInt(list.get(0).getCount());
			list.get(0).setRatio("100");
			for(int i=1;i<list.size();i++){
				list.get(i).setRatio(String.valueOf(Integer.parseInt(list.get(i).getCount())*100/count));
			}
		}
		map.put("rows", list);
		return map;
	}

	/**
	 * 钉钉-销售统计
	 */
	@RequestMapping("/visit/pagedataGetStatisticsByDw")
	@ResponseBody
	public Map<String,Object> pagedataGetStatisticsByDw(BfglDto bfglDto){
		Map<String,Object> map= new HashMap<>();
		User user = getLoginInfo();
		bfglDto.setYhid(user.getYhid());
		List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(!CollectionUtils.isEmpty(dwAndbjlist)) {
			if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
				bfglDto.setDwxzbj("1");
			}
		}
		List<BfglDto> list = bfglService.getStatisticsListByDw(bfglDto);
		if(!list.isEmpty()){
			int count=Integer.parseInt(list.get(0).getCount());
			list.get(0).setRatio("100");
			for(int i=1;i<list.size();i++){
				list.get(i).setRatio(String.valueOf(Integer.parseInt(list.get(i).getCount())*100/count));
			}
		}
		map.put("rows", list);
		return map;
	}

	/**
	 * 钉钉-查看统计数据详情
	 */
	@RequestMapping("/visit/pagedataViewStatisticList")
	@ResponseBody
	public Map<String,Object> pagedataViewStatisticList(BfglDto bfglDto){
		Map<String,Object> map= new HashMap<>();
		User user = getLoginInfo();
		bfglDto.setYhid(user.getYhid());
		List<Map<String, String>> dwAndbjlist = sjxxService.getJsjcdwByjsid(user.getDqjs());
		if(!CollectionUtils.isEmpty(dwAndbjlist)) {
			if("1".equals(dwAndbjlist.get(0).get("dwxdbj"))) {
				bfglDto.setDwxzbj("1");
			}
		}
		List<BfglDto> list = bfglService.viewStatisticList(bfglDto);
		map.put("rows", list);
		return map;
	}

	/**
	 * 拜访评论
	 * @param sjycDto
	 * @return
	 */
	@RequestMapping("/visit/pagedataCommentVisit")
	@ResponseBody
	public Map<String,Object> pagedataCommentVisit(SjycDto sjycDto){
		Map<String,Object> map=new HashMap<>();
		User user=getLoginInfo();
		sjycDto.setYcid(StringUtil.generateUUID());
		sjycDto.setLrry(user.getYhid());
		boolean iSsuccess=sjycService.insert(sjycDto);
		map.put("status",iSsuccess?"success":"fail");
		map.put("ycid",sjycDto.getYcid());
		map.put("message",iSsuccess?xxglservice.getModelById("ICOM00001").getXxnr():xxglservice.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
