package com.matridx.igams.common.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.matridx.igams.common.dao.entities.CydcszDto;
import com.matridx.igams.common.dao.entities.CydcszModel;
import com.matridx.igams.common.dao.entities.CydcxxModel;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.GrlbzdszDto;
import com.matridx.igams.common.dao.entities.LbzdszDto;
import com.matridx.igams.common.dao.entities.ProcessModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IDcszService;
import com.matridx.igams.common.service.svcinterface.IGrlbzdszService;
import com.matridx.igams.common.service.svcinterface.ILbzdszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.springboot.util.base.StringUtil;

@Controller
@RequestMapping("/common")
public class CommonExportController extends BaseController{
	
	@Autowired
	private IDcszService dcszService;
	@Autowired
	private ILbzdszService lbzdszService;
	@Autowired
	private IGrlbzdszService grlbzdszService;
	@Autowired
	private IXxglService xxglService;
	
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	
	@Override
	public String getPrefix(){
		return urlPrefix;
	}
	
	private final Logger log = LoggerFactory.getLogger(CommonExportController.class);
	
	/**
	 * 根据业务ID，获取相应的导出字段信息，同时显示到客户页面
	 */
	@RequestMapping(value="/export/commExportPrepare")
	public ModelAndView commExportPrepare(DcszDto dcszDto, HttpServletRequest request){
		
		ModelAndView mav = new ModelAndView("common/export/com_export");
		
		dcszDto.setYhid(getLoginInfo(request).getYhid());
		
		if(StringUtil.isNotBlank(dcszDto.getYwid())){
			//获取已选择的导出字段
			List<DcszDto> choseList = dcszService.getChoseList(dcszDto);
			//获取该成员未选择的导出字段，并按照分类区分显示
			List<DcszDto> cateMap = dcszService.getCateWaitList(dcszDto);
			//常用导出列表
			List<CydcszModel> cydcszs = dcszService.getCydcszxxs(dcszDto);
			
			mav.addObject("choseList", choseList);
			
			mav.addObject("cateMap", cateMap);
			
			mav.addObject("cydcszs", cydcszs);
		}
		StringBuilder concatCs = new StringBuilder();
		Set<String> keys = request.getParameterMap().keySet();
		for (String key : keys) {
			if (!"ywid".equals(key) && !"ids".equals(key) && !"expType".equals(key) && !"callbackJs".equals(key) && !"time".equals(key) && !"access_token".equals(key) && !"selectjson".equals(key)){
				concatCs.append(",").append(key).append("=").append(request.getParameter(key));
			}
		}
		if (StringUtil.isNotBlank(concatCs.toString())){
			concatCs = new StringBuilder(concatCs.substring(1));
		}
		mav.addObject("concatCs", concatCs.toString());
		mav.addObject("dcszDto", dcszDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 根据业务id获取相应的数据
	 */
	@PostMapping("/template/minidataCommExportPrepare")
	@ResponseBody
	public Map<String,Object> minidataCommExportPrepare(DcszDto dcszDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		dcszDto.setYhid(getLoginInfo(request).getYhid());
		if(StringUtil.isNotBlank(dcszDto.getYwid())){
			//获取已选择的导出字段
			List<DcszDto> choseList = dcszService.getChoseList(dcszDto);
			//获取该成员未选择的导出字段，并按照分类区分显示
			List<DcszDto> cateMap = dcszService.getCateWaitList(dcszDto);
			//常用导出列表
			List<CydcszModel> cydcszs = dcszService.getCydcszxxs(dcszDto);

			map.put("choseList", choseList);

			map.put("cateMap", cateMap);

			map.put("cydcszs", cydcszs);
		}

		map.put("dcszDto", dcszDto);
		map.put("urlPrefix", urlPrefix);
		return map;
	}
	/**
     * 选中数据导出
     * dcszModel.ywid  必须  如PROJECT
     * dcszModel.sfbc  是否保存个人设置    Y:保存
     * dcszModel.fileType  0：excel  1：cvs
     * choseList  已选字段
     */
	@RequestMapping(value="/export/exportSelect")
	@ResponseBody
	public Map<String,Object> exportSelect(DcszDto dcszDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		dcszDto.setYhid(user.getYhid());
		dcszDto.setJsid(user.getDqjs());
		Map<String,Object> map = new HashMap<>();
		try {
			ProcessModel pModel = dcszService.export(dcszDto,dcszDto.getChoseList(),user,request);
			map.put("result", true);
			map.put("totalCount",pModel.getCurrentCount());
			map.put("wjid",pModel.getWjid());
		} catch (BusinessException e) {
			//logException(e);
			map.put("result", false);
			map.put("msg", e.getMsgId());
		} catch (Exception e) {
			log.error(e.toString());
		}
		return map;
	}


	/**
	 * 根据搜索条件导出   从其他action进入
	 * 导出文件并保存个人设置 
	 * dcszModel.ywid  必须  如PROJECT
	 * dcszModel.sfbc  是否保存个人设置    Y:保存
	 * dcszModel.fileType  0：excel  1：cvs
	 * choseList  已选字段
	 * @param dcszDto  传入的实体类   包含sql条件参数
	 */
	@RequestMapping(value="/export/exportSearch")
	@ResponseBody
	public Map<String,Object> exportSearch(DcszDto dcszDto,HttpServletRequest request){
		
		Map<String,Object> map = new HashMap<>();
		
		try {
			User user = getLoginInfo(request);
			dcszDto.setYhid(user.getYhid());
			dcszDto.setJsid(user.getDqjs());
			
			ProcessModel pModel = dcszService.export(dcszDto,dcszDto.getChoseList(),user,request);
			map.put("result", true);
			map.put("totalCount",pModel.getCurrentCount());
			map.put("wjid",pModel.getWjid());
		} catch (BusinessException e) {
			//logException(e);
			map.put("result", false);
			map.put("msg", e.getMsgId());
		} catch (Exception e) {
			map.put("result", false);
			map.put("msg", e.getMessage());
		}
		return map;
	}
	
	/**
	 * 取消导出
	 */
	@RequestMapping(value="/export/commCancelExport")
	@ResponseBody
	public Map<String,Object> commCancelExport(DcszDto dcszDto) {
		Map<String,Object> map = new HashMap<>();
		map.put("result", dcszService.commCancelExport(dcszDto));
		return map;
	}
	
	/**
	 * 检查文件处理进度
	 */

	@RequestMapping(value="/export/commCheckExport")
	@ResponseBody
	public Map<String,Object> commCheckExport(DcszDto dcszDto) {
		return dcszService.commCheckExport(dcszDto);
	}
	
	/**
	 * 文件下载处理
	 */
	@RequestMapping(value="/export/commDownloadExport")
	@ResponseBody
	public void commDownloadExport(DcszDto dcszDto,HttpServletResponse response){
		dcszService.commDownloadExport(dcszDto,response);
	}
	
	/**
	 * 列表字段选择页面显示
	 */
	@RequestMapping(value="/title/commTitleSelectPrepare")
	public ModelAndView commTitleSelectPrepare(LbzdszDto lbzdszDto,GrlbzdszDto grlbzdszDto,HttpServletRequest request){
		ModelAndView mav = new ModelAndView("common/listfields/listfields_set");
		try {
			
			if(StringUtil.isBlank(lbzdszDto.getYwid())){
				return mav;
			}
			grlbzdszDto.setYhid(getLoginInfo(request).getYhid());
			lbzdszDto.setYhid(getLoginInfo(request).getYhid());
			lbzdszDto.setJsid(getLoginInfo(request).getDqjs());
			List<LbzdszDto> choseList = grlbzdszService.getChoseList(grlbzdszDto);
			List<LbzdszDto> waitList = lbzdszService.getWaitList(lbzdszDto);

			mav.addObject("lbzdszDto", lbzdszDto);
			mav.addObject("choseList", choseList);
			mav.addObject("waitList", waitList);
			mav.addObject("urlPrefix", urlPrefix);
			//return "listFields_set";
		} catch (Exception e) {
			//logException(e);
			//return ERROR;
		}
		return mav;
	}

	/**
	 * 列表字段选择页面显示
	 */
	@RequestMapping(value="/title/commTitleSelectPrepareVue")
	@ResponseBody
	public Map<String,Object> commTitleSelectPrepareVue(LbzdszDto lbzdszDto,GrlbzdszDto grlbzdszDto,HttpServletRequest request){
		Map<String,Object>map=new HashMap<>();
		try {
			map.put("status","true");
			if(StringUtil.isBlank(lbzdszDto.getYwid())){
				map.put("status","false");
			}
			grlbzdszDto.setYhid(getLoginInfo(request).getYhid());
			lbzdszDto.setYhid(getLoginInfo(request).getYhid());
			lbzdszDto.setJsid(getLoginInfo(request).getDqjs());
			List<LbzdszDto> choseList = grlbzdszService.getChoseList(grlbzdszDto);
			List<LbzdszDto> waitList = lbzdszService.getWaitList(lbzdszDto);

			map.put("lbzdszDto", lbzdszDto);
			map.put("choseList", choseList);
			map.put("waitList", waitList);
			map.put("urlPrefix", urlPrefix);
			//return "listFields_set";
		} catch (Exception e) {
			//logException(e);
			//return ERROR;
		}
		return map;
	}

	/**
	 * 列表字段选择保存
	 */
	@RequestMapping(value="/title/commTitleSelectSave")
	@ResponseBody
	public Map<String, Object> commTitleSelectSave(LbzdszDto lbzdszDto,GrlbzdszDto grlbzdszDto,HttpServletRequest request){
		try {
			grlbzdszDto.setYhid(getLoginInfo(request).getYhid());
			lbzdszDto.setYhid(getLoginInfo(request).getYhid());
			lbzdszDto.setJsid(getLoginInfo(request).getDqjs());
			if(StringUtil.isNotBlank(grlbzdszDto.getChoseListVueStr())){
				List<GrlbzdszDto> list= JSON.parseArray(grlbzdszDto.getChoseListVueStr(),GrlbzdszDto.class);
				grlbzdszDto.setChoseListVue(list);
			}
			boolean isSuccess = grlbzdszService.SaveChoseList(grlbzdszDto);

			Map<String, Object> map = new HashMap<>();
			if(isSuccess) {
				List<LbzdszDto> choseList = grlbzdszService.getChoseList(grlbzdszDto);
				List<LbzdszDto> waitList = lbzdszService.getWaitList(lbzdszDto);

				map.put("choseList", choseList);
				map.put("waitList", waitList);
			}
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
			//logException(e);
			//return ERROR;
		}
		return new HashMap<>();
	}
	
	/**
	 * 列表字段恢复默认
	 */
	@RequestMapping(value="/title/commTitleDefaultSave")
	@ResponseBody
	public Map<String, Object> commTitleDefaultSave(LbzdszDto lbzdszDto,GrlbzdszDto grlbzdszDto,HttpServletRequest request){
		try {
			grlbzdszDto.setYhid(getLoginInfo(request).getYhid());
			lbzdszDto.setYhid(getLoginInfo(request).getYhid());
			lbzdszDto.setJsid(getLoginInfo(request).getDqjs());
			boolean isSuccess = grlbzdszService.delete(grlbzdszDto);

			Map<String, Object> map = new HashMap<>();
			if(isSuccess) {
				List<LbzdszDto> choseList = grlbzdszService.getChoseList(grlbzdszDto);
				List<LbzdszDto> waitList = lbzdszService.getWaitList(lbzdszDto);

				map.put("choseList", choseList);
				map.put("waitList", waitList);
			}
			map.put("status",isSuccess?"success":"fail");
			map.put("message",isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		} catch (Exception e) {
			//logException(e);
			//return ERROR;
		}
		return new HashMap<>();
	}
	
	/**
	 * 获取常用导出信息
	 */
	@PostMapping("/export/commGetCydcxx")
	@ResponseBody
	public Map<String,Object> commGetCydcxx(CydcszDto cydcszDto,HttpServletRequest request){
		Map<String,Object> map = new HashMap<>();
		if(StringUtil.isNotBlank(cydcszDto.getCyid())){
			//常用导出列表
			List<CydcxxModel> cydcxxs = dcszService.getCydcxxs(cydcszDto);

			map.put("cydcxxs", cydcxxs);
		}

		map.put("urlPrefix", urlPrefix);
		return map;
	}
}
