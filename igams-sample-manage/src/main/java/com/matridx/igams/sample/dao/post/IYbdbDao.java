package com.matridx.igams.sample.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.sample.dao.entities.YbdbDto;
import com.matridx.igams.sample.dao.entities.YbdbModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IYbdbDao  extends BaseBasicDao<YbdbDto, YbdbModel> {


    /**
     * 根据样本调拨id删除样本调拨管理表
     * @param ybdbDto
     * @return
     */
    int delByYbdbid(YbdbDto ybdbDto);

    int updateZt(YbdbDto ybdbDto);
    /*
        获取样本调拨审核IDS
     */
    List<YbdbDto> getPagedAuditSampleAllot(YbdbDto ybdbDto);
    /*
          获取样本调拨审核数据
       */
    List<YbdbDto> getAuditListSampleAllot(List<YbdbDto> tYbdbList);
}
