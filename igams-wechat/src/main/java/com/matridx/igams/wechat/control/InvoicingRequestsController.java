package com.matridx.igams.wechat.control;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.CommonCreditEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.KpsqDto;
import com.matridx.igams.wechat.service.svcinterface.IKpsqService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/invoicing")
public class InvoicingRequestsController extends BaseController {


	@Autowired
	private IFjcfbService fjcfbService;
	@Autowired
	private IKpsqService kpsqService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private IXxglService xxglService;
	@Autowired
	private ICommonService commonService;


	/**
	 * 获取开票申请list
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/requests/pageGetListInvoice")
	@ResponseBody
	public Map<String, Object> pageGetListInvoice(KpsqDto kpsqDto , HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		super.setCzdmList(request,map);
		kpsqDto.setYwlx(CommonCreditEnum.SW_YSZK_KP.getCode());
		List<KpsqDto> list = kpsqService.getPagedDtoList(kpsqDto);
		map.put("total", kpsqDto.getTotalNumber());
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(KpsqDto.class, "fpsqid","fpxz","kpzt","fphm","sqbm","kpdx","khmc","kjrq","kplx","kjje","kpnr","yx","sh","khyhjzh","dzdh","bz","ddslid","ywlx","ywid","lrsj","lrsj","xgsj","xgry","scsj","scry","scbj","zt","wbcxid","fpxzmc","kpdxmc","kplxmc","jgmc","kpztmc","shxx_sqsj","shxx_gwmc","sqr","shxx_lrryxm","shxx_shsj","shxx_sftg","shxx_shxxid","shxx_shyj","shxx_dqgwmc");
		SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
		listFilters[0] = filter;
		map.put("rows", JSONObject.toJSONString(list,listFilters));
		map.put("auditType", AuditTypeEnum.AUDIT_BUSINESS_INVOICE_APPLICATION.getCode());
		map.put("shlb", AuditTypeEnum.AUDIT_BUSINESS_INVOICE_APPLICATION.getCode());
		List<JcsjDto> invoiceObjectList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INVOICE_OBJECT.getCode());
		List<JcsjDto> invoiceTypeList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INVOICE_TYPE.getCode());
		List<JcsjDto> invoiceBodyList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INVOICE_BODY.getCode());
		List<JcsjDto> invoiceClassList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INVOICE_CLASS.getCode());
		map.put("invoiceObjectList", invoiceObjectList);
		map.put("invoiceTypeList", invoiceTypeList);
		map.put("invoiceBodyList", invoiceBodyList);
		map.put("invoiceClassList", invoiceClassList);
		map.put("ywlx", BusTypeEnum.IMP_INVOICE_CREDIT.getCode());
		return map;
	}

	/**
	 * 获取查看信息
	 * @param
	 * @return
	 */
	@RequestMapping("/requests/viewInvoice")
	@ResponseBody
	public Map<String, Object> viewInvoice(KpsqDto kpsqDto) {
		Map<String, Object> map = new HashMap<>();
		if (StringUtil.isNotBlank(kpsqDto.getFpsqid())){
			kpsqDto = kpsqService.getDto(kpsqDto);
			if (null == kpsqDto)
				return map;
			if (StringUtil.isNotBlank(kpsqDto.getSpr())){
				User user = new User();
				user.setIds(Arrays.asList(kpsqDto.getSpr().split(",")));
				List<User> list = commonService.getListByIds(user);
				map.put("userList", list);
			}
			FjcfbDto fjcfbDto = new FjcfbDto();
			fjcfbDto.setYwid(kpsqDto.getFpsqid());
			fjcfbDto.setYwlx(BusTypeEnum.IMP_INVOICE_CREDIT.getCode());
			List<FjcfbDto> list = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
			map.put("list",list);
			map.put("kpsqDto",kpsqDto);
		}
		return map;
	}

	/**
	 * 获取修改信息
	 * @param
	 * @return
	 */
	@RequestMapping("/requests/modInvoice")
	@ResponseBody
	public Map<String, Object> modInvoice(KpsqDto kpsqDto) {
		return viewInvoice(kpsqDto);
	}

	/**
	 * 获取提交信息
	 * @param
	 * @return
	 */
	@RequestMapping("/requests/modSaveInvoice")
	@ResponseBody
	public Map<String, Object> modSaveInvoice(KpsqDto kpsqDto) {
		return submitSaveInvoice(kpsqDto);
	}

