package com.matridx.igams.experiment.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.experiment.dao.entities.ZdhJkcsDto;
import com.matridx.igams.experiment.dao.entities.ZdhJkcsModel;
import com.matridx.igams.experiment.dao.post.IZdhJkcsDao;
import com.matridx.igams.experiment.service.svcinterface.IZdhJkcsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author : 郭祥杰
 * @date :
 */
@Service
public class ZdhJkcsServiceImpl extends BaseBasicServiceImpl<ZdhJkcsDto, ZdhJkcsModel, IZdhJkcsDao> implements IZdhJkcsService {
    /**
     * @Description: 批量新增接口参数
     * @param jkcsDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/11 9:24
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertJkcsList(List<ZdhJkcsDto> jkcsDtoList) {
        return dao.insertJkcsList(jkcsDtoList);
    }

    /**
     * @Description: 批量新增
     * @param jkcsDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/19 14:25
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertJkcsDtos(List<ZdhJkcsDto> jkcsDtoList) {
        return dao.insertJkcsDtos(jkcsDtoList);
    }
}
