package com.matridx.igams.wechat.control;

import com.matridx.igams.common.GlobalString;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.DataPermissionTypeEnum;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.wechat.dao.entities.CwglDto;
import com.matridx.igams.wechat.service.svcinterface.ICwglService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/inspection")
public class SampleNumController {
	
	@Autowired
	private ICwglService cwglService;
	@Autowired
	private IXxglService xxglService;
	
	/**
	 * 跳转标本量列表页面
	 * @return
	 */
	@RequestMapping(value = "/samplenum/pageListSamplenum")
	public ModelAndView getSamplenumListPage(CwglDto cwglDto) {
		ModelAndView mav =new ModelAndView("wechat/samplenum/samplenum_List");
		//获取审核类型
		cwglDto.setAuditType(AuditTypeEnum.AUDIT_SAMPNUM.getCode());
		mav.addObject("CwglDto", cwglDto);
		return mav;
	}
	
	/**
	 * 获取标本量列表
	 * @param cwglDto
	 * @return
	 */
	@RequestMapping(value ="/samplenum/pageGetListSamplenum")
	@ResponseBody
	public Map<String,Object> getSamplenumListPageList(CwglDto cwglDto){
		List<CwglDto> cwgllist=cwglService.getPagedDtoList(cwglDto);
		Map<String, Object> map= new HashMap<>();
		map.put("total", cwglDto.getTotalNumber());
		map.put("rows", cwgllist);
		return map;
	}
	
	/**
	 * 查看
	 * @param cwglDto
	 * @return
	 */
	@RequestMapping(value ="/samplenum/viewSamplenum")
	public ModelAndView viewSamplenum(CwglDto cwglDto) {
		ModelAndView mav =new ModelAndView("wechat/samplenum/samplenum_view");
		CwglDto cwglxx=cwglService.getDtoById(cwglDto.getCwid());
		mav.addObject("CwglDto", cwglxx);
		return mav;
	}
	
	/**
	 * 删除
	 * @param cwglDto
	 * @return
	 */
	@RequestMapping(value="/samplenum/delSamplenum")
	@ResponseBody
	public Map<String,Object> delSampleNum(CwglDto cwglDto){
		boolean isSuccess=cwglService.delete(cwglDto);
		Map<String, Object> map= new HashMap<>();
		map.put("status",isSuccess?"success":"fail");
		map.put("message",isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 提交
	 * @param cwglDto
	 * @return
	 */
	@RequestMapping(value="/samplenum/submitSampleNum")
	public ModelAndView submitSampleNum(CwglDto cwglDto) {
		ModelAndView mav =new ModelAndView("wechat/samplenum/samplenum_view");
		CwglDto cwglxx=cwglService.getDtoById(cwglDto.getCwid());
		cwglDto.setAuditType(AuditTypeEnum.AUDIT_SAMPNUM.getCode());
		mav.addObject("CwglDto", cwglxx);
		mav.addObject("ywid", cwglxx.getCwid());
		return mav;
	}
	
	/**
	 * 标本量申请审核列表页面
	 * @return
	 */
	@RequestMapping(value="/samplenum/pageListAuditSampleNum")
	public ModelAndView getPageListSampleNumAudit() {
        return new ModelAndView("wechat/samplenum/samplenum_listaudit");
	}
	
	/**
	 * 标本量审核列表
	 * @return
	 */
	@RequestMapping(value ="/samplenum/pageGetListAuditSampleNum")
	@ResponseBody
	public Map<String,Object> listMaterAudit(CwglDto cwglDto){
		
		//附加委托参数
		DataPermission.addWtParam(cwglDto);
		//附加数据权限控制
		//DataPermission.addDyx(wlglDto, "wj", SsdwTableEnum.WJGL);
		//附加审核状态过滤
		if(GlobalString.AUDIT_SHZT_YSH.equals(cwglDto.getDqshzt())){
			DataPermission.add(cwglDto, DataPermissionTypeEnum.PERMISSION_TYPE_YSP, "cw", "cwid", AuditTypeEnum.AUDIT_SAMPNUM);
		}else{
			DataPermission.add(cwglDto, DataPermissionTypeEnum.PERMISSION_TYPE_WSP, "cw", "cwid", AuditTypeEnum.AUDIT_SAMPNUM);
		}
		
		List<CwglDto> t_List = cwglService.getPagedAuditList(cwglDto);
		
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", cwglDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	/**
	 * 标本量审核页面
	 * @param cwglDto
	 * @return
	 */
	@RequestMapping(value="/samplenum/AuditSampleNum")
	public ModelAndView getSumpleNumAuditPage(CwglDto cwglDto) {
		ModelAndView mav =new ModelAndView("wechat/samplenum/samplenum_listviewaudit");
		CwglDto cwglxx=cwglService.getDtoById(cwglDto.getCwid());
		mav.addObject("CwglDto", cwglxx);
		return mav;
	}
}
