package com.matridx.igams.common.controller;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.TyszmxDto;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.ITyszmxService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.xml.BasicXmlReader;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Component
@RequestMapping("/init")
public class JcsjRedisInitController implements ApplicationRunner{
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private IJcsjService jcsjService;
    @Autowired
    private IXtszService xtszService;
	@Autowired
	private ITyszmxService tyszmxService;
    private final Logger log = LoggerFactory.getLogger(JcsjRedisInitController.class);

    @Value("${matridx.imptype.file:}")
    private String impTypeFile;
    @Value("${matridx.exptype.file:}")
    private String expTypeFile;
    @Value("${matridx.systemflg.redisreset:}")
    private String redisreset;

    @Value("${spring.rabbitmq.report.username:}")
    private String username;
    @Value("${spring.rabbitmq.report.password:}")
    private String password;

    @Value("${spring.rabbitmq.report.host:}")
    private String host;

    @Value("${spring.rabbitmq.report.port:}")
    private String port;

    @Value("${spring.rabbitmq.report.virtualHost:}")
    private String virtualHost;
    @Autowired
    DingTalkUtil dingTalkUtil;
    @Autowired
    IDdxxglService ddxxglService;
    @Autowired
    IXxglService xxglService;
    @RequestMapping("/redis")
    @ResponseBody
    public void jcsjRedisInit() {
        //Map<String,Object> map = new HashMap<>();
        //清除基础数据的所有键
        //Set<String> keys = redisTemplate.keys("*"+"matridx_jcsj:"+"*");
        //redisTemplate.delete(keys);
//		redisUtil.delLike("matridx_jcsj_");
//		redisUtil.delLike("matridx_jcsj:");
        //重新加载基础数据到redis中

//		for (int j = 0; j < dtolist.size(); j++) {
//			if ("2".equals(dtolist.get(j).getScbj())){//停用数据
//				redisUtil.hset("JCSJ_DISABLE:"+"matridx_jcsj_"+dtolist.get(j).getJclb(), dtolist.get(j).getCsid(), JSONObject.toJSONString(dtolist.get(j)),-1);
//			}else{//正常数据
//				redisUtil.hset("matridx_jcsj:"+dtolist.get(j).getJclb(), dtolist.get(j).getCsid(), JSONObject.toJSONString(dtolist.get(j)),-1);
//				redisUtil.lSet("List_matridx_jcsj:"+dtolist.get(j).getJclb(),  JSONObject.toJSONString(dtolist.get(j)),-1);
//			}
//			//包含停用的基础数据（不包含删除的）
//			redisUtil.lSet("All_matridx_jcsj:"+dtolist.get(j).getJclb(),  JSONObject.toJSONString(dtolist.get(j)),-1);
//		}
        String uuid = StringUtil.generateUUID();
        try {
            redisUtil.del("changjcsj");
            while (!redisUtil.setIfAbsent("redis-lock", uuid, 5L, TimeUnit.SECONDS)) {
                Thread.sleep(500);
            }
            List<JcsjDto> dtolist = jcsjService.getAllDtoList();//查出所有scbj!=‘1’基础数据

            log.error("分布式锁redis-lock开启:");
            String pre = "";
            Map<Object, Object> map;
            for (JcsjDto jcsjDto : dtolist) {
                if (!pre.equals(jcsjDto.getJclb())) {
                    if (StringUtil.isNotBlank(pre)) {
                        map = redisUtil.hmget("changjcsj");
                        if (map.get(pre) != null) {
                            redisUtil.del("JCSJ_DISABLE:" + "matridx_jcsj_" + pre + "_new");
                            redisUtil.del("matridx_jcsj:" + pre + "_new");
                            redisUtil.del("List_matridx_jcsj:" + pre + "_new");
                            redisUtil.del("All_matridx_jcsj:" + pre + "_new");
                        } else {
                            redisUtil.rename("JCSJ_DISABLE:" + "matridx_jcsj_" + pre + "_new", "JCSJ_DISABLE:" + "matridx_jcsj_" + pre);
                            redisUtil.rename("matridx_jcsj:" + pre + "_new", "matridx_jcsj:" + pre);
                            redisUtil.rename("List_matridx_jcsj:" + pre + "_new", "List_matridx_jcsj:" + pre);
                            redisUtil.rename("All_matridx_jcsj:" + pre + "_new", "All_matridx_jcsj:" + pre);
                        }

                    }
                    pre = jcsjDto.getJclb();
                }
                if ("2".equals(jcsjDto.getScbj())) {//停用数据
                    redisUtil.hset("JCSJ_DISABLE:" + "matridx_jcsj_" + jcsjDto.getJclb() + "_new", jcsjDto.getCsid(), JSONObject.toJSONString(jcsjDto), -1);
                } else {//正常数据
                    redisUtil.hset("matridx_jcsj:" + jcsjDto.getJclb() + "_new", jcsjDto.getCsid(), JSONObject.toJSONString(jcsjDto), -1);
                    redisUtil.lSet("List_matridx_jcsj:" + jcsjDto.getJclb() + "_new", JSONObject.toJSONString(jcsjDto), -1);
                }
                //包含停用的基础数据（不包含删除的）
                redisUtil.lSet("All_matridx_jcsj:" + jcsjDto.getJclb() + "_new", JSONObject.toJSONString(jcsjDto), -1);

            }
            map = redisUtil.hmget("changjcsj");
            if (StringUtil.isNotBlank(pre)) {
                if (map.get(pre) != null) {
                    redisUtil.del("JCSJ_DISABLE:" + "matridx_jcsj_" + pre + "_new");
                    redisUtil.del("matridx_jcsj:" + pre + "_new");
                    redisUtil.del("List_matridx_jcsj:" + pre + "_new");
                    redisUtil.del("All_matridx_jcsj:" + pre + "_new");
                } else {
                    redisUtil.rename("JCSJ_DISABLE:" + "matridx_jcsj_" + pre + "_new", "JCSJ_DISABLE:" + "matridx_jcsj_" + pre);
                    redisUtil.rename("matridx_jcsj:" + pre + "_new", "matridx_jcsj:" + pre);
                    redisUtil.rename("List_matridx_jcsj:" + pre + "_new", "List_matridx_jcsj:" + pre);
                    redisUtil.rename("All_matridx_jcsj:" + pre + "_new", "All_matridx_jcsj:" + pre);
                }
            }

            log.error("---------------基础数据存储redis执行结束。执行条数为：" + dtolist.size() + "---------------");
        } catch (Exception e) {
            log.error("初始化系统设置错误:" + e.getMessage());
        } finally {
            log.error("分布式锁开始关闭:");
            if (uuid.equals(redisUtil.get("redis-lock"))) {
                redisUtil.del("redis-lock");
                log.error("分布式锁关闭成功:");
                redisUtil.del("changjcsj");
            }
        }


    }

