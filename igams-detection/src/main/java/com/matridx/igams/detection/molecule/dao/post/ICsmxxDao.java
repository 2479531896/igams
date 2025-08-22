package com.matridx.igams.detection.molecule.dao.post;

import com.matridx.igams.detection.molecule.dao.entities.CsmxxDto;
import com.matridx.igams.detection.molecule.dao.entities.CsmxxModel;
import org.apache.ibatis.annotations.Mapper;
import com.matridx.igams.common.dao.BaseBasicDao;

@Mapper
public interface ICsmxxDao extends BaseBasicDao<CsmxxDto, CsmxxModel>{

}
