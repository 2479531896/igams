package com.matridx.igams.bioinformation.util;



import com.amihaiemil.docker.*;
import com.matridx.igams.bioinformation.dao.entities.DockerDto;
import com.matridx.igams.bioinformation.service.svcinterface.IDockerService;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.net.URI;
@Component
public class DockerUtil{

    @Autowired
    IXtszService xtszService;
    @Autowired
    IDockerService dockerService;
    private final Logger log = LoggerFactory.getLogger(DockerUtil.class);

    /**
     * 连接docker服务器
     */
    public Docker connectDocker(String url){

        Docker docker = new TcpDocker(URI.create(url));
        log.error("docker连接测试：" + url);
        return docker;
    }

    /**
     * 创建容器
     */
    public Container createContainers(String path,String data,String out_dir,String jgid) throws Exception {
        DockerDto dockerDto = new DockerDto();
        dockerDto.setDockerid(StringUtil.generateUUID());
        dockerDto.setJgid(jgid);
        dockerDto.setZt("0");
        XtszDto xtszDto = xtszService.selectById("uri");
        if (null == xtszDto || !StringUtil.isNotBlank(xtszDto.getSzz())) {
            throw new Exception("未获取到URI!");
        }
        Docker docker = connectDocker(xtszDto.getSzz());
//        Images images = docker.images();
//        Image image = images.pull("ggplot2", "latest");
        //构建CMD参数
        JsonArrayBuilder cmdJsonArray = Json.createArrayBuilder();
        JsonArrayBuilder binds = Json.createArrayBuilder();
        cmdJsonArray.add("Rscript");
        cmdJsonArray.add(path);
        cmdJsonArray.add(data);
        cmdJsonArray.add(out_dir);


        //构建volumes参数
        JsonObjectBuilder volumesObjectBuilder = Json.createObjectBuilder();
        //  /usr/share/fonts
        JsonObjectBuilder valArrayBuilder = Json.createObjectBuilder().add("bind","/usr/share/fonts").add("mode", "rw");
//        JsonObjectBuilder script_dir_Builder = Json.createObjectBuilder().add("bind","/code").add("mode", "rw");
        JsonObjectBuilder out_dir_Builder = Json.createObjectBuilder().add("bind",out_dir).add("mode", "rw");

        volumesObjectBuilder.add("/usr/share/fonts", valArrayBuilder.build());
        binds.add("/usr/share/fonts:/usr/share/fonts:rw");
//        volumesObjectBuilder.add(data, script_dir_Builder.build());
        binds.add("/home/centos/bioinfo/python:/code:rw");
//        binds.add("/code:/code:rw");
        volumesObjectBuilder.add(out_dir, out_dir_Builder.build());
        binds.add(out_dir+":"+out_dir+":rw");


        //构建HostConfig 参数
        JsonObjectBuilder hostConfig = Json.createObjectBuilder();



        hostConfig.add("Binds",binds.build());
        log.error("Binds参数：" + binds);
        //构建容器参数
        JsonObjectBuilder job = Json.createObjectBuilder().
                add("Image", "ggplot2:latest").
                add("HostConfig", hostConfig.build()).
                add("Cmd", cmdJsonArray.build()).
                add("Volumes", volumesObjectBuilder.build());
                //自定义Mac地址
//                add("MacAddress", "12:34:56:78:9a:bc").
                //保证容器启动后不关闭，守护式启动。定义标准输入流
                //三个参数分别为：是否附加到标准输入、将标准流附加到 TTY如果未关闭则包括stdin、打开标准输入
//                add("AttachStdin", true).
//                add("Tty", true).
//                add("OpenStdin", true);
        Container container = docker.containers().create(dockerDto.getDockerid(), job.build());
        log.error("创建容器成功信息为：" + container);
        try {
            container.start();
        } catch (Exception e) {
//            container.remove();
            throw new Exception(e.getMessage());
        }
        startSelectDOckerThread();
        dockerService.insert(dockerDto);
        return container;
    }

    public Boolean startSelectDOckerThread(){
        XtszDto xtszDto = xtszService.selectById("dockerState");
        if (null != xtszDto && StringUtil.isNotBlank(xtszDto.getSzz())){
            if ("0".equals(xtszDto.getSzz())){
                log.error("---------------开启线程---------------");
                DockerUtil dockerUtil = new DockerUtil();
                SelectDockerStateThread selectDockerStateThread = new SelectDockerStateThread(dockerService,xtszService,dockerUtil);
                selectDockerStateThread.start();
                XtszDto xtszDto1 = new XtszDto();
                xtszDto1.setSzlb("dockerState");
                xtszDto1.setSzz("1");
                xtszDto1.setOldszlb("dockerState");
                xtszService.update(xtszDto1);
            }
        }
        return true;
    }
}

