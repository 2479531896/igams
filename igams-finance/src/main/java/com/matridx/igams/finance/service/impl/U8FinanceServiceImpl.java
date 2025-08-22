package com.matridx.igams.finance.service.impl;

import com.matridx.igams.common.dao.entities.FjcfbDto;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.enums.AccountEnum;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.enums.BusTypeEnum;
import com.matridx.igams.common.exception.BusinessException;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.common.service.svcinterface.IFjcfbService;
import com.matridx.igams.common.service.svcinterface.IJcsjService;
import com.matridx.igams.common.util.*;
import com.matridx.igams.common.util.sseemitter.CommonXWPFWordThread;
import com.matridx.igams.common.util.sseemitter.DbXWPFWordThread;
import com.matridx.igams.finance.dao.financesql.IFinanceDao;
import com.matridx.igams.finance.service.svcinterface.IU8FinanceService;
import com.matridx.springboot.util.base.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@code @author:JYK}
 */
@Service
public class U8FinanceServiceImpl implements IU8FinanceService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    IFjcfbService fjcfbService;
    @Value("${matridx.systemflg.contractemail:}")
    private String contractemail;
    @Value("${matridx.fileupload.releasePath}")
    private String releaseFilePath;
    @Value("${matridx.fileupload.tempPath}")
    private String tempPath;
    @Autowired
    IFinanceDao financeDao;
    @Autowired
    IJcsjService jcsjService;
    @Autowired
    RedisXmlConfig redisXmlConfig;
    @Override
    public Map<String,Object>  ExportFinanceOut(Map<String,Object> map) throws BusinessException {
        String bbcsdm= (String) map.get("bbcsdm");
        if (bbcsdm.contains("XSFY")){//如果报表等于销售费用
            return this.exportSaleCost(map);
        }else if (bbcsdm.contains("CBFY")){//如果报表等于成本费用
            this.exportCost(map);
        }else if (bbcsdm.contains("BMFY")){//如果报表等于部门费用
            this.exportDepartmentCost(map);
        }else if(bbcsdm.contains("SRDB_YJ")){
            this.exportDbExport(map);
        }else if(bbcsdm.contains("CBSR")){
            this.exportRevenueCost(map);
        }
        return map;
    }
    /*
        成本收入报表导出
     */
    private void exportRevenueCost(Map<String, Object> map) throws BusinessException {
        String ztcsdm= (String) map.get("ztcsdm");
        String bbcsdm= (String) map.get("bbcsdm");
        //获取账套信息
        JcsjDto jcsjDto_zt = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_U8.getCode()).stream().filter(e -> ztcsdm.equals(e.getCsdm())).collect(Collectors.toList()).get(0);
        map.put("databaseName",jcsjDto_zt.getCskz1());
        List<Map<String,Object>> revenueCost = financeDao.getRevenueCost(map);
        //获取报表信息
        JcsjDto jcsjDto_bb = new JcsjDto();
        jcsjDto_bb.setCsdm(bbcsdm);
        jcsjDto_bb = jcsjService.getDto(jcsjDto_bb);
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.FINANCE_TEMPLATE.getCode());
        fjcfbDto.setYwid(jcsjDto_bb.getCsid());
        //获取模板附件
        List<FjcfbDto> fjcfbDtos=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        if (CollectionUtils.isEmpty(fjcfbDtos)){
            throw new BusinessException("msg","未找到对应模板，请联系管理员维护！");
        }
        map.put("wjlj",fjcfbDtos.get(0).getWjlj());
        map.put("wjid",StringUtil.generateUUID());
        map.put("totalCount",revenueCost.size());
        map.put("tempPath", tempPath); // 临时文件路径
        DataToExcel dataToExcel = new DataToExcel();
        dataToExcel.init(revenueCost,AccountEnum.getValueByCode(ztcsdm)+map.get("year")+fjcfbDtos.get(0).getWjm(),map,redisUtil,"2");
        new DataToExcelThread(dataToExcel).start();
    }


    /**
     * {@code @description} 部门费用
     */
    @SuppressWarnings("unchecked")
    private void exportDepartmentCost(Map<String,Object> map) throws BusinessException {
        String ztcsdm= (String) map.get("ztcsdm");
        String rq= (String) map.get("rq");
        String[] rqsplit = rq.split("~");
        map.put("rqstart",rqsplit[0]);
        map.put("rqend",rqsplit[1]);
        Map<String,Object> commonParam = getCommonParamTwo(ztcsdm,"BMFY");
        Map<String,String> rqStartAndEnd = getRqStartAndEnd(rqsplit[0],rqsplit[1]);
        StringBuilder haveDepBud = new StringBuilder();
        haveDepBud.append("select cDepName from (");
        for (String ztdm : commonParam.keySet()) {
            if ("fjcfbDtos".equals(ztdm))
                continue;
            Map<String, Object> paramMap = (Map<String, Object>) commonParam.get(ztdm);
            List<JcsjDto> bbkms = (List<JcsjDto>)paramMap.get("bbkms");
            String deptName = String.valueOf(paramMap.get("deptName"));
            haveDepBud.append("select CASE WHEN CHARINDEX ( '临检部' , REPLACE(").append(StringUtil.isNotBlank(deptName) ? ("COALESCE(de.cDepName,'" + deptName + "')") : "de.cDepName").append(",'-',''))>0 THEN '临检部' ELSE REPLACE(").append(StringUtil.isNotBlank(deptName) ? ("COALESCE(de.cDepName,'" + deptName + "')") : "de.cDepName").append(",'-','') END cDepName ").append("from <<databaseName>>GL_accvouch gl left join <<databaseName>>Department de on de.cDepCode=gl.cdept_id ").append("where gl.dbill_date >= cast('").append(rqStartAndEnd.get("rqstart")).append("' as datetime) and gl.dbill_date < cast('").append(rqStartAndEnd.get("rqend")).append("' as datetime) and ( ");
            for (JcsjDto bbkm : bbkms) {
                haveDepBud.append("ccode like '").append(bbkm.getFcsdm()).append("'+'%' or ");
            }
            haveDepBud = new StringBuilder(haveDepBud.substring(0, haveDepBud.lastIndexOf("or")));
            haveDepBud.append(")  UNION ALL ");
            //替换参数
            haveDepBud = new StringBuilder(String.valueOf(haveDepBud).replaceAll("<<databaseName>>", String.valueOf(paramMap.get("databaseName"))));
        }
        haveDepBud = new StringBuilder(haveDepBud.substring(0, haveDepBud.lastIndexOf("UNION ALL")));
        haveDepBud.append(")tmp WHERE cDepName IS NOT NULL GROUP BY cDepName");
        //获取所有账套有数据的部门
        String haveDepSql = String.valueOf(haveDepBud);
        checkSql(haveDepSql);
        List<String> departments = financeDao.getHaveCostDepartment(haveDepSql);
        if (CollectionUtils.isEmpty(departments)){
            throw new BusinessException("msg","该时间段未产生部门费用，请选择其他时间导出！");
        }
        StringBuilder sqlParam = new StringBuilder();
        StringBuilder sqlParamFive = new StringBuilder();
        StringBuilder sqlParamFour = new StringBuilder();
        sqlParam.append(",SUM(");
        for (String department : departments) {
            //每个部门的金额
            sqlParamFour.append(",SUM(COALESCE(newtemp.").append(department).append(",0)) ").append(department);
            sqlParamFive.append(",SUM(COALESCE(newtemp.").append(department).append(",0)) z@").append(department);
            //总金额
            sqlParam.append("COALESCE(newtemp.").append(department).append(",0)+");
        }
        sqlParam.deleteCharAt(sqlParam.length() - 1);
        sqlParam.append(") C@费用合计");
        sqlParamFive.insert(0,sqlParam);
        sqlParam.append(sqlParamFour);
        StringBuilder allSql = new StringBuilder();
        allSql.append("SELECT newtemp.A@父费用类别,newtemp.B@费用类别 <<sqlParamFive>> from (");
        allSql = new StringBuilder(String.valueOf(allSql).replaceAll("<<sqlParamFive>>", String.valueOf(sqlParamFive)));
        for (String ztdm : commonParam.keySet()) {
            if ("fjcfbDtos".equals(ztdm))
                continue;
            Map<String, Object> paramMap = (Map<String, Object>) commonParam.get(ztdm);
            List<JcsjDto> bbkms = (List<JcsjDto>)paramMap.get("bbkms");
            List<JcsjDto> bbzkms = (List<JcsjDto>)paramMap.get("bbzkms");
            String deptName = String.valueOf(paramMap.get("deptName"));
            allSql.append("select newtemp.ffylb AS A@父费用类别,newtemp.fylb AS B@费用类别 <<sqlParam>>,2 px from( select gl.md,CASE WHEN CHARINDEX ( '临检部' , ").append(StringUtil.isNotBlank(deptName) ? ("COALESCE(de.cDepName,'" + deptName + "')") : "de.cDepName").append(")>0 THEN '临检部' ELSE ").append(StringUtil.isNotBlank(deptName) ? ("COALESCE(de.cDepName,'" + deptName + "')") : "REPLACE(de.cDepName,'-','')").append(" END cDepName,gl.dbill_date,gl.ccode <<sqlParamTwo>> ").append("from <<databaseName>>GL_accvouch gl left join <<databaseName>>Department de on de.cDepCode=gl.cdept_id where gl.dbill_date >= cast('").append(rqStartAndEnd.get("rqstart")).append("' as datetime) and gl.dbill_date < cast('").append(rqStartAndEnd.get("rqend")).append("' as datetime) and ( ");
            for (JcsjDto bbkm : bbkms) {
                allSql.append("ccode like '").append(bbkm.getFcsdm()).append("'+'%' or ");
            }
            allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf("or")));
            allSql.append(") ) s PIVOT ( SUM(s.md) FOR s.cDepName IN (").append(StringUtil.join(departments, ",")).append(") ) AS newtemp  GROUP BY newtemp.ffylb,newtemp.fylb ").append("UNION ALL select newtemp.ffylb AS A@父费用类别,newtemp.ffylb+'小计' AS B@费用类别 <<sqlParam>>,1 px from( select gl.md,CASE WHEN CHARINDEX ( '临检部' , ").append(StringUtil.isNotBlank(deptName) ? ("COALESCE(de.cDepName,'" + deptName + "')") : "de.cDepName").append(")>0 THEN '临检部' ELSE ").append(StringUtil.isNotBlank(deptName) ? ("COALESCE(de.cDepName,'" + deptName + "')") : "REPLACE(de.cDepName,'-','')").append(" END cDepName,gl.dbill_date,gl.ccode <<sqlParamTwo>> ").append("from <<databaseName>>GL_accvouch gl left join <<databaseName>>Department de on de.cDepCode=gl.cdept_id where gl.dbill_date >= cast('").append(rqStartAndEnd.get("rqstart")).append("' as datetime) and gl.dbill_date < cast('").append(rqStartAndEnd.get("rqend")).append("' as datetime) and ( ");
            for (JcsjDto bbkm : bbkms) {
                allSql.append("ccode like '").append(bbkm.getFcsdm()).append("'+'%' or ");
            }
            allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf("or")));
            allSql.append(") ) s PIVOT ( SUM(s.md) FOR s.cDepName IN (").append(StringUtil.join(departments, ",")).append(") ) AS newtemp  GROUP BY newtemp.ffylb  UNION ALL ");
            StringBuilder sqlParamTwo = new StringBuilder();
            StringBuilder sqlParamThree = new StringBuilder();
            sqlParamTwo.append(",(CASE");
            sqlParamThree.append(",(CASE");
            for (JcsjDto bbzkm : bbzkms) {
                //费用类别别名
                sqlParamTwo.append(" WHEN gl.ccode like " + "'").append(bbzkm.getCsdm()).append("%").append("'").append(" THEN ").append("'").append(StringUtil.isNotBlank(bbzkm.getCskz1()) ? bbzkm.getCskz1() : bbzkm.getCsmc()).append("'");
                //父费用类别别名
                sqlParamThree.append(" WHEN gl.ccode like " + "'").append(bbzkm.getCsdm()).append("%").append("'").append(" THEN ").append("'").append(bbzkm.getCskz2()).append("'");
            }
            sqlParamTwo.append(" END) AS fylb");
            sqlParamThree.append(" END) AS ffylb");
            sqlParamTwo.append(sqlParamThree);
            //替换参数
            allSql = new StringBuilder(String.valueOf(allSql).replaceAll("<<sqlParam>>", String.valueOf(sqlParam))
                    .replaceAll("<<sqlParamTwo>>", String.valueOf(sqlParamTwo))
                    .replaceAll("<<databaseName>>", String.valueOf(paramMap.get("databaseName"))));
        }
        allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf("UNION ALL")));
        allSql.append(")newtemp WHERE A@父费用类别 IS NOT NULL GROUP BY newtemp.A@父费用类别,newtemp.B@费用类别,px ORDER BY newtemp.A@父费用类别 DESC,px,newtemp.B@费用类别 DESC");
        String sql = String.valueOf(allSql);
        checkSql(sql);
        List<Map<String, Object>> maps = financeDao.getDepartmentCost(sql);
        List<FjcfbDto> fjcfbDtos= (List<FjcfbDto>)commonParam.get("fjcfbDtos");
        map.put("wjlj",fjcfbDtos.get(0).getWjlj());
        map.put("bbcsdm","BMFY");
        String wjid= StringUtil.generateUUID();
        map.put("wjid",wjid);
        map.put("totalCount",maps.size());
        map.put("tempPath", tempPath); // 临时文件路径
        DataToExcel dataToExcel = new DataToExcel();
        dataToExcel.init(maps,AccountEnum.getValueByCode(ztcsdm)+map.get("rq")+fjcfbDtos.get(0).getWjm(),map,redisUtil,"1");
        new DataToExcelThread(dataToExcel).start();
    }
    /**
     * {@code @description} 成本费用
     */
    @SuppressWarnings("unchecked")
    private void exportCost(Map<String,Object> map) throws BusinessException {
        String ztcsdm= (String) map.get("ztcsdm");
        String rq= (String) map.get("rq");
        String[] rqsplit = rq.split("~");
        map.put("rqstart",rqsplit[0]);
        map.put("rqend",rqsplit[1]);
        Map<String,Object> commonParam = getCommonParamTwo(ztcsdm,"CBFY");
        List<String> allMonth = getAllMonth(rqsplit[0],rqsplit[1]);
        Map<String,String> rqStartAndEnd = getRqStartAndEnd(rqsplit[0],rqsplit[1]);
        StringBuilder allSql = new StringBuilder();
        allSql.append("SELECT  newtemp.A@费用类别,newtemp.A@部门 <<sqlParamTwo>> from (");
        StringBuilder sqlParamTwo = new StringBuilder();
        StringBuilder sqlParamThree = new StringBuilder();
        StringBuilder sqlParamFour = new StringBuilder();
        sqlParamThree.append(",SUM(");
        sqlParamFour.append("(CASE");
        for (String month : allMonth) {
            //每个月份的金额
            sqlParamTwo.append(",SUM(COALESCE(newtemp.").append(month).append(",0)) ").append(month);
            //总金额
            sqlParamThree.append("COALESCE(newtemp.").append(month).append(",0)+");
            //月份别名
            sqlParamFour.append(" WHEN FORMAT(dbill_date,'MM') =" + "'").append(month.substring(month.length() - 2)).append("'").append(" THEN ").append("'").append(month).append("'");
        }
        sqlParamFour.append(" END)");
        sqlParamThree.deleteCharAt(sqlParamThree.length() - 1);
        sqlParamThree.append(") O@总费用");
        sqlParamTwo.append(sqlParamThree);
        for (String ztdm : commonParam.keySet()) {
            if ("fjcfbDtos".equals(ztdm))
                continue;
            Map<String, Object> paramMap = (Map<String, Object>) commonParam.get(ztdm);
            String ccode = String.valueOf(paramMap.get("ccode"));
            String deptName = String.valueOf(paramMap.get("deptName"));
            allSql.append("select newtemp.fylb as A@费用类别,").append(StringUtil.isNotBlank(deptName) ? ("COALESCE(newtemp.cDepName,'" + deptName + "')") : "newtemp.cDepName").append(" as A@部门 ").append("<<sqlParamTwo>> ,2 px from ( select <<sqlParam>> AS fylb,de.cDepName,gl.md, <<sqlParamFour>> AS yf ").append("from <<databaseName>>GL_accvouch gl left join <<databaseName>>Department de on de.cDepCode=gl.cdept_id ").append("where gl.dbill_date >= cast('").append(rqStartAndEnd.get("rqstart")).append("' as datetime) and gl.dbill_date < cast('").append(rqStartAndEnd.get("rqend")).append("' as datetime) and gl.ccode like '").append(ccode).append("'+'%' ) s PIVOT ").append("( SUM(s.md) FOR s.yf IN ( ").append(StringUtil.join(allMonth, ",")).append(") ) AS newtemp where newtemp.fylb is not null GROUP BY newtemp.fylb,newtemp.cDepName ").append("UNION ALL select newtemp.fylb as A@费用类别,'总计' as A@部门 <<sqlParamTwo>> ,1 px from ( select <<sqlParam>> AS fylb,gl.md, <<sqlParamFour>> AS yf ").append("from <<databaseName>>GL_accvouch gl where gl.dbill_date >= cast('").append(rqStartAndEnd.get("rqstart")).append("' as datetime) and gl.dbill_date < cast('").append(rqStartAndEnd.get("rqend")).append("' as datetime) and gl.ccode like '").append(ccode).append("'+'%' ) s PIVOT").append(" ( SUM(s.md) FOR s.yf IN (").append(StringUtil.join(allMonth, ",")).append(") ) AS newtemp where newtemp.fylb is not null GROUP BY newtemp.fylb UNION ALL ");
            List<JcsjDto> bbzkms = (List<JcsjDto>)paramMap.get("bbzkms");
            StringBuilder sqlParam = new StringBuilder();
            sqlParam.append("(CASE");
            for (JcsjDto bbzkm : bbzkms) {
                //费用类别别名
                sqlParam.append(" WHEN gl.ccode like " + "'").append(bbzkm.getCsdm()).append("%").append("'").append(" THEN '").append(StringUtil.isNotBlank(bbzkm.getCskz1()) ? bbzkm.getCskz1() : bbzkm.getCsmc()).append("'");
            }
            sqlParam.append(" END)");
            //替换参数
            allSql = new StringBuilder(String.valueOf(allSql).replaceAll("<<sqlParam>>", String.valueOf(sqlParam))
                    .replaceAll("<<sqlParamFour>>", String.valueOf(sqlParamFour))
                    .replaceAll("<<sqlParamTwo>>", String.valueOf(sqlParamTwo))
                    .replaceAll("<<databaseName>>", String.valueOf(paramMap.get("databaseName"))));
        }
        allSql = new StringBuilder(allSql.substring(0, allSql.lastIndexOf("UNION ALL")));
        allSql.append(")newtemp GROUP BY newtemp.A@费用类别,px,newtemp.A@部门 ORDER BY newtemp.A@费用类别,px,newtemp.A@部门 DESC");
        String sql = String.valueOf(allSql);
        checkSql(sql);
        List<Map<String, Object>> maps = financeDao.getCost(sql);
        List<FjcfbDto> fjcfbDtos= (List<FjcfbDto>)commonParam.get("fjcfbDtos");
        map.put("wjlj",fjcfbDtos.get(0).getWjlj());
        map.put("bbcsdm","CBFY");
        map.put("wjid",StringUtil.generateUUID());
        map.put("totalCount",maps.size());
        map.put("tempPath", tempPath); // 临时文件路径
        DataToExcel dataToExcel = new DataToExcel();
        dataToExcel.init(maps,AccountEnum.getValueByCode(ztcsdm)+map.get("rq")+fjcfbDtos.get(0).getWjm(),map,redisUtil,"1");
        new DataToExcelThread(dataToExcel).start();
    }

    private Map<String, String> getRqStartAndEnd(String rqstart, String rqend) {
        Map<String, String> map = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat rqformat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar instance = Calendar.getInstance();
            instance.setTime(format.parse(rqstart));
            instance.set(Calendar.DAY_OF_MONTH, instance.getActualMinimum(Calendar.DAY_OF_MONTH));
            map.put("rqstart",rqformat.format(instance.getTime()));
            instance.setTime(format.parse(rqend));
            instance.set(Calendar.DAY_OF_MONTH, instance.getActualMaximum(Calendar.DAY_OF_MONTH));
            instance.add(Calendar.DATE,1);
            map.put("rqend",rqformat.format(instance.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return map;
    }

    private Map<String,Object> exportDbExport(Map<String,Object> map) throws BusinessException {
        map.put("lastyear",Integer.valueOf(String.valueOf(map.get("year")))-1);
        List<Map<String, Object>> maps = financeDao.getDbCost(map);
        Map<String,Object> commonParam = getCommonParamTwo(String.valueOf(map.get("ztcsdm")),String.valueOf(map.get("bbcsdm")));
        List<FjcfbDto> fjcfbDtos= (List<FjcfbDto>)commonParam.get("fjcfbDtos");
        map.put("mbmc",fjcfbDtos.get(0).getWjm().split(".xlsx")[0]);
        map.put("wjlj",fjcfbDtos.get(0).getWjlj());
        map.put("totalCount",maps.size());
        map.put("maplist",maps);
        return replaceFinanceTemplateTwo(map);

    }
    /**
     * {@code @description} 销售费用
     */
    @SuppressWarnings("unchecked")
    private Map<String,Object> exportSaleCost( Map<String,Object> map) throws BusinessException {
        String ztcsdm= (String) map.get("ztcsdm");
        String rq= (String) map.get("rq");
        Map<String,Object> commonParam = getCommonParam(ztcsdm);
        Map<String,String> rqStartAndEnd = getRqStartAndEnd(rq,rq);
        map.put("rqstart",rqStartAndEnd.get("rqstart"));
        map.put("rqend",rqStartAndEnd.get("rqend"));
        map.put("bbzkms",commonParam.get("bbzkms"));
        map.put("ccode",commonParam.get("ccode"));
        map.put("databaseName",commonParam.get("databaseName"));
        List<JcsjDto> bbzkms = (List<JcsjDto>)commonParam.get("bbzkms");
        StringBuilder sqlParam = new StringBuilder();
        StringBuilder sqlParamTwo = new StringBuilder();
        StringBuilder sqlParamThree = new StringBuilder();
        sqlParam.append("newtemp.cPersonName 人员,newtemp.cDepName 部门");
        sqlParamTwo.append(",SUM(");
        sqlParamThree.append("(CASE");
        for (JcsjDto bbzkm : bbzkms) {
            //每个子科目的金额
            sqlParam.append(",SUM(COALESCE(newtemp.").append(bbzkm.getCsmc()).append(",0)) ").append(bbzkm.getCsmc());
            //总金额
            sqlParamTwo.append("COALESCE(newtemp.").append(bbzkm.getCsmc()).append(",0)+");
            //费用类别别名
            sqlParamThree.append(" WHEN gl.ccode like " + "'").append(bbzkm.getCsdm()).append("%").append("'").append(" THEN ").append("'").append(bbzkm.getCsmc()).append("'");
        }
        sqlParamTwo.deleteCharAt(sqlParamTwo.length() - 1);
        sqlParamTwo.append(") 总费用");
        sqlParam.append(sqlParamTwo);
        map.put("sqlParam",sqlParam.toString());
        sqlParamThree.append(" END)");
        map.put("sqlParamThree",sqlParamThree.toString());
        List<Map<String, Object>> maps = financeDao.selectFinanceSaleCost(map);
        for (Map<String, Object> stringObjectMap : maps) {
            stringObjectMap.put("所属公司", AccountEnum.getValueByCode(ztcsdm));
        }
        List<FjcfbDto> fjcfbDtos= (List<FjcfbDto>)commonParam.get("fjcfbDtos");
        Map<String,Object> map_t=new HashMap<>();
        map_t.put("month",rq.substring(6,7));
        map_t.put("maplist",maps);
        map_t.put("rq",rq);
        map_t.put("ztmc", AccountEnum.getValueByCode(ztcsdm));
        map_t.put("mbmc",fjcfbDtos.get(0).getWjm().split(".xlsx")[0]);
        map_t.put("wjlj",fjcfbDtos.get(0).getWjlj());
        map_t.put("totalCount",maps.size());
        return replaceFinanceTemplate(map_t);
    }
    /**
     * {@code @description} 获取共通参数
     * @param ztcsdm 账套参数代码 bb 报表参数代码
     * @return Map
     */
    private Map<String,Object> getCommonParamTwo(String ztcsdm,String bbcsdm) throws BusinessException {
        Map<String,Object> resultMap = new HashMap<>();
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.FINANCE_TEMPLATE.getCode());
        for (String csdm : ztcsdm.split(",")) {
            Map<String, Object> map = new HashMap<>();
            //获取账套信息
            JcsjDto jcsjDto_zt = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_U8.getCode()).stream().filter(e -> csdm.equals(e.getCsdm())).collect(Collectors.toList()).get(0);
            map.put("databaseName",jcsjDto_zt.getCskz1());
            map.put("deptName",jcsjDto_zt.getCskz2());
            //获取报表信息
            JcsjDto jcsjDto_bb = new JcsjDto();
            jcsjDto_bb.setLikecsdm(bbcsdm);
            jcsjDto_bb.setFcsdm(csdm);
            jcsjDto_bb = jcsjService.getDto(jcsjDto_bb);
            fjcfbDto.setYwid(jcsjDto_bb.getCsid());

            JcsjDto jcsjDto = new JcsjDto();
            jcsjDto.setFfcsid(jcsjDto_bb.getCsid());
            jcsjDto.setJclb(BasicDataTypeEnum.ACCOUNT_SUB_SUBJECT.getCode());
            //获取报表子科目
            List<JcsjDto> bbzkms = jcsjService.getListByFfid(jcsjDto);
            if (CollectionUtils.isEmpty(bbzkms)){
                throw new BusinessException("msg","未配置对应报表子科目，请联系管理员维护！");
            }
            //获取报表科目参数代码 通过fcsdm去重获取报表科目参数代码
            List<JcsjDto> bbkms = bbzkms.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(JcsjDto::getFcsdm))), ArrayList::new));
            map.put("bbkms",bbkms);
            //获取报表子科目
            map.put("bbzkms",bbzkms);
            map.put("ccode",bbkms.get(0).getFcsdm());
            resultMap.put(csdm,map);
        }
        //获取模板附件
        List<FjcfbDto> fjcfbDtos=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        resultMap.put("fjcfbDtos",fjcfbDtos);
        if (CollectionUtils.isEmpty(fjcfbDtos)){
            throw new BusinessException("msg","未找到对应模板，请联系管理员维护！");
        }
        return resultMap;
    }
    /**
     * {@code @description} 获取共通参数
     *
     * @param ztcsdm 账套参数代码 bb 报表参数代码
     * @return Map
     */
    private Map<String,Object> getCommonParam(String ztcsdm) throws BusinessException {
        Map<String, Object> map = new HashMap<>();
        //获取账套信息
        JcsjDto jcsjDto_zt = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.ACCOUNT_U8.getCode()).stream().filter(e -> ztcsdm.equals(e.getCsdm())).collect(Collectors.toList()).get(0);
        map.put("databaseName",jcsjDto_zt.getCskz1());
        FjcfbDto fjcfbDto=new FjcfbDto();
        fjcfbDto.setYwlx(BusTypeEnum.FINANCE_TEMPLATE.getCode());
        Map<String, List<JcsjDto>> jclist = jcsjService
                .getDtoListbyJclb(new BasicDataTypeEnum[] { BasicDataTypeEnum.REPORT_FORMS});
        List<JcsjDto> xsbbs = jclist.get(BasicDataTypeEnum.REPORT_FORMS.getCode());
        String xsbbid = "";
        for (JcsjDto jcsjDto:xsbbs){
            if (jcsjDto.getCsdm().contains("XSFY")&&jcsjDto.getFcsdm().equals(ztcsdm)){
                fjcfbDto.setYwid(jcsjDto.getCsid());
                xsbbid = jcsjDto.getCsid();
                break;
            }
        }
        //获取模板附件
        List<FjcfbDto> fjcfbDtos=fjcfbService.selectFjcfbDtoByYwidAndYwlx(fjcfbDto);
        map.put("fjcfbDtos",fjcfbDtos);
        if (CollectionUtils.isEmpty(fjcfbDtos)){
            throw new BusinessException("msg","未找到对应模板，请联系管理员维护！");
        }
        JcsjDto jcsjDto = new JcsjDto();
        jcsjDto.setFfcsid(xsbbid);
        jcsjDto.setJclb(BasicDataTypeEnum.ACCOUNT_SUB_SUBJECT.getCode());
        List<JcsjDto> bbzkms = jcsjService.getListByFfid(jcsjDto);
        if (CollectionUtils.isEmpty(bbzkms)){
            throw new BusinessException("msg","未配置对应报表子科目，请联系管理员维护！");
        }
        //获取报表科目参数代码 通过fcsdm去重获取报表科目参数代码
        List<JcsjDto> bbkms = bbzkms.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(JcsjDto::getFcsdm))), ArrayList::new));
        map.put("bbkms",bbkms);
        //获取报表子科目
        map.put("bbzkms",bbzkms);
        map.put("ccode",bbzkms.get(0).getFcsdm());
        return map;
    }
    /**
     *   获取两个月份之间的所有月份(含跨年)
     */
    public  Map<String,List<String>> getMonthAndYearBetween(String minDate, String maxDate) {
        Map<String,List<String>> map = new HashMap<>();
        ArrayList<String> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");// 格式化为年月
        try {
            Calendar min = Calendar.getInstance();
            Calendar max = Calendar.getInstance();

            min.setTime(sdf.parse(minDate));
            min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

            max.setTime(sdf.parse(maxDate));
            max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

            while (min.before(max)) {
                result.add(sdf.format(min.getTime()));
                min.add(Calendar.MONTH, 1);
            }

            // 实现排序方法
            result.sort((Comparator<Object>) (o1, o2) -> {
                String str1 = (String) o1;
                String str2 = (String) o2;
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
                Date date1 = null;
                Date date2 = null;
                try {
                    date1 = format.parse(str1);
                    date2 = format.parse(str2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (date2 != null && date2.compareTo(date1) > 0) {
                    return -1;
                }
                return 1;
            });
            map.put("months",result);
            List<String> years = new ArrayList<>();
            Date minDateYear=sdf.parse(minDate);
            Date maxDateYear=sdf.parse(maxDate);
            if (minDateYear.getTime()>maxDateYear.getTime()){
                return null;
            }
            String startDate=sdf.format(minDateYear);
            String endDate=sdf.format(maxDateYear);
            Calendar from = Calendar.getInstance();
            from.setTime(minDateYear);
            Calendar to = Calendar.getInstance();
            to.setTime(maxDateYear);
            //只要年的差
            int fromYear = from.get(Calendar.YEAR);
            int toYear = to.get(Calendar.YEAR);
            int year = toYear - fromYear;
            if (year==0){
                years.add(startDate+"~"+endDate);
            }else {
                for (int i=0;i<=year;i++){
                    if (i==0){
                        years.add(startDate+"~"+fromYear+"-12");
                    }else if (i != year){
                        years.add((fromYear+i)+"-01"+"~"+(fromYear+i)+"-12");
                    }else {
                        years.add(toYear+"-01"+"~"+endDate);
                    }
                }
            }
            map.put("years",years);
        }catch (ParseException e) {
            e.printStackTrace();
        }

        return map;
    }

    @Override
    public List<String> getYearByCcode(Map<String, Object> map) throws BusinessException {
        return financeDao.getYearByCcode(map);
    }

    public List<String> getAllMonth(String rqStart,String rqEnd){
        List<String> rqs = new ArrayList<>();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(rqStart));
            Calendar endcalendar = Calendar.getInstance();
            endcalendar.setTime(sdf.parse(rqEnd));
            int ASCII = 67;//从C开始
            while (endcalendar.compareTo(calendar) >= 0)
            {
                rqs.add((char) ASCII+"@"+ format.format(calendar.getTime()));
                ASCII++;
                calendar.add(Calendar.MONTH, 1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return rqs;
    }
    /**
     * 模板替换
     */
    public Map<String,Object> replaceFinanceTemplateTwo(Map<String,Object> map){
        DbXWPFWordUtil dbXWPFWordUtil=new DbXWPFWordUtil();
        map.put("buyerEmail", contractemail);
        map.put("releaseFilePath", releaseFilePath); // 正式文件路径
        map.put("tempPath", tempPath); // 临时文件路径
        String wjid= StringUtil.generateUUID();
        dbXWPFWordUtil.init(redisXmlConfig,map,wjid);
        DbXWPFWordThread dbXWPFWordThread=new DbXWPFWordThread(dbXWPFWordUtil,wjid);
        dbXWPFWordThread.start();
          map.put("wjid",wjid);
          map.put("totalCount",map.get("totalCount"));
          return map;
    }
    public Map<String,Object> replaceFinanceTemplate(Map<String,Object> map){
        CommonXWPFWordUtil commonXWPFWordUtil=new CommonXWPFWordUtil();
        map.put("buyerEmail", contractemail);
        map.put("releaseFilePath", releaseFilePath); // 正式文件路径
        map.put("tempPath", tempPath); // 临时文件路径
        String wjid= StringUtil.generateUUID();
        commonXWPFWordUtil.init(redisXmlConfig,map,wjid);
        CommonXWPFWordThread commonXWPFWordThread=new CommonXWPFWordThread(commonXWPFWordUtil,wjid);
        commonXWPFWordThread.start();
        map.put("wjid",wjid);
        map.put("totalCount",map.get("totalCount"));
        return map;
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
}
