package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SwhtmxDto;
import com.matridx.igams.wechat.dao.entities.SwhtmxModel;
import com.matridx.igams.wechat.dao.post.ISwhtmxDao;
import com.matridx.igams.wechat.service.svcinterface.ISwhtmxService;
import org.springframework.stereotype.Service;
import java.util.List;


/**
 * 商务合同明细(IgamsSwhtmx)表服务实现类
 *
 * @author makejava
 * @since 2023-04-04 13:52:29
 */
@Service
public class SwhtmxServiceImpl extends BaseBasicServiceImpl<SwhtmxDto, SwhtmxModel, ISwhtmxDao> implements ISwhtmxService {

    @Override
    public Boolean insertList(List<SwhtmxDto> list) {
        return dao.insertList(list) != 0;
    }

    @Override
    public List<SwhtmxDto> getAllList(SwhtmxDto swhtmxDto) {
        return dao.getAllList(swhtmxDto);
    }

    @Override
    public List<SwhtmxDto> getHbsfInfo(SwhtmxDto swhtmxDto) {
        return dao.getHbsfInfo(swhtmxDto);
    }

    @Override
    public Boolean updateList(List<SwhtmxDto> list) {
        return dao.updateList(list);
    }

    @Override
    public Boolean modProjectInfo(SwhtmxDto swhtmxDto) {
        return dao.modProjectInfo(swhtmxDto)!=0;
    }
}
