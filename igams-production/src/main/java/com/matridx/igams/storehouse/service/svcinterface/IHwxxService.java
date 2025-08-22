package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.storehouse.dao.entities.DhxxDto;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.entities.HwxxModel;

import java.util.List;
import java.util.Map;

public interface IHwxxService extends BaseBasicService<HwxxDto, HwxxModel>{
	/**
	 * 获取待检验列表 
	 * @param
	 * @return
	 */
	List<HwxxDto> getPagedDtoDjyList(HwxxDto hwxxDto);

	/**
	 * 获取贵重物料的数据
	 * @param
	 * @return
	 */
	List<HwxxDto> getGZDtoList(HwxxDto hwxxDto);

	/**
	 * 根据入库id查询信息
	 * @param
	 * @return
	 */
	List<HwxxDto> getHwxxByRkid(HwxxDto hwxxDto);

	/**
	 * copy新增保存到货明细信息
	 * @param list
	 * @return
	 */
	boolean copyInsertHwxxList(List<HwxxDto> list);

	/**
	 * 根据ids仓库信息
	 * @param hwxxDto
	 * @return
	 */
    List<HwxxDto> getCkInfo(HwxxDto hwxxDto);

	/**
	 * 获取待检验列表
	 * @param
	 * @return
	 */
	List<HwxxDto> getDtoListByLlid(HwxxDto hwxxDto);
	/**
	 * 根据合同明细ids获取入库明细数据
	 * @param htmxDot
	 * @return
	 */
	List<HwxxDto> getListByHtmxids(HtmxDto htmxDot);

	/**
	 * 显示待入库数据列表
	 * @param hwxxDto
	 * @return
	 */
	List<HwxxDto> getPagedStockPending(HwxxDto hwxxDto);

	/**
	 * 待入库列表查通过hwid看一条信息
	 * @param hwxxDto
	 * @return
	 */
	HwxxDto getOneByHwid(HwxxDto hwxxDto);
	/**
	 * 根据货物id查询货物信息。
	 * @param hwxxDto
	 * @return
	 */
    List<HwxxDto> getInfoByDhjyid(HwxxDto hwxxDto);


	/**
	 * 根据物料编码、仓库、库位、生产批号获取物料信息
	 * @param
	 * @return
	 */
    List<HwxxDto> getDtoByWlbmAndCkdmAndKwbhdmAndscph(HwxxDto hwxxDto);

	/**
	 * 新增保存到货明细信息
	 * @param list
	 * @return
	 */
	boolean insertHwxxList(List<HwxxDto> list);
	/**
	 * 修改保存
	 * @param list
	 * @param dhxxDto
	 * @return
	 */
	boolean updateHwxxList(List<HwxxDto> list,DhxxDto dhxxDto);
	
	/**
	 * 批量删除到货明细信息
	 * @param hwxxDto
	 * @return
	 */
	boolean deleteHwxxDtos(HwxxDto hwxxDto);
	
	/**
	 * 批量修改到货明细信息
	 * @param hwxxDtos
	 * @return
	 */
    boolean updateHwxxDtos(List<HwxxDto> hwxxDtos);

	/**
	 * 批量修改到货明细信息
	 * @param
	 * @return
	 */
    boolean updateHwInfos(List<HwxxDto> hwxxDtos);

	
	/**
	 * 根据搜索条件获取导出条数
	 * @return
	 */
    int getCountForSearchExp(HwxxDto hwxxDto, Map<String, Object> params);
	
	/**
	 * 批量修改到货明细信息中入库信息
	 * @param hwxxDtos
	 * @return
	 */
    boolean updateRkglDtos(List<HwxxDto> hwxxDtos);
	
	/**
	 * 获取货物选择列表
	 * @return
	 */
    List<HwxxDto> chooseListHwxx(HwxxDto hwxxDto);
	
