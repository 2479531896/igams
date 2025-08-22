package com.matridx.igams.hrm.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.QzszDto;
import com.matridx.igams.hrm.dao.entities.QzszModel;
import com.matridx.igams.hrm.dao.post.IQzszDao;
import com.matridx.igams.hrm.service.svcinterface.IQzszService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author WYX
 * @version 1.0
 * @className QzszServiceImpl
 * @description TODO
 * @date 16:50 2023/1/29
 **/
@Service
public class QzszServiceImpl extends BaseBasicServiceImpl<QzszDto, QzszModel, IQzszDao> implements IQzszService {
    @Override
    public boolean insertQzszDtos(List<QzszDto> qzszDtos) {
        return dao.insertQzszDtos(qzszDtos);
    }

    @Override
    public void deleteByJxmbds(QzszDto qzszDto) {
        dao.deleteByJxmbds(qzszDto);
    }


    @Override
    public List<QzszDto> getDtoListByMzbszid(QzszDto qzszDto) {
        return dao.getDtoListByMzbszid(qzszDto);
    }

    @Override
    public QzszDto getDtoByJxmbidAndMbszidWithNull(QzszDto qzszDto) {
        return dao.getDtoByJxmbidAndMbszidWithNull(qzszDto);
    }
}
