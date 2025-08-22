package com.matridx.igams.hrm.service.impl;

import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.*;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.dao.entities.UserDto;

import com.alibaba.fastjson.JSON;
import com.matridx.igams.common.service.svcinterface.IUserService;
import com.matridx.igams.hrm.dao.entities.NdpxDto;
import com.matridx.igams.hrm.dao.entities.NdpxModel;
import com.matridx.igams.hrm.dao.post.INdpxDao;
import com.matridx.igams.hrm.service.svcinterface.INdpxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import com.matridx.springboot.util.email.EmailUtil;
import org.apache.commons.collections.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NdpxServiceImpl extends BaseBasicServiceImpl<NdpxDto, NdpxModel, INdpxDao> implements INdpxService, IFileImport {

    @Autowired
    ICommonService commonService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IJgxxService jgxxService;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    IUserService userService;
    private final Logger log = LoggerFactory.getLogger(NdpxServiceImpl.class);

    @Override
    public List<String> getFilterData() {
        return dao.getFilterData();
    }

    @Override
    public List<NdpxDto> getTaskInfo(NdpxDto ndpxDto) {
        return dao.getTaskInfo(ndpxDto);
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean addSaveAnnualPlan(NdpxDto ndpxDto) {
        ndpxDto.setNdpxid(StringUtil.generateUUID());
        int inserted = dao.insert(ndpxDto);
        if(inserted==0){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean modSaveAnnualPlan(NdpxDto ndpxDto) {
        int updated = dao.update(ndpxDto);
        if(updated==0){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public boolean delAnnualPlan(NdpxDto ndpxDto) {
        int deleted = dao.delete(ndpxDto);
        if(deleted==0){
            return false;
        }
        return true;
    }

    /**
     * 根据选择信息获取导出信息
     */
    public List<NdpxDto> getListForSelectExp(Map<String,Object> params){
        NdpxDto ndpxDto = (NdpxDto)params.get("entryData");
        queryJoinFlagExport(params,ndpxDto);
        return dao.getListForSelectExp(ndpxDto);
    }


    /**
     * 根据搜索条件获取导出条数
     */
    public int getCountForSearchExp(NdpxDto ndpxDto,Map<String, Object> params) {
        return dao.getCountForSearchExp(ndpxDto);
    }
    /**
     * 根据搜索条件分页获取导出信息
     */

    public List<NdpxDto> getListForSearchExp(Map<String, Object> params)
    {
        NdpxDto ndpxDto = (NdpxDto)params.get("entryData");
        queryJoinFlagExport(params,ndpxDto);

        return dao.getListForSearchExp(ndpxDto);
    }
    @SuppressWarnings("unchecked")
    private void queryJoinFlagExport(Map<String,Object> params,NdpxDto ndpxDto){
        List<DcszDto> choseList = (List<DcszDto>) params.get("choseList");
        StringBuilder sqlParam = new StringBuilder();
        for(DcszDto dcszDto:choseList){
            if(dcszDto == null || dcszDto.getDczd() == null)
                continue;

            sqlParam.append(",");
            if(StringUtil.isNotBlank(dcszDto.getSqlzd())){
                sqlParam.append(dcszDto.getSqlzd());
            }
            sqlParam.append(" ");
            sqlParam.append(dcszDto.getDczd());
        }
        String sqlcs=sqlParam.toString();
        ndpxDto.setSqlParam(sqlcs);
    }
	 @Override
    public boolean existCheck(String fieldName, String value) {
        return false;
    }

    @Override
    public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages) throws BusinessException {
        NdpxDto ndpxDto= (NdpxDto) baseModel;
        if(StringUtil.isNotBlank(ndpxDto.getJsyhm())){
            User yh = commonService.getDtoByYhm(ndpxDto.getJsyhm());
            if(yh!=null){
                ndpxDto.setJs(yh.getYhid());
            }
        }
        List<JcsjDto> pxfsList = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.TRAINING_METHODS.getCode());
        if(pxfsList!=null&&!pxfsList.isEmpty()){
            for(JcsjDto dto:pxfsList){
                if(ndpxDto.getPxfsmc().equals(dto.getCsmc())){
                    ndpxDto.setPxfs(dto.getCsid());
                    break;
                }
            }
        }
        JgxxDto jgxxDto=new JgxxDto();
        jgxxDto.setJgmc(ndpxDto.getSsbmmc());
        JgxxDto jgxx = jgxxService.getJgxxByJgmc(jgxxDto);
        if(jgxx!=null){
            ndpxDto.setSsbm(jgxx.getJgid());
        }
        ndpxDto.setLrry(user.getYhid());
        ndpxDto.setNdpxid(StringUtil.generateUUID());
        if("计划内".equals(ndpxDto.getLb())){
            ndpxDto.setLb("0");
        }else if("计划外".equals(ndpxDto.getLb())){
            ndpxDto.setLb("1");
        }
        int inserted = dao.insert(ndpxDto);
        return inserted != 0;
    }

    @Override
    public String cellTransform(String tranTrack, String value, ImportRecordModel recModel, StringBuffer errorMessage) {
        return null;
    }

    @Override
    public boolean insertNormalImportRec(Map<String, String> recMap, User user) {
        return false;
    }

    @Override
    public boolean checkDefined(List<Map<String, String>> defined) {
        return true;
    }
    /*
        定时任务增加年度培训通知
        邮件标题：培训名称
        邮件内容： 请按年度培训计划发起培训，培训名称xXXXXXXXXX,计划培训时间XXXXXXXXX,
        参培部门/人员XXXXXXXXXXXXXXX,培训方式XXXXX,备注XXXXXXXXXXX.
        请尽快完成！
    */
    public void remindAnnualTraining(Map<String,String> map){
        //外部程序id
        String wbcxid = map.get("wbcxid");
        if (StringUtil.isBlank(wbcxid)){
            wbcxid = "matridxOA";
        }
        NdpxDto ndpxDto = new NdpxDto();
        ndpxDto.setWbcxid(wbcxid);
        //如果当前时间=计划培训时间
        ndpxDto.setJhpxsj(DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd"));
        List<NdpxDto> dtoList = dao.getRemindAnnualTraining(ndpxDto);
        if (!CollectionUtils.isEmpty(dtoList)){
            try {
                Map<String, NdpxDto> zgMap = new HashMap<>();
                for (NdpxDto dto : dtoList) {
                    String yxnr = getRemindAnnualTrainingMsg(dto);
                    //判断讲师是否为null 如果不为null,发送邮件给讲师。
                    if (StringUtil.isNotBlank(dto.getJs())){
                        if (StringUtil.isNotBlank(dto.getJsyx())){
                            emailUtil.sendEmail(Collections.singletonList(dto.getJsyx()), dto.getPxmc(), yxnr);
                        }else {
                            log.error("remindAnnualTraining--缺失讲师邮箱:{}", JSON.toJSONString(dto));
                        }
                    }else if (StringUtil.isNotBlank(dto.getBmzgs())){
                        //讲师为null, 发送邮件给部门直属主管
                        zgMap.put(dto.getBmzgs(),dto);
                    }else {
                        log.error("remindAnnualTraining--缺失部门主管:{}", JSON.toJSONString(dto));
                    }
                }
                if (!MapUtils.isEmpty(zgMap)){
                    UserDto userDto = new UserDto();
                    userDto.setWbcxid(wbcxid);
                    userDto.setIds(new ArrayList<>(zgMap.keySet()));
                    List<UserDto> userDtos = userService.getBmzgsInfo(userDto);
                    for (UserDto dto : userDtos) {
                        NdpxDto ndpxDto_msg = zgMap.get(dto.getBmzgs());
                        String yxnr = getRemindAnnualTrainingMsg(ndpxDto_msg);
                        if (StringUtil.isNotBlank(dto.getEmail())){
                            emailUtil.sendEmail(Collections.singletonList(dto.getEmail()), ndpxDto_msg.getPxmc(), yxnr);
                        }else {
                            log.error("remindAnnualTraining--缺失主管邮箱:{}", JSON.toJSONString(ndpxDto_msg));
                        }
                    }
                }
                NdpxDto ndpxDto_up = new NdpxDto();
                ndpxDto_up.setSffs("1");
                ndpxDto_up.setIds(dtoList.stream().map(NdpxDto::getNdpxid).collect(Collectors.toList()));
                dao.updateSffs(ndpxDto_up);
            } catch (Exception e) {
                log.error("remindAnnualTraining--错误信息:{}",e.getMessage());
            }
        }
    }
    /*
        获取邮箱消息内容
     */
    @NotNull
    private static String getRemindAnnualTrainingMsg(NdpxDto dto) {
        return "请按年度培训计划发起培训！\n培训名称："+ dto.getPxmc()+"\n计划培训时间："+ dto.getJhpxsj()+"\n参培部门/人员："+(StringUtil.isNotBlank(dto.getCpbmry())? dto.getCpbmry():"无")
                +"\n培训方式："+ dto.getPxfsmc()+"\n备注："+(StringUtil.isNotBlank(dto.getBz())? dto.getBz():"无"+"\n请尽快完成！");
    }
}
