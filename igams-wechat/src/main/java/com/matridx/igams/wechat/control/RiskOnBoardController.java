package com.matridx.igams.wechat.control;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.FxsjglDto;
import com.matridx.igams.wechat.dao.entities.SjsyglDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.service.svcinterface.IFxsjglService;
import com.matridx.igams.wechat.service.svcinterface.ISjsyglService;
import com.matridx.igams.wechat.service.svcinterface.ISjxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/risk")
public class RiskOnBoardController extends BaseController {


	@Autowired
	private IFjcfbService fjcfbService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private IXxglService xxglService;
	@Autowired
	private ISjxxService sjxxService;
	@Autowired
	private IFxsjglService fxsjglService;
	@Autowired
	private ISjsyglService sjsyglService;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	/**
	 * 风险上机列表
	 * @param
	 * @return
	 */
	@RequestMapping("/board/pageListBoard")
	public ModelAndView pageListBoard(FxsjglDto fxsjglDto) {
		ModelAndView mav = new ModelAndView("wechat/riskBoard/riskBoard_list");
		fxsjglDto.setAuditType(AuditTypeEnum.AUDIT_RISK_BOARD.getCode());
		mav.addObject("dto",fxsjglDto);
		List<JcsjDto> list = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RISK_TYPE.getCode());
		List<JcsjDto> riskList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RISKY_ACTIONS.getCode());
		mav.addObject("list",list);
		mav.addObject("riskList",riskList);
		return mav;
	}

	/**
	 * 获取风险上机list
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/board/pageGetListBoard")
	@ResponseBody
	public Map<String, Object> pageGetListBoard(FxsjglDto fxsjglDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		List<FxsjglDto> list = fxsjglService.getPagedDtoList(fxsjglDto);
		if(!CollectionUtils.isEmpty(list)){
			for(FxsjglDto dto:list){
				if(StringUtil.isNotBlank(dto.getLrry())&&dto.getLrry().equals(user.getYhid())){
					dto.setChbj("1");
				}
			}
		}
		map.put("rows",list);
		map.put("total",fxsjglDto.getTotalNumber());
		return map;
	}

	/**
	 * 标本状态风险上机新增
	 * @param
	 * @return
	 */
	@RequestMapping("/board/pagedataAddBoard")
	@ResponseBody
	public Map<String, Object> pagedataAddBoard(FxsjglDto fxsjglDto) {
		Map<String, Object> map = new HashMap<>();
		List<JcsjDto> list = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RISK_TYPE.getCode());
		List<JcsjDto> riskList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RISKY_ACTIONS.getCode());
		User user = getLoginInfo();
		fxsjglDto.setFxsjid(StringUtil.generateUUID());
		fxsjglDto.setLrry(user.getYhid());
		for (JcsjDto jcsjDto : list) {
			if ("1".equals(jcsjDto.getSfmr())){
				fxsjglDto.setFxlb(jcsjDto.getCsid());
				break;
			}
		}
		for (JcsjDto jcsjDto : riskList) {
			if ("1".equals(jcsjDto.getSfmr())){
				fxsjglDto.setBbcl(jcsjDto.getCsid());
				break;
			}
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
		Date date = new Date();
		fxsjglDto.setTzrq(sdf.format(date));
		try {
			fxsjglService.addInfo(fxsjglDto);
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
			return map;
		}
		map.put("status", "success");
		map.put("ywid", fxsjglDto.getFxsjid());
		map.put("auditType", AuditTypeEnum.AUDIT_RISK_BOARD.getCode());
		map.put("message",xxglService.getModelById("ICOM00001").getXxnr());
		return map;

	}

	/**
	 * 风险上机新增
	 * @param
	 * @return
	 */
	@RequestMapping("/board/riskboardBoard")
	public ModelAndView addBoard(FxsjglDto fxsjglDto) {
		ModelAndView mav = new ModelAndView("wechat/riskBoard/riskBoard_add");
		List<JcsjDto> list = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RISK_TYPE.getCode());
		List<JcsjDto> riskList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RISKY_ACTIONS.getCode());
		if (StringUtil.isNotBlank(fxsjglDto.getSyglid())){
			SjsyglDto dtoById = sjsyglService.getDtoById(fxsjglDto.getSyglid());
			fxsjglDto.setHzxm(dtoById.getHzxm());
			fxsjglDto.setYbbh(dtoById.getYbbh());
			fxsjglDto.setNbbm(dtoById.getNbbm());
			fxsjglDto.setYblxmc(dtoById.getYblxmc());
			fxsjglDto.setYwid(dtoById.getYwid());
		}else{
			SjxxDto sjxxDto = new SjxxDto();
			sjxxDto.setSjid(fxsjglDto.getYwid());
			SjxxDto dtoById = sjxxService.getDto(sjxxDto);
			fxsjglDto.setHzxm(dtoById.getHzxm());
			fxsjglDto.setYbbh(dtoById.getYbbh());
			fxsjglDto.setNbbm(dtoById.getNbbm());
			fxsjglDto.setYblxmc(dtoById.getYblxmc());
		}
		for (JcsjDto jcsjDto : list) {
			if ("1".equals(jcsjDto.getSfmr())){
				fxsjglDto.setFxlb(jcsjDto.getCsid());
				break;
			}
		}
		for (JcsjDto jcsjDto : riskList) {
			if ("1".equals(jcsjDto.getSfmr())){
				fxsjglDto.setBbcl(jcsjDto.getCsid());
				break;
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
		Date date = new Date();
		fxsjglDto.setTzrq(sdf.format(date));
		mav.addObject("formAction","/risk/board/riskboardSaveBoard");
		mav.addObject("dto",fxsjglDto);
		mav.addObject("list",list);
		mav.addObject("riskList",riskList);
		mav.addObject("ywlx",BusTypeEnum.IMP_RISK_BOARD.getCode());
		mav.addObject("phoneywid",StringUtil.generateUUID());
		mav.addObject("applicationurl",applicationurl);
		mav.addObject("urlPrefix",urlPrefix);

		return mav;
	}


	/**
	 * 风险上机新增保存
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/board/riskboardSaveBoard")
	@ResponseBody
	public Map<String, Object> addSaveBoard(FxsjglDto fxsjglDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		fxsjglDto.setFxsjid(fxsjglDto.getPhoneywid());
		fxsjglDto.setLrry(user.getYhid());
		try {
			fxsjglService.addInfo(fxsjglDto);
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
			return map;
		}
		map.put("status", "success");
		map.put("ywid", fxsjglDto.getFxsjid());
		map.put("auditType", AuditTypeEnum.AUDIT_RISK_BOARD.getCode());
		redisUtil.del("COM_FILE_UPLOAD:"+fxsjglDto.getPhoneywid()+"_"+fxsjglDto.getYwlx());
		map.put("message",xxglService.getModelById("ICOM00001").getXxnr());
		return map;
	}

	/**
	 * 风险上机修改
	 * @param
	 * @return
	 */
	@RequestMapping("/board/modBoard")
	public ModelAndView modBoard(FxsjglDto fxsjglDto) {
		ModelAndView mav = new ModelAndView("wechat/riskBoard/riskBoard_add");
		FxsjglDto dto = fxsjglService.getDtoById(fxsjglDto.getFxsjid());
		List<JcsjDto> list = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RISK_TYPE.getCode());
		List<JcsjDto> riskList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RISKY_ACTIONS.getCode());
		mav.addObject("dto",dto);
		mav.addObject("ywlx",BusTypeEnum.IMP_RISK_BOARD.getCode());
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(fxsjglDto.getFxsjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_RISK_BOARD.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		mav.addObject("formAction","/risk/board/modSaveBoard");
		mav.addObject("list",list);
		mav.addObject("riskList",riskList);
		mav.addObject("applicationurl",applicationurl);
		mav.addObject("phoneywid",dto.getFxsjid());
		mav.addObject("urlPrefix",urlPrefix);
		return mav;
	}


	/**
	 * 风险上机修改保存
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/board/modSaveBoard")
	@ResponseBody
	public Map<String, Object> modSaveBoard(FxsjglDto fxsjglDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		fxsjglDto.setXgry(user.getYhid());
		try {
			fxsjglService.modInfo(fxsjglDto);
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", e.getMsg());
			return map;
		}
		map.put("status", "success");
		map.put("ywid", fxsjglDto.getFxsjid());
		redisUtil.del("COM_FILE_UPLOAD:"+fxsjglDto.getPhoneywid()+"_"+fxsjglDto.getYwlx());
		map.put("auditType", AuditTypeEnum.AUDIT_RISK_BOARD.getCode());
		map.put("message",xxglService.getModelById("ICOM00001").getXxnr());
		return map;
	}

	/**
	 * 风险上机提交
	 * @param
	 * @return
	 */
	@RequestMapping("/board/submitBoard")
	public ModelAndView submitBoard(FxsjglDto fxsjglDto) {
		ModelAndView mav = modBoard(fxsjglDto);
		mav.addObject("formAction","/risk/board/submitSaveBoard");
		return mav;
	}


	/**
	 * 风险上机提交保存
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/board/submitSaveBoard")
	@ResponseBody
	public Map<String, Object> submitSaveBoard(FxsjglDto fxsjglDto) {
        return modSaveBoard(fxsjglDto);
	}

	/**
	 * 风险上机删除保存
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/board/delBoard")
	@ResponseBody
	public Map<String, Object> delSaveBoard(FxsjglDto fxsjglDto) {
		Map<String, Object> map = new HashMap<>();
		User user = getLoginInfo();
		fxsjglDto.setScry(user.getYhid());
		boolean success = fxsjglService.delInfo(fxsjglDto);
		map.put("status", success ? "success" : "fail");
		map.put("message", success ? xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 风险上机查看
	 * @param
	 * @return
	 */
	@RequestMapping("/board/viewBoard")
	public ModelAndView viewBoard(FxsjglDto fxsjglDto) {
		ModelAndView mav = new ModelAndView("wechat/riskBoard/riskBoard_view");
		FxsjglDto dto = fxsjglService.getDtoById(fxsjglDto.getFxsjid());
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(fxsjglDto.getFxsjid());
		fjcfbDto.setYwlx(BusTypeEnum.IMP_RISK_BOARD.getCode());
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		mav.addObject("fjcfbDtos", fjcfbDtos);
		mav.addObject("dto",dto);
		return mav;
	}

	/**
	 * 风险上机审核列表
	 * @param
	 * @return
	 */
	@RequestMapping("/board/pageListBoardAudit")
	public ModelAndView pageListBoardAudit(FxsjglDto fxsjglDto) {
		ModelAndView mav = new ModelAndView("wechat/riskBoard/riskBoard_auditList");
		fxsjglDto.setAuditType(AuditTypeEnum.AUDIT_RISK_BOARD.getCode());
		mav.addObject("dto",fxsjglDto);
		List<JcsjDto> list = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RISK_TYPE.getCode());
		List<JcsjDto> riskList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.RISKY_ACTIONS.getCode());
		mav.addObject("list",list);
		mav.addObject("riskList",riskList);
		return mav;
	}

	/**
	 * 获取风险上机审核list
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/board/pageGetListBoardAudit")
	@ResponseBody
	public Map<String, Object> pageGetListBoardAudit(FxsjglDto fxsjglDto) {
		Map<String, Object> map = new HashMap<>();
		//附加委托参数
		DataPermission.addWtParam(fxsjglDto);
		//附加审核状态过滤
		List<AuditTypeEnum> auditTypes = new ArrayList<>();
		auditTypes.add(AuditTypeEnum.AUDIT_RISK_BOARD);
		if(GlobalString.AUDIT_SHZT_YSH.equals(fxsjglDto.getDqshzt())){
			DataPermission.add(fxsjglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "fxsjgl", "fxsjid",auditTypes);
		}else{
			DataPermission.add(fxsjglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "fxsjgl", "fxsjid",auditTypes);
		}
		List<FxsjglDto> list = fxsjglService.getPagedAuditList(fxsjglDto);
		map.put("rows",list);
		map.put("total",fxsjglDto.getTotalNumber());
		return map;
	}

}
