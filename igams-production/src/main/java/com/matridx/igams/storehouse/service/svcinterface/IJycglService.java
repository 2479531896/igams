package com.matridx.igams.storehouse.service.svcinterface;

import java.util.List;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.JycglDto;
import com.matridx.igams.storehouse.dao.entities.JycglModel;

public interface IJycglService extends BaseBasicService<JycglDto, JycglModel> {
	/**
	 * 根据用户id查询检验车货物id
	 * 
	 * @param userId 用户id
	 * @param cskz1
	 * @return
	 */
    JycglDto getJychwidList(String userId, String cskz1);

	/**
	 * 获取检验车里的货物信息
	 * 
	 * @param jycgl
	 * @return
	 */
    List<JycglDto> getGoods(JycglDto jycgl);

	/**
	 * 根据id删除检验车的的货物数据
	 *
	 * @param ryid
	 * @param hwids 货物ids
	 */
    void deleteHw(String ryid, List<String> hwids);
}
