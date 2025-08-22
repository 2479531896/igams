package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.CsszDto;
import com.matridx.igams.hrm.dao.entities.CsszModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author WYX
 * @version 1.0
 * @className ICsszDao
 * @description TODO
 * @date 16:30 2023/1/9
 **/
@Mapper
public interface ICsszDao  extends BaseBasicDao<CsszDto, CsszModel> {
    /**
     * 根据模板类型获取初始设置数据
     */
    CsszDto getDtoByKhlx(String khlx);

}