	/**
	 * 获取提交信息
	 * @param
	 * @return
	 */
	@RequestMapping("/requests/submitInvoice")
	@ResponseBody
	public Map<String, Object> submitInvoice(KpsqDto kpsqDto) {
		return viewInvoice(kpsqDto);
	}

	/**
	 * 获取提交信息
	 * @param
	 * @return
	 */
	@RequestMapping("/requests/submitSaveInvoice")
	@ResponseBody
	public Map<String, Object> submitSaveInvoice(KpsqDto kpsqDto) {
		Map<String, Object> map = new HashMap<>();
		Boolean success = false;
		if (StringUtil.isNotBlank(kpsqDto.getFpsqid())){
			User user = getLoginInfo();
			kpsqDto.setXgry(user.getYhid());
			success = kpsqService.submitInfo(kpsqDto);
		}
		map.put("status", success ? "success" : "fail");
		map.put("ywid", kpsqDto.getFpsqid());
		map.put("message", success ? xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}


	/**
	 * 删除保存信息
	 * @param
	 * @return
	 */
	@RequestMapping("/requests/delSaveInvoice")
	@ResponseBody
	public Map<String, Object> delSaveInvoice(KpsqDto kpsqDto) {
		Map<String, Object> map = new HashMap<>();
		Boolean success = false;
		if (CollectionUtils.isNotEmpty(kpsqDto.getIds())){
			User user = getLoginInfo();
			kpsqDto.setScry(user.getYhid());
			success = kpsqService.delInfo(kpsqDto);
		}
		map.put("status", success ? "success" : "fail");
		map.put("message", success ? xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}

	/**
	 * 获取开票审核list
	 *
	 * @param
	 * @return
	 */
	@RequestMapping("/requests/pageGetListAudit")
	@ResponseBody
	public Map<String, Object> pageGetListAudit(KpsqDto kpsqDto, HttpServletRequest request) {
		Map<String, Object> map = new HashMap<>();
		super.setCzdmList(request,map);
		//附加委托参数
		DataPermission.addWtParam(kpsqDto);
		//附加审核状态过滤
		List<AuditTypeEnum> auditTypes = new ArrayList<>();
		auditTypes.add(AuditTypeEnum.AUDIT_BUSINESS_INVOICE_APPLICATION);
		if(GlobalString.AUDIT_SHZT_YSH.equals(kpsqDto.getDqshzt())){
			DataPermission.add(kpsqDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "kpsqgl", "fpsqid",auditTypes);
		}else{
			DataPermission.add(kpsqDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "kpsqgl", "fpsqid",auditTypes);
		}
		List<KpsqDto> list = kpsqService.getPagedAuditList(kpsqDto);
		map.put("total", kpsqDto.getTotalNumber());
		SimplePropertyPreFilter filter = new SimplePropertyPreFilter(KpsqDto.class, "fpsqid","fpxz","kpzt","fphm","sqbm","kpdx","khmc","kjrq","kplx","kjje","kpnr","yx","sh","khyhjzh","dzdh","bz","ddslid","ywlx","ywid","lrsj","lrsj","xgsj","xgry","scsj","scry","scbj","zt","wbcxid","fpxzmc","kpdxmc","kplxmc","jgmc","kpztmc","shxx_sqsj","shxx_gwmc","sqr","shxx_lrryxm","shxx_shsj","shxx_sftg","shxx_shxxid","shxx_shyj","shxx_dqgwmc");
		SimplePropertyPreFilter[] listFilters = new SimplePropertyPreFilter[1];
		listFilters[0] = filter;
		map.put("rows", JSONObject.toJSONString(list,listFilters));
		map.put("auditType", AuditTypeEnum.AUDIT_BUSINESS_INVOICE_APPLICATION.getCode());
		List<JcsjDto> invoiceObjectList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INVOICE_OBJECT.getCode());
		List<JcsjDto> invoiceTypeList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INVOICE_TYPE.getCode());
		List<JcsjDto> invoiceBodyList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INVOICE_BODY.getCode());
		List<JcsjDto> invoiceClassList = redisUtil.lgetDto("List_matridx_jcsj:"+ BasicDataTypeEnum.INVOICE_CLASS.getCode());
		map.put("invoiceObjectList", invoiceObjectList);
		map.put("invoiceTypeList", invoiceTypeList);
		map.put("invoiceBodyList", invoiceBodyList);
		map.put("invoiceClassList", invoiceClassList);
		map.put("ywlx", BusTypeEnum.IMP_INVOICE_CREDIT.getCode());
		return map;
	}


}
