package com.matridx.las.frame.connect.dao.post;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.las.frame.connect.dao.entities.ZyxxLasDto;
import com.matridx.las.frame.connect.dao.entities.ZyxxLasModel;

@Mapper
public interface IZyxxLasDao extends BaseBasicDao<ZyxxLasDto, ZyxxLasModel>{

    /**
     * 获取资源信息
     * @return
     */
    List<ZyxxLasDto> getZyxxInit();
}
