package com.matridx.igams.production.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.QgglDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.dao.entities.QgmxModel;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;

import java.util.List;
import java.util.Map;

public interface IQgmxService extends BaseBasicService<QgmxDto, QgmxModel>{

	/**
	 * 添加请购明细数据
	 */
	boolean insertQgmx(List<QgmxDto> list);
	
	/**
	 * 取消请购操作
	 */
	boolean updateQxqg(String qgmxid);
	
	/**
	 * 修改请购明细
	 */
	boolean updateForList(List<QgmxDto> list);
	
	/**
	 * 请购明细修改
	 */
	boolean updateQgmx(List<QgmxDto> list);
	
	/**
	 * 根据物料子类别统称筛选条件查询相关请购明细数据
	 */
	List<QgmxDto> getMxfjByWlzlbtc(QgmxDto qgmxDto);
	
	/**
	 * 下载请购明细附件
	 */
	Map<String,Object> downloadQgmxFile(QgmxDto qgmxDto);
	
	/**
	 * 根据业务ID和子业务ID获取附件信息
	 */
	List<QgmxDto> getFileByYwidAndZywid(QgmxDto qgmxDto);
	
    
    /**
          * 根据请购明细ID清空关联ID
	 */
	boolean clearGlid(String qgmxid);

    /**
     * 更新取消请购和拒绝审批信息
	 */
	boolean updateQxqgxx(QgmxDto qgmxDto);
	
    /**
	 * 更新拒绝审批信息
	 */
	boolean updateJjspxx(QgmxDto qgmxDto);
    
    /**
     * 请购明细列表(不分页)
	 */
	List<QgmxDto> getQgmxList(QgmxDto qgmxDto);

	/**
	 * 修改请购明细信息
	 */
	boolean updatePurchaseDetail(List<QgmxDto> qgmxList, QgglDto qgglDto) throws BusinessException;

	/**
	 * 根据请购明细ID获取信息
	 */
	List<HtmxDto> getListByQgmxids(QgmxDto qgmxDto, String dqjs);

	/**
	 * 根据请购ID查询未填写统称物料
	 */
	List<QgmxDto> selectByQgid(String qgid);
	
	
	/**
	 * 获取请购物料列表
	 */
	List<QgmxDto> getPagedList(QgmxDto qgmxDto);
	
	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForSearchExp(QgmxDto qgmxDto, Map<String, Object> params);
	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForAuditSearchExp(QgmxDto qgmxDto, Map<String, Object> params);
	
	/**
	 * 获取请购物料明细信息
	 */
	List<QgmxDto> getDtoByQgidAndQgmxid(QgmxDto qgmxDto);
		
	/**
	 * 获取请购物料列表信息
	 */
	List<QgmxDto> getPagedQgmxDtos(QgmxDto qgmxDto);
	
	/**
	 * 价格统计
	 */
	List<Map<String,Object>> statisticPrice(QgmxDto qgmxDto);
	
	/**
	 * 请购物料周期统计
	 */
	List<Map<String,Object>> statisticQgmxCycle(QgmxDto qgmxDto);
	
	/**
	 * 取消请购页面明细列表
	 */
	List<QgmxDto> getQxqgmxList(QgmxDto qgmxDto);

	/**
	 * 根据请购ID获取请购明细ID
	 */
	List<QgmxDto> getListByQgid(String qgid);

	/**
	 * 根据请购明细ID删除明细信息
	 */
	boolean deleteQgmxDtos(QgmxDto qgmxDto);
	/**
	 * 更新请购明细的到货数量
	 */
	void updateQgmxDhsl(QgmxDto qgmxDto);
	
	/**
	 * 批量更新请购明细到货信息
	 */
	boolean qgmxDtoModDhxx(List<QgmxDto> qgmxDtos);
	
	/**
	 * 根据请购ids获取请购信息（共通页面）
	 */
	List<QgmxDto> getCommonDtoListByQgids(QgmxDto qgmxDto);

	/**
	 * 删除行政入库明细 删除入库数量
	 */
	boolean delRkslByQgmxid(QgmxDto qgmxDto);

	/**
	 * 根据请购明细id获取请购id
	 */
	List<QgmxDto>getQgidsByQgmxids(QgmxDto qgmxDto);
	/**
	 * 获取行政请购未确认列表
	 */
	List<QgmxDto>getPageListUnconfirmPurchaseAdministration(QgmxDto qgmxDto);
	/**
	 * 根据请购明细id获取未确认请购明显信息
	 */
	QgmxDto getWqrQgmxByQgmxid(QgmxDto qgmxDto);

	/**
	 * 批量更新请购明细关联ID
	 */
	boolean updateGlidDtos(List<QgmxDto> qgmxDtos);

	/**
	 * 更新确认标记
	 */
	boolean updateQrbj(QgmxDto qgmxDto);

	/**
	 * 获取行政库存数据
	 */
	List<QgmxDto>getXzkcList(QgmxDto qgmxDto);
		/**
	 * 单条物料点击查看
		 */
	List<QgmxDto>  getQgmxDtos(QgmxDto qgmxDto);
	/**
	 * 更新入库数量
	 */
	int  updateRksl(List<HwxxDto> hwxxDtos);
	/**
	 * 更新入库数量
	 */
	boolean updateRkslByIds(List<QgmxDto> qgmxDtos);
	/**
	 * 获取明细
	 */
	List<QgmxDto>getQgmxListByQgidAndQgmxid(QgmxDto qgmxDto);
	List<QgmxDto> getPagedDetails(QgmxDto qgmxDto);
	/**
	 * @description 修改产品注册号
	 */
    boolean updateCpzch(List<QgmxDto> qgmxDtos);
	/**
	 * 获取明细数据
	 */
	List<QgmxDto> getAdminInStoreDetails(QgmxDto qgmxDto);

	/**
	 * @Description: 获取改请购单库存信息
	 * @param qgmxDto
	 * @return java.util.List<com.matridx.igams.production.dao.entities.QgmxDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/7/8 16:32
	 */
	List<QgmxDto> queryByKcxx(QgmxDto qgmxDto);
}
