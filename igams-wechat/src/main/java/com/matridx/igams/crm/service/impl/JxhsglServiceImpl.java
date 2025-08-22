package com.matridx.igams.crm.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.matridx.igams.common.dao.entities.AuditParam;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.ShgcDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XtszDto;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.enums.AuditStateEnum;
import com.matridx.igams.common.enums.AuditTypeEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.StatusEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.IAuditService;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IShgcService;
import com.matridx.igams.common.service.svcinterface.IXtszService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.service.svcinterface.IXxglService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.crm.dao.entities.JxhsglDto;
import com.matridx.igams.crm.dao.entities.JxhsglModel;
import com.matridx.igams.crm.dao.entities.JxhsmxDto;
import com.matridx.igams.crm.dao.post.IJxhsglDao;
import com.matridx.igams.crm.service.svcinterface.IJxhsglService;
import com.matridx.igams.crm.service.svcinterface.IJxhsmxService;
import com.matridx.igams.wechat.dao.entities.SwyszkDto;
import com.matridx.igams.wechat.service.svcinterface.ISwyszkService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JxhsglServiceImpl extends BaseBasicServiceImpl<JxhsglDto, JxhsglModel, IJxhsglDao> implements IJxhsglService, IAuditService {

    @Autowired
    private IJxhsmxService jxhsmxService;
    @Autowired
    private ISwyszkService swyszkService;
    @Autowired
    private IXxdyService xxdyService;
    @Autowired
    private IXxglService xxglService;
    @Autowired
    private IShgcService shgcService;
    @Autowired
    private ICommonService commonService;
    @Autowired
    private IXtszService xtszService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    DingTalkUtil talkUtil;

    private Logger log = LoggerFactory.getLogger(JxhsglServiceImpl.class);
    /**
     * 编辑保存绩效核算管理
     * @param jxhsglDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String,Object> editSaveJxhsgl(JxhsglDto jxhsglDto, User user) throws BusinessException {
        BigDecimal jxzje=new BigDecimal("0");
        BigDecimal sd=new BigDecimal("0");//税点
        if(StringUtil.isNotBlank(jxhsglDto.getSd())){
            sd=new BigDecimal(jxhsglDto.getSd());
        }
        Map<String,Object> map = new HashMap<>();
        JxhsglDto dto = dao.getSameDto(jxhsglDto);
        if (dto != null){
            throw new BusinessException(dto.getFzrxm() + "，"+dto.getJxksrq()+"~"+dto.getJxjsrq()+ "绩效核算表已创建，若要调整请进行修改操作!");
        }
        if (StringUtil.isNotBlank(jxhsglDto.getJxhsglid())){
            // 修改
            List<JxhsmxDto> insertList;
            List<JxhsmxDto> updateList=new ArrayList<>();
            List<JxhsmxDto> deleteList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(jxhsglDto.getJxhsmxDtos())){
                for(JxhsmxDto jxhsmxDto : jxhsglDto.getJxhsmxDtos()){
                    jxzje=jxzje.add(new BigDecimal(StringUtil.isNotBlank(jxhsmxDto.getJxhsje())?jxhsmxDto.getJxhsje():"0"));
                }
                List<JxhsmxDto> jxhsmxDtos = jxhsglDto.getJxhsmxDtos();
                insertList = jxhsmxDtos.stream().filter(jxhsmxDto -> StringUtil.isBlank(jxhsmxDto.getJxhsmxid())).collect(Collectors.toList());
                updateList = jxhsmxDtos.stream().filter(jxhsmxDto -> StringUtil.isNotBlank(jxhsmxDto.getJxhsmxid())).collect(Collectors.toList());

                JxhsmxDto jxhsmxDto = new JxhsmxDto();
                jxhsmxDto.setJxhsglid(jxhsglDto.getJxhsglid());
                List<JxhsmxDto> dataJxhsmxDtos =  jxhsmxService.getDtoList(jxhsmxDto);
                if (!CollectionUtils.isEmpty(dataJxhsmxDtos)){
                    for (int i = dataJxhsmxDtos.size() - 1; i >= 0; i--) {
                        // 数据库中的信息
                        JxhsmxDto jxhsmxDto_d = dataJxhsmxDtos.get(i);
                        Optional<JxhsmxDto> any = updateList.stream().filter(item -> item.getJxhsmxid().equals(jxhsmxDto_d.getJxhsmxid())).findAny();
                        if (any.isPresent()){
                            // 前端传过来的数据
                            JxhsmxDto jxhsmxDto_h = any.get();
                            // 保留数据
                            BigDecimal newValue = new BigDecimal(StringUtil.isNotBlank(jxhsmxDto_h.getJxhsje())?jxhsmxDto_h.getJxhsje():"0").subtract(new BigDecimal(StringUtil.isNotBlank(jxhsmxDto_h.getJxhsje())?jxhsmxDto_h.getJxhsje():"0"));
//                            jxhsmxDto_h.setJxhsje(newValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
                            jxhsmxDto_h.setYjxhsje(newValue.toString());
                        } else {
                            updateList.remove(i);
                            deleteList.add(jxhsmxDto_d);
                        }
                    }
                }
            } else {
                insertList = new ArrayList<>();
            }
            jxhsglDto.setXgry(user.getYhid());
            jxhsglDto.setJxhsze(jxzje.toString());
            jxhsglDto.setShze(String.valueOf(jxzje.multiply((new BigDecimal("100").subtract(sd)).divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP)));
            int update = dao.update(jxhsglDto);
            if (update <= 0){
                // 保存失败
                throw new RuntimeException("保存失败!");
            }
            boolean isSuccess = false;
            if (!CollectionUtils.isEmpty(deleteList)){
                deleteList.forEach(item -> item.setScry(user.getYhid()));
                deleteList.forEach(item -> item.setJxhsje(new BigDecimal(0).subtract(new BigDecimal(StringUtil.isNotBlank(item.getJxhsje())?item.getJxhsje():"0")).toString()));
                isSuccess = jxhsmxService.deleteDtos(deleteList);
                if (!isSuccess){
                    throw new BusinessException("更新失败！--1");
                }
            }
            if (!CollectionUtils.isEmpty(updateList)){
                updateList.forEach(item -> item.setXgry(user.getYhid()));
                isSuccess = jxhsmxService.updateDtos(updateList);
                if (!isSuccess){
                    throw new BusinessException("更新失败！--2");
                }
                updateList.forEach(item -> item.setJxhsje(item.getYjxhsje()));
            }
            if (!CollectionUtils.isEmpty(insertList)){
                //判断jxhsmxDtos中是否存在两个yszkid一样的的数据
                List<JxhsmxDto> sameLists = insertList.stream().filter(item -> item.getYszkid().equals(insertList.get(0).getYszkid())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(sameLists)){
                    throw new BusinessException("存在重复的应收账款，请检查！");
                }
                for (JxhsmxDto insertDto : insertList) {
                    insertDto.setJxhsmxid(StringUtil.generateUUID());
                    insertDto.setJxhsglid(jxhsglDto.getJxhsglid());
                }
                isSuccess = jxhsmxService.insertDtos(insertList);
                if (!isSuccess){
                    throw new BusinessException("保存绩效核算明细失败!");
                }
            }
            if (isSuccess){
                List<JxhsmxDto> wholeList = new ArrayList<>();
                wholeList.addAll(insertList);
                wholeList.addAll(updateList);
                wholeList.addAll(deleteList);
                swyszkService.updateJxhsmxsAddJxje(wholeList);
            }
        } else {
            List<JxhsmxDto> jxhsmxDtos = jxhsglDto.getJxhsmxDtos();
            if (!CollectionUtils.isEmpty(jxhsmxDtos)){
                //判断jxhsmxDtos中是否存在两个yszkid一样的的数据
//                List<JxhsmxDto> sameLists = jxhsmxDtos.stream().filter(jxhsmxDto -> jxhsmxDto.getYszkid().equals(jxhsmxDtos.get(0).getYszkid())).collect(Collectors.toList());
//                if (!CollectionUtils.isEmpty(sameLists)){
//                    throw new RuntimeException("存在重复的应收账款，请检查！");
//                }
                for (JxhsmxDto jxhsmxDto : jxhsmxDtos) {
                    jxzje=jxzje.add(new BigDecimal(StringUtil.isNotBlank(jxhsmxDto.getJxhsje())?jxhsmxDto.getJxhsje():"0"));
                    jxhsmxDto.setJxhsmxid(StringUtil.generateUUID());
                    jxhsmxDto.setJxhsglid(jxhsglDto.getJxhsglid());
                }
            }
            // 新增
            // 设置id
            jxhsglDto.setJxhsglid(StringUtil.generateUUID());
            // 默认是否核对为0
            jxhsglDto.setSfhd("0");
            jxhsglDto.setJxhsze(jxzje.toString());
            jxhsglDto.setShze(String.valueOf(jxzje.multiply((new BigDecimal("100").subtract(sd)).divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP)));
            // 设置录入人员
            jxhsglDto.setLrry(user.getYhid());
            jxhsglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
            int insert = dao.insert(jxhsglDto);
            if (insert <= 0){
                // 保存失败
                throw new RuntimeException("保存失败!");
            }

            boolean isSuccess = jxhsmxService.insertDtos(jxhsmxDtos);
            if (!isSuccess){
                throw new RuntimeException("保存绩效核算明细失败!");
            }
            isSuccess = swyszkService.updateJxhsmxs(jxhsmxDtos);

        }
        // 提交审核
        ShgcDto shgcDto = new ShgcDto();
        shgcDto.setExtend_1("[\""+jxhsglDto.getJxhsglid()+"\"]");
        shgcDto.setShlb(AuditTypeEnum.AUDIT_PERFORMANCE_CHECK.getCode());
        try
        {
            map = shgcService.checkAndCommit(shgcDto, user);
        } catch (Exception e)
        {
            throw new BusinessException(e.getMessage());
        }
        return map;
    }

    /**
     * 删除保存绩效核算管理
     *
     * @param jxhsglDto
     * @param user
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> delSaveJxhsgl(JxhsglDto jxhsglDto, User user) throws BusinessException {
        Map<String,Object> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(jxhsglDto.getIds())){
            jxhsglDto.setScry(user.getYhid());
            int delete = dao.delete(jxhsglDto);
            if (delete > 0){
                JxhsmxDto jxhsmxDto = new JxhsmxDto();
                jxhsmxDto.setJxglids(jxhsglDto.getIds());
                List<JxhsmxDto> deleteList = jxhsmxService.getDtoList(jxhsmxDto);

                deleteList.forEach(item -> item.setScry(user.getYhid()));
                deleteList.forEach(item -> item.setJxhsje(new BigDecimal(0).subtract(new BigDecimal(item.getJxhsje())).toString()));
                boolean isSuccess = jxhsmxService.deleteDtos(deleteList);
                if (!isSuccess){
                    throw new BusinessException("删除失败！");
                }
                isSuccess = swyszkService.updateJxhsmxs(deleteList);
                if (!isSuccess){
                    throw new BusinessException("更新商务应收账款失败！");
                }
            }else {
                throw new BusinessException("删除失败！");
            }
        } else {
            throw new BusinessException("删除失败！");
        }
        map.put("status","success");
        map.put("message","删除成功!");
        return map;
    }

    /**
     * 编辑保存设置信息
     *
     * @param data
     * @param user
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Map<String, Object> editSaveSettings(Map<String, Object> data, User user) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        List<JcsjDto> dylxs = redisUtil.lgetDto(("List_matridx_jcsj:" + BasicDataTypeEnum.XXDY_TYPE.getCode()));
        try {
            List<Map<String,Object>> list1 = JSONArray.parseObject((String) data.get("list_one"), new TypeReference<List<Map<String, Object>>>() {});
            List<Map<String,Object>> list2 = JSONArray.parseObject((String) data.get("list_two"), new TypeReference<List<Map<String, Object>>>() {});
            List<Map<String, Object>> insertList = new ArrayList<>();
            List<Map<String, Object>> updateList = new ArrayList<>();
            List<Map<String, Object>> deleteList = new ArrayList<>();
            XxdyDto xxdyDto_jxhssz1 = new XxdyDto();
            xxdyDto_jxhssz1.setDylxcsdm("JXHS_CFKHSZ");
            if (!CollectionUtils.isEmpty(list1)){
                JcsjDto jcsjDto = dylxs.stream().filter(item -> "JXHS_CFKHSZ".equals(item.getCsdm())).findFirst().get();
                list1.forEach(item -> item.put("dylx",jcsjDto.getCsid()));
            }
            if (!CollectionUtils.isEmpty(list2)){
                JcsjDto jcsjDto = dylxs.stream().filter(item -> "JXHS_TCKHSZ".equals(item.getCsdm())).findFirst().get();
                list2.forEach(item -> item.put("dylx",jcsjDto.getCsid()));
                list2.forEach(item -> item.put("dyxx","TCKH"));
            }
            // 数据库中数据
            List<Map<String, Object>> data_list1 = xxdyService.getOriginList(xxdyDto_jxhssz1);
            if (CollectionUtils.isEmpty(data_list1)){
                insertList.addAll(list1);
            }
            if (CollectionUtils.isEmpty(list1)){
                deleteList.addAll(data_list1);
            }
            if (!CollectionUtils.isEmpty(data_list1) && !CollectionUtils.isEmpty(list1)){
                for (int i = data_list1.size() - 1; i >= 0; i--) {
                    Map<String, Object> xxdy = data_list1.get(i);
                    // 查找前端数据中是否有与数据库中数据的yxx和dyxx一致的数据
                    Optional<Map<String, Object>> any = list1.stream().filter(item -> xxdy.get("yxx").equals(item.get("yxx")) && xxdy.get("dyxx").equals(item.get("dyxx"))).findAny();
                    if (any.isPresent()){
                        // 若有,加入updateList
                        xxdy.put("kzcs1",any.get().get("kzcs1"));
                        // 去除前端数据中的该数据(剩余数据为新增数据)
                        list1.remove(any.get());
                        updateList.add(xxdy);
                    } else {
                        deleteList.add(xxdy);
                    }
                }
                if (!CollectionUtils.isEmpty(list1)){
                    insertList.addAll(list1);
                }
            }
            XxdyDto xxdyDto_jxhssz2 = new XxdyDto();
            xxdyDto_jxhssz2.setDylxcsdm("JXHS_TCKHSZ");
            xxdyDto_jxhssz2.setDyxx("TCKH");
            List<Map<String, Object>> data_list2 = xxdyService.getOriginList(xxdyDto_jxhssz2);
            if (CollectionUtils.isEmpty(data_list2)){
                insertList.addAll(list2);
            }
            if (CollectionUtils.isEmpty(list2)){
                deleteList.addAll(data_list2);
            }
            if (!CollectionUtils.isEmpty(data_list2) && !CollectionUtils.isEmpty(list2)){
                for (int i = data_list2.size() - 1; i >= 0; i--) {
                    Map<String, Object> xxdy = data_list2.get(i);
                    // 查找前端数据中是否有与数据库中数据的yxxx一致的数据
                    Optional<Map<String, Object>> any = list2.stream().filter(item -> xxdy.get("yxx").equals(item.get("yxx"))).findAny();
                    if (any.isPresent()){
                        list2.remove(any.get());
                    } else {
                        deleteList.add(xxdy);
                    }
                }
                if (!CollectionUtils.isEmpty(list2)){
                    insertList.addAll(list2);
                }
            }
            if (!CollectionUtils.isEmpty(insertList)){
                insertList.forEach(item -> item.put("lrry",user.getYhid()));
                insertList.forEach(item -> item.put("dyid",StringUtil.generateUUID()));
                xxdyService.batchInsertDtos(insertList);
            }
            if (!CollectionUtils.isEmpty(updateList)){
                updateList.forEach(item -> item.put("xgry",user.getYhid()));
                xxdyService.batchUpdateDtos(updateList);
            }
            if (!CollectionUtils.isEmpty(deleteList)){
                deleteList.forEach(item -> item.put("scry",user.getYhid()));
                xxdyService.batchDeleteDtos(deleteList);
            }
        } catch (Exception e) {
            map.put("status", "fail");
            map.put("message", "保存失败！错误信息:"+e.getMessage());
            return map;
        }
        map.put("status", "success");
        map.put("message", "保存成功！");
        return map;
    }

    /**
     * 审核列表
     *
     * @param jxhsglDto
     * @return
     */
    @Override
    public List<JxhsglDto> getPagedAuditList(JxhsglDto jxhsglDto) {
        // 获取人员ID和履历号
        List<JxhsglDto> t_List= dao.getPagedAuditList(jxhsglDto);

        if (CollectionUtils.isEmpty(t_List))
            return t_List;

        List<JxhsglDto> sqList = dao.getAuditList(t_List);

        commonService.setSqrxm(sqList);

        return sqList;
    }


    /**
     * 进行前置处理，比如审核时显示编辑页面，则先进行编辑信息的保存操作
     *
     * @param baseModel  审核过程列表
     * @param operator   操作人
     * @param auditParam 审核传递的额外参数信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updatePreAuditTarget(Object baseModel, User operator, AuditParam auditParam) throws BusinessException {
        JxhsglDto jxhsglDto = (JxhsglDto) baseModel;
        jxhsglDto.setXgry(operator.getYhid());
        jxhsglDto.setJxhsglid(auditParam.getShgcDto().getYwid());
        dao.updateZt(jxhsglDto);
        return true;
    }

    /**
     * 更新审核对象
     *
     * @param shgcList   审核过程列表
     * @param operator   操作人
     * @param auditParam 审核传递的额外参数信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditTarget(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        // TODO Auto-generated method stub
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
        for (ShgcDto shgcDto :shgcList){
            JxhsglDto jxhsglDto = new JxhsglDto();
            jxhsglDto.setJxhsglid(shgcDto.getYwid());
            jxhsglDto.setXgry(operator.getYhid());
            JxhsglDto dto = dao.getDto(jxhsglDto);

            if(auditParam!=null){
                if(auditParam.isPassOpe()) {
                    if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())||AuditStateEnum.AUDITING.equals(shgcDto.getAuditState())) {

                    }
                }
            }
            if (AuditStateEnum.AUDITBACK.equals(shgcDto.getAuditState())) {
                // 审核不通过
                jxhsglDto.setZt(StatusEnum.CHECK_UNPASS.getCode());
                /*
INSERT INTO "public"."matridx_xxgl" ("xxid", "xxlx", "xxnr", "ddqid") VALUES ('ICOMM_PC0001', NULL, '您有一个绩效核算审核未通过', NULL);
INSERT INTO "public"."matridx_xxgl" ("xxid", "xxlx", "xxnr", "ddqid") VALUES ('ICOMM_PC0002', NULL, '# 您有一个绩效核算审核未通过
* 提交者：#{0}
* 名称：#{1}
* 类别：#{2}
* 意见：#{3}
* 时间：#{4}', NULL);
                 */
                String ICOMM_PC0001 = xxglService.getMsg("ICOMM_PC0001");
                String ICOMM_PC0011 = xxglService.getMsg("ICOMM_PC0011");
                if (shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0) {
                    for (int i = 0; i < shgcDto.getSpgwcyDtos().size(); i++) {
                        if (StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())) {
                            talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),shgcDto.getSpgwcyDtos().get(i).getYhid(),shgcDto.getSpgwcyDtos().get(i).getYhm(), shgcDto.getSpgwcyDtos().get(i).getYhid(), ICOMM_PC0001,
                                    StringUtil.replaceMsg(ICOMM_PC0011, operator.getZsxm(), dto.getMc(),
                                            shgcDto.getShlbmc(),shgcDto.getShxx_shyj(),
                                            DateUtils.getCustomFomratCurrentDate("YYYY-MM-dd HH:mm:ss")));
                        }
                    }
                }
            } else if(AuditStateEnum.AUDITED.equals(shgcDto.getAuditState())){
                //审核通过
                jxhsglDto.setZt(StatusEnum.CHECK_PASS.getCode());
                /*
INSERT INTO "public"."matridx_xxgl" ("xxid", "xxlx", "xxnr", "ddqid") VALUES ('ICOMM_PC0002', NULL, '您的绩效核算提交已被审核通过', NULL);
INSERT INTO "public"."matridx_xxgl" ("xxid", "xxlx", "xxnr", "ddqid") VALUES ('ICOMM_PC0012', NULL, '* 审核者：#{0}
* 类别：#{1}
* 名称：#{2}
* 时间：#{3}', NULL);
                 */
                String ICOMM_PC0002 = xxglService.getMsg("ICOMM_PC0002");
                String ICOMM_PC0012 = xxglService.getMsg("ICOMM_PC0012");
                if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0){
                    try{
                        int size = shgcDto.getSpgwcyDtos().size();
                        for(int i=0;i<size;i++){
                            if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),
                                        shgcDto.getSpgwcyDtos().get(i).getYhid(),
                                        shgcDto.getSpgwcyDtos().get(i).getYhm(),
                                        shgcDto.getSpgwcyDtos().get(i).getYhid(),
                                        ICOMM_PC0002,
                                        StringUtil.replaceMsg(ICOMM_PC0012,operator.getZsxm(),shgcDto.getShlbmc() ,dto.getMc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }else {
                //新增提交审核
                jxhsglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                /*
INSERT INTO "public"."matridx_xxgl" ("xxid", "xxlx", "xxnr", "ddqid") VALUES ('ICOMM_PC0003', NULL, '您有一个新的绩效核算审核任务', NULL);
INSERT INTO "public"."matridx_xxgl" ("xxid", "xxlx", "xxnr", "ddqid") VALUES ('ICOMM_PC0013', NULL, '# 您有一个新的审核任务
* 提交者：#{0}
* 类别：#{1}
* 名称：#{2}
* 时间：#{3}  ', NULL);
                 */
                String ICOMM_PC0003 = xxglService.getMsg("ICOMM_PC0003");
                String ICOMM_PC0013 = xxglService.getMsg("ICOMM_PC0013");
                if(shgcDto.getSpgwcyDtos() != null && shgcDto.getSpgwcyDtos().size() > 0){
                    try{
                        int size = shgcDto.getSpgwcyDtos().size();
                        for(int i=0;i<size;i++){
                            if(StringUtil.isNotBlank(shgcDto.getSpgwcyDtos().get(i).getYhm())){
                                talkUtil.sendWorkDyxxMessage(shgcDto.getShlb(),
                                        shgcDto.getSpgwcyDtos().get(i).getYhid(),
                                        shgcDto.getSpgwcyDtos().get(i).getYhm(),
                                        shgcDto.getSpgwcyDtos().get(i).getYhid(),
                                        ICOMM_PC0003,
                                        StringUtil.replaceMsg(ICOMM_PC0013,operator.getZsxm(),shgcDto.getShlbmc() ,dto.getMc(),DateUtils.getCustomFomratCurrentDate("HH:mm:ss")));
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
            dao.updateZt(jxhsglDto);
        }
        return true;
    }

    /**
     * 更新撤回对象
     *
     * @param shgcList   审核过程列表
     * @param operator   操作人
     * @param auditParam 审核传递的额外参数信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean updateAuditRecall(List<ShgcDto> shgcList, User operator, AuditParam auditParam) throws BusinessException {
        // TODO Auto-generated method stub
        if(shgcList == null || shgcList.isEmpty()){
            return true;
        }
//		String token = talkUtil.getToken();
        if(auditParam.isCancelOpe()) {
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String jxhsglid = shgcDto.getYwid();
                JxhsglDto jxhsglDto = new JxhsglDto();
                jxhsglDto.setXgry(operator.getYhid());
                jxhsglDto.setJxhsglid(jxhsglid);
                jxhsglDto.setZt(StatusEnum.CHECK_SUBMIT.getCode());
                dao.update(jxhsglDto);
            }
        }else{
            //审核回调方法
            for(ShgcDto shgcDto : shgcList){
                String jxhsglid = shgcDto.getYwid();
                JxhsglDto jxhsglDto = new JxhsglDto();
                jxhsglDto.setXgry(operator.getYhid());
                jxhsglDto.setJxhsglid(jxhsglid);
                jxhsglDto.setZt(StatusEnum.CHECK_NO.getCode());
                dao.update(jxhsglDto);
            }
        }
        return true;
    }

    /**
     * 查看审核历史的页面时，查找机构id信息
     *
     * @param shgcDto
     */
    @Override
    public Map<String, Object> requirePreAuditMember(ShgcDto shgcDto) throws BusinessException {
        return null;
    }

    /**
     * 返回审核相关的业务信息
     *
     * @param param
     */
    @Override
    public Map<String, Object> returnAuditServiceInfo(Map<String, Object> param) throws BusinessException {
        Map<String, Object> map =new HashMap<>();
        @SuppressWarnings("unchecked")
        List<String> ids = (List<String>)param.get("ywids");
        JxhsglDto jxhsglDto = new JxhsglDto();
        jxhsglDto.setIds(ids);
        List<JxhsglDto> dtoList = dao.getDtoList(jxhsglDto);
        List<String> list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(dtoList)){
            for(JxhsglDto dto:dtoList){
                list.add(dto.getLrry());
            }
        }
        map.put("list",list);
        return map;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean savePerformance(List<SwyszkDto> list,User user,JxhsglDto jxhsglDto){
        String jxshglid=null;
        boolean sfxg=false;
        BigDecimal jxzje=new BigDecimal("0");
        BigDecimal sd=new BigDecimal("0");//税点
        XtszDto xtszDto=new XtszDto();//获取税点
        xtszDto.setSzlb("crm.performance.taxpoint");
        xtszDto=xtszService.getDto(xtszDto);
        if(xtszDto!=null && StringUtil.isNotBlank(xtszDto.getSzz())){
            sd=new BigDecimal(xtszDto.getSzz());
        }
        List<JxhsmxDto> add_list=new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(SwyszkDto swyszkDto:list){
                swyszkDto.setXgry(user.getYhid());
                jxzje=jxzje.add(new BigDecimal(swyszkDto.getJxje()));
                JxhsmxDto jxhsmxDto=new JxhsmxDto();
                jxhsmxDto.setJxhsje(swyszkDto.getJxje());
                jxhsmxDto.setYjxhsje(swyszkDto.getJxje());
                jxhsmxDto.setYszkid(swyszkDto.getYszkid());
                if(StringUtil.isBlank(jxhsmxDto.getJxhsmxid())) {
                    jxhsmxDto.setLrry(user.getYhid());
                    jxhsmxDto.setJxhsmxid(StringUtil.generateUUID());
                    add_list.add(jxhsmxDto);
                }
                if(StringUtil.isNotBlank(swyszkDto.getJxhsglid())&&StringUtil.isBlank(jxshglid)) {
                    jxshglid = swyszkDto.getJxhsglid();
                    sd=new BigDecimal(swyszkDto.getSd());
                    sfxg=true;
                }
            }
            if(StringUtil.isBlank(jxshglid)){
                jxshglid=StringUtil.generateUUID();
            }
            for(JxhsmxDto jxhsmxDto:add_list){
                jxhsmxDto.setJxhsglid(jxshglid);
            }
        }
        if(!CollectionUtils.isEmpty(add_list)){
            jxhsmxService.insertDtos(add_list);
        }
        jxhsglDto.setSfhd("1");
        jxhsglDto.setJxksrq(jxhsglDto.getJxksrq()+"-01");//拼接起始日期
        YearMonth yearMonth = YearMonth.parse(jxhsglDto.getJxjsrq());
        LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
        jxhsglDto.setJxjsrq(String.valueOf(lastDayOfMonth));//拼接结束日期
        jxhsglDto.setHdry(user.getYhid());
        jxhsglDto.setJxhsglid(jxshglid);
        jxhsglDto.setJxhsze(String.valueOf(jxzje));
        jxhsglDto.setSd(String.valueOf(sd));
        jxhsglDto.setShze(String.valueOf(jxzje.multiply((new BigDecimal("100").subtract(sd)).divide(new BigDecimal("100"))).setScale(2, RoundingMode.HALF_UP)));
        if(sfxg){//若已经存在则进行合并
            jxhsglDto.setXgry(user.getXgry());
            dao.update(jxhsglDto);
        }else{//没有则进行创建
            jxhsglDto.setMc(user.getZsxm()+"-"+jxhsglDto.getJxksrq()+"~"+jxhsglDto.getJxjsrq()+"-绩效核算表");
            jxhsglDto.setFzr(user.getYhid());
            jxhsglDto.setZt(StatusEnum.CHECK_NO.getCode());
            jxhsglDto.setLrry(user.getYhid());
            dao.insert(jxhsglDto);
        }
        //更新应收账款绩效核算金额以及是否核算完成
        swyszkService.updateInvoiceList(list);
        return true;
    }
}
