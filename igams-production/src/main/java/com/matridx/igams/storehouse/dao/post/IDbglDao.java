package com.matridx.igams.storehouse.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.DbglDto;
import com.matridx.igams.storehouse.dao.entities.DbglModel;

@Mapper
public interface IDbglDao extends BaseBasicDao<DbglDto, DbglModel>{
	/**
	 * 根据调拨单查询调拨信息
	 * @return
	 */
	List<DbglDto> queryByDbdh(DbglDto dbglDto);
	
	/**
	 * 生成调拨单号
	 * @return
	 */
	String getDbdhSerial(String prefix);
	/**
	 * 通过id获取具体信息
	 */
	DbglDto getDtoByDbid(DbglDto dbglDto);
}
