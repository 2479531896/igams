package com.matridx.bioinformation.util;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amihaiemil.docker.Container;
import com.amihaiemil.docker.Containers;
import com.amihaiemil.docker.Docker;
import com.amihaiemil.docker.Image;
import com.amihaiemil.docker.Images;
import com.amihaiemil.docker.UnixDocker;
import com.matridx.bioinformation.dao.entities.RwglDto;
import com.matridx.bioinformation.service.svcinterface.IRwglService;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@Component
public class DockerUtil implements ApplicationRunner {
    @Autowired
    IRwglService rwglService;
    @Autowired
    IXtszService xtszService;
    @Value("${matridx.operate.timeout:}")
    private String timeout;
    @Value("${matridx.operate.dockerSock:}")
    private String dockerSock;
    @Value("${matridx.operate.filePath:}")
    private String filePath;
    private Logger log = LoggerFactory.getLogger(DockerUtil.class);

    /**
     * 连接docker服务器
     *
     * @return
     */
    public Docker connectDocker(String dockerSock) throws Exception {
//        Docker docker = new TcpDocker(URI.create(uri));
//        log.error("docker连接测试：" + uri);
        Docker docker = new UnixDocker(
                new File(dockerSock)     //this is the default path to Docker's unix socket
        );
        log.error("docker连接测试：" + dockerSock);
        return docker;
    }

    /**
     * 获取images列表
     *
     * @return
     */
    public List<String> images() throws Exception {
//        XtszDto xtszDto = xtszService.selectById("uri");
//        if (null == xtszDto || !StringUtil.isNotBlank(xtszDto.getSzz())) {
//            throw new Exception("未获取到URI!");
//        }
//        log.error("uri:"+xtszDto.getSzz());
        Docker docker = connectDocker(filePath+dockerSock);
        Images images = docker.images();
        log.error("images："+images.toString());
        List<String> strings = new ArrayList<>();
        for (Image image : images) {
            log.error("遍历容器中的所有镜像:" + image);
            JSONObject jsonObject = JSONObject.parseObject(String.valueOf(image));
            if (null != jsonObject.get("RepoTags")){
                JSONArray repoTags = JSONArray.parseArray(jsonObject.get("RepoTags").toString());
                if (repoTags != null && repoTags.size() > 0) {
                    for (Object js : repoTags) {
                        if (js != null) {
                            log.error("RepoTags：" + js.toString());
                            strings.add(js.toString());
                        }
                    }
                }
            }
        }
        return strings;
    }

