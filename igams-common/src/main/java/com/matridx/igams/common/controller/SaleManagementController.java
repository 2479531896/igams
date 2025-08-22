package com.matridx.igams.common.controller;

import com.matridx.igams.common.dao.entities.LscxszDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.ILscxqxService;
import com.matridx.igams.common.service.svcinterface.ILscxszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.RedisXmlConfig;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateTimeUtil;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectItem;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sale")
public class SaleManagementController extends BaseController{
	
	@Autowired
	ILscxszService lscxszService;
	@Autowired
	IXxglService xxglService;
	@Autowired
	IJcsjService jcsjService;
	@Autowired
	ILscxqxService lscxqxService;
	@Value("${matridx.prefix.urlprefix:}")
	private String urlPrefix;
	@Autowired
	RedisXmlConfig redisXmlConfig;
	/**
	 * 导出文件存放路径
	 */
	@Value("${matridx.file.exportFilePath}")
	private final String exportFilePath = null;
	@Override
	public String getPrefix() {
		return urlPrefix;
	}
	
	private final Logger log = LoggerFactory.getLogger(TemporaryQueryController.class);

	/**
	 * 临时查询列表页面
	 */
	@RequestMapping(value="/query/pageListQuery")
	public ModelAndView getTemporaryQueryPageList() {
		return new ModelAndView("systemmain/sale/sale_list");
	}
	
	/**
	 * 临时查询列表
	 */
	@RequestMapping(value="/query/pageGetListQuery")
	@ResponseBody
	public Map<String,Object> pageGetListQuery(LscxszDto lscxszDto){
		Map<String,Object> result = new HashMap<>();
		User user = getLoginInfo();
		lscxszDto.setYhid(user.getYhid());
		lscxszDto.setJsid(user.getDqjs());
		lscxszDto.setAllflg("1");
		List<String> cxqfdms = new ArrayList<>();
		cxqfdms.add("ALL");
		cxqfdms.add("LIMIT");
		lscxszDto.setCxqfdms(cxqfdms);
		
		List<LscxszDto> lscxszlist=lscxszService.getPagedDtoListByLimt(lscxszDto);
		result.put("total", lscxszDto.getTotalNumber());
		result.put("rows", lscxszlist);
		
		return result;
	}
	
	/**
	 * 执行页面
	 */
	@RequestMapping(value="/query/executeTemporaryQuery")
	public ModelAndView getExecutePage(LscxszDto lscxszDto) {
		ModelAndView mav=new ModelAndView("systemmain/sale/sale_execute");
		LscxszDto t_lscxszDto = lscxszService.selectLscxByCxid(lscxszDto);
		if(t_lscxszDto!= null && StringUtil.isNotBlank(t_lscxszDto.getCxdm())) {
			List<Map<String,String>> fieldList = lscxszService.getFieldList(t_lscxszDto.getCxdm());
			if(t_lscxszDto.getCxmc().contains("华山")){
				mav.addObject("xsbj", "1");
			}
			mav.addObject("fieldList",fieldList);
		}
		mav.addObject("LscxszDto", t_lscxszDto);
		mav.addObject("urlPrefix",urlPrefix);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}

	/**
	 * 下载页面
	 */
	@RequestMapping(value="/query/downloadTemporaryQuery")
	public ModelAndView getDownloadPage(LscxszDto lscxszDto) {
		ModelAndView mav = new ModelAndView("systemmain/sale/sale_download");
		LscxszDto t_lscxszDto = lscxszService.selectLscxByCxid(lscxszDto);
		if (t_lscxszDto != null && StringUtil.isNotBlank(t_lscxszDto.getCxdm())) {
			List<Map<String, String>> fieldList = lscxszService.getFieldList(t_lscxszDto.getCxdm());
			mav.addObject("fieldList", fieldList);
		}
		mav.addObject("LscxszDto", t_lscxszDto);
		mav.addObject("urlPrefix", urlPrefix);
		return mav;
	}
	