	/**
	 * 根据合同明细ID获取到货信息
	 * @return
	 */
    List<HwxxDto> getDtoByHtmxid(HwxxDto hwxxDto);
		/**
	 * 根据ids查询货物信息
	 * @param hwxxDto
	 * @return
	 */
        List<HwxxDto> getHwxxByids(HwxxDto hwxxDto);
	/**
	 * 更具仓库ids 查询hwxx
	 */
    List<HwxxDto> getHwInfoByCkids(HwxxDto hwxxDto);
	/**
	 * 到货检验id置空
	 *
	 * @param hwxxDto
	 */
    void updateDhjyidEmpty(HwxxDto hwxxDto);
	/**
	 * 通过到货检验id货物id置空
	 * @param dto
	 * @return
	 */
    int updateDhjyidEmptyByDhjyids(HwxxDto dto);
	/**
	 * 通过到货检验id更新货物信息
	 *
	 * @param dto
	 */
    void updateHwxxByDhjyids(HwxxDto dto);
	/**
	 * 根据到货检验id查询货物信息
	 * @param dto
	 * @return
	 */
    List<HwxxDto> getListByDhjyid(HwxxDto dto);

	/**
	 * 更新请购明细到货数量和到货日期
	 *
	 * @param hwxxDtos
	 */
	void updateQgmxs(List<HwxxDto> hwxxDtos);
	
	/**
	 * 更新合同明细到货数量和到货日期
	 *
	 * @param hwxxDtos
	 */
	void updateHtmxs(List<HwxxDto> hwxxDtos);
	
	/**
	 * 是否发起退回申请
	 * @param hwxxDtos
	 * @param type
	 * @param yhid
	 * @throws BusinessException 
	 */
    void createArrivalGoodsBack(List<HwxxDto> hwxxDtos, String type, String yhid) throws BusinessException;
	
	/**
	 * 根据Dhids查询到货信息
	 * @param hwxxDto
	 * @return
	 */
    List<HwxxDto> getListByDhids(HwxxDto hwxxDto);

	/**
	 * 根据入库IDs获取货物信息
	 * @param
	 * @return
	 */
    List<HwxxDto> getDtoListByrkids(HwxxDto hwxxDto);
	/**
	 * 判断追溯号是否重复
	 * @return
	 */
    String judgeZsh(List<HwxxDto> hwxxDtos);
	
	 /**
	  * 根据zt和dhid获取货物信息
	  * @param hwxxDto
	  * @return
	  */
     List<HwxxDto> getHwxxByZtAndDhids(HwxxDto hwxxDto);
	 
	/**
	 * 根据到货ID获取货物信息
	 * @param dhid
	 * @return
	 */
    List<HwxxDto> getListByDhid(String dhid);
	
	/**
	 * 根据入库ID查询货物信息
	 * @param rkid
	 * @return
	 */
    List<HwxxDto> getDtoListById(String rkid);
	
	/**
	  *	根据检验id获取领料信息
	  * @param hwxxDto
	  * @return
	  */
    List<HwxxDto> getDtoByDhjyid(HwxxDto hwxxDto);
	 
	 /**
	  *	根据检验id获取货物
	  * @return
	  */
     List<HwxxDto> getDtoListByJyId(String jyid);
	 
	 /**
	  * 批量更新货物信息调拨明细关联ID
	  * @param list
	  * @return
	  */
     boolean updateDbmxId(List<HwxxDto> list);
	 
	 /**
	  *	根据追溯号物料id分组查询
	  * @return
	  */
     List<HwxxDto> queryByDhid(String dhid);
	 
	 /**
	  * 通过追溯号物料id批量更新库存关联id
	  * @param list
	  * @return
	  */
     boolean updateByWlidAndZsh(List<HwxxDto> list);
	 
	 /**
	  * 批量更新货物数量
	  * @param list
	  * @return
	  */
     boolean updateRksl(List<HwxxDto> list);
	 
	 /**
	  * 根据入库ID获取货物信息(根据物料和追溯号分组)
	  * @param hwxxDto
	  * @return
	  */
     List<HwxxDto> queryByRkid(HwxxDto hwxxDto);
	 
	 /**
	  * 根据检验ID获取货物信息(根据物料和追溯号分组)
	  * @param hwxxDto
	  * @return
	  */
     List<HwxxDto> queryByJyid(HwxxDto hwxxDto);
	 
	 /**
	  * 根据物料id获取物料信息
	  * @param hwxxDto
	  * @return
	  */
     List<HwxxDto> queryWlglDtosByWlid(HwxxDto hwxxDto);
	 
	 /**
	  * 到货列表货物借用信息
	  * @param hwxxDto
	  * @return
	  */
	 List<HwxxDto> getBorrowList(HwxxDto hwxxDto);
	 
	 /**
	  * 到货列表货物归还信息
	  * @param hwxxDto
	  * @return
	  */
	 List<HwxxDto> getReturnBackList(HwxxDto hwxxDto);
	 
