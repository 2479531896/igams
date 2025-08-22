package com.matridx.igams.production.dao.gmsql;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.Bas_partModel;
import com.matridx.igams.production.dao.entities.ComputationUnitModel;
import com.matridx.igams.production.dao.entities.InventoryDto;
import com.matridx.igams.production.dao.entities.InventoryModel;
@Mapper
public interface IGmInventoryDao extends BaseBasicDao<InventoryDto, InventoryModel>{
	/**
	 * 插入到sqlService数据库
	 * @param inModel

	 */
    boolean insertmatridx(InventoryModel inModel);
	
	/**
	 * 插入到sqlService数据库
	 * @param map

	 */
    boolean insertsubmatridx(Map<String, String> map);
	
	/**
	 * 修改sqlService数据库
	 * @param inModel

	 */
    boolean updatematridx(InventoryModel inModel);
	
	/**
	 * 删除
	 * @param inModel

	 */
    boolean deletematridx(InventoryModel inModel);
	
	/**
	 * 查询单位是否存在
	 * @param cComUnitName

	 */
    ComputationUnitModel selectmatridx(String cComUnitName);
	
	/**
	 * 查询物料是否存在
	 * @param cInvCode

	 */
    InventoryModel select_matridx_InvName(String cInvCode);
	
	/**
	 * 查询最大的partid

	 */
    Bas_partModel select_matridx_partid();
	
	/**
	 * 添加Bas_part表
	 * @param bModel

	 */
    boolean insert_matridx_Bas_part(Bas_partModel bModel);
	
	/*-----------------------两个数据库----------------------------*/
	
	/**
	 * 插入到sqlService数据库
	 * @param inModel

	 */
    boolean insertInventory(InventoryModel inModel);
	
	/**
	 * 插入到sqlService数据库
	 * @param map

	 */
    boolean insertsubInventory(Map<String, String> map);
	
	/**
	 * 修改sqlService数据库
	 * @param inModel

	 */
    boolean updateInventory(InventoryModel inModel);
	
	/**
	 * 修改sqlService数据库
	 * @param inModel

	 */
    boolean updateInventory2017(InventoryModel inModel);
	
	/**
	 * 修改sqlService数据库
	 * @param inModel

	 */
    boolean updateInventory2020(InventoryModel inModel);
	
	/**
	 * 删除
	 * @param inModel

	 */
    boolean deleteInventory(InventoryModel inModel);
	
	/**
	 * 查询单位是否存在
	 * @param cComUnitName

	 */
    ComputationUnitModel selectInventory(String cComUnitName);
	
	/**
	 * 查询单位是否存在
	 * @param cComUnitName

	 */
    ComputationUnitModel selectInventory2017(String cComUnitName);
	
	/**
	 * 查询单位是否存在
	 * @param cComUnitName

	 */
    ComputationUnitModel selectInventory2020(String cComUnitName);
	
	/**
	 * 查询物料是否存在
	 * @param cInvCode

	 */
    InventoryModel select_Inventory_InvName(String cInvCode);
	
	/**
	 * 查询最大的partid

	 */
    Bas_partModel select_Inventory_partid();

	/**
	 * 查询partid

	 */
    Bas_partModel select_Inventory_partid_cInvCode(Bas_partModel bas_partModel);
	
	/**
	 * 查询最大的partid

	 */
    Bas_partModel select_Inventory2017_partid();
	
	/**
	 * 查询最大的partid

	 */
    Bas_partModel select_Inventory2020_partid();
	
	/**
	 * 添加Bas_part表
	 * @param bModel

	 */
    boolean insert_Inventory_Bas_part(Bas_partModel bModel);
	
	/**
	 * 插入到sqlService数据库
	 * @param inModel

	 */
    boolean insertInventory2017(InventoryModel inModel);
	
	/**
	 * 插入到sqlService数据库
	 * @param inModel

	 */
    boolean insertInventory2020(InventoryModel inModel);
	
	/**
	 * 插入到sqlService数据库
	 * @param map

	 */
    boolean insertsubInventory2017(Map<String, String> map);
	
	/**
	 * 插入到sqlService数据库
	 * @param map

	 */
    boolean insertsubInventory2020(Map<String, String> map);
	
	/**
	 * 添加Bas_part表
	 * @param bModel

	 */
    boolean insert_Inventory2017_Bas_part(Bas_partModel bModel);
	
	/**
	 * 添加Bas_part表
	 * @param bModel

	 */
    boolean insert_Inventory2020_Bas_part(Bas_partModel bModel);
}
