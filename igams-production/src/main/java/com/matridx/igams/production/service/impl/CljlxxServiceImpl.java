package com.matridx.igams.production.service.impl;
import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.CljlxxDto;
import com.matridx.igams.production.dao.entities.CljlxxModel;
import com.matridx.igams.production.dao.entities.ShfkdjDto;
import com.matridx.igams.production.dao.post.ICljlxxDao;
import com.matridx.igams.production.service.svcinterface.ICljlxxService;
import com.matridx.igams.production.service.svcinterface.IShfkdjService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 *  2022-08-01 18:47
 **/
@Service
public class CljlxxServiceImpl extends BaseBasicServiceImpl<CljlxxDto, CljlxxModel, ICljlxxDao> implements ICljlxxService {
    @Autowired
    IShfkdjService shfkdjService;
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean saveComment(ShfkdjDto shfkdjDto) throws BusinessException {
        List<String> llids = JSON.parseArray(shfkdjDto.getLlid(), String.class);
        CljlxxDto cljlxxDto = new CljlxxDto();
        cljlxxDto.setLlid(StringUtil.join(llids,","));
        cljlxxDto.setSfjs(shfkdjDto.getSfjs());
        cljlxxDto.setLrry(shfkdjDto.getLrry());
        cljlxxDto.setShfkid(shfkdjDto.getShfkid());
        cljlxxDto.setCljlid(StringUtil.generateUUID());
        cljlxxDto.setBz(shfkdjDto.getBz());
        int insert = dao.insert(cljlxxDto);
        if (insert<1){
            throw new BusinessException("msg","保存售后评论失败！");
        }
        if ("1".equals(shfkdjDto.getSfjs())){
            //修改进度为 2
            shfkdjDto.setJd("2");
            boolean updateJd = shfkdjService.updateJd(shfkdjDto);
            if (!updateJd){
                throw new BusinessException("msg","修改进度失败！");
            }
        }
        return true;
    }

    @Override
    public List<CljlxxDto> getRecords(CljlxxDto cljlxxDto) {
        return dao.getDtoList(cljlxxDto);
    }
}
