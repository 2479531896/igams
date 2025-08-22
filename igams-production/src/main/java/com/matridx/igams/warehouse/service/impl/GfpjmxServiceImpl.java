package com.matridx.igams.warehouse.service.impl;


import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IShxxService;
import com.matridx.igams.warehouse.dao.entities.GfpjmxDto;
import com.matridx.igams.warehouse.dao.entities.GfpjmxModel;
import com.matridx.igams.warehouse.dao.post.IGfpjmxDao;
import com.matridx.igams.warehouse.service.svcinterface.IGfpjmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jld
 */
@Service
public class GfpjmxServiceImpl extends BaseBasicServiceImpl<GfpjmxDto, GfpjmxModel, IGfpjmxDao> implements IGfpjmxService {
    @Autowired
    IShxxService shxxService;
    /**
     * @Description: 供方评价明细新增
     * @param gfpjmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/26 13:56
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insetGfpjmxDtoList(List<GfpjmxDto> gfpjmxDtoList) {
        return dao.insetGfpjmxDtoList(gfpjmxDtoList);
    }

    /**
     * @Description: 供方评价明细修改
     * @param gfpjmxDtoList
     * @return boolean
     * @Author: 郭祥杰
     * @Date: 2024/6/26 14:30
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateGfyzmxDtoList(List<GfpjmxDto> gfpjmxDtoList) {
        return dao.updateGfyzmxDtoList(gfpjmxDtoList);
    }

    /**
     * @Description: 获取审核意见
     * @param gfpjid
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @Author: 郭祥杰
     * @Date: 2024/7/1 15:23
     */
    @Override
    public Map<String, String> queryShxxMap(String gfpjid) {
        Map<String, String> map = new HashMap<>();
        ShxxDto shxxDto = new ShxxDto();
        shxxDto.setYwid(gfpjid);
        shxxDto.setShlb(AuditTypeEnum.AUDIT_SUPPLIER_EVALUATION.getCode());
        List<ShxxDto> shxxDtos = shxxService.getShxxLsit(shxxDto);
        map.put("cgbyj",(shxxDtos!=null && !shxxDtos.isEmpty())?
                StringUtil.isNotBlank(shxxDtos.get(0).getShyj())?shxxDtos.get(0).getShyj()
                        +"\t(姓名："+shxxDtos.get(0).getShrmc()+"\t日期:"+shxxDtos.get(0).getShsj()+")"
                        :"同意。\t(姓名："+shxxDtos.get(0).getShrmc()+"\t日期:"+shxxDtos.get(0).getShsj()+")":"/");
        map.put("yfbyj",(shxxDtos!=null && !shxxDtos.isEmpty() && shxxDtos.size()>=2)?
                StringUtil.isNotBlank(shxxDtos.get(1).getShyj())?shxxDtos.get(1).getShyj()
                        +"\t(姓名："+shxxDtos.get(1).getShrmc()+"\t日期:"+shxxDtos.get(1).getShsj()+")"
                        :"同意。\t(姓名："+shxxDtos.get(1).getShrmc()+"\t日期:"+shxxDtos.get(1).getShsj()+")":"/");
        map.put("scbyj",(shxxDtos!=null && !shxxDtos.isEmpty() && shxxDtos.size()>=3)?
                StringUtil.isNotBlank(shxxDtos.get(2).getShyj())?shxxDtos.get(2).getShyj()
                        +"\t(姓名："+shxxDtos.get(2).getShrmc()+"\t日期:"+shxxDtos.get(2).getShsj()+")"
                        :"同意。\t(姓名："+shxxDtos.get(2).getShrmc()+"\t日期:"+shxxDtos.get(2).getShsj()+")":"/");
        int t = (shxxDtos!=null && !shxxDtos.isEmpty())?shxxDtos.size():0;
        map.put("zlbyj",(shxxDtos!=null && !shxxDtos.isEmpty() && shxxDtos.size()>=6)?
                StringUtil.isNotBlank(shxxDtos.get(t-3).getShyj())?shxxDtos.get(t-3).getShyj()
                        +"\t(姓名："+shxxDtos.get(t-3).getShrmc()+"\t日期:"+shxxDtos.get(t-3).getShsj()+")"
                        :"同意。\t(姓名："+shxxDtos.get(t-3).getShrmc()+"\t日期:"+shxxDtos.get(t-3).getShsj()+")":"/");
        map.put("glzdbyj",(shxxDtos!=null && !shxxDtos.isEmpty() && shxxDtos.size()>=6)?
                StringUtil.isNotBlank(shxxDtos.get(t-2).getShyj())?shxxDtos.get(t-2).getShyj()
                        +"\t(姓名："+shxxDtos.get(t-2).getShrmc()+"\t日期:"+shxxDtos.get(t-2).getShsj()+")"
                        :"同意。\t(姓名："+shxxDtos.get(t-2).getShrmc()+"\t日期:"+shxxDtos.get(t-2).getShsj()+")":"/");
        map.put("sqryj",(shxxDtos!=null && !shxxDtos.isEmpty() && shxxDtos.size()>=6)?
                StringUtil.isNotBlank(shxxDtos.get(t-1).getShyj())?shxxDtos.get(t-1).getShyj()
                        +"\t(姓名："+shxxDtos.get(t-1).getShrmc()+"\t日期:"+shxxDtos.get(t-1).getShsj()+")"
                        :"同意。\t(姓名："+shxxDtos.get(t-1).getShrmc()+"\t日期:"+shxxDtos.get(t-1).getShsj()+")":"/");
        StringBuilder sybmyj = new StringBuilder();
        if(shxxDtos!=null && shxxDtos.size()>6){
            for (int k = 3;k<shxxDtos.size()-3;k++){
                sybmyj.append(StringUtil.isNotBlank(shxxDtos.get(k).getShyj())?
                        shxxDtos.get(k).getShyj()+"\t(姓名："+shxxDtos.get(k).getShrmc()+"\t日期:"+shxxDtos.get(k).getShsj()+")\n"
                        :"同意。\t(姓名："+shxxDtos.get(k).getShrmc()+"\t日期:"+shxxDtos.get(k).getShsj()+")\n");
            }
        }
        map.put("sybmyj",StringUtil.isNotBlank(sybmyj)?sybmyj.toString():"/");
        return map;
    }
}
