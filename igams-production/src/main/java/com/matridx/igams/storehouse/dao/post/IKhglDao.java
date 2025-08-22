package com.matridx.igams.storehouse.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.storehouse.dao.entities.KhglDto;
import com.matridx.igams.storehouse.dao.entities.KhglModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IKhglDao extends BaseBasicDao<KhglDto, KhglModel> {
	/**
	 * 通过khdm查找
	 */
	KhglDto getKhglInfoByKhdm(KhglDto khglDto);
}
