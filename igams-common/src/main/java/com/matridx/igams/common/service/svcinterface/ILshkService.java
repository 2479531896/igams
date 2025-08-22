package com.matridx.igams.common.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.common.dao.entities.LshkDto;
import com.matridx.igams.common.dao.entities.LshkModel;
import com.matridx.igams.common.enums.SerialNumTypeEnum;
import com.matridx.igams.common.exception.BusinessException;

public interface ILshkService extends BaseBasicService<LshkDto, LshkModel>{
	/**
	 * 生成流水号编号
	 * @param type	编号类型
	 * @param lshkDto 编号相关参数
	 */
	 String doMakeSerNum(SerialNumTypeEnum type, LshkDto lshkDto) throws BusinessException;
	
	/**
	 * 根据现在的编码，去除前缀查找流水号库里的编码，如果现在的编码更新，则更新成现在的编码
	 */
	 boolean updateSerNum(String lshlx,String lshqz,String nowNum);
}
