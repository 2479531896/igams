package com.matridx.igams.storehouse.service.svcinterface;

import java.util.List;
import java.util.Map;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.DhxxDto;
import com.matridx.igams.storehouse.dao.entities.DhxxModel;

public interface IDhxxService extends BaseBasicService<DhxxDto, DhxxModel>{
	/**
	 * 获取审核列表 
	 * @param dhxxDto
	 * @return
	 */
	List<DhxxDto> getPagedAuditDhxx(DhxxDto dhxxDto);
	
	/**
	 * 获取到货列表 
	 * @param dhxxDto
	 * @return
	 */
    List<DhxxDto> getPagedDtoList(DhxxDto dhxxDto);
	
	/**
	 * 判断到货单号是否重复
	 * @param dhdh
	 * @param dhid
	 * @return
	 */
    boolean isDhdhRepeat(String dhdh, String dhid);
	
	/**
	 * 保存到货信息
	 * @param dhxxDto
	 * @return
	 */
    boolean addSaveArrivalGoods(DhxxDto dhxxDto)throws BusinessException;

	/**
	 * 保存采购红字
	 * @param dhxxDto
	 * @return
	 */
    boolean purchaseAdd(DhxxDto dhxxDto)throws BusinessException;
	/**
	 * 修改采购红字
	 * @param dhxxDto
	 * @return
	 */
    boolean purchaseMod(DhxxDto dhxxDto) throws BusinessException;
	/**
	 * 修改保存到货信息
	 * @param dhxxDto
	 * @return
	 */
    boolean modSaveArrivalGoods(DhxxDto dhxxDto) throws BusinessException;
	
	/**
	 * 自动生成到货单号
	 * @return
	 */
    String generateDhdh();
	
	/**
	 * 根据搜索条件获取导出条数
	 * @param dhxxDto
	 * @return
	 */
    int getCountForSearchExp(DhxxDto dhxxDto, Map<String, Object> params);
	
	/**
	 * 检查能否删除到货信息
	 * @param dhxxDto
	 * @return
	 */
    Map<String,Object> checkscqx(DhxxDto dhxxDto);
	
	/**
	 * 删除到货单
	 * @param dhxxDto
	 * @return
	 */
    boolean deleteDhxx(DhxxDto dhxxDto) throws BusinessException;


	/**
	 * 采购红字删除
	 * @param dhxxDto
	 * @return
	 */
    boolean purchaseDel(DhxxDto dhxxDto) throws BusinessException;
	
	/**
	 * 根据到货ids获取到货信息（共通页面）
	 * @param dhxxDto
	 * @return
	 */
    List<DhxxDto> getCommonDtoListByDhids(DhxxDto dhxxDto);

	/**
	 * 确认按钮仅更新到货信息的确认人员/标记/时间信息
	 * @param dhxxDto
	 * @return
	 */
	boolean updateConfirmDhxx(DhxxDto dhxxDto);
	/**
	 * 高级修改保存到货信息
	 * @param dhxxDto
	 * @return
	 */
    boolean advancedmodsave(DhxxDto dhxxDto) throws BusinessException;
	
	/**
	 * 查找到货列表的借用总量和归还总量
	 * @return
	 */
	List<DhxxDto> getJyzlAndGhzlList();
	/**
	 * 到货列表钉钉
	 */
	List<DhxxDto> getPagedDtoListDingTalk(DhxxDto dhxxDto);
	/**
	 * 出入库统计
	 * @param dhxxDto
	 * @return
	 */
    List<DhxxDto> getCountStatistics(DhxxDto dhxxDto);
}
