package com.matridx.igams.hrm.dao.post;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.hrm.dao.entities.PxtkglbDto;
import com.matridx.igams.hrm.dao.entities.PxtkglbModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IPxtkglbDao extends BaseBasicDao<PxtkglbDto, PxtkglbModel> {

    /**
     * 通过pxid删除题库配置
     */
    void deleteByPxid(PxtkglbDto pxtkglbDto);

    /**
     * 通过pxid获取全部信息
     */
    List<PxtkglbDto> getListByPxid(PxtkglbDto pxtkglbDto);

}
