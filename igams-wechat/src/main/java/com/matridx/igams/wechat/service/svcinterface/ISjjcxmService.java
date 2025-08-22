package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjjcxmDto;
import com.matridx.igams.wechat.dao.entities.SjjcxmModel;
import com.matridx.igams.wechat.dao.entities.SjtssqDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;

import java.util.List;

public interface ISjjcxmService extends BaseBasicService<SjjcxmDto, SjjcxmModel>{

	/**
	 * 根据送检信息新增检测项目
	 * @param sjxxDto
	 * @return
	 */
	boolean insertBySjxx(SjxxDto sjxxDto);


	/**
	 * 根据送检信息新增检测项目
	 * @param sjxxDto
	 * @return
	 */
	boolean syncInfo(SjxxDto sjxxDto) throws BusinessException;

	/**
	 * 根据送检信息新增检测项目
	 * @param sjjcxmDtos
	 * @return
	 */
	Boolean insertSjjcxmDtos(List<SjjcxmDto> sjjcxmDtos);
	
	/**
	 * 根据送检信息新增检测项目
	 * @param sjxxDto
	 * @return
	 */
	boolean insertSjjcxm(SjxxDto sjxxDto,String yhid);

	/**
	 * 根据项目管理id修改
	 * @param list
	 * @return
	 */
	Boolean updateListNew(List<SjjcxmDto> list);
	/**
	 * 获取检测项目所以数据
	 * @param sjjcxmDto
	 * @return
	 */
    List<SjjcxmDto> getAllDtoList(SjjcxmDto sjjcxmDto);
		/**
	 * 更改应收账款信息
	 * @param dto
	 * @return
	 */
	int updateYszkInfoToNull(SjjcxmDto dto);

	/**
	 * 置导出标记为null
	 * @param dto
	 * @return
	 */
	Boolean updateDcEmpty(SjjcxmDto dto);

	/**
	 * 批量更新对账数据
	 * @param sjjcxmDtos
	 * @return
	 */
	int updateListDzInfo(List<SjjcxmDto> sjjcxmDtos);

	/**
	 * 批量修改
	 * @param sjjcxmDtos
	 * @return
	 */
	Boolean updateList(List<SjjcxmDto> sjjcxmDtos);
	/**
	 *查询送检检测项目
	 * @param sjid
	 * @return
	 */
    List<String> getSjjcxm(String sjid);

	/**
	 * 根据送检信息修改检测项目
	 * @param sjxxDto
	 * @return
	 */
	boolean updateBySjxx(SjxxDto sjxxDto,SjxxDto oldDto);

	/**
	 * 获取检测项目信息
	 * @param sjxxDto
	 * @return
	 */
    List<SjjcxmDto> getSjjcxmDtos(SjxxDto sjxxDto);

	/**
	 * 根据sjid查询检测项目信息
	 * @param sjids
	 * @return
	 */
	List<SjjcxmDto> getListBySjid(List<String> sjids);
	/**
	 * 根据sjid清空检测子项目
	 * @param sjid
	 * @return
	 */
	boolean emptySubDetect(String sjid) ;

	/**
	 * 根据送检信息更新检测项目
	 * @param sjjcxmDtos
	 * @return
	 */
	int updateSjjcxmDtos(List<SjjcxmDto> sjjcxmDtos);
	/**
	 * 重新调整应付金额
	 * @return
	 */
    void readjustPayment(SjxxDto sjxxDto, List<SjjcxmDto> sjjcxmDtos, List<SjtssqDto> sjtssqDtos);
	/**
	 * 更新导出标记
	 * @param sjjcxmDto
	 * @return
	 */
	boolean updateDcbj(SjjcxmDto sjjcxmDto);
}
