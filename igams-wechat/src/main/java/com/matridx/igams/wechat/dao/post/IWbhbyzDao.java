package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.WbhbyzDto;
import com.matridx.igams.wechat.dao.entities.WbhbyzModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IWbhbyzDao extends BaseBasicDao<WbhbyzDto, WbhbyzModel>{

	/**
	 * 根据代码查询合作伙伴
	 * @param code
	 * @return
	 */
	List<WbhbyzDto> getSjhbByCode(String code);
	/**
	 * 查询合作伙伴
	 * @return
	 */
	List<WbhbyzDto> getSjhbAll();

	/**
	 * 根据代码查询合作伙伴
	 * @param
	 * @return
	 */
	WbhbyzDto getInfoByHbid(String hbid);

	/**
	 * 查询伙伴信息
	 */
	List<WbhbyzDto> getListByCode(WbhbyzDto wbhbyzDto);

	/**
	 *插入伙伴
	 * @param
	 * @return
	 */
    int insertPartner(List<WbhbyzDto> wbhbyzDtos);

}
