package com.matridx.igams.experiment.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.springboot.util.base.StringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequestMapping("/eureka")
@Controller
public class EurekaController {

    @Value("${eureka.client.serviceUrl.defaultZone:}")
    private  String eurekaUrl;
    @Autowired
    RedisUtil redisUtil;
    @Value("${matridx.wechat.applicationurl:}")
    private  String applicationurl;
    
    private final Logger log = LoggerFactory.getLogger(EurekaController.class);
    
    ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    @RequestMapping("/eureka/pageListEurekaServer")
    public ModelAndView eurekaList(){

        return new ModelAndView("experiment/eureka/eureka_List");
    }

    @RequestMapping("/eureka/pagedataReloadView")
    public ModelAndView eurekaReloadView(String apps,String ip_ports){
        ModelAndView mav = new ModelAndView("experiment/eureka/eureka_reload");
        mav.addObject("apps",apps);
        mav.addObject("ip_ports",ip_ports);

        return mav;
    }


    @ResponseBody
    @RequestMapping("/eureka/pagedataEurekaList")
    public Map<String,Object> getList(String isRedis,String sortName,String sortOrder){
        Map<String,Object> returnMap=new HashMap<>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        
        RestTemplate restTemplate = new RestTemplate();

        String[] userNameAndPass=returnUserNameAndPassWord();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userNameAndPass[0], userNameAndPass[1]));
        //获取eureka列表
        JSONObject jsonObject=restTemplate.getForObject(eurekaUrl+"/apps/", JSONObject.class);
        JSONObject appliactions= null;
        if (jsonObject != null) {
            appliactions = JSONObject.parseObject(JSONObject.toJSONString(jsonObject.get("applications")));
        }
        JSONArray appliactionList= null;
        if (appliactions != null) {
            appliactionList = JSONObject.parseArray(JSONObject.toJSONString(appliactions.get("application")));
        }
        List<Map<String,Object>>mapList=new ArrayList<>();
        List<Object>redisList=redisUtil.lGet("eurekaList");
        if (appliactionList != null) {
            for(int i=0;i<appliactionList.size();i++){
                JSONObject appliaction=appliactionList.getJSONObject(i);
                JSONArray instanceList=JSONObject.parseArray(JSONObject.toJSONString(appliaction.get("instance")));
                for(int o=0;o<instanceList.size();o++){
                    JSONObject instance=instanceList.getJSONObject(o);
                    Map<String,Object>map=new HashMap<>();
                    JSONObject leaseInfo=instanceList.getJSONObject(o).getJSONObject("leaseInfo");
                    SimpleDateFormat time= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    map.put("app",instance.get("app"));
                    map.put("registrationTimestamp",time.format(Long.valueOf(leaseInfo.get("registrationTimestamp").toString())));
                    map.put("lastRenewalTimestamp",time.format(Long.valueOf(leaseInfo.get("lastRenewalTimestamp").toString())));
                    map.put("serviceUpTimestamp",time.format(Long.valueOf(leaseInfo.get("serviceUpTimestamp").toString())));
                    map.put("lastUpdatedTimestamp",time.format(Long.valueOf(instance.get("lastUpdatedTimestamp").toString())));
                    map.put("lastDirtyTimestamp",time.format(Long.valueOf(instance.get("lastDirtyTimestamp").toString())));
                    map.put("ipAddr",instance.get("ipAddr"));
                    map.put("port",JSONObject.parseObject(JSONObject.toJSONString(instance.get("port"))).get("$"));
                    boolean flag=false;
                    if (redisList != null) {
                        for(Object obj:redisList){
                            JSONObject job=JSONObject.parseObject(obj.toString());
                            String ipaddr=map.get("ipAddr")+":"+map.get("port");
                            if(job.get("ipAddr").equals(ipaddr)){
                                flag=true;
                            }
                        }
                    }
                    if(!flag){
                        mapList.add(map);
                    }

                }
            }
        }
        if("0".equals(isRedis)){
            returnMap.put("total",mapList.size());
            mapList.sort((o1, o2) -> {
                if ("port".equals(sortName)) {
                    if (sortOrder == null || "".equals(sortOrder) || sortOrder.equalsIgnoreCase("asc"))
                        return ((Integer) o1.get(sortName)).compareTo((Integer) o2.get(sortName));
                    else
                        return -1 * ((Integer) o1.get(sortName)).compareTo((Integer) o2.get(sortName));
                } else {
                    if (sortOrder == null || "".equals(sortOrder) || sortOrder.equalsIgnoreCase("asc"))
                        return ((String) o1.get(sortName)).compareTo((String) o2.get(sortName));
                    else
                        return -1 * ((String) o1.get(sortName)).compareTo((String) o2.get(sortName));
                }
            });
            returnMap.put("rows",mapList);
        }else{
            List<Object>list=redisUtil.lGet("eurekaList");
            if(list==null){
                list=new ArrayList<>();
            }
            returnMap.put("total",list.size());
            returnMap.put("rows",lgetDto(list,sortName,sortOrder));
        }
        return returnMap;
    }
    public  List<Map<String,Object>> lgetDto(List<Object>list,String sortName,String sortOrder){
        List<Map<String,Object>> jclist = new ArrayList<>();
        for (Object obj : list) {
            Map<String,Object> j = JSON.parseObject(obj.toString(), Map.class);
            jclist.add(j);
        }
        jclist.sort((o1, o2) -> {
            if ("port".equals(sortName)) {
                if (sortOrder == null || "".equals(sortOrder) || sortOrder.equalsIgnoreCase("asc"))
                    return ((Integer) o1.get(sortName)).compareTo((Integer) o2.get(sortName));
                else
                    return -1 * ((Integer) o1.get(sortName)).compareTo((Integer) o2.get(sortName));
            } else {
                if (sortOrder == null || "".equals(sortOrder) || sortOrder.equalsIgnoreCase("asc"))
                    return ((String) o1.get(sortName)).compareTo((String) o2.get(sortName));
                else
                    return -1 * ((String) o1.get(sortName)).compareTo((String) o2.get(sortName));
            }
        });
        //jclist.sort((a,b) => {return ((String)a.get(sortName)).compareTo((String)b.get(sortName));});
        return jclist;
    }
    @RequestMapping("/eureka/pagedataDelEurekaServer")
    @ResponseBody
    public Map<String,Object> delEurekaServer(String apps,String ip_ports){
    	log.error("停止--apps：" + apps+" ip_ports:"+ip_ports);
        Map<String,Object> map=new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        String[] userNameAndPass=returnUserNameAndPassWord();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userNameAndPass[0], userNameAndPass[1]));
        String[] appsArr=apps.split(",");
        String[] ip_portArr=ip_ports.split(",");
        List<Object>redisList=redisUtil.lGet("eurekaList");
        for(int i=0;i<appsArr.length;i++){
            if(StringUtil.isNotBlank(appsArr[i])){
                //请求API剔除服务
                //restTemplate.delete(eurekaUrl+"/apps/"+appsArr[i]+"/"+ip_portArr[i]);
                boolean flag =false;


                String ip=ip_portArr[i].split(":")[0];
                String port=ip_portArr[i].split(":")[1];
                String appsServer=appsArr[i];
                if (redisList != null) {
                    for(Object obj:redisList){
                        JSONObject job=JSONObject.parseObject(obj.toString());
                        if(job.get("ipAddr").equals(ip_portArr[i])){
                            flag=true;
                        }
                    }
                }
                if(!flag){
                	log.error("停止--根据servers从已剔除Redis清单里获取信息继续剔除服务：" + eurekaUrl+"/apps/"+appsArr[i]+"/"+ip_portArr[i]+"/status?value=OUT_OF_SERVICE");
                	
                    restTemplate.put(eurekaUrl+"/apps/"+appsArr[i]+"/"+ip_portArr[i]+"/status?value=OUT_OF_SERVICE",new HashMap<>());
                    Map<String,Object> redisMap=new HashMap<>();
                    redisMap.put("ip",ip);
                    redisMap.put("port",port);
                    redisMap.put("app",appsServer);
                    redisMap.put("ipAddr",ip+":"+port.trim());
                    redisMap.put("operate","stop");
                    redisUtil.lSet("eurekaList",JSONObject.toJSONString(redisMap),300);
                }
                //请求eureka
                Runnable newRunnable = () -> sendHttp(ip,port,appsServer,"stop");
                cachedThreadPool.execute(newRunnable);
                //restTemplate.getForObject("http://"+ip+":9510"+"/eurekaApp/"+port+"/"+appsArr[i].toLowerCase()+"/stop",JSONObject.class);
            }
        }
        map.put("status","success");
        map.put("message","停止eureka成功");
        return map;
    }


    public void sendHttp(String ip,String port,String server,String operate){
        RestTemplate restTemplate = new RestTemplate();
        log.error("请求相应的服务器执行相应shell文件： " + "http://"+ip+":9510"+"/eurekaApp/"+port+"/"+server.toLowerCase()+"/"+operate+"?delurl="+eurekaUrl+"/apps/"+server+"/"+ip+":"+port+"&ip="+ip+":"+port+"&appurl="+applicationurl);
        restTemplate.getForObject("http://"+ip+":9510"+"/eurekaApp/"+port+"/"+server.toLowerCase()+"/"+operate+"?delurl="+eurekaUrl+"/apps/"+server+"/"+ip+":"+port+"&ip="+ip+":"+port+"&appurl="+applicationurl,JSONObject.class);
    }


    @RequestMapping("/eureka/pagedataStartEurekaServer")
    @ResponseBody
    public Map<String,Object> startEurekaServer(String apps,String ip_ports){
    	log.error("启动--apps：" + apps+" ip_ports:"+ip_ports);
        Map<String,Object> map=new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        String[] userNameAndPass=returnUserNameAndPassWord();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userNameAndPass[0], userNameAndPass[1]));
        String[] appsArr=apps.split(",");
        String[] ip_portArr=ip_ports.split(",");
        List<Object>redisList=redisUtil.lGet("eurekaList");
        for(int i=0;i<appsArr.length;i++){
            if(StringUtil.isNotBlank(appsArr[i])){
                //请求API剔除服务
                //restTemplate.delete(eurekaUrl+"/apps/"+appsArr[i]+"/"+ip_portArr[i]);
                boolean flag =false;
                if (redisList != null) {
                    for(Object obj:redisList){
                        JSONObject job=JSONObject.parseObject(obj.toString());
                        if(job.get("ipAddr").equals(ip_portArr[i])){
                            flag=true;
                        }
                    }
                }

                String ip=ip_portArr[i].split(":")[0];
                String port=ip_portArr[i].split(":")[1];
                //请求eureka
                String appsServer=appsArr[i];
                if(!flag){
                	log.error("启动--根据servers从已剔除Redis清单里获取信息继续剔除服务：" + eurekaUrl+"/apps/"+appsArr[i]+"/"+ip_portArr[i]+"/status?value=OUT_OF_SERVICE");
                    restTemplate.put(eurekaUrl+"/apps/"+appsArr[i]+"/"+ip_portArr[i]+"/status?value=OUT_OF_SERVICE",new HashMap<>());
                    Map<String,Object> redisMap=new HashMap<>();
                    redisMap.put("ip",ip);
                    redisMap.put("port",port);
                    redisMap.put("app",appsServer);
                    redisMap.put("ipAddr",ip+":"+port.trim());
                    redisMap.put("operate","start");

                    redisUtil.lSet("eurekaList",JSONObject.toJSONString(redisMap),300);
                }

                Runnable newRunnable = () -> sendHttp(ip,port,appsServer,"start");
                cachedThreadPool.execute(newRunnable);
                //restTemplate.getForObject("http://"+ip+":9510"+"/eurekaApp/"+port+"/"+appsArr[i].toLowerCase()+"/start",JSONObject.class);
            }
        }
        map.put("status","success");
        map.put("message","启动eureka成功");
        return map;
    }

    @RequestMapping("/eureka/pagedataReloadEurekaServer")
    @ResponseBody
    public Map<String,Object> reloadEurekaServer(String apps,String ip_ports,String servers){
    	log.error("重载--apps：" + apps+" ip_ports:"+ip_ports+"  servers"+ servers);
        Map<String,Object> map=new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        String[] userNameAndPass=returnUserNameAndPassWord();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userNameAndPass[0], userNameAndPass[1]));
        String[] appsArr=apps.split(",");
        String[] ip_portArr=ip_ports.split(",");
        List<Object>redisList=redisUtil.lGet("eurekaList");
        for(int i=0;i<appsArr.length;i++){
            if(StringUtil.isNotBlank(appsArr[i])){
                //请求API剔除服务
                //restTemplate.delete(eurekaUrl+"/apps/"+appsArr[i]+"/"+ip_portArr[i]);
                boolean flag =false;
                if (redisList != null) {
                    for(Object obj:redisList){
                        JSONObject job=JSONObject.parseObject(obj.toString());
                        if(job.get("ipAddr").equals(ip_portArr[i])){
                            flag=true;
                        }
                    }
                }

                String ip=ip_portArr[i].split(":")[0];
                String port=ip_portArr[i].split(":")[1];
                //请求eureka
                if(StringUtil.isNotBlank(servers)){
                    String[] serversArr=servers.split(",");
                    if(serversArr.length>0){
                        for (String s : serversArr) {
                            String appsServer = appsArr[i];
                            if (!flag) {
                                log.error("重载--根据servers从已剔除Redis清单里获取信息继续剔除服务：" + eurekaUrl + "/apps/" + appsArr[i] + "/" + ip_portArr[i] + "/status?value=OUT_OF_SERVICE");
                                restTemplate.put(eurekaUrl + "/apps/" + appsArr[i] + "/" + ip_portArr[i] + "/status?value=OUT_OF_SERVICE", new HashMap<>());
                                Map<String, Object> redisMap = new HashMap<>();
                                redisMap.put("ip", ip);
                                redisMap.put("port", port);
                                redisMap.put("app", appsServer);
                                redisMap.put("ipAddr", ip + ":" + port);
                                redisMap.put("operate", "reloadAnd" + s);
                                redisUtil.lSet("eurekaList", JSONObject.toJSONString(redisMap), 300);
                            }

                            Runnable newRunnable = () -> sendHttp(ip, port, appsServer, "reloadAnd" + s);
                            cachedThreadPool.execute(newRunnable);
                            //restTemplate.getForObject("http://"+ip+"/eurekaApp/"+port+"/"+appsArr[i].toLowerCase()+"/reloadAnd"+serversArr[x],JSONObject.class);
                        }
                    }
                }else{
                    String appsServer=appsArr[i];
                    if(!flag){
                    	log.error("重载--未传递servers，则根据apps从已剔除Redis清单里获取信息继续剔除服务：" + eurekaUrl+"/apps/"+appsArr[i]+"/"+ip_portArr[i]+"/status?value=OUT_OF_SERVICE");
                        restTemplate.put(eurekaUrl+"/apps/"+appsArr[i]+"/"+ip_portArr[i]+"/status?value=OUT_OF_SERVICE",new HashMap<>());
                        Map<String,Object> redisMap=new HashMap<>();
                        redisMap.put("ip",ip);
                        redisMap.put("port",port);
                        redisMap.put("app",appsServer);
                        redisMap.put("ipAddr",ip+":"+port.trim());
                        redisMap.put("operate","reload");
                        redisUtil.lSet("eurekaList",JSONObject.toJSONString(redisMap),300);
                    }

                    Runnable newRunnable = () -> sendHttp(ip,port,appsServer,"reload");
                    cachedThreadPool.execute(newRunnable);
                    //log.error("继续请求相应的服务器执行shell文件： " + "http://"+ip+":9510"+"/eurekaApp/"+port+"/"+appsArr[i].toLowerCase()+"/reload");
                    //restTemplate.getForObject("http://"+ip+":9510"+"/eurekaApp/"+port+"/"+appsArr[i].toLowerCase()+"/reload",JSONObject.class);
                }

            }
        }
        map.put("status","success");
        map.put("message","重启eureka成功");
        return map;
    }

    public String[] returnUserNameAndPassWord(){
        String url=eurekaUrl.replace("http://","");
        String urlArr=url.split("@")[0];
        return urlArr.split(":");
    }
}
