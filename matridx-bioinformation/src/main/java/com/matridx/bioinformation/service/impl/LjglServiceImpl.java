package com.matridx.bioinformation.service.impl;


import com.matridx.bioinformation.dao.entities.LjglDto;
import com.matridx.bioinformation.dao.entities.LjglModel;
import com.matridx.bioinformation.dao.post.ILjglDao;
import com.matridx.bioinformation.service.svcinterface.ILjglService;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
public class LjglServiceImpl extends BaseBasicServiceImpl<LjglDto, LjglModel, ILjglDao> implements ILjglService {

    @Override
    public String[] fileList(String[] list) {
        List<String> lj_list = new ArrayList<>();
        List<String> wj_list = new ArrayList<>();
        List<String> file_list = new ArrayList<>();
        for (String s : list) {
            if (s.indexOf(".") == -1){
                lj_list.add(s);
                Collections.sort(lj_list);
            }else{
                wj_list.add(s);
                Collections.sort(wj_list);
            }
        }
        file_list.addAll(lj_list);
        file_list.addAll(wj_list);
        return file_list.toArray(new String[file_list.size()]);
    }
}