package com.matridx.igams.wechat.service.impl;


import com.alibaba.fastjson.JSON;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.SjycDto;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.wechat.dao.entities.BfplszDto;
import com.matridx.igams.wechat.dao.entities.KhzyfpDto;
import com.matridx.igams.wechat.dao.entities.KhzyfpModel;
import com.matridx.igams.wechat.dao.post.IKhzyfpDao;
import com.matridx.igams.wechat.service.svcinterface.IKhzyfpService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KhzyfpServiceImpl extends BaseBasicServiceImpl<KhzyfpDto, KhzyfpModel, IKhzyfpDao> implements IKhzyfpService {

    @Autowired
    IXxglService xxglService;

    @Autowired
    DingTalkUtil talkUtil;

    @Override
    public List<KhzyfpDto> getPagedCustomerList(KhzyfpDto khzyfpDto) {
        return dao.getPagedCustomerList(khzyfpDto);
    }

    @Override
    public List<KhzyfpDto> getPagedVisitRecordsList(KhzyfpDto khzyfpDto) {
        return dao.getPagedVisitRecordsList(khzyfpDto);
    }

    @Override
    public boolean mergeList(List<KhzyfpDto> list) {
        return dao.mergeList(list);
    }

    @Override
    public boolean insertList(List<KhzyfpDto> list) {
        return dao.insertList(list);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean resourceDispenseSaveVisitingObject(KhzyfpDto khzyfpDto) {
        List<KhzyfpDto> khzyfpDtos=(List<KhzyfpDto>) JSON.parseArray(khzyfpDto.getZyfp_json(), KhzyfpDto.class);
        if(khzyfpDtos!=null&&!khzyfpDtos.isEmpty()){
            List<KhzyfpDto> insertList=new ArrayList<>();
            List<KhzyfpDto> updateList=new ArrayList<>();
            List<String> ids =new ArrayList<>();
            for(KhzyfpDto dto:khzyfpDtos){
                if("KH".equals(khzyfpDto.getFpbj())){
                    ids.add(dto.getDwid());
                }
                if("XS".equals(khzyfpDto.getFpbj())){
                    ids.add(dto.getYhid());
                }
                if(StringUtil.isNotBlank(dto.getDwid())&&StringUtil.isNotBlank(dto.getYhid())){
                    if(StringUtil.isNotBlank(dto.getZyfpid())){
                        updateList.add(dto);
                    }else{
                        dto.setZyfpid(StringUtil.generateUUID());
                        dto.setLrry(khzyfpDto.getScry());
                        insertList.add(dto);
                    }
                }
            }
            khzyfpDto.setIds(ids.stream().distinct().collect(Collectors.toList()));
            dao.delUselessData(khzyfpDto);
            dao.delete(khzyfpDto);
            if(!updateList.isEmpty()){
                boolean restored = dao.restoreData(updateList);
                if(!restored){
                    return false;
                }
            }
            if(!insertList.isEmpty()){
                boolean inserted = dao.insertList(insertList);
                if(!inserted){
                    return false;
                }
            }
            dao.delVisitFrequencyData(khzyfpDto);
            Map<String, List<KhzyfpDto>> listMap = khzyfpDtos.stream().collect(Collectors.groupingBy(KhzyfpDto::getYhid));
            for (Map.Entry<String, List<KhzyfpDto>> entry : listMap.entrySet()) {
                List<KhzyfpDto> resultModelList = entry.getValue();
                //小程序访问
                String dingtalkbtn = "page=/pages/index/index" + URLEncoder.encode("?toPageUrl=/pages/index/visit/visitregistrationlist/visitregistrationlist", StandardCharsets.UTF_8);
                List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> t_btnJsonLists = new ArrayList<>();
                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList t_btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                t_btnJsonList.setTitle("小程序");
                t_btnJsonList.setActionUrl(dingtalkbtn);
                t_btnJsonLists.add(t_btnJsonList);
                talkUtil.sendCardMessage(resultModelList.get(0).getYhm(), resultModelList.get(0).getDdid(), xxglService.getMsg("ICOMM_CRM00001"), xxglService.getMsg("ICOMM_CRM00002"), t_btnJsonLists, "1");
            }
        }
        return true;
    }

    @Override
    public boolean deleteByYhms(List<String> yhms) {
        return dao.deleteByYhms(yhms);
    }
}
