package com.matridx.igams.storehouse.service.impl;

import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.storehouse.dao.entities.LykcxxDto;
import com.matridx.igams.storehouse.dao.entities.QyxxDto;
import com.matridx.igams.storehouse.dao.entities.QyxxModel;
import com.matridx.igams.storehouse.dao.post.IQyxxDao;
import com.matridx.igams.storehouse.service.svcinterface.ILykcxxService;
import com.matridx.igams.storehouse.service.svcinterface.IQyxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:JYK
 */
@Service
public class QyxxServiceImpl extends BaseBasicServiceImpl<QyxxDto, QyxxModel, IQyxxDao> implements IQyxxService {
    @Autowired
    ILykcxxService lykcxxService;
    @Autowired
    private IFjcfbService fjcfbService;
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean saveSampling(QyxxDto qyxxDto) throws BusinessException {
        qyxxDto.setQyid(StringUtil.generateUUID());
        int insert = dao.insert(qyxxDto);
        if (insert<1){
            throw new BusinessException("msg", "保存失败!");
        }
        LykcxxDto lykcxxDto = new LykcxxDto();
        lykcxxDto.setLykcid(qyxxDto.getLykcid());
        lykcxxDto.setKcl(qyxxDto.getQyl());
        //表示减
        lykcxxDto.setKclbj("0");
        boolean updateKcl = lykcxxService.updateKcl(lykcxxDto);
        if (!updateKcl){
            throw new BusinessException("msg", "更新库存量失败!");
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delSampling(QyxxDto qyxxDto) throws BusinessException {
        List<QyxxDto> list = dao.getQyxxListByIds(qyxxDto);
        List<LykcxxDto> lykcxxDtos = new ArrayList<>();
        for (QyxxDto dto : list) {
            LykcxxDto lykcxxDto = new LykcxxDto();
            lykcxxDto.setLykcid(dto.getLykcid());
            lykcxxDto.setKcl(String.valueOf(dto.getQyl()));
            lykcxxDto.setXgry(qyxxDto.getScry());
            lykcxxDtos.add(lykcxxDto);
        }
        int delete = dao.delete(qyxxDto);
        if (delete<1){
            throw new BusinessException("msg", "删除失败!");
        }
        boolean updateList = lykcxxService.updateKclList(lykcxxDtos);
        if (!updateList){
            throw new BusinessException("msg", "更新库存量失败!");
        }
        return true;
    }
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateSummary(QyxxDto qyxxDto) throws BusinessException{
        int result=dao.updateDto(qyxxDto);
        if (result<1){
            throw new BusinessException("msg", "保存取样小结失败!");
        }
        if(!CollectionUtils.isEmpty(qyxxDto.getFjids())) {
            for (int i = 0; i < qyxxDto.getFjids().size(); i++) {
                boolean save2RealFile = fjcfbService.save2RealFile(qyxxDto.getFjids().get(i), qyxxDto.getQyid());
                if (!save2RealFile){
                    throw new BusinessException("msg", "保存附件失败!");
                }
            }
        }
        return true;
    }

    @Override
    public List<QyxxDto> getDtolistQySampleStock(QyxxDto qyxxDto) {
        return dao.getDtolistQySampleStock(qyxxDto);
    }
}
