package com.matridx.igams.storehouse.dao.post;

import java.util.List;


import com.matridx.igams.storehouse.dao.entities.FhglDto;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.CkglDto;
import com.matridx.igams.storehouse.dao.entities.CkglModel;

@Mapper
public interface ICkglDao extends BaseBasicDao<CkglDto, CkglModel>{
	/**
	 * 查询已有流水号
	 * @param prefix
	 * @return
	 */
	String getDjhSerial(String prefix);
	
	/**
	 * 新增保存到货明细信息
	 * @return
	 */
	int insertCkgls(List<CkglDto> ckglDtos);
	
	/**
	 * 通过llid分组查询
	 * @return
	 */
	List<CkglDto> groupByCkid(String llid);

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
	List<CkglDto> getAuditListCkglDto(List<CkglDto> list);

	/**
	 * 获取出库单条数据
	 * @param ckglDto
	 * @return
	 */
	CkglDto getDtoByCkid(CkglDto ckglDto);

	
	/**
	 * 批量更新关联id
	 * @return
	 */
	int updateCkgls (List<CkglDto> ckglDtos);
	/**
	 * 删除
	 * @param
	 * @return
	 */
	int delOutDepot (CkglDto ckglDto);

	/**
	 * 自动生成记录编号
	 * @param
	 * @return
	 */
	String generateCkjlbh(String s);
	
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
	int deleteByLlid(CkglDto ckglDto);
	
	/**
	 * 根据领料id获取
	 * @param
	 * @return
	 */
	List<CkglDto> getDtoByLlids(CkglDto ckglDto);
	
	/**
	 * 批量更新出库信息
	 * @param
	 * @return
	 */
	int updateCkglDtos(List<CkglDto> ckglDtos);
	/**
	 * 根据fhids获取出库信息
	 */
	List<CkglDto> selectCkglDtoByFhids(FhglDto fhglDto);
}
