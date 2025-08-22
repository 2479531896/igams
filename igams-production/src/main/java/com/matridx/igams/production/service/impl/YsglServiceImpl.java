package com.matridx.igams.production.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.matridx.igams.common.dao.BaseModel;
import com.matridx.igams.common.dao.entities.DdxxglDto;
import com.matridx.igams.common.dao.entities.ImportRecordModel;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.JgxxDto;
import com.matridx.igams.common.dao.entities.User;
import com.matridx.igams.common.dao.entities.XxdyDto;
import com.matridx.igams.common.data.DataPermission;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.DingMessageType;
import com.matridx.igams.common.enums.SsdwTableEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.BaseBasicServiceImpl;
import com.matridx.igams.common.service.svcinterface.ICommonService;
import com.matridx.igams.common.service.svcinterface.IDdxxglService;
import com.matridx.igams.common.service.svcinterface.IFileImport;
import com.matridx.igams.common.service.svcinterface.IJgxxService;
import com.matridx.igams.common.service.svcinterface.IXxdyService;
import com.matridx.igams.common.util.DingTalkUtil;
import com.matridx.igams.production.dao.entities.YsglDto;
import com.matridx.igams.production.dao.entities.YsglModel;
import com.matridx.igams.production.dao.entities.YsmxDto;
import com.matridx.igams.production.dao.matridxsql.GL_accvouchDao;
import com.matridx.igams.production.dao.post.IYsglDao;
import com.matridx.igams.production.service.svcinterface.IYsglService;
import com.matridx.igams.production.service.svcinterface.IYsmxService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class YsglServiceImpl extends BaseBasicServiceImpl<YsglDto, YsglModel, IYsglDao> implements IYsglService, IFileImport {

    private static final Logger log = LoggerFactory.getLogger(YsglServiceImpl.class);
    @Autowired
    private IYsmxService ysmxService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IJgxxService jgxxService;
    @Autowired
    GL_accvouchDao gl_accvouchDao;
    @Autowired
    ICommonService commonService;
    @Autowired
    IDdxxglService ddxxglService;
    @Autowired
    DingTalkUtil dingTalkUtil;
    @Autowired
    IXxdyService xxdyService;
    @Value("${matridx.prefix.urlprefix:}")
    private String urlPrefix;
    @Value("${matridx.wechat.applicationurl:}")
    private String applicationurl;
    /*
     * 功能：存储初始化数据
     */
    private Map<Long, Map<String,Object>> thread_initMap = new HashMap<>();
    @Override
    public List<Map<String, String>> getDepartments() {
        return dao.getDepartments();
    }

    @Override
    public List<Map<String, String>> getYears() {
        return dao.getYears();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean addSaveFinancialBudget(YsglDto ysglDto) {
        ysglDto.setYsglid(StringUtil.generateUUID());
        int inserted = dao.insert(ysglDto);
        if(inserted==0){
            return false;
        }
        List<YsmxDto> list = JSON.parseArray(ysglDto.getYsmx_json(), YsmxDto.class);
        if(list!=null&&!list.isEmpty()){
            for(YsmxDto dto:list){
                dto.setYsmxid(StringUtil.generateUUID());
                dto.setYsglid(ysglDto.getYsglid());
                dto.setLrry(ysglDto.getLrry());
            }
            boolean inserted1 = ysmxService.insertList(list);
            if(!inserted1){
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean modSaveFinancialBudget(YsglDto ysglDto) {
        int updated = dao.update(ysglDto);
        if(updated==0){
            return false;
        }
        YsmxDto ysmxDto=new YsmxDto();
        ysmxDto.setIds(ysglDto.getYsglid());
        ysmxDto.setScry(ysglDto.getXgry());
        ysmxService.delObsoleteData(ysmxDto);
        ysmxService.delete(ysmxDto);
        List<YsmxDto> list = JSON.parseArray(ysglDto.getYsmx_json(), YsmxDto.class);
        if(list!=null&&!list.isEmpty()){
            for(YsmxDto dto:list){
                dto.setYsmxid(StringUtil.generateUUID());
                dto.setYsglid(ysglDto.getYsglid());
                dto.setLrry(ysglDto.getXgry());
            }
            boolean inserted = ysmxService.insertList(list);
            if(!inserted){
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public boolean delFinancialBudget(YsglDto ysglDto) {
        int deleted = dao.delete(ysglDto);
        if(deleted==0){
            return false;
        }
        YsmxDto ysmxDto=new YsmxDto();
        ysmxDto.setIds(ysglDto.getIds());
        ysmxDto.setScry(ysglDto.getScry());
        boolean deleted1 = ysmxService.delete(ysmxDto);
        if(!deleted1){
            return false;
        }
        return true;
    }

    @Override
    public YsglDto saveVerification(YsglDto ysglDto) {
        return dao.saveVerification(ysglDto);
    }

    @Override
    public boolean existCheck(String fieldName, String value) {
        return false;
    }

    @Override
    public boolean insertImportRec(BaseModel baseModel, User user,int rowindex, StringBuffer errorMessages) throws BusinessException {
        YsglDto ysglDto=(YsglDto)baseModel;
        JgxxDto jgxxDto=new JgxxDto();
        jgxxDto.setJgmc(ysglDto.getBmmc());
        JgxxDto jgxx = jgxxService.getJgxxByJgmc(jgxxDto);
        if(jgxx!=null){
            ysglDto.setBm(jgxx.getJgid());
        }
        List<JcsjDto> xmdls = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_SUBJECT_TWO.getCode());
        List<JcsjDto> xmfls = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_SUB_SUBJECT_TWO.getCode());
        List<YsmxDto> ysmxDtos=new ArrayList<>();
        YsmxDto ysmxDto=new YsmxDto();
        ysmxDto.setYsmxid(StringUtil.generateUUID());
        ysmxDto.setYsje(ysglDto.getYsje());
        ysmxDto.setLrry(user.getYhid());
        if(!xmdls.isEmpty()){
            for(JcsjDto xmdl:xmdls){
                if(xmdl.getCsmc().equals(ysglDto.getKmdlmc())){
                    ysmxDto.setKmdl(xmdl.getCsid());
                    break;
                }
            }
        }
        if(!xmfls.isEmpty()){
            for(JcsjDto xmfl:xmfls){
                if(xmfl.getCsmc().equals(ysglDto.getKmflmc())){
                    ysmxDto.setKmfl(xmfl.getCsid());
                    break;
                }
            }
        }
        YsglDto dto = dao.saveVerification(ysglDto);
        if(dto!=null){
            ysmxDto.setYsglid(dto.getYsglid());
            ysmxDtos.add(ysmxDto);
            boolean inserted = ysmxService.insertList(ysmxDtos);
            if(!inserted){
                return false;
            }
        }else{
            ysglDto.setYsglid(StringUtil.generateUUID());
            ysglDto.setLrry(user.getYhid());
            ysmxDto.setYsglid(ysglDto.getYsglid());
            int inserted = dao.insert(ysglDto);
            if(inserted==0){
                return false;
            }
            ysmxDtos.add(ysmxDto);
            boolean inserted1 = ysmxService.insertList(ysmxDtos);
            if(!inserted1){
                return false;
            }
        }

        return true;
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
     private final Object object = new Object();
    /*
     * 功能：初始化数据
     */
    @Override
    public void InitData(boolean needSubject,boolean needDepartment,User user,YsglDto ysglDto) throws BusinessException {
        Map<String, Object> initMap = new HashMap<>();
        synchronized (object){
            if (thread_initMap.size()>10){
                throw new BusinessException("msg","系统繁忙，请稍后再试！");
            }
            thread_initMap.remove(ysglDto.getThreadId());
            thread_initMap.put(ysglDto.getThreadId(),initMap);
        }
        if ("1".equals(ysglDto.getSfbmxz())){
            DataPermission.addCurrentUser(ysglDto,user);
            DataPermission.addJsDdw(ysglDto, "ysgl", SsdwTableEnum.YSGL);
            //判断当前角色是否对单位进行限制
            if("1".equals(user.getDqjsdwxdbj())) {
                Map<String,Object> param = new HashMap<>();
                param.put("user", user);
                //取得授权的机构
                List<String> jgids = commonService.listOrgByYhid(param);
                if (CollectionUtils.isEmpty(jgids)){
                    throw new BusinessException("msg","当前用户未授权任何单位权限！");
                }
                ysglDto.setQxjgids(jgids);
            }
        }
        //获取账套信息
        List<JcsjDto> ztDtos = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_U8.getCode());
        initMap.put("ztDtos", ztDtos);
        //获取科目信息
        if (needSubject){
            for (JcsjDto ztDto : ztDtos) {
                //统计报表
                JcsjDto cwystj = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.REPORT_FORMS.getCode()).stream().filter(e -> e.getCsdm().contains("CWYSTJ")&&ztDto.getCsid().equals(e.getFcsid())).collect(Collectors.toList()).get(0);
                if (cwystj == null){
                    throw new BusinessException("msg","未找到"+ztDto.getCsmc()+"的预算统计报表信息！");
                }
                initMap.put(ztDto.getCsdm()+"cwystj", cwystj);
                //获取报表科目
                List<JcsjDto> bbkms = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_SUBJECT.getCode()).stream().filter(e -> cwystj.getCsid().equals(e.getFcsid())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(bbkms)){
                    throw new BusinessException("msg","未找到"+cwystj.getCsmc()+"的科目信息！");
                }
                initMap.put(ztDto.getCsdm()+"bbkms", bbkms);
                List<JcsjDto> bbzkms = new ArrayList<>();
                for (JcsjDto bbkm : bbkms) {
                    List<JcsjDto> bbzkmsF = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_SUB_SUBJECT.getCode()).stream().filter(e -> bbkm.getCsid().equals(e.getFcsid())).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(bbzkmsF)){
                        bbzkms.addAll(bbzkmsF);
                    }
                }
                initMap.put(ztDto.getCsdm()+"bbzkms", bbzkms);
            }
        }
        if (needDepartment){
            JgxxDto jgxxDto_Sel = new JgxxDto();
            jgxxDto_Sel.setWbcxid(user.getWbcxid());
            List<JgxxDto> jgxxList = jgxxService.getJgxx2List(jgxxDto_Sel);
            List<JgxxDto> jgxxDtos = jgxxService.queryByWbcx(jgxxDto_Sel);
            for (JgxxDto jgxxDto : jgxxDtos) {
                JgxxDto firstLevelJgxx = jgxxService.getFirstLevelJgidByJgxx(jgxxDto,jgxxDtos);
                initMap.put(jgxxDto.getJgid(), firstLevelJgxx);
            }
            for (JcsjDto ztDto : ztDtos) {
                List<JgxxDto> ztJgxxs = new ArrayList<>();
                for (JgxxDto jgxxDto : jgxxList) {
                    if (ztDto.getCskz3().equals(jgxxDto.getFbsbj())) {
                        ztJgxxs.add(jgxxDto);
                    }
                }
//                if (CollectionUtils.isEmpty(ztJgxxs)){
//                    throw new BusinessException("msg","未找到"+ztDto.getCsmc()+"的部门信息！");
//                }
                initMap.put(ztDto.getCsdm()+"ztJgxxs", ztJgxxs);
                String bmmc = "";
                if (!CollectionUtils.isEmpty(ysglDto.getQxjgids())){
                    //cskz2不为空，表示有特殊处理
                    if(StringUtil.isNotBlank(ztDto.getCskz2())){
                        //获取归属部门
                        List<String> qxjgids = ysglDto.getQxjgids();
                        JgxxDto jgxxDto = new JgxxDto();
                        jgxxDto.setJgmc(ztDto.getCskz2());
                        jgxxDto.setFbsbj(ztDto.getCskz3());
                        jgxxDto.setWbcxid(user.getWbcxid());
                        bmmc = getJsmc(qxjgids,jgxxDto);
                    }
                    List<String> qxjgdms = new ArrayList<>();
                    for (JgxxDto ztJgxx : ztJgxxs) {
                        for (String qxjgid : ysglDto.getQxjgids()) {
                            if (ztJgxx.getJgid().equals(qxjgid)){
                                qxjgdms.addAll(Arrays.asList(ztJgxx.getKzcs2().split(",")));
                                break;
                            }
                        }
                    }
                    initMap.put(ztDto.getCsdm()+"qxjgdms", qxjgdms);
                }else{
                    if(StringUtil.isNotBlank(ztDto.getCskz2())){
                        List<String> qxjgids = new ArrayList<>();
                        JgxxDto jgxxDto = new JgxxDto();
                        jgxxDto.setJgmc(ztDto.getCskz2());
                        jgxxDto.setFbsbj(ztDto.getCskz3());
                        jgxxDto.setWbcxid(user.getWbcxid());
                        bmmc = getJsmc(qxjgids,jgxxDto);
                    }
                }
                initMap.put(ztDto.getCsdm()+"bmmc", bmmc);
            }
        }
    }
    /*
     * 功能：清除初始化数据
     */
    @Override
    public void delInitData(long threadId) {
        thread_initMap.remove(threadId);
    }

    /*
     * 功能：获取预算与实际支出信息
     */
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getBudgetAndActualExpenses(YsglDto ysglDto) throws BusinessException {
        Map<String, Object> initMap = thread_initMap.get(ysglDto.getThreadId());
        // 预算支出
        double budgetExpenses = dao.getBudgetExpenses(ysglDto);
        if ("month".equals(ysglDto.getTjlx())){
            budgetExpenses = new BigDecimal(budgetExpenses).divide(new BigDecimal(12), 2, RoundingMode.HALF_UP).doubleValue();
        }
        StringBuilder allSql = new StringBuilder();
        //获取账套信息
        List<JcsjDto> ztDtos = (List<JcsjDto>)initMap.get("ztDtos");
        allSql.append("SELECT COALESCE(SUM(tmp.md),0) from ( ");
        for (JcsjDto ztDto : ztDtos) {
            List<String> qxjgdms = (List<String>) initMap.get(ztDto.getCsdm() + "qxjgdms");
            if ("1".equals(ysglDto.getSfbmxz()) && CollectionUtils.isEmpty(qxjgdms))
                continue;
            List<JcsjDto> bbzkms = (List<JcsjDto>) initMap.get(ztDto.getCsdm() + "bbzkms");
            String bmmc = (String) initMap.get(ztDto.getCsdm() + "bmmc");
            allSql.append(" SELECT COALESCE(SUM(gl.md) ,0) md from " + ztDto.getCskz1() + "GL_accvouch gl where gl.dbill_date >= cast('" + ysglDto.getRqStart() + "' as date) and gl.dbill_date  < DATEADD(day,1,cast('" + ysglDto.getRqEnd() + "' as date)) ");
            if(StringUtil.isBlank(bmmc)){
                allSql.append("AND (gl.cdept_id is not null)");
            }
            allSql.append("AND (");
            for (JcsjDto bbzkm : bbzkms) {
                String[] csdmSplit = bbzkm.getCsdm().split(",");
                for (String csdm : csdmSplit) {
                    allSql.append("gl.ccode like '" + csdm + "%' OR ");
                }
            }
            allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf("OR ")));
            allSql.append(")");
            if (!CollectionUtils.isEmpty(qxjgdms)) {
                allSql.append(" AND ");
                if(StringUtil.isNotBlank(bmmc)){
                    allSql.append("(");
                }
                allSql.append(" gl.cdept_id in (");
                for (String qxjgdm : qxjgdms) {
                    allSql.append("'" + qxjgdm + "',");
                }
                allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf(",")));
                allSql.append(") ");
                if(StringUtil.isNotBlank(bmmc)){
                    allSql.append(" OR gl.cdept_id is null) ");
                }
            }
            allSql.append("UNION ALL");
        }
        allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf("UNION ALL")));
        allSql.append(")tmp");
        String sql = String.valueOf(allSql);
        checkSql(sql);
        // 实际支出
        double actualExpenses = gl_accvouchDao.getActualExpenses(sql);
        Map<String, Object> map = new HashMap<>();
        map.put("budgetExpenses", budgetExpenses);
        map.put("actualExpenses", actualExpenses);
        return map;
    }
    /*
     * 功能：获取部门实际费用信息
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getDepartmentActualExpenses(YsglDto ysglDto) throws BusinessException {
        Map<String, Object> initMap = thread_initMap.get(ysglDto.getThreadId());
        StringBuilder allSql = new StringBuilder();
        //获取账套信息
        List<JcsjDto> ztDtos = (List<JcsjDto>)initMap.get("ztDtos");
        allSql.append("SELECT COALESCE(SUM(tmp.md),0) md,tmp.cDepName from ( ");
        for (JcsjDto ztDto : ztDtos) {
            List<String> qxjgdms = (List<String>) initMap.get(ztDto.getCsdm() + "qxjgdms");
            if ("1".equals(ysglDto.getSfbmxz()) && CollectionUtils.isEmpty(qxjgdms))
                continue;
            String bmmc = (String) initMap.get(ztDto.getCsdm() + "bmmc");
            List<JcsjDto> bbzkms = (List<JcsjDto>) initMap.get(ztDto.getCsdm() + "bbzkms");
            allSql.append(" SELECT COALESCE(SUM(gl.md),0) md,");
            allSql.append("COALESCE(dep.cDepName,'" + bmmc + "') cDepName from " + ztDto.getCskz1() + "GL_accvouch gl left join (");
            List<JgxxDto> ztJgxxs = (List<JgxxDto>) initMap.get(ztDto.getCsdm() + "ztJgxxs");
            for (JgxxDto ztJgxx : ztJgxxs) {
                JgxxDto firstLevelJgxx = (JgxxDto) initMap.get(ztJgxx.getJgid());
                String jgdms = ztJgxx.getKzcs2();
                String[] jgdmSplit = jgdms.split(",");
                for (String jgdm : jgdmSplit) {
                    if ("1".equals(ysglDto.getSfbmxz())) {
                        allSql.append(" SELECT '" + jgdm + "' as jgdm,'" + ztJgxx.getJgmc() + "' cDepName  UNION ALL");
                    } else {
                        allSql.append(" SELECT '" + jgdm + "' as jgdm,'" + firstLevelJgxx.getJgmc() + "' cDepName  UNION ALL");
                    }
                }
            }
            allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf("UNION ALL")));
            allSql.append(") dep on dep.jgdm = gl.cdept_id  where gl.dbill_date >= cast('" + ysglDto.getRqStart() + "' as date) and gl.dbill_date  < DATEADD(day,1,cast('" + ysglDto.getRqEnd() + "' as date))");
            if(StringUtil.isBlank(bmmc)){
                allSql.append("and (dep.jgdm is not null)");
            }
            allSql.append("AND (");
            for (JcsjDto bbzkm : bbzkms) {
                String[] csdmSplit = bbzkm.getCsdm().split(",");
                for (String csdm : csdmSplit) {
                    allSql.append("gl.ccode like '" + csdm + "%' OR ");
                }
            }

            allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf("OR ")));
            allSql.append(") ");
            if (!CollectionUtils.isEmpty(qxjgdms)) {
                allSql.append(" AND ");
                if(StringUtil.isNotBlank(bmmc)){
                    allSql.append("(");
                }
                allSql.append(" gl.cdept_id in (");
                for (String qxjgdm : qxjgdms) {
                    allSql.append("'" + qxjgdm + "',");
                }
                allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf(",")));
                allSql.append(") ");
                if(StringUtil.isNotBlank(bmmc)){
                    allSql.append(" OR gl.cdept_id is null)");
                }
            }
            allSql.append(" group by dep.cDepName ");
            allSql.append("UNION ALL");
        }
        allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf("UNION ALL")));
        allSql.append(")tmp group by tmp.cDepName");
        String sql = String.valueOf(allSql);
        checkSql(sql);
        // 实际支出
        List<Map<String, Object>> departmentActualExpenses = gl_accvouchDao.getDepartmentActualExpenses(sql);
        initMap.put("departmentActualExpenses", departmentActualExpenses);
        return departmentActualExpenses;
    }
    /*
     * 功能：获取科目实际费用信息
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getSubjectActualExpenses(YsglDto ysglDto) throws BusinessException {
        Map<String, Object> initMap = thread_initMap.get(ysglDto.getThreadId());
        StringBuilder allSql = new StringBuilder();
        //获取账套信息
        List<JcsjDto> ztDtos = (List<JcsjDto>)initMap.get("ztDtos");
        allSql.append("SELECT COALESCE(SUM(tmp.md),0) md,tmp.ccodeName from ( ");
        for (JcsjDto ztDto : ztDtos) {
            List<String> qxjgdms = (List<String>) initMap.get(ztDto.getCsdm() + "qxjgdms");
            String bmmc = (String) initMap.get(ztDto.getCsdm() + "bmmc");
            if ("1".equals(ysglDto.getSfbmxz()) && CollectionUtils.isEmpty(qxjgdms))
                continue;
            List<JcsjDto> bbzkms = (List<JcsjDto>) initMap.get(ztDto.getCsdm() + "bbzkms");
            allSql.append(" SELECT COALESCE(SUM(gl.md) ,0) md,CASE ");
            for (JcsjDto bbzkm : bbzkms) {
                String[] csdmSplit = bbzkm.getCsdm().split(",");
                for (String csdm : csdmSplit) {
                    allSql.append(" WHEN gl.ccode like '" + csdm + "%' THEN '" + bbzkm.getCskz2() + "'");
                }
            }
            allSql.append(" END ccodeName");
            allSql.append(" from " + ztDto.getCskz1() + "GL_accvouch gl where gl.dbill_date >= cast('" + ysglDto.getRqStart() + "' as date) and gl.dbill_date  < DATEADD(day,1,cast('" + ysglDto.getRqEnd() + "' as date)) ");
            allSql.append("AND (");
            for (JcsjDto bbzkm : bbzkms) {
                String[] csdmSplit = bbzkm.getCsdm().split(",");
                for (String csdm : csdmSplit) {
                    allSql.append("gl.ccode like '" + csdm + "%' OR ");
                }
            }
            allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf("OR ")));
            allSql.append(") ");
            if(StringUtil.isBlank(bmmc)){
                allSql.append("and  (gl.cdept_id is not null)");
            }
            if (!CollectionUtils.isEmpty(qxjgdms)) {
                allSql.append(" AND ");
                if(StringUtil.isNotBlank(bmmc)){
                    allSql.append("(");
                }
                allSql.append(" gl.cdept_id in (");
                for (String qxjgdm : qxjgdms) {
                    allSql.append("'" + qxjgdm + "',");
                }
                allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf(",")));
                allSql.append(") ");
                if(StringUtil.isNotBlank(bmmc)){
                    allSql.append(" OR gl.cdept_id is null)");
                }
            }
            allSql.append(" group by gl.ccode");
            allSql.append(" UNION ALL");
        }
        allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf("UNION ALL")));
        allSql.append(")tmp group by tmp.ccodeName");
        String sql = String.valueOf(allSql);
        checkSql(sql);
        // 实际支出
        List<Map<String, Object>> subjectActualExpenses = gl_accvouchDao.getSubjectActualExpenses(sql);
        initMap.put("subjectActualExpenses", subjectActualExpenses);
        return subjectActualExpenses;
    }
    /*
     * 功能：获取部门预算进度信息
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getDepartmentBudgetProgress(YsglDto ysglDto) throws BusinessException {
        Map<String, Object> initMap = thread_initMap.get(ysglDto.getThreadId());
        List<Map<String, Object>> progressList = dao.getBudgetExpensesGroupByDepartment(ysglDto);
        if (!"1".equals(ysglDto.getSfbmxz())){
            List<Map<String, Object>> progressListZ = new ArrayList<>();
            for (Map<String, Object> map : progressList) {
                JgxxDto firstLevelJgxx = (JgxxDto)initMap.get(String.valueOf(map.get("jgid")));
                map.put("departmentname", firstLevelJgxx.getJgmc());
                boolean isExist = false;
                for (Map<String, Object> zMap : progressListZ) {
                    if (zMap.get("departmentname")!=null && zMap.get("departmentname").equals(map.get("departmentname"))){
                        isExist = true;
                        zMap.put("budget", new BigDecimal(zMap.get("budget")!=null?String.valueOf(zMap.get("budget")):"0").add(new BigDecimal(map.get("budget")!=null?String.valueOf(map.get("budget")):"0")).doubleValue());
                    }
                }
                if (!isExist){
                    progressListZ.add(map);
                }
            }
            progressList = progressListZ;
        }
        List<Map<String, Object>> departmentActualExpenses = (List<Map<String, Object>>)initMap.get("departmentActualExpenses");
        if (departmentActualExpenses==null){
            departmentActualExpenses = this.getDepartmentActualExpenses(ysglDto);
        }
        for (Map<String, Object> map : progressList) {
            if ("month".equals(ysglDto.getTjlx())){
                map.put("budget", new BigDecimal(String.valueOf(map.get("budget"))).divide(new BigDecimal(12), 2, RoundingMode.HALF_UP).doubleValue());
            }
            if ("season".equals(ysglDto.getTjlx())){
                map.put("budget", new BigDecimal(String.valueOf(map.get("budget"))).divide(new BigDecimal(4), 2, RoundingMode.HALF_UP).doubleValue());
            }
            for (Map<String, Object> departmentActualExpens : departmentActualExpenses) {
                if (map.get("departmentname").equals(departmentActualExpens.get("cDepName"))){
                    map.put("actual", departmentActualExpens.get("md"));
                    break;
                }
            }
        }
        return progressList;
    }
    /*
     * 功能：获取科目预算进度信息
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getSubjectBudgetProgress(YsglDto ysglDto) throws BusinessException {
        Map<String, Object> initMap = thread_initMap.get(ysglDto.getThreadId());
        List<Map<String, Object>> progressList = dao.getBudgetExpensesGroupBySubject(ysglDto);
        List<Map<String, Object>> subjectActualExpenses = (List<Map<String, Object>>)initMap.get("subjectActualExpenses");
        if (subjectActualExpenses==null){
            subjectActualExpenses = this.getSubjectActualExpenses(ysglDto);
        }
        for (Map<String, Object> map : progressList) {
            if ("month".equals(ysglDto.getTjlx())){
                map.put("budget", new BigDecimal(String.valueOf(map.get("budget"))).divide(new BigDecimal(12), 2, RoundingMode.HALF_UP).doubleValue());
            }
            for (Map<String, Object> subjectActualExpens : subjectActualExpenses) {
                if (map.get("subjectname").equals(subjectActualExpens.get("ccodeName"))){
                    map.put("actual", subjectActualExpens.get("md"));
                    break;
                }
            }
        }
        return progressList;
    }
    /*
     * 功能：获取科目预算支出明细信息
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getSubjectExpenseDetail(YsglDto ysglDto) throws BusinessException {
        Map<String, Object> initMap = thread_initMap.get(ysglDto.getThreadId());
        List<Map<String, Object>> subjectBudgetExpenseDetail = dao.getSubjectBudgetExpenseDetail(ysglDto);
        StringBuilder allSql = new StringBuilder();
        //获取账套信息
        List<JcsjDto> ztDtos = (List<JcsjDto>)initMap.get("ztDtos");
        allSql.append("SELECT COALESCE(SUM(tmp.md),0) md,tmp.ccodeName from ( ");
        for (JcsjDto ztDto : ztDtos) {
            List<String> qxjgdms = (List<String>) initMap.get(ztDto.getCsdm() + "qxjgdms");
            String bmmc = (String) initMap.get(ztDto.getCsdm() + "bmmc");
            if ("1".equals(ysglDto.getSfbmxz()) && CollectionUtils.isEmpty(qxjgdms))
                continue;
            List<JcsjDto> bbzkms = (List<JcsjDto>) initMap.get(ztDto.getCsdm() + "bbzkms");
            allSql.append(" SELECT COALESCE(SUM(gl.md) ,0) md,CASE ");
            for (JcsjDto bbzkm : bbzkms) {
                String[] csdmSplit = bbzkm.getCsdm().split(",");
                for (String csdm : csdmSplit) {
                    allSql.append(" WHEN gl.ccode like '" + csdm + "%' THEN '" + bbzkm.getCsmc() + "'");
                }
            }
            allSql.append(" END ccodeName");
            allSql.append(" from " + ztDto.getCskz1() + "GL_accvouch gl where gl.dbill_date >= cast('" + ysglDto.getRqStart() + "' as date) and gl.dbill_date  < DATEADD(day,1,cast('" + ysglDto.getRqEnd() + "' as date)) ");
            allSql.append("AND (");
            for (JcsjDto bbzkm : bbzkms) {
                String[] csdmSplit = bbzkm.getCsdm().split(",");
                for (String csdm : csdmSplit) {
                    allSql.append("gl.ccode like '" + csdm + "%' OR ");
                }
            }
            allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf("OR ")));
            allSql.append(") ");
            if(StringUtil.isBlank(bmmc)){
                allSql.append(" AND (gl.cdept_id is not null)");
            }
            if (!CollectionUtils.isEmpty(qxjgdms)) {
                allSql.append(" AND ");
                if(StringUtil.isNotBlank(bmmc)){
                    allSql.append("(");
                }
                allSql.append(" gl.cdept_id in (");
                for (String qxjgdm : qxjgdms) {
                    allSql.append("'" + qxjgdm + "',");
                }
                allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf(",")));
                allSql.append(") ");
                if(StringUtil.isNotBlank(bmmc)){
                    allSql.append(" OR gl.cdept_id is null)");
                }
            }
            allSql.append(" group by gl.ccode");
            allSql.append(" UNION ALL");
        }
        allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf("UNION ALL")));
        allSql.append(")tmp group by tmp.ccodeName");
        String sql = String.valueOf(allSql);
        checkSql(sql);
        // 实际支出明细
        List<Map<String, Object>> subjectActualExpensesDetail = gl_accvouchDao.getSubjectActualExpensesDetail(sql);
        for (Map<String, Object> map : subjectBudgetExpenseDetail) {
            if ("month".equals(ysglDto.getTjlx())){
                map.put("budget", new BigDecimal(String.valueOf(map.get("budget"))).divide(new BigDecimal(12), 2, RoundingMode.HALF_UP).doubleValue());
            }
            for (Map<String, Object> subjectActualExpens : subjectActualExpensesDetail) {
                if (map.get("subjectchildname").equals(subjectActualExpens.get("ccodeName"))){
                    map.put("actual", subjectActualExpens.get("md"));
                    break;
                }
            }
        }
        return subjectBudgetExpenseDetail;
    }

    /**
     * 若存在insert，delete等高风险语句，进行return,
     */
    public void checkSql(String sql) throws BusinessException {
        String checkSql = sql.toLowerCase();
        if(checkSql.contains("insert")) {
            throw new BusinessException("msg","存在高风险insert语句");
        }else if(checkSql.contains("delete")) {
            throw new BusinessException("msg","存在高风险delete语句");
        }else if(checkSql.contains("update")) {
            throw new BusinessException("msg","存在高风险update语句");
        }else if(checkSql.contains("alert")) {
            throw new BusinessException("msg","存在高风险alert语句");
        }else if(checkSql.contains("drop")) {
            throw new BusinessException("msg","存在高风险drop语句");
        }
    }

    /**
     * 定时任务项预算、花费存入redis
     * @throws BusinessException
     */
    public void setFinancialStatisticsToRedis() {
        //获取各机构科目年度预算
        YsglDto ysglDto = new YsglDto();
        ysglDto.setNd(DateUtils.getCustomFomratCurrentDate("yyyy"));
        List<JgxxDto> jgxxList = jgxxService.getJgxx2ListAll();
        List<Map<String, Object>> progressList = dao.getDepartmentSubjectBudgetDetail(ysglDto);
        // 获取各机构科目支出
        List<Map<String, Object>> expenses = getDepartmentSubjectExpenseDetail(jgxxList.stream().filter(item -> StringUtil.isNotBlank(item.getKzcs2())).collect(Collectors.toList()));
        Map<String, Object> endMap = new HashMap<>();
        //jgxxList去重

        List<JgxxDto> unrepeatedJgmcList = new ArrayList<>();
        List<JgxxDto> unrepeatedJgidList = new ArrayList<>();
        for (JgxxDto jgxxDto : jgxxList) {
            if (CollectionUtils.isEmpty(unrepeatedJgmcList)){
                unrepeatedJgmcList.add(jgxxDto);
            } else {
                if (!unrepeatedJgmcList.stream().filter(item -> item.getJgmc().equals(jgxxDto.getJgmc())).findAny().isPresent()) {
                    unrepeatedJgmcList.add(jgxxDto);
                }
            }
            if (CollectionUtils.isEmpty(unrepeatedJgidList)){
                unrepeatedJgidList.add(jgxxDto);
            } else {
                if (!unrepeatedJgidList.stream().filter(item -> item.getJgid().equals(jgxxDto.getJgid())).findAny().isPresent()) {
                    unrepeatedJgidList.add(jgxxDto);
                }
            }
        }
        for (JgxxDto jgxxDto : unrepeatedJgidList) {
            if (endMap.get(jgxxDto.getJgid()) != null){
                continue;
            }
            //获取机构的预算信息
            List<Map<String, Object>> jg_progress_list = progressList.stream().filter(progress -> jgxxDto.getJgid().equals(progress.get("jgid"))).collect(Collectors.toList());
            dealJgxxInfo(jgxxDto,jg_progress_list,null,jgxxList,endMap);
        }
        for (JgxxDto jgxxDto : unrepeatedJgmcList) {
            //获取机构的预算信息
            List<Map<String, Object>> jg_expenses_list = expenses.stream().filter(expense -> jgxxDto.getJgmc().equals(expense.get("cDepName"))).collect(Collectors.toList());
            dealJgxxInfo(jgxxDto,null,jg_expenses_list,jgxxList,endMap);
        }
        for (String key : endMap.keySet()) {
            redisUtil.hset("FINANCIALSTATISTICS", key, JSON.toJSONString(endMap.get(key)),-1);
        }
    }

    /**
     * 定时任务项预算、花费存入redis
     * @throws BusinessException
     */
    public void sendSubjectBudgetDetailMessage(){
        List<DdxxglDto> ddxxglDtolist = ddxxglService.selectByDdxxlx(DingMessageType.EXPENSE_BUDGET_MESSAGE.getCode());
        if (!CollectionUtils.isEmpty(ddxxglDtolist)) {
            for (DdxxglDto ddxxglDto : ddxxglDtolist) {
                String yhm = ddxxglDto.getYhm();
                String ddid = ddxxglDto.getDdid();
                String jgid = "";
                User user = redisUtil.hugetDto("Users", yhm);
                if (user != null && StringUtil.isNotBlank(user.getYhid())){
                    XxdyDto xxdyDto = new XxdyDto();
                    xxdyDto.setDylxcsdm("YHJG");
                    xxdyDto.setDyxx(user.getYhid());
                    XxdyDto info = xxdyService.getDto(xxdyDto);
                    if (info != null && StringUtil.isNotBlank(info.getDyxx())) {
                        jgid = info.getYxx();
                    }
                }
                if (StringUtil.isBlank(jgid)){
                    if (StringUtil.isNotBlank(jgid) && user != null && StringUtil.isNotBlank(user.getJgid())){
                        jgid = user.getJgid();
                    }else {
                        log.error("sendSubjectBudgetDetailMessage未从REDIS中获取到用户的机构ID:{}==={}", yhm, ddxxglDto.getYhid());
                        User user_search = new User();
                        user_search.setYhid( ddxxglDto.getYhid());
                        User userInfoById =commonService.getUserInfoById(user_search);
                        if (userInfoById != null && StringUtil.isNotBlank(userInfoById.getJgid())){
                            jgid = userInfoById.getJgid();
                        }else {
                            log.error("sendSubjectBudgetDetailMessage未从数据库中获取到用户的机构ID:{}==={}", yhm,ddxxglDto.getYhid());
                            continue;
                        }
                    }
                }
                // 内网访问
                String internalbtn = applicationurl+urlPrefix+"/ws/financialStatistics/pagedataFinancialStatisticsOnPhone?jgid="+jgid;
                //外网访问
                List<OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList> btnJsonLists = new ArrayList<>();
                OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList btnJsonList = new OapiMessageCorpconversationAsyncsendV2Request.BtnJsonList();
                btnJsonList.setTitle("查看");
                btnJsonList.setActionUrl(internalbtn);
                btnJsonLists.add(btnJsonList);
                dingTalkUtil.sendCardMessage(yhm,ddid,"部门预算及其使用情况","部门预算及其使用情况",
                        btnJsonLists,"1");
            }
        }
    }

    public Map<String,Object> getDetail(String jgid){
        Map<String, Object> map = new HashMap<>();
        String redisKey = "FINANCIALSTATISTICS";
        Object budgetObj = redisUtil.hget(redisKey, jgid);
        List<Map<String,Object>> subjectChildList = new ArrayList<>();
        BigDecimal budgetYear = new BigDecimal(0.00);
        BigDecimal expensesYear = new BigDecimal(0.00);
        BigDecimal expensesSeason = new BigDecimal(0.00);
        BigDecimal expensesMonth = new BigDecimal(0.00);
        String departmentName = "";
        List<Map<String,Object>> subjectList = new ArrayList<>();
        if (budgetObj != null){
            List<Map<String,Object>> budgets = JSONArray.parseObject(budgetObj.toString(),new TypeReference<List<Map<String, Object>>>() {});
            for (Map<String, Object> budget : budgets) {
                if (StringUtil.isBlank(departmentName) && budget.get("departmentname") != null){
                    departmentName = budget.get("departmentname").toString();
                }
                Map<String,Object> subjectChild = new HashMap<>();
                subjectChild.put("subjectname",budget.get("subjectname"));
                subjectChild.put("subjectchildname",budget.get("subjectchildname"));
                subjectChild.put("name",budget.get("subjectname") + ":" + budget.get("subjectchildname"));
                BigDecimal budgetValue = new BigDecimal(0);
                BigDecimal expensesValue = new BigDecimal(0);
                if (budget.get("budget") != null) {
                    budgetYear = budgetYear.add(new BigDecimal(budget.get("budget").toString()));
                    budgetValue = new BigDecimal(budget.get("budget").toString()).divide(new BigDecimal(10000),2,RoundingMode.HALF_UP);
                }
                if (budget.get("expenses_year") != null) {
                    expensesYear = expensesYear.add(new BigDecimal(budget.get("expenses_year").toString()));
                    expensesValue = new BigDecimal(budget.get("expenses_year").toString()).divide(new BigDecimal(10000),2,RoundingMode.HALF_UP);
                }
                if (budget.get("expenses_season") != null) {
                    expensesSeason = expensesSeason.add(new BigDecimal(budget.get("expenses_season").toString()));
                }
                if (budget.get("expenses_month") != null) {
                    expensesMonth = expensesMonth.add(new BigDecimal(budget.get("expenses_month").toString()));
                }
                subjectChild.put("budget",budgetValue.setScale(2,RoundingMode.HALF_UP).toString());
                subjectChild.put("expenses",expensesValue.setScale(2,RoundingMode.HALF_UP).toString());
                subjectChild.put("difference",expensesValue.subtract(budgetValue).setScale(2,RoundingMode.HALF_UP).toString());
                subjectChild.put("divide",(budgetValue.compareTo(new BigDecimal(0)) > 0) ? (expensesValue.subtract(budgetValue).multiply(new BigDecimal(100)).divide(budgetValue,2,RoundingMode.HALF_UP).toString()) : ((expensesValue.compareTo(new BigDecimal(0)) > 0) ?"100.00":"0.00"));
                boolean isOver = false;
                if (expensesValue.compareTo(budgetValue) > 0){
                    isOver = true;
                }
                subjectChild.put("isOver",isOver);
                subjectChildList.add(subjectChild);

                Map<String, Object> subjectMap = new HashMap<>();
                if (!CollectionUtils.isEmpty(subjectList)){
                    Optional<Map<String, Object>> first = subjectList.stream().filter(item -> item.get("subjectname").toString().equals(budget.get("subjectname"))).findFirst();
                    if (first.isPresent()){
                        subjectMap = first.get();
                        subjectList.remove(subjectMap);
                    }
                }
                subjectMap.put("subjectname",budget.get("subjectname"));
                BigDecimal subject_budgetYear = new BigDecimal(0.00);
                BigDecimal subject_expensesYear = new BigDecimal(0.00);
                if (subjectMap.get("budget") != null) {
                    subject_budgetYear = new BigDecimal(subjectMap.get("budget").toString());
                }
                if (subjectMap.get("expenses") != null) {
                    subject_budgetYear = new BigDecimal(subjectMap.get("expenses").toString());
                }
                if (budget.get("budget") != null) {
                    subject_budgetYear = subject_budgetYear.add(new BigDecimal(budget.get("budget").toString()).divide(new BigDecimal(10000),2,RoundingMode.HALF_UP));
                }
                if (budget.get("expenses_year") != null) {
                    subject_expensesYear = subject_expensesYear.add(new BigDecimal(budget.get("expenses_year").toString()).divide(new BigDecimal(10000),2,RoundingMode.HALF_UP));
                }
                subjectMap.put("budget",subject_budgetYear);
                subjectMap.put("expenses",subject_expensesYear);
                BigDecimal subjectDivide = subject_budgetYear.compareTo(new BigDecimal(0)) > 0?(subject_budgetYear.compareTo(new BigDecimal(0)) > 0?subject_expensesYear.divide(subject_budgetYear, 2, RoundingMode.DOWN):new BigDecimal(2)):new BigDecimal(0);
                subjectMap.put("divide",subjectDivide.multiply(new BigDecimal(100)));
                subjectMap.put("color",subjectDivide.compareTo(new BigDecimal(1)) > 0?"red":subjectDivide.compareTo(getDayOfYearPercent()) > 1 ?"orange":"#4CAF50");
                subjectList.add(subjectMap);
            }
        } else {
            log.error("getSubjectBudgetDetail未从REDIS中获取到相关机构的预算数据:{}", jgid);
        }
        map.put("subjectList",subjectList);
        expensesYear = expensesYear.divide(new BigDecimal(10000),2,RoundingMode.HALF_UP);
        expensesSeason = expensesSeason.divide(new BigDecimal(10000),2,RoundingMode.HALF_UP);
        expensesMonth = expensesMonth.divide(new BigDecimal(10000),2,RoundingMode.HALF_UP);
        BigDecimal budgetSeason = budgetYear.divide(new BigDecimal(30000),2,RoundingMode.HALF_UP);//季度预算,万元
        BigDecimal budgetMonth = budgetYear.divide(new BigDecimal(120000),2,RoundingMode.HALF_UP);//月度预算,万元
        budgetYear = budgetYear.divide(new BigDecimal(10000),2,RoundingMode.HALF_UP);//年度预算,万元
        BigDecimal yearDivide = expensesYear.compareTo(new BigDecimal(0)) > 0?(budgetYear.compareTo(new BigDecimal(0)) > 0?expensesYear.divide(budgetYear, 2, RoundingMode.DOWN):new BigDecimal(2)):new BigDecimal(0);
        BigDecimal seasonDivide = expensesSeason.compareTo(new BigDecimal(0)) > 0?(budgetSeason.compareTo(new BigDecimal(0)) > 0?expensesSeason.divide(budgetSeason, 2, RoundingMode.DOWN):new BigDecimal(2)):new BigDecimal(0);
        BigDecimal monthDivide = expensesMonth.compareTo(new BigDecimal(0)) > 0?(budgetMonth.compareTo(new BigDecimal(0)) > 0?expensesMonth.divide(budgetMonth, 2, RoundingMode.DOWN):new BigDecimal(2)):new BigDecimal(0);
        map.put("budgetYear",budgetYear.toString());
        map.put("budgetSeason",budgetSeason.toString());
        map.put("budgetMonth",budgetMonth.toString());
        map.put("expensesYear",expensesYear.toString());
        map.put("expensesSeason",expensesSeason.toString());
        map.put("expensesMonth",expensesMonth.toString());
        map.put("colorYear",yearDivide.compareTo(new BigDecimal(1)) > 0?"red":yearDivide.compareTo(getDayOfYearPercent()) > 1 ?"orange":"#4CAF50");
        map.put("colorSeason",seasonDivide.compareTo(new BigDecimal(1)) > 0?"red":seasonDivide.compareTo(getDayOfSeasonPercent()) > 1 ?"orange":"#4CAF50");
        map.put("colorMonth",monthDivide.compareTo(new BigDecimal(1)) > 0?"red":monthDivide.compareTo(getDayOfMonthPercent()) > 1 ?"orange":"#4CAF50");
        map.put("yearDivide",yearDivide.multiply(new BigDecimal(100)));
        map.put("seasonDivide",seasonDivide.multiply(new BigDecimal(100)));
        map.put("monthDivide",monthDivide.multiply(new BigDecimal(100)));
        // 根据 name 进行排序
        map.put("subjectChildList",subjectChildList.stream().sorted(Comparator.comparing(o -> o.get("name").toString())).collect(Collectors.toList()));
        map.put("departmentName",departmentName);
        return map;
    }

    /**
     * 获取部门、科目详细预算
     * @param ysglDto
     * @return
     * @throws BusinessException
     */
    public List<Map<String, Object>> getDepartmentSubjectBudgetDetail(YsglDto ysglDto) throws BusinessException {
        return dao.getDepartmentSubjectBudgetDetail(ysglDto);
    }

    /**
     * 处理预算
     * @param jgxxDto
     * @param progress
     * @param expenses
     * @param jgxxList
     * @param endMap
     */
    public void dealJgxxInfo(JgxxDto jgxxDto, List<Map<String, Object>> progress, List<Map<String, Object>> expenses, List<JgxxDto> jgxxList, Map<String, Object> endMap){
        String jgid = jgxxDto.getJgid();
        String departmentname = jgxxDto.getJgmc();
        List<Map<String, Object>> list = new ArrayList<>();//子科目预决算List
        //查找endlist是否已存在机构的预算费用信息
        Object o = endMap.get(jgid);
        if ( o != null){
            list = (List<Map<String, Object>>) o;
        }
        if (!CollectionUtils.isEmpty(progress)){
            //处理预算
            for (Map<String, Object> progres : progress) {
                Map<String, Object> map = new HashMap<>();
                if (!CollectionUtils.isEmpty(list)){
                    //查找已存在机构的预算信息中是否存在同样科目的预算信息
                    Optional<Map<String, Object>> first = list.stream().filter(end -> progres.get("subjectname").equals(end.get("subjectname")) && progres.get("subjectchildname").equals(end.get("subjectchildname"))).findFirst();
                    if (first.isPresent()){
                        map = first.get();
                        list.remove(map);
                    }
                }
                map.put("jgid",jgid);
                map.put("departmentname",departmentname);
                map.put("subjectname",progres.get("subjectname"));
                map.put("subjectchildname",progres.get("subjectchildname"));
                BigDecimal oldbudget = new BigDecimal(map.get("budget") != null ? map.get("budget").toString() : "0");
                BigDecimal newbudget = new BigDecimal(progres.get("budget") != null ? progres.get("budget").toString() : "0");
                map.put("budget",oldbudget.add(newbudget).toString());
                list.add(map);
            }
        }

        if (!CollectionUtils.isEmpty(expenses)){
            //处理实际费用
            for (Map<String, Object> expense : expenses) {
                // 找到子科目一样的预算
                Optional<Map<String, Object>> first = list.stream().filter(map -> map.get("subjectchildname").equals(expense.get("ccodeName")) && map.get("subjectname").equals(expense.get("codeName"))).findFirst();
                if (first.isPresent()){
                    //找到
                    Map<String, Object> map = first.get();
                    list.remove(map);
                    String key = "expenses_"+expense.get("type");
                    BigDecimal oldexpenses = new BigDecimal(map.get(key) != null ? map.get(key).toString() : "0");
                    BigDecimal newexpenses = new BigDecimal(expense.get("md") != null ? expense.get("md").toString() : "0");
                    map.put(key, oldexpenses.add(newexpenses).toString());
                    list.add(map);
                } else {
                    //未找到
                    Map<String, Object> map = new HashMap<>();
                    map.put("jgid",jgid);
                    map.put("departmentname",departmentname);
                    map.put("subjectname",expense.get("codeName"));
                    map.put("subjectchildname",expense.get("ccodeName"));
                    map.put("budget",String.valueOf("0"));
                    map.put("expenses_"+expense.get("type"), expense.get("md"));
                    list.add(map);
                }
            }
        }
        if (!CollectionUtils.isEmpty(list)){
            endMap.put(jgid,list);
            if (StringUtil.isNotBlank(jgxxDto.getFjgid()) && !"1".equals(jgxxDto.getFjgid())){
                Optional<JgxxDto> fjgxxOption = jgxxList.stream().filter(item -> item.getJgid().equals(jgxxDto.getFjgid())).findFirst();
                if (fjgxxOption.isPresent()){
                    dealJgxxInfo(fjgxxOption.get(),progress,expenses,jgxxList,endMap);
                }
            }
        }
    }

    /**
     * 功能：获取各部门各科目预算支出明细信息
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getDepartmentSubjectExpenseDetail(List<JgxxDto> jgxxList)  {
        List<JgxxDto> limitJgxxList = new ArrayList<>();
        for (JgxxDto jgxxDto : jgxxList) {
            if (!CollectionUtils.isEmpty(limitJgxxList)){
                Optional<JgxxDto> first = limitJgxxList.stream().filter(jgxx -> jgxx.getJgid().equals(jgxxDto.getJgid()) && jgxx.getFbsbj().equals(jgxxDto.getFbsbj())).findFirst();
                if (first.isPresent()){
                    JgxxDto oldJgxx = first.get();
                    limitJgxxList.remove(oldJgxx);
                    if (StringUtil.isNotBlank(oldJgxx.getKzcs2()) && StringUtil.isNotBlank(jgxxDto.getKzcs2())){
                        List<String> oldcskz2s = new ArrayList<>(Arrays.asList(oldJgxx.getKzcs2().split(",")));
                        List<String> newcskz2s = new ArrayList<>(Arrays.asList(jgxxDto.getKzcs2().split(",")));
                        for (String newcskz2 : newcskz2s) {
                            if (!oldcskz2s.contains(newcskz2)){
                                oldcskz2s.add(newcskz2);
                            }
                        }
                        oldJgxx.setKzcs2(String.join(",",oldcskz2s));
                    } else if (StringUtil.isNotBlank(jgxxDto.getKzcs2())) {
                        oldJgxx.setKzcs2(jgxxDto.getKzcs2());
                    }
                    limitJgxxList.add(oldJgxx);
                }else {
                    limitJgxxList.add(jgxxDto);
                }
            }else {
                limitJgxxList.add(jgxxDto);
            }
        }
        String nowDate = DateUtils.getCustomFomratCurrentDate("yyyy-MM-dd");
        String year = DateUtils.getCustomFomratCurrentDate("yyyy");
        // 获取实际费用
        List<YsglDto> ysglList = new ArrayList<>();
        // 获取年度
        YsglDto ysglDto_year = new YsglDto();
        ysglDto_year.setRqStart(year + "-01-01");
        ysglDto_year.setRqEnd(nowDate);
        ysglDto_year.setTjlx("year");
        ysglDto_year.setNd(year);
        ysglList.add(ysglDto_year);
        // 获取季度
        String season = ((((DateUtils.getMonth(new Date())-1) / 3 ) * 3 + 1)>9?"":"0")+((((DateUtils.getMonth(new Date())-1) / 3  ) * 3 + 1));
        YsglDto ysglDto_season = new YsglDto();
        ysglDto_season.setRqStart(year + "-"+season+"-01");
        ysglDto_season.setRqEnd(nowDate);
        ysglDto_season.setTjlx("season");
        ysglDto_season.setNd(year);
        ysglList.add(ysglDto_season);
        // 获取月度
        YsglDto ysglDto_month = new YsglDto();
        ysglDto_month.setRqStart(DateUtils.getCustomFomratCurrentDate("yyyy-MM-01"));
        ysglDto_month.setRqEnd(nowDate);
        ysglDto_month.setTjlx("month");
        ysglDto_month.setNd(year);
        ysglList.add(ysglDto_month);
        List<JcsjDto> ztDtos = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_U8.getCode());//账套
        List<JcsjDto> report_forms = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.REPORT_FORMS.getCode());
        List<JcsjDto> cwystjList = report_forms.stream().filter(e -> e.getCsdm().contains("CWYSTJ")).collect(Collectors.toList());//财务预算统计
        List<JcsjDto> account_subject = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_SUBJECT.getCode());//报表科目
        List<JcsjDto> account_sub_subject = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_SUB_SUBJECT.getCode());//报表子科目
        // 拼接账套内层sql
        StringBuilder inSql = new StringBuilder();
        for (JcsjDto ztDto : ztDtos) {
            String inSqlTemp = "SELECT COALESCE(SUM(gl.md) ,0) AS md,{ccodeSql} AS ccodeName ,{codeSql} AS codeName ,COALESCE(dep.cDepName,'{ztCskz2}') AS cDepName" +
                    " FROM {ztCskz1}GL_accvouch gl" +
                    " LEFT JOIN ({jgSql}) dep ON dep.jgdm = gl.cdept_id" +
                    " WHERE gl.dbill_date >= cast('{rqStart}' as date) and gl.dbill_date  < DATEADD(day,1,cast('{rqEnd}' as date))" +
                    " AND ({ccodeSql2})" +
                    " GROUP BY gl.ccode,dep.cDepName UNION ALL ";
            List<JgxxDto> collect = limitJgxxList.stream().filter(item -> ztDto.getCskz3().equals(item.getFbsbj())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(collect)){
                log.error("{}账套下未找到机构数据!",ztDto.getCsmc());
                continue;
            }
            //1、获取当前账套的财务预算统计 基础数据
            Optional<JcsjDto> cwystj_optional = cwystjList.stream().filter(e -> ztDto.getCsid().equals(e.getFcsid())).findFirst();
            if (!cwystj_optional.isPresent()){
                log.error("{}账套下未找到财务预算统计基础数据!",ztDto.getCsmc());
                continue;
            }
            JcsjDto cwystj = cwystj_optional.get();
            //2、获取当前账套的报表科目List
            List<JcsjDto> bbkms = account_subject.stream().filter(e -> cwystj.getCsid().equals(e.getFcsid())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(bbkms)){
                log.error("{}财务预算统计下未找到报表科目基础数据!",cwystj.getCsmc());
                continue;
            }
            //3、遍历获取所有报表子科目

            StringBuilder codeSql = new StringBuilder();
            StringBuilder ccodeSql = new StringBuilder();
            StringBuilder ccodeSql2 = new StringBuilder();
            codeSql.append(" CASE");
            ccodeSql.append(" CASE");
            String bbkmSqlTemp = " WHEN gl.ccode LIKE '{csdm}%' THEN '{bbkmCsmc}'";
            String bbzkmSqlTemp = " WHEN gl.ccode LIKE '{csdm}%' THEN '{bbzkmCsmc}'";
            String bbzkmSql2Temp = " gl.ccode LIKE '{csdm}%' OR";
            List<JcsjDto> bbzkms = new ArrayList<>();
            for (JcsjDto bbkm : bbkms) {
                List<JcsjDto> bbzkm_list = account_sub_subject.stream().filter(e -> bbkm.getCsid().equals(e.getFcsid())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(bbzkm_list)){
                    bbzkms.addAll(bbzkm_list);
                    for (JcsjDto bbzkm : bbzkm_list) {
                        String[] bbzkm_csdms = bbzkm.getCsdm().split(",");
                        for (String bbzkmCsdm : bbzkm_csdms) {
                            codeSql.append(bbkmSqlTemp.replace("{csdm}", bbzkmCsdm).replace("{bbkmCsmc}", bbkm.getCsmc()));
                            ccodeSql.append(bbzkmSqlTemp.replace("{csdm}", bbzkmCsdm).replace("{bbzkmCsmc}", bbzkm.getCsmc()));
                            ccodeSql2.append(bbzkmSql2Temp.replace("{csdm}", bbzkmCsdm));
                        }
                    }
                }
            }
            codeSql.append(" ELSE '未配置科目' END");
            ccodeSql.append(" ELSE '未配置子科目' END");
            inSqlTemp = inSqlTemp.replace("{codeSql}",codeSql);
            inSqlTemp = inSqlTemp.replace("{ccodeSql}",ccodeSql);
            inSqlTemp = inSqlTemp.replace("{ccodeSql2}",ccodeSql2.substring(0, ccodeSql2.lastIndexOf("OR")));

            StringBuilder jgSql = new StringBuilder();
            for (JgxxDto jgxxDto : collect) {
                String[] kzcs2s = jgxxDto.getKzcs2().split(",");
                String kzcs2SqlTemp = " SELECT '{jgdm}' AS jgdm, '{jgmc}' AS cDepName UNION ALL ";
                for (String kzcs2 : kzcs2s) {
                    jgSql.append(kzcs2SqlTemp.replace("{jgdm}", kzcs2).replace("{jgmc}", jgxxDto.getJgmc()));
                }
            }
            inSqlTemp = inSqlTemp.replace("{jgSql}", jgSql.substring(0, jgSql.lastIndexOf("UNION ALL")));
            inSqlTemp = inSqlTemp.replace("{ztCskz2}",ztDto.getCskz2()).replace("{ztCskz1}",ztDto.getCskz1());
            inSql.append(inSqlTemp);
        }
        inSql = new StringBuilder(inSql.substring(0, inSql.lastIndexOf("UNION ALL")));
        // 拼接sql
        StringBuilder allSql = new StringBuilder();
        allSql.append("SELECT COALESCE(SUM(tmp.md),0) md,tmp.cDepName,tmp.ccodeName,tmp.codeName,tmp.type from ( ");// 实际费用,机构部门,科目
        for (YsglDto ysglDto : ysglList) {
            allSql.append("SELECT COALESCE(SUM(tt.md),0) md, tt.cDepName, tt.ccodeName, tt.codeName, ");
            allSql.append("'"+ysglDto.getTjlx() + "' as type");
            allSql.append(" from ( ");
            allSql.append(inSql.toString().replace("{rqStart}",ysglDto.getRqStart()).replace("{rqEnd}",ysglDto.getRqEnd()));
            allSql.append(")tt group by tt.cDepName,tt.ccodeName,tt.codeName");
            allSql.append(" UNION ALL ");
        }
        allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf("UNION ALL")));
        allSql.append(")tmp group by tmp.cDepName,tmp.ccodeName,tmp.codeName,tmp.type");
        String sql = String.valueOf(allSql);
        log.error("SQL:{}",sql);
        try {
            checkSql(sql);
        } catch (BusinessException e) {
            throw new RuntimeException(e);
        }
        // 实际支出明细
        List<Map<String, Object>> departmentSubjectExpenseDetail = gl_accvouchDao.getDepartmentSubjectExpenseDetail(sql);
        log.error("departmentSubjectExpenseDetail:{}",JSON.toJSONString(departmentSubjectExpenseDetail));
        return departmentSubjectExpenseDetail;
    }

    /**
     * 获取当天在整年中所占百分比
     * @return
     */
    private BigDecimal getDayOfYearPercent(){
        // 获取当前日期
        LocalDate now = LocalDate.now();
        // 获取今年第一天
        LocalDate firstDayOfYear = LocalDate.of(now.getYear(), 1, 1);
        // 计算已过去的天数
        long daysPassed = ChronoUnit.DAYS.between(firstDayOfYear, now) + 1;
        // 判断是否为闰年，计算今年总天数
        int totalDays = (now.isLeapYear()) ? 366 : 365;
        return new BigDecimal(daysPassed).divide(new BigDecimal(totalDays),2,RoundingMode.DOWN);
    }

    /**
     * 获取当天在当前季度中所占百分比
     * @return
     */
    private BigDecimal getDayOfSeasonPercent(){
        LocalDate now = LocalDate.now();
        // 当前季度的开始日期
        LocalDate startOfQuarter = now.withMonth(((now.getMonthValue() - 1) / 3) * 3 + 1).withDayOfMonth(1);
        // 当前季度的结束日期
        LocalDate endOfQuarter = startOfQuarter.plusMonths(3).minusDays(1);
        // 计算当前季度的总天数
        long totalDays = ChronoUnit.DAYS.between(startOfQuarter, endOfQuarter) + 1;
        // 计算当前日期与季度开始日期之间的天数
        long daysPassed = ChronoUnit.DAYS.between(startOfQuarter, now) + 1;
        // 计算并输出百分比
        return new BigDecimal(daysPassed).divide(new BigDecimal(totalDays),2,RoundingMode.DOWN);
    }

    /**
     * 获取当天在当前月份中所占百分比
     * @return
     */
    private BigDecimal getDayOfMonthPercent(){
        LocalDate now = LocalDate.now();
        // 当前季度的开始日期
        LocalDate startOfMonth = now.withDayOfMonth(1);
        // 当前月份的结束日期
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        // 计算当前月份的总天数
        long totalDays = ChronoUnit.DAYS.between(startOfMonth, endOfMonth) + 1;
        // 计算当前日期与季度开始日期之间的天数
        long daysPassed = ChronoUnit.DAYS.between(startOfMonth, now) + 1;
        // 计算并输出百分比
        return new BigDecimal(daysPassed).divide(new BigDecimal(totalDays),2,RoundingMode.DOWN);
    }

    private String getJsmc(List<String> qxjgids,JgxxDto jgxxDto){
        String jgmc ="";
        JgxxDto jgxxDtoT = jgxxService.queryByJgxxDto(jgxxDto);
        if(jgxxDtoT!=null){
            if (qxjgids == null || qxjgids.isEmpty()) {
                if(!"1".equals(jgxxDtoT.getFjgid())){
                    JgxxDto jgxxDtoS = new JgxxDto();
                    jgxxDtoS.setFbsbj(jgxxDto.getFbsbj());
                    jgxxDtoS.setJgid(jgxxDtoT.getFjgid());
                    jgxxDtoS.setWbcxid(jgxxDto.getWbcxid());
                    jgmc = getJsmc(qxjgids,jgxxDtoS);
                }else{
                    jgmc = jgxxDtoT.getJgmc();
                }
            } else {
                for (String jgid:qxjgids){
                    if(jgid.equals(jgxxDtoT.getJgid())){
                        jgmc=jgxxDtoT.getJgmc();
                        break;
                    }
                }
                if(StringUtil.isBlank(jgmc)){
                    JgxxDto jgxxDtoS = new JgxxDto();
                    jgxxDtoS.setJgid(jgxxDtoT.getFjgid());
                    jgxxDtoS.setFbsbj(jgxxDto.getFbsbj());
                    jgxxDtoS.setWbcxid(jgxxDto.getWbcxid());
                    jgmc = getJsmc(qxjgids,jgxxDtoS);
                }
            }
        }
        return jgmc;
    }
}
