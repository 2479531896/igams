package com.matridx.igams.common.controller;

import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.QualityComplaintsEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/complaints")
public class QualityComplaintsController extends BaseController{

	@Autowired
	private IZltsglService zltsglService;
	@Autowired
	private ITsclqkService tsclqkService;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	private ISjycService sjycService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IFjcfbService fjcfbService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}

	/**
	 * 获取质量投诉列表
	 */
	@RequestMapping("/complaints/pageGetListQualityComplaints")
	@ResponseBody
	public Map<String, Object> pageGetListQualityComplaints(ZltsglDto zltsglDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		super.setCzdmList(request,map);
		super.setTyszList(request,map);
		List<ZltsglDto> list = zltsglService.getPagedDtoList(zltsglDto);
		map.put("lylist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COMPLAINT_SOURCE.getCode()));
		map.put("total", zltsglDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}

	/**
	 * 查看质量投诉
	 */
	@RequestMapping("/complaints/viewQualityComplaints")
	@ResponseBody
	public Map<String, Object> viewQualityComplaints(ZltsglDto zltsglDto) {
		Map<String, Object> map = new HashMap<>();
		ZltsglDto zltsglDto_t = zltsglService.getDto(zltsglDto);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(zltsglDto.getZltsid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_QUALITY_COMPLAINTS.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
		TsclqkDto tsclqkDto=new TsclqkDto();
		tsclqkDto.setZltsid(zltsglDto.getZltsid());
		List<TsclqkDto> dtoList = tsclqkService.getDtoList(tsclqkDto);
		if(dtoList!=null&&!dtoList.isEmpty()){
			List<String> ywids=new ArrayList<>();
			for(TsclqkDto dto:dtoList){
				ywids.add(dto.getTsclqkid());
			}
			FjcfbDto fj = new FjcfbDto();
			fj.setYwlx(BusTypeEnum.IMP_COMPLAINTS_HANDLING_SITUATION.getCode());
			fj.setYwids(ywids);
			List<FjcfbDto> fjcfbDtoList = fjcfbService.selectFjcfbDtoByYwidsAndYwlx(fj);
			if(fjcfbDtoList!=null&&!fjcfbDtoList.isEmpty()){
				for(TsclqkDto dto:dtoList){
					List<FjcfbDto> fjcfbDtos_t=new ArrayList<>();
					for(FjcfbDto fjcfbDto_t:fjcfbDtoList){
						if(dto.getTsclqkid().equals(fjcfbDto_t.getYwid())){
							fjcfbDtos_t.add(fjcfbDto_t);
						}
					}
					dto.setFjcfbDtos(fjcfbDtos_t);
				}
			}
		}
		map.put("fjcfbDtos",fjcfbDtos);
		map.put("zltsglDto", zltsglDto_t);
		map.put("tsclqkDtos", dtoList);
		return map;
	}

	/**
	 * 投诉待处理清单(部门)
	 * @return
	 */
	@RequestMapping("/complaints/pageGetListQualityComplaintsByBm")
	@ResponseBody
	public Map<String,Object> pageGetListQualityComplaintsByBm(TsclqkDto tsclqkDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		User user=getLoginInfo(request);
		tsclqkDto.setYhid(user.getYhid());
		List<TsclqkDto> tsclqkDtoList=tsclqkService.getPagedListByBm(tsclqkDto);
		super.setCzdmList(request,map);
		super.setTyszList(request,map);
		map.put("total", tsclqkDto.getTotalNumber());
		map.put("rows", tsclqkDtoList);
		return map;
	}
	/**
	 * 新增质量投诉
	 */
	@RequestMapping("/complaints/addQualityComplaints")
	@ResponseBody
	public Map<String, Object> addQualityComplaints(ZltsglDto zltsglDto) {
		Map<String, Object> map = new HashMap<>();
		List<ZltsglDto> xmlist=new ArrayList<>();

		if(StringUtil.isNotBlank(zltsglDto.getYwlx())){
			if(QualityComplaintsEnum.COMPLAINTS_EXCEPTION.getCode().equals(zltsglDto.getYwlx())){
				SjycDto sjycDto=new SjycDto();
				sjycDto.setYcid(zltsglDto.getYwid());
				SjycDto dto = sjycService.getDto(sjycDto);
				if(dto!=null){
					zltsglDto.setNbbm(dto.getNbbm());
					zltsglDto.setHzxm(dto.getHzxm());
					zltsglDto.setNl(dto.getNl());
					zltsglDto.setYblxmc(dto.getYblxmc());
					zltsglDto.setJcdwmc(dto.getJcdwmc());
					zltsglDto.setSjid(dto.getSjid());
					xmlist=zltsglService.getItemSelectionInfo(zltsglDto);
				}
			}else if(QualityComplaintsEnum.COMPLAINTS_INSPECTION.getCode().equals(zltsglDto.getYwlx())){
				ZltsglDto dto = zltsglService.getInspectionInfo(zltsglDto);
				if(dto!=null){
					zltsglDto.setNbbm(dto.getNbbm());
					zltsglDto.setHzxm(dto.getHzxm());
					zltsglDto.setNl(dto.getNl());
					zltsglDto.setYblxmc(dto.getYblxmc());
					zltsglDto.setJcdwmc(dto.getJcdwmc());
					zltsglDto.setSjid(dto.getSjid());
					xmlist=zltsglService.getItemSelectionInfo(zltsglDto);
				}
			}
		}
		map.put("zltsglDto", zltsglDto);
		map.put("lylist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COMPLAINT_SOURCE.getCode()));
		map.put("xmlist",xmlist);
		map.put("ywlx", BusTypeEnum.IMP_QUALITY_COMPLAINTS.getCode());
		return map;
	}

	/**
	 * 新增保存质量投诉
	 */
	@RequestMapping("/complaints/addSaveQualityComplaints")
	@ResponseBody
	public Map<String, Object> addSaveQualityComplaints(ZltsglDto zltsglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		zltsglDto.setLrry(user.getYhid());
		boolean isSuccess = zltsglService.addSaveQualityComplaints(zltsglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 修改质量投诉
	 */
	@RequestMapping("/complaints/modQualityComplaints")
	@ResponseBody
	public Map<String, Object> modQualityComplaints(ZltsglDto zltsglDto) {
		Map<String, Object> map = new HashMap<>();
		ZltsglDto zltsglDto_t = zltsglService.getDto(zltsglDto);
		TsclqkDto tsclqkDto=new TsclqkDto();
		tsclqkDto.setZltsid(zltsglDto.getZltsid());
		List<TsclqkDto> dtoList = tsclqkService.getDtoList(tsclqkDto);
		if(dtoList!=null&&!dtoList.isEmpty()){
			StringBuilder bm_str= new StringBuilder();
			for(TsclqkDto dto:dtoList){
				bm_str.append(",").append(dto.getBmid()).append("-").append(dto.getBmmc()).append("(").append(dto.getWbcxmc()).append(")");
			}
			if(StringUtil.isNotBlank(bm_str.toString())){
				zltsglDto_t.setBm_str(bm_str.substring(1));
			}
		}
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(zltsglDto.getZltsid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_QUALITY_COMPLAINTS.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
		zltsglDto.setSjid(zltsglDto_t.getSjid());
		List<ZltsglDto> xmlist=zltsglService.getItemSelectionInfo(zltsglDto);
		map.put("fjcfbDtos",fjcfbDtos);
		map.put("zltsglDto", zltsglDto_t);
		map.put("lylist",redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.COMPLAINT_SOURCE.getCode()));
		map.put("xmlist",xmlist);
		map.put("ywlx", BusTypeEnum.IMP_QUALITY_COMPLAINTS.getCode());
		return map;
	}

	/**
	 * 修改保存质量投诉
	 */
	@RequestMapping("/complaints/modSaveQualityComplaints")
	@ResponseBody
	public Map<String, Object> modSaveQualityComplaints(ZltsglDto zltsglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		zltsglDto.setXgry(user.getYhid());
		boolean isSuccess = zltsglService.modSaveQualityComplaints(zltsglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 删除保存质量投诉
	 */
	@RequestMapping("/complaints/delQualityComplaints")
	@ResponseBody
	public Map<String, Object> delQualityComplaints(ZltsglDto zltsglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		zltsglDto.setScry(user.getYhid());
		boolean isSuccess = zltsglService.delQualityComplaints(zltsglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 处理质量投诉
	 */
	@RequestMapping("/complaints/disposalQualityComplaints")
	@ResponseBody
	public Map<String, Object> disposalQualityComplaints(TsclqkDto tsclqkDto) {
		Map<String, Object> map = new HashMap<>();
		ZltsglDto zltsglDto=new ZltsglDto();
		zltsglDto.setZltsid(tsclqkDto.getZltsid());
		ZltsglDto zltsglDto_t = zltsglService.getDto(zltsglDto);
		TsclqkDto tsclqkDto_t = tsclqkService.getDto(tsclqkDto);
		FjcfbDto fjcfbDto=new FjcfbDto();
		fjcfbDto.setYwid(tsclqkDto.getTsclqkid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_COMPLAINTS_HANDLING_SITUATION.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
		map.put("fjcfbDtos",fjcfbDtos);
		map.put("zltsglDto", zltsglDto_t);
		map.put("tsclqkDto", tsclqkDto_t);
		map.put("ywlx", BusTypeEnum.IMP_COMPLAINTS_HANDLING_SITUATION.getCode());
		return map;
	}

	/**
	 * 处理保存质量投诉
	 */
	@RequestMapping("/complaints/disposalSaveQualityComplaints")
	@ResponseBody
	public Map<String, Object> disposalSaveQualityComplaints(TsclqkDto tsclqkDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		tsclqkDto.setClry(user.getYhid());
		boolean isSuccess = tsclqkService.disposalSaveQualityComplaints(tsclqkDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}

	/**
	 * 质量投诉结论
	 */
	@RequestMapping("/complaints/concludeQualityComplaints")
	@ResponseBody
	public Map<String, Object> concludeQualityComplaints(ZltsglDto zltsglDto) {
		Map<String, Object> map = new HashMap<>();
		ZltsglDto zltsglDto_t = zltsglService.getDto(zltsglDto);
		TsclqkDto tsclqkDto=new TsclqkDto();
		tsclqkDto.setZltsid(zltsglDto.getZltsid());
		List<TsclqkDto> departmentList = tsclqkService.getDepartmentList(tsclqkDto);
		List<TsclqkDto> dtoList = tsclqkService.getDtoList(tsclqkDto);
		map.put("zltsglDto", zltsglDto_t);
		map.put("departmentList", departmentList);
		map.put("dtoList", dtoList);
		return map;
	}

	/**
	 * 质量投诉结论保存
	 */
	@RequestMapping("/complaints/concludeSaveQualityComplaints")
	@ResponseBody
	public Map<String, Object> concludeSaveQualityComplaints(ZltsglDto zltsglDto,HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo(request);
		zltsglDto.setXgry(user.getYhid());
		boolean isSuccess = zltsglService.concludeSaveQualityComplaints(zltsglDto);
		map.put("status", isSuccess ? "success" : "fail");
		map.put("message", isSuccess ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
}
