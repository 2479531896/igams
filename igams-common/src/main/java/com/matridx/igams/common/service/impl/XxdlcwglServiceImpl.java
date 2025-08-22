package com.matridx.igams.common.service.impl;

import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.dao.entities.XxdlcwglDto;
import com.matridx.igams.common.dao.entities.XxdlcwglModel;
import com.matridx.igams.common.dao.post.IXxdlcwglDao;
import com.matridx.igams.common.service.svcinterface.IXxdlcwglService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

@Service
public class XxdlcwglServiceImpl extends BaseBasicServiceImpl<XxdlcwglDto, XxdlcwglModel, IXxdlcwglDao> implements IXxdlcwglService{

    private static final Logger log = LoggerFactory.getLogger(XxdlcwglServiceImpl.class);
    public boolean insert(XxdlcwglDto xxdlcwglDto) {
        XxdlcwglDto xxdlcwglDto_t = new XxdlcwglDto();
        xxdlcwglDto_t.setCwid(xxdlcwglDto.getCwid());
        xxdlcwglDto_t.setCwlx(xxdlcwglDto.getCwlx());
        if (StringUtil.isNotBlank(xxdlcwglDto.getYsnr())){
            xxdlcwglDto_t.setYsnr(xxdlcwglDto.getYsnr().length()>3999?xxdlcwglDto.getYsnr().substring(0, 3999):xxdlcwglDto.getYsnr());
        }else {
            xxdlcwglDto_t.setYsnr(xxdlcwglDto.getYsnr());
        }
        int result = dao.insert(xxdlcwglDto_t);
        return result > 0;
    }

    public int deleteByDate(String date){
        return dao.deleteByDate(date);
    }


    /**
     * 定时任务：每隔一天更新清理数据
     */
    public void clearXxdlcwglDate(Map<String,Object> codeMap) {
        if (codeMap != null && codeMap.get("date") != null) {
            String date = (String) codeMap.get("date");
            if (StringUtil.isNotBlank(date)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, Integer.parseInt(date));
                String lastDate = sdf.format(cal.getTime());
                int size = deleteByDate(lastDate);
                log.error("定时清理Xxdlcwgl数据成功，清理数据条数为：" + size);
            }
        }
    }
}
