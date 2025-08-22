package com.matridx.las.netty.service.svcinterface;

import java.util.Map;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.las.netty.dao.entities.WkglDto;
import com.matridx.las.netty.dao.entities.WkglModel;

public interface IWkglService extends BaseBasicService<WkglDto, WkglModel> {
    /**
     * 生成文库文件
     *
     * @param wkglDto
     * @return
     */
    Map<String, Object> getParamForLibrary(WkglDto wkglDto);

    /**
     * 导入文库信息
     *
     * @param wkglDto
     * @return
     */
    Map<String, Object> modSaveLibrary(WkglDto wkglDto) throws BusinessException;

    /**
     * 获取详细信息
     *
     * @param wkglDto
     * @return
     */
    WkglDto getWkglDtoBywkid(WkglDto wkglDto);

    boolean insertWkgl(WkglDto wkglDto);
}
