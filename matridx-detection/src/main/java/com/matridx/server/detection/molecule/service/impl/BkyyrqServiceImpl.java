package com.matridx.server.detection.molecule.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.server.detection.molecule.dao.entities.BkyyrqDto;
import com.matridx.server.detection.molecule.dao.entities.BkyyrqModel;
import com.matridx.server.detection.molecule.dao.post.IBkyyrqDao;
import com.matridx.server.detection.molecule.service.svcinterface.IBkyyrqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BkyyrqServiceImpl extends BaseBasicServiceImpl<BkyyrqDto, BkyyrqModel, IBkyyrqDao> implements IBkyyrqService{

    private Logger log = LoggerFactory.getLogger(BkyyrqServiceImpl.class);

    /**
     * 新增不可预约日期信息
     * @param bkyyrqDtos
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Map<String,Object> insertBkyyrqDtoList(List<BkyyrqDto> bkyyrqDtos){
        Map<String,Object> map=new HashMap<>();
        boolean isSuccess = dao.insertBkyyrqDtoList(bkyyrqDtos);

        map.put("message", isSuccess?"保存成功！":"保存失败！");
        return map;
    }
    /**
     * 修改不可预约日期信息
     * @param bkyyrqDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public Map<String,Object> updateBkyyrqDto(BkyyrqDto bkyyrqDto){
        Map<String,Object> map=new HashMap<>();
        boolean isSuccess = false;
        isSuccess = dao.updateBkyyrqDto(bkyyrqDto);
        map.put("message", isSuccess?"修改成功！":"修改失败！");
        return map;
    }
    /**
     * 根据id查询不可预约日期信息
     * @param bkyyrqDto
     * @return
     */
    public BkyyrqDto getBkyyrqDto(BkyyrqDto bkyyrqDto){
        return dao.getBkyyrqDto(bkyyrqDto);
    }

    /**
     * 删除不可预约日期信息
     * @param bkyyrqDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delUnAppDateDetails(BkyyrqDto bkyyrqDto){
        boolean isSuccess = false;
        isSuccess = dao.delUnAppDateDetails(bkyyrqDto);
        return isSuccess;
    }
    /**
     * 根据日期范围查询不可预约日期信息
     * @param bkyyrqDto
     * @return
     */
    public List<BkyyrqDto> getUnAppDate(BkyyrqDto bkyyrqDto){
        return dao.getUnAppDate(bkyyrqDto);
    }
}
