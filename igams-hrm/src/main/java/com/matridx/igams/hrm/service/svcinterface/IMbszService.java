package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.MbszDto;
import com.matridx.igams.hrm.dao.entities.MbszModel;

import java.util.List;

/**
 * @author:JYK
 */
public interface IMbszService extends BaseBasicService<MbszDto, MbszModel> {
    /**
     * @description 批量修改模板设置信息
     */
    boolean updateMbszDtos(List<MbszDto> mbszDtos);
    /**
     * @description 模板高级设置
     */
    boolean advancedTemplateSetting(MbszDto mbszDto) throws BusinessException;
    /**
     * 根据模板ids删除数据
     *
     */
    boolean deleteBymbid(MbszDto mbszDto);

    /**
     * @description 修改生效日期和有效期
     */
    boolean updateSxrqAndYxq(MbszDto mbszDto);
}
