package com.matridx.igams.experiment.service.svcinterface;

import com.matridx.igams.common.service.BaseBasicService;
import com.matridx.igams.experiment.dao.entities.ZdhYhxxDto;
import com.matridx.igams.experiment.dao.entities.ZdhYhxxModel;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface IZdhYhxxService extends BaseBasicService<ZdhYhxxDto, ZdhYhxxModel> {


    String qrCodeGenerate(String qRCodeSettingId);
    Map<String,Object> qrCodeAnalysis(String qRCodeSettingId,String wechatid, HttpServletRequest request);
}
