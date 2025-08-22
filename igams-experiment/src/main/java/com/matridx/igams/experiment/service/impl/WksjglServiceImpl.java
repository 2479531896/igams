package com.matridx.igams.experiment.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.*;
import com.matridx.igams.experiment.dao.post.IWksjglDao;
import com.matridx.igams.experiment.service.svcinterface.ICxyxxService;
import com.matridx.igams.experiment.service.svcinterface.IWkglService;
import com.matridx.igams.experiment.service.svcinterface.IWksjglService;
import com.matridx.igams.experiment.service.svcinterface.IWksjmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateTimeUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class WksjglServiceImpl extends BaseBasicServiceImpl<WksjglDto, WksjglModel, IWksjglDao> implements IWksjglService{

	@Autowired
	IWksjmxService wksjmxService;


	@Autowired
	RedisUtil redisUtil;

	@Autowired
	ICxyxxService cxyxxService;

	private final Logger log = LoggerFactory.getLogger(WksjglServiceImpl.class);
	/**
	 * 导出文件存放路径
	 */
	@Value("${matridx.file.exportFilePath}")
	private final String exportFilePath = null;
	/**
	 * 新增文库上机管理信息
	 */
	@Override
	public boolean insertDto(WksjglDto wksjglDto) {
		wksjglDto.setWksjid(StringUtil.generateUUID());
		int result=dao.insert(wksjglDto);
		return result > 0;
	}

	/**
	 * 根据文库ID获取文库上机信息
	 */
	public WksjglDto getDtoByWkid(WksjglDto wksjglDto) {
		return dao.getDtoByWkid(wksjglDto);
	}


	@Override
	public Map<String,Object> getWksjmxInfo(CxyxxDto cxyxxDto,String nddw,String sfjyh,IWkglService wkglService){
		Map<String,Object>map=new HashMap<>();
		WksjglDto wksjglDto=new WksjglDto();
		wksjglDto.setWkid(cxyxxDto.getWkid());
		wksjglDto=dao.getDtoByWkid(wksjglDto);
		List<WksjmxDto> wksjmxList;
		WksjmxDto wksjmxDto=new WksjmxDto();
		String tjqz="";//体积前缀
		//如果是第一次上机,从文库导出获取
		if(wksjglDto==null) {
			WksjglDto wksjglDto_new = new WksjglDto();
			wksjmxDto.setWkid(cxyxxDto.getWkid());
			wksjmxList=wksjmxService.getListForWksj(wksjmxDto);
			WkglDto dto = wkglService.getDtoById(cxyxxDto.getWkid());
			if (StringUtil.isNotBlank(dto.getYqlx())){
				JcsjDto cxy = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.SEQUENCER_CODE.getCode(), dto.getYqlx());
				if (cxy!=null){
					CxyxxDto cxyxxDto_mc = new CxyxxDto();
					cxyxxDto_mc.setMc(cxy.getCsmc());
					CxyxxDto cyxxx = cxyxxService.getDto(cxyxxDto_mc);
					wksjglDto_new.setCxy(cyxxx.getMc());
					wksjglDto_new.setCxyid(cyxxx.getCxyid());
				}
			}
		}else {
			//如果不是则从文库上机明细表获取
			wksjmxDto.setWksjid(wksjglDto.getWksjid());
			wksjmxList=wksjmxService.getDtoList(wksjmxDto);
		}
		if(wksjmxList!=null && wksjmxList.size()>0){
			List<JcsjDto> jcsjList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.WECGATSAMPLE_TYPE.getCode());
			List<JcsjDto> zkjcsjList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.QUALITY_SAMPLE_SETTING.getCode());
			for(WksjmxDto dto:wksjmxList){
				String wkbm=dto.getWkbm();
				if(wksjglDto==null){
					if(!wkbm.contains("-DNA")&&!wkbm.contains("-RNA")&&!wkbm.contains("-Onco")
							&& (!"0".equals(sfjyh) || StringUtil.isBlank(sfjyh))){
						Optional<JcsjDto> jcsjDto= Optional.of(new JcsjDto());
						if(dto.getNbbm().startsWith("NC-") || dto.getNbbm().startsWith("PC-")|| dto.getNbbm().startsWith("DC-")){
							jcsjDto=zkjcsjList.stream().filter(item->item.getCsdm().equals(dto.getNbbm())).findFirst();
						}else{
							String nbbm=dto.getNbbm();
							String lastNbbm=nbbm.substring(nbbm.length()-1,nbbm.length());
							jcsjDto=jcsjList.stream().filter(item->item.getCsdm().equals(lastNbbm)).findFirst();
							if(jcsjDto.isEmpty()){//若没有匹配成功，则默认为该标本的样本类型为其他
								jcsjDto=jcsjList.stream().filter(item->item.getCsdm().equals("XXX")).findFirst();
							}
						}
						if(!jcsjDto.isEmpty()&&StringUtil.isNotBlank(jcsjDto.get().getCskz3())) {
							com.alibaba.fastjson.JSONObject jsonObject=JSON.parseObject(jcsjDto.get().getCskz3());
							if(jsonObject!=null){
								if(StringUtil.isNotBlank(tjqz)){//若体积前缀不为空，则拼接完全的key，从微信标本cskz3获取体积
									if(wkbm.contains("-TBtNGS")){
										dto.setTj(jsonObject.getString(tjqz+"_TBT_VOLUMN"));
									}else{
										dto.setTj(jsonObject.getString(tjqz+"_VOLUMN"));
									}
								}else{
									if(wkbm.contains("-TBtNGS")){
										dto.setTj(jsonObject.getString("TBT_VOLUMN"));
									}else{
										dto.setTj(jsonObject.getString("DEFAULT_VOLUMN"));
									}
								}
							}
							if(StringUtil.isNotBlank(nddw)){
								dto.setNddw(nddw);
							}
						}
						dto.setWknd("-1");
						dto.setDlnd("-1");
					}
				}
				if(StringUtil.isNotBlank(dto.getFcjcdm())){
					if("ADDDETECT".equals(dto.getFcjcdm())){
						dto.setYy("备注：加测申请");
					}else if("RECHECK".equals(dto.getFcjcdm())){
						dto.setYy("备注：复测申请");
					}else if("REM".equals(dto.getFcjcdm())){
						dto.setYy("备注：加测去人源申请");
					}else if("PK".equals(dto.getFcjcdm())){
						dto.setYy("备注：PK申请");
					}else if("LAB_RECHECK".equals(dto.getFcjcdm())){
						dto.setYy("备注：实验室复测申请");
					}else if("LAB_REM".equals(dto.getFcjcdm())){
						dto.setYy("备注：实验室加测去人源申请");
					}else if("LAB_ADDDETECT".equals(dto.getFcjcdm())){
						dto.setYy("备注：实验室加测申请");
					}
				}
			}
		}
		map.put("nddw",nddw);
		map.put("sfjyh",sfjyh);
		map.put("cxyxxDto",cxyxxDto);
		map.put("wksjmxList",wksjmxList);
		return map;
	}
	/**
	 * 保存文库上机信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean addSaveWksj(WksjglDto wksjglDto) {
		//先查询是否有上机信息若有则更新，否则则新增
		WksjglDto t_wksjglDto=dao.getDtoByWkid(wksjglDto);
		if(t_wksjglDto!=null){
			dao.update(wksjglDto);
		}else{
			//保存文库上机管理信息（新增）
			boolean saveWksjgl = insertDto(wksjglDto);
			if (!saveWksjgl)
				return false;
		}
		WksjmxDto wksjmxDto=new WksjmxDto();
		wksjmxDto.setWksjid(wksjglDto.getWksjid());
		if(StringUtils.isNotBlank(wksjglDto.getWksjid()))
			wksjmxService.deleteDtos(wksjmxDto);//删除文库上机明细
		//处理文库上机明细
		JSONObject wksjmx_json=JSONObject.fromObject(wksjglDto.getWksj_Json());
		String data_str=wksjmx_json.getString("data");
		List<Map<String, String>> wksjmxmaps= (List<Map<String, String>>) JSONArray.toCollection(JSONArray.fromObject(data_str),Map.class);

		List<WksjmxDto> wksjmxDtos=new ArrayList<>();

		//保存文库上机明细信息
		if(wksjmxmaps!=null && wksjmxmaps.size()>0){
			for (Map<String, String> wksjmxmap : wksjmxmaps) {
				wksjmxmap.put("wksjmxid", StringUtil.generateUUID());
				wksjmxmap.put("wksjid", wksjglDto.getWksjid());
				wksjmxmap.put("wknd", wksjmxmap.get("con.lib"));
				wksjmxmap.put("dnand", wksjmxmap.get("dnand"));
				wksjmxmap.put("dlnd", wksjmxmap.get("dlnd"));
				wksjmxmap.put("hsnd", wksjmxmap.get("con.dna"));
				wksjmxmap.put("tj", wksjmxmap.get("volume"));
				wksjmxmap.put("hbnd", wksjmxmap.get("con.sum"));
				wksjmxmap.put("nddw", wksjmxmap.get("original_con_unit"));
				wksjmxmap.put("yy", wksjmxmap.get("comment"));
				wksjmxmap.put("yjxjsjl", wksjmxmap.get("goal_reads"));
				wksjmxmap.put("xsbs", wksjmxmap.get("dilution_factor"));
				wksjmxmap.put("xmdm", wksjmxmap.get("xmdm"));
				WksjmxDto wksjmxDto_t = new WksjmxDto();
				wksjmxDto_t.setSyglid(wksjmxmap.get("syglid"));
				wksjmxDto_t.setXgry(wksjglDto.getXgry());
				wksjmxDtos.add(wksjmxDto_t);
			}
		}
		//插入文库上机明细数据
		boolean isAddSuccess=wksjmxService.insertDtos(wksjmxmaps);
		if(!isAddSuccess)
			return false;
		if(wksjmxDtos.size()>0){
			// 根据文库上机明细数据更新 送检实验管理表的数据
			wksjmxService.updateComputerTime(wksjmxDtos);
//			if(!result){
//				return false;
//			}
		}
		//更新fjsq表文库 编号
		wksjmxService.updateSyglByWksjid(wksjmxDto);
		return true;
	}

	@Override
	public List<WksjglDto> getWksjxxByWkid(String wkid){
		return dao.getWksjxxByWkid(wkid);
	}

	@Override
	public boolean deleteDto(WksjglDto wksjglDto) {
		return dao.deleteDto(wksjglDto);
	}

	@Override
	public boolean deleteDtoList(List<WksjglDto> delsjglList) {
		return dao.deleteDtoList(delsjglList);
	}

	/**
	 * 撤销删除文库上机管理（删除标记置为0）
	 */
	@Override
	public boolean cancelWksjglDeleteByWkid(List<WksjglDto> wksjglDtos) {
		return dao.cancelWksjglDeleteByWkid(wksjglDtos);
	}
	/**
	 * 更新测序仪
	 */
	@Override
	public boolean updateCxy(WksjglDto wksjglDto) {
		return dao.updateCxy(wksjglDto);
	}

	public boolean delList(List<WksjglDto> wksjglDtos){
		return dao.delList(wksjglDtos);
	}

	/**
	 * @param wksjglDto
	 * @param response
	 */
	@Override
	public Map<String, Object> downloadChipExport(WksjglDto wksjglDto, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<>();
		try {
			List<Map<String,Object>> list = dao.getChipByWkids(wksjglDto);
			for (Map<String, Object> map : list) {
				Object o = map.get("qcjson");
				if (o != null){
					Map<String,Object> jsonMap = JSON.parseObject(o.toString());
					map.put("jsonMap", jsonMap);
					map.put("sheetName",jsonMap.get("芯片号"));
				}
			}
			//
			String fileName = String.valueOf(new Date().getTime());
			String fileType = ".xls";
			fileName += fileType;
			String currentDay = DateTimeUtil.getFormatDay(new Date());
			File filepath = new File(exportFilePath + currentDay.substring(0, 4) + "/" + currentDay.substring(0, 6) + "/" + currentDay.substring(0, 8) + "/");
			if(!filepath.exists()){
				filepath.mkdirs();
			}
			Workbook workbook = new XSSFWorkbook(); // 创建Excel工作簿
			for (Map<String, Object> map : list) {
				String sheetName = (String) map.get("sheetName");
				Sheet sheet = workbook.createSheet(sheetName); // 创建工作表
				int rowNum = 0;
				if(map == null)
					continue;
				Row row = sheet.createRow(rowNum); // 创建新行
				rowNum++;
				Cell cell0 = row.createCell(0); // 创建单元格
				cell0.setCellValue("检测单位: ");
				Cell cell1 = row.createCell(1); // 创建单元格
				cell1.setCellValue(map.get("jcdw") != null ? map.get("jcdw").toString() : "");
				rowNum = setRowCellMap(map.get("jsonMap"), rowNum, sheet);
			}
			try (FileOutputStream outputStream = new FileOutputStream(filepath + fileName)) {
				workbook.write(outputStream); // 将工作簿写入文件
			}
			workbook.close(); // 关闭工作簿释放资源
			result.put("wjlj", filepath + fileName);
			result.put("wjm", fileName);
			log.error(filepath + fileName);
			chipDownloadExport(filepath+fileName,fileName,response);
		} catch (Exception e) {
			result.put("error", "导出失败!");
			log.error(e.toString());
		}
		return result;
	}

	public int setRowCellMap(Object jsonMap,Integer rowNum ,Sheet sheet){
		Row titleRow = sheet.createRow(rowNum);
		rowNum++;
		titleRow.createCell(0).setCellValue("指标");
		titleRow.createCell(1).setCellValue("数值");
		titleRow.createCell(2).setCellValue("分数");
		if(jsonMap != null && jsonMap instanceof Map){
			Map<String, Object> jsonMap_t = (Map<String, Object>) jsonMap;
			Map<String, Object> fs_detail = (Map<String, Object>)jsonMap_t.get("分数细节");
			String keys = "测序仪,实验室,芯片号,文库总数量,文库类型分类数量,Density,Q30,Intensity,PF,TotalReads,测序时长(小时),分析时长(分钟),下机数据量比例,质控品数据量情况,建库失败文库数量,Pooling偏差CV%,Pooling偏差文库数量(>30%),Pooling偏差文库数量(>40%),接头错误个数,质控品微生物背景,平均接头自连比例%,平均低质量的比例（LOWQ）,分数";
			String[] split = keys.split(",");
			for (String s : split) {
				Row row = sheet.createRow(rowNum);
				rowNum++;
				row.createCell(0).setCellValue(s);
				if ("质控品微生物背景".equals(s)){
					row.createCell(1).setCellValue(jsonMap_t.get(s) != null ? jsonMap_t.get(s).toString() : "{}");
				} else{
					row.createCell(1).setCellValue(jsonMap_t.get(s) != null ? jsonMap_t.get(s).toString() : "");
				}
				if ("分数".equals(s)){
					row.createCell(2).setCellValue(jsonMap_t.get(s) != null ? jsonMap_t.get(s).toString() : "0");
				} else {
					row.createCell(2).setCellValue(fs_detail.get(s) != null ? fs_detail.get(s).toString() : "0");
				}
			}
			rowNum++;
		}
		return rowNum;
	}
	public void chipDownloadExport(String filePath, String fileName, HttpServletResponse response) {
		InputStream in = null;
		OutputStream out = null;
		try {
			//设置Content-Disposition
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
			response.setContentType("application/octet-stream; charset=utf-8");
			//response.setHeader("Content-Disposition", "attachment;filename="+fileName);
			//读取目标文件，通过response将目标文件写到客户端
			//获取目标文件的绝对路径
			//读取文件
			in = new FileInputStream(filePath);
			out = response.getOutputStream();

			//创建缓冲区
			byte[] buffer = new byte[1024];
			int len;
			//循环将输入流中的内容读取到缓冲区当中
			while ((len = in.read(buffer)) > 0) {
				//输出缓冲区的内容到浏览器，实现文件下载
				out.write(buffer, 0, len);
			}
			//写文件
			int b;
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			out.flush();
			in.close();
			out.close();
		} catch (Exception e) {
			log.error(e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception ex) {
				log.error(ex.getMessage());
			}
		}
	}

}
