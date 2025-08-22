package com.matridx.igams.web.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.web.dao.entities.RslyDto;
import com.matridx.igams.web.dao.entities.RslyModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IRslyDao extends BaseBasicDao<RslyDto, RslyModel> {
    /**
     * 获取审批id
     * @param rslyDto
     * @return
     */
    RslyDto getSpid(RslyDto rslyDto);

    /**
     * 点击已录用人数展示页面
     * @param rslyDto
     * @return
     */
    List<RslyDto> viewEmployDetails(RslyDto rslyDto);

}
