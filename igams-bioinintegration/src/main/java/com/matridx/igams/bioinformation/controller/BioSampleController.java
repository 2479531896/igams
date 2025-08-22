package com.matridx.igams.bioinformation.controller;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.bioinformation.dao.entities.CnvjgDto;
import com.matridx.igams.bioinformation.dao.entities.CnvjgxqDto;
import com.matridx.igams.bioinformation.dao.entities.OtherDto;
import com.matridx.igams.bioinformation.service.svcinterface.ICnvjgService;
import com.matridx.igams.bioinformation.service.svcinterface.ICnvjgxqService;
import com.matridx.igams.bioinformation.service.svcinterface.IOtherService;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/sample")
public class BioSampleController extends BaseController{
	@Autowired
	private ICnvjgService cnvjgService;
	@Autowired
	private ICnvjgxqService cnvjgxqService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	private IOtherService otherService;
	/**
	 * 获取CNV结果列表
	 */
	@RequestMapping("/cnv/pagedataCnvJg")
	@ResponseBody
	public Map<String, Object> pagedataCnvJg(CnvjgDto cnvjgDto) {
		Map<String, Object> map = new HashMap<>();
		User user=getLoginInfo();
		List<Map<String,String>> dwlist=otherService.getJsjcdwByjsid(user.getDqjs());
		List<String> jcdwList=new ArrayList<>();
		List<String> hbmcList=new ArrayList<>();
		if(dwlist!=null&&dwlist.size() > 0){
			if("1".equals(dwlist.get(0).get("dwxdbj"))) {//判断当前角色是否进行了单位限制
				jcdwList= new ArrayList<>();
				for (Map<String, String> stringStringMap : dwlist) {
					if (stringStringMap.get("jcdw") != null) {
						jcdwList.add(stringStringMap.get("jcdw"));
					}
				}
				//判断伙伴权限
				List<String> hbqxList = otherService.getHbidByYhid(user.getYhid());
				if(hbqxList!=null && hbqxList.size()>0) {
					hbmcList=otherService.getHbmcByHbid(hbqxList);
				}
			}
		}
		cnvjgDto.setJcdws(jcdwList);
		cnvjgDto.setSjhbs(hbmcList);
		Object oncoxtsz=redisUtil.hget("matridx_xtsz","onco.report.flag");
		if(oncoxtsz!=null){
			JSONObject job=JSONObject.parseObject(String.valueOf(oncoxtsz));
			if("1".equals(job.getString("szz"))){
				cnvjgDto.setOncoqx("1");
			}
		}
		List<CnvjgDto> list = cnvjgService.getPagedDtoList(cnvjgDto);
		map.put("total", cnvjgDto.getTotalNumber());
		map.put("rows", list);
		map.put("zlpdlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TUMOR_JUDGEMENT.getCode()));
		map.put("mxyclist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MODEL_PREDICTION.getCode()));
		map.put("zljglist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TUMOR_RESULT.getCode()));
		map.put("pdyylist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.JUDGE_REASON.getCode()));
		return map;
	}


	/**
	 * 获取CNV权限
	 */
	@RequestMapping("/cnv/pagedataCnvPurview")
	@ResponseBody
	public Map<String, Object> pagedataCnvPurview(String zyid) {
		Map<String, Object> map = new HashMap<>();
		User user=getLoginInfo();
		OtherDto otherDto_t=new OtherDto();
		otherDto_t.setJsid(user.getDqjs());
		otherDto_t.setZyid(zyid);
		List<OtherDto> jszyczb = otherService.getJszyczb(otherDto_t);
		map.put("audit","false");
		map.put("batchaudit","false");
		for(OtherDto dto:jszyczb){
			if("audit".equals(dto.getCzdm())){
				map.put("audit","true");
			}else if("batchaudit".equals(dto.getCzdm())){
				map.put("batchaudit","true");
			}
		}
		return map;
	}

	/**
	 * 获取CNV--Today列表
	 */
	@RequestMapping("/cnv/pagedataTodayList")
	@ResponseBody
	public Map<String, Object> getTodayList(CnvjgDto cnvjgDto) {
		Map<String, Object> map;
		User user=getLoginInfo();
		map = cnvjgService.getTodayList(user,cnvjgDto);
		return map;
	}

	/**
	 * CNV结果列表查看
	 */
	@RequestMapping("/cnv/pagedataViewCnv")
	@ResponseBody
	public Map<String, Object> viewCnv(CnvjgDto cnvjgDto) {
		Map<String, Object> map = new HashMap<>();
		CnvjgDto cnvjgDto_t = cnvjgService.getDtoById(cnvjgDto.getCnvjgid());
		CnvjgxqDto cnvjgxqDto=new CnvjgxqDto();
		cnvjgxqDto.setCnvjgid(cnvjgDto.getCnvjgid());
		List<CnvjgxqDto> list = cnvjgxqService.getListById(cnvjgxqDto);
		StringBuilder ysycb= new StringBuilder();
		if(list!=null&&list.size()>0){
			for(CnvjgxqDto dto:list){
				if("0".equals(dto.getSfqh())){
					if(StringUtil.isNotBlank(dto.getQswz())&&StringUtil.isNotBlank(dto.getZzwz())){
						BigDecimal qswz=new BigDecimal(dto.getQswz());
						BigDecimal zzwz=new BigDecimal(dto.getZzwz());
						BigDecimal xqqswz=new BigDecimal(dto.getXqqswz());
						BigDecimal xqzzwz=new BigDecimal(dto.getXqzzwz());
						BigDecimal qj = zzwz.subtract(qswz);
						BigDecimal half=new BigDecimal("0.5");
						if(xqqswz.compareTo(qswz) < 0){
							if(xqzzwz.compareTo(qswz)>=0&&xqzzwz.compareTo(zzwz)<=0){
								BigDecimal subtract = xqzzwz.subtract(qswz);
								BigDecimal divide = subtract.divide(qj, RoundingMode.HALF_DOWN);
								if(divide.compareTo(half) > 0){
									ysycb.append(",").append(dto.getMc());
								}
							}else if(xqzzwz.compareTo(zzwz) > 0){
								ysycb.append(",").append(dto.getMc());
							}
						}else if(xqqswz.compareTo(qswz)>=0&&xqqswz.compareTo(zzwz)<=0){
							if(xqzzwz.compareTo(qswz)>=0&&xqzzwz.compareTo(zzwz)<=0){
								BigDecimal subtract = xqzzwz.subtract(xqqswz);
								BigDecimal divide = subtract.divide(qj, RoundingMode.HALF_DOWN);
								if(divide.compareTo(half) > 0){
									ysycb.append(",").append(dto.getMc());
								}
							}else if(xqzzwz.compareTo(zzwz) > 0){
								BigDecimal subtract = zzwz.subtract(xqqswz);
								BigDecimal divide = subtract.divide(qj, RoundingMode.HALF_DOWN);
								if(divide.compareTo(half) > 0){
									ysycb.append(",").append(dto.getMc());
								}
							}
						}
					}
				}
			}
		}
		if(ysycb.length()>0){
			ysycb = new StringBuilder(ysycb.substring(1));
		}
		cnvjgDto_t.setYsycb(ysycb.toString());
		List<CnvjgDto> list1= cnvjgService.getCnvjgByNbbm(cnvjgDto);
		map.put("otherSize",list1.size());
		map.put("cnvjgDto",cnvjgDto_t);
		map.put("zlpdlist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TUMOR_JUDGEMENT.getCode()));
		map.put("mxyclist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.MODEL_PREDICTION.getCode()));
		map.put("zljglist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TUMOR_RESULT.getCode()));
		map.put("pdyylist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.JUDGE_REASON.getCode()));
		List<CnvjgxqDto> cnvjgxqDtos = cnvjgxqService.getDtoList(cnvjgxqDto);
		for(CnvjgxqDto cnvjgxqDto1:cnvjgxqDtos){
			List<CnvjgxqDto>cnvAjyList=cnvjgxqService.getAjyList(cnvjgxqDto1);
			cnvjgxqDto1.setAjyList(cnvAjyList);
		}

		map.put("cnvjgxqDtos",cnvjgxqDtos);
		//获取审核人和检验人列表
		Map<String, Object> userInfo = otherService.getReviewUserInfo(new OtherDto());
		map.put("userInfo", userInfo);
        return map;
	}

	/**
	 * 根据内部编码查询除当前样本外其他cnv文库
	 */
	@RequestMapping("/cnv/pagedataOtherCnvByNbbm")
	@ResponseBody
	public Map<String,Object> getOtherCnvByNbbm(CnvjgDto cnvjgDto){

		Map<String,Object> map=new HashMap<>();
		List<CnvjgDto> list= cnvjgService.getCnvjgByNbbm(cnvjgDto);
		map.put("otherList",list);
		return map;
	}
	/**
	 * 新增CNV明细
	 */
	@RequestMapping("/cnv/pagedataAddSaveCnvDetail")
	@ResponseBody
	public Map<String, Object> addSaveCnvDetail(CnvjgxqDto cnvjgxqDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		cnvjgxqDto.setLrry(user.getYhid());
		boolean isSuccess = cnvjgxqService.addSaveCnvDetail(cnvjgxqDto);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	/**
	 * 修改CNV审核信息
	 */
	@RequestMapping("/cnv/pagedataModSaveCnv")
	@ResponseBody
	public Map<String, Object> modSaveCnv(CnvjgDto cnvjgDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		cnvjgDto.setXgry(user.getYhid());
		boolean isSuccess = cnvjgService.modSaveCnv(cnvjgDto,user);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 批量修改CNV审核信息
	 */
	@RequestMapping("/cnv/pagedataBatchModSaveCnv")
	@ResponseBody
	public Map<String, Object> batchModSaveCnv(CnvjgDto cnvjgDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		cnvjgDto.setXgry(user.getYhid());
		boolean isSuccess = cnvjgService.batchModSaveCnv(cnvjgDto,user);
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?(xxglService.getModelById("ICOM00001").getXxnr()):xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}


	/**
	 * cnv实验室列表
	 */
	@RequestMapping("/cnv/pagedataLaboratoryList")
	@ResponseBody
	public Map<String, Object> pagedataLaboratoryList(CnvjgDto cnvjgDto) {
		Map<String, Object> map = new HashMap<>();
		User user=getLoginInfo();
		List<Map<String,String>> dwlist=otherService.getJsjcdwByjsid(user.getDqjs());
		List<String> jcdwList=new ArrayList<>();
		if(dwlist!=null&&dwlist.size() > 0){
			if("1".equals(dwlist.get(0).get("dwxdbj"))) {//判断当前角色是否进行了单位限制
				jcdwList= new ArrayList<>();
				for (Map<String, String> stringStringMap : dwlist) {
					if (stringStringMap.get("jcdw") != null) {
						jcdwList.add(stringStringMap.get("jcdw"));
					}
				}
			}
		}
		cnvjgDto.setJcdws(jcdwList);
		cnvjgDto.setJclb(BasicDataTypeEnum.DETECTION_UNIT.getCode());
		List<CnvjgDto> list = cnvjgService.getPagedLaboratoryList(cnvjgDto);
		map.put("total", cnvjgDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * cnv实验室列表查看
	 */
	@RequestMapping("/cnv/pagedataLaboratoryView")
	@ResponseBody
	public Map<String, Object> pagedataLaboratoryView(CnvjgDto cnvjgDto) {
		Map<String, Object> map = new HashMap<>();
		if(StringUtil.isNotBlank(cnvjgDto.getFilter())){
			SimpleDateFormat dayDeal = new SimpleDateFormat("yyyy-MM-dd");
			cnvjgDto.setCxkssjstart(dayDeal.format(DateUtils.getDate(new Date(), -1)) + " 16:00:00");
			cnvjgDto.setCxkssjend(dayDeal.format(DateUtils.getDate(new Date(), 0)) + " 16:00:00");
		}
        User user=getLoginInfo();
        List<Map<String,String>> dwlist=otherService.getJsjcdwByjsid(user.getDqjs());
        List<String> jcdwList=new ArrayList<>();
        List<String> hbmcList=new ArrayList<>();
		if(dwlist!=null&&dwlist.size() > 0){
			if("1".equals(dwlist.get(0).get("dwxdbj"))) {//判断当前角色是否进行了单位限制
				jcdwList= new ArrayList<>();
				for (Map<String, String> stringStringMap : dwlist) {
					if (stringStringMap.get("jcdw") != null) {
						jcdwList.add(stringStringMap.get("jcdw"));
					}
				}
				//判断伙伴权限
				List<String> hbqxList = otherService.getHbidByYhid(user.getYhid());
				if(hbqxList!=null && hbqxList.size()>0) {
					hbmcList=otherService.getHbmcByHbid(hbqxList);
				}
			}
		}

        cnvjgDto.setJcdws(jcdwList);
        cnvjgDto.setSjhbs(hbmcList);
        Object oncoxtsz=redisUtil.hget("matridx_xtsz","onco.report.flag");
        if(oncoxtsz!=null){
            JSONObject job=JSONObject.parseObject(String.valueOf(oncoxtsz));
            if("1".equals(job.getString("szz"))){
                cnvjgDto.setOncoqx("1");
            }
        }
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
						List<CnvjgxqDto>cnvAjyList;
						cnvAjyList=cnvjgxqService.getAjyList(cnvjgxqDto_t);
						cnvjgxqDto_t.setAjyList(cnvAjyList);
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
		//获取审核人和检验人列表
		Map<String, Object> userInfo = otherService.getReviewUserInfo(new OtherDto());
		map.put("userInfo", userInfo);
		return map;
	}
}
