package com.matridx.igams.common.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IFjcfbDao extends BaseBasicDao<FjcfbDto, FjcfbModel>{

	/**
	 * 转换失败更新相应的转换次数
	 */
	 int updateFjljFail(FjcfbModel fjcfbModel);
	
	/**
	 * 根据业务ID获取附件列表
	 * fjcfbDto
	 * 
	 */
	 List<FjcfbModel> getModelListByYwid(FjcfbDto fjcfbDto);
	
	/**
	 * 获取获取下一个序号
	 * fjModel
	 * 
	 */
	 String getDRNextSeqNum(FjcfbModel fjModel);
	
	/**
	 * 更新序号
	 * fjModel
	 * 
	 */
	 int updateSeqNum(FjcfbModel fjModel);

	/**
	 * 通过业务ID查询附件信息
	 * ywid
	 * 
	 */
	 List<FjcfbDto> selectFjcfbDtoByYwid(String ywid);

	/**
	 * 通过业务ID删除附件信息
	 * fjcfbModel
	 * 
	 */
	 int deleteByYwid(FjcfbModel fjcfbModel);

	/**
	 * 更新转换标记
	 * fjcfbModel
	 * 
	 */
	 int updateZhbj(FjcfbModel fjcfbModel);

	/**
	 * 通过业务ID和业务类型查询附件信息
	 * fjcfbDto
	 * 
	 */
	 List<FjcfbDto> selectFjcfbDtoByYwidAndYwlx(FjcfbDto fjcfbDto);

	/**
	 * 附件排序
	 * fjcfbDto
	 * 
	 */
	 int fileSort(FjcfbDto fjcfbDto);
	
	/**
	 * 转换后的文件新增一个zhwjxx
	 * fjcfbModel
	 * 
	 */
	 boolean updateZhwjxx(FjcfbModel fjcfbModel);
	
	/**
	 * 查询没有转换过的文件
	 * 
	 */
	 List<FjcfbDto> selectWord();
	
	 JcsjDto getdefault();

	/**
	 * 根据业务ID和业务类型删除附件信息
	 * fjcfbModel
	 * 
	 */
	 int deleteByYwidAndYwlx(FjcfbModel fjcfbModel);

	/**
	 * 根据业务IDs和业务类型删除附件信息
	 * fjcfbDto
	 * 
	 */
	 int deleteByYwidsAndYwlx(FjcfbDto fjcfbDto);
	
	/**
	 * 查询是否有转换为pdf的文件
	 * 
	 */
	 List<FjcfbDto> selectzhpdf(FjcfbDto fjcfbDto);

	/**
	 * 根据业务IDs和业务类型
	 * fj
	 * 
	 */
	 List<FjcfbDto> selectFjcfbDtoByYwidsAndYwlx(FjcfbDto fjcfbDto);

	/**
	 * 根据业务ID和序号查询
	 * fjcfbDtos
	 * 
	 */
	 List<FjcfbDto> getListByYwidAndXh(List<FjcfbDto> fjcfbDtos);

	/**
	 * 替换文件
	 * fjcfbModel
	 * 
	 */
	 int replaceFile(FjcfbModel fjcfbModel);
	
	/**
	 * 根据fjids删除附件
	 * fjcfbDto
	 * 
	 */
	 boolean delByFjids(FjcfbDto fjcfbDto);

	/**
	 * 根据业务ID和子业务ID查询附件信息
	 * fjcfbDto
	 * 
	 */
	 List<FjcfbDto> getListByZywid(FjcfbDto fjcfbDto);

    List<FjcfbDto> selectFjcfbDtoByYwidAndYwlxOrderByYwlx(FjcfbDto fjcfbDto);

	/**
	 * 批量更新附件存放表
	 * fjcfbDtos
	 * 
	 */
	int updateByYwidAndWjmhz(List<FjcfbDto> fjcfbDtos);

	/**
	 * 批量新增附件存放表信息
	 * list
	 * 
	 */
	int batchInsertFjcfb(List<FjcfbDto> list);

	/**
	 * 通过业务ID查询删除标记为0的附件信息
	 * yzid
	 * 
	 */
	List<FjcfbDto> getFjcfbDtoByYwid(String yzid);
	
	/**
	 * 通过附件IDs查询删除标记为0的附件信息
	 * yzid
	 * 
	 */
	List<FjcfbDto> selectFjcfbDtoByFjids(FjcfbDto fjcfbDto);

	/**
	 * 查询删除标记不等于1的附件信息
	 * fjcfbDto
	 * 
	 */
	FjcfbDto getDtoWithScbjNotOne(FjcfbDto fjcfbDto);
	/**
	 * 根据ywids获取附件list
	 * fjcfbDto
	 * 
	 */
	List<FjcfbDto> selectFjcfbDtoByIds(FjcfbDto fjcfbDto);
	
	/**
	 * 根据fjid 更新删除标记
	 * fjcfbDto
	 * 
	 */
	int deleteByFjid(FjcfbDto fjcfbDto);

	//根据业务ID和业务类型删除附件信息
	int updateScbjYwidAndYwlxd(FjcfbDto fjcfbDto);
	/*
    	获取附件存在情况
 	*/
	List<FjcfbDto> existFileInfo(FjcfbDto fjcfbDto);
	/*
    	根据文件路径获取附件信息List
 	*/
	List<FjcfbDto> getListByFilePaths(Map<String,Object> searchMap);

}
