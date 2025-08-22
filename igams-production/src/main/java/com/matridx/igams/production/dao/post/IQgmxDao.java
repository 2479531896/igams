package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.HtmxDto;
import com.matridx.igams.production.dao.entities.QgmxDto;
import com.matridx.igams.production.dao.entities.QgmxModel;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface IQgmxDao extends BaseBasicDao<QgmxDto, QgmxModel>{

	/**
	 * 添加采购明细数据
	 */
	boolean insertQgmx(List<QgmxDto> list);
	
	/**
	 * 取消采购操作
	 */
	boolean updateQxqg(String qgmxid);
	
	/**
	 * 修改采购明细
	 */
	boolean updateForList(List<QgmxDto> list);
	
	/**
	 * 采购明细修改
	 */
	boolean updateQgmx(List<QgmxDto> list);
	
	/**
	 * 根据物料子类别统称筛选条件查询相关请购明细
	 */
	List<QgmxDto> getMxfjByWlzlbtc(QgmxDto qgmxDto);
	
	/**
	 * 根据业务ID和子业务ID获取附件信息
	 */
	List<QgmxDto> getFileByYwidAndZywid(QgmxDto qgmxDto);
	
    
    /**
     * 根据请购明细ID清空关联ID
     */
	boolean clearGlid(String qgmxid);

    /**
          * 更新取消请购信息
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
	 * 根据请购明细ID获取信息
	 */
	List<HtmxDto> getListByQgmxids(QgmxDto qgmxDto);

	/**
	 * 根据请购ID查询未填写统称物料
	 */
	List<QgmxDto> selectByQgid(String qgid);

	/**
	 * 获取请购明细信息
	 */
	List<QgmxDto> getListByQgid(String qgid);

	/**
	 * 批量删除请购明细信息
	 */
	int deleteQgmxDtos(QgmxDto qgmxDto);
	
	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForSearchExp(QgmxDto qgmxDto);
	
	/**
	 * 从数据库分页获取导出数据
	 */
	List<QgmxDto> getListForSearchExp(QgmxDto qgmxDto);
	
	/**
	 * 从数据库分页获取导出数据
	 */
	List<QgmxDto> getListForSelectExp(QgmxDto qgmxDto);
	
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
	 * 请购取消页面获取明细数据
	 */
	List<QgmxDto> getQxqgmxList(QgmxDto qgmxDto);
	
	/**
	 * 更新请购明细的到货数量
	 */
	void updateQgmxDhsl(QgmxDto qgmxDto);
	
	/**
	 * 更新请购明细的到货信息
	 */
	int qgmxDtoModDhxx(List<QgmxDto> qgmxDtos);
	
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

	int getCountForAuditSearchExp(QgmxDto qgmxDto);

	List<QgmxDto> getListForAuditSearchExp(QgmxDto qgmxDto);

	List<QgmxDto> getListForAuditSelectExp(QgmxDto qgmxDto);
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
	 * @Description: 根据请购id查询
	 * @param qgmxDto
	 * @return java.util.List<com.matridx.igams.production.dao.entities.QgmxDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/7/8 16:34
	 */
	List<QgmxDto> queryByKcxx(QgmxDto qgmxDto);
}