	 /**
	  * 更新归还的货物信息
	  * @param hwxxDtos
	  * @return
	  */
	boolean updateHwxxReturn(List<HwxxDto> hwxxDtos);
	
	/**
	 * 更新借用的货物信息
	 * @param hwxxDtos
	 * @return
	 */
	boolean updateHwxxBorrow(List<HwxxDto> hwxxDtos);
	
	/**
	 * 获取借用/归还总量
	 * @param hw_jyzl
	 * @return
	 */
	HwxxDto getJyGhzlByDhid(HwxxDto hw_jyzl);

	/**
	 * 更改hw信息
	 *
	 * @param hwxxDto
	 */
	void updateByHwId(HwxxDto hwxxDto);
	
	/**
	 * 根据物料id获取货物信息
	 * @param hwxxDto
	 * @return
	 */
	List<HwxxDto> queryHWxx(HwxxDto hwxxDto,String dqjs);
	/**
	 * 更新其他出入库明细ID
	 * @param list
	 * @return
	 */
	boolean updateqtcrk(List<HwxxDto> list);


	/**
	 * 获取追溯号流水
	 * @return
	 */
	String getcCodeSerialZsh(String prefix);
	
	/**
	 * 更具到货id查找hwxx
	 */
	List<HwxxDto> getHwxxListByDhid(HwxxDto hwxxDto);
	
	/**
	 * 由spbm(jgid)获取部门的参数扩展一
	 * @param sqbm
	 * @return
	 */
	String getHwxxjgdh(String sqbm);
	/**
	 * 根据物料id获取货物信息
	 * @param hwxxDto
	 * @return
	 */
	List<HwxxDto> selectHWxx(HwxxDto hwxxDto);
	/**
	 * 全部库存列表
	 *
	 * @param hwxxDto
	 * @return
	 */
    List<HwxxDto> getPagedDtoAllStockList(HwxxDto hwxxDto);
	/**
	 * 根据hwid查看货物全部信息
	 * @param hwxxDto
	 * @return
	 */
    HwxxDto viewStockDto(HwxxDto hwxxDto);
	/**
	 * 从数据库分页获取导出数据
	 * @param
	 * @return
	 */
    List<HwxxDto> getStockListForSelectExp(Map<String, Object> params);
	/**
	 * 根据搜索条件获取导出条数
	 * @param hwxxDto
	 * @return
	 */
    int getStockCountForSearchExp(HwxxDto hwxxDto, Map<String, Object> params);
	/**
	 * 从数据库分页获取导出数据
	 * @param
	 * @return
	 */
    List<HwxxDto> getStockListForSearchExp(Map<String, Object> params);
	
	/**
	 * 新增保存到货明细信息
	 * @param list
	 * @return
	 */
	boolean insertHwxxs(List<HwxxDto> list);
	
	/**
	 * 获取待处理列表
	 * @param hwxxDto
	 * @return
	 */
	List<HwxxDto> getPagedDtoDclList(HwxxDto hwxxDto);

	/**
	 * 待处理列表查看
	 * @param hwxxDto
	 * @return
	 */
	HwxxDto viewPendingDetail(HwxxDto hwxxDto);
	/**
	 * 根据hwid查看仓库货物信息
	 * @param hwxxDto
	 * @return
	 */
    HwxxDto getCkhwxxByHwid(HwxxDto hwxxDto);
	
	/**
	 * 根据dhjyid查询
	 * @param hwxxDto
	 * @return
	 */
    List<HwxxDto> queryByDhjyid(HwxxDto hwxxDto);
	/**
	 * 根据入库id查询
	 */
    HwxxDto queryByRkxx(HwxxDto hwxxDto);
	/**
	 * 根据请购明细id查找
	 */
	List<HwxxDto> getDtolistByQgmxid(HwxxDto hwxxDto);
	/**
	 * 获取到货信息列表
	 * @param
	 * @return
	 */
	List<HwxxDto> getDtoListByDhid(HwxxDto hwxxDto);


	/**
	 * 获取到货信息列表
	 * @param
	 * @return
	 */
	List<HwxxDto> getListByChDhid(HwxxDto hwxxDto);
	

	/**
	 * 根据入库id查找信息
	 */
	List<HwxxDto> getListRkId(HwxxDto hwxxDto);

