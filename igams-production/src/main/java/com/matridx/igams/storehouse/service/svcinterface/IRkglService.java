package com.matridx.igams.storehouse.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.RkglDto;
import com.matridx.igams.storehouse.dao.entities.RkglModel;

public interface IRkglService extends BaseBasicService<RkglDto, RkglModel>{

	/**
	 * 入库审核列表
	 * @param rkglDto
	 * @return
	 */
    List<RkglDto> getPagedAuditRkgl(RkglDto rkglDto);

	/**
	 * 入库单号生成
	 * @return
	 */
    String generatePutInStorageCode(RkglDto rkglDto);
	
	/**
	 * 判断入库单是否重复
	 * @return
	 */
    boolean isRkdhRepeat(String rkdh, String rkid);
	
	/**
	 * 保存入库信息
	 * @return
	 */
    boolean addSavePutInStorage(RkglDto rkglDto) throws BusinessException;
	
	/**
	 * 入库修改保存
	 * @return
	 */
    boolean modSavePutInStorage(RkglDto rkglDto)throws BusinessException;
	
	/**
	 * 入库删除废弃
	 * @return
	 */
    boolean deleteRkgl(RkglDto rkglDto) throws BusinessException;
	
	/**
	 * 根据搜索条件获取导出条数
	 * @param rkglDto
	 * @return
	 */
    int getCountForSearchExp(RkglDto rkglDto, Map<String, Object> params);
	
	/**
	 * 入库修改
	 * @param rkglDto
	 * @return
	 */
    boolean updateRkgl(RkglDto rkglDto) throws BusinessException ;

	/**
	 * 入库高级修改
	 * @param rkglDto
	 * @return
	 */
    boolean advancedUpdateRkgl(RkglDto rkglDto) throws BusinessException ;

	/**
	 * 根据入库ids获取入库信息（共通页面）
	 * @param rkglDto
	 * @return
	 */
    List<RkglDto> getCommonDtoListByRkids(RkglDto rkglDto);
	
	
	/**
	 * 更新入库关联ID
	 * @param rkglDto
	 * @return
	 */
    boolean updateGldb(RkglDto rkglDto);
	
	/**
	 * 自动生成u8调拨出入库单号
	 * @return
	 */
    String generatecCodeInRd8();
	
	/**
	 * 自动生成u8调拨出入库单号
	 * @return
	 */
    String generatecCodeInRd9();

	/**
	 * 获取入库列表钉钉
	 * @param rkglDto
	 * @return
	 */
    List<RkglDto> getPagedDtoListDingTalk(RkglDto rkglDto);
}
