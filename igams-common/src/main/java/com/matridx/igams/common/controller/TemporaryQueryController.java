package com.matridx.igams.common.controller;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.LscxqxDto;
import com.matridx.igams.common.dao.entities.LscxszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.ILscxqxService;
import com.matridx.igams.common.service.svcinterface.ILscxszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.PcrSangerUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.stream.Collectors;

@Controller
@RequestMapping("/systemmain")
public class TemporaryQueryController extends BaseController{
	
	@Autowired
	ILscxszService lscxszService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ILscxqxService lscxqxService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	RedisUtil redisUtil;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Value("${matridx.fileupload.releasePath}")
	private String releaseFilePath;
	@Value("${matridx.fileupload.tempPath}")
	private String tempFilePath;
	@Override
	public String getPrefix(){
		return urlPrefix;
	}

	
	private final Logger log = LoggerFactory.getLogger(TemporaryQueryController.class);

	/**
	 * 临时查询列表页面
	 */
	@RequestMapping(value="/query/pageListQuery")
	public ModelAndView getTemporaryQueryPageList() {
		ModelAndView mav=new ModelAndView("systemmain/query/query_list");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[] {BasicDataTypeEnum.QUERY_DIVISION,BasicDataTypeEnum.DISPLAY_MODE});
		mav.addObject("cxqflist", jclist.get(BasicDataTypeEnum.QUERY_DIVISION.getCode()));
		mav.addObject("xsfslist", jclist.get(BasicDataTypeEnum.DISPLAY_MODE.getCode()));
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 临时查询列表
	 */
	@RequestMapping(value="/query/pageGetListQuery")
	@ResponseBody
	public Map<String,Object> pageGetListQuery(LscxszDto lscxszDto){
		List<LscxszDto> lscxszlist=lscxszService.getPagedDtoList(lscxszDto);
		Map<String,Object> result = new HashMap<>();
		result.put("total", lscxszDto.getTotalNumber());
		result.put("rows", lscxszlist);
		return result;
	}
	
