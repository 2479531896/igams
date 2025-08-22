package com.matridx.igams.experiment.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.experiment.dao.entities.NgsglDto;
import com.matridx.igams.experiment.dao.entities.NgsglModel;


@Mapper
public interface INgsglDao extends BaseBasicDao<NgsglDto, NgsglModel>{

	/**
	 * 接收ngs信息并保存
	 */
	boolean addSaveNgsDtos(List<NgsglDto> list);
	
	/**
	 * 根据制备编码和内部编码获取相应信息
	 */
	List<NgsglDto> getDtoByZbbmAndNbbm(NgsglDto ngsglDto);
	
	/**
	 * 根据检测ID获取ngs信息
	 */
	NgsglDto getDtoByJcid(NgsglDto ngsglDto);
	
	/**
	 * 获取NGS所有信息
	 */
	List<NgsglDto> getPageNgsglDto(NgsglDto ngsglDto);
	
	/**
	 * 根据ngsid获取ngs详细信息
	 */
	NgsglDto getDtoByngsid(String ngsid);
	
	/**
	 * 筛选
	 */
	List<NgsglDto> queryBySfcg();
	
	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForSearchExp(NgsglDto ngsglDto);
	
	/**
	 * 从数据库分页获取导出数据
	 */
	List<NgsglDto> getListForSearchExp(NgsglDto ngsglDto);
	
	/**
	 * 从数据库分页获取导出数据
	 */
	List<NgsglDto> getListForSelectExp(NgsglDto ngsglDto);

    List<String> getSjxxByLikeNbbm(String nbbm);
}
