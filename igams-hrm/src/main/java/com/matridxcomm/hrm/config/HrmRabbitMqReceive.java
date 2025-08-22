package com.matridxcomm.hrm.config;

import com.matridx.igams.common.dao.entities.XxdlcwglDto;
import com.matridx.igams.common.service.svcinterface.IXxdlcwglService;
import com.matridx.igams.hrm.service.svcinterface.IGrksglService;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HrmRabbitMqReceive {

	/**
	 * FTP服务器地址
	 * 在SpringBoot中使用@Value只能给普通变量赋值，不能给静态变量赋值
	 */
	@Autowired
	IXxdlcwglService xxdlcwglService;
	@Autowired
	IGrksglService grksglService;
	private final Logger log = LoggerFactory.getLogger(HrmRabbitMqReceive.class);
	/**
	 * 个人新增
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.grks.submit${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertGrksgl(String str) {
		try {
			grksglService.submitScore(str);
		} catch (Exception e) {
			log.error("prefixFlg grksgl.insert:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.grks.submit");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}


}
