package com.matridx.igams.sample.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.sample.dao.entities.JzkcxxDto;
import com.matridx.igams.sample.dao.entities.JzkcxxModel;
import com.matridx.igams.sample.dao.post.IJzkcxxDao;
import com.matridx.igams.sample.service.svcinterface.IJzkcxxService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * {@code @author:JYK}
 */
@Service
public class JzkcxxServiceImpl extends BaseBasicServiceImpl<JzkcxxDto, JzkcxxModel, IJzkcxxDao> implements IJzkcxxService {
    /**
     * 批量更新
     */
    public int updateList(List<JzkcxxDto> list){
        return dao.updateList(list);
    }
}
