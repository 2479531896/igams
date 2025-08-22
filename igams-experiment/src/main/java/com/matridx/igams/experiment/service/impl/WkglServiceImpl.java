package com.matridx.igams.experiment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.matridx.igams.common.controller.CommonFileController;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.ExportTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.experiment.dao.entities.*;
import com.matridx.igams.experiment.dao.post.IWkglDao;
import com.matridx.igams.experiment.service.svcinterface.*;
import com.matridx.igams.experiment.util.PoolingUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WkglServiceImpl extends BaseBasicServiceImpl<WkglDto, WkglModel, IWkglDao> implements IWkglService{

	@Autowired
	IWkmxService wkmxService;
	@Autowired
	IWksjglService wksjglService;
	@Autowired
	IWksjmxService wksjmxService;
	@Autowired
	IFjcfbService fjcfbService;
	@Autowired
	private ISysjglService sysjglService;

	@Value("${matridx.file.exportFilePath:}")
	private String exportFilePath;
	@Value("${matridx.wechat.applicationurl:}")
	private String applicationurl;
	@Value("${matridx.fileupload.prefix}")
	private String prefix;
	@Value("${matridx.fileupload.releasePath}")
	private String releaseFilePath;
	@Autowired
	RedisUtil redisUtil;
	private final Logger log = LoggerFactory.getLogger(CommonFileController.class);
	
	/**
	 * 新增文库管理信息
	 */
	@Override
	public boolean insertDto(WkglDto wkglDto) {
		wkglDto.setWkid(StringUtil.generateUUID());
		int result=dao.insert(wkglDto);
		return result > 0;
	}
	/**
	 * 获取文库列表
	 */
	@Override
	public List<WkglDto> getPagedWkglDtoList(WkglDto wkglDto){
		return dao.getPagedWkglDtoList(wkglDto);
	}
	
	/**
	 * 删除文库信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteWkxxlist(WkglDto wkglDto) {
		boolean scwkglxx=dao.deleteWkglxx(wkglDto);
		WkmxDto wkmxDto=new WkmxDto();
		if(scwkglxx) {
			wkmxDto.setIds(wkglDto.getIds());
			wkmxDto.setScry(wkglDto.getScry());
			wkmxService.deleteWkmxxxlist(wkmxDto);
			//文库上机管理也需要进行删除
			WksjglDto wksjglDto=new WksjglDto();
			wksjglDto.setIds(wkglDto.getIds());
			List<WksjglDto> wksjglDtos=wksjglService.getDtoList(wksjglDto);
			if(!CollectionUtils.isEmpty(wksjglDtos)){
				wksjglService.delList(wksjglDtos);
				WksjmxDto wksjmxDto=new WksjmxDto();
				wksjmxDto.setIds(wksjglDtos.stream().map(WksjglDto::getWksjid).collect(Collectors.toList()));
				wksjmxService.deleteByWksjids(wksjmxDto);
			}
			//删除文库上机管理信息
			SysjglDto sysjglDto = new SysjglDto();
			sysjglDto.setIds(wkglDto.getIds());
			sysjglDto.setScbj("1");
			sysjglDto.setScry(wkglDto.getScry());
			sysjglService.deleteByIds(sysjglDto);
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 *  复制文件到指定文件夹
	 * @param exportPath   复制文件的文件夹地址
	 * @param path       复制的文件路径
	 */
	public void copyDocument(String exportPath,String path) {
		//拿到模板，保存到当前路径中
		BufferedInputStream bis = null;
		FileInputStream is;
		OutputStream os = null; //输出流
        byte[] buffer = new byte[1024];
        try {
    		//先确认目标文件是否存在，如果存在，则先进行删除
    		File file = new File(exportPath);
    		if(file.exists()) {
    			file.delete();
    		}
        	File templateFile = new File(path);
        	is = new FileInputStream(templateFile);
            os = new FileOutputStream(exportPath);
            bis = new BufferedInputStream(is);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                os.flush();
                i = bis.read(buffer);
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
			if (bis != null) {
				bis.close();
			}
			if (os != null) {
				os.flush();
				os.close();
			}

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
	
	/**
	 *  下载文件
	 */
	public void downloadDocument(String exportPath,String wjlx,HttpServletResponse response) {
		InputStream iStream;

        response.setHeader("content-type", "application/octet-stream");
        //指明为下载
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=" + wjlx);
        byte[] buffer = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null; //输出流
        try {
        	iStream= new FileInputStream(exportPath);
            os = response.getOutputStream();
            bis = new BufferedInputStream(iStream);
            int i = bis.read(buffer);
            while(i != -1){
                os.write(buffer);
                os.flush();
                i = bis.read(buffer);
            }
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
        	log.error(e.toString());
        }
        try {
			if (bis != null) {
				bis.close();
			}
			if (os != null) {
				os.flush();
				os.close();
			}
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	log.error(e.toString());
        }
	}

	/**
	 * 解析pooling数据
	 */
	@Override
	public Map<String, Object> analysisPooling(String filePath){
		// TODO Auto-generated method stub
		List<Map<String, String>> list= new ArrayList<>();
		File file = new File(filePath);
		if(!file.exists()){//判断是否存在当前文件夹，不存在则创建
			log.error("文件不存在！");
		}
		FileInputStream fis;
		XSSFWorkbook workbook;
		Map<String, Object> resultmap= new HashMap<>();
        try{
			fis = new FileInputStream(file);
			workbook = new XSSFWorkbook(fis);
			int numberOfSheets = workbook.getNumberOfSheets();
			if (numberOfSheets >1){
				for (int i = 0; i < numberOfSheets; i++){
					XSSFSheet sheet = workbook.getSheet("新公式");
					sheet.setForceFormulaRecalculation(true);
					int rowsize=rowCount(sheet);
					for (int j = 3; j < rowsize; j++){
						Map<String,String> map= new HashMap<>();
						XSSFRow row = sheet.getRow(j);
						map.put("wkmc", row.getCell(0).toString());//文库名称
						map.put("Quantity", row.getCell(1).toString());//quantity
						map.put("csxz", row.getCell(13).toString()); //参数修正
						map.put("yyjytj", row.getCell(15).toString().substring(0,5));//原液加液体积
						map.put("ysyjytj", row.getCell(16).toString().substring(0,5));//稀释液加样体积
						list.add(map);
					}
				}
			}else {
				XSSFSheet sheet = workbook.getSheet("全");
				sheet.setForceFormulaRecalculation(true);
				int rowsize=rowCount(sheet);
				for (int j = 3; j < rowsize; j++){
					Map<String,String> map= new HashMap<>();
					XSSFRow row = sheet.getRow(j);
					map.put("wkmc", row.getCell(0).toString());//文库名称 A列
					map.put("Quantity", row.getCell(1).toString());//quantity B列
					map.put("csxz", row.getCell(12).getRawValue().toString()); //参数修正 N列
					map.put("yyjytj", row.getCell(15).toString().substring(0,5));//原液加液体积 P列
					map.put("ysyjytj", row.getCell(16).toString().substring(0,5));//稀释液加样体积 Q列
					list.add(map);
				}
				resultmap.put("hyxs",sheet.getRow(10).getCell(20).getRawValue().toString());
				resultmap.put("hhwkcl",sheet.getRow(31).getCell(20).getRawValue().toString());
				resultmap.put("sjhh",sheet.getRow(5).getCell(25).getRawValue().toString());
			}
		} catch (Exception  e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        resultmap.put("pooling",list);
		return resultmap;
	}
	
	/**
	 * 获取有效数据
	 */
	private static int rowCount(Sheet sheet) {
		int rows=sheet.getLastRowNum();
		int count=0;
		for (int i = 3; i < rows; i++){
			count=i;
			Row row=sheet.getRow(i);
			if(row!=null) {
				if(StringUtils.isBlank(row.getCell(0).toString())) { //判断为空
					break;
				}
			}
		}
		return count;
	}
	
	/**
	 * 查询当天已建立的文库数量
	 */
	public WkglDto getCountByDay(WkglDto wkglDto) {
		return dao.getCountByDay(wkglDto);
	}
	
	/**
	 * 根据文库名称查询文库信息
	 */
	public WkglDto getDtoByWkmc(WkglDto wkglDto) {
		return dao.getDtoByWkmc(wkglDto);
	}
	
	/**
	 * 查询pooling表模板
	 */
	@Override
	public Map<String, Object> getPoolingTemplatePath(WkglDto wkglDto) {
		// TODO Auto-generated method stub
		return dao.getPoolingTemplatePath(wkglDto);
	}
	/**
	 * 查询文库t\q数量
	 */
	@Override
	public Map<String, Object> getPoolingLibraryCount(String wkid) {
		// TODO Auto-generated method stub
		return dao.getPoolingLibraryCount(wkid);
	}
	@Override
	public boolean updatesjph(WkglDto wkglDto) {
		// TODO Auto-generated method stub
		return dao.updatesjph(wkglDto);
	}
	//更新测序仪
	@Override
	public boolean updateYqlx(WkglDto wkglDto) {
		// TODO Auto-generated method stub
		return dao.updateYqlx(wkglDto);
	}

	/**
	 * 更新文库上机上传时间
	 */
	public boolean updateSjscsj(WkglDto wkglDto){
		return dao.updateSjscsj(wkglDto);
	}
	/**
	 * 更新文库上机上传时间
	 */
	public boolean updateWkxxByWkid(WkglDto wkglDto){
		return dao.updateWkxxByWkid(wkglDto);
	}

	/**
	 * 合并文库，需要把原始文库置为停用标记
	 * @param wkglDto
	 * @return
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean deleteMergeWkxx(WkglDto wkglDto) {
		boolean scwkglxx=dao.delMergeWkgl(wkglDto);
		WkmxDto wkmxDto=new WkmxDto();
		if(scwkglxx) {
			wkmxDto.setIds(wkglDto.getIds());
			wkmxDto.setScry(wkglDto.getScry());
			wkmxService.delMergeWkmxlist(wkmxDto);
			//为保留相应的试剂信息，实验试剂关联表的相应记录也置标记为2
			//删除文库上机管理信息
			SysjglDto sysjglDto = new SysjglDto();
			sysjglDto.setIds(wkglDto.getIds());
			sysjglDto.setScbj("2");
			sysjglDto.setScry(wkglDto.getScry());
			sysjglService.deleteByIds(sysjglDto);
			return true;
		}else {
			return false;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean cancelMergeWkxx(WkglDto wkglDto) {
		//将修改删除标记由2改为0
		boolean scwkglxx=dao.cancelMergeWkgl(wkglDto);
		WkmxDto wkmxDto=new WkmxDto();
		if(scwkglxx) {
			wkmxDto.setIds(wkglDto.getIds());
			wkmxService.cancelMergeWkmxlist(wkglDto);
			//为返回相应的试剂信息，实验试剂关联表的相应记录也置标记为0
			//删除文库上机管理信息
			SysjglDto sysjglDto = new SysjglDto();
			sysjglDto.setIds(wkglDto.getIds());
			sysjglDto.setScbj("0");
			sysjglDto.setScry(null);
			sysjglService.deleteByIds(sysjglDto);
			return true;
		}else {
			return false;
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean cancelMergeWksj(WkglDto wkglDto) {
		//处理文库上机表数据，复原被删除的多个wksjgl(scbj由2复原为0，数据为ywkid字段的wkid查找)，删除合并后新增的wksjgl(合并后的wkid)，删除新增的wksjmx数据(直接删除，非scbj删除)
		List<String> ywkidlist = Arrays.asList(wkglDto.getYwkid().split(","));
		List<WksjglDto> wksjglDtos = new ArrayList<>();
		for (String ywkid : ywkidlist){
			WksjglDto ywksjglDto = new WksjglDto();
			ywksjglDto.setWkid(ywkid);
			ywksjglDto.setXgry(wkglDto.getScry());
			wksjglDtos.add(ywksjglDto);
		}
		//1. 复原被删除的多个wksjgl
		wksjglService.cancelWksjglDeleteByWkid(wksjglDtos);
		//2. 删除新增的wksjmx数据
		WksjglDto wksjglDto = new WksjglDto();
		wksjglDto.setWkid(wkglDto.getWkid());
		WksjglDto wksjglDto_t = wksjglService.getDtoByWkid(wksjglDto);
		if (wksjglDto_t != null){
			WksjmxDto wksjmxDto = new WksjmxDto();
			wksjmxDto.setWksjid(wksjglDto_t.getWksjid());
			wksjmxService.deleteDtos(wksjmxDto);
		}
		//3. 删除合并后新增的wksjgl
		wksjglDto.setScry(wkglDto.getScry());
		wksjglService.delete(wksjglDto);
		return true;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean cancelMerge(WkglDto wkglDto) {
		Map<String, Object> map = new HashMap<>();
		//将wkid字段的数据进行合并后新文库和文库明细数据删除
		WkglDto wkglDto1 = new WkglDto();
		List<String> ids1 = new ArrayList<>();
		ids1.add(wkglDto.getWkid());
		wkglDto1.setIds(ids1);
		wkglDto1.setScry(wkglDto.getScry());
		boolean isSuccess = deleteWkxxlist(wkglDto1);
		//将ywkid字段的数据复原：删除标记由2改为0
		if(isSuccess) {
			WkglDto wkglDto2 = new WkglDto();
			List<String> ids2 = Arrays.asList(wkglDto.getYwkid().split(","));
			wkglDto2.setIds(ids2);
			cancelMergeWkxx(wkglDto2);
			//处理文库上机表数据，复原被删除的多个wksjgl(scbj由2复原为0，数据为ywkid字段的wkid查找)，删除合并后新增的wksjgl(合并后的wkid)，删除新增的wksjmx数据(直接删除，非scbj删除)
			cancelMergeWksj(wkglDto);
			//更改文库合并子数据相关的送检信息表的jt合并前
			List<String> sjidlist = new ArrayList<>();
			List<String> jtlist = new ArrayList<>();
			WkmxDto wkmxDto = new WkmxDto();
			wkmxDto.setIds(ids2);
			List<WkmxDto> wkmxDtoList = wkmxService.getWkmxByIds(wkmxDto);
            for (WkmxDto wkmx : wkmxDtoList ){
				if (StringUtil.isNotBlank(wkmx.getSjid())){
					sjidlist.add(wkmx.getSjid());
				}
				if (StringUtil.isNotBlank(wkmx.getJtxx())){
					jtlist.add(wkmx.getJtxx());
				}
			}
			// 调用igams-wechat中的接口，保存送检接头信息
			map.put("sjid", sjidlist);
			map.put("jt", jtlist);
			map.put("sfgxsyrq", "0");
			map.put("xgry",wkglDto.getScry());
			boolean result = wkmxService.insetSjjtxx(map);
			if (!result)
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();// 若为false回滚
			return result;
		}else{
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return isSuccess;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public boolean mergeSaveWk(WkmxDto wkmxDto) {
		String wkidnew = StringUtil.generateUUID();
		wkmxDto.setExtend_1("mergeSaveWk");
		wkmxDto.setExtend_2(wkidnew);//用于文库新增方法addSaveWkxx新增wkidnew的数据，便于后续文库上机管理使用wkidnew
		try {
			//将ids先拿出来避免 addSaveWkxx的处理更新掉ids
			List<String> wkidlist = new ArrayList<>(wkmxDto.getIds());
			//新增文库数据
			boolean isSuccess = wkmxService.addSaveWkxx(wkmxDto);
			if (isSuccess){
				//删除合并的选择数据,删除标记置为2
				WkglDto wkgl = new WkglDto();
				wkgl.setIds(wkmxDto.getIds());
				wkgl.setScry(wkmxDto.getLrry());//controller层的user.getYhid()存于录入人员，故这里取录入人员
				//删除文库前的相应文库
				deleteMergeWkxx(wkgl);
				//上级表处理
				List<WksjmxDto> addsjmxList = new ArrayList<>();
				List<WksjglDto> delsjglList = new ArrayList<>();
				boolean isInsert = false;//循环查原wkid的上机明细数据，有一个则true，一个没有则false，为true才进行文库上机部分的删除新增等修改
				for (String yid : wkidlist){//遍历原文库ID
//					有上机数据的，原上机管理删除置为2，新增sjgl（wksjid，wkid），新增sjmx（使用原sjmx数据存入，wksjid更改为新增的wksjid）
//					无上机数据的，原上机管理删除置为2，新增sjgl（wksjid，wkid），新增sjmx（使用getListForWksj数据存入，wksjid更改为新增的wksjid）
					WksjglDto wksjglDto = new WksjglDto();
					wksjglDto.setWkid(yid);
					wksjglDto = wksjglService.getDtoByWkid(wksjglDto);
					List<WksjmxDto> wksjmxDtoList;
					WksjmxDto wksjmxDto = new WksjmxDto();
					if (wksjglDto != null ){
						wksjmxDto.setWksjid(wksjglDto.getWksjid());
						wksjmxDtoList = wksjmxService.getDtoList(wksjmxDto);
						if (wksjmxDtoList != null && wksjmxDtoList.size()>0){
							//原sjgl删除置为2
							isInsert = true;
							wksjglDto.setScry(wkmxDto.getLrry());
							delsjglList.add(wksjglDto);
							addsjmxList.addAll(wksjmxDtoList);
						}
					}else{
						wksjmxDto.setWkid(yid);
						//如果不是则从文库上机明细表获取
						wksjmxDtoList=wksjmxService.getListForWksj(wksjmxDto);
						addsjmxList.addAll(wksjmxDtoList);
					}
				}
				//根据isInsert判断是否做处理
				if (isInsert){
					//删除原来的多条文库上机管理数据
					if (delsjglList.size()>0){
						wksjglService.deleteDtoList(delsjglList);
					}
					//新增一条新的文库上机管理数据
					WksjglDto wksjglDto1 = new WksjglDto();
					wksjglDto1.setWkid(wkidnew);
					String wksjid_new = StringUtil.generateUUID();
					wksjglDto1.setWksjid(wksjid_new);
					wksjglDto1.setLrry(wkmxDto.getLrry());
					wksjglService.insert(wksjglDto1);
                    //新增文库上机明细数据
					List<WksjmxDto> wksjmxDtos = new ArrayList<>();
					if (addsjmxList.size()>0){
						for (WksjmxDto wksjmx : addsjmxList){
							wksjmx.setWksjmxid(StringUtil.generateUUID());
							wksjmx.setWksjid(wksjid_new);
							wksjmxDtos.add(wksjmx);
						}
						wksjmxService.insertDtoList(wksjmxDtos);
					}
				}
			}
		} catch (BusinessException e) {
			throw new RuntimeException(e);
		}
		return true;
	}

	/**
	 * 接收外部仪器实验数据并保存为附件
	 */
	public void saveExcFile(String ybbh,String str){
		//根据日期创建文件夹
		String storePath = prefix + releaseFilePath + BusTypeEnum.IMP_SAMPLERECORD + "/"+ "UP"+
				DateUtils.getCustomFomratCurrentDate("yyyy")+ "/"+"UP"+
				DateUtils.getCustomFomratCurrentDate("yyyyMM")+"/" +"UP"+
				DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
		try{
			File dir = new File(storePath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			FileWriter file1 = new FileWriter(storePath+"/"+ybbh+".txt");
			BufferedWriter bufferedWriter = new BufferedWriter(file1);
			bufferedWriter.write(str);
			bufferedWriter.close();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Map<String, Object> syncLibraryInfo(HttpServletRequest request) {
		Map<String,Object> map =new HashMap<>();
//		String wkmc=request.getParameter("wkmc");
//		String jcdw=request.getParameter("jcdw");
//		String nbbh=request.getParameter("nbbh");
//		String jtxx=request.getParameter("jtxx");
//		String sjph1=request.getParameter("sjph1");//宏基因组DNA建库试剂盒
//		String sjph2=request.getParameter("sjph2");//逆转录试剂盒
//		String jcdwid="";
//		List<JcsjDto> jcdwList=redisUtil.hmgetDto( "matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
//		if(jcdwList.size()>0){
//			for(JcsjDto jcsjDto:jcdwList){
//				if(jcsjDto.getCsmc().equals(jcdw)){
//					jcdwid=jcsjDto.getCsid();
//					break;
//				}
//			}
//		}
//		WkglDto wkglDto=new WkglDto();
//		wkglDto.setWkmc(wkmc);
//		WkglDto wkglDto_t = dao.getDto(wkglDto);
//		int xh;
//		wkglDto.setWkid(StringUtil.generateUUID());
//		wkglDto.setJcdw(jcdwid);
//		wkglDto.setSjph1(sjph1);
//		wkglDto.setSjph2(sjph2);
//		wkglDto.setZt(StatusEnum.CHECK_NO.getCode());
//		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//		wkglDto.setWkrq(df.format(new Date()));
//		wkglDto.setLrsj(df.format(new Date()));
//		List<WkglDto> wkglxx = dao.getWkglByJcdwAndLrsj(wkglDto);
//		if (wkglxx != null && wkglxx.size() > 0) {
//			int wkxh = Integer.parseInt(wkglxx.get(0).getXh());
//			for (int i = 0; i < wkglxx.size(); i++) {
//				if (i < wkglxx.size() - 1) {
//					if (Integer.parseInt(wkglxx.get(i).getXh()) > Integer.parseInt(wkglxx.get(i + 1).getXh())) {
//						wkxh = Integer.parseInt(wkglxx.get(i).getXh()) + 1;
//					} else {
//						wkxh = Integer.parseInt(wkglxx.get(i + 1).getXh()) + 1;
//					}
//				}
//			}
//			wkglDto.setXh(String.valueOf(wkxh));
//		} else {
//			wkglDto.setXh("1");
//		}
//		if(wkglDto_t!=null){
//			wkglDto.setZt(wkglDto_t.getZt());
//			wkglDto.setWkid(wkglDto_t.getWkid());
//			wkglDto.setWkrq(wkglDto_t.getWkrq());
//			WkmxDto wkmxDto=new WkmxDto();
//			wkmxDto.setNbbh(nbbh);
//			List<WkmxDto> wkmxDtos=wkmxService.getWkmxByNbbh(wkmxDto);
//			if(!CollectionUtils.isEmpty(wkmxDtos)){
//				map.put("message","内部编号重复:"+nbbh);
//				return map;
//			}
//			wkmxDto.setWkid(wkglDto_t.getWkid());
//			wkmxDto.setJtxx(jtxx);
//			wkmxDtos=wkmxService.getWkmxByJt(wkmxDto);
//			if(!CollectionUtils.isEmpty(wkmxDtos)){
//				map.put("message","接头不允许重复:"+jtxx);
//				return map;
//			}
//			List<WkmxDto> dtoList = wkmxService.getDtoList(wkmxDto);
//			if(dtoList!=null&&dtoList.size()>=40){
//				map.put("message", "文库名称不允许重复!");
//				return map;
//			}else{
//				xh= (dtoList != null ? dtoList.size() : 0) +1;
//			}
//		}else{
//			map.put("message", "文库名称不允许重复!");
//			return map;
//		}
//		map.put("wkid",wkglDto.getWkid());
//		WkmxDto wkmxDto=new WkmxDto();
//		wkmxDto.setWkid(wkglDto.getWkid());
//		wkmxDto.setXh(String.valueOf(xh));
//		wkmxDto.setJcdw(jcdwid);
//		wkmxDto.setNbbh(nbbh);
//		wkmxDto.setJtxx(jtxx);
//		wkmxDto.setWkrq(wkglDto_t.getWkrq());
//		if (nbbh.contains("-REM"))
//			wkmxDto.setHzbj("REM");
//		if (nbbh.contains("-DNA")) {
//			wkmxDto.setJcxmdm("D");
//		} else if (nbbh.contains("-RNA")) {
//			wkmxDto.setJcxmdm("R");
//		} else if (nbbh.contains("-tNGS") || nbbh.contains("-HS")) {
//			wkmxDto.setJcxmdm("C");
//		}
//		if(StringUtil.isNotBlank(nbbh)){
//			String[] nbbhSplit = nbbh.split("-");
//			if(nbbhSplit.length>=2){
//				String nbbm;
//				//后缀
//				StringBuilder suffix= new StringBuilder();
//				if(nbbhSplit[0].length()>6){
//					for(int i=1;i<nbbhSplit.length;i++){
//						suffix.append('-').append(nbbhSplit[i]);
//					}
//					suffix = new StringBuilder(suffix.substring(1));
//					nbbm=nbbhSplit[0];
//				}else{
//					for(int i=2;i<nbbhSplit.length;i++){
//						suffix.append('-').append(nbbhSplit[i]);
//					}
//					suffix = new StringBuilder(suffix.substring(1));
//					nbbm=nbbhSplit[0]+"-"+nbbhSplit[1];
//				}
//				List<String> jcdws =new ArrayList<>();
//				Map<String, Object> params = new HashMap<>();
//				params.put("nbbm",nbbm);
//				String tyid="";
//				if(jcdwList.size()>0){
//					for(JcsjDto jcsjDto:jcdwList){
//						if("0z".equals(jcsjDto.getCsdm())){
//							tyid=jcsjDto.getCsid();
//							break;
//						}
//					}
//				}
//				if(!tyid.equals(jcdwid)){
//					jcdws.add(jcdwid);
//					jcdws.add(tyid);
//				}
//				if(jcdws.size()>0){
//					params.put("jcdws",jcdws.toArray(new String[jcdws.size()]));
//				}
//				List<Map<String, Object>> detectionInfo = getDetectionInfo(params);
//				if(detectionInfo!=null&&detectionInfo.size()>0){
//					for(Map<String,Object> objectMap:detectionInfo){
//						if(objectMap.get("kzcs1")!=null){
//							if(suffix.toString().equals(objectMap.get("kzcs1").toString())){
//								if(objectMap.get("syglid")!=null){
//									WkmxDto wkmxDto_t=new WkmxDto();
//									wkmxDto_t.setSyglid(objectMap.get("syglid").toString());
//									List<WkmxDto> wkmxDto_x = wkmxService.verifySame(wkmxDto_t);
//									if(wkmxDto_x==null || wkmxDto_x.size() ==0){
//										wkmxDto.setSyglid(objectMap.get("syglid").toString());
//										wkmxDto.setTqm(objectMap.get("nbzbm")!=null?objectMap.get("nbzbm").toString():"");
//									}
//								}
//								break;
//							}
//						}
//					}
//				}
//			}
//		}
//		List<WkmxDto> wkmxlist = new ArrayList<>();
//		wkmxlist.add(wkmxDto);
//		wkmxService.insertWkxx(wkmxlist);
		return map;
	}
	/**
	 * 获取实验管理数据
	 */
	public List<Map<String,Object>> getDetectionInfo(Map<String, Object> params){
		return dao.getDetectionInfo(params);
	}
	/**
	 * 获取实验管理数据
	 */

	/**
	 * 获取pooling相关信息
	 */
	public Map<String,Object> getPoolingInfo(WkglDto wkglDto) {
		return dao.getPoolingInfo(wkglDto);
	}

	/**
	 * 处理pooling模板
	 * @param wkglDto
	 * @return
	 */
	public Map<String,Object> dealPooping(WkglDto wkglDto,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("status", "fail");
		//导出模板文件路径
		Map<String,Object> map= dao.getPoolingTemplatePath(wkglDto);
		if (StringUtil.isNotBlank(wkglDto.getSjzl())){
			map.put("sjzl", wkglDto.getSjzl());
		}
		map.put("timezone",wkglDto.getTimezone());
		Map<String,Object> setting = new HashMap<>();

		//根据Q\T的书来判断使用何种模板
		String item = "";
		if(StringUtil.isBlank(request.getParameter("tsum")))
			return resultMap;
		BigDecimal tsum = new BigDecimal(request.getParameter("tsum"));
		BigDecimal qsum = new BigDecimal(request.getParameter("qsum"));
		if (tsum.compareTo(BigDecimal.valueOf(0)) > 0 && qsum.compareTo(BigDecimal.valueOf(0)) <= 0){
			//T（全）
			item = "T";
		} else if (qsum.compareTo(BigDecimal.valueOf(0)) > 0 && tsum.compareTo(BigDecimal.valueOf(0)) <= 0) {
			//Q（全）
			item = "Q";
		} else {
			//ALL（tpool+全）
			item = "ALL";
		}
		if (map.get("cxycskz1") != null){
			String cxycskz1 = (String) map.get("cxycskz1");
			List<Map<String,Object>> jsonList = JSONArray.parseObject(cxycskz1, new TypeReference<List<Map<String, Object>>>() {});
			if (!CollectionUtils.isEmpty(jsonList)){
				for (Map<String,Object> tempSetting : jsonList) {
					//匹配项目
					if (item.equals(tempSetting.get("Item"))){
						//匹配设置
						if ("QT".contains(item) && request.getParameter("ReagentName").equals(tempSetting.get("ReagentName"))){
							setting = tempSetting;
							break;
						}else if ("ALL".equals(item)){
							setting = tempSetting;
							break;
						}
					}
				}
			}
		}
		if ("ALL".equals(item)){
			if (StringUtil.isNotBlank(request.getParameter("tpool_ReagentName"))){
				setting.put("tpool_ReagentName",request.getParameter("tpool_ReagentName"));
			}
			if (StringUtil.isNotBlank(request.getParameter("tpool_PoolVolume"))){
				setting.put("tpool_PoolVolume",request.getParameter("tpool_PoolVolume"));
			}
			if (StringUtil.isNotBlank(request.getParameter("tpool_Concentration"))){
				setting.put("tpool_Concentration",request.getParameter("tpool_Concentration"));
			}
			if (StringUtil.isNotBlank(request.getParameter("tpool_LibraryReferenceVolume"))){
				setting.put("tpool_LibraryReferenceVolume",request.getParameter("tpool_LibraryReferenceVolume"));
			}
			if (StringUtil.isNotBlank(request.getParameter("tpool_DilutedLibraryReferenceVolume"))){
				setting.put("tpool_DilutedLibraryReferenceVolume",request.getParameter("tpool_DilutedLibraryReferenceVolume"));
			}
			if (StringUtil.isNotBlank(request.getParameter("tpool_DilutionFactor"))){
				setting.put("tpool_DilutionFactor",request.getParameter("tpool_DilutionFactor"));
			}
			if (StringUtil.isNotBlank(request.getParameter("tpool_MReadsMultiple"))){
				setting.put("tpool_MReadsMultiple",request.getParameter("tpool_MReadsMultiple"));
			}
			if (StringUtil.isNotBlank(request.getParameter("tpool_MixDilutedLibraryReferenceVolume"))){
				setting.put("tpool_MixDilutedLibraryReferenceVolume",request.getParameter("tpool_MixDilutedLibraryReferenceVolume"));
			}
			if (StringUtil.isNotBlank(request.getParameter("tpool_MixDilutedLibraryReferenceConcentration"))){
				setting.put("tpool_MixDilutedLibraryReferenceConcentration",request.getParameter("tpool_MixDilutedLibraryReferenceConcentration"));
			}
		}
		if (StringUtil.isNotBlank(request.getParameter("PoolVolume"))){
			setting.put("PoolVolume",request.getParameter("PoolVolume"));
		}
		if (StringUtil.isNotBlank(request.getParameter("Concentration"))){
			setting.put("Concentration",request.getParameter("Concentration"));
		}
		if (StringUtil.isNotBlank(request.getParameter("LibraryReferenceVolume"))){
			setting.put("LibraryReferenceVolume",request.getParameter("LibraryReferenceVolume"));
		}
		if (StringUtil.isNotBlank(request.getParameter("DilutedLibraryReferenceVolume"))){
			setting.put("DilutedLibraryReferenceVolume",request.getParameter("DilutedLibraryReferenceVolume"));
		}
		if (StringUtil.isNotBlank(request.getParameter("DilutionFactor"))){
			setting.put("DilutionFactor",request.getParameter("DilutionFactor"));
		}
		if (StringUtil.isNotBlank(request.getParameter("MReadsMultiple"))){
			setting.put("MReadsMultiple",request.getParameter("MReadsMultiple"));
		}
		if (StringUtil.isNotBlank(request.getParameter("MixDilutedLibraryReferenceVolume"))){
			setting.put("MixDilutedLibraryReferenceVolume",request.getParameter("MixDilutedLibraryReferenceVolume"));
		}
		if (StringUtil.isNotBlank(request.getParameter("MixDilutedLibraryReferenceConcentration"))){
			setting.put("MixDilutedLibraryReferenceConcentration",request.getParameter("MixDilutedLibraryReferenceConcentration"));
		}
		if (StringUtil.isNotBlank(request.getParameter("TheMinimum"))){
			setting.put("TheMinimum",request.getParameter("TheMinimum"));
		}
		String wkid = wkglDto.getWkid();
		//1、将文库的录入时间设置为文件夹路径，例：/matridx/download/POOLING_EXPORT/2024/202401/20240121
		//1.1、获取文库录入时间
		String lrsj = map.get("format_lrsj").toString().replace("-", "");
		//1.2、拼接pooling文件夹路径：exportFilePath+POOLING_EXPORT+"/"+lrsj年+"/"+lrsj年月+"/"+lrsj年月日
		String filepath = exportFilePath + ExportTypeEnum.POOLING_EXPORT.getCode() + "/" + lrsj.substring(0, 4) + "/" + lrsj.substring(0, 6) + "/" + lrsj;
		//2、获取pooling模板文件路径
		String path;
		if (map != null && map.get("wjlj") != null && StringUtil.isNotBlank(map.get("wjlj").toString())) {
			path = map.get("wjlj").toString();
		} else {
			resultMap.put("message","pooling模板有误或不存在！");
			return resultMap;
		}
		DBEncrypt dbEncrypt = new DBEncrypt();
		path = dbEncrypt.dCode(path);
		//3、判断pooling文件夹是否存在，不存在则创建
		File file = new File(filepath);
		if (!file.exists()) {//判断是否存在当前文件夹，不存在则创建
			file.mkdirs();
		}
		//4、设置pooling文件名：检测单位代码_lrsj年月日_文库id_当前时间毫秒值+随机数.xlsx
		String newFileName = map.get("wkmc") + "_" +  map.get("jcdwdwmc") + ".xlsx";
		//5、设置pooling文件路径
		String exportPath = filepath + "/" + newFileName;
		//6、将pooling模板复制到指定pooling文件夹
		copyDocument(exportPath, path);
		resultMap.put("status","success");
		//7、修改pooling文件内容
		PoolingUtil poolingUtil = new PoolingUtil();
		log.error("pooling导出-----开始 wkid:" + wkglDto.getWkid() + " wkmc:" + wkglDto.getWkmc());
		//7.1、根据wkid查询文库明细List(主要获取文库名称和浓度)
		WksjmxDto wksjmxDto=new WksjmxDto();
		wksjmxDto.setWkid(wkid);
		poolingUtil.setPoolingRedisStatus(redisUtil,wkid+"_"+wkglDto.getTimezone(),"start","正在获取文库信息...",newFileName,exportPath);
		
		WksjglDto t_wksjglDto = new WksjglDto();
		t_wksjglDto.setWkid(wksjmxDto.getWkid());
		t_wksjglDto = wksjglService.getDtoByWkid(t_wksjglDto);
		List<WksjmxDto> wksjmxDtoList = null;
		if (t_wksjglDto != null ){
			wksjmxDto.setWksjid(t_wksjglDto.getWksjid());
			wksjmxDtoList = wksjmxService.getWksjDtoList(wksjmxDto);
		}else{
			wksjmxDto.setWkid(wkglDto.getWkid());
			//如果不是则从文库上机明细表获取
			wksjmxDtoList=wksjmxService.getListForWksj(wksjmxDto);
		}
		List<WksjmxDto> wksjmxList = wksjmxDtoList;
		
		//List<WksjmxDto> wksjmxList=wksjmxService.getListForWksj(wksjmxDto);
		// 开启线程拷贝临时文件
		Map<String, Object> finalSetting = setting;
		new Thread(() -> {
			poolingUtil.dealPooling(exportPath,newFileName,redisUtil,map,wksjmxList, finalSetting,wksjmxService,fjcfbService);
		}).start();
		return resultMap;
	}


	/**
	 * pooling系数批量调整 设置 处理
	 * @param wkglDto
	 * @return
	 */
	public Map<String, Object> generatePoolingSetting(WkglDto wkglDto) {
		Map<String, Object> returnMap = new HashMap<>();
		//获取 pooling系数批量调整 List
		WkglDto dtoById = getDtoById(wkglDto.getWkid());
		//获取系统设置中的 pooling.coefficient
		Object coefficient = redisUtil.hget("matridx_xtsz", "pooling.coefficient");
		List<Map<String, Object>> list = new ArrayList<>();
		if (coefficient != null) {
			com.alibaba.fastjson.JSONObject job = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(coefficient));
			String szz = job.getString("szz");
			list = (List<Map<String, Object>>) JSONArray.parse(szz);
		}
		//系统设置中的基本参数
		Object detectionExtend = redisUtil.hget("matridx_xtsz", "pooling.detection.extend");
		String detectionExtendStr = "";
		if (detectionExtend != null) {
			com.alibaba.fastjson.JSONObject job = com.alibaba.fastjson.JSONObject.parseObject(String.valueOf(detectionExtend));
			detectionExtendStr = job.getString("szz");
		}
		returnMap.put("detectionExtend", detectionExtendStr);
		//获取 pooling模板
		List<JcsjDto> poolingList = redisUtil.lgetDto("All_matridx_jcsj:" + BasicDataTypeEnum.POOLING_TEMPLATE.getCode());
		returnMap.put("poolingList", poolingList);
		JcsjDto poolTemplate01 = null;
		JcsjDto poolTemplate02 = null;
		for (JcsjDto jcsjDto : poolingList) {
			if ("01".equals(jcsjDto.getCsdm())){
				poolTemplate01 = jcsjDto;
			}else if ("02".equals(jcsjDto.getCsdm())){
				poolTemplate02 = jcsjDto;
			}
		}
		//获取试剂选择
		JcsjDto jc_sjxz = new JcsjDto();
		if (StringUtil.isNotBlank(dtoById.getSjxz())){
			jc_sjxz = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.REAGENT_SELECT.getCode(), dtoById.getSjxz());
		}
		String mbid = null;//模板id
		Map<String,Object> setting = new HashMap<>();
		boolean isHaveTpool = false;//是否显示tpool列
		List<Integer> mReads = Arrays.asList(new Integer[]{25,50,100,125, 150, 400, 450, 475, 500});
		//获取 Q\T数量
		Map<String,Object> map = getPoolingLibraryCount(wkglDto.getWkid());
		String item = "Q";
		//根据所获取的模板类型，在基础数据中获取相应的设置信息
		if (map != null){
			BigDecimal tsum = new BigDecimal(String.valueOf(map.get("tsum")));
			BigDecimal qsum = new BigDecimal(String.valueOf(map.get("qsum")));
			returnMap.put("tsum", tsum);
			returnMap.put("qsum", qsum);
			if (tsum.compareTo(BigDecimal.valueOf(0)) > 0 && qsum.compareTo(BigDecimal.valueOf(0)) > 0){
				mbid = poolTemplate02.getCsid();
			}else {
				mbid = poolTemplate01.getCsid();
			}
			//根据Q\T的数来判断使用何种模板，采用公用标准进行模板判断，特殊模板的设置采用读取相应的基础数据设置
			if (tsum.compareTo(BigDecimal.valueOf(0)) > 0 && qsum.compareTo(BigDecimal.valueOf(0)) <= 0){
				//T（全）
				item = "T";
			} else if (qsum.compareTo(BigDecimal.valueOf(0)) > 0 && tsum.compareTo(BigDecimal.valueOf(0)) <= 0) {
				//Q（全）
				item = "Q";
			} else {
				//ALL（tpool+全）
				item = "ALL";
				isHaveTpool = true;
			}
		}
		//根据所获取的模板类型，在基础数据中获取相应的设置信息
		if (StringUtil.isNotBlank(dtoById.getYqlx())){
			returnMap.put("yqlx", dtoById.getYqlx());
			JcsjDto cxy = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.SEQUENCER_CODE.getCode(), dtoById.getYqlx());
			if (cxy!=null && StringUtils.isNotBlank(cxy.getCskz1())){
				String json = cxy.getCskz1();
				List<Map<String,Object>> jsonList = JSONArray.parseObject(json, new TypeReference<List<Map<String, Object>>>() {});
				for (Map<String,Object> tempSetting : jsonList) {
					//匹配项目
					if (item.equals(tempSetting.get("Item"))){
						//匹配设置
						if ("QT".contains(item) && StringUtil.isNotBlank(jc_sjxz.getCsmc()) && jc_sjxz.getCsmc().equals(tempSetting.get("ReagentName"))){
							setting = tempSetting;
							break;
						}else if ("ALL".equals(item)){
							setting = tempSetting;
							break;
						}
					}
				}
				if (setting != null){
					if(setting.get("Flux") != null){
						mReads = (List<Integer>) setting.get("Flux");
					}
					if (setting.get("Coefficient") !=null){
						List<Map<String, Object>> coefficientList = (List<Map<String, Object>>) setting.get("Coefficient");
						for (Map<String, Object> m : coefficientList) {
							for (int i = 0; i < list.size(); i++) {
								if (list.get(i).get("name").equals(m.get("name"))){
									list.set(i,m);
									break;
								}
							}
						}
					}
				}
			}
		}
		// 根据Q和T的数量决定模板，然后从基础数据获取相应的设置，再根据设置调整模板
		if (setting.get("TemplateDm")!=null){
			String mbdm = (String) setting.get("TemplateDm");
			if ("01".equals(mbdm)){
				mbid = poolTemplate01.getCsid();
				isHaveTpool = false;
			} else if ("02".equals(mbdm)) {
				mbid = poolTemplate02.getCsid();
				isHaveTpool = true;
			}
		}
		returnMap.put("isHaveTpool", isHaveTpool);
		if (StringUtil.isBlank(mbid)){
			mbid = poolTemplate01.getCsid();
		}
		returnMap.put("mbid", mbid);
		if (setting != null && setting.get("TheMinimum") ==null){
			setting.put("TheMinimum", 1.5);
		}
		if (setting != null && setting.get("ReagentName") ==null){
			setting.put("ReagentName", jc_sjxz.getCsmc());
		}
		if (setting != null && setting.get("tpoolReagentName") ==null){
			setting.put("tpool_ReagentName", jc_sjxz.getCsmc());
		}
		returnMap.put("settingStr",JSON.toJSONString(setting));
		returnMap.put("setting",setting);
		//获取文库信息，计算pooling系数
		WkmxDto wkmxDto = new WkmxDto();
		wkmxDto.setWkid(wkglDto.getWkid());
		List<WkmxDto> wkmxlist = wkmxService.getWkmxByWkid(wkmxDto);
		BigDecimal poolingFactor = BigDecimal.valueOf(0);
		if (!CollectionUtils.isEmpty(list) && !CollectionUtils.isEmpty(wkmxlist)){
			for (WkmxDto dto : wkmxlist) {
				if (StringUtil.isNotBlank(dto.getWkmc())){
					for (Map<String,Object> listMap : list) {
						if (listMap.get("key") == null || dto.getWkmc().contains(listMap.get("key").toString())){
							BigDecimal value = new BigDecimal(String.valueOf(listMap.get("value")));
							poolingFactor = poolingFactor.add(value);
							//log.error("wkmc:" +dto.getWkmc() + " key:" + listMap.get("key").toString() + " value:" + listMap.get("value").toString());
							break;
						}
					}
				}
			}
		}
		returnMap.put("poolingFactor",poolingFactor.doubleValue());
		returnMap.put("poolingCoefficient", JSON.toJSONString(list));
		//计算 数据总量，并推荐通量
		BigDecimal floatFactor = new BigDecimal(0);
		if (setting.get("FloatFactor") != null){
			floatFactor = new BigDecimal(String.valueOf(setting.get("FloatFactor")));
		} else if (StringUtil.isNotBlank(detectionExtendStr)){
			JSONObject parse = JSONObject.parseObject(detectionExtendStr);
			if (StringUtil.isNotBlank(parse.getString("FloatFactor"))){
				floatFactor = new BigDecimal(parse.getString("FloatFactor"));
			}
		}
		int index = 0;
		BigDecimal mReadsMultiple = new BigDecimal((setting != null && setting.get("MReadsMultiple") != null) ? ((com.alibaba.fastjson.JSONObject) setting.get("MReadsMultiple")).getString("value"):"10");
		Integer mRead = mReads.get(index);
		BigDecimal mReadFloat = new BigDecimal(String.valueOf(mRead)).multiply(floatFactor.add(new BigDecimal(1)));
		BigDecimal doubleSpareCapacity = mReadFloat.subtract(poolingFactor.multiply(mReadsMultiple));
		while (doubleSpareCapacity.compareTo(BigDecimal.valueOf(0)) <= 0){
			if (index < mReads.size() - 2){
				index++;
				mRead = mReads.get(index);
				mReadFloat = new BigDecimal(String.valueOf(mRead)).multiply(floatFactor.add(new BigDecimal(1)));
				doubleSpareCapacity = mReadFloat.subtract(poolingFactor.multiply(mReadsMultiple));
			}else {
				break;
			}
		}
		String sjzl = String.valueOf(mReads.get(index));
		returnMap.put("sjzl", sjzl);
		returnMap.put("realSjzl", poolingFactor.multiply(mReadsMultiple));
		return returnMap;
	}
}
