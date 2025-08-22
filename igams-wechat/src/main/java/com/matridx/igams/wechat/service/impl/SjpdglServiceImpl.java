package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.dao.entities.SjpdglDto;
import com.matridx.igams.wechat.dao.entities.SjpdglModel;
import com.matridx.igams.wechat.dao.entities.SjwltzDto;
import com.matridx.igams.wechat.dao.entities.SjwlxxDto;
import com.matridx.igams.wechat.dao.entities.WlcstxDto;
import com.matridx.igams.wechat.dao.post.ISjpdglDao;
import com.matridx.igams.wechat.dao.post.ISjwlxxDao;
import com.matridx.igams.wechat.enums.LogisticDataTypeEnum;
import com.matridx.igams.wechat.service.svcinterface.IHbqxService;
import com.matridx.igams.wechat.service.svcinterface.ISjpdglService;
import com.matridx.igams.wechat.service.svcinterface.ISjwltzService;
import com.matridx.igams.wechat.service.svcinterface.IWlcstxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SjpdglServiceImpl extends BaseBasicServiceImpl<SjpdglDto, SjpdglModel, ISjpdglDao> implements ISjpdglService{
 	@Autowired
    private ISjwltzService sjwltzService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;
    @Autowired
    IWlcstxService wlcstxService;
    @Autowired
    ISjwlxxDao sjwlxxDao;
    @Autowired
    IHbqxService hbqxService;
    Logger logger = LoggerFactory.getLogger(SjpdglServiceImpl.class);
    @Autowired
    ICommonService commonService;
    @Autowired
    IFjcfbService fjcfbService;
    @Value("${matridx.wechat.menuurl:}")
    private String menuurl;

    /**
     * 自动生成派单号
     * @return
     */
    public String generatePdh() {
        String date = DateUtils.getCustomFomratCurrentDate("yyyyMMdd");
        String prefix = date + "-";
        // 查询流水号
        String serial = dao.getPdhSerial(prefix);
        return prefix + serial;
    }
    /**
     * 删除
     * @param sjpdglDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    @Override
    public boolean deletesjpd(SjpdglDto sjpdglDto){
        return dao.deletesjpd(sjpdglDto);
    }
    /**
     * 获取我的物流信息
     */
    @Override
    public List<SjpdglDto> getPagedBylrry(SjpdglDto sjpdglDto) {
        return dao.getPagedBylrry(sjpdglDto);
    }

    @Override
    public boolean updatesjpd(SjpdglDto sjpdglDto) {
        return false;
    }

    /**
     * 派单保存/派单接口
     * @param sjpdglDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    @Override
    public String pdMsgSave(SjpdglDto sjpdglDto) throws RuntimeException {
        if (StringUtil.isNotBlank(sjpdglDto.getBbbh())) {
            List<SjpdglDto> bbbhRepeat = dao.isBbbhRepeat(sjpdglDto);
            if (bbbhRepeat.size() > 0) {
                throw new RuntimeException("标本编号已存在，请重新输入！");
            }
        }
        //先处理通知人员
        List<SjwltzDto> userlist = new ArrayList<>();
        if (StringUtil.isNotBlank(sjpdglDto.getTzry_str())){
            if (",".equals(sjpdglDto.getTzry_str().substring(0,1))){
                sjpdglDto.setTzry_str(sjpdglDto.getTzry_str().substring(1));//如果通知人员开头为,号，去除符号
            }
            String[] yhidlist = sjpdglDto.getTzry_str().split(",");
            userlist = new ArrayList<>();
            for (String yhid:yhidlist) {
                SjwltzDto sjwltzDto = new SjwltzDto();
                sjwltzDto.setRyid(yhid);
                userlist.add(sjwltzDto);
            }
        }
        userlist = sjwltzService.getYhinfo(userlist);
        sjpdglDto.setTzryList(userlist);

        //判断小程序点击保存是更新还是新增
        if (StringUtil.isNotBlank(sjpdglDto.getSjpdid())){
            //更新
            updateOrder(sjpdglDto);
        }else {
            //新增
            sjpdglDto.setSjpdid(StringUtil.generateUUID());
            insertDto(sjpdglDto);
        }
        return sjpdglDto.getSjpdid();
    }

     public boolean notificateTzry(SjpdglDto sjpdglDto) {
            List<SjwltzDto> userlist = sjpdglDto.getTzryList();
            //派单需要给通知人员发送钉钉消息
//            String token = talkUtil.getToken();
            if (userlist.size()>0){
                for (SjwltzDto sjwltzDto: userlist) {
                    String internalbtn = null;
                    try {
                        internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/logistics/orderlist/orderlist&wbfw=1", "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        logger.error(e.getMessage());
                    }
                    logger.error("internalbtn:" + internalbtn);
                    List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                    OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                    btnJsonList.setTitle("小程序");
                    btnJsonList.setActionUrl(internalbtn);
                    btnJsonLists.add(btnJsonList);
                    if ("td".equals(sjpdglDto.getTdbj())) {
                        talkUtil.sendCardMessageThread(sjwltzDto.getYhm(),
                                sjwltzDto.getDdid(),
                                xxglService.getMsg("ICOMM_WLTZ00001"),
                                StringUtil.replaceMsg(xxglService.getMsg("ICOMM_WLTZ00005"),
                                        sjpdglDto.getQylxr(),
                                        sjpdglDto.getQylxdh(),
                                        sjpdglDto.getQydz(),
                                        sjpdglDto.getYjsj(),
                                        sjpdglDto.getJcdwmc(),
                                        sjpdglDto.getJsfsmc(),
                                        sjpdglDto.getPdbz(),
                                        DateUtils.getCustomFomratCurrentDate("HH:mm:ss")
                                ),
                                btnJsonLists, "1"
                        );
                    }else {
                        if ("顺丰快递".equals(sjpdglDto.getJsfsmc()) || "京东快递".equals(sjpdglDto.getJsfsmc())||"京东".equals(sjpdglDto.getJsfsmc())||"同城/闪送/滴滴".equals(sjpdglDto.getJsfsmc())||"同城闪送".equals(sjpdglDto.getJsfsmc())) {
                            talkUtil.sendCardMessageThread(sjwltzDto.getYhm(),
                                    sjwltzDto.getDdid(),
                                    xxglService.getMsg("ICOMM_WLTZ00001"),
                                    StringUtil.replaceMsg(xxglService.getMsg("ICOMM_WL00005"),
                                            sjpdglDto.getJsfsmc(),
                                            sjpdglDto.getQylxr(),
                                            sjpdglDto.getQylxdh(),
                                            sjpdglDto.getHzxm(),
                                            sjpdglDto.getBbbh(),
                                            sjpdglDto.getBblxmc(),
                                            sjpdglDto.getPdh(),
                                            sjpdglDto.getGldh(),
                                            sjpdglDto.getPdbz(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")
                                    ),
                                    btnJsonLists, "1"
                            );
                        } else {
                            talkUtil.sendCardMessageThread(sjwltzDto.getYhm(),
                                    sjwltzDto.getDdid(),
                                    xxglService.getMsg("ICOMM_WLTZ00001"),
                                    StringUtil.replaceMsg(xxglService.getMsg("ICOMM_WLTZ00002"),
                                            sjpdglDto.getQylxr(),
                                            sjpdglDto.getQylxdh(),
                                            sjpdglDto.getQydz(),
                                            sjpdglDto.getYjsj(),
                                            sjpdglDto.getHzxm(),
                                            sjpdglDto.getBblxmc(),
                                            sjpdglDto.getJcdwmc(),
                                            sjpdglDto.getJsfsmc(),
                                            sjpdglDto.getPdbz(),
                                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")
                                    ),
                                    btnJsonLists, "1"
                            );
                        }
                    }

                }

            }
            //更新物流超时提醒表的次数，新增为0，更新在次数上+1
            WlcstxDto wlcstxDto = new WlcstxDto();
            wlcstxDto.setYwid(sjpdglDto.getWlid());
            wlcstxDto.setYwlx(LogisticDataTypeEnum.NOTIFY_OVERTIME.getCode());
            boolean isSuccess = wlcstxService.insertOrUpdateWlcstx(wlcstxDto);
            if (!isSuccess)
                return false;
            //派单后添加到延时队列，
            Map<String,Object> parameters = new HashMap<>();
//            parameters.put("userList", userlist);//通知人员
//            parameters.put("sjpdglDto", sjpdglDto);
            //派单后添加到延时队列，使用Map不使用Dto，避免携带无用信息
            List<Map<String,String>> userMapList = new ArrayList<>();
            for ( SjwltzDto sjwltzDto : userlist){
                Map<String,String> usermap = new HashMap<>();
                usermap.put("ddid",sjwltzDto.getDdid());
                usermap.put("ryid",sjwltzDto.getRyid());
                usermap.put("rymc",sjwltzDto.getRymc());
                usermap.put("yhm",sjwltzDto.getYhm());
                userMapList.add(usermap);
            }
            Map<String,String> sjpdMap = new HashMap<>();
            sjpdMap.put("wlid",sjpdglDto.getWlid());
            sjpdMap.put("qylxr",sjpdglDto.getQylxr());
            sjpdMap.put("qylxdh",sjpdglDto.getQylxdh());
            sjpdMap.put("qydz",sjpdglDto.getQydz());
            sjpdMap.put("yjsj",sjpdglDto.getYjsj());
            sjpdMap.put("bblxmc",sjpdglDto.getBblxmc());
            sjpdMap.put("jcdwmc",sjpdglDto.getJcdwmc());
            sjpdMap.put("jsfsmc",sjpdglDto.getJsfsmc());
            sjpdMap.put("pdbz",sjpdglDto.getPdbz());
            parameters.put("userList", userMapList);//通知人员
            parameters.put("sjpdglDto", sjpdMap);


         amqpTempl.convertAndSend("wl_delay_exchange", "wl_delay_key", JSONObject.toJSONString(parameters), new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    XtszDto gaptime = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "sjpd_gaptime")),XtszDto.class);
                    int time_min;
                    String time_str = "300000";//未取到值则取默认的五分钟
                    if (gaptime!=null && StringUtil.isNotBlank(gaptime.getSzz())){
                        time_min = Integer.parseInt(gaptime.getSzz())*60000;
                        time_str = String.valueOf(time_min);
                    }
//                    message.getMessageProperties().setExpiration("300000");
                    message.getMessageProperties().setExpiration(time_str);
                    return message;
                }
            });
            return true;
    }

    /**
     * 获取小程序派单信息
     * @param sjpdglDto
     * @return
     */
    @Override
    public Map<String, Object> pdMsgGet(SjpdglDto sjpdglDto) {
        Map<String, Object> map = new HashMap<>();
        SjpdglDto sjpdglDto_t = dao.getDtoById(sjpdglDto.getSjpdid());
        if (StringUtil.isNotBlank(sjpdglDto.getWlid())){
            sjpdglDto_t.setWlid(sjpdglDto.getWlid());
        }else {
            SjwlxxDto sjwlxxDto_t = new SjwlxxDto();
            sjwlxxDto_t.setSjpdid(sjpdglDto.getSjpdid());
            sjwlxxDto_t = sjwlxxDao.getWlxxBySjpdidAndSjwlid(sjwlxxDto_t);
            sjpdglDto_t.setWlid(sjwlxxDto_t.getWlid());
        }
        List<SjwltzDto> list=sjwltzService.getDtoListBySjpdid(sjpdglDto_t.getWlid());
        String tzry_str="";
        if (list!=null && list.size()>0){
            for (SjwltzDto sjwltzdto: list) {
                tzry_str = tzry_str +","+sjwltzdto.getRyid();
            }
            sjpdglDto_t.setXsmc(list.get(0).getXsmc());
            sjpdglDto_t.setTzry_str(tzry_str);
        }
        map.put("sjpdglDto",sjpdglDto_t);
        map.put("hbmc",sjpdglDto_t.getHbmc());
        map.put("sjwltzDtoList",list);
        return map;
    }

    /**
     * 派单修改
     * @param sjpdglDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean updateOrder(SjpdglDto sjpdglDto) {
        //获取wlid
        SjwlxxDto sjwlxxDto_t  = new SjwlxxDto();
        sjwlxxDto_t.setSjpdid(sjpdglDto.getSjpdid());
        sjwlxxDto_t = sjwlxxDao.getWlxxBySjpdidAndSjwlid(sjwlxxDto_t);
        sjpdglDto.setWlid(sjwlxxDto_t.getWlid());

         if ("1".equals(sjpdglDto.getPdbj())){
            SjwlxxDto sjwlxxDto = new SjwlxxDto();
            sjwlxxDto.setXgry(sjpdglDto.getXgry());
            sjwlxxDto.setJdbj("0");
            sjwlxxDto.setScbj("0");
            sjpdglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
            sjwlxxDto.setWlid(sjpdglDto.getWlid());
            sjwlxxDto.setWlzt(StatusEnum.CHECK_SUBMIT.getCode());
             sjwlxxDto.setJsfs(sjpdglDto.getJsfs());
             sjwlxxDto.setGldh(StringUtil.isNotBlank(sjpdglDto.getGldh())?sjpdglDto.getGldh():"");
             sjwlxxDto.setWlfy(StringUtil.isNotBlank(sjpdglDto.getWlfy())?sjpdglDto.getWlfy():"");
             sjwlxxDao.update(sjwlxxDto);
             sjwlxxDto.setQydz(sjpdglDto.getQydz());
             sjwlxxDto.setYgqjsj(sjpdglDto.getYjsj());
             sjwlxxDto.setBblxmc(sjpdglDto.getBblxmc());
             sjwlxxDto.setPdrddid(sjpdglDto.getPdrddid());
             sjwlxxDto.setLrry(sjpdglDto.getLrry());
            redisUtil.set("SJWLXX:"+ sjpdglDto.getWlid() , JSON.toJSONString(sjwlxxDto) ,28800);//注意redis过期时间是秒为单位，不是毫秒，时效8h
        }
//        FjcfbDto fjcfbDto=new FjcfbDto();
//         fjcfbDto.setYwid(sjpdglDto.getSjpdid());
//         fjcfbService.deleteByYwid(fjcfbDto);
        if (sjpdglDto.getFjids() != null && sjpdglDto.getFjids().size() > 0) {
            if("local".equals(sjpdglDto.getFjbcbj())) {
                for (int i = 0; i < sjpdglDto.getFjids().size(); i++)
                {
                    boolean saveFile = fjcfbService.save2RealFile(sjpdglDto.getFjids().get(i), sjpdglDto.getSjpdid());
                    if (!saveFile)
                        return false;
                }
            }else {
                MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                String fjids = StringUtils.join(sjpdglDto.getFjids(), ",");
                paramMap.add("fjids", fjids);
                paramMap.add("ywid", sjpdglDto.getSjpdid());
                RestTemplate restTemplate = new RestTemplate();
                String param = restTemplate.postForObject(menuurl + "/wechat/getFileAddress", paramMap, String.class);
                if (param != null) {
                    JSONArray parseArray = JSONObject.parseArray(param);
                    for (int i = 0; i < parseArray.size(); i++) {
                        FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
                        fjcfbModel.setYwid(sjpdglDto.getSjpdid());
                        // 下载服务器文件到指定文件夹
                        commonService.downloadFile(fjcfbModel);
                        fjcfbService.insert(fjcfbModel);
                    }
                }
            }
        }
        //更新送检派单管理信息
        int num = dao.update(sjpdglDto);
        if (num<=0){
            return false;
        }
        //更新送检物流通知信息
        boolean isOK = sjwltzService.deleteByYwid(sjpdglDto.getWlid());
        if (!isOK){
            return false;
        }
        List<SjwltzDto> list = new ArrayList<>();
        List<SjwltzDto> sjwltzDtos = sjpdglDto.getTzryList();
        if (sjwltzDtos!=null && sjwltzDtos.size()>0){
            for (SjwltzDto user: sjwltzDtos) {
                SjwltzDto sjwltzDto=new SjwltzDto();
                sjwltzDto.setYwid(sjpdglDto.getWlid());
                sjwltzDto.setYwlx(LogisticDataTypeEnum.NOTIFY_DISTRIBUTE.getCode());
                sjwltzDto.setRyid(user.getRyid());
                sjwltzDto.setDdid(user.getDdid());
                sjwltzDto.setXsmc(sjpdglDto.getXsmc());
                list.add(sjwltzDto);
            }
            isOK = sjwltzService.insertList(list);
            if (!isOK){
                return false;
            }
        }
        if ("1".equals(sjpdglDto.getPdbj())&&((!("自送").equals(sjpdglDto.getJsfsmc()))
                ||!(sjpdglDto.getTzry_str().equals(sjpdglDto.getLrry())))){
            notificateTzry(sjpdglDto);
        }
        return true;
    }

    /**
     * 派单保存
     * @param sjpdglDto
     * @return
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean insertDto(SjpdglDto sjpdglDto){
        if (sjpdglDto.getFjids() != null && sjpdglDto.getFjids().size() > 0) {
            if("local".equals(sjpdglDto.getFjbcbj())) {
                for (int i = 0; i < sjpdglDto.getFjids().size(); i++)
                {
                    boolean saveFile = fjcfbService.save2RealFile(sjpdglDto.getFjids().get(i), sjpdglDto.getSjpdid());
                    if (!saveFile)
                        return false;
                }
            }else {
                MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                String fjids = StringUtils.join(sjpdglDto.getFjids(), ",");
                paramMap.add("fjids", fjids);
                paramMap.add("ywid", sjpdglDto.getSjpdid());
                RestTemplate restTemplate = new RestTemplate();
                String param = restTemplate.postForObject(menuurl + "/wechat/getFileAddress", paramMap, String.class);
                if (param != null) {
                    JSONArray parseArray = JSONObject.parseArray(param);
                    for (int i = 0; i < parseArray.size(); i++) {
                        FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
                        fjcfbModel.setYwid(sjpdglDto.getSjpdid());
                        // 下载服务器文件到指定文件夹
                        commonService.downloadFile(fjcfbModel);
                        fjcfbService.insert(fjcfbModel);
                    }
                }
            }
        }
        //增加一条送检物流信息数据
        SjwlxxDto sjwlxxDto = new SjwlxxDto();
        sjwlxxDto.setWlid(StringUtil.generateUUID());
        sjwlxxDto.setSjpdid(sjpdglDto.getSjpdid());
        sjwlxxDto.setLrry(sjpdglDto.getLrry());
        sjwlxxDto.setJdbj("0");
        sjwlxxDto.setScbj("0");
        sjwlxxDto.setJsfs(sjpdglDto.getJsfs());
        sjwlxxDto.setGldh(sjpdglDto.getGldh());
        sjwlxxDto.setWlfy(sjpdglDto.getWlfy());
        //判断是进行保存还是进行派单，根据不同操作更新状态
        sjpdglDto.setWlid(sjwlxxDto.getWlid());
        if ("1".equals(sjpdglDto.getPdbj())){
            sjpdglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
            sjwlxxDto.setWlzt(StatusEnum.CHECK_SUBMIT.getCode());
        }else {
            sjpdglDto.setZt(StatusEnum.CHECK_NO.getCode());
            sjwlxxDto.setWlzt(StatusEnum.CHECK_NO.getCode());
        }
        int num = sjwlxxDao.insert(sjwlxxDto);
        sjwlxxDto.setQydz(sjpdglDto.getQydz());
        sjwlxxDto.setYgqjsj(sjpdglDto.getYjsj());
        sjwlxxDto.setBblxmc(sjpdglDto.getBblxmc());
        sjwlxxDto.setPdrddid(sjpdglDto.getPdrddid());
        sjwlxxDto.setPdrmc(sjpdglDto.getPdrmc());
        redisUtil.set("SJWLXX:"+ sjwlxxDto.getWlid() , JSON.toJSONString(sjwlxxDto) ,28800);//注意redis过期时间是秒为单位，不是毫秒，时效8h
        if (num<=0){
            return false;
        }
        //增加送检物流通知数据
        List<SjwltzDto> list = new ArrayList<>();
        List<SjwltzDto> sjwltzDtos = sjpdglDto.getTzryList();
        if (sjwltzDtos!=null && sjwltzDtos.size()>0){
            for (SjwltzDto user: sjwltzDtos) {
                SjwltzDto sjwltzDto=new SjwltzDto();
                sjwltzDto.setYwid(sjwlxxDto.getWlid());
                sjwltzDto.setYwlx(LogisticDataTypeEnum.NOTIFY_DISTRIBUTE.getCode());
                sjwltzDto.setRyid(user.getRyid());
                sjwltzDto.setDdid(user.getDdid());
                sjwltzDto.setXsmc(sjpdglDto.getXsmc());
                list.add(sjwltzDto);
            }
            boolean isok = sjwltzService.insertList(list);
            if (!isok){
                return false;
            }
        }
        String pdh = generatePdh();
        sjpdglDto.setPdh(pdh);
        //获取当前时间
        if ("1".equals(sjpdglDto.getPdbj())) {
            StringBuffer stringBuffer = new StringBuffer();//添加历史记录
            String str="您的订单派单成功!派单人:"+sjpdglDto.getPdrmc();
            stringBuffer.append(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
            stringBuffer.append("\n");
            stringBuffer.append(str);
            if(StringUtil.isNotBlank(sjpdglDto.getJsfsmc())){//添加寄送方式
                stringBuffer.append("\n");
                stringBuffer.append("寄送方式：");
                stringBuffer.append(sjpdglDto.getJsfsmc());
            }
            if(StringUtil.isNotBlank(sjpdglDto.getGldh())){//添加关联单号
                stringBuffer.append("\n");
                stringBuffer.append("关联单号：");
                stringBuffer.append(sjpdglDto.getGldh());
            }
            if(StringUtil.isNotBlank(sjpdglDto.getBbbh())){//添加关联单号
                stringBuffer.append("\n");
                stringBuffer.append("样本编号：");
                stringBuffer.append(sjpdglDto.getBbbh());
            }
            if(StringUtil.isNotBlank(sjpdglDto.getWlfy())){//配送费用
                stringBuffer.append("\n");
                stringBuffer.append("配送费用：");
                stringBuffer.append(sjpdglDto.getWlfy());
            }
            if(StringUtil.isNotBlank(sjpdglDto.getQylxdh())){//添加联系电话
                stringBuffer.append("\n");
                stringBuffer.append("联系电话：");
                stringBuffer.append(sjpdglDto.getQylxr());
            }
            if(StringUtil.isNotBlank(sjpdglDto.getPdbz())){//添加派单备注
                stringBuffer.append("\n");
                stringBuffer.append("派单备注：");
                stringBuffer.append(sjpdglDto.getPdbz());
            }
            sjpdglDto.setLsjl(stringBuffer.toString()+"\n");
        }
        int i = dao.insert(sjpdglDto);
        if (i<=0){
            return false;
        }
        //给派单通知人员发送钉钉消息
        if ("1".equals(sjpdglDto.getPdbj())&&((!("自送".equals(sjpdglDto.getJsfsmc())))
                ||!(sjpdglDto.getTzry_str().equals(sjpdglDto.getLrry())))){
            notificateTzry(sjpdglDto);
        }
        return true;
    }

    /**
     * 查询历史订单
     * @param sjpdglDto
     * @return
     */
    @Override
    public List<SjpdglDto> getPagedHistoryOrder(SjpdglDto sjpdglDto) {
        return dao.getPagedHistoryOrder(sjpdglDto);
    }

    /**
     * 确认标本编号是否重复
     * @param sjpdglDto
     * @return
     */
    @Override
    public List<SjpdglDto> isBbbhRepeat(SjpdglDto sjpdglDto){
        return dao.isBbbhRepeat(sjpdglDto);
    }
    /**
     * 接单列表
     */
    public List<SjpdglDto> getPagedOrderList(SjpdglDto sjpdglDto){
        return dao.getPagedOrderList(sjpdglDto);
    }

    /**
     * 根据录入人员查找最新的派单管理数据
     * @param sjpdglDto
     * @return
     */
    @Override
    public SjpdglDto getLatestSjpd(SjpdglDto sjpdglDto) {
        return dao.getLatestSjpd(sjpdglDto);
    }

    /**
     * 取消订单
     * @param sjpdglDto
     * @return
     */
    public boolean cancelOrder(SjpdglDto sjpdglDto){
        //获取当前时间
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(simpleDateFormat.format(date));
        stringBuffer.append("\n");
        stringBuffer.append("订单已取消!取消人员:");
        stringBuffer.append(sjpdglDto.getLsjl());
        if(StringUtil.isNotBlank(sjpdglDto.getQxpdyy())){//添加派单备注
            stringBuffer.append("\n");
            stringBuffer.append("取消原因：");
            stringBuffer.append(sjpdglDto.getQxpdyy());
        }
        sjpdglDto.setLsjl(stringBuffer.toString()+"\n");
        int result=dao.update(sjpdglDto);
        if(result>0){
            SjwlxxDto sjwlxxDto=new SjwlxxDto();
            sjwlxxDto.setSjpdid(sjpdglDto.getSjpdid());
            List<SjwlxxDto> sjwlxxDtoList=sjwlxxDao.getWlxxList(sjwlxxDto);
            if(sjwlxxDtoList!=null && sjwlxxDtoList.size()>0){
                for(SjwlxxDto t_sjwlxx : sjwlxxDtoList){
                    redisUtil.del("SJWLXX:"+t_sjwlxx.getWlid());
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<SjpdglDto> getPagedOrdersByZt(SjpdglDto sjpdglDto, User user) {
        if (StringUtil.isNotBlank(user.getDqjs())){
            sjpdglDto.setJsid(user.getDqjs());
            sjpdglDto.setDqjsdwxdbj(user.getDqjsdwxdbj());
        }
        else {
            List<String> jsids =getAllXzJsids(sjpdglDto);
            sjpdglDto.setJsid(jsids.get(0));
            SjpdglDto sjpdglDto1=getDqjsdwxdbjByJsid(sjpdglDto);
            sjpdglDto.setDqjsdwxdbj(sjpdglDto1.getDqjsdwxdbj());
        }
        if (sjpdglDto.getDqjsdwxdbj().equals("1")) {
            List<String> jcdws = getJcdwListByJsid(sjpdglDto);
            sjpdglDto.setSjjcdws(jcdws);
        }
        if (sjpdglDto.getSign().equals("signal")) {
            List<String> sjhbs = hbqxService.getHbmcByYhid(sjpdglDto.getLrry());
            sjpdglDto.setSjhbs(sjhbs);
        }
        return dao.getPagedOrdersByZt(sjpdglDto);
    }

    @Override
    public List<SjpdglDto> getPagedDtoList(SjpdglDto sjpdglDto) {
        List<SjpdglDto> pagedDtoList = dao.getPagedDtoList(sjpdglDto);
        if (!CollectionUtils.isEmpty(pagedDtoList)){
            for (SjpdglDto dto : pagedDtoList) {
                if (StringUtil.isNotBlank(dto.getYssc())){
                    long yssc = Long.parseLong(dto.getYssc());
                    long days = TimeUnit.MILLISECONDS.toDays(yssc);
                    if (days>0){
                        yssc = new BigDecimal(yssc).subtract(new BigDecimal(days).multiply(new BigDecimal(1000*60*60*24))).intValue();
                    }
                    long hours = TimeUnit.MILLISECONDS.toHours(yssc);
                    if (hours>0){
                        yssc = new BigDecimal(yssc).subtract(new BigDecimal(hours).multiply(new BigDecimal(1000*60*60))).intValue();
                    }
                    long minutes = TimeUnit.MILLISECONDS.toMinutes(yssc);
                    if (minutes>0){
                        yssc = new BigDecimal(yssc).subtract(new BigDecimal(minutes).multiply(new BigDecimal(1000*60))).intValue();
                    }
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(yssc);
                    if (seconds>0){
                        yssc = new BigDecimal(yssc).subtract(new BigDecimal(seconds).multiply(new BigDecimal(1000))).intValue();
                    }
                    dto.setYssc((days==0?"":days+"天")+(hours==0?"":hours+"小时")+(minutes==0?"":minutes+"分钟")+(seconds==0?"":seconds+"秒")+yssc+"毫秒");
                }
            }
        }
        return pagedDtoList;
    }

    @Override
    public List<SjpdglDto> getDtoListSignalAndAll(SjpdglDto sjpdglDto, User user) {
            if (StringUtil.isNotBlank(user.getDqjs())){
                sjpdglDto.setJsid(user.getDqjs());
                sjpdglDto.setDqjsdwxdbj(user.getDqjsdwxdbj());
            }
            else {
                List<String> jsids =getAllXzJsids(sjpdglDto);
                sjpdglDto.setJsid(jsids.get(0));
                SjpdglDto sjpdglDto1=getDqjsdwxdbjByJsid(sjpdglDto);
                sjpdglDto.setDqjsdwxdbj(sjpdglDto1.getDqjsdwxdbj());
            }
            if (sjpdglDto.getDqjsdwxdbj().equals("1")) {
                List<String> jcdws = getJcdwListByJsid(sjpdglDto);
                sjpdglDto.setSjjcdws(jcdws);
            }
        if (sjpdglDto.getSign().equals("signal")) {
            List<String> sjhbs = hbqxService.getHbmcByYhid(sjpdglDto.getLrry());
            sjpdglDto.setSjhbs(sjhbs);
        }
        return dao.getPagedDtoList(sjpdglDto);
    }

    @Override
    public List<String> getJcdwListByJsid(SjpdglDto sjpdglDto) {
        return dao.getJcdwListByJsid(sjpdglDto);
    }

    @Override
    public List<String> getAllXzJsids(SjpdglDto sjpdglDto) {
        return dao.getAllXzJsids(sjpdglDto);
    }

    @Override
    public SjpdglDto getDqjsdwxdbjByJsid(SjpdglDto sjpdglDto) {
        return dao.getDqjsdwxdbjByJsid(sjpdglDto);
    }

    /**
     * 导出
     *
     * @param sjpdglDto
     * @return
     */
    @Override
    public int getCountForSearchExp(SjpdglDto sjpdglDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(sjpdglDto);
    }
	
	@Override
    public int updateSjpdxx(SjpdglDto sjpdglDto) {
        return dao.updateSjpdxx(sjpdglDto);
    }
   /**
     * 根据搜索条件获取导出信息
     *
     * @param params
     * @return
     */
    public List<SjpdglDto> getListForSearchExp(Map<String, Object> params) {
        SjpdglDto sjpdglDto = (SjpdglDto) params.get("entryData");
        queryJoinFlagExport(params, sjpdglDto);
        return dao.getListForSearchExp(sjpdglDto);
    }

    /**
     * 根据选择信息获取导出信息
     *
     * @param params
     * @return
     */
    public List<SjpdglDto> getListForSelectExp(Map<String, Object> params) {
        SjpdglDto sjpdglDto = (SjpdglDto) params.get("entryData");
        queryJoinFlagExport(params, sjpdglDto);
        return dao.getListForSelectExp(sjpdglDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, SjpdglDto sjpdglDto) {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuffer sqlParam = new StringBuffer();
        for (DcszDto dcszDto : choseList) {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs = sqlParam.toString();
        sjpdglDto.setSqlParam(sqlcs);
    }
    @Override
    public Map<String,Object> getTodayArriveYbs(SjpdglDto sjpdglDto,User user) {
        if (StringUtil.isNotBlank(user.getDqjs())){
            sjpdglDto.setJsid(user.getDqjs());
            sjpdglDto.setDqjsdwxdbj(user.getDqjsdwxdbj());
        }
        else {
            List<String> jsids =getAllXzJsids(sjpdglDto);
            sjpdglDto.setJsid(jsids.get(0));
            SjpdglDto sjpdglDto1=getDqjsdwxdbjByJsid(sjpdglDto);
            sjpdglDto.setDqjsdwxdbj(sjpdglDto1.getDqjsdwxdbj());
        }
        if (sjpdglDto.getDqjsdwxdbj().equals("1")) {
            List<String> jcdws = getJcdwListByJsid(sjpdglDto);
            sjpdglDto.setSjjcdws(jcdws);
        }
        if (sjpdglDto.getSign().equals("signal")) {
            List<String> sjhbs = hbqxService.getHbmcByYhid(sjpdglDto.getLrry());
            sjpdglDto.setSjhbs(sjhbs);
        }
        return dao.getTodayArriveYbs(sjpdglDto);
    }
    
	public boolean dispatchRemind() {
        //找当前时间前一小时和后两小时的为接单数据进行提醒
        XtszDto sjpd_afterYgsj = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "sjpd_afterYgsj")),XtszDto.class);
        XtszDto sjpd_beforeYgsj = JSONObject.parseObject(String.valueOf(redisUtil.hget("matridx_xtsz", "sjpd_beforeYgsj")),XtszDto.class);
        SjpdglDto sjpdglDto_t = new SjpdglDto();
        sjpdglDto_t.setYjsjkssj("-"+sjpd_beforeYgsj.getSzz());
        sjpdglDto_t.setYjsjjssj(sjpd_afterYgsj.getSzz());
        List<SjpdglDto> sjpdglDtos = dao.getNotJdList(sjpdglDto_t);
        for (SjpdglDto sjpdglDto : sjpdglDtos){
            //通知派单人员
            String internalbtn = null;
            try {
                internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/logistics/orderlist/orderlist&wbfw=1","utf-8");
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage());
            }
            logger.error("internalbtn: ra"+internalbtn);
            List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
            OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
            btnJsonList.setTitle("小程序");
            btnJsonList.setActionUrl(internalbtn);
            btnJsonLists.add(btnJsonList);
            talkUtil.sendCardMessage(sjpdglDto.getYhm(),
                    sjpdglDto.getPdrddid(),
                    xxglService.getMsg("ICOMM_WLCSTZ00003"),
                    StringUtil.replaceMsg(  xxglService.getMsg("ICOMM_WLCSTZ00004"),
                            sjpdglDto.getQylxr(),
                            sjpdglDto.getQylxdh(),
                            sjpdglDto.getQydz(),
                            sjpdglDto.getYjsj(),
                            sjpdglDto.getBblxmc(),
                            sjpdglDto.getJcdwmc(),
                            sjpdglDto.getJsfsmc(),
                            sjpdglDto.getPdbz(),
                            DateUtils.getCustomFomratCurrentDate("HH:mm:ss")
                    ),
                    btnJsonLists,"1"
            );
            //通知接单人员
            List<SjwltzDto> sjwltzDtos = sjwltzService.getDtoListByYwid(sjpdglDto.getWlid());
            for (SjwltzDto sjwltzDto: sjwltzDtos){
                talkUtil.sendCardMessage(sjwltzDto.getYhm(),
                        sjwltzDto.getDdid(),
                        xxglService.getMsg("ICOMM_WLCSTZ00001"),
                        StringUtil.replaceMsg(  xxglService.getMsg("ICOMM_WLCSTZ00002"),
                                sjpdglDto.getQylxr(),
                                sjpdglDto.getQylxdh(),
                                sjpdglDto.getQydz(),
                                sjpdglDto.getYjsj(),
                                sjpdglDto.getBblxmc(),
                                sjpdglDto.getJcdwmc(),
                                sjpdglDto.getJsfsmc(),
                                sjpdglDto.getPdbz(),
                                DateUtils.getCustomFomratCurrentDate("HH:mm:ss")
                        ),
                        btnJsonLists,"1"
                );
            }
        }
        return true;
    }

    @Override
    public SjpdglDto getSfpd(SjpdglDto sjpdglDto) {
        return dao.getSfpd(sjpdglDto);
    }

    @Override
    public boolean updateByGldh(SjpdglDto sjpdglDto) {
        return dao.updateByGldh(sjpdglDto);
    }

    @Override
    public void getYssc(SjpdglDto sjpdglDto) {
        if (StringUtil.isNotBlank(sjpdglDto.getYssc())){
            long yssc = Long.parseLong(sjpdglDto.getYssc());
            long days = TimeUnit.MILLISECONDS.toDays(yssc);
            if (days>0){
                yssc = new BigDecimal(yssc).subtract(new BigDecimal(days).multiply(new BigDecimal(1000*60*60*24))).intValue();
            }
            long hours = TimeUnit.MILLISECONDS.toHours(yssc);
            if (hours>0){
                yssc = new BigDecimal(yssc).subtract(new BigDecimal(hours).multiply(new BigDecimal(1000*60*60))).intValue();
            }
            long minutes = TimeUnit.MILLISECONDS.toMinutes(yssc);
            if (minutes>0){
                yssc = new BigDecimal(yssc).subtract(new BigDecimal(minutes).multiply(new BigDecimal(1000*60))).intValue();
            }
            long seconds = TimeUnit.MILLISECONDS.toSeconds(yssc);
            if (seconds>0){
                yssc = new BigDecimal(yssc).subtract(new BigDecimal(seconds).multiply(new BigDecimal(1000))).intValue();
            }
            sjpdglDto.setYssc((days==0?"":days+"天")+(hours==0?"":hours+"小时")+(minutes==0?"":minutes+"分钟")+(seconds==0?"":seconds+"秒")+yssc+"毫秒");
        }
    }
}
