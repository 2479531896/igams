package com.matridx.igams.storehouse.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.*;

public interface ICkglService extends BaseBasicService<CkglDto, CkglModel>{

	/**
	 * 获取列表信息
	 */
    List<CkglDto> getPagedDtoList(CkglDto ckglDto);
	/**
	 * 自动生成出库单号
	 * @param
	 * @return
	 */
    String generateDjh(String prefix);

	/**
	 * 获取出库单条数据
	 * @param ckglDto
	 * @return
	 */
	CkglDto getDtoByCkid(CkglDto ckglDto);
	
	/**
	 * 新增保存到货明细信息
	 * @return
	 */
	boolean insertCkgls(List<CkglDto> ckglDtos);
	
	/**
	 * 通过llid分组查询
	 * @return
	 */
	List<CkglDto> groupByCkid(String llid);
	
	/**
	 * 批量更新关联id
	 * @return
	 */
	boolean updateCkgls (List<CkglDto> ckglDtos);
	/**
	 * 删除
	 * @param
	 * @return
	 */
	boolean delOutDepot (CkglDto ckglDto);

	/**
	 * 自动生成记录编号
	 * @param
	 * @return
	 */
    String generateCkjlbh(String s);

	/**
	 * 获取出库单数据
	 * @param ckglDto
	 * @return
	 */
	List<CkglDto> getOutBoundOrder(CkglDto ckglDto);

	/**
	 * 获取出库单数据
	 * @param ckglDto
	 * @return
	 */
	List<CkglDto> getDtoListByCkdh(CkglDto ckglDto);

	/**
	 * 审核列表
	 * @param
	 * @return
	 */
    List<CkglDto> getPagedAuditCkglDto(CkglDto ckglDto);
	
	/**
	 * 查询出库信息
	 * @param
	 * @return
	 */
	List<CkglDto> getDtoByCkids(CkglDto ckglDto);
	
	/**
	 * 通过领料id删除出库单
	 * @param
	 * @return
	 */
	boolean deleteByLlid(CkglDto ckglDto);
	
	/**
	 * 根据领料id获取
	 * @param
	 * @return
	 */
	List<CkglDto> getDtoByLlids(CkglDto ckglDto);
	
	/**
	 *批量更新出库信息
	 * @param
	 * @return
	 */
	boolean updateCkglDtos(List<CkglDto> ckglDtos);

	/**
	 * 出库红字新增
	 * @param
	 * @return
	 */
    boolean addOutDepotPunch(CkglDto ckglDto) throws BusinessException;

	/**
	 * 出库红字修改
	 * @param
	 * @return
	 */
    boolean modOutDepotPunch(CkglDto ckglDto) throws BusinessException;

	/**
	 * 出库单废弃
	 * @param
	 * @return
	 */
    boolean delOutDepotPunch(CkglDto ckglDto) throws BusinessException;
	/**
	 * 委外出库保存
	 * @param ckglDto
	 * @return
	 */
	boolean saveOutsourceDelivery(CkglDto ckglDto) throws BusinessException;
	/**
	 * 根据fhids获取出库信息
	 */
	List<CkglDto> selectCkglDtoByFhids(FhglDto fhglDto);
}
