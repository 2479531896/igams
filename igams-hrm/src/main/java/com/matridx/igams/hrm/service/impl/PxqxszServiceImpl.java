package com.matridx.igams.hrm.service.impl;


import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.GzglDto;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IGzglService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.hrm.dao.entities.PxqxszDto;
import com.matridx.igams.hrm.dao.entities.PxqxszModel;
import com.matridx.igams.hrm.dao.post.IPxqxszDao;
import com.matridx.igams.hrm.service.svcinterface.IPxqxszService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class PxqxszServiceImpl extends BaseBasicServiceImpl<PxqxszDto, PxqxszModel, IPxqxszDao> implements IPxqxszService {

    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IGzglService gzglService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    /**
     * 查询机构列表(未选择)
     */
    public List<PxqxszDto> getPagedUnSelectJgList(PxqxszDto pxqxszDto){
        return dao.getPagedUnSelectJgList(pxqxszDto);
    }

    /**
     * 查询机构列表(已选择)
     */
    public List<PxqxszDto> getPagedSelectedJgList(PxqxszDto pxqxszDto){
        return dao.getPagedSelectedJgList(pxqxszDto);
    }
    /**
     * 批量新增
     */
    public boolean insertList(List<PxqxszDto> list){
        return dao.insertList(list);
    }

    /**
     * 定时任务
     */
    public List<PxqxszDto> getBeforeDayList(String day){
        return dao.getBeforeDayList(day);
    }

    /**
     * 定时发布培训任务
     */
    public void sendTrainTask(Map<String,Object> map){
        if(map.get("day")!=null){
            String day = String.valueOf(map.get("day"));
            if(StringUtil.isNotBlank(day)){
                //获取当前日期
                Date date = new Date();
                //将时间格式化成yyyy-MM-dd HH:mm:ss的格式
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                //创建Calendar实例
                Calendar cal = Calendar.getInstance();
                //设置当前时间
                cal.setTime(date);
                cal.add(Calendar.DATE, -Integer.parseInt(day));
                List<PxqxszDto> beforeDayList = getBeforeDayList(format.format(cal.getTime()));
                if(!CollectionUtils.isEmpty(beforeDayList)){
                    List<GzglDto> list=new ArrayList<>();
                    List<String> ddids=new ArrayList<>();
                    List<User> users=new ArrayList<>();
                    for(PxqxszDto dto:beforeDayList){
                        if(StringUtil.isNotBlank(dto.getPxid())){
                            if(!ddids.contains(dto.getDdid())){
                                ddids.add(dto.getDdid());
                                User user = new User();
                                user.setDdid(dto.getDdid());
                                user.setYhm(dto.getYhm());
                                users.add(user);
                            }
                            GzglDto gzglDto=new GzglDto();
                            gzglDto.setYwid(dto.getPxid());
                            gzglDto.setFzr(dto.getYhid());
                            gzglDto.setYwmc(dto.getPxbt());
                            gzglDto.setRwmc(dto.getPxlbmc());
                            gzglDto.setQwwcsj(format.format(date));
                            gzglDto.setGzid(StringUtil.generateUUID());
                            gzglDto.setZt(StatusEnum.CHECK_NO.getCode());
                            gzglDto.setJgid(dto.getJgid());
                            gzglDto.setDdid(dto.getDdid());
                            gzglDto.setUrlqz(urlPrefix);
                            gzglDto.setYwdz("/train/train/pagedataViewTrainDetails?pxid=" +gzglDto.getYwid() );
                            gzglDto.setTgbj("0");
                            gzglDto.setLrry(dto.getYhid());
                            gzglDto.setGlbj("1");
                            list.add(gzglDto);
                        }
                    }
                    if(!CollectionUtils.isEmpty(list)){
                        gzglService.insertList(list);
                    }
//            String token = talkUtil.getToken();
                    for(User user:users){
                        //小程序访问
                        String external = "/pages/index/index";
                        List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                        OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                        btnJsonList.setTitle("小程序");
                        btnJsonList.setActionUrl(external);
                        btnJsonLists.add(btnJsonList);
                        talkUtil.sendCardMessage(user.getYhm(), user.getDdid(),xxglService.getMsg("ICOMM_PX00001"), StringUtil.replaceMsg(xxglService.getMsg("ICOMM_PX00002"), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")),btnJsonLists,"1");
                    }
                }
            }
        }
    }
}
