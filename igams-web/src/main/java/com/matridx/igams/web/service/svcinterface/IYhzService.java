package com.matridx.igams.web.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.web.dao.entities.YhzDto;
import com.matridx.igams.web.dao.entities.YhzModel;

import java.util.List;

public interface IYhzService extends BaseBasicService<YhzDto, YhzModel>{

    /**
     * 获取用户组信息
     * @param yhzDto
     * @return
     */
    List<YhzDto> getYhzxx(YhzDto yhzDto);

    /**
     * 查找当前用户私有的用户组
     * @param yhzDto
     * @return
     */
    List<String> getPrivateYhzList(YhzDto yhzDto);
}