	/**
	 * 根据货物id查找生产批号和保修时间
	 */
	HwxxDto getScphAndBxsjByHwid(String hwid);
	/**
	 * 根据物料id查找信息
	 */
	List<HwxxDto> getPagedListByWlid(HwxxDto hwxxDto);

	/**
	 * 更新库存量
	 * @param list
	 * @return
	 */
	boolean updateKclList(List<HwxxDto> list);
	/**
	 * 修改到货数量
	 */
	boolean updateDhsls(List<HwxxDto> hwxxDtoList);
	/**
	 * 钉钉库存查看接口
	 */
    HwxxDto getDtoByHwid(String hwid);
	List<HwxxDto> getDtoListByRkid(HwxxDto hwxxDto);
	/**
	 * 请购数量统计
	 */
	List<HwxxDto> getPurchaseStatistics(String year);

    List<HwxxDto> getWlCountGroupByCk();

	List<HwxxDto> getWlSockByCkid(HwxxDto hwxxDto);

	List<HwxxDto> getWlSockxxByCkidAndWlid(HwxxDto hwxxDto);

    List<HwxxDto> getDtoByDhjyidWithYck(HwxxDto hwxxDto_t);

    List<HwxxDto> getHwxxByHtmxidsWithHz(HwxxDto hwxxDto);

	List<HwxxDto> getHwxxWithHz(HwxxDto hwxxDto);
	
	List<HwxxDto> getWlkcInfoGroupBy(HwxxDto hwxxDto);

	/**
	 * 获取货期
	 * @param hwxxDto
	 * @return
	 */
	List<HwxxDto> getLastestHq(HwxxDto hwxxDto);
	/**
	 * 获取领料数量
	 */
	List<HwxxDto> selectFiveMonthsLlsl(Map<String,Object> map);
	/**
	 * 获取在途数量
	 */
	List<HwxxDto> selectZtsl(HwxxDto hwxxDto);
	/**
	 * @description 获取关联设备
	 * @param hwxxDto
	 * @return
	 */
    List<HwxxDto> getPagedGlsbList(HwxxDto hwxxDto);
	/**
	 * 根据归还id获取货物信息
	 */
	List<HwxxDto> getHwxxByJcghids(HwxxDto hwxxDto);
	/**
	 * @description 获取不合格证书打印数据
	 */
	List<HwxxDto> getDtoListWithCertificate(HwxxDto hwxxDto);
	/**
	 * @description 上传报告，和记录附件
	 */
    boolean uploadSaveInspectionGoods(HwxxDto hwxxDto);
	/*
		设备验收新增 删除操作同步货物信息
	 */
    boolean updateForSbys(HwxxDto hwxxDto);

	/**
	 * 根据入库id，生产批号分组查询物料信息
	 */
	List<HwxxDto> queryBycBatch(HwxxDto hwxxDto);

	List<HwxxDto> orderByQueryByDhjyid(HwxxDto hwxxDto);
	/**
	 * @Description: 根据到货id分组查询
	 * @param hwxxDto
	 * @return java.util.List<com.matridx.igams.storehouse.dao.entities.HwxxDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/11/7 13:36
	 */
	List<HwxxDto> queryByDhidGroup(HwxxDto hwxxDto);
	/**
	 * @Description: 根据到货id更新状态
	 * @param hwxxDto
	 * @return int
	 * @Author: 郭祥杰
	 * @Date: 2024/11/7 14:01
	 */
	boolean updateByDhid(HwxxDto hwxxDto);

	/**
	 * @Description: 根据角色id查询库存
	 * @param hwxxDto
	 * @return java.util.List<com.matridx.igams.storehouse.dao.entities.HwxxDto>
	 * @Author: 郭祥杰
	 * @Date: 2025/8/18 16:16
	 */
	List<HwxxDto> getPagedDtoByJsid(HwxxDto hwxxDto);

	/**
	 * @Description: 更新库存
	 * @param hwxxDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2025/8/19 16:32
	 */
	boolean updateHwxxDtoByHwid(HwxxDto hwxxDto);

	/**
	 * @Description: 根据llid修改库存量
	 * @param hwxxDto
	 * @return boolean
	 * @Author: 郭祥杰
	 * @Date: 2025/8/20 15:18
	 */
	boolean updateKclDtoByLLid(HwxxDto hwxxDto);
}


