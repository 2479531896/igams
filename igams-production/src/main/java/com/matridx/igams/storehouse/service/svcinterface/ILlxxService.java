package com.matridx.igams.storehouse.service.svcinterface;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.storehouse.dao.entities.LlxxDto;
import com.matridx.igams.storehouse.dao.entities.LlxxModel;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
public interface ILlxxService  extends BaseBasicService<LlxxDto, LlxxModel> {
    /**
     * @Description: 保存领料信息
     * @param llxxDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2025/8/19 16:13
     */
    boolean saveLlxxDto(LlxxDto llxxDto) throws BusinessException;

    /**
     * @Description: 领料列表
     * @param llxxDto
     * @return java.util.List<com.matridx.igams.storehouse.dao.entities.LlxxDto>
     * @Author: 郭祥杰
     * @Date: 2025/8/19 16:14
     */
    List<LlxxDto> getPagedDtoByJsid(LlxxDto llxxDto);

    /**
     * @Description: 删除领料信息
     * @param llxxDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2025/8/19 16:44
     */
    boolean deleteLlxxDto(LlxxDto llxxDto) throws BusinessException;
}
