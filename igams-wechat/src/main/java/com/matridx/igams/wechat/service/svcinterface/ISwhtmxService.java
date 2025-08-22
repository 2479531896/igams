package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SwhtmxDto;
import com.matridx.igams.wechat.dao.entities.SwhtmxModel;

import java.util.List;


/**
 * 商务合同明细(IgamsSwhtmx)表服务接口
 *
 * @author makejava
 * @since 2023-04-04 13:52:29
 */
public interface ISwhtmxService extends BaseBasicService<SwhtmxDto, SwhtmxModel> {
    /**
     * 批量插入
     */
    Boolean insertList(List<SwhtmxDto> list);
    /**
     * 获取客户合同明细
     */
    List<SwhtmxDto> getAllList(SwhtmxDto swhtmxDto);

    /**
     * 获取伙伴收费明细
     */
    List<SwhtmxDto> getHbsfInfo(SwhtmxDto swhtmxDto);
    /**
     * 批量修改
     */
    Boolean updateList(List<SwhtmxDto> list);

    /**
     * 修改项目
     * @param swhtmxDto
     * @return
     */
    Boolean modProjectInfo(SwhtmxDto swhtmxDto);

}
