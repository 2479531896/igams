package com.matridx.las.netty.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.ShxxDto;
import com.matridx.igams.common.dao.entities.XxdlcwglDto;
import com.matridx.igams.common.dao.post.IShxxDao;
import com.matridx.igams.common.service.svcinterface.IXxdlcwglService;
import com.matridx.springboot.util.base.StringUtil;

@Component
public class NettyMqReceive {
	
	@Autowired
	IXxdlcwglService xxdlcwglService;
	@Value("${matridx.rabbit.flg:}")
	private String prefixFlg;
	@Value("${matridx.rabbit.receiveflg:}")
	private String receiveflg;
	@Autowired
	private IShxxDao shxxDao;
	
	private Logger log = LoggerFactory.getLogger(NettyMqReceive.class);
	
	/**
	 * 审核信息新增
	 * @param str
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.shxx.insert${matridx.rabbit.flg:}"))
	public void insertShxx(String str) {
		log.error(prefixFlg + " Receive shxx-insert:"+str);
        try {
        	ShxxDto shxx = JSONObject.parseObject(str, ShxxDto.class);
        	if("1".equals(receiveflg)) {
        		if(!prefixFlg.equals(shxx.getPrefixFlg())){
        			shxxDao.insert(shxx);
            	}
        	}       	
		} catch (Exception e) {
			log.error("sys.igams.shxx.insert:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.shxx.insert");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
}
