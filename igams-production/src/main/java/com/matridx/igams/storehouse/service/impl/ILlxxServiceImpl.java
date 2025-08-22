package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.storehouse.dao.entities.HwxxDto;
import com.matridx.igams.storehouse.dao.entities.LlxxDto;
import com.matridx.igams.storehouse.dao.entities.LlxxModel;
import com.matridx.igams.storehouse.dao.post.ILlxxDao;
import com.matridx.igams.storehouse.service.svcinterface.IHwxxService;
import com.matridx.igams.storehouse.service.svcinterface.ILlxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Service
public class ILlxxServiceImpl  extends BaseBasicServiceImpl<LlxxDto, LlxxModel, ILlxxDao> implements ILlxxService {
    @Autowired
    private IHwxxService hwxxService;

    /**
     * @Description: 保存领料信息
     * @param llxxDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2025/8/19 15:26
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean saveLlxxDto(LlxxDto llxxDto) throws BusinessException {
        llxxDto.setLlid(StringUtil.generateUUID());
        boolean result = dao.insert(llxxDto)>0;
        if(!result){
            throw new BusinessException("领料信息保存失败");
        }
        HwxxDto hwxxDto = new HwxxDto();
        hwxxDto.setHwid(llxxDto.getHwid());
        hwxxDto.setModkcl("0");
        hwxxDto.setKcl(llxxDto.getQlsl());
        hwxxDto.setXgry(llxxDto.getLrry());
        result = hwxxService.updateHwxxDtoByHwid(hwxxDto);
        if(!result){
            throw new BusinessException("货物信息更新失败");
        }
        return true;
    }

    /**
     * @Description: 领料列表
     * @param llxxDto
     * @return java.util.List<com.matridx.igams.storehouse.dao.entities.LlxxDto>
     * @Author: 郭祥杰
     * @Date: 2025/8/19 16:14
     */
    @Override
    public List<LlxxDto> getPagedDtoByJsid(LlxxDto llxxDto) {
        return dao.getPagedDtoList(llxxDto);
    }

    /**
     * @Description: 删除领料信息
     * @param llxxDto
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2025/8/19 16:44
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean deleteLlxxDto(LlxxDto llxxDto) throws BusinessException {
        boolean result = dao.delete(llxxDto)>0;
        if(!result){
            throw new BusinessException("领料信息删除失败");
        }
        HwxxDto hwxxDto = new HwxxDto();
        hwxxDto.setIds(llxxDto.getIds());
        hwxxDto.setXgry(llxxDto.getScry());
        result = hwxxService.updateKclDtoByLLid(hwxxDto);
        if(!result){
            throw new BusinessException("货物信息更新失败");
        }
        return true;
    }
}
