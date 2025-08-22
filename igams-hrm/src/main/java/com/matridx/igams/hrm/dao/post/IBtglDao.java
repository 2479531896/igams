package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.BtglDto;
import com.matridx.igams.hrm.dao.entities.BtglModel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IBtglDao extends BaseBasicDao<BtglDto, BtglModel>{

}
