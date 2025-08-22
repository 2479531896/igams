package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;

import com.matridx.igams.production.dao.entities.DdbxglDto;
import com.matridx.igams.production.dao.entities.DdbxglModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author:JYK
 */
@Mapper
public interface IDdbxglDao extends BaseBasicDao<DdbxglDto, DdbxglModel>{
}
