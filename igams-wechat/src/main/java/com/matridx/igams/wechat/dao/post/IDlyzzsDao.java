package com.matridx.igams.wechat.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.DlyzzsDto;
import com.matridx.igams.wechat.dao.entities.DlyzzsModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IDlyzzsDao extends BaseBasicDao<DlyzzsDto, DlyzzsModel>{


	/**
	 * 批量查询耐药基因注释时
	 * @return
	 */
    List<DlyzzsDto> queryByNames(DlyzzsDto dlyzzsDto);

}
