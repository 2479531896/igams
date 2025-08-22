package com.matridx.igams.hrm.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.GzglDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.enums.RedisCommonKeyEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.*;
import com.matridx.igams.hrm.dao.entities.GrksglDto;
import com.matridx.igams.hrm.dao.entities.GrksglModel;
import com.matridx.igams.hrm.dao.entities.GrksmxDto;
import com.matridx.igams.hrm.dao.entities.PxglDto;
import com.matridx.igams.hrm.dao.post.IGrksglDao;
import com.matridx.igams.hrm.service.svcinterface.IGrksglService;
import com.matridx.igams.hrm.service.svcinterface.IGrksmxService;
import com.matridx.igams.hrm.service.svcinterface.IPxglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridxapp.hrm.config.HrmRabbitMqReceive;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GrksglServiceImpl extends BaseBasicServiceImpl<GrksglDto, GrksglModel, IGrksglDao> implements IGrksglService {
    private final Logger log = LoggerFactory.getLogger(HrmRabbitMqReceive.class);
    @Autowired
    IGrksmxService grksmxService;
    @Autowired
    IPxglService pxglService;
    @Autowired
    IGzglService gzglService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;

    /**
     * 获取考试清单
     */
    public List<GrksglDto> getPersonalTests(GrksglDto grksglDto){
        return dao.getPersonalTests(grksglDto);
    }

    /**
     * 提交并判分
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void submitScore(String str){
        log.error("答题结果："+str);
        JSONArray jsonArray = JSONArray.parseArray(str);
        Object hget = redisUtil.lGet(RedisCommonKeyEnum.TRAIN_GRKSMX.getKey() +jsonArray.getJSONObject(0).getString("gzid"));
        log.error("---------------------拿到Grksmx的Redis数据为："+hget);
        List<GrksmxDto> list = JSONArray.parseArray(String.valueOf(hget),GrksmxDto.class);
        List<GrksmxDto> mxlist=new ArrayList<>();
        for(int i=0;i<jsonArray.size();i++){
            for(GrksmxDto grksmxDto:list){
                if(jsonArray.getJSONObject(i).getString("ksid").equals(grksmxDto.getKsid())){
                    grksmxDto.setDtjg(jsonArray.getJSONObject(i).getString("dtjg"));
                    grksmxDto.setGzid(jsonArray.getJSONObject(i).getString("gzid"));
                    grksmxDto.setKsid(jsonArray.getJSONObject(i).getString("ksid"));
                    grksmxDto.setDtkssj(jsonArray.getJSONObject(i).getString("dtkssj").replaceAll("T", " ").substring(0, 19));
                    grksmxDto.setDtjssj(jsonArray.getJSONObject(i).getString("dtjssj").replaceAll("T", " ").substring(0, 19));
                    grksmxDto.setFs(jsonArray.getJSONObject(i).getString("fs"));
                    mxlist.add(grksmxDto);
                    list.remove(grksmxDto);
                    break;
                }
            }
        }
        Object get = redisUtil.get(RedisCommonKeyEnum.TRAIN_GRKSGL.getKey() +jsonArray.getJSONObject(0).getString("gzid"));
        if(get!=null){
            log.error("---------------------拿到Grksgl的Redis数据为："+ get);
            GrksglDto grksglDto = JSON.parseObject(get.toString(),GrksglDto.class);
//            redisUtil.del(RedisCommonKeyEnum.TRAIN_GRKSGL.getKey()+grksglDto.getGzid());
//            redisUtil.del(RedisCommonKeyEnum.TRAIN_GRKSMX.getKey()+grksglDto.getGzid());
            grksglDto.setKsjssj(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            GrksglDto grksglDto_t = getScore(mxlist, grksglDto);
            PxglDto pxglDto = new PxglDto();
            pxglDto.setGzid(grksglDto_t.getGzid());
            Object px = redisUtil.get(RedisCommonKeyEnum.TRAIN_TRAINPXINFO.getKey() + grksglDto.getPxid());
            log.error("---------------------submitScore-TRAINPXINFO_：" + px);
            PxglDto pxglDto_t;
            if (px!=null){
                pxglDto_t = JSON.parseObject(String.valueOf(px), PxglDto.class);
            }else {
                pxglDto_t = pxglService.viewTrainTask(pxglDto);
            }
            GzglDto gzglDto=new GzglDto();
            gzglDto.setGzid(grksglDto_t.getGzid());
            insert(grksglDto_t);
            if("true".equals(grksglDto_t.getJdflg())){
                gzglDto.setZt("80");
                gzglDto.setUrlqz(urlPrefix);
                gzglDto.setYwdz("/train/train/pagedataViewTrainDetails?grksid=" + grksglDto.getGrksid());
                gzglDto.setDdid(pxglDto_t.getQrry());
                gzglDto.setQrry(pxglDto_t.getQrryid());
                gzglService.progressSaveTask(gzglDto, grksglDto.getRequest());
            }else{
                int tgfs = Integer.parseInt(pxglDto_t.getTgfs());
                int score= Integer.parseInt(grksglDto_t.getZf());
                if (score >= tgfs) {
                    gzglDto.setZt("80");
                    gzglDto.setTgbj("1");
                    grksglDto.setTgbj("1");
                } else {
                    gzglDto.setZt("00");
                    gzglDto.setTgbj("0");
                    grksglDto.setTgbj("0");
                    if (StringUtil.isNotBlank(pxglDto_t.getKscs())) {
                        int kscs = Integer.parseInt(pxglDto_t.getKscs());
                        List<GrksglDto> grksglDtos = getPersonalTests(grksglDto);
                        int testCount = grksglDtos.size();
                        if ((kscs - testCount) == 0) {
                            gzglDto.setZt("80");
                        }
                    }
                }
            }
            Object userPxInfo = redisUtil.hget(RedisCommonKeyEnum.TRAIN_USERPXINFO.getKey() + grksglDto.getPxid(),grksglDto.getRyid());
            log.error("---------------------submitScore-USERPXINFO_：" + userPxInfo);
            if (userPxInfo!=null){
                GrksglDto grksglDto_info = JSON.parseObject(String.valueOf(userPxInfo), GrksglDto.class);
                //更新redis考试次数
                grksglDto_info.setKscs(String.valueOf(Integer.parseInt(grksglDto_info.getKscs())-1));
                //更新redis通过标记
                if ("1".equals(gzglDto.getTgbj())){
                    grksglDto_info.setTgbj(gzglDto.getTgbj());
                }
                //更新redis考试总分，获取最大分数
                if (StringUtil.isNotBlank(grksglDto_info.getZf())&&StringUtil.isNotBlank(grksglDto_info.getGrksid())){
                    BigDecimal zf1=new BigDecimal(grksglDto_info.getZf());
                    BigDecimal zf2=new BigDecimal(grksglDto_t.getZf());
                    // -1小于，0相等，1大于
                    if (zf1.compareTo(zf2) < 0){
                        grksglDto_info.setZf(grksglDto_t.getZf());
                        grksglDto_info.setGrksid(grksglDto_t.getGrksid());
                    }
                }else {
                    grksglDto_info.setZf(grksglDto_t.getZf());
                    grksglDto_info.setGrksid(grksglDto_t.getGrksid());
                }
                redisUtil.hset(RedisCommonKeyEnum.TRAIN_USERPXINFO.getKey()+grksglDto.getPxid(),grksglDto.getRyid(),JSON.toJSONString(grksglDto_info),RedisCommonKeyEnum.TRAIN_USERPXINFO.getExpire());
            }
            grksmxService.insertList(grksglDto_t.getGrksmxDtos());
            gzglDto.setXgry(grksglDto_t.getRyid());
            gzglService.update(gzglDto);
            grksglDto.setCkdabj(pxglDto_t.getCkdabj());
            grksglDto.setDcckbj(pxglDto_t.getDcckbj());
            redisUtil.set(RedisCommonKeyEnum.TRAIN_GRKSGL.getKey()+grksglDto.getGzid(),JSON.toJSONString(grksglDto),RedisCommonKeyEnum.TRAIN_GRKSGL.getExpire());
        }else{
            log.error("---------------------没有拿到Grksgl的Redis数据----------------------------");
        }
    }


    /**
     * 明细新增
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public GrksglDto getScore(List<GrksmxDto> list,GrksglDto grksglDto) {
        log.error("-----开始执行判分操作-------人员：" + grksglDto.getRyid() );
        String jdflg = "";
        int score = 0;
        boolean flag = false;
        for (int t=0;t<list.size();t++) {
            list.get(t).setXh(String.valueOf(t+1));
            if ("SELECT".equals(list.get(t).getTmlx()) || "MULTIPLE".equals(list.get(t).getTmlx())  || "JUDGE".equals(list.get(t).getTmlx())) {
                String answer = list.get(t).getDa();
                String dtjg = list.get(t).getDtjg();
                JSONArray jsonArray_t = JSONArray.parseArray(dtjg);
                log.error( "ksid:"+ list.get(t).getKsid() + " 答案：" +answer + " 答题结果：" + dtjg);
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < jsonArray_t.size(); i++) {
                    if (i == (jsonArray_t.size() - 1)) {
                        str.append(jsonArray_t.getString(i));
                    } else {
                        str.append(jsonArray_t.getString(i)).append(",");
                    }
                }
                log.error("第"+(t+1)+"题答题结果为："+str);
                list.get(t).setDtjg(str.toString());
                String[] split = answer.split(",");
                String[] option = str.toString().split(",");
                log.error("第"+(t+1)+"题答案个数为："+split.length);
                log.error("第"+(t+1)+"题答题结果个数为："+option.length);
                if (split.length == option.length) {
                    for (String s : option) {
                        if (answer.contains(s)) {
                            flag = true;
                        } else {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        log.error("第"+(t+1)+"题判断结果为： 对");
                        score += Integer.parseInt(list.get(t).getFs());
                        list.get(t).setDf(list.get(t).getFs());
                    } else {
                        log.error("第"+(t+1)+"题判断结果为： 错");
                        list.get(t).setDf("0");
                    }
                } else {
                    log.error("第"+(t+1)+"题判断结果为： 错");
                    list.get(t).setDf("0");
                }
            } else {
                jdflg="true";
                list.get(t).setDf("0");
            }
            list.get(t).setLrry(grksglDto.getRyid());
        }
        redisUtil.lSet(RedisCommonKeyEnum.TRAIN_GRKSMX.getKey()+grksglDto.getGzid(),JSON.toJSONString(list),RedisCommonKeyEnum.TRAIN_GRKSMX.getExpire());
        grksglDto.setJdflg(jdflg);
        grksglDto.setZf(String.valueOf(score));
        grksglDto.setGrksmxDtos(list);
        return grksglDto;
    }

    @Override
    public boolean updateGrksglDtos(List<GrksglDto> upGrksglDtos) {
        return dao.updateGrksglDtos(upGrksglDtos);
    }

    @Override
    public List<GrksglDto> getPagedIncomplete(GrksglDto grksglDto) {
        return dao.getPagedIncomplete(grksglDto);
    }

    /**
     * 导出
     */
    @Override
    public int getCountForSearchExp(GrksglDto grksglDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(grksglDto);
    }

    @Override
    public boolean remindInComplete(GrksglDto grksglDto) {
//        String token = talkUtil.getToken();
//         List<String> ddids=grksglDto.getIds();
        List<String> pxbts=grksglDto.getIdss();
        List<User> users = JSON.parseArray(grksglDto.getTzry_json(), User.class);
        int[] tj=new int[pxbts.size()];
        int sum=1;
        for (int i=0;i<users.size();i++){
            if (tj[i]!=0) {
                continue;
            }
            StringBuilder string= new StringBuilder(sum + "." + pxbts.get(i));
            for (int j=i+1;j<users.size();j++){
                if (users.get(i).getYhm().equals(users.get(j).getYhm())){
                    string.append(",");
                    string.append("\n");
                    sum++;
                    string.append(sum).append(".").append(pxbts.get(j));
                    tj[i]=1;
                    tj[j]=1;
                }
            }
            sum=1;
            String ICOMM_KS0001 = xxglService.getMsg("ICOMM_KS0001");
            String ICOMM_KS0002 = xxglService.getMsg("ICOMM_KS0002");
            String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/learningpage/learnlist/learnlist", StandardCharsets.UTF_8);
            //外网访问
            //String external=externalurl+"/ws/auditProcess/auditPhone?shxx_shry="+shgcDto.getSpgwcyDtos().get(i).getYhid()+"&shxx_shjs="+shgcDto.getSpgwcyDtos().get(i).getJsid()+"&ywid="+t_fjsqDto.getFjid()+"&sign="+sign+"&xlcxh="+shgcDto.getXlcxh()+"&business_url="+"/recheck/modRecheck&ywzd=fjid&shlbmc="+shgcDto.getShlbmc();
            List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
            OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
            btnJsonList.setTitle("详细");
            btnJsonList.setActionUrl(internalbtn);
            btnJsonLists.add(btnJsonList);
            if (StringUtil.isNotBlank(users.get(i).getYhm())) {
                talkUtil.sendCardMessage(users.get(i).getYhm(), users.get(i).getDdid(), ICOMM_KS0001, StringUtil.replaceMsg(ICOMM_KS0002, string.toString(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
            }
        }
        return true;
    }

    @Override
    public GrksglDto getDtoByPxidAndFzr(GrksglDto grksglDto) {
        return dao.getDtoByPxidAndFzr(grksglDto);
    }

    @Override
    public List<GrksglDto> getPagedRedPacket(GrksglDto grksglDto) {
        return dao.getPagedRedPacket(grksglDto);
    }

    @Override
    public GrksglDto getDtoRedPacketById(String id) {
        return dao.getDtoRedPacketById(id);
    }

    @Override
    public boolean updateRedPacket(GrksglDto grksglDto) {
        return dao.updateRedPacket(grksglDto);
    }

    public void remindInComplete() {
        GrksglDto grksglDto = new GrksglDto();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String date1 = date.format(new Date());
        grksglDto.setDqsj(date1);
        List<GrksglDto> grksglDtoList = dao.remindInComplete(grksglDto);
//        String token = talkUtil.getToken();
        String ICOMM_KS0001 = xxglService.getMsg("ICOMM_KS0001");
        String ICOMM_KS0002 = xxglService.getMsg("ICOMM_KS0002");
        if (!CollectionUtils.isEmpty(grksglDtoList)) {
            for (GrksglDto grksglDto1 : grksglDtoList) {
                String[] testAll = null;
                StringBuilder str = new StringBuilder();
                if (grksglDto1.getString_agg() != null) {
                    testAll = grksglDto1.getString_agg().split("@");
                }
                //小程序访问
                String internalbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/learningpage/learnlist/learnlist", StandardCharsets.UTF_8);
                //外网访问
                //String external=externalurl+"/ws/auditProcess/auditPhone?shxx_shry="+shgcDto.getSpgwcyDtos().get(i).getYhid()+"&shxx_shjs="+shgcDto.getSpgwcyDtos().get(i).getJsid()+"&ywid="+t_fjsqDto.getFjid()+"&sign="+sign+"&xlcxh="+shgcDto.getXlcxh()+"&business_url="+"/recheck/modRecheck&ywzd=fjid&shlbmc="+shgcDto.getShlbmc();
                List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                btnJsonList.setTitle("详细");
                btnJsonList.setActionUrl(internalbtn);
                btnJsonLists.add(btnJsonList);
                if (testAll != null && testAll.length > 0) {
                    for (int i = 0; i < testAll.length; i++) {
                        str.append(i).append(1).append(".").append(testAll[i]);
                        if (i < testAll.length - 1) {
                            str.append("," + "\n");
                        }
                    }
                }
                if (StringUtil.isNotBlank(grksglDto1.getYhm())) {
                    talkUtil.sendCardMessage(grksglDto1.getYhm(), grksglDto1.getDdid(), ICOMM_KS0001, StringUtil.replaceMsg(ICOMM_KS0002, str.toString(), DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
                }
            }

        }
    }

    /**
     * 根据搜索条件获取导出信息
     */
    public List<GrksglDto> getListForSearchExp(Map<String, Object> params) {
        GrksglDto grksglDto = (GrksglDto) params.get("entryData");
        queryJoinFlagExport(params, grksglDto);
        return dao.getListForSearchExp(grksglDto);
    }

    /**
     * 根据选择信息获取导出信息
     */
    public List<GrksglDto> getListForSelectExp(Map<String, Object> params) {
        GrksglDto grksglDto = (GrksglDto) params.get("entryData");
        queryJoinFlagExport(params, grksglDto);
        return dao.getListForSelectExp(grksglDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, GrksglDto grksglDto) {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
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
        grksglDto.setSqlParam(sqlcs);
    }

    @Override
    public List<GrksglDto> getRedPacketGroup(GrksglDto grksglDto) {
        return dao.getRedPacketGroup(grksglDto);
    }

    @Override
    public GrksglDto getRedPacketGroupPm(GrksglDto grksglDto) {
        return dao.getRedPacketGroupPm(grksglDto);
    }

    @Override
    public GrksglDto getSfHaveRedpacket(GrksglDto grksglDto) {
        return dao.getSfHaveRedpacket(grksglDto);
    }

    @Override
    public GrksglDto getSfHaveRedpacketNo(GrksglDto grksglDto) {
        return dao.getSfHaveRedpacketNo(grksglDto);
    }

    @Override
    public GrksglDto getDtoByIdAndGzid(GrksglDto grksglDto) {
        return dao.getDtoByIdAndGzid(grksglDto);
    }

    @Override
    public boolean remandTrain(GrksglDto grksglDto) {
        List<GrksglDto> grksglDtoList=dao.getIncompleteDtoList(grksglDto);
        for (GrksglDto grksglDto1:grksglDtoList){
            String external ="";
            if ("0".equals(grksglDto1.getSpbj())){
                external = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/learningpage/learntest/learntest&urlPrefix=" + urlPrefix + "&pxid=" + grksglDto1.getPxid(), StandardCharsets.UTF_8);
            }else if ("1".equals(grksglDto1.getSpbj())){
                external = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/learningpage/videolist/videolist&urlPrefix=" + urlPrefix + "&gzid=" + grksglDto1.getGzid()+"&ywid="+grksglDto1.getPxid()+"&pxbt="+grksglDto1.getPxbt()+"&TabCur="+"0", StandardCharsets.UTF_8);
            }
            List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
            OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
            btnJsonList.setTitle("小程序");
            btnJsonList.setActionUrl(external);
            btnJsonLists.add(btnJsonList);
            talkUtil.sendCardMessage(grksglDto1.getYhm(), "", xxglService.getMsg("ICOMM_PX00005"), StringUtil.replaceMsg(xxglService.getMsg("ICOMM_PX00006"),grksglDto1.getPxbt(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")), btnJsonLists, "1");
        }

        return true;
    }

    /**
     * 导出
     */
    public int getCountForSearchExpHb(GrksglDto grksglDto, Map<String, Object> params) {
        return dao.getCountForSearchExpHb(grksglDto);
    }
    /**
     * 根据选择信息获取导出信息
     */
    public List<GrksglDto> getListForSelectExpHb(Map<String, Object> params) {
        GrksglDto grksglDto = (GrksglDto) params.get("entryData");
        queryJoinFlagExport(params, grksglDto);
        return dao.getListForSelectExpHb(grksglDto);
    }
    /**
     * 根据搜索条件获取导出信息
     */
    public List<GrksglDto> getListForSearchExpHb(Map<String, Object> params) {
        GrksglDto grksglDto = (GrksglDto) params.get("entryData");
        queryJoinFlagExport(params, grksglDto);
        return dao.getListForSearchExpHb(grksglDto);
    }

    @Override
    public Double getRedPacketSum(GrksglDto grksglDto) {
        return dao.getRedPacketSum(grksglDto);
    }
}
