package com.matridx.igams.detection.molecule.service.impl;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.detection.molecule.dao.entities.FzjcxxDto;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.detection.molecule.dao.entities.KzbmxDto;
import com.matridx.igams.detection.molecule.dao.entities.KzbmxModel;
import com.matridx.igams.detection.molecule.dao.post.IKzbmxDao;
import com.matridx.igams.detection.molecule.service.svcinterface.IKzbmxService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class KzbmxServiceImpl extends BaseBasicServiceImpl<KzbmxDto, KzbmxModel, IKzbmxDao> implements IKzbmxService{

    /**
     * 批量插入扩增板明细数据
     */
    @Override
    public boolean batchInsertDtoList(List<KzbmxDto> kzbmxlist) {
        return dao.batchInsertDtoList(kzbmxlist);
    }

    /**
     * 通过扩增板ID获取扩增明细数据
     */
    @Override
    public List<KzbmxDto> getKzbmxListByKzbid(String kzbid) {
        return dao.getKzbmxListByKzbid(kzbid);
    }

    /**
     * 删除扩增板ID对应的扩增板明细数据
     */
    @Override
    public void deleteKzbmxByKzbid(Map<String, Object> map) {
        dao.deleteKzbmxByKzbid(map);
    }

    /**
     * 通过样本编号获取扩增明细数据
     */
    public KzbmxDto getDtoByYbbh(KzbmxDto kzbmxDto){
        return dao.getDtoByYbbh(kzbmxDto);
    }

    /**
     * 插入扩增板明细数据
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertKzbmx(KzbmxDto kzbmxDto, FzjcxxDto fzjcxxDto, User user){
        kzbmxDto.setYbbh(fzjcxxDto.getYbbh());
        kzbmxDto.setSyh(fzjcxxDto.getSyh());
        kzbmxDto.setXh(fzjcxxDto.getKzbxh());
        kzbmxDto.setLrry(user.getYhid());
        return insert(kzbmxDto);
    }

    /**
     * 修改扩增板明细数据
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateKzbmx(KzbmxDto kzbmxDto, FzjcxxDto fzjcxxDto, User user){
        kzbmxDto.setYbbh(fzjcxxDto.getYbbh());
        kzbmxDto.setSyh(fzjcxxDto.getSyh());
        kzbmxDto.setXgry(user.getYhid());
        return update(kzbmxDto);
    }

}
