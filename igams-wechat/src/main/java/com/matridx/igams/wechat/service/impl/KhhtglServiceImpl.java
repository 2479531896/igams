package com.matridx.igams.wechat.service.impl;


import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.KhhtglDto;
import com.matridx.igams.wechat.dao.entities.KhhtglModel;
import com.matridx.igams.wechat.dao.post.IKhhtglDao;
import com.matridx.igams.wechat.service.svcinterface.IKhhtglService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户合同管理(IgamsKhhtgl)表服务实现类
 *
 * @author makejava
 * @since 2023-04-04 13:50:59
 */
@Service
public class KhhtglServiceImpl extends BaseBasicServiceImpl<KhhtglDto, KhhtglModel, IKhhtglDao> implements IKhhtglService {

    @Override
    public Boolean insertList(List<KhhtglDto> list) {
        return dao.insertList(list);
    }
}
