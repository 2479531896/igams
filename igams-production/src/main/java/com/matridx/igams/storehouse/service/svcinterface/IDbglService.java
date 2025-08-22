package com.matridx.igams.storehouse.service.svcinterface;


import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.DbglDto;
import com.matridx.igams.storehouse.dao.entities.DbglModel;

public interface IDbglService extends BaseBasicService<DbglDto, DbglModel>{
	/**
	 * 判断调拨单号是否重复
	 * @return
	 */
    boolean isDbdhRepeat(String dbdh, String dbid);
	
	/**
	 * 调拨单保存
	 * @return
	 */
    boolean allocationsave(DbglDto dbglDto) throws BusinessException;

	/**
	 * 处理保存
	 * @return
	 */
    boolean disposalPendingSave(DbglDto dbglDto) throws BusinessException;
	
	/**
	 * 生成调拨单
	 * @return
	 */
    String generateDbdh();

	/**
	 * 通过id获取具体信息
	 */
    DbglDto getDtoByDbid(DbglDto dbglDto);
	/**
	 * 调拨列表删除
	 */
	boolean DeleteAllocations(DbglDto dbglDto) throws BusinessException;
}
