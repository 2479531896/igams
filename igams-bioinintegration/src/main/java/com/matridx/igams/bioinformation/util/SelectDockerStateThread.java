package com.matridx.igams.bioinformation.util;

import com.alibaba.fastjson.JSONObject;
import com.amihaiemil.docker.Container;
import com.amihaiemil.docker.Containers;
import com.amihaiemil.docker.Docker;
import com.matridx.igams.bioinformation.dao.entities.DockerDto;
import com.matridx.igams.bioinformation.service.svcinterface.IDockerService;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class SelectDockerStateThread extends Thread {

	private final Logger log = LoggerFactory.getLogger(SelectDockerStateThread.class);

	private IXtszService xtszService;

	private IDockerService dockerService;

	private DockerUtil dockerUtil;

	public SelectDockerStateThread(IDockerService dockerService, IXtszService xtszService, DockerUtil dockerUtil) {
		this.dockerService = dockerService;
		this.xtszService = xtszService;
		this.dockerUtil = dockerUtil;
	}

	@Override
	public void run() {
		try {
			while(true) {
				TimeUnit.SECONDS.sleep(10);
//				// 查询任务列表
				List<DockerDto> dockerDtos = dockerService.getDtoList(new DockerDto());
				if(dockerDtos != null && dockerDtos.size() > 0) {
					log.error("运行中的任务数量："+ dockerDtos.size());
					List<String> strings= new ArrayList<>();
					XtszDto xtszDto = xtszService.selectById("uri");
					if (null == xtszDto || !StringUtil.isNotBlank(xtszDto.getSzz())) {
						throw new Exception("未获取到URI!");
					}
					Docker docker = dockerUtil.connectDocker(xtszDto.getSzz());
					Containers containers = docker.containers();
					for ( Container container : containers) {
						log.error("遍历容器中的所有容器:" + container);
						JSONObject jsonObject = JSONObject.parseObject(String.valueOf(container));
						strings.add(jsonObject.get("Names").toString().split("\"")[1].substring(1));
					}

					List<DockerDto> endDtoList = dockerService.getEndDtoList(strings);
					if (null != endDtoList && endDtoList.size()>0){
						for (DockerDto dockerDto : endDtoList) {
							Container container = containers.get(dockerDto.getDockerid());
							JsonObject jsonObject = container.inspect();
							log.error("logs:" + container.logs());
							log.error("inspect:" + jsonObject);
							dockerDto.setLogs(String.valueOf(container.logs()));
							dockerDto.setInspect(jsonObject.toString());
							JSONObject object = JSONObject.parseObject(jsonObject.get("State").toString());
							dockerDto.setZt("1");
							if (!"0".equals(object.get("ExitCode").toString())){
								dockerDto.setZt("2");
							}
							container.remove();
						}
						dockerService.updateList(endDtoList);
						log.error("docker线程处理："+ strings);
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
			log.info("更改系统配置:"+ 0);
			// 更改系统配置 0
			XtszDto xtszDto = new XtszDto();
			xtszDto.setSzlb("dockerState");
			xtszDto.setSzz("0");
			xtszDto.setOldszlb("dockerState");
			xtszService.update(xtszDto);
			e.printStackTrace();
		}
	}
}
