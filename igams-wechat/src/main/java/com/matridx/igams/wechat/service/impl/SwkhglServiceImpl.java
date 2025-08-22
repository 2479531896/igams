package com.matridx.igams.wechat.service.impl;


import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.wechat.dao.entities.SwkhglDto;
import com.matridx.igams.wechat.dao.entities.SwkhglModel;
import com.matridx.igams.wechat.dao.post.ISwkhglDao;
import com.matridx.igams.wechat.service.svcinterface.ISwkhglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.Map;


@Service
public class SwkhglServiceImpl extends BaseBasicServiceImpl<SwkhglDto, SwkhglModel, ISwkhglDao> implements ISwkhglService {

    @Autowired
    RedisUtil redisUtil;

    public List<SwkhglDto> getListForSelectExp(Map<String, Object> params) {
        SwkhglDto swkhglDto = (SwkhglDto) params.get("entryData");
        queryJoinFlagExport(params,swkhglDto);
        return dao.getListForSelectExp(swkhglDto);
    }


    public List<SwkhglDto> getListForSearchExp(Map<String, Object> params) {
        SwkhglDto swkhglDto = (SwkhglDto) params.get("entryData");
        queryJoinFlagExport(params,swkhglDto);
        return dao.getListForSearchExp(swkhglDto);
    }


    public int getCountForSearchExp(SwkhglDto swkhglDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(swkhglDto);
    }

    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, SwkhglDto swkhglDto)
    {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuffer sqlParam = new StringBuffer();
        for (DcszDto dcszDto : choseList)
        {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;



            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd()))
            {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        swkhglDto.setSqlParam(sqlParam.toString());
    }

    @Override
    public List<SwkhglDto> getRepeatDtoList(SwkhglDto swkhglDto) {
        return dao.getRepeatDtoList(swkhglDto);
    }

    /**
     * 更新对账字段
     * @param swkhglDto
     * @return
     */
    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateDzzd(SwkhglDto swkhglDto){
        return dao.updateDzzd(swkhglDto);
    }

    /**
     * 定时更新客户状态区分
     */
    public void updateCustomStateDistinguish(){
        Object oncoxtsz=redisUtil.hget("matridx_xtsz","business.custom.state.standard");
        if(oncoxtsz!=null) {
            XtszDto xtszDto = JSON.parseObject(String.valueOf(oncoxtsz), XtszDto.class);
            if(StringUtil.isNotBlank(xtszDto.getSzz())){
                String szz=xtszDto.getSzz();
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH,-Integer.parseInt(szz));
                String date = DateUtils.format(calendar.getTime(), "yyyy-MM-dd");
                dao.updateCustomStateDistinguish(date);
            }
        }
    }
}
