package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.dao.entities.CydcszModel;
import com.matridx.igams.common.dao.entities.CydcxxModel;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DcszModel;
import com.matridx.igams.common.dao.entities.ProcessModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.post.IDcszDao;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.factory.ServiceFactory;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDcszService;
import com.matridx.igams.common.util.FileExportThread;
import com.matridx.igams.common.util.RedisXmlConfig;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateTimeUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DcszServiceImpl extends BaseBasicServiceImpl<DcszDto, DcszModel, IDcszDao> implements IDcszService{
	
	private final Logger log = LoggerFactory.getLogger(DcszServiceImpl.class);
	
	@Autowired
	RedisXmlConfig redisXmlConfig;
	
	@Value("${matridx.exptype.limit}")
	private String exportLimit;
	
	/**
	 * 导出文件存放路径
	 */
	@Value("${matridx.file.exportFilePath}")
	private final String exportFilePath = null;
	
	/**
	 * 获取已选字段  
	 */
	@Override
	public List<DcszDto> getChoseList(DcszDto dcszDto) {
		return dao.getChoseList(dcszDto);
	}

	/**
	 * 获取未选字段   
	 */
	@Override
	public List<DcszDto> getCateWaitList(DcszDto dcszDto) {
		//获取导出字段分类

		return dao.getCateWaitList(dcszDto);
	}

	@Override
	public void updateChoseList(DcszDto dcszDto, List<DcszDto> choseList) throws BusinessException {
		if(StringUtil.isBlank(dcszDto.getYwid())){
			throw new BusinessException("导出文件出错，没有找到导出业务类型！");
		}
		if(choseList==null || choseList.isEmpty()){
			throw new BusinessException("导出出错，请选择要导出的字段！");
		}
		//删除原先配置
		dao.deleteLs(dcszDto);
		Map<String,Object> map = new HashMap<>();
		map.put("list", choseList);
		map.put("yhid", dcszDto.getYhid());
		map.put("ywid", dcszDto.getYwid());
		dao.batchInsertMap(map);
	}
	

	@Override
	public List<CydcszModel> getCydcszxxs(DcszDto dcszDto) {
		return dao.getCydcszs(dcszDto);
	}

	/**
	 * 文件导出设置
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public ProcessModel export(DcszDto dcDto, List<String> choseList, User user, HttpServletRequest request) throws Exception {
		if(StringUtil.isBlank(dcDto.getYwid())){
			throw new BusinessException("导出文件出错，没有找到导出业务类型！");
		}
		if(choseList== null || choseList.isEmpty()){
			throw new BusinessException("导出出错，请选择要导出的字段！");
		}
		List<DcszDto> choseLists = getSelectedList(dcDto.getYwid(),choseList);

		if(StringUtil.isNotBlank(dcDto.getSfbc())&&dcDto.getSfbc().equals("Y")){
			updateChoseList(dcDto,choseLists);
		}
		
		ProcessModel pro = new ProcessModel();
		//创建并写入文件
		String fileName = String.valueOf(new Date().getTime());
		String fileType = ".xls";
		if(StringUtil.isNotBlank(dcDto.getFileType())&&dcDto.getFileType().equals("1")){
			fileType = ".csv";
		}
		fileName += fileType;
		String currentDay = DateTimeUtil.getFormatDay(new Date());
		File filepath = new File(exportFilePath + currentDay.substring(0, 4) + "/" + currentDay.substring(0, 6) + "/" + currentDay.substring(0, 8) + "/");
		if(!filepath.exists()){
			filepath.mkdirs();
		}
		
		pro.setFilePath(fileName);
		
		Map<String, String> configMap = redisXmlConfig.getExpConfig(dcDto.getYwid());

		//获取配置导出配置
		String getCount = configMap.get("count-method");
		String classEntity = configMap.get("class-entity");
		String classService = configMap.get("class-service");
		Object serviceInstance = ServiceFactory.getService(classService);//获取方法所在class
		Class<?> dtoClass = Class.forName(classEntity);//获取实体类class
		//设置查询条件信息
		Object dtoInstance = dtoClass.newInstance();
		Map<String,String> concatMap = new HashMap<>();
		if (StringUtil.isNotBlank(request.getParameter("concatCs"))){
			String[] concatCss = request.getParameter("concatCs").split(",");
			for (String concatCs : concatCss) {
				String[] concatC = concatCs.split("=");
				String concatKey = concatC[0];
				String concatValue = concatC[1];
				if (StringUtil.isNotBlank(concatValue)){
					concatMap.put(concatKey,concatValue);
				}
			}
		}
		if ("java.util.HashMap".equals(classEntity)){
			Enumeration<String> parameterNames = request.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String key = parameterNames.nextElement();
				String value = request.getParameter(key);
				if (StringUtil.isNotBlank(value)){
					((Map<String, Object>)dtoInstance).put(key, value);
				}
			}
			((Map<String, Object>)dtoInstance).put("yhid", user.getYhid());
			((Map<String, Object>)dtoInstance).put("ddid", user.getDdid());
			((Map<String, Object>)dtoInstance).put("wxid", user.getWechatid());
			Class cls = dcDto.getClass();
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				try {
					if (field.get(dcDto)!=null && field.get(dcDto)!=""){
						//向dtoInstance中设置属性值
						BeanUtils.setProperty(dtoInstance, field.getName(), field.get(dcDto));
//						Method setter = dtoInstancob.getWriteMethod();
//						dtoInstance.put(field.getName(),field.get(dcDto));
					}
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
			for (;cls.getSuperclass()!=null;cls = cls.getSuperclass()){
				Field[] superfields = cls.getDeclaredFields();
				for (Field field : superfields) {
					field.setAccessible(true);
					try {
						if (field.get(dcDto)!=null && field.get(dcDto)!=""){
							BeanUtils.setProperty(dtoInstance, field.getName(), field.get(dcDto));
						}
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
			}
		}else {
			BeanInfo beanInfo = Introspector.getBeanInfo(dtoInstance.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				String value = request.getParameter(key);
				if (value != null){
					// 得到property对应的setter方法
					Method setter = property.getWriteMethod();
					switch (setter.getParameterTypes()[0].getName()) {
						case "java.lang.String":
							setter.invoke(dtoInstance, value);
							break;
						case "java.util.List": {
							String s_value = value.replace("[", "").replace("]", "").replace(" ", "");
							if (StringUtil.isBlank(s_value))
								continue;
							String[] ss_value = s_value.split(",");
							List<String> list = Arrays.asList(ss_value);
							setter.invoke(dtoInstance, list);
							break;
						}
						case "[Ljava.lang.String;": {
							String s_value = value.replace("[", "").replace("]", "");
							if (StringUtil.isBlank(s_value))
								continue;
							String[] ss_value = s_value.split(",");
							setter.invoke(dtoInstance, new Object[]{ss_value});
							break;
						}
					}
				}else {
					if (concatMap.containsKey(key)){
						Method setter = property.getWriteMethod();
						setter.invoke(dtoInstance, concatMap.get(key));
					}
				}
			}
		}

		if(dcDto.getYwid().toLowerCase().contains("select")){
			int totalCount = dcDto.getIds().size();
			pro.setCurrentCount(totalCount);
			dcDto.setTotalNumber(totalCount);
		}else{
			Map<String, Object> otherParMap = new HashMap<>();
			otherParMap.put("jsid", dcDto.getJsid());
			otherParMap.put("yhid", dcDto.getYhid());
			//获取页数
			if ("java.util.HashMap".equals(classEntity)){
				dtoClass = Map.class;
			}
			Method getCountMethod = serviceInstance.getClass().getMethod(getCount, dtoClass,Map.class);//获取计数方法
			int totalCount = (Integer) getCountMethod.invoke(serviceInstance, dtoInstance,otherParMap);
			pro.setCurrentCount(totalCount);
			dcDto.setTotalNumber(totalCount);
		}
		
		RedisUtil redisUtil = redisXmlConfig.getRedisUtil();
		
		String downId = StringUtil.generateUUID();
		
		pro.setWjid(downId);
		dcDto.setWjid(downId);
		
		redisUtil.hset("EXP_:_"+downId,"filePath", filepath.getPath() + "/" + fileName,3600);
		redisUtil.hset("EXP_:_"+downId,"fileName", fileName);
		
		FileComExport fileComExport = new FileComExport();
		fileComExport.init(redisXmlConfig,dcDto,dtoInstance,choseLists,exportLimit);
		FileExportThread exportThread = new FileExportThread(fileComExport);
		exportThread.start();
		
		return pro;
	}
	

	/**
	 * 取消导出
	 */
	public boolean commCancelExport(DcszDto dcszDto){
		String fileid = dcszDto.getWjid();
		
		RedisUtil redisUtil = redisXmlConfig.getRedisUtil();
		//存在导出Fin参数，则放入Cancel参数
		if(redisUtil.hget("EXP_:_"+fileid,"Fin")!=null){
			redisUtil.hset("EXP_:_"+fileid,"isCancel",true,60);
			return true;
		}
		return false;
	}
	
	/**
	 * 检查文件处理进度
	 */
	public Map<String,Object> commCheckExport(DcszDto dcszDto){
		String fileid = dcszDto.getWjid();
		boolean isFinished = false;
		boolean isCanceled = false;
		Map<String,Object> map = new HashMap<>();
		
		RedisUtil redisUtil = redisXmlConfig.getRedisUtil();
		
		if(redisUtil.hget("EXP_:_"+fileid,"isCancel") != null){
			isCanceled = (Boolean) redisUtil.hget("EXP_:_" + fileid, "isCancel");
		}
		if(redisUtil.hget("EXP_:_"+fileid,"Fin") != null)
			isFinished = (Boolean) redisUtil.hget("EXP_:_" + fileid, "Fin");
		else{
			map.put("result", true);
		}
		
		if(isFinished){
			String msg = (String)redisUtil.hget("EXP_:_"+fileid,"Msg");
			if(isCanceled){//取消标记
				map.put("cancel", true);
			}
			if(StringUtil.isNotBlank(msg)){//结束且有提示信息
				map.put("msg", msg);
			}
			map.put("result", true);
			
		}else{
			if(isCanceled){//取消标记
				map.put("cancel", true);
			}
			map.put("result", false);
		}
		map.put("currentCount", redisUtil.hget("EXP_:_"+fileid,"Cnt"));
		return map;
	}
	
	/**
	 * 文件下载处理
	 */
	public void commDownloadExport(DcszDto dcszDto,HttpServletResponse response) {
		InputStream in = null;
		OutputStream out = null;
		RedisUtil redisUtil = redisXmlConfig.getRedisUtil();
		String fileid = dcszDto.getWjid();
		
		try{
	        String filePath = (String)redisUtil.hget("EXP_:_"+fileid,"filePath");
	        String fileName = (String)redisUtil.hget("EXP_:_"+fileid,"fileName");
			
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
		}catch(Exception e){
			log.error(e.getMessage());
		}finally{
			redisUtil.del("EXP_:_"+fileid);
			try{
				if(in != null){
					in.close();
				}
				if(out != null){
					out.close();
				}
			}catch(Exception ex){
				log.error(ex.getMessage());
			}
		}
	}
	
	/**
	 * 根据客户选择的信息，从数据获取相应的SQL字段等信息
	 */
	private List<DcszDto> getSelectedList(String ywid,List<String> choseList){
		Map<String, Object> param = new HashMap<>();
		param.put("ywid", ywid);
		param.put("choseList", choseList);
		return dao.getSelectedList(param);
	}

	/**
	 * 根据常用导出ID获取常用导出明细
	 */
	public List<CydcxxModel> getCydcxxs(CydcszModel cydcszModel){
		return dao.getCydcxxs(cydcszModel);
	}

	/**
	 * Mngs文件导出设置
	 */
	@Transactional(propagation=Propagation.REQUIRED,rollbackFor=Exception.class)
	public ProcessModel mngsExport(DcszDto dcDto,HttpServletRequest request) throws Exception {
		if(StringUtil.isBlank(dcDto.getYwid())){
			throw new BusinessException("导出文件出错，没有找到导出业务类型！");
		}
		List<DcszDto> choseLists = getListById(dcDto.getYwid());

		ProcessModel pro = new ProcessModel();
		//创建并写入文件
		String fileName ="";
		if("DRUG_RESISTANCE_RESULT".equals(dcDto.getYwid())){
			fileName= dcDto.getWkbh()+".耐药检测结果统计";
		}else if("VIRULENCE_RESULT".equals(dcDto.getYwid())){
			fileName= dcDto.getWkbh()+".毒力报告结果";
		}else if("AI_RESULT".equals(dcDto.getYwid())){
			fileName= dcDto.getWkbh()+".dr";
		}else if("AUDIT_RESULT".equals(dcDto.getYwid())){
			fileName= dcDto.getWkbh();
		}

		String fileType = ".xls";
		fileName += fileType;
		String currentDay = DateTimeUtil.getFormatDay(new Date());
		File filepath = new File(exportFilePath + currentDay.substring(0, 4) + "/" + currentDay.substring(0, 6) + "/" + currentDay.substring(0, 8) + "/");
		if(!filepath.exists()){
			filepath.mkdirs();
		}

		pro.setFilePath(fileName);

		Map<String, String> configMap = redisXmlConfig.getExpConfig(dcDto.getYwid());

		//获取配置导出配置
		String classEntity = configMap.get("class-entity");
		Class<?> dtoClass = Class.forName(classEntity);//获取实体类class
		//设置查询条件信息
		Object dtoInstance = dtoClass.newInstance();

		BeanInfo beanInfo = Introspector.getBeanInfo(dtoInstance.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

		for (PropertyDescriptor property : propertyDescriptors) {
			String key = property.getName();
			String value = request.getParameter(key);
			if (value != null){
				// 得到property对应的setter方法
				Method setter = property.getWriteMethod();
				switch (setter.getParameterTypes()[0].getName()) {
					case "java.lang.String":
						setter.invoke(dtoInstance, value);
						break;
					case "java.util.List": {
						String s_value = value.replace("[", "").replace("]", "").replace(" ", "");
						if (StringUtil.isBlank(s_value))
							continue;
						String[] ss_value = s_value.split(",");
						List<String> list = Arrays.asList(ss_value);
						setter.invoke(dtoInstance, list);
						break;
					}
					case "[Ljava.lang.String;": {
						String s_value = value.replace("[", "").replace("]", "");
						if (StringUtil.isBlank(s_value))
							continue;
						String[] ss_value = s_value.split(",");
						setter.invoke(dtoInstance, new Object[]{ss_value});
						break;
					}
				}
			}
		}

		pro.setCurrentCount(1);
		dcDto.setTotalNumber(1);

		RedisUtil redisUtil = redisXmlConfig.getRedisUtil();

		String downId = StringUtil.generateUUID();

		pro.setWjid(downId);
		dcDto.setWjid(downId);

		redisUtil.hset("EXP_:_"+downId,"filePath", filepath.getPath() + "/" + fileName,3600);
		redisUtil.hset("EXP_:_"+downId,"fileName", fileName);

		FileComExport fileComExport = new FileComExport();
		fileComExport.init(redisXmlConfig,dcDto,dtoInstance,choseLists,exportLimit);
		//FileExportThread exportThread = new FileExportThread(fileComExport);
		fileComExport.commExportDeal();
		//exportThread.start();

		return pro;
	}

	/**
	 * 根据ywid获取全部信息
	 */
	public List<DcszDto> getListById(String ywid){
		return dao.getListById(ywid);
	}
}
