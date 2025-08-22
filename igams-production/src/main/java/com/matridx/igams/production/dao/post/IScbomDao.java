package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.BomglDto;
import com.matridx.igams.production.dao.entities.BomglModel;
import com.matridx.igams.production.dao.entities.BommxDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IScbomDao extends BaseBasicDao<BomglDto, BomglModel> {
  BomglDto selectDtoByMjwlid(String mjwlid);
}