	/**
	 * 获取临时查询结果
	 */
	@RequestMapping(value = "/query/pagedataExecuteQuery")
	@ResponseBody
	public Map<String, Object> getQueryResult(LscxszDto lscxszDto, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<>();
		//获取用户信息
		User user = getLoginInfo(request);
		String yhid = user.getYhid();
		// 获取临时查询结果
		try {
			LscxszDto t_lscxszDto = lscxszService.selectLscxByCxid(lscxszDto);
			result.put("lscxszDto", t_lscxszDto);
			// 处理查询sql
			t_lscxszDto.setCxtjs(lscxszDto.getCxtjs());
			String cxdmone = lscxszService.dealStatisticsQuerySql(t_lscxszDto, yhid);
			t_lscxszDto.setCxdm(cxdmone);
			String cxdm = lscxszService.dealQuerySql(t_lscxszDto, yhid);
			lscxszDto.setCxdm(cxdm);

			List<HashMap<String, Object>> listResult = lscxszService.getQueryResult(lscxszDto);

			// 判断类型处理
			if("Table".equals(t_lscxszDto.getXsfsdm())) { // 表格
				List<Map<String, Object>> listmap = new ArrayList<>();
				List<String> headList = new ArrayList<>();
				// 遍历list
				for (HashMap<String, Object> stringObjectHashMap : listResult) {
					if(stringObjectHashMap == null)
						continue;
					// 遍历hashmap，取key和value
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
				result.put("resultList", listmap);
				result.put("headList", headList);
			}else{
				result.put("resultList", listResult);
			}
		} catch (Exception e) {
			result.put("error", "SQL有误!");
			log.error(e.toString());
		}
		return result;
	}
	/**
	 * 获取临时查询结果
	 */
	@RequestMapping(value = "/query/pagedataDownloadQuery")
	@ResponseBody
	public Map<String, Object> getDownloadResult(LscxszDto lscxszDto, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<>();
		//获取用户信息
		User user = getLoginInfo(request);
		String yhid = user.getYhid();
		// 获取临时查询结果
		LscxszDto t_lscxszDto = lscxszService.selectLscxByCxid(lscxszDto);
		result.put("lscxszDto", t_lscxszDto);
		// 处理查询sql
		t_lscxszDto.setCxtjs(lscxszDto.getCxtjs());
		String cxdmone = lscxszService.dealStatisticsQuerySql(t_lscxszDto, yhid);
		t_lscxszDto.setCxdm(cxdmone);
		String cxdm = lscxszService.dealQuerySql(t_lscxszDto, yhid);
		lscxszDto.setCxdm(cxdm);
		return dealExcel(result,lscxszDto);
	}

	public Map<String,Object> dealExcel(Map<String,Object> result,LscxszDto lscxszDto){
		try {
			List<HashMap<String, Object>> listResult = lscxszService.getQueryResult(lscxszDto);
			String fileName = String.valueOf(new Date().getTime());
			String fileType = ".xls";
//			if(StringUtil.isNotBlank(lscxszDto.getFileType())&&lscxszDto.getFileType().equals("1")){
//				fileType = ".csv";
//			}
			fileName += fileType;
			String currentDay = DateTimeUtil.getFormatDay(new Date());
			File filepath = new File(exportFilePath + currentDay.substring(0, 4) + "/" + currentDay.substring(0, 6) + "/" + currentDay.substring(0, 8) + "/");
			if(!filepath.exists()){
				filepath.mkdirs();
			}
			Workbook workbook = new XSSFWorkbook(); // 创建Excel工作簿
			Sheet sheet = workbook.createSheet("Sheet1"); // 创建工作表
			int rowNum = 0;
			List<Map<String, Object>> listmap = new ArrayList<>();
			List<String> headList = new ArrayList<>();
			// 遍历list
			for (HashMap<String, Object> stringObjectHashMap : listResult) {
				if(stringObjectHashMap == null)
					continue;
				// 遍历hashmap，取key和value
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
			if (!CollectionUtils.isEmpty(headList)){
				Row row = sheet.createRow(rowNum++); // 创建新行
				int cellNum = 0;
				for (String s : headList) {
					Cell cell = row.createCell(cellNum++); // 创建单元格
					cell.setCellValue(s); // 设置单元格的值
				}
			}
			for (HashMap<String, Object> data : listResult) {
				Row row = sheet.createRow(rowNum++); // 创建新行
				int cellNum = 0;
				for (String s : headList) {
					Cell cell = row.createCell(cellNum++); // 创建单元格
					cell.setCellValue(data.get(s) != null ? data.get(s).toString() :""); // 设置单元格的值
				}
			}
			try (FileOutputStream outputStream = new FileOutputStream(filepath + fileName)) {
				workbook.write(outputStream); // 将工作簿写入文件
			}
			workbook.close(); // 关闭工作簿释放资源
			log.error(filepath + fileName);
			result.put("wjlj", filepath + fileName);
			result.put("wjm", fileName);
			result.put("temporary", "1");
		} catch (Exception e) {
			result.put("error", "SQL有误!");
			log.error(e.toString());
		}
		return result;
	}

	/**
	 * 为华山定制的下载接口
	 *
	 * @return
	 */
	@RequestMapping(value="/query/pagedataDownloadHuashanData")
	@ResponseBody
	public void downloadHuashanData(LscxszDto lscxszDto,HttpServletResponse response){
		User user = getLoginInfo();
		Map<String, Object> result = new HashMap<>();
		OutputStream out = null;
		FileOutputStream fos = null;
		XSSFWorkbook wb = null;
		try {
			LscxszDto t_lscxszDto = lscxszService.selectLscxByCxid(lscxszDto);
			if (t_lscxszDto != null && StringUtil.isNotBlank(t_lscxszDto.getCxdm())) {
				String cxdm = lscxszService.dealDownloadHuashanData(t_lscxszDto);
				t_lscxszDto.setCxdm(cxdm);
				// 处理查询sql
				t_lscxszDto.setCxtjs(lscxszDto.getCxtjs());
				String cxdmone = lscxszService.dealStatisticsQuerySql(t_lscxszDto, user.getYhid());
				t_lscxszDto.setCxdm(cxdmone);
				cxdm = lscxszService.dealQuerySql(t_lscxszDto, user.getYhid());
				lscxszDto.setCxdm(cxdm);
				List<HashMap<String, Object>> listResult = lscxszService.getQueryResult(lscxszDto);
				List<String> headList = new ArrayList<>();
				//创建并写入文件
				String fileName = String.valueOf(new Date().getTime());
				String fileType = ".xlsx";
				
				fileName += fileType;
				String currentDay = DateTimeUtil.getFormatDay(new Date());
				File filepath = new File(exportFilePath + currentDay.substring(0, 4) + "/" + currentDay.substring(0, 6) + "/" + currentDay.substring(0, 8) + "/");
				if(!filepath.exists()){
					filepath.mkdirs();
				}
				
				String file_Path = filepath.getPath() + "/" + fileName;
				
				try{
					
			        //设置Content-Disposition  
			        response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
			        response.setContentType("application/octet-stream; charset=utf-8");
			        //response.setHeader("Content-Disposition", "attachment;filename="+fileName);  
			        //读取目标文件，通过response将目标文件写到客户端  
			        //获取目标文件的绝对路径  
			        //读取文件  ;
			        out = response.getOutputStream();

			        //创建工作表
					fos = new FileOutputStream(file_Path);
					wb = new XSSFWorkbook();// keep 10000 rows in memory, exceeding rows will be flushed to disk
					Sheet sheet =  wb.createSheet();//创建sheet
					wb.setSheetName(0, "病原结果");
					
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
			        
					for(int curRow=1;curRow<listResult.size();curRow++){
						if(curRow==1){
							//设置标题行
							Row headRow = sheet.createRow(0);
							for(int l =0;l<headList.size();l++){
								Cell headCell = headRow.createCell(l);
								XSSFRichTextString hts=new XSSFRichTextString(headList.get(l));
								headCell.setCellValue(hts);
							}
							//冻结第一行全部列
							sheet.createFreezePane(0, 1);
						}
						HashMap<String, Object> rowInfo= listResult.get(curRow);
						Row row = sheet.createRow(curRow);
						for(int x =0;x<headList.size();x++){
							Cell cell = row.createCell(x);
							XSSFRichTextString hts=new XSSFRichTextString((String)rowInfo.get(headList.get(x)));
							cell.setCellValue(hts);
						}
					}

					//输出更新缓存到文件
					fos.flush();
				}catch(Exception e){
					log.error(e.getMessage());
				}
				wb.write(fos);
				fos.flush();
				fos.close();
				wb.close();
				//读取文件用于下载
				InputStream in = null; 
				in= new FileInputStream(file_Path);
				
				//创建缓冲区
		        byte[] buffer = new byte[1024];
		        int len;
		        //循环将输入流中的内容读取到缓冲区当中
		        while((len=in.read(buffer))>0){
		        	//输出缓冲区的内容到浏览器，实现文件下载
		        	out.write(buffer, 0, len);
		        }
		        //写文件  
		        int b;  
		        while((b=in.read())!= -1)  
		        {  
		            out.write(b);  
		        }  
		        out.flush();
		        in.close();
		        out.close();
			}
		}catch (Exception e){
			result.put("error", "SQL有误");
			log.error("SQL有误");
		}finally{
			try{
				if(out != null){
					out.close();
				}
			}catch(Exception ex){
				log.error(ex.getMessage());
			}
		}
	}

}

