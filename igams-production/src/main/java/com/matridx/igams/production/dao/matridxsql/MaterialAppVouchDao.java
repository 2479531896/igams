package com.matridx.igams.production.dao.matridxsql;

import java.util.List;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.MaterialAppVouchDto;
import com.matridx.igams.production.dao.entities.MaterialAppVouchModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MaterialAppVouchDao extends BaseBasicDao<MaterialAppVouchDto,MaterialAppVouchModel>{
	/**
	 * 获取单号
	 */
	List<MaterialAppVouchDto> getcCode(MaterialAppVouchDto materialAppVouchDto);
	
	/**
	 * 判断单号是否存在
	 */
	MaterialAppVouchDto queryByCode(MaterialAppVouchDto materialAppVouchDto);
}
