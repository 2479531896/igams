package com.matridx.igams.hrm.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.JxmbmxDto;
import com.matridx.igams.hrm.dao.entities.JxmbmxModel;
import com.matridx.igams.hrm.dao.post.IJxmbmxDao;
import com.matridx.igams.hrm.service.svcinterface.IJxmbmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:JYK
 */
@Service
public class JxmbmxServiceImpl extends BaseBasicServiceImpl<JxmbmxDto, JxmbmxModel, IJxmbmxDao> implements IJxmbmxService {

    @Override
    public List<JxmbmxDto> getJxmbmxList(JxmbmxDto jxmbmxDto) {
        List<JxmbmxDto> jxmbmxDtoList=getDtoList(jxmbmxDto);
        List<JxmbmxDto> jxmbmxDtoList_sj=new ArrayList<>();
        List<JxmbmxDto> jxmbmxDtoList_xj=new ArrayList<>();
        for (JxmbmxDto jxmbmxDto_t:jxmbmxDtoList){
            if (StringUtil.isBlank(jxmbmxDto_t.getZbsjid())){
                jxmbmxDtoList_sj.add(jxmbmxDto_t);
            }else {
                jxmbmxDtoList_xj.add(jxmbmxDto_t);
            }
        }
        if (CollectionUtils.isNotEmpty(jxmbmxDtoList_sj)){
            for (JxmbmxDto dto : jxmbmxDtoList_sj) {
                getChrild(dto, jxmbmxDtoList_xj);
            }
        }
        return jxmbmxDtoList_sj;
    }

    @Override
    public boolean insertMbmxList(List<JxmbmxDto> list) {
        return dao.insertMbmxList(list);
    }

    @Override
    public boolean deleteByJxmbid(String jxmbid) {
        return dao.deleteByJxmbid(jxmbid);
    }

    public void getChrild(JxmbmxDto jxmbmxDto,List<JxmbmxDto> list){
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        List<JxmbmxDto> resultList=new ArrayList<>();
        for (int i=0;i<list.size();i++){
            if (list.get(i).getZbsjid().equals(jxmbmxDto.getJxmbmxid())){
                resultList.add(list.get(i));
                getChrild(resultList.get(resultList.size()-1),list);

            }
        }
        jxmbmxDto.setChildren(resultList);

    }
    //获取树结构 绩效模板明细
    @Override
    public List<JxmbmxDto> getNewDtoList(List<JxmbmxDto> jxmbmxDtos) {
        List<JxmbmxDto> newDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(jxmbmxDtos)){
            for (JxmbmxDto dto : jxmbmxDtos) {
                if (StringUtil.isBlank(dto.getZbsjid())){
                    List<JxmbmxDto> out = out(dto.getJxmbmxid(), jxmbmxDtos);
                    dto.setChildren(out);
                    newDtoList.add(dto);
                }
            }
        }
        return newDtoList;
    }

    //递归获取
    private List<JxmbmxDto> out(String jxmbmxid,List<JxmbmxDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)){
            return dtoList;
        }
        //获取下级
        List<JxmbmxDto> jxmbmxDtos = new ArrayList<>();
        for (JxmbmxDto jxmbmxDto : dtoList) {
            if (jxmbmxid.equals(jxmbmxDto.getZbsjid())){
                jxmbmxDtos.add(jxmbmxDto);
            }
        }
        //无下级停止递归
        if(CollectionUtils.isNotEmpty(jxmbmxDtos)) {
            for (JxmbmxDto jxmbmxDto: jxmbmxDtos) {
                jxmbmxDto.setChildren(out(jxmbmxDto.getJxmbmxid(),dtoList));
            }
        }
        return jxmbmxDtos;
    }
}
