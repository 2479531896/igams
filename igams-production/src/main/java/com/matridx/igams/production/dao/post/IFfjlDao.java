package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.FfjlDto;
import com.matridx.igams.production.dao.entities.FfjlModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IFfjlDao extends BaseBasicDao<FfjlDto, FfjlModel>{
    /**
     * @description 新增发放记录
     */
    boolean insertFfjlDtos(List<FfjlDto> ffjlDtos);
    /**
     * @description 修改发放记录
     */
    boolean updateFfjlDtos(List<FfjlDto> ffjlDtos);
    /**
     * @description 删除发放记录
     */
    boolean deleteByIds(FfjlDto ffjlDto);
}