    /**
     * 创建容器
     *
     * @param
     * @return
     */
    public Container createContainers(RwglDto rwglDto) throws Exception {
//        if (StringUtil.isBlank(rwglDto.getUri())) {
//            throw new Exception("未获取到URI!");
//        }
        Docker docker = connectDocker(filePath+dockerSock);
//        Images images = docker.images();
//        Image image = null;
//        FutureTask<Image>task = new FutureTask<Image>(new Callable<Image>() {
//            public Image call() throws Exception {
//                String[] split = rwglDto.getImage().split(":");
//                Image image = images.pull(split[0], split[1]);
//                return image;
//            }
//        });
//        Thread t = new Thread(task);
//        t.start();
//        image = task.get(Long.parseLong(insertTimeout), TimeUnit.MILLISECONDS); //允许阻塞等待方法处理1秒，超出则抛异常
//        log.error("image："+String.valueOf(image));
//        log.error(String.valueOf("image.getState："+t.getState()));
        JSONObject jsonObject = JSONObject.parseObject(rwglDto.getSj());
        //构建CMD参数
        JsonArrayBuilder cmdJsonArray = Json.createArrayBuilder();
        String[] strings = jsonObject.get("entrypoint").toString().split(" ");
        for (String string : strings) {
            cmdJsonArray.add(string);
            log.error("entrypoint参数：" + string);
        }

        JsonArrayBuilder binds = Json.createArrayBuilder();
        if (null != jsonObject.get("args")){
            JSONArray jsonArray = JSONArray.parseArray(jsonObject.get("args").toString());
            if (jsonArray != null && jsonArray.size() > 0) {
                for (Object js : jsonArray) {
                    if (js != null) {
                        String val = js.toString();
                        if (StringUtil.isNotBlank(val)){
                            if (val.split("/").length>1){
                                binds.add(val+":"+val+":rw");
                            }
                            cmdJsonArray.add(val);
                            log.error("args参数：" + js.toString());
                        }

                    }
                }
            }
        }
        if (null != jsonObject.get("kwargs")){
            JSONObject kwargsJson = JSONObject.parseObject(jsonObject.get("kwargs").toString());
            Iterator<String> iterator = kwargsJson.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String val = kwargsJson.get(key).toString();
                if (StringUtil.isNotBlank(val)){
                    if ("false".equals(val))
                        break;
                    if (val.split("/").length>1){
                        binds.add(key+":"+val+":rw");
                    }
                    if (key.split("").length>1){
                        if ("true".equals(val)){
                            cmdJsonArray.add("--"+key);
                        }else{
                            cmdJsonArray.add("--"+key);
                            cmdJsonArray.add(val);
                        }

                    }else{
                        if ("true".equals(val)){
                            cmdJsonArray.add("-"+key);
                        }else{
                            cmdJsonArray.add("-"+key);
                            cmdJsonArray.add(val);
                        }
                    }
                    log.error("kwargs参数："+key+ ":" + val);
                }


            }
        }
        //构建env参数
        JsonArrayBuilder envArrayBuilder = Json.createArrayBuilder();
        if (null!=jsonObject.get("env")){
            JSONObject envJson = JSONObject.parseObject(jsonObject.get("env").toString());
            Iterator<String> envIterator = envJson.keySet().iterator();
            while (envIterator.hasNext()) {
                String key = envIterator.next();
                String val = envJson.get(key).toString();
                envArrayBuilder.add(key + "=" + val);
                log.error("env参数：" + key + "=" + val);
            }
        }

        //构建volumes参数
        JsonObjectBuilder volumesObjectBuilder = Json.createObjectBuilder();
        if (null != jsonObject.get("volumes")){
            JSONObject volumesJson = JSONObject.parseObject(jsonObject.get("volumes").toString());
            Iterator<String> volumesIterator = volumesJson.keySet().iterator();
            while (volumesIterator.hasNext()) {
                String key = volumesIterator.next();
                JSONObject valJson = JSONObject.parseObject(volumesJson.get(key).toString());
                JsonObjectBuilder valArrayBuilder = Json.createObjectBuilder().
                        add("bind", valJson.get("bind").toString()).add("mode", valJson.get("mode").toString());
                binds.add(key+":"+valJson.get("bind").toString()+":"+valJson.get("mode").toString());
                volumesObjectBuilder.add(key, valArrayBuilder.build());
                log.error("volumes参数：" + key + ":" + valJson);
            }
        }
        //构建HostConfig 参数
        JsonObjectBuilder hostConfig = Json.createObjectBuilder();
        //构建options参数
        if (null != jsonObject.get("options")){
            JSONObject optionsJson = JSONObject.parseObject(jsonObject.get("options").toString());
            log.error("options参数：" + optionsJson.toJSONString());
            hostConfig.add("CpuShares", jsonObject.get("cpuShares")==null ? 0 : Integer.parseInt(optionsJson.get("cpuShares").toString())).
                    add("Memory", jsonObject.get("memory")==null ? 0 :Integer.parseInt(optionsJson.get("memory").toString())).
                    add("Privileged", jsonObject.get("privileged")==null ? false :"true".equals(optionsJson.get("privileged").toString()));
//                    add("AutoRemove", false).
//                    add("Dns", Json.createArrayBuilder().add("8.8.8.8").add("4.4.4.4").build());
        }

