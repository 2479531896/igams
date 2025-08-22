package com.matridx.igams.common.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JcsjModel;
import com.matridx.igams.common.dao.post.IJcsjDao;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.MQTypeEnum_pub;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.util.CommonWordUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.encrypt.DBEncrypt;
import com.matridx.springboot.util.xml.BasicXmlReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JcsjServiceImpl extends BaseBasicServiceImpl<JcsjDto, JcsjModel, IJcsjDao> implements IJcsjService{
	
	@Value("${matridx.basictype.file:}")
	private String basicTypeFile;
	@Value("${matridx.fileupload.releasePath}")
	private String releaseFilePath;
	@Value("${matridx.fileupload.prefix}")
	private String prefix;
	@Value("${matridx.fileupload.tempPath}")
	private String tempFilePath;
	@Autowired(required = false)
	private AmqpTemplate amqpTempl;
	@Autowired
	RedisUtil redisUtil;
	@Autowired
	IFjcfbService fjcfbService;
	@Value("${spring.rabbitmq.docok:mq.tran.basic.ok}")
	private String DOC_OK = null;
	private final Logger logger = LoggerFactory.getLogger(JcsjServiceImpl.class);
	
	/**
	 * /**（该方法包括使用中和停用的数据类型）
	 * 批量处理基础数据的编码，根据编码（csidFieldName）取得名称，并反射设置到DTO实例对应属性（csmcFieldName）
	 * 注：一次处理单个数据类型，若dtoList有多个字段需要转换，多次调用本方法
	 * @param dtoList 具体业务查询返回的DTO集
	 * @param basicDateTypes 基础数据枚举们
	 * @param fieldNames 数据编码属性名称，以及名称对应的属性名称；例：{"csid":"csmc", "csid3":"csmc3"}
	 * @author goofus
	 * 
	 * 使用例子：
	   handleCodeToValue(xmjbxxDtoList,
						new BasicDataTypeEnum[] {BasicDataTypeEnum.PROJECT_TYPE,BasicDataTypeEnum.PROJECT_CATA },
						new String[] { "xmlx:xmlxmc","xmlb:xmlbmc" });
	 */
	public void handleCodeToValue(List<?> dtoList, BasicDataTypeEnum[] basicDateTypes, String[] fieldNames){
		if (null == basicDateTypes || basicDateTypes.length < 1 || null == fieldNames
				|| fieldNames.length < 1 || basicDateTypes.length != fieldNames.length) {
			return ;
		}
		
		for(int a=0,al=basicDateTypes.length; a<al; a++){
			BasicDataTypeEnum basicDateType = basicDateTypes[a];
			String fieldName = fieldNames[a];
			if(StringUtil.isBlank(fieldName)) continue;
			String[] names = fieldName.split(":");
			String csidFieldName = names[0];
			String csmcFieldName = names[1];
			if(null != basicDateType && StringUtil.isNotBlank(csidFieldName) && StringUtil.isNotBlank(csmcFieldName))
				handleCodeToValue(dtoList, basicDateType, csidFieldName, csmcFieldName);
		}
	}
	
	/**
	 * （该方法包括使用中和停用的数据类型）
	 * 批量处理基础数据的编码，根据编码（csidFieldName）取得名称，并反射设置到DTO实例对应属性（csmcFieldName）
	 * 注：一次处理单个数据类型，若dtoList有多个字段需要转换，多次调用本方法
	 * @param dtoList 具体业务查询返回的DTO集
	 * @param basicDateType 基础数据枚举
	 * @param csidFieldName 数据编码属性名称
	 * @param csmcFieldName 名称对应的属性名称
	 * @author goofus
	 */
	public void handleCodeToValue(List<?> dtoList, BasicDataTypeEnum basicDateType, String csidFieldName, String csmcFieldName){
		if(null == dtoList || dtoList.isEmpty() || null == basicDateType
				|| StringUtil.isBlank(csidFieldName) || StringUtil.isBlank(csmcFieldName)) return ;
		try {
//			JcsjDto jcsjDto = new JcsjDto();
//			jcsjDto.setJclb(basicDateType.getCode());
//			List<JcsjDto> jcList = dao.getDtoList(jcsjDto);
			List<JcsjDto> jcList = redisUtil.lgetDto("All_matridx_jcsj:" + basicDateType.getCode());
			for(Object dto : dtoList){
				String csmc = null;
				
				Class<?> entityClass = dto.getClass();
				String csid;
				if (StringUtil.isNotBlank(entityClass.getName()) && entityClass.getName().equals("java.util.HashMap")){
					csid = (String) ((Map<String, Object>) dto).get(csidFieldName);
				}else {
					Method dwMethod = entityClass.getMethod("get" + StringUtil.firstToUpper(csidFieldName));
					csid = (String) dwMethod.invoke(dto);
				}
				if(StringUtil.isNotBlank(csid)){
					for(JcsjDto jcsj : jcList){
						if(null != jcsj && csid.equals(String.valueOf(jcsj.getCsid()))){
							csmc = jcsj.getCsmc();
							break;
						}
					}
				}
				
				if(StringUtil.isNotBlank(csmc) && StringUtil.isNotBlank(csmcFieldName)){
					if (StringUtil.isNotBlank(entityClass.getName()) && entityClass.getName().equals("java.util.HashMap")){
						((Map<String, Object>) dto).put(csmcFieldName,csmc);
					}else {
						Method mcMethod = entityClass.getMethod("set" + StringUtil.firstToUpper(csmcFieldName), String.class);
						mcMethod.invoke(dto, csmc);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * （该方法包括使用中和停用的数据类型）
	 * 批量处理基础数据的编码，根据编码（csidFieldName）取得名称，并反射设置到DTO实例对应属性（csmcFieldName）
	 * 注：一次处理单个数据类型，若dtoList有多个字段需要转换，多次调用本方法
	 * @param dtoList 具体业务查询返回的DTO集
	 * @param basicDateType 基础数据枚举
	 * @param csidFieldName 数据编码属性名称
	 * @param csmcFieldName 名称对应的属性名称
	 * @author goofus
	 */
	public void handleCodeToValue(List<?> dtoList, String basicDateType, String csidFieldName, String csmcFieldName){
		if(null == dtoList || dtoList.isEmpty() || null == basicDateType
				|| StringUtil.isBlank(csidFieldName) || StringUtil.isBlank(csmcFieldName)) return ;
		try {
//			JcsjDto jcsjDto = new JcsjDto();
//			jcsjDto.setJclb(basicDateType);
//			List<JcsjDto> jcList = dao.getDtoList(jcsjDto);
			List<JcsjDto> jcList = redisUtil.hmgetDto("matridx_jcsj:"+basicDateType);
			for(Object dto : dtoList){
				String csmc = null;
				
				Class<?> entityClass = dto.getClass();
				String csid;
				if (StringUtil.isNotBlank(entityClass.getName()) && entityClass.getName().equals("java.util.HashMap")){
					csid = (String) ((Map<String, Object>) dto).get(csidFieldName);
				}else {
					Method dwMethod = entityClass.getMethod("get" + StringUtil.firstToUpper(csidFieldName));
					csid = (String) dwMethod.invoke(dto);
				}

				if(StringUtil.isNotBlank(csid)){
					for(JcsjDto jcsj : jcList){
						if(null != jcsj && csid.equals(String.valueOf(jcsj.getCsid()))){
							csmc = jcsj.getCsmc();
							break;
						}
					}
				}
				
				if(StringUtil.isNotBlank(csmc) && StringUtil.isNotBlank(csmcFieldName)){
					if (StringUtil.isNotBlank(entityClass.getName()) && entityClass.getName().equals("java.util.HashMap")){
						((Map<String, Object>) dto).put(csmcFieldName,csmc);
					}else {
						Method mcMethod = entityClass.getMethod("set" + StringUtil.firstToUpper(csmcFieldName), String.class);
						mcMethod.invoke(dto, csmc);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	@Override
	public JcsjDto getDto(JcsjDto jcsjDto){
		return dao.getDto(jcsjDto);

	}
	
	/**
	 * 根据参数类别获取参数列表，允许一次多个
	 */
	public Map<String, List<JcsjDto>> getDtoListbyJclb(BasicDataTypeEnum[] basicDateTypes){
		if(basicDateTypes == null)
			return null;
		Map<String, List<JcsjDto>> result = new HashMap<>();

		for (BasicDataTypeEnum basicDateType : basicDateTypes) {
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb(basicDateType.getCode());
			List<JcsjDto> jcList = dao.getDtoListByJclb(jcsjDto);
			List<JcsjDto> tjcList = new ArrayList<>();
			if (jcList != null && !jcList.isEmpty()) {
				for (JcsjDto dto : jcList) {
					if ("0".equals(dto.getScbj())) {
						tjcList.add(dto);
					}
				}
			}
			result.put(basicDateType.getCode(), tjcList);
		}
		
		return result;
	}
	
	/**
	 * 根据参数类别获取参数列表，允许一次
	 */
	public List<JcsjDto> getDtoListbyJclb(BasicDataTypeEnum basicDateTypes){
		if(basicDateTypes == null)
			return null;
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb(basicDateTypes.getCode());
		List<JcsjDto> jcList = dao.getDtoListByJclb(jcsjDto);
		List<JcsjDto> tjcList = new ArrayList<>();
		if(jcList!=null && !jcList.isEmpty()){
			for (JcsjDto dto : jcList) {
				if ("0".equals(dto.getScbj())) {
					tjcList.add(dto);
				}
			}
		}
		return tjcList;
	}
	
	/**
	 * 根据参数类别获取参数列表，允许一次多个，包含停用数据
	 */
	public Map<String, List<JcsjDto>> getDtoListbyJclbInStop(BasicDataTypeEnum[] basicDateTypes){
		if(basicDateTypes == null)
			return null;
		Map<String, List<JcsjDto>> result = new HashMap<>();

		for (BasicDataTypeEnum basicDateType : basicDateTypes) {
			List<JcsjDto> jcsjList = redisUtil.lgetDto("All_matridx_jcsj:" + basicDateType.getCode());

			if (jcsjList==null || jcsjList.isEmpty()) {
				jcsjList = resetRedisJcsjList(basicDateType.getCode());
				logger.error("getDtoListbyJclbInStop未找到数据重新查询：" + basicDateType.getCode() + " 重新查询结果为:" + jcsjList.size());
			}

			result.put(basicDateType.getCode(), jcsjList);
		}
		/*
		JcsjDto jcsjDto = new JcsjDto();
		List<String> jclbs = new ArrayList<String>();
		for(int a=0,al=basicDateTypes.length; a<al; a++){
			jclbs.add(basicDateTypes[a].getCode());
		}
		jcsjDto.setJclbs(jclbs);
		
		List<JcsjDto> jcList = dao.getInstopDtoList(jcsjDto);
		
		if(jcList != null) {
			List<JcsjDto> b_jcs = null;
			String preJclb = "";
			for(int i=0;i<jcList.size();i++) {
				if(!jcList.get(i).getJclb().equals(preJclb)) {
					if(StringUtil.isNotBlank(preJclb)) {
						result.put(preJclb, b_jcs);
					}
					b_jcs = new ArrayList<JcsjDto>();
					preJclb = jcList.get(i).getJclb();
				}
				b_jcs.add(jcList.get(i));
			}
			if(StringUtil.isNotBlank(preJclb)) {
				result.put(preJclb, b_jcs);
			}
		}*/
		
		return result;
	}
	
	/**
	 * 根据参数类别获取参数列表，允许一次，包含停用数据
	 */
	public List<JcsjDto> getDtoListbyJclbInStop(BasicDataTypeEnum basicDateTypes){
		if(basicDateTypes == null)
			return null;
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb(basicDateTypes.getCode());
		return dao.getInstopDtoList(jcsjDto);
	}
	
	/**
	 * 获取列表(不保存到缓存中)
	 */
	public List<JcsjDto> getJcsjDtoList(JcsjDto jcsjDto){
		List<JcsjDto> jcList = dao.getDtoList(jcsjDto);
		for(int i=(jcList.size()-1);i>=0;i--){
			if(!"0".equals(jcList.get(i).getScbj())){
				jcList.remove(i);
			}
		}
		return jcList;
	}

	/**
	 * 获取列表(不保存到缓存中)
	 */
	public List<JcsjDto> getJcsjDtoListAndJl(JcsjDto jcsjDto){
		List<JcsjDto> jcList = dao.getInstopDtoList(jcsjDto);
		for(int i=(jcList.size()-1);i>=0;i--){
			if(!"0".equals(jcList.get(i).getScbj())){
				jcList.remove(i);
			}
		}
		if (jcList.isEmpty()){
			jcsjDto.setYzid(null);
			List<JcsjDto> jcLists = dao.getInstopDtoList(jcsjDto);
			for(int i=(jcLists.size()-1);i>=0;i--){
				if(!"0".equals(jcLists.get(i).getScbj())){
					jcLists.remove(i);
				}
			}
			return jcLists;
		}
		return jcList;
	}
	
	/**
	 * 获取列表，包含停用数据(不保存到缓存中)
	 */
	public List<JcsjDto> getJcsjDtoListInStop(JcsjDto jcsjDto){
		return dao.getInstopDtoList(jcsjDto);
	}
	
	/**
     * 查询基础数据类别列表
     */
    public List<Map<String, String>> getJclbList(){
    	return BasicXmlReader.readXmlToList(basicTypeFile, new String[]{"id","name","useCache","parentFId","parentId","basic_extend_1","basic_extend_2","basic_extend_3","basic_extend_4","basic_extend_5","pId","basic_extend_1_select","basic_extend_2_select","basic_extend_3_select","basic_extend_4_select","basic_file_1","basic_file_2","basic_file_3","basic_name_1","basic_name_2","basic_name_3"});
    }
    
    /**
     * 插入基础数据信息
     */
    @Override
    public boolean insert(JcsjModel jcsjModel){
		if (StringUtil.isBlank(jcsjModel.getCsid())){
			String csid = StringUtil.generateUUID();
			jcsjModel.setCsid(csid);
		}
    	int result = dao.insert(jcsjModel);
    	//redis同步存入一条基础数据
		//redisUtil.hset("matridx_jcsj:"+jcsjModel.getJclb(), jcsjModel.getCsid(), JSONObject.toJSONString(jcsjModel),-1);
		
		resetRedisJcsjList(jcsjModel.getJclb());
    	return result > 0;
    }
    
    @Override 
	public List<JcsjDto> getPagedDtoList(JcsjDto jcsjDto){
		//获取审核信息
		try {
			return dao.getPagedDtoList(jcsjDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
    /**
	  * 根据省份从基础数据中查询城市
	  */
	@Override
	public List<JcsjDto> jcsjcity(JcsjDto jcsjDto){
		// TODO Auto-generated method stub
		jcsjDto.setJclb("CITY");
		return dao.getInstopDtoList(jcsjDto);
	}

	/**
	  * 查询默认的类型
	  */
	@Override
	public JcsjDto getdefault(JcsjDto jcsjDto){
		// TODO Auto-generated method stub
		JcsjDto jcsjDto_t = new JcsjDto();
		jcsjDto_t.setSfmr("1");
		jcsjDto_t.setJclb(jcsjDto.getJclb());
		jcsjDto_t.setDefaultCsid(jcsjDto.getCsid());
		return dao.getDto(jcsjDto_t);
	}
	
	 /**
	  * 新增保存基础数据并判断是否同类型下已存在默认数据
	  */
	 public boolean insertJcsj(JcsjDto jcsjDto) throws BusinessException {
		 String sfmr=jcsjDto.getSfmr();
		 JcsjDto sfczmrsj=getdefault(jcsjDto);
		 if(("1").equals(sfmr) && sfczmrsj!=null) {
			 throw new BusinessException("msg","不允许存在多条默认数据!");
		 }
		 insert(jcsjDto);
		 if(jcsjDto.getFjids_D()!=null && !jcsjDto.getFjids_D().isEmpty()) {
			 FjcfbDto fjcfbDto=new FjcfbDto();
			 fjcfbDto.setYwlx(jcsjDto.getYwlx_D());
			 fjcfbDto.setYwid(jcsjDto.getCsid());
			 JcsjDto jcsjDto_t=new JcsjDto();
			 jcsjDto_t.setFjids(jcsjDto.getFjids_D());
			 jcsjDto_t.setCsid(jcsjDto.getCsid());
			 fileHandling(jcsjDto_t,fjcfbDto);
		 }
		 
		 if(jcsjDto.getFjids_R()!=null && !jcsjDto.getFjids_R().isEmpty()) {
			 FjcfbDto fjcfbDto=new FjcfbDto();
			 fjcfbDto.setYwlx(jcsjDto.getYwlx_R());
			 fjcfbDto.setYwid(jcsjDto.getCsid());
			 JcsjDto jcsjDto_t=new JcsjDto();
			 jcsjDto_t.setFjids(jcsjDto.getFjids_R());
			 jcsjDto_t.setCsid(jcsjDto.getCsid());
			 fileHandling(jcsjDto_t,fjcfbDto);
		 }
		 
		 if(jcsjDto.getFjids_C()!=null && !jcsjDto.getFjids_C().isEmpty()) {
			 FjcfbDto fjcfbDto=new FjcfbDto();
			 fjcfbDto.setYwlx(jcsjDto.getYwlx_C());
			 fjcfbDto.setYwid(jcsjDto.getCsid());
			 JcsjDto jcsjDto_t=new JcsjDto();
			 jcsjDto_t.setFjids(jcsjDto.getFjids_C());
			 jcsjDto_t.setCsid(jcsjDto.getCsid());
			 fileHandling(jcsjDto_t,fjcfbDto);
		 }
		 return true;
	 }
	 
	 /**
  	  * 处理报告模板
	  */
	 private boolean fileHandling(JcsjDto jcsjDto, FjcfbDto fjcfbDto) {
		// 删除原有文件
		List<FjcfbDto> fjcfbDtos = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
		if (fjcfbDtos != null && !fjcfbDtos.isEmpty())
		{
			for (FjcfbDto dto : fjcfbDtos) {
				fjcfbService.delFile(dto);
			}
		}
		// 文件复制到正式文件夹，插入信息至正式表
		if (jcsjDto.getFjids() != null && !jcsjDto.getFjids().isEmpty()){
			boolean saveFile = fjcfbService.save2RealFile(jcsjDto.getFjids().get(0), jcsjDto.getCsid() );
				if (!saveFile)
					return false;
					// 附件排序
			boolean isSuccess = fjcfbService.fileSort(fjcfbDto);
				if (!isSuccess)
					return false;

			String filePath=getFilePath(jcsjDto.getFjids().get(0));
			if(StringUtil.isBlank(filePath)) 
				return false; 
			CommonWordUtil commonwordutil=new CommonWordUtil();
			commonwordutil.reformWord(filePath);
			if(BusTypeEnum.IMP_PPTGL_FILE.getCode().equals(fjcfbDto.getYwlx())){
				List<FjcfbDto> dtoList = fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
				if(!CollectionUtils.isEmpty(dtoList)){
					for(FjcfbDto fjcfbDto_t:dtoList){
						if(fjcfbDto_t.getWjm().indexOf(".doc")!=-1||fjcfbDto_t.getWjm().indexOf(".docx")!=-1||fjcfbDto_t.getWjm().indexOf(".xls")!=-1||fjcfbDto_t.getWjm().indexOf(".xlsx")!=-1||fjcfbDto_t.getWjm().indexOf(".ppt")!=-1||fjcfbDto_t.getWjm().indexOf(".pptx")!=-1){
							DBEncrypt p = new DBEncrypt();
							String wjljjm=p.dCode(fjcfbDto_t.getWjlj());
							boolean sendFlg=fjcfbService.sendWordFile(wjljjm);
							if(sendFlg) {
								Map<String,String> map= new HashMap<>();
								String fwjm=p.dCode(fjcfbDto_t.getFwjm());
								map.put("wordName", fwjm);
								map.put("fwjlj",fjcfbDto_t.getFwjlj());
								map.put("fjid",fjcfbDto_t.getFjid());
								map.put("ywlx",fjcfbDto_t.getYwlx());
								map.put("MQDocOkType",DOC_OK);
								//发送Rabbit消息转换pdf
								amqpTempl.convertAndSend("doc2pdf_exchange", MQTypeEnum_pub.CONTRACE_BASIC_W2P.getCode(),JSONObject.toJSONString(map));
							}
						}
					}
				}
			}
		}
		return true;
	 }
	 /**
	  * 修改保存基础数据并判断是否同类型下已存在默认数据
	  */
	 public boolean updateJcsj(JcsjDto jcsjDto) throws BusinessException {
		 String sfmr=jcsjDto.getSfmr();
		 JcsjDto sfczmrsj=getdefault(jcsjDto);
		 if(("1").equals(sfmr) && sfczmrsj!=null) {
			 throw new BusinessException("msg","不允许存在多条默认数据!");
		 }
		 update(jcsjDto);
    	 //redis同步修改一条基础数据
    	 //redisUtil.hset("matridx_jcsj:"+jcsjDto.getJclb(), jcsjDto.getCsid(), JSONObject.toJSONString(jcsjDto),-1);
		 //更新该基础类别的基础数据（list）
		 resetRedisJcsjList(jcsjDto.getJclb());
		 //修改附件
		 if(jcsjDto.getFjids_D()!=null && !jcsjDto.getFjids_D().isEmpty()) {
			 FjcfbDto fjcfbDto=new FjcfbDto();
			 fjcfbDto.setYwlx(jcsjDto.getYwlx_D());
			 fjcfbDto.setYwid(jcsjDto.getCsid());
			 JcsjDto jcsjDto_t=new JcsjDto();
			 jcsjDto_t.setFjids(jcsjDto.getFjids_D());
			 jcsjDto_t.setCsid(jcsjDto.getCsid());
			 fileHandling(jcsjDto_t,fjcfbDto);
		 }
		 
		 if(jcsjDto.getFjids_R()!=null && !jcsjDto.getFjids_R().isEmpty()) {
			 FjcfbDto fjcfbDto=new FjcfbDto();
			 fjcfbDto.setYwlx(jcsjDto.getYwlx_R());
			 fjcfbDto.setYwid(jcsjDto.getCsid());
			 JcsjDto jcsjDto_t=new JcsjDto();
			 jcsjDto_t.setFjids(jcsjDto.getFjids_R());
			 jcsjDto_t.setCsid(jcsjDto.getCsid());
			 fileHandling(jcsjDto_t,fjcfbDto);
		 }
		 
		 if(jcsjDto.getFjids_C()!=null && !jcsjDto.getFjids_C().isEmpty()) {
			 FjcfbDto fjcfbDto=new FjcfbDto();
			 fjcfbDto.setYwlx(jcsjDto.getYwlx_C());
			 fjcfbDto.setYwid(jcsjDto.getCsid());
			 JcsjDto jcsjDto_t=new JcsjDto();
			 jcsjDto_t.setFjids(jcsjDto.getFjids_C());
			 jcsjDto_t.setCsid(jcsjDto.getCsid());
			 fileHandling(jcsjDto_t,fjcfbDto);
		 }
		 return true;
	 }

	 /*
	   根据基础类别和扩展参数获取检测项目
	   @param jcsjDto
	  * @return
	  */
//	@Override
//	public List<JcsjDto> getDtoByJclbAndCskz1(JcsjDto jcsjDto) {
//		// TODO Auto-generated method stub
//		return dao.getDtoList(jcsjDto);
//	}
	/**
	  * 根据基础类别和扩展参数获取检测项目
	  */
	@Override
	public List<JcsjDto> getDtoByJclbAndCskz(JcsjDto jcsjDto) {
		// TODO Auto-generated method stub
		return dao.getDtoList(jcsjDto);
	}
	@Override
	public List<JcsjDto> getCskz1NotNull(JcsjDto jcsjDto){
		// TODO Auto-generated method stub
		return dao.getCskz1NotNull(jcsjDto);
	}
	
	/**
	 * 获取正式文件夹下的文件路径
	 */
	private String getFilePath(String wjid) {
		Map<Object,Object> mFile = redisUtil.hmget("IMP_:_"+wjid);
		//文件路径
		String pathString = (String)mFile.get("fwjlj");
		//分文件名
		String fwjm = (String)mFile.get("fwjm");
		String t_path = pathString.substring(prefix.length() + tempFilePath.length());
		//分文件路径
		return prefix + releaseFilePath + t_path+"/"+fwjm;
	}
	
	/**
	 * 根据ids查询基础数据
	 */
	public List<JcsjDto> getJcsjByids(JcsjDto jcsjDto){
		return dao.getInstopDtoList(jcsjDto);
	}
	
	/**
	 * 根据基础类别和参数名称查询基础信息
	 */
	public JcsjDto getDtoByCsmcAndJclb(JcsjDto jcsjDto) {
		return dao.getDto(jcsjDto);
	}

	/**
	 * 查询停用数据
	 */
	@Override
	public List<JcsjDto> getInstopDtoList(JcsjDto jcsjDto) {
		// TODO Auto-generated method stub
		return dao.getInstopDtoList(jcsjDto);
	}
	
	/**
	 * 根据基础类别和cskz3查询基础数据信息
	 */
	public JcsjDto getDtoByKzcs(JcsjDto jcsjDto) {
		return dao.getDto(jcsjDto);
	}

	/**
	 * 获取参数排序最大值
	 */
	@Override
	public Integer getMax(JcsjDto jcsjDto) {
		return dao.getMax(jcsjDto);
	}
	
	/*
	  修改参数名称
	  @param jcsjDto
	 * @return
	 */
//	@Override
//	public boolean updateXxlx(JcsjDto jcsjDto) {
//		return dao.update(jcsjDto) != 0;
//	}
	
	/*
	  删除消息类别
	  @param jcsjDto
	 * @return
	 */
//	@Override
//	public boolean deleteXxlx(JcsjDto jcsjDto) {
//		jcsjDto.setScbj("1");
//		return dao.update(jcsjDto)!=0;
//	}

	/**
	 * 伙伴列表页面获取可选择的检测单位
	 */
	@Override
	public List<JcsjDto> getPagesOptionJcdw(JcsjDto jcsjDto) {
		// TODO Auto-generated method stub
		return dao.getPagesOptionJcdw(jcsjDto);
	}
	
	/**
	 * 查询检测项目
	 */
	@Override
	public List<JcsjDto> selectDetectionUnit(JcsjDto jcsjDto) {
		// TODO Auto-generated method stub
		if (null == jcsjDto.getIds() || jcsjDto.getIds().size()<= 0 ){
			jcsjDto.setSfmr("1");
		}
		return dao.getDtoList(jcsjDto);
	}

	/**
	 * 根据父ID查询基础数据信息
	 */
	@Override
	public List<JcsjDto> getListByFid(JcsjDto jcsjDto) {
		// TODO Auto-generated method stub
		return dao.getInstopDtoList(jcsjDto);
	}

	/**
	 * 根据参数代码和基础类别查找数据
	 */
	@Override
	public JcsjDto getDtoByCsdmAndJclb(JcsjDto jcsjDto) {
		// TODO Auto-generated method stub
		return dao.getDto(jcsjDto);
	}
	/**
	 * 证件类型查找
	 */
	public List<JcsjDto> getZjlx(){
		// TODO Auto-generated method stub
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto .setJclb("IDCARD_CATEGORY");
		return dao.getDtoList(jcsjDto);
	}

	@Override
	public Map<String, List<JcsjDto>> getDtoListByType(BasicDataTypeEnum[] basicDateTypes) {
		if(basicDateTypes == null)
			return null;
		
		Map<String, List<JcsjDto>> result = new HashMap<>();
		for (BasicDataTypeEnum basicDateType : basicDateTypes) {
			List<JcsjDto> jcsjList = redisUtil.lgetDto("List_matridx_jcsj:" + basicDateType.getCode());

			if (jcsjList.isEmpty()) {
				jcsjList = resetRedisJcsjList(basicDateType.getCode());
				logger.error("getDtoListByType未找到数据重新查询：" + basicDateType.getCode() + " 重新查询结果为:" + jcsjList.size());
			}

			result.put(basicDateType.getCode(), jcsjList);
		}
		
		/*Map<String, List<JcsjDto>> result = new HashMap<String, List<JcsjDto>>();

		for(int a=0,al=basicDateTypes.length; a<al; a++){
			JcsjDto jcsjDto = new JcsjDto();
			jcsjDto.setJclb(basicDateTypes[a].getCode());
			List<JcsjDto> jcList = dao.getDtoList(jcsjDto);
			List<JcsjDto> tjcList = new ArrayList<>();
			if(jcList!=null && jcList.size() > 0){
				for(int i=0;i< jcList.size();i++){
					if("0".equals(jcList.get(i).getScbj())){
						tjcList.add(jcList.get(i));
					}
				}
			}
			result.put(basicDateTypes[a].getCode(), tjcList);
		}*/

		return result;
	}

	@Override
	public List<JcsjDto> getAllDtoList() {
		return dao.getAllDtoList();
	}

	/**
	 * 基础数据的所有基础类别
	 */
	@Override
	public List<String> getJclbxx() {
		return dao.getJclbxx();
	}

	/**
	 * 根据类别查找该类别的所有基础数据
	 */
	@Override
	public List<JcsjDto> getDtoListBylb(String jclb) {
		JcsjDto jcsjDto = new JcsjDto();
		jcsjDto.setJclb(jclb);
		return dao.getDtoList(jcsjDto);
	}

	@Override
	public JcsjDto getByAndCsdm(JcsjDto jcsjDto) {
		return dao.getDto(jcsjDto);
	}

	/**
	 * 更新该基础类别的基础数据（list）
	 */
	@Override
	public List<JcsjDto> resetRedisJcsjList(String jclb){
		JcsjDto jclbDto = new JcsjDto();
		jclbDto.setJclb(jclb);
		
//		List<JcsjDto> dtolist = dao.getDtoList(jclbDto);

		String uuid=StringUtil.generateUUID();
		List<JcsjDto> dtolist = dao.getNormalList(jclbDto);
		int redisSizeBefore = redisUtil.lgetDto("List_matridx_jcsj:" + jclbDto.getJclb()).size();
		try {
			if(!redisUtil.setIfAbsent("redis-lock",uuid,5L, TimeUnit.SECONDS)){
				Map<Object,Object>map=redisUtil.hmget("changjcsj");
				Map<String,Object> newmap=new HashMap<>();
				for(Object obj : map.values()) {
					newmap.put(obj.toString(),obj.toString());
				}
				newmap.put(jclbDto.getJclb(),jclbDto.getJclb());
				redisUtil.hmset("changjcsj", newmap);
			}
			redisUtil.del("matridx_jcsj:" + jclbDto.getJclb());
			redisUtil.del("JCSJ_DISABLE:"+"matridx_jcsj_"+jclbDto.getJclb());
			redisUtil.del("List_matridx_jcsj:" + jclbDto.getJclb());
			redisUtil.del("All_matridx_jcsj:" + jclbDto.getJclb());

			for (JcsjDto jcsjDto : dtolist) {
				if ("2".equals(jcsjDto.getScbj())) {//停用数据
					redisUtil.hset("JCSJ_DISABLE:" + "matridx_jcsj_" + jcsjDto.getJclb(), jcsjDto.getCsid(), JSONObject.toJSONString(jcsjDto), -1);
				} else {//正常数据
					redisUtil.hset("matridx_jcsj:" + jcsjDto.getJclb(), jcsjDto.getCsid(), JSONObject.toJSONString(jcsjDto), -1);
					redisUtil.lSet("List_matridx_jcsj:" + jcsjDto.getJclb(), JSONObject.toJSONString(jcsjDto), -1);
				}
				//包含停用的基础数据（不包含删除的）
				redisUtil.lSet("All_matridx_jcsj:" + jcsjDto.getJclb(), JSONObject.toJSONString(jcsjDto), -1);
			}

			int redisSizeAfter = redisUtil.lgetDto("List_matridx_jcsj:" + jclbDto.getJclb()).size();
			if (redisSizeBefore!=redisSizeAfter){
				logger.error("-----基础数据大小不一致，redisSizeBefore:"+redisSizeBefore+";redisSizeAfter:"+redisSizeAfter+";数据库:"+dtolist.size());
			}
		}catch (Exception e){
			logger.error("修改基础数据错误:"+e.getMessage());
		}finally {
			if(uuid.equals(redisUtil.get("redis-lock"))){
				redisUtil.del("redis-lock");
			}
		}


		return dtolist;
	}


	/**
	 * 推送基础数据List至微信
	 */
	@Override
	public Map<String, Object> pushJcsjLisToWechat(JcsjDto jcsjDto){
		Map<String, Object> resultMap= new HashMap<>();
		Map<String,Object> jsonMap = new HashMap<>();
		List<JcsjDto> jcsjDtos = dao.getJcsjListByIdsAndJclb(jcsjDto);
		jsonMap.put("jcsjDtos",jcsjDtos);
		if (jcsjDto.getIds()!=null){
			//推送部分
			if (!jcsjDtos.isEmpty()){
				jsonMap.put("key","part");
				amqpTempl.convertAndSend("wechat.exchange", "matridx.basicdata.push", JSONObject.toJSONString(jsonMap));
				resultMap.put("status","success");
				resultMap.put("message","推送成功！");
				return resultMap;
			}
		}else if (jcsjDto.getJclb()!=null){
			//推送该类别
			jsonMap.put("key","all");
			amqpTempl.convertAndSend("wechat.exchange", "matridx.basicdata.push", JSONObject.toJSONString(jsonMap));
			resultMap.put("status","success");
			resultMap.put("message","推送成功！");
			return resultMap;
		}
		resultMap.put("status","fali");
		resultMap.put("message","无数据可推送！");
		return resultMap;
	}

	/**
	 * 根据csid查询基础数据（所有）
	 */
	public JcsjDto getJcsjByCsid(JcsjDto jcsjDto){
		return dao.getJcsjByCsid(jcsjDto);
	}
	/**
	 * 删除某一基础类别(慎用)
	 */
	public boolean deleteByJclb(JcsjDto jcsjDto){
		return dao.deleteByJclb(jcsjDto);
	}
	/**
	 * 批量新增基础数据
	 */
	public int batchInsertJcsjDtos(List<JcsjDto> jcsjDtos){
		int i = dao.batchInsertJcsjDtos(jcsjDtos);
		resetRedisJcsjList(jcsjDtos.get(0).getJclb());
		return i;
	}
	/**
	 * 批量新增
	 */
	public boolean insertList(List<JcsjDto> list){
		if(list == null || list.size() == 0)
			return false;
		boolean result = dao.insertList(list);
		resetRedisJcsjList(list.getFirst().getJclb());
		return result;
	}
	/**
	 * 批量修改删除标记
	 */
	public boolean updateListScbj(List<JcsjDto> list){
		return dao.updateListScbj(list);
	}

	/**
	 * 查询ResFirst取值范围
	 */

	public JcsjDto getResFirstInfo(){
		return dao.getResFirstInfo();
	}

	@Override
	public List<JcsjDto> getListByFfid(JcsjDto jcsjDto) {
		return dao.getListByFfid(jcsjDto);
	}

	@Override
	public List<JcsjDto> getJcsjListByIdsAndJclb(JcsjDto jcsjDto) {
		return dao.getJcsjListByIdsAndJclb(jcsjDto);
	}

	/**
	 * 根据基础数据类别获取基础数据及子级数据
	 * @param jclbDto
	 * @return
	 */
	public List<JcsjDto> getDtoAndSubListByJclb(JcsjDto jclbDto){
		return dao.getDtoAndSubListByJclb(jclbDto);
	}
}
