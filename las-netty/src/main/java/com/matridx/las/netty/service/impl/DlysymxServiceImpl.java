package com.matridx.las.netty.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.common.dao.entities.WkmxPcrModel;
import com.matridx.igams.common.dao.entities.WkmxPcrResultModel;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.las.netty.dao.entities.DlysymxDto;
import com.matridx.las.netty.dao.entities.DlysymxModel;
import com.matridx.las.netty.dao.entities.WkmxDto;
import com.matridx.las.netty.dao.post.IDlysymxDao;
import com.matridx.las.netty.dao.post.IWkmxDao;
import com.matridx.las.netty.service.svcinterface.IDlysymxService;
import com.matridx.springboot.util.base.StringUtil;

@Service
public class DlysymxServiceImpl extends BaseBasicServiceImpl<DlysymxDto, DlysymxModel, IDlysymxDao>
        implements IDlysymxService {
    private Logger log = LoggerFactory.getLogger(DlysymxServiceImpl.class);

    @Autowired
    private IWkmxDao wkmxDao;

    /**
     * 保存定量仪结果信息
     *
     * @param pcr_result
     * @return
     */
    @Override
    @Transactional
    public WkmxPcrModel saveDlysymx(String pcr_result) {
        if (StringUtil.isBlank(pcr_result)) {
            log.error("传入的结果为空");
            return null;
        }
        WkmxPcrModel wkmxPcrModel = JSONObject.parseObject(pcr_result, WkmxPcrModel.class);
        try {

            // 先将上报时间保存为已上报
            List<WkmxDto> wkmxList = new ArrayList<WkmxDto>();
            // 保存完后，保存明细
            if (wkmxPcrModel.getResult() != null && wkmxPcrModel.getResult().size() > 0) {
                List<WkmxPcrResultModel> list = wkmxPcrModel.getResult();
                List<DlysymxDto> list2 = new ArrayList<DlysymxDto>();
                for (WkmxPcrResultModel wk : list) {
                    DlysymxDto dlysymxDto = new DlysymxDto();
                    dlysymxDto.setDlysymxid(StringUtil.generateUUID());
                    if (StringUtil.isNotBlank(wk.getCtVaule())) {
                        BigDecimal a = new BigDecimal(wk.getCtVaule());
                        BigDecimal b = new BigDecimal(100);
                        DecimalFormat df2 = new DecimalFormat("##0.00");
                        dlysymxDto.setCtvalue(df2.format(a.divide(b)));
                    }
                    dlysymxDto.setConcentration(wk.getConcentration());
                    dlysymxDto.setConcentrationunit(wk.getConcentrationUnit());
                    dlysymxDto.setStdconcentration(wk.getStdConcentration());
                    dlysymxDto.setReferencedye(wk.getReferenceDye());
                    dlysymxDto.setSampleuid(wk.getSampleUID());
                    dlysymxDto.setReplicatedgroup(wk.getReplicatedGroup());
                    dlysymxDto.setQcresult(wk.getQcResult());
                    dlysymxDto.setWellhindex(wk.getWellHIndex());
                    dlysymxDto.setWellvindex(wk.getWellVIndex());
                    dlysymxDto.setWell(wk.getWell());
                    dlysymxDto.setSamplenumber(wk.getSampleNumber());
                    dlysymxDto.setSamplename(wk.getSampleName());
                    dlysymxDto.setSampletype(wk.getSampleType());
                    dlysymxDto.setDyename(wk.getDyeName());
                    dlysymxDto.setGenename(wk.getGeneName());
                    dlysymxDto.setWkid(wkmxPcrModel.getWkid());
                    dlysymxDto.setJcdw(wkmxPcrModel.getJcdw());
                    dlysymxDto.setJcdwtype(wkmxPcrModel.getJcdwtype());
                    //  dlysymxDto.setDjc(wkmxPcrModel.getDjcsy());
                    list2.add(dlysymxDto);
                    //修改文库明细信息对应数据,如果有文库id，才修改，没有的话，没法修改
                    if (StringUtil.isNotBlank(wkmxPcrModel.getWkid())) {
                        WkmxDto wkmxParamDto = new WkmxDto();
                        wkmxParamDto.setWkid(wkmxPcrModel.getWkid());
                        wkmxParamDto.setNbbh(wk.getSampleName());
                        //wkmxParamDto.setYsybid(wk.getSampleName());
                        List<WkmxDto> wkmxDtoList = wkmxDao.getWkmxByWkid(wkmxParamDto);
                        if (wkmxDtoList != null && wkmxDtoList.size() > 0) {
                            WkmxDto wkmxdto = wkmxDtoList.get(0);
                            if ("1".equals(wkmxPcrModel.getDjcsy())) {
                                wkmxdto.setDycpcrid(dlysymxDto.getDlysymxid());

                            } else if ("2".equals(wkmxPcrModel.getDjcsy())) {
                                wkmxdto.setDecpcrid(dlysymxDto.getDlysymxid());
                            }
                            wkmxdto.setPcrkw(dlysymxDto.getWell());
                            if (StringUtil.isNotBlank(wk.getCtVaule())) {
                                BigDecimal a = new BigDecimal(wk.getCtVaule());
                                BigDecimal b = new BigDecimal(100);
                                DecimalFormat df2 = new DecimalFormat("##0.00");
                                wkmxdto.setQuantity(df2.format(a.divide(b)));
                            }
                            wkmxList.add(wkmxdto);

                        }
                    }
                }

                //统一到后台更新
                dao.saveDlysymx(list2);
                wkmxDao.updateByWkmxId(wkmxList);
                log.info("传入验证结果保存成功");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        return wkmxPcrModel;
    }
}
