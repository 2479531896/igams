package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SysxxDto;
import com.matridx.igams.wechat.dao.entities.SysxxModel;
import com.matridx.igams.wechat.dao.post.ISysxxDao;
import com.matridx.igams.wechat.service.svcinterface.ISysxxService;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SysxxServiceImpl extends BaseBasicServiceImpl<SysxxDto, SysxxModel, ISysxxDao> implements ISysxxService {
    /**
     * 根据对方实验室名称获取实验室信息
     * @param dfsysmc
     * @return
     */
    public SysxxDto getSysxxDtoByDfsysmc(String dfsysmc){
        // 获取当前日期
        Calendar currentDate = Calendar.getInstance();

        // 获取当前日期是当周的第几天
        int dayOfWeek = currentDate.get(Calendar.DAY_OF_WEEK) - 1;

        Map<String,String> map = new HashMap<>();
        map.put("dfsysmc",dfsysmc);
        map.put("zq",String.valueOf(dayOfWeek));
        return dao.getSysxxDtoByDfsysmc(map);
    }

    /**
     * 获取列表数据
     */

    @Override
    public List<SysxxDto> getPageSysxxDtoList(SysxxDto sysxxDto) {
        return dao.getPageSysxxDtoList(sysxxDto);
    }
}
