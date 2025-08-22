package com.matridx.igams.web.service.impl;

import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.web.dao.entities.YhzDto;
import com.matridx.igams.web.dao.entities.YhzModel;
import com.matridx.igams.web.dao.post.IYhzDao;
import com.matridx.igams.web.service.svcinterface.IYhzService;

import java.util.List;

@Service
public class YhzServiceImpl extends BaseBasicServiceImpl<YhzDto, YhzModel, IYhzDao> implements IYhzService{

    /**
     * 获取用户组信息
     * @param yhzDto
     * @return
     */
    @Override
    public List<YhzDto> getYhzxx(YhzDto yhzDto) {
        return dao.getYhzxx(yhzDto);
    }

    /**
     * 查找当前用户私有的用户组
     * @param yhzDto
     * @return
     */
    @Override
    public List<String> getPrivateYhzList(YhzDto yhzDto) {
        return dao.getPrivateYhzList(yhzDto);
    }
}
