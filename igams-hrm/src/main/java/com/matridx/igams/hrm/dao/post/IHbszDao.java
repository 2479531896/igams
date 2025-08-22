package com.matridx.igams.hrm.dao.post;
import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.HbszDto;
import com.matridx.igams.hrm.dao.entities.HbszModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author:JYK
 */
@Mapper
public interface IHbszDao extends BaseBasicDao<HbszDto, HbszModel> {
    /**
     * @description 获取红包设置信息对应培训信息
     * @param hbszDto
     * @return list
     */
    List<HbszDto> getDtoListWithPx(HbszDto hbszDto);

}
