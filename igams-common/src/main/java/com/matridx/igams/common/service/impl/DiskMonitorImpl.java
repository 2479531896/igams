package com.matridx.igams.common.service.impl;

import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.post.IDdxxglDao;
import com.matridx.igams.common.service.svcinterface.DiskMonitorService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DiskMonitorImpl implements DiskMonitorService{

	@Autowired
	DingTalkUtil talkUtil;
	
	@Autowired
	IDdxxglDao ddxxDao;
	
	@Autowired
	private IJcsjService jcsjService;
	
	@Autowired
	IXxglService xxglService;
	private final Logger log = LoggerFactory.getLogger(DiskMonitorImpl.class);
	
	@Value("${matridx.systemflg.systemname:}")
	private String systemname;
	@Value("${matridx.systemflg.cens:}")
	private String cens;
	/**
	 * 监测磁盘的大小
	 */
  public void getDiskMonitor() {
	  Map<String, Object> map = getDeskUsageLinux();
	  String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
      log.info("读取硬盘的内存成功！"+ LocalDateTime.now()+ "硬盘总内存："+map.get("total")+",已使用："+map.get("used")+",使用率："+map.get("use_rate")+",系统："+systemname);
	  if(map.get("use_rate")!=null&&!map.get("use_rate").equals("")) {
		  String use_rate = map.get("use_rate").toString().replace('%', ' ').trim();
		  if(Integer.parseInt(use_rate)>=90) {
			  //发送钉钉消息
			  JcsjDto jcsjDto = new JcsjDto();
			  jcsjDto.setJclb("DINGMESSAGETYPE");
				jcsjDto.setCsdm("DISK_OUT");
				List<DdxxglDto> ddxxList=ddxxDao.selectByDdxxlx(jcsjService.getDtoByCsdmAndJclb(jcsjDto).getCsdm());
				if(ddxxList!=null && !ddxxList.isEmpty()) {
                    for (DdxxglDto ddxxglDto : ddxxList) {
//						String token =talkUtil.getToken();
                        if (ddxxglDto.getDdid() != null && StringUtil.isNotBlank(ddxxglDto.getDdid())) {
                            talkUtil.sendWorkMessage(ddxxglDto.getYhm(), ddxxglDto.getDdid(), xxglService.getMsg("ICOMM_DO0010"), xxglService.getMsg("ICOMM_DO0020", map.get("use_rate").toString(), map.get("total").toString(), map.get("used").toString(), time, systemname));
                            log.info("发送钉钉消息成功！" + LocalDateTime.now() + "硬盘总内存" + map.get("total") + ",已使用：" + map.get("used") + ",使用率：" + map.get("use_rate") + ",系统：" + systemname);
                        }
                    }
				}
		  }
	  }
  }
  public  Map<String, Object> getDeskUsageLinux() {
      Map<String, Object> map = new HashMap<>();
      try {
          Runtime rt = Runtime.getRuntime();
          String ml = "df -hl ";
          if(StringUtil.isBlank(cens)) {
        	  cens ="centos-home";
          }
          Process p = rt.exec(ml);// df -hl 查看硬盘空间
          log.info("读取硬盘的内存命令为："+ml+",时间为："+ LocalDateTime.now());
          BufferedReader in = null;
          try {
              in = new BufferedReader(new InputStreamReader(
                      p.getInputStream()));
              String str;
              String[] strArray;
              StringBuilder allLineString = new StringBuilder();
              while ((str = in.readLine()) != null) {
                  allLineString.append(str).append(";");
                  //不是指定磁盘不读取
                  if(!str.contains(cens))
                	  continue;
                  int m = 0;
                  strArray = str.split(" ");
                  for (String para : strArray) {
                      if (para.trim().isEmpty())
                          continue;
                      ++m;
                      if (para.endsWith("G") || para.endsWith("Gi")) {
                          // 目前的服务器
                          if (m == 2) {
                        	  map.put("total",para);
                          }
                          if (m == 3) {
                        	  map.put("used",para);
                          }
                      }
                      if (para.endsWith("%")) {
                          if (m == 5) {
                        	  map.put("use_rate",para);
                          }
                      }
                  }
              }
              log.info("读取硬盘大小总记录为："+allLineString+",时间为："+ LocalDateTime.now());
          } catch (Exception e) {
              e.printStackTrace();
          } finally {
              in.close();
          }
      } catch (Exception e) {
          e.printStackTrace();
      }
      return map;

  }
  
  
}
