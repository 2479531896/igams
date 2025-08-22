package com.matridx.igams.common.dao.post;

import org.apache.ibatis.annotations.Mapper;

import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.common.dao.entities.DxglDto;
import com.matridx.igams.common.dao.entities.DxglModel;

@Mapper
public interface IDxglDao extends BaseBasicDao<DxglDto, DxglModel>{

    /**
     * 根据手机号获取当天短信管理信息
     */
    DxglDto getDxglBySjhToday(DxglDto dxglDto);
    /**
     * 根据手机号获取所有短信管理信息
     */
    DxglDto getDxglBySjh(DxglDto dxglDto);

    /**
     * 更新短信管理信息
     */
    boolean updateDxgl(DxglDto dxglDto);
    /**
     * 新增短信管理信息
     */
    boolean insertDxgl(DxglDto dxglDto);
}
