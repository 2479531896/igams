package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.production.dao.entities.WlglDto;
import com.matridx.igams.production.dao.entities.WlglModel;
import com.matridx.igams.production.dao.entities.WlgllsbDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IWlglDao extends BaseBasicDao<WlglDto, WlglModel>{

	/**
	 * 更新标本申请的审核状态
	 */
	void updateZt(WlglModel wlglModel);
	
	/**
	 * 获取标本申请审核的ID列表
	 */
	List<WlglDto> getPagedAuditIdList(WlglDto wlglDto);
	
	/**
	 * 根据标本申请审核的ID列表获取审核列表详细信息
	 */
	List<WlglDto> getAuditListByIds(List<WlglDto> wlglDtos);
	
	/**
	 * 根据搜索条件获取导出条数
	 */
	int getCountForSearchExp(WlglDto wlglDto);
	
	/**
	 * 从数据库分页获取导出物料数据
	 */
	List<WlglDto> getListForSearchExp(WlglDto wlglDto);
	
	/**
	 * 从数据库分页获取导出物料数据
	 */
	List<WlglDto> getListForSelectExp(WlglDto wlglDto);

	/**
	 * 通过wlid查询物料表，返回一个wllsbDto
	 */
	WlgllsbDto selectWllsbDtoByWlid(String wlid);

	/**
	 * 模糊查询物料信息
	 */
	List<WlglDto> selectSameMater(WlglDto wlglDto);

	/**
	 * 根据查询条件查询物料信息
	 */
	List<WlglDto> selectWl(WlglDto wlglDto);
	
	/**
	 * 根据物料编码获取多个物料信息
	 */
	List<WlglDto> getDtosByIds(WlglDto wlglDto);
	
	
	/**
	 * 新增或修改物料信息
	 */
	void insertOrUpdateWlgl(WlglDto wlglDto);
	
	/**
	 * 返回必填项为空的物料信息
	 */
	List<WlglDto> getNullRequiredDtos(List<ShgcDto> shgcDtos);
	
	/**
	 * 根据物料id获取物料基本信息
	 */
	List<WlglDto> getWlglDtoListByDtos(List<String> list);
	
	/**
	 * 库存维护修改数据
	 */
	int updateKcwh(WlglDto wlglDto);
	
	/**
	 * 获取前15条物料信息
	 */
	List<WlglDto> getWlgl(WlglDto wlglDto);

	/**
	 * 通过wlbm获取wlid
	 */
	WlglDto getWlidByWlbm(String wlbm);

	/**
	 * 废弃
	 */
	boolean discard(WlglDto wlglDto);
	/**
	 * 单条物料点击查看接口
	 */
	WlglDto queryByWlid(WlglDto wlglDto);
	/**
	 * 根据物料ids获取物料信息
	 */
    List<WlglDto> queryByWllxByWlids(WlglDto wlglDto);
	/**
	 * 根据物料id获取物料信息
	 */
    List<WlglDto> getDtoListByWlid(WlglDto wlglDto);
	/**
	 * 获取选中的数据
	 */
	List<WlglDto> getOrderedDtoList(WlglDto wlglDto);
	/**
	 * 获取最大的货期
	 */
	String selectBiggestHq(String wlid);

	/**
	 * @Description: 根据yhid查询个人设置
	 * @param wlglDto
	 * @return java.util.List<com.matridx.igams.production.dao.entities.WlglDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/7/23 16:11
	 */
	List<WlglDto> queryByGrsz(WlglDto wlglDto);

	/**
	 * @Description: 获取物料信息
	 * @param wlglDto
	 * @return java.util.List<com.matridx.igams.production.dao.entities.WlglDto>
	 * @Author: 郭祥杰
	 * @Date: 2024/7/24 16:04
	 */
	List<WlglDto> queryByIds(WlglDto wlglDto);
}