	/**
	 * 若存在insert，delete等高风险语句，进行替换,
	 */
	public void replace(LscxszDto lscxszDto) {
		if(!("AISELECT".equals(lscxszDto.getCxqfdm()) || "AIINSERT".equals(lscxszDto.getCxqfdm()) || "AIDEL".equals(lscxszDto.getCxqfdm()))){
			if(lscxszDto.getCxdm().toLowerCase().contains("insert")) {
				lscxszDto.setCxdm((lscxszDto.getCxdm()).replaceAll("(?i)insert", ""));
			}else if(lscxszDto.getCxdm().toLowerCase().contains("delete")) {
				lscxszDto.setCxdm((lscxszDto.getCxdm()).replaceAll("(?i)delete", ""));
			}else if(lscxszDto.getCxdm().toLowerCase().contains("update")) {
				lscxszDto.setCxdm((lscxszDto.getCxdm()).replaceAll("(?i)update", ""));
			}else if(lscxszDto.getCxdm().toLowerCase().contains("alert")) {
				lscxszDto.setCxdm((lscxszDto.getCxdm()).replaceAll("(?i)alert", ""));
			}else if(lscxszDto.getCxdm().toLowerCase().contains("drop")) {
				lscxszDto.setCxdm((lscxszDto.getCxdm()).replaceAll("(?i)drop", ""));
			}
		}
	}
	/**
	 * 新增页面
	 */
	@RequestMapping(value="/query/addTemporaryQuery")
	public ModelAndView getAddQueryPage(LscxszDto lscxszDto) {
		ModelAndView mav=new ModelAndView("systemmain/query/query_add");
		lscxszDto.setFormAction("add");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[] {BasicDataTypeEnum.QUERY_DIVISION,BasicDataTypeEnum.DISPLAY_MODE});
		mav.addObject("cxqflist", jclist.get(BasicDataTypeEnum.QUERY_DIVISION.getCode()));
		mav.addObject("xsfslist", jclist.get(BasicDataTypeEnum.DISPLAY_MODE.getCode()));
		mav.addObject("LscxszDto", lscxszDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	
	/**
	 * 新增提交保存
	 */
	@RequestMapping(value="/query/addSaveTemporaryQuery")
	@ResponseBody
	public Map<String,Object> addSaveLscx(LscxszDto lscxszDto,HttpServletRequest request) {
		User user = getLoginInfo(request);
		lscxszDto.setLrry(user.getYhid());
		replace(lscxszDto);
		boolean isSuccess=lscxszService.insert(lscxszDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 查看临时查询信息
	 */
	@RequestMapping(value="/query/viewTemporaryQuery")
	public ModelAndView viewLscxPage(LscxszDto lscxszDto) {
		ModelAndView mav = new ModelAndView("systemmain/query/query_view");
		LscxszDto lscxsz=lscxszService.selectLscxByCxid(lscxszDto);
		mav.addObject("LscxszDto", lscxsz);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 修改界面
	 */
	@RequestMapping(value="/query/modTemporaryQuery")
	public ModelAndView modLscxszPage(LscxszDto lscxszDto) {
		ModelAndView mav=new ModelAndView("systemmain/query/query_add");
		Map<String, List<JcsjDto>> jclist = jcsjService.getDtoListbyJclbInStop(new BasicDataTypeEnum[] {BasicDataTypeEnum.QUERY_DIVISION,BasicDataTypeEnum.DISPLAY_MODE});
		mav.addObject("cxqflist", jclist.get(BasicDataTypeEnum.QUERY_DIVISION.getCode()));
		mav.addObject("xsfslist", jclist.get(BasicDataTypeEnum.DISPLAY_MODE.getCode()));
		LscxszDto lscxsz=lscxszService.selectLscxByCxid(lscxszDto);
		lscxsz.setFormAction("mod");
		mav.addObject("LscxszDto", lscxsz);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 修改提交保存
	 */
	@RequestMapping(value="/query/modSaveTemporaryQuery")
	@ResponseBody
	public Map<String, Object> modSaveLscx(LscxszDto lscxszDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		lscxszDto.setXgry(user.getYhid());
		replace(lscxszDto);
		boolean isSuccess=lscxszService.update(lscxszDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
		return map;
	}
	
	/**
	 * 删除临时查询信息
	 */
	@RequestMapping(value="/query/delTemporaryQuery")
	@ResponseBody
	public Map<String,Object> delLscx(LscxszDto lscxszDto,HttpServletRequest request){
		User user = getLoginInfo(request);
		lscxszDto.setScry(user.getYhid());
		boolean isSuccess=lscxszService.delete(lscxszDto);
		Map<String,Object> map = new HashMap<>();
		map.put("status", isSuccess?"success":"fail");
		map.put("message", isSuccess?xxglService.getModelById("ICOM00003").getXxnr():xxglService.getModelById("ICOM00004").getXxnr());
		return map;
	}
	
	/**
	 * 执行页面
	 */
	@RequestMapping(value="/query/executeTemporaryQuery")
	public ModelAndView getExecutePage(LscxszDto lscxszDto) {
		ModelAndView mav=new ModelAndView("systemmain/query/query_execute");
		LscxszDto lscxsz=lscxszService.selectLscxByCxid(lscxszDto);
		mav.addObject("LscxszDto", lscxsz);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 获取临时查询结果
	 */
	@RequestMapping(value="/query/executeQuery")
	@ResponseBody
	public Map<String,Object> getQueryResult(LscxszDto lscxszDto){
		Map<String,Object> result=new HashMap<>();
		List<Map<String,Object>> listmap=new ArrayList<>();
		List<String> headList=new ArrayList<>();
		//获取临时查询结果
		try {
			LscxszDto lscxxx=lscxszService.selectLscxByCxid(lscxszDto);
			if(lscxxx.getCxdm().toLowerCase().contains("＞")) {
				lscxxx.setCxdm(lscxxx.getCxdm().replaceAll("＞", ">"));
			}
			if(lscxxx.getCxdm().toLowerCase().contains("＜")) {
				lscxxx.setCxdm(lscxxx.getCxdm().replaceAll("＜", "<"));
			}
			lscxszDto.setCxdm(lscxxx.getCxdm());
			List<HashMap<String, Object>> lscxresult=lscxszService.getQueryResult(lscxszDto);
			//遍历list
			for (HashMap<String, Object> stringObjectHashMap : lscxresult) {
				if(stringObjectHashMap == null)
					continue;
				//遍历hashmap，取key和value
				Map<String, Object> map = new HashMap<>(stringObjectHashMap);
				listmap.add(map);
			}
			
			Statement stmt = CCJSqlParserUtil.parse(lscxszDto.getCxdm());
			Select select = (Select) stmt;
			SelectBody selectBody = select.getSelectBody();
			if (selectBody instanceof PlainSelect) {
				PlainSelect plainSelect = (PlainSelect) selectBody;
				List<SelectItem> selectitems = plainSelect.getSelectItems();
				for (SelectItem selectitem : selectitems) {
					String itemString = selectitem.toString();
					if (StringUtil.isNotBlank(itemString)) {
						String[] s_item = itemString.split(" ");
						String t_item = s_item[s_item.length - 1];
						headList.add(t_item.substring(!t_item.contains(".") ? 0 : (t_item.indexOf(".") + 1)));
					}
				}
			}
		} catch (Exception e) {
			result.put("error", "SQL有误!");
			log.error(e.toString());
		}
		result.put("resultList", listmap);
		result.put("headList", headList);
		return result;
	}
	
	
	@RequestMapping(value = "/query/distinguishTemporaryQuery")
	public ModelAndView configuser(LscxqxDto lscxqxDto){
		ModelAndView mav = new ModelAndView("systemmain/query/query_configuser");
		mav.addObject("lscxqxDto", lscxqxDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	
	/**
	 * 可选用户
	 */
	@RequestMapping(value ="/configuser/pagedataListSelectUser")
	@ResponseBody
	public Map<String,Object> listUnSelectUser(UserDto userDto){
		List<UserDto> t_List;
		if("USER".equals(userDto.getLx())){
			t_List = lscxqxService.getPagedOptionalList(userDto);
		}else{
			t_List = lscxqxService.getPagedOptionalRoleList(userDto);
		}
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", userDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	
	/**
	 * 已选用户
	 */
	@RequestMapping(value ="/configuser/pagedataSelectedUser")
	@ResponseBody
	public Map<String,Object> listSelectedUser(UserDto userDto){
		List<UserDto> t_List = lscxqxService.getPagedSelectedList(userDto);
		//Json格式的要求{total:22,rows:{}}
		//构造成Json的格式传递
		Map<String,Object> result = new HashMap<>();
		result.put("total", userDto.getTotalNumber());
		result.put("rows", t_List);
		return result;
	}
	
	
	
	/**
	 * 添加用户
	 */
	@RequestMapping(value ="/configuser/pagedataToSelected")
	@ResponseBody
	public Map<String,Object> toSelected(LscxqxDto lscxqxDto){
		try{
			User user = getLoginInfo();
			lscxqxDto.setLrry(user.getYhid());
			Map<String,Object> map = new HashMap<>();			
			LscxszDto lscxszDto = new LscxszDto();
			lscxszDto.setCids(lscxqxDto.getCids());
			boolean iSuccess = lscxszService.queryByCxid(lscxszDto);
			if(iSuccess) {
				boolean result = lscxqxService.toSelected(lscxqxDto);
				map.put("status", result?"success":"fail");
				map.put("message", result?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			}else {
				map.put("status", "fail");
				map.put("message", xxglService.getModelById("ICOM99060").getXxnr());
			}
			return map;
		}catch(Exception e){
			Map<String,Object> map = new HashMap<>();
			map.put("status", "fail");
			map.put("message", e.getMessage());
			return map;
		}
	}
	
	
	/**
	 * 去除用户
	 */
	@RequestMapping(value ="/configuser/pagedataToOptional")
	@ResponseBody
	public Map<String,Object> toOptional(LscxqxDto lscxqxDto){
		try{
			boolean result = lscxqxService.toOptional(lscxqxDto);
			Map<String,Object> map = new HashMap<>();
			map.put("status", result?"success":"fail");
			map.put("message", result?xxglService.getModelById("ICOM00001").getXxnr():xxglService.getModelById("ICOM00002").getXxnr());
			return map;
		}catch(Exception e){
			Map<String,Object> map = new HashMap<>();
			map.put("status", "fail");
			map.put("message", e.getMessage());
			return map;
		}
	}
	
	/**
	 * 根据类别区分查询临时查询信息的页面
	 */
	@RequestMapping(value ="/query/pageListByType")
	public ModelAndView getStaticPageByType(LscxszDto lscxszDto){
		ModelAndView mav=new ModelAndView("systemmain/statistics/statistics_common");
		try{
			User user = getLoginInfo();
			lscxszDto.setYhid(user.getYhid());
			
			List<String> cxqfdms = new ArrayList<>();
			cxqfdms.add("STATISTICS");
			lscxszDto.setCxqfdms(cxqfdms);
			lscxszDto.setSortName("px");
			lscxszDto.setSortOrder("asc");
			lscxszDto.setPageSize(100);
			lscxszDto.setPageNumber(1);
			lscxszDto.setPageStart(0);
			List<LscxszDto> lscxszlist=lscxszService.getPagedSimpDtoListByLimt(lscxszDto);
			
			mav.addObject("lscxszlist",lscxszlist);
			mav.addObject("urlPrefix", urlPrefix);
		}catch(Exception e){
			log.error(e.getLocalizedMessage());
		}
		return mav;
	}
	
	/**
	 * 根据类别区分查询临时查询信息的数据
	 */
	@RequestMapping(value ="/query/pageGetListByType")
	@ResponseBody
	public Map<String,Object> getStaticDataByType(LscxszDto lscxszDto){
		Map<String,Object> result = new HashMap<>();
		try{
			User user = getLoginInfo();
			lscxszDto.setYhid(user.getYhid());
			lscxszDto.setJsid(user.getDqjs());
			List<String> cxqfdms = new ArrayList<>();
			cxqfdms.add("STATISTICS");
			lscxszDto.setCxqfdms(cxqfdms);
			lscxszDto.setSortName("px");
			lscxszDto.setSortOrder("asc");
			lscxszDto.setPageSize(100);
			lscxszDto.setPageNumber(1);
			lscxszDto.setPageStart(0);
			
			List<LscxszDto> lscxszlist=lscxszService.getPagedDtoListByLimt(lscxszDto);
			
			if(lscxszlist!=null && !lscxszlist.isEmpty()) {
				for (LscxszDto t_lscxszDto : lscxszlist) {
					// 处理查询sql
					t_lscxszDto.setCxtjs(lscxszDto.getCxtjs());
					String csdmone = lscxszService.dealStatisticsQuerySql(t_lscxszDto, user.getYhid());
					t_lscxszDto.setCxdm(csdmone);
					String cxdm = lscxszService.dealQuerySql(t_lscxszDto, user.getYhid());
					t_lscxszDto.setCxdm(cxdm);

					List<HashMap<String, Object>> listResult = lscxszService.getQueryResult(t_lscxszDto);
					//为了页面使用，删除大容量字段
					//t_lscxszDto.setCxdm("");
					t_lscxszDto.setBz("");
					t_lscxszDto.setCxmc("");
					// 判断类型处理
					if ("Table".equals(t_lscxszDto.getXsfsdm())) { // 表格
						List<Map<String, Object>> listmap = new ArrayList<>();
						List<String> headList = new ArrayList<>();
						// 遍历list
						for (HashMap<String, Object> stringObjectHashMap : listResult) {
							if(stringObjectHashMap == null)
								continue;
							// 遍历hashmap，取key和value
							Map<String, Object> t_map = new HashMap<>(stringObjectHashMap);
							listmap.add(t_map);
						}

						Statement stmt = CCJSqlParserUtil.parse(t_lscxszDto.getCxdm());
						Select select = (Select) stmt;
						SelectBody selectBody = select.getSelectBody();
						if (selectBody instanceof PlainSelect) {
							PlainSelect plainSelect = (PlainSelect) selectBody;
							List<SelectItem> selectitems = plainSelect.getSelectItems();
							for (SelectItem selectitem : selectitems) {
								String itemString = selectitem.toString();
								if (StringUtil.isNotBlank(itemString)) {
									String[] s_item = itemString.split(" ");
									String t_item = s_item[s_item.length - 1];
									headList.add(t_item.substring(!t_item.contains(".") ? 0 : (t_item.indexOf(".") + 1)));
								}
							}
						}
						Map<String, Object> subresult = new HashMap<>();
						subresult.put("resultList", listmap);
						subresult.put("headList", headList);
						result.put(t_lscxszDto.getCxbm(), subresult);
					} else if ("Pie".equals(t_lscxszDto.getXsfsdm())) { // 饼状图
						Map<String, Object> subresult = new HashMap<>();
						subresult.put("resultList", listResult);
						result.put(t_lscxszDto.getCxbm(), subresult);
					} else if ("Bar".equals(t_lscxszDto.getXsfsdm())) { // 柱状图
						Map<String, Object> subresult = new HashMap<>();
						subresult.put("resultList", listResult);
						result.put(t_lscxszDto.getCxbm(), subresult);
					}
				}
			}
			result.put("lscxszlist", lscxszlist);
			result.put("urlPrefix", urlPrefix);
		}catch(Exception e){
			log.error(e.getLocalizedMessage());
		}
		return result;
	}

	/**
	 * 日志SQL转换页面
	 */
	@RequestMapping(value="/query/pagedataConvertSqlPage")
	public ModelAndView pagedataConvertSqlPage() {
		ModelAndView mav=new ModelAndView("systemmain/query/sql_convert");
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	/**
	 * 日志SQL转换
	 */
	@RequestMapping(value="/query/pagedataConvertSql")
	@ResponseBody
	public Map<String,Object>  pagedataConvertSql(String before) {
		Map<String,Object> map = new HashMap<>();
		// 获取带问号的SQL语句
		int statementStartIndex = before.indexOf("Preparing: ");
		if (statementStartIndex < 0){
			map.put("status", "fail");
			map.put("message", "转换前的数据不符合日志格式！");
			return map;
		}
		int statementEndIndex = before.indexOf("\n");
		if(statementEndIndex < "Preparing: ".length())
		{
			statementEndIndex = before.indexOf("\n", statementEndIndex+2);
		}
		String statementStr = before.substring(statementStartIndex + "Preparing: ".length(), statementEndIndex);
		//获取参数
		int parametersStartIndex = before.indexOf("Parameters: ");
		int parametersEndIndex = before.length();
		String parametersStr = before.substring(parametersStartIndex + "Parameters: ".length(), parametersEndIndex);
		if(parametersStr.contains("\n")){
			int i = parametersStr.indexOf("\n");
			parametersStr = parametersStr.substring(0, i);
		}
		String[]  parametersSplit = parametersStr.split(",");
		String[]  statementSplit = statementStr.split("\\?");
		StringBuilder sql= new StringBuilder();
		for (var i = 0; i < statementSplit.length; i++) {
			sql.append(statementSplit[i]);
			for (var j = 0; j < parametersSplit.length; j++) {
				if(i==j){
					if(parametersSplit[j].contains("(") && parametersSplit[j].contains(")")){
						// 如果数据中带括号将使用其他逻辑
						String tempStr = parametersSplit[j].substring(0, parametersSplit[j].indexOf("("));
						// 获取括号中内容
						String typeStr = parametersSplit[j].substring(parametersSplit[j].indexOf("(")+1, parametersSplit[j].indexOf(")"));
						// 如果为字符类型或时间 加''
						if ( "String".equals(typeStr) || "Timestamp".equals(typeStr)) {
							sql.append("'").append(tempStr.trim()).append("'");
						} else {
							// 数值类型
							sql.append(tempStr.trim());
						}
					}else{
						sql.append(parametersSplit[j]);
					}
					break;
				}
			}
		}
		sql = new StringBuilder(sql.toString().replaceAll("＞", ">").replaceAll("＜", "<"));
		map.put("status", "success");
		map.put("message", "转换成功！");
		map.put("sql", sql.toString());
		return map;
	}


	/**
	 * 日志SQL转换页面
	 */
	@RequestMapping(value="/query/pagedataPCRSangerPage")
	public ModelAndView pagedataPCRSangerPage() {
		ModelAndView mav = new ModelAndView("systemmain/utilpage/PCRSangerPage");
		mav.addObject("ywlx","PCR_SANGER_REPORT_DOC");
		return mav;
	}

	/**
	 * 日志SQL转换
	 */
	@RequestMapping(value="/query/pagedataPCRSangerDeal")
	@ResponseBody
	public Map<String, Object> pagedataPCRSangerDeal(String fjid, String ywlx) {
		Map<String, Object> map = new HashMap<>();
		Object o_file = redisUtil.hget("IMP_:_" + fjid, "wjlj");
		String wjlj = String.valueOf(o_file == null ? "" : o_file);
		if(StringUtil.isBlank(wjlj)){
			map.put("status", "fail");
			map.put("message", "<h3>错误信息：(因有错误，文件无法导入到系统中)</h3><br>");
			return map;
		}
		//获取模板文件
		List<JcsjDto> jcsjDtos = redisUtil.lgetDto("List_matridx_jcsj:"+BasicDataTypeEnum.REPORT_TEMEPLATE.getCode());
		List<JcsjDto> collect = jcsjDtos.stream().filter(e -> "PCRSANGER".equals(e.getCsdm())).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(collect)){
			map.put("status", "fail");
			map.put("message", "<h3>错误信息：模板文件不存在，请在“报告模板”基础数据中添加参数代码为“PCRSANGER”的模板</h3><br>");
			return map;
		}
		JcsjDto jc_mb = collect.get(0);
		FjcfbDto fjcfbDto = new FjcfbDto();
		fjcfbDto.setYwid(jc_mb.getCsid());
		fjcfbDto.setYwlx("IMP_REPORT_C_TEMEPLATE");
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		if (CollectionUtils.isEmpty(fjcfbDtos) || StringUtil.isBlank(fjcfbDtos.get(0).getWjlj())){
			map.put("status", "fail");
			map.put("message", "<h3>错误信息：模板文件不存在，请在“报告模板”基础数据中添加参数代码为“PCRSANGER”的模板</h3><br>");
			return map;
		}
		PcrSangerUtil pcrSangerUtil = new PcrSangerUtil();
		String exportFilePath = null;
		try {
			exportFilePath = pcrSangerUtil.ExportWord(wjlj, wjlj.substring(0,wjlj.lastIndexOf(".")), new DBEncrypt().dCode(fjcfbDtos.get(0).getWjlj()), ywlx, tempFilePath, releaseFilePath);
		} catch (Exception e) {
			map.put("status", "fail");
			map.put("message", "<h3>错误信息："+e.getMessage()+"</h3><br>");
			return map;
		}
		map.put("status", "success");
		map.put("message", "转换成功！");
		map.put("wjlj", exportFilePath);
		return map;
	}

}
