package com.matridx.igams.wechat.service.svcinterface;

import com.matridx.igams.common.dao.entities.WkmxPcrModel;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.wechat.dao.entities.SjyzmxDto;
import com.matridx.igams.wechat.dao.entities.SjyzmxModel;

public interface ISjyzmxService extends BaseBasicService<SjyzmxDto, SjyzmxModel>{

	/**
	 * 根据验证id查询最大序号
	 * @param yzid
	 * @return
	 */
	int getMaxXh(String yzid);
	/**
	 * 接受并保存pcr实验返回的消息
	 * @param wkmxPcrModel
	 * @return
	 */
	boolean getSjyzmxResult(WkmxPcrModel wkmxPcrModel);
}
