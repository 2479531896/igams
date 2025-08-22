package com.matridx.las.netty.dobackimpl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matridx.las.netty.channel.command.FrameModel;
import com.matridx.las.netty.dao.entities.CubsMaterialModel;
import com.matridx.las.netty.dao.entities.MlpzDto;
import com.matridx.las.netty.dao.entities.SjlcDto;
import com.matridx.las.netty.dao.entities.YblcsjDto;
import com.matridx.las.netty.global.material.InstrumentMaterialGlobal;
import com.matridx.las.netty.service.svcinterface.IMlpzService;
import com.matridx.las.netty.service.svcinterface.IYblcsjService;
import com.matridx.las.netty.util.SpringUtil;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class CubicsStpCallBackImpl extends BaseCallBackImpl  {
	@Autowired
	private IMlpzService mlpzService;
	/**
	 * 回调函数回调处理,启动停止，连接等
	 * 
	 * @param times     返回次数：1 第一次 2 第二次
	 * @param recModel  返回的帧信息
	 * @param sendModel 发送的帧信息
	 * @return 返回结果
	 */


	public boolean callfunc(int times, FrameModel recModel, FrameModel sendModel,SjlcDto sjlcDto) {
		try {

			//获取命令对象
			if(StringUtil.isNotBlank(sendModel.getMlid())) {
				// 根据返回信息处理
				MlpzDto mlpzDto = new MlpzDto();
				mlpzDto.setMlid(sendModel.getMlid());

				mlpzDto = mlpzService.getDtoById(sendModel.getMlid());
				//String deviceID = mlpzDto.getXylx();
				if (StringUtil.isNotBlank(mlpzDto.getMethodname()) && StringUtil.isNotBlank(mlpzDto.getParamlx())){
					if ("cubicsStart".equals(mlpzDto.getMethodname())){
						switch (mlpzDto.getParamlx()) {
							case "cubicsStart":
								CubsMaterialModel cubsMaterialModel = new CubsMaterialModel();
								cubsMaterialModel.setPassId(recModel.getPassId());
								cubsMaterialModel = InstrumentMaterialGlobal.getCubsMaterial(cubsMaterialModel);
								//更新建库仪开始时间
								//更新夹取托盘时间
								IYblcsjService yblcsjService = (IYblcsjService) SpringUtil.getBean("yblcsjService");
								YblcsjDto yblcsjDto = new YblcsjDto();
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
								Date date = new Date();
								yblcsjDto.setJqrztpsj(sdf.format(date));
								if(null!=cubsMaterialModel){
									yblcsjDto.setYsybid(cubsMaterialModel.getYsybid());
								}
								yblcsjService.updateYblcsjById(yblcsjDto);
								break;
						}
					}

				}
			}

			return recModel.getCmd().equals("OK");
		}catch (Exception e){
			log.error(String.format("Cub命令回调检查错误%s", e.getMessage()));
			log.error(String.format("出错流程类型id%s", sjlcDto.getLclx()));
			log.error(String.format("出错事件id%s", sjlcDto.getSjid()));
		}
		return true;
	}

	

}
