package com.matridx.bioinformation.util;

import com.alibaba.fastjson.JSONObject;
import com.amihaiemil.docker.Container;
import com.amihaiemil.docker.Containers;
import com.amihaiemil.docker.Docker;
import com.matridx.bioinformation.dao.entities.RwglDto;
import com.matridx.bioinformation.service.svcinterface.IRwglService;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SelectDockerStateThread extends Thread{

	private Logger log = LoggerFactory.getLogger(SelectDockerStateThread.class);

	private IXtszService xtszService;

	private IRwglService rwglService;

	private DockerUtil dockerUtil;

	private String dockerSock;

	private String filePath;


	public SelectDockerStateThread(IRwglService rwglService,IXtszService xtszService, DockerUtil dockerUtil,String dockerSock,String filePath) {
		this.rwglService = rwglService;
		this.xtszService = xtszService;
		this.dockerUtil = dockerUtil;
		this.dockerSock = dockerSock;
		this.filePath = filePath;
	}

	@Override
	public void run() {
		try {
			while(true) {
				TimeUnit.SECONDS.sleep(10);
//				// 查询任务列表
				List<RwglDto> rwglDtos = rwglService.getDtoList(new RwglDto());
				if(rwglDtos != null && rwglDtos.size() > 0) {
					log.error("运行中的任务数量："+ rwglDtos.size());
//					XtszDto xtszDto = xtszService.selectById("uri");
//					if (null == xtszDto || !StringUtil.isNotBlank(xtszDto.getSzz())) {
//						throw new Exception("未获取到URI!");
//					}
					List<String> strings= new ArrayList<>();
					Docker docker = dockerUtil.connectDocker(filePath+dockerSock);
					Containers containers = docker.containers();
					for ( Container container : containers) {
						log.error("遍历容器中的所有容器:" + container);
						JSONObject jsonObject = JSONObject.parseObject(String.valueOf(container));
						strings.add(jsonObject.get("Names").toString().split("\"")[1].substring(1));
					}
					List<RwglDto> endDtoList = rwglService.getEndDtoList(strings);
					if (null != endDtoList && endDtoList.size()>0){
						for (RwglDto rwglDto : endDtoList) {
							Container container = containers.get(rwglDto.getDocker());
							JsonObject jsonObject = container.inspect();
							String logs = ""+container.logs();
							log.error("logs:" + logs);
							log.error("inspect:" + jsonObject);
							rwglDto.setLogs(logs);
							rwglDto.setInspect(jsonObject.toString());
							JSONObject object = JSONObject.parseObject(jsonObject.get("State").toString());
							rwglDto.setRwzt("3");
							if (!"0".equals(object.get("ExitCode").toString())){
								rwglDto.setRwzt("4");
							}
							container.remove();
						}
						rwglService.updateList(endDtoList);
						log.error("docker线程处理："+strings.toString());
					}
				}else{
					log.info("更改系统配置:"+ 0);
					// 更改系统配置 0
					XtszDto xtszDto = new XtszDto();
					xtszDto.setSzlb("dockerState");
					xtszDto.setSzz("0");
					xtszDto.setOldszlb("dockerState");
					xtszService.update(xtszDto);
					return;
				}
			}
		} catch (Exception e) {
			log.info("线程出错！");
//			// 更改系统配置 0
			XtszDto xtszDto = new XtszDto();
			xtszDto.setSzlb("dockerState");
			xtszDto.setSzz("0");
			xtszDto.setOldszlb("dockerState");
			xtszService.update(xtszDto);
			e.printStackTrace();
		}
	}
}
