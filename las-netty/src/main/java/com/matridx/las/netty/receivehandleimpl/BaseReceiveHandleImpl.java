package com.matridx.las.netty.receivehandleimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.channel.command.IReceiveHandle;
import com.matridx.las.netty.channel.server.handler.MatridxProtocolHandler;
import com.matridx.las.netty.dao.entities.YqdzxxDto;
import com.matridx.las.netty.dao.entities.YqztxxDto;
import com.matridx.las.netty.service.svcinterface.IYqdzxxService;
import com.matridx.las.netty.service.svcinterface.IYqztxxService;
import com.matridx.las.netty.util.SpringUtil;
import com.matridx.springboot.util.base.StringUtil;

public class BaseReceiveHandleImpl implements IReceiveHandle {
	Logger log = LoggerFactory.getLogger(MatridxProtocolHandler.class);
	/**
	 * 处理发送过来的状态信息
	 */
	@Transactional
	public void handleStateMesg(FrameModel reModel) {
		IYqdzxxService yqdzxxService = (IYqdzxxService) SpringUtil.getBean("yqdzxxServiceImpl");
		IYqztxxService yqztxxService = (IYqztxxService) SpringUtil.getBean("yqztxxServiceImpl");

		// 收到信息后先将动作表更新
		if (reModel.getCmd().equals("STATE")) {
			JSONObject rejsonObject = JSONObject.parseObject(reModel.getCmdParam()[0]);
			YqdzxxDto yqdzxxDto = new YqdzxxDto();
			yqdzxxDto.setDzid(StringUtil.generateUUID());
			yqdzxxDto.setYqid(reModel.getDeviceID());
			yqdzxxDto.setYqlx(reModel.getCommand());
			yqdzxxDto.setDzzt(rejsonObject.get("Action").toString());
			if (rejsonObject.get("Result") != null) {
				yqdzxxDto.setSbcs(rejsonObject.get("Result").toString());
			}
			yqdzxxDto.setSbsj(rejsonObject.get("Date").toString());
			yqdzxxService.insert(yqdzxxDto);
			// 更新仪器状态
			YqztxxDto yqztxxDto = new YqztxxDto();
			yqztxxDto.setSbid(reModel.getDeviceID());
			yqztxxDto.setYqzt(rejsonObject.get("Action").toString());
			log.info("保存动作："+yqdzxxDto.getSbcs());
			yqztxxService.updateStYqztxx(yqztxxDto.getSbid(), rejsonObject.get("Action").toString());

		}
	}

	public JSONObject setBackData(String backdata) {
		JSONObject jsonObject = new JSONObject();
		String[] bs = backdata.split(",");
		for (String s : bs) {
			if (StringUtil.isNotBlank(s)) {
				String[] baks = s.split(":");
				if (baks != null && baks.length > 1) {
					jsonObject.put(baks[0], baks[1]);

				}
			}
		}
		return jsonObject;
	}
}