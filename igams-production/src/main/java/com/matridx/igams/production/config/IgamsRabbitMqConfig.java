package com.matridx.igams.production.config;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.production.dao.entities.ComputationUnitModel;
import com.matridx.igams.production.dao.matridxsql.IMatridxInventoryDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.XxdlcwglDto;
import com.matridx.igams.common.service.svcinterface.IXxdlcwglService;
import com.matridx.igams.production.dao.entities.WlglDto;
import com.matridx.igams.production.service.svcinterface.IWlglService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridxapp.production.config.ProductionRabbitMqReceive;
		
@Configuration
public class IgamsRabbitMqConfig {
	@Autowired
	IWlglService wlglService;
	@Autowired
	IXxdlcwglService xxdlcwglService;
	@Value("${matridx.rabbit.flg:}")
	private String rabbitFlg;
	@Value("${matridx.rabbit.systemreceiveflg:}")
	private String systemreceiveflg;

	
	private final Logger log = LoggerFactory.getLogger(ProductionRabbitMqReceive.class);

	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.wlgl.insertWlgl${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void syncInsertWlgl(String str) {
		try {
			log.error(rabbitFlg + " sys.igams.wlgl.insertWlgl:"+str);
			WlglDto wlglDto = JSONObject.parseObject(str, WlglDto.class);
			wlglService.syncWlgl(wlglDto);
		} catch (Exception e) {
			log.error("sys.igams.wlgl.insertWlgl:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.wlgl.insertWlgl");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}


	}
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.wlgl.updateWlgl(deleteWlgl,updateKcwh)${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void syncUpOrDelWlgl(String str) {
		try {
			log.error(rabbitFlg + " sys.igams.wlgl.syncUpOrDelWlgl:"+str);
			WlglDto wlglDto = JSONObject.parseObject(str, WlglDto.class);
			if ("updateWlgl".equals(wlglDto.getSyncFlag())){
				wlglService.syncUpdateWlgl(wlglDto);
			}else if ("deleteWlgl".equals(wlglDto.getSyncFlag())){
				wlglService.synclDeteWlgl(wlglDto);
			}else if ("updateKcwh".equals(wlglDto.getSyncFlag())){
				wlglService.syncUpdateKcwh(wlglDto);
			}
		} catch (Exception e) {
			log.error("sys.igams.wlgl.syncUpOrDelWlgl:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.wlgl.syncUpOrDelWlgl");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}


	}
	/**
	 * 物料信息新增，修改
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.wlgl.update${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void insertOrUpdateWlgl(String str) {
		log.error(rabbitFlg + " Receive wlgl-update:"+str);
        try {
			WlglDto wlglDto = JSONObject.parseObject(str, WlglDto.class);
			if(!rabbitFlg.equals(wlglDto.getPrefix())){
				wlglService.insertOrUpdateWlgl(wlglDto);
				XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
				xxdlcwglDto.setCwid(StringUtil.generateUUID());
				xxdlcwglDto.setCwlx("sys.igams.wlgl.update");
				if(str.length() > 4000){
					str = str.substring(0, 4000);
				}
				xxdlcwglDto.setYsnr(str);
				xxdlcwglService.insert(xxdlcwglDto);
			}
		} catch (Exception e) {
			log.error("Receive wlgl-update:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.wlgl.update");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
	
	
	/**
	 * 物料信息删除
	 */
	@RabbitListener(queues = ("${matridx.rabbit.preflg:}sys.igams.wlgl.del${matridx.rabbit.flg:}"), containerFactory="defaultFactory")
	public void deleteWlgl(String str) {
		log.debug(rabbitFlg + " Receive wlgl-del:"+str);
        try {
			WlglDto wlglDto = JSONObject.parseObject(str, WlglDto.class);
			if(!rabbitFlg.equals(wlglDto.getPrefix())){
				wlglService.delete(wlglDto);
				XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
				xxdlcwglDto.setCwid(StringUtil.generateUUID());
				xxdlcwglDto.setCwlx("sys.igams.wlgl.del");
				if(str.length() > 4000){
					str = str.substring(0, 4000);
				}
				xxdlcwglDto.setYsnr(str);
				xxdlcwglService.insert(xxdlcwglDto);
			}
		} catch (Exception e) {
			log.error("Receive wlgl-del:"+e.getMessage());
			// TODO Auto-generated catch block
			XxdlcwglDto xxdlcwglDto = new XxdlcwglDto();
			xxdlcwglDto.setCwid(StringUtil.generateUUID());
			xxdlcwglDto.setCwlx("sys.igams.wlgl.del");
			if(str.length() > 4000){
				str = str.substring(0, 4000);
			}
			xxdlcwglDto.setYsnr(str);
			xxdlcwglService.insert(xxdlcwglDto);
		}
	}
}
