package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JcsjModel;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;

import java.util.List;
import java.util.Map;

public interface IJcsjService extends BaseBasicService<JcsjDto, JcsjModel>{
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
	 void handleCodeToValue(List<?> dtoList, BasicDataTypeEnum[] basicDateTypes, String[] fieldNames);
	
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
	 void handleCodeToValue(List<?> dtoList, BasicDataTypeEnum basicDateType, String csidFieldName, String csmcFieldName);
	
	/**
	 * 根据参数类别获取参数列表，允许一次多个，不包含停用数据
	 */
	 Map<String, List<JcsjDto>> getDtoListbyJclb(BasicDataTypeEnum[] basicDateTypes);
	
	/**
	 * 根据参数类别获取参数列表，允许一次多个，不包含停用数据
	 */
	 List<JcsjDto> getDtoListbyJclb(BasicDataTypeEnum basicDateTypes);
	
	/**
	 * 根据参数类别获取参数列表，允许一次多个，包含停用数据
	 */
	 Map<String, List<JcsjDto>> getDtoListbyJclbInStop(BasicDataTypeEnum[] basicDateTypes);
	
	/**
	 * 根据参数类别获取参数列表，允许一次，包含停用数据
	 */
	 List<JcsjDto> getDtoListbyJclbInStop(BasicDataTypeEnum basicDateTypes);
	
	/**
     * 查询基础数据类别列表
     */
     List<Map<String, String>> getJclbList();
    
    /**
	 * 获取列表(不保存到缓存中)
	 */
	 List<JcsjDto> getJcsjDtoList(JcsjDto jcsjDto);
	/**
	 * 获取列表(不保存到缓存中)
	 */
	 List<JcsjDto> getJcsjDtoListAndJl(JcsjDto jcsjDto);
	
	/**
	 * 获取列表，包含停用数据(不保存到缓存中)
	 */
	 List<JcsjDto> getJcsjDtoListInStop(JcsjDto jcsjDto);
	
	/**
	  * 根据省份从基础数据中查询城市
	  */
	  List<JcsjDto>  jcsjcity(JcsjDto jcsjDto);
	 
	 /**
	  * 查询默认的类型
	  */
	  JcsjDto getdefault(JcsjDto jcsjDto);
	 
	 /**
	  * 新增保存基础数据并判断是否同类型下已存在默认数据
	  */
	  boolean insertJcsj(JcsjDto jcsjDto) throws BusinessException;
	 
	 /**
	  * 修改保存基础数据并判断是否同类型下已存在默认数据
	  */
	  boolean updateJcsj(JcsjDto jcsjDto) throws BusinessException;

//	 /**
//	  * 根据基础类别和扩展参数获取检测项目
//	  * @param jcsjDto
//	  * @return
//	  */
//	 List<JcsjDto> getDtoByJclbAndCskz1(JcsjDto jcsjDto);
	/**
	  * 根据基础类别和扩展参数获取检测项目
	  */
	 List<JcsjDto> getDtoByJclbAndCskz(JcsjDto jcsjDto);
	
	/**
	 * 通过类别查询扩展参数为1基础数据
	 */
	 List<JcsjDto> getCskz1NotNull(JcsjDto jcsjDto);
	
	/**
	 * 根据ids查询基础数据
	 */
	 List<JcsjDto> getJcsjByids(JcsjDto jcsjDto);
	 
	/**
	 * 根据基础类别和参数名称查询基础信息
	 */
	 JcsjDto getDtoByCsmcAndJclb(JcsjDto jcsjDto);
	
	/**
	 * 查询停用数据
	 */
	 List<JcsjDto> getInstopDtoList(JcsjDto jcsjDto);
	
	/**
	 * 根据基础类别和cskz3查询基础数据信息
	 */
	 JcsjDto getDtoByKzcs(JcsjDto jcsjDto);
	
	/**
	 * 查询消息类别排序最大值
	 */
	 Integer getMax(JcsjDto jcsjDto);
	
//	/**
//	 * 修改消息类别
//	 * @param jcsjDto
//	 * @return
//	 */
//	 boolean updateXxlx(JcsjDto jcsjDto);
//
//
//	/**
//	 * 删除消息类别
//	 * @param jcsjDto
//	 * @return
//	 */
//	 boolean deleteXxlx(JcsjDto jcsjDto);

	/**
	 * 伙伴列表页面获取可选择的检测单位
	 */
	 List<JcsjDto> getPagesOptionJcdw(JcsjDto jcsjDto);
	
	/**
	 * 查询检测项目
	 */
	 List<JcsjDto> selectDetectionUnit(JcsjDto jcsjDto);

	/**
	 * 根据父ID查询基础数据信息
	 */
	 List<JcsjDto> getListByFid(JcsjDto jcsjDto);

	/**
	 * 根据csdm和jclb去查找数据
	 */
	 JcsjDto getDtoByCsdmAndJclb(JcsjDto jcsjDto);

	/**
	 * 基础数据的所有基础类别
	 */
	 List<String> getJclbxx();

	/**
	 * 根据类别查找该类别的所有基础数据
	 */
	 List<JcsjDto> getDtoListBylb(String jclb);
	/**
	 * 更具参数代码和基础类型查找
	 */
	 JcsjDto getByAndCsdm(JcsjDto jcsjDto);
	/**
	 * 证件类型查找
	 */
	 List<JcsjDto> getZjlx();

	/**
	 * 根据参数类别获取参数列表，允许一次多个
	 */
	 Map<String, List<JcsjDto>> getDtoListByType(BasicDataTypeEnum[] basicDateTypes);

    List<JcsjDto> getAllDtoList();

	/**
	 * 更新该基础类别的基础数据（list）
	 */
    List<JcsjDto> resetRedisJcsjList(String jclb);
	/**
	 * 推送基础数据List至微信
	 */
    Map<String,Object> pushJcsjLisToWechat(JcsjDto jcsjDto);
	/**
	 * 根据csid查询基础数据（所有）
	 */
	JcsjDto getJcsjByCsid(JcsjDto jcsjDto);
	/**
	 * 删除某一基础类别(慎用)
	 */
	boolean deleteByJclb(JcsjDto jcsjDto);

	/**
	 * 批量新增基础数据
	 */
	int batchInsertJcsjDtos(List<JcsjDto> jcsjDtos);
	/**
	 * 批量新增
	 */
	 boolean insertList(List<JcsjDto> list);
	/**
	 * 批量修改删除标记
	 */
	 boolean updateListScbj(List<JcsjDto> list);


	/**
	 * 查询ResFirst取值范围
	 */

	 JcsjDto getResFirstInfo();
	/**
	 * 通过父父id获取list
	 */
	List<JcsjDto> getListByFfid(JcsjDto jcsjDto);

	/**
	 * 通过ids和类别获取list
	 */
	List<JcsjDto> getJcsjListByIdsAndJclb(JcsjDto jcsjDto);

	/**
	 * 根据基础数据类别获取基础数据及子级数据
	 * @param jclbDto
	 * @return
	 */
	List<JcsjDto> getDtoAndSubListByJclb(JcsjDto jclbDto);
}
