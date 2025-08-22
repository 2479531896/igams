package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.dao.entities.TyszmxDto;
import com.matridx.igams.common.dao.entities.TyszmxModel;
import com.matridx.igams.common.dao.post.ITyszmxDao;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ITyszmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TyszmxServiceImpl extends BaseBasicServiceImpl<TyszmxDto, TyszmxModel, ITyszmxDao> implements ITyszmxService {

    @Override
    public boolean insertList(List<TyszmxDto> list) {
        return dao.insertList(list);
    }

    @Override
    public boolean delAbandonedData(TyszmxDto tyszmxDto) {
        return dao.delAbandonedData(tyszmxDto);
    }

    @Override
    public List<TyszmxDto> getAllList() {
        return dao.getAllList();
    }

    @Override
    public List<TyszmxDto> getListByFnrid(TyszmxDto tyszmxDto) {
        return dao.getListByFnrid(tyszmxDto);
    }

    @Override
    public List<Map<String,String>> getCommonGjsx(TyszmxDto tyszmxDto) {
        List<Map<String,String>> map=new ArrayList<>();
        if(StringUtil.isNotBlank(tyszmxDto.getQqlj())){
            Map<String,Object> params=new HashMap<>();
            switch (tyszmxDto.getQqlj()){
                case "jg":
                    map=dao.getAllJgList();
                    break;
                case "wbcx":
                    map=dao.getAllWbcxList();
                    break;
                case "crmbm":
                    params.put("sxbj","BM");
                    map=dao.getCrmFilterData(params);
                    break;
                case "crmywy":
                    params.put("sxbj","YWY");
                    params.put("ids", tyszmxDto.getIds());
                    map=dao.getCrmFilterData(params);
                    break;
                case "lc":
                    map=dao.getAllYblc();
                    break;
                case "zlc":
                    map=dao.getAllYbzlc();
                    break;
            }
        }
        return map;
    }
}
