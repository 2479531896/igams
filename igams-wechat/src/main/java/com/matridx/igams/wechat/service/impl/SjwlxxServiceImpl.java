package com.matridx.igams.wechat.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.FjcfbModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.post.ICommonDao;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.EntityTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.dao.entities.SjpdglDto;
import com.matridx.igams.wechat.dao.entities.SjtdxxDto;
import com.matridx.igams.wechat.dao.entities.SjwltzDto;
import com.matridx.igams.wechat.dao.entities.SjwlxxDto;
import com.matridx.igams.wechat.dao.entities.SjwlxxModel;
import com.matridx.igams.wechat.dao.entities.WlcstxDto;
import com.matridx.igams.wechat.dao.post.ISjwlxxDao;
import com.matridx.igams.wechat.enums.LogisticDataTypeEnum;
import com.matridx.igams.wechat.service.svcinterface.ISjkdxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjpdglService;
import com.matridx.igams.wechat.service.svcinterface.ISjtdxxService;
import com.matridx.igams.wechat.service.svcinterface.ISjwltzService;
import com.matridx.igams.wechat.service.svcinterface.ISjwlxxService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SjwlxxServiceImpl extends BaseBasicServiceImpl<SjwlxxDto, SjwlxxModel, ISjwlxxDao> implements ISjwlxxService{
    @Autowired
    private ISjpdglService sjpdglService;
    @Autowired
    IFjcfbService fjcfbService;
    @Autowired
    private ISjwltzService sjwltzService;
    @Autowired
    ICommonService commonService;
    @Autowired
    private ISjkdxxService sjkdxxService;
    @Autowired
    ICommonDao commonDao;
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private AmqpTemplate amqpTempl;
//    @Value("${matridx.dingtalk.jumpdingtalkurl:}")
//    String jumpdingtalkurl;
    @Value("${matridx.wechat.menuurl:}")
    private String menuurl;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;

    @Autowired
    IWlcstxService wlcstxService;
    @Autowired
    ISjtdxxService sjtdxxService;

    private Logger log = LoggerFactory.getLogger(SjwlxxServiceImpl.class);
    /**
     * 送达录入保存
     * @param sjwlxxDto
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean arriveSaveLr(SjwlxxDto sjwlxxDto, User user){
        if (StringUtil.isNotBlank(sjwlxxDto.getSjtdid())){
            SjtdxxDto sjtdxxDto = new SjtdxxDto();
            sjtdxxDto.setWlzt("40");
            sjtdxxDto.setSjtdid(sjwlxxDto.getSjtdid());
            boolean updatetd = sjtdxxService.update(sjtdxxDto);
            if (!updatetd){
                return false;
            }
            List<SjwlxxDto> dtoListById = dao.getDtoListById(sjwlxxDto.getSjtdid());
            for (SjwlxxDto dto : dtoListById) {
                //保存附件信息
                if (sjwlxxDto.getFjids() != null && sjwlxxDto.getFjids().size() > 0) {
                    MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                    String fjids = StringUtils.join(sjwlxxDto.getFjids(), ",");
                    paramMap.add("fjids", fjids);
                    paramMap.add("ywid", dto.getWlid());
                    RestTemplate restTemplate = new RestTemplate();
                    String param = restTemplate.postForObject(menuurl + "/wechat/getFileAddress", paramMap, String.class);
                    if (param != null) {
                        JSONArray parseArray = JSONObject.parseArray(param);
                        for (int i = 0; i < parseArray.size(); i++) {
                            FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
                            fjcfbModel.setYwid(dto.getWlid());
                            fjcfbModel.setFjid(StringUtil.generateUUID());
                            // 下载服务器文件到指定文件夹
                            commonService.downloadFile(fjcfbModel);
                            fjcfbService.insert(fjcfbModel);
                        }
                    }
                }
                //将原先的物流状态改为40
                SjwlxxDto sjwlxxDto_t = new SjwlxxDto();
                sjwlxxDto_t.setWlid(dto.getWlid());
                sjwlxxDto_t.setWlzt("40");
                Date date = new Date();
                SimpleDateFormat df_24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sjwlxxDto_t.setSdsj(df_24.format(date));
                sjwlxxDto_t.setSdbz(dto.getSdbz());
                sjwlxxDto_t.setXgry(user.getYhid());
                int update = dao.update(sjwlxxDto_t);
                if (update <= 0) {
                    return false;
                }
                //更改派单的状态

                SjpdglDto sjpdglDto = new SjpdglDto();
                sjpdglDto.setSjpdid(dto.getSjpdid());
                sjpdglDto.setZt("50");
                sjpdglDto.setXgry(user.getYhid());
                if (!"qt".equals(sjwlxxDto.getJsfsdm())) {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
                    stringBuffer.append("\n");
                    stringBuffer.append("您的快递已送达至");
                    stringBuffer.append(dto.getJcdwmc());
                    sjpdglDto.setLsjl(stringBuffer.toString() + "\n");
                }
                if (StringUtil.isNotBlank(dto.getQjsj())){
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                    Date qjsjdate = DateUtils.parseDate(dto.getQjsj());
                    SimpleDateFormat df_qjsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    String qjsjstr = df_qjsj.format(qjsjdate);
                    //日期格式要对应 不然会报错
                    LocalDateTime qjsj = LocalDateTime.parse(qjsjstr, df);
                    LocalDateTime now = LocalDateTime.now();
                    long hmrqc = ChronoUnit.MILLIS.between(qjsj, now);
                    sjpdglDto.setYssc(String.valueOf(hmrqc));
                }
                boolean flag = sjpdglService.update(sjpdglDto);
                if (!flag) {
                    return false;
                }
            }
        }else {
            //保存附件信息
            if (sjwlxxDto.getFjids() != null && sjwlxxDto.getFjids().size() > 0) {
                if (!"qt".equals(sjwlxxDto.getJsfsdm())) {
                    MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                    String fjids = StringUtils.join(sjwlxxDto.getFjids(), ",");
                    paramMap.add("fjids", fjids);
                    paramMap.add("ywid", sjwlxxDto.getWlid());
                    RestTemplate restTemplate = new RestTemplate();
                    String param = restTemplate.postForObject(menuurl + "/wechat/getFileAddress", paramMap, String.class);
                    if (param != null) {
                        JSONArray parseArray = JSONObject.parseArray(param);
                        for (int i = 0; i < parseArray.size(); i++) {
                            FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
                            fjcfbModel.setYwid(sjwlxxDto.getWlid());
                            // 下载服务器文件到指定文件夹
                            commonService.downloadFile(fjcfbModel);
                            fjcfbService.insert(fjcfbModel);
                        }
                    }
                }
            }
//        if(sjwlxxDto.getFjids()!=null && sjwlxxDto.getFjids().size() > 0){
//            String prefjidString = "";
//            for (int i = 0; i < sjwlxxDto.getFjids().size(); i++) {
//                String t_fjid = sjwlxxDto.getFjids().get(i);
//                if(StringUtil.isNotBlank(t_fjid) && t_fjid.equals(prefjidString))
//                    continue;
//
//                prefjidString = t_fjid;
//
//                boolean saveFile = fjcfbService.save2RealFile(sjwlxxDto.getFjids().get(i),sjwlxxDto.getWlid());
//                if(!saveFile)
//                    return false;
//            }
//        }
            SjwlxxDto dtoById = dao.getQjsj(sjwlxxDto.getWlid());
            if ("GT".equals(sjwlxxDto.getJsfsdm())) {//判断寄送方式是否为 高铁
//                List<JcsjDto> jsfsList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SENDING_METHOD.getCode());//寄送方式并为快递类型
                List<JcsjDto> jsfsList = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode());//寄送方式并为快递类型
                for (JcsjDto dto : jsfsList) {
                    if ("QYY".equals(dto.getCsdm())) {
                        sjwlxxDto.setJsfs(dto.getCsid());
                        sjwlxxDto.setJsfsmc(dto.getCsmc());
                    }
                }
                //将原先的物流状态改为40
                SjwlxxDto sjwlxxDto_t = new SjwlxxDto();
                sjwlxxDto_t.setWlid(sjwlxxDto.getWlid());
                sjwlxxDto_t.setWlzt("40");
                sjwlxxDto_t.setXgry(user.getYhid());
                sjwlxxDto_t.setSdsj(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                sjwlxxDto_t.setSdbz(sjwlxxDto.getSdbz());
                int update = dao.update(sjwlxxDto_t);
                if (update <= 0) {
                    return false;
                }
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("您的快递已送达至高铁站,班次为");
                stringBuffer.append(sjwlxxDto.getBc());
                stringBuffer.append("目的地为");
                stringBuffer.append(sjwlxxDto.getDddd());
                if (sjwlxxDto.getYjddsj() == null || sjwlxxDto.getYjddsj() == "") {
                    stringBuffer.append(",预计");
                    stringBuffer.append(sjwlxxDto.getYjddsj());
                    stringBuffer.append("到达");
                }
                if (StringUtil.isNotBlank(sjwlxxDto.getSdbz())) {
                    stringBuffer.append("\n");
                    stringBuffer.append("备注：");
                    stringBuffer.append(sjwlxxDto.getSdbz());
                }
                SjpdglDto high = new SjpdglDto();
                high.setSjpdid(sjwlxxDto.getSjpdid());
                high.setLsjl(stringBuffer.toString() + "\n");
                sjpdglService.update(high);

                SjtdxxDto sjtdxxDto = new SjtdxxDto();
                String sjtdid = StringUtil.generateUUID();
                sjtdxxDto.setSjtdid(sjtdid);
                //先新增一条新的物流信息
                sjwlxxDto.setSjwlid(sjwlxxDto.getWlid());
                sjwlxxDto.setWlid(StringUtil.generateUUID());
                sjwlxxDto.setWlzt("10");
                sjwlxxDto.setSdbz(null);
                if (StringUtil.isNotBlank(sjwlxxDto.getTdxx_str())) {
                    sjwlxxDto.setSjtdid(sjtdid);
                }
                sjwlxxDto.setLrry(user.getYhid());
                int insert = dao.insert(sjwlxxDto);
                if (insert <= 0) {
                    return false;
                }
                SjpdglDto sjpdglDto = new SjpdglDto();
                List<String> tdwlids = new ArrayList<>();
                //新增团单信息
                if (StringUtil.isNotBlank(sjwlxxDto.getTdxx_str())) {
                    sjpdglDto.setTdbj("td");
                    List<SjwlxxDto> sjwlxxDtos = JSONObject.parseArray(sjwlxxDto.getTdxx_str(), SjwlxxDto.class);
                    sjtdxxDto.setJsfs(sjwlxxDto.getJsfs());
                    sjtdxxDto.setGldh(sjwlxxDto.getGldh());
                    sjtdxxDto.setBc(sjwlxxDto.getBc());
                    sjtdxxDto.setDddd(sjwlxxDto.getDddd());
                    sjtdxxDto.setYjddsj(sjwlxxDto.getYjddsj());
                    sjtdxxDto.setSdsj(sjwlxxDto.getSdsj());
                    sjtdxxDto.setQylxr(sjwlxxDto.getQylxr());
                    sjtdxxDto.setQylxdh(sjwlxxDto.getQylxdh());
                    sjtdxxDto.setJcdw(sjwlxxDto.getJcdw());
                    sjtdxxDto.setWlzt("10");
                    sjtdxxDto.setSdbz(sjwlxxDto.getSdbz());
                    sjtdxxDto.setJdr(sjwlxxDtos.get(0).getJdr());
                    sjtdxxDto.setLrry(sjwlxxDto.getLrry());
                    boolean three = sjtdxxService.insert(sjtdxxDto);
                    if (!three) {
                        return false;
                    }

                    for (SjwlxxDto dto : sjwlxxDtos) {
                        //保存附件信息
                        if (sjwlxxDto.getFjids() != null && sjwlxxDto.getFjids().size() > 0) {
                            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                            String fjids = StringUtils.join(sjwlxxDto.getFjids(), ",");
                            paramMap.add("fjids", fjids);
                            paramMap.add("ywid", dto.getWlid());
                            RestTemplate restTemplate = new RestTemplate();
                            String param = restTemplate.postForObject(menuurl + "/wechat/getFileAddress", paramMap, String.class);
                            if (param != null) {
                                JSONArray parseArray = JSONObject.parseArray(param);
                                for (int i = 0; i < parseArray.size(); i++) {
                                    FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
                                    fjcfbModel.setYwid(dto.getWlid());
                                    fjcfbModel.setFjid(StringUtil.generateUUID());
                                    // 下载服务器文件到指定文件夹
                                    commonService.downloadFile(fjcfbModel);
                                    fjcfbService.insert(fjcfbModel);
                                }
                            }
                        }
                        //将原先的物流状态改为40
                        sjwlxxDto_t.setWlid(dto.getWlid());
                        int one = dao.update(sjwlxxDto_t);
                        if (one <= 0) {
                            return false;
                        }
                        high.setSjpdid(dto.getSjpdid());
                        sjpdglService.update(high);
                        //先新增一条新的物流信息
                        sjwlxxDto.setSjwlid(dto.getWlid());
                        sjwlxxDto.setSjpdid(dto.getSjpdid());
                        tdwlids.add(sjwlxxDto.getWlid());
                        sjwlxxDto.setWlid(StringUtil.generateUUID());
                        int two = dao.insert(sjwlxxDto);
                        if (two <= 0) {
                            return false;
                        }
                    }
                }
                //新增物流通知人员
                if (StringUtil.isNotBlank(sjwlxxDto.getTzry_str())) {
                    List<SjwltzDto> list = new ArrayList<>();
                    String[] split = sjwlxxDto.getTzry_str().split(",");
                    if ("td".equals(sjpdglDto.getTdbj())){
                        for (String tdwlid : tdwlids) {
                            for (String s : split) {
                                SjwltzDto sjwltzDto = new SjwltzDto();
                                sjwltzDto.setYwid(tdwlid);
                                sjwltzDto.setYwlx(LogisticDataTypeEnum.NOTIFY_ARRIVE.getCode());
                                sjwltzDto.setRyid(s);
                                sjwltzDto.setXsmc(sjwlxxDto.getXsmc());
                                list.add(sjwltzDto);
                            }
                        }
                        for (String s : split) {
                            SjwltzDto sjwltzDto = new SjwltzDto();
                            sjwltzDto.setYwid(sjtdid);
                            sjwltzDto.setYwlx(LogisticDataTypeEnum.NOTIFY_ARRIVE.getCode());
                            sjwltzDto.setRyid(s);
                            sjwltzDto.setXsmc(sjwlxxDto.getXsmc());
                            list.add(sjwltzDto);
                        }
                    }else {
                        for (String s : split) {
                            SjwltzDto sjwltzDto = new SjwltzDto();
                            sjwltzDto.setYwid(sjwlxxDto.getWlid());
                            sjwltzDto.setYwlx(LogisticDataTypeEnum.NOTIFY_ARRIVE.getCode());
                            sjwltzDto.setRyid(s);
                            sjwltzDto.setXsmc(sjwlxxDto.getXsmc());
                            list.add(sjwltzDto);
                        }
                    }
                    boolean isSuccess = sjwltzService.insertList(list);
                    if (!isSuccess) {
                        return false;
                    }
                    List<SjwltzDto> userlist = sjwltzService.getYhinfo(list);
                    SjwlxxDto dto = dao.getDto(sjwlxxDto);
                    sjpdglDto.setTzryList(userlist);
                    sjpdglDto.setWlid(sjwlxxDto.getWlid());
                    sjpdglDto.setQylxr(dto.getJdrmc());
                    sjpdglDto.setQylxdh("无");
                    sjpdglDto.setQydz(dto.getDddd());
                    sjpdglDto.setYjsj(sjwlxxDto.getYjddsj());
                    sjpdglDto.setBblxmc(dto.getBblxmc());
                    sjpdglDto.setJcdwmc(dto.getJcdwmc());
                    sjpdglDto.setJsfsmc(sjwlxxDto.getJsfsmc());
                    sjpdglDto.setPdbz(sjwlxxDto_t.getSdbz());
                    sjpdglDto.setHzxm(sjwlxxDto_t.getHzxm());
                    sjpdglService.notificateTzry(sjpdglDto);
                }
            } else {//不是高铁的处理
                //将原先的物流状态改为40
                SjwlxxDto sjwlxxDto_t = new SjwlxxDto();
                sjwlxxDto_t.setWlid(sjwlxxDto.getWlid());
                sjwlxxDto_t.setWlzt("40");
                Date date = new Date();
                SimpleDateFormat df_24 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sjwlxxDto_t.setSdsj(df_24.format(date));
                sjwlxxDto_t.setSdbz(sjwlxxDto.getSdbz());
                sjwlxxDto_t.setXgry(user.getYhid());
                sjwlxxDto_t.setYgqjsj(sjwlxxDto.getYgqjsj());
                sjwlxxDto_t.setQjsj(sjwlxxDto.getQjsj());
                sjwlxxDto_t.setYjsdsj(sjwlxxDto.getYjsdsj());
                int update = dao.update(sjwlxxDto_t);
                if (update <= 0) {
                    return false;
                }
                //更改派单的状态
                SjpdglDto sjpdglDto = new SjpdglDto();
                sjpdglDto.setSjpdid(sjwlxxDto.getSjpdid());
                sjpdglDto.setZt("50");
                sjpdglDto.setXgry(user.getYhid());
                if (!"qt".equals(sjwlxxDto.getJsfsdm())) {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
                    stringBuffer.append("\n");
                    stringBuffer.append("您的快递已送达至");
                    stringBuffer.append(sjwlxxDto.getJcdwmc());
                    if(StringUtil.isNotBlank(sjwlxxDto.getSdbz())){
                        stringBuffer.append("\n");
                        stringBuffer.append("备注：");
                        stringBuffer.append(sjwlxxDto.getSdbz());
                    }
                    sjpdglDto.setLsjl(stringBuffer.toString() + "\n");
                }
                //取样员和自送更新运输时长
                if (("QYY".equals(sjwlxxDto.getJsfsdm())||"ZS".equals(sjwlxxDto.getJsfsdm()))&&StringUtil.isNotBlank(dtoById.getQjsj())){
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
                    Date qjsjdate = DateUtils.parseDate(dtoById.getQjsj());
                    SimpleDateFormat df_qjsj = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    String qjsjstr = df_qjsj.format(qjsjdate);
                    //日期格式要对应 不然会报错
                    LocalDateTime qjsj = LocalDateTime.parse(qjsjstr, df);
                    LocalDateTime now = LocalDateTime.now();
                    long hmrqc = ChronoUnit.MILLIS.between(qjsj, now);
                    sjpdglDto.setYssc(String.valueOf(hmrqc));
                }
                boolean flag = sjpdglService.update(sjpdglDto);
                return flag;
            }
        }
        return true;
    }

    /**
     * 根据wlid获取送检物流信息
     * @param sjwlxxDto
     * @return
     */
    public SjwlxxDto getSjwlxxDtoById(SjwlxxDto sjwlxxDto){
        return dao.getSjwlxxDtoById(sjwlxxDto);
    }

    /**
     * 取件保存
     * @param sjwlxxDto
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean pickUpSaveInfo(SjwlxxDto sjwlxxDto,User user) throws RuntimeException {
        boolean isSuccess= StringUtil.isNotBlank(sjwlxxDto.getTzbj());
        //保存附件信息
        if (StringUtil.isNotBlank(sjwlxxDto.getSjtdid())) {
            if (sjwlxxDto.getFjids() != null && sjwlxxDto.getFjids().size() > 0) {
                MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                String fjids = StringUtils.join(sjwlxxDto.getFjids(), ",");
                paramMap.add("fjids", fjids);
                paramMap.add("ywid", sjwlxxDto.getWlid());
                RestTemplate restTemplate = new RestTemplate();
                String param = restTemplate.postForObject(menuurl + "/wechat/getFileAddress", paramMap, String.class);
                if (param != null) {
                    JSONArray parseArray = JSONObject.parseArray(param);
                    for (int i = 0; i < parseArray.size(); i++) {
                        FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
                        fjcfbModel.setYwid(sjwlxxDto.getWlid());
                        fjcfbModel.setFjid(StringUtil.generateUUID());
                        // 下载服务器文件到指定文件夹
                        commonService.downloadFile(fjcfbModel);
                        isSuccess = fjcfbService.insert(fjcfbModel);
                    }
                }
            }
        }
        else {
            if (sjwlxxDto.getFjids() != null && sjwlxxDto.getFjids().size() > 0) {
                if (!("qt").equals(sjwlxxDto.getJsfsdm())) {
                    MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                    String fjids = StringUtils.join(sjwlxxDto.getFjids(), ",");
                    paramMap.add("fjids", fjids);
                    paramMap.add("ywid", sjwlxxDto.getWlid());
                    RestTemplate restTemplate = new RestTemplate();
                    String param = restTemplate.postForObject(menuurl + "/wechat/getFileAddress", paramMap, String.class);
                    if (param != null) {
                        JSONArray parseArray = JSONObject.parseArray(param);
                        for (int i = 0; i < parseArray.size(); i++) {
                            FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
                            fjcfbModel.setYwid(sjwlxxDto.getWlid());
                            // 下载服务器文件到指定文件夹
                            commonService.downloadFile(fjcfbModel);
                            isSuccess = fjcfbService.insert(fjcfbModel);
                        }
                    }
                }
            }
        }
        if (isSuccess) {
            //更新送检派单管理表数据
            SjpdglDto sjpdglDto = new SjpdglDto();
            sjpdglDto.setXgry(sjwlxxDto.getXgry());
            sjpdglDto.setSjpdid(sjwlxxDto.getSjpdid());
            if(StringUtil.isNotBlank(sjwlxxDto.getSjpdzt())){
                sjpdglDto.setZt(sjwlxxDto.getSjpdzt());
            }
            if (StringUtil.isBlank(sjwlxxDto.getSjtdid())) {
                sjpdglDto.setBbbh(sjwlxxDto.getBbbh());
                List<SjpdglDto> bbbhRepeat = sjpdglService.isBbbhRepeat(sjpdglDto);
                if (bbbhRepeat.size() > 0) {
                    throw new RuntimeException("标本编号已存在，请重新输入！");
                }
            }
                StringBuffer stringBuffer=new StringBuffer();
                stringBuffer.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                stringBuffer.append("\n");
                stringBuffer.append("您的快递已由");
                stringBuffer.append(sjwlxxDto.getLrrymc());
                stringBuffer.append("在");
                stringBuffer.append(sjwlxxDto.getQydz());
                stringBuffer.append("取件");
//                JcsjDto jcsjDto=redisUtil.hgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SENDING_METHOD.getCode(),sjwlxxDto.getJsfs());//寄送方式并为快递类型
                JcsjDto jcsjDto=redisUtil.hgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE.getCode(),sjwlxxDto.getJsfs());//查询寄送方式的详细数据
                if("SF".equals(jcsjDto.getCsdm())||"JD".equals(jcsjDto.getCsdm())||"TC".equals(jcsjDto.getCsdm())){
                    stringBuffer.append(",并且已交付");
                    stringBuffer.append(jcsjDto.getCsmc());
                    if(StringUtil.isNotBlank(sjwlxxDto.getGldh())){
                        stringBuffer.append("，关联单号为:");
                        stringBuffer.append(sjwlxxDto.getGldh());
                    }
                }
            if("ZS".equals(jcsjDto.getCsdm())){
                stringBuffer.append(",并由");
                stringBuffer.append(sjwlxxDto.getJdrmc());
                stringBuffer.append("派送");
                if(StringUtil.isNotBlank(sjwlxxDto.getGldh())){
                    stringBuffer.append("，关联单号为:");
                    stringBuffer.append(sjwlxxDto.getGldh());
                }
            }
            if(sjwlxxDto.getBbbh()!=null&&sjwlxxDto.getBbbh()!=""){
                stringBuffer.append(",标本编号为");
                stringBuffer.append(sjwlxxDto.getBbbh());
            }
            if(sjwlxxDto.getYjsdsj()!=null&&sjwlxxDto.getYjsdsj()!=""){
                stringBuffer.append(",预计送达时间为");
                stringBuffer.append(sjwlxxDto.getYjsdsj());
            }
            if(StringUtil.isNotBlank(sjwlxxDto.getQjbz())){
                stringBuffer.append("\n");
                stringBuffer.append("备注：");
                stringBuffer.append(sjwlxxDto.getQjbz());
            }
            sjpdglDto.setLsjl(stringBuffer.toString()+"\n");
            sjpdglDto.setYjddsj(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
            isSuccess = sjpdglService.update(sjpdglDto);
        }
        if (isSuccess) {
            //更新送检物流信息表数据
            sjwlxxDto.setQjbj("1");
			sjwlxxDto.setQjsj(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            isSuccess = update(sjwlxxDto);
            if(StringUtil.isNotBlank(sjwlxxDto.getJsfs())){
//                List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SENDING_METHOD.getCode());//寄送方式并为快递类型
                List<JcsjDto> list = redisUtil.hmgetDto("matridx_jcsj:"+ BasicDataTypeEnum.SD_TYPE.getCode());
                String jsfsmc = "";
                for (JcsjDto jcsjDto : list) {
                    if (jcsjDto.getCsid().equals(sjwlxxDto.getJsfs())){
                        jsfsmc = jcsjDto.getCsmc();
                    }
                }
                sjkdxxService.insertSjkdxxInfo(sjwlxxDto.getWlid(),sjwlxxDto.getGldh(),jsfsmc,null, EntityTypeEnum.ENTITY_SJWLXXDTO.getCode());
            }
        }
        //发送取件通知给派单人
        if(isSuccess&&(StringUtil.isBlank(sjwlxxDto.getTzbj()))&&StringUtil.isBlank(sjwlxxDto.getSjtdid())){
            //获取送检派单信息
            SjwlxxDto sjwlxxDto_t = getSjwlxxDtoById(sjwlxxDto);
            //消息替换map
            Map<String, Object> msgMap = new HashMap<>();
            msgMap.put("jdrmc", user.getZsxm());
            msgMap.put("sjwlxxDto", sjwlxxDto_t);
            String msgContent = xxglService.getReplaceMsg("ICOMM_QJ00002",msgMap);
            String jsfs = sjwlxxDto.getJsfs();
//            JcsjDto jsfsDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.SENDING_METHOD.getCode(), jsfs);//寄送方式并为快递类型
            JcsjDto jsfsDto = redisUtil.hgetDto("matridx_jcsj:" + BasicDataTypeEnum.SD_TYPE.getCode(), jsfs);
            String jsfscsdm = jsfsDto.getCsdm();
            if("SF".equals(jsfscsdm)||"JD".equals(jsfscsdm)||"TC".equals(jsfscsdm)){
                msgContent += "并由"+jsfsDto.getCsmc()+"配送！";
            }
                //发送取消接单的消息通知到派单人
            String internalbtn = null;
            try {
                internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/logistics/my/viewlogistics/viewlogistics&wbfw=1&sjpdid=" + sjwlxxDto_t.getSjpdid(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage());
            }
            List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                btnJsonList.setTitle("小程序");
                btnJsonList.setActionUrl(internalbtn);
                btnJsonLists.add(btnJsonList);
                //小程序访问
                talkUtil.sendCardMessage(sjwlxxDto_t.getYhm(), sjwlxxDto_t.getPdrddid(), xxglService.getMsg("ICOMM_QJ00001"), msgContent, btnJsonLists, "1");

        }
        return isSuccess;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean cancelReceipt(SjwlxxDto sjwlxxDto,String sffs) {
        if (StringUtil.isNotBlank(sjwlxxDto.getSjtdid())) {
            SjpdglDto sjpdglDto=new SjpdglDto();
            sjpdglDto.setSjpdid(sjwlxxDto.getSjpdid());
            Date date=new Date();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            StringBuffer stringBuffer = new StringBuffer();//添加历史记录
            String str=sjwlxxDto.getJdrmc()+"已取消接单";
            stringBuffer.append(simpleDateFormat.format(date));//添加派单信息和时间
            stringBuffer.append("\n");
            stringBuffer.append(str);
            stringBuffer.append("\n");
            stringBuffer.append("取消接单原因：");
            stringBuffer.append(sjwlxxDto.getQxjdyy());
            sjpdglDto.setLsjl(stringBuffer.toString()+"\n");
            sjpdglService.update(sjpdglDto);
            //保存附件信息
            if (sjwlxxDto.getFjids() != null && sjwlxxDto.getFjids().size() > 0) {
                MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                String fjids = StringUtils.join(sjwlxxDto.getFjids(), ",");
                paramMap.add("fjids", fjids);
                paramMap.add("ywid", sjwlxxDto.getWlid());
                RestTemplate restTemplate = new RestTemplate();
                String param = restTemplate.postForObject(menuurl + "/wechat/getFileAddress", paramMap, String.class);
                if (param != null) {
                    JSONArray parseArray = JSONObject.parseArray(param);
                    for (int i = 0; i < parseArray.size(); i++) {
                        FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
                        fjcfbModel.setYwid(sjwlxxDto.getWlid());
                        fjcfbModel.setFjid(StringUtil.generateUUID());
                        // 下载服务器文件到指定文件夹
                        commonService.downloadFile(fjcfbModel);
                        fjcfbService.insert(fjcfbModel);
                    }
                }
            }
        }
        //保存附件信息
        else {
            if ("yes".equals(sffs)) {
                if (sjwlxxDto.getFjids() != null && sjwlxxDto.getFjids().size() > 0) {
                    MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
                    String fjids = StringUtils.join(sjwlxxDto.getFjids(), ",");
                    paramMap.add("fjids", fjids);
                    paramMap.add("ywid", sjwlxxDto.getWlid());
                    RestTemplate restTemplate = new RestTemplate();
                    String param = restTemplate.postForObject(menuurl + "/wechat/getFileAddress", paramMap, String.class);
                    if (param != null) {
                        JSONArray parseArray = JSONObject.parseArray(param);
                        for (int i = 0; i < parseArray.size(); i++) {
                            FjcfbModel fjcfbModel = JSONObject.toJavaObject((JSONObject) parseArray.get(i), FjcfbModel.class);
                            fjcfbModel.setYwid(sjwlxxDto.getWlid());
                            // 下载服务器文件到指定文件夹
                            commonService.downloadFile(fjcfbModel);
                            fjcfbService.insert(fjcfbModel);
                        }
                    }
                }
            }
        }
        sjwlxxDto.setJdbj("0");
        sjwlxxDto.setWlzt("10");
        int result=dao.update(sjwlxxDto);
        if(result>0){
            //根据主键ID从redis中获取数据
            Object get = redisUtil.get("SJWLXX:"+sjwlxxDto.getWlid());
            log.error("wlid为"+sjwlxxDto.getWlid());
            SjwlxxDto sjwlxxDto1;
            if(get==null){//如果未从redis取到,则从数据库查询，并重新放入redis
                log.error("redis为空");
                sjwlxxDto1=dao.getSjwlxxDtoById(sjwlxxDto);
            }else{
                log.error("redis不为空");
                sjwlxxDto1 = JSON.parseObject(get.toString(), SjwlxxDto.class);
                if(StringUtil.isBlank(sjwlxxDto1.getSjpdid()))
                    sjwlxxDto1=dao.getSjwlxxDtoById(sjwlxxDto);
            }
            //将redis种jdbj置为0
            sjwlxxDto1.setJdbj("0");
            sjwlxxDto1.setWlzt("10");
            redisUtil.set("SJWLXX:"+sjwlxxDto.getWlid(), JSON.toJSONString(sjwlxxDto1),288000);
            //重新放回死信
            Map<String,Object> parameters = new HashMap<>();
            SjpdglDto sjpdglDto=new SjpdglDto();
            sjpdglDto.setWlid(sjwlxxDto1.getWlid());
            sjpdglDto.setSjpdid(sjwlxxDto1.getSjpdid());
            sjpdglDto.setWlzt("10");
            sjpdglDto.setZt("10");
            sjpdglDto.setJdbj("0");
            List<SjwltzDto> userlist = sjwltzService.getDtoListBySjpdid(sjwlxxDto1.getWlid());
            if (StringUtil.isBlank(sjwlxxDto.getSjtdid())) {
                Date date=new Date();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                StringBuffer stringBuffer = new StringBuffer();//添加历史记录
                String str=sjwlxxDto1.getJdrmc()+"已取消接单";
                stringBuffer.append(simpleDateFormat.format(date));//添加派单信息和时间
                stringBuffer.append("\n");
                stringBuffer.append(str);
                stringBuffer.append("\n");
                stringBuffer.append("取消接单原因：");
                stringBuffer.append(sjwlxxDto.getQxjdyy());
                sjpdglDto.setLsjl(stringBuffer.toString()+"\n");
                sjpdglService.update(sjpdglDto);
//                parameters.put("userList", userlist);//通知人员
//                parameters.put("sjpdglDto", sjpdglDto);

                //派单后添加到延时队列，使用Map不使用Dto，避免携带无用信息
                List<Map<String,String>> userMapList = new ArrayList<>();
                for ( SjwltzDto sjwltzDto : userlist){
                    Map<String,String> usermap = new HashMap<>();
                    usermap.put("ddid",sjwltzDto.getDdid());
                    usermap.put("ryid",sjwltzDto.getRyid());
                    usermap.put("rymc",sjwltzDto.getRymc());
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
//                message.getMessageProperties().setExpiration("20000");
                        message.getMessageProperties().setExpiration("120000");
                        return message;
                    }
                });
                //将超时提醒至为1
                WlcstxDto wlcstxDto = new WlcstxDto();
                wlcstxDto.setYwid(sjpdglDto.getWlid());
                wlcstxDto.setCscs("1");
                wlcstxService.insertOrUpdateWlcstx(wlcstxDto);
                //发送取消接单的消息通知到派单人
                String internalbtn = null;
                try {
                    internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/logistics/my/my&wbfw=1", "utf-8");
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage());
                }
                List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                btnJsonList.setTitle("小程序");
                btnJsonList.setActionUrl(internalbtn);
                btnJsonLists.add(btnJsonList);
                String yhm = null;
                if(StringUtil.isNotBlank(sjwlxxDto1.getLrry())){
                   User user = new User();
                   user.setYhid(sjwlxxDto1.getLrry());
                   User userInfoById = commonDao.getUserInfoById(user);
                   if (userInfoById!=null){
                       yhm = userInfoById.getYhm();
                   }
                }
                talkUtil.sendCardMessage(yhm, sjwlxxDto1.getPdrddid(), xxglService.getMsg("ICOMM_WLTZ00003"), StringUtil.replaceMsg(xxglService.getMsg("ICOMM_WLTZ00004"),
                        sjwlxxDto.getJdrmc(), sjwlxxDto.getQxjdyy(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
                sjpdglDto.setJsfsmc(sjpdglDto.getPdjsfsmc());
                sjpdglDto=sjpdglService.getDtoById(sjpdglDto.getSjpdid());
                sjpdglDto.setTzryList(userlist);
                sjpdglDto.setWlid(sjwlxxDto.getWlid());
                sjpdglService.notificateTzry(sjpdglDto);
                dao.updateJdrAndJdsj(sjwlxxDto.getWlid());
            }
            else if (StringUtil.isNotBlank(sjwlxxDto.getSjtdid())&&"yes".equals(sffs)){
                sjpdglDto=sjpdglService.getDto(sjpdglDto);
//                parameters.put("userList", userlist);//通知人员
//                parameters.put("sjpdglDto", sjpdglDto);

                //派单后添加到延时队列，使用Map不使用Dto，避免携带无用信息
                List<Map<String,String>> userMapList = new ArrayList<>();
                for ( SjwltzDto sjwltzDto : userlist){
                    Map<String,String> usermap = new HashMap<>();
                    usermap.put("ddid",sjwltzDto.getDdid());
                    usermap.put("ryid",sjwltzDto.getRyid());
                    usermap.put("rymc",sjwltzDto.getRymc());
                    usermap.put("yhm",sjwltzDto.getRymc());
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
//                message.getMessageProperties().setExpiration("20000");
                        message.getMessageProperties().setExpiration("120000");
                        return message;
                    }
                });
                //将超时提醒至为1
                WlcstxDto wlcstxDto = new WlcstxDto();
                wlcstxDto.setYwid(sjpdglDto.getWlid());
                wlcstxDto.setCscs("1");
                wlcstxService.insertOrUpdateWlcstx(wlcstxDto);
                //发送取消接单的消息通知到派单人
                String internalbtn = null;
                try {
                    internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/logistics/my/my&wbfw=1", "utf-8");
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage());
                }
                List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                btnJsonList.setTitle("小程序");
                btnJsonList.setActionUrl(internalbtn);
                btnJsonLists.add(btnJsonList);
                talkUtil.sendCardMessage(sjwlxxDto1.getYhm(), sjwlxxDto1.getPdrddid(), xxglService.getMsg("ICOMM_WLTZ00003"), StringUtil.replaceMsg(xxglService.getMsg("ICOMM_WLTZ00004"),
                        sjwlxxDto.getJdrmc(), sjwlxxDto.getQxjdyy(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
                //重新发送派单通知
                sjpdglDto.setJsfsmc(sjpdglDto.getPdjsfsmc());
                sjpdglDto=sjpdglService.getDtoById(sjpdglDto.getSjpdid());
                sjpdglDto.setTdbj("td");
                userlist = sjwltzService.getDtoListBySjpdid(sjwlxxDto1.getWlid());
                sjpdglDto.setTzryList(userlist);
                sjpdglDto.setWlid(sjwlxxDto.getWlid());
                sjpdglService.notificateTzry(sjpdglDto);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<SjwlxxDto> getWlxxList(SjwlxxDto sjwlxxDto) {
        return dao.getWlxxList(sjwlxxDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void orderLrSave(SjwlxxDto sjwlxxDto,SjpdglDto sjpdglDto,String sffs){
        sjwlxxDto.setWlzt("20");
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(new SimpleDateFormat("YYYY-MM-dd HH:mm:ss").format(new Date()));
        stringBuffer.append("\n");
        if (StringUtil.isNotBlank(sjwlxxDto.getSjwlid())){
            stringBuffer.append("订单转交给");
            stringBuffer.append(sjwlxxDto.getJdrmc());
        }else{
            stringBuffer.append("您的快递已由");
            stringBuffer.append(sjwlxxDto.getJdrmc());
            stringBuffer.append("接单");
            stringBuffer.append("\n");
            stringBuffer.append("预计取件时间为");
            stringBuffer.append(sjwlxxDto.getYgqjsj());
        }
        if(StringUtil.isNotBlank(sjwlxxDto.getJdbz())){
            stringBuffer.append("\n");
            stringBuffer.append("接单备注：");
            stringBuffer.append(sjwlxxDto.getJdbz());
        }
        dao.update(sjwlxxDto);
        User user_t=new User();
        user_t.setYhid(sjpdglDto.getLrry());
        user_t=commonService.getUserInfoById(user_t);
        if (StringUtil.isNotBlank(sjwlxxDto.getSjtdid())) {
            SjpdglDto sjpdglDto_t=new SjpdglDto();
            sjpdglDto_t.setSjpdid(sjpdglDto.getSjpdid());
            sjpdglDto_t.setLsjl(stringBuffer.toString()+"\n");
            sjpdglService.update(sjpdglDto_t);
            if ("yes".equals(sffs)) {
                sjpdglDto_t.setLsjl(stringBuffer.toString() + "\n");
                sjpdglDto_t.setSjpdid(sjpdglDto.getSjpdid());
                sjpdglDto_t.setXgry(sjwlxxDto.getXgry());
                if (StringUtil.isBlank(sjwlxxDto.getTzbj())) {
                    //小程序访问
                    String internalbtn = null;
                    try {
                        internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/logistics/my/my&wbfw=1", "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        log.error(e.getMessage());
                    }
                    List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                    OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                    btnJsonList.setTitle("小程序");
                    btnJsonList.setActionUrl(internalbtn);
                    btnJsonLists.add(btnJsonList);
                    if(user_t!=null){
                        talkUtil.sendCardMessage(user_t.getYhm(), sjpdglDto.getPdrddid(), xxglService.getMsg("ICOMM_WL00003"), StringUtil.replaceMsg(xxglService.getMsg("ICOMM_WL00004"),
                                sjpdglDto.getBblxmc(), sjpdglDto.getQydz(), sjwlxxDto.getJdrmc(), sjwlxxDto.getYgqjsj()), btnJsonLists, "1");
                    }
                }
            }
        }
        else {
            SjpdglDto sjpdglDto_t = new SjpdglDto();
            sjpdglDto_t.setLsjl(stringBuffer.toString() + "\n");
            sjpdglDto_t.setSjpdid(sjpdglDto.getSjpdid());
            sjpdglDto_t.setXgry(sjwlxxDto.getXgry());
            if (StringUtil.isBlank(sjwlxxDto.getSjwlid())) {
                sjpdglDto_t.setZt("20");
            }
            sjpdglDto_t.setYgqjsj(sjpdglDto.getYgqjsj());
            sjpdglDto_t.setJdbj("1");
            sjpdglService.update(sjpdglDto_t);
            if (StringUtil.isBlank(sjwlxxDto.getTzbj())) {
//                String token = talkUtil.getToken();
                //小程序访问
//                DBEncrypt p = new DBEncrypt();
//                String dingtalkurl = p.dCode(jumpdingtalkurl);
                //内网访问
                String internalbtn = null;
                try {
                    internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/logistics/my/my&wbfw=1", "utf-8");
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage());
                }
                List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                btnJsonList.setTitle("小程序");
                btnJsonList.setActionUrl(internalbtn);
                btnJsonLists.add(btnJsonList);
                if(user_t!=null){
                    talkUtil.sendCardMessage(user_t.getYhm(), sjpdglDto.getPdrddid(), xxglService.getMsg("ICOMM_WL00003"), StringUtil.replaceMsg(xxglService.getMsg("ICOMM_WL00004"),
                            sjpdglDto.getBblxmc(), sjpdglDto.getQydz(), sjwlxxDto.getJdrmc(), sjwlxxDto.getYgqjsj()), btnJsonLists, "1");
                }
            }
        }
    }


    /**
     * 获取派单信息
     * @return
     */
    public Map<String,Object> generateWlxx(SjwlxxDto sjwlxxDto){
        List<SjwlxxDto> list=dao.getWlxxList(sjwlxxDto);
        List<Map<String,Object>> mapList=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        for (int i=0;i<list.size();i++)
        {
            if (!list.get(i).getWlzt().equals("00")){
                if (StringUtil.isBlank(list.get(i).getSjwlid())){
                Map<String,Object> map1=new HashMap<>();
                String str="您的订单派单成功!派单人:"+list.get(i).getPdrmc();
                if (StringUtil.isNotBlank(list.get(i).getJsfsmc())) {
                    str += ",寄送方式:" + list.get(i).getJsfsmc();
                }
                    if (StringUtil.isNotBlank(list.get(i).getGldh()))
                        str+=",关联单号:"+list.get(i).getGldh();
                map1.put("str",str);
                map1.put("str4",str);
                map1.put("str2",list.get(i).getLrsj());
                map1.put("str3",list.get(i).getLrtime());
                map1.put("bz",list.get(i).getPdbz());
                map1.put("ddid",list.get(i).getPdrddid());
                FjcfbDto fjcfbDto=new FjcfbDto();
                fjcfbDto.setYwid(list.get(i).getSjpdid());
                fjcfbDto.setYwlx(BusTypeEnum.IMP_LOGISTICS_DISPATCH_IMG.getCode());
                List<FjcfbDto> fjlist=fjcfbService.getDtoList(fjcfbDto);
                if (fjlist.size()>0){
                    map1.put("fjlist",fjlist);
                }
                else{
                    map1.put("fjlist",null);
                }
                mapList.add(map1);
                }
            }
            if (list.get(i).getScbj().equals("2")){
                Map<String,Object> map1=new HashMap<>();
                String str="订单已取消!取消人员:"+list.get(i).getQxrymc();
                map1.put("str",str);
                map1.put("str4",str);
                map1.put("str2",list.get(i).getScsj());
                map1.put("str3",list.get(i).getSctime());
                map1.put("bz",list.get(i).getQxpdyy());
                map1.put("fjlist",null);
                mapList.add(map1);
            }
            if (StringUtil.isNotBlank(list.get(i).getJdsj()) && list.get(i).getScbj().equals("0"))
            {    Map<String,Object> map1=new HashMap<>();
                String str;
                if (StringUtil.isNotBlank(list.get(i).getSjwlid())){
                    str="订单转交给"+list.get(i).getJdrmc();
                }else{
                    str="您的快递已由"+list.get(i).getJdrmc()+"接单,"+"预计取件时间为"+list.get(i).getYgqjsj();
                }
                map1.put("str",str);
                map1.put("str4",str);
                map1.put("str2",list.get(i).getJdsj());
                map1.put("str3",list.get(i).getJdtime());
                map1.put("ddid",list.get(i).getJdrddid());
                map1.put("bz",list.get(i).getJdbz());
                map1.put("fjlist",null);
                mapList.add(map1);
            }
            if (StringUtil.isNotBlank(list.get(i).getQjsj())){
                Map<String,Object> map1=new HashMap<>();
                String str="您的快递已由"+list.get(i).getJdrmc()+"在"+list.get(i).getQydz()+"取件";
                if(StringUtil.isNotBlank(list.get(i).getSjwlid()))
                    str="您的快递已由"+list.get(i).getJdrmc()+"在"+list.get(i).getDddd()+"取件";
                if("SF".equals(list.get(i).getJsfsdm())||"JD".equals(list.get(i).getJsfsdm())||"TC".equals(list.get(i).getJsfsdm())||"ZS".equals(list.get(i).getJsfsdm())){
                    if ("SF".equals(list.get(i).getJsfsdm())||"JD".equals(list.get(i).getJsfsdm())||"TC".equals(list.get(i).getJsfsdm())) {
                        str = str + ",并且已交付" + list.get(i).getJsfsmc();
                    }else{
                        str=str+",并由"+list.get(i).getJdrmc()+"派送";
                    }
                }
                String string = str;
                if(StringUtil.isNotBlank(list.get(i).getGldh())){
                    str=str+",关联单号为:"+list.get(i).getGldh();
                    if (list.get(i).getGldh().contains("JD") || list.get(i).getGldh().contains("SF")){
                        string=string+",关联单号为:<a href='javascript:void(0);' onclick='showInfo(\""+list.get(i).getGldh()+"\")'>"+list.get(i).getGldh()+"</a>";
                    }else {
                        string=string+",关联单号为:"+list.get(i).getGldh();
                    }
                }
                if(list.get(i).getBbbh()!=null&&list.get(i).getBbbh()!=""){
                    str=str+",标本编号为"+list.get(i).getBbbh();
                    string=string+",标本编号为"+list.get(i).getBbbh();
                }
                if(list.get(i).getYjsdsj()!=null&&list.get(i).getYjsdsj()!=""){
                    str=str+",预计送达时间为"+list.get(i).getYjsdsj();
                    string=string+",预计送达时间为"+list.get(i).getYjsdsj();
                }
                //获取取件附件信息
                FjcfbDto fjcfbDto=new FjcfbDto();
                fjcfbDto.setYwid(list.get(i).getWlid());
                fjcfbDto.setYwlx(BusTypeEnum.IMP_LOGISTICS_PICKUP_IMG.getCode());
                List<FjcfbDto> fjlist=fjcfbService.getDtoList(fjcfbDto);
                map1.put("str",str);
                map1.put("str4",string);
                map1.put("str2",list.get(i).getQjsj());
                map1.put("str3",list.get(i).getQjtime());
                map1.put("bz",list.get(i).getQjbz());
                map1.put("fjlist",fjlist);
                map1.put("pathList","");
                mapList.add(map1);
            }
            if (StringUtil.isNotBlank(list.get(i).getSdsj())) {
                if (!(list.get(i).getJsfsdm().equals("SF"))&&!(list.get(i).getJsfsdm().equals("JD"))&&!(list.get(i).getJsfsdm().equals("TC"))&&!(list.get(i).getJsfsdm().equals("ZS"))) {
                    Map<String, Object> map1 = new HashMap<>();
                    String str = "";
               if ("GT".equals(list.get(i).getJsfsdm())) {//若为高铁，后面还会产生一条新的物流信息，并保存了班次信息
                        if (list.get(i + 1).getYjddsj() == null || list.get(i + 1).getYjddsj() == "") {
                            str = "您的快递已送达至高铁站,班次为" + list.get(i + 1).getBc() + ",目的地为" + list.get(i + 1).getDddd();
                        } else {
                            str = "您的快递已送达至高铁站,班次为" + list.get(i + 1).getBc() + ",目的地为" + list.get(i + 1).getDddd() + ",预计" + list.get(i + 1).getYjddsj() + "到达";
                        }
                    }
                    //获取取件附件信息
                    FjcfbDto fjcfbDto = new FjcfbDto();
                    fjcfbDto.setYwid(list.get(i).getWlid());
                    fjcfbDto.setYwlx(BusTypeEnum.IMP_LOGISTICS_ARRIVE.getCode());
                    List<FjcfbDto> fjlist = fjcfbService.getDtoList(fjcfbDto);
                    map1.put("str", str);
                    map1.put("str4",str);
                    map1.put("str2", list.get(i).getSdsj());
                    map1.put("str3", list.get(i).getSdtime());
                    map1.put("bz", list.get(i).getSdbz());
                    map1.put("fjlist", fjlist);
                    map1.put("pathList", "");
                    mapList.add(map1);
                }
            }
        }
        map.put("status",mapList.size()>0?"success":"fail");
        map.put("mapList",mapList);
        return map;
    }

    /**
     * 统计标本运输时间
     * @param map
     * @return
     */
    public List<Map<String,Object>> getTransportationTimeListByRq(Map<String,Object> map){
        return dao.getTransportationTimeListByRq(map);
    }

    @Override
    public List<SjwlxxDto> getSjwlxxsByJsfsAndYh(SjwlxxDto sjwlxxDto) {
        return dao.getSjwlxxsByJsfsAndYh(sjwlxxDto);
    }

    @Override
 	public List<SjwlxxDto> getDtoListById(String sjtdid) {
        return dao.getDtoListById(sjtdid);
    }

    @Override
    public List<SjwlxxDto> getSjwlxxsBySjtdid(SjwlxxDto sjwlxxDto) {
        return dao.getSjwlxxsBySjtdid(sjwlxxDto);
    }
}
