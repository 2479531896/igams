package com.matridx.igams.production.controller;

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
import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.controller.BaseController;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.enums.SsdwTableEnum;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.production.dao.entities.QgqxglDto;
import com.matridx.igams.production.dao.entities.QgqxmxDto;
import com.matridx.igams.production.service.svcinterface.IQgqxglService;
import com.matridx.igams.production.service.svcinterface.IQgqxmxService;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/purchaseCancel")
public class PurchaseCancelController extends BaseController{
	
	@Autowired
	IQgqxmxService qgqxmxService;
	
	@Autowired
	IXxglService xxglService;
	
	@Autowired
	IQgqxglService qgqxglService;
	
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	//是否发送rabbit标记     1：发送
  	@Value("${matridx.rabbit.systemconfigflg:}")
  	private String systemconfigflg;
  	@Value("${matridx.rabbit.preflg:}")
  	private String preRabbitFlg;
  	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
  	
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	/**
	 * 取消请购审核列表页面
	 */
	@RequestMapping("/purchaseCancel/pageListAuditPurchaseCancel")
	public ModelAndView pageListAuditPurchaseCancel() {
		ModelAndView mav =new ModelAndView("purchaseCancel/purchaseCancel/purchaseCancel_auditList");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 取消请购审核列表
	 */
	@RequestMapping("/purchaseCancel/pageGetListAuditPurchaseCancel")
	@ResponseBody
	public Map<String,Object> pageGetListAuditPurchaseCancel(QgqxglDto qgqxglDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		// 附加委托参数
		DataPermission.addWtParam(qgqxglDto);
		// 附加审核状态过滤
		if (GlobalString.AUDIT_SHZT_YSH.equals(qgqxglDto.getDqshzt())){
			DataPermission.add(qgqxglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "qgqxgl", "qgqxid", AuditTypeEnum.AUDIT_REQUISITIONS_CANCEL);
		} else{
			DataPermission.add(qgqxglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "qgqxgl", "qgqxid", AuditTypeEnum.AUDIT_REQUISITIONS_CANCEL);
		}
		DataPermission.addSpDdw(qgqxglDto, "qgqxgl", SsdwTableEnum.QGQXGL);
		DataPermission.addCurrentUser(qgqxglDto, getLoginInfo(request));
		List<QgqxglDto> listMap = qgqxglService.getPagedAuditQgqxgl(qgqxglDto);

		map.put("total", qgqxglDto.getTotalNumber());
		map.put("rows", listMap);
		return map;
	}
	
	/**
	 * 取消请购列表
	 */
	@RequestMapping("/purchaseCancel/pageListPurchaseCancel")
	public ModelAndView pageListPurchaseCancel() {
		ModelAndView mav=new ModelAndView("purchaseCancel/purchaseCancel/purchaseCancel_List");
		mav.addObject("auditType", AuditTypeEnum.AUDIT_REQUISITIONS_CANCEL.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 取消请购列表数据
	 */
	@RequestMapping("/purchaseCancel/pageGetListPurchaseCancel")
	@ResponseBody
	public Map<String,Object> pageGetListPurchaseCancel(QgqxmxDto qgqxmxDto){
		Map<String,Object> map=new HashMap<>();
		List<QgqxmxDto> list=qgqxmxService.getPagedDtoList(qgqxmxDto);
		map.put("total", qgqxmxDto.getTotalNumber());
		map.put("rows", list);
		return map;
	}
	
	/**
	 * 保存删除取消请购信息
	 */
	@ResponseBody
	@RequestMapping(value = "/purchaseCancel/delSaveContract")
	public Map<String,Object> delContractSave(QgqxmxDto qgqxmxDto,HttpServletRequest request){
		Map<String,Object> map=new HashMap<>();
		try {
			//获取用户信息
			User user = getLoginInfo(request);
			qgqxmxDto.setScry(user.getYhid());
			boolean isSuccess = qgqxglService.deleteQgqx(qgqxmxDto);
			if("1".equals(systemconfigflg) && isSuccess) {
				qgqxmxDto.setPrefixFlg(prefixFlg);
				amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qgqxmx.delQgqxmx",JSONObject.toJSONString(qgqxmxDto));
			}
			map.put("status", isSuccess?"success":"fail");
			map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		} catch (BusinessException e) {
			map.put("status", "fail");
			map.put("message", StringUtil.isNotBlank(e.getMsg())?e.getMsg():xxglService.getModelById("ICOM00004").getXxnr());
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", xxglService.getModelById("ICOM00004").getXxnr());
		}
		return map;
	}

	/**
	 * 删除取消请购信息
	 */
	@RequestMapping(value = "/purchaseCancel/delPurchaseCancel")
	public ModelAndView delContractView(QgqxmxDto qgqxmxDto){
		ModelAndView mav=new ModelAndView("purchaseCancel/purchaseCancel/purchaseCancel_Del");
		QgqxglDto qgqxglDto_t=qgqxglService.getDtoById(qgqxmxDto.getQgqxid());
		qgqxglDto_t.setFormAction("delContractSave");
		mav.addObject("qgqxglDto_t", qgqxglDto_t);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 查看请购取消信息
	 */
	@RequestMapping("/purchaseCancel/viewPurchaseCancel")
	public ModelAndView viewPurchaseCancel(QgqxmxDto qgqxmxDto) {
		ModelAndView mav=new ModelAndView("purchaseCancel/purchaseCancel/purchaseCancel_View");
		QgqxglDto qgqxglDto_t=qgqxglService.getDtoById(qgqxmxDto.getQgqxid());
		// 查询附件信息 -采购单
		mav.addObject("qgqxglDto_t", qgqxglDto_t);
		mav.addObject("auditType", AuditTypeEnum.AUDIT_REQUISITIONS_CANCEL.getCode());
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	} 
	
	/**
	 * 采购取消明细数据
	 */
	@RequestMapping("/purchaseCancel/pagedataGetListpurchaseCancelInfo")
	@ResponseBody
	public Map<String,Object> getListpurchaseCancelInfo(QgqxmxDto qgqxmxDto){
		Map<String,Object> map=new HashMap<>();
		List<QgqxmxDto> list=qgqxmxService.getPagedDtoList(qgqxmxDto);
		map.put("rows", list);
		map.put("total", qgqxmxDto.getTotalNumber());
		return map;
	}

	/**
	 * 请购取消修改页面(提交)
	 */
	@RequestMapping("/purchaseCancel/submitPurchaseCancel")
	@ResponseBody
	public ModelAndView submitPurchaseCancel(QgqxglDto qgqxglDto) {
		ModelAndView mav =new ModelAndView("purchase/purchase/purchase_cancel");
		QgqxglDto qgqxglDto_t = qgqxglService.getDtoById(qgqxglDto.getQgqxid());
		qgqxglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_CANCEL.getCode());
        mav.addObject("qgglDto", qgqxglDto_t);
        mav.addObject("url", "/purchaseCancel/purchaseCancel/pagedataQgqxmxList?qgqxid="+qgqxglDto_t.getQgqxid());
        mav.addObject("formAction", "/purchaseCancel/purchaseCancel/pagedataSavePurchaseCancel");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
	}
	/**
	 * 请购取消修改页面（审核）
	 */
	@RequestMapping("/purchaseCancel/auditPurchaseCancel")
	@ResponseBody
	public ModelAndView auditPurchaseCancel(QgqxglDto qgqxglDto) {
		ModelAndView mav =new ModelAndView("purchase/purchase/purchase_cancel");
		QgqxglDto qgqxglDto_t = qgqxglService.getDtoById(qgqxglDto.getQgqxid());
		qgqxglDto_t.setAuditType(AuditTypeEnum.AUDIT_REQUISITIONS_CANCEL.getCode());
        mav.addObject("qgglDto", qgqxglDto_t);
        mav.addObject("url", "/purchaseCancel/purchaseCancel/pagedataQgqxmxList?qgqxid="+qgqxglDto_t.getQgqxid());
        mav.addObject("formAction", "/purchaseCancel/purchaseCancel/pagedataSavePurchaseCancel");
        mav.addObject("urlPrefix", urlPrefix);
        return mav;
	}
	
	/**
	 * 请购取消修改保存
	 */
	@RequestMapping("/purchaseCancel/pagedataSavePurchaseCancel")
	@ResponseBody
	public Map<String,Object> modSavePurchaseCancel(QgqxglDto qgqxglDto,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
        User user = getLoginInfo(request);
        qgqxglDto.setXgry(user.getYhid());
        boolean isSuccess=qgqxglService.modSavePurchaseCancel(qgqxglDto);
        if("1".equals(systemconfigflg) && isSuccess) {
        	qgqxglDto.setPrefixFlg(prefixFlg);
			amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.qgqxgl.updateQgqxgl",JSONObject.toJSONString(qgqxglDto));
		}
        map.put("ywid", qgqxglDto.getQgqxid());
        map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
        return map;
	}
	
	/**
	 * 请购取消明细列表(不分页)
	 */
	@RequestMapping("/purchaseCancel/pagedataQgqxmxList")
	@ResponseBody
	public Map<String,Object> getQgqxmxList(QgqxglDto qgqxglDto){
		Map<String,Object> map=new HashMap<>();
		QgqxmxDto qgqxmxDto=new QgqxmxDto();
		qgqxmxDto.setQgqxid(qgqxglDto.getQgqxid());
		List<QgqxmxDto> qgqxmxlist=qgqxmxService.getQgqxmxCancelList(qgqxmxDto);
		map.put("rows", qgqxmxlist);
		return map;
	}
	
}
