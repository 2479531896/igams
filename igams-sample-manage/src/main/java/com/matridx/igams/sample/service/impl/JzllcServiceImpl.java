package com.matridx.igams.sample.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.sample.dao.entities.JzllcDto;
import com.matridx.igams.sample.dao.entities.JzllcModel;
import com.matridx.igams.sample.dao.post.IJzllcDao;
import com.matridx.igams.sample.service.svcinterface.IJzllcService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * {@code @author:JYK}
 */
@Service
public class JzllcServiceImpl extends BaseBasicServiceImpl<JzllcDto, JzllcModel, IJzllcDao> implements IJzllcService {
    /**
     * 领料车列表
     */
    public List<JzllcDto> getLlcDtoList(JzllcDto jzllcDto){
        return dao.getLlcDtoList(jzllcDto);
    }
    /**
     * 删除
     */
    public boolean deleteByRyid(String ryid){
        return dao.deleteByRyid(ryid);
    }
}
