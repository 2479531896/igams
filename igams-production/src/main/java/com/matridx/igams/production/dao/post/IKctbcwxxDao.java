package com.matridx.igams.production.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.production.dao.entities.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IKctbcwxxDao extends BaseBasicDao<KctbcwxxDto, KctbcwxxModel>{

    /**
     * 新增保存同步库存错误信息
     */
    Integer insertKctbcwxxList(List<KctbcwxxDto> list);
}
