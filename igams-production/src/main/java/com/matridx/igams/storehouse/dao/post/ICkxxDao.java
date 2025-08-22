package com.matridx.igams.storehouse.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.CkxxDto;
import com.matridx.igams.storehouse.dao.entities.CkxxModel;

@Mapper
public interface ICkxxDao extends BaseBasicDao<CkxxDto, CkxxModel>{
	/**
	 * 状态查询请购数量物料详情
	 * @return
	 */
	List<CkxxDto> getItemInfoByZtQgsl(CkxxDto ckxxDto);
	/**
	 * 状态查询请购数量物料详情
	 * @return
	 */
	List<CkxxDto> getItemInfoByZtHtcgzsl(CkxxDto ckxxDto);
	/**
	 * 生产时间查询
	 * @return
	 */
	List<CkxxDto> getScLcInfoList(CkxxDto ckxxDto);
	/**
	 * 状态查询请购数量物料详情
	 * @return
	 */
	List<CkxxDto> getItemInfoByZtHtwdhsl(CkxxDto ckxxDto);
	/**
	 * 状态查询请购数量物料详情
	 * @return
	 */
	List<CkxxDto> getItemInfoByWlCkKcl(CkxxDto ckxxDto);
	/**
	 * 状态查询请购数量物料详情
	 * @return
	 */
	List<CkxxDto> getItemInfoByWlCkhwKcl(CkxxDto ckxxDto);



	/**
	 * 检查是不是父仓库
	 * @param fckid
	 * @return
	 */
	CkxxDto checkFck(String fckid);


	CkxxDto getDtoByCkdm(String ckdm);
	/**
	 * 删除父仓库的同时删除子仓库
	 * @param ckxxDto
	 */
	void deletezck(CkxxDto ckxxDto);
	
	/**
	 * 查询库位信息
	 * @return
	 */
	List<CkxxDto> getKwDtoList(CkxxDto ckxxDto);

	/**
	 * 流程查询领料出库物料详情
	 * @return
	 */
	List<CkxxDto> getItemInfoByLcLlsl(CkxxDto ckxxDto);

	/**
	 * 客户发货数量物料详情
	 * @return
	 */
	List<CkxxDto> getItemInfoByFhsl(CkxxDto ckxxDto);

	/**
	 * 客户借用数量物料详情
	 * @return
	 */
	List<CkxxDto> getItemInfoByJysl(CkxxDto ckxxDto);

	/**
	 * 流程查询成品入库其它入库详情
	 * @return
	 */
	List<CkxxDto> getItemInfoByLcCpsl(CkxxDto ckxxDto);


	/**
	 * 根据部门查出出入库数据
	 * @return
	 */
	List<CkxxDto> getCRkslByBm(CkxxDto ckxxDto);

	/**
	 * 根据流程查出数据
	 * @return
	 */
	List<CkxxDto> getdepotStatusLcInfo(CkxxDto ckxxDto);


	/**
	 * 根据状态查出数据
	 * @return
	 */
	List<CkxxDto> getdepotStatusZtInfo(CkxxDto ckxxDto);

	/**
	 * 查询子仓库
	 * @return
	 */
	List<CkxxDto> getKwDtoListByFckid(CkxxDto ckxxDto);

	/**
	 * 根据部门查出出种类分类入库数据
	 * @return
	 */
	List<CkxxDto> getDtoZlRksl(CkxxDto ckxxDto);

	/**
	 * 根据部门查出出种类分类出库数据
	 * @return
	 */
	List<CkxxDto> getDtoZlCksl(CkxxDto ckxxDto);

	/**
	 * 查询物料详情
	 * @return
	 */
	List<CkxxDto> getItemInfo(CkxxDto ckxxDto);

	/**
	 * 根据流程查出出种类分类请购
	 * @return
	 */
	List<CkxxDto> getDtoZlLcQgsl(CkxxDto ckxxDto);


	/**
	 * 根据状态查出出种类分类请购中
	 * @return
	 */
	List<CkxxDto> getDtoZlZtQgzsl(CkxxDto ckxxDto);



	/**
	 * 根据状态查出合同采购中数量
	 * @return
	 */
	List<CkxxDto> getDtoZlZtHtcgzsl(CkxxDto ckxxDto);


	/**
	 * 根据状态查出合同到货中数量
	 * @return
	 */
	List<CkxxDto> getDtoZlZtHtwdhsl(CkxxDto ckxxDto);

	/**
	 * 根据状态查出质检中数量
	 * @return
	 */
	List<CkxxDto> getDtoZlZtzjzsl(CkxxDto ckxxDto);


	/**
	 * 根据状态查出待处理数量
	 * @return
	 */
	List<CkxxDto> getDtoZlZtzjdclsl(CkxxDto ckxxDto);

	/**
	 * 根据流程查出出种类分类到货
	 * @return
	 */
	List<CkxxDto> getDtoZlLcDhsl(CkxxDto ckxxDto);
	/**
	 * 仓库库存量物料
	 * @return
	 */
	List<CkxxDto> getDtoWlCkKcl(CkxxDto ckxxDto);

	/**
	 * 仓库货物库存量物料
	 * @return
	 */
	List<CkxxDto> getDtoWlCkhwKcl(CkxxDto ckxxDto);
	/**
	 * 客户仓库库存
	 * @return
	 */
	List<CkxxDto> getCkkcl(CkxxDto ckxxDto);
	/**
	 * 客户发货物料
	 * @return
	 */
	List<CkxxDto> getDtoZlKhfh(CkxxDto ckxxDto);

	/**
	 * 客户借用数据
	 * @return
	 */
	List<CkxxDto> getKhJysl(CkxxDto ckxxDto);

	/**
	 * 客户借用物料
	 * @return
	 */
	List<CkxxDto> getDtoZlKhjy(CkxxDto ckxxDto);
	/**
	 * 根据流程查出出种类分类质检合格数量
	 * @return
	 */
	List<CkxxDto> getDtoZlLcZjhgsl(CkxxDto ckxxDto);

	/**
	 * 根据流程查出出种类分类质检不合格数量
	 * @return
	 */
	List<CkxxDto> getDtoZlLcZjbhgsl(CkxxDto ckxxDto);

	/**
	 * 根据流程查出出种类分类领料
	 * @return
	 */
	List<CkxxDto> getDtoZlLcLlck(CkxxDto ckxxDto);

	/**
	 * 根据流程查出出种类分类借用数量
	 * @return
	 */
	List<CkxxDto> getDtoZlLcJysl(CkxxDto ckxxDto);

	/**
	 * 根据流程查出出种类分类归还数量
	 * @return
	 */
	List<CkxxDto> getDtoZlLcGhsl(CkxxDto ckxxDto);

	/**
	 * 客户发货数据
	 * @return
	 */
	List<CkxxDto> getKhfhsl(CkxxDto ckxxDto);

	/**
	 * 根据流程查出出种类分类收发数量
	 * @return
	 */
	List<CkxxDto> getDtoZlLcSfsl(CkxxDto ckxxDto);

	/**
	 * 流程查询请购数量物料详情
	 * @return
	 */
	List<CkxxDto> getItemInfoByLcQgsl(CkxxDto ckxxDto);
	/**
	 * 流程查询采购入库详情
	 * @return
	 */
	List<CkxxDto> getItemInfoByLcCgrk(CkxxDto ckxxDto);
	/**
	 * 流程查询质检数量详情
	 * @return
	 */
	List<CkxxDto> getItemInfoByLcZjsl(CkxxDto ckxxDto);
}
