package com.matridx.igams.hrm.service.svcinterface;

import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.hrm.dao.entities.JpglDto;
import com.matridx.igams.hrm.dao.entities.JpglModel;

import java.util.Map;

public interface IJpglService extends BaseBasicService<JpglDto, JpglModel>{
    /**
     * 删除
     */
    boolean delAwardManagement(JpglDto jpglDto);
    /**
     * 新增保存
     */
    boolean addSaveAwardManagement(JpglDto jpglDto);
    /**
     * 修改保存
     */
    boolean modSaveAwardManagement(JpglDto jpglDto);
	 /*
        获取抽奖信息
     */
    JpglDto getLotteryInfo(String jpglid);
    /*
        获取抽奖结果
     */
    Map<String, Object> getLotteryResult(JpglDto jpglDto, User user) throws BusinessException;
}
