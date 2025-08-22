package com.matridx.igams.bioinformation.controller;

import com.matridx.igams.bioinformation.dao.entities.*;
import com.matridx.igams.bioinformation.service.svcinterface.ICnvjgxqService;
import com.matridx.igams.bioinformation.service.svcinterface.IOtherService;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.bioinformation.service.svcinterface.ICnvjgService;
import com.matridx.igams.bioinformation.service.svcinterface.IBioXpxxService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
//import com.matridx.igams.wechat.dao.entities.SjxxDto;
//import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
//import com.matridx.igams.wechat.service.svcinterface.ISjhbxxService;
//import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/chip")
public class ChipController extends BaseController{
	@Autowired
	private IBioXpxxService xpxxService;
	@Autowired
	private ICnvjgService cnvjgService;
	@Autowired
	private IJcsjService jcsjService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	private IOtherService otherService;
	@Autowired
	private ICnvjgxqService cnvjgxqService;


	/**
	 * 获取芯片列表
	 */
	@RequestMapping("/chip/pagedataChip")
	@ResponseBody
	public Map<String, Object> pagedataChip(BioXpxxDto bioXpxxDto) {
		Map<String, Object> map = new HashMap<>();
		List<BioXpxxDto> list = xpxxService.getPagedDtoList(bioXpxxDto);
		map.put("total", bioXpxxDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 芯片列表查看
	 */
	@RequestMapping("/chip/pagedataViewChip")
	@ResponseBody
	public Map<String, Object> viewChip(CnvjgDto cnvjgDto) {
		Map<String, Object> map = new HashMap<>();
		List<CnvjgDto> list = cnvjgService.getDtoList(cnvjgDto);
		if(list!=null&&list.size()>0){
			List<String> cnvjgids=new ArrayList<>();
			for(CnvjgDto cnvjgDto_t:list){
				cnvjgids.add(cnvjgDto_t.getCnvjgid());
			}
			CnvjgxqDto cnvjgxqDto=new CnvjgxqDto();
			cnvjgxqDto.setCnvjgids(cnvjgids);
			List<CnvjgxqDto> cnvjgxqDtos = cnvjgxqService.getDtoList(cnvjgxqDto);
			for(CnvjgDto dto:list){
				List<CnvjgxqDto> mxDtos = new ArrayList<>();
				for(CnvjgxqDto cnvjgxqDto_t:cnvjgxqDtos){
					if(dto.getCnvjgid().equals(cnvjgxqDto_t.getCnvjgid())){
						mxDtos.add(cnvjgxqDto_t);
					}
				}
				dto.setCnvjgxqDtos(mxDtos);
			}
		}
		map.put("list", list);
		map.put("zlpdlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TUMOR_JUDGEMENT.getCode()));
		map.put("mxyclist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MODEL_PREDICTION.getCode()));
		map.put("zljglist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TUMOR_RESULT.getCode()));
		map.put("pdyylist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.JUDGE_REASON.getCode()));
		return map;
	}
	/**
	 * 生信审核系统芯片列表
	 */
	@RequestMapping("/chip/pagedataBioChip")
	@ResponseBody
	public Map<String, Object> pagedataBioChip(BioXpxxDto bioXpxxDto) {
		Map<String, Object> map = new HashMap<>();
		Set<String> sjids=new HashSet<>();
		OtherDto sjxxDto =new OtherDto();
		User user=getLoginInfo();
		List<OtherDto> sjxxlist;
		List<Map<String,String>> jcdwList=otherService.getJsjcdwByjsid(user.getDqjs());
		if("1".equals(bioXpxxDto.getSjqxsx())) {
			List<String> userids= new ArrayList<>();
			if(user.getYhid()!=null) {
				userids.add(user.getYhid());
			}
			if(user.getDdid()!=null) {
				userids.add(user.getDdid());
			}
			if(user.getWechatid()!=null) {
				userids.add(user.getWechatid());
			}
			if(userids.size()>0) {
				sjxxDto.setUserids(userids);
				bioXpxxDto.setUserids(userids);

			}

			//判断伙伴权限
			List<String> hbqxList = otherService.getHbidByYhid(user.getYhid());
			if(hbqxList!=null && hbqxList.size()>0) {
				List<String> hbmcList=otherService.getHbmcByHbid(hbqxList);
				if(hbmcList!=null  && hbmcList.size()>0) {
					sjxxDto.setSjhbs(hbmcList);
				}
			}
			if(jcdwList!=null&&jcdwList.size() > 0){
				if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
					List<String> strList= new ArrayList<>();
					for (Map<String, String> stringStringMap : jcdwList) {
						if (stringStringMap.get("jcdw") != null) {
							strList.add(stringStringMap.get("jcdw"));
						}
					}
					if(strList.size()>0) {
						sjxxDto.setJcdwxz(strList);
						Map<String,Object> params = otherService.pareMapFromDto(sjxxDto);
						sjxxlist=otherService.getDtoListOptimize(params);
					}else {
						sjxxlist= new ArrayList<>();
					}
				}else {
					Map<String,Object> params = otherService.pareMapFromDto(sjxxDto);
					sjxxlist=otherService.getDtoListOptimize(params);
				}
			}else {
				Map<String,Object> params = otherService.pareMapFromDto(sjxxDto);
				sjxxlist=otherService.getDtoListOptimize(params);
			}
		for (OtherDto dto : sjxxlist) {
			if(StringUtil.isNotBlank(dto.getSjid())){
				sjids.add(dto.getSjid());
			}
		}
		bioXpxxDto.setSjids(sjids);}
		List<BioXpxxDto> list = xpxxService.getPagedBioChipList(bioXpxxDto);
		List<JcsjDto> detection_unit= redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());//得到实验室列表
		List<JcsjDto> sequencer_list= redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SEQUENCER_CODE.getCode());//得到测序仪
		map.put("total", bioXpxxDto.getTotalNumber());
		map.put("rows", list);
		map.put("detection_unit",detection_unit);
		map.put("sequencer_list", sequencer_list);
		return map;
	}
	/**
	 * 今日芯片列表芯片列表
	 */
	@RequestMapping("/chip/pagedataTodayChip")
	@ResponseBody
	public Map<String, Object> pagedataTodayChip(BioXpxxDto bioXpxxDto) {
		Map<String, Object> map = new HashMap<>();
		Set<String> sjids=new HashSet<>();
		OtherDto sjxxDto =new OtherDto();
		User user=getLoginInfo();
		List<OtherDto> sjxxlist;
		List<Map<String,String>> jcdwList=otherService.getJsjcdwByjsid(user.getDqjs());
		if("1".equals(bioXpxxDto.getSjqxsx())) {
			List<String> userids= new ArrayList<>();
			if(user.getYhid()!=null) {
				userids.add(user.getYhid());
			}
			if(user.getDdid()!=null) {
				userids.add(user.getDdid());
			}
			if(user.getWechatid()!=null) {
				userids.add(user.getWechatid());
			}
			if(userids.size()>0) {
				sjxxDto.setUserids(userids);
				bioXpxxDto.setUserids(userids);

			}

			//判断伙伴权限
			List<String> hbqxList = otherService.getHbidByYhid(user.getYhid());
			if(hbqxList!=null && hbqxList.size()>0) {
				List<String> hbmcList=otherService.getHbmcByHbid(hbqxList);
				if(hbmcList!=null  && hbmcList.size()>0) {
					sjxxDto.setSjhbs(hbmcList);
				}
			}
			if(jcdwList!=null&&jcdwList.size() > 0){
				if("1".equals(jcdwList.get(0).get("dwxdbj"))) {
					List<String> strList= new ArrayList<>();
					for (Map<String, String> stringStringMap : jcdwList) {
						if (stringStringMap.get("jcdw") != null) {
							strList.add(stringStringMap.get("jcdw"));
						}
					}
					if(strList.size()>0) {
						sjxxDto.setJcdwxz(strList);
						Map<String,Object> params = otherService.pareMapFromDto(sjxxDto);
						sjxxlist=otherService.getDtoListOptimize(params);
					}else {
						sjxxlist= new ArrayList<>();
					}
				}else {
					Map<String,Object> params = otherService.pareMapFromDto(sjxxDto);
					sjxxlist=otherService.getDtoListOptimize(params);
				}
			}else{
				Map<String,Object> params = otherService.pareMapFromDto(sjxxDto);
				sjxxlist=otherService.getDtoListOptimize(params);
			}

			for (OtherDto dto : sjxxlist) {
				if(StringUtil.isNotBlank(dto.getSjid())){
					sjids.add(dto.getSjid());
				}
			}
			bioXpxxDto.setSjids(sjids);}
		List<BioXpxxDto> list = xpxxService.getDtoTodayChipList(bioXpxxDto);
		map.put("total", bioXpxxDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}
}
