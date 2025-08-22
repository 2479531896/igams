package com.matridx.igams.hrm.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.hrm.dao.entities.GrjxmxDto;
import com.matridx.igams.hrm.dao.entities.GrjxmxModel;
import com.matridx.igams.hrm.dao.post.IGrjxmxDao;
import com.matridx.igams.hrm.service.svcinterface.IGrjxmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:JYK
 */
@Service
public class GrjxmxServiceImpl extends BaseBasicServiceImpl<GrjxmxDto, GrjxmxModel, IGrjxmxDao> implements IGrjxmxService {
    @Override
    public boolean insertGrjxmxDtos(List<GrjxmxDto> grjxmxDtos) {
        return dao.insertGrjxmxDtos(grjxmxDtos);
    }

    //获取树结构 绩效模板明细
    @Override
    public List<GrjxmxDto> getNewDtoList(List<GrjxmxDto> grjxmxDtos) {
        List<GrjxmxDto> newDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(grjxmxDtos)){
            for (GrjxmxDto dto : grjxmxDtos) {
                if (StringUtil.isBlank(dto.getZbsjid())){
                    List<GrjxmxDto> out = out(dto.getJxmbmxid(), grjxmxDtos);
                    dto.setChildren(out);
                    newDtoList.add(dto);
                }
            }
        }
        return newDtoList;
    }
    //递归获取
    private List<GrjxmxDto> out(String jxmbmxid,List<GrjxmxDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)){
            return dtoList;
        }
        //获取下级
        List<GrjxmxDto> grjxmxDtos = new ArrayList<>();
        for (GrjxmxDto grjxmxDto : dtoList) {
            if (jxmbmxid.equals(grjxmxDto.getZbsjid())){
                grjxmxDtos.add(grjxmxDto);
            }
        }
        //无下级停止递归
        if(CollectionUtils.isNotEmpty(grjxmxDtos)) {
            for (GrjxmxDto grjxmxDto: grjxmxDtos) {
                grjxmxDto.setChildren(out(grjxmxDto.getJxmbmxid(),dtoList));
            }
        }
        return grjxmxDtos;
    }


    @Override
    public boolean updateGrjxmxDtosWithNull(List<GrjxmxDto> grjxmxDtos) {
        return dao.updateGrjxmxDtosWithNull(grjxmxDtos);
    }

    @Override
    public boolean updateGrjxmxDtosWithGwid(List<GrjxmxDto> grjxmxDtos) {
        return dao.updateGrjxmxDtosWithGwid(grjxmxDtos);
    }

    @Override
    public String getSumScore(GrjxmxDto grjxmxDto) {
        return dao.getSumScore(grjxmxDto);
    }

    @Override
    public List<GrjxmxDto> getDtoListWithScore(GrjxmxDto grjxmxDto) {
        return dao.getDtoListWithScore(grjxmxDto);
    }

    @Override
    public List<GrjxmxDto> getDtoListWithNull(GrjxmxDto grjxmxDto) {
        return dao.getDtoListWithNull(grjxmxDto);
    }

    @Override
    public boolean discard(GrjxmxDto grjxmxDto) {
        return dao.discard(grjxmxDto);
    }


    @Override
    public List<GrjxmxDto> getDtoListByGwid(GrjxmxDto grjxmxDto) {
        return dao.getDtoListByGwid(grjxmxDto);
    }
}
