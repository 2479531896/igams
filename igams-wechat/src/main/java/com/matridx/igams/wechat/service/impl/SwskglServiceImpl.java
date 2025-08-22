package com.matridx.igams.wechat.service.impl;


import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SwskglDto;
import com.matridx.igams.wechat.dao.entities.SwskglModel;
import com.matridx.igams.wechat.dao.entities.SwyszkDto;
import com.matridx.igams.wechat.dao.post.ISwskglDao;
import com.matridx.igams.wechat.dao.post.ISwyszkDao;
import com.matridx.igams.wechat.service.svcinterface.ISwskglService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class SwskglServiceImpl extends BaseBasicServiceImpl<SwskglDto, SwskglModel, ISwskglDao> implements ISwskglService {

    @Autowired
    ISwyszkDao swyszkDao;

    @Override
    public String getTotalAmount(String yszkid) {
        return dao.getTotalAmount(yszkid);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delBusinessReceipts(SwskglDto swskglDto) {
        int delete = dao.delete(swskglDto);
        if(delete==0){
            return false;
        }
        for(String yszkid:swskglDto.getYszkids()){
            SwyszkDto swyszkDto=new SwyszkDto();
            swyszkDto.setYszkid(yszkid);
            swyszkDto.setXgry(swskglDto.getScry());
            SwyszkDto swyszkDto_t = swyszkDao.getDto(swyszkDto);
            String totalAmount = dao.getTotalAmount(yszkid);
            BigDecimal hkzje=new BigDecimal(totalAmount);
            BigDecimal jsje=new BigDecimal("0");
            BigDecimal kdfy=new BigDecimal("0");
            BigDecimal wfkje=new BigDecimal("0");
            BigDecimal sfje=new BigDecimal("0");
            if(StringUtil.isNotBlank(swyszkDto_t.getSfje())){
                sfje=new BigDecimal(swyszkDto_t.getSfje());
            }
            if(StringUtil.isNotBlank(swyszkDto_t.getJsje())){
                jsje=new BigDecimal(swyszkDto_t.getJsje());
            }
            if(StringUtil.isNotBlank(swyszkDto_t.getKdfy())){
                kdfy=new BigDecimal(swyszkDto_t.getKdfy());
            }
            if(StringUtil.isNotBlank(swyszkDto_t.getWfkje())){
                wfkje=new BigDecimal(swyszkDto_t.getWfkje());
            }
            BigDecimal whkje = jsje.add(kdfy).subtract(hkzje).subtract(sfje);
            if(whkje.compareTo(BigDecimal.ZERO) < 0)//若计算后未回款金额小于0，则当作0来进行保存
                whkje =new BigDecimal("0");
            swyszkDto.setWhkje(String.valueOf(whkje));
            swyszkDto.setHkje(String.valueOf(hkzje));
            if(wfkje.compareTo(BigDecimal.ZERO)==0&&whkje.compareTo(BigDecimal.ZERO)==0){
                swyszkDto.setSfjq("1");
            }else{
                swyszkDto.setSfjq("0");
            }
            int num = swyszkDao.update(swyszkDto);
            if(num==0){
                return false;
            }
        }
        return true;
    }
}