        if (null != jsonObject.get("is_daemon")){
            if ("true".equals(jsonObject.get("is_daemon").toString())) {
                JsonObjectBuilder restartPolicy = Json.createObjectBuilder().
                        add("Name", "always").
                        add("MaximumRetryCount", 0);
                hostConfig.add("RestartPolicy", restartPolicy.build());
                log.error("RestartPolicy参数：RestartPolicy:{Name:always,MaximumRetryCount:0}");
            }
        }

        hostConfig.add("Binds",binds.build());
        log.error("Binds参数：" + binds.build());
        //构建容器参数
        JsonObjectBuilder job = Json.createObjectBuilder().
                add("Image", rwglDto.getImage()).
                add("HostConfig", hostConfig.build()).
                add("Cmd", cmdJsonArray.build()).
                add("Volumes", volumesObjectBuilder.build()).
                add("Env", envArrayBuilder.build());
                //自定义Mac地址
//                add("MacAddress", "12:34:56:78:9a:bc").
                //保证容器启动后不关闭，守护式启动。定义标准输入流
                //三个参数分别为：是否附加到标准输入、将标准流附加到 TTY如果未关闭则包括stdin、打开标准输入
//                add("AttachStdin", true).
//                add("Tty", true).
//                add("OpenStdin", true);
        Container container = docker.containers().create(rwglDto.getDocker(), job.build());
        log.error("创建容器成功信息为：" + container);
        return container;
    }


    /**
     * 操作容器
     */

    public void operateContainer(RwglDto rwglDto) throws Exception {
        if (StringUtil.isBlank(rwglDto.getOperate())){
            throw new BusinessException("msg","未获取到操作！");
        }
        if ("remove".equals(rwglDto.getOperate())){
            rwglDto.setScbj("1");
            rwglDto.setScry(rwglDto.getXgry());
            rwglDto.setXgry(null);
        }else if (!"rename".equals(rwglDto.getOperate())){
//            XtszDto xtszDto = xtszService.selectById("uri");
//            if (null == xtszDto || !StringUtil.isNotBlank(xtszDto.getSzz())) {
//                throw new Exception("未获取到URI!");
//            }
            if ("start".equals(rwglDto.getOperate())){
//                rwglDto.setUri(xtszDto.getSzz());
                rwglDto.setDocker(StringUtil.generateUUID());
                createContainers(rwglDto);
            }
            if (StringUtil.isBlank(rwglDto.getDocker())) {
                throw new Exception("未获取到容器名称!");
            }
            Docker docker = connectDocker(filePath+dockerSock);
            Containers containers = docker.containers();
            Container container = containers.get(rwglDto.getDocker());
            FutureTask<Boolean>task = new FutureTask<Boolean>(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    switch (rwglDto.getOperate()) {
                        case "start":
                            rwglDto.setRwzt("1");
                            startSelectDOckerThread();
                            log.info("正在执行开始操作", rwglDto.getOperate());
                            try {
                                container.start();
                            } catch (Exception e) {
                                container.remove();
                                throw new Exception(e.getMessage());
                            }
                            break;
                        case "stop":
                            rwglDto.setDocker(null);
                            rwglDto.setSj(null);
                            rwglDto.setRwzt("0");
                            log.info("正在执行停止操作", rwglDto.getOperate());
                            try {
                                container.stop();
                                container.remove();
                            } catch (Exception e) {
                                rwglDto.setRwzt("0");
                                rwglService.update(rwglDto);
                                try {
                                    container.remove();
                                } catch (Exception ex) {

                                }
                            }
                            break;
                        case "kill":
                            rwglDto.setDocker(null);
                            rwglDto.setSj(null);
                            rwglDto.setRwzt("0");
                            log.info("正在执行杀死操作", rwglDto.getOperate());
                            try {
                                container.kill();
                                container.remove();
                            } catch (Exception e) {
                                rwglDto.setRwzt("0");
                                rwglService.update(rwglDto);
                                try {
                                    container.remove();
                                } catch (Exception ex) {

                                }

                            }
                            break;
//                        case "restart":
//                            rwglDto.setRwzt("1");
//                            startSelectDOckerThread();
//                            log.info("正在执行重启操作", rwglDto.getOperate());
//                            container.restart();
//                            break;
//                        case "rename":
//                            rwglDto.setRwmc(rwglDto.getNewrwmc());
//                            log.info("正在执行重命名操作", rwglDto.getOperate());
//                            container.rename(rwglDto.getNewrwmc());
//                            break;
                        case "pause":
                            rwglDto.setDocker(null);
                            rwglDto.setSj(null);
                            rwglDto.setRwzt("2");
                            log.info("正在执行暂停操作", rwglDto.getOperate());
                            try {
                                container.pause();
                            } catch (Exception e) {
                                rwglDto.setRwzt("3");
                                rwglService.update(rwglDto);
                                try {
                                    container.remove();
                                } catch (Exception ex) {

                                }
                            }
                            break;
                        case "unpause":
                            rwglDto.setDocker(null);
                            rwglDto.setSj(null);
                            rwglDto.setRwzt("1");
                            log.info("正在执行取消暂停操作", rwglDto.getOperate());
                            try {
                                container.unpause();
                            } catch (Exception e) {
                                rwglDto.setRwzt("3");
                                rwglService.update(rwglDto);
                                try {
                                    container.remove();
                                } catch (Exception ex) {

                                }
                            }
                            break;
                        case "remove":
                            rwglDto.setScbj("1");
                            rwglDto.setScry(rwglDto.getXgry());
                            rwglDto.setXgry(null);
                            log.info("正在执行移除操作", rwglDto.getOperate());
                            break;
                        default:
                            throw new BusinessException(rwglDto.getOperate()+"操作过程中失败！");
                    }
                    return true;  //返回处理结果
                }
            });
            Thread t = new Thread(task);
            t.start();
            boolean bl = task.get(Long.parseLong(timeout), TimeUnit.MILLISECONDS); //允许阻塞等待方法处理1秒，超出则抛异常
            log.error("bl："+String.valueOf(bl));
            log.error(String.valueOf("t.getState："+t.getState()));
            if (!bl){
                throw new BusinessException("操作失败！");
            }
        }
        Boolean success =  rwglService.update(rwglDto);
        if (!success){
            throw new BusinessException("数据库修改失败！");
        }
    }

    public Boolean startSelectDOckerThread(){
        XtszDto xtszDto = xtszService.selectById("dockerState");
        if (null != xtszDto && StringUtil.isNotBlank(xtszDto.getSzz())){
            if ("0".equals(xtszDto.getSzz())){
                log.error("---------------开启线程---------------");
                DockerUtil dockerUtil = new DockerUtil();
                SelectDockerStateThread selectDockerStateThread = new SelectDockerStateThread(rwglService,xtszService,dockerUtil,dockerSock,filePath);
                selectDockerStateThread.start();
                XtszDto xtszDto1 = new XtszDto();
                xtszDto1.setSzlb("dockerState");
                xtszDto1.setSzz("1");
                xtszDto1.setOldszlb("dockerState");
                xtszService.update(xtszDto1);
            }else{
                return false;
            }
        }
        return true;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.error("---------------docker查询状态线程---------------");
        XtszDto xtszDto = xtszService.selectById("dockerState");
        if (null != xtszDto && StringUtil.isNotBlank(xtszDto.getSzz())){
            if ("1".equals(xtszDto.getSzz())){
                log.error("---------------开启线程---------------");
                DockerUtil dockerUtil = new DockerUtil();
                SelectDockerStateThread selectDockerStateThread = new SelectDockerStateThread(rwglService,xtszService,dockerUtil,dockerSock,filePath);
                selectDockerStateThread.start();
            }
        }
    }
}

