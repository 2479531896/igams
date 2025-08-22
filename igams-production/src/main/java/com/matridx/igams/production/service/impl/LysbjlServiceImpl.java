package com.matridx.igams.production.service.impl;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.*;
import com.matridx.igams.production.dao.post.ILysbjlDao;
import com.matridx.igams.production.service.svcinterface.ILysbjlService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:JYK
 */
@Service
public class LysbjlServiceImpl extends BaseBasicServiceImpl<LysbjlDto, LysbjlModel, ILysbjlDao> implements ILysbjlService {
    @Override
    public boolean saveRecordRetention(LysbjlDto lysbjlDto) {
        lysbjlDto.setSbjlid(StringUtil.generateUUID());
        int insert = dao.insert(lysbjlDto);
        return insert > 0;
    }

    @Override
    public List<LysbjlDto> getDtoListGroup(LysbjlDto lysbjlDto) {
        return dao.getDtoListGroup(lysbjlDto);
    }

    @Override
    public Map<String, Object> getStatisticsRetention(LysbjlDto lysbjlDto) {
        List<LysbjlDto> dtoList = dao.getDtoList(lysbjlDto);
        Map<String, Object> map = new HashMap<>();
        for (String id : lysbjlDto.getIds()) {
            List<LysbjlDto> list = dtoList.stream().filter(e -> e.getSbid().equals(id)).collect(Collectors.toList());
            map.put(id,list);
        }
        return map;
    }
}
