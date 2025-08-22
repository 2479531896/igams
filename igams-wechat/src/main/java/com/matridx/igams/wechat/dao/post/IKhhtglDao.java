package com.matridx.igams.wechat.dao.post;


import com.matridx.igams.common.dao.BaseBasicDao;
import com.matridx.igams.wechat.dao.entities.KhhtglDto;
import com.matridx.igams.wechat.dao.entities.KhhtglModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 客户合同管理(IgamsKhhtgl)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-04 13:50:42
 */
@Mapper
public interface IKhhtglDao extends BaseBasicDao<KhhtglDto, KhhtglModel> {

    /**
     * 批量保存
     */
    Boolean insertList(List<KhhtglDto> list);
}

