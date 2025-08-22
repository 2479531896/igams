package com.matridx.igams.bioinformation.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.bioinformation.dao.entities.ZsxxDto;
import com.matridx.igams.bioinformation.service.svcinterface.IZsxxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.bioinformation.dao.entities.NyfxjgDto;
import com.matridx.igams.bioinformation.dao.entities.NyfxjgModel;
import com.matridx.igams.bioinformation.dao.post.INyfxjgDao;
import com.matridx.igams.bioinformation.service.svcinterface.INyfxjgService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class NyfxjgServiceImpl extends BaseBasicServiceImpl<NyfxjgDto, NyfxjgModel, INyfxjgDao> implements INyfxjgService{

    @Autowired
    private IZsxxService zsxxService;
    /**
     * 批量插入耐药分析结果
     */
    @Override
    public boolean insertList(List<NyfxjgDto> nyfxjgDtoList) {
        return dao.insertList(nyfxjgDtoList);
    }
    @Override
    public List<NyfxjgDto> getDtos(NyfxjgDto nyfxjgDto){
        List<NyfxjgDto> dtoList = dao.getDtoList(nyfxjgDto);
        List<ZsxxDto> list = zsxxService.getDtoList(new ZsxxDto());
        if (null != dtoList && dtoList.size() > 0){
            for (NyfxjgDto dto : dtoList) {
                if (StringUtil.isNotBlank(dto.getNr())){
                    JSONObject jsonObject = JSONObject.parseObject(dto.getNr());
                    dto.setGenefamily(jsonObject.get("nyjzu")==null?"":jsonObject.get("nyjzu").toString());
                    dto.setGene(jsonObject.get("topjy")==null?"":jsonObject.get("topjy").toString());
                    dto.setMechanism(jsonObject.get("nyjz")==null?"":jsonObject.get("nyjz").toString());
                    dto.setTagrget(jsonObject.get("nylb")==null?"":jsonObject.get("nylb").toString());
                    dto.setCkwztaxid(jsonObject.get("ckwztaxid")==null?"":jsonObject.get("ckwztaxid").toString());
                    dto.setCkwz(jsonObject.get("ckwz")==null?"":jsonObject.get("ckwz").toString());
                    if (null != jsonObject.get("topjydjcxx")){
                        StringBuilder reads = new StringBuilder();
                        String[] strings = jsonObject.get("topjydjcxx").toString().split(";");
                        if(strings.length>1){
                            for (String string : strings) {
                                if(string.split(":").length>1){
                                    reads.append(";").append(string.split(":")[1]);
                                }else{
                                    reads.append(";").append(string.split(":")[0]);
                                }
                            }
                        }else{
                            reads = new StringBuilder(";" + strings[0]);
                        }

                        dto.setReads(reads.substring(1,reads.length()));
                    }
                    if (null != list && list.size() >0){
                        for (ZsxxDto zsxxDto : list) {
                            if (zsxxDto.getMc().equals(dto.getGenefamily())){
                                dto.setSpecies(zsxxDto.getKnwzly());
                                break;
                            }
                        }
                    }
                }
            }
        }
        return dtoList;
    }

    @Override
    public List<NyfxjgDto> getListByWkcxid(NyfxjgDto nyfxjgDto){
        List<NyfxjgDto> nyfxjgDtoList=dao.getListByWkcxid(nyfxjgDto);
        List<NyfxjgDto> nyfxjgDtos=new ArrayList<>();
        for (NyfxjgDto dto : nyfxjgDtoList) {
            if(StringUtil.isNoneBlank(dto.getNr())){
                NyfxjgDto nyfxjgDtoi= JSON.parseObject(dto.getNr(),NyfxjgDto.class);
                nyfxjgDtos.add(nyfxjgDtoi);
            }
        }
        return nyfxjgDtos;
    }

    /**
     * 根据耐药基因查找数据
     */
    @Override
    public List<NyfxjgDto> getListByNyjy(NyfxjgDto nyfxjgDto) {

        return dao.getListByNyjy(nyfxjgDto);
    }

    /**
     * 根据ids查找数据
     */
    public List<NyfxjgDto> getDtoListByIds(NyfxjgDto nyfxjgDto){
        List<NyfxjgDto> nyfxjgDtoList=dao.getDtoListByIds(nyfxjgDto);
        List<NyfxjgDto> nyfxjgDtos=new ArrayList<>();
        for (NyfxjgDto dto : nyfxjgDtoList) {
            if("1".equals(dto.getSfbg())){
                if(StringUtil.isNoneBlank(dto.getNr())){
                    NyfxjgDto nyfxjgDto_t= JSON.parseObject(dto.getNr(),NyfxjgDto.class);
                    nyfxjgDto_t.setDrug_class(nyfxjgDto_t.getNylb());
                    nyfxjgDto_t.setOrigin_species(nyfxjgDto_t.getCkwztaxid());
                    nyfxjgDto_t.setRelated_gene(nyfxjgDto_t.getNyjzu());
                    nyfxjgDto_t.setMain_mechanism(nyfxjgDto_t.getNyjz());
                    nyfxjgDto_t.setRef_species(nyfxjgDto_t.getCkwz());
                    nyfxjgDto_t.setGenes(nyfxjgDto_t.getTopjy());
                    nyfxjgDto_t.setReads(nyfxjgDto_t.getTopjydjcxx());
                    nyfxjgDtos.add(nyfxjgDto_t);
                }
            }
        }
        return nyfxjgDtos;
    }

    /**
     * 通过ids查询List
     */
    public List<NyfxjgDto> getDtoListById(String id){
        List<NyfxjgDto> nyfxjgDtoList=dao.getDtoListById(id);
        List<NyfxjgDto> nyfxjgDtos=new ArrayList<>();
        for (NyfxjgDto dto : nyfxjgDtoList) {
           if("1".equals(dto.getSfbg())){
               if(StringUtil.isNoneBlank(dto.getNr())){
                   NyfxjgDto nyfxjgDto_t= JSON.parseObject(dto.getNr(),NyfxjgDto.class);
                   nyfxjgDto_t.setDrug_class(nyfxjgDto_t.getNylb());
                   nyfxjgDto_t.setOrigin_species(nyfxjgDto_t.getCkwztaxid());
                   nyfxjgDto_t.setRelated_gene(nyfxjgDto_t.getNyjzu());
                   nyfxjgDto_t.setMain_mechanism(nyfxjgDto_t.getNyjz());
                   nyfxjgDto_t.setRef_species(nyfxjgDto_t.getCkwz());
                   nyfxjgDto_t.setGenes(nyfxjgDto_t.getTopjy());
                   nyfxjgDto_t.setReads(nyfxjgDto_t.getTopjydjcxx());
                   nyfxjgDtos.add(nyfxjgDto_t);
               }
           }
        }
        return nyfxjgDtos;
    }

    /**
     * 耐药结果导出
     */
    public List<NyfxjgDto> getListForExp(Map<String, Object> params) {
        NyfxjgDto nyfxjgDto = (NyfxjgDto) params.get("entryData");
        nyfxjgDto.setWkcxid(nyfxjgDto.getIds().get(0));
        nyfxjgDto.setBbh(nyfxjgDto.getIds().get(1));
        List<NyfxjgDto> list = dao.getListForExp(nyfxjgDto);
        List<NyfxjgDto> newlist =new ArrayList<>();
        if(list!=null&&list.size()>0){
            for(NyfxjgDto dto:list){
                if(StringUtil.isNotBlank(dto.getNr())){
                    NyfxjgDto nyfxjgDto_t= JSON.parseObject(dto.getNr(),NyfxjgDto.class);
                    newlist.add(nyfxjgDto_t);
                }
            }
        }
        return newlist;
    }

    /**
     * 获取导出数据
     */
    public List<NyfxjgDto> getExportList(NyfxjgDto nyfxjgDto){
        return dao.getListForExp(nyfxjgDto);
    }
}
