package com.matridx.igams.hrm.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.entities.DcszDto;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.RabbitEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.hrm.dao.entities.YghmcDto;
import com.matridx.igams.hrm.dao.entities.YghmcModel;
import com.matridx.igams.hrm.dao.post.IYghmcDao;
import com.matridx.igams.hrm.service.svcinterface.IYghmcService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.igams.common.util.DingTalkUtil;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author:JYK
 */
@Service
public class YghmcServiceImpl extends BaseBasicServiceImpl<YghmcDto, YghmcModel, IYghmcDao> implements IYghmcService {
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Autowired
    IDdxxglService ddxxglService;
    @Autowired
    DingTalkUtil talkUtil;
    @Autowired
    IXxglService xxglService;
    @Autowired(required=false)
    private AmqpTemplate amqpTempl;
    @Value("${matridx.rabbit.flg:}")
    private String prefixFlg;
    @Value("${matridx.rabbit.preflg:}")
    private String preRabbitFlg;
    //是否发送rabbit标记     1：发送
    @Value("${matridx.rabbit.configflg:1}")
    private String configflg;
    /**
     * 批量修改
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateList(YghmcDto yghmcDto){
        return dao.updateList(yghmcDto);
    }

    /**
     * 导出
     */
    @Override
    public int getCountForSearchExp(YghmcDto yghmcDto, Map<String, Object> params) {
        return dao.getCountForSearchExp(yghmcDto);
    }

    @Override
    public List<String> getAllYgYhid() {
        return dao.getAllYgYhid();
    }

    @Override
    public void insertYghmcDtos(List<YghmcDto> yghmcDtos) {
        dao.insertYghmcDtos(yghmcDtos);
    }
    @Override
    public void updateYghmcDtos(List<YghmcDto> yghmcDtos) {
        dao.updateYghmcDtos(yghmcDtos);
    }
    /**
     * 根据搜索条件获取导出信息
     */
    public List<YghmcDto> getListForSearchExp(Map<String, Object> params) {
        YghmcDto yghmcDto = (YghmcDto) params.get("entryData");
        queryJoinFlagExport(params, yghmcDto);
        return dao.getListForSearchExp(yghmcDto);
    }

