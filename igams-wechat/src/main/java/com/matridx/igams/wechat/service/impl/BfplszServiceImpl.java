package com.matridx.igams.wechat.service.impl;


import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.BfglDto;
import com.matridx.igams.wechat.dao.entities.BfplszDto;
import com.matridx.igams.wechat.dao.entities.BfplszModel;
import com.matridx.igams.wechat.dao.post.IBfglDao;
import com.matridx.igams.wechat.dao.post.IBfplszDao;
import com.matridx.igams.wechat.service.svcinterface.IBfplszService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Service
public class BfplszServiceImpl extends BaseBasicServiceImpl<BfplszDto, BfplszModel, IBfplszDao> implements IBfplszService {

    @Autowired
    private IBfglDao bfglDao;

    @Override
    public List<BfplszDto> getPagedToBeVisitedList(BfplszDto bfplszDto) {
        return dao.getPagedToBeVisitedList(bfplszDto);
    }

    @Override
    public boolean SaveFrequencySetting(BfplszDto bfplszDto) {
        if(StringUtil.isNotBlank(bfplszDto.getPlszid())){
            int updated = dao.update(bfplszDto);
            if(updated==0){
                return false;
            }
        }else{
            bfplszDto.setPlszid(StringUtil.generateUUID());
            int inserted = dao.insert(bfplszDto);
            if(inserted==0){
                return false;
            }
        }
        try {
            determineAndModVisitMarker(bfplszDto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    /**
     * 判断并更改拜访标记（公共方法）
     */
    public boolean determineAndModVisitMarker(BfplszDto bfplszDto) throws Exception {
        BfplszDto dto = dao.getDto(bfplszDto);
        if(dto!=null){
            BfglDto bfglDto=new BfglDto();
            bfglDto.setDwid(bfplszDto.getDwid());
            bfglDto.setLxrid(bfplszDto.getLxrid());
            bfglDto.setBfr(bfplszDto.getYhid());
            bfglDto.setBfsjstart(dto.getQsrq());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            BigDecimal bfpc=new BigDecimal(dto.getBfpc());
            if("DAY".equals(dto.getPclxdm())){
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(DateUtils.parse(dto.getQsrq()));
                calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(dto.getPczq()));
                bfglDto.setBfsjend( sdf.format(calendar.getTime()));
            }else if("WEEK".equals(dto.getPclxdm())){
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(DateUtils.parse(dto.getQsrq()));
                calendar.add(Calendar.WEEK_OF_MONTH,Integer.parseInt(dto.getPczq()));
                bfglDto.setBfsjend( sdf.format(calendar.getTime()));
            }else if("MONTH".equals(dto.getPclxdm())){
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(DateUtils.parse(dto.getQsrq()));
                calendar.add(Calendar.MONTH,Integer.parseInt(dto.getPczq()));
                bfglDto.setBfsjend( sdf.format(calendar.getTime()));
            }else if("QUARTER".equals(dto.getPclxdm())){
                Calendar calendar=Calendar.getInstance();
                calendar.setTime(DateUtils.parse(dto.getQsrq()));
                calendar.add(Calendar.MONTH,Integer.parseInt(dto.getPczq())*3);
                bfglDto.setBfsjend( sdf.format(calendar.getTime()));
            }
            List<BfglDto> dtoList = bfglDao.getDtoList(bfglDto);
            if(dtoList!=null&&!dtoList.isEmpty()){
                BigDecimal length=new BigDecimal(dtoList.size());
                if(length.compareTo(bfpc)>=0){
                    dto.setBfbj("1");
                }else{
                    dto.setBfbj("0");
                }
            }else{
                dto.setBfbj("0");
            }
            int updated = dao.update(dto);
            if(updated==0){
                return false;
            }
        }
        return true;
    }

    public void updateTimedTaskInfo(){
        dao.updateTimedTaskDates();
        dao.updateTimedTaskVisitMarkers();
    }
}
