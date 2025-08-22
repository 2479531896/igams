package com.matridx.igams.hrm.service.impl;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.UserDto;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.enums.RedisCommonKeyEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IUserService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.hrm.dao.entities.JpglDto;
import com.matridx.igams.hrm.dao.entities.JpglModel;
import com.matridx.igams.hrm.dao.entities.JpjgDto;
import com.matridx.igams.hrm.dao.entities.JpmxDto;
import com.matridx.igams.hrm.dao.post.IJpglDao;
import com.matridx.igams.hrm.service.svcinterface.IJpglService;
import com.matridx.igams.hrm.service.svcinterface.IJpjgService;
import com.matridx.igams.hrm.service.svcinterface.IJpmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.encrypt.DBEncrypt;
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
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class JpglServiceImpl extends BaseBasicServiceImpl<JpglDto, JpglModel, IJpglDao> implements IJpglService {
    @Autowired
    private IJpmxService jpmxService;
    @Autowired
    IFjcfbService fjcfbService;
	@Autowired
    DingTalkUtil dingTalkUtil;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IJpjgService jpjgService;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Autowired
    IUserService userService;
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;
    @Value("${matridx.rabbit.preflg:}")
    private String preRabbitFlg;
    @Value("${matridx.rabbit.flg:}")
    private String rabbitFlg;
    private final Logger log = LoggerFactory.getLogger(JpglServiceImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delAwardManagement(JpglDto jpglDto) {
        JpmxDto jpmxDto=new JpmxDto();
        jpmxDto.setScry(jpglDto.getScry());
        jpmxDto.setIds(jpglDto.getIds());
        boolean delete = jpmxService.delete(jpmxDto);
        if(!delete){
            return false;
        }
        int deleted = dao.delete(jpglDto);
        if(deleted==0){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addSaveAwardManagement(JpglDto jpglDto) {
        jpglDto.setJpglid(StringUtil.generateUUID());
        int inserted = dao.insert(jpglDto);
        if(inserted==0){
            return false;
        }
        List<JpmxDto> list = (List<JpmxDto>) JSON.parseArray(jpglDto.getJpmx_json(), JpmxDto.class);
        if(list!=null&&!list.isEmpty()){
            for(JpmxDto dto:list){
                dto.setJpmxid(StringUtil.generateUUID());
                dto.setJpglid(jpglDto.getJpglid());
                dto.setSysl(dto.getSl());
                dto.setLrry(jpglDto.getLrry());
                if(StringUtil.isNotBlank(dto.getFjid())){
                    boolean saveFile = fjcfbService.save3RealFile(dto.getFjid(),dto.getJpglid(), dto.getJpmxid());
                    if (!saveFile){
                        return false;
                    }
                }
            }
            boolean inserted1 = jpmxService.insertList(list);
            if(!inserted1){
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean modSaveAwardManagement(JpglDto jpglDto) {
        int updated = dao.update(jpglDto);
        if(updated==0){
            return false;
        }
        JpmxDto jpmxDto=new JpmxDto();
        jpmxDto.setIds(jpglDto.getJpglid());
        jpmxDto.setScry(jpglDto.getXgry());
        jpmxService.delete(jpmxDto);
        List<JpmxDto> list = (List<JpmxDto>) JSON.parseArray(jpglDto.getJpmx_json(), JpmxDto.class);
        if(list!=null&&!list.isEmpty()){
            List<JpmxDto> updateList=new ArrayList<>();
            List<JpmxDto> insertList=new ArrayList<>();
            FjcfbDto fjcfbDto=new FjcfbDto();
            fjcfbDto.setYwlx(BusTypeEnum.IMP_AWARD_MANAGEMENT.getCode());
            fjcfbDto.setYwid(jpglDto.getJpglid());
            List<FjcfbDto> fjcfbDtos = fjcfbService.getDtoList(fjcfbDto);
            for(JpmxDto dto:list){
                dto.setJpglid(jpglDto.getJpglid());
                dto.setSysl(dto.getSl());
                dto.setXgry(jpglDto.getXgry());
                if(StringUtil.isBlank(dto.getJpmxid())){
                    dto.setJpmxid(StringUtil.generateUUID());
                    if(StringUtil.isNotBlank(dto.getFjid())){
                        boolean saveFile = fjcfbService.save3RealFile(dto.getFjid(),dto.getJpglid(), dto.getJpmxid());
                        if (!saveFile){
                            return false;
                        }
                    }
                    insertList.add(dto);
                }else{
                    if(StringUtil.isNotBlank(dto.getFjid())){
                        boolean isFind=false;
                        for(FjcfbDto fjcfbDto_t:fjcfbDtos){
                            if(dto.getJpmxid().equals(fjcfbDto_t.getZywid())){
                                isFind=true;
                                break;
                            }
                        }
                        if(!isFind){
                            boolean saveFile = fjcfbService.save3RealFile(dto.getFjid(),dto.getJpglid(), dto.getJpmxid());
                            if (!saveFile){
                                return false;
                            }
                        }
                    }
                    updateList.add(dto);
                }
            }
            if(!insertList.isEmpty()){
                boolean inserted1 = jpmxService.insertList(insertList);
                if(!inserted1){
                    return false;
                }
            }
            if(!updateList.isEmpty()){
                boolean updated1 = jpmxService.updateList(updateList);
                if(!updated1){
                    return false;
                }
            }
        }
        return true;
    }
	
	/*
        发送抽奖至群
     */
    public void sendLotteryToCrowd(Map<String,String> map){
        String jpglid = map.get("jpglid");
        String wbcxid = map.get("wbcxid");
        JpglDto dtoById = dao.getLotteryInfo(jpglid);
        String pictureUrl = dtoById.getTpfm();
        UserDto userDto_sel = new UserDto();
        userDto_sel.setWbcxid(wbcxid);
        UserDto userDto = userService.getWbcxDto(userDto_sel);
        DBEncrypt dbEncrypt = new DBEncrypt();
        String dingtalkurl = dbEncrypt.dCode(userDto.getJumpdingtalkurl());
        String external = dingtalkurl+"page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/jumpLink/jumpLink&url="+URLEncoder.encode(applicationurl+urlPrefix+"/award/award/minidataLuckWheel?access_token={access_token}"+URLEncoder.encode("&jpglid=" + jpglid, StandardCharsets.UTF_8), StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        log.error("sendLotteryToCrowd--external{}",external);
        boolean isSuccess = dingTalkUtil.sendGroupMessageAtTo(dtoById.getBt(), dtoById.getWebhook(), pictureUrl, external);
        if (isSuccess){
            JpglDto jpglDto_up = new JpglDto();
            jpglDto_up.setJpglid(jpglid);
            jpglDto_up.setSffs("1");
            dao.updateSffs(jpglDto_up);
            JpmxDto jpmxDto = new JpmxDto();
            jpmxDto.setJpglid(jpglid);
            List<JpmxDto> dtoList = jpmxService.getLotteryInfos(jpmxDto);
            //过期时间 结束日期-开始日期 加一天
            redisUtil.set(RedisCommonKeyEnum.AWARD_JPMX.getKey()+jpmxDto.getJpglid(),JSON.toJSONString(dtoList),dtoList.get(0).getRqc()*24*60*60);
            redisUtil.set(RedisCommonKeyEnum.AWARD_JPGL.getKey()+jpglid, JSON.toJSONString(dtoById), dtoById.getRqc()*24*60*60);
        }
    }

    @Override
    public JpglDto getLotteryInfo(String jpglid) {
        Object jpglObj = redisUtil.get(RedisCommonKeyEnum.AWARD_JPGL.getKey() + jpglid);
        JpglDto jpglDto;
        if (jpglObj!=null){
            jpglDto = JSON.parseObject(String.valueOf(jpglObj), JpglDto.class);
        }else {
            jpglDto = dao.getLotteryInfo(jpglid);
            redisUtil.set(RedisCommonKeyEnum.AWARD_JPGL.getKey()+jpglid, JSON.toJSONString(jpglDto), jpglDto.getRqc()*24*60*60);
        }
        return jpglDto;
    }

    /*
        获取抽奖结果
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> getLotteryResult(JpglDto jpglDto, User user) throws BusinessException {
        long l = System.currentTimeMillis();
        Map<String,Object> map = new HashMap<>();
        JpmxDto jpmxDto_sel = new JpmxDto();
        jpmxDto_sel.setJpglid(jpglDto.getJpglid());
        List<JpmxDto> jpmxDtos = jpmxService.getLotteryInfos(jpmxDto_sel);
        int zsl = 0;
        for (JpmxDto jpmxDto : jpmxDtos) {
            int sysl = Integer.parseInt(StringUtil.isBlank(jpmxDto.getSysl())?"0":jpmxDto.getSysl());
            sysl = Math.max(sysl, 0);
            //增加倍率
            if (StringUtil.isNotBlank(jpmxDto.getBs())){
                sysl = new BigDecimal(jpmxDto.getBs()).multiply(new BigDecimal(sysl)).intValue();
            }
            zsl = zsl + sysl;
        }
        //很遗憾，奖品已经被抽完了！
        if (zsl<=0){
            log.error("---------------------getLotteryResult--奖品抽完了：ryid:"+user.getYhid()+"rymc"+user.getZsxm());
            map.put("result",false);
            map.put("jpmxDto",jpmxDtos.stream().filter(e->"谢谢参与".equals(e.getJpmc())).collect(Collectors.toList()).get(0));
            return map;
        }
        Random random = new Random();
        int jps =  random.nextInt(zsl)+1;
        int zjpsl = 0;
        for (JpmxDto jpmxDto : jpmxDtos) {
            int sysl = Integer.parseInt(StringUtil.isBlank(jpmxDto.getSysl())?"0":jpmxDto.getSysl());
            sysl = Math.max(sysl, 0);
            zjpsl = zjpsl + new BigDecimal(StringUtil.isNotBlank(jpmxDto.getBs())?jpmxDto.getBs():"1").multiply(new BigDecimal(sysl)).intValue();
            if (zjpsl>=jps){
                map.put("jpmxDto",jpmxDto);
                //抽奖结果
                map.put("result",!"谢谢参与".equals(jpmxDto.getJpmc()));
                log.error("---------------------getLotteryResult--jpmxDto："+ JSON.toJSONString(jpmxDto)+"随机数："+jps+",ryid:"+user.getYhid()+"rymc"+user.getZsxm());
                JpjgDto jpjgDto = new JpjgDto();
                jpjgDto.setJpjgid(StringUtil.generateUUID());
                jpjgDto.setJpglid(jpmxDto.getJpglid());
                jpjgDto.setJpmxid(jpmxDto.getJpmxid());
                jpjgDto.setYhid(user.getYhid());
                Date date = new Date();
                jpjgDto.setHjsj(DateUtils.getCustomFomratCurrentDate(date,"yyyy-MM-dd HH:mm:ss.SSS"));
                log.error("--------保存获奖记录-------------");
                boolean insert = jpjgService.insert(jpjgDto);
                if (!insert) {
                    throw new BusinessException("msg", "保存获奖记录失败！");
                }
                boolean update = jpmxService.updateSysl(jpmxDto);
                if (!update) {
                    throw new BusinessException("msg", "更新奖品剩余数量失败！");
                }
                jpmxDto.setSysl(String.valueOf(sysl - 1));
                //过期时间 结束日期-开始日期 加一天
                redisUtil.set(RedisCommonKeyEnum.AWARD_JPMX.getKey()+jpmxDto.getJpglid(),JSON.toJSONString(jpmxDtos),jpmxDtos.get(0).getRqc()*24*60*60);
                jpjgDto.setYhm(user.getYhm());
                jpjgDto.setZsxm(user.getZsxm());
                jpjgDto.setJpmc(jpmxDto.getJpmc());
                String jpjgStr = JSON.toJSONString(jpjgDto);
                redisUtil.hset(RedisCommonKeyEnum.AWARD_RECORES.getKey()+jpmxDto.getJpglid(),user.getYhid(),jpjgStr, jpmxDtos.get(0).getRqc()*24*60*60);
                if (!"谢谢参与".equals(jpmxDto.getJpmc())){
                    redisUtil.zIncrementScore(RedisCommonKeyEnum.AWARD_RECORE_LIST.getKey()+jpmxDto.getJpglid(),jpjgStr,date.getTime() ,jpmxDtos.get(0).getRqc()*24*60*60);
                }
        /*        if ("1".equals(jpmxDto.getSftz())&&StringUtil.isNotBlank(jpmxDto.getTznr())&&StringUtil.isNotBlank(user.getZsxm())) {
                    String tznr = jpmxDto.getTznr();
                    tznr = tznr.replace("【name】", user.getZsxm()).replace("【jpmc】", jpmxDto.getJpmc());
                    Map<String, String> paramMap = new HashMap<>();
                    paramMap.put("yhid",user.getYhid());
                    paramMap.put("tzqcskz1",jpmxDto.getTzqcskz1());
                    paramMap.put("tznr",tznr);
                    paramMap.put("sendGroupMessage","1");
                    amqpTempl.convertAndSend("send_group_messages_delay_exchange", preRabbitFlg+"send_group_messages_delay_key"+rabbitFlg, JSON.toJSONString(paramMap), new MessagePostProcessor() {
                        @Override
                        public Message postProcessMessage(Message message) throws AmqpException {
                            message.getMessageProperties().setExpiration("6000");//6秒
                            return message;
                        }
                    });
                }*/
                break;
            }
        }
        log.error("----------getLotteryResult--耗时："+(System.currentTimeMillis()-l)/1000+"秒");
        return map;
    }
}
