package com.matridx.igams.bioinformation.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.bioinformation.dao.entities.DlfxjgDto;
import com.matridx.igams.bioinformation.dao.entities.DlfxjgModel;
import com.matridx.igams.bioinformation.dao.post.IDlfxjgDao;
import com.matridx.igams.bioinformation.service.svcinterface.IDlfxjgService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DlfxjgServiceImpl extends BaseBasicServiceImpl<DlfxjgDto, DlfxjgModel, IDlfxjgDao> implements IDlfxjgService{

    /**
     * 批量插入毒力分析结果数据
     */
    @Override
    public boolean insertList(List<DlfxjgDto> dlfxjglist) {
        return dao.insertList(dlfxjglist);
    }

    @Override
    public List<DlfxjgDto> getDtoList(DlfxjgDto dlfxjgDto){
        List<DlfxjgDto> dtoList = dao.getDtoList(dlfxjgDto);
        if (null != dtoList && dtoList.size() > 0){
            for (DlfxjgDto dto : dtoList) {
                if (StringUtil.isNotBlank(dto.getNr())){
                    JSONObject jsonObject = JSONObject.parseObject(dto.getNr());
                    dto.setVirulencefactor(jsonObject.get("dlyz").toString());
                    dto.setCategory(jsonObject.get("dlxglb").toString());
                    dto.setGene(jsonObject.get("dljy").toString());
                    dto.setReads(jsonObject.get("dbreads").toString());
                    dto.setSpecies(jsonObject.get("vfdb").toString());
                    String dlyz = jsonObject.getString("dlyz");
                    if(StringUtil.isNotBlank(dlyz)){
                        String vfid="";
                        String[] split = dlyz.split("\\(");
                        if(split.length>1){
                            String[] split_t = split[1].split("\\)");
                            vfid=split_t[0];
                        }
                        if(StringUtil.isNotBlank(vfid)){
                            dto.setVfid(vfid);
                        }
                    }
                }
            }
        }
        return dtoList;
    }

    @Override
    public List<DlfxjgDto> getDtoListByWkcxId(DlfxjgDto dlfxjgDto) {
         return dao.getDtoListByWkcxId(dlfxjgDto);
    }

    /**
     * 毒力结果导出
     */
    public List<DlfxjgDto> getListForExp(Map<String, Object> params) {
        DlfxjgDto dlfxjgDto = (DlfxjgDto) params.get("entryData");
        dlfxjgDto.setWkcxid(dlfxjgDto.getIds().get(0));
        dlfxjgDto.setBbh(dlfxjgDto.getIds().get(1));
        List<DlfxjgDto> list = dao.getListForExp(dlfxjgDto);
        List<DlfxjgDto> newlist =new ArrayList<>();
        if(list!=null&&list.size()>0){
            for(DlfxjgDto dto:list){
                if(StringUtil.isNotBlank(dto.getNr())){
                    DlfxjgDto dlfxjgDto_t= JSON.parseObject(dto.getNr(),DlfxjgDto.class);
                    newlist.add(dlfxjgDto_t);
                }
            }
        }
        return newlist;
    }
}