    /**
     * 根据选择信息获取导出信息
     */
    public List<YghmcDto> getListForSelectExp(Map<String, Object> params) {
        YghmcDto yghmcDto = (YghmcDto) params.get("entryData");
        queryJoinFlagExport(params, yghmcDto);
        return dao.getListForSelectExp(yghmcDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String, Object> params, YghmcDto yghmcDto) {
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for (DcszDto dcszDto : choseList) {
            if (dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if (StringUtil.isNotBlank(dcszDto.getSqlzd())) {
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs = sqlParam.toString();
        yghmcDto.setSqlParam(sqlcs);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean processSaveRoster(YghmcDto yghmcDto) {
        boolean isSuccess = dao.updateDqtxByIds(yghmcDto);
        if("1".equals(configflg)&&isSuccess) {
            Map<String, Object> map = new HashMap<>();
            map.put("prefix",prefixFlg);
            map.put("ids",yghmcDto.getIds());
            amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate", RabbitEnum.HMC_PRO.getCode() + JSONObject.toJSONString(map));
        }
        return isSuccess;
    }

    @Override
    public List<YghmcDto> getAllRosterBm(YghmcDto yghmcDto) {
        return dao.getAllRosterBm(yghmcDto);
    }

    /**
     * 合同提醒定时发布任务
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void expireRemindTask() throws BusinessException{
        YghmcDto yghmcDto = new YghmcDto();
        List<YghmcDto> dtoList = dao.getDtoList(yghmcDto);
        List<YghmcDto> txlist = new ArrayList<>();
        List<YghmcDto> btxlist = new ArrayList<>();
        for (YghmcDto dto : dtoList) {
            if (StringUtil.isNotBlank(dto.getRqc())){
                if (Integer.parseInt(dto.getRqc())<45&&"1".equals(dto.getDqtx())){
                    txlist.add(dto);
                }
                if (Integer.parseInt(dto.getRqc())>=45&&"0".equals(dto.getDqtx())){
                    dto.setDqtx("1");
                    dto.setPrefix(prefixFlg);
                    btxlist.add(dto);
                }
            }
        }
        if (!CollectionUtils.isEmpty(btxlist)){
            boolean isSuccess = dao.updateListWithDqtx(btxlist);
            if (!isSuccess){
                throw new BusinessException("msg","修改到期提醒状态失败！！");
            }
            if("1".equals(configflg)) {
                amqpTempl.convertAndSend("sys.igams", preRabbitFlg + "sys.igams.user.operate", RabbitEnum.HMC_MOD.getCode() + JSONObject.toJSONString(btxlist));
            }
        }
        if (!CollectionUtils.isEmpty(txlist)){
//            String token = talkUtil.getToken();
            String ICOMM_HTDQ001 = xxglService.getMsg("ICOMM_HTDQ001");
            List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.CONTRACT_DQTX.getCode());
            for (DdxxglDto ddxxglDto : ddxxglDtolist) {
                // 内网访问
                String internalbtn =applicationurl + urlPrefix + "/ws/roster/viewDqRoster";
                List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                btnJsonList.setTitle("详细");
                btnJsonList.setActionUrl(internalbtn);
                btnJsonLists.add(btnJsonList);
                talkUtil.sendCardMessage(ddxxglDto.getYhm(),ddxxglDto.getDdid(),ICOMM_HTDQ001,ICOMM_HTDQ001,
                        btnJsonLists,"1");
            }

        }

    }

    @Override
    public List<YghmcDto> getAllYghtxxByHmc() {
        return dao.getAllYghtxxByHmc();
    }

    @Override
    public List<YghmcDto> getAllYgLzxxByHmc() {
        return dao.getAllYgLzxxByHmc();
    }

    @Override
    public Map<String, Object> getScreenClassfy() {
        return dao.getScreenClassfy();
    }

    @Override
    public List<YghmcDto> getSubordinateEmployee(YghmcDto yghmcDto) {
        return dao.getSubordinateEmployee(yghmcDto);
    }

    @Override
    public List<YghmcDto> getUserByYhmOrZsxm(YghmcDto yghmcDto) {
        return dao.getUserByYhmOrZsxm(yghmcDto);
    }

    @Override
    public YghmcDto getDtoByYhId(String yhid) {
        return dao.getDtoByYhId(yhid);
    }

    @Override
    public List<YghmcDto> getLzryByRq(YghmcDto yghmcDto) {
        return dao.getLzryByRq(yghmcDto);
    }
    /**
     * 获取所有员工花名册
     */
    public List<YghmcDto> getAllList(){
        return dao.getAllList();
    }

    /**
     * 递归获取下级
     */
    public void recursiveGetInfo(List<YghmcDto> allList,List<String> ddids,int cj,List<String> ryids){
        if(cj!=0&&!CollectionUtils.isEmpty(ddids)){
            List<String> newList=new ArrayList<>();
            for(String ddid:ddids){
                for(YghmcDto yghmcDto:allList){
                    if(ddid.equals(yghmcDto.getZszg())){
                        ryids.add(yghmcDto.getYhid());
                        if(StringUtil.isNotBlank(yghmcDto.getDdid())){
                            newList.add(yghmcDto.getDdid());
                        }
                    }
                }
            }
            cj--;
            newList = newList.stream().distinct().collect(Collectors.toList());//去重
            recursiveGetInfo(allList,newList,cj,ryids);
        }
    }

    @Override
    public List<YghmcDto> getYghmcDtos(YghmcDto yghmcDto) {
        return dao.getYghmcDtos(yghmcDto);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public YghmcDto queryYghmcDto(String yghmcid) {
        YghmcDto yghmcDto = getDtoById(yghmcid);
        String nowTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MMdd"));
        nowTime = "HR"+"-"+nowTime+"-";
        boolean jlbhBoolean = false;
        if(StringUtil.isNotBlank(yghmcDto.getJlbh())){
            String substring = yghmcDto.getJlbh().substring(0, yghmcDto.getJlbh().lastIndexOf("-"));
            if(substring.equals(nowTime)){
                jlbhBoolean = true;
            }
        }
        if(!jlbhBoolean){
            String jlbh = dao.queryJlbh(nowTime);
            yghmcDto.setJlbh(nowTime+jlbh);
        }
        return yghmcDto;
    }
}
