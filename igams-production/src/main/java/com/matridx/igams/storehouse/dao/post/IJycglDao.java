package com.matridx.igams.storehouse.dao.post;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.JycglDto;
import com.matridx.igams.storehouse.dao.entities.JycglModel;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * @author lifan
 *检验车管理Dao
 */
@Mapper
public interface IJycglDao extends BaseBasicDao<JycglDto,JycglModel>{
	/**
	 * 根据用户id查询检验车货物id
	 * @param kzcs1
	 * @return
	 */
	List<String> getJychwidList(@Param("ryid") String ryid, @Param("kzcs1") String kzcs1);
	/**
	 * 获取检验车里的货物信息
	 * @param jycgl
	 * @return
	 */
	List<JycglDto> getGoods(JycglDto jycgl);
	/**
	 * 清空检验车
	 * @param jygl
	 * @return
	 */
	boolean cleanJyc(JycglDto jygl);
	/**
	 * 删除检验车中的货物
	 *
	 * @param jygl
	 */
	void deleteHw(JycglDto jygl);
}
