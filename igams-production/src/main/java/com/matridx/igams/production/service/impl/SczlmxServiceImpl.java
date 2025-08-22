package com.matridx.igams.production.service.impl;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.production.dao.entities.SczlmxDto;
import com.matridx.igams.production.dao.entities.SczlmxModel;
import com.matridx.igams.production.dao.entities.XqjhmxDto;
import com.matridx.igams.production.dao.post.ISczlmxDao;
import com.matridx.igams.production.service.svcinterface.ISczlmxService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class SczlmxServiceImpl extends BaseBasicServiceImpl<SczlmxDto, SczlmxModel, ISczlmxDao> implements ISczlmxService {
    private final Logger log = LoggerFactory.getLogger(SczlmxServiceImpl.class);

    @Override
    public Boolean insertList(List<SczlmxDto> sczlmxDtos) {
        return dao.insertList(sczlmxDtos)!=0;
    }

    @Override
    public Boolean updateList(List<SczlmxDto> sczlmxDtos) {
        return dao.updateList(sczlmxDtos)!=0;
    }

    @Override
    public List<SczlmxDto> getProduceEligibles(SczlmxDto sczlmxDto) {
        return dao.getProduceEligibles(sczlmxDto);
    }

    /**
     * 根据接收日期的开始日期和结束日期，自动计算每一天的日期，形成一个list
     */
    public List<String> getRqsByStartAndEnd(SczlmxDto sczlmxDto)
    {
        List<String> rqs = new ArrayList<>();
        try
        {
            if ("day".equals(sczlmxDto.getTj()))
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse(sczlmxDto.getLrsjstart()));
                Calendar endcalendar = Calendar.getInstance();
                if (StringUtil.isNotBlank(sczlmxDto.getLrsjend()))
                {
                    endcalendar.setTime(sdf.parse(sczlmxDto.getLrsjend()));
                } else
                {
                    endcalendar.setTime(new Date());
                }
                while (endcalendar.compareTo(calendar) >= 0)
                {
                    rqs.add(sdf.format(calendar.getTime()));
                    calendar.add(Calendar.DATE, 1);
                }
            }else if ("week".equals(sczlmxDto.getTj())&&StringUtil.isNotBlank(sczlmxDto.getZq())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(sdf.parse(sczlmxDto.getLrsjend()));
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    log.error(e.getMessage());
                }
                int zq = Integer.parseInt(sczlmxDto.getZq());
                for (int i = 0; i < zq; i++)
                {
                    rqs.add(sdf.format(calendar.getTime()));
                    calendar.add(Calendar.DATE, -7);
                }
                sczlmxDto.setLrsjstart(rqs.get(rqs.size()-1));
            } else if ("mon".equals(sczlmxDto.getTj()))
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse(sczlmxDto.getLrsjMstart()));

                Calendar endcalendar = Calendar.getInstance();
                if (StringUtil.isNotBlank(sczlmxDto.getLrsjMend()))
                {
                    endcalendar.setTime(sdf.parse(sczlmxDto.getLrsjMend()));
                } else
                {
                    endcalendar.setTime(new Date());
                }

                while (endcalendar.compareTo(calendar) >= 0)
                {
                    rqs.add(sdf.format(calendar.getTime()));
                    calendar.add(Calendar.MONTH, 1);
                }
            } else if ("year".equals(sczlmxDto.getTj()))
            {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sdf.parse(sczlmxDto.getLrsjYstart()));

                Calendar endcalendar = Calendar.getInstance();
                if (StringUtil.isNotBlank(sczlmxDto.getLrsjYend()))
                {
                    endcalendar.setTime(sdf.parse(sczlmxDto.getLrsjYend()));
                } else
                {
                    endcalendar.setTime(new Date());
                }

                while (endcalendar.compareTo(calendar) >= 0)
                {
                    rqs.add(sdf.format(calendar.getTime()));
                    calendar.add(Calendar.YEAR, 1);
                }
            }
        } catch (Exception e)
        {
            log.error(e.getMessage());
        }
        return rqs;
    }

    @Override
    public List<SczlmxDto> getProducePlanStatis(SczlmxDto sczlmxDto) {
        return dao.getProducePlanStatis(sczlmxDto);
    }

    @Override
    public List<SczlmxDto> getSczlmxDtos(List<XqjhmxDto> list) {
        return dao.getSczlmxDtos(list);
    }

    @Override
    public List<SczlmxDto> getPagedProduce(SczlmxDto sczlmxDto) {
        return dao.getPagedProduce(sczlmxDto);
    }

    @Override
    public String getProgressSerial(String prefix) {
        return dao.getProgressSerial(prefix);
    }

    @Override
    public List<SczlmxDto> getDtoListForPrint(SczlmxDto sczlmxDto) {
        return dao.getDtoListForPrint(sczlmxDto);
    }
}
