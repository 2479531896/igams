package com.matridx.igams.bioinformation.util;

import com.alibaba.fastjson.JSONObject;
import com.matridx.igams.bioinformation.dao.entities.*;
import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.springboot.util.base.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.*;

public class JDBCUtils {
    private final Logger log = LoggerFactory.getLogger(JDBCUtils.class);

    private static final String driverName = "org.postgresql.Driver";       // 数据库驱动名称

    private static final String url = "jdbc:postgresql://172.17.70.9:5432/microbase";

    private static final String url1 = "jdbc:postgresql://172.17.70.6:5432/deterpret";// 数据库url


    private static final String userName = "ituser";          // 用户名

    private static final String password = "Matridx@2022";     // 用户密码

    private static Connection conn = null;                  // 数据库连接对象

    private static PreparedStatement preparedStatement = null;  // 执行操作对象

    public List<Map<String,String>> querySample(){
        List<Map<String,String>> list =new ArrayList<>();
        try {
            Class.forName(driverName);      // 2、注册数据库驱动
            conn = DriverManager.getConnection("jdbc:postgresql://172.17.60.197:5432/matridx", "matridx", "matridx2020!");    // 3、获取数据库连接
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        ResultSet result = null;
        String sql = "SELECT " +
                "sample.uid, " +
                "sample.total_reads," +
                "sample.clean_gc," +
                "sample.clean_q20," +
                "sample.clean_q30," +
                "sample.lib_concentration," +
                "sample.adapter," +
                "sample.host_index," +
                "sample.host_percentile," +
                "sample.qc_data," +
                "sample.spike_count," +
                "sample.spike_detail," +
                "sample.ybbh," +
                "xpxxls.cxyid," +
                "jcsj.fcsid," +
                "sample.RECHECK," +
                "sample.create_date," +
                "sjxx.sjid," +
                "sample.r_rna, " +
                "sample.dna_concentration, " +
                "xpxxls.xpid " +
                "FROM " +
                "core_sample sample " +
                "LEFT JOIN igams_xpxx xpxxls ON xpxxls.xpm = sample.chip " +
                "LEFT JOIN matridx_jcsj jcsj ON jcsj.csid = xpxxls.cxyid " +
                "LEFT JOIN igams_sjxx sjxx ON sjxx.ybbh = sample.ybbh " +
                "WHERE " +
                "sample.create_date < '2022-09-01' " +
                "AND jcsj.scbj = '0'";

        try {
            preparedStatement = conn.prepareStatement(sql);         // 4、执行预编译SQL操作
            result = preparedStatement.executeQuery();  // 5、执行查询操作
            while (result.next()) {
                Map<String,String>map=new HashMap<>();
                map.put("wkcxid",StringUtil.generateUUID());
                map.put("wkbh",result.getString(1));
                map.put("sffsbg","0");
                map.put("sjid",result.getString(18));
                map.put("xpid",result.getString(21));
                map.put("cxyid",result.getString(14));
                map.put("sysid",result.getString(15));
                if(map.get("wkbh").contains("RNA")){
                    map.put("wklx","RNA");
                }else if(map.get("wkbh").contains("DNA")){
                    map.put("wklx","DNA");
                }else{
                    map.put("wklx","");
                }
                if("加测".equals(result.getString(16))){
                    map.put("fcjc","2");
                }else if("复测".equals(result.getString(16))){
                    map.put("fcjc","1");
                }else{
                    map.put("fcjc","");
                }
                map.put("cleangc",result.getString(3));
                map.put("cleanq20",result.getString(4));
                map.put("cleanq30",result.getString(5));
                map.put("adapter",result.getString(7));


                map.put("libcon",result.getString(6));
                map.put("nuccon",result.getString(20));
                map.put("qc",JSONObject.toJSONString(JSONObject.parseObject(result.getString(10))));
                map.put("fxbb","V1:mngslibs-2.6.0");
                map.put("zxbb","V1:mngslibs-2.6.0");
                JSONObject rnaObj=JSONObject.parseObject(result.getString(19));
                if(rnaObj!=null&&rnaObj.size()>0){
                    map.put("rnareadsrate",rnaObj.get("RNA_reads_rate")==null?"":rnaObj.get("RNA_reads_rate").toString());
                }else{
                    map.put("rnareadsrate","");
                }
                map.put("scbj","0");
                map.put("lrsj",result.getTimestamp(17).toString());
                map.put("xgsj",result.getTimestamp(17).toString());
                JSONObject object= JSONObject.parseObject(result.getString(10));
                if(object!=null&&object.size()>0){
                    BigDecimal hu = new BigDecimal(100);
                    BigDecimal rawreads=new BigDecimal("0");
                    BigDecimal barcodematch;
                    try {
                        rawreads   =  new BigDecimal(object.get("cleanreads").toString()).multiply(hu).divide(new BigDecimal(object.get("clean").toString()),5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
                        map.put("rawreads",rawreads.toString());

                    }catch (Exception e){

                        map.put("rawreads","");
                    }
                    try {
                        barcodematch = new BigDecimal(object.get("barcode_all_match").toString()).multiply(hu).divide(rawreads,5, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
                        map.put("barcodematch",barcodematch.toString());

                    }catch (Exception e){
                        map.put("barcodematch","");
                    }


                    map.put("clean",object.get("clean")==null?"":object.get("clean")+"");
                    map.put("dilutionf",object.get("dilution_factor")==null?"":object.get("dilution_factor")+"");
                    map.put("comment",object.get("comment")==null?"":object.get("comment")+"");
                }else{
                    map.put("barcodematch","");
                    map.put("rawreads","");
                    map.put("clean","");
                    map.put("dilutionf","");
                    map.put("comment","");
                }



                list.add(map);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }finally {
            // 7、释放资源
            close(conn, preparedStatement, result);
        }
        return list;
    }

    public List<CnvjgDto> queryCnvjg(){
        List<CnvjgDto> list =new ArrayList<>();
        try {
            Class.forName(driverName);      // 2、注册数据库驱动
            conn = DriverManager.getConnection("jdbc:postgresql://172.17.60.197:5432/matridx", "matridx", "matridx2020!");    // 3、获取数据库连接
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        ResultSet result = null;
        String sql = "select cnvqc.gc,wkcx.wkbh,wkcx.wkcxid,cnvqc.total_reads,cnvqc.mapped_reads,cnvqc.uniq_reads,cnvqc.waviness,cnvqc.karyotype,cnvqc.sex,cnvqc.model_predict,cnvqc.qc,cnvqc.tumor,cnvqc.review_result,cnvqc.feature,wkcx.xpid,sample.create_date  " +
                "from igams_wkcx wkcx  " +
                "left join core_sample sample on  wkcx.wkbh= sample.uid " +
                "left join core_cnvqc cnvqc on sample.id=cnvqc.sample_id " +
                "where sample.create_date < '2022-09-01' and wkbh is not null and cnvqc.sample_id is not null";

        try {
            preparedStatement = conn.prepareStatement(sql);         // 4、执行预编译SQL操作
            result = preparedStatement.executeQuery();  // 5、执行查询操作
            while (result.next()) {
                CnvjgDto cnvjgDto=new CnvjgDto();
                cnvjgDto.setCnvjgid(StringUtil.generateUUID());
                cnvjgDto.setWkbh(result.getString(2));
                cnvjgDto.setWkcxid(result.getString(3));
                cnvjgDto.setGchl(result.getString(1));
                cnvjgDto.setSjl(result.getString(4));
                cnvjgDto.setBdxls(result.getString(5));
                cnvjgDto.setUniqxls(result.getString(6));
                cnvjgDto.setWaviness(result.getString(7));
                if(StringUtil.isNotBlank(result.getString(8))&& !result.getString(8).contains("(")){
                    cnvjgDto.setKaryotype(result.getString(8));
                }else{
                    if(StringUtil.isNotBlank(result.getString(8))){
                        cnvjgDto.setKaryotype("'"+String.valueOf(result.getString(8)).trim()+"'");
                    }else{
                        cnvjgDto.setKaryotype("");
                    }

                }

                cnvjgDto.setXb(result.getString(9));
                cnvjgDto.setLrsj(result.getString(16));

                if("-1".equals(result.getString(13))){
                    cnvjgDto.setZszl("DAB0CC93E4954DBEAE3661A006F3A9DC");
                }else if("0".equals(result.getString(12))){
                    cnvjgDto.setZszl("B1E02DCF1BB24044B616C41C9DD1696C");
                }else if("1".equals(result.getString(12))){
                    cnvjgDto.setZszl("DB9ACDBBBDBA40E4BFD744FE96A0E681");
                }else if(StringUtil.isBlank(result.getString(12))){
                    cnvjgDto.setZszl("E4FCA8E180A0431CA6C7EA2FFAE80C89");
                }
                if(result.getString(13).equals("-1")){
                    cnvjgDto.setShjg("DD3561080F34421A836500EFAF6A14A1");
                }else if("0".equals(result.getString(13))){
                    cnvjgDto.setShjg("C80F0C99FF4740CA853763F141D21DC2");
                }else if("1".equals(result.getString(13))){
                    cnvjgDto.setShjg("4F5E9048331A4F5890F15BAC7B2CAF34");
                }else if("2".equals(result.getString(13))){
                    cnvjgDto.setShjg("E22E4C341DD9439D942BE779F295229B");
                }else if("3".equals(result.getString(13))){
                    cnvjgDto.setShjg("83D3E1FA188C49A2B1A0382510AD0C8A");
                }else if("4".equals(result.getString(13))){
                    cnvjgDto.setShjg("18F9B1D2FE5143848DEDCF34B3D531F9");
                }
                if("1".equals(result.getString(14))){
                    cnvjgDto.setPdyy("D3EB049EDD984C77A6DA53C261463136");
                }else if("2".equals(result.getString(14))){
                    cnvjgDto.setPdyy("2D46E0D32CEB4F0C8FBEE29EEBF5D284");
                }else if("3".equals(result.getString(14))){
                    cnvjgDto.setPdyy("C27F2D7AAC754E18AC0EAAC6176F6218");
                }else if("4".equals(result.getString(14))){
                    cnvjgDto.setPdyy("5BBEAE6F97C545998E5A9E11DB0FD954");
                }else if("5".equals(result.getString(14))){
                    cnvjgDto.setPdyy("1572FF3309D4401099A9A113A6BEDAB8");
                }
                BigDecimal aijg=new BigDecimal("0");
                if(StringUtil.isNotBlank(result.getString(10))){
                    aijg=new BigDecimal(result.getString(10));
                }
                BigDecimal bigDecimal=new BigDecimal("0.3");
                if(aijg.compareTo(bigDecimal)>=0){
                    cnvjgDto.setAijg("2F91096DE41447E09E1C08126607C3A9");
                }else{
                    cnvjgDto.setAijg("6E640E42A5CF43F2AD8C1D6032C26A4C");
                }


                cnvjgDto.setQcfs(result.getString(11));
                cnvjgDto.setOnresult(result.getString(10));
                cnvjgDto.setXpid(result.getString(15));
                list.add(cnvjgDto);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }finally {
            // 7、释放资源
            close(conn, preparedStatement, result);
        }
        return list;
    }

    public List<Map<String,String>> queryCnvjgxq(){
        List<Map<String,String>> list =new ArrayList<>();
        try {
            Class.forName(driverName);      // 2、注册数据库驱动
            conn = DriverManager.getConnection("jdbc:postgresql://172.17.60.197:5432/matridx", "matridx", "matridx2020!");    // 3、获取数据库连接
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        ResultSet result = null;
        String sql = "select cnvjg.cnvjgid,cnvjg.xpid,cnvresult.chr,cnvresult.zone_start,cnvresult.zone_end,cnvresult.cnv_type,cnvresult.mos, " +
                "cnvresult.chr_level_var,cnvresult.pos_start,cnvresult.pos_end,cnvresult.copy_num,cnvresult.result,cnvresult.detail,cnvresult.checked,cnvresult.manual,sample.create_date,cnvjg.wkcxid " +
                "from core_cnvresult cnvresult " +
                "LEFT JOIN core_sample sample ON sample.ID = cnvresult.sample_id " +
                "left join igams_cnvjg cnvjg on cnvjg.wkbh = sample.uid " +
                "where sample.create_date < '2022-09-01' and cnvjg.cnvjgid is not null";

        try {
            preparedStatement = conn.prepareStatement(sql);         // 4、执行预编译SQL操作
            result = preparedStatement.executeQuery();  // 5、执行查询操作
            while (result.next()) {
                Map<String,String>map=new HashMap<>();
                map.put("cnvjgxqid",StringUtil.generateUUID());
                map.put("cnvjgid",result.getString(1));
                map.put("rst",result.getString(3));
                map.put("qsqd",result.getString(4));
                map.put("zzqd",result.getString(5));
                map.put("cnvlx",result.getString(6));

                if("true".equals(result.getString(7))){
                    map.put("sfqh","1");
                }else if("false".equals(result.getString(7))){
                    map.put("sfqh","0");
                }
                if("result.getString(7)".equals(result.getString(8))){
                    map.put("sffzsb","1");
                }else if("false".equals(result.getString(8))){
                    map.put("sffzsb","0");
                }
                map.put("qswz",result.getString(9));
                map.put("zzwz",result.getString(10));
                map.put("kbs",result.getString(11));
                map.put("cnvjg",result.getString(13));
                map.put("cnvxq",result.getString(12));
                map.put("sfhb","0");
                map.put("xpid",result.getString(2));
                if("true".equals(result.getString(15))){
                    map.put("sfsdtj","1");
                }else if("false".equals(result.getString(15))){
                    map.put("sfsdtj","0");
                }
                map.put("lrsj",result.getString(16));
                map.put("scbj","0");
                map.put("wkcxid",result.getString(17));
                list.add(map);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }finally {
            // 7、释放资源
            close(conn, preparedStatement, result);
        }
        return list;
    }
    public List<Map<String,String>> queryXpxx(){
        List<Map<String,String>> list =new ArrayList<>();
        try {
            Class.forName(driverName);      // 2、注册数据库驱动
            conn = DriverManager.getConnection("jdbc:postgresql://172.17.60.197:5432/matridx", "matridx", "matridx2020!");    // 3、获取数据库连接
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        ResultSet result = null;
        String sql = "select  chip.uid as a,machine.code as b,jcsj.csid as c from core_chip chip left join  core_machine machine on chip.machine_id=machine.id and machine.index2_reverse ='t' left join matridx_jcsj jcsj on jcsj.csdm=machine.code and jcsj.csmc=machine.name  where machine.code is not null and chip.create_date < '2022-09-01'  and jcsj.scbj= '0' ";

        try {
            preparedStatement = conn.prepareStatement(sql);         // 4、执行预编译SQL操作
            result = preparedStatement.executeQuery();  // 5、执行查询操作
            while (result.next()) {
                Map<String,String>map=new HashMap<>();
                map.put("zjid",StringUtil.generateUUID());
                map.put("xpm",result.getString(1));
                map.put("code",result.getString(2));
                map.put("csid",result.getString(3));
                list.add(map);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }finally {
            // 7、释放资源
            close(conn, preparedStatement, result);
        }
        return list;
    }
    /**
     * 查询数据
     * @return  返回查询到结果
     */
    public  List<BioWzglDto> queryData(String date) {
        try {
            Class.forName(driverName);      // 2、注册数据库驱动
            conn = DriverManager.getConnection(url, userName, password);    // 3、获取数据库连接
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        ResultSet result = null;
        String sql = "select * from species_species where category != 'other' and to_char(last_update,'YYYY-MM-DD') >= '"+date+"'";      // 定义预编译SQL
        List<BioWzglDto> list =new ArrayList<>();
        try {
            preparedStatement = conn.prepareStatement(sql);         // 4、执行预编译SQL操作
            result = preparedStatement.executeQuery();  // 5、执行查询操作
            while (result.next()) {
                BioWzglDto bioWzglDto=new BioWzglDto();
                bioWzglDto.setWzid(result.getInt(1)+"");
                bioWzglDto.setWzywm(result.getString(2));
                bioWzglDto.setWzzwm(result.getString(3));
                bioWzglDto.setWzfl(result.getString(5));
                if(StringUtil.isNotBlank(result.getString(11))){
                    bioWzglDto.setWzlx(result.getString(11).equals("0")?"G-":"G+");
                }
                bioWzglDto.setWzzs(result.getString(7));
                bioWzglDto.setScbj("0");
                bioWzglDto.setFid(result.getInt(8)+"");
                bioWzglDto.setFldj(result.getString(13));
                bioWzglDto.setFl(result.getString(4).equals("core")?"0":"1");
                bioWzglDto.setBdlb(result.getString(12));
                bioWzglDto.setWzdl(result.getString(5));
                bioWzglDto.setZbx(result.getString(6));
                list.add(bioWzglDto);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }finally {
            // 7、释放资源
            close(conn, preparedStatement, result);
        }
        return list;
    }

    /**
     * 查询毒力因子注释数据
     * @return  返回查询到结果
     */
    public  List<BioDlyzzsDto> queryDlyzzsData() {
        try {
            Class.forName(driverName);      // 2、注册数据库驱动
            conn = DriverManager.getConnection(url, userName, password);    // 3、获取数据库连接
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        ResultSet result = null;
        String sql = "select * from species_virulencefactor";// 定义预编译SQL
        List<BioDlyzzsDto> list =new ArrayList<>();
        try {
            preparedStatement = conn.prepareStatement(sql);         // 4、执行预编译SQL操作
            result = preparedStatement.executeQuery();  // 5、执行查询操作
            while (result.next()) {
                BioDlyzzsDto bioDlyzzsDto=new BioDlyzzsDto();
                bioDlyzzsDto.setDljyid(result.getString(1));
                bioDlyzzsDto.setName(result.getString(2));
                bioDlyzzsDto.setVfid(result.getString(3));
                bioDlyzzsDto.setVfcategory(result.getString(4));
                bioDlyzzsDto.setComment(result.getString(5));
                bioDlyzzsDto.setData(result.getString(6));
                bioDlyzzsDto.setTaxid(result.getString(7));
                list.add(bioDlyzzsDto);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }finally {
            // 7、释放资源
            close(conn, preparedStatement, result);
        }
        return list;
    }

    /**
     * 查询注释信息数据
     * @return  返回查询到结果
     */
    public  List<ZsxxDto> queryZsxxData() {
        try {
            Class.forName(driverName);      // 2、注册数据库驱动
            conn = DriverManager.getConnection(url, userName, password);    // 3、获取数据库连接
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        ResultSet result = null;
        String sql = "select * from species_drugresistgene";      // 定义预编译SQL
        List<ZsxxDto> list =new ArrayList<>();
        try {
            preparedStatement = conn.prepareStatement(sql);         // 4、执行预编译SQL操作
            result = preparedStatement.executeQuery();  // 5、执行查询操作
            while (result.next()) {
                ZsxxDto zsxxDto=new ZsxxDto();
                zsxxDto.setZsid(result.getString(1));
                zsxxDto.setMc(result.getString(2));
                zsxxDto.setZs(result.getString(3));
                zsxxDto.setTaxids(result.getString(5));
                zsxxDto.setGlsrst(result.getString(6));
                zsxxDto.setKnwzly(result.getString(7));
                zsxxDto.setType("0");
                list.add(zsxxDto);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }finally {
            // 7、释放资源
            close(conn, preparedStatement, result);
        }
        return list;
    }

    public Map<String,List<WkcxbbDto>> queryWkcxbbDto(){
        try {
            Class.forName(driverName);      // 2、注册数据库驱动
            conn = DriverManager.getConnection(url1, userName, password);    // 3、获取数据库连接
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        ResultSet result = null;
        String sql = "select uid,total_reads,spike_count from core_sample where create_date >='2023-1-5'";      // 定义预编译SQL
        Map<String,List<WkcxbbDto>>map=new HashMap<>();
        List<WkcxbbDto> dna_list =new ArrayList<>();
        List<WkcxbbDto> other_list =new ArrayList<>();
        try {
            preparedStatement = conn.prepareStatement(sql);         // 4、执行预编译SQL操作
            result = preparedStatement.executeQuery();  // 5、执行查询操作
            while (result.next()) {
                WkcxbbDto wkcxbbDto=new WkcxbbDto();
                wkcxbbDto.setWkbh(result.getString(1));
                if(Long.parseLong(result.getString(3))!=0L){
                    BigDecimal bigDecimal1 = new BigDecimal(1000000);
                    BigDecimal decimal = new BigDecimal(result.getString(2));
                    BigDecimal sp_in = new BigDecimal(result.getString(3));
                    sp_in = sp_in.multiply(bigDecimal1).divide(decimal,5, RoundingMode.HALF_UP).setScale(3, RoundingMode.HALF_UP);
                    wkcxbbDto.setSpikeinrpm( sp_in.setScale(2, RoundingMode.HALF_UP).toString());

                }else{
                    wkcxbbDto.setSpikeinrpm("0");
                }

                if(wkcxbbDto.getWkbh().contains("DNA")){
                    dna_list.add(wkcxbbDto);
                }else{
                    other_list.add(wkcxbbDto);
                }

            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }finally {
            // 7、释放资源
            close(conn, preparedStatement, result);
        }
        map.put("dna",dna_list);
        map.put("other",other_list);
        return map;
    }

    /**
     * 查询注释信息数据
     * @return  返回查询到结果
     */
    public  List<XgwxDto> queryXgwxData(List<JcsjDto> jcsjDtos) {
        try {
            Class.forName(driverName);      // 2、注册数据库驱动
            conn = DriverManager.getConnection(url, userName, password);    // 3、获取数据库连接
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }
        ResultSet result = null;
        String sql = "select * from species_paper";      // 定义预编译SQL
        List<XgwxDto> list =new ArrayList<>();
        try {
            preparedStatement = conn.prepareStatement(sql);         // 4、执行预编译SQL操作
            result = preparedStatement.executeQuery();  // 5、执行查询操作
            while (result.next()) {
                XgwxDto xgwxDto=new XgwxDto();
                xgwxDto.setWxid(result.getString(1));
                xgwxDto.setBt(result.getString(3));
                xgwxDto.setTaxid(result.getString(4));
                if("t".equals(result.getString(5))){
                    xgwxDto.setMrwx("1");
                }else if("f".equals(result.getString(5))){
                    xgwxDto.setMrwx("0");
                }
                String yblxdm="";
                //0和other是其他，1和PB是外周血，2和CSF是脑脊液，3和BALF是肺泡灌洗液
                if("0".equals(result.getString(6))||"other".equals(result.getString(6))){
                    Optional<JcsjDto> optional = jcsjDtos.stream().filter(item -> "1".equals(item.getCskz1())).findFirst();
                    if (optional.isPresent()){
                        yblxdm=optional.get().getCsdm();
                    } else {
                        yblxdm="XXX";
                    }
                }
                if("1".equals(result.getString(6))||"PB".equals(result.getString(6))){
                    yblxdm="B";
                }
                if("2".equals(result.getString(6))||"CSF".equals(result.getString(6))){
                    yblxdm="N";
                }
                if("3".equals(result.getString(6))||"BALF".equals(result.getString(6))){
                    yblxdm="L";
                }
                for(JcsjDto dto:jcsjDtos){
                    if(yblxdm.equals(dto.getCsdm())){
                        xgwxDto.setYblx(dto.getCsid());
                        break;
                    }
                }
                xgwxDto.setZz(result.getString(7));
                xgwxDto.setQk(result.getString(8));
                list.add(xgwxDto);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            log.error(e.getMessage());
        }finally {
            // 7、释放资源
            close(conn, preparedStatement, result);
        }
        return list;
    }

    /**
     * 释放资源
     * @param conn  关闭数据库连接
     * @param statement 关闭数据库操作对象
     * @param rs    关闭结果集操作对象
     */
    public static void close(Connection conn, Statement statement, ResultSet rs) {
        if (Objects.nonNull(rs)) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (Objects.nonNull(statement)) {
            try {
                statement.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (Objects.nonNull(conn)) {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
