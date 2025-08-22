package com.matridx.server.wechat.service.impl;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.server.wechat.dao.entities.*;
import com.matridx.server.wechat.dao.post.ISjycWechatDao;
import com.matridx.server.wechat.service.svcinterface.ISjycStatisticsWechatService;
import com.matridx.server.wechat.service.svcinterface.ISjycWechatService;
import com.matridx.server.wechat.service.svcinterface.ISjyctzWechatService;
import com.matridx.server.wechat.service.svcinterface.IWxyhService;
import com.matridx.server.wechat.util.WeChatUtils;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author hmz
 * @date2023/06/27 version V1.0
 **/
@Service
public class SjycWechatServiceImpl extends BaseBasicServiceImpl<SjycWechatDto, SjycWechatModel, ISjycWechatDao> implements ISjycWechatService {


    private Logger log = LoggerFactory.getLogger(SjycWechatServiceImpl.class);
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    ISjyctzWechatService sjyctzService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    IWxyhService wxyhService;

    @Autowired
    ISjycStatisticsWechatService sjycStatisticsWechatService;
    /**
     * 根据送检id获取异常信息
     *
     * @param sjycDto
     * @return
     */
    public List<SjycWechatDto> getDtoBySjid(SjycWechatDto sjycDto) {
        return dao.getDtoBySjid(sjycDto);
    }

    /**
     * 根据送检ID获取异常数量
     *
     * @param sjycDto
     * @return
     */
    public int getCountBySjid(SjycWechatDto sjycDto) {
        return dao.getCountBySjid(sjycDto);
    }
    /**
     * 评价
     * @param sjycDto
     * @return
     */
    public boolean evaluation(SjycWechatDto sjycDto) {
        int result = dao.evaluation(sjycDto);
        if (result == 0)
            return false;

        return true;
    }

    public boolean addSaveException(SjycWechatDto sjycDto) {
        boolean result = insertDto(sjycDto);
        if (!result) {
            return false;
        }

        if(sjycDto.getSjycstatisticsids()!=null&&sjycDto.getSjycstatisticsids().size()>0){
            List<SjycStatisticsWechatDto> sjycStatisticsDtoList=new ArrayList<>();
            for(String statisticsId:sjycDto.getSjycstatisticsids()){
                if(StringUtil.isBlank(statisticsId)){
                    continue;
                }
                SjycStatisticsWechatDto sjycStatisticsDto=new SjycStatisticsWechatDto();
                sjycStatisticsDto.setYcid(sjycDto.getYcid());
                sjycStatisticsDto.setSjid(sjycDto.getYwid());
                sjycStatisticsDto.setStatisticsid(StringUtil.generateUUID());
                sjycStatisticsDto.setJcsjcsid(statisticsId);
                sjycStatisticsDtoList.add(sjycStatisticsDto);
            }
            sjycStatisticsWechatService.insertList(sjycStatisticsDtoList);
        }
        String wechatycqf = "";
        List<JcsjDto> ycqfList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.EXCEPTION_DISTINGUISH.getCode());
        if (ycqfList!=null && ycqfList.size()>0) {
            for (JcsjDto jc_ycqf : ycqfList) {
                if ("WECHAT_EXCEPTION".equals(jc_ycqf.getCsdm())){
                    wechatycqf = jc_ycqf.getCsid();
                    break;
                }
            }
        }
        if (wechatycqf.equals(sjycDto.getYcqf())){
            result = saveFile(sjycDto);
        }
        return result;
    }


    /**
     * 异常新增
     *
     * @param sjycDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean insertDto(SjycWechatDto sjycDto) {
        if (StringUtil.isBlank(sjycDto.getYcid())) {
            sjycDto.setYcid(StringUtil.generateUUID());
        }
        int result = dao.insert(sjycDto);
        if (result == 0)
            return false;

        return true;
    }

    /**
     * 保存附件
     * @param sjycDto
     * @return
     */
    public boolean saveFile(SjycWechatDto sjycDto) {
        //文件复制到正式文件夹，插入信息至正式表
        if(sjycDto.getFjids()!=null && sjycDto.getFjids().size() > 0){
            String prefjidString = "";
            for (int i = 0; i < sjycDto.getFjids().size(); i++) {
                String t_fjid = sjycDto.getFjids().get(i);
                if(StringUtil.isNotBlank(t_fjid) && t_fjid.equals(prefjidString))
                    continue;
                prefjidString = t_fjid;
                boolean saveFile = fjcfbService.save2RealFile(sjycDto.getFjids().get(i),sjycDto.getYcid());
                if(!saveFile)
                    return false;
            }
        }
        // 附件排序
        FjcfbDto fjcfbDto = new FjcfbDto();
        fjcfbDto.setYwid(sjycDto.getYcid());
        fjcfbDto.setYwlx(BusTypeEnum.IMP_EXCEPTION.getCode());
        boolean result = fjcfbService.fileSort(fjcfbDto);
        if (!result) {
            log.error("异常id："+sjycDto.getYcid()+" 附件排序失败！");
        }
        return true;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean modSaveException(SjycWechatDto sjycDto) {
        // TODO Auto-generated method stub
        int result = dao.update(sjycDto);


        if (result <= 0) {
            return false;
        }
        List<String> tzrys = sjycDto.getTzrys();
        SjyctzWechatDto sjyctzDto = new SjyctzWechatDto();
        sjyctzDto.setYcid(sjycDto.getYcid());
        //先删除原有通知人员
        sjyctzService.delete(sjyctzDto);
        if (tzrys != null && tzrys.size() > 0) {
            sjyctzDto.setIds(sjycDto.getTzrys());
            sjyctzDto.setLx(sjycDto.getTzlx());
            boolean isSuccess = sjyctzService.insert(sjyctzDto);
            if (!isSuccess) {
                return false;
            }
        }
        return true;
    }


    /**
     * 结束异常任务
     * @param sjycDto
     * @return
     */
    @Override
    public boolean finishYc(SjycWechatDto sjycDto) {
        List<SjycWechatDto> sjycWechatDto=dao.getFinishList(sjycDto);
        if(sjycWechatDto!=null&&sjycWechatDto.size()>0){
            for(SjycWechatDto dto:sjycWechatDto){
                WxyhDto wxyhDto=new WxyhDto();
                wxyhDto.setWxid( dto.getTwr());
                wxyhDto=wxyhService.getDto(wxyhDto);
                Map<String, String> messageMap = new HashMap<>();
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                Date date=new Date();
                String nowDate=sdf.format(date);
                messageMap.put("keyword2", nowDate);
                messageMap.put("keyword1", dto.getYcbt());
                messageMap.put("keyword3", "投诉已关闭");
                WeChatUtils.sendWeChatMessageMap(redisUtil,restTemplate, "TEMPLATE_EXCEPTION", dto.getTwr(), wxyhDto.getWbcxdm(),messageMap);
            }
        }

        return dao.finishYc(sjycDto);
    }
}