    @RequestMapping("/readXml")
    @ResponseBody
    public void xmlRedisInit() {
        //读取导入配置config文件，更新数据到redis覆盖旧键
        List<Map<String, String>> importList = BasicXmlReader.readXmlToList(impTypeFile);
        String impkey = "IMP_:_CONFIG";
        redisUtil.set(impkey, importList);

        //读取导出配置config文件，更新数据到redis覆盖旧键
        List<Map<String, String>> exportList = BasicXmlReader.readXmlToList(expTypeFile);
        String expkey = "EXP_:_CONFIG";
        redisUtil.set(expkey, exportList);
        log.error("---------------导入导出基本配置存储redis执行结束---------------");
    }

    public void xtszRedisInit() {
        //redis中存储系统设置使用map结构，键为表名matridx_xtsz，item为表字段szlb，值为表字段szz内容
        //redisTemplate.delete("matridx_xtsz");
        log.error("---------------开始系统设置-----------");
        redisUtil.del("matridx_xtsz");
        //重新加载基础数据到redis中
        List<XtszDto> xtszList = xtszService.getXtszList();//查找出系统设置表中的所有数据
        for (XtszDto xtszDto : xtszList) {
            redisUtil.hset("matridx_xtsz", xtszDto.getSzlb(), JSONObject.toJSONString(xtszDto), -1);

        }
        log.error("---------------系统设置存储redis执行结束---------------");
    }

	public void tyszRedisInit() {
		log.error("---------------开始执行通用设置-----------");
		redisUtil.del("GeneralSetting");
		//重新加载基础数据到redis中
		List<TyszmxDto> tyszmxDtos = tyszmxService.getAllList();//查找出系统设置表中的所有数据
		if(tyszmxDtos!=null&& !tyszmxDtos.isEmpty()){
			List<TyszmxDto> newList=new ArrayList<>();
			TyszmxDto tyszmxDto=tyszmxDtos.get(0);
			for(TyszmxDto dto:tyszmxDtos){
				if(tyszmxDto.getEjzyid().equals(dto.getEjzyid())){
					newList.add(dto);
				}else{
                    List<TyszmxDto>_newList=newList.stream().sorted(Comparator.comparing(TyszmxDto::getBtid,Comparator.nullsLast(String::compareTo))).collect(Collectors.toList());
					redisUtil.hset("GeneralSetting", tyszmxDto.getEjzyid(), JSONObject.toJSONString(_newList) ,-1);
					newList=new ArrayList<>();
					tyszmxDto=dto;
					newList.add(dto);
				}
			}
            List<TyszmxDto>_newList=newList.stream().sorted(Comparator.comparing(TyszmxDto::getBtid,Comparator.nullsLast(String::compareTo))).collect(Collectors.toList());
			redisUtil.hset("GeneralSetting", tyszmxDto.getEjzyid(), JSONObject.toJSONString(_newList) ,-1);
		}
		log.error("---------------通用设置存储redis执行结束---------------");
	}

    @PostConstruct
    public void runBefore(){
        log.error("---------------JcsjRedisInitController PostConstruct注解下的runBefore---------------");
        if ("1".equals(redisreset) || "".equals(redisreset)) {
            try {
                jcsjRedisInit();
                xmlRedisInit();
                xtszRedisInit();
                tyszRedisInit();
            }catch (Exception e){
                sendExceptionMsg(e.getMessage());
                throw new RuntimeException("主服务器系统数据初始化失败");
            }
        }
    }
    /*
        主服务器系统数据初始化失败发送消息
     */
    private void sendExceptionMsg(String msg) {
        List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.SERVER_INIT_EXCEPTION.getCode());
        if (!CollectionUtils.isEmpty(ddxxglDtolist)) {
            String ICOMM_FWYC001 = xxglService.getMsg("ICOMM_FWYC001");
            String ICOMM_FWYC002 = xxglService.getMsg("ICOMM_FWYC002");
            if (StringUtil.isNotBlank(ICOMM_FWYC001)&&StringUtil.isNotBlank(ICOMM_FWYC002)){
                for (DdxxglDto ddxxglDto : ddxxglDtolist) {
                    dingTalkUtil.sendWorkMessage(ddxxglDto.getYhm(),
                            ddxxglDto.getYhid(), ICOMM_FWYC001, StringUtil.replaceMsg(ICOMM_FWYC002, msg));
                }
            }
        }
    }



    @Override
    public void run(ApplicationArguments args) {
        log.error("---------------ApplicationRunner---------------");
    }

}
